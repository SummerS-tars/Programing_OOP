同学你好，

遇到 “错误: 缺少 JavaFX 运行时组件, 需要使用该组件来运行此应用程序” 这个提示，即使在使用 `maven-shade-plugin` 打包后，通常意味着 Java 启动器在执行 JAR 包时，无法正确初始化 JavaFX 环境。这在主类直接继承自 `javafx.application.Application` 时尤为常见。

一个经过验证且常用的解决方案是：**创建一个不直接继承 `javafx.application.Application` 的新 “启动器” 主类。**

这个新的启动器类将包含 `public static void main(String[] args)` 方法，并在该方法内部调用你实际 JavaFX 应用主类的 `launch` 方法。然后，`maven-shade-plugin`（以及 `maven-jar-plugin`）中的 `Main-Class` 指向这个新的启动器类。

让我们一步步来解决这个问题：

### 1. 查看你当前的 `Main.java`
首先，我需要确认一下你 `top.thesumst.Main` 类的结构。它是否直接 `extends Application`？

*(我在这里本想获取 `top.thesumst.Main.java` 的内容，但工具调用出了一点问题。不过，根据标准的 JavaFX 应用结构，我假设你的 `top.thesumst.Main` 类看起来大致是这样的：)*

```java
package top.thesumst;

import javafx.application.Application;
import javafx.stage.Stage;
// ... 其他 import

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 你的 JavaFX 应用启动逻辑
        // 例如:
        // Parent root = FXMLLoader.load(getClass().getResource("your-fxml.fxml"));
        // primaryStage.setTitle("My JavaFX App");
        // primaryStage.setScene(new Scene(root, 300, 275));
        // primaryStage.show();
        System.out.println("JavaFX Application Start Method Called with args: " + getParameters().getRaw());
        // 假设你的 GUIView 处理了 Stage 的显示
        top.thesumst.view.gui.GUIView.launchGUI(primaryStage, getParameters().getRaw().toArray(new String[0]));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
```

### 2. 创建一个新的启动器主类 (Launcher)

如果你的 `Main.java` 确实是直接继承 `Application`，请创建一个新的 Java 类，例如 `top.thesumst.Launcher`：

**`src/main/java/top/thesumst/Launcher.java`**:
```java
package top.thesumst;

public class Launcher {
    public static void main(String[] args) {
        // 调用你原始的 JavaFX Application 主类的 launch 方法
        // Main.main(args) 也可以，或者直接 Application.launch(Main.class, args)
        top.thesumst.Main.launch(top.thesumst.Main.class, args);
    }
}
```

**重要说明：**
你命令行中使用了 `java -jar .\target\project-6.0.jar gui`，这表明你希望将参数 `gui` 传递给你的应用程序。上面的 `Launcher` 会将 `args` 传递给 `Main.launch`，这样你的 JavaFX 应用在其 `init()` 或 `start()` 方法中通过 `getParameters().getRaw()` 就可以获取到这些参数。

在你的 `Main.java` 的 `start` 方法中，你似乎期望通过 `getParameters().getRaw()` 获取参数。
改为 `top.thesumst.view.gui.GUIView.launchGUI(primaryStage, getParameters().getRaw().toArray(new String[0]));` 可能更合适，以确保参数被传递。

### 3. 修改 `pom.xml`

现在，你需要更新 `pom.xml` 文件，将 `<mainClass>` 属性指向这个新的 `Launcher` 类：

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>23</maven.compiler.source>
        <maven.compiler.target>23</maven.compiler.target>
        <javafx.version>21</javafx.version>
        <javafx.maven.plugin.version>0.0.8</javafx.maven.plugin.version>
        <mainClass>top.thesumst.Launcher</mainClass>
    </properties>

    <dependencies>
      <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.9.2</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.11</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-controls</artifactId>
            <version>${javafx.version}</version>
        </dependency>
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-fxml</artifactId>
            <version>${javafx.version}</version>
        </dependency>
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-base</artifactId>
            <version>${javafx.version}</version>
        </dependency>
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-graphics</artifactId>
            <version>${javafx.version}</version>
        </dependency>
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.10.1</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.8.0</version>
                <configuration>
                    <source>${maven.compiler.source}</source>
                    <target>${maven.compiler.target}</target>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.openjfx</groupId>
                <artifactId>javafx-maven-plugin</artifactId>
                <version>${javafx.maven.plugin.version}</version>
                <configuration>
                    <mainClass>${mainClass}</mainClass>
                </configuration>
            </plugin>
            <plugin>
                <artifactId>maven-jar-plugin</artifactId>
                <version>3.0.2</version>
                <configuration>
                    <archive>
                        <manifest>
                            <mainClass>${mainClass}</mainClass> </manifest>
                    </archive>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-shade-plugin</artifactId>
                <version>3.2.4</version>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>shade</goal>
                        </goals>
                        <configuration>
                            <transformers>
                                <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                    <mainClass>${mainClass}</mainClass> </transformer>
                            </transformers>
                            <filters>
                                <filter>
                                    <artifact>*:*</artifact>
                                    <excludes>
                                        <exclude>META-INF/*.SF</exclude>
                                        <exclude>META-INF/*.DSA</exclude>
                                        <exclude>META-INF/*.RSA</exclude>
                                    </excludes>
                                </filter>
                            </filters>
                        </configuration>
                    </execution>
                </executions>
            </plugin>
        </plugins>
        <pluginManagement>
          <plugins>
                <plugin>
                    <artifactId>maven-clean-plugin</artifactId>
                    <version>3.1.0</version>
                </plugin>
                <plugin>
                    <artifactId>maven-resources-plugin</artifactId>
                    <version>3.0.2</version>
                </plugin>
                <plugin>
                    <artifactId>maven-surefire-plugin</artifactId>
                    <version>2.22.1</version>
                </plugin>
                <plugin>
                    <artifactId>maven-install-plugin</artifactId>
                    <version>2.5.2</version>
                </plugin>
                <plugin>
                    <artifactId>maven-deploy-plugin</artifactId>
                    <version>2.8.2</version>
                </plugin>
                <plugin>
                    <artifactId>maven-site-plugin</artifactId>
                    <version>3.7.1</version>
                </plugin>
                <plugin>
                    <artifactId>maven-project-info-reports-plugin</artifactId>
                    <version>3.0.0</version>
                </plugin>
            </plugins>
        </pluginManagement>
    </build>
</project>
```

**关键修改点**：

* 在 `<properties>` 中，将 `<mainClass>` 的值从 `top.thesumst.Main` 修改为 `top.thesumst.Launcher`。
* `javafx-maven-plugin` 和 `maven-jar-plugin` 以及 `maven-shade-plugin` 中的 `ManifestResourceTransformer` 现在都会使用 `top.thesumst.Launcher` 作为主类。

### 4. 重新打包和运行

1.  **创建 `Launcher.java` 文件** 并将上述代码粘贴进去。
2.  **修改 `pom.xml`** 如上所示。
3.  **清理并重新打包项目**:
    ```bash
    mvn clean package
    ```
4.  **运行 JAR 包**:
    ```bash
    java -jar target/project-6.0.jar gui
    ```
    或者，如果你没有参数：
    ```bash
    java -jar target/project-6.0.jar
    ```

这个方法通过引入一个不直接依赖 JavaFX `Application`类的中间启动器，通常能解决 "缺少 JavaFX 运行时组件" 的问题，因为它允许 JavaFX 环境在 `Application.launch()` 被调用时正确初始化。

请尝试以上步骤，如果还有问题，请告诉我具体的错误信息。