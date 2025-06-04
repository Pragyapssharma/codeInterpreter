public class Token {
    final TokenType type;
    final String lexeme;
    final Object literal;
	public String line;
	public String column;

    public Token(TokenType type, String lexeme, Object literal, String line, String column) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
        this.column = column;
    }

    @Override
    public String toString() {
        return type + " " + lexeme + " " + (literal != null ? literal : "null") + " " + line + " " + column;
        
    }
}