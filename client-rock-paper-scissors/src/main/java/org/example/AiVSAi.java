package org.example;

import org.example.gui.Man;

/**
 * Клас для реалізації гри між двома штучними інтелектами (AI).
 * @author Dmytro Kononchuk KI-403
 */
public class AiVSAi {

    private SerialCommunicator communicator; // Комунікатор для зв'язку з Arduino
    private String AiChoose; // Вибір стратегії AI

    /**
     * Конструктор класу AiVSAi.
     *
     * @param communicator комунікатор для зв'язку з Arduino
     * @param AiChoose вибір стратегії AI (виграшна стратегія або випадковий хід)
     */
    public AiVSAi(SerialCommunicator communicator, String AiChoose) {
        this.communicator = communicator;
        this.AiChoose = AiChoose;
    }

    /**
     * Запускає гру між двома AI.
     *
     * @param onGameEnd слухач, який буде викликано при завершенні гри
     */
    public void PlayGame(Runnable onGameEnd) {
        // Відправляємо вибір режиму AI на Arduino
        if (AiChoose.equals("Ai vs Ai(Win strategy)")) {
            communicator.sendMessage("Ai vs Ai(Win strategy)\n");
        } else if (AiChoose.equals("Ai vs Ai(Random move)")) {
            communicator.sendMessage("Ai vs Ai(Random move)\n");
        }

        startWaitingForResults(onGameEnd); // Запускаємо очікування результатів
    }

    /**
     * Запускає форму для очікування результатів гри.
     *
     * @param onGameEnd слухач, який буде викликано при завершенні гри
    */
    private void startWaitingForResults(Runnable onGameEnd) {
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
