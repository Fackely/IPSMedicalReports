package com.princetonsa.actionform.manejoPaciente;
import java.util.HashMap;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ResultadoBoolean;
import util.UtilidadTexto;

import com.princetonsa.action.inventarios.SeccionesAction;

public class MotivoCierreAperturaIngresosForm extends ValidatorForm
{

	Logger logger = Logger.getLogger(SeccionesAction.class);
	
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
	 * estado del formulario
	 */
	private String estado;
	
	/**
	 * 
	 */
	private String codigo;
	/**
	 * 
	 */
	private String descripcion;
	/**
	 * 
	 */
	private String tipo;
	/**
	 * 
	 */
	private String activo="1";
	/**
	 * 
	 */
	private HashMap motivoCierreAperturaIngresosMap=new HashMap();
	
	/**
	 * 
	 */
	private String codigom;
	
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
		
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
        errores=super.validate(mapping,request);
        
        
        
        logger.info("Descripcion>>>>>>>>>"+this.getDescripcion()+"<<<<<<<<<<<<<<<<<<");
        
        
        
        if(this.estado.equals("insertar"))
        {
        	if(UtilidadTexto.isEmpty(this.getCodigo()))
        		errores.add("Código requerido",new ActionMessage("errors.required", "El código del motivo"));
           	
        	
        	if(UtilidadTexto.isEmpty(this.getDescripcion()))
        		errores.add("Descripción requerido",new ActionMessage("errors.required", "La descripción del motivo"));
        		
        	if(!errores.isEmpty())
        		this.setEstado("guardarNuevo");
        }
        if(this.estado.equals("guardarModificacion"))
        {
        	if(UtilidadTexto.isEmpty(this.getCodigo()))
        		errores.add("código requerido",new ActionMessage("errors.required", "El código del motivo"));
        	
           	if(UtilidadTexto.isEmpty(this.getDescripcion()))
        		errores.add("Descripción requerido",new ActionMessage("errors.required", "La descripción del motivo"));
        		
        	if(!errores.isEmpty())
        		this.setEstado("modificarRegistro");
        }
        return errores;        
    }

	/**
	 * 
	 */
	public void reset( int codigoInstitucion)
	{
		
		this.patronOrdenar="";
		this.codigo="";
		this.descripcion="";
		this.tipo="";
		this.activo="1";
		this.motivoCierreAperturaIngresosMap= new HashMap();
		motivoCierreAperturaIngresosMap.put("numRegistros", 0);
		this.codigom="";
		this.mensaje=new ResultadoBoolean(false);
		
		
	}




	
	public String getCodigom() {
		return codigom;
	}

	public void setCodigom(String codigom) {
		this.codigom = codigom;
	}

	public void setmotivoCierreAperturaIngresosMap(String key,Object obj) {
		this.motivoCierreAperturaIngresosMap.put(key,obj);
	}

	
	
	public Object getmotivoCierreAperturaIngresosMap(String key) {
		return motivoCierreAperturaIngresosMap.get(key);
	}
	
	public String getCodigo() {
		return codigo;
	}


	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public HashMap getMotivoCierreAperturaIngresosMap() {
		return motivoCierreAperturaIngresosMap;
	}


	public void setMotivoCierreAperturaIngresosMap(HashMap motivoCierreAperturaIngresos) {
		this.motivoCierreAperturaIngresosMap = motivoCierreAperturaIngresos;
	}


	public String getPatronOrdenar() {
		return patronOrdenar;
	}


	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}




	public String getActivo() {
		return activo;
	}




	public void setActivo(String activo) {
		this.activo = activo;
	}




	public String getTipo() {
		return tipo;
	}




	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	public String getUltimoPatron() {
		return ultimoPatron;
	}

	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}
	
}