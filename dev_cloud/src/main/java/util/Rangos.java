/*
 * @(#)Rangos.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package util;

import java.io.Serializable;

/**
 * 	Clase para la centralizacion de rangos.
 *	
 *	@author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 * @version 1.0, Abril 11, 2005
 */
public class Rangos implements Serializable 
{
    /**
     * Rango Inicial String
     */
    private String rangoInicial;
    
    /**
     * Rango Final String
     */
    private String rangoFinal;

    /**
     * Cod del estado
     */
    private InfoDatosInt estado;
    
    /**
     * Constructor
     *
     */
    public Rangos()
    {
        this.rangoInicial="";
        this.rangoFinal="";
        this.estado=new InfoDatosInt();
    }
    
    /**
     * Constructor
     * @param codigo
     * @param rangoInicial
     * @param rangoFinal
     * @param codigoEstado
     * @param descripcionEstado
     */
    public Rangos(String rangoInicial, String rangoFinal, int codigoEstado,  String descripcionEstado)
    {
        this.rangoInicial= rangoInicial;
        this.rangoFinal=rangoFinal;
        this.estado.setCodigo(codigoEstado);
        this.estado.setDescripcion(descripcionEstado);
    }
    
    /**
     * resetea todos los valores
     *
     */
    public void clean()
    {
        this.rangoInicial="";
        this.rangoFinal="";
        this.estado=new InfoDatosInt();
    }
    
    /**
     * @return Returns the estado.
     */
    public InfoDatosInt getEstado() {
        return estado;
    }
    /**
     * @param estado The estado to set.
     */
    public void setEstado(InfoDatosInt estado) {
        this.estado = estado;
    }
    /**
     * @return Returns the rangoFinal.
     */
    public String getRangoFinal() {
        return rangoFinal;
    }
    /**
     * @param rangoFinal The rangoFinal to set.
     */
    public void setRangoFinal(String rangoFinal) {
        this.rangoFinal = rangoFinal;
    }
    /**
     * @return Returns the rangoInicial.
     */
    public String getRangoInicial() {
        return rangoInicial;
    }
    /**
     * @param rangoInicial The rangoInicial to set.
     */
    public void setRangoInicial(String rangoInicial) {
        this.rangoInicial = rangoInicial;
    }
    
    /**
     * asigna el codigo del estado
     * @param codigoEstado
     */
    public void setCodigoEstado(int codigoEstado){
        this.estado.setCodigo(codigoEstado);
    }
    
    /**
     * retorna el codigo del estado
     * @return
     */
    public int getCodigoEstado(){
        return this.estado.getCodigo();
    }
    
    /**
     * asigna la descripcion del estado
     * @param descEstado
     */
    public void setDescripcionEstado(String descEstado){
        this.estado.setDescripcion(descEstado);
    }
    
    /**
     * retorna la descripcion del estado
     * @return
     */
    public String getDescripcionEstado(){
        return this.estado.getDescripcion();
    }
    
    
}
