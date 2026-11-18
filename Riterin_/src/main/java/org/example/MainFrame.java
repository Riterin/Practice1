package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {

    public MainFrame() {
        setSize(500, 600);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        add(panel);
        Color a = new Color(4, 46, 96);
        panel.setBackground(a);

        GridBagConstraints bag = new GridBagConstraints();
        bag.gridx = 1;
        bag.gridy = 1;
        panel.setLayout(new GridBagLayout());

        JButton log = new JButton("Логин");
        log.setSize(300, 50);
        panel.add(log, bag);

        bag.insets = new Insets(50, 0, 0, 0);

        log.getActionListeners();
        log.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Login();
                dispose();
            }
        });

        bag.gridy = 2;
        JButton regis = new JButton("Регистрация");
        log.setSize(300, 50);
        panel.add(regis, bag);

        regis.getActionListeners();
        regis.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Register();
                dispose();
            }
        });


        setVisible(true);
    }
}
