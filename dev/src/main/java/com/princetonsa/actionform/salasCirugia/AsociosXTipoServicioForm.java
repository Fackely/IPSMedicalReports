package com.princetonsa.actionform.salasCirugia;


import java.io.IOException;
import java.util.HashMap;
import java.util.jar.JarException;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import org.apache.struts.action.ActionMessage;
import org.omg.CORBA.portable.ValueOutputStream;

import util.ConstantesBD;
import util.Utilidades;

/**
 * 
 * @author Juan Sebastian Castaño
 * Clase FORMA de la funcionalidad Asocios por tipo de servicio
 */

public class AsociosXTipoServicioForm extends ValidatorForm {
	
	/**
	 * id de clase
	 */
	private static final long serialVersionUID = 1L;


	/* estado del formulario */
	private String estado;

	/**
	 * Posicion del registro a eliminar
	 */
	private String codigoEliminarPos;
	
	
	/**
	 * Posicion del registro a modificar
	 */
	private String codigoModificarPos;
	
	/**
	 * Variables para permitir la ordenacion por columnas en una consulta
	 */
	private String indice;
	private String ultimoIndice;
	
	/* Mapa de resultados */	
	private HashMap<String, Object> mapaServAsocios;
	
	
	/* Mapa de resultados de tipos de servicio para el select */	
	private HashMap<String, Object> mapaTiposServicio;
	
