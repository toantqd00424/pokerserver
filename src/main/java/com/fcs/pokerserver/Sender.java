package com.fcs.pokerserver;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;



class MessageQueue 
{	
	public MessageQueue(String topic, String content) {
		super();
		this.topic = topic;
		this.content = content;
	}
	private String topic;
	private String content;
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
public class Sender extends Thread{
	Queue<MessageQueue> queue = new LinkedBlockingDeque<MessageQueue> ();
	MqttClient client;
	
	public Sender(MqttClient client) {
		super();
		this.client = client;
		this.start();
	}

	public void add(String topic,String content)
	{
		queue.add(new MessageQueue(topic, content));
		
		
	}
	
	public void run()
	{
		try {
			while(1==1)
			{
				MessageQueue mq = queue.poll();
				if(mq==null) continue;
				else
				{
					//Thread.sleep(1);
					//System.out.println(mq.getTopic()+":"+mq.getContent());
					client.publish(mq.getTopic(), mq.getContent().getBytes(), 2, false);
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		MqttClient myClient;
		MqttConnectOptions connOpt;

		String BROKER_URL = "tcp://broker.hivemq.com:1883";
		String SERVER_TOPIC = "/pokerserver/server";
		
		connOpt = new MqttConnectOptions();

		connOpt.setCleanSession(true);
		connOpt.setKeepAliveInterval(30);
		

		// Connect to Broker
		try {
			myClient = new MqttClient(BROKER_URL, "pokerserver"+System.currentTimeMillis());
			
			myClient.connect(connOpt);
			Sender sender= new Sender(myClient);
			sender.add("/pokerserver/server", "test1");
			sender.add("/pokerserver/server", "test2");
			sender.add("/pokerserver/server", "test3");
			sender.add("/pokerserver/server", "test4");
			
		} catch (MqttException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}


