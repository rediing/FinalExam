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
            // 하나의 수업이 2개 이상의 강의실을 사용하는 경우, 각 강의실을 , 를 기준으로 분리
            String[] parts = line.split(", ");

            for (String part : parts) {
                String[] roomSchedule = part.split(":", 2);  // 강의실 이름과 시간표 부분을 구분
                if (roomSchedule.length != 2) {
                    continue;
                }

                String room = roomSchedule[0].trim();  // 강의실 이름
                String scheduleString = roomSchedule[1].trim();  // 시간표 정보

                // 해당 강의실에 시간표를 추가
                timetable.putIfAbsent(room, new ArrayList<>());
                timetable.get(room).add(scheduleString);
            }
        }

        reader.close();
    }

    // 강의실 이름으로 시간표 가져오기
    public List<String> getSchedule(String room) {
        return timetable.getOrDefault(room, Collections.emptyList());
    }
}
