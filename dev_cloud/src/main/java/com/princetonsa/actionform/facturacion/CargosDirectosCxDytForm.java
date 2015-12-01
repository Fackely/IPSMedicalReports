/*
 * Marzo 25, 2008
 */
package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ElementoApResource;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.facturacion.InfoResponsableCobertura;

import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;

/**
 * @author Sebastián Gómez R
 *
 * Clase que almacena y carga la información utilizada para la funcionalidad
 * Cargos Directos x cirugias y dyt
 */
public class CargosDirectosCxDytForm extends ValidatorForm 
{
	
	Logger logger = Logger.getLogger(CargosDirectosCxDytForm.class);
	
	/**
	 * Variable usada para almacenar el estado del controlador
	 */
	private String estado;
	
	/**
	 * Id de la cuenta
	 */
	private String idCuenta;
	
	/**
	 * Número de la solicitud
	 */
	private String numeroSolicitud;
	/**
	 * Imprimir
	 */
	private boolean imprimir;
	
	//Atributos para realizar validaciones
	private String fechaIngreso;
	private String horaIngreso;
	private String fechaSistema;
	private String horaSistema;
	
	/**
	 * Mapa para mensajes Justificacion Pendiente
	 */
	private HashMap justificacionNoPosMap = new HashMap();
	
	/**
	 * Arreglo para almacenar las advertencias en la funcionalidad de cargos directos 
	 */
	private ArrayList<ElementoApResource> advertencias = new ArrayList<ElementoApResource>();
	
	/**
	 * Arreglo para almacenar las cuentas asociadas
	 */
	private ArrayList<HashMap<String, Object>> cuentasAsocio = new ArrayList<HashMap<String,Object>>();
	
	//*************ATRIBUTOS DETALLE DE LA ORDEN*******************************************************
	private HashMap<String, Object> encabezadoSolicitud = new HashMap<String, Object>();
	private HashMap<String, Object> cirugiasSolicitud = new HashMap<String, Object>();
	private HashMap<String, Object> datosActoQx = new HashMap<String, Object>();
	private HashMap<String, Object> otrosProfesionales = new HashMap<String, Object>();
	
	//Manejo de los combos
	private ArrayList<HashMap<String, Object>> tiposSala = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> salas = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> esquemasTarifarios = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> especialidades = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> centrosCosto = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> tiposAnestesia = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> anestesiologos = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> asocios = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> asociosServicio = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> profesionales = new ArrayList<HashMap<String,Object>>();
	private ArrayList<HashMap<String, Object>> finalidades = new ArrayList<HashMap<String,Object>>();
	
	private InfoResponsableCobertura infoCobertura;
	private int tarifarioOficial ;
	private int codigoAsocioCirujano;
	private boolean requeridaInformacionRips;
	
	private boolean habilitarInfoRecienNacidos; //me indica si debo mostrar la seccion desplegable de recien nacidos
	private boolean seccionRecienNacidos; //me indica si la seccion de info recien nacidos está cerrada o abierta
	private HashMap<String, Object> infoRecienNacidos = new HashMap<String, Object>();
	private ArrayList<HashMap<String, Object>> sexos = new ArrayList<HashMap<String,Object>>();
	//**************************************************************************************************
	
	private String index;
	private int codigoSexo; //se usa para la busqueda genérica de servicios
	
	//************ATRIBUTOS PARA EL DETALLE DEL SERVICIO****************************************
	private int posicion;
	private String tipoHonorario;
	private String codigoProfesional;
	private boolean entidadManejaRips;
	//***************************************************************************************
	
	//*************ATRIBUTOS PARA EL MANEJO DE LAS AUTORIZACIONES DE CAPITACION*************
	/**
	 * Este atributo se usa para determinar cuando se generó una 
	 * autorización de solicitudes y mostrar el respectivo botón
	 */
	private boolean mostrarImprimirAutorizacion;
	
	/**
	 * lista que contiene los nombres de 
	 * los reportes de las autorzaciones 
	 */
	private ArrayList<String> listaNombresReportes;
	
	/**
	 * Atributo que contiene 
	 * el diagnóstico y tipo CIE del paciente
	 */
	private DtoDiagnostico dtoDiagnostico;
	
	/**
	 * Atributo que indica si la solicitud generada
	 * es de PyP
	 */
	private boolean solPYP;
	
	private String participaAnestesiologo;
	//*************ATRIBUTOS PARA EL MANEJO DE LAS AUTORIZACIONES DE CAPITACION*************
	
	/**Atributo que se encarga de obtener los datos de la cobertura de la cirugia*/
	private List<InfoResponsableCobertura> listaInfoRespoCoberturaCx;
	
