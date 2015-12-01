/*
 * @(#)RespuestaHashMap.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_05
 *
 */

package util;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Clase contenedora, cuyo �nico objetivo es manejar 
 * una pareja entero y HashMap, donde el entero indica
 * el n�mero de elementos que est� manejando el HashMap
 * La idea es manejar esta clase junto al m�todo 
 * autom�tico de UtilidadBD. Recuerde que si agrega nuevos
 * elementos (filas), debe aumentar el tama�o
 * 
 * @version 1.0 Nov 24, 2004
 */
public class RespuestaHashMap implements Serializable{

    /**
     * Mapa utilizado por esta clase
     */
    private HashMap mapa;
    
    /**
     * Numero de registros almacenados
     * en el mapa (este n�mero difiere
     * del size)
     */
    private int tamanio;
    
    /**
     * Constructor del objeto vac�o,
     * la idea es evitar inconsistencias
     * en la creaci�n. Un mapa vac�o no
     * tiene elementos (0 elementos)
     */
    public RespuestaHashMap ()
    {
        mapa=new HashMap();
        tamanio=0;
    }
    
    /**
     * Constructor legal de este objeto
     * 
     * @param mapa
     * @param tamanio
     */
    public RespuestaHashMap (HashMap mapa, int tamanio)
    {
        this.mapa=mapa;
        this.tamanio=tamanio;
    }
    
    /**
     * M�todo que limpia este objeto
     */
    public void limpiar()
    {
        this.mapa.clear();
        this.tamanio=0;
        
    }
    
    /**
     * @return Retorna el/la mapa.
     */
    public HashMap getMapa() {
        return mapa;
    }
    /**
     * @return Retorna el/la tamanio.
     */
    public int getTamanio() {
        return tamanio;
    }
    /**
     * @param tamanio El/La tamanio a establecer.
     */
    public void setTamanio(int tamanio) {
        this.tamanio = tamanio;
    }
}
