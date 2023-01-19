package Dao;

import lombok.AllArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@ToString
public class Match {
    int matchid;
    String host;
    String guest;
    String station;
    com.datastax.driver.core.LocalDate date;
    String kind;
    int round;
}
