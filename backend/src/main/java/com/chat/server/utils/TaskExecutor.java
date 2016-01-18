package com.chat.server.utils;

import java.util.concurrent.*;

/**
 * Created on 08.12.2015.
 */
public class TaskExecutor {
    private static TaskExecutor instance;
    private BlockingQueue<Runnable> queue;
    private MessageThread messageThread;
    private final Object taskAdded;

    private TaskExecutor(){
        this.queue = new LinkedBlockingQueue<>();
        this.messageThread = new MessageThread();
        this.taskAdded = new Object();
    }

    public static TaskExecutor getInstance(){
        if ( instance == null ){
            instance = new TaskExecutor();
            instance.execute();
        }
        return instance;
    }

    public void add(Runnable task){
        queue.add( task );
        synchronized ( taskAdded ){
            taskAdded.notify();
        }
    }

    private void execute(){
        messageThread.start();
    }

    private class MessageThread extends Thread {

        public void run(){
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
                    task.run();
                }
            } catch (InterruptedException e){
                System.out.println("Exception... in TaskExecutor");
            }
        }
    }
}
