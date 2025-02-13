import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class Main {
    private static String readFileContents(String filename) {
        String fileContents = "";
        try {
            fileContents = Files.readString(Path.of(filename));
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
        }
        return fileContents;
    }

    public static void main(String[] args) throws IOException {

        if (args.length < 2) {
            System.err.println("Usage: ./your_program.sh tokenize <filename>");
            System.exit(1);
        }

        String command = args[0];
        String filename = args[1];
        String fileContents = readFileContents(filename);


        if (command.equals("tokenize")) {
            Scanner scanner = new Scanner(fileContents);
            List<Token> tokens = scanner.scanTokens();
            scanner.printTokens();
            System.exit(scanner.getErrorCode());
        } else if (command.equals("parse")) {
            Scanner scanner = new Scanner(fileContents);
            List<Token> tokens = scanner.scanTokens();
            if(scanner.hadScanningError) System.exit(65);
            Parser parser = new Parser(tokens);
            Expr expression = parser.parseExpression();
            if (parser.hadError) System.exit(parser.getErrorCode());
            System.out.println(new AstPrinter().print(expression));
        } else if(command.equals("evaluate")){
            Scanner scanner = new Scanner(fileContents);
            List<Token> tokens = scanner.scanTokens();
            if(scanner.hadScanningError) System.exit(65);
            Parser parser = new Parser(tokens);
            Expr expression = parser.parseExpression();
            if (parser.hadError) System.exit(parser.getErrorCode());
            Interpreter interpreter = new Interpreter();
            interpreter.interpret(expression);
            if(interpreter.hadRuntimeError) System.exit(70);
        } else if(command.equals("run")){
            Scanner scanner = new Scanner(fileContents);
            List<Token> tokens = scanner.scanTokens();
            if(scanner.hadScanningError) System.exit(65);
            Parser parser = new Parser(tokens);
            List<Stmt> statements = List.of();
            try{
                statements = parser.parse();
            }catch (Exception e){
            }
            if (parser.hadError) System.exit(parser.getErrorCode());
            Interpreter interpreter = new Interpreter();
            interpreter.interpret(statements);
            if(interpreter.hadRuntimeError) System.exit(70);
        }
    }
}
