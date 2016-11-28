/**
 * 
 */
package com.servinte.axioma.generadorReporte.historiaClinica;

import util.ConstantesBD;

/**
 * @author JorOsoVe
 *
 */
public class DtoFiltroImpresionHistoriaClinica 
{

	/**
	 * 
	 */
	private String fechaInicial;
	
	/**
	 * 
	 */
	private String fechaFinal;
	
	/**
	 * 
	 */
	private String horaInicial;
	
	/**
	 * 
	 */
	private String horaFinal;
	
	/**
	 * 
	 */
	private int cuenta;
	
	/**
	 * 
	 */
	private int cuentaAsociada;
	
	/**
	 * 
	 */
	private int ingreso;
	
	/**
	 * 
	 */
	private String mostrarInformacion;
	
	/**
	 * 
	 */
	private boolean imprimirSignosVitales;
	
		
	/**
	 * 
	 */
	private boolean imprimirValoracionesEnfermeria;
	
	
	/**
	 * atributo para activa impresion de seccion
	 */
	private boolean  imprimirAdminMedicamentos;
	
	/**
	 *atributo para imprimir consumos insumos 
	 */
	private boolean imprimirConsumosInsumos;
	
	/**
	 * 
	 */
	private boolean imprimirOrdenesMedicas;
	
	/**
	 * atributo para indicar si se imprime la orden procedimiento
	 */
	private boolean imprimirOrdenesProcedimiento;
	
	
	/**
	 * 
	 */
	private boolean imprimirSoporteRespiratorio;
	
	/**
	 * 
	 */
	private boolean imprimirControlLiquidos;
	

	/**
	 * 
	 */
	private boolean imprimirEvoluciones;
	
	/**
	 * 
	 */
	private boolean imprimirValUrgencias;
	
	/**
	 * 
	 */
	private boolean imprimirValHospitalizacion;
	
	/**
	 * 
	 */
	private boolean imprimirRespuestaInterpretacionInterconsulta;
	
	/**
	 * 
	 */
	private boolean imprimirValoracionesConsultaExterna;
	
	/**
	 *Indica si se imprime o no la interpretacion respuesta 
	 */
	private boolean imprimirInterpretacionRespuesta;
	
	
	private boolean imprimirNotasEnfermeria;
	
	
	/**
	 * Atributo que define si se imrpime o no la sección de Cateteres y Sondas
	 */
	private boolean imprimirCateteresSondas;
	
	/**
	 * Atributo que define si se imrpime o no la sección de Cuidados Especiales
	 */
	private boolean imprimirCuidadosEspeciales;
	
	/**
	 * 
	 */
	private boolean imprimirOrdenaAmbulatorias;
	
	/**
	 * boolean para saber si se imprime la hoja quirurgica
	 */
	private boolean imprimirCirugia;
	
	private boolean imprimirEscalaGlasgow;

	public DtoFiltroImpresionHistoriaClinica()
	{
		this.mostrarInformacion="";
		this.ingreso=ConstantesBD.codigoNuncaValido;
		this.cuenta=ConstantesBD.codigoNuncaValido;
		this.cuentaAsociada=ConstantesBD.codigoNuncaValido;
		this.fechaInicial="";
		this.fechaFinal="";
		this.horaInicial="";
		this.horaFinal="";
		this.imprimirSignosVitales=false;
		this.imprimirValoracionesEnfermeria=false;
		this.imprimirEvoluciones=false;
		this.imprimirAdminMedicamentos=false;
		this.imprimirConsumosInsumos=false;
		this.imprimirOrdenesMedicas=false;
		this.imprimirOrdenesProcedimiento=false;
		this.imprimirValoracionesConsultaExterna=false;
		this.imprimirSoporteRespiratorio=false;
		this.imprimirControlLiquidos=false;
		this.imprimirValUrgencias=false;
		this.imprimirValHospitalizacion=false;
		this.imprimirRespuestaInterpretacionInterconsulta=false;
		this.imprimirInterpretacionRespuesta=false;
		this.imprimirNotasEnfermeria=false;
		this.imprimirOrdenaAmbulatorias=false;
		this.imprimirCateteresSondas=false;
		this.imprimirCuidadosEspeciales=false;
		this.imprimirCirugia=false;
		this.imprimirEscalaGlasgow = false;
	}




