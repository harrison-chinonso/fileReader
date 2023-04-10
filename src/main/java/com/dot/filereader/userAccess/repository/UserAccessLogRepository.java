package com.dot.filereader.userAccess.repository;

import com.dot.filereader.userAccess.model.UserAccessLog;
import org.json.simple.JSONObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public  interface UserAccessLogRepository extends JpaRepository<UserAccessLog, Long> {

    @Query(value = "SELECT ip, COUNT(ip) as counter FROM USER_ACCESS_LOG where date between :startDate AND :endDate group by user_access_log.ip HAVING COUNT(ip) > :limit ",nativeQuery = true)
    List<JSONObject>findAllByUserAccessLogIpBetweenStartDateAndEndDate(LocalDateTime startDate, LocalDateTime endDate, int limit);
}
