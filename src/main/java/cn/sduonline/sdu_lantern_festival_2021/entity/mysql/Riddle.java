package cn.sduonline.sdu_lantern_festival_2021.entity.mysql;

public class Riddle {
    private int riddleID;
    private String content;
    private String tip;
    private String ans;


    public int getRiddleID() {
        return riddleID;
    }

    public void setRiddleID(int riddleID) {
        this.riddleID = riddleID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }
}
