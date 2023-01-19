package Dao;

import Logs.StatsManager;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

import java.util.Set;

public class AvaibleTicketsDao extends CassandraTable{


    public AvaibleTicketsDao(Session session){
        super(session);
    }


    public void create(int matchid, String sector,
                       Set<Integer> placeList){
        String query = new StringBuilder("INSERT INTO ")
                .append("AvaibleTickets ( matchid, sector, placeList )")
                .append("VALUES (")
                .append(matchid).append(",'")
                .append(sector).append("',")
                .append(placeList)
                //.append(counter).append("','")
                .append(");")
                .toString();
        StatsManager.getInstance().queryLog(query);
        session.execute(query);
    }
    public void create(AvaibleTickets at){
        String query = new StringBuilder("INSERT INTO ")
                .append("AvaibleTickets ( matchid, sector, placeList )")
                .append("VALUES (")
                .append(at.getMachtid()).append(",'")
                .append(at.getSector()).append("',")
                .append(at.getPlaceList())
                //.append(at.getAvailable()).append("','")
                .append(");")
                .toString();
        StatsManager.getInstance().queryLog(query);
        session.execute(query);
    }

    public void increase(int matchid, String sector, int place){
        String query = new StringBuilder("UPDATE AvaibleTickets SET ")
                //.append("available = ").append("available + 1, ")
                .append(" placeList = placeList + [").append(place).append("]")
                .append(" WHERE ")
                .append(" matchid = ").append(matchid)
                .append(" and sector = '").append(sector)
                .append("';")
                .toString();
        StatsManager.getInstance().queryLog(query);

        session.execute(query);
    }

    public void decrese(int matchid, String sector, int place){

        String query = new StringBuilder("UPDATE AvaibleTickets SET  ")
                //.append("available = ").append("available - 1, ")
                .append("placeList = placeList - [").append(place).append("]")
                .append(" WHERE ")
                .append(" matchid = ").append(matchid)
                .append(" and sector = '").append(sector)
                .append("';")
                .toString();
        StatsManager.getInstance().queryLog(query);
        session.execute(query);
    }

    public ResultSet getAvaibleTickets(int matchid, String sector){

        String query = new StringBuilder("SELECT placeList FROM AvaibleTickets where ")
                .append(" matchid = ").append(matchid)
                .append(" and sector = '").append(sector)
                .append("';")
                .toString();
        StatsManager.getInstance().queryLog(query);
        ResultSet rs = session.execute(query);

        return rs;
    }
    public ResultSet getAvaibleTickets(){

        String query = new StringBuilder("SELECT placeList FROM AvaibleTickets ")
                .append(";")
                .toString();
        StatsManager.getInstance().queryLog(query);
        ResultSet rs = session.execute(query);

        return rs;
    }

}



