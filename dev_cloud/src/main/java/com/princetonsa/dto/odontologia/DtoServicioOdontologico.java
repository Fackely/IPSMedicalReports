package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.odontologia.InfoTarifaServicioPresupuesto;

/**
 * 
 * @author Víctor Hugo Gómez L. 
 *
 */

public class DtoServicioOdontologico implements Serializable
{
	/**
	 * Versión serial
	 */
	
	/**
	 * Código pk del registro en la tabla servicios_cita_odontologica
	 */
	private long codigoPk;
	
	private static final long serialVersionUID = 1L;
	private int codigoServicio;
	private String codigoServTarifario;
	private int minutosduracion;
	private int minutosduracionNuevos;
	
	private int especialidad;
	private int ordenServicio; 
	private String descripcionServicio;
	private String codigoTipoServicio;
	private String habilitarSeleccion;
	private String garantiaServicio;
	private String estadoPlanTratamiento;
	//private int codigoProgSerPlanTrata;
	private int codigoPrograma;
	private String nombrePrograma;
	private String siglaPrograma;
	private String seccionPlanTrata;
	private int codigoPiezaDental;
	private ArrayList<DtoSuperficiesPorPrograma> superficies=new ArrayList<DtoSuperficiesPorPrograma>() ;


	//private int superficieDental;
	private String despPiezaDental;
	private String edadAplicaPD;
	private int codigoPresuOdoProgSer;
	private String casoBusServicio;
	private String asociarSerCita;
	private ArrayList<InfoDatosInt> condicionesToma;
	private String estaVinculadoSerCita;
	private int codigoPlanTratamiento;
	
	private DtoProgHallazgoPieza programaHallazgoPieza;
	
	
	private InfoTarifaServicioPresupuesto infoTarifa;
	private DtoSubCuentas responsableServicio;
	
	// atributos validacion para el cambio de servicio
	private String facturado;
	
	private String aplicaAnticipo;
	private String aplicaAbono;
	/** * Boolean, indica si el paciente paga o no la atención  */
	private boolean pacientePagaAtencion;
	/** * Atributo que almacena la fecha de la cita en estado Asignado o Reservado que esta relacionada con el servicio */
	private String fechaCita;
	private String horaCita;
	private DtoPaquetesOdontologicos paquete;
	
	/** * Codigo de la cita asociada a este servicio  */
	private int codigoCita;
	
	/**
	 * Atributo que indica si el servicio se muestra o no en la p&aacute;gina
	 * de Otros servicios
	 */
	private boolean muestraEnPantalla;
	
	/**
	 * Atributo con el indicativo asociado al servicio.
	 */
	private String indicativo;
	
	/**
	 * Estado de la cita a la cual esta vinculado el servicio.
	 */
	private String estadoCitaVinculadoServicio;
	
	
	public DtoServicioOdontologico()
	{
		this.reset();
	}
	
	public void reset()
	{
		
		this.codigoServicio = ConstantesBD.codigoNuncaValido;
		this.codigoServTarifario= "";
		//this.minutosduracion = ConstantesBD.codigoNuncaValido;
		this.especialidad = ConstantesBD.codigoNuncaValido;
		this.ordenServicio = ConstantesBD.codigoNuncaValido; 
		this.descripcionServicio = "";
		this.codigoTipoServicio = "";
		this.habilitarSeleccion = ConstantesBD.acronimoNo;
		this.garantiaServicio = ConstantesBD.acronimoNo;
		this.estadoPlanTratamiento = "";
		this.codigoPrograma = ConstantesBD.codigoNuncaValido;
		this.nombrePrograma = "";
		this.siglaPrograma = "";
		this.seccionPlanTrata = "";
		this.codigoPiezaDental = ConstantesBD.codigoNuncaValido;
		this.despPiezaDental = "";
		this.edadAplicaPD = "";
		this.codigoPresuOdoProgSer = ConstantesBD.codigoNuncaValido;
		this.casoBusServicio = "";
		this.asociarSerCita = ConstantesBD.acronimoNo;
		this.condicionesToma = new ArrayList<InfoDatosInt>();
		this.estaVinculadoSerCita = ConstantesBD.acronimoNo;
		this.codigoPlanTratamiento= ConstantesBD.codigoNuncaValido;
		this.superficies=new ArrayList<DtoSuperficiesPorPrograma>() ;
		this.programaHallazgoPieza=new DtoProgHallazgoPieza();
		// atributos validacion para el cambio de servicio
		this.facturado = ConstantesBD.acronimoNo;
		this.minutosduracion=0;
		
		this.fechaCita="";
		this.horaCita="";
		
		this.paquete=new DtoPaquetesOdontologicos();
		
		this.setMuestraEnPantalla(true);
		
		this.setEstadoCitaVinculadoServicio("");
		
		this.setIndicativo("");
		
		this.codigoCita = ConstantesBD.codigoNuncaValido;
		
		this.setCodigoPk(ConstantesBD.codigoNuncaValidoLong);
	
	}

