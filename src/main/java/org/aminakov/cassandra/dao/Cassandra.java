package org.aminakov.cassandra.dao;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import org.aminakov.cassandra.model.User;

public class Cassandra {

    Cluster cluster = null;
    Keyspace keyspace = null;

    public Cassandra() {
    }

    public void insert(User user) {
        cluster = HFactory.getOrCreateCluster("Test Cluster", "localhost:9160");
        keyspace =  HFactory.createKeyspace("USERKEYSPACE", cluster);
        ColumnFamilyDefinition cf = HFactory.createColumnFamilyDefinition("USERKEYSPACE","userColumnFamily");
        cluster.addColumnFamily(cf);
        StringSerializer stringSerializer = StringSerializer.get();
        Mutator<String> mutator = HFactory.createMutator(keyspace, stringSerializer);
        mutator.insert(user.getId(), cf.getName(), HFactory.createStringColumn("firstName", user.getFirstName()));
        mutator.insert(user.getId(), cf.getName(), HFactory.createStringColumn("lastName", user.getLastName()));
        mutator.insert(user.getId(), cf.getName(), HFactory.createStringColumn("phone", user.getPhone()));
        mutator.insert(user.getId(), cf.getName(), HFactory.createStringColumn("phoneHash", user.getPhoneHash()));
        mutator.insert(user.getId(), cf.getName(), HFactory.createStringColumn("nickName", user.getNickname()));
        mutator.insert(user.getId(), cf.getName(), HFactory.createStringColumn("email", user.getEmail()));
        mutator.insert(user.getId(), cf.getName(), HFactory.createStringColumn("postalCode", user.getPostalCode()));
        mutator.insert(user.getId(), cf.getName(), HFactory.createStringColumn("birthDate", user.getBirthDate()));
        mutator.insert(user.getId(), cf.getName(), HFactory.createStringColumn("bio", user.getBio()));
        mutator.insert(user.getId(), cf.getName(), HFactory.createStringColumn("sex", user.getSex()));
        mutator.insert(user.getId(), cf.getName(), HFactory.createStringColumn("avatar", user.getAvatar()));
        mutator.insert(user.getId(), cf.getName(), HFactory.createStringColumn("wallpaper", user.getWallpaper()));
        mutator.insert(user.getId(), cf.getName(), HFactory.createStringColumn("pin", user.getPin()));
        mutator.insert(user.getId(), cf.getName(), HFactory.createStringColumn("registrationDate", user.getRegistrationDate().toString()));
        mutator.insert(user.getId(), cf.getName(), HFactory.createStringColumn("smsNickname", user.getSmsNickname()));
        System.out.println("Done..");
    }
}
