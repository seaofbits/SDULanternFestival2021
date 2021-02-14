package cn.sduonline.sdu_lantern_festival_2021.entity.mysql;

public class Room {
    int roomID;
    int roomCapacity;
    long user1ID;
    long user2ID;
    String roomEndState;
    String roomEndReason;


    public int getRoomID() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID = roomID;
    }

    public int getRoomCapacity() {
        return roomCapacity;
    }

    public void setRoomCapacity(int roomCapacity) {
        this.roomCapacity = roomCapacity;
    }

    public long getUser1ID() {
        return user1ID;
    }

    public void setUser1ID(long user1ID) {
        this.user1ID = user1ID;
    }

    public long getUser2ID() {
        return user2ID;
    }

    public void setUser2ID(long user2ID) {
        this.user2ID = user2ID;
    }

    public String getRoomEndState() {
        return roomEndState;
    }

    public void setRoomEndState(String roomEndState) {
        this.roomEndState = roomEndState;
    }

    public String getRoomEndReason() {
        return roomEndReason;
    }

    public void setRoomEndReason(String roomEndReason) {
        this.roomEndReason = roomEndReason;
    }
}
