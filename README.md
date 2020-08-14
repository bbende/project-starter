# Project Starter

Shell application to quickly start a new spring-boot project using the following technologies:

* Spring Boot for the overall application framework
* Jersey for REST APIs
* Swagger and Swagger UI for REST API docs
* Spring Data with JPA provided by EclipseLink
* Flyway for database migrations
* Spring MVC with Thymeleaf
* Unpoly JS, Stimulus, and Bootstrap
* Test Containers

## Local Development

1. Build the whole project

        mvn clean package
        
    For quick development builds, use _-Pskip-all_ to disable checkstyle, java docs, attach sources, and tests.
 
2. From first terminal:
 
        cd project-starter-webapp
        mvn spring-boot:run -Dspring-boot.run.profiles=dev

    This starts the Spring Boot application with an in-memory H2 database, and with dev-tools enabled so that 
    any changes to the Java code will automatically reload the application. 
    
3. From second terminal:

        cd project-starter-webapp
        gulp watch --envName dev --srcDir src/main/webapp --buildDir target/frontend --outDir target/classes/static

    The Gulp task starts a watch on all the frontend resources so that any changes to Javascript and CSS will 
    automatically be rebuilt and copied into the static resources location of the running application.

4. Open [http://localhost:8080/index.html](http://localhost:8080/index.html) in your browser.

## Databases

The default application jar/assembly bundles H2 and is configured to use file-based H2 in the default application.properties.

Additional database drivers can be bundled by activating Maven profiles:

    mvn clean package -Pmysql
    mvn clean package -Pmariadb
    mvn clean package -Ppostgres
    
Integration tests can be run against all supported databases by running the following command:

    mvn verify -Ptest-all-dbs
    
NOTE: This leverages Test Containers and requires a local Docker instance.

To run the integration tests against a single type of database, use the following command:

    mvn verify -Dspring.profiles.active=postgres

See the _project-starter-testcontainers_ module to find the various DataSource factories and their profile values.
        
## Docker

The _jib-maven-plugin_ is used to build and push docker images. Maven profiles are used to activate the plugin.

To use a local Docker daemon:

    mvn clean package -Pdocker-local

To push to a remote registry:

    mvn clean package -Pdocker-remote
    
NOTE: The _docker-remote_ profile will push to Docker Hub by default. 

This requires that you provide your Docker Hub credentials in _~/.m2/settings.xml_:

    <server>
        <id>docker.io</id>
        <username>YOUR_USERNAME</username>
        <password>YOUR_PASSWORD</password>
        <configuration>
          <email>YOUR_EMAIL</email>
        </configuration>
      </server>
      
The property _docker.image.path_ controls the image path used by jib.

This can be overridden on the command line:

    mvn clean package -Pdocker-remote -Ddocker.image.path=aws_account_id.dkr.ecr.region.amazonaws.com/my-app
    
Assuming your ECR credentials are configured properly.

See the [jib-maven-plugin](https://github.com/GoogleContainerTools/jib/tree/master/jib-maven-plugin) documentation for more information.     
