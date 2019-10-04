-- Lab 6
-- apatel81
-- May 27, 2019

USE `BAKERY`;
-- BAKERY-1
-- Find all customers who did not make a purchase between October 5 and October 11 (inclusive) of 2007. Output first and last name in alphabetical order by last name.
select Firstname, LastName 
from

(select distinct Customer 
from receipts
where Customer not in 
    (select distinct Customer 
    from receipts
    where SaleDate >= 20071005 and SaleDate <= 20071011)
    )
as A 

join

(select * from customers) as B

on A.Customer = B.CId
order by LastName;


USE `BAKERY`;
-- BAKERY-2
-- Find the customer(s) who spent the most money at the bakery during October of 2007. Report first, last name and total amount spent (rounded to two decimal places). Sort by last name.
select FirstName, LastName, round(Total, 2) 
from 

(select *
from
(select Customer, sum(total) as Total
from

(select Receipt, sum(PRICE) as total
from 

(select Receipt, Item
from items) as A

join

(select GId, PRICE from goods) as B

on A.Item = B.GId

group by Receipt) as C

join

(select RNumber, Customer 
 from receipts 
 where SaleDate >= 20071001 and SaleDate <= 20071031) as D
 
on C.Receipt = D.RNumber
group by Customer) as E

join

(select * from customers) as F

on E.Customer = F.CId) as G

where Total = 
    (select max(Total)
    from
    (select Customer, sum(total) as Total
    from
    
    (select Receipt, sum(PRICE) as total
    from 
    
    (select Receipt, Item
    from items) as A
    
    join
    
    (select GId, PRICE from goods) as B
    
    on A.Item = B.GId
    
    group by Receipt) as C
    
    join
    
    (select RNumber, Customer 
     from receipts 
     where SaleDate >= 20071001 and SaleDate <= 20071031) as D
     
    on C.Receipt = D.RNumber
    group by Customer) as E
    
    join
    
    (select * from customers) as F
    
    on E.Customer = F.CId);


USE `BAKERY`;
-- BAKERY-3
-- Find all customers who never purchased a twist ('Twist') during October 2007. Report first and last name in alphabetical order by last name.

select FirstName, LastName
from customers
where Cid not in 
    (select Customer
    from receipts
    where RNumber in
        (  
        select Receipt
        from items 
        where Item = 
            (
            select GId 
            from goods
            where Food = "Twist"
            )
        )
        ) 
order by LastName;


USE `BAKERY`;
-- BAKERY-4
-- Find the type of baked good (food type & flavor) responsible for the most total revenue.
select Flavor, Food
from goods
where GId = 
    (
    select Item 
    from 
        
        (select Item, count(Item), sum(PRICE) as total
        from 
        
        (select Item
        from items) as A
        
        join
        
        (select * from goods) as B
        
        on A.Item = B.GId
        group by Item) as C
        
    where total = 
        (
        select max(total) from 
            (    
            select Item, count(Item), sum(PRICE) as total
            from 
            
            (select Item
            from items) as A
            
            join
            
            (select * from goods) as B
            
            on A.Item = B.GId
            group by Item
            ) as D
        )
    );


USE `BAKERY`;
-- BAKERY-5
-- Find the most popular item, based on number of pastries sold. Report the item (food, flavor) and total quantity sold.
select Flavor, Food, totCount 
from
(
    select * from
    (select Item, count(Item) as totCount from 
    (select Item from items) as A
    
    join 
    
    (select * from goods) as B
    
    on A.Item = B.GId
    group by Item) as C
    
    where totCount = 
        (
        select max(totCount) from
            (    
            select Item, count(Item) as totCount from 
            (select Item from items) as A
            
            join 
            (select * from goods) as B
            
            on A.Item = B.GId
            group by Item
            ) as C
        )
) as D

join

(select * from goods) as E

on D.Item = E.GId;


