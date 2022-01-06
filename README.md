# User Manager
## asd_project1_group5

Advanced Software Development project - User manager

Application Server: Wildfly
Database: PostgreDB

The chosen application server is the Wildfly version 25.0.0.Final.
You have to download the server [here](https://www.wildfly.org/downloads/) and add a runtime configuration in your IDE. 

The application is reachable via [localhost:8080](https://localhost:8080/)

To create and start the database navigate to the "docker-compose.yaml" file and execute "docker-compose up -d"
The "docker-compose.yaml" file does include the default credentials.


###Troubleshooting
If the project doesn't build try:

goto: File|Settings|Build, Execution, Deployment|Build Tools|Maven|Runners

Check the first checkbox 'Delegate IDE Build/...'

