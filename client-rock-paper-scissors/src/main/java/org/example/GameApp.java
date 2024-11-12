package org.example;

import com.fazecast.jSerialComm.SerialPort;
import org.example.gui.EnterModes;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class GameApp {
    private SerialCommunicator serialCommunicator;
    private EnterModes mode;

    public GameApp() {
        // Ініціалізуємо серійну комунікацію
        SerialPort[] ports = SerialPort.getCommPorts();
        serialCommunicator = new SerialCommunicator(ports[1]);


        // Запускаємо вибір режиму
        startModeSelection();
    }

    private void startModeSelection() {
        serialCommunicator.openPort();
        mode = new EnterModes();
        mode.button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleModeSelection();
            }
        });

        mode.setVisible(true);
    }

    private void handleModeSelection() {
        String selectedMode = mode.getModes();

        // Відправляємо вибраний режим на Arduino
        serialCommunicator.sendMessage(selectedMode + "\n");
        mode.setVisible(false); // Приховуємо вікно вибору режиму

        // Виконуємо дію відповідно до вибраного режиму
        if ("Man vs Man".equals(selectedMode)) {
            ManVSMan choose = new ManVSMan(serialCommunicator);
            choose.PlayGame(() -> onGameEnd()); // Передаємо метод завершення гри
        } else if ("Man vs AI".equals(selectedMode)) {
            ManVSAi choose = new ManVSAi(serialCommunicator);
            choose.PlayGame(() -> onGameEnd()); // Передаємо метод завершення гри
        } else if ("Ai vs Ai(Random move)".equals(selectedMode)) {
            AiVSAi choose=new AiVSAi(serialCommunicator,selectedMode);
            choose.PlayGame(()->onGameEnd());
        } else if ("Ai vs Ai(Win strategy)".equals(selectedMode)) {
            AiVSAi choose=new AiVSAi(serialCommunicator,selectedMode);
            choose.PlayGame(()->onGameEnd());
        }
    }

    private void onGameEnd() {
        // Після завершення гри знову відкриваємо вікно вибору режиму
        SwingUtilities.invokeLater(() -> {
            serialCommunicator.closePort();
            startModeSelection(); // Заново запускаємо вибір режиму
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameApp::new);
    }
}