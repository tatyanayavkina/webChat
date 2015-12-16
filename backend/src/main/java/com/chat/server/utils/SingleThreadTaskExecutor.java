package com.chat.server.utils;

import java.util.concurrent.*;

/**
 * Created on 08.12.2015.
 */
public class SingleThreadTaskExecutor {
    private static SingleThreadTaskExecutor instance;
    private BlockingQueue<Runnable> queue;
    private ThreadPoolExecutor threadPoolExecutor;
    private final Object taskAdded;

    private SingleThreadTaskExecutor(){
        this.queue = new LinkedBlockingQueue<>();
        this.threadPoolExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        this.taskAdded = new Object();
    }

    public static SingleThreadTaskExecutor getInstance(){
        if ( instance == null ){
            instance = new SingleThreadTaskExecutor();
            instance.execute();
        }
        return instance;
    }

    public void add(Runnable task){
        queue.add(task);
        synchronized ( taskAdded ){
            taskAdded.notify();
        }
    }

    public void execute(){
        try{
            while( true ){
                Runnable task = null;
                if ( !queue.isEmpty() ){
                    task = queue.take();
                }
                if ( task == null ){
                    synchronized ( taskAdded ){
                        taskAdded.wait();
                    }
                    continue;
                }
                threadPoolExecutor.execute( task );
            }
        } catch (InterruptedException e){
            System.out.println("Exception... in SingleThreadTaskExecutor");
        }

    }
}
