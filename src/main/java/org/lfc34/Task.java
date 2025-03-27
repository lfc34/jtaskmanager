package org.lfc34;

import java.time.*;
import java.time.format.DateTimeFormatter;

// a good way to encapsulate data
public class Task {
    public static DateTimeFormatter deadLineFormat = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");
    private String taskName = "";
    public enum taskState {
        DONE,
        UNDONE,
        IN_PROCESS,
        EXPIRED
    };
    private taskState currentState = taskState.UNDONE;

    private LocalDateTime deadline = LocalDateTime.now();

    public Task() {}

    public Task(String taskName, LocalDateTime deadline) {
        this.taskName = taskName;
        this.deadline = deadline;
    }
    
    public static taskState parseState(String strState) {
        return switch (strState) {
            case "DONE" -> taskState.DONE;
            case "UNDONE" -> taskState.UNDONE;
            case "IN_PROCESS" -> taskState.IN_PROCESS;
            case "EXPIRED" -> taskState.EXPIRED;
            default -> null;
        };
    }

    public void setName(String newName) {
        this.taskName = newName;
    }

    public void setState(taskState newState) {
        this.currentState = newState;
    }

    public void setDeadline(LocalDateTime date) {
        this.deadline = date;
    }

    public String getName() {
        return this.taskName;
    }

    public taskState getState() {
        return this.currentState;
    }

    public LocalDateTime getDeadline() {
        return this.deadline;
    }

    public String getBeautifulDeadline() {
        return getDeadline().format(deadLineFormat);
    }
}
