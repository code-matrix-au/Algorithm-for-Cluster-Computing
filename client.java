import java.io.*;
import java.net.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class client {
    private Socket socket = null;
    private BufferedReader in = null;
    private DataOutputStream out = null;
    private static String[] messageArr; // Holds the incoming message in array by separated space " ".
    private static String message; // Holds the entire incoming message as string.

    // Client Commands
    private static final String HELO = "HELO"; // Initial hello to the server
    private static final String AUTH = "AUTH ashwin"; // Authentication detais
    private static final String REDY = "REDY"; // Client is ready.
    private static final String Capable = "Capable"; // Space
    private static final String SPACE = " "; // Space
    private static final String OK = "OK"; // OK
    private static final String GETS = "GETS"; // request for server state information
    private static final String GETSALL = "GETS All"; // request for server state information
    private static final String SCHD = "SCHD"; // actual scheduling decision
    private static final String CNTJ = "CNTJ"; // request job count on a specified server, of a particular job state
    private static final String EJWT = "EJWT"; // request for sum of estimated waiting time
    private static final String LSTJ = "LSTJ"; // request information on running and waiting jobs on a particular
                                               // server
    private static final String PSHJ = "PSHJ"; // request to get next job skipping current job without scheduling
    private static final String MIGJ = "MIGJ"; // request to migrate a job
    private static final String KILJ = "KILJ"; // signal to kill a specific job
    private static final String TERM = "TERM"; // request to terminate a server
    private static final String QUIT = "QUIT"; // request to quit

    private static String serverType; // Server Type
    private static String serverCore; // Server core
    private static int serverID; // Server core

    public client(String address, int port) throws Exception { // Client socket connection
        socket = new Socket(address, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new DataOutputStream(socket.getOutputStream());
    }

    public String[] readmsg() throws Exception { // Read the incoming message

        var str = in.readLine();

        String inputMsg = str.toString();
        message = inputMsg;
        messageArr = inputMsg.split("\\s+"); // split the message by space

        return messageArr;

    }

    public void sendMsg(String msg) throws IOException { // send message to the server

        String m = msg + "\n"; // add new line character on the end of all the messages.
        byte[] message = m.getBytes();
        out.write(message);
        out.flush();

    }

    public static void main(String[] args) {

        try {

            client client = new client("localhost", 50000); // Create client instance

            client.sendMsg(HELO); // send Helo to the server

            client.readmsg(); // read back

            client.sendMsg(AUTH); // send username

            while (client.readmsg()[0] != "nonsence") {

                if (messageArr[0].equals(OK)) {
                    client.sendMsg(REDY);
                }

                switch (messageArr[0]) {
                case "JOBN": // send jobs

                    String jobArr[] = messageArr;

                    client.sendMsg(GETS + SPACE + Capable + SPACE + jobArr[4] + SPACE + jobArr[5] + SPACE + jobArr[6]);

                    client.readmsg();

                    int size = Integer.parseInt(messageArr[1]);
                    String[][] arr = new String[9][size];

                    client.sendMsg(OK);

                    for (int i = 0; i < size; i++) {
                        String temp[] = client.readmsg();
                        for (int j = 0; j < 9; j++) {
                            arr[j][i] = temp[j];

                        }
                    }

                    client.sendMsg(OK);
                    client.readmsg();

                    if (messageArr[0].equals(".")) {

                        client.sendMsg(SCHD + SPACE + jobArr[2] + SPACE + arr[0][0] + SPACE + arr[1][0]);

                    }

                    break;

                case "JOBP":
                    System.out.println("JOBP");

                    break;

                case "JCPL":
                    System.out.println(message);
                    client.sendMsg(REDY);

                    break;

                case "RESF":
                    System.out.println("RESF");

                case "RESR":
                    System.out.println("RESR");

                    break;

                case "NONE": // quit
                    client.sendMsg(QUIT);
                    client.readmsg();
                    client.in.close();
                    client.out.close();
                    client.socket.close();
                    break;
                }

            }

        } catch (

        Exception e) {
            System.out.println(e);
        }
    }
}
