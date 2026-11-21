package com.ada.incident.incident;

public record IncidentResponse(
        Long id,
        String title,
        String description,
        IncidentPriority priority,
        IncidentStatus status,
        String createdAt
) {

    public static IncidentResponse fromIncident(Incident incident) {
        return new IncidentResponse(
                incident.getId(),
                incident.getTitle(),
                incident.getDescription(),
                incident.getPriority(),
                incident.getStatus(),
                incident.getCreatedAt().toString()
        );
    }
}
