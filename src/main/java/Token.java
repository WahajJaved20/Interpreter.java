import lombok.Getter;

public class Token {
    @Getter
    private TokenType tokenType;
    private int line;
    private String lexeme;
    private String literal;

    Token(TokenType tokenType) {
        this.tokenType = tokenType;
        this.lexeme = "";
        this.literal = null;
        this.line = 0;
    }

    Token(TokenType tokenType, String lexeme, int line) {
        this.tokenType = tokenType;
        this.lexeme = lexeme;
        this.literal = null;
        this.line = line;
    }

    Token(TokenType tokenType, String lexeme, String literal, int line) {
        this.tokenType = tokenType;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    public String toString() {
        if (tokenType == TokenType.STRING) {
            return tokenType + " " + '"' + lexeme + '"' + " " + literal;
        } else {
            return tokenType + " " + lexeme + " " + literal;
        }
    }
};