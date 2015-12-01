package com.princetonsa.actionform.historiaClinica;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;

public class MotivosSircForm extends ValidatorForm
{
	
	/**
	 * 
	 */
	private String estado;
	
	/**
	 * 
	 */
	private HashMap motivosSirc;
	
	/**
	 * 
	 */
	private HashMap motivosSircEliminado;
	
	/**
	 * 
	 */
	private String patronOrdenar;
	
	/**
	 * 
	 */
	private String ultimoPatron;
	
	 /**
     * para la nevagación del pager, cuando se ingresa
     * un registro nuevo.
     */
    private int maxPageItems;

	 /**
     * para la nevagación del pager, cuando se ingresa
     * un registro nuevo.
     */
    private String linkSiguiente;
    
   /**
     * para controlar la página actual
     * del pager.
     */
    private int offset;
    
    /**
     * Posicion del registro que se eliminara
     */
    private int posEliminar;
     
    /**
     * 
     */
    private ResultadoBoolean mostrarMensaje=new ResultadoBoolean(false);
    
    
	/**
	 * 
	 *
	 */
	public void reset()
	{
		this.motivosSirc=new HashMap();
		this.motivosSirc.put("numRegistros", "0");
		this.motivosSircEliminado=new HashMap();
		this.motivosSircEliminado.put("numRegistros", "0");
		this.linkSiguiente="motivosSirc.do";
		this.maxPageItems=20;
		this.offset=0;
		this.posEliminar=ConstantesBD.codigoNuncaValido;
		this.patronOrdenar="";
		this.ultimoPatron="";
	}

	/**
	 * 
	 */
	 public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		if(this.estado.equals("guardar"))
		{
			int numReg=Integer.parseInt(this.motivosSirc.get("numRegistros")+"");
			for(int i=0;i<numReg;i++)
			{
				if((this.motivosSirc.get("codigo_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","El Codigo del registro "+(i+1)));
				}
				else
				{
					try
					{
						Double.parseDouble(this.motivosSirc.get("codigo_"+i)+"");
						for(int j=0;j<i;j++)
						{
							if((this.motivosSirc.get("codigo_"+i)+"").equalsIgnoreCase(this.motivosSirc.get("codigo_"+j)+""))
							{
								errores.add("", new ActionMessage("errors.yaExiste","El código "+this.motivosSirc.get("codigo_"+i)));
							}
						}
					}
					catch (Exception e) 
					{
						errores.add("", new ActionMessage("errors.integer","El código "+this.motivosSirc.get("codigo_"+i)));
					}
					
				}
				if((this.motivosSirc.get("descripcion_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","La Descripción del registro "+(i+1)));
				}
				if((this.motivosSirc.get("tipomotivo_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","El Tipo Motivo del registro "+(i+1)));
				}
			}
		}
		return errores;
	}

	 /**
	  * 
	  * @return
	  */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * 
	 * @param estado
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMotivosSirc()
	{
		return motivosSirc;
	}

	/**
	 * 
	 * @param motivosSirc
	 */
	public void setMotivosSirc(HashMap motivosSirc)
	{
		this.motivosSirc = motivosSirc;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMotivosSirc(String key)
	{
		return motivosSirc.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public  void setMotivosSirc(String key,Object value)
	{
		this.motivosSirc.put(key, value);
	}
	

	public String getLinkSiguiente()
	{
		return linkSiguiente;
	}

	public void setLinkSiguiente(String linkSiguiente)
	{
		this.linkSiguiente = linkSiguiente;
	}

	public int getMaxPageItems()
	{
		return maxPageItems;
	}

	public void setMaxPageItems(int maxPageItems)
	{
		this.maxPageItems = maxPageItems;
	}

	public int getOffset()
	{
		return offset;
	}

	public void setOffset(int offset)
	{
		this.offset = offset;
	}

	public int getPosEliminar()
	{
		return posEliminar;
	}

	public void setPosEliminar(int posEliminar)
	{
		this.posEliminar = posEliminar;
	}

	public HashMap getMotivosSircEliminado()
	{
		return motivosSircEliminado;
	}

	public void setMotivosSircEliminado(HashMap motivosSircEliminado)
	{
		this.motivosSircEliminado = motivosSircEliminado;
	}
	public Object getMotivosSircEliminado(String key)
	{
		return motivosSircEliminado.get(key);
	}

	public void setMotivosSircEliminado(String key,Object value)
	{
		this.motivosSircEliminado.put(key, value);
	}

	public String getPatronOrdenar()
	{
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar)
	{
		this.patronOrdenar = patronOrdenar;
	}

	public String getUltimoPatron()
	{
		return ultimoPatron;
	}

	public void setUltimoPatron(String ultimoPatron)
	{
		this.ultimoPatron = ultimoPatron;
	}

	public ResultadoBoolean getMostrarMensaje() {
		return mostrarMensaje;
	}

	public void setMostrarMensaje(ResultadoBoolean mostrarMensaje) {
		this.mostrarMensaje = mostrarMensaje;
	}
}
