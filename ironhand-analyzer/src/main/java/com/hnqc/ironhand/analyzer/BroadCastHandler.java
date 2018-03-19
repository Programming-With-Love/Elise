package com.hnqc.ironhand.analyzer;

import com.hnqc.ironhand.common.broadcast.BroadcastListener;
import com.hnqc.ironhand.common.pojo.message.BroadcastMessage;
import org.springframework.stereotype.Component;

@Component
public class BroadCastHandler implements BroadcastListener.Handler {
    @Override
    public void handle(BroadcastMessage message) {

    }
}