	public String getFechaInicial() {
		return fechaInicial;
	}




	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}




	public String getFechaFinal() {
		return fechaFinal;
	}




	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}




	public int getCuenta() {
		return cuenta;
	}




	public void setCuenta(int cuenta) {
		this.cuenta = cuenta;
	}




	public int getCuentaAsociada() {
		return cuentaAsociada;
	}




	public void setCuentaAsociada(int cuentaAsociada) {
		this.cuentaAsociada = cuentaAsociada;
	}




	public int getIngreso() {
		return ingreso;
	}




	public void setIngreso(int ingreso) {
		this.ingreso = ingreso;
	}




	public boolean isImprimirSignosVitales() {
		return imprimirSignosVitales;
	}




	public void setImprimirSignosVitales(boolean imprimirSignosVitales) {
		this.imprimirSignosVitales = imprimirSignosVitales;
	}




	public boolean isImprimirValoracionesEnfermeria() {
		return imprimirValoracionesEnfermeria;
	}




	public void setImprimirValoracionesEnfermeria(
			boolean imprimirValoracionesEnfermeria) {
		this.imprimirValoracionesEnfermeria = imprimirValoracionesEnfermeria;
	}




	public Boolean isImprimirAdminMedicamentos() {
		return imprimirAdminMedicamentos;
	}




	public void setImprimirAdminMedicamentos(Boolean imprimirAdminMedicamentos) {
		this.imprimirAdminMedicamentos = imprimirAdminMedicamentos;
	}




	public Boolean isImprimirConsumosInsumos() {
		return imprimirConsumosInsumos;
	}




	public void setImprimirConsumosInsumos(Boolean imprimirConsumosInsumos) {
		this.imprimirConsumosInsumos = imprimirConsumosInsumos;
	}




	public Boolean isImprimirOrdenesProcedimiento() {
		return imprimirOrdenesProcedimiento;
	}




	public void setImprimirOrdenesProcedimiento(Boolean imprimirOrdenesProcedimiento) {
		this.imprimirOrdenesProcedimiento = imprimirOrdenesProcedimiento;
	}




	public boolean isImprimirSoporteRespiratorio() {
		return imprimirSoporteRespiratorio;
	}




	public void setImprimirSoporteRespiratorio(boolean imprimirSoporteRespiratorio) {
		this.imprimirSoporteRespiratorio = imprimirSoporteRespiratorio;
	}




	public boolean isImprimirControlLiquidos() {
		return imprimirControlLiquidos;
	}




	public void setImprimirControlLiquidos(boolean imprimirControlLiquidos) {
		this.imprimirControlLiquidos = imprimirControlLiquidos;
	}




	public boolean isImprimirEvoluciones() {
		return imprimirEvoluciones;
	}




	public void setImprimirEvoluciones(boolean imprimirEvoluciones) {
		this.imprimirEvoluciones = imprimirEvoluciones;
	}




	public boolean isImprimirValUrgencias() {
		return imprimirValUrgencias;
	}




	public void setImprimirValUrgencias(boolean imprimirValUrgencias) {
		this.imprimirValUrgencias = imprimirValUrgencias;
	}




	public boolean isImprimirValHospitalizacion() {
		return imprimirValHospitalizacion;
	}




	public void setImprimirValHospitalizacion(boolean imprimirValHospitalizacion) {
		this.imprimirValHospitalizacion = imprimirValHospitalizacion;
	}




	public boolean isImprimirRespuestaInterpretacionInterconsulta() {
		return imprimirRespuestaInterpretacionInterconsulta;
	}




	public void setImprimirRespuestaInterpretacionInterconsulta(
			boolean imprimirRespuestaInterpretacionInterconsulta) {
		this.imprimirRespuestaInterpretacionInterconsulta = imprimirRespuestaInterpretacionInterconsulta;
	}




	public boolean isImprimirValoracionesConsultaExterna() {
		return imprimirValoracionesConsultaExterna;
	}




	public void setImprimirValoracionesConsultaExterna(
			boolean imprimirValoracionesConsultaExterna) {
		this.imprimirValoracionesConsultaExterna = imprimirValoracionesConsultaExterna;
	}




	public Boolean isImprimirInterpretacionRespuesta() {
		return imprimirInterpretacionRespuesta;
	}




	public void setImprimirInterpretacionRespuesta(
			Boolean imprimirInterpretacionRespuesta) {
		this.imprimirInterpretacionRespuesta = imprimirInterpretacionRespuesta;
	}




	public boolean isImprimirNotasEnfermeria() {
		return imprimirNotasEnfermeria;
	}




	public void setImprimirNotasEnfermeria(boolean imprimirNotasEnfermeria) {
		this.imprimirNotasEnfermeria = imprimirNotasEnfermeria;
	}




	public boolean isImprimirCateteresSondas() {
		return imprimirCateteresSondas;
	}




	public void setImprimirCateteresSondas(boolean imprimirCateteresSondas) {
		this.imprimirCateteresSondas = imprimirCateteresSondas;
	}




	public boolean isImprimirCuidadosEspeciales() {
		return imprimirCuidadosEspeciales;
	}




	public void setImprimirCuidadosEspeciales(boolean imprimirCuidadosEspeciales) {
		this.imprimirCuidadosEspeciales = imprimirCuidadosEspeciales;
	}




	public boolean isImprimirOrdenaAmbulatorias() {
		return imprimirOrdenaAmbulatorias;
	}




	public void setImprimirOrdenaAmbulatorias(boolean imprimirOrdenaAmbulatorias) {
		this.imprimirOrdenaAmbulatorias = imprimirOrdenaAmbulatorias;
	}




	public String getHoraInicial() {
		return horaInicial;
	}




	public void setHoraInicial(String horaInicial) {
		this.horaInicial = horaInicial;
	}




	public String getHoraFinal() {
		return horaFinal;
	}




	public void setHoraFinal(String horaFinal) {
		this.horaFinal = horaFinal;
	}




	public String getMostrarInformacion() {
		return mostrarInformacion;
	}




	public void setMostrarInformacion(String mostrarInformacion) {
		this.mostrarInformacion = mostrarInformacion;
	}




	public boolean isImprimirOrdenesMedicas() {
		return imprimirOrdenesMedicas;
	}




	public void setImprimirOrdenesMedicas(boolean imprimirOrdenesMedicas) {
		this.imprimirOrdenesMedicas = imprimirOrdenesMedicas;
	}




	/**
	 * @return the imprimirCirugia
	 */
	public boolean isImprimirCirugia() {
		return imprimirCirugia;
	}




	/**
	 * @param imprimirCirugia the imprimirCirugia to set
	 */
	public void setImprimirCirugia(boolean imprimirCirugia) {
		this.imprimirCirugia = imprimirCirugia;
	}

	public boolean isImprimirEscalaGlasgow() {
		return imprimirEscalaGlasgow;
	}
	
	public void setImprimirEscalaGlasgow(boolean imprimirEscalaGlasgow) {
		this.imprimirEscalaGlasgow = imprimirEscalaGlasgow;
	}
	
}
