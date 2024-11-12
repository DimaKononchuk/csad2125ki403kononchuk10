package org.example;

import com.fazecast.jSerialComm.SerialPort;
import lombok.Getter;
import lombok.Setter;
import org.example.gui.EnterModes;
import org.example.config.ConfigReader;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 *  Головний клас для запуску гри, що реалізує серійну комунікацію та вибір режимів гри.
 *  @author Dmytro Kononchuk KI-403
 */
@Setter
@Getter
public class GameApp {
    private SerialCommunicator serialCommunicator; // Комунікатор для зв'язку з Arduino
    private EnterModes mode; // Вікно для вибору режиму гри
    private ConfigReader configReader = new ConfigReader(new File("config.xml"));
    /**
     * Конструктор класу GameApp. Ініціалізує серійну комунікацію та запускає вибір режиму.

     */
    public GameApp() {
        // Ініціалізуємо серійну комунікацію
        serialCommunicator = new SerialCommunicator(SerialPort.getCommPort(configReader.getPort()));
        serialCommunicator.openPort();
        // Запускаємо вибір режиму
        startModeSelection();
    }
    public GameApp(SerialCommunicator communicator) {
        // Ініціалізуємо серійну комунікацію
        serialCommunicator = communicator;
        serialCommunicator.openPort();
        // Запускаємо вибір режиму
        startModeSelection();
    }

    /**
     * Запускає вікно для вибору режиму гри.
     */
    public void startModeSelection() {

        mode = new EnterModes();
        mode.button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleModeSelection(); // Обробляємо вибір режиму
            }
        });

        mode.setVisible(true); // Відображаємо вікно вибору режиму
    }

    /**
     * Обробляє вибір режиму гри.
     */
    public void handleModeSelection() {
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
            AiVSAi choose = new AiVSAi(serialCommunicator, selectedMode);
            choose.PlayGame(() -> onGameEnd());
        } else if ("Ai vs Ai(Win strategy)".equals(selectedMode)) {
            AiVSAi choose = new AiVSAi(serialCommunicator, selectedMode);
            choose.PlayGame(() -> onGameEnd());
        }
    }

    /**
     * Метод, який викликається при завершенні гри.
     * Знову відкриває вікно вибору режиму.
     */
    public void onGameEnd() {
        // Після завершення гри знову відкриваємо вікно вибору режиму
        SwingUtilities.invokeLater(() -> {

            startModeSelection(); // Заново запускаємо вибір режиму
        });
    }

    /**
     * Головний метод програми.
     *
     * @param args аргументи командного рядка
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameApp::new); // Запускаємо додаток
    }
}
