package org.aminakov.cassandra.dao;

import me.prettyprint.cassandra.model.HColumnImpl;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.SliceQuery;
import org.aminakov.cassandra.model.User;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImplTests {
    static ObjectMapper jsonMapper = new ObjectMapper();
    UserDao userDao;
    List<User> users;
    List<String> keys;
    Constants constants;

    @Before
    public void setupTests() {
        constants = new Constants("TestCluster", "Qwerty", "Users", "127.0.0.1");
        userDao = new UserDaoImpl();

        users = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            User user = new User();
            user.setId(String.valueOf(i));
            user.setFirstName("firstName" + i);
            user.setLastName("lastName" + i);
            user.setPhone("phone" + i);
            user.setPhoneHash("phoneHash" + i);
            user.setNickname("nickName" + i);
            user.setEmail("email" + i);
            user.setPostalCode("postalCode" + i);
            user.setBirthDate("birthDate" + i);
            user.setBio("bio" + i);
            user.setSex("sex" + i);
            user.setAvatar("avatar" + i);
            user.setWallpaper("wallpaper" + i);
            user.setPin("pin" + i);
            user.setRegistrationDate(System.currentTimeMillis());
            user.setSmsNickname("smsNickName" + i);
            users.add(user);
        }
    }

    @Test
    public void persistUserTest() throws IOException {
        try {
            keys = userDao.persist(users);
            SliceQuery<String, String, String> usersFromCassondra = HFactory.createSliceQuery(constants.getKeyspace(), StringSerializer.get(), StringSerializer.get(), StringSerializer.get());
            usersFromCassondra.setColumnFamily(constants.CF_NAME).setKey("3").setRange("", "", false, Integer.MAX_VALUE - 1);
            ColumnSliceIterator<String, String, String> result = new ColumnSliceIterator<>(usersFromCassondra, "", "", false);
            HColumnImpl<String, String> column = null;
            while (result.hasNext()) {
                column = (HColumnImpl<String, String>) result.next();
            }
            User resultUser;
            resultUser = jsonMapper.readValue(column != null ? column.getValue() : null, User.class);

            Assert.assertNotNull(constants);
            Assert.assertNotNull(userDao);
            Assert.assertNotNull(keys);
            Assert.assertEquals(users.get(3).getFirstName(), resultUser.getFirstName());

        } finally {
            if (constants != null) {
                constants.getCurrentClstr().dropKeyspace("Qwerty");
            }
        }
    }

    @Test
    public void deleteUserTest() throws IOException {
        try {
            keys = userDao.persist(users);
            userDao.delete(users.get(3));
            SliceQuery<String, String, String> usersFromCassondra = HFactory.createSliceQuery(constants.getKeyspace(), StringSerializer.get(), StringSerializer.get(), StringSerializer.get());
            usersFromCassondra.setColumnFamily(constants.CF_NAME).setKey("3").setRange("", "", false, Integer.MAX_VALUE - 1);
            ColumnSliceIterator<String, String, String> result = new ColumnSliceIterator<>(usersFromCassondra, "", "", false);
            HColumnImpl<String, String> column = null;
            while (result.hasNext()) {
                column = (HColumnImpl<String, String>) result.next();
            }
            Assert.assertNull(column);

        } finally {
            if (constants != null) {
                constants.getCurrentClstr().dropKeyspace("Qwerty");
            }
        }
    }
}
