package com.turn.ttorrent.client.network.keyProcessors;

import com.turn.ttorrent.client.network.*;
import com.turn.ttorrent.common.TimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.NoRouteToHostException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class ConnectableKeyProcessor implements KeyProcessor {

  private static final Logger logger = LoggerFactory.getLogger(ConnectableKeyProcessor.class);

  private final Selector mySelector;
  private final TimeService myTimeService;
  private final TimeoutStorage myTimeoutStorage;

  public ConnectableKeyProcessor(Selector selector, TimeService timeService, TimeoutStorage timeoutStorage) {
    this.mySelector = selector;
    this.myTimeService = timeService;
    this.myTimeoutStorage = timeoutStorage;
  }

  @Override
  public void process(SelectionKey key) throws IOException {
    SelectableChannel channel = key.channel();
    if (!(channel instanceof SocketChannel)) {
      logger.warn("incorrect instance of channel. The key is cancelled");
      key.cancel();
      return;
    }
    SocketChannel socketChannel = (SocketChannel) channel;
    Object attachment = key.attachment();
    if (!(attachment instanceof ConnectTask)) {
      logger.warn("incorrect instance of attachment for channel {}. The key for the channel is cancelled", socketChannel);
      key.cancel();
      return;
    }
    final ConnectTask connectTask = (ConnectTask) attachment;
    final ConnectionListener connectionListener = connectTask.getConnectionListener();
    final boolean isConnectFinished;
    try {
       isConnectFinished = socketChannel.finishConnect();
    } catch (NoRouteToHostException e) {
      logger.info("Could not connect to {}:{}, received NoRouteToHostException", connectTask.getHost(), connectTask.getPort());
      connectionListener.onError(socketChannel, e);
      return;
    }
    if (!isConnectFinished) {
      logger.info("Could not connect to {}:{}", connectTask.getHost(), connectTask.getPort());
      connectionListener.onError(socketChannel, null);
      return;
    }
    socketChannel.configureBlocking(false);
    ReadWriteAttachment keyAttachment = new ReadWriteAttachment(connectionListener, myTimeService.now(), myTimeoutStorage.getTimeoutMillis());
    socketChannel.register(mySelector, SelectionKey.OP_READ, keyAttachment);
    connectionListener.onConnectionEstablished(socketChannel);
  }

  @Override
  public boolean accept(SelectionKey key) {
    return key.isValid() && key.isConnectable();
  }
}
