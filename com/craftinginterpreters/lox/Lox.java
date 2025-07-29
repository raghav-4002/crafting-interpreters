package com.craftinginterpreters.lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox 
{
    static boolean hadError = false;

    public static void main(String[] args) throws IOException
    {
        /* If more than one arguments are provided */
        if (args.length > 1) {
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        }

        /* If a single argument (script) is provided */
        else if (args.length == 1){
            runFile(args[0]);
        } 

        /* If no argument is provided, run the interactive mode */
        else {
            runPrompt();
        }
    }

    /* Running a script */
    private static void runFile(String path) throws IOException
    {
        /* Read the contents of the file in the provided `path` */
        byte[] bytes = Files.readAllBytes(Paths.get(path));

        /* Execute the contents of the file */
        run(new String(bytes, Charset.defaultCharset()));

        /* Indicate an error in the exit code */
        if (hadError) {
            System.exit(65);
        }
    }

    /* Running in an interactive mode */
    private static void runPrompt() throws IOException
    {
        /* Create standard input stream object */
        InputStreamReader input = new InputStreamReader(System.in);

        /* Create an object that can read from the stream `input` */
        BufferedReader reader = new BufferedReader(input);

        for(;;) {
            System.out.print("> ");
            String line = reader.readLine();

            /* If user pressed ctrl-d */
            if (line == null) {
                break;
            }

            run(line);

            /* Reset the flag */
            hadError = false;
        }
    }

    /* The actual runner */
    private static void run(String source)
    {
        Scanner scanner = new Scanner(source);

        /* Store the scanned tokens in a list */
        List<Token> tokens = scanner.scanTokens();

        // For now, just print the tokens
        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    /* ========== Error Handling =========== */

    static void error(int line, String message)
    {
        report(line, "", message);
    }

    private static void report(int line, String where, String message)
    {
        System.err.println(
            "[line " + line + "] Error" + where + ": " + message 
        );

        hadError = true;
    }
}
