package org.example;

import javax.swing.*;
import java.awt.*;

public class PersonalAccount extends JFrame {
    private final DatabaseConnection dbConnection;
    private final int id;

    public PersonalAccount(int id) {
        this.dbConnection = new DatabaseConnection();
        this.id = id;
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel panel = new JPanel();
        add(panel);
        setTitle("Личный кабинет");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setLayout(new GridBagLayout());

        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel labelWelcome = new JLabel("Ваш личный кабинет");
        panel.add(labelWelcome, gbc);

        gbc.insets = new Insets(30, 0, 0, 0);
        gbc.gridy = 1;
        String role = dbConnection.getRoleByUserId(id).orElse("Роль не найдена");
        JLabel labelRole = new JLabel("Роль: " + dbConnection.roleToRussian(role));
        panel.add(labelRole, gbc);
        System.out.println(labelRole.getText());

        gbc.gridy = 2;
        if (dbConnection.roleToRussian(role) == "Администратор") {
            JButton adminPanel = new JButton("Активировать Админ-панель");
            adminPanel.setSize(200, 50);
            panel.add(adminPanel, gbc);
        } else if (dbConnection.roleToRussian(role) == "Барбер") {
            JButton barberPanel = new JButton("Активировать меню");
            barberPanel.setSize(200, 50);
            panel.add(barberPanel, gbc);
        }

        setVisible(true);
    }
}
