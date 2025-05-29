package top.thesumst.view;

import top.thesumst.observer.Observer;
import top.thesumst.view.console.CLIView;
import top.thesumst.view.gui.GUIView;

public class ViewFactory 
{
    public static Observer getView(String viewType)
    {
        if(viewType.equals("cli")) return new CLIView();
        else if(viewType.equals("gui")) return new GUIView();
        else throw new IllegalArgumentException("启动模式错误: " + viewType);
    }
}
