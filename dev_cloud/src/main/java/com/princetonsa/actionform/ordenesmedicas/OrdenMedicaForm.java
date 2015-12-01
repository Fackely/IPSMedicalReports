/*
 * Creado en May 31, 2005
 */
package com.princetonsa.actionform.ordenesmedicas;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.enfermeria.DtoFrecuenciaCuidadoEnferia;
import com.princetonsa.dto.historiaClinica.DtoValoracion;
import com.princetonsa.dto.manejoPaciente.DtoResultadoLaboratorio;
import com.princetonsa.dto.ordenesmedicas.DtoPrescripcionDialisis;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.Valoraciones;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.UtilidadesHistoriaClinica;

/**
 * Form que permite solicitar a un paciente  simultaneamente servicios como medicamentos, 
 * procedimientos, interconsultas, soporte respiratorio, dietas, cuidades de enfermer铆a.
 *
 *  @author Andr茅s Mauricio Ruiz V茅lez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public class OrdenMedicaForm extends ValidatorForm
{
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private transient Logger logger = Logger.getLogger(OrdenMedicaForm.class);

	/**
	 * Manejo de estados para el flujo de la funcionalidad
	 */
	private String estado;
	
	//SECCION DE INFORMACION GENERAL
	
	/**
	 * Fecha de ingreso de la orden
	 */
	private String fechaOrden;

	/**
	 * Hora de ingreso de la orden 
	 */
	private String horaOrden;
  
	/**
	 * Fecha de la orden temporal
	 */
	private String fechaTempOrden;
  
	/**
	 * Hora de la orden temporal
	 */
	private String horaTempOrden;
  
	/**
	 * Collection para traer la informacion genereal de la Orden Medica 
	 */
	private Collection InfOrdenMedica;
	
	//SECCION TIPO DE MONITOREO
	
	/**
	 * Tipo de monitoreo
	 */
	private int tipoMonitoreo;
  
	/**
	 * Variable para tener el ultimo tipo de monitoreo
	 */
	private int tipoMonitoreoTemp;
  
	/**
	 * Coleccion para traer los tipos de soportes respiratorios   
	 */
	private Collection listadoTiposMonitoreo;
  
	/**
	 * collecion que retorna los tipos de monitoreos registrados Anteriormente
	 */
	private Collection monitoreosHisto;
  
	//SECCION DE SOPORTE RESPIRATORIO
  
	/**
	 * Campo de chequeo que permite definir si el paciente tiene oxigeno-terapia
	 */
	private String oxigenoTerapia;
  
	/**
	 * Campo para guardar el valor postulado de la oxigeno terapia, para saber si 茅ste fue
	 * modificado
	 */
	private String tempOxigenoTerapia;
  
	/**
	 * Cantidad de litros de ox铆geno por minuto solicitados por el m茅dico
	 */
	private float cantidadSoporteRespiratorio;
  
	/**
	 * Campo para guardar el valor postulado de la cantidad de soporte, para saber si 茅ste fue
	 * modificado
	 */
	private float tempCantidadSoporte;
  
	/**
	 * Equipo usado en la oxigenoterapia
	 */
	private int equipoElemento;
	
	/**
	 * Campo para guardar el valor postulado del equipo elemento, para saber si 茅ste fue
	 * modificado
	 */
	private int tempEquipoElemento;
  
	/**
	 * Campo descripci贸n para ingresar observaciones adicionales a la ox铆geno-terapia.
	 */
	private String descripcionSoporteRespiratorio;
  
	/**
	 * Campo para registrar la descripci贸n nueva del soporte respiratorio
	 */
	private String descripcionSoporteNueva;
  
	/**
	 * Campo para guardar la descripci贸n del soporte respiratorio de la cuenta de urgencias
	 * cuando se realiza el asocio de cuentas, y de esta forma poder mostrar esta informaci贸n en el formulario
	 */
	private String descripcionSoporteUrgencias;
  
	/**
	 * Campo para indicar si se finaliza el soporte respiratorio
	 */
	private boolean finalizarSoporte;
  
	/**
	 * Coleccion para traer los tipos de soportes respiratorios   
	 */
	private Collection listadoSoporteRes;
  
	/**
	 * collecion que retorna los historicos del soporte respiratorio 
	 */
	private Collection soporteRespiraHisto;
	
	
	// ---- CAMPOS PARA LA MODIFICACION DE LA SECCION MEZCLAS---- //
	private String numeroOrdenModificar;
	private String numeroSolicitudModificar;
	private HashMap mezclaModificar;
	private String codigoArticuloMezcla;
	private String nombreArticuloMezcla;
	private boolean esPosArticuloMezcla;
	private String tipoTransaccionPedido;	
	private HashMap listadoHistoCuidadosEsp;
	
	/**
	 * 
	 */
	private String fechaCuidado="";
	
  
