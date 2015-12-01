/**
 * 
 */
package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.Utilidades;

import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.dto.odontologia.DtoCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoMotivosCambioServicio;
import com.princetonsa.dto.odontologia.DtoProcesoCentralizadoConfirmacionCambioServicios;
import com.princetonsa.dto.odontologia.DtoServicioCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoServiciosOdontologiaCambioServicio;
import com.princetonsa.dto.odontologia.DtoSolictudCambioServicioCita;

/**
 * @author armando
 *
 */
public class SolicitudCambioServicioForm extends ValidatorForm 
{
	
	/**
	 * 
	 */
	private int codigoCitaCambiarServicio;
	
	/**
	 * 
	 */
	private String estado;
	
	/**
	 * 
	 */
	private DtoCitaOdontologica cita;
	
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
	private String observacionesGenerales;
	
	/**
	 * 
	 */
	private ArrayList<DtoMotivosCambioServicio> motivosCambios;
	
	/**
	 * 
	 */
	private String flujo;
	
	/**
	 * 
	 */
	private ArrayList<DtoServiciosOdontologiaCambioServicio> serviciosPaciente;
	
	/**
	 * Listado de los responsables de la cuenta
	 */
	private ArrayList<DtoSubCuentas> responsablesCuenta;
	
	/**
	 * 
	 */
	private boolean resumen;

	/**
	 * 
	 */
	private boolean mostrarTarifas;

	/**
	 * 
	 */
	private double valorTotalServiciosPaciente;
	
	/**
	 * 
	 */
	private double valorTotalServiciosConvenio;

	/**
	 * 
	 */
	private double abonosDisponiblesPaciente;

	/**
	 * 
	 */
	private double valorTotalDevolucionPaciente;
	
	/**
	 * 
	 */
	private ArrayList<DtoServiciosOdontologiaCambioServicio> aCargoDelPaciente;
	
	/**
	 * 
	 */
	private ArrayList<DtoServiciosOdontologiaCambioServicio> aCargoDelConvenio;
	
	/**
	 * 
	 */
	private DtoSolictudCambioServicioCita solicitud;
	
	/**
	 * 
	 */
	private DtoProcesoCentralizadoConfirmacionCambioServicios dtoConfirmacion;
	
	/**
	 * 
	 */
	public void reset()
	{
		
		this.cita=new DtoCitaOdontologica();
		this.mensaje=new ResultadoBoolean(false);
		this.serviciosPaciente=new ArrayList<DtoServiciosOdontologiaCambioServicio>();
		this.aCargoDelPaciente=new ArrayList<DtoServiciosOdontologiaCambioServicio>();
		this.aCargoDelConvenio=new ArrayList<DtoServiciosOdontologiaCambioServicio>();
		this.motivosCambios=new ArrayList<DtoMotivosCambioServicio>();
		this.codigoCitaCambiarServicio=ConstantesBD.codigoNuncaValido;
		this.observacionesGenerales="";
		this.mostrarTarifas=false;
		this.valorTotalDevolucionPaciente=0;
		this.valorTotalServiciosPaciente=0;
		this.valorTotalServiciosConvenio=0;
		this.abonosDisponiblesPaciente=0;
		this.dtoConfirmacion=new DtoProcesoCentralizadoConfirmacionCambioServicios();
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
	public DtoCitaOdontologica getCita() {
		return cita;
	}
	public void setCita(DtoCitaOdontologica cita) {
		this.cita = cita;
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
	public ArrayList<DtoServiciosOdontologiaCambioServicio> getServiciosPaciente() {
		return serviciosPaciente;
	}
	public void setServiciosPaciente(
			ArrayList<DtoServiciosOdontologiaCambioServicio> serviciosPaciente) {
		this.serviciosPaciente = serviciosPaciente;
	}
	
	public ArrayList<DtoMotivosCambioServicio> getMotivosCambios() {
		return motivosCambios;
	}
	
	
	public void setMotivosCambios(ArrayList<DtoMotivosCambioServicio> motivosCambios) {
		this.motivosCambios = motivosCambios;
	}
	public int getCodigoCitaCambiarServicio() {
		return codigoCitaCambiarServicio;
	}
	public void setCodigoCitaCambiarServicio(int codigoCitaCambiarServicio) {
		this.codigoCitaCambiarServicio = codigoCitaCambiarServicio;
	}
	public String getObservacionesGenerales() {
		return observacionesGenerales;
	}
	public void setObservacionesGenerales(String observacionesGenerales) {
		this.observacionesGenerales = observacionesGenerales;
	}
	public ArrayList<DtoSubCuentas> getResponsablesCuenta() {
		return responsablesCuenta;
	}
	public void setResponsablesCuenta(ArrayList<DtoSubCuentas> responsablesCuenta) {
		this.responsablesCuenta = responsablesCuenta;
	}
	public boolean isMostrarTarifas() {
		return mostrarTarifas;
	}
	public void setMostrarTarifas(boolean mostrarTarifas) {
		this.mostrarTarifas = mostrarTarifas;
	}
	
	public double getAbonosDisponiblesPaciente() {
		return abonosDisponiblesPaciente;
	}
	public void setAbonosDisponiblesPaciente(double abonosDisponiblesPaciente) {
		this.abonosDisponiblesPaciente = abonosDisponiblesPaciente;
	}
	public double getValorTotalDevolucionPaciente() {
		return valorTotalDevolucionPaciente;
	}
	public void setValorTotalDevolucionPaciente(double valorTotalDevolucionPaciente) {
		this.valorTotalDevolucionPaciente = valorTotalDevolucionPaciente;
	}
	public ArrayList<DtoServiciosOdontologiaCambioServicio> getaCargoDelPaciente() {
		return aCargoDelPaciente;
	}
	public void setaCargoDelPaciente(
			ArrayList<DtoServiciosOdontologiaCambioServicio> aCargoDelPaciente) {
		this.aCargoDelPaciente = aCargoDelPaciente;
	}
	public ArrayList<DtoServiciosOdontologiaCambioServicio> getaCargoDelConvenio() {
		return aCargoDelConvenio;
	}
	public void setaCargoDelConvenio(
			ArrayList<DtoServiciosOdontologiaCambioServicio> aCargoDelConvenio) {
		this.aCargoDelConvenio = aCargoDelConvenio;
	}
	public double getValorTotalServiciosPaciente() {
		return valorTotalServiciosPaciente;
	}
	public void setValorTotalServiciosPaciente(double valorTotalServiciosPaciente) {
		this.valorTotalServiciosPaciente = valorTotalServiciosPaciente;
	}
	public double getValorTotalServiciosConvenio() {
		return valorTotalServiciosConvenio;
	}
	public void setValorTotalServiciosConvenio(double valorTotalServiciosConvenio) {
		this.valorTotalServiciosConvenio = valorTotalServiciosConvenio;
	}
	public DtoProcesoCentralizadoConfirmacionCambioServicios getDtoConfirmacion() {
		return dtoConfirmacion;
	}
	public void setDtoConfirmacion(
			DtoProcesoCentralizadoConfirmacionCambioServicios dtoConfirmacion) {
		this.dtoConfirmacion = dtoConfirmacion;
	}
	public boolean isResumen() {
		return resumen;
	}
	public void setResumen(boolean resumen) {
		this.resumen = resumen;
	}
	public String getFlujo() {
		return flujo;
	}
	public void setFlujo(String flujo) {
		this.flujo = flujo;
	}
	
	
	
}
