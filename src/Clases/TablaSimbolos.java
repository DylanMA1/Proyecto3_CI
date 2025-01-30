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
    public void addToSymbolTable(String type, String tokenType, String tokenName, Symbol valor, int tamanoArreglo, String ambito) {
        Object valorObject = (valor != null && valor.value != null) ? valor.value : "null";

        // Crear el símbolo con el nivel de scope actual
        Simbolo simbolo = new Simbolo(type, tokenType, tokenName, valorObject, tamanoArreglo, ambito);

        // Manejo de operadores: se permite agregar duplicados si son operadores
        if (type.equals("OperadorAritmetico") || type.equals("OperadorRelacional") || type.equals("OperadorLogico") || type.equals("OperadorUnario")) {
            tablaSimbolos.add(simbolo);
            return;
        }

        if (!exists(tokenName)) {
            tablaSimbolos.add(simbolo);
        } else {
            throw new RuntimeException("Error semántico: El símbolo '" + tokenName + "' ya existe en este scope.");
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
     * Obtiene el valor de un arreglo en un índice específico.
     *
     * @param nombre El nombre del arreglo (como Symbol).
     * @param indice El índice del arreglo (como Symbol).
     * @return El valor en el índice especificado (como Symbol).
     * @throws RuntimeException Si el arreglo no existe o el índice está fuera de los límites.
     */
    public Symbol obtenerValorArreglo(String nombre, Symbol indice) {

        // Obtener el índice como un entero
        if (!(indice.value instanceof Integer)) {
            throw new RuntimeException("Error semántico: El índice debe ser un entero.");
        }
        int idx = (Integer) indice.value;

        // Buscar el arreglo en la tabla de símbolos
        for (Simbolo simbolo : tablaSimbolos) {
            if (simbolo.getNombre().equals(nombre)) {
                // Verificar si es un arreglo
                if (simbolo.getArraySize() == -1) {
                    throw new RuntimeException("Error semántico: '" + nombre + "' no es un arreglo.");
                }

                // Verificar si el índice está dentro de los límites
                if (idx < 0 || idx >= simbolo.getArraySize()) {
                    throw new RuntimeException("Error semántico: Índice fuera de los límites para el arreglo '" + nombre + "'.");
                }

                List<Object> array = (List<Object>) simbolo.getValor();
                Object valor = array.get(idx);

                return new Symbol(-1, valor); // Devolver el valor como un Symbol
            }
        }
        // Si el arreglo no se encuentra, lanzar un error
        throw new RuntimeException("Error semántico: Arreglo '" + nombre + "' no declarado.");
    }

    /**
     * Asigna el ámbito a las variables basándose en la posición de las funciones.
     * Recorre la tabla de símbolos y asigna el nombre de la función como ámbito
     * a todas las variables que estén antes de la función. Se detiene si encuentra
     * otra función o llega al final/inicio de la lista.
     */
    public void asignarAmbito() {
        String ambitoActual = "Global"; // Ámbito inicial
        for (int i = 0; i < tablaSimbolos.size(); i++) {
            Simbolo simbolo = tablaSimbolos.get(i);

            // Si el símbolo es una función, actualizamos el ámbito actual
            if (simbolo.getTipoToken().equals("FUNCTION")) {
                ambitoActual = simbolo.getNombre(); // El ámbito ahora es el nombre de la función
                for (int i2 = i - 1; i < tablaSimbolos.size(); i2--) {
                    simbolo = tablaSimbolos.get(i2);
                    if (simbolo.getTipoToken().equals("VARIABLE") || simbolo.getTipoToken().equals("PARAMETER")) {
                        simbolo.setAmbito(ambitoActual);
                    }
                    System.out.println(i2);
                    if (i2 == 0 || simbolo.getTipoToken().equals("FUNCTION")) {
                        break;
                    }
                }
            }

        }
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
            System.out.println("------------------------------------------------------------------------------------------------------");
            simbolosWriter.write("-------------------------------------------------------------------------------------------------------\n");
            simbolosWriter.write(String.format("| %-15s | %-25s | %-25s | %-40s | %-10s |%n", "Nombre", "Tipo token", "Tipo", "Valor", "Tamaño"));
            simbolosWriter.write("-------------------------------------------------------------------------------------------------------\n");

            for (Simbolo simbolo : tablaSimbolos) {
                String linea = String.format("| %-15s | %-25s | %-25s | %-40s | %-10s | %-10s |%n",
                        simbolo.getNombre(),
                        simbolo.getTipoToken(),
                        simbolo.getTipo(),
                        simbolo.getValor(),
                        (simbolo.getArraySize() != -1) ? simbolo.getArraySize() : "N/A",
                        simbolo.getAmbito()
                );
                System.out.print(linea); // Imprime en consola
                simbolosWriter.write(linea); // Escribe en el archivo
            }

            simbolosWriter.write("-------------------------------------------------------------------------------------------------------\n");
            System.out.println("--------------------------------------------------------------------------------------------------------");
        } catch (IOException e) {
            System.err.println("Error al escribir la tabla de símbolos en el archivo: " + e.getMessage());
        }
    }
}