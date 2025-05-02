package business.logic;
import data.model.Clients;
import data.model.Queues;
import java.util.List;

public class SelectionStrategy {
      public SelectionStrategy(){}

      public void addClientInQueue(List<Queues> q,List<Clients> clientsWithTheSameArrT){
         clientsWithTheSameArrT.sort((c1, c2) -> {
             if (c1.getArrivalTime() == c2.getArrivalTime()) {
                 return Integer.compare(c2.getServiceTime(), c1.getServiceTime());
                 /*daca e cazul in care au acelasi arrival time, sortez descrescator clientii in functie de service time
                  ca prima data sa ii adaug pe cei cu timpul cel mai mare de asteptare si sa echilibrez cozile cat se poate */
             }
             return Integer.compare(c1.getArrivalTime(), c2.getArrivalTime());
         });

         for(Clients c : clientsWithTheSameArrT){
             /*
              din toti clientii cu acelasi arrival time, selectez clientul si il adaug in coada, si cresc waiting period
              pentru aceasta coada cu timpul de servire al clientului
             */
              Queues bestQ=getBestQ(q);
              bestQ.addClient(c);
             System.out.println("Client with ID " + c.getId() + " is in queue " + bestQ.getIdQ());
              bestQ.waitingPeriod.addAndGet(c.getServiceTime());
         }
}

    private Queues getBestQ(List<Queues> queues){
        Queues bestQ=queues.get(0); //intodeauna prima casa care se va deschide/va fi luata in considerare va fi prima casa
        for(Queues q:queues){
            if(q.waitingPeriod.get()<bestQ.getWaitingPeriod().get()){ //folosesc .get pentru ca nu permite altfel lucurul cu atomic integer
                                                                        //daca waiting time la coada curenta e mai buna decat cea a cozii actuale considerate best, modific acet aspect
                bestQ=q;
            }
        }
        return bestQ;
    }



}

