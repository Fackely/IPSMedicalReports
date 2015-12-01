package util.Busqueda;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ResultadoBoolean;
import util.UtilidadFecha;


public class BusquedaConciliacionesForm extends ValidatorForm
{
	/**
	 * Para manejo de Logs
	 */
	private Logger logger = Logger.getLogger(BusquedaConciliacionesForm.class);
	/**
	 * Variable para el manejo del estado
	 */
	private String estado;
	
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	private String nombreFormaRemite;
	private String convenio;
	private String conciliacion;
	private String fechaInicial;
	private String fechaFinal;
	private String conceptoConciliacion;
	private String nroActa;
	
	/**
	 * Mapa que almacena las Conciliaciones de la busqueda
	 */
	private HashMap mapaConciliaciones= new HashMap();
	
	private String patronOrdenar;
	
	/**
	 * Resetea los valores de la forma
	 * sin los que necesita para la busqueda
	 * @param codigoInstitucion
	 */
	public void reset(int codigoInstitucion)
	{
		this.estado="";		
		this.mapaConciliaciones= new HashMap();
		this.mapaConciliaciones.put("numRegistros", "0");
		this.patronOrdenar="";
	}
	
	/**
	 * Resetea los valores de los parametros de busqueda
	 * @param codigoInstitucion
	 */
	public void resetParametros(int codigoInstitucion)
	{
		this.convenio="";
		this.conciliacion="";
		this.conceptoConciliacion="";
		this.fechaInicial="";
		this.fechaFinal="";
		this.nroActa="";
		this.nombreFormaRemite="";
	}

	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	public Object getMapaConciliaciones(String key) {
		return mapaConciliaciones.get(key);
	}
	
	public void setMapaConciliaciones(String key, Object value){
		this.mapaConciliaciones.put(key, value);
	}
	
	public HashMap getMapaConciliaciones() {
		return mapaConciliaciones;
	}

	public void setMapaConciliaciones(HashMap mapaConciliaciones) {
		this.mapaConciliaciones = mapaConciliaciones;
	}

	public String getNombreFormaRemite() {
		return nombreFormaRemite;
	}

	public void setNombreFormaRemite(String nombreFormaRemite) {
		this.nombreFormaRemite = nombreFormaRemite;
	}

	public String getConvenio() {
		return convenio;
	}

	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}

	public String getConciliacion() {
		return conciliacion;
	}

	public void setConciliacion(String conciliacion) {
		this.conciliacion = conciliacion;
	}

	public String getFechaInicial() {
		return fechaInicial;
	}

	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	public String getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public String getConceptoConciliacion() {
		return conceptoConciliacion;
	}

	public void setConceptoConciliacion(String conceptoConciliacion) {
		this.conceptoConciliacion = conceptoConciliacion;
	}

	public String getNroActa() {
		return nroActa;
	}

	public void setNroActa(String nroActa) {
		this.nroActa = nroActa;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}
	
	/**
	 * Metodo encargado de validar..
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
		ActionErrors errores= new ActionErrors();
        String fechaaux="";        
		
       if(this.getEstado().equals("consultarConciliaciones"))
		{
			if(this.convenio.equals("-1") && this.conciliacion.equals("") && this.fechaInicial.equals("") && this.fechaFinal.equals("") && this.conceptoConciliacion.equals("-1") && this.nroActa.equals(""))
				errores.add("descripcion",new ActionMessage("prompt.generico","Es requerido al menos el ingreso de un criterio "));
		
			
			if((!this.getFechaInicial().equals("") && this.getFechaFinal().equals("")) || (this.getFechaInicial().equals("") && !this.getFechaFinal().equals("")))
        		errores.add("descripcion",new ActionMessage("errors.required","La Fecha Inicial y Final "));
        	if((!this.getFechaInicial().equals("") && !this.getFechaFinal().equals("")))
        	{
        		if(!UtilidadFecha.compararFechas(this.getFechaFinal().toString(), "00:00", this.getFechaInicial().toString(), "00:00").isTrue())
					errores.add("descripcion",new ActionMessage("errors.invalid"," Fecha Conciliacion Inicial "+this.getFechaInicial().toString()+" mayor a la Fecha Conciliacion Final "+this.getFechaFinal().toString()+" "));
        		else
				{    					
					if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),"00:00",this.getFechaInicial().toString(), "00:00").isTrue())
						errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual",this.getFechaInicial().toString(),UtilidadFecha.getFechaActual()));					 	
					
					if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),"00:00",this.getFechaFinal().toString(), "00:00").isTrue())
						errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual",this.getFechaFinal().toString(),UtilidadFecha.getFechaActual()));
					
					fechaaux=UtilidadFecha.incrementarDiasAFecha(this.getFechaInicial().toString(), 180, false);
					
					if(!UtilidadFecha.compararFechas(fechaaux, "00:00", this.getFechaFinal().toString(), "00:00").isTrue())
						errores.add("descripcion",new ActionMessage("errors.invalid","El rango entre Fecha Conciliacion inicial y Fecha Conciliacions final supera los 180 dias por lo tanto el rango elegido"));
				}
        	}		
		}    
		
        return errores;
    }
}