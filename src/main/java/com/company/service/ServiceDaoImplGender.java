package com.company.service;

import com.company.dao.DaoImplGender;
import com.company.model.Gender;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;

public class ServiceDaoImplGender implements DaoImplGender<Gender> {

    private final SessionFactory factory;

    public ServiceDaoImplGender(SessionFactory factory) {
        this.factory = factory;
    }

    @Override
    public List<Gender> selectAll() {
        try (Session session = factory.openSession()){
            Query<Gender> query = session.createQuery("from Gender ");
            return query.list();
        }
    }
}
