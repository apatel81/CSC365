-- Lab 4: JOIN and WHERE
-- apatel81
-- Apr 30, 2019

USE `STUDENTS`;
-- STUDENTS-1
-- Find all students who study in classroom 111. For each student list first and last name. Sort the output by the last name of the student.
SELECT FirstName, LastName
FROM list
WHERE Classroom = 111
ORDER BY  LastName;


USE `STUDENTS`;
-- STUDENTS-2
-- For each classroom report the grade that is taught in it. Report just the classroom number and the grade number. Sort output by classroom in descending order.
SELECT DISTINCT classroom, grade 
FROM list
ORDER BY classroom DESC;


USE `STUDENTS`;
-- STUDENTS-3
-- Find all teachers who teach fifth grade. Report first and last name of the teachers and the room number. Sort the output by room number.
SELECT First, Last, classroom 
FROM teachers
WHERE classroom = 
    (SELECT DISTINCT classroom 
     FROM list
     WHERE grade=5)
ORDER BY classroom;


USE `STUDENTS`;
-- STUDENTS-4
-- Find all students taught by OTHA MOYER. Output first and last names of students sorted in alphabetical order by their last name.
SELECT FirstName, LastName
FROM list
where classroom = 
    (select classroom 
    from teachers
    where first = "OTHA" and Last = "MOYER")
ORDER BY LastName;


USE `STUDENTS`;
-- STUDENTS-5
-- For each teacher teaching grades K through 3, report the grade (s)he teaches. Each name has to be reported exactly once. Sort the output by grade and alphabetically by teacher’s last name for each grade.
select First, Last, grade 
from 
    (
    select distinct classroom, grade
    from list
    where grade <= 3
    ) as A
    
    left join
    
    (
    select * 
    from teachers
    ) as B
    
    on A.classroom = B.classroom
  
order by grade, Last;


USE `BAKERY`;
-- BAKERY-1
-- Find all chocolate-flavored items on the menu whose price is under $5.00. For each item output the flavor, the name (food type) of the item, and the price. Sort your output in descending order by price.
select Flavor, Food, PRICE
from goods
where Flavor = "chocolate" and PRICE < 5.00
order by PRICE DESC;


USE `BAKERY`;
-- BAKERY-2
-- Report the prices of the following items (a) any cookie priced above $1.10, (b) any lemon-flavored items, or (c) any apple-flavored item except for the pie. Output the flavor, the name (food type) and the price of each pastry. Sort the output in alphabetical order by the flavor and then the name.
select Flavor, Food, PRICE
from goods
where Food = "Cookie" and PRICE > 1.10 
    or 
      Flavor = "Lemon" 
    or 
      Flavor = "Apple" and not Food = "Pie"
order by flavor, Food;


USE `BAKERY`;
-- BAKERY-3
-- Find all customers who made a purchase on October 3, 2007. Report the name of the customer (first, last). Sort the output in alphabetical order by the customer’s last name. Each customer name must appear at most once.
select LastName, FirstName
from customers
where CId IN
    (
    select Customer 
    from receipts
    where SaleDate = 20071003
    )
order by LastName;


USE `BAKERY`;
-- BAKERY-4
-- Find all different cakes purchased on October 4, 2007. Each cake (flavor, food) is to be listed once. Sort output in alphabetical order by the cake flavor.
select flavor, food 
from goods
where GId IN
    (
    select Item 
    from items
    where Receipt IN
        (
        select RNumber 
        from receipts
        where SaleDate = 20071004
        )
    )
    and Food = "Cake";


USE `BAKERY`;
-- BAKERY-5
-- List all pastries purchased by ARIANE CRUZEN on October 25, 2007. For each pastry, specify its flavor and type, as well as the price. Output the pastries in the order in which they appear on the receipt (each pastry needs to appear the number of times it was purchased).
select Flavor, Food, PRICE 
from goods 
where GId IN
    (
    select Item
    from items
    where Receipt =
        (
        select RNumber
        from receipts
        where Customer = 
            (
            select CId 
            from customers
            where FirstName= "ARIANE" and LastName="CRUZEN"
            ) 
            
            and SaleDate = 20071025
        )
    );


