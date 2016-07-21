package com.richard;

import com.datastax.driver.core.*;
import com.datastax.driver.core.policies.DCAwareRoundRobinPolicy;
import com.datastax.driver.core.policies.DefaultRetryPolicy;
import com.datastax.driver.core.policies.TokenAwarePolicy;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.richard.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.embedded.undertow.UndertowBuilderCustomizer;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;

/**
 * Created using Intellij IDE
 * Created by rnkoaa on 7/20/16.
 */
@SpringBootApplication
public class EventProcessApplication {
    private static final Logger LOG = LoggerFactory.getLogger(EventProcessApplication.class);

    //private static Cluster cluster;

    @Autowired
    private Session session;

    public static void main(String[] args) {
        SpringApplication.run(EventProcessApplication.class, args);
    }

/*
    @Autowired
    SimpleUserRepository repository;*/

    @Bean
    public CommandLineRunner run() {
        return args -> {
           /* // Connect to the cluster and keyspace "demo"
            cluster = Cluster
                    .builder()
                    .addContactPoint("127.0.0.1")
                    .withRetryPolicy(DefaultRetryPolicy.INSTANCE)
                    *//*.withLoadBalancingPolicy(
                            new TokenAwarePolicy(new DCAwareRoundRobinPolicy()))*//*
                    .build();
            session = cluster.connect("example");*/

            selectStatement();
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
    public UndertowEmbeddedServletContainerFactory embeddedServletContainerFactory() {
        UndertowEmbeddedServletContainerFactory factory =
                new UndertowEmbeddedServletContainerFactory();

        factory.addBuilderCustomizers(new UndertowBuilderCustomizer() {
            @Override
            public void customize(ServerProperties.Undertow.Builder builder) {

            }
            *//*@Override
            public void customize() {
                builder.addHttpListener(8080, "0.0.0.0");
            }*//*
        });

        return factory;
    }*/

}
