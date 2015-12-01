package com.princetonsa.actionform;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

/**
 * 
 *
 * @version 1.0, Mar 25, 2003
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>,
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 */
public class ModificarAdmisionHospitalariaForm extends ValidatorForm
{

	private String codigoCompuestoOrigen = "";
	private int origen = -1;
	private String nombreOrigen = "";
	private String codigoCompuestoDiagnostico = "";
	private String diagnostico = "";
	private String nombreDiagnostico = "";
	private String codigoCompuestoMedico = "";
	private String medico = "";
	private String nombreMedico = "";
	private int cama = -1;
	private String ccostoCama = "";
	private String nombreCama = "";
	private String estadoCama = "";
	private String tipoUsuarioCama = "";
	private String descripcionCama = "";
	private String codigoCompuestoCausaExterna = "";
	private int causaExterna = -1;
	private String nombreCausaExterna = "";
	private String numeroAutorizacion = "";	
	private String tipoRegimen = "";	
	private String hora = "";
	private String fecha = "";
	private String idCuenta = "";
	private int codigoAdmisionHospitalaria = 0;
	private String accion = "";
		
	/**
	 * Captura el valor del criterio que definió el usuario para buscar el
	 * diagnóstico. Código y nombre
	 */		 
	private String criterioBusquedaDiagnostico="";
	
	
	
	/**
	 * Returns the codigoCompuestoDiagnostico.
	 * @return String
	 */
	public String getCodigoCompuestoDiagnostico() 
	{
		return codigoCompuestoDiagnostico;
	}
	
	/**
	 * Sets the codigoCompuestoDiagnostico.
	 * @param codigoCompuestoDiagnostico The codigoCompuestoDiagnostico to set
	 */
	public void setCodigoCompuestoDiagnostico(String codigoCompuestoDiagnostico) 
	{

		this.codigoCompuestoDiagnostico = codigoCompuestoDiagnostico;
			
		String[] codCompuesto = codigoCompuestoDiagnostico.split("-");
			
		if( codCompuesto.length == 3 )
		{
			this.diagnostico = codCompuesto[0]+"-"+codCompuesto[1];	
			this.nombreDiagnostico = codCompuesto[2];
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
		String[] origenCod = origen.split("-");
			
		
		if( origenCod.length == 2 )
		{
			this.origen = Integer.parseInt(origenCod[0]);
			this.nombreOrigen = origenCod[1];
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
			
		
		if( codCompuestoCausaExterna.length == 2 )
		{
			this.causaExterna = Integer.parseInt(codCompuestoCausaExterna[0]);
			this.nombreCausaExterna = codCompuestoCausaExterna[1];
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
		
		if( idMedico.length == 3 )
		{
			this.medico = idMedico[0]+"-"+idMedico[1];
			this.nombreMedico  = idMedico[2];
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
		cama = 0;
		diagnostico = "";
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
	
		// Only need crossfield validations here
		if( accion.equals("modificar") )
		{
			if( this.origen < 0 )
			{
				errors.add("origen", new ActionMessage("errors.seleccion", "origen de la admisión"));
			}
			if( this.causaExterna < 0 ) 
			{	
				errors.add("causaExterna", new ActionMessage("errors.seleccion", "causa externa"));
			}
			if(diagnostico==null||diagnostico.equals("-1")|| diagnostico.equals(""))
			{
				errors.add("diagnostico", new ActionMessage("errors.seleccion", "diagnóstico"));
			}
		}
		else
		if (this.accion.equals("finalizar") || this.accion.equals("mostrarDatosAdmisionHospitalaria") )
		{
			if( this.origen < 0 )
			{
				errors.add("origen", new ActionMessage("errors.seleccion", "origen de la admisión"));
			}
			if( this.medico.equals("-1") )
			{
				errors.add("medico", new ActionMessage("errors.seleccion", "médico"));
			}
			if( this.causaExterna < 0 ) 
			{
				errors.add("causaExterna", new ActionMessage("errors.seleccion", "causa externa"));
			}
			if( this.cama < 0 )
			{
				errors.add("cama", new ActionMessage("errors.seleccion", "cama"));
			}
			if(diagnostico==null||diagnostico.equals("-1")|| diagnostico.equals(""))
			{
				errors.add("diagnostico", new ActionMessage("errors.seleccion", "diagnóstico"));
			}
			if (fecha==null||fecha.equals(""))
			{
				errors.add("fecha", new ActionMessage("errors.formatoFechaInvalido", "de admisión"));
			}
			else
			{
				String fechaTemp[]=fecha.split("-");
				if (fechaTemp==null||fechaTemp.length!=3)
				{
					errors.add("fecha", new ActionMessage("errors.formatoFechaInvalido", "de admisión"));
				}
			}
				
			if (hora==null||hora.equals(""))
			{
				errors.add("hora", new ActionMessage("errors.formatoHoraInvalido", "de admisión"));
			}
			else
			{
				//Si no pasa nada de lo anterior reviso si el formato de la hora está correcto
				String horaTemp[]=hora.split(":");
				if (horaTemp==null||horaTemp.length!=2)
				{
					errors.add("hora", new ActionMessage("errors.formatoHoraInvalido", "de admisión"));
				}
				else
				{
					try
					{
						int horaEntera=Integer.parseInt(horaTemp[0]);
						int minutoEntero=Integer.parseInt(horaTemp[1]);
						if (horaEntera<0||horaEntera>23)
						{
							errors.add("hora", new ActionMessage("errors.formatoHoraInvalido", "de admisión"));
						}
						if (minutoEntero<0||minutoEntero>59)
						{
							errors.add("hora", new ActionMessage("errors.formatoHoraInvalido", "de admisión"));
						}
					}
					catch (NumberFormatException ne)
					{
						errors.add("hora", new ActionMessage("errors.formatoHoraInvalido", "de admisión"));
					}
	
				}
			}
		}
		else 
		if (this.accion.equals("volverPaginaPrincipal")&& (diagnostico==null || diagnostico.equals("-1")  || diagnostico.equals("")) )
		{
			errors.add("diagnostico", new ActionMessage("errors.seleccion", "diagnóstico"));
		}
		else 
		if (this.accion.equals("buscarTexto")&&( this.criterioBusquedaDiagnostico==null|| this.criterioBusquedaDiagnostico.length()<3) )
		{
			errors.add("criterioDiagnostico", new ActionMessage("errors.criterioBusquedaNombre"));
		}	
		return errors;
	}

}