	public DtoProgHallazgoPieza getProgramaHallazgoPieza() {
		return programaHallazgoPieza;
	}

	public void setProgramaHallazgoPieza(DtoProgHallazgoPieza programaHallazgoPieza) {
		this.programaHallazgoPieza = programaHallazgoPieza;
	}

	
	public String getHoraCita() {
		return horaCita;
	}

	public void setHoraCita(String horaCita) {
		this.horaCita = horaCita;
	}

	public String getFechaCita() {
		return fechaCita;
	}

	public void setFechaCita(String fechaCita) {
		this.fechaCita = fechaCita;
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
	 * @return the minutosduracion
	 */
	public int getMinutosduracion() {
		return minutosduracion;
	}

	/**
	 * @param minutosduracion the minutosduracion to set
	 */
	public void setMinutosduracion(int minutosduracion) {
		this.minutosduracion = minutosduracion;
	}

	/**
	 * @return the especialidad
	 */
	public int getEspecialidad() {
		return especialidad;
	}

	/**
	 * @param especialidad the especialidad to set
	 */
	public void setEspecialidad(int especialidad) {
		this.especialidad = especialidad;
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
	 * @return the descripcionServicio
	 */
	public String getDescripcionServicio() {
		return descripcionServicio;
	}

	/**
	 * @param descripcionServicio the descripcionServicio to set
	 */
	public void setDescripcionServicio(String descripcionServicio) {
		this.descripcionServicio = descripcionServicio;
	}

	/**
	 * @return the condicionesToma
	 */
	public ArrayList<InfoDatosInt> getCondicionesToma() {
		return condicionesToma;
	}

	/**
	 * @param condicionesToma the condicionesToma to set
	 */
	public void setCondicionesToma(ArrayList<InfoDatosInt> condicionesToma) {
		this.condicionesToma = condicionesToma;
	}

	/**
	 * @return the asociarSerCita
	 */
	public String getAsociarSerCita() {
		return asociarSerCita;
	}

	/**
	 * @param asociarSerCita the asociarSerCita to set
	 */
	public void setAsociarSerCita(String asociarSerCita) {
		this.asociarSerCita = asociarSerCita;
	}

	/**
	 * @return the habilitarSeleccion
	 */
	public String getHabilitarSeleccion() {
		return habilitarSeleccion;
	}

	/**
	 * @param habilitarSeleccion the habilitarSeleccion to set
	 */
	public void setHabilitarSeleccion(String habilitarSeleccion) {
		this.habilitarSeleccion = habilitarSeleccion;
	}

	/**
	 * @return the garantiaServicio
	 */
	public String getGarantiaServicio() {
		return garantiaServicio;
	}

	/**
	 * @param garantiaServicio the garantiaServicio to set
	 */
	public void setGarantiaServicio(String garantiaServicio) {
		this.garantiaServicio = garantiaServicio;
	}


	/**
	 * @return the codigoPrograma
	 */
	public int getCodigoPrograma() {
		return codigoPrograma;
	}

	/**
	 * @param codigoPrograma the codigoPrograma to set
	 */
	public void setCodigoPrograma(int codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}

	/**
	 * @return the nombrePrograma
	 */
	public String getNombrePrograma() {
		return nombrePrograma;
	}

	/**
	 * @param nombrePrograma the nombrePrograma to set
	 */
	public void setNombrePrograma(String nombrePrograma) {
		this.nombrePrograma = nombrePrograma;
	}

	/**
	 * @return the siglaPrograma
	 */
	public String getSiglaPrograma() {
		return siglaPrograma;
	}

	/**
	 * @param siglaPrograma the siglaPrograma to set
	 */
	public void setSiglaPrograma(String siglaPrograma) {
		this.siglaPrograma = siglaPrograma;
	}

	/**
	 * @return the seccionPlanTrata
	 */
	public String getSeccionPlanTrata() {
		return seccionPlanTrata;
	}

	/**
	 * @param seccionPlanTrata the seccionPlanTrata to set
	 */
	public void setSeccionPlanTrata(String seccionPlanTrata) {
		this.seccionPlanTrata = seccionPlanTrata;
	}

	/**
	 * @return the codigoPiezaDental
	 */
	public int getCodigoPiezaDental() {
		return codigoPiezaDental;
	}

	/**
	 * @param codigoPiezaDental the codigoPiezaDental to set
	 */
	public void setCodigoPiezaDental(int codigoPiezaDental) {
		this.codigoPiezaDental = codigoPiezaDental;
	}

	/**
	 * @return the despPiezaDental
	 */
	public String getDespPiezaDental() {
		return despPiezaDental;
	}

	/**
	 * @param despPiezaDental the despPiezaDental to set
	 */
	public void setDespPiezaDental(String despPiezaDental) {
		this.despPiezaDental = despPiezaDental;
	}

	/**
	 * @return the edadAplicaPD
	 */
	public String getEdadAplicaPD() {
		return edadAplicaPD;
	}

	/**
	 * @param edadAplicaPD the edadAplicaPD to set
	 */
	public void setEdadAplicaPD(String edadAplicaPD) {
		this.edadAplicaPD = edadAplicaPD;
	}

	/**
	 * @return the estadoPlanTratamiento
	 */
	public String getEstadoPlanTratamiento() {
		return estadoPlanTratamiento;
	}

	/**
	 * @param estadoPlanTratamiento the estadoPlanTratamiento to set
	 */
	public void setEstadoPlanTratamiento(String estadoPlanTratamiento) {
		this.estadoPlanTratamiento = estadoPlanTratamiento;
	}

	/**
	 * @return the casoBusServicio
	 */
	public String getCasoBusServicio() {
		return casoBusServicio;
	}

	/**
	 * @param casoBusServicio the casoBusServicio to set
	 */
	public void setCasoBusServicio(String casoBusServicio) {
		this.casoBusServicio = casoBusServicio;
	}

	/**
	 * @return the codigoPresuOdoProgSer
	 */
	public int getCodigoPresuOdoProgSer() {
		return codigoPresuOdoProgSer;
	}

	/**
	 * @param codigoPresuOdoProgSer the codigoPresuOdoProgSer to set
	 */
	public void setCodigoPresuOdoProgSer(int codigoPresuOdoProgSer) {
		this.codigoPresuOdoProgSer = codigoPresuOdoProgSer;
	}

	/**
	 * @return the estaVinculadoSerCita
	 */
	public String getEstaVinculadoSerCita() {
		return estaVinculadoSerCita;
	}

	/**
	 * @param estaVinculadoSerCita the estaVinculadoSerCita to set
	 */
	public void setEstaVinculadoSerCita(String estaVinculadoSerCita) {
		this.estaVinculadoSerCita = estaVinculadoSerCita;
	}

	/**
	 * @return the codigoPlanTratamiento
	 */
	public int getCodigoPlanTratamiento() {
		return codigoPlanTratamiento;
	}

	/**
	 * @param codigoPlanTratamiento the codigoPlanTratamiento to set
	 */
	public void setCodigoPlanTratamiento(int codigoPlanTratamiento) {
		this.codigoPlanTratamiento = codigoPlanTratamiento;
	}

	/**
	 * @return the codigoTipoServicio
	 */
	public String getCodigoTipoServicio() {
		return codigoTipoServicio;
	}

	/**
	 * @param codigoTipoServicio the codigoTipoServicio to set
	 */
	public void setCodigoTipoServicio(String codigoTipoServicio) {
		this.codigoTipoServicio = codigoTipoServicio;
	}

	/**
	 * @return the facturado
	 */
	public String getFacturado() {
		return facturado;
	}

	/**
	 * @param facturado the facturado to set
	 */
	public void setFacturado(String facturado) {
		this.facturado = facturado;
	}

	/**
	 * @return the infoTarifa
	 */
	public InfoTarifaServicioPresupuesto getInfoTarifa() {
		return infoTarifa;
	}

	/**
	 * @param infoTarifa the infoTarifa to set
	 */
	public void setInfoTarifa(InfoTarifaServicioPresupuesto infoTarifa) {
		this.infoTarifa = infoTarifa;
	}

	/**
	 * @return the responsableServicio
	 */
	public DtoSubCuentas getResponsableServicio() {
		return responsableServicio;
	}

	/**
	 * @param responsableServicio the responsableServicio to set
	 */
	public void setResponsableServicio(DtoSubCuentas responsableServicio) {
		this.responsableServicio = responsableServicio;
	}

	/**
	 * @return the aplicaAnticipo
	 */
	public String getAplicaAnticipo() {
		return aplicaAnticipo;
	}

	/**
	 * @param aplicaAnticipo the aplicaAnticipo to set
	 */
	public void setAplicaAnticipo(String aplicaAnticipo) {
		this.aplicaAnticipo = aplicaAnticipo;
	}

	/**
	 * @return the aplicaAbono
	 */
	public String getAplicaAbono() {
		return aplicaAbono;
	}

	/**
	 * @param aplicaAbono the aplicaAbono to set
	 */
	public void setAplicaAbono(String aplicaAbono) {
		this.aplicaAbono = aplicaAbono;
	}

	/**
	 * @return the pacientePagaAtencion
	 */
	public boolean getPacientePagaAtencion() {
		return pacientePagaAtencion;
	}

	/**
	 * @param pacientePagaAtencion the pacientePagaAtencion to set
	 */
	public void setPacientePagaAtencion(boolean pacientePagaAtencion) {
		this.pacientePagaAtencion = pacientePagaAtencion;
	}



	/**
	 * @return the codigoServTarifario
	 */
	public String getCodigoServTarifario() {
		return codigoServTarifario;
	}

	/**
	 * @param codigoServTarifario the codigoServTarifario to set
	 */
	public void setCodigoServTarifario(String codigoServTarifario) {
		this.codigoServTarifario = codigoServTarifario;
	}

	

	public DtoPaquetesOdontologicos getPaquete() {
		return paquete;
	}

	public void setPaquete(DtoPaquetesOdontologicos paquete) {
		this.paquete = paquete;
	}

	/**
	 * @param muestraEnPantalla the muestraEnPantalla to set
	 */
	public void setMuestraEnPantalla(boolean muestraEnPantalla) {
		this.muestraEnPantalla = muestraEnPantalla;
	}

	/**
	 * @return the muestraEnPantalla
	 */
	public boolean isMuestraEnPantalla() {
		return muestraEnPantalla;
	}

/**
	 * @return minutosduracionNuevos
	 */
	public int getMinutosduracionNuevos() {
		return minutosduracionNuevos;
	}

	/**
	 * @param asigna minutosduracionNuevos
	 */
	public void setMinutosduracionNuevos(int minutosduracionNuevos) {
		this.minutosduracionNuevos = minutosduracionNuevos;
	}
	
	
	public ArrayList<DtoSuperficiesPorPrograma> getSuperficies() {
		return superficies;
	}

	public void setSuperficies(ArrayList<DtoSuperficiesPorPrograma> superficies) {
		this.superficies = superficies;
	}
	

	/**
	 * @param indicativo
	 */
	public void setIndicativo(String indicativo) {
		this.indicativo = indicativo;
	}

	/**
	 * @return
	 */
	public String getIndicativo() {
		return indicativo;
	}

	
	public int getCodigoCita() {
		return codigoCita;
	}

	public void setCodigoCita(int codigoCita) {
		this.codigoCita = codigoCita;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}

	/**
	 * @return the codigoPk
	 */
	public long getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param estadoCitaVinculadoServicio the estadoCitaVinculadoServicio to set
	 */
	public void setEstadoCitaVinculadoServicio(
			String estadoCitaVinculadoServicio) {
		this.estadoCitaVinculadoServicio = estadoCitaVinculadoServicio;
	}

	/**
	 * @return the estadoCitaVinculadoServicio
	 */
	public String getEstadoCitaVinculadoServicio() {
		return estadoCitaVinculadoServicio;
	}

}
