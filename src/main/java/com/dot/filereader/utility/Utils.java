package com.dot.filereader.utility;

import com.dot.filereader.userAccess.model.UserAccessLog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static com.dot.filereader.utility.Constants.DATE_PATTERN;

public class Utils {

    public static LocalDateTime getEndDateTime(String startDate, String duration) {
        //returns hourly increment to start date time based on specified duration
        return LocalDateTime.parse(startDate, DateTimeFormatter.ofPattern(DATE_PATTERN)).plusHours(getDurationInHours(duration));
    }

    public static int getDurationInHours(String duration) {
        //returns 1 is duration is specified as hourly and 24 if duration is specified as daily
        if(duration.isEmpty()) return 0;
        return (Objects.equals(duration, "hourly")) ? 1 : 24;
    }

    public static String formatStartDateTime(String startDate) {
        //formats input start date
        return startDate.replace(".", " ");
    }

    public static List<UserAccessLog> extractFileContents(Scanner scanner) {

        List<UserAccessLog> userAccessLogs = new ArrayList<>();

        while (scanner.hasNextLine()) { //loops through every line of file parser
            //get current line of file content and initialises it as a string data type
            String data = scanner.nextLine();
            //convert current line of file content into an array using (|) symbol which is used as a separator in user logs file
            String[] splitValueArray = data.split("\\|");
            String date = splitValueArray[0].substring(0, 19); //extract user access log date for current line
            String ip = splitValueArray[1]; //extract user access log ip address for current line
            String request = splitValueArray[2]; //extract user access log ip address for current line
            String status = splitValueArray[3]; //extract user access log request for current line
            String userAgent = splitValueArray[4]; //extract user access log user agent for current line
            LocalDateTime localDateTime = LocalDateTime.parse((date), DateTimeFormatter.ofPattern(DATE_PATTERN)); //convert log date to local date time

            //use current line values to create a user access log POJO ready for database persistence
            UserAccessLog userAccessLog = UserAccessLog.builder()
                    .ip(ip)
                    .request(request)
                    .userAgent(userAgent)
                    .status(status)
                    .date(localDateTime)
                    .build();

            userAccessLogs.add(userAccessLog);
        }

        return userAccessLogs;
    }

}
