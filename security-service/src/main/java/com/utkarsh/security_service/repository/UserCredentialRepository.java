package com.utkarsh.security_service.repository;

import com.utkarsh.security_service.entity.UserCredential;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCredentialRepository extends MongoRepository<UserCredential, String> {
    Optional<UserCredential> findByName(String username);
}
