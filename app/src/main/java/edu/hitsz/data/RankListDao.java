package edu.hitsz.data;

import java.util.List;

/**
 * @author night
 */
public interface RankListDao {
    void doAdd(RankList rankList);
    void deleteList(RankList rankList);
    List<RankList> queryAll();
    void writeAll();
}
