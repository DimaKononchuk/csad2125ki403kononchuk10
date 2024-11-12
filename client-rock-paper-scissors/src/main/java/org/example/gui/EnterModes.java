package org.example.gui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EnterModes extends JFrame{

    private JPanel panel1;
    private JComboBox comboBox;
    public JButton button;

    public EnterModes(){
        super("Rock Paper Scissors - Choose Mode");
        comboBox.addItem("Man vs Man");
        comboBox.addItem("Man vs AI");
        comboBox.addItem("Ai vs Ai(Random move)");
        comboBox.addItem("Ai vs Ai(Win strategy)");
        super.setContentPane(panel1); // Встановлюємо панель як контент для фрейму
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setSize(350, 250);
        super.setResizable(false);
        super.setLocationRelativeTo(null); // Центруємо вікно
        super.setVisible(true); // Відображаємо вікно

    };

    public String getModes() {
        return comboBox.getSelectedItem().toString();
    }


}
