package org.example;

import org.example.SerialCommunicator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ResultWaitingForm {
    private SerialCommunicator serialCommunicator;
    private Timer resultCheckTimer;
    private StringBuilder dataBuffer;
    private Runnable onGameEndListener;
    private JDialog waitingDialog; // Діалог для очікування результатів

    public ResultWaitingForm(SerialCommunicator serialCommunicator) {
        this.serialCommunicator = serialCommunicator;
        this.dataBuffer = new StringBuilder();
    }

    public void setOnGameEndListener(Runnable listener) {
        this.onGameEndListener = listener;
    }

    public void start() {
        // Показуємо форму очікування результатів
        showWaitingForm();
        // Запускаємо таймер для перевірки результатів
        startResultCheckTimer();
    }

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

    private void startResultCheckTimer() {
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

    private void checkForResults() throws InterruptedException {
        if (serialCommunicator.hasAvailableData()) {
            String response = serialCommunicator.readMessage();
            if (response != null) {
                System.out.println("Отримано дані: " + response); // Логування отриманих даних
                dataBuffer.append(response); // Додаємо до буфера

                // Перевіряємо, чи містить буфер символ завершення передачі даних
                if (dataBuffer.toString().contains("\n")) {
                    String[] results = dataBuffer.toString().trim().split("\n"); // Розділяємо на рядки
                    dataBuffer.setLength(0); // Очищаємо буфер
                    Thread.sleep(2000);
                    showResult(results); // Показуємо результати
                    resultCheckTimer.stop(); // Зупиняємо таймер
                    waitingDialog.dispose(); // Закриваємо форму очікування
                }
            }
        }
    }

    private void showResult(String[] results) {
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

    private void endGame() {
        if (onGameEndListener != null) {
            onGameEndListener.run(); // Викликаємо слухача на завершення гри
        }
    }
}
