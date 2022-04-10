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

    public static void lisp_print(Value a) {
        System.out.println(a);
    }

    public void test() {
        lisp_print((new VBool(Boolean.TRUE)).getValue() ? lisp_inc(new VInt(10)) : lisp_dec(new VInt(10)));
    }
}
