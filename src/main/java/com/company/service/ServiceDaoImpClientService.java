package com.company.service;

import com.company.dao.DaoImpl;
import com.company.model.ClientService;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import java.util.List;

public class ServiceDaoImpClientService implements DaoImpl<ClientService, Integer> {

    private final SessionFactory factory;

    public ServiceDaoImpClientService(SessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void create(ClientService clientService) {
        try (Session session = factory.openSession()){
            session.beginTransaction();
            session.save(clientService);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(ClientService clientService) {
        try (Session session = factory.openSession()){
            session.beginTransaction();
            session.update(clientService);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(ClientService clientService) {
        try (Session session = factory.openSession()){
            session.beginTransaction();
            session.delete(clientService);
            session.getTransaction().commit();
        }
    }

    @Override
    public ClientService readById(Integer integer) {
        try (Session session = factory.openSession()){
            return session.get(ClientService.class, integer);
        }
    }

    @Override
    public List<ClientService> readAll() {
        try (Session session = factory.openSession()){
            Query<ClientService> query = session.createQuery("from ClientService ");
            return query.list();
        }
    }
}
