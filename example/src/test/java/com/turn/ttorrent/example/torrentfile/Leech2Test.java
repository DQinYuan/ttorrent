package com.turn.ttorrent.example.torrentfile;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.net.UnknownHostException;

public class Leech2Test {

    @BeforeMethod
    public void setUp(){
        if (Logger.getRootLogger().getAllAppenders().hasMoreElements())
            return;
        BasicConfigurator.configure(new ConsoleAppender(new PatternLayout("[%d{MMdd HH:mm:ss,SSS}] %6p - %20.20c - %m %n")));
    }

    @Test
    public void leech2Test() throws UnknownHostException {
        Client.download("leech2", false);
    }

}
