package com.ada.incident.incident;

import jakarta.validation.constraints.NotNull;

public record IncidentStatusUpdateRequest(
        @NotNull
        IncidentStatus status
) {
}
