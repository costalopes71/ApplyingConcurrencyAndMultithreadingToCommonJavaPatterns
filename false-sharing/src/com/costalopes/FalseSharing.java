package com.costalopes;

/**
 * False sharing eh um problema que acontece em programacao concorrente. Esse problema eh causado por causa de como 
 * os caches do CPU sao organizados. Imagine o seguinte cenario:
 * Thread A precisa da variavel x apenas, e a thread B trabalha apenas com a variavel y, ou seja, Thread A nao toca (nao le e
 * nem escreve) na variavel y nunca, e por sua vez a thread B nao toca na variavel x nunca.
 * Thread A carrega a variavel x da memoria principal e cacheia ela em um dos seus caches,o cache da CPU eh organizado em
 * linhas, e cada uma dessas linhas pode ter 64bytes (ou 8 longs), entao suponhamos que ao carregar a variavel x a thread A
 * tambem carregou a variavel y junto. Digamos agora que a thread B carregou a variavel y e por causa do funcionamento do
 * cache, a variavel x acabou sendo carregada para o cache do cpu tbm. Qdo o valor de x ou y eh modificado um broadcast eh
 * mandado para todos os cpus, marcando aquela linha da variavel que foi incrementada como "suja", e fazendo com que a trhead
 * tenha que ir novamente na memoria principal para carregar essa variavel. Entao, qdo a thread A incrementar o valor de x
 * a linha inteira eh marcada como suja no cache da cpu da thread B, assim sendo, apesar de a thread B nunca precisar do valor
 * de x, qdo ela for usar a variavel y, ela tera que ir novamente ate a memoria principal, pois a linha de cache em que esta
 * a variavel y esta marcada como suja (cache miss). Ou seja, esse problema causa um impacto tremendo de performance =(
 * Para resolver isso podemos usar uma tecnica chamada padding (preenchimento), que consite basicamente em por a variavel
 * da classe cercada por outros longs, para ter certeza que quando ela for carregada para o cache esses outros longs que nao
 * sao usados tambem serao carregados na linha de cache, e como eles nunca serao tocados por nenhuma thread, entao outros cpus
 * que nao precisarem dela nunca carregarao ela =)  
 * @author Jose Paulmard
 * @commentsBy Joao Lopes
 * @implementedBy Joao Lopes
 */
public class FalseSharing {

	public static int NUM_THREADS_MAX = 4;
	public final static long ITERATIONS = 50_000_000L;
	
	private static VolatileLongPadded[] paddedLongs;
	private static VolatileLongUnPadded[] unPaddedLongs;
	
	public final static class VolatileLongPadded {
		
		@SuppressWarnings("unused")
		private long padd1, padd2, padd3, padd4, padd5, padd6;
		public volatile long value = 0L;
		@SuppressWarnings("unused")
		private long padd11, padd12, padd13, padd14, padd15, padd16;
		
	}
	
	public final static class VolatileLongUnPadded {
		public volatile long value = 0L;
	}
	
	static {
		paddedLongs = new VolatileLongPadded[NUM_THREADS_MAX];	
		for (int i = 0; i < paddedLongs.length; i++) {
			paddedLongs[i] = new VolatileLongPadded();
		}
		unPaddedLongs = new VolatileLongUnPadded[NUM_THREADS_MAX];	
		for (int i = 0; i < paddedLongs.length; i++) {
			unPaddedLongs[i] = new VolatileLongUnPadded();
		}
	}
	
	public static void main(final String[] args) throws Exception {
		runBenchmark();
	}

	private static void runBenchmark() throws InterruptedException {
		
		long begin, end;
		
		for (int n = 1; n <= NUM_THREADS_MAX; n++) {
			
			Thread[] threads = new Thread[n];
			
			for (int j = 0; j < threads.length; j++) {
				threads[j] = new Thread(createPaddedRunnable(j));
			}
			
			begin = System.currentTimeMillis();
			for (Thread t : threads) { t.start(); }
			for (Thread t : threads) { t.join(); }
			end = System.currentTimeMillis();
			System.out.printf("		Padded # threads %d - T = %dms\n", n, end - begin);
			
			for (int j = 0; j < threads.length; j++) {
				threads[j] = new Thread(createUnPaddedRunnable(j));
			}
			
			begin = System.currentTimeMillis();
			for (Thread t : threads) { t.start(); }
			for (Thread t : threads) { t.join(); }
			end = System.currentTimeMillis();
			System.out.printf("		UnPadded # threads %d - T = %dms\n\n", n, end - begin);
			
		}
		
	}

	private static Runnable createPaddedRunnable(int j) {
		return () -> {
			long i = ITERATIONS + 1;
			while (0 != --i) {
				paddedLongs[j].value = i;
			}
		};
	}

	private static Runnable createUnPaddedRunnable(final int j) {

		return () -> {
			long i = ITERATIONS + 1;
			while (0 != --i) {
				unPaddedLongs[j].value = i;
			}
		};
	}
	
}
