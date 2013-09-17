package org.aminakov.cassandra.dao;

/**
 * @author Oleksandr Minakov
 * date: 8/4/13
 */

import me.prettyprint.cassandra.model.HColumnImpl;
import me.prettyprint.cassandra.serializers.CompositeSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.ColumnSliceIterator;
import me.prettyprint.hector.api.beans.*;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;
import org.aminakov.cassandra.model.User;
import org.aminakov.cassandra.utils.Constants;
import org.codehaus.jackson.map.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {

    static Constants constants = new Constants("TestCluster", "Qwerty", "Users", "127.0.0.1");
    static ObjectMapper jsonMapper = new ObjectMapper();
    static CompositeSerializer compositeSerializer = new CompositeSerializer();
    static final int COUNT = 100;


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
        updateIndexes(entity);
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

    private void updateIndexes(User user) {
        ColumnFamilyDefinition cfDef = HFactory.createColumnFamilyDefinition("Qwerty",
                "Indexes", ComparatorType.COMPOSITETYPE);
        cfDef.setComparatorTypeAlias("(UTF8Type, UTF8Type)");
        constants.addColumnFamily(cfDef);
        String userJson = null;
        try {
            userJson = jsonMapper.writeValueAsString(user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Composite key = new Composite();
        Mutator<String> mutator = HFactory.createMutator(constants.getKeyspace(), StringSerializer.get());

        key.addComponent(user.getLastName(), StringSerializer.get());
        key.addComponent(user.getId(), StringSerializer.get());
        mutator.insert("lastNameIndex", "Indexes", HFactory.createColumn(key, userJson));
        key = new Composite();
        key.addComponent(user.getFirstName(), StringSerializer.get());
        key.addComponent(user.getId(), StringSerializer.get());
        mutator.insert("firstNameIndex", "Indexes", HFactory.createColumn(key, userJson));
        key = new Composite();
        key.addComponent(user.getEmail(), StringSerializer.get());
        key.addComponent(user.getId(), StringSerializer.get());
        mutator.insert("emailIndex", "Indexes", HFactory.createColumn(key, userJson));

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
        ColumnSliceIterator<String, String, String> result = new ColumnSliceIterator<String, String, String>(usersFromCassondra, "", "", false);
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
        List<User> users = new ArrayList<User>();
        for (String key : keys) {
            users.add(getById(key));
        }
        return users;
    }

    @Override
    public List<User> getByLastName(String lastName) {
        List<String> keys = new ArrayList<String>();
        Composite startRange = new Composite();
        startRange.add(0, lastName);
        SliceQuery<String, Composite, String> query = HFactory.createSliceQuery(constants.getKeyspace(), StringSerializer.get(),
                compositeSerializer, StringSerializer.get());
        query.setKey("lastNameIndex").setColumnFamily("Indexes").setRange(startRange, null, false, COUNT);
        QueryResult<ColumnSlice<Composite, String>> result = query.execute();
        ColumnSlice<Composite, String> columnSlice = result.get();
        for ( HColumn<Composite, String> col: columnSlice.getColumns() ) {
                keys.add(col.getName().get(1, StringSerializer.get()));
        }
        return getById(keys);
    }

    @Override
    public List<User> getByFirstName(String firstName) {
        List<String> keys = new ArrayList<String>();
        Composite startRange = new Composite();
        startRange.add(0, firstName);
        SliceQuery<String, Composite, String> query = HFactory.createSliceQuery(constants.getKeyspace(), StringSerializer.get(),
                compositeSerializer, StringSerializer.get());
        query.setKey("firstNameIndex").setColumnFamily("Indexes").setRange(startRange, null, false, COUNT);
        QueryResult<ColumnSlice<Composite, String>> result = query.execute();
        ColumnSlice<Composite, String> columnSlice = result.get();
        for ( HColumn<Composite, String> col: columnSlice.getColumns() ) {
            keys.add(col.getName().get(1, StringSerializer.get()));
        }
        return getById(keys);
    }

    @Override
    public List<User> getByEmail(String email) {
        List<String> keys = new ArrayList<String>();
        Composite startRange = new Composite();
        startRange.add(0, email);
        SliceQuery<String, Composite, String> query = HFactory.createSliceQuery(constants.getKeyspace(), StringSerializer.get(),
                compositeSerializer, StringSerializer.get());
        query.setKey("emailIndex").setColumnFamily("Indexes").setRange(startRange, null, false, COUNT);
        QueryResult<ColumnSlice<Composite, String>> result = query.execute();
        ColumnSlice<Composite, String> columnSlice = result.get();
        for ( HColumn<Composite, String> col: columnSlice.getColumns() ) {
            keys.add(col.getName().get(1, StringSerializer.get()));
        }
        return getById(keys);
    }

    @Override
    public List<User> getAll(String indexName, String startFrom, int count) {
        List<User> users = new ArrayList<User>();
        Composite startRange = new Composite();
        startRange.add(0, startFrom);
        SliceQuery<String, Composite, String> query = HFactory.createSliceQuery(constants.getKeyspace(), StringSerializer.get(),
                compositeSerializer, StringSerializer.get());
        query.setKey(indexName).setColumnFamily("Indexes").setRange(startRange, null, false, count);
        QueryResult<ColumnSlice<Composite, String>> result = query.execute();
        ColumnSlice<Composite, String> columnSlice = result.get();
        for ( HColumn<Composite, String> col: columnSlice.getColumns() ) {
            try {
                users.add(jsonMapper.readValue(col.getValue(), User.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return users;
    }

    private List<String> getRowKeys(String startFrom, int count) {
        List<String> keys = new ArrayList<String>();
        RangeSlicesQuery<String, String, String> userIds = HFactory.createRangeSlicesQuery(constants.getKeyspace(),
                StringSerializer.get(), StringSerializer.get(), StringSerializer.get());
        userIds.setColumnFamily(constants.CF_NAME).setKeys(startFrom, "").setReturnKeysOnly().setRowCount(count);
        QueryResult<OrderedRows<String, String, String>> result = userIds.execute();
        OrderedRows<String, String, String> orderedRows = result.get();
        List<Row<String, String, String>> keyList = orderedRows.getList();
        for (Row<String, String, String> row : keyList) {
            keys.add(row.getKey());
        }
        return keys;
    }

    @Override
    public int countAll() {
        return getRowKeys(null, Integer.MAX_VALUE - 1).size();
    }
}
