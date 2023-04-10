package com.dot.filereader.service;

import com.dot.filereader.blockedIP.repository.BlockedIpRepository;
import com.dot.filereader.userAccess.repository.UserAccessLogRepository;
import com.dot.filereader.blockedIP.model.BlockedIPTable;
import com.dot.filereader.userAccess.model.UserAccessLog;
import com.dot.filereader.utility.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigInteger;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static com.dot.filereader.utility.Constants.DATE_PATTERN;

@Slf4j
@RequiredArgsConstructor
@Service
public class AccessLimitImplementation implements AccessLimitService {

    private final UserAccessLogRepository userAccessLogRepository;
    private final BlockedIpRepository blockedIpRepository;

    //Service logic for extracting user access logs data from input file and persisting it to a database
    @Override
    public boolean readFileAndLoadToDB(Path filePath) {
        try {
            File file = new File(String.valueOf(filePath)); //Get actual file using input filepath
            Scanner scanner = new Scanner(file); //use java utility class to parse file content into primitive data types
            List<UserAccessLog> userAccessLogs = Utils.extractFileContents(scanner); //logic to convert file content into a List of objects
            if (userAccessLogs.isEmpty()) { //If file has no content returns true and throw an exception
                throw new RuntimeException("unable to extract access logs from provided file");
            }

            userAccessLogRepository.saveAll(userAccessLogs); //Persist file content to database
            scanner.close(); //close utility for parsing
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("an error occurred while processing file");
        }
    }

    //Service logic for extracting command line arguments
    @Override
    public HashMap<String, String> extractCommandLineArguments(String... args) {
        HashMap<String, String> params = new HashMap<>();

        if (args == null || args.length < 4) { //validate all required command line arguments are passed
            throw new RuntimeException("Passed less arguments than required. Please complete required arguments and try again");
        }
        log.info("ARGUEMENTs :: {} ", (Object) args);
        for (String arg : args) { //iterate through array of command line arguments and instantiate parameters for log file reading
            String[] splitFromEqual = arg.split("="); //split argument name and value using = sign
            String key = splitFromEqual[0].substring(2); //initialise key as argument name
            String value = splitFromEqual[1]; //initialise value as argument value
            params.put(key, value); //hold key/value pair in a map object
        }
        return params;
    }

    //Service logic to checks if a specific IP address makes more than a certain number of requests
    // for a specified period or window of time.
    @Override
    public List<JSONObject> checkUsersAccessLimitExceededFromDB(String start, int limit, String duration) {
        String startDate = Utils.formatStartDateTime(start); //format start date
        LocalDateTime startDateTime = LocalDateTime.parse(startDate, DateTimeFormatter.ofPattern(DATE_PATTERN));
        LocalDateTime endDateTime = Utils.getEndDateTime(startDate, duration); //generate end date time based on provided duration
        int durationAllocated = Utils.getDurationInHours(duration);

        log.info("START {}, END {}", startDateTime, endDateTime);
        //fetch IP addresses that exceeded limit of requests based on duration specified and number of request they sent within specified duration
        List<JSONObject> requestsAtDuration = userAccessLogRepository.findAllByUserAccessLogIpBetweenStartDateAndEndDate(startDateTime, endDateTime, limit);

        if (!requestsAtDuration.isEmpty()) {
            requestsAtDuration.forEach(e -> { //loop through list of IPs that exceeded limit
                BigInteger count = (BigInteger) e.get("counter"); //get number of request an IP sent within duration

                String builder = new StringBuilder()
                        .append(count).append(" requests sent between ")
                        .append(startDateTime)
                        .append(" and ")
                        .append(endDateTime)
                        .append(" which is greater than limit of ")
                        .append(limit).toString();

                BlockedIPTable blockedIPTable = BlockedIPTable.builder()
                        .ip((String) e.get("ip"))
                        .comment(builder)
                        .requestNumber(count)
                        .build();
                log.warn("IP {} has been barred for making {} requests more than the set limit of {} within this duration {}", e.get("ip"), count, limit, durationAllocated);

                blockedIpRepository.save(blockedIPTable);
            });
        }
        return requestsAtDuration;
    }
}
