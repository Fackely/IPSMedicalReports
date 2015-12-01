package com.princetonsa.actionform;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.Utilidades;

/**
 * ActionForm, tiene la función de bean dentro de la forma, que contiene todos
 * los datos especificados para una admisión hospitalaria, maneja tanto códigos
 * como nombres. Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1.0, Mar 4, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 */
public class AdmisionesHospitalariasForm extends ValidatorForm
{
	/**
	 * Código del origen, está compuesto por : código "-" nombre.
	 */
	private String codigoCompuestoOrigen = "";
	
	/**
	 * Código númerico del origen en la base de datos
	 */
	private int origen = -1;
	
	/**
	 * Nombre del origen.
	 */
	private String nombreOrigen = "";
	
	/**
	 * Código del diagnóstico, está compuesto por : código(acronimo) "-" tipoCie
	 * "-" nombre
	 */
	private String codigoCompuestoDiagnostico = "";
	
	/**
	 * Código del diagnóstico (código-tipoCie)
	 */
	private String diagnostico = "";
	
	/**
	 * Nombre del diagnóstico
	 */
	private String nombreDiagnostico = "";
	
	/**
	 * Código del médico. está compuesto por : tipoIdMedico "-" idMedico "-"
	 * nombre completo
	 */
	private String codigoCompuestoMedico = "";
	
	/**
	 * Identificación del médico (tipoId-numeroId)
	 */
	private String medico = "";
	
	/**
	 * Nombre completo del médico
	 */
	private String nombreMedico = "";
	
	/**
	 * Código interno (axioma) de la cama
	 */
	private int cama = -1;
	
	/**
	 * Nombre del centro de costo al cual pertenece la cama
	 */
	private String ccostoCama = "";
	
	/**
	 * Código de la cama a nivel de institución
	 */
	private String nombreCama = "";
	
	/**
	 * habitacion a nivel de institucion
	 */
	private String habitacionStr="";
	
	/**
	 * Estado de la cama (Disponible, ocupada, etc)
	 */
	private String estadoCama = "";
	
	/**
	 * Tipo de Usuario de la cama (hombre, mujer, niño, etc)
	 */
	private String tipoUsuarioCama = "";
	
	/**
	 * Descripción mas detallada de la cama, si la hay
	 */
	private String descripcionCama = "";
	
	/**
	 * Código de la causa externa, esta compuesto por : Código "-" nombre 
	 */
	private String codigoCompuestoCausaExterna = "";
	
	/**
	 * Código de la causa externa
	 */
	private int causaExterna = -1;
	
	/**
	 * Nombre de la causa externa
	 */
	private String nombreCausaExterna = "";
	
	/**
	 * Número de autorización de la admisión
	 */
	private String numeroAutorizacion = "";	
	
	/**
	 * Tipo de regimen en el cual está el paciente
	 */
	private String tipoRegimen = "";	
	
	/**
	 * Hora de la admisión.
	 */
	private String hora = "";
	
	/**
	 * Fecha de la admisión
	 */
	private String fecha = "";
	
	/**
	 * Código del Paciente
	 */
	private int codigoPaciente;
	/**
	 * Código de la cuenta del paciente
	 */
	private String idCuenta = "";
	
	/**
	 * Código de la admision hospitalaria del paciente
	 */
	private int codigoAdmisionHospitalaria = 0;
	
	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private String accion = "";
	
	/**
	 * Captura el valor del criterio que definió el usuario para buscar el
	 * diagnóstico. Código y nombre
	 */		 
	private String criterioBusquedaDiagnostico="";

	/**
	 * Tipo de identificacion del paciente al cual se le quiere modificar su
	 * ultima admisión abierta (Este parametro se le pasa a través de una URL al
	 * momento de hacer click en un link a modificar admisión de este paciente)
	 */
	private String tipoId="";

	/**
	 * Número de identificacion del paciente al cual se le quiere modificar su
	 * ultima admisión abierta (Este parametro se le pasa a través de una URL al
	 * momento de hacer click en un link a modificar admisión de este paciente)
	 */
	private String numeroId="";
	
