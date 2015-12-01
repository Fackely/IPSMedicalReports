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
public class Desplazamiento {

    private String codigoDesplazamiento;
    private String fecha;
    private String pais;
    private String departamento;
    private String municipio;
    
    
    /**
     * @return Returns the codigoDesplazamiento.
     */
    public String getCodigoDesplazamiento() {
        return codigoDesplazamiento;
    }
    /**
     * @param codigoDesplazamiento The codigoDesplazamiento to set.
     */
    public void setCodigoDesplazamiento(String codigoDesplazamiento) {
        this.codigoDesplazamiento = codigoDesplazamiento;
    }
    /**
     * @return Returns the departamento.
     */
    public String getDepartamento() {
        return departamento;
    }
    /**
     * @param departamento The departamento to set.
     */
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
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
     * @return Returns the municipio.
     */
    public String getMunicipio() {
        return municipio;
    }
    /**
     * @param municipio The municipio to set.
     */
    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }
    /**
     * @return Returns the pais.
     */
    public String getPais() {
        return pais;
    }
    /**
     * @param pais The pais to set.
     */
    public void setPais(String pais) {
        this.pais = pais;
    }
}
