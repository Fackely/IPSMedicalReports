package com.princetonsa.actionform;

import org.apache.struts.validator.ValidatorForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ResultadoBoolean;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

public class UbicacionGeograficaForm extends ValidatorForm {
	
	
//////////////////////////////////INCIO DE MAPAS//////////////////////////////////////////
	
	private HashMap paisMap;
	
	private HashMap departamentoMap;
	
	private HashMap ciudadMap;
	
	private HashMap localidadMap;
	
	private HashMap barrioMap;
	
	private HashMap paisEliminadoMap;
	
	private HashMap departamentoEliminadoMap;
	
	private HashMap ciudadEliminadoMap;
	
	private HashMap localidadEliminadoMap;
	
	private HashMap barrioEliminadoMap;
	
//////////////////////////////////FIN DE MAPAS//////////////////////////////////////////
	
	
	
	
	
	private String estado="", linkSiguiente;
	
	/**
	 *  Para la navegacion del pager, cuando se ingresa
	 *  un registro nuevo.
	 */
	private int maxPageItems;
	
	/**
	 * 
	 */
	private String patronOrdenar;
	
	/**
	 * 
	 */
	private String ultimoPatron;
	
	private int indexSeleccionado;
	private int indexSeleccionadoDepartamento;
	private int indexSeleccionadoCiudad;
	private int indexSeleccionadoLocalidad;
	private int indexSeleccionadoBarrio;
	
	/**
	 *  para controlar la página actual
     *  del pager.
	 */
	 private int offset;
	 
	 
	/**
	 * Posicion del registro que se eliminara
	 */
	 private int posEliminar;
	 
