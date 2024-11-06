import org.example.ManVSAi;
import org.example.ResultWaitingForm;
import org.example.SerialCommunicator;
import org.example.gui.Man;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ManVSAiTest {

    private SerialCommunicator mockCommunicator;
    private ManVSAi game;
    private Runnable mockOnGameEnd;

    @BeforeEach
    public void setUp() {
        // Ініціалізація моків перед кожним тестом
        mockCommunicator = mock(SerialCommunicator.class);

        game = Mockito.mock(ManVSAi.class);
        game.setCommunicator(mockCommunicator);
        mockOnGameEnd = mock(Runnable.class);
    }


    @Test
    public void testStartWaitingForResults_CallsOnGameEndListener() {
        // Підготовка
        ResultWaitingForm mockResultWaitingForm = mock(ResultWaitingForm.class); // Мок для ResultWaitingForm

        // Дії
        game.startWaitingForResults(mockOnGameEnd); // Запуск очікування результатів

        // Налаштування мокованого ResultWaitingForm для виклику слухача завершення гри
        doAnswer(invocation -> {
            Runnable listener = invocation.getArgument(0); // Отримуємо слухача
            if (listener != null) {
                listener.run(); // Викликаємо слухача, якщо він не null
            }
            return null;
        }).when(mockResultWaitingForm).setOnGameEndListener(any());

        // Виклик слухача
        mockResultWaitingForm.setOnGameEndListener(mockOnGameEnd); // Встановлюємо слухача на завершення гри

        // Перевірка: впевнюємося, що слухач завершення гри був викликаний
        verify(mockOnGameEnd).run(); // Перевіряємо, чи викликався слухач
    }
}
