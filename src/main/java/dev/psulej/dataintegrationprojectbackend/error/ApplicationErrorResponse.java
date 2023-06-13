package dev.psulej.dataintegrationprojectbackend.error;


import java.util.List;

public record ApplicationErrorResponse(List<ApplicationError> errors) {
}