package data.model;

public class Clients {
    public int id;
    public int arrivalTime;
    public int serviceTime;
    private int startServiceTime;

    public Clients(int id, int arrivalTime, int serviceTime) {
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }




    public void setStartServiceTime(int startServiceTime) {
        this.startServiceTime = startServiceTime;
    }


    public int getArrivalTime(){
        return arrivalTime;
    }

    public int getServiceTime(){
        return serviceTime;
    }

    public int getId(){
        return id;
    }
}
