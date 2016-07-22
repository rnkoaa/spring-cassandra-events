package com.richard;

import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created on 7/22/2016.
 */
@Slf4j
@Component
public class RequestEventCSVParser {

    private String fileLocation = "%s/data/kuiper/response_event.tbl.csv";

    @Autowired
    public RequestEventCSVParser(@Value("${user.home}") String userHomeLocation) {
        fileLocation = String.format(fileLocation, userHomeLocation);
    }

    void processResponseEvents() throws IOException {
        CSVReader reader = new CSVReader(new FileReader(fileLocation));
        List<String[]> failedRequests = new ArrayList<>();
        List<String[]> allRawItems = new ArrayList<>();
        List<String[]> successfulRequests = new ArrayList<>();
        List<String[]> otherResponses = new ArrayList<>();
        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
          /*  //id,datetime,resource,ip,tmdb_id,response_code,success_failure
            if (!nextLine[2].isEmpty() && nextLine[2].equals("persons")) {
                nextLine[2] = "person";
                filterByResponseCode(failedRequests, successfulRequests, otherResponses, nextLine);
                //strings.add(nextLine);
            } else {
                nextLine[2] = "movie";
                filterByResponseCode(failedRequests, successfulRequests, otherResponses, nextLine);
            }*/
            allRawItems.add(nextLine);


        }

/*        Set<String> otherResponseCodes = otherResponses
                .stream()
                .map(e -> e[5])
                .collect(Collectors.toSet());

        System.out.println("There were " + successfulRequests.size() + " Successful Requests");
        System.out.println("There were " + failedRequests.size() + " Failed Requests");
        System.out.println("There were " + otherResponses.size() + " other Response");

        Map<String, Set<String>> bucketedResponses = new HashMap<>();
        otherResponseCodes.forEach(responseCode -> {
            Set<String> responseItems = otherResponses.stream()
                                                      .filter(response -> response[5].equals(responseCode))
                                                      .map(res -> res[4])
                                                      .collect(Collectors.toSet());
            bucketedResponses.put(responseCode, responseItems);
        });*/

       /* Set<String> successItems = successfulRequests
                .stream()
                .map(req -> req[4])
                .collect(Collectors.toSet());

        Set<String> failureItems = failedRequests
                .stream()
                .map(req -> req[4])
                .collect(Collectors.toSet());

        bucketedResponses.put("200", successItems);
        bucketedResponses.put("404", failureItems);
        assert (bucketedResponses.size() == 6);
        bucketedResponses
                .forEach((k, v) ->
                        System.out.println("Code: => " + k + ", Number of Items: => " + v.size()));

        //remove any item that was retried successfully.
        dedupeResponseEvents("502", bucketedResponses, successItems, failureItems);
        dedupeResponseEvents("503", bucketedResponses, successItems, failureItems);
        dedupeResponseEvents("504", bucketedResponses, successItems, failureItems);
        dedupeResponseEvents("429", bucketedResponses, successItems, failureItems);

        System.out.println("============== After deduping ========================");
        assert (bucketedResponses.size() == 6);
        bucketedResponses
                .forEach((k, v) ->
                        System.out.println("Code: => " + k + ", Number of Items: => " + v.size()));*/
/*
        Set<String> allUniqueItems = bucketedResponses
                .entrySet()
                .stream()
                .flatMap(new Function<Map.Entry<String, Set<String>>, Stream<String>>() {
                    @Override
                    public Stream<String> apply(Map.Entry<String, Set<String>> stringSetEntry) {
                        return stringSetEntry.getValue().stream();
                    }
                }).collect(Collectors.toSet());*/

        List<String[]> dedupedItems = dedupeRawTypes(allRawItems, new HashSet<>());
    }

    private List<String[]> dedupeRawTypes(List<String[]> allRawItems, Set<String> allUniqueItems) {
        Map<String, List<String[]>> groupingByIds = allRawItems.stream()
                                                               .collect(Collectors.groupingBy(strings -> strings[4]));

        System.out.println(groupingByIds.size());
/*
        groupingByIds.entrySet().stream().filter(stringListEntry -> stringListEntry.getValue().size() > 1)
        .collect(Collectors.toMap());*/

        Map<String, List<String[]>> duplicates = new HashMap<>();
        groupingByIds.entrySet()
                     .stream()
                     .filter(stringListEntry -> stringListEntry.getValue().size() > 1)
                     .forEach(stringListEntry ->
                             duplicates.put(stringListEntry.getKey(), stringListEntry.getValue()
                             )
                     );
        System.out.println("Duplicates Size: " + duplicates.size());

        Map<String, List<String[]>> duplicatedItems = new HashMap<>(duplicates.size());

        for (Map.Entry<String, List<String[]>> stringListEntry : duplicates.entrySet()) {
            List<String[]> items = stringListEntry.getValue();
            String[] successful = items.stream()
                                       .filter(e -> Objects.equals(e[5], "200"))
                                       .findAny()
                                       .orElse(new String[]{});
            if(successful.length > 0){
                duplicatedItems.put(stringListEntry.getKey(), Collections.singletonList(successful));
            }else{
                successful = items.stream()
                                 .filter(e -> Objects.equals(e[5], "404"))
                                 .findAny()
                                 .orElse(new String[]{});
                if(successful.length > 0){
                    duplicatedItems.put(stringListEntry.getKey(), Collections.singletonList(successful));
                }else{
                    successful = items.stream().findAny().orElse(new String[]{});
                    duplicatedItems.put(stringListEntry.getKey(), Collections.singletonList(successful));
                }
            }
        }

        System.out.println("Filtered Duplicated Items: " + duplicatedItems.size());

        for (Map.Entry<String, List<String[]>> stringListEntry : duplicatedItems.entrySet()) {

            groupingByIds.remove(stringListEntry.getKey());
            groupingByIds.put(stringListEntry.getKey(), stringListEntry.getValue());
        }

        final Iterator<String[]> iterator = allRawItems.iterator();


        return null;
    }

    private void filterByResponseCode(List<String[]> failedRequests, List<String[]> successfulRequests, List<String[]> otherResponses, String[] nextLine) {
        switch (nextLine[5]) {
            case "404":
                failedRequests.add(nextLine);
                break;
            case "200":
                successfulRequests.add(nextLine);
                break;
            default:
                otherResponses.add(nextLine);
                break;
        }
    }

    private void dedupeResponseEvents(String responseCode, Map<String, Set<String>> bucketedResponses, Set<String> successItems, Set<String> failureItems) {
        Set<String> itemIds = bucketedResponses.get(responseCode);
        Iterator<String> iterator = itemIds.iterator();
        while (iterator.hasNext()) {
            String itemId = iterator.next();
            if (!StringUtils.isEmpty(itemId)) {
                if (successItems.contains(itemId) || failureItems.contains(itemId))
                    iterator.remove();
            }
        }

        bucketedResponses.put(responseCode, itemIds);
    }
}
