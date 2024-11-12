package org.example;

import org.example.gui.Man;

public class AiVSAi {


    private SerialCommunicator communicator;
    private String AiChoose;

    public AiVSAi(SerialCommunicator communicator,String AiChoose){
        this.communicator=communicator;
        this.AiChoose=AiChoose;

    }


    public void PlayGame(Runnable onGameEnd) {
        if(AiChoose.equals("Ai vs Ai(Win strategy)")){
            communicator.sendMessage("Ai vs Ai(Win strategy)\n");
        } else if (AiChoose.equals("Ai vs Ai(Random move)")) {
            communicator.sendMessage("Ai vs Ai(Random move)\n");
        }

        startWaitingForResults(onGameEnd);
    }

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
