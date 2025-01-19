import CUP.Parser;
import Clases.TablaSimbolos;
import java_cup.runtime.Symbol;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import JFLEX.Lexer;
import CUP.sym;
import jflex.exceptions.SilentExit;

public class Main {

    // Tabla de símbolos
    private static final TablaSimbolos tablaSimbolos = new TablaSimbolos();

    public static void main(String[] args) throws SilentExit {
        InputStream originalIn = System.in;

        String lexerFilePath = "src/JFLEX/Lexer.jflex";
        String parserFilePath = "src/CUP/Parser.cup";

        generarLexer(lexerFilePath);
        generarParser(parserFilePath);

        System.setIn(originalIn);

        Scanner scanner = new Scanner(System.in);
        String archivo;

        while (true) {
            System.out.print("Por favor, ingrese la ruta del archivo: ");
            archivo = scanner.nextLine();

            if (Files.exists(Paths.get(archivo)) && Files.isReadable(Paths.get(archivo))) {
                break;
            } else {
                System.err.println("La ruta proporcionada no es válida o el archivo no es accesible. Inténtelo nuevamente.");
            }
        }

        String archivoSalida = "salida.txt";
        String archivoSimbolos = "salida_simbolos.txt";

        try (FileReader reader = new FileReader(archivo);
             BufferedWriter writer = new BufferedWriter(new FileWriter(archivoSalida, false));
             BufferedWriter simbolosWriter = new BufferedWriter(new FileWriter(archivoSimbolos, false))) {

            MultiOutputStream multiOut = new MultiOutputStream(System.out, writer);
            MultiOutputStream multiErr = new MultiOutputStream(System.err, writer);

            PrintStream printStream = new PrintStream(multiOut);
            PrintStream printErr = new PrintStream(multiErr);

            System.setOut(printStream);
            System.setErr(printErr);

            Lexer lexer = new Lexer(reader);
            Symbol token;

            while (true) {
                token = lexer.next_token();
                if (token.sym != 0) {
                    String tokenName = sym.terminalNames[token.sym];
                    int linea = lexer.getLine() + 1;
                    int columna = lexer.getColumn() + 1;

                    String tipo = getTokenType(token.sym);

                    System.out.println("Token: " + tokenName + ", Tipo: " + tipo + ", Línea: " + linea + ", Columna: " + columna);

                    // Agregar token a la tabla de símbolos
                    tablaSimbolos.addToSymbolTable(token, tokenName, tipo, linea, columna);

                } else {
                    break;
                }
            }

            for (TablaSimbolos.SymbolTableEntry entry : tablaSimbolos.getEntries()) {
                simbolosWriter.write(entry.toString());
                simbolosWriter.newLine();
            }

            validateSyntax(archivo);

        } catch (IOException e) {
            System.err.println("Error al leer o escribir archivos: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error durante el análisis léxico o sintáctico: " + e.getMessage());
        }
    }

    private static void validateSyntax(String filePath) {
        try (FileReader reader = new FileReader(filePath)) {
            Lexer lexer = new Lexer(reader);
            lexer.desactivarImpresionErrores();
            Parser parser = new Parser(lexer);
            parser.parse();
            System.out.println("Análisis completado.");
        } catch (Exception e) {
            System.err.println("Error durante el análisis: " + e.getMessage());
        }
    }

    private static String getTokenType(int sym) {
        switch (sym) {
            case CUP.sym.INTEGER:
                return "int";
            case CUP.sym.FLOAT:
                return "float";
            case CUP.sym.BOOL:
                return "bool";
            case CUP.sym.CHAR:
                return "char";
            case CUP.sym.STRING:
                return "string";
            case CUP.sym.IDENTIFIER:
                return "variable/función";
            case CUP.sym.MAIN:
                return "main";
            default:
                return "Sin tipo";
        }
    }

    public static void generarParser(String inputFile) {
        try {
            String[] archivoEntrada = {
                    "-destdir", "src/CUP",
                    "-parser", "Parser",
                    "-symbols", "sym",
                    inputFile
            };

            java_cup.Main.main(archivoEntrada);
            System.out.println("Parser generado exitosamente.");
        } catch (Exception e) {
            System.err.println("Error al generar el parser: " + e.getMessage());
        }
    }

    public static void generarLexer(String inputFile) throws SilentExit {
        String[] archivoEntrada = { inputFile };
        jflex.Main.generate(archivoEntrada);
        System.out.println("Lexer generado exitosamente.");
    }

    /**
     * Clase para redirigir la salida a múltiples flujos.
     */
    static class MultiOutputStream extends OutputStream {
        private final OutputStream console;
        private final Writer fileWriter;

        public MultiOutputStream(OutputStream console, Writer fileWriter) {
            this.console = console;
            this.fileWriter = fileWriter;
        }

        @Override
        public void write(int b) throws IOException {
            console.write(b);
            fileWriter.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            console.write(b, off, len);
            fileWriter.write(new String(b, off, len));
        }

        @Override
        public void flush() throws IOException {
            console.flush();
            fileWriter.flush();
        }

        @Override
        public void close() throws IOException {
            console.close();
            fileWriter.close();
        }
    }

}
