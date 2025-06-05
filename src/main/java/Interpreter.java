public class Interpreter {
    public Object interpret(String ast) {
        // Remove the parentheses
        ast = ast.substring(1, ast.length() - 1);

        // Check if the AST is a number literal
        if (ast.matches("\\d+(\\.\\d+)?")) {
            return Double.parseDouble(ast);
        }

        // Check if the AST is a string literal
        if (ast.startsWith("\"") && ast.endsWith("\"")) {
            return ast.substring(1, ast.length() - 1);
        }

        // Check if the AST is a boolean literal
        if (ast.equals("true")) {
            return true;
        } else if (ast.equals("false")) {
            return false;
        }

        // Check if the AST is a nil literal
        if (ast.equals("nil")) {
            return "nil";
        }

        // Handle other expressions
        int spaceIndex = ast.indexOf(' ');
        String operator = ast.substring(0, spaceIndex);
        String leftOperand = ast.substring(spaceIndex + 1, ast.indexOf(' ', spaceIndex + 1));
        String rightOperand = ast.substring(ast.indexOf(' ', spaceIndex + 1) + 1);

        Object leftValue = interpretOperand(leftOperand);
        Object rightValue = interpretOperand(rightOperand);

        if (operator.equals("+")) {
            if (leftValue instanceof Double && rightValue instanceof Double) {
                return (double) leftValue + (double) rightValue;
            }
        }

        // Handle other operators
        throw new RuntimeException("Unsupported operator: " + operator);
    }

    private Object interpretOperand(String operand) {
        if (operand.startsWith("\"") && operand.endsWith("\"")) {
            return operand.substring(1, operand.length() - 1);
        } else if (operand.matches("\\d+(\\.\\d+)?")) {
            return Double.parseDouble(operand);
        } else if (operand.equals("true")) {
            return true;
        } else if (operand.equals("false")) {
            return false;
        } else if (operand.equals("nil")) {
            return "nil";
        } else {
            throw new RuntimeException("Unsupported operand: " + operand);
        }
    }
}