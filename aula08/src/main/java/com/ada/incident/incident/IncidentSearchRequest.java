package com.ada.incident.incident;

public record IncidentSearchRequest(
        IncidentStatus status,
        String title) {
}
