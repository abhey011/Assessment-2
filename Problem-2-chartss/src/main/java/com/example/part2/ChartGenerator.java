package com.example.part2;
import org.apache.xpath.operations.Neg;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChartGenerator {

    public static JFreeChart createBarChart(List<ExcelData> data) {
        // Example: Create a bar chart with team names and the count of interviews
        Map <String, Integer> panelMap = data.stream().filter(data1 -> data1.getMonth().equals("Oct-23") || data1.getMonth().equals("Nov-23")).collect(Collectors.groupingBy(ExcelData::getPanel, Collectors.summingInt(e->1)));

        List<Map.Entry<String,Integer>> top3Panels = panelMap.entrySet().stream().sorted(Map.Entry.<String,Integer>comparingByValue().reversed()).limit(3).collect(Collectors.toList());

        CategoryDataset dataset = createDataset2(top3Panels);
        JFreeChart chart = ChartFactory.createBarChart(
                "Top 3 Panels",
                "Panel",
                "Number of Interviews",
                dataset,
                PlotOrientation.HORIZONTAL,
                true,
                true,
                false
        );

        // Customize chart properties as needed
        chart.setBackgroundPaint(Color.WHITE);
        return chart;
    }

    private static CategoryDataset createDataset2(List<Map.Entry<String, Integer>> top3Panels) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        top3Panels.forEach(entry -> dataset.addValue(entry.getValue(), "Interviews", entry.getKey()));

        // Add other dataset entries as needed

        return dataset;
    }

    public static JFreeChart top3SkillsPieChart(){
        Map<String, Integer> top3Skills = getTop3SkillsFromDatabaseView();

        DefaultPieDataset dataset = createTop3SkillsDataset(top3Skills);

        JFreeChart pieChart = ChartFactory.createPieChart(
                "Top 3 Skills - OCT/NOV 2023",
                dataset,
                true,
                true,
                false
        );
        pieChart.setBackgroundPaint(Color.WHITE);
        return pieChart;
    }

    public static JFreeChart top3SkillsPeakTimePieChart(){
        Map<String, Integer> top3Skills = getTop3SkillsDuringPeakTime();

        DefaultPieDataset dataset = createTop3SkillsDataset(top3Skills);

        JFreeChart pieChart = ChartFactory.createPieChart(
                "Top 3 Skills in Peak time - OCT/NOV 2023",
                dataset,
                true,
                true,
                false
        );
        pieChart.setBackgroundPaint(Color.WHITE);
        return pieChart;
    }

    private static Map<String,Integer> getTop3SkillsFromDatabaseView(){
        Map<String,Integer> top3Skills = new HashMap<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/charts", "root", "Aj@123456")){
            String sql = "SELECT skill, COUNT(*) as totalInterviews FROM my_view WHERE month IN ('Oct-23','Nov-23') GROUP BY skill ORDER BY totalInterviews DESC LIMIT 3";
            String sql2 = "CREATE VIEW my_view AS SELECT skill, month, count(*) as totalInterviews FROM interviewTable GROUP BY skill, month";
            try(Statement statement = connection.createStatement()) {
                statement.executeUpdate(sql2);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                try(ResultSet resultSet = statement.executeQuery()){
                    while (resultSet.next()) {
                        String skill = resultSet.getString("skill");
                        int totalInterviews = resultSet.getInt("totalInterviews");
                        top3Skills.put(skill,totalInterviews);
                    }

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return top3Skills;
    }

    private static DefaultPieDataset createTop3SkillsDataset(Map<String, Integer> top3Skills){
        DefaultPieDataset dataset = new DefaultPieDataset<>();
        top3Skills.forEach(dataset::setValue);
        return dataset;
    }

    private static Map<String,Integer> getTop3SkillsDuringPeakTime(){
        Map<String,Integer> top3Skills = new HashMap<>();
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/charts", "root", "Aj@123456")){
            String sql = "SELECT skill, COUNT(*) as totalInterviews FROM my_view2 WHERE month IN ('Oct-23','Nov-23') and TIME(time) BETWEEN '17:00:00' AND '19:00:00' GROUP BY skill ORDER BY totalInterviews DESC LIMIT 3";
            String sql2 = "CREATE VIEW my_view2 AS SELECT skill, month, time, count(*) as totalInterviews FROM interviewTable GROUP BY skill, month, time";
            try(Statement statement = connection.createStatement()) {
                statement.executeUpdate(sql2);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                try(ResultSet resultSet = statement.executeQuery()){
                    while (resultSet.next()) {
                        String skill = resultSet.getString("skill");
                        int totalInterviews = resultSet.getInt("totalInterviews");
                        top3Skills.put(skill,totalInterviews);
                    }

                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return top3Skills;
    }


    public static JFreeChart maxInterviewBarChart() {
        Map <String,Integer> map = getMaxInterviewsByTeam();
        DefaultCategoryDataset dataset = createMaxInterviewByTeamDataset(map);
        JFreeChart barChart = createBarChartMaxInterviews(dataset);
        return barChart;
    }

    private static Map<String, Integer> getMaxInterviewsByTeam(){
        Map<String, Integer> map = new HashMap<>();

        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/charts", "root", "Aj@123456")) {
            String sql = "SELECT team, COUNT(*) AS totalInterviews from interviewTable WHERE month in ('Oct-23', 'Nov-23') GROUP BY team ORDER BY totalInterviews DESC LIMIT 1";
            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                try(ResultSet resultSet = statement.executeQuery()) {
                    if(resultSet.next()) {
                        String team = resultSet.getString("team");
                        int totalInterviews = resultSet.getInt("totalInterviews");
                        map.put(team, totalInterviews);
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    private static DefaultCategoryDataset createMaxInterviewByTeamDataset(Map<String,Integer> map){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        map.forEach((team, totalInterviews) -> dataset.addValue(totalInterviews, "Interviews", team));
        return dataset;
    }

    private static JFreeChart createBarChartMaxInterviews(DefaultCategoryDataset dataset) {
        JFreeChart barChart = ChartFactory.createBarChart(
          "Team with Max interviews - Oct/Nov 2023",
          "Team",
          "Number of Interviews",
          dataset
        );

        barChart.setBackgroundPaint(Color.WHITE);
        return barChart;
    }


    public static JFreeChart minInterviewBarChart() {
        Map <String,Integer> map = getMinInterviewsByTeam();
        DefaultCategoryDataset dataset = createMinInterviewByTeamDataset(map);
        JFreeChart barChart = createBarChartMinInterviews(dataset);
        return barChart;
    }

    private static Map<String, Integer> getMinInterviewsByTeam(){
        Map<String, Integer> map = new HashMap<>();

        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/charts", "root", "Aj@123456")) {
            String sql = "SELECT team, COUNT(*) AS totalInterviews from interviewTable WHERE month in ('Oct-23', 'Nov-23') GROUP BY team ORDER BY totalInterviews LIMIT 1";
            try(PreparedStatement statement = connection.prepareStatement(sql)) {
                try(ResultSet resultSet = statement.executeQuery()) {
                    if(resultSet.next()) {
                        String team = resultSet.getString("team");
                        int totalInterviews = resultSet.getInt("totalInterviews");
                        map.put(team, totalInterviews);
                    }
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return map;
    }

    private static DefaultCategoryDataset createMinInterviewByTeamDataset(Map<String,Integer> map){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        map.forEach((team, totalInterviews) -> dataset.addValue(totalInterviews, "Interviews", team));
        return dataset;
    }

    private static JFreeChart createBarChartMinInterviews(DefaultCategoryDataset dataset) {
        JFreeChart barChart = ChartFactory.createBarChart(
                "Team with Min interviews - Oct/Nov 2023",
                "Team",
                "Number of Interviews",
                dataset
        );

        barChart.setBackgroundPaint(Color.WHITE);
        return barChart;
    }

}