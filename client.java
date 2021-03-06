/**\
 * By: Ashiwn Bhanderi
 * ID: 44164971
 * Unit: Comp 3100
 * Deliverables: Reduce server rental cost. 
 */

import java.io.*;
import java.net.*;
import java.util.HashMap;

public class client {
    private Socket socket = null; 
    private BufferedReader in = null;
    private static  DataOutputStream out = null;
    private static String[] messageArr; // Holds the incoming message in array by separated space " ".
    private static HashMap<String, Server> capableServerList; // Store list of capable server/job.
    private static String [] firstCapableServer; // Stores the first capable server/job.

    // Client Commands
    private static final String HELO = "HELO"; // Initial hello to the server
    private static final String AUTH = "AUTH " + System.getProperty("user.name"); // Authentication detais
    private static final String REDY = "REDY"; // Client is ready.
    private static final String Capable = "Capable"; // request capable surver to run that job.
    private static final String SPACE = " "; // Space
    private static final String OK = "OK"; // OK
    private static final String GETS = "GETS"; // request for server state information
    private static final String SCHD = "SCHD"; // actual scheduling decision
    private static final String QUIT = "QUIT"; // request to quit


    public client(String address, int port) throws Exception { // Client socket connection
        capableServerList = new HashMap<String, Server>();
        firstCapableServer = new String [9]; // 9 items to store
        socket = new Socket(address, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new DataOutputStream(socket.getOutputStream());
    }

    public String[] readmsg() throws Exception { // Read the incoming message

        var str = in.readLine();
        String inputMsg = str.toString();
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
 
        for(Server server:capableServerList.values()){  // iterate list of servers and get low cost server
            if(server.getCoreCount() >= job.getCoreReq() &&
                server.getMemory() >= job.getMemoryReq() &&
                server.getDisk() >= job.getDiskReq() &&
                job.getStartTime() >= job.getRunTime()){
                    // if server found, schedule the job and return to calling function.
                    client.sendMsg(SCHD + SPACE + job.getJobID() + SPACE + server.getType() + SPACE + server.getServerID());
                    return;
                }
        }
        // if algorith cannot find the best server, assign job to the first server.
        client.sendMsg(SCHD + SPACE + job.getJobID() + SPACE + firstCapableServer[0] + SPACE + firstCapableServer[1]);

    }

    public static void main(String[] args) {

        try {

            client client = new client("localhost", 50000); // Create client instance

            client.sendMsg(HELO); // send Helo to the server

            client.readmsg(); // read back

            client.sendMsg(AUTH); // send username
       
            while (client.readmsg()[0] != "nonsence") {

                if (messageArr[0].equals(OK)) {
                    client.sendMsg(REDY); // send client is ready to receive data.
                }

                switch (messageArr[0]) {
                    case "JOBN": // We received a job to schedule.

                        /**
                         * create a Job object and store job details.
                         * Convert string to interger
                        */ 
                        Job job = new Job(Integer.parseInt(messageArr[1]),
                                          Integer.parseInt(messageArr[2]), 
                                          Integer.parseInt(messageArr[3]), 
                                          Integer.parseInt(messageArr[4]),  
                                          Integer.parseInt(messageArr[5]),
                                          Integer.parseInt(messageArr[6]));

                        // Request for capable server to run the job reeived.                
                        client.sendMsg(GETS + SPACE 
                                            + Capable 
                                            + SPACE 
                                            + job.getCoreReq() 
                                            + SPACE 
                                            + job.getMemoryReq()
                                            + SPACE 
                                            + job.getDiskReq());
                               
                        String[] msg = client.readmsg(); // read the data back from gets capable

                        client.sendMsg(OK); // Send Ok to receive the server list

                        //add the capable servers to the list
                        for (int i = 0; i < Integer.parseInt(msg[1]); i++) {
                            String[] server = client.readmsg();
                            if( i == 0){
                                firstCapableServer = server; // store the first server
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
                            capableServerList.put(server[0] + " " + server[1], s); // Add to the hashMap

                        }

                        client.sendMsg(OK);// ACK to the server that we have received servers.

                        client.readmsg();

                        if (messageArr[0].equals(".")) { // check if we are ready to schedule
                            algorithmLowCost(job); // call the algorithm function
                            capableServerList.clear(); // clear the list for the next job.
                        }

                        break;

                    case "JCPL":// job complete
                        client.sendMsg(REDY);

                        break;

                    case "NONE": // quit
                        client.sendMsg(QUIT); // send quit
                        client.readmsg();
                        client.in.close();
                        client.out.close();
                        client.socket.close();
                        break;
                }
            }

        } catch (

        Exception e) {
        }
    }
}
