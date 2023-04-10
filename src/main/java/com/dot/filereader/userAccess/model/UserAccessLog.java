package com.dot.filereader.userAccess.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "USER_ACCESS_LOG")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class UserAccessLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime date;
    private String ip;
    private String request;
    private String status;
    private String userAgent;

}
