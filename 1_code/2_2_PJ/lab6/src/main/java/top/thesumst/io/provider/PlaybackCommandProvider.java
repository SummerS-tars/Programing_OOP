package top.thesumst.io.provider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; // 引入 Collectors

import top.thesumst.type.Event;

public class PlaybackCommandProvider extends CLICommandProvider 
{
    private final List<String> rawCommands;

    // 构造函数现在接收文件名，而不是完整的文件系统路径
    public PlaybackCommandProvider(String scriptFileName) throws IOException 
    {
        super(CommandProviderMode.PLAYBACK);

        // 构建相对于类路径根的资源路径
        // 假设你的脚本在 resources/top/thesumst/scripts/ 目录下
        String resourcePath = "/top/thesumst/scripts/" + scriptFileName;

        // 使用 PlaybackCommandProvider 类的类加载器获取资源
        InputStream inputStream = PlaybackCommandProvider.class.getResourceAsStream(resourcePath);

        if (inputStream == null)
            throw new IOException("无法在类路径中找到资源: " + resourcePath);

        // 从 InputStream 读取所有行
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            this.rawCommands = reader.lines().collect(Collectors.toList());
        }
    }

    public List<Event> getEvents() {
        List<Event> events = new ArrayList<>();
        for (String rawCommand : rawCommands) {
            // 确保 CLICommandProvider 中的 inputBuffer 是可以被子类访问和设置的
            // 如果 inputBuffer 是 private 的，你可能需要一个 protected 的 setter 方法
            // 或者修改 CLICommandProvider.getEvent() 的行为以接受参数
            inputBuffer = rawCommand; // 假设有这样一个方法，或者 inputBuffer 是 protected
            events.add(super.getEvent());
        }
        return events;
    }
}

// PlaybackCommandProviderTestDrive 中的调用也需要相应修改
class PlaybackCommandProviderTestDrive {
    public static void main(String[] args) {
        try {
            // 现在传递的是脚本文件名，例如 "peace.cmd"
            // 确保 peace.cmd 位于 src/main/resources/top/thesumst/scripts/ 目录下
            PlaybackCommandProvider playbackCommandProvider = new PlaybackCommandProvider("peace.cmd");
            List<Event> events = playbackCommandProvider.getEvents();
            for (Event event : events) {
                System.out.println(event);
            }
        } catch (IOException e) {
            System.err.println("错误: " + e.getMessage());
            e.printStackTrace(); // 打印堆栈跟踪以获取更多信息
        }
    }
}
