import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    // Parses primary expressions like true, false, nil, and numbers
    Expr parsePrimary() {
        Token token = advance(); // Read the next token

        switch (token.type) {
            case TRUE:
                return new Literal(true);
            case FALSE:
                return new Literal(false);
            case NIL:
                return new Literal(null);
            case NUMBER:
                return new Literal(Double.parseDouble(token.lexeme));
            case STRING:
                return new Literal(token.lexeme);
            default:
                throw new ParseError("Unexpected token: " + token.lexeme);
        }
    }

    // Main parse method
    public Expr parse() {
        return parsePrimary();
    }

    private Token advance() {
        return tokens.get(current++);
    }
}