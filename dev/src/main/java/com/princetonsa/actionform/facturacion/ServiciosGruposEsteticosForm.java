package com.princetonsa.actionform.facturacion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.Utilidades;



public class ServiciosGruposEsteticosForm extends ValidatorForm
{
	
	/**
	 *  Estado en el que se encuentra el proceso
	 */
	
	private String estado="";
	
	
	// Mapa para tabla servicios_grupos_esteticos
	private HashMap gruposEsteticosMap;
	
	private HashMap gruposEsteticosEliminadosMap;
	
	private String patronOrdenarGrupos;
	
	private String ultimoPatronGrupos;
	
	private int indexSeleccionado;
	
	
	// Mapa para la tabla serv_esteticos
	
	private HashMap serviciosEsteticosMap;
	
	private HashMap serviciosEsteticosEliminadosMap;
	
	private String patronOrdenar;
	
	private String ultimoPatron;
	
	/**
	 * para la navegacion del pager, cuando se ingresa
	 * un registro nuevo.
	 */
	
	private String linkSiguiente;
	
	/**
	 *  Para la navegacion del pager, cuando se ingresa
	 *  un registro nuevo.
	 */
	private int maxPageItems;
	

	
	
	/**
	 *  para controlar la página actual
     *  del pager.
	 */
	
	 private int offset;
	 
	
		
	 
	/**
	 * Posicion del registro que se eliminara
	 */
	    
	 private int posEliminar;
	 
	 /**
	  * 
	  */
	 
	 private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	 
	 
	 	/**
	     * resetea los atributos del form
	     */
		 
	  	    
	    public void reset()
	    {
	    	serviciosEsteticosMap=new HashMap();
	    	serviciosEsteticosMap.put("numRegistros", "0");
	    	serviciosEsteticosEliminadosMap=new HashMap();
	    	serviciosEsteticosEliminadosMap.put("numRegistros", "0");
	    	linkSiguiente="";
	    	this.maxPageItems=20;
	    	this.patronOrdenar="";
	    	this.ultimoPatron="";
	    	this.offset=0;
	    	this.posEliminar=ConstantesBD.codigoNuncaValido;
	    	
	    	gruposEsteticosMap=new HashMap();
	    	gruposEsteticosMap.put("numRegistros", "0");
	    	gruposEsteticosEliminadosMap=new HashMap();
	    	gruposEsteticosEliminadosMap.put("numRegistros", "0");
	    	this.patronOrdenarGrupos="";
	    	this.ultimoPatronGrupos="";
	    	this.indexSeleccionado=ConstantesBD.codigoNuncaValido;
	    	
	    }
	    
	    
	    
	    
	    /**
		 *  Validate the properties that have been set from this HTTP request, and
		 *  return an <code>ActionErrors</code> object that encapsulates any 
		 *  validation errors that have been found. If no errors are found, return
		 *  <code>null</code> or an <code>ActionErrors</code> object with no recorded
		 *  error messages.
		 *  @param mapping The mapping used to select this instance
		 *  @param request The servlet request we are processing
		 */
	    
