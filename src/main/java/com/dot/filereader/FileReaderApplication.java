package com.dot.filereader;

import com.dot.filereader.service.AccessLimitService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import static com.dot.filereader.utility.Constants.*;

@SpringBootApplication
@RequiredArgsConstructor
public class FileReaderApplication implements CommandLineRunner {
    private final AccessLimitService accessLimitService;

    public static void main(String[] args) {
        SpringApplication.run(FileReaderApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Path assessFilePath; //file path to user access log file
        String startDate; //date of "yyyy-MM-dd.HH:mm:ss" format
        String duration; //duration, can be "hourly", "daily"
        int limit; //limit of request based on duration. 200 per hour and 500 per day
        HashMap<String, String> arguments = accessLimitService.extractCommandLineArguments(args);

        if (arguments.isEmpty()) { //validate command line arguments are passed
            throw new RuntimeException("START, DURATION, LIMIT and ACCESS FILE PATH are not defined");
        }

        assessFilePath = Paths.get(arguments.get(ACCESS_FILE));
        startDate = arguments.get(START);
        duration = arguments.get(DURATION);
        limit = Integer.parseInt(arguments.get(LIMIT));

        accessLimitService.readFileAndLoadToDB(assessFilePath);
        accessLimitService.checkUsersAccessLimitExceededFromDB(startDate, limit, duration);
    }
}
//args = new String[]{"--accessFile=/Users/chinonsoharrison/IdeaProjects/fileReader/src/test/resources/test_user_access.txt", "--start=2022-01-01 00:00:01", "--duration=hourly", "--limit=3"};