public class Token {
    public TokenType tokenType;
    public int line;
    public String lexeme;
    public Object literal;

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

    Token(TokenType tokenType, String lexeme, Object literal, int line) {
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