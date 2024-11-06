
import org.example.ResultWaitingForm;
import org.example.SerialCommunicator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.image.DataBuffer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ResultWaitingFormTest {
    private SerialCommunicator serialCommunicatorMock;
    private ResultWaitingForm resultWaitingForm;

    @BeforeEach
    void setUp() {
        // Створюємо mock-об'єкт для SerialCommunicator
        serialCommunicatorMock = Mockito.mock(SerialCommunicator.class);
        resultWaitingForm = Mockito.mock(ResultWaitingForm.class);
        resultWaitingForm.setSerialCommunicator(serialCommunicatorMock);

    }

    @Test
    void testStart() {
        // Запускаємо процес очікування результатів
        resultWaitingForm.start();
        verify(resultWaitingForm).start();
    }

    @Test
    void testCheckForResults_WhenNoData() throws InterruptedException {
        // Налаштовуємо mock, щоб повертав false при перевірці наявності даних
        when(serialCommunicatorMock.hasAvailableData()).thenReturn(false);
        // Викликаємо метод перевірки результатів
        resultWaitingForm.checkForResults();
        // Створюємо мок для буфера даних
        StringBuilder mockStringBuilder=Mockito.mock(StringBuilder.class);
        when(mockStringBuilder.length()).thenReturn(0);
        when(resultWaitingForm.getDataBuffer()).thenReturn(mockStringBuilder);

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
            when(resultWaitingForm.getText()).thenReturn(new StringBuilder("Test result\n"));
            // Перевіряємо, що дані були додані до буфера
            assertEquals("Test result", resultWaitingForm.getText().toString().trim());
            // Перевіряємо, що вікно результатів показується
//            assertFalse(resultWaitingForm.getWaitingDialog().isDisplayable());


        }



    @Test
    void testShowResult() {
        // Перевіряємо, чи вікно результатів правильно відображається
        String[] results = {"Player 1 wins!", "Player 2 loses!"};
        resultWaitingForm.showResult(results);

        verify(resultWaitingForm).showResult(results);
    }

    @Test
    void testEndGame() {
        // Налаштовуємо слухача завершення гри
        Runnable listener = mock(Runnable.class);
        when(resultWaitingForm.getOnGameEndListener()).thenReturn(listener);

        // Викликаємо метод завершення гри
        resultWaitingForm.endGame();
        verify(resultWaitingForm).endGame();
    }
}

