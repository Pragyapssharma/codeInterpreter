import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        System.err.println("Logs from your program will appear here!");

        if (args.length < 2) {
            System.err.println("Usage: ./your_program.sh tokenize <filename>");
            System.exit(1);
        }

        String command = args[0];
        String filename = args[1];

        if (!command.equals("tokenize")) {
            System.err.println("Unknown command: " + command);
            System.exit(1);
        }

        String fileContents = "";
        try {
            fileContents = Files.readString(Path.of(filename));
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
        }

        boolean hasError = false;
        int lineNumber = 1;

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
                        break;
                    } else {
                        String lexeme = fileContents.substring(start, i + 1);
                        String literal = fileContents.substring(start + 1, i);
                        System.out.println("STRING " + lexeme + " " + literal);
                        i++; 
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
}