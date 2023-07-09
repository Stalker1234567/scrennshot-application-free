package com.screenappfree.zarubin.screenapplicationfree;

import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;

public class ScreenCaptureManager {

    public void captureScreen(String folderPath) {
        folderPath = SettingsManager.loadFolderPath();
        if (folderPath != null) {
            try {
                // Получение размеров экрана
                Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());

                // Создание объекта Robot для захвата экрана
                Robot robot = new Robot();
                BufferedImage screenshot = robot.createScreenCapture(screenRect);

                // Генерация уникального имени файла на основе текущего времени
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmssSSS");
                String timestamp = now.format(formatter);
                String filename = "screenshot_" + timestamp + ".png";

                // Сохранение скриншота в файл в выбранной папке
                File file = new File(folderPath, filename);
                ImageIO.write(screenshot, "png", file);

                System.out.println("Скриншот сохранен в файл: " + file.getAbsolutePath());
            } catch (AWTException | IOException ex) {
                ex.printStackTrace();
            }
        } else {
            System.out.println("Не удалось загрузить путь к папке из настроек.");
        }
    }
}