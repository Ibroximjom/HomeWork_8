package uz.pdp.db;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.UUID;

public class Message implements Serializable {
    private final UUID id = UUID.randomUUID();
    private final UUID fromId;
    private final UUID toId;
    private final String text;
    private final ZonedDateTime zonedDateTime = ZonedDateTime.now();

    public Message(UUID fromId, UUID toId, String text) {
        this.fromId = fromId;
        this.toId = toId;
        this.text = text;
    }

    public UUID getId() {
        return id;
    }

    public UUID getFromId() {
        return fromId;
    }

    public UUID getToId() {
        return toId;
    }

    public String getText() {
        return text;
    }

    public ZonedDateTime getZonedDateTime() {
        return zonedDateTime;
    }
}