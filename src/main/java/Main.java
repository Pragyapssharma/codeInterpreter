import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class Main {
	
	private static boolean hasError = false;
    private static int lineNumber = 1;
	
    public static void main(String[] args) {
        System.err.println("Logs from your program will appear here!");

        if (args.length < 2) {
            System.err.println("Usage: ./your_program.sh tokenize <filename>");
            System.exit(1);
        }

        String command = args[0];
        String filename = args[1];

        String fileContents = "";
        try {
            fileContents = Files.readString(Path.of(filename));
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
        }

        if (command.equals("tokenize")) {
            tokenize(fileContents);
            if (hasError) {
                System.exit(65);
            } else {
                System.exit(0);
            }
        } else if (command.equals("parse")) {
            List<Token> tokens = tokenizeAndReturnTokens(fileContents);
            Parser parser = new Parser(tokens);
            String result = parser.parse();
            System.out.println(result);
            if (hasError) {
                System.exit(65);
            } else {
                System.exit(0);
            }
        } else {
            System.err.println("Unknown command: " + command);
            System.exit(1);
        }

        
        final Map<String, String> keywords = Map.ofEntries(
        	    new AbstractMap.SimpleEntry<>("and", "AND"),
        	    new AbstractMap.SimpleEntry<>("class", "CLASS"),
        	    new AbstractMap.SimpleEntry<>("else", "ELSE"),
        	    new AbstractMap.SimpleEntry<>("false", "FALSE"),
        	    new AbstractMap.SimpleEntry<>("for", "FOR"),
        	    new AbstractMap.SimpleEntry<>("fun", "FUN"),
        	    new AbstractMap.SimpleEntry<>("if", "IF"),
        	    new AbstractMap.SimpleEntry<>("nil", "NIL"),
        	    new AbstractMap.SimpleEntry<>("or", "OR"),
        	    new AbstractMap.SimpleEntry<>("print", "PRINT"),
        	    new AbstractMap.SimpleEntry<>("return", "RETURN"),
        	    new AbstractMap.SimpleEntry<>("super", "SUPER"),
        	    new AbstractMap.SimpleEntry<>("this", "THIS"),
        	    new AbstractMap.SimpleEntry<>("true", "TRUE"),
        	    new AbstractMap.SimpleEntry<>("var", "VAR"),
        	    new AbstractMap.SimpleEntry<>("while", "WHILE")
        	);


        for (int i = 0; i < fileContents.length(); i++) {
            char c = fileContents.charAt(i);

            if (c == '\n') {
                lineNumber++;
                continue;
            }

            switch (c) {
                case '=':
                    if (i + 1 < fileContents.length() && fileContents.charAt(i + 1) == '=') {
                        System.out.println("EQUAL_EQUAL == null");
                        i++; 
                    } else {
                        System.out.println("EQUAL = null");
                    }
                    break;
                case '!':
                    if (i + 1 < fileContents.length() && fileContents.charAt(i + 1) == '=') {
                        System.out.println("BANG_EQUAL != null");
                        i++;
                    } else {
                        System.out.println("BANG ! null");
                    }
                    break;
                case '/':
                    if (i + 1 < fileContents.length() && fileContents.charAt(i + 1) == '/') {
                        while (i < fileContents.length() && fileContents.charAt(i) != '\n') {
                            i++;
                        }
                        lineNumber++;
                    } else {
                        System.out.println("SLASH / null");
                    }
                    break;
                case '#':
                case '@':
                case '$':
                case '%':
                    System.err.println("[line " + lineNumber + "] Error: Unexpected character: " + c);
                    hasError = true;
                    break;
                case '+':
                    System.out.println("PLUS + null");
                    break;
                case '"': {
                    int start = i;
                    i++;
                    while (i < fileContents.length() && fileContents.charAt(i) != '"') {
                        if (fileContents.charAt(i) == '\n') {
                            lineNumber++;
                        }
                        i++;
                    }

                    if (i >= fileContents.length()) { 
                        System.err.println("[line " + lineNumber + "] Error: Unterminated string.");
                        hasError = true;
                    } else {
                        String lexeme = fileContents.substring(start, i + 1);
                        String literal = fileContents.substring(start + 1, i);
                        System.out.println("STRING " + lexeme + " " + literal);
                    }
                    break;
                }
                case '(':
                    System.out.println("LEFT_PAREN ( null");
                    break;
                case ')':
                    System.out.println("RIGHT_PAREN ) null");
                    break;
                case '{':
                    System.out.println("LEFT_BRACE { null");
                    break;
                case '}':
                    System.out.println("RIGHT_BRACE } null");
                    break;
                case ',':
                    System.out.println("COMMA , null");
                    break;
                case '.':
                    System.out.println("DOT . null");
                    break;
                case '-':
                    System.out.println("MINUS - null");
                    break;
                case ';':
                    System.out.println("SEMICOLON ; null");
                    break;
                case '*':
                    System.out.println("STAR * null");
                    break;
                case '<':
                    if (i + 1 < fileContents.length() && fileContents.charAt(i + 1) == '=') {
                        System.out.println("LESS_EQUAL <= null");
                        i++; 
                    } else {
                        System.out.println("LESS < null");
                    }
                    break;
                case '>':
                    if (i + 1 < fileContents.length() && fileContents.charAt(i + 1) == '=') {
                        System.out.println("GREATER_EQUAL >= null");
                        i++; 
                    } else {
                        System.out.println("GREATER > null");
                    }
                    break;
                case '0': case '1': case '2': case '3': case '4':
                case '5': case '6': case '7': case '8': case '9': {
                    int start = i;
                    while (i < fileContents.length() && Character.isDigit(fileContents.charAt(i))) {
                        i++;
                    }

                    if (i < fileContents.length() && fileContents.charAt(i) == '.' &&
                        i + 1 < fileContents.length() && Character.isDigit(fileContents.charAt(i + 1))) {
                        i++;
                        while (i < fileContents.length() && Character.isDigit(fileContents.charAt(i))) {
                            i++;
                        }
                    }

                    String lexeme = fileContents.substring(start, i);
                    double literalValue = Double.parseDouble(lexeme);
                    System.out.println("NUMBER " + lexeme + " " + literalValue);

                    i--;
                    break;
                }
                case 'a': case 'b': case 'c': case 'd': case 'e': case 'f':
                case 'g': case 'h': case 'i': case 'j': case 'k': case 'l':
                case 'm': case 'n': case 'o': case 'p': case 'q': case 'r':
                case 's': case 't': case 'u': case 'v': case 'w': case 'x':
                case 'y': case 'z': case 'A': case 'B': case 'C': case 'D':
                case 'E': case 'F': case 'G': case 'H': case 'I': case 'J':
                case 'K': case 'L': case 'M': case 'N': case 'O': case 'P':
                case 'Q': case 'R': case 'S': case 'T': case 'U': case 'V':
                case 'W': case 'X': case 'Y': case 'Z': case '_': {
                    int start = i;
                    while (i < fileContents.length() &&
                        (Character.isLetterOrDigit(fileContents.charAt(i)) || fileContents.charAt(i) == '_')) {
                        i++;
                    }

                    String lexeme = fileContents.substring(start, i);
                    String tokenType = keywords.getOrDefault(lexeme, "IDENTIFIER");
                    System.out.println(tokenType + " " + lexeme + " null");

                    i--;
                    break;
                }
                default:
                    break;
            }
        }

        System.out.println("EOF  null");


        if (hasError) {
            System.exit(65);
        } else {
            System.exit(0);
        }
    }
    
    private static List<Token> tokenizeAndReturnTokens(String fileContents) {
        Scanner scanner = new Scanner(fileContents);
        return scanner.scanTokens();
    }

    private static void tokenize(String fileContents) {
        Scanner scanner = new Scanner(fileContents);
        List<Token> tokens = scanner.scanTokens();
        for (Token token : tokens) {
            if (token.type != TokenType.EOF) {
                System.out.println(token.type + " " + token.lexeme + " " + token.literal);
            } else {
                System.out.println("EOF  null");
            }
        }
    }
}