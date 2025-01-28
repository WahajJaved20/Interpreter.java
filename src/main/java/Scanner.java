import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Character.isDigit;


public class Scanner {
    private List<Token> tokens;
    private final String sourceFile;
    private int line = 1;
    private int errorCode = 0;
    private int position = 0;
    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("and", TokenType.AND);
        keywords.put("class", TokenType.CLASS);
        keywords.put("else", TokenType.ELSE);
        keywords.put("false", TokenType.FALSE);
        keywords.put("for", TokenType.FOR);
        keywords.put("fun", TokenType.FUN);
        keywords.put("if", TokenType.IF);
        keywords.put("nil", TokenType.NIL);
        keywords.put("or", TokenType.OR);
        keywords.put("print", TokenType.PRINT);
        keywords.put("return", TokenType.RETURN);
        keywords.put("super", TokenType.SUPER);
        keywords.put("this", TokenType.THIS);
        keywords.put("true", TokenType.TRUE);
        keywords.put("var", TokenType.VAR);
        keywords.put("while", TokenType.WHILE);
    }

    Scanner(String sourceFile) {
        this.sourceFile = sourceFile;
        tokens = new ArrayList<>();
    }

    private boolean isAlphanumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }

    private void error(int line, String message, int errorCode) {
        System.err.println("[line " + line + "] Error: " + message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    private boolean isAtEnd(int position) {
        return position >= sourceFile.length();
    }

    private boolean match(char c) {
        if (isAtEnd(position + 1)) return false;
        if (sourceFile.charAt(position + 1) != c) return false;
        position++;
        return true;
    }

    private void handleStrings() {
        StringBuilder sequence = new StringBuilder();
        int current = position + 1;
        char character = sourceFile.charAt(current);
        while (!isAtEnd(current)) {
            if (character == '\n') line++;
            character = sourceFile.charAt(current);
            if (character == '"') break;
            sequence.append(character);
            current++;
        }
        if (isAtEnd(current) || character != '"') {
            error(line, "Unterminated string.", 65);
            position = current;
            return;
        }
        position = current;
        tokens.add(new Token(TokenType.STRING, sequence.toString(), sequence.toString(), line));
    }

    private void handleNumbers() {
        StringBuilder sequence = new StringBuilder();
        int current = position;
        char character = sourceFile.charAt(current);
        while (!isAtEnd(current) && isDigit(character)) {
            sequence.append(character);
            if (!isAtEnd(current + 1)) character = sourceFile.charAt(current + 1);
            current++;
            if (character == '.') break;
        }
        if (character == '.') {
            sequence.append(character);
            current++;
            character = sourceFile.charAt(current);
            while (!isAtEnd(current) && isDigit(character)) {
                sequence.append(character);
                if (!isAtEnd(current + 1)) character = sourceFile.charAt(current + 1);
                current++;
            }
            position = current;
        } else {
            position = --current;
        }
        tokens.add(new Token(TokenType.NUMBER, sequence.toString(), String.valueOf(Double.parseDouble(sequence.toString())), line));
    }

    void handleIdentifiers() {
        StringBuilder sequence = new StringBuilder();
        int current = position;
        char character = sourceFile.charAt(current);
        while (!isAtEnd(current) && isAlphanumeric(character)) {
            sequence.append(character);
            if (!isAtEnd(current + 1)) character = sourceFile.charAt(current + 1);
            current++;
        }
        position = --current;
        if (keywords.get(sequence.toString()) != null) {
            tokens.add(new Token(keywords.get(sequence.toString()), sequence.toString(), null, line));
        } else {
            tokens.add(new Token(TokenType.IDENTIFIER, sequence.toString(), null, line));
        }
    }

    void printTokens() {
        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    public List<Token> scanTokens() {
        for (position = 0; !isAtEnd(position); position++) {
            char c = sourceFile.charAt(position);
            switch (c) {
                case '(':
                    tokens.add(new Token(TokenType.LEFT_PAREN, "(", line));
                    break;
                case ')':
                    tokens.add(new Token(TokenType.RIGHT_PAREN, ")", line));
                    break;
                case '{':
                    tokens.add(new Token(TokenType.LEFT_BRACE, "{", line));
                    break;
                case '}':
                    tokens.add(new Token(TokenType.RIGHT_BRACE, "}", line));
                    break;
                case ',':
                    tokens.add(new Token(TokenType.COMMA, ",", line));
                    break;
                case '.':
                    tokens.add(new Token(TokenType.DOT, ".", line));
                    break;
                case '-':
                    tokens.add(new Token(TokenType.MINUS, "-", line));
                    break;
                case '+':
                    tokens.add(new Token(TokenType.PLUS, "+", line));
                    break;
                case ';':
                    tokens.add(new Token(TokenType.SEMICOLON, ";", line));
                    break;
                case '*':
                    tokens.add(new Token(TokenType.STAR, "*", line));
                    break;
                case '!':
                    boolean ignoredBang = match('=') ? tokens.add(new Token(TokenType.BANG_EQUAL, "!=", line)) : tokens.add(new Token(TokenType.BANG, "!", line));
                    break;
                case '=':
                    boolean ignoredEqual = match('=') ? tokens.add(new Token(TokenType.EQUAL_EQUAL, "==", line)) : tokens.add(new Token(TokenType.EQUAL, "=", line));
                    break;
                case '<':
                    boolean ignoredLess = match('=') ? tokens.add(new Token(TokenType.LESS_EQUAL, "<=", line)) : tokens.add(new Token(TokenType.LESS, "<", line));
                    break;
                case '>':
                    boolean ignoredGreater = match('=') ? tokens.add(new Token(TokenType.GREATER_EQUAL, ">=", line)) : tokens.add(new Token(TokenType.GREATER, ">", line));
                    break;
                case '/':
                    if (match('/')) {
                        while (!isAtEnd(position) && c != '\n') {
                            c = sourceFile.charAt(position);
                            position++;
                        }
                        position--;
                        line++;
                        break;
                    }
                    tokens.add(new Token(TokenType.SLASH, "/", line));
                    break;
                case '"':
                    handleStrings();
                    break;
                case ' ', '\t', '\r':
                    break;
                case '\n':
                    line++;
                    break;
                default:
                    if (isDigit(c)) {
                        handleNumbers();
                    } else if (isAlphanumeric(c)) {
                        handleIdentifiers();
                    } else {
                        error(line, "Unexpected character: " + c, 65);
                        break;
                    }
            }
        }
        tokens.add(new Token(TokenType.EOF));
        return tokens;
    }
}


