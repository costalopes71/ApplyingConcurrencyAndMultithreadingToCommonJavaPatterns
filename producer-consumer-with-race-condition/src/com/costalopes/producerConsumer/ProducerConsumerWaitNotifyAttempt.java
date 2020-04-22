package com.costalopes.producerConsumer;

/**
 * Essa implementacao do padrao Producer Consumer esta muito mais correta pois, dessa vez temos os metodos consume() e produce() tambem sincronizados como na classe 
 * anterior <code>ProducerConsumerSynchornizedAttempt</code>, entao podemos contar que nao teremos problemas de Race Condition acontecendo uma vez que as operacoes de
 * escrita e leitura de cada thread estao bloqueadas pelo uso de uma thread de cada vez. E tambem nessa implementacao qdo precisamos aguardar por algo nao precisamos
 * mais fazer isso num bloco while, que causava DeadLock, pois podemos usar o proprio objeto de lock para por a thread corrente no estado de WAITING usando para isso o metodo
 * notify, e tirar as outras threads do estado de WAITING para RUNNABLE usando o metodo notify() para que elas possam continuar seu trabalho.   
 * @author Jose Paumard
 * @implementedBy Joao Lopes
 * @commentsBy Joao Lopes
 */
public class ProducerConsumerWaitNotifyAttempt {

	// objeto comum a todas intancias que fara o papel de lock
	private static Object LOCK = new Object();
	
	private static int[] buffer;
	private static int count;
	
	static class Producer {
		
		void produce() {
			synchronized (LOCK) {
				if (isFull(buffer)) { 
					try {
						LOCK.wait();
					} catch (InterruptedException e) {
						// nao preciso fazer nada
					}
				}
				buffer[count++] = 1;
				LOCK.notify();
			}
		}
		
	}
	
	static class Consumer {
		
		void consume() {
			synchronized (LOCK) {
				if (isEmpty(buffer)) { 
					try {
						LOCK.wait();
					} catch (InterruptedException e) {
						// nao preciso fazer nada
					}
				}
				buffer[--count] = 0;
				LOCK.notify();
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
