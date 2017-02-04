package com.study.client;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * Created by tianyuzhi on 17/1/19.
 */
public class SocketChannelClient {
    public static String printBuffer(ByteBuffer buffer) {
        StringBuilder sb = new StringBuilder(buffer.capacity());
        if (null != buffer) {
            while (buffer.hasRemaining()) {
                //System.out.print((char)buffer.get());
                sb.append((char)buffer.get());
            }
        }
        return sb.toString();

    }

    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 9999));
        ByteBuffer buf = ByteBuffer.allocate(100);

        String newData = "GET / HTTP/1.1\r\n" +
                "Content-Length: 5\r\n" +
                "\r\n12345" +
                "GET / HTTP/1.1\r\n" +
                "Content-Length: 5\r\n" +
                "\r\n12345";

        newData = "GET / HTTP/1.1\r\n" +
                "Content-Length: 5\r\n" +
                "\r\n12345" +
                "GET / HTTP/1.1\r\n";

        buf.put(newData.getBytes("UTF-8"));
        buf.flip();
        buf.mark();
        while (buf.hasRemaining()) {
            socketChannel.write(buf);
        }
        buf.reset();
        System.out.println("send to server:" + printBuffer(buf));

        buf.clear();        // read from server

        int bytesRead = socketChannel.read(buf);
        int zeroCount = 0;
        while (bytesRead != -1 && zeroCount < 10) {
            bytesRead = socketChannel.read(buf);
            if (bytesRead == 0) {
                zeroCount ++;
            }
        }
        buf.flip();
        System.out.println("from server:" + printBuffer(buf));

        buf.clear();

        System.out.println("client done writing");
        socketChannel.close();


    }
}
