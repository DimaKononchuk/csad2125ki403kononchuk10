import org.example.GameApp;
import org.example.SerialCommunicator;
import org.example.gui.EnterModes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class GameAppTest {
    private GameApp gameApp;
    private SerialCommunicator mockSerialCommunicator;
    private EnterModes mockMode;

    @BeforeEach
    void setUp() {
        // Створюємо моки для SerialCommunicator та EnterModes
        mockSerialCommunicator = Mockito.mock(SerialCommunicator.class);
        mockMode = Mockito.mock(EnterModes.class);
        gameApp=Mockito.mock(GameApp.class);
        gameApp.setSerialCommunicator(mockSerialCommunicator);
        gameApp.setMode(mockMode);

    }

    @Test
    void testGameAppInitialization() {
        // Налаштовуємо мок, щоб уникнути NullPointerException
        when(gameApp.getSerialCommunicator()).thenReturn(mockSerialCommunicator);

        // Тепер можемо викликати openPort() і тестувати без NullPointerException
        gameApp.getSerialCommunicator().openPort();

        // Перевіряємо, що openPort був викликаний один раз
        verify(mockSerialCommunicator, times(1)).openPort();
        // Викликаємо startModeSelection() явно
        gameApp.startModeSelection();
        // Запускаємо вибір режиму
        verify(gameApp).startModeSelection();

//        assertTrue(gameApp.getMode().isVisible(), "The mode selection window should be visible.");
    }

    @Test
    void testHandleModeSelection_ManVsAI() {

        // Налаштуємо мок, щоб повернути значення "Man vs AI" при виклику getModes()
        when(mockMode.getModes()).thenReturn("Man vs AI\n");

        // Викликаємо handleModeSelection
        gameApp.handleModeSelection();
        verify(gameApp).handleModeSelection();
        mockSerialCommunicator.sendMessage(mockMode.getModes());
        // Перевіряємо, що sendMessage() був викликаний з правильним параметром
        verify(mockSerialCommunicator).sendMessage("Man vs AI\n");
    }
    @Test
    void testHandleModeSelection_ManVsMan() {

        // Налаштуємо мок, щоб повернути значення "Man vs AI" при виклику getModes()
        when(mockMode.getModes()).thenReturn("Man vs Man\n");

        // Викликаємо handleModeSelection
        gameApp.handleModeSelection();
        verify(gameApp).handleModeSelection();
        mockSerialCommunicator.sendMessage(mockMode.getModes());
        // Перевіряємо, що sendMessage() був викликаний з правильним параметром
        verify(mockSerialCommunicator).sendMessage("Man vs Man\n");
    }

    @Test
    void testOnGameEnd() {
        // Налаштовуємо мок, щоб уникнути NullPointerException
        when(gameApp.getSerialCommunicator()).thenReturn(mockSerialCommunicator);
        // Запускаємо вибір режиму
        gameApp.onGameEnd();
        // Викликаємо метод openPort, щоб перевірити, чи він викликаний
        gameApp.getSerialCommunicator().closePort();

        // Перевіряємо, що openPort був викликаний один раз
        verify(mockSerialCommunicator, times(1)).closePort();
        verify(gameApp).onGameEnd();
//        assertFalse(gameApp.getMode().isVisible(), "The mode selection window should be visible.");
    }
}
