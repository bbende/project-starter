
CREATE TABLE PERSON (
    ID VARCHAR(50) NOT NULL,
    FIRST_NAME VARCHAR(100) NOT NULL,
    LAST_NAME VARCHAR(100) NOT NULL,
    AGE INT NOT NULL,
    CREATED TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UPDATED TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT PK__PERSON_ID PRIMARY KEY (ID)
);

CREATE TABLE EVENT (
    ID VARCHAR(50) NOT NULL,
    PERSON_ID VARCHAR(50) NOT NULL,
    TITLE VARCHAR(200) NOT NULL,
    DESCRIPTION TEXT NOT NULL,
    CREATED TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT PK__EVENT_ID PRIMARY KEY (ID),
    CONSTRAINT FK__EVENT_PERSON_ID FOREIGN KEY (PERSON_ID) REFERENCES PERSON(ID) ON DELETE CASCADE
);