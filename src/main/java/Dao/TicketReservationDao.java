package Dao;

import Logs.StatsManager;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TicketReservationDao extends CassandraTable {

    public TicketReservationDao(Session session){
        super(session);
    }

    public void create(UUID ticketid, int matchid, String sector,
                       int place, UUID userid){
        String query = new StringBuilder("INSERT INTO ")
                .append("reservedTicketByUser ( ticketid, userid, matchid, sector, place)")
                .append("VALUES (")
                .append(ticketid).append(",")
                .append(userid).append(",")
                .append(matchid).append(",'")
                .append(sector).append("',")
                .append(place).append(");")
                .toString();
        StatsManager.getInstance().queryLog(query);
        session.execute(query);
        StatsManager.getInstance().insertIntoTicketsReservation();
    }

    public List<Ticket> getAllReservation(){
        String query = "SELECT * FROM reservedTicketByUser;";

        ResultSet rs = session.execute(query);

        List<Ticket> allTickets = new ArrayList<>();
        rs.forEach(
                r->allTickets.add(new Ticket(
                        r.getUUID("ticketid"),
                        r.getInt("matchid"),
                        r.getString("sector"),
                        r.getInt("place"),
                        r.getUUID("userid")
                ))
        );
        return allTickets;
    }

    public List<TicketReservation> getReservation(int matchid, String sector, int place){

        String query = new StringBuilder("SELECT * FROM reservedTicketByUser where ")
                .append(" matchid = ").append(matchid)
                .append(" and sector = '").append(sector)
                .append("' and place = ").append(place)
                .append(";")
                .toString();
        StatsManager.getInstance().queryLog(query);

        ResultSet rs = session.execute(query);

        List<TicketReservation> allTickets = new ArrayList<>();
        rs.forEach(
                r->allTickets.add(new TicketReservation(
                        r.getUUID("ticketid"),
                        r.getInt("matchid"),
                        r.getString("sector"),
                        r.getInt("place"),
                        r.getUUID("userid")
                ))
        );
        return allTickets;
    }

    public List<Ticket> getReservation(int matchid, String sector){

        String query = new StringBuilder("SELECT * FROM reservedTicketByUser where ")
                .append(" matchid = ").append(matchid)
                .append(" and sector = '").append(sector)
                .append("';")
                .toString();
        StatsManager.getInstance().queryLog(query);

        ResultSet rs = session.execute(query);

        List<Ticket> allTickets = new ArrayList<>();
        rs.forEach(
                r->allTickets.add(new Ticket(
                        r.getUUID("ticketid"),
                        r.getInt("matchid"),
                        r.getString("sector"),
                        r.getInt("place"),
                        r.getUUID("userid")
                ))
        );
        return allTickets;
    }
    public void removeReservation(int matchid,String sector,int place,UUID userid){

        String query = new StringBuilder("Delete FROM reservedTicketByUser where ")
                .append(" matchid = ").append(matchid)
                .append(" and sector ='").append(sector)
                .append("' and place = ").append(place)
                .append(" and userid = ").append(userid)
                .append(";")
                .toString();
        //System.out.print("\033[31m");
        StatsManager.getInstance().queryLog(query);
        //System.out.print("\033[0m");
        session.execute(query);

    }

    public void resetReservations(){
        String query = "TRUNCATE reservedTicketByUser;";
        StatsManager.getInstance().queryLog(query);
        session.execute(query);
    }
}
