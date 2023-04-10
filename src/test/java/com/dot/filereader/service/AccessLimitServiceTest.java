package com.dot.filereader.service;

import com.dot.filereader.blockedIP.repository.BlockedIpRepository;
import com.dot.filereader.userAccess.repository.UserAccessLogRepository;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AccessLimitServiceTest {

    @Mock
    UserAccessLogRepository userAccessLogRepository;

    @Mock
    BlockedIpRepository blockedIpRepository;
    @InjectMocks
    AccessLimitImplementation accessLimitImplementation;

    @Test
    void extractCommandLineArgumentsTest() {
        String[] args = new String[0];
        assertThatThrownBy(() -> accessLimitImplementation.extractCommandLineArguments(args)).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Passed less arguments than required. Please complete required arguments and try again");
    }

    @Test
    void readFileAndLoadToDBTest() throws URISyntaxException {
        Path assessFilePath = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("test_user_access.txt")).toURI());
        assertNotNull(assessFilePath);
        Boolean result = accessLimitImplementation.readFileAndLoadToDB(assessFilePath);
        assertEquals(result, true);
    }

    @Test
    void checkUsersAccessLimitExceededFromDBTest() {
        JSONObject json = new JSONObject();
        json.put("ip", "192.168.169.194");
        json.put("counter", BigInteger.valueOf(5));
        JSONObject json2 = new JSONObject();
        json2.put("ip", "192.168.19.70");
        json2.put("counter", BigInteger.valueOf(6));

        List<JSONObject> repoList = List.of(json, json2);

        int limit = 2;
        String start = "2022-01-01 00:07:57";
        String duration = "hourly";
        when(userAccessLogRepository.findAllByUserAccessLogIpBetweenStartDateAndEndDate(any(LocalDateTime.class), any(LocalDateTime.class), anyInt())).thenReturn(repoList);
        List<JSONObject> result = accessLimitImplementation.checkUsersAccessLimitExceededFromDB(start, limit, duration);
        assertEquals(result.size(), repoList.size());
    }

}
