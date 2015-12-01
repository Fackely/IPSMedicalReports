/*
 * @(#)EvolucionForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.actionform;
 
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.atencion.Evolucion;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;

/**
 * ActionForm, tiene la función de bean dentro de la forma, que contiene todos
 * los datos especificados para una evolución, maneja tanto códigos
 * como nombres. Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * 
 * @version 1.0, May 26, 2003
 * 
 *@todo: Validar que cuando tiene que llenar la reversión del egreso el campo NO sea vacio
 */
public class EvolucionForm extends ValidatorForm
{
	/**
	 * Fecha en la que se realiza esta evolución.
	 * Se le dan valores a fecha y hora pues son
	 * obligatorios y como validación se hace por
	 * XML tiene que estar bien incluso ANTES de
	 * llegar al action. De todas maneras el método
	 * clean los limpia y les dá valores correpondientes
	 * a la fecha actual
	 */
	private String fechaEvolucion;

	/**
	 * Hora en la que se realiza esta evolución.
	 */
	private String horaEvolucion;

	/**
	 * Información suministrada por el paciente.
	 */
	private String informacionDadaPaciente="";

	/**
	 * Define si la Información suministrada por 
	 * el paciente va o no a la epicrisis
	 */
	private boolean informacionDadaPacienteBoolean=false;

	/**
	 * Hallazgos Importantes del médico
	 */
	private String hallazgosImportantes="";

	/**
	 * Procedimientos quirurgicos y obstetricos realizados
	 */
	private String procedimientosQuirurgicosYObstetricos="";
	
	/**
	 * Define si los Procedimientos quirurgicos y obstetricos realizados
	 * al paciente va o no a la epicrisis
	 */
	private boolean procedimientosQuirurgicosYObstetricosBoolean=false;
	
	/**
	 * Fecha y Resultados de exámenes diagnostico
	 */
	private String fechaResultadosExamenesDiagnostico="";
	
	/**
	 * Define si la Fecha y Resultados de exámenes diagnostico 
	 * del paciente va o no a la epicrisis
	 */
	private boolean fechaResultadosExamenesDiagnosticoBoolean=false;
	
	/**
	 * Pronóstico
	 */
	private String pronostico="";

	/**
	 * Define si el Pronóstico 
	 * del paciente va o no a la epicrisis
	 */
	private boolean pronosticoBoolean=false;
	
	/**
	 * Define si los Hallazgos Importantes del médico 
	 * el paciente va o no a la epicrisis
	 */
	private boolean hallazgosImportantesBoolean=false;

	/**
	 * Descripción de las complicaciones de la 
	 * enfermedad del paciente.
	 */
	private String descripcionComplicacion="";

	/**
	 * Define si las complicaciones de la enfermedad 
	 * del paciente van o no a la epicrisis
	 */
	private boolean descripcionComplicacionBoolean=false;

	/**
	 * Tratamiento prescrito al paciente.
	 */
	private String tratamiento="";

	/**
	 * Define si el tratamiento del paciente va o no a 
	 * la epicrisis
	 */
	private boolean tratamientoBoolean=false;

	/**
	 * Resultados del tratamiento prescrito al paciente.
	 */
	private String resultadosTratamiento="";

	/**
	 * Define si los resultados del tratamiento prescrito 
	 * del paciente van o no a la epicrisis
	 */
	private boolean resultadosTratamientoBoolean=false;
	
	/**
	 * Cambios en el manejo dado al paciente.
	 */
	private String cambiosManejo="";

	/**
	 * Define si los Cambios en el manejo 
	 * del paciente van o no a la epicrisis
	 */
	private boolean cambiosManejoBoolean=false;
	
	/**
	 * Observaciones generales de la evolución.
	 */
	private String observaciones="";

	/**
	 * Define si  las observaciones generales de 
	 * la evolución del paciente van o no a la 
	 * epicrisis
	 */
	private boolean observacionesBoolean=false;

	/**
	 * El tipo de esta evolución es 'o', de 'otros'.
	 */
	private char tipoEvolucion = 'o';
	
	/**
	 * Aqui se captura el diagnostico de ingreso
	 * (Completo compuesto por codigo/CIE/nombre, 
	 * separado por "-")
	 */
	private String diagnosticoIngreso_1="";

	/**
	 * Aqui se captura el diagnostico de complicación
	 * (Completo compuesto por codigo/CIE/nombre, 
	 * separado por "-")
	 */
	private String diagnosticoComplicacion_1="";
	
	/**
	 * Aqui se captura el diagnostico de muerte
	 * (Completo compuesto por codigo/CIE/nombre, 
	 * separado por "-")
	 */
	private String diagnosticoMuerte_1="";
	

	/**
	 * Para manejar los signos vitales
	 */	
	private Map signosVitales = new HashMap();
	
	/**
	 * Define si  los signos vitales de 
	 * la evolución del paciente van o no a la 
	 * epicrisis
	 */
	private boolean signosVitalesBoolean=false;
	
	/**
	 * Número de signos vitales
	 */
	private int numSignosVitales=0;

	/**
	 * Para manejar los diagnosticos presuntivos principal y relacionados. El
	 * valor viene de forma 'codigo- nombre'
	 */
	private Map diagnosticosPresuntivos = new HashMap();
	
	/**
	 * Define si  los diagnosticos presuntivos de 
	 * la evolución del paciente van o no a la 
	 * epicrisis
	 */
	private boolean diagnosticosPresuntivosBoolean=false;
	 
	/**
	 * Entero para saber cuantos diagnosticos presuntivos se 
	 * generaron dinámicamente 
	 */
	private int numDiagnosticosPresuntivos = 0;

	/**
	 * Entero para saber cuantos diagnosticos presuntivos
	 * existían en el objeto (Restricción de adjunto no
	 * puede modificar antiguos)
	 */
	private int numDiagnosticosPresuntivosOriginal = 0;
	
	/**
	 * Entero con el código del tipo de recargo seleccionado
	 * por defecto
	 */
	private int recargo=ConstantesBD.codigoTipoRecargoSinRecargo;

	/**
	 * Para manejar los diagnosticos definitivos principal y relacionados. El
	 * valor viene de forma 'codigo- nombre'
	 */
	private Map diagnosticosDefinitivos = new HashMap();
	
	/**
	 * Define si  los diagnosticos definitivos de 
	 * la evolución del paciente van o no a la 
	 * epicrisis
	 */
	private boolean diagnosticosDefinitivosBoolean=false;

	/**
	 * Entero para saber cuantos diagnosticos definitivos se 
	 * generaron dinámicamente 
	 */
	private int numDiagnosticosDefinitivos = 0;

	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private String estado = "";

	/**
	 * String que dice si se le puede o no dar orden de salida a este
	 * paciente (solo si el usuario es del centro de costo tratante).
	 * Se usa como String para manejarlo con logic:equals
	 */
	private String puedeDarOrdenSalida="false";
	
	/**
	 * Booleanq que me indica si el médico es tratante  o no
	 */
	private boolean esTratante=false;
	
	/**
	 * Boolean donde el médico guarda si la admisión es o no cobrable
	 */
	private boolean esCobrable=false;	

	/**
	 * String donde se almacena el dato de si se va a enviar todo, nada
	 * o lo seleccionado a la epicrisis (No es necesario en el mundo pero
	 * para que permanezca su valor en caso de error - volver misma pag)
	 */
	private String opcionesAEnviar="seleccionado";

	/**
	 * Indica si se ha dado o no la orden de salida al paciente (Para
	 * manejo checkbox).
	 */
	private String ordenSalida="false";

	/**
	 * Incluso si el médico está autorizado a dar orden de salida
	 * en ocasiones debe mostrar un warning en este espacio para
	 * si hacen falta evoluciones. Si esta en "", se muestra la opcion
	 * de orden de salida 
	 */
	
	private String warningOrdenSalida="";
	
	/**
	 * warnings de las referencias internas y externas
	 */
	private String warningReferencias="";
	

	/**
	 * Código del destino de salida en caso que el usuario 
	 * quiera (y pueda) dar la orden de salida. Empieza en
	 * -1 por el seleccione
	 */
	private int destinoSalida=-1;

	/**
	 * Si el usuario especifica un destino de salida "otros"
	 * en este campo se guarda la descripción del caso
	 */
	private String otroDestinoSalida="";

	/**
	 * En este campo se guarda el motivo de la reversión
	 * del egreso si esta es la primera evolución después
	 * de la reversión
	 */
	private String motivoReversionEgreso="";
	
	/**
	 * Define si el motivo de reversion de Egreso va a la
	 * epicrisis 
	 */
	private boolean motivoReversionEgresoBoolean=false;

	/**
	 * En este campo se dice se especifica si en la presentación
	 * debe aparecer el campo de regresión del egreso
	 */
	private String debeDarMotivoRegresionEgreso="false";

	/**
	 * En este campo se le especifica a la forma en que modo
	 * se encuentra, insertar, modificar o resumen
	 */
	private String modoPermitido="resumen";

