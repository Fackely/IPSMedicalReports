package com.princetonsa.dto.epicrisis;

import java.io.Serializable;
import java.util.HashMap;

import util.ConstantesBD;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * 
 * @author wilson
 *
 */
public class DtoServiciosCirugiaEpicrisis implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private int codigo;
	
	/**
	 * 
	 */
	private int codigoServicio;
	
	/**
	 * 
	 */
	private String servicio;
	
	/**
	 * 
	 */
	private String especialidadInterviene;
	
	/**
	 * 
	 */
	private String descripcionQx;
	
	/**
	 * 
	 */
	private HashMap<Object, Object> diagnosticosPostoperatorios;

	/**
	 * 
	 */
	private String justificacionNoPos;
	
	/**
	 * 
	 *
	 */
	public DtoServiciosCirugiaEpicrisis() 
	{
		this.codigo=ConstantesBD.codigoNuncaValido;
		this.codigoServicio=ConstantesBD.codigoNuncaValido;
		this.servicio="";
		this.especialidadInterviene="";
		this.descripcionQx="";
		this.diagnosticosPostoperatorios= new HashMap<Object, Object>();
		this.diagnosticosPostoperatorios.put("numRegistros", "0");
		this.justificacionNoPos="";
	}

	/**
	 * @return the descripcionQx
	 */
	public String getDescripcionQx() {
		return descripcionQx;
	}

	/**
	 * @param descripcionQx the descripcionQx to set
	 */
	public void setDescripcionQx(String descripcionQx) {
		this.descripcionQx = descripcionQx;
	}

	/**
	 * @return the diagnosticosPostoperatorios
	 */
	public HashMap<Object, Object> getDiagnosticosPostoperatorios() {
		return diagnosticosPostoperatorios;
	}

	/**
	 * @param diagnosticosPostoperatorios the diagnosticosPostoperatorios to set
	 */
	public void setDiagnosticosPostoperatorios(
			HashMap<Object, Object> diagnosticosPostoperatorios) {
		this.diagnosticosPostoperatorios = diagnosticosPostoperatorios;
	}

	/**
	 * @return the especialidadInterviene
	 */
	public String getEspecialidadInterviene() {
		return especialidadInterviene;
	}

	/**
	 * @param especialidadInterviene the especialidadInterviene to set
	 */
	public void setEspecialidadInterviene(String especialidadInterviene) {
		this.especialidadInterviene = especialidadInterviene;
	}

	/**
	 * @return the servicio
	 */
	public String getServicio() {
		return servicio;
	}

	/**
	 * @param servicio the servicio to set
	 */
	public void setServicio(String servicio) {
		this.servicio = servicio;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getNumRegDxPost()
	{
		if(!UtilidadTexto.isEmpty(this.getDiagnosticosPostoperatorios().get("numRegistros")+""))
			return Utilidades.convertirAEntero(this.getDiagnosticosPostoperatorios().get("numRegistros")+"");
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDxPrincipalPostopertorio()
	{
		String dx="";
		for(int w=0; w<getNumRegDxPost(); w++)
		{
			if(UtilidadTexto.getBoolean(this.getDiagnosticosPostoperatorios().get("principal_"+w)+""))
			{	
				dx=this.getDiagnosticosPostoperatorios().get("acronimo_"+w)+" "+this.getDiagnosticosPostoperatorios().get("nombrediagnostico_"+w);
			}
		}
		return dx;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDxRelacionadosPostoperatorio()
	{
		String dx="";
		for(int w=0; w<this.getNumRegDxPost(); w++)
		{
			if(!UtilidadTexto.getBoolean(this.getDiagnosticosPostoperatorios().get("principal_"+w)+"")
				|| !UtilidadTexto.getBoolean(this.getDiagnosticosPostoperatorios().get("complicacion_"+w)+""))
			{
				dx+=this.getDiagnosticosPostoperatorios().get("acronimo_"+w)+" "+this.getDiagnosticosPostoperatorios().get("nombrediagnostico_"+w)+", ";
			}
		}
		return dx;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDxComplicacionPostoperatorio()
	{
		String dx="";
		for(int w=0; w<this.getNumRegDxPost(); w++)
		{
			if(!UtilidadTexto.getBoolean(this.getDiagnosticosPostoperatorios().get("principal_"+w)+"")
				|| UtilidadTexto.getBoolean(this.getDiagnosticosPostoperatorios().get("complicacion_"+w)+""))
			{
				dx+=this.getDiagnosticosPostoperatorios().get("acronimo_"+w)+" "+this.getDiagnosticosPostoperatorios().get("nombrediagnostico_"+w)+", ";
			}
		}
		return dx;
	}

	/**
	 * @return the codigoServicio
	 */
	public int getCodigoServicio() {
		return codigoServicio;
	}

	/**
	 * @param codigoServicio the codigoServicio to set
	 */
	public void setCodigoServicio(int codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the justificacionNoPos
	 */
	public String getJustificacionNoPos() {
		return justificacionNoPos;
	}

	/**
	 * @param justificacionNoPos the justificacionNoPos to set
	 */
	public void setJustificacionNoPos(String justificacionNoPos) {
		this.justificacionNoPos = justificacionNoPos;
	}
	
	
}
