package com.maanondev.e2softdemo.Model;

/**
 * Created by pankaj on 10/5/2017.
 */

public class Reg {

    public String UserName;
    public String Password;
    public String Name;
    public Long Dob;
    public Long Mob;

    public Reg(String userName, String password, String name, Long dob, Long mob) {
        UserName = userName;
        Password = password;
        Name = name;
        Dob = dob;
        Mob = mob;
    }
}
