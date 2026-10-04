========================================================================================================================
Starting points:
    1) To run PQ-Backend you should create a new in the Oracle database:
        GRANT DBA TO gnosis;
        
        GRANT CREATE SESSION TO gnosis;
        GRANT CREATE TABLE TO gnosis;
        GRANT CREATE VIEW TO gnosis;
        
        ALTER USER gnosis ACCOUNT UNLOCK;
        ALTER USER gnosis TEMPORARY TABLESPACE temp;
        ALTER USER gnosis PROFILE DEFAULT;
        GRANT CREATE SESSION TO gnosis;
    2) Add the following properties in resources -> environments -> local -> credentials.properties
        #Database
        spring.datasource.url=jdbc:oracle:thin:@//localhost:1521/XEPDB1
        spring.datasource.username=gnosis
        
        #Email
        spring.mail.username=play.quiz.10@gmail.com
        spring.mail.password=otlcafqtfcevwpks
        
        application.security.jwt.token.prefix=Bearer
        application.security.jwt.secret=U7jWVCe1rW2tFnwBoP02RZAGUXKubTVmU8Jo44xHIBM=

For integration tests, user TEST_SCHEMA should be created:
    CREATE USER TEST_SCHEMA IDENTIFIED BY testpassword;
    GRANT DBA TO TEST_SCHEMA;

    GRANT CREATE SESSION TO TEST_SCHEMA;
    GRANT CREATE TABLE TO TEST_SCHEMA;
    GRANT CREATE VIEW TO TEST_SCHEMA;
    
    ALTER USER TEST_SCHEMA ACCOUNT UNLOCK;
    ALTER USER TEST_SCHEMA TEMPORARY TABLESPACE temp;
    ALTER USER TEST_SCHEMA PROFILE DEFAULT;
    GRANT CREATE SESSION TO TEST_SCHEMA;

NOTE!
The test database can be populated by changing the database configuration in migrations/environment/development.properties.



========================================================================================================================
#Commands
- mvn clean install -> clean project and rebuild it
- mvn migration:new -Dmigration.description=name_of_the_migration
- mvn migration:up
- mvn migration:down -Dmigration.down.steps=Number_of_migrations_to_rollback (from Run Anything)
- mvn migration:status
- mvn clean verify sonar:sonar -Dsonar.projectKey=PlayQuiz -Dsonar.host.url=http://localhost:9000 -Dsonar.login=<sonar_token>
- mvn jacoco:report (target->site->index.html)

- git rm -r --cached . -> remove git cached files
