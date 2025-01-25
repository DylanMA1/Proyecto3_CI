package Clases;

import java_cup.runtime.Symbol;

public class FuncionesOperadores {

    /**
     * Combina dos expresiones y un operador en un único símbolo y evalúa el resultado.
     *
     * @param operandoIzquierdo El símbolo que representa la expresión del lado izquierdo.
     * @param operador El símbolo que representa el operador.
     * @param operandoDerecho El símbolo que representa la expresión del lado derecho.
     * @return Un nuevo símbolo que representa el resultado de la operación.
     */
    public static Symbol combinarExpresionesAritmeticas(Symbol operandoIzquierdo, Symbol operador, Symbol operandoDerecho) {
        Object izquierda = operandoIzquierdo.value;
        Object derecha = operandoDerecho.value;
        String oper = operador.value.toString();

        Object resultado = null;

        try {
            if (izquierda instanceof Integer && derecha instanceof Integer) {
                int izq = (Integer) izquierda;
                int der = (Integer) derecha;

                switch (oper) {
                    case "+":
                        resultado = izq + der;
                        break;
                    case "-":
                        resultado = izq - der;
                        break;
                    case "*":
                        resultado = izq * der;
                        break;
                    case "/":
                        if (der == 0) throw new ArithmeticException("División por cero.");
                        resultado = izq / der;
                        break;
                    case "%":
                        resultado = izq % der;
                        break;
                    case "^":
                        resultado = Math.pow(izq, der);
                        break;
                    default:
                        throw new IllegalArgumentException("Operador no soportado: " + oper);
                }
            } else if (izquierda instanceof Float || derecha instanceof Float) {
                float izq = ((Number) izquierda).floatValue();
                float der = ((Number) derecha).floatValue();

                switch (oper) {
                    case "+":
                        resultado = izq + der;
                        break;
                    case "-":
                        resultado = izq - der;
                        break;
                    case "*":
                        resultado = izq * der;
                        break;
                    case "/":
                        if (der == 0.0) throw new ArithmeticException("División por cero.");
                        resultado = izq / der;
                        break;
                    case "^":
                        resultado = Math.pow(izq, der);
                        break;
                    default:
                        throw new IllegalArgumentException("Operador no soportado: " + oper);
                }
            } else {
                throw new IllegalArgumentException("Tipos incompatibles: " + izquierda.getClass() + " y " + derecha.getClass());
            }
        } catch (Exception e) {
            System.err.println("Error evaluando expresión: " + e.getMessage());
        }
        System.out.println("combinarExpresionesAritmeticas: "+ resultado);
        return new Symbol(-1, resultado);
    }

    /**
     * Combina dos expresiones y un operador relacional en un único símbolo y evalúa el resultado.
     *
     * @param operandoIzquierdo El símbolo que representa la expresión del lado izquierdo.
     * @param operador El símbolo que representa el operador relacional.
     * @param operandoDerecho El símbolo que representa la expresión del lado derecho.
     * @return Un nuevo símbolo que representa el resultado de la operación relacional (booleano).
     */
    public static Symbol combinarExpresionesRelacionales(Symbol operandoIzquierdo, Symbol operador, Symbol operandoDerecho) {
        if (operandoIzquierdo.value instanceof Number && operandoDerecho.value instanceof Number) {
            Float valorIzquierda = ((Number) operandoIzquierdo.value).floatValue();
            Float valorDerecha = ((Number) operandoDerecho.value).floatValue();

            System.out.println("Izquierda: " + valorIzquierda + " (" + valorIzquierda.getClass().getName() + ")");
            System.out.println("Derecha: " + valorDerecha + " (" + valorDerecha.getClass().getName() + ")");
            System.out.println("Operador: " + operador.value.toString() + " (" + operador.getClass().getName() + ")");

            boolean resultado;

            // Operador de comparación
            switch (operador.value.toString()) {
                case "<":
                    resultado = valorIzquierda < valorDerecha;
                    break;
                case ">":
                    resultado = valorIzquierda > valorDerecha;
                    break;
                case "<=":
                    resultado = valorIzquierda <= valorDerecha;
                    break;
                case ">=":
                    resultado = valorIzquierda >= valorDerecha;
                    break;
                case "==":
                    resultado = valorIzquierda.equals(valorDerecha);
                    break;
                case "!=":
                    resultado = !valorIzquierda.equals(valorDerecha);
                    break;
                default:
                    throw new IllegalArgumentException("Operador relacional no válido: " + operador.value);
            }

            // Retornar el resultado como Symbol
            System.out.println("combinarExpresionesRelacionales: " + resultado);
            return new Symbol(-1, resultado);
        }

        // Si los tipos no son compatibles
        System.err.println("Error: Tipos incompatibles para comparación relacional");
        return null;
    }


