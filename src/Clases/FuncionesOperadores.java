package Clases;

import java_cup.runtime.Symbol;

/**
 * Clase que proporciona métodos para combinar expresiones aritméticas, relacionales,
 * lógicas y unarias, evaluando su resultado y devolviendo un símbolo con el valor calculado.
 * Los errores se manejan en "modo pánico", reportando el error pero sin detener la ejecución.
 */
public class FuncionesOperadores {

    /**
     * Combina dos expresiones aritméticas y un operador, evaluando el resultado.
     *
     * @param operandoIzquierdo Símbolo que representa la expresión del lado izquierdo.
     * @param operador Símbolo que representa el operador aritmético.
     * @param operandoDerecho Símbolo que representa la expresión del lado derecho.
     * @return Símbolo que contiene el resultado de la operación aritmética.
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
                    case "+": resultado = izq + der; break;
                    case "-": resultado = izq - der; break;
                    case "*": resultado = izq * der; break;
                    case "/":
                        if (der == 0) throw new ArithmeticException("División por cero.");
                        resultado = izq / der;
                        break;
                    case "%": resultado = izq % der; break;
                    case "^": resultado = Math.pow(izq, der); break;
                    default: throw new IllegalArgumentException("Operador no soportado: " + oper);
                }
            } else if (izquierda instanceof Float || derecha instanceof Float) {
                float izq = ((Number) izquierda).floatValue();
                float der = ((Number) derecha).floatValue();

                switch (oper) {
                    case "+": resultado = izq + der; break;
                    case "-": resultado = izq - der; break;
                    case "*": resultado = izq * der; break;
                    case "/":
                        if (der == 0.0) throw new ArithmeticException("División por cero.");
                        resultado = izq / der;
                        break;
                    case "^": resultado = Math.pow(izq, der); break;
                    default: throw new IllegalArgumentException("Operador no soportado: " + oper);
                }
            } else {
                throw new IllegalArgumentException("Tipos incompatibles: " + izquierda.getClass() + " y " + derecha.getClass());
            }
        } catch (Exception e) {
            System.err.println("Error semántico: " + e.getMessage());
            resultado = null;
        }

        return new Symbol(-1, resultado);
    }

    /**
     * Combina dos expresiones y un operador relacional, evaluando el resultado.
     *
     * @param operandoIzquierdo Símbolo que representa la expresión del lado izquierdo.
     * @param operador Símbolo que representa el operador relacional.
     * @param operandoDerecho Símbolo que representa la expresión del lado derecho.
     * @return Símbolo que contiene el resultado de la operación relacional (booleano).
     */
    public static Symbol combinarExpresionesRelacionales(Symbol operandoIzquierdo, Symbol operador, Symbol operandoDerecho) {
        try {
            if (operandoIzquierdo.value instanceof Number && operandoDerecho.value instanceof Number) {
                Float valorIzquierda = ((Number) operandoIzquierdo.value).floatValue();
                Float valorDerecha = ((Number) operandoDerecho.value).floatValue();

                boolean resultado;
                switch (operador.value.toString()) {
                    case "<": resultado = valorIzquierda < valorDerecha; break;
                    case ">": resultado = valorIzquierda > valorDerecha; break;
                    case "<=": resultado = valorIzquierda <= valorDerecha; break;
                    case ">=": resultado = valorIzquierda >= valorDerecha; break;
                    case "==": resultado = valorIzquierda.equals(valorDerecha); break;
                    case "!=": resultado = !valorIzquierda.equals(valorDerecha); break;
                    default: throw new IllegalArgumentException("Operador relacional no válido: " + operador.value);
                }

                return new Symbol(-1, resultado);
            } else {
                throw new IllegalArgumentException("Tipos incompatibles para comparación relacional");
            }
        } catch (Exception e) {
            System.err.println("Error semántico: " + e.getMessage());
            return null;
        }
    }

    /**
     * Combina dos expresiones y un operador lógico, evaluando el resultado.
     *
     * @param operandoIzquierdo Símbolo que representa la expresión del lado izquierdo.
     * @param operador Símbolo que representa el operador lógico.
     * @param operandoDerecho Símbolo que representa la expresión del lado derecho (puede ser null).
     * @return Símbolo que contiene el resultado de la operación lógica (booleano).
     */
    public static Symbol combinarExpresionesLogicas(Symbol operandoIzquierdo, Symbol operador, Symbol operandoDerecho) {
        Object izquierda = operandoIzquierdo != null ? operandoIzquierdo.value : null;
        Object derecha = operandoDerecho != null ? operandoDerecho.value : null;
        String oper = operador.value.toString();
        Object resultado = null;

        try {
            if (izquierda instanceof Boolean && (derecha == null || derecha instanceof Boolean)) {
                boolean izq = (Boolean) izquierda;
                boolean der = derecha != null ? (Boolean) derecha : false;

                switch (oper) {
                    case "&&": resultado = izq && der; break;
                    case "||": resultado = izq || der; break;
                    default: throw new IllegalArgumentException("Operador lógico no soportado: " + oper);
                }
            } else {
                throw new IllegalArgumentException("Tipos incompatibles: " + izquierda.getClass() + " y " + (derecha != null ? derecha.getClass() : "null"));
            }
        } catch (Exception e) {
            System.err.println("Error semántico: " + e.getMessage());
            resultado = null;
        }

        return new Symbol(-1, resultado);
    }

    /**
     * Combina una expresión y un operador unario, evaluando el resultado.
     *
     * @param termino Símbolo que representa el término al que se aplica el operador.
     * @param operador Símbolo que representa el operador unario.
     * @param esPrefijo Indica si el operador es prefijo (true) o postfijo (false).
     * @return Símbolo que contiene el resultado de la operación unaria.
     */
    public static Symbol combinarExpresionUnaria(Symbol termino, Symbol operador, boolean esPrefijo) {
        try {
            Object valorTermino = termino.value;

            if (!(valorTermino instanceof Float || valorTermino instanceof Integer)) {
                throw new IllegalArgumentException("El término debe ser un número (Float o Integer)");
            }

            if (!(operador.value instanceof String)) {
                throw new IllegalArgumentException("El operador unario debe ser una cadena");
            }
            String operadorUnario = (String) operador.value;

            Number resultado = null;
            if (valorTermino instanceof Integer) {
                int numero = (Integer) valorTermino;
                switch (operadorUnario) {
                    case "++": resultado = esPrefijo ? numero + 1 : numero; termino.value = numero + 1; break;
                    case "--": resultado = esPrefijo ? numero - 1 : numero; termino.value = numero - 1; break;
                    default: throw new IllegalArgumentException("Operador unario no soportado: " + operadorUnario);
                }
            } else if (valorTermino instanceof Float) {
                float numero = (Float) valorTermino;
                switch (operadorUnario) {
                    case "++": resultado = esPrefijo ? numero + 1 : numero; termino.value = numero + 1; break;
                    case "--": resultado = esPrefijo ? numero - 1 : numero; termino.value = numero - 1; break;
                    default: throw new IllegalArgumentException("Operador unario no soportado: " + operadorUnario);
                }
            }

            return new Symbol(-1, resultado);
        } catch (Exception e) {
            System.err.println("Error semántico: " + e.getMessage());
            return null;
        }
    }
}