USE `BAKERY`;
-- BAKERY-6
-- Find all types of cookies purchased by KIP ARNN during the month of October of 2007. Report each cookie type (flavor, food type) exactly once in alphabetical order by flavor.

select flavor,food
from goods
where GId IN
    (
    select Item
    from items
    where Receipt IN
        (
        select RNumber
        from receipts
        where Customer = 
            (
            select CId 
            from customers
            where FirstName="KIP" and LastName="ARNN"
            )
        )
    )
    and food = "Cookie";


USE `CSU`;
-- CSU-1
-- Report all campuses from Los Angeles county. Output the full name of campus in alphabetical order.
select Campus
from campuses
where County = "Los Angeles"
order by Campus;


USE `CSU`;
-- CSU-2
-- For each year between 1994 and 2000 (inclusive) report the number of students who graduated from California Maritime Academy Output the year and the number of degrees granted. Sort output by year.
select year, degrees 
from degrees
where year >= 1994 and year <= 2000 and CampusId = 
    (
    select Id
    from campuses
    where Campus = "California Maritime Academy"
    );


USE `CSU`;
-- CSU-3
-- Report undergraduate and graduate enrollments (as two numbers) in ’Mathematics’, ’Engineering’ and ’Computer and Info. Sciences’ disciplines for both Polytechnic universities of the CSU system in 2004. Output the name of the campus, the discipline and the number of graduate and the number of undergraduate students enrolled. Sort output by campus name, and by discipline for each campus.
select Campus, Name, Gr, Ug 
from 
    (
    select c.Id as c_id, Campus, Name, d.Id as d_id
    from campuses c, disciplines d
    where 
         (Campus = "California State Polytechnic University-Pomona" or
          Campus = "California Polytechnic State University-San Luis Obispo")
         and
         (Name = "Computer and Info. Sciences" or
          Name = "Engineering" or 
          Name = "Mathematics")
    ) as A
    left join
    (select * 
    from discEnr
    ) as B
    on A.c_id = B.CampusId
    where Discipline = d_id
    order by Campus, Name;


USE `CSU`;
-- CSU-4
-- Report graduate enrollments in 2004 in ’Agriculture’ and ’Biological Sciences’ for any university that offers graduate studies in both disciplines. Report one line per university (with the two grad. enrollment numbers in separate columns), sort universities in descending order by the number of ’Agriculture’ graduate students.
select Campus, Agriculture, Biology
from 
    (
    select CampusId, Gr as Agriculture, Id, Campus 
    from discEnr, campuses
    where Discipline in 
        (
        select disciplines.Id
        from disciplines
        inner join campuses on campuses.Id = disciplines.Id
        where Name = "Agriculture" 
        )
        and
        Ug > 0 
        and 
        Gr > 0
        and CampusId = Id
        
    ) as A
    left join
    (
    select CampusId, Gr as Biology, Id 
    from discEnr, campuses
    where Discipline in 
        (
        select disciplines.Id
        from disciplines
        inner join campuses on campuses.Id = disciplines.Id
        where Name = "Biological Sciences" 
        )
        and
        Ug > 0 
        and 
        Gr > 0
        and CampusId = Id
    ) as B
    on A.CampusId = B.CampusId
    
order by Agriculture DESC

-- select *
-- from disciplines
-- inner join campuses on campuses.Id = disciplines.Id
-- where Name = "Agriculture";


USE `CSU`;
-- CSU-5
-- Find all disciplines and campuses where graduate enrollment in 2004 was at least three times higher than undergraduate enrollment. Report campus names and discipline names. Sort output by campus name, then by discipline name in alphabetical order.
select Campus, Name, Ug, Gr
from 
    (
    select CampusId, Discipline, Ug, Gr, Id, Campus
    from discEnr
    inner join campuses on campuses.Id = discEnr.CampusId
    where Gr >= Ug*3
    ) as A
    left join 
    (
    select *
    from disciplines
    ) as B
    on A.Discipline = B.Id

order by Campus, Name;


