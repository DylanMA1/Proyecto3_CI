package Clases;

import java.util.*;

import java_cup.runtime.Symbol;

/**
 * Clase que representa la tabla de símbolos utilizada durante el análisis léxico y sintáctico.
 */
public class TablaSimbolos {

    private final List<SymbolTableEntry> symbolTable;

    private final Map<String, TipoDatos> tiposDato = new HashMap<>();

    public TablaSimbolos() {
        symbolTable = new ArrayList<>();
    }

    /**
     * Agrega un token a la tabla de símbolos.
     *
     * @param token     El token identificado.
     * @param tokenName El nombre del token.
     * @param tipo      El tipo asociado (si es aplicable, ej. variables o funciones).
     * @param linea     La línea donde se encuentra el token.
     * @param columna   La columna donde se encuentra el token.
     */
    public void addToSymbolTable(Symbol token, String tokenName, String tipo, int linea, int columna) {
        SymbolTableEntry entry = new SymbolTableEntry(tokenName, token.value, tipo, linea, columna);
        symbolTable.add(entry);
    }

    /**
     * Devuelve todas las entradas de la tabla de símbolos.
     *
     * @return Lista de entradas en la tabla de símbolos.
     */
    public List<SymbolTableEntry> getEntries() {
        return symbolTable;
    }



    /**
     * Clase para representar una entrada en la tabla de símbolos.
     */
    public class SymbolTableEntry {
        private final String name;
        private final Object value;
        private final String type; // Tipo asociado, ej. int, float, función
        private final int line;
        private final int column;

        public SymbolTableEntry(String name, Object value, String type, int line, int column) {
            this.name = name;
            this.value = value;
            this.type = type;
            this.line = line;
            this.column = column;
        }

        public String toString() {
            return "Símbolo: " + name +
                    ", Valor: " + value +
                    ", Tipo: " + type +
                    ", Línea: " + line +
                    ", Columna: " + column;
        }
    }

    public void agregarTipoDato(TipoDatos tipoDato){
        this.tiposDato.put("string",tipoDato);
    }

    public void declarar(String nombre, TipoDatos tipo) {
        if (tiposDato.containsKey(nombre)) {
            throw new RuntimeException("Error semántico: Variable '" + nombre + "' ya declarada.");
        }
        tiposDato.put(nombre, tipo);
    }

    public TipoDatos obtenerTipo(String nombre) {
        if (!tiposDato.containsKey(nombre)) {
            throw new RuntimeException("Error semántico: Variable '" + nombre + "' no declarada.");
        }
        return tiposDato.get(nombre);
    }

}


