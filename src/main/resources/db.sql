create database book141j2ee;

create table books(
    id int primary key auto_increment,
    name varchar(150),
    date varchar(100),
    price double

);
drop table books;
insert into books(name,date,price) values ('《算法导论》','2006-9',85.00);
insert into books(name,date,price) values ('《UNIX编程艺术》','2006-2',46.00);
insert into books(name,date,price) values ('《编程》','2006-9',55.00);
insert into books(name,date,price) values ('《代码大全》','2010-9',56.00);

select * from books;

drop table cust;

create table cust(
  id int primary key auto_increment,
  name varchar(150),
  pwd varchar(150)
);
insert into cust(name, pwd) values ('a','d7afde3e7059cd0a0fe09eec4b0008cd');
insert into cust(name, pwd) values ('b','d7afde3e7059cd0a0fe09eec4b0008cd');
select * from cust;


create table user(
    id int primary key auto_increment,
    name varchar(150),
    pwd varchar(150)
);
insert into user(name, pwd) values ('a','d7afde3e7059cd0a0fe09eec4b0008cd');
select * from user where name='b';

create table test1(
    id int primary key auto_increment,
    name varchar(150),
    sex varchar(4),
    age int
);
insert into test1(name, sex,age) values ('a','男',19);
insert into test1(name, sex,age) values ('a','女',19);
insert into test1(name, sex,age) values ('b','男',19);
insert into test1(name, sex,age) values ('c','男',19);
insert into test1(name, sex,age) values ('d','女',20);
insert into test1(name, sex,age) values ('d','女',18);
select * from test1;

select * from test1;

select * from test1 where name in (
    select t.name from (select name,count(name) c from test1 group by name) t where c>1);

select name,count(name) c from test1 group by name;
select t.name from (select name,count(name) c from test1 group by name) t where c>1;
select * from test1 where name in (
    select t.name from (select name,count(name) c from test1 group by name) t where c>1);
select name,count(name) c from test1 group by name having c>1;