USE `BAKERY`;
-- BAKERY-6
-- Find the date of highest revenue during the month of October, 2007.
select SaleDate
from 
    (
    select SaleDate, sum(PRICE) as total 
    from 
        (
        select SaleDate, Receipt, Item
        from
        
        (select * from receipts) as A
        
        join 
        
        (select * from items) as B
        
        on A.RNumber = B.Receipt
        ) as C
        
    join 
    
    (select * from goods) as D
    
    on C.Item = D.GId
    group by SaleDate
    ) as E
    
where total = 
    (
    select max(total) from
    (    
        select SaleDate, sum(PRICE) as total 
        from 
            (
            select SaleDate, Receipt, Item
            from
            
            (select * from receipts) as A
            
            join 
            
            (select * from items) as B
            
            on A.RNumber = B.Receipt
            ) as C
            
        join 
        
        (select * from goods) as D
        
        on C.Item = D.GId
        group by SaleDate
        ) as E
    );


USE `BAKERY`;
-- BAKERY-7
-- Find the best-selling item (by number of purchases) on the day of the highest revenue in October of 2007.
select Flavor, Food, numItems
from
(
select *
from
(
    select Item, count(Item) as numItems
    from
    (
    select RNumber 
    from receipts
    where SaleDate =
        (
        select SaleDate
        from 
            (
            select SaleDate, sum(PRICE) as total 
            from 
                (
                select SaleDate, Receipt, Item
                from
                
                (select * from receipts) as A
                
                join 
                
                (select * from items) as B
                
                on A.RNumber = B.Receipt
                ) as C
                
            join 
            
            (select * from goods) as D
            
            on C.Item = D.GId
            group by SaleDate
            ) as E
            
        where total = 
            (
            select max(total) from
            (    
                select SaleDate, sum(PRICE) as total 
                from 
                    (
                    select SaleDate, Receipt, Item
                    from
                    
                    (select * from receipts) as A
                    
                    join 
                    
                    (select * from items) as B
                    
                    on A.RNumber = B.Receipt
                    ) as C
                    
                join 
                
                (select * from goods) as D
                
                on C.Item = D.GId
                group by SaleDate
                ) as E
            )
        ) 
        
    ) as F
    
    join 
    
    (select * from items) as G
    
    on F.RNumber = G.Receipt
    group by Item
    
) as H

where numItems = 
    (
    select max(numItems) from
    (
    select Item, count(Item) as numItems
    from
    (
    select RNumber 
    from receipts
    where SaleDate =
        (
            select SaleDate
            from 
                (
                select SaleDate, sum(PRICE) as total 
                from 
                    (
                    select SaleDate, Receipt, Item
                    from
                    
                    (select * from receipts) as A
                    
                    join 
                    
                    (select * from items) as B
                    
                    on A.RNumber = B.Receipt
                    ) as C
                    
                join 
                
                (select * from goods) as D
                
                on C.Item = D.GId
                group by SaleDate
                ) as E
                
            where total = 
                (
                select max(total) from
                (    
                    select SaleDate, sum(PRICE) as total 
                    from 
                        (
                        select SaleDate, Receipt, Item
                        from
                        
                        (select * from receipts) as A
                        
                        join 
                        
                        (select * from items) as B
                        
                        on A.RNumber = B.Receipt
                        ) as C
                        
                    join 
                    
                    (select * from goods) as D
                    
                    on C.Item = D.GId
                    group by SaleDate
                    ) as E
                )
            ) 
            
        ) as F
        
        join 
        
        (select * from items) as G
        
        on F.RNumber = G.Receipt
        group by Item
        ) as H
    )
) as I

join 

(select * from goods) as J

on I.Item = J.GId;


USE `BAKERY`;
-- BAKERY-8
-- For every type of Cake report the customer(s) who purchased it the largest number of times during the month of October 2007. Report the name of the pastry (flavor, food type), the name of the customer (first, last), and the number of purchases made. Sort output in descending order on the number of purchases, then in alphabetical order by last name of the customer, then by flavor.
select X.Flavor, X.Food, FirstName, LastName, X.qty
from

