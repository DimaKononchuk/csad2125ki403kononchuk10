import com.fazecast.jSerialComm.SerialPort;
import org.example.SerialCommunicator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



public class CommunicationTest {

    private SerialPort mockSerialPort;
    private SerialCommunicator serialCommunicator;

    @BeforeEach
    void setUp() {
        mockSerialPort = mock(SerialPort.class);
        serialCommunicator = new SerialCommunicator(mockSerialPort);
    }

    @Test
    void openPortTest() {
        // Arrange
        when(mockSerialPort.openPort()).thenReturn(true);

        // Act
        boolean result = serialCommunicator.openPort();

        // Assert
        assertTrue(result, "Port should be opened successfully.");
        verify(mockSerialPort).setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);
        verify(mockSerialPort).setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 1000);
        verify(mockSerialPort).openPort();
    }

    @Test
    void closePortTest() {
        // Arrange
        when(mockSerialPort.isOpen()).thenReturn(true);

        // Act
        serialCommunicator.closePort();

        // Assert
        verify(mockSerialPort).closePort();
    }
    @Test
    void readMessageTest() throws InterruptedException {
        // Arrange
        when(mockSerialPort.isOpen()).thenReturn(true);
        byte[] testData = "Response from Arduino".getBytes(StandardCharsets.UTF_8);
        when(mockSerialPort.readBytes(any(byte[].class), anyInt())).thenAnswer(invocation -> {
            byte[] buffer = invocation.getArgument(0);
            System.arraycopy(testData, 0, buffer, 0, testData.length);
            return testData.length;
        });

        // Act
        String result = serialCommunicator.readMessage();

        // Assert
        assertEquals("Response from Arduino", result);
    }
    @Test
    void sendMessageTest() {
        // Arrange
        when(mockSerialPort.isOpen()).thenReturn(true);
        when(mockSerialPort.writeBytes(any(byte[].class), anyInt())).thenReturn(1);
        String message = "Test message";

        // Act
        boolean result = serialCommunicator.sendMessage(message);

        // Assert
        assertTrue(result, "Message should be sent successfully.");
        verify(mockSerialPort).writeBytes(message.getBytes(StandardCharsets.UTF_8), message.length());
    }


}
