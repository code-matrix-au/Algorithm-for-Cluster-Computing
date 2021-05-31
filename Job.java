public class Job {

    private int startTime;
    private int jobID;
    private int runTime;
    private int coreReq;
    private int memoryReq;
    private int diskReq;

    public Job(int s, int j, int r, int c, int m, int d) {

        startTime = s;
        jobID = j;
        runTime = r;
        coreReq = c;
        memoryReq = m;
        diskReq = d;

    }

    public int getStartTime() {
        return startTime;
    }

    public int getJobID() {
        return jobID;
    }

    public int getRunTime() {
        return runTime;
    }

    public int getCoreReq() {
        return coreReq;
    }

    public int getMemoryReq() {
        return memoryReq;
    }

    public int getDiskReq() {
        return diskReq;
    }

}
