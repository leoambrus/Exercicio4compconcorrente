# Implementação e Análise de Pools de Threads em Java

Este repositório contém as implementações desenvolvidas para o Laboratório 11 da disciplina de Programação Concorrente. O objetivo é demonstrar, avaliar e comparar duas abordagens de concorrência em Java: um pool de threads manual (Atividade 1) e a computação assíncrona com `Future`s usando o `ExecutorService` (Atividade 3).

## Conteúdo do Repositório

Este projeto é dividido em 4 arquivos principais:

* **MyPool.java** * A implementação da **Atividade 1**. Este arquivo contém um pool de threads manual, implementado com a classe `FilaTarefas`, que gerencia uma `LinkedList` de tarefas `Runnable` e um conjunto de threads trabalhadoras que consomem essa fila usando `wait()` e `notify()`.

* **FuturePool.java** * A implementação da **Atividade 3**. Este arquivo usa a biblioteca `java.util.concurrent` para demonstrar a computação assíncrona. Ele utiliza um `ExecutorService` (`newFixedThreadPool`) e objetos `Future` para coletar resultados de tarefas `Callable` (a `VerificaPrimoTask`).

* **MyPoollog.java** * Uma versão de **teste em lote** da Atividade 1. Este programa executa o pool manual com múltiplas combinações de threads (T) e repetições (R), redirecionando a saída de cada teste para um arquivo de log (ex: `T8R50.txt`).

* **FuturePoollog.java** * Uma versão de **teste em lote** da Atividade 3. Este programa executa o pool com `Future`s com múltiplas combinações de threads (T) e tamanhos de entrada (N), redirecionando a saída de cada teste para um arquivo de log (ex: `T4N600.txt`).

---

## Como Compilar e Executar

Todos os arquivos podem ser compilados individualmente usando o `javac`. A execução é feita chamando `java` seguido do nome da classe que contém o método `main`.

### 1. Atividade 1 (Pool Manual)

Para compilar e executar a versão base (saída no console): ```bash javac MyPool.java java MyPool ```

Para compilar e executar a versão de teste em lote (saída em arquivos `T<threads>R<repeticoes>.txt`): ```bash javac MyPoollog.java java MyPoollog ```

### 2. Atividade 3 (Pool com `Future`)

Para compilar e executar a versão base (saída no console): ```bash javac FuturePool.java java FuturePool ```

Para compilar e executar a versão de teste em lote (saída em arquivos `T<threads>N<numeros>.txt`): ```bash javac FuturePoollog.java java FuturePoollog ```

---

## Estratégias de Concorrência

### Atividade 1 (MyPool.java)

Utiliza um padrão **Produtor-Consumidor manual**. A `main thread` atua como produtora, adicionando tarefas `Runnable` a uma `LinkedList` compartilhada. As threads trabalhadoras (`MyPoolThreads`) atuam como consumidoras, usando `synchronized`, `wait()` (quando a fila está vazia) e `notify()` (para acordar uma thread) para gerenciar o acesso à fila.

### Atividade 3 (FuturePool.java)

Utiliza o framework **ExecutorService** nativo do Java. O `ExecutorService` gerencia o pool e a fila de tarefas internamente. As tarefas são submetidas como `Callable` (que podem retornar um valor). O método `submit()` retorna um `Future`, que é uma "promessa" de um resultado. A `main thread` pode então chamar `future.get()` para obter o resultado, bloqueando apenas se a tarefa ainda não tiver sido concluída.
