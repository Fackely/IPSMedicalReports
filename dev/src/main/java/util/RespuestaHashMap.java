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
 * Clase contenedora, cuyo único objetivo es manejar 
 * una pareja entero y HashMap, donde el entero indica
 * el número de elementos que está manejando el HashMap
 * La idea es manejar esta clase junto al método 
 * automático de UtilidadBD. Recuerde que si agrega nuevos
 * elementos (filas), debe aumentar el tamaño
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
     * en el mapa (este número difiere
     * del size)
     */
    private int tamanio;
    
    /**
     * Constructor del objeto vacío,
     * la idea es evitar inconsistencias
     * en la creación. Un mapa vacío no
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
     * Método que limpia este objeto
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
