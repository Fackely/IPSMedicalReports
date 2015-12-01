/**
 * 
 */
package com.servinte.axioma.generadorReporte.historiaClinica;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dto.manejoPaciente.DtoNotaAclaratoria;



/**
 * Dto creado con el fin de tener identifiado que secciones del reporte sevan a imprimir
 * @author LuiCasOv
 *
 */
public class DtoImpresionHistoriaClinica 
{
	
	/**
	 * id del ingreso del paciente 
	 */
	private String idIngreso;
	
	/**
	 * cuenta del paciente 
	 */
	private String cuenta;
	
	/**
	 * Cuenta asociada del paciente 
	 */
	private String cuentaAsociada;
	
	
	/**
	 * Lista con el resultado de la seccion evolucion
	 */
	private ArrayList<DtoResultadoImpresionHistoriaClinica> resultadoSeccionEvolucion;
	
	/**
	 *Mapa con valores de interprestacion respuesta 
	 */
	private HashMap interpretacionRespuesta;
	
	/**
	 *Indica si se imprime o no la interpretacion respuesta 
	 */
	private Boolean imprimirInterpretacionRespuesta;
	
	/**
	 * atributo para activa impresion de seccion
	 */
	private Boolean  imprimirAdminMedicamentos;
	
	/**
	 * atributo con lista de campos de admin medicamentos
	 */
	private HashMap adminMedicamente;
	
	
	/**
	 *atributo para imprimir consumos insumos 
	 */
	private Boolean imprimirConsumosInsumos;
	
	/**
	 *mapa con informacion de consumos insumos  
	 */
	private HashMap consumosInsumos;
	
	
	
	/**
	 * Si se imprimen los signos vitales 
	 */
	private boolean imprimirSignosVitales;
	
	
	/**
	 * Dto con la información de los signos vitales 
	 */
	private DtoSignosVitalesHC signosVitales;
	
	
	/**
	 * Si se debe imprimir la informacion de las valoraciones de enfermeria 
	 */
	private Boolean imprimirValoracionesEnfermeria;
	
	/**
	 * Mapa con la información de las valoraciones de enfermeria
	 */
	private HashMap valoracionesEnfermeria;
	
	/**
	 *Usuario de la aplciación 
	 */
	private UsuarioBasico usuario;
	
	/**
	 * Paciente a tratar y de impresion de HC
	 */
	private  PersonaBasica paciente;
	
	
	/**
	 * atributo para indicar si se imprime la orden procedimiento
	 */
	private Boolean imprimirOrdenesProcedimiento;
	
	
	/**
	 * mapa con informacion de procedimientos
	 */
	private HashMap ordenesProcedimientosProcedimientos;
	
	
	/**
	 * Si se debe imprimir el sorporte respiratorio
	 */
	private boolean imprimirSoporteRespiratorio;
	
	/**
	 * Mapa con la informacion del soporte respiratorio
	 */
	private HashMap soporteRespiratorio;
	
	/**
	 * Si se debe imprimir la sección del control de líquidos
	 */
	private boolean imprimirControlLiquidos;
	
	/**
	 * Mapa con la informacion de control de líquidos
	 */
	private HashMap controlLiquidos;
	
	
	
	/**
	 * Mapa con los datos a mostrar de ordenes medicamentos 
	 */
	private HashMap ordemesMedicamentosInsumos;
	
	/**
	 * Mapa con los datos a mostrar de ordenes cirugias 
	 */
	private HashMap ordemesCirugias;
	
	/**
	 * Mapa con los datos a mostrar de ordenes interconsulta
	 */
	private HashMap ordenesInterconsulta;
	
	
	/**
	 * Si se debe imprimir la sección de notas de enfermeria
	 */
	private boolean imprimirNotasEnfermeria;
	
	
	/**
	 * Collection con la información de las notas de enfermeria 
	 */
	private Collection notasEnfermeria;
	
	
	/**
	 *Mapa con la informacion del encabezdo del paciente 
	 */
	private HashMap encabezadoPaciente;
	
	/**
	 * Atributo que define si se imrpime o no la sección de Cateteres y Sondas
	 */
	private boolean imprimirCateteresSondas;
	
