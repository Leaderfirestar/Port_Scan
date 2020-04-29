import java.io.*;
import java.net.*;
import java.rmi.ConnectException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

class PortScan{
    public static void main (String[] args)throws IOException, TimeoutException{
        if (args[0].compareTo("-s") == 0){
            singlePort(args[1], Integer.parseInt(args[2]));
        }
        if (args[0].compareTo("-r") == 0){
            portRange(args[1], args[2]);
        }
    }
    static void singlePort(String target, int port)throws IOException, TimeoutException, ConnectException{
        String succ = String.format("Port %s is open", port);
        String fail = String.format("Port %s is closed", port);
        String filt = String.format("Port %s is filtered or not responding to our probes", port);
        String question = String.format("Port %s throws permission denied", port);
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(target, port), 20); //20 is timeout in ms
            System.out.println(succ);
            socket.close();
        } catch (SocketTimeoutException e){
            System.out.println(filt);
        } catch (ConnectException e){
            System.out.println(fail);
        } catch (SocketException e){
            System.out.println(question);
        }
    }
    static void portRange(String target, String range)throws IOException, TimeoutException, ConnectException{
        String[] domain = range.split("-");
            for (int i = Integer.valueOf(domain[0]); i <= Integer.valueOf(domain[1]); i++){
                try{
                    Socket socket = new Socket();
                    socket.connect(new InetSocketAddress(target, i), 20);
                    String succ = String.format("Port %s is open", i);
                    System.out.println(succ);
                    socket.close();
                } catch (SocketTimeoutException e){
                    String filt = String.format("Port %s is closed, filtered, or not responding to our probes", i);
                    System.out.println(filt);
                } catch (ConnectException e){
                    String fail = String.format("Port %s is closed", i);
                    System.out.println(fail);
                } catch (SocketException e){
                    String question = String.format("Port %s throws permission denied", i);
                    System.out.println(question);
                }
            }
    }
}