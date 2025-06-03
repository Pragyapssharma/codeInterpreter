public class Literal implements Expr {
    final Object value;

    Literal(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value == null ? "nil" : value.toString();
    }
}