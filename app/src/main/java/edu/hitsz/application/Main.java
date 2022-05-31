package edu.hitsz.application;


/**
 * 程序入口
 *
 * @author hitsz
 */
public class Main {
    public static void main(String[] args) {

    }

    public static final int WINDOW_WIDTH = 512;
    public static final int WINDOW_HEIGHT = 768;
    public static final Object MAIN_LOCK = new Object();
//
//    public static void main(String[] args) {
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//
//        JFrame frame = new JFrame("Aircraft War");
//        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
//        frame.setResizable(false);
//        //设置窗口的大小和位置,居中放置
//        frame.setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0,
//                WINDOW_WIDTH, WINDOW_HEIGHT);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        JPanel startWindow = new StartWindow().getGameModeChoose();
//        frame.add(startWindow);
//        frame.setVisible(true);
//
//        synchronized (MAIN_LOCK) {
//            while (startWindow.isVisible()) {
//                // 主线程等待菜单面板关闭
//                try {
//                    MAIN_LOCK.wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        frame.remove(startWindow);
//
//
//        System.out.println("Hello Aircraft War");
//
//        if (StartWindow.soundMode == 1) {
//            MusicThread.setEnableMode(1);
//        } else {
//            MusicThread.setEnableMode(0);
//        }
//        frame.remove(startWindow);
//
//        Game game;
//        if (StartWindow.gameMode == 1) {
//            game = new GameEasy();
//        } else if (StartWindow.gameMode == 2) {
//
//            game = new GameNormal();
//        }
//        else{
//            game=new GameHard();
//        }
//
//        game.setDifficulty();
//        game.action();
//
//        frame.add(game);
//        frame.setVisible(true);
//
//
//        synchronized (MAIN_LOCK) {
//            while (game.isVisible()) {
//                // 主线程等待菜单面板关闭
//                try {
//                    MAIN_LOCK.wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        frame.remove(game);
//
//        while (true) {
//            JPanel rankListWindow = new RankListWindow().getRankListWindow();
//            frame.add(rankListWindow);
//            frame.setVisible(true);
//            synchronized (MAIN_LOCK) {
//                while (rankListWindow.isVisible()) {
//                    // 主线程等待菜单面板关闭
//                    try {
//                        MAIN_LOCK.wait();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            frame.remove(rankListWindow);
//        }
//    }
}
