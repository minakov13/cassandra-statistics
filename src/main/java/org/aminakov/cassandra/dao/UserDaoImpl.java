package org.aminakov.cassandra.dao;

import me.prettyprint.cassandra.model.HColumnImpl;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.SliceQuery;
import org.aminakov.cassandra.model.User;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {

    public static Constants constants = new Constants("TestCluster", "Qwerty", "Users", "127.0.0.1");
    public static ObjectMapper jsonMapper = new ObjectMapper();

    @Override
    public String persist(User entity) {
        String userJson;
        try {
            userJson = jsonMapper.writeValueAsString(entity);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        Mutator<String> mutator = HFactory.createMutator(constants.getKeyspace(), StringSerializer.get());
        mutator.insert(entity.getId(), constants.CF_NAME, HFactory.createStringColumn(entity.getId(), userJson));
        System.out.println("User was added, id:" + entity.getId());
        return entity.getId();
    }

    @Override
    public List<String> persist(List<User> entityList) {
        List<String> keys = new ArrayList<String>();
        for (User user : entityList) {
            keys.add(persist(user));
        }
        return keys;
    }

    @Override
    public void delete(User entity) {
        try {
            Mutator<String> mutator = HFactory.createMutator(constants.getKeyspace(), StringSerializer.get());
            mutator.delete(entity.getId(), constants.CF_NAME, null, StringSerializer.get());
            System.out.println("User was deleted, id:" + entity.getId());
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
        }
    }

    @Override
    public void delete(List<User> entityList) {
        for (User user : entityList) {
            delete(user);
        }
    }

    @Override
    public User getById(String key) {
        User user = null;
        SliceQuery<String, String, String> usersFromCassondra = HFactory.createSliceQuery(constants.getKeyspace(), 
                StringSerializer.get(), StringSerializer.get(), StringSerializer.get());
        usersFromCassondra.setColumnFamily(constants.CF_NAME).setKey(key).setRange("", "", false, Integer.MAX_VALUE - 1);
        ColumnSliceIterator<String, String, String> result = new ColumnSliceIterator<>(usersFromCassondra, "", "", false);
        while (result.hasNext()) {
            HColumnImpl<String, String> column = (HColumnImpl<String, String>) result.next();
            try {
                user = jsonMapper.readValue(column.getValue(), User.class);
            } catch (IOException e) {
                System.out.println("Error" + e.getMessage());
            }
        }
        return user;
    }

    @Override
    public List<User> getById(List<String> keys) {
        List<User> users = new ArrayList<>();
        for (String key : keys) {
            users.add(getById(key));
        }
        return users;
    }

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
    public List<User> getAll(int count) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int countAll() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
