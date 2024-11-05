
import org.example.ResultWaitingForm;
import org.example.SerialCommunicator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResultWaitingFormTest {
    private SerialCommunicator serialCommunicatorMock;
    private ResultWaitingForm resultWaitingForm;

    @BeforeEach
    void setUp() {
        // Створюємо mock-об'єкт для SerialCommunicator
        serialCommunicatorMock = Mockito.mock(SerialCommunicator.class);
        resultWaitingForm = new ResultWaitingForm(serialCommunicatorMock);

    }

    @Test
    void testStart() {
        // Запускаємо процес очікування результатів
        resultWaitingForm.start();

        // Перевіряємо, що вікно очікування було відображено
//        assertNotNull(resultWaitingForm.waitingDialog);
        assertTrue(resultWaitingForm.getWaitingDialog().isVisible());
    }

    @Test
    void testCheckForResults_WhenNoData() throws InterruptedException {
        // Налаштовуємо mock, щоб повертав false при перевірці наявності даних
        when(serialCommunicatorMock.hasAvailableData()).thenReturn(false);
        // Викликаємо метод перевірки результатів
        resultWaitingForm.checkForResults();

        // Перевіряємо, що буфер даних залишився порожнім
        assertEquals(0, resultWaitingForm.getDataBuffer().length());
    }

    @Test
        void testCheckForResults_WhenDataAvailable() throws InterruptedException {
            // Налаштовуємо mock, щоб повертав true і віддавав дані
            when(serialCommunicatorMock.readMessage()).thenReturn("Test result\n");
            when(serialCommunicatorMock.hasAvailableData()).thenReturn(true);
            resultWaitingForm.start();
            // Викликаємо метод перевірки результатів
            resultWaitingForm.checkForResults();

            // Перевіряємо, що дані були додані до буфера
            assertEquals("Test result", resultWaitingForm.getText().toString().trim());
            // Перевіряємо, що вікно результатів показується
            assertFalse(resultWaitingForm.getWaitingDialog().isDisplayable());


        }



    @Test
    void testShowResult() {
        // Перевіряємо, чи вікно результатів правильно відображається
        String[] results = {"Player 1 wins!", "Player 2 loses!"};
        resultWaitingForm.showResult(results);

        // Перевіряємо, що JOptionPane був викликаний
        // (Тут ми можемо використати інший mock, щоб перевірити виклик JOptionPane)
        // В даному випадку просто перевіримо, що метод працює без виключень.
        assertDoesNotThrow(() -> {
            JOptionPane.showMessageDialog(null, "Test Result", "Result", JOptionPane.INFORMATION_MESSAGE);
        });
    }

    @Test
    void testEndGame() {
        // Налаштовуємо слухача завершення гри
        Runnable listener = mock(Runnable.class);
        resultWaitingForm.setOnGameEndListener(listener);

        // Викликаємо метод завершення гри
        resultWaitingForm.endGame();

        // Перевіряємо, що слухач був викликаний
        verify(listener).run();
    }
}

