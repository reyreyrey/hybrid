package com.ivi.hybrid.core.push.requests;

import android.os.Looper;


import com.ivi.hybrid.core.config.Config;
import com.ivi.hybrid.core.push.callback.Callback;
import com.ivi.hybrid.core.push.utils.Utils;
import com.ivi.hybrid.utils.LogUtil;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class ScheduleRequest implements Runnable {
    private DatagramSocket socket;
    private DatagramPacket sendPacket;
    private Callback mCallback;
    private ReceiveThread mReceiveThread;

    public ScheduleRequest(String ip, int port, ScheduleRequestBody requestBody, Callback callback) {
        try {
            mCallback = callback;
            InetAddress serverAddress = InetAddress.getByName(ip);
            socket = new DatagramSocket();
            socket.setBroadcast(true);
            sendPacket = new DatagramPacket(requestBody.getBody(), requestBody.getBody().length,
                    serverAddress, port);
            mReceiveThread = new ReceiveThread(socket, callback);
            mReceiveThread.start();
        } catch (Exception e) {
            mCallback.sendMessage(Request.MESSAGE_FAILURE, new Error(e.getMessage()));
        }
    }

    public ScheduleRequest(ScheduleRequestBody requestBody, Callback callback) {
        this(Config.getPushIp(), Config.getPushPort(), requestBody, callback);
    }


    @Override
    public void run() {
        try {
            mCallback.sendMessage(Request.MESSAGE_SEND_BEFORE, sendPacket.getData());
            LogUtil.d("send package:" + Utils.logByteArray(sendPacket.getData()));
            socket.send(sendPacket);
            mCallback.sendMessage(Request.MESSAGE_SEND, sendPacket.getData());
        } catch (Exception e) {
            mCallback.sendMessage(Request.MESSAGE_FAILURE, new Error(e.getMessage()));
        }
    }


    public Callback getCallback() {
        return mCallback;
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public byte[] getData() {
        return sendPacket.getData();
    }

    public void setData(byte[] data) {
        sendPacket.setData(data);
    }


    public class ReceiveThread extends Thread {
        private DatagramSocket mSocket;
        private Callback mCallback;

        public ReceiveThread(DatagramSocket socket, Callback callback) {
            mSocket = socket;
            mCallback = callback;
        }

        @Override
        public void run() {
            try {
                if (Looper.myLooper() == null) {
                    Looper.prepare();
                }
                while (true) {
                    DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
                    mSocket.receive(receivePacket);
                    mCallback.sendMessage(Request.MESSAGE_SUCCESS, receivePacket.getData());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
