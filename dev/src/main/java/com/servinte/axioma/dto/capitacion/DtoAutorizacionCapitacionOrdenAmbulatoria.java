package com.servinte.axioma.dto.capitacion;

import java.io.Serializable;
import java.util.Date;

import util.ConstantesBD;

import com.princetonsa.dto.ordenes.DtoOrdenesAmbulatorias;

/**
 * Esta clase se encarga de contener los datos para las validaciones de 
 * autorizacion de capitación de Ordenes Ambulatorias 
 * 
 * @author Camilo Gómez
 *
 */
public class DtoAutorizacionCapitacionOrdenAmbulatoria implements Serializable{

	private static final long serialVersionUID = 1L;

	private boolean mostrarBotonImprimir;
	/**Variable que almacena el número de la solictud */
	private int numeroSolicitudAutorizar;
	/**Variable que almacena el codigo del servicio*/
	private int codigoServicioAutorizar;
	/**Variable que almacena el nombre del servicio*/
	private String nombreServicioAutorizar;
	/**Variable que almacena true o false si se hace llamado desde Ordenes Ambulatorias*/
	private boolean ordenAmbulatoria;
	/**Variable que indica si la validacion es desde el proceso Autoriz(1106) o validacion para el Nivel y consecutivos (1115)*/
	private boolean procesoGeneracionAutoriz;
	
	
	/**dto Ordenes Ambulatorias*/
	private DtoOrdenesAmbulatorias dtoOrdenesAmbulatorias;	
	/**Variable que almacena el estado de la autorización si se generó para la orden ambulatoria*/
	private String estadoAutorizacion;
	/**Variable que almacena si se esta anulando la orden*/
	private boolean procesoAnulacionOrden;
	/**Variable que almacena el convenio de la Orden Ambulatoria*/
	private Integer convenio;
	/**Variable que almacena la fecha de la autorización asociada a la Orden Ambulatoria*/
	private Date fechaAutorizacion; 
	/**Variable que almacena el estado de Medicamento/Insumo de la autorización asociada a la Orden Ambulatoria*/
	private String estadoArticulo;
	/**Variable que almacena la institucion*/
	private Integer institucion;
	/**Variable que indica si existe una Autorización de Capitación asociada a una Orden Ambulatoria*/
	private boolean existeAutorizOrden;
	/**Variable que almacena el consecutivo de la autorizacion asociada a la Orden Ambulatoria*/
	private Long consecutivoAutorEntSub;
	/**Variable que almacena el consecutivo de la autorizacion de Entidad Subcontratada*/
	private String consecutivoAutorizacion;
	
	/**Variables almacenan el codigo y nombre del centro de costo de la Autorizacion*/
		private Integer codigoCentrosCostoSolicitadoAutoriz;	
		private String centrosCostoSolicitadoAutoriz;
	/**Variables que almacenan el codigo y nombre del centro de Atencion de la Autorizacion*/
		private Integer codigoCentroAtencionAutoriz;
		private String centroAtencionAutoriz;	
	/**Variable que almacena el centro de Atencion del Paciente*/
		private Integer codigoCentrosCostoSolicitadoPaciente;
		private Integer codigoCentroAtencionPaciente;		
	
	
	/**Variable que almacena true o false si el centro de atencion de la autorizacion corresponde con el del paciente*/
	private boolean centroAtencionCorresponde;
	
	private String tipoEntidadCentroCostoSolicitadoAutoriz;
	/**Variable que indica el tipo de solicitud para actualizar tabla por asocio con autorizacion*/
	private int tipoSolicitud;
	/**Variable que indica el codigo del servicio asociado a la autorizacion*/
	private Integer codigoServicio;
	/**Variable que indica el codigo del articulo asociado a la autorizacion*/
	private Integer codigoArticulo;
	/**Variable que indica el nombre del articulo asociado a la autorizacion*/
	private String nombreArticulo;
	
	/**
	 * Método constructor de la clase
	 */
	public DtoAutorizacionCapitacionOrdenAmbulatoria() {
		this.reset();
	}
	
