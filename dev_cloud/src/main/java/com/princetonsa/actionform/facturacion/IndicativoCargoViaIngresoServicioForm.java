package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

public class IndicativoCargoViaIngresoServicioForm extends ValidatorForm
{
	/**
	 * 
	 */
	private String estado;

	/**
	 * 
	 */
	private HashMap gruposServicios;
	
	/**
	 * 
	 */
	private HashMap viasIngreso;

	/**
	 * 
	 */
	private String grupoServicio;
	
	/**
	 * 
	 */
	private String viaIngreso;

	/**
	 * 
	 */
	private HashMap procedimientos;
	
	/**
	 * 
	 */
	private String nombreGrupo;
	
	/**
	 * 
	 */
	private String tipoPaciente;
	
	/**
	 * 
	 */
	private ArrayList<HashMap <String, Object>> tipoPacienteMap; 
	



	/**
	 * 
	 *
	 */
	public void reset()
	{
		this.gruposServicios=new HashMap();
		this.gruposServicios.put("numRegistros", "0");
		this.grupoServicio="";
		this.procedimientos=new HashMap();
		this.procedimientos.put("numRegistros", "0");
		this.viasIngreso=new HashMap();
		this.viasIngreso.put("numRegistros", "0");
		this.viaIngreso="";
		this.nombreGrupo="";
		this.tipoPaciente="";
		this.tipoPacienteMap=new ArrayList<HashMap<String,Object>>();
		
	}

	
	public void resetGrupoServicio()	{
		this.gruposServicios=new HashMap();
		this.gruposServicios.put("numRegistros", "0");
		//this.grupoServicio="";
	}	
	
	
	
	/**
	 *Se tiene en cuenta para futuras validaciones 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		return errores;
	}

	
	
	/**
	 * @return the tipoPacienteMap
	 */
	public ArrayList<HashMap<String, Object>> getTipoPacienteMap() {
		return tipoPacienteMap;
	}

	/**
	 * @param tipoPacienteMap the tipoPacienteMap to set
	 */
	public void setTipoPacienteMap(
			ArrayList<HashMap<String, Object>> tipoPacienteMap) {
		this.tipoPacienteMap = tipoPacienteMap;
	}
	
	/**
	 * Tamaño del arreglo tipo paciente map
	 * @return
	 */
	public int getNumTipoPacienteMap()
	{
		return this.tipoPacienteMap.size();
	}

	
	public String getEstado()
	{
		return estado;
	}

	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	public HashMap getGruposServicios()
	{
		return gruposServicios;
	}

	public void setGruposServicios(HashMap gruposServicios)
	{
		this.gruposServicios = gruposServicios;
	}
	
	public Object getGruposServicios(String key)
	{
		return gruposServicios.get(key);
	}

	public void setGruposServicios(String key,Object value)
	{
		this.gruposServicios.put(key, value);
	}

	public String getGrupoServicio()
	{
		return grupoServicio;
	}

	public void setGrupoServicio(String grupoServicio)
	{
		this.grupoServicio = grupoServicio;
	}

	public HashMap getProcedimientos()
	{
		return procedimientos;
	}

	public void setProcedimientos(HashMap procedimientos)
	{
		this.procedimientos = procedimientos;
	}

	public Object getProcedimientos(String key)
	{
		return procedimientos.get(key);
	}

	public void setProcedimientos(String key,Object value)
	{
		this.procedimientos.put(key, value);
	}

	public String getViaIngreso()
	{
		return viaIngreso;
	}

	public void setViaIngreso(String viaIngreso)
	{
		this.viaIngreso = viaIngreso;
	}

	public HashMap getViasIngreso()
	{
		return viasIngreso;
	}

	public void setViasIngreso(HashMap viasIngreso)
	{
		this.viasIngreso = viasIngreso;
	}
	public Object getViasIngreso(String key)
	{
		return viasIngreso.get(key);
	}

	public void setViasIngreso(String key,Object value)
	{
		this.viasIngreso.put(key, value);
	}

	public String getNombreGrupo()
	{
		return nombreGrupo;
	}

	public void setNombreGrupo(String nombreGrupo)
	{
		this.nombreGrupo = nombreGrupo;
	}

	/**
	 * @return the tipoPaciente
	 */
	public String getTipoPaciente() {
		return tipoPaciente;
	}

	/**
	 * @param tipoPaciente the tipoPaciente to set
	 */
	public void setTipoPaciente(String tipoPaciente) {
		this.tipoPaciente = tipoPaciente;
	}
}