	 private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	 
	 public void reset(){
			
			this.paisMap=new HashMap();
			paisMap.put("numRegistros","0");

			this.departamentoMap=new HashMap();
			departamentoMap.put("numRegistros","0");
			
			this.ciudadMap=new HashMap();
			ciudadMap.put("numRegistros","0");
			
			this.localidadMap=new HashMap();
			localidadMap.put("numRegistros","0");
			
			this.barrioMap=new HashMap();
			barrioMap.put("numRegistros","0");
			
			this.paisEliminadoMap=new HashMap();
			paisEliminadoMap.put("numRegistros","0");
			
			this.departamentoEliminadoMap=new HashMap();
			departamentoEliminadoMap.put("numRegistros","0");
			
			this.ciudadEliminadoMap=new HashMap();
			ciudadEliminadoMap.put("numRegistros","0");
			
			this.localidadEliminadoMap=new HashMap();
			localidadEliminadoMap.put("numRegistros","0");
			
			this.barrioEliminadoMap=new HashMap();
			barrioEliminadoMap.put("numRegistros","0");
			
			linkSiguiente="";
	       	this.maxPageItems=10;
	    	this.patronOrdenar="";
	    	this.ultimoPatron="";
	    	this.offset=0;
	    	this.posEliminar=ConstantesBD.codigoNuncaValido;
	    	this.indexSeleccionado=ConstantesBD.codigoNuncaValido;
	    	this.indexSeleccionadoDepartamento=ConstantesBD.codigoNuncaValido;
	    	this.indexSeleccionadoCiudad=ConstantesBD.codigoNuncaValido;
	    	this.indexSeleccionadoLocalidad=ConstantesBD.codigoNuncaValido;
	    	this.indexSeleccionadoBarrio=ConstantesBD.codigoNuncaValido;
		
			
		}

	 	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
		{
			ActionErrors errores= new ActionErrors();
			boolean existe=false;
			int indice=ConstantesBD.codigoNuncaValido;
			if(this.estado.equals("guardarPais"))
			{
				int numRegPais=Integer.parseInt(this.paisMap.get("numRegistros")+"");
				for(int i=0;i<numRegPais;i++)
				{
					if((this.paisMap.get("codigo_pais_"+i)+"").trim().equals(""))
					{
						errores.add("codigo_pais", new ActionMessage("errors.required","El código del registro "+(i+1)));
					}
					else
					{
						for(int j=0;j<i;j++)
						{
							if((this.paisMap.get("codigo_pais_"+i)+"").equalsIgnoreCase(this.paisMap.get("codigo_pais_"+j)+""))
							{
								existe=true;
							}
						}
						if(existe)
							errores.add("", new ActionMessage("errors.yaExiste","El código "+this.paisMap.get("codigo_pais_"+i)));
						
					}
					if((this.paisMap.get("descripcion_"+i)+"").trim().equals(""))
					{
						errores.add("descripcion", new ActionMessage("errors.required","La descripción del registro "+(i+1)));
					}
				}
			}
			else if(this.estado.equals("guardarDepartamento"))
			{
				int numRegDepartamento=Integer.parseInt(this.departamentoMap.get("numRegistros")+"");
				existe=false;
				for(int i=0;i<numRegDepartamento;i++)
				{
					if((this.departamentoMap.get("codigo_departamento_"+i)+"").trim().equals(""))
					{
						errores.add("codigo_departamento", new ActionMessage("errors.required","El código del registro "+(i+1)));
					}
					else
					{
						for(int j=0;j<i;j++)
						{
							if(((this.departamentoMap.get("codigo_departamento_"+i)+"").equalsIgnoreCase(this.departamentoMap.get("codigo_departamento_"+j)+"")))
							{
								existe=true;
							}
						}
						if(existe)
							errores.add("", new ActionMessage("errors.yaExiste","El código "+this.departamentoMap.get("codigo_departamento_"+i)+" para el codigo de pais "+this.departamentoMap.get("codigo_pais_"+i)));
					}
					if((this.departamentoMap.get("descripcion_"+i)+"").trim().equals(""))
					{
						errores.add("descripcion", new ActionMessage("errors.required","La descripción del registro "+(i+1)));
					}
				}
			}
			else if(this.estado.equals("guardarCiudad"))
			{
				int numRegCiudad=Integer.parseInt(this.ciudadMap.get("numRegistros")+"");
				existe=false;
				for(int i=0;i<numRegCiudad;i++)
				{
					if((this.ciudadMap.get("codigo_ciudad_"+i)+"").trim().equals(""))
					{
						errores.add("codigo_ciudad", new ActionMessage("errors.required","El código del registro "+(i+1)));
					}
					else
					{
						for(int j=0;j<i;j++)
						{
							if(((this.ciudadMap.get("codigo_ciudad_"+i)+"").equalsIgnoreCase(this.ciudadMap.get("codigo_ciudad_"+j)+""))&&((this.ciudadMap.get("codigo_departamento_"+i)+"").equalsIgnoreCase(this.ciudadMap.get("codigo_departamento_"+j)+"")))
							{
								existe=true;
							}
						}
						if(existe)
							errores.add("", new ActionMessage("errors.yaExiste","El código "+this.ciudadMap.get("codigo_ciudad_"+i)+" para el codigo de departamento "+this.ciudadMap.get("codigo_departamento_"+i)));
					}
					if((this.ciudadMap.get("descripcion_"+i)+"").trim().equals(""))
					{
						errores.add("descripcion", new ActionMessage("errors.required","La descripción del registro "+(i+1)));
					}
				}
			}
			else if(this.estado.equals("guardarLocalidad"))
			{
				int numRegLocalidad=Integer.parseInt(this.localidadMap.get("numRegistros")+"");
				existe=false;
				for(int i=0;i<numRegLocalidad;i++)
				{
					if((this.localidadMap.get("codigo_localidad_"+i)+"").trim().equals(""))
					{
						errores.add("codigo_localidad", new ActionMessage("errors.required","El código del registro "+(i+1)));
					}
					else
					{
						for(int j=0;j<i;j++)
						{
							if(((this.localidadMap.get("codigo_localidad_"+i)+"").equalsIgnoreCase(this.localidadMap.get("codigo_localidad_"+j)+""))&&((this.localidadMap.get("codigo_ciudad_"+i)+"").equalsIgnoreCase(this.localidadMap.get("codigo_ciudad_"+j)+"")))
							{
								existe=true;
							}
						}
						if(existe)
							errores.add("", new ActionMessage("errors.yaExiste","El código "+this.localidadMap.get("codigo_localidad_"+i)+" para el codigo de ciudad "+this.localidadMap.get("codigo_ciudad_"+i)));
					}
					if((this.departamentoMap.get("descripcion_"+i)+"").trim().equals(""))
					{
						errores.add("descripcion", new ActionMessage("errors.required","La descripción del registro "+(i+1)));
					}
				}
			}
			else if(this.estado.equals("guardarBarrio"))
			{
				int numRegBarrio=Integer.parseInt(this.barrioMap.get("numRegistros")+"");
				existe=false;
				for(int i=0;i<numRegBarrio;i++)
				{
					if((this.barrioMap.get("codigo_barrio_"+i)+"").trim().equals(""))
					{
						errores.add("codigo_barrio", new ActionMessage("errors.required","El código del registro "+(i+1)));
					}
					else
					{
						for(int j=0;j<i;j++)
						{
							if(((this.barrioMap.get("codigo_barrio_"+i)+"").equalsIgnoreCase(this.barrioMap.get("codigo_barrio_"+j)+""))&&((this.barrioMap.get("codigo_ciudad_"+i)+"").equalsIgnoreCase(this.barrioMap.get("codigo_ciudad_"+j)+"")))
							{
								existe=true;
							}
						}
						if(existe)
							errores.add("", new ActionMessage("errors.yaExiste","El código "+this.barrioMap.get("codigo_barrio_"+i)+" para el codigo de ciudad "+this.barrioMap.get("codigo_ciudad_"+i)));
					}
					if((this.barrioMap.get("descripcion_"+i)+"").trim().equals(""))
					{
						errores.add("descripcion", new ActionMessage("errors.required","La descripción del registro "+(i+1)));
					}
				}
			}
			return errores;
		}

