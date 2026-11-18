package org.example;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.xml.crypto.Data;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TableFrame extends JFrame {

    private final DatabaseConnection dbConnection;
    private JTable users;
    private DefaultTableModel usersTableModel;

    private JTable product;
    private DefaultTableModel productTableModel;

    public TableFrame() {
        this.dbConnection = new DatabaseConnection();
        setTitle("Таблицы");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 1000);

        initTable();
        loadData();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initTable() {
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel panel = new JPanel();
        add(panel);
        panel.setLayout(new GridBagLayout());

        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel labeluser = new JLabel("ТАБЛИЦА ПОЛЬЗОВАТЕЛЕЙ");
        panel.add(labeluser, gbc);
        gbc.gridy = 1;

        //users
        String[] usersColumnNames = {"Логин", "Пароль", "Роль"};
        usersTableModel = new DefaultTableModel(usersColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        users = new JTable(usersTableModel);
        JScrollPane usersScrollPane = new JScrollPane(users);
        panel.add(usersScrollPane, gbc);

        gbc.gridy = 2;
        gbc.insets = new Insets(20, 0, 0, 0);

        JLabel labelproducts = new JLabel("ТАБЛИЦА ПРОДУКТОВ");
        panel.add(labelproducts, gbc);
        gbc.gridy = 3;

        //products
        String[] productColumnNames = {"ID", "Название", "Количество"};
        productTableModel = new DefaultTableModel(productColumnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }
        };
        product = new JTable(productTableModel);
        JScrollPane productScrollPane = new JScrollPane(product);
        panel.add(productScrollPane, gbc);

        gbc.gridx = 1;

        JButton refresh = new JButton("Обновить");
        panel.add(refresh, gbc);
        refresh.addActionListener(e -> loadData());

        gbc.gridy = 1;

        JButton save = new JButton("Сохранить данные в БД");
        panel.add(save, gbc);
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (product.isEditing()) {
                    product.getCellEditor().stopCellEditing();
                }
                List<Product> products = new ArrayList<>();
                for (int row = 0; row < productTableModel.getRowCount(); row++) {
                    int id = Integer.parseInt(productTableModel.getValueAt(row, 0).toString());
                    String name = productTableModel.getValueAt(row, 1).toString();
                    int quality = Integer.parseInt(productTableModel.getValueAt(row, 2).toString());

                    products.add(new Product(id, name, quality));
                }
                int updated = dbConnection.saveProducts(products);
                JOptionPane.showMessageDialog(TableFrame.this,
                        "Обновлено строк: " + updated,
                        "Сохранение",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void loadData() {
        //users
        usersTableModel.setRowCount(0);

        List<User> users = dbConnection.getAllUsers();
        for (User user : users) {
            Object[] rowData = {
                    user.getLogin(),
                    user.getPassword(),
                    user.getRole()
            };
            usersTableModel.addRow(rowData);
        }

        //products
        productTableModel.setRowCount(0);

        List<Product> products = dbConnection.getAllProducts();
        for (Product product : products) {
            Object[] rowData = {
                    product.getId(),
                    product.getName(),
                    product.getQuality()
            };
            productTableModel.addRow(rowData);
        }
    }
}