	/**
	 * Atributo que representa la collection de aolumans dinamicas
	 * 
	 */
	private Collection columnasDinamicasCateteresSondas;
	
	/**
	 * Atributo que representa los datos de la sección de Cateteres y Sondas
	 */
	private HashMap cateteresSondas;
	
	/**
	 * Atributo que define si se imrpime o no la sección de Cuidados Especiales
	 */
	private boolean imprimirCuidadosEspeciales;
	
	/**
	 * Atributo que representa los datos de la sección de Cuidades Especiales
	 */
	private HashMap cuidadosEspeciales;
	
	/**
	 *Datos de ordenes medicas 
	 */
	private HashMap ordenesMedicas;

	/**
	 * Si se debe imprimir las ordenes ambulatorias
	 */
	private boolean imprimirOrdenaAmbulatorias;
	
	/**
	 * Mapa con la información de las ordenes ambulatorias
	 */
	private HashMap ordenesAmbulatorias;
	
	/**
	 * Fecha de egreso del paciente 
	 */
	private String fechaEgreso;
	
	/**
	 * Codigo de vía de Ingreso del paciente
	 */
	private String codigoViaIngreso;
	
	/**
	 * Via de ingreso del paciente
	 */
	private String viaEgreso;
	
	/**
	 * Si se deben imprimir antecedentes 
	 */
	private Boolean imprimirAntecedentes;
	
	
	/**
	 *atributo que sirve para validar si se imprime o no la informacion de la cirugia 
	 */
	private Boolean imprimirInformacionCirugia;
	
	/**
	 * Mapa con la informacion de los pacientes
	 */
	private HashMap antecedentes;
	
	/**
	 *Indica si se selecciono para imprimir 
	 */
	private Boolean hayResumenParcial;
	
	/**
	 * Mapa con datos de resumen parcial de HC 
	 */
	private HashMap resumenParcialHc;
	
	
	/**
	 * Indica si se imprimen las notas generales de cirugia 
	 */
	private Boolean imprimirNotasGeneralesCirugia;
	
	/**
	 * Indica si se emprimen las notas de recuperacion
	 */
	private Boolean imprimirNotasRecuperacion;
	
	
	
	/**
	 * Indica si se va a imprimir la seccion de notas aclaratorias
	 */
	private Boolean imprimirNotasAclaratorias;
	
	private List<DtoNotaAclaratoria> listaNotasAclaratorias;

	/**
	 * Constructor de clase 
	 */
	public DtoImpresionHistoriaClinica()
	{
		//Se inicializan los atributos del DTO
		this.idIngreso="";
		this.cuenta="";
		this.cuentaAsociada="";
		this.resultadoSeccionEvolucion=new ArrayList<DtoResultadoImpresionHistoriaClinica>();
		this.imprimirSignosVitales=false;
		this.signosVitales=new DtoSignosVitalesHC();
		this.imprimirValoracionesEnfermeria=false;
		this.valoracionesEnfermeria=new HashMap();
		this.imprimirAdminMedicamentos=false;
		this.imprimirConsumosInsumos=false;
		this.imprimirOrdenesProcedimiento=false;
		this.adminMedicamente= new HashMap();
		this.consumosInsumos= new HashMap();
		this.ordenesProcedimientosProcedimientos= new HashMap();
		this.imprimirSoporteRespiratorio=false;
		this.soporteRespiratorio=new HashMap();
		this.imprimirControlLiquidos=false;
		this.controlLiquidos=new HashMap();
		this.ordemesMedicamentosInsumos=new HashMap();
		this.ordemesCirugias=new HashMap();
		this.ordenesInterconsulta = new HashMap();
		this.interpretacionRespuesta = new HashMap();
		this.imprimirInterpretacionRespuesta=false;
		this.notasEnfermeria=new ArrayList();
		this.imprimirNotasEnfermeria=false;
		this.imprimirOrdenaAmbulatorias=false;
		this.ordenesAmbulatorias=new HashMap();
		this.encabezadoPaciente= new HashMap();
		this.imprimirCateteresSondas=false;
		this.columnasDinamicasCateteresSondas=new ArrayList();
		this.cateteresSondas= new HashMap();
		this.imprimirCuidadosEspeciales=false;
		this.cuidadosEspeciales= new HashMap();
		this.ordenesMedicas = new HashMap();
		this.codigoViaIngreso="";
		this.fechaEgreso="";
		this.viaEgreso="";
		this.imprimirAntecedentes=false;
		this.antecedentes=new HashMap();
		this.imprimirInformacionCirugia=false;
		this.hayResumenParcial=false;
		this.resumenParcialHc=new HashMap();
		this.imprimirNotasGeneralesCirugia=false;
		this.imprimirNotasRecuperacion=false;
		this.imprimirNotasAclaratorias=false;
		this.listaNotasAclaratorias=new ArrayList<DtoNotaAclaratoria>();
	}


	
	
	
	
	
	/**
	 * @return the imprimirNotasEnfermeria
	 */
	public boolean isImprimirNotasEnfermeria() {
		return imprimirNotasEnfermeria;
	}







	/**
	 * @param imprimirNotasEnfermeria the imprimirNotasEnfermeria to set
	 */
	public void setImprimirNotasEnfermeria(boolean imprimirNotasEnfermeria) {
		this.imprimirNotasEnfermeria = imprimirNotasEnfermeria;
	}







	/**
	 * @return the notasEnfermeria
	 */
	public Collection getNotasEnfermeria() {
		return notasEnfermeria;
	}



	/**
	 * @param notasEnfermeria the notasEnfermeria to set
	 */
	public void setNotasEnfermeria(Collection notasEnfermeria) {
		this.notasEnfermeria = notasEnfermeria;
	}




	/**
	 * @return the imprimirAdminMedicamentos
	 */
	public boolean isImprimirAdminMedicamentos() {
		return imprimirAdminMedicamentos;
	}


	/**
	 * @param imprimirAdminMedicamentos the imprimirAdminMedicamentos to set
	 */
	public void setImprimirAdminMedicamentos(boolean imprimirAdminMedicamentos) {
		this.imprimirAdminMedicamentos = imprimirAdminMedicamentos;
	}


	/**
	 * @return the adminMedicamente
	 */
	public HashMap getAdminMedicamente() {
		return adminMedicamente;
	}


	/**
	 * @param adminMedicamente the adminMedicamente to set
	 */
	public void setAdminMedicamente(HashMap adminMedicamente) {
		this.adminMedicamente = adminMedicamente;
	}
	
	/**
	 * @return Si se imprime los signos vitales
	 */
	public boolean isImprimirSignosVitales() {
		return imprimirSignosVitales;
	}


	/**
	 * @param imprimirSignosVitales
	 */
	public void setImprimirSignosVitales(boolean imprimirSignosVitales) {
		this.imprimirSignosVitales = imprimirSignosVitales;
	}


	/**
	 * @return informacion de los signos vitales a imprimir
	 */
	public DtoSignosVitalesHC getSignosVitales() {
		return signosVitales;
	}


	/**
	 * @param signosVitales
	 */
	public void setSignosVitales(DtoSignosVitalesHC signosVitales) {
		this.signosVitales = signosVitales;
	}


	/**
	 * @return Si se imprimen las valoraciones de enfermeria
	 */
	public boolean isImprimirValoracionesEnfermeria() {
		return imprimirValoracionesEnfermeria;
	}


	/**
	 * @param imprimirValoracionesEnfermeria
	 */
	public void setImprimirValoracionesEnfermeria(
			boolean imprimirValoracionesEnfermeria) {
		this.imprimirValoracionesEnfermeria = imprimirValoracionesEnfermeria;
	}


	/**
	 * @return Mapa con la informacion de valoraciones de enfermeria
	 */
	public HashMap getValoracionesEnfermeria() {
		return valoracionesEnfermeria;
	}


	/**
	 * @param valoracionesEnfermeria
	 */
	public void setValoracionesEnfermeria(HashMap valoracionesEnfermeria) {
		this.valoracionesEnfermeria = valoracionesEnfermeria;
	}

	
	/**
	 * @return the usuario
	 */
	public UsuarioBasico getUsuario() {
		return usuario;
	}


	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(UsuarioBasico usuario) {
		this.usuario = usuario;
	}