		public HashMap getBarrioEliminadoMap() {
			return barrioEliminadoMap;
		}
		
		public Object getBarrioEliminadoMap(String key) {
			return barrioEliminadoMap.get(key);
		}
		
		public void setBarrioEliminadoMap(String key, Object Value) {
			this.barrioEliminadoMap.put(key,Value);
		}

		public void setBarrioEliminadoMap(HashMap barrioEliminadoMap) {
			this.barrioEliminadoMap = barrioEliminadoMap;
		}

		public HashMap getBarrioMap() {
			return barrioMap;
		}
		
		public Object getBarrioMap(String key) {
			return barrioMap.get(key);
		}
		
		public void setBarrioMap(String key, Object Value) {
			this.barrioMap.put(key,Value);
		}

		public void setBarrioMap(HashMap barrioMap) {
			this.barrioMap = barrioMap;
		}

		public HashMap getCiudadEliminadoMap() {
			return ciudadEliminadoMap;
		}
		
		public Object getCiudadEliminadoMap(String key) {
			return ciudadEliminadoMap.get(key);
		}
		
		public void setCiudadEliminadoMap(String key, Object Value) {
			this.ciudadEliminadoMap.put(key,Value);
		}

		public void setCiudadEliminadoMap(HashMap ciudadEliminadoMap) {
			this.ciudadEliminadoMap = ciudadEliminadoMap;
		}

		public HashMap getCiudadMap() {
			return ciudadMap;
		}
		
		public Object getCiudadMap(String key) {
			return ciudadMap.get(key);
		}
		
		public void setCiudadMap(String key, Object Value) {
			this.ciudadMap.put(key,Value);
		}

		public void setCiudadMap(HashMap ciudadMap) {
			this.ciudadMap = ciudadMap;
		}

		public HashMap getDepartamentoEliminadoMap() {
			return departamentoEliminadoMap;
		}
		
		public Object getDepartamentoEliminadoMap(String key) {
			return departamentoEliminadoMap.get(key);
		}
		
		public void setDepartamentoEliminadoMap(String key, Object Value) {
			this.departamentoEliminadoMap.put(key,Value);
		}

		public void setDepartamentoEliminadoMap(HashMap departamentoEliminadoMap) {
			this.departamentoEliminadoMap = departamentoEliminadoMap;
		}

		public HashMap getDepartamentoMap() {
			return departamentoMap;
		}
		
		public Object getDepartamentoMap(String key) {
			return departamentoMap.get(key);
		}
		
		public void setDepartamentoMap(String key, Object Value) {
			this.departamentoMap.put(key,Value);
		}

		public void setDepartamentoMap(HashMap departamentoMap) {
			this.departamentoMap = departamentoMap;
		}

		public String getEstado() {
			return estado;
		}

		public void setEstado(String estado) {
			this.estado = estado;
		}

