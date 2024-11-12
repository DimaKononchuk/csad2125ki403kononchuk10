package org.example;

import com.fazecast.jSerialComm.SerialPort;
import lombok.Getter;
import lombok.Setter;
import org.example.gui.Man;
import javax.swing.*;

/**
 * Клас для реалізації гри між двома гравцями.
 * @author Dmytro Kononchuk KI-403
 */
@Getter
@Setter
public class ManVSMan {

    private SerialCommunicator communicator; // Комунікатор для зв'язку з Arduino
    private Man player1; // Перший гравець
    private Man player2; // Другий гравець

    /**
     * Конструктор класу ManVSMan.

     * @param communicator комунікатор для зв'язку з Arduino
     */
    public ManVSMan(SerialCommunicator communicator) {
        this.communicator = communicator;
    }

    /**
     * Запускає гру між двома гравцями.
     *
     * @param onGameEnd слухач, який буде викликано при завершенні гри
     */
    public void PlayGame(Runnable onGameEnd) {
        player1 = new Man("Player №1");
        player1.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                communicator.sendMessage("Player №1:" + player1.getName() + ":" + player1.getChoice() + "\n");
                System.out.println(player1.getChoice());
                player2 = new Man("Player №2");
                player2.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                        communicator.sendMessage("Player №2:" + player2.getName() + ":" + player2.getChoice() + "\n");
                        System.out.println(player2.getChoice());
                        // Після завершення дій гравців запускаємо форму очікування результатів
                        startWaitingForResults(onGameEnd);
                    }
                });
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
