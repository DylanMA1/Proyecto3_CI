package Clases;

public enum TipoDatos {
    INTEGER, FLOAT, BOOL, CHAR, STRING, IDENTIFIER, UNDEFINED;

    public boolean esCompatible(TipoDatos otro) {
        return this == otro;
    }
}