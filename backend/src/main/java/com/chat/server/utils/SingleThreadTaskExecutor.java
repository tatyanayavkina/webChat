package com.chat.server.utils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

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
        this.threadPoolExecutor = (ThreadPoolExecutor) Executors.newSingleThreadExecutor();
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
                Runnable task = queue.take();
                if ( task == null ){
                    taskAdded.wait();
                    continue;
                }
                threadPoolExecutor.execute( task );
            }
        } catch (InterruptedException e){
            System.out.println("Exception... in SingleThreadTaskExecutor");
        }

    }
}
