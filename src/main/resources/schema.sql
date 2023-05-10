create table SECTOR (
    ID          int not null AUTO_INCREMENT,
    NAME        varchar(100) not null,
    PARENT_ID   int,

    PRIMARY KEY ( ID )
);

alter table SECTOR add foreign key (PARENT_ID)  references SECTOR(ID);

create table CUSTOMER (
    ID          int not null AUTO_INCREMENT,
    NAME        varchar(100) not null,
    AGREE       number(1) not null default 0,

    PRIMARY KEY ( ID )
);

create table CUSTOMER_SECTOR (
    CUSTOMER_ID int not null,
    SECTOR_ID   int not null,
    PRIMARY KEY ( CUSTOMER_ID, SECTOR_ID )
);

alter table CUSTOMER_SECTOR add foreign key (CUSTOMER_ID)  references CUSTOMER(ID);
alter table CUSTOMER_SECTOR add foreign key (SECTOR_ID)    references SECTOR(ID);

create sequence CUSTOMER_SEQ
    START WITH 1
    INCREMENT BY 50;