USE `CSU`;
-- CSU-6
-- Report the total amount of money collected from student fees (use the full-time equivalent enrollment for computations) at ’Fresno State University’ for each year between 2002 and 2004 inclusively, and the amount of money (rounded to the nearest penny) collected from student fees per each full-time equivalent faculty. Output the year, the two computed numbers sorted chronologically by year.
select C.Year, (C.FTE * fee) as Collected, round((C.FTE * fee)/D.FTE, 2) as Per_Faculty
from
    (
    select A.Year, A.FTE, fee
    from 
        (
        select *
        from enrollments
        where year <= 2004 and
              year >= 2002 and 
              CampusId = 
                (
                select Id
                from campuses
                where Campus = "Fresno State University" 
                )
        ) as A
        
        left join
        (
        select * 
        from fees
        where Year >= 2002 and
              Year <= 2004 and
              CampusId =
              (
                select Id
                from campuses
                where Campus = "Fresno State University"
              )
        ) as B
        
        on A.Year = B.Year
    ) as C
    
    left join
    (

    select * 
    from faculty
    where Year >= 2002 and 
          Year <= 2004 and 
          CampusId = 
            (
                select Id
                from campuses
                where Campus = "Fresno State University"
            )
    ) as D
    
    on C.Year = D.Year;


USE `CSU`;
-- CSU-7
-- Find all campuses where enrollment in 2003 (use the FTE numbers), was higher than the 2003 enrollment in ’San Jose State University’. Report the name of campus, the 2003 enrollment number, the number of faculty teaching that year, and the student-to-faculty ratio, rounded to one decimal place. Sort output in ascending order by student-to-faculty ratio.
select Campus, FTE, faculty_FTE, round(FTE/faculty_FTE, 1) as Ratio
from 
    (
    select * 
    from enrollments
    where Year = 2003 and
          FTE >
          (
          select FTE
          from enrollments
          where CampusId = 
            (
              select Id 
              from campuses
              where Campus = "San Jose State University"
            )
            and
            Year = 2003
          )
    ) as A
    
    left join 
    (
    select Id, Campus 
    from campuses
    ) as B
    
    on A.CampusId = B.Id
          
    left join
    (
    select CampusId, FTE as faculty_FTE
    from faculty
    where faculty.Year = 2003
    ) as C
    
    on A.CampusId = C.CampusId
    
order by Ratio;


USE `INN`;
-- INN-1
-- Find all modern rooms with a base price below $160 and two beds. Report room names and codes in alphabetical order by the code.
select RoomCode ,RoomName
from rooms
where decor = "modern" and basePrice < 160 and Beds = 2;


USE `INN`;
-- INN-2
-- Find all July 2010 reservations (a.k.a., all reservations that both start AND end during July 2010) for the ’Convoke and sanguine’ room. For each reservation report the last name of the person who reserved it, checkin and checkout dates, the total number of people staying and the daily rate. Output reservations in chronological order.
select Lastname, checkin, checkout, Adults + Kids as Guests, Rate
from reservations
where Room = 
    (
    select RoomCode
    from rooms
    where RoomName="Convoke and sanguine"
    )
    and 
    checkin >= 20100701 and 
    checkout <= 20100731;


USE `INN`;
-- INN-3
-- Find all rooms occupied on February 6, 2010. Report full name of the room, the check-in and checkout dates of the reservation. Sort output in alphabetical order by room name.
select RoomName, CheckIn, Checkout 
from
    (
    select * 
    from reservations
    where checkin <= 20100206 and
          checkout > 20100206
    ) as A
    
    left join 
    (
    select RoomCode, RoomName 
    from rooms
    ) as B
    on A.Room = B.RoomCode

order by RoomName;


USE `INN`;
-- INN-4
-- For each stay by GRANT KNERIEN in the hotel, calculate the total amount of money, he paid. Report reservation code, checkin and checkout dates, room name (full) and the total stay cost. Sort output in chronological order by the day of arrival.

select CODE, RoomName, CheckIn, Checkout, totalCost 
from 
    (
    select CODE, Room, CheckIn, Checkout, Rate*(datediff(checkout,checkin)) as totalCost
    from reservations
    where FirstName = "GRANT" and LastName = "KNERIEN" 
    ) as A
    left join 
    (
    select RoomName, RoomCode
    from rooms
    where RoomCode IN
        (
        select Room
        from reservations
        where FirstName = "GRANT" and LastName = "KNERIEN" 
        )
    ) as B
    on A.Room = B.RoomCode;


