package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import org.apache.struts.action.ActionErrors;

import util.ConstantesBD;

import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.Convenios;


/**
 * Esta clase se encarga de contener los datos necesarios para
 * generar el proceso de autorización
 * @author Angela Maria Aguirre
 * @since 2/12/2010
 * 
 * @author Cristhian Murillo
 * @since 16/06/2011
 */
public class DTOProcesoAutorizacion implements Serializable
{
	
	/** * serialVersionUID */
	private static final long serialVersionUID = 1L;
	
	/** * Atributo para almacenar los datos a autorizar sólo cuando es a través de solicitud */
	private DTOSolicitudAutorizacion dtoSolicitud;
	
	/** * Atributo para almacenar los datos a autorizar sólo cuando es a través de una autorización de ingreso estancia */
	private DTOAutorizacionIngresoEstancia dtoAutorizacionIngresoEstancia;
	
	/** * dtoEntidadSubcontratada */
	private DtoEntidadSubcontratada dtoEntidadSubcontratada;
	
	/** * Observaciones para la autorización */
	private String observaciones;
	
	/** * paciente */
	private PersonaBasica paciente;
	
	/** * usuarioSesion */
	private UsuarioBasico usuarioSesion;
	
	/** * indicadorPrioridad */
	private Integer indicadorPrioridad;
	
	/** * erroresAutorizacion */
	private ActionErrors erroresAutorizacion;
	
	/** * Este atributo se ingresa desde las funcionalidades que son autorizaciones manuales */
	private Date fechaVencimientoAutorizacion;
		
	/** * listaAutorizaciones */
	private ArrayList<DTOAutorEntidadSubcontratadaCapitacion> listaAutorizaciones;
	
	/** * Este atributo indica el tipo de autorización: * Automatica - Manual - Ingreso Estancia  * */
	private String tipoAutorizacion;
	
	/** * convenioRecobro */
	private Convenios convenioRecobro;
	
	/** * descripcionConvenioRecobro */
	private String descripcionConvenioRecobro;
	
	/** * Diferencia una orden Médica de una ambulatoria */
	private boolean ordenAmbulatoria; 
	
	/** * Contrato con el que se va a autorizar */
	private Integer contratoAutorizacion;
	
	/* DCU 1106 - Versión cambio 1.1. Cristhian Murillo */
	private Double valorMonto 		= null;
	private Double porcentajeMonto 	= null;
	private Integer tipoMonto 		= null;
	private Integer viaIngreso 		= null;
	private String tipoDetalleMonto = null;
	
	/*DCU 966 Diana Ruiz*/
	private int centroCostoResponde;
	
	private String tipoAfiliado;
	private int clasificacionSocioEcono;
	
	/** * lista Centros Costo de las validaciones*/
	private ArrayList<DtoCentroCosto> listaCentrosCosto;
	
	/** * Acronimo del Tipo de entidad que se va a autorizar (Interna o Externa) */
	private String tipoEntidadAutoriza = null;
	
	
	/**
	 * Constructor de la clase
	 */
	public DTOProcesoAutorizacion() 
	{
		this.ordenAmbulatoria = false;
		this.centroCostoResponde = ConstantesBD.codigoNuncaValido;
		this.listaCentrosCosto = new ArrayList<DtoCentroCosto>();
	}
	


	/**
	 * @return valor de dtoSolicitud
	 */
	public DTOSolicitudAutorizacion getDtoSolicitud() {
		return dtoSolicitud;
	}


	/**
	 * @param dtoSolicitud el dtoSolicitud para asignar
	 */
	public void setDtoSolicitud(DTOSolicitudAutorizacion dtoSolicitud) {
		this.dtoSolicitud = dtoSolicitud;
	}


	/**
	 * @return valor de dtoAutorizacionIngresoEstancia
	 */
	public DTOAutorizacionIngresoEstancia getDtoAutorizacionIngresoEstancia() {
		return dtoAutorizacionIngresoEstancia;
	}


	/**
	 * @param dtoAutorizacionIngresoEstancia el dtoAutorizacionIngresoEstancia para asignar
	 */
	public void setDtoAutorizacionIngresoEstancia(
			DTOAutorizacionIngresoEstancia dtoAutorizacionIngresoEstancia) {
		this.dtoAutorizacionIngresoEstancia = dtoAutorizacionIngresoEstancia;
	}


