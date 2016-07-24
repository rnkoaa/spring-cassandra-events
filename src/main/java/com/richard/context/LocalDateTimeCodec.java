package com.richard.context;

import com.datastax.driver.core.DataType;
import com.datastax.driver.core.ProtocolVersion;
import com.datastax.driver.core.TypeCodec;
import com.datastax.driver.core.exceptions.InvalidTypeException;

import java.nio.ByteBuffer;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * Created using Intellij IDE
 * Created by rnkoaa on 7/24/16.
 */
public class LocalDateTimeCodec extends TypeCodec<LocalDateTime> {
    protected LocalDateTimeCodec() {
        super(DataType.timestamp(), LocalDateTime.class);
    }

    @Override
    public ByteBuffer serialize(LocalDateTime value, ProtocolVersion protocolVersion) throws InvalidTypeException {
        long millis = value.toInstant(ZoneOffset.UTC).toEpochMilli();
        return bigint().serializeNoBoxing(millis, protocolVersion);
    }

    @Override
    public LocalDateTime deserialize(ByteBuffer bytes, ProtocolVersion protocolVersion) throws InvalidTypeException {
        long millis = bigint().deserializeNoBoxing(bytes, protocolVersion);
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(millis), ZoneOffset.UTC);
    }

    @Override
    public LocalDateTime parse(String value) throws InvalidTypeException {
        if (value == null || value.equals("NULL"))
            return null;

        try {
            return LocalDateTime.parse(value);
        } catch (IllegalArgumentException iae) {
            throw new InvalidTypeException("Could not parse format: " + value, iae);
        }
    }

    @Override
    public String format(LocalDateTime value) throws InvalidTypeException {
        if (value == null)
            return "NULL";

        return value.toString();
    }
}
