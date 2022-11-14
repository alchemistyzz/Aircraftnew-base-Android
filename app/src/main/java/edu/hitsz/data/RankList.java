package edu.hitsz.data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

/**
 * @author night
 */
public class RankList {
    private int id;
    private int user_id;
    private int score;
    private String user_name;
    private String time;
    private int mode;



    public RankList(int score, String user_name, int user_id, String time, int mode) {
        this.score = score;
        this.user_name = user_name;
        this.user_id=user_id;
        this.time = time;
        this.mode = mode;
    }

    public RankList(){
        super();
    }

    public static RankList resultToRankList(ResultSet resultSet) {
        try {
            RankList rankList = new RankList();
            rankList.setId(resultSet.getInt("id"));
            rankList.setMode(resultSet.getInt("mode"));
            rankList.setScore(resultSet.getInt("score"));
            rankList.setTime(resultSet.getString("time"));
            rankList.setUser_id(resultSet.getInt("user_id"));
            rankList.setUser_name(resultSet.getString("user_name"));
            return rankList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public static String rankListToSql(RankList rankList){
        String sql=
                "score="+rankList.getScore()+","+
                "time="+rankList.getTime()+","+
                "user_name="+rankList.getUser_name()+","+
                "user_id="+rankList.getUser_id()+","+
                "mode="+rankList.getMode();
        return sql;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
