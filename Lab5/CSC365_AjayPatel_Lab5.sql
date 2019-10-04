-- Lab 5
-- apatel81
-- May 14, 2019

USE `STUDENTS`;
-- STUDENTS-1
-- Report the names of teachers who have between seven and eight (inclusive) students in their classrooms. Sort output in alphabetical order by the teacher's last name.
select last, first from
(select last, first, count(list.classroom) as students
from teachers
join list on teachers.classroom = list.classroom 
group by list.classroom
having students >= 7 and students <= 8
order by last) as A;


USE `STUDENTS`;
-- STUDENTS-2
-- For each grade, report the number of classrooms in which it is taught and the total number of students in the grade. Sort the output by the number of classrooms in descending order, then by grade in ascending order.

select A.grade, Classrooms, Students from 

(select grade, count(grade) Students
from teachers
join list on teachers.classroom = list.classroom 
group by grade
order by Students) as A

join 

(select grade, count(distinct teachers.classroom) as Classrooms
from teachers 
join list 
on teachers.classroom = list.classroom
group by grade) as B

on A.grade = B.grade
order by Classrooms desc, grade;


USE `STUDENTS`;
-- STUDENTS-3
-- For each Kindergarten (grade 0) classroom, report the total number of students. Sort output in the descending order by the number of students.
select classroom, count(classroom) as COUNT
from list 
where grade = 0
group by classroom
order by COUNT desc;


USE `STUDENTS`;
-- STUDENTS-4
-- For each fourth grade classroom, report the student (last name) who is the last (alphabetically) on the class roster. Sort output by classroom.
select classroom, LastName 
from  list 
where grade = 4 
      and LastName not in
      
(select distinct A.LastName 
from 

(select classroom, LastName 
from list
where grade = 4
order by classroom, LastName) as A

join 

(select classroom, LastName 
from list
where grade = 4
order by classroom, LastName
) as B

where A.LastName < B.LastName and
      A.classroom = B.classroom
)

order by classroom;


USE `BAKERY`;
-- BAKERY-1
-- For each flavor which is found in more than three types of items sold by the bakery, report the average price (rounded to the nearest penny) of an item of this flavor and the total number of different items of this flavor on the menu. Sort the output in ascending order by the average price.
select Flavor, round(AVG(PRICE), 2) as AveragePrice ,count(Food) as FoodCount
from goods
group by Flavor
having FoodCount > 3
order by AveragePrice;


USE `BAKERY`;
-- BAKERY-2
-- Find the total amount of money the bakery earned in October 2007 from selling eclairs. Report just the amount.
select sum(Totals) from 
(select ItemCount * PRICE as Totals
from
(select Item, count(Item) as ItemCount 
from items
where Item in 
    (select GId 
     from goods
     where Food = "Eclair"
    )

group by Item
) as A
 
join 

(
select PRICE, GId 
from goods
where Food = "Eclair"
) as B

on A.Item = B.GId
) as C;


USE `BAKERY`;
-- BAKERY-3
-- For each visit by NATACHA STENZ output the receipt number, date of purchase, total number of items purchased and amount paid, rounded to the nearest penny. Sort by the amount paid, most to least.
select RNumber, SaleDate, count(Item) as TotalItems, round(sum(PRICE), 2) as AmountPaid
from

(select * 
from receipts
where Customer = 
    (
    select CId
    from customers
    where LastName = "STENZ" and FirstName = "NATACHA"
    )) as A
    
join 

(select * 
from items) as B

on A.RNumber = B.Receipt

join 

(select *  
from goods) as C

on B.Item = C.GId
group by RNumber, SaleDate
order by AmountPaid desc;


USE `BAKERY`;
-- BAKERY-4
-- For each day of the week of October 8 (Monday through Sunday) report the total number of purchases (receipts), the total number of pastries purchased and the overall daily revenue. Report results in chronological order and include both the day of the week and the date.
select DAYNAME(SaleDate), SaleDate, count(distinct RNumber) as Receipts, count(Receipt) as Items,
round(sum(PRICE), 2) as total
from 

(select * 
from receipts
where SaleDate >= 20071008 and SaleDate <= 20071014
order by SaleDate) as A

join 

(select Receipt, Item 
from items) as B

on A.RNumber = B.Receipt

join 

(select GId, PRICE 
from goods) as C

on B.Item = C.GId

group by SaleDate
order by SaleDate


-- select *
-- from 

-- (select * 
-- from receipts
-- where SaleDate >= 20071008 and SaleDate <= 20071014
-- order by SaleDate) as A

-- join 

