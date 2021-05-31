
public class Server {

    private String type;
    private int limit;
    private int bootupTime;
    private int hourlyRate;
    private int coreCount;
    private int memory;
    private int disk;
    private int serverID;
    private String state;
    private int startTime;
    private int wJobs;
    private int rJobs;

    public Server(String t, int l, int b, int h, int c, int m, int d) {

        type = t;
        limit = l;
        bootupTime = b;
        hourlyRate = h;
        coreCount = c;
        memory = m;
        disk = d;

    }

    public Server(String t, int i, String s, int startT, int c, int m, int d, int w, int r) {

        type = t;
        serverID = i;
        state = s;
        startTime = startT;
        coreCount = c;
        memory = m;
        disk = d;
        wJobs = w;
        rJobs = r;

    }

    public String getType() {

        return type;
    }

    public int getLimit() {

        return limit;
    }

    public int getBootupTime() {

        return bootupTime;
    }

    public int getHourlyRate() {

        return hourlyRate;
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
