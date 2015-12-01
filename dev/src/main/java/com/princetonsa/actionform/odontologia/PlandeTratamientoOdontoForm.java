package com.princetonsa.actionform.odontologia;

import java.math.BigDecimal;
import java.util.ArrayList;

import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ValoresPorDefecto;
import util.odontologia.InfoIngresoPlanTratamiento;
import util.odontologia.InfoPlanTratamiento;

import com.princetonsa.dto.odontologia.DtoLogPlanTratamiento;
import com.princetonsa.dto.odontologia.DtoLogProgServPlant;
import com.princetonsa.dto.odontologia.DtoMotivosAtencion;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoOdo;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologico;
import com.princetonsa.dto.odontologia.DtoServArtIncCitaOdo;
import com.princetonsa.mundo.odontologia.PresupuestoOdontologico;


public class PlandeTratamientoOdontoForm extends ValidatorForm {

	
	/**
	 * OBJETO PARA CONSTRUIR LA INTERFAZ GRAFICA DEL PLAN DE TRATAMIENTO
	 */
	private InfoPlanTratamiento infoPlanTratamiento = new InfoPlanTratamiento();
	/**
	 * 
	 */
	private String estado;
	
	/**
	 * 
	 * 
	 */
	private DtoPlanTratamientoOdo dtoPlanTratamiento;
	
	/**
	 * 
	 * 
	 */
	private DtoPlanTratamientoOdo dtoLogPlanTratamiento;

	/**
	 * 
	 * 
	 */
	private boolean utilizaProgramas; //OJO para que
	
	/**
	 * 
	 */
	
	
	private ArrayList<DtoLogPlanTratamiento> arrayLogs = new ArrayList<DtoLogPlanTratamiento>();
	/***
	 * 
	 * 
	 * 
	 */
	
	private ArrayList<DtoLogProgServPlant> arrayLogProg = new ArrayList<DtoLogProgServPlant>();
	
	/***
	 * 
	 * 
	 * @param institucion
	 */
	private boolean mostrarProgramas; //OJO ---para que
	/**
	 * 
	 * 
	 */
	
	private DtoPresupuestoOdontologico dtoPresupuesto= new DtoPresupuestoOdontologico();
	/**
	 * 
	 * 
	 * 
	 */
	private DtoPresupuestoOdontologico dtoLogPresupuesto;
	
	
	
	/**
	 * ArrayList Motivos  
	 */
	private ArrayList<DtoMotivosAtencion> listaMotivos;
	
	/**
	 * Validar Presupuesto contratado
	 * 
	 */
	private String validaPresupuestoOdoContratado;
	
	/**
	 * TMP PARA EL CODIGO DEL PROGRAMA
	 */
	private BigDecimal tmpCodigoPrograma;
	private BigDecimal tmpCodigoDetallePlanTratamiento;
	private int tmpCodigoServicio;
	
	
	/**
	 * 	LISTA LOS HISTORICOS DEL LOS PROGRAMAS 
	 */
	private ArrayList<DtoLogProgServPlant> listaLogProgServPlant = new ArrayList<DtoLogProgServPlant>();
	
	/**
	 * CARGAR LISTA DE SERVICIOS ARTICULOS INCLUIDOS  
	 */
	private ArrayList<DtoServArtIncCitaOdo> listaServArtIncPlanT = new ArrayList<DtoServArtIncCitaOdo>();
	
	/**
	 * LISTA PARA CARGAR LOS INGRESOS DEL PACIENTE
	 */
	private ArrayList<InfoIngresoPlanTratamiento> listaIngresoPlan= new ArrayList<InfoIngresoPlanTratamiento>();
	/**
	 * Info Ingreso Plan de Tratamiento
	 */
	private InfoIngresoPlanTratamiento infoIngresoPlanTratamiento;
	
