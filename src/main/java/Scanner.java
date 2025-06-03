
import java.util.ArrayList;
import java.util.List;

public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    public Scanner(String source) {
        this.source = source;
    }

    public List<Token> scanTokens() {
        // Implement token scanning logic here
        tokens.add(new Token(TokenType.EOF, "", null));
        return tokens;
    }
}