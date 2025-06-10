#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Markdown 标题重复编号清理工具
专门用于清理标题中的重复编号，例如：
- "### 2.1. 2.1 信息的二进制表示" -> "### 2.1. 信息的二进制表示"
- "#### 3.2.1. 3.2.1 寄存器操作" -> "#### 3.2.1. 寄存器操作"
"""

import re
import os
import glob
import argparse

def clean_duplicate_numbers_in_file(file_path):
    """清理单个 Markdown 文件中标题的重复编号"""
    try:
        # 读取文件
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        original_content = content
        
        # 按行处理
        lines = content.split('\n')
        cleaned_lines = []
        
        for line in lines:
            # 匹配标题行中的重复编号模式
            # 例如: "### 2.1. 2.1 信息的二进制表示" 
            # 模式: (#+\s+)(\d+(?:\.\d+)*\.)\s+(\d+(?:\.\d+)*)\s+(.+)
            match = re.match(r'^(#+\s+)(\d+(?:\.\d+)*\.)\s+(\d+(?:\.\d+)*)\s+(.+)$', line)
            
            if match:
                header_prefix = match.group(1)  # "### "
                first_number = match.group(2)   # "2.1."
                second_number = match.group(3)  # "2.1"
                title_text = match.group(4)     # "信息的二进制表示"
                
                # 检查第一个编号（去掉末尾的点）是否与第二个编号相同
                first_number_no_dot = first_number.rstrip('.')
                if first_number_no_dot == second_number:
                    # 重复编号，删除第二个编号
                    cleaned_line = f"{header_prefix}{first_number} {title_text}"
                    cleaned_lines.append(cleaned_line)
                    print(f"  修复: {line.strip()}")
                    print(f"  ->   {cleaned_line.strip()}")
                else:
                    # 不是重复编号，保持原样
                    cleaned_lines.append(line)
            else:
                # 不是标题行或不符合重复编号模式，保持原样
                cleaned_lines.append(line)
        
        content = '\n'.join(cleaned_lines)
        
        # 只有在内容发生变化时才写回文件
        if content != original_content:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(content)
            print(f"✅ 成功清理 {file_path}")
            return True
        else:
            print(f"⚠️  {file_path} 无重复编号需要清理")
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
        print(f"\n📄 处理文件: {file_path}")
        if clean_duplicate_numbers_in_file(file_path):
            processed_count += 1
    
    print(f"\n✨ 清理完成！共处理了 {processed_count}/{len(markdown_files)} 个文件")

def main():
    parser = argparse.ArgumentParser(description='Markdown标题重复编号清理工具')
    parser.add_argument('path', nargs='?', 
                       default=r'e:\_ComputerLearning\3_Programming_OOP\Review',
                       help='要处理的目录路径或文件路径（默认: e:\_ComputerLearning\3_Programming_OOP\Review）')
    parser.add_argument('--file', '-f', action='store_true',
                       help='将路径视为单个文件而不是目录')
    
    args = parser.parse_args()
    
    print("🧹 Markdown 标题重复编号清理工具")
    print("功能：")
    print("  • 清理标题中的重复编号（如 '### 2.1. 2.1 标题' -> '### 2.1. 标题'）")
    print("  • 保留正确的编号格式（带点的格式）")
    print("-" * 60)
    
    if args.file or os.path.isfile(args.path):
        # 处理单个文件
        if args.path.lower().endswith(('.md', '.markdown', '.mdown', '.mkdn')):
            print(f"🎯 处理单个文件: {args.path}")
            clean_duplicate_numbers_in_file(args.path)
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
