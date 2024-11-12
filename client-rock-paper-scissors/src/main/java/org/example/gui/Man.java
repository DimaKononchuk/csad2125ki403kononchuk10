package org.example.gui;
import com.fazecast.jSerialComm.SerialPort;
import lombok.Getter;
import org.example.SerialCommunicator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.ImageGraphicAttribute;
public class Man extends JFrame{

    private JPanel panel;
    private JButton rockButton;
    private JButton paperButton;
    private JButton scissorsButton;
    private JLabel LabelName;
    private JLabel RockLabel;
    private JLabel PaperLabel;
    private JLabel ScissorsLabel;
    @Getter
    private String name;

    // Метод для отримання вибору
    @Getter
    private String choice=new String();

    public Man(String title){
        super(title);

        while (name == null || name.trim().isEmpty()) { // Цикл триватиме, поки ім'я не буде введене
            name = JOptionPane.showInputDialog("Enter the name");
            if (name == null) {
                // Користувач натиснув "Скасувати"
                System.exit(0);
                return; // Вихід з конструктора
            } else if (name.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Error: name cannot be empty. Try again.",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        RockLabel.setIcon(new ImageIcon("./img/rock.png"));
        PaperLabel.setIcon(new ImageIcon("./img/paper.png"));
        ScissorsLabel.setIcon(new ImageIcon("./img/scissors.png"));
        LabelName.setText(name); // Встановлення введеного імені у JLabel
        super.setSize(400,300);
        super.add(panel);
        super.setLocationRelativeTo(null); // Центруємо вікно
        super.setVisible(true);
        rockButton.addActionListener(buttonListener);
        paperButton.addActionListener(buttonListener);
        scissorsButton.addActionListener(buttonListener);

    }
    ActionListener buttonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource(); // Отримуємо кнопку, яка була натиснута
            choice=clickedButton.getText();
            JOptionPane.showMessageDialog(null, "Ви натиснули: " + clickedButton.getText());
            close();
        }
    };
    public void close(){
        super.dispose();
    }
}
