package com.richard;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Created on 7/21/2016.
 */
@Data
public class RequestEvent {
    private String id;
    private LocalDateTime eventTime;
    private int responseCode;
    private long resourceId;
    private String resourceType;
    private String serverIp;
}
