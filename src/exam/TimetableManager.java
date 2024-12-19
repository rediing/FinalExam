package exam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

public class TimetableManager {
    private Map<String, List<String>> timetable = new HashMap<>();

    // CSV 파일 읽어서 timetable 초기화
    public void loadTimetable(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;

        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(", ");

            for (String part : parts) {
                String[] roomSchedule = part.split(":", 2);
                if (roomSchedule.length != 2) {
                    continue;
                }

                String room = roomSchedule[0].trim();
                String scheduleString = roomSchedule[1].trim();

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

    // 교시를 시간 범위로 변환한 후 병합
    private String mergePeriodsToTime(String periods) {
        String[] periodArray = periods.split(",");
        List<LocalTime[]> timeRanges = new ArrayList<>();

        // 교시를 시간 범위로 변환
        for (String period : periodArray) {
            int periodInt = Integer.parseInt(period.trim());
            LocalTime startTime = LocalTime.of(9, 0).plusHours(periodInt - 1);
            LocalTime endTime = startTime.plusHours(1);
            timeRanges.add(new LocalTime[]{startTime, endTime});
        }

        // 시간 범위를 병합
        Collections.sort(timeRanges, Comparator.comparing(o -> o[0])); // 시작 시간 기준 정렬
        List<LocalTime[]> mergedRanges = new ArrayList<>();
        LocalTime[] currentRange = timeRanges.get(0);

        for (int i = 1; i < timeRanges.size(); i++) {
            LocalTime[] nextRange = timeRanges.get(i);
            if (!currentRange[1].isBefore(nextRange[0])) { // 연속되거나 겹치는 경우
                currentRange[1] = nextRange[1];
            } else { // 연속되지 않는 경우
                mergedRanges.add(currentRange);
                currentRange = nextRange;
            }
        }
        mergedRanges.add(currentRange);

        // 병합된 시간 범위를 문자열로 변환
        StringBuilder result = new StringBuilder();
        for (LocalTime[] range : mergedRanges) {
            result.append(range[0]).append("~").append(range[1]).append(", ");
        }

        // 마지막 쉼표 제거
        if (result.length() > 0) {
            result.setLength(result.length() - 2);
        }

        return result.toString();
    }

    // 요일과 교시 정보를 시간 범위로 변환
    public List<String> getFormattedSchedule(String room) {
        List<String> rawSchedule = getSchedule(room);
        List<String> formattedSchedule = new ArrayList<>();

        for (String schedule : rawSchedule) {
            String[] dayAndPeriods = schedule.split("\\(");
            if (dayAndPeriods.length != 2) {
                continue;
            }

            String day = dayAndPeriods[0].trim();
            String periods = dayAndPeriods[1].replace(")", "").trim();
            String timeRange = mergePeriodsToTime(periods);

            formattedSchedule.add(day + "요일 " + timeRange);
        }

        return formattedSchedule;
    }



}