	/**
	 * Número de la admisión que se desea modificar (Este parametro se le pasa
	 * a través de una URL al momento de hacer click en un link a modificar
	 * admisión)
	 */
	private String idBusqueda="";	
	
	/**
	 * Código del centro de costo de la cama
	 */
	private int codigoCentroCostoCama=0;
	
	//****************************************************************
	/**
	 * Objeto usado para almacenar el listado de centros de costos del médico al 
	 * realizar la admisión
	 */
	private Collection centrosCosto;
	/**
	 * Variable para almacenar el centro de costo del médico seleccionado
	 * 
	 */
	private int codigoCentroCosto;
	
	/**
	 * 
	 * Variable usada para saber si la admsion Hospitalaria automática
	 * tiene varios montos de cobro
	 */
	
	private boolean variosMontos;
	
	//****************************************************************
	/**
	 * Returns the codigoCompuestoDiagnostico.
	 * @return String
	 */
	public String getCodigoCompuestoDiagnostico() 
	{
		return codigoCompuestoDiagnostico;
	}
	//***********************VARIABLES INDICADORES*****************************************
	/**
	 * <code>indicador</code> que informa si la cuenta activa del paciente es de adminisiones
	 * 0 => no es de admisiones
	 * 1 => es de admisiones
	 */
	private String indicador;
	/**
	 * Variable que me muestra si existe asocio
	 */
	private boolean existeAsocio;
	
	/**
	 *Usuario que hizo la admisión
	 */
	private String loginUsuario;
	/**
	 * Para paginador
	 */
	private String linkSiguiente;
	
	/**
	 *	 Colección que guarda los datos de la cama actual en la que está el paciente
	 */
	private Collection datosCamaActual;
	
	/**
	 * Habitación de la cama inicial
	 */
	private String habitacionInicial;
	
	/**
	 * Campos para la creación de la cuenta
	 * 
	 */
	private String convenio;
	private String tipoPaciente;
	private String codigoEstadoCuenta;
	private String viaIngreso;
	private String tipoRegimenCuenta;
	private String tipoAfiliado;
	private String estrato;
	private String naturaleza;
	private String codigoMonto;
	private boolean indicativoTransito;
	private String codigoTipoEvento;
	private String codigoArpAfiliado;
	private boolean desplazado;
	private String numeroPoliza;
	private String numeroCarnet;
	private String usuario;
	private int codigoArea;
	private int codigoOrigenAdmision;
	private int codigoIngreso;
	//atributos para datos paciente referido
	private boolean pacienteReferido;
	private String institucionRefiere;
	private String profesionalRefiere;
	private String especialidadRefiere;
	
	
	/**
	 * Variable que indica si debo abrir referencia
	 */
	private boolean deboAbrirReferencia;
	
	/**
	 * Mapa donde se guardan los datos de la cama reservada
	 */
	private HashMap camaReserva = new HashMap();
	
	/**
	 * Variable donde se almacena el código de la cama de la reserva
	 */
	private int codigoCamaReserva;
	
	
	//****************SETTERS GETTERS Y MÉTODOS******************************************************
	
	/**
	 * Sets the codigoCompuestoDiagnostico.
	 * @param codigoCompuestoDiagnostico The codigoCompuestoDiagnostico to set
	 */
	public void setCodigoCompuestoDiagnostico(String codigoCompuestoDiagnostico) 
	{
		this.codigoCompuestoDiagnostico = codigoCompuestoDiagnostico;
			
		String[] codCompuesto = codigoCompuestoDiagnostico.split(ConstantesBD.separadorSplit);
			
		if( codCompuesto.length >= 3 )
		{
			this.diagnostico = codCompuesto[0]+"-"+codCompuesto[1];
			this.nombreDiagnostico = "";
			for(int i=2; i < codCompuesto.length; i++)
				this.nombreDiagnostico += codCompuesto[i];
		}
		else
		{
			this.diagnostico = "-1";
			this.nombreDiagnostico = "Seleccione un diagnóstico válido";
		}
	}
	
	/**
	 * Returns the codigoCompuestoOrigen.
	 * @return String
	 */
	public String getCodigoCompuestoOrigen()
	{
		return codigoCompuestoOrigen;
	}
	