		public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
		{
			ActionErrors errores= new ActionErrors();
			
			if(this.estado.equals("guardar"))
			{
				int numReg=Integer.parseInt(this.gruposEsteticosMap.get("numRegistros")+"");
				for(int i=0;i<numReg;i++)
				{
					if((this.gruposEsteticosMap.get("codigo_"+i)+"").trim().equals(""))
					{
						errores.add("codigo", new ActionMessage("errors.required","El Codigo del registro "+(i+1)));
					}
					if((this.gruposEsteticosMap.get("codigo_"+i)+"").length()>8)
					{
						errores.add("codigo", new ActionMessage("errors.notEspecific","Se han ingresado caracteres especiales en el codigo del registro "+(i+1)+". Por favor verifique"));
					}
					else
					{
						for(int j=0;j<i;j++)
						{
							if((this.gruposEsteticosMap.get("codigo_"+i)+"").equalsIgnoreCase(this.gruposEsteticosMap.get("codigo_"+j)+""))
							{
								errores.add("", new ActionMessage("errors.yaExiste","El código "+this.gruposEsteticosMap.get("codigo_"+i)));
							}
						}
					}
					if((this.gruposEsteticosMap.get("descripcion_"+i)+"").trim().equals(""))
					{
						errores.add("codigo", new ActionMessage("errors.required","La Descripción del registro "+(i+1)));
					}
					if((this.gruposEsteticosMap.get("descripcion_"+i)+"").length()>128)
					{
						errores.add("", new ActionMessage("errors.notEspecific","Se han ingresado caracteres especiales en la descripción del registro "+(i+1)+". Por favor verifique"));
					}
					if((this.gruposEsteticosMap.get("activo_"+i)+"").trim().equals(""))
					{
						errores.add("codigo", new ActionMessage("errors.required","Es Necesario Activar El Registro "+(i+1)));
					}
				}
			}
			/*if(this.estado.equals("guardarDetalle"))
			{
				int numReg=Integer.parseInt(this.serviciosEsteticosMap.get("numRegistros")+"");
				for(int i=0;i<numReg;i++)
				{
					if((this.serviciosEsteticosMap.get("codigoServicio_"+i)+"").trim().equals(""))
					{
						errores.add("codigo", new ActionMessage("errors.required","El Codigo del registro "+(i+1)));
					}
					else
					{
						for(int j=0;j<i;j++)
						{
							if((this.serviciosEsteticosMap.get("codigo_"+i)+"").equalsIgnoreCase(this.serviciosEsteticosMap.get("codigoServicio_"+j)+""))
							{
								errores.add("", new ActionMessage("errors.yaExiste","El código "+this.serviciosEsteticosMap.get("codigoServicio_"+i)));
							}
						}
					}
					if((this.serviciosEsteticosMap.get("descripcionServicio_"+i)+"").trim().equals(""))
					{
						errores.add("codigo", new ActionMessage("errors.required","La Descripción del registro "+(i+1)));
					}
					if((this.gruposEsteticosMap.get("activo_"+i)+"").trim().equals(""))
					{
						errores.add("codigo", new ActionMessage("errors.required","Es Necesario Activar El Registro "+(i+1)));
					}
				}
			}*/
			
			return errores;
		}

	    
	    
	 
	 
