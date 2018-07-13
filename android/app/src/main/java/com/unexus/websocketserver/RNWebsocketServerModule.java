package com.unexus.websocketserver;

import android.util.Log;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by umsi on 27/11/2017.
 */

public class RNWebsocketServerModule extends ReactContextBaseJavaModule {
    private WebServer webServer = null;

    public RNWebsocketServerModule(ReactApplicationContext reactContext) {
        super(reactContext);
    }

    @Override
    public String getName() {
        return "RNWebsocketServer";
    }

    @ReactMethod
    public void start(String ipAddress, int port) {
        if (webServer == null) {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(ipAddress, port);

            webServer = new WebServer(inetSocketAddress);

            webServer.setReuseAddr(true);
            webServer.start();
        }
    }

    @ReactMethod
    public void stop() {
        try {
            if (webServer != null) {
                webServer.stop();
            }
        } catch (IOException ioEx) {
            Log.e("RNWS_IO_ERR", ioEx.getMessage());
        } catch (InterruptedException iEx) {
            Log.e("RNWS_INTERRUPT_ERR", iEx.getMessage());
        } finally {
            webServer = null;
        }
    }
}
