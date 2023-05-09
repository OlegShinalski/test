Helmes test task

Used technologies and tools
- Java 17
- Maven
- SpringBoot
- Spring Data JPA
- in memory H2 database
- Lombok
- Guava
- Some other small spring embedded things

Build project : mvn clean install

Run project : mvn spring-boot:run

URL is http://localhost:8080/customer
Each GET request creates new customer, consecutive POST requests are update last customer


Remarks : 
1. Database dump is 2 files, which initially loaded : schema.sql and data.sql
   During execution H2 console is available on URL http://localhost:8080/h2-console (JDBC:ULR=jdbc:h2:mem:testdb USER=sa PASSWORD=)
   
   Current database content could be queried :
   SELECT c.*, s.*
      FROM customer c
      JOIN customer_sector cs ON cs.customer_id=c.id
      JOIN sector s ON s.id=cs.sector_id
      ORDER BY c.name, s.name

2. For Sector loading form database used 2 different approaches :
   1. Load all SECTOR in one query, then create hierarchical structure on Java side (Emulation of ORACLE hierarchical query).
      Pros - only one query.
      Cons - applicable for not large number of records. This feature is set by default.
   2. Load only top=level SECTOR(s), then featch others using Hibernate lazy-loading.
      Cons - more queries (number of rows+1). Switch by property sections.one_query=false in application.properties file. 
 
