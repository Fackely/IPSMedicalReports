/*
 * Creado en 16-ene-2006
 * 
 * Autor Santiago Salamanca
 * SysMédica
 */
package com.sysmedica.util;

/**
 * @author santiago
 *
 */
public class SemanaEpidemiologica {
    
    private String fechaInicial;
    private String fechaFinal;
    private int numeroSemana;
    /**
     * @return Returns the fechaFinal.
     */
    public String getFechaFinal() {
        return fechaFinal;
    }
    /**
     * @param fechaFinal The fechaFinal to set.
     */
    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }
    /**
     * @return Returns the fechaInicial.
     */
    public String getFechaInicial() {
        return fechaInicial;
    }
    /**
     * @param fechaInicial The fechaInicial to set.
     */
    public void setFechaInicial(String fechaInicial) {
        this.fechaInicial = fechaInicial;
    }
    /**
     * @return Returns the numeroSemana.
     */
    public int getNumeroSemana() {
        return numeroSemana;
    }
    /**
     * @param numeroSemana The numeroSemana to set.
     */
    public void setNumeroSemana(int numeroSemana) {
        this.numeroSemana = numeroSemana;
    }
}