(select Food, Flavor, max(qty) as qty
from
(select LastName, FirstName, Food, Flavor, count(Flavor) as qty
from
(
    select * 
    from
        (
        select Item, Flavor, Food, Receipt
        from 
        
        (select * from goods where Food = "Cake") as A
        
        join 
        
        (select * from items) as B
        
        on A.Gid = B.Item) as C
    
    join 
    
    (select RNumber, Customer from receipts) as D
    
    on C.Receipt = D.RNumber
) as E

join
 
(select * from customers) as F

on E.Customer = F.CId

group by LastName, FirstName, Food, Flavor) as Z

group by Food, Flavor) as Y

join

(
    select LastName, FirstName, Food, Flavor, count(Flavor) as qty
    from
    (
        select * 
        from
            (
            select Item, Flavor, Food, Receipt
            from 
            
            (select * from goods where Food = "Cake") as A
            
            join 
            
            (select * from items) as B
            
            on A.Gid = B.Item) as C
        
        join 
        
        (select RNumber, Customer from receipts) as D
        
        on C.Receipt = D.RNumber
    ) as E
    
    join
     
    (select * from customers) as F
    
    on E.Customer = F.CId
    
    group by LastName, FirstName, Food, Flavor
    
) as X

on Y.Food = X.Food and
   Y.Flavor = X.Flavor and
   Y.qty = X.qty
   
order by qty desc, LastName, flavor;


USE `BAKERY`;
-- BAKERY-9
-- Output the names of all customers who made multiple purchases (more than one receipt) on the latest day in October on which they made a purchase. Report names (first, last) of the customers and the earliest day in October on which they made a purchase, sorted in chronological order.

select LastName, FirstName, SaleDate
from
(
    select C.Customer, min(SaleDate) as SaleDate
    from
    (
        select A.Customer
        from 
        
        (select Customer, SaleDate, count(SaleDate) as countSD
         from receipts
         group by Customer, SaleDate
         order by Customer) as A
        
        join
        
        (select Customer, max(SaleDate) as SaleDate
         from receipts
         group by Customer) as B
        
        on A.Customer = B.Customer and
          A.SaleDate = B.SaleDate
        
        where countSD > 1
        
    ) as C
    
    join
    
    (select Customer, SaleDate, count(SaleDate) as countSD
     from receipts
     group by Customer, SaleDate
     order by Customer) as D
     
    on C.Customer = D.Customer
    group by C.Customer
    
) as E

join 

(select * from customers) as F

on E.Customer = F.CId
order by SaleDate;


USE `BAKERY`;
-- BAKERY-10
-- Find out if sales (in terms of revenue) of Chocolate-flavored items or sales of Croissants (of all flavors) were higher in October of 2007. Output the word 'Chocolate' if sales of Chocolate-flavored items had higher revenue, or the word 'Croissant' if sales of Croissants brought in more revenue.

select item
from
    (
    (
        select Flavor as item , sum(Price) as Total
        from 
        
        (
        select * 
        from items
        where Item in
            (
            select GId
            from goods
            where Flavor = "Chocolate" or 
                  Food = "Croissant"
            )
        ) as A   
        
        join 
        
        (select * 
         from goods 
         where Flavor = "Chocolate" or
              Food = "Croissant") as B
               
        on A.Item = B.GId
        where Flavor = "Chocolate"
    ) 

    UNION 

    (select Food as item, sum(Price) as Total
    from 
    
        (
        select * 
        from items
        where Item in
            (
            select GId
            from goods
            where Flavor = "Chocolate" or 
                  Food = "Croissant"
            )
        ) as A   
    
        join 
    
        (select * 
         from goods 
         where Flavor = "Chocolate" or
              Food = "Croissant") as B
               
        on A.Item = B.GId
    where Food = "Croissant") 

    ) as Z
    
