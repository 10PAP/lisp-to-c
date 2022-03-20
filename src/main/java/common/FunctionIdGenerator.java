package common;

//TODO: починить id генератор

public class FunctionIdGenerator {
    private static long idCounter = 0;
    public static synchronized String createID() {
        return String.valueOf(idCounter++);
    }
    public static synchronized String getID() {return String.valueOf(idCounter);}
}
