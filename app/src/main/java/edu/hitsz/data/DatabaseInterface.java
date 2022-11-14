package edu.hitsz.data;

import java.net.URI;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public interface DatabaseInterface {
    void addRankList(RankList rankList);

    void addUser(User user);

    void deleteRankList(RankList rankList);

    void deleteUser(User user);

    void updateRanklist(RankList rankList);

    void updateUser(User user);

    boolean userExistByAccount(String account);


    List<RankList> getAllRankListGlobal(int mode);
    List<RankList> getAllRankListByUser(int mode,int userId);

    RankList getRankListById(int id);

    List<User> getAllUser();

    User getUserById(int id);

    User getUserByAccount(String account);

    Boolean execute(String sql);

    ResultSet executeQuery(String sql);


}
