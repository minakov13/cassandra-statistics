package org.aminakov.cassandra;

import org.aminakov.cassandra.dao.Constants;
import org.aminakov.cassandra.dao.UserDao;
import org.aminakov.cassandra.dao.UserDaoImpl;
import org.aminakov.cassandra.model.User;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App
{
    public static ObjectMapper jsonMapper = new ObjectMapper();

    public static void main( String[] args ) throws IOException {
        List<User> users = new ArrayList<>();
        List<String> results = new ArrayList<>();
        UserDao userDao = new UserDaoImpl();
        for (int i = 0; i < 20; i++) {
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
        Constants constants = new Constants("TestCluster", "Qwerty", "Users", "127.0.0.1");
       /* for (User u : users) {
            String json = jsonMapper.writeValueAsString(u);
            System.out.println(u.toString());
            System.out.println("============= JSON ===========");
            System.out.println(json);
           *//* try {
                Mutator<String> mutator = HFactory.createMutator(constants.getKeyspace(), StringSerializer.get());
                System.out.println("Creating new mutator");

                mutator.insert(u.getId(), constants.CF_NAME, HFactory.createStringColumn(u.getId(), json));

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }*//*

            System.out.println("User was added, id:" + u.getId());
        }*/
        results = userDao.persist(users);
        for (String s : results) {
            System.out.println(s);
        }

        /*SliceQuery<String, String, String> usersFromCassondra = HFactory.createSliceQuery(constants.getKeyspace(), StringSerializer.get(), StringSerializer.get(), StringSerializer.get());
        usersFromCassondra.setColumnFamily(constants.CF_NAME).setKey("9").setRange("", "", false, Integer.MAX_VALUE - 1);
        ColumnSliceIterator<String, String, String> result = new ColumnSliceIterator<String, String, String>(usersFromCassondra, "", "", false);
        while (result.hasNext()) {
            HColumnImpl<String, String> column = (HColumnImpl<String, String>) result.next();
            User res = null;
            res = jsonMapper.readValue(column.getValue(), User.class);
            System.out.println(res.toString());
        }*/
        for (User user : users) {
            userDao.delete(user);
        }

//        mutator.insert("names", cf.getName(), HFactory.createStringColumn("1", "Ivanov"));
//        mutator.insert("names", cf.getName(), HFactory.createStringColumn("2", "Sidorov"));
//        mutator.insert("names", cf.getName(), HFactory.createStringColumn("3", "Petrov"));
//        System.out.println("Done..");
    }
}