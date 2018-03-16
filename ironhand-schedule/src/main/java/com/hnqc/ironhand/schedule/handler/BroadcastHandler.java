package com.hnqc.ironhand.schedule.handler;

import com.hnqc.ironhand.common.broadcast.BroadcastListener;
import com.hnqc.ironhand.common.pojo.message.BroadcastMessage;
import org.springframework.stereotype.Component;

@Component
public class BroadcastHandler implements BroadcastListener.Handler {
    @Override
    public void handle(BroadcastMessage message) {

    }
}