where Total = 
    (
    select max(Total)
    from
        (
        (
            select Flavor as item , sum(Price) as Total
            from 
            
            (
            select * 
            from items
            where Item in
                (
                select GId
                from goods
                where Flavor = "Chocolate" or 
                      Food = "Croissant"
                )
            ) as A   
            
            join 
            
            (select * 
             from goods 
             where Flavor = "Chocolate" or
                  Food = "Croissant") as B
                   
            on A.Item = B.GId
            where Flavor = "Chocolate"
        ) 
    
        UNION 
    
        (select Food as item, sum(Price) as Total
        from 
        
            (
            select * 
            from items
            where Item in
                (
                select GId
                from goods
                where Flavor = "Chocolate" or 
                      Food = "Croissant"
                )
            ) as A   
        
            join 
        
            (select * 
             from goods 
             where Flavor = "Chocolate" or
                  Food = "Croissant") as B
                   
            on A.Item = B.GId
        where Food = "Croissant") 
    
        ) as Z
    );


USE `INN`;
-- INN-1
-- Find the most popular room (based on the number of reservations) in the hotel  (Note: if there is a tie for the most popular room status, report all such rooms). Report the full name of the room, the room code and the number of reservations.

select RoomName, Room, cntRms
from 
(
 select Room, count(Room) as cntRms
 from reservations
 group by Room
 having cntRms = 
    (select max(cntRms)
     from
     (select Room, count(Room) as cntRms 
      from reservations
      group by Room) as A)
 
) as B 

join

(select RoomCode, RoomName from rooms) as C

on B.Room = C.RoomCode;


USE `INN`;
-- INN-2
-- Find the room(s) that have been occupied the largest number of days based on all reservations in the database. Report the room name(s), room code(s) and the number of days occupied. Sort by room name.
select RoomName, Room, nights
from 
(
 select Room, sum(datediff(Checkout, CheckIn)) as nights
 from reservations
 group by Room
 having nights = 

    (select max(nights)
    from 
    (select Room, sum(datediff(Checkout, CheckIn)) as nights
    from reservations
    group by Room) as A)
    
) as B 

join

(select RoomCode, RoomName from rooms) as C

on B.Room = C.RoomCode;


USE `INN`;
-- INN-3
-- For each room, report the most expensive reservation. Report the full room name, dates of stay, last name of the person who made the reservation, daily rate and the total amount paid. Sort the output in descending order by total amount paid.
select RoomName, CheckIn, CheckOut, LastName, Rate, A.maxStay as total
from

(select Room, max(datediff(Checkout, CheckIn) * Rate) as maxStay
 from reservations
 group by Room) as A

join

(select Room, (datediff(Checkout, CheckIn) * Rate) as maxStay,
 Rate, CheckIn, CheckOut, LastName
 from reservations) as B

on A.Room = B.Room and
   A.maxStay = B.maxStay
   
   
join 

(select RoomName, RoomCode from rooms) as C

on A.Room = C.RoomCode

order by total desc;


USE `INN`;
-- INN-4
-- For each room, report whether it is occupied or unoccupied on July 4, 2010. Report the full name of the room, the room code, and either 'Occupied' or 'Empty' depending on whether the room is occupied on that day. (the room is occupied if there is someone staying the night of July 4, 2010. It is NOT occupied if there is a checkout on this day, but no checkin). Output in alphabetical order by room code. 
select RoomName, Room, July4
from 
(
select distinct Room, @July4Status := "Occupied" as July4
from reservations
where Room in

(select distinct Room
from reservations
where CheckIn <= 20100704 and CheckOut >= 20100705)

UNION

select distinct Room, @July4Status := "Empty" as July4
from reservations
where Room not in
(select distinct Room
from reservations
where CheckIn <= 20100704 and CheckOut >= 20100705)
) as A

join

(select RoomName, RoomCode from rooms) as B

on A.Room = B.RoomCode
order by Room;