	private void reset()
	{
		this.mostrarBotonImprimir			=false;
		this.numeroSolicitudAutorizar		=ConstantesBD.codigoNuncaValido;
		this.codigoServicioAutorizar		=ConstantesBD.codigoNuncaValido;
		this.nombreServicioAutorizar		="";
		this.ordenAmbulatoria				=false;
		this.procesoGeneracionAutoriz		=false;
		this.setDtoOrdenesAmbulatorias(new DtoOrdenesAmbulatorias());
		this.estadoAutorizacion				="";
		this.fechaAutorizacion				=new Date();
		this.convenio						=new Integer(0);
		this.estadoArticulo					="";
		this.procesoAnulacionOrden			=false;
		this.existeAutorizOrden				=false;
		this.consecutivoAutorEntSub			=ConstantesBD.codigoNuncaValidoLong;
		this.consecutivoAutorizacion		="";
		this.codigoCentrosCostoSolicitadoAutoriz=ConstantesBD.codigoNuncaValido;
		this.centrosCostoSolicitadoAutoriz	="";
	
		this.codigoCentroAtencionAutoriz=ConstantesBD.codigoNuncaValido;
		this.setCentroAtencionAutoriz("");	
	
		this.codigoCentroAtencionPaciente=ConstantesBD.codigoNuncaValido;
		this.codigoCentrosCostoSolicitadoPaciente=ConstantesBD.codigoNuncaValido;
		
		this.setCentroAtencionCorresponde(false);
		this.tipoEntidadCentroCostoSolicitadoAutoriz="";
		
		this.tipoSolicitud					=ConstantesBD.codigoNuncaValido;
		this.codigoServicio					=ConstantesBD.codigoNuncaValido;
		this.codigoArticulo					=ConstantesBD.codigoNuncaValido;
		this.nombreArticulo					="";
	}

	public void setMostrarBotonImprimir(boolean mostrarBotonImprimir) {
		this.mostrarBotonImprimir = mostrarBotonImprimir;
	}

	public boolean isMostrarBotonImprimir() {
		return mostrarBotonImprimir;
	}
	
	public void setCodigoServicioAutorizar(int codigoServicioAutorizar) {
		this.codigoServicioAutorizar = codigoServicioAutorizar;
	}

	public int getCodigoServicioAutorizar() {
		return codigoServicioAutorizar;
	}

	public void setNumeroSolicitudAutorizar(int numeroSolicitudAutorizar) {
		this.numeroSolicitudAutorizar = numeroSolicitudAutorizar;
	}

	public int getNumeroSolicitudAutorizar() {
		return numeroSolicitudAutorizar;
	}

	public void setNombreServicioAutorizar(String nombreServicioAutorizar) {
		this.nombreServicioAutorizar = nombreServicioAutorizar;
	}

	public String getNombreServicioAutorizar() {
		return nombreServicioAutorizar;
	}

	public void setOrdenAmbulatoria(boolean ordenAmbulatoria) {
		this.ordenAmbulatoria = ordenAmbulatoria;
	}

	public boolean isOrdenAmbulatoria() {
		return ordenAmbulatoria;
	}

	public void setProcesoGeneracionAutoriz(boolean procesoGeneracionAutoriz) {
		this.procesoGeneracionAutoriz = procesoGeneracionAutoriz;
	}

	public boolean isProcesoGeneracionAutoriz() {
		return procesoGeneracionAutoriz;
	}

	public void setDtoOrdenesAmbulatorias(DtoOrdenesAmbulatorias dtoOrdenesAmbulatorias) {
		this.dtoOrdenesAmbulatorias = dtoOrdenesAmbulatorias;
	}

	public DtoOrdenesAmbulatorias getDtoOrdenesAmbulatorias() {
		return dtoOrdenesAmbulatorias;
	}

	public void setEstadoAutorizacion(String estadoAutorizacion) {
		this.estadoAutorizacion = estadoAutorizacion;
	}

	public String getEstadoAutorizacion() {
		return estadoAutorizacion;
	}

	public void setFechaAutorizacion(Date fechaAutorizacion) {
		this.fechaAutorizacion = fechaAutorizacion;
	}

	public Date getFechaAutorizacion() {
		return fechaAutorizacion;
	}

	public void setConvenio(Integer convenio) {
		this.convenio = convenio;
	}

	public Integer getConvenio() {
		return convenio;
	}

	public void setEstadoArticulo(String estadoArticulo) {
		this.estadoArticulo = estadoArticulo;
	}

	public String getEstadoArticulo() {
		return estadoArticulo;
	}

	public void setProcesoAnulacionOrden(boolean procesoAnulacionOrden) {
		this.procesoAnulacionOrden = procesoAnulacionOrden;
	}

	public boolean isProcesoAnulacionOrden() {
		return procesoAnulacionOrden;
	}

