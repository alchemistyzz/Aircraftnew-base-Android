package edu.hitsz.data;

/**
 * @author night
 */
public class RankList implements Comparable<RankList>, java.io.Serializable {
    private String score;
    private String userName;
    private String time;
    private String mode;

    public RankList(String score, String userName, String time, String mode) {
        this.score = score;
        this.userName = userName;
        this.time = time;
        this.mode = mode;
    }

    @Override
    public int compareTo(RankList rankList) {
        return Integer.parseInt(this.score) < Integer.parseInt(rankList.getScore()) ? 1 : -1;
    }


    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
