package exam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TimetableManager {
    private Map<String, List<String>> timetable = new HashMap<>();

    // CSV 파일 읽어서 timetable 초기화
    public void loadTimetable(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(":");
            String room = parts[0].trim();
            String times = parts[1].trim();

            timetable.putIfAbsent(room, new ArrayList<>());
            timetable.get(room).add(times);
        }

        reader.close();
    }

    // 강의실 이름으로 시간표 가져오기
    public List<String> getSchedule(String room) {
        return timetable.getOrDefault(room, Collections.emptyList());
    }
}
