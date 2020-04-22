package com.costalopes.producerConsumer;

public class ProducerConsumer {

	private static int[] buffer;
	private static int count;
	
	static class Producer {
		
		void produce() {
			while(isFull(buffer)) { 
				// nao faco nada, preciso esperar alguem consumir
			}
			buffer[count++] = 1;
		}
		
	}
	
	static class Consumer {
		
		void consume() {
			while(isEmpty(buffer)) { 
				// nao faco nada, preciso esperar alguem produzir
			}
			buffer[--count] = 0;
		}

		
	}

	private static boolean isEmpty(int[] buffer) {
		return count == 0;
	}
	private static boolean isFull(int[] buffer) {
		return count == buffer.length;
	}
	
	public static void main(String[] args) throws InterruptedException {
		
		buffer = new int[10];
		count = 0;
		
		Producer producer = new Producer();
		Consumer consumer = new Consumer();
		
		Runnable produceTask = () -> {
			
			for (int i = 0; i < 50; i++) {
				producer.produce();
			}
			System.out.println("Done producing...");
		};
		
		Runnable consumerTask = () -> {
			
			for (int i = 0; i < 50; i++) {
				consumer.consume();
			}
			System.out.println("Done consuming...");
		};
		
		Thread producerThread = new Thread(produceTask);
		Thread consumerThread = new Thread(consumerTask);
		
		producerThread.start();
		consumerThread.start();
		
		producerThread.join();
		consumerThread.join();
		
		System.out.println("Should be logic to wait to be 0 elements in the array, but....");
		System.out.println("Elements in the array: " + count);
		
	}
	
}
