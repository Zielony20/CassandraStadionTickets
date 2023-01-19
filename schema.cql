CREATE KEYSPACE srds WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 5};

INSERT INTO Tickets ( ticketid, matchid, sector, place, userid, name, surname, email )VALUES (b286fb9d-7aef-4e13-9ad3-5cbb122ba797,3,'C',4,6ca3ec99-e8f7-43a6-85a8-36c75e8291cb,'Mrs. Abdul Reichel','Kshlerin','c24f8_1673889176@yourHostName.com');

CREATE TABLE IF NOT EXISTS srds.Matches
(
    matchid int,
    host text,
    guest text,
    stadion text,
    date text,
    kind text,
    round int,
    PRIMARY KEY(matchid)
);

CREATE TABLE IF NOT EXISTS srds.Tickets
(
    ticketid uuid,
    matchid int,
    sector text,
    place int,
    userid uuid,
    name text,
    surname text,
    email text,
    PRIMARY KEY ((matchid),sector,place)
);

CREATE TABLE IF NOT EXISTS srds.ReservedTicketByUser
(
    ticketid uuid,
    userid uuid,
    matchid int,
    sector text,
    place int,
    PRIMARY KEY ((matchid,sector),place,userid)
);

CREATE TABLE IF NOT EXISTS srds.AvaibleTickets
(
    matchid int,
    sector text,
    placeList list<int>,
    PRIMARY KEY (matchid, sector)
);
