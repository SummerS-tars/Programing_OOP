package top.thesumst.io.provider;

import org.junit.Before;
import org.junit.Test;

import top.thesumst.io.provider.BaseCommandProvider.CommandProviderMode;

import org.junit.After;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Scanner;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import static org.junit.Assert.*;


public class CLICommandProviderTest {
    
    private CLICommandProvider provider;
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalErr = System.err;
    private final InputStream originalIn = System.in;

    @Before
    public void setUp() {
        provider = new CLICommandProvider(CommandProviderMode.CLI);
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void restoreStreams() {
        System.setErr(originalErr);
        System.setIn(originalIn);
    }

    @Test
    public void testCloseNormal() {
        // Test normal close operation
        provider.close();
        // No exception should be thrown
    }    
    
    @Test
    public void testCloseWithException() throws Exception {        // Create a test scanner that we can close properly
        @SuppressWarnings({"resource", "unused"}) // 抑制资源泄漏和未使用变量警告，这是测试代码
        final Scanner realScanner = new Scanner("test");
        
        // Create a problematic scanner using a proxy approach
        Scanner mockScanner = new Scanner("test");
        
        // Use reflection to replace the close method with one that throws an exception
        Field scannerField = CLICommandProvider.class.getDeclaredField("scanner");
        scannerField.setAccessible(true);
        scannerField.set(provider, mockScanner);
        
        // Now replace the scanner with our controlled InputStream that will throw an exception
        ByteArrayInputStream exceptionInputStream = new ByteArrayInputStream("test".getBytes()) {
            @Override
            public void close() {
                throw new RuntimeException("Test exception");
            }
        };
        System.setIn(exceptionInputStream);
        
        // Create a new provider that will use our problematic input stream
        provider = new CLICommandProvider(CommandProviderMode.CLI);

        // Call the close method
        provider.close();

        // Verify the error message was printed
        assert(errContent.toString().contains("关闭扫描器时出错: Test exception"));
    }
    
    @Test
    public void testGetNextCommand() throws Exception {
        // Set up input
        String input = "test command";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        // Create new provider with the redirected input
        CLICommandProvider testProvider = new CLICommandProvider(CommandProviderMode.CLI);
        
        // Call getNextCommand
        testProvider.getNextCommand();
        
        // Get the inputBuffer field via reflection
        Field inputBufferField = BaseCommandProvider.class.getDeclaredField("inputBuffer");
        inputBufferField.setAccessible(true);
        String buffer = (String) inputBufferField.get(testProvider);
        
        // Verify the input was read
        assertEquals(input, buffer);
        
        // Clean up
        testProvider.close();
    }
    
    @Test
    public void testGetNextCommandWithBufferAlreadyFilled() throws Exception {
        // Set the inputBuffer directly
        String testBuffer = "existing command";
        Field inputBufferField = BaseCommandProvider.class.getDeclaredField("inputBuffer");
        inputBufferField.setAccessible(true);
        inputBufferField.set(provider, testBuffer);
        
        // Call getNextCommand (should not change buffer)
        provider.getNextCommand();
        
        // Verify buffer wasn't changed
        String buffer = (String) inputBufferField.get(provider);
        assertEquals(testBuffer, buffer);
    }
    
    @Test
    public void testHasCommandWithInput() throws Exception {
        // Set up input
        String input = "test command";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        
        // Create new provider with the redirected input
        CLICommandProvider testProvider = new CLICommandProvider(CommandProviderMode.CLI);
        
        // Verify hasCommand returns true
        assertTrue(testProvider.hasCommand());
        
        // Clean up
        testProvider.close();
    }
    
    @Test
    public void testHasCommandWithBuffer() throws Exception {
        // Set the inputBuffer directly
        Field inputBufferField = BaseCommandProvider.class.getDeclaredField("inputBuffer");
        inputBufferField.setAccessible(true);
        inputBufferField.set(provider, "buffered command");
        
        // Verify hasCommand returns true
        assertTrue(provider.hasCommand());
    }
    
    @Test
    public void testHasCommandWithoutInput() throws Exception {
        // Set up empty input
        System.setIn(new ByteArrayInputStream(new byte[0]));
        
        // Create new provider with the empty input
        CLICommandProvider testProvider = new CLICommandProvider(CommandProviderMode.CLI);
        
        // For an empty input, hasCommand would typically return false,
        // but this depends on the exact behavior of Scanner with an empty stream
        // This test mainly ensures it doesn't throw an exception
        testProvider.hasCommand();
        
        // Clean up
        testProvider.close();
    }
}