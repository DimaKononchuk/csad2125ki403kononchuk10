package org.example;

import com.fazecast.jSerialComm.SerialPort;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SerialCommunicator {
    private final SerialPort arduinoPort;



    // Функція для відкриття порту
    public boolean openPort() {//String portName, int baudRate
         //Знаходимо порт за назвою
        //arduinoPort = SerialPort.getCommPort(portName);
        if (arduinoPort == null) {
            System.out.println("Порт не знайдено.");
            return false;
        }

        // Налаштування порту
        arduinoPort.setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);
        arduinoPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 1000, 1000);

        // Відкриваємо порт
        if (arduinoPort.openPort()) {
            System.out.println("Порт " + arduinoPort.getSystemPortName() + " відкрито.");
            return true;
        } else {
            System.out.println("Не вдалося відкрити порт.");
            return false;
        }

    }

    // Функція для закриття порту
    public void closePort() {
        if (arduinoPort != null && arduinoPort.isOpen()) {
            arduinoPort.closePort();
            System.out.println("Порт закрито.");
        }
    }

    // Функція для надсилання повідомлення
    public boolean sendMessage(String message) {
        if (arduinoPort != null && arduinoPort.isOpen()) {
            byte[] buffer = message.getBytes();
            int bytesSent = arduinoPort.writeBytes(buffer, buffer.length);
            if (bytesSent > 0) {
                System.out.println("Повідомлення надіслано: " + message);
                return true;
            } else {
                System.out.println("Не вдалося надіслати повідомлення.");
                return false;
            }
        }
        return false;
    }

    // Функція для читання відповіді від Arduino
    public String readMessage() {
        if (arduinoPort != null && arduinoPort.isOpen()) {
            byte[] readBuffer = new byte[2048];
            int numRead = arduinoPort.readBytes(readBuffer, readBuffer.length);
            if (numRead > 0) {
                return new String(readBuffer, 0, numRead);
            }
        }
        return null;
    }

    // Функція для перевірки наявності доступних даних
    public boolean hasAvailableData() {
        if (arduinoPort != null && arduinoPort.isOpen()) {
            return arduinoPort.bytesAvailable() > 0;
        }
        return false;
    }
}
