package org.aminakov.cassandra.dao;

/**
 * @author Oleksandr Minakov
 * date: 8/4/13
 */

import org.aminakov.cassandra.model.User;
import java.util.List;

public interface UserDao extends AbstractDao<String, User> {

    /**
     * Returns user by his/her last name. If no such number of phone found,
     * returns empty list
     *
     * @param lastName Is user's expected phone number's hash.
     * @return list of users with matching last name
     */
    List<User> getByLastName(String lastName);

    /**
     * Returns user by his/her first name. If no such number of phone found,
     * returns empty list
     *
     * @param firstName Is user's expected phone number's hash.
     * @return list of users with matching first name
     */
    List<User> getByFirstName(String firstName);

    /**
     * Returns a list of users by the email. If no users with such email are registered, returns empty list
     *
     * @param email Users' expected email.
     * @return list of users with matching email
     */
    List<User> getByEmail(String email);

    /**
     * Get users starting from fromId.
     *
     * @param indexName Name of the index for search.
     * @param startFrom Start from user id.
     * @param count  The count of return entities.
     * @return list of users
     */
    List<User> getAll(String indexName, String startFrom, int count);

    /**
     * Returns count of users in database
     *
     * @return count of users in database
     */
    int countAll();
}
