package exam;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        TimetableManager t = new TimetableManager();
        t.loadTimetable("C:\\Users\\lemon\\OneDrive\\바탕 화면\\schedule.csv");
        List<String> roomASchedule = t.getSchedule("07-303 화학물질센터실습실");
        System.out.println(roomASchedule);
    }
}