package org.example;

import com.fazecast.jSerialComm.SerialPort;
import org.example.gui.EnterModes;
import org.example.gui.Man;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//
//public class Main {
//    public static void main(String[] args) {
//        SerialPort[] ports = SerialPort.getCommPorts();
//        SerialCommunicator serialCommunicator = new SerialCommunicator(ports[1]);
//        serialCommunicator.openPort();
//
//        EnterModes mode = new EnterModes();
//        mode.button.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                // Можна закрити вікно або виконати інші дії тут
//                if (mode.getModes().equals("Man vs Man")) {
//                    serialCommunicator.sendMessage("Man vs Man"+"\n");
//                    mode.setVisible(false);
//                    ManVSMan choose1 = new ManVSMan(serialCommunicator);
//                    choose1.PlayGame();
//
//                }else if(mode.getModes().equals("Man vs AI")){
//                    serialCommunicator.sendMessage("Man vs AI"+"\n");
//                    mode.setVisible(false);
//                    ManVSAi choose = new ManVSAi(serialCommunicator);
//                    choose.PlayGame();
//
//                }else if(mode.getModes().equals("AI vs AI")){
//                    serialCommunicator.sendMessage("AI vs AI"+"\n");
//                }
//            }
//        });
//
//        // Створюємо окремий потік для читання даних від Arduino
//        Thread readThread = new Thread(() -> {
//            StringBuilder dataBuffer = new StringBuilder(); // Буфер для накопичення даних
//            while (true) {
//                if (serialCommunicator.hasAvailableData()) {
//                    String response = serialCommunicator.readMessage();
//                    if (response != null) {
//                        dataBuffer.append(response); // Додаємо дані в буфер
//                        // Перевіряємо, чи містить буфер символ завершення передачі даних
//                        if (response.contains("\n")) { // Наприклад, '\n' використовується як символ завершення
//                            String fullMessage = dataBuffer.toString().trim(); // Отримуємо всі дані
//                            System.out.println("Відповідь від Arduino: " + fullMessage);
//                            JOptionPane.showMessageDialog(null, fullMessage, "Result", JOptionPane.INFORMATION_MESSAGE);
//                            dataBuffer.setLength(0); // Очищаємо буфер після обробки
//                        }
//                    }
//                }
//                try {
//                    Thread.sleep(1000); // Невелика затримка
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                }
//            }
//        });
//        readThread.start(); // Запускаємо потік для читання даних
//
//
//
//    }
//}

//        // Отримуємо всі доступні порти
//        SerialPort[] ports = SerialPort.getCommPorts();
//        System.out.println("Доступні порти:");
//        for (int i = 0; i < ports.length; i++) {
//            System.out.println(i + 1 + ": " + ports[i].getSystemPortName());
//        }
//
//        // Вибір порту користувачем
//        System.out.print("Виберіть порт (введіть номер): ");
//        Scanner scanner = new Scanner(System.in);
//        int chosenPort = scanner.nextInt() - 1;
//        scanner.nextLine(); // Щоб уникнути проблем із наступним зчитуванням
//
//        if (chosenPort < 0 || chosenPort >= ports.length) {
//            System.out.println("Невірний номер порту. Вихід.");
//            return;
//        }
//
//        //String portName = .getSystemPortName();
//        SerialCommunicator communicator = new SerialCommunicator(ports[chosenPort]);
//        // Відкриття порту
//        if (!communicator.openPort()) {
//            return; // Вихід, якщо не вдалося відкрити порт
//        }
//
//        // Створюємо окремий потік для читання даних від Arduino
//        Thread readThread = new Thread(() -> {
//            while (true) {
//                if (communicator.hasAvailableData()) {
//                    String response = communicator.readMessage();
//                    if (response != null) {
//                        System.out.println("Відповідь від Arduino: " + response);
//                    }
//                }
//                try {
//                    Thread.sleep(100); // невелика затримка
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                }
//            }
//        });
//        readThread.start(); // запускаємо потік для читання даних
//
//        // Введення тексту для відправлення на Arduino
//        System.out.println("Введіть текст для відправлення на Arduino (або 'exit' для завершення):");
//        while (true) {
//            System.out.print("> ");
//            String message = scanner.nextLine();
//
//            // Вихід з програми, якщо користувач вводить 'exit'
//            if (message.equalsIgnoreCase("exit")) {
//                break;
//            }
//
//            // Надсилаємо дані до Arduino
//            communicator.sendMessage(message);
//        }
//
//        // Закриття порту і завершення програми
//        communicator.closePort();
//    }
//}