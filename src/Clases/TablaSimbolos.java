package Clases;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java_cup.runtime.Symbol;

/**
 * Clase que representa la tabla de símbolos utilizada durante el análisis léxico y sintáctico.
 */
@SuppressWarnings("LanguageDetectionInspection")
public class TablaSimbolos {

    private ArrayList<Simbolo> tablaSimbolos;
    private int nivelScopeActual;

    public TablaSimbolos() {
        tablaSimbolos = new ArrayList<>();
        nivelScopeActual = 0;
    }

    /**
     * Agrega un token a la tabla de símbolos.
     *
     * @param type      El token identificado.
     * @param tokenName El nombre del token.
     * @param valor     El tipo asociado (si es aplicable, ej. variables o funciones).
     */
    public void addToSymbolTable(String type, String tokenType, String tokenName, Symbol valor, int tamanoArreglo) {
        Object valorObject = (valor != null && valor.value != null) ? valor.value : "null";
        String ambitoActual = tokenType.equals("FUNCTION") ? "global" : obtenerAmbitoActual();

        if (tokenType.equals("OPERADOR")) {
            return;
        }

        if (tokenType.equals("FUNCTION") && exists(tokenName)) {
            System.err.println("Error semántico: La función '" + tokenName + "' ya está declarada");
            return;
        }

        if (verificarDuplicadoEnAmbito(ambitoActual, tokenName)) {
            return;
        }

        Simbolo simbolo = new Simbolo(type, tokenType, tokenName, valorObject, tamanoArreglo, nivelScopeActual, ambitoActual);
        tablaSimbolos.add(simbolo);
    }

    /**
     * Verifica las duplicaciones en el ambito al crear el simbolo a la tabla de simbolos.
     */
    private boolean verificarDuplicadoEnAmbito(String ambitoActual, String tokenName) {
        if (existeEnAmbito(ambitoActual, tokenName)) {
            System.err.println("Error semántico: La variable '" + tokenName + "' ya está declarada en el mismo ámbito");
            return true;
        }
        return false;
    }

    /**
     * Asigna el ámbito a las variables basándose en la posición de las funciones.
     * Recorre la tabla de símbolos y asigna el nombre de la función como ámbito
     * a todas las variables que estén antes de la función. Se detiene si encuentra
     * otra función o llega al final/inicio de la lista.
     */
    public void asignarAmbito() {
        String ambitoActual;
        Simbolo simbolo;

        for (int i = 0; i < tablaSimbolos.size(); i++) {
            simbolo = tablaSimbolos.get(i);
            if (simbolo.getTipoToken().equals("FUNCTION")) {
                ambitoActual = simbolo.getNombre();
                for (int x = i - 1; i < tablaSimbolos.size(); x--) {
                    simbolo = tablaSimbolos.get(x);
                    if (!(simbolo.getTipoToken().equals("FUNCTION")))  {
                        simbolo.setAmbito(ambitoActual);
                    }
                    if (x == 0 || simbolo.getTipoToken().equals("FUNCTION")) {
                        break;
                    }
                }
            }
        }
    }