USE `INN`;
-- INN-5
-- Find the highest-grossing month (or months, in case of a tie). Report the month, the total number of reservations and the revenue. For the purposes of the query, count the entire revenue of a stay that commenced in one month and ended in another towards the earlier month. (e.g., a September 29 - October 3 stay is counted as September stay for the purpose of revenue computation). In case of a tie, months should be sorted in chronological order.
select month, nRes, rev
from
    (
    select sum(datediff(Checkout, Checkin) * rate) as rev, 
    monthname(Checkin) as month, 
    count(monthname(Checkin)) as nRes
    from reservations
    group by month
    ) as A

where rev = 
    (
    select max(rev)
    from
        (
        select sum(datediff(Checkout, Checkin) * rate) as rev, 
        monthname(Checkin) as month
        from reservations
        group by month
        ) as A
    )
order by month desc;


USE `STUDENTS`;
-- STUDENTS-1
-- Find the teacher(s) who teach(es) the largest number of students. Report the name of the teacher(s) (first and last) and the number of students in their class.

select Last, First, cnt
from
(
select classroom, count(classroom) as cnt
from list
group by classroom
having cnt = 
    (
    select max(cnt) as cnt
    from
        (
        select classroom, count(classroom) as cnt
        from list
        group by classroom
        ) as A
    )
) as C
join 

(select * from teachers) as B

on C.classroom = B.classroom;


USE `STUDENTS`;
-- STUDENTS-2
-- Find the grade(s) with the largest number of students whose last names start with letters 'A', 'B' or 'C' Report the grade and the number of students
select grade, count(grade) as cnt
from list
where LastName like "A%" or
      LastName like "B%" or
      LastName like "C%"
      
group by grade
having cnt = 
    (
    select max(cnt)
    from 
        (
        select grade, count(grade) as cnt
        from list
        where LastName like "A%" or
              LastName like "B%" or
              LastName like "C%"
          
        group by grade
        ) as A
    );


USE `STUDENTS`;
-- STUDENTS-3
-- Find all classrooms which have fewer students in them than the average number of students in a classroom in the school. Report the classroom numbers in ascending order. Report the number of student in each classroom.
select classroom, count(classroom) as numStudents
from list
group by classroom
having numStudents < 
    (
    select avg(numStudents) as avgStudents
    from
    (select classroom, count(classroom) as numStudents
    from list
    group by classroom) as A
    )
order by classroom;


USE `STUDENTS`;
-- STUDENTS-4
--  Find all pairs of classrooms with the same number of students in them. Report each pair only once. Report both classrooms and the number of students. Sort output in ascending order by the number of students in the classroom.
select A.classroom, B.classroom, A.numStudents 
from 
(
select classroom, count(classroom) as numStudents
from list
group by classroom
) as A

join

(
select classroom, count(classroom) as numStudents
from list
group by classroom
) as B

on A.numStudents = B.numStudents and
   A.classroom < B.classroom
  
order by A.numStudents;


USE `STUDENTS`;
-- STUDENTS-5
-- For each grade with more than one classroom, report the last name of the teacher who teachers the classroom with the largest number of students in the grade. Output results in ascending order by grade.
select C.grade as grade, Last
from 
(
select grade, max(numStudents) as mostStudents
from
(
select grade, classroom, count(classroom) as numStudents
from list
group by grade, classroom
having grade in 
    (
    select grade 
    from 
        (
        select grade, count(distinct classroom) as multClass
        from list
        group by grade
        having multClass > 1
        ) as A
    )
) as B

group by grade
) as C

join

(
select grade, classroom, count(classroom) as numStudents
from list
group by grade, classroom
having grade in 
    (
    select grade 
    from 
        (
        select grade, count(distinct classroom) as multClass
        from list
        group by grade
        having multClass > 1
        ) as A
    )
) as D

on C.grade = D.grade and
   C.mostStudents = D.numStudents
   
join

(select * from teachers) as E

on D.classroom = E.classroom
order by grade;


USE `CSU`;
-- CSU-1
-- Find the campus with the largest enrollment in 2000. Output the name of the campus and the enrollment.

