import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Expr parse() {
        Token token = tokens.get(current);

        if (token.type == TokenType.TRUE) {
            return new Literal(true);
        }
        if (token.type == TokenType.FALSE) {
            return new Literal(false);
        }
        if (token.type == TokenType.NIL) {
            return new Literal(null);
        }

        throw new ParseError("Unexpected token: " + token.lexeme);
    }
}