private String ancla;
	
	/**
	 * 
	 */
	private int posPagerHistoricos;
	
	/**
	 * 
	 */
	private int posPagerHistoricosVE;

	
	/**
	 * 
	 */
	private String etiquetaCampoOtro;
	
	/**
	 * 
	 */
	private ArrayList<Object> resultadoLaboratorios;
	
	
	/**
	 * 
	 */
	private ArrayList<Object> resultadoLaboratoriosHistoricos;
	
	/**
	 * 
	 */
	
	//------------------------SECCION DIETA -----------------------------------------------//
  
	/**
	 * 
	 */
	private HashMap listadoAlmacenesMap;
  
	/**
	 * Campo Volumen total de las cantidades de nutricion parenteral 
	 */
	private String volumenTotal;
	
	/**
	 * Campo Unidad del Volumen total
	 * */
	private String unidadVolumenTotal;
  
	/**
	 * Campo Velocidad infusion  
	 */
	private String velocidadInfusion; 
  
	/**
	 * Campo de chequeo para determinar si el paciente tiene nutrici贸n oral
	 */
	private boolean nutricionOral;
	
	/**
	 * Campo para guardar el valor postulado de via oral, para saber si 茅ste fue
	 * modificado
	 */
	private boolean tempNutricionOral;
 
	/**
	 * Campo de chequeo para determinar si el paciente tiene nutrici贸n parenteral
	 */
	private boolean nutricionParenteral;
	
	/**
	 * Campo para guardar el valor postulado de via parenteral, para saber si 茅ste fue
	 * modificado
	 */
	private boolean tempNutricionParenteral;
  
	/**
	 * Campo descripci贸n para ingresar observaciones adicionales a la dieta oral del paciente
	 */
	private String descripcionDieta;
   
	/**
	 * Campo para registrar la descripci贸n nueva de la dieta
	 */
	
	private String descripcionDietaNueva;

	/**
	 * Campo descripci贸n para ingresar observaciones adicionales a la dieta parenteral del paciente
	 */
	private String descripcionDietaParenteral;

	private String descripcionDietaParenteralNueva;
	
	/**
	 * Seleccion mezcla
	 */
	private int mezcla;

	/**
	 * Seleccion mezcla
	 */
	private int mezclaTempo;

	/**
	 * Campo para guardar la descripci贸n dieta de la cuenta de urgencias
	 * cuando se realiza el asocio de cuentas, y de esta forma poder mostrar esta informaci贸n en el formulario
	 */
	private String descripcionDietaUrgencias;
	
	/**
	 * Campo para seleccionar el centro de costo que se desea depache la nutrici贸n parenteral
	 */
	private int farmacia;

	/**
	 * Campo para seleccionar el centro de costo que se desea depache la nutrici贸n parenteral
	 */
	private String nombreFarmacia;

	/**
	 * Campo para guardar algun otro tipo de nutricion Oral
	 */
	 private String otroNutORal;
	 
	/**
	 * Campo para guardar el valor postulado otra nutrici贸n oral, para saber si 茅ste fue
	 * modificado
	 */
	 private String tempOtroNutOral;
	
	/**
	 * Campo que guarda las nutriciones orales y parenterales
	 */
	private HashMap mapa;
	
	/**
	 * Hashmap temporal para guardar la nutrici贸n oral
	 */
	private HashMap tempMapa;
	
	/**
	 * Campo para indicar que se finaliza la dieta
	 */ 
	private boolean finalizarDieta;
	
	/**
	 * Campo para guardar el valor postulado de finalizar dieta, para saber si 茅ste fue
	 * modificado
	 */
	private boolean tempFinalizarDieta;
	
	
	/**
	 * Campo para indicar que se finaliza la dieta por la enfermera
	 */ 
	private boolean finalizarDietaEnfermeria;
	
	
	/**
	 * Campo para indicar que se finaliza la dieta por la enfermera
	 */ 
	private boolean tempfinalizarDietaEnfermeria;
	
	
	/**
	 * Coleccion para traer el Listado de tipos de nutricion Oral	 
	 */
	private Collection listadoNutOral;
	
	/**
	 * Coleccion con el listado de otros tipos de nutrici贸n oral
	 */
	private Collection listadoOtrosNutOral;
	 
	/**
	 * Coleccion para traer el Listado de tipos de nutricion Oral	 
	 */
	private Collection listadoNutParent;
	
	/**
	 * Coleccion para traer el Listado de los historicos de nutricion parenteral	 
	 */
	private Collection listadoNutParentHisto;
	
	/**
	 * Coleccion para traer el Listado de los historicos de nutricion Oral	 
	 */
	private Collection listadoNutOralHisto;
	
	/**
	 * Indide para el recorrido de los historicos de nutricion parenteral 
	 */
	
	private int posIndParent;
	
	/**
	 * Mostrar mensajes de error debido
	 */
	private String mensajeError;
	
	/**
	 * Mandar el par谩metro desde el action
	 */
	private String parametroMensaje;
	
	/**
	 * Collection para traer las mezclas finalizadas para la cuenta en el
	 * ver anteriores
	 */
	private Collection mezclasFinalizadasAnteriores;
	
	/**
	 * Collection para traer el detalle de art铆culos de la mezcla seleccionada en ver anteriores
	 */
	private Collection detalleArticulosMezclaAnteriores;
	
	/**
	 * C贸digo del encabezado anterior de la mezcla finalizada seleccionada
	 */
	private int codEncabezadoMinAnterior;
	
	/**
	 * C贸digo del encabezado que corresponde a la mezcla finalizada en ver anteriores
	 */
	private int codEncabezadoAnterior;
	
	/**
	 * C贸digo de la mezcla anterior seleccionada en ver anteriores
	 */
	private int mezclaAnterior;
	
	/**
	 * Coleccion para traer el Listado de tipos de Parenteral a mostrar en ver anteriores	 
	 */
	private Collection listadoNutParentAnterior;
	
	/**
	 * Listar las mezclas no finalizadas
	 */
	private Collection listadoMezclasSinFinalizar;	
	
	/**
	 * 
	 */
	private String interfazNutricion;
	 
	/**
	 * Variable para capturar los tipos de dietas en estado activo
	 */
	private HashMap nutricionOralMap;
	
	
	//-------------SECCION DE CUIDADES DE ENFERMERIA -------------------------------------------------//
	
	/**
	 * Coleccion para traer el Listado de tipos de cuidados de emfermeria	
	 */
	private Collection listadoCuidadosEmfermeria;

	/**
	 * Coleccion para traer el Listado de tipos de cuidados de emfermeria	
	 */
	private Collection listadoCuidadosEmfermeriaPopUp;

	/**
	 * Coleccion para traer el Listado de los otros tipos de cuidados de emfermeria	
	 */
	private Collection listadoOtrosCuidadosEnfer;
	
	/**
	 * Para Almacenar Otro Tipo de Cuidado de emfermeria	
	 */
	private String otroCuidadoEnf;
	
	/**
	 * Campo para guardar los cuidades de enfermer铆a del paciente
	 */
	private HashMap cuidadosEnfermeria;
	
	/**
	 * Coleccion para traer el Listado de los historicos de cuidados de enfermeria	
	 */
	private Collection listadoCuidadosEnfHisto;
	
	/**
	 * Coleccion para traer el Listado de los historicos de cuidados de enfermeria	
	 */
	private Collection listadoCuidadosEnfHistoPopUp; 
 
	/**
	 * Indide para el recorrido de los historicos 
	 */
	private int posIndEnf;
	

	/**
     * Almacena los tipos de frecuencias
     * */
    private ArrayList<HashMap<String,Object>> arrayTipoFrecuencias;
    
    /**
     * Almacen la informacion de las frecuencias de cuidados de enfermeria almacenados
     * */
    private ArrayList<DtoFrecuenciaCuidadoEnferia> arrayFrecuenciasCuidadoEnfer;
	 
	//-----------------------SECCION OBSERVACIONES GENERALES-------------------------------//
	
	/**
	 * Campo para registrar las observaciones generales de la orden m茅dica. 
	 */
	private String observacionesGenerales;
	
	/**
	 * Campo para registrar las observaciones generales de la orden m茅dica. 
	 */
	private String observacionesGeneralesNueva;
	
	/**
	 * Campo para guardar las observaciones generales de la cuenta de urgencias
	 * cuando se realiza el asocio de cuentas, y de esta forma poder mostrar esta informaci贸n en el formulario
	 */
	private String observacionesGralesUrgencias;
	
	/**
	 * Fecha de la valoraci贸n
	 */
	private String fechaValoracion;
	 
	/**
	 * Hora de la valoraci贸n
	 */
	private String horaValoracion;
	
	/**
	 * Variable del area de texto emergerte para 
	 * cuando se intenta escribir en un cajon de texto.	
	 */
	private String temp;
	
	/**
	 * Vector que guarda las descripciones de los tipos de nutrici贸n oral que no est谩 parametrizados
	 * en hospitalizaci贸n
	 */
	private Vector otrasNutricionesOrales;
	
	/**
	 * Campo para indicar si no hay soporte respiratorio
	 */
	private boolean noHaySoporte;
	
	//------------------------------------------------------ ORDEN DE LA HOJA NEUROL脫GICA ---------------------------------------------//
	/**
	 * Campo para guardar si presenta hoja neurol贸gica
	 */
	private String presentaHojaNeuro;
	
	/**
	 * Campo para guardar las observaciones en la orden
	 * de la hoja neurol贸gica
	 */
	private String observacionesHojaNeuro;
	
	/**
	 * Campo para guardar las observaciones nuevas en la orden
	 * de la hoja neurol贸gica
	 */
	private String observacionesHojaNeuroNueva;
	
	/**
	 * Campo para indicar si se finaliza la orden de la
	 * hoja neurol贸gica
	 */
	private String finalizadaHojaNeuro;
	
	/**
	 * Menjo de secciones
	 */
	private HashMap mapaSecciones;
	
	/**
	 * Variable que me indica si debo abrir la referencia
	 */
	private boolean deboAbrirReferencia;
	

	
	
	
	/* ------------------------------ desarrollo justificacion no pos ----------------------------------------- */
	/**
	 * Mapa justificacion mapa donde se almacenan los datos para insetar de la justificacion de articulos nopos
	 */
	private HashMap justificacionMap=new HashMap();
	
	/**
	 * Mapa justificacion mapa donde se almacenan los datos para insetar de la justificacion de articulos nopos de los articulos Historicos
	 */
	private HashMap justificacionHistoricoMap=new HashMap();
	
	/**
	 * mapa en el que se guardan las llaves que indican si se ha justificado 
	 * (actualmente solo aplica para justificaciones de articulos diferentes a medicamentos)
	 */
	private HashMap justificadoMap = new HashMap();
	
	/**
	 * Mapa medicamento pos
	 */
	private HashMap medicamentosPosMap=new HashMap();
	
	/**
	 * Mapa medicamento no pos
	 */
	private HashMap medicamentosNoPosMap=new HashMap();
	
	/**
	 * Mapa medicamento sustituto no pos
	 */
	private HashMap sustitutosNoPosMap=new HashMap();
	
	/**
	 * Mapa diagnosticos definitivos
	 */
	private HashMap diagnosticosDefinitivos=new HashMap();
	
	/**
	 * Mapa diagnosticos presuntivos
	 */
	private HashMap diagnosticosPresuntivos=new HashMap();	
	
	/**
	 * numero de justificacion
	 */
	private int numjus=0;
	
	/**
	 * nuemero de articulos 
	 */
	private int numeroElementos;
	
	private String hiddens;
	
	private String hiddensHistorico;		
	
	private String suspenderEnfermeria;
	
	
	/**
	 * 
	 */
	private ArrayList<Object> valoracionEnfermeria;

	
	/**
	 * 
	 */
	private ArrayList<Object> valoracionEnfermeriaHistoricos;
	

		
	//********ATRIBUTOS PARA LA SECCION DE PRESCRIPCION DIALISIS*****************************************
	private boolean deboAbrirPrescripcionDialisis;
	private String codigoTipoMembrana = ""; //variable usada para enviar el codigo de tipo membrana seleccionada por ajax
	private DtoPrescripcionDialisis dialisis = new DtoPrescripcionDialisis();
	private ArrayList<DtoPrescripcionDialisis> historicoDialisis = new ArrayList<DtoPrescripcionDialisis>();
	//***************************************************************************************************
	
	
	private String dosificacion;
		
	/**
	 * 
	 */
	private boolean modificacionRegistroEnfermeria;
	
	/**
	 * 
	 */
	private boolean puedoModifcarMezcla;
	
	/**
	 * 
	 * */
	private String indicadorUtilitario;
	
	/**
	 * 
	 * */
	private HashMap observacionesMezclasMap;
	

	private String suspender="";
	
	//--------------------------------------------------------------
	// atributo utilizado desde la hqx para indicar si se pone la cabecera o no
	private String esDummy="";
	
	//--------------------------------------------------------------
	// atributo utilizado desde la hqx para indicar si se pone la cabecera o no
	private String ocultarCabeza="";

	/**
	 * atributo usado para determinar si se ingreso un nuevo registro en la secci髇 Observaciones Generales
	 */
	private boolean existeNuevoRegObservacionesGral;
	
	/**
	 * Campo que guarda el codigo del encabezado que contiene el ultimo soporte respiratorio 
	 */
	private int codigoEncabezadoSoporteRespira;
	
	/**
	 * Campo que almacena la descripcion del soporte respiratorio actual
	 */
	private String descripcionIndivSoporteRespira = "";
	
	
	/**
	 * campo que indica que el proceso de orden medica fue exitoso
	 * MT2300
	 */
	private Boolean procesoExitoso;
	//--------------------------------------------------- FIN DE ATRIBUTOS --------------------------------------------------------------------//


	public String getOcultarCabeza() {
		return ocultarCabeza;
	}


	public void setOcultarCabeza(String ocultarCabeza) {
		this.ocultarCabeza = ocultarCabeza;
	}
	
	//------------------------------------------------------------
	
	//----------------------------------------------------------------
	//Modificado por anexo 779
	
	private ArrayList mensajes = new ArrayList();

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
	//----------------------------------------------------------------
	//--------------------------------------------------- FIN DE ATRIBUTOS --------------------------------------------------------------------//

	
	/**
	 * M茅todo para resetear los valores
	 *
	 */
	public void reset()
	{
		this.ancla="";
		this.posPagerHistoricos=0;
		this.posPagerHistoricosVE=0;
		this.etiquetaCampoOtro="";
		this.valoracionEnfermeria=new ArrayList<Object>();
		this.valoracionEnfermeriaHistoricos=new ArrayList<Object>();

		this.resultadoLaboratorios=new ArrayList<Object>();
		this.resultadoLaboratoriosHistoricos=new ArrayList<Object>();
		
		//justificacion no pos
		this.numeroElementos=0;
		this.justificacionMap=new HashMap();
		this.justificacionHistoricoMap = new HashMap();
		this.medicamentosNoPosMap=new HashMap();
		this.medicamentosPosMap=new HashMap();
		this.sustitutosNoPosMap=new HashMap();
		this.diagnosticosDefinitivos=new HashMap();
		this.diagnosticosPresuntivos=new HashMap();
		this.numjus=0;
		this.hiddens="";
		this.hiddensHistorico="";
		//----
			
		this.listadoHistoCuidadosEsp=new HashMap();
		this.listadoHistoCuidadosEsp.put("numRegistros","0");
		fechaOrden=UtilidadFecha.getFechaActual();
		horaOrden=UtilidadFecha.getHoraActual();
		fechaTempOrden="";
		horaTempOrden="";
		tipoMonitoreo=0;
		tipoMonitoreoTemp=0;
			
		oxigenoTerapia="";
		tempOxigenoTerapia="";
		cantidadSoporteRespiratorio=0;
		tempCantidadSoporte=0;
		equipoElemento=-1;
		tempEquipoElemento=-1;
		descripcionSoporteRespiratorio="";
		descripcionSoporteNueva="";
		finalizarSoporte=false;
		nutricionOral=false;
		tempNutricionOral=false;
		nutricionParenteral=false;
		tempNutricionParenteral=false;
		descripcionDieta="";
		descripcionDietaNueva="";
		descripcionDietaParenteral="";
		descripcionDietaParenteralNueva="";
		farmacia=0;
		nombreFarmacia="";
		mapa = new HashMap();
		tempMapa = new HashMap();
		
		finalizarDieta=false;
		finalizarDietaEnfermeria=false;
		tempFinalizarDieta=false;
		cuidadosEnfermeria=new HashMap();
		observacionesGenerales="";
		observacionesGeneralesNueva = "";
		observacionesGralesUrgencias = "";
		descripcionSoporteUrgencias = "";
		descripcionDietaUrgencias = "";
			
		otrasNutricionesOrales=new Vector();
			
		volumenTotal = "";
		this.unidadVolumenTotal = "";
		velocidadInfusion = ""; 
		otroNutORal = "";
		tempOtroNutOral = "";
		noHaySoporte=false;
		temp = "";
		 
		otroCuidadoEnf = "";
		codEncabezadoMinAnterior=0;
		codEncabezadoAnterior=0;
		mezclaAnterior=0;
		
		//----------Orden Hoja Neurol贸gica -----------//
		presentaHojaNeuro="";
		observacionesHojaNeuro="";
		observacionesHojaNeuroNueva="";
		finalizadaHojaNeuro="";
		 
		posIndEnf=0;
		posIndParent=0;
		
		mezcla=0;
		mezclaTempo=0;
		listadoMezclasSinFinalizar=null;
		
		
		//	---- CAMPOS PARA LA MODIFICACION DE LA SECCION MEZCLAS---- //
		this.numeroOrdenModificar="";
		this.numeroSolicitudModificar="";
		this.mezclaModificar=new HashMap();
		this.mezclaModificar.put("numRegistros", "0");
		this.codigoArticuloMezcla="";
		this.nombreArticuloMezcla="";
		this.tipoTransaccionPedido = "";
		
		this.deboAbrirReferencia = false;
		this.interfazNutricion = "";
		this.nutricionOralMap = new HashMap();
		this.suspenderEnfermeria ="";
		
		///********ATRIBUTOS PARA LA SECCION DE PRESCRIPCION DIALISIS*****************************************
		this.deboAbrirPrescripcionDialisis = false;
		this.codigoTipoMembrana = "";
		this.dialisis = new DtoPrescripcionDialisis();
		this.historicoDialisis = new ArrayList<DtoPrescripcionDialisis>();
		//***************************************************************************************************
		
		this.modificacionRegistroEnfermeria=false;
		this.puedoModifcarMezcla=true;
		
		this.dosificacion="";
		this.indicadorUtilitario = "";
		this.esPosArticuloMezcla = true;
		this.observacionesMezclasMap = new HashMap();
		this.suspender="";
		
		//Seccion Cuidados de Enfermeria
		this.arrayTipoFrecuencias = new ArrayList<HashMap<String,Object>>();
		this.arrayFrecuenciasCuidadoEnfer = new ArrayList<DtoFrecuenciaCuidadoEnferia>();
		
		this.justificadoMap = new HashMap();
		
		this.setExisteNuevoRegObservacionesGral(false);
		this.descripcionIndivSoporteRespira = "";
		
	}



	public void resetInterfaz ()
	{
		
		this.mensajes = new ArrayList();
	}
