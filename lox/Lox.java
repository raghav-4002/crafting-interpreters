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
    public static void main(String[] args) throws IOException
    {
        /* More than 1 args are provided */
        if (args.length > 1)
        {
            System.out.println("Usage: jlox [script]");
            System.exit(64);
        }

        /* Run the script from the file */
        else if (args.length == 1)
        {
            runFile(args[0]);
        }

        /* Start the interactive session */
        else
        {
            runPrompt();
        }
    }

    private static void runFile(String path) throws IOException
    {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
    }

    private static void runPrompt() throws IOException
    {
        /* Creates an input stream?? */
        InputStreamReader input = new InputStreamReader(System.in);

        /* An object that reads from the stream `input` */
        BufferedReader reader = new BufferedReader(input);

        for (;;)
        {
            System.out.println("> ");
            String line = reader.readLine();

            /* If ctrl-d is pressed */
            if (line == null)
            {
                break;
            }

            run(line);
        }
    }

    private static void run(String source)
    {
        /* Create an object of type `Scanner` */
        Scanner scanner = new Scanner(source);

        /* Create a list of type `Token` which has scanned tokens */
        List<Token> tokens = scanner.scanTokens();


        /* For now, just print the tokens */
        for (Token token : tokens)
        {
            System.out.println(token);
        }
    }

    static void error(int line, String message)
    {
        report(line, "", message);
    }

    private static void report(int line, String where,
                                String message)
    {
        System.err.println
        (
            "[line " + line + "] Error" + where + ": " + message
        );

        hadError = true;
    }
}
