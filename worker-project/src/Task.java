import java.io.*;
import java.util.Scanner;

public class Task {
    public final int cpu;
    public final int mem;
    public final int io;

    private static final int CPU_ITER = 10000;
    private static final int MEM_SIZE = 30000;
    private static final int NUM_ARRAYS = 500;
    private static final int IO_SIZE = 30000;
    private static final int NUM_THREADS = 2;

    public Task(int cpu, int mem, int io) throws IllegalArgumentException {
        if (cpu < 0 || cpu > 10 || mem < 0 || mem > 10 || io < 0 || io > 10) {
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
        if (level == 0) return;
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
        if (level == 0) return;
        for (int i = 0; i < NUM_ARRAYS; i++) {
            byte[] load = new byte[level * MEM_SIZE];
            load[0] = 'a';
            load[load.length-1] = 'z';
            byte x = load[0];
        }
    }

    private void loadIO(int level) {
        if (level == 0) return;
        File[] files = new File[level];
        try {
            for(int i = 0; i < level; i++) {
                files[i] = new File(super.toString() + " file " + i);
                if (files[i].createNewFile()) {
                    try {
                        FileWriter writer = new FileWriter(files[i]);
                        for (int j = 0; j < IO_SIZE/10; j++) {
                            writer.write("aaaaaaaaaa");
                        }
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("File already exists!");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < level; i++) {
            try {
                Scanner reader = new Scanner(files[i]);
                reader.nextLine();
                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < level; i++) {
            files[i].delete();
        }
    }

    @Override
    public String toString() {
        return "Task{CPU Load = " + this.cpu + "/10" +
                ", Memory Load = " + this.mem + "/10" +
                ", IO Load = " + this.io + "/10}";
    }
}
