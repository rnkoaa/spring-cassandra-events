package com.richard.context;

import com.datastax.driver.core.*;
import com.datastax.driver.core.policies.DefaultRetryPolicy;
import com.datastax.driver.extras.codecs.jdk8.ZonedDateTimeCodec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * Created using Intellij IDE
 * Created by rnkoaa on 7/20/16.
 */
@Slf4j
@Configuration
public class CassandraConfig implements DisposableBean {

    @Autowired
    Environment env;

    private Cluster cluster;
    private Session session;

    @Bean
    public Cluster cluster() {
        cluster = Cluster
                .builder()
                .addContactPoint(env.getRequiredProperty("spring.data.cassandra.contact-points"))
                .withRetryPolicy(DefaultRetryPolicy.INSTANCE)
                //.withCodecRegistry(new CodecRegistry().register(new LocalDateTimeCodec()))
                    /*.withLoadBalancingPolicy(
                            new TokenAwarePolicy(new DCAwareRoundRobinPolicy()))*/
                .build();

        // Due to internal implementation details, you have to retrieve the tuple type from a live cluster:
       /* TupleType tupleType = cluster.getMetadata()
                .newTupleType(DataType.timestamp(), DataType.varchar());
        cluster.getConfiguration().getCodecRegistry()
                .register(new ZonedDateTimeCodec(tupleType));*/

        CodecRegistry codecRegistry = cluster.getConfiguration().getCodecRegistry();
        codecRegistry.register(new LocalDateTimeCodec());

        return cluster;
    }

    @Bean
    public Session session(Cluster cluster) {
        session = cluster.connect(env.getRequiredProperty("spring.data.cassandra.keyspace-name"));
        /*// Due to internal implementation details, you have to retrieve the tuple type from a live cluster:
        TupleType tupleType = cluster.getMetadata()
                .newTupleType(DataType.timestamp(), DataType.varchar());
        cluster.getConfiguration().getCodecRegistry()
                .register(new ZonedDateTimeCodec(tupleType));*/

        return session;
    }

    @Override
    public void destroy() throws Exception {
        log.info("Closing Cluster and session");
        session.close();
        cluster.close();
        log.info("Is Cluster Closed.: {}", cluster.isClosed());
    }
}
