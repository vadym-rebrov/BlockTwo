<p style="font-size: 18px; text-align: justify;">
    Subject area: Movies and Cinema.<br/><br/>
    To run the program, PostgreSQL must be installed, and a database named movis_db must be created. Connection settings, including the port, are configured in the <code>src/main/resources/application.yaml</code> file. Upon application startup, a Liquibase script will execute to create the necessary tables and populate them with initial data.<br/><br/>
    A custom index, defined via CustomPostgreSQLDialect, is used to enable full-text search for movies by director name. To ensure data uniqueness, composite unique indexes are configured for the movie and director tables, while single unique indexes are used for genre and country. Additionally, CHECK CONSTRAINTS are created to ensure text fields are not empty and to validate that the rating attribute falls within the 0 to 10 range.<br/><br/>
</p>

### Access to PostgreSQL:
### - Host: localhost
### - Port: 5433
### - Username: postgres
### - Password: 1111
### - Database: movies_db

<p style="font-size: 18px; text-align: justify;">
The application provides RESTful APIs for "Movie" and "Director" entities, including full CRUD operations. <br/>
Key features for the Movie API include multi-criteria filtering with pagination, Excel report generation based on search results, and data import from JSON files.
</p>
