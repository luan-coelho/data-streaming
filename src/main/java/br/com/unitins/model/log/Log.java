package br.com.unitins.model.log;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String message;
    private String details;
    private LogType logType;
    private LocalDateTime timestamp;

    @PrePersist
    private void setTimestamp() {
        this.timestamp = LocalDateTime.now();
    }

    public String getTimestamp() {
        if (this.timestamp != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss", new Locale("pt", "BR"));
            return this.timestamp.format(formatter);
        }
        return null;
    }
}