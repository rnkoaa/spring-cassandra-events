package com.richard;

import com.datastax.driver.core.Cluster;
import com.google.common.collect.Lists;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StopWatch;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created using Intellij IDE
 * Created by rnkoaa on 7/24/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = EventProcessApplication.class)
public class RequestEventCSVParserTests {

    @Autowired
    RequestEventCSVParser requestEventCSVParser;

    @Autowired
    RequestEventBulkLoader requestEventBulkLoader;

    @Autowired
    Cluster cluster;

    @Ignore
    @Test
    public void testContextLoads() throws IOException {
        StopWatch stopWatch = new StopWatch("testContextLoads");
        stopWatch.start();
        Set<String[]> requestEvents = requestEventCSVParser.processCSV();
        assert (requestEvents.size() > 0);
        stopWatch.stop();
        System.out.println("Total Time taken: " + stopWatch.getTotalTimeMillis());
    }


    @Ignore
    @Test
    public void testLoadToCassandra() throws IOException {
        Set<String[]> requestEventCSV = requestEventCSVParser.processCSV();

        List<String[]> subLists = Lists.newArrayList(requestEventCSV).subList(0, 20);

        assert (subLists.size() > 0);
        System.out.println(subLists.size());

        List<RequestEvent> requestEvents = mapToRequestEvents(subLists);

        requestEvents.forEach(System.out::println);

        requestEventBulkLoader.bulkLoadData(requestEvents);
    }

    @Test
    public void testLoadAllToCassandra() throws IOException {
        StopWatch stopWatch = new StopWatch("testContextLoads");
        stopWatch.start();
        Set<String[]> requestEventCSV = requestEventCSVParser.processCSV();
        List<String[]> subLists = Lists.newArrayList(requestEventCSV);
        List<RequestEvent> requestEvents = mapToRequestEvents(subLists);
        requestEventBulkLoader.bulkLoadData(requestEvents);
        stopWatch.stop();
        System.out.println("Total Time taken: " + stopWatch.getTotalTimeSeconds());
    }

    private List<RequestEvent> mapToRequestEvents(List<String[]> subLists) {
        return subLists.stream()
                .map(e -> RequestEvent.builder()
                        .eventTime(LocalDateTime.parse(e[1]))
                        .id(UUID.fromString(e[0]))
                        .resourceId(Integer.valueOf(e[4]))
                        .responseCode(Integer.valueOf(e[5]))
                        .serverIp(e[3])
                        .resourceType(e[2])
                        .build()
                )
                .collect(Collectors.toList());
    }
}
