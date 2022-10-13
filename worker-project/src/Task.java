public class Task {
    public final int cpu;
    public final int mem;
    public final int io;

    private static final int CPU_ITER = 10000000;
    private static final int MEM_SIZE = 50000000;
    private static final int IO_SIZE = 50000000;
    private static final int NUM_THREADS = 2;

    public Task(int cpu, int mem, int io) throws IllegalArgumentException {
        if (cpu < 1 || cpu > 10 || mem < 1 || mem > 10 || io < 1 || io > 10) {
            throw new IllegalArgumentException(
                    "CPU, Mem, and IO should be between 1 and 10"
            );
        }
        this.cpu = cpu;
        this.mem = mem;
        this.io = io;
    }

    public void run() throws InterruptedException {
        Runnable[] cpuRunners = new Runnable[NUM_THREADS];
        Thread[] cpuThreads = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            cpuRunners[i] = () -> loadCPU(this.cpu);
            cpuThreads[i] = new Thread(cpuRunners[i]);
            cpuThreads[i].start();
        }
//        Runnable cpuRunner = () -> loadCPU(this.cpu);
        Runnable memRunner = () -> loadMem(this.mem);
        Runnable ioRunner = () -> loadIO(this.io);
//        Thread cpuThread = new Thread(cpuRunner);
        Thread memThread =  new Thread(memRunner);
        Thread ioThread = new Thread(ioRunner);
//        cpuThread.start();
        memThread.start();
        ioThread.start();
//        cpuThread.join();
        memThread.join();
        ioThread.join();
        for (int i = 0; i < NUM_THREADS; i++) {
            cpuThreads[i].join();
        }
    }

    private void loadCPU(int level) {
        try {
            for (int i = 0; i < level * CPU_ITER; i++) {
                if (System.currentTimeMillis() % 100 == 0) {
                    Thread.sleep(0);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void loadMem(int level) {
        byte[] load = new byte[MEM_SIZE * level];
        load[0] = 'a';
        load[load.length-1] = 'z';
    }

    private void loadIO(int level) {

    }

    @Override
    public String toString() {
        return "Task{CPU Load = " + this.cpu + "/10" +
                ", Memory Load = " + this.mem + "/10" +
                ", IO Load = " + this.io + "/10}";
    }
}