	/**
	 * En este campo se guardar el valor de la muerte
	 * empieza en "nodefinido" 
	 */
	private String muerte="";

	/**
	 * Este campo se usa al momento de modificar, donde
	 * se debe especificar si el usuario puede añadir y quitar
	 * checkbox de la epicrisis o solo añadir
	 */
	private String soloAnadir="true";

	/**
	 * Este campo especifica la fecha de creación 
	 * de la cuenta y/o la admisión
	 */
	private String fechaCuentaOAdmision="01/01/1980";

	/**
	 * Este campo define si nos encontramos en una 
	 * admisión o no
	 */
	private boolean estamosEnAdmision=false;

	/**
	 * Este campo permite recordar, despuès de guardar 
	 * o modificar una evolución la que se debe mostrar
	 * en el resumen
	 */
	private int numeroSolicitud=0;

	/**
	 * Código de la valoración con la que se asociará esta
	 * evolución 
	 */
	private int codigoValoracion=-1;

	/**
	 * Fecha máxima de la evolución 
	 * (La mayor fecha de evolución entre
	 * todas las evoluciones existentes en
	 * esta cuenta)
	 */
	private String fechaMaximaEvolucionFormatoBD="";

	/**
	 * Fecha máxima de la valoración 
	 * (La mayor fecha de valoración entre
	 * todas las valoraciones existentes en
	 * esta cuenta)
	 */
	private String fechaMaximaValoracionFormatoBD="";

	/**
	 * Hora máxima de la evolución, en la
	 * fecha máxima 
	 * (La mayor hora de evolución entre
	 * todas las evoluciones existentes en
	 * esta cuenta)
	 */
	private String horaMaximaEvolucion="";

	/**
	 * Hora máxima de la valoración, en la
	 * fecha máxima 
	 * (La mayor hora de valoración entre
	 * todas las valoraciones existentes en
	 * esta cuenta)
	 */
	private String horaMaximaValoracion="";

	/**
	 * Fecha en la que se realizó la primera valoración
	 * de esta cuenta en formato de BD
	 */
	private String fechaPrimeraValoracionFormatoBD="";
	
	/**
	 * Hora en la que se realizó la primera valoración
	 * de esta cuenta 
	 */
	private String horaPrimeraValoracion="";

	/**
	 * Atributo que especifica si el médico que esta 
	 * accediendo es adjunto y por consiguiente,
	 * si se debe mostrar el mensaje de finalización
	 */
	private boolean esAdjunto;
	
	/**
	 * Variable que me indica si puedo finalizar manejo
	 */
	private boolean puedoFinalizarManejo;
	
	/**
	 * Boolean que indica (para el caso en que
	 * tieneSolicitudCambioManejoTratante sea 
	 * mayor que 0) si el usuario se quiere volver 
	 * tratante, definitivamente no quiere o si 
	 * todavía no se ha hecho una idea (Necesita
	 * más evoluciones / examenes proc?)
	 */
	private char convertirMedicoATratante;
	
	/**
	 * Atributo que indica el número de la solicitud
	 * que piedio convertir a médico tratante, si no
	 * se pidio, este valor es menor que 1
	 */
	private int numeroSolicitudPidioConvertirMedicoATratante;
	
	/**
	 * Boolean que indica si se desea finalizar la
	 * atención para el caso de un médico adjunto
	 */
	private boolean deseaFinalizarAtencion;
	
	/**
	 * String donde se guardar la nota de finalización
	 * de la atención
	 */
	private String notaFinalizacionAtencion;
	
	/**
	 * Boolean que dice si debo mostrar el mensaje
	 * de cancelación de tratante al ingresar una
	 * evolución
	 */
	private boolean deboMostrarMensajeCancelacionTratante=false;
	
	/**
	 * Mapa para manejar campos cuyo número no
	 * se conoce de antemano (En este caso los 
	 * diagnosticos)
	 */
	private  HashMap camposParametrizablesMap = new HashMap();
	
	/**
	 * Manejo de impresión de diagnósticos
	 */
	private boolean imprimirDiagnosticos;
	
	/**
	 * Tipo de diagnostico
	 */
	private int tipoDiagnosticoPrincipal;
	
	/**
	 * Manejo del balance de liquidos
	 */
	private HashMap mapaBalanceLiquidos;
	
	/**
	 * atributo que indica el codigo de la ficha que recien se ingreso.
	 */
	private int codigoFicha=0;
	/**
	 * atributo que indica cual ficha de vigilancia epidemiologica debe abrirse de acuerdo al diagnostico
	 */
	private int codigoEnfermedadNotificable=0;
	
	/**
	 * atributo que indica cual es el codigo del diagnostico (CIE-10) para epidemiologia
	 */
	private String codigoDiagnostico="";

	  /**
     * Mapa para almacenar la informacion histórica de dieta de Registro de Enfermeria.
     */
    private HashMap mapaDietaHistorico;
    
    /**
     * Mapa para almacenar la informacion de dieta, La informacion parametrizada.
     */
    private HashMap mapaDieta;   
    
    /**
     * 
     */
    private String fechaMuerte;
    
    /**
     * 
     */
    private String horaMuerte;
    
    /**
     * Indicador para mensaje de Asociacion de reingreso con ingreso anterior
     */
    private int indI;
    
    /**
     * Mapa para manejar el Mensaje de Egresos
     */
    private HashMap mapaE;
    
    /**
     * 
     */
    private String certificadoDefuncion;
    
    /**
     * Mapa para almacenar las especialidades que posee la cuenta del paciente
     */
    private HashMap especialidadesCuentaMapa;
    
	
	public void setCamposParametrizablesMap(String key, Object value) 
	{
		camposParametrizablesMap.put(key, value);
	}
	
//	---------------------------SIGNOS VITALES 24 HORAS-----------------------------------------------//
    /**
     * Coleccion para traer el Listado de tipos Signos Vitales parametrizados en enfermer?a
     * por centro de costo instituci?n    
     */
    private Collection signosVitalesInstitucionCcosto;
    
    /**
     * Coleccion para traer el listado hist?rico de los signos vitales fijos de las
     * ?ltimas 24 horas
     */
    private Collection signosVitalesFijosHisto;
    
    /**
     * Collecci?n para traer el listado hist?rico de los signos vitales parametrizados por 
     * instituci?n centro de costo de las ?ltimas 24 horas
     */
    private Collection signosVitalesParamHisto;
    
    /**
     * Collecci?n para traer el listado con los c?digos hist?ricos, fecha registro y hora registro,
     * de los signos vitales fijos y parametrizados de las ?ltimas 24 horas  
     */
    private Collection signosVitalesHistoTodos;
	
    /**
     * codigo de la consulta a seguir
     */
    private int codigoConductaSeguir;
    
    /**
     * si la conducta a seguir es remitir
     * este atributo se llena
     */
    private String acronimoTipoReferencia;
    
    /**
     * mapa con las conductas a seguir 
     */
    private HashMap conductasSeguirTagMap;
    
    /**
     * Variable para llevar el valor de la comparacion de las especialidades
     */
    private boolean controlInterpretacion;
    
    
	/**
	 * Retorna el valor de un campo dado su nombre
	 */
	public Object getCamposParametrizablesMap(String key) 
	{
		return camposParametrizablesMap.get(key);
	}
	/**
	 * @return
	 */
	public HashMap getCamposParametrizablesMapaCompleto()
	{
		return camposParametrizablesMap;
	}
	
	/**
	 * Campo para saber si se debe ocultar el encabezado
	 */
	private boolean ocultarEncabezado;
	


	/**
	 * Reset NO estandar, para limpiar al terminar todo el proceso, NO al
	 * cambiar de página. Limpia T ODOS los datos menos el estado
	 */
	
