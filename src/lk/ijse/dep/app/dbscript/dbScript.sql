CREATE DATABASE esoftplaceorder;

use placeorder;

CREATE TABLE Customer(
id VARCHAR(10),
name VARCHAR(10) NOT NULL,
address VARCHAR(100) NOT NULL,
CONSTRAINT PRIMARY KEY(id)
);

CREATE TABLE Item(
code varchar(10),
description varchar(10) NOT NULL,
unitprice decimal(10,2) NOT NULL,
qtyOnHand int not null,
constraint primary key(code)
);

CREATE TABLE `Order`(
id varchar(10),
date date,
customerId varchar(10),
constraint primary key(id),
constraint foreign key(customerId) references Customer(id),

);

create table OrderDetail(
orderId varchar(10),
code varchar (10),
qty int (5),
unitPrice decimal (6,2)
constraint primary key(orderId,code),
constraint foreign key(orderId) references "Order"(id),
constraint foreign key (code) references Item(code),
);



