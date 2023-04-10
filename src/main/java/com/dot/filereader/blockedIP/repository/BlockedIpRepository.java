package com.dot.filereader.blockedIP.repository;

import com.dot.filereader.blockedIP.model.BlockedIPTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockedIpRepository extends JpaRepository<BlockedIPTable, Long> {

}
