package com.company.dao;



import java.util.List;

public interface DaoImpl<Entity, Key> {

    void create(Entity entity);
    void update(Entity entity);
    void delete(Entity entity);
    Entity readById(Key key);
    List<Entity> readAll();
}