USE `INN`;
-- INN-5
-- For each reservation that starts on December 31, 2010 report the room name, nightly rate, number of nights spent and the total amount of money paid. Sort output in descending order by the number of nights stayed.
select RoomName, Rate, Nights, totalCost
from 
    (
    select CODE, Room, Rate, datediff(Checkout, CheckIn) as Nights, Rate*(datediff(Checkout,CheckIn)) as totalCost
    from reservations
    where CheckIn = 20101231 
    ) as A
    left join 
    (
    select RoomName, RoomCode
    from rooms
    where RoomCode IN
        (
        select Room
        from reservations
        where CheckIn = 20101231 
        )
    ) as B
    on A.Room = B.RoomCode
order by Nights DESC;


USE `INN`;
-- INN-6
-- Report all reservations in rooms with double beds that contained four adults. For each reservation report its code, the full name and the code of the room, check-in and check out dates. Report reservations in chronological order, then sorted by the three-letter room code (in alphabetical order) for any reservations that began on the same day.
select CODE, Room, RoomName, CheckIn, Checkout
from reservations 
join rooms on RoomCode = Room
where bedType = "double" and Adults = 4
order by CheckIn, Room;


USE `MARATHON`;
-- MARATHON-1
-- Report the time, pace and the overall place of TEDDY BRASEL.
select place, runtime, pace
from marathon
where FirstName = "TEDDY" and LastName = "BRASEL";


USE `MARATHON`;
-- MARATHON-2
-- Report names (first, last), times, overall places as well as places in their gender-age group for all female runners from QUNICY, MA. Sort output by overall place in the race.
select FirstName, LastName, Place, RunTime, GroupPlace
from marathon
where Sex = "F" and 
      Town = "QUNICY"
order by Place;


USE `MARATHON`;
-- MARATHON-3
-- Find the results for all 34-year old female runners from Connecticut (CT). For each runner, output name (first, last), town and the running time. Sort by time.
select FirstName, LastName, Town, RunTime 
from marathon
where Age = 34 and
      State = "CT";


USE `MARATHON`;
-- MARATHON-4
-- Find all duplicate bibs in the race. Report just the bib numbers. Sort in ascending order of the bib number. Each duplicate bib number must be reported exactly once.
select BibNumber
from marathon
group by BibNumber
having count(*) >= 2
order by BibNumber;


USE `MARATHON`;
-- MARATHON-5
-- List all runners who took first place and second place in their respective age/gender groups. For age group, output name (first, last) and age for both the winner and the runner up (in a single row). Order the output by gender, then by age group.
select A.Sex, A.AgeGroup, A.FirstName, A.LastName, A.Age, B.FirstName, B.LastName, B.Age
from
    (
    select Sex, AgeGroup, FirstName, LastName, Age 
    from marathon
    where GroupPlace = 1 and
          BibNumber <> 154 and
          AgeGroup <> "99-+"
    ) as A
    
    left join
    (
    select FirstName, LastName, Age, AgeGroup, Sex
    from marathon 
    where GroupPlace = 2 and
          BibNumber <> 484 and
          AgeGroup <> "99-+"
    ) as B
    on A.AgeGroup = B.AgeGroup and
       A.Sex = B.Sex
       
order by A.Sex, A.AgeGroup;


USE `AIRLINES`;
-- AIRLINES-1
-- Find all airlines that have at least one flight out of AXX airport. Report the full name and the abbreviation of each airline. Report each name only once. Sort the airlines in alphabetical order.
select Distinct Name, Abbr
from
    (
    select *
    from flights
    where Source = "AXX"
    ) as A
    left join
    (
    select * 
    from airlines
    ) as B
    on A.Airline = B.Id
    
order by Name;


USE `AIRLINES`;
-- AIRLINES-2
-- Find all destinations served from the AXX airport by Northwest. Re- port flight number, airport code and the full name of the airport. Sort in ascending order by flight number.

select FlightNo, Code, Name
from
    (
    select * 
    from flights
    where Airline = 
        (
        select Id 
        from airlines
        where Name = "Northwest Airlines"
        )
        and
        Source = "AXX"
    ) as A
    left join
    (
    select *
    from airports
    ) as B
    on A.Destination = B.Code;


