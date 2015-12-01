package com.princetonsa.actionform.historiaClinica;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.InfoDatos;

import com.princetonsa.dto.historiaClinica.DtoIngresoHistoriaClinica;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoComponente;
import com.princetonsa.dto.odontologia.DtoAntecendenteOdontologico;
import com.princetonsa.mundo.antecedentes.AntecedentePediatrico;
import com.servinte.axioma.dto.historiaClinica.InfoIngresoDto;

public class ImpresionResumenAtencionesForm extends ValidatorForm implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4391282751604033706L;
	

	/**
	 * 
	 */
	private String estado;
	
	/**
	 * 
	 */
	private String idIngreso="";
	
	
	/**
	 * 
	 */
	private HashMap adminMedicamentos;
	
	/**
	 * 
	 */
	private HashMap insumos;
	
	/**
	 * 
	 */
	private String atenciones="";
	
	private boolean imprimirVIUrgencias;
	private boolean imprimirVIHospitalizacion;

	
	/**
	 * 
	 */
	private HashMap resultadoLaboratorios;
	
	/**
	 * 
	 */
	private HashMap valoracionesEnfermeria;
	

	
	/**
	 * 
	 */
	private HashMap repuestaInterpretacionProcedimientos;
	
	private HashMap ordenesMedicas;
	private HashMap ordenesCirugia;
	private HashMap ordenesMedicamentos;
	private HashMap ordenesProcedimientos;
	private HashMap ordenesInterconsulta;
	private HashMap ordenesAmbulatorias;
	
	/**
	 * reporte certificado de atencion medica
	 */
	private HashMap registroAccidentesTransitoReporte1;
	
	/**
	 * reporte certificado fusoat
	 */
	private HashMap registroAccidentesTransitoReporte2;
	
	private HashMap solicitudesInterConsulta;
	
	
	private HashMap codigosEvoluciones;
	
	/**
	 * 
	 */
	private HashMap encabezadoImpresion;
	
	/**
	 * 
	 */
	private HashMap soporteRespiratorio;
	
	/**
	 * 
	 */
	private HashMap eventosAdversos;
	
	/**
	 * 
	 */
	private String fechaInicial = "";
	
	/**
	 * 
	 */
	private String fechaFinal = "";
	
	/**
	 * 
	 */
	private String horaInicial = "";
	
	/**
	 * 
	 */
	private String horaFinal = "";
	
	
	/**
	 * U  --> URGENCIAS
	 * H  --> HOSPITALIZACION
	 * A  --> AMBOS
	 * "" --> NO EXISTE ASOCIO
	 */
	private String filtroAsocio = "";
	
	/**
	 * 
	 */
	private String cuenta = "";
	
	private HashMap secciones;
	
	
	
	private HashMap resumenParcialHistoriaClinica;
	
	private HashMap<String,Object> valoracionesCuidadoEspecial;
	
	//--------------------------------------------MANIZALES --------------------------------------//
	
	//----------------------ANOTACIONES DE ENFERMER�A--------------------------------------//
	 /**
     * Collection que guarda el listado de las anotaciones de enfermer�a
     * ingresadas en el registro de enfermer�a
     */
    private Collection historicoAnotacionesEnfermeria;
    
    //---------------------------SIGNOS VITALES-----------------------------------------------//
    /**
     * Coleccion para traer el Listado de tipos Signos Vitales parametrizados en enfermer�a
     * por centro de costo instituci�n    
     */
    private Collection signosVitalesInstitucionCcosto;
    
    /**
     * Coleccion para traer el listado hist�rico de los signos vitales fijos de la secci�n
     * de acuerdo a los par�metros de b�squeda
     */
    private Collection signosVitalesFijosHisto;
    
    /**
     * Collecci�n para traer el listado hist�rico de los signos vitales parametrizados por 
     * instituci�n centro de costo de acuerdo a los par�metros de b�squeda  
     */
    private Collection signosVitalesParamHisto;
    
    /**
     * Collecci�n para traer el listado con los c�digos hist�ricos, fecha registro y hora registro,
     * de los signos vitales fijos y parametrizados  
     */
    private Collection signosVitalesHistoTodos;
    
    //-----------------------------SECCION CATETER SONDA-------------------------------------------------//
    /**
     * Coleccion para traer el Listado de las columnas de cat�teres y sonda parametrizados en enfermer�a
     * por centro de costo instituci�n    
     */
    private Collection colCateteresSondaInstitucionCcosto;
    
    /**
     * Coleccion para traer el listado hist�rico de los cateter sonda fijos de la secci�n
    */
    private Collection cateterSondaFijosHisto;
    
    /**
     * Collecci�n para traer el listado hist�rico de los cateter sonda parametrizados por 
     * instituci�n centro de costo  
     */
    private Collection cateterSondaParamHisto;
    
    /**
     * Collecci�n para traer el listado con los c�digos hist�ricos, fecha registro y hora registro, usuario
     * de los cat�teres sonda fijos y parametrizados  
     */
    private Collection cateterSondaHistoTodos;
    
    /**
     * Mapa para guardar la informaci�n hist�rica de los cateteres sonda tanto fijos como
     * parametrizados ordenados por fecha inserci�n
     */
    private HashMap mapaHistoricoCateterSonda;
    
    //  -----------------------------SECCION CUIDADOS ESPECIALES DE ENFERMER�A-------------------------------------------------// 
    
    /**
     * Mapa que contiene el hist�rico de cuidados especiales de enfermer�a para
     * ser mostrado en la impresi�n de historia cl�nica
     */
    private HashMap mapaHistoricoCuidadosEspeciales;
    
    //-----------------------------SUB-SECCION ESPECIFICACIONES GLASGOW-------------------------------------------------//
    /**
     * Coleccion para traer el Listado de las especificaciones glasgow parametrizados en enfermer�a
     * por centro de costo instituci�n    
     */
    private Collection escalasGlasgowInstitucionCCosto;
    
    /**
     * Mapa que contiene el hist�rico de escala glasgow para
     * ser mostrado en la impresi�n de historia cl�nica
     */
    private HashMap mapaHistoricoEscalaGlasgow;
    
    //  -----------------------------SUB-SECCION PUPILAS-------------------------------------------------//
    /**
     * Mapa que contiene el hist�rico de pupilas para
     * ser mostrado en la impresi�n de historia cl�nica
     */
    private HashMap mapaHistoricoPupilas;
    
    //  -----------------------------SUB-SECCION CONVULSIONES-------------------------------------------------//
    /**
     * Mapa que contiene el hist�rico de convulsiones para
     * ser mostrado en la impresi�n de historia cl�nica
     */
    private HashMap mapaHistoricoConvulsiones;
    
    //-----------------------------SUB-SECCION CONTROL ESFINTERES-------------------------------------------------//
    /**
     * Mapa que contiene el hist�rico de control de esfinteres para
     * ser mostrado en la impresi�n de historia cl�nica
     */
    private HashMap mapaHistoricoControlEsfinteres;
    
    //-----------------------------SUB-SECCION FUERZA MUSCULAR-------------------------------------------------//
    /**
     * Mapa que contiene el hist�rico de fuerza muscular para
     * ser mostrado en la impresi�n de historia cl�nica
     */
    private HashMap mapaHistoricoFuerzaMuscular;
    
    //------------------------------ SECCION CIRUG�AS ------------------------------------------//
    /**
     * Se consultan los codigo de las peticiones de cirug�a,de acuerdo a los
     * par�metros de b�squeda en la impresi�n de la historia cl�nica 
     */
    private HashMap mapaCodigosPeticionCirugia;
    
    //---------------------------- SUB-SECCI�N ENCABEZADOS HOJA ANESTESIA ------------------------//
    /**
     * Se consultan los encabezados de la hoja de anestesia, de acuerdo
     * a la lista de peticiones que cumplieron con los criterios de b�squeda 
     */
    private HashMap mapaEncabezadosHojaAnestesia;
    
    //---------------------------- SUB-SECCI�N EXAMENES LABORATORIO PREANESTESIA ------------------------//
    /**
     * Se consultan los ex�menes de laboratorio de la preanestesia, de acuerdo
     * a la lista de peticiones que cumplieron con los criterios de b�squeda 
     */
    private HashMap mapaExamenesLaboratorioPreanestesia;
    
    //---------------------------- SUB-SECCI�N EXAMENES FISICOS ------------------------//
    /**
     * Se consultan el hist�rico de los ex�menes f�sicos de tipo text, de acuerdo
     * a la lista de peticiones que cumplieron con los criterios de b�squeda 
     */
    private HashMap mapaHistoExamenesFisicosText;
    
    /**
     * Se consultan el hist�rico de los ex�menes f�sicos de tipo text area, de acuerdo
     * a la lista de peticiones que cumplieron con los criterios de b�squeda 
     */
    private HashMap mapaHistoExamenesFisicosTextArea;
    
    //---------------------------- SUB-SECCI�N CONCLUSIONES ------------------------//
    /**
     * Se consultan el hist�rico de las conclusiones de preanestesia, de acuerdo
     * a la lista de peticiones que cumplieron con los criterios de b�squeda 
     */
    private HashMap mapaHistoConclusiones;
    
   //---------------------------- SUB-SECCI?N TECNICA DE ANESTESIA ----------------------//
	/**
	 * Colecci?n para traer los tipos de t?cnicas de anestesia general que tienen opciones para la instituci?n
	 */
	private Collection listadoTecAnestesiaOpcionesGral;
	
	/**
	 * Colecci?n para traer los tipos de t?cnicas de anestesia general sin opciones para la instituci?n
	 */
	private Collection listadoTecAnestesiaGral;
	
	/**
	 * Colecci?n para traer los tipos de t?cnicas de anestesia regional para la instituci?n
	 */
	private Collection listadoTecAnestesiaRegional;
	
	/**
	 * Mapa para almacenar el hist?rico de la secci?n t?cnica de anestesia
	 */
	private HashMap mapaHistoTecAnestesia = new HashMap();
	
	//---------------------------- SUB-SECCI?N SIGNOS VITALES HOJA ANESTESIA ----------------------//
	/**
	 * Colecci?n para traer los tipos de signos vitales de la hoja de anetesia
	 */
	private Collection listadoSignosVitales;
	
	/**
	 * Mapa para almacenar el hist?rico de la secci?n signos vitales
	 * de la hoja de anestesia
	 */
	private HashMap mapaHistoSignosVitales = new HashMap();
        
    //---------------------------- SUB-SECCI�N BALANCE DE LIQUIDOS HOJA ANESTESIA ------------------------//
    /**
     * Se consultan el hist�rico de balance de l�quidos, de acuerdo
     * a la lista de solicitudes que cumplieron con los criterios de b�squeda 
     */
    private HashMap mapaHistoBalanceLiquidosHojaAnestesia;
    
    //---------------------------- SUB-SECCI�N MEDICAMENTOS HOJA ANESTESIA ------------------------//
    /**
     * Se consultan el hist�rico de medicamentos, de acuerdo
     * a la lista de solicitudes que cumplieron con los criterios de b�squeda 
     */
    private HashMap mapaHistoMedicamentosHojaAnestesia;
    
    
    
    
	
	//--------------------------------------------------------------------------------------------//
    /**
     * Mapa para traer la informacion de control de liquidos desde registro de enfermeria.
     */
	private HashMap mapaControlLiq;

	/**
     * Mapa para traer la informacion de antecedentes de alergias
     */
	private HashMap mapaAntAlergia;

	/**
     * Mapa para traer la informacion de antecedentes de familiares
     */
	private HashMap mapaAntFamiliares;

	/**
     * Mapa para traer la informacion de antecedentes de familiares Oculares
     */
	private HashMap mapaAntFamOftal;

	/**
     * Mapa para traer la informacion de antecedentes de familiares Oculares
     */
	private HashMap mapaAntPersoOftal;

	/**
     * Mapa para traer la informacion de antecedentes de Gineco Obstetricos.
     */
	private HashMap mapaAntGineco;
	
	/**
	 * Hist?rico de la duraci?n de la menstruacion para Antecedentes Gineco Obstetricos  
	 */
	private ArrayList historicos;
	
	/**
	 * 
	 *
	 */
	
	/**
	 * Mapa Para Antecedentes de Medicamentos.
	 */
	private HashMap mapaAntMedicamento;
	
	/**
	 * Mapa Para Antecedentes de Medicos y QuirurGicos.
	 */
	private HashMap mapaAntMedicos;

	/**
	 * Mapa Para Antecedentes Toxicos
	 */
	private HashMap mapaAntToxicos;
	
	/**
	 * Mapa Para Antecedentes Toxicos
	 */
	private HashMap mapaAntTransfusionales;

	/**
	 * Mapa Para Antecedentes Toxicos
	 */
	private HashMap mapaVacunas;
	
	/**
	 *  Para la informacion de vacunas.
	 */
	private HashMap mapaTiposInmunizacion;
	
	/**
	 * Para Antecedentes Varios.
	 */
	private HashMap mapaAntOtros;
	
	/**
	 * Para Cargar Antecedentes Pediatricos.
	 */
	private HashMap mapaAntPediatricos;
	private AntecedentePediatrico antPed =  new AntecedentePediatrico();
	private ArrayList categoriaEmbarazoOpcionCampo = new ArrayList();
	private HashMap tiposParto = new HashMap();
	private HashMap tiposPartoCarga = new HashMap();
	private HashMap motivosTiposParto = new HashMap();
	private HashMap motivosTiposPartoCarga = new HashMap();
	private ArrayList tiposPartoList = new ArrayList();
	private ArrayList inmunizacionesList = new ArrayList(); 
	
	/**
	 * Para Cargar Antecedentes Odontologicos
	 */
	private HashMap mapaAntOdonto;

	/**
	 * Para Cargar Toda la informacion de Hoja Quir�rgica 
	 */
	private HashMap mapaHojaQuirur;
	
	/**
	 * Para Cargar Toda la informacion de Notas de Enfermeria. 
	 */
	private HashMap mapaNotasEnfer;
	
	/**
	 * Para Cargar Toda la informacion de Notas de Recuperacion 
	 */
	private HashMap mapaNotasRecuperacion;
	
	/**
	 * 
	 */
	private HashMap mapaEnlacesValoracionCE;
	
	/**
	 * 
	 */
	private HashMap infoInstitucion;
	
	/**
	 * Guarda los numerso de las solicitudes asociadas a una factura separados por comas
	 */
	private String solicitudesFactura;
	
	/**
	 * 
	 */
	private HashMap mapaConsultasPYP;
	
