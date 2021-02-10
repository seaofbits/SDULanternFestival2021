package cn.sduonline.sdu_lantern_festival_2021.entity.mysql;

public class Ranking {
    private long userID;
    private int totalPoints;
    private int totalCorrectNum;
    private int totalAnswerNum;

    public Ranking() {
    }


    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int getTotalCorrectNum() {
        return totalCorrectNum;
    }

    public void setTotalCorrectNum(int totalCorrectNum) {
        this.totalCorrectNum = totalCorrectNum;
    }

    public int getTotalAnswerNum() {
        return totalAnswerNum;
    }

    public void setTotalAnswerNum(int totalAnswerNum) {
        this.totalAnswerNum = totalAnswerNum;
    }
}
