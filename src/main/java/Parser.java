import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Expr parse() {
        if (tokens.get(current).type == TokenType.TRUE) {
            return new Literal(true);
        }
        if (tokens.get(current).type == TokenType.FALSE) {
            return new Literal(false);
        }
        if (tokens.get(current).type == TokenType.NIL) {
            return new Literal(null);
        }

        throw new ParseError("Unexpected token: " + tokens.get(current).lexeme);
    }
}