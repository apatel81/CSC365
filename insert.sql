INSERT INTO Rooms(roomId, roomName, bedType, numBeds, maxOcc, basePrice, decor) VALUES (1,'Traditional','King',1,3,99.99,'Modern');
INSERT INTO Rooms(roomId, roomName, bedType, numBeds, maxOcc, basePrice, decor) VALUES (2,'Ancient','Double',2,5,109.99,'Rustic');
INSERT INTO Rooms(roomId, roomName, bedType, numBeds, maxOcc, basePrice, decor) VALUES (3,'Coastal','Queen',2,4,105.99,'Modern');
INSERT INTO Rooms(roomId, roomName, bedType, numBeds, maxOcc, basePrice, decor) VALUES (4,'Habitat','Double',2,5,109.99,'Modern');
INSERT INTO Rooms(roomId, roomName, bedType, numBeds, maxOcc, basePrice, decor) VALUES (5,'Woodland','Queen',2,4,119.99,'Modern');
INSERT INTO Rooms(roomId, roomName, bedType, numBeds, maxOcc, basePrice, decor) VALUES (6,'Superior','King',3,6,299.99,'Modern');
INSERT INTO Rooms(roomId, roomName, bedType, numBeds, maxOcc, basePrice, decor) VALUES (7,'Hobit Hole','Double',1,1,59.99,'Rustic');
INSERT INTO Rooms(roomId, roomName, bedType, numBeds, maxOcc, basePrice, decor) VALUES (8,'River','Queen',2,4,129.99,'Modern');
INSERT INTO Rooms(roomId, roomName, bedType, numBeds, maxOcc, basePrice, decor) VALUES (9,'Urban','Queen',2,5,99.99,'Rustic');
INSERT INTO Rooms(roomId, roomName, bedType, numBeds, maxOcc, basePrice, decor) VALUES (10,'Industrial','Double',3,3,159.99,'Rustic');
INSERT INTO Customer(cId, name, address, phone) VALUES (1,'Ajay','1487 Canyon Circle',5597970044);
INSERT INTO Customer(cId, name, address, phone) VALUES (2,'Patel','1 Grand Ave',5592990060);
INSERT INTO Customer(cId, name, address, phone) VALUES (3,'Ethan','6 S Tassajara Dr',3034549242);
INSERT INTO Customer(cId, name, address, phone) VALUES (4,'Baylor','10 Mustang Way',8403829384);
INSERT INTO Customer(cId, name, address, phone) VALUES (5,'Monica Whitehead','11365 SW Bobwhite Pl.',5038071930);
INSERT INTO Customer(cId, name, address, phone) VALUES (6,'John Whitehead','11365 SW Bobwhite Pl.',5038071930);
INSERT INTO Credit_Card(ccn, type, secCode, expDate, bill_address) VALUES (1234567890123450,'Visa',123,'2019-05-22','1487 Canyon Circle');
INSERT INTO Credit_Card(ccn, type, secCode, expDate, bill_address) VALUES (6543210987654320,'MasterCard',321,'2020-01-01','1 Grand Ave');
INSERT INTO Credit_Card(ccn, type, secCode, expDate, bill_address) VALUES (1112345678912340,'MasterCard',001,'2020-03-08','Northridge Hills St.');
INSERT INTO Credit_Card(ccn, type, secCode, expDate, bill_address) VALUES (3729483028472640,'Discover',333,'2021-01-03','SW 8th st');
INSERT INTO Credit_Card(ccn, type, secCode, expDate, bill_address) VALUES (8768098778909998,'Discover',323,'2021-01-02','11365 SW Bobwhite Pl.');
INSERT INTO Credit_Card(ccn, type, secCode, expDate, bill_address) VALUES (1111333355557777,'Discover',999,'2023-01-02','11365 SW Bobwhite Pl.');
INSERT INTO Ownership(cId, ccn) VALUES (1,1234567890123450);
INSERT INTO Ownership(cId, ccn) VALUES (2,6543210987654320);
INSERT INTO Ownership(cId, ccn) VALUES (3,1112345678912340);
INSERT INTO Ownership(cId, ccn) VALUES (4,3729483028472640);
INSERT INTO Ownership(cId, ccn) VALUES (5,8768098778909998);
INSERT INTO Ownership(cId, ccn) VALUES (6,1111333355557777);
INSERT INTO Reservations(resId, cId, roomId, checkIn, checkOut, rate, numAdults, numKids, cancelled) VALUES (1,1,2,'2019-04-20','2019-05-23',89.99,3,0,0);
INSERT INTO Reservations(resId, cId, roomId, checkIn, checkOut, rate, numAdults, numKids, cancelled) VALUES (2,2,1,'2019-05-22','2019-05-23',119.99,1,2,0);
INSERT INTO Reservations(resId, cId, roomId, checkIn, checkOut, rate, numAdults, numKids, cancelled) VALUES (3,3,3,'2019-05-23','2019-05-26',110.99,2,3,0);
INSERT INTO Reservations(resId, cId, roomId, checkIn, checkOut, rate, numAdults, numKids, cancelled) VALUES (4,4,4,'2019-05-25','2019-05-26',109.99,2,0,0);
INSERT INTO Reservations(resId, cId, roomId, checkIn, checkOut, rate, numAdults, numKids, cancelled) VALUES (5,1,1,'2019-05-24','2019-05-27',99.99,1,0,0);
INSERT INTO Reservations(resId, cId, roomId, checkIn, checkOut, rate, numAdults, numKids, cancelled) VALUES (6,4,1,'2018-11-28','2018-12-10',99.99,1,0,0);
INSERT INTO Reservations(resId, cId, roomId, checkIn, checkOut, rate, numAdults, numKids, cancelled) VALUES (7,4,2,'2019-06-04','2019-06-10',99.99,1,0,0);
INSERT INTO Reservations(resId, cId, roomId, checkIn, checkOut, rate, numAdults, numKids, cancelled) VALUES (8,4,1,'2019-05-27','2019-06-12',99.99,1,0,0);
INSERT INTO Reservations(resId, cId, roomId, checkIn, checkOut, rate, numAdults, numKids, cancelled) VALUES (9,4,2,'2019-06-10','2019-06-20',99.99,1,0,0);
INSERT INTO Reservations(resId, cId, roomId, checkIn, checkOut, rate, numAdults, numKids, cancelled) VALUES (10,1,2,'2019-05-23','2019-06-04',99.99,1,0,0);
INSERT INTO Reservations(resId, cId, roomId, checkIn, checkOut, rate, numAdults, numKids, cancelled) VALUES (11,3,5,'2019-05-20','2019-06-12',300,1,0,0);
INSERT INTO Reservations(resId, cId, roomId, checkIn, checkOut, rate, numAdults, numKids, cancelled) VALUES (12,1,5,'2019-06-27','2019-06-07',99.99,1,0,0);
INSERT INTO Transaction(tId, ccn, resId, amountPaid, datePaid) VALUES (1,1234567890123450,1,3000,'2019-05-18');
INSERT INTO Transaction(tId, ccn, resId, amountPaid, datePaid) VALUES (2,6543210987654320,2,119.99,'2019-05-18');
INSERT INTO Transaction(tId, ccn, resId, amountPaid, datePaid) VALUES (3,1112345678912340,3,110.99,'2019-05-20');
INSERT INTO Transaction(tId, ccn, resId, amountPaid, datePaid) VALUES (4,1111333355557777,4,500.99,'2019-05-20');
INSERT INTO Transaction(tId, ccn, resId, amountPaid, datePaid) VALUES (5,8768098778909998,5,444.99,'2019-06-20');
INSERT INTO Transaction(tId, ccn, resId, amountPaid, datePaid) VALUES (6,3729483028472640,6,333.99,'2019-06-07');
INSERT INTO Transaction(tId, ccn, resId, amountPaid, datePaid) VALUES (7,3729483028472640,7,900.99,'2019-06-20');
INSERT INTO Transaction(tId, ccn, resId, amountPaid, datePaid) VALUES (8,3729483028472640,8,300.99,'2019-05-20');
INSERT INTO Transaction(tId, ccn, resId, amountPaid, datePaid) VALUES (9,3729483028472640,9,77.99,'2019-05-20');
INSERT INTO Transaction(tId, ccn, resId, amountPaid, datePaid) VALUES (10,1234567890123450,9,77.99,'2019-05-20');
INSERT INTO Transaction(tId, ccn, resId, amountPaid, datePaid) VALUES (11,1112345678912340,9,77.99,'2019-05-20');
INSERT INTO Transaction(tId, ccn, resId, amountPaid, datePaid) VALUES (12,1234567890123450,9,77.99,'2019-05-20');
