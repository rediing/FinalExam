package exam;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        TimetableManager timetableManager = new TimetableManager();
        try {
            timetableManager.loadTimetable("C:\\Users\\lemon\\OneDrive\\바탕 화면\\schedule.csv");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "시간표 데이터를 불러오는 데 실패했습니다.");
            return;
        }

        SwingUtilities.invokeLater(() -> new RoomSearchGUI(timetableManager).createAndShowGUI());
    }
}
