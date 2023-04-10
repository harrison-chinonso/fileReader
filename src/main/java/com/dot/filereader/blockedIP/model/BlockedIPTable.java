package com.dot.filereader.blockedIP.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "BLOCKED_IP_TABLE")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BlockedIPTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private  String ip;
    private BigInteger requestNumber;
    private String comment ;
}
