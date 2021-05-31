public class Job {
    // Self explanatory naming convention
    private int startTime;
    private int jobID;
    private int runTime;
    private int coreReq;
    private int memoryReq;
    private int diskReq;

    public Job(int startTime, int jobID, int runTime, int coreReq, int memoryReq, int diskReq) {

        this.startTime = startTime;
        this.jobID = jobID;
        this.runTime = runTime;
        this.coreReq = coreReq;
        this.memoryReq = memoryReq;
        this.diskReq = diskReq;

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
