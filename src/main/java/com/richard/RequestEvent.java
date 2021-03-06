package com.richard;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created on 7/21/2016.
 */
@Data
@Builder
public class RequestEvent {
    private UUID id;
    private LocalDateTime eventTime;
    private int responseCode;
    private int resourceId;
    private String resourceType;
    private String serverIp;
}
