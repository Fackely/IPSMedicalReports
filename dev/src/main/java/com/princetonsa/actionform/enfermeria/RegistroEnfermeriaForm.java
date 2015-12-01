/*
 * Creado en Feb 17, 2006
 */
package com.princetonsa.actionform.enfermeria;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.enfermeria.DtoFrecuenciaCuidadoEnferia;
import com.princetonsa.dto.enfermeria.DtoRegistroAlertaOrdenesMedicas;
import com.princetonsa.dto.historiaClinica.DtoValoracion;
import com.princetonsa.dto.ordenesmedicas.DtoPrescripDialFechaHora;
import com.princetonsa.dto.ordenesmedicas.DtoPrescripcionDialisis;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.Valoraciones;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * @author Andrï¿½s Mauricio Ruiz Vï¿½lez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public class RegistroEnfermeriaForm extends ValidatorForm
{
	/**
	 * Serial Verion UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private transient Logger logger = Logger.getLogger(RegistroEnfermeriaForm.class);
	
	/**
	 * Estado para el manejo del flujo de la funcionalidad
	 */
	private String estado;
	
	//-----------------------------------------------------------------------------------------------------------------------------------------------------------//
	
	/**
	 * Nï¿½mero de identificaciï¿½n del paciente
	 */
	private String numeroId;
	
	/**
	 * Tipo de identificaciï¿½n del paciente
	 */
	private String tipoId;
	
	/**
	 * Campo que guarda si el registro de enfermerï¿½a permite modificaciï¿½n,
	 * o es sï¿½lo de consulta, de acuerdo a la ocupaciï¿½n del profesional de la salud.
	 */
	private boolean esConsulta;
	
	/**
	 * Codigo del registro de enfermeria
	 */
	private int codigoRegEnfer;
	
	private String esResumenAtenciones ;
	
	
	private String fechaInicio="";
	
	
	//*************ATRIBUTOS DESTINADOS PARA EL FLUJO DE REGISTRO DE ENFERMERï¿½A POR ï¿½REA*******************************
	/**
	 * Centro de costo que se selecciona en la opciï¿½n registro enfermeria por centro de costo
	 */
	private int centroCostoSeleccionado;
	private String codigoPiso;
	private String codigoHabitacion;
	private String codigoCama;
	private String tipoRompimiento;
	
	private HashMap<String, Object> areas = new HashMap<String, Object>();
	private ArrayList<HashMap<String, Object>> pisos = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> habitaciones = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> camas = new ArrayList<HashMap<String,Object>>();
	
	//******************************************************************************************************************
	
	
//	---------------------------------------------------DECLARACIï¿½N DE LOS ATRIBUTOS-----------------------------------------------------------//
	
	
	
	/**
	 * Fecha del registro de enfermerï¿½a
	 */
	private String fechaRegistro;
	
	/**
	 * Fecha del registro de enfermerï¿½a
	 */
	private String horaRegistro;
	
	/**
	 * Nombre del tipo de monitoreo
	 */
	private String nombreTipoMonitoreo;
	
	
	/**
	 * 
	 */
	private ArrayList<Object> resultadoLaboratorios;
	
	/**
	 * 
	 */
	private ArrayList<Object> resultadoLaboratoriosHistoricos;
	
	
	private String ancla;
	
	
	/**
	 * 
	 */
	private int posPagerHistoricosVE;
	
	/**
	 * 
	 */
	private int posPagerHistoricos;
	

	
	/**
	 * 
	 */
	private ArrayList<Object> valoracionEnfermeria;

	
	/**
	 * 
	 */
	private ArrayList<Object> valoracionEnfermeriaHistoricos;
	
	/**
	 * 
	 */
	private String etiquetaCampoOtro;
	
	
	/**
	 * 
	 */
	private String etiquetaCampoOtroResLab;

	
	
	/**
	 * Tipo de monitoreo
	 */
	private int tipoMonitoreo;
	
	/**
	 * Manejo de los diagnosticos de enfermerï¿½a
	 */
	private HashMap diagnosticosNanda;
	
	/**
	 * Descripciï¿½n del soporte respiratorio de la orden mï¿½dica
	 */
	private String descripcionSoporteOrdenMedica;

	/**
	 * Manejo de los histï¿½ricos de soporte respiratorio
	 */
	private HashMap soporteRespiratorio;

	/**
	 * Manejo de los histï¿½ricos de soporte respiratorio
	 */
	private Collection fechasSoporteRespiratorio;

	/**
	 * Manejo de los histï¿½ricos anteriores de soporte respiratorio
	 */
	private HashMap anterioresSoporteRespiratorio;

	/**
	 * Manejo de los histï¿½ricos anteriores de soporte respiratorio por ordenes
	 */
	private HashMap anterioresSoporteRespiratorioOrden;

	/**
	 * Manejo de los histï¿½ricos de Diagnï¿½sticos Nanda
	 */
	private Collection fechasNanda;

	/**
	 * Manejo de los histï¿½ricos anteriores de Diagnï¿½sticos Nanda
	 */
	private HashMap anterioresDiagnosticosNandaHistorico;

	/**
	 * Fecha para la consulta del ver anteriores de soporte respiratorio
	 */
	private String fechaAnterioresSoporte;

	/**
	 * Fecha para la consulta del ver anteriores de soporte respiratorio
	 */
	private String fechaAnterioresNanda;

	/**
	 * Manejo de los diagnosticos de enfermerï¿½a
	 */
	private HashMap diagnosticosNandaHistorico;

	/**
	 * Manejo de los histï¿½ricos de soporte respiratorio
	 */
	private HashMap soporteRespiratorioHistorico;

	 
    //--------------------------------------------------------------------------------------------------------------------------//
	/**
     * Nï¿½mero de registros por pager
     */
    private int maxPageItems;
	
	/** 
	 * Mapa para manejar la consulta de los pacientes que estï¿½n en una cama
	 * para el centro de costo seleccionado  
	 */
	private HashMap mapaConsultaPacientesCentroCosto;
	
	 /**
     * Variables para manejar el paginador de la consulta de pacientes del centro de costo
     */
    private int index;
	private int pager;
    private int offset;
    private String linkSiguiente;
    
    /**
     * Allmacena el indice por el cual 
     * se va ordenar el HashMap
     */
    private String patronOrdenar;
    
    /**
     * Almacena el ultimo indice por el 
     * cual se ordeno el HashMap
     */
    private String ultimoPatron;
    
    //---------------------------------Campos de la secciï¿½n Signos Vitales---------------------------------//
    /**
     * Campo frecuencia cardiaca
     */
    private String frecuenciaCardiaca;
    
    /**
     * Campo frecuencia respiratoria
     */
    private String frecuenciaRespiratoria;
    
    /**
     * Campo Presiï¿½n Arterial Sistï¿½lica
     */
    private String presionArterialSistolica;
    
    /**
     * Campo Presiï¿½n Arterial Diastï¿½lica
     */
    private String presionArterialDiastolica;
    
    /**
     * Campo Presiï¿½n Arterial Media
     */
    private String presionArterialMedia;
    
    /**
     * Campo Temperatura del paciente
     */
    private String temperaturaPaciente;
    
    /**
     * Coleccion para traer el Listado de tipos Signos Vitales parametrizados en enfermerï¿½a
     * por centro de costo instituciï¿½n    
     */
    private Collection signosVitalesInstitucionCcosto;
    
    /**
     * Mapa para almacenar la informacion de los signos vitales parametrizados por 
     * instituciï¿½n centro de costo
     */
    private HashMap mapaSignosVitales;
    
    /**
     * Coleccion para traer el listado histï¿½rico de los signos vitales fijos de la secciï¿½n
     * de acuerdo a la hora inicio,fin del turno y la hora del sistema  
     */
    private Collection signosVitalesFijosHisto;
    
    /**
     * Collecciï¿½n para traer el listado histï¿½rico de los signos vitales parametrizados por 
     * instituciï¿½n centro de costo de acuerdo a la hora inicio,fin del turno y la hora del sistema  
     */
    private Collection signosVitalesParamHisto;
    
    /**
     * Collecciï¿½n para traer el listado con los cï¿½digos histï¿½ricos, fecha registro y hora registro,
     * de los signos vitales fijos y parametrizados  
     */
    private Collection signosVitalesHistoTodos;
    
    /**
     * Collecciï¿½n para traer las fechas histï¿½ricas de los signos vitales fijos y parametrizados
     */
    private Collection fechasHistoSignosVitales;
    
    /**
     * Campo para guardar la fecha seleccionada en el listado de ver anteriores en los signos vitales
     * y poder asï¿½ consultar los registros que hay en la fecha
     */
    private String fechaVerAnterioresSVital;
    
    /**
     * Collection para traer el listado de los signos vitales fijos que estï¿½n en el rango de acuerdo a 
     * la fecha seleccionada en ver anteriores
     */
    private Collection signosVitalesFijosFechaAnt;
    
    /**
     * Collection para traer el listado de los signos vitales parametrizados por instituciï¿½n
     * centro de costo que estï¿½n en el rango de acuerdo a la fecha seleccionada en ver anteriores
     */
    private Collection signosVitalesParamFechaAnt;
    
    /**
     *Collecciï¿½n para traer el listado con los cï¿½digos histï¿½ricos, fecha registro y hora registro,
     * de los signos vitales fijos y parametrizados para un rango especï¿½fico de fechas
     */
    private Collection signosVitalesTodosFechaAnt;
    
    
    //------------------Campos de la Seccion Dieta ----------------------------------------------------------//
    /**
     * Campo de chequeo para determinar si el paciente tiene nutriciï¿½n oral
     */
    private boolean hayDieta;

    /**
     * Campo de chequeo para determinar si el paciente tiene nutriciï¿½n oral
     */
    private String nutricionOral;
      
    /**
	 * Campo de chequeo para determinar si el paciente tiene nutriciï¿½n oral
	 */
    private String tiposNutricionOral;
      
    /**
     * Campo de chequeo para determinar si el paciente tiene nutriciï¿½n parenteral
     */
    private String nutricionParenteral;

    /**
	 * Campo para almacenar la descripcion de la dieta del paciente que se registro desde ordenes medicas
	 */
    private String descripcionDieta;
    
    /**
     * Mapa para almacenar la informacion de dieta, de los medicamentos.
     */
    private HashMap mapaDieta;

    /**
     * Mapa para almacenar la informacion histï¿½rica de dieta.
     */
    private HashMap mapaDietaHistorico;
    
    /**
     * Mapa para almacenar la informacion histï¿½rica de ORDENES MEDICAS.
     */
    private HashMap mapaDietaOrdenes;
   
    /**
     * Campo para determinar donde esta el paginador de medicamentos administrados (no parametrizados).
     */
    private int paginadorMedAdm;
    
    /**
     * Campo para determinar donde esta el paginador de medicamentos administrados (parametrizados y no parametrizados).
     */

    private int paginadorLiqAdmin;

    /**
     * Campo para determinar donde esta el paginador de medicamentos eliminados (parametrizados).
     */

    private int paginadorLiqElim;
    
    /**
     * Campo para determinar cuando se finalizara un turno de enfermeria en el momento
     * de guardar informaciï¿½n.
     */

    private boolean finalizarTurno;
    
    /**
     * Coleccion para traer el Listado de los historicos de nutricion Oral  DESDE ORDEN MEDICA. 
     */
     private Collection listadoNutOralHisto;

    
    //-------------------Campos de la secciï¿½n Cuidados Especiales de Enfermerï¿½a ----------------------------------//

    /**
     * Colecciï¿½n para traer el Listado de los cuidados especiales parametrizados en enfermerï¿½a
     * por centro de costo instituciï¿½n    
     */
    private Collection colCuidadosEspecialesInstitucionCcosto;

    /**
     * Mapa para el manejo de los cuidados especiales de enfermerï¿½a
     */
    private HashMap mapaCuidadosEspeciales;
    
    /**
     * Mapa para el manejo de los cuidados especiales de enfermerï¿½a
     */
    private HashMap mapaCuidadosEspecialesHistorico;
    
    /**
     * Mapa que almacena la informaciï¿½n del mï¿½dico y enfermera ingresados en 
     * los cuidados especiales tanto en la orden mï¿½dica como en el registro de enfermerï¿½a
     */
    private HashMap mapaColsCuidadosEspeciales;
    
    /**
     * Mapa que almacena la informaciï¿½n del mï¿½dico y enfermera ingresados en 
     * los cuidados especiales tanto en la orden mï¿½dica como en el registro de enfermerï¿½a
     */
    private HashMap mapaColsCuidadosEspecialesHistorico;

    /**
     * 
     */
    private HashMap mapaColsCuiEspHistoricoFechas;
    
    /**
     * Indice para el recorrido del juego de informaciï¿½n de los cuidados
     * especiales de enfermerï¿½a 
     */
    private int indiceCuidadoEnf;
    
    /**
     * Almacena los tipos de frecuencias
     * */
    private ArrayList<HashMap<String,Object>> arrayTipoFrecuencias;
    
    /**
     * Almacen la informacion de las frecuencias de cuidados de enfermeria almacenados
     * */
    private ArrayList<DtoFrecuenciaCuidadoEnferia> arrayFrecuenciasCuidadoEnfer;
    
    //---------------------------------Campos de la secciï¿½n Soporte Respiratorio---------------------------------//
    /**
     * Coleccion para traer el Listado de los tipos de soportes respiratorios parametrizados en enfermerï¿½a
     * por centro de costo instituciï¿½n    
     */
    private Collection soportesRespiratorioInstitucionCcosto;

    /**
     * Coleccion para traer el Listado de las opciones de los tipos de soportes respiratorios parametrizados en enfermerï¿½a
     * por centro de costo instituciï¿½n    
     */
    private Collection opcionesSoportesRespiratorioInstitucionCcosto;

    //---------------------------------Campos de la secciï¿½n Catï¿½teres y Sondas---------------------------------//
    /**
     * Coleccion para traer el Listado de las columnas de catï¿½teres y sonda parametrizados en enfermerï¿½a
     * por centro de costo instituciï¿½n    
     */
    private Collection colCateteresSondaInstitucionCcosto;
    
    /**
     * Mapa para guardar la informaciï¿½n de los catï¿½teres y sondas ingresados
     * tanto para las columnas fijas como las parametrizadas
     */
    private HashMap mapaCateterSonda;
    
    /**
     * Colecciï¿½n para traer los articulos (insumos) que hacen parte de catï¿½teres y sondas
     * parametrizados por instituciï¿½n
     */
    private Collection articulosCateterSondaIns;
    
    /**
     * Coleccion para traer los insumos solicitados al paciente a travï¿½s de solicitud de
     * medicamentos que hacen parte de catï¿½teres y sondas que no se encuentran anuladas
     * y se encuentran ya despachadas
     */
    private Collection articulosDespachadosCatSonda;
    
    /**
     * Coleccion para traer el listado histï¿½rico de los cateter sonda fijos de la secciï¿½n
    */
    private Collection cateterSondaFijosHisto;
    
    /**
     * Collecciï¿½n para traer el listado histï¿½rico de los cateter sonda parametrizados por 
     * instituciï¿½n centro de costo  
     */
    private Collection cateterSondaParamHisto;
    
    /**
     * Collecciï¿½n para traer el listado con los cï¿½digos histï¿½ricos, fecha registro y hora registro,
     * de los catï¿½teres sonda fijos y parametrizados  
     */
    private Collection cateterSondaHistoTodos;
    
    /**
     * Mapa para guardar la informaciï¿½n histï¿½rica de los cateteres sonda tanto fijos como
     * parametrizados ordenados por fecha inserciï¿½n
     */
    private HashMap mapaHistoricoCateterSonda;
    
    //---------------------------------Campos de la secciï¿½n Exï¿½menes Fisicos---------------------------------//
    /**
     * Coleccion para traer el Listado de tipos de Exï¿½menes Fï¿½sicos parametrizados en enfermerï¿½a
     * por centro de costo instituciï¿½n    
     */
    private Collection examenesFisicosInstitucionCcosto;
    
    /**
     * Mapa para almacenar la informacion de los exï¿½menes fï¿½sicos parametrizados por 
     * instituciï¿½n centro de costo
     */
    private HashMap mapaExamenesFisicos;
    
    /**
     * Variable que indica si se insertï¿½ algï¿½n dato en la secciï¿½n exï¿½menes fï¿½sicos
     */
    private boolean insertaronExamenesFisicos;
    
    //  ---------------------------------Campos de la secciï¿½n Anotaciones de Enfermerï¿½a---------------------------------//
    /**
     * Campo que guarda la anotaciï¿½n de enfermerï¿½a
     */
    private String anotacionEnfermeria;
    
    /**
     * Collection que guarda el listado de las anotaciones de enfermerï¿½a
     * ingresadas en el registro de enfermerï¿½a
     */
    private Collection historicoAnotacionesEnfermeria;

    /**
     * Collection que guarda el listado de la toma de muestras
     */
    private HashMap historicoTomaMuestras;

	/**
     * Coleccion que contiene las fechas de las anotaciones de enfermeria.
     */
    private Collection historicoAnotacionesEnfermeriaFechas;
    
    /**
     * Manejo de la advertencia de las anotaciones de enfermerï¿½a
     */
	private String mensajeAnotaciones;
    
    //  -------------------------Observaciones Generales -------------------------------------------------//
	/**
	 * Observaciones Generales de la Orden Mï¿½dica
	 */
	private String observacionesOrdenMedica;
	
	//--------------------------------------Campo de la secciï¿½n Hoja Neurolï¿½gica ---------------------------------------------//
	/**
	 * Campo que indica si seleccionaron Si o No en Hoja Neurolï¿½gica en la funcionalidad
	 * Orden Mï¿½dica
	 */
	private String existeHojaNeurologica; 
	
	/**
	 * Campo que indica si estï¿½ finalizada la hoja neurolï¿½gica desde la orden mï¿½dica
	 */
	private String finalizadaHojaNeurologica;
	
	/**
	 * Campo que guarda la fecha de finalizaciï¿½n de la hoja neurolï¿½gica desde la orden mï¿½dica
	 */
	private String fechaFinalizacionHNeurologica;
	
	/**
	 * Campo que guarda el nombre del mï¿½dico y registro que finalizï¿½ la hoja neurolï¿½gica 
	 * desde la orden mï¿½dica
	 */
	private String medicoHNeurologica;
	
	/**
	 * Campo que guarda las especialidades del mï¿½dico que finalizï¿½ la hoja neurolï¿½gica 
	 * desde la orden mï¿½dica
	 */
	private String fechaGrabacionHojaNeuro;
	
	//--------------------------------------- Subsecciï¿½n Escala Glasgow ----------------------------------------------------------//
	
	 /**
     * Mapa para almacenar la informacion de la subsecciï¿½n escala glasgow
     */
    private HashMap mapaEscalaGlasgow;
    
    /**
     * Coleccion para traer el Listado las caracterï¿½sticas y especificacione de la escala
     * glasgow parametrizados en enfermerï¿½a por centro de costo instituciï¿½n    
     */
    private Collection especificacionesGlasgowCcIns;
    
    /**
     * Coleccion para traer el histï¿½rico de la escala glasgow    
     */
    private HashMap historicoEscalaGlasgow;
    
    /**
     * Indice para el recorrido del juego de informaciï¿½n de la subsecciï¿½n
     * escala glasgow
     */
    private int indiceEscalaGlasgow;
    
    //---------------------------------------- Fin Subsecciï¿½n Escala Glasgow ----------------------------------------------------------//
	
	//----------Seccion pupilas
	
	/**
	 * Manejo del tamaï¿½o de las pupilas
	 */
	private Collection tamanosPupilas;
	
	/**
	 * Manejo reacciones de las pupilas
	 */
	private Collection reaccionesPupilas;
	
	/**
	 * Manejo ingreso e historico datos pupilas
	 */
	private HashMap mapaPupilas;
	
	/**
	 * Manejo indices histï¿½rico
	 */
	private int indicePupilas;
	
	// --- fin seccion pupilas
    
    //----------------------- Mapa para el manejo de las secciones ------------------------//
    
	private HashMap mapaSecciones;
	
	//------------------------------------------------------------SECCION CONVULSIONES ---------------------------------------------------------------//
	/**
	 * 
	 */
	private String convulsion;
	
	/**
	 * 
	 */
	private String observcionConvulstion;
	
	/**
	 * 
	 */
	private HashMap tiposConvulsiones;
	
	/**
	 * 
	 */
	private HashMap historicoConvulsiones;
	//---------------------------------------------------------FIN SECCION CONVULSIONES ---------------------------------------------------------------//
	
	
	
	//-------------------------------------------------------SECCION DE CONTROL DE ESFINTETRES ------------------------
	/**
	 * Entero con el codigo del control de esfinteres seleccionado
	 */
	private int codigoControlEsfinteres;
	
	/**
	 * String para las observaciones
	 */
	private String observacionControlEsfinteres;
	
	/**
	 * HahMapa con los datos hisotricos
	 */
	private HashMap historicoControlEsfinteres;
	
	/**
	 * Cadena para las observaciones del pop out dinamico
	 */
	private String temporalDinamico;
	
//	---------------------------------------------------FIN DE LA DECLARACIï¿½N DE LOS ATRIBUTOS-----------------------------------------------------------//
	
//	-------------------------------------------------------SECCION DE FUERZA MUSCULA ------------------------
	/**
	 * HahMapa con los datos hisotricos
	 */
	private HashMap mapaFuerzaMuscular;
	
	/**
	 * HashMap con la lectura de los datos nuevos
	 */
	private HashMap mapaNewFuerzaMuscular;
	
	/**
	 * HashMap con los tipos de fuerza muscular
	 */
	private HashMap mapaTiposFuerzaMuscular;
	
	
//	---------------------------------------------------FIN DE LA DECLARACIï¿½N DE LOS ATRIBUTOS-----------------------------------------------------------//

	//-----Informacion de finalizacion desde la orden medica de la dieta.
    
    private String fechaGrabacionDietaOrden; 
    private String fechaRegistroDietaOrden; 
    private String medicoDietaOrden; 
    private String observacionDietaParenteOrden;
    
    //***ATRIBUTOS PARA LA HISTORIA DE ATENCIONES**************
    private String idCuenta;
    private boolean esResumenAt;
    private boolean esconderPYP = false;
    //*********************************************************
	
    
    /**
     * Mapa para almacenar las muestras de laboratorio. 
     */
    private HashMap mapaMuestra;
    
    
    /**
	 * Campo para indicar que se finaliza la dieta por la enfermera
	 */ 
	private boolean finalizarDietaEnfermeria;
	
	private boolean finalizarDietaEnfermeriaAnt;
	
	private String observacionesEnfermeria;
	
	private String nuevaObservDietaEnfermeria;
	
	private String interfazNutricion;
	
	
	/**
	 * Variable para capturar los tipos de dietas en estado activo
	 */
	private HashMap nutricionOralMap;
	
	
	private String suspensionOrdenMedica;
	
	
	//---Cierre y Apertura de Notas--------------------------------------------------
	
	/**
	 * HashMap validacionesCierreAperturaNotasMap
	 * */
	private HashMap validacionesCierreAperturaNotasMap;
	//-------------------------------------------------------------------------------
	//***********ATRIBUTOS SECCION PRESCRIPCION DIALISIS*****************************
	private boolean deboAbrirPrescripcionDialisis;
	private DtoPrescripcionDialisis dialisis;
	private ArrayList<DtoPrescripcionDialisis> historicoDialisis = new ArrayList<DtoPrescripcionDialisis>();
	//*******************************************************************************
		
	private HashMap mapaMezclas;
	
	//********ATRIBUTOS PARA EL LLAMADO DE LOS SIGNOS VITALES DE ENFERMERIA DUMMY************
	
	private String cuentas="";

	
	//***************************************************************************************
	
		//----------------------------------------------------------------
	//*************************************************************************************************
	//Modificado por anexo 779
	
	private ArrayList mensajes = new ArrayList();
	
	/**
	 * Filtro para validar si muestran solo pacientes con nueva información registrada
	 * MT-3438
	 */
	private boolean pacientesNuevaInformacion;
	
	/**
	 * Atributo que contiene las alertas generadas para una cuenta dada teniendo como clave
	 * la sección en la que se debe presentar la alerta MT-3438 
	 */
	private HashMap<Integer, DtoRegistroAlertaOrdenesMedicas> listaAlertasOrdenesMed = 
		new HashMap<Integer, DtoRegistroAlertaOrdenesMedicas>();

	/**
	 * Atributo para indicar que las alertas de las secciones fueron revisadas
	 */
	private boolean revisado;
	
	/**
	 * Fecha de inicio del registro de enfermería para validación al momento de guardar
	 * la alerta del registro.
	 */
	private String FechaInicioRegistro;
	
	/**
	 * Fecha de inicio del registro de enfermería para validación al momento de guardar
	 * la alerta del registro.
	 */
	private String HoraInicioRegistro;
	
	/**
	 * Define si la consulta es por área
	 */
	private boolean consultaXArea; 
	
	/**
	 * 
	 */
	public int getSizeMensajes() {
		return mensajes.size();
	}
	/**
	 * @return the mensajes
	 */
	public ArrayList getMensajes() {
		return mensajes;
	}


	/**
	 * @param mensajes the mensajes to set
	 */
	public void setMensajes(ArrayList mensajes) {
		this.mensajes = mensajes;
	}
	//*****************************************************************************************************
	//----------------------------------------------------------------
	
	
	
	/**
	 * Mï¿½todo para limpiar la clase
	 */
	public void reset()
	{

		this.etiquetaCampoOtro="";
		this.etiquetaCampoOtroResLab="";
		this.valoracionEnfermeria=new ArrayList<Object>();
		this.valoracionEnfermeriaHistoricos=new ArrayList<Object>();
		this.ancla="";
		this.posPagerHistoricos=0;
		this.resultadoLaboratorios=new ArrayList<Object>();
		this.resultadoLaboratoriosHistoricos=new ArrayList<Object>();

		this.codigoRegEnfer = 0;
		this.esResumenAtenciones = "";
		this.fechaInicio="";
		this.fechaRegistro=UtilidadFecha.getFechaActual();
		this.horaRegistro=UtilidadFecha.getHoraActual();
		
		this.numeroId = "";
		this.tipoId = "";
		
		this.nombreTipoMonitoreo="";
		this.tipoMonitoreo=0;
		this.observacionesOrdenMedica="";
		this.descripcionSoporteOrdenMedica ="";
		//this.maxPageItems = 0;
		//this.mapaConsultaPacientesCentroCosto = new HashMap();
		this.patronOrdenar ="";
		this.ultimoPatron = "";
		
		//-------Atributos para el flujo de registro enfermeria por area-----------------
		/*this.centroCostoSeleccionado=-1;
		this.codigoPiso = "";
		this.codigoHabitacion = "";
		this.codigoCama = "";
		this.tipoRompimiento = "";
		this.areas = new HashMap<String, Object>();
		this.pisos = new ArrayList<HashMap<String,Object>>();
		this.habitaciones = new ArrayList<HashMap<String,Object>>();
		this.camas = new ArrayList<HashMap<String,Object>>();*/
		
		//---------------Secciï¿½n Signos Vitales-------------------------//
		this.frecuenciaCardiaca = "";
		this.frecuenciaRespiratoria = "";
		this.presionArterialDiastolica = "";
		this.presionArterialSistolica = "";
		this.presionArterialMedia = "";
		this.temperaturaPaciente = "";
		this.mapaSignosVitales=new HashMap();
		this.fechaVerAnterioresSVital = "";
		this.signosVitalesInstitucionCcosto=new ArrayList();
		this.signosVitalesFijosHisto=new ArrayList();
		this.signosVitalesParamHisto=new ArrayList();
		this.signosVitalesHistoTodos=new ArrayList();
		//this.esConsulta=false;
		
		//---------------Secciï¿½n Dieta---------------------------------//
		this.mapaDieta = new HashMap();
		this.mapaDietaHistorico = new HashMap();
		this.mapaDietaOrdenes = new HashMap();
		this.paginadorMedAdm = 0;
		this.paginadorLiqAdmin = 0;
		this.paginadorLiqElim = 0;
		this.finalizarTurno = false;
		this.mapaDietaHistorico.put("fechaHistoricoDieta","");
		this.mapaDieta.put("medNuevos","1");
		this.finalizarDietaEnfermeria=false;
		this.finalizarDietaEnfermeriaAnt=false;
		this.observacionesEnfermeria="";
		this.nuevaObservDietaEnfermeria="";
		this.interfazNutricion="";
		this.nutricionOralMap = new HashMap();				
		this.suspensionOrdenMedica="";////////-----------------------------------------------variables ANDRES
		
		
		//-------------- Secciï¿½n Catï¿½ter Sonda -----------------------//
		this.mapaCateterSonda = new HashMap();
		this.mapaHistoricoCateterSonda = new HashMap();
		this.colCateteresSondaInstitucionCcosto=new ArrayList();
		this.articulosDespachadosCatSonda=new ArrayList();
		this.cateterSondaFijosHisto=new ArrayList();
		this.cateterSondaParamHisto=new ArrayList();
		this.cateterSondaHistoTodos=new ArrayList();
		
		//---------------Secciï¿½n Exï¿½menes Fï¿½sicos-------------------------//
		this.mapaExamenesFisicos = new HashMap();
		this.insertaronExamenesFisicos = false;
		
		//--------------- Secciï¿½n Anotaciones de enfermeria ------------------//
		this.anotacionEnfermeria = "";
		this.mensajeAnotaciones="";
		this.historicoAnotacionesEnfermeria=new ArrayList();
		this.historicoAnotacionesEnfermeriaFechas=new ArrayList();
		
		//--------------- Ver anteriores de soporte respiratorio -----------------//
		this.anterioresSoporteRespiratorio = new HashMap();
		this.anterioresSoporteRespiratorioOrden = new HashMap();
		this.fechaAnterioresSoporte="";
		
		//---------Secciï¿½n soporte respiratorio -----------//
		this.soporteRespiratorioHistorico=new HashMap();
		this.soporteRespiratorioHistorico.put("numRegistros","0");
		this.soportesRespiratorioInstitucionCcosto=new ArrayList();
		this.soporteRespiratorio=new HashMap();
		this.soporteRespiratorio.put("numRegistros","0");

		//--------------- Ver anteriores Nanda -----------------//
		this.anterioresDiagnosticosNandaHistorico = new HashMap();
		this.fechaAnterioresNanda="";
		
		//----------------- Secciï¿½n Cuidados Especiales de enfermeria --------------//
		this.mapaColsCuidadosEspeciales=new HashMap();
		this.mapaColsCuidadosEspecialesHistorico=new HashMap();
		this.mapaColsCuiEspHistoricoFechas=new HashMap();
		this.mapaCuidadosEspeciales=new HashMap();
		this.mapaCuidadosEspecialesHistorico=new HashMap();
		this.indiceCuidadoEnf=0;		
		
		//---------------- Secciï¿½n Hoja Neurolï¿½gica ------------------------------//
		this.existeHojaNeurologica="";
		this.finalizadaHojaNeurologica = "";
		this.fechaFinalizacionHNeurologica="";
		this.medicoHNeurologica="";
		this.fechaGrabacionHojaNeuro="";
		this.mapaEscalaGlasgow = new HashMap();
		this.indiceEscalaGlasgow=0;
		this.especificacionesGlasgowCcIns=new ArrayList();
		
//		---------------- Secciï¿½n CONVULSION ------------------------------//
		
		this.convulsion="";
		this.observcionConvulstion="";
		this.historicoConvulsiones = new HashMap();
		this.historicoConvulsiones.put("numRegistros","0");
		this.tiposConvulsiones=new HashMap();
		this.tiposConvulsiones.put("numRegistros","0");
		
		//-------------Secciï¿½n CONTROL DE ESFINTERES---------------------//	
		this.codigoControlEsfinteres = ConstantesBD.codigoNuncaValido;
		this.observacionControlEsfinteres = "";
		this.historicoControlEsfinteres =  new HashMap();
		this.historicoControlEsfinteres.put("numRegistros","0");
		this.temporalDinamico = "";
		
		//----fuerza muscular
		this.mapaFuerzaMuscular= new HashMap();
		this.mapaFuerzaMuscular.put("numRegistros","0");
		this.mapaTiposFuerzaMuscular=new HashMap();
		this.mapaTiposFuerzaMuscular.put("numRegistros","0");
		this.mapaNewFuerzaMuscular= new HashMap();
		
		//-- Seccion pupilas --
		this.mapaPupilas=new HashMap();
		this.mapaPupilas.put("numFechas", new Integer(0));
		this.tamanosPupilas=new ArrayList();
		this.reaccionesPupilas=new ArrayList();
		this.indicePupilas=0;
		resetNanda();
		
		//atributos para HISTORIA DE ATENCIONES
		this.idCuenta = "0";
		this.esResumenAt = false;
		this.esconderPYP = false;
		
		this.mapaMuestra = new HashMap();
		
		//Cierre y Apertura de Notas
		this.validacionesCierreAperturaNotasMap = new HashMap();
		
		//Seccion prescripcion dialisis
		this.deboAbrirPrescripcionDialisis = false;
		this.dialisis = new DtoPrescripcionDialisis();
		this.historicoDialisis = new ArrayList<DtoPrescripcionDialisis>();
		
		this.mapaMezclas=new HashMap();
		this.mapaMezclas.put("numRegistros","0");
		
		//this.setPacientesNuevaInformacion(false);
		//this.setListaAlertasOrdenesMed(new HashMap<Integer, DtoRegistroAlertaOrdenesMedicas>());
		this.setRevisado(false);
		this.setFechaInicioRegistro("");
		this.setHoraInicioRegistro("");
	}

	public void resetInterfaz ()
	{
		this.mensajes = new ArrayList();
	}
	
	public void resetBusqueda() {
		this.centroCostoSeleccionado=-1;
		this.codigoPiso = "";
		this.codigoHabitacion = "";
		this.codigoCama = "";
		this.tipoRompimiento = "";
		this.areas = new HashMap<String, Object>();
		this.pisos = new ArrayList<HashMap<String,Object>>();
		this.habitaciones = new ArrayList<HashMap<String,Object>>();
		this.camas = new ArrayList<HashMap<String,Object>>();
		this.maxPageItems = 0;
		this.mapaConsultaPacientesCentroCosto = new HashMap();
		this.setConsultaXArea(false);
		this.setPacientesNuevaInformacion(false);
	}
	
	
	public String getCuentas() {
		return cuentas;
	}


	public void setCuentas(String cuentas) {
		this.cuentas = cuentas;
	}


	public HashMap getMapaMezclas() 
	{
		return mapaMezclas;
	}

	public void setMapaMezclas(HashMap mapaMezclas) 
	{
		this.mapaMezclas = mapaMezclas;
	}

	public Object getMapaMezclas(String key) 
	{
		return mapaMezclas.get(key);
	}

	public void setMapaMezclas(String key,Object value) 
	{
		this.mapaMezclas.put(key, value);
	}

	/**
	 * Mï¿½todo utilizado para cerrar todas
	 * las secciones del jsp
	 */
	public void resetSecciones()
	{
		// Secciones desplegables
		this.mapaSecciones=new HashMap();
		this.mapaSecciones.put("seccionNanda", "false");
		this.mapaSecciones.put("seccionSignosVitales", "false");
		this.mapaSecciones.put("seccionRespiratorio", "false");
		this.mapaSecciones.put("seccionDieta", "false");
		this.mapaSecciones.put("seccionMezclas", "false");
		this.mapaSecciones.put("seccionControlLiquidos", "false");
		this.mapaSecciones.put("seccionCuidados", "false");
		this.mapaSecciones.put("seccionCateter", "false");
		this.mapaSecciones.put("seccionFisico", "false");
		this.mapaSecciones.put("seccionNotas", "false");
		this.mapaSecciones.put("seccionObservaciones", "false");
		this.mapaSecciones.put("seccionHojaNeurologica", "false");
		this.mapaSecciones.put("seccionTomaMuestra", "false");
		this.mapaSecciones.put("seccionPrescripcionDialisis", "false");
	}
	
	/**
	 * Reset especifico para los diagnosticos de enfermerï¿½a
	 *
	 */
	public void resetNanda()
	{
		HashMap diagNandaNuevo=new HashMap();
		diagNandaNuevo.put("numRegistros", "0");
		diagNandaNuevo.put("codigosInsertados", "");
		
		setDiagnosticosNanda(diagNandaNuevo);
		this.diagnosticosNandaHistorico=new HashMap();
		this.diagnosticosNandaHistorico.put("numRegistros","0");
	}
	
	/**
	 * Validate the properties that have been set from this HTTP request, and
	 * return an <code>ActionErrors</code> object that encapsulates any
	 * validation errors that have been found.  If no errors are found, return
	 * <code>null</code> or an <code>ActionErrors</code> object with no recorded
	 * error messages.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		if(estado.equals("guardar"))
		{
		    Connection con=null;
			try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
			catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexiï¿½n"+e.toString());
				}
			
			HttpSession session=request.getSession();	
			PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
			UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			DtoValoracion valoracion=new DtoValoracion();
			
			//----------------------------- Validaciï¿½n del Encabezado del Registro de Enfermerï¿½a-----------------------------------//
			final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
			boolean errorHora=false, validarFechaHora=true;
			String fechaHoraMinima[]={null, null};
			
			//-------Se obtiene el tiempo mï¿½ximo de grabaciï¿½n de registros -----------------//
			String tiempoMaximoGrabacion=ValoresPorDefecto.getTiempoMaximoGrabacion(usuario.getCodigoInstitucionInt());
			
			//-------Si el parï¿½metro estï¿½ configurado se obtiene la fecha y hora mï¿½nima a comparar -----//
			if(UtilidadCadena.noEsVacio(tiempoMaximoGrabacion))
			{
				String hora[]=tiempoMaximoGrabacion.split(":");
				
				int minutos=((Integer.parseInt(hora[0])*60)+Integer.parseInt(hora[1]))*-1;
				
				fechaHoraMinima=UtilidadFecha.incrementarMinutosAFechaHora(UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(), minutos, false);
			}
			else
				validarFechaHora=false;
			
			//---------Validaciï¿½n de la hora de registro-----------//
			if(!UtilidadCadena.noEsVacio(this.horaRegistro))
				{
					errorHora=true;	
					errores.add("Hora Registro vacio", new ActionMessage("errors.required","La Hora de Registro"));
				}
			else if (!UtilidadFecha.validacionHora(this.horaRegistro).puedoSeguir)
				{
				errorHora=true;	
				errores.add("Formato hora invalido", new ActionMessage("errors.formatoHoraInvalido","de Registro"));
				}
			
			//----------Validaciï¿½n de la fecha de registro-------------//
			if (!UtilidadCadena.noEsVacio(this.fechaRegistro))
				{
				errores.add("Fecha Registro vacio", new ActionMessage("errors.required","La Fecha de Registro"));
				}
			else if (!UtilidadFecha.validarFecha(this.fechaRegistro))
				{
				errores.add("Fecha Registro Invalido", new ActionMessage("errors.formatoFechaInvalido", " de Registro"));
				}
			else
				{
					boolean tieneErroresFecha=false;
					//------Fecha registro----------//
					Date fechaRegistro = null;
					
					try 
						{
							fechaRegistro = dateFormatter.parse(this.fechaRegistro);
						}	
					catch (java.text.ParseException e) 
						{
							tieneErroresFecha=true;
						}
					
					if (!tieneErroresFecha)
					{
						//---- Validar que la fecha hora registro no sea superior a la fecha hora actual-----//
						if((UtilidadFecha.conversionFormatoFechaABD(fechaRegistro)+horaRegistro).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+UtilidadFecha.getHoraActual())>0)
						{
							if((UtilidadFecha.conversionFormatoFechaABD(fechaRegistro)).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
							{
								errores.add("fecha", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de Registro", "actual"));
							}
							else if(horaRegistro.compareTo(UtilidadFecha.getHoraActual())>0 && !errorHora)
							{
								errores.add("Hora", new ActionMessage("errors.horaPosteriorAOtraDeReferencia", "de Registro", "actual"));
							}
						}
						
						//---- Validar que la fecha hora registro no sea menor a la fecha hora mï¿½nima calculada de acuerdo al parï¿½metro
						//-----Tiempo mï¿½ximo grabaciï¿½n registros-----//
						if((UtilidadFecha.conversionFormatoFechaABD(fechaRegistro)+horaRegistro).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaHoraMinima[0])+fechaHoraMinima[1])<0 && validarFechaHora)
						{
							if((UtilidadFecha.conversionFormatoFechaABD(fechaRegistro)).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaHoraMinima[0]))<0)
							{
								errores.add("fecha", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "de Registro", fechaHoraMinima[0]));
							}
							else if(horaRegistro.compareTo(fechaHoraMinima[1])<0 && !errorHora)
							{
								errores.add("Hora", new ActionMessage("errors.horaAnteriorAOtraDeReferencia", "de Registro", fechaHoraMinima[1]));
							}
						}
						
						//----Validar que la fecha/hora registro sea mayor a la fecha/hora de valoraciï¿½n cuando el paciente estï¿½ por via de ingreso urgencias u hospitalizaciï¿½n
						if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion || paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
							{
								try
								{	
									//-----------Si existe cuenta de asocio y no hay valoraciï¿½n con el cï¿½digo de la cuenta------------//
									if(paciente.getCodigoCuentaAsocio()!=0 && !UtilidadValidacion.tieneValoraciones(con, paciente.getCodigoCuenta()))
										valoracion = Valoraciones.cargarBase(con,UtilidadValidacion.obtenerNumeroSolicitudPrimeraValoracion(con, paciente.getCodigoCuentaAsocio())+"");
									else
										valoracion = Valoraciones.cargarBase(con,UtilidadValidacion.obtenerNumeroSolicitudPrimeraValoracion(con, paciente.getCodigoCuenta())+"");
									
									if(!valoracion.getNumeroSolicitud().equals(""))
									{
										//--------Se verifica que la fecha/hora de registro sea mayor que la fecha/hora de valoraciï¿½n----------//
										if((UtilidadFecha.conversionFormatoFechaABD(valoracion.getFechaValoracion())+valoracion.getHoraValoracion().substring(0,5)).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaRegistro)+horaRegistro)>0)
											{
												if(UtilidadFecha.conversionFormatoFechaABD(valoracion.getFechaValoracion()).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaRegistro))>0)
													{
														errores.add("fechaHora", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "de Registro", "de la Valoraciï¿½n Inicial"));
													}
												else if(valoracion.getHoraValoracion().substring(0,5).compareTo(horaRegistro)>0 && !errorHora)
													{
														errores.add("fechaHora", new ActionMessage("errors.horaAnteriorAOtraDeReferencia", "de Registro", "de la Valoraciï¿½n Inicial"));
													}
											}
									}
								}
								catch (SQLException e)
								{
									e.printStackTrace();
								}
								
							}//if viaIngrreso=Urgencias u hospitalizaciï¿½n
						//--------El paciente es de via de ingreso consulta externa o ambulatorios--------//
						else
							{
								String fechaApertura = UtilidadValidacion.obtenerFechaAperturaCuenta(con, paciente.getCodigoCuenta());
								String horaApertura = UtilidadValidacion.obtenerHoraAperturaCuenta(con, paciente.getCodigoCuenta());
								
								//---------Se verifica que la fecha/hora de registro sea mayor que la fecha/hora de apertura de la cuenta --------//
								if((UtilidadFecha.conversionFormatoFechaABD(fechaRegistro)+horaRegistro).compareTo(fechaApertura+horaApertura)<0)
									{
										if (UtilidadFecha.conversionFormatoFechaABD(fechaRegistro).compareTo(fechaApertura)<0)
											{
												errores.add("fechaHora", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "de Registro", "de Apertura de la Cuenta"));
											}
										else if(horaRegistro.compareTo(horaApertura)<0 && !errorHora)
											{
												errores.add("fechaHora", new ActionMessage("errors.horaAnteriorAOtraDeReferencia", "de Registro", "de Apertura de la Cuenta"));
											}
									}
							}//else 
					}//if !tieneErroresFecha
				
				}//else		
			//-----------------------------------------------------------------------------------------------------------------------//			
			//validacion para los campos de Cuidados de Enfermeria
			if(!this.colCuidadosEspecialesInstitucionCcosto.isEmpty())
			{
				//validacion de los campos de frecuencia y periodo
				Vector codigosCuidadosEnfer=(Vector) this.getMapaCuidadosEspeciales("codigosCuidadoEnf");
				String valor = "";
				if (codigosCuidadosEnfer != null)
				{
					for(int i=0; i<codigosCuidadosEnfer.size();i++)
					{
						valor = codigosCuidadosEnfer.elementAt(i).toString();
						
						if(!UtilidadTexto.getBoolean(mapaCuidadosEspeciales.get("indicativoControlEspecial_"+valor)+""))
						{
							//Frecuencia y tipo de frecuencia
							if(!(this.mapaCuidadosEspeciales.get("frecuencia_"+valor)+"").trim().equals("") &&
									Utilidades.convertirAEntero(this.mapaCuidadosEspeciales.get("frecuencia_"+valor).toString()) <= 0)
								errores.add("descripcion", new ActionMessage("errors.notEspecific","La frecuencia del Procedimiento ["+this.mapaCuidadosEspeciales.get("descripcion_"+valor)+"] de la Secciï¿½n Cuidados Especiales de Enfermeria debe ser un Valor numerico Mayor a Cero"));
							else if(!(this.mapaCuidadosEspeciales.get("frecuencia_"+valor)+"").trim().equals("") 
									&& Utilidades.convertirAEntero(this.mapaCuidadosEspeciales.get("tipofrecuencia_"+valor).toString()) < 0)
								errores.add("descripcion", new ActionMessage("errors.notEspecific","El Tipo de frecuencia del Procedimiento ["+this.mapaCuidadosEspeciales.get("descripcion_"+valor)+"] de la Secciï¿½n Cuidados Especiales de Enfermeria es Requerido"));
							
							//Periodo y tipo de periodo
							if(!(this.mapaCuidadosEspeciales.get("periodo_"+valor)+"").trim().equals("") &&
									Utilidades.convertirAEntero(this.mapaCuidadosEspeciales.get("periodo_"+valor).toString()) <= 0)
								errores.add("descripcion", new ActionMessage("errors.notEspecific","El Periodo del Procedimiento ["+this.mapaCuidadosEspeciales.get("descripcion_"+valor)+"] de la Secciï¿½n Cuidados Especiales de Enfermeria debe ser un Valor numerico Mayor a Cero"));
							else if(!(this.mapaCuidadosEspeciales.get("periodo_"+valor)+"").trim().equals("") 
									&& Utilidades.convertirAEntero(this.mapaCuidadosEspeciales.get("tipoperiodo_"+valor).toString()) < 0)
								errores.add("descripcion", new ActionMessage("errors.notEspecific","El Tipo de Periodo del Procedimiento ["+this.mapaCuidadosEspeciales.get("descripcion_"+valor)+"] de la Secciï¿½n Cuidados Especiales de Enfermeria es Requerido"));
							
							if(!(this.mapaCuidadosEspeciales.get("periodo_"+valor)+"").trim().equals("") 
									&& Utilidades.convertirAEntero(this.mapaCuidadosEspeciales.get("tipoperiodo_"+valor).toString()) > 0 
										&& ((this.mapaCuidadosEspeciales.get("frecuencia_"+valor)+"").trim().equals("") || Utilidades.convertirAEntero(this.mapaCuidadosEspeciales.get("tipofrecuencia_"+valor).toString()) < 0))
								errores.add("descripcion", new ActionMessage("errors.notEspecific","La informaciï¿½n de la Frecuencia del Procedimiento ["+this.mapaCuidadosEspeciales.get("descripcion_"+valor)+"] de la Secciï¿½n Cuidados Especiales de Enfermeria es Requerida para el ingreso del Periodo"));
							
							if(!(this.mapaCuidadosEspeciales.get("frecuencia_"+valor)+"").trim().equals("") &&
								Utilidades.convertirAEntero(this.mapaCuidadosEspeciales.get("frecuencia_"+valor).toString()) > 0 && 
									!(this.mapaCuidadosEspeciales.get("periodo_"+valor)+"").trim().equals("")	&& 
										Utilidades.convertirAEntero(this.mapaCuidadosEspeciales.get("tipoperiodo_"+valor).toString()) > 0)
							{
								int frecuencia = 0,periodo = 0 ;
								
								frecuencia = Utilidades.convertirAEntero(this.mapaCuidadosEspeciales.get("frecuencia_"+valor).toString());
								periodo = Utilidades.convertirAEntero(this.mapaCuidadosEspeciales.get("periodo_"+valor).toString());
								
								//Pasamos la frecuencia y el periodo a minutos
								if(Utilidades.convertirAEntero(this.mapaCuidadosEspeciales.get("tipofrecuencia_"+valor).toString()) == ConstantesBD.codigoTipoFrecuenciaHoras)
									frecuencia = frecuencia * 60; 
								else if (Utilidades.convertirAEntero(this.mapaCuidadosEspeciales.get("tipofrecuencia_"+valor).toString()) == ConstantesBD.codigoTipoFrecuenciaDias)
									frecuencia = frecuencia * 1440;
								
								if(Utilidades.convertirAEntero(this.mapaCuidadosEspeciales.get("tipoperiodo_"+valor).toString()) == ConstantesBD.codigoTipoFrecuenciaHoras)
									periodo = periodo * 60;
								else if(Utilidades.convertirAEntero(this.mapaCuidadosEspeciales.get("tipoperiodo_"+valor).toString()) == ConstantesBD.codigoTipoFrecuenciaDias)
									periodo = periodo * 1440;
								
								if(periodo < frecuencia)
									errores.add("descripcion", new ActionMessage("errors.notEspecific","La Relaciï¿½n Frecuencia/Tipo Frecuencia debe ser menor a la Relaciï¿½n Periodo/Tipo Periodo en el Procedimiento ["+this.mapaCuidadosEspeciales.get("descripcion_"+valor)+"] de la Secciï¿½n Cuidados Especiales de Enfermeria."));							
							}
						}
																		
					}
				}
			}
			//------------------------------------------------------------------------------------------------------------------------
			
			
			//------------ VALIDACIONES SIGNOS VITALES------------------------------------------------------------------------
			if(!this.frecuenciaCardiaca.equals("")&&Utilidades.convertirAEntero(this.frecuenciaCardiaca)==ConstantesBD.codigoNuncaValido)
			{
				errores.add("",new ActionMessage("errors.integer","El campo F.C (Sección Signos Vitales)"));
			}
			if(!this.frecuenciaRespiratoria.equals("")&&Utilidades.convertirAEntero(this.frecuenciaRespiratoria)==ConstantesBD.codigoNuncaValido)
			{
				errores.add("",new ActionMessage("errors.integer","El campo F.R (Sección Signos Vitales)"));
			}
				
			//-------------------------------------------------------------------------------------------------------------------
			//------VALIDACIONES DE DIETA
			/*
			 * Esta validacion se quita despues de evaluar este cambio
			 * con el ingeniero jose eduardo Arias Doncel
			if ( this.mapaDieta != null && UtilidadCadena.noEsVacio(this.mapaDieta.get("numRegistros")+"") )
		    {
				int nroRegMedNoParam = 0; 
				
				if (UtilidadCadena.noEsVacio(this.mapaDieta.get("numRegistros")+""))
					nroRegMedNoParam=Integer.parseInt( this.mapaDieta.get("numRegistros")+"");
				
				if ( nroRegMedNoParam > 0 )
				{
				   for(int j = 0 ; j < nroRegMedNoParam ; j ++)
					 { 
				   		String des = this.getMapaDieta("descripcion_" + j) + ""; 
				   		String vol = this.getMapaDieta("volumen_total_" + j) + "";
				   		String vel = this.getMapaDieta("velocidad_infusion_" + j) + "";
				   	
				   		
				   			if( des.trim().equals("") )
				   				errores.add("desReq", new ActionMessage("errors.required","Para El Medicamento " + des + " la Descripciï¿½n"));

				   			if( vol.trim().equals("") )
				   				errores.add("desVol", new ActionMessage("errors.required","Para El Medicamento " + des + " el Volumen Total"));
				   			
				   			if( vel.trim().equals("") )
				   				errores.add("desVel", new ActionMessage("errors.required","Para El Medicamento " + des + " la Velocidad de Infusiï¿½n"));
				   		
					 }
				}
		    }	
			 */
			if ( UtilidadCadena.noEsVacio(this.mapaDieta.get("medNuevos")+"") )
		    {
			
				int nroMed = 0;
				//--------Validar que los medicamentos que se insertaran tengan volumen y velocidad de infusion    
				if ( UtilidadCadena.noEsVacio(this.getMapaDieta("medNuevos")+"") )
				{
					nroMed = Integer.parseInt(this.getMapaDieta("medNuevos")+"");  //-Los Medicamentos Nuevos
				}
				//-------------------------------------------------------------------------------------------------------------------//
				//---- Validar los medicamentos nuevos --- Barrer el mapa ( Lï¿½QUIDOS / MEDICAMENTOS INFUSIï¿½N )
				for (int i = 0; i < nroMed; i++)  
				{
					String des = (this.getMapaDieta("med_des_" + i) + "");
					String vol = (this.getMapaDieta("med_vol_" + i) + "");
					String vel = (this.getMapaDieta("med_vel_" + i) + "");
					
			   		if ( !des.trim().equals("") && ( vol.trim().equals("") || vel.trim().equals("") ) ) 
			   		{
			   			if( vol.trim().equals("") )
			   			{
			   				errores.add("volumenRequerido", new ActionMessage("errors.required","Para El Nuevo Medicamento " + des + " el Volumen Total"));
			   			}	
			   			if( vel.trim().equals("") )
			   			{
			   				errores.add("volumenRequerido", new ActionMessage("errors.required","Para El Nuevo Medicamento " + des + " la Velocidad de Infusiï¿½n"));
			   			}
			   		}
			   		
				}
			}			
		//-----------------------------------------------------------------------------------------------------------------------//		
			
		//---------------------------- Validaciï¿½n de los campos de la secciï¿½n cateter sonda -------------------------------------//
		if (this.getMapaCateterSonda("codigosCateterSonda") != null || this.getMapaCateterSonda("codsNuevosCateterSonda")!=null )
		{
			String[] vecCodNuevosCateterSonda=null;
			int numCods=0;
			
			//------Agrego al vector de strings los codigos de los nuevos cateteres -----//
			if (this.getMapaCateterSonda("codsNuevosCateterSonda")!=null)
			{
				String codNuevosCateterSonda = (String) this.getMapaCateterSonda("codsNuevosCateterSonda");	
				vecCodNuevosCateterSonda=codNuevosCateterSonda.split("-");
				numCods=vecCodNuevosCateterSonda.length;
			}	
			
			//------Agrego al vector de strings los codigos de los cateteres histï¿½ricos-----//
			if (this.getMapaCateterSonda("codigosCateterSonda") != null)
			{
				Vector codigosCateterSonda=(Vector) this.getMapaCateterSonda("codigosCateterSonda");
				String tempo[]=new String[(numCods+codigosCateterSonda.size())];
				for(int j=0; j<numCods;j++)
				{
					tempo[j]=vecCodNuevosCateterSonda[j];
				}
				int x=0;
				for (int c=numCods; c<codigosCateterSonda.size()+numCods; c++)
					{
						tempo[c]=codigosCateterSonda.elementAt(x)+"";
						x++;
					}
				vecCodNuevosCateterSonda=tempo;
			}
			
				//Se valida la fecha de inserciï¿½n y retiro
				for (int i=0; i<vecCodNuevosCateterSonda.length; i++)
				{
					int codFilaArticuloNuevo=Integer.parseInt(vecCodNuevosCateterSonda[i]);
										
					//---------Se verifica si se debe realizar la verificaciï¿½n de fecha/hora inserciï¿½n y fecha/hora retiro ----------//
					String validarInsercion="true";
					String validarRetiro="true";
					
					if (UtilidadCadena.noEsVacio(this.getMapaCateterSonda("validarInsercion_"+codFilaArticuloNuevo)+""))
						validarInsercion=this.getMapaCateterSonda("validarInsercion_"+codFilaArticuloNuevo)+"";
					
					if (UtilidadCadena.noEsVacio(this.getMapaCateterSonda("validarRetiro_"+codFilaArticuloNuevo)+""))
						validarRetiro=this.getMapaCateterSonda("validarRetiro_"+codFilaArticuloNuevo)+"";
					
					int codArticuloDespacho=Integer.parseInt(this.getMapaCateterSonda("tipoCateterSonda_"+codFilaArticuloNuevo)+"");
					
					//---------Se verifica que se haya seleccionado el articulo despachado de tipo cateter sonda-------//
					if (codArticuloDespacho != -1)
					{
						String fechaInsercion=this.getMapaCateterSonda("fechaInsercion_"+codFilaArticuloNuevo)+"";
						String horaInsercion=this.getMapaCateterSonda("horaInsercion_"+codFilaArticuloNuevo)+"";
						String fechaRetiro=this.getMapaCateterSonda("fechaRetiro_"+codFilaArticuloNuevo)+"";
						String horaRetiro=this.getMapaCateterSonda("horaRetiro_"+codFilaArticuloNuevo)+"";
						
						
						boolean validarFechaInsercion=true;
						if (UtilidadCadena.noEsVacio(fechaInsercion))
						{
							//---------Se valida el formato de la hora de inserciï¿½n ----------//
							if (UtilidadCadena.noEsVacio(horaInsercion))
								{
									if (!UtilidadFecha.validacionHora(horaInsercion).puedoSeguir)
										{
										//errorHora=true;	
										validarFechaInsercion=false;
										errores.add("Formato hora invalido", new ActionMessage("errors.formatoHoraInvalido","de Inserciï¿½n "+horaInsercion));
										}
								}
							else
								{
								errores.add("Hora Insercion vacio", new ActionMessage("errors.required","La Hora de Inserciï¿½n"));
								validarFechaInsercion=false;
								}
							
							if(!UtilidadFecha.validarFecha(fechaInsercion))
							{
								errores.add("Fecha Insercion", new ActionMessage("errors.formatoFechaInvalido", "de inserciï¿½n "+fechaInsercion));
								validarFechaInsercion=false;
							}
							else
							{
								//---Se verifica que la fecha de inserciï¿½n sea menor o igual a la fecha del sistema
								if (validarFechaInsercion && validarInsercion.equals("true") )
								{
									if((UtilidadFecha.conversionFormatoFechaABD(fechaInsercion)+horaInsercion).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+UtilidadFecha.getHoraActual())>0)
									{
										if((UtilidadFecha.conversionFormatoFechaABD(fechaInsercion)).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
										{
											errores.add("fechaInsercion", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de inserciï¿½n "+fechaInsercion, "actual"));
										}
										else if(horaInsercion.compareTo(UtilidadFecha.getHoraActual())>0 && !errorHora)
										{
											errores.add("HoraInserciï¿½n", new ActionMessage("errors.horaPosteriorAOtraDeReferencia", "de inserciï¿½n "+horaInsercion, "actual"));
										}
										//errores.add("fechaInsercion", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Inserciï¿½n "+fechaInsercion, "actual"));
									}
									
									String fechaAperturaCuenta=UtilidadValidacion.obtenerFechaAperturaCuenta(con, paciente.getCodigoCuenta());
									String horaAperturaCuenta=UtilidadValidacion.obtenerHoraAperturaCuenta(con, paciente.getCodigoCuenta());
									//--- Se verifica que la fecha de inserciï¿½n sea mayor o igual que la fecha de apertura de la cuenta
									if((UtilidadFecha.conversionFormatoFechaABD(fechaInsercion)+horaInsercion).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaAperturaCuenta)+horaAperturaCuenta)<0)
									{
										if((UtilidadFecha.conversionFormatoFechaABD(fechaInsercion)).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaAperturaCuenta))<0)
										{
											errores.add("fechaInsercion", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "de inserciï¿½n "+fechaInsercion, "de apertura de la cuenta "+UtilidadFecha.conversionFormatoFechaAAp(fechaAperturaCuenta)));
										}
										else if(horaInsercion.compareTo(UtilidadFecha.getHoraActual())<0)
										{
											errores.add("HoraInserciï¿½n", new ActionMessage("errors.horaAnteriorAOtraDeReferencia", "de inserciï¿½n "+horaInsercion, "de apertura de la cuenta "+horaAperturaCuenta));
										}
										//errores.add("fechaInsercion", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "Inserciï¿½n "+fechaInsercion, "de apertura de la cuenta"));
									}
									
									//---- Validar que la fecha hora inserciï¿½n no sea menor a la fecha hora mï¿½nima calculada de acuerdo al parï¿½metro
									//-----Tiempo mï¿½ximo grabaciï¿½n registros-----//
									if((UtilidadFecha.conversionFormatoFechaABD(fechaInsercion)+horaInsercion).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaHoraMinima[0])+fechaHoraMinima[1])<0 && validarFechaHora)
									{
										if((UtilidadFecha.conversionFormatoFechaABD(fechaInsercion)).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaHoraMinima[0]))<0)
										{
											errores.add("fecha", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "de inserciï¿½n "+fechaInsercion, fechaHoraMinima[0]));
										}
										else if(horaInsercion.compareTo(fechaHoraMinima[1])<0 && !errorHora)
										{
											errores.add("Hora", new ActionMessage("errors.horaAnteriorAOtraDeReferencia", "de inserciï¿½n "+horaInsercion, fechaHoraMinima[1]));
										}
									}
									
								}//if validarFechaInsercion
							}//else
						}//if fecha insercion
						else
						{
							validarFechaInsercion=false;
							//---------Si la fecha inserciï¿½n es vacï¿½a y se ingreso hora de inserciï¿½n es requerida la fecha de inserciï¿½n
							if (UtilidadCadena.noEsVacio(horaInsercion))
								{
								errores.add("Fecha Insercion vacio", new ActionMessage("errors.required","La Fecha de Inserciï¿½n"));
								}
						}
						
						boolean errorHoraRetiro=false;
						//--------------Se valida la fecha de retiro ----------------------------//
						if (UtilidadCadena.noEsVacio(fechaRetiro) && validarRetiro.equals("true"))
						{
							//---------Se valida el formato de la hora de inserciï¿½n ----------//
							if (UtilidadCadena.noEsVacio(horaRetiro))
								{
									if (!UtilidadFecha.validacionHora(horaRetiro).puedoSeguir)
										{
										//errorHora=true;	
										errores.add("Formato hora invalido", new ActionMessage("errors.formatoHoraInvalido","de retiro "+horaRetiro));
										errorHoraRetiro=true;
										}

									//-Validar que la hora de retiro no sea mayor a la actual (o del servidor).
									if((UtilidadFecha.conversionFormatoFechaABD(horaRetiro)).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getHoraActual()))>0)
									{
										errores.add("fecha", new ActionMessage("errors.horaPosteriorAOtraDeReferencia", " Retiro ", UtilidadFecha.getHoraActual()));
									}
									
									//---------Se valida que se haya ingresado fecha inserciï¿½n y hora inserciï¿½n ya que hay fechaRetiro y horaRetiro
									if (!UtilidadCadena.noEsVacio(fechaInsercion) && !UtilidadCadena.noEsVacio(horaInsercion))
										{
										errores.add("Fecha Insercion vacio", new ActionMessage("errors.required","La Fecha de Inserciï¿½n"));
										errores.add("Hora Insercion vacio", new ActionMessage("errors.required","La Hora de Inserciï¿½n"));
										}
								}
							else
								{
								errores.add("Hora Retiro vacio", new ActionMessage("errors.required","La Hora de Retiro"));
								errorHoraRetiro=true;
								}
							
							if(!UtilidadFecha.validarFecha(fechaRetiro))
							{
								errores.add("Fecha Retiro", new ActionMessage("errors.formatoFechaInvalido", "de retiro "+fechaRetiro));
							}
							else
							{
								//---Se verifica que la fecha de retiro sea menor o igual a la fecha del sistema
								if((UtilidadFecha.conversionFormatoFechaABD(fechaRetiro)).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
								{
									errores.add("fechaRetiro", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de retiro "+fechaRetiro, "actual"));
								}
							
								//Si el formato de la fecha de inserciï¿½n es correcto se realiza la verificaciï¿½n
								if (validarFechaInsercion)
								{
								//---Se verifica que la fecha de retiro sea mayor o igual que la fecha de inserciï¿½n
									if((UtilidadFecha.conversionFormatoFechaABD(fechaRetiro)+horaRetiro).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaInsercion)+horaInsercion)<0)
									{
										if((UtilidadFecha.conversionFormatoFechaABD(fechaRetiro)).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaInsercion))<0)
										{
											errores.add("fechaRetiro", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "de retiro "+fechaRetiro, "de insercion "+fechaInsercion));
										}
										else if(horaRetiro.compareTo(horaInsercion)<0 && !errorHoraRetiro)
										{
											errores.add("HoraRetiro", new ActionMessage("errors.horaAnteriorAOtraDeReferencia", "de retiro "+horaRetiro, "de inserciï¿½n "+horaInsercion));
										}
										//errores.add("fechaRetiro", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "Retiro "+fechaRetiro, "Insercion "+fechaInsercion));
									}
									
									//---- Validar que la fecha hora retiro no sea menor a la fecha hora mï¿½nima calculada de acuerdo al parï¿½metro
									//-----Tiempo mï¿½ximo grabaciï¿½n registros-----//
									if((UtilidadFecha.conversionFormatoFechaABD(fechaRetiro)+horaRetiro).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaHoraMinima[0])+fechaHoraMinima[1])<0 && validarFechaHora)
									{
										if((UtilidadFecha.conversionFormatoFechaABD(fechaRetiro)).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaHoraMinima[0]))<0)
										{
											errores.add("fecha", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "de retiro "+fechaRetiro, fechaHoraMinima[0]));
										}
										else if(horaRetiro.compareTo(fechaHoraMinima[1])<0 && !errorHoraRetiro)
										{
											errores.add("Hora", new ActionMessage("errors.horaAnteriorAOtraDeReferencia", "de retiro "+horaRetiro, fechaHoraMinima[1]));
										}
									}
								}
							}//else
						}//if fechaRetiro !=vacï¿½o
					else
						{
							//---------Si la fecha retiro es vacï¿½a y se ingreso hora de retiro es requerida la fecha de retiro
							if (UtilidadCadena.noEsVacio(horaRetiro) && validarRetiro.equals("true"))
							{
							errores.add("Fecha Retiro vacio", new ActionMessage("errors.required","La Fecha de Retiro"));
							}
						}//else
						
					}//if tipoCateter != -1
				}//for
			}//if codsNuevosCateterSonda!=null

			//---------------------------- Fin de la validaciï¿½n de los campos de la secciï¿½n cateter sonda -------------------------------------//

			//-- Validar la informacion de toma de muestras 
			int numRows = util.UtilidadCadena.vInt(this.getMapaMuestra("numRegistros")+"");
			boolean hayError = false;
			String fechaActual = UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()) + " " + UtilidadFecha.getHoraActual();
		    for(int j=0; j<numRows;j++) 
		    	{
		    	   if ( UtilidadTexto.getBoolean(this.getMapaMuestra("tm_" +j)+"") )
		    	   {
			    		String fecha=this.getMapaMuestra("fecha_ing_"+j)+"";
			    		String 	hora=this.getMapaMuestra("hora_ing_"+j)+"";
			    		
			    		
			    		String fecha_solicitud = this.getMapaMuestra("fecha_"+j)+"";
			    		fecha_solicitud =  UtilidadFecha.conversionFormatoFechaABD( fecha_solicitud.split(" ")[0] ) + " " + fecha_solicitud.split(" ")[1];
			    		
			    		if (!UtilidadCadena.noEsVacio(fecha))
			    		{
			    			errores.add("Fecha Vacia", new ActionMessage("errors.required"," La Fecha de Muestra "));
							hayError = true;
			    		}		
						
			    		if (!UtilidadCadena.noEsVacio(hora))
			    		{
			    			errores.add("Hora Vacia", new ActionMessage("errors.required"," La Hora de Muestra "));
							hayError = true;
			    		}
			    		else		
			    		{
				    		if(!UtilidadFecha.validarFecha(fecha))
							{
								errores.add("Fecha de Muestra", new ActionMessage("errors.formatoFechaInvalido", " de Muestra "+fecha));
								hayError = true;
							}
				    		if (!UtilidadFecha.validacionHora(hora).puedoSeguir)
							{
				    			errores.add("Formato hora Muestra", new ActionMessage("errors.formatoHoraInvalido"," de Muestra "+hora));
								hayError = true;
							}
				    		
				    		if (!hayError)
				    		{
				    			String fechaAux = UtilidadFecha.conversionFormatoFechaABD(fecha) + " " +hora; 
				    			//--- Validar que la fecha ingresada sea mayor a la de la solicitud  y menor a la hora fecha actual.
								if( fechaAux.compareTo(fecha_solicitud)<0  )
								{
									errores.add("fech Invalida", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", " de Muestra " +fechaAux," de Solicitud "+ fecha_solicitud ));
								}
								
								if(fechaAux.compareTo(fechaActual)>0)
								{
									errores.add("fech Invalida", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", " de Muestra " + fechaAux, " Actual "));
								}
				    		}
			    		}	
			    		
		    	   }
		    	}
		    
		    //**************VALIDACIONES PRESCRIPCION DIALISIS***************************************************
		    if(this.deboAbrirPrescripcionDialisis)
		    {
		    	String fechaSistema = UtilidadFecha.getFechaActual(con);
		    	String horaSistema = UtilidadFecha.getHoraActual(con);
		    	//Validacion de fecha/horqa inicio diï¿½lisis -------------------------------------------
		    	boolean fechaInicialValida = false, horaInicioValida = false, encontro = false;
		    	String fechaInicioDialisis = "", horaInicioDialisis = "";
		    	//Se toma la fecha/hora inicio dialisis que se editaba
		    	for(DtoPrescripDialFechaHora fechaHora:this.dialisis.getFechasHorasIniciales())
		    		if(fechaHora.getConsecutivo().equals(""))
		    		{
		    			fechaInicioDialisis = fechaHora.getFechaInicialDialisis();
		    			horaInicioDialisis = fechaHora.getHoraInicialDialisis();
		    			encontro = true;
		    		}
		    	
		    		
	    		if(!fechaInicioDialisis.equals(""))
	    		{
	    			if(!UtilidadFecha.validarFecha(fechaInicioDialisis))
	    				errores.add("", new ActionMessage("errors.formatoFechaInvalido","inicio diï¿½lisis (Prescripciï¿½n Diï¿½lisis)"));
	    			else
	    				fechaInicialValida = true;
	    			if(horaInicioDialisis.equals(""))
	    				errores.add("", new ActionMessage("errors.required","La hora inicio diï¿½lisis (Prescripciï¿½n Diï¿½lisis)"));
	    			
	    		}
	    		
	    		if(!horaInicioDialisis.equals(""))
	    		{
	    			if(!UtilidadFecha.validacionHora(horaInicioDialisis).puedoSeguir)
	    				errores.add("", new ActionMessage("errors.formatoHoraInvalido","inicio diï¿½lisis (Prescripciï¿½n Diï¿½lisis)"));
	    			else
	    				horaInicioValida = true;
	    			if(fechaInicioDialisis.equals(""))
	    				errores.add("", new ActionMessage("errors.required","La fecha inicio diï¿½lisis (Prescripciï¿½n Diï¿½lisis)"));
	    		}
	    		
	    		if(fechaInicialValida&&horaInicioValida)
	    		{
	    			if(!UtilidadFecha.compararFechas(fechaSistema, horaSistema,fechaInicioDialisis, horaInicioDialisis).isTrue())
	    				errores.add("", new ActionMessage("errors.fechaHoraPosteriorIgualActual","inicio diï¿½lisis (Prescripciï¿½n Diï¿½lisis)","del sistema: "+fechaSistema+" - "+horaSistema));
	    			if(!UtilidadFecha.compararFechas(fechaInicioDialisis, horaInicioDialisis, this.dialisis.getFechaOrden(), this.dialisis.getHoraOrden()).isTrue())
	    				errores.add("", new ActionMessage("errors.fechaHoraAnteriorIgualActual","inicio diï¿½lisis (Prescripciï¿½n Diï¿½lisis)","de la orden: "+this.dialisis.getFechaOrden()+" - "+this.dialisis.getHoraOrden()));
	    			
	    		}
	    		
	    		//Si no es encontrï¿½ fecha/hora inicio dialisis editable se toma la primera registrada  del historico
	    		if(!encontro&&this.dialisis.getFechasHorasIniciales().size()>0)
	    		{
	    			fechaInicioDialisis = this.dialisis.getFechasHorasIniciales().get(this.dialisis.getFechasHorasIniciales().size()-1).getFechaInicialDialisis();
	    			fechaInicialValida = true;
	    			horaInicioDialisis = this.dialisis.getFechasHorasIniciales().get(this.dialisis.getFechasHorasIniciales().size()-1).getHoraInicialDialisis();
	    			horaInicioValida = true;
	    		}
		    	
		    	
		    	//Validacion de la fecha/hora final diï¿½lisis -------------------------------------------------------
		    	boolean fechaFinalValida = false, horaFinalValida = false;
		    	if(!this.dialisis.isFinalizado()&&this.dialisis.isManejoFinalizado())
		    	{
		    		//Se verifia que se haya ingresado fecha hora inicio
		    		if(!this.dialisis.isIngresadoFechaHoraInicio())
		    			errores.add("",new ActionMessage("errors.minimoCampos","un ingreso de fecha/hora inicio dialisis","finalizaciï¿½n (Prescripciï¿½n Diï¿½lisis)"));
		    		
		    		if(!this.dialisis.getFechaFinalDialisis().equals(""))
		    		{
		    			if(!UtilidadFecha.validarFecha(this.dialisis.getFechaFinalDialisis()))
		    				errores.add("", new ActionMessage("errors.formatoFechaInvalido","fin diï¿½lisis (Prescripciï¿½n Diï¿½lisis)"));
		    			else
		    				fechaFinalValida = true;
		    			if(this.dialisis.getHoraFinalDialisis().equals(""))
		    				errores.add("", new ActionMessage("errors.required","La hora fin diï¿½lisis (Prescripciï¿½n Diï¿½lisis)"));
		    		}
		    		
		    		if(!this.dialisis.getHoraFinalDialisis().equals(""))
		    		{
		    			if(!UtilidadFecha.validacionHora(this.dialisis.getHoraFinalDialisis()).puedoSeguir)
		    				errores.add("", new ActionMessage("errors.formatoHoraInvalido","fin diï¿½lisis (Prescripciï¿½n Diï¿½lisis)"));
		    			else
		    				horaFinalValida = true;
		    			if(this.dialisis.getFechaFinalDialisis().equals(""))
		    				errores.add("", new ActionMessage("errors.required","La fecha fin diï¿½lisis (Prescripciï¿½n Diï¿½lisis)"));
		    		}
		    		
		    		if(fechaFinalValida&&horaFinalValida)
		    		{
		    			if(!UtilidadFecha.compararFechas(fechaSistema, horaSistema,this.dialisis.getFechaFinalDialisis(), this.dialisis.getHoraFinalDialisis()).isTrue())
		    				errores.add("", new ActionMessage("errors.fechaHoraPosteriorIgualActual","fin diï¿½lisis (Prescripciï¿½n Diï¿½lisis)","del sistema: "+fechaSistema+" - "+horaSistema));
		    			
		    			if(fechaInicialValida&&horaInicioValida)
		    				if(!UtilidadFecha.compararFechas(this.dialisis.getFechaFinalDialisis(), this.dialisis.getHoraFinalDialisis(),fechaInicioDialisis, horaInicioDialisis).isTrue())
			    				errores.add("", new ActionMessage("errors.fechaHoraAnteriorIgualActual","fin diï¿½lisis (Prescripciï¿½n Diï¿½lisis)","inicio diï¿½lisis "));
		    			
		    			
		    		}
		    		
		    	}
		    	
		    	if(this.dialisis.getModalidadTerapia().equals(ConstantesIntegridadDominio.acronimoHemodialisis))
		    	{
		    		//Se verifica si se modificï¿½ el peso Seco
		    		if(!this.dialisis.getHemodialisis().get(0).getPesoSeco().equals("")&&
		    				!this.dialisis.getHemodialisis().get(0).getPesoSeco().equals(this.dialisis.getHemodialisis().get(0).getPesoSecoAnterior()))
		    		{
		    			if(Utilidades.convertirADouble(this.dialisis.getHemodialisis().get(0).getPesoSeco())>=0)
							errores = DtoPrescripcionDialisis.validacionDigitosDecimales(this.dialisis.getHemodialisis().get(0).getPesoSeco(), errores, "El campo Peso seco (Prescripciï¿½n Diï¿½lisis)");
						else
							errores.add("", new ActionMessage("errors.float","El campo Peso seco (Prescripciï¿½n Diï¿½lisis)"));
		    		}
		    	}
		    }
		    //***************************************************************************************************

			if(!errores.isEmpty())
				this.estado = "";
		    
			try
			{
				UtilidadBD.cerrarConexion(con);
			} 
			catch (SQLException e1)
			{
				logger.warn("No se pudo cerrar la conexiï¿½n"+e1.toString());
			}	
		}//if estado guardar
			
				
		return errores;
	}
	