	/* Mapa de resultados de tipos de servicio para el select */	
	private HashMap<String, Object> mapaTiposAsocio;
	
	
	/* Numero de registros en el mapa */
	private int NumMapaServAsocios;
	
	
	public void clean()
	{
		this.estado = "";
		this.NumMapaServAsocios = 0;
		this.mapaServAsocios = new HashMap<String, Object>();
		this.mapaTiposServicio = new HashMap<String, Object>();
		this.mapaTiposAsocio = new HashMap<String, Object>();
		this.indice = "";
		this.ultimoIndice = "";
		this.codigoEliminarPos = "";
		this.codigoModificarPos = "";
	}
	
	
	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}


	/**
	 * @return the mapaServAsocios
	 */
	public HashMap<String, Object> getMapaServAsocios() {
		return mapaServAsocios;
	}


	/**
	 * @param mapaServAsocios the mapaServAsocios to set
	 */
	public void setMapaServAsocios(HashMap<String, Object> mapaServAsocios) {
		this.mapaServAsocios = mapaServAsocios;
	}
	
	public void setMapaServAsocios(String  key, Object obj) {
		this.mapaServAsocios.put(key, obj);
	}
	
	public Object getMapaServAsocios(String key) {
		return mapaServAsocios.get(key);
	}


	/**
	 * @return the numMapaServAsocios
	 */
	public int getNumMapaServAsocios() {
		return Utilidades.convertirAEntero(this.mapaServAsocios.get("numRegistros")+"", true);
		//return Utilidades.convertirAEntero((this.mapaServAsocios.get("numRegistros").toString().equals("")?"0":this.mapaServAsocios.get("numRegistros"))+"", true);
	}


	/**
	 * @param numMapaServAsocios the numMapaServAsocios to set
	 */
	public void setNumMapaServAsocios(int numMapaServAsocios) {
		this.NumMapaServAsocios = numMapaServAsocios;
	}


	/**
	 * @return the indice
	 */
	public String getIndice() {
		return indice;
	}


	/**
	 * @param indice the indice to set
	 */
	public void setIndice(String indice) {
		this.indice = indice;
	}


	/**
	 * @return the ultimoIndice
	 */
	public String getUltimoIndice() {
		return ultimoIndice;
	}


	/**
	 * @param ultimoIndice the ultimoIndice to set
	 */
	public void setUltimoIndice(String ultimoIndice) {
		this.ultimoIndice = ultimoIndice;
	}


	/**
	 * @return the mapaTiposServicio
	 */
	public HashMap<String, Object> getMapaTiposServicio() {
		return mapaTiposServicio;
	}
	
	/**
	 * @return the mapaTiposServicio
	 */
	public Object getMapaTiposServicio(String key) {
		return mapaTiposServicio.get(key);
	}

	
	/**
	 * @param mapaTiposServicio the mapaTiposServicio to set
	 */
	public void setMapaTiposServicio(String key, Object obj) {
		this.mapaTiposServicio.put(key, obj);
	}

	/**
	 * @param mapaTiposServicio the mapaTiposServicio to set
	 */
	public void setMapaTiposServicio(HashMap<String, Object> mapaTiposServicio) {
		this.mapaTiposServicio = mapaTiposServicio;
	}


	/**
	 * @return the mapaTiposAsocio
	 */
	public HashMap<String, Object> getMapaTiposAsocio() {
		return mapaTiposAsocio;
	}


	/**
	 * @param mapaTiposAsocio the mapaTiposAsocio to set
	 */
	public void setMapaTiposAsocio(HashMap<String, Object> mapaTiposAsocio) {
		this.mapaTiposAsocio = mapaTiposAsocio;
	}
	
	/**
	 * @return the mapaTiposAsocio
	 */
	public Object getMapaTiposAsocio(String key) {
		return mapaTiposAsocio.get(key);
	}


	/**
	 * @param mapaTiposAsocio the mapaTiposAsocio to set
	 */
	public void setMapaTiposAsocio(String key, Object obj) {
		this.mapaTiposAsocio.put(key, obj);
	}


	/**
	 * @return the codigoEliminarPos
	 */
	public String getCodigoEliminarPos() {
		return codigoEliminarPos;
	}


	/**
	 * @param codigoEliminarPos the codigoEliminarPos to set
	 */
	public void setCodigoEliminarPos(String codigoEliminarPos) {
		this.codigoEliminarPos = codigoEliminarPos;
	}


	/**
	 * @return the codigoModificarPos
	 */
	public String getCodigoModificarPos() {
		return codigoModificarPos;
	}


	/**
	 * @param codigoModificarPos the codigoModificarPos to set
	 */
	public void setCodigoModificarPos(String codigoModificarPos) {
		this.codigoModificarPos = codigoModificarPos;
	}
	
	
	/**
	 * Metodo de validacion de campos a guardar.
	 */	
	 public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	    {
	        ActionErrors errores = new ActionErrors();
	        boolean fueMod = false;
	        if(this.estado.equals("guardar") || this.estado.equals("guardarModificado"))
		     {
	        	//System.out.print("\n \n el valor derl mapa es --> "+this.getMapaServAsocios());
	        	// como se maneja solamente un registro a la vez, para insercion se conoce que la ultima posicion del mapa sera el nuevo registro
		        int pos;
		       
		        
		        HashMap<String, Object> tempMapServAso = new HashMap<String, Object>();
				tempMapServAso = this.getMapaServAsocios();
				
				pos = Integer.parseInt(tempMapServAso.get("numRegistros").toString());
				// ultima posicion real del mapa
				pos --;
				
				 for (int i=0; i < this.getNumMapaServAsocios(); i++)
				 {
					 if(this.estado.equals("guardar"))
					 {
						 // campos de guardado de un nuevo registro
						 // si en algun campo del mapa el tipo de servicio y asocio son iguales a los nuevos no permitirlo
						 if (i != pos)
						 {
							 if (this.getMapaServAsocios("codigoTipoServicio_"+i).toString().equals(this.getMapaServAsocios("codigoTipoServicio_"+pos).toString()) && this.getMapaServAsocios("codigoTipoAsocio_"+i).toString().equals(this.getMapaServAsocios("codigoTipoAsocio_"+pos).toString().trim()))
							 {
								 // error de llaves duplicadas
								 errores.add("",new ActionMessage("errors.warnings","Tipo de Asocio y Tipo de Servicio ya existentes, cambielos, dado que no se pueden insertar registros duplicados."));								
							 }
						 }
						 if(this.getMapaServAsocios("codigoTipoServicio_"+i).toString().equals(""))
						 {
							 errores.add("",new ActionMessage("errors.required","Tipo de Servicio, "));
						 }
						 if(this.getMapaServAsocios("codigoTipoAsocio_"+i).toString().equals(ConstantesBD.codigoNuncaValido+""))
						 {
							 errores.add("",new ActionMessage("errors.required","Tipo de Asocio, "));
						 }
						 if(this.getMapaServAsocios("servicio_"+i).toString().equals("") || Integer.parseInt(this.getMapaServAsocios("servicio_"+i).toString()) == ConstantesBD.codigoNuncaValido)
						 {
								 errores.add("",new ActionMessage("errors.required","El Servicio."));
						 }
							 
						 
						
					 }
					 else if (this.estado.equals("guardarModificado"))
					 {				
						 
						 
						 // campos de guardado de un nuevo registro
						 // si en algun campo del mapa el tipo de servicio y asocio son iguales a los modificados no permitirlo
						 
						 if (i != Integer.parseInt(this.getCodigoModificarPos()))
						 {
							 if (this.getMapaServAsocios("codigoTipoServicio_"+i).toString().equals(this.getMapaServAsocios("codigoTipoServicio_"+Integer.parseInt(this.getCodigoModificarPos())).toString()) && this.getMapaServAsocios("codigoTipoAsocio_"+i).toString().equals(this.getMapaServAsocios("codigoTipoAsocio_"+Integer.parseInt(this.getCodigoModificarPos())).toString().trim()))
							 {
								 // error de llaves duplicadas
								 errores.add("",new ActionMessage("errors.warnings","Tipo de Asocio y Tipo de Servicio ya existentes, cambielos, dado que no se pueden insertar registros duplicados."));								
							 }
						 }
						 
						 
						 // solamente comparar los campos del registro que se va ha modificar						 
						 if ( i == Integer.parseInt(this.getCodigoModificarPos()) )
						 {
							
							 if(this.getMapaServAsocios("codigoTipoServicio_"+i).toString().equals("-1"))
							 {
								 errores.add("",new ActionMessage("errors.required","Tipo de Servicio, "));
							 }
							 //if(this.getMapaServAsocios("codigoTipoAsocio_"+i).toString().equals(""))
							 if (Integer.parseInt(this.getMapaServAsocios("codigoTipoAsocio_"+i).toString().trim()) == ConstantesBD.codigoNuncaValido)
							 {
								 errores.add("",new ActionMessage("errors.required","Tipo de Asocio, "));
								
							 }
							 //if(this.getMapaServAsocios("servicio_"+i).toString().equals("") || Integer.parseInt(this.getMapaServAsocios("servicio_"+i).toString()) == ConstantesBD.codigoNuncaValido)
							 if(Integer.parseInt(this.getMapaServAsocios("servicio_"+i).toString()) == ConstantesBD.codigoNuncaValido)
							 {
								errores.add("",new ActionMessage("errors.required","El Servicio."));
								
							 }
							 // bandera de evaluacion de tipo de error, si fue en form de mod o en form de indercion.
							 fueMod = true;
							
						 }
						 
					}
				
				 }
				 
				 if(!errores.isEmpty())
				 {
					 // evaluar a que estado se debe enviar, para evitar que se pierda el formulario de captura de datos.
					 if (fueMod)					 
						 this.estado = "modificar";
					 else
						 this.estado = "nuevo";
					 //this.estado = "";
				 }
		        		
		        		
				 
		     }
			
			return errores;
	    }

}