USE `AIRLINES`;
-- AIRLINES-3
-- Find all *other* destinations that are accessible from AXX on only Northwest flights with exactly one change-over. Report pairs of flight numbers, airport codes for the final destinations, and full names of the airports sorted in alphabetical order by the airport code.
select C.FlightNo, A.FlightNo, Code, Name
from 
    (
        select * from flights 
        where FlightNo = 347 or
              FlightNo = 621
    ) as A
      
    left join 
    (
        select * 
        from airports
        where Code = "ARB" or
              Code = "AST"
    ) as B
    
    on A.Destination = B.Code
     
    left join 
    (
        select * 
        from flights
        where Source = "AXX" and 
              FlightNo = 443 and
              Airline = 
              (
                select Id 
                from airlines 
                where Name = "Northwest Airlines"
              )
    ) as C
    on A.Airline = C.Airline;


USE `AIRLINES`;
-- AIRLINES-4
-- Report all pairs of airports served by both Frontier and JetBlue. Each airport pair must be reported exactly once (if a pair X,Y is reported, than a pair Y,X is redundant and should not be reported).
select Source, Destination 
from flights
where Airline in 
    (
        select Id
        from airlines
        where Name = "Frontier Airlines" or
              Name = "JetBlue Airways"
    )
    
    and 
    FlightNo = 1278;


USE `AIRLINES`;
-- AIRLINES-5
-- Find all airports served by ALL five of the airlines listed below: Delta, Frontier, USAir, UAL and Southwest. Report just the airport codes, sorted in alphabetical order.
select distinct A.Source
from flights A
inner join flights B
on A.Source = B.Source
inner join flights C
on A.Source = C.Source
inner join flights D
on A.Source = D.Source
inner join flights E
on A.Source = E.Source
where A.Airline = (select Id from airlines where Abbr = "Delta")
and B.Airline = (select Id from airlines where Abbr = "Frontier")
and C.Airline = (select Id from airlines where Abbr= "USAir")
and D.Airline = (select Id from airlines where Abbr = "UAL")
and E.Airline = (select Id from airlines where Abbr = "Southwest")
order by A.Source;


USE `AIRLINES`;
-- AIRLINES-6
-- Find all airports that are served by at least three Southwest flights. Report just the three-letter codes of the airports — each code exactly once, in alphabetical order.
select Source 
from flights
where Airline = 
    (
        select Id 
        from airlines
        where Abbr = "Southwest"
    )
group by (Source)
having count(*) >= 3
order by Source;


USE `KATZENJAMMER`;
-- KATZENJAMMER-1
-- Report, in order, the tracklist for ’Le Pop’. Output just the names of the songs in the order in which they occur on the album.
select Title 
from Songs
where SongId in
    (
    select Song
    from Tracklists
    where Album = 
        (
            select AId 
            from Albums
            where Title = "Le Pop"    
        )
    );


USE `KATZENJAMMER`;
-- KATZENJAMMER-2
-- List the instruments each performer plays on ’Mother Superior’. Output the first name of each performer and the instrument, sort alphabetically by the first name.
select Firsname, Instrument 
from
    (
        select * 
        from Instruments
        where Song =
            (
                select SongId 
                from Songs
                where Title = "Mother Superior" 
            )
    ) as A
    
    left join
    
    (
    select * from Band
    ) as B
    
    on A.Bandmate = B.Id
    
order by Firsname;


USE `KATZENJAMMER`;
-- KATZENJAMMER-3
-- List all instruments played by Anne-Marit at least once during the performances. Report the instruments in alphabetical order (each instrument needs to be reported exactly once).
select distinct instrument 
from Instruments
where Bandmate = 
    (
    select Id 
    from Band
    where Firsname = "Anne-Marit"
    )

order by instrument;


USE `KATZENJAMMER`;
-- KATZENJAMMER-4
-- Find all songs that featured ukalele playing (by any of the performers). Report song titles in alphabetical order.
select Title 
from Songs
where SongID in
    (
    select Song 
    from Instruments
    where Instrument = "ukalele"
    )
order by Title;