//	-------------------------------------------------------------SETS Y GETS----------------------------------------------------------------//

	
	
	
	/**
	 * @return Returns the estado.
	 */
	public String getEstado()
	{
		return estado;
	}
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}
	/**
	 * @return Returns the numeroId.
	 */
	public String getNumeroId()
	{
		return numeroId;
	}
	/**
	 * @param numeroId The numeroId to set.
	 */
	public void setNumeroId(String numeroId)
	{
		this.numeroId = numeroId;
	}
	/**
	 * @return Returns the tipoId.
	 */
	public String getTipoId()
	{
		return tipoId;
	}
	/**
	 * @param tipoId The tipoId to set.
	 */
	public void setTipoId(String tipoId)
	{
		this.tipoId = tipoId;
	}
	/**
	 * @return Returns the centroCostoSeleccionado.
	 */
	public int getCentroCostoSeleccionado()
	{
		return centroCostoSeleccionado;
	}
	/**
	 * @param centroCostoSeleccionado The centroCostoSeleccionado to set.
	 */
	public void setCentroCostoSeleccionado(int centroCostoSeleccionado)
	{
		this.centroCostoSeleccionado = centroCostoSeleccionado;
	}

	/**
	 * @return Retorna nombreTipoMonitoreo.
	 */
	public String getNombreTipoMonitoreo()
	{
		return nombreTipoMonitoreo;
	}

	/**
	 * @param nombreTipoMonitoreo Asigna nombreTipoMonitoreo.
	 */
	public void setNombreTipoMonitoreo(String nombreTipoMonitoreo)
	{
		this.nombreTipoMonitoreo = nombreTipoMonitoreo;
	}

	/**
	 * @return Retorna observacionesOrdenMedica.
	 */
	public String getObservacionesOrdenMedica()
	{
		return observacionesOrdenMedica;
	}

	/**
	 * @param observaciones Asigna observaciones.
	 */
	public void setObservacionesOrdenMedica(String observacionesOrdenMedica)
	{
		this.observacionesOrdenMedica = observacionesOrdenMedica;
	}

	/**
	 * @return Retorna tipoMonitoreo.
	 */
	public int getTipoMonitoreo()
	{
		return tipoMonitoreo;
	}

	/**
	 * @param tipoMonitoreo Asigna tipoMonitoreo.
	 */
	public void setTipoMonitoreo(int tipoMonitoreo)
	{
		this.tipoMonitoreo = tipoMonitoreo;
	}

	/**
	 * @return Retorna diagnosticosNanda.
	 */
	public HashMap getDiagnosticosNanda()
	{
		return diagnosticosNanda;
	}

	/**
	 * @param diagnosticosNanda Asigna diagnosticosNanda.
	 */
	public void setDiagnosticosNanda(HashMap diagnosticosNanda)
	{
		this.diagnosticosNanda = diagnosticosNanda;
	}

	/**
	 * @return Returns the maxPageItems.
	 */
	public int getMaxPageItems()
	{
		return maxPageItems;
	}
	/**
	 * @param maxPageItems The maxPageItems to set.
	 */
	public void setMaxPageItems(int maxPageItems)
	{
		this.maxPageItems = maxPageItems;
	}
	/**
	 * @return Returns the mapaConsultaPacientesCentroCosto.
	 */
	public HashMap getMapaConsultaPacientesCentroCosto()
	{
		return mapaConsultaPacientesCentroCosto;
	}
	/**
	 * @param mapaConsultaPacientesCentroCosto The mapaConsultaPacientesCentroCosto to set.
	 */
	public void setMapaConsultaPacientesCentroCosto(
			HashMap mapaConsultaPacientesCentroCosto)
	{
		this.mapaConsultaPacientesCentroCosto = mapaConsultaPacientesCentroCosto;
	}
	
	/**
	 * @return Returns the mapaConsultaPacientesCentroCosto.
	 */
	public Object getMapaConsultaPacientesCentroCosto(String key)
	{
		return mapaConsultaPacientesCentroCosto.get(key);
	}
	/**
	 * @param mapaConsultaPacientesCentroCosto The mapaConsultaPacientesCentroCosto to set.
	 */
	public void setMapaConsultaPacientesCentroCosto(String key,Object obj)
	{
		this.mapaConsultaPacientesCentroCosto.put(key,obj);
	}
	
	/**
	 * @return Returns the index.
	 */
	public int getIndex()
	{
		return index;
	}
	/**
	 * @param index The index to set.
	 */
	public void setIndex(int index)
	{
		this.index = index;
	}
	/**
	 * @return Returns the linkSiguiente.
	 */
	public String getLinkSiguiente()
	{
		return linkSiguiente;
	}
	/**
	 * @param linkSiguiente The linkSiguiente to set.
	 */
	public void setLinkSiguiente(String linkSiguiente)
	{
		this.linkSiguiente = linkSiguiente;
	}
	/**
	 * @return Returns the offset.
	 */
	public int getOffset()
	{
		return offset;
	}
	/**
	 * @param offset The offset to set.
	 */
	public void setOffset(int offset)
	{
		this.offset = offset;
	}
	/**
	 * @return Returns the pager.
	 */
	public int getPager()
	{
		return pager;
	}
	/**
	 * @param pager The pager to set.
	 */
	public void setPager(int pager)
	{
		this.pager = pager;
	}
	/**
	 * @return Returns the patronOrdenar.
	 */
	public String getPatronOrdenar()
	{
		return patronOrdenar;
	}
	/**
	 * @param patronOrdenar The patronOrdenar to set.
	 */
	public void setPatronOrdenar(String patronOrdenar)
	{
		this.patronOrdenar = patronOrdenar;
	}
	/**
	 * @return Returns the ultimoPatron.
	 */
	public String getUltimoPatron()
	{
		return ultimoPatron;
	}
	
	
	
	/**
	 * @return Returns the esResumenAtenciones.
	 */
	public String getEsResumenAtenciones()
	{
		return esResumenAtenciones;
	}

	/**
	 * @param esResumenAtenciones The esResumenAtenciones to set.
	 */
	public void setEsResumenAtenciones(String esResumenAtenciones)
	{
		this.esResumenAtenciones=esResumenAtenciones;
	}

	/**
	 * @param ultimoPatron The ultimoPatron to set.
	 */
	public void setUltimoPatron(String ultimoPatron)
	{
		this.ultimoPatron = ultimoPatron;
	}
	
	/**
	 * @return Retorna diagnosticosNandaHistorico.
	 */
	public HashMap getDiagnosticosNandaHistorico()
	{
		return diagnosticosNandaHistorico;
	}

	/**
	 * @param diagnosticosNandaHistorico Asigna diagnosticosNandaHistorico.
	 */
	public void setDiagnosticosNandaHistorico(HashMap diagnosticosNandaHistorico)
	{
		this.diagnosticosNandaHistorico = diagnosticosNandaHistorico;
	}

	/**
	 * @return Retorna tiposNutricionOral.
	 */
	public String getTiposNutricionOral() {
		return tiposNutricionOral;
	}
	/**
	 * @param Asigna tiposNutricionOral.
	 */
	public void setTiposNutricionOral(String tiposNutricionOral) {
		this.tiposNutricionOral = tiposNutricionOral;
	}
	/**
	 * @return Retorna descripcionDieta.
	 */
	public String getDescripcionDieta() {
		return descripcionDieta;
	}
	/**
	 * @param Asigna descripcionDieta.
	 */
	public void setDescripcionDieta(String descripcionDieta) {
		this.descripcionDieta = descripcionDieta;
	}

	/**
	 * @return Retorna fechaRegistro.
	 */
	public String getFechaRegistro()
	{
		return fechaRegistro;
	}

	/**
	 * @param fechaRegistro Asigna fechaRegistro.
	 */
	public void setFechaRegistro(String fechaRegistro)
	{
		this.fechaRegistro = fechaRegistro;
	}
	/**
	 * @return Returns the frecuenciaCardiaca.
	 */
	public String getFrecuenciaCardiaca()
	{
		return frecuenciaCardiaca;
	}
	/**
	 * @param frecuenciaCardiaca The frecuenciaCardiaca to set.
	 */
	public void setFrecuenciaCardiaca(String frecuenciaCardiaca)
	{
		this.frecuenciaCardiaca = frecuenciaCardiaca;
	}
	/**
	 * @return Returns the frecuenciaRespiratoria.
	 */
	public String getFrecuenciaRespiratoria()
	{
		return frecuenciaRespiratoria;
	}
	/**
	 * @param frecuenciaRespiratoria The frecuenciaRespiratoria to set.
	 */
	public void setFrecuenciaRespiratoria(String frecuenciaRespiratoria)
	{
		this.frecuenciaRespiratoria = frecuenciaRespiratoria;
	}
	/**
	 * @return Returns the presionArterialDiastolica.
	 */
	public String getPresionArterialDiastolica()
	{
		return presionArterialDiastolica;
	}
	/**
	 * @param presionArterialDiastolica The presionArterialDiastolica to set.
	 */
	public void setPresionArterialDiastolica(String presionArterialDiastolica)
	{
		this.presionArterialDiastolica = presionArterialDiastolica;
	}
	/**
	 * @return Returns the presionArterialMedia.
	 */
	public String getPresionArterialMedia()
	{
		return presionArterialMedia;
	}
	/**
	 * @param presionArterialMedia The presionArterialMedia to set.
	 */
	public void setPresionArterialMedia(String presionArterialMedia)
	{
		this.presionArterialMedia = presionArterialMedia;
	}
	/**
	 * @return Returns the presionArterialSistolica.
	 */
	public String getPresionArterialSistolica()
	{
		return presionArterialSistolica;
	}
	/**
	 * @param presionArterialSistolica The presionArterialSistolica to set.
	 */
	public void setPresionArterialSistolica(String presionArterialSistolica)
	{
		this.presionArterialSistolica = presionArterialSistolica;
	}
	/**
	 * @return Returns the temperaturaPaciente.
	 */
	public String getTemperaturaPaciente()
	{
		return temperaturaPaciente;
	}
	/**
	 * @param temperaturaPaciente The temperaturaPaciente to set.
	 */
	public void setTemperaturaPaciente(String temperaturaPaciente)
	{
		this.temperaturaPaciente = temperaturaPaciente;
	}
	/**
	 * @return Returns the signosVitalesInstitucionCcosto.
	 */
	public Collection getSignosVitalesInstitucionCcosto()
	{
		return signosVitalesInstitucionCcosto;
	}
	/**
	 * @param signosVitalesInstitucionCcosto The signosVitalesInstitucionCcosto to set.
	 */
	public void setSignosVitalesInstitucionCcosto(
			Collection signosVitalesInstitucionCcosto)
	{
		this.signosVitalesInstitucionCcosto = signosVitalesInstitucionCcosto;
	}
	/**
	 * @return Retorna horaRegistro.
	 */
	public String getHoraRegistro() {
		return horaRegistro;
	}
	/**
	 * @param Asigna horaRegistro.
	 */
	public void setHoraRegistro(String horaRegistro) {
		this.horaRegistro = horaRegistro;
	}
	
	/**
	 * @return Retorna mapaDieta.
	 */
	public HashMap getMapaDieta() {
		return mapaDieta;
	}
	/**
	 * @param Asigna mapaDieta.
	 */
	public void setMapaDieta(HashMap mapaDieta) {
		this.mapaDieta = mapaDieta;
	}
	/**
	 * @return Retorna mapaDieta.
	 */
	public Object getMapaDieta(Object key) {
		return mapaDieta.get(key);
	}
	/**
	 * @param Asigna mapaDieta.
	 */
	public void setMapaDieta(Object key, Object dato) {
		this.mapaDieta.put(key, dato);
	}

	/**
	 * @return Retorna mapaDietaHistorico.
	 */
	public HashMap getMapaDietaHistorico() {
		return mapaDietaHistorico;
	}

	/**
	 * @param Asigna mapaDietaHistorico.
	 */
	public void setMapaDietaHistorico(HashMap mapaDietaHistorico) {
		this.mapaDietaHistorico = mapaDietaHistorico;
	}

	/** 
	 * @return Retorna mapaDieta.
	 */
	public Object getMapaDietaHistorico(Object key) {
		return mapaDietaHistorico.get(key);
	}
	/**
	 * @param Asigna mapaDieta.
	 */
	public void setMapaDietaHistorico(Object key, Object dato) {
		this.mapaDietaHistorico.put(key, dato);
	}
	
	
	/**
	 * @return Retorna mapaSecciones.
	 */
	public HashMap getMapaSecciones()
	{
		return mapaSecciones;
	}

	/**
	 * @param mapaSecciones Asigna mapaSecciones.
	 */
	public void setMapaSecciones(HashMap mapaSecciones)
	{
		this.mapaSecciones = mapaSecciones;
	}
	
	/**
	 * @return Retorna mapaSecciones.
	 */
	public Object getMapaSecciones(Object key) {
		return mapaSecciones.get(key);
	}
	/**
	 * @param Asigna mapaSecciones.
	 */
	public void setMapaSecciones(Object key, Object dato) {
		this.mapaSecciones.put(key, dato);
	}
	
	/**
	 * @return Retorna mapaSignosVitales.
	 */
	public HashMap getMapaSignosVitales() {
		return mapaSignosVitales;
	}
	/**
	 * @param Asigna mapaSignosVitales.
	 */
	public void setMapaSignosVitales(HashMap mapaSignosVitales) {
		this.mapaSignosVitales = mapaSignosVitales;
	}
	/**
	 * @return Retorna mapaSignosVitales.
	 */
	public Object getMapaSignosVitales(Object key) {
		return mapaSignosVitales.get(key);
	}
	/**
	 * @param Asigna mapaSignosVitales.
	 */
	public void setMapaSignosVitales(Object key, Object dato) {
		this.mapaSignosVitales.put(key, dato);
	}
	
		/**
	 * @return Retorna hayDieta.
	 */
	public boolean getHayDieta() {
		return hayDieta;
	}
	/**
	 * @param Asigna hayDieta.
	 */
	public void setHayDieta(boolean hayDieta) {
		this.hayDieta = hayDieta;
	}
	/**
	 * @return Retorna nutricionOral.
	 */
	public String getNutricionOral() {
		return nutricionOral;
	}
	/**
	 * @param Asigna nutricionOral.
	 */
	public void setNutricionOral(String nutricionOral) {
		this.nutricionOral = nutricionOral;
	}
	/**
	 * @return Retorna nutricionParenteral.
	 */
	public String getNutricionParenteral() {
		return nutricionParenteral;
	}
	/**
	 * @param Asigna nutricionParenteral.
	 */
	public void setNutricionParenteral(String nutricionParenteral) {
		this.nutricionParenteral = nutricionParenteral;
	}
	
	/**
	 * @return Retorna mapaExamenesFisicos.
	 */
	public HashMap getMapaExamenesFisicos() {
		return mapaExamenesFisicos;
	}
	/**
	 * @param Asigna mapaExamenesFisicos.
	 */
	public void setMapaExamenesFisicos(HashMap mapaExamenesFisicos) {
		this.mapaExamenesFisicos = mapaExamenesFisicos;
	}
	/**
	 * @return Retorna mapaExamenesFisicos.
	 */
	public Object getMapaExamenesFisicos(Object key) {
		return mapaExamenesFisicos.get(key);
	}
	/**
	 * @param Asigna dato.
	 */
	public void setMapaExamenesFisicos(Object key, Object dato) {
		this.mapaExamenesFisicos.put(key, dato);
	}

	
	/**
	 * @return Returns the examenesFisicosInstitucionCcosto.
	 */
	public Collection getExamenesFisicosInstitucionCcosto()
	{
		return examenesFisicosInstitucionCcosto;
	}
	/**
	 * @param examenesFisicosInstitucionCcosto The examenesFisicosInstitucionCcosto to set.
	 */
	public void setExamenesFisicosInstitucionCcosto(
			Collection examenesFisicosInstitucionCcosto)
	{
		this.examenesFisicosInstitucionCcosto = examenesFisicosInstitucionCcosto;
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
	 * @return Returns the soportesRespiratorioInstitucionCcosto.
	 */
	public Collection getSoportesRespiratorioInstitucionCcosto()
	{
		return soportesRespiratorioInstitucionCcosto;
	}
	/**
	 * @param soportesRespiratorioInstitucionCcosto The soportesRespiratorioInstitucionCcosto to set.
	 */
	public void setSoportesRespiratorioInstitucionCcosto(
			Collection soportesRespiratorioInstitucionCcosto)
	{
		this.soportesRespiratorioInstitucionCcosto = soportesRespiratorioInstitucionCcosto;
	}
	/**
	 * @return Retorna opcionesSoportesRespiratorioInstitucionCcosto.
	 */
	public Collection getOpcionesSoportesRespiratorioInstitucionCcosto()
	{
		return opcionesSoportesRespiratorioInstitucionCcosto;
	}
	/**
	 * @param opcionesSoportesRespiratorioInstitucionCcosto Asigna opcionesSoportesRespiratorioInstitucionCcosto.
	 */
	public void setOpcionesSoportesRespiratorioInstitucionCcosto(
			Collection opcionesSoportesRespiratorioInstitucionCcosto)
	{
		this.opcionesSoportesRespiratorioInstitucionCcosto = opcionesSoportesRespiratorioInstitucionCcosto;
	}
	/**
	 * @return Returns the anotacionEnfermeria.
	 */
	public String getAnotacionEnfermeria()
	{
		return anotacionEnfermeria;
	}
	/**
	 * @param anotacionEnfermeria The anotacionEnfermeria to set.
	 */
	public void setAnotacionEnfermeria(String anotacionEnfermeria)
	{
		this.anotacionEnfermeria = anotacionEnfermeria;
	}
	/**
	 * @return Returns the historicoAnotacionesEnfermeria.
	 */
	public Collection getHistoricoAnotacionesEnfermeria()
	{
		return historicoAnotacionesEnfermeria;
	}
	/**
	 * @param historicoAnotacionesEnfermeria The historicoAnotacionesEnfermeria to set.
	 */
	public void setHistoricoAnotacionesEnfermeria(
			Collection historicoAnotacionesEnfermeria)
	{
		this.historicoAnotacionesEnfermeria = historicoAnotacionesEnfermeria;
	}
	
	/**
	 * @return Retorna paginadorMedAdm.
	 */
	public int getPaginadorMedAdm() {
		return paginadorMedAdm;
	}
	/**
	 * @param Asigna paginadorMedAdm.
	 */
	public void setPaginadorMedAdm(int paginadorMedAdm) {
		this.paginadorMedAdm = paginadorMedAdm;
	}
	
	/**
	 * @return Retorna mapaCateterSonda.
	 */
	public HashMap getMapaCateterSonda() {
		return mapaCateterSonda;
	}
	/**
	 * @param Asigna mapaCateterSonda.
	 */
	public void setMapaCateterSonda(HashMap mapaCateterSonda) {
		this.mapaCateterSonda = mapaCateterSonda;
	}
	/**
	 * @return Retorna mapaCateterSonda.
	 */
	public Object getMapaCateterSonda(Object key) {
		return mapaCateterSonda.get(key);
	}
	/**
	 * @param Asigna dato.
	 */
	public void setMapaCateterSonda(Object key, Object dato) {
		this.mapaCateterSonda.put(key, dato);
	}
	/**
	 * @return Returns the articulosCateterSondaIns.
	 */
	public Collection getArticulosCateterSondaIns()
	{
		return articulosCateterSondaIns;
	}
	/**
	 * @param articulosCateterSondaIns The articulosCateterSondaIns to set.
	 */
	public void setArticulosCateterSondaIns(Collection articulosCateterSondaIns)
	{
		this.articulosCateterSondaIns = articulosCateterSondaIns;
	}
	
	/**
	 * @return Returns the articulosDespachadosCatSonda.
	 */
	public Collection getArticulosDespachadosCatSonda()
	{
		return articulosDespachadosCatSonda;
	}
	/**
	 * @param articulosDespachadosCatSonda The articulosDespachadosCatSonda to set.
	 */
	public void setArticulosDespachadosCatSonda(
			Collection articulosDespachadosCatSonda)
	{
		this.articulosDespachadosCatSonda = articulosDespachadosCatSonda;
	}
	/**
	 * @return Retorna paginadorLiqAdmin.
	 */
	public int getPaginadorLiqAdmin() {
		return paginadorLiqAdmin;
	}
	/**
	 * @param Asigna paginadorLiqAdmin.
	 */
	public void setPaginadorLiqAdmin(int paginadorLiqAdmin) {
		this.paginadorLiqAdmin = paginadorLiqAdmin;
	}
	/**
	 * @return Retorna paginadorLiqElim.
	 */
	public int getPaginadorLiqElim() {
		return paginadorLiqElim;
	}
	/**
	 * @param Asigna paginadorLiqElim.
	 */
	public void setPaginadorLiqElim(int paginadorLiqElim) {
		this.paginadorLiqElim = paginadorLiqElim;
	}
	
	/**
	 * @return Returns the signosVitalesFijosHisto.
	 */
	public Collection getSignosVitalesFijosHisto()
	{
		return signosVitalesFijosHisto;
	}
	/**
	 * @param signosVitalesFijosHisto The signosVitalesFijosHisto to set.
	 */
	public void setSignosVitalesFijosHisto(Collection signosVitalesFijosHisto)
	{
		this.signosVitalesFijosHisto = signosVitalesFijosHisto;
	}
	/**
	 * @return Returns the signosVitalesParamHisto.
	 */
	public Collection getSignosVitalesParamHisto()
	{
		return signosVitalesParamHisto;
	}
	/**
	 * @param signosVitalesParamHisto The signosVitalesParamHisto to set.
	 */
	public void setSignosVitalesParamHisto(Collection signosVitalesParamHisto)
	{
		this.signosVitalesParamHisto = signosVitalesParamHisto;
	}
	/**
	 * @return Returns the signosVitalesHistoTodos.
	 */
	public Collection getSignosVitalesHistoTodos()
	{
		return signosVitalesHistoTodos;
	}
	/**
	 * @param signosVitalesHistoTodos The signosVitalesHistoTodos to set.
	 */
	public void setSignosVitalesHistoTodos(Collection signosVitalesHistoTodos)
	{
		this.signosVitalesHistoTodos = signosVitalesHistoTodos;
	}

	/**
	 * @return Retorna soporteRespiratorioHistorico.
	 */
	public HashMap getSoporteRespiratorioHistorico()
	{
		return soporteRespiratorioHistorico;
	}

	/**
	 * @param soporteRespiratorioHistorico Asigna soporteRespiratorioHistorico.
	 */
	public void setSoporteRespiratorioHistorico(HashMap soporteRespiratorioHistorico)
	{
		this.soporteRespiratorioHistorico = soporteRespiratorioHistorico;
	}
	
	/**
	 * @return Retorna tamaï¿½o de soporteRespiratorioHistorico.
	 */
	public int getSoporteRespiratorioHistoricoSize()
	{
		int numRegistros=Integer.parseInt(soporteRespiratorioHistorico.get("numRegistros")+"");
		return numRegistros;
	}

	/**
	 * @return Retorna tamaï¿½o de soporteRespiratorioHistorico (Ver anteriores).
	 */
	public int getAnterioresSoporteRespiratorioOrdenSize()
	{
		int numRegistros=Integer.parseInt(anterioresSoporteRespiratorioOrden.get("numRegistros")+"");
		return numRegistros;
	}

	/**
	 * @return Retorna soporteRespiratorio.
	 */
	public HashMap getSoporteRespiratorio()
	{
		return soporteRespiratorio;
	}

	/**
	 * @param soporteRespiratorio Asigna soporteRespiratorio.
	 */
	public void setSoporteRespiratorio(HashMap soporteRespiratorio)
	{
		this.soporteRespiratorio = soporteRespiratorio;
	}

	/**
	 * @return Returns the fechasHistoSignosVitales.
	 */
	public Collection getFechasHistoSignosVitales()
	{
		return fechasHistoSignosVitales;
	}
	/**
	 * @param fechasHistoSignosVitales The fechasHistoSignosVitales to set.
	 */
	public void setFechasHistoSignosVitales(Collection fechasHistoSignosVitales)
	{
		this.fechasHistoSignosVitales = fechasHistoSignosVitales;
	}
	/**
	 * @return Returns the fechaVerAnterioresSVital.
	 */
	public String getFechaVerAnterioresSVital()
	{
		return fechaVerAnterioresSVital;
	}
	/**
	 * @param fechaVerAnterioresSVital The fechaVerAnterioresSVital to set.
	 */
	public void setFechaVerAnterioresSVital(String fechaVerAnterioresSVital)
	{
		this.fechaVerAnterioresSVital = fechaVerAnterioresSVital;
	}
	/**
	 * @return Returns the signosVitalesFijosFechaAnt.
	 */
	public Collection getSignosVitalesFijosFechaAnt()
	{
		return signosVitalesFijosFechaAnt;
	}
	/**
	 * @param signosVitalesFijosFechaAnt The signosVitalesFijosFechaAnt to set.
	 */
	public void setSignosVitalesFijosFechaAnt(
			Collection signosVitalesFijosFechaAnt)
	{
		this.signosVitalesFijosFechaAnt = signosVitalesFijosFechaAnt;
	}
	/**
	 * @return Returns the signosVitalesParamFechaAnt.
	 */
	public Collection getSignosVitalesParamFechaAnt()
	{
		return signosVitalesParamFechaAnt;
	}
	
	/**
	 * @param signosVitalesParamFechaAnt The signosVitalesParamFechaAnt to set.
	 */
	public void setSignosVitalesParamFechaAnt(
			Collection signosVitalesParamFechaAnt)
	{
		this.signosVitalesParamFechaAnt = signosVitalesParamFechaAnt;
	}
	
	/**
	 * @return Returns the signosVitalesTodosFechaAnt.
	 */
	public Collection getSignosVitalesTodosFechaAnt()
	{
		return signosVitalesTodosFechaAnt;
	}
	/**
	 * @param signosVitalesTodosFechaAnt The signosVitalesTodosFechaAnt to set.
	 */
	public void setSignosVitalesTodosFechaAnt(
			Collection signosVitalesTodosFechaAnt)
	{
		this.signosVitalesTodosFechaAnt = signosVitalesTodosFechaAnt;
	}
	
	/**
	 * @return Retorna finalizarTurno.
	 */
	public boolean getFinalizarTurno() {
		return finalizarTurno;
	}
	/**
	 * @param Asigna finalizarTurno.
	 */
	public void setFinalizarTurno(boolean finalizarTurno) {
		this.finalizarTurno = finalizarTurno;
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
	 * @return Retorna anterioresSoporteRespiratorio.
	 */
	public HashMap getAnterioresSoporteRespiratorio()
	{
		return anterioresSoporteRespiratorio;
	}

	/**
	 * @param anterioresSoporteRespiratorio Asigna anterioresSoporteRespiratorio.
	 */
	public void setAnterioresSoporteRespiratorio(
			HashMap anterioresSoporteRespiratorio)
	{
		this.anterioresSoporteRespiratorio = anterioresSoporteRespiratorio;
	}

	/**
	 * @return Retorna anterioresSoporteRespiratorioOrden.
	 */
	public HashMap getAnterioresSoporteRespiratorioOrden()
	{
		return anterioresSoporteRespiratorioOrden;
	}

	/**
	 * @param anterioresSoporteRespiratorioOrden Asigna anterioresSoporteRespiratorioOrden.
	 */
	public void setAnterioresSoporteRespiratorioOrden(
			HashMap anterioresSoporteRespiratorioOrden)
	{
		this.anterioresSoporteRespiratorioOrden = anterioresSoporteRespiratorioOrden;
	}

	/**
	 * @return Retorna fechasSoporteRespiratorio.
	 */
	public Collection getFechasSoporteRespiratorio()
	{
		return fechasSoporteRespiratorio;
	}

	/**
	 * @param fechasSoporteRespiratorio Asigna fechasSoporteRespiratorio.
	 */
	public void setFechasSoporteRespiratorio(Collection fechasSoporteRespiratorio)
	{
		this.fechasSoporteRespiratorio = fechasSoporteRespiratorio;
	}

	/**
	 * @return Retorna fechaAnterioresSoporte.
	 */
	public String getFechaAnterioresSoporte()
	{
		return fechaAnterioresSoporte;
	}

	/**
	 * @param fechaAnterioresSoporte Asigna fechaAnterioresSoporte.
	 */
	public void setFechaAnterioresSoporte(String fechaAnterioresSoporte)
	{
		this.fechaAnterioresSoporte = fechaAnterioresSoporte;
	}

	/**
	 * @return Retorna fechaAnterioresNanda.
	 */
	public String getFechaAnterioresNanda()
	{
		return fechaAnterioresNanda;
	}

	/**
	 * @param fechaAnterioresNanda Asigna fechaAnterioresNanda.
	 */
	public void setFechaAnterioresNanda(String fechaAnterioresNanda)
	{
		this.fechaAnterioresNanda = fechaAnterioresNanda;
	}

	/**
	 * @return Retorna anterioresDiagnosticosNandaHistorico.
	 */
	public HashMap getAnterioresDiagnosticosNandaHistorico()
	{
		return anterioresDiagnosticosNandaHistorico;
	}

	/**
	 * @param anterioresDiagnosticosNandaHistorico Asigna anterioresDiagnosticosNandaHistorico.
	 */
	public void setAnterioresDiagnosticosNandaHistorico(
			HashMap anterioresDiagnosticosNandaHistorico)
	{
		this.anterioresDiagnosticosNandaHistorico = anterioresDiagnosticosNandaHistorico;
	}

	/**
	 * @return Retorna fechasNanda.
	 */
	public Collection getFechasNanda()
	{
		return fechasNanda;
	}

	/**
	 * @param fechasNanda Asigna fechasNanda.
	 */
	public void setFechasNanda(Collection fechasNanda)
	{
		this.fechasNanda = fechasNanda;
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

	/**
	 * @return Retorna colCuidadosEspecialesInstitucionCcosto.
	 */
	public Collection getColCuidadosEspecialesInstitucionCcosto()
	{
		return colCuidadosEspecialesInstitucionCcosto;
	}

	/**
	 * @param colCuidadosEspecialesInstitucionCcosto Asigna colCuidadosEspecialesInstitucionCcosto.
	 */
	public void setColCuidadosEspecialesInstitucionCcosto(
			Collection colCuidadosEspecialesInstitucionCcosto)
	{
		this.colCuidadosEspecialesInstitucionCcosto = colCuidadosEspecialesInstitucionCcosto;
	}

	/**
	 * @return Retorna mapaCuidadosEspeciales.
	 */
	public HashMap getMapaCuidadosEspeciales()
	{
		return mapaCuidadosEspeciales;
	}

	/**
	 * @param mapaCuidadosEspeciales Asigna mapaCuidadosEspeciales.
	 */
	public void setMapaCuidadosEspeciales(HashMap mapaCuidadosEspeciales)
	{
		this.mapaCuidadosEspeciales = mapaCuidadosEspeciales;
	}
	/**
	 * @return Returns the descripcionSoporteOrdenMedica.
	 */
	public String getDescripcionSoporteOrdenMedica()
	{
		return descripcionSoporteOrdenMedica;
	}
	/**
	 * @param descripcionSoporteOrdenMedica The descripcionSoporteOrdenMedica to set.
	 */
	public void setDescripcionSoporteOrdenMedica(
			String descripcionSoporteOrdenMedica)
	{
		this.descripcionSoporteOrdenMedica = descripcionSoporteOrdenMedica;
	}
	/**
	 * @return Returns the insertaronExamenesFisicos.
	 */
	public boolean isInsertaronExamenesFisicos()
	{
		return insertaronExamenesFisicos;
	}
	/**
	 * @param insertaronExamenesFisicos The insertaronExamenesFisicos to set.
	 */
	public void setInsertaronExamenesFisicos(boolean insertaronExamenesFisicos)
	{
		this.insertaronExamenesFisicos = insertaronExamenesFisicos;
	}

	/**
	 * @return Retorna mapaColsCuidadosEspeciales.
	 */
	public HashMap getMapaColsCuidadosEspeciales() {
		return mapaColsCuidadosEspeciales;
	}
	/**
	 * @param Asigna mapaColsCuidadosEspeciales.
	 */
	public void setMapaColsCuidadosEspeciales(HashMap mapaColsCuidadosEspeciales) {
		this.mapaColsCuidadosEspeciales = mapaColsCuidadosEspeciales;
	}
	/**
	 * @return Retorna mapaColsCuidadosEspeciales.
	 */
	public Object getMapaColsCuidadosEspeciales(Object key) {
		return mapaColsCuidadosEspeciales.get(key);
	}
	/**
	 * @param Asigna dato.
	 */
	public void setMapaColsCuidadosEspeciales(Object key, Object dato) {
		this.mapaColsCuidadosEspeciales.put(key, dato);
	}

	/**
	 * @return Retorna the indiceCuidadoEnf.
	 */
	public int getIndiceCuidadoEnf()
	{
		return indiceCuidadoEnf;
	}

	/**
	 * @param indiceCuidadoEnf The indiceCuidadoEnf to set.
	 */
	public void setIndiceCuidadoEnf(int indiceCuidadoEnf)
	{
		this.indiceCuidadoEnf = indiceCuidadoEnf;
	}
	
	/**
	 * @return Retorna mapaCuidadosEspeciales.
	 */
	public Object getMapaCuidadosEspeciales(Object key) {
		return mapaCuidadosEspeciales.get(key);
	}
	/**
	 * @param Asigna dato.
	 */
	public void setMapaCuidadosEspeciales(Object key, Object dato) {
		this.mapaCuidadosEspeciales.put(key, dato);
	}

	/**
	 * @return Retorna mensajeAnotaciones.
	 */
	public String getMensajeAnotaciones()
	{
		return mensajeAnotaciones;
	}

	/**
	 * @param mensajeAnotaciones Asigna mensajeAnotaciones.
	 */
	public void setMensajeAnotaciones(String mensajeAnotaciones)
	{
		this.mensajeAnotaciones = mensajeAnotaciones;
	}
	/**
	 * @return Retorna the existeHojaNeurologica.
	 */
	public String getExisteHojaNeurologica()
	{
		return existeHojaNeurologica;
	}

	/**
	 * @param existeHojaNeurologica The existeHojaNeurologica to set.
	 */
	public void setExisteHojaNeurologica(String existeHojaNeurologica)
	{
		this.existeHojaNeurologica = existeHojaNeurologica;
	}

	/**
	 * @return Retorna the finalizadaHojaNeurologica.
	 */
	public String getFinalizadaHojaNeurologica()
	{
		return finalizadaHojaNeurologica;
	}

	/**
	 * @param finalizadaHojaNeurologica The finalizadaHojaNeurologica to set.
	 */
	public void setFinalizadaHojaNeurologica(String finalizadaHojaNeurologica)
	{
		this.finalizadaHojaNeurologica = finalizadaHojaNeurologica;
	}
	
	/**
	 * @return Retorna the fechaGrabacionHojaNeuro.
	 */
	public String getFechaGrabacionHojaNeuro()
	{
		return fechaGrabacionHojaNeuro;
	}

	/**
	 * @param fechaGrabacionHojaNeuro The fechaGrabacionHojaNeuro to set.
	 */
	public void setFechaGrabacionHojaNeuro(
			String especialidadesMedicoHNeurologica)
	{
		this.fechaGrabacionHojaNeuro = especialidadesMedicoHNeurologica;
	}

	/**
	 * @return Retorna the fechaFinalizacionHNeurologica.
	 */
	public String getFechaFinalizacionHNeurologica()
	{
		return fechaFinalizacionHNeurologica;
	}

	/**
	 * @param fechaFinalizacionHNeurologica The fechaFinalizacionHNeurologica to set.
	 */
	public void setFechaFinalizacionHNeurologica(
			String fechaFinalizacionHNeurologica)
	{
		this.fechaFinalizacionHNeurologica = fechaFinalizacionHNeurologica;
	}

	/**
	 * @return Retorna the medicoHNeurologica.
	 */
	public String getMedicoHNeurologica()
	{
		return medicoHNeurologica;
	}

	/**
	 * @param medicoHNeurologica The medicoHNeurologica to set.
	 */
	public void setMedicoHNeurologica(String medicoHNeurologica)
	{
		this.medicoHNeurologica = medicoHNeurologica;
	}

	/**
	 * @return Retorna mapaEscalaGlasgow.
	 */
	public HashMap getMapaEscalaGlasgow() {
		return mapaEscalaGlasgow;
	}
	/**
	 * @param Asigna mapaEscalaGlasgow.
	 */
	public void setMapaEscalaGlasgow(HashMap mapaEscalaGlasgow) {
		this.mapaEscalaGlasgow = mapaEscalaGlasgow;
	}
	/**
	 * @return Retorna mapaEscalaGlasgow.
	 */
	public Object getMapaEscalaGlasgow(Object key) {
		return mapaEscalaGlasgow.get(key);
	}
	/**
	 * @param Asigna dato.
	 */
	public void setMapaEscalaGlasgow(Object key, Object dato) {
		this.mapaEscalaGlasgow.put(key, dato);
	}

	/**
	 * @return Retorna the especificacionesGlasgowCcIns.
	 */
	public Collection getEspecificacionesGlasgowCcIns()
	{
		return especificacionesGlasgowCcIns;
	}

	/**
	 * @param especificacionesGlasgowCcIns The especificacionesGlasgowCcIns to set.
	 */
	public void setEspecificacionesGlasgowCcIns(
			Collection especificacionesGlasgowCcIns)
	{
		this.especificacionesGlasgowCcIns = especificacionesGlasgowCcIns;
	}
	
	/**
	 * @return Retorna the historicoEscalaGlasgow.
	 */
	public HashMap getHistoricoEscalaGlasgow()
	{
		return historicoEscalaGlasgow;
	}

	/**
	 * @param historicoEscalaGlasgow The historicoEscalaGlasgow to set.
	 */
	public void setHistoricoEscalaGlasgow(HashMap historicoEscalaGlasgow)
	{
		this.historicoEscalaGlasgow = historicoEscalaGlasgow;
	}

	/**
	 * @return Retorna the indiceEscalaGlasgow.
	 */
	public int getIndiceEscalaGlasgow()
	{
		return indiceEscalaGlasgow;
	}

	/**
	 * @param indiceEscalaGlasgow The indiceEscalaGlasgow to set.
	 */
	public void setIndiceEscalaGlasgow(int indiceEscalaGlasgow)
	{
		this.indiceEscalaGlasgow = indiceEscalaGlasgow;
	}

	/**
	 * @return Retorna mapaDietaOrdenes
	 */
	public HashMap getMapaDietaOrdenes() {
		return mapaDietaOrdenes;
	}

	/**
	 * @param Asigna mapaDietaHistorico.
	 */
	public void setMapaDietaOrdenes(HashMap mapaOrdenes) {
		this.mapaDietaOrdenes = mapaOrdenes;
	}

	/** 
	 * @return Retorna mapaDieta.
	 */
	public Object getMapaDietaOrdenes(Object key) {
		return mapaDietaOrdenes.get(key);
	}
	/**
	 * @param Asigna mapaDieta.
	 */
	public void setMapaDietaOrdenes(Object key, Object dato) {
		this.mapaDietaOrdenes.put(key, dato);
	}

	public String getConvulsion() {
		return convulsion;
	}

	public void setConvulsion(String convulsion) {
		this.convulsion = convulsion;
	}

	public HashMap getHistoricoConvulsiones() {
		return historicoConvulsiones;
	}

	public void setHistoricoConvulsiones(HashMap historicoConvulsiones) {
		this.historicoConvulsiones = historicoConvulsiones;
	}

	public String getObservcionConvulstion() {
		return observcionConvulstion;
	}

	public void setObservcionConvulstion(String observcionConvulstion) {
		this.observcionConvulstion = observcionConvulstion;
	}

	public HashMap getTiposConvulsiones() {
		return tiposConvulsiones;
	}

	public void setTiposConvulsiones(HashMap tiposConvulsiones) {
		this.tiposConvulsiones = tiposConvulsiones;
	}
	
	
	/**
	 * @return Returns the historicoControlEsfinteres
	 */
	public HashMap getHistoricoControlEsfinteres()
	{
		return historicoControlEsfinteres;
	}
	
	/**
	 * @param historicoControlEsfinteres The historicoControlEsfinteres to set.
	 */
	public void setHistoricoControlEsfinteres(HashMap historicoControlEsfinteres)
	{
		this.historicoControlEsfinteres = historicoControlEsfinteres;
	}
	
	/**
	 * @param key
	 * @return
	 */
	public Object getHistoricoControlEsfinteres(String key) 
	{
		return historicoControlEsfinteres.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setHistoricoControlEsfinteres(String key, Object value) 
	{
		historicoControlEsfinteres.put(key, value);
	}

	/**
	 * @return Returns the codigoControlEsfinteres.
	 */
	public int getCodigoControlEsfinteres()
	{
		return codigoControlEsfinteres;
	}

	/**
	 * @param codigoControlEsfinteres The codigoControlEsfinteres to set.
	 */
	public void setCodigoControlEsfinteres(int codigoControlEsfinteres)
	{
		this.codigoControlEsfinteres=codigoControlEsfinteres;
	}

	/**
	 * @return Returns the observacionControlEsfinteres.
	 */
	public String getObservacionControlEsfinteres()
	{
		return observacionControlEsfinteres;
	}

	/**
	 * @param observacionControlEsfinteres The observacionControlEsfinteres to set.
	 */
	public void setObservacionControlEsfinteres(String observacionControlEsfinteres)
	{
		this.observacionControlEsfinteres=observacionControlEsfinteres;
	}
	
	

	/**
	 * @return Returns the temporalDinamico.
	 */
	public String getTemporalDinamico()
	{
		return temporalDinamico;
	}

	/**
	 * @param temporalDinamico The temporalDinamico to set.
	 */
	public void setTemporalDinamico(String temporalDinamico)
	{
		this.temporalDinamico=temporalDinamico;
	}

	/**
	 * @return Returns the mapaFuerzaMuscular.
	 */
	public HashMap getMapaFuerzaMuscular() {
		return mapaFuerzaMuscular;
	}

	/**
	 * @param mapaFuerzaMuscular The mapaFuerzaMuscular to set.
	 */
	public void setMapaFuerzaMuscular(HashMap mapaFuerzaMuscular) {
		this.mapaFuerzaMuscular = mapaFuerzaMuscular;
	}
	
	/**
	 * @return Returns the mapaFuerzaMuscular.
	 */
	public String getMapaFuerzaMuscular(String key) {
		return mapaFuerzaMuscular.get(key).toString();
	}

	/**
	 * @param mapaFuerzaMuscular The mapaFuerzaMuscular to set.
	 */
	public void setMapaFuerzaMuscular(String key, String valor) {
		this.mapaFuerzaMuscular.put(key, valor); 
	}
	
	/**
	 * @return Returns the mapaFuerzaMuscular.
	 */
	public String getMapaNewFuerzaMuscular(String key) {
		return mapaNewFuerzaMuscular.get(key).toString();
	}

	/**
	 * @param mapaFuerzaMuscular The mapaFuerzaMuscular to set.
	 */
	public void setMapaNewFuerzaMuscular(String key, String valor) {
		this.mapaNewFuerzaMuscular.put(key, valor); 
	}

	/**
	 * @return Returns the mapaNewFuerzaMuscular.
	 */
	public HashMap getMapaNewFuerzaMuscular() {
		return mapaNewFuerzaMuscular;
	}

	/**
	 * @param mapaNewFuerzaMuscular The mapaNewFuerzaMuscular to set.
	 */
	public void setMapaNewFuerzaMuscular(HashMap mapaNewFuerzaMuscular) {
		this.mapaNewFuerzaMuscular = mapaNewFuerzaMuscular;
	}
	
	/**
	 * obtiene el numero de registros del mapa
	 * @return
	 */
	public int getNumeroRegistrosMapaFuerzaMuscular()
	{
		return Integer.parseInt(mapaFuerzaMuscular.get("numRegistros").toString());
	}
	
	/**
	 * @return Returns the mapaFuerzaMuscular.
	 */
	public String getMapaTiposFuerzaMuscular(String key) {
		return mapaTiposFuerzaMuscular.get(key).toString();
	}

	/**
	 * @param mapaFuerzaMuscular The mapaFuerzaMuscular to set.
	 */
	public void setMapaTiposFuerzaMuscular(String key, String valor) {
		this.mapaTiposFuerzaMuscular.put(key, valor); 
	}
	
	// Inicio pupilas
	
	/**
	 * obtiene el numero de registros del mapa
	 * @return
	 */
	public int getNumeroRegistrosMapaTiposFuerzaMuscular()
	{
		return Integer.parseInt(mapaTiposFuerzaMuscular.get("numRegistros").toString());
	}

	/**
	 * @return Retorna tamanosPupilas.
	 */
	public Collection getTamanosPupilas()
	{
		return tamanosPupilas;
	}

	/**
	 * @param tamanosPupilas Asigna tamanosPupilas.
	 */
	public void setTamanosPupilas(Collection tamanosPupilas)
	{
		this.tamanosPupilas = tamanosPupilas;
	}

	/**
	 * @return Retorna reaccionesPupilas.
	 */
	public Collection getReaccionesPupilas()
	{
		return reaccionesPupilas;
	}

	/**
	 * @param reaccionesPupilas Asigna reaccionesPupilas.
	 */
	public void setReaccionesPupilas(Collection reaccionesPupilas)
	{
		this.reaccionesPupilas = reaccionesPupilas;
	}

	/**
	 * @return Retorna mapaPupilas.
	 */
	public HashMap getMapaPupilas()
	{
		return mapaPupilas;
	}

	/**
	 * @param mapaPupilas Asigna mapaPupilas.
	 */
	public void setMapaPupilas(HashMap mapaPupilas)
	{
		this.mapaPupilas = mapaPupilas;
	}

	/**
	 * @return Retorna indicePupilas.
	 */
	public int getIndicePupilas()
	{
		return indicePupilas;
	}

	/**
	 * @param indicePupilas Asigna indicePupilas.
	 */
	public void setIndicePupilas(int indicePupilas)
	{
		this.indicePupilas = indicePupilas;
	}

	// Fin pupilas
	/**
	 * @return Returns the mapaTiposFuerzaMuscular.
	 */
	public HashMap getMapaTiposFuerzaMuscular() {
		return mapaTiposFuerzaMuscular;
	}

	/**
	 * @param mapaTiposFuerzaMuscular The mapaTiposFuerzaMuscular to set.
	 */
	public void setMapaTiposFuerzaMuscular(HashMap mapaTiposFuerzaMuscular) {
		this.mapaTiposFuerzaMuscular = mapaTiposFuerzaMuscular;
	}

	/**
	 * @return Retorna listadoNutOralHisto.
	 */
	public Collection getListadoNutOralHisto() {
		return listadoNutOralHisto;
	}

	/**
	 * @param Asigna listadoNutOralHisto.
	 */
	public void setListadoNutOralHisto(Collection listadoNutOralHisto) {
		this.listadoNutOralHisto = listadoNutOralHisto;
	}

	/**
	 * @return Retorna fechaGrabacionDietaOrden.
	 */
	public String getFechaGrabacionDietaOrden() {
		return fechaGrabacionDietaOrden;
	}

	/**
	 * @param Asigna fechaGrabacionDietaOrden.
	 */
	public void setFechaGrabacionDietaOrden(String fechaGrabacionDietaOrden) {
		this.fechaGrabacionDietaOrden = fechaGrabacionDietaOrden;
	}

	/**
	 * @return Retorna fechaRegistroDietaOrden.
	 */
	public String getFechaRegistroDietaOrden() {
		return fechaRegistroDietaOrden;
	}

	/**
	 * @param Asigna fechaRegistroDietaOrden.
	 */
	public void setFechaRegistroDietaOrden(String fechaRegistroDietaOrden) {
		this.fechaRegistroDietaOrden = fechaRegistroDietaOrden;
	}


	/**
	 * @return Retorna medicoDietaOrden.
	 */
	public String getMedicoDietaOrden() {
		return medicoDietaOrden;
	}

	/**
	 * @param Asigna medicoDietaOrden.
	 */
	public void setMedicoDietaOrden(String medicoDietaOrden) {
		this.medicoDietaOrden = medicoDietaOrden;
	}

	/**
	 * @return Retorna observacionDietaParenteOrden.
	 */
	public String getObservacionDietaParenteOrden() {
		return observacionDietaParenteOrden;
	}

	/**
	 * @param Asigna observacionDietaParenteOrden.
	 */
	public void setObservacionDietaParenteOrden(String observacionDietaParenteOrden) {
		this.observacionDietaParenteOrden = observacionDietaParenteOrden;
	}

	/**
	 * @return Retorna the esConsulta.
	 */
	public boolean getEsConsulta()
	{
		return esConsulta;
	}

	/**
	 * @param esConsulta The esConsulta to set.
	 */
	public void setEsConsulta(boolean esConsulta)
	{
		this.esConsulta = esConsulta;
	}

	/**
	 * @return Returns the esResumenAt.
	 */
	public boolean isEsResumenAt() {
		return esResumenAt;
	}

	/**
	 * @param esResumenAt The esResumenAt to set.
	 */
	public void setEsResumenAt(boolean esResumenAt) {
		this.esResumenAt = esResumenAt;
	}

	/**
	 * @return Returns the idCuenta.
	 */
	public String getIdCuenta() {
		return idCuenta;
	}

	/**
	 * @param idCuenta The idCuenta to set.
	 */
	public void setIdCuenta(String idCuenta) {
		this.idCuenta = idCuenta;
	}

	/**
	 * @return Returns the codigoRegEnfer.
	 */
	public int getCodigoRegEnfer()
	{
		return codigoRegEnfer;
	}

	/**
	 * @param codigoRegEnfer The codigoRegEnfer to set.
	 */
	public void setCodigoRegEnfer(int codigoRegEnfer)
	{
		this.codigoRegEnfer=codigoRegEnfer;
	}

	/**
	 * @return Returns the esconderPYP.
	 */
	public boolean isEsconderPYP() {
		return esconderPYP;
	}

	/**
	 * @param esconderPYP The esconderPYP to set.
	 */
	public void setEsconderPYP(boolean esconderPYP) {
		this.esconderPYP = esconderPYP;
	}
	
	//-----------mapaMuestra
	
	/**
	 * @return Retorna mapaPupilas.
	 */
	public Object getMapaMuestra(String key)
	{
		return mapaMuestra.get(key+"");
	}

	/**
	 * @param mapaPupilas Asigna mapaPupilas.
	 */
	public void setMapaMuestra(String key, String valor)
	{
		this.mapaMuestra.put(key,valor);
	}	

	/**
	 * @return Retorna mapaPupilas.
	 */
	public HashMap getMapaMuestra()
	{
		return mapaMuestra;
	}

	/**
	 * @param mapaPupilas Asigna mapaPupilas.
	 */
	public void setMapaMuestra(HashMap mapa)
	{
		this.mapaMuestra = mapa;
	}

	public HashMap getMapaColsCuidadosEspecialesHistorico()
	{
		return mapaColsCuidadosEspecialesHistorico;
	}

	public void setMapaColsCuidadosEspecialesHistorico(HashMap mapaColsCuidadosEspecialesHistorico)
	{
		this.mapaColsCuidadosEspecialesHistorico = mapaColsCuidadosEspecialesHistorico;
	}	
	public Object getMapaColsCuidadosEspecialesHistorico(String key)
	{
		return mapaColsCuidadosEspecialesHistorico;
	}

	public void setMapaColsCuidadosEspecialesHistorico(String key,Object value)
	{
		this.mapaColsCuidadosEspecialesHistorico.put(key, value);
	}

	public HashMap getMapaCuidadosEspecialesHistorico()
	{
		return mapaCuidadosEspecialesHistorico;
	}

	public void setMapaCuidadosEspecialesHistorico(
			HashMap mapaCuidadosEspecialesHistorico)
	{
		this.mapaCuidadosEspecialesHistorico = mapaCuidadosEspecialesHistorico;
	}	

	public Object getMapaCuidadosEspecialesHistorico(String key)
	{
		return mapaCuidadosEspecialesHistorico.get(key);
	}

	public void setMapaCuidadosEspecialesHistorico(String key,Object value)
	{
		this.mapaCuidadosEspecialesHistorico.put(key, value);
	}

	public String getFechaInicio()
	{
		return fechaInicio;
	}

	public void setFechaInicio(String fechaInicio)
	{
		this.fechaInicio = fechaInicio;
	}

	public HashMap getMapaColsCuiEspHistoricoFechas()
	{
		return mapaColsCuiEspHistoricoFechas;
	}

	public void setMapaColsCuiEspHistoricoFechas(
			HashMap mapaColsCuiEspHistoricoFechas)
	{
		this.mapaColsCuiEspHistoricoFechas = mapaColsCuiEspHistoricoFechas;
	}	
	
	public Object getMapaColsCuiEspHistoricoFechas(String key)
	{
		return mapaColsCuiEspHistoricoFechas.get(key);
	}

	public void setMapaColsCuiEspHistoricoFechas(String key,Object value)
	{
		this.mapaColsCuiEspHistoricoFechas.put(key, value);
	}

	public Collection getHistoricoAnotacionesEnfermeriaFechas()
	{
		return historicoAnotacionesEnfermeriaFechas;
	}

	public void setHistoricoAnotacionesEnfermeriaFechas(
			Collection historicoAnotacionesEnfermeriaFechas)
	{
		this.historicoAnotacionesEnfermeriaFechas = historicoAnotacionesEnfermeriaFechas;
	}

	/**
	 * @return the areas
	 */
	public HashMap<String, Object> getAreas() {
		return areas;
	}

	/**
	 * @param areas the areas to set
	 */
	public void setAreas(HashMap<String, Object> areas) {
		this.areas = areas;
	}
	/**
	 * @return the areas
	 */
	public Object getAreas(String key) {
		return areas.get(key);
	}

	/**
	 * @param areas the areas to set
	 */
	public void setAreas(String key, Object obj) {
		this.areas.put(key,obj);
	}

	/**
	 * @return the pisos
	 */
	public ArrayList<HashMap<String, Object>> getPisos() {
		return pisos;
	}

	/**
	 * @param pisos the pisos to set
	 */
	public void setPisos(ArrayList<HashMap<String, Object>> pisos) {
		this.pisos = pisos;
	}

	/**
	 * @return the habitaciones
	 */
	public ArrayList<HashMap<String, Object>> getHabitaciones() {
		return habitaciones;
	}

	/**
	 * @param habitaciones the habitaciones to set
	 */
	public void setHabitaciones(ArrayList<HashMap<String, Object>> habitaciones) {
		this.habitaciones = habitaciones;
	}

	/**
	 * @return the camas
	 */
	public ArrayList<HashMap<String, Object>> getCamas() {
		return camas;
	}

	/**
	 * @param camas the camas to set
	 */
	public void setCamas(ArrayList<HashMap<String, Object>> camas) {
		this.camas = camas;
	}

	/**
	 * @return the codigoCama
	 */
	public String getCodigoCama() {
		return codigoCama;
	}

	/**
	 * @param codigoCama the codigoCama to set
	 */
	public void setCodigoCama(String codigoCama) {
		this.codigoCama = codigoCama;
	}

	/**
	 * @return the codigoHabitacion
	 */
	public String getCodigoHabitacion() {
		return codigoHabitacion;
	}

	/**
	 * @param codigoHabitacion the codigoHabitacion to set
	 */
	public void setCodigoHabitacion(String codigoHabitacion) {
		this.codigoHabitacion = codigoHabitacion;
	}

	/**
	 * @return the codigoPiso
	 */
	public String getCodigoPiso() {
		return codigoPiso;
	}

	/**
	 * @param codigoPiso the codigoPiso to set
	 */
	public void setCodigoPiso(String codigoPiso) {
		this.codigoPiso = codigoPiso;
	}

	/**
	 * @return the tipoRompimiento
	 */
	public String getTipoRompimiento() {
		return tipoRompimiento;
	}

	/**
	 * @param tipoRompimiento the tipoRompimiento to set
	 */
	public void setTipoRompimiento(String tipoRompimiento) {
		this.tipoRompimiento = tipoRompimiento;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isFinalizarDietaEnfermeria() {
		return finalizarDietaEnfermeria;
	}

	/**
	 * 
	 * @param finalizarDietaEnfermeria
	 */
	public void setFinalizarDietaEnfermeria(boolean finalizarDietaEnfermeria) {
		this.finalizarDietaEnfermeria = finalizarDietaEnfermeria;
	}

	/**
	 * 
	 * @return
	 */
	public String getObservacionesEnfermeria() {
		return observacionesEnfermeria;
	}

	/**
	 * 
	 * @param observacionesEnfermeria
	 */
	public void setObservacionesEnfermeria(String observacionesEnfermeria) {
		this.observacionesEnfermeria = observacionesEnfermeria;
	}

	public String getInterfazNutricion() {
		return interfazNutricion;
	}

	public void setInterfazNutricion(String interfazNutricion) {
		this.interfazNutricion = interfazNutricion;
	}
	
	
	public HashMap getNutricionOralMap() {
		return nutricionOralMap;
	}

	public void setNutricionOralMap(HashMap nutricionOralMap) {
		this.nutricionOralMap = nutricionOralMap;
	}

	public void setNutricionOralMap(String key, Object value) {
		this.nutricionOralMap.put(key, value);
	}

	public boolean isFinalizarDietaEnfermeriaAnt() {
		return finalizarDietaEnfermeriaAnt;
	}

	public void setFinalizarDietaEnfermeriaAnt(boolean finalizarDietaEnfermeriaAnt) {
		this.finalizarDietaEnfermeriaAnt = finalizarDietaEnfermeriaAnt;
	}

	public String getSuspensionOrdenMedica() {
		return suspensionOrdenMedica;
	}

	public void setSuspensionOrdenMedica(String suspensionOrdenMedica) {
		this.suspensionOrdenMedica = suspensionOrdenMedica;
	}

	/**
	 * @return the validacionesCierreAperturaNotasMap
	 */
	public HashMap getValidacionesCierreAperturaNotasMap() {
		return validacionesCierreAperturaNotasMap;
	}

	/**
	 * @param validacionesCierreAperturaNotasMap the validacionesCierreAperturaNotasMap to set
	 */
	public void setValidacionesCierreAperturaNotasMap(
			HashMap validacionesCierreAperturaNotasMap) {
		this.validacionesCierreAperturaNotasMap = validacionesCierreAperturaNotasMap;
	}
	
	/**
	 * @return the validacionesCierreAperturaNotasMap
	 */
	public Object getValidacionesCierreAperturaNotasMap(String key) {
		return validacionesCierreAperturaNotasMap.get(key);
	}

	/**
	 * @param validacionesCierreAperturaNotasMap the validacionesCierreAperturaNotasMap to set
	 */
	public void setValidacionesCierreAperturaNotasMap(String key, Object value) {
		this.validacionesCierreAperturaNotasMap.put(key, value);
	}

	/**
	 * @return the deboAbrirPrescripcionDialisis
	 */
	public boolean isDeboAbrirPrescripcionDialisis() {
		return deboAbrirPrescripcionDialisis;
	}

	/**
	 * @param deboAbrirPrescripcionDialisis the deboAbrirPrescripcionDialisis to set
	 */
	public void setDeboAbrirPrescripcionDialisis(
			boolean deboAbrirPrescripcionDialisis) {
		this.deboAbrirPrescripcionDialisis = deboAbrirPrescripcionDialisis;
	}

	/**
	 * @return the dialisis
	 */
	public DtoPrescripcionDialisis getDialisis() {
		return dialisis;
	}

	/**
	 * @param dialisis the dialisis to set
	 */
	public void setDialisis(DtoPrescripcionDialisis dialisis) {
		this.dialisis = dialisis;
	}

	/**
	 * @return the historicoDialisis
	 */
	public ArrayList<DtoPrescripcionDialisis> getHistoricoDialisis() {
		return historicoDialisis;
	}

	/**
	 * @param historicoDialisis the historicoDialisis to set
	 */
	public void setHistoricoDialisis(
			ArrayList<DtoPrescripcionDialisis> historicoDialisis) {
		this.historicoDialisis = historicoDialisis;
	}
	
	public int getNumHistoricoDialisis()
	{
		return this.historicoDialisis.size();
	}

	/**
	 * @return the nuevaObservDietaEnfermeria
	 */
	public String getNuevaObservDietaEnfermeria() {
		return nuevaObservDietaEnfermeria;
	}

	/**
	 * @param nuevaObservDietaEnfermeria the nuevaObservDietaEnfermeria to set
	 */
	public void setNuevaObservDietaEnfermeria(String nuevaObservDietaEnfermeria) {
		this.nuevaObservDietaEnfermeria = nuevaObservDietaEnfermeria;
	}

	/**
	 * @return the arrayTipoFrecuencias
	 */
	public ArrayList<HashMap<String,Object>> getArrayTipoFrecuencias() {
		return arrayTipoFrecuencias;
	}

	/**
	 * @param arrayTipoFrecuencias the arrayTipoFrecuencias to set
	 */
	public void setArrayTipoFrecuencias(
			ArrayList<HashMap<String,Object>> arrayTipoFrecuencias) {
		this.arrayTipoFrecuencias = arrayTipoFrecuencias;
	}

	/**
	 * @return the arrayFrecuenciasCuidadoEnfer
	 */
	public ArrayList<DtoFrecuenciaCuidadoEnferia> getArrayFrecuenciasCuidadoEnfer() {
		return arrayFrecuenciasCuidadoEnfer;
	}

	/**
	 * @param arrayFrecuenciasCuidadoEnfer the arrayFrecuenciasCuidadoEnfer to set
	 */
	public void setArrayFrecuenciasCuidadoEnfer(
			ArrayList<DtoFrecuenciaCuidadoEnferia> arrayFrecuenciasCuidadoEnfer) {
		this.arrayFrecuenciasCuidadoEnfer = arrayFrecuenciasCuidadoEnfer;
	}	

	/**
	 * Retorna historicoTomaMuestras
	 * @return
	 */
    public HashMap getHistoricoTomaMuestras() {
		return historicoTomaMuestras;
	}
    
    /**
     * Asiga historicoTomaMuestras
     * @param historicoTomaMuestras
     */
	public void setHistoricoTomaMuestras(HashMap historicoTomaMuestras) {
		this.historicoTomaMuestras = historicoTomaMuestras;
	}
	
	public ArrayList<Object> getResultadoLaboratoriosHistoricos() {
		return resultadoLaboratoriosHistoricos;
	}
	public void setResultadoLaboratoriosHistoricos(
			ArrayList<Object> resultadoLaboratoriosHistoricos) {
		this.resultadoLaboratoriosHistoricos = resultadoLaboratoriosHistoricos;
	}
	public String getAncla() {
		return ancla;
	}
	public void setAncla(String ancla) {
		this.ancla = ancla;
	}
	public int getPosPagerHistoricos() {
		return posPagerHistoricos;
	}
	public void setPosPagerHistoricos(int posPagerHistoricos) {
		this.posPagerHistoricos = posPagerHistoricos;
	}
	public ArrayList<Object> getResultadoLaboratorios() {
		return resultadoLaboratorios;
	}
	public void setResultadoLaboratorios(ArrayList<Object> resultadoLaboratorios) {
		this.resultadoLaboratorios = resultadoLaboratorios;
	}
	
	
	public String getEtiquetaCampoOtro() {
		return etiquetaCampoOtro;
	}
	public void setEtiquetaCampoOtro(String etiquetaCampoOtro) {
		this.etiquetaCampoOtro = etiquetaCampoOtro;
	}
	public ArrayList<Object> getValoracionEnfermeria() {
		return valoracionEnfermeria;
	}
	public void setValoracionEnfermeria(ArrayList<Object> valoracionEnfermeria) {
		this.valoracionEnfermeria = valoracionEnfermeria;
	}
	public ArrayList<Object> getValoracionEnfermeriaHistoricos() {
		return valoracionEnfermeriaHistoricos;
	}
	public void setValoracionEnfermeriaHistoricos(
			ArrayList<Object> valoracionEnfermeriaHistoricos) {
		this.valoracionEnfermeriaHistoricos = valoracionEnfermeriaHistoricos;
	}
	public int getPosPagerHistoricosVE() {
		return posPagerHistoricosVE;
	}
	public void setPosPagerHistoricosVE(int posPagerHistoricosVE) {
		this.posPagerHistoricosVE = posPagerHistoricosVE;
	}
	public String getEtiquetaCampoOtroResLab() {
		return etiquetaCampoOtroResLab;
	}
	public void setEtiquetaCampoOtroResLab(String etiquetaCampoOtroResLab) {
		this.etiquetaCampoOtroResLab = etiquetaCampoOtroResLab;
	}
	/**
	 * @param pacientesNuevaInformacion the pacientesNuevaInformacion to set
	 */
	public void setPacientesNuevaInformacion(boolean pacientesNuevaInformacion) {
		this.pacientesNuevaInformacion = pacientesNuevaInformacion;
	}
	/**
	 * @return the pacientesNuevaInformacion
	 */
	public boolean isPacientesNuevaInformacion() {
		return pacientesNuevaInformacion;
	}
	/**
	 * @param listaAlertasOrdenesMed the listaAlertasOrdenesMed to set
	 */
	public void setListaAlertasOrdenesMed(HashMap<Integer, DtoRegistroAlertaOrdenesMedicas> listaAlertasOrdenesMed) {
		this.listaAlertasOrdenesMed = listaAlertasOrdenesMed;
	}
	/**
	 * @return the listaAlertasOrdenesMed
	 */
	public HashMap<Integer, DtoRegistroAlertaOrdenesMedicas> getListaAlertasOrdenesMed() {
		return listaAlertasOrdenesMed;
	}
	/**
	 * @param revisado the revisado to set
	 */
	public void setRevisado(boolean revisado) {
		this.revisado = revisado;
	}
	/**
	 * @return the revisado
	 */
	public boolean isRevisado() {
		return revisado;
	}
	/**
	 * @param fechaInicioRegistro the fechaInicioRegistro to set
	 */
	public void setFechaInicioRegistro(String fechaInicioRegistro) {
		FechaInicioRegistro = fechaInicioRegistro;
	}
	/**
	 * @return the fechaInicioRegistro
	 */
	public String getFechaInicioRegistro() {
		return FechaInicioRegistro;
	}
	/**
	 * @param horaInicioRegistro the horaInicioRegistro to set
	 */
	public void setHoraInicioRegistro(String horaInicioRegistro) {
		HoraInicioRegistro = horaInicioRegistro;
	}
	/**
	 * @return the horaInicioRegistro
	 */
	public String getHoraInicioRegistro() {
		return HoraInicioRegistro;
	}
	/**
	 * @param consultaXArea the consultaXArea to set
	 */
	public void setConsultaXArea(boolean consultaXArea) {
		this.consultaXArea = consultaXArea;
	}
	/**
	 * @return the consultaXArea
	 */
	public boolean isConsultaXArea() {
		return consultaXArea;
	}

}