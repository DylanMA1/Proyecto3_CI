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

        /**
         * Constructor que inicializa un objeto de tipo EncabezadoFuncion.
         * @param tipo El tipo de retorno de la función.
         * @param nombre El nombre de la función.
         * @param parametros Los parámetros que acepta la función.
         */
        public EncabezadoFuncion(TipoDatos tipo, String nombre, Object parametros) {
            this.tipo = tipo;
            this.nombre = nombre;
            this.parametros = parametros;
        }
    }
}
