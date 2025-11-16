/* Disciplina: Programacao Concorrente */
/* Prof.: Silvana Rossetto */
/* Laboratório: 11 */
/* Codigo: Exemplo de uso de futures (Modificado para Atividade 3) */
/* -------------------------------------------------------------------*/

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.List;

/**
 * Item 2: Nova tarefa que implementa Callable.
 * Esta tarefa conta o número de primos em um intervalo [inicio, fim].
 */
class VerificaPrimoTask implements Callable<Integer> {
    private long inicio;
    private long fim;

    // Construtor que define o intervalo de trabalho da tarefa
    public VerificaPrimoTask(long inicio, long fim) {
        this.inicio = inicio;
        this.fim = fim;
    }

    /**
     * Método auxiliar para verificar a primalidade (da Atividade 1).
     * Usa 'long' para suportar números grandes.
     */
    private boolean ehPrimo(long n) {
        if (n <= 1) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        for (long i = 3; i < Math.sqrt(n) + 1; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }

    /**
     * Método call() executado pela thread do pool.
     * Conta os primos no seu intervalo e retorna a contagem.
     */
    @Override
    public Integer call() throws Exception {
        int contagemLocal = 0;
        for (long i = inicio; i <= fim; i++) {
            if (ehPrimo(i)) {
                contagemLocal++;
            }
        }
        return contagemLocal;
    }
}

/**
 * Classe principal (renomeada para FuturePool conforme Atividade 3).
 * Item 3: Completa o programa para contar primos de 1 a N.
 */
public class FuturePool {
    // Define o intervalo total (N) e o número de threads
    private static final int N = 1000; // Valor "bastante grande"
    private static final int NTHREADS = 10;

    public static void main(String[] args) {
        //cria um pool de threads (NTHREADS)
        ExecutorService executor = Executors.newFixedThreadPool(NTHREADS);
        //cria uma lista para armazenar os "recibos" (Futures)
        List<Future<Integer>> list = new ArrayList<Future<Integer>>();

        // Define o tamanho de cada bloco de trabalho
        long tamanhoBloco = N / NTHREADS;
        
        // Distribui o trabalho (1 a N) entre as NTHREADS
        for (int i = 0; i < NTHREADS; i++) {
            long inicio = (i * tamanhoBloco) + 1;
            long fim = (i + 1) * tamanhoBloco;

            // Garante que o último bloco vá até N
            if (i == NTHREADS - 1) {
                fim = N;
            }

            Callable<Integer> worker = new VerificaPrimoTask(inicio, fim);
            Future<Integer> submit = executor.submit(worker);
            list.add(submit);
        }

        // Recupera os resultados (contagens parciais) e faz o somatório final
        int somaTotalPrimos = 0;
        for (Future<Integer> future : list) {
            try {
                // future.get() bloqueia até que a tarefa específica termine
                somaTotalPrimos += future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        
        System.out.println("Total de primos encontrados (1 a " + N + "): " + somaTotalPrimos);
        executor.shutdown();
    }
}