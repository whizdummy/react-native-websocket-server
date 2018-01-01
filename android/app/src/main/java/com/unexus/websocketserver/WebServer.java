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
    private static final String ON_OPEN             = "WS_ON_OPEN";
    private static final String ON_CLOSE            = "WS_ON_CLOSE";
    private static final String ON_MESSAGE          = "WS_ON_MESSAGE";
    private static final String ON_ERROR            = "WS_ON_ERROR";
    private static final String ON_START            = "WS_ON_START";
    private static final String ON_ERROR_MESSAGE    = "WS_ON_ERROR_MESSAGE";

    public WebServer(InetSocketAddress inetSocketAddress) {
        super(inetSocketAddress);
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        Log.d(ON_OPEN, "Websocket onOpen");
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        Log.d(ON_CLOSE, "Websocket onClose");
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        Log.d(ON_MESSAGE, "Websocket onMessage");

        try {
            // Parse passed message data to JSON object
            JSONObject jsonObject = new JSONObject(message);

            // Add origin (Who sent the message)
            jsonObject.put("origin", conn.getRemoteSocketAddress().getHostName());

            broadcast(jsonObject.toString());
        } catch (JSONException e) {
            broadcast(e.getMessage());
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        Log.d(ON_ERROR, "Websocket onError");

        Log.e(ON_ERROR_MESSAGE, ex.getMessage());
    }

    @Override
    public void onStart() {
        Log.d(ON_START, "Websocket onStart");
    }
}

