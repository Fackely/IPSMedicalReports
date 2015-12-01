/*
 * Created on 2/12/2005
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 *
 */
package com.princetonsa.actionform.inventarios;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

/**
 * @version 1.0, 2/12/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class ArticulosPuntoPedidoForm extends ValidatorForm
{
    
    /**
     * Variable para manejar el estado de la funcionalidad
     */
    private String estado;
    /**
     * Mapa para manejar los articulos y sus existencias
     */
    private HashMap articulos;
    
        
    /**
     * Patron por el que se desea ordenar el listado
     */
    private String patronOrdenar;
    
    /**
     * Ultimo Patron por el que se ordena.
     */
    private String ultimoPatron;
    
    /**
     * Porcentaje Alerta para realizar la busqueda
     */
    private String porcentajeAleta;
    
    /**
     * Numero de items mostrados en la pagina
     */
    private int maxPageItems;
    
    /**
     * Variables para la busqueda avanzada.
     */
    private String codBusqueda;
    private String desBusqueda;
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		return errores;
	}
    
    /**
     * Reset de los atributos  
     */
    public void reset()
    {
        this.articulos=new HashMap();
        this.articulos.put("numRegistros", "0");
        this.patronOrdenar="";
        this.ultimoPatron="";
        this.porcentajeAleta="0.0";
        this.maxPageItems=10;
        this.codBusqueda="";
        this.desBusqueda="";
    }

    /**
     * @return Returns the articulos.
     */
    public HashMap getArticulos()
    {
        return articulos;
    }
    /**
     * @param articulos The articulos to set.
     */
    public void setArticulos(HashMap articulos)
    {
        this.articulos = articulos;
    }
    /**
     * @return Returns the articulos.
     */
    public Object getArticulos(String key)
    {
        return articulos.get(key);
    }
    /**
     * @param articulos The articulos to set.
     */
    public void setArticulos(String key,Object value)
    {
        this.articulos.put(key, value);
    }    
    /**
     * @return Returns the estado.
     */
    public String getEstado()
    {
        return estado;
    }
    /**
     * @param estado The estado to set.
     */
    public void setEstado(String estado)
    {
        this.estado = estado;
    }
    /**
     * @return Returns the patronOrdenar.
     */
    public String getPatronOrdenar()
    {
        return patronOrdenar;
    }
    /**
     * @param patronOrdenar The patronOrdenar to set.
     */
    public void setPatronOrdenar(String patronOrdenar)
    {
        this.patronOrdenar = patronOrdenar;
    }
    /**
     * @return Returns the ultimoPatron.
     */
    public String getUltimoPatron()
    {
        return ultimoPatron;
    }
    /**
     * @param ultimoPatron The ultimoPatron to set.
     */
    public void setUltimoPatron(String ultimoPatron)
    {
        this.ultimoPatron = ultimoPatron;
    }
    /**
     * @return Returns the maxPageItems.
     */
    public int getMaxPageItems()
    {
        return maxPageItems;
    }
    /**
     * @param maxPageItems The maxPageItems to set.
     */
    public void setMaxPageItems(int maxPageItems)
    {
        this.maxPageItems = maxPageItems;
    }
    /**
     * @return Returns the porcentajeAleta.
     */
    public String getPorcentajeAleta()
    {
        return porcentajeAleta;
    }
    /**
     * @param porcentajeAleta The porcentajeAleta to set.
     */
    public void setPorcentajeAleta(String porcentajeAleta)
    {
        this.porcentajeAleta = porcentajeAleta;
    }
    /**
     * @return Returns the codBusqueda.
     */
    public String getCodBusqueda()
    {
        return codBusqueda;
    }
    /**
     * @param codBusqueda The codBusqueda to set.
     */
    public void setCodBusqueda(String codBusqueda)
    {
        this.codBusqueda = codBusqueda;
    }
    /**
     * @return Returns the desBusqueda.
     */
    public String getDesBusqueda()
    {
        return desBusqueda;
    }
    /**
     * @param desBusqueda The desBusqueda to set.
     */
    public void setDesBusqueda(String desBusqueda)
    {
        this.desBusqueda = desBusqueda;
    }
}
