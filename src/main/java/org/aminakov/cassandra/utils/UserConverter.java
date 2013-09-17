package org.aminakov.cassandra.utils;

/**
 * @author Oleksandr Minakov
 * date: 8/8/13
 */


import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.factory.HFactory;
import org.aminakov.cassandra.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserConverter {

    public static List<HColumn<String, String>> user2row(User user) {
        List<HColumn<String, String>> hColumns = new ArrayList<HColumn<String, String>>();
        hColumns.add(HFactory.createColumn("id", user.getId()));
        hColumns.add(HFactory.createColumn("firstName", user.getFirstName()));
        hColumns.add(HFactory.createColumn("lastName", user.getLastName()));
        hColumns.add(HFactory.createColumn("phone", user.getPhone()));
        hColumns.add(HFactory.createColumn("phoneHash", user.getPhoneHash()));
        hColumns.add(HFactory.createColumn("nickName", user.getNickname()));
        hColumns.add(HFactory.createColumn("email", user.getEmail()));
        hColumns.add(HFactory.createColumn("postalCode", user.getPostalCode()));
        hColumns.add(HFactory.createColumn("birthDate", user.getBirthDate()));
        hColumns.add(HFactory.createColumn("bio", user.getBio()));
        hColumns.add(HFactory.createColumn("sex", user.getSex()));
        hColumns.add(HFactory.createColumn("avatar", user.getAvatar()));
        hColumns.add(HFactory.createColumn("wallpaper", user.getWallpaper()));
        hColumns.add(HFactory.createColumn("pin", user.getPin()));
        hColumns.add(HFactory.createColumn("registrationDate", user.getRegistrationDate().toString()));
        hColumns.add(HFactory.createColumn("smsNickName", user.getPhone()));
        return hColumns;
    }

    public static User row2user(List<HColumn<String, String>> user) {
        User newUser = new User();
        for(HColumn<String, String> columns : user) {
            if(columns.getName().equals("id"))
                newUser.setId(columns.getValue());

            if(columns.getName().equals("firstName"))
                newUser.setFirstName(columns.getValue());

            if(columns.getName().equals("lastName"))
                newUser.setLastName(columns.getValue());

            if(columns.getName().equals("phone"))
                newUser.setPhone(columns.getValue());

            if(columns.getName().equals("phoneHash"))
                newUser.setPhoneHash(columns.getValue());

            if(columns.getName().equals("nickName"))
                newUser.setNickname(columns.getValue());

            if(columns.getName().equals("email"))
                newUser.setEmail(columns.getValue());

            if(columns.getName().equals("postalCode"))
                newUser.setPostalCode(columns.getValue());

            if(columns.getName().equals("birthDate"))
                newUser.setBirthDate(columns.getValue());

            if(columns.getName().equals("bio"))
                newUser.setBio(columns.getValue());

            if(columns.getName().equals("sex"))
                newUser.setSex(columns.getValue());

            if(columns.getName().equals("avatar"))
                newUser.setAvatar(columns.getValue());

            if(columns.getName().equals("wallpaper"))
                newUser.setWallpaper(columns.getValue());

            if(columns.getName().equals("pin"))
                newUser.setPin(columns.getValue());

            if(columns.getName().equals("registrationDate"))
                newUser.setRegistrationDate(Long.getLong(columns.getValue()));

            if(columns.getName().equals("smsNickName"))
                newUser.setPhone(columns.getValue());

        }
        return newUser;
    }
}
