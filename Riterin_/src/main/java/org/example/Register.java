package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Register extends JFrame  {
    private final JLabel status = new JLabel("Вход и регистрация");

    private final JLabel logLabel = new JLabel("логин:");
    private final JTextField loginField = new JTextField();

    private final JLabel pasLabel = new JLabel("пароль:");
    private final JTextField passwordField = new JTextField(); // Скрывает пароль звездочками

    private final JButton enterBtn = new JButton("вход");
    private final JButton confirmBtn = new JButton("регистрация");

    private final DatabaseConnection database = new DatabaseConnection();

    public Register() {
        JPanel panel = new JPanel();
        add(panel);
        Color a = new Color(153, 239, 211);
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(3, 5, 3, 5);

        loginField.setColumns(15);
        passwordField.setColumns(15);

        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        add(status, c);
        c.gridwidth = 1;

        c.gridx = 0; c.gridy = 1; add(logLabel, c);
        c.gridx = 1; c.gridy = 1; add(loginField, c);

        c.gridx = 0; c.gridy = 2; add(pasLabel, c);
        c.gridx = 1; c.gridy = 2; add(passwordField, c);

        c.gridx = 0; c.gridy = 3; add(enterBtn, c);
        c.gridx = 1; c.gridy = 3; add(confirmBtn, c);

        // РЕГИСТРАЦИЯ
        confirmBtn.addActionListener(e -> {
            String l = loginField.getText().trim();
            String p = passwordField.getText().trim();

            if (l.isEmpty() || p.isEmpty()) {
                status.setText("Заполните все поля!");
                return;
            }

            if (database.registerUser(new User(l, p))) {
                status.setText("Пользователь успешно создан!");
                passwordField.setText(""); // Очищаем поле пароля
            } else {
                status.setText("Логин уже занят или ошибка БД");
            }
        });

        // ВХОД
        enterBtn.addActionListener(e -> {
            String l = loginField.getText().trim();
            String p = passwordField.getText().trim();

            if (l.isEmpty() || p.isEmpty()) {
                status.setText("Заполните все поля!");
                return;
            }

            database.authenticate(l, p).ifPresentOrElse(
                    user -> {
                        new PersonalAccount(user.getId());
                        dispose();
                    },
                    () -> status.setText("Неверный логин или пароль")
            );
        });

        setVisible(true);
    }
}
