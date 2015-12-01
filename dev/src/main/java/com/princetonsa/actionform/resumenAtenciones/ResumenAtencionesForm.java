/*
 * Created on 11/06/2004
 *
 * Juand David Ramírez
 * Princeton S.A.
 */
package com.princetonsa.actionform.resumenAtenciones;
 
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.InfoDatos;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.manejoPaciente.InfoIngresoCuenta;

import com.princetonsa.dto.historiaClinica.DtoCodigosCirugiaPeticiones;
import com.princetonsa.dto.historiaClinica.DtoEspecialidadesHC;
import com.princetonsa.dto.historiaClinica.DtoIngresoHistoriaClinica;
import com.princetonsa.dto.historiaClinica.DtoPreanestesia;
import com.princetonsa.dto.historiaClinica.DtoViasIngresoHC;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoComponente;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoEscala;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.dto.odontologia.DtoAntecendenteOdontologico;
import com.princetonsa.mundo.antecedentes.AntecedentePediatrico;
import com.servinte.axioma.dto.historiaClinica.InfoIngresoDto;
import com.servinte.axioma.dto.manejoPaciente.DtoNotaAclaratoria;
import com.servinte.axioma.orm.CentroAtencion;

/**
 * @author juanda
 * 
 */
public class ResumenAtencionesForm extends ActionForm
{
	
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(ResumenAtencionesForm.class);
	/**
	 * Manejo del tipo de valoracion
	 * Hospitalizacion General
	 * Hospitalizacion Pediátrica
	 * Urgencias General
	 * Urgencias Pediátrica
	 */
	private int tipoValoracionInicial;
	
	private int numeroAdministraciones;
	
	private String nomViaIngreso;
	/**
	 * Manejo de la vía de ingreso del paciente
	 */
	private int viaIngreso;

	/**
	 * Manejo de la fecha de ingreso del paciente.
	 */
	private String fechIngreso;
	/**
	 * Manejo de la fecha de egreso del paciente.
	 */
	private String fechEgreso;
	/**
	 * Manejar la cuenta del usuario para el resumen
	 */
	private int cuenta;
	
	/**
	 * Maneja el id de ingreso 
	 */
	private String idIngreso;
	
	/**
	 * Posicion del articulo seleccionado en el mapa de administracion de medicamentos
	 */
	private int posArticulo;
	
	/**
	 * Manejo de resumenes para imprimir en los resumenes y listados
	 * Los ingresos y los antecedentes están aparte porque
	 * es necesario encontrarlos en la misma página
	 */
	private Collection datosAdmision;
	
	/**
	 * Colleccion que contiene las cuentas
	 * De acuerdo con el diseño, en el resumen de atenciones aparece "Ingresos"
	 * pero lo que realmente se está mostrando son las cuentas
	 */
	private Collection cuentas;
	
	/**
	 * Colleccion que contiene los antecedentes del paciente
	 * En esta collección se pueden manejar todos los antecedentes, 
	 * ya que en cada pantallazo sólo se va a mostrar un tipo de antecedentes
	 */
	private Collection antecedentes;
	
	private boolean quitarEncabezados=false;
	
	//**************SECCION DE LOS ANTECEDENTES***************************************************
	//Antecedentes alergias
	private boolean existenAntecedentesAlergias;
	private HashMap mapaAntAlergia = new HashMap();

	//Antecedentes familiares
	private boolean existenAntecedentesFamiliares;
	private HashMap mapaAntFamiliares = new HashMap();

	//Antecedentes Familiares Oculares
	private boolean existenAntecedentesOftalFamiliares;
	private HashMap mapaAntFamOftal = new HashMap();

	//Antecedentes personales oculares
	private boolean existenAntecedentesOftalPersonales;
	private HashMap mapaAntPersoOftal = new HashMap();
	
	
	//Antecedentes gineco-obstétricos
	private boolean existenAntecedentesGinecoObstetricos;
	private HashMap mapaAntGineco = new HashMap();

	//Antecedentes Medicamentos
	private boolean existenAntecedentesMedicamentos;
	private HashMap mapaAntMedicamento = new HashMap();

	//Antecedentes Médicos
	private boolean existenAntecedentesMedicos;
	private HashMap mapaAntMedicos = new HashMap();

	//Antecedentes Tóxicos
	private boolean existenAntecedentesToxicos;
	private HashMap mapaAntToxicos = new HashMap();
	
	//Antecedentes Trasnfusionales
	private boolean existenAntecedentesTransfusionales;
	private HashMap mapaAntTransfusionales = new HashMap();
	
	//Antecedentes vacunas
	private boolean existenAntecedentesVacunas;
	private HashMap mapaVacunas = new HashMap();
	private HashMap mapaTiposInmunizacion = new HashMap();
	
	//Antecedentes Varios
	private boolean existenAntecedentesVarios;
	private HashMap mapaAntOtros = new HashMap();
	
	//antecedentes pediatricos
	private boolean existenAntecedentesPediatricos;
	private HashMap mapaAntPediatricos = new HashMap();
	private AntecedentePediatrico antPed;
	private HashMap tiposParto = new HashMap();
	private HashMap motivosTiposParto = new HashMap();
	private ArrayList tiposPartoList = new ArrayList();
	private HashMap tiposPartoCarga = new HashMap();
	private HashMap motivosTiposPartoCarga = new HashMap();
	private ArrayList categoriaEmbarazoOpcionCampo = new ArrayList();

	
	//antecedentes odontologicos
	private boolean existenAntecedentesOdontologicos;
	private HashMap mapaAntOdonto = new HashMap();
	
	// indica si los antecedentes se visualizan en un popup
	private boolean esPopupAntecedentes;
	
	//******************************************************************************************

	/**
	 * Verificar si al paciente ya se la hizo una valoración inicial
	 */
	private boolean existeValoracionInicial;
	
	/**
	 * Verificar si el paciente tiene interconsultas
	 */
	private boolean existenInterconsultas;
	
	/**
	 * Objetos que almacenan la informacion de itnercnsultas
	 */
	private HashMap solicitudesInterConsulta = new HashMap();
	
	/**
	 * Verificar si el paciente tiene procedimientos
	 */
	private boolean existenProcedimientos;
	
	/**
	 * Verifica si el paciente tiene citas
	 */
	private boolean existenCitas;
	
	/**
	 * Mapa donde se almacena la información de la respuesta e interperetacion de procedimientos
	 */
	private HashMap respuestaInterpretacionProcedimientos = new HashMap();

	//ATRIBUTOS PARA LA RESPUESTA DE LA CIRUGIA*****************************
	private boolean existenCirugias;
	private HashMap mapaCodigosPeticionCirugia = new HashMap();
	private HashMap mapaEncabezadosHojaAnestesia = new HashMap();
	private HashMap mapaExamenesLaboratorioPreanestesia = new HashMap();
	private HashMap mapaHistoExamenesFisicosText = new HashMap();
	private HashMap mapaHistoExamenesFisicosTextArea = new HashMap();
	private HashMap mapaHistoConclusiones = new HashMap();
	private HashMap mapaHistoBalanceLiquidosHojaAnestesia = new HashMap();
	private HashMap mapaHistoMedicamentosHojaAnestesia = new HashMap();
	private Collection listadoTecAnestesiaOpcionesGral = new ArrayList();
	private Collection listadoTecAnestesiaGral = new ArrayList();
	private Collection listadoTecAnestesiaRegional = new ArrayList();
	private HashMap mapaHistoTecAnestesia = new HashMap();
	private Collection listadoSignosVitales = new ArrayList();
	private HashMap mapaHistoSignosVitales = new HashMap();
	private HashMap mapaHojaQuirur = new HashMap();
	private String numeroSolCx;
	private String codigoPeticionCx;

	/**
	 * Verificar si el paciente tiene administración de medicamentos
	 */
	private boolean existeAdminMedicamentos;
	
	/**
	 * Verifica si el paciente tiene consumo de insumos
	 */
	private boolean existeConsumoInsumos;

	/**
	 * Verificar si el paciente tiene evoluciones
	 */
	private boolean existenEvoluciones;
	/**
	 * Objeto que almacena los códigos de las evoluciones
	 */
	private HashMap codigosEvoluciones = new HashMap();
	

	private boolean existenOrdenesMedicas;
	
	/**
	 * Objetos que almacenan la informaicon de las ordenes medicas
	 */
	private HashMap ordenesMedicas = new HashMap();
	private HashMap ordenesCirugia = new HashMap();
	private HashMap ordenesMedicamentos = new HashMap();
	private HashMap ordenesProcedimientos = new HashMap();
	private HashMap ordenesInterconsultas = new HashMap();
	
	/**
	 * Mapa que almacena la información de los eventos adversos
	 */
	private HashMap eventosAdversos = new HashMap();
	
	//*********REGISTRO DE ENFERMERIA*****************************
	//atributos para las notas de enfermeria
	private boolean existeNotasEnfermeria;
	private Collection historicoAnotacionesEnfermeria;
	
	//atributos para los signos vitales
	private boolean existeSignosVitales;
	private Collection signosVitalesInstitucionCcosto;
	private Collection signosVitalesFijosHisto;
	private Collection signosVitalesParamHisto;
	private Collection signosVitalesHistoTodos;
	
	//atributos para cateteres y sondas
	private boolean existeCateterSonda;
	private Collection colCateteresSondaInstitucionCcosto;
	private Collection cateterSondaFijosHisto;
	private Collection cateterSondaParamHisto;
	private Collection cateterSondaHistoTodos;
	private HashMap mapaHistoricoCateterSonda = new HashMap();
	
	//atributos para el control de liquidos
	private boolean existeControlLiquidos;
	private HashMap mapaControlLiq = new HashMap();
	
	//atributos para el soporte respiratorio
	private boolean existeSoporteRespiratorio;
	private HashMap soporteRespiratorio = new HashMap();
	
	//atributos para los cuidados especiales
	private boolean existeCuidadosEspeciales;
	private HashMap mapaHistoricoCuidadosEspeciales = new HashMap();
	
	//atributos para la hoja neurólogica
	private boolean existeHojaNeurologica;
	private Collection escalasGlasgowInstitucionCCosto = new ArrayList();
	private HashMap mapaHistoricoEscalaGlasgow = new HashMap();
	private HashMap mapaHistoricoPupilas = new HashMap();
	private HashMap mapaHistoricoConvulsiones = new HashMap();
	private HashMap mapaHistoricoControlEsfinteres = new HashMap();
	private HashMap mapaHistoricoFuerzaMuscular = new HashMap();
	
	//atributos para las escalas
	private boolean existenEscalasXIngreso;
	private ArrayList<DtoEscala> escalas;
	
	//atributos para el listado de las valoraciones de cuidado especial
	private HashMap<String, Object> valoracionesCuidadoEspecial = new HashMap<String, Object>();
	
	//**************************************************************
	
	/**
	 * Verifica si la cuenta tiene consultas PYP
	 */
	private boolean existeConsultasPYP;

	/**
	 * Objeto que almacena el listado de consultas PYP
	 */
	private HashMap consultasPYP = new HashMap();
	
	/**
	 * Variable que almacena el numero de registros
	 * del mapa consultasPYP
	 */
	private int numConsultasPYP;
	
	/**
	 * Atributos destinados para la ordenacion del lisrtado
	 */
	private String indiceConsultasPYP;
	private String ultimoIndiceConsultasPYP;

	/**
	 * Verificar la existencia de citas para pacientes de consulta externa
	 */
	private HashMap citas = new HashMap();
	
	/**
	 * Número de citas
	 */
	private int numCitas;
	
	/**
	 * Posicion de la cita seleccionada
	 */
	private int posCita;
	/**
	 * Mapa que maneja el detalle de la cita
	 */
	private HashMap detalleCita = new HashMap();
	
	/**
	 * Npúmero del registro del detalle de la cita
	 */
	private int numDetalleCita;
	
	/**
	 * Verificar si el paciente tiene evoluciones
	 */
	private int egreso;

	/**
	 * Manejo de estados de la funcionalidad
	 */
	private String estado=""; 
	
	/**
	 * Coleccion para manejar el listado de solicitudes
	 */
	private Collection coleccionSolicitudes;

	/**
	 * Booleano para mostrar el mensaje de asocio
	 */
	private boolean mostrarMensajeAsocio;
	
	/**
	 * HashMap parfa almacenar una solicitud con sus respectivos 
	 * articulos, administracion,detalles, cirugias etc.
	 */
	private HashMap solicitudes=new HashMap();
	
	/**
	 * Variable para manejar el numero de solicitudes que tiene un paciente
	 */
	private int numeroSolicitudes;

	/**
	 * Valrible que almacena la ultima propiedad por la que se ordeno.
	 */
	private String ultimaPropiedad;
	/**
	 * Columna por la que se desea ordenar
	 */
	private String columna;
	
	/**
	 * Número de registros por pagina del listado
	 */
	private int maxPageItems;
	
	/**
	 * Offset del pager
	 */
	private int offset;
	
	/**
	 * Login usuario de la sesión
	 */
	private String usuario;
	
	/**
	 * Se valida si hay permisos para imprimir la historia clínica
	 */
	private boolean impresionHistoriaClinica;
	
	/**
	 * pagina siguiente en la paginacion
	 */
	private String linkSiguiente;
	
	/**
	 * Número de documentos adjuntos
	 */
	private int numDocumentosAdjuntos = 0;
	
	/**
	 * Colección con los nombres generados de los archivos adjuntos 
	 */
	private HashMap documentosAdjuntosGenerados;
	
	/**
	 * String con el nombre del archivo para la base de datos
	 */
	private String nombreArchivo;
	
	
	/**
	 * String con el nombre real del archivo
	 */
	private String nombreOriginal;
	
	/**
	 * Cadena para saber si el paciente cargado en session tiene hoja obstetrica
	 */
	private String existeHojaObstetrica="";
	
	/**
	 * Variable implementada para verificar si existe triage
	 */
	private boolean existeTriage;
	
	/**
	 * Objeto que lleva el listado de los triage del paciente
	 */
	private HashMap listadoTriage = new HashMap();
	
	/**
	 * Número de registros del mapa listadoTriage
	 */
	private int numTriage;
	
	/**
	 * Consecutivo del Triage seleccionado
	 */
	private String consecutivoTriage;
	
	/**
	 * Objeto que almacena el detalle del triage
	 */
	private HashMap detalleTriage = new HashMap();
	
	/**
	 * Indica si la cuenta tiene accidentes de tránsito
	 */
	private boolean accidenteTransito;
	
	
	/**
	 * Indica si la cuenta tiene evento catastrofico
	 */
	private boolean eventoCatastrofico;
	
	/**
	 * Objetos usados para almacenar la información de accidentes de tránsito
	 */
	private HashMap registroAccidentesTransitoReporte1 = new HashMap();
	private HashMap registroAccidentesTransitoReporte2 = new HashMap();
	
	
	/**
	 * Almacena la informacion de la hoja de administración de medicamentos
	 */
	private HashMap adminMedicamentos = new HashMap();
	
	/**
	 * Almacena el detalle de la adminsitracion de un articulo
	 */
	private HashMap detalleArticuloAdmin = new HashMap();
	
	/**
	 * 
	 */
	private HashMap detalleArticuloAdminE = new HashMap();
	
	/**
	 * Almacena la informacion del consumo de insumos
	 */
	private HashMap insumos = new HashMap();
	
	/**
	 * Indica si el detalle se va a ver con la cuenta asociada
	 */
	private boolean cuentaAsociada;
	
	//***** Muestro link de resumen parcial historia clinica *********
	
	private int muestroResumenPHC=0;
	
	private Integer muestroResumenPHCAsocio;
	
	
	/**
	 * 
	 */
	private HashMap resultadoLaboratorios;
	
	/**
	 * 
	 */
	private HashMap valoracionesEnfermeria;
	

	
	//****************************************************************
	
	//**************************************************************************************************
	//********ATRIBUTOS RELACIONADOS CON LA INFORMACION DE LA CUENTA DE URGENCIAS DEL ASOCIO*************
	//****************************************************************************************************
	private String cuentaAsocio;
	private boolean asocio;
	private boolean existenInterconsultasAsocio;
	private boolean existenProcedimientosAsocio;
	private boolean existenEvolucionesAsocio;
	private boolean existeAdminMedicamentosAsocio;
	private boolean existeConsumoInsumosAsocio;
	private boolean existeCargosDirectos;
	private int egresoAsocio;
	private HashMap consultasPYPAsocio = new HashMap();
	private int numConsultasPYPAsocio;
	private boolean existeConsultasPYPAsocio;
	private Collection datosAdmisionAsocio;
	private boolean existeValoracionInicialAsocio;
	
	//atributos de respuesta de cirugias
	private boolean existenCirugiasAsocio;
	private HashMap mapaCodigosPeticionCirugiaAsocio = new HashMap();
	private HashMap mapaEncabezadosHojaAnestesiaAsocio = new HashMap();
	private HashMap mapaExamenesLaboratorioPreanestesiaAsocio = new HashMap();
	private HashMap mapaHistoExamenesFisicosTextAsocio = new HashMap();
	private HashMap mapaHistoExamenesFisicosTextAreaAsocio = new HashMap();
	private HashMap mapaHistoConclusionesAsocio = new HashMap();
	private HashMap mapaHistoBalanceLiquidosHojaAnestesiaAsocio = new HashMap();
	private HashMap mapaHistoMedicamentosHojaAnestesiaAsocio = new HashMap();
	private Collection listadoTecAnestesiaOpcionesGralAsocio = new ArrayList();
	private Collection listadoTecAnestesiaGralAsocio = new ArrayList();
	private Collection listadoTecAnestesiaRegionalAsocio = new ArrayList();
	private HashMap mapaHistoTecAnestesiaAsocio = new HashMap();
	private Collection listadoSignosVitalesAsocio = new ArrayList();
	private HashMap mapaHistoSignosVitalesAsocio = new HashMap();
	private HashMap mapaHojaQuirurAsocio = new HashMap();
	
	private int tipoValoracionInicialAsocio;
	private HashMap adminMedicamentosAsocio = new HashMap();
	private HashMap insumosAsocio = new HashMap();
	private HashMap respuestaInterpretacionProcedimientosAsocio = new HashMap();
	private HashMap solicitudesInterConsultaAsocio = new HashMap();
	private HashMap codigosEvolucionesAsocio = new HashMap();
	
	private boolean existenOrdenesMedicasAsocio;
	private HashMap ordenesMedicasAsocio = new HashMap();
	private HashMap ordenesCirugiaAsocio = new HashMap();
	private HashMap ordenesMedicamentosAsocio = new HashMap();
	private HashMap ordenesProcedimientosAsocio = new HashMap();
	private HashMap ordenesInterconsultasAsocio = new HashMap();
	
	//********	REGISTRO ENFERMERIA ASOCIO*********************************
	//atributos para notas de enfermeria
	private boolean existeNotasEnfermeriaAsocio;
	private Collection historicoAnotacionesEnfermeriaAsocio;
	//atributos para los signos vitales
	private boolean existeSignosVitalesAsocio;
	private Collection signosVitalesInstitucionCcostoAsocio;
	private Collection signosVitalesFijosHistoAsocio;
	private Collection signosVitalesParamHistoAsocio;
	private Collection signosVitalesHistoTodosAsocio;
	//atributos para cateteres y sondas
	private boolean existeCateterSondaAsocio;
	private Collection colCateteresSondaInstitucionCcostoAsocio;
	private Collection cateterSondaFijosHistoAsocio;
	private Collection cateterSondaParamHistoAsocio;
	private Collection cateterSondaHistoTodosAsocio;
	private HashMap mapaHistoricoCateterSondaAsocio = new HashMap();
	//atributos para el control de liquidos
	private boolean existeControlLiquidosAsocio;
	private HashMap mapaControlLiqAsocio = new HashMap();
	//atributos para el soporte respiratorio
	private boolean existeSoporteRespiratorioAsocio;
	private HashMap soporteRespiratorioAsocio = new HashMap();
	//atributos para los cuidados especiales
	private boolean existeCuidadosEspecialesAsocio;
	private HashMap mapaHistoricoCuidadosEspecialesAsocio = new HashMap();
	//atributos para la hoja neurólogica
	private boolean existeHojaNeurologicaAsocio;
	private Collection escalasGlasgowInstitucionCCostoAsocio = new ArrayList();
	private HashMap mapaHistoricoEscalaGlasgowAsocio = new HashMap();
	private HashMap mapaHistoricoPupilasAsocio = new HashMap();
	private HashMap mapaHistoricoConvulsionesAsocio = new HashMap();
	private HashMap mapaHistoricoControlEsfinteresAsocio = new HashMap();
	private HashMap mapaHistoricoFuerzaMuscularAsocio = new HashMap();
	
	//*****************************************************************************************************
	//*****************************************************************************************************
	
	/**
	 * HashMap de listado de cuentas 
	 * */
	private HashMap mapaValidacionesInfoCuentas = new HashMap();
	
	/**
	 * indicador de la posicion del mapa MapaValidacionesInfoCuentas
	 * */
	int indiceMapaValInfoCuentas ;
	//*****************************************************************************************************
	//*****************************************************************************************************
	
	//Atributos para Respuesta de Procedimientos parametrizada*********************************************
	/**
	 * DtoPlantilla
	 * */
	private ArrayList<DtoPlantilla> plantillaDtoArray;
	//***************************************************************************************
	
	//Definicion de Dto
	
	/**
	 * Almacena informacion de la preanestesia
	 * */
	private DtoPreanestesia preanestesiaDto;
	
	
	//******************************************************************************************************
	//******************************************************************************************************
	//********ATRIBUTOS PARA MOSTRAR INFORMACIÓN EN EL RESUMEN DEL INGRESO ********************************
	//****************************************************************************************************
	/**
	 * Indicador para filtrar la informacion de historia clinica en los asocios, los valores pueden ser:
	 * U : Urgencias
	 * H : Hospitalizacion
	 * A : Ambos
	 */
	private String filtroAsocio;
	/**
	 * Fecha Inicial
	 */
	private String fechaInicial;
	/**
	 * Fecha Final
	 */
	private String fechaFinal;
	/**
	 * Hora Inicial
	 */
	private String horaInicial;
	/**
	 * Hora Final
	 */
	private String horaFinal;
	private String fechaIngreso;
	private String fechaEgreso;
	//******************************************************************************************************
	//******************************************************************************************************
	
	/**
	 * Objeto que maneja los chequeos en la impresion
	 */
	private HashMap checkImpresion = new HashMap();
	
	
	//********ATRIBUTOS USADOS PARA MOSTRAR LA INFORMACION DE REFERENCIA********************************
	/**
	 * Variable que indica si existe referencia Interna
	 */
	private boolean existeReferenciaInterna;
	/**
	 * Variable que indica si existe referencia externa
	 */
	private boolean existeReferenciaExterna;
	/**
	 * Número de la referencia interna
	 */
	private String numeroReferenciaInterna;
	/**
	 * Número de la referencia externa
	 */
	private String numeroReferenciaExterna;
	/**
	 * Variable que me dice si debo especificar en el label de la JSP
	 * el tipo de referencia
	 */
	private boolean especificarTipoReferencia;
	/**
	 * Variable que me indica si existe contrareferencia
	 */
	private boolean existeContrarreferencia;
	//**********************************************************************************************	
	//ATRIBUTOS PARA CONSENTIMIENTO INFORMADO *****************************
	/**
	 * HashMap historial registro de Impresiones Consentimiento Informado	 
	 */	
	private HashMap historialConsentimientoInfMap;
	
	/**
	 * Indica si existe consentimientos informados para el ingreso
	 * */
	private String existeHistorialConsentimiento;
	//**********************************************************************************************
	
	//**********************************************************************************************	
	//ATRIBUTOS PARA CARGOS DIRECTOS****************************************************************
	/**
	 * HashMap información de los Cargos Directos de Articulo
	 * */	
	private HashMap solCDirectosArticulosMap;
	
	/**
	 * HashMap información de los Cargos Directo de Consulta 
	 * */
	private HashMap solCDirectosConsultasMap;
	
	//ATRIBUTOS PARA PARA LA IMPRESION DE LAS SOLICITUDES TAREA No. 39076 Xplanner3 *****************************
	/**
	 * Numero de la solicitud 
	 */	
	private String solicitud=ConstantesBD.codigoNuncaValido+"";
	
	/**
	 * indica de donde es llamada la impresion
	 * puende tener los siguientes valores:
	 * -- procedimientos
	 * -- medicamentos
	 * -- interconsulta
	 * */
	private String tipoSolicitud="";
	
	private String codigoArticulo=ConstantesBD.codigoNuncaValido+"";
	//**********************************************************************************************
	
	/**
	 * HashMap información de los Cargos Directos de Procedimientos 
	 * */
	private HashMap solCDirectosProcedimientosMap;
	
	/**
	 * HashMap información de los Cargos Directos de Servicios
	 * */
	private HashMap solCDirectosServiciosMap;
	
	/**
	 * HashMap informacion de los Cargos Directos de Cirugia - DyT
	 * */
	private HashMap solCDirectosCirugiaDyTMap; 
	
	//**********************************************************************************************
	
	
	//**********************************************************************************************	
	//ATRIBUTOS PARA HOJA DE ANESTESIA**************************************************************
	
	/**
	 * HashMap mapaHojaAnestesia
	 * */
	private HashMap mapaHojaAnestesia;
	
	//**********************************************************************************************
	
	
	//****************************************************
	//---- Perfil de Farmacoterapia
	/**
	 * Mapa con la información del perfil de Farmacoterapia
	 */
	private HashMap perfilFarmacoterapiaMap;
	/**
	 * Mapa con la información del perfil de Farmacoterapia para un mes
	 */
	private HashMap perfilFarmacoterapiaMesMap;
	/**
	 * Mapa con la información de las filas de perfil de Farmacoterapia para un mes
	 */
	private HashMap filasPerfilFarmacoterapiaMesMap;
	/**
	 * Mapa con la información de las columnas de perfil de Farmacoterapia para un mes
	 */
	private HashMap columnasPerfilFarmacoterapiaMesMap;
	/**
	 * Patron para el ordenamiento
	 */
	private String patronOrdenar;
	/**
	 * Patron para el ordenamiento
	 */
	private String ultimoPatron;
	//****************************************************
	
	private String esImpresionAutomatica;
	
//	******************************************************
	/**
	 * mapa en el cual se almacenan las solicitudes
	 */
	private HashMap solicitudesCita;
	
//************************************************************************	
	
	private ArrayList<DtoAntecendenteOdontologico> arrayAntecedentesActuales;
    private DtoAntecendenteOdontologico ultimoAntecedente;
	private String datosGeneralesUltimoAnt;
	private DtoComponente componenteAntOdo;

	private List<DtoIngresoHistoriaClinica> listaDtoHc;
	
	private Boolean tienePermisoImprimirDetalleItemHC;
	
	private Boolean tienePermisoImprimirHC;
	
	private boolean estadoBotonImprimirHC=true;
	private boolean imprimiendoHC=false;
	private int contarCantidadSubmitImpresion=0;
	
	private Boolean tienePermisoConsultarHistoriaDeAtenciones;
	
	private boolean haSeleccionadoIngreso=false;
	private int cantidadIngresosSeleccionados=0;
	
	private Integer rowSpanSeccionUnoReporteHC;
	
	private Integer rowSpanSeccionDosReporteHC;
	
	private Integer rowSpanSeccionTresReporteHC;
	/**
	* Tipo Modificacion: Segun incidencia 5055
	* Autor: Alejandro Aguirre Luna
	* usuario: aleagulu
	* Fecha: 30/01/2013
	* Descripcion:  
	**/
	private List<Integer> rowSpanSeccionSeisReporteHC_lista = new ArrayList<Integer>();
	private List<Integer> rowSpanSeccionSieteReporteHC_lista = new ArrayList<Integer>();
	private List<Integer> rowSpanSeccionOchoReporteHC_lista = new ArrayList<Integer>();
	private List<Integer> rowSpanSeccionNueveReporteHC_lista = new ArrayList<Integer>();
	private List<Integer> rowSpanSeccionDiezReporteHC_lista = new ArrayList<Integer>();
	
	private Integer contadorSeisUrgencias = 0;
	private Integer contadorSeisHospitalizacion = 0;
	
	private Integer contadorSieteUrgencias = 0;
	private Integer contadorSieteHospitalizacion = 0;
	
	private Integer contadorOchoUrgencias = 0;
	private Integer contadorOchoHospitalizacion = 0;
	
	private Integer contadorNueveUrgencias = 0;
	private Integer contadorNueveHospitalizacion = 0;
	
	private Integer contadorDiezUrgencias = 0;
	private Integer contadorDiezHospitalizacion = 0;
	

	
	/*
	* Antes MT6313 
	* Tipo Modificacion: Segun incidencia MT6713
	* Autor: Jesús Darío Ríos
	* usuario: jesrioro
	* Fecha: 20/03/2013
	* Descripcion: Atributos y metodos para el flujo de consultar citas          
	*/
	private boolean vieneDeConsultaExterna=false;
	
	/**
	 * @return the vieneDeConsultaExterna
	 */
	public boolean getVieneDeConsultaExterna() {
		return vieneDeConsultaExterna;
	}

	/**
	 * @param vieneDeConsultaExterna the vieneDeConsultaExterna to set
	 */
	public void setVieneDeConsultaExterna(boolean vieneDeConsultaExterna) {
		this.vieneDeConsultaExterna = vieneDeConsultaExterna;
	}
	//Fin MT6713
	
	public void resetContadoresSecciones(){
		contadorSeisUrgencias = 0;
		contadorSeisHospitalizacion = 0;
		contadorSieteUrgencias = 0;
		contadorSieteHospitalizacion = 0;
		contadorOchoUrgencias = 0;
		contadorOchoHospitalizacion = 0;
		contadorNueveUrgencias = 0;
		contadorNueveHospitalizacion = 0;
		contadorDiezUrgencias = 0;
		contadorDiezHospitalizacion = 0;
	}
	
