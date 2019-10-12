package com.github.echcz.sb2demo2.endpoint;

import javax.websocket.Session;

public interface Endpoint {
    void onOpen(Session session);

    void onMessage(String message, Session session);

    void onClose(Session session);

    void onError(Session session, Throwable error);
}
