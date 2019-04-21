package com.turn.ttorrent.example.torrentfile;

import com.turn.ttorrent.client.SimpleClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Client {

    /**
     *
     * @param downloadPath
     * @param seeder  是否做种 ,如果做种的话,则该下载不能立刻停掉
     * @throws UnknownHostException
     */
    public static void download(String downloadPath, boolean seeder) throws UnknownHostException {
        String torrentPath = "file1.jar.torrent";
        SimpleClient client = new SimpleClient();

        InetAddress address = InetAddress.getLocalHost();

        try {
            // 该函数在下载函数后才会返回,但是对于做种的节点来说,因为节点中原本就有相应的文件,该函数会立刻返回
            client.downloadTorrent(torrentPath,
                    downloadPath,
                    address);
            if (seeder){
                do {
                    Thread.sleep(20_000);
                    // 如果发现已经没有peer则退出
                } while (client.getPeers() > 0);
            }
        } catch (Exception e) {
            client.stop();
        }

    }

}