/**
	 * @return the esDummy
	 */
	public String getEsDummy() {
		return esDummy;
	}


	/**
	 * @param esDummy the esDummy to set
	 */
	public void setEsDummy(String esDummy) {
		this.esDummy = esDummy;
	}


public String getSuspender() {
		return suspender;
	}


	public void setSuspender(String suspender) {
		this.suspender = suspender;
	}


/**
	 * @return the codigoTipoMembrana
	 */
	public String getCodigoTipoMembrana() {
		return codigoTipoMembrana;
	}


	/**
	 * @param codigoTipoMembrana the codigoTipoMembrana to set
	 */
	public void setCodigoTipoMembrana(String codigoTipoMembrana) {
		this.codigoTipoMembrana = codigoTipoMembrana;
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
	
	HttpSession session=request.getSession();	
	PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
	DtoValoracion valoracion=new DtoValoracion();
	boolean errorHora=false;
	boolean existenMedicamentosMezcla = false;
	
	
	if(estado.equals("salir"))
	{
		//errores=super.validate(mapping,request);
		if(finalizarSoporte)
		{
			oxigenoTerapia = "";
			equipoElemento = -1;
		}
		
		//*********************************************************************************
		//Validaciones Mezclas	
		
		Connection con1=null;
		UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");		
		
		try
		{
			con1 = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
		}
		catch(SQLException e)
		{
				logger.warn("No se pudo abrir la conexi贸n"+e.toString());

		}
		
		//-----------------------------------------------------------------------------------------------------------------------//			
		//validacion para los campos de Cuidados de Enfermeria
		if(this.getListadoCuidadosEmfermeria()!=null && !this.getListadoCuidadosEmfermeria().isEmpty())	 	
		{
			//validacion de los campos de frecuencia y periodo
			Vector codigosCuidadosEnfer=(Vector) this.mapa.get("codigosCuidadoEnf");
			String valor = "";
			
			
			if (codigosCuidadosEnfer != null)
			{
				for(int i=0; i<codigosCuidadosEnfer.size();i++)
				{
					valor = codigosCuidadosEnfer.elementAt(i).toString();
					if(!UtilidadTexto.getBoolean(mapa.get("indicativoControlEspecial_"+valor)+""))
					{
					
						//Frecuencia y tipo de frecuencia
						if(!(this.mapa.get("frecuencia_"+valor)+"").trim().equals("") &&
								Utilidades.convertirAEntero((this.mapa.get("frecuencia_"+valor)+"")) <= 0)
							errores.add("descripcion", new ActionMessage("errors.notEspecific","La frecuencia del Procedimiento ["+this.mapa.get("descripcion_"+valor)+"] de la Secci髇 Cuidados Especiales de Enfermeria debe ser un Valor numerico Mayor a Cero"));
						else if(!(this.mapa.get("frecuencia_"+valor)+"").trim().equals("") 
								&& Utilidades.convertirAEntero((this.mapa.get("tipofrecuencia_"+valor)+"")) < 0)
							errores.add("descripcion", new ActionMessage("errors.notEspecific","El Tipo de frecuencia del Procedimiento ["+this.mapa.get("descripcion_"+valor)+"] de la Secci髇 Cuidados Especiales de Enfermeria es Requerido"));
						
						//Periodo y tipo de periodo
						if(!(this.mapa.get("periodo_"+valor)+"").trim().equals("") &&
								Utilidades.convertirAEntero((this.mapa.get("periodo_"+valor)+"")) <= 0)
							errores.add("descripcion", new ActionMessage("errors.notEspecific","El Periodo del Procedimiento ["+this.mapa.get("descripcion_"+valor)+"] de la Secci髇 Cuidados Especiales de Enfermeria debe ser un Valor numerico Mayor a Cero"));
						else if(!(this.mapa.get("periodo_"+valor)+"").trim().equals("") 
								&& Utilidades.convertirAEntero((this.mapa.get("tipoperiodo_"+valor)+"")) < 0)
							errores.add("descripcion", new ActionMessage("errors.notEspecific","El Tipo de Periodo del Procedimiento ["+this.mapa.get("descripcion_"+valor)+"] de la Secci髇 Cuidados Especiales de Enfermeria es Requerido"));
						
						if(!(this.mapa.get("periodo_"+valor)+"").trim().equals("") 
								&& Utilidades.convertirAEntero((this.mapa.get("tipoperiodo_"+valor)+"")) > 0 
									&& ((this.mapa.get("frecuencia_"+valor)+"").trim().equals("") || Utilidades.convertirAEntero((this.mapa.get("tipofrecuencia_"+valor)+"")) < 0))
							errores.add("descripcion", new ActionMessage("errors.notEspecific","La informaci髇 de la Frecuencia del Procedimiento ["+this.mapa.get("descripcion_"+valor)+"] de la Secci髇 Cuidados Especiales de Enfermeria es Requerida para el ingreso del Periodo"));
						
						if(!(this.mapa.get("frecuencia_"+valor)+"").trim().equals("") &&
							Utilidades.convertirAEntero((this.mapa.get("frecuencia_"+valor)+"")) > 0 && 
								!(this.mapa.get("periodo_"+valor)+"").trim().equals("")	&& 
									Utilidades.convertirAEntero((this.mapa.get("tipoperiodo_"+valor)+"")) > 0)
						{
							int frecuencia = 0,periodo = 0 ;
							
							frecuencia = Utilidades.convertirAEntero((this.mapa.get("frecuencia_"+valor)+""));
							periodo = Utilidades.convertirAEntero((this.mapa.get("periodo_"+valor)+""));
							
							//Pasamos la frecuencia y el periodo a minutos
							if(Utilidades.convertirAEntero((this.mapa.get("tipofrecuencia_"+valor)+"")) == ConstantesBD.codigoTipoFrecuenciaHoras)
								frecuencia = frecuencia * 60; 
							else if (Utilidades.convertirAEntero((this.mapa.get("tipofrecuencia_"+valor)+"")) == ConstantesBD.codigoTipoFrecuenciaDias)
								frecuencia = frecuencia * 1440;
							
							if(Utilidades.convertirAEntero((this.mapa.get("tipoperiodo_"+valor)+"")) == ConstantesBD.codigoTipoFrecuenciaHoras)
								periodo = periodo * 60;
							else if(Utilidades.convertirAEntero((this.mapa.get("tipoperiodo_"+valor)+"")) == ConstantesBD.codigoTipoFrecuenciaDias)
								periodo = periodo * 1440;
							
							if(periodo < frecuencia)
								errores.add("descripcion", new ActionMessage("errors.notEspecific","La Relaci髇 Frecuencia/Tipo Frecuencia debe ser menor a la Relaci髇 Periodo/Tipo Periodo en el Procedimiento ["+this.mapa.get("descripcion_"+valor)+"] de la Secci髇 Cuidados Especiales de Enfermeria."));							
						}	
					}
				}
			}
		}
		
		if(UtilidadCadena.noEsVacio(this.otroCuidadoEnf))
		{
			String valor = "otroCuidadoEnf";
			//Frecuencia y tipo de frecuencia
			if(!(this.mapa.get("frecuencia_"+valor)+"").trim().equals("") &&
					Utilidades.convertirAEntero((this.mapa.get("frecuencia_"+valor)+"")) <= 0)
				errores.add("descripcion", new ActionMessage("errors.notEspecific","La frecuencia del Otro Cuidado ["+this.otroCuidadoEnf+"] de la Secci髇 Cuidados Especiales de Enfermeria debe ser un Valor numerico Mayor a Cero"));
			else if(!(this.mapa.get("frecuencia_"+valor)+"").trim().equals("") 
					&& Utilidades.convertirAEntero((this.mapa.get("tipofrecuencia_"+valor)+"")) < 0)
				errores.add("descripcion", new ActionMessage("errors.notEspecific","El Tipo de frecuencia del Otro Cuidado ["+this.otroCuidadoEnf+"] de la Secci髇 Cuidados Especiales de Enfermeria es Requerido"));
			
			//Periodo y tipo de periodo
			
			if(!(this.mapa.get("periodo_"+valor)+"").trim().equals("") &&
					Utilidades.convertirAEntero((this.mapa.get("periodo_"+valor)+"")) <= 0)
				errores.add("descripcion", new ActionMessage("errors.notEspecific","El Periodo del Otro Cuidado ["+this.otroCuidadoEnf+"] de la Secci髇 Cuidados Especiales de Enfermeria debe ser un Valor numerico Mayor a Cero"));
			else if(!(this.mapa.get("periodo_"+valor)+"").trim().equals("") 
					&& Utilidades.convertirAEntero((this.mapa.get("tipoperiodo_"+valor)+"")) < 0)
				errores.add("descripcion", new ActionMessage("errors.notEspecific","El Tipo de Periodo del Otro Cuidado ["+this.otroCuidadoEnf+"] de la Secci髇 Cuidados Especiales de Enfermeria es Requerido"));
			
			if(!(this.mapa.get("periodo_"+valor)+"").trim().equals("") 
					&& Utilidades.convertirAEntero((this.mapa.get("tipoperiodo_"+valor)+"")) > 0 
						&& ((this.mapa.get("frecuencia_"+valor)+"").trim().equals("") || Utilidades.convertirAEntero((this.mapa.get("tipofrecuencia_"+valor)+"")) < 0))
				errores.add("descripcion", new ActionMessage("errors.notEspecific","La informaci髇 de la Frecuencia del Otro Cuidado ["+this.otroCuidadoEnf+"] de la Secci髇 Cuidados Especiales de Enfermeria es Requerida para el ingreso del Periodo"));
			
			if(!(this.mapa.get("frecuencia_"+valor)+"").trim().equals("") &&
				Utilidades.convertirAEntero((this.mapa.get("frecuencia_"+valor)+"")) > 0 && 
					!(this.mapa.get("periodo_"+valor)+"").trim().equals("")	&& 
						Utilidades.convertirAEntero((this.mapa.get("tipoperiodo_"+valor)+"")) > 0)
			{
				int frecuencia = 0,periodo = 0 ;
				
				frecuencia = Utilidades.convertirAEntero((this.mapa.get("frecuencia_"+valor)+""));
				periodo = Utilidades.convertirAEntero((this.mapa.get("periodo_"+valor)+""));
				
				//Pasamos la frecuencia y el periodo a minutos
				if(Utilidades.convertirAEntero((this.mapa.get("tipofrecuencia_"+valor)+"")) == ConstantesBD.codigoTipoFrecuenciaHoras)
					frecuencia = frecuencia * 60; 
				else if (Utilidades.convertirAEntero((this.mapa.get("tipofrecuencia_"+valor)+"")) == ConstantesBD.codigoTipoFrecuenciaDias)
					frecuencia = frecuencia * 1440;
				
				if(Utilidades.convertirAEntero((this.mapa.get("tipoperiodo_"+valor)+"")) == ConstantesBD.codigoTipoFrecuenciaHoras)
					periodo = periodo * 60;
				else if(Utilidades.convertirAEntero((this.mapa.get("tipoperiodo_"+valor)+"")) == ConstantesBD.codigoTipoFrecuenciaDias)
					periodo = periodo * 1440;
				
				if(periodo < frecuencia)
					errores.add("descripcion", new ActionMessage("errors.notEspecific","La Relaci髇 Frecuencia/Tipo Frecuencia debe ser menor a la Relaci髇 Periodo/Tipo Periodo del Otro Cuidado ["+this.otroCuidadoEnf+"] de la Secci髇 Cuidados Especiales de Enfermeria."));							
			}
		}
				
		
		if(this.mezcla>0 
				&& Utilidades.convertirAEntero(this.listadoAlmacenesMap.get("numRegistros")+"")>0)
		{
		
			//Validacion de Justificacion NO POS para los articulos historicos		
			Iterator iterador = this.getListadoNutParent().iterator();		
			while (iterador.hasNext()) 
			{
				HashMap elemento = (HashMap)iterador.next();
	
				if (UtilidadesFacturacion.requiereJustificacioArt(con1, paciente.getCodigoConvenio() , Integer.parseInt(elemento.get("codigo")+""))
						&& (elemento.get("espos")+"").equals(""+ConstantesBD.acronimoFalseCorto) 
							&&	Utilidades.convertirADouble(this.getMapa("tipoParent_"+elemento.get("codigo"))+"") > 0)
				{
					if (UtilidadesHistoriaClinica.validarEspecialidadProfesionalSalud(con1, usuario, true)){
						
						if(UtilidadTexto.getBoolean(elemento.get("esmedicamento")+"") 
							&& !justificacionMap.containsKey(elemento.get("codigo")+"_yajustifico") 
							&& !justificacionMap.containsKey(elemento.get("codigo")+"_pendiente"))
							errores.add("Justificacion", new ActionMessage("errors.required", "Justificacion del articulo - "+elemento.get("descripcion").toString().toLowerCase()));
						
						if(!UtilidadTexto.getBoolean(elemento.get("esmedicamento")+"")
							&& !justificadoMap.get("justificado_"+elemento.get("codigo")).toString().equals(ConstantesBD.acronimoSi))
							errores.add("Justificacion", new ActionMessage("errors.required", "Justificacion del insumo - "+elemento.get("descripcion").toString().toLowerCase()));
					}else{
						errores.add("Justificacion", new ActionMessage("errors.warnings","No puede solicitar el Articulo No POS "+elemento.get("descripcion").toString().toLowerCase()+" Ya que no cumple con la validacion m茅dico especialista"));								
					}
				}
				
				//logger.info("\n esmedicamento_ -->"+elemento.get("codigo")+"  --- "+elemento.get("esmedicamento"));
				//Almacen la informaci贸n de la naturaleza del articulo
				this.setMapa("esmedicamento_"+elemento.get("codigo"),elemento.get("esmedicamento").toString());
				
				//logger.info("\n elemento -->"+this.getMapa());
				
				if(Utilidades.convertirADouble(this.getMapa("tipoParent_"+elemento.get("codigo"))+"") > 0 
						&& (this.getMapa("unidad_volumen_"+elemento.get("codigo"))+"").equals("")
							&& (this.getMapa("esmedicamento_"+elemento.get("codigo"))+"").equals(ConstantesBD.acronimoSi))
				{
					errores.add("descripcion", new ActionMessage("errors.required", "La unidad de la Unidosis del Medicamento ["+(elemento.get("descripcion")+"").toLowerCase()+"] "));
				}
				else
					if(Utilidades.convertirAEntero(this.getMapa("tipoParent_"+elemento.get("codigo"))+"") < 0)
				{
					this.setMapa("tipoParent_"+elemento.get("codigo"),"0");
				}

				existenMedicamentosMezcla = true;
			}

			//**************************************************************************************************************
					
			if(this.getMapa("codigosParent") != null)
			{
				//Para los articulos de la mezcla sin valor se llenan por defecto con 0
				for(int w=0; w<this.getNumeroElementos(); w++)
				{
					if(UtilidadesFacturacion.requiereJustificacioArt(con1, paciente.getCodigoConvenio(),Integer.parseInt(this.getMapa("art_"+w)+""))
							&& this.getMapa("espos_"+w).toString().equals("false") 
								&& Utilidades.convertirADouble(this.getMapa("tipoParent_"+this.getMapa("art_"+w).toString()).toString()) > 0)
					{
						if (UtilidadesHistoriaClinica.validarEspecialidadProfesionalSalud(con1, usuario, true)
								&& !justificacionMap.containsKey(this.getMapa("art_"+w)+""+"_yajustifico") 
								&& !justificacionMap.containsKey(this.getMapa("art_"+w)+""+"_pendiente")
								)
						{
							errores.add("Justificacion", new ActionMessage("errors.required", "Justificacion del articulo - "+this.getMapa("nombreart_"+w).toString().toLowerCase()));	
						}
						if (!UtilidadesHistoriaClinica.validarEspecialidadProfesionalSalud(con1, usuario, true) )
						{
							errores.add("Justificacion", new ActionMessage("errors.warnings","No puede solicitar el Articulo No POS "+this.getMapa("nombreart_"+w).toString().toLowerCase()+" Ya que no cumple con la validacion m茅dico especialista"));								
						}
					}
								
					int articulo = Utilidades.convertirAEntero(this.getMapaCompleto().get("art_"+w)+"", true);
					double unidosis = Utilidades.convertirADouble(this.getMapa("tipoParent_"+articulo)+"", true);
					
					if(unidosis<=0 && articulo>0)				
						this.setMapa("tipoParent_"+articulo,"0");			
					else if(unidosis>0 && articulo>0)
					{
						if(this.getMapa("unidad_volumen_"+articulo).toString().equals(""))
							errores.add("", new ActionMessage("errors.required","La unidad de la Unidosis del Medicamento ["+this.getMapa("nombreart_"+w).toString().toLowerCase()+"] "));
					}
					
					if(this.getNumeroElementos() > 0)
						existenMedicamentosMezcla = true;
					
					//Almacen la informaci贸n de la naturaleza del articulo. el medico solo puede a帽adir medicamentos
					this.setMapa("esmedicamento_"+articulo,ConstantesBD.acronimoSi);
				}
				
				//Se verifica la dosificaci贸n
				if(this.mezcla>0)
				{
					if(UtilidadTexto.isEmpty(this.dosificacion))				
						errores.add("", new ActionMessage("errors.required","La dosificaci贸n de la Mezcla"));
					
					/*if(this.volumenTotal > 0 && this.unidadVolumenTotal.equals(""))
						errores.add("", new ActionMessage("errors.required","La unidad del Volumen Total de la Mezcla "));*/
					
					if(!existenMedicamentosMezcla)
						errores.add("descripcion", new ActionMessage("errors.notEspecific","La mezcla seleccionada, no tiene art铆culos asociados. Por favor revisar"));
				}
			}
		}		
		
		UtilidadBD.closeConnection(con1);
		
		//*********************************************************************************
		
		
		if(oxigenoTerapia!=null && UtilidadCadena.noEsVacio(oxigenoTerapia) && oxigenoTerapia.equals(ValoresPorDefecto.getValorTrueParaConsultas()))
		{
			if(cantidadSoporteRespiratorio==0)
			{	
					errores.add("Campo Cantidad soporte respiratorio vacio", new ActionMessage("errors.required","El campo Cantidad Soporte Respiratorio"));
			}
			if(equipoElemento == -1 || equipoElemento == 0)
			{	
					errores.add("Campo Equipo Elemento vacio", new ActionMessage("errors.required","El campo Equipo elemento"));
			}
		}
		
		//Fecha actual y patr贸n de fecha a utilizar en las validaciones
		//final Date fechaActual = new Date();
		final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
		
		//VALIDACION DE LA HORA ORDEN
		if(this.horaOrden.trim().equals(""))
		{
			errorHora=true;	
			errores.add("Campo Hora Orden vacio", new ActionMessage("errors.required","El campo Hora Orden"));
		}
		
		//VALIDACION DE LA FECHA ORDEN
		if(this.fechaOrden.trim().equals(""))
		{	
				errores.add("Campo Fecha Orden vacio", new ActionMessage("errors.required","El campo Fecha Orden"));
		}
		else
		{
				if(!UtilidadFecha.validarFecha(this.fechaOrden))
				{
						errores.add("Fecha Orden", new ActionMessage("errors.formatoFechaInvalido", " Orden"));							
				}
				else
				{		
					boolean tieneErroresFecha=false;
					 
					//Fecha orden
					Date fechaOrden = null;
									
					try 
					{
						fechaOrden = dateFormatter.parse(this.fechaOrden);
					}	
					catch (java.text.ParseException e) 
					{
						tieneErroresFecha=true;
					}
					
					if (!tieneErroresFecha)
					{
						// Validar que la fecha hora orden no sea superior a la fecha hora actual
						if((UtilidadFecha.conversionFormatoFechaABD(fechaOrden)+horaOrden).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+UtilidadFecha.getHoraActual())>0)
						{
							if((UtilidadFecha.conversionFormatoFechaABD(fechaOrden)).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
							{
								errores.add("fecha", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "orden", "actual"));
							}
							else if(horaOrden.compareTo(UtilidadFecha.getHoraActual())>0 && !errorHora)
							{
								errores.add("Hora", new ActionMessage("errors.horaPosteriorAOtraDeReferencia", "orden", "actual"));
							}
						}
						//Validar que la fecha sea mayor a la fecha de valoraci贸n cuando el paciente est谩 por via de ingreso
						//urgencias u hospitalizaci贸n
						if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion || paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
						{
							Connection con=null;
							try
							{
									con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
							}
							catch(SQLException e)
							{
									logger.warn("No se pudo abrir la conexi贸n"+e.toString());
							}

						try
							{	
								if(paciente.getCodigoCuentaAsocio()!=0){
									valoracion = Valoraciones.cargarBase(con,UtilidadValidacion.obtenerNumeroSolicitudPrimeraValoracion(con, paciente.getCodigoCuentaAsocio())+"");
								}
								else{
									valoracion = Valoraciones.cargarBase(con,UtilidadValidacion.obtenerNumeroSolicitudPrimeraValoracion(con, paciente.getCodigoCuenta())+"");
									
									if(valoracion.getFechaValoracion() != null && !valoracion.getFechaValoracion().trim().isEmpty()
											&& valoracion.getHoraValoracion() != null && !valoracion.getHoraValoracion().trim().isEmpty()
											&& (UtilidadFecha.conversionFormatoFechaABD(valoracion.getFechaValoracion())+valoracion.getHoraValoracion().substring(0,5)).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaOrden)+horaOrden)>0)
									{
										if(UtilidadFecha.conversionFormatoFechaABD(valoracion.getFechaValoracion()).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaOrden))>0)
										{
											errores.add("fechaHora", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "orden", "de la Valoraci贸n Inicial"));
										}
										else if(valoracion.getHoraValoracion().substring(0,5).compareTo(horaOrden)>0 && !errorHora)
										{
											errores.add("fechaHora", new ActionMessage("errors.horaAnteriorAOtraDeReferencia", "orden", "de la Valoraci贸n Inicial"));
										}
									}
								}
							}
						
						catch (SQLException e)
							{
								e.printStackTrace();
							}
						UtilidadBD.closeConnection(con);
						}//if via de ingreso es urgencias u hospitalizaci贸n
					}//if no tiene errores fecha
				}//else formato fecha
		   }//ELSE FECHA ORDEN					
		
		//***********VALIDACIONES DE LA PRESCRIPCI脫N DIALISIS*****************************
		if(this.deboAbrirPrescripcionDialisis)
			errores = this.dialisis.validate(errores);
		//********************************************************************************
		
	}//tiene errores
	
	
	
	if(!errores.isEmpty())
	{
		if(estado.equals("salir"))
			this.setEstado("empezar");
	}
	
	
	return errores;
	
}
/**
 * @return Retorna volumenTotal.
 */
