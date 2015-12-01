package com.princetonsa.actionform.ordenesmedicas.procedimientos;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.dto.ordenes.DtoProcedimiento;

import util.ConstantesBD;
import util.UtilidadCadena;

/**
 * ActionForm, tiene la funci�n de bean dentro de la forma, que contiene todos
 * los datos generales de un procedimiento. Y adicionalmente hace el manejo de
 * reset de la forma y de validaci�n de errores de datos de entrada.
 * @version 1.0, Febrero 25 / 2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @see org.apache.struts.action.ActionForm#validate(ActionMapping,
 * HttpServletRequest)
 */
public class ProcedimientoForm extends ValidatorForm
{
	/**
	 * Estado en el que se encuentra la ejecuci�n
	 */
	private String estado;
	
	/**
	 * Campo utilizado para responder solicitudes de ordenes m�dicas hechas a
	 * centros de costo externos a servicios no parametrizados
	 */
	private String respuestaOtros;
	
	/**
	 * Observaciones de respuesta de la interconsulta para otros previamente
	 * ingresada
	 */
	private String respuestaOtrosAnterior;
	
	/**
	 * Numero de autorizacion de la solicitud del procedimiento
	 */
	//private String numeroAutorizacion;	
	/**
	 * N�mero de solicitud de la solicitud de procedimiento a responder
	 */
	private int numeroSolicitud = 0;
	
	/**
	 * Fecha en que se ejecut� el procedimiento
	 */
	private String fechaEjecucion;
	
	/**
	 * Fecha en que se realiz� la solicitud del procedimiento
	 */
	private String fechaSolicitud;
	
	/**
	 * Hora en se ejecut� el procedimiento.
	 */
	private String horaEjecucion;
	
	/**
	 * Hora en que se realiz� la solicitud del procedimiento.
	 */
	private String horaSolicitud;
	
	/**
	 * Resultados del procedimiento
	 */
	private String resultados;
	
	/**
	 * Resultados ingresados anteriormente.
	 */
	private String resultadosAnteriores;
	
	/**
	 * Observaciones del procedimiento
	 */
	private String observaciones;
	
	/**
	 * Observaciones ingresadas anteriormente
	 */
	private String observacionesAnteriores;
	
	/**
	 * C�digo del tipo de recargo del procedimiento
	 */
	private int codigoTipoRecargo;

	/**
	 * Nombre del tipo de recargo del procedimiento
	 */	
	private String nombreTipoRecargo;
	
	/**
	 * Comentario historica cl�nica de la secci�n de documentos adjuntos
	 */
	private String comentariosHistoriaClinica;
	
	/**
	 * Comentarios historia cl�nica ingresados anteriormente
	 */
	private String comentariosHistoriaClinicaAnteriores;
	
	/**
	 * Colecci�n con los nombres generados de los archivos adjuntos 
	 */
	private final Map documentosAdjuntosGenerados = new HashMap(); 
	
	/**
	 * N�mero de documentos adjuntos
	 */
	private int numDocumentosAdjuntos = 1;
	
	private int ind;
	
	/**
	 * Solicitud m�ltiple
	 */
	private boolean multiple;
	
	/**
	 * Finalizar solicitud M�ltiple
	 */
	private boolean finalizar;
	
	/**
	 * Variable para almacenar el nombre del servicio solicitado
	 */
	private String nombreServicio;
	
	/**
	 * Indica si viene de pyp
	 */
	private boolean vieneDePyp = false;
	
	
	/**
	 * Datos m�dico que responde
	 */
	private String datosMedicoResponde;
	
	/**
	 * C�digo m�dico responde
	 */
	private int codigoMedicoResponde;

	
	
	//********ATRIBUTOS DIAGN�STICOS PROCEDIMIENTOS***************************
	/**
	 * Objeto que almacena los diagn�sticos de la respuesta de procedimientos
	 */
	private HashMap diagnosticos = new HashMap();
	/**
	 * N�mero de diagn�sticos del procedimientos
	 */
	private int numDiagnosticos;
	//************************************************************************
	
	/**
	 * DtoPlantilla
	 * */
	private DtoPlantilla plantillaDto;
	
	/**
	 * 
	 * */
	private DtoProcedimiento procedimientoDto;
	
	/**
	 * String indicador 
	 * */
	private String indicadorDummy;
	