//************************************************************************	
	
	private ArrayList<DtoAntecendenteOdontologico> arrayAntecedentesActuales;
    private DtoAntecendenteOdontologico ultimoAntecedente;
	private String datosGeneralesUltimoAnt;
	private DtoComponente componenteAntOdo;
	
//*************************************************************************	
	
	/**
	 * 
	 */
	private String viaIngreso = "";
	
	/**
	 *mapa con informaciond e notas de recuperacion  
	 */
	private HashMap informacionNotasRecuperacion;
	
	/**
	 * 
	 */
	private String tipoImpresion="";
	
	private String listaIngresosSeleccionados="";
	
	/**
	 *nombre archivo generado 
	 */
	private String nombreArchivoGenerado;
	
	/**
	 * Para registrar informacion relevante del ingreso seleccionado
	 */
	private InfoIngresoDto ingresoSelecccionado;
		
	
	/**
	 *Listado de ingresos seleccionados 
	 */
	private List<DtoIngresoHistoriaClinica> listaDtoHc;
	
	/**
	 * Atributo que almacena si existe busqueda x algun criterio
	 */
	private boolean esBusquedaResumen;
	

	/**
	 * Atributo que almacena el tiempo Resumen HC paciente
	 */
	private String codigoManual;
	
	/**
	 * Atributo que almacena el tipo de impresion Resumen HC paciente
	 */
	private String impresionResumenHC;
	
	/**
	 * Atributo que almacena isImpresionPop
	 */
	
	private boolean isImpresionPop;
	
	public void reset()
	{
		this.ingresoSelecccionado=null;
		this.resultadoLaboratorios=new HashMap();
		this.valoracionesEnfermeria=new HashMap();
		this.resultadoLaboratorios.put("numRegistros", "0");
		this.valoracionesEnfermeria.put("numRegistros", "0");

		this.imprimirVIUrgencias=false;
		this.imprimirVIHospitalizacion=false;
		
		this.secciones=new HashMap();
		
		this.resumenParcialHistoriaClinica=new HashMap();
		this.resumenParcialHistoriaClinica.put("numRegistros", 0);
		
		this.valoracionesCuidadoEspecial=new HashMap();
		this.valoracionesCuidadoEspecial.put("numRegistros", 0);
		
		this.adminMedicamentos=new HashMap();
		this.adminMedicamentos.put("numRegistros", "0");

		this.insumos=new HashMap();
		this.insumos.put("numRegistros", "0");
		
		this.ordenesMedicas=new HashMap();
		this.ordenesMedicas.put("numRegistros", "0");
		
		this.ordenesCirugia=new HashMap();
		this.ordenesCirugia.put("numRegistros", "0");

		this.ordenesMedicamentos=new HashMap();
		this.ordenesMedicamentos.put("numRegistros", "0");

		this.ordenesProcedimientos=new HashMap();
		this.ordenesProcedimientos.put("numRegistros", "0");

		this.ordenesInterconsulta=new HashMap();
		this.ordenesInterconsulta.put("numRegistros", "0");
		
		this.ordenesAmbulatorias=new HashMap();
		this.ordenesAmbulatorias.put("numRegistros", "0");
		
		this.repuestaInterpretacionProcedimientos=new HashMap();
		this.repuestaInterpretacionProcedimientos.put("numRegistros", "0");
	
		this.registroAccidentesTransitoReporte1= new HashMap();
		this.registroAccidentesTransitoReporte2= new HashMap();

		this.solicitudesInterConsulta=new HashMap();
		this.solicitudesInterConsulta.put("numRegistros", "0");

		this.codigosEvoluciones= new HashMap();
		this.codigosEvoluciones.put("numRegistros", "0");
		
		this.encabezadoImpresion= new HashMap();
		this.encabezadoImpresion.put("numRegistros", "0");
		
		this.soporteRespiratorio=new HashMap();
		this.soporteRespiratorio.put("numRegistros", "0");
		
		
		//------------------ANOTACIONES ENFERMER�A----------------------------//
		this.historicoAnotacionesEnfermeria=new ArrayList();
		
		//------------------SIGNOS VITALES-----------------------------------//
		this.signosVitalesInstitucionCcosto=new ArrayList();
		this.signosVitalesFijosHisto=new ArrayList();
		this.signosVitalesParamHisto=new ArrayList();
		this.signosVitalesHistoTodos=new ArrayList();
		
		//----------------- CATETER SONDA -----------------------//
		this.mapaHistoricoCateterSonda = new HashMap();
		this.colCateteresSondaInstitucionCcosto=new ArrayList();
		this.cateterSondaFijosHisto=new ArrayList();
		this.cateterSondaParamHisto=new ArrayList();
		this.cateterSondaHistoTodos=new ArrayList();
		
		//------------------CUIDADOS ESPECIALES DE ENFERMER�A----------//
		this.mapaHistoricoCuidadosEspeciales=new HashMap();
		this.mapaHistoricoCuidadosEspeciales.put("numRegistros","0");
		
		//------------------ESCALA GLASGOW --------------------------//
		this.escalasGlasgowInstitucionCCosto=new ArrayList();
		this.mapaHistoricoEscalaGlasgow=new HashMap();
		this.mapaHistoricoEscalaGlasgow.put("numRegistros","0");
		
		//------------------PUPILAS --------------------------//
		this.mapaHistoricoPupilas=new HashMap();
		this.mapaHistoricoPupilas.put("numRegistros","0");
		
		//----------------- CONVULSIONES ------------------------//
		this.mapaHistoricoConvulsiones=new HashMap();
		this.mapaHistoricoConvulsiones.put("numRegistros","0");
		
		//----------------- CONTROL DE ESFINTERES ------------------------//
		this.mapaHistoricoControlEsfinteres=new HashMap();
		this.mapaHistoricoControlEsfinteres.put("numRegistros","0");
		
		//----------------- FUERZA MUSCULAR ------------------------//
		this.mapaHistoricoFuerzaMuscular=new HashMap();
		this.mapaHistoricoFuerzaMuscular.put("numRegistros","0");
		
		//------------------------ CIRUG�AS ------------------------------//
		this.mapaCodigosPeticionCirugia=new HashMap();
		this.mapaCodigosPeticionCirugia.put("numRegistros","0");
		
		//-------------------- SUB-SECCI�N ENCABEZADOS DE HOJA ANESTESIA -----------//
		this.mapaEncabezadosHojaAnestesia=new HashMap();
		this.mapaEncabezadosHojaAnestesia.put("numRegistros","0");
		
		//-------------------- SUB-SECCI�N EX�MENES LABORATORIO PREANESTESIA -----------//
		this.mapaExamenesLaboratorioPreanestesia=new HashMap();
		this.mapaExamenesLaboratorioPreanestesia.put("numRegistros","0");
		
		//-------------------- SUB-SECCI�N EX�MENES FISICOS -----------//
		this.mapaHistoExamenesFisicosText=new HashMap();
		this.mapaHistoExamenesFisicosText.put("numRegistros","0");
		this.mapaHistoExamenesFisicosTextArea=new HashMap();
		this.mapaHistoExamenesFisicosTextArea.put("numRegistros","0");
		
		//-------------------- SUB-SECCI�N CONCLUSIONES --------------------//
		this.mapaHistoConclusiones=new HashMap();
		this.mapaHistoConclusiones.put("numRegistros","0");
		
		//-------------------- SUB-SECCI�N BALANCE DE LIQUIDOS --------------------//
		this.mapaHistoBalanceLiquidosHojaAnestesia=new HashMap();
		this.mapaHistoBalanceLiquidosHojaAnestesia.put("numRegistros","0");
		
		//-------------------- SUB-SECCI�N MEDICAMENTOS --------------------//
		this.mapaHistoMedicamentosHojaAnestesia=new HashMap();
		this.mapaHistoMedicamentosHojaAnestesia.put("numRegistros","0");
		
		//-------------------- SUB-SECCI?N SIGNOS VITALES HOJA ANESTESIA --------------------//
		this.listadoSignosVitales=new ArrayList();
		this.mapaHistoSignosVitales=new HashMap();
		this.mapaHistoSignosVitales.put("numRegistrosTiempos","0");
		this.mapaHistoSignosVitales.put("numRegistrosValores","0");
		
				
		//---Manizales
		
		this.mapaControlLiq = new HashMap();
		this.mapaAntAlergia = new HashMap();
		this.mapaAntFamiliares = new HashMap();
		this.mapaAntFamOftal = new HashMap();
		this.mapaAntPersoOftal = new HashMap();
		this.mapaAntGineco = new HashMap();
		this.mapaAntMedicamento = new HashMap();	
		this.mapaAntMedicos = new HashMap();
		this.mapaAntToxicos = new HashMap();
		this.mapaAntTransfusionales = new HashMap();
		this.mapaVacunas = new HashMap();
		this.mapaTiposInmunizacion = new HashMap(); 
		this.mapaAntOtros = new HashMap();
		this.mapaAntPediatricos = new HashMap();
		antPed = new AntecedentePediatrico();	
		this.mapaAntOdonto = new HashMap();
		this.mapaHojaQuirur = new HashMap();
		this.mapaNotasEnfer = new HashMap();
		this.mapaNotasRecuperacion = new HashMap();
		
		
		this.categoriaEmbarazoOpcionCampo = new ArrayList();
		this.tiposParto = new HashMap();
		this.tiposPartoCarga = new HashMap();
		this.motivosTiposParto = new HashMap();
		this.motivosTiposPartoCarga = new HashMap();
		this.tiposPartoList = new ArrayList();
		this.inmunizacionesList = new ArrayList(); 
		
		this.mapaEnlacesValoracionCE= new HashMap();
		this.mapaEnlacesValoracionCE.put("numRegistros", "0");
		
		this.eventosAdversos= new HashMap();
		this.eventosAdversos.put("numRegistros", "0");
		
		this.solicitudesFactura="";
		this.infoInstitucion=new HashMap();
		

		this.arrayAntecedentesActuales= new ArrayList<DtoAntecendenteOdontologico>();
		this.ultimoAntecedente= new DtoAntecendenteOdontologico();
		this.datosGeneralesUltimoAnt = new String("");
		this.componenteAntOdo = new DtoComponente();
	
		this.mapaConsultasPYP = new HashMap();
		this.mapaConsultasPYP.put("numRegistros", "0");
		this.nombreArchivoGenerado="";
		this.informacionNotasRecuperacion=new HashMap();
		this.listaDtoHc = new ArrayList<DtoIngresoHistoriaClinica>();
	}
	
	/**
	 * 
	 *
	 */
	public void resetCriteriosBusqueda()
	{
		this.fechaInicial="";
		this.fechaFinal="";
		this.horaInicial="";
		this.horaFinal="";
		this.cuenta="";
		this.filtroAsocio="";
		this.idIngreso="";
		this.atenciones="";
		
		this.esBusquedaResumen = false;
	}
	
	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		return new ActionErrors();
	}

	public HashMap getAdminMedicamentos()
	{
		return adminMedicamentos;
	}

	public void setAdminMedicamentos(HashMap adminMedicamentos)
	{
		this.adminMedicamentos = adminMedicamentos;
	}

	public String getEstado()
	{
		return estado;
	}

	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	public String getIdIngreso()
	{
		return idIngreso;
	}

	public void setIdIngreso(String idIngreso)
	{
		this.idIngreso = idIngreso;
	}

	public HashMap getInsumos()
	{
		return insumos;
	}

	public void setInsumos(HashMap insumos)
	{
		this.insumos = insumos;
	}

	public HashMap getRepuestaInterpretacionProcedimientos()
	{
		return repuestaInterpretacionProcedimientos;
	}

	public void setRepuestaInterpretacionProcedimientos(
			HashMap repuestaInterpretacionProcedimientos)
	{
		this.repuestaInterpretacionProcedimientos = repuestaInterpretacionProcedimientos;
	}

	/**
	 * @return Returns the cuenta.
	 */
	public String getCuenta() {
		return cuenta;
	}

	/**
	 * @param cuenta The cuenta to set.
	 */
	public void setCuenta(String cuenta) {
		this.cuenta = cuenta;
	}

	/**
	 * @return Returns the fechaFinal.
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * @param fechaFinal The fechaFinal to set.
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * @return Returns the fechaInicial.
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * @param fechaInicial The fechaInicial to set.
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * @return Returns the filtroAsocio.
	 */
	public String getFiltroAsocio() {
		return filtroAsocio;
	}

	/**
	 * @param filtroAsocio The filtroAsocio to set.
	 */
	public void setFiltroAsocio(String filtroAsocio) {
		this.filtroAsocio = filtroAsocio;
	}

	/**
	 * @return Returns the horaFinal.
	 */
	public String getHoraFinal() {
		return horaFinal;
	}

	/**
	 * @param horaFinal The horaFinal to set.
	 */
	public void setHoraFinal(String horaFinal) {
		this.horaFinal = horaFinal;
	}

	/**
	 * @return Returns the horaInicial.
	 */
	public String getHoraInicial() {
		return horaInicial;
	}

	/**
	 * @param horaInicial The horaInicial to set.
	 */
	public void setHoraInicial(String horaInicial) {
		this.horaInicial = horaInicial;
	}
	
	
	

	/**
	 * @return Returns the registroAccidentesTransitoReporte1.
	 */
	public HashMap getRegistroAccidentesTransitoReporte1() {
		return registroAccidentesTransitoReporte1;
	}

	/**
	 * @param registroAccidentesTransitoReporte1 The registroAccidentesTransitoReporte1 to set.
	 */
	public void setRegistroAccidentesTransitoReporte1(
			HashMap registroAccidentesTransitoReporte1) {
		this.registroAccidentesTransitoReporte1 = registroAccidentesTransitoReporte1;
	}

	/**
	 * @return Returns the registroAccidentesTransitoReporte2.
	 */
	public HashMap getRegistroAccidentesTransitoReporte2() {
		return registroAccidentesTransitoReporte2;
	}

	/**
	 * @param registroAccidentesTransitoReporte2 The registroAccidentesTransitoReporte2 to set.
	 */
	public void setRegistroAccidentesTransitoReporte2(
			HashMap registroAccidentesTransitoReporte2) {
		this.registroAccidentesTransitoReporte2 = registroAccidentesTransitoReporte2;
	}
	
	public HashMap getOrdenesCirugia()
	{
		return ordenesCirugia;
	}

	public void setOrdenesCirugia(HashMap ordenesCirugia)
	{
		this.ordenesCirugia = ordenesCirugia;
	}

	
	public HashMap getOrdenesInterconsulta()
	{
		return ordenesInterconsulta;
	}

	public void setOrdenesInterconsulta(HashMap ordenesInterconsulta)
	{
		this.ordenesInterconsulta = ordenesInterconsulta;
	}

	public HashMap getOrdenesMedicamentos()
	{
		return ordenesMedicamentos;
	}

	public void setOrdenesMedicamentos(HashMap ordenesMedicamentos)
	{
		this.ordenesMedicamentos = ordenesMedicamentos;
	}


	public HashMap getOrdenesProcedimientos()
	{
		return ordenesProcedimientos;
	}

	public void setOrdenesProcedimientos(HashMap ordenesProcedimientos)
	{
		this.ordenesProcedimientos = ordenesProcedimientos;
	}

	
