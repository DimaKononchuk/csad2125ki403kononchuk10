package org.example;

import com.fazecast.jSerialComm.SerialPort;
import lombok.RequiredArgsConstructor;
import org.example.config.ConfigReader;

import java.io.File;

/**
 * Клас для управління серійною комунікацією з Arduino.
 * @author Dmytro Kononchuk KI-403
 */
@RequiredArgsConstructor
public class SerialCommunicator {
    private final SerialPort arduinoPort;
    private final ConfigReader configReader = new ConfigReader(new File("config.xml"));

    /**
     * Відкриває серійний порт для комунікації з Arduino.
     * @return true, якщо порт успішно відкрито; false в іншому випадку.     *
     */
    public boolean openPort() {
        // Перевірка наявності порту
        if (arduinoPort == null) {
            System.out.println("Порт не знайдено.");
            return false;
        }

        // Налаштування параметрів порту
        arduinoPort.setComPortParameters(configReader.getNewBaundRate(),
                configReader.getNewDataBits(),
                configReader.getNewStopBits(),
                SerialPort.NO_PARITY);
        arduinoPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING,
                configReader.getNewReadTimes(),
                configReader.getNewWriteTimes());

        // Спроба відкрити порт
        if (arduinoPort.openPort()) {
            System.out.println("Порт " + arduinoPort.getSystemPortName() + " відкрито.");
            return true;
        } else {
            System.out.println("Не вдалося відкрити порт.");
            return false;
        }
    }

    /**
     * Закриває серійний порт.
     */
    public void closePort() {
        if (arduinoPort != null && arduinoPort.isOpen()) {
            arduinoPort.closePort();
            System.out.println("Порт закрито.");
        }
    }

    /**
     * Надсилає повідомлення до Arduino.
     *
     * @param message повідомлення для відправки.
     * @return true, якщо повідомлення успішно надіслано; false в іншому випадку.
     */
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

    /**
     * Читає відповідь від Arduino.
     *
     * @return зчитане повідомлення.
     * @throws InterruptedException якщо операція зчитування перервана.
     */
    public String readMessage() throws InterruptedException {
        StringBuilder messageBuilder = new StringBuilder();
        if (arduinoPort != null && arduinoPort.isOpen()) {
            byte[] readBuffer = new byte[4096]; // Збільшений буфер
            int numRead;
            do {
                numRead = arduinoPort.readBytes(readBuffer, readBuffer.length);
                if (numRead > 0) {
                    messageBuilder.append(new String(readBuffer, 0, numRead));
                }
                Thread.sleep(50); // Коротша затримка для зчитування наступних байтів
            } while (arduinoPort.bytesAvailable() > 0);
        }
        return messageBuilder.toString();
    }

    /**
     * Перевіряє наявність доступних даних у порту.
     *
     * @return true, якщо є доступні дані; false в іншому випадку.
     */
    public boolean hasAvailableData() {
        if (arduinoPort != null && arduinoPort.isOpen()) {
            return arduinoPort.bytesAvailable() > 0;
        }
        return false;
    }
}
