package Logs;

import java.util.Timer;
import java.util.TimerTask;

public class StatsManager {

    private static StatsManager INSTANCE;
    private boolean queryLogs = false;
    private int reservationRetries=0;
    private int reservationSuccess=0;
    private int reservationFail=0;
    private int insertIntoTickets=0;
    private int insertIntoTicketsReservation=0;
    private int reservationFailBecauseFull=0;
    private long time;

    public static StatsManager getInstance(){
        if(INSTANCE==null){
            INSTANCE = new StatsManager();
        }
        return INSTANCE;
    }

    public void setQueryLogs(boolean q) {
        this.queryLogs = q;
    }
    public boolean getQueryLogs(){
        return queryLogs;
    }

    public void queryLog(String query){
        if(queryLogs){
            System.out.println(query);
        }
    }
    public void queryLog(String query, boolean localAgrement){
        if(queryLogs || localAgrement){
            System.out.println(query);
        }
    }

    public synchronized void reservationRetries() {
        reservationRetries+=1;
    }

    public synchronized void reservationSuccess() {
        reservationSuccess+=1;
    }
    public synchronized void reservationFail() {
        reservationFail+=1;
    }
    public synchronized void insertIntoTickets() {
        insertIntoTickets+=1;
    }

    public synchronized void insertIntoTicketsReservation() {
        insertIntoTicketsReservation+=1;
    }

    public void summorize(){
        System.out.println("");
        System.out.println("Prób rezerwacji: "+reservationRetries);
        System.out.println("Udane rezerwacje: "+reservationSuccess);
        System.out.println("Nieudane rezerwacje: "+(reservationFail+reservationFailBecauseFull));

        System.out.println("Nieudane rezerwacje przez konflikt rezerwacji: "+reservationFail);
        System.out.println("Nieudane rezerwacje bo na starcie nie było wolnych biletów: "+reservationFailBecauseFull);

        System.out.println("Wykonane polecenia InsertTicket: "+insertIntoTickets);
        System.out.println("Wykonane polecenia InsertReservation: "+insertIntoTicketsReservation);
        System.out.println("Czas: "+stopTimer());

    }

    public synchronized void reservationFailBecauseFull() {
        reservationFailBecauseFull+=1;
    }

    public void startTimer() {
        this.time = System.nanoTime();
    }

    public long stopTimer() {
        return System.nanoTime() - this.time;
    }
}
