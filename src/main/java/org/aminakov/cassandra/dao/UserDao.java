package org.aminakov.cassandra.dao;

import org.aminakov.cassandra.model.User;

import java.util.List;

public interface UserDao extends AbstractDao<String, User> {

    User getByPhone(String phoneNumber);

    List<User> getByPhoneHashes(List<String> phoneHashes);

    User getByNickname(String nickname);

    List<User> getByNicknames(List<String> nicknames);

    User getBySMSNickname(String nickname);

    List<String> findUserIdsByPhone(List<String> phoneNumbers);

    List<User> getByEmail(String email);

    List<User> getAll(int count);

    int countAll();
}
