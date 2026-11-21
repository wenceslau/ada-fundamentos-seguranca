package com.ada.incident.incident;

import com.ada.incident.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IncidentRepository extends JpaRepository<Incident, Long> {

    List<Incident> findAllByUser_UsernameAndStatusOrTitleContainingIgnoreCase(String userUsername, IncidentStatus status, String title);

    @Query("SELECT i FROM Incident i " +
           " WHERE i.user.username = :userUsername" +
           " AND (:status is null OR i.status = :status) " +
           " AND (:title is null OR LOWER(i.title) LIKE LOWER(CONCAT('%', :title, '%')))")
    List<Incident> findAllByUserStatusOrTitle(String userUsername, IncidentStatus status, String title);
}
