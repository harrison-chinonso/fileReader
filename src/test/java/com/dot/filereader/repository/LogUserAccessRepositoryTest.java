package com.dot.filereader.repository;

import com.dot.filereader.userAccess.model.UserAccessLog;
import com.dot.filereader.userAccess.repository.UserAccessLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

//user_access
@Slf4j
@SpringBootTest(args = {"--accessFile=/Users/chinonsoharrison/IdeaProjects/fileReader//src/test/resources/test_user_access.txt", "--start=2020-01-01.00:00:01", "--duration=hourly", "--limit=1"})
@TestPropertySource(locations = "classpath:application-test.yaml")
class LogUserAccessRepositoryTest {

    @Autowired
    private UserAccessLogRepository userAccessLogRepository;


    @BeforeEach
    void setUp() {
        userAccessLogRepository.deleteAll();
        UserAccessLog entity = new UserAccessLog(1L, LocalDateTime.parse("2022-01-04T09:26:39"), "192.168.1.1", "GET", "200", "User");
        UserAccessLog entity2 = new UserAccessLog(2L, LocalDateTime.parse("2022-01-07T09:27:40"), "192.168.1.1", "GET", "200", "User");
        UserAccessLog entity3 = new UserAccessLog(3L, LocalDateTime.parse("2022-01-07T07:28:42"), "192.168.1.2", "GET", "200", "User");
        UserAccessLog entity4 = new UserAccessLog(4L, LocalDateTime.parse("2022-01-08T08:31:49"), "192.168.1.1", "GET", "200", "User");

        userAccessLogRepository.saveAll(List.of(entity,entity2,entity3,entity4));
    }

    LocalDateTime start = LocalDateTime.parse("2022-01-04T07:50:55");
    LocalDateTime end = LocalDateTime.parse("2022-01-08T08:53:55");


    @Test
    void findAllByUserAccessLogIpBetweenStartDateAndEndDateTest() {
        List<JSONObject> result = userAccessLogRepository.findAllByUserAccessLogIpBetweenStartDateAndEndDate(start, end, 1);

        assert (result.size() == 1);
        assertNotNull(result.get(0).get("ip"));
        assert (result.get(0).get("ip").equals("192.168.1.1"));
    }
}
