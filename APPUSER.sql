--------------------------------------------------------
--  DDL for Table APPUSER
--------------------------------------------------------

  CREATE TABLE "SANDBOX"."APPUSER" 
   (	"ID" NUMBER, 
	"USERNAME" VARCHAR2(20 BYTE), 
	"PASSWORD" VARCHAR2(20 BYTE), 
	"DESCRIPTION" VARCHAR2(1024 BYTE)
   ) SEGMENT CREATION IMMEDIATE 
  PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 
 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  DDL for Index TABLE1_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "SANDBOX"."TABLE1_PK" ON "SANDBOX"."APPUSER" ("ID") 
  PCTFREE 10 INITRANS 2 MAXTRANS 255 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS" ;
--------------------------------------------------------
--  Constraints for Table APPUSER
--------------------------------------------------------

  ALTER TABLE "SANDBOX"."APPUSER" ADD CONSTRAINT "TABLE1_PK" PRIMARY KEY ("ID")
  USING INDEX PCTFREE 10 INITRANS 2 MAXTRANS 255 
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1
  BUFFER_POOL DEFAULT FLASH_CACHE DEFAULT CELL_FLASH_CACHE DEFAULT)
  TABLESPACE "USERS"  ENABLE;
  ALTER TABLE "SANDBOX"."APPUSER" MODIFY ("DESCRIPTION" NOT NULL ENABLE);
  ALTER TABLE "SANDBOX"."APPUSER" MODIFY ("PASSWORD" NOT NULL ENABLE);
  ALTER TABLE "SANDBOX"."APPUSER" MODIFY ("USERNAME" NOT NULL ENABLE);
  ALTER TABLE "SANDBOX"."APPUSER" MODIFY ("ID" NOT NULL ENABLE);
--------------------------------------------------------
--  DDL for Trigger APPUSER_CACHE_TRIGGER
--------------------------------------------------------

  CREATE OR REPLACE EDITIONABLE TRIGGER "SANDBOX"."APPUSER_CACHE_TRIGGER" 
AFTER DELETE OR INSERT OR UPDATE ON APPUSER 
FOR EACH ROW
DECLARE 
 bt_conn    utl_tcp.connection;
 message    varchar2(1024);
 retval     binary_integer;
BEGIN
  bt_conn := utl_tcp.open_connection(remote_host => 'localhost'
                                    ,remote_port => 8080
                                    ,tx_timeout => 1
                                    );
  message := REPLACE(
              REPLACE(
                'GET http://localhost:8080/cachekill?entity=%entity%&key=%key% HTTP/1.1', 
                '%entity%', 'USER'), 
                '%key%', coalesce(:new.ID, :old.ID)
            );

  retval := utl_tcp.write_line(bt_conn,message);
  retval := utl_tcp.write_line(bt_conn, '');
  utl_tcp.flush(bt_conn);
  utl_tcp.close_connection(bt_conn);
exception
 when others then
  utl_tcp.close_connection(bt_conn);
END;
/
ALTER TRIGGER "SANDBOX"."APPUSER_CACHE_TRIGGER" ENABLE;

