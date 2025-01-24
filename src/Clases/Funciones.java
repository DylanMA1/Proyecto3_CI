package Clases;

public class Funciones {
    // Métodos relacionados con las funciones

    /**
     * Clase auxiliar para capturar los datos del encabezado de la función.
     */
    public static class EncabezadoFuncion {
        public TipoDatos tipo;
        public String nombre;
        public Object parametros;

        public EncabezadoFuncion(TipoDatos tipo, String nombre, Object parametros) {
            this.tipo = tipo;
            this.nombre = nombre;
            this.parametros = parametros;
        }
    }
}
