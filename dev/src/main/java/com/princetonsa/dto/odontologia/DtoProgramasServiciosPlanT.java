package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import util.ConstantesBD;
import util.InfoDatosDouble;
import util.InfoDatosInt;

public class DtoProgramasServiciosPlanT implements Serializable, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private BigDecimal codigoPk;
	private BigDecimal detPlanTratamiento;
	private InfoDatosDouble programa;
	private InfoDatosInt servicio;
	private String estadoPrograma;
	private InfoDatosInt motivo;
	private int convencion;
	private String inclusion;
	private String exclusion;
	private String garantia; 
	private String estadoServicio;
	private String indicativoPrograma;
	private String indicativoServicio;
	private String porConfirmado;
	private InfoDatosInt especialidad;
	private int ordenServicio;
	private String activo; 
	
	//Parametros de Busqueda
	private String buscarProgramas;
	private ArrayList<String> estadosProgramasOservicios;
	private String estadoAutorizacion;
	private int codigoHallazgo;
	private boolean cargarServicios;
	private String codigoTarifario;
	
	private BigDecimal codigoCita;
	private BigDecimal valoracion;
	private BigDecimal evolucion;
	

	private DtoInfoFechaUsuario usuarioModifica ;

	private Integer numeroSuperficies;
	
	private int codigoTipoHallazgo;
	
	/**
	 * MANEJO DE HISTORICOS  DEL DETALLE DEL PLAN DE TRATAMIENTO
	 */
	private BigDecimal logDetPlanTratamiento;
	
	private int codigoPlanTratamiento;
	
	/**
	 * Codigo del paciente
	 */
	private int codigoPaciente;
	
	/**
	 * Número de ingreso del paciente
	 */
	private int ingresoPaciente;
	
	/**
	 * Informacion del Programa Hallazgo Pieza
	 */
	private DtoProgHallazgoPieza programaHallazgoPieza;
	
	
	/**
	 * Constructor
	 */
	public DtoProgramasServiciosPlanT(){
		this.reset();
	}

	/**
	 * @return the ordenServicio
	 */
	public int getOrdenServicio() {
		return ordenServicio;
	}
	/**
	 * @param ordenServicio the ordenServicio to set
	 */
	public void setOrdenServicio(int ordenServicio) {
		this.ordenServicio = ordenServicio;
	}
	/**
	 * @return the activo
	 */
	public String getActivo() {
		return activo;
	}
	/**
	 * @param activo the activo to set
	 */
	public void setActivo(String activo) {
		this.activo = activo;
	}
	
	
	
	/**
	 * 
	 */
	void reset(){
		 this.codigoPk = new BigDecimal(ConstantesBD.codigoNuncaValidoDouble);
		 this.detPlanTratamiento= new BigDecimal(ConstantesBD.codigoNuncaValidoDouble);
		 this.programa = new InfoDatosDouble();
		 this.servicio= new InfoDatosInt();
		 this.estadoPrograma="";
		 this.motivo= new InfoDatosInt();
		 this.convencion=ConstantesBD.codigoNuncaValido;
		 this.inclusion="";
		 this.exclusion="";
		 this.garantia=""; 
		 this.estadoServicio="";
		 this.indicativoPrograma="";
		 this.indicativoServicio="";
		 this.porConfirmado="";
		 this.usuarioModifica = new DtoInfoFechaUsuario();
		 this.especialidad= new InfoDatosInt();
		 this.ordenServicio = ConstantesBD.codigoNuncaValido;
		 this.activo= ConstantesBD.acronimoSi;
		 this.buscarProgramas = "";
		 this.estadosProgramasOservicios = new ArrayList<String>();
		 this.estadoAutorizacion="";
		 this.codigoHallazgo = ConstantesBD.codigoNuncaValido;
		 this.cargarServicios = false;
		 this.codigoTarifario=""; 
		 
		 this.valoracion = new BigDecimal(ConstantesBD.codigoNuncaValido);
		 this.evolucion = new BigDecimal(ConstantesBD.codigoNuncaValido);
		 this.codigoCita  = new BigDecimal(ConstantesBD.codigoNuncaValido);
		 this.logDetPlanTratamiento= BigDecimal.ZERO;
		 this.codigoPlanTratamiento = ConstantesBD.codigoNuncaValido;
		 
		 this.codigoPaciente = ConstantesBD.codigoNuncaValido;
		 
		 this.ingresoPaciente = ConstantesBD.codigoNuncaValido;
		 
		 this.programaHallazgoPieza = new DtoProgHallazgoPieza();
		 
		 this.codigoTipoHallazgo = ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * 
	 * @return
	 */
	public BigDecimal getCodigoPk() {
		return codigoPk;
	}
	public void setCodigoPk(BigDecimal codigoPk) {
		this.codigoPk = codigoPk;
	}
	public BigDecimal getDetPlanTratamiento() {
		return detPlanTratamiento;
	}
	public void setDetPlanTratamiento(BigDecimal detPlanTratamiento) {
		this.detPlanTratamiento = detPlanTratamiento;
	}
	public InfoDatosDouble getPrograma() {
		return programa;
	}
	public void setPrograma(InfoDatosDouble programa) {
		this.programa = programa;
	}
	public InfoDatosInt getServicio() {
		return servicio;
	}
	public void setServicio(InfoDatosInt servicio) {
		this.servicio = servicio;
	}
	public String getEstadoPrograma() {
		return estadoPrograma;
	}
	public void setEstadoPrograma(String estadoPrograma) {
		this.estadoPrograma = estadoPrograma;
	}
	public InfoDatosInt getMotivo() { // cambiar por String OJO
		return motivo;
	}
	public void setMotivo(InfoDatosInt motivo) {
		this.motivo = motivo;
	}
	public int getConvencion() {
		return convencion;
	}
	public void setConvencion(int convencion) {
		this.convencion = convencion;
	}
	
	public String getGarantia() {
		return garantia;
	}
	public void setGarantia(String garantia) {
		this.garantia = garantia;
	}
	public String getEstadoServicio() {
		return estadoServicio;
	}
	public void setEstadoServicio(String estadoServicio) {
		this.estadoServicio = estadoServicio;
	}
	public String getIndicativoPrograma() {
		return indicativoPrograma;
	}
	public void setIndicativoPrograma(String indicativoPrograma) {
		this.indicativoPrograma = indicativoPrograma;
	}
	public String getIndicativoServicio() {
		return indicativoServicio;
	}
	public void setIndicativoServicio(String indicativoServicio) {
		this.indicativoServicio = indicativoServicio;
	}

	public String getInclusion() {
		return inclusion;
	}
	public void setInclusion(String inclusion) {
		this.inclusion = inclusion;
	}
	public String getExclusion() {
		return exclusion;
	}
	public void setExclusion(String exclusion) {
		this.exclusion = exclusion;
	}
	public String getPorConfirmado() {
		return porConfirmado;
	}
	public void setPorConfirmado(String porConfirmado) {
		this.porConfirmado = porConfirmado;
	}
	public DtoInfoFechaUsuario getUsuarioModifica() {
		return usuarioModifica;
	}
	public void setUsuarioModifica(DtoInfoFechaUsuario usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}
	public InfoDatosInt getEspecialidad() {
		return especialidad;
	}
	public void setEspecialidad(InfoDatosInt especialidad) {
		this.especialidad = especialidad;
	}
	public String getBuscarProgramas() {
		return buscarProgramas;
	}
	public void setBuscarProgramas(String buscarProgramas) {
		this.buscarProgramas = buscarProgramas;
	}
	public ArrayList<String> getEstadosProgramasOservicios() {
		return estadosProgramasOservicios;
	}
	public void setEstadosProgramasOservicios(
			ArrayList<String> estadosProgramasOservicios) {
		this.estadosProgramasOservicios = estadosProgramasOservicios;
	}
	public void setEstadoAutorizacion(String estadoAutorizacion) {
		this.estadoAutorizacion = estadoAutorizacion;
	}
	public String getEstadoAutorizacion() {
		return estadoAutorizacion;
	}
	public int getCodigoHallazgo() {
		return codigoHallazgo;
	}
	public void setCodigoHallazgo(int codigoHallazgo) {
		this.codigoHallazgo = codigoHallazgo;
	}
	public boolean isCargarServicios() {
		return cargarServicios;
	}
	public void setCargarServicios(boolean cargarServicios) {
		this.cargarServicios = cargarServicios;
	}
	/**
	 * @return the valoracion
	 */
	public BigDecimal getValoracion() {
		return valoracion;
	}
	/**
	 * @param valoracion the valoracion to set
	 */
	public void setValoracion(BigDecimal valoracion) {
		this.valoracion = valoracion;
	}
	/**
	 * @return the evolucion
	 */
	public BigDecimal getEvolucion() {
		return evolucion;
	}
	/**
	 * @param evolucion the evolucion to set
	 */
	public void setEvolucion(BigDecimal evolucion) {
		this.evolucion = evolucion;
	}
	public BigDecimal getCodigoCita() {
		return codigoCita;
	}
	public void setCodigoCita(BigDecimal codigoCita) {
		this.codigoCita = codigoCita;
	}
	/**
	 * @return Retirna el atributo numeroSuperficies
	 */
	public Integer getNumeroSuperficies()
	{
		return numeroSuperficies;
	}
	/**
	 * @param numeroSuperficies Asigna el atributo numeroSuperficies
	 */
	public void setNumeroSuperficies(Integer numeroSuperficies)
	{
		this.numeroSuperficies = numeroSuperficies;
	}

	/**
	 * @return the codigoTarifario
	 */
	public String getCodigoTarifario() {
		return codigoTarifario;
	}

	/**
	 * @param codigoTarifario the codigoTarifario to set
	 */
	public void setCodigoTarifario(String codigoTarifario) {
		this.codigoTarifario = codigoTarifario;
	}

	public void setLogDetPlanTratamiento(BigDecimal logDetPlanTratamiento) {
		this.logDetPlanTratamiento = logDetPlanTratamiento;
	}

	public BigDecimal getLogDetPlanTratamiento() {
		return logDetPlanTratamiento;
	}

	/**
	 * @param codigoPlanTratamiento the codigoPlanTratamiento to set
	 */
	public void setCodigoPlanTratamiento(int codigoPlanTratamiento) {
		this.codigoPlanTratamiento = codigoPlanTratamiento;
	}

	/**
	 * @return the codigoPlanTratamiento
	 */
	public int getCodigoPlanTratamiento() {
		return codigoPlanTratamiento;
	}

	/**
	 * @param codigoPaciente the codigoPaciente to set
	 */
	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	/**
	 * @return the codigoPaciente
	 */
	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	/**
	 * @param ingresoPaciente the ingresoPaciente to set
	 */
	public void setIngresoPaciente(int ingresoPaciente) {
		this.ingresoPaciente = ingresoPaciente;
	}

	/**
	 * @return the ingresoPaciente
	 */
	public int getIngresoPaciente() {
		return ingresoPaciente;
	}

	/**
	 * @param programaHallazgoPieza the programaHallazgoPieza to set
	 */
	public void setProgramaHallazgoPieza(DtoProgHallazgoPieza programaHallazgoPieza) {
		this.programaHallazgoPieza = programaHallazgoPieza;
	}

	/**
	 * @return the programaHallazgoPieza
	 */
	public DtoProgHallazgoPieza getProgramaHallazgoPieza() {
		return programaHallazgoPieza;
	}

	/**
	 * @param codigoTipoHallazgo the codigoTipoHallazgo to set
	 */
	public void setCodigoTipoHallazgo(int codigoTipoHallazgo) {
		this.codigoTipoHallazgo = codigoTipoHallazgo;
	}

	/**
	 * @return the codigoTipoHallazgo
	 */
	public int getCodigoTipoHallazgo() {
		return codigoTipoHallazgo;
	}	
}
