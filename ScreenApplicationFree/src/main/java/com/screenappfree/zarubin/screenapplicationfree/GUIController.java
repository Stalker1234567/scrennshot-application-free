package com.screenappfree.zarubin.screenapplicationfree;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;

import java.io.File;

public class GUIController {

    @FXML
    private Button hotkeyButton;
    private KeyCode hotKey;
    private boolean waitingForKey = false;
    @FXML
    private TreeView<String> treeView;
    @FXML
    private Button openButton;
    @FXML
    private Label hotkeyLabel;
    @FXML
    private AnchorPane mainPane;
    private ScreenCaptureManager screenCaptureManager;

    @FXML
    public void handleHotkeyButtonAction() {
        if (!waitingForKey) {
            hotkeyButton.setText("Press a key...");
            waitingForKey = true;
        } else {
            // Пользователь выбрал горячую клавишу
            hotkeyButton.setText("Choosing a hotkey");
            waitingForKey = false;
            String hotkey = hotKey != null ? hotKey.getName() : "";
            hotkeyLabel.setText("Hotkey: " + hotkey);
        }
    }

    @FXML
    public void handleKeyPressed(KeyEvent event) {
        if (waitingForKey) {
            hotKey = event.getCode();
            hotkeyLabel.setText("Hotkey: " + hotKey.getName());
            waitingForKey = false;
        }
    }

    @FXML
    public void handleOpenButtonAction(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Folder");
        File selectedDirectory = directoryChooser.showDialog(((Button) event.getSource()).getScene().getWindow());

        if (selectedDirectory != null) {
            String path = selectedDirectory.getAbsolutePath();
            TreeItem<String> newItem = new TreeItem<>(path);
            treeView.getRoot().getChildren().add(newItem);

            // Сохранение выбранной папки в настройках
            SettingsManager.saveFolderPath(path);
        }
    }

    @FXML
    public void initialize() {
        screenCaptureManager = new ScreenCaptureManager();
        TreeItem<String> rootItem = new TreeItem<>();
        rootItem.setExpanded(true);
        treeView.setRoot(rootItem);

        // Добавляем слушатель фокуса на окно приложения
        mainPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.windowProperty().addListener((observableWindow, oldWindow, newWindow) -> {
                    if (newWindow != null) {
                        newWindow.focusedProperty().addListener((observableFocused, oldFocused, newFocused) -> {
                            if (newFocused) {
                                // При фокусе окна включаем обработку горячей клавиши
                                mainPane.addEventHandler(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
                            } else {
                                // При потере фокуса окна выключаем обработку горячей клавиши
                                mainPane.removeEventHandler(KeyEvent.KEY_PRESSED, this::handleKeyPressed);
                            }
                        });
                    }
                });
            }
        });
        // Загрузка сохраненных настроек
        String savedFolderPath = SettingsManager.loadFolderPath();
        String savedHotkey = SettingsManager.loadHotkey();

        // Применение настроек
        if (savedFolderPath != null) {
            // Применить сохраненный путь к папке
            File selectedDirectory = new File(savedFolderPath);
            if (selectedDirectory.exists() && selectedDirectory.isDirectory()) {
                String path = selectedDirectory.getAbsolutePath();
                TreeItem<String> newItem = new TreeItem<>(path);
                treeView.getRoot().getChildren().add(newItem);
            }
        }

        if (savedHotkey != null && !savedHotkey.isEmpty()) {
            // Применить сохраненную горячую клавишу
            hotKey = KeyCode.valueOf(savedHotkey);
            hotkeyLabel.setText("Hotkey: " + savedHotkey);

            // Установка слушателя клавиш для горячей клавиши
            mainPane.addEventHandler(KeyEvent.KEY_PRESSED, this::handleHotkeyPressed);
        }
    }

    @FXML
    public void handleSaveButtonAction(ActionEvent event) {
        String folderPath = getSelectedFolderPath();
        String hotkey = hotKey != null ? hotKey.getName() : "";

        SettingsManager.saveSettings(folderPath, hotkey);
    }

    @FXML
    public void handleHotkeyPressed(KeyEvent event) {
        if (event.getCode() == hotKey) {
            // Горячая клавиша нажата, выполняем захват экрана и сохранение скриншота
            String folderPath = SettingsManager.loadFolderPath();
            screenCaptureManager.captureScreen(folderPath);
        }
    }

    private String getSelectedFolderPath() {
        TreeItem<String> selectedItem = treeView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            return selectedItem.getValue();
        }
        return null;
    }
}