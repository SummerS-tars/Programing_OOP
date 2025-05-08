#
# PowerShell 7 Emoji 显示设置脚本
# 这个脚本可以帮助配置PowerShell 7以正确显示emoji和Unicode字符
#

# 显示当前PowerShell版本信息
Write-Host "PowerShell版本信息:" -ForegroundColor Cyan
$PSVersionTable

# 确保输出编码为UTF-8
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8

# 检查字体设置
Write-Host "`n当前控制台字体信息:" -ForegroundColor Cyan
$fontInfo = [System.Runtime.InteropServices.Marshal]::PtrToStructure(
    [System.Console]::GetCurrentConsoleFont($false),
    [Type][System.ConsoleAPI.CONSOLE_FONT_INFO]
)

Write-Host "`n字体建议:" -ForegroundColor Yellow
Write-Host "1. 建议使用 'Cascadia Code' 或 'Cascadia Mono' (Windows Terminal默认字体)" 
Write-Host "2. 其他支持emoji的字体: 'Consolas', 'JetBrains Mono', 'Fira Code'"
Write-Host "3. 可以在终端右键菜单 -> 属性 -> 字体 中更改字体"

# 测试emoji显示
Write-Host "`n======== 基本Emoji显示测试 ========" -ForegroundColor Green

$emojis = @(
    "🎂 生日蛋糕",
    "✨ 闪烁",
    "🎉 庆祝",
    "🖥️ 电脑",
    "🐛 Bug",
    "🏃 运动",
    "💇 理发",
    "🍚 米饭",
    "🌙 月亮",
    "🎊 庆祝",
    "⭐ 星星",
    "(╯°□°)╯︵ ┻━┻ 掀桌"
)

foreach ($emoji in $emojis) {
    Write-Host $emoji
}

Write-Host "`n======== ANSI颜色+Emoji测试 ========" -ForegroundColor Green

# ANSI颜色代码
$RED = "`e[31m"
$GREEN = "`e[32m"
$YELLOW = "`e[33m"
$BLUE = "`e[34m"
$MAGENTA = "`e[35m"
$CYAN = "`e[36m"
$RESET = "`e[0m"

Write-Host "$RED红色🍎$RESET"
Write-Host "$GREEN绿色🥦$RESET"
Write-Host "$YELLOW黄色🍋$RESET"
Write-Host "$BLUE蓝色🔵$RESET"
Write-Host "$MAGENTA洋红色🌸$RESET"
Write-Host "$CYAN青色🌊$RESET"

Write-Host "`n======== 解决方案 ========" -ForegroundColor Cyan

Write-Host "如果看不到emoji，请尝试以下解决方案:" -ForegroundColor Yellow
Write-Host "1. 确保使用PowerShell 7 (不是旧版PowerShell 5.1)"
Write-Host "2. 安装并使用Windows Terminal应用 (Microsoft Store可下载)"
Write-Host "3. 设置支持Unicode的字体，如Cascadia Code"
Write-Host "4. 添加以下命令到你的PowerShell配置文件 (执行 notepad `$PROFILE):"
Write-Host "   [Console]::OutputEncoding = [System.Text.Encoding]::UTF8" -ForegroundColor Cyan

Write-Host "`n按任意键退出..." -ForegroundColor Magenta
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")
