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
        return equality();
    }

    private String equality() {
        String result = comparison();

        while (match(TokenType.BANG_EQUAL, TokenType.EQUAL_EQUAL)) {
            String operator = previous().lexeme;
            String right = comparison();
            result = "(" + operator + " " + result + " " + right + ")";
        }

        return result;
    }
    
    private String comparison() {
        String result = term();

        while (match(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
            String operator = previous().lexeme;
            String right = term();
            result = "(" + operator + " " + result + " " + right + ")";
        }

        return result;
    }

    // Term -> Factor ((STAR | SLASH) Factor)*
    private String term() {
        String result = factor();

        while (match(TokenType.STAR, TokenType.SLASH)) {
            String operator = previous().lexeme;
            String right = factor();
            result = "(" + operator + " " + result + " " + right + ")";
        }

        return result;
    }

    // Factor -> NUMBER | STRING | TRUE | FALSE | NIL | ( expression )
    private String factor() {
        if (match(TokenType.MINUS)) {
            String expr = factor();
            return "(- " + expr + ")";
        } else if (match(TokenType.BANG)) {
            String expr = factor();
            return "(! " + expr + ")";
        } else if (match(TokenType.NUMBER)) {
            String lexeme = previous().lexeme;
            if (!lexeme.contains(".")) {
                lexeme += ".0";
            }
            return lexeme;
        } else if (match(TokenType.STRING)) {
            String lexeme = previous().lexeme;
            return lexeme.substring(1, lexeme.length() - 1);
        } else if (match(TokenType.TRUE)) {
            return "true";
        } else if (match(TokenType.FALSE)) {
            return "false";
        } else if (match(TokenType.NIL)) {
            return "nil";
        } else if (match(TokenType.LEFT_PAREN)) {
            String expr = expression();  // Parse the inner expression
            if (!match(TokenType.RIGHT_PAREN)) {  // Check for closing parenthesis
                throw new RuntimeException("Expect ')' after expression.");
            }
            return "(group " + expr + ")";
        }

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

    private Token consume(TokenType type, String message) {
        if (check(type)) {
            return advance();
        }

        Token token = peek();
        throw new RuntimeException("Expected " + type + " but got " + token.type + " at line " + message);
    }
}