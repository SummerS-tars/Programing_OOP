#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Markdown 列表缩进修复工具
专门用于修复 MD007 规则错误：
- 将错误的 2 空格缩进修正为 0 空格（顶级列表项）
- 将错误的 6 空格缩进修正为 4 空格（嵌套列表项）
- 保持正确的 4 空格嵌套缩进不变
"""

import re
import os
import glob
import argparse

def fix_list_indentation(file_path):
    """修复单个 Markdown 文件中的列表缩进问题"""
    try:
        # 读取文件
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        original_content = content
        
        # 按行处理
        lines = content.split('\n')
        result_lines = []
        changes_made = 0
        
        for i, line in enumerate(lines):
            current_line = line
            
            # 检测列表项（无序列表：- * +，有序列表：数字.）
            list_match = re.match(r'^(\s*)([*\-+]|\d+\.)\s+', line)
            
            if list_match:
                indent_spaces = list_match.group(1)
                list_marker = list_match.group(2)
                indent_count = len(indent_spaces)
                
                # MD007 规则修复：
                # 2 空格 -> 0 空格（顶级列表）
                if indent_count == 2:
                    new_line = line[2:]  # 移除前 2 个空格
                    result_lines.append(new_line)
                    changes_made += 1
                    print(f"  行 {i+1}: 修复 2 空格缩进 -> 0 空格")
                
                # 6 空格 -> 4 空格（嵌套列表）
                elif indent_count == 6:
                    new_line = line[2:]  # 移除前 2 个空格
                    result_lines.append(new_line)
                    changes_made += 1
                    print(f"  行 {i+1}: 修复 6 空格缩进 -> 4 空格")
                
                # 其他缩进保持不变（0, 4, 8 等是正确的）
                else:
                    result_lines.append(current_line)
            else:
                # 非列表行保持不变
                result_lines.append(current_line)
        
        content = '\n'.join(result_lines)
        
        # 只有在内容发生变化时才写回文件
        if content != original_content:
            with open(file_path, 'w', encoding='utf-8') as f:
                f.write(content)
            print(f"✅ 成功修复 {file_path} ({changes_made} 处修改)")
            return True
        else:
            print(f"⚠️  {file_path} 列表缩进无需修复")
            return False
        
    except Exception as e:
        print(f"❌ 处理文件 {file_path} 时出错: {e}")
        return False

def analyze_file_indentation(file_path):
    """分析文件中的列表缩进情况"""
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            lines = f.readlines()
        
        print(f"\n📊 分析文件: {file_path}")
        indentation_stats = {}
        problems = []
        
        for i, line in enumerate(lines):
            list_match = re.match(r'^(\s*)([*\-+]|\d+\.)\s+', line)
            if list_match:
                indent_count = len(list_match.group(1))
                list_marker = list_match.group(2)
                
                if indent_count not in indentation_stats:
                    indentation_stats[indent_count] = 0
                indentation_stats[indent_count] += 1
                
                # 检测问题缩进
                if indent_count == 2:
                    problems.append(f"行 {i+1}: 2 空格缩进 (应为 0)")
                elif indent_count == 6:
                    problems.append(f"行 {i+1}: 6 空格缩进 (应为 4)")
                elif indent_count not in [0, 4, 8, 12]:  # 常见的正确缩进
                    problems.append(f"行 {i+1}: {indent_count} 空格缩进 (非标准)")
        
        print("缩进统计:")
        for indent, count in sorted(indentation_stats.items()):
            status = "✅" if indent in [0, 4, 8, 12] else "❌"
            print(f"  {status} {indent} 空格: {count} 项")
        
        if problems:
            print(f"\n发现 {len(problems)} 个缩进问题:")
            for problem in problems[:10]:  # 只显示前10个
                print(f"  {problem}")
            if len(problems) > 10:
                print(f"  ... 还有 {len(problems) - 10} 个问题")
        else:
            print("✅ 未发现缩进问题")
            
        return len(problems)
        
    except Exception as e:
        print(f"❌ 分析文件 {file_path} 时出错: {e}")
        return 0

def find_markdown_files(directory):
    """递归查找目录中的所有Markdown文件"""
    markdown_files = []
    
    # 支持的Markdown文件扩展名
    extensions = ['*.md', '*.markdown', '*.mdown', '*.mkdn']
    
    for extension in extensions:
        pattern = os.path.join(directory, '**', extension)
        markdown_files.extend(glob.glob(pattern, recursive=True))
    
    return sorted(markdown_files)

def process_directory(directory, analyze_only=False):
    """处理目录中的所有Markdown文件"""
    print(f"🔍 正在搜索目录: {directory}")
    
    markdown_files = find_markdown_files(directory)
    
    if not markdown_files:
        print(f"📂 在目录 {directory} 中未找到任何Markdown文件")
        return
    
    print(f"📋 找到 {len(markdown_files)} 个Markdown文件")
    
    if analyze_only:
        print("\n🔍 分析列表缩进...")
        total_problems = 0
        for file_path in markdown_files:
            problems = analyze_file_indentation(file_path)
            total_problems += problems
        print(f"\n📊 总计发现 {total_problems} 个缩进问题")
    else:
        print("\n🚀 开始修复列表缩进...")
        processed_count = 0
        
        for file_path in markdown_files:
            if fix_list_indentation(file_path):
                processed_count += 1
        
        print(f"\n✨ 修复完成！共处理了 {processed_count}/{len(markdown_files)} 个文件")

def main():
    parser = argparse.ArgumentParser(description='Markdown列表缩进修复工具')
    parser.add_argument('path', nargs='?', 
                       default=r'e:\_ComputerLearning\3_Programming_OOP\Review',
                       help='要处理的目录路径或文件路径（默认: e:\_ComputerLearning\3_Programming_OOP\Review）')
    parser.add_argument('--file', '-f', action='store_true',
                       help='将路径视为单个文件而不是目录')
    parser.add_argument('--analyze', '-a', action='store_true',
                       help='仅分析缩进问题，不进行修复')
    
    args = parser.parse_args()
    
    print("📐 Markdown 列表缩进修复工具")
    print("功能包括：")
    print("  • 修复 MD007 规则错误")
    print("  • 2 空格缩进 -> 0 空格（顶级列表）")
    print("  • 6 空格缩进 -> 4 空格（嵌套列表）")
    print("  • 保持正确的 0, 4, 8 空格缩进不变")
    print("-" * 50)
    
    if args.file or os.path.isfile(args.path):
        # 处理单个文件
        if args.path.lower().endswith(('.md', '.markdown', '.mdown', '.mkdn')):
            print(f"🎯 处理单个文件: {args.path}")
            if args.analyze:
                analyze_file_indentation(args.path)
            else:
                fix_list_indentation(args.path)
        else:
            print(f"❌ 文件 {args.path} 不是Markdown文件")
    else:
        # 处理目录
        if os.path.isdir(args.path):
            process_directory(args.path, args.analyze)
        else:
            print(f"❌ 路径 {args.path} 不存在或不是有效的目录/文件")

if __name__ == "__main__":
    main()
