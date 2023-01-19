package Dao;

import Logs.StatsManager;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MatchDao extends CassandraTable {


    public MatchDao(Session session){
        super(session);
    }

    public void create(int id, String host, String guest,
                       String stadion, LocalDate date,
                       String kind, int round){
        String query = new StringBuilder("INSERT INTO ")
                .append("Matches ( matchid, host, guest, stadion, date, kind, round )")
                .append("VALUES (")
                .append(id).append(",'")
                .append(host).append("','")
                .append(guest).append("','")
                .append(stadion).append("','")
                .append(date).append("','")
                .append(kind).append("',")
                .append(round).append(");")
                .toString();
        StatsManager.getInstance().queryLog(query);
        session.execute(query);
    }

    public List<Match> getAllMatches(){
        String query = "SELECT * FROM Matches;";

        ResultSet rs = session.execute(query);

        List<Match> allMatches = new ArrayList<>();
        rs.forEach(
                r->allMatches.add(new Match(
                        r.getInt("matchid"),
                        r.getString("host"),
                        r.getString("guest"),
                        r.getString("station"),
                        r.getDate("date"),
                        r.getString("kind"),
                        r.getInt("round")
                ))
        );
        return allMatches;
    }

    public void removeMatch(int matchid, String sector, int place){

        String query = new StringBuilder("Delete FROM Matches where ")
                .append("matchid = ").append(matchid)
                .append(" sector =").append(sector)
                .append(" place = ").append(place)
                .append(";")
                .toString();
        StatsManager.getInstance().queryLog(query);
        session.execute(query);

    }

    public void resetMatches(){
        String query = "TRUNCATE Matches;";
        StatsManager.getInstance().queryLog(query);
        session.execute(query);
    }

}
