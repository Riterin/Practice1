package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Login extends JFrame  {
    public Login() {
        setTitle("Авторизация");
        setSize(500, 500);
        setVisible(true);
        setLocation(410, 290);

        JPanel panel = new JPanel();
        add(panel);
        panel.setBackground(Color.CYAN);

        GridBagConstraints bag = new GridBagConstraints();
        panel.setLayout(new GridBagLayout());

        bag.gridx = 1;
        bag.gridy = 1;
        panel.add(new Label("Логин"), bag);

        bag.insets = new Insets(10, 0, 0, 0);

        bag.gridy = 2;
        JTextField login = new JTextField("", 20);
        panel.add(login, bag);

        bag.insets = new Insets(10, 0, 0, 0);

        bag.gridy = 3;
        panel.add(new Label("Пароль"), bag);

        bag.insets = new Insets(10, 0, 0, 0);

        bag.gridy = 4;
        JTextField password = new JTextField("", 20);
        panel.add(password, bag);

        bag.insets = new Insets(30, 0, 0, 0);

        bag.gridy = 5;
        JButton confirm = new JButton("Залогиниться");
        confirm.setSize(100, 50);
        panel.add(confirm, bag);

        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainFrame();
                dispose();
            }
        });
    }
}
