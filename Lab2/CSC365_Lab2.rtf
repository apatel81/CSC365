{\rtf1\ansi\ansicpg1252\cocoartf1561\cocoasubrtf600
{\fonttbl\f0\fswiss\fcharset0 Helvetica;}
{\colortbl;\red255\green255\blue255;}
{\*\expandedcolortbl;;}
\margl1440\margr1440\vieww28300\viewh17700\viewkind0
\pard\tx720\tx1440\tx2160\tx2880\tx3600\tx4320\tx5040\tx5760\tx6480\tx7200\tx7920\tx8640\pardirnatural\partightenfactor0

\f0\fs24 \cf0 CREATE TABLE Customer(\
	id INT NOT NULL AUTO_INCREMENT,\
	name VARCHAR(50) NOT NULL,\
	ssn INT NOT NULL UNIQUE,\
	address VARCHAR(100), \
	phone_number BIGINT(10),\
	PRIMARY KEY(id)\
	);\
\
\
CREATE TABLE CreditCard(\
	cc_number INT NOT NULL AUTO_INCREMENT,\
	cc_type VARCHAR(50) NOT NULL,\
	credit_limit INT,\
	credit_balance INT,\
	is_active TINYINT(1) DEFAULT 0,\
	PRIMARY KEY (cc_number)\
	);\
\
\
CREATE TABLE Ownership(\
	customer_id INT NOT NULL,\
	cc_number INT NOT NULL,\
	is_current TINYINT(1) DEFAULT 1,\
	FOREIGN KEY (customer_id) REFERENCES Customer (id),\
	FOREIGN KEY (cc_number) REFERENCES CreditCard(cc_number),	\
	PRIMARY KEY (customer_id, cc_number)\
	);\
\
\
CREATE TABLE Vendor(\
	id INT NOT NULL AUTO_INCREMENT,\
	name VARCHAR(50) NOT NULL,\
	address VARCHAR(100), \
	PRIMARY KEY (id)\
	);\
\
\
CREATE TABLE Payment(\
	id INT NOT NULL AUTO_INCREMENT,\
	customer_id INT NOT NULL,\
	cc_number INT NOT NULL,\
	amount_paid FLOAT NOT NULL,\
	date DATE NOT NULL,\
	FOREIGN KEY (customer_id) REFERENCES Customer (id),\
	FOREIGN KEY (cc_number) REFERENCES CreditCard(cc_number),\
	PRIMARY KEY (id)\
	);\
\
\
CREATE TABLE Transaction(\
	id INT NOT NULL AUTO_INCREMENT,\
	customer_id INT NOT NULL,\
	cc_number INT NOT NULL,\
	vendor_id INT NOT NULL,\
	amount_spent FLOAT NOT NULL,\
	date DATE NOT NULL,\
	FOREIGN KEY (customer_id) REFERENCES Customer (id),\
	FOREIGN KEY (cc_number) REFERENCES CreditCard(cc_number),\
	FOREIGN KEY (vendor_id) REFERENCES Vendor(id),\
	PRIMARY KEY (id)\
	);	}