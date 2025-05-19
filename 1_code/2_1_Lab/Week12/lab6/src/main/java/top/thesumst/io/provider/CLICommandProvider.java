package top.thesumst.io.provider;

import java.util.Scanner;

import top.thesumst.type.Event;

public class CLICommandProvider extends BaseCommandProvider
{
    private Scanner scanner;

    public CLICommandProvider()
    {
        super(CommandProviderMode.CLI);
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void getNextCommand()
    {
        if(inputBuffer == null) {
            if(scanner.hasNextLine()) {
                inputBuffer = scanner.nextLine();
            }
        }
    }

    @Override
    public boolean hasCommand()
    {
        return inputBuffer != null || scanner.hasNextLine();
    }

    @Override
    public void close()
    {
        try {
            scanner.close();
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
            Event event = provider.getEvent(provider.inputBuffer);
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
            event.getHandleSuccess() == null ? "null" : event.getHandleSuccess().toString(),
            event.getMessage() == null ? "null" : event.getMessage());
        return string;
    }
}