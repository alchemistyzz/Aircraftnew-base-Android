package edu.hitsz.data;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.hitsz.LaunchActivity;

/**
 * @author night
 */
public class RankListDaoImpl implements RankListDao {
    private List<RankList> rankLists;

    public RankListDaoImpl() {
        rankLists=new ArrayList<>();
        String fileName = "/data/user/0/edu.hitsz/files/rankList"+ LaunchActivity.GAME_MODE +".ser";
        File file = new File(fileName);
        if (file.length() != 0) {
            try {
                FileInputStream fileIn = new FileInputStream(fileName);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                rankLists = (List<RankList>) in.readObject();
                in.close();
                fileIn.close();
            } catch (IOException i) {
                i.printStackTrace();
                return;
            } catch (ClassNotFoundException c) {
                c.printStackTrace();
                return;
            }
        }
    }

    @Override
    public void writeAll() {
        try {
            FileOutputStream fileOut = new FileOutputStream("/data/user/0/edu.hitsz/files/rankList"+ LaunchActivity.GAME_MODE +".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(rankLists);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }


    @Override
    public void doAdd(RankList rankList) {
        rankLists.add(rankList);
    }

    @Override
    public void deleteList(RankList rankList) {
        rankLists.remove(rankList);
    }

    @Override
    public List<RankList> queryAll() {
        Collections.sort(rankLists);
        return rankLists;
    }


}
