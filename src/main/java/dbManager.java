import Dao.AvaibleTicketsDao;
import Dao.MatchDao;
import Dao.TicketDao;
import Dao.TicketReservationDao;
import com.datastax.driver.core.Session;
import lombok.Getter;

@Getter
public class dbManager {
    private Session session;
    private TicketDao ticketsDao;
    private MatchDao matchesDao;
    private TicketReservationDao reservationsDao;
    private AvaibleTicketsDao avaibleTicketsDao;

    dbManager(Session session){
        this.session=session;
        ticketsDao = new TicketDao(session);
        matchesDao = new MatchDao(session);
        reservationsDao = new TicketReservationDao(session);
        avaibleTicketsDao = new AvaibleTicketsDao(session);
    }
}