	//Atributo para saber si se muestra o no informacion del paciente en la impresion 
	private boolean ocultarInfoPaciente;
	
	
	private ArrayList<String> codigosRespuestas = new ArrayList<String>();
	private String codigoRespuesta;
	//************ATRIBUTOS PARA EL FLUJO DE PLANTILLAS DE NO CRUENTOS*******************
	private String codigoCirugia;

	private boolean consultarIterpretacion;
	
	//************************************************************************************
	
	/**
	 * Borra los datos especificados del form.
	 */
	public void reset()	
	{		
		this.fechaEjecucion = "";
		this.fechaSolicitud = "";
		this.resultados = "";
		this.resultadosAnteriores = "";
		this.observaciones = "";
		this.observacionesAnteriores = "";
		this.codigoTipoRecargo = 0;
		this.nombreTipoRecargo = "";
		this.comentariosHistoriaClinica = "";
		this.comentariosHistoriaClinicaAnteriores = "";
		this.numDocumentosAdjuntos = 0;
		this.documentosAdjuntosGenerados.clear();
		//this.numeroAutorizacion = "";
		this.respuestaOtros  = "";
		this.respuestaOtrosAnterior = "";
		this.ind=0;
		this.nombreServicio = "";
		
		//atributos diagn�sticos procedimientos
		this.diagnosticos = new HashMap();
		this.numDiagnosticos = 0;
		this.plantillaDto = new DtoPlantilla();
		this.procedimientoDto = new DtoProcedimiento();
		
		this.indicadorDummy = ConstantesBD.acronimoNo;
		
		this.datosMedicoResponde = "";
		this.codigoMedicoResponde = ConstantesBD.codigoNuncaValido;
		this.consultarIterpretacion=true;
		
		
	}
	
	/**
	 * @return the codigoCirugia
	 */
	public String getCodigoCirugia() {
		return codigoCirugia;
	}

	/**
	 * @param codigoCirugia the codigoCirugia to set
	 */
	public void setCodigoCirugia(String codigoCirugia) {
		this.codigoCirugia = codigoCirugia;
	}

