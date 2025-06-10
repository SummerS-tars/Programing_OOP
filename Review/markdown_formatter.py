#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Markdown格式修复工具
修复以下格式问题：
1. 标题后添加空行
2. 列表前后添加空行
3. 代码块前后添加空行
"""

import re
import os
import glob
import argparse

def fix_markdown_formatting(content):
    """修复Markdown格式"""
    lines = content.split('\n')
    fixed_lines = []
    i = 0
    
    while i < len(lines):
        current_line = lines[i]
        fixed_lines.append(current_line)
        
        # 1. 处理标题后的空行
        if re.match(r'^#{1,6}\s+', current_line):
            # 如果下一行不是空行且存在，则添加空行
            if i + 1 < len(lines) and lines[i + 1].strip() != '':
                fixed_lines.append('')
        
        # 2. 处理列表前后的空行
        elif is_list_item(current_line):
            # 检查列表前是否需要空行
            if (len(fixed_lines) >= 2 and 
                fixed_lines[-2].strip() != '' and 
                not is_list_item(fixed_lines[-2]) and
                not re.match(r'^#{1,6}\s+', fixed_lines[-2])):
                # 在当前行前插入空行
                fixed_lines.insert(-1, '')
            
            # 继续处理列表项，直到列表结束
            while i + 1 < len(lines) and (is_list_item(lines[i + 1]) or lines[i + 1].strip() == ''):
                i += 1
                fixed_lines.append(lines[i])
            
            # 检查列表后是否需要空行
            if (i + 1 < len(lines) and 
                lines[i + 1].strip() != '' and 
                not is_list_item(lines[i + 1])):
                fixed_lines.append('')
        
        # 3. 处理代码块前后的空行
        elif current_line.strip().startswith('```'):
            # 代码块开始
            if current_line.strip() == '```' or re.match(r'^```\w+', current_line.strip()):
                # 检查代码块前是否需要空行
                if (len(fixed_lines) >= 2 and 
                    fixed_lines[-2].strip() != '' and 
                    not re.match(r'^#{1,6}\s+', fixed_lines[-2])):
                    fixed_lines.insert(-1, '')
                
                # 继续处理代码块内容，直到结束标记
                while i + 1 < len(lines):
                    i += 1
                    fixed_lines.append(lines[i])
                    if lines[i].strip() == '```':
                        break
                
                # 检查代码块后是否需要空行
                if (i + 1 < len(lines) and 
                    lines[i + 1].strip() != ''):
                    fixed_lines.append('')
        
        i += 1
    
    return '\n'.join(fixed_lines)

def is_list_item(line):
    """判断是否为列表项"""
    stripped = line.strip()
    if not stripped:
        return False
    
    # 有序列表：数字. 或 数字)
    if re.match(r'^\d+[.)]\s+', stripped):
        return True
    
    # 无序列表：- * +
    if re.match(r'^[-*+]\s+', stripped):
        return True
    
    # 带缩进的列表项
    if re.match(r'^\s+[-*+]\s+', line):
        return True
    
    if re.match(r'^\s+\d+[.)]\s+', line):
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
        formatted_content = fix_markdown_formatting(content)
        
        # 清理多余的连续空行（超过2个连续空行合并为2个）
        formatted_content = re.sub(r'\n{4,}', '\n\n\n', formatted_content)
        
        # 确保文件结尾有换行符
        if not formatted_content.endswith('\n'):
            formatted_content += '\n'
        
        # 只有在内容发生变化时才写回文件
        if formatted_content != original_content:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(formatted_content)
            print(f"✅ 成功格式化 {file_path}")
            return True
        else:
            print(f"⚠️  {file_path} 格式已正确，无需修改")
            return False
        
    except Exception as e:
        print(f"❌ 格式化文件 {file_path} 时出错: {e}")
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
    
    print("\n🚀 开始格式化文件...")
    processed_count = 0
    
    for file_path in markdown_files:
        if format_markdown_file(file_path):
            processed_count += 1
    
    print(f"\n✨ 格式化完成！共处理了 {processed_count}/{len(markdown_files)} 个文件")

def main():
    parser = argparse.ArgumentParser(description='修复Markdown文件格式')
    parser.add_argument('path', nargs='?', 
                       default=r'e:\_ComputerLearning\3_Programming_OOP\Review',
                       help='要处理的目录路径或文件路径（默认: e:\_ComputerLearning\3_Programming_OOP\Review）')
    parser.add_argument('--file', '-f', action='store_true',
                       help='将路径视为单个文件而不是目录')
    
    args = parser.parse_args()
    
    if args.file or os.path.isfile(args.path):
        # 处理单个文件
        if args.path.lower().endswith(('.md', '.markdown', '.mdown', '.mkdn')):
            print(f"🎯 格式化单个文件: {args.path}")
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
