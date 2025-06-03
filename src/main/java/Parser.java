import java.util.List;

class Parser {
    private List<Token> tokens;
    private int current;

    Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.current = 0;
    }

    // Parse expression
    String parse() {
        return expression();
    }

    // Expression -> Term ((PLUS | MINUS) Term)*
    private String expression() {
        String result = term();

        while (match(TokenType.PLUS)) {
            String operator = previous().lexeme;
            String right = term();
            result = "(" + operator + " " + result + " " + right + ")";
        }

        return result;
    }

    // Term -> Factor ((STAR | SLASH) Factor)*
    private String term() {
        String result = factor();

        while (match(TokenType.STAR)) {
            String operator = previous().lexeme;
            String right = factor();
            result = "(" + operator + " " + result + " " + right + ")";
        }

        return result;
    }

    // Factor -> NUMBER | STRING | TRUE | FALSE | NIL | ( expression )
    private String factor() {
        if (match(TokenType.NUMBER, TokenType.STRING)) {
            return previous().literal.toString();
        } else if (match(TokenType.TRUE)) {
            return "true";
        } else if (match(TokenType.FALSE)) {
            return "false";
        } else if (match(TokenType.NIL)) {
            return "nil";
        } else if (match(TokenType.LEFT_PAREN)) {
            String expr = expression();
            consume(TokenType.RIGHT_PAREN);
            return expr;
        }

        // Handle invalid input
        throw new RuntimeException("Invalid input");
    }

    // Helper methods
    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) {
            return false;
        }

        return peek().type == type;
    }

    private Token advance() {
        if (!isAtEnd()) {
            current++;
        }

        return previous();
    }

    private boolean isAtEnd() {
        return peek().type == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private Token consume(TokenType type) {
        if (check(type)) {
            return advance();
        }

        // Handle invalid input
        throw new RuntimeException("Invalid input");
    }
}