package exam;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
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

    // 현재 시간과 비교
    public boolean isRoomInUse(String room) {
        List<String> formattedSchedule = getFormattedSchedule(room);
        LocalTime now = LocalTime.now();
        DayOfWeek today = LocalDate.now().getDayOfWeek(); // 현재 요일 (월요일 ~ 일요일)

        for (String schedule : formattedSchedule) {
            // "요일 "로 분리하여 요일과 시간 구간 추출
            String[] parts = schedule.split("요일 ");
            if (parts.length != 2) {
                continue;
            }

            String day = parts[0]; // 요일 부분
            String timeRange = parts[1]; // 시간 구간

            // 요일 문자열을 DayOfWeek로 변환
            DayOfWeek scheduleDay = convertToDayOfWeek(day);
            if (scheduleDay == null || scheduleDay != today) {
                continue; // 현재 요일과 다르면 건너뜀
            }

            // 시간 구간 파싱
            String[] times = timeRange.split("~");
            if (times.length != 2) {
                continue;
            }

            LocalTime startTime = LocalTime.parse(times[0].trim());
            LocalTime endTime = LocalTime.parse(times[1].trim());

            // 현재 시간이 시간 구간에 속하는지 확인
            if (now.isAfter(startTime) && now.isBefore(endTime)) {
                return true; // 현재 시간에 강의실이 사용 중
            }
        }

        return false; // 현재 요일과 시간에 강의실이 비어 있음
    }

    // 요일 문자열을 DayOfWeek로 변환
    private DayOfWeek convertToDayOfWeek(String day) {
        switch (day) {
            case "월": return DayOfWeek.MONDAY;
            case "화": return DayOfWeek.TUESDAY;
            case "수": return DayOfWeek.WEDNESDAY;
            case "목": return DayOfWeek.THURSDAY;
            case "금": return DayOfWeek.FRIDAY;
            case "토": return DayOfWeek.SATURDAY;
            case "일": return DayOfWeek.SUNDAY;
            default: return null;
        }
    }

}
