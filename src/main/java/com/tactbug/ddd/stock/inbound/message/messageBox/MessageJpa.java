package com.tactbug.ddd.stock.inbound.message.messageBox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageJpa extends JpaRepository<Message, Long> {
}
