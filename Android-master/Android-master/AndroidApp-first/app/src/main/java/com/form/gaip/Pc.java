package com.form.gaip;

public class Pc {
        String Name;
    String seatNum;
    String location;
    String result;

    public String getUserName() {
        return Name;
    }
    public String getseatNum() {
        return seatNum;
    }
    public String getLocation() {
        return location;
    }
    public String getresult() {
        return result;
    }

        public void setUserName(String Name) {
            this.Name = Name;
        }
    public void setseatNum(String seatNum) {
        this.seatNum = seatNum;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public void setresult(String result) {
        this.result = result;
    }
        public Pc(String Name,String seatNum,String location) {
            this.Name = Name;
            this.seatNum=seatNum;
            this.location=location;
        }
    }

