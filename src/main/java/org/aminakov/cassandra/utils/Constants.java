package org.aminakov.cassandra.utils;

import me.prettyprint.cassandra.model.BasicColumnFamilyDefinition;
import me.prettyprint.cassandra.model.BasicKeyspaceDefinition;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;

public class Constants {

    public String CLUSTER_NAME;
    public String KEYSPACE_NAME;
    public String CF_NAME;
    public String HOST;

    private Cluster cluster;
    BasicColumnFamilyDefinition defaultColumnFamilyDefinition;
    BasicKeyspaceDefinition defaultKeyspaceDefinition;

    BasicKeyspaceDefinition getNewKeyspaceDef() {
        defaultKeyspaceDefinition = new BasicKeyspaceDefinition();
        defaultKeyspaceDefinition.setName(KEYSPACE_NAME);
        defaultKeyspaceDefinition.setDurableWrites(true);
        defaultKeyspaceDefinition.setReplicationFactor(1);
        defaultKeyspaceDefinition.setStrategyClass("org.apache.cassandra.locator.SimpleStrategy");
        return this.defaultKeyspaceDefinition;
    }

    public Keyspace getKeyspace() {
        getCurrentCluster();
        return HFactory.createKeyspace(KEYSPACE_NAME, cluster);
    }

    BasicColumnFamilyDefinition getNewCfDef(String name) {
        this.defaultColumnFamilyDefinition = new BasicColumnFamilyDefinition();
        this.defaultColumnFamilyDefinition.setKeyspaceName(KEYSPACE_NAME);
        this.defaultColumnFamilyDefinition.setComparatorType(ComparatorType.UTF8TYPE);
        this.defaultColumnFamilyDefinition.setName(name);
        return this.defaultColumnFamilyDefinition;
    }

    public Cluster getCurrentCluster() {
        if (this.cluster == null) {
            CassandraHostConfigurator cassandraHostConfigurator = new CassandraHostConfigurator();
            cassandraHostConfigurator.setHosts(HOST);
            cassandraHostConfigurator.setAutoDiscoverHosts(true);
            cassandraHostConfigurator.setPort(9160);
            cassandraHostConfigurator.setMaxActive(1);
            cassandraHostConfigurator.setCassandraThriftSocketTimeout(60 * 1000);

            this.cluster = HFactory.getOrCreateCluster(this.CLUSTER_NAME, cassandraHostConfigurator);
            return this.cluster;
        } else {
            return this.cluster;
        }
    }

    void addKeySpace(KeyspaceDefinition keyspaceDefinition) {
        boolean isPresent = false;
        for (KeyspaceDefinition def : cluster.describeKeyspaces()) {
            if (keyspaceDefinition.getName().equals(def.getName())) {
                isPresent = true;
            }
        }
        if (!isPresent) {
            cluster.addKeyspace(keyspaceDefinition);
        }
    }

    public void addColumnFamily(ColumnFamilyDefinition columnFamilyDefinition) {
        boolean isPresent = false;
        for (KeyspaceDefinition def : cluster.describeKeyspaces()) {
            for (ColumnFamilyDefinition cf : def.getCfDefs()) {
                if (cf.getName().equals(columnFamilyDefinition.getName()))
                    isPresent = true;
            }
        }
        if (!isPresent) {
            cluster.addColumnFamily(columnFamilyDefinition);
        }
    }

    public void dropColumnFamily(String columnFamilyName) {
        cluster.dropColumnFamily(KEYSPACE_NAME, columnFamilyName);
    }

    public Constants(String clName, String ksName, String cfName, String host) {
        HOST = host;
        CLUSTER_NAME = clName;
        KEYSPACE_NAME = ksName;
        CF_NAME = cfName;
        this.cluster = getCurrentCluster();
        defaultKeyspaceDefinition = getNewKeyspaceDef();
        defaultColumnFamilyDefinition = getNewCfDef(CF_NAME);
        addKeySpace(defaultKeyspaceDefinition);
        addColumnFamily(defaultColumnFamilyDefinition);
    }
}