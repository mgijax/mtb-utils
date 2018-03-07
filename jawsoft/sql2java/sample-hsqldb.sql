



create table CUSTOMER  (
   CUSTOMER_NAME        VARCHAR(50),
   CUSTOMER_ID          IDENTITY,
   CUSTOMER_ADDRESS     VARCHAR(50)
);


-- just to test 2 fields as primary key...

create table SHIPPER  (
   SHIPPER_NAME    VARCHAR(50) NOT NULL,
   SHIPPER_COUNTRY    VARCHAR(50) NOT NULL,
   SHIPPER_DESCRIPTION VARCHAR(50),
   CONSTRAINT PK_SHIP PRIMARY KEY (SHIPPER_NAME, SHIPPER_COUNTRY)
);


create table MANUFACTURER  (
   MANUFACTURER_ID      IDENTITY,
   MANUFACTURER_NAME    VARCHAR(50),
   MANUFACTURER_DESCRIPTION VARCHAR(50)
);


create table PRODUCT  (
   PRODUCT_ID           IDENTITY,
   PRODUCT_PRICE        DECIMAL(10,2) NOT NULL,
   MANUFACTURER_ID      INTEGER,
   PRODUCT_NAME         VARCHAR(50),
   PRODUCT_DESCRIPTION  VARCHAR(50),
   PRODUCT_INSERTIONDATE DATETIME,
   CONSTRAINT FK_M2M FOREIGN KEY (MANUFACTURER_ID) REFERENCES MANUFACTURER (MANUFACTURER_ID)
);


create table DELIVERY  (
   CUSTOMER_ID          INTEGER,
   PRODUCT_ID           INTEGER,
   DELIVERY_DATE        DATETIME,
   CONSTRAINT FK_C2C FOREIGN KEY (CUSTOMER_ID) REFERENCES CUSTOMER (CUSTOMER_ID),
   CONSTRAINT FK_P2P FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCT (PRODUCT_ID)
);
