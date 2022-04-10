import java.util.Scanner;

interface Value {
    <T> T getValue();
}

class VInt implements Value {

    private final Integer value;

    public VInt(int value) {
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    public Integer getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

class VString implements Value {

    private final String value;

    public VString(String value) {
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}

class VBool implements Value {

    private final Boolean value;

    public VBool(Boolean value) {
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    public Boolean getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

public class Runtime {

    private static final Scanner scanner = new Scanner(System.in);

    public static Value lisp_add(Value a, Value b) {
        return new VInt((Integer) a.getValue() + (Integer) b.getValue());
    }

    public static Value lisp_sub(Value a, Value b) {
        return new VInt((Integer) a.getValue() - (Integer) b.getValue());
    }

    public static Value lisp_mul(Value a, Value b) {
        return new VInt((Integer) a.getValue() * (Integer) b.getValue());
    }

    public static Value lisp_div(Value a, Value b) {
        return new VInt((Integer) a.getValue() / (Integer) b.getValue());
    }

    public static Value lisp_inc(Value a) {
        return new VInt((Integer) a.getValue() + 1);
    }

    public static Value lisp_dec(Value a) {
        return new VInt((Integer) a.getValue() - 1);
    }

    public static Value lisp_eq(Value a, Value b) {
        return new VBool(a.getValue().equals(b.getValue()));
    }

    public static Value lisp_or(Value a, Value b) {
        return new VBool(((VBool) a).getValue().booleanValue() || ((VBool) b).getValue().booleanValue());
    }

    public static Value lisp_and(Value a, Value b) {
        return new VBool(((VBool) a).getValue().booleanValue() && ((VBool) b).getValue().booleanValue());
    }

    public static Value lisp_not(Value a) {
        return new VBool(!((VBool) a).getValue().booleanValue());
    }

    public static void lisp_print(Value a) {
        System.out.println(a);
    }

    public static Value lisp_readInt() {
        return new VInt(Integer.parseInt(scanner.next()));
    }

    public static Value lisp_readString() {
        return new VString(scanner.next());
    }

    public static Value lisp_readBool() {
        String ret = scanner.next();
        if (ret.equals("true")) {
            return new VBool(Boolean.TRUE);
        }
        if (ret.equals("false")) {
            return new VBool(Boolean.FALSE);
        }
        return null;
    }
}