	public void reset ()
	{
		//Siempre se sugiere la fecha del sistema
		String fechaYHoraSugeridas[]=this.calcularFechaYHoraActual();
		this.fechaEvolucion=fechaYHoraSugeridas[0];
		this.horaEvolucion=fechaYHoraSugeridas[1];
		this.informacionDadaPaciente="";
		this.informacionDadaPacienteBoolean=false;
		this.hallazgosImportantes="";
		this.procedimientosQuirurgicosYObstetricos="";
		/*
		 * OJO!!
		 * El campo "procedimientosQuirurgicosYObstetricos"
		 * fue reutilizado para el tipo de cuidado
		 * el cual va automajicamente a la epicriis, por eso
		 * el boolean debe ser iniciado en true
		 */
		this.procedimientosQuirurgicosYObstetricosBoolean=true;
		//this.procedimientosQuirurgicosYObstetricosBoolean=false;
		this.fechaResultadosExamenesDiagnostico="";
		this.fechaResultadosExamenesDiagnosticoBoolean=false;
		this.pronostico="";
		this.pronosticoBoolean=false;
		this.hallazgosImportantesBoolean=false;
		this.descripcionComplicacion="";
		this.descripcionComplicacionBoolean=false;
		this.tratamiento="";
		this.tratamientoBoolean=false;
		this.resultadosTratamiento="";
		this.resultadosTratamientoBoolean=false;
		this.cambiosManejo="";
		this.cambiosManejoBoolean=false;
		this.observaciones="";
		this.observacionesBoolean=false;
		this.tipoEvolucion = 'o';
		this.diagnosticoIngreso_1="";
		this.diagnosticoComplicacion_1="";
		this.signosVitalesBoolean=false;
		this.numSignosVitales=0;
		this.diagnosticosPresuntivosBoolean=false;
		this.numDiagnosticosPresuntivos = 0;
		this.numDiagnosticosPresuntivosOriginal = 0;
		this.diagnosticosDefinitivosBoolean=false;
		this.numDiagnosticosDefinitivos = 0;
		this.puedeDarOrdenSalida="false";	
		this.esCobrable=false;	
		this.opcionesAEnviar="seleccionado";
		this.ordenSalida="false";
		this.destinoSalida=-1;
		this.otroDestinoSalida="";
		this.motivoReversionEgreso="";
		this.motivoReversionEgresoBoolean=false;
		this.debeDarMotivoRegresionEgreso="false";
		this.modoPermitido="resumen";
		this.diagnosticosDefinitivos=new HashMap();
		this.diagnosticosDefinitivos.put("numdiagval","0");
		this.diagnosticosPresuntivos=new HashMap();
		this.signosVitales=new HashMap();
		this.diagnosticoMuerte_1="";
		this.muerte="";
		this.soloAnadir="true";
		this.numeroSolicitud=0;
		this.warningOrdenSalida="";
		this.warningReferencias="";
		this.fechaCuentaOAdmision=fechaYHoraSugeridas[0];
		this.estamosEnAdmision=false;
		this.codigoValoracion=-1;
		this.fechaMaximaEvolucionFormatoBD="";
		this.fechaMaximaValoracionFormatoBD="";
		this.horaMaximaEvolucion="";
		this.horaMaximaValoracion="";
		this.fechaPrimeraValoracionFormatoBD="";
		this.horaPrimeraValoracion="";
		this.camposParametrizablesMap=new HashMap();
		this.deseaFinalizarAtencion=false;
		this.esAdjunto=false;
		this.puedoFinalizarManejo=false;
		this.notaFinalizacionAtencion="";

		this.numeroSolicitudPidioConvertirMedicoATratante=0;
		this.convertirMedicoATratante='p';
		this.deboMostrarMensajeCancelacionTratante=false;
		
		this.esTratante=false;
		this.recargo=ConstantesBD.codigoTipoRecargoSinRecargo;
		this.imprimirDiagnosticos=true;
		this.tipoDiagnosticoPrincipal=0;
		this.mapaBalanceLiquidos=new HashMap();
		this.mapaBalanceLiquidos.put("numRegistros","0");
		
		
		//-- Para Sacar la informacion de Dieta de las ultimas 24 Horas. 
		this.mapaDietaHistorico = new HashMap();
		this.mapaDietaHistorico.put("fechaHistoricoDieta","");
		this.mapaDieta = new HashMap();
		this.mapaDieta.put("medNuevos","1");
		
		//------------------SIGNOS VITALES ENFERMERIA-------------------------------//
		this.signosVitalesInstitucionCcosto=new ArrayList();
		this.signosVitalesFijosHisto=new ArrayList();
		this.signosVitalesParamHisto=new ArrayList();
		this.signosVitalesHistoTodos=new ArrayList();
		
		this.fechaMuerte="";
		this.horaMuerte="";
		this.certificadoDefuncion="";
		
		this.codigoConductaSeguir=ConstantesBD.codigoNuncaValido;
		this.acronimoTipoReferencia="";
		this.inicializarTagMap();
		
		this.ocultarEncabezado = false;
		
		this.indI=0;
		this.mapaE=new HashMap();
		mapaE.put("numRegistros", 0);
		this.especialidadesCuentaMapa=new HashMap();
		this.controlInterpretacion=false;
	}
	
