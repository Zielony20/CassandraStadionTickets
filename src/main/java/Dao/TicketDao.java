package Dao;

import Logs.StatsManager;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TicketDao extends CassandraTable {

    public TicketDao(Session session){
        super(session);
    }

    public void create(UUID id, int matchid, String sector,
                       int place, UUID userid, String name,
                       String surname, String email){
        String query = new StringBuilder("INSERT INTO ")
                .append("Tickets ( ticketid, matchid, sector, place, userid, name, surname, email )")
                .append("VALUES (")
                .append(id).append(",")
                .append(matchid).append(",'")
                .append(sector).append("',")
                .append(place).append(",")
                .append(userid).append(",'")
                .append(name).append("','")
                .append(surname).append("','")
                .append(email).append("') ;")
                .toString();
        StatsManager.getInstance().queryLog(query);

        session.execute(query);
        StatsManager.getInstance().insertIntoTickets();
    }

    public void createEmpty(UUID id, int matchid, String sector,
                            int place){
        String query = new StringBuilder("INSERT INTO ")
                .append("Tickets ( ticketid, matchid, sector, place )")
                .append("VALUES (")
                .append(id).append(",")
                .append(matchid).append(",'")
                .append(sector).append("',")
                .append(place).append(");")
                .toString();
        StatsManager.getInstance().queryLog(query);

        session.execute(query);
    }

    public List<Ticket> getAllTickets(){
        String query = "SELECT * FROM Tickets;";

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
    public List<Ticket> getTickets(int matchid, String sector){
        String query = "SELECT * FROM Tickets where matchid="+matchid+" and sector= '" +sector+ "' ;";
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
    public List<Ticket> getTickets(int matchid){
        String query = "SELECT * FROM Tickets where matchid="+matchid+";";

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

    public void removeTicket(int matchid,String sector,int place){

        String query = new StringBuilder("Delete FROM Tickets where ")
                .append("matchid = ").append(matchid)
                .append(" sector =").append(sector)
                .append(" place = ").append(place)
                .append(";")
                .toString();
        StatsManager.getInstance().queryLog(query);
        session.execute(query);

    }


    public void resetTickets(){
        String query = "TRUNCATE Tickets;";
        StatsManager.getInstance().queryLog(query);
        session.execute(query);
    }


    public List<Ticket> getTickets(int matchid, String sector, int reservedPlace) {
        String query = "SELECT * FROM Tickets where matchid="+matchid+
                " and sector= '"+sector+"' and place= "+reservedPlace+" ;";
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
}
