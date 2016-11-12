package fr.piroxxi.factorygame.event;

import fr.piroxxi.factorygame.log.Log;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActionEventBus implements Runnable {
    @Log
    private static Logger LOG;

    private Thread eventsQueueDeamon;
    private boolean alive = true;
    private Map<Class<? extends ActionEvent>, List<ActionEventHandler>> handlers;
    private List<ActionEvent> eventsQueue;

    @PostConstruct
    public void initIt() {
        handlers = new HashMap<>();
        eventsQueue = new ArrayList<>();
        eventsQueueDeamon = new Thread(this);
        eventsQueueDeamon.start();
    }

    @PreDestroy
    public void cleanUp() throws Exception {
        this.alive = false;
        synchronized (eventsQueueDeamon) {
            eventsQueueDeamon.notify();
        }
    }

    public <E extends ActionEvent> void on(Class<E> type, ActionEventHandler<E> handler) {
        List<ActionEventHandler> handlers = this.handlers.get(type);
        if (handlers == null) {
            handlers = new ArrayList<>();
            this.handlers.put(type, handlers);
        }
        handlers.add(handler);
    }

    public void notify(ActionEvent event) {
        eventsQueue.add(event);
        synchronized (eventsQueueDeamon) {
            eventsQueueDeamon.notify();
        }
    }

    public void emptyQueue() {
        while (!this.eventsQueue.isEmpty()) {
            synchronized (eventsQueueDeamon) {
                eventsQueueDeamon.notify();
            }
        }
    }


    /**
     * This runs periodically to notify handlers if an event appeard.
     */
    @Override
    public void run() {
        while (this.alive) {
            synchronized (eventsQueueDeamon) {
                while (!this.eventsQueue.isEmpty()) {
                    ActionEvent event = this.eventsQueue.get(0);

                    List<ActionEventHandler> globalListners = this.handlers.get(ActionEvent.class);
                    if (globalListners != null)
                        globalListners.stream().forEach(l -> l.event(event));

                    List<ActionEventHandler> specificListners = this.handlers.get(event.getClass());
                    if (specificListners != null)
                        specificListners.stream().forEach(l -> l.event(event));

                    this.eventsQueue.remove(0);
                }

                try {
                    eventsQueueDeamon.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
