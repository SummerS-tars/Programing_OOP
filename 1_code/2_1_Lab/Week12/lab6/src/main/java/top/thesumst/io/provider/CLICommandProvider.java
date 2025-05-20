package top.thesumst.io.provider;

import java.util.Scanner;

import top.thesumst.tools.ScannerTools;
import top.thesumst.type.Event;

public class CLICommandProvider extends BaseCommandProvider
{
    private Scanner scanner;
    private boolean isOpen;

    public CLICommandProvider()
    {
        super(CommandProviderMode.CLI);
        this.isOpen = false;
    }

    @Override
    public void getNextCommand()
    {
        try {
            if(!isOpen || scanner == null)
                throw new IllegalStateException("命令提供器未打开");
            if(inputBuffer == null)
            {
                inputBuffer = scanner.nextLine();
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public boolean hasCommand()
    {
        return inputBuffer != null || scanner.hasNextLine();
    }

    @Override
    public void open()
    {
        if(scanner == null || !isOpen) {
            try {
                scanner = ScannerTools.getScanner();
                isOpen = true;
            } catch (Exception e) {
                System.err.println("打开扫描器时出错: " + e.getMessage());
                isOpen = false;
            }
        }
    }

    @Override
    public void close()
    {
        try {
            if(scanner != null && isOpen) {
                scanner = null;
                isOpen = false;
            }
        } catch (Exception e) {
            System.err.println("关闭扫描器时出错: " + e.getMessage());
        }
    }
}

class CLICommandProviderTestDrive
{
    public static void main(String[] args) {
        CLICommandProvider provider = new CLICommandProvider();
        provider.getNextCommand();
        if(provider.hasCommand()) {
            Event event = provider.getEvent();
            System.out.println(eventToString(event));
        }
    }

    private static String eventToString(Event event)
    {
        String string = String.format("Event: %s\nRawCommand: %s\nCommand: %s\nData: %s\nHandleSuccess: %s\nMessage: %s\n",
            event.getType(),
            event.getRawCommand(),
            event.getCommand() == null ? "null" : event.getCommand().getClass().getSimpleName(),
            event.getData() == null ? "null" : event.getData().toString(),
            event.getState() == null ? "null" : event.getState().toString(),
            event.getMessage() == null ? "null" : event.getMessage());
        return string;
    }
}