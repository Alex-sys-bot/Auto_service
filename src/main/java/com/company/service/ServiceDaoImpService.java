package com.company.service;

import com.company.dao.DaoImpl;
import com.company.model.Service;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class ServiceDaoImpService implements DaoImpl<Service, Integer> {
    private final SessionFactory factory;

    public ServiceDaoImpService(SessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void create(Service service) {
        try (Session session = factory.openSession()){
            session.beginTransaction();
            session.save(service);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Service service) {
        try (Session session = factory.openSession()){
            session.beginTransaction();
            session.update(service);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Service service) {
        try (Session session = factory.openSession()){
            session.beginTransaction();
            session.delete(service);
            session.getTransaction().commit();
        }
    }

    @Override
    public Service readById(Integer integer) {
        try (Session session = factory.openSession()){
            return session.get(Service.class, integer);
        }
    }

    @Override
    public List<Service> readAll() {
        try (Session session = factory.openSession()){
            Query<Service> query = session.createQuery("from Service ");
            return query.list();
        }
    }
}
