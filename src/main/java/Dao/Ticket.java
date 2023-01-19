package Dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;


@AllArgsConstructor
@ToString
@Getter
public class Ticket {
    UUID ticketid;
    int matchid;
    String sector;
    int place;
    UUID userid;
}