    /**
     * Verifica si una variable con el mismo nombre ya está en el ámbito actual.
     */
    public boolean existeEnAmbito(String ambito, String nombre) {
        for (Simbolo simbolo : tablaSimbolos) {
            if (simbolo.getNombre().equals(nombre) && simbolo.getAmbito().equals(ambito)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Obtiene el ámbito actual basado en la función en la que está la variable.
     */
    public String obtenerAmbitoActual() {
        for (int i = tablaSimbolos.size() - 1; i >= 0; i--) {
            Simbolo simbolo = tablaSimbolos.get(i);
            if (simbolo.getTipoToken().equals("FUNCTION")) {
                return simbolo.getNombre();
            }
        }
        return "global";
    }

    public Simbolo obtenerSimbolo(String nombre) {
        for (Simbolo simbolo : tablaSimbolos) {
            if (simbolo.getNombre().equals(nombre)) {
                return simbolo;
            }
        }
        return null;
    }


    /**
     * Abre un nuevo scope.
     *
     * @param tokenOpenBlock El token que representa la apertura del bloque (OPEN_BLOCK).
     */
    public void abrirScope(Symbol tokenOpenBlock) {
        nivelScopeActual++;
    }

    /**
     * Cierra el scope actual.
     *
     * @param tokenCloseBlock El token que representa el cierre del bloque (CLOSE_BLOCK).
     */
    public void cerrarScope(Symbol tokenCloseBlock) {
        if (nivelScopeActual > 0) { // No cerrar el scope global
            nivelScopeActual--;
        } else {
            System.out.println("Error: intento de cerrar scope global");
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

    /**
     * Busca un símbolo en la tabla de símbolos por su nombre.
     *
     * @param nombre El nombre del símbolo a buscar.
     * @return El objeto {@code Simbolo} correspondiente al nombre buscado si se encuentra
     * en la tabla de símbolos; de lo contrario, devuelve {@code null}.
     */
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
     * @param nombre El nombre del símbolo a buscar.
     * @return El tipo del símbolo como {@code TipoDatos}, o {@code null} si no se encuentra.
     */
    public TipoDatos obtenerTipo(String nombre) {
        for (Simbolo simbolo : tablaSimbolos) {
            if (simbolo.getNombre().equals(nombre)) {
                return TipoDatos.valueOf(simbolo.getTipo()); // Convierte el tipo almacenado a TipoDatos
            }
        }
        System.err.println("Error semántico: Variable '" + nombre + "' no declarada.");
        return null;
    }

    /**
     * Obtiene el valor de un símbolo dado su nombre.
     *
     * @param nombre El nombre del símbolo a buscar, encapsulado en un objeto {@code Symbol}.
     * @return Un nuevo objeto {@code Symbol} con el valor de la variable, o el mismo objeto {@code nombre}
     *         si la variable no está declarada.
     */
    public Symbol obtenerValor(Symbol nombre) {
        String variable = nombre.value.toString();
        String ambitoActual = obtenerAmbitoActual();

        // Buscar primero en el ámbito actual
        for (Simbolo simbolo : tablaSimbolos) {
            if (simbolo.getNombre().equals(variable) && simbolo.getAmbito().equals(ambitoActual)) {
                return new Symbol(-1, simbolo.getValor()); // Retorna el valor si se encuentra en el mismo ámbito
            }
        }

        // Mensaje de error si no está en el ámbito actual
        System.err.println("Error semántico: Variable '" + variable + "' no declarada en el ámbito actual.");

        // Buscar en toda la tabla de símbolos (variable global o en otro ámbito)
        for (Simbolo simbolo : tablaSimbolos) {
            if (simbolo.getNombre().equals(variable)) {
                return new Symbol(-1, simbolo.getValor()); // Retorna el valor si existe en otro ámbito
            }
        }

        // Mensaje de error si no se encuentra en ningún ámbito
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
     * Obtiene el tipo de retorno de una función a partir de su nombre.
     *
     * @param Funcion El nombre de la función representado como un objeto {@code Symbol}.
     * @return Un objeto {@code Symbol} que contiene el tipo de retorno de la función,
     *         o {@code null} si la función no está declarada.
     */
    public Symbol obtenerTipoRetornoFuncion(Symbol Funcion) {
        // Obtener el nombre de la función a partir del valor del símbolo
        String nombreFuncion = Funcion.value.toString();
        Simbolo simbolo;

        // Recorrer la tabla de símbolos para buscar la función
        for (int i = 0; i < tablaSimbolos.size(); i++) {
            simbolo = tablaSimbolos.get(i);

            // Si el nombre coincide, devolver el tipo de retorno de la función
            if (simbolo.getNombre().equals(nombreFuncion)) {
                return new Symbol(-1, simbolo.getTipo());
            }
        }

        // Si no se encuentra la función, mostrar un mensaje de error
        System.err.println("Error semántico: función '" + nombreFuncion + "' no declarada.");
        return null;
    }

    /**
     * Obtiene los tipos de los parámetros de una función específica.
     *
     * @param Funcion El nombre de la función representado como un objeto {@code Symbol}.
     * @return Una lista de cadenas que representan los tipos de los parámetros de la función.
     *         Si la función no tiene parámetros o no está declarada, se devuelve una lista vacía.
     */
    public List<String> obtenerTipoParametrosFuncion(Symbol Funcion) {
        // Obtener el nombre de la función a partir del valor del símbolo
        String nombreFuncion = Funcion.value.toString();
        Simbolo simbolo;
        List<String> listaTipoParametros = new ArrayList<>();

        // Recorrer la tabla de símbolos para buscar la función
        for (int i = 0; i < tablaSimbolos.size(); i++) {
            simbolo = tablaSimbolos.get(i);

            // Si se encuentra la función, buscar sus parámetros en la tabla de símbolos
            if (simbolo.getNombre().equals(nombreFuncion)) {

                // Buscar los parámetros de la función en posiciones anteriores de la tabla de símbolos
                for (int x = i - 1; x >= 0; x--) {
                    simbolo = tablaSimbolos.get(x);

                    // Si el símbolo es un parámetro, agregar su tipo a la lista
                    if (simbolo.getTipoToken().equals("PARAMETER")) {
                        listaTipoParametros.add(simbolo.getTipo());
                    }

                    // Si se alcanza el inicio de la tabla o se encuentra otra función, detener la búsqueda
                    if (simbolo.getTipoToken().equals("FUNCTION")) {
                        break;
                    }
                }
                return listaTipoParametros;
            }
        }
        return listaTipoParametros;
    }

    /**
     * Escribe la tabla de símbolos en un archivo de texto en formato tabular.
     * <p>
     * La tabla de símbolos se guarda en un archivo llamado "salida_simbolos.txt",
     * con columnas que incluyen el nombre del símbolo, tipo de token, tipo de dato,
     * valor, tamaño (si es un array), alcance (scope) y ámbito (ambito).
     * </p>
     * <p>
     * Si el archivo ya existe, se sobrescribe. En caso de error al escribir el archivo,
     * se captura y muestra un mensaje de error en la salida estándar de error.
     * </p>
     */
    public void escribirTabla() {
        // Nombre del archivo donde se guardará la tabla de símbolos
        String archivoSimbolos = "salida_simbolos.txt";

        try (BufferedWriter simbolosWriter = new BufferedWriter(new FileWriter(archivoSimbolos, false))) {
            // Escribir encabezado de la tabla con separadores
            simbolosWriter.write("-------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
            simbolosWriter.write(String.format("| %-15s | %-25s | %-25s | %-40s | %-10s | %-10s | %-10s |%n",
                    "Nombre", "Tipo token", "Tipo", "Valor", "Tamaño", "Scope", "Ámbito"));
            simbolosWriter.write("-------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

            // Iterar sobre la tabla de símbolos y escribir cada símbolo en el archivo
            for (Simbolo simbolo : tablaSimbolos) {
                String linea = String.format("| %-15s | %-25s | %-25s | %-40s | %-10s | %-10s | %-10s |%n",
                        simbolo.getNombre(),
                        simbolo.getTipoToken(),
                        simbolo.getTipo(),
                        simbolo.getValor(),
                        (simbolo.getArraySize() != -1) ? simbolo.getArraySize() : "N/A", // Si no es un array, mostrar "N/A"
                        simbolo.getScope(),
                        simbolo.getAmbito()
                );
                simbolosWriter.write(linea); // Escribe la línea en el archivo
            }

            // Escribir la línea final del separador
            simbolosWriter.write("-------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

        } catch (IOException e) {
            // Capturar y mostrar cualquier error de escritura en el archivo
            System.err.println("Error al escribir la tabla de símbolos en el archivo: " + e.getMessage());
        }
    }
}