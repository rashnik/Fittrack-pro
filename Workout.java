import java.io.Serializable;
import java.time.LocalDateTime;

public class Workout implements Serializable {
    private static final long serialVersionUID = 1L;
    private String type;
    private int duration;
    private int calories;
    private LocalDateTime performedAt;
    private String notes;

    public Workout(String type, int duration, int calories, LocalDateTime performedAt, String notes) {
        this.type = type;
        this.duration = duration;
        this.calories = calories;
        this.performedAt = performedAt;
        this.notes = notes;
    }

    public String getType() { return type; }
    public int getDuration() { return duration; }
    public int getCalories() { return calories; }
    public LocalDateTime getPerformedAt() { return performedAt; }
    public String getNotes() { return notes; }

    @Override
    public String toString() {
        return String.format("%s — %d min — %d kcal — %s", type, duration, calories, performedAt.toString());
    }
}
