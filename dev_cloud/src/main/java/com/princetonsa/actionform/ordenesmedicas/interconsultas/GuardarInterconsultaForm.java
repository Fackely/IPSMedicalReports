package com.princetonsa.actionform.ordenesmedicas.interconsultas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.facturacion.InfoResponsableCobertura;

import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;

/**
 * Form que contiene todos los datos específicos para generar la solicitud
 * de interconsulta, manejando tanto códigos como nombres.
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1.0, Feb 10, 2004
 */

public class GuardarInterconsultaForm extends ValidatorForm 
{
		/**
		 * Verificar que ya hay un manejo conjunto para la cuenta y el centro de costo solicitado
		 */
		private boolean manejoConjunto;
		
		/**
		 *	boolean para verificar si el usuario desea anular
		 * una solicitud de interconsulta 
		 */
		private boolean anularSolicitud;
		
		/**
		 * Campo requerido cuando  anularSolicitud=true,
		 *  para justificar ¿por qué?  desea anular una solicitud
		 */
		private String motivoAnulacion;
		
		/**
		 * Indica el código del médico que generó la anulación
		 */
		private int codigoMedicoAnulacion;
		
		/**
		 * Colección con los nombres generados de los archivos adjuntos 
		 */
		private final Map documentosAdjuntosGenerados = new HashMap(); 
		
		/**
		 * Indica el # de  consecutivo de la orden médica
		 */
		private int consecutivoOrdenesMedicas;
		
		/**
		 * Número de documentos adjuntos
		 */
		private int numDocumentosAdjuntos = 0;
		
		/**
		 * Es el numero de la Solicitud asociado a la interconsulta
		 */
		private int numeroSolicitud;
		
		/**
		 * nombre (String) que contiene el nombre del servicio solicitado
		 * asociado a la interconsulta
		 */
		private String nombreCodigoServicioSolicitado; 
		
		/**
		 * String que contiene el acronimo de dias tramite urgente o normal grupo del Servicio Solicitado
		 */
		private String acronimoDiasTramite;
		
		/**
		 * boolean para verificar si existía solicitudes de cambio de 
		 * tratante anteriores.
		 */
		private boolean tieneSolicitudesCambioTratantePrevias;
		
		/**
		 * Para seleccionar otros procedimientos
		 */
		private String nombreOtros;
		
		/**
		 *  resumen de la Historia Clinica  asociado a la interconsulta
		 */
		private String resumenHistoriaClinica;
		
		/**
		 *  comentario asociado a la interconsulta
		 */
		private String comentario;
		
		/**
		 * Motivo de la solicitud asociado a la interconsulta 
		 */
		private String motivoSolicitud;
		
		/**
		 *  String para almacenar un nuevo motivo de solicitud
		 *  en la funcionalidad modificar interconsulta
		 */
		private String motivoSolicitudNueva;
		
		/**
		 *  String para almacenar un nuevo resumen de Historia
		 *  clínica en la funcionalidad modificar interconsulta
		 */
		private String resumenHistoriaClinicaNueva;
		
		/**
		 *  String para almacenar un nuevo comentario
		 *  en la funcionalidad modificar interconsulta
		 */
		
		
		private String comentarioNuevo;
		
		/** Estado de historia clínica de la solicitud */
		private InfoDatosInt estadoHistoriaClinica;
		
		/**
		 *  Opciones de manejo interconsulta
		 *  1) se desea un concepto solamente
		 *  2) Se desea un conceto solamente
		 *  3) Manejo conjunto 
		 */ 
		private int seleccionManejo; 
		
		/**
		 * para determinar el cod del procedimiento
		 */
		private int codigoServicioInterconsulta;
		
		/**
		 *  Para determinar cual es el nombre del procedimiento   solicitado
		 */ 
		private String nombreServicio;
		
		/**
	 	* Estado en el que se encuentra el proceso.
	 	*/
		private String estado;
		
		/**
		* bool para verificar solicitud otro procedimiento
		*/
		private boolean esSolicitudOtros;
		
	    /**
	     * boolean para verificar si existe alguna solicitud de transferencia previa
	     * y mostrar la correspondiente advertencia
	     */
	    private boolean existeSolicitudTransferenciaPrevia;
		
