package com.princetonsa.dto.ordenes;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;
import util.facturacion.InfoResponsableCobertura;

import com.servinte.axioma.orm.Convenios;

/**
 * Dto creado para el manejo de Procedimientos
 * @author Jose Eduardo Arias Doncel * 
 * */
public class DtoProcedimiento implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * */
	private int numeroSolicitud;
	
	/**
	 * 
	 * */
	private int codigoCuenta;	
	
	/**
	 * Almance la informacion del codigo de respuesta de procedimiento que 
	 * tiene el registro en egresos de muerto
	 * */
	private int codigoPkResProcMuerto;
	
	/**
	 * 
	 * */
	private int codigoViaIngreso;
	
	/**
	 * 
	 * */
	private String fechaSolicitud;
	
	/**
	 * 
	 * */
	private String horaSolicitud;
	
	/**
	 * 
	 * */
	private int codigoCentroCostoSolicitado;
	
	/**
	 * 
	 * */
	private int codigoServicioSolicitado;
	
	
	/**
	 * 
	 * */
	private String nombreServicioSolicitado;
	
	
	/**
	 * Carga una respuesta de procedimiento especifica o sirve para guardar una nueva
	 * */
	private DtoRespuestaProcedimientos respuestaProceEspecificoDto;
	
	/**
	 * 
	 * */
	private ArrayList<DtoRespuestaProcedimientos> respuestaProceArray;
	
	/**
	 *  numero de respuestas anteriores
	 * */
	private int numeroResSolProcAnteriores;
	
	/**
	 * 
	 * */
	private int codigoTipoSolicitud;
	
	/**
	 * 
	 * */
	private int codigoGrupoServicio;
	
	/**
	 * 
	 * */
	private int codigoConvenio;
	
	/**
	 * 
	 * */
	private String nombreConvenio;
	
	/**
	 * 
	 * */
	private String codigoTipoRegimen;
	
	/**
	 * 
	 * */
	private String nombreTipoRegimen;
	
	/**
	 * 
	 * */
	private String indicativoCentroCostoRegRespuestaProc;
	
	/**
	 * 
	 */
	private String interpretacion;
	
	
	/**
	 * 
	 */
	private boolean mostrarInterpretacionEnResumen;
	
	/**
	 * Cambio Anexo.
	 * Atributo que almacena la cantidad del servicio solicitado
	 * 
	 */
	private Integer cantidadServicioSolicitado;
	
	/**Almacena la info de la cobertura de cada servicio**/
	private InfoResponsableCobertura responsableCoberturaServicio;
	/**Almacena el convenio responsable de cada servicio**/
	private Convenios convenioResponsable;
	
	public DtoProcedimiento()
	{
		this.clean();
	}
	
	public void clean()
	{		
		this.numeroSolicitud = ConstantesBD.codigoNuncaValido;
		this.fechaSolicitud = "";
		this.horaSolicitud = "";
		this.codigoCentroCostoSolicitado = ConstantesBD.codigoNuncaValido;
		this.codigoServicioSolicitado = ConstantesBD.codigoNuncaValido;
		this.respuestaProceEspecificoDto = new DtoRespuestaProcedimientos();
		this.respuestaProceArray = new ArrayList<DtoRespuestaProcedimientos>();
		this.numeroResSolProcAnteriores = 0;
		this.codigoCuenta = ConstantesBD.codigoNuncaValido;
		this.codigoViaIngreso = ConstantesBD.codigoNuncaValido;		
		this.codigoPkResProcMuerto = ConstantesBD.codigoNuncaValido;
		this.codigoTipoSolicitud = ConstantesBD.codigoNuncaValido;
		this.codigoGrupoServicio = ConstantesBD.codigoNuncaValido;
		this.nombreServicioSolicitado = "";
		this.codigoConvenio = ConstantesBD.codigoNuncaValido;
		this.nombreConvenio = "";
		this.codigoTipoRegimen = "";
		this.nombreTipoRegimen = "";
		this.indicativoCentroCostoRegRespuestaProc = ConstantesBD.acronimoNo;
		this.cantidadServicioSolicitado=ConstantesBD.codigoNuncaValido;

		this.interpretacion="";
		this.mostrarInterpretacionEnResumen=false;
		
		this.responsableCoberturaServicio	= new InfoResponsableCobertura();
		this.convenioResponsable	= new Convenios();
	}

	/**
	 * @return the numeroResSolProcAnteriores
	 */
	public int getNumeroResSolProcAnteriores() {
		return numeroResSolProcAnteriores;
	}

	/**
	 * @param numeroResSolProcAnteriores the numeroResSolProcAnteriores to set
	 */
	public void setNumeroResSolProcAnteriores(int numeroResSolProcAnteriores) {
		this.numeroResSolProcAnteriores = numeroResSolProcAnteriores;
	}

	/**
	 * @return the numeroSolicitud
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	/**
	 * @return the codigoCentroCostoSolicitado
	 */
	public int getCodigoCentroCostoSolicitado() {
		return codigoCentroCostoSolicitado;
	}

	/**
	 * @param codigoCentroCostoSolicitado the codigoCentroCostoSolicitado to set
	 */
	public void setCodigoCentroCostoSolicitado(int codigoCentroCostoSolicitado) {
		this.codigoCentroCostoSolicitado = codigoCentroCostoSolicitado;
	}

	/**
	 * @return the codigoServicioSolicitado
	 */
	public int getCodigoServicioSolicitado() {
		return codigoServicioSolicitado;
	}

	/**
	 * @param codigoServicioSolicitado the codigoServicioSolicitado to set
	 */
	public void setCodigoServicioSolicitado(int codigoServicioSolicitado) {
		this.codigoServicioSolicitado = codigoServicioSolicitado;
	}

	/**
	 * @return the respuestaProceArray
	 */
	public ArrayList<DtoRespuestaProcedimientos> getRespuestaProceArray() {
		return respuestaProceArray;
	}

	/**
	 * @param respuestaProceArray the respuestaProceArray to set
	 */
	public void setRespuestaProceArray(
			ArrayList<DtoRespuestaProcedimientos> respuestaProceArray) {
		this.respuestaProceArray = respuestaProceArray;
	}

	/**
	 * @return the fechaSolicitud
	 */
	public String getFechaSolicitud() {
		return fechaSolicitud;
	}

	/**
	 * @param fechaSolicitud the fechaSolicitud to set
	 */
	public void setFechaSolicitud(String fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	/**
	 * @return the horaSolicitud
	 */
	public String getHoraSolicitud() {
		return horaSolicitud;
	}

	/**
	 * @param horaSolicitud the horaSolicitud to set
	 */
	public void setHoraSolicitud(String horaSolicitud) {
		this.horaSolicitud = horaSolicitud;
	}

	/**
	 * @return the codigoCuenta
	 */
	public int getCodigoCuenta() {
		return codigoCuenta;
	}

	/**
	 * @param codigoCuenta the codigoCuenta to set
	 */
	public void setCodigoCuenta(int codigoCuenta) {
		this.codigoCuenta = codigoCuenta;
	}

	/**
	 * @return the codigoViaIngreso
	 */
	public int getCodigoViaIngreso() {
		return codigoViaIngreso;
	}

	/**
	 * @param codigoViaIngreso the codigoViaIngreso to set
	 */
	public void setCodigoViaIngreso(int codigoViaIngreso) {
		this.codigoViaIngreso = codigoViaIngreso;
	}

	
	/**
	 * @return the respuestaProceEspecificoDto
	 */
	public DtoRespuestaProcedimientos getRespuestaProceEspecificoDto() {
		return respuestaProceEspecificoDto;
	}

	/**
	 * @param respuestaProceEspecificoDto the respuestaProceEspecificoDto to set
	 */
	public void setRespuestaProceEspecificoDto(
			DtoRespuestaProcedimientos respuestaProceEspecificoDto) {
		this.respuestaProceEspecificoDto = respuestaProceEspecificoDto;
	}
	
	/**
	 * @return the codigoPkResProcMuerto
	 */
	public int getCodigoPkResProcMuerto() {
		return codigoPkResProcMuerto;
	}

	/**
	 * @param codigoPkResProcMuerto the codigoPkResProcMuerto to set
	 */
	public void setCodigoPkResProcMuerto(int codigoPkResProcMuerto) {
		this.codigoPkResProcMuerto = codigoPkResProcMuerto;
	}

	/**
	 * @return the codigoTipoSolicitud
	 */
	public int getCodigoTipoSolicitud() {
		return codigoTipoSolicitud;
	}

	/**
	 * @param codigoTipoSolicitud the codigoTipoSolicitud to set
	 */
	public void setCodigoTipoSolicitud(int codigoTipoSolicitud) {
		this.codigoTipoSolicitud = codigoTipoSolicitud;
	}
	
	/**
	 * @param codigoGrupoServicio the codigoGrupoServicio to set
	 */
	public void setCodigoGrupoServicio(int codigoGrupoServicio) {
		this.codigoGrupoServicio = codigoGrupoServicio;
	}

	/**
	 * @return the codigoGrupoServicio
	 */
	public int getCodigoGrupoServicio() {
		return codigoGrupoServicio;
	}

	/**
	 * @return the nombreServicioSolicitado
	 */
	public String getNombreServicioSolicitado() {
		return nombreServicioSolicitado;
	}

	/**
	 * @param nombreServicioSolicitado the nombreServicioSolicitado to set
	 */
	public void setNombreServicioSolicitado(String nombreServicioSolicitado) {
		this.nombreServicioSolicitado = nombreServicioSolicitado;
	}

	/**
	 * @return the codigoConvenio
	 */
	public int getCodigoConvenio() {
		return codigoConvenio;
	}

	/**
	 * @param codigoConvenio the codigoConvenio to set
	 */
	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	/**
	 * @return the nombreConvenio
	 */
	public String getNombreConvenio() {
		return nombreConvenio;
	}

	/**
	 * @param nombreConvenio the nombreConvenio to set
	 */
	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
	}

	/**
	 * @return the codigoTipoRegimen
	 */
	public String getCodigoTipoRegimen() {
		return codigoTipoRegimen;
	}

	/**
	 * @param codigoTipoRegimen the codigoTipoRegimen to set
	 */
	public void setCodigoTipoRegimen(String codigoTipoRegimen) {
		this.codigoTipoRegimen = codigoTipoRegimen;
	}

	/**
	 * @return the nombreTipoRegimen
	 */
	public String getNombreTipoRegimen() {
		return nombreTipoRegimen;
	}

	/**
	 * @param nombreTipoRegimen the nombreTipoRegimen to set
	 */
	public void setNombreTipoRegimen(String nombreTipoRegimen) {
		this.nombreTipoRegimen = nombreTipoRegimen;
	}

	public String getIndicativoCentroCostoRegRespuestaProc() {
		return indicativoCentroCostoRegRespuestaProc;
	}

	public void setIndicativoCentroCostoRegRespuestaProc(
			String indicativoCentroCostoRegRespuestaProc) {
		this.indicativoCentroCostoRegRespuestaProc = indicativoCentroCostoRegRespuestaProc;
	}

	public void setCantidadServicioSolicitado(Integer cantidadServicioSolicitado) {
		this.cantidadServicioSolicitado = cantidadServicioSolicitado;
	}

	public Integer getCantidadServicioSolicitado() {
		return cantidadServicioSolicitado;
	}

	public String getInterpretacion() {
		return interpretacion;
	}

	public void setInterpretacion(String interpretacion) {
		this.interpretacion = interpretacion;
	}

	public boolean isMostrarInterpretacionEnResumen() {
		return mostrarInterpretacionEnResumen;
	}

	public void setMostrarInterpretacionEnResumen(
			boolean mostrarInterpretacionEnResumen) {
		this.mostrarInterpretacionEnResumen = mostrarInterpretacionEnResumen;
	}
	
	public InfoResponsableCobertura getResponsableCoberturaServicio() {
		return responsableCoberturaServicio;
	}

	public void setResponsableCoberturaServicio(
			InfoResponsableCobertura responsableCoberturaServicio) {
		this.responsableCoberturaServicio = responsableCoberturaServicio;
	}

	public Convenios getConvenioResponsable() {
		return convenioResponsable;
	}

	public void setConvenioResponsable(Convenios convenioResponsable) {
		this.convenioResponsable = convenioResponsable;
	}
	
}