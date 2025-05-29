package top.thesumst.view.gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * GUI控制器类，负责处理MainGameView.fxml的UI控件
 */
public class GUIController implements Initializable {

    @FXML
    private StackPane chessboardContainer;

    @FXML
    private GridPane rightInfoPane; // 根据FXML中右侧容器的fx:id命名，如果不同请修改

    /**
     * 初始化方法，在FXML加载完成后自动调用
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // MOTD已通过Alert显示，此处不再需要设置Label
        // 可以在这里添加对chessboardContainer和rightInfoPane的额外初始化代码（如果需要）
    }
}
