package com.company;

import com.company.dao.DaoImpl;
import com.company.model.Client;
import com.company.service.ServiceDaoImpClient;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;

public class ProgramConsole {
    public static void main(String[] args){
        SessionFactory factory = new Configuration().configure().buildSessionFactory();
        DaoImpl<Client, Integer> daoClient = new ServiceDaoImpClient(factory);

        List<Client> listClients = new ArrayList<>();
        listClients.addAll(daoClient.readAll());

        for (Client client: listClients) {
            System.out.println(client);
        }

        factory.close();
    }
}
