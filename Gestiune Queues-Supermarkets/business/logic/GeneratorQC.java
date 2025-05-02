package business.logic;

import data.model.Clients;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import java.util.Random;

public class GeneratorQC implements  Runnable {

    //date citite din gui
    public int timeLimit; // timpul maxim de derulare a sesiunii de shopping
    public int maxProcessingTime;//timp maxim de servire per client
    public int minProcessingTime;//timp minim de servire per client
    public int numberOfServers;//numarul de cozi
    public int numberOfClients;//numarul initial de clienti
    private int arrivalMin;
    private int arrivalMax;


    private Scheduler scheduler; //scheduler-ul care trimite clientii la diferite cozi/threads in functie de selectionStrategy
    private List<Clients> generatedClients; //lista de clients generati aleator

    public GeneratorQC(int timeLimit,int arrivalMax,int arrivalMin, int maxProcessingTime, int minProcessingTime, int numberOfServers, int numberOfClients, Scheduler scheduler){
        this.timeLimit = timeLimit;
        this.maxProcessingTime = maxProcessingTime;
        this.minProcessingTime = minProcessingTime;
        this.numberOfServers = numberOfServers;
        this.numberOfClients = numberOfClients;
        this.scheduler = scheduler;
        this.arrivalMin = arrivalMin;
        this.arrivalMax = arrivalMax;

        generatedClients = new ArrayList<>();
        generateNRandomClients(); //generarea aleatoare a clientilor
    }

    private void generateNRandomClients(){ //aloca random clientilor un arrivalTime si un service time, clienti pe care apoi ii stochez intr-o lista cu toti clientii generati
        Random rand = new Random();
        for (int i = 0; i < numberOfClients; i++) {
            int arrivalTime = rand.nextInt(arrivalMax - arrivalMin + 1) + arrivalMin;

            int serviceTime = rand.nextInt(maxProcessingTime - minProcessingTime + 1) + minProcessingTime; //un serviceTime valid intre timpii de in si asteptare
            Clients client = new Clients(i, arrivalTime, serviceTime);
            generatedClients.add(client);
        }
        List<Clients> sortableClients = new ArrayList<>(generatedClients);
        sortableClients.sort(Comparator.comparingInt(Clients::getArrivalTime));
        generatedClients = sortableClients;    }


    @Override
    public void run() {
        int currentTime = 0;
        while (currentTime < timeLimit) {
            List<Clients> toBeAssigned = new ArrayList<>();


            for (Clients client : generatedClients) {
                if (client.getArrivalTime() == currentTime) {
                    toBeAssigned.add(client);
                }
            }

            for (Clients client : toBeAssigned) {
                scheduler.dispatchClient(client);
            }

            generatedClients.removeAll(toBeAssigned);

            try {
                Thread.sleep(1000); // simulare timp real
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            currentTime++;
        }
    }

    public List<Clients> getGeneratedClients() {
        return generatedClients;
  }
}