-- (select Receipt, Item 
-- from items) as B

-- on A.RNumber = B.Receipt

-- join 

-- (select GId, PRICE 
-- from goods) as C

-- on B.Item = C.GId
-- order by SaleDate;


USE `BAKERY`;
-- BAKERY-5
-- Report all days on which more than ten tarts were purchased, sorted in chronological order.
select SaleDate from 
(select count(Item), SaleDate
from 

(select *
from items
where item in
    (
    select GId
    from goods
    where Food = "Tart"
    ) 
) as A

join 

(select * 
from receipts) as B

on A.Receipt = B.RNumber
group by SaleDate
having count(Item) > 10) as C;


USE `CSU`;
-- CSU-1
-- For each campus that averaged more than $2,500 in fees between the years 2000 and 2005 (inclusive), report the total cost of fees for this six year period. Sort in ascending order by fee.
select Campus, TotalCost
from 

(select CampusID, sum(fee) as TotalCost
from fees 
where CampusId in
    (select CampusID 
    from 
    (select CampusId, AVG(fee) 
    from fees
    where Year >= 2000 and Year <= 2005
    group by CampusID
    having AVG(fee) > 2500) as A) 
    
    and
    
    Year >= 2000 and Year <= 2005
group by CampusID
) as B

join

(select * 
from campuses) as C

on B.CampusID = C.Id
order by TotalCost;


USE `CSU`;
-- CSU-2
-- For each campus for which data exists for more than 60 years, report the average, the maximum and the minimum enrollment (for all years). Sort your output by average enrollment.
select Campus, Minimum, Average, Maximum
from

(select CampusId, count(Year), min(enrolled) as Minimum, avg(enrolled) as Average, max(enrolled) as Maximum
from enrollments
group by CampusId
having count(Year) > 60) as A

join

(select * from campuses) as B

on A.CampusId = B.Id
order by Average;


USE `CSU`;
-- CSU-3
-- For each campus in LA and Orange counties report the total number of degrees granted between 1998 and 2002 (inclusive). Sort the output in descending order by the number of degrees.

select Campus, Total
from 

(select CampusId, sum(degrees) as Total 
from degrees
where CampusId in 
    (
    select Id 
    from campuses
    where County = "Los Angeles" or County = "Orange"
    )
    
    and Year >= 1998 and Year <= 2002

group by CampusId) as A

join

(select * from campuses) as B

on A.CampusId = B.Id
order by Total desc;


USE `CSU`;
-- CSU-4
-- For each campus that had more than 20,000 enrolled students in 2004, report the number of disciplines for which the campus had non-zero graduate enrollment. Sort the output in alphabetical order by the name of the campus. (Exclude campuses that had no graduate enrollment at all.)
select Campus, Total 
from 

(select CampusId, count(Discipline) as Total
from discEnr 
where CampusId in 
    (
    select CampusId 
    from enrollments
    where Year = 2004 and 
          Enrolled >= 20000
    )
    and Gr > 0
group by CampusId) as A

join

(select Id, Campus from campuses) as B

on A.CampusId = B.Id

order by Campus


-- California State University-Fullerton	17
-- California State University-Northridge	17
-- California State University-Sacramento	16
-- Long Beach State University	18
-- San Diego State University	19
-- San Francisco State University	17
-- San Jose State University	19;


USE `MARATHON`;
-- MARATHON-1
-- For each gender/age group, report total number of runners in the group, the overall place of the best runner in the group and the overall place of the slowest runner in the group. Output result sorted by age group and sorted by gender (F followed by M) within each age group.
select AgeGroup, Sex, count(Age), min(Place), max(Place)
from marathon
group by AgeGroup, Sex
order by AgeGroup, Sex



-- select * from marathon;


USE `MARATHON`;
-- MARATHON-2
-- Report the total number of gender/age groups for which both the first and the second place runners (within the group) are from the same state.
select count(*) as Total
from

(select *
from marathon
where GroupPlace = 1 
order by AgeGroup, Sex) as A

join 

(select *
from marathon
where GroupPlace = 2
order by AgeGroup, Sex) as B

on A.State = B.State and 
   A.AgeGroup = B.AgeGroup and
   A.Sex = B.Sex;


USE `MARATHON`;
-- MARATHON-3
-- For each full minute, report the total number of runners whose pace was between that number of minutes and the next. In other words: how many runners ran the marathon at a pace between 5 and 6 mins, how many at a pace between 6 and 7 mins, and so on.
select TIME_FORMAT(Pace, "%i"), count(MINUTE(Pace))
from marathon
group by TIME_FORMAT(pace, "%i");