	/**
	 * Sets the codigoCompuestoOrigen.
	 * @param codigoCompuestoOrigen The codigoCompuestoOrigen to set
	 */
	public void setCodigoCompuestoOrigen(String origen) 
	{
		this.codigoCompuestoOrigen = origen;
			
		if (origen!=null&&origen.equals("-1"))
		{
			this.origen=-1;
			this.nombreOrigen="Seleccione un Valor Valido";
		}
		else
		{
			String[] origenCod = origen.split("-");
			if( origenCod.length >= 2 )
			{
				this.origen = Integer.parseInt(origenCod[0]);
				this.nombreOrigen = "";
				for(int i=1; i < origenCod.length; i++)
					this.nombreOrigen += origenCod[i];
			}
		}
	}
		
	
	/**
	 * Returns the codigoCompuestoCausaExterna.
	 * @return String
	 */
	public String getCodigoCompuestoCausaExterna() 
	{
		return codigoCompuestoCausaExterna;
	}
	
	/**
	 * Sets the codigoCompuestoCausaExterna.
	 * @param codigoCompuestoCausaExterna The codigoCompuestoCausaExterna to set
	 */
	public void setCodigoCompuestoCausaExterna(String codigoCompuestoCausaExterna) 
	{
		this.codigoCompuestoCausaExterna = codigoCompuestoCausaExterna;
		String[] codCompuestoCausaExterna = codigoCompuestoCausaExterna.split("-");
			
		
		if( codCompuestoCausaExterna.length >= 2 )
		{

			if (codCompuestoCausaExterna[0].length()>0)
			{
				this.causaExterna = Integer.parseInt(codCompuestoCausaExterna[0]);
				this.nombreCausaExterna = "";
				for(int i=1; i < codCompuestoCausaExterna.length; i++)
					this.nombreCausaExterna += codCompuestoCausaExterna[i];
			}
			else
			{
				this.causaExterna=-1;
				this.nombreCausaExterna = "";
				for(int i=1; i < codCompuestoCausaExterna.length; i++)
					this.nombreCausaExterna += codCompuestoCausaExterna[i];
			}
		}






			

	}	
	
	/**
	 * Returns the accion.
	 * @return String
	 */
	public String getAccion() {
		return accion;
	}
	
	/**
	 * Returns the cama.
	 * @return int
	 */
	public int getCama() {
		return cama;
	}
	
	/**
	 * Returns the causaExterna.
	 * @return int
	 */
	public int getCausaExterna() {
		return causaExterna;
	}
	
	/**
	 * Returns the ccostoCama.
	 * @return String
	 */
	public String getCcostoCama() {
		return ccostoCama;
	}
	
	/**
	 * Returns the codigoAdmisionHospitalaria.
	 * @return int
	 */
	public int getCodigoAdmisionHospitalaria() {
		return codigoAdmisionHospitalaria;
	}
	
	/**
	 * Returns the codigoCompuestoMedico.
	 * @return String
	 */
	public String getCodigoCompuestoMedico() {
		return codigoCompuestoMedico;
	}
	
	/**
	 * Returns the criterioBusquedaDiagnostico.
	 * @return String
	 */
	public String getCriterioBusquedaDiagnostico() {
		return criterioBusquedaDiagnostico;
	}
	
	/**
	 * @return Returns the centrosCosto.
	 */
	public Collection getCentrosCosto() {
		return centrosCosto;
	}
	/**
	 * @param centrosCosto The centrosCosto to set.
	 */
	public void setCentrosCosto(Collection centrosCosto) {
		this.centrosCosto = centrosCosto;
	}
	/**
	 * Returns the descripcionCama.
	 * @return String
	 */
	public String getDescripcionCama() {
		return descripcionCama;
	}
	
	/**
	 * Returns the diagnostico.
	 * @return String
	 */
	public String getDiagnostico() {
		return diagnostico;
	}
	
	/**
	 * Returns the estadoCama.
	 * @return String
	 */
	public String getEstadoCama() {
		return estadoCama;
	}
	