select Campus, Enrolled
from
(
select *
from enrollments
where Year = 2000 and
      Enrolled = 
        (
         select max(Enrolled) as maxE
         from enrollments
         where Year = 2000
        )
) as A
join

(select * from campuses) as B

on A.CampusId = B.Id;


USE `CSU`;
-- CSU-2
-- Find the university that granted the highest average number of degrees per year over its entire recorded history. Report the name of the university.

select campus
from
(
    select CampusId
    from
    (
    select CampusId, avg(degrees) as avgDeg
    from degrees
    group by CampusId
    ) as A
    
    join
    
    (
    select max(avgDeg) as avgDeg
    from
        (
        select CampusId, avg(degrees) as avgDeg
        from degrees
        group by CampusId
        ) as A
    )  as B
    
    on A.avgDeg = B.avgDeg
    
) as C

join 

(select * from campuses) as D

on C.CampusId = D.Id;


USE `CSU`;
-- CSU-3
-- Find the university with the lowest student-to-faculty ratio in 2003. Report the name of the campus and the student-to-faculty ratio, rounded to one decimal place. Use FTE numbers for enrollment.
select Campus, ratio
from
    (select *
    from 
        (
        select A.CampusId, round(A.FTE/B.FTE, 1) as ratio 
        from 
        
        (select *
        from enrollments
        where Year = 2003) as A
        
        join
        
        (select *
        from faculty
        where Year = 2003) as B
        
        on A.CampusId = B.CampusId
        
        ) as C
        
    where ratio =
        (select min(ratio) from
            (
            select A.CampusId, round(A.FTE/B.FTE, 1) as ratio 
            from 
            
            (select *
            from enrollments
            where Year = 2003) as A
            
            join
            
            (select *
            from faculty
            where Year = 2003) as B
            
            on A.CampusId = B.CampusId
            
            ) as C
        )
    ) as D

join

(select * from campuses) as E

on D.CampusId = E.Id;


USE `CSU`;
-- CSU-4
-- Find the university with the largest percentage of the undergraduate student body in the discipline 'Computer and Info. Sciences' in 2004. Output the name of the campus and the percent of these undergraduate students on campus.
select Campus, perUg
from
(
    select *
    from
    (
        select A.CampusId, round((Ug/Enrolled) * 100, 1) as perUg
        from
        (
        select CampusId, Ug
        from discEnr
        where Year = 2004 and
              Discipline = 
                (
                select Id from disciplines
                where Name = "Computer and Info. Sciences"
                )
        ) as A   
        
        join
        
        (
        select * 
        from enrollments
        where Year = 2004
        ) as B
        
        on A.CampusId = B.CampusId
        
    ) as C
    
    where perUg =
        (
        select max(perUg) as perUg
        from
            (
                select A.CampusId, round((Ug/Enrolled) * 100, 1) as perUg
                from
                (
                select CampusId, Ug
                from discEnr
                where Year = 2004 and
                      Discipline = 
                        (
                        select Id from disciplines
                        where Name = "Computer and Info. Sciences"
                        )
                ) as A   
                
                join
                
                (
                select * 
                from enrollments
                where Year = 2004
                ) as B
                
                on A.CampusId = B.CampusId
                
            ) as C
    
        )
) as D

join 

(select * from campuses) as E

on D.CampusId = E.Id;


USE `CSU`;
-- CSU-5
-- For each year between 1997 and 2003 (inclusive) find the university with the highest ratio of total degrees granted to total enrollment (use enrollment numbers). Report the years, the names of the campuses and the ratios. List in chronological order.
-- No attempt


