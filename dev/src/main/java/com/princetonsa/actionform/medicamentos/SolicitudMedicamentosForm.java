/*
 * Creado en 2/09/2004
 *
 */
package com.princetonsa.actionform.medicamentos;

import java.io.Serializable;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.facturacion.InfoResponsableCobertura;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.historiaClinica.UtilidadesJustificacionNoPos;
import util.inventarios.UtilidadInventarios;

import com.princetonsa.dto.historiaClinica.DtoValoracion;
import com.princetonsa.dto.manejoPaciente.DTOProcesoAutorizacion;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.mundo.Camas1;
import com.princetonsa.mundo.CentrosCostoViaIngreso;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.Valoraciones;
import com.princetonsa.mundo.manejoPaciente.Habitaciones;
import com.princetonsa.mundo.manejoPaciente.Pisos;

/**
 * @author Juan David Ramírez López
 *
 * Princeton S.A.
 */
public class SolicitudMedicamentosForm extends ValidatorForm implements Serializable  
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5130730052550350758L;

	/**
	 * Atributo que almacena el resultado 
	 * del par&aacute;metro  Formato Impresi&oacute;n 
	 * Autorizaci&oacute;n Entidad Subcontratada
	 */
	private String formatoImpAutorizacion;
	
	/**
	 * Almacena el nombre del archivo generado para luego ser 
	 * visualizado
	 */
	private String nombreArchivoGenerado;
	
	/**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(SolicitudMedicamentosForm.class);
	
	/**
	 * Manejo de estados para el flujo de la funcionalidad
	 */
	private String estado;
	
	/**
	 * DtoDiagnostico
	 */
	private DtoDiagnostico dtoDiagnostico;

	/**
	 * Número de solicitud
	 */
	private int numeroSolicitud;
	
	/**
	 * Entero para manejar el codigo del detalle cargo ( Orden Ambulatoria - detalle Autorizacion)
	 * 	 */
	private int codigoDetalleCargo;
	
	/**
	 * Entero que contiene el codigo del Convenio del Cargo (Autorizaciones) 
	 */
	private int codigoConvenioCargo;
	
    /**
     * Entero qeu contiene  el codido de la Sub_cuenta del cargo ( Autorizaciones)
     */
	private int codigoSubCuentaCargo;
	
	
	/**
	 * numero de solicitud de los mismos medicamentos pero con diferente dosificacion
	 */
	private int numeroSolicitudMedDiferenteDosificacion;
	
	/**
	 * Manejo de consecutivo de ordenes medicas
	 */
	private int numeroOrdenMedica;
	
	/**
	 * Manejo de consecutivo de ordenes medicas de los medicamentos con diferente dosificacion
	 */
	private int numeroOrdenMedicaMedDiferenteDosificacion;
	
	/**
	 * Fecha de la solicitud
	 */
	private String fecha;
	
	/**
	 * Hora de la solicitud
	 */
	private String hora;
	
	/**
	 * 
	 */
	private String ordenAmbulatoria="";
	
	/**
	 * 
	 */
	private boolean indicativoOrdenAmbulatoria=false;
	/**
	 * Campo para seleccionar el centro de costo
	 * que se desea que despache la solicitud
	 * (Debe ser de tipo Subalmacen)
	 */
	private int centroCostoSolicitado;
	
    /**
     * nombre del centro de costo solicitado
     */
    private String nombreCentroCostoSolicitado;
    
	/**
	 * Campo para seleccionar el centro de costo
	 * que se desea que despache la solicitud
	 * (Debe ser de tipo Subalmacen)
	 */
	private int centroCostoSolicitante;
	
	/**
	 * Solicitud Urgente
	 */
	private boolean urgente;
	
	/**
	 * Orden con despachos
	 */	
	
	private boolean tieneDespacho;
	
	/**
	 * Numero de autorización de la solicitud
	 */
	//private String numeroAutorizacion;
	
	/**
	 * Observaciones generales de la solicitud
	 */
	private String observacionesGenerales;
	
	/**
	 * Campo para manejar la modificación de las observaciones
	 */
	private String observacionesGeneralesNuevas;
	
	/**
	 * Manejo del listado de los medicamentos
	 */
	private HashMap medicamentos;
	
	/**
	 * 
	 */
	private HashMap medicamentosDiferenteDosificacion;
	
	/**
	 * 
	 */
	private int codArticuloSelDiferenteDosificacion;
	
	/**
	 * Código del artículo que se desdea eliminar
	 */
	private int codigoEliminado;
	
	/**
	 * Número de elementos existentes en el hashmap
	 */
	private int numeroElementos;
	
	/**
	 * Manejar el resumen de ingreso y la consulta de solicitudes de interconsulta
	 */
	private Vector articulos;
	
	/**
	 * 
	 */
	private Vector articulosDiferenteDosificacion;
	
	/**
	 * Condigo del artículo al cual se le desea imprimir la justificación
	 */
	private int codigoArticuloAImprimir;
	
	/**
	 * Colecion para manejar los datos de la consulta de solicitudes del paciente.
	 */
	private Collection coleccion;
	
	/**
	 * Colecion para manejar los Insumos de una solicitud.
	 */
	private Collection coleccionInsumos;
		
	/**
	 * Cadena con el motivo de la anulacion
	 */
	private String motivoAnulacion;
	
	/**
	 * Booleano para mostrar el mensaje de asocio
	 */
	private boolean mostrarMensajeAsocio; 
	/**
	 * Booleana que indica si puedo modificar una solicitud
	 */
	private boolean puedoModificar;
	/**
	 * Booleana que indica si puedo suspender un articulo de la solicitud
	 */
	private boolean puedoSuspender;
	/**
	 * Valrible que almacena la ultima propiedad por la que se ordeno.
	 */
	private String ultimaPropiedad;
	/**
	 * Columna por la que se desea ordenar
	 */
	private String columna;
	
	/**
	 * Variable que me indica si estoy en el resumen de atencion
	 */
	private boolean resumenAtencion;
	
	/**
	 * Validar el tipo de regimen para el caso en
	 * el cual no se requiere una justificacion de
	 * medicamentos NO POS
	 */
	private char codigoTipoRegimen;
	
	/**
	 * variable que sirve de indicador en el caso de que el tratante esté realizando
	 * la consulta de medicamentos que no se encuentren justificados.
	 */
	private char esPosibleJustificar;
	
	private HashMap justificacion=new HashMap();
	
	/**
	 * Entero que me indica si las solicitudes se listan por paciente o por área
	 */
	private int tipoListadoSolicitudes;
	
	/*
	 * Manejo de constantes para los listados
	 */
	
	/**
	 * Constante para dejar indefinido  el tipo de listado
	 */
	public static int listadoNoDefinido=0;
	
	/**
	 * Constante para definir listado de solicitudes por area del usuario
	 */
	public static int listadoPorArea=1;
	
	/**
	 * Constante para definir listado de solicitudes por paciente
	 */
	public static int listadoPorPaciente=2;
	
    /**
     * listado de almacenes del mapa
     */
    private HashMap listadoAlmacenesMap= new HashMap();
    
    /**
	 * Colección con los medicamentos solicitados a un paciente para mostrarlos
	 * en una ventana cuando el usuario se ubica sobre el detalle
	 */
	private Collection medicamentosSolicitadosPaciente=null;
    
	
	/**
	 * boolean que indica si se debe postular un servicio
	 */
	private boolean postularArticulo=false;
	
	/**
	 * codigo del servicio a postular
	 */
	private int codigoArticuloPostular;
	
	/**
	 * indicativo que indica si la solicitud es de pyp.
	 */
	private boolean solPYP=false;
	
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
	private int areaFiltro;
	
	/**
	 * 
	 */
	private int pisoFiltro;
	
	/**
	 * 
	 */
	private int habitacionFiltro;
	
	
	/**
	 * 
	 */
	private int camaFiltro;
	
	/**
	 * 
	 */
	private HashMap <String, Object> areasTagMap; 
	
	/**
	 * 
	 */
	private HashMap<String, Object> pisosTagMap;
	
	/**
	 * 
	 */
	private HashMap<String, Object> habitacionesTagMap;
	
	/**
	 * 
	 */
	private HashMap<Object, Object> camasTagMap;
	
	
	/**
	 * Variable que indica si se confirma la generacion de una orden de ambulatorios en caso de ser necesario.
	 */
	private boolean generarOrdenArticulosConfirmada;
	
	/**
	 * 
	 */
	private ResultadoBoolean mostrarValidacionArticulos;
	
	/**
	 * 
	 */
	private HashMap<String, Object> articulosConfirmacion=new HashMap<String,Object>();

	
	/*  desarrollo justificacion no pos */
	/**
	 * Mapa justificacion mapa donde se almacenan los datos para insetar de la justificacion de articulos nopos
	 */
	private HashMap justificacionMap=new HashMap();
	
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
	 * Strign hiddens resultadoi de la generacin de hiddens
	 */
	private String hiddens="";
	
	/**
	 * String de articulos insertados donde solo se va a consultar la justificacion
	 */
	private String artConsultaNP="";
	
	
	//-------*------
	
	
	/**
	 * Variable para almacenar los Convenios que sean plan especial por ingreso del Paciente
	 */
	private int numConveniosPlanEspecial=0;
	//------------------------------------
	
	/**
	 * Almacena la información de las solicitudes Historicas
	 * */
	private HashMap listadoHistoricoSolicitudMap;
	//------------------------------------
	
	
	/**
	 * Campo para saber si son requeridos los comentarios
	 */
	private boolean requeridoComentarios;

	/**
	 * Variable para el manejo de check correspondiente a control especial
	 */
	private String checkCE="";
	
	/**
	 * atributo que almacena true si se debe guardar la justificacion no pos 
	 * dependiendo del tiempo tratamiento de un articulo
	 */
	private boolean insertarJustNoPos;
	
	/**
	 * Atributo que almacena la lista de datos retorandos
	 * por el proceso de autorizaci&oacute;n de solicitud 
	 * de medicamentos
	 */
	private ArrayList<DTOProcesoAutorizacion> listaProcesoAutorizacion;
	
	
	/**
	 * Atributo que almacena el tipoCIE del diagnostico
	 * de la solicitud de medicamentos
	 */
	private String tipoCieDiagnostico;
	
	/**
	 * Atributo que almacena el tipoCIE (Int) del diagnostico
	 * de la solicitud de medicamentos
	 */
	private int tipoCieDiagnosticoInt;
	
	/**
	 * Atributo que almacena el acronimo del 
	 * diagnostico de la solicitud de medicamentos
	 */
	private String acronimoDiagnostico;
	
	/**
	 * Atributo que indica si la orden
	 * es ambulatoria
	 */
	private boolean esOrdenAmbulatoria;
	
	/**
	 * Este atributo se usa para determinar cuando se generó una 
	 * autorización de solicitudes y mostrar el respectivo botón
	 */
	private boolean mostrarImprimirAutorizacion;
	
	/**
	 * lista que contiene los nombres de los reportes de las autorzaciones 
	 * 
	 */
	private ArrayList<String> listaNombresReportes;
	
	/**Determina si los centros de costo 
	 * de la autorizacion con el seleccionado no corresponde*/
	private boolean centroCostoNoCorresponde;
	
	/***/
	private ArrayList<String> listaAdvertencias; 
	
	/**
	 * Atributo que indica si se debe guardar registro de alerta en enfermeria
	 */
	private String generaAlertaEnfermeria; 
	
	/**
	 * Atributo que almacena la info de cobertura de cada articulo
	 */
	private ArrayList<InfoResponsableCobertura> infoCoberturaArticulo;
	
	/**
	 * Numero maximo de resultados por pagina, se inicializa en 10 
	 * si por alguna razon no se pudo inicializar desde el action
	 */
	private int maxPageItems=10;
	
	/**
	 * Justificaciones Modificacion medicamento justificacion asociada 
	 * 
	 */
	private String justificacionModifica;
	
	/**
	 * @return
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}

	/**
	 * @param maxPageItems
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}
	
	/**
	 * @return Retorna estado.
	 */
	public String getEstado()
	{
		return estado;
	}
	
	/**
	 * @param estado Asigna estado.
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}
	/**
	 * @return Retorna centroCostoSolicitado.
	 */
	public int getCentroCostoSolicitado()
	{
		return centroCostoSolicitado;
	}
	/**
	 * @param centroCostoSolicitado Asigna centroCostoSolicitado.
	 */
	public void setCentroCostoSolicitado(int centroCostoSolicitado)
	{
		this.centroCostoSolicitado = centroCostoSolicitado;
	}
	/**
	 * @return Retorna fecha.
	 */
	public String getFecha()
	{
		return fecha;
	}
	/**
	 * @param fecha Asigna fecha.
	 */
	public void setFecha(String fecha)
	{
		this.fecha = fecha;
	}
	/**
	 * @return Retorna hora.
	 */
	public String getHora()
	{
		return hora;
	}
	/**
	 * @param hora Asigna hora.
	 */
	public void setHora(String hora)
	{
		this.hora = hora;
	}
	/**
	 * Verifica si un ahy una solicitud con articulos inactivos
	 */
	public boolean esreform=false;
	
	public boolean isEsreform() {
		return esreform;
	}

	public void setEsreform(boolean esreform) {
		this.esreform = esreform;
	}
	
	/**
	 * @return Retorna numeroAutorizacion.
	 */
	/*
	public String getNumeroAutorizacion()
	{
		return numeroAutorizacion;
	}
	*/		
	/**
	 * @param numeroAutorizacion Asigna numeroAutorizacion.
	 */
	/*
	public void setNumeroAutorizacion(String numeroAutorizacion)
	{
		this.numeroAutorizacion = numeroAutorizacion;
	}
	*/
	/**
	 * @return Retorna observacionesGenerales.
	 */
	public String getObservacionesGenerales()
	{
		return observacionesGenerales;
	}
	/**
	 * @param observacionesGenerales Asigna observacionesGenerales.
	 */
	public void setObservacionesGenerales(String observacionesGenerales)
	{
		this.observacionesGenerales = observacionesGenerales;
	}
	
	
	/**
	 * Validación del form
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors=new ActionErrors();
		if(estado.equals("guardar") || estado.equals("guardarModificacion"))
		{
			Connection con = null; 
			try {
				con = UtilidadBD.abrirConexion();
				//UtilidadValidacion.validarNumeroAutorizacion(errors, numeroAutorizacion);

				boolean valida=true;
				if(!UtilidadFecha.validarFecha(fecha))
				{
					errors.add("fecha", new ActionMessage("errors.formatoFechaInvalido",fecha));
					valida=false;
				}
				if(!UtilidadFecha.validacionHora(hora).puedoSeguir)
				{
					errors.add("hora", new ActionMessage("errors.formatoHoraInvalido",hora));
					valida=false;
				}
				if(valida)
				{
					PersonaBasica paciente=(PersonaBasica)request.getSession().getAttribute("pacienteActivo");
					codigoTipoRegimen=paciente.getCodigoTipoRegimen();
					int viaIngreso=paciente.getCodigoUltimaViaIngreso();
					int numeroSolicitud = 0;
						if(UtilidadValidacion.perteneceSolicitudACuentaComparacionXTiempo(con, this.numeroSolicitud, paciente.getCodigoCuenta()) || this.numeroSolicitud==0)
							numeroSolicitud = UtilidadValidacion.obtenerNumeroSolicitudPrimeraValoracion(con, paciente.getCodigoCuenta());
						else
							numeroSolicitud = UtilidadValidacion.obtenerNumeroSolicitudPrimeraValoracion(con, paciente.getCodigoCuentaAsocio());

						DtoValoracion valoracion = Valoraciones.cargarBase(con, numeroSolicitud+"");
						if(viaIngreso==ConstantesBD.codigoViaIngresoAmbulatorios)
						{
							if((UtilidadFecha.conversionFormatoFechaABD(paciente.getFechaIngreso())+paciente.getHoraIngreso()).compareTo(UtilidadFecha.conversionFormatoFechaABD(fecha)+hora)>0)
							{
								if(UtilidadFecha.conversionFormatoFechaABD(paciente.getFechaIngreso()).compareTo(UtilidadFecha.conversionFormatoFechaABD(fecha))>0)
								{
									errors.add("fechaHora", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "de la solicitud", "de Admision"));
								}
								else if(paciente.getHoraIngreso().compareTo(hora)>0)
								{
									errors.add("fechaHora", new ActionMessage("errors.horaAnteriorAOtraDeReferencia", "de la solicitud", "de Admision"));
								}
							}
						}
						else
						{
							if(viaIngreso==ConstantesBD.codigoViaIngresoConsultaExterna)
							{
								try
								{
									if((UtilidadFecha.conversionFormatoFechaABD(valoracion.getFechaGrabacion())+valoracion.getHoraGrabacion()).compareTo(UtilidadFecha.conversionFormatoFechaABD(fecha)+hora)>0)
									{
										if(UtilidadFecha.conversionFormatoFechaABD(valoracion.getFechaGrabacion()).compareTo(UtilidadFecha.conversionFormatoFechaABD(fecha))>0)
										{
											errors.add("fechaHora", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "de la solicitud", "de la Cita"));
										}
										else if(valoracion.getHoraGrabacion().compareTo(hora)>0)
										{
											errors.add("fechaHora", new ActionMessage("errors.horaAnteriorAOtraDeReferencia", "de la solicitud", "de la Cita"));
										}
									}
								}
								catch(Exception e)
								{
									/*en caso tal que no exista la valoración entonces se valuda contra la fecha de apertura de la cuenta*/
									String[] fechaHoraAperturaFormatoBD= (PersonaBasica.getFechaHoraApertura(con, paciente.getCodigoCuenta()).trim()).split("   ");
									if( ((fechaHoraAperturaFormatoBD[0]+UtilidadFecha.convertirHoraACincoCaracteres(fechaHoraAperturaFormatoBD[1])).trim()).compareTo(UtilidadFecha.conversionFormatoFechaABD(fecha)+hora) >0)
									{
										if(fechaHoraAperturaFormatoBD[0].trim().compareTo(UtilidadFecha.conversionFormatoFechaABD(fecha))>0)
										{
											errors.add("fechaHora", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "de la solicitud", "de la Apertura de la cuenta"));
										}
										else if(UtilidadFecha.convertirHoraACincoCaracteres(fechaHoraAperturaFormatoBD[1]).trim().compareTo(hora)>0)
										{
											errors.add("fechaHora", new ActionMessage("errors.horaAnteriorAOtraDeReferencia", "de la solicitud", "de la Apertura de la cuenta"));
										}
									}
								}
							}
							else if(valoracion.getHoraValoracion().length()>5 && (UtilidadFecha.conversionFormatoFechaABD(valoracion.getFechaValoracion())+valoracion.getHoraValoracion().substring(0,5)).compareTo(UtilidadFecha.conversionFormatoFechaABD(fecha)+hora)>0)
							{
								if(UtilidadFecha.conversionFormatoFechaABD(valoracion.getFechaValoracion()).compareTo(UtilidadFecha.conversionFormatoFechaABD(fecha))>0)
								{
									errors.add("fechaHora", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "de la solicitud", "de la Valoración Inicial"));
								}
								else if(valoracion.getHoraValoracion().substring(0,5).compareTo(hora)>0)
								{
									errors.add("fechaHora", new ActionMessage("errors.horaAnteriorAOtraDeReferencia", "de la solicitud", "de la Valoración Inicial"));
								}
							}
						}
					
					//en caso de que la conexion este abierta
					if((UtilidadFecha.conversionFormatoFechaABD(fecha)+hora).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+UtilidadFecha.getHoraActual())>0)
					{
						if((UtilidadFecha.conversionFormatoFechaABD(fecha)).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
						{
							errors.add("fecha", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de la solicitud", "actual"));
						}
						else if(hora.compareTo(UtilidadFecha.getHoraActual())>0)
						{
							errors.add("Hora", new ActionMessage("errors.horaPosteriorAOtraDeReferencia", "de la solicitud", "actual"));
						}
						else
						{
							//falta verificar que si funcione
							if(UtilidadInventarios.existeCierreInventarioParaFecha(this.getFecha(), ((UsuarioBasico)request.getSession().getAttribute("usuarioBasico")).getCodigoInstitucionInt()))
								errors.add("Fecha", new ActionMessage("error.inventarios.existeCierreInventarios", this.getFecha()));
						}
					}
				}
				if(centroCostoSolicitado==0)
				{
					errors.add("centroCosto", new ActionMessage("error.noFarmacia"));
				}
				// cuando hay medicamentos inactivos y se quiere reformular la orden
				if (esreform && numeroElementos!=0)
				{
					int nuevoNumElem=0;
					String codigos=getMedicamento("codigos")+"";
					String[] cod=codigos.split("-");
					for(int i=0; i<cod.length; i++)
					{
						if(getMedicamento("estado_"+cod[i])!=null)
						{
							if (getMedicamento("estado_"+cod[i]).equals("1"))
							{
								nuevoNumElem=1;
							}
						
						}
						else {
							nuevoNumElem=1 ;
						}
					
					}
					
					if (nuevoNumElem==0)
					{
						errors.add("centroCosto", new ActionMessage("error.solicitudMedicamentos.noElementosSeleccionados"));
					}
				}
				
				if(numeroElementos==0)
				{
					errors.add("centroCosto", new ActionMessage("error.solicitudMedicamentos.noElementosSeleccionados"));
				}
				else
				{
					String codigos=getMedicamento("codigos")+"";
					String[] cod=codigos.split("-");
					HttpSession session=request.getSession();	
					PersonaBasica paciente=(PersonaBasica)request.getSession().getAttribute("pacienteActivo");
					UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
					for(int i=0; i<cod.length; i++)
					{
						if(UtilidadValidacion.esMedicamento(Integer.parseInt(cod[i])))
						{
							// Si es orden ambulatoria no se requiere la justificacion ya que en este momento ya se devio de haber diligenciado
							if(!indicativoOrdenAmbulatoria){
								if (UtilidadesFacturacion.requiereJustificacioArt(con, paciente.getCodigoConvenio() , Utilidades.convertirAEntero(Integer.parseInt(cod[i])+"")) )
								{
									if(getMedicamento("unidosis_"+cod[i])!=null&&getMedicamento("tipoFrecuencia_"+cod[i])!=null)
									{
										//this.setMedicamento("insertarJustNP_"+cod[i],UtilidadInventarios.validarTiempoTratamiento(con, Utilidades.convertirAEntero(cod[i]+""),getMedicamento("unidosis_"+cod[i])+"", getMedicamento("tipoFrecuencia_"+cod[i])+"", paciente));
										if(!UtilidadTexto.getBoolean(justificacionMap.get(cod[i]+"_sevaasociar")))
										{
      											if (UtilidadesHistoriaClinica.validarEspecialidadProfesionalSalud(con, usuario, true)
													&&	 getMedicamento("es_pos_"+cod[i]).toString().equals("false")
													&& !UtilidadTexto.getBoolean(justificacionMap.get(cod[i]+"_yajustifico")) 
													&& (!justificacionMap.containsKey(cod[i]+"_pendiente")
													|| UtilidadTexto.getBoolean(justificacionMap.get(cod[i]+"_pendiente")))
													&& artConsultaNP.indexOf(" "+cod[i]+" ")<0)
											{
												errors.add("Justificacion", new ActionMessage("errors.required", "Justificacion del articulo - "+getMedicamento("cod_"+cod[i]).toString().split(ConstantesBD.separadorSplit)[1]));
											}

											if (!UtilidadesHistoriaClinica.validarEspecialidadProfesionalSalud(con, usuario, true) 
													&&	 getMedicamento("es_pos_"+cod[i]).toString().equals("false")
													&& artConsultaNP.indexOf(" "+cod[i]+" ")<0)
											{
												errors.add("Justificacion", new ActionMessage("errors.warnings","No puede solicitar el Articulo No POS "+getMedicamento("cod_"+cod[i]).toString().split(ConstantesBD.separadorSplit)[1]+" Ya que no cumple con la validacion médico especialista"));								
											}
										}
									}
								}
							}
							if(getMedicamento("dosis_"+cod[i])!=null) {
								if((getMedicamento("dosis_"+cod[i])+"").trim().equals("")) {
									errors.add("dosis_"+i, new ActionMessage("errors.required", "Dosis (Artículo "+cod[i]+")"));
								}
								else {
									try	{
										if(Double.parseDouble(getMedicamento("dosis_"+cod[i])+"")<=0) {
											errors.add("", new ActionMessage("errors.floatMayorQue", "La dosis", "0"));
										}
									}
									catch (Exception e)	{
										errors.add("", new ActionMessage("errors.floatMayorQue", "La dosis", "0"));
									}
								}
							}
							else {
								errors.add("dosis_"+i, new ActionMessage("errors.required", "Dosis (Artículo "+cod[i]+")"));
							}

							logger.info("UNIDOSIS -> "+getMedicamento("unidosis_"+cod[i]));

							if(getMedicamento("unidosis_"+cod[i])!=null)
							{
								if((getMedicamento("unidosis_"+cod[i])+"").trim().equals(""))
								{
									errors.add("unidosis_"+i, new ActionMessage("errors.required", "Unidosis (Artículo "+cod[i]+")"));
								}
							}
							else
							{
								errors.add("unidosis_"+i, new ActionMessage("errors.required", "Unidosis (Artículo "+cod[i]+")"));
							}



							if(getMedicamento("frecuencia_"+cod[i])!=null)
							{
								if((getMedicamento("frecuencia_"+cod[i])+"").trim().equals(""))
								{
									errors.add("frecuencia_"+i, new ActionMessage("errors.required", "Frecuencia (Artículo "+cod[i]+")"));
								}
								else
								{
									try
									{
										if(Integer.parseInt(getMedicamento("frecuencia_"+cod[i])+"")<1)
										{
											errors.add("", new ActionMessage("errors.integerMayorQue", "La Frecuencia", "0"));
										}
									}
									catch (NumberFormatException e)
									{
										errors.add("frecuencia_"+i, new ActionMessage("errors.integer", "Frecuencia (Artículo "+cod[i]+")"));
									}
								}
							}
							else
							{
								errors.add("frecuencia_"+i, new ActionMessage("errors.required", "Frecuencia (Artículo "+cod[i]+")"));
							}

							if(getMedicamento("tipoFrecuencia_"+cod[i])!=null)
							{
								if((getMedicamento("tipoFrecuencia_"+cod[i])+"").trim().equals(""))
								{
									errors.add("tipoFrecuencia_"+i, new ActionMessage("errors.required", "Tipo de Frecuencia (Artículo "+cod[i]+")"));
								}
							}
							else
							{
								errors.add("tipoFrecuencia_"+i, new ActionMessage("errors.required", "Tipo de Frecuencia (Artículo "+cod[i]+")"));
							}

							if(getMedicamento("via_"+cod[i])!=null)
							{
								if((getMedicamento("via_"+cod[i])+"").trim().equals(""))
								{
									errors.add("via_"+i, new ActionMessage("errors.required", "Vía de Administración (Artículo "+cod[i]+")"));
								}
							}
							else
							{
								errors.add("via_"+i, new ActionMessage("errors.required", "Vía de Administración (Artículo "+cod[i]+")"));
							}
							
							if(getMedicamento("diastratamiento_"+cod[i])!=null)
							{
								if((getMedicamento("diastratamiento_"+cod[i])+"").trim().equals(""))
								{
									errors.add("diastratamiento_"+i, new ActionMessage("errors.required", "Días de Tratamiento (Artículo "+cod[i]+")"));
								}
							}
							else
							{
								errors.add("diastratamiento_"+i, new ActionMessage("errors.required", "Días de Tratamiento (Artículo "+cod[i]+")"));
							}
						}
						else
							// Si es orden ambulatoria no se requiere la justificación ya que en este momento ya se devio de haber diligenciado
							if(!indicativoOrdenAmbulatoria){
								if (UtilidadesFacturacion.requiereJustificacioArt(con, paciente.getCodigoConvenio() , Utilidades.convertirAEntero(Integer.parseInt(cod[i])+"")) )
								{	
									if (UtilidadesHistoriaClinica.validarEspecialidadProfesionalSalud(con, usuario, true)
										&& getMedicamento("es_pos_" + cod[i]).toString().equals("false")
										&& ((getMedicamento("justificado_"+ cod[i]) != null
										&& (UtilidadTexto.isEmpty(getMedicamento("justificado_"+ cod[i]).toString())
											|| getMedicamento("justificado_" + cod[i]).toString().equals(ConstantesBD.acronimoNo)) 
											|| (getMedicamento("justificado_"+ cod[i]) == null)))
								        // && artConsultaNP.indexOf(" "+cod[i]+" ")<0
										)
									{
										errors.add("Justificacion", new ActionMessage("errors.required", "Justificacion del Insumo - "+getMedicamento("cod_"+cod[i]).toString().split(ConstantesBD.separadorSplit)[1]));
									}

									if (!UtilidadesHistoriaClinica.validarEspecialidadProfesionalSalud(con, usuario, true) 
											&&	 getMedicamento("es_pos_"+cod[i]).toString().equals("false")
											//&& artConsultaNP.indexOf(" "+cod[i]+" ")<0
									)
									{
										errors.add("Justificacion", new ActionMessage("errors.warnings","No puede solicitar el Articulo No POS "+getMedicamento("cod_"+cod[i]).toString().split(ConstantesBD.separadorSplit)[1]+" Ya que no cumple con la validacion médico especialista"));								
									}
								}
							}
						//LA CANTIDAD ES REQUERIDA SI EL 
						//EL CALCULO DE LA CANTIDAD ES AUTOMATICO
						if(errors.isEmpty())
						{	
							if(UtilidadValidacion.esMedicamento(Integer.parseInt(cod[i])) 
									&& UtilidadValidacion.articuloTieneCantidadUnidosisActivaDadoUnidosis(Integer.parseInt(getMedicamento("unidosis_"+cod[i])+"")))
								//&& ValoresPorDefecto.getIngresoCantidadSolMedicamentos(((UsuarioBasico)request.getSession().getAttribute("usuarioBasico")).getCodigoInstitucionInt()).equals("true"))
							{	
								try
								{
									if(Integer.parseInt(getMedicamento("cantidad_"+cod[i])+"")<1)
										errors.add("", new ActionMessage("errors.integerMayorQue", "Total Unidades (Medicamento "+cod[i]+")", "0"));
								}
								catch (Exception e)
								{
									errors.add("cantidad_"+i, new ActionMessage("errors.integer", "Total Unidades (Medicamento "+cod[i]+")"));
								}
							}

							//PARA INSUMOS SEGUN ANGELA EL 2007-06-01 SIEMPRE VA HA SER REQUERIDA LA CANTIDAD
							else
							{
								try
								{
									if(Integer.parseInt(getMedicamento("cantidad_"+cod[i])+"")<0)
										errors.add("", new ActionMessage("errors.integerMayorIgualQue", "Total Unidades (Medicamento "+cod[i]+")", "0"));
								}
								catch (Exception e)
								{
									errors.add("cantidad_"+i, new ActionMessage("errors.integer", "Total Unidades (Insumo "+cod[i]+")"));
								}
							}
						}
						if(centroCostoSolicitado!=ConstantesBD.codigoCentroCostoExternos && (codigoTipoRegimen==ConstantesBD.codigoTipoRegimenContributivo || codigoTipoRegimen==ConstantesBD.codigoTipoRegimenVinculado || codigoTipoRegimen==ConstantesBD.codigoTipoRegimenSubsidiado))
						{
							if((getMedicamento("es_pos_"+cod[i])+"").equals("false"))
							{
								//	validarJustificacion(errors, ((UsuarioBasico)request.getSession().getAttribute("usuarioBasico")).getCodigoInstitucionInt(), cod[i]);							
							}
						}
					}

					logger.info("getMedicamentosDiferenteDosificacion(MDD_codigos)->"+getMedicamentosDiferenteDosificacion("MDD_codigos"));

					/////////////////////////////////////////////////////////validaciones MDD Medicamentos diferente dosificacion//////////////////////////////////////////////////////

					String MDD_codigos=getMedicamentosDiferenteDosificacion("MDD_codigos")+"";
					String[] MDD_cod=MDD_codigos.split("-");

					logger.info("MDD_cod.length->"+MDD_cod.length);

					for(int i=0; (i<(MDD_cod.length) && !UtilidadTexto.isEmpty(getMedicamentosDiferenteDosificacion("MDD_codigos")+"")); i++)
					{
						if(UtilidadValidacion.esMedicamento(Integer.parseInt(MDD_cod[i])))
						{
							if(getMedicamentosDiferenteDosificacion("MDD_dosis_"+MDD_cod[i])!=null)
							{
								if((getMedicamentosDiferenteDosificacion("MDD_dosis_"+MDD_cod[i])+"").trim().equals(""))
								{
									errors.add("MDD_dosis_"+i, new ActionMessage("errors.required", "Dosis (Artículo Diferente Dosificación "+MDD_cod[i]+")"));
								}
								else
								{
									try
									{
										if(Double.parseDouble(getMedicamentosDiferenteDosificacion("MDD_dosis_"+MDD_cod[i])+"")<=0)
										{
											errors.add("", new ActionMessage("errors.floatMayorQue", "La dosis (Artículo Diferente Dosificación)", "0"));
										}
									}
									catch (Exception e) 
									{
										errors.add("", new ActionMessage("errors.floatMayorQue", "La dosis (Artículo Diferente Dosificación)", "0"));
									}
								}
							}
							else
							{
								errors.add("MDD_dosis_"+i, new ActionMessage("errors.required", "Dosis (Artículo Diferente Dosificación "+MDD_cod[i]+")"));
							}

							if(getMedicamentosDiferenteDosificacion("MDD_unidosis_"+MDD_cod[i])!=null)
							{
								if((getMedicamentosDiferenteDosificacion("MDD_unidosis_"+MDD_cod[i])+"").trim().equals(""))
								{
									errors.add("MDD_unidosis_"+i, new ActionMessage("errors.required", "Unidosis (Artículo Diferente Dosificación "+MDD_cod[i]+")"));
								}
							}
							else
							{
								errors.add("MDD_unidosis_"+i, new ActionMessage("errors.required", "Unidosis (Artículo Diferente Dosificación "+MDD_cod[i]+")"));
							}

							if(getMedicamentosDiferenteDosificacion("MDD_frecuencia_"+MDD_cod[i])!=null)
							{
								if((getMedicamentosDiferenteDosificacion("MDD_frecuencia_"+MDD_cod[i])+"").trim().equals(""))
								{
									errors.add("MDD_frecuencia_"+i, new ActionMessage("errors.required", "Frecuencia (Artículo Diferente Dosificación "+MDD_cod[i]+")"));
								}
								else
								{
									try
									{
										if(Integer.parseInt(getMedicamentosDiferenteDosificacion("MDD_frecuencia_"+MDD_cod[i])+"")<1)
										{
											errors.add("", new ActionMessage("errors.integerMayorQue", "La Frecuencia", "0"));
										}
									}
									catch (NumberFormatException e)
									{
										errors.add("MDD_frecuencia_"+i, new ActionMessage("errors.integer", "Frecuencia (Artículo Diferente Dosificación "+MDD_cod[i]+")"));
									}
								}
							}
							else
							{
								errors.add("MDD_frecuencia_"+i, new ActionMessage("errors.required", "Frecuencia (Artículo Diferente Dosificación "+MDD_cod[i]+")"));
							}

							if(getMedicamentosDiferenteDosificacion("MDD_tipoFrecuencia_"+MDD_cod[i])!=null)
							{
								if((getMedicamentosDiferenteDosificacion("MDD_tipoFrecuencia_"+MDD_cod[i])+"").trim().equals(""))
								{
									errors.add("MDD_tipoFrecuencia_"+i, new ActionMessage("errors.required", "Tipo de Frecuencia (Artículo Diferente Dosificación "+MDD_cod[i]+")"));
								}
							}
							else
							{
								errors.add("MDD_tipoFrecuencia_"+i, new ActionMessage("errors.required", "Tipo de Frecuencia (Artículo Diferente Dosificación "+MDD_cod[i]+")"));
							}

							if(getMedicamentosDiferenteDosificacion("MDD_via_"+MDD_cod[i])!=null)
							{
								if((getMedicamentosDiferenteDosificacion("MDD_via_"+MDD_cod[i])+"").trim().equals(""))
								{
									errors.add("MDD_via_"+i, new ActionMessage("errors.required", "Vía de Administración (Artículo Diferente Dosificación "+MDD_cod[i]+")"));
								}
							}
							else
							{
								errors.add("MDD_via_"+i, new ActionMessage("errors.required", "Vía de Administración (Artículo Diferente Dosificación "+MDD_cod[i]+")"));
							}
						}

						//LA CANTIDAD ES REQUERIDA SI EL 
						//EL CALCULO DE LA CANTIDAD ES AUTOMATICO
						if(errors.isEmpty())
						{	
							if(UtilidadValidacion.esMedicamento(Integer.parseInt(MDD_cod[i])) 
									&& UtilidadValidacion.articuloTieneCantidadUnidosisActivaDadoUnidosis(Integer.parseInt(getMedicamentosDiferenteDosificacion("MDD_unidosis_"+MDD_cod[i])+"")))
								//&& ValoresPorDefecto.getIngresoCantidadSolMedicamentos(((UsuarioBasico)request.getSession().getAttribute("usuarioBasico")).getCodigoInstitucionInt()).equals("true"))
							{	
								try
								{
									if(Integer.parseInt(getMedicamentosDiferenteDosificacion("MDD_cantidad_"+MDD_cod[i])+"")<1)
										errors.add("", new ActionMessage("errors.integerMayorQue", "Total Unidades (Medicamento Diferente Dosificación "+MDD_cod[i]+")", "0"));
								}
								catch (Exception e)
								{
									errors.add("MDD_cantidad_"+i, new ActionMessage("errors.integer", "Total Unidades (Medicamento Diferente Dosificación "+MDD_cod[i]+")"));
								}
							}
						}
					}

					/////////////////////////////////////////////////////////fin validaciones MDD Medicamentos diferente dosificacion//////////////////////////////////////////////////
				}

				if(estado.equals("guardar")&&this.observacionesGenerales.trim().equals("")&&this.requeridoComentarios)
					errors.add("", new ActionMessage("errors.required","El campo observaciones generales"));
			} catch (Exception e) {
				Log4JManager.error("Error validando Solicitud Medicamentos: " + e);
			} finally {
				UtilidadBD.closeConnection(con);
			}
		}
		if(estado.equals("anularSolicitud"))
			if(motivoAnulacion.equals(""))
			{
				estado="anular";
				errors.add("Motivo", new ActionMessage("errors.required","Motivo de la anulacion"));
			}
		//Mt 4568 Validar justificacion No pos articulo general
        if(estado.equals("modificarEliminar")){
			 
        	try
			{
        	Connection con = null; 
			con = UtilidadBD.abrirConexion();
        	  	 
        	    if(UtilidadesJustificacionNoPos.existeJustificacion(con, 
        	    		            (((UsuarioBasico)request.getSession().getAttribute("usuarioBasico")).getCodigoInstitucionInt()),
        	    		               String.valueOf(codigoEliminado), Integer.toString(numeroSolicitud))){
        	    
        	    	errors.add("", new ActionMessage("errors.warnings", "El medicamento a elimar tiene asociada una Justificacion No Pos."));
        	    }  
        	   
			}
			catch (Exception e) 
			{
				e.printStackTrace();
			}
        }
        
		
		return errors;
	}
	/**
	 * @param errors
	 * @param codigoInstitucion
	 * @param codigo
	 */
	/*private void validarJustificacion(ActionErrors errors, int codigoInstitucion, String codigo)
	{
		int[] codigosJustificacion=Utilidades.buscarCodigosJustificaciones(codigoInstitucion, true, true);
		//se debe validar solo cuando no viene de orden ambulatoria 	
		if(!this.indicativoOrdenAmbulatoria)
		{
			for(int i=0; i<codigosJustificacion.length; i++)
			{
				String justifi=(String)getMedicamento("just"+codigosJustificacion[i]+"_"+codigo);
				if(justifi!=null)
				{
					if(justifi.equals(""))
					{
						errors.add("jus"+codigosJustificacion[i]+"_"+codigo, new ActionMessage("errors.required", "Justificación (Artículo "+codigo+")"));
					}
				}
				else
				{
					errors.add("jus"+codigosJustificacion[i]+"_"+codigo, new ActionMessage("errors.required", "Justificación (Artículo "+codigo+")"));
				}
			}
		}
		else {
			logger.info("\n\n\n\n\n\n\n ES DE ORDEN AMBULATORIOA ---*-- ");
		}
	}*/

	/**
	 * @return Retorna urgente.
	 */
	public boolean getUrgente()
	{
		return urgente;
	}
	/**
	 * @param urgente Asigna urgente.
	 */
	public void setUrgente(boolean urgente)
	{
		this.urgente = urgente;
	}
	/**
	 * @return Retorna medicamentos.
	 */
	public HashMap getMedicamentos()
	{
		return medicamentos;
	}
	/**
	 * @param medicamentos Asigna medicamentos.
	 */
	public void setMedicamentos(HashMap medicamentos)
	{
		this.medicamentos = medicamentos;
	}
	/**
	 * @return Retorna objeto de acuerdo con la llave.
	 */
	public Object getMedicamento(String key)
	{
		//toca hacer esto, por que, como los textarea de justificacion se arman en javascrip, entonces, cuando es \n o \r el toma como si el
		//caracter especial fuera la n o la r, y toca poner \\.
		if(key.contains("just"))
		{
			String cadena=(medicamentos.get(key)+"").replace("\r", "\\r");
			return cadena.replace("\n", " \\n");
			
		}
		return medicamentos.get(key);
	}
	/*public HashMap getMedicamento()
	{
		return this.medicamentos;
	}*/
	/**
	 * Asigna una propiedad al hashmap
	 * @param key
	 * @param value
	 */
	public void setMedicamento(String key, Object value)
	{
		medicamentos.put(key, value);
	}
	
	/**
	 * setea los atributos de la orden ambulatora 
	 *
	 */
	public void resetAmbulatorias()
	{
		this.ordenAmbulatoria="";
		this.indicativoOrdenAmbulatoria=false;
		//this.solPYP=false;
		this.centroCostoNoCorresponde=false;	
	}
	
	/**
	 * 
	 *
	 */
	public void reset(int codigoInstitucion, int codigoCentroAtencion)
	{
		this.numeroSolicitud=0;
		this.numeroSolicitudMedDiferenteDosificacion=0;
		this.numeroOrdenMedicaMedDiferenteDosificacion=0;
		fecha=UtilidadFecha.getFechaActual();
		hora=UtilidadFecha.getHoraActual();
		
		centroCostoSolicitado=0;
        this.nombreCentroCostoSolicitado="";
		
        
        urgente=false;
        setTieneDespacho(false);
        //numeroAutorizacion="";
		observacionesGenerales="";
		observacionesGeneralesNuevas="";
		medicamentos=new HashMap();
		medicamentos.put("codigos", "0");
		
		this.medicamentosDiferenteDosificacion= new HashMap();
		this.medicamentosDiferenteDosificacion.put("MDD_codigos", "");
		this.medicamentosDiferenteDosificacion.put("numRegistros", 0);
		this.codArticuloSelDiferenteDosificacion=ConstantesBD.codigoNuncaValido;
		
		codigoEliminado=0;
		numeroElementos=0;
		articulos=new Vector();
		articulosDiferenteDosificacion= new Vector();
		coleccion=null;		
		motivoAnulacion="";
		mostrarMensajeAsocio=false;
		puedoModificar=false;
		columna="";
		ultimaPropiedad="";		
		resumenAtencion=false;
		esPosibleJustificar=' ';
		justificacion=new HashMap();
        this.listadoAlmacenesMap= new HashMap();
		//tipoListadoSolicitudes=listadoNoDefinido;
        this.postularArticulo = false;
        
        this.fechaInicialFiltro=UtilidadFecha.incrementarDiasAFecha(UtilidadFecha.getFechaActual(), -1, false);
        this.fechaFinalFiltro=UtilidadFecha.getFechaActual();
        this.areaFiltro=ConstantesBD.codigoNuncaValido;
		this.pisoFiltro=ConstantesBD.codigoNuncaValido;
		this.habitacionFiltro=ConstantesBD.codigoNuncaValido;;
		this.camaFiltro= ConstantesBD.codigoNuncaValido;
		
		inicializarTagsMap(codigoInstitucion, codigoCentroAtencion);
		/*5007*/
		/*Estas lineas estaban comentadas, al omitirlas dejaba asociar justificaciones de un paciente a otro diferente*/
		this.justificacionMap=new HashMap();
		this.justificacionMap.put("numRegistros", 0);

		
		this.generarOrdenArticulosConfirmada=false;
		this.mostrarValidacionArticulos=new ResultadoBoolean(false);
		this.articulosConfirmacion=new HashMap<String, Object>();
		
		numConveniosPlanEspecial=0;
		this.hiddens="";
		this.artConsultaNP="";
		this.listadoHistoricoSolicitudMap = new HashMap();
		
		this.requeridoComentarios = false;
		this.checkCE="";
		this.acronimoDiagnostico="";
		this.tipoCieDiagnostico="";
		this.esOrdenAmbulatoria=false;
		this.tipoCieDiagnosticoInt=ConstantesBD.codigoNuncaValido;
		this.listaAdvertencias=new ArrayList<String>();
		this.insertarJustNoPos= false;
		
		this.dtoDiagnostico	= new DtoDiagnostico();
		this.esreform=false;
		this.justificacionModifica="";
		//this.infoCoberturaArticulo	= new ArrayList<InfoResponsableCobertura>();
	}
    
	
	
	/**
	 * @return the requeridoComentarios
	 */
	public boolean isRequeridoComentarios() {
		return requeridoComentarios;
	}

	/**
	 * @param requeridoComentarios the requeridoComentarios to set
	 */
	public void setRequeridoComentarios(boolean requeridoComentarios) {
		this.requeridoComentarios = requeridoComentarios;
	}

	/**
	 * 
	 * @param codigoInstitucion
	 * @param codigoCentroAtencion
	 */
	public void inicializarTagsMap(int codigoInstitucion, int codigoCentroAtencion)
	{
		this.areasTagMap= CentrosCostoViaIngreso.consultarCentrosCostoViaIngreso(codigoInstitucion, codigoCentroAtencion);
		this.pisosTagMap= Pisos.pisosXCentroAtencionTipo(codigoCentroAtencion, codigoInstitucion);
		this.habitacionesTagMap= Habitaciones.habitacionesXCentroAtencionTipo(codigoCentroAtencion, codigoInstitucion);
		this.camasTagMap= Camas1.listadoCamas1(codigoInstitucion, codigoCentroAtencion);
	}
	
	/**
	 * inicializa la coleccion de insumos
	 *
	 */
	public void resetCollection ()
	{
	    coleccionInsumos=null;
	}
	
	/**
	 * @return Retorna codigoEliminado.
	 */
	public int getCodigoEliminado()
	{
		return codigoEliminado;
	}
	/**
	 * @param codigoEliminado Asigna codigoEliminado.
	 */
	public void setCodigoEliminado(int codigoEliminado)
	{
		this.codigoEliminado = codigoEliminado;
	}
	/**
	 * @return Retorna numeroElementos.
	 */
	public int getNumeroElementos()
	{
		return numeroElementos;
	}
	/**
	 * @param numeroElementos Asigna numeroElementos.
	 */
	public void setNumeroElementos(int numeroElementos)
	{
		this.numeroElementos = numeroElementos;
	}
	/**
	 * @return Retorna el numeroSolicitud.
	 */
	public int getNumeroSolicitud()
	{
		return numeroSolicitud;
	}
	/**
	 * @param numeroSolicitud Asigna el numeroSolicitud.
	 */
	public void setNumeroSolicitud(int numeroSolicitud)
	{
		this.numeroSolicitud = numeroSolicitud;
	}
	/**
	 * @return Retorna articulos.
	 */
	public Vector getArticulos()
	{
		return articulos;
	}
	/**
	 * @param articulos Asigna articulos.
	 */
	public void setArticulos(Vector articulos)
	{
		this.articulos = articulos;
	}
	
	/**
	 * @return the articulosDiferenteDosificacion
	 */
	public Vector getArticulosDiferenteDosificacion() {
		return articulosDiferenteDosificacion;
	}

	/**
	 * @param articulosDiferenteDosificacion the articulosDiferenteDosificacion to set
	 */
	public void setArticulosDiferenteDosificacion(
			Vector articulosDiferenteDosificacion) {
		this.articulosDiferenteDosificacion = articulosDiferenteDosificacion;
	}

	/**
	 * @return Retorna codigoArticuloAImprimir.
	 */
	public int getCodigoArticuloAImprimir()
	{
		return codigoArticuloAImprimir;
	}
	/**
	 * @param codigoArticuloAImprimir Asigna codigoArticuloAImprimir.
	 */
	public void setCodigoArticuloAImprimir(int codigoArticuloAImprimir)
	{
		this.codigoArticuloAImprimir = codigoArticuloAImprimir;
	}
	/**
	 * @return Retorna el colecion.
	 */
	public Collection getColeccion() {
		return coleccion;
	}
	/**
	 * @param colecion Asigna el colecion.
	 */
	public void setColeccion(Collection coleccion) {
		this.coleccion = coleccion;
	}
	/**
	 * @return Retorna el coleccionInsumos.
	 */
	public Collection getColeccionInsumos() {
		return coleccionInsumos;
	}
	/**
	 * @param coleccionInsumos Asigna el coleccionInsumos.
	 */
	public void setColeccionInsumos(Collection coleccionInsumos) {
		this.coleccionInsumos = coleccionInsumos;
	}

	/**
	 * @return Retorna observacionesGeneralesNuevas.
	 */
	public String getObservacionesGeneralesNuevas()
	{
		return observacionesGeneralesNuevas;
	}
	/**
	 * @param observacionesGeneralesNuevas Asigna observacionesGeneralesNuevas.
	 */
	public void setObservacionesGeneralesNuevas(
			String observacionesGeneralesNuevas)
	{
		this.observacionesGeneralesNuevas = observacionesGeneralesNuevas;
	}
	/**
	 * @return Retorna el motivoAnulacion.
	 */
	public String getMotivoAnulacion() {
		return motivoAnulacion;
	}
	/**
	 * @param motivoAnulacion Asigna el motivoAnulacion.
	 */
	public void setMotivoAnulacion(String motivoAnulacion) {
		this.motivoAnulacion = motivoAnulacion;
	}
	/**
	 * @return Retorna mostrarMensajeAsocio.
	 */
	public boolean getMostrarMensajeAsocio()
	{
		return mostrarMensajeAsocio;
	}
	/**
	 * @param mostrarMensajeAsocio Asigna mostrarMensajeAsocio.
	 */
	public void setMostrarMensajeAsocio(boolean mostrarMensajeAsocio)
	{
		this.mostrarMensajeAsocio = mostrarMensajeAsocio;
	}
	/**
	 * @return Retorna el puedoModificar.
	 */
	public boolean getPuedoModificar() {
		return puedoModificar;
	}
	/**
	 * @param puedoModificar Asigna el puedoModificar.
	 */
	public void setPuedoModificar(boolean puedoModificar) {
		this.puedoModificar = puedoModificar;
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
	 * @return Retorna el resumenAtencion.
	 */
	public boolean isResumenAtencion() {
		return resumenAtencion;
	}
	/**
	 * @param resumenAtencion Asigna el resumenAtencion.
	 */
	public void setResumenAtencion(boolean resumenAtencion) {
		this.resumenAtencion = resumenAtencion;
	}
	/**
	 * @return Retorna puedoSuspender.
	 */
	public boolean getPuedoSuspender()
	{
		return puedoSuspender;
	}
	/**
	 * @param puedoSuspender Asigna puedoSuspender.
	 */
	public void setPuedoSuspender(boolean puedoSuspender)
	{
		this.puedoSuspender = puedoSuspender;
	}
	/**
	 * @return Retorna codigoTipoRegimen.
	 */
	public char getCodigoTipoRegimen()
	{
		return codigoTipoRegimen;
	}
	/**
	 * @param codigoTipoRegimen Asigna codigoTipoRegimen.
	 */
	public void setCodigoTipoRegimen(char codigoTipoRegimen)
	{
		this.codigoTipoRegimen = codigoTipoRegimen;
	}
	/**
	 * @return Retorna numeroOrdenMedica.
	 */
	public int getNumeroOrdenMedica()
	{
		return numeroOrdenMedica;
	}
	/**
	 * @param numeroOrdenMedica Asigna numeroOrdenMedica.
	 */
	public void setNumeroOrdenMedica(int numeroOrdenMedica)
	{
		this.numeroOrdenMedica = numeroOrdenMedica;
	}    
	/**
	 * @return Retorna centroCostoSolicitante.
	 */
	public int getCentroCostoSolicitante()
	{
		return centroCostoSolicitante;
	}
	/**
	 * @param centroCostoSolicitante Asigna centroCostoSolicitante.
	 */
	public void setCentroCostoSolicitante(int centroCostoSolicitante)
	{
		this.centroCostoSolicitante = centroCostoSolicitante;
	}
	/**
	 * @return Returns the esPosibleJustificar.
	 */
	public char getEsPosibleJustificar() {
		return esPosibleJustificar;
	}
	/**
	 * @param esPosibleJustificar The esPosibleJustificar to set.
	 */
	public void setEsPosibleJustificar(char esPosibleJustificar) {
		this.esPosibleJustificar = esPosibleJustificar;
	}
	/**
	 * @return Returns the justificacion.
	 */
	public HashMap getJustificacion() {
		return justificacion;
	}
	/**
	 * @param justificacion The justificacion to set.
	 */
	public void setJustificacion(HashMap justificacion) {
		this.justificacion = justificacion;
	}
	
	/**
	 * Retorna un objeto del arreglo
	 * @param key
	 * @return
	 */
	public Object getJustificacion(String key)
	{
		return justificacion.get(key);
	}
	
	/**
	 * Asigna un objeto al arreglo
	 * @param key
	 * @param obj
	 */
	public void setJustificacion(String key,Object obj)
	{
		justificacion.put(key,obj);
	}
	
	/**
	 * @return Retorna tipoListadoSolicitudes.
	 */
	public int getTipoListadoSolicitudes()
	{
		return tipoListadoSolicitudes;
	}

	/**
	 * @param tipoListadoSolicitudes Asigna tipoListadoSolicitudes.
	 */
	public void setTipoListadoSolicitudes(int tipoListadoSolicitudes)
	{
		this.tipoListadoSolicitudes = tipoListadoSolicitudes;
	}
      /**
     * Set del mapa del listado de los almacenes autorizados 
     * @param key
     * @param value
     */
    public void setListadoAlmacenesMap(String key, Object value) 
    {
        listadoAlmacenesMap.put(key, value);
    }
    /**
     * Get del mapa de N servicios o N articulos
     * Retorna el valor de un campo dado su nombre
     */
    public Object getListadoAlmacenesMap(String key) 
    {
        return listadoAlmacenesMap.get(key);
    }
    
    /**
     * @return Returns the listadoAlmacenesMap.
     */
    public HashMap getListadoAlmacenesMap() {
        return listadoAlmacenesMap;
    }
    /**
     * @param listadoAlmacenesMap The listadoAlmacenesMap to set.
     */
    public void setListadoAlmacenesMap(HashMap listadoAlmacenesMap) {
        this.listadoAlmacenesMap = listadoAlmacenesMap;
    }
    /**
     * @return Returns the nombreCentroCostoSolicitado.
     */
    public String getNombreCentroCostoSolicitado() {
        return nombreCentroCostoSolicitado;
    }
    /**
     * @param nombreCentroCostoSolicitado The nombreCentroCostoSolicitado to set.
     */
    public void setNombreCentroCostoSolicitado(
            String nombreCentroCostoSolicitado) {
        this.nombreCentroCostoSolicitado = nombreCentroCostoSolicitado;
    }

	/**
	 * @return Retorna the medicamentosSolicitadosPaciente.
	 */
	public Collection getMedicamentosSolicitadosPaciente()
	{
		return medicamentosSolicitadosPaciente;
	}

	/**
	 * @param medicamentosSolicitadosPaciente The medicamentosSolicitadosPaciente to set.
	 */
	public void setMedicamentosSolicitadosPaciente(
			Collection medicamentosSolicitadosPaciente)
	{
		this.medicamentosSolicitadosPaciente = medicamentosSolicitadosPaciente;
	}

	public int getCodigoArticuloPostular() {
		return codigoArticuloPostular;
	}

	public void setCodigoArticuloPostular(int codigoArticuloPostular) {
		this.codigoArticuloPostular = codigoArticuloPostular;
	}

	public boolean isPostularArticulo() {
		return postularArticulo;
	}

	public void setPostularArticulo(boolean postularArticulo) {
		this.postularArticulo = postularArticulo;
	}

	public boolean isSolPYP() {
		return solPYP;
	}

	public void setSolPYP(boolean solPYP) {
		this.solPYP = solPYP;
	}

	public boolean isIndicativoOrdenAmbulatoria() {
		return indicativoOrdenAmbulatoria;
	}

	public void setIndicativoOrdenAmbulatoria(boolean indicativoOrdenAmbulatoria) {
		this.indicativoOrdenAmbulatoria = indicativoOrdenAmbulatoria;
	}

	public String getOrdenAmbulatoria() {
		return ordenAmbulatoria;
	}

	public void setOrdenAmbulatoria(String ordenAmbulatoria) {
		this.ordenAmbulatoria = ordenAmbulatoria;
	}
	
	/**
	 * @return the areaFiltro
	 */
	public int getAreaFiltro() {
		return areaFiltro;
	}

	/**
	 * @param areaFiltro the areaFiltro to set
	 */
	public void setAreaFiltro(int areaFiltro) {
		this.areaFiltro = areaFiltro;
	}

	/**
	 * @return the camaFiltro
	 */
	public int getCamaFiltro() {
		return camaFiltro;
	}

	/**
	 * @param camaFiltro the camaFiltro to set
	 */
	public void setCamaFiltro(int camaFiltro) {
		this.camaFiltro = camaFiltro;
	}

	/**
	 * @return the habitacionFiltro
	 */
	public int getHabitacionFiltro() {
		return habitacionFiltro;
	}

	/**
	 * @param habitacionFiltro the habitacionFiltro to set
	 */
	public void setHabitacionFiltro(int habitacionFiltro) {
		this.habitacionFiltro = habitacionFiltro;
	}

	/**
	 * @return the pisoFiltro
	 */
	public int getPisoFiltro() {
		return pisoFiltro;
	}

	/**
	 * @param pisoFiltro the pisoFiltro to set
	 */
	public void setPisoFiltro(int pisoFiltro) {
		this.pisoFiltro = pisoFiltro;
	}
	
	/**
	 * @return the areasTagMap
	 */
	public HashMap<String, Object> getAreasTagMap() {
		return areasTagMap;
	}

	/**
	 * @param areasTagMap the areasTagMap to set
	 */
	public void setAreasTagMap(HashMap<String, Object> areasTagMap) {
		this.areasTagMap = areasTagMap;
	}

	/**
	 * @return the camasTagMap
	 */
	public HashMap<Object, Object> getCamasTagMap() {
		return camasTagMap;
	}

	/**
	 * @param camasTagMap the camasTagMap to set
	 */
	public void setCamasTagMap(HashMap<Object, Object> camasTagMap) {
		this.camasTagMap = camasTagMap;
	}

	/**
	 * @return the habitacionesTagMap
	 */
	public HashMap<String, Object> getHabitacionesTagMap() {
		return habitacionesTagMap;
	}

	/**
	 * @param habitacionesTagMap the habitacionesTagMap to set
	 */
	public void setHabitacionesTagMap(HashMap<String, Object> habitacionesTagMap) {
		this.habitacionesTagMap = habitacionesTagMap;
	}

	/**
	 * @return the pisosTagMap
	 */
	public HashMap<String, Object> getPisosTagMap() {
		return pisosTagMap;
	}

	/**
	 * @param pisosTagMap the pisosTagMap to set
	 */
	public void setPisosTagMap(HashMap<String, Object> pisosTagMap) {
		this.pisosTagMap = pisosTagMap;
	}

////////
	
	/**
	 * @return the areasTagMap
	 */
	public Object getAreasTagMap(String key) {
		return areasTagMap.get(key);
	}

	/**
	 * @param areasTagMap the areasTagMap to set
	 */
	public void setAreasTagMap(String key, Object value) {
		this.areasTagMap.put(key, value);
	}

	/**
	 * @return the camasTagMap
	 */
	public Object getCamasTagMap(String key) {
		return camasTagMap.get(key);
	}

	/**
	 * @param camasTagMap the camasTagMap to set
	 */
	public void setCamasTagMap(String key, Object value) {
		this.camasTagMap.put(key, value);
	}

	/**
	 * @return the habitacionesTagMap
	 */
	public Object getHabitacionesTagMap(String key) {
		return habitacionesTagMap.get(key);
	}

	/**
	 * @param habitacionesTagMap the habitacionesTagMap to set
	 */
	public void setHabitacionesTagMap(String key, Object value) {
		this.habitacionesTagMap.put(key, value);
	}

	/**
	 * @return the pisosTagMap
	 */
	public Object getPisosTagMap(String key) {
		return pisosTagMap.get(key);
	}

	/**
	 * @param pisosTagMap the pisosTagMap to set
	 */
	public void setPisosTagMap(String key, Object value) {
		this.pisosTagMap.put(key, value);
	}

	/**
	 * @return the fechaFinalFiltro
	 */
	public String getFechaFinalFiltro() {
		return fechaFinalFiltro;
	}

	/**
	 * @param fechaFinalFiltro the fechaFinalFiltro to set
	 */
	public void setFechaFinalFiltro(String fechaFinalFiltro) {
		this.fechaFinalFiltro = fechaFinalFiltro;
	}

	/**
	 * @return the fechaInicialFiltro
	 */
	public String getFechaInicialFiltro() {
		return fechaInicialFiltro;
	}

	/**
	 * @param fechaInicialFiltro the fechaInicialFiltro to set
	 */
	public void setFechaInicialFiltro(String fechaInicialFiltro) {
		this.fechaInicialFiltro = fechaInicialFiltro;
	}

	public HashMap<String, Object> getArticulosConfirmacion() {
		return articulosConfirmacion;
	}

	public void setArticulosConfirmacion(
			HashMap<String, Object> articulosConfirmacion) {
		this.articulosConfirmacion = articulosConfirmacion;
	}

	public boolean isGenerarOrdenArticulosConfirmada() {
		return generarOrdenArticulosConfirmada;
	}

	public void setGenerarOrdenArticulosConfirmada(
			boolean generarOrdenArticulosConfirmada) {
		this.generarOrdenArticulosConfirmada = generarOrdenArticulosConfirmada;
	}

	public ResultadoBoolean getMostrarValidacionArticulos() {
		return mostrarValidacionArticulos;
	}

	public void setMostrarValidacionArticulos(
			ResultadoBoolean mostrarValidacionArticulos) {
		this.mostrarValidacionArticulos = mostrarValidacionArticulos;
	}

public Object getMedicamentos(String key)
{
	return this.medicamentos.get(key);
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
	 * 
	 * @param key
	 * @param value
	 */
    public void setJustificacionMap(String key, Object value) 
    {
    	this.justificacionMap.put(key, value);
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

	
	public void setMedicamentosPosMap(String key, Object value) {
		this.medicamentosPosMap.put(key, value);
	}
	
	public Object getMedicamentosPosMap(String key) {
        return medicamentosPosMap.get(key);
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
	 * 
	 * @return
	 */
	public int getNumConveniosPlanEspecial() {
		return numConveniosPlanEspecial;
	}
	
	/**
	 * 
	 * @param numConveniosPlanEspecial
	 */
	public void setNumConveniosPlanEspecial(int numConveniosPlanEspecial) {
		this.numConveniosPlanEspecial = numConveniosPlanEspecial;
	}

	/**
	 * @return the listadoHistoricoSolicitudMap
	 */
	public HashMap getListadoHistoricoSolicitudMap() {
		return listadoHistoricoSolicitudMap;
	}

	/**
	 * @param listadoHistoricoSolicitudMap the listadoHistoricoSolicitudMap to set
	 */
	public void setListadoHistoricoSolicitudMap(HashMap listadoHistoricoSolicitudMap) {
		this.listadoHistoricoSolicitudMap = listadoHistoricoSolicitudMap;
	}
	
	/**
	 * @return the listadoHistoricoSolicitudMap
	 */
	public Object getListadoHistoricoSolicitudMap(String key) {
		return listadoHistoricoSolicitudMap.get(key);
	}

	/**
	 * @param listadoHistoricoSolicitudMap the listadoHistoricoSolicitudMap to set
	 */
	public void setListadoHistoricoSolicitudMap(String key, Object value) {
		this.listadoHistoricoSolicitudMap.put(key, value);
	}

	public String getHiddens() {
		return hiddens;
	}

	public void setHiddens(String hiddens) {
		this.hiddens = hiddens;
	}

	public String getArtConsultaNP() {
		return artConsultaNP;
	}

	public void setArtConsultaNP(String artConsultaNP) {
		this.artConsultaNP = artConsultaNP;
	}

	/**
	 * @return the medicamentosDiferenteDosificacion
	 */
	public HashMap getMedicamentosDiferenteDosificacion() {
		return medicamentosDiferenteDosificacion;
	}

	/**
	 * @param medicamentosDiferenteDosificacion the medicamentosDiferenteDosificacion to set
	 */
	public void setMedicamentosDiferenteDosificacion(
			HashMap medicamentosDiferenteDosificacion) {
		this.medicamentosDiferenteDosificacion = medicamentosDiferenteDosificacion;
	}
	
	/**
	 * @return the medicamentosDiferenteDosificacion
	 */
	public Object getMedicamentosDiferenteDosificacion(Object key) {
		return medicamentosDiferenteDosificacion.get(key);
	}

	/**
	 * @param medicamentosDiferenteDosificacion the medicamentosDiferenteDosificacion to set
	 */
	public void setMedicamentosDiferenteDosificacion(
			Object key, Object value) {
		this.medicamentosDiferenteDosificacion.put(key, value);
	}

	/**
	 * @return the codArticuloSelDiferenteDosificacion
	 */
	public int getCodArticuloSelDiferenteDosificacion() {
		return codArticuloSelDiferenteDosificacion;
	}

	/**
	 * @param codArticuloSelDiferenteDosificacion the codArticuloSelDiferenteDosificacion to set
	 */
	public void setCodArticuloSelDiferenteDosificacion(
			int codArticuloSelDiferenteDosificacion) {
		this.codArticuloSelDiferenteDosificacion = codArticuloSelDiferenteDosificacion;
	}

	/**
	 * @return the numeroSolicitudMedDiferenteDosificacion
	 */
	public int getNumeroSolicitudMedDiferenteDosificacion() {
		return numeroSolicitudMedDiferenteDosificacion;
	}

	/**
	 * @param numeroSolicitudMedDiferenteDosificacion the numeroSolicitudMedDiferenteDosificacion to set
	 */
	public void setNumeroSolicitudMedDiferenteDosificacion(
			int numeroSolicitudMedDiferenteDosificacion) {
		this.numeroSolicitudMedDiferenteDosificacion = numeroSolicitudMedDiferenteDosificacion;
	}

	/**
	 * @return the numeroOrdenMedicaMedDiferenteDosificacion
	 */
	public int getNumeroOrdenMedicaMedDiferenteDosificacion() {
		return numeroOrdenMedicaMedDiferenteDosificacion;
	}

	/**
	 * @param numeroOrdenMedicaMedDiferenteDosificacion the numeroOrdenMedicaMedDiferenteDosificacion to set
	 */
	public void setNumeroOrdenMedicaMedDiferenteDosificacion(
			int numeroOrdenMedicaMedDiferenteDosificacion) {
		this.numeroOrdenMedicaMedDiferenteDosificacion = numeroOrdenMedicaMedDiferenteDosificacion;
	}

	public int getCodigoDetalleCargo() {
		return codigoDetalleCargo;
	}

	public void setCodigoDetalleCargo(int codigoDetalleCargo) {
		this.codigoDetalleCargo = codigoDetalleCargo;
	}

	public int getCodigoConvenioCargo() {
		return codigoConvenioCargo;
	}

	public void setCodigoConvenioCargo(int codigoConvenioCargo) {
		this.codigoConvenioCargo = codigoConvenioCargo;
	}

	public int getCodigoSubCuentaCargo() {
		return codigoSubCuentaCargo;
	}

	public void setCodigoSubCuentaCargo(int codigoSubCuentaCargo) {
		this.codigoSubCuentaCargo = codigoSubCuentaCargo;
	}

	public String getCheckCE() {
		return checkCE;
	}

	public void setCheckCE(String checkCE) {
		this.checkCE = checkCE;
	}

	public void setListaProcesoAutorizacion(ArrayList<DTOProcesoAutorizacion> listaProcesoAutorizacion) {
		this.listaProcesoAutorizacion = listaProcesoAutorizacion;
	}

	public ArrayList<DTOProcesoAutorizacion> getListaProcesoAutorizacion() {
		return listaProcesoAutorizacion;
	}

	public void setNombreArchivoGenerado(String nombreArchivoGenerado) {
		this.nombreArchivoGenerado = nombreArchivoGenerado;
	}

	public String getNombreArchivoGenerado() {
		return nombreArchivoGenerado;
	}

	public void setFormatoImpAutorizacion(String formatoImpAutorizacion) {
		this.formatoImpAutorizacion = formatoImpAutorizacion;
	}

	public String getFormatoImpAutorizacion() {
		return formatoImpAutorizacion;
	}

	public String getTipoCieDiagnostico() {
		return tipoCieDiagnostico;
	}

	public void setTipoCieDiagnostico(String tipoCieDiagnostico) {
		this.tipoCieDiagnostico = tipoCieDiagnostico;
	}

	public String getAcronimoDiagnostico() {
		return acronimoDiagnostico;
	}

	public void setAcronimoDiagnostico(String acronimoDiagnostico) {
		this.acronimoDiagnostico = acronimoDiagnostico;
	}

	public boolean isEsOrdenAmbulatoria() {
		return esOrdenAmbulatoria;
	}

	public void setEsOrdenAmbulatoria(boolean esOrdenAmbulatoria) {
		this.esOrdenAmbulatoria = esOrdenAmbulatoria;
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

	public void setTipoCieDiagnosticoInt(int tipoCieDiagnosticoInt) {
		this.tipoCieDiagnosticoInt = tipoCieDiagnosticoInt;
	}

	public int getTipoCieDiagnosticoInt() {
		return tipoCieDiagnosticoInt;
	}
	
	public void setCentroCostoNoCorresponde(boolean centroCostoNoCorresponde) {
		this.centroCostoNoCorresponde = centroCostoNoCorresponde;
	}

	public boolean isCentroCostoNoCorresponde() {
		return centroCostoNoCorresponde;
	}
	
	/** 
	 * @param tieneDespacho
	 */

	public void setTieneDespacho(boolean tieneDespacho) {
		this.tieneDespacho = tieneDespacho;
	}
	
	/** 
	 * @return 
	 */

	public boolean isTieneDespacho() {
		return tieneDespacho;
	}

	public void setListaAdvertencias(ArrayList<String> listaAdvertencias) {
		this.listaAdvertencias = listaAdvertencias;
	}

	public ArrayList<String> getListaAdvertencias() {
		return listaAdvertencias;
	}

	/**
	 * @param generaAlertaEnfermeria the generaAlertaEnfermeria to set
	 */
	public void setGeneraAlertaEnfermeria(String generaAlertaEnfermeria) {
		this.generaAlertaEnfermeria = generaAlertaEnfermeria;
	}

	/**
	 * @return the generaAlertaEnfermeria
	 */
	public String getGeneraAlertaEnfermeria() {
		if(UtilidadTexto.isEmpty(generaAlertaEnfermeria)) {
			generaAlertaEnfermeria = ConstantesBD.acronimoNo;
		}
		return generaAlertaEnfermeria;
	}
	
	public void setInsertarJustNoPos(boolean insertarJustNoPos) {
		this.insertarJustNoPos = insertarJustNoPos;
	}

	public boolean isInsertarJustNoPos() {
		return insertarJustNoPos;
	}

	public DtoDiagnostico getDtoDiagnostico() {
		return dtoDiagnostico;
	}

	public void setDtoDiagnostico(DtoDiagnostico dtoDiagnostico) {
		this.dtoDiagnostico = dtoDiagnostico;
	}

	public ArrayList<InfoResponsableCobertura> getInfoCoberturaArticulo() {
		return infoCoberturaArticulo;
	}

	public void setInfoCoberturaArticulo(ArrayList<InfoResponsableCobertura> infoCoberturaArticulo) {
		this.infoCoberturaArticulo = infoCoberturaArticulo;
	}

	public String getJustificacionModifica() {
		return justificacionModifica;
	}

	public void setJustificacionModifica(String justificacionModifica) {
		this.justificacionModifica = justificacionModifica;
	}
}