    /**
     * Combina dos expresiones y un operador lógico en un único símbolo y evalúa el resultado.
     *
     * @param operandoIzquierdo El símbolo que representa la expresión del lado izquierdo.
     * @param operador El símbolo que representa el operador lógico.
     * @param operandoDerecho El símbolo que representa la expresión del lado derecho (puede ser null en el caso del operador '!')
     * @return Un nuevo símbolo que representa el resultado de la operación lógica.
     */
    public static Symbol combinarExpresionesLogicas(Symbol operandoIzquierdo, Symbol operador, Symbol operandoDerecho) {
        Object izquierda = operandoIzquierdo != null ? operandoIzquierdo.value : null;
        System.out.println("Izquierdo: " + izquierda);
        Object derecha = operandoDerecho != null ? operandoDerecho.value : null;
        System.out.println("Derecha: " + derecha);
        String oper = operador.value.toString();
        System.out.println("Operador: " + oper);

        Object resultado = null;

        try {
            if (izquierda instanceof Boolean && (derecha == null || derecha instanceof Boolean)) {
                boolean izq = (Boolean) izquierda;
                boolean der = derecha != null ? (Boolean) derecha : false;

                switch (oper) {
                    case "&&": // AND lógico
                        resultado = izq && der;
                        break;
                    case "||": // OR lógico
                        resultado = izq || der;
                        break;

                    default:
                        throw new IllegalArgumentException("Operador lógico no soportado: " + oper);
                }
            } else {
                throw new IllegalArgumentException("Tipos incompatibles: " + izquierda.getClass() + " y " + (derecha != null ? derecha.getClass() : "null"));
            }
        } catch (Exception e) {
            System.err.println("Error evaluando expresión lógica: " + e.getMessage());
        }

        System.out.println("combinarExpresionesLogicas: " + resultado);
        return new Symbol(-1, resultado);
    }

    public static Float combinarExpresionUnaria(Symbol termino, Symbol operador, boolean esPrefijo) {
        System.out.println(termino.value + " (" + termino.value.getClass().getName() + ")");
        System.out.println(operador.value + " (" + operador.value.getClass().getName() + ")");

        Object valorTermino = termino.value;

        if (!(valorTermino instanceof Float)) {
            throw new IllegalArgumentException("El término debe ser un número");
        }
        Float numero = (Float) valorTermino;

        // Validar que el operador es una cadena
        Object valorOperador = operador.value;
        if (!(valorOperador instanceof String)) {
            throw new IllegalArgumentException("El operador unario debe ser una cadena");
        }
        String operadorUnario = (String) valorOperador;

        // Lógica para prefijo y postfijo
        switch (operadorUnario) {
            case "++":
                if (esPrefijo) {
                    return numero + 1; // Incremento prefijo
                } else {
                    Float original = numero; // Guardar el valor original
                    termino.value = (numero + 1); // Incrementar el valor
                    return original; // Retornar el valor antes de incrementar
                }
            case "--":
                if (esPrefijo) {
                    return numero - 1; // Decremento prefijo
                } else {
                    Float original = numero; // Guardar el valor original
                    termino.value = (numero - 1); // Decrementar el valor
                    return original; // Retornar el valor antes de decrementar
                }
            default:
                throw new IllegalArgumentException("Operador unario no soportado: " + operadorUnario);
        }
    }
}