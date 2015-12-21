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

import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;

import com.pte.liquid.relay.Transport;
import com.pte.liquid.relay.exception.RelayException;
import com.pte.liquid.relay.model.Message;

public class LiquidLegacyConsumer implements Runnable{

	private boolean keepRunning = true;
	private final static Logger logger = Logger.getLogger(LiquidLegacyConsumer.class);
	private LinkedBlockingQueue<Message> queue;	
	private Transport transport;
	private long wait = 1000;
	
	public LiquidLegacyConsumer(LinkedBlockingQueue<Message> queue, Transport transport){
		logger.info("Launching legacy consumer");
		this.queue = queue;
		this.transport = transport;
	}
	
	public void destroy(){
		keepRunning = false;
	}

	public void run() {
		while(keepRunning){
			try {
				while(keepRunning && !queue.isEmpty()){
					Message message = queue.poll();	
					
					if(message != null){
						transport.send(message);					
					}							
				}
				Thread.sleep(wait);
			} catch (InterruptedException e) {
				if(logger.isDebugEnabled()){
					e.printStackTrace();
				}
			} catch (RelayException e) {
				if(logger.isDebugEnabled()){
					e.printStackTrace();
				}			
			}
			
			
			
		}	
		logger.info("Breaking the habit.");
	}

	
}
