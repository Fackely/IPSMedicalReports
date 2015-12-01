/*
 * Junio 06, 2006
 */
package com.princetonsa.actionform.historiaClinica;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * @author Sebastián Gómez 
 *
 * Clase que almacena y carga la información utilizada para la funcionalidad
 *Información del Parto
 */
public class InformacionPartoForm extends ValidatorForm 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(InformacionPartoForm.class);
	
	
	/**
	 * Variable usada para almacenar el estado del controlador
	 */
	private String estado;
	
	/**
	 * Mapa donde se almacenan la informacion del Parto
	 */
	private HashMap infoParto = new HashMap();
	
	/**
	 * Mapa para almacenar el listado de solicitudes para informacion parto
	 */
	private HashMap listadoSolicitudes = new HashMap();
	
	/**
	 * Número de registros del mapa listadoSolicitudes
	 */
	private int numSolicitudes;
	
	/**
	 * Variable donde se almacena el numero de solicitud 
	 * seleccionado
	 */
	private String numeroSolicitud;
	
	/**
	 * Variable donde se alamcena la posición del registro seleccionado
	 */
	private int pos;
	
	/**
	 * Datos del egreso
	 */
	private String fechaEgreso;
	private String horaEgreso;
	
	/**
	 * Variable que almacena el login del usuario logueado
	 */
	private String loginUsuario;
	
	//***ATRIBUTOS OPCION PERIODO*********
	/**
	 * fecha inicial
	 */
	private String fechaInicial;
	/**
	 * fecha Final
	 */
	private String fechaFinal;
	
	///****ATRIBUTOS DE ORDENACIÓN************
	private String indice;
	private String ultimoIndice;
	
	//*****ATRIBUTOS PAGER****************
	private int maxPageItems;
	private String linkSiguiente;
	private int offset;
	
	//*****ATRIBUTOS SECCIONES**************
	private boolean seccionIdentificacion;
	private boolean seccionEnfermedades;
	private boolean seccionGeneral;
	private boolean seccionVigilancia;
	private boolean seccionAtencion;
	private boolean seccionEgreso;
	private boolean seccionVigilanciaClinica;
	HashMap mapaIdentificacion = new HashMap();
	HashMap mapaEnfermedades = new HashMap();
	HashMap mapaMedicacion = new HashMap();
	HashMap mapaTransfusion = new HashMap();
	
	/**
	 * mapa de la seccion vigilancia clinica Map
	 */
	private HashMap vigilanciaClinicaMap= new HashMap();
	
	/**
	 * mapa de la seccion partograma
	 */
	private HashMap partogramaMap= new HashMap();
	
	/**
	 * reset de los datos de la forma
	 *
	 */
	public void reset()
	{
		this.estado="";
		this.infoParto = new HashMap();
		this.listadoSolicitudes = new HashMap();
		this.numSolicitudes = 0;
		this.numeroSolicitud = "";
		this.pos = 0;
		
		this.indice = "";
		this.ultimoIndice = "";
		
		this.maxPageItems = 0;
		this.linkSiguiente = "";
		this.offset = 0;
		
		this.fechaInicial = "";
		this.fechaFinal = "";
		
		this.fechaEgreso = "";
		this.horaEgreso = "";
		
		this.loginUsuario = "";
		
		this.seccionIdentificacion = false;
		this.seccionEnfermedades = false;
		this.seccionGeneral = true;
		this.seccionVigilancia = false;
		this.seccionAtencion = false;
		this.seccionEgreso = false;
		this.seccionVigilanciaClinica = false;
		this.inicializarInfoParto();
	}
	
	public void inicializarInfoParto()
	{
		this.mapaIdentificacion = new HashMap();
		this.mapaEnfermedades = new HashMap();
		this.mapaMedicacion = new HashMap();
		this.mapaTransfusion = new HashMap();
		
		this.vigilanciaClinicaMap= new HashMap();
		this.vigilanciaClinicaMap.put("numRegistros", "0");
		
		this.partogramaMap= new HashMap();
		this.inicializarTagsPartograma();
	}
	
	/**
	 * 
	 * 
	 */
	public void inicializarTagsPartograma()
	{
		this.partogramaMap.put("mapaPosiciones", Utilidades.obtenerCodigoNombreTablaMap("posiciones"));
		this.partogramaMap.put("mapaParidades", Utilidades.obtenerCodigoNombreTablaMap("paridades"));
		this.partogramaMap.put("mapaMembranas", Utilidades.obtenerCodigoNombreTablaMap("membranas"));
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
		
		if(this.estado.equals("guardar"))
		{
			//Validacion de la expulsion tapón
			if(this.getInfoParto("expulsionTapon")==null)
				this.setInfoParto("expulsionTapon", "");
			//Validacion de la ruptura de membranas
			if(this.getInfoParto("rupturaMembranas")==null)
				this.setInfoParto("rupturaMembranas", "");
			
			//Parto/Aborto -------------------------------------------------------------------------------------------
			if(this.getInfoParto("parto")==null||this.getInfoParto("parto").equals("")) {
				if(this.getInfoParto("esAborto")== null || this.getInfoParto("esAborto").equals("null")) {
					errores.add("Parto/Aborto es requerido",new ActionMessage("errors.required","Parto/Aborto"));
				}
			}
			
			//Validaciones campos numéricos -------------------------------------------------------------------------
			errores = validacionesCamposNumericos(errores);
			
			boolean inicioPartoEsCesarea = this.getInfoParto("inicioTParto").toString().equals(ConstantesIntegridadDominio.acronimoCesarea);
			if(UtilidadTexto.getBoolean(this.getInfoParto("finalizado").toString()))
			{
			
				if(this.getInfoParto("parto")==null||this.getInfoParto("parto").equals("")||this.getInfoParto("parto").equals(ConstantesBD.acronimoSi))
				{
					/****************VALIDACIONES ********************************/
					//El número de embarazo--------------------------------------------------------------------------------------------
					if(this.getInfoParto("numeroEmbarazo").toString().equals(""))
						errores.add("el numero de embarazo es requerido",new ActionMessage("errors.required","El número de embarazo"));
					
					//En Donde----------------------------------------------------------------------------------------------
					if(this.getInfoParto("controlPrenatal").toString().equals(ConstantesBD.acronimoNo)&&this.getInfoParto("enDonde").toString().equals(""))
						errores.add("en donde es requerido",new ActionMessage("errors.required","El campo en donde (información general)"));
					
					//Semanas gestacional--------------------------------------------------------------------------------------
					if(this.getInfoParto("semanas").toString().equals(""))
						errores.add("las semanas de gestacion es requerido",new ActionMessage("errors.required","semanas (edad gestacional)"));
					
					//días gestacional--------------------------------------------------------------------------------------
					if(this.getInfoParto("dias").toString().equals(""))
						errores.add("los días de gestacion es requerido",new ActionMessage("errors.required","días (edad gestacional)"));
					
					//Por gestacional--------------------------------------------------------------------------------------
					if(this.getInfoParto("porGestacional").toString().equals(""))
						errores.add("por gestacion es requerido",new ActionMessage("errors.required","por (edad gestacional)"));
					
					//Consultas Prenatales----------------------------------------------------------------------------------------------
					if(this.getInfoParto("consultasPrenatal").toString().equals(""))
						errores.add("las consultas prenatales es requerido",new ActionMessage("errors.required","Consultas Prenatales"));
					
					//Inicio T. de Parto--------------------------------------------------------------------------------------
					if(this.getInfoParto("inicioTParto").toString().equals(""))
						errores.add("inicio T. de Parto es requerido",new ActionMessage("errors.required","Inicio T. de Parto"));
					
					//Desde Cuando--------------------------------------------------------------------------------------
					if(this.getInfoParto("desdeCuandoFetales").toString().equals(""))
						errores.add("desde cuando es requerido",new ActionMessage("errors.required","el campo desde cuando (percepción movimientos fetales)"));
					
					//Fecha/Hora Contracciones----------------------------------------------------------------------------------------------
					boolean horaValida = true;
					String aux0 = this.getInfoParto("horaContracciones").toString();
					if(!UtilidadFecha.validacionHora(aux0).puedoSeguir)
						horaValida = false;
					
					aux0 = this.getInfoParto("fechaContracciones").toString();
					
					if(!UtilidadFecha.validarFecha(aux0))
					{
						if(!inicioPartoEsCesarea)
							errores.add("Fecha Contracciones Inválidas",new ActionMessage("errors.formatoFechaInvalido","de contracciones"));
						else
							this.setInfoParto("fechaContracciones", "");
							
					}
					else 
					{
						if(UtilidadFecha.conversionFormatoFechaABD(aux0).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
						{
							if(!inicioPartoEsCesarea)
								errores.add("Fecha Contracciones Mayor a Actual",new ActionMessage("errors.fechaPosteriorIgualActual","de contracciones","actual"));
							else
								this.setInfoParto("fechaContracciones", "");
						}
						else if(horaValida&&!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),aux0,this.getInfoParto("horaContracciones").toString()).isTrue())
						{
							if(!inicioPartoEsCesarea)
								errores.add("Fecha/Hora Contracciones Mayor a Actual",new ActionMessage("errors.fechaHoraPosteriorIgualActual","de contracciones ","actual"));
							else
							{
								this.setInfoParto("fechaContracciones", "");
								this.setInfoParto("horaContracciones", "");
							}
						}
						
						
						if(!fechaEgreso.equals("")&&UtilidadFecha.conversionFormatoFechaABD(aux0).compareTo(fechaEgreso)>0)
						{
							if(!inicioPartoEsCesarea)
								errores.add("Fecha Contracciones Mayor a Actual",new ActionMessage("errors.fechaPosteriorIgualActual","de contracciones","de egreso en orden de salida"));
							else
								this.setInfoParto("fechaContracciones", "");
						}
						else if(horaValida&&!horaEgreso.equals("")&&!fechaEgreso.equals("")&&!UtilidadFecha.compararFechas(UtilidadFecha.conversionFormatoFechaAAp(fechaEgreso),horaEgreso,aux0,this.getInfoParto("horaContracciones").toString()).isTrue())
						{
							if(!inicioPartoEsCesarea)
								errores.add("Fecha/Hora Contracciones Mayor a Egreso",new ActionMessage("errors.fechaHoraPosteriorIgualActual","de contracciones","de egreso en orden de salida"));
							else
							{
								this.setInfoParto("fechaContracciones", "");
								this.setInfoParto("horaContracciones", "");
							}
						}
					}
					
					if(!horaValida)
					{
						if(!inicioPartoEsCesarea)
							errores.add("Hora Contracciones Inválida",new ActionMessage("errors.formatoHoraInvalido","de contracciones"));
						else
							this.setInfoParto("horaContracciones", "");
					}
					
					//Corticoides Antenatales--------------------------------------------------------------------------------------
					if(this.getInfoParto("corticoides").toString().equals(""))
						errores.add("corticoides antenatales es requerido",new ActionMessage("errors.required","Corticoides antenatales"));
					
					//Fecha/Hora Expulsión Tapón Mucoso--------------------------------------------------------------------------------------
					horaValida = true;
					aux0 = this.getInfoParto("horaExpulsion")==null?"":this.getInfoParto("horaExpulsion").toString();
					if(!UtilidadFecha.validacionHora(aux0).puedoSeguir)
						horaValida = false;
					
					aux0 = this.getInfoParto("fechaExpulsion")==null?"":this.getInfoParto("fechaExpulsion").toString();
					
					if(this.getInfoParto("expulsionTapon").toString().equals(ConstantesBD.acronimoSi))
					{
						
						if(!UtilidadFecha.validarFecha(aux0))
						{
							if(!inicioPartoEsCesarea)
								errores.add("Fecha Expulsion Inválidas",new ActionMessage("errors.formatoFechaInvalido","de expulsión tapón mucoso"));
							else
							{
								this.setInfoParto("fechaExpulsion", "");
								this.setInfoParto("expulsionTapon", ConstantesBD.acronimoNo);
							}
						}
						else 
						{
							if(UtilidadFecha.conversionFormatoFechaABD(aux0).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
							{
								if(!inicioPartoEsCesarea)
									errores.add("Fecha Expulsión Tapón Mucoso Mayor a Actual",new ActionMessage("errors.fechaPosteriorIgualActual","de expulsión tapón mucoso","actual"));
								else
								{
									this.setInfoParto("fechaExpulsion", "");
									this.setInfoParto("expulsionTapon", ConstantesBD.acronimoNo);
								}
							}
							else if(horaValida&&!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),aux0,this.getInfoParto("horaExpulsion").toString()).isTrue())
							{
								if(!inicioPartoEsCesarea)
									errores.add("Fecha/Hora Expulsión Tapón Mucoso Mayor a Actual",new ActionMessage("errors.fechaHoraPosteriorIgualActual","de expulsión tapón mucoso","actual"));
								else
								{
									this.setInfoParto("fechaExpulsion", "");
									this.setInfoParto("horaExpulsion", "");
									this.setInfoParto("expulsionTapon", ConstantesBD.acronimoNo);
								}
							}
							
							
							if(!fechaEgreso.equals("")&&UtilidadFecha.conversionFormatoFechaABD(aux0).compareTo(fechaEgreso)>0)
							{
								if(!inicioPartoEsCesarea)
									errores.add("Fecha Expulsión Tapón Mucoso Mayor a Actual",new ActionMessage("errors.fechaPosteriorIgualActual","de expulsión tapón mucoso","de egreso en orden de salida"));
								else
								{
									this.setInfoParto("fechaExpulsion", "");
									this.setInfoParto("expulsionTapon", ConstantesBD.acronimoNo);
								}
							}
							else if(horaValida&&!horaEgreso.equals("")&&!fechaEgreso.equals("")&&!UtilidadFecha.compararFechas(UtilidadFecha.conversionFormatoFechaAAp(fechaEgreso),horaEgreso,aux0,this.getInfoParto("horaExpulsion").toString()).isTrue())
							{
								if(!inicioPartoEsCesarea)
									errores.add("Fecha/Hora Expulsión Tapón Mucoso Mayor a Egreso",new ActionMessage("errors.fechaHoraPosteriorIgualActual","de expulsión tapón mucoso","de egreso en orden de salida"));
								else
								{
									this.setInfoParto("fechaExpulsion", "");
									this.setInfoParto("horaExpulsion", "");
									this.setInfoParto("expulsionTapon", ConstantesBD.acronimoNo);
								}
								
							}
						}
						
						if(!horaValida)
						{
							if(!inicioPartoEsCesarea)
								errores.add("Hora Expulsión Tapón Mucoso Inválida",new ActionMessage("errors.formatoHoraInvalido","de expulsión tapón mucoso"));
							else
							{
								this.setInfoParto("horaExpulsion", "");
								this.setInfoParto("expulsionTapon", ConstantesBD.acronimoNo);
							}
								
						}
					}
					
					
					
					//Fecha/Hora Ruptura Membranas--------------------------------------------------------------------------------------
					if(this.getInfoParto("rupturaMembranas")!=null&&this.getInfoParto("rupturaMembranas").toString().equals(ConstantesBD.acronimoSi))
					{
						horaValida = true;
						aux0 = this.getInfoParto("horaRuptura").toString();
						if(!UtilidadFecha.validacionHora(aux0).puedoSeguir)
							horaValida = false;
						
						aux0 = this.getInfoParto("fechaRuptura").toString();
						if(!UtilidadFecha.validarFecha(aux0))
						{
							if(!inicioPartoEsCesarea)
								errores.add("Fecha Ruptura Inválidas",new ActionMessage("errors.formatoFechaInvalido","de ruptura de membranas"));
							else
							{
								this.setInfoParto("fechaRuptura", "");
								this.setInfoParto("rupturaMembranas", ConstantesBD.acronimoNo);
							}
						}
						else 
						{
							if(UtilidadFecha.conversionFormatoFechaABD(aux0).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
							{
								if(!inicioPartoEsCesarea)
									errores.add("Fecha Ruptura Membranas Mayor a Actual",new ActionMessage("errors.fechaPosteriorIgualActual","de ruptura de membranas","actual"));
								else
								{
									this.setInfoParto("fechaRuptura", "");
									this.setInfoParto("rupturaMembranas", ConstantesBD.acronimoNo);
								}
							}
							else if(horaValida&&!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),aux0,this.getInfoParto("horaRuptura").toString()).isTrue())
							{
								if(!inicioPartoEsCesarea)
									errores.add("Fecha/Hora Ruptura Membranas Mayor a Actual",new ActionMessage("errors.fechaHoraPosteriorIgualActual","de ruptura de membranas","actual"));
								else
								{
									this.setInfoParto("fechaRuptura", "");
									this.setInfoParto("horaRuptura", "");
									this.setInfoParto("rupturaMembranas", ConstantesBD.acronimoNo);
								}
							}
							
							if(!fechaEgreso.equals("")&&UtilidadFecha.conversionFormatoFechaABD(aux0).compareTo(fechaEgreso)>0)
							{
								if(!inicioPartoEsCesarea)
									errores.add("Fecha Ruptura Membranas Mayor a Actual",new ActionMessage("errors.fechaPosteriorIgualActual","de ruptura de membranas","de egreso en orden de salida"));
								else
								{
									this.setInfoParto("fechaRuptura", "");
									this.setInfoParto("rupturaMembranas", ConstantesBD.acronimoNo);
								}
							}
							else if(horaValida&&!horaEgreso.equals("")&&!fechaEgreso.equals("")&&!UtilidadFecha.compararFechas(UtilidadFecha.conversionFormatoFechaAAp(fechaEgreso),horaEgreso,aux0,this.getInfoParto("horaRuptura").toString()).isTrue())
							{
								if(!inicioPartoEsCesarea)
									errores.add("Fecha/Hora ruptura membranas Mayor a Egreso",new ActionMessage("errors.fechaHoraPosteriorIgualActual","de ruptura de membranas","de egreso en orden de salida"));
								else
								{
									this.setInfoParto("fechaRuptura", "");
									this.setInfoParto("horaRuptura", "");
									this.setInfoParto("rupturaMembranas", ConstantesBD.acronimoNo);
								}
							}
						}
						
						if(!horaValida)
						{
							if(!inicioPartoEsCesarea)
								errores.add("Hora Ruptura Membranas Inválida",new ActionMessage("errors.formatoHoraInvalido","de ruptura de membranas"));
							else
							{
								this.setInfoParto("horaRuptura", "");
								this.setInfoParto("rupturaMembranas", ConstantesBD.acronimoNo);
							}
						}
					}
						
					//Fecha/Hora Sangrado--------------------------------------------------------------------------------------
					if(this.getInfoParto("sangrado").toString().equals(ConstantesBD.acronimoSi))
					{
						horaValida = true;
						aux0 = this.getInfoParto("horaSangrado").toString();
						if(!UtilidadFecha.validacionHora(aux0).puedoSeguir)
							horaValida = false;
						
						aux0 = this.getInfoParto("fechaSangrado").toString();
						if(!UtilidadFecha.validarFecha(aux0))
							errores.add("Fecha Sangrado Inválidas",new ActionMessage("errors.formatoFechaInvalido","de sangrado"));
						else 
						{
							if(UtilidadFecha.conversionFormatoFechaABD(aux0).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
								errores.add("Fecha Sangrado Mayor a Actual",new ActionMessage("errors.fechaPosteriorIgualActual","de sangrado","actual"));
							else if(horaValida&&!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),aux0,this.getInfoParto("horaSangrado").toString()).isTrue())
								errores.add("Fecha/Hora Sangrado Mayor a Actual",new ActionMessage("errors.fechaHoraPosteriorIgualActual","de sangrado","actual"));
							
							if(UtilidadFecha.conversionFormatoFechaABD(this.getInfoParto("fechaAdmision").toString()).compareTo(UtilidadFecha.conversionFormatoFechaABD(aux0))>0)
								errores.add("Fecha Sangrado Menor a Fecha Admision",new ActionMessage("errors.fechaAnteriorIgualActual","de sangrado","de admisión "+this.getInfoParto("fechaAdmision")));
							else if(horaValida&&!UtilidadFecha.compararFechas(aux0,this.getInfoParto("horaSangrado").toString(),this.getInfoParto("fechaAdmision").toString(),this.getInfoParto("horaAdmision").toString()).isTrue())
								errores.add("Fecha/Hora Sangrado Menor a Fecha/Hora Admision",new ActionMessage("errors.fechaHoraAnteriorIgualActual","de sangrado","de admisión "+this.getInfoParto("fechaAdmision")+" "+this.getInfoParto("horaAdmision")));
							
							if(!fechaEgreso.equals("")&&UtilidadFecha.conversionFormatoFechaABD(aux0).compareTo(fechaEgreso)>0)
								errores.add("Fecha Sangrado Mayor a Actual",new ActionMessage("errors.fechaPosteriorIgualActual","de sangrado","de egreso en orden de salida"));
							else if(horaValida&&!horaEgreso.equals("")&&!fechaEgreso.equals("")&&!UtilidadFecha.compararFechas(UtilidadFecha.conversionFormatoFechaAAp(fechaEgreso),horaEgreso,aux0,this.getInfoParto("horaSangrado").toString()).isTrue())
								errores.add("Fecha/Hora Sangrado Mayor a Egreso",new ActionMessage("errors.fechaHoraPosteriorIgualActual","de sangrado","de egreso en orden de salida"));
						}
						
						if(!horaValida)
							errores.add("Hora Sangrado Inválida",new ActionMessage("errors.formatoHoraInvalido","de sangrado"));
					}
					
					//Presentación--------------------------------------------------------------------------------------
					if(this.getInfoParto("presentacion").toString().equals(""))
						errores.add("presentacion es requerido",new ActionMessage("errors.required","(Sección Partograma) La presentación"));
					
					//Acompañante en T. de P--------------------------------------------------------------------------------------
					if(this.getInfoParto("acompanante").toString().equals(""))
						errores.add("Acompañante es requerido",new ActionMessage("errors.required","Acompañante en T. de P."));
					
					//Enema/Rasurado--------------------------------------------------------------------------------------
					if(this.getInfoParto("enemaRasurado").toString().equals(""))
						errores.add("Enema/Rasurado es requerido",new ActionMessage("errors.required","Enema/Rasurado"));
					
					//Fecha/Hora Parto----------------------------------------------------------------------------------------------
					horaValida = true;
					aux0 = this.getInfoParto("horaParto").toString();
					if(!UtilidadFecha.validacionHora(aux0).puedoSeguir)
						horaValida = false;
					
					aux0 = this.getInfoParto("fechaParto").toString();
					if(!UtilidadFecha.validarFecha(aux0))
						errores.add("Fecha Parto Inválidas",new ActionMessage("errors.formatoFechaInvalido","del parto"));
					else 
					{
						if(UtilidadFecha.conversionFormatoFechaABD(aux0).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
							errores.add("Fecha Parto Mayor a Actual",new ActionMessage("errors.fechaPosteriorIgualActual","del parto","actual"));
						else if(horaValida&&!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),aux0,this.getInfoParto("horaParto").toString()).isTrue())
							errores.add("Fecha/Hora Parto Mayor a Actual",new ActionMessage("errors.fechaHoraPosteriorIgualActual","del parto","actual"));
						
						if(UtilidadFecha.conversionFormatoFechaABD(this.getInfoParto("fechaAdmision").toString()).compareTo(UtilidadFecha.conversionFormatoFechaABD(aux0))>0)
							errores.add("Fecha Parto Menor a Fecha Admision",new ActionMessage("errors.fechaAnteriorIgualActual","del parto","de admisión "+this.getInfoParto("fechaAdmision")));
						else if(horaValida&&!UtilidadFecha.compararFechas(aux0,this.getInfoParto("horaParto").toString(),this.getInfoParto("fechaAdmision").toString(),this.getInfoParto("horaAdmision").toString()).isTrue())
							errores.add("Fecha/Hora Parto Menor a Fecha/Hora Admision",new ActionMessage("errors.fechaHoraAnteriorIgualActual","del parto","de admisión "+this.getInfoParto("fechaAdmision")+" "+this.getInfoParto("horaAdmision")));
						
						if(!fechaEgreso.equals("")&&UtilidadFecha.conversionFormatoFechaABD(aux0).compareTo(fechaEgreso)>0)
							errores.add("Fecha Parto Mayor a Actual",new ActionMessage("errors.fechaPosteriorIgualActual","del parto","de egreso en orden de salida"));
						else if(horaValida&&!horaEgreso.equals("")&&!fechaEgreso.equals("")&&!UtilidadFecha.compararFechas(UtilidadFecha.conversionFormatoFechaAAp(fechaEgreso),horaEgreso,aux0,this.getInfoParto("horaParto").toString()).isTrue())
							errores.add("Fecha/Hora Parto Mayor a Egreso",new ActionMessage("errors.fechaHoraPosteriorIgualActual","del parto","de egreso en orden de salida"));
					}
					
					if(!horaValida)
						errores.add("Hora Parto Inválida",new ActionMessage("errors.formatoHoraInvalido","del parto"));
					
					//Posicion en Parto ----------------------------------------------------
					if(this.getInfoParto("posicionParto").toString().equals(""))
						errores.add("posicion en parto es requerido",new ActionMessage("errors.required","Posición en parto"));
						
					//Episiotomia--------------------------------------------------------------------------------------
					if(this.getInfoParto("episiotomia").toString().equals(""))
						errores.add("episiotomia es requerido",new ActionMessage("errors.required","Episiotomia"));
											
					//Terminacion--------------------------------------------------------------------------------------
					if(this.getInfoParto("terminacion").toString().equals(""))
						errores.add("Terminación es requerido",new ActionMessage("errors.required","Terminación"));
					
					//Indicacion principal del parto -------------------------------------------------------
					if(this.getInfoParto("indicacionParto").toString().equals("")&&!this.getInfoParto("terminacion").toString().equals(ConstantesIntegridadDominio.acronimoEspontanea))
						errores.add("Indicacion principal parto es requerido",new ActionMessage("errors.required","Indicación principal parto"));
					
					//Placenta--------------------------------------------------------------------------------------
					if(this.getInfoParto("placenta").toString().equals(""))
						errores.add("Placenta es requerido",new ActionMessage("errors.required","Placenta"));
					
					//Oxitocicos en Alumbramiento--------------------------------------------------------------------------------------
					if(this.getInfoParto("oxitocicos").toString().equals(""))
						errores.add("Ocitócicos en Alumbramiento es requerido",new ActionMessage("errors.required","Ocitócicos en Alumbramiento"));
					
					//N° Hijos vivos--------------------------------------------------------------------------------------------
					if(this.getInfoParto("cantidadHijosVivos").toString().equals(""))
						errores.add("N° hijos vivos del parto actual es requerido",new ActionMessage("errors.required","N° hijos vivos del parto actual"));
					
					//N° Hijos muertos--------------------------------------------------------------------------------------------
					if(this.getInfoParto("cantidadHijosMuertos").toString().equals(""))
						errores.add("N° hijos muertos del parto actual es requerido",new ActionMessage("errors.required","N° hijos muertos del parto actual"));
					
					//Validacion cantidad Hijos y cantidad Recien Nacidos-------------------------------------------------
					int cantHVivos = this.getInfoParto("cantidadHijosVivos").toString().equals("")?0:Integer.parseInt(this.getInfoParto("cantidadHijosVivos").toString());
					int cantHMuertos = this.getInfoParto("cantidadHijosMuertos").toString().equals("")?0:Integer.parseInt(this.getInfoParto("cantidadHijosMuertos").toString());
					if((cantHVivos+cantHMuertos)<Integer.parseInt(this.getInfoParto("cantidadRecienNacidos").toString()))
						errores.add("cantidad de hijos debe ser >= a la cantidad recien nacidos",new ActionMessage("errors.MayorIgualQue","La cantidad de hijos","la cantidad de recién nacidos "+this.getInfoParto("cantidadRecienNacidos")));
					
					//Validacion cantidadHijos (debe ser mayor que 0)
					if(!this.getInfoParto("cantidadHijos").toString().equals(""))
					{
						int cantidadHijos = Integer.parseInt(this.getInfoParto("cantidadHijos").toString());
						if(cantidadHijos<=0)
							errores.add("cantidad de hijos <= 0",new ActionMessage("errors.integerMayorQue","La cantidad de hijos","0"));
					}
			
					
					//Ligadura Cordón--------------------------------------------------------------------------------------
					if(this.getInfoParto("ligadura").toString().equals(""))
						errores.add("Ligadura Cordón es requerido",new ActionMessage("errors.required","Ligadura Cordón"));
					
					//Fecha/Hora Egreso----------------------------------------------------------------------------------------------
					horaValida = true;
					aux0 = this.getInfoParto("horaEgreso").toString();
					if(!UtilidadFecha.validacionHora(aux0).puedoSeguir)
						horaValida = false;
					
					aux0 = this.getInfoParto("fechaEgreso").toString();
					if(!UtilidadFecha.validarFecha(aux0))
						errores.add("Fecha Parto Inválidas",new ActionMessage("errors.formatoFechaInvalido","del egreso materno"));
					else 
					{
						if(UtilidadFecha.conversionFormatoFechaABD(aux0).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
							errores.add("Fecha Egreso Mayor a Actual",new ActionMessage("errors.fechaPosteriorIgualActual","del egreso materno","actual"));
						else if(horaValida&&!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),aux0,this.getInfoParto("horaEgreso").toString()).isTrue())
							errores.add("Fecha/Hora Egreso Mayor a Actual",new ActionMessage("errors.fechaHoraPosteriorIgualActual","del egreso materno","actual"));
						
						if(UtilidadFecha.conversionFormatoFechaABD(this.getInfoParto("fechaAdmision").toString()).compareTo(UtilidadFecha.conversionFormatoFechaABD(aux0))>0)
							errores.add("Fecha Egreso Menor a Fecha Admision",new ActionMessage("errors.fechaAnteriorIgualActual","del egreso materno","de admisión "+this.getInfoParto("fechaAdmision")));
						else if(horaValida&&!UtilidadFecha.compararFechas(aux0,this.getInfoParto("horaEgreso").toString(),this.getInfoParto("fechaAdmision").toString(),this.getInfoParto("horaAdmision").toString()).isTrue())
							errores.add("Fecha/Hora Egreso Menor a Fecha/Hora Admision",new ActionMessage("errors.fechaHoraAnteriorIgualActual","del egreso materno","de admisión "+this.getInfoParto("fechaAdmision")+" "+this.getInfoParto("horaAdmision")));
						
						if(!fechaEgreso.equals("")&&UtilidadFecha.conversionFormatoFechaABD(aux0).compareTo(fechaEgreso)>0)
							errores.add("Fecha Egreso Mayor a Actual",new ActionMessage("errors.fechaPosteriorIgualActual","del egreso materno","de egreso en orden de salida"));
						else if(horaValida&&!horaEgreso.equals("")&&!fechaEgreso.equals("")&&!UtilidadFecha.compararFechas(UtilidadFecha.conversionFormatoFechaAAp(fechaEgreso),horaEgreso,aux0,this.getInfoParto("horaEgreso").toString()).isTrue())
							errores.add("Fecha/Hora Egreso Mayor a Egreso",new ActionMessage("errors.fechaHoraPosteriorIgualActual","del egreso materno","de egreso en orden de salida"));
					}
					
					if(!horaValida)
						errores.add("Hora Egreso Inválida",new ActionMessage("errors.formatoHoraInvalido","del egreso materno"));
					
					//Condición Egreso--------------------------------------------------------------------------------------
					if(this.getInfoParto("condicionEgreso").toString().equals(""))
						errores.add("Condicion Egreso es requerido",new ActionMessage("errors.required","Condición (Egreso Materno)"));
					
					//Antirubeola Pos-Parto--------------------------------------------------------------------------------------
					if(this.getInfoParto("antirubeola")==null||this.getInfoParto("antirubeola").toString().equals(""))
						errores.add("Antirubeola es requerido",new ActionMessage("errors.required","El campo Antirubeola Post-Parto (Egreso Materno)"));
					
					//Anticoncepcion--------------------------------------------------------------------------------------
					if(this.getInfoParto("anticoncepcion").toString().equals(""))
						errores.add("Anticoncepción es requerido",new ActionMessage("errors.required","Anticoncepción (Egreso Materno)"));
					
					/****************************************************************/
				}
				else if(this.getInfoParto("parto").toString().equals(ConstantesBD.acronimoNo))
				{
					//******CUANDO ES ABORTO SOLO SE VALIDA LA FECHA/HORA Y EDAD GESTACIONAL
					
					
					//Semanas gestacional--------------------------------------------------------------------------------------
					if(this.getInfoParto("semanas").toString().equals(""))
						errores.add("las semanas de gestacion es requerido",new ActionMessage("errors.required","semanas (edad gestacional)"));
					
					//días gestacional--------------------------------------------------------------------------------------
					if(this.getInfoParto("dias").toString().equals(""))
						errores.add("los días de gestacion es requerido",new ActionMessage("errors.required","días (edad gestacional)"));
					
					//Por gestacional--------------------------------------------------------------------------------------
					if(this.getInfoParto("porGestacional").toString().equals(""))
						errores.add("por gestacion es requerido",new ActionMessage("errors.required","por (edad gestacional)"));
					
					//Validación del número de hijos aborto---------------------------------------------------------------------------------
					if(this.getInfoParto("numeroHijosAborto")==null||this.getInfoParto("numeroHijosAborto").toString().equals(""))
						errores.add("numero hijos aborto es requerido",new ActionMessage("errors.required","El campo N° hijos aborto"));
					else if(Integer.parseInt(this.getInfoParto("numeroHijosAborto").toString())<=0)
						errores.add("numero hijos aborto es requerido",new ActionMessage("errors.integerMayorQue","El campo N° hijos aborto","0"));
					else
					{
						for(int i=0;i<Integer.parseInt(this.getInfoParto("numeroHijosAborto").toString());i++)
						{
							//validacion del peso del Hijo i
							if(this.getInfoParto("pesoAborto_"+i).toString().equals(""))
								errores.add("peso del hijo aborto es requerido",new ActionMessage("errors.required","El peso del hijo N° "+(i+1)));
							else if(Integer.parseInt(this.getInfoParto("pesoAborto_"+i).toString())<0)
								errores.add("numero hijos aborto es requerido",new ActionMessage("errors.integerMayorIgualQue","El peso del hijo N° "+(i+1),"0"));
							
						}
					}
					
					//Fecha/Hora Parto----------------------------------------------------------------------------------------------
					boolean horaValida = true;
					String aux0 = this.getInfoParto("horaParto").toString();
					if(!UtilidadFecha.validacionHora(aux0).puedoSeguir)
						horaValida = false;
					
					aux0 = this.getInfoParto("fechaParto").toString();
					if(!UtilidadFecha.validarFecha(aux0))
						errores.add("Fecha Parto Inválidas",new ActionMessage("errors.formatoFechaInvalido","del aborto (atención del parto)"));
					else 
					{
						if(UtilidadFecha.conversionFormatoFechaABD(aux0).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
							errores.add("Fecha Parto Mayor a Actual",new ActionMessage("errors.fechaPosteriorIgualActual","del aborto (atención del parto)","actual"));
						else if(horaValida&&!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),aux0,this.getInfoParto("horaParto").toString()).isTrue())
							errores.add("Fecha/Hora Parto Mayor a Actual",new ActionMessage("errors.fechaHoraPosteriorIgualActual","del aborto (atención del parto)","actual"));
						
						if(UtilidadFecha.conversionFormatoFechaABD(this.getInfoParto("fechaAdmision").toString()).compareTo(UtilidadFecha.conversionFormatoFechaABD(aux0))>0)
							errores.add("Fecha Parto Menor a Fecha Admision",new ActionMessage("errors.fechaAnteriorIgualActual","del aborto (atención del parto)","de admisión "+this.getInfoParto("fechaAdmision")));
						else if(horaValida&&!UtilidadFecha.compararFechas(aux0,this.getInfoParto("horaParto").toString(),this.getInfoParto("fechaAdmision").toString(),this.getInfoParto("horaAdmision").toString()).isTrue())
							errores.add("Fecha/Hora Parto Menor a Fecha/Hora Admision",new ActionMessage("errors.fechaHoraAnteriorIgualActual","del aborto (atención del parto)","de admisión "+this.getInfoParto("fechaAdmision")+" "+this.getInfoParto("horaAdmision")));
						
						if(!fechaEgreso.equals("")&&UtilidadFecha.conversionFormatoFechaABD(aux0).compareTo(fechaEgreso)>0)
							errores.add("Fecha Parto Mayor a Actual",new ActionMessage("errors.fechaPosteriorIgualActual","del aborto (atención del parto)","de egreso en orden de salida"));
						else if(horaValida&&!horaEgreso.equals("")&&!fechaEgreso.equals("")&&!UtilidadFecha.compararFechas(UtilidadFecha.conversionFormatoFechaAAp(fechaEgreso),horaEgreso,aux0,this.getInfoParto("horaParto").toString()).isTrue())
							errores.add("Fecha/Hora Parto Mayor a Egreso",new ActionMessage("errors.fechaHoraPosteriorIgualActual","del aborto (atención del parto)","de egreso en orden de salida"));
					}
					
					if(!horaValida)
						errores.add("Hora Parto Inválida",new ActionMessage("errors.formatoHoraInvalido","del aborto (atención del parto)"));
					
					errores = validacionFechasHorasValidas(errores,inicioPartoEsCesarea);
				}
				
				//*******VALIDACION REQUERIDOS AL FINALIZAR BIEN SEA PARTO O ABORTO*******************
				
				//Validacion de tiene Carne
				if(this.getInfoParto("tieneCarne")==null||this.getInfoParto("tieneCarne").toString().equals(""))
					errores.add("requerido tiene carne",new ActionMessage("errors.required","el campo tiene carné"));
				
				//***************************************************************************************
				
			}
			else
			{
				
				//FEcha/hora parto------------------------------------------------------------------------------------------------------
				if(!this.getInfoParto("fechaParto").toString().equals("")&&!UtilidadFecha.validarFecha(this.getInfoParto("fechaParto").toString()))
					errores.add("Fecha Parto Inválidas",new ActionMessage("errors.formatoFechaInvalido","del parto/aborto"));
				if(!this.getInfoParto("horaParto").toString().equals("")&&!UtilidadFecha.validacionHora(this.getInfoParto("horaParto").toString()).puedoSeguir)
					errores.add("Hora Parto Inválida",new ActionMessage("errors.formatoHoraInvalido","del parto/aborto"));
				
				errores = validacionFechasHorasValidas(errores,inicioPartoEsCesarea);
				
			}
			
			//Grado Desgarros--------------------------------------------------------------------------------------
			try
			{
				if(!this.getInfoParto("gradoDesgarros").toString().equals("")&&
					(Integer.parseInt(this.getInfoParto("gradoDesgarros").toString())<1||Integer.parseInt(this.getInfoParto("gradoDesgarros").toString())>4))
					errores.add("Rango del grado de desgarros",new ActionMessage("errors.range","El grado de desgarros","1","4"));
			}
			catch(NumberFormatException e)
			{
				logger.warn("El campo grado no era numérico!!");
			}
			
			//Se verifica longitud del campo observaciones -------------------------------------------------------
			if(this.getInfoParto("observacionesNuevas")!=null&&this.getInfoParto("observacionesNuevas").toString().length()>4000)
				errores.add("sobrepasa longitud",new ActionMessage("errors.maxlength","El campo observaciones","4000"));
			
			
			//EN EL ESTADO GUARDAR PARA LA SECCION DE VIGILANCIA TRAB PARTO NO IMPORTA SI FINALIZAO O NO
			boolean centinelaError=false;
			boolean existeRegistroVigencia=false;
			
			
			for(int w=0; w<Integer.parseInt(this.getVigilanciaClinicaMap("numRegistros").toString()); w++)
			{
				logger.info("INDEX-->"+w);
				if(!this.getVigilanciaClinicaMap("fueeliminado_"+w).toString().equals("true"))
				{
					existeRegistroVigencia=true;
					//se valida primero la fecha
					if(!UtilidadFecha.esFechaValidaSegunAp(this.getVigilanciaClinicaMap("fecha_"+w).toString()))
					{
						errores.add("", new ActionMessage("errors.formatoFechaInvalido", "(Sección Partograma) "+this.getVigilanciaClinicaMap("fecha_"+w).toString()));
						centinelaError=true;
					}
					//se valida que sea menor igual que la del sistema
					else 
					{
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getVigilanciaClinicaMap("fecha_"+w).toString(), UtilidadFecha.getFechaActual()))
						{
							errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "(Sección Partograma) "+this.getVigilanciaClinicaMap("fecha_"+w).toString(), UtilidadFecha.getFechaActual()+" actual"));
							centinelaError=true;
						}
					}
					//se valida la hora
					if(!UtilidadFecha.validacionHora(this.getVigilanciaClinicaMap("hora_"+w).toString()).puedoSeguir)
					{
						errores.add("", new ActionMessage("errors.formatoHoraInvalido", "(Sección Partograma) "+this.getVigilanciaClinicaMap("hora_"+w).toString()));
						centinelaError=true;
					}
					
					//en caso de que la fecha y hora esten bien entonces se evalua que sea menor a la del sistema
					if(!centinelaError)
					{
						if (!UtilidadFecha.compararFechas( UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(),this.getVigilanciaClinicaMap("fecha_"+w).toString(), this.getVigilanciaClinicaMap("hora_"+w).toString()).isTrue()  )
						{	
                            errores.add("", new ActionMessage("errors.fechaHoraPosteriorIgualActual", "(Sección Partograma) "+this.getVigilanciaClinicaMap("fecha_"+w).toString()+" "+this.getVigilanciaClinicaMap("hora_"+w).toString(), UtilidadFecha.getFechaActual()+" "+UtilidadFecha.getHoraActual()));
                            centinelaError=true;
						}    
					}
					
					if(this.getVigilanciaClinicaMap("acronimoposicionmaterna_"+w).toString().equals(""))
					{
						errores.add("", new ActionMessage("errors.required","(Sección Partograma) El campo Posición Materna"));
						centinelaError=true;
					}
					try
					{
						int tensionArterial=0;
						tensionArterial=Integer.parseInt(this.getVigilanciaClinicaMap("tensionarterial_"+w).toString());
						if(tensionArterial>240 || tensionArterial<20)
						{
							errores.add("", new ActionMessage("errors.range", "(Sección Partograma) La Tensión Sistólica "+this.getVigilanciaClinicaMap("tensionarterial_"+w).toString(), "20", "240"));
							centinelaError=true;
						}
					}
					catch(Exception e)
					{
						errores.add("", new ActionMessage("errors.range", "(Sección Partograma) La Tensión Sistólica "+this.getVigilanciaClinicaMap("tensionarterial_"+w).toString(), "20", "240"));
						centinelaError=true;
					}
					try
					{
						int tensionArterial1=0;
						tensionArterial1=Integer.parseInt(this.getVigilanciaClinicaMap("tensionarterial1_"+w).toString());
						if(tensionArterial1>240 || tensionArterial1<20)
						{
							errores.add("", new ActionMessage("errors.range", "(Sección Partograma) La Tensión Diastólica "+this.getVigilanciaClinicaMap("tensionarterial1_"+w).toString(), "20", "240"));
							centinelaError=true;
						}
					}
					catch(Exception e)
					{
						errores.add("", new ActionMessage("errors.range", "(Sección Partograma) La Tensión Diastólica "+this.getVigilanciaClinicaMap("tensionarterial1_"+w).toString(), "20", "240"));
						centinelaError=true;
					}
					
					if(!this.getVigilanciaClinicaMap("pulsomaterno_"+w).equals(""))
					{
						try
						{
							int pulsoMaterno=0;
							pulsoMaterno=Integer.parseInt(this.getVigilanciaClinicaMap("pulsomaterno_"+w).toString());
							if(pulsoMaterno>240 || pulsoMaterno<20)
							{
								errores.add("", new ActionMessage("errors.range", "(Sección Partograma) El Pulso Materno "+this.getVigilanciaClinicaMap("pulsomaterno_"+w).toString(), "20", "240"));
								centinelaError=true;
							}
						}
						catch(Exception e)
						{
							errores.add("", new ActionMessage("errors.range", "(Sección Partograma) El Pulso Materno "+this.getVigilanciaClinicaMap("pulsomaterno_"+w).toString(), "20", "240"));
							centinelaError=true;
						}
					}
					if(this.getVigilanciaClinicaMap("acronimofreccardiacafetal_"+w).toString().equals(""))
					{
						errores.add("", new ActionMessage("errors.required","(Sección Partograma) El campo Desaceleraciones"));
						centinelaError=true;
					}
					if(!this.getVigilanciaClinicaMap("duracioncontracciones_"+w).equals(""))
					{
						try
						{
							int duracionContracciones=0;
							duracionContracciones=Integer.parseInt(this.getVigilanciaClinicaMap("duracioncontracciones_"+w).toString());
							if(duracionContracciones<1)
							{
								errores.add("", new ActionMessage("errors.integerMayorQue", "(Sección Partograma) La Duración Contracciones "+this.getVigilanciaClinicaMap("duracioncontracciones_"+w).toString(), "0"));
								centinelaError=true;
							}
						}
						catch(Exception e)
						{
							errores.add("", new ActionMessage("errors.integerMayorQue", "(Sección Partograma) La Duración Contracciones "+this.getVigilanciaClinicaMap("duracioncontracciones_"+w).toString(), "0"));
							centinelaError=true;
						}
					}
					if(!this.getVigilanciaClinicaMap("freccontracciones_"+w).toString().equals(""))
					{
						try
						{
							int frec=0;
							frec=Integer.parseInt(this.getVigilanciaClinicaMap("freccontracciones_"+w).toString());
							if(frec<1)
							{
								errores.add("", new ActionMessage("errors.integerMayorQue", "(Sección Partograma) La Frecuencia Contracciones "+this.getVigilanciaClinicaMap("freccontracciones_"+w).toString(), "0"));
								centinelaError=true;
							}
						}
						catch(Exception e)
						{
							errores.add("", new ActionMessage("errors.integerMayorQue", "(Sección Partograma) La Frecuencia Contracciones "+this.getVigilanciaClinicaMap("freccontracciones_"+w).toString(), "0"));
							centinelaError=true;
						}
					}
					if(this.getVigilanciaClinicaMap("acronimolocalizaciondolor_"+w).toString().equals(""))
					{
						errores.add("", new ActionMessage("errors.required", "(Sección Partograma) El campo Localización Dolor"));
						centinelaError=true;
					}
					if(this.getVigilanciaClinicaMap("acronimointensidaddolor_"+w).toString().equals(""))
					{
						errores.add("", new ActionMessage("errors.required", "(Sección Partograma) El campo Intensidad Dolor"));
						centinelaError=true;
					}
					
					int dilatacion=0;
					try
					{
						dilatacion=Integer.parseInt(this.getVigilanciaClinicaMap("dilatacion_"+w).toString().trim());
					}
					catch(Exception e)
					{
						errores.add("", new ActionMessage("errors.integerMayorIgualQue", "(Sección Partograma) La Dilatación "+this.getVigilanciaClinicaMap("dilatacion_"+w).toString(), "0 y menor igual 11"));
						centinelaError=true;
					}
					if(dilatacion<0 || dilatacion>11)
					{
						errores.add("", new ActionMessage("errors.integerMayorIgualQue", "(Sección Partograma) La Dilatación "+this.getVigilanciaClinicaMap("dilatacion_"+w).toString(), "0 y menor igual 11"));
						centinelaError=true;
					}
					
					if(this.getVigilanciaClinicaMap("codigovariedadposicion_"+w).toString().equals(""))
					{
						errores.add("", new ActionMessage("errors.required", "(Sección Partograma) El campo Variedad Posición"));
						centinelaError=true;
					}
					
					int fcf=10;
					try
					{
						fcf=Integer.parseInt(this.getVigilanciaClinicaMap("fcf_"+w).toString().trim());
					}
					catch(Exception e)
					{
						errores.add("", new ActionMessage("errors.integerMayorIgualQue", "(Sección Partograma) La FCF "+this.getVigilanciaClinicaMap("fcf_"+w).toString(), "10 y menor igual 300"));
						centinelaError=true;
					}
					if(fcf<10 || fcf>300)
					{
						errores.add("", new ActionMessage("errors.integerMayorIgualQue", "(Sección Partograma) La FCF "+this.getVigilanciaClinicaMap("fcf_"+w).toString(), "10 y menor igual 300"));
						centinelaError=true;
					}
					
					int estacion=0;
					try
					{
						estacion=Integer.parseInt(this.getVigilanciaClinicaMap("estacion_"+w).toString().trim());
					}
					catch(Exception e)
					{
						errores.add("", new ActionMessage("errors.integerMayorIgualQue", "(Sección Partograma) La Estación "+this.getVigilanciaClinicaMap("estacion_"+w).toString(), "-4 y menor igual 4"));
						centinelaError=true;
					}
					if(estacion<-4 || estacion>4)
					{
						errores.add("", new ActionMessage("errors.integerMayorIgualQue", "(Sección Partograma) La Estación "+this.getVigilanciaClinicaMap("estacion_"+w).toString(), "-4 y menor igual 4"));
						centinelaError=true;
					}
					
					
					if(centinelaError)
					{
						this.seccionVigilanciaClinica=true;
						this.setEstado("detalle");
					}
				}//if (no estabd y no fueeliminado mem)	
			}// fin for vig trab parto
			
			if(existeRegistroVigencia)
			{
				if(this.getPartogramaMap("codigoposicion").toString().equals(""))
				{
					errores.add("", new ActionMessage("errors.required","(Sección Partograma) Si ingresa una dilatación valida entonces el campo Posición"));
					centinelaError=true;
				}
				if(this.getPartogramaMap("codigoparidad").toString().equals(""))
				{
					errores.add("", new ActionMessage("errors.required","(Sección Partograma) Si ingresa una dilatación valida entonces el campo Paridad"));
					centinelaError=true;
				}
				if(this.getPartogramaMap("codigomembrana").toString().equals(""))
				{
					errores.add("", new ActionMessage("errors.required","(Sección Partograma) Si ingresa una dilatación valida entonces el campo Membrana"));
					centinelaError=true;
				}
				//Presentación--------------------------------------------------------------------------------------
				if(this.getInfoParto("presentacion").toString().equals(""))
					errores.add("presentacion es requerido",new ActionMessage("errors.required","(Sección Partograma) Si ingresa una dilatación valida entonces el campo Presentación"));
			}
			
			if(!centinelaError)
			{
				for(int w=0; w<Integer.parseInt(this.getVigilanciaClinicaMap("numRegistros").toString()); w++)
				{
					if(!this.getVigilanciaClinicaMap("fueeliminado_"+w).toString().equals("true"))
					{	
						//se debe validar que no exista una misma fecha - hora
						String fecha= this.getVigilanciaClinicaMap("fecha_"+w).toString();
						String hora= this.getVigilanciaClinicaMap("hora_"+w).toString();
						
						for(int x=w+1; x<Integer.parseInt(this.getVigilanciaClinicaMap("numRegistros").toString()); x++)
						{
							String fechaSiguiente= this.getVigilanciaClinicaMap("fecha_"+x).toString();
							String horaSiguiente= this.getVigilanciaClinicaMap("hora_"+x).toString();
							
							if(fecha.equals(fechaSiguiente) && hora.equals(horaSiguiente))
							{
								errores.add("", new ActionMessage("error.partograma.mismaFechaHora", fecha, hora ));
							}
						}
					}	
				}
			}	
			if(centinelaError||!errores.isEmpty())
			{
				this.setEstado("detalle");
			}
		}
		return errores;
	}

	/**
	 * Método usado para la validación de campos numéruicos
	 * @param errores
	 * @return
	 */
	private ActionErrors validacionesCamposNumericos(ActionErrors errores) 
	{
		//Número de Embarazo -----------------------------------------------------
		if(!this.getInfoParto("numeroEmbarazo").toString().equals(""))
		{
			try
			{
				Integer.parseInt(this.getInfoParto("numeroEmbarazo").toString());
			}
			catch(Exception e)
			{
				errores.add("Numero Embarazo debe ser numéricos", new ActionMessage("errors.integer","El número de embarazo"));
			}
		}
		
		//Edad Gestacional Semanas ------------------------------------------------------------
		if(!this.getInfoParto("semanas").toString().equals(""))
		{
			try
			{
				Integer.parseInt(this.getInfoParto("semanas").toString());
			}
			catch(Exception e)
			{
				errores.add("Número de Semanas debe ser numéricos", new ActionMessage("errors.integer","El número de semanas (edad gestacional)"));
			}
		}
		
		//Edad Gestacional Días ------------------------------------------------------------
		if(!this.getInfoParto("dias").toString().equals(""))
		{
			try
			{
				Integer.parseInt(this.getInfoParto("dias").toString());
			}
			catch(Exception e)
			{
				errores.add("Número de Días debe ser numéricos", new ActionMessage("errors.integer","El número de días (edad gestacional)"));
			}
		}
		
		//Consultas Prenatales ------------------------------------------------------------
		if(!this.getInfoParto("consultasPrenatal").toString().equals(""))
		{
			try
			{
				Integer.parseInt(this.getInfoParto("consultasPrenatal").toString());
			}
			catch(Exception e)
			{
				errores.add("Número de consultas prenatales debe ser numérico", new ActionMessage("errors.integer","El número de consultas prenatales"));
			}
		}
		
		//N° hijos aborto ------------------------------------------------------------
		if(this.getInfoParto("numeroHijosAborto")!=null&&!this.getInfoParto("numeroHijosAborto").toString().equals(""))
		{
			try
			{
				Integer.parseInt(this.getInfoParto("numeroHijosAborto").toString());
			}
			catch(Exception e)
			{
				errores.add("Número de hijos aborto debe ser numérico", new ActionMessage("errors.integer","El campo N° hijos aborto"));
			}
		}
		
		//Grado Desgarros ------------------------------------------------------------
		if(!this.getInfoParto("gradoDesgarros").toString().equals(""))
		{
			try
			{
				Integer.parseInt(this.getInfoParto("gradoDesgarros").toString());
			}
			catch(Exception e)
			{
				errores.add("El campo grado desgarros", new ActionMessage("errors.integer","El campo Grado (1 a 4)"));
			}
		}
		
		//N° hijos vivos ------------------------------------------------------------
		if(!this.getInfoParto("cantidadHijosVivos").toString().equals(""))
		{
			try
			{
				Integer.parseInt(this.getInfoParto("cantidadHijosVivos").toString());
			}
			catch(Exception e)
			{
				errores.add("Número de hijos vivios debe ser numérico", new ActionMessage("errors.integer","El campo N° hijos vivos del parto actual"));
			}
		}
		
		//N° hijos muertos ------------------------------------------------------------
		if(!this.getInfoParto("cantidadHijosMuertos").toString().equals(""))
		{
			try
			{
				Integer.parseInt(this.getInfoParto("cantidadHijosMuertos").toString());
			}
			catch(Exception e)
			{
				errores.add("Número de hijos vivos debe ser numérico", new ActionMessage("errors.integer","El campo N° hijos muertos del parto actual"));
			}
		}
		
			
		return errores;
	}

	private ActionErrors validacionFechasHorasValidas(ActionErrors errores, boolean inicioPartoEsCesarea) 
	{
		//FEcha/hora contracciones------------------------------------------------------------------------------------------------------
		if(!this.getInfoParto("fechaContracciones").toString().equals("")&&!UtilidadFecha.validarFecha(this.getInfoParto("fechaContracciones").toString()))
		{
			if(!inicioPartoEsCesarea)
				errores.add("Fecha Contracciones Inválidas",new ActionMessage("errors.formatoFechaInvalido","de contracciones"));
			else
				this.setInfoParto("fechaContracciones", "");
		}
		if(!this.getInfoParto("horaContracciones").toString().equals("")&&!UtilidadFecha.validacionHora(this.getInfoParto("horaContracciones").toString()).puedoSeguir)
		{
			if(!inicioPartoEsCesarea)
				errores.add("Hora Contracciones Inválida",new ActionMessage("errors.formatoHoraInvalido","de contracciones"));
			else
				this.setInfoParto("horaContracciones", "");
				
		}
		
		
		//FEcha/hora expulsion------------------------------------------------------------------------------------------------------
		if(this.getInfoParto("expulsionTapon").toString().equals(ConstantesBD.acronimoSi))
		{
			if(!this.getInfoParto("fechaExpulsion").toString().equals("")&&!UtilidadFecha.validarFecha(this.getInfoParto("fechaExpulsion").toString()))
			{
				if(!inicioPartoEsCesarea)
					errores.add("Fecha Expulsion Inválidas",new ActionMessage("errors.formatoFechaInvalido","de expulsión tapón mucoso"));
				else
					this.setInfoParto("fechaExpulsion", "");
			}
			if(!this.getInfoParto("horaExpulsion").toString().equals("")&&!UtilidadFecha.validacionHora(this.getInfoParto("horaExpulsion").toString()).puedoSeguir)
			{
				if(!inicioPartoEsCesarea)
					errores.add("Hora Expulsion Inválida",new ActionMessage("errors.formatoHoraInvalido","de expulsión tapón mucoso"));
				else
					this.setInfoParto("horaExpulsion", "");
			}
		}
		
		//FEcha/hora ruptura de membranas------------------------------------------------------------------------------------------------------
		if(this.getInfoParto("rupturaMembranas").toString().equals(ConstantesBD.acronimoSi))
		{
			if(!this.getInfoParto("fechaRuptura").toString().equals("")&&!UtilidadFecha.validarFecha(this.getInfoParto("fechaRuptura").toString()))
			{
				if(!inicioPartoEsCesarea)
					errores.add("Fecha Ruptura Inválidas",new ActionMessage("errors.formatoFechaInvalido","de ruptura de membranas"));
				else
					this.setInfoParto("fechaRuptura", "");
			}
			if(!this.getInfoParto("horaRuptura").toString().equals("")&&!UtilidadFecha.validacionHora(this.getInfoParto("horaRuptura").toString()).puedoSeguir)
			{
				if(!inicioPartoEsCesarea)
					errores.add("Hora Ruptura Inválida",new ActionMessage("errors.formatoHoraInvalido","de ruptura de membranas"));
				else
					this.setInfoParto("horaRuptura", "");
			}
		}
		
		//FEcha/hora sangrado------------------------------------------------------------------------------------------------------
		if(this.getInfoParto("sangrado").toString().equals(ConstantesBD.acronimoSi))
		{
			if(!this.getInfoParto("fechaSangrado").toString().equals("")&&!UtilidadFecha.validarFecha(this.getInfoParto("fechaSangrado").toString()))
				errores.add("Fecha Sangrado Inválidas",new ActionMessage("errors.formatoFechaInvalido","de sangrado"));
			if(!this.getInfoParto("horaSangrado").toString().equals("")&&!UtilidadFecha.validacionHora(this.getInfoParto("horaSangrado").toString()).puedoSeguir)
				errores.add("Hora Sangrado Inválida",new ActionMessage("errors.formatoHoraInvalido","de sangrado"));
		}
		
		//FEcha/hora egreso------------------------------------------------------------------------------------------------------
		if(!this.getInfoParto("fechaEgreso").toString().equals("")&&!UtilidadFecha.validarFecha(this.getInfoParto("fechaEgreso").toString()))
			errores.add("Fecha Egreso Inválidas",new ActionMessage("errors.formatoFechaInvalido","de egreso materno"));
		if(!this.getInfoParto("horaEgreso").toString().equals("")&&!UtilidadFecha.validacionHora(this.getInfoParto("horaEgreso").toString()).puedoSeguir)
			errores.add("Hora Egreso Inválida",new ActionMessage("errors.formatoHoraInvalido","de egreso materno"));
		
		return errores;
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
	 * @return Returns the indice.
	 */
	public String getIndice() {
		return indice;
	}

	/**
	 * @param indice The indice to set.
	 */
	public void setIndice(String indice) {
		this.indice = indice;
	}

	/**
	 * @return Returns the infoParto.
	 */
	public HashMap getInfoParto() {
		return infoParto;
	}

	/**
	 * @param infoParto The infoParto to set.
	 */
	public void setInfoParto(HashMap infoParto) {
		this.infoParto = infoParto;
	}
	
	/**
	 * @return Retorna un elemento del mapa infoParto.
	 */
	public Object getInfoParto(String key) {
		return infoParto.get(key);
	}

	/**
	 * @param infoParto The infoParto to set.
	 */
	public void setInfoParto(String key,Object obj) {
		this.infoParto.put(key,obj);
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
	 * @return Returns the ultimoIndice.
	 */
	public String getUltimoIndice() {
		return ultimoIndice;
	}

	/**
	 * @param ultimoIndice The ultimoIndice to set.
	 */
	public void setUltimoIndice(String ultimoIndice) {
		this.ultimoIndice = ultimoIndice;
	}

	/**
	 * @return Returns the listadoSolicitudes.
	 */
	public HashMap getListadoSolicitudes() {
		return listadoSolicitudes;
	}

	/**
	 * @param listadoSolicitudes The listadoSolicitudes to set.
	 */
	public void setListadoSolicitudes(HashMap listadoSolicitudes) {
		this.listadoSolicitudes = listadoSolicitudes;
	}
	
	/**
	 * @return Retorna un elemento del mapa listadoSolicitudes.
	 */
	public Object getListadoSolicitudes(String key) {
		return listadoSolicitudes.get(key);
	}

	/**
	 * @param Asigna un elemento al mapa listadoSolicitudes.
	 */
	public void setListadoSolicitudes(String key,Object obj) {
		this.listadoSolicitudes.put(key,obj);
	}

	/**
	 * @return Returns the numeroSolicitud.
	 */
	public String getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @param numeroSolicitud The numeroSolicitud to set.
	 */
	public void setNumeroSolicitud(String numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	/**
	 * @return Returns the numSolicitudes.
	 */
	public int getNumSolicitudes() {
		return numSolicitudes;
	}

	/**
	 * @param numSolicitudes The numSolicitudes to set.
	 */
	public void setNumSolicitudes(int numSolicitudes) {
		this.numSolicitudes = numSolicitudes;
	}

	/**
	 * @return Returns the pos.
	 */
	public int getPos() {
		return pos;
	}

	/**
	 * @param pos The pos to set.
	 */
	public void setPos(int pos) {
		this.pos = pos;
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
	 * @return the seccionEnfermedades
	 */
	public boolean isSeccionEnfermedades() {
		return seccionEnfermedades;
	}

	/**
	 * @param seccionEnfermedades the seccionEnfermedades to set
	 */
	public void setSeccionEnfermedades(boolean seccionEnfermedades) {
		this.seccionEnfermedades = seccionEnfermedades;
	}

	/**
	 * @return the seccionIdentificacion
	 */
	public boolean isSeccionIdentificacion() {
		return seccionIdentificacion;
	}

	/**
	 * @param seccionIdentificacion the seccionIdentificacion to set
	 */
	public void setSeccionIdentificacion(boolean seccionIdentificacion) {
		this.seccionIdentificacion = seccionIdentificacion;
	}

	

	/**
	 * @return the seccionEgreso
	 */
	public boolean isSeccionEgreso() {
		return seccionEgreso;
	}

	/**
	 * @param seccionEgreso the seccionEgreso to set
	 */
	public void setSeccionEgreso(boolean seccionEgreso) {
		this.seccionEgreso = seccionEgreso;
	}

	/**
	 * @return the mapaEnfermedades
	 */
	public HashMap getMapaEnfermedades() {
		return mapaEnfermedades;
	}

	/**
	 * @param mapaEnfermedades the mapaEnfermedades to set
	 */
	public void setMapaEnfermedades(HashMap mapaEnfermedades) {
		this.mapaEnfermedades = mapaEnfermedades;
	}
	
	/**
	 * @return Retorna elemento del mapa mapaEnfermedades
	 */
	public Object getMapaEnfermedades(String key) {
		return mapaEnfermedades.get(key);
	}

	/**
	 * @param ASigna elemento al mapa mapaEnfermedades 
	 */
	public void setMapaEnfermedades(String key,Object obj) {
		this.mapaEnfermedades.put(key,obj);
	}

	/**
	 * @return the mapaIdentificacion
	 */
	public HashMap getMapaIdentificacion() {
		return mapaIdentificacion;
	}

	/**
	 * @param mapaIdentificacion the mapaIdentificacion to set
	 */
	public void setMapaIdentificacion(HashMap mapaIdentificacion) {
		this.mapaIdentificacion = mapaIdentificacion;
	}
	
	/**
	 * @return Retorna elemento del mapa mapaIdentificacion
	 */
	public Object getMapaIdentificacion(String key) {
		return mapaIdentificacion.get(key);
	}

	/**
	 * @param Asigna elemento al mapa mapaIdentificacion 
	 */
	public void setMapaIdentificacion(String key,Object obj) {
		this.mapaIdentificacion.put(key,obj);
	}

	/**
	 * @return the mapaMedicacion
	 */
	public HashMap getMapaMedicacion() {
		return mapaMedicacion;
	}

	/**
	 * @param mapaMedicacion the mapaMedicacion to set
	 */
	public void setMapaMedicacion(HashMap mapaMedicacion) {
		this.mapaMedicacion = mapaMedicacion;
	}
	
	/**
	 * @return Retorna elemento del mapa mapaMedicacion
	 */
	public Object getMapaMedicacion(String key) {
		return mapaMedicacion.get(key);
	}

	/**
	 * @param Asigna elemento al mapa mapaMedicacion
	 */
	public void setMapaMedicacion(String key,Object obj) {
		this.mapaMedicacion.put(key,obj);
	}

	/**
	 * @return the mapaTransfusion
	 */
	public HashMap getMapaTransfusion() {
		return mapaTransfusion;
	}

	/**
	 * @param mapaTransfusion the mapaTransfusion to set
	 */
	public void setMapaTransfusion(HashMap mapaTransfusion) {
		this.mapaTransfusion = mapaTransfusion;
	}
	
	/**
	 * @return Retorna elemento del mapa mapaTransfusion
	 */
	public Object getMapaTransfusion(String key) {
		return mapaTransfusion.get(key);
	}

	/**
	 * @param Asigna elemento al mapa mapaTransfusion
	 */
	public void setMapaTransfusion(String key,Object obj) {
		this.mapaTransfusion.put(key, obj);
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
	 * @return the horaEgreso
	 */
	public String getHoraEgreso() {
		return horaEgreso;
	}

	/**
	 * @param horaEgreso the horaEgreso to set
	 */
	public void setHoraEgreso(String horaEgreso) {
		this.horaEgreso = horaEgreso;
	}

	/**
	 * @return the loginUsuario
	 */
	public String getLoginUsuario() {
		return loginUsuario;
	}

	/**
	 * @param loginUsuario the loginUsuario to set
	 */
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	/**
	 * @return Returns the vigilanciaClinicaMap.
	 */
	public HashMap getVigilanciaClinicaMap()
	{
		return vigilanciaClinicaMap;
	}

	/**
	 * @param vigilanciaClinicaMap The vigilanciaClinicaMap to set.
	 */
	public void setVigilanciaClinicaMap(HashMap vigilanciaClinicaMap)
	{
		this.vigilanciaClinicaMap = vigilanciaClinicaMap;
	}
	
	/**
	 * @return Returns the vigilanciaClinicaMap.
	 */
	public Object getVigilanciaClinicaMap(Object key)
	{
		return vigilanciaClinicaMap.get(key);
	}

	/**
	 * @param vigilanciaClinicaMap The vigilanciaClinicaMap to set.
	 */
	public void setVigilanciaClinicaMap(Object key, Object value)
	{
		this.vigilanciaClinicaMap.put(key, value);
	}

	/**
	 * @return Returns the seccionVigilanciaClinica.
	 */
	public boolean isSeccionVigilanciaClinica()
	{
		return seccionVigilanciaClinica;
	}

	/**
	 * @param seccionVigilanciaClinica The seccionVigilanciaClinica to set.
	 */
	public void setSeccionVigilanciaClinica(boolean seccionVigilanciaClinica)
	{
		this.seccionVigilanciaClinica = seccionVigilanciaClinica;
	}

	/**
	 * @return Returns the partogramaMap.
	 */
	public HashMap getPartogramaMap()
	{
		return partogramaMap;
	}

	/**
	 * @param partogramaMap The partogramaMap to set.
	 */
	public void setPartogramaMap(HashMap partogramaMap)
	{
		this.partogramaMap = partogramaMap;
	}
	
	/**
	 * @return Returns the partogramaMap.
	 */
	public Object getPartogramaMap(Object key)
	{
		return partogramaMap.get(key);
	}

	/**
	 * @param partogramaMap The partogramaMap to set.
	 */
	public void setPartogramaMap(Object key, Object value)
	{
		this.partogramaMap.put(key, value);
	}

	/**
	 * @return the seccionGeneral
	 */
	public boolean isSeccionGeneral() {
		return seccionGeneral;
	}

	/**
	 * @param seccionGeneral the seccionGeneral to set
	 */
	public void setSeccionGeneral(boolean seccionGeneral) {
		this.seccionGeneral = seccionGeneral;
	}

	/**
	 * @return the seccionVigilancia
	 */
	public boolean isSeccionVigilancia() {
		return seccionVigilancia;
	}

	/**
	 * @param seccionVigilancia the seccionVigilancia to set
	 */
	public void setSeccionVigilancia(boolean seccionVigilancia) {
		this.seccionVigilancia = seccionVigilancia;
	}

	/**
	 * @return the seccionAtencion
	 */
	public boolean isSeccionAtencion() {
		return seccionAtencion;
	}

	/**
	 * @param seccionAtencion the seccionAtencion to set
	 */
	public void setSeccionAtencion(boolean seccionAtencion) {
		this.seccionAtencion = seccionAtencion;
	}
	
}
