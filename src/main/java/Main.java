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
        if (command.equals("tokenize")) {
            scanner.printTokens();
            System.exit(scanner.getErrorCode());
        }else if(command.equals("parse")){
            Parser parser = new Parser(tokens);
            Expr expression = parser.parse();
            if(parser.hadError) return;
            System.out.println(new AstPrinter().print(expression));
        }
    }
}
