#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Markdown 代码块空行修复工具
专门用于修复代码块周围缺失的空行问题，符合MD031规则
"""

import re
import os
import glob
import argparse

def fix_code_block_spacing(file_path):
    """修复单个 Markdown 文件中代码块周围的空行"""
    try:
        # 读取文件
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        original_content = content
        
        # 按行处理
        lines = content.split('\n')
        result_lines = []
        i = 0
        changes_made = 0
        
        while i < len(lines):
            line = lines[i]
            
            # 检测代码块开始（支持缩进）
            if line.strip().startswith('```'):
                # 获取代码块的缩进
                indent = len(line) - len(line.lstrip())
                
                # 检查代码块前是否需要空行
                if (result_lines and 
                    result_lines[-1].strip() != '' and 
                    not result_lines[-1].strip().startswith('```')):
                    result_lines.append('')
                    changes_made += 1
                    print(f"  添加代码块前空行，行 {i+1}")
                
                # 添加代码块开始行
                result_lines.append(line)
                i += 1
                
                # 添加代码块内容，直到找到结束标记
                while i < len(lines):
                    current_line = lines[i]
                    result_lines.append(current_line)
                    
                    # 检查是否是代码块结束（考虑缩进）
                    if (current_line.strip().startswith('```') and 
                        len(current_line) - len(current_line.lstrip()) == indent):
                        break
                    i += 1
                
                # 检查代码块后是否需要空行
                if (i + 1 < len(lines) and 
                    lines[i + 1].strip() != '' and 
                    not lines[i + 1].strip().startswith('```')):
                    result_lines.append('')
                    changes_made += 1
                    print(f"  添加代码块后空行，行 {i+2}")
            else:
                result_lines.append(line)
            
            i += 1
        
        content = '\n'.join(result_lines)
        
        # 只有在内容发生变化时才写回文件
        if content != original_content:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(content)
            print(f"✅ 成功修复 {file_path} ({changes_made} 处修改)")
            return True
        else:
            print(f"⚠️  {file_path} 无需修复")
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
        print(f"\n📄 处理文件: {file_path}")
        if fix_code_block_spacing(file_path):
            processed_count += 1
    
    print(f"\n✨ 修复完成！共处理了 {processed_count}/{len(markdown_files)} 个文件")

def main():
    parser = argparse.ArgumentParser(description='Markdown代码块空行修复工具')
    parser.add_argument('path', nargs='?', 
                       default=r'e:\_ComputerLearning\3_Programming_OOP\Review',
                       help='要处理的目录路径或文件路径（默认: e:\_ComputerLearning\3_Programming_OOP\Review）')
    parser.add_argument('--file', '-f', action='store_true',
                       help='将路径视为单个文件而不是目录')
    
    args = parser.parse_args()
    
    print("🔧 Markdown 代码块空行修复工具")
    print("功能：")
    print("  • 确保代码块前后有空行（符合MD031规则）")
    print("  • 支持缩进的代码块")
    print("  • 自动检测代码块的缩进级别")
    print("-" * 60)
    
    if args.file or os.path.isfile(args.path):
        # 处理单个文件
        if args.path.lower().endswith(('.md', '.markdown', '.mdown', '.mkdn')):
            print(f"🎯 处理单个文件: {args.path}")
            fix_code_block_spacing(args.path)
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
