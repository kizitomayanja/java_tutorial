package ug.insurance.jdbc.threads;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadDemo {

    private Scanner input;

    public ThreadDemo(Scanner scanner){
        this.input = scanner;
    }

    public void demo(){

        Thread worker = new Thread(()->{
            System.out.println("Worker thread is working.");
        });

        worker.start();

        System.out.println("Main thread is working");
    }

    public void runnableDemo() {
        Runnable task = () -> {
            System.out.println("Runnable task is working.");
        };

//        These are actually quite useful.
//        For instance:
//        Runnable fraudCheck = () -> {  //Assuming you have concurrent fraud checking methods to run.
//        ...run fraud checks.
//    }

        Thread worker = new Thread(task);

        worker.start();

        System.out.println("This is main working");
    }

    public void joinDemo() throws InterruptedException {//Todo: Find out what the exception does
        Runnable joinRunnable = () -> {
            System.out.println("All the tasks in here");
        };

        Thread worker = new Thread(joinRunnable);

        worker.start();

        System.out.println("This won't necessarily come after the thread's execution");

        worker.join(); // Now the main thread will wait for this thread to complete execution.

        System.out.println("This will always come after the worker thread is done");

    }

    public void print(String message){
        System.out.println(message);
    }

    private void printMenu(){
        print("====Thread tests====");
        print("""
                1. Basic Demo
                2. Basic Demo with Runnable
                3. Join Demo
                4. Run Race Condition Demo
                5. Same Race Condition but Synchronized
                6. Same Race Condition but with Atomic Integer
                7. Testing thread pool with Executor Service
                0. Exit
                Enter your choice:
                """);
    }

    private static int counter = 0;

    private static AtomicInteger atomicCounter = new AtomicInteger(0);

    private static synchronized void increment(){
        counter++;
    }

    private void executorDemo(){
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        for(int i = 0; i<10; i++){
            int claimId = i;

            executorService.submit(() -> {
                System.out.println("Running Claim " + claimId + " on Thread " + Thread.currentThread().getName());
            });
        }

        executorService.shutdown();//Todo: Try to come up with practical ways to use this. Say, batch processing.
    }

    private void raceConditionDemo() throws InterruptedException {
        // This one's interesting. It worked well for 1000, but 100,000. Things got interesting.
        Runnable task = () -> {
          for(int i = 0; i < 100000; i++){
              counter++;
          }
        };

//        So imagine thread1 read the counter as 10 and incremented it by one.
//        At the same time, imagine thread2 also read the counter as 10.
//        Both increments will move it up to 11 even though they happen twice
//        This is a race condition.
//        Now, checkout the synchronized example.

        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        print("Counter: " + counter);
    }

    private void raceConditionSynchronizedDemo() throws InterruptedException{
// So, synchronized places a lock on the method increment, making it only
// accessible to one thread at a time.
        Runnable task = () -> {
            for(int i = 0; i<100000; i++){
                increment();
            }
        };

        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("Counter: " + counter);
    }

    private void raceConditionWithAtomicIntegerDemo() throws InterruptedException{
        Runnable task = () -> {
            for (int i = 0; i<100000; i++){
                atomicCounter.incrementAndGet();
            }
        };

        Thread thread1 = new Thread(task);
        Thread thread2 = new Thread(task);

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();

        System.out.println("Atomic counter: " + atomicCounter);
    }

    public void testThreads() throws InterruptedException {
        boolean isRunning = true;

        while(isRunning){
            printMenu();

            int choice = input.nextInt();

            switch(choice){
                case 1:
                    demo();
                    break;
                case 2:
                    runnableDemo();
                    break;
                case 3:
                    joinDemo();
                    break;
                case 4:
                    raceConditionDemo();
                    break;
                case 5:
                    raceConditionSynchronizedDemo();
                    break;
                case 6:
                    raceConditionWithAtomicIntegerDemo();
                    break;
                case 7:
                    executorDemo();
                    break;
                case 0:
                    isRunning = false;
                    break;
                default:
                    print("Invalid option");
                    break;
            }
        }
    }
}