	/**
	 * Posicion del Arreglo listaIngresoPlan
	 */
	private int postArrayIngresoPlan;
	
	
	/** * Indica si el apciente tiene algun ingreso abierto*/
	private boolean tieneIngresoAbierto;
	
	
	/**
	 * 
	 * @param institucion
	 */
	public void reset(int institucion)
	{
	 this.estado= new String("");	
	 this.dtoPlanTratamiento= new DtoPlanTratamientoOdo();
	 this.setUtilizaProgramas(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(institucion).equals(ConstantesBD.acronimoSi)); //CREO QUE MALA PRACTICA
	 this.arrayLogs = new ArrayList<DtoLogPlanTratamiento>();
	 this.arrayLogProg = new ArrayList<DtoLogProgServPlant>();
	 this.mostrarProgramas = true;
	 this.dtoPresupuesto = new DtoPresupuestoOdontologico();
	 this.dtoLogPlanTratamiento = new DtoPlanTratamientoOdo();
	 this.dtoLogPresupuesto = new DtoPresupuestoOdontologico();
	 this.infoPlanTratamiento = new InfoPlanTratamiento();
	 this.listaMotivos = new ArrayList<DtoMotivosAtencion>();
	 this.setValidaPresupuestoOdoContratado("");
	 this.tmpCodigoPrograma = BigDecimal.ZERO;
	 this.tmpCodigoDetallePlanTratamiento = BigDecimal.ZERO;
	 this.tmpCodigoServicio = ConstantesBD.codigoNuncaValido;
	 this.listaLogProgServPlant = new ArrayList<DtoLogProgServPlant>();
	 this.setListaServArtIncPlanT(new ArrayList<DtoServArtIncCitaOdo>());
	 this.listaIngresoPlan = new ArrayList<InfoIngresoPlanTratamiento>();
	 this.postArrayIngresoPlan=ConstantesBD.codigoNuncaValido;
	 this.infoIngresoPlanTratamiento= new InfoIngresoPlanTratamiento();
	 //this.tieneIngresoAbierto = false;
	}
	
	
	
	
	
	
	
	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado){
		this.estado = estado;
	}

	/**
	 * @return the dtoPlanTratamiento
	 */
	public DtoPlanTratamientoOdo getDtoPlanTratamiento() {
		return dtoPlanTratamiento;
	}

	/**
	 * @param dtoPlanTratamiento the dtoPlanTratamiento to set
	 */
	public void setDtoPlanTratamiento(DtoPlanTratamientoOdo dtoPlanTratamiento) {
		this.dtoPlanTratamiento = dtoPlanTratamiento;
	}

	public void setInfoPlanTratamiento(InfoPlanTratamiento infoPlanTratamiento) {
		this.infoPlanTratamiento = infoPlanTratamiento;
	}

	public InfoPlanTratamiento getInfoPlanTratamiento() {
		return infoPlanTratamiento;
	}


	public void setUtilizaProgramas(boolean utilizaProgramas) {
		this.utilizaProgramas = utilizaProgramas;
	}


	public boolean isUtilizaProgramas() {
		return utilizaProgramas;
	}


	/**
	 * @return the arrayLogs
	 */
	public ArrayList<DtoLogPlanTratamiento> getArrayLogs() {
		return arrayLogs;
	}


	/**
	 * @param arrayLogs the arrayLogs to set
	 */
	public void setArrayLogs(ArrayList<DtoLogPlanTratamiento> arrayLogs) {
		this.arrayLogs = arrayLogs;
	}


	/**
	 * @return the arrayLogProg
	 */
	public ArrayList<DtoLogProgServPlant> getArrayLogProg() {
		return arrayLogProg;
	}


	/**
	 * @param arrayLogProg the arrayLogProg to set
	 */
	public void setArrayLogProg(ArrayList<DtoLogProgServPlant> arrayLogProg) {
		this.arrayLogProg = arrayLogProg;
	}


	/**
	 * @return the mostrarProgramas
	 */
	public boolean isMostrarProgramas() {
		return mostrarProgramas;
	}


	/**
	 * @param mostrarProgramas the mostrarProgramas to set
	 */
	public void setMostrarProgramas(boolean mostrarProgramas) {
		this.mostrarProgramas = mostrarProgramas;
	}


	/**
	 * @return the dtoLogPlanTratamiento
	 */
	public DtoPlanTratamientoOdo getDtoLogPlanTratamiento() {
		return dtoLogPlanTratamiento;
	}


	/**
	 * @param dtoLogPlanTratamiento the dtoLogPlanTratamiento to set
	 */
	public void setDtoLogPlanTratamiento(DtoPlanTratamientoOdo dtoLogPlanTratamiento) {
		this.dtoLogPlanTratamiento = dtoLogPlanTratamiento;
	}


	/**
	 * @return the dtoLogPresupuesto
	 */
	public DtoPresupuestoOdontologico getDtoLogPresupuesto() {
		return dtoLogPresupuesto;
	}


	/**
	 * @param dtoLogPresupuesto the dtoLogPresupuesto to set
	 */
	public void setDtoLogPresupuesto(DtoPresupuestoOdontologico dtoLogPresupuesto) {
		this.dtoLogPresupuesto = dtoLogPresupuesto;
	}


	/**
	 * @return the dtoPresupuesto
	 */
	public DtoPresupuestoOdontologico getDtoPresupuesto() {
		return dtoPresupuesto;
	}


	/**
	 * @param dtoPresupuesto the dtoPresupuesto to set
	 */
	public void setDtoPresupuesto(DtoPresupuestoOdontologico dtoPresupuesto) {
		this.dtoPresupuesto = dtoPresupuesto;
	}


	public void setListaMotivos(ArrayList<DtoMotivosAtencion> listaMotivos) {
		this.listaMotivos = listaMotivos;
	}


	public ArrayList<DtoMotivosAtencion> getListaMotivos() {
		return listaMotivos;
	}


	public void setValidaPresupuestoOdoContratado(
			String validaPresupuestoOdoContratado) {
		this.validaPresupuestoOdoContratado = validaPresupuestoOdoContratado;
	}


	public String getValidaPresupuestoOdoContratado() {
		return validaPresupuestoOdoContratado;
	}


	public void setTmpCodigoPrograma(BigDecimal tmpCodigoPrograma) {
		this.tmpCodigoPrograma = tmpCodigoPrograma;
	}


	public BigDecimal getTmpCodigoPrograma() {
		return tmpCodigoPrograma;
	}
	
	public String getMensajeCancelacion()
	{
		String mensaje="El Plan de Tratamiento sera Cancelado. ";
		ArrayList<DtoPresupuestoOdontologico> array=this.getPresupuestosPlanTratamiento();
		if(array.size()>0)
		{
			mensaje+="Al cancelar el Plan de Tratamiento se cancelará automáticamente el presupuesto ";
			for(DtoPresupuestoOdontologico dto: array)
			{
				mensaje+=dto.getConsecutivo()+", ";
			}
			mensaje+=" ¿DESEA CONTINUAR? ";
		}
		return mensaje;
	}
	
	public ArrayList<DtoPresupuestoOdontologico> getPresupuestosPlanTratamiento()
	{
		DtoPresupuestoOdontologico dto= new DtoPresupuestoOdontologico();
		dto.setPlanTratamiento(this.getDtoPlanTratamiento().getCodigoPk());
		ArrayList<String> estadosPresupuesto= new ArrayList<String>();
		estadosPresupuesto.add(ConstantesIntegridadDominio.acronimoEstadoActivo);
		estadosPresupuesto.add(ConstantesIntegridadDominio.acronimoContratadoContratado);
		estadosPresupuesto.add(ConstantesIntegridadDominio.acronimoContratadoSuspendidoTemporalmente);
		return PresupuestoOdontologico.cargarPresupuesto(dto);
	}







	public void setTmpCodigoDetallePlanTratamiento(
			BigDecimal tmpCodigoDetallePlanTratamiento) {
		this.tmpCodigoDetallePlanTratamiento = tmpCodigoDetallePlanTratamiento;
	}







	public BigDecimal getTmpCodigoDetallePlanTratamiento() {
		return tmpCodigoDetallePlanTratamiento;
	}







	public void setTmpCodigoServicio(int tmpCodigoServicio) {
		this.tmpCodigoServicio = tmpCodigoServicio;
	}







	public int getTmpCodigoServicio() {
		return tmpCodigoServicio;
	}







	public void setListaLogProgServPlant(ArrayList<DtoLogProgServPlant> listaLogProgServPlant) {
		this.listaLogProgServPlant = listaLogProgServPlant;
	}







	public ArrayList<DtoLogProgServPlant> getListaLogProgServPlant() {
		return listaLogProgServPlant;
	}







	public void setListaServArtIncPlanT(ArrayList<DtoServArtIncCitaOdo> listaServArtIncPlanT) {
		this.listaServArtIncPlanT = listaServArtIncPlanT;
	}







	public ArrayList<DtoServArtIncCitaOdo> getListaServArtIncPlanT() {
		return listaServArtIncPlanT;
	}







	public void setListaIngresoPlan(ArrayList<InfoIngresoPlanTratamiento> listaIngresoPlan) {
		this.listaIngresoPlan = listaIngresoPlan;
	}







	public ArrayList<InfoIngresoPlanTratamiento> getListaIngresoPlan() {
		return listaIngresoPlan;
	}







	public void setPostArrayIngresoPlan(int postArrayIngresoPlan) {
		this.postArrayIngresoPlan = postArrayIngresoPlan;
	}







	public int getPostArrayIngresoPlan() {
		return postArrayIngresoPlan;
	}







	public void setInfoIngresoPlanTratamiento(InfoIngresoPlanTratamiento infoIngresoPlanTratamiento) {
		this.infoIngresoPlanTratamiento = infoIngresoPlanTratamiento;
	}







	public InfoIngresoPlanTratamiento getInfoIngresoPlanTratamiento() {
		return infoIngresoPlanTratamiento;
	}







	public boolean isTieneIngresoAbierto() {
		return tieneIngresoAbierto;
	}







	public void setTieneIngresoAbierto(boolean tieneIngresoAbierto) {
		this.tieneIngresoAbierto = tieneIngresoAbierto;
	}
	
}
