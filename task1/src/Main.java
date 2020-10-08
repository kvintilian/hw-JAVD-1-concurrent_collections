import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        final int CALL_TIMEOUT = 1000;
        final int CALL_COUNT = 60;
        final int OPERATOR_TIMEOUT = 3000;
        final int OPERATOR_COUNT = 15;
        final BlockingQueue<Integer> incomingCalls = new LinkedBlockingQueue<>();

        Runnable ats = () -> {
            int counter = 1;
            int waves = 2;
            while (!Thread.currentThread().isInterrupted() && waves-- != 0) {
                for (int i = 0; i < CALL_COUNT; i++)
                    incomingCalls.add(counter++);
                System.out.println(Thread.currentThread().getName() + ": поступило " + CALL_COUNT + " звонков!");
                try {
                    Thread.sleep(CALL_TIMEOUT);
                } catch (InterruptedException ignored) {
                }
            }
        };

        Thread callStation = new Thread(ats, "АТС");
        callStation.start();

        Runnable operator = () -> {
            Random random = new Random();
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    while (incomingCalls.isEmpty())
                        Thread.sleep(OPERATOR_TIMEOUT);
                    Integer callNum = incomingCalls.take();
                    System.out.println((Thread.currentThread().getName() + " обрабатывает звонок №" + callNum));
                    Thread.sleep(OPERATOR_TIMEOUT - random.nextInt(1000));
                    if (incomingCalls.isEmpty()) return;
                } catch (InterruptedException ignored) {
                }
            }
        };

        List<Thread> operators = new ArrayList<>();
        for (int i = 0; i < OPERATOR_COUNT; i++) {
            Thread operatorThread = new Thread(operator, "Оператор " + (i + 1));
            operators.add(operatorThread);
            operatorThread.start();
        }

        try {
            callStation.join();
            for (Thread operatorThread : operators)
                operatorThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}