	/**
	 * inicializa los tag map
	 *
	 */
	public void inicializarTagMap()
	{
		this.setConductasSeguirTagMap(Evolucion.conductasASeguirMap());
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
		//Antes que nada vamos a revisar que viene en el request.getParameter
		//de todos los campos checkbox, para limpiar el contenido de los mismos

		if (estado!=null&&estado.equals("empezar"))
		{
			this.reset();
		}
		
		
		// Perform validator framework validations
		ActionErrors errors = super.validate(mapping, request);

		//Aqui van las validaciones que solo ocurren al momento
		//que se quiere cambiar algo en la BD y específicamente
		//cuando se quiere insertar 
		if (estado.equals("salir")&&modoPermitido.equals("insertar"))
		{
		    cambiarValoresCheckbox (request);
			
			if (this.codigoValoracion==-1)
			{
				errors.add("codigoValoracionInvalido", new ActionMessage("errors.seleccion", "valoración"));
			}
			
			//Hay que limpiar los diagnosticos seleccionados y que quedaron
			//en null
			
			if ((this.getDiagnosticoDefinitivo("principal").toString().equals("null") || this.getDiagnosticoDefinitivo("principal").toString().equals(""))
				&& (this.getDiagnosticoPresuntivo("principal").toString().equals("null") || this.getDiagnosticoPresuntivo("principal").toString().equals("")))
			{
				errors.add("errors.diagnosticoPrincipal", new ActionMessage("errors.diagnosticoPrincipal", "definitivo"));
			}
			
			//El primero no tiene checkbox
			/*for (j=2;j<=this.numDiagnosticosDefinitivos;j++)
			{
				if (request.getParameter("diagnosticoDefinitivo(checkbox_"+ j + ")")==null)
				{
					this.setDiagnosticoDefinitivo("checkbox_"+ j, null);
				}
			}*/
			
			/*for (j=2;j<=this.numDiagnosticosPresuntivos;j++)
			{
				if (request.getParameter("diagnosticoPresuntivo(checkbox_"+ j + ")")==null)
				{
					this.setDiagnosticoPresuntivo("checkbox_"+ j, "invalido");
				}
			}*/
			

			// Fecha actual y patrón de fecha a utilizar en las validaciones
			final Date fechaActual = new Date();
			final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
			final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd/MM/yyyy:HH:mm");

			boolean tieneErroresFechaYHora=false;
			int comparacion;
	
			//boolean errorHora=false;
			
			//VALIDACION DE LA HORA DE EVOLUCION
			if(this.horaEvolucion.trim().equals(""))
			{
				errors.add("Hora Evolución vacío", new ActionMessage("errors.required","La hora de evolución"));
				tieneErroresFechaYHora=true;
			}
			else if(!UtilidadFecha.validacionHora(this.horaEvolucion).puedoSeguir)
				{
						errors.add("hora de evolución", new ActionMessage("errors.formatoHoraInvalido", " de evolución "+this.horaEvolucion));
						tieneErroresFechaYHora=true;
				}
			
			//VALIDACION DE LA FECHA ORDEN
			if(this.fechaEvolucion.trim().equals(""))
			{	
					errors.add("Fecha Evolución vacio", new ActionMessage("errors.required","La fecha de evolución"));
					tieneErroresFechaYHora=true;
			}
			else if(!UtilidadFecha.validarFecha(this.fechaEvolucion))
				{
					errors.add("fecha evolución", new ActionMessage("errors.formatoFechaInvalido", "de evolución "+this.fechaEvolucion));
					tieneErroresFechaYHora=true;
				}
			
			// Fecha de la evolución
			Date fechaEvolucion = null;
			try 
			{
				fechaEvolucion = dateFormatter.parse(this.fechaEvolucion);
			}	
			catch (java.text.ParseException e) 
			{
				tieneErroresFechaYHora=true;
			}
			
			Date fechaHoraEvolucion = null;
			if (!tieneErroresFechaYHora)
			{
				// Fecha y Hora de la evolución
				try 
				{
					fechaHoraEvolucion = dateTimeFormatter.parse(this.fechaEvolucion + ":" + this.horaEvolucion);
				}	
				catch (java.text.ParseException e) 
				{
					tieneErroresFechaYHora=true;
				}
			}

			if (!tieneErroresFechaYHora)
			{
				//Solo si no tiene errores de fecha, realizamos esta validación
				String fechaEvolucionFormatoBD=UtilidadFecha.conversionFormatoFechaABD(fechaEvolucion);
				comparacion=this.fechaPrimeraValoracionFormatoBD.compareTo(fechaEvolucionFormatoBD);
				//@todo No borrar estas impresiones, necesarias para depuración
				
				
				if (comparacion>0)
				{
					//En este caso la fecha de la valoración inicial es mayor a la de la evolución
					//pero la evolución SOLO se puede crear una vez se tenga valoración
					errors.add("fechaEvolucionMayorValoracionInicial", new ActionMessage("errors.horaSuperiorA", "de la valoración inicial", "de la evolución"));
				}
				else if (comparacion==0)
				{
					//En este caso fueron hechas el mismo día,
					//luego comparamos las horas
					comparacion=this.horaPrimeraValoracion.compareTo(horaEvolucion);
					
					
					if (comparacion>0)
					{
						//Mismo día pero sin embargo la evolución tiene fecha posterior
						//mostramos el error correspondiente
						errors.add("fechaEvolucionMayorValoracionInicial", new ActionMessage("errors.horaSuperiorA", "de la valoración inicial", "de la evolución"));
					}
				}
				// Validar que la fecha ingresada no sea superior a la fecha actual
				if (fechaActual.compareTo(fechaEvolucion) < 0) 
				{
					errors.add("fechaEvolucion", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de evolución", "actual"));
				}
				// Validar que si la fecha ingresada es igual a la fecha actual, la hora ingresada no sea superior a la hora actual
				else if (fechaActual.compareTo(fechaHoraEvolucion) < 0) 
				{
					errors.add("horaEvolucion", new ActionMessage("errors.horaSuperiorA", "de evolución", "actual"));
				}

				Date dateCuenta=null;
				try
				{
					dateCuenta=dateFormatter.parse(this.fechaCuentaOAdmision);
				}
				catch (java.text.ParseException e) 
				{
						errors.add("fechaCuentaParse", new ActionMessage("errors.formatoFechaInvalido", "de apertura de la cuenta"));
				}

				//La fecha de la evolución NO puede ser menor a la fecha de la cuenta
				if (fechaEvolucion.compareTo(dateCuenta)<0)
				{
					if (this.estamosEnAdmision)
					{
						errors.add("fechaCuentaOAdmision", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "de evolución", "de admisión"));
					}
					else
					{
						errors.add("fechaCuentaOAdmision", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "de evolución", "de apertura de la cuenta"));
					}
				}
			}

			if(errors.isEmpty())
			{
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				if(Utilidades.esCamaUciDadaFecha(paciente.getCodigoCuenta()+"", this.getFechaEvolucion(), this.getHoraEvolucion()))
				{
					if(this.procedimientosQuirurgicosYObstetricos.equals("-1"))
					{	
						errors.add("codigoProcedimientosQuirurgicos", new ActionMessage("errors.seleccion", "Tipo de Monitoreo"));
					}	
				}
				else
					this.procedimientosQuirurgicosYObstetricos.equals("");
			}
			
			
			
			if ((this.getDiagnosticoDefinitivo("principal").toString().equals("null") || this.getDiagnosticoDefinitivo("principal").toString().equals(""))
					&& (this.getDiagnosticoPresuntivo("principal").toString().equals("null") || this.getDiagnosticoPresuntivo("principal").toString().equals("")))
			{
				errors.add("sinDiagnosticoPrincipalDefinitivo", new ActionMessage("errors.diagnosticoPrincipal", "definitivo"));
			}

			//Con el siguiente if se está revisando que el usuario
			//escribo algo en la descripción, si lo hace el diagnostico
			//de complicación NO puede estar vacio
			/*
			if (descripcionComplicacion!=null&&!descripcionComplicacion.equals(""))
			{
				if (diagnosticoComplicacion_1==null||diagnosticoComplicacion_1.equals(""))
				{
					errors.add("complicacionSinDiagnostico", new ActionMessage("error.evolucion.complicacionsindiagnostico"));
				}
			}
			*/
			//Ahora vamos a revisar que si podia seleccionar orden de salida Y
			//la seleccionó Y está en la opción de otros, lo obligue a llenar el campo
			//de texto
			if (puedeDarOrdenSalida.equals("true")&&ordenSalida.equals("true")&&destinoSalida==0)
			{
				if (otroDestinoSalida==null||this.otroDestinoSalida.equals(""))
				{
					errors.add("otraOrdenSalidaIncompleta", new ActionMessage("errors.descripcionOtro", "la orden de salida"));
				}
			}
			
			//Si puede dar orden de salida, la dio , el pronóstico debe ser
			//obligatorio
			/*if (puedeDarOrdenSalida.equals("true")&&ordenSalida.equals("true"))
			{
				if (pronostico==null||pronostico.equals(""))
				{
					errors.add("pronosticoRequerido", new ActionMessage("error.evolucion.pronosticoRequerido"));
				}
			}*/
			
			//Ahora vamos a revisar que si hay una descripción de un signo vital hay un
			//valor para el mismo (No se puede comentar sobre pulsaciones sin poner el
			//valor)
			/*for (int i=0;i<numSignosVitales;i++)
			{
				if (getSignoVital( "desc_"+ (i+1) )!=null&&!getSignoVital( "desc_"+ (i+1) ).equals(""))
				{
					//Si la descripción es vacia, el signo vital tambien debe serlo
					//(Debe ser vacio)
					if (getSignoVital("" + (i+1))==null||getSignoVital("" + (i+1)).equals(""))
					{
						errors.add("descSignoVitalSinValor", new ActionMessage("error.evolucion.descsignovitalsinvalor"));
					}
				}
			}*/
			
			//LA CONDUCTA A SEGUIR ES REQUERIDA
			if(this.getCodigoConductaSeguir()==ConstantesBD.codigoNuncaValido)
			{
				errors.add("", new ActionMessage("errors.required", "La Conducta a Seguir"));
			}
			else
			{
				//si seleccion ala conducta a seguir remitir entonces el tipo de referencia es requerida
				if(this.getCodigoConductaSeguir()==ConstantesBD.codigoConductaASeguirRemitirEvolucion)
				{
					if(UtilidadTexto.isEmpty(this.getAcronimoTipoReferencia()))
						errors.add("", new ActionMessage("errors.required", "El tipo de referencia"));
				}
			}
			
			
			
			//Ahora vamos a revisar que si tiene
			//la orden de salida:
			
			if (ordenSalida!=null&&ordenSalida.equals("true"))
			{
				
				//Si hay orden de salida el destino de salida no puede
				//ser ni nulo ni seleccione
				if (this.destinoSalida==-1)
				{
					errors.add("ordenSalidaSinDestinoSalida", new ActionMessage("error.evolucion.ordensalidasindestinosalida"));
				}
				
				//La muerte NO puede ser null
				if (muerte==null||muerte.equals(""))
				{
					errors.add("ordenSalidaSinMuerte", new ActionMessage("error.evolucion.ordensalidasinmuerte"));
				}
				//Si el paciente murio, el diagnostico debe estar lleno
				else if (muerte.equals("true") )
				{
					if (diagnosticoMuerte_1==null ||diagnosticoMuerte_1.equals(""))
					{
						errors.add("muerteSinDiagnostico", new ActionMessage("error.evolucion.muertesindiagnostico"));
					}
					
					if(UtilidadTexto.isEmpty(this.getFechaMuerte()) || UtilidadTexto.isEmpty(this.getHoraMuerte()))
					{
						errors.add("", new ActionMessage("error.evolucion.muerteSinFechaMuerte"));
					}
					else if(!UtilidadFecha.esFechaValidaSegunAp(this.getFechaMuerte()) || !UtilidadFecha.validacionHora(this.getHoraMuerte()).puedoSeguir)
					{
						if(!UtilidadFecha.esFechaValidaSegunAp(this.getFechaMuerte()))
							errors.add("", new ActionMessage("errors.formatoFechaInvalido", "Fecha Muerte "+this.getFechaMuerte()));
						if(!UtilidadFecha.validacionHora(this.getHoraMuerte()).puedoSeguir)
							errors.add("", new ActionMessage("errors.formatoHoraInvalido", "Hora Muerte "+this.getHoraMuerte()));
					}
					//se valida  que la fecha no sea menor a la admision del paciente ni que supere la fecha del sistema
					else
					{
						PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
						try 
						{
							String fechaAdmision= UtilidadFecha.conversionFormatoFechaAAp(UtilidadValidacion.getFechaAdmision(paciente.getCodigoCuenta()));
							String horaAdminsio= UtilidadFecha.convertirHoraACincoCaracteres(UtilidadValidacion.getHoraAdmision(paciente.getCodigoCuenta()));
							
							if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaAdmision, this.getFechaMuerte()))
							{
								errors.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de muerte "+ this.getFechaMuerte()+ " "+this.horaMuerte, "de admisión "+fechaAdmision+" "+horaAdminsio));
							}
							if(this.getFechaMuerte().equals(fechaAdmision))
							{
								String horaMuerteTemp=this.getHoraMuerte().replaceAll(":","");
								String horaAdminTemp= horaAdminsio.replaceAll(":", "");
								
								if(Integer.parseInt(horaMuerteTemp)< Integer.parseInt(horaAdminTemp))
									errors.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de muerte "+ this.getFechaMuerte()+ " "+this.horaMuerte, "de admisión "+ fechaAdmision+" "+horaAdminsio));
							}
							
							if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getFechaMuerte(), UtilidadFecha.getFechaActual()))
							{
								errors.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "actual "+ UtilidadFecha.getFechaActual()+ " "+UtilidadFecha.getHoraActual(), "de muerte "+ this.getFechaMuerte()+" "+this.getHoraMuerte()));
							}
							if(this.getFechaMuerte().equals(UtilidadFecha.getFechaActual()))
							{
								String horaMuerteTemp=this.getHoraMuerte().replaceAll(":","");
								String horaActual= UtilidadFecha.getHoraActual().replaceAll(":", "");
								
								if(Integer.parseInt(horaMuerteTemp)> Integer.parseInt(horaActual))
									errors.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "actual "+ UtilidadFecha.getFechaActual()+ " "+UtilidadFecha.getHoraActual(), "de muerte "+ this.getFechaMuerte()+" "+this.getHoraMuerte()));
							}
						} 
						catch (NumberFormatException e) 
						{
							e.printStackTrace();
						} 
						catch (SQLException e) 
						{
							e.printStackTrace();
						}
						
					}
					if(UtilidadTexto.isEmpty(this.certificadoDefuncion))
					{
						errors.add("", new ActionMessage("errors.required", "Si el paciente esta muerto entonces el certificado de defunción"));
					}
				}
				
				//Siempre que voy a dar orden de salida debo revisar que la hora de la evolución sea
				//mayor a cualquier hora de de valoración. Definimos un boolean, para evitar, que si
				//sale un error de evolucion, tambien aparezca el error de valoracion
				
				boolean yaEncontreErrorFechasMaximas=false;
				
				//Como esta validación involucra fecha y hora, si se llega a presentar
				//un error con estas, NO validamos
				if (!tieneErroresFechaYHora)
				{
					String fechaEvolucionFormatoBD=UtilidadFecha.conversionFormatoFechaABD(this.fechaEvolucion);
					comparacion=0;
					horaMaximaEvolucion=UtilidadFecha.convertirHoraACincoCaracteres(horaMaximaEvolucion);
					
					if (this.fechaMaximaEvolucionFormatoBD!=null&&!this.fechaMaximaEvolucionFormatoBD.equals("") )
					{
						comparacion=fechaMaximaEvolucionFormatoBD.compareTo(fechaEvolucionFormatoBD);
						if (comparacion>0)
						{
							errors.add("fechaORdenSalida-Evoluciones", new ActionMessage("errors.horaSuperiorA", "de alguna/s de la/s evolución/es de esta cuenta", "de orden de salida"));
							yaEncontreErrorFechasMaximas=true;
						}
						else if (comparacion==0)
						{
							
							if (horaMaximaEvolucion.compareTo(horaEvolucion)>0)
							{
								errors.add("fechaORdenSalida-Evoluciones", new ActionMessage("errors.horaSuperiorA", "de alguna/s de la/s evolución/es de esta cuenta", "de orden de salida"));
								yaEncontreErrorFechasMaximas=true;
							}
						}
					}
					
					if (!yaEncontreErrorFechasMaximas&&this.fechaMaximaValoracionFormatoBD!=null&&!this.fechaMaximaValoracionFormatoBD.equals(""))
					{
						comparacion=fechaMaximaValoracionFormatoBD.compareTo(fechaEvolucionFormatoBD);
						
						if (comparacion>0)
						{
							errors.add("fechaOrdenSalida-Valoraciones", new ActionMessage("errors.horaSuperiorA", "de alguna/s de la/s valoración/es de esta cuenta", "de orden de salida"));
						}
						else if (comparacion==0)
						{
							if (horaMaximaValoracion.compareTo(horaEvolucion)>0)
							{
								errors.add("fechaOrdenSalida-Valoraciones", new ActionMessage("errors.horaSuperiorA", "de alguna/s de la/s valoración/es de esta cuenta", "de orden de salida"));
							}
						}
					}

				}
			}
			
		}

		return errors;
	}

	/**
	 * @return
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @return
	 */
	public String getCambiosManejo() {
		return cambiosManejo;
	}

	/**
	 * @return
	 */
	public String getDescripcionComplicacion() {
		return descripcionComplicacion;
	}

	/**
	 * @return
	 */
	public String getFechaEvolucion() {
		return fechaEvolucion;
	}

	/**
	 * @return
	 */
	public String getHoraEvolucion() {
		return horaEvolucion;
	}

	/**
	 * @return
	 */
	public String getInformacionDadaPaciente() {
		return informacionDadaPaciente;
	}

	/**
	 * @return
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @return
	 */
	public String isOrdenSalida() {
		return ordenSalida;
	}

	/**
	 * @return
	 */
	public String getResultadosTratamiento() {
		return resultadosTratamiento;
	}

	/**
	 * @return
	 */
	public char getTipoEvolucion() {
		return tipoEvolucion;
	}

	/**
	 * @return
	 */
	public String getTratamiento() {
		return tratamiento;
	}

	/**
	 * @param string
	 */
	public void setEstado(String string) {
		estado = string;
	}

	/**
	 * @param string
	 */
	public void setCambiosManejo(String string) {
		cambiosManejo = string;
	}

	/**
	 * @param string
	 */
	public void setDescripcionComplicacion(String string) {
		descripcionComplicacion = string;
	}

	/**
	 * @param string
	 */
	public void setFechaEvolucion(String string) {
		fechaEvolucion = string;
	}

	/**
	 * @param string
	 */
	public void setHoraEvolucion(String string) {
		horaEvolucion = string;
	}

	/**
	 * @param string
	 */
	public void setInformacionDadaPaciente(String string) {
		informacionDadaPaciente = string;
	}

	/**
	 * @param string
	 */
	public void setObservaciones(String string) {
		observaciones = string;
	}

	/**
	 * @param b
	 */
	public void setOrdenSalida(String b) {
		ordenSalida = b;
	}

	/**
	 * @param string
	 */
	public void setResultadosTratamiento(String string) {
		resultadosTratamiento = string;
	}

	/**
	 * @param c
	 */
	public void setTipoEvolucion(char c) {
		tipoEvolucion = c;
	}

	/**
	 * @param string
	 */
	public void setTratamiento(String string) {
		tratamiento = string;
	}

	/**
	 * @return
	 */
	public Map getDiagnosticosDefinitivos() {
		return diagnosticosDefinitivos;
	}

	/**
	 * @return
	 */
	public Map getDiagnosticosPresuntivos() {
		return diagnosticosPresuntivos;
	}

	/**
	 * @return
	 */
	public int getNumDiagnosticosDefinitivos() {
		return numDiagnosticosDefinitivos;
	}

	/**
	 * @return
	 */
	public int getNumDiagnosticosPresuntivos() {
		return numDiagnosticosPresuntivos;
	}

	/**
	 * @param i
	 */
	public void setNumDiagnosticosDefinitivos(int i) {
		numDiagnosticosDefinitivos = i;
	}

	/**
	 * @param i
	 */
	public void setNumDiagnosticosPresuntivos(int i) {
		numDiagnosticosPresuntivos = i;
	}

	/**
	 * Asigna un diagnostico presuntivo (ppal o relacionado)
	 */
	public void setDiagnosticoPresuntivo(String key, Object value) 
	{
		diagnosticosPresuntivos.put(key, value);
	}
	
	/**
	 * Retorna el diagnostico presuntivo (ppal o relacionado) asociado a la
	 * llave dada
	 */
	public Object getDiagnosticoPresuntivo(String key) 
	{
		return diagnosticosPresuntivos.get(key);
	}
	
	/**
	 * * Asigna un diagnostico definitivo (ppal o relacionado)
	 */
	public void setDiagnosticoDefinitivo(String key, Object value) 
	{
		diagnosticosDefinitivos.put(key, value);
	}
	
	/**
	 * Retorna el diagnostico definitivo (ppal o relacionado) asociado a la
	 * llave dada
	 */
	public Object getDiagnosticoDefinitivo(String key) 
	{
		return diagnosticosDefinitivos.get(key);
	}	

	/**
	 * @return
	 */
	public String getDiagnosticoComplicacion_1() {
		return diagnosticoComplicacion_1;
	}

	/**
	 * @return
	 */
	public String getDiagnosticoIngreso_1() {
		return diagnosticoIngreso_1;
	}

	/**
	 * @param string
	 */
	public void setDiagnosticoComplicacion_1(String string) {
		diagnosticoComplicacion_1 = string;
	}

	/**
	 * @param string
	 */
	public void setDiagnosticoIngreso_1(String string) {
		diagnosticoIngreso_1 = string;
	}

	/**
	 * Asigna un signo vital nuevo
	 */
	public void setSignoVital(String key, Object value) 
	{
		signosVitales.put(key, value);
	}
	
	/**
	 * Retorna el valor del signo vital dada su llave(codigo)
	 */
	public Object getSignoVital(String key) 
	{
		return signosVitales.get(key);
	}	

	/**
	 * Retorna el número de signos vitales cargados
	 * @return int
	 */
	public int getNumSignosVitales() 
	{
		return numSignosVitales;
	}

	/**
	 * Asigna el número de signos vitales cargados.
	 * @param numSignosVitales The numSignosVitales to set
	 */
	public void setNumSignosVitales(int numSignosVitales) 
	{
		this.numSignosVitales = numSignosVitales;
	}

	/**
	 * @return
	 */
	public String getPuedeDarOrdenSalida() {
		return puedeDarOrdenSalida;
	}

	/**
	 * @param b
	 */
	public void setPuedeDarOrdenSalida(String b) {
		puedeDarOrdenSalida = b;
	}

	/**
	 * @return
	 */
	public boolean isEsCobrable() {
		return esCobrable;
	}

	/**
	 * @param b
	 */
	public void setEsCobrable(boolean b) {
		esCobrable = b;
	}

	/**
	 * @return
	 */
	public boolean isCambiosManejoBoolean() {
		return cambiosManejoBoolean;
	}

	/**
	 * @return
	 */
	public boolean isDescripcionComplicacionBoolean() {
		return descripcionComplicacionBoolean;
	}

	/**
	 * @return
	 */
	public String getHallazgosImportantes() {
		return hallazgosImportantes;
	}

	/**
	 * @return
	 */
	public boolean isObservacionesBoolean() {
		return observacionesBoolean;
	}

	/**
	 * @return
	 */
	public boolean isResultadosTratamientoBoolean() {
		return resultadosTratamientoBoolean;
	}

	/**
	 * @return
	 */
	public boolean isTratamientoBoolean() {
		return tratamientoBoolean;
	}

	/**
	 * @param b
	 */
	public void setCambiosManejoBoolean(boolean b) {
		cambiosManejoBoolean = b;
	}

	/**
	 * @param b
	 */
	public void setDescripcionComplicacionBoolean(boolean b) {
		descripcionComplicacionBoolean = b;
	}

	/**
	 * @param string
	 */
	public void setHallazgosImportantes(String string) {
		hallazgosImportantes = string;
	}

	/**
	 * @param b
	 */
	public void setObservacionesBoolean(boolean b) {
		observacionesBoolean = b;
	}

	/**
	 * @param b
	 */
	public void setResultadosTratamientoBoolean(boolean b) {
		resultadosTratamientoBoolean = b;
	}

	/**
	 * @param b
	 */
	public void setTratamientoBoolean(boolean b) {
		tratamientoBoolean = b;
	}

	/**
	 * @return
	 */
	public String getFechaResultadosExamenesDiagnostico() {
		return fechaResultadosExamenesDiagnostico;
	}

	/**
	 * @return
	 */
	public boolean isFechaResultadosExamenesDiagnosticoBoolean() {
		return fechaResultadosExamenesDiagnosticoBoolean;
	}

	/**
	 * @return
	 */
	public boolean isHallazgosImportantesBoolean() {
		return hallazgosImportantesBoolean;
	}

	/**
	 * @return
	 */
	public boolean isInformacionDadaPacienteBoolean() {
		return informacionDadaPacienteBoolean;
	}

	/**
	 * @return
	 */
	public String getProcedimientosQuirurgicosYObstetricos() {
		return procedimientosQuirurgicosYObstetricos;
	}

	/**
	 * @return
	 */
	public boolean isProcedimientosQuirurgicosYObstetricosBoolean() {
		return procedimientosQuirurgicosYObstetricosBoolean;
	}

	/**
	 * @return
	 */
	public String getPronostico() {
		return pronostico;
	}

	/**
	 * @return
	 */
	public boolean isPronosticoBoolean() {
		return pronosticoBoolean;
	}

	/**
	 * @return
	 */
	public boolean isSignosVitalesBoolean() {
		return signosVitalesBoolean;
	}

	/**
	 * @param string
	 */
	public void setFechaResultadosExamenesDiagnostico(String string) {
		fechaResultadosExamenesDiagnostico = string;
	}

	/**
	 * @param b
	 */
	public void setFechaResultadosExamenesDiagnosticoBoolean(boolean b) {
		fechaResultadosExamenesDiagnosticoBoolean = b;
	}

	/**
	 * @param b
	 */
	public void setHallazgosImportantesBoolean(boolean b) {
		hallazgosImportantesBoolean = b;
	}

	/**
	 * @param b
	 */
	public void setInformacionDadaPacienteBoolean(boolean b) {
		informacionDadaPacienteBoolean = b;
	}

	/**
	 * @param string
	 */
	public void setProcedimientosQuirurgicosYObstetricos(String string) {
		procedimientosQuirurgicosYObstetricos = string;
	}

	/**
	 * @param b
	 */
	public void setProcedimientosQuirurgicosYObstetricosBoolean(boolean b) {
		procedimientosQuirurgicosYObstetricosBoolean = b;
	}

	/**
	 * @param string
	 */
	public void setPronostico(String string) {
		pronostico = string;
	}

	/**
	 * @param b
	 */
	public void setPronosticoBoolean(boolean b) {
		pronosticoBoolean = b;
	}

	/**
	 * @return
	 */
	public String getOpcionesAEnviar() {
		return opcionesAEnviar;
	}

	/**
	 * @param string
	 */
	public void setOpcionesAEnviar(String string) {
		opcionesAEnviar = string;
	}

	/**
	 * @return
	 */
	public boolean isDiagnosticosDefinitivosBoolean() {
		return diagnosticosDefinitivosBoolean;
	}

	/**
	 * @return
	 */
	public boolean isDiagnosticosPresuntivosBoolean() {
		return diagnosticosPresuntivosBoolean;
	}

	/**
	 * @param b
	 */
	public void setDiagnosticosDefinitivosBoolean(boolean b) {
		diagnosticosDefinitivosBoolean = b;
	}

	/**
	 * @param b
	 */
	public void setDiagnosticosPresuntivosBoolean(boolean b) {
		diagnosticosPresuntivosBoolean = b;
	}

	/**
	 * @param b
	 */
	public void setSignosVitalesBoolean(boolean b) {
		signosVitalesBoolean = b;
	}

	/**
	 * @return
	 */
	public String getOrdenSalida() {
		return ordenSalida;
	}

	/**
	 * @return
	 */
	public int getDestinoSalida() {
		return destinoSalida;
	}

	/**
	 * @param i
	 */
	public void setDestinoSalida(int i) {
		destinoSalida = i;
	}

	/**
	 * @return
	 */
	public String getOtroDestinoSalida() {
		return otroDestinoSalida;
	}

	/**
	 * @param string
	 */
	public void setOtroDestinoSalida(String string) {
		otroDestinoSalida = string;
	}

	/**
	 * @return
	 */
	public String getDebeDarMotivoRegresionEgreso() {
		return debeDarMotivoRegresionEgreso;
	}

	/**
	 * @return
	 */
	public String getMotivoReversionEgreso() {
		return motivoReversionEgreso;
	}

	/**
	 * @param string
	 */
	public void setDebeDarMotivoRegresionEgreso(String string) {
		debeDarMotivoRegresionEgreso = string;
	}

	/**
	 * @param string
	 */
	public void setMotivoReversionEgreso(String string) {
		motivoReversionEgreso = string;
	}
	
	public String[] calcularFechaYHoraActual ()
	{
		String arreglo[]=new String[2];

		GregorianCalendar calendar = new GregorianCalendar(new SimpleTimeZone(-18000000, "America/Bogota"));

		int anioAct = calendar.get(Calendar.YEAR);
		int mesAct  = calendar.get(Calendar.MONTH)+1;
		String mesAct2 = (new Integer(mesAct)).toString();	
		mesAct2 = (mesAct2.length() < 2) ? "0"+mesAct2 : mesAct2;	
		int diaAct  = calendar.get(Calendar.DAY_OF_MONTH);
		String diaAct2 = (new Integer(diaAct)).toString();
		diaAct2 = (diaAct2.length() < 2) ? "0"+diaAct2 : diaAct2;	
		String minute = calendar.get(Calendar.MINUTE) + "";
		minute = (minute.length() < 2) ? "0"+minute : minute;
		String hour = calendar.get(Calendar.HOUR_OF_DAY) + "";
		hour = (hour.length() < 2) ? "0"+hour : hour;
		String horaAct = hour + ":" + minute;

		//Fecha
		arreglo[0]=diaAct2+ "/" + mesAct2 + "/" + anioAct;
		//Hora
		arreglo[1]=horaAct;
		
		return arreglo; 
	}

	/**
	 * @return
	 */
	public String getModoPermitido() {
		return modoPermitido;
	}

	/**
	 * @param string
	 */
	public void setModoPermitido(String string) {
		modoPermitido = string;
	}

	/**
	 * Método que cambia los valores de los datos que en el jsp se 
	 * manejan como checkbox, dependiendo si fueron 
	 * seleccionados o no
	 * 
	 * @param request El request de la aplicación
	 */
	public void cambiarValoresCheckbox (HttpServletRequest request)
	{
		if (request.getParameter("esCobrable")==null)
		{
			esCobrable=false;
		}
		else
		{
			esCobrable=true;
		}
		
		//Ahora cambiamos los valores propios a la hospitalización
		//(Todos los checkbox que se envian a la epicrisis)
		cambiarValoresCheckboxHospitalizacion(request);
	}

	/**
	 * Método que cambia los valores de los datos que en el jsp se 
	 * manejan como checkbox, dependiendo si fueron 
	 * seleccionados o no. Es de hospitalización porque solo cambia
	 * los checkbox que definen lo que se va a mostrar en la epicrisis
	 * que solo aplica para los hospitalizados
	 * 
	 * @param request El request de la aplicación
	 */
	public void cambiarValoresCheckboxHospitalizacion (HttpServletRequest request)
	{
		
		//Signos Vitales
		if (request.getParameter("signosVitalesBoolean")==null)
		{
			signosVitalesBoolean=false;
		}
		else
		{
			signosVitalesBoolean=true;
		}
		
		//Diagnosticos Presuntivos
		if (request.getParameter("diagnosticosPresuntivosBoolean")==null)
		{
			diagnosticosPresuntivosBoolean=false;
		}
		else
		{
			diagnosticosPresuntivosBoolean=true;
		}
		//Diagnosticos Definitivos
		if (request.getParameter("diagnosticosDefinitivosBoolean")==null)
		{
			diagnosticosDefinitivosBoolean=false;
		}
		else
		{
			diagnosticosDefinitivosBoolean=true;
		}
		//Información dada por el paciente
		if (request.getParameter("informacionDadaPacienteBoolean")==null)
		{
			informacionDadaPacienteBoolean=false;
		}
		else
		{
			informacionDadaPacienteBoolean=true;
		}
		
		//Hallazgos Importantes
		if (request.getParameter("hallazgosImportantesBoolean")==null)
		{
			hallazgosImportantesBoolean=false;
		}
		else
		{
			hallazgosImportantesBoolean=true;
		}
		//Proc. Quirurgicos y Obstetricos
		if (request.getParameter("procedimientosQuirurgicosYObstetricosBoolean")==null)
		{
			procedimientosQuirurgicosYObstetricosBoolean=false;
		}
		else
		{
			procedimientosQuirurgicosYObstetricosBoolean=true;
		}
		//Fecha y res. Examenes Diagnostico
		if (request.getParameter("fechaResultadosExamenesDiagnosticoBoolean")==null)
		{
			fechaResultadosExamenesDiagnosticoBoolean=false;
		}
		else
		{
			fechaResultadosExamenesDiagnosticoBoolean=true;
		}
		//Descripción Complicacion
		if (request.getParameter("descripcionComplicacionBoolean")==null)
		{
			descripcionComplicacionBoolean=false;
		}
		else
		{
			descripcionComplicacionBoolean=true;
		}
		//Tratamiento
		if (request.getParameter("tratamientoBoolean")==null)
		{
			tratamientoBoolean=false;
		}
		else
		{
			tratamientoBoolean=true;
		}
		//Resultados Tratamiento
		if (request.getParameter("resultadosTratamientoBoolean")==null)
		{
			resultadosTratamientoBoolean=false;
		}
		else
		{
			resultadosTratamientoBoolean=true;
		}
		//Cambios Manejo Paciente
		if (request.getParameter("cambiosManejoBoolean")==null)
		{
			cambiosManejoBoolean=false;
		}
		else
		{
			cambiosManejoBoolean=true;
		}
		//Pronostico
		if (request.getParameter("pronosticoBoolean")==null)
		{
			pronosticoBoolean=false;
		}
		else
		{
			pronosticoBoolean=true;
		}
		//Observaciones
		if (request.getParameter("observacionesBoolean")==null)
		{
			observacionesBoolean=false;
		}
		else
		{
			observacionesBoolean=true;
		}
		
		//Motivo de Reversion
		if (request.getParameter("motivoReversionEgresoBoolean")==null)
		{
			motivoReversionEgresoBoolean=false;
		}
		else
		{
			motivoReversionEgresoBoolean=true;
		}
		
	}
	/**
	 * @return
	 */
	public String getDiagnosticoMuerte_1() {
		return diagnosticoMuerte_1;
	}

	/**
	 * @param string
	 */
	public void setDiagnosticoMuerte_1(String string) {
		diagnosticoMuerte_1 = string;
	}

	/**
	 * @return
	 */
	public String getMuerte() {
		return muerte;
	}

	/**
	 * @param string
	 */
	public void setMuerte(String string) {
		muerte = string;
	}

	/**
	 * @return
	 */
	public String getSoloAnadir() {
		return soloAnadir;
	}

	/**
	 * @param string
	 */
	public void setSoloAnadir(String string) {
		soloAnadir = string;
	}

	/**
	 * @return
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @param i
	 */
	public void setNumeroSolicitud(int i) {
		numeroSolicitud = i;
	}

	/**
	 * @return
	 */
	public String getWarningOrdenSalida() {
		return warningOrdenSalida;
	}

	/**
	 * @param string
	 */
	public void setWarningOrdenSalida(String string) {
		warningOrdenSalida = string;
	}

	/**
	 * @return
	 */
	public String getFechaCuentaOAdmision() {
		return fechaCuentaOAdmision;
	}

	/**
	 * @param string
	 */
	public void setFechaCuentaOAdmision(String string) {
		fechaCuentaOAdmision = string;
	}

	/**
	 * @return
	 */
	public boolean isEstamosEnAdmision() {
		return estamosEnAdmision;
	}

	/**
	 * @param b
	 */
	public void setEstamosEnAdmision(boolean b) {
		estamosEnAdmision = b;
	}

	/**
	 * @return
	 */
	public boolean isMotivoReversionEgresoBoolean() {
		return motivoReversionEgresoBoolean;
	}

	/**
	 * @param b
	 */
	public void setMotivoReversionEgresoBoolean(boolean b) {
		motivoReversionEgresoBoolean = b;
	}

	/**
	 * @return
	 */
	public int getCodigoValoracion() {
		return codigoValoracion;
	}

	/**
	 * @param i
	 */
	public void setCodigoValoracion(int i) {
		codigoValoracion = i;
	}

	/**
	 * @return
	 */
	public String getFechaMaximaEvolucionFormatoBD()
	{
		return fechaMaximaEvolucionFormatoBD;
	}

	/**
	 * @return
	 */
	public String getFechaMaximaValoracionFormatoBD()
	{
		return fechaMaximaValoracionFormatoBD;
	}

	/**
	 * @return
	 */
	public String getHoraMaximaEvolucion()
	{
		return horaMaximaEvolucion;
	}

	/**
	 * @return
	 */
	public String getHoraMaximaValoracion()
	{
		return horaMaximaValoracion;
	}

	/**
	 * @param string
	 */
	public void setFechaMaximaEvolucionFormatoBD(String string)
	{
		fechaMaximaEvolucionFormatoBD = string;
	}

	/**
	 * @param string
	 */
	public void setFechaMaximaValoracionFormatoBD(String string)
	{
		fechaMaximaValoracionFormatoBD = string;
	}

	/**
	 * @param string
	 */
	public void setHoraMaximaEvolucion(String string)
	{
		horaMaximaEvolucion = string;
	}

	/**
	 * @param string
	 */
	public void setHoraMaximaValoracion(String string)
	{
		horaMaximaValoracion = string;
	}

	/**
	 * @return
	 */
	public String getFechaPrimeraValoracionFormatoBD()
	{
		return fechaPrimeraValoracionFormatoBD;
	}

	/**
	 * @param string
	 */
	public void setFechaPrimeraValoracionFormatoBD(String string)
	{
		fechaPrimeraValoracionFormatoBD = string;
	}

	/**
	 * @return
	 */
	public String getHoraPrimeraValoracion()
	{
		return horaPrimeraValoracion;
	}

	/**
	 * @param string
	 */
	public void setHoraPrimeraValoracion(String string)
	{
		horaPrimeraValoracion = string;
	}

	/**
	 * @return
	 */
	public boolean getEsAdjunto() {
		return esAdjunto;
	}

	/**
	 * @param b
	 */
	public void setEsAdjunto(boolean b) {
		esAdjunto = b;
	}

	/**
	 * @return
	 */
	public boolean getDeseaFinalizarAtencion() {
		return deseaFinalizarAtencion;
	}

	/**
	 * @param b
	 */
	public void setDeseaFinalizarAtencion(boolean b) {
		deseaFinalizarAtencion = b;
	}

	/**
	 * @return
	 */
	public String getNotaFinalizacionAtencion() {
		return notaFinalizacionAtencion;
	}

	/**
	 * @param string
	 */
	public void setNotaFinalizacionAtencion(String string) {
		notaFinalizacionAtencion = string;
	}

	/**
	 * @return
	 */
	public char getConvertirMedicoATratante() {
		return convertirMedicoATratante;
	}

	/**
	 * @param b
	 */
	public void setConvertirMedicoATratante(char b) {
		convertirMedicoATratante = b;
	}

	/**
	 * @return
	 */
	public int getNumeroSolicitudPidioConvertirMedicoATratante() {
		return numeroSolicitudPidioConvertirMedicoATratante;
	}

	/**
	 * @param i
	 */
	public void setNumeroSolicitudPidioConvertirMedicoATratante(int i) {
		numeroSolicitudPidioConvertirMedicoATratante = i;
	}

    /**
     * @return Returns the deboMostrarMensajeCancelacionTratante.
     */
    public boolean getDeboMostrarMensajeCancelacionTratante()
    {
        return deboMostrarMensajeCancelacionTratante;
    }
    /**
     * @param deboMostrarMensajeCancelacionTratante The deboMostrarMensajeCancelacionTratante to set.
     */
    public void setDeboMostrarMensajeCancelacionTratante(
            boolean deboMostrarMensajeCancelacionTratante)
    {
        this.deboMostrarMensajeCancelacionTratante = deboMostrarMensajeCancelacionTratante;
    }
    /**
     * @return Returns the esTratante.
     */
    public boolean getEsTratante()
    {
        return esTratante;
    }
    /**
     * @param esTratante The esTratante to set.
     */
    public void setEsTratante(boolean esTratante)
    {
        this.esTratante = esTratante;
    }
    
    /**
     * Método que me dice si debo mostrar la sección
     * de diagnosticos Relacionados
     * @return
     */
    public boolean getDeboMostrarSeccionDiagnosticosDefinitivosRelacionados()
    {
    	/*
    	 * El adjunto también debe ver esta sección
    	 */
        if (/*this.esTratante||*/this.getNumDiagnosticosDefinitivos()>1)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    /**
     * @return Returns the numDiagnosticosPresuntivosOriginal.
     */
    public int getNumDiagnosticosPresuntivosOriginal()
    {
        return numDiagnosticosPresuntivosOriginal;
    }
    /**
     * @param numDiagnosticosPresuntivosOriginal The numDiagnosticosPresuntivosOriginal to set.
     */
    public void setNumDiagnosticosPresuntivosOriginal(
            int numDiagnosticosPresuntivosOriginal)
    {
        this.numDiagnosticosPresuntivosOriginal = numDiagnosticosPresuntivosOriginal;
    }
    /**
     * @return Returns the recargo.
     */
    public int getRecargo()
    {
        return recargo;
    }
    /**
     * @param recargo The recargo to set.
     */
    public void setRecargo(int recargo)
    {
        this.recargo = recargo;
    }
	/**
	 * @return Retorna imprimirDiagnosticos.
	 */
	public boolean getImprimirDiagnosticos()
	{
		return imprimirDiagnosticos;
	}
	/**
	 * @param imprimirDiagnosticos Asigna imprimirDiagnosticos.
	 */
	public void setImprimirDiagnosticos(boolean imprimirDiagnosticos)
	{
		this.imprimirDiagnosticos = imprimirDiagnosticos;
	}
	/**
	 * @return Retorna tipoDiagnosticoPrincipal.
	 */
	public int getTipoDiagnosticoPrincipal()
	{
		return tipoDiagnosticoPrincipal;
	}
	/**
	 * @param tipoDiagnosticoPrincipal Asigna tipoDiagnosticoPrincipal.
	 */
	public void setTipoDiagnosticoPrincipal(int tipoDiagnosticoPrincipal)
	{
		this.tipoDiagnosticoPrincipal = tipoDiagnosticoPrincipal;
	}


	/**
	 * @return Retorna mapaBalanceLiquidos.
	 */
	public HashMap getMapaBalanceLiquidos()
	{
		return mapaBalanceLiquidos;
	}


	/**
	 * @param mapaBalanceLiquidos Asigna mapaBalanceLiquidos.
	 */
	public void setMapaBalanceLiquidos(HashMap mapaBalanceLiquidos)
	{
		this.mapaBalanceLiquidos = mapaBalanceLiquidos;
	}
	public boolean isPuedoFinalizarManejo() {
		return puedoFinalizarManejo;
	}
	public void setPuedoFinalizarManejo(boolean puedoFinalizarManejo) {
		this.puedoFinalizarManejo = puedoFinalizarManejo;
	}
	public String getCodigoDiagnostico() {
		return codigoDiagnostico;
	}
	public void setCodigoDiagnostico(String codigoDiagnostico) {
		this.codigoDiagnostico = codigoDiagnostico;
	}
	public int getCodigoEnfermedadNotificable() {
		return codigoEnfermedadNotificable;
	}
	public void setCodigoEnfermedadNotificable(int codigoEnfermedadNotificable) {
		this.codigoEnfermedadNotificable = codigoEnfermedadNotificable;
	}
	public int getCodigoFicha() {
		return codigoFicha;
	}
	public void setCodigoFicha(int codigoFicha) {
		this.codigoFicha = codigoFicha;
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
	 * @return the fechaMuerte
	 */
	public String getFechaMuerte() {
		return fechaMuerte;
	}
	/**
	 * @param fechaMuerte the fechaMuerte to set
	 */
	public void setFechaMuerte(String fechaMuerte) {
		this.fechaMuerte = fechaMuerte;
	}
	/**
	 * @return the horaMuerte
	 */
	public String getHoraMuerte() {
		return horaMuerte;
	}
	/**
	 * @param horaMuerte the horaMuerte to set
	 */
	public void setHoraMuerte(String horaMuerte) {
		this.horaMuerte = horaMuerte;
	}
	/**
	 * @return the certificadoDefuncion
	 */
	public String getCertificadoDefuncion() {
		return certificadoDefuncion;
	}
	/**
	 * @param certificadoDefuncion the certificadoDefuncion to set
	 */
	public void setCertificadoDefuncion(String certificadoDefuncion) {
		this.certificadoDefuncion = certificadoDefuncion;
	}
	/**
	 * @return the acronimoTipoReferencia
	 */
	public String getAcronimoTipoReferencia() {
		return acronimoTipoReferencia;
	}
	/**
	 * @param acronimoTipoReferencia the acronimoTipoReferencia to set
	 */
	public void setAcronimoTipoReferencia(String acronimoTipoReferencia) {
		this.acronimoTipoReferencia = acronimoTipoReferencia;
	}
	/**
	 * @return the codigoConductaSeguir
	 */
	public int getCodigoConductaSeguir() {
		return codigoConductaSeguir;
	}
	/**
	 * @param codigoConductaSeguir the codigoConductaSeguir to set
	 */
	public void setCodigoConductaSeguir(int codigoConductaSeguir) {
		this.codigoConductaSeguir = codigoConductaSeguir;
	}
	/**
	 * @return the conductasSeguirTagMap
	 */
	public HashMap getConductasSeguirTagMap() {
		return conductasSeguirTagMap;
	}
	/**
	 * @param conductasSeguirTagMap the conductasSeguirTagMap to set
	 */
	public void setConductasSeguirTagMap(HashMap conductasSeguirTagMap) {
		this.conductasSeguirTagMap = conductasSeguirTagMap;
	}
	/**
	 * @return the conductasSeguirTagMap
	 */
	public Object getConductasSeguirTagMap(Object key) {
		return conductasSeguirTagMap.get(key);
	}
	/**
	 * @return the warningReferencias
	 */
	public String getWarningReferencias() {
		return warningReferencias;
	}
	/**
	 * @param warningReferencias the warningReferencias to set
	 */
	public void setWarningReferencias(String warningReferencias) {
		this.warningReferencias = warningReferencias;
	}
	/**
	 * @return the ocultarEncabezado
	 */
	public boolean isOcultarEncabezado() {
		return ocultarEncabezado;
	}
	/**
	 * @param ocultarEncabezado the ocultarEncabezado to set
	 */
	public void setOcultarEncabezado(boolean ocultarEncabezado) {
		this.ocultarEncabezado = ocultarEncabezado;
	}
	public int getIndI() {
		return indI;
	}
	public void setIndI(int indI) {
		this.indI = indI;
	}
	public HashMap getMapaE() {
		return mapaE;
	}
	public void setMapaE(HashMap mapaE) {
		this.mapaE = mapaE;
	}
	
	public Object getMapaE(String key) {
		return mapaE.get(key);
	}

	public void setMapaE(String key, Object value) {
		this.mapaE.put(key, value);
	}
	
	
	public HashMap getEspecialidadesCuentaMapa() {
		return especialidadesCuentaMapa;
	}
	public void setEspecialidadesCuentaMapa(HashMap especialidadesCuentaMapa) {
		this.especialidadesCuentaMapa = especialidadesCuentaMapa;
	}
	
	public void setEspecialidadesCuentaMapa(String key, Object value) {
		this.especialidadesCuentaMapa.put(key, value);
	}
	public boolean isControlInterpretacion() {
		return controlInterpretacion;
	}
	public void setControlInterpretacion(boolean controlInterpretacion) {
		this.controlInterpretacion = controlInterpretacion;
	}
	
	
}
