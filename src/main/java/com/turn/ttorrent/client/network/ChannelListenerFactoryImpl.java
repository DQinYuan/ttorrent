package com.turn.ttorrent.client.network;

import com.turn.ttorrent.common.PeersStorageProvider;
import com.turn.ttorrent.common.SharingPeerFactoryImpl;
import com.turn.ttorrent.common.SharingPeerRegister;
import com.turn.ttorrent.common.TorrentsStorageProvider;

import java.util.concurrent.ExecutorService;

public class ChannelListenerFactoryImpl implements ChannelListenerFactory {

  private final PeersStorageProvider myPeersStorageProvider;
  private final TorrentsStorageProvider myTorrentsStorageProvider;
  private final SharingPeerRegister mySharingPeerRegister;
  private final ExecutorService myExecutorService;
  private final SharingPeerFactoryImpl mySharingPeerFactory;


  public ChannelListenerFactoryImpl(PeersStorageProvider peersStorageProvider,
                                    TorrentsStorageProvider torrentsStorageProvider,
                                    SharingPeerRegister sharingPeerRegister,
                                    ExecutorService executorService,
                                    SharingPeerFactoryImpl sharingPeerFactory) {
    this.myPeersStorageProvider = peersStorageProvider;
    this.myTorrentsStorageProvider = torrentsStorageProvider;
    this.mySharingPeerRegister = sharingPeerRegister;
    this.myExecutorService = executorService;
    this.mySharingPeerFactory = sharingPeerFactory;
  }

  @Override
  public ConnectionListener newChannelListener() {
    return new StateChannelListener(myPeersStorageProvider, myTorrentsStorageProvider, mySharingPeerRegister, myExecutorService, mySharingPeerFactory);
  }
}