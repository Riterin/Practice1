package org.example;

import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Драйвер не найден");
        }

        new Register();
        new TableFrame();
    }
}
/*
--Таблица users
CREATE TABLE users (
    id serial primary key,
    login varchar(50) unique not null,
    password varchar(50) not null,
	role varchar(50) NOT NULL DEFAULT 'caliber_client'
);

--Дано изначальные пользователи
insert into users(login, password, role) values
    ('admin', 'AdMiN1@3', 'caliber_admin'),
    ('barber', 'br1', 'caliber_barbers'),
    ('client', 'cl1', 'caliber_client');

--Таблица products
CREATE TABLE products (
    id serial primary key,
    name varchar(50) unique not null,
    quality int not null
);

--Заполнение продуктов
insert into products(name, quality) values
    ('Лак для волос', 10),
    ('Шампунь', 15),
    ('Крем-паста', 5);

CREATE ROLE caliber_admin WITH LOGIN;
CREATE ROLE caliber_barbers WITH LOGIN;
CREATE ROLE caliber_client WITH LOGIN;
*/