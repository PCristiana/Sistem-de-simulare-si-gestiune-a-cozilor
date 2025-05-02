package business.logic;

import data.model.Clients;
import data.model.Queues;


import java.util.ArrayList;
import java.util.List;

public class Scheduler {
    private List<Queues> qs;
    private SelectionStrategy selection;
    private int numberServers;



    public Scheduler(int numberServers, SelectionStrategy sel) {
        this.numberServers = numberServers; //no de cozi
        this.selection = sel;
        qs = new ArrayList<>(); //o lista cu toate cozile precizate

        for (int i = 0; i < numberServers; i++) {
            Queues q = new Queues(); //se creaza o coada noua pentru fiecare server
            qs.add(q); //se adauga in list
            Thread t = new Thread(q); //se aloca un nou thread pentru fiecare
            t.start(); //se porneste acel thread creat=> se porneste metoda run din Queues=> procesarea clientilor
        }

    }

    public void dispatchClient(Clients client){
        List<Clients> clientList = new ArrayList<>();
        clientList.add(client);
        selection.addClientInQueue(qs, clientList);

    }

}

