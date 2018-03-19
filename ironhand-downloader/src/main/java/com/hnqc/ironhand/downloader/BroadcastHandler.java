package com.hnqc.ironhand.downloader;

import com.hnqc.ironhand.common.broadcast.BroadcastListener;
import com.hnqc.ironhand.common.pojo.message.BroadcastMessage;
import org.springframework.stereotype.Component;

@Component
public class BroadcastHandler implements BroadcastListener.Handler {
    @Override
    public void handle(BroadcastMessage message) {

    }
}
