package exam;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RoomSearchGUI {
    private TimetableManager timetableManager;

    public RoomSearchGUI(TimetableManager timetableManager) {
        this.timetableManager = timetableManager;
    }

    public void createAndShowGUI() {
        JFrame frame = new JFrame("강의실 검색");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        //검색 표시줄 및 버튼을 위한 패널 만들기
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());

        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("검색");

        // 검색 패널에 검색 필드 및 버튼 추가
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        mainPanel.add(searchPanel, BorderLayout.NORTH);


        frame.add(mainPanel);
        frame.setVisible(true);
    }
}
