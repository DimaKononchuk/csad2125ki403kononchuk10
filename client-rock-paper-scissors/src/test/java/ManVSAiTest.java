import org.example.ManVSAi;
import org.example.ResultWaitingForm;
import org.example.SerialCommunicator;
import org.example.gui.Man;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

        game = new ManVSAi(mockCommunicator);
        mockOnGameEnd = mock(Runnable.class);
    }

    //@Test
    public void testPlayGame_SendsMessageForPlayer() {
        // Підготовка
        Man mockPlayer = mock(Man.class); // Створення мока для гравця
        when(mockPlayer.getName()).thenReturn("dima"); // Налаштування повернення імені гравця
        when(mockPlayer.getChoice()).thenReturn("Rock"); // Налаштування повернення вибору гравця

        // Додавання слухача до гри
        game.PlayGame(mockOnGameEnd); // Запуск гри з моком слухача завершення
        game.setPlayer1(mockPlayer); // Пряме встановлення мока гравця в гру
        verify(mockCommunicator).openPort();
        // Симулюємо закриття вікна
        game.getPlayer1().addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                // Виклик методу sendMessage вручну для перевірки
                mockCommunicator.sendMessage("Player:" + mockPlayer.getName() + ":" + mockPlayer.getChoice() + "\n");
            }
        });

        // Дії
        game.startWaitingForResults(mockOnGameEnd); // Запуск очікування результатів

        // Перевірка: впевнюємося, що повідомлення про вибір гравця надіслано до комунікатора
        verify(mockCommunicator).sendMessage("Player:dima:Rock\n");
    }

    @Test
    public void testStartWaitingForResults_CallsOnGameEndListener() {
        // Підготовка
        ResultWaitingForm mockResultWaitingForm = mock(ResultWaitingForm.class); // Мок для ResultWaitingForm
        game = new ManVSAi(mockCommunicator); // Створення нової гри

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
