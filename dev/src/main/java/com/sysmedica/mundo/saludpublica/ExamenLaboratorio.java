/*
 * Creado en 15-jul-2005
 * 
 * Autor Santiago Salamanca
 * SysMédica
 */
package com.sysmedica.mundo.saludpublica;

/**
 * @author santiago
 *
 */
public class ExamenLaboratorio {
    
    private String codigoExamenLaboratorio;
    private String fechaTomaPrueba;
    private String resultadoPrueba;
    private String fechaResultadoPrueba;
    private String tipo;
    /**
     * @return Returns the codigoExamenLaboratorio.
     */
    public String getCodigoExamenLaboratorio() {
        return codigoExamenLaboratorio;
    }
    /**
     * @param codigoExamenLaboratorio The codigoExamenLaboratorio to set.
     */
    public void setCodigoExamenLaboratorio(String codigoExamenLaboratorio) {
        this.codigoExamenLaboratorio = codigoExamenLaboratorio;
    }
    /**
     * @return Returns the fechaResultadoPrueba.
     */
    public String getFechaResultadoPrueba() {
        return fechaResultadoPrueba;
    }
    /**
     * @param fechaResultadoPrueba The fechaResultadoPrueba to set.
     */
    public void setFechaResultadoPrueba(String fechaResultadoPrueba) {
        this.fechaResultadoPrueba = fechaResultadoPrueba;
    }
    /**
     * @return Returns the fechaTomaPrueba.
     */
    public String getFechaTomaPrueba() {
        return fechaTomaPrueba;
    }
    /**
     * @param fechaTomaPrueba The fechaTomaPrueba to set.
     */
    public void setFechaTomaPrueba(String fechaTomaPrueba) {
        this.fechaTomaPrueba = fechaTomaPrueba;
    }
    /**
     * @return Returns the resultadoPrueba.
     */
    public String getResultadoPrueba() {
        return resultadoPrueba;
    }
    /**
     * @param resultadoPrueba The resultadoPrueba to set.
     */
    public void setResultadoPrueba(String resultadoPrueba) {
        this.resultadoPrueba = resultadoPrueba;
    }
    /**
     * @return Returns the tipo.
     */
    public String getTipo() {
        return tipo;
    }
    /**
     * @param tipo The tipo to set.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
