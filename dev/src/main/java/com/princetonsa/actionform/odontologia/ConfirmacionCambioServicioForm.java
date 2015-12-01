/**
 * 
 */
package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;

import com.princetonsa.dto.odontologia.DtoCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoMotivosCambioServicio;
import com.princetonsa.dto.odontologia.DtoProcesoCentralizadoConfirmacionCambioServicios;
import com.princetonsa.dto.odontologia.DtoServiciosOdontologiaCambioServicio;
import com.princetonsa.dto.odontologia.DtoSolictudCambioServicioCita;

/**
 * @author armando
 *
 */
public class ConfirmacionCambioServicioForm extends ValidatorForm 
{

	/**
	 * 
	 */
	private String estado;
	
	
	/**
	 * 
	 */
	private ResultadoBoolean mensaje;
	
	/**
	 * 
	 */
	private int codigoInstitucion;
	
	/**
	 * 
	 */
	private DtoProcesoCentralizadoConfirmacionCambioServicios dtoConfirmacion;
	
	/**
	 * 
	 */
	private DtoSolictudCambioServicioCita dtoSolicitud;
	
	/**
	 * 
	 */
	private int codigoSolitud;
	
	/**
	 * 
	 */
	private boolean anulacion;
	
	/**
	 * 
	 */
	private int motivoAnulacion;
	
	/**
	 * 
	 */
	private boolean resumen;
	
	/**
	 * 
	 */
	public void reset()
	{
		this.mensaje=new ResultadoBoolean(false);
		this.dtoConfirmacion=new DtoProcesoCentralizadoConfirmacionCambioServicios();
		this.dtoSolicitud=new DtoSolictudCambioServicioCita();
		this.anulacion=false;
		this.motivoAnulacion=ConstantesBD.codigoNuncaValido;
		this.resumen=false;
	}
	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping,HttpServletRequest request) 
	{
      ActionErrors errores = new ActionErrors();
      return errores;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}

	public ResultadoBoolean getMensaje() {
		return mensaje;
	}
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}
	public DtoProcesoCentralizadoConfirmacionCambioServicios getDtoConfirmacion() {
		return dtoConfirmacion;
	}
	public void setDtoConfirmacion(
			DtoProcesoCentralizadoConfirmacionCambioServicios dtoConfirmacion) {
		this.dtoConfirmacion = dtoConfirmacion;
	}
	public DtoSolictudCambioServicioCita getDtoSolicitud() {
		return dtoSolicitud;
	}
	public void setDtoSolicitud(DtoSolictudCambioServicioCita dtoSolicitud) {
		this.dtoSolicitud = dtoSolicitud;
	}
	public int getCodigoSolitud() {
		return codigoSolitud;
	}
	public void setCodigoSolitud(int codigoSolitud) {
		this.codigoSolitud = codigoSolitud;
	}
	public boolean isAnulacion() {
		return anulacion;
	}
	public void setAnulacion(boolean anulacion) {
		this.anulacion = anulacion;
	}
	public int getMotivoAnulacion() {
		return motivoAnulacion;
	}
	public void setMotivoAnulacion(int motivoAnulacion) {
		this.motivoAnulacion = motivoAnulacion;
	}
	public boolean isResumen() {
		return resumen;
	}
	public void setResumen(boolean resumen) {
		this.resumen = resumen;
	}
}
