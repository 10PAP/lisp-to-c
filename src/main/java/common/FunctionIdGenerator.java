package common;

public class FunctionIdGenerator {
    private static long idCounter = 0;
    public static synchronized String createID() {
        return String.valueOf(idCounter++);
    }
}
