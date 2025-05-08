package top.thesumst;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SimpleJavaFXApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 创建一个按钮
        Button btn = new Button();
        btn.setText("点我!");

        // 设置按钮的点击事件处理
        btn.setOnAction(event -> {
            System.out.println("按钮被点击了!");
        });

        // 创建一个布局面板 (这里使用 StackPane 将按钮居中)
        StackPane root = new StackPane();
        root.getChildren().add(btn);

        // 创建一个场景，并将布局面板放入场景中
        Scene scene = new Scene(root, 300, 250);

        // 设置舞台（窗口）的标题
        primaryStage.setTitle("我的第一个 JavaFX 应用");
        // 将场景设置到舞台
        primaryStage.setScene(scene);
        // 显示舞台
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args); // 启动 JavaFX 应用
    }
}