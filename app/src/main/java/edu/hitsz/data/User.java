package edu.hitsz.data;

import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String name;
    private String account;
    private String password;
    private String register_time;
    private int money;
    private String friend;
    private int super_prop_cnt;
    private int froze_prop_cnt;
    private int bomb_prop_cnt;

    public User(String name, String account, String password, String register_time) {
        this.name = name;
        this.account = account;
        this.password = password;
        this.register_time = register_time;
    }

    public User(String name, String account, String password, String register_time, int money, String friend, int super_prop_cnt, int froze_prop_cnt, int bomb_prop_cnt) {
        this.name = name;
        this.account = account;
        this.password = password;
        this.register_time = register_time;
        this.money = money;
        this.friend = friend;
        this.super_prop_cnt = super_prop_cnt;
        this.froze_prop_cnt = froze_prop_cnt;
        this.bomb_prop_cnt = bomb_prop_cnt;
    }



    public User() {
        super();
    }

    public static User resultToUser(ResultSet resultSet) {
        try {
            User user = new User();
            user.setAccount(resultSet.getString("account"));
            user.setPassword(resultSet.getString("password"));
            user.setSuper_prop_cnt(resultSet.getInt("super_prop_cnt"));
            user.setFroze_prop_cnt(resultSet.getInt("froze_prop_cnt"));
            user.setBomb_prop_cnt(resultSet.getInt("bomb_prop_cnt"));
            user.setId(resultSet.getInt("id"));
            user.setMoney(resultSet.getInt("money"));
            user.setName(resultSet.getString("name"));
            user.setRegister_time(resultSet.getString("register_time"));
            user.setFriend(resultSet.getString("friend"));
            return user;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRegister_time() {
        return register_time;
    }

    public void setRegister_time(String register_time) {
        this.register_time = register_time;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public String getFriend() {
        return friend;
    }

    public void setFriend(String friend) {
        this.friend = friend;
    }

    public int getSuper_prop_cnt() {
        return super_prop_cnt;
    }

    public void setSuper_prop_cnt(int super_prop_cnt) {
        this.super_prop_cnt = super_prop_cnt;
    }

    public int getFroze_prop_cnt() {
        return froze_prop_cnt;
    }

    public void setFroze_prop_cnt(int froze_prop_cnt) {
        this.froze_prop_cnt = froze_prop_cnt;
    }

    public int getBomb_prop_cnt() {
        return bomb_prop_cnt;
    }

    public void setBomb_prop_cnt(int bomb_prop_cnt) {
        this.bomb_prop_cnt = bomb_prop_cnt;
    }
}
