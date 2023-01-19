package Dao;

import com.datastax.driver.core.ConsistencyLevel;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;
import com.datastax.driver.core.Statement;

public class CassandraTable {

    protected Session session;

    public CassandraTable(Session session){
        this.session = session;
    }

    public void createMatch(){
        String query = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append("Matches (")
                .append("matchid int, ")
                .append("host text, ")
                .append("guest text, ")
                .append("stadion text, ")
                .append("date text, ")
                .append("kind text, ")
                .append("round int, ")
                .append("PRIMARY KEY(matchid));")
                .toString();
        System.out.println(query);
        session.execute(query);
    }

    public void createTicket(){
        String query = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append("Tickets (")
                .append("ticketid uuid,")
                .append("matchid int,")
                .append("sector text,")
                .append("place int,")
                .append("userid uuid,")
                .append("name text,")
                .append("surname text,")
                .append("email text,")
                .append("PRIMARY KEY ((matchid),sector,place,userid));")
                .toString();
        System.out.println(query);
        session.execute(query);
    }

    public void createReservedTicketByUser(){
        String query = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append("ReservedTicketByUser (")
                .append("ticketid uuid,")
                .append("userid uuid,")
                .append("matchid int,")
                .append("sector text, ")
                .append("place int, ")
                .append("PRIMARY KEY ((matchid, sector), place, userid));")
                .toString();
        System.out.println(query);
        session.execute(query);
    }

    public void createAvaibleTickets(){
        String query = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append("AvaibleTickets (")
                .append("matchid int, ")
                .append("sector text, ")
                .append("placeList set<int> , ")
                .append("PRIMARY KEY (matchid, sector));")
                .toString();
        System.out.println(query);
        session.execute(query);
    }

    public void dropMatch(){
        session.execute("DROP TABLE IF EXISTS Matches;");
    }

    public void dropTicket(){
        session.execute("DROP TABLE IF EXISTS Tickets;");
    }

    public void dropAvaibleTickets(){
        session.execute("DROP TABLE IF EXISTS AvaibleTickets;");
    }

    public void dropreservedTicketByUser(){
        session.execute("DROP TABLE IF EXISTS reservedTicketByUser;");
    }

}