	/**
	 * reset de los datos de la forma
	 *
	 */
	public void reset()
	{
		this.estado="";
		this.idCuenta = "";
		this.numeroSolicitud = "";
		this.imprimir = false;
		
		this.fechaIngreso = "";
		this.horaIngreso = "";
		this.fechaSistema = "";
		this.horaSistema = "";
		
		this.advertencias = new ArrayList<ElementoApResource>();
		this.cuentasAsocio = new ArrayList<HashMap<String,Object>>();
		
		//Atributos detalle de la orden
		this.encabezadoSolicitud = new HashMap<String, Object>();
		this.cirugiasSolicitud = new HashMap<String, Object>();
		this.datosActoQx = new HashMap<String, Object>();
		this.otrosProfesionales = new HashMap<String, Object>();
		
		this.tiposSala = new ArrayList<HashMap<String,Object>>();
		this.salas = new ArrayList<HashMap<String,Object>>();
		this.esquemasTarifarios = new ArrayList<HashMap<String,Object>>();
		this.especialidades = new ArrayList<HashMap<String,Object>>();
		this.centrosCosto = new ArrayList<HashMap<String,Object>>();
		this.tiposAnestesia = new ArrayList<HashMap<String,Object>>();
		this.anestesiologos = new ArrayList<HashMap<String,Object>>();
		this.asocios = new ArrayList<HashMap<String,Object>>();
		this.asociosServicio = new ArrayList<HashMap<String,Object>>();
		this.profesionales = new ArrayList<HashMap<String,Object>>();
		this.finalidades = new ArrayList<HashMap<String,Object>>();
		this.justificacionNoPosMap= new HashMap();
        justificacionNoPosMap.put("numRegistros", 0);
		
		this.index = "";
		this.codigoSexo = ConstantesBD.codigoNuncaValido;
		
		this.infoCobertura = new InfoResponsableCobertura();
		this.tarifarioOficial = ConstantesBD.codigoNuncaValido;
		this.codigoAsocioCirujano = ConstantesBD.codigoNuncaValido;
		this.requeridaInformacionRips = false;
		
		this.habilitarInfoRecienNacidos = false;
		this.seccionRecienNacidos = false;
		this.infoRecienNacidos = new HashMap<String, Object>();
		this.sexos = new ArrayList<HashMap<String,Object>>();
		
		//Atributos para el detalle del servicio
		this.posicion = ConstantesBD.codigoNuncaValido;
		this.tipoHonorario = "";
		this.codigoProfesional = "";
		this.entidadManejaRips = false;
		this.dtoDiagnostico = new DtoDiagnostico();
		this.mostrarImprimirAutorizacion=false;
		this.listaNombresReportes= new ArrayList<String>();
		this.solPYP=false;
		this.participaAnestesiologo = ConstantesBD.acronimoSi;
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
			this.fechaSistema = UtilidadFecha.getFechaActual();
			this.horaSistema = UtilidadFecha.getHoraActual();
			
			boolean fechaValida = false, horaValida = false, fechaFinalValida = false, horaFinalValida = false;
			boolean fechaHoraInicioAtencionValida = true;
			
			//**********************FECHA/HORA SOLICITUD*******************************************************
			//Validacion de la fecha de solicitud
			String fecha = this.getEncabezadoSolicitud("fechaSolicitud").toString();
			if(fecha.trim().equals(""))
				errores.add("",new ActionMessage("errors.required","La fecha de solicitud"));
			else if(!UtilidadFecha.validarFecha(fecha))
				errores.add("",new ActionMessage("errors.formatoFechaInvalido","de solicitud"));
			else
				fechaValida = true;
			
			//Validacion de la hora de solicitud
			String hora = this.getEncabezadoSolicitud("horaSolicitud").toString();
			if(hora.trim().equals(""))
				errores.add("",new ActionMessage("errors.required","La hora de solicitud"));
			else if(!UtilidadFecha.validacionHora(hora).puedoSeguir)
				errores.add("",new ActionMessage("errors.formatoHoraInvalido","de solicitud"));
			else
				horaValida = true;
			
			if(fechaValida&&horaValida)
			{
				if(!UtilidadFecha.compararFechas(this.fechaSistema, this.horaSistema, fecha, hora).isTrue())
					errores.add("",new ActionMessage("errors.fechaHoraPosteriorIgualActual","de solicitud","del sistema: "+fechaSistema+" - "+horaSistema));
				
				if(!UtilidadFecha.compararFechas(fecha, hora, this.fechaIngreso, this.horaIngreso).isTrue())
					errores.add("",new ActionMessage("errors.fechaHoraAnteriorIgualActual","de solicitud","del ingreso del paciente: "+fechaIngreso+" - "+horaIngreso));
			}
			//*****************************************************************************
			
			//Validacion del centro de costo
			if(this.getEncabezadoSolicitud("centroCostoSolicitado").toString().equals(""))
				errores.add("",new ActionMessage("errors.required","El centro de costo solicitado"));
			
			//***************************FECHA/HORA INICIO-FIN ATENCION**************************************************
			fechaValida = false; horaValida = false;
			
			//Validacion de la fecha inicio atencion
			fecha = this.getDatosActoQx("fechaInicialCx").toString();
			if(fecha.trim().equals(""))
				errores.add("",new ActionMessage("errors.required","La fecha inicio atención"));
			else if(!UtilidadFecha.validarFecha(fecha))
				errores.add("",new ActionMessage("errors.formatoFechaInvalido","inicio atención"));
			else
				fechaValida = true;
			
			//Validacion de la hora inicio atención
			hora = this.getDatosActoQx("horaInicialCx").toString();
			if(hora.trim().equals(""))
				errores.add("",new ActionMessage("errors.required","La hora inicio atención"));
			else if(!UtilidadFecha.validacionHora(hora).puedoSeguir)
				errores.add("",new ActionMessage("errors.formatoHoraInvalido","inicio atención"));
			else
				horaValida = true;
			
			if(fechaValida&&horaValida)
			{
				fechaHoraInicioAtencionValida = true;
				
				if(!UtilidadFecha.compararFechas(this.fechaSistema, this.horaSistema, fecha, hora).isTrue())
					errores.add("",new ActionMessage("errors.fechaHoraPosteriorIgualActual","inicio atención","del sistema: "+fechaSistema+" - "+horaSistema));
				
				if(!UtilidadFecha.compararFechas(fecha, hora, this.fechaIngreso, this.horaIngreso).isTrue())
					errores.add("",new ActionMessage("errors.fechaHoraAnteriorIgualActual","inicio atención","del ingreso del paciente: "+fechaIngreso+" - "+horaIngreso));
			}
			
			
			fechaFinalValida = false; horaFinalValida = false;
			
			//Validacion de la fecha fin atencion
			fecha = this.getDatosActoQx("fechaFinalCx").toString();
			if(fecha.trim().equals(""))
				errores.add("",new ActionMessage("errors.required","La fecha fin atención"));
			else if(!UtilidadFecha.validarFecha(fecha))
				errores.add("",new ActionMessage("errors.formatoFechaInvalido","fin atención"));
			else
				fechaFinalValida = true;
			
			//Validacion de la hora inicio atención
			hora = this.getDatosActoQx("horaFinalCx").toString();
			if(hora.trim().equals(""))
				errores.add("",new ActionMessage("errors.required","La hora fin atención"));
			else if(!UtilidadFecha.validacionHora(hora).puedoSeguir)
				errores.add("",new ActionMessage("errors.formatoHoraInvalido","fin atención"));
			else
				horaFinalValida = true;
			
			if(fechaFinalValida&&horaFinalValida)
			{
				if(!UtilidadFecha.compararFechas(this.fechaSistema, this.horaSistema, fecha, hora).isTrue())
					errores.add("",new ActionMessage("errors.fechaHoraPosteriorIgualActual","fin atención","del sistema: "+fechaSistema+" - "+horaSistema));
				
				if(fechaValida&&horaValida&&!UtilidadFecha.compararFechas(fecha, hora, this.getDatosActoQx("fechaInicialCx").toString(), this.getDatosActoQx("horaInicialCx").toString()).isTrue())
					errores.add("",new ActionMessage("errors.fechaHoraAnteriorIgualActual","fin atención","inicio atención"));
			}
			
			//Validación de la duración
			if(UtilidadTexto.isEmpty(this.getDatosActoQx("duracionCx").toString()))
				errores.add("", new ActionMessage("errors.required","La duración de la atención"));
			//**********************************************************************************************************************
			
			
			
			//*******************FECHA/HORA INGRESO - SALIDA SALA***************************************************
			fechaValida = false; horaValida = false;
			
			//Validacion de la fecha ingreso sala
			fecha = this.getDatosActoQx("fechaIngresoSala").toString();
			if(fecha.trim().equals(""))
				errores.add("",new ActionMessage("errors.required","La fecha ingreso sala"));
			else if(!UtilidadFecha.validarFecha(fecha))
				errores.add("",new ActionMessage("errors.formatoFechaInvalido","ingreso sala"));
			else
				fechaValida = true;
			
			//Validacion de la hora ingreso sala
			hora = this.getDatosActoQx("horaIngresoSala").toString();
			if(hora.trim().equals(""))
				errores.add("",new ActionMessage("errors.required","La hora ingreso sala"));
			else if(!UtilidadFecha.validacionHora(hora).puedoSeguir)
				errores.add("",new ActionMessage("errors.formatoHoraInvalido","ingreso sala"));
			else
				horaValida = true;
			
			if(fechaValida&&horaValida)
			{
				if(!UtilidadFecha.compararFechas(this.fechaSistema, this.horaSistema, fecha, hora).isTrue())
					errores.add("",new ActionMessage("errors.fechaHoraPosteriorIgualActual","ingreso sala","del sistema: "+fechaSistema+" - "+horaSistema));
				
				if(!UtilidadFecha.compararFechas(fecha, hora, this.fechaIngreso, this.horaIngreso).isTrue())
					errores.add("",new ActionMessage("errors.fechaHoraAnteriorIgualActual","ingreso sala","del ingreso del paciente: "+fechaIngreso+" - "+horaIngreso));
			}
			
			fechaFinalValida = false; horaFinalValida = false;
			
			//Validacion de la fecha salida sala
			fecha = this.getDatosActoQx("fechaSalidaSala").toString();
			if(fecha.trim().equals(""))
				errores.add("",new ActionMessage("errors.required","La fecha salida sala"));
			else if(!UtilidadFecha.validarFecha(fecha))
				errores.add("",new ActionMessage("errors.formatoFechaInvalido","salida sala"));
			else
				fechaFinalValida = true;
			
			//Validacion de la hora salida sala
			hora = this.getDatosActoQx("horaSalidaSala").toString();
			if(hora.trim().equals(""))
				errores.add("",new ActionMessage("errors.required","La hora salida sala"));
			else if(!UtilidadFecha.validacionHora(hora).puedoSeguir)
				errores.add("",new ActionMessage("errors.formatoHoraInvalido","salida sala"));
			else
				horaFinalValida = true;
			
			if(fechaFinalValida&&horaFinalValida)
			{
				if(!UtilidadFecha.compararFechas(this.fechaSistema, this.horaSistema, fecha, hora).isTrue())
					errores.add("",new ActionMessage("errors.fechaHoraPosteriorIgualActual","salida sala","del sistema: "+fechaSistema+" - "+horaSistema));
				
				if(fechaValida&&horaValida&&!UtilidadFecha.compararFechas(fecha, hora, this.getDatosActoQx("fechaIngresoSala").toString(), this.getDatosActoQx("horaIngresoSala").toString()).isTrue())
					errores.add("",new ActionMessage("errors.fechaHoraAnteriorIgualActual","salida sala","ingreso sala"));
			}
			//***************************************************************************************************
			
			//Validacion del tipo de sala
			if(this.getDatosActoQx("codigoTipoSala").toString().equals(""))
				errores.add("",new ActionMessage("errors.required","El tipo de sala"));
			
			//Validacion de la sala
			if(this.getDatosActoQx("codigoSala").toString().equals(""))
				errores.add("",new ActionMessage("errors.required","La sala"));
			
			
			//*******************VALIDACIONES SECCION OTROS PROFESIONALES**********************************************
			int contador  = 0;
			int numOtrosProfesionales = Utilidades.convertirAEntero(this.getOtrosProfesionales("numRegistros")+"",true);
			for(int i=0;i<numOtrosProfesionales;i++)
			{
				if(!UtilidadTexto.getBoolean(this.getOtrosProfesionales("fueEliminado_"+i)+""))
				{
					contador ++;
					if(this.getOtrosProfesionales("codigoAsocio_"+i).toString().equals(""))
						errores.add("",new ActionMessage("errors.required","El asocio en el registro N°"+contador+" de la sección 'Otros Profesionales'"));
					if(this.getOtrosProfesionales("codigoProfesional_"+i).toString().equals(""))
						errores.add("",new ActionMessage("errors.required","El profesional en el registro N°"+contador+" de la sección 'Otros Profesionales'"));
				}
			}
			//Validación para que los asocios no se repitan
			HashMap codigosComparados = new HashMap();
			String descripcion = "";
			String aux1 = "";
			String aux2 = "";
			boolean tmp=true;
			for(int i=0;i<numOtrosProfesionales;i++)
				if(!UtilidadTexto.getBoolean(this.getOtrosProfesionales("fueEliminado_"+i)+""))
				{
					aux1=this.getOtrosProfesionales("codigoAsocio_"+i).toString();
					
					for(int j=numOtrosProfesionales-1;j>i;j--)
						if(!UtilidadTexto.getBoolean(this.getOtrosProfesionales("fueEliminado_"+j)+""))
						{
							
							aux2=this.getOtrosProfesionales("codigoAsocio_"+j).toString();
							//se compara
							if(aux1.compareToIgnoreCase(aux2)==0&&!aux1.equals("")
								&&!aux2.equals("")&&!codigosComparados.containsValue(aux1))
							{
								if(descripcion.equals(""))
									descripcion = (i+1) + "";
								descripcion += "," + (j+1);
								
							}
						}
					
					if(!descripcion.equals(""))
					{
						errores.add("asocios iguales", 
								new ActionMessage("error.salasCirugia.igualesGeneral",
									"asocios","en los registros Nº "+descripcion+" de la sección 'Otros Profesionales'"));
					}
					
					descripcion = "";
					codigosComparados.put(i+"",aux1);
				
					
					
					//----------------------------------------------------------------------------------------------------
					//modificado por tarea 80807
					//no se permite repetir profesionales
					for (int j=(i+1);j<numOtrosProfesionales;j++)
					{
						if (!UtilidadTexto.getBoolean(this.getOtrosProfesionales("fueEliminado_"+i)+"")&& tmp)
							{
								if ((this.getOtrosProfesionales("codigoProfesional_"+i)+"").equals(this.getOtrosProfesionales("codigoProfesional_"+j)+""))
								{
									errores.add("descripcion",new ActionMessage("prompt.generico"," No se pueden ingresar Profesionales Repetidos"));
									tmp=false;
								}
					
							}
					}
					//-----------------------------------------------------------------------------------------------------
					
				}
			//**********************************************************************************************************
			
			//******************VALIDACIÓN DE LA INFORMACIÓN DEL RECIÉN NACIDO*****************************************
			if(this.requeridaInformacionRips&&this.habilitarInfoRecienNacidos)
			{
				if(Utilidades.convertirAEntero(this.getInfoRecienNacidos("edadGestacional")+"", true)<=0)
					errores.add("",new ActionMessage("errors.required","La edad gestacional (información recién nacido)"));
				
				if(UtilidadTexto.isEmpty(this.getInfoRecienNacidos("controlPrenatal")+""))
					errores.add("",new ActionMessage("errors.required","El control prenatal (información recién nacido)"));
				
				int numHijos = Utilidades.convertirAEntero(this.getInfoRecienNacidos("nroHijos")+"", true);
				
				if(numHijos<=0)
					errores.add("",new ActionMessage("errors.required","El número de hijos (información recién nacido)"));
				else
				{
					for(int i=0;i<numHijos;i++)
					{
						fechaValida = false; horaValida = false;
						
						//Validacion de la fecha de nacimiento
						fecha = this.getInfoRecienNacidos("fechaNacimiento_"+i)+"";
						if(UtilidadTexto.isEmpty(fecha))
							errores.add("",new ActionMessage("errors.required","La fecha de nacimiento del hijo N°"+(i+1)));
						else if(!UtilidadFecha.validarFecha(fecha))
							errores.add("",new ActionMessage("errors.formatoFechaInvalido","de nacimiento del hijo N°"+(i+1)));
						else
							fechaValida = true;
						
						//Validacion de la hora de nacimiento
						hora = this.getInfoRecienNacidos("horaNacimiento_"+i)+"";
						if(UtilidadTexto.isEmpty(hora))
							errores.add("",new ActionMessage("errors.required","La hora de nacimiento del hijo N°"+(i+1)));
						else if(!UtilidadFecha.validacionHora(hora).puedoSeguir)
							errores.add("",new ActionMessage("errors.formatoHoraInvalido","de nacimiento del hijo N°"+(i+1)));
						else
							horaValida = true;
						
						if(fechaValida&&horaValida)
						{
							if(!UtilidadFecha.compararFechas(this.fechaSistema, this.horaSistema, fecha, hora).isTrue())
								errores.add("",new ActionMessage("errors.fechaHoraPosteriorIgualActual","de nacimiento del hijo N°"+(i+1),"del sistema: "+fechaSistema+" - "+horaSistema));
							
							if(fechaHoraInicioAtencionValida&&!UtilidadFecha.compararFechas(fecha, hora, this.getDatosActoQx("fechaInicialCx").toString(), this.getDatosActoQx("horaInicialCx").toString()).isTrue())
								errores.add("",new ActionMessage("errors.fechaHoraAnteriorIgualActual","de nacimiento del hijo N°"+(i+1),"inicio atención"));
						}
						
						//Validación del sexo
						if(UtilidadTexto.isEmpty(this.getInfoRecienNacidos("sexo_"+i)+""))
							errores.add("",new ActionMessage("errors.required","el sexo del hijo N°"+(i+1)));
						
						//Validación del peso
						if(Utilidades.convertirAEntero(this.getInfoRecienNacidos("peso_"+i)+"",true)<=0)
							errores.add("",new ActionMessage("errors.required","el peso del hijo N°"+(i+1)));
						
						//Validación del diagnóstico recién nacido
						if(UtilidadTexto.isEmpty(this.getInfoRecienNacidos("diagnosticoRN_"+i)+""))
							errores.add("",new ActionMessage("errors.required","el diagnóstico de recién nacido del hijo N°"+(i+1)));
						
						//Validación de la fecha/hora muerte
						if(!UtilidadTexto.isEmpty(this.getInfoRecienNacidos("diagnosticoMuerte_"+i)+""))
						{
							fechaValida = false; horaValida = false;
							
							//Validacion de la fecha de muerte
							fecha = this.getInfoRecienNacidos("fechaMuerte_"+i)+"";
							if(UtilidadTexto.isEmpty(fecha))
								errores.add("",new ActionMessage("errors.required","La fecha de muerte del hijo N°"+(i+1)));
							else if(!UtilidadFecha.validarFecha(fecha))
								errores.add("",new ActionMessage("errors.formatoFechaInvalido","de muerte del hijo N°"+(i+1)));
							else
								fechaValida = true;
							
							//Validacion de la hora de muerte
							hora = this.getInfoRecienNacidos("horaMuerte_"+i)+"";
							if(UtilidadTexto.isEmpty(hora))
								errores.add("",new ActionMessage("errors.required","La hora de muerte del hijo N°"+(i+1)));
							else if(!UtilidadFecha.validacionHora(hora).puedoSeguir)
								errores.add("",new ActionMessage("errors.formatoHoraInvalido","de muerte del hijo N°"+(i+1)));
							else
								horaValida = true;
							
							if(fechaValida&&horaValida)
							{
								if(!UtilidadFecha.compararFechas(this.fechaSistema, this.horaSistema, fecha, hora).isTrue())
									errores.add("",new ActionMessage("errors.fechaHoraPosteriorIgualActual","de muerte del hijo N°"+(i+1),"del sistema: "+fechaSistema+" - "+horaSistema));
								
								if(fechaHoraInicioAtencionValida&&!UtilidadFecha.compararFechas(fecha, hora, this.getDatosActoQx("fechaInicialCx").toString(), this.getDatosActoQx("horaInicialCx").toString()).isTrue())
									errores.add("",new ActionMessage("errors.fechaHoraAnteriorIgualActual","de muerte del hijo N°"+(i+1),"inicio atención"));
							}
							
						}
							
					}
				}
					
			}
			//**********************************************************************************************************
			
			
			//********************VALIDACIONES SECCION SERVICIOS********************************************************
			
			if(this.getNumCirugiasReales()==0)
				errores.add("",new ActionMessage("errors.minimoCampos2","1 servicio","generar el cargo directo"));
			
			for(int i=0;i<this.getNumCirugias();i++)
			{
				if(!UtilidadTexto.getBoolean(this.cirugiasSolicitud.get("fueEliminado_"+i)+""))
				{
					//1) Se verifica el esquema tarifario
					if(UtilidadTexto.isEmpty(this.cirugiasSolicitud.get("codigoEsquemaTarifario_"+i)+""))
					{
						String[] atributos = {"El esquema tarifario"};
						errores = agregarErrorServicio(errores, i, "errors.required", atributos);
					}
					
					//2) Se verifica la vía
					if(UtilidadTexto.isEmpty(this.cirugiasSolicitud.get("codigoViaCx_"+i)+""))
					{
						String[] atributos = {"La vía"};
						errores = agregarErrorServicio(errores, i, "errors.required", atributos);
					}
					
					//3) Se verifica la especialidad que interviene
					if(UtilidadTexto.isEmpty(this.cirugiasSolicitud.get("codigoEspecialidadInterviene_"+i)+""))
					{
						String[] atributos = {"La especialidad que interviene"};
						errores = agregarErrorServicio(errores, i, "errors.required", atributos);
					}
					
					//***********VALIDACION DE LOS ASOCIOS DE CADA CIRUGÍA******************************
					int numAsocios = Utilidades.convertirAEntero(this.cirugiasSolicitud.get("numProfesionales_"+i)+"",true);
					
					if(numAsocios<=0)
					{
						String[] atributos = {"ingresar el asocio cirujano","generar el cargo directo"};
						errores = agregarErrorServicio(errores, i, "errors.minimoCampos2", atributos);
					}
					else
					{
						boolean asocioCirujano = false; //para verificar si se ingresó el asocio cirujano
						String nombreAsocio = "";
						tmp=true;
						for(int j=0;j<numAsocios;j++)
							if(!UtilidadTexto.getBoolean(this.cirugiasSolicitud.get("fueEliminado_"+i+"_"+j)+""))
							{
								nombreAsocio = this.cirugiasSolicitud.get("nombreAsocio_"+i+"_"+j).toString();
								
								//Se verifica si se ingresó el asocio cirujano
								if(Integer.parseInt(this.getCirugiasSolicitud("codigoAsocio_"+i+"_"+j).toString())==this.codigoAsocioCirujano)
									asocioCirujano = true;
								
								//Se verifica el profesional del asocio
								if(UtilidadTexto.isEmpty(this.cirugiasSolicitud.get("codigoProfesional_"+i+"_"+j)+""))
								{
									String[] atributos = {"El profesional del asocio "+nombreAsocio};
									errores = agregarErrorServicio(errores, i, "errors.required", atributos);
								}
								
								//Se verifica la especialidad del asocio
								if(UtilidadTexto.isEmpty(this.cirugiasSolicitud.get("codigoEspecialidad_"+i+"_"+j)+""))
								{
									String[] atributos = {"La especialidad del asocio "+nombreAsocio};
									errores = agregarErrorServicio(errores, i, "errors.required", atributos);
								}
								
								//----------------------------------------------------------------------------------------------------
								//modificado por tarea 80807
								//no se permite repetir profesionales
								for (int k=(j+1);k<numAsocios;k++)
								{
									if (!UtilidadTexto.getBoolean(this.getOtrosProfesionales("fueEliminado_"+k)+"")&& tmp)
										{
											if ((this.cirugiasSolicitud.get("codigoProfesional_"+i+"_"+j)+"").equals(this.cirugiasSolicitud.get("codigoProfesional_"+i+"_"+k)+""))
											{
												errores.add("descripcion",new ActionMessage("prompt.generico"," No se pueden ingresar Profesionales Repetidos para el Servicio "+(i+1)));
												tmp=false;
											}
								
										}
								}
								//-----------------------------------------------------------------------------------------------------
								
								
							}
						
						if(!asocioCirujano)
						{
							String[] atributos = {"El asocio cirujano"};
							errores = agregarErrorServicio(errores, i, "errors.required", atributos);
						}
						

						
						
						
					} 
					//**********************************************************************************
					
					//Validacion de los datos rips del servicio
					if(this.requeridaInformacionRips&&this.entidadManejaRips)
					{
						//Se valida la finalidad del servicio
						if(UtilidadTexto.isEmpty(this.cirugiasSolicitud.get("codigoFinalidadCx_"+i)+""))
						{
							String[] atributos = {"La finalidad (información RIPS)"};
							errores = agregarErrorServicio(errores, i, "errors.required", atributos);
						}
						
						//Se valida el diagnostico principal
						if(UtilidadTexto.isEmpty(this.cirugiasSolicitud.get("diagPrincipal_"+i)+""))
						{
							String[] atributos = {"El diagnóstico principal (información RIPS)"};
							errores = agregarErrorServicio(errores, i, "errors.required", atributos);
						}
					}
					else if(!this.requeridaInformacionRips&&this.entidadManejaRips)
					{
						if(UtilidadTexto.getBoolean(this.cirugiasSolicitud.get("requiereDiagnostico_"+i)+"")&&
							UtilidadTexto.isEmpty(this.cirugiasSolicitud.get("diagPrincipal_"+i)+"")&&
							this.cirugiasSolicitud.get("tipoServicio_"+i).toString().equals(ConstantesBD.codigoServicioNoCruentos+""))
						{
							String[] atributos = {"El diagnóstico principal (información RIPS)"};
							errores = agregarErrorServicio(errores, i, "errors.required", atributos);
						}
							
					}
					
					this.cirugiasSolicitud.put("mensajeError_"+i,ConstantesBD.acronimoNo);
				}
			}
			
			
			/** MT 3911 - Diana Ruiz*/
			//Validación del tipo de anestesia
			String liquidaAnestesia = this.getDatosActoQx("cobrarAnestesia").toString();
			
			if(liquidaAnestesia.equals(ConstantesBD.acronimoSi)){
				if(this.getDatosActoQx("codigoTipoAnestesia").toString().equals(""))
					errores.add("",new ActionMessage("errors.required","El tipo de anestesia"));
			
				if (participaAnestesiologo.equals(ConstantesBD.acronimoSi)){
					if(this.getDatosActoQx("codigoAnestesiologo").toString().equals(""))
						errores.add("",new ActionMessage("errors.required","El anestesiólogo"));
				}
			
			}
			
			
			
				
				
			
		}
		