public String getVolumenTotal() {
	return volumenTotal;
}
/**
 * @param Asigna volumenTotal.
 */
public void setVolumenTotal(String volumenTotal) {
	this.volumenTotal = volumenTotal;
}


/**
 * @return Retorna otroNutORal.
 */
public String getOtroNutORal() {
	return otroNutORal;
}
/**
 * @param Asigna otroNutORal.
 */
public void setOtroNutORal(String otroNutORal) {
	this.otroNutORal = otroNutORal;
}

/**
 * @return Returns the cuidadosEnfermeria.
 */
public HashMap getCuidadosEnfermeria() {
	return cuidadosEnfermeria;
}
/**
 * @param cuidadosEnfermeria The cuidadosEnfermeria to set.
 */
public void setCuidadosEnfermeria(HashMap cuidadosEnfermeria) {
	this.cuidadosEnfermeria = cuidadosEnfermeria;
}
/**
 * @return Returns the equipoElemento.
 */
public int getEquipoElemento() {
	return equipoElemento;
}
/**
 * @param equipoElemento The equipoElemento to set.
 */
public void setEquipoElemento(int equipoElemento) {
	this.equipoElemento = equipoElemento;
}
/**
 * @return Returns the cantidadSoporteRespiratorio.
 */
public float getCantidadSoporteRespiratorio()
{
	return cantidadSoporteRespiratorio;
}
/**
 * @param cantidadSoporteRespiratorio The cantidadSoporteRespiratorio to set.
 */
