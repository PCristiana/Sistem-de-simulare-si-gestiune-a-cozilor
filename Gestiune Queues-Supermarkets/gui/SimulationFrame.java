package gui;

import business.logic.*;
import data.model.*;
import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimulationFrame extends JFrame {
    private JPanel clientPanel;
    private JPanel queuePanel;
    private JPanel waitingLinePanel;
    private JTextField clientField;
    private JTextField queueField;
    private JTextField timeLimitField;
    private JTextField minArrivalField;
    private JTextField maxArrivalField;
    private JTextField minServiceField;
    private JTextField maxServiceField;
    private JButton startButton;
    private List<JPanel> queues;
    private List<JLabel> waitingClients;
    private List<List<JLabel>> queueClients;
    private List<List<Integer>> serviceTimes;
    private JLabel currentTimeLabel;
    private JLabel averageTimeLabel;
    private int currentTime = 0;

    public SimulationFrame() {
        setTitle("Queue Management Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLayout(new BorderLayout());

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(197, 213, 165));
        leftPanel.setPreferredSize(new Dimension(220, 700));

        clientField = new JTextField("0", 5);
        queueField = new JTextField("0", 5);
        timeLimitField = new JTextField("0", 5);
        minArrivalField = new JTextField("0", 5);
        maxArrivalField = new JTextField("0", 5);
        minServiceField = new JTextField("0", 5);
        maxServiceField = new JTextField("0", 5);

        startButton = new JButton("START");
        startButton.setBackground(new Color(231, 76, 60));
        startButton.setForeground(Color.WHITE);

        addLabeledField(leftPanel, "Number of Clients:", clientField);
        addLabeledField(leftPanel, "Number of Queues:", queueField);
        addLabeledField(leftPanel, "Max Simulation Time:", timeLimitField);
        addLabeledField(leftPanel, "Min Arrival Time:", minArrivalField);
        addLabeledField(leftPanel, "Max Arrival Time:", maxArrivalField);
        addLabeledField(leftPanel, "Min Service Time:", minServiceField);
        addLabeledField(leftPanel, "Max Service Time:", maxServiceField);

        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(startButton);

        queuePanel = new JPanel();
        queuePanel.setLayout(new GridLayout(1, 0, 10, 10));
        queuePanel.setBackground(new Color(245, 242, 232));

        clientPanel = new JPanel();
        clientPanel.setLayout(new BorderLayout());
        clientPanel.setBackground(new Color(255, 255, 255));

        waitingLinePanel = new JPanel();
        waitingLinePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
        waitingLinePanel.setBorder(BorderFactory.createTitledBorder("Waiting Line"));
        waitingLinePanel.setBackground(new Color(214, 234, 248));

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(waitingLinePanel, BorderLayout.NORTH);
        centerPanel.add(queuePanel, BorderLayout.CENTER);

        JPanel timePanel = new JPanel();
        currentTimeLabel = new JLabel("Current Time: 0");
        averageTimeLabel = new JLabel("Average Waiting Time: 0.00");
        timePanel.add(currentTimeLabel);
        timePanel.add(Box.createHorizontalStrut(20));
        timePanel.add(averageTimeLabel);
        add(leftPanel, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);
        add(timePanel, BorderLayout.SOUTH);

        startButton.addActionListener(e -> startSimulation());
    }

    private void addLabeledField(JPanel panel, String labelText, JTextField field) {
        JLabel label = new JLabel(labelText);
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        fieldPanel.setMaximumSize(new Dimension(200, 30));
        fieldPanel.setBackground(panel.getBackground());
        fieldPanel.add(label);
        fieldPanel.add(field);
        panel.add(fieldPanel);
    }

    private void startSimulation() {
        queuePanel.removeAll();
        waitingLinePanel.removeAll();
        currentTime = 0;

        int numClients = Integer.parseInt(clientField.getText());
        int numQueues = Integer.parseInt(queueField.getText());
        int maxTime = Integer.parseInt(timeLimitField.getText());
        int minArrival = Integer.parseInt(minArrivalField.getText());
        int maxArrival = Integer.parseInt(maxArrivalField.getText());
        int minService = Integer.parseInt(minServiceField.getText());
        int maxService = Integer.parseInt(maxServiceField.getText());

        queues = new ArrayList<>();
        queueClients = new ArrayList<>();
        serviceTimes = new ArrayList<>();
        waitingClients = new ArrayList<>();

        Random rand = new Random();

        for (int i = 0; i < numQueues; i++) {
            JPanel queue = new JPanel();
            queue.setLayout(new BoxLayout(queue, BoxLayout.Y_AXIS));
            queue.setBorder(BorderFactory.createTitledBorder("Queue " + (i + 1)));
            queue.setBackground(new Color(245, 242, 232));

            ImageIcon cashierIcon = new ImageIcon("images/cashier.png");
            if (cashierIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                Image resizedCashierImage = cashierIcon.getImage().getScaledInstance(30, 40, Image.SCALE_DEFAULT);
                JLabel cashierLabel = new JLabel(new ImageIcon(resizedCashierImage));
                queue.add(cashierLabel);
            }

            queuePanel.add(queue);
            queues.add(queue);
            queueClients.add(new ArrayList<>());
            serviceTimes.add(new ArrayList<>());
        }

        for (int i = 1; i <= numClients; i++) {
            int arrivalTime = rand.nextInt(maxArrival - minArrival + 1) + minArrival;
            int serviceTime = rand.nextInt(maxService - minService + 1) + minService;

            JPanel clientPanel = new JPanel();
            clientPanel.setLayout(new BoxLayout(clientPanel, BoxLayout.Y_AXIS));
            clientPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel clientLabel = new JLabel("ID: " + i + ", Arr: " + arrivalTime + ", Serv: " + serviceTime);
            clientLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            clientLabel.putClientProperty("arrival", arrivalTime);
            clientLabel.putClientProperty("service", serviceTime);

            ImageIcon clientIcon = new ImageIcon("images/client4.png");
            if (clientIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                Image resizedClientImage = clientIcon.getImage().getScaledInstance(20, 30, Image.SCALE_DEFAULT);
                JLabel clientImageLabel = new JLabel(new ImageIcon(resizedClientImage));
                clientPanel.add(clientImageLabel);
            }

            clientPanel.add(clientLabel);
            waitingClients.add(clientLabel);
            waitingLinePanel.add(clientPanel);
        }

        queuePanel.revalidate();
        queuePanel.repaint();
        waitingLinePanel.revalidate();
        waitingLinePanel.repaint();

        new Thread(() -> {
            List<Integer> waitingTimes = new ArrayList<>();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter("simulation_log.txt"))) {
                writer.write("Simulation Logic: \n");

                writer.write("Time " + currentTime + ": All queues are closed\n");

                while (currentTime <= maxTime) {
                    final int t = currentTime;
                    SwingUtilities.invokeLater(() -> currentTimeLabel.setText("Current Time: " + t));

                    List<JLabel> arrived = new ArrayList<>();
                    for (JLabel client : waitingClients) {
                        int arrTime = (int) client.getClientProperty("arrival");
                        if (arrTime == currentTime) {
                            arrived.add(client);
                        }
                    }

                    writer.write("Time " + currentTime + ":\n");

                    for (int i = 0; i < queues.size(); i++) {
                        JPanel queue = queues.get(i);
                        StringBuilder queueStatus = new StringBuilder("Queue " + (i + 1) + ": ");
                        if (queueClients.get(i).isEmpty()) {
                            queueStatus.append("Closed");
                        } else {
                            queueStatus.append("Clients: ");
                            for (JLabel client : queueClients.get(i)) {
                                queueStatus.append(client.getText()).append(" ");
                            }
                        }
                        writer.write(queueStatus.toString() + "\n");
                    }

                    StringBuilder waitingStatus = new StringBuilder("Waiting Clients: ");
                    for (JLabel client : waitingClients) {
                        waitingStatus.append(client.getText()).append(" ");
                    }
                    writer.write(waitingStatus.toString() + "\n");

                    for (JLabel client : arrived) {
                        int service = (int) client.getClientProperty("service");
                        int minQueue = 0;
                        int minLoad = serviceTimes.get(0).stream().mapToInt(Integer::intValue).sum();
                        for (int i = 1; i < queues.size(); i++) {
                            int load = serviceTimes.get(i).stream().mapToInt(Integer::intValue).sum();
                            if (load < minLoad) {
                                minQueue = i;
                                minLoad = load;
                            }
                        }

                        queueClients.get(minQueue).add(client);
                        serviceTimes.get(minQueue).add(service);
                        queues.get(minQueue).add(client);
                        waitingLinePanel.remove(client.getParent());
                    }

                    for (int i = 0; i < queues.size(); i++) {
                        if (!queueClients.get(i).isEmpty()) {
                            int timeLeft = serviceTimes.get(i).get(0) - 1;
                            serviceTimes.get(i).set(0, timeLeft);
                            JLabel label = queueClients.get(i).get(0);
                            label.setText(label.getText().replaceAll("Serv: \\d+", "Serv: " + timeLeft));
                            if (timeLeft <= 0) {
                                JLabel served = queueClients.get(i).remove(0);
                                queues.get(i).remove(served);
                                serviceTimes.get(i).remove(0);

                                int arrival = (int) served.getClientProperty("arrival");
                                int waiting = currentTime - arrival;
                                waitingTimes.add(waiting);

                                double avg = waitingTimes.stream().mapToInt(Integer::intValue).average().orElse(0.0);
                                SwingUtilities.invokeLater(() -> averageTimeLabel.setText(
                                        String.format("Average Waiting Time: %.2f", avg)));
                            }
                        }
                    }

                    queuePanel.revalidate();
                    queuePanel.repaint();
                    waitingLinePanel.revalidate();
                    waitingLinePanel.repaint();

                    currentTime++;
                    Thread.sleep(1000);
                }

                writer.write("\nFinal Status:\n");
                for (int i = 0; i < queues.size(); i++) {
                    StringBuilder finalQueueStatus = new StringBuilder("Queue " + (i + 1) + ": ");
                    if (queueClients.get(i).isEmpty()) {
                        finalQueueStatus.append("Closed");
                    } else {
                        finalQueueStatus.append("Clients: ");
                        for (JLabel client : queueClients.get(i)) {
                            finalQueueStatus.append(client.getText()).append(" ");
                        }
                    }
                    writer.write(finalQueueStatus.toString() + "\n");
                }

                writer.write("\nSimulation ended.\n");
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimulationFrame().setVisible(true));
    }
}
