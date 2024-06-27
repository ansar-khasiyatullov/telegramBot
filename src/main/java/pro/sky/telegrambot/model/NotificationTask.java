package pro.sky.telegrambot.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "notification_task")
public class NotificationTask { // object of typical notification task
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private long chatId;
    private String noteText;
    private LocalDateTime noteTime;

    public NotificationTask() {
    }

    public NotificationTask(long chatId, String noteText, LocalDateTime noteTime) {
    }

    public long getId() {
        return id;
    }

    public long getChatId() {
        return chatId;
    }

    public String getNoteText() {
        return noteText;
    }

    public LocalDateTime getNoteTime() {
        return noteTime;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setChatId(long chatId) {
        this.chatId = chatId;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public void setNoteTime(LocalDateTime noteTime) {
        this.noteTime = noteTime;
    }

    @Override
    public String toString() {
        return "NotificationTask{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", noteText='" + noteText + '\'' +
                ", noteTime=" + noteTime +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationTask that = (NotificationTask) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
