package org.aminakov.cassandra.dao;

import org.aminakov.cassandra.model.User;

import java.util.List;

public interface UserDao extends AbstractDao<String, User> {

    List<User> getByLastName(String lastName);

    List<User> getByFirstName(String firstName);

    List<User> getByEmail(String email);

    List<User> getAll(String indexName, String startFrom, int count);

    int countAll();
}