		/**
		 * String para manejar el número de autorización  de la solicitud de interconsulta	
		 */
		//private String numeroAutorizacion; 
		
		/**
		 * El centro de costo que está realizando la solicitud de interconsulta
		 */
		private String centroCostoSolicitado;
		
		/**
		 * El centro de costo que podrá responder la solicitud de interconsulta
		 */
		private String ocupacionSolicitada;
		
		/**
		 * El centro de costo que podrá responder la solicitud de interconsulta
		 */
		private String codigoOcupacionSolicitada;
		
		/**
		 *  código de la especialidad solicitante de la solicitud de interconsulta
		 */
		private String especialidadSolicitante;
		
		/**
		 *  para validar ocupación en la transferencia de manejo 
		 * y manejo conjunto
		 */
		private boolean esMedico;
	
		/**
		 * Indica el código de la cuenta asociada
		 * a interconsulta
		 */
		private int codigoCuenta;
	
		/**
		* Indicativo de urgencia para la solicitud de interconsulta
		*/
		private boolean urgente;
	
		/**
		 * Fecha de grabación postulada por el sistema
		 */	
		private String fechaGrabacion;
		
		/**
		 *Hora de grabación postulada por el sistema 
		 */
		private String horaGrabacion;
		
		/**
		 * centro de costo que solicita asociado a la interconsulta
		 */
		private String centroCostoSolicitante;
		
		/**
		 * fecha de solicitud de la  interconsulta
		 */
		private String fechaSolicitud;
		
		/**
		 * Hora de solicitud de la  interconsulta
		 */
		private String horaSolicitud;
		
		/**
		 * indica si es resumen de atenciones o no
		 */
		private boolean esResumenAtenciones=false;
		
		/**
		 * indicativo que indica si la solicitud es de pyp.
		 */
		private boolean solPYP=false;
		
		/**
		 * variable que me indica desde que accion estoy llamando la funcionalidad (ejecutar - solictar - programar)
		 */
		private String accionPYP="";
		
		/**
		 * Arreglo donde se almacenan las opciones del manejo de interconsulta
		 */
		private ArrayList<HashMap<String, Object>> opcionesManejo = new ArrayList<HashMap<String,Object>>();
		
		/**
		 * Graba la informacion de la justificacion
		 * */
		private String justificacionSolicitud;
		
		/**
		 * Graba la informacion de la modificacion de la justificacion 
		 * */
		private String justificacionSolicitudNueva;
		
		/**
		 * Este atributo se usa para determinar cuando se generó una 
		 * autorización de solicitudes y mostrar el respectivo botón
		 */
		private boolean mostrarImprimirAutorizacion;
		
		/**
		 * lista que contiene los nombres de los reportes de las autorzaciones 
		 * 
		 */
		private ArrayList<String> listaNombresReportes=new ArrayList<String>();
		
		/**
		 * Atributo que contiene el diagnóstico y tipo CIE del paciente
		 */
		private DtoDiagnostico dtoDiagnostico;
		
		/**
		 * Atributo que indica si se genero o no una Autorizacion de 
		 * Capitacion Automatica para la solicitud de interconsulta
		 */
		private boolean generoAutorizacion;
		
		/**Lista que almacena los servicios que se asignaron en la cita*/
		private ArrayList<String> listaServiciosImpimirOrden = new ArrayList<String>();
		
		/**Almacena mensajes de Advertencia */
		private ArrayList<String> listaAdvertencias;
		
		/**Lista que almacena la respectiva cobertura de cada servicio*/
		private List<InfoResponsableCobertura> infoCoberturaServicio;
		
		
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
				for( int i=0; i<this.numDocumentosAdjuntos; i++ )
				{
					if( request.getParameter("documentoAdjuntoGenerado(checkbox_"+i+")") == null )
						this.setDocumentoAdjuntoGenerado("checkbox_"+i, "off");
					else
						this.setDocumentoAdjuntoGenerado("checkbox_"+i, "on");					
				}
				if(request.getParameter("anularSolicitud")==null)
					this.setAnularSolicitud(false);

