import java.io.*;
import java.net.*;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

public class client {
    private Socket socket = null;
    private BufferedReader in = null;

    private static  DataOutputStream out = null;
    private static String[] messageArr; // Holds the incoming message in array by separated space " ".
    private static String message; // Holds the entire incoming message as string.
    private static HashMap<String, Server> serverList;
    private static HashMap<String, Job> jobList;
    private static HashMap<String, Server> capableServerList;
    private static String [] firstCapableServer;

    // Client Commands
    private static final String HELO = "HELO"; // Initial hello to the server
    private static final String AUTH = "AUTH " + System.getProperty("user.name"); // Authentication detais
    private static final String REDY = "REDY"; // Client is ready.
    private static final String Capable = "Capable"; // request capable surver to run that job.
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

    public client(String address, int port) throws Exception { // Client socket connection
        serverList = new HashMap<String, Server>();
        jobList = new HashMap<String, Job>();
        capableServerList = new HashMap<String, Server>();
        firstCapableServer = new String [9];

        socket = new Socket(address, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new DataOutputStream(socket.getOutputStream());
    }

    public static void loadServerFromFile() {
        try {

            File file = new File("ds-system.xml"); // file location
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            NodeList nodeList = doc.getElementsByTagName("server"); // nodeList is not iterable, so we are using for

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                String type = (node.getAttributes().getNamedItem("type").getTextContent());
                String limit = (node.getAttributes().getNamedItem("limit").getTextContent());
                String bootupTime = (node.getAttributes().getNamedItem("bootupTime").getTextContent());
                String hourlyRate = (node.getAttributes().getNamedItem("hourlyRate").getTextContent());
                String coreCount = (node.getAttributes().getNamedItem("coreCount").getTextContent());
                String memory = (node.getAttributes().getNamedItem("memory").getTextContent());
                String disk = (node.getAttributes().getNamedItem("disk").getTextContent());
                Server server= new Server(type, Integer.parseInt(limit), 
                                                Integer.parseInt(bootupTime),
                                                Integer.parseInt(hourlyRate), 
                                                Integer.parseInt(coreCount), 
                                                Integer.parseInt(memory),
                                                Integer.parseInt(disk));
                serverList.put(type, server);
            }

        } catch (Exception e) {
            // System.out.println(e);
        }
    }

    public String[] readmsg() throws Exception { // Read the incoming message

        var str = in.readLine();

        String inputMsg = str.toString();
        message = inputMsg;
        messageArr = inputMsg.split("\\s+"); // split the message by space

        return messageArr;

    }

    public static  void sendMsg(String msg) throws IOException { // send message to the server

        String m = msg + "\n"; // add new line character on the end of all the messages.
        byte[] message = m.getBytes();
        out.write(message);
        out.flush();

    }
    public static void algorithmLowCost(Job job) throws IOException{

 
        for(Server server:capableServerList.values()){
            if(server.getCoreCount() >= job.getCoreReq() &&
                server.getMemory() >= job.getMemoryReq() &&
                server.getDisk() >= job.getDiskReq() &&
                job.getStartTime() >= job.getRunTime()){
                   // System.out.println("Job by alorithm");
                    client.sendMsg(SCHD + SPACE + job.getJobID() + SPACE + server.getType() + SPACE + server.getServerID());
                    return;
                }
        }
       // System.out.println("Job to the first server");
        
        client.sendMsg(SCHD + SPACE + job.getJobID() + SPACE + firstCapableServer[0] + SPACE + firstCapableServer[1]);

    }

    public static void main(String[] args) {

        try {

            client client = new client("localhost", 50000); // Create client instance

            client.sendMsg(HELO); // send Helo to the server

            client.readmsg(); // read back

            client.sendMsg(AUTH); // send username

            loadServerFromFile(); // load the server list from the file.

            while (client.readmsg()[0] != "nonsence") {

                if (messageArr[0].equals(OK)) {
                    client.sendMsg(REDY);
                }

                switch (messageArr[0]) {
                    case "JOBN": // send jobs

                        Job job = new Job(Integer.parseInt(messageArr[1]),
                                        Integer.parseInt( messageArr[2]), 
                                        Integer.parseInt( messageArr[3]), 
                                        Integer.parseInt(messageArr[4]),  
                                        Integer.parseInt(messageArr[5]),
                                        Integer.parseInt(messageArr[6]));

                        client.sendMsg(GETS + SPACE + Capable + SPACE + job.getCoreReq() + SPACE + job.getMemoryReq()
                                + SPACE + job.getDiskReq());

                        String[] msg = client.readmsg(); // read the data back from gets capable

                        client.sendMsg(OK); // Send Ok to receive the server list
                        for (int i = 0; i < Integer.parseInt(msg[1]); i++) {
                            String[] server = client.readmsg();
                            if( i == 0){
                                firstCapableServer = server;
                            }

                            Server s = new Server(server[0], 
                                 Integer.parseInt(server[1]), 
                                                  server[2],
                                    Integer.parseInt(server[3]),
                                    Integer.parseInt(server[4]),
                                    Integer.parseInt(server[5]), 
                                    Integer.parseInt(server[6]),
                                    Integer.parseInt(server[7]), 
                                    Integer.parseInt(server[8]));
                            capableServerList.put(server[0] + " " + server[1], s);

                        }

                        client.sendMsg(OK);

                        client.readmsg();

                        if (messageArr[0].equals(".")) {
                            algorithmLowCost(job);
                            capableServerList.clear();
                        }

                        break;

                    case "JOBP":
                        // System.out.println("JOBP");

                        break;

                    case "JCPL":
                        // System.out.println(message);
                        client.sendMsg(REDY);

                        break;

                    case "RESF":
                        // System.out.println("RESF");

                    case "RESR":
                        // System.out.println("RESR");

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
            // System.out.println(e);
        }
    }
}
