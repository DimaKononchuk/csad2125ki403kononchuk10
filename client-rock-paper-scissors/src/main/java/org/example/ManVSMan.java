package org.example;
import com.fazecast.jSerialComm.SerialPort;
import org.example.gui.Man;
import javax.swing.*;

public class ManVSMan {

    SerialCommunicator communicator;
    private Man player1;
    private Man player2;
    public ManVSMan(SerialCommunicator communicator){
        this.communicator=communicator;

    }
    public void PlayGame(Runnable onGameEnd){
        player1=new Man("Player №1");
        player1.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                communicator.sendMessage("Player №1:"+player1.getName().toString()+":"+player1.getChoice()+"\n");
                System.out.println(player1.getChoice());
                player2=new Man("Player №2");
                player2.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent windowEvent) {
                        communicator.sendMessage("Player №2:"+player2.getName().toString()+":"+player2.getChoice()+"\n");
                        System.out.println(player2.getChoice());
                        //Main.main(null);
                        // Після завершення дій гравців запускаємо форму очікування результатів
                        startWaitingForResults(onGameEnd);
                    }
                });
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
