/*
 * Created on 23/11/2005
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

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadTexto;

/** 
 * @version 1.0, 23/11/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class MotDevolucionInventariosForm extends ValidatorForm
{
    /**
     * Estado de la funcionalidad.
     */
    private String estado;
    
    /**
     * Mapa para manejar toda la informacion de un motivo de una devolucion de inventarios.
     */
    private HashMap motivos;
    
    /**
     * Variable para manejar la institucion.
     */
    private int institucion;
    
    /**
     * Motivo eliminar
     */
    private int posEliminar;
    
    /**
     * Patron por el que se desea ordenar el listado
     */
    private String patronOrdenar;
    
    /**
     * Ultimo Patron por el que se ordena.
     */
    private String ultimoPatron;
    /**
     * Validate de la funcionalidad
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		if(estado.equals("guardar"))
		{
		    for(int i=0;i<Integer.parseInt(motivos.get("numRegistros")+"");i++)
		    {
		    	if(!UtilidadTexto.getBoolean(motivos.get("eliminado_"+i)+""))
		    	{
			        if((motivos.get("codigo_"+i)+"").trim().equals(""))
			        {
			            errores.add("CODIGO REQUERIDO", new ActionMessage("errors.required","El código para el registro "+(i+1)));
			        }
			        else
			        {
				        for(int j=0;j<i;j++)
				        {
				            if((motivos.get("codigo_"+i)+"").equals(motivos.get("codigo_"+j)+""))
					        {
				                errores.add("YA EXISTE EL REGISTRO.", new ActionMessage("errors.yaExiste","El código "+motivos.get("codigo_"+i)));
					        }
				        }
			        }
			        if((motivos.get("descripcion_"+i)+"").trim().equals(""))
			        {
			            errores.add("DESCRIPCION REQUERIDO", new ActionMessage("errors.required","La descripcion para el registro "+(i+1)));
			        }
			        else
			        {
				        for(int j=0;j<i;j++)
				        {
				            if((motivos.get("descripcion_"+i)+"").equals(motivos.get("descripcion_"+j)+""))
					        {
				                errores.add("YA EXISTE EL REGISTRO.", new ActionMessage("errors.yaExiste","La Descripcion \""+motivos.get("descripcion_"+i)+"\""));
					        }
				            
				        }
			        }
		    	}
		    }
		}
		return errores;
	}
    
    /**
     * Reset de los atributos  
     */
    public void reset()
    {
        this.motivos=new HashMap();
        this.institucion=ConstantesBD.codigoNuncaValido;
        this.posEliminar=ConstantesBD.codigoNuncaValido;
        this.patronOrdenar="";
        this.ultimoPatron="";
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
     * @return Returns the institucion.
     */
    public int getInstitucion()
    {
        return institucion;
    }
    /**
     * @param institucion The institucion to set.
     */
    public void setInstitucion(int institucion)
    {
        this.institucion = institucion;
    }
    /**
     * @return Returns the motivos.
     */
    public HashMap getMotivos()
    {
        return motivos;
    }
    /**
     * @param motivos The motivos to set.
     */
    public void setMotivos(HashMap motivos)
    {
        this.motivos = motivos;
    }
    /**
     * @return Returns the motivos.
     */
    public Object getMotivos(String key)
    {
        return motivos.get(key);
    }
    /**
     * @param motivos The motivos to set.
     */
    public void setMotivos(String key,Object value)
    {
        this.motivos.put(key, value);
    }    
    /**
     * @return Returns the posEliminar.
     */
    public int getPosEliminar()
    {
        return posEliminar;
    }
    /**
     * @param posEliminar The posEliminar to set.
     */
    public void setPosEliminar(int posEliminar)
    {
        this.posEliminar = posEliminar;
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
}
