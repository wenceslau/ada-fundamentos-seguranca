package com.ada.incident.incident;

import com.ada.incident.exception.IncidentNotFoundException;
import com.ada.incident.exception.StatusChangeException;
import com.ada.incident.user.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class IncidentService {

    private final IncidentRepository incidentRepository;
    private final UserRepository userRepository;

    public IncidentService(IncidentRepository incidentRepository, UserRepository userRepository) {
        this.incidentRepository = incidentRepository;
        this.userRepository = userRepository;
    }

    public Incident createIncident(IncidentCreateRequest request, String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        var incident = new Incident();
        incident.setStatus(IncidentStatus.OPEN);
        incident.setPriority(request.priority());
        incident.setTitle(request.title());
        incident.setDescription(request.description());
        incident.setCreatedAt(LocalDateTime.now());
        incident.setUser(user);

        return incidentRepository.save(incident);
    }

    public Incident updateIncidentStatus(Long incidentId, IncidentStatus newStatus, String username) {
        var incident = incidentRepository.findById(incidentId)
                .orElseThrow(() -> new IncidentNotFoundException(incidentId));

        if (!incident.getUser().getUsername().equals(username)) {
            throw new IncidentNotFoundException(incidentId);
        }

        if (incident.getStatus() == IncidentStatus.CLOSED) {
            throw new StatusChangeException("Cannot change status of a closed incident.");
        }

        if (incident.getStatus() == IncidentStatus.OPEN && newStatus == IncidentStatus.OPEN) {
            throw new StatusChangeException("Incident is already open.");
        }

        if (incident.getStatus() == IncidentStatus.IN_PROGRESS && newStatus == IncidentStatus.OPEN) {
            throw new StatusChangeException("Cannot change status from IN_PROGRESS to OPEN.");
        }

        incident.setStatus(newStatus);
        return incidentRepository.save(incident);
    }

    public Incident getIncidentById(Long incidentId, String username) {
        var optional = incidentRepository.findById(incidentId);

        if (optional.isPresent()) {
            var incident = optional.get();
            if (incident.getUser().getUsername().equals(username)) {
                return incident;
            }
        }
        throw new IncidentNotFoundException(incidentId);
    }

    public List<Incident> searchIncidents(IncidentSearchRequest request, String username) {
        return incidentRepository.findAllByUserStatusOrTitle(username, request.status(), request.title());
    }


}
