#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
简单的Markdown格式修复工具
如果有两个连续的空行，删除一个
"""

import os
import glob
import argparse

def fix_double_blank_lines(file_path):
    """修复文件中的双空行问题"""
    try:
        # 读取文件
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        original_content = content
        
        # 将两个连续的换行符替换为一个
        # \n\n\n -> \n\n (三个换行符变成两个)
        # 这样可以保留一个空行，但删除多余的空行
        while '\n\n\n' in content:
            content = content.replace('\n\n\n', '\n\n')
        
        # 只有在内容发生变化时才写回文件
        if content != original_content:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(content)
            print(f"✅ 修复了 {file_path} 中的多余空行")
            return True
        else:
            print(f"⚠️  {file_path} 中没有发现多余空行")
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
    
    print(f"\n🚀 开始修复多余空行...")
    processed_count = 0
    
    for file_path in markdown_files:
        if fix_double_blank_lines(file_path):
            processed_count += 1
    
    print(f"\n✨ 处理完成！共修复了 {processed_count}/{len(markdown_files)} 个文件")

def main():
    parser = argparse.ArgumentParser(description='修复Markdown文件中的多余空行')
    parser.add_argument('path', nargs='?', 
                       default=r'e:\_ComputerLearning\3_Programming_OOP\Review',
                       help='要处理的目录路径或文件路径（默认: e:\_ComputerLearning\3_Programming_OOP\Review）')
    parser.add_argument('--file', '-f', action='store_true',
                       help='将路径视为单个文件而不是目录')
    
    args = parser.parse_args()
    
    if args.file or os.path.isfile(args.path):
        # 处理单个文件
        if args.path.lower().endswith(('.md', '.markdown', '.mdown', '.mkdn')):
            print(f"🎯 处理单个文件: {args.path}")
            fix_double_blank_lines(args.path)
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
