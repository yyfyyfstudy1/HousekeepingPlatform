package com.usyd.capstone;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.usyd.capstone.common.component.WebSocketServer;
import com.usyd.capstone.entity.MessageDB;
import com.usyd.capstone.mapper.MessageMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CapstoneApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSocketServerTest {

    private final String URL = "ws://localhost:8082/imserver/";

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private MessageMapper messageMapper;
    private CountDownLatch latch;
    private String receivedMessage;

    @Before
    public void setUp() {
        WebSocketServer.setApplicationContext(applicationContext);
        latch = new CountDownLatch(1);  // Used to wait for a message
    }

    @Test
    public void testOnOpen() throws Exception {
        StandardWebSocketClient client = new StandardWebSocketClient();
        WebSocketConnectionManager manager = new WebSocketConnectionManager(client, new TestWebSocketHandler(), URL + "testUserEmail");
        manager.start();

        boolean awaited = latch.await(5, TimeUnit.SECONDS); // Wait up to 5 seconds for a message

        manager.stop();

        Assert.assertTrue("Did not receive a message", awaited);
        Assert.assertTrue(receivedMessage.contains("\"userEmail\":\"testUserEmail\""));
    }


    @Test
    public void testOnClose() throws Exception {
        StandardWebSocketClient client = new StandardWebSocketClient();

        // Client 1 to observe
        WebSocketConnectionManager manager1 = new WebSocketConnectionManager(client, new TestWebSocketHandler(), URL + "observerEmail");
        manager1.start();

        // Let's ensure it's connected
        Thread.sleep(1000);

        // Client 2 to close
        WebSocketConnectionManager manager2 = new WebSocketConnectionManager(client, new TestWebSocketHandler(), URL + "closerEmail");
        manager2.start();

        // Let's ensure it's connected
        Thread.sleep(1000);

        // Now, close the second connection
        manager2.stop();

        boolean awaited = latch.await(5, TimeUnit.SECONDS); // Wait up to 5 seconds for a message from the observer client

        manager1.stop();

        Assert.assertTrue("Observer did not receive a message", awaited);
    }




    @Test
    public void testOnMessage() throws Exception {
        StandardWebSocketClient client = new StandardWebSocketClient();

        // Client 1 (sender)
        ListenableFuture<WebSocketSession> senderFuture = client.doHandshake(new TestWebSocketHandler(), URL + "senderEmail");
        WebSocketSession senderSession = senderFuture.get();

        // Client 2 (receiver)
        ListenableFuture<WebSocketSession> receiverFuture = client.doHandshake(new TestWebSocketHandler(), URL + "receiverEmail");
        WebSocketSession receiverSession = receiverFuture.get();

        // Let's ensure both are connected
        Thread.sleep(1000);

        // Now, sender sends a message
        JSONObject messageToSend = new JSONObject();
        messageToSend.put("to", "receiverEmail");
        messageToSend.put("text", "Hello, receiver!");

        senderSession.sendMessage(new TextMessage(messageToSend.toString()));

        boolean awaited = latch.await(5, TimeUnit.SECONDS); // Wait up to 5 seconds for a message from the receiver client

        senderSession.close();
        receiverSession.close();

        Assert.assertTrue("Receiver did not receive a message", awaited);

        // Check database for the message
        // to get the latest message.
        QueryWrapper<MessageDB> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("post_time").last("LIMIT 1");
        MessageDB latestMessage = messageMapper.selectOne(wrapper);

        Assert.assertNotNull(latestMessage);
        Assert.assertEquals("Hello, receiver!", latestMessage.getPostMessageContent());
        Assert.assertEquals("senderEmail", latestMessage.getFromUserEmail());
        Assert.assertEquals("receiverEmail", latestMessage.getToUserEmail());
    }


    class TestWebSocketHandler extends TextWebSocketHandler {

        @Override
        public void afterConnectionEstablished(WebSocketSession session) throws Exception {
            // Logic after connection was established
        }

        @Override
        protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
            // Handle received messages
            receivedMessage = message.getPayload();
            latch.countDown();  // Signal that we received a message
        }
    }
}
