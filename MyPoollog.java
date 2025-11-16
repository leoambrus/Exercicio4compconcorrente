/* Disciplina: Programacao Concorrente */
/* Prof.: Silvana Rossetto */
/* Laboratório: 11 */
/* Codigo: Criando um pool de threads em Java */

import java.util.LinkedList;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.IOException;

//-------------------------------------------------------------------------------
// Documentação da classe FilaTarefas
/*
 A classe FilaTarefas implementa um pool de threads com tamanho fixo.
 Ela gerencia uma fila de tarefas (Runnables) e um conjunto de threads 
 trabalhadoras que consomem essas tarefas.
 */
class FilaTarefas {
     /* O número de threads trabalhadoras no pool. */
     private final int nThreads;
     
     /* O vetor que armazena as threads trabalhadoras. */
     private final MyPoolThreads[] threads;
     
     /* A fila (LinkedList) onde as tarefas (Runnable) aguardam execução. */
     private final LinkedList<Runnable> queue;
     
     /* Flag booleana que indica se o pool foi desligado (shutdown). */
     private boolean shutdown;

     /*
      * Construtor. Cria a fila de tarefas e inicializa as threads do pool.
      * param nThreads O número de threads trabalhadoras a serem criadas no pool.
      */
     public FilaTarefas(int nThreads) {
         this.shutdown = false;
         this.nThreads = nThreads;
         queue = new LinkedList<Runnable>();
         threads = new MyPoolThreads[nThreads];
         for (int i = 0; i < nThreads; i++) {
             // Modificação: Dar um nome à thread
             threads[i] = new MyPoolThreads("Thread " + i);
             threads[i].start();
         } 
     }

     /*
      * Adiciona uma nova tarefa (Runnable) à fila de execução.
      * O método é sincronizado no objeto 'queue' para garantir thread-safety.
      * Se o pool estiver em processo de shutdown, a tarefa é ignorada.
      * Notifica uma thread em espera para que a tarefa seja processada.
      *
      * @param r A tarefa (Runnable) a ser executada.
      */
     public void execute(Runnable r) {
         synchronized(queue) {
             if (this.shutdown) return;
             queue.addLast(r);
             queue.notify();
         }
     }
     
     /*
      * Inicia o processo de desligamento (shutdown) do pool de threads.
      * Nenhuma nova tarefa será aceita. As threads terminarão após
      * processarem as tarefas restantes na fila.
      * Este método aguarda (via join) o término de todas as threads.
      */
     public void shutdown() {
         synchronized(queue) {
             this.shutdown = true;
             queue.notifyAll();
         }
         for (int i = 0; i < nThreads; i++) {
           try { threads[i].join(); } catch (InterruptedException e) { return; }
         }
     }

     /*
      * Classe interna que define a thread trabalhadora do pool.
      * Cada thread executa em loop, retirando e executando tarefas da fila.
      */
     private class MyPoolThreads extends Thread {
        
        // Modificação: Construtor para aceitar um nome
        public MyPoolThreads(String name) {
            super(name);
        }

        public void run() {
          Runnable r;
          while (true) {
            synchronized(queue) {
              while (queue.isEmpty() && (!shutdown)) {
                try { queue.wait(); }
                catch (InterruptedException ignored){ }
              }
              if (queue.isEmpty()) return; 
              r = queue.removeFirst();
            }
            try { r.run(); }
            catch (RuntimeException e) {}
          } 
        } 
     } 
}
//-------------------------------------------------------------------------------

//--PASSO 1: cria uma classe que implementa a interface Runnable 
class Hello implements Runnable {
     String msg;
     public Hello(String m) { msg = m; }

     //--metodo executado pela thread
     public void run() {
        // Modificação: Adicionar nome da thread à saída
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " | " + msg);
     }
}

class Primo implements Runnable {
     //...completar implementacao, recebe um numero inteiro positivo e imprime se esse numero eh primo ou nao
     private int numero;

     public Primo(int n) {
         this.numero = n;
     }

     private boolean ehPrimo(int n) {
         int i;
         if (n <= 1) return false;
         if (n == 2) return true;
         if (n % 2 == 0) return false;
         for (i = 3; i < Math.sqrt(n) + 1; i += 2) {
             if (n % i == 0) return false;
         }
         return true;
     }

     public void run() {
         // Modificação: Adicionar nome da thread à saída
         String threadName = Thread.currentThread().getName();
         if (ehPrimo(this.numero)) {
             System.out.println(threadName + " | Resultado: O numero " + this.numero + " eh primo.");
         } else {
             System.out.println(threadName + " | Resultado: O numero " + this.numero + " nao eh primo.");
         }
     }
}

//Classe da aplicação (método main)
class MyPoollog {
     public static void main (String[] args) {

        // Arrays com os números de threads e repetições que queremos testar
        int[] numThreadsArray = {6, 8, 10};
        int[] numRepeticoesArray = {25, 50, 75, 100};

        for (int nThreads : numThreadsArray) {
            for (int nRepeticoes : numRepeticoesArray) {
                String fileName = "T" + nThreads + "R" + nRepeticoes + ".txt";
                try (PrintStream fileOut = new PrintStream(new FileOutputStream(fileName))) {
                    System.setOut(fileOut);

                    //--PASSO 2: cria o pool de threads
                    FilaTarefas pool = new FilaTarefas(nThreads); 

                    //--PASSO 3: dispara a execução dos objetos runnable usando o pool de threads
                    for (int i = 0; i < nRepeticoes; i++) {
                        Runnable primo = new Primo(i);
                        pool.execute(primo);
                    }

                    //--PASSO 4: esperar pelo termino das threads
                    pool.shutdown();
                    System.out.println("Terminou");

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
     }
}
