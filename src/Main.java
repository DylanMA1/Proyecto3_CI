import CUP.Parser;
import Clases.TablaSimbolos;
import Clases.TipoDatos;
import Clases.Simbolo;
import java_cup.runtime.Symbol;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import JFLEX.Lexer;
import CUP.sym;
import jflex.exceptions.SilentExit;

public class Main {

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

            Parser parser = new Parser(lexer);

            while (true) {
                token = lexer.next_token();
                if (token.sym != 0) {
                    String tokenName = sym.terminalNames[token.sym];
                    int linea = lexer.getLine() + 1;
                    int columna = lexer.getColumn() + 1;

                    TipoDatos tipo = getTokenType(token.sym);

                    System.out.println("Token: " + tokenName + ", Tipo: " + tipo + ", Línea: " + linea + ", Columna: " + columna);

                } else {
                    break;
                }
            }

            // Escribir la tabla de símbolos en el archivo de salida
            for (Simbolo simbolo : parser.tablaSimbolos.getEntries()) {
                simbolosWriter.write(simbolo.toString());
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
            parser.tablaSimbolos.imprimirTabla(); // Esto ahora escribe en el archivo
            System.out.println("Análisis completado.");
        } catch (Exception e) {
            System.err.println("Error durante el análisis: " + e.getMessage());
        }
    }

    private static TipoDatos getTokenType(int sym) {
        switch (sym) {
            case CUP.sym.INTEGER:
                return TipoDatos.INTEGER;
            case CUP.sym.FLOAT:
                return TipoDatos.FLOAT;
            case CUP.sym.BOOL:
                return TipoDatos.BOOL;
            case CUP.sym.CHAR:
                return TipoDatos.CHAR;
            case CUP.sym.STRING:
                return TipoDatos.STRING;
            case CUP.sym.IDENTIFIER:
                return TipoDatos.IDENTIFIER;
            default:
                return TipoDatos.UNDEFINED;
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
