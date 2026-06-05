package com.example.devmatch.event;

import com.example.devmatch.model.JobApplication;


public record ApplicationCreatedEvent(
        JobApplication application
) {
}