	public void setInstitucion(Integer institucion) {
		this.institucion = institucion;
	}

	public Integer getInstitucion() {
		return institucion;
	}

	public void setExisteAutorizOrden(boolean existeAutorizOrden) {
		this.existeAutorizOrden = existeAutorizOrden;
	}

	public boolean isExisteAutorizOrden() {
		return existeAutorizOrden;
	}

	public void setConsecutivoAutorEntSub(Long consecutivoAutorEntSub) {
		this.consecutivoAutorEntSub = consecutivoAutorEntSub;
	}

	public Long getConsecutivoAutorEntSub() {
		return consecutivoAutorEntSub;
	}

	public void setCodigoCentroAtencionAutoriz(
			Integer codigoCentroAtencionAutoriz) {
		this.codigoCentroAtencionAutoriz = codigoCentroAtencionAutoriz;
	}

	public Integer getCodigoCentroAtencionAutoriz() {
		return codigoCentroAtencionAutoriz;
	}

	public void setCodigoCentroAtencionPaciente(
			Integer codigoCentroAtencionPaciente) {
		this.codigoCentroAtencionPaciente = codigoCentroAtencionPaciente;
	}

	public Integer getCodigoCentroAtencionPaciente() {
		return codigoCentroAtencionPaciente;
	}

	public void setCentroAtencionAutoriz(String centroAtencionAutoriz) {
		this.centroAtencionAutoriz = centroAtencionAutoriz;
	}

	public String getCentroAtencionAutoriz() {
		return centroAtencionAutoriz;
	}

	public void setCentroAtencionCorresponde(boolean centroAtencionCorresponde) {
		this.centroAtencionCorresponde = centroAtencionCorresponde;
	}

	public boolean isCentroAtencionCorresponde() {
		return centroAtencionCorresponde;
	}

	public void setCodigoCentrosCostoSolicitadoAutoriz(
			Integer codigoCentrosCostoSolicitadoAutoriz) {
		this.codigoCentrosCostoSolicitadoAutoriz = codigoCentrosCostoSolicitadoAutoriz;
	}

	public Integer getCodigoCentrosCostoSolicitadoAutoriz() {
		return codigoCentrosCostoSolicitadoAutoriz;
	}

	public void setCentrosCostoSolicitadoAutoriz(
			String centrosCostoSolicitadoAutoriz) {
		this.centrosCostoSolicitadoAutoriz = centrosCostoSolicitadoAutoriz;
	}

	public String getCentrosCostoSolicitadoAutoriz() {
		return centrosCostoSolicitadoAutoriz;
	}

	public void setCodigoCentrosCostoSolicitadoPaciente(
			Integer codigoCentrosCostoSolicitadoPaciente) {
		this.codigoCentrosCostoSolicitadoPaciente = codigoCentrosCostoSolicitadoPaciente;
	}

	public Integer getCodigoCentrosCostoSolicitadoPaciente() {
		return codigoCentrosCostoSolicitadoPaciente;
	}

	/**
	 * @return the tipoEntidadCentroCostoSolicitadoAutoriz
	 */
	public String getTipoEntidadCentroCostoSolicitadoAutoriz() {
		return tipoEntidadCentroCostoSolicitadoAutoriz;
	}

	/**
	 * @param tipoEntidadCentroCostoSolicitadoAutoriz the tipoEntidadCentroCostoSolicitadoAutoriz to set
	 */
	public void setTipoEntidadCentroCostoSolicitadoAutoriz(
			String tipoEntidadCentroCostoSolicitadoAutoriz) {
		this.tipoEntidadCentroCostoSolicitadoAutoriz = tipoEntidadCentroCostoSolicitadoAutoriz;
	}

	public String getConsecutivoAutorizacion() {
		return consecutivoAutorizacion;
	}

	public void setConsecutivoAutorizacion(String consecutivoAutorizacion) {
		this.consecutivoAutorizacion = consecutivoAutorizacion;
	}

	public int getTipoSolicitud() {
		return tipoSolicitud;
	}

	public void setTipoSolicitud(int tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}

	public Integer getCodigoServicio() {
		return codigoServicio;
	}

	public void setCodigoServicio(Integer codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	public Integer getCodigoArticulo() {
		return codigoArticulo;
	}

	public void setCodigoArticulo(Integer codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	public String getNombreArticulo() {
		return nombreArticulo;
	}

	public void setNombreArticulo(String nombreArticulo) {
		this.nombreArticulo = nombreArticulo;
	}

}