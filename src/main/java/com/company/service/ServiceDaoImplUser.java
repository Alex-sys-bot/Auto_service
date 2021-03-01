package com.company.service;

import com.company.dao.DaoImpl;
import com.company.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class ServiceDaoImplUser implements DaoImpl<User, Integer> {

    private final SessionFactory factory;

    public ServiceDaoImplUser(SessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void create(User user) {

    }

    @Override
    public void update(User user) {

    }

    @Override
    public void delete(User user) {

    }

    @Override
    public User readById(Integer integer) {
        return null;
    }

    @Override
    public List<User> readAll() {
        try(Session session = factory.openSession()){
            Query<User> query = session.createQuery("from User");
            return query.list();
        }
    }
}
