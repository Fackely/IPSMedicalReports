/*
 * Creado en 02-ago-2005
 * 
 * Autor Santiago Salamanca
 * SysMédica
 */
package com.sysmedica.mundo.saludpublica;

/**
 * @author santiago
 *
 */
public class VacunacionRabia {

    private int codigo;
    private int numeroDosis;
    private String fechaAplicacion;
    /**
     * @return Returns the codigo.
     */
    public int getCodigo() {
        return codigo;
    }
    /**
     * @param codigo The codigo to set.
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    /**
     * @return Returns the fechaAplicacion.
     */
    public String getFechaAplicacion() {
        return fechaAplicacion;
    }
    /**
     * @param fechaAplicacion The fechaAplicacion to set.
     */
    public void setFechaAplicacion(String fechaAplicacion) {
        this.fechaAplicacion = fechaAplicacion;
    }
    /**
     * @return Returns the numeroDosis.
     */
    public int getNumeroDosis() {
        return numeroDosis;
    }
    /**
     * @param numeroDosis The numeroDosis to set.
     */
    public void setNumeroDosis(int numeroDosis) {
        this.numeroDosis = numeroDosis;
    }
}
