package com.unexus.websocketserver;

import android.util.Log;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetSocketAddress;

/**
 * Created by umsi on 27/11/2017.
 */

public class WebServer extends WebSocketServer {
    public WebServer(InetSocketAddress inetSocketAddress) {
        super(inetSocketAddress);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        try {
            String jsonString = (new JSONObject()).put("type", "onMessage")
                    .put("data", conn.getRemoteSocketAddress().getHostName() + " entered the room")
                    .toString();

            broadcast(jsonString);
        } catch (JSONException e) {
            broadcast(e.getMessage());
        }
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        try {
            String jsonString = (new JSONObject()).put("type", "onMessage")
                    .put("data", conn.getRemoteSocketAddress().getHostName() + " has left the room")
                    .toString();

            broadcast(jsonString);
        } catch (JSONException e) {
            broadcast(e.getMessage());
        }
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        try {
            String jsonString = (new JSONObject()).put("type", "onMessage")
                    .put("data", conn.getRemoteSocketAddress().getHostName() + ": " + message)
                    .toString();

            broadcast(jsonString);
        } catch (JSONException e) {
            broadcast(e.getMessage());
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        try {
            String jsonString = (new JSONObject()).put("type", "onError")
                    .put("data", ex.getMessage())
                    .toString();

            broadcast(jsonString);
        } catch (JSONException e) {
            broadcast(e.getMessage());
        }
    }

    @Override
    public void onStart() {
        try {
            String jsonString = (new JSONObject()).put("type", "onStart")
                    .put("data", "Websocket server now starting...")
                    .toString();

            broadcast(jsonString);
        } catch (JSONException e) {
            broadcast(e.getMessage());
        }
    }
}