public void setCantidadSoporteRespiratorio(float cantidadSoporteRespiratorio)
{
	this.cantidadSoporteRespiratorio = cantidadSoporteRespiratorio;
}
	/**
	 * @return Returns the estado.
	 */
	public String getEstado() {
		return estado;
	}
	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
/**
 * @return Returns the farmacia.
 */
public int getFarmacia() {
	return farmacia;
}
/**
 * @param farmacia The farmacia to set.
 */
public void setFarmacia(int farmacia) {
	this.farmacia = farmacia;
}
/**
 * @return Returns the fechaOrden.
 */
public String getFechaOrden() {
	return fechaOrden;
}
/**
 * @param fechaOrden The fechaOrden to set.
 */
public void setFechaOrden(String fechaOrden) {
	this.fechaOrden = fechaOrden;
}
/**
 * @return Returns the finalizarDieta.
 */
public boolean isFinalizarDieta() {
	return finalizarDieta;
}
/**
 * @param finalizarDieta The finalizarDieta to set.
 */
public void setFinalizarDieta(boolean finalizarDieta) {
	this.finalizarDieta = finalizarDieta;
}
/**
 * @return Returns the finalizarSoporte.
 */
public boolean isFinalizarSoporte() {
	return finalizarSoporte;
}
/**
 * @param finalizarSoporte The finalizarSoporte to set.
 */
public void setFinalizarSoporte(boolean finalizarSoporte) {
	this.finalizarSoporte = finalizarSoporte;
}
/**
 * @return Returns the horaOrden.
 */
public String getHoraOrden() {
	return horaOrden;
}
/**
 * @param horaOrden The horaOrden to set.
 */
public void setHoraOrden(String horaOrden) {
	this.horaOrden = horaOrden;
}




//----------Funcion del hashMap----------------

public boolean isFinalizarDietaEnfermeria() {
	return finalizarDietaEnfermeria;
}

public void setFinalizarDietaEnfermeria(boolean finalizarDietaEnfermeria) {
	this.finalizarDietaEnfermeria = finalizarDietaEnfermeria;
}

/**
 * @return Returna la propiedad del mapa mapa.
 */
public Object getMapa(String key)
{
	return mapa.get(key);
}
/**
 * @param Asigna la propiedad al mapa
 */
public void setMapa(String key, Object value)
{
	this.mapa.put(key, value);
}

/**
 * @return Returns the nutricion.
 */
public HashMap getMapaCompleto() {
	return mapa;
}
/**
 * @param nutricion The nutricion to set.
 */
public void setMapaCompleto(HashMap mapa) {
	this.mapa = mapa;
}

//----------Funcion del hashMap temporal----------------

/**
 * @return Returna la propiedad del tempMapa
 */
public Object getTempMapa(String key)
{
	return tempMapa.get(key);
}
/**
 * @param Asigna la propiedad al tempMapa
 */
public void setTempMapa(String key, Object value)
{
	this.tempMapa.put(key, value);
}

/**
 * @return Returns.
 */
public HashMap getTempMapaCompleto() {
	return tempMapa;
}
/**
 * @param tempMapa
 */
public void setTempMapaCompleto(HashMap tempMapa) {
	this.tempMapa = tempMapa;
}

//---------------------------------------------

/**
 * @return Returns the nutricionOral.
 */
public boolean isNutricionOral() {
	return nutricionOral;
}
/**
 * @param nutricionOral The nutricionOral to set.
 */
public void setNutricionOral(boolean nutricionOral) {
	this.nutricionOral = nutricionOral;
}
/**
 * @return Returns the nutricionParenteral.
 */
public boolean isNutricionParenteral() {
	return nutricionParenteral;
}
/**
 * @param nutricionParenteral The nutricionParenteral to set.
 */
public void setNutricionParenteral(boolean nutricionParenteral) {
	this.nutricionParenteral = nutricionParenteral;
}
/**
 * @return Returns the observacionesGenerales.
 */
public String getObservacionesGenerales() {
	return observacionesGenerales;
}
/**
 * @param observacionesGenerales The observacionesGenerales to set.
 */
public void setObservacionesGenerales(String observacionesGenerales) {
	this.observacionesGenerales = observacionesGenerales;
}
/**
 * @return Returns the tipoMonitoreo.
 */
public int getTipoMonitoreo() {
	return tipoMonitoreo;
}
/**
 * @param tipoMonitoreo The tipoMonitoreo to set.
 */
public void setTipoMonitoreo(int tipoMonitoreo) {
	this.tipoMonitoreo = tipoMonitoreo;
}
/**
 * @return Returns the descripcionDieta.
 */
public String getDescripcionDieta() {
	return descripcionDieta;
}
/**
 * @param descripcionDieta The descripcionDieta to set.
 */
public void setDescripcionDieta(String descripcionDieta) {
	this.descripcionDieta = descripcionDieta;
}
/**
 * @return Returns the descripcionDietaNueva.
 */
public String getDescripcionDietaNueva()
{
	return descripcionDietaNueva;
}
/**
 * @param descripcionDietaNueva The descripcionDietaNueva to set.
 */
public void setDescripcionDietaNueva(String descripcionDietaNueva)
{
	this.descripcionDietaNueva = descripcionDietaNueva;
}
	/**
	 * @return Returns the descripcionSoporteRespiratorio.
	 */
	public String getDescripcionSoporteRespiratorio() {
		return descripcionSoporteRespiratorio;
	}
	/**
	 * @param descripcionSoporteRespiratorio The descripcionSoporteRespiratorio to set.
	 */
	public void setDescripcionSoporteRespiratorio(
			String descripcionSoporteRespiratorio) {
		this.descripcionSoporteRespiratorio = descripcionSoporteRespiratorio;
	}
/**
 * @return Returns the descripcionSoporteNueva.
 */
public String getDescripcionSoporteNueva()
{
	return descripcionSoporteNueva;
}
/**
 * @param descripcionSoporteNueva The descripcionSoporteNueva to set.
 */
public void setDescripcionSoporteNueva(String descripcionSoporteNueva)
{
	this.descripcionSoporteNueva = descripcionSoporteNueva;
}

/**
 * @return Returns the oxigenoTerapia.
 */
public String getOxigenoTerapia()
{
	return oxigenoTerapia;
}
/**
 * @param oxigenoTerapia The oxigenoTerapia to set.
 */
public void setOxigenoTerapia(String oxigenoTerapia)
{
	this.oxigenoTerapia = oxigenoTerapia;
}
/**
 * @return Returns the fechaHoraValoracion.
 */
public String getFechaValoracion()
{
	return fechaValoracion;
}
/**
 * @param fechaHoraValoracion The fechaHoraValoracion to set.
 */
public void setFechaValoracion(String fechaValoracion)
{
	this.fechaValoracion = fechaValoracion;
}
/**
 * @return Returns the horaValoracion.
 */
public String getHoraValoracion()
{
	return horaValoracion;
}
/**
 * @param horaValoracion The horaValoracion to set.
 */
public void setHoraValoracion(String horaValoracion)
{
	this.horaValoracion = horaValoracion;
}

/**
 * @return Retorna listadoNutOral.
 */
public Collection getListadoNutOral() {
	return listadoNutOral;
}
/**
 * @param Asigna listadoNutOral.
 */
