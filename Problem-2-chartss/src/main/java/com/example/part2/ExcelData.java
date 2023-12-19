package com.example.part2;

import java.time.LocalDateTime;
import java.util.Date;

public class ExcelData {
    String date;
    String month;
    String team;
    String panel;
    String round;
    String skill;
    String time;
    String currLoc;
    String preLoc;
    String name;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getPanel() {
        return panel;
    }

    public void setPanel(String panel) {
        this.panel = panel;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCurrLoc() {
        return currLoc;
    }

    public void setCurrLoc(String currLoc) {
        this.currLoc = currLoc;
    }

    public String getPreLoc() {
        return preLoc;
    }

    public void setPreLoc(String preLoc) {
        this.preLoc = preLoc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return "ExcelData{" +
                "date='" + date + '\'' +
                ", month='" + month + '\'' +
                ", team='" + team + '\'' +
                ", panel='" + panel + '\'' +
                ", round='" + round + '\'' +
                ", skill='" + skill + '\'' +
                ", time='" + time + '\'' +
                ", currLoc='" + currLoc + '\'' +
                ", preLoc='" + preLoc + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