	/**
	* Valida  las propiedades que han sido establecidas para este request HTTP,
	* y retorna un objeto <code>ActionErrors</code> que encapsula los errores de
	* validaci�n encontrados. Si no se encontraron errores de validaci�n,
	* retorna <code>null</code>.
	* @param mapping el mapeado usado para elegir esta instancia
	* @param request el <i>servlet request</i> que est� siendo procesado en este momento
	* @return un objeto <code>ActionErrors</code> con los (posibles) errores encontrados al validar este formulario,
	* o <code>null</code> si no se encontraron errores.
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = super.validate(mapping, request);		
		
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
		
		if(estado.equals("imprimir"))
		{
			return null;
		}
		
		if( estado != null && estado.equals("finalizar") ) 
		{
			// Fecha actual y patr�n de fecha a utilizar en las validaciones
			boolean tieneErrores=false;
			final Date fechaActualD = new Date();
			final SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
			final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
			final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("dd/MM/yyyy:HH:mm");
	
			//validar que la hora este en el formato valido
			Date horaEjecucionTemp=null;
			try
			{
				horaEjecucionTemp=timeFormatter.parse(this.horaEjecucion);
			}
			catch (java.text.ParseException e)
			{
				tieneErrores=true;
				errores.add("horaEjecucion", new ActionMessage("errors.formatoHoraInvalido", "de ejecuci�n"));
			}
			
			
			// Valida que la fecha de ejecuci�n tenga el formato de fecha establecido
			Date fechaEjecucionD = null;
			try 
			{
				fechaEjecucionD = dateFormatter.parse(this.fechaEjecucion);
			}	
			catch (java.text.ParseException e) 
			{
				tieneErrores=true;
				errores.add("fechaEjecucion", new ActionMessage("errors.formatoFechaInvalido", "de ejecuci�n"));
			}	
			
			if(!tieneErrores)
			{
				// Valida que la fecha de ejecuci�n no sea mayor que la fecha actual
				if ( fechaActualD.compareTo(fechaEjecucionD) < 0 ) 
				{
					errores.add("fechaEjecucion", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de ejecuci�n", "actual"));
				}
				else
				{
					Date fechaHoraEjecucion = null;

					try 
					{
						fechaHoraEjecucion = dateTimeFormatter.parse(this.fechaEjecucion + ":" + this.horaEjecucion);
					} catch (ParseException e) 
					{
						e.printStackTrace();
					}
					if ( fechaActualD.compareTo(fechaHoraEjecucion) < 0 ) 
					{
						errores.add("horaEjecucion", new ActionMessage("errors.horaSuperiorA", "de ejecucion", "actual"));
					}
					// Valida que la fecha de ejecuci�n sea mayor o igual que la fecha de solicitud
					Date fechaSolicitudD = null;
					try 
					{
						fechaSolicitudD = dateFormatter.parse(this.fechaSolicitud==null?"":this.fechaSolicitud);
						// validar que la fecha de ejecucion sea mayor a la fecha de solicitud.
						if( fechaEjecucionD != null && fechaEjecucionD.compareTo(fechaSolicitudD) < 0 )
						{
							errores.add("fechaEjecucion", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "de ejecuci�n", "de solicitud"));
						}
						if(fechaEjecucionD.compareTo(fechaSolicitudD) == 0)
						{
							if(horaEjecucionTemp.compareTo(timeFormatter.parse(horaSolicitud))<0)
							{
								errores.add("horaEjecucion", new ActionMessage("errors.horaSuperiorA", "Solicitud", "Ejecucion"));
							}
						}
					}	
					catch (java.text.ParseException e) 
					{
						errores.add("fechaEjecucion", new ActionMessage("errors.formatoFechaInvalido", "de solicitud"));
					}
				}

				//resto de validaciones de fecha y hora.
				
			}
			
			
			
			
		
			
			
			
			
			
			if( !UtilidadCadena.noEsVacio(this.resultados) )
				errores.add("respuesta", new ActionMessage("errors.required", "El campo resultados"));
		}		
		return errores;
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
	 * @return Returns the ind.
	 */
	public int getInd()
	{
		return ind;
	}
	/**
	 * @param ind The ind to set.
	 */
	public void setInd(int ind)
	{
		this.ind= ind;
	}
	/**
	 * Retorna el estado en el que se encuentra la ejecuci�n
	 * @return String
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * Asigna el estado en el que se encuentra la ejecuci�n
	 * @param estado The estado to set
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	/**
	 * Retorna el n�mero de solicitud de la solicitud de procedimiento a
	 * responder
	 * @return int
	 */
	public int getNumeroSolicitud()
	{
		return numeroSolicitud;
	}

	/**
	 * Asigna el n�mero de solicitud de la solicitud de procedimiento a
	 * responder
	 * @param numeroSolicitud The numeroSolicitud to set
	 */
	public void setNumeroSolicitud(int numeroSolicitud)
	{
		this.numeroSolicitud = numeroSolicitud;
	}
	
	/**
	 * Retorna la fecha en que se ejecut� el procedimiento
	 * @return String
	 */
	public String getFechaEjecucion()
	{
		return fechaEjecucion;
	}

	/**
	 * Asigna la fecha en que se ejecut� el procedimiento
	 * @param fechaEjecucion The fechaEjecucion to set
	 */
	public void setFechaEjecucion(String fechaEjecucion)
	{
		this.fechaEjecucion = fechaEjecucion;
	}

	/**
	 * Retorna los resultados del procedimiento
	 * @return String
	 */
	public String getResultados()
	{
		return resultados;
	}

	/**
	 * Asigna los resultados del procedimiento
	 * @param resultados The resultados to set
	 */
	public void setResultados(String resultados)
	{
		this.resultados = resultados;
	}

	/**
	 * Retorna las observaciones del procedimiento
	 * @return String
	 */
	public String getObservaciones()
	{
		return observaciones;
	}

	/**
	 * Asigna las observaciones del procedimiento
	 * @param observaciones The observaciones to set
	 */
	public void setObservaciones(String observaciones)
	{
		this.observaciones = observaciones;
	}	

	/**
	 * Retorna el c�digo del tipo de recargo del procedimiento
	 * @return int
	 */
	public int getCodigoTipoRecargo()
	{
		return codigoTipoRecargo;
	}

	/**
	 * Asigna el c�digo del tipo de recargo del procedimiento
	 * @param codigoTipoRecargo The codigoTipoRecargo to set
	 */
	public void setCodigoTipoRecargo(int codigoTipoRecargo)
	{
		this.codigoTipoRecargo = codigoTipoRecargo;
	}

