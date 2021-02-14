package cn.sduonline.sdu_lantern_festival_2021.entity.mysql;

public class AnswerSheet {
    int answerSheetID;
    int riddleListID;
    int userID;
    String answer1;
    String answer2;
    String answer3;
    String answer4;
    String answer5;
    String answer6;
    int answeredNum;
    int correctNum;
    int multiple;
    int score;


    public String[] getAnswersAsArray() {
        return new String[]{
                answer1, answer2, answer3, answer4, answer5, answer6
        };
    }

    public void setAnswersByArray(String[] answers) {
        switch (answers.length) {
        case 6:
            this.setAnswer6(answers[5]);
        case 5:
            this.setAnswer5(answers[4]);
        case 4:
            this.setAnswer4(answers[3]);
        case 3:
            this.setAnswer3(answers[2]);
        case 2:
            this.setAnswer2(answers[1]);
        case 1:
            this.setAnswer1(answers[0]);
        case 0:
            break;
        default:
            throw new IllegalArgumentException("answers的长度不正确");
        }
    }


    public AnswerSheet() {
    }

    public int getAnswerSheetID() {
        return answerSheetID;
    }

    public void setAnswerSheetID(int answerSheetID) {
        this.answerSheetID = answerSheetID;
    }

    public int getRiddleListID() {
        return riddleListID;
    }

    public void setRiddleListID(int riddleListID) {
        this.riddleListID = riddleListID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public String getAnswer4() {
        return answer4;
    }

    public void setAnswer4(String answer4) {
        this.answer4 = answer4;
    }

    public String getAnswer5() {
        return answer5;
    }

    public void setAnswer5(String answer5) {
        this.answer5 = answer5;
    }

    public String getAnswer6() {
        return answer6;
    }

    public void setAnswer6(String answer6) {
        this.answer6 = answer6;
    }

    public int getAnsweredNum() {
        return answeredNum;
    }

    public void setAnsweredNum(int answeredNum) {
        this.answeredNum = answeredNum;
    }

    public int getCorrectNum() {
        return correctNum;
    }

    public void setCorrectNum(int correctNum) {
        this.correctNum = correctNum;
    }

    public int getMultiple() {
        return multiple;
    }

    public void setMultiple(int multiple) {
        this.multiple = multiple;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
