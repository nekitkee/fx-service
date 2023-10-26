package com.mintos.fxservice.repositories;

import com.mintos.fxservice.models.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
