import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Main {

    enum Work {
        WRITE("Запись"), READ("Чтение");
        private String title;

        Work(String title) {
            this.title = title;
        }

        public String getCode() {
            return title;
        }
    }


    public static void main(String[] args) throws InterruptedException {
        ConcurrentHashMap<Integer, Integer> integerConcurrentHashMap = new ConcurrentHashMap<>();
        Map<Integer, Integer> integerMap = Collections.synchronizedMap(new HashMap<>());

        int nThreads = Runtime.getRuntime().availableProcessors();

        List<Integer> integerList = generateIntArray(nThreads * 50, 1000, 100000000);
        System.out.printf("Массив из %d элементов\n", integerList.size());

        System.out.println("Работа с Collections.synchronizedMap(new HashMap<>())");
        // ======= synchronizedMap ЗАПИСЬ =======
        someWork(integerMap, nThreads, integerList, Work.WRITE);
        // ======= synchronizedMap ЧТЕНИЕ =======
        someWork(integerMap, nThreads, integerList, Work.READ);
        System.out.println("Работа с ConcurrentHashMap<>()");
        // ======= ConcurrentHashMap ЗАПИСЬ =======
        someWork(integerConcurrentHashMap, nThreads, integerList, Work.WRITE);
        // ======= ConcurrentHashMap ЧТЕНИЕ =======
        someWork(integerConcurrentHashMap, nThreads, integerList, Work.READ);

    }

    private static void someWork(Map<Integer, Integer> integerMap, int nThreads, List<Integer> integerList, Work work) throws InterruptedException {
        long endTime;
        long startTime;
        List<Thread> threads;
        startTime = System.currentTimeMillis();
        threads = new ArrayList<>();
        for (int i = 0; i < nThreads; i++) {
            int finalI = i;
            Runnable runnable = () -> {
                integerList.subList(nThreads * finalI, nThreads * (finalI + 1)).forEach(integer -> {
                    if (work == Work.WRITE) {
                        integerMap.put(integer, integer);
                    } else {
                        int tmp = integerMap.get(integer);
                    }
                });
            };
            Thread thread = new Thread(runnable);
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        threads.clear();
        endTime = System.currentTimeMillis();
        System.out.printf("%s: %d\n", work.title, endTime - startTime);
    }

    public static List<Integer> generateIntArray(int size, int min, int max) {
        List<Integer> ints = new ArrayList<>();
        Random random = new Random();
        IntStream.range(0, size).forEach(i -> ints.add(random.nextInt((max - min) + 1) + min));
        return ints;
    }
}
