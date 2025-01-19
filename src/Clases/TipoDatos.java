package Clases;

public enum TipoDatos {
    INTEGER, FLOAT, BOOL, CHAR, STRING;

    public boolean esCompatible(TipoDatos otro) {
        // Lógica de compatibilidad de tipos básica
        return this == otro;
    }
}