	public Integer getContadorSieteUrgencias() {
		return contadorSieteUrgencias;
	}
	public void setContadorSieteUrgencias(Integer contadorSieteUrgencias) {
		this.contadorSieteUrgencias = contadorSieteUrgencias;
	}
	public Integer getContadorSieteHospitalizacion() {
		return contadorSieteHospitalizacion;
	}
	public void setContadorSieteHospitalizacion(Integer contadorSieteHospitalizacion) {
		this.contadorSieteHospitalizacion = contadorSieteHospitalizacion;
	}
	public Integer getContadorOchoUrgencias() {
		return contadorOchoUrgencias;
	}
	public void setContadorOchoUrgencias(Integer contadorOchoUrgencias) {
		this.contadorOchoUrgencias = contadorOchoUrgencias;
	}
	public Integer getContadorOchoHospitalizacion() {
		return contadorOchoHospitalizacion;
	}
	public void setContadorOchoHospitalizacion(Integer contadorOchoHospitalizacion) {
		this.contadorOchoHospitalizacion = contadorOchoHospitalizacion;
	}
	public Integer getContadorNueveUrgencias() {
		return contadorNueveUrgencias;
	}
	public void setContadorNueveUrgencias(Integer contadorNueveUrgencias) {
		this.contadorNueveUrgencias = contadorNueveUrgencias;
	}
	public Integer getContadorNueveHospitalizacion() {
		return contadorNueveHospitalizacion;
	}
	public void setContadorNueveHospitalizacion(Integer contadorNueveHospitalizacion) {
		this.contadorNueveHospitalizacion = contadorNueveHospitalizacion;
	}
	public Integer getContadorDiezUrgencias() {
		return contadorDiezUrgencias;
	}
	public void setContadorDiezUrgencias(Integer contadorDiezUrgencias) {
		this.contadorDiezUrgencias = contadorDiezUrgencias;
	}
	public Integer getContadorDiezHospitalizacion() {
		return contadorDiezHospitalizacion;
	}
	public void setContadorDiezHospitalizacion(Integer contadorDiezHospitalizacion) {
		this.contadorDiezHospitalizacion = contadorDiezHospitalizacion;
	}
	public Integer getContadorSeisUrgencias() {
		return contadorSeisUrgencias;
	}
	public void setContadorSeisUrgencias(Integer contadorSeisUrgencias) {
		this.contadorSeisUrgencias = contadorSeisUrgencias;
	}
	public Integer getContadorSeisHospitalizacion() {
		return contadorSeisHospitalizacion;
	}
	public void setContadorSeisHospitalizacion(Integer contadorSeisHospitalizacion) {
		this.contadorSeisHospitalizacion = contadorSeisHospitalizacion;
	}
	public List<Integer> getRowSpanSeccionSeisReporteHC_lista() {
		return rowSpanSeccionSeisReporteHC_lista;
	}
	public void setRowSpanSeccionSeisReporteHC_lista(
			List<Integer> rowSpanSeccionSeisReporteHC_lista) {
		this.rowSpanSeccionSeisReporteHC_lista = rowSpanSeccionSeisReporteHC_lista;
	}
	public List<Integer> getRowSpanSeccionSieteReporteHC_lista() {
		return rowSpanSeccionSieteReporteHC_lista;
	}
	public void setRowSpanSeccionSieteReporteHC_lista(
			List<Integer> rowSpanSeccionSieteReporteHC_lista) {
		this.rowSpanSeccionSieteReporteHC_lista = rowSpanSeccionSieteReporteHC_lista;
	}
	public List<Integer> getRowSpanSeccionOchoReporteHC_lista() {
		return rowSpanSeccionOchoReporteHC_lista;
	}
	public void setRowSpanSeccionOchoReporteHC_lista(
			List<Integer> rowSpanSeccionOchoReporteHC_lista) {
		this.rowSpanSeccionOchoReporteHC_lista = rowSpanSeccionOchoReporteHC_lista;
	}
	public List<Integer> getRowSpanSeccionNueveReporteHC_lista() {
		return rowSpanSeccionNueveReporteHC_lista;
	}
	public void setRowSpanSeccionNueveReporteHC_lista(
			List<Integer> rowSpanSeccionNueveReporteHC_lista) {
		this.rowSpanSeccionNueveReporteHC_lista = rowSpanSeccionNueveReporteHC_lista;
	}
	public List<Integer> getRowSpanSeccionDiezReporteHC_lista() {
		return rowSpanSeccionDiezReporteHC_lista;
	}
	public void setRowSpanSeccionDiezReporteHC_lista(
			List<Integer> rowSpanSeccionDiezReporteHC_lista) {
		this.rowSpanSeccionDiezReporteHC_lista = rowSpanSeccionDiezReporteHC_lista;
	}
	private String flag = "primera";
	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	private boolean infoEnUrgencias = false;
	private boolean infoEnHospitalizacion = false;

	public boolean isInfoEnUrgencias() {
		return infoEnUrgencias;
	}

	public void setInfoEnUrgencias(boolean infoEnUrgencias) {
		this.infoEnUrgencias = infoEnUrgencias;
	}

	public boolean isInfoEnHospitalizacion() {
		return infoEnHospitalizacion;
	}

	public void setInfoEnHospitalizacion(boolean infoEnHospitalizacion) {
		this.infoEnHospitalizacion = infoEnHospitalizacion;
	}

	public ResumenAtencionesForm(){
		rowSpanSeccionSeisReporteHC_lista = new ArrayList<Integer>(10);
		rowSpanSeccionSieteReporteHC_lista = new ArrayList<Integer>(10);
		rowSpanSeccionOchoReporteHC_lista = new ArrayList<Integer>(10);
		rowSpanSeccionNueveReporteHC_lista = new ArrayList<Integer>(10);
		rowSpanSeccionDiezReporteHC_lista = new ArrayList<Integer>(10);
		
		for(int i = 0; i < 50; i++){
			rowSpanSeccionSeisReporteHC_lista.add(i,0);
			rowSpanSeccionSieteReporteHC_lista.add(i,0);
			rowSpanSeccionOchoReporteHC_lista.add(i,0);
			rowSpanSeccionNueveReporteHC_lista.add(i,0);
			rowSpanSeccionDiezReporteHC_lista.add(i,0);
		}
		setFlag("ultima");
	}
	public void contadorFilasSeis(Integer posicion){
		if(posicion == 1){
			contadorSeisHospitalizacion++;
			infoEnHospitalizacion = true;
		}
		if(posicion == 2 || posicion == 3){
			contadorSeisUrgencias++;
			infoEnUrgencias = true;
		}
	}
	
	public void contadorFilasSiete(Integer posicion){
		if(posicion == 1){
			contadorSieteHospitalizacion++;
			infoEnHospitalizacion = true;
		}
		if(posicion == 2 || posicion == 3){
			contadorSieteUrgencias++;
			infoEnUrgencias = true;
		}
	}
	
	public void contadorFilasDiez(Integer posicion){
		if(posicion == 1){
			contadorDiezHospitalizacion++;
			infoEnHospitalizacion = true;
		}
		if(posicion == 2 || posicion == 3){
			contadorDiezUrgencias++;
			infoEnUrgencias = true;
		}
	}
	
	public void contadorFilasOcho(Integer posicion){
		if(posicion == 1){
			contadorOchoHospitalizacion++;
			infoEnHospitalizacion = true;
		}
		if(posicion == 2 || posicion == 3){
			contadorOchoUrgencias++;
			infoEnUrgencias = true;
		}
	}
	
	public void contadorFilasNueve(Integer posicion){
		if(posicion == 1){
			contadorNueveHospitalizacion++;
			infoEnHospitalizacion = true;
		}
		if(posicion == 2 || posicion == 3){
			contadorNueveUrgencias++;
			infoEnUrgencias = true;
		}
	}
	/**
	* Tipo Modificacion: Segun incidencia 6480
	* Autor: Alejandro Aguirre Luna
	* usuario: aleagulu
	* Fecha: 19/02/2013
	* Descripcion: Nuevas funciones que permiten hacer la sumatoria a la sección
	* correspondiente dependiendo de si la vía de ingreso es 1 (Urgencias) ó 3
	* (Hospitalización). Esto para el caso en que el paciente halla tenido a lo
	* sumo una evolución. 
	**/
	public void contadorFilasSeis_nuevo(int posicion){
		if(posicion == 1){
			contadorSeisUrgencias++;
			infoEnUrgencias = true;
		}
		if(posicion == 3){
			contadorSeisHospitalizacion++;
			infoEnHospitalizacion = true;
		}
	}
	
	public void contadorFilasSiete_nuevo(int posicion){
		if(posicion == 1){
			contadorSieteUrgencias++;
			infoEnUrgencias = true;
		}
		if(posicion == 3){
			contadorSieteHospitalizacion++;
			infoEnHospitalizacion = true;
		}
	}
	
	public void contadorFilasDiez_nuevo(int posicion){
		if(posicion == 1){
			contadorDiezUrgencias++;
			infoEnUrgencias = true;
		}
		if(posicion == 3){
			contadorDiezHospitalizacion++;
			infoEnHospitalizacion = true;
		}
	}
	
	public void contadorFilasOcho_nuevo(int posicion){
		if(posicion == 1){
			contadorOchoUrgencias++;
			infoEnUrgencias = true;
		}
		if(posicion == 3){
			contadorOchoHospitalizacion++;
			infoEnHospitalizacion = true;
		}
	}
	
	public void contadorFilasNueve_nuevo(int posicion){
		if(posicion == 1){
			contadorNueveUrgencias++;
			infoEnUrgencias = true;
		}
		if(posicion == 3){
			contadorNueveHospitalizacion++;
			infoEnHospitalizacion = true;
		}
	}
	

	private Integer rowSpanSeccionCuatroReporteHC;
	
	private Integer rowSpanSeccionCincoReporteHC;
	
	
	private Integer rowSpanSeccionSeisReporteHC;
	
	private Integer rowSpanSeccionSieteReporteHC;
	
	private Integer rowSpanSeccionOchoReporteHC;
	
	private Integer rowSpanSeccionNueveReporteHC;
	
	private Integer rowSpanSeccionDiezReporteHC;
	
	
	private List<DtoCodigosCirugiaPeticiones> listaDtoSOlcitudes;
	
	//NOTAS ACLARATORIAS
	private DtoNotaAclaratoria notaAclaratoriaDTO;
	private ArrayList<DtoNotaAclaratoria> notasAclaratorias;
	private boolean detalleNotaAclaratoria;
	private boolean guardarNotaAclaratoria;
	private boolean nuevaNotaAclaratoria;
	private String codigoNotaAclaratoria;	
	
	/**
	 * 
	 */
	private Boolean existenCitasCE;

	/**
	 *Valida si el reporte es para suba o versalles 
	 */
	private Integer estadoHCCLiente;
	
	
	/**
	 * 
	 */
	private List<Boolean> listaCheckGlobalHC=new ArrayList<Boolean>();
	
	/**
	 * Estado  que indica si estan todos los check seleccioandos
	 */
	private Boolean estadoCheckTodos;
	
	
	
	private String anoFiltro;
	
	private String fechaIngresoInicialFiltro;
	
	private String fechaIngresoFinalFiltro;
	
	private String centroAtencionFiltro;
	
	private String viaIngresoFiltro;
	
	private String especialidadFiltro;
	
	private ArrayList<CentroAtencion> listaCentrosAtencion;
	
	private List<DtoViasIngresoHC> viasIngresoList;
	
	private List<DtoEspecialidadesHC> especialdiades;
	
	private  List<String>  anosPaciente;
	
	private String viaIngresoSeleccionadaFiltroHc;
	
	private String espeSeleccioandaFiltroHc;
	
	private String anoSeleccionadoFiltroHC;
	
	private String estadoFiltroBusqueda;
	
	private Boolean hayResultados;
	
	private Boolean mostrarFiltroAnos;
	
	private Integer cantidadDiasParametro;
	
	private boolean ordenamiento;
	
	/**
	 * Atributo que almacena si existe busqueda x algun criterio
	 */
	private boolean esBusquedaResumen;
	

	/**
	 * Atributo que almacena la info de las valoraciones cuando existe valoracion inicial y de cuidades especiales
	 */
	private HashMap<String,Object> datosValoracionInicial = new HashMap<String, Object>();
	
	/**
	 * Atributo que almacena la bandera de mostrar la ruta jsp o no
	 */
	private boolean mostrarRutaJsp;
	
	
	/**
	 * Para registrar informacion relevante del ingreso seleccionado
	 */
	private InfoIngresoDto ingresoSelecccionado;
	
	
	/**

	 * @return the solicitudesCita
	 */
	public HashMap getSolicitudesCita() {
		return solicitudesCita;
	}


	/**
	 * @param solicitudesCita the solicitudesCita to set
	 */
	public void setSolicitudesCita(HashMap solicitudesCita) {
		this.solicitudesCita = solicitudesCita;
	}


	/**
	 * @return Returns the accidenteTransito.
	 */
	public boolean isAccidenteTransito() {
		return accidenteTransito;
	}


	/**
	 * @param accidenteTransito The accidenteTransito to set.
	 */
	public void setAccidenteTransito(boolean accidenteTransito) {
		this.accidenteTransito = accidenteTransito;
	}


