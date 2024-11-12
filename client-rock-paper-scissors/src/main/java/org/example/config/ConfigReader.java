package org.example.config;

import lombok.Getter;
import lombok.Setter;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;

/**
 * @author Dmytro Kononchuk KI-403
 * Клас для зчитування конфігураційних параметрів з XML файлу.
 */
public class ConfigReader {
    private PortParameters portParameters;
    private PortTimeouts portTimeouts;

    /**
     * Конструктор без параметрів.
     */
    public ConfigReader() {}

    /**
     * Конструктор, який приймає файл XML для зчитування конфігурацій.
     *
     * @param xmlFile файл XML, що містить конфігураційні параметри
     */
    public ConfigReader(File xmlFile) {
        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            Document document = saxBuilder.build(xmlFile);

            Element rootElement = document.getRootElement();
            this.portParameters = new PortParameters();
            this.portTimeouts = new PortTimeouts();

            // Зчитування параметрів порту
            Element portParamsElement = rootElement.getChild("portParameters");
            if (portParamsElement != null) {
                this.portParameters.setPort(portParamsElement.getChildText("port"));
                this.portParameters.setNewDataBits(Integer.parseInt(portParamsElement.getChildText("newDataBits")));
                this.portParameters.setNewStopBits(Integer.parseInt(portParamsElement.getChildText("newStopBits")));
                this.portParameters.setNewBaundRate(Integer.parseInt(portParamsElement.getChildText("newBaundRate")));
            }

            // Зчитування тайм-аутів
            Element portTimeoutsElement = rootElement.getChild("portTimeouts");
            if (portTimeoutsElement != null) {
                this.portTimeouts.setNewReadTimes(Integer.parseInt(portTimeoutsElement.getChildText("newReadTimes")));
                this.portTimeouts.setNewWriteTimes(Integer.parseInt(portTimeoutsElement.getChildText("newWriteTimes")));
            }
        } catch (IOException | org.jdom2.JDOMException e) {
            e.printStackTrace();
        }
    }

    /**
     * Клас для зберігання параметрів порту.
     */
    @Getter
    @Setter
    public static class PortParameters {
        private String port; // Назва порту
        private int newDataBits; // Кількість біт даних
        private int newStopBits; // Кількість стоп-бітів
        private int newBaundRate; // Швидкість передачі даних
    }

    /**
     * Клас для зберігання тайм-аутів порту.
     */
    @Getter
    @Setter
    public static class PortTimeouts {
        private int newReadTimes; // Тайм-аут для читання
        private int newWriteTimes; // Тайм-аут для запису
    }

    // Геттери для отримання конфігурації

    /**
     * Отримує назву порту.
     *
     * @return назва порту або null, якщо не задано
     */
    public String getPort() {
        return portParameters != null ? portParameters.getPort() : null;
    }

    /**
     * Отримує швидкість передачі даних.
     *
     * @return швидкість передачі даних або 0, якщо не задано
     */
    public int getNewBaundRate() {
        return portParameters != null ? portParameters.getNewBaundRate() : 0;
    }

    /**
     * Отримує кількість біт даних.
     *
     * @return кількість біт даних або 0, якщо не задано
     */
    public int getNewDataBits() {
        return portParameters != null ? portParameters.getNewDataBits() : 0;
    }

    /**
     * Отримує кількість стоп-бітів.
     *
     * @return кількість стоп-бітів або 0, якщо не задано
     */
    public int getNewStopBits() {
        return portParameters != null ? portParameters.getNewStopBits() : 0;
    }

    /**
     * Отримує тайм-аут для читання.
     *
     * @return тайм-аут для читання або 0, якщо не задано
     */
    public int getNewReadTimes() {
        return portTimeouts != null ? portTimeouts.getNewReadTimes() : 0;
    }

    /**
     * Отримує тайм-аут для запису.
     *
     * @return тайм-аут для запису або 0, якщо не задано
     */
    public int getNewWriteTimes() {
        return portTimeouts != null ? portTimeouts.getNewWriteTimes() : 0;
    }
}
