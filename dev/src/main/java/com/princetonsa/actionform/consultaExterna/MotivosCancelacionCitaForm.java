package com.princetonsa.actionform.consultaExterna;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;

public class MotivosCancelacionCitaForm extends ValidatorForm 
{

	/**
	 * 
	 */
	private String estado;
	
	/**
	 * 
	 */
	private int indiceMotivoEliminar;
	
	/**
	 * 
	 */
	 private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	
	/**
	 * 
	 */
	private HashMap mapaMotivos;
	
	/**
	 * 
	 */
	private HashMap mapaMotivosEliminados;
	
	
	/**
	 * 
	 *
	 */
	public void reset()
	{
		this.estado="";
		this.indiceMotivoEliminar=ConstantesBD.codigoNuncaValido;
		this.mapaMotivos= new HashMap();
		this.mapaMotivos.put("numRegistros", "0");
		this.mapaMotivosEliminados= new HashMap();
		this.mapaMotivosEliminados.put("numRegistros", "0");
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
			int numReg=Integer.parseInt(this.mapaMotivos.get("numRegistros")+"");
			for(int i=0;i<numReg;i++)
			{
				if((this.mapaMotivos.get("descripcion_"+i)+"").trim().equals(""))
				{
					errores.add("codigo paquete", new ActionMessage("errors.required","La Descripción del registro "+(i+1)));
				}
				if((this.mapaMotivos.get("tipocancelacion_"+i)+"").trim().equals(""))
				{
					errores.add("codigo paquete", new ActionMessage("errors.required","El Tipo de Cancelacion del registro "+(i+1)));
				}
			}
		}
		return errores;
	}
	
	
	
	

	/**
	 * 
	 * @return
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * 
	 * @param estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaMotivos() {
		return mapaMotivos;
	}

	/**
	 * 
	 * @param mapaMotivos
	 */
	public void setMapaMotivos(HashMap mapaMotivos) {
		this.mapaMotivos = mapaMotivos;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaMotivos(String key) {
		return mapaMotivos.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaMotivos(String key,Object value) {
		this.mapaMotivos.put(key, value);
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaMotivosEliminados() {
		return mapaMotivosEliminados;
	}

	/**
	 * 
	 * @param mapaMotivosEliminados
	 */
	public void setMapaMotivosEliminados(HashMap mapaMotivosEliminados) {
		this.mapaMotivosEliminados = mapaMotivosEliminados;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getMapaMotivosEliminados(String key) {
		return mapaMotivosEliminados.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setMapaMotivosEliminados(String key,Object value) {
		this.mapaMotivosEliminados.put(key, value);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getIndiceMotivoEliminar() {
		return indiceMotivoEliminar;
	}

	/**
	 * 
	 * @param indiceMotivoEliminar
	 */
	public void setIndiceMotivoEliminar(int indiceMotivoEliminar) {
		this.indiceMotivoEliminar = indiceMotivoEliminar;
	}



	/**
	 * 
	 * @return
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}



	/**
	 * 
	 * @param mensaje
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}
	
	
}
