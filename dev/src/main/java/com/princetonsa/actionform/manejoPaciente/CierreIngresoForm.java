package com.princetonsa.actionform.manejoPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;

import util.ConstantesBD;
import util.ResultadoBoolean;

/**
* Mauricio Jaramillo
* @author axioma
* Fecha Mayo de 2008
*/

public class CierreIngresoForm extends ValidatorForm
{

	private String estado;
	
	/**
     * Mensaje que informa sobre la generacion de la aplicacion de pagos facturas varias
     */
    private ResultadoBoolean mensaje = new ResultadoBoolean(false);
    
    /**
     * Codigo del Ingreso
     */
    private int idIngreso;
    
    /**
     * Fecha en la que se realizo el ingreso
     */
    private String fechaIngreso;
    
    /**
     * Hora en la que se realizo el ingreso
     */
    private String horaIngreso;
    
    /**
     * Variable que maneja el motivo del cierre del ingreso
     */
    private String motivoCierre;
    
    /**
     * Carga los datos del select de motivosCierre
     */
    private ArrayList<HashMap<String, Object>> tiposMotivoCierre;
    
    /**
     * Carga las cuentas del ingreso del paciente
     */
    private HashMap cuentasIngreso;
    
    /**
     * Nombre del convenio
     */
    private String convenio;
    
    /**
     * Descripcion Motivo Ingreso
     */
	private String desIngreso;
	
	/**
	 * Maneja si hay errores en el validate
	 */
	private boolean errores;
    
	/**
     * Metodo que inicializa todas las variables.
     */
    public void reset()
    {
    	this.idIngreso = ConstantesBD.codigoNuncaValido;
    	this.fechaIngreso = "";
    	this.horaIngreso = "";
    	this.motivoCierre = "";
    	this.convenio = "";
    	this.desIngreso = "";
    	this.errores = false;
    	this.tiposMotivoCierre = new ArrayList<HashMap<String,Object>>();
    	this.cuentasIngreso = new HashMap();
    	this.cuentasIngreso.put("numRegistros", "0");
    }
	
    /**
     * Funcion Validate
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		if(this.estado.equals("cerrar"))
		{
			if(this.motivoCierre.trim().equals("") || this.motivoCierre.trim().equals("null"))
			{
				errores.add("codigo", new ActionMessage("errors.required","El Motivo de Cierre "));
				this.errores = true;
			}
		}
		
		return errores;
	}

    /**
     * @return
     */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the mensaje
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @return the convenio
	 */
	public String getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(String convenio) {
		this.convenio = convenio;
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
	 * @return the motivoCierre
	 */
	public String getMotivoCierre() {
		return motivoCierre;
	}

	/**
	 * @param motivoCierre the motivoCierre to set
	 */
	public void setMotivoCierre(String motivoCierre) {
		this.motivoCierre = motivoCierre;
	}

	/**
	 * @return the tiposMotivoCierre
	 */
	public ArrayList<HashMap<String, Object>> getTiposMotivoCierre() {
		return tiposMotivoCierre;
	}

	/**
	 * @param tiposMotivoCierre the tiposMotivoCierre to set
	 */
	public void setTiposMotivoCierre(
			ArrayList<HashMap<String, Object>> tiposMotivoCierre) {
		this.tiposMotivoCierre = tiposMotivoCierre;
	}

	/**
	 * @return the idIngreso
	 */
	public int getIdIngreso() {
		return idIngreso;
	}

	/**
	 * @param idIngreso the idIngreso to set
	 */
	public void setIdIngreso(int idIngreso) {
		this.idIngreso = idIngreso;
	}

	/**
	 * @return the desIngreso
	 */
	public String getDesIngreso() {
		return desIngreso;
	}

	/**
	 * @param desIngreso the desIngreso to set
	 */
	public void setDesIngreso(String desIngreso) {
		this.desIngreso = desIngreso;
	}

	/**
	 * @return the errores
	 */
	public boolean isErrores() {
		return errores;
	}

	/**
	 * @param errores the errores to set
	 */
	public void setErrores(boolean errores) {
		this.errores = errores;
	}

	/**
	 * @return the cuentasIngreso
	 */
	public HashMap getCuentasIngreso() {
		return cuentasIngreso;
	}

	/**
	 * @param cuentasIngreso the cuentasIngreso to set
	 */
	public void setCuentasIngreso(HashMap cuentasIngreso) {
		this.cuentasIngreso = cuentasIngreso;
	}
	
	/**
	 * @param key
	 * @return
	 */	
	public Object getCuentasIngreso(String key) 
	{
		return cuentasIngreso.get(key);
	}
	
	/**
	 * @param key
	 * @param value
	 */	
	public void setCuentasIngreso(String key, Object value) 
	{
		this.cuentasIngreso.put(key, value);
	}
	
}