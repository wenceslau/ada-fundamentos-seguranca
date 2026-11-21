package com.ada.incident.incident;

import com.ada.incident.user.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/incidents")
public class IncidentController {

    private final IncidentService incidentService;

    public IncidentController(IncidentService incidentService) {
        this.incidentService = incidentService;
    }

    @PostMapping
    public ResponseEntity<IncidentResponse> createIncident(@RequestBody @Valid IncidentCreateRequest request,
                                                           Authentication authentication) {

        var incident = incidentService.createIncident(request, authentication.getName());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(IncidentResponse.fromIncident(incident));
    }

    @GetMapping
    public List<IncidentResponse> searchIncidents(IncidentSearchRequest searchRequest,
                                                  Authentication authentication) {

        var list = incidentService.searchIncidents(searchRequest, authentication.getName());

        return list.stream()
                .map(IncidentResponse::fromIncident)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentResponse> getIncidentById(@PathVariable Long id,
                                                            Authentication authentication) {
        var incident = incidentService.getIncidentById(id, authentication.getName());
        return ResponseEntity.ok(
                IncidentResponse.fromIncident(incident)
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<IncidentResponse> updateIncidentStatus(@PathVariable Long id,
                                                                 @RequestBody @Valid IncidentStatusUpdateRequest request,
                                                                 Authentication authentication) {
        var incident = incidentService.updateIncidentStatus(id, request.status(), authentication.getName());
        return ResponseEntity.ok(
                IncidentResponse.fromIncident(incident)
        );
    }

}