//-------------------------------------MANIZALES --------------------------------------------------//
	
	public Collection getHistoricoAnotacionesEnfermeria() {
		return historicoAnotacionesEnfermeria;
	}

	public void setHistoricoAnotacionesEnfermeria(
			Collection historicoAnotacionesEnfermeria) {
		this.historicoAnotacionesEnfermeria = historicoAnotacionesEnfermeria;
	}

	public Collection getSignosVitalesInstitucionCcosto() {
		return signosVitalesInstitucionCcosto;
	}

	public void setSignosVitalesInstitucionCcosto(
			Collection signosVitalesInstitucionCcosto) {
		this.signosVitalesInstitucionCcosto = signosVitalesInstitucionCcosto;
	}

	public Collection getSignosVitalesFijosHisto() {
		return signosVitalesFijosHisto;
	}

	public void setSignosVitalesFijosHisto(Collection signosVitalesFijosHisto) {
		this.signosVitalesFijosHisto = signosVitalesFijosHisto;
	}

	public Collection getSignosVitalesHistoTodos() {
		return signosVitalesHistoTodos;
	}

	public void setSignosVitalesHistoTodos(Collection signosVitalesHistoTodos) {
		this.signosVitalesHistoTodos = signosVitalesHistoTodos;
	}

	public Collection getSignosVitalesParamHisto() {
		return signosVitalesParamHisto;
	}

	public void setSignosVitalesParamHisto(Collection signosVitalesParamHisto) {
		this.signosVitalesParamHisto = signosVitalesParamHisto;
	}
	
	/**
	 * @return Returns the colCateteresSondaInstitucionCcosto.
	 */
	public Collection getColCateteresSondaInstitucionCcosto()
	{
		return colCateteresSondaInstitucionCcosto;
	}
	/**
	 * @param colCateteresSondaInstitucionCcosto The colCateteresSondaInstitucionCcosto to set.
	 */
	public void setColCateteresSondaInstitucionCcosto(
			Collection colCateteresSondaInstitucionCcosto)
	{
		this.colCateteresSondaInstitucionCcosto = colCateteresSondaInstitucionCcosto;
	}
	
	/**
	 * @return Returns the cateterSondaFijosHisto.
	 */
	public Collection getCateterSondaFijosHisto()
	{
		return cateterSondaFijosHisto;
	}
	/**
	 * @param cateterSondaFijosHisto The cateterSondaFijosHisto to set.
	 */
	public void setCateterSondaFijosHisto(Collection cateterSondaFijosHisto)
	{
		this.cateterSondaFijosHisto = cateterSondaFijosHisto;
	}
	/**
	 * @return Returns the cateterSondaHistoTodos.
	 */
	public Collection getCateterSondaHistoTodos()
	{
		return cateterSondaHistoTodos;
	}
	/**
	 * @param cateterSondaHistoTodos The cateterSondaHistoTodos to set.
	 */
	public void setCateterSondaHistoTodos(Collection cateterSondaHistoTodos)
	{
		this.cateterSondaHistoTodos = cateterSondaHistoTodos;
	}
	/**
	 * @return Returns the cateterSondaParamHisto.
	 */
	public Collection getCateterSondaParamHisto()
	{
		return cateterSondaParamHisto;
	}
	/**
	 * @param cateterSondaParamHisto The cateterSondaParamHisto to set.
	 */
	public void setCateterSondaParamHisto(Collection cateterSondaParamHisto)
	{
		this.cateterSondaParamHisto = cateterSondaParamHisto;
	}
	
	/**
	 * @return Retorna mapaHistoricoCateterSonda.
	 */
	public HashMap getMapaHistoricoCateterSonda()
	{
		return mapaHistoricoCateterSonda;
	}

	/**
	 * @param mapaHistoricoCateterSonda Asigna mapaHistoricoCateterSonda.
	 */
	public void setMapaHistoricoCateterSonda(HashMap mapaHistoricoCateterSonda)
	{
		this.mapaHistoricoCateterSonda = mapaHistoricoCateterSonda;
	}

	public HashMap getMapaHistoricoCuidadosEspeciales() {
		return mapaHistoricoCuidadosEspeciales;
	}

	public void setMapaHistoricoCuidadosEspeciales(
			HashMap mapaHistoricoCuidadosEspeciales) {
		this.mapaHistoricoCuidadosEspeciales = mapaHistoricoCuidadosEspeciales;
	}

	public Collection getEscalasGlasgowInstitucionCCosto() {
		return escalasGlasgowInstitucionCCosto;
	}

	public void setEscalasGlasgowInstitucionCCosto(
			Collection escalasGlasgowInstitucionCCosto) {
		this.escalasGlasgowInstitucionCCosto = escalasGlasgowInstitucionCCosto;
	}

	public HashMap getMapaHistoricoEscalaGlasgow() {
		return mapaHistoricoEscalaGlasgow;
	}

	public void setMapaHistoricoEscalaGlasgow(HashMap mapaHistoricoEscalaGlasgow) {
		this.mapaHistoricoEscalaGlasgow = mapaHistoricoEscalaGlasgow;
	}

	public HashMap getMapaHistoricoPupilas() {
		return mapaHistoricoPupilas;
	}

	public void setMapaHistoricoPupilas(HashMap mapaHistoricoPupilas) {
		this.mapaHistoricoPupilas = mapaHistoricoPupilas;
	}

	public HashMap getMapaHistoricoConvulsiones() {
		return mapaHistoricoConvulsiones;
	}

	public void setMapaHistoricoConvulsiones(HashMap mapaHistoricoConvulsiones) {
		this.mapaHistoricoConvulsiones = mapaHistoricoConvulsiones;
	}

	public HashMap getMapaHistoricoControlEsfinteres() {
		return mapaHistoricoControlEsfinteres;
	}

	public void setMapaHistoricoControlEsfinteres(
			HashMap mapaHistoricoControlEsfinteres) {
		this.mapaHistoricoControlEsfinteres = mapaHistoricoControlEsfinteres;
	}

	public HashMap getMapaHistoricoFuerzaMuscular() {
		return mapaHistoricoFuerzaMuscular;
	}

	public void setMapaHistoricoFuerzaMuscular(HashMap mapaHistoricoFuerzaMuscular) {
		this.mapaHistoricoFuerzaMuscular = mapaHistoricoFuerzaMuscular;
	}
	
	public HashMap getMapaCodigosPeticionCirugia() {
		return mapaCodigosPeticionCirugia;
	}

	public void setMapaCodigosPeticionCirugia(HashMap mapaCodigosPeticionCirugia) {
		this.mapaCodigosPeticionCirugia = mapaCodigosPeticionCirugia;
	}
	
	public HashMap getMapaEncabezadosHojaAnestesia() {
		return mapaEncabezadosHojaAnestesia;
	}

	public void setMapaEncabezadosHojaAnestesia(HashMap mapaEncabezadosHojaAnestesia) {
		this.mapaEncabezadosHojaAnestesia = mapaEncabezadosHojaAnestesia;
	}
	
	public HashMap getMapaExamenesLaboratorioPreanestesia() {
		return mapaExamenesLaboratorioPreanestesia;
	}

	public void setMapaExamenesLaboratorioPreanestesia(
			HashMap mapaExamenesLaboratorioPreanestesia) {
		this.mapaExamenesLaboratorioPreanestesia = mapaExamenesLaboratorioPreanestesia;
	}
	
	public HashMap getMapaHistoExamenesFisicosText() {
		return mapaHistoExamenesFisicosText;
	}

	public void setMapaHistoExamenesFisicosText(HashMap mapaHistoExamenesFisicosText) {
		this.mapaHistoExamenesFisicosText = mapaHistoExamenesFisicosText;
	}

	public HashMap getMapaHistoExamenesFisicosTextArea() {
		return mapaHistoExamenesFisicosTextArea;
	}

	public void setMapaHistoExamenesFisicosTextArea(
			HashMap mapaHistoExamenesFisicosTextArea) {
		this.mapaHistoExamenesFisicosTextArea = mapaHistoExamenesFisicosTextArea;
	}
	
	public HashMap getMapaHistoConclusiones() {
		return mapaHistoConclusiones;
	}

	public void setMapaHistoConclusiones(HashMap mapaHistoConclusiones) {
		this.mapaHistoConclusiones = mapaHistoConclusiones;
	}
	
	public HashMap getMapaHistoBalanceLiquidosHojaAnestesia() {
		return mapaHistoBalanceLiquidosHojaAnestesia;
	}

	public void setMapaHistoBalanceLiquidosHojaAnestesia(
			HashMap mapaHistoBalanceLiquidosHojaAnestesia) {
		this.mapaHistoBalanceLiquidosHojaAnestesia = mapaHistoBalanceLiquidosHojaAnestesia;
	}

	public HashMap getMapaHistoMedicamentosHojaAnestesia() {
		return mapaHistoMedicamentosHojaAnestesia;
	}

	public void setMapaHistoMedicamentosHojaAnestesia(
			HashMap mapaHistoMedicamentosHojaAnestesia) {
		this.mapaHistoMedicamentosHojaAnestesia = mapaHistoMedicamentosHojaAnestesia;
	}