		return errores;
	}
	
	
	/**
	 * Método implementado para agregar los errores de un servicio
	 * @param errores
	 * @param posicion
	 * @param llave
	 * @param atributos
	 * @return
	 */
	public ActionErrors agregarErrorServicio(ActionErrors errores,int posicion,String llave,String[] atributos)
	{
		if(!UtilidadTexto.getBoolean(this.cirugiasSolicitud.get("mensajeError_"+posicion)+""))
		{
			this.cirugiasSolicitud.put("mensajeError_"+posicion,ConstantesBD.acronimoSi);
			errores.add("",new ActionMessage("prompt.generico",this.getCirugiasSolicitud("descripcionServicio_"+posicion).toString()+" @"));
		}
		
		switch(atributos.length)
		{
			case 1:
				errores.add("",new ActionMessage(llave,atributos[0]));
				break;
			case 2:
				errores.add("",new ActionMessage(llave,atributos[0],atributos[1]));
				break;
			
		}
		
		return errores;
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
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the advertencias
	 */
	public ArrayList<ElementoApResource> getAdvertencias() {
		return advertencias;
	}

	/**
	 * @param advertencias the advertencias to set
	 */
	public void setAdvertencias(ArrayList<ElementoApResource> advertencias) {
		this.advertencias = advertencias;
	}
	
	/**
	 * @return the advertencias
	 */
	public int getSizeAdvertencias() {
		return advertencias.size();
	}

	/**
	 * @return the idCuenta
	 */
	public String getIdCuenta() {
		return idCuenta;
	}

	/**
	 * @param idCuenta the idCuenta to set
	 */
	public void setIdCuenta(String idCuenta) {
		this.idCuenta = idCuenta;
	}

	/**
	 * @return the cuentasAsocio
	 */
	public ArrayList<HashMap<String, Object>> getCuentasAsocio() {
		return cuentasAsocio;
	}

	/**
	 * @param cuentasAsocio the cuentasAsocio to set
	 */
	public void setCuentasAsocio(ArrayList<HashMap<String, Object>> cuentasAsocio) {
		this.cuentasAsocio = cuentasAsocio;
	}

	/**
	 * @return the cirugiasSolicitud
	 */
	public HashMap<String, Object> getCirugiasSolicitud() {
		return cirugiasSolicitud;
	}

	/**
	 * @param cirugiasSolicitud the cirugiasSolicitud to set
	 */
	public void setCirugiasSolicitud(HashMap<String, Object> cirugiasSolicitud) {
		this.cirugiasSolicitud = cirugiasSolicitud;
	}
	
	
	/**
	 * @return the cirugiasSolicitud
	 */
	public Object getCirugiasSolicitud(String key) {
		return cirugiasSolicitud.get(key);
	}

	/**
	 * @param cirugiasSolicitud the cirugiasSolicitud to set
	 */
	public void setCirugiasSolicitud(String key,Object obj) {
		this.cirugiasSolicitud.put(key, obj);
	}
	

	/**
	 * @return the datosActoQx
	 */
	public HashMap<String, Object> getDatosActoQx() {
		return datosActoQx;
	}

	/**
	 * @param datosActoQx the datosActoQx to set
	 */
	public void setDatosActoQx(HashMap<String, Object> datosActoQx) {
		this.datosActoQx = datosActoQx;
	}
	
	/**
	 * @return the datosActoQx
	 */
	public Object getDatosActoQx(String key) {
		return datosActoQx.get(key);
	}

	/**
	 * @param datosActoQx the datosActoQx to set
	 */
	public void setDatosActoQx(String key,Object obj) {
		this.datosActoQx.put(key, obj);
	}

	/**
	 * @return the encabezadoSolicitud
	 */
	public HashMap<String, Object> getEncabezadoSolicitud() {
		return encabezadoSolicitud;
	}

	/**
	 * @param encabezadoSolicitud the encabezadoSolicitud to set
	 */
	public void setEncabezadoSolicitud(HashMap<String, Object> encabezadoSolicitud) {
		this.encabezadoSolicitud = encabezadoSolicitud;
	}
	
	/**
	 * @return the encabezadoSolicitud
	 */
	public Object getEncabezadoSolicitud(String key) {
		return encabezadoSolicitud.get(key);
	}

	/**
	 * @param encabezadoSolicitud the encabezadoSolicitud to set
	 */
	public void setEncabezadoSolicitud(String key,Object obj) {
		this.encabezadoSolicitud.put(key,obj);
	}

	/**
	 * @return the especialidades
	 */
	public ArrayList<HashMap<String, Object>> getEspecialidades() {
		return especialidades;
	}

	/**
	 * @param especialidades the especialidades to set
	 */
	public void setEspecialidades(ArrayList<HashMap<String, Object>> especialidades) {
		this.especialidades = especialidades;
	}

	/**
	 * @return the esquemasTarifarios
	 */
	public ArrayList<HashMap<String, Object>> getEsquemasTarifarios() {
		return esquemasTarifarios;
	}

	/**
	 * @param esquemasTarifarios the esquemasTarifarios to set
	 */
	public void setEsquemasTarifarios(
			ArrayList<HashMap<String, Object>> esquemasTarifarios) {
		this.esquemasTarifarios = esquemasTarifarios;
	}



	/**
	 * @return the otrosProfesionales
	 */
	public HashMap<String, Object> getOtrosProfesionales() {
		return otrosProfesionales;
	}

	/**
	 * @param otrosProfesionales the otrosProfesionales to set
	 */
	public void setOtrosProfesionales(HashMap<String, Object> otrosProfesionales) {
		this.otrosProfesionales = otrosProfesionales;
	}
	
	/**
	 * @return the otrosProfesionales
	 */
	public Object getOtrosProfesionales(String key) {
		return otrosProfesionales.get(key);
	}

	/**
	 * @param otrosProfesionales the otrosProfesionales to set
	 */
	public void setOtrosProfesionales(String key,Object obj) {
		this.otrosProfesionales.put(key,obj);
	}

	/**
	 * @return the salas
	 */
	public ArrayList<HashMap<String, Object>> getSalas() {
		return salas;
	}

	/**
	 * @param salas the salas to set
	 */
	public void setSalas(ArrayList<HashMap<String, Object>> salas) {
		this.salas = salas;
	}

	/**
	 * @return the tiposSala
	 */
	public ArrayList<HashMap<String, Object>> getTiposSala() {
		return tiposSala;
	}

	/**
	 * @param tiposSala the tiposSala to set
	 */
	public void setTiposSala(ArrayList<HashMap<String, Object>> tiposSala) {
		this.tiposSala = tiposSala;
	}

	/**
	 * @return the centrosCosto
	 */
	public ArrayList<HashMap<String, Object>> getCentrosCosto() {
		return centrosCosto;
	}

	/**
	 * @param centrosCosto the centrosCosto to set
	 */
	public void setCentrosCosto(ArrayList<HashMap<String, Object>> centrosCosto) {
		this.centrosCosto = centrosCosto;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getNumCentrosCosto()
	{
		return this.centrosCosto.size();
	}

	/**
	 * @return the index
	 */
	public String getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(String index) {
		this.index = index;
	}

	/**
	 * @return the tiposAnestesia
	 */
	public ArrayList<HashMap<String, Object>> getTiposAnestesia() {
		return tiposAnestesia;
	}

	/**
	 * @param tiposAnestesia the tiposAnestesia to set
	 */
	public void setTiposAnestesia(ArrayList<HashMap<String, Object>> tiposAnestesia) {
		this.tiposAnestesia = tiposAnestesia;
	}
	
	/**
	 * Método para retornar el número de tipos de anestesia
	 * @return
	 */
	public int getNumTiposAnestesia()
	{
		return this.tiposAnestesia.size();
	}

	/**
	 * @return the anestesiologos
	 */
	public ArrayList<HashMap<String, Object>> getAnestesiologos() {
		return anestesiologos;
	}

	/**
	 * @param anestesiologos the anestesiologos to set
	 */
	public void setAnestesiologos(ArrayList<HashMap<String, Object>> anestesiologos) {
		this.anestesiologos = anestesiologos;
	}
	
	/**
	 * Método para retornar el numero de anestesiologos
	 * @return
	 */
	public int getNumAnestesiologos()
	{
		return this.anestesiologos.size();
	}

	/**
	 * @return the asocios
	 */
	public ArrayList<HashMap<String, Object>> getAsocios() {
		return asocios;
	}

	/**
	 * @param asocios the asocios to set
	 */
	public void setAsocios(ArrayList<HashMap<String, Object>> asocios) {
		this.asocios = asocios;
	}

	/**
	 * @return the profesionales
	 */
	public ArrayList<HashMap<String, Object>> getProfesionales() {
		return profesionales;
	}

	/**
	 * @param profesionales the profesionales to set
	 */
	public void setProfesionales(ArrayList<HashMap<String, Object>> profesionales) {
		this.profesionales = profesionales;
	}

	/**
	 * @return the codigoSexo
	 */
	public int getCodigoSexo() {
		return codigoSexo;
	}

	/**
	 * @param codigoSexo the codigoSexo to set
	 */
	public void setCodigoSexo(int codigoSexo) {
		this.codigoSexo = codigoSexo;
	}
	
	/**
	 * Método para consultar el número de cirugias
	 * @return
	 */
	public int getNumCirugias()
	{
		return Utilidades.convertirAEntero(this.cirugiasSolicitud.get("numRegistros")+"",true);
	}
	
	/**
	 * Método para obtener el número real de cirugias que no han sido eliminadas
	 * @return
	 */
	public int getNumCirugiasReales()
	{
		int contador = 0;
		
		for(int i=0;i<getNumCirugias();i++)
			if(!UtilidadTexto.getBoolean(this.cirugiasSolicitud.get("fueEliminado_"+i)+""))
				contador++;
		
		return contador;
	}

	/**
	 * @return the posicion
	 */
	public int getPosicion() {
		return posicion;
	}

	/**
	 * @param posicion the posicion to set
	 */
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}

	/**
	 * @return the infoCobertura
	 */
	public InfoResponsableCobertura getInfoCobertura() {
		return infoCobertura;
	}

	/**
	 * @param infoCobertura the infoCobertura to set
	 */
	public void setInfoCobertura(InfoResponsableCobertura infoCobertura) {
		this.infoCobertura = infoCobertura;
	}

	/**
	 * @return the tarifarioOficial
	 */
	public int getTarifarioOficial() {
		return tarifarioOficial;
	}

	/**
	 * @param tarifarioOficial the tarifarioOficial to set
	 */
	public void setTarifarioOficial(int tarifarioOficial) {
		this.tarifarioOficial = tarifarioOficial;
	}

	/**
	 * @return the asociosServicio
	 */
	public ArrayList<HashMap<String, Object>> getAsociosServicio() {
		return asociosServicio;
	}

	/**
	 * @param asociosServicio the asociosServicio to set
	 */
	public void setAsociosServicio(
			ArrayList<HashMap<String, Object>> asociosServicio) {
		this.asociosServicio = asociosServicio;
	}

	/**
	 * @return the tipoHonorario
	 */
	public String getTipoHonorario() {
		return tipoHonorario;
	}

	/**
	 * @param tipoHonorario the tipoHonorario to set
	 */
	public void setTipoHonorario(String tipoHonorario) {
		this.tipoHonorario = tipoHonorario;
	}

	/**
	 * @return the codigoAsocioCirujano
	 */
	public int getCodigoAsocioCirujano() {
		return codigoAsocioCirujano;
	}

	/**
	 * @param codigoAsocioCirujano the codigoAsocioCirujano to set
	 */
	public void setCodigoAsocioCirujano(int codigoAsocioCirujano) {
		this.codigoAsocioCirujano = codigoAsocioCirujano;
	}

	/**
	 * @return the codigoProfesional
	 */
	public String getCodigoProfesional() {
		return codigoProfesional;
	}

	/**
	 * @param codigoProfesional the codigoProfesional to set
	 */
	public void setCodigoProfesional(String codigoProfesional) {
		this.codigoProfesional = codigoProfesional;
	}

	/**
	 * @return the entidadManejaRips
	 */
	public boolean isEntidadManejaRips() {
		return entidadManejaRips;
	}

	/**
	 * @param entidadManejaRips the entidadManejaRips to set
	 */
	public void setEntidadManejaRips(boolean entidadManejaRips) {
		this.entidadManejaRips = entidadManejaRips;
	}

	/**
	 * @return the finalidades
	 */
	public ArrayList<HashMap<String, Object>> getFinalidades() {
		return finalidades;
	}

	/**
	 * @param finalidades the finalidades to set
	 */
	public void setFinalidades(ArrayList<HashMap<String, Object>> finalidades) {
		this.finalidades = finalidades;
	}

	/**
	 * Método para obtener el número de finalidades
	 * @return
	 */
	public int getNumFinalidades()
	{
		return this.finalidades.size();
	}

	/**
	 * @return the habilitarInfoRecienNacidos
	 */
	public boolean isHabilitarInfoRecienNacidos() {
		return habilitarInfoRecienNacidos;
	}

	/**
	 * @param habilitarInfoRecienNacidos the habilitarInfoRecienNacidos to set
	 */
	public void setHabilitarInfoRecienNacidos(boolean habilitarInfoRecienNacidos) {
		this.habilitarInfoRecienNacidos = habilitarInfoRecienNacidos;
	}

	/**
	 * @return the seccionRecienNacidos
	 */
	public boolean isSeccionRecienNacidos() {
		return seccionRecienNacidos;
	}

	/**
	 * @param seccionRecienNacidos the seccionRecienNacidos to set
	 */
	public void setSeccionRecienNacidos(boolean seccionRecienNacidos) {
		this.seccionRecienNacidos = seccionRecienNacidos;
	}

	/**
	 * @return the infoRecienNacidos
	 */
	public HashMap<String, Object> getInfoRecienNacidos() {
		return infoRecienNacidos;
	}

	/**
	 * @param infoRecienNacidos the infoRecienNacidos to set
	 */
	public void setInfoRecienNacidos(HashMap<String, Object> infoRecienNacidos) {
		this.infoRecienNacidos = infoRecienNacidos;
	}
	
	/**
	 * @return the infoRecienNacidos
	 */
	public Object getInfoRecienNacidos(String key) {
		return infoRecienNacidos.get(key);
	}

	/**
	 * @param infoRecienNacidos the infoRecienNacidos to set
	 */
	public void setInfoRecienNacidos(String key,Object obj) {
		this.infoRecienNacidos.put(key,obj);
	}

	/**
	 * @return the sexos
	 */
	public ArrayList<HashMap<String, Object>> getSexos() {
		return sexos;
	}

	/**
	 * @param sexos the sexos to set
	 */
	public void setSexos(ArrayList<HashMap<String, Object>> sexos) {
		this.sexos = sexos;
	}

	/**
	 * @return the fechaIngreso
	 */
	public String getFechaIngreso() {
		return fechaIngreso;
	}

	/**
	 * @param fechaIngreso the fechaIngreso to set
	 */
	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	/**
	 * @return the fechaSistema
	 */
	public String getFechaSistema() {
		return fechaSistema;
	}

	/**
	 * @param fechaSistema the fechaSistema to set
	 */
	public void setFechaSistema(String fechaSistema) {
		this.fechaSistema = fechaSistema;
	}

	/**
	 * @return the horaIngreso
	 */
	public String getHoraIngreso() {
		return horaIngreso;
	}

	/**
	 * @param horaIngreso the horaIngreso to set
	 */
	public void setHoraIngreso(String horaIngreso) {
		this.horaIngreso = horaIngreso;
	}

	/**
	 * @return the horaSistema
	 */
	public String getHoraSistema() {
		return horaSistema;
	}

	/**
	 * @param horaSistema the horaSistema to set
	 */
	public void setHoraSistema(String horaSistema) {
		this.horaSistema = horaSistema;
	}

	/**
	 * @return the requeridaInformacionRips
	 */
	public boolean isRequeridaInformacionRips() {
		return requeridaInformacionRips;
	}

	/**
	 * @param requeridaInformacionRips the requeridaInformacionRips to set
	 */
	public void setRequeridaInformacionRips(boolean requeridaInformacionRips) {
		this.requeridaInformacionRips = requeridaInformacionRips;
	}

	/**
	 * @return the imprimir
	 */
	public boolean isImprimir() {
		return imprimir;
	}

	/**
	 * @param imprimir the imprimir to set
	 */
	public void setImprimir(boolean imprimir) {
		this.imprimir = imprimir;
	}

	/**
	 * @return the numeroSolicitud
	 */
	public String getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(String numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getJustificacionNoPosMap() {
		return justificacionNoPosMap;
	}

	/**
	 * 
	 * @param justificacionNoPosMap
	 */
	public void setJustificacionNoPosMap(HashMap justificacionNoPosMap) {
		this.justificacionNoPosMap = justificacionNoPosMap;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getJustificacionNoPosMap(String key) {
		return justificacionNoPosMap.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setJustificacionNoPosMap(String key,Object value) {
		this.justificacionNoPosMap.put(key, value);
	}

	public void setMostrarImprimirAutorizacion(boolean mostrarImprimirAutorizacion) {
		this.mostrarImprimirAutorizacion = mostrarImprimirAutorizacion;
	}

	public boolean isMostrarImprimirAutorizacion() {
		return mostrarImprimirAutorizacion;
	}

	public void setListaNombresReportes(ArrayList<String> listaNombresReportes) {
		this.listaNombresReportes = listaNombresReportes;
	}

	public ArrayList<String> getListaNombresReportes() {
		return listaNombresReportes;
	}

	public void setDtoDiagnostico(DtoDiagnostico dtoDiagnostico) {
		this.dtoDiagnostico = dtoDiagnostico;
	}

	public DtoDiagnostico getDtoDiagnostico() {
		return dtoDiagnostico;
	}

	public void setSolPYP(boolean solPYP) {
		this.solPYP = solPYP;
	}

	public boolean isSolPYP() {
		return solPYP;
	}

	public String getParticipaAnestesiologo() {
		return participaAnestesiologo;
	}

	public void setParticipaAnestesiologo(String participaAnestesiologo) {
		this.participaAnestesiologo = participaAnestesiologo;
	}

	public List<InfoResponsableCobertura> getListaInfoRespoCoberturaCx() {
		return listaInfoRespoCoberturaCx;
	}

	public void setListaInfoRespoCoberturaCx(
			List<InfoResponsableCobertura> listaInfoRespoCoberturaCx) {
		this.listaInfoRespoCoberturaCx = listaInfoRespoCoberturaCx;
	}
}
