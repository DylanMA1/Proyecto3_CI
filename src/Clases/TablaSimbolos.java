package Clases;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java_cup.runtime.Symbol;

/**
 * Clase que representa la tabla de símbolos utilizada durante el análisis léxico y sintáctico.
 */
@SuppressWarnings("LanguageDetectionInspection")
public class TablaSimbolos {

    private ArrayList<Simbolo> tablaSimbolos;

    public TablaSimbolos() {
        tablaSimbolos = new ArrayList<>();
    }

    /**
     * Agrega un token a la tabla de símbolos.
     *
     * @param type     El token identificado.
     * @param tokenName El nombre del token.
     * @param valor      El tipo asociado (si es aplicable, ej. variables o funciones).
     */
    public void addToSymbolTable(String type, String tokenName, Symbol valor) {
        Object valorObject = (valor != null && valor.value != null) ? valor.value : "null";

        Simbolo simbolo = new Simbolo(type, tokenName, valorObject);

        // Manejo de operadores: se permite agregar duplicados si son operadores
        if (type.equals("OperadorAritmetico") || type.equals("OperadorRelacional") || type.equals("OperadorLogico") || type.equals("OperadorUnario")) {
            tablaSimbolos.add(simbolo);
            return;
        }

        // Validar duplicados para otros tipos de símbolos
        if (!exists(tokenName)) {
            tablaSimbolos.add(simbolo);
        } else if (type.equals("FUNCTION")) {
            throw new RuntimeException("Error semántico: La función '" + tokenName + "' ya existe en la tabla.");
        } else {
            throw new RuntimeException("Error semántico: El símbolo '" + tokenName + "' ya existe en la tabla.");
        }
    }


    /**
     * Comprueba si un símbolo con un nombre específico ya existe en la tabla.
     *
     * @param nombre El nombre del símbolo a buscar.
     * @return True si existe, de lo contrario False.
     */
    public boolean exists(String nombre) {
        return tablaSimbolos.stream().anyMatch(s -> s.getNombre().equals(nombre));
    }

    /**
     * Devuelve todas las entradas de la tabla de símbolos.
     *
     * @return Lista de símbolos en la tabla de símbolos.
     */
    public List<Simbolo> getEntries() {
        return tablaSimbolos;
    }

    public Simbolo searchByName(String nombre) {
        for (Simbolo simbolo : tablaSimbolos) {
            if (simbolo.getNombre().equals(nombre)) {
                return simbolo;
            }
        }
        return null;
    }

    /**
     * Obtiene el tipo de un símbolo dado su nombre.
     *
     * @param nombre El nombre del símbolo.
     * @return El tipo del símbolo como TipoDatos.
     */
    public TipoDatos obtenerTipo(String nombre) {
        for (Simbolo simbolo : tablaSimbolos) {
            if (simbolo.getNombre().equals(nombre)) {
                return TipoDatos.valueOf(simbolo.getTipo());
            }
        }
        System.err.println("Error semántico: Variable '" + nombre + "' no declarada.");
        return null;
    }

    /**
     * Obtiene el tipo de un símbolo dado su nombre.
     *
     * @param nombre El nombre del símbolo.
     * @return El tipo del símbolo como TipoDatos.
     */
    public Symbol obtenerValor(Symbol nombre) {
        String variable = nombre.value.toString();
        for (Simbolo simbolo : tablaSimbolos) {
            if (simbolo.getNombre().equals(variable)) {
                return new Symbol(-1, simbolo.getValor());
            }
        }
        System.err.println("Error semántico: Variable '" + variable + "' no declarada.");
        return nombre;
    }


    /**
     * Comprueba si dos tipos de datos son compatibles.
     *
     * @param tipo1 El primer tipo.
     * @param tipo2 El segundo tipo.
     * @return True si son compatibles, False en caso contrario.
     */
    public boolean esTipoCompatible(TipoDatos tipo1, TipoDatos tipo2) {
        return tipo1.esCompatible(tipo2);
    }

    /**
     * Imprime la tabla de símbolos en formato tabular y la escribe en un archivo.
     */
    public void imprimirTabla() {
        String archivoSimbolos = "salida_simbolos.txt";
        try (BufferedWriter simbolosWriter = new BufferedWriter(new FileWriter(archivoSimbolos, false))) {
            System.out.println("---------------------------------------------------------------------------------------------");
            simbolosWriter.write("----------------------------------------------------------------------------------------------\n");
            simbolosWriter.write(String.format("| %-15s | %-25s | %-40s |%n", "Nombre", "Tipo", "Valor"));
            simbolosWriter.write("----------------------------------------------------------------------------------------------\n");

            for (Simbolo simbolo : tablaSimbolos) {
                String linea = String.format("| %-15s | %-25s | %-40s |%n",
                        simbolo.getNombre(),
                        simbolo.getTipo(),
                        simbolo.getValor()
                );
                System.out.print(linea); // Imprime en consola
                simbolosWriter.write(linea); // Escribe en el archivo
            }

            simbolosWriter.write("------------------------------------------------------------------------------------------\n");
            System.out.println("---------------------------------------------------------------------------------------------");
        } catch (IOException e) {
            System.err.println("Error al escribir la tabla de símbolos en el archivo: " + e.getMessage());
        }
    }
}