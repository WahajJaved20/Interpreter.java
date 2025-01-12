import java.util.ArrayList;
import java.util.List;


public class Scanner {
    private List<Token> tokens;
    private String sourceFile;
    private int line = 1;
    private int errorCode = 0;
    private int position = 0;

    Scanner(String sourceFile) {
        this.sourceFile = sourceFile;
        tokens = new ArrayList<>();
    }

    private void error(int line, String message, int errorCode) {
        System.err.println("[line " + line + "] Error: " + message);
        this.errorCode = errorCode;
    }

    public int getErrorCode(){
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
        while (!isAtEnd(current) && character != '"') {
            if (character == '\n') line++;
            character = sourceFile.charAt(current);
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

    void printTokens(){
        for(Token token: tokens){
            System.out.println(token);
        }
    }

    public void scanTokens() {
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
                        position --;
                        line++;
                        break;
                    }
                    tokens.add(new Token(TokenType.SLASH, "/", line));
                    break;
                case '"':
                    handleStrings();
                    break;
                case ' ', '\t', '\r': break;
                case '\n':
                    line++;
                    break;
                default:
                    error(line, "Unexpected character: "+ c, 65);
                    break;
            }
        }
        tokens.add(new Token(TokenType.EOF));
    }
}