	/**
	 * Returns the fecha.
	 * @return String
	 */
	public String getFecha() {
		return fecha;
	}
	
	/**
	 * Returns the hora.
	 * @return String
	 */
	public String getHora() {
		return hora;
	}
	
	/**
	 * Returns the idCuenta.
	 * @return String
	 */
	public String getIdCuenta() {
		return idCuenta;
	}
	
	/**
	 * Returns the medico.
	 * @return String
	 */
	public String getMedico() {
		return medico;
	}
	
	/**
	 * Returns the nombreCama.
	 * @return String
	 */
	public String getNombreCama() {
		return nombreCama;
	}
	
	/**
	 * Returns the nombreCausaExterna.
	 * @return String
	 */
	public String getNombreCausaExterna() {
		return nombreCausaExterna;
	}
	
	/**
	 * Returns the nombreDiagnostico.
	 * @return String
	 */
	public String getNombreDiagnostico() {
		return nombreDiagnostico;
	}
	
	/**
	 * Returns the nombreMedico.
	 * @return String
	 */
	public String getNombreMedico() {
		return nombreMedico;
	}
	
	/**
	 * Returns the nombreOrigen.
	 * @return String
	 */
	public String getNombreOrigen() {
		return nombreOrigen;
	}
	
	/**
	 * Returns the numeroAutorizacion.
	 * @return String
	 */
	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}
	
	/**
	 * Returns the origen.
	 * @return int
	 */
	public int getOrigen() {
		return origen;
	}
	
	/**
	 * Returns the tipoRegimen.
	 * @return String
	 */
	public String getTipoRegimen() {
		return tipoRegimen;
	}
	
	/**
	 * Returns the tipoUsuarioCama.
	 * @return String
	 */
	public String getTipoUsuarioCama() {
		return tipoUsuarioCama;
	}
	
	/**
	 * Sets the accion.
	 * @param accion The accion to set
	 */
	public void setAccion(String accion) {
		this.accion = accion;
	}
	
	/**
	 * Sets the cama.
	 * @param cama The cama to set
	 */
	public void setCama(int cama) {
		this.cama = cama;
	}
	
	/**
	 * Sets the causaExterna.
	 * @param causaExterna The causaExterna to set
	 */
	public void setCausaExterna(int causaExterna) {
		this.causaExterna = causaExterna;
	}
	
	/**
	 * Sets the ccostoCama.
	 * @param ccostoCama The ccostoCama to set
	 */
	public void setCcostoCama(String ccostoCama) {
		this.ccostoCama = ccostoCama;
	}
	
	/**
	 * Sets the codigoAdmisionHospitalaria.
	 * @param codigoAdmisionHospitalaria The codigoAdmisionHospitalaria to set
	 */
	public void setCodigoAdmisionHospitalaria(int codigoAdmisionHospitalaria) {
		this.codigoAdmisionHospitalaria = codigoAdmisionHospitalaria;
	}
	
	/**
	 * Sets the codigoCompuestoMedico.
	 * @param codigoCompuestoMedico The codigoCompuestoMedico to set
	 */
	public void setCodigoCompuestoMedico(String codigoCompuestoMedico)
	{
		this.codigoCompuestoMedico = codigoCompuestoMedico;
		
		String[] idMedico = codigoCompuestoMedico.split("-");
		
		if( idMedico.length >= 3 )
		{
			this.medico = idMedico[0]+"-"+idMedico[1];
			this.nombreMedico = "";
			for( int i=2; i < idMedico.length; i++ )
				this.nombreMedico  += idMedico[i];
		}
		else
		{
			this.medico = "-1";
			this.nombreMedico = "";
		}
	}
	
	/**
	 * Sets the criterioBusquedaDiagnostico.
	 * @param criterioBusquedaDiagnostico The criterioBusquedaDiagnostico to set
	 */
	public void setCriterioBusquedaDiagnostico(String criterioBusquedaDiagnostico) {
		this.criterioBusquedaDiagnostico = criterioBusquedaDiagnostico;
	}
	
	/**
	 * Sets the descripcionCama.
	 * @param descripcionCama The descripcionCama to set
	 */
	public void setDescripcionCama(String descripcionCama) {
		this.descripcionCama = descripcionCama;
	}
	
	/**
	 * Sets the diagnostico.
	 * @param diagnostico The diagnostico to set
	 */
	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}
	
	/**
	 * Sets the estadoCama.
	 * @param estadoCama The estadoCama to set
	 */
	public void setEstadoCama(String estadoCama) {
		this.estadoCama = estadoCama;
	}
	
	/**
	 * Sets the fecha.
	 * @param fecha The fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
	/**
	 * Sets the hora.
	 * @param hora The hora to set
	 */
	public void setHora(String hora) {
		this.hora = hora;
	}
	
	/**
	 * Sets the idCuenta.
	 * @param idCuenta The idCuenta to set
	 */
	public void setIdCuenta(String idCuenta) {
		this.idCuenta = idCuenta;
	}
	
	/**
	 * Sets the medico.
	 * @param medico The medico to set
	 */
	public void setMedico(String medico) {
		this.medico = medico;
	}
	
	/**
	 * Sets the nombreCama.
	 * @param nombreCama The nombreCama to set
	 */
	public void setNombreCama(String nombreCama) {
		this.nombreCama = nombreCama;
	}
	
	/**
	 * Sets the nombreCausaExterna.
	 * @param nombreCausaExterna The nombreCausaExterna to set
	 */
	public void setNombreCausaExterna(String nombreCausaExterna) {
		this.nombreCausaExterna = nombreCausaExterna;
	}
	
	/**
	 * Sets the nombreDiagnostico.
	 * @param nombreDiagnostico The nombreDiagnostico to set
	 */
	public void setNombreDiagnostico(String nombreDiagnostico) {
		this.nombreDiagnostico = nombreDiagnostico;
	}
	
	/**
	 * Sets the nombreMedico.
	 * @param nombreMedico The nombreMedico to set
	 */
	public void setNombreMedico(String nombreMedico) {
		this.nombreMedico = nombreMedico;
	}
	
	/**
	 * Sets the nombreOrigen.
	 * @param nombreOrigen The nombreOrigen to set
	 */
	public void setNombreOrigen(String nombreOrigen) {
		this.nombreOrigen = nombreOrigen;
	}
	
	/**
	 * Sets the numeroAutorizacion.
	 * @param numeroAutorizacion The numeroAutorizacion to set
	 */
	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}
	
	/**
	 * Sets the origen.
	 * @param origen The origen to set
	 */
	public void setOrigen(int origen) {
		this.origen = origen;
	}
	
	/**
	 * Sets the tipoRegimen.
	 * @param tipoRegimen The tipoRegimen to set
	 */
	public void setTipoRegimen(String tipoRegimen) {
		this.tipoRegimen = tipoRegimen;
	}
	
	/**
	 * Sets the tipoUsuarioCama.
	 * @param tipoUsuarioCama The tipoUsuarioCama to set
	 */
	public void setTipoUsuarioCama(String tipoUsuarioCama) {
		this.tipoUsuarioCama = tipoUsuarioCama;
	}
	
	/**
	 * Reset NO estandar, para limpiar al terminar todo el proceso, NO al
	 * cambiar de página
	 */
	
	public void resetModificables ()
	{
		origen = 0;
		nombreOrigen = "";
		codigoCompuestoOrigen = "";
		codigoCompuestoDiagnostico = "";
		nombreDiagnostico = "";
		diagnostico = "";
		causaExterna = 0;
		nombreCausaExterna = "";
		codigoCompuestoCausaExterna = "";
		numeroAutorizacion = "";
	}
		
	public void reset ()
	{
		origen = 0;
		nombreOrigen = "";
		codigoCompuestoOrigen = "";
		medico = "";
		nombreMedico = "";
		codigoCompuestoMedico = "";
		estadoCama = "";
		tipoUsuarioCama = "";
		descripcionCama = "";
		nombreCama = "";
		habitacionStr="";
		cama = 0;
		diagnostico = "";
		this.codigoCompuestoDiagnostico = "";
		this.nombreDiagnostico = "";
		causaExterna = 0;
		nombreCausaExterna = "";
		codigoCompuestoCausaExterna = "";
		numeroAutorizacion = "";
		tipoRegimen = "";
		hora = "";
		fecha = "";
		criterioBusquedaDiagnostico="";
		accion="";
		nombreCama="";
		estadoCama="";
		tipoUsuarioCama="";
		descripcionCama="";
		ccostoCama="";	
		tipoId="";
		numeroId="";
		idBusqueda="";
		codigoCentroCostoCama=0;
		indicador="";
		codigoPaciente=0;
		this.existeAsocio=false;
		this.loginUsuario="";
		this.linkSiguiente="";
		this.centrosCosto=new ArrayList();
		this.variosMontos=false;
		
		
		//datos de la cuenta
		convenio = "";
		tipoPaciente = "";
		codigoEstadoCuenta = "";
		viaIngreso = "";
		tipoRegimenCuenta = "";
		tipoAfiliado = "";
		estrato = "";
		naturaleza = "";
		codigoMonto = "";
		indicativoTransito = false;
		codigoTipoEvento = "";
		codigoArpAfiliado = "";
		desplazado = false;
		numeroPoliza = "";
		numeroCarnet = "";
		usuario = "";
		codigoArea = 0;
		codigoOrigenAdmision = 0;
		codigoIngreso = 0;
		pacienteReferido = false;
		institucionRefiere = "";
		profesionalRefiere = "";
		especialidadRefiere = ConstantesBD.codigoEspecialidadMedicaNinguna + ConstantesBD.separadorSplit + "";
		
		this.deboAbrirReferencia = false;
		
		this.camaReserva = new HashMap();
		this.codigoCamaReserva = 0;
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
		// Perform validator framework validations
		ActionErrors errors = super.validate(mapping, request);

		return errors;
	}

	/**
	 * Returns the idBusqueda.
	 * @return String
	 */
	public String getIdBusqueda() {
		return idBusqueda;
	}

	/**
	 * Returns the numeroId.
	 * @return String
	 */
	public String getNumeroId() {
		return numeroId;
	}

	/**
	 * Returns the tipoId.
	 * @return String
	 */
	public String getTipoId() {
		return tipoId;
	}

	/**
	 * Sets the idBusqueda.
	 * @param idBusqueda The idBusqueda to set
	 */
	public void setIdBusqueda(String idBusqueda) {
		this.idBusqueda = idBusqueda;
	}

	/**
	 * Sets the numeroId.
	 * @param numeroId The numeroId to set
	 */
	public void setNumeroId(String numeroId) {
		this.numeroId = numeroId;
	}

	/**
	 * Sets the tipoId.
	 * @param tipoId The tipoId to set
	 */
	public void setTipoId(String tipoId) {
		this.tipoId = tipoId;
	}

	/**
	 * @return
	 */
	public int getCodigoCentroCostoCama() {
		return codigoCentroCostoCama;
	}

	/**
	 * @param i
	 */
	public void setCodigoCentroCostoCama(int i) {
		codigoCentroCostoCama = i;
	}

	/**
	 * @return Returns the indicador.
	 */
	public String getIndicador() {
		return indicador;
	}
	/**
	 * @param indicador The indicador to set.
	 */
	public void setIndicador(String indicador) {
		this.indicador = indicador;
	}
	
	/**
	 * @return Returns the codigoPaciente.
	 */
	public int getCodigoPaciente() {
		return codigoPaciente;
	}
	/**
	 * @param codigoPaciente The codigoPaciente to set.
	 */
	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}
	
	/**
	 * @return Returns the existeAsocio.
	 */
	public boolean isExisteAsocio() {
		return existeAsocio;
	}
	/**
	 * @param existeAsocio The existeAsocio to set.
	 */
	public void setExisteAsocio(boolean existeAsocio) {
		this.existeAsocio = existeAsocio;
	}
	/**
	 * @return Returns the loginUsuario.
	 */
	public String getLoginUsuario() {
		return loginUsuario;
	}
	/**
	 * @param loginUsuario The loginUsuario to set.
	 */
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
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
	 * @return Returns the codigoCentroCosto.
	 */
	public int getCodigoCentroCosto() {
		return codigoCentroCosto;
	}
	/**
	 * @param codigoCentroCosto The codigoCentroCosto to set.
	 */
	public void setCodigoCentroCosto(int codigoCentroCosto) {
		this.codigoCentroCosto = codigoCentroCosto;
	}
	
	/**
	 * @return Returns the variosMontos.
	 */
	public boolean isVariosMontos() {
		return variosMontos;
	}
	/**
	 * @param variosMontos The variosMontos to set.
	 */
	public void setVariosMontos(boolean variosMontos) {
		this.variosMontos = variosMontos;
	}
    /**
     * @return Returns the habitacionStr.
     */
    public String getHabitacionStr() {
        return habitacionStr;
    }
    /**
     * @param habitacionStr The habitacionStr to set.
     */
    public void setHabitacionStr(String habitacionStr) {
        this.habitacionStr = habitacionStr;
    }
 
	/**
	 * @return Returns the habitacionInicial.
	 */
	public String getHabitacionInicial()
	{
		return habitacionInicial;
	}
	/**
	 * @param habitacionInicial The habitacionInicial to set.
	 */
	public void setHabitacionInicial(String habitacionInicial)
	{
		this.habitacionInicial = habitacionInicial;
	}
	/**
	 * @return Returns the datosCamaActual.
	 */
	public Collection getDatosCamaActual()
	{
		return datosCamaActual;
	}
	/**
	 * @param datosCamaActual The datosCamaActual to set.
	 */
	public void setDatosCamaActual(Collection datosCamaActual)
	{
		this.datosCamaActual = datosCamaActual;
	}

	public String getCodigoEstadoCuenta() {
		return codigoEstadoCuenta;
	}

	public void setCodigoEstadoCuenta(String codigoEstadoCuenta) {
		this.codigoEstadoCuenta = codigoEstadoCuenta;
	}

	public int getCodigoIngreso() {
		return codigoIngreso;
	}

	public void setCodigoIngreso(int codigoIngreso) {
		this.codigoIngreso = codigoIngreso;
	}

	public String getCodigoMonto() {
		return codigoMonto;
	}

	public void setCodigoMonto(String codigoMonto) {
		this.codigoMonto = codigoMonto;
	}

	public String getConvenio() {
		return convenio;
	}

	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}

	public String getEstrato() {
		return estrato;
	}

	public void setEstrato(String estrato) {
		this.estrato = estrato;
	}

	public String getNaturaleza() {
		return naturaleza;
	}

	public void setNaturaleza(String naturaleza) {
		this.naturaleza = naturaleza;
	}

	public String getNumeroCarnet() {
		return numeroCarnet;
	}

	public void setNumeroCarnet(String numeroCarnet) {
		this.numeroCarnet = numeroCarnet;
	}

	public String getNumeroPoliza() {
		return numeroPoliza;
	}

	public void setNumeroPoliza(String numeroPoliza) {
		this.numeroPoliza = numeroPoliza;
	}

	public String getTipoAfiliado() {
		return tipoAfiliado;
	}

	public void setTipoAfiliado(String tipoAfiliado) {
		this.tipoAfiliado = tipoAfiliado;
	}

	public String getTipoPaciente() {
		return tipoPaciente;
	}

	public void setTipoPaciente(String tipoPaciente) {
		this.tipoPaciente = tipoPaciente;
	}

	public String getTipoRegimenCuenta() {
		return tipoRegimenCuenta;
	}

	public void setTipoRegimenCuenta(String tipoRegimenCuenta) {
		this.tipoRegimenCuenta = tipoRegimenCuenta;
	}

	public String getViaIngreso() {
		return viaIngreso;
	}

	public void setViaIngreso(String viaIngreso) {
		this.viaIngreso = viaIngreso;
	}

	/**
	 * @return Returns the especialidadRefiere.
	 */
	public String getEspecialidadRefiere() {
		return especialidadRefiere;
	}

	/**
	 * @param especialidadRefiere The especialidadRefiere to set.
	 */
	public void setEspecialidadRefiere(String especialidadRefiere) {
		this.especialidadRefiere = especialidadRefiere;
	}

	/**
	 * @return Returns the institucionRefiere.
	 */
	public String getInstitucionRefiere() {
		return institucionRefiere;
	}

	/**
	 * @param institucionRefiere The institucionRefiere to set.
	 */
	public void setInstitucionRefiere(String institucionRefiere) {
		this.institucionRefiere = institucionRefiere;
	}

	/**
	 * @return Returns the pacienteReferido.
	 */
	public boolean isPacienteReferido() {
		return pacienteReferido;
	}

	/**
	 * @param pacienteReferido The pacienteReferido to set.
	 */
	public void setPacienteReferido(boolean pacienteReferido) {
		this.pacienteReferido = pacienteReferido;
	}

	/**
	 * @return Returns the profesionalRefiere.
	 */
	public String getProfesionalRefiere() {
		return profesionalRefiere;
	}

	/**
	 * @param profesionalRefiere The profesionalRefiere to set.
	 */
	public void setProfesionalRefiere(String profesionalRefiere) {
		this.profesionalRefiere = profesionalRefiere;
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
	 * @return Returns the codigoArea.
	 */
	public int getCodigoArea() {
		return codigoArea;
	}

	/**
	 * @param codigoArea The codigoArea to set.
	 */
	public void setCodigoArea(int codigoArea) {
		this.codigoArea = codigoArea;
	}

	/**
	 * @return Returns the indicativoTransito.
	 */
	public boolean isIndicativoTransito() {
		return indicativoTransito;
	}

	/**
	 * @param indicativoTransito The indicativoTransito to set.
	 */
	public void setIndicativoTransito(boolean indicativoTransito) {
		this.indicativoTransito = indicativoTransito;
	}

	/**
	 * @return the codigoArpAfiliado
	 */
	public String getCodigoArpAfiliado() {
		return codigoArpAfiliado;
	}

	/**
	 * @param codigoArpAfiliado the codigoArpAfiliado to set
	 */
	public void setCodigoArpAfiliado(String codigoArpAfiliado) {
		this.codigoArpAfiliado = codigoArpAfiliado;
	}

	/**
	 * @return the codigoTipoEvento
	 */
	public String getCodigoTipoEvento() {
		return codigoTipoEvento;
	}

	/**
	 * @param codigoTipoEvento the codigoTipoEvento to set
	 */
	public void setCodigoTipoEvento(String codigoTipoEvento) {
		this.codigoTipoEvento = codigoTipoEvento;
	}

	/**
	 * @return the desplazado
	 */
	public boolean isDesplazado() {
		return desplazado;
	}

	/**
	 * @param desplazado the desplazado to set
	 */
	public void setDesplazado(boolean desplazado) {
		this.desplazado = desplazado;
	}

	/**
	 * @return the codigoOrigenAdmision
	 */
	public int getCodigoOrigenAdmision() {
		return codigoOrigenAdmision;
	}

	/**
	 * @param codigoOrigenAdmision the codigoOrigenAdmision to set
	 */
	public void setCodigoOrigenAdmision(int codigoOrigenAdmision) {
		this.codigoOrigenAdmision = codigoOrigenAdmision;
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
	 * @return the camaReserva
	 */
	public HashMap getCamaReserva() {
		return camaReserva;
	}

	/**
	 * @param camaReserva the camaReserva to set
	 */
	public void setCamaReserva(HashMap camaReserva) {
		this.camaReserva = camaReserva;
	}
	
	/**
	 * @return Retorna elemento del mapa camaReserva
	 */
	public Object getCamaReserva(String key) {
		return camaReserva.get(key);
	}

	/**
	 * @param Asigna elemento al mapa camaReserva 
	 */
	public void setCamaReserva(String key,Object obj) {
		this.camaReserva.put(key,obj);
	}

	/**
	 * @return the codigoCamaReserva
	 */
	public int getCodigoCamaReserva() {
		return codigoCamaReserva;
	}

	/**
	 * @param codigoCamaReserva the codigoCamaReserva to set
	 */
	public void setCodigoCamaReserva(int codigoCamaReserva) {
		this.codigoCamaReserva = codigoCamaReserva;
	}
}