public HashMap getMapaHistoTecAnestesia() {
		return mapaHistoTecAnestesia;
	}

	public void setMapaHistoTecAnestesia(HashMap mapaHistoTecAnestesia) {
		this.mapaHistoTecAnestesia = mapaHistoTecAnestesia;
	}
	
	/**
	 * @return Returna la propiedad del mapa mapaHistoTecAnestesia
	 */
	public Object getMapaHistoTecAnestesia(String key)
	{
		return mapaHistoTecAnestesia.get(key);
	}
	/**
	 * @param Asigna la propiedad al mapa mapaHistoTecAnestesia
	 */
	public void setMapaHistoTecAnestesia(String key, Object value)
	{
		this.mapaHistoTecAnestesia.put(key, value);
	}
	
	
	
	/**
	 * @return Returna la propiedad del mapa mapaHistoSignosVitales
	 */
	public Object getMapaHistoSignosVitales(String key)
	{
		return mapaHistoSignosVitales.get(key);
	}
	/**
	 * @param Asigna la propiedad al mapa mapaHistoSignosVitales
	 */
	public void setMapaHistoSignosVitales(String key, Object value)
	{
		this.mapaHistoSignosVitales.put(key, value);
	}
	
	/**
	 * @return Returns the listadoSignosVitales.
	 */
	public Collection getListadoSignosVitales()
	{
		return listadoSignosVitales;
	}
	
	/**
	 * @param listadoSignosVitales The listadoSignosVitales to set.
	 */
	public void setListadoSignosVitales(Collection listadoSignosVitales)
	{
		this.listadoSignosVitales = listadoSignosVitales;
	}
	
	public HashMap getMapaHistoSignosVitales() {
		return mapaHistoSignosVitales;
	}

		/**
	 * @param Asigna Mapa de Control de Liquidos.
	 */
	public void setMapaControlLiq(HashMap mapa) {
		this.mapaControlLiq = mapa;
	}

	/** 
	 * @return Retorna mapaDieta.
	 */
	public Object getMapaControlLiq(String key) {
		return this.mapaControlLiq.get(key);
	}
	/**
	 * @param Asigna mapaDieta.
	 */
	public void setMapaControlLiq(String key, String dato) {
		this.mapaControlLiq.put(key, dato);
	}

