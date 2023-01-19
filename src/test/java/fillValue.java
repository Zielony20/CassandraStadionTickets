import Dao.MatchDao;
import Dao.TicketDao;
import Dao.TicketReservationDao;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class fillValue {

    static String ipaddr = "127.0.0.1";
    static String namespace = "srds";
    static CassandraConnector cc = new CassandraConnector();

    public static void main(String[] args) {

        cc.connect(ipaddr,9042,namespace);
        dbManager agent = new dbManager(cc.getSession());
        agent.getMatchesDao().create(1,"Arsenal","Chelsea","Wembley", LocalDate.of(2022,1,15),"Premier League",1);
        agent.getMatchesDao().create(2,"Arsenal","Manchester City","Wembley", LocalDate.of(2022,1,23),"Premier League",3);
        agent.getMatchesDao().create(3,"Arsenal","Manchester Utd","Wembley", LocalDate.of(2022,2,23),"Premier League",5);
        agent.getMatchesDao().create(4,"Arsenal","Tottenham","Wembley", LocalDate.of(2022,2,28),"Premier League",7);
        agent.getMatchesDao().create(5,"Arsenal","Newcastle","Wembley", LocalDate.of(2022,3,1),"Premier League",9);
        agent.getMatchesDao().create(6,"Arsenal","Real Madrid","Wembley", LocalDate.of(2022,2,14),"Champions League",1);

        List<String> sectors = Arrays.asList("A", "B", "C", "D");
        int placeCount = 50;
        int matchesCount = 6;
        List<Integer> places = IntStream.rangeClosed(1,0+placeCount).boxed().collect(Collectors.toList());

        for(int m=1;m<=matchesCount;m++){

            for(String sector: sectors){
                agent.getAvaibleTicketsDao().create(m,sector,places);
            }
        }
        cc.close();
    }

}




