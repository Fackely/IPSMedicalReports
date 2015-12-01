/*
 * Creado en 04-ago-2005
 * 
 * Autor Santiago Salamanca
 * SysMédica
 */
package com.sysmedica.mundo.saludpublica;



/**
 * @author santiago
 *
 */
public class DosisRabia {
    
    private int codigoDosis;
    private int codigoFicha;
        
    private int numero;
    private String fecha;
    
    
    /**
     * Metodo para resetear los atributos
     *
     */
    public void reset() {
        numero = 0;
        fecha = "";
    }
    
    /**
     * @return Returns the codigoDosis.
     */
    public int getCodigoDosis() {
        return codigoDosis;
    }
    /**
     * @param codigoDosis The codigoDosis to set.
     */
    public void setCodigoDosis(int codigoDosis) {
        this.codigoDosis = codigoDosis;
    }
    /**
     * @return Returns the codigoFicha.
     */
    public int getCodigoFicha() {
        return codigoFicha;
    }
    /**
     * @param codigoFicha The codigoFicha to set.
     */
    public void setCodigoFicha(int codigoFicha) {
        this.codigoFicha = codigoFicha;
    }
    /**
     * @return Returns the fecha.
     */
    public String getFecha() {
        return fecha;
    }
    /**
     * @param fecha The fecha to set.
     */
    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
    /**
     * @return Returns the numero.
     */
    public int getNumero() {
        return numero;
    }
    /**
     * @param numero The numero to set.
     */
    public void setNumero(int numero) {
        this.numero = numero;
    }
}
