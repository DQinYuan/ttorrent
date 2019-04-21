package com.turn.ttorrent.example.torrentfile;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.UnknownHostException;

// 在seeder夹下已经存放了一个完好的文件,用于做种
public class SeederTest {

    @BeforeMethod
    public void setUp(){
        if (Logger.getRootLogger().getAllAppenders().hasMoreElements())
            return;
        BasicConfigurator.configure(new ConsoleAppender(new PatternLayout("[%d{MMdd HH:mm:ss,SSS}] %6p - %20.20c - %m %n")));
    }

    @Test
    public void seederTest() throws UnknownHostException {
        Client.download("seeder", true);
    }

}
