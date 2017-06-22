//Copyright 2015 Paul Tegelaar
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.
package com.pte.liquid.async;

import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import com.pte.liquid.relay.Marshaller;
import com.pte.liquid.relay.Transport;
import com.pte.liquid.relay.exception.RelayException;
import com.pte.liquid.relay.model.Message;

public class LiquidLegacyTransport implements Transport{
		
	private final static Logger logger = Logger.getLogger("LiquidLegacyTransport");
	private Transport transport;
	private int queueSize = 1000;
	private int threshholdSize = 100;
	private final LinkedBlockingQueue<Message> queue = new LinkedBlockingQueue<Message>(queueSize);
	private LiquidLegacyConsumer consumer;
	private LiquidLegacyProducer producer;
	
	public LiquidLegacyTransport(Transport transport){
		logger.info("Creating new async transport");
		this.transport = transport;
		consumer = new LiquidLegacyConsumer(queue, transport);
		producer = new LiquidLegacyProducer(queue, threshholdSize);
		new Thread(consumer).start();
	}

	public synchronized void send(Message msg) throws RelayException {
		producer.procesMessage(msg);
	}

	public void setMarshaller(Marshaller marshaller) {
		transport.setMarshaller(marshaller);		
	}

	public void setProperties(Properties properties) {
		transport.setProperties(properties);		
	}

	public void destroy() {
		consumer.destroy();		
	}

	public int getQueueSize() {
		return queueSize;
	}

	public void setQueueSize(int queueSize) {
		this.queueSize = queueSize;
	}

	public int getThreshholdSize() {
		return threshholdSize;
	}

	public void setThreshholdSize(int threshholdSize) {
		this.threshholdSize = threshholdSize;
	}
	
	
	
	
	

}
