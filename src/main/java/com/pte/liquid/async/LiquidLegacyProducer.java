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

import com.pte.liquid.relay.exception.RelayException;
import com.pte.liquid.relay.model.Message;

public class LiquidLegacyProducer{
	
	private final static Logger logger = Logger.getLogger(LiquidLegacyProducer.class);
	
	private LinkedBlockingQueue<Message> queue;
	private int threasHold;
 
	public LiquidLegacyProducer(LinkedBlockingQueue<Message> queue, int threashHold){		
		this.queue = queue;
		this.threasHold = threashHold;
	}

	public void procesMessage(Message message) throws RelayException{
		try {
			if(queue.remainingCapacity() <= threasHold){
				logger.warn("Threashold reached, dumping logging message because volume is to high.");
			}else{
				queue.put(message);
			}						
		} catch (InterruptedException e) {
			throw new RelayException(e);
		}
	}
	
	

}
