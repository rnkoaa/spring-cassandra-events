package com.richard;

import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    Set<String[]> processCSV() throws IOException {
        //read and skip first line
        CSVReader reader = new CSVReader(new FileReader(fileLocation));
        List<String[]> finalResult = new ArrayList<>();
        String[] line;
        while ((line = reader.readNext()) != null) {
            if (line[2].isEmpty())
                line[2] = "movie";
            else if (line[2].equals("persons"))
                line[2] = "person";
            finalResult.add(line);
        }

        //remove the header line
        finalResult.remove(0);
        Set<String[]> personsSuccessfulOrFailedReq = finalResult.stream()
                .filter(e -> Objects.equals(e[2], "person"))
                .filter(e -> Objects.equals(e[5], "404") || Objects.equals(e[5], "200"))
                .collect(Collectors.toSet());

        List<String[]> personsOtherReq = finalResult.stream()
                .filter(e -> Objects.equals(e[2], "person"))
                .filter(e -> !((e[5].equals("404")) || (e[5].equals("200"))))
                .collect(Collectors.toList());

        Set<String[]> moviesSuccessfulOrFailedReq = finalResult.stream()
                .filter(e -> e[2].equals("movie"))
                .filter(e -> Objects.equals(e[5], "404") || Objects.equals(e[5], "200"))
                .collect(Collectors.toSet());


        Set<String[]> moviesOtherReq = finalResult.stream()
                .filter(e -> e[2].equals("movie"))
                .filter(e -> !(e[5].equals("404") || (e[5].equals("200"))))
                .collect(Collectors.toSet());

        Iterator<String[]> otherMovieIterator = moviesOtherReq.iterator();
        Set<String> movieResourceIds = personsSuccessfulOrFailedReq.stream()
                .map(e -> e[4])
                .collect(Collectors.toSet());

        while (otherMovieIterator.hasNext()) {
            String[] itemIterator = otherMovieIterator.next();
            if (movieResourceIds.contains(itemIterator[4]))
                otherMovieIterator.remove();
        }

        Iterator<String[]> personsOtherIterator = personsOtherReq.iterator();
        Set<String> personResourceIds = personsSuccessfulOrFailedReq.stream()
                .map(e -> e[4])
                .collect(Collectors.toSet());

        while (personsOtherIterator.hasNext()) {
            String[] itemIterator = personsOtherIterator.next();
            if (personResourceIds.contains(itemIterator[4]))
                personsOtherIterator.remove();
        }

        Set<String[]> finalRequestResult =
                new HashSet<>(moviesSuccessfulOrFailedReq.size()
                        + personsSuccessfulOrFailedReq.size()
                        + ((personsOtherReq.size() > 0) ? personsOtherReq.size() : 0)
                        + ((moviesOtherReq.size() > 0) ? moviesOtherReq.size() : 0));

        finalRequestResult.addAll(personsSuccessfulOrFailedReq);
        finalRequestResult.addAll(moviesSuccessfulOrFailedReq);

        if (moviesOtherReq.size() > 0)
            finalRequestResult.addAll(moviesOtherReq);

        if (personsOtherReq.size() > 0)
            finalRequestResult.addAll(personsOtherReq);

        return finalRequestResult;
    }


}
