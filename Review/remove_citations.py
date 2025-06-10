#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
移除Markdown文件中的引用标签
支持处理指定目录及其子目录中的所有Markdown文件
"""

import re
import os
import glob
import argparse

def remove_citations_from_file(file_path):
    """移除单个文件中的 [cite_start] 和 [cite: num] 标签"""
    try:
        # 读取文件
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # 检查是否包含引用标签
        if '[cite_start]' not in content and '[cite:' not in content:
            print(f"跳过 {file_path} - 未发现引用标签")
            return False
            
        original_content = content
        
        # 移除 [cite_start] 标签
        content = re.sub(r'\[cite_start\]', '', content)
          # 移除 [cite: num] 标签（数字可能有多位）
        content = re.sub(r' \[cite: \d+(?:, \d+)*\]', '', content)
        
        # 清理可能产生的多余空格，但保持行首的缩进（用于列表等格式）
        # 只清理行中间的多余空格，不影响行首的缩进
        lines = content.split('\n')
        cleaned_lines = []
        for line in lines:
            # 保持行首缩进，只清理行中间的多余空格
            if line.strip():  # 非空行
                # 找到行首的缩进
                indent = len(line) - len(line.lstrip())
                # 清理行内容中的多余空格，但保持缩进
                cleaned_content = re.sub(r'  +', ' ', line[indent:])
                cleaned_lines.append(line[:indent] + cleaned_content)
            else:  # 空行
                cleaned_lines.append(line)
        content = '\n'.join(cleaned_lines)
        
        # 只有在内容发生变化时才写回文件
        if content != original_content:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(content)
            print(f"✅ 成功移除 {file_path} 中的引用标签")
            return True
        else:
            print(f"⚠️  {file_path} 中未发现可移除的引用标签")
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
    
    print("\n🚀 开始处理文件...")
    processed_count = 0
    
    for file_path in markdown_files:
        if remove_citations_from_file(file_path):
            processed_count += 1
    
    print(f"\n✨ 处理完成！共处理了 {processed_count}/{len(markdown_files)} 个文件")

def main():
    parser = argparse.ArgumentParser(description='移除Markdown文件中的引用标签')
    parser.add_argument('path', nargs='?', 
                       default=r'e:\_ComputerLearning\3_Programming_OOP',
                       help='要处理的目录路径或文件路径（默认: e:\_ComputerLearning\3_Programming_OOP）')
    parser.add_argument('--file', '-f', action='store_true',
                       help='将路径视为单个文件而不是目录')
    
    args = parser.parse_args()
    
    if args.file or os.path.isfile(args.path):
        # 处理单个文件
        if args.path.lower().endswith(('.md', '.markdown', '.mdown', '.mkdn')):
            print(f"🎯 处理单个文件: {args.path}")
            remove_citations_from_file(args.path)
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
