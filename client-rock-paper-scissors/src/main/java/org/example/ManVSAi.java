package org.example;

import lombok.Getter;
import lombok.Setter;
import org.example.gui.Man;

import javax.swing.*;

/**
 * Клас для реалізації гри між гравцем і штучним інтелектом (AI).
 * @author Dmytro Kononchuk KI-403
 */
@Setter
@Getter
public class ManVSAi {

    private SerialCommunicator communicator; // Комунікатор для зв'язку з Arduino
    private Man player1; // Гравець

    /**
     * Конструктор класу ManVSAi.

     * @param communicator комунікатор для зв'язку з Arduino
     */
    public ManVSAi(SerialCommunicator communicator) {
        this.communicator = communicator;
    }

    /**
     * Запускає гру між гравцем і штучним інтелектом.
     *
     * @param onGameEnd слухач, який буде викликано при завершенні гри
     */
    public void PlayGame(Runnable onGameEnd) {
        player1 = new Man("Player");
        player1.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                communicator.sendMessage("Player:" + player1.getName() + ":" + player1.getChoice() + "\n");

                System.out.println(player1.getChoice());
                startWaitingForResults(onGameEnd); // Запускаємо очікування результатів
            }
        });
    }

    /**
     * Запускає форму для очікування результатів після завершення гри.
     *
     * @param onGameEnd слухач, який буде викликано при завершенні гри
     */
    public void startWaitingForResults(Runnable onGameEnd) {
        // Створюємо екземпляр ResultWaitingForm для очікування результатів
        ResultWaitingForm resultWaitingForm = new ResultWaitingForm(communicator);

        // Додаємо слухача для завершення гри
        resultWaitingForm.setOnGameEndListener(() -> {
            // Викликаємо onGameEnd після завершення гри
            if (onGameEnd != null) {
                onGameEnd.run();
            }
        });

        // Запускаємо форму для очікування результатів
        resultWaitingForm.start();
    }
}
