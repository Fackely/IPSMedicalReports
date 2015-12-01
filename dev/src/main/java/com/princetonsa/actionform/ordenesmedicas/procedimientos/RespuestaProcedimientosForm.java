/*
 * 
 */
package com.princetonsa.actionform.ordenesmedicas.procedimientos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.RespuestaValidacion;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dto.facturacion.DtoArticuloIncluidoSolProc;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantillaServDiag;
import com.princetonsa.dto.ordenes.DtoProcedimiento;
import com.princetonsa.mundo.historiaClinica.Plantillas;
import com.princetonsa.mundo.ordenesmedicas.procedimientos.RespuestaProcedimientos;
import com.princetonsa.mundo.solicitudes.SolicitudProcedimiento;

/**
 * @author Jorge Armando Osorio Velasquez
 * @author Wilson Rios
 *
 */
public class RespuestaProcedimientosForm extends ValidatorForm
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(RespuestaProcedimientosForm.class);
	
	/**
	 * codigo de resopuesta
	 */
	private String codigoRespuesta;
	
	/**
	 * 
	 */
	private String estado;
	

	/**
	 * 
	 */
	private int finalidadRespuesta;
	
	/**
	 * 
	 */
	private String nuevasObservaciones;

	
	/**
	 * 
	 */
	private String filtro="";
	
	/**
	 * 
	 */
	private String fechaInicialFiltro;
	
	/**
	 * 
	 */
	private String fechaFinalFiltro;
	
	/**
	 * 
	 */
	private String centroCostosSolicitanteFiltro;
	
	/**
	 * 
	 */
	private String codigoEstadoNuevo;
	
	/**
	 * 
	 */
	private String fechaCambio;
	
	/**
	 * 
	 */
	private String horaCambio;
		
	/**
	 * 
	 */
	private HashMap listaSolicitudes = new HashMap();
	
	/**
	 * 
	 */
	private SolicitudProcedimiento solicitudProcedimiento=new SolicitudProcedimiento();
	
	/**
	 * Arreglo de Citas incumplidas con Multas
	 */
	private ArrayList<HashMap<String, Object>> citasIncumplidas = new ArrayList<HashMap<String, Object>>();
	
	
	/**
	 * 
	 */
	private String patronOrdenar;
	
	/**
	 * 
	 */
	private String ultimoPatron;
	
	/**
	 * 
	 */
	private String numeroSolicitud;
	
	/**
	 * 
	 * */
	private String codigoPeticion;
	
	/**
	 * 
	 * */
	private String codigoServicio;
	
	/**
	 * 
	 * */
	private String codigoSolCirugiaSer;
	
	/**
	 * Fecha en que se ejecutï¿½ el procedimiento
	 */
	private String fechaEjecucion;
	
	/**
	 * Hora en se ejecutï¿½ el procedimiento.
	 */
	private String horaEjecucion;
	
	/**
	 * Cï¿½digo del tipo de recargo del procedimiento
	 */
	private int codigoTipoRecargo;
	
	/**
	 * 
	 */
	private String nombreTipoRecargo;
	
	/**
	 * Comentario historica clï¿½nica 
	 */
	private String comentariosHistoriaClinica;
	
	/**
	 * Resultados del procedimiento
	 */
	private String resultados;
	
	/**
	 * 
	 */
	private String resultadosAnteriores;
	
	/**
	 * Observaciones del procedimiento
	 */
	private String observaciones;
	
	/**
	 * 
	 */
	private int maxPageItems;
	
	/**
	 * 
	 */
	private String finalizar;
	
	private String valorCheck="";
	
	

	/**
	 * 
	 */
	private String finalizarSolicitudMultible;
	
	/**
	 * 
	 */
	private boolean paginaSinEncabezados=false;
	
	
	//********ATRIBUTOS DIAGNï¿½STICOS PROCEDIMIENTOS***************************
	/**
	 * Objeto que almacena los diagnï¿½sticos de la respuesta de procedimientos
	 */
	private HashMap diagnosticos = new HashMap();
	/**
	 * Nï¿½mero de diagnï¿½sticos del procedimientos
	 */
	private int numDiagnosticos;
	//************************************************************************

	
	//**********DOCUMENTOS ADJUNTOS******************************
	/**
	 * Colecciï¿½n con los nombres generados de los archivos adjuntos 
	 */
	private final Map documentosAdjuntosGenerados = new HashMap(); 
	
	/**
	 * Nï¿½mero de documentos adjuntos
	 */
	private int numDocumentosAdjuntos = 1;
	
	//***********************************************************
	
	/**
     * para la nevagaciï¿½n del pager, cuando se ingresa
     * un registro nuevo.
     */
    private String linkSiguiente;
	/**
	 * 
	 */
	private HashMap datosHistoriaClinica;
	
	private String codigoPaciente="";
	
	/**
	 * 
	 */
	private String linkVolver="";
	private String linkVolverListado="";
	

	/**
	 * Contiene el mapa de estados de Autorizacion 
	 */
	private HashMap estadoAuto;
	

	
	/**
	 * Indica si viene de pyp
	 */
	private boolean vieneDePyp = false;
	
	//***********ATRIBUTOS RELACIONADOS CON LA AGENDA DE PROCEDIMIENTOS*************
	private String codigoCita;
	private boolean deboMostrarOtrosServiciosCita;
	//********************************************************************************
	
	private String areaFiltro;
	private String pisoFiltro;
	private String habitacionFiltro;
	private String camaFiltro;
	
	private String requiereDiagnostico;
	
	
	//******** TRIBUTOS PARA IDENTIFICAR QUIEN RESPONDE EL PROCEDIMIENTO *****************
	
	private String responde="";
	
	//***************************************************************************************
	
	/**
	 * DtoPlantilla
	 * */
	private DtoPlantilla plantillaDto;
	
	/**
	 * 
	 * */
	private DtoProcedimiento procedimientoDto;
	
	/**
	 * Indica desde donde es llamada la Respuesta Procedimientos
	 * */
	private String funcionalidad ;
	
	/**
	 * ArraYlist Dto de Servicios/Diagnosticos de la plantilla
	 * */
	private ArrayList<DtoPlantillaServDiag> plantillasServDiagArray;
	
	/**
	 * 
	 * */
	private int codigoPlantillaPk;
	
	/**
	 * HashMap textos de Respuesta
	 * */
	private HashMap textoResultadoMap = new HashMap();	
	
	/**
	 * HashMap incluidosServiciosMap
	 * */
	private HashMap incluidosServiciosMap = new HashMap();	
	
	/**
	 * HashMap incluidosServiciosMap
	 * */
	private HashMap incluidosArticulosMap = new HashMap();
	
	
	/**
	 * Dto de articulos incluidos solicitud de procedimiento
	 * */
	private ArrayList <DtoArticuloIncluidoSolProc> arrayArticuloIncluidoDto ;
	
	/**
	 * HashMap de Justificacion de los articulos Incluidos
	 * */
	private HashMap justificacionMap = new HashMap();
	
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
	 * 
	 * */
	private String hiddens;
	
	//***************************************************************************************
	
	private String portatil = ConstantesBD.codigoNuncaValido+"";
	
	/**
	 * 
	 */
	private int diagnosticoPlantilla;
	
	/**
	 * HashMap mapaUtilitario
	 * */
	private HashMap utilitarioMap = new HashMap();
	
	/**
	 * 
	 * */
	private String indicativoMarcado;
	
	//*****************************************************************************************************
	//*****************************************************************************************************
	//Atributos para Responder Procedimientos Entidades Subcontratadas
	
	/**
	 * indica cual fue el flujo paciente/rango
	 * */
	private String tipoOrigen = "";
	
	/**
	 * 
	 * */
	private String codigoEntidadSub;
	
	
	/**
	 * 
	 * */
	private ArrayList<DtoEntidadSubcontratada> entidadesSubArray;
	
	//----------------------------------------------------------------
	//*****************************************************************************************************
	//Modificado por anexo 779
	
	private ArrayList mensajes = new ArrayList();
	
	
	/**
	 * 
	 */
	private Boolean mostrarEnlaceOrdenesAmbulatorias;
	

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
	//***********************************************************************************************************
	//----------------------------------------------------------------
	
	public void resetInterfaz ()
	{
		this.mensajes = new ArrayList();
	}
	
	/**
	 * 
	 * */
	private String mostrarProcedimientosDyT = ConstantesBD.acronimoSi;
	
	
	/**
	 * Contiene el codigo del profesional para filtrar el tipo de especilidades
	 */
	private String codProfesionalFiltro;
	
	
	
	
	// Anexo 41 - Cambio 1.52
	private boolean sinAutorizacionEntidadsubcontratada = false;;
	private ArrayList<String> listaAdvertencias = new ArrayList<String>();
	
	
	//*********************************************************************************************
	//*********************************************************************************************
	//*********************************************************************************************

	public void reset(boolean resetFiltro)
	{
		this.nuevasObservaciones="";
		this.listaSolicitudes=new HashMap();
		this.listaSolicitudes.put("numRegistros", "0");
		this.codigoEstadoNuevo="";
		this.fechaCambio="";
		this.horaCambio="";
		this.solicitudProcedimiento= new SolicitudProcedimiento();
		this.numeroSolicitud="";
		this.codigoPeticion = "";
		this.codigoServicio = "";
		this.codigoSolCirugiaSer = "";
		this.ultimoPatron="";
		this.patronOrdenar="";
		this.finalizar="";
		this.finalizarSolicitudMultible="";
		
		//wilson
		this.fechaEjecucion=UtilidadFecha.getFechaActual();
		this.horaEjecucion=UtilidadFecha.getHoraActual();
		this.codigoTipoRecargo=ConstantesBD.codigoTipoRecargoSinRecargo;
		this.comentariosHistoriaClinica="";
		this.resultados="";
		this.resultadosAnteriores="";
		this.observaciones="";
		
		this.diagnosticos = new HashMap();
		this.numDiagnosticos = 0;
		
		this.documentosAdjuntosGenerados.clear();
		this.numDocumentosAdjuntos = 0;
		this.codigoRespuesta="";
		
		this.datosHistoriaClinica=new HashMap();
		this.nombreTipoRecargo="";
		this.maxPageItems=20;
		this.linkSiguiente="";
		this.valorCheck="N";
		this.paginaSinEncabezados=false;
		this.codigoPaciente="";
		
		//this.linkVolver="";
		
		this.codigoCita = "";
		this.deboMostrarOtrosServiciosCita = false;
		
		this.requiereDiagnostico="";
		this.portatil=ConstantesBD.codigoNuncaValido+""; 
		
		if(resetFiltro)
		{
			this.areaFiltro="";
			this.pisoFiltro="";
			this.habitacionFiltro="";
			this.camaFiltro="";
		}
		this.responde="";
		
		this.plantillaDto = new DtoPlantilla();
		this.procedimientoDto = new DtoProcedimiento();
		this.codigoPlantillaPk = ConstantesBD.codigoNuncaValido;
		this.plantillasServDiagArray = new ArrayList<DtoPlantillaServDiag>();
		this.utilitarioMap = new HashMap();
		
		this.textoResultadoMap = new HashMap();
		this.arrayArticuloIncluidoDto = new ArrayList<DtoArticuloIncluidoSolProc>();
		this.justificacionMap = new HashMap();
		this.hiddens = "";
		this.funcionalidad = "";
		this.diagnosticoPlantilla=ConstantesBD.codigoNuncaValido;
		this.indicativoMarcado = ConstantesBD.acronimoNo;		
		this.estadoAuto=new HashMap();
		
		this.codigoEntidadSub = "";
		this.entidadesSubArray = new ArrayList<DtoEntidadSubcontratada>();
		
		this.codProfesionalFiltro= "";
		this.finalidadRespuesta=ConstantesBD.codigoNuncaValido;
		
		//this.sinAutorizacionEntidadsubcontratada = false;
		this.listaAdvertencias = new ArrayList<String>();
		this.mostrarEnlaceOrdenesAmbulatorias=false;
	}
	
	
	public int getDiagnosticoPlantilla() {
		return diagnosticoPlantilla;
	}


	public void setDiagnosticoPlantilla(int diagnosticoPlantilla) {
		this.diagnosticoPlantilla = diagnosticoPlantilla;
	}


	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores=new ActionErrors();
		
		
		if( this.getEstado() != null )
		{
			for( int i=0; i<this.numDocumentosAdjuntos; i++ )
			{
				if( request.getParameter("documentoAdjuntoGenerado(checkbox_"+i+")") == null )
				{
					if( this.getDocumentoAdjuntoGenerado("codigo_"+i) == null )
						this.setDocumentoAdjuntoGenerado("checkbox_"+i, "off");
				}
			}
		}
		
		logger.info("estado: "+this.estado);
		logger.info("codigoNuevoEstado: "+this.codigoEstadoNuevo);
		if(this.estado.equals("modificarEstadoSolicitud"))
		{
			if(!this.codigoEstadoNuevo.trim().equals(""))
			{
				boolean fechaCorrecta=true;
				boolean horaCorrecta=true;
				if(this.fechaCambio.trim().equals(""))
				{
					errores.add("cofechaCambio",new ActionMessage("errors.required","La fecha"));
					fechaCorrecta=false;
				}
				else
				{
					if(!UtilidadFecha.validarFecha(this.fechaCambio))
					{
						errores.add("fecha", new ActionMessage("errors.formatoFechaInvalido",this.fechaCambio));
						fechaCorrecta=false;
					}
					else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaCambio,UtilidadFecha.getFechaActual()))
					{
						errores.add("Fecha Mayor del Sistema",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia","Fecha ("+this.fechaCambio+")","Actual ("+UtilidadFecha.getFechaActual()+")"));
						fechaCorrecta=false;
					}
				}
				if(this.horaCambio.trim().equals(""))
				{
					errores.add("horaCambio",new ActionMessage("errors.required","La Hora"));
					fechaCorrecta=false;
				}
				else 
				{
					RespuestaValidacion val=UtilidadFecha.validacionHora(this.horaCambio);
					if(!val.puedoSeguir)
					{
						errores.add("error en la hora",new ActionMessage("error.errorEnBlanco",val.textoRespuesta+""));
						fechaCorrecta=false;
					}
				}
				if(fechaCorrecta&&horaCorrecta)
				{
					int numeroSolSeleccionadas = 0;
					for(int i=0;i<Integer.parseInt(listaSolicitudes.get("numRegistros")+"");i++)
					{
						if((listaSolicitudes.get("modificarestado_"+i)+"").equals(ConstantesBD.acronimoSi))
						{
							numeroSolSeleccionadas++;
							//String fechaAux = UtilidadFecha.conversionFormatoFechaABD(this.fechaCambio) + " " +this.horaCambio; 
							//String fechaActual = UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()) + " " + UtilidadFecha.getHoraActual();
							//String fechaSolicitud=UtilidadFecha.conversionFormatoFechaABD(listaSolicitudes.get("fechasolicitud_"+i)+"")+" "+listaSolicitudes.get("horasolicitud_"+i);
	
			    			//--- Validar que la fecha ingresada sea mayor a la de la solicitud  y menor a la hora fecha actual.
							int comparacion1=UtilidadFecha.conversionFormatoFechaABD(this.fechaCambio).compareTo(UtilidadFecha.conversionFormatoFechaABD(listaSolicitudes.get("fechasolicitud_"+i)+""));
							if( comparacion1 < 0 )
							{
								errores.add("fech Invalida", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", " de Cambio " +this.fechaCambio," de Solicitud "+ listaSolicitudes.get("fechasolicitud_"+i) ));
							}
							else if (comparacion1 == 0 && (this.horaCambio.compareTo(listaSolicitudes.get("horasolicitud_"+i)+"")<=0))
							{
								errores.add("fech Invalida", new ActionMessage("errors.horaAnteriorAOtraDeReferencia", " de Cambio " +this.horaCambio," de Solicitud "+ listaSolicitudes.get("horasolicitud_"+i) ));
								
							}
							
							comparacion1=UtilidadFecha.conversionFormatoFechaABD(this.fechaCambio).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
							
							if(comparacion1>0)
							{
								errores.add("fech Invalida", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", " de Cambio " + this.fechaCambio, " Actual "));
							}
							else if (comparacion1 == 0 && (this.horaCambio.compareTo(UtilidadFecha.getHoraActual())>0))
							{
								errores.add("fech Invalida", new ActionMessage("errors.horaPosteriorAOtraDeReferencia", " de Cambio " +this.horaCambio," Actual "));
								
							}
						}
					}
					if(numeroSolSeleccionadas==0)
					{
						errores.add("", new ActionMessage("errors.minimoCampos2","la selección de una orden","realizar el proceso"));
					}
				}
			}
			else
			{
				errores.add("", new ActionMessage("errors.required","El campo Toma de Muestra/En proceso"));
			}
			
			if(!errores.isEmpty())
			{
				this.estado = "";
			}
		}
		

		if(this.estado.equals("imprimir") )
		{
			int contImpri=0;
			for(int i=0;i<Integer.parseInt(listaSolicitudes.get("numRegistros")+"");i++)
			{
				if(UtilidadTexto.getBoolean(listaSolicitudes.get("modificarestado_"+i)+"")) 
				{
					contImpri++;
				}
			}
			if(contImpri==0)
			{
				errores.add("",new ActionMessage("error.ordenes.reporteProcedimientos"));
			}
		}	
		
		if(this.estado.equals("guardar"))
		{
			//Validacion para los articulos incluidos si los hay
			if(this.getArrayArticuloIncluidoDto().size()>0)			
				errores = RespuestaProcedimientos.valicacionesArticulosIncluidosProc(errores,this.arrayArticuloIncluidoDto,this.justificacionMap);
						
			//Validaciones de la fecha de ejecucion 
			if(!UtilidadFecha.validarFecha(this.fechaEjecucion))		
				errores.add("",new ActionMessage("errors.formatoFechaInvalido",this.getFechaEjecucion()));
			
			if(!UtilidadFecha.validacionHora(this.horaEjecucion).puedoSeguir)
				errores.add("",new ActionMessage("errors.formatoHoraInvalido",this.getHoraEjecucion()));
			
			if(errores.isEmpty())
			{					
				if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),this.fechaEjecucion,this.getHoraEjecucion()).isTrue())
					errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual"," de Ejecución ("+this.fechaEjecucion+" "+this.getHoraEjecucion()+")","Actual ("+UtilidadFecha.getFechaActual()+" "+UtilidadFecha.getHoraActual()+")"));
				
				if(!UtilidadFecha.compararFechas(this.fechaEjecucion,this.getHoraEjecucion(),solicitudProcedimiento.getFechaSolicitud(),solicitudProcedimiento.getHoraSolicitud()).isTrue())
					errores.add("descripcion",new ActionMessage("errors.fechaHoraAnteriorIgualActual"," de Ejecución ("+this.fechaEjecucion+" "+this.getHoraEjecucion()+")"," de la Orden ("+solicitudProcedimiento.getFechaSolicitud()+" "+solicitudProcedimiento.getHoraSolicitud()+")"));						
			}			
		
			
			if(this.resultados.trim().equals("") && this.getUtilitarioMap("resultadosRequerido").equals(ConstantesBD.acronimoSi))
			{
				errores.add("",new ActionMessage("errors.required","El Resultado"));
			}
			
			//Valida la informaciï¿½n requerida de la parametrizaciï¿½n del formulario
			errores = Plantillas.validacionCamposPlantilla(this.getPlantillaDto(), errores);
			
			
				if(this.procedimientoDto.getRespuestaProceEspecificoDto().getCodigoMedicoCentroCostoTercero().equals("")){
					errores.add("",new ActionMessage("errors.required","El Profesional que Responde "));
				}
				
				if(this.procedimientoDto.getRespuestaProceEspecificoDto().getCodEspecialidadProfResponde().equals("")){
					errores.add("",new ActionMessage("errors.required","La Especialidad del Profesional que Responde "));
				}
					
			
			
			
			
			//Valida los datos de la seccion muerte
			if(this.procedimientoDto.getRespuestaProceEspecificoDto().getEsMuerto().equals(ConstantesBD.acronimoSi))
			{
				if(this.procedimientoDto.getRespuestaProceEspecificoDto().getDiagnosticoMuerteCadenaCompleta().equals(""))
					errores.add("",new ActionMessage("errors.required","El Diagnï¿½stico de Muerte"));
				
				if(this.procedimientoDto.getRespuestaProceEspecificoDto().getFechaMuerte().equals(""))
					errores.add("",new ActionMessage("errors.required","La Fecha de Muerte"));
				else if(!this.procedimientoDto.getRespuestaProceEspecificoDto().getHoraMuerte().equals(""))
				{
					if(!UtilidadFecha.validarFecha(this.procedimientoDto.getRespuestaProceEspecificoDto().getFechaMuerte()))
						errores.add("Fecha de Muerte", new ActionMessage("errors.formatoFechaInvalido",this.procedimientoDto.getRespuestaProceEspecificoDto().getFechaMuerte()));
					else
					{
						if(!UtilidadFecha.compararFechas(
								this.procedimientoDto.getRespuestaProceEspecificoDto().getFechaMuerte(), 
								this.procedimientoDto.getRespuestaProceEspecificoDto().getHoraMuerte(), 
								this.procedimientoDto.getFechaSolicitud(),
								this.procedimientoDto.getHoraSolicitud()).isTrue())
							errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual","de Muerte "+this.procedimientoDto.getRespuestaProceEspecificoDto().getFechaMuerte()+" "+this.procedimientoDto.getRespuestaProceEspecificoDto().getHoraMuerte()," de Solicitud "+this.procedimientoDto.getFechaSolicitud()+" "+this.procedimientoDto.getHoraSolicitud()));
						
						if(!UtilidadFecha.compararFechas(
								UtilidadFecha.getFechaActual(),
								UtilidadFecha.getHoraActual(),
								this.procedimientoDto.getRespuestaProceEspecificoDto().getFechaMuerte(), 
								this.procedimientoDto.getRespuestaProceEspecificoDto().getHoraMuerte()).isTrue())
							errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual","de Muerte "+this.procedimientoDto.getRespuestaProceEspecificoDto().getFechaMuerte()+" "+this.procedimientoDto.getRespuestaProceEspecificoDto().getHoraMuerte()," Actual "+UtilidadFecha.getFechaActual()+" "+UtilidadFecha.getHoraActual()));
					}	
				}
				
				if(this.procedimientoDto.getRespuestaProceEspecificoDto().getHoraMuerte().equals(""))
					errores.add("",new ActionMessage("errors.required","La Hora de Muerte"));
				else
				{
					if(!UtilidadFecha.validacionHora(this.procedimientoDto.getRespuestaProceEspecificoDto().getHoraMuerte()).puedoSeguir)
						errores.add("descripcion",new ActionMessage("errors.formatoHoraInvalido"," de Muerte"));						
				}
			}
			if(this.finalidadRespuesta<=0)
				errores.add("",new ActionMessage("errors.required","La finalidad"));
			
			if(!errores.isEmpty())
			{
				this.estado="continuarConErrores";
			}			
		}
		return errores;
	}

	

	public String getEstado()
	{
		return estado;
	}

	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	public String getCentroCostosSolicitanteFiltro()
	{
		return centroCostosSolicitanteFiltro;
	}

	public void setCentroCostosSolicitanteFiltro(
			String centroCostosSolicitanteFiltro)
	{
		this.centroCostosSolicitanteFiltro = centroCostosSolicitanteFiltro;
	}

	public String getFechaFinalFiltro()
	{
		return fechaFinalFiltro;
	}

	public void setFechaFinalFiltro(String fechaFinalFiltro)
	{
		this.fechaFinalFiltro = fechaFinalFiltro;
	}

	public String getFechaInicialFiltro()
	{
		return fechaInicialFiltro;
	}

	public void setFechaInicialFiltro(String fechaInicialFiltro)
	{
		this.fechaInicialFiltro = fechaInicialFiltro;
	}

	public String getFiltro()
	{
		return filtro;
	}

	public void setFiltro(String filtro)
	{
		this.filtro = filtro;
	}

	public HashMap getListaSolicitudes()
	{
		return listaSolicitudes;
	}

	public void setListaSolicitudes(HashMap listaSolicitudes)
	{
		this.listaSolicitudes = listaSolicitudes;
	}
	public Object getListaSolicitudes(String key)
	{
		return this.listaSolicitudes.get(key);
	}

	public void setListaSolicitudes(String key,Object value)
	{
		this.listaSolicitudes.put(key, value);
	}

	public String getCodigoEstadoNuevo()
	{
		return codigoEstadoNuevo;
	}

	public void setCodigoEstadoNuevo(String codigoEstadoNuevo)
	{
		this.codigoEstadoNuevo = codigoEstadoNuevo;
	}

	public String getFechaCambio()
	{
		return fechaCambio;
	}

	public void setFechaCambio(String fechaCambio)
	{
		this.fechaCambio = fechaCambio;
	}

	public String getHoraCambio()
	{
		return horaCambio;
	}

	public void setHoraCambio(String horaCambio)
	{
		if(horaCambio.length()<5)
			horaCambio="0"+horaCambio;
		this.horaCambio = horaCambio; 
	}

	/**
	 * @return Returns the numeroSolicitud.
	 */
	public String getNumeroSolicitud() {
		return numeroSolicitud;
	}
	
	/**
	 * @return Returns the numeroSolicitud.
	 */
	public int getNumeroSolicitudInt() {
		return Integer.parseInt(this.numeroSolicitud);
	}
	
	/**
	 * @param numeroSolicitud The numeroSolicitud to set.
	 */
	public void setNumeroSolicitud(String numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	public String getPatronOrdenar()
	{
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar)
	{
		this.patronOrdenar = patronOrdenar;
	}

	public String getUltimoPatron()
	{
		return ultimoPatron;
	}

	public void setUltimoPatron(String ultimoPatron)
	{
		this.ultimoPatron = ultimoPatron;
	}

	/**
	 * @return Returns the solicitudProcedimiento.
	 */
	public SolicitudProcedimiento getSolicitudProcedimiento() {
		return solicitudProcedimiento;
	}

	/**
	 * @param solicitudProcedimiento The solicitudProcedimiento to set.
	 */
	public void setSolicitudProcedimiento(
			SolicitudProcedimiento solicitudProcedimiento) {
		this.solicitudProcedimiento = solicitudProcedimiento;
	}

	/**
	 * @return Returns the finalizar.
	 */
	public String getFinalizar() {
		return finalizar;
	}

	/**
	 * @param finalizar The finalizar to set.
	 */
	public void setFinalizar(String finalizar) {
		this.finalizar = finalizar;
	}

	/**
	 * @return Returns the codigoTipoRecargo.
	 */
	public int getCodigoTipoRecargo() {
		return codigoTipoRecargo;
	}

	/**
	 * @param codigoTipoRecargo The codigoTipoRecargo to set.
	 */
	public void setCodigoTipoRecargo(int codigoTipoRecargo) {
		this.codigoTipoRecargo = codigoTipoRecargo;
	}

	/**
	 * @return Returns the comentariosHistoriaClinica.
	 */
	public String getComentariosHistoriaClinica() {
		return comentariosHistoriaClinica;
	}

	/**
	 * @param comentariosHistoriaClinica The comentariosHistoriaClinica to set.
	 */
	public void setComentariosHistoriaClinica(String comentariosHistoriaClinica) {
		this.comentariosHistoriaClinica = comentariosHistoriaClinica;
	}

	/**
	 * @return Returns the fechaEjecucion.
	 */
	public String getFechaEjecucion() {
		return fechaEjecucion;
	}

	/**
	 * @param fechaEjecucion The fechaEjecucion to set.
	 */
	public void setFechaEjecucion(String fechaEjecucion) {
		this.fechaEjecucion = fechaEjecucion;
	}

	/**
	 * @return Returns the horaEjecucion.
	 */
	public String getHoraEjecucion() {
		return horaEjecucion;
	}

	/**
	 * @param horaEjecucion The horaEjecucion to set.
	 */
	public void setHoraEjecucion(String horaEjecucion) {
		this.horaEjecucion = horaEjecucion;
	}

	/**
	 * @return Returns the observaciones.
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @param observaciones The observaciones to set.
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return Returns the resultados.
	 */
	public String getResultados() {
		return resultados;
	}

	/**
	 * @param resultados The resultados to set.
	 */
	public void setResultados(String resultados) {
		this.resultados = resultados;
	}

	/**
	 * @return Returns the vieneDePyp.
	 */
	public boolean isVieneDePyp() {
		return vieneDePyp;
	}

	/**
	 * @param vieneDePyp The vieneDePyp to set.
	 */
	public void setVieneDePyp(boolean vieneDePyp) {
		this.vieneDePyp = vieneDePyp;
	}
	
	/**
	 * @return Returns the diagnosticos.
	 */
	public HashMap getDiagnosticos() {
		return diagnosticos;
	}
	/**
	 * @param diagnosticos The diagnosticos to set.
	 */
	public void setDiagnosticos(HashMap diagnosticos) {
		this.diagnosticos = diagnosticos;
	}
	/**
	 * @return Retorna un elemento del mapa diagnosticos.
	 */
	public Object getDiagnosticos(String key) {
		return diagnosticos.get(key);
	}
	/**
	 * @param Asigna un elemento al mapa diagnosticos.
	 * 
	 */
	public void setDiagnosticos(String key,Object obj) {
		this.diagnosticos.put(key,obj);
	}
	
	/**
	 * @return Returns the numDiagnosticos.
	 */
	public int getNumDiagnosticos() {
		return numDiagnosticos;
	}
	/**
	 * @param numDiagnosticos The numDiagnosticos to set.
	 */
	public void setNumDiagnosticos(int numDiagnosticos) {
		this.numDiagnosticos = numDiagnosticos;
	}
	/**
	 * Retorna el nombre generado del documento adjunto
	 * @param key
	 * @return Object
	 */
	public Object getDocumentoAdjuntoGenerado(String key)
	{
		return documentosAdjuntosGenerados.get(key);
	}	
	
	
	/**
	 * Asigna el nombre generado del documento adjunto bajo la llave dada
	 * @param key
	 * @param value
	 */
	public void setDocumentoAdjuntoGenerado(String key, Object value) 
	{
		String val = (String) value;
		
		if (val != null) 
			val = val.trim();

		documentosAdjuntosGenerados.put(key, value);
	}

	/**
	 * @return Returns the documentosAdjuntosGenerados.
	 */
	public Map getDocumentosAdjuntosGenerados() {
		return documentosAdjuntosGenerados;
	}

	/**
	 * @return Returns the numDocumentosAdjuntos.
	 */
	public int getNumDocumentosAdjuntos() {
		return numDocumentosAdjuntos;
	}

	/**
	 * @param numDocumentosAdjuntos The numDocumentosAdjuntos to set.
	 */
	public void setNumDocumentosAdjuntos(int numDocumentosAdjuntos) {
		this.numDocumentosAdjuntos = numDocumentosAdjuntos;
	}

	/**
	 * @return Returns the codigoRespuesta.
	 */
	public String getCodigoRespuesta() {
		return codigoRespuesta;
	}

	/**
	 * @param codigoRespuesta The codigoRespuesta to set.
	 */
	public void setCodigoRespuesta(String codigoRespuesta) {
		this.codigoRespuesta = codigoRespuesta;
	}

	/**
	 * @return Returns the datosHistoriaClinica.
	 */
	public HashMap getDatosHistoriaClinica() {
		return datosHistoriaClinica;
	}

	/**
	 * @param datosHistoriaClinica The datosHistoriaClinica to set.
	 */
	public void setDatosHistoriaClinica(HashMap datosHistoriaClinica) {
		this.datosHistoriaClinica = datosHistoriaClinica;
	}

	/**
	 * @return Returns the resultadosAnteriores.
	 */
	public String getResultadosAnteriores() {
		return resultadosAnteriores;
	}

	/**
	 * @param resultadosAnteriores The resultadosAnteriores to set.
	 */
	public void setResultadosAnteriores(String resultadosAnteriores) {
		this.resultadosAnteriores = resultadosAnteriores;
	}


	/**
	 * @return Returns the nombreTipoRecargo.
	 */
	public String getNombreTipoRecargo() {
		return nombreTipoRecargo;
	}


	/**
	 * @param nombreTipoRecargo The nombreTipoRecargo to set.
	 */
	public void setNombreTipoRecargo(String nombreTipoRecargo) {
		this.nombreTipoRecargo = nombreTipoRecargo;
	}


	public int getMaxPageItems()
	{
		return maxPageItems;
	}


	public void setMaxPageItems(int maxPageItems)
	{
		this.maxPageItems = maxPageItems;
	}


	public String getLinkSiguiente()
	{
		return linkSiguiente;
	}


	public void setLinkSiguiente(String linkSiguiente)
	{
		this.linkSiguiente = linkSiguiente;
	}


	public String getValorCheck()
	{
		return valorCheck;
	}


	public void setValorCheck(String valorCheck)
	{
		this.valorCheck = valorCheck;
	}


	public boolean isPaginaSinEncabezados()
	{
		return paginaSinEncabezados;
	}


	public void setPaginaSinEncabezados(boolean paginaSinEncabezados)
	{
		this.paginaSinEncabezados = paginaSinEncabezados;
	}


	/**
	 * @return Returns the codigoPaciente.
	 */
	public String getCodigoPaciente()
	{
		return codigoPaciente;
	}


	/**
	 * @param codigoPaciente The codigoPaciente to set.
	 */
	public void setCodigoPaciente(String codigoPaciente)
	{
		this.codigoPaciente = codigoPaciente;
	}


	/**
	 * @return Returns the linkVolver.
	 */
	public String getLinkVolver()
	{
		return linkVolver;
	}


	/**
	 * @param linkVolver The linkVolver to set.
	 */
	public void setLinkVolver(String linkVolver)
	{
		this.linkVolver = linkVolver;
	}


	/**
	 * @return the codigoCita
	 */
	public String getCodigoCita() {
		return codigoCita;
	}


	/**
	 * @param codigoCita the codigoCita to set
	 */
	public void setCodigoCita(String codigoCita) {
		this.codigoCita = codigoCita;
	}


	/**
	 * @return the deboMostrarOtrosServiciosCita
	 */
	public boolean isDeboMostrarOtrosServiciosCita() {
		return deboMostrarOtrosServiciosCita;
	}


	/**
	 * @param deboMostrarOtrosServiciosCita the deboMostrarOtrosServiciosCita to set
	 */
	public void setDeboMostrarOtrosServiciosCita(
			boolean deboMostrarOtrosServiciosCita) {
		this.deboMostrarOtrosServiciosCita = deboMostrarOtrosServiciosCita;
	}


	public String getAreaFiltro() {
		return areaFiltro;
	}


	public void setAreaFiltro(String areaFiltro) {
		this.areaFiltro = areaFiltro;
	}


	public String getCamaFiltro() {
		return camaFiltro;
	}


	public void setCamaFiltro(String camaFiltro) {
		this.camaFiltro = camaFiltro;
	}


	public String getHabitacionFiltro() {
		return habitacionFiltro;
	}


	public void setHabitacionFiltro(String habitacionFiltro) {
		this.habitacionFiltro = habitacionFiltro;
	}


	public String getPisoFiltro() {
		return pisoFiltro;
	}


	public void setPisoFiltro(String pisoFiltro) {
		this.pisoFiltro = pisoFiltro;
	}


	/**
	 * @return the requiereDiagnostico
	 */
	public String getRequiereDiagnostico() {
		return requiereDiagnostico;
	}


	/**
	 * @param requiereDiagnostico the requiereDiagnostico to set
	 */
	public void setRequiereDiagnostico(String requiereDiagnostico) {
		this.requiereDiagnostico = requiereDiagnostico;
	}

	
	public String getResponde() {
		return responde;
	}


	public void setResponde(String responde) {
		this.responde = responde;
	}


	/**
	 * @return the plantillaDto
	 */
	public DtoPlantilla getPlantillaDto() {
		return plantillaDto;
	}


	/**
	 * @param plantillaDto the plantillaDto to set
	 */
	public void setPlantillaDto(DtoPlantilla plantillaDto) {
		this.plantillaDto = plantillaDto;
	}


	/**
	 * @return the utilitarioMap
	 */
	public HashMap getUtilitarioMap() {
		return utilitarioMap;
	}


	/**
	 * @param utilitarioMap the utilitarioMap to set
	 */
	public void setUtilitarioMap(HashMap utilitarioMap) {
		this.utilitarioMap = utilitarioMap;
	}	
	
	/**
	 * @return the utilitarioMap
	 */
	public Object getUtilitarioMap(String key) {
		return utilitarioMap.get(key);
	}

	/**
	 * @param utilitarioMap the utilitarioMap to set
	 */
	public void setUtilitarioMap(String key, Object value) {
		this.utilitarioMap.put(key, value);
	}


	/**
	 * @return the textoResultadoMap
	 */
	public HashMap getTextoResultadoMap() {
		return textoResultadoMap;
	}


	/**
	 * @param textoResultadoMap the textoResultadoMap to set
	 */
	public void setTextoResultadoMap(HashMap textoResultadoMap) {
		this.textoResultadoMap = textoResultadoMap;
	}	
	
	
	/**
	 * @return the textoResultadoMap
	 */
	public Object getTextoResultadoMap(String key) {
		return textoResultadoMap.get(key);
	}


	/**
	 * @param textoResultadoMap the textoResultadoMap to set
	 */
	public void setTextoResultadoMap(String key, Object value) {
		this.textoResultadoMap.put(key,value);
	}
	

	
	public String getPortatil() {
		return portatil;
	}


	public void setPortatil(String portatil) {
		this.portatil = portatil;
	}


	/**
	 * @return the incluidosServiciosMap
	 */
	public HashMap getIncluidosServiciosMap() {
		return incluidosServiciosMap;
	}


	/**
	 * @param incluidosServiciosMap the incluidosServiciosMap to set
	 */
	public void setIncluidosServiciosMap(HashMap incluidosServiciosMap) {
		this.incluidosServiciosMap = incluidosServiciosMap;
	}
	
	/**
	 * @return the incluidosServiciosMap
	 */
	public Object getIncluidosServiciosMap(String key) {
		return incluidosServiciosMap.get(key);
	}


	/**
	 * @param incluidosServiciosMap the incluidosServiciosMap to set
	 */
	public void setIncluidosServiciosMap(String key, Object value) {
		this.incluidosServiciosMap.put(key, value);
	}


	/**
	 * @return the incluidosArticulosMap
	 */
	public HashMap getIncluidosArticulosMap() {
		return incluidosArticulosMap;
	}


	/**
	 * @param incluidosArticulosMap the incluidosArticulosMap to set
	 */
	public void setIncluidosArticulosMap(HashMap incluidosArticulosMap) {
		this.incluidosArticulosMap = incluidosArticulosMap;
	}
	
	/**
	 * @return the incluidosArticulosMap
	 */
	public Object getIncluidosArticulosMap(String key) {
		return incluidosArticulosMap.get(key);
	}


	/**
	 * @param incluidosArticulosMap the incluidosArticulosMap to set
	 */
	public void setIncluidosArticulosMap(String key, Object value) {
		this.incluidosArticulosMap.put(key, value);
	}


	/**
	 * @return the arrayArticuloIncluidoDto
	 */
	public ArrayList<DtoArticuloIncluidoSolProc> getArrayArticuloIncluidoDto() {
		return arrayArticuloIncluidoDto;
	}


	/**
	 * @param arrayArticuloIncluidoDto the arrayArticuloIncluidoDto to set
	 */
	public void setArrayArticuloIncluidoDto(
			ArrayList<DtoArticuloIncluidoSolProc> arrayArticuloIncluidoDto) {
		this.arrayArticuloIncluidoDto = arrayArticuloIncluidoDto;
	}


	/**
	 * @return the justificacionInclMap
	 */
	public HashMap getJustificacionMap() {
		return justificacionMap;
	}


	/**
	 * @param justificacionInclMap the justificacionInclMap to set
	 */
	public void setJustificacionMap(HashMap justificacionInclMap) {
		this.justificacionMap = justificacionMap;
	}
	
	/**
	 * @return the justificacionInclMap
	 */
	public Object getJustificacionMap(String key) {
		return justificacionMap.get(key);
	}


	/**
	 * @param justificacionInclMap the justificacionInclMap to set
	 */
	public void setJustificacionMap(String key, Object value) {
		this.justificacionMap.put(key, value);
	}


	/**
	 * @return the hiddens
	 */
	public String getHiddens() {
		return hiddens;
	}


	/**
	 * @param hiddens the hiddens to set
	 */
	public void setHiddens(String hiddens) {
		this.hiddens = hiddens;
	}


	/**
	 * @return the plantillasServDiagArray
	 */
	public ArrayList<DtoPlantillaServDiag> getPlantillasServDiagArray() {
		return plantillasServDiagArray;
	}


	/**
	 * @param plantillasServDiagArray the plantillasServDiagArray to set
	 */
	public void setPlantillasServDiagArray(
			ArrayList<DtoPlantillaServDiag> plantillasServDiagArray) {
		this.plantillasServDiagArray = plantillasServDiagArray;
	}


	/**
	 * @return the codigoPlantillaPk
	 */
	public int getCodigoPlantillaPk() {
		return codigoPlantillaPk;
	}


	/**
	 * @param codigoPlantillaPk the codigoPlantillaPk to set
	 */
	public void setCodigoPlantillaPk(int codigoPlantillaPk) {
		this.codigoPlantillaPk = codigoPlantillaPk;
	}


	/**
	 * @return the codigoServicio
	 */
	public String getCodigoServicio() {
		return codigoServicio;
	}


	/**
	 * @param codigoServicio the codigoServicio to set
	 */
	public void setCodigoServicio(String codigoServicio) {
		this.codigoServicio = codigoServicio;
	}


	/**
	 * @return the funcionalidad
	 */
	public String getFuncionalidad() {
		if(UtilidadTexto.isEmpty(this.funcionalidad))
			return "";
		
		return funcionalidad;
	}


	/**
	 * @param funcionalidad the funcionalidad to set
	 */
	public void setFuncionalidad(String funcionalidad) {
		this.funcionalidad = funcionalidad;
	}


	/**
	 * @return the codigoPeticion
	 */
	public String getCodigoPeticion() {
		return codigoPeticion;
	}


	/**
	 * @param codigoPeticion the codigoPeticion to set
	 */
	public void setCodigoPeticion(String codigoPeticion) {
		this.codigoPeticion = codigoPeticion;
	}


	/**
	 * @return the codigoSolCirugiaSer
	 */
	public String getCodigoSolCirugiaSer() {
		return codigoSolCirugiaSer;
	}


	/**
	 * @param codigoSolCirugiaSer the codigoSolCirugiaSer to set
	 */
	public void setCodigoSolCirugiaSer(String codigoSolCirugiaSer) {
		this.codigoSolCirugiaSer = codigoSolCirugiaSer;
	}


	/**
	 * @return the procedimientoDto
	 */
	public DtoProcedimiento getProcedimientoDto() {
		return procedimientoDto;
	}


	/**
	 * @param procedimientoDto the procedimientoDto to set
	 */
	public void setProcedimientoDto(DtoProcedimiento procedimientoDto) {
		this.procedimientoDto = procedimientoDto;
	}


	/**
	 * @return the indicativoMarcado
	 */
	public String getIndicativoMarcado() {
		return indicativoMarcado;
	}


	/**
	 * @param indicativoMarcado the indicativoMarcado to set
	 */
	public void setIndicativoMarcado(String indicativoMarcado) {
		this.indicativoMarcado = indicativoMarcado;
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


	public String getMostrarProcedimientosDyT() {
		return mostrarProcedimientosDyT;
	}


	public void setMostrarProcedimientosDyT(String mostrarProcedimientosDyT) {
		this.mostrarProcedimientosDyT = mostrarProcedimientosDyT;
	}
	public HashMap getEstadoAuto() {
		return estadoAuto;
	}
	public void setEstadoAuto(HashMap estadoAuto) {
		this.estadoAuto = estadoAuto;
	}
	public ArrayList<HashMap<String, Object>> getCitasIncumplidas() {
		return citasIncumplidas;
	}
	public void setCitasIncumplidas(
			ArrayList<HashMap<String, Object>> citasIncumplidas) {
		this.citasIncumplidas = citasIncumplidas;
	}
	public String getTipoOrigen() {
		return tipoOrigen;
	}
	public void setTipoOrigen(String tipoOrigen) {
		this.tipoOrigen = tipoOrigen;
	}
	public String getCodigoEntidadSub() {
		return codigoEntidadSub;
	}
	public void setCodigoEntidadSub(String codigoEntidadSub) {
		this.codigoEntidadSub = codigoEntidadSub;
	}
	public ArrayList<DtoEntidadSubcontratada> getEntidadesSubArray() {
		return entidadesSubArray;
	}
	public void setEntidadesSubArray(
			ArrayList<DtoEntidadSubcontratada> entidadesSubArray) {
		this.entidadesSubArray = entidadesSubArray;
	}
	/**
	 * @return the codProfesionalFiltro
	 */
	public String getCodProfesionalFiltro() {
		return codProfesionalFiltro;
	}
	/**
	 * @param codProfesionalFiltro the codProfesionalFiltro to set
	 */
	public void setCodProfesionalFiltro(String codProfesionalFiltro) {
		this.codProfesionalFiltro = codProfesionalFiltro;
	}
	
	public String getLinkVolverListado() {
		return linkVolverListado;
	}
	public void setLinkVolverListado(String linkVolverListado) {
		this.linkVolverListado = linkVolverListado;
	}
	public String getFinalizarSolicitudMultible() {
		return finalizarSolicitudMultible;
	}
	public void setFinalizarSolicitudMultible(String finalizarSolicitudMultible) {
		this.finalizarSolicitudMultible = finalizarSolicitudMultible;
	}
	public String getNuevasObservaciones() {
		return nuevasObservaciones;
	}
	public void setNuevasObservaciones(String nuevasObservaciones) {
		this.nuevasObservaciones = nuevasObservaciones;
	}
	public int getFinalidadRespuesta() {
		return finalidadRespuesta;
	}
	public void setFinalidadRespuesta(int finalidadRespuesta) {
		this.finalidadRespuesta = finalidadRespuesta;
	}
	public boolean isSinAutorizacionEntidadsubcontratada() {
		return sinAutorizacionEntidadsubcontratada;
	}
	public void setSinAutorizacionEntidadsubcontratada(
			boolean sinAutorizacionEntidadsubcontratada) {
		this.sinAutorizacionEntidadsubcontratada = sinAutorizacionEntidadsubcontratada;
	}
	public ArrayList<String> getListaAdvertencias() {
		return listaAdvertencias;
	}
	public void setListaAdvertencias(ArrayList<String> listaAdvertencias) {
		this.listaAdvertencias = listaAdvertencias;
	}
	/**
	 * @return the mostrarEnlaceOrdenesAmbulatorias
	 */
	public Boolean getMostrarEnlaceOrdenesAmbulatorias() {
		return mostrarEnlaceOrdenesAmbulatorias;
	}
	/**
	 * @param mostrarEnlaceOrdenesAmbulatorias the mostrarEnlaceOrdenesAmbulatorias to set
	 */
	public void setMostrarEnlaceOrdenesAmbulatorias(
			Boolean mostrarEnlaceOrdenesAmbulatorias) {
		this.mostrarEnlaceOrdenesAmbulatorias = mostrarEnlaceOrdenesAmbulatorias;
	}
	

}