USE `CSU`;
-- CSU-6
-- For each campus report the year of the best student-to-faculty ratio, together with the ratio itself. Sort output in alphabetical order by campus name. Use FTE numbers to compute ratios.
select Campus, F.Year, ratio
from 
    (
    select D.CampusId as CampusId, D.ratio as ratio, Year
    from
        (
        select CampusId, max(ratio) as ratio
        from 
        (
        select A.CampusId as CampusId, A.Year as Year, 
        round(A.FTE/B.FTE, 2) as ratio
        from
            (select *
            from enrollments) as A
            
            join
            
            (select *
            from faculty) as B
            
            on A.Year = B.Year and 
               A.CampusId = B.CampusId
               
        ) as C
        
        group by CampusId) as D
        
        join
        
        (
        select A.CampusId as CampusId, A.Year as Year, 
        round(A.FTE/B.FTE, 2) as ratio
        from
            (select *
            from enrollments) as A
            
            join
            
            (select *
            from faculty) as B
            
            on A.Year = B.Year and 
               A.CampusId = B.CampusId
        ) as E
        
        on D.CampusId = E.CampusId and
           D.ratio = E.ratio
     
    ) as F
    
    join
    
    (select * from campuses) as G
    
    on F.CampusId = G.Id
      
order by Campus;


USE `CSU`;
-- CSU-7
-- For each year (for which the data is available) report the total number of campuses in which student-to-faculty ratio became worse (i.e. more students per faculty) as compared to the previous year. Report in chronological order.

select C.Year, count(C.CampusId)
from
    (
    select A.CampusId as CampusId,
           A.Year as Year, A.FTE/B.FTE as ratio
    from
        (
        select *
        from enrollments
        ) as A
        
    join
    
        (
        select *
        from faculty
        ) as B
        
    on A.CampusId = B.CampusId and 
       A.Year = B.Year
    ) as C
   
join

    (
    select A.CampusId as CampusId,
           A.Year as Year, A.FTE/B.FTE as ratio
    from
        (
        select *
        from enrollments
        ) as A
        
    join
    
        (
        select *
        from faculty
        ) as B
        
    on A.CampusId = B.CampusId and 
       A.Year = B.Year
    ) as D
    
on C.CampusId = D.CampusId and
   C.Year - 1 = D.Year and
   C.ratio > D.ratio
 
group by C.Year
order by C.Year;


USE `MARATHON`;
-- MARATHON-1
-- Find the state(s) with the largest number of participants. List state code(s) sorted alphabetically.

select State
from
(
select State, count(State) as cntState
from marathon
group by State
having cntState =
    (
    select max(cntState) as cntState
    from
        (   
        select State, count(State) as cntState
        from marathon
        group by State
        ) as A
    )
) as B;


USE `MARATHON`;
-- MARATHON-2
--  Find all towns in Rhode Island (RI) which fielded more female runners than male runners for the race. Report the names of towns, sorted alphabetically.

select A.Town 
from
(
    select Sex, count(Sex) as cntSex, Town
    from marathon
    where State = "RI" and Sex = "F"
    group by Town, Sex
) as A
 
join

(
    select Sex, count(Sex) as cntSex, Town
    from marathon
    where State = "RI" and Sex = "M"
    group by Town, Sex
) as B

on A.Town = B.Town and
   A.cntSex > B.cntSex
   
order by A.Town;


USE `MARATHON`;
-- MARATHON-3
-- For each state, report the gender-age group with the largest number of participants. Output state, age group, gender, and the number of runners in the group. Report only information for the states where the largest number of participants in a gender-age group is greater than one. Sort in alphabetical order by state code and gender.
select A.State, AgeGroup, Sex, B.cnt
from

(
select State, max(cnt) as cnt
from
    (                
    select State, AgeGroup, Sex, 
    count(AgeGroup) as cnt
    
    from marathon
    group by State, AgeGroup, Sex
    order by State
    ) as A

group by State) as A

join 

(                
    select State, AgeGroup, Sex, 
    count(AgeGroup) as cnt
    
    from marathon
    group by State, AgeGroup, Sex
    order by State
) as B

on A.State = B.State and
   A.cnt = B.cnt
   
where B.cnt > 1
order by A.State, Sex


