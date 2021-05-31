//Self explanatory naming convention
public class Server {

    private String type;
    private int serverID;
    private String state;
    private int startTime;
    private int coreCount;
    private int memory;
    private int disk;
    private int wJobs;
    private int rJobs;


    public Server(String type, int serverID, String state, int startTime, int coreCount, int memory, int disk, int wJobs, int rJobs) {

        this.type = type;
        this.serverID = serverID;
        this.state = state;
        this.startTime = startTime;
        this.coreCount = coreCount;
        this.memory = memory;
        this.disk = disk;
        this.wJobs = wJobs;
        this.rJobs = rJobs;

    }

    public String getType() {

        return type;
    }

    public int getCoreCount() {

        return coreCount;
    }

    public int getMemory() {

        return memory;
    }

    public int getDisk() {

        return disk;
    }

    public int getServerID() {

        return serverID;
    }

    public String getstate() {

        return state;
    }

    public int getstartTime() {

        return startTime;
    }

    public int getwJobs() {

        return wJobs;
    }

    public int getRJobs() {

        return rJobs;
    }

}
