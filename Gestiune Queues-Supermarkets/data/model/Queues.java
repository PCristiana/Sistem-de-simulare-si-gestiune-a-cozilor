package data.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Queues implements Runnable {
    private List<Clients> servedClients = new ArrayList<>();


    public final int idQ;
    private static int counter = 0;
    public BlockingQueue<Clients> queue;
    public AtomicInteger waitingPeriod;

    public Queues() {
        this.idQ = counter++; //id unic pentru fiecare coada
        queue = new LinkedBlockingQueue<>();
        waitingPeriod = new AtomicInteger(0);
    }

    public int getIdQ() {
        return idQ;
    }

     public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
     }

    public void addClient(Clients client) {
        queue.add(client);
        waitingPeriod.addAndGet(client.getServiceTime());
    }


    private boolean running = true;
    public void run(){
        while(running || !queue.isEmpty()) {
            try { // de ce nu merge implementarea thread-urilor fara try-catch?
                Clients client = queue.poll(); //blocare thread pana apare un client, cand e adaugat in coada, take() il returneaza si continua
                if(client!=null){
                    client.setStartServiceTime((int) (System.currentTimeMillis() / 1000));
                    servedClients.add(client);
                Thread.sleep(client.getServiceTime()*1000); //simulare timp de servire
                waitingPeriod.addAndGet(-client.getServiceTime()); //se scade din perioada totala de asteptare a cozii
            }else{Thread.sleep(100);} //delay pentru a trata overloading CPU-ul
                } catch (InterruptedException e) {
                System.out.println("Queue is interrupted");
               break;
            }
        }
        System.out.println("Queue-thread stopped");
    }
}
