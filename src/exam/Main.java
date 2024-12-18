package exam;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        TimetableManager t = new TimetableManager();
        t.loadTimetable("C:\\Users\\lemon\\OneDrive\\바탕 화면\\schedule.csv");
        List<String> roomASchedule = t.getSchedule("07-301 수질오염분석실험실");
        System.out.println(roomASchedule);
    }
}