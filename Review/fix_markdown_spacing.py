#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
修复 Markdown 文件中特定的格式问题：
1. 标题后添加空行
2. 列表前后添加空行
3. 代码块前后添加空行
专门解决列表后面和标题前面添加多余空行的问题
"""

import re
import os
import glob
import argparse

def fix_markdown_spacing(content):
    """修复Markdown格式间距"""
    lines = content.split('\n')
    result_lines = []
    
    i = 0
    while i < len(lines):
        current_line = lines[i]
        
        # 检查当前行是否是标题
        if re.match(r'^#{1,6}\s+', current_line):
            result_lines.append(current_line)
            
            # 标题后添加空行（如果下一行不是空行）
            if i + 1 < len(lines) and lines[i + 1].strip() != '':
                result_lines.append('')
        
        # 检查当前行是否是列表项
        elif is_list_item(current_line):
            # 检查列表前是否需要空行
            if (result_lines and 
                result_lines[-1].strip() != '' and 
                not is_list_item(result_lines[-1]) and
                not re.match(r'^#{1,6}\s+', result_lines[-1])):
                result_lines.append('')
            
            # 添加当前列表项
            result_lines.append(current_line)
            
            # 收集完整的列表块
            j = i + 1
            while j < len(lines):
                next_line = lines[j]
                if is_list_item(next_line):
                    result_lines.append(next_line)
                    j += 1
                elif next_line.strip() == '':
                    # 空行，暂时跳过，稍后决定是否需要
                    j += 1
                else:
                    # 非列表内容，列表结束
                    break
            
            # 检查列表后是否需要空行
            if (j < len(lines) and 
                lines[j].strip() != '' and 
                not re.match(r'^#{1,6}\s+', lines[j])):
                result_lines.append('')
            
            i = j - 1  # 设置索引，因为外层循环会 +1
        
        # 检查当前行是否是代码块开始
        elif current_line.strip().startswith('```'):
            # 代码块前添加空行（如果前一行不是空行）
            if result_lines and result_lines[-1].strip() != '':
                result_lines.append('')
            
            # 添加代码块开始行
            result_lines.append(current_line)
            
            # 添加代码块内容直到结束
            j = i + 1
            while j < len(lines):
                result_lines.append(lines[j])
                if lines[j].strip() == '```':
                    # 代码块后添加空行（如果下一行不是空行）
                    if j + 1 < len(lines) and lines[j + 1].strip() != '':
                        result_lines.append('')
                    break
                j += 1
            
            i = j  # 设置索引
        
        else:
            # 普通行，直接添加
            result_lines.append(current_line)
        
        i += 1
    
    # 清理连续的多个空行，最多保留两个连续空行
    final_lines = []
    empty_count = 0
    
    for line in result_lines:
        if line.strip() == '':
            empty_count += 1
            if empty_count <= 2:  # 最多保留两个连续空行
                final_lines.append(line)
        else:
            empty_count = 0
            final_lines.append(line)
    
    return '\n'.join(final_lines)

def is_list_item(line):
    """判断是否为列表项"""
    if not line.strip():
        return False
    
    # 无序列表：- * +
    if re.match(r'^\s*[-*+]\s+', line):
        return True
    
    # 有序列表：数字. 或 数字)
    if re.match(r'^\s*\d+[.)]\s+', line):
        return True
    
    return False

def format_markdown_file(file_path):
    """格式化单个Markdown文件"""
    try:
        # 读取文件
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        original_content = content
        
        # 修复格式
        formatted_content = fix_markdown_spacing(content)
        
        # 确保文件结尾有换行符
        if not formatted_content.endswith('\n'):
            formatted_content += '\n'
        
        # 只有在内容发生变化时才写回文件
        if formatted_content != original_content:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(formatted_content)
            print(f"✅ 成功修复 {file_path}")
            return True
        else:
            print(f"⚠️  {file_path} 格式已正确，无需修改")
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
    
    print("\n🚀 开始修复文件...")
    processed_count = 0
    
    for file_path in markdown_files:
        if format_markdown_file(file_path):
            processed_count += 1
    
    print(f"\n✨ 修复完成！共处理了 {processed_count}/{len(markdown_files)} 个文件")

def main():
    parser = argparse.ArgumentParser(description='修复Markdown文件格式间距问题')
    parser.add_argument('path', nargs='?', 
                       default=r'e:\_ComputerLearning\3_Programming_OOP\Review',
                       help='要处理的目录路径或文件路径')
    parser.add_argument('--file', '-f', action='store_true',
                       help='将路径视为单个文件而不是目录')
    
    args = parser.parse_args()
    
    if args.file or os.path.isfile(args.path):
        # 处理单个文件
        if args.path.lower().endswith(('.md', '.markdown', '.mdown', '.mkdn')):
            print(f"🎯 修复单个文件: {args.path}")
            format_markdown_file(args.path)
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