	/**
	 * Método para validar el form
	 * Como esta funcionalidad no tiene campos para ingresar,
	 * no necesita validaciones, se deja por si en un futuro es necesario
	 */	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errores = new ActionErrors();
		if(estado.equals("buscarIngreso")||estado.equals("impresionHC"))
		{
			
			
			//*****Se validan atributos requeridos*************************
			if(!this.fechaInicial.equals("")&&this.fechaFinal.equals(""))
				errores.add("La fecha final es requerida", new ActionMessage("errors.required","La fecha final H.C"));
			if(this.fechaInicial.equals("")&&!this.fechaFinal.equals(""))
				errores.add("La fecha inicial es requerida", new ActionMessage("errors.required","La fecha inicial H.C"));
			if(!this.horaInicial.equals("")&&this.horaFinal.equals(""))
				errores.add("La hora final es requerida", new ActionMessage("errors.required","La hora final H.C"));
			if(this.horaInicial.equals("")&&!this.horaFinal.equals(""))
				errores.add("La hora inicial es requerida", new ActionMessage("errors.required","La hora inicial H.C"));
			if(!this.horaInicial.equals("")&&!this.horaFinal.equals("")&&this.fechaInicial.equals("")&&this.fechaFinal.equals(""))
				errores.add("La fecha inicial y final es requerida con horas", new ActionMessage("errors.minimoCampos","La fecha inicial y final H.C","la busqueda con hora inicial y final H.C"));
			//************************************************************
			
			//***Se valida formato de los atributos***********************
			if(!UtilidadFecha.validarFecha(this.fechaInicial)&&!this.fechaInicial.equals(""))
				errores.add("Fecha Inicial inválida",new ActionMessage("errors.formatoFechaInvalido","inicial H.C"));
			if(!UtilidadFecha.validarFecha(this.fechaFinal)&&!this.fechaFinal.equals(""))
				errores.add("Fecha Final inválida",new ActionMessage("errors.formatoFechaInvalido","final H.C"));
			if(!UtilidadFecha.validacionHora(this.horaInicial).puedoSeguir&&!this.horaInicial.equals(""))
				errores.add("Hora Inicial inválida",new ActionMessage("errors.formatoHoraInvalido","inicial H.C"));
			if(!UtilidadFecha.validacionHora(this.horaFinal).puedoSeguir&&!this.horaFinal.equals(""))
				errores.add("Hora Final inválida",new ActionMessage("errors.formatoHoraInvalido","final H.C"));
			//***********************************************************
			
			if(errores.isEmpty()&&!this.fechaInicial.equals("")&&!this.fechaFinal.equals(""))
			{
				//******validaciones de los rangos******************************************
				if(UtilidadFecha.conversionFormatoFechaABD(this.fechaInicial).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.fechaFinal))>0)
					errores.add("la fecha inicial es mayor a la fecha final",new ActionMessage("errors.fechaPosteriorIgualActual","inicial H.C","final H.C"));
				if(!this.horaInicial.equals("")&&!this.horaFinal.equals(""))
					if(UtilidadFecha.conversionFormatoFechaABD(this.fechaInicial).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.fechaFinal))==0&&
						!UtilidadFecha.compararFechas(this.fechaFinal,this.horaFinal,this.fechaInicial,this.horaInicial).isTrue())
						errores.add("la fecha hora final es menor a fecha hora inicial",new ActionMessage("errors.fechaHoraPosteriorIgualActual","inicial H.C","final H.C"));
				
				if(UtilidadFecha.conversionFormatoFechaABD(this.fechaInicial).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.fechaIngreso))<0)
					errores.add("la fecha inicial es menor a la fecha de ingreso", new ActionMessage("errors.fechaAnteriorIgualActual","inicial H.C","de ingreso"));
				
				if(!this.fechaEgreso.equals(""))
				{
					if(UtilidadFecha.conversionFormatoFechaABD(this.fechaFinal).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.fechaEgreso))>0)
						errores.add("la fecha final es mayor a la fecha de egreso", new ActionMessage("errors.fechaPosteriorIgualActual","final H.C","de egreso"));
					
				}
				//**************************************************************************
				
			}
			
			
			
			if(!errores.isEmpty())
				this.estado = "resumenIngreso";
		}
		
		
		if(this.estado.equals("buscarNuevosIngresos")){








			if(  (this.anoSeleccionadoFiltroHC.equals("") || this.anoSeleccionadoFiltroHC.equals("-1")) &&
					( this.fechaIngresoInicialFiltro.equals("")  || this.fechaIngresoInicialFiltro.equals("-1")) &&
					(  this.fechaIngresoFinalFiltro.equals("")  || this.fechaIngresoFinalFiltro.equals("-1")) &&
					(this.centroAtencionFiltro.equals("") || this.centroAtencionFiltro.equals("-1")) &&
					(this.viaIngresoSeleccionadaFiltroHc.equals("")||this.viaIngresoSeleccionadaFiltroHc.equals("-1"))&&
					(this.espeSeleccioandaFiltroHc.equals("")||this.espeSeleccioandaFiltroHc.equals("-1"))){
				errores.add("Debe seleccionar al menos un filtro de busqueda", new ActionMessage("errors.seleccionAlgunFiltro"));

			}

			
			
			
			if(!this.fechaIngresoInicialFiltro.equals("") && this.fechaIngresoFinalFiltro.equals("")){
				
				errores.add("errors.fechaInicialSiFechaFinal", new ActionMessage("errors.fechaInicialSiFechaFinal"));
			}
			

			if(this.fechaIngresoInicialFiltro.equals("") && !this.fechaIngresoFinalFiltro.equals("")){

				errores.add("errors.fechaFinalSinFechaInicial", new ActionMessage("errors.fechaFinalSinFechaInicial"));
			}

			
			
			
			if(!this.fechaIngresoInicialFiltro.equals("")){
				String fechaInicial = UtilidadFecha.conversionFormatoFechaAAp(
						this.fechaIngresoInicialFiltro);

				String fechaActual = UtilidadFecha.conversionFormatoFechaAAp(
						Calendar.getInstance().getTime());


				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(
						fechaInicial, fechaActual)){

					errores.add("FECHA INICIAL MAYOR QUE FECHA ACTUAL.", new ActionMessage(
							"errors.fechaPosteriorIgualActual"," Inicial "+fechaInicial," Actual "+fechaActual));
				}
			}
			
			
			
			
			
			if(!this.fechaIngresoFinalFiltro.equals("")){
				String fechaInicial = UtilidadFecha.conversionFormatoFechaAAp(
						this.fechaIngresoFinalFiltro);

				String fechaActual = UtilidadFecha.conversionFormatoFechaAAp(
						Calendar.getInstance().getTime());


				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(
						fechaInicial, fechaActual)){

					errores.add("FECHA INICIAL MAYOR QUE FECHA ACTUAL.", new ActionMessage(
							"errors.fechaPosteriorIgualActual"," Inicial "+fechaInicial," Actual "+fechaActual));
				}
			}
			
			
			
			if(!this.fechaIngresoFinalFiltro.equals("") &&
					!this.fechaIngresoInicialFiltro.equals("")){
				
				String fechaInicial = UtilidadFecha.conversionFormatoFechaAAp(
						this.fechaIngresoInicialFiltro);

				String fechaActual = UtilidadFecha.conversionFormatoFechaAAp(
						this.fechaIngresoFinalFiltro);


				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(
						fechaInicial, fechaActual)){

					errores.add("FECHA INICIAL MAYOR QUE FECHA ACTUAL.", new ActionMessage(
							"errors.fechaPosteriorIgualActual"," Inicial "+fechaInicial," Final "+fechaActual));
				}
				
				String[] arregloFecha = (this.fechaIngresoInicialFiltro).split("/");
				String[] arregloFechaFinal = (this.fechaIngresoFinalFiltro).split("/");
				
			    int dia= Integer.parseInt(arregloFecha[0]);
			    int mes= Integer.parseInt(arregloFecha[1]);
			    int anio= Integer.parseInt(arregloFecha[2]);
			    
			    int diaB= Integer.parseInt(arregloFechaFinal[0]);
			    int mesB= Integer.parseInt(arregloFechaFinal[1]);
			    int anioB= Integer.parseInt(arregloFechaFinal[2]);
			    
			    int numeroDias=UtilidadFecha.numeroDiasEntreFechas(diaB,mesB,anioB,dia,mes,anio);
			   
				if(this.cantidadDiasParametro.equals(0)){
					if(numeroDias > 180){
						errores.add("Dias incorrectos .", new ActionMessage(
								"errors.diasFecha180",""));
					}
				}else{
					if(numeroDias > 365){
						errores.add("Dias incorrectos .", new ActionMessage(
								"errors.diasFecha365",""));
					}
				}
				
			
				
			}
			
			
			
			
			
			
		}
		return errores;
	}
	
	
	/**
	 * @return Returns the nombreArchivo.
	 */
	public String getNombreArchivo()
	{
		return nombreArchivo;
	}
	/**
	 * @param nombreArchivo The nombreArchivo to set.
	 */
	public void setNombreArchivo(String nombreArchivo)
	{
		this.nombreArchivo=nombreArchivo;
	}
	/**
	 * @return Returns the nombreOriginal.
	 */
	public String getNombreOriginal()
	{
		return nombreOriginal;
	}
	/**
	 * @param nombreOriginal The nombreOriginal to set.
	 */
	public void setNombreOriginal(String nombreOriginal)
	{
		this.nombreOriginal=nombreOriginal;
	}
	
	/**
	 * @return Returns the mapa.
	 */
	public HashMap getDocumentosAdjuntosGenerados()
	{
		return documentosAdjuntosGenerados;
	}
	
	/**
	 * @return Returna la propiedad del mapa mapa.
	 */
	public Object getDocumentosAdjuntosGenerados(String key)
	{
		return documentosAdjuntosGenerados.get(key);
	}
	/**
	 * @param Asigna la propiedad al mapa
	 */
	public void setDocumentosAdjuntosGenerados(String key, Object value)
	{
		this.documentosAdjuntosGenerados.put(key, value);
	}
	
	/**
	 * @param mapa The mapa to set.
	 */
	public void setDocumentosAdjuntosGenerados(HashMap documentosAdjuntosGenerados)
	{
		this.documentosAdjuntosGenerados = documentosAdjuntosGenerados;
	}
	
	
	/**
	 * @return Returns the numDocumentosAdjuntos.
	 */
	public int getNumDocumentosAdjuntos()
	{
		return numDocumentosAdjuntos;
	}
	/**
	 * @param numDocumentosAdjuntos The numDocumentosAdjuntos to set.
	 */
	public void setNumDocumentosAdjuntos(int numDocumentosAdjuntos)
	{
		this.numDocumentosAdjuntos=numDocumentosAdjuntos;
	}
	/**
	 * @return el estado
	 */
	public String getEstado()
	{
		return estado;
	}

	
	
	/**
	 * @return Returns the existeHojaObstetrica.
	 */
	public String getExisteHojaObstetrica()
	{
		return existeHojaObstetrica;
	}


	/**
	 * @param existeHojaObstetrica The existeHojaObstetrica to set.
	 */
	public void setExisteHojaObstetrica(String existeHojaObstetrica)
	{
		this.existeHojaObstetrica=existeHojaObstetrica;
	}


	/**
	 * Asignar el estado
	 * @param string
	 */
	public void setEstado(String string)
	{
		estado = string;
	}

	
	/**
	 * Método para restaurar los valores por defecto de la clase
	 */
	public void reset()
	{
		
		this.resultadoLaboratorios=new HashMap();
		this.valoracionesEnfermeria=new HashMap();
		this.resultadoLaboratorios.put("numRegistros", "0");
		this.valoracionesEnfermeria.put("numRegistros", "0");
		

		this.numeroAdministraciones=0;
		tipoValoracionInicial=0;
		viaIngreso=0;
		cuenta=0;
		idIngreso = "";
		this.posArticulo = 0;
		datosAdmision=new ArrayList();
		cuentas=new ArrayList();
		antecedentes=new ArrayList();
		coleccionSolicitudes=new ArrayList();
		mostrarMensajeAsocio=false;
		//ANTECEDENTES---------------------------------------------------
		existenAntecedentesAlergias=false;
		mapaAntAlergia = new HashMap();
		
		existenAntecedentesFamiliares=false;
		mapaAntFamiliares = new HashMap();
		
		existenAntecedentesOftalFamiliares=false;
		mapaAntFamOftal = new HashMap();
		
		existenAntecedentesOftalPersonales=false;
		mapaAntPersoOftal = new HashMap();
		
		existenAntecedentesGinecoObstetricos=false;
		mapaAntGineco = new HashMap();
		
		existenAntecedentesMedicamentos=false;
		mapaAntMedicamento = new HashMap();
		
		existenAntecedentesMedicos=false;
		mapaAntMedicos = new HashMap();
		
		
		existenAntecedentesPediatricos=false;
		mapaAntPediatricos = new HashMap();
		antPed = new AntecedentePediatrico();
		tiposParto = new HashMap();
		motivosTiposParto = new HashMap();
		tiposPartoList = new ArrayList();
		tiposPartoCarga = new HashMap();
		motivosTiposPartoCarga = new HashMap();
		categoriaEmbarazoOpcionCampo = new ArrayList();
		
		existenAntecedentesToxicos=false;
		mapaAntToxicos = new HashMap();
		
		existenAntecedentesTransfusionales=false;
		mapaAntTransfusionales = new HashMap();
		
		existenAntecedentesVacunas = false;
		mapaVacunas = new HashMap();
		mapaTiposInmunizacion = new HashMap();
		
		existenAntecedentesVarios=false;
		mapaAntOtros = new HashMap();
		
		this.existenAntecedentesOdontologicos=false;
		mapaAntOdonto = new HashMap();
		
		this.esPopupAntecedentes=false;
		//----------------------------------------------------------
		existeValoracionInicial=false;
		existenInterconsultas=false;
		solicitudesInterConsulta = new HashMap();
		existenProcedimientos=false;
		existenCitas = false;
		respuestaInterpretacionProcedimientos = new HashMap();
		
		existenCirugias=false;
		mapaCodigosPeticionCirugia = new HashMap();
		mapaEncabezadosHojaAnestesia = new HashMap();
		mapaExamenesLaboratorioPreanestesia = new HashMap();
		mapaHistoExamenesFisicosText = new HashMap();
		mapaHistoExamenesFisicosTextArea = new HashMap();
		mapaHistoConclusiones = new HashMap();
		mapaHistoBalanceLiquidosHojaAnestesia = new HashMap();
		mapaHistoMedicamentosHojaAnestesia = new HashMap();
		listadoTecAnestesiaOpcionesGral = new ArrayList();
		listadoTecAnestesiaGral = new ArrayList();
		listadoTecAnestesiaRegional = new ArrayList();
		mapaHistoTecAnestesia = new HashMap();
		listadoSignosVitales = new ArrayList();
		mapaHistoSignosVitales = new HashMap();
		mapaHojaQuirur = new HashMap();
		this.numeroSolCx = "";
		this.codigoPeticionCx = "";
		
		existeAdminMedicamentos=false;
		existeConsumoInsumos = false;
		adminMedicamentos = new HashMap();
		detalleArticuloAdmin = new HashMap();
		detalleArticuloAdminE = new HashMap();
		insumos = new HashMap();
		existenEvoluciones=false;
		codigosEvoluciones = new HashMap();
		
		
		
		
		this.existenOrdenesMedicas = false;
		this.ordenesMedicas = new HashMap();
		this.ordenesCirugia = new HashMap();
		this.ordenesMedicamentos = new HashMap();
		this.ordenesProcedimientos = new HashMap();
		this.ordenesInterconsultas = new HashMap();
		
		this.numeroSolicitudes=0;
		egreso=0;
		estado="";
		this.solicitudes=new HashMap();
		ultimaPropiedad="codigoviaingreso";
		columna="";
		this.maxPageItems = 0;
		this.usuario = "";
		this.offset = 0;
		this.linkSiguiente = "";
		this.documentosAdjuntosGenerados=new HashMap();
		this.nombreArchivo="";
		this.nombreOriginal="";
		this.existeHojaObstetrica="";
		
		this.citas = new HashMap();
		this.numCitas = 0;
		this.posCita = 0;
		this.detalleCita = new HashMap();
		this.numDetalleCita = 0;
		
		//REGISTRO DE ENFERMERIA------------------------------
		this.existeNotasEnfermeria = false;
		this.historicoAnotacionesEnfermeria = new ArrayList();
		
		this.existeSignosVitales = false;
		this.signosVitalesInstitucionCcosto = new ArrayList();
		this.signosVitalesFijosHisto = new ArrayList();
		this.signosVitalesParamHisto = new ArrayList();
		this.signosVitalesHistoTodos = new ArrayList();
		
		this.existeCateterSonda = false;
		this.colCateteresSondaInstitucionCcosto = new ArrayList();
		this.cateterSondaFijosHisto = new ArrayList();
		this.cateterSondaParamHisto = new ArrayList();
		this.cateterSondaHistoTodos = new ArrayList();
		this.mapaHistoricoCateterSonda = new HashMap();
		
		this.existeControlLiquidos = false;
		this.mapaControlLiq = new HashMap();
		
		this.existeSoporteRespiratorio = false;
		this.soporteRespiratorio = new HashMap();
		
		this.existeCuidadosEspeciales = false;
		this.mapaHistoricoCuidadosEspeciales = new HashMap();
		
		this.existeHojaNeurologica = false;
		this.escalasGlasgowInstitucionCCosto = new ArrayList();
		this.mapaHistoricoEscalaGlasgow = new HashMap();
		this.mapaHistoricoPupilas = new HashMap();
		this.mapaHistoricoConvulsiones = new HashMap();
		this.mapaHistoricoControlEsfinteres = new HashMap();
		this.mapaHistoricoFuerzaMuscular = new HashMap();
		//--------------------------------------------
		
		this.existeConsultasPYP = false;
		this.consultasPYP = new HashMap();
		this.consultasPYP.put("numRegistros", "0");
		this.numConsultasPYP = 0;
		this.indiceConsultasPYP = "";
		this.ultimoIndiceConsultasPYP = "";
		this.impresionHistoriaClinica = false;
		this.accidenteTransito = false;
		this.eventoCatastrofico=false;
		registroAccidentesTransitoReporte1 = new HashMap();
		registroAccidentesTransitoReporte2 = new HashMap();
		
		this.resetAsocio();
		this.resetMostrarInformacion();
		this.fechaIngreso = "";
		this.fechaEgreso = "";
		this.cuentaAsociada = false;
		this.checkImpresion = new HashMap();
		
		
		this.existeTriage = false;
		this.listadoTriage = new HashMap();
		this.numTriage = 0;
		this.consecutivoTriage = "";
		this.detalleTriage = new HashMap();		
		
		//Atributos de la referencia----------------------------
		this.existeReferenciaExterna = false;
		this.existeReferenciaInterna = false;
		this.existeContrarreferencia = false;
		this.especificarTipoReferencia = false;
		this.numeroReferenciaExterna = "";
		this.numeroReferenciaInterna = "";
		
		//Atributos de Consentimiento Informado----------------------------
		this.historialConsentimientoInfMap = new HashMap();
		
		//Atributos Cargos Directos----------------------------------------
		this.solCDirectosArticulosMap = new HashMap();
		this.solCDirectosCirugiaDyTMap = new HashMap();
		this.solCDirectosConsultasMap = new HashMap();
		this.solCDirectosProcedimientosMap = new HashMap();
		this.solCDirectosServiciosMap = new HashMap();		
		this.existeCargosDirectos = false;		
		
		//Atributos de la impresion de las solicitudes --------------------------
		this.solicitud =ConstantesBD.codigoNuncaValido+"";
		this.tipoSolicitud="";
		this.codigoArticulo=ConstantesBD.codigoNuncaValido+"";
		
		//Atributos de la escala
		this.existenEscalasXIngreso = false;
		this.escalas = new ArrayList<DtoEscala>();
		
		//Atributos para el listado de las valoraciones de cuidado especial
		this.valoracionesCuidadoEspecial = new HashMap<String, Object>();
		
		// Perfil de Farmacoterapia
		this.perfilFarmacoterapiaMap = new HashMap<String, Object>();
		this.perfilFarmacoterapiaMap.put("numRegistros", "0");
		this.perfilFarmacoterapiaMesMap = new HashMap<String, Object>();
		this.perfilFarmacoterapiaMesMap.put("numRegistros", "0");
		this.filasPerfilFarmacoterapiaMesMap = new HashMap<String, Object>();
		this.filasPerfilFarmacoterapiaMesMap.put("numRegistros", "0");
		this.columnasPerfilFarmacoterapiaMesMap = new HashMap<String, Object>();
		this.columnasPerfilFarmacoterapiaMesMap.put("numRegistros", "0");
		this.patronOrdenar="";
		this.ultimoPatron="";
		
		this.esImpresionAutomatica = ConstantesBD.acronimoNo;
		
		this.solicitudesCita = new HashMap();
		
		
		
		this.arrayAntecedentesActuales= new ArrayList<DtoAntecendenteOdontologico>();
		this.ultimoAntecedente= new DtoAntecendenteOdontologico();
		this.datosGeneralesUltimoAnt = new String("");
		this.componenteAntOdo = new DtoComponente();
		this.listaDtoHc= new ArrayList<DtoIngresoHistoriaClinica>();
		this.tienePermisoImprimirDetalleItemHC=false;
		this.tienePermisoImprimirHC=false;
		this.tienePermisoConsultarHistoriaDeAtenciones=false;
		this.rowSpanSeccionUnoReporteHC=new Integer(0);
		this.rowSpanSeccionDosReporteHC=new Integer(0);
		this.rowSpanSeccionTresReporteHC=new Integer(0);
		this.rowSpanSeccionCuatroReporteHC=new Integer(0);
		this.rowSpanSeccionCincoReporteHC=new Integer(0);
		this.rowSpanSeccionSeisReporteHC=new Integer(0);
		this.rowSpanSeccionSieteReporteHC=new Integer(0);
		this.rowSpanSeccionOchoReporteHC=new Integer(0);
		this.rowSpanSeccionNueveReporteHC=new Integer(0);
		this.rowSpanSeccionDiezReporteHC=new Integer(0);
		this.muestroResumenPHCAsocio=new Integer(0);
		this.notaAclaratoriaDTO = new DtoNotaAclaratoria();
		this.notasAclaratorias = new ArrayList<DtoNotaAclaratoria>();
		this.detalleNotaAclaratoria = false;
		this.guardarNotaAclaratoria = false;
		this.nuevaNotaAclaratoria = false;
		this.codigoNotaAclaratoria = null;
		this.listaDtoSOlcitudes=new ArrayList<DtoCodigosCirugiaPeticiones>();
		this.estadoHCCLiente=new Integer(0);
		this.existenCitasCE=new Boolean(false);
		this.anoFiltro="";
		this.fechaIngresoInicialFiltro="";
		this.fechaIngresoFinalFiltro="";
		this.centroAtencionFiltro="";
		this.viaIngresoFiltro="";
		this.especialidadFiltro="";
		this.listaCentrosAtencion= new ArrayList<CentroAtencion>();
		this.viasIngresoList= new ArrayList<DtoViasIngresoHC>();
		this.especialdiades= new ArrayList<DtoEspecialidadesHC>();
		this.anosPaciente= new ArrayList<String>();
		
		this.viaIngresoSeleccionadaFiltroHc="";
		
		this.espeSeleccioandaFiltroHc="";
		
		this.anoSeleccionadoFiltroHC="";
		
		this.estadoFiltroBusqueda="";
		this.hayResultados=false;
		this.mostrarFiltroAnos=false;
		this.cantidadDiasParametro= new Integer(0);
		this.ordenamiento=false;
		
		this.esBusquedaResumen = false;
		
		this.datosValoracionInicial = new HashMap<String, Object>();
		this.mostrarRutaJsp = false;
		
		this.estadoBotonImprimirHC=true;
		this.imprimiendoHC=false;
		this.contarCantidadSubmitImpresion=0;
	}
	
	


	/**
	 * @return the ultimoPatron
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}


	/**
	 * @param ultimoPatron the ultimoPatron to set
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}


	/**
	 * @return the numeroSolCx
	 */
	public String getNumeroSolCx() {
		return numeroSolCx;
	}


	/**
	 * @param numeroSolCx the numeroSolCx to set
	 */
	public void setNumeroSolCx(String numeroSolCx) {
		this.numeroSolCx = numeroSolCx;
	}
	
	/**
	* Tipo Modificacion: Segun incidencia 6480
	* Autor: Alejandro Aguirre Luna
	* usuario: aleagulu
	* Fecha: 18/01/2013
	* Descripcion: 
	**/
	private String numResultados = "";
	public String getNumResultados() {
		return numResultados;
	}
	public void setNumResultados(String numResultados) {
		this.numResultados = numResultados;
	}

	public void resetAsocio()
	{
		this.cuentaAsocio = "";
		this.asocio = false;
		this.existenInterconsultasAsocio = false;
		this.solicitudesInterConsultaAsocio = new HashMap();
		this.existenProcedimientosAsocio = false;
		this.respuestaInterpretacionProcedimientosAsocio = new HashMap();
		this.existenEvolucionesAsocio = false;
		codigosEvolucionesAsocio = new HashMap();
		this.existeAdminMedicamentosAsocio = false;
		this.existeConsumoInsumosAsocio = false;
		
		this.existenOrdenesMedicasAsocio = false;
		this.ordenesMedicamentosAsocio = new HashMap();
		this.ordenesCirugiaAsocio = new HashMap();
		this.ordenesProcedimientosAsocio = new HashMap();
		this.ordenesInterconsultasAsocio = new HashMap();
		
		//REGISTRO DE ENFERMERIA---------------------------------------
		this.existeNotasEnfermeriaAsocio = false;
		this.historicoAnotacionesEnfermeriaAsocio = new ArrayList();
		
		this.existeSignosVitalesAsocio = false;
		this.signosVitalesInstitucionCcostoAsocio = new ArrayList();
		this.signosVitalesFijosHistoAsocio = new ArrayList();
		this.signosVitalesParamHistoAsocio = new ArrayList();
		this.signosVitalesHistoTodosAsocio = new ArrayList();
		
		this.existeCateterSondaAsocio = false;
		this.colCateteresSondaInstitucionCcostoAsocio = new ArrayList();
		this.cateterSondaFijosHistoAsocio = new ArrayList();
		this.cateterSondaParamHistoAsocio = new ArrayList();
		this.cateterSondaHistoTodosAsocio = new ArrayList();
		this.mapaHistoricoCateterSondaAsocio = new HashMap();
		
		this.existeControlLiquidosAsocio = false;
		this.mapaControlLiqAsocio = new HashMap();
		
		this.existeSoporteRespiratorioAsocio = false;
		this.soporteRespiratorioAsocio = new HashMap();
		
		this.existeCuidadosEspecialesAsocio = false;
		this.mapaHistoricoCuidadosEspecialesAsocio = new HashMap();
		
		this.existeHojaNeurologicaAsocio = false;
		this.escalasGlasgowInstitucionCCostoAsocio = new ArrayList();
		this.mapaHistoricoEscalaGlasgowAsocio = new HashMap();
		this.mapaHistoricoPupilasAsocio = new HashMap();
		this.mapaHistoricoConvulsionesAsocio = new HashMap();
		this.mapaHistoricoControlEsfinteresAsocio = new HashMap();
		this.mapaHistoricoFuerzaMuscularAsocio = new HashMap();
		//---------------------------------------------------------
		
		this.egresoAsocio = 0;
		this.consultasPYPAsocio = new HashMap();
		this.numConsultasPYPAsocio = 0;
		this.existeConsultasPYPAsocio = false;
		this.datosAdmisionAsocio = new ArrayList();
		this.existeValoracionInicialAsocio = false;
		
		//atributos para respuesta de cirugias
		this.existenCirugiasAsocio = false;
		mapaCodigosPeticionCirugiaAsocio = new HashMap();
		mapaEncabezadosHojaAnestesiaAsocio = new HashMap();
		mapaExamenesLaboratorioPreanestesiaAsocio = new HashMap();
		mapaHistoExamenesFisicosTextAsocio = new HashMap();
		mapaHistoExamenesFisicosTextAreaAsocio = new HashMap();
		mapaHistoConclusionesAsocio = new HashMap();
		mapaHistoBalanceLiquidosHojaAnestesiaAsocio = new HashMap();
		mapaHistoMedicamentosHojaAnestesiaAsocio = new HashMap();
		listadoTecAnestesiaOpcionesGralAsocio = new ArrayList();
		listadoTecAnestesiaGralAsocio = new ArrayList();
		listadoTecAnestesiaRegionalAsocio = new ArrayList();
		mapaHistoTecAnestesiaAsocio = new HashMap();
		listadoSignosVitalesAsocio = new ArrayList();
		mapaHistoSignosVitalesAsocio = new HashMap();
		mapaHojaQuirurAsocio = new HashMap();
		
		this.tipoValoracionInicialAsocio = 0;
		this.adminMedicamentosAsocio = new HashMap();
		this.insumosAsocio = new HashMap();
	}
	
	public void resetMostrarInformacion()
	{
		///atributos para mostrar la informacion
		this.filtroAsocio = "";
		this.fechaInicial = "";
		this.fechaFinal = "";
		this.horaInicial = "";
		this.horaFinal = "";
		
	}
	
	public void resetBusqueda()
	{
		existeValoracionInicial=false;
		existenInterconsultas=false;
		existenProcedimientos=false;
		existenCirugias=false;
		existeAdminMedicamentos=false;
		existeConsumoInsumos = false;
		existenEvoluciones=false;
		egreso=0;
		
		this.existenAntecedentesVarios = false;
		this.existenAntecedentesToxicos = false;
		this.existenAntecedentesPediatricos = false;
		this.existenAntecedentesOdontologicos=false;
		
		this.existeNotasEnfermeria = false;
		this.existeSignosVitales = false;
		this.existeCateterSonda = false;
		this.existeControlLiquidos = false;
		this.existeSoporteRespiratorio = false;
		this.existeCuidadosEspeciales = false;
		this.existeHojaNeurologica = false;
		
		this.existeConsultasPYP = false;
		this.accidenteTransito = false;
		this.eventoCatastrofico=false;
		
		
		this.existenInterconsultasAsocio = false;
		this.existenProcedimientosAsocio = false;
		this.existenEvolucionesAsocio = false;
		this.existeAdminMedicamentosAsocio = false;
		this.existeConsumoInsumosAsocio = false;
		this.existenOrdenesMedicas = false;
		
		this.existeNotasEnfermeriaAsocio = false;
		this.existeSignosVitalesAsocio = false;
		this.existeCateterSondaAsocio = false;
		this.existeControlLiquidosAsocio = false;
		this.existeSoporteRespiratorioAsocio = false;
		this.existeCuidadosEspecialesAsocio = false;
		this.existeHojaNeurologicaAsocio = false;
		
		this.egresoAsocio = 0;
		this.existeConsultasPYPAsocio = false;
		this.existeValoracionInicialAsocio = false;
		this.existenCirugiasAsocio = false;
		this.existenOrdenesMedicasAsocio = false;
		
		this.existenCitas = false;
		
		
	}

	/**
	 * @return los ingresos que se muestran al ingresar a la funcionalidad
	 */
	public Collection getCuentas()
	{
		return cuentas;
	}

	/**
	 * Asignar los ingresos que se muestran al ingresar a la funcionalidad
	 * @param collection
	 */
	public void setCuentas(Collection collection)
	{
		cuentas = collection;
	}

	/**
	 * @return collection con lista de antecedentes
	 */
	public Collection getAntecedentes()
	{
		return antecedentes;
	}

	/**
	 * @param collection con lo antecedentes a listar
	 */
	public void setAntecedentes(Collection collection)
	{
		antecedentes = collection;
	}

	/**
	 * @return true si existen antecedentes de Alergias
	 */
	public boolean isExistenAntecedentesAlergias()
	{
		return existenAntecedentesAlergias;
	}

	/**
	 * @return true si existen antecedentes Familiares
	 */
	public boolean isExistenAntecedentesFamiliares()
	{
		return existenAntecedentesFamiliares;
	}

	/**
	 * @return  true si existen antecedentes GinecoObstétricos
	 */
	public boolean isExistenAntecedentesGinecoObstetricos()
	{
		return existenAntecedentesGinecoObstetricos;
	}

	/**
	 * @return true si existen antecedentes de Medicamentos
	 */
	public boolean isExistenAntecedentesMedicamentos()
	{
		return existenAntecedentesMedicamentos;
	}

	

	/**
	 * @return true si existen antecedentes Pediátricos
	 */
	public boolean isExistenAntecedentesPediatricos()
	{
		return existenAntecedentesPediatricos;
	}

	/**
	 * @return true si existen antecedentes Tóxicos
	 */
	public boolean isExistenAntecedentesToxicos()
	{
		return existenAntecedentesToxicos;
	}

	/**
	 * @return true si existen antecedentes Transfusionales
	 */
	public boolean isExistenAntecedentesTransfusionales()
	{
		return existenAntecedentesTransfusionales;
	}

	/**
	 * @return true si existen antecedentes Varios
	 */
	public boolean isExistenAntecedentesVarios()
	{
		return existenAntecedentesVarios;
	}

	/**
	 * Asigna true si existen antecedentes de Alergias 
	 * @param b
	 */
	public void setExistenAntecedentesAlergias(boolean b)
	{
		existenAntecedentesAlergias = b;
	}

	/**
	 * Asigna true si existen antecedentes Familiares
	 * @param b
	 */
	public void setExistenAntecedentesFamiliares(boolean b)
	{
		existenAntecedentesFamiliares = b;
	}

	/**
	 * Asigna true si existen antecedentes GinecoObstétricos
	 * @param b
	 */
	public void setExistenAntecedentesGinecoObstetricos(boolean b)
	{
		existenAntecedentesGinecoObstetricos = b;
	}

	/**
	 * Asigna true si existen antecedentes Medicamentos
	 * @param b
	 */
	public void setExistenAntecedentesMedicamentos(boolean b)
	{
		existenAntecedentesMedicamentos = b;
	}

	

	/**
	 * Asigna true si existen antecedentes Pediátricos
	 * @param b
	 */
	public void setExistenAntecedentesPediatricos(boolean b)
	{
		existenAntecedentesPediatricos = b;
	}

	/**
	 * Asigna true si existen antecedentes Tóxicos
	 * @param b
	 */
	public void setExistenAntecedentesToxicos(boolean b)
	{
		existenAntecedentesToxicos = b;
	}

	/**
	 * Asigna true si existen antecedentes Transfusionales
	 * @param b
	 */
	public void setExistenAntecedentesTransfusionales(boolean b)
	{
		existenAntecedentesTransfusionales = b;
	}

	/**
	 * Asigna true si existen antecedentes Varios
	 * @param b
	 */
	public void setExistenAntecedentesVarios(boolean b)
	{
		existenAntecedentesVarios = b;
	}

	/**
	 * Verificar si hay antecedentes para imprimir la sección
	 * @return true si exixte algún antecedente
	 */
	public boolean isHayAntecedentes()
	{
		return (this.existenAntecedentesAlergias || this.existenAntecedentesFamiliares ||
					this.existenAntecedentesGinecoObstetricos || this.existenAntecedentesMedicamentos ||
					this.existenAntecedentesMedicos || this.existenAntecedentesPediatricos ||
					this.existenAntecedentesToxicos || this.existenAntecedentesTransfusionales ||
					this.existenAntecedentesVarios || this.existenAntecedentesOftalFamiliares || 
					this.existenAntecedentesOftalPersonales || this.existenAntecedentesOdontologicos || this.existenAntecedentesVacunas);
	}

	/**
	 * @return cuenta del Usuario
	 */
	public int getCuenta()
	{
		return cuenta;
	}

	/**
	 * Asignar la cuenta del Usuario
	 * @param cuenta
	 */
	public void setCuenta(int cuenta)
	{
		this.cuenta = cuenta;
	}

	/**
	 * @return datos específicos de la admisión
	 */
	public Collection getDatosAdmision()
	{
		return datosAdmision;
	}

	/**
	 * Asignar los datos específicos de la admisión
	 * @param collection
	 */
	public void setDatosAdmision(Collection collection)
	{
		datosAdmision = collection;
	}

	/**
	 * @return la via de ingreso del paciente
	 */
	public int getViaIngreso()
	{
		return viaIngreso;
	}

	/**
	 * Asignar la via de ingreso del paciente
	 * @param i
	 */
	public void setViaIngreso(int i)
	{
		viaIngreso = i;
	}

	
	/**
	 * Verivicar la existencia de cirugías para el paciente (Pendiente)
	 * @return
	 */
	public boolean isExistenCirugias()
	{
		return existenCirugias;
	}

	/**
	 * Verivicar la existencia de evoluciones para el paciente
	 * @return
	 */
	public boolean isExistenEvoluciones()
	{
		return existenEvoluciones;
	}

	/**
	 * Verivicar la existencia de interconsultas para el paciente
	 * @return
	 */
	public boolean isExistenInterconsultas()
	{
		return existenInterconsultas;
	}

	/**
	 * Verivicar la existencia de procedimientos para el paciente
	 * @return
	 */
	public boolean isExistenProcedimientos()
	{
		return existenProcedimientos;
	}

	/**
	 * Verivicar la existencia de valoración inicial para el paciente
	 * (Urgencias General, Urgencias Pediátrica, Hospitalización General, Hospitalización Pediátrica)
	 * @return
	 */
	public boolean isExisteValoracionInicial()
	{
		return existeValoracionInicial;
	}

	
	/**
	 * Verivicar la existencia de cirugías para el paciente (Pendiente)
	 * @param b
	 */
	public void setExistenCirugias(boolean b)
	{
		existenCirugias = b;
	}

	/**
	 * Verivicar la existencia de evolucion para el paciente
	 * @param b
	 */
	public void setExistenEvoluciones(boolean b)
	{
		existenEvoluciones = b;
	}

	/**
	 * Verivicar la existencia de interconsultas para el paciente
	 * @param b
	 */
	public void setExistenInterconsultas(boolean b)
	{
		existenInterconsultas = b;
	}

	/**
	 * Verivicar la existencia de procedimientos para el paciente
	 * @param b
	 */
	public void setExistenProcedimientos(boolean b)
	{
		existenProcedimientos = b;
	}

	/**
	 * Verivicar la existencia de valoración inicial para el paciente
	 * (Urgencias General, Urgencias Pediátrica, Hospitalización General, Hospitalización Pediátrica)
	 * @param b
	 */
	public void setExisteValoracionInicial(boolean b)
	{
		existeValoracionInicial = b;
	}

	/**
	 * Este método me dice si en la sección de atenciones hay información, de no haber, no se debe mostrar
	 * @return
	 */
	public boolean getExistenAtenciones()
	{
		boolean respuesta = false;
		
		if(this.mapaValidacionesInfoCuentas!= null && 
				this.mapaValidacionesInfoCuentas.containsKey("numRegistros"))
		{
			int numRegistros = Integer.parseInt(this.mapaValidacionesInfoCuentas.get("numRegistros").toString());
			for(int i = 0; i < numRegistros ; i++)
			{
				if(this.mapaValidacionesInfoCuentas.containsKey("existenCirugias_"+i) && 
						UtilidadTexto.getBoolean(this.mapaValidacionesInfoCuentas.get("existenCirugias_"+i).toString()))
					respuesta = true;
				
				if(this.mapaValidacionesInfoCuentas.containsKey("existeValoracionInicial_"+i) && 
						UtilidadTexto.getBoolean(this.mapaValidacionesInfoCuentas.get("existeValoracionInicial_"+i).toString()))
					respuesta = true;
				
				if(this.mapaValidacionesInfoCuentas.containsKey("existeAdminMedicamentos_"+i) && 
						UtilidadTexto.getBoolean(this.mapaValidacionesInfoCuentas.get("existeAdminMedicamentos_"+i).toString()))
					respuesta = true;
				
				if(this.mapaValidacionesInfoCuentas.containsKey("existeConsumoInsumos_"+i) && 
						UtilidadTexto.getBoolean(this.mapaValidacionesInfoCuentas.get("existeConsumoInsumos_"+i).toString()))
					respuesta = true;
				
				if(this.mapaValidacionesInfoCuentas.containsKey("existenCirugias_"+i) && 
						UtilidadTexto.getBoolean(this.mapaValidacionesInfoCuentas.get("existenCirugias_"+i).toString()))
					respuesta = true;
				
				if(this.mapaValidacionesInfoCuentas.containsKey("existenEvoluciones_"+i) && 
						UtilidadTexto.getBoolean(this.mapaValidacionesInfoCuentas.get("existenEvoluciones_"+i).toString()))
					respuesta = true;
				
				if(this.mapaValidacionesInfoCuentas.containsKey("egreso_"+i) && 
						!this.mapaValidacionesInfoCuentas.get("egreso_"+i).toString().equals("0"))
					respuesta = true;
				
				if(this.mapaValidacionesInfoCuentas.containsKey("existenInterconsultas_"+i) && 
						UtilidadTexto.getBoolean(this.mapaValidacionesInfoCuentas.get("existenInterconsultas_"+i).toString()))
					respuesta = true;				
				
				if(this.mapaValidacionesInfoCuentas.containsKey("existenProcedimientos_"+i) && 
						UtilidadTexto.getBoolean(this.mapaValidacionesInfoCuentas.get("existenProcedimientos_"+i).toString()))
					respuesta = true;
				
				if(this.mapaValidacionesInfoCuentas.containsKey("existeConsultasPYP_"+i) && 
						UtilidadTexto.getBoolean(this.mapaValidacionesInfoCuentas.get("existeConsultasPYP_"+i).toString()))
					respuesta = true;
				
				if(this.mapaValidacionesInfoCuentas.containsKey("existenInterconsultasAsocio_"+i) && 
						UtilidadTexto.getBoolean(this.mapaValidacionesInfoCuentas.get("existenInterconsultasAsocio_"+i).toString()))
					respuesta = true;
				
				if(this.mapaValidacionesInfoCuentas.containsKey("existeNotasEnfermeria_"+i) && 
						UtilidadTexto.getBoolean(this.mapaValidacionesInfoCuentas.get("existeNotasEnfermeria_"+i).toString()))
					respuesta = true;
				
				if(this.mapaValidacionesInfoCuentas.containsKey("existenProcedimientosAsocio_"+i) && 
						UtilidadTexto.getBoolean(this.mapaValidacionesInfoCuentas.get("existenProcedimientosAsocio_"+i).toString()))
					respuesta = true;
				
				if(this.mapaValidacionesInfoCuentas.containsKey("eventoAdverso_"+i) && 
						UtilidadTexto.getBoolean(this.mapaValidacionesInfoCuentas.get("eventoAdverso_"+i).toString()))
					respuesta = true;
				
				if(this.mapaValidacionesInfoCuentas.containsKey("existenEvolucionesAsocio_"+i) && 
						UtilidadTexto.getBoolean(this.mapaValidacionesInfoCuentas.get("existenEvolucionesAsocio_"+i).toString()))
					respuesta = true;
				
				if(this.mapaValidacionesInfoCuentas.containsKey("existeAdminMedicamentosAsocio_"+i) && 
						UtilidadTexto.getBoolean(this.mapaValidacionesInfoCuentas.get("existeAdminMedicamentosAsocio_"+i).toString()))
					respuesta = true;
				
				if(this.mapaValidacionesInfoCuentas.containsKey("existeConsumoInsumosAsocio_"+i) && 
						UtilidadTexto.getBoolean(this.mapaValidacionesInfoCuentas.get("existeConsumoInsumosAsocio_"+i).toString()))
					respuesta = true;
				
				if(this.mapaValidacionesInfoCuentas.containsKey("existeNotasEnfermeriaAsocio_"+i) && 
						UtilidadTexto.getBoolean(this.mapaValidacionesInfoCuentas.get("existeNotasEnfermeriaAsocio_"+i).toString()))
					respuesta = true;
				
				if(this.mapaValidacionesInfoCuentas.containsKey("egresoAsocio_"+i) && 
						!this.mapaValidacionesInfoCuentas.get("egresoAsocio_"+i).toString().equals("0"))
					respuesta = true;
				
				if(this.mapaValidacionesInfoCuentas.containsKey("existeConsultasPYPAsocio_"+i) && 
						UtilidadTexto.getBoolean(this.mapaValidacionesInfoCuentas.get("existeConsultasPYPAsocio_"+i).toString()))
					respuesta = true;
				
				if(this.mapaValidacionesInfoCuentas.containsKey("existeValoracionInicialAsocio_"+i) && 
						UtilidadTexto.getBoolean(this.mapaValidacionesInfoCuentas.get("existeValoracionInicialAsocio_"+i).toString()))
					respuesta = true;
				
				if(this.mapaValidacionesInfoCuentas.containsKey("existenCirugiasAsocio_"+i) && 
						UtilidadTexto.getBoolean(this.mapaValidacionesInfoCuentas.get("existenCirugiasAsocio_"+i).toString()))
					respuesta = true;
				
				if(this.mapaValidacionesInfoCuentas.containsKey("isHayAntecedentes_"+i) && 
						UtilidadTexto.getBoolean(this.mapaValidacionesInfoCuentas.get("isHayAntecedentes_"+i).toString()))
					respuesta = true;
				
				if(this.mapaValidacionesInfoCuentas.containsKey("accidenteTransito_"+i) && 
						UtilidadTexto.getBoolean(this.mapaValidacionesInfoCuentas.get("accidenteTransito_"+i).toString()))
					respuesta = true;
				
				if(this.mapaValidacionesInfoCuentas.containsKey("existenCitas_"+i) && 
						UtilidadTexto.getBoolean(this.mapaValidacionesInfoCuentas.get("existenCitas_"+i).toString()))
					respuesta = true;
				
				if(this.mapaValidacionesInfoCuentas.containsKey("existenCitasAsocio_"+i) && 
						UtilidadTexto.getBoolean(this.mapaValidacionesInfoCuentas.get("existenCitasAsocio_"+i).toString()))
					respuesta = true;
				
				if(this.mapaValidacionesInfoCuentas.containsKey("eventoCatastrofico_"+i) && 
						UtilidadTexto.getBoolean(this.mapaValidacionesInfoCuentas.get("eventoCatastrofico_"+i).toString()))
					respuesta = true;
				
				if(this.mapaValidacionesInfoCuentas.containsKey("existenOrdenesMedicas_"+i) && 
						UtilidadTexto.getBoolean(this.mapaValidacionesInfoCuentas.get("existenOrdenesMedicas_"+i).toString()))
					respuesta = true;
				
				if(this.mapaValidacionesInfoCuentas.containsKey("existenEscalasXIngreso_"+i) && 
						UtilidadTexto.getBoolean(this.mapaValidacionesInfoCuentas.get("existenEscalasXIngreso_"+i).toString()))
					respuesta = true;
				
				if(this.mapaValidacionesInfoCuentas.containsKey("existeCargosDirectos_"+i) && 
						UtilidadTexto.getBoolean(this.mapaValidacionesInfoCuentas.get("existeCargosDirectos_"+i).toString()))
					respuesta = true;
				
				
				
				if(this.mapaValidacionesInfoCuentas.containsKey("existenValoracionesCuidadoEspecial_"+i) && 
						UtilidadTexto.getBoolean(this.mapaValidacionesInfoCuentas.get("existenValoracionesCuidadoEspecial_"+i).toString()))
					respuesta = true;
				
				if(this.getMuestroResumenPHC()>0){
					respuesta = true;
				}
				
				if(this.getMuestroResumenPHCAsocio()>0){
					respuesta = true;
				}
				
				
			}			
			return respuesta;
		}
		else
			return respuesta;	
	}
	
	public Integer getNumeroResultados(){
		Integer rta = Integer.parseInt(this.mapaValidacionesInfoCuentas.get("numRegistros").toString()); 
		return rta;
	}

	/**
	 * @return el tipo de valoración
	 * Hospitalizacion General
	 * Hospitalizacion Pediátrica
	 * Urgencias General
	 * Urgencias Pediátrica
	 */
	public int getTipoValoracionInicial()
	{
		return tipoValoracionInicial;
	}

	/**
	 * Asigna el tipo de valoración
	 * Hospitalizacion General
	 * Hospitalizacion Pediátrica
	 * Urgencias General
	 * Urgencias Pediátrica 
	 * @param i
	 */
	public void setTipoValoracionInicial(int i)
	{
		tipoValoracionInicial = i;
	}

	/**
	 * Retorna el egreso
	 * Se hace con el fin de verificar sy hay egreso o no, y cual es la evolución en la cual se hizo
	 * @return 0		-->	No hay egreso
	 * @return >0 	-->	Número de la evolución que contiene la orden de salida
	 */
	public int getEgreso()
	{
		return egreso;
	}

	

	/**
	 * Asigna el egreso
	 * Se hace con el fin de verificar sy hay egreso o no, y cual es la evolución en la cual se hizo
	 * @return 0		-->	No hay egreso
	 * @return >0 	-->	Número de la evolución que contiene la orden de salida
	 */
	public void setEgreso(int i)
	{
		egreso = i;
	}

	/**
	 * @return Retorna el coleccionSolicitudes.
	 */
	public Collection getColeccionSolicitudes() {
		return coleccionSolicitudes;
	}
	/**
	 * @param coleccionSolicitudes Asigna el coleccionSolicitudes.
	 */
	public void setColeccionSolicitudes(Collection coleccionSolicitudes) {
		this.coleccionSolicitudes = coleccionSolicitudes;
	}
	/**
	 * @return Retorna el mostrarMensajeAsocio.
	 */
	public boolean isMostrarMensajeAsocio() {
		return mostrarMensajeAsocio;
	}
	/**
	 * @param mostrarMensajeAsocio Asigna el mostrarMensajeAsocio.
	 */
	public void setMostrarMensajeAsocio(boolean mostrarMensajeAsocio) {
		this.mostrarMensajeAsocio = mostrarMensajeAsocio;
	}
	
	/**
	 * Metod que retorna el bojeto del hashmap dado su key
	 * @param key, Indice
	 * @return, Object, valor del hashmap
	 */
	public Object getSolicitudes(String key)
	{
		return  solicitudes.get(key);
	}
	
	/**
	 * Metodo que asigna un ogjeto al hashmap
	 * @param key
	 * @param value
	 */
	public void setSolicitudes(String key,Object value)
	{
		solicitudes.put(key, value);
	}
	/**
	 * @return Retorna el solicitudes.
	 */
	public HashMap getSolicitudes() {
		return solicitudes;
	}
	
	/**
	 * @param solicitudes Asigna el solicitudes.
	 */
	public void setSolicitudes(HashMap solicitudes) {
		this.solicitudes = solicitudes;
	}
	/**
	 * @return Retorna el numeroSolicitudes.
	 */
	public int getNumeroSolicitudes() {
		return numeroSolicitudes;
	}
	/**
	 * @param numeroSolicitudes Asigna el numeroSolicitudes.
	 */
	public void setNumeroSolicitudes(int numeroSolicitudes) {
		this.numeroSolicitudes = numeroSolicitudes;
	}
	/**
	 * @return Retorna el columna.
	 */
	public String getColumna() {
		return columna;
	}
	/**
	 * @param columna Asigna el columna.
	 */
	public void setColumna(String columna) {
		this.columna = columna;
	}
	/**
	 * @return Retorna el ultimaPropiedad.
	 */
	public String getUltimaPropiedad() {
		return ultimaPropiedad;
	}
	/**
	 * @param ultimaPropiedad Asigna el ultimaPropiedad.
	 */
	public void setUltimaPropiedad(String ultimaPropiedad) {
		this.ultimaPropiedad = ultimaPropiedad;
	}
	/**
	 * @return Retorna el fEgreso.
	 */
	public String getFechEgreso() {
		return fechEgreso;
	}
	/**
	 * @param egreso Asigna el fEgreso.
	 */
	public void setFechEgreso(String egreso) {
		fechEgreso = egreso;
	}
	/**
	 * @return Retorna el fIngreso.
	 */
	public String getFechIngreso() {
		return fechIngreso;
	}
	/**
	 * @param ingreso Asigna el fIngreso.
	 */
	public void setFechIngreso(String ingreso) {
		fechIngreso = ingreso;
	}
	/**
	 * @return Retorna el nomViaIngreso.
	 */
	public String getNomViaIngreso() {
		return nomViaIngreso;
	}
	/**
	 * @param nomViaIngreso Asigna el nomViaIngreso.
	 */
	public void setNomViaIngreso(String nomViaIngreso) {
		this.nomViaIngreso = nomViaIngreso;
	}
	/**
	 * @return Retorna el numeroAdministraciones.
	 */
	public int getNumeroAdministraciones() {
		return numeroAdministraciones;
	}
	/**
	 * @param numeroAdministraciones Asigna el numeroAdministraciones.
	 */
	public void setNumeroAdministraciones(int numeroAdministraciones) {
		this.numeroAdministraciones = numeroAdministraciones;
	}
	/**
	 * @return Returns the maxPageItems.
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}
	/**
	 * @param maxPageItems The maxPageItems to set.
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}
	/**
	 * @return Returns the offset.
	 */
	public int getOffset() {
		return offset;
	}
	/**
	 * @param offset The offset to set.
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}
	/**
	 * @return Returns the linkSiguiente.
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}
	/**
	 * @param linkSiguiente The linkSiguiente to set.
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}
	/**
	 * @return Retorna existenAntecedentesOftalFamiliares.
	 */
	public boolean getExistenAntecedentesOftalFamiliares() {
		return existenAntecedentesOftalFamiliares;
	}
	/**
	 * @param Asigna existenAntecedentesOftalFamiliares.
	 */
	public void setExistenAntecedentesOftalFamiliares(
			boolean existenAntecedentesOftalFamiliares) {
		this.existenAntecedentesOftalFamiliares = existenAntecedentesOftalFamiliares;
	}
	/**
	 * @return Retorna existenAntecedentesOftalPersonales.
	 */
	public boolean getExistenAntecedentesOftalPersonales() {
		return existenAntecedentesOftalPersonales;
	}
	/**
	 * @param Asigna existenAntecedentesOftalPersonales.
	 */
	public void setExistenAntecedentesOftalPersonales(
			boolean existenAntecedentesOftalPersonales) {
		this.existenAntecedentesOftalPersonales = existenAntecedentesOftalPersonales;
	}


	/**
	 * @return Retorna existenAntecedentesOdontologicos.
	 */
	public boolean getExistenAntecedentesOdontologicos() {
		return existenAntecedentesOdontologicos;
	}


	/**
	 * @param Asigna existenAntecedentesOdontologicos.
	 */
	public void setExistenAntecedentesOdontologicos(
			boolean existenAntecedentesOdontologicos) {
		this.existenAntecedentesOdontologicos = existenAntecedentesOdontologicos;
	}
	
	/**
	 * @return Retorna existenAntecedentesVacunas
	 */
	public boolean getExistenAntecedentesVacunas() {
		return existenAntecedentesVacunas;
	}

	/**
	 * @param Asigna existenAntecedentesVacunas.
	 */
	public void setExistenAntecedentesVacunas(
			boolean existenAntecedentesVacunas) {
		this.existenAntecedentesVacunas = existenAntecedentesVacunas;
	}

	

	/**
	 * @return Returns the existeConsultasPYP.
	 */
	public boolean isExisteConsultasPYP() {
		return existeConsultasPYP;
	}


	/**
	 * @param existeConsultasPYP The existeConsultasPYP to set.
	 */
	public void setExisteConsultasPYP(boolean existeConsultasPYP) {
		this.existeConsultasPYP = existeConsultasPYP;
	}


	/**
	 * @return Returns the consultasPYP.
	 */
	public HashMap getConsultasPYP() {
		return consultasPYP;
	}


	/**
	 * @param consultasPYP The consultasPYP to set.
	 */
	public void setConsultasPYP(HashMap consultasPYP) {
		this.consultasPYP = consultasPYP;
	}
	
	/**
	 * @return Retorna un elemento del mapa consultasPYP.
	 */
	public Object getConsultasPYP(String key) {
		return consultasPYP.get(key);
	}


	/**
	 * @param Asigna un elemento al mapa consultasPYP.
	 */
	public void setConsultasPYP(String key,Object obj) {
		this.consultasPYP.put(key,obj);
	}

	/**
	 * @return Returns the numConsultasPYP.
	 */
	public int getNumConsultasPYP() {
		return numConsultasPYP;
	}


	/**
	 * @param numConsultasPYP The numConsultasPYP to set.
	 */
	public void setNumConsultasPYP(int numConsultasPYP) {
		this.numConsultasPYP = numConsultasPYP;
	}


	/**
	 * @return Returns the indiceConsultasPYP.
	 */
	public String getIndiceConsultasPYP() {
		return indiceConsultasPYP;
	}


	/**
	 * @param indiceConsultasPYP The indiceConsultasPYP to set.
	 */
	public void setIndiceConsultasPYP(String indiceConsultasPYP) {
		this.indiceConsultasPYP = indiceConsultasPYP;
	}


	/**
	 * @return Returns the ultimoIndiceConsultasPYP.
	 */
	public String getUltimoIndiceConsultasPYP() {
		return ultimoIndiceConsultasPYP;
	}


	/**
	 * @param ultimoIndiceConsultasPYP The ultimoIndiceConsultasPYP to set.
	 */
	public void setUltimoIndiceConsultasPYP(String ultimoIndiceConsultasPYP) {
		this.ultimoIndiceConsultasPYP = ultimoIndiceConsultasPYP;
	}


	/**
	 * @return Returns the usuario.
	 */
	public String getUsuario() {
		return usuario;
	}


	/**
	 * @param usuario The usuario to set.
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}


	/**
	 * @return Returns the consultasPYPAsocio.
	 */
	public HashMap getConsultasPYPAsocio() {
		return consultasPYPAsocio;
	}


	/**
	 * @param Asigna un elemento al mapa consultasPYPAsocio.
	 */
	public void setConsultasPYPAsocio(String key, Object obj) {
		this.consultasPYPAsocio.put(key,obj);
	}
	
	/**
	 * @return Retorna un elemento del mapa consultasPYPAsocio.
	 */
	public Object getConsultasPYPAsocio(String key) {
		return consultasPYPAsocio.get(key);
	}


	/**
	 * @param consultasPYPAsocio The consultasPYPAsocio to set.
	 */
	public void setConsultasPYPAsocio(HashMap consultasPYPAsocio) {
		this.consultasPYPAsocio = consultasPYPAsocio;
	}


	/**
	 * @return Returns the datosAdmisionAsocio.
	 */
	public Collection getDatosAdmisionAsocio() {
		return datosAdmisionAsocio;
	}


	/**
	 * @param datosAdmisionAsocio The datosAdmisionAsocio to set.
	 */
	public void setDatosAdmisionAsocio(Collection datosAdmisionAsocio) {
		this.datosAdmisionAsocio = datosAdmisionAsocio;
	}


	/**
	 * @return Returns the egresoAsocio.
	 */
	public int getEgresoAsocio() {
		return egresoAsocio;
	}


	/**
	 * @param egresoAsocio The egresoAsocio to set.
	 */
	public void setEgresoAsocio(int egresoAsocio) {
		this.egresoAsocio = egresoAsocio;
	}


	/**
	 * @return Returns the existeConsultasPYPAsocio.
	 */
	public boolean isExisteConsultasPYPAsocio() {
		return existeConsultasPYPAsocio;
	}


	/**
	 * @param existeConsultasPYPAsocio The existeConsultasPYPAsocio to set.
	 */
	public void setExisteConsultasPYPAsocio(boolean existeConsultasPYPAsocio) {
		this.existeConsultasPYPAsocio = existeConsultasPYPAsocio;
	}


	

	/**
	 * @return Returns the existeAdminMedicamentosAsocio.
	 */
	public boolean isExisteAdminMedicamentosAsocio() {
		return existeAdminMedicamentosAsocio;
	}


	/**
	 * @param existeAdminMedicamentosAsocio The existeAdminMedicamentosAsocio to set.
	 */
	public void setExisteAdminMedicamentosAsocio(
			boolean existeAdminMedicamentosAsocio) {
		this.existeAdminMedicamentosAsocio = existeAdminMedicamentosAsocio;
	}


	/**
	 * @return Returns the existeConsumoInsumosAsocio.
	 */
	public boolean isExisteConsumoInsumosAsocio() {
		return existeConsumoInsumosAsocio;
	}


	/**
	 * @param existeConsumoInsumosAsocio The existeConsumoInsumosAsocio to set.
	 */
	public void setExisteConsumoInsumosAsocio(boolean existeConsumoInsumosAsocio) {
		this.existeConsumoInsumosAsocio = existeConsumoInsumosAsocio;
	}


	/**
	 * @return Returns the existenCirugiasAsocio.
	 */
	public boolean isExistenCirugiasAsocio() {
		return existenCirugiasAsocio;
	}


	/**
	 * @param existenCirugiasAsocio The existenCirugiasAsocio to set.
	 */
	public void setExistenCirugiasAsocio(boolean existenCirugiasAsocio) {
		this.existenCirugiasAsocio = existenCirugiasAsocio;
	}


	/**
	 * @return Returns the existenEvolucionesAsocio.
	 */
	public boolean isExistenEvolucionesAsocio() {
		return existenEvolucionesAsocio;
	}


	/**
	 * @param existenEvolucionesAsocio The existenEvolucionesAsocio to set.
	 */
	public void setExistenEvolucionesAsocio(boolean existenEvolucionesAsocio) {
		this.existenEvolucionesAsocio = existenEvolucionesAsocio;
	}


	/**
	 * @return Returns the existenInterconsultasAsocio.
	 */
	public boolean isExistenInterconsultasAsocio() {
		return existenInterconsultasAsocio;
	}


	/**
	 * @param existenInterconsultasAsocio The existenInterconsultasAsocio to set.
	 */
	public void setExistenInterconsultasAsocio(boolean existenInterconsultasAsocio) {
		this.existenInterconsultasAsocio = existenInterconsultasAsocio;
	}


	/**
	 * @return Returns the existenProcedimientosAsocio.
	 */
	public boolean isExistenProcedimientosAsocio() {
		return existenProcedimientosAsocio;
	}


	/**
	 * @param existenProcedimientosAsocio The existenProcedimientosAsocio to set.
	 */
	public void setExistenProcedimientosAsocio(boolean existenProcedimientosAsocio) {
		this.existenProcedimientosAsocio = existenProcedimientosAsocio;
	}



	/**
	 * @return Returns the existeValoracionInicialAsocio.
	 */
	public boolean isExisteValoracionInicialAsocio() {
		return existeValoracionInicialAsocio;
	}


	/**
	 * @param existeValoracionInicialAsocio The existeValoracionInicialAsocio to set.
	 */
	public void setExisteValoracionInicialAsocio(
			boolean existeValoracionInicialAsocio) {
		this.existeValoracionInicialAsocio = existeValoracionInicialAsocio;
	}


	/**
	 * @return Returns the numConsultasPYPAsocio.
	 */
	public int getNumConsultasPYPAsocio() {
		return numConsultasPYPAsocio;
	}


	/**
	 * @param numConsultasPYPAsocio The numConsultasPYPAsocio to set.
	 */
	public void setNumConsultasPYPAsocio(int numConsultasPYPAsocio) {
		this.numConsultasPYPAsocio = numConsultasPYPAsocio;
	}


	/**
	 * @return Returns the tipoValoracionInicialAsocio.
	 */
	public int getTipoValoracionInicialAsocio() {
		return tipoValoracionInicialAsocio;
	}


	/**
	 * @param tipoValoracionInicialAsocio The tipoValoracionInicialAsocio to set.
	 */
	public void setTipoValoracionInicialAsocio(int tipoValoracionInicialAsocio) {
		this.tipoValoracionInicialAsocio = tipoValoracionInicialAsocio;
	}


	/**
	 * @return Returns the impresionHistoriaClinica.
	 */
	public boolean isImpresionHistoriaClinica() {
		return impresionHistoriaClinica;
	}


	/**
	 * @param impresionHistoriaClinica The impresionHistoriaClinica to set.
	 */
	public void setImpresionHistoriaClinica(boolean impresionHistoriaClinica) {
		this.impresionHistoriaClinica = impresionHistoriaClinica;
	}


	/**
	 * @return Returns the asocio.
	 */
	public boolean isAsocio() {
		return asocio;
	}


	/**
	 * @param asocio The asocio to set.
	 */
	public void setAsocio(boolean asocio) {
		this.asocio = asocio;
	}


	/**
	 * @return Returns the cuentaAsocio.
	 */
	public String getCuentaAsocio() {
		return cuentaAsocio;
	}


	/**
	 * @param cuentaAsocio The cuentaAsocio to set.
	 */
	public void setCuentaAsocio(String cuentaAsocio) {
		this.cuentaAsocio = cuentaAsocio;
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
	 * @return Returns the fechaEgreso.
	 */
	public String getFechaEgreso() {
		return fechaEgreso;
	}


	/**
	 * @param fechaEgreso The fechaEgreso to set.
	 */
	public void setFechaEgreso(String fechaEgreso) {
		this.fechaEgreso = fechaEgreso;
	}


	/**
	 * @return Returns the fechaIngreso.
	 */
	public String getFechaIngreso() {
		return fechaIngreso;
	}


	/**
	 * @param fechaIngreso The fechaIngreso to set.
	 */
	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}


	/**
	 * @return Returns the idIngreso.
	 */
	public String getIdIngreso() {
		return idIngreso;
	}


	/**
	 * @param idIngreso The idIngreso to set.
	 */
	public void setIdIngreso(String idIngreso) {
		this.idIngreso = idIngreso;
	}


	/**
	 * @return Returns the existeAdminMedicamentos.
	 */
	public boolean isExisteAdminMedicamentos() {
		return existeAdminMedicamentos;
	}


	/**
	 * @param existeAdminMedicamentos The existeAdminMedicamentos to set.
	 */
	public void setExisteAdminMedicamentos(boolean existeAdminMedicamentos) {
		this.existeAdminMedicamentos = existeAdminMedicamentos;
	}


	/**
	 * @return Returns the existeConsumoInsumos.
	 */
	public boolean isExisteConsumoInsumos() {
		return existeConsumoInsumos;
	}


	/**
	 * @param existeConsumoInsumos The existeConsumoInsumos to set.
	 */
	public void setExisteConsumoInsumos(boolean existeConsumoInsumos) {
		this.existeConsumoInsumos = existeConsumoInsumos;
	}


	/**
	 * @return Retorna un elemento del mapa adminMedicamentos.
	 */
	public Object getAdminMedicamentos(String key) {
		return adminMedicamentos.get(key);
	}


	/**
	 * @param Asigna un elemento al mapa adminMedicamentos.
	 */
	public void setAdminMedicamentos(String key,Object obj) {
		this.adminMedicamentos.put(key,obj);
	}
	
	/**
	 * @return Returns the adminMedicamentos.
	 */
	public HashMap getAdminMedicamentos() {
		return adminMedicamentos;
	}


	/**
	 * @param adminMedicamentos The adminMedicamentos to set.
	 */
	public void setAdminMedicamentos(HashMap adminMedicamentos) {
		this.adminMedicamentos = adminMedicamentos;
	}


	/**
	 * @return Returns the adminMedicamentosAsocio.
	 */
	public HashMap getAdminMedicamentosAsocio() {
		return adminMedicamentosAsocio;
	}


	/**
	 * @param adminMedicamentosAsocio The adminMedicamentosAsocio to set.
	 */
	public void setAdminMedicamentosAsocio(HashMap adminMedicamentosAsocio) {
		this.adminMedicamentosAsocio = adminMedicamentosAsocio;
	}
	
	/**
	 * @return Retorna un elemento del mapa adminMedicamentosAsocio.
	 */
	public Object getAdminMedicamentosAsocio(String key) {
		return adminMedicamentosAsocio.get(key);
	}


	/**
	 * @param Asigna un elemento al mapa adminMedicamentosAsocio.
	 */
	public void setAdminMedicamentosAsocio(String key,Object obj) {
		this.adminMedicamentosAsocio.put(key,obj);
	}


	/**
	 * @return Returns the insumos.
	 */
	public HashMap getInsumos() {
		return insumos;
	}


	/**
	 * @param insumos The insumos to set.
	 */
	public void setInsumos(HashMap insumos) {
		this.insumos = insumos;
	}
	
	/**
	 * @return Retorna un elemento del mapa insumos.
	 */
	public Object getInsumos(String key) {
		return insumos.get(key);
	}


	/**
	 * @param Asigna elemento al mapa insumos.
	 */
	public void setInsumos(String key,Object obj) {
		this.insumos.put(key,obj);
	}


	/**
	 * @return Returns the insumosAsocio.
	 */
	public HashMap getInsumosAsocio() {
		return insumosAsocio;
	}


	/**
	 * @param insumosAsocio The insumosAsocio to set.
	 */
	public void setInsumosAsocio(HashMap insumosAsocio) {
		this.insumosAsocio = insumosAsocio;
	}
	
	/**
	 * @return Retorna un elemento del mapa insumosAsocio.
	 */
	public Object getInsumosAsocio(String key) {
		return insumosAsocio.get(key);
	}


	/**
	 * @param Asigna un elemento al mapa insumosAsocio.
	 */
	public void setInsumosAsocio(String key,Object obj) {
		this.insumosAsocio.put(key,obj);
	}


	/**
	 * @return Returns the cuentaAsociada.
	 */
	public boolean isCuentaAsociada() {
		return cuentaAsociada;
	}


	/**
	 * @param cuentaAsociada The cuentaAsociada to set.
	 */
	public void setCuentaAsociada(boolean cuentaAsociada) {
		this.cuentaAsociada = cuentaAsociada;
	}


	/**
	 * @return Returns the respuestaInterpretacionProcedimientos.
	 */
	public HashMap getRespuestaInterpretacionProcedimientos() {
		return respuestaInterpretacionProcedimientos;
	}


	/**
	 * @param Asigna elemento al mapa respuestaInterpretacionProcedimientos.
	 */
	public void setRespuestaInterpretacionProcedimientos(
			String key,Object obj) {
		this.respuestaInterpretacionProcedimientos.put(key,obj);
	}
	
	/**
	 * @return Retorna elemento del mapa respuestaInterpretacionProcedimientos.
	 */
	public Object getRespuestaInterpretacionProcedimientos(String key) {
		return respuestaInterpretacionProcedimientos.get(key);
	}


	/**
	 * @param respuestaInterpretacionProcedimientos The respuestaInterpretacionProcedimientos to set.
	 */
	public void setRespuestaInterpretacionProcedimientos(
			HashMap respuestaInterpretacionProcedimientos) {
		this.respuestaInterpretacionProcedimientos = respuestaInterpretacionProcedimientos;
	}


	/**
	 * @return Returns the respuestaInterpretacionProcedimientosAsocio.
	 */
	public HashMap getRespuestaInterpretacionProcedimientosAsocio() {
		return respuestaInterpretacionProcedimientosAsocio;
	}


	/**
	 * @param respuestaInterpretacionProcedimientosAsocio The respuestaInterpretacionProcedimientosAsocio to set.
	 */
	public void setRespuestaInterpretacionProcedimientosAsocio(
			HashMap respuestaInterpretacionProcedimientosAsocio) {
		this.respuestaInterpretacionProcedimientosAsocio = respuestaInterpretacionProcedimientosAsocio;
	}
	
	/**
	 * @return Retorna un elemento del mapa respuestaInterpretacionProcedimientosAsocio.
	 */
	public Object getRespuestaInterpretacionProcedimientosAsocio(String key) {
		return respuestaInterpretacionProcedimientosAsocio.get(key);
	}


	/**
	 * @param Asigna elemento al mapa respuestaInterpretacionProcedimientosAsocio.
	 */
	public void setRespuestaInterpretacionProcedimientosAsocio(
			String key,Object obj) {
		this.respuestaInterpretacionProcedimientosAsocio.put(key,obj);
	}


	/**
	 * @return Returns the existeNotasEnfermeria.
	 */
	public boolean isExisteNotasEnfermeria() {
		return existeNotasEnfermeria;
	}


	/**
	 * @param existeNotasEnfermeria The existeNotasEnfermeria to set.
	 */
	public void setExisteNotasEnfermeria(boolean existeNotasEnfermeria) {
		this.existeNotasEnfermeria = existeNotasEnfermeria;
	}


	/**
	 * @return Returns the existeNotasEnfermeriaAsocio.
	 */
	public boolean isExisteNotasEnfermeriaAsocio() {
		return existeNotasEnfermeriaAsocio;
	}


	/**
	 * @param existeNotasEnfermeriaAsocio The existeNotasEnfermeriaAsocio to set.
	 */
	public void setExisteNotasEnfermeriaAsocio(boolean existeNotasEnfermeriaAsocio) {
		this.existeNotasEnfermeriaAsocio = existeNotasEnfermeriaAsocio;
	}


	/**
	 * @return Returns the historicoAnotacionesEnfermeria.
	 */
	public Collection getHistoricoAnotacionesEnfermeria() {
		return historicoAnotacionesEnfermeria;
	}


	/**
	 * @param historicoAnotacionesEnfermeria The historicoAnotacionesEnfermeria to set.
	 */
	public void setHistoricoAnotacionesEnfermeria(
			Collection historicoAnotacionesEnfermeria) {
		this.historicoAnotacionesEnfermeria = historicoAnotacionesEnfermeria;
	}


	/**
	 * @return Returns the historicoAnotacionesEnfermeriaAsocio.
	 */
	public Collection getHistoricoAnotacionesEnfermeriaAsocio() {
		return historicoAnotacionesEnfermeriaAsocio;
	}


	/**
	 * @param historicoAnotacionesEnfermeriaAsocio The historicoAnotacionesEnfermeriaAsocio to set.
	 */
	public void setHistoricoAnotacionesEnfermeriaAsocio(
			Collection historicoAnotacionesEnfermeriaAsocio) {
		this.historicoAnotacionesEnfermeriaAsocio = historicoAnotacionesEnfermeriaAsocio;
	}


	/**
	 * @return Returns the existeSignosVitales.
	 */
	public boolean isExisteSignosVitales() {
		return existeSignosVitales;
	}


	/**
	 * @param existeSignosVitales The existeSignosVitales to set.
	 */
	public void setExisteSignosVitales(boolean existeSignosVitales) {
		this.existeSignosVitales = existeSignosVitales;
	}


	/**
	 * @return Returns the existeSignosVitalesAsocio.
	 */
	public boolean isExisteSignosVitalesAsocio() {
		return existeSignosVitalesAsocio;
	}


	/**
	 * @param existeSignosVitalesAsocio The existeSignosVitalesAsocio to set.
	 */
	public void setExisteSignosVitalesAsocio(boolean existeSignosVitalesAsocio) {
		this.existeSignosVitalesAsocio = existeSignosVitalesAsocio;
	}


	/**
	 * @return Returns the signosVitalesFijosHisto.
	 */
	public Collection getSignosVitalesFijosHisto() {
		return signosVitalesFijosHisto;
	}


	/**
	 * @param signosVitalesFijosHisto The signosVitalesFijosHisto to set.
	 */
	public void setSignosVitalesFijosHisto(Collection signosVitalesFijosHisto) {
		this.signosVitalesFijosHisto = signosVitalesFijosHisto;
	}


	/**
	 * @return Returns the signosVitalesFijosHistoAsocio.
	 */
	public Collection getSignosVitalesFijosHistoAsocio() {
		return signosVitalesFijosHistoAsocio;
	}


	/**
	 * @param signosVitalesFijosHistoAsocio The signosVitalesFijosHistoAsocio to set.
	 */
	public void setSignosVitalesFijosHistoAsocio(
			Collection signosVitalesFijosHistoAsocio) {
		this.signosVitalesFijosHistoAsocio = signosVitalesFijosHistoAsocio;
	}


	/**
	 * @return Returns the signosVitalesHistoTodos.
	 */
	public Collection getSignosVitalesHistoTodos() {
		return signosVitalesHistoTodos;
	}


	/**
	 * @param signosVitalesHistoTodos The signosVitalesHistoTodos to set.
	 */
	public void setSignosVitalesHistoTodos(Collection signosVitalesHistoTodos) {
		this.signosVitalesHistoTodos = signosVitalesHistoTodos;
	}


	/**
	 * @return Returns the signosVitalesHistoTodosAsocio.
	 */
	public Collection getSignosVitalesHistoTodosAsocio() {
		return signosVitalesHistoTodosAsocio;
	}


	/**
	 * @param signosVitalesHistoTodosAsocio The signosVitalesHistoTodosAsocio to set.
	 */
	public void setSignosVitalesHistoTodosAsocio(
			Collection signosVitalesHistoTodosAsocio) {
		this.signosVitalesHistoTodosAsocio = signosVitalesHistoTodosAsocio;
	}


	/**
	 * @return Returns the signosVitalesInstitucionCcosto.
	 */
	public Collection getSignosVitalesInstitucionCcosto() {
		return signosVitalesInstitucionCcosto;
	}


	/**
	 * @param signosVitalesInstitucionCcosto The signosVitalesInstitucionCcosto to set.
	 */
	public void setSignosVitalesInstitucionCcosto(
			Collection signosVitalesInstitucionCcosto) {
		this.signosVitalesInstitucionCcosto = signosVitalesInstitucionCcosto;
	}


	/**
	 * @return Returns the signosVitalesInstitucionCcostoAsocio.
	 */
	public Collection getSignosVitalesInstitucionCcostoAsocio() {
		return signosVitalesInstitucionCcostoAsocio;
	}


	/**
	 * @param signosVitalesInstitucionCcostoAsocio The signosVitalesInstitucionCcostoAsocio to set.
	 */
	public void setSignosVitalesInstitucionCcostoAsocio(
			Collection signosVitalesInstitucionCcostoAsocio) {
		this.signosVitalesInstitucionCcostoAsocio = signosVitalesInstitucionCcostoAsocio;
	}


	/**
	 * @return Returns the signosVitalesParamHisto.
	 */
	public Collection getSignosVitalesParamHisto() {
		return signosVitalesParamHisto;
	}


	/**
	 * @param signosVitalesParamHisto The signosVitalesParamHisto to set.
	 */
	public void setSignosVitalesParamHisto(Collection signosVitalesParamHisto) {
		this.signosVitalesParamHisto = signosVitalesParamHisto;
	}


	/**
	 * @return Returns the signosVitalesParamHistoAsocio.
	 */
	public Collection getSignosVitalesParamHistoAsocio() {
		return signosVitalesParamHistoAsocio;
	}


	/**
	 * @param signosVitalesParamHistoAsocio The signosVitalesParamHistoAsocio to set.
	 */
	public void setSignosVitalesParamHistoAsocio(
			Collection signosVitalesParamHistoAsocio) {
		this.signosVitalesParamHistoAsocio = signosVitalesParamHistoAsocio;
	}


	/**
	 * @return Returns the cateterSondaFijosHisto.
	 */
	public Collection getCateterSondaFijosHisto() {
		return cateterSondaFijosHisto;
	}


	/**
	 * @param cateterSondaFijosHisto The cateterSondaFijosHisto to set.
	 */
	public void setCateterSondaFijosHisto(Collection cateterSondaFijosHisto) {
		this.cateterSondaFijosHisto = cateterSondaFijosHisto;
	}


	/**
	 * @return Returns the cateterSondaFijosHistoAsocio.
	 */
	public Collection getCateterSondaFijosHistoAsocio() {
		return cateterSondaFijosHistoAsocio;
	}


	/**
	 * @param cateterSondaFijosHistoAsocio The cateterSondaFijosHistoAsocio to set.
	 */
	public void setCateterSondaFijosHistoAsocio(
			Collection cateterSondaFijosHistoAsocio) {
		this.cateterSondaFijosHistoAsocio = cateterSondaFijosHistoAsocio;
	}


	/**
	 * @return Returns the cateterSondaHistoTodos.
	 */
	public Collection getCateterSondaHistoTodos() {
		return cateterSondaHistoTodos;
	}


	/**
	 * @param cateterSondaHistoTodos The cateterSondaHistoTodos to set.
	 */
	public void setCateterSondaHistoTodos(Collection cateterSondaHistoTodos) {
		this.cateterSondaHistoTodos = cateterSondaHistoTodos;
	}


	/**
	 * @return Returns the cateterSondaHistoTodosAsocio.
	 */
	public Collection getCateterSondaHistoTodosAsocio() {
		return cateterSondaHistoTodosAsocio;
	}


	/**
	 * @param cateterSondaHistoTodosAsocio The cateterSondaHistoTodosAsocio to set.
	 */
	public void setCateterSondaHistoTodosAsocio(
			Collection cateterSondaHistoTodosAsocio) {
		this.cateterSondaHistoTodosAsocio = cateterSondaHistoTodosAsocio;
	}


	/**
	 * @return Returns the cateterSondaParamHisto.
	 */
	public Collection getCateterSondaParamHisto() {
		return cateterSondaParamHisto;
	}


	/**
	 * @param cateterSondaParamHisto The cateterSondaParamHisto to set.
	 */
	public void setCateterSondaParamHisto(Collection cateterSondaParamHisto) {
		this.cateterSondaParamHisto = cateterSondaParamHisto;
	}


	/**
	 * @return Returns the cateterSondaParamHistoAsocio.
	 */
	public Collection getCateterSondaParamHistoAsocio() {
		return cateterSondaParamHistoAsocio;
	}


	/**
	 * @param cateterSondaParamHistoAsocio The cateterSondaParamHistoAsocio to set.
	 */
	public void setCateterSondaParamHistoAsocio(
			Collection cateterSondaParamHistoAsocio) {
		this.cateterSondaParamHistoAsocio = cateterSondaParamHistoAsocio;
	}


	/**
	 * @return Returns the colCateteresSondaInstitucionCcosto.
	 */
	public Collection getColCateteresSondaInstitucionCcosto() {
		return colCateteresSondaInstitucionCcosto;
	}


	/**
	 * @param colCateteresSondaInstitucionCcosto The colCateteresSondaInstitucionCcosto to set.
	 */
	public void setColCateteresSondaInstitucionCcosto(
			Collection colCateteresSondaInstitucionCcosto) {
		this.colCateteresSondaInstitucionCcosto = colCateteresSondaInstitucionCcosto;
	}


	/**
	 * @return Returns the colCateteresSondaInstitucionCcostoAsocio.
	 */
	public Collection getColCateteresSondaInstitucionCcostoAsocio() {
		return colCateteresSondaInstitucionCcostoAsocio;
	}


	/**
	 * @param colCateteresSondaInstitucionCcostoAsocio The colCateteresSondaInstitucionCcostoAsocio to set.
	 */
	public void setColCateteresSondaInstitucionCcostoAsocio(
			Collection colCateteresSondaInstitucionCcostoAsocio) {
		this.colCateteresSondaInstitucionCcostoAsocio = colCateteresSondaInstitucionCcostoAsocio;
	}


	/**
	 * @return Returns the existeCateterSonda.
	 */
	public boolean isExisteCateterSonda() {
		return existeCateterSonda;
	}


	/**
	 * @param existeCateterSonda The existeCateterSonda to set.
	 */
	public void setExisteCateterSonda(boolean existeCateterSonda) {
		this.existeCateterSonda = existeCateterSonda;
	}


	/**
	 * @return Returns the existeCateterSondaAsocio.
	 */
	public boolean isExisteCateterSondaAsocio() {
		return existeCateterSondaAsocio;
	}


	/**
	 * @param existeCateterSondaAsocio The existeCateterSondaAsocio to set.
	 */
	public void setExisteCateterSondaAsocio(boolean existeCateterSondaAsocio) {
		this.existeCateterSondaAsocio = existeCateterSondaAsocio;
	}


	/**
	 * @return Returns the mapaHistoricoCateterSonda.
	 */
	public HashMap getMapaHistoricoCateterSonda() {
		return mapaHistoricoCateterSonda;
	}


	/**
	 * @param mapaHistoricoCateterSonda The mapaHistoricoCateterSonda to set.
	 */
	public void setMapaHistoricoCateterSonda(HashMap mapaHistoricoCateterSonda) {
		this.mapaHistoricoCateterSonda = mapaHistoricoCateterSonda;
	}
	
	/**
	 * @return Retorna un elemento del mapa mapaHistoricoCateterSonda.
	 */
	public Object getMapaHistoricoCateterSonda(String key) {
		return mapaHistoricoCateterSonda.get(key);
	}


	/**
	 * @param Asigna elemento al mapa mapaHistoricoCateterSonda.
	 */
	public void setMapaHistoricoCateterSonda(String key,Object obj) {
		this.mapaHistoricoCateterSonda.put(key,obj);
	}
	


	/**
	 * @return Returns the mapaHistoricoCateterSondaAsocio.
	 */
	public HashMap getMapaHistoricoCateterSondaAsocio() {
		return mapaHistoricoCateterSondaAsocio;
	}


	/**
	 * @param mapaHistoricoCateterSondaAsocio The mapaHistoricoCateterSondaAsocio to set.
	 */
	public void setMapaHistoricoCateterSondaAsocio(
			HashMap mapaHistoricoCateterSondaAsocio) {
		this.mapaHistoricoCateterSondaAsocio = mapaHistoricoCateterSondaAsocio;
	}
	
	/**
	 * @return Retorna un elemento del mapa mapaHistoricoCateterSondaAsocio.
	 */
	public Object getMapaHistoricoCateterSondaAsocio(String key) {
		return mapaHistoricoCateterSondaAsocio.get(key);
	}


	/**
	 * @param Asigna elemento al mapa mapaHistoricoCateterSondaAsocio.
	 */
	public void setMapaHistoricoCateterSondaAsocio(
			String key, Object obj) {
		this.mapaHistoricoCateterSondaAsocio.put(key,obj);
	}


	/**
	 * @return Returns the existeControlLiquidos.
	 */
	public boolean isExisteControlLiquidos() {
		return existeControlLiquidos;
	}


	/**
	 * @param existeControlLiquidos The existeControlLiquidos to set.
	 */
	public void setExisteControlLiquidos(boolean existeControlLiquidos) {
		this.existeControlLiquidos = existeControlLiquidos;
	}


	/**
	 * @return Returns the existeControlLiquidosAsocio.
	 */
	public boolean isExisteControlLiquidosAsocio() {
		return existeControlLiquidosAsocio;
	}


	/**
	 * @param existeControlLiquidosAsocio The existeControlLiquidosAsocio to set.
	 */
	public void setExisteControlLiquidosAsocio(boolean existeControlLiquidosAsocio) {
		this.existeControlLiquidosAsocio = existeControlLiquidosAsocio;
	}


	/**
	 * @return Returns the mapaControlLiq.
	 */
	public HashMap getMapaControlLiq() {
		return mapaControlLiq;
	}


	/**
	 * @param mapaControlLiq The mapaControlLiq to set.
	 */
	public void setMapaControlLiq(HashMap mapaControlLiq) {
		this.mapaControlLiq = mapaControlLiq;
	}
	
	/**
	 * @return Retorna un elemento del mapa mapaControlLiq.
	 */
	public Object getMapaControlLiq(String key) {
		return mapaControlLiq.get(key);
	}


	/**
	 * @param Asigna un elemento al mapa mapaControlLiq.
	 */
	public void setMapaControlLiq(String key, Object obj) {
		this.mapaControlLiq.put(key,obj);
	}
	


	/**
	 * @return Returns the mapaControlLiqAsocio.
	 */
	public HashMap getMapaControlLiqAsocio() {
		return mapaControlLiqAsocio;
	}


	/**
	 * @param mapaControlLiqAsocio The mapaControlLiqAsocio to set.
	 */
	public void setMapaControlLiqAsocio(HashMap mapaControlLiqAsocio) {
		this.mapaControlLiqAsocio = mapaControlLiqAsocio;
	}
	
	/**
	 * @return Retorna un elemento del mapa mapaControlLiqAsocio.
	 */
	public Object getMapaControlLiqAsocio(String key) {
		return mapaControlLiqAsocio.get(key);
	}


	/**
	 * @param Asigna un elemento al mapa mapaControlLiqAsocio.
	 */
	public void setMapaControlLiqAsocio(String key, Object obj) {
		this.mapaControlLiqAsocio.put(key,obj);
	}


	/**
	 * @return Returns the checkImpresion.
	 */
	public HashMap getCheckImpresion() {
		return checkImpresion;
	}


	/**
	 * @param checkImpresion The checkImpresion to set.
	 */
	public void setCheckImpresion(HashMap checkImpresion) {
		this.checkImpresion = checkImpresion;
	}
	
	/**
	 * @return Retorna un elemento del mapa checkImpresion.
	 */
	public Object getCheckImpresion(String key) {
		return checkImpresion.get(key);
	}


	/**
	 * @param Asigna un elemento al mapa checkImpresion.
	 */
	public void setCheckImpresion(String key,Object obj) {
		this.checkImpresion.put(key,obj);
	}
	
	public void chequearTodos()
	{
		for(int i=1;i<=34;i++)
		{
			this.setCheckImpresion("ck_"+i,"true");
			this.setCheckImpresion("ck_"+i+"A","true");
		}
		//NOTA* los eventos adversos van deschqueados por defecto
		this.setCheckImpresion("ck_20","false");
		this.setCheckImpresion("ck_20A","false");
		this.setCheckImpresion("ck_todos","true");
		
		/**
		* Tipo Modificacion: Segun incidencia 5055
		* Autor: Alejandro Aguirre Luna
		* usuario: aleagulu
		* Fecha: 12/02/2013
		* Descripcion: Se deben dejar seleccionar todos los checks al iniciar. 
		**/
		for(int k = 1; k <= 2; k++){
			//PARACLINICOS
			for(int p = 1; p < 2;p++){
				this.setCheckImpresion("ck_"+p+"_Paraclinicos_"+k, "true");
				this.setCheckImpresion("hd_"+p+"_Paraclinicos_"+k, "true");
			}
			//ENFERMERIA
			for(int p = 1; p <= 10;p++){
				this.setCheckImpresion("ck_"+p+"_Enfermeria_"+k, "true");
				this.setCheckImpresion("hd_"+p+"_Enfermeria_"+k, "true");
			}
			//ADMIN MEDICAMENTOS
			for(int p = 1; p <= 2;p++){
				this.setCheckImpresion("ck_"+p+"_AdminMedicamentos_"+k, "true");
				this.setCheckImpresion("hd_"+p+"_AdminMedicamentos_"+k, "true");
			}
			//OTROS
			for(int p = 1; p <= 2;p++){
				this.setCheckImpresion("ck_"+p+"_Otros_"+k, "true");
				this.setCheckImpresion("hd_"+p+"_Otros_"+k, "true");
			}
			
			//VALORACION, EVLOCUCION, INTERCONSULTA Y ORDENES
			for(int p = 1; p <= 6;p++){
				this.setCheckImpresion("ck_"+p+"_ValEvolInterOrd_"+k, "true");
				this.setCheckImpresion("hd_"+p+"_ValEvolInterOrd_"+k, "true");
			}
		}
	}

	/**
	 * @return Returns the mapaAntAlergia.
	 */
	public HashMap getMapaAntAlergia() {
		return mapaAntAlergia;
	}


	/**
	 * @param mapaAntAlergia The mapaAntAlergia to set.
	 */
	public void setMapaAntAlergia(HashMap mapaAntAlergia) {
		this.mapaAntAlergia = mapaAntAlergia;
	}
	
	/**
	 * @return Retorna un elemento del mapa mapaAntAlergia.
	 */
	public Object getMapaAntAlergia(String key) {
		return mapaAntAlergia.get(key);
	}


	/**
	 * @param Asigna un elemento al mapa mapaAntAlergia.
	 */
	public void setMapaAntAlergia(String key,Object obj) {
		this.mapaAntAlergia.put(key,obj);
	}
	
	/**
	 * @return Returns the mapaAntFamiliares.
	 */
	public HashMap getMapaAntFamiliares() {
		return mapaAntFamiliares;
	}


	/**
	 * @param mapaAntFamiliares The mapaAntFamiliares to set.
	 */
	public void setMapaAntFamiliares(HashMap mapaAntFamiliares) {
		this.mapaAntFamiliares = mapaAntFamiliares;
	}
	
	/**
	 * @return Retorna un elemento del mapa mapaAntFamiliares.
	 */
	public Object getMapaAntFamiliares(String key) {
		return mapaAntFamiliares.get(key);
	}


	/**
	 * @param Asigna un elemento al mapa mapaAntFamiliares.
	 */
	public void setMapaAntFamiliares(String key,Object obj) {
		this.mapaAntFamiliares.put(key,obj);
	}


	/**
	 * @return Returns the mapaAntFamOftal.
	 */
	public HashMap getMapaAntFamOftal() {
		return mapaAntFamOftal;
	}


	/**
	 * @param mapaAntFamOftal The mapaAntFamOftal to set.
	 */
	public void setMapaAntFamOftal(HashMap mapaAntFamOftal) {
		this.mapaAntFamOftal = mapaAntFamOftal;
	}
	
	/**
	 * @return Retorna un elemento del mapa mapaAntFamOftal.
	 */
	public Object getMapaAntFamOftal(String key) {
		return mapaAntFamOftal.get(key);
	}


	/**
	 * @param Asigna un elemento al mapa mapaAntFamOftal.
	 */
	public void setMapaAntFamOftal(String key,Object obj) {
		this.mapaAntFamOftal.put(key,obj);
	}


	/**
	 * @return Retorna elemento del mapa solicitudesInterInterpretadas.
	 */
	public Object getSolicitudesInterConsulta(String key) {
		return solicitudesInterConsulta.get(key);
	}


	/**
	 * @param Asigna un elemento al mapa solicitudesInterInterpretadas .
	 */
	public void setSolicitudesInterConsulta(String key,Object obj) {
		this.solicitudesInterConsulta.put(key,obj);
	}


	/**
	 * @return Retorna elemento del mapa solicitudesInterInterpretadasAsocio.
	 */
	public Object getSolicitudesInterConsultaAsocio(String key) {
		return solicitudesInterConsultaAsocio.get(key);
	}


	/**
	 * @param Asigna un elemento al mapa solicitudesInterInterpretadasAsocio .
	 */
	public void setSolicitudesInterConsultaAsocio(String key,Object obj) {
		this.solicitudesInterConsultaAsocio.put(key,obj);
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


	/**
	 * @return Retorna un elemento del mapa codigosEvoluciones.
	 */
	public Object getCodigosEvoluciones(String key) 
	{
		return codigosEvoluciones.get(key);
	}


	/**
	 * @param Asigna un elemento al mapa codigosEvoluciones.
	 */
	public void setCodigosEvoluciones(String key,Object obj) {
		this.codigosEvoluciones.put(key,obj);
	}
	
	

	/**
	 * @return Returns the codigosEvolucionesAsocio.
	 */
	public HashMap getCodigosEvolucionesAsocio() {
		return codigosEvolucionesAsocio;
	}


	/**
	 * @param codigosEvolucionesAsocio The codigosEvolucionesAsocio to set.
	 */
	public void setCodigosEvolucionesAsocio(HashMap codigosEvolucionesAsocio) {
		this.codigosEvolucionesAsocio = codigosEvolucionesAsocio;
	}


	/**
	 * @return Retorna elemento del mapa codigosEvolucionesAsocio.
	 */
	public Object getCodigosEvolucionesAsocio(String key) {
		return codigosEvolucionesAsocio.get(key);
	}


	/**
	 * @param Asigna elemento del mapa codigosEvolucionesAsocio.
	 */
	public void setCodigosEvolucionesAsocio(String key,Object obj) {
		this.codigosEvolucionesAsocio.put(key,obj);
	}


	/**
	 * @return Returns the solicitudesInterConsultaAsocio.
	 */
	public HashMap getSolicitudesInterConsultaAsocio() {
		return solicitudesInterConsultaAsocio;
	}


	/**
	 * @param solicitudesInterConsultaAsocio The solicitudesInterConsultaAsocio to set.
	 */
	public void setSolicitudesInterConsultaAsocio(
			HashMap solicitudesInterConsultaAsocio) {
		this.solicitudesInterConsultaAsocio = solicitudesInterConsultaAsocio;
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
	 * @return Returns the existenOrdenesMedicas.
	 */
	public boolean isExistenOrdenesMedicas() {
		return existenOrdenesMedicas;
	}


	/**
	 * @param existenOrdenesMedicas The existenOrdenesMedicas to set.
	 */
	public void setExistenOrdenesMedicas(boolean existenOrdenesMedicas) {
		this.existenOrdenesMedicas = existenOrdenesMedicas;
	}


	/**
	 * @return Returns the existenOrdenesMedicasAsocio.
	 */
	public boolean isExistenOrdenesMedicasAsocio() {
		return existenOrdenesMedicasAsocio;
	}


	/**
	 * @param existenOrdenesMedicasAsocio The existenOrdenesMedicasAsocio to set.
	 */
	public void setExistenOrdenesMedicasAsocio(boolean existenOrdenesMedicasAsocio) {
		this.existenOrdenesMedicasAsocio = existenOrdenesMedicasAsocio;
	}


	/**
	 * @return Returns the ordenesCirugia.
	 */
	public HashMap getOrdenesCirugia() {
		return ordenesCirugia;
	}


	/**
	 * @param ordenesCirugia The ordenesCirugia to set.
	 */
	public void setOrdenesCirugia(HashMap ordenesCirugia) {
		this.ordenesCirugia = ordenesCirugia;
	}
	
	/**
	 * @return Retorna elemento del mapa ordenesCirugia.
	 */
	public Object getOrdenesCirugia(String key) {
		return ordenesCirugia.get(key);
	}


	/**
	 * @param Asigna elemento al mapa ordenesCirugia.
	 */
	public void setOrdenesCirugia(String key,Object obj) {
		this.ordenesCirugia.put(key,obj);
	}


	/**
	 * @return Returns the ordenesCirugiaAsocio.
	 */
	public HashMap getOrdenesCirugiaAsocio() {
		return ordenesCirugiaAsocio;
	}


	/**
	 * @param ordenesCirugiaAsocio The ordenesCirugiaAsocio to set.
	 */
	public void setOrdenesCirugiaAsocio(HashMap ordenesCirugiaAsocio) {
		this.ordenesCirugiaAsocio = ordenesCirugiaAsocio;
	}


	/**
	 * @return Retorna elemento del mapa ordenesCirugiaAsocio.
	 */
	public Object getOrdenesCirugiaAsocio(String key) {
		return ordenesCirugiaAsocio.get(key);
	}


	/**
	 * @param Asigna elemento al mapa ordenesCirugiaAsocio.
	 */
	public void setOrdenesCirugiaAsocio(String key,Object obj) {
		this.ordenesCirugiaAsocio.put(key,obj);
	}


	/**
	 * @return Returns the ordenesInterconsultas.
	 */
	public HashMap getOrdenesInterconsultas() {
		return ordenesInterconsultas;
	}


	/**
	 * @param ordenesInterconsultas The ordenesInterconsultas to set.
	 */
	public void setOrdenesInterconsultas(HashMap ordenesInterconsultas) {
		this.ordenesInterconsultas = ordenesInterconsultas;
	}


	/**
	 * @return Retorna elemento del mapa ordenesInterconsultas.
	 */
	public Object getOrdenesInterconsultas(String key) {
		return ordenesInterconsultas.get(key);
	}


	/**
	 * @param Asigna elemento en el msps ordenesInterconsultas.
	 */
	public void setOrdenesInterconsultas(String key,Object obj) {
		this.ordenesInterconsultas.put(key,obj);
	}


	/**
	 * @return Returns the ordenesInterconsultasAsocio.
	 */
	public HashMap getOrdenesInterconsultasAsocio() {
		return ordenesInterconsultasAsocio;
	}


	/**
	 * @param ordenesInterconsultasAsocio The ordenesInterconsultasAsocio to set.
	 */
	public void setOrdenesInterconsultasAsocio(HashMap ordenesInterconsultasAsocio) {
		this.ordenesInterconsultasAsocio = ordenesInterconsultasAsocio;
	}


	/**
	 * @return Retorna elemento del mapa ordenesInterconsultasAsocio.
	 */
	public Object getOrdenesInterconsultasAsocio(String key) {
		return ordenesInterconsultasAsocio.get(key);
	}


	/**
	 * @param Asigna elemento al mapa ordenesInterconsultasAsocio.
	 */
	public void setOrdenesInterconsultasAsocio(String key,Object obj) {
		this.ordenesInterconsultasAsocio.put(key,obj);
	}


	/**
	 * @return Returns the ordenesMedicamentos.
	 */
	public HashMap getOrdenesMedicamentos() {
		return ordenesMedicamentos;
	}


	/**
	 * @param ordenesMedicamentos The ordenesMedicamentos to set.
	 */
	public void setOrdenesMedicamentos(HashMap ordenesMedicamentos) {
		this.ordenesMedicamentos = ordenesMedicamentos;
	}


	/**
	 * @return Retorna elemento del mapa ordenesMedicamentos.
	 */
	public Object getOrdenesMedicamentos(String key) {
		return ordenesMedicamentos.get(key);
	}


	/**
	 * @param Asigna elemento al mapa ordenesMedicamentos.
	 */
	public void setOrdenesMedicamentos(String key,Object obj) {
		this.ordenesMedicamentos.put(key,obj);
	}


	/**
	 * @return Returns the ordenesMedicamentosAsocio.
	 */
	public HashMap getOrdenesMedicamentosAsocio() {
		return ordenesMedicamentosAsocio;
	}


	/**
	 * @param ordenesMedicamentosAsocio The ordenesMedicamentosAsocio to set.
	 */
	public void setOrdenesMedicamentosAsocio(HashMap ordenesMedicamentosAsocio) {
		this.ordenesMedicamentosAsocio = ordenesMedicamentosAsocio;
	}


	/**
	 * @return Retorna elemento del mapaordenesMedicamentosAsocio.
	 */
	public Object getOrdenesMedicamentosAsocio(String key) {
		return ordenesMedicamentosAsocio.get(key);
	}


	/**
	 * @param Asigna elemento al mapa ordenesMedicamentosAsocio .
	 */
	public void setOrdenesMedicamentosAsocio(String key,Object obj) {
		this.ordenesMedicamentosAsocio.put(key,obj);
	}


	/**
	 * @return Returns the ordenesMedicas.
	 */
	public HashMap getOrdenesMedicas() {
		return ordenesMedicas;
	}


	/**
	 * @param ordenesMedicas The ordenesMedicas to set.
	 */
	public void setOrdenesMedicas(HashMap ordenesMedicas) {
		this.ordenesMedicas = ordenesMedicas;
	}


	/**
	 * @return Retorna elemento del mapa ordenesMedicas.
	 */
	public Object getOrdenesMedicas(String key) {
		return ordenesMedicas.get(key);
	}


	/**
	 * @param Asigna elemento al mapa ordenesMedicas.
	 */
	public void setOrdenesMedicas(String key,Object obj) {
		this.ordenesMedicas.put(key,obj);
	}


	/**
	 * @return Returns the ordenesMedicasAsocio.
	 */
	public HashMap getOrdenesMedicasAsocio() {
		return ordenesMedicasAsocio;
	}


	/**
	 * @param ordenesMedicasAsocio The ordenesMedicasAsocio to set.
	 */
	public void setOrdenesMedicasAsocio(HashMap ordenesMedicasAsocio) {
		this.ordenesMedicasAsocio = ordenesMedicasAsocio;
	}


	/**
	 * @return Retorna elemento del mapa ordenesMedicasAsocio.
	 */
	public Object getOrdenesMedicasAsocio(String key) {
		return ordenesMedicasAsocio.get(key);
	}


	/**
	 * @param Asigna elemento al mapa ordenesMedicasAsocio.
	 */
	public void setOrdenesMedicasAsocio(String key,Object obj) {
		this.ordenesMedicasAsocio.put(key,obj);
	}


	/**
	 * @return Returns the ordenesProcedimientos.
	 */
	public HashMap getOrdenesProcedimientos() {
		return ordenesProcedimientos;
	}


	/**
	 * @param ordenesProcedimientos The ordenesProcedimientos to set.
	 */
	public void setOrdenesProcedimientos(HashMap ordenesProcedimientos) {
		this.ordenesProcedimientos = ordenesProcedimientos;
	}


	/**
	 * @return Retorna elemento del mapa ordenesProcedimientos.
	 */
	public Object getOrdenesProcedimientos(String key) {
		return ordenesProcedimientos.get(key);
	}


	/**
	 * @param Asigna elemento al mapa ordenesProcedimientos.
	 */
	public void setOrdenesProcedimientos(String key,Object obj) {
		this.ordenesProcedimientos.put(key,obj);
	}


	/**
	 * @return Returns the ordenesProcedimientosAsocio.
	 */
	public HashMap getOrdenesProcedimientosAsocio() {
		return ordenesProcedimientosAsocio;
	}


	/**
	 * @param ordenesProcedimientosAsocio The ordenesProcedimientosAsocio to set.
	 */
	public void setOrdenesProcedimientosAsocio(HashMap ordenesProcedimientosAsocio) {
		this.ordenesProcedimientosAsocio = ordenesProcedimientosAsocio;
	}


	/**
	 * @return Retorna elemento del mapa ordenesProcedimientosAsocio.
	 */
	public Object getOrdenesProcedimientosAsocio(String key) {
		return ordenesProcedimientosAsocio.get(key);
	}


	/**
	 * @param Asigna elemento al mapa ordenesProcedimientosAsocio.
	 */
	public void setOrdenesProcedimientosAsocio(String key,Object obj) {
		this.ordenesProcedimientosAsocio.put(key,obj);
	}


	/**
	 * @return Returns the mapaAntPersoOftal.
	 */
	public HashMap getMapaAntPersoOftal() {
		return mapaAntPersoOftal;
	}


	/**
	 * @param mapaAntPersoOftal The mapaAntPersoOftal to set.
	 */
	public void setMapaAntPersoOftal(HashMap mapaAntPersoOftal) {
		this.mapaAntPersoOftal = mapaAntPersoOftal;
	}
	
	/**
	 * @return Retorna elemento del mapa mapaAntPersoOftal.
	 */
	public Object getMapaAntPersoOftal(String key) {
		return mapaAntPersoOftal.get(key);
	}


	/**
	 * @param Asigna elemento al mapa mapaAntPersoOftal.
	 */
	public void setMapaAntPersoOftal(String key,Object obj) {
		this.mapaAntPersoOftal.put(key,obj);
	}


	/**
	 * @return Returns the mapaAntGineco.
	 */
	public HashMap getMapaAntGineco() {
		return mapaAntGineco;
	}


	/**
	 * @param mapaAntGineco The mapaAntGineco to set.
	 */
	public void setMapaAntGineco(HashMap mapaAntGineco) {
		this.mapaAntGineco = mapaAntGineco;
	}
	
	/**
	 * @return Retorna elemento del mapa mapaAntGineco.
	 */
	public Object getMapaAntGineco(String key) {
		return mapaAntGineco.get(key);
	}


	/**
	 * @param Asigna elemento al maps mapaAntGineco.
	 */
	public void setMapaAntGineco(String key,Object obj) {
		this.mapaAntGineco.put(key,obj);
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
	 * @return Retorna elemento del mapa registroAccidentesTransitoReporte1.
	 */
	public Object getRegistroAccidentesTransitoReporte1(String key) {
		return registroAccidentesTransitoReporte1.get(key);
	}


	/**
	 * @param Asigna elemento al mapa registroAccidentesTransitoReporte1.
	 */
	public void setRegistroAccidentesTransitoReporte1(String key,Object obj) {
		this.registroAccidentesTransitoReporte1.put(key,obj);
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
	
	/**
	 * @return Retorna elemento al mapa registroAccidentesTransitoReporte2.
	 */
	public Object getRegistroAccidentesTransitoReporte2(String key) {
		return registroAccidentesTransitoReporte2.get(key);
	}


	/**
	 * @param Asigna elemento al mapa registroAccidentesTransitoReporte2.
	 */
	public void setRegistroAccidentesTransitoReporte2(String key,Object obj) {
		this.registroAccidentesTransitoReporte2.put(key,obj);
	}


	/**
	 * @return Returns the mapaAntMedicamento.
	 */
	public HashMap getMapaAntMedicamento() {
		return mapaAntMedicamento;
	}


	/**
	 * @param mapaAntMedicamento The mapaAntMedicamento to set.
	 */
	public void setMapaAntMedicamento(HashMap mapaAntMedicamento) {
		this.mapaAntMedicamento = mapaAntMedicamento;
	}
	
	/**
	 * @return Retorna elemento del mapa mapaAntMedicamento.
	 */
	public Object getMapaAntMedicamento(String key) {
		return mapaAntMedicamento.get(key);
	}


	/**
	 * @param Asigna elemento del mapa mapaAntMedicamento.
	 */
	public void setMapaAntMedicamento(String key,Object obj) {
		this.mapaAntMedicamento.put(key,obj);
	}


	/**
	 * @return Returns the existenAntecedentesMedicos.
	 */
	public boolean isExistenAntecedentesMedicos() {
		return existenAntecedentesMedicos;
	}


	/**
	 * @param existenAntecedentesMedicos The existenAntecedentesMedicos to set.
	 */
	public void setExistenAntecedentesMedicos(boolean existenAntecedentesMedicos) {
		this.existenAntecedentesMedicos = existenAntecedentesMedicos;
	}


	/**
	 * @return Returns the mapaAntMedicos.
	 */
	public HashMap getMapaAntMedicos() {
		return mapaAntMedicos;
	}


	/**
	 * @param mapaAntMedicos The mapaAntMedicos to set.
	 */
	public void setMapaAntMedicos(HashMap mapaAntMedicos) {
		this.mapaAntMedicos = mapaAntMedicos;
	}
	
	/**
	 * @return Retorna un elemento del mapa mapaAntMedicos.
	 */
	public Object getMapaAntMedicos(String key) {
		return mapaAntMedicos.get(key);
	}


	/**
	 * @param Asigna elemento al mapa mapaAntMedicos.
	 */
	public void setMapaAntMedicos(String key,Object obj) {
		this.mapaAntMedicos.put(key,obj);
	}


	/**
	 * @return Returns the mapaAntToxicos.
	 */
	public HashMap getMapaAntToxicos() {
		return mapaAntToxicos;
	}


	/**
	 * @param mapaAntToxicos The mapaAntToxicos to set.
	 */
	public void setMapaAntToxicos(HashMap mapaAntToxicos) {
		this.mapaAntToxicos = mapaAntToxicos;
	}
	
	/**
	 * @return Retorna elemento del mapa mapaAntToxicos.
	 */
	public Object getMapaAntToxicos(String key) {
		return mapaAntToxicos.get(key);
	}


	/**
	 * @param Asigna elemento al mapa mapaAntToxicos.
	 */
	public void setMapaAntToxicos(String key,Object obj) {
		this.mapaAntToxicos.put(key,obj);
	}


	/**
	 * @return Returns the existeSoporteRespiratorio.
	 */
	public boolean isExisteSoporteRespiratorio() {
		return existeSoporteRespiratorio;
	}


	/**
	 * @param existeSoporteRespiratorio The existeSoporteRespiratorio to set.
	 */
	public void setExisteSoporteRespiratorio(boolean existeSoporteRespiratorio) {
		this.existeSoporteRespiratorio = existeSoporteRespiratorio;
	}


	/**
	 * @return Returns the existeSoporteRespiratorioAsocio.
	 */
	public boolean isExisteSoporteRespiratorioAsocio() {
		return existeSoporteRespiratorioAsocio;
	}


	/**
	 * @param existeSoporteRespiratorioAsocio The existeSoporteRespiratorioAsocio to set.
	 */
	public void setExisteSoporteRespiratorioAsocio(
			boolean existeSoporteRespiratorioAsocio) {
		this.existeSoporteRespiratorioAsocio = existeSoporteRespiratorioAsocio;
	}


	/**
	 * @return Returns the soporteRespiratorio.
	 */
	public HashMap getSoporteRespiratorio() {
		return soporteRespiratorio;
	}


	/**
	 * @param soporteRespiratorio The soporteRespiratorio to set.
	 */
	public void setSoporteRespiratorio(HashMap soporteRespiratorio) {
		this.soporteRespiratorio = soporteRespiratorio;
	}
	
	/**
	 * @return Retorna el elemento del mapa soporteRespiratorio.
	 */
	public Object getSoporteRespiratorio(String key) {
		return soporteRespiratorio.get(key);
	}


	/**
	 * @param Asigna elemento del mapa soporteRespiratorio.
	 */
	public void setSoporteRespiratorio(String key,Object obj) {
		this.soporteRespiratorio.put(key,obj);
	}


	/**
	 * @return Returns the soporteRespiratorioAsocio.
	 */
	public HashMap getSoporteRespiratorioAsocio() {
		return soporteRespiratorioAsocio;
	}


	/**
	 * @param soporteRespiratorioAsocio The soporteRespiratorioAsocio to set.
	 */
	public void setSoporteRespiratorioAsocio(HashMap soporteRespiratorioAsocio) {
		this.soporteRespiratorioAsocio = soporteRespiratorioAsocio;
	}
	
	/**
	 * @return Retorna elemento del mapa soporteRespiratorioAsocio.
	 */
	public Object getSoporteRespiratorioAsocio(String key) {
		return soporteRespiratorioAsocio.get(key);
	}


	/**
	 * @param Asigna elemento al map soporteRespiratorioAsocio.
	 */
	public void setSoporteRespiratorioAsocio(String key,Object obj) {
		this.soporteRespiratorioAsocio.put(key,obj);
	}


	/**
	 * @return Returns the mapaAntTransfusionales.
	 */
	public HashMap getMapaAntTransfusionales() {
		return mapaAntTransfusionales;
	}


	/**
	 * @param mapaAntTransfusionales The mapaAntTransfusionales to set.
	 */
	public void setMapaAntTransfusionales(HashMap mapaAntTransfusionales) {
		this.mapaAntTransfusionales = mapaAntTransfusionales;
	}
	
	/**
	 * @return Retorna elemento del mapa mapaAntTransfusionales.
	 */
	public Object getMapaAntTransfusionales(String key) {
		return mapaAntTransfusionales.get(key);
	}


	/**
	 * @param Asigna elemento al mapa mapaAntTransfusionales.
	 */
	public void setMapaAntTransfusionales(String key,Object obj) {
		this.mapaAntTransfusionales.put(key,obj);
	}


	/**
	 * @return Returns the mapaTiposInmunizacion.
	 */
	public HashMap getMapaTiposInmunizacion() {
		return mapaTiposInmunizacion;
	}


	/**
	 * @param mapaTiposInmunizacion The mapaTiposInmunizacion to set.
	 */
	public void setMapaTiposInmunizacion(HashMap mapaTiposInmunizacion) {
		this.mapaTiposInmunizacion = mapaTiposInmunizacion;
	}
	
	/**
	 * @return Retorna elemento del mapa mapaTiposInmunizacion.
	 */
	public Object getMapaTiposInmunizacion(String key) {
		return mapaTiposInmunizacion.get(key);
	}


	/**
	 * @param Asigna elemento al mapa mapaTiposInmunizacion.
	 */
	public void setMapaTiposInmunizacion(String key,Object obj) {
		this.mapaTiposInmunizacion.put(key,obj);
	}


	/**
	 * @return Returns the mapaVacunas.
	 */
	public HashMap getMapaVacunas() {
		return mapaVacunas;
	}


	/**
	 * @param mapaVacunas The mapaVacunas to set.
	 */
	public void setMapaVacunas(HashMap mapaVacunas) {
		this.mapaVacunas = mapaVacunas;
	}
	
	/**
	 * @return Retorna elemento del mapa mapaVacunas.
	 */
	public Object getMapaVacunas(String key) {
		return mapaVacunas.get(key);
	}


	/**
	 * @param Asigna elemento al mapa mapaVacunas.
	 */
	public void setMapaVacunas(String key,Object obj) {
		this.mapaVacunas.put(key,obj);
	}


	/**
	 * @return Returns the mapaAntOtros.
	 */
	public HashMap getMapaAntOtros() {
		return mapaAntOtros;
	}


	/**
	 * @param mapaAntOtros The mapaAntOtros to set.
	 */
	public void setMapaAntOtros(HashMap mapaAntOtros) {
		this.mapaAntOtros = mapaAntOtros;
	}
	
	/**
	 * @return Retorna el elemento del mapa mapaAntOtros.
	 */
	public Object getMapaAntOtros(String key) {
		return mapaAntOtros.get(key);
	}


	/**
	 * @param Asigna elemento al mapa mapaAntOtros.
	 */
	public void setMapaAntOtros(String key,Object obj) {
		this.mapaAntOtros.put(key,obj);
	}


	/**
	 * @return Returns the existeCuidadosEspeciales.
	 */
	public boolean isExisteCuidadosEspeciales() {
		return existeCuidadosEspeciales;
	}


	/**
	 * @param existeCuidadosEspeciales The existeCuidadosEspeciales to set.
	 */
	public void setExisteCuidadosEspeciales(boolean existeCuidadosEspeciales) {
		this.existeCuidadosEspeciales = existeCuidadosEspeciales;
	}


	/**
	 * @return Returns the existeCuidadosEspecialesAsocio.
	 */
	public boolean isExisteCuidadosEspecialesAsocio() {
		return existeCuidadosEspecialesAsocio;
	}


	/**
	 * @param existeCuidadosEspecialesAsocio The existeCuidadosEspecialesAsocio to set.
	 */
	public void setExisteCuidadosEspecialesAsocio(
			boolean existeCuidadosEspecialesAsocio) {
		this.existeCuidadosEspecialesAsocio = existeCuidadosEspecialesAsocio;
	}


	/**
	 * @return Returns the mapaHistoricoCuidadosEspeciales.
	 */
	public HashMap getMapaHistoricoCuidadosEspeciales() {
		return mapaHistoricoCuidadosEspeciales;
	}


	/**
	 * @param mapaHistoricoCuidadosEspeciales The mapaHistoricoCuidadosEspeciales to set.
	 */
	public void setMapaHistoricoCuidadosEspeciales(
			HashMap mapaHistoricoCuidadosEspeciales) {
		this.mapaHistoricoCuidadosEspeciales = mapaHistoricoCuidadosEspeciales;
	}
	
	/**
	 * @return Retorna un elemento del mapa mapaHistoricoCuidadosEspeciales.
	 */
	public Object getMapaHistoricoCuidadosEspeciales(String key) {
		return mapaHistoricoCuidadosEspeciales.get(key);
	}


	/**
	 * @param Asigna un elemento al mapa mapaHistoricoCuidadosEspeciales.
	 */
	public void setMapaHistoricoCuidadosEspeciales(String key,Object obj) {
		this.mapaHistoricoCuidadosEspeciales.put(key,obj);
	}


	/**
	 * @return Returns the mapaHistoricoCuidadosEspecialesAsocio.
	 */
	public HashMap getMapaHistoricoCuidadosEspecialesAsocio() {
		return mapaHistoricoCuidadosEspecialesAsocio;
	}


	/**
	 * @param mapaHistoricoCuidadosEspecialesAsocio The mapaHistoricoCuidadosEspecialesAsocio to set.
	 */
	public void setMapaHistoricoCuidadosEspecialesAsocio(
			HashMap mapaHistoricoCuidadosEspecialesAsocio) {
		this.mapaHistoricoCuidadosEspecialesAsocio = mapaHistoricoCuidadosEspecialesAsocio;
	}
	
	/**
	 * @return Retorna un elemento del mapa mapaHistoricoCuidadosEspecialesAsocio.
	 */
	public Object getMapaHistoricoCuidadosEspecialesAsocio(String key) {
		return mapaHistoricoCuidadosEspecialesAsocio.get(key);
	}


	/**
	 * @param Asigna un elemento al mapa mapaHistoricoCuidadosEspecialesAsocio.
	 */
	public void setMapaHistoricoCuidadosEspecialesAsocio(String key,Object obj) {
		this.mapaHistoricoCuidadosEspecialesAsocio.put(key,obj);
	}


	/**
	 * @return Returns the escalasGlasglowInstitucionCCosto.
	 */
	public Collection getEscalasGlasgowInstitucionCCosto() {
		return escalasGlasgowInstitucionCCosto;
	}


	/**
	 * @param escalasGlasglowInstitucionCCosto The escalasGlasglowInstitucionCCosto to set.
	 */
	public void setEscalasGlasgowInstitucionCCosto(
			Collection escalasGlasglowInstitucionCCosto) {
		this.escalasGlasgowInstitucionCCosto = escalasGlasglowInstitucionCCosto;
	}


	/**
	 * @return Returns the escalasGlasglowInstitucionCCostoAsocio.
	 */
	public Collection getEscalasGlasgowInstitucionCCostoAsocio() {
		return escalasGlasgowInstitucionCCostoAsocio;
	}


	/**
	 * @param escalasGlasglowInstitucionCCostoAsocio The escalasGlasglowInstitucionCCostoAsocio to set.
	 */
	public void setEscalasGlasgowInstitucionCCostoAsocio(
			Collection escalasGlasglowInstitucionCCostoAsocio) {
		this.escalasGlasgowInstitucionCCostoAsocio = escalasGlasglowInstitucionCCostoAsocio;
	}


	/**
	 * @return Returns the existeHojaNeurologica.
	 */
	public boolean isExisteHojaNeurologica() {
		return existeHojaNeurologica;
	}


	/**
	 * @param existeHojaNeurologica The existeHojaNeurologica to set.
	 */
	public void setExisteHojaNeurologica(boolean existeHojaNeurologica) {
		this.existeHojaNeurologica = existeHojaNeurologica;
	}


	/**
	 * @return Returns the existeHojaNeurologicaAsocio.
	 */
	public boolean isExisteHojaNeurologicaAsocio() {
		return existeHojaNeurologicaAsocio;
	}


	/**
	 * @param existeHojaNeurologicaAsocio The existeHojaNeurologicaAsocio to set.
	 */
	public void setExisteHojaNeurologicaAsocio(boolean existeHojaNeurologicaAsocio) {
		this.existeHojaNeurologicaAsocio = existeHojaNeurologicaAsocio;
	}


	/**
	 * @return Returns the mapaHistoricoControlEsfinteres.
	 */
	public HashMap getMapaHistoricoControlEsfinteres() {
		return mapaHistoricoControlEsfinteres;
	}


	/**
	 * @param mapaHistoricoControlEsfinteres The mapaHistoricoControlEsfinteres to set.
	 */
	public void setMapaHistoricoControlEsfinteres(
			HashMap mapaHistoricoControlEsfinteres) {
		this.mapaHistoricoControlEsfinteres = mapaHistoricoControlEsfinteres;
	}
	
	/**
	 * @return Retorna un elemento del mapa mapaHistoricoControlEsfinteres.
	 */
	public Object getMapaHistoricoControlEsfinteres(String key) {
		return mapaHistoricoControlEsfinteres.get(key);
	}


	/**
	 * @param Asigna un elemento al mapa mapaHistoricoControlEsfinteres.
	 */
	public void setMapaHistoricoControlEsfinteres(String key,Object obj) {
		this.mapaHistoricoControlEsfinteres.put(key,obj);
	}
	


	/**
	 * @return Returns the mapaHistoricoControlEsfinteresAsocio.
	 */
	public HashMap getMapaHistoricoControlEsfinteresAsocio() {
		return mapaHistoricoControlEsfinteresAsocio;
	}


	/**
	 * @param mapaHistoricoControlEsfinteresAsocio The mapaHistoricoControlEsfinteresAsocio to set.
	 */
	public void setMapaHistoricoControlEsfinteresAsocio(
			HashMap mapaHistoricoControlEsfinteresAsocio) {
		this.mapaHistoricoControlEsfinteresAsocio = mapaHistoricoControlEsfinteresAsocio;
	}
	
	/**
	 * @return Retorna un elemento del mapa mapaHistoricoControlEsfinteresAsocio.
	 */
	public Object getMapaHistoricoControlEsfinteresAsocio(String key) {
		return mapaHistoricoControlEsfinteresAsocio.get(key);
	}


	/**
	 * @param Asigna un elemento al mapa mapaHistoricoControlEsfinteresAsocio.
	 */
	public void setMapaHistoricoControlEsfinteresAsocio(String key,Object obj) {
		this.mapaHistoricoControlEsfinteresAsocio.put(key,obj);
	}
	

	/**
	 * @return Returns the mapaHistoricoConvulsiones.
	 */
	public HashMap getMapaHistoricoConvulsiones() {
		return mapaHistoricoConvulsiones;
	}


	/**
	 * @param mapaHistoricoConvulsiones The mapaHistoricoConvulsiones to set.
	 */
	public void setMapaHistoricoConvulsiones(HashMap mapaHistoricoConvulsiones) {
		this.mapaHistoricoConvulsiones = mapaHistoricoConvulsiones;
	}
	
	
	/**
	 * @return Retorna un elemento del mapa mapaHistoricoConvulsiones.
	 */
	public Object getMapaHistoricoConvulsiones(String key) {
		return mapaHistoricoConvulsiones.get(key);
	}


	/**
	 * @param Asigna un elemento al mapa mapaHistoricoConvulsiones.
	 */
	public void setMapaHistoricoConvulsiones(String key,Object obj) {
		this.mapaHistoricoConvulsiones.put(key,obj);
	}


	/**
	 * @return Returns the mapaHistoricoConvulsionesAsocio.
	 */
	public HashMap getMapaHistoricoConvulsionesAsocio() {
		return mapaHistoricoConvulsionesAsocio;
	}


	/**
	 * @param mapaHistoricoConvulsionesAsocio The mapaHistoricoConvulsionesAsocio to set.
	 */
	public void setMapaHistoricoConvulsionesAsocio(
			HashMap mapaHistoricoConvulsionesAsocio) {
		this.mapaHistoricoConvulsionesAsocio = mapaHistoricoConvulsionesAsocio;
	}
	
	/**
	 * @return Retorna un elemento del mapa mapaHistoricoConvulsionesAsocio.
	 */
	public Object getMapaHistoricoConvulsionesAsocio(String key) {
		return mapaHistoricoConvulsionesAsocio.get(key);
	}


	/**
	 * @param Asigna un elemento al mapaHistoricoConvulsionesAsocio.
	 */
	public void setMapaHistoricoConvulsionesAsocio(String key,Object obj) {
		this.mapaHistoricoConvulsionesAsocio.put(key, obj);
	}


	/**
	 * @return Returns the mapaHistoricoEscalaGlasgow.
	 */
	public HashMap getMapaHistoricoEscalaGlasgow() {
		return mapaHistoricoEscalaGlasgow;
	}


	/**
	 * @param mapaHistoricoEscalaGlasgow The mapaHistoricoEscalaGlasgow to set.
	 */
	public void setMapaHistoricoEscalaGlasgow(HashMap mapaHistoricoEscalaGlasgow) {
		this.mapaHistoricoEscalaGlasgow = mapaHistoricoEscalaGlasgow;
	}
	
	/**
	 * @return Retorna un elemento del mapa mapaHistoricoEscalaGlasgow.
	 */
	public Object getMapaHistoricoEscalaGlasgow(String key) {
		return mapaHistoricoEscalaGlasgow.get(key);
	}


	/**
	 * @param Asigna un elemento al mapa mapaHistoricoEscalaGlasgow.
	 */
	public void setMapaHistoricoEscalaGlasgow(String key,Object obj) {
		this.mapaHistoricoEscalaGlasgow.put(key,obj);
	}


	/**
	 * @return Returns the mapaHistoricoEscalaGlasgowAsocio.
	 */
	public HashMap getMapaHistoricoEscalaGlasgowAsocio() {
		return mapaHistoricoEscalaGlasgowAsocio;
	}


	/**
	 * @param mapaHistoricoEscalaGlasgowAsocio The mapaHistoricoEscalaGlasgowAsocio to set.
	 */
	public void setMapaHistoricoEscalaGlasgowAsocio(
			HashMap mapaHistoricoEscalaGlasgowAsocio) {
		this.mapaHistoricoEscalaGlasgowAsocio = mapaHistoricoEscalaGlasgowAsocio;
	}
	
	/**
	 * @return Retorna un elemento del mapa mapaHistoricoEscalaGlasgowAsocio.
	 */
	public Object getMapaHistoricoEscalaGlasgowAsocio(String key) {
		return mapaHistoricoEscalaGlasgowAsocio.get(key);
	}


	/**
	 * @param Asigna un elemento al mapa mapaHistoricoEscalaGlasgowAsocio The mapaHistoricoEscalaGlasgowAsocio.
	 */
	public void setMapaHistoricoEscalaGlasgowAsocio(String key,Object obj) {
		this.mapaHistoricoEscalaGlasgowAsocio.put(key,obj);
	}


	/**
	 * @return Retorna un elemento del mapa mapaHistoricoFuerzaMuscular.
	 */
	public Object getMapaHistoricoFuerzaMuscular(String key) {
		return mapaHistoricoFuerzaMuscular.get(key);
	}


	/**
	 * @param Asigna un elemento al mapa mapaHistoricoFuerzaMuscular.
	 */
	public void setMapaHistoricoFuerzaMuscular(String key,Object obj) {
		this.mapaHistoricoFuerzaMuscular.put(key,obj);
	}
	/**
	 * @return Returns the mapaHistoricoFuerzaMuscular.
	 */
	public HashMap getMapaHistoricoFuerzaMuscular() {
		return mapaHistoricoFuerzaMuscular;
	}


	/**
	 * @param mapaHistoricoFuerzaMuscular The mapaHistoricoFuerzaMuscular to set.
	 */
	public void setMapaHistoricoFuerzaMuscular(HashMap mapaHistoricoFuerzaMuscular) {
		this.mapaHistoricoFuerzaMuscular = mapaHistoricoFuerzaMuscular;
	}
	


	/**
	 * @return Returns the mapaHistoricoFuerzaMuscularAsocio.
	 */
	public HashMap getMapaHistoricoFuerzaMuscularAsocio() {
		return mapaHistoricoFuerzaMuscularAsocio;
	}


	/**
	 * @param mapaHistoricoFuerzaMuscularAsocio The mapaHistoricoFuerzaMuscularAsocio to set.
	 */
	public void setMapaHistoricoFuerzaMuscularAsocio(
			HashMap mapaHistoricoFuerzaMuscularAsocio) {
		this.mapaHistoricoFuerzaMuscularAsocio = mapaHistoricoFuerzaMuscularAsocio;
	}
	
	/**
	 * @return Retorna un elemento del mapa mapaHistoricoFuerzaMuscularAsocio.
	 */
	public Object getMapaHistoricoFuerzaMuscularAsocio(String key) {
		return mapaHistoricoFuerzaMuscularAsocio.get(key);
	}


	/**
	 * @param Asigna un elemento al mapa mapaHistoricoFuerzaMuscularAsocio.
	 */
	public void setMapaHistoricoFuerzaMuscularAsocio(String key,Object obj) {
		this.mapaHistoricoFuerzaMuscularAsocio.put(key,obj);
	}


	/**
	 * @return Returns the mapaHistoricoPupilas.
	 */
	public HashMap getMapaHistoricoPupilas() {
		return mapaHistoricoPupilas;
	}


	/**
	 * @param mapaHistoricoPupilas The mapaHistoricoPupilas to set.
	 */
	public void setMapaHistoricoPupilas(HashMap mapaHistoricoPupilas) {
		this.mapaHistoricoPupilas = mapaHistoricoPupilas;
	}
	/**
	 * @return Retorna un elemento del mapa mapaHistoricoPupilas.
	 */
	public Object getMapaHistoricoPupilas(String key) {
		return mapaHistoricoPupilas.get(key);
	}


	/**
	 * @param Asigna un elemento al mapa mapaHistoricoPupilas.
	 */
	public void setMapaHistoricoPupilas(String key,Object obj) {
		this.mapaHistoricoPupilas.put(key,obj);
	}


	/**
	 * @return Returns the mapaHistoricoPupilasAsocio.
	 */
	public HashMap getMapaHistoricoPupilasAsocio() {
		return mapaHistoricoPupilasAsocio;
	}


	/**
	 * @param mapaHistoricoPupilasAsocio The mapaHistoricoPupilasAsocio to set.
	 */
	public void setMapaHistoricoPupilasAsocio(HashMap mapaHistoricoPupilasAsocio) {
		this.mapaHistoricoPupilasAsocio = mapaHistoricoPupilasAsocio;
	}
	/**
	 * @return Retorna un elemento del mapa mapaHistoricoPupilasAsocio.
	 */
	public Object getMapaHistoricoPupilasAsocio(String key) {
		return mapaHistoricoPupilasAsocio.get(key);
	}


	/**
	 * @param Asigna un elemento al mapa mapaHistoricoPupilasAsocio The mapaHistoricoPupilasAsocio.
	 */
	public void setMapaHistoricoPupilasAsocio(String key,Object obj) {
		this.mapaHistoricoPupilasAsocio.put(key,obj);
	}


	/**
	 * @return Returns the antPed.
	 */
	public AntecedentePediatrico getAntPed() {
		return antPed;
	}


	/**
	 * @param antPed The antPed to set.
	 */
	public void setAntPed(AntecedentePediatrico antPed) {
		this.antPed = antPed;
	}


	/**
	 * @return Returns the mapaAntPediatricos.
	 */
	public HashMap getMapaAntPediatricos() {
		return mapaAntPediatricos;
	}


	/**
	 * @param mapaAntPediatricos The mapaAntPediatricos to set.
	 */
	public void setMapaAntPediatricos(HashMap mapaAntPediatricos) {
		this.mapaAntPediatricos = mapaAntPediatricos;
	}
	
	/**
	 * @return Retorna un elemento del mapa mapaAntPediatricos.
	 */
	public Object getMapaAntPediatricos(String key) {
		return mapaAntPediatricos.get(key);
	}


	/**
	 * @param Asigna elemento al mapa mapaAntPediatricos.
	 */
	public void setMapaAntPediatricos(String key,Object obj) {
		this.mapaAntPediatricos.put(key,obj);
	}




	

	/**
	 * @return Returns the tiposParto.
	 */
	public HashMap getTiposParto() {
		return tiposParto;
	}


	/**
	 * @param tiposParto The tiposParto to set.
	 */
	public void setTiposParto(HashMap tiposParto) {
		this.tiposParto = tiposParto;
	}
	
	/**
	 * @return Retorna elemento del mapa tiposParto.
	 */
	public Object getTiposParto(String key) {
		return tiposParto.get(key);
	}


	/**
	 * @param Asigna elemento al mapa tiposParto.
	 */
	public void setTiposParto(String key,Object obj) {
		this.tiposParto.put(key,obj);
	}


	/**
	 * @return Returns the motivosTiposParto.
	 */
	public HashMap getMotivosTiposParto() {
		return motivosTiposParto;
	}


	/**
	 * @param motivosTiposParto The motivosTiposParto to set.
	 */
	public void setMotivosTiposParto(HashMap motivosTiposParto) {
		this.motivosTiposParto = motivosTiposParto;
	}
	
	/**
	 * @return Retorna elemento del mapa motivosTiposParto.
	 */
	public Object getMotivosTiposParto(String key) {
		return motivosTiposParto.get(key);
	}


	/**
	 * @param Asigna elemento del mapa motivosTiposParto.
	 */
	public void setMotivosTiposParto(String key,Object obj) {
		this.motivosTiposParto.put(key,obj);
	}

	/**
	 * @return Returns the tiposPartoList.
	 */
	public ArrayList getTiposPartoList() {
		return tiposPartoList;
	}


	/**
	 * @param tiposPartoList The tiposPartoList to set.
	 */
	public void setTiposPartoList(ArrayList tiposPartoList) {
		this.tiposPartoList = tiposPartoList;
	}


	/**
	 * @return Returns the motivosTiposPartoCarga.
	 */
	public HashMap getMotivosTiposPartoCarga() {
		return motivosTiposPartoCarga;
	}


	/**
	 * @param motivosTiposPartoCarga The motivosTiposPartoCarga to set.
	 */
	public void setMotivosTiposPartoCarga(HashMap motivosTiposPartoCarga) {
		this.motivosTiposPartoCarga = motivosTiposPartoCarga;
	}
	
	/**
	 * @return Retorna elemento del mapa motivosTiposPartoCarga.
	 */
	public Object getMotivosTiposPartoCarga(String key) {
		return motivosTiposPartoCarga.get(key);
	}


	/**
	 * @param Asigna elemento al mapa motivosTiposPartoCarga.
	 */
	public void setMotivosTiposPartoCarga(String key,Object obj) {
		this.motivosTiposPartoCarga.put(key,obj);
	}


	/**
	 * @return Returns the tiposPartoCarga.
	 */
	public HashMap getTiposPartoCarga() {
		return tiposPartoCarga;
	}


	/**
	 * @param tiposPartoCarga The tiposPartoCarga to set.
	 */
	public void setTiposPartoCarga(HashMap tiposPartoCarga) {
		this.tiposPartoCarga = tiposPartoCarga;
	}
	
	/**
	 * @return Retorna elemento del mapa tiposPartoCarga.
	 */
	public Object getTiposPartoCarga(String key) {
		return tiposPartoCarga.get(key);
	}
	
	public ArrayList getEmbarazoOpcionCampos()
	{
		return categoriaEmbarazoOpcionCampo;
	}

	public InfoDatos getEmbarazoOpcionCampo(int ai_i)
	{
		return (InfoDatos)categoriaEmbarazoOpcionCampo.get(ai_i);
	}

	/**
	 * @param Asigna elemento al mapa tiposPartoCarga.
	 */
	public void setTiposPartoCarga(String key,Object obj) {
		this.tiposPartoCarga.put(key,obj);
	}


	/**
	 * @return Returns the mapaAntOdonto.
	 */
	public HashMap getMapaAntOdonto() {
		return mapaAntOdonto;
	}


	/**
	 * @param mapaAntOdonto The mapaAntOdonto to set.
	 */
	public void setMapaAntOdonto(HashMap mapaAntOdonto) {
		this.mapaAntOdonto = mapaAntOdonto;
	}
	
	/**
	 * @return Retorna elemento del mapa mapaAntOdonto.
	 */
	public Object getMapaAntOdonto(String key) {
		return mapaAntOdonto.get(key);
	}


	/**
	 * @param Asigna elemento al mapa mapaAntOdonto.
	 */
	public void setMapaAntOdonto(String key,Object obj) {
		this.mapaAntOdonto.put(key,obj);
	}


	/**
	 * @return Returns the listadoSignosVitales.
	 */
	public Collection getListadoSignosVitales() {
		return listadoSignosVitales;
	}


	/**
	 * @param listadoSignosVitales The listadoSignosVitales to set.
	 */
	public void setListadoSignosVitales(Collection listadoSignosVitales) {
		this.listadoSignosVitales = listadoSignosVitales;
	}


	/**
	 * @return Returns the listadoSignosVitalesAsocio.
	 */
	public Collection getListadoSignosVitalesAsocio() {
		return listadoSignosVitalesAsocio;
	}


	/**
	 * @param listadoSignosVitalesAsocio The listadoSignosVitalesAsocio to set.
	 */
	public void setListadoSignosVitalesAsocio(Collection listadoSignosVitalesAsocio) {
		this.listadoSignosVitalesAsocio = listadoSignosVitalesAsocio;
	}


	/**
	 * @return Returns the listadoTecAnestesiaGral.
	 */
	public Collection getListadoTecAnestesiaGral() {
		return listadoTecAnestesiaGral;
	}


	/**
	 * @param listadoTecAnestesiaGral The listadoTecAnestesiaGral to set.
	 */
	public void setListadoTecAnestesiaGral(Collection listadoTecAnestesiaGral) {
		this.listadoTecAnestesiaGral = listadoTecAnestesiaGral;
	}


	/**
	 * @return Returns the listadoTecAnestesiaGralAsocio.
	 */
	public Collection getListadoTecAnestesiaGralAsocio() {
		return listadoTecAnestesiaGralAsocio;
	}


	/**
	 * @param listadoTecAnestesiaGralAsocio The listadoTecAnestesiaGralAsocio to set.
	 */
	public void setListadoTecAnestesiaGralAsocio(
			Collection listadoTecAnestesiaGralAsocio) {
		this.listadoTecAnestesiaGralAsocio = listadoTecAnestesiaGralAsocio;
	}


	/**
	 * @return Returns the listadoTecAnestesiaOpcionesGral.
	 */
	public Collection getListadoTecAnestesiaOpcionesGral() {
		return listadoTecAnestesiaOpcionesGral;
	}


	/**
	 * @param listadoTecAnestesiaOpcionesGral The listadoTecAnestesiaOpcionesGral to set.
	 */
	public void setListadoTecAnestesiaOpcionesGral(
			Collection listadoTecAnestesiaOpcionesGral) {
		this.listadoTecAnestesiaOpcionesGral = listadoTecAnestesiaOpcionesGral;
	}


	/**
	 * @return Returns the listadoTecAnestesiaOpcionesGralAsocio.
	 */
	public Collection getListadoTecAnestesiaOpcionesGralAsocio() {
		return listadoTecAnestesiaOpcionesGralAsocio;
	}


	/**
	 * @param listadoTecAnestesiaOpcionesGralAsocio The listadoTecAnestesiaOpcionesGralAsocio to set.
	 */
	public void setListadoTecAnestesiaOpcionesGralAsocio(
			Collection listadoTecAnestesiaOpcionesGralAsocio) {
		this.listadoTecAnestesiaOpcionesGralAsocio = listadoTecAnestesiaOpcionesGralAsocio;
	}


	/**
	 * @return Returns the listadoTecAnestesiaRegional.
	 */
	public Collection getListadoTecAnestesiaRegional() {
		return listadoTecAnestesiaRegional;
	}


	/**
	 * @param listadoTecAnestesiaRegional The listadoTecAnestesiaRegional to set.
	 */
	public void setListadoTecAnestesiaRegional(
			Collection listadoTecAnestesiaRegional) {
		this.listadoTecAnestesiaRegional = listadoTecAnestesiaRegional;
	}


	/**
	 * @return Returns the listadoTecAnestesiaRegionalAsocio.
	 */
	public Collection getListadoTecAnestesiaRegionalAsocio() {
		return listadoTecAnestesiaRegionalAsocio;
	}


	/**
	 * @param listadoTecAnestesiaRegionalAsocio The listadoTecAnestesiaRegionalAsocio to set.
	 */
	public void setListadoTecAnestesiaRegionalAsocio(
			Collection listadoTecAnestesiaRegionalAsocio) {
		this.listadoTecAnestesiaRegionalAsocio = listadoTecAnestesiaRegionalAsocio;
	}

	//------------------------------------------------------------------
	/**
	 * @return Returns the mapaCodigosPeticionCirugia.
	 */
	public HashMap getMapaCodigosPeticionCirugia() {
		return mapaCodigosPeticionCirugia;
	}


	/**
	 * @param mapaCodigosPeticionCirugia The mapaCodigosPeticionCirugia to set.
	 */
	public void setMapaCodigosPeticionCirugia(HashMap mapaCodigosPeticionCirugia) {
		this.mapaCodigosPeticionCirugia = mapaCodigosPeticionCirugia;
	}
	/**
	 * @return Retorna elemento del mapa mapaCodigosPeticionCirugia.
	 */
	public Object getMapaCodigosPeticionCirugia(String key) {
		return mapaCodigosPeticionCirugia.get(key);
	}


	/**
	 * @param Asigna elemento al mapa mapaCodigosPeticionCirugia.
	 */
	public void setMapaCodigosPeticionCirugia(String key,Object obj) {
		this.mapaCodigosPeticionCirugia.put(key,obj);
	}
	
	


	/**
	 * @return Returns the mapaCodigosPeticionCirugiaAsocio.
	 */
	public HashMap getMapaCodigosPeticionCirugiaAsocio() {
		return mapaCodigosPeticionCirugiaAsocio;
	}
	/**
	 * @param mapaCodigosPeticionCirugiaAsocio The mapaCodigosPeticionCirugiaAsocio to set.
	 */
	public void setMapaCodigosPeticionCirugiaAsocio(
			HashMap mapaCodigosPeticionCirugiaAsocio) {
		this.mapaCodigosPeticionCirugiaAsocio = mapaCodigosPeticionCirugiaAsocio;
	}
	/**
	 * @return Retorna elemento del mapa mapaCodigosPeticionCirugiaAsocio.
	 */
	public Object getMapaCodigosPeticionCirugiaAsocio(String key) {
		return mapaCodigosPeticionCirugiaAsocio.get(key);
	}
	/**
	 * @param Asigna elemento al mapa mapaCodigosPeticionCirugiaAsocio.
	 */
	public void setMapaCodigosPeticionCirugiaAsocio(String key,Object obj) {
		this.mapaCodigosPeticionCirugiaAsocio.put(key,obj);
	}
	


	/**
	 * @return Returns the mapaEncabezadosHojaAnestesia.
	 */
	public HashMap getMapaEncabezadosHojaAnestesia() {
		return mapaEncabezadosHojaAnestesia;
	}


	/**
	 * @param mapaEncabezadosHojaAnestesia The mapaEncabezadosHojaAnestesia to set.
	 */
	public void setMapaEncabezadosHojaAnestesia(HashMap mapaEncabezadosHojaAnestesia) {
		this.mapaEncabezadosHojaAnestesia = mapaEncabezadosHojaAnestesia;
	}
	/**
	 * @return Retorna elemento del mapa mapaEncabezadosHojaAnestesia.
	 */
	public Object getMapaEncabezadosHojaAnestesia(String key) {
		return mapaEncabezadosHojaAnestesia.get(key);
	}


	/**
	 * @param Asigna elemento al mapa mapaEncabezadosHojaAnestesia.
	 */
	public void setMapaEncabezadosHojaAnestesia(String key,Object obj) {
		this.mapaEncabezadosHojaAnestesia.put(key,obj);
	}
	


	/**
	 * @return Returns the mapaEncabezadosHojaAnestesiaAsocio.
	 */
	public HashMap getMapaEncabezadosHojaAnestesiaAsocio() {
		return mapaEncabezadosHojaAnestesiaAsocio;
	}
	/**
	 * @param mapaEncabezadosHojaAnestesiaAsocio The mapaEncabezadosHojaAnestesiaAsocio to set.
	 */
	public void setMapaEncabezadosHojaAnestesiaAsocio(
			HashMap mapaEncabezadosHojaAnestesiaAsocio) {
		this.mapaEncabezadosHojaAnestesiaAsocio = mapaEncabezadosHojaAnestesiaAsocio;
	}
	/**
	 * @return Retorna elemento del mapa mapaEncabezadosHojaAnestesiaAsocio.
	 */
	public Object getMapaEncabezadosHojaAnestesiaAsocio(String key) {
		return mapaEncabezadosHojaAnestesiaAsocio.get(key);
	}
	/**
	 * @param Asigna elemento al mapa mapaEncabezadosHojaAnestesiaAsocio.
	 */
	public void setMapaEncabezadosHojaAnestesiaAsocio(String key,Object obj) {
		this.mapaEncabezadosHojaAnestesiaAsocio.put(key,obj);
	}
	


	/**
	 * @return Returns the mapaExamenesLaboratorioPreanestesia.
	 */
	public HashMap getMapaExamenesLaboratorioPreanestesia() {
		return mapaExamenesLaboratorioPreanestesia;
	}
	/**
	 * @param mapaExamenesLaboratorioPreanestesia The mapaExamenesLaboratorioPreanestesia to set.
	 */
	public void setMapaExamenesLaboratorioPreanestesia(
			HashMap mapaExamenesLaboratorioPreanestesia) {
		this.mapaExamenesLaboratorioPreanestesia = mapaExamenesLaboratorioPreanestesia;
	}
	/**
	 * @return Retorna elemento del mapa mapaExamenesLaboratorioPreanestesia.
	 */
	public Object getMapaExamenesLaboratorioPreanestesia(String key) {
		return mapaExamenesLaboratorioPreanestesia.get(key);
	}
	/**
	 * @param Asigna elemento al mapa mapaExamenesLaboratorioPreanestesia.
	 */
	public void setMapaExamenesLaboratorioPreanestesia(String key,Object obj) {
		this.mapaExamenesLaboratorioPreanestesia.put(key,obj);
	}
	


	/**
	 * @return Returns the mapaExamenesLaboratorioPreanestesiaAsocio.
	 */
	public HashMap getMapaExamenesLaboratorioPreanestesiaAsocio() {
		return mapaExamenesLaboratorioPreanestesiaAsocio;
	}
	/**
	 * @param mapaExamenesLaboratorioPreanestesiaAsocio The mapaExamenesLaboratorioPreanestesiaAsocio to set.
	 */
	public void setMapaExamenesLaboratorioPreanestesiaAsocio(
			HashMap mapaExamenesLaboratorioPreanestesiaAsocio) {
		this.mapaExamenesLaboratorioPreanestesiaAsocio = mapaExamenesLaboratorioPreanestesiaAsocio;
	}
	/**
	 * @return Retorna un elemento del mapa mapaExamenesLaboratorioPreanestesiaAsocio.
	 */
	public Object getMapaExamenesLaboratorioPreanestesiaAsocio(String key) {
		return mapaExamenesLaboratorioPreanestesiaAsocio.get(key);
	}
	/**
	 * @param Asigna elemento al mapa mapaExamenesLaboratorioPreanestesiaAsocio.
	 */
	public void setMapaExamenesLaboratorioPreanestesiaAsocio(String key,Object obj) {
		this.mapaExamenesLaboratorioPreanestesiaAsocio.put(key,obj);
	}


	/**
	 * @return Returns the mapaHistoBalanceLiquidosHojaAnestesia.
	 */
	public HashMap getMapaHistoBalanceLiquidosHojaAnestesia() {
		return mapaHistoBalanceLiquidosHojaAnestesia;
	}
	/**
	 * @param mapaHistoBalanceLiquidosHojaAnestesia The mapaHistoBalanceLiquidosHojaAnestesia to set.
	 */
	public void setMapaHistoBalanceLiquidosHojaAnestesia(
			HashMap mapaHistoBalanceLiquidosHojaAnestesia) {
		this.mapaHistoBalanceLiquidosHojaAnestesia = mapaHistoBalanceLiquidosHojaAnestesia;
	}
	/**
	 * @return Retorna un elemento del mapa mapaHistoBalanceLiquidosHojaAnestesia.
	 */
	public Object getMapaHistoBalanceLiquidosHojaAnestesia(String key) {
		return mapaHistoBalanceLiquidosHojaAnestesia.get(key);
	}
	/**
	 * @param Asigna elemento al mapa mapaHistoBalanceLiquidosHojaAnestesia.
	 */
	public void setMapaHistoBalanceLiquidosHojaAnestesia(String key,Object obj) {
		this.mapaHistoBalanceLiquidosHojaAnestesia.put(key,obj);
	}


	/**
	 * @return Returns the mapaHistoBalanceLiquidosHojaAnestesiaAsocio.
	 */
	public HashMap getMapaHistoBalanceLiquidosHojaAnestesiaAsocio() {
		return mapaHistoBalanceLiquidosHojaAnestesiaAsocio;
	}
	/**
	 * @param mapaHistoBalanceLiquidosHojaAnestesiaAsocio The mapaHistoBalanceLiquidosHojaAnestesiaAsocio to set.
	 */
	public void setMapaHistoBalanceLiquidosHojaAnestesiaAsocio(
			HashMap mapaHistoBalanceLiquidosHojaAnestesiaAsocio) {
		this.mapaHistoBalanceLiquidosHojaAnestesiaAsocio = mapaHistoBalanceLiquidosHojaAnestesiaAsocio;
	}
	/**
	 * @return Retorna elemento al mapa mapaHistoBalanceLiquidosHojaAnestesiaAsocio.
	 */
	public Object getMapaHistoBalanceLiquidosHojaAnestesiaAsocio(String key) {
		return mapaHistoBalanceLiquidosHojaAnestesiaAsocio.get(key);
	}
	/**
	 * @param Asigna elemento al mapa mapaHistoBalanceLiquidosHojaAnestesiaAsocio.
	 */
	public void setMapaHistoBalanceLiquidosHojaAnestesiaAsocio(String key,Object obj) {
		this.mapaHistoBalanceLiquidosHojaAnestesiaAsocio.put(key,obj);
	}
	


	/**
	 * @return Returns the mapaHistoConclusiones.
	 */
	public HashMap getMapaHistoConclusiones() {
		return mapaHistoConclusiones;
	}
	/**
	 * @param mapaHistoConclusiones The mapaHistoConclusiones to set.
	 */
	public void setMapaHistoConclusiones(HashMap mapaHistoConclusiones) {
		this.mapaHistoConclusiones = mapaHistoConclusiones;
	}
	/**
	 * @return Retorna elemento del mapa mapaHistoConclusiones.
	 */
	public Object getMapaHistoConclusiones(String key) {
		return mapaHistoConclusiones.get(key);
	}
	/**
	 * @param Asigna elemento al mapa mapaHistoConclusiones.
	 */
	public void setMapaHistoConclusiones(String key,Object obj) {
		this.mapaHistoConclusiones.put(key,obj);
	}


	/**
	 * @return Returns the mapaHistoConclusionesAsocio.
	 */
	public HashMap getMapaHistoConclusionesAsocio() {
		return mapaHistoConclusionesAsocio;
	}
	/**
	 * @param mapaHistoConclusionesAsocio The mapaHistoConclusionesAsocio to set.
	 */
	public void setMapaHistoConclusionesAsocio(HashMap mapaHistoConclusionesAsocio) {
		this.mapaHistoConclusionesAsocio = mapaHistoConclusionesAsocio;
	}
	/**
	 * @return Retorna elemento del mapa mapaHistoConclusionesAsocio.
	 */
	public Object getMapaHistoConclusionesAsocio(String key) {
		return mapaHistoConclusionesAsocio.get(key);
	}
	/**
	 * @param Asigna elemento al mapa mapaHistoConclusionesAsocio.
	 */
	public void setMapaHistoConclusionesAsocio(String key,Object obj) {
		this.mapaHistoConclusionesAsocio.put(key,obj);
	}
	


	/**
	 * @return Returns the mapaHistoExamenesFisicosText.
	 */
	public HashMap getMapaHistoExamenesFisicosText() {
		return mapaHistoExamenesFisicosText;
	}
	/**
	 * @param mapaHistoExamenesFisicosText The mapaHistoExamenesFisicosText to set.
	 */
	public void setMapaHistoExamenesFisicosText(HashMap mapaHistoExamenesFisicosText) {
		this.mapaHistoExamenesFisicosText = mapaHistoExamenesFisicosText;
	}
	/**
	 * @return Retorna elemento del mapa mapaHistoExamenesFisicosText.
	 */
	public Object getMapaHistoExamenesFisicosText(String key) {
		return mapaHistoExamenesFisicosText.get(key);
	}
	/**
	 * @param Asigna elemento al mapa mapaHistoExamenesFisicosText.
	 */
	public void setMapaHistoExamenesFisicosText(String key,Object obj) {
		this.mapaHistoExamenesFisicosText.put(key,obj);
	}


	/**
	 * @return Returns the mapaHistoExamenesFisicosTextArea.
	 */
	public HashMap getMapaHistoExamenesFisicosTextArea() {
		return mapaHistoExamenesFisicosTextArea;
	}
	/**
	 * @param mapaHistoExamenesFisicosTextArea The mapaHistoExamenesFisicosTextArea to set.
	 */
	public void setMapaHistoExamenesFisicosTextArea(
			HashMap mapaHistoExamenesFisicosTextArea) {
		this.mapaHistoExamenesFisicosTextArea = mapaHistoExamenesFisicosTextArea;
	}
	/**
	 * @return Retorna elemento del mapa mapaHistoExamenesFisicosTextArea.
	 */
	public Object getMapaHistoExamenesFisicosTextArea(String key) {
		return mapaHistoExamenesFisicosTextArea.get(key);
	}
	/**
	 * @param Asigna elemento al mapa mapaHistoExamenesFisicosTextArea.
	 */
	public void setMapaHistoExamenesFisicosTextArea(String key,Object obj) {
		this.mapaHistoExamenesFisicosTextArea.put(key,obj);
	}
	


	/**
	 * @return Returns the mapaHistoExamenesFisicosTextAreaAsocio.
	 */
	public HashMap getMapaHistoExamenesFisicosTextAreaAsocio() {
		return mapaHistoExamenesFisicosTextAreaAsocio;
	}
	/**
	 * @param mapaHistoExamenesFisicosTextAreaAsocio The mapaHistoExamenesFisicosTextAreaAsocio to set.
	 */
	public void setMapaHistoExamenesFisicosTextAreaAsocio(
			HashMap mapaHistoExamenesFisicosTextAreaAsocio) {
		this.mapaHistoExamenesFisicosTextAreaAsocio = mapaHistoExamenesFisicosTextAreaAsocio;
	}
	/**
	 * @return Retorna elemento del mapa mapaHistoExamenesFisicosTextAreaAsocio.
	 */
	public Object getMapaHistoExamenesFisicosTextAreaAsocio(String key) {
		return mapaHistoExamenesFisicosTextAreaAsocio.get(key);
	}
	/**
	 * @param Asigna elemento al mapa mapaHistoExamenesFisicosTextAreaAsocio.
	 */
	public void setMapaHistoExamenesFisicosTextAreaAsocio(String key,Object obj) {
		this.mapaHistoExamenesFisicosTextAreaAsocio.put(key,obj);
	}


	/**
	 * @return Returns the mapaHistoExamenesFisicosTextAsocio.
	 */
	public HashMap getMapaHistoExamenesFisicosTextAsocio() {
		return mapaHistoExamenesFisicosTextAsocio;
	}
	/**
	 * @param mapaHistoExamenesFisicosTextAsocio The mapaHistoExamenesFisicosTextAsocio to set.
	 */
	public void setMapaHistoExamenesFisicosTextAsocio(
			HashMap mapaHistoExamenesFisicosTextAsocio) {
		this.mapaHistoExamenesFisicosTextAsocio = mapaHistoExamenesFisicosTextAsocio;
	}
	/**
	 * @return Retorna elemento del mapa mapaHistoExamenesFisicosTextAsocio.
	 */
	public Object getMapaHistoExamenesFisicosTextAsocio(String key) {
		return mapaHistoExamenesFisicosTextAsocio.get(key);
	}
	/**
	 * @param Asigna elemento al mapa mapaHistoExamenesFisicosTextAsocio.
	 */
	public void setMapaHistoExamenesFisicosTextAsocio(String key,Object obj) {
		this.mapaHistoExamenesFisicosTextAsocio.put(key,obj);
	}


	/**
	 * @return Returns the mapaHistoMedicamentosHojaAnestesia.
	 */
	public HashMap getMapaHistoMedicamentosHojaAnestesia() {
		return mapaHistoMedicamentosHojaAnestesia;
	}
	/**
	 * @param mapaHistoMedicamentosHojaAnestesia The mapaHistoMedicamentosHojaAnestesia to set.
	 */
	public void setMapaHistoMedicamentosHojaAnestesia(
			HashMap mapaHistoMedicamentosHojaAnestesia) {
		this.mapaHistoMedicamentosHojaAnestesia = mapaHistoMedicamentosHojaAnestesia;
	}
	/**
	 * @return Retorna elemento del mapa mapaHistoMedicamentosHojaAnestesia.
	 */
	public Object getMapaHistoMedicamentosHojaAnestesia(String key) {
		return mapaHistoMedicamentosHojaAnestesia.get(key);
	}
	/**
	 * @param Asigna elemento al mapa mapaHistoMedicamentosHojaAnestesia.
	 */
	public void setMapaHistoMedicamentosHojaAnestesia(String key,Object obj) {
		this.mapaHistoMedicamentosHojaAnestesia.put(key,obj);
	}
	


	/**
	 * @return Returns the mapaHistoMedicamentosHojaAnestesiaAsocio.
	 */
	public HashMap getMapaHistoMedicamentosHojaAnestesiaAsocio() {
		return mapaHistoMedicamentosHojaAnestesiaAsocio;
	}
	/**
	 * @param mapaHistoMedicamentosHojaAnestesiaAsocio The mapaHistoMedicamentosHojaAnestesiaAsocio to set.
	 */
	public void setMapaHistoMedicamentosHojaAnestesiaAsocio(
			HashMap mapaHistoMedicamentosHojaAnestesiaAsocio) {
		this.mapaHistoMedicamentosHojaAnestesiaAsocio = mapaHistoMedicamentosHojaAnestesiaAsocio;
	}
	/**
	 * @return Retorna elemento del mapa mapaHistoMedicamentosHojaAnestesiaAsocio.
	 */
	public Object getMapaHistoMedicamentosHojaAnestesiaAsocio(String key) {
		return mapaHistoMedicamentosHojaAnestesiaAsocio.get(key);
	}
	/**
	 * @param Asigna elemento al mapa mapaHistoMedicamentosHojaAnestesiaAsocio.
	 */
	public void setMapaHistoMedicamentosHojaAnestesiaAsocio(String key,Object obj) {
		this.mapaHistoMedicamentosHojaAnestesiaAsocio.put(key,obj);
	}


	/**
	 * @return Returns the mapaHistoSignosVitales.
	 */
	public HashMap getMapaHistoSignosVitales() {
		return mapaHistoSignosVitales;
	}
	/**
	 * @param mapaHistoSignosVitales The mapaHistoSignosVitales to set.
	 */
	public void setMapaHistoSignosVitales(HashMap mapaHistoSignosVitales) {
		this.mapaHistoSignosVitales = mapaHistoSignosVitales;
	}
	/**
	 * @return Retorna elemento del mapa mapaHistoSignosVitales.
	 */
	public Object getMapaHistoSignosVitales(String key) {
		return mapaHistoSignosVitales.get(key);
	}
	/**
	 * @param Asigna elemento al mapa mapaHistoSignosVitales.
	 */
	public void setMapaHistoSignosVitales(String key,Object obj) {
		this.mapaHistoSignosVitales.put(key,obj);
	}
	


	/**
	 * @return Returns the mapaHistoSignosVitalesAsocio.
	 */
	public HashMap getMapaHistoSignosVitalesAsocio() {
		return mapaHistoSignosVitalesAsocio;
	}
	/**
	 * @param mapaHistoSignosVitalesAsocio The mapaHistoSignosVitalesAsocio to set.
	 */
	public void setMapaHistoSignosVitalesAsocio(HashMap mapaHistoSignosVitalesAsocio) {
		this.mapaHistoSignosVitalesAsocio = mapaHistoSignosVitalesAsocio;
	}
	/**
	 * @return Retorna elemento del mapa mapaHistoSignosVitalesAsocio.
	 */
	public Object getMapaHistoSignosVitalesAsocio(String key) {
		return mapaHistoSignosVitalesAsocio.get(key);
	}
	/**
	 * @param Asigna elemento al mapa mapaHistoSignosVitalesAsocio.
	 */
	public void setMapaHistoSignosVitalesAsocio(String key,Object obj) {
		this.mapaHistoSignosVitalesAsocio.put(key,obj);
	}


	/**
	 * @return Returns the mapaHistoTecAnestesia.
	 */
	public HashMap getMapaHistoTecAnestesia() {
		return mapaHistoTecAnestesia;
	}
	/**
	 * @param mapaHistoTecAnestesia The mapaHistoTecAnestesia to set.
	 */
	public void setMapaHistoTecAnestesia(HashMap mapaHistoTecAnestesia) {
		this.mapaHistoTecAnestesia = mapaHistoTecAnestesia;
	}
	/**
	 * @return Retorna elemento del mapa mapaHistoTecAnestesia.
	 */
	public Object getMapaHistoTecAnestesia(String key) {
		return mapaHistoTecAnestesia.get(key);
	}
	/**
	 * @param Asigna elemento al mapa mapaHistoTecAnestesia.
	 */
	public void setMapaHistoTecAnestesia(String key,Object obj) {
		this.mapaHistoTecAnestesia.put(key,obj);
	}

	/**
	 * @return Returns the mapaHistoTecAnestesiaAsocio.
	 */
	public HashMap getMapaHistoTecAnestesiaAsocio() {
		return mapaHistoTecAnestesiaAsocio;
	}


	/**
	 * @param mapaHistoTecAnestesiaAsocio The mapaHistoTecAnestesiaAsocio to set.
	 */
	public void setMapaHistoTecAnestesiaAsocio(HashMap mapaHistoTecAnestesiaAsocio) {
		this.mapaHistoTecAnestesiaAsocio = mapaHistoTecAnestesiaAsocio;
	}
	/**
	 * @return Retorna elemento del mapa mapaHistoTecAnestesiaAsocio.
	 */
	public Object getMapaHistoTecAnestesiaAsocio(String key) {
		return mapaHistoTecAnestesiaAsocio.get(key);
	}
	/**
	 * @param Asigna elemento al mala mapaHistoTecAnestesiaAsociot.
	 */
	public void setMapaHistoTecAnestesiaAsocio(String key,Object obj) {
		this.mapaHistoTecAnestesiaAsocio.put(key,obj);
	}


	/**
	 * @return Returns the mapaHojaQuirur.
	 */
	public HashMap getMapaHojaQuirur() {
		return mapaHojaQuirur;
	}
	/**
	 * @param mapaHojaQuirur The mapaHojaQuirur to set.
	 */
	public void setMapaHojaQuirur(HashMap mapaHojaQuirur) {
		this.mapaHojaQuirur = mapaHojaQuirur;
	}
	/**
	 * @return Retorna elemento del mapa mapaHojaQuirur.
	 */
	public Object getMapaHojaQuirur(String key) {
		return mapaHojaQuirur.get(key);
	}
	/**
	 * @param Asigna elemento al mapa mapaHojaQuirur .
	 */
	public void setMapaHojaQuirur(String key,Object obj) {
		this.mapaHojaQuirur.put(key,obj);
	}


	/**
	 * @return Returns the mapaHojaQuirurAsocio.
	 */
	public HashMap getMapaHojaQuirurAsocio() {
		return mapaHojaQuirurAsocio;
	}
	/**
	 * @param mapaHojaQuirurAsocio The mapaHojaQuirurAsocio to set.
	 */
	public void setMapaHojaQuirurAsocio(HashMap mapaHojaQuirurAsocio) {
		this.mapaHojaQuirurAsocio = mapaHojaQuirurAsocio;
	}
	/**
	 * @return Retorna elemento del mapa mapaHojaQuirurAsocio.
	 */
	public Object getMapaHojaQuirurAsocio(String key) {
		return mapaHojaQuirurAsocio.get(key);
	}
	/**
	 * @param Asigna elemento al mapa mapaHojaQuirurAsocio.
	 */
	public void setMapaHojaQuirurAsocio(String key,Object obj) {
		this.mapaHojaQuirurAsocio.put(key,obj);
	}


	public boolean isEsPopupAntecedentes()
	{
		return esPopupAntecedentes;
	}


	public void setEsPopupAntecedentes(boolean esPopupAntecedentes)
	{
		this.esPopupAntecedentes = esPopupAntecedentes;
	}


	/**
	 * @return the existenCitas
	 */
	public boolean isExistenCitas() {
		return existenCitas;
	}


	/**
	 * @param existenCitas the existenCitas to set
	 */
	public void setExistenCitas(boolean existenCitas) {
		this.existenCitas = existenCitas;
	}


	/**
	 * @return the numCitas
	 */
	public int getNumCitas() {
		return numCitas;
	}


	/**
	 * @param numCitas the numCitas to set
	 */
	public void setNumCitas(int numCitas) {
		this.numCitas = numCitas;
	}


	/**
	 * @param citas the citas to set
	 */
	public void setCitas(HashMap citas) {
		this.citas = citas;
	}


	/**
	 * @return the citas
	 */
	public HashMap getCitas() {
		return citas;
	}
	
	/**
	 * @param Asigna un elemento al mapa citas 
	 */
	public void setCitas(String key,Object obj) {
		this.citas.put(key,obj);
	}


	/**
	 * @return Retorna un elemento del mapa  citas
	 */
	public Object getCitas(String key) {
		return citas.get(key);
	}


	/**
	 * @return the posArticulo
	 */
	public int getPosArticulo() {
		return posArticulo;
	}


	/**
	 * @param posArticulo the posArticulo to set
	 */
	public void setPosArticulo(int posArticulo) {
		this.posArticulo = posArticulo;
	}


	/**
	 * @return the detalleArticuloAdmin
	 */
	public HashMap getDetalleArticuloAdmin() {
		return detalleArticuloAdmin;
	}


	/**
	 * @param detalleArticuloAdmin the detalleArticuloAdmin to set
	 */
	public void setDetalleArticuloAdmin(HashMap detalleArticuloAdmin) {
		this.detalleArticuloAdmin = detalleArticuloAdmin;
	}
	
	/**
	 * @return Elemento del mapa detalleArticuloAdmin
	 */
	public Object getDetalleArticuloAdmin(String key) {
		return detalleArticuloAdmin.get(key);
	}


	/**
	 * @param Asigna elemento al mapa detalleArticuloAdmin 
	 */
	public void setDetalleArticuloAdmin(String key,Object obj) {
		this.detalleArticuloAdmin.put(key,obj);
	}
	
	/**
	 * @return the detalleArticuloAdminE
	 */
	public HashMap getDetalleArticuloAdminE() {
		return detalleArticuloAdminE;
	}


	/**
	 * @param detalleArticuloAdminE the detalleArticuloAdminE to set
	 */
	public void setDetalleArticuloAdminE(HashMap detalleArticuloAdminE) {
		this.detalleArticuloAdminE = detalleArticuloAdminE;
	}
	
	/**
	 * @return Elemento del mapa detalleArticuloAdminE
	 */
	public Object getDetalleArticuloAdminE(String key) {
		return detalleArticuloAdminE.get(key);
	}


	/**
	 * @param Asigna elemento al mapa detalleArticuloAdminE 
	 */
	public void setDetalleArticuloAdminE(String key,Object obj) {
		this.detalleArticuloAdminE.put(key,obj);
	}


	/**
	 * @return the detalleTriage
	 */
	public HashMap getDetalleTriage() {
		return detalleTriage;
	}


	/**
	 * @param detalleTriage the detalleTriage to set
	 */
	public void setDetalleTriage(HashMap detalleTriage) {
		this.detalleTriage = detalleTriage;
	}
	
	/**
	 * @return Elemento del mapa detalleTriage
	 */
	public Object getDetalleTriage(String key) {
		return detalleTriage.get(key);
	}


	/**
	 * @param Asigna elemento al mapa detalleTriage
	 */
	public void setDetalleTriage(String key,Object obj) {
		this.detalleTriage.put(key,obj);
	}


	/**
	 * @return the existeTriage
	 */
	public boolean isExisteTriage() {
		return existeTriage;
	}


	/**
	 * @param existeTriage the existeTriage to set
	 */
	public void setExisteTriage(boolean existeTriage) {
		this.existeTriage = existeTriage;
	}


	/**
	 * @return the listadoTriage
	 */
	public HashMap getListadoTriage() {
		return listadoTriage;
	}


	/**
	 * @param listadoTriage the listadoTriage to set
	 */
	public void setListadoTriage(HashMap listadoTriage) {
		this.listadoTriage = listadoTriage;
	}


	/**
	 * @return Retorna elemento del mapa listadoTriage
	 */
	public Object getListadoTriage(String key) {
		return listadoTriage.get(key);
	}


	/**
	 * @param Asigna elemento al mapa listadoTriage
	 */
	public void setListadoTriage(String key,Object obj) {
		this.listadoTriage.put(key,obj);
	}


	/**
	 * @return the numTriage
	 */
	public int getNumTriage() {
		return numTriage;
	}


	/**
	 * @param numTriage the numTriage to set
	 */
	public void setNumTriage(int numTriage) {
		this.numTriage = numTriage;
	}


	/**
	 * @return the consecutivoTriage
	 */
	public String getConsecutivoTriage() {
		return consecutivoTriage;
	}


	/**
	 * @param consecutivoTriage the consecutivoTriage to set
	 */
	public void setConsecutivoTriage(String consecutivoTriage) {
		this.consecutivoTriage = consecutivoTriage;
	}


	public boolean isEventoCatastrofico()
	{
		return eventoCatastrofico;
	}


	public void setEventoCatastrofico(boolean eventoCatastrofico)
	{
		this.eventoCatastrofico = eventoCatastrofico;
	}


	/**
	 * @return the especificarTipoReferencia
	 */
	public boolean isEspecificarTipoReferencia() {
		return especificarTipoReferencia;
	}


	/**
	 * @param especificarTipoReferencia the especificarTipoReferencia to set
	 */
	public void setEspecificarTipoReferencia(boolean especificarTipoReferencia) {
		this.especificarTipoReferencia = especificarTipoReferencia;
	}


	/**
	 * @return the existeContrarreferencia
	 */
	public boolean isExisteContrarreferencia() {
		return existeContrarreferencia;
	}


	/**
	 * @param existeContrarreferencia the existeContrarreferencia to set
	 */
	public void setExisteContrarreferencia(boolean existeContrarreferencia) {
		this.existeContrarreferencia = existeContrarreferencia;
	}


	/**
	 * @return the existeReferenciaExterna
	 */
	public boolean isExisteReferenciaExterna() {
		return existeReferenciaExterna;
	}


	/**
	 * @param existeReferenciaExterna the existeReferenciaExterna to set
	 */
	public void setExisteReferenciaExterna(boolean existeReferenciaExterna) {
		this.existeReferenciaExterna = existeReferenciaExterna;
	}


	/**
	 * @return the existeReferenciaInterna
	 */
	public boolean isExisteReferenciaInterna() {
		return existeReferenciaInterna;
	}


	/**
	 * @param existeReferenciaInterna the existeReferenciaInterna to set
	 */
	public void setExisteReferenciaInterna(boolean existeReferenciaInterna) {
		this.existeReferenciaInterna = existeReferenciaInterna;
	}


	/**
	 * @return the numeroReferenciaExterna
	 */
	public String getNumeroReferenciaExterna() {
		return numeroReferenciaExterna;
	}


	/**
	 * @param numeroReferenciaExterna the numeroReferenciaExterna to set
	 */
	public void setNumeroReferenciaExterna(String numeroReferenciaExterna) {
		this.numeroReferenciaExterna = numeroReferenciaExterna;
	}


	/**
	 * @return the numeroReferenciaInterna
	 */
	public String getNumeroReferenciaInterna() {
		return numeroReferenciaInterna;
	}


	/**
	 * @param numeroReferenciaInterna the numeroReferenciaInterna to set
	 */
	public void setNumeroReferenciaInterna(String numeroReferenciaInterna) {
		this.numeroReferenciaInterna = numeroReferenciaInterna;
	}


	/**
	 * @return the historialConsentimientoInfMap
	 */
	public HashMap getHistorialConsentimientoInfMap() {
		return historialConsentimientoInfMap;
	}


	/**
	 * @param historialConsentimientoInfMap the historialConsentimientoInfMap to set
	 */
	public void setHistorialConsentimientoInfMap(
			HashMap historialConsentimientoInfMap) {
		this.historialConsentimientoInfMap = historialConsentimientoInfMap;
	}
	
	/**
	 * @return the historialConsentimientoInfMap
	 */
	public Object getHistorialConsentimientoInfMap(String key) {
		return historialConsentimientoInfMap.get(key);
	}


	/**
	 * @param historialConsentimientoInfMap the historialConsentimientoInfMap to set
	 */
	public void setHistorialConsentimientoInfMap(String key, Object value) {
		this.historialConsentimientoInfMap.put(key, value);
	}


	/**
	 * @return the existeHistorialConsentimiento
	 */
	public String getExisteHistorialConsentimiento() {
		return existeHistorialConsentimiento;
	}


	/**
	 * @param existeHistorialConsentimiento the existeHistorialConsentimiento to set
	 */
	public void setExisteHistorialConsentimiento(
			String existeHistorialConsentimiento) {
		this.existeHistorialConsentimiento = existeHistorialConsentimiento;
	}


	/**
	 * @return the detalleCita
	 */
	public HashMap getDetalleCita() {
		return detalleCita;
	}


	/**
	 * @param detalleCita the detalleCita to set
	 */
	public void setDetalleCita(HashMap detalleCita) {
		this.detalleCita = detalleCita;
	}

	/**
	 * @return the detalleCita
	 */
	public Object getDetalleCita(String key) {
		return detalleCita.get(key);
	}


	/**
	 * @param detalleCita the detalleCita to set
	 */
	public void setDetalleCita(String key,Object obj) {
		this.detalleCita.put(key,obj);
	}
	
	
	/**
	 * @return the numDetalleCita
	 */
	public int getNumDetalleCita() {
		return numDetalleCita;
	}


	/**
	 * @param numDetalleCita the numDetalleCita to set
	 */
	public void setNumDetalleCita(int numDetalleCita) {
		this.numDetalleCita = numDetalleCita;
	}


	/**
	 * @return the posCita
	 */
	public int getPosCita() {
		return posCita;
	}


	/**
	 * @param posCita the posCita to set
	 */
	public void setPosCita(int posCita) {
		this.posCita = posCita;
	}


	/**
	 * @return the existeCargosDirectos
	 */
	public boolean isExisteCargosDirectos() {
		return existeCargosDirectos;
	}

	public String getSolicitud() {
		return solicitud;
	}
	/**
	 * @param existeCargosDirectos the existeCargosDirectos to set
	 */
	public void setExisteCargosDirectos(boolean existeCargosDirectos) {
		this.existeCargosDirectos = existeCargosDirectos;
	}


	/**
	 * @return the solCDirectosArticulosMap
	 */
	public HashMap getSolCDirectosArticulosMap() {
		return solCDirectosArticulosMap;
	}
	public void setSolicitud(String solicitud) {
		this.solicitud = solicitud;
	}

	/**
	 * @param solCDirectosArticulosMap the solCDirectosArticulosMap to set
	 */
	public void setSolCDirectosArticulosMap(HashMap solCDirectosArticulosMap) {
		this.solCDirectosArticulosMap = solCDirectosArticulosMap;
	}
	
	/**
	 * @return the solCDirectosArticulosMap
	 */
	public Object getSolCDirectosArticulosMap(String key) {
		return solCDirectosArticulosMap.get(key);
	}


	/**
	 * @param solCDirectosArticulosMap the solCDirectosArticulosMap to set
	 */
	public void setSolCDirectosArticulosMap(String key, Object value) {
		this.solCDirectosArticulosMap.put(key, value);
	}

	public String getTipoSolicitud() {
		return tipoSolicitud;
	}


	public void setTipoSolicitud(String tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}


	public String getCodigoArticulo() {
		return codigoArticulo;
	}


	public void setCodigoArticulo(String codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}


	/**
	 * @return the solCDirectosCirugiaDyTMap
	 */
	public Object getSolCDirectosCirugiaDyTMap(String key) {
		return solCDirectosCirugiaDyTMap.get(key);
	}


	/**
	 * @param solCDirectosCirugiaDyTMap the solCDirectosCirugiaDyTMap to set
	 */
	public void setSolCDirectosCirugiaDyTMap(String key, Object value) {
		this.solCDirectosCirugiaDyTMap.put(key, value);
	}


	/**
	 * @return the solCDirectosConsultasMap
	 */
	public Object getSolCDirectosConsultasMap(String key) {
		return solCDirectosConsultasMap.get(key);
	}


	/**
	 * @param solCDirectosConsultasMap the solCDirectosConsultasMap to set
	 */
	public void setSolCDirectosConsultasMap(String key, Object value) {
		this.solCDirectosConsultasMap.put(key, value);
	}


	/**
	 * @return the solCDirectosProcedimientosMap
	 */
	public Object getSolCDirectosProcedimientosMap(String key) {
		return solCDirectosProcedimientosMap.get(key);
	}


	/**
	 * @param solCDirectosProcedimientosMap the solCDirectosProcedimientosMap to set
	 */
	public void setSolCDirectosProcedimientosMap(
			String key, Object value) {
		this.solCDirectosProcedimientosMap.put(key, value);
	}


	/**
	 * @return the solCDirectosServiciosMap
	 */
	public Object getSolCDirectosServiciosMap(String key) {
		return solCDirectosServiciosMap.get(key);
	}


	/**
	 * @param solCDirectosServiciosMap the solCDirectosServiciosMap to set
	 */
	public void setSolCDirectosServiciosMap(String key, Object value) {
		this.solCDirectosServiciosMap.put(key, value);
	}


	/**
	 * @return the solCDirectosCirugiaDyTMap
	 */
	public HashMap getSolCDirectosCirugiaDyTMap() {
		return solCDirectosCirugiaDyTMap;
	}


	/**
	 * @param solCDirectosCirugiaDyTMap the solCDirectosCirugiaDyTMap to set
	 */
	public void setSolCDirectosCirugiaDyTMap(HashMap solCDirectosCirugiaDyTMap) {
		this.solCDirectosCirugiaDyTMap = solCDirectosCirugiaDyTMap;
	}


	/**
	 * @return the solCDirectosConsultasMap
	 */
	public HashMap getSolCDirectosConsultasMap() {
		return solCDirectosConsultasMap;
	}


	/**
	 * @param solCDirectosConsultasMap the solCDirectosConsultasMap to set
	 */
	public void setSolCDirectosConsultasMap(HashMap solCDirectosConsultasMap) {
		this.solCDirectosConsultasMap = solCDirectosConsultasMap;
	}


	/**
	 * @return the solCDirectosProcedimientosMap
	 */
	public HashMap getSolCDirectosProcedimientosMap() {
		return solCDirectosProcedimientosMap;
	}


	/**
	 * @param solCDirectosProcedimientosMap the solCDirectosProcedimientosMap to set
	 */
	public void setSolCDirectosProcedimientosMap(
			HashMap solCDirectosProcedimientosMap) {
		this.solCDirectosProcedimientosMap = solCDirectosProcedimientosMap;
	}


	/**
	 * @return the solCDirectosServiciosMap
	 */
	public HashMap getSolCDirectosServiciosMap() {
		return solCDirectosServiciosMap;
	}


	/**
	 * @param solCDirectosServiciosMap the solCDirectosServiciosMap to set
	 */
	public void setSolCDirectosServiciosMap(HashMap solCDirectosServiciosMap) {
		this.solCDirectosServiciosMap = solCDirectosServiciosMap;
	}

	/**
	 * @return the mapaHojaAnestesia
	 */
	public HashMap getMapaHojaAnestesia() {
		return mapaHojaAnestesia;
	}


	/**
	 * @param mapaHojaAnestesia the mapaHojaAnestesia to set
	 */
	public void setMapaHojaAnestesia(HashMap mapaHojaAnestesia) {
		this.mapaHojaAnestesia = mapaHojaAnestesia;
	}
	
	/**
	 * @return the mapaHojaAnestesia
	 */
	public Object getMapaHojaAnestesia(String key) {
		return mapaHojaAnestesia.get(key);
	}


	/**
	 * @param mapaHojaAnestesia the mapaHojaAnestesia to set
	 */
	public void setMapaHojaAnestesia(String key, Object value) {
		this.mapaHojaAnestesia.put(key, value);
	}


	/**
	 * @return the mapaValidacionesInfoCuentas
	 */
	public HashMap getMapaValidacionesInfoCuentas() {
		return mapaValidacionesInfoCuentas;
	}


	/**
	 * @param mapaValidacionesInfoCuentas the mapaValidacionesInfoCuentas to set
	 */
	public void setMapaValidacionesInfoCuentas(HashMap mapaValidacionesInfoCuentas) {
		this.mapaValidacionesInfoCuentas = mapaValidacionesInfoCuentas;
	}
	
	/**
	 * @return the mapaValidacionesInfoCuentas
	 */
	public Object getMapaValidacionesInfoCuentas(String key) {
		return mapaValidacionesInfoCuentas.get(key);
	}


	/**
	 * @param mapaValidacionesInfoCuentas the mapaValidacionesInfoCuentas to set
	 */
	public void setMapaValidacionesInfoCuentas(String key, Object value) {
		this.mapaValidacionesInfoCuentas.put(key, value);
	}


	/**
	 * @return the indiceMapaValInfoCuentas
	 */
	public int getIndiceMapaValInfoCuentas() {
		return indiceMapaValInfoCuentas;
	}


	/**
	 * @param indiceMapaValInfoCuentas the indiceMapaValInfoCuentas to set
	 */
	public void setIndiceMapaValInfoCuentas(int indiceMapaValInfoCuentas) {
		this.indiceMapaValInfoCuentas = indiceMapaValInfoCuentas;
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


	/**
	 * @return the muestroResumenPHC
	 */
	public int getMuestroResumenPHC() {
		return muestroResumenPHC;
	}


	/**
	 * @param muestroResumenPHC the muestroResumenPHC to set
	 */
	public void setMuestroResumenPHC(int muestroResumenPHC) {
		this.muestroResumenPHC = muestroResumenPHC;
	}


	/**
	 * @return the escalas
	 */
	public ArrayList<DtoEscala> getEscalas() {
		return escalas;
	}


	/**
	 * @param escalas the escalas to set
	 */
	public void setEscalas(ArrayList<DtoEscala> escalas) {
		this.escalas = escalas;
	}


	/**
	 * @return the existenEscalasXIngreso
	 */
	public boolean isExistenEscalasXIngreso() {
		return existenEscalasXIngreso;
	}


	/**
	 * @param existenEscalasXIngreso the existenEscalasXIngreso to set
	 */
	public void setExistenEscalasXIngreso(boolean existenEscalasXIngreso) {
		this.existenEscalasXIngreso = existenEscalasXIngreso;
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
	 * @return the valoracionesCuidadoEspecial
	 */
	public Object getValoracionesCuidadoEspecial(String key) {
		return valoracionesCuidadoEspecial.get(key);
	}


	/**
	 * @param valoracionesCuidadoEspecial the valoracionesCuidadoEspecial to set
	 */
	public void setValoracionesCuidadoEspecial(String key,Object obj) {
		this.valoracionesCuidadoEspecial.put(key, obj);
	}

	/**
	 * @return the plantillaDtoArray
	 */
	public ArrayList<DtoPlantilla> getPlantillaDtoArray() {
		return plantillaDtoArray;
	}


	/**
	 * @param plantillaDtoArray the plantillaDtoArray to set
	 */
	public void setPlantillaDtoArray(ArrayList<DtoPlantilla> plantillaDtoArray) {
		this.plantillaDtoArray = plantillaDtoArray;
	}


	/**
	 * @return the codigoPeticionCx
	 */
	public String getCodigoPeticionCx() {
		return codigoPeticionCx;
	}


	/**
	 * @param codigoPeticionCx the codigoPeticionCx to set
	 */
	public void setCodigoPeticionCx(String codigoPeticionCx) {
		this.codigoPeticionCx = codigoPeticionCx;
	}


	/**
	 * @return the perfilFarmacoterapiaMap
	 */
	public HashMap getPerfilFarmacoterapiaMap() {
		return perfilFarmacoterapiaMap;
	}


	/**
	 * @param perfilFarmacoterapiaMap the perfilFarmacoterapiaMap to set
	 */
	public void setPerfilFarmacoterapiaMap(HashMap perfilFarmacoterapiaMap) {
		this.perfilFarmacoterapiaMap = perfilFarmacoterapiaMap;
	}
	
	/**
	 * @return the perfilFarmacoterapiaMap
	 */
	public Object getPerfilFarmacoterapiaMap(String llave) {
		return perfilFarmacoterapiaMap.get(llave);
	}


	/**
	 * @param perfilFarmacoterapiaMap the perfilFarmacoterapiaMap to set
	 */
	public void setPerfilFarmacoterapiaMap(String llave, Object obj) {
		this.perfilFarmacoterapiaMap.put(llave, obj);
	}


	/**
	 * @return the perfilFarmacoterapiaMesMap
	 */
	public HashMap getPerfilFarmacoterapiaMesMap() {
		return perfilFarmacoterapiaMesMap;
	}


	/**
	 * @param perfilFarmacoterapiaMesMap the perfilFarmacoterapiaMesMap to set
	 */
	public void setPerfilFarmacoterapiaMesMap(HashMap perfilFarmacoterapiaMesMap) {
		this.perfilFarmacoterapiaMesMap = perfilFarmacoterapiaMesMap;
	}


	/**
	 * @return the filasPerfilFarmacoterapiaMesMap
	 */
	public HashMap getFilasPerfilFarmacoterapiaMesMap() {
		return filasPerfilFarmacoterapiaMesMap;
	}


	/**
	 * @param filasPerfilFarmacoterapiaMesMap the filasPerfilFarmacoterapiaMesMap to set
	 */
	public void setFilasPerfilFarmacoterapiaMesMap(
			HashMap filasPerfilFarmacoterapiaMesMap) {
		this.filasPerfilFarmacoterapiaMesMap = filasPerfilFarmacoterapiaMesMap;
	}


	/**
	 * @return the columnasPerfilFarmacoterapiaMesMap
	 */
	public HashMap getColumnasPerfilFarmacoterapiaMesMap() {
		return columnasPerfilFarmacoterapiaMesMap;
	}


	/**
	 * @param columnasPerfilFarmacoterapiaMesMap the columnasPerfilFarmacoterapiaMesMap to set
	 */
	public void setColumnasPerfilFarmacoterapiaMesMap(
			HashMap columnasPerfilFarmacoterapiaMesMap) {
		this.columnasPerfilFarmacoterapiaMesMap = columnasPerfilFarmacoterapiaMesMap;
	}


	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}


	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}


	/**
	 * @return the esImpresionAutomatica
	 */
	public String getEsImpresionAutomatica() {
		return esImpresionAutomatica;
	}


	/**
	 * @param esImpresionAutomatica the esImpresionAutomatica to set
	 */
	public void setEsImpresionAutomatica(String esImpresionAutomatica) {
		this.esImpresionAutomatica = esImpresionAutomatica;
	}


	public DtoPreanestesia getPreanestesiaDto() {
		return preanestesiaDto;
	}


	public void setPreanestesiaDto(DtoPreanestesia preanestesiaDto) {
		this.preanestesiaDto = preanestesiaDto;
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



	public boolean isQuitarEncabezados() {
		return quitarEncabezados;
	}


	public void setQuitarEncabezados(boolean quitarEncabezados) {
		this.quitarEncabezados = quitarEncabezados;
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
	 * @return the tienePermisoImprimirDetalleItemHC
	 */
	public Boolean getTienePermisoImprimirDetalleItemHC() {
		return tienePermisoImprimirDetalleItemHC;
	}


	/**
	 * @param tienePermisoImprimirDetalleItemHC the tienePermisoImprimirDetalleItemHC to set
	 */
	public void setTienePermisoImprimirDetalleItemHC(
			Boolean tienePermisoImprimirDetalleItemHC) {
		this.tienePermisoImprimirDetalleItemHC = tienePermisoImprimirDetalleItemHC;
	}


	/**
	 * @return the tienePermisoImprimirHC
	 */
	public Boolean getTienePermisoImprimirHC() {
		return tienePermisoImprimirHC;
	}


	/**
	 * @param tienePermisoImprimirHC the tienePermisoImprimirHC to set
	 */
	public void setTienePermisoImprimirHC(Boolean tienePermisoImprimirHC) {
		this.tienePermisoImprimirHC = tienePermisoImprimirHC;
	}


	/**
	 * @return the tienePermisoConsultarHistoriaDeAtenciones
	 */
	public Boolean getTienePermisoConsultarHistoriaDeAtenciones() {
		return tienePermisoConsultarHistoriaDeAtenciones;
	}


	/**
	 * @param tienePermisoConsultarHistoriaDeAtenciones the tienePermisoConsultarHistoriaDeAtenciones to set
	 */
	public void setTienePermisoConsultarHistoriaDeAtenciones(
			Boolean tienePermisoConsultarHistoriaDeAtenciones) {
		this.tienePermisoConsultarHistoriaDeAtenciones = tienePermisoConsultarHistoriaDeAtenciones;
	}


	/**
	 * @return the rowSpanSeccionUnoReporteHC
	 */
	public Integer getRowSpanSeccionUnoReporteHC() {
		return rowSpanSeccionUnoReporteHC;
	}


	/**
	 * @param rowSpanSeccionUnoReporteHC the rowSpanSeccionUnoReporteHC to set
	 */
	public void setRowSpanSeccionUnoReporteHC(Integer rowSpanSeccionUnoReporteHC) {
		this.rowSpanSeccionUnoReporteHC = rowSpanSeccionUnoReporteHC;
	}


	/**
	 * @return the rowSpanSeccionDosReporteHC
	 */
	public Integer getRowSpanSeccionDosReporteHC() {
		return rowSpanSeccionDosReporteHC;
	}


	/**
	 * @param rowSpanSeccionDosReporteHC the rowSpanSeccionDosReporteHC to set
	 */
	public void setRowSpanSeccionDosReporteHC(Integer rowSpanSeccionDosReporteHC) {
		this.rowSpanSeccionDosReporteHC = rowSpanSeccionDosReporteHC;
	}


	/**
	 * @return the rowSpanSeccionTresReporteHC
	 */
	public Integer getRowSpanSeccionTresReporteHC() {
		return rowSpanSeccionTresReporteHC;
	}


	/**
	 * @param rowSpanSeccionTresReporteHC the rowSpanSeccionTresReporteHC to set
	 */
	public void setRowSpanSeccionTresReporteHC(Integer rowSpanSeccionTresReporteHC) {
		this.rowSpanSeccionTresReporteHC = rowSpanSeccionTresReporteHC;
	}


	/**
	 * @return the rowSpanSeccionCuatroReporteHC
	 */
	public Integer getRowSpanSeccionCuatroReporteHC() {
		return rowSpanSeccionCuatroReporteHC;
	}


	/**
	 * @param rowSpanSeccionCuatroReporteHC the rowSpanSeccionCuatroReporteHC to set
	 */
	public void setRowSpanSeccionCuatroReporteHC(
			Integer rowSpanSeccionCuatroReporteHC) {
		this.rowSpanSeccionCuatroReporteHC = rowSpanSeccionCuatroReporteHC;
	}


	/**
	 * @return the rowSpanSeccionCincoReporteHC
	 */
	public Integer getRowSpanSeccionCincoReporteHC() {
		return rowSpanSeccionCincoReporteHC;
	}


	/**
	 * @param rowSpanSeccionCincoReporteHC the rowSpanSeccionCincoReporteHC to set
	 */
	public void setRowSpanSeccionCincoReporteHC(Integer rowSpanSeccionCincoReporteHC) {
		this.rowSpanSeccionCincoReporteHC = rowSpanSeccionCincoReporteHC;
	}


	/**
	 * @return the rowSpanSeccionSeisReporteHC
	 */
	public Integer getRowSpanSeccionSeisReporteHC() {
		return rowSpanSeccionSeisReporteHC;
	}


	/**
	 * @param rowSpanSeccionSeisReporteHC the rowSpanSeccionSeisReporteHC to set
	 */
	public void setRowSpanSeccionSeisReporteHC(Integer rowSpanSeccionSeisReporteHC) {
		this.rowSpanSeccionSeisReporteHC = rowSpanSeccionSeisReporteHC;
	}


	/**
	 * @return the rowSpanSeccionSieteReporteHC
	 */
	public Integer getRowSpanSeccionSieteReporteHC() {
		return rowSpanSeccionSieteReporteHC;
	}


	/**
	 * @param rowSpanSeccionSieteReporteHC the rowSpanSeccionSieteReporteHC to set
	 */
	public void setRowSpanSeccionSieteReporteHC(Integer rowSpanSeccionSieteReporteHC) {
		this.rowSpanSeccionSieteReporteHC = rowSpanSeccionSieteReporteHC;
	}


	/**
	 * @return the rowSpanSeccionOchoReporteHC
	 */
	public Integer getRowSpanSeccionOchoReporteHC() {
		return rowSpanSeccionOchoReporteHC;
	}


	/**
	 * @param rowSpanSeccionOchoReporteHC the rowSpanSeccionOchoReporteHC to set
	 */
	public void setRowSpanSeccionOchoReporteHC(Integer rowSpanSeccionOchoReporteHC) {
		this.rowSpanSeccionOchoReporteHC = rowSpanSeccionOchoReporteHC;
	}


	/**
	 * @return the rowSpanSeccionNueveReporteHC
	 */
	public Integer getRowSpanSeccionNueveReporteHC() {
		return rowSpanSeccionNueveReporteHC;
	}


	/**
	 * @param rowSpanSeccionNueveReporteHC the rowSpanSeccionNueveReporteHC to set
	 */
	public void setRowSpanSeccionNueveReporteHC(Integer rowSpanSeccionNueveReporteHC) {
		this.rowSpanSeccionNueveReporteHC = rowSpanSeccionNueveReporteHC;
	}


	/**
	 * @return the rowSpanSeccionDiezReporteHC
	 */
	public Integer getRowSpanSeccionDiezReporteHC() {
		return rowSpanSeccionDiezReporteHC;
	}


	/**
	 * @param rowSpanSeccionDiezReporteHC the rowSpanSeccionDiezReporteHC to set
	 */
	public void setRowSpanSeccionDiezReporteHC(Integer rowSpanSeccionDiezReporteHC) {
		this.rowSpanSeccionDiezReporteHC = rowSpanSeccionDiezReporteHC;
	}


	/**
	 * @return the muestroResumenPHCAsocio
	 */
	public Integer getMuestroResumenPHCAsocio() {
		return muestroResumenPHCAsocio;
	}


	/**
	 * @param muestroResumenPHCAsocio the muestroResumenPHCAsocio to set
	 */
	public void setMuestroResumenPHCAsocio(Integer muestroResumenPHCAsocio) {
		this.muestroResumenPHCAsocio = muestroResumenPHCAsocio;
	}

	/**
	 * @return the notaAclaratoriaDTO
	 */
	public DtoNotaAclaratoria getNotaAclaratoriaDTO() {
		return notaAclaratoriaDTO;
	}


	/**
	 * @param notaAclaratoriaDTO the notaAclaratoriaDTO to set
	 */
	public void setNotaAclaratoriaDTO(DtoNotaAclaratoria notaAclaratoriaDTO) {
		this.notaAclaratoriaDTO = notaAclaratoriaDTO;
	}


	/**
	 * @return the notasAclaratorias
	 */
	public ArrayList<DtoNotaAclaratoria> getNotasAclaratorias() {
		return notasAclaratorias;
	}


	/**
	 * @param notasAclaratorias the notasAclaratorias to set
	 */
	public void setNotasAclaratorias(ArrayList<DtoNotaAclaratoria> notasAclaratorias) {
		this.notasAclaratorias = notasAclaratorias;
	}


	/**
	 * @return the detalleNotaAclaratoria
	 */
	public boolean isDetalleNotaAclaratoria() {
		return detalleNotaAclaratoria;
	}


	/**
	 * @param detalleNotaAclaratoria the detalleNotaAclaratoria to set
	 */
	public void setDetalleNotaAclaratoria(boolean detalleNotaAclaratoria) {
		this.detalleNotaAclaratoria = detalleNotaAclaratoria;
	}


	/**
	 * @return the guardarNotaAclaratoria
	 */
	public boolean isGuardarNotaAclaratoria() {
		return guardarNotaAclaratoria;
	}


	/**
	 * @param guardarNotaAclaratoria the guardarNotaAclaratoria to set
	 */
	public void setGuardarNotaAclaratoria(boolean guardarNotaAclaratoria) {
		this.guardarNotaAclaratoria = guardarNotaAclaratoria;
	}


	/**
	 * @return the nuevaNotaAclaratoria
	 */
	public boolean isNuevaNotaAclaratoria() {
		return nuevaNotaAclaratoria;
	}


	/**
	 * @param nuevaNotaAclaratoria the nuevaNotaAclaratoria to set
	 */
	public void setNuevaNotaAclaratoria(boolean nuevaNotaAclaratoria) {
		this.nuevaNotaAclaratoria = nuevaNotaAclaratoria;
	}


	public String getCodigoNotaAclaratoria() {
		return codigoNotaAclaratoria;
	}


	public void setCodigoNotaAclaratoria(String codigoNotaAclaratoria) {
		this.codigoNotaAclaratoria = codigoNotaAclaratoria;
	}


	/**
	 * @return the listaDtoSOlcitudes
	 */
	public List<DtoCodigosCirugiaPeticiones> getListaDtoSOlcitudes() {
		return listaDtoSOlcitudes;
	}


	/**
	 * @param listaDtoSOlcitudes the listaDtoSOlcitudes to set
	 */
	public void setListaDtoSOlcitudes(
			List<DtoCodigosCirugiaPeticiones> listaDtoSOlcitudes) {
		this.listaDtoSOlcitudes = listaDtoSOlcitudes;
	}


	/**
	 * @return the estadoHCCLiente
	 */
	public Integer getEstadoHCCLiente() {
		return estadoHCCLiente;
	}


	/**
	 * @param estadoHCCLiente the estadoHCCLiente to set
	 */
	public void setEstadoHCCLiente(Integer estadoHCCLiente) {
		this.estadoHCCLiente = estadoHCCLiente;
	}


	/**
	 * @return the existenCitasCE
	 */
	public Boolean getExistenCitasCE() {
		return existenCitasCE;
	}


	/**
	 * @param existenCitasCE the existenCitasCE to set
	 */
	public void setExistenCitasCE(Boolean existenCitasCE) {
		this.existenCitasCE = existenCitasCE;
	}


	/**
	 * @return the listaCheckGlobalHC
	 */
	public List<Boolean> getListaCheckGlobalHC() {
		return listaCheckGlobalHC;
	}


	/**
	 * @param listaCheckGlobalHC the listaCheckGlobalHC to set
	 */
	public void setListaCheckGlobalHC(List<Boolean> listaCheckGlobalHC) {
		this.listaCheckGlobalHC = listaCheckGlobalHC;
	}


	/**
	 * @return the estadoCheckTodos
	 */
	public Boolean getEstadoCheckTodos() {
		return estadoCheckTodos;
	}


	/**
	 * @param estadoCheckTodos the estadoCheckTodos to set
	 */
	public void setEstadoCheckTodos(Boolean estadoCheckTodos) {
		this.estadoCheckTodos = estadoCheckTodos;
	}


	/**
	 * @return the anoFiltro
	 */
	public String getAnoFiltro() {
		return anoFiltro;
	}


	/**
	 * @param anoFiltro the anoFiltro to set
	 */
	public void setAnoFiltro(String anoFiltro) {
		this.anoFiltro = anoFiltro;
	}










	/**
	 * @return the centroAtencionFiltro
	 */
	public String getCentroAtencionFiltro() {
		return centroAtencionFiltro;
	}


	/**
	 * @param centroAtencionFiltro the centroAtencionFiltro to set
	 */
	public void setCentroAtencionFiltro(String centroAtencionFiltro) {
		this.centroAtencionFiltro = centroAtencionFiltro;
	}


	/**
	 * @return the viaIngresoFiltro
	 */
	public String getViaIngresoFiltro() {
		return viaIngresoFiltro;
	}


	/**
	 * @param viaIngresoFiltro the viaIngresoFiltro to set
	 */
	public void setViaIngresoFiltro(String viaIngresoFiltro) {
		this.viaIngresoFiltro = viaIngresoFiltro;
	}


	/**
	 * @return the especialidadFiltro
	 */
	public String getEspecialidadFiltro() {
		return especialidadFiltro;
	}


	/**
	 * @param especialidadFiltro the especialidadFiltro to set
	 */
	public void setEspecialidadFiltro(String especialidadFiltro) {
		this.especialidadFiltro = especialidadFiltro;
	}


	/**
	 * @return the fechaIngresoInicialFiltro
	 */
	public String getFechaIngresoInicialFiltro() {
		return fechaIngresoInicialFiltro;
	}


	/**
	 * @param fechaIngresoInicialFiltro the fechaIngresoInicialFiltro to set
	 */
	public void setFechaIngresoInicialFiltro(String fechaIngresoInicialFiltro) {
		this.fechaIngresoInicialFiltro = fechaIngresoInicialFiltro;
	}


	/**
	 * @return the fechaIngresoFinalFiltro
	 */
	public String getFechaIngresoFinalFiltro() {
		return fechaIngresoFinalFiltro;
	}


	/**
	 * @param fechaIngresoFinalFiltro the fechaIngresoFinalFiltro to set
	 */
	public void setFechaIngresoFinalFiltro(String fechaIngresoFinalFiltro) {
		this.fechaIngresoFinalFiltro = fechaIngresoFinalFiltro;
	}


	/**
	 * @return the listaCentrosAtencion
	 */
	public ArrayList<CentroAtencion> getListaCentrosAtencion() {
		return listaCentrosAtencion;
	}


	/**
	 * @param listaCentrosAtencion the listaCentrosAtencion to set
	 */
	public void setListaCentrosAtencion(
			ArrayList<CentroAtencion> listaCentrosAtencion) {
		this.listaCentrosAtencion = listaCentrosAtencion;
	}


	/**
	 * @return the viasIngresoList
	 */
	public List<DtoViasIngresoHC> getViasIngresoList() {
		return viasIngresoList;
	}


	/**
	 * @param viasIngresoList the viasIngresoList to set
	 */
	public void setViasIngresoList(List<DtoViasIngresoHC> viasIngresoList) {
		this.viasIngresoList = viasIngresoList;
	}


	/**
	 * @return the especialdiades
	 */
	public List<DtoEspecialidadesHC> getEspecialdiades() {
		return especialdiades;
	}


	/**
	 * @param especialdiades the especialdiades to set
	 */
	public void setEspecialdiades(List<DtoEspecialidadesHC> especialdiades) {
		this.especialdiades = especialdiades;
	}


	/**
	 * @return the anosPaciente
	 */
	public List<String> getAnosPaciente() {
		return anosPaciente;
	}


	/**
	 * @param anosPaciente the anosPaciente to set
	 */
	public void setAnosPaciente(List<String> anosPaciente) {
		this.anosPaciente = anosPaciente;
	}


	/**
	 * @return the viaIngresoSeleccionadaFiltroHc
	 */
	public String getViaIngresoSeleccionadaFiltroHc() {
		return viaIngresoSeleccionadaFiltroHc;
	}


	/**
	 * @param viaIngresoSeleccionadaFiltroHc the viaIngresoSeleccionadaFiltroHc to set
	 */
	public void setViaIngresoSeleccionadaFiltroHc(
			String viaIngresoSeleccionadaFiltroHc) {
		this.viaIngresoSeleccionadaFiltroHc = viaIngresoSeleccionadaFiltroHc;
	}





	/**
	 * @return the espeSeleccioandaFiltroHc
	 */
	public String getEspeSeleccioandaFiltroHc() {
		return espeSeleccioandaFiltroHc;
	}


	/**
	 * @param espeSeleccioandaFiltroHc the espeSeleccioandaFiltroHc to set
	 */
	public void setEspeSeleccioandaFiltroHc(String espeSeleccioandaFiltroHc) {
		this.espeSeleccioandaFiltroHc = espeSeleccioandaFiltroHc;
	}


	/**
	 * @return the anoSeleccionadoFiltroHC
	 */
	public String getAnoSeleccionadoFiltroHC() {
		return anoSeleccionadoFiltroHC;
	}


	/**
	 * @param anoSeleccionadoFiltroHC the anoSeleccionadoFiltroHC to set
	 */
	public void setAnoSeleccionadoFiltroHC(String anoSeleccionadoFiltroHC) {
		this.anoSeleccionadoFiltroHC = anoSeleccionadoFiltroHC;
	}


	/**
	 * @return the estadoFiltroBusqueda
	 */
	public String getEstadoFiltroBusqueda() {
		return estadoFiltroBusqueda;
	}


	/**
	 * @param estadoFiltroBusqueda the estadoFiltroBusqueda to set
	 */
	public void setEstadoFiltroBusqueda(String estadoFiltroBusqueda) {
		this.estadoFiltroBusqueda = estadoFiltroBusqueda;
	}


	/**
	 * @return the hayResultados
	 */
	public Boolean getHayResultados() {
		return hayResultados;
	}


	/**
	 * @param hayResultados the hayResultados to set
	 */
	public void setHayResultados(Boolean hayResultados) {
		this.hayResultados = hayResultados;
	}


	/**
	 * @return the mostrarFiltroAnos
	 */
	public Boolean getMostrarFiltroAnos() {
		return mostrarFiltroAnos;
	}


	/**
	 * @param mostrarFiltroAnos the mostrarFiltroAnos to set
	 */
	public void setMostrarFiltroAnos(Boolean mostrarFiltroAnos) {
		this.mostrarFiltroAnos = mostrarFiltroAnos;
	}


	/**
	 * @return the cantidadDiasParametro
	 */
	public Integer getCantidadDiasParametro() {
		return cantidadDiasParametro;
	}


	/**
	 * @param cantidadDiasParametro the cantidadDiasParametro to set
	 */
	public void setCantidadDiasParametro(Integer cantidadDiasParametro) {
		this.cantidadDiasParametro = cantidadDiasParametro;
	}


	/**
	 * @return the ordenamiento
	 */
	public boolean isOrdenamiento() {
		return ordenamiento;
	}


	/**
	 * @param ordenamiento the ordenamiento to set
	 */
	public void setOrdenamiento(boolean ordenamiento) {
		this.ordenamiento = ordenamiento;
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
	 * @return the datosValoracionInicial
	 */
	public HashMap<String, Object> getDatosValoracionInicial() {
		return datosValoracionInicial;
	}

	/**
	 * @return the ingresoSelecccionado
	 */
	public InfoIngresoDto getIngresoSelecccionado() {
		return ingresoSelecccionado;
	}

	/**
	 * @param datosValoracionInicial the datosValoracionInicial to set
	 */
	public void setDatosValoracionInicial(
			HashMap<String, Object> datosValoracionInicial) {
		this.datosValoracionInicial = datosValoracionInicial;
	}
	
	/**
	 * @param ingresoSelecccionado the ingresoSelecccionado to set
	 */
	public void setIngresoSelecccionado(InfoIngresoDto ingresoSelecccionado) {
		this.ingresoSelecccionado = ingresoSelecccionado;
	}

	/**
	 * @return the mostrarRutaJsp
	 */
	public boolean isMostrarRutaJsp() {
		return mostrarRutaJsp;
	}

	/**
	 * @param mostrarRutaJsp the mostrarRutaJsp to set
	 */
	public void setMostrarRutaJsp(boolean mostrarRutaJsp) {
		this.mostrarRutaJsp = mostrarRutaJsp;
	}

	public boolean isEstadoBotonImprimirHC() {
		return estadoBotonImprimirHC;
	}

	public void setEstadoBotonImprimirHC(boolean estadoBotonImprimirHC) {
		this.estadoBotonImprimirHC = estadoBotonImprimirHC;
	}

	public boolean isImprimiendoHC() {
		return imprimiendoHC;
	}

	public void setImprimiendoHC(boolean imprimiendoHC) {
		this.imprimiendoHC = imprimiendoHC;
	}

	public int getContarCantidadSubmitImpresion() {
		return contarCantidadSubmitImpresion;
	}

	public void setContarCantidadSubmitImpresion(int contarCantidadSubmitImpresion) {
		this.contarCantidadSubmitImpresion = contarCantidadSubmitImpresion;
	}

	public boolean isHaSeleccionadoIngreso() {
		return haSeleccionadoIngreso;
	}

	public void setHaSeleccionadoIngreso(boolean haSeleccionadoIngreso) {
		this.haSeleccionadoIngreso = haSeleccionadoIngreso;
	}

	public int getCantidadIngresosSeleccionados() {
		return cantidadIngresosSeleccionados;
	}

	public void setCantidadIngresosSeleccionados(int cantidadIngresosSeleccionados) {
		this.cantidadIngresosSeleccionados = cantidadIngresosSeleccionados;
	}

	//----------------------------------------------------------------------------------------	
}
