package org.aminakov.cassandra;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import org.aminakov.cassandra.dao.Constants;

  public class App
{
    public static void main( String[] args ) {
//         List<User> users = new ArrayList<User>();
////         Cassandra cassandra = new Cassandra();
//         for (int i = 0; i < 100; i++) {
//                 User user = new User();
//                 user.setFirstName("firstName" + i);
//                 user.setLastName("lastName" + 1);
//                 user.setPhone("phone" + 1);
//                 user.setPhoneHash("phoneHash" + 1);
//                 user.setNickname("nickName" + 1);
//                 user.setEmail("email" + 1);
//                 user.setPostalCode("postalCode" + 1);
//                 user.setBirthDate("birthDate" + 1);
//                 user.setBio("bio" + 1);
//                 user.setSex("sex" + 1);
//                 user.setAvatar("avatar" + 1);
//                 user.setWallpaper("wallpaper" + 1);
//                 user.setPin("pin" + 1);
//                 user.setRegistrationDate(System.currentTimeMillis());
//                 user.setSmsNickname("smsNickName" + 1);
//                 users.add(user);
//         }
//         for (User u : users) {
//            cassandra.insert(u);
//         }
        Constants constants = new Constants("TestCluster", "Qwerty", "TestCf", "127.0.0.1");
        ColumnFamilyDefinition cf = constants.getNewCfDef("familyCf");
        constants.getCurrentClstr().addColumnFamily(cf);
        StringSerializer stringSerializer = StringSerializer.get();
        Mutator<String> mutator = HFactory.createMutator(constants.getKeyspace(), stringSerializer);
        mutator.insert("names", cf.getName(), HFactory.createStringColumn("1", "Ivanov"));
        mutator.insert("names", cf.getName(), HFactory.createStringColumn("2", "Sidorov"));
        mutator.insert("names", cf.getName(), HFactory.createStringColumn("3", "Petrov"));
        System.out.println("Done..");
    }
}