Análise de Pool de Threads e Computação Assíncrona em Java
Este repositório contém os códigos-fonte desenvolvidos para o Laboratório 11 da disciplina de Programação Concorrente. Os arquivos demonstram duas implementações de concorrência: um pool de threads manual (Atividade 1) e um pool de threads com futuros (Atividade 3), além de suas respectivas versões modificadas para geração de logs de teste.

1. MyPool.java
Propósito: Implementação da Atividade 1 (versão base).

O que faz: Este arquivo contém uma implementação de um pool de threads manual. A classe FilaTarefas gerencia uma LinkedList de tarefas (Runnable) e um conjunto de MyPoolThreads (threads trabalhadoras) que consomem essa fila usando wait() e notify().

Execução: A classe MyPool (main) submete 25 tarefas (Primo) para um pool de 10 threads. A saída é impressa diretamente no console.

Como compilar e executar:

Bash

javac MyPool.java
java MyPool
2. FuturePool.java
Propósito: Implementação da Atividade 3 (versão base).

O que faz: Este arquivo utiliza o ExecutorService nativo do Java para demonstrar computação assíncrona com futuros. A classe VerificaPrimoTask implementa Callable para retornar um valor (a contagem de primos).

Execução: A classe FuturePool (main) divide o trabalho de contar primos de 1 a 1000 entre 10 threads. Ela submete as tarefas (.submit()) e recebe objetos Future, que são usados para coletar os resultados parciais (.get()) e somar o total. A saída é impressa no console.

Como compilar e executar:

Bash

javac FuturePool.java
java FuturePool
3. MyPoollog.java
Propósito: Versão de teste em lote da Atividade 1 (MyPool.java).

O que faz: Este arquivo modifica a implementação do pool manual para realizar testes em escala e gerar logs.

Modificações:

Geração de Log: A main (classe MyPoollog) redireciona toda a saída (System.out) para arquivos de texto.

Identificação de Thread: As threads trabalhadoras agora são nomeadas (ex: "Thread 0", "Thread 1"), e o log de saída inclui qual thread executou qual tarefa.

Testes em Lote: A main executa loops aninhados para testar múltiplas combinações de threads (6, 8, 10) e repetições de tarefas (25, 50, 75, 100).

Execução: O programa não imprime no console. Ele gera 12 arquivos de log (ex: T6R25.txt, T8R50.txt, etc.) contendo os resultados de cada execução.

Como compilar e executar:

Bash

javac MyPoollog.java
java MyPoollog
4. FuturePoollog.java
Propósito: Versão de teste em lote da Atividade 3 (FuturePool.java).

O que faz: Este arquivo modifica a implementação do ExecutorService para realizar testes em escala e gerar logs detalhados.

Modificações:

Geração de Log: A main (classe FuturePoollog) redireciona toda a saída (System.out) para arquivos de texto.

Identificação de Thread: A tarefa VerificaPrimoTask agora imprime qual thread do pool (ex: pool-1-thread-1) está executando seu intervalo e o resultado.

Testes em Lote: A main executa loops aninhados para testar 25 combinações de threads (2, 4, 6, 8, 10) e valores de N (200, 400, 600, 800, 1000).

Execução: O programa imprime o progresso dos testes no console (System.err) e gera 25 arquivos de log (ex: T2N200.txt, T4N600.txt, etc.) com os resultados detalhados de cada execução.

Como compilar e executar:

Bash

javac FuturePoollog.java
java FuturePoollog
