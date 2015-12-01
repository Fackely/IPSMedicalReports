package com.princetonsa.actionform.manejoPaciente;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import util.ConstantesBD;
import util.ResultadoBoolean;


/**
 * Clase para el manejo de la parametrizacion 
 * de los motivos de satisfacción e insatisfacción
 * en la atención de pacientes
 * Date: 2008-05-9
 * @author garias@princetonsa.com
 */
public class MotivosSatisfaccionInsatisfaccionForm extends ValidatorForm 
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
	 * Mapa con la información de los motivox
	 */
	private HashMap motivosMap;
	
	/**
	 * String Patron Ordenar 
	 * **/
	private String patronOrdenar;
	
	/**
	 * String Ultimo Patron de Ordenamiento
	 * */
	private String ultimoPatron;
	
	/**
	 * String Link Siguiente
	 * */
	private String linkSiguiente;
	
	/**
	 * Estado de auxiliar
	 */
	private String estadoAux;
	
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	/**
	 * Reset
	 *
	 */
	public void reset()
	{
		posMap=ConstantesBD.codigoNuncaValido;
		HashMap mapa=new HashMap();
		mapa.put("numRegistros", "0");
		patronOrdenar="";
		ultimoPatron="";
		linkSiguiente="";
		estadoAux="";
		mensaje = new ResultadoBoolean(false);
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
	 * @return the motivosMap
	 */
	public HashMap getMotivosMap() {
		return motivosMap;
	}

	/**
	 * @param motivosMap the motivosMap to set
	 */
	public void setMotivosMap(HashMap motivosMap) {
		this.motivosMap = motivosMap;
	}

	/**
	 * @return the motivosMap
	 */
	public Object getMotivosMap(String llave) {
		return motivosMap.get(llave);
	}

	/**
	 * @param motivosMap the motivosMap to set
	 */
	public void setMotivosMap(String llave, Object obj) {
		this.motivosMap.put(llave, obj);
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
	 * @return the linkSiguiente
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente the linkSiguiente to set
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * @return the estadoAux
	 */
	public String getEstadoAux() {
		return estadoAux;
	}

	/**
	 * @param estadoAux the estadoAux to set
	 */
	public void setEstadoAux(String estadoAux) {
		this.estadoAux = estadoAux;
	}

	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}
    
    
}	