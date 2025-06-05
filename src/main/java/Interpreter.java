public class Interpreter {
    public Object interpret(String ast) {
        // Implement the logic to evaluate the AST
        // For now, let's just handle the literals
        if (ast.equals("true")) {
            return true;
        } else if (ast.equals("false")) {
            return false;
        } else if (ast.equals("nil")) {
            return "nil";
        } else {
            // Handle other expressions
            // You can use a recursive descent parser to evaluate the expression
            // For example, you can use a method to evaluate the addition expression
            // and another method to evaluate the multiplication expression
            // and so on
            return evaluateExpression(ast);
        }
    }

    private Object evaluateExpression(String ast) {
        // Remove the parentheses and the operator
        ast = ast.substring(1, ast.length() - 1);
        int spaceIndex = ast.indexOf(' ');
        String operator = ast.substring(0, spaceIndex);
        String leftOperand = ast.substring(spaceIndex + 1, ast.indexOf(' ', spaceIndex + 1));
        String rightOperand = ast.substring(ast.indexOf(' ', spaceIndex + 1) + 1);

        Object leftValue = getOperandValue(leftOperand);
        Object rightValue = getOperandValue(rightOperand);

        if (operator.equals("+")) {
            return (double) leftValue + (double) rightValue;
        } else if (operator.equals("-")) {
            return (double) leftValue - (double) rightValue;
        } else if (operator.equals("*")) {
            return (double) leftValue * (double) rightValue;
        } else if (operator.equals("/")) {
            return (double) leftValue / (double) rightValue;
        } else if (operator.equals("==")) {
            return leftValue.equals(rightValue);
        } else if (operator.equals("!=")) {
            return !leftValue.equals(rightValue);
        } else {
            throw new RuntimeException("Unsupported operator: " + operator);
        }
    }

    private Object getOperandValue(String operand) {
        if (operand.startsWith("(")) {
            Interpreter interpreter = new Interpreter();
            return interpreter.interpret(operand);
        } else if (operand.equals("true")) {
            return true;
        } else if (operand.equals("false")) {
            return false;
        } else if (operand.equals("nil")) {
            return "nil";
        } else {
            return Double.parseDouble(operand);
        }
    }
}