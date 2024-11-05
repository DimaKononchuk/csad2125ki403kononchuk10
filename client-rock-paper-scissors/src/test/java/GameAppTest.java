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
        gameApp=new GameApp();
        gameApp.setSerialCommunicator(mockSerialCommunicator);
        gameApp.setMode(mockMode);

    }

    @Test
    void testGameAppInitialization() {
        // Запускаємо вибір режиму
        gameApp.startModeSelection();
        // Викликаємо метод openPort, щоб перевірити, чи він викликаний
        gameApp.getSerialCommunicator().openPort();

        // Перевіряємо, що openPort був викликаний один раз
        verify(mockSerialCommunicator, times(1)).openPort();

        assertTrue(gameApp.getMode().isVisible(), "The mode selection window should be visible.");
    }

    @Test
    void testHandleModeSelection_ManVsAI() {

        // Налаштуємо мок, щоб повернути значення "Man vs AI" при виклику getModes()
        when(mockMode.getModes()).thenReturn("Man vs AI");

        // Викликаємо handleModeSelection
        gameApp.handleModeSelection();

        // Перевіряємо, що sendMessage() був викликаний з правильним параметром
        verify(mockSerialCommunicator).sendMessage("Man vs AI\n");
    }
    @Test
    void testHandleModeSelection_ManVsMan() {

        // Налаштуємо мок, щоб повернути значення "Man vs Man" при виклику getModes()
        when(mockMode.getModes()).thenReturn("Man vs Man");

        // Викликаємо handleModeSelection
        gameApp.handleModeSelection();

        // Перевіряємо, що sendMessage() був викликаний з правильним параметром
        verify(mockSerialCommunicator).sendMessage("Man vs Man\n");
    }

    @Test
    void testOnGameEnd() {

        // Запускаємо вибір режиму
        gameApp.onGameEnd();
        // Викликаємо метод openPort, щоб перевірити, чи він викликаний
        gameApp.getSerialCommunicator().closePort();

        // Перевіряємо, що openPort був викликаний один раз
        verify(mockSerialCommunicator, times(1)).closePort();

        assertFalse(gameApp.getMode().isVisible(), "The mode selection window should be visible.");
    }
}