USE `MARATHON`;
-- MARATHON-4
-- For each state with runners in the marathon, report the number of runners from the state who finished in top 10 in their gender-age group. If a state did not have runners in top 10, do not output information for that state. Sort in descending order by the number of top 10 runners.
select State, count(State)
from marathon
where GroupPlace <= 10
group by State
order by count(State) desc;


USE `MARATHON`;
-- MARATHON-5
-- For each Connecticut town with 3 or more participants in the race, report the average time of its runners in the race computed in seconds. Output the results sorted by the average time (lowest average time first).
select Town, round(Avg(TIME_TO_SEC(RunTime)), 1) as Time
from marathon 
where State = "CT"
group by Town
having count(Town) >= 3
order by Time;


USE `AIRLINES`;
-- AIRLINES-1
-- Find all airports with exactly 17 outgoing flights. Report airport code and the full name of the airport sorted in alphabetical order by the code.
select Source, Name 
from 

(select Source, count(Source) as num_flights
from flights
group by Source
having num_flights = 17) as A

join 

(select * from airports) as B

on A.Source = B.Code
order by Source;


USE `AIRLINES`;
-- AIRLINES-2
-- Find the number of airports from which airport ANP can be reached with exactly one transfer. Make sure to exclude ANP itself from the count. Report just the number.
select count(distinct B.Source)
from

(select * 
from flights
where Destination = "ANP") as A

join

(select * 
from flights 
where Source <> "ANP") as B

on A.Source = B.Destination;


USE `AIRLINES`;
-- AIRLINES-3
-- Find the number of airports from which airport ATE can be reached with at most one transfer. Make sure to exclude ATE itself from the count. Report just the number.
select count(distinct Source)
from 

(select distinct B.Source
from

(select * 
from flights 
where Destination = "ATE") as A

join

(select * 
from flights 
where Source <> "ATE") as B

on A.Source = B.Destination

UNION

select Source 
from flights 
where Destination = "ATE") as C;


USE `AIRLINES`;
-- AIRLINES-4
-- For each airline, report the total number of airports from which it has at least one outgoing flight. Report the full name of the airline and the number of airports computed. Report the results sorted by the number of airports in descending order. In case of tie, sort by airline name A-Z.
select Name, num_airports
from

(select Airline, count(distinct Source) as num_airports
from flights
group by Airline) as A

join 

(select Name, Id from airlines) as B

on A.Airline = B.Id
order by num_airports desc, name;


USE `INN`;
-- INN-1
-- For each room, report the total revenue for all stays and the average revenue per stay generated by stays in the room that began in the months of September, October and November. Sort output in descending order by total revenue. Output full room names.
select RoomName, TotalRev, round(AvgRev,2)
from 
(select Room, sum(Rev) as TotalRev, avg(Rev) as AvgRev
from
(select Room, datediff(checkout, checkin) * Rate as Rev
from reservations
where CheckIn >= 20100901 and CheckIn <= 20101130) as A

group by Room) as B

join

(select RoomCode, RoomName from rooms) as C

on B.Room = C.RoomCode
order by TotalRev desc;


USE `INN`;
-- INN-2
-- Report the total number of reservations that began on Fridays, and the total revenue they brought in.
select count(*), sum(Rev)
from
    (select DAYNAME(CheckIn) as day, datediff(checkout, checkin) * Rate as Rev 
    from reservations) as A
    
where day = "Friday";


USE `INN`;
-- INN-3
-- For each day of the week, report the total number of reservations that began on that day and the total revenue for these reservations. Report days of week as Monday, Tuesday, etc. Order days from Sunday to Saturday.
select day, count(day), sum(Rev)
from 

(select DAYNAME(CheckIn) as day, datediff(checkout, checkin) * Rate as Rev 
from reservations) as A

group by day
order by FIELD(day, 'Sunday', 'Monday', 'Tuesday',
               'Wednesday', 'Thursday', 'Friday',
               'Saturday');


USE `INN`;
-- INN-4
-- For each room report the highest markup against the base price and the largest markdown (discount). Report markups and markdowns as the signed difference between the base price and the rate. Sort output in descending order beginning with the largest markup. In case of identical markup/down sort by room name A-Z. Report full room names.
select RoomName, maX-basePrice as markup, miN-basePrice as discount
from 

(select Room, min(Rate) as miN, max(Rate) as maX
from reservations
group by Room) as A

join

(select RoomCode, RoomName, basePrice from rooms) as B

on A.Room = B.RoomCode
order by markup desc, RoomName;


