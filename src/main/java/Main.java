import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
  private String readFileContents(String filename) {
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

    if (command.equals("tokenize")) {
      byte[] bytes = Files.readAllBytes(Paths.get(filename));
      String fileContents = (new String(bytes, Charset.defaultCharset()));
      Scanner scanner = new Scanner(fileContents);
      scanner.scanTokens();
      scanner.printTokens();
      System.exit(scanner.getErrorCode());
    }
  }
}
