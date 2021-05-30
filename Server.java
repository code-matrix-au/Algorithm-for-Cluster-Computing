
public class Server {

    private String type;
    private String limit;
    private String bootupTime;
    private String hourlyRate;
    private String coreCount;
    private String memory;
    private String disk;

    public Server(String t, String l, String b, String h, String c, String m, String d) {

        type = t;
        limit = l;
        bootupTime = b;
        hourlyRate = h;
        coreCount = c;
        memory = m;
        disk = d;
    }

    public String getType() {

        return type;
    }

    public String getLimit() {

        return limit;
    }

    public String getBootupTime() {

        return bootupTime;
    }

    public String getHourlyRate() {

        return hourlyRate;
    }

    public String getCoreCount() {

        return coreCount;
    }

    public String getMemory() {

        return memory;
    }

    public String getDisk() {

        return disk;
    }

}
