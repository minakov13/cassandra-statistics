package org.aminakov.cassandra.dao;

import org.aminakov.cassandra.model.User;

import java.util.List;

public class UserDaoImpl implements UserDao {
    @Override
    public User getByPhone(String phoneNumber) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<User> getByPhoneHashes(List<String> phoneHashes) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public User getByNickname(String nickname) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<User> getByNicknames(List<String> nicknames) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public User getBySMSNickname(String nickname) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<String> findUserIdsByPhone(List<String> phoneNumbers) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<User> getByEmail(String email) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<User> getAll(String fromId, int count) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int countAll() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