	/**
	 * @return valor de dtoEntidadSubcontratada
	 */
	public DtoEntidadSubcontratada getDtoEntidadSubcontratada() {
		return dtoEntidadSubcontratada;
	}


	/**
	 * @param dtoEntidadSubcontratada el dtoEntidadSubcontratada para asignar
	 */
	public void setDtoEntidadSubcontratada(
			DtoEntidadSubcontratada dtoEntidadSubcontratada) {
		this.dtoEntidadSubcontratada = dtoEntidadSubcontratada;
	}


	/**
	 * @return valor de observaciones
	 */
	public String getObservaciones() {
		return observaciones;
	}


	/**
	 * @param observaciones el observaciones para asignar
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}


	/**
	 * @return valor de paciente
	 */
	public PersonaBasica getPaciente() {
		return paciente;
	}


	/**
	 * @param paciente el paciente para asignar
	 */
	public void setPaciente(PersonaBasica paciente) {
		this.paciente = paciente;
	}


	/**
	 * @return valor de usuarioSesion
	 */
	public UsuarioBasico getUsuarioSesion() {
		return usuarioSesion;
	}


	/**
	 * @param usuarioSesion el usuarioSesion para asignar
	 */
	public void setUsuarioSesion(UsuarioBasico usuarioSesion) {
		this.usuarioSesion = usuarioSesion;
	}


	/**
	 * @return valor de indicadorPrioridad
	 */
	public Integer getIndicadorPrioridad() {
		return indicadorPrioridad;
	}


	/**
	 * @param indicadorPrioridad el indicadorPrioridad para asignar
	 */
	public void setIndicadorPrioridad(Integer indicadorPrioridad) {
		this.indicadorPrioridad = indicadorPrioridad;
	}


	/**
	 * @return valor de erroresAutorizacion
	 */
	public ActionErrors getErroresAutorizacion() {
		return erroresAutorizacion;
	}


	/**
	 * @param erroresAutorizacion el erroresAutorizacion para asignar
	 */
	public void setErroresAutorizacion(ActionErrors erroresAutorizacion) {
		this.erroresAutorizacion = erroresAutorizacion;
	}


	/**
	 * @return valor de fechaVencimientoAutorizacion
	 */
	public Date getFechaVencimientoAutorizacion() {
		return fechaVencimientoAutorizacion;
	}


	/**
	 * @param fechaVencimientoAutorizacion el fechaVencimientoAutorizacion para asignar
	 */
	public void setFechaVencimientoAutorizacion(Date fechaVencimientoAutorizacion) {
		this.fechaVencimientoAutorizacion = fechaVencimientoAutorizacion;
	}


	/**
	 * @return valor de listaAutorizaciones
	 */
	public ArrayList<DTOAutorEntidadSubcontratadaCapitacion> getListaAutorizaciones() {
		return listaAutorizaciones;
	}


	/**
	 * @param listaAutorizaciones el listaAutorizaciones para asignar
	 */
	public void setListaAutorizaciones(
			ArrayList<DTOAutorEntidadSubcontratadaCapitacion> listaAutorizaciones) {
		this.listaAutorizaciones = listaAutorizaciones;
	}


	/**
	 * @return valor de tipoAutorizacion
	 */
	public String getTipoAutorizacion() {
		return tipoAutorizacion;
	}


	/**
	 * @param tipoAutorizacion el tipoAutorizacion para asignar
	 */
	public void setTipoAutorizacion(String tipoAutorizacion) {
		this.tipoAutorizacion = tipoAutorizacion;
	}


	/**
	 * @return valor de convenioRecobro
	 */
	public Convenios getConvenioRecobro() {
		return convenioRecobro;
	}


	/**
	 * @param convenioRecobro el convenioRecobro para asignar
	 */
	public void setConvenioRecobro(Convenios convenioRecobro) {
		this.convenioRecobro = convenioRecobro;
	}


	/**
	 * @return valor de descripcionConvenioRecobro
	 */
	public String getDescripcionConvenioRecobro() {
		return descripcionConvenioRecobro;
	}


	/**
	 * @param descripcionConvenioRecobro el descripcionConvenioRecobro para asignar
	 */
	public void setDescripcionConvenioRecobro(String descripcionConvenioRecobro) {
		this.descripcionConvenioRecobro = descripcionConvenioRecobro;
	}