public void setListadoNutOral(Collection listadoNutOral) {
	this.listadoNutOral = listadoNutOral;
}

/**
 * @return Retorna listadoNutParent.
 */
public Collection getListadoNutParent() {
	return listadoNutParent;
}
/**
 * @param Asigna listadoNutParent.
 */
public void setListadoNutParent(Collection listadoNutParent) {
	this.listadoNutParent = listadoNutParent;
}


/**
 * @return Retorna listadoSoporteRes.
 */
public Collection getListadoSoporteRes() {
	return listadoSoporteRes;
}
/**
 * @param Asigna listadoSoporteRes.
 */
public void setListadoSoporteRes(Collection listadoSoporteRes) {
	this.listadoSoporteRes = listadoSoporteRes;
}
/**
 * @return Retorna listadoTiposMonitoreo.
 */
public Collection getListadoTiposMonitoreo() {
	return listadoTiposMonitoreo;
}
/**
 * @return Retorna observacionesGeneralesNueva.
 */
public String getObservacionesGeneralesNueva() {
	return observacionesGeneralesNueva;
}
/**
 * @param Asigna observacionesGeneralesNueva.
 */
public void setObservacionesGeneralesNueva(String observacionesGeneralesNueva) {
	this.observacionesGeneralesNueva = observacionesGeneralesNueva;
}
/**
 * @param Asigna listadoTiposMonitoreo.
 */
public void setListadoTiposMonitoreo(Collection listadoTiposMonitoreo) {
	this.listadoTiposMonitoreo = listadoTiposMonitoreo;
}
/**
 * @return Retorna listadoCuidadosEmfermeria.
 */
public Collection getListadoCuidadosEmfermeria() {
	return listadoCuidadosEmfermeria;
}
/**
 * @param Asigna listadoCuidadosEmfermeria.
 */
public void setListadoCuidadosEmfermeria(Collection listadoCuidadosEmfermeria) {
	this.listadoCuidadosEmfermeria = listadoCuidadosEmfermeria;
}

/**
 * @return Retorna otroCuidadoEnf.
 */
public String getOtroCuidadoEnf() {
	return otroCuidadoEnf;
}
/**
 * @param Asigna otroCuidadoEnf.
 */
public void setOtroCuidadoEnf(String otroCuidadoEnf) {
	this.otroCuidadoEnf = otroCuidadoEnf;
}

/**
 * @return Retorna infOrdenMedica.
 */
public Collection getInfOrdenMedica() {
	return InfOrdenMedica;
}
/**
 * @param Asigna infOrdenMedica.
 */
public void setInfOrdenMedica(Collection infOrdenMedica) {
	InfOrdenMedica = infOrdenMedica;
}

/**
 * @return Retorna listadoNutParentHisto.
 */
public Collection getListadoNutParentHisto() {
	return listadoNutParentHisto;
}
/**
 * @param Asigna listadoNutParentHisto.
 */
public void setListadoNutParentHisto(Collection listadoNutParentHisto) {
	this.listadoNutParentHisto = listadoNutParentHisto;
}
/**
 * @return Retorna monitoreosHisto.
 */
public Collection getMonitoreosHisto() {
	return monitoreosHisto;
}
/**
 * @param Asigna monitoreosHisto.
 */
public void setMonitoreosHisto(Collection monitoreosHisto) {
	this.monitoreosHisto = monitoreosHisto;
}
/**
 * @return Retorna soporteRespiraHisto.
 */
public Collection getSoporteRespiraHisto() {
	return soporteRespiraHisto;
}
/**
 * @param Asigna soporteRespiraHisto.
 */