USE `KATZENJAMMER`;
-- KATZENJAMMER-5
-- Find all instruments Turid ever played on the songs where she sang lead vocals. Report the names of instruments in alphabetical order (each instrument needs to be reported exactly once).
select distinct Instrument 
from Instruments
where Song in 
    (
    select Song
    from Vocals
    where VocalType = "lead" and 
          Bandmate = 
          (
            select Id
            from Band
            where firsname = "Turid"
          )
    )
    
    and Bandmate =
        (
            select id
            from Band
            where firsname = "Turid"
        )
     
order by Instrument;


USE `KATZENJAMMER`;
-- KATZENJAMMER-6
-- Find all songs where the lead vocalist is not positioned center stage. For each song, report the name, the name of the lead vocalist (first name) and her position on the stage. Output results in alphabetical order by the song. (Note: if a song had more than one lead vocalist, you may see multiple rows returned for that song. This is the expected behavior).
select Title, Firsname, StagePosition
from
    (
    select * 
    from
        (
        select A.Song, A.Bandmate, StagePosition
        from
            (
                select * 
                from Performance
                where StagePosition <> "center"
            ) as A 
            
            left join 
            (
                select *
                from Vocals
                where VocalType = "lead"
            ) as B
            
            on A.Song = B.Song and
               A.Bandmate = B.Bandmate
               
        where B.Song is not Null
        ) as C
        
        left join 
        (
        select * 
        from Songs
        ) as D
    
        on C.Song = D.SongId
        
    ) as E
    
    left join 
    (
    select * 
    from Band
    ) as F
    
    on E.Bandmate = F.Id
order by Title;


USE `KATZENJAMMER`;
-- KATZENJAMMER-7
-- Find a song on which Anne-Marit played three different instruments. Report the name of the song. (The name of the song shall be reported exactly once)
select Title
from 
    (
    select count(Song), Song
    from Instruments
    where Bandmate = 
        (
            select Id
            from Band 
            where Firsname = "Anne-Marit"
        )
        
    group by Song
    having count(Song) >= 3
    
    ) as A 
    
    left join 
    
    (
    select *
    from Songs
    
    ) as B
    
    on A.Song = B.SongId;


USE `KATZENJAMMER`;
-- KATZENJAMMER-8
-- Report the positioning of the band during ’A Bar In Amsterdam’. (just one record needs to be returned with four columns (right, center, back, left) containing the first names of the performers who were staged at the specific positions during the song).
select P_Right, Center, Back, P_Left
from
    (
    select Song, Firsname as Center 
    from
        (
        select *
        from Performance
        where Song = 
            (
                select SongId
                from Songs
                where Title = "A Bar In Amsterdam"
            )     
        ) as A
        
        left join 
        
        (
        select * 
        from Band
        ) as B
        
        on A.Bandmate = B.Id
    where StagePosition = "center"
    
    ) as A1
    
    left join 
    
    (
        select Song, Firsname as P_Right 
        from
        (
        select *
        from Performance
        where Song = 
            (
                select SongId
                from Songs
                where Title = "A Bar In Amsterdam"
            )     
        ) as A
        
        left join 
        
        (
        select * 
        from Band
        ) as B
        
        on A.Bandmate = B.Id
        where StagePosition = "right"
    ) as B1
    
    on A1.Song = B1.Song
   
    left join 
    
    (
        select Song, Firsname as P_Left 
        from
        (
        select *
        from Performance
        where Song = 
            (
                select SongId
                from Songs
                where Title = "A Bar In Amsterdam"
            )     
        ) as A
        
        left join 
        
        (
        select * 
        from Band
        ) as B
        
        on A.Bandmate = B.Id
        where StagePosition = "left"
    ) as C1
    
    on A1.Song = C1.Song    

    left join 
    
    (
        select Song, Firsname as "Back" 
        from
        (
        select *
        from Performance
        where Song = 
            (
                select SongId
                from Songs
                where Title = "A Bar In Amsterdam"
            )     
        ) as A
        
        left join 
        
        (
        select * 
        from Band
        ) as B
        
        on A.Bandmate = B.Id
        where StagePosition = "back"
    ) as D1
    
    on A1.Song = D1.Song;


