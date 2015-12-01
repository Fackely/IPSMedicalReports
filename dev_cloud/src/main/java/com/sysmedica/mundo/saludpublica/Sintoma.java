/*
 * Creado en 27-jul-2005
 * 
 * Autor Santiago Salamanca
 * SysMédica
 */
package com.sysmedica.mundo.saludpublica;

/**
 * @author santiago
 *
 */
public class Sintoma {

    private String codigoSintoma;
    private String nombre;
    private String condicion;
    /**
     * @return Returns the codigoSintoma.
     */
    public String getCodigoSintoma() {
        return codigoSintoma;
    }
    /**
     * @param codigoSintoma The codigoSintoma to set.
     */
    public void setCodigoSintoma(String codigoSintoma) {
        this.codigoSintoma = codigoSintoma;
    }
    /**
     * @return Returns the condicion.
     */
    public String getCondicion() {
        return condicion;
    }
    /**
     * @param condicion The condicion to set.
     */
    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }
    /**
     * @return Returns the nombre.
     */
    public String getNombre() {
        return nombre;
    }
    /**
     * @param nombre The nombre to set.
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
