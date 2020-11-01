import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.*;

public class GUI implements ActionListener {
    private static JFrame frame;
    private static JPanel panel;
    private static JTextField target;
    private static JTextField p1;
    private static JTextField p2;
    private static JLabel warning;
    private static JLabel checkbox;
    private static JCheckBox check;
    public static void main(String[] args){
        setup();
    }
    static void setup(){
        //Setup Frame
        frame = new JFrame();
        frame.setMinimumSize(new Dimension(350, 600));
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        panel = new JPanel();
        frame.add(panel);
        panel.setLayout(null);
        //Add JLabel for the target ip to panel
        JLabel targetLabel = new JLabel("Target");
        targetLabel.setBounds(10, 20, 80, 25);
        panel.add(targetLabel);
        //Add JTextField for the target ip to panel
        target = new JTextField("localhost");
        target.setBounds(100, 20, 165, 25);
        panel.add(target);
        //Add JLabel for starting port
        JLabel startPort = new JLabel("Starting port");
        startPort.setBounds(10, 50, 80, 25);
        panel.add(startPort);
        //Add JTextField for starting port
        p1 = new JTextField("1");
        p1.setBounds(100, 50, 50, 25);
        panel.add(p1);
        //Add JLabel for ending port
        JLabel endPort = new JLabel("Ending port");
        endPort.setBounds(160, 50, 80, 25);
        panel.add(endPort);
        //Add JTextField for end port
        p2 = new JTextField("1024");
        p2.setBounds(235, 50, 50, 25);
        panel.add(p2);
        //Add JButton to scan
        JButton button = new JButton("Scan");
        button.setBounds(10, 80, 80, 25);
        button.addActionListener(new GUI());
        panel.add(button);
        //Add empty warning label for exceptions
        warning = new JLabel("");
        warning.setBounds(10, 110, 165, 25);
        panel.add(warning);
        //Add JLabel for checkbox
        checkbox = new JLabel("Only show open ports");
        checkbox.setBounds(100, 80, 125, 25);
        panel.add(checkbox);
        //Add checkbox to display only open ports
        check = new JCheckBox();
        check.setBounds(250, 80, 20, 20);
        panel.add(check);
        frame.pack();
        frame.setVisible(true);
    }
    static void reset(String t, int start, int end){
        frame.add(panel);
        panel.setLayout(null);
        //Add JLabel for the target ip to panel
        JLabel targetLabel = new JLabel("Target");
        targetLabel.setBounds(10, 20, 80, 25);
        panel.add(targetLabel);
        //Add JTextField for the target ip to panel
        target = new JTextField(t);
        target.setBounds(100, 20, 165, 25);
        panel.add(target);
        //Add JLabel for starting port
        JLabel startPort = new JLabel("Starting port");
        startPort.setBounds(10, 50, 80, 25);
        panel.add(startPort);
        //Add JTextField for starting port
        p1 = new JTextField(String.valueOf(start));
        p1.setBounds(100, 50, 50, 25);
        panel.add(p1);
        //Add JLabel for ending port
        JLabel endPort = new JLabel("Ending port");
        endPort.setBounds(160, 50, 80, 25);
        panel.add(endPort);
        //Add JTextField for end port
        p2 = new JTextField(String.valueOf(end));
        p2.setBounds(235, 50, 50, 25);
        panel.add(p2);
        //Add JButton to scan
        JButton button = new JButton("Scan");
        button.setBounds(10, 80, 80, 25);
        button.addActionListener(new GUI());
        panel.add(button);
        //Add empty warning label for exceptions
        warning = new JLabel("");
        warning.setBounds(10, 110, 165, 25);
        panel.add(warning);
        //Add JLabel for checkbox
        checkbox = new JLabel("Only show open ports");
        checkbox.setBounds(100, 80, 125, 25);
        panel.add(checkbox);
        //Add checkbox to display only open ports
        check = new JCheckBox();
        check.setBounds(250, 80, 20, 20);
        panel.add(check);
        frame.pack();
        frame.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        String t = target.getText();
        String start = p1.getText();
        String end = p2.getText();
        if(t == null){
            warning.setText("Target must be a valid ipv4 address");
        }else if(start == null){
            warning.setText("Starting port must be a number");
        }else if(end == null){
            warning.setText("Ending port must be a number");
        }else if(t.equalsIgnoreCase("localhost")){
            try{
                boolean isSelected = check.isSelected();
                panel.removeAll();
                reset(t, Integer.parseInt(start), Integer.parseInt(end));
                check.setSelected(isSelected);
                panel.revalidate();
                panel.repaint();
                scan(t, Integer.parseInt(start), Integer.parseInt(end));
            }catch(IOException error){
                warning.setText("IOException detected");
            }
        }else{
            String[] split = t.split(".");
            if(split.length != 4){
                warning.setText("Target must be a valid ipv4 address");
            }
            for(String s: split){
                if(Integer.parseInt(s) > 255 || Integer.parseInt(s) < 0){
                    warning.setText("Target must be a valid ipv4 address");
                }
            }
            if(Integer.parseInt(split[3]) == 255 || Integer.parseInt(split[3]) == 0){
                warning.setText("Target must be a valid ipv4 address");
            }
            try{
                boolean isSelected = check.isSelected();
                panel.removeAll();
                reset(t, Integer.parseInt(start), Integer.parseInt(end));
                check.setSelected(isSelected);
                panel.revalidate();
                panel.repaint();
                scan(t, Integer.parseInt(start), Integer.parseInt(end));
            }catch(IOException error){
                warning.setText("IOException detected");
            }
        }
    }
    static void scan(String target, int start, int end) throws IOException{
        int y = 140;
        for(int i = start; i <= end; i++){
            try{
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(target, i), 20);
                JLabel label = new JLabel("Port" + i + " is open");
                label.setBounds(10, y, 340, 25);
                panel.add(label);
                frame.revalidate();
                frame.repaint();
                frame.pack();
                frame.setVisible(true);
                y += 30;
            }catch(SocketTimeoutException e){
                if(!check.isSelected()){
                    JLabel label = new JLabel("Port" + i + " is closed, filtered, or not responding");
                    label.setBounds(10, y, 340, 25);
                    panel.add(label);
                    frame.revalidate();
                    frame.repaint();
                    frame.pack();
                    frame.setVisible(true);
                    y += 30;
                }
            }catch(ConnectException e){
                if(!check.isSelected()){
                    JLabel label = new JLabel("Port" + i + " is closed");
                    label.setBounds(10, y, 340, 25);
                    panel.add(label);
                    frame.revalidate();
                    frame.repaint();
                    frame.pack();
                    frame.setVisible(true);
                    y += 30;
                }
            }catch(SocketException e){
                if(!check.isSelected()){
                    JLabel label = new JLabel("Port" + i + " throws permission denied");
                    label.setBounds(10, y, 340, 25);
                    panel.add(label);
                    frame.revalidate();
                    frame.repaint();
                    frame.pack();
                    frame.setVisible(true);
                    y += 30;
                }
            }
        }
        warning.setText("Finished scanning");
    }
}