	/**
	 * @return valor de ordenAmbulatoria
	 */
	public boolean isOrdenAmbulatoria() {
		return ordenAmbulatoria;
	}


	/**
	 * @param ordenAmbulatoria el ordenAmbulatoria para asignar
	 */
	public void setOrdenAmbulatoria(boolean ordenAmbulatoria) {
		this.ordenAmbulatoria = ordenAmbulatoria;
	}


	/**
	 * @return valor de contratoAutorizacion
	 */
	public Integer getContratoAutorizacion() {
		return contratoAutorizacion;
	}


	/**
	 * @param contratoAutorizacion el contratoAutorizacion para asignar
	 */
	public void setContratoAutorizacion(Integer contratoAutorizacion) {
		this.contratoAutorizacion = contratoAutorizacion;
	}


	/**
	 * @return valor de valorMonto
	 */
	public Double getValorMonto() {
		return valorMonto;
	}


	/**
	 * @param valorMonto el valorMonto para asignar
	 */
	public void setValorMonto(Double valorMonto) {
		this.valorMonto = valorMonto;
	}


	/**
	 * @return valor de porcentajeMonto
	 */
	public Double getPorcentajeMonto() {
		return porcentajeMonto;
	}


	/**
	 * @param porcentajeMonto el porcentajeMonto para asignar
	 */
	public void setPorcentajeMonto(Double porcentajeMonto) {
		this.porcentajeMonto = porcentajeMonto;
	}


	/**
	 * @return valor de tipoMonto
	 */
	public Integer getTipoMonto() {
		return tipoMonto;
	}


	/**
	 * @param tipoMonto el tipoMonto para asignar
	 */
	public void setTipoMonto(Integer tipoMonto) {
		this.tipoMonto = tipoMonto;
	}
	
	/**
	 * @param centro de costo que responde
	 */

	public void setCentroCostoResponde(int centroCostoResponde) {
		this.centroCostoResponde = centroCostoResponde;
	}
	
	/**
	 * @return centro de costo que responde
	 */

	public int getCentroCostoResponde() {
		return centroCostoResponde;
	}


	/**
	 * @return valor de viaIngreso
	 */
	public Integer getViaIngreso() {
		return viaIngreso;
	}


	/**
	 * @param viaIngreso el viaIngreso para asignar
	 */
	public void setViaIngreso(Integer viaIngreso) {
		this.viaIngreso = viaIngreso;
	}


	/**
	 * @return valor de listaCentrosCosto
	 */
	public ArrayList<DtoCentroCosto> getListaCentrosCosto() {
		return listaCentrosCosto;
	}


	/**
	 * @param listaCentrosCosto el listaCentrosCosto para asignar
	 */
	public void setListaCentrosCosto(ArrayList<DtoCentroCosto> listaCentrosCosto) {
		this.listaCentrosCosto = listaCentrosCosto;
	}




	/**
	 * @return valor de tipoEntidadAutoriza
	 */
	public String getTipoEntidadAutoriza() {
		return tipoEntidadAutoriza;
	}




	/**
	 * @param tipoEntidadAutoriza el tipoEntidadAutoriza para asignar
	 */
	public void setTipoEntidadAutoriza(String tipoEntidadAutoriza) {
		this.tipoEntidadAutoriza = tipoEntidadAutoriza;
	}



	/**
	 * @return valor de tipoDetalleMonto
	 */
	public String getTipoDetalleMonto() {
		return tipoDetalleMonto;
	}



	/**
	 * @param tipoDetalleMonto el tipoDetalleMonto para asignar
	 */
	public void setTipoDetalleMonto(String tipoDetalleMonto) {
		this.tipoDetalleMonto = tipoDetalleMonto;
	}



	public void setTipoAfiliado(String tipoAfiliado) {
		this.tipoAfiliado = tipoAfiliado;
	}



	public String getTipoAfiliado() {
		return tipoAfiliado;
	}



	public void setClasificacionSocioEcono(int clasificacionSocioEcono) {
		this.clasificacionSocioEcono = clasificacionSocioEcono;
	}



	public int getClasificacionSocioEcono() {
		return clasificacionSocioEcono;
	}


}
