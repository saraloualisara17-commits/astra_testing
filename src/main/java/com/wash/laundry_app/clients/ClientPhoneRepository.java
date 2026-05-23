package com.wash.laundry_app.clients;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientPhoneRepository extends JpaRepository<ClientPhone, Long> {
    Optional<ClientPhone> findFirstByClientId(Long clientId);
}
