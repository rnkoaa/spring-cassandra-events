package com.richard;

import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

//import io.undertow.Undertow;

/**
 * Created using Intellij IDE
 * Created by rnkoaa on 7/20/16.
 */
@SpringBootApplication
public class EventProcessApplication {
    private static final Logger LOG = LoggerFactory.getLogger(EventProcessApplication.class);

    @Autowired
    private Session session;

    public static void main(String[] args) {
        SpringApplication.run(EventProcessApplication.class, args);
    }

    @Bean
    public CommandLineRunner run() {
       // System.setProperty("spring.profiles.active", "docker_machine");
        return args -> {
            //selectStatement();
            //saveBoundStatement();


            //cluster.close();

        };
    }

    private void selectStatement() {
        Statement select = QueryBuilder.select().all().from("example", "users")
                .where(eq("user_id", 42));
        ResultSet results = session.execute(select);
        for (Row row : results) {
            System.out.format("%s %d \n", row.getString("fname"),
                    row.getInt("user_id"));
        }
    }


    /** Update **/
    /*// Update the same user with a new age
    Statement update = QueryBuilder.update("demo", "users")
            .with(QueryBuilder.set("age", 36))
            .where((QueryBuilder.eq("lastname", "Jones")));
                        session.execute(update);
// Select and show the change
    select = QueryBuilder.select().all().from("demo", "users")
				.where(eq("lastname", "Jones"));
    results = session.execute(select);
		for (Row row : results) {
        System.out.format("%s %d \n", row.getString("firstname"),
                row.getInt("age"));*/

    /**
     Delete
     */

/*    // Delete the user from the users table
    Statement delete = QueryBuilder.delete().from("users")
            .where(QueryBuilder.eq("lastname", "Jones"));
    results = session.execute(delete);
    // Show that the user is gone
    select = QueryBuilder.select().all().from("demo", "users");
    results = session.execute(select);
		for (Row row : results) {
        System.out.format("%s %d %s %s %s\n", row.getString("lastname"),
                row.getInt("age"), row.getString("city"),
                row.getString("email"), row.getString("firstname"));
    }*/

    private void saveBoundStatement() {
        // Insert one record into the users table
        session.execute("INSERT INTO users (uname, fname, lname, user_id) VALUES ('rnkoaa', 'richard', 'agyei', 42)");

        // Use select to get the user we just entered
        ResultSet results = session.execute("SELECT * FROM users WHERE user_id=42");
        for (Row row : results) {
            System.out.format("%s %d\n", row.getString("fname"), row.getInt("user_id"));
        }

        String stmt =  "INSERT INTO users (uname, fname, lname, user_id) VALUES (?,?,?,?);";
        PreparedStatement preparedStatement = session.prepare(stmt);
        BoundStatement boundStatement = new BoundStatement(preparedStatement);

        session.execute(boundStatement.bind("ricardomanet", "kwame", "Odame", 3));
    }

    /*@Bean
    public UndertowEmbeddedServletContainerFactory embeddedServletContainerFactory(@Value("${server.port}") int serverPort) {
        UndertowEmbeddedServletContainerFactory factory = new UndertowEmbeddedServletContainerFactory();
        factory.addBuilderCustomizers((UndertowBuilderCustomizer) builder -> {
            builder.addHttpListener(serverPort, "0.0.0.0");
        });
        return factory;
    }*/

}