-- CT	20-39	M	9
-- CT	40-49	M	9
-- FL	40-49	M	4
-- IN	20-39	M	2
-- MA	20-39	M	61
-- MO	40-49	M	3
-- NC	20-39	F	4
-- NH	40-49	M	6
-- NJ	20-39	F	2
-- NJ	20-39	M	2
-- PA	20-39	F	2
-- PA	20-39	M	2
-- RI	20-39	M	66
-- VT	20-39	M	3;


USE `MARATHON`;
-- MARATHON-4
-- Find the 30th fastest female runner. Report her overall place in the race, first name, and last name. This must be done using a single SQL query (which may be nested) that DOES NOT use the LIMIT clause. Think carefully about what it means for a row to represent the 30th fastest (female) runner.
select *
from marathon 
where Sex = "F"




--       RunTime



-- SELECT *
-- FROM marathon AS a 
-- WHERE Sex = "F" and 
--     15 - 1 = (
--     SELECT COUNT(RunTime) 
--     FROM marathon b 
--     WHERE  b.RunTime > a.RunTime);


USE `MARATHON`;
-- MARATHON-5
-- For each town in Connecticut report the total number of male and the total number of female runners. Both numbers shall be reported on the same line. If no runners of a given gender from the town participated in the marathon, report 0. Sort by number of total runners from each town (in descending order) then by town.

select A.Town, ifnull(Men, 0) as Men, 
ifnull(Women, 0) as Women

from

(select distinct Town
from marathon
where State = "CT") as A

left join

(
select Town, count(Sex) as Men
from marathon
where State = "CT" and Sex = "M"
group by Town, Sex
) as B

on A.Town = B.Town

left join

(
select Town, count(Sex) as Women
from marathon
where State = "CT" and Sex = "F"
group by Town, Sex
) as C

on A.Town = C.Town

order by ifnull(Men,0)+ifnull(Women,0) desc, Town


-- STONINGTON	4	2
-- NORTHFORD	3	2
-- NEW MILFORD	4	0
-- ESSEX	0	3
-- LAKEVILLE	3	0
-- POMFRET	2	1
-- HAMDEN	2	0
-- HIGGANUM	1	1
-- GROTON	1	0
-- NORWICH	1	0
-- OAKDALE	1	0
-- WILLIAMANTIC	0	1;


USE `KATZENJAMMER`;
-- KATZENJAMMER-1
-- Report the first name of the performer who never played accordion.

-- No attempt


USE `KATZENJAMMER`;
-- KATZENJAMMER-2
-- Report, in alphabetical order, the titles of all instrumental compositions performed by Katzenjammer ("instrumental composition" means no vocals).

-- No attempt


USE `KATZENJAMMER`;
-- KATZENJAMMER-3
-- Report the title(s) of the song(s) that involved the largest number of different instruments played (if multiple songs, report the titles in alphabetical order).
-- No attempt


USE `KATZENJAMMER`;
-- KATZENJAMMER-4
-- Find the favorite instrument of each performer. Report the first name of the performer, the name of the instrument and the number of songs the performer played the instrument on. Sort in alphabetical order by the first name.

-- No attempt


USE `KATZENJAMMER`;
-- KATZENJAMMER-5
-- Find all instruments played ONLY by Anne-Marit. Report instruments in alphabetical order.
-- No attempt


USE `KATZENJAMMER`;
-- KATZENJAMMER-6
-- Report the first name of the performer who played the largest number of different instruments. Sort in ascending order.

-- No attempt


USE `KATZENJAMMER`;
-- KATZENJAMMER-8
-- Who spent the most time performing in the center of the stage (in terms of number of songs on which she was positioned there)? Return just the first name of the performer(s). Sort in ascending order.

-- No attempt


USE `KATZENJAMMER`;
-- KATZENJAMMER-7
-- Which instrument(s) was/were played on the largest number of songs? Report just the names of the instruments (note, you are counting number of songs on which an instrument was played, make sure to not count two different performers playing same instrument on the same song twice).
-- No attempt