	/**
	 * Retorna el nombre del tipo de recargo del procedimiento
	 * @return String
	 */
	public String getNombreTipoRecargo()
	{
		return nombreTipoRecargo;
	}

	/**
	 * Asigna el nombre del tipo de recargo del procedimiento
	 * @param nombreTipoRecargo The nombreTipoRecargo to set
	 */
	public void setNombreTipoRecargo(String nombreTipoRecargo)
	{
		this.nombreTipoRecargo = nombreTipoRecargo;
	}

	/**
	 * Retorna el comentario historica cl�nica de la secci�n de documentos adjuntos
	 * @return String
	 */
	public String getComentariosHistoriaClinica()
	{
		return comentariosHistoriaClinica;
	}

	/**
	 * Asigna el comentario historica cl�nica de la secci�n de documentos
	 * adjuntos
	 * @param comentariosHistoriaClinica The comentariosHistoriaClinica to set
	 */
	public void setComentariosHistoriaClinica(String comentariosHistoriaClinica)
	{
		this.comentariosHistoriaClinica = comentariosHistoriaClinica;
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
	 * Retorna el n�mero de documentos adjuntos
	 * @return int
	 */
	public int getNumDocumentosAdjuntos()
	{
		return numDocumentosAdjuntos;
	}

	/**
	 * Asigna el n�mero de documentos adjuntos
	 * @param numDocumentosAdjuntos The numDocumentosAdjuntos to set
	 */
	public void setNumDocumentosAdjuntos(int numDocumentosAdjuntos)
	{
		this.numDocumentosAdjuntos = numDocumentosAdjuntos;
	}
	
	/**
	 * Retorna la fecha en que se realiz� la solicitud del procedimiento
	 * @return String
	 */
	public String getFechaSolicitud()
	{
		return fechaSolicitud;
	}

	/**
	 * Asigna la fecha en que se realiz� la solicitud del procedimiento
	 * @param fechaSolicitud The fechaSolicitud to set
	 */
	public void setFechaSolicitud(String fechaSolicitud)
	{
		this.fechaSolicitud = fechaSolicitud;
	}

	/**
	 * Retorna los resultados ingresados anteriormente.
	 * @return String
	 */
	public String getResultadosAnteriores()
	{
		return resultadosAnteriores;
	}

	/**
	 * Asigna los resultados ingresados anteriormente.
	 * @param resultadosAnteriores The resultadosAnteriores to set
	 */
	public void setResultadosAnteriores(String resultadosAnteriores)
	{
		this.resultadosAnteriores = resultadosAnteriores;
	}

	/**
	 * Retorna las observaciones ingresadas anteriormente
	 * @return String
	 */
	public String getObservacionesAnteriores()
	{
		return observacionesAnteriores;
	}

	/**
	 * Asigna las observaciones ingresadas anteriormente
	 * @param observacionesAnteriores The observacionesAnteriores to set
	 */
	public void setObservacionesAnteriores(String observacionesAnteriores)
	{
		this.observacionesAnteriores = observacionesAnteriores;
	}

	/**
	 * Retorna los comentarios historia cl�nica ingresados anteriormente
	 * @return String
	 */
	public String getComentariosHistoriaClinicaAnteriores()
	{
		return comentariosHistoriaClinicaAnteriores;
	}

	/**
	 * Asigna los comentarios historia cl�nica ingresados anteriormente
	 * @param comentariosHistoriaClinicaAnteriores The comentariosHistoriaClinicaAnteriores to set
	 */
	public void setComentariosHistoriaClinicaAnteriores(String comentariosHistoriaClinicaAnteriores)
	{
		this.comentariosHistoriaClinicaAnteriores =
			comentariosHistoriaClinicaAnteriores;
	}

	/**
	 * Retorna el numero de autorizaci�n
	 * @return
	 */
	/*
	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}
	*/
	/**
	 * Asigna el numero de autorizaci�n
	 * @param string
	 */
	/*
	public void setNumeroAutorizacion(String string) {
		numeroAutorizacion = string;
	}
	*/
	/**
	 * Retorna la respuesta de otros procedimientos para centros de costo externos
	 * @return
	 */
	public String getRespuestaOtros() {
		return respuestaOtros;
	}

	/**
	 * Asigna la respuesta de otros procedimientos para centros de costo externos
	 * @param string
	 */
	public void setRespuestaOtros(String string) {
		respuestaOtros = string;
	}

	/**
	 * Retorna las observaciones de respuesta de la interconsulta para otros
	 * previamente ingresada
	 * @return String
	 */
	public String getRespuestaOtrosAnterior()
	{
		return respuestaOtrosAnterior;
	}

	/**
	 * Asigna las observaciones de respuesta de la interconsulta para otros
	 * previamente ingresada
	 * @param respuestaOtrosAnterior The respuestaOtrosAnterior to set
	 */
	public void setRespuestaOtrosAnterior(String respuestaOtrosAnterior)
	{
		this.respuestaOtrosAnterior = respuestaOtrosAnterior;
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
	 * @return Returns the horaSolicitud.
	 */
	public String getHoraSolicitud() {
		return horaSolicitud;
	}
	/**
	 * @param horaSolicitud The horaSolicitud to set.
	 */
	public void setHoraSolicitud(String horaSolicitud) {
		this.horaSolicitud = horaSolicitud;
	}

	/**
	 * @return Retorna finalizar.
	 */
	public boolean getFinalizar()
	{
		return finalizar;
	}

	/**
	 * @param finalizar Asigna finalizar.
	 */
	public void setFinalizar(boolean finalizar)
	{
		this.finalizar = finalizar;
	}

	/**
	 * @return Retorna multiple.
	 */
	public boolean getMultiple()
	{
		return multiple;
	}

	/**
	 * @param multiple Asigna multiple.
	 */
	public void setMultiple(boolean multiple)
	{
		this.multiple = multiple;
	}

	public String getNombreServicio() {
		return nombreServicio;
	}

	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
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
	 * @return the indicadorDummy
	 */
	public String getIndicadorDummy() {
		return indicadorDummy;
	}

	/**
	 * @param indicadorDummy the indicadorDummy to set
	 */
	public void setIndicadorDummy(String indicadorDummy) {
		this.indicadorDummy = indicadorDummy;
	}

	/**
	 * @return the ocultarInfoPaciente
	 */
	public boolean isOcultarInfoPaciente() {
		return ocultarInfoPaciente;
	}

	/**
	 * @param ocultarInfoPaciente the ocultarInfoPaciente to set
	 */
	public void setOcultarInfoPaciente(boolean ocultarInfoPaciente) {
		this.ocultarInfoPaciente = ocultarInfoPaciente;
	}

	/**
	 * @return the codigosRespuestas
	 */
	public ArrayList<String> getCodigosRespuestas() {
		return codigosRespuestas;
	}

	/**
	 * @param codigosRespuestas the codigosRespuestas to set
	 */
	public void setCodigosRespuestas(ArrayList<String> codigosRespuestas) {
		this.codigosRespuestas = codigosRespuestas;
	}

	/**
	 * @return the codigoRespuesta
	 */
	public String getCodigoRespuesta() {
		return codigoRespuesta;
	}

	/**
	 * @param codigoRespuesta the codigoRespuesta to set
	 */
	public void setCodigoRespuesta(String codigoRespuesta) {
		this.codigoRespuesta = codigoRespuesta;
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
	 * @return the datosMedicoResponde
	 */
	public String getDatosMedicoResponde() {
		return datosMedicoResponde;
	}

	/**
	 * @param datosMedicoResponde the datosMedicoResponde to set
	 */
	public void setDatosMedicoResponde(String datosMedicoResponde) {
		this.datosMedicoResponde = datosMedicoResponde;
	}

	/**
	 * @return the codigoMedicoResponde
	 */
	public int getCodigoMedicoResponde() {
		return codigoMedicoResponde;
	}

	/**
	 * @param codigoMedicoResponde the codigoMedicoResponde to set
	 */
	public void setCodigoMedicoResponde(int codigoMedicoResponde) {
		this.codigoMedicoResponde = codigoMedicoResponde;
	}

	public boolean isConsultarIterpretacion() {
		return consultarIterpretacion;
	}

	public void setConsultarIterpretacion(boolean consultarIterpretacion) {
		this.consultarIterpretacion = consultarIterpretacion;
	}

	
}
