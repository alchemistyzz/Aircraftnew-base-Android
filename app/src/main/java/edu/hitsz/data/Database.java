package edu.hitsz.data;

import android.media.MediaPlayer;
import android.os.Message;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.hitsz.LaunchActivity;
import edu.hitsz.LoadingActivity;
import edu.hitsz.LoginActivity;
import edu.hitsz.R;

public class Database implements DatabaseInterface {
    private final static String database_url = "jdbc:mysql://106.52.3.181:2235/aircraft?serverTimezone=UTC&useSSL=false&allowPublicKeyRetrieval=true";
    private final static String database_account = "aircraft";
    private final static String database_password = "123Abc!333ka@";

    private static Database database;
    private Connection connection;
    private Statement statement;
    private PreparedStatement preparedStatement;

    private List<RankList> rankLists = new ArrayList<RankList>();
    private List<User> users = new ArrayList<User>();


    private Database() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    connection = DriverManager.getConnection(database_url, database_account, database_password);
                    statement = connection.createStatement();
                    Message message = new Message();
                    message.what = LoadingActivity.CONNECT_SUCCESS;
                    LoadingActivity.handler.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void init() {
        database = new Database();
    }

    public static Database getInstance() {
        return database;
    }

    @Override
    public ResultSet executeQuery(String sql) {
        try {
            return statement.executeQuery(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public Boolean execute(String sql) {
        try {
            return statement.execute(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }


    @Override
    public boolean userExistByAccount(String account) {
        try {
            String query = "select * from user where account=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, account);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
                return true;
            else
                return false;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    @Override
    public void addRankList(RankList rankList) {
        try {
            String query = "insert into ranklist (user_id,score,user_name,time,mode) values (?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, rankList.getUser_id());
            preparedStatement.setInt(2, rankList.getScore());
            preparedStatement.setString(3, rankList.getUser_name());
            preparedStatement.setString(4, rankList.getTime());
            preparedStatement.setInt(5, rankList.getMode());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
    public void addUser(User user) {
        try {
            String query = "insert into user (super_prop_cnt,bomb_prop_cnt,froze_prop_cnt,name,account,password,register_time,money,friend) values (?,?,?,?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, user.getSuper_prop_cnt());
            preparedStatement.setInt(2, user.getBomb_prop_cnt());
            preparedStatement.setInt(3, user.getFroze_prop_cnt());
            preparedStatement.setString(4, user.getName());
            preparedStatement.setString(5, user.getAccount());
            preparedStatement.setString(6, user.getPassword());
            preparedStatement.setString(7, user.getRegister_time());
            preparedStatement.setInt(8, user.getMoney());
            preparedStatement.setString(9, user.getFriend());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void deleteRankList(RankList rankList) {
        try {
            String query = "delete from ranklist where id=" + rankList.getId();
            statement.execute(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void deleteUser(User user) {
        try {
            String query = "delete from user where id=" + user.getId();
            statement.execute(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void updateRanklist(RankList rankList) {
        try {
            String query = "update ranklist set user_id=?,score=?,user_name=?,time=?,mode=? where id=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, rankList.getUser_id());
            preparedStatement.setInt(2, rankList.getScore());
            preparedStatement.setString(3, rankList.getUser_name());
            preparedStatement.setString(4, rankList.getTime());
            preparedStatement.setInt(5, rankList.getMode());
            preparedStatement.setInt(6, rankList.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void updateUser(User user) {
        try {
            String query = "update user set account=?,password=?,name=?,register_time=?,money=?,friend=?,super_prop_cnt=?,froze_prop_cnt=?," +
                    "bomb_prop_cnt=? where id=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, user.getAccount());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setString(4, user.getRegister_time());
            preparedStatement.setInt(5, user.getMoney());
            preparedStatement.setString(6, user.getFriend());
            preparedStatement.setInt(7, user.getSuper_prop_cnt());
            preparedStatement.setInt(8, user.getFroze_prop_cnt());
            preparedStatement.setInt(9, user.getBomb_prop_cnt());
            preparedStatement.setInt(10, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    @Override
    public List<RankList> getAllRankListGlobal(int mode) {
        try {
            List<RankList> rankLists = new ArrayList<>();
            String query = "select user_id,max(score) as score,min(id) as id,min(time) as time,min(u" +
                    "ser_name) as user_name,mode from ranklist where mode=" + mode + " group by user_id,mode order by score desc";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                rankLists.add(RankList.resultToRankList(resultSet));
            }
            return rankLists;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }

    }

    @Override
    public List<RankList> getAllRankListByUser(int mode, int userId) {
        try {
            List<RankList> rankLists = new ArrayList<>();
            String query = "select * from ranklist where mode=" + mode + " and user_id=" + userId + " order by score desc";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                rankLists.add(RankList.resultToRankList(resultSet));
            }
            return rankLists;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public RankList getRankListById(int id) {
        try {
            String query = "select * from ranklist where id=" + id;
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            RankList rankList = RankList.resultToRankList(resultSet);
            return rankList;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getAllUser() {
        try {
            List<User> users = new ArrayList<>();
            String query = "select * from user";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                users.add(User.resultToUser(resultSet));
            }
            return users;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public User getUserById(int id) {
        try {
            String query = "select * from user where id=" + id;
            ResultSet resultSet = statement.executeQuery(query);
            resultSet.next();
            User user = User.resultToUser(resultSet);
            return user;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    @Override
    public User getUserByAccount(String account) {
        try {
            String query = "select * from user where account=?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, account);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            User user = User.resultToUser(resultSet);
            return user;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}