USE `INN`;
-- INN-5
-- For each room report how many nights in calendar year 2010 the room was occupied. Report the room code, the full name of the room and the number of occupied nights. Sort in descending order by occupied nights. (Note: it has to be number of nights in 2010. The last reservation in each room can go beyond December 31, 2010, so the ”extra” nights in 2011 need to be deducted).
select D.Room, E.Roomname, TotalNights - 1
from 

(select C.Room, totalNights + nights as TotalNights
from 

(select Room, sum(nights) as totalNights
from
(select Room, datediff(checkout, checkin) as nights
from reservations
where Checkout <= 20101231) as A
group by Room) as C

join 

(select Room, datediff(20110101, checkIn) as nights
from reservations
where Checkout >= 20110101) as B

on C.Room = B.Room 
) as D

join

(select RoomCode, RoomName from rooms) as E

on D.Room = E.RoomCode
order by TotalNights desc

-- IBD	Immutable before decorum	236
-- CAS	Convoke and sanguine	228
-- TAA	Thrift and accolade	222
-- RND	Recluse and defiance	220
-- AOB	Abscond or bolster	216
-- FNA	Frugal not apropos	210
-- IBS	Interim but salutary	208
-- MWC	Mendicant with cryptic	203
-- HBB	Harbinger but bequest	200
-- RTE	Riddle to exculpate	186;


USE `KATZENJAMMER`;
-- KATZENJAMMER-1
-- For each performer (by first name) report how many times she sang lead vocals on a song. Sort output in descending order by the number of leads.
select Firsname, numLeads
from

(select Bandmate, count(VocalType) as numLeads
from
    (select *
    from Vocals
    where VocalType = "lead") as A
    
group by Bandmate) as B

join
 
(select * from Band) as C

on B.Bandmate = C.Id
order by numLeads desc;


USE `KATZENJAMMER`;
-- KATZENJAMMER-2
-- Report how many different instruments each performer plays on songs from the album 'Le Pop'. Sort the output by the first name of the performers.
select Firsname, numInsts
from 

(select Bandmate, count(distinct Instrument) as numInsts
from

(select *
from Instruments
where Song in 
    (
    select Song
    from Tracklists 
    where Album = 
        (select AId 
         from Albums
         where Title = "Le Pop")
    )) as A
    
group by Bandmate) as B

join

(select * from Band) as C

on B.Bandmate = C.Id
order by Firsname;


USE `KATZENJAMMER`;
-- KATZENJAMMER-3
-- Report the number of times Turid stood at each stage position when performing live. Sort output in ascending order of the number of times she performed in each position.

select StagePosition, count(StagePosition) as spCount
from
(
select *
from Performance
where Bandmate =
    (
    select Id 
    from Band
    where Firsname = "Turid"
    ) 
    ) as A
    
group by StagePosition
order by spCount;


USE `KATZENJAMMER`;
-- KATZENJAMMER-4
-- Report how many times each performer (other than Anne-Marit) played bass balalaika on the songs where Anne-Marit was positioned on the left side of the stage. Sort output alphabetically by the name of the performer.

select Firsname, instCount
from

(select Bandmate, count(Instrument) as instCount
from 
(
select *
from Instruments
where Song in 
    (
     select Song
     from Performance
     where Bandmate = 
        (
        select Id 
        from Band
        where Firsname = "Anne-Marit"
        )
    
        and
    
        StagePosition = "left"
    )
    
    and Instrument = "bass balalaika"
    and Bandmate <> 
        (
         select Id
         from Band
         where Firsname = "Anne-Marit"
        )
) as A
group by Bandmate) as B

join

(select * from Band) as C

on B.Bandmate = C.Id
order by Firsname;


USE `KATZENJAMMER`;
-- KATZENJAMMER-5
-- Report all instruments (in alphabetical order) that were played by three or more people.
select Instrument
from 

(select Instrument, count(distinct Bandmate) as numPeople
from Instruments
group by Instrument
having numPeople >= 3) as A

order by Instrument;


USE `KATZENJAMMER`;
-- KATZENJAMMER-6
-- For each performer, report the number of times they played more than one instrument on the same song. Sort output in alphabetical order by first name of the performer
select Firsname, totalInst 
from

(select Bandmate, count(numInst) as totalInst
from 

    (select Song, Bandmate, count(Instrument) as numInst 
    from Instruments
    group by Song, Bandmate
    having numInst > 1) as A

group by Bandmate) as B

join

(select * from Band) as C

on B.Bandmate = C.Id
order by Firsname;


