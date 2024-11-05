package org.example;

import lombok.Getter;
import lombok.Setter;
import org.example.SerialCommunicator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/***
 * Клас для відображення діалогу очікування результатів гри.
 * @author Dmytro Kononchuk KI-403
 */
@Getter
@Setter
public class ResultWaitingForm {
    private SerialCommunicator serialCommunicator; // Комунікатор для зв'язку з Arduino
    private Timer resultCheckTimer; // Таймер для перевірки результатів
    private StringBuilder dataBuffer; // Буфер для зберігання отриманих даних
    private StringBuilder text;
    private Runnable onGameEndListener; // Слухач для обробки завершення гри
    private JDialog waitingDialog; // Діалог для очікування результатів

    /**
     * Конструктор для ініціалізації класу з переданим комунікатором.

     * @param serialCommunicator комунікатор для зв'язку з Arduino
     */
    public ResultWaitingForm(SerialCommunicator serialCommunicator) {
        this.serialCommunicator = serialCommunicator;
        this.dataBuffer = new StringBuilder();
        this.text=new StringBuilder();
    }

    /**
     * Встановлює слухача для обробки події завершення гри.
     *
     * @param listener слухач, який буде викликано при завершенні гри
     */
    public void setOnGameEndListener(Runnable listener) {
        this.onGameEndListener = listener;
    }

    /**
     * Запускає процес очікування результатів.
     */
    public void start() {
        // Показуємо форму очікування результатів
        showWaitingForm();
        // Запускаємо таймер для перевірки результатів
        startResultCheckTimer();
    }

    /**
     * Відображає діалогове вікно для очікування результатів.
     */
    private void showWaitingForm() {
        waitingDialog = new JDialog(); // Використовуємо JDialog для вікна очікування
        waitingDialog.setTitle("Waiting");
        waitingDialog.setSize(300, 100);
        waitingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        waitingDialog.setLocationRelativeTo(null); // Центруємо діалог на екрані

        JLabel waitingLabel = new JLabel("We are waiting for the results...", SwingConstants.CENTER);
        waitingDialog.add(waitingLabel);
        waitingDialog.setVisible(true);
    }

    /**
     * Запускає таймер для перевірки результатів через певний інтервал часу.
     */
    public void startResultCheckTimer() {
        resultCheckTimer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    checkForResults();
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        resultCheckTimer.start();
    }

    /**
     * Перевіряє наявність результатів і обробляє їх, якщо вони доступні.
     *
     * @throws InterruptedException якщо перевірка результатів була перервана
     */
    public void checkForResults() throws InterruptedException {
        if (serialCommunicator.hasAvailableData()) {
            String response = serialCommunicator.readMessage();
            if (response != null) {
                System.out.println("Отримано дані: " + response); // Логування отриманих даних
                dataBuffer.append(response); // Додаємо до буфера

                // Перевіряємо, чи містить буфер символ завершення передачі даних
                if (dataBuffer.toString().contains("\n")) {
                    String[] results = dataBuffer.toString().trim().split("\n"); // Розділяємо на рядки
                    text.setLength(0);
                    text.replace(0,dataBuffer.length()-1,dataBuffer.toString());
                    dataBuffer.setLength(0); // Очищаємо буфер

                    showResult(results); // Показуємо результати
                    resultCheckTimer.stop(); // Зупиняємо таймер
                    waitingDialog.dispose(); // Закриваємо форму очікування
                }
            }
        }
    }

    /**
     * Відображає результати гри у вигляді повідомлення.
     *
     * @param results масив результатів для відображення
     */
    public void showResult(String[] results) {
        StringBuilder resultMessage = new StringBuilder(); // Заголовок для результатів

        // Обробка кожного рядка результату
        for (String result : results) {
            resultMessage.append(result).append("\n"); // Додаємо кожен результат з перенесенням рядка
        }

        // Викликаємо JOptionPane для показу результату
        JOptionPane.showMessageDialog(null, resultMessage.toString(), "Result", JOptionPane.INFORMATION_MESSAGE);

        // Викликаємо слухача на завершення гри
        endGame();
    }

    /**
     * Викликає слухача на завершення гри, якщо він заданий.
     */
    public void endGame() {
        if (onGameEndListener != null) {
            onGameEndListener.run(); // Викликаємо слухача на завершення гри
        }
    }
}
