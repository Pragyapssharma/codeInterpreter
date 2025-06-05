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
        } else if (command.equals("parse")) {
        	String result = parse(fileContents);
            if (result != null) {
                if (result.matches("\\d+(\\.\\d*[1-9])?")) {
                    System.out.println(result.replaceAll("0+$", "").replaceAll("\\.$", ""));
                } else {
                    System.out.println(result);
                }
            }
        } else if (command.equals("evaluate")) {
            Object result = evaluate(fileContents);
            if (result != null) {
            	if (result instanceof Double) {
                    System.out.println(((Double) result).toString().replaceAll("0+$", "").replaceAll("\\.$", ""));
                } else if (result instanceof Boolean) {
                    System.out.println(result.toString().toLowerCase());
                } else if (result.equals("nil")) {
                    System.out.println("nil");
                } else {
                    System.out.println(result.toString());
                }
            }
            if (hasError) {
                System.exit(65);
            } else {
                System.exit(0);
            }
        } else {
            System.err.println("Unknown command: " + command);
            System.exit(1);
        }

        if (hasError) {
            System.exit(65);
        } else {
            System.exit(0);
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
        List<Token> tokens = scanner.scanTokens();
        if (scanner.hadError()) {
            hasError = true;
        }
        if (!scanner.hadError()) {
            tokens.add(new Token(TokenType.EOF, "", null, scanner.getLine()));
        }
        return tokens;
    }

    private static void tokenize(String fileContents) {
        Scanner scanner = new Scanner(fileContents);
        List<Token> tokens = scanner.scanTokens();
        for (Token token : tokens) {
            if (token.type == TokenType.ERROR) {
                System.err.println("[line " + token.line + "] Error: " + token.lexeme);
                hasError = true;
            } else if (token.type != TokenType.EOF) {
                System.out.println(token.type + " " + token.lexeme + " " + (token.literal != null ? token.literal : "null"));
            }
        }
        System.out.println("EOF  null");
        if (scanner.hadError()) {
            hasError = true;
        }
    }
    
    private static String parse(String fileContents) {
        List<Token> tokens = tokenizeAndReturnTokens(fileContents);
        Parser parser = new Parser(tokens);
        try {
            return parser.parse();
        } catch (ParseError e) {
            hasError = true;
            return null;
        }
    }
    
    private static Object evaluate(String fileContents) {
        List<Token> tokens = tokenizeAndReturnTokens(fileContents);
        Parser parser = new Parser(tokens);
        try {
            String ast = parser.parse().trim();
            ast = ast.replaceAll("^group ", "");
            while (ast.startsWith("(") && ast.endsWith(")")) {
                ast = ast.substring(1, ast.length() - 1).trim();
                ast = ast.replaceAll("^group ", "");
            }
            return evaluateAst(ast);
        } catch (ParseError e) {
            hasError = true;
            return null;
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            hasError = true;
            return null;
        }
    }

    private static Object evaluateAst(String ast) {
        ast = ast.trim();
        
        // Preserve group logic but ensure proper processing
        ast = ast.replaceAll("^group ", "");
        while (ast.startsWith("(") && ast.endsWith(")")) {
            ast = ast.substring(1, ast.length() - 1).trim();
            ast = ast.replaceAll("^group ", "");
        }

        // Handle numeric values directly
        if (ast.matches("\\d+(\\.\\d+)?")) {
            return Double.parseDouble(ast);
        }

        // Operator precedence handling: * and /
        List<String> operators = List.of(" * ", " / ");
        for (String op : operators) {
            if (ast.contains(op)) {
                String[] parts = ast.split(" \\" + op.trim() + " ");
                double left = (double) evaluateAst(parts[0].trim());
                double right = (double) evaluateAst(parts[1].trim());

                if (op.equals(" / ") && right == 0) {
                    throw new ArithmeticException("Error: Division by zero");
                }

                return op.equals(" * ") ? left * right : left / right;
            }
        }

        // Handle unary negation (-)
        if (ast.startsWith("-")) {
            double operand = (double) evaluateAst(ast.substring(1).trim());
            return -operand;
        }

        // Handle logical NOT (!)
        if (ast.startsWith("!")) {
            Object operand = evaluateAst(ast.substring(1).trim());
            if (operand instanceof Boolean) {
                return !(Boolean) operand;
            } else if (operand instanceof Double) {
                return ((Double) operand) == 0;
            } else if (operand.equals("nil")) {
                return true;
            } else {
                return true;
            }
        }

        // Handle Boolean literals
        if (ast.equals("true")) {
            return true;
        } else if (ast.equals("false")) {
            return false;
        } else if (ast.equals("nil")) {
            return "nil";
        }

        // Handle quoted strings
        if (ast.startsWith("\"") && ast.endsWith("\"")) {
            return ast.substring(1, ast.length() - 1);
        }

        // Fallback for unrecognized expressions
        return ast;
    }
    
    private static boolean isTruthy(Object obj) {
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        } else if (obj instanceof Double) {
            return ((Double) obj) != 0;
        } else if (obj.equals("nil")) {
            return false;
        } else {
            return true;
        }
    }

    
}