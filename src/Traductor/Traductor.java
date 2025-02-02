package Traductor;

import Clases.Simbolo;
import CUP.Parser;

public class Traductor {

    public String traducir(Parser parser) {
        StringBuilder codigoSalida = new StringBuilder();

        codigoSalida.append("// Código traducido\n\n");

        if (parser.tablaSimbolos != null) {
            for (Simbolo simbolo : parser.tablaSimbolos.getEntries()) {
                if (simbolo.getTipo().equals("FUNCION")) {
                    codigoSalida.append(traducirFuncion(simbolo));
                } else if (simbolo.getNombre().equals("MAIN")) {
                    codigoSalida.append(traducirMain(simbolo));
                } else {
                    codigoSalida.append("// No se puede traducir el símbolo: " + simbolo.getNombre() + "\n");
                }
            }
        }

        return codigoSalida.toString();
    }

    private char[] traducirMain(Simbolo simbolo) {
        return new char[0];
    }

    private char[] traducirFuncion(Simbolo simbolo) {
        return new char[0];
    }

}

