import com.datastax.driver.core.*;

import java.util.List;

public class CassandraConnector {
    private Cluster cluster;
    private Session session;

    public void connect(String ipaddr, Integer port, String namespace,  ConsistencyLevel consistencyLevel) {


        Cluster.Builder b = Cluster.builder().addContactPoint(ipaddr)
                .withQueryOptions(new QueryOptions().setConsistencyLevel(consistencyLevel))
                .withPoolingOptions(new PoolingOptions().setCoreConnectionsPerHost(HostDistance.LOCAL, 4)
                .setMaxConnectionsPerHost(HostDistance.LOCAL, 10));

        if (port != null) {
            b.withPort(port);
        }
        cluster = b.build();
        session = cluster.connect();

        Metadata metadata = cluster.getMetadata();
        boolean createKeyspace = true;
        List<KeyspaceMetadata> keyspaces = metadata.getKeyspaces();
        for(KeyspaceMetadata keyspace: keyspaces) {
            if(keyspace.getName().equals(namespace)) {
                createKeyspace = false;
                break;
            }
        }
        if (createKeyspace){
            session.execute("CREATE KEYSPACE "+namespace+" WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 4};");
        }
        session.execute("Use "+namespace+";");
    }

    public Session getSession() {
        return this.session;
    }


    public void close() {
        session.close();
        cluster.close();
    }
}

