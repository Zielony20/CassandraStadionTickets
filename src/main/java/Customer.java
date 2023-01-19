import Dao.TicketReservation;
import Logs.StatsManager;
import com.github.javafaker.Faker;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@ToString
public class Customer implements Runnable {

    private final int ticketsCount;
    private final int match;
    private final String sector;
    private final UUID userid;
    private final String name;
    private final String surname;
    private final String mail;
    private dbManager dbManager;
    private UUID ticket;

    public Customer(int ticketsCount, int match, String sector, dbManager dbManager){
        this.match = match;
        this.sector = sector;
        this.dbManager = dbManager;
        this.ticketsCount = ticketsCount;
        this.mail = generateRandomEmail();
        Faker faker = new Faker();
        this.name = faker.name().fullName().replaceAll("'","");
        this.surname = faker.name().lastName().replaceAll("'","");
        this.userid = UUID.randomUUID();
        this.ticket = UUID.randomUUID();
    }

    @Override
    public void run() {
        //Symulacja początkowego opóźnienia
        try {
            this.randomDelay();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Dostępne bilety
        List<Integer> avaiblePlaces = new ArrayList<>( dbManager.getAvaibleTicketsDao()
                .getAvaibleTickets(match,sector).one().getSet("placeList",Integer.class) );

        //Miejsca zarezerwowane przez innych użytkowników ale jeszcze nie kupione
        List<Integer> currentReserved = dbManager.getReservationsDao()
                .getReservation(match,sector)
                .stream()
                .map(item -> (Integer)item.getPlace())
                .collect(Collectors.toList());

        //usuń z wolnych miejsc również zarezerwowane
        avaiblePlaces.removeAll(currentReserved);

        if (avaiblePlaces.size()>=ticketsCount){

            List<Integer> plannedReservedPlaces = new ArrayList<>();

            for(int i=0;i<ticketsCount;i++){
                int index = ThreadLocalRandom.current().nextInt(0,avaiblePlaces.size());
                //System.out.println(index);
                int place = avaiblePlaces.get(index);
                plannedReservedPlaces.add(place);

                //stwórz rezerwacje
                dbManager.getReservationsDao().create(ticket,match,sector,place,userid);
                StatsManager.getInstance().reservationRetries();
                avaiblePlaces.remove(index);
            }
            //odczekaj 2 sekundy sesji
            try {
                this.delay();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean success = true;
            for(int reservedPlace:plannedReservedPlaces){
                List<TicketReservation> reservationList = dbManager.getReservationsDao().getReservation(match,sector,reservedPlace);

                if(reservationList.size()!=1){
                    //Jest więcej rezerwacji!
                    success = false;
                    //System.out.print(reservationList.size()-1+"+");
                    break;
                }
            }

            for(int reservedPlace:plannedReservedPlaces) {
                if(success){
                    //stwórz bilet
                    dbManager.getAvaibleTicketsDao().decrese(match,sector,reservedPlace);
                    dbManager.getTicketsDao().create(ticket,match,sector,reservedPlace,userid,name,surname,mail);

                    //sprawdzenie czy bilet został kupiny
                    if(!dbManager.getTicketsDao().getTickets(match,sector,reservedPlace).isEmpty() ){
                        StatsManager.getInstance().reservationSuccess();
                    }
                }
                else{
                    StatsManager.getInstance().reservationFail();
                }

                //usuń rezerwację
                dbManager.getReservationsDao().removeReservation(match,sector,reservedPlace,userid);
            }
        }else{
            for(int i=0;i<ticketsCount;i++){

                StatsManager.getInstance().reservationRetries();
                StatsManager.getInstance().reservationFailBecauseFull();
            }

            //System.out.println("Nie ma wolnych miejsc");
        }

    }

    private String getUniqueId() {
        return String.format("%s_%s", UUID.randomUUID().toString().substring(0, 5), System.currentTimeMillis() / 1000);
    }
    public String generateRandomEmail() {
        return String.format("%s@%s", getUniqueId(), "yourHostName.com");
    }
    private void delay() throws InterruptedException {
        Thread.sleep(2000);
    }
    private void randomDelay() throws InterruptedException {
        int delay = ThreadLocalRandom.current().nextInt(10000);

        Thread.sleep(delay);
    }
}

