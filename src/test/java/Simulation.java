import Logs.StatsManager;
import com.datastax.driver.core.ConsistencyLevel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;


public class Simulation {

    private static final int NUMBER_OF_CUSTOMERS = 5000;
    static String ipaddr = "127.0.0.1";
    static String namespace = "srds";
    static ConsistencyLevel consistencyLevel = ConsistencyLevel.QUORUM;
    static CassandraConnector cc = new CassandraConnector();

    public static void main(String[] args) {

        cc.connect(ipaddr,9042,namespace, consistencyLevel);
        dbManager dbManager = new dbManager(cc.getSession());
        StatsManager.getInstance().setQueryLogs(false);


        List<Customer> customers = runCustomers(dbManager);

        StatsManager.getInstance().summorize();
        cc.close();
    }

    public static List<Customer> runCustomers(dbManager dbManager) {
        List<Thread> threads = new ArrayList<>();
        List<Customer> customers = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++) {
            int ticketsCount = ThreadLocalRandom.current().nextInt(1, 4 + 1);
            int match = ThreadLocalRandom.current().nextInt(1, 6 + 1);
            List<String> sectors = new ArrayList<>(Arrays.asList("A","B","C","D"));
            String sector = sectors.get(ThreadLocalRandom.current().nextInt(sectors.size()));

            Customer c = new Customer(ticketsCount, match, sector, dbManager);
            customers.add(c);
        }
        System.out.println("Czas zakupów");
        StatsManager.getInstance().startTimer();

        for (int i = 0; i < NUMBER_OF_CUSTOMERS; i++) {
            Thread t = new Thread(customers.get(i));
            t.start();
            threads.add(t);
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return customers;
    }


}

