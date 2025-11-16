/* Disciplina: Programacao Concorrente */
/* Prof.: Silvana Rossetto */
/* Laboratório: 11 */
/* Codigo: Exemplo de uso de futures (Modificado para Atividade 3 com Logs) */
/* -------------------------------------------------------------------*/

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.ArrayList;
import java.util.List;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.IOException;

class VerificaPrimoTask implements Callable<Integer> {
    private long inicio;
    private long fim;

    public VerificaPrimoTask(long inicio, long fim) {
        this.inicio = inicio;
        this.fim = fim;
    }

    private boolean ehPrimo(long n) {
        if (n <= 1) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        for (long i = 3; i < Math.sqrt(n) + 1; i += 2) {
            if (n % i == 0) return false;
        }
        return true;
    }

    @Override
    public Integer call() throws Exception {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " | Iniciando. Intervalo: " + inicio + " a " + fim);

        int contagemLocal = 0;
        for (long i = inicio; i <= fim; i++) {
            if (ehPrimo(i)) {
                contagemLocal++;
            }
        }
        
        System.out.println(threadName + " | Terminou. Encontrou " + contagemLocal + " primos.");
        return contagemLocal;
    }
}

public class FuturePoollog {
    
    // Arrays de parâmetros para os testes
    private static final int[] THREAD_COUNTS = {2, 4, 6, 8, 10};
    private static final int[] N_VALUES = {200, 400, 600, 800, 1000};

    public static void main(String[] args) {
        
        // Loop externo para iterar sobre o número de threads
        for (int NTHREADS : THREAD_COUNTS) {
            
            // Loop interno para iterar sobre o valor de N
            for (int N : N_VALUES) {
                
                // O nome do arquivo agora é dinâmico para cada teste
                String fileName = "T" + NTHREADS + "N" + N + ".txt";
                
                // Mensagem no console para sabermos qual teste está rodando
                System.err.println("Executando teste: T" + NTHREADS + "N" + N + " -> Salvando em " + fileName);

                try (PrintStream fileOut = new PrintStream(new FileOutputStream(fileName))) {
                    System.setOut(fileOut);
                    
                    System.out.println("Iniciando pool com " + NTHREADS + " threads para N=" + N);

                    ExecutorService executor = Executors.newFixedThreadPool(NTHREADS);
                    List<Future<Integer>> list = new ArrayList<Future<Integer>>();

                    long tamanhoBloco = N / NTHREADS;
                    
                    for (int i = 0; i < NTHREADS; i++) {
                        long inicio = (i * tamanhoBloco) + 1;
                        long fim = (i + 1) * tamanhoBloco;

                        if (i == NTHREADS - 1) {
                            fim = N;
                        }
                        
                        System.out.println("Main | Submetendo tarefa " + i + " (Intervalo: " + inicio + "-" + fim + ")");
                        Callable<Integer> worker = new VerificaPrimoTask(inicio, fim);
                        Future<Integer> submit = executor.submit(worker);
                        list.add(submit);
                    }

                    System.out.println("Main | Todas as " + list.size() + " tarefas foram submetidas.");

                    int somaTotalPrimos = 0;
                    int taskIndex = 0;
                    for (Future<Integer> future : list) {
                        try {
                            System.out.println("Main | Aguardando resultado da tarefa " + taskIndex + "...");
                            int resultadoParcial = future.get(); 
                            System.out.println("Main | Resultado da tarefa " + taskIndex + " recebido: " + resultadoParcial);
                            somaTotalPrimos += resultadoParcial;
                            taskIndex++;
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                    
                    System.out.println("Total de primos encontrados (1 a " + N + "): " + somaTotalPrimos);
                    executor.shutdown();
                    System.out.println("Terminou");

                } catch (IOException e) {
                    // Imprime erros de IO no console (stderr)
                    e.printStackTrace();
                }
            } // Fim do loop N_VALUES
        } // Fim do loop THREAD_COUNTS
        
        System.err.println("Todos os 25 testes foram concluídos.");
    }
}