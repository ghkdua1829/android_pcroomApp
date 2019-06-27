package com.form.gaip;

public class Seat {
    String seat_num;
    String name;
    String used;


    public String getseat_num() {
        return seat_num;
    }
    public String getname() {
        return name;
    }
    public String getButtonname(String A) {
        if(A.equalsIgnoreCase("1")) {
            return "사용불가";
        }
        else
            return "사용가능";
    }
    public String getused() {
        return used;
    }

    public void setseat_num(String seat_num) {
        this.seat_num = seat_num;
    }
    public void setname(String name) {
        this.name = name;
    }
    public void setused(String used) {
        this.used = used;
    }
    public Seat(String seat_num,String name,String used) {
        this.seat_num = seat_num;
        this.name=name;
        this.used=used;
    }
}

