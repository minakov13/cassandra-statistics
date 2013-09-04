package org.aminakov.cassandra.dao;

import me.prettyprint.cassandra.model.BasicColumnFamilyDefinition;
import me.prettyprint.cassandra.model.BasicKeyspaceDefinition;
import me.prettyprint.cassandra.service.CassandraHostConfigurator;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;

public class Constants {

    public String CLUSTER_NAME;
    public String KEYSPACE_NAME;
    public String CF_NAME;
    public String HOST;

    private Cluster cluster;
    private BasicColumnFamilyDefinition columnFamilyDefinition;
    private BasicKeyspaceDefinition keyspaceDefinition;

    private BasicKeyspaceDefinition getNewKeyspaceDef() {
        keyspaceDefinition = new BasicKeyspaceDefinition();
        keyspaceDefinition.setName(KEYSPACE_NAME);
        keyspaceDefinition.setDurableWrites(true);
        keyspaceDefinition.setReplicationFactor(1);
        keyspaceDefinition.setStrategyClass("org.apache.cassandra.locator.SimpleStrategy");
        return this.keyspaceDefinition;
    }

    public Keyspace getKeyspace() {
        getCurrentClstr();
        return HFactory.createKeyspace(KEYSPACE_NAME, cluster);
    }

    public BasicColumnFamilyDefinition getNewCfDef(String name) {
        this.columnFamilyDefinition = new BasicColumnFamilyDefinition();
        this.columnFamilyDefinition.setKeyspaceName(KEYSPACE_NAME);
        this.columnFamilyDefinition.setName(name);
        return this.columnFamilyDefinition;
    }

    public Cluster getCurrentClstr() {

        boolean keyspaceFlag = false;
        boolean columnFamilyUsers = false;

        if (this.cluster == null) {
            this.keyspaceDefinition = this.getNewKeyspaceDef();

            CassandraHostConfigurator cassandraHostConfigurator = new CassandraHostConfigurator();
            cassandraHostConfigurator.setHosts(HOST);
            cassandraHostConfigurator.setAutoDiscoverHosts(true);
            cassandraHostConfigurator.setPort(9160);
            cassandraHostConfigurator.setMaxActive(1);
            cassandraHostConfigurator.setCassandraThriftSocketTimeout(60 * 1000);

            this.cluster = HFactory.getOrCreateCluster(this.CLUSTER_NAME, cassandraHostConfigurator);

            for (KeyspaceDefinition def : cluster.describeKeyspaces()) {
                if (def.getName().equals(KEYSPACE_NAME)) {
                    keyspaceFlag = true;
                    for (ColumnFamilyDefinition cf : def.getCfDefs()) {

                        if (cf.getName().equals(CF_NAME))
                            columnFamilyUsers = true;
                    }
                }
            }

            if (!keyspaceFlag) {
                this.cluster.addKeyspace(keyspaceDefinition);
            }

            for (KeyspaceDefinition def : cluster.describeKeyspaces()) {
                if (def.getName().equals(KEYSPACE_NAME)) {
                    if (!columnFamilyUsers) {
                        this.columnFamilyDefinition = getNewCfDef(this.CF_NAME);
                        this.cluster.addColumnFamily(columnFamilyDefinition);
                    }
                }
            }
            return this.cluster;
        } else {
            return this.cluster;
        }
    }

    public Constants(String clName, String ksName, String cfName, String host) {
        HOST = host;
        CLUSTER_NAME = clName;
        KEYSPACE_NAME = ksName;
        CF_NAME = cfName;
        this.cluster = getCurrentClstr();
    }
}