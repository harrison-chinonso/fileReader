package com.dot.filereader.service;

import org.json.simple.JSONObject;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;

public interface AccessLimitService {
       boolean readFileAndLoadToDB(Path filePath);

       HashMap<String,String> extractCommandLineArguments(String[] args);

       List<JSONObject> checkUsersAccessLimitExceededFromDB(String start, int limit, String duration);
}
