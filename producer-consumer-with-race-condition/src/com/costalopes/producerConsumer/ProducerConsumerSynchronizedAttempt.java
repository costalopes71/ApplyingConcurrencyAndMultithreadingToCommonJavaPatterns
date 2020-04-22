package com.costalopes.producerConsumer;

/**
 * Essa abordagem para implementar o padrão Producer Consumer consiste em synchronizar o acesso aos metodas consume() e produce() de maneira que não haja
 * problemas de Race Condition (ou seja, diferentes threads tentando ler e escrever ao mesmo tempo, como por exemplo no atributo buffer e no atributo count.
 * Essa implementação falha pois, apesar de os metodos estarem devidamente sincronizados, eles irao causar um deadlock, causado pelo fato de quando o codigo
 * de uma das threads (a thread do Consumer ou a thread do Producer) entrar no while loop, como essa thread detem o lock, nada sera produzido caso a thread
 * que detem o lock seja o producer, e nada sera consumido caso a thread que detem o lock seja o consumer. =(
 * @author Jose Paumard
 * @implementedBy Joao Lopes
 * @commentsBy Joao Lopes 
 *
 */
public class ProducerConsumerSynchronizedAttempt {

	// objeto comum a todas intancias que fara o papel de lock
	private static Object LOCK = new Object();
	
	private static int[] buffer;
	private static int count;
	
	static class Producer {
		
		void produce() {
			synchronized (LOCK) {
				while(isFull(buffer)) { 
					// nao faco nada, preciso esperar alguem consumir
				}
				buffer[count++] = 1;
			}
		}
		
	}
	
	static class Consumer {
		
		void consume() {
			synchronized (LOCK) {
				while(isEmpty(buffer)) { 
					// nao faco nada, preciso esperar alguem produzir
				}
				buffer[--count] = 0;
			}
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