public void setSoporteRespiraHisto(Collection soporteRespiraHisto) {
	this.soporteRespiraHisto = soporteRespiraHisto;
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
 * @return Retorna listadoCuidadosEnfHisto.
 */
public Collection getListadoCuidadosEnfHisto() {
	return listadoCuidadosEnfHisto;
}
/**
 * @param Asigna listadoCuidadosEnfHisto.
 */
public void setListadoCuidadosEnfHisto(Collection listadoCuidadosEnfHisto) {
	this.listadoCuidadosEnfHisto = listadoCuidadosEnfHisto;
}
/**
 * @return Retorna posIndEnf.
 */
public int getPosIndEnf() {
	return posIndEnf;
}
/**
 * @param Asigna posIndEnf.
 */
public void setPosIndEnf(int posIndEnf) {
	this.posIndEnf = posIndEnf;
}
	/**
	 * @return Retorna posIndParent.
	 */
	public int getPosIndParent() {
		return posIndParent;
	}
	/**
	 * @param Asigna posIndParent.
	 */
	public void setPosIndParent(int posIndParent) {
		this.posIndParent = posIndParent;
	}
/**
 * @return Returns the tempCantidadSoporte.
 */
public float getTempCantidadSoporte()
{
	return tempCantidadSoporte;
}
/**
 * @param tempCantidadSoporte The tempCantidadSoporte to set.
 */
public void setTempCantidadSoporte(float tempCantidadSoporte)
{
	this.tempCantidadSoporte = tempCantidadSoporte;
}
/**
 * @return Returns the tempEquipoElemento.
 */
public int getTempEquipoElemento()
{
	return tempEquipoElemento;
}
/**
 * @param tempEquipoElemento The tempEquipoElemento to set.
 */
public void setTempEquipoElemento(int tempEquipoElemento)
{
	this.tempEquipoElemento = tempEquipoElemento;
}
/**
 * @return Returns the tempOxigenoTerapia.
 */
public String getTempOxigenoTerapia()
{
	return tempOxigenoTerapia;
}
/**
 * @param tempOxigenoTerapia The tempOxigenoTerapia to set.
 */
public void setTempOxigenoTerapia(String tempOxigenoTerapia)
{
	this.tempOxigenoTerapia = tempOxigenoTerapia;
}
/**
 * @return Returns the tempFinalizarDieta.
 */
public boolean isTempFinalizarDieta()
{
	return tempFinalizarDieta;
}
/**
 * @param tempFinalizarDieta The tempFinalizarDieta to set.
 */
public void setTempFinalizarDieta(boolean tempFinalizarDieta)
{
	this.tempFinalizarDieta = tempFinalizarDieta;
}
/**
 * @return Returns the tempNutricionOral.
 */
public boolean isTempNutricionOral()
{
	return tempNutricionOral;
}
/**
 * @param tempNutricionOral The tempNutricionOral to set.
 */
public void setTempNutricionOral(boolean tempNutricionOral)
{
	this.tempNutricionOral = tempNutricionOral;
}
	/**
	 * @return Returns the tempNutricionParenteral.
	 */
	public boolean isTempNutricionParenteral()
	{
		return tempNutricionParenteral;
	}
	/**
	 * @param tempNutricionParenteral The tempNutricionParenteral to set.
	 */
	public void setTempNutricionParenteral(boolean tempNutricionParenteral)
	{
		this.tempNutricionParenteral = tempNutricionParenteral;
	}
/**
 * @return Returns the tempOtroNutOral.
 */
public String getTempOtroNutOral()
{
	return tempOtroNutOral;
}
/**
 * @param tempOtroNutOral The tempOtroNutOral to set.
 */
public void setTempOtroNutOral(String tempOtroNutOral)
{
	this.tempOtroNutOral = tempOtroNutOral;
}
	/**
	 * @return Retorna temp.
	 */
	public String getTemp() {
		return temp;
	}
	/**
	 * @param Asigna temp.
	 */
	public void setTemp(String temp) {
		this.temp = temp;
	}
/**
 * @return Retorna tipoMonitoreoTemp.
 */
public int getTipoMonitoreoTemp() {
	return tipoMonitoreoTemp;
}
/**
 * @param Asigna tipoMonitoreoTemp.
 */
public void setTipoMonitoreoTemp(int tipoMonitoreoTemp) {
	this.tipoMonitoreoTemp = tipoMonitoreoTemp;
}
/**
 * @return Returns the listadoOtrosNutOral.
 */
public Collection getListadoOtrosNutOral()
{
	return listadoOtrosNutOral;
}
/**
 * @param listadoOtrosNutOral The listadoOtrosNutOral to set.
 */
public void setListadoOtrosNutOral(Collection listadoOtrosNutOral)
{
	this.listadoOtrosNutOral = listadoOtrosNutOral;
}
/**
 * @return Returns the observacionesGralesUrgencias.
 */
public String getObservacionesGralesUrgencias()
{
	return observacionesGralesUrgencias;
}
/**
 * @param observacionesGralesUrgencias The observacionesGralesUrgencias to set.
 */
public void setObservacionesGralesUrgencias(String observacionesGralesUrgencias)
{
	this.observacionesGralesUrgencias = observacionesGralesUrgencias;
}
/**
 * @return Returns the descripcionDietaUrgencias.
 */
public String getDescripcionDietaUrgencias()
{
	return descripcionDietaUrgencias;
}
/**
 * @param descripcionDietaUrgencias The descripcionDietaUrgencias to set.
 */
public void setDescripcionDietaUrgencias(String descripcionDietaUrgencias)
{
	this.descripcionDietaUrgencias = descripcionDietaUrgencias;
}
/**
 * @return Returns the descripcionSoporteUrgencias.
 */
public String getDescripcionSoporteUrgencias()
{
	return descripcionSoporteUrgencias;
}
/**
 * @param descripcionSoporteUrgencias The descripcionSoporteUrgencias to set.
 */
public void setDescripcionSoporteUrgencias(String descripcionSoporteUrgencias)
{
	this.descripcionSoporteUrgencias = descripcionSoporteUrgencias;
}
	/**
	 * @return Returns the otrasNutricionesOrales.
	 */
	public Vector getOtrasNutricionesOrales()
	{
		return otrasNutricionesOrales;
	}
	/**
	 * @param otrasNutricionesOrales The otrasNutricionesOrales to set.
	 */
	public void setOtrasNutricionesOrales(Vector otrasNutricionesOrales)
	{
		this.otrasNutricionesOrales = otrasNutricionesOrales;
	}
	/**
	 * @return Returns the noHaySoporte.
	 */
	public boolean isNoHaySoporte()
	{
		return noHaySoporte;
	}
	/**
	 * @param noHaySoporte The noHaySoporte to set.
	 */
	public void setNoHaySoporte(boolean noHaySoporte)
	{
		this.noHaySoporte = noHaySoporte;
	}
/**
 * @return Returns the listadoOtrosCuidadosEnfer.
 */
public Collection getListadoOtrosCuidadosEnfer()
{
	return listadoOtrosCuidadosEnfer;
}
/**
 * @param listadoOtrosCuidadosEnfer The listadoOtrosCuidadosEnfer to set.
 */
public void setListadoOtrosCuidadosEnfer(Collection listadoOtrosCuidadosEnfer)
{
	this.listadoOtrosCuidadosEnfer = listadoOtrosCuidadosEnfer;
}
/**
 * @return Returns the fechaTempOrden.
 */
public String getFechaTempOrden()
{
	return fechaTempOrden;
}
/**
 * @param fechaTempOrden The fechaTempOrden to set.
 */
public void setFechaTempOrden(String fechaTempOrden)
{
	this.fechaTempOrden = fechaTempOrden;
}
/**
 * @return Returns the horaTempOrden.
 */
public String getHoraTempOrden()
{
	return horaTempOrden;
}
/**
 * @param horaTempOrden The horaTempOrden to set.
 */
public void setHoraTempOrden(String horaTempOrden)
{
	this.horaTempOrden = horaTempOrden;
}


/**
 * @return Retorna the finalizadaHojaNeuro.
 */
public String getFinalizadaHojaNeuro()
{
	return finalizadaHojaNeuro;
}


/**
 * @param finalizadaHojaNeuro The finalizadaHojaNeuro to set.
 */
public void setFinalizadaHojaNeuro(String finalizadaHojaNeuro)
{
	this.finalizadaHojaNeuro = finalizadaHojaNeuro;
}


/**
 * @return Retorna the observacionesHojaNeuro.
 */
public String getObservacionesHojaNeuro()
{
	return observacionesHojaNeuro;
}


/**
 * @param observacionesHojaNeuro The observacionesHojaNeuro to set.
 */
public void setObservacionesHojaNeuro(String observacionesHojaNeuro)
{
	this.observacionesHojaNeuro = observacionesHojaNeuro;
}


/**
 * @return Retorna the presentaHojaNeuro.
 */
public String getPresentaHojaNeuro()
{
	return presentaHojaNeuro;
}


/**
 * @param presentaHojaNeuro The presentaHojaNeuro to set.
 */
public void setPresentaHojaNeuro(String presentaHojaNeuro)
{
	this.presentaHojaNeuro = presentaHojaNeuro;
}


/**
 * @return Retorna the observacionesHojaNeuroNueva.
 */
public String getObservacionesHojaNeuroNueva()
{
	return observacionesHojaNeuroNueva;
}


/**
 * @param observacionesHojaNeuroNueva The observacionesHojaNeuroNueva to set.
 */
public void setObservacionesHojaNeuroNueva(String observacionesHojaNeuroNueva)
{
	this.observacionesHojaNeuroNueva = observacionesHojaNeuroNueva;
}


/**
 * @return Retorna descripcionDietaParenteral.
 */
public String getDescripcionDietaParenteral()
{
	return descripcionDietaParenteral;
}


/**
 * @param descripcionDietaParenteral Asigna descripcionDietaParenteral.
 */
public void setDescripcionDietaParenteral(String descripcionDietaParenteral)
{
	this.descripcionDietaParenteral = descripcionDietaParenteral;
}


/**
 * @return Retorna descripcionDietaParenteralNueva.
 */
public String getDescripcionDietaParenteralNueva()
{
	return descripcionDietaParenteralNueva;
}


/**
 * @param descripcionDietaParenteralNueva Asigna descripcionDietaParenteralNueva.
 */
public void setDescripcionDietaParenteralNueva(
		String descripcionDietaParenteralNueva)
{
	this.descripcionDietaParenteralNueva = descripcionDietaParenteralNueva;
}


/**
 * @return Retorna mezcla.
 */
public int getMezcla()
{
	return mezcla;
}


/**
 * @param mezcla Asigna mezcla.
 */
public void setMezcla(int mezcla)
{
	this.mezcla = mezcla;
}


/**
 * @return Retorna mezclaTempo.
 */
public int getMezclaTempo()
{
	return mezclaTempo;
}


/**
 * @param mezclaTempo Asigna mezclaTempo.
 */
public void setMezclaTempo(int mezclaTempo)
{
	this.mezclaTempo = mezclaTempo;
}

/**
 * @return Retorna mensajeError.
 */
public String getMensajeError()
{
	return mensajeError;
}

/**
 * @param mensajeError Asigna mensajeError.
 */
public void setMensajeError(String mensajeError)
{
	this.mensajeError = mensajeError;
}

/**
 * @param parametroMensaje Asigna parametroMensaje.
 */
public void setParametroMensaje(String parametroMensaje)
{
	this.parametroMensaje = parametroMensaje;
}

/**
 * @return Retorna parametroMensaje.
 */
public String getParametroMensaje()
{
	return parametroMensaje;
}


/**
 * @return Retorna nombreFarmacia.
 */
public String getNombreFarmacia()
{
	return nombreFarmacia;
}

/**
 * @return Retorna the mezclasFinalizadasAnteriores.
 */
public Collection getMezclasFinalizadasAnteriores()
{
	return mezclasFinalizadasAnteriores;
}


/**
 * @param nombreFarmacia Asigna nombreFarmacia.
 */
public void setNombreFarmacia(String nombreFarmacia)
{
	this.nombreFarmacia = nombreFarmacia;
}

/**
 * @param mezclasFinalizadasAnteriores The mezclasFinalizadasAnteriores to set.
 */
public void setMezclasFinalizadasAnteriores(
		Collection mezclasFinalizadasAnteriores)
{
	this.mezclasFinalizadasAnteriores = mezclasFinalizadasAnteriores;
}


/**
 * @return Retorna the detalleArticulosMezclaAnteriores.
 */
public Collection getDetalleArticulosMezclaAnteriores()
{
	return detalleArticulosMezclaAnteriores;
}


/**
 * @param detalleArticulosMezclaAnteriores The detalleArticulosMezclaAnteriores to set.
 */
public void setDetalleArticulosMezclaAnteriores(
		Collection detalleArticulosMezclaAnteriores)
{
	this.detalleArticulosMezclaAnteriores = detalleArticulosMezclaAnteriores;
}


/**
 * @return Retorna the codEncabezadoAnterior.
 */
public int getCodEncabezadoAnterior()
{
	return codEncabezadoAnterior;
}


/**
 * @param codEncabezadoAnterior The codEncabezadoAnterior to set.
 */
public void setCodEncabezadoAnterior(int codEncabezadoAnterior)
{
	this.codEncabezadoAnterior = codEncabezadoAnterior;
}


/**
 * @return Retorna the codEncabezadoMinAnterior.
 */
public int getCodEncabezadoMinAnterior()
{
	return codEncabezadoMinAnterior;
}


/**
 * @param codEncabezadoMinAnterior The codEncabezadoMinAnterior to set.
 */
public void setCodEncabezadoMinAnterior(int codEncabezadoMinAnterior)
{
	this.codEncabezadoMinAnterior = codEncabezadoMinAnterior;
}


/**
 * @return Retorna the mezclaAnterior.
 */
public int getMezclaAnterior()
{
	return mezclaAnterior;
}


/**
 * @param mezclaAnterior The mezclaAnterior to set.
 */
public void setMezclaAnterior(int mezclaAnterior)
{
	this.mezclaAnterior = mezclaAnterior;
}


/**
 * @return Retorna the listadoNutParentAnterior.
 */
public Collection getListadoNutParentAnterior()
{
	return listadoNutParentAnterior;
}


/**
 * @param listadoNutParentAnterior The listadoNutParentAnterior to set.
 */
public void setListadoNutParentAnterior(Collection listadoNutParentAnterior)
{
	this.listadoNutParentAnterior = listadoNutParentAnterior;
}


/**
 * @return Retorna listadoAlmacenesMap.
 */
public HashMap getListadoAlmacenesMap()
{
	return listadoAlmacenesMap;
}


/**
 * @param listadoAlmacenesMap Asigna listadoAlmacenesMap.
 */
public void setListadoAlmacenesMap(HashMap listadoAlmacenesMap)
{
	this.listadoAlmacenesMap = listadoAlmacenesMap;
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
 * @return Retorna listadoMezclasSinFinalizar.
 */
public Collection getListadoMezclasSinFinalizar()
{
	return listadoMezclasSinFinalizar;
}

/**
 * @param listadoMezclasSinFinalizar Asigna listadoMezclasSinFinalizar.
 */
public void setListadoMezclasSinFinalizar(Collection listadoMezclasSinFinalizar)
{
	this.listadoMezclasSinFinalizar = listadoMezclasSinFinalizar;
}

public HashMap getMezclaModificar()
{
	return mezclaModificar;
}

public void setMezclaModificar(HashMap mezclaModificar)
{
	this.mezclaModificar = mezclaModificar;
}

public Object getMezclaModificar(String key)
{
	return mezclaModificar.get(key);
}

public void setMezclaModificar(String key,Object value)
{
	this.mezclaModificar.put(key, value);
}


public String getNumeroOrdenModificar()
{
	return numeroOrdenModificar;
}

public void setNumeroOrdenModificar(String numeroOrdenModificar)
{
	this.numeroOrdenModificar = numeroOrdenModificar;
}

public String getNumeroSolicitudModificar()
{
	return numeroSolicitudModificar;
}

public void setNumeroSolicitudModificar(String numeroSolicitudModificar)
{
	this.numeroSolicitudModificar = numeroSolicitudModificar;
}

public String getCodigoArticuloMezcla()
{
	return codigoArticuloMezcla;
}

public void setCodigoArticuloMezcla(String codigoArticuloMezcla)
{
	this.codigoArticuloMezcla = codigoArticuloMezcla;
}

public String getNombreArticuloMezcla()
{
	return nombreArticuloMezcla;
}

public void setNombreArticuloMezcla(String nombreArticuloMezcla)
{
	this.nombreArticuloMezcla = nombreArticuloMezcla;
}

public Collection getListadoCuidadosEmfermeriaPopUp()
{
	return listadoCuidadosEmfermeriaPopUp;
}

public void setListadoCuidadosEmfermeriaPopUp(
		Collection listadoCuidadosEmfermeriaPopUp)
{
	this.listadoCuidadosEmfermeriaPopUp = listadoCuidadosEmfermeriaPopUp;
}

public Collection getListadoCuidadosEnfHistoPopUp()
{
	return listadoCuidadosEnfHistoPopUp;
}

public void setListadoCuidadosEnfHistoPopUp(
		Collection listadoCuidadosEnfHistoPopUp)
{
	this.listadoCuidadosEnfHistoPopUp = listadoCuidadosEnfHistoPopUp;
}

public HashMap getListadoHistoCuidadosEsp()
{
	return listadoHistoCuidadosEsp;
}

public void setListadoHistoCuidadosEsp(HashMap listadoHistoCuidadosEsp)
{
	this.listadoHistoCuidadosEsp = listadoHistoCuidadosEsp;
}

public String getFechaCuidado()
{
	return fechaCuidado;
}

public void setFechaCuidado(String fechaCuidado)
{
	this.fechaCuidado = fechaCuidado;
}

/**
 * @return the deboAbrirReferencia
 */
public boolean isDeboAbrirReferencia() {
	return deboAbrirReferencia;
}

/**
 * @param deboAbrirReferencia the deboAbrirReferencia to set
 */
public void setDeboAbrirReferencia(boolean deboAbrirReferencia) {
	this.deboAbrirReferencia = deboAbrirReferencia;
}

/**
 * @return the diagnosticosDefinitivos
 */
public HashMap getDiagnosticosDefinitivos() {
	return diagnosticosDefinitivos;
}

/**
 * @param diagnosticosDefinitivos the diagnosticosDefinitivos to set
 */
public void setDiagnosticosDefinitivos(HashMap diagnosticosDefinitivos) {
	this.diagnosticosDefinitivos = diagnosticosDefinitivos;
}

/**
 * @return the diagnosticosPresuntivos
 */
public HashMap getDiagnosticosPresuntivos() {
	return diagnosticosPresuntivos;
}

/**
 * @param diagnosticosPresuntivos the diagnosticosPresuntivos to set
 */
public void setDiagnosticosPresuntivos(HashMap diagnosticosPresuntivos) {
	this.diagnosticosPresuntivos = diagnosticosPresuntivos;
}

/**
 * @return the justificacionMap
 */
public HashMap getJustificacionMap() {
	return justificacionMap;
}

/**
 * @param justificacionMap the justificacionMap to set
 */
public void setJustificacionMap(HashMap justificacionMap) {
	this.justificacionMap = justificacionMap;
}

/**
 * @return the medicamentosNoPosMap
 */
public HashMap getMedicamentosNoPosMap() {
	return medicamentosNoPosMap;
}

/**
 * @param medicamentosNoPosMap the medicamentosNoPosMap to set
 */
public void setMedicamentosNoPosMap(HashMap medicamentosNoPosMap) {
	this.medicamentosNoPosMap = medicamentosNoPosMap;
}

/**
 * @return the medicamentosPosMap
 */
public HashMap getMedicamentosPosMap() {
	return medicamentosPosMap;
}

/**
 * @param medicamentosPosMap the medicamentosPosMap to set
 */
public void setMedicamentosPosMap(HashMap medicamentosPosMap) {
	this.medicamentosPosMap = medicamentosPosMap;
}

/**
 * @return the numjus
 */
public int getNumjus() {
	return numjus;
}

/**
 * @param numjus the numjus to set
 */
public void setNumjus(int numjus) {
	this.numjus = numjus;
}

/**
 * @return the sustitutosNoPosMap
 */
public HashMap getSustitutosNoPosMap() {
	return sustitutosNoPosMap;
}

/**
 * @param sustitutosNoPosMap the sustitutosNoPosMap to set
 */
public void setSustitutosNoPosMap(HashMap sustitutosNoPosMap) {
	this.sustitutosNoPosMap = sustitutosNoPosMap;
}

/**
 * @return the numeroElementos
 */
public int getNumeroElementos() {
	return numeroElementos;
}

/**
 * @param numeroElementos the numeroElementos to set
 */
public void setNumeroElementos(int numeroElementos) {
	this.numeroElementos = numeroElementos;
}

/**
 * @return the mapa
 */
public HashMap getMapa() {
	return mapa;
}

/**
 * @param mapa the mapa to set
 */
public void setMapa(HashMap mapa) {
	this.mapa = mapa;
}

/**
 * 
 * @return
 */
public boolean isTempfinalizarDietaEnfermeria() {
	return tempfinalizarDietaEnfermeria;
}

/**
 * 
 * @param tempfinalizarDietaEnfermeria
 */
public void setTempfinalizarDietaEnfermeria(boolean tempfinalizarDietaEnfermeria) {
	this.tempfinalizarDietaEnfermeria = tempfinalizarDietaEnfermeria;
}

/**
 * 
 * @return
 */
public String getInterfazNutricion() {
	return interfazNutricion;
}

/**
 * 
 * @param interfazNutricion
 */
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

public String getSuspenderEnfermeria() {
	return suspenderEnfermeria;
}

public void setSuspenderEnfermeria(String suspenderEnfermeria) {
	this.suspenderEnfermeria = suspenderEnfermeria;
}

public String getHiddens() {
	return hiddens;
}

public void setHiddens(String hiddens) {
	this.hiddens = hiddens;
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
/**
 * M茅todo para obtener el n煤mero de registros del hist贸rico de la dialisis
 * @return
 */
public int getNumHistoricoDialisis()
{
	return this.historicoDialisis.size();
}

/**
 * @return the indicadorUtilitario
 */
public String getIndicadorUtilitario() {
	return indicadorUtilitario;
}

/**
 * @param indicadorUtilitario the indicadorUtilitario to set
 */
public void setIndicadorUtilitario(String indicadorUtilitario) {
	this.indicadorUtilitario = indicadorUtilitario;
}

/**
 * @param velocidadInfusion the velocidadInfusion to set
 */
public void setVelocidadInfusion(String velocidadInfusion) {
	this.velocidadInfusion = velocidadInfusion;
}

/**
 * @return the velocidadInfusion
 */
public String getVelocidadInfusion() {
	return velocidadInfusion;
}

public boolean isModificacionRegistroEnfermeria() {
	return modificacionRegistroEnfermeria;
}

public void setModificacionRegistroEnfermeria(
		boolean modificacionRegistroEnfermeria) {
	this.modificacionRegistroEnfermeria = modificacionRegistroEnfermeria;
}

public void resetSecciones()
{
	this.mapaSecciones=new HashMap();
	this.mapaSecciones.put("seccionParenteral", "false");
}

public boolean isPuedoModifcarMezcla() {
	return puedoModifcarMezcla;
}

public void setPuedoModifcarMezcla(boolean puedoModifcarMezcla) {
	this.puedoModifcarMezcla = puedoModifcarMezcla;
}

public String getDosificacion() {
	return dosificacion;
}

public void setDosificacion(String dosificacion) {
	this.dosificacion = dosificacion;
}


/**
 * @return the unidadVolumenTotal
 */
public String getUnidadVolumenTotal() {
	return unidadVolumenTotal;
}


/**
 * @param unidadVolumenTotal the unidadVolumenTotal to set
 */
public void setUnidadVolumenTotal(String unidadVolumenTotal) {
	this.unidadVolumenTotal = unidadVolumenTotal;
}

/**
 * @param esPosArticuloMezcla the esPosArticuloMezcla to set
 */
public void setEsPosArticuloMezcla(boolean esPosArticuloMezcla) {
	this.esPosArticuloMezcla = esPosArticuloMezcla;
}


/**
 * @return the esPosArticuloMezcla
 */
public boolean isEsPosArticuloMezcla() {
	return esPosArticuloMezcla;
}


/**
 * @return the justificacionHistoricoMap
 */
public HashMap getJustificacionHistoricoMap() {
	return justificacionHistoricoMap;
}


/**
 * @param justificacionHistoricoMap the justificacionHistoricoMap to set
 */
public void setJustificacionHistoricoMap(HashMap justificacionHistoricoMap) {
	this.justificacionHistoricoMap = justificacionHistoricoMap;
}


/**
 * @return the hiddensHistorico
 */
public String getHiddensHistorico() {
	return hiddensHistorico;
}


/**
 * @param hiddensHistorico the hiddensHistorico to set
 */
public void setHiddensHistorico(String hiddensHistorico) {
	this.hiddensHistorico = hiddensHistorico;
}


/**
 * @return the observacionesMezclasMap
 */
public HashMap getObservacionesMezclasMap() {
	return observacionesMezclasMap;
}


/**
 * @param observacionesMezclasMap the observacionesMezclasMap to set
 */
public void setObservacionesMezclasMap(HashMap observacionesMezclasMap) {
	this.observacionesMezclasMap = observacionesMezclasMap;
}

/**
 * @return the observacionesMezclasMap
 */
public Object getObservacionesMezclasMap(String key) {
	return observacionesMezclasMap.get(key);
}

/**
 * @param observacionesMezclasMap the observacionesMezclasMap to set
 */
public void setObservacionesMezclasMap(String key, Object value) {
	this.observacionesMezclasMap.put(key, value);
}


/**
 * @return the tipoTransaccionPedido
 */
public String getTipoTransaccionPedido() {
	return tipoTransaccionPedido;
}


/**
 * @param tipoTransaccionPedido the tipoTransaccionPedido to set
 */
public void setTipoTransaccionPedido(String tipoTransaccionPedido) {
	this.tipoTransaccionPedido = tipoTransaccionPedido;
}


/**
 * @return the arrayTipoFrecuencias
 */
public ArrayList<HashMap<String, Object>> getArrayTipoFrecuencias() {
	return arrayTipoFrecuencias;
}


/**
 * @param arrayTipoFrecuencias the arrayTipoFrecuencias to set
 */
public void setArrayTipoFrecuencias(
		ArrayList<HashMap<String, Object>> arrayTipoFrecuencias) {
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
	 * @return the justificadoMap
	 */
	public HashMap getJustificadoMap() {
		return justificadoMap;
	}
	
	/**
	 * @param justificadoMap the justificadoMap to set
	 */
	public void setJustificadoMap(HashMap justificadoMap) {
		this.justificadoMap = justificadoMap;
	}

	/**
	 * @return the justificadoMap
	 */
	public Object getJustificadoMap(String llave) {
		return justificadoMap.get(llave);
	}
	
	/**
	 * @param justificadoMap the justificadoMap to set
	 */
	public void setJustificadoMap(String llave, Object obj) {
		this.justificadoMap.put(llave, obj);
	}


	public ArrayList<Object> getResultadoLaboratorios() {
		return resultadoLaboratorios;
	}


	public void setResultadoLaboratorios(
			ArrayList<Object> resultadoLaboratorios) {
		this.resultadoLaboratorios = resultadoLaboratorios;
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


	public String getEtiquetaCampoOtro() {
		return etiquetaCampoOtro;
	}


	public void setEtiquetaCampoOtro(String etiquetaCampoOtro) {
		this.etiquetaCampoOtro = etiquetaCampoOtro;
	}


	public int getPosPagerHistoricos() {
		return posPagerHistoricos;
	}


	public void setPosPagerHistoricos(int posPagerHistoricos) {
		this.posPagerHistoricos = posPagerHistoricos;
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

	/**
	 * @param existeNuevoRegObservacionesGral the existeNuevoRegObservacionesGral to set
	 */
	public void setExisteNuevoRegObservacionesGral(
			boolean existeNuevoRegObservacionesGral) {
		this.existeNuevoRegObservacionesGral = existeNuevoRegObservacionesGral;
	}


	/**
	 * @return the existeNuevoRegObservacionesGral
	 */
	public boolean isExisteNuevoRegObservacionesGral() {
		return existeNuevoRegObservacionesGral;
	}

	/**
	 * @return the codigoEncabezadoSoporteRespira
	 */
	public int getCodigoEncabezadoSoporteRespira() {
		return codigoEncabezadoSoporteRespira;
	}

	/**
	 * @param codigoEncabezadoSoporteRespira the codigoEncabezadoSoporteRespira to set
	 */
	public void setCodigoEncabezadoSoporteRespira(int codigoEncabezadoSoporteRespira) {
		this.codigoEncabezadoSoporteRespira = codigoEncabezadoSoporteRespira;
	}

	/**
	 * @return the descripcionIndivSoporteRespira
	 */
	public String getDescripcionIndivSoporteRespira() {
		return descripcionIndivSoporteRespira;
	}

	/**
	 * @param descripcionIndivSoporteRespira the descripcionIndivSoporteRespira to set
	 */
	public void setDescripcionIndivSoporteRespira(
			String descripcionIndivSoporteRespira) {
		this.descripcionIndivSoporteRespira = descripcionIndivSoporteRespira;
	}

	//MT2300
	public Boolean getProcesoExitoso() {
		return procesoExitoso;
	}


	public void setProcesoExitoso(Boolean procesoExitoso) {
		this.procesoExitoso = procesoExitoso;
	}
}