		public String getLinkSiguiente() {
			return linkSiguiente;
		}

		public void setLinkSiguiente(String linkSiguiente) {
			this.linkSiguiente = linkSiguiente;
		}

		public HashMap getLocalidadEliminadoMap() {
			return localidadEliminadoMap;
		}
		
		public Object getLocalidadEliminadoMap(String key) {
			return localidadEliminadoMap.get(key);
		}
		
		public void setLocalidadEliminadoMap(String key, Object Value) {
			this.localidadEliminadoMap.put(key,Value);
		}

		public void setLocalidadEliminadoMap(HashMap localidadEliminadoMap) {
			this.localidadEliminadoMap = localidadEliminadoMap;
		}

		public HashMap getLocalidadMap() {
			return localidadMap;
		}
		
		public Object getLocalidadMap(String key) {
			return localidadMap.get(key);
		}
		
		public void setLocalidadMap(String key, Object Value) {
			this.localidadMap.put(key,Value);
		}

		public void setLocalidadMap(HashMap localidadMap) {
			this.localidadMap = localidadMap;
		}

		public int getMaxPageItems() {
			return maxPageItems;
		}

		public void setMaxPageItems(int maxPageItems) {
			this.maxPageItems = maxPageItems;
		}

		public ResultadoBoolean getMensaje() {
			return mensaje;
		}

		public void setMensaje(ResultadoBoolean mensaje) {
			this.mensaje = mensaje;
		}

		public int getOffset() {
			return offset;
		}

		public void setOffset(int offset) {
			this.offset = offset;
		}

		public HashMap getPaisEliminadoMap() {
			return paisEliminadoMap;
		}
		
		public Object getPaisEliminadoMap(String key) {
			return paisEliminadoMap.get(key);
		}
		
		public void setPaisEliminadoMap(String key, Object Value) {
			this.paisEliminadoMap.put(key,Value);
		}

		public void setPaisEliminadoMap(HashMap paisEliminadoMap) {
			this.paisEliminadoMap = paisEliminadoMap;
		}

		public HashMap getPaisMap() {
			return paisMap;
		}
		
		public Object getPaisMap(String key) {
			return paisMap.get(key);
		}
		
		public void setPaisMap(String key, Object Value) {
			this.paisMap.put(key,Value);
		}

		public void setPaisMap(HashMap paisMap) {
			this.paisMap = paisMap;
		}

		public String getPatronOrdenar() {
			return patronOrdenar;
		}

		public void setPatronOrdenar(String patronOrdenar) {
			this.patronOrdenar = patronOrdenar;
		}

		public int getPosEliminar() {
			return posEliminar;
		}

		public void setPosEliminar(int posEliminar) {
			this.posEliminar = posEliminar;
		}

		public String getUltimoPatron() {
			return ultimoPatron;
		}

		public void setUltimoPatron(String ultimoPatron) {
			this.ultimoPatron = ultimoPatron;
		}

		public int getIndexSeleccionado() {
			return indexSeleccionado;
		}

		public void setIndexSeleccionado(int indexSeleccionado) {
			this.indexSeleccionado = indexSeleccionado;
		}

		public int getIndexSeleccionadoBarrio() {
			return indexSeleccionadoBarrio;
		}

		public void setIndexSeleccionadoBarrio(int indexSeleccionadoBarrio) {
			this.indexSeleccionadoBarrio = indexSeleccionadoBarrio;
		}

		public int getIndexSeleccionadoCiudad() {
			return indexSeleccionadoCiudad;
		}

		public void setIndexSeleccionadoCiudad(int indexSeleccionadoCiudad) {
			this.indexSeleccionadoCiudad = indexSeleccionadoCiudad;
		}

		public int getIndexSeleccionadoDepartamento() {
			return indexSeleccionadoDepartamento;
		}

		public void setIndexSeleccionadoDepartamento(int indexSeleccionadoDepartamento) 
		{
			this.indexSeleccionadoDepartamento = indexSeleccionadoDepartamento;
		}

		public int getIndexSeleccionadoLocalidad() {
			return indexSeleccionadoLocalidad;
		}

		public void setIndexSeleccionadoLocalidad(int indexSeleccionadoLocalidad) {
			this.indexSeleccionadoLocalidad = indexSeleccionadoLocalidad;
		}

}
