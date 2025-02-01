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
    private int nivelScopeActual;

    public TablaSimbolos() {
        tablaSimbolos = new ArrayList<>();
        nivelScopeActual = 0;
    }

    /**
     * Agrega un token a la tabla de símbolos.
     *
     * @param type     El token identificado.
     * @param tokenName El nombre del token.
     * @param valor      El tipo asociado (si es aplicable, ej. variables o funciones).
     */
    public void addToSymbolTable(String type, String tokenType, String tokenName, Symbol valor, int tamanoArreglo) {
        Object valorObject = (valor != null && valor.value != null) ? valor.value : "null";
        String ambitoActual = obtenerAmbitoActual();

        if (tokenType.equals("OPERADOR")){
            return;
        }

        if (existeEnAmbito(ambitoActual, tokenName)) {
            System.err.println("Error semántico: La variable '" + tokenName + "' ya está declarada en el mismo ámbito");
            return;
        }

        Simbolo simbolo = new Simbolo(type, tokenType, tokenName, valorObject, tamanoArreglo, nivelScopeActual, ambitoActual);
        tablaSimbolos.add(simbolo);
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

    /**
     * Comprueba si un símbolo con un nombre específico ya existe en el scope actual.
     *
     * @param nombre El nombre del símbolo a buscar.
     * @return True si existe en el scope actual, de lo contrario False.
     */
    public boolean existsInCurrentScope(String nombre) {
        for (Simbolo simbolo : tablaSimbolos) {
            if (simbolo.getNombre().equals(nombre) && simbolo.getScope() == nivelScopeActual) {
                return true;
            }
        }
        return false;
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
            //System.out.println("Intentando cerrar scope. Nivel actual antes de cerrar: " + nivelScopeActual);
            //tablaSimbolos.removeIf(simbolo -> simbolo.getScope() == nivelScopeActual);
            nivelScopeActual--;
        } else {
            System.out.println("Error: intento de cerrar scope global");
        }
    }

    /**
     * Revisa el nivel del scope actual.
     *
     * Se usa en depuración del scope en la tabla de símbolos.
     */
    public int getNivelScopeActual() {
        return nivelScopeActual;
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

    public Symbol obtenerTipoRetornoFuncion(Symbol Funcion) {
        String nombreFuncion = Funcion.value.toString();
        Simbolo simbolo;

        for (int i = 0; i < tablaSimbolos.size(); i++) {
            simbolo = tablaSimbolos.get(i);
            if (simbolo.getNombre().equals(nombreFuncion)) {
                return new Symbol(-1, simbolo.getTipo());
            }
        }
        System.err.println("Error semántico: funcion '" + nombreFuncion + "' no declarada.");
        return null;
    }

    public List<String> obtenerTipoParametrosFuncion (Symbol Funcion) {
        String nombreFuncion = Funcion.value.toString();
        Simbolo simbolo;
        List<String> listaTipoParametros = new ArrayList<>();

        for (int i = 0; i < tablaSimbolos.size(); i++) {
            simbolo = tablaSimbolos.get(i);

            if (simbolo.getNombre().equals(nombreFuncion)) {

                for (int x = i - 1; i < tablaSimbolos.size(); x--) {
                    simbolo = tablaSimbolos.get(x);

                    if (simbolo.getTipoToken().equals("PARAMETER")) {
                        listaTipoParametros.add(simbolo.getTipo());
                    }
                    if (x == 0 || simbolo.getTipoToken().equals("FUNCTION")) {
                        break;
                    }
                }
                return listaTipoParametros;
            }
        }
        return listaTipoParametros;
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

    public void analisisSemantico() {
        Simbolo funcion;
        Simbolo simbolo;
        boolean hasReturns;

        for (int i = 0; i < tablaSimbolos.size(); i++) {
            simbolo = tablaSimbolos.get(i);

            if (simbolo.getTipoToken().equals("FUNCTION")) {
                funcion = simbolo;
                hasReturns = false;

                for (int x = i - 1; i < tablaSimbolos.size(); x--) {
                    simbolo = tablaSimbolos.get(x);

                    if (simbolo.getTipoToken().equals("RETURN")) {
                        hasReturns = true;

                        if (!(simbolo.getAmbito().equals(funcion.getAmbito())) && !(simbolo.getTipo().equals(funcion.getTipo())) && !(funcion.getTipo().equals("UNDEFINED"))) {
                            System.err.println("Error semántico: la funcion: " + funcion.getNombre() + " de tipo: " + funcion.getTipo() + " no puede devolver un parametro de tipo: " + simbolo.getTipo());
                            break;
                        } else if (simbolo.getTipo().equals("VOID")) {
                            System.err.println("Error semántico: la funcion: " + funcion.getNombre() + " debe devolver un parametro de tipo: " + funcion.getTipo());
                            break;
                        } else if (funcion.getTipo().equals("UNDEFINED")) {
                            System.err.println("Error semántico: la funcion: " + funcion.getNombre() + " no debe devolver ningun parametro");
                            break;
                        }

                    } else if (x == 0 || simbolo.getTipoToken().equals("FUNCTION")) {
                        if (!(funcion.getTipo().equals("UNDEFINED")) && !hasReturns) {
                            System.err.println("Error semántico: la funcion: " + funcion.getNombre() + " debe devolver un parametro de tipo: " + funcion.getTipo());
                        }
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
            simbolosWriter.write("-------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
            simbolosWriter.write(String.format("| %-15s | %-25s | %-25s | %-40s | %-10s | %-10s | %-10s |%n", "Nombre", "Tipo token", "Tipo", "Valor", "Tamaño", "Scope", "Ambito"));
            simbolosWriter.write("-------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

            for (Simbolo simbolo : tablaSimbolos) {
                String linea = String.format("| %-15s | %-25s | %-25s | %-40s | %-10s | %-10s | %-10s |%n",
                        simbolo.getNombre(),
                        simbolo.getTipoToken(),
                        simbolo.getTipo(),
                        simbolo.getValor(),
                        (simbolo.getArraySize() != -1) ? simbolo.getArraySize() : "N/A",
                        simbolo.getScope(),
                        simbolo.getAmbito()
                );
                System.out.print(linea); // Imprime en consola
                simbolosWriter.write(linea); // Escribe en el archivo
            }

            simbolosWriter.write("-------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
            System.out.println("--------------------------------------------------------------------------------------------------------");
        } catch (IOException e) {
            System.err.println("Error al escribir la tabla de símbolos en el archivo: " + e.getMessage());
        }
    }
}