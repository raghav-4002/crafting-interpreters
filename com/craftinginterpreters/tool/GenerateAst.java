package com.craftinginterpreters.tool;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

/**
 * GenerateAst.java
 *
 * Usage:
 *   java com.craftinginterpreters.tool.GenerateAst <output directory>
 *
 * This will produce Expr.java and Stmt.java in the given output directory.
 */
public class GenerateAst {
  public static void main(String[] args) throws IOException {
    if (args.length != 1) {
      System.err.println("Usage: generate_ast <output directory>");
      System.exit(64);
    }
    String outputDir = args[0];

    defineAst(outputDir, "Expr", Arrays.asList(
        "Assign   : String name, Expr value",
        "Binary   : Expr left, Token operator, Expr right",
        "Call     : Expr callee, Token paren, List<Expr> arguments",
        "Get      : Expr object, Token name",
        "Grouping : Expr expression",
        "Literal  : Object value",
        "Logical  : Expr left, Token operator, Expr right",
        "Set      : Expr object, Token name, Expr value",
        "Super    : Token keyword, String method",
        "This     : Token keyword",
        "Unary    : Token operator, Expr right",
        "Variable : String name"
    ));

    defineAst(outputDir, "Stmt", Arrays.asList(
        "Block      : List<Stmt> statements",
        "Class      : String name, Expr.Variable superclass, List<Stmt.Function> methods",
        "Expression : Expr expression",
        "Function   : String name, List<String> params, List<Stmt> body",
        "If         : Expr condition, Stmt thenBranch, Stmt elseBranch",
        "Print      : Expr expression",
        "Return     : Token keyword, Expr value",
        "Var        : String name, Expr initializer",
        "While      : Expr condition, Stmt body"
    ));
  }

  private static void defineAst(String outputDir, String baseName, List<String> types) throws IOException {
    String path = outputDir + "/" + baseName + ".java";
    PrintWriter writer = new PrintWriter(path, "UTF-8");

    writer.println("package com.craftinginterpreters.lox;");
    writer.println();
    writer.println("import java.util.List;");
    writer.println();
    writer.println("abstract class " + baseName + " {");

    // The Visitor interface.
    defineVisitor(writer, baseName, types);

    // The AST classes.
    for (String type : types) {
      String className = type.split(":")[0].trim();
      String fields = type.split(":")[1].trim();
      writer.println();
      writer.println("  static class " + className + " extends " + baseName + " {");

      // Constructor.
      writer.println("    " + className + "(" + fields + ") {");

      // Store parameters in fields.
      String[] fieldList = fields.split(", ");
      for (String field : fieldList) {
        String name = field.split(" ")[1];
        writer.println("      this." + name + " = " + name + ";");
      }
      writer.println("    }");

      // Visitor pattern accept method.
      writer.println();
      writer.println("    @Override");
      writer.println("    <R> R accept(Visitor<R> visitor) {");
      writer.println("      return visitor.visit" + className + baseName + "(this);");
      writer.println("    }");

      // Fields.
      writer.println();
      for (String field : fieldList) {
        writer.println("    final " + field + ";");
      }

      writer.println("  }");
    }

    // The base accept method.
    writer.println();
    writer.println("  abstract <R> R accept(Visitor<R> visitor);");
    writer.println("}");
    writer.close();
  }

  private static void defineVisitor(PrintWriter writer, String baseName, List<String> types) {
    writer.println("  interface Visitor<R> {");

    for (String type : types) {
      String typeName = type.split(":")[0].trim();
      writer.println("    R visit" + typeName + baseName + "(" + typeName + " " + baseName.toLowerCase() + ");");
    }

    writer.println("  }");
  }
}
