# Steps to follow to run the project

### Docker database setup
1. Create an account on https://container-registry.oracle.com
2. Accept the terms and conditions here https://container-registry.oracle.com/ords/ocr/ba/database/enterprise
3. Click on your username and create a new access token (copy it somewhere safe)
4. In a terminal run `docker login container-registry.oracle.com`
5. Type your email and access token when asked for username and password
6. Open a terminal in the project folder and run `docker-compose up`
7. It will take a while to download the image and start the database
8. You should now be able to start/stop the database via Docker and connect to it using the SYSTEM user and the password provided inside 
   the docker-compose.yml file

### (Optional) IntelliJ database setup
1. Add a new datasource in IntelliJ
2. Use connection type `Service Name` with service as `ORCLCDB`
3. Username = `system` and password is the password provided in the docker-compose.yml file
4. Download the Oracle Driver and test the connection

### Create users
1. Open the `create-user-for-app.sql` file
2. Execute the code that prints all PDBs and connect to the only one available
3. Execute the code that creates the users
4. (Optional) You can connect to these schemas in IntelliJ similarly as the SYSTEM user, but replace the service name with the PDB name

### Create local and global schemas
1. Open ~~`src/main/resources/db`~~ `db` and you will find the SQL scripts to create the schemas
2. Execute the scripts in the order of the versions, and it should be good to go

[//]: # (### Run the application)

[//]: # (1. Before running the Spring Boot application, make sure you add these VM options:)

[//]: # (   1. `-Dspring.oltp.datasource.password=<the same password you used to create the user>`)

[//]: # (   2. `-Dspring.olap.datasource.password=<the same password you used to create the user>`)

[//]: # (   3. `-Dproject.jwt.secret-key=<use a webiste like https://jwtsecrets.com to generate a 256 bit secret key>`)