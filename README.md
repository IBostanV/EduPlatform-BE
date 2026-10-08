========================================================================================================================
Starting points:
    1) To run PQ-Backend you need PostgreSQL 17, e.g. in Docker:
        docker run -d --name Postgres-PQ -p 5432:5432 -e POSTGRES_DB=playquiz -e POSTGRES_USER=gnosis -e POSTGRES_PASSWORD=<db_password> -v pq-postgres-data:/var/lib/postgresql/data --restart unless-stopped postgres:17
        docker exec Postgres-PQ psql -U gnosis -d playquiz -c "CREATE DATABASE playquiz_test"
    2) Add the following properties in resources -> environments -> local -> credentials.properties
        #Database
        spring.datasource.url=jdbc:postgresql://localhost:5432/playquiz
        spring.datasource.username=gnosis
        spring.datasource.password=<db_password>
        
        #Email
        spring.mail.username=play.quiz.10@gmail.com
        spring.mail.password=otlcafqtfcevwpks
        
        application.security.jwt.token.prefix=Bearer
        application.security.jwt.secret=U7jWVCe1rW2tFnwBoP02RZAGUXKubTVmU8Jo44xHIBM=

The test profile (application-test.yml) uses the playquiz_test database.

NOTE!
Database migrations are run by Flyway when the application starts (the test profile migrates playquiz_test the same way).
V1__baseline.sql is the schema as it was in Oracle when the project moved to PostgreSQL; the Oracle-era migrations
are in git history. A new migration goes in persistence/src/main/resources/db/migration as
V<yyyyMMddHHmmss>__name_of_the_migration.sql.
Applied migrations are never edited: Flyway checks their checksums. To undo one, write a new migration.



========================================================================================================================
#Commands
- mvn clean install -> clean project and rebuild it
- mvn -pl persistence flyway:info -Dflyway.password=<db_password> -> applied and pending migrations
- mvn -pl persistence flyway:migrate -Dflyway.password=<db_password> -> apply pending migrations without starting the app
- mvn -pl persistence flyway:repair -Dflyway.password=<db_password> -> clear a failed migration after fixing its script
  (another database: add -Dflyway.url=jdbc:postgresql://localhost:5432/playquiz_test; the password can also come from the FLYWAY_PASSWORD environment variable)
- mvn clean verify sonar:sonar -Dsonar.projectKey=PlayQuiz -Dsonar.host.url=http://localhost:9000 -Dsonar.login=<sonar_token>
- mvn jacoco:report (target->site->index.html)

- git rm -r --cached . -> remove git cached files