					ActionErrors errores= new ActionErrors();
					if(estado.equals("salir"))
					{				
						errores=super.validate(mapping,request);
						if(seleccionManejo==-1)
						{
							errores.add("Seleccion Manejo", new ActionMessage("errors.required","El Selección de Manejo"));
						}
						
						if(this.motivoSolicitud.length()>3000)
							errores.add("campo motivo de la solcitud", new ActionMessage("errors.maxlength","El motivo de la orden","3000"));
						
						if(this.resumenHistoriaClinica.length()>3000)
							errores.add("campo resume historia clinica", new ActionMessage("errors.maxlength","El resumen de la historia clínica","3000"));
					}
					else if(estado.equals("modificar"))
					{
								errores=super.validate(mapping,request);
					}
					else if(estado.equals("inserta"))
					{
								mapping.findForward("principal");
					}
				
					else if(estado.equals("guardarModificacion"))
					{
								errores=super.validate(mapping,request);
								if(anularSolicitud==true)
								{
										if(motivoAnulacion.equals(""))
										{
													errores.add("ab.cd",new ActionMessage("errors.required","Si selecciona Anular solicitud, debe llenar el campo Motivo Anulación solicitud Interconsulta que"));
										}
								}
					}
				return errores;
			}
			
				/**
				 *resetea todos los posible valores que se 
				 *utilizan en la solicitud de interconsulta 
				 */
				public void reset()
				{
					this.solPYP=false;
					this.accionPYP="";
					documentosAdjuntosGenerados.clear();
					numDocumentosAdjuntos=0;
					numeroSolicitud=0;
					consecutivoOrdenesMedicas=0;
					nombreOtros="";
					resumenHistoriaClinica="";
					comentario="";
					motivoSolicitud="";
					seleccionManejo=0; 
					codigoServicioInterconsulta=0;
					nombreCodigoServicioSolicitado="";
					nombreServicio="";
					estado="";
					esSolicitudOtros=false;
					//numeroAutorizacion="";
					centroCostoSolicitado="";
					ocupacionSolicitada="";
					codigoOcupacionSolicitada=ConstantesBD.codigoNuncaValido+"";
					especialidadSolicitante="";
					esMedico=false;
					urgente=false;
					centroCostoSolicitante="";
					fechaSolicitud="";
					horaSolicitud="";
					tieneSolicitudesCambioTratantePrevias=false;
					existeSolicitudTransferenciaPrevia=false;
					this.opcionesManejo = new ArrayList<HashMap<String,Object>>();
					this.justificacionSolicitud = "";
					this.justificacionSolicitudNueva = "";
					this.dtoDiagnostico = new DtoDiagnostico();
					this.mostrarImprimirAutorizacion=false;
					this.listaNombresReportes= new ArrayList<String>();
					this.listaServiciosImpimirOrden=new ArrayList<String>();
					this.setAcronimoDiasTramite("");
					this.listaAdvertencias=new ArrayList<String>();
				}
				
				/**
				 * resetea los valores que añaden o modifican campos
				 * de la solicitud de interconsulta
				 */
				public void resetNuevosQueModifican()
				{
					motivoSolicitudNueva="";
					resumenHistoriaClinicaNueva="";
					comentarioNuevo="";	
					anularSolicitud=false;
					motivoAnulacion="";
					this.numDocumentosAdjuntos=0;
					documentosAdjuntosGenerados.clear();
					this.justificacionSolicitudNueva = "";
				}
								
				/**Retorna el estado en que se encuentre la solicitud
				 * @return estado
				 */
				public String getEstado() {
					return estado;
				}
		
				/**Retorna el motivo de la solicitud de interconsulta
				 * @return
				 */
				public String getMotivoSolicitud() {
					return motivoSolicitud;
				}
		
				/**Retorna la seleccion de manejo dada a la solicitud de interconsulta
				 * @return
				 */
				public int getSeleccionManejo() {
					return seleccionManejo;
				}
		
				/**Asigna el estado en que se encuentre la solicitud
				 * @param estado
				 */
				public void setEstado(String string) {
					estado = string;
				}
		
				/**Asigna el motivo de la solicitud de interconsulta
				 * @param motivoSolicitud
				 */
				public void setMotivoSolicitud(String string) {
					motivoSolicitud = string;
				}
		
				/**Asigna la seleccion de manejo dada a la solicitud de interconsulta
				 * @param seleccionManejo
				 */
				public void setSeleccionManejo(int i) {
					seleccionManejo = i;
				}

				/**Retorna el código de servicio de la Interconsulta en formato entero
				 * @return
				 */
				public int getCodigoServicioInterconsulta() {
					return codigoServicioInterconsulta;
				}
		
				/**Retorna el comentario de la solicitud de interconsulta
				 * @return
				 */
				public String getComentario() {
					return comentario;
				}
		
				/**Retorna el nombre del procedimiento cuando el
				 * centro de costo es externo
				 * @return
				 */
				public String getNombreOtros() {
					return nombreOtros;
				}
		
				/**Retorna el número de la solicitud asociado a la solicitud de interconsulta
				 * @return
				 */
				public int getNumeroSolicitud() {
					return numeroSolicitud;
				}
		
				/**Retorna el resumen de la Historia Clínica
				 * @return
				 */
				public String getResumenHistoriaClinica() {
					return resumenHistoriaClinica;
				}
		
				/**Asigna el código de servicio de la Interconsulta en formato entero
				 * @param codigoServicioInterconsulta
				 */
				public void setCodigoServicioInterconsulta(int i) {
					codigoServicioInterconsulta = i;
				}
		
				/**Asigna el comentario asociado a la solicitud de interconsulta
				 * @param comentario
				 */
				public void setComentario(String string) {
					comentario = string;
				}
		
				/**Asigna el nombre del procedimiento cuando el
				 * centro de costo es externo
				 * @param nombreOtros
				 */
				public void setNombreOtros(String string) {
					nombreOtros = string;
				}
		
				/**Asigna el número de la solicitud asociado a la solicitud de interconsulta
				 * @param numeroSolicitud
				 */
				public void setNumeroSolicitud(int i) {
					numeroSolicitud = i;
				}
		
				/**Asigna el resumen de la Historia Clínica
				 * @param resumenHistoriaClinica
				 */
				public void setResumenHistoriaClinica(String string) {
					resumenHistoriaClinica = string;
				}

				/**Retorna un bool si la solicitud de procedimineto es otros
				 * @return
				 */
				public boolean isEsSolicitudOtros() {
					return esSolicitudOtros;
				}
		
				/**Asigna un bool si la solicitud de procedimineto es otros
				 * @param esSolicitudOtros
				 */
				public void setEsSolicitudOtros(boolean b) {
					esSolicitudOtros = b;
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
				 * Retorna el número de documentos adjuntos
				 * @return int
				 */
				public int getNumDocumentosAdjuntos()
				{
					return numDocumentosAdjuntos;
				}
		
				/**
				 * Asigna el número de documentos adjuntos
				 * @param numDocumentosAdjuntos The numDocumentosAdjuntos to set
				 */
				public void setNumDocumentosAdjuntos(int numDocumentosAdjuntos)
				{
					this.numDocumentosAdjuntos = numDocumentosAdjuntos;
				}
		
				/**Asigna el número de autorización de la solicitud de Interconsulta
				 * @param numeroAutorizacion
				 */
				/*
				public void setNumeroAutorizacion(String string) {
					numeroAutorizacion = string;
				}
				*/
				/**Retorna el número de autorización de la solicitud de Interconsulta
				 * @return
				 */
				/*
				public String getNumeroAutorizacion() {
					return numeroAutorizacion;
				}
				*/
				/**Retorna el centro de Costo solicitado de la solicitud de Interconsulta
				 * @return
				 */
				public String getCentroCostoSolicitado() {
					return centroCostoSolicitado;
				}
		
				/**Asigna el centro de Costo solicitado de la solicitud de Interconsulta
				 * @param centroCostoSolicitado
				 */
				public void setCentroCostoSolicitado(String string) {
					centroCostoSolicitado = string;
				}
		
				/**Retorna la ocupación solicitada de la solicitud de Interconsulta
				 * @return
				 */
				public String getOcupacionSolicitada() {
					return ocupacionSolicitada;
				}
		
				/**Asigna la ocupación solicitada de la solicitud de Interconsulta
				 * @param ocupacionSolicitada
				 */
				public void setOcupacionSolicitada(String string) {
					ocupacionSolicitada = string;
				}
		
				/**Retorna la especialidad solicitante de la solicitud de Interconsulta
				 * @return
				 */
				public String getEspecialidadSolicitante() {
					return especialidadSolicitante;
				}
		
				/**Asigna la especialidad solicitante de la solicitud de Interconsulta
				 * @param especialidadSolicitante
				 */
				public void setEspecialidadSolicitante(String string) {
					especialidadSolicitante = string;
				}
		
				/**Retorna la Fecha de grabación de la solicitud de Interconsulta
				 * @return
				 */
				public String getFechaGrabacion() {
					return fechaGrabacion;
				}
		
				/**Asigna la fecha de grabación de la solicitud de interconsulta
				 * @param fechaGrabacion
				 */
				public void setFechaGrabacion(String string) {
					fechaGrabacion = string;
				}
		
				/**Retorna  la hora de grabación de la solicitud de interconsulta
				 * @return
				 */
				public String getHoraGrabacion() {
					return horaGrabacion;
				}
		
				/**Asigna la hora de grabación de la solicitud de interconsulta
				 * @param horaGrabacion
				 */
				public void setHoraGrabacion(String string) {
					horaGrabacion = string;
				}
		
				/**Retorna el centro de costo solicitante asociado a la solicitud de interconsulta
				 * @return
				 */
				public String getCentroCostoSolicitante() {
					return centroCostoSolicitante;
				}
		
				/**Asigna el centro de costo solicitante asociado a la solicitud de interconsulta
				 * @param centroCostoSolicitante
				 */
				public void setCentroCostoSolicitante(String string) {
					centroCostoSolicitante = string;
				}
		
				/**Retorna  la fecha de solicitud  de interconsulta
				 * @return
				 */
				public String getFechaSolicitud() {
					return fechaSolicitud;
				}
		
				/**Retorna  la hora de la solicitud de interconsulta
				 * @return
				 */
				public String getHoraSolicitud() {
					return horaSolicitud;
				}
		
				/**Asigna  la fecha de la solicitud de interconsulta
				 * @param fechaSolicitud
				 */
				public void setFechaSolicitud(String string) {
					fechaSolicitud = string;
				}
		
				/**Asigna  la hora de solicitud de interconsulta
				 * @param horaSolicitud
				 */
				public void setHoraSolicitud(String string) {
					horaSolicitud = string;
				}
		
				
				/**Retorna el nombre del servicio (procedimiento) solicitado
				 * @return
				 */
				public String getNombreServicio() {
					return nombreServicio;
				}
		
				/**Asigna el nombre del servicio (procedimiento) solicitado
				 * @param nombreServicio
				 */
				public void setNombreServicio(String string) {
					nombreServicio = string;
				}
		
				/**Retorna un bool para determinar si es urgente o no, asociado a la interconsulta
				 * @return
				 */
				public boolean getUrgente() {
					return urgente;
				}
			
				/**Asigna un bool para determinar si es urgente o no, asociado a la interconsulta
				 * @param urgente
				 */
				public void setUrgente(boolean b) {
					urgente = b;
				}
			
				/**Retorna un bool para determinar si el usuario es médico
				 * @return
				 */
				public boolean isEsMedico() {
					return esMedico;
				}
			
				/**Asigna un bool para determinar si el usuario es médico
				 * @param esMedico
				 */
				public void setEsMedico(boolean b) {
					esMedico = b;
				}

				/**Retorna el código de la cuenta asociada a la solicitud 
				 * @return
				 */
				public int getCodigoCuenta() {
					return codigoCuenta;
				}
		
				/**Asigna el código de la cuenta asociada a la solicitud
				 * @param codigoCuenta
				 */
				public void setCodigoCuenta(int i) {
					codigoCuenta = i;
				}

				/**Retorna un bool para determinar la existencia de una solicitud de 
				 * transferencia de manejo previa
				 * @return
				 */
				public boolean isExisteSolicitudTransferenciaPrevia() {
					return existeSolicitudTransferenciaPrevia;
				}
		
				/**Asigna  un bool para determinar la existencia de una solicitud de 
				 * transferencia de manejo previa
				 * @param existeSolicitudTransferenciaPrevia
				 */
				public void setExisteSolicitudTransferenciaPrevia(boolean b) {
					existeSolicitudTransferenciaPrevia = b;
				}
		
				/**Retorna una cadena para añadirle al motivo de la solicitud previamente ingresada
				 * @return
				 */
				public String getMotivoSolicitudNueva() {
					return motivoSolicitudNueva;
				}
		
				/**Asigna una cadena para añadirle al motivo de la solicitud previamente ingresada
				 * @param motivoSolicitudNueva
				 */
				public void setMotivoSolicitudNueva(String string) {
					motivoSolicitudNueva = string;
				}
		
				/**Retorna el consecutivo de la órden médica asociada a la solicitud de interconsulta
				 * @return
				 */
				public int getConsecutivoOrdenesMedicas() {
					return consecutivoOrdenesMedicas;
				}
		
				/**Asigna el consecutivo de la órden médica asociada a la solicitud de interconsulta
				 * @param consecutivoOrdenesMedicas
				 */
				public void setConsecutivoOrdenesMedicas(int i) {
					consecutivoOrdenesMedicas = i;
				}
		
				/**Retorna una cadena para añadirle al comentario de la solicitud previamente ingresada 
				 * @return
				 */
				public String getComentarioNuevo() {
					return comentarioNuevo;
				}
		
				/**Retorna una cadena para añadirle al resumen de la historia clínica de la solicitud previamente ingresada 
				 * @return
				 */
				public String getResumenHistoriaClinicaNueva() {
					return resumenHistoriaClinicaNueva;
				}
		
				/**Asigna una cadena para añadirle al comentario de la solicitud previamente ingresada 
				 * @param comentarioNuevo
				 */
				public void setComentarioNuevo(String string) {
					comentarioNuevo = string;
				}
		
				/**Asigna una cadena para añadirle al resumen de la historia clínica de la solicitud previamente ingresada 
				 * @param resumenHistoriaClinicaNueva
				 */
				public void setResumenHistoriaClinicaNueva(String string) {
					resumenHistoriaClinicaNueva = string;
				}
		
				/**Retorna un bool para determinar la existencia de solicitudes de cambio tratante previas
				 * @return
				 */
				public boolean getTieneSolicitudesCambioTratantePrevias() {
					return tieneSolicitudesCambioTratantePrevias;
				}
		
				/**Asigna un bool para determinar la existencia de solicitudes de cambio tratante previas
				 * @param tieneSolicitudesCambioTratantePrevias
				 */
				public void setTieneSolicitudesCambioTratantePrevias(boolean b) {
					tieneSolicitudesCambioTratantePrevias = b;
				}
		
				/**Retorna el código del médico que pretende generar la anulación de una interconsulta
				 * @return
				 */
				public int getCodigoMedicoAnulacion() {
					return codigoMedicoAnulacion;
				}
		
				/**Retorna el motivo de la anulación de la interconsulta
				 * @return
				 */
				public String getMotivoAnulacion() {
					return motivoAnulacion;
				}
		
				/**Asigna  el código del médico que pretende generar la anulación de una interconsulta
				 * @param codigoMedicoAnulacion
				 */
				public void setCodigoMedicoAnulacion(int i) {
					codigoMedicoAnulacion = i;
				}
		
				/**Asigna el motivo de la anulación de la solicitud de interconsulta
				 * @param motivoAnulacion
				 */
				public void setMotivoAnulacion(String string) {
					motivoAnulacion = string;
				}
		
				/**Retorna un bool para determinar si se activó el checkbox de anulación de solicitud
				 * @return
				 */
				public boolean getAnularSolicitud() {
					return anularSolicitud;
				}
		
				/**Asigna un bool para determinar si se activó el checkbox de anulación de solicitud
				 * @param anularSolicitud
				 */
				public void setAnularSolicitud(boolean b) {
					anularSolicitud = b;
				}
				
				/**Retorna el nombre (String) del código de servicio solicitado asociado a la solicitud
				 * @return
				 */
				public String getNombreCodigoServicioSolicitado() {
					return nombreCodigoServicioSolicitado;
				}
		
				/**Asigna el nombre (String) del código de servicio solicitado asociado a la solicitud
				 * @param nombreCodigoServicioSolicitado 
				 */
				public void setNombreCodigoServicioSolicitado(String string) {
					nombreCodigoServicioSolicitado = string;
				}
		
		/**
		 * Ya hay un manejo conjunto para la cuenta y el centro de costo solicitado
		 * @return
		 */
		public boolean getManejoConjunto()
		{
			return manejoConjunto;
		}

		/**
		 * Ya hay un manejo conjunto para la cuenta y el centro de costo solicitado
		 * @param b
		 */
		public void setManejoConjunto(boolean b)
		{
			manejoConjunto= b;
		}

		/**
		 * @return Retorna el estadoHistoriaClinica.
		 */
		public InfoDatosInt getEstadoHistoriaClinica() {
			return estadoHistoriaClinica;
		}
		/**
		 * @param estadoHistoriaClinica Asigna el estadoHistoriaClinica.
		 */
		public void setEstadoHistoriaClinica(InfoDatosInt estadoHistoriaClinica) {
			this.estadoHistoriaClinica = estadoHistoriaClinica;
		}
        /**
         * @return Returns the esResumenAtenciones.
         */
        public boolean getEsResumenAtenciones() {
            return esResumenAtenciones;
        }
        /**
         * @param esResumenAtenciones The esResumenAtenciones to set.
         */
        public void setEsResumenAtenciones(boolean esResumenAtenciones) {
            this.esResumenAtenciones = esResumenAtenciones;
        }

		public String getCodigoOcupacionSolicitada() {
			return codigoOcupacionSolicitada;
		}

		public void setCodigoOcupacionSolicitada(String codigoOcupacionSolicitada) {
			this.codigoOcupacionSolicitada = codigoOcupacionSolicitada;
		}

		public String getAccionPYP() {
			return accionPYP;
		}

		public void setAccionPYP(String accionPYP) {
			this.accionPYP = accionPYP;
		}

		public boolean isSolPYP() {
			return solPYP;
		}

		public void setSolPYP(boolean solPYP) {
			this.solPYP = solPYP;
		}

		/**
		 * @return the opcionesManejo
		 */
		public ArrayList<HashMap<String, Object>> getOpcionesManejo() {
			return opcionesManejo;
		}

		/**
		 * @param opcionesManejo the opcionesManejo to set
		 */
		public void setOpcionesManejo(ArrayList<HashMap<String, Object>> opcionesManejo) {
			this.opcionesManejo = opcionesManejo;
		}
		
		/**
		 * Método para obtener el número de opciones de manejo de interconsulta
		 * @return
		 */
		public int getNumOpcionesManejo()
		{
			return this.opcionesManejo.size();
		}

		public String getJustificacionSolicitud() {
			return justificacionSolicitud;
		}

		public void setJustificacionSolicitud(String justificacionSolicitud) {
			this.justificacionSolicitud = justificacionSolicitud;
		}	

		public String getJustificacionSolicitudNueva() {
			return justificacionSolicitudNueva;
		}

		public void setJustificacionSolicitudNueva(String justificacionSolicitudNueva) {
			this.justificacionSolicitudNueva = justificacionSolicitudNueva;
		}

		public void setMostrarImprimirAutorizacion(
				boolean mostrarImprimirAutorizacion) {
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

		public void setGeneroAutorizacion(boolean generoAutorizacion) {
			this.generoAutorizacion = generoAutorizacion;
		}

		public boolean isGeneroAutorizacion() {
			return generoAutorizacion;
		}

		public void setListaServiciosImpimirOrden(
				ArrayList<String> listaServiciosImpimirOrden) {
			this.listaServiciosImpimirOrden = listaServiciosImpimirOrden;
		}

		public ArrayList<String> getListaServiciosImpimirOrden() {
			return listaServiciosImpimirOrden;
		}

		public void setAcronimoDiasTramite(String acronimoDiasTramite) {
			this.acronimoDiasTramite = acronimoDiasTramite;
		}

		public String getAcronimoDiasTramite() {
			return acronimoDiasTramite;
		}

		public void setListaAdvertencias(ArrayList<String> listaAdvertencias) {
			this.listaAdvertencias = listaAdvertencias;
		}

		public ArrayList<String> getListaAdvertencias() {
			return listaAdvertencias;
		}
		
		/**
		 * @return infoCoberturaServicio
		 */
		public List<InfoResponsableCobertura> getInfoCoberturaServicio() {
			return infoCoberturaServicio;
		}

		/**
		 * @param infoCoberturaServicio
		 */
		public void setInfoCoberturaServicio(List<InfoResponsableCobertura> infoCoberturaServicio) {
			this.infoCoberturaServicio = infoCoberturaServicio;
		}
		
		
}
