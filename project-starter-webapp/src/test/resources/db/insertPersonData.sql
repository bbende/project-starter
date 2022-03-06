
-- Test data for person table

INSERT INTO person(ID, FIRST_NAME, LAST_NAME, AGE)
  VALUES ('P1', 'Alice', 'Smith', '25' );

INSERT INTO person(ID, FIRST_NAME, LAST_NAME, AGE)
  VALUES ('P2', 'Bob', 'Jones', '30');

-- Test data for event table

INSERT INTO event(ID, PERSON_ID, TITLE, DESCRIPTION)
  VALUES ('E1', 'P1', 'Event #1', 'This is event #1.');

INSERT INTO event(ID, PERSON_ID, TITLE, DESCRIPTION)
  VALUES ('E2', 'P1', 'Event #2', 'This is event #2.');