package com.princetonsa.actionform.historiaClinica;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import util.ConstantesBD;
import util.historiaClinica.UtilidadesHistoriaClinica;


/**
 * Clase para el manejo de valoracion pacientes en cuidados especiales
 * Date: 2008-05-29
 * @author garias@princetonsa.com
 */
public class ValoracionPacientesCuidadosEspecialesForm extends ValidatorForm 
{
	/**
	 * Estado de la funcionalidad
	 */
	private String estado;
	
	/**
	 * Indicador de posición mapa
	 */
	private int posMap; 
	
	/**
	 * Mapa con la información de valoracion pacientes en cuidados especiales
	 */
	private HashMap valoracionPacientesCuidadosEspecialesMap;
	
	/**
	 * Mapa con los tipos de monitoreo
	 */
	private HashMap tiposMonitoreoMap;
	
	/**
	 * String Patron Ordenar 
	 * **/
	private String patronOrdenar;
	
	/**
	 * String Ultimo Patron de Ordenamiento
	 * */
	private String ultimoPatron;
	
	/**
	 * centros de costo autorizados para el medico tratante
	 * */
	private HashMap centrosCosto;
	
	/**
	 *
	 */
	public void reset()
	{
		this.posMap=ConstantesBD.codigoNuncaValido;
		this.valoracionPacientesCuidadosEspecialesMap=new HashMap();
		this.valoracionPacientesCuidadosEspecialesMap.put("numRegistros", "0");
		this.tiposMonitoreoMap=new HashMap();
		this.tiposMonitoreoMap.put("numRegistros", "0");
		this.centrosCosto = new HashMap();
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
        ActionErrors errores = new ActionErrors();
        errores = super.validate(mapping,request);
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
	 * @return the posMap
	 */
	public int getPosMap() {
		return posMap;
	}

	/**
	 * @param posMap the posMap to set
	 */
	public void setPosMap(int posMap) {
		this.posMap = posMap;
	}

	/**
	 * @return the tiposMonitoreoMap
	 */
	public HashMap getTiposMonitoreoMap() {
		return tiposMonitoreoMap;
	}

	/**
	 * @param tiposMonitoreoMap the tiposMonitoreoMap to set
	 */
	public void setTiposMonitoreoMap(HashMap tiposMonitoreoMap) {
		this.tiposMonitoreoMap = tiposMonitoreoMap;
	}

	/**
	 * @return the valoracionPacientesCuidadosEspecialesMap
	 */
	public HashMap getValoracionPacientesCuidadosEspecialesMap() {
		return valoracionPacientesCuidadosEspecialesMap;
	}

	/**
	 * @param valoracionPacientesCuidadosEspecialesMap the valoracionPacientesCuidadosEspecialesMap to set
	 */
	public void setValoracionPacientesCuidadosEspecialesMap(HashMap valoracionPacientesCuidadosEspecialesMap) {
		this.valoracionPacientesCuidadosEspecialesMap = valoracionPacientesCuidadosEspecialesMap;
	}
	
	/**
	 * @return the tiposMonitoreoMap
	 */
	public Object getTiposMonitoreoMap(String llave) {
		return tiposMonitoreoMap.get(llave);
	}

	/**
	 * @param tiposMonitoreoMap the tiposMonitoreoMap to set
	 */
	public void setTiposMonitoreoMap(String llave, Object obj) {
		this.tiposMonitoreoMap.put(llave, obj);
	}

	/**
	 * @return the valoracionPacientesCuidadosEspecialesMap
	 */
	public Object getValoracionPacientesCuidadosEspecialesMap(String llave) {
		return valoracionPacientesCuidadosEspecialesMap.get(llave);
	}

	/**
	 * @param valoracionPacientesCuidadosEspecialesMap the valoracionPacientesCuidadosEspecialesMap to set
	 */
	public void setValoracionPacientesCuidadosEspecialesMap(String llave, Object obj) {
		this.valoracionPacientesCuidadosEspecialesMap.put(llave, obj);
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
	 * @return the centrosCosto
	 */
	public HashMap getCentrosCosto() {
		return centrosCosto;
	}

	/**
	 * @param centrosCosto the centrosCosto to set
	 */
	public void setCentrosCosto(HashMap centrosCosto) {
		this.centrosCosto = centrosCosto;
	}

	
	
	
    
}	