	/**
	 * @return the paciente
	 */
	public PersonaBasica getPaciente() {
		return paciente;
	}


	/**
	 * @param paciente the paciente to set
	 */
	public void setPaciente(PersonaBasica paciente) {
		this.paciente = paciente;
	}


	/**
	 * @return the imprimirAdminMedicamentos
	 */
	public Boolean getImprimirAdminMedicamentos() {
		return imprimirAdminMedicamentos;
	}


	/**
	 * @param imprimirAdminMedicamentos the imprimirAdminMedicamentos to set
	 */
	public void setImprimirAdminMedicamentos(Boolean imprimirAdminMedicamentos) {
		this.imprimirAdminMedicamentos = imprimirAdminMedicamentos;
	}


	/**
	 * @return the imprimirConsumosInsumos
	 */
	public Boolean getImprimirConsumosInsumos() {
		return imprimirConsumosInsumos;
	}


	/**
	 * @param imprimirConsumosInsumos the imprimirConsumosInsumos to set
	 */
	public void setImprimirConsumosInsumos(Boolean imprimirConsumosInsumos) {
		this.imprimirConsumosInsumos = imprimirConsumosInsumos;
	}


	/**
	 * @return the consumosInsumos
	 */
	public HashMap getConsumosInsumos() {
		return consumosInsumos;
	}


	/**
	 * @param consumosInsumos the consumosInsumos to set
	 */
	public void setConsumosInsumos(HashMap consumosInsumos) {
		this.consumosInsumos = consumosInsumos;
	}


	/**
	 * @return the imprimirOrdenesProcedimiento
	 */
	public Boolean getImprimirOrdenesProcedimiento() {
		return imprimirOrdenesProcedimiento;
	}


	/**
	 * @param imprimirOrdenesProcedimiento the imprimirOrdenesProcedimiento to set
	 */
	public void setImprimirOrdenesProcedimiento(Boolean imprimirOrdenesProcedimiento) {
		this.imprimirOrdenesProcedimiento = imprimirOrdenesProcedimiento;
	}


	/**
	 * @return the ordenesProcedimientosProcedimientos
	 */
	public HashMap getOrdenesProcedimientosProcedimientos() {
		return ordenesProcedimientosProcedimientos;
	}


	/**
	 * @param ordenesProcedimientosProcedimientos the ordenesProcedimientosProcedimientos to set
	 */
	public void setOrdenesProcedimientosProcedimientos(
			HashMap ordenesProcedimientosProcedimientos) {
		this.ordenesProcedimientosProcedimientos = ordenesProcedimientosProcedimientos;
	}

	

	/**
	 * @return si se imprime el soporte respiratorio
	 */
	public boolean isImprimirSoporteRespiratorio() {
		return imprimirSoporteRespiratorio;
	}


	/**
	 * @param imprimirSoporteRespiratorio
	 */
	public void setImprimirSoporteRespiratorio(boolean imprimirSoporteRespiratorio) {
		this.imprimirSoporteRespiratorio = imprimirSoporteRespiratorio;
	}


	/**
	 * @return mapa con la informacion de soporte respiratorio
	 */
	public HashMap getSoporteRespiratorio() {
		return soporteRespiratorio;
	}


	/**
	 * @param soporteRespiratorio
	 */
	public void setSoporteRespiratorio(HashMap soporteRespiratorio) {
		this.soporteRespiratorio = soporteRespiratorio;
	}


	/**
	 * @return Si se imprimen los controles de líquidos
	 */
	public boolean isImprimirControlLiquidos() {
		return imprimirControlLiquidos;
	}


	/**
	 * @param imprimirControlLiquidos
	 */
	public void setImprimirControlLiquidos(boolean imprimirControlLiquidos) {
		this.imprimirControlLiquidos = imprimirControlLiquidos;
	}


	/**
	 * @return Mapa con la informacion de controles de liquido 
	 */
	public HashMap getControlLiquidos() {
		return controlLiquidos;
	}


