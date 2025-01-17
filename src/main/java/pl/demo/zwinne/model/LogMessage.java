package pl.demo.zwinne.model;

import org.springframework.data.elasticsearch.annotations.Document;

import java.time.Instant;
import java.util.Map;

@Document(indexName = "logs")
public class LogMessage {
    private String id; // Document ID in Elasticsearch
    private String level; // Log level (e.g., INFO, WARN, ERROR)
    private String message; // The actual log message
    private Instant timestamp; // When the log was created
    private String loggerName; // Name of the logger

    // Constructor
    public LogMessage(String level, String message, Instant timestamp, String loggerName) {
        this.level = level;
        this.message = message;
        this.timestamp = timestamp;
        this.loggerName = loggerName;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public void setLoggerName(String loggerName) {
        this.loggerName = loggerName;
    }



    // toString method for easy debugging or logging
    @Override
    public String toString() {
        return "LogMessage{" +
                "id='" + id + '\'' +
                ", level='" + level + '\'' +
                ", message='" + message + '\'' +
                ", timestamp=" + timestamp +
                ", loggerName='" + loggerName + '\'' +
                '}';
    }
}