package com.turn.ttorrent.example.torrentfile;

import com.turn.ttorrent.tracker.Tracker;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.InetAddress;

// 启动一个Tracker负责对BT的下载进行追踪
public class TrackerTest {

    @BeforeMethod
    public void setUp(){
        if (Logger.getRootLogger().getAllAppenders().hasMoreElements())
            return;
        BasicConfigurator.configure(new ConsoleAppender(new PatternLayout("[%d{MMdd HH:mm:ss,SSS}] %6p - %20.20c - %m %n")));
    }


    @Test
    public void trackerTest() throws IOException, InterruptedException {
        int port = 6969;
        Tracker tracker = new Tracker(port,
                "http://" + InetAddress.getLocalHost().getHostAddress() + ":" + port + ""
                        + "/announce");
        // peers项Tracker播报的时间间隔
        tracker.setAnnounceInterval(5);
        // 超过10s没有反应的peer将会被清理
        tracker.setPeerCollectorExpireTimeout(10);

        try {
            // true 表示开启一个后台线程定期(每10s)清理peer为0的torrent
            tracker.start(true);

            //等到torrent全部被清理后自动退出
            while (tracker.getTrackedTorrents().size() >= 0){
                Thread.sleep(20_000);
            }
        } finally {
            tracker.stop();
        }

    }

}
