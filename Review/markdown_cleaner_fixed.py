#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Markdown 文件综合清理工具
整合了以下功能：
1. 移除引用标签 ([cite_start] 和 [cite: num])
2. 修复格式问题（标题后空行、列表前后空行、代码块前后空行）
3. 清理多余的连续空行（保留最多一个空行）
4. 保持正确的列表缩进
5. 统一无序列表符号（将星号*转换为破折号-）
"""

import re
import os
import glob
import argparse

def clean_markdown_file(file_path):
    """清理单个 Markdown 文件"""
    try:
        # 读取文件
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        original_content = content
          # 1. 移除引用标签
        # 移除 [cite_start] 标签
        content = re.sub(r'\[cite_start\]', '', content)
        
        # 移除 [cite: num] 标签（数字可能有多位），可能有前导空格也可能没有
        content = re.sub(r' ?\[cite: \d+(?:, \d+)*\]', '', content)
        
        # 2. 按行处理，修复格式和间距问题
        lines = content.split('\n')
        cleaned_lines = []
        
        for i, line in enumerate(lines):
            current_line = line
            
            # 转换无序列表符号：将星号*转换为破折号-
            if re.match(r'^(\s*)\*(\s+)', current_line):
                current_line = re.sub(r'^(\s*)\*(\s+)', r'\1-\2', current_line)
            
            # 清理行内的多余空格，但保持行首缩进（用于列表等格式）
            if current_line.strip():  # 非空行
                # 找到行首的缩进
                indent = len(current_line) - len(current_line.lstrip())
                # 清理行内容中的多余空格，但保持缩进
                cleaned_content = re.sub(r'  +', ' ', current_line[indent:])
                current_line = current_line[:indent] + cleaned_content
            
            cleaned_lines.append(current_line)
        
        # 3. 处理空行和格式问题
        final_lines = []
        i = 0
        
        while i < len(cleaned_lines):
            line = cleaned_lines[i]
            
            # 如果是标题
            if line.strip().startswith('#'):
                final_lines.append(line)
                
                # 标题后确保有一个空行（除非文件结束）
                if i + 1 < len(cleaned_lines) and cleaned_lines[i + 1].strip() != '':
                    final_lines.append('')
            
            # 如果是列表项
            elif re.match(r'^\s*[*\-+]\s+', line) or re.match(r'^\s*\d+\.\s+', line):
                # 列表前确保有空行（除非是文件开始或前一行已经是空行）
                if final_lines and final_lines[-1].strip() != '':
                    final_lines.append('')
                
                final_lines.append(line)
                
                # 查找列表的结束
                j = i + 1
                while j < len(cleaned_lines):
                    next_line = cleaned_lines[j]
                    # 如果是列表项或缩进内容，继续
                    if (re.match(r'^\s*[*\-+]\s+', next_line) or 
                        re.match(r'^\s*\d+\.\s+', next_line) or
                        (next_line.strip() != '' and len(next_line) - len(next_line.lstrip()) > 0)):
                        final_lines.append(next_line)
                        j += 1
                    else:
                        break
                
                # 列表后确保有空行（除非文件结束）
                if j < len(cleaned_lines) and cleaned_lines[j].strip() != '':
                    final_lines.append('')
                
                i = j - 1
            
            # 如果是代码块开始
            elif line.strip().startswith('```'):
                # 代码块前确保有空行
                if final_lines and final_lines[-1].strip() != '':
                    final_lines.append('')
                
                final_lines.append(line)
                
                # 添加代码块内容直到结束
                i += 1
                while i < len(cleaned_lines):
                    final_lines.append(cleaned_lines[i])
                    if cleaned_lines[i].strip().startswith('```'):
                        break
                    i += 1
                
                # 代码块后确保有空行（除非文件结束）
                if i + 1 < len(cleaned_lines) and cleaned_lines[i + 1].strip() != '':
                    final_lines.append('')
            
            # 普通行
            else:
                final_lines.append(line)
            
            i += 1
        
        # 4. 清理多余的连续空行（最多保留一个空行）
        result_lines = []
        prev_empty = False
        
        for line in final_lines:
            if line.strip() == '':  # 空行
                if not prev_empty:
                    result_lines.append(line)
                    prev_empty = True
            else:  # 非空行
                result_lines.append(line)
                prev_empty = False
        
        # 移除文件末尾的空行
        while result_lines and result_lines[-1].strip() == '':
            result_lines.pop()
        
        content = '\n'.join(result_lines)
        
        # 只有在内容发生变化时才写回文件
        if content != original_content:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(content)
            print(f"✅ 成功清理 {file_path}")
            return True
        else:
            print(f"⚠️  {file_path} 无需清理")
            return False
        
    except Exception as e:
        print(f"❌ 处理文件 {file_path} 时出错: {e}")
        return False

def find_markdown_files(directory):
    """递归查找目录中的所有Markdown文件"""
    markdown_files = []
    
    # 支持的Markdown文件扩展名
    extensions = ['*.md', '*.markdown', '*.mdown', '*.mkdn']
    
    for extension in extensions:
        pattern = os.path.join(directory, '**', extension)
        markdown_files.extend(glob.glob(pattern, recursive=True))
    
    return sorted(markdown_files)

def process_directory(directory):
    """处理目录中的所有Markdown文件"""
    print(f"🔍 正在搜索目录: {directory}")
    
    markdown_files = find_markdown_files(directory)
    
    if not markdown_files:
        print(f"📂 在目录 {directory} 中未找到任何Markdown文件")
        return
    
    print(f"📋 找到 {len(markdown_files)} 个Markdown文件:")
    for file_path in markdown_files:
        print(f"   - {file_path}")
    
    print("\n🚀 开始清理文件...")
    processed_count = 0
    
    for file_path in markdown_files:
        if clean_markdown_file(file_path):
            processed_count += 1
    
    print(f"\n✨ 清理完成！共处理了 {processed_count}/{len(markdown_files)} 个文件")

def main():
    parser = argparse.ArgumentParser(description='Markdown文件综合清理工具')
    parser.add_argument('path', nargs='?', 
                       default=r'e:\_ComputerLearning\3_Programming_OOP\Review',
                       help='要处理的目录路径或文件路径（默认: e:\_ComputerLearning\3_Programming_OOP\Review）')
    parser.add_argument('--file', '-f', action='store_true',
                       help='将路径视为单个文件而不是目录')
    
    args = parser.parse_args()
    
    print("🧹 Markdown 综合清理工具")
    print("功能包括：")
    print("  • 移除引用标签 ([cite_start] 和 [cite: num])")
    print("  • 修复格式问题（标题、列表、代码块的空行）")
    print("  • 清理多余的连续空行")
    print("  • 保持正确的列表缩进")
    print("  • 统一无序列表符号（将星号*转换为破折号-）")
    print("-" * 50)
    
    if args.file or os.path.isfile(args.path):
        # 处理单个文件
        if args.path.lower().endswith(('.md', '.markdown', '.mdown', '.mkdn')):
            print(f"🎯 处理单个文件: {args.path}")
            clean_markdown_file(args.path)
        else:
            print(f"❌ 文件 {args.path} 不是Markdown文件")
    else:
        # 处理目录
        if os.path.isdir(args.path):
            process_directory(args.path)
        else:
            print(f"❌ 路径 {args.path} 不存在或不是有效的目录/文件")

if __name__ == "__main__":
    main()