public HashMap getOrdenesMedicas()
	{
		return ordenesMedicas;
	}

	public void setOrdenesMedicas(HashMap ordenesMedicas)
	{
		this.ordenesMedicas = ordenesMedicas;
	}

	/**
	 * @return Retorna Mapa de Control de Liquidos.
	 */
	public HashMap getMapaControlLiq() {
		return mapaControlLiq;
	}

	/**
	 * @param Asigna Mapa 
	 */
	public void setMapaAntAlergia(HashMap mapa) {
		this.mapaAntAlergia = mapa;
	}

	/** 
	 * @return Retorna Mapa
	 */
	public Object getMapaAntAlergia(String key) {
		return this.mapaAntAlergia.get(key);
	}
	/**
	 * @param Asigna Mapa.
	 */
	public void setMapaAntAlergia(String key, String dato) {
		this.mapaAntAlergia.put(key, dato);
	}

	/**
	 * @return Retorna Mapa 
	 */
	public HashMap getMapaAntAlergia() {
		return mapaAntAlergia;
	}

	/**
	 * @param Asigna Mapa 
	 */
	public void setMapaAntFamiliares(HashMap mapa) {
		this.mapaAntFamiliares = mapa;
	}

	/** 
	 * @return Retorna Mapa
	 */
	public Object getMapaAntFamiliares(String key) {
		return this.mapaAntFamiliares.get(key);
	}
	/**
	 * @param Asigna Mapa.
	 */
	public void setMapaAntFamiliares(String key, String dato) {
		this.mapaAntFamiliares.put(key, dato);
	}

	/**
	 * @return Retorna Mapa 
	 */
	public HashMap getMapaAntFamiliares() {
		return mapaAntFamiliares;
	}

	
	/**
	 * @param Asigna Mapa 
	 */
	public void setMapaAntFamOftal(HashMap mapa) {
		this.mapaAntFamOftal = mapa;
	}

	/** 
	 * @return Retorna Mapa
	 */
	public Object getMapaAntFamOftal(String key) {
		return this.mapaAntFamOftal.get(key);
	}
	/**
	 * @param Asigna Mapa.
	 */
	public void setMapaAntFamOftal(String key, String dato) {
		this.mapaAntFamOftal.put(key, dato);
	}

	/**
	 * @return Retorna Mapa 
	 */
	public HashMap getMapaAntFamOftal() {
		return mapaAntFamOftal;
	}

	/**
	 * @return Returns the codigosEvoluciones.
	 */
	public HashMap getCodigosEvoluciones() {
		return codigosEvoluciones;
	}

	/**
	 * @param codigosEvoluciones The codigosEvoluciones to set.
	 */
	public void setCodigosEvoluciones(HashMap codigosEvoluciones) {
		this.codigosEvoluciones = codigosEvoluciones;
	}

	public String getAtenciones()
	{
		return atenciones;
	}

	public void setAtenciones(String atenciones)
	{
		this.atenciones = atenciones;
	}
	
		/**
	 * @return Returns the encabezadoImpresion.
	 */
	public HashMap getEncabezadoImpresion() {
		return encabezadoImpresion;
	}

	/**
	 * @param encabezadoImpresion The encabezadoImpresion to set.
	 */
	public void setEncabezadoImpresion(HashMap encabezadoImpresion) {
		this.encabezadoImpresion = encabezadoImpresion;
	}

	public HashMap getSecciones()
	{
		return secciones;
	}

	public void setSecciones(HashMap secciones)
	{
		this.secciones = secciones;
	}
	
	public Object getSecciones(String key)
	{
		return secciones.get(key);
	}

	public void setSecciones(String key,Object value)
	{
		this.secciones.put(key, value);
	}

	public boolean isImprimirVIHospitalizacion()
	{
		return imprimirVIHospitalizacion;
	}

	public void setImprimirVIHospitalizacion(boolean imprimirVIHospitalizacion)
	{
		this.imprimirVIHospitalizacion = imprimirVIHospitalizacion;
	}

	public boolean isImprimirVIUrgencias()
	{
		return imprimirVIUrgencias;
	}

	public void setImprimirVIUrgencias(boolean imprimirVIUrgencias)
	{
		this.imprimirVIUrgencias = imprimirVIUrgencias;
	}

	/**
	 * @return Returns the solicitudesInterConsulta.
	 */
	public HashMap getSolicitudesInterConsulta() {
		return solicitudesInterConsulta;
	}

	/**
	 * @param solicitudesInterConsulta The solicitudesInterConsulta to set.
	 */
	public void setSolicitudesInterConsulta(HashMap solicitudesInterConsulta) {
		this.solicitudesInterConsulta = solicitudesInterConsulta;
	}
	

	
	/**
	 * @param Asigna Mapa 
	 */
	public void setMapaAntPersoOftal(HashMap mapa) {
		this.mapaAntPersoOftal = mapa;
	}

	/** 
	 * @return Retorna Mapa
	 */
	public Object getMapaAntPersoOftal(String key) {
		return this.mapaAntPersoOftal.get(key);
	}
	/**
	 * @param Asigna Mapa.
	 */
	public void setMapaAntPersoOftal(String key, String dato) {
		this.mapaAntPersoOftal.put(key, dato);
	}

	/**
	 * @return Retorna Mapa 
	 */
	public HashMap getMapaAntPersoOftal() {
		return mapaAntPersoOftal;
	}


	/**
	 * @param Asigna Mapa 
	 */
	public void setMapaAntGineco(HashMap mapa) {
		this.mapaAntGineco = mapa;
	}

	/** 
	 * @return Retorna Mapa
	 */
	public Object getMapaAntGineco(String key) {
		return this.mapaAntGineco.get(key);
	}
	/**
	 * @param Asigna Mapa.
	 */
	public void setMapaAntGineco(String key, String dato) {
		this.mapaAntGineco.put(key, dato);
	}

	/**
	 * @return Retorna Mapa 
	 */
	public HashMap getMapaAntGineco() {
		return mapaAntGineco;
	}	
	
	/**
	 * Returns the historicos.
	 * @return ArrayList
	 */
	public ArrayList getHistoricos() 
	{
		return historicos;
	}

	/**
	 * Sets the historicos.
	 * @param historicos The historicos to set
	 */
	public void setHistoricos(ArrayList historicos) 
	{
		this.historicos = historicos;
	}
	
	public HashMap getSoporteRespiratorio()
	{
		return soporteRespiratorio;
	}

	public void setSoporteRespiratorio(HashMap soporteRespiratorio)
	{
		this.soporteRespiratorio = soporteRespiratorio;
	}


	/**
	 * @param Asigna Mapa 
	 */
	public void setMapaAntMedicamento(HashMap mapa) {
		this.mapaAntMedicamento = mapa;
	}

	/** 
	 * @return Retorna Mapa
	 */
	public Object getMapaAntMedicamento(String key) {
		return this.mapaAntMedicamento.get(key);
	}
	/**
	 * @param Asigna Mapa.
	 */
	public void setMapaAntMedicamento(String key, String dato) {
		this.mapaAntMedicamento.put(key, dato);
	}

	/**
	 * @return Retorna Mapa 
	 */
	public HashMap getMapaAntMedicamento() {
		return mapaAntMedicamento;
	}
	
	/**
	 * @param Asigna Mapa 
	 */
	public void setMapaAntMedicos(HashMap mapa) {
		this.mapaAntMedicos = mapa;
	}

	/** 
	 * @return Retorna Mapa
	 */
	public Object getMapaAntMedicos(String key) {
		return this.mapaAntMedicos.get(key);
	}
	/**
	 * @param Asigna Mapa.
	 */
	public void setMapaAntMedicos(String key, String dato) {
		this.mapaAntMedicos.put(key, dato);
	}

	/**
	 * @return Retorna Mapa 
	 */
	public HashMap getMapaAntMedicos() {
		return mapaAntMedicos;
	}		

	/**
	 * @param Asigna Mapa 
	 */
	public void setMapaAntToxicos(HashMap mapa) {
		this.mapaAntToxicos = mapa;
	}

	/** 
	 * @return Retorna Mapa
	 */
	public Object getMapaAntToxicos(String key) {
		return this.mapaAntToxicos.get(key);
	}
	/**
	 * @param Asigna Mapa.
	 */
	public void setMapaAntToxicos(String key, String dato) {
		this.mapaAntToxicos.put(key, dato);
	}

	/**
	 * @return Retorna Mapa 
	 */
	public HashMap getMapaAntToxicos() {
		return mapaAntToxicos;
	}		
	
	
	/**
	 * @param Asigna Mapa 
	 */
	public void setMapaAntTransfusionales(HashMap mapa) {
		this.mapaAntTransfusionales = mapa;
	}

	/** 
	 * @return Retorna Mapa
	 */
	public Object getMapaAntTransfusionales(String key) {
		return this.mapaAntTransfusionales.get(key);
	}
	/**
	 * @param Asigna Mapa.
	 */
	public void setMapaAntTransfusionales(String key, String dato) {
		this.mapaAntTransfusionales.put(key, dato);
	}

	/**
	 * @return Retorna Mapa 
	 */
	public HashMap getMapaAntTransfusionales() {
		return mapaAntTransfusionales;
	}		
	
	/**
	 * @param Asigna Mapa 
	 */
	public void setMapaVacunas(HashMap mapa) {
		this.mapaVacunas = mapa;
	}

	/** 
	 * @return Retorna Mapa
	 */
	public Object getMapaVacunas(String key) {
		return this.mapaVacunas.get(key);
	}
	/**
	 * @param Asigna Mapa.
	 */
	public void setMapaVacunas(String key, String dato) {
		this.mapaVacunas.put(key, dato);
	}

	/**
	 * @return Retorna Mapa 
	 */
	public HashMap getMapaVacunas() {
		return mapaVacunas;
	}
	
	/**
	 * @return Retorna mapaTiposInmunizacion.
	 */
	public HashMap getMapaTiposInmunizacion() {
		return mapaTiposInmunizacion;
	}

	/**
	 * @param Asigna mapaTiposInmunizacion.
	 */
	public void setMapaTiposInmunizacion(HashMap mapaTiposInmunizacion) {
		this.mapaTiposInmunizacion = mapaTiposInmunizacion;
	}

	/**
	 * @return Retorna mapaTiposInmunizacion.
	 */
	public Object getMapaTiposInmunizacion(String key)
	{
		return mapaTiposInmunizacion.get(key);
	}

	/**
	 * @param Asigna mapaTiposInmunizacion.
	 */
	public void setMapaTiposInmunizacion(String key, Object obj) 
	{
		this.mapaTiposInmunizacion.put(key, obj);
	}


	/**
	 * @return Retorna mapaTiposInmunizacion.
	 */
	public HashMap getMapaAntOtros() {
		return mapaAntOtros;
	}

	/**
	 * @param Asigna mapaTiposInmunizacion.
	 */
	public void setMapaAntOtros(HashMap mapaTiposInmunizacion) {
		this.mapaAntOtros = mapaTiposInmunizacion;
	}

	/**
	 * @return Retorna mapaTiposInmunizacion.
	 */
	public Object getMapaAntOtros(String key)
	{
		return mapaAntOtros.get(key);
	}

	/**
	 * @param Asigna mapaTiposInmunizacion.
	 */
	public void setMapaAntOtros(String key, Object obj) 
	{
		this.mapaAntOtros.put(key, obj);
	}
	
	/**
	 * @return Retorna mapaTiposInmunizacion.
	 */
	public HashMap getMapaAntPediatricos() {
		return mapaAntPediatricos;
	}

	/**
	 * @param Asigna mapaTiposInmunizacion.
	 */
	public void setMapaAntPediatricos(HashMap mapa) {
		this.mapaAntPediatricos = mapa;
	}

	/**
	 * @return Retorna mapaTiposInmunizacion.
	 */
	public Object getMapaAntPediatricos(String key)
	{
		return mapaAntPediatricos.get(key);
	}

	/**
	 * @param Asigna mapaTiposInmunizacion.
	 */
	public void setMapaAntPediatricos(String key, Object obj) 
	{
		this.mapaAntPediatricos.put(key, obj);
	}
	
	public AntecedentePediatrico getAntPed()
	{
		return antPed;
	}

	public void setAntPed(AntecedentePediatrico antPed)
	{
		this.antPed = antPed;
	}

	
	public ArrayList getEmbarazoOpcionCampos()
	{
		return categoriaEmbarazoOpcionCampo;
	}

	public InfoDatos getEmbarazoOpcionCampo(int ai_i)
	{
		return (InfoDatos)categoriaEmbarazoOpcionCampo.get(ai_i);
	}

	public HashMap getMotivosTiposParto() {
		return motivosTiposParto;
	}

	public void setMotivosTiposParto(HashMap motivosTiposParto) {
		this.motivosTiposParto = motivosTiposParto;
	}

	public HashMap getMotivosTiposPartoCarga() {
		return motivosTiposPartoCarga;
	}

	public void setMotivosTiposPartoCarga(HashMap motivosTiposPartoCarga) {
		this.motivosTiposPartoCarga = motivosTiposPartoCarga;
	}

	public HashMap getTiposParto() {
		return tiposParto;
	}

	public void setTiposParto(HashMap tiposParto) {
		this.tiposParto = tiposParto;
	}

	public HashMap getTiposPartoCarga() {
		return tiposPartoCarga;
	}

	public void setTiposPartoCarga(HashMap tiposPartoCarga) {
		this.tiposPartoCarga = tiposPartoCarga;
	}

	public ArrayList getInmunizacionesList() {
		return inmunizacionesList;
	}

	public void setInmunizacionesList(ArrayList inmunizacionesList) {
		this.inmunizacionesList = inmunizacionesList;
	}

	public ArrayList getTiposPartoList() {
		return tiposPartoList;
	}

	public void setTiposPartoList(ArrayList tiposPartoList) {
		this.tiposPartoList = tiposPartoList;
	}
	
	//- mapaAntOdonto
	/**
	 * @return Retorna mapaTiposInmunizacion.
	 */
	public HashMap getMapaAntOdonto() {
		return mapaAntOdonto;
	}

	/**
	 * @param Asigna mapaTiposInmunizacion.
	 */
	public void setMapaAntOdonto(HashMap mapa) {
		this.mapaAntOdonto = mapa;
	}

	/**
	 * @return Retorna mapaTiposInmunizacion.
	 */
	public Object getMapaAntOdonto(String key)
	{
		return mapaAntOdonto.get(key);
	}

	/**
	 * @param Asigna mapaTiposInmunizacion.
	 */
	public void setMapaAntOdonto(String key, Object obj) 
	{
		this.mapaAntOdonto.put(key, obj);
	}	
	
	//-mapaHojaQuirur
	/**
	 * @return Retorna mapaTiposInmunizacion.
	 */
	public HashMap getMapaHojaQuirur() {
		return mapaHojaQuirur;
	}

	/**
	 * @param Asigna mapaTiposInmunizacion.
	 */
	public void setMapaHojaQuirur(HashMap mapa) {
		this.mapaHojaQuirur = mapa;
	}

	/**
	 * @return Retorna mapaTiposInmunizacion.
	 */
	public String getMapaHojaQuirur(String key)
	{
		return mapaHojaQuirur.get(key)+"";
	}

	/**
	 * @param Asigna mapaTiposInmunizacion.
	 */
	public void setMapaHojaQuirur(String key, Object obj) 
	{
		this.mapaHojaQuirur.put(key, obj);
	}

	public Collection getListadoTecAnestesiaGral() {
		return listadoTecAnestesiaGral;
	}

	public void setListadoTecAnestesiaGral(Collection listadoTecAnestesiaGral) {
		this.listadoTecAnestesiaGral = listadoTecAnestesiaGral;
	}

	public Collection getListadoTecAnestesiaOpcionesGral() {
		return listadoTecAnestesiaOpcionesGral;
	}

	public void setListadoTecAnestesiaOpcionesGral(
			Collection listadoTecAnestesiaOpcionesGral) {
		this.listadoTecAnestesiaOpcionesGral = listadoTecAnestesiaOpcionesGral;
	}

	public Collection getListadoTecAnestesiaRegional() {
		return listadoTecAnestesiaRegional;
	}

	public void setListadoTecAnestesiaRegional(
			Collection listadoTecAnestesiaRegional) {
		this.listadoTecAnestesiaRegional = listadoTecAnestesiaRegional;
	}	
	
	/**
	 * @return Retorna mapaTiposInmunizacion.
	 */
	public HashMap getMapaNotasEnfer() {
		return this.mapaNotasEnfer;
	}

	/**
	 * @param Asigna mapaTiposInmunizacion.
	 */
	public void setMapaNotasEnfer(HashMap mapa) {
		this.mapaNotasEnfer = mapa;
	}

	/**
	 * @return Retorna mapaTiposInmunizacion.
	 */
	public String getMapaNotasEnfer(String key)
	{
		return mapaNotasEnfer.get(key)+"";
	}

	/**
	 * @param Asigna mapaTiposInmunizacion.
	 */
	public void setMapaNotasEnfer(String key, Object obj) 
	{
		this.mapaNotasEnfer.put(key, obj);
	}

	
	/**
	 * @return Retorna mapaTiposInmunizacion.
	 */
	public HashMap getMapaNotasRecuperacion() {
		return this.mapaNotasRecuperacion;
	}

	/**
	 * @param Asigna mapaTiposInmunizacion.
	 */
	public void setMapaNotasRecuperacion(HashMap mapa) {
		this.mapaNotasRecuperacion = mapa;
	}

	/**
	 * @return Retorna mapaTiposInmunizacion.
	 */
	public String getMapaNotasRecuperacion(String key)
	{
		return mapaNotasRecuperacion.get(key)+"";
	}

	/**
	 * @param Asigna mapaTiposInmunizacion.
	 */
	public void setMapaNotasRecuperacion(String key, Object obj) 
	{
		this.mapaNotasRecuperacion.put(key, obj);
	}

	/**
	 * @return Returns the mapaEnlacesValoracionCE.
	 */
	public HashMap getMapaEnlacesValoracionCE()
	{
		return mapaEnlacesValoracionCE;
	}

	/**
	 * @param mapaEnlacesValoracionCE The mapaEnlacesValoracionCE to set.
	 */
	public void setMapaEnlacesValoracionCE(HashMap mapaEnlacesValoracionCE)
	{
		this.mapaEnlacesValoracionCE = mapaEnlacesValoracionCE;
	}

	/**
	 * @return Returns the mapaEnlacesValoracionCE.
	 */
	public Object getMapaEnlacesValoracionCE(Object key)
	{
		return mapaEnlacesValoracionCE.get(key);
	}

	/**
	 * @param mapaEnlacesValoracionCE The mapaEnlacesValoracionCE to set.
	 */
	public void setMapaEnlacesValoracionCE(Object key, Object value)
	{
		this.mapaEnlacesValoracionCE.put(key, value);
	}

	/**
	 * @return the eventosAdversos
	 */
	public HashMap getEventosAdversos() {
		return eventosAdversos;
	}

	/**
	 * @param eventosAdversos the eventosAdversos to set
	 */
	public void setEventosAdversos(HashMap eventosAdversos) {
		this.eventosAdversos = eventosAdversos;
	}
	
	/**
	 * @return the eventosAdversos
	 */
	public Object getEventosAdversos(String llave) {
		return eventosAdversos.get(llave);
	}

	/**
	 * @param eventosAdversos the eventosAdversos to set
	 */
	public void setEventosAdversos(String llave, Object obj) {
		this.eventosAdversos.put(llave, obj);
	}

	public HashMap getResumenParcialHistoriaClinica() {
		return resumenParcialHistoriaClinica;
	}

	public void setResumenParcialHistoriaClinica(
			HashMap resumenParcialHistoriaClinica) {
		this.resumenParcialHistoriaClinica = resumenParcialHistoriaClinica;
	}

	/**
	 * @return the valoracionesCuidadoEspecial
	 */
	public HashMap<String, Object> getValoracionesCuidadoEspecial() {
		return valoracionesCuidadoEspecial;
	}

	/**
	 * @param valoracionesCuidadoEspecial the valoracionesCuidadoEspecial to set
	 */
	public void setValoracionesCuidadoEspecial(
			HashMap<String, Object> valoracionesCuidadoEspecial) {
		this.valoracionesCuidadoEspecial = valoracionesCuidadoEspecial;
	}

	/**
	 * @return the solicitudesFactura
	 */
	public String getSolicitudesFactura() {
		return solicitudesFactura;
	}

	/**
	 * @param solicitudesFactura the solicitudesFactura to set
	 */
	public void setSolicitudesFactura(String solicitudesFactura) {
		this.solicitudesFactura = solicitudesFactura;
	}
	
	/**
	 * @return the infoInstitucion
	 */
	public HashMap getInfoInstitucion() {
		return infoInstitucion;
	}

	/**
	 * @param infoInstitucion the infoInstitucion to set
	 */
	public void setInfoInstitucion(HashMap infoInstitucion) {
		this.infoInstitucion = infoInstitucion;
	}

	/**
	 * @return the arrayAntecedentesActuales
	 */
	public ArrayList<DtoAntecendenteOdontologico> getArrayAntecedentesActuales() {
		return arrayAntecedentesActuales;
	}

	/**
	 * @param arrayAntecedentesActuales the arrayAntecedentesActuales to set
	 */
	public void setArrayAntecedentesActuales(
			ArrayList<DtoAntecendenteOdontologico> arrayAntecedentesActuales) {
		this.arrayAntecedentesActuales = arrayAntecedentesActuales;
	}

	/**
	 * @return the ultimoAntecedente
	 */
	public DtoAntecendenteOdontologico getUltimoAntecedente() {
		return ultimoAntecedente;
	}

	/**
	 * @param ultimoAntecedente the ultimoAntecedente to set
	 */
	public void setUltimoAntecedente(DtoAntecendenteOdontologico ultimoAntecedente) {
		this.ultimoAntecedente = ultimoAntecedente;
	}

	/**
	 * @return the datosGeneralesUltimoAnt
	 */
	public String getDatosGeneralesUltimoAnt() {
		return datosGeneralesUltimoAnt;
	}

	/**
	 * @param datosGeneralesUltimoAnt the datosGeneralesUltimoAnt to set
	 */
	public void setDatosGeneralesUltimoAnt(String datosGeneralesUltimoAnt) {
		this.datosGeneralesUltimoAnt = datosGeneralesUltimoAnt;
	}

	/**
	 * @return the componenteAntOdo
	 */
	public DtoComponente getComponenteAntOdo() {
		return componenteAntOdo;
	}

	/**
	 * @param componenteAntOdo the componenteAntOdo to set
	 */
	public void setComponenteAntOdo(DtoComponente componenteAntOdo) {
		this.componenteAntOdo = componenteAntOdo;
	}

	/**
	 * @return the ordenesAmbulatorias
	 */
	public HashMap getOrdenesAmbulatorias() {
		return ordenesAmbulatorias;
	}

	/**
	 * @param ordenesAmbulatorias the ordenesAmbulatorias to set
	 */
	public void setOrdenesAmbulatorias(HashMap ordenesAmbulatorias) {
		this.ordenesAmbulatorias = ordenesAmbulatorias;
	}

	/**
	 * @return the ordenesAmbulatorias
	 */
	public Object getOrdenesAmbulatorias(String llave) {
		return ordenesAmbulatorias.get(llave);
	}

	/**
	 * @param ordenesAmbulatorias the ordenesAmbulatorias to set
	 */
	public void setOrdenesAmbulatorias(String llave, Object obj) {
		this.ordenesAmbulatorias.put(llave, obj);
	}

	/**
	 * @return the mapaConsultasPYP
	 */
	public HashMap getMapaConsultasPYP() {
		return mapaConsultasPYP;
	}

	/**
	 * @param mapaConsultasPYP the mapaConsultasPYP to set
	 */
	public void setMapaConsultasPYP(HashMap mapaConsultasPYP) {
		this.mapaConsultasPYP = mapaConsultasPYP;
	}

	public HashMap getResultadoLaboratorios() {
		return resultadoLaboratorios;
	}

	public void setResultadoLaboratorios(HashMap resultadoLaboratorios) {
		this.resultadoLaboratorios = resultadoLaboratorios;
	}

	public HashMap getValoracionesEnfermeria() {
		return valoracionesEnfermeria;
	}

	public void setValoracionesEnfermeria(HashMap valoracionesEnfermeria) {
		this.valoracionesEnfermeria = valoracionesEnfermeria;
	}

	/**
	 * @return the nombreArchivoGenerado
	 */
	public String getNombreArchivoGenerado() {
		return nombreArchivoGenerado;
	}

	/**
	 * @param nombreArchivoGenerado the nombreArchivoGenerado to set
	 */
	public void setNombreArchivoGenerado(String nombreArchivoGenerado) {
		this.nombreArchivoGenerado = nombreArchivoGenerado;
	}

	/**
	 * @return the viaIngreso
	 */
	public String getViaIngreso() {
		return viaIngreso;
	}

	/**
	 * @param viaIngreso the viaIngreso to set
	 */
	public void setViaIngreso(String viaIngreso) {
		this.viaIngreso = viaIngreso;
	}

	/**
	 * @return the informacionNotasRecuperacion
	 */
	public HashMap getInformacionNotasRecuperacion() {
		return informacionNotasRecuperacion;
	}

	/**
	 * @param informacionNotasRecuperacion the informacionNotasRecuperacion to set
	 */
	public void setInformacionNotasRecuperacion(HashMap informacionNotasRecuperacion) {
		this.informacionNotasRecuperacion = informacionNotasRecuperacion;
	}

	/**
	 * @return the tipoImpresion
	 */
	public String getTipoImpresion() {
		return tipoImpresion;
	}

	/**
	 * @param tipoImpresion the tipoImpresion to set
	 */
	public void setTipoImpresion(String tipoImpresion) {
		this.tipoImpresion = tipoImpresion;
	}

	/**
	 * @return the listaIngresosSeleccionados
	 */
	public String getListaIngresosSeleccionados() {
		return listaIngresosSeleccionados;
	}

	/**
	 * @param listaIngresosSeleccionados the listaIngresosSeleccionados to set
	 */
	public void setListaIngresosSeleccionados(String listaIngresosSeleccionados) {
		this.listaIngresosSeleccionados = listaIngresosSeleccionados;
	}

	/**
	 * @return the listaDtoHc
	 */
	public List<DtoIngresoHistoriaClinica> getListaDtoHc() {
		return listaDtoHc;
	}

	/**
	 * @param listaDtoHc the listaDtoHc to set
	 */
	public void setListaDtoHc(List<DtoIngresoHistoriaClinica> listaDtoHc) {
		this.listaDtoHc = listaDtoHc;
	}
	
	/**
	 * @return the esBusquedaResumen
	 */
	public boolean isEsBusquedaResumen() {
		return esBusquedaResumen;
	}

	/**
	 * @param esBusquedaResumen the esBusquedaResumen to set
	 */
	public void setEsBusquedaResumen(boolean esBusquedaResumen) {
		this.esBusquedaResumen = esBusquedaResumen;
	}

	/**
	 * @return the ingresoSelecccionado
	 */
	public InfoIngresoDto getIngresoSelecccionado() {
		return ingresoSelecccionado;
	}

	/**
	 * @param ingresoSelecccionado the ingresoSelecccionado to set
	 */
	public void setIngresoSelecccionado(InfoIngresoDto ingresoSelecccionado) {
		this.ingresoSelecccionado = ingresoSelecccionado;
	}

	public String getImpresionResumenHC() {
		return impresionResumenHC;
	}

	public void setImpresionResumenHC(String impresionResumenHC) {
		this.impresionResumenHC = impresionResumenHC;
	}

	public String getCodigoManual() {
		return codigoManual;
	}

	public void setCodigoManual(String codigoManual) {
		this.codigoManual = codigoManual;

	}

	public boolean isImpresionPop() {
		return isImpresionPop;
	}

	public void setImpresionPop(boolean isImpresionPop) {
		this.isImpresionPop = isImpresionPop;
	}	

	
		
}
