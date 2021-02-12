package com.android.address_book;

public class Group {

    String groupNo;
    String groupName;

    public Group(String groupNo, String groupName) {
        this.groupNo = groupNo;
        this.groupName = groupName;
    }

    public String getGroupNo() {
        return groupNo;
    }

    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