	/**
	 * @param controlLiquidos
	 */
	public void setControlLiquidos(HashMap controlLiquidos) {
		this.controlLiquidos = controlLiquidos;
	}



	/**
	 * @return numero de ingreso 
	 */
	public String getIdIngreso() {
		return idIngreso;
	}


	/**
	 * @param idIngreso
	 */
	public void setIdIngreso(String idIngreso) {
		this.idIngreso = idIngreso;
	}


	/**
	 * @return numero de cuenta 
	 */
	public String getCuenta() {
		return cuenta;
	}


	/**
	 * @param cuenta
	 */
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}


	/**
	 * @return cuenta asociada 
	 */
	public String getCuentaAsociada() {
		return cuentaAsociada;
	}


	/**
	 * @param cuentaAsociada
	 */
	public void setCuentaAsociada(String cuentaAsociada) {
		this.cuentaAsociada = cuentaAsociada;
	}

	/**
	 * @return the ordemesMedicamentosInsumos
	 */
	public HashMap getOrdemesMedicamentosInsumos() {
		return ordemesMedicamentosInsumos;
	}


	/**
	 * @param ordemesMedicamentosInsumos the ordemesMedicamentosInsumos to set
	 */
	public void setOrdemesMedicamentosInsumos(HashMap ordemesMedicamentosInsumos) {
		this.ordemesMedicamentosInsumos = ordemesMedicamentosInsumos;
	}



	/**
	 * @return the ordemesCirugias
	 */
	public HashMap getOrdemesCirugias() {
		return ordemesCirugias;
	}



	/**
	 * @param ordemesCirugias the ordemesCirugias to set
	 */
	public void setOrdemesCirugias(HashMap ordemesCirugias) {
		this.ordemesCirugias = ordemesCirugias;
	}


	/**
	 * @return the ordenesInterconsulta
	 */
	public HashMap getOrdenesInterconsulta() {
		return ordenesInterconsulta;
	}


	/**
	 * @param ordenesInterconsulta the ordenesInterconsulta to set
	 */
	public void setOrdenesInterconsulta(HashMap ordenesInterconsulta) {
		this.ordenesInterconsulta = ordenesInterconsulta;
	}


	/**
	 * @return the interpretacionRespuesta
	 */
	public HashMap getInterpretacionRespuesta() {
		return interpretacionRespuesta;
	}



	/**
	 * @param interpretacionRespuesta the interpretacionRespuesta to set
	 */
	public void setInterpretacionRespuesta(HashMap interpretacionRespuesta) {
		this.interpretacionRespuesta = interpretacionRespuesta;
	}


	/**
	 * @return the imprimirInterpretacionRespuesta
	 */
	public Boolean getImprimirInterpretacionRespuesta() {
		return imprimirInterpretacionRespuesta;
	}


	/**
	 * @param imprimirInterpretacionRespuesta the imprimirInterpretacionRespuesta to set
	 */
	public void setImprimirInterpretacionRespuesta(
			Boolean imprimirInterpretacionRespuesta) {
		this.imprimirInterpretacionRespuesta = imprimirInterpretacionRespuesta;
	}


	/**
	 * @return the encabezadoPaciente
	 */
	public HashMap getEncabezadoPaciente() {
		return encabezadoPaciente;
	}



	/**
	 * @param encabezadoPaciente the encabezadoPaciente to set
	 */
	public void setEncabezadoPaciente(HashMap encabezadoPaciente) {
		this.encabezadoPaciente = encabezadoPaciente;
	}


	/**
	 * @return the imprimirCateteresSondas
	 */
	public boolean isImprimirCateteresSondas() {
		return imprimirCateteresSondas;
	}



	/**
	 * @param imprimirCateteresSondas the imprimirCateteresSondas to set
	 */
	public void setImprimirCateteresSondas(boolean imprimirCateteresSondas) {
		this.imprimirCateteresSondas = imprimirCateteresSondas;
	}



	/**
	 * @return the columnasDinamicasCateteresSondas
	 */
	public Collection getColumnasDinamicasCateteresSondas() {
		return columnasDinamicasCateteresSondas;
	}



	/**
	 * @param columnasDinamicasCateteresSondas the columnasDinamicasCateteresSondas to set
	 */
	public void setColumnasDinamicasCateteresSondas(
			Collection columnasDinamicasCateteresSondas) {
		this.columnasDinamicasCateteresSondas = columnasDinamicasCateteresSondas;
	}


	/**
	 * @return the cateteresSondas
	 */
	public HashMap getCateteresSondas() {
		return cateteresSondas;
	}



	/**
	 * @param cateteresSondas the cateteresSondas to set
	 */
	public void setCateteresSondas(HashMap cateteresSondas) {
		this.cateteresSondas = cateteresSondas;
	}




	/**
	 * @return the imprimirCuidadosEspeciales
	 */
	public boolean isImprimirCuidadosEspeciales() {
		return imprimirCuidadosEspeciales;
	}




	/**
	 * @param imprimirCuidadosEspeciales the imprimirCuidadosEspeciales to set
	 */
	public void setImprimirCuidadosEspeciales(boolean imprimirCuidadosEspeciales) {
		this.imprimirCuidadosEspeciales = imprimirCuidadosEspeciales;
	}




	/**
	 * @return the cuidadosEspeciales
	 */
	public HashMap getCuidadosEspeciales() {
		return cuidadosEspeciales;
	}



	/**
	 * @param cuidadosEspeciales the cuidadosEspeciales to set
	 */
	public void setCuidadosEspeciales(HashMap cuidadosEspeciales) {
		this.cuidadosEspeciales = cuidadosEspeciales;
	}



	/**
	 * @return the ordenesMedicas
	 */
	public HashMap getOrdenesMedicas() {
		return ordenesMedicas;
	}



	/**
	 * @param ordenesMedicas the ordenesMedicas to set
	 */
	public void setOrdenesMedicas(HashMap ordenesMedicas) {
		this.ordenesMedicas = ordenesMedicas;
	}




	/**
	 * @return Si se imprimen ordenes ambulatorias
	 */
	public boolean isImprimirOrdenaAmbulatorias() {
		return imprimirOrdenaAmbulatorias;
	}







	/**
	 * @param imprimirOrdenaAmbulatorias
	 */
	public void setImprimirOrdenaAmbulatorias(boolean imprimirOrdenaAmbulatorias) {
		this.imprimirOrdenaAmbulatorias = imprimirOrdenaAmbulatorias;
	}







	/**
	 * @return mapa con la informacion de ordenes ambulatorias 
	 */
	public HashMap getOrdenesAmbulatorias() {
		return ordenesAmbulatorias;
	}







	/**
	 * @param ordenesAmbulatorias
	 */
	public void setOrdenesAmbulatorias(HashMap ordenesAmbulatorias) {
		this.ordenesAmbulatorias = ordenesAmbulatorias;
	}







	/**
	 * @return the codigoViaIngreso
	 */
	public String getCodigoViaIngreso() {
		return codigoViaIngreso;
	}







	/**
	 * @param codigoViaIngreso the codigoViaIngreso to set
	 */
	public void setCodigoViaIngreso(String codigoViaIngreso) {
		this.codigoViaIngreso = codigoViaIngreso;
	}







	/**
	 * @return the fechaEgreso
	 */
	public String getFechaEgreso() {
		return fechaEgreso;
	}







	/**
	 * @param fechaEgreso the fechaEgreso to set
	 */
	public void setFechaEgreso(String fechaEgreso) {
		this.fechaEgreso = fechaEgreso;
	}







	/**
	 * @return the viaEgreso
	 */
	public String getViaEgreso() {
		return viaEgreso;
	}







	/**
	 * @param viaEgreso the viaEgreso to set
	 */
	public void setViaEgreso(String viaEgreso) {
		this.viaEgreso = viaEgreso;
	}







	/**
	 * @return the imprimirAntecedentes
	 */
	public Boolean getImprimirAntecedentes() {
		return imprimirAntecedentes;
	}







	/**
	 * @param imprimirAntecedentes the imprimirAntecedentes to set
	 */
	public void setImprimirAntecedentes(Boolean imprimirAntecedentes) {
		this.imprimirAntecedentes = imprimirAntecedentes;
	}







	/**
	 * @return the antecedentes
	 */
	public HashMap getAntecedentes() {
		return antecedentes;
	}







	/**
	 * @param antecedentes the antecedentes to set
	 */
	public void setAntecedentes(HashMap antecedentes) {
		this.antecedentes = antecedentes;
	}





	/**
	 * @return Lsta con los resultados de evolucion
	 */
	public ArrayList<DtoResultadoImpresionHistoriaClinica> getResultadoSeccionEvolucion() {
		return resultadoSeccionEvolucion;
	}







	/**
	 * @param resultadoSeccionEvolucion
	 */
	public void setResultadoSeccionEvolucion(
			ArrayList<DtoResultadoImpresionHistoriaClinica> resultadoSeccionEvolucion) {
		this.resultadoSeccionEvolucion = resultadoSeccionEvolucion;
	}







	/**
	 * @return the imprimirInformacionCirugia
	 */
	public Boolean getImprimirInformacionCirugia() {
		return imprimirInformacionCirugia;
	}







	/**
	 * @param imprimirInformacionCirugia the imprimirInformacionCirugia to set
	 */
	public void setImprimirInformacionCirugia(Boolean imprimirInformacionCirugia) {
		this.imprimirInformacionCirugia = imprimirInformacionCirugia;
	}







	/**
	 * @return the hayResumenParcial
	 */
	public Boolean getHayResumenParcial() {
		return hayResumenParcial;
	}







	/**
	 * @param hayResumenParcial the hayResumenParcial to set
	 */
	public void setHayResumenParcial(Boolean hayResumenParcial) {
		this.hayResumenParcial = hayResumenParcial;
	}







	/**
	 * @return the resumenParcialHc
	 */
	public HashMap getResumenParcialHc() {
		return resumenParcialHc;
	}







	/**
	 * @param resumenParcialHc the resumenParcialHc to set
	 */
	public void setResumenParcialHc(HashMap resumenParcialHc) {
		this.resumenParcialHc = resumenParcialHc;
	}







	/**
	 * @return the imprimirNotasGeneralesCirugia
	 */
	public Boolean getImprimirNotasGeneralesCirugia() {
		return imprimirNotasGeneralesCirugia;
	}







	/**
	 * @param imprimirNotasGeneralesCirugia the imprimirNotasGeneralesCirugia to set
	 */
	public void setImprimirNotasGeneralesCirugia(
			Boolean imprimirNotasGeneralesCirugia) {
		this.imprimirNotasGeneralesCirugia = imprimirNotasGeneralesCirugia;
	}







	/**
	 * @return the imprimirNotasRecuperacion
	 */
	public Boolean getImprimirNotasRecuperacion() {
		return imprimirNotasRecuperacion;
	}







	/**
	 * @param imprimirNotasRecuperacion the imprimirNotasRecuperacion to set
	 */
	public void setImprimirNotasRecuperacion(Boolean imprimirNotasRecuperacion) {
		this.imprimirNotasRecuperacion = imprimirNotasRecuperacion;
	}







	/**
	 * @return the imprimirNotasAclaratorias
	 */
	public Boolean getImprimirNotasAclaratorias() {
		return imprimirNotasAclaratorias;
	}







	/**
	 * @param imprimirNotasAclaratorias the imprimirNotasAclaratorias to set
	 */
	public void setImprimirNotasAclaratorias(Boolean imprimirNotasAclaratorias) {
		this.imprimirNotasAclaratorias = imprimirNotasAclaratorias;
	}







	/**
	 * @return the listaNotasAclaratorias
	 */
	public List<DtoNotaAclaratoria> getListaNotasAclaratorias() {
		return listaNotasAclaratorias;
	}







	/**
	 * @param listaNotasAclaratorias the listaNotasAclaratorias to set
	 */
	public void setListaNotasAclaratorias(
			List<DtoNotaAclaratoria> listaNotasAclaratorias) {
		this.listaNotasAclaratorias = listaNotasAclaratorias;
	}

	

}

