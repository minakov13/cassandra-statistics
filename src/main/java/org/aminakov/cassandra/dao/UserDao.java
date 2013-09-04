package org.aminakov.cassandra.dao;

import org.aminakov.cassandra.model.User;

import java.util.List;

public interface UserDao {

    /**
     * Returns user by his/her phone number. If no such number of phone found
     * returns null
     *
     * @param phoneNumber is user's expected phone number
     * @return User with such phone number if found, otherwise null
     */
    User getByPhone(String phoneNumber);

    /**
     * Returns all users by their phone numbers.
     * returns null
     *
     * @param phoneHashes is the list of users'  phone numbers hashes
     * @return List of users with such phone number hashes
     */
    List<User> getByPhoneHashes(List<String> phoneHashes);

    /**
     * Returns user by user's nickname. If no such nickname or phone found
     * returns null. There could be only one user with such nickname
     *
     * @param nickname is user's expected nickname
     * @return User with such nickname if found, otherwise null
     */
    User getByNickname(String nickname);

    List<User> getByNicknames(List<String> nicknames);

    User getBySMSNickname(String nickname);

    /**
     * Returned user id list based on phone list.
     *
     * @param phoneNumbers The hash phone list.
     * @return List of users id found in the system.
     */
    List<String> findUserIdsByPhone(List<String> phoneNumbers);

    /**
     * Returns a list of users by the email. If no users with such email are registered, returns empty list
     *
     * @param email - users' expected email
     * @param pagination - Pagination object defining the output range
     * @return list of users with matching email
     */
    List<User> getByEmail(String email);

    /**
     * Returns a list of users with the specified role. If no users with such role are registered, returns empty list
     *
     * @param role - users' role
     * @param pagination - Pagination object defining the output range
     * @return list of users with matching role
     */
//    List<User> getByRole(SpotbotRole role, Pagination pagination);

    /**
     * Get users starting from fromId.
     *
     * @param fromId Start from user id.
     * @param count  The count of return entities.
     * @return The users id list.
     */
    List<User> getAll(String fromId, int count);

    /**
     * todo move me to parent after refactoring is finishing
     * Returns count of users in database
     *
     * @return count of users in database
     */
    int countAll();

}
