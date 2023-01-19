package Dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.Set;

@Getter
@ToString
@AllArgsConstructor
public class AvaibleTickets {

    private int machtid;
    private String sector;
    private Set<Integer> placeList;
    //private int available;
}