	/**
	 * 
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
	public String getLinkSiguiente() 
	{
		return linkSiguiente;
	}

	/**
	 * 
	 * @param linkSiguiente
	 */
	public void setLinkSiguiente(String linkSiguiente) 
	{
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * 
	 * @return
	 */
	public int getMaxPageItems() 
	{
		return maxPageItems;
	}

	/**
	 * 
	 * @param maxPageItems
	 */
	public void setMaxPageItems(int maxPageItems) 
	{
		this.maxPageItems = maxPageItems;
	}

	/**
	 * 
	 * @return
	 */
	public ResultadoBoolean getMensaje() 
	{
		return mensaje;
	}

	/**
	 * 
	 * @param mensaje
	 */
	public void setMensaje(ResultadoBoolean mensaje) 
	{
		this.mensaje = mensaje;
	}

	/**
	 * 
	 * @return
	 */
	public int getOffset() 
	{
		return offset;
	}

	/**
	 * 
	 * @param offset
	 */
	public void setOffset(int offset) 
	{
		this.offset = offset;
	}

	/**
	 * 
	 * @return
	 */
	public String getPatronOrdenar() 
	{
		return patronOrdenar;
	}

	/**
	 * 
	 * @param patronOrdenar
	 */
	public void setPatronOrdenar(String patronOrdenar) 
	{
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * 
	 * @return
	 */
	public int getPosEliminar() 
	{
		return posEliminar;
	}

	/**
	 * 
	 * @param posEliminar
	 */
	public void setPosEliminar(int posEliminar) 
	{
		this.posEliminar = posEliminar;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getServiciosEsteticosEliminadosMap() 
	{
		return serviciosEsteticosEliminadosMap;
	}

	/**
	 * 
	 * @param serviciosEsteticosEliminadosMap
	 */
	public void setServiciosEsteticosEliminadosMap(
			HashMap serviciosEsteticosEliminadosMap) 
	{
		this.serviciosEsteticosEliminadosMap = serviciosEsteticosEliminadosMap;
	}
	
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getServiciosEsteticosEliminadosMap(String key)
	{
		return serviciosEsteticosEliminadosMap.get(key);
	}

	
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	
	public void setServiciosEsteticosEliminadosMap(String key,Object value)
	{
		this.serviciosEsteticosEliminadosMap.put(key, value);
	}

	

	
	/**
	 * 
	 * @return
	 */
	public HashMap getServiciosEsteticosMap() 
	{
		return serviciosEsteticosMap;
	}


	/**
	 * 
	 * @param serviciosEsteticosMap
	 */
	public void setServiciosEsteticosMap(HashMap serviciosEsteticosMap) 
	{
		this.serviciosEsteticosMap = serviciosEsteticosMap;
	}
	
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getServiciosEsteticosMap(String key) 
	{
		return serviciosEsteticosMap.get(key);
	}

	
	
	/**
	 * 
	 * @param serviciosEsteticosMap the serviciosEsteticosMap to set
	 */
	
	public void setServiciosEsteticosMap(String key,Object value) 
	{
		this.serviciosEsteticosMap.put(key, value);
	}

	


	/**
	 * 
	 * @return
	 */
	public String getUltimoPatron() 
	{
		return ultimoPatron;
	}

	
	/**
	 * 
	 * @param ultimoPatron
	 */
	public void setUltimoPatron(String ultimoPatron) 
	{
		this.ultimoPatron = ultimoPatron;
	}



	/**
	 * 
	 */
	public HashMap getGruposEsteticosEliminadosMap() 
	{
		return gruposEsteticosEliminadosMap;
	}



	/**
	 * 
	 * @param gruposEsteticosEliminadosMap
	 */
	public void setGruposEsteticosEliminadosMap(HashMap gruposEsteticosEliminadosMap) 
	{
		this.gruposEsteticosEliminadosMap = gruposEsteticosEliminadosMap;
	}
	
	
	/**
	 * 
	 * @param Key
	 * @return
	 */
	public Object getGruposEsteticosEliminadosMap(String Key)
	{
		return gruposEsteticosEliminadosMap.get(Key);
	}
	
	
	/**
	 * 
	 * @param Key
	 * @param value
	 */
	public void setGruposEsteticosEliminadosMap(String Key,Object value)
	{
		this.gruposEsteticosEliminadosMap.put(Key, value);
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	public HashMap getGruposEsteticosMap() 
	{
		return gruposEsteticosMap;
	}



	/**
	 * 
	 * @param gruposEsteticosMap
	 */
	public void setGruposEsteticosMap(HashMap gruposEsteticosMap) 
	{
		this.gruposEsteticosMap = gruposEsteticosMap;
	}
	
	
	/**
	 * 
	 * @param Key
	 * @return
	 */
	public Object getGruposEsteticosMap(String Key)
	{
		return gruposEsteticosMap.get(Key);
	}
	
	
	/**
	 * @param gruposesteticosMap the gruposEsteticosMap to set
	 * @param Key
	 * @param value
	 */
	public void setGruposEsteticosMap(String Key,Object value)
	{
		this.gruposEsteticosMap.put(Key, value);
	}
	

	
	/**
	 * 
	 * @return
	 */
	public String getPatronOrdenarGrupos() 
	{
		return patronOrdenarGrupos;
	}



	/**
	 * 
	 * @param patronOrdenarGrupos
	 */
	public void setPatronOrdenarGrupos(String patronOrdenarGrupos) 
	{
		this.patronOrdenarGrupos = patronOrdenarGrupos;
	}



	/**
	 * 
	 * @return
	 */
	public String getUltimoPatronGrupos() 
	{
		return ultimoPatronGrupos;
	}



	/**
	 * 
	 * @param ultimoPatronGrupos
	 */
	public void setUltimoPatronGrupos(String ultimoPatronGrupos) 
	{
		this.ultimoPatronGrupos = ultimoPatronGrupos;
	}


	/**
	 * 
	 * @return
	 */
	public int getIndexSeleccionado() {
		return indexSeleccionado;
	}


	/**
	 * 
	 * @param indexSeleccionado
	 */
	public void setIndexSeleccionado(int indexSeleccionado) {
		this.indexSeleccionado = indexSeleccionado;
	}




		

}
