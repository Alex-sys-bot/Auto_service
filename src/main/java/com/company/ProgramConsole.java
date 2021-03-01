package com.company;

import com.company.dao.DaoImpl;
import com.company.model.Client;
import com.company.model.User;
import com.company.service.ServiceDaoImpClient;
import com.company.service.ServiceDaoImplUser;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;

public class ProgramConsole {
    public static void main(String[] args){
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        DaoImpl<User, Integer> daoClient = new ServiceDaoImplUser(factory);

        List<User> listClients = new ArrayList<>();
        listClients.addAll(daoClient.readAll());

        for (User user: listClients) {
            System.out.println(user);
        }

        factory.close();
    }
}
