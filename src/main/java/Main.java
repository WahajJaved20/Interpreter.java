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
        Scanner scanner = new Scanner(fileContents);
        List<Token> tokens = scanner.scanTokens();
        if(scanner.hadScanningError) System.exit(65);
        Parser parser = new Parser(tokens);
        if (parser.hadError) System.exit(parser.getErrorCode());
        Expr expression = parser.parse();
        if (command.equals("tokenize")) {
            scanner.printTokens();
            System.exit(scanner.getErrorCode());
        } else if (command.equals("parse")) {
            if (parser.hadError) System.exit(parser.getErrorCode());
            System.out.println(new AstPrinter().print(expression));
        } else if(command.equals("evaluate")){
            Interpreter interpreter = new Interpreter();
            interpreter.interpret(expression);
            if(interpreter.hadRuntimeError) System.exit(70);
        }
    }
}
