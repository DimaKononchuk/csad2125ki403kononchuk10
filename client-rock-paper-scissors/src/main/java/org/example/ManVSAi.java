package org.example;

import org.example.gui.Man;

import javax.swing.*;

public class ManVSAi {

    SerialCommunicator communicator;
    private Man player1;

    public ManVSAi(SerialCommunicator communicator){
        this.communicator=communicator;

    }
    public void PlayGame(Runnable onGameEnd){
        player1=new Man("Player");
        player1.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                communicator.sendMessage("Player:"+player1.getName().toString()+":"+player1.getChoice()+"\n");


                System.out.println(player1.getChoice());
                startWaitingForResults(onGameEnd);
            }
        });

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
