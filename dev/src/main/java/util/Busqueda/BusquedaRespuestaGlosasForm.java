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

public class BusquedaRespuestaGlosasForm extends ValidatorForm
{
	/**
	 * Para manejo de Logs
	 */
	private Logger logger = Logger.getLogger(BusquedaRespuestaGlosasForm.class);
	/**
	 * Variable para el manejo del estado
	 */
	private String estado;
	
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	/**
	 * Atributo Patron Ordenar 
	 * **/
	private String patronOrdenar;
	
	/**
	 * Atributo Ultimo Patron de Ordenamiento
	 * */
	private String ultimoPatron;
	
	/**
	 * Variable para buscar por el numero de respuesta
	 */
	private String respuesta;
	
	/**
	 * Variable para buscar por rangos la fecha de respuesta Ini
	 */
	private String fechaRespIni;
	
	/**
	 * Variable para buscar por rangos la fecha de respuesta Fin
	 */
	private String fechaRespFin;
	
	/**
	 * Variable para buscar por glosa sistema
	 */
	private String glosa;
	
	/**
	 * Variable para buscar por glosa entidad
	 */
	private String glosaEntidad;
	
	/**
	 * Variable para buscar por el convenio
	 */
	private String convenio;
	
	/**
	 * Variable para buscar por fecha de radicacion de la glosa
	 */
	private String fechaRadicacion;
	
	/**
	 * Variable para mantener actualizado la respuesta que se selecciono en el pop up
	 */
	private String codigoRespuestaInsertada;
	
	/**
	 * Mapa que almacena las Respuestas de la consulta
	 */
	private HashMap respuestasMap = new HashMap();
	
	/**
	 * Estado de la Glosa para la busqueda
	 */
	private String estadoGlosa;
	
	/**
	 * Estado de la Respuesta para la busqueda
	 */
	private String estadoRespuesta;
	
	/**
	 * Variable para el manejo del indicativo Glosa o Preglosa
	 */
	private String indicativoglosa;
	
	/**
	 * 
	 */
	private String consecutivoFactura;
	
	
	/**
	 * Resetea los valores de la forma
	 * sin los que necesita para la busqueda
	 * @param codigoInstitucion
	 */
	public void reset(int codigoInstitucion)
	{
		this.estado="";
		this.patronOrdenar="";
		this.ultimoPatron="";
		this.respuestasMap = new HashMap();
		this.respuestasMap.put("numRegistros","0");
		this.indicativoglosa="";
	}
	


	/**
	 * Resetea los valores de los parametros de busqueda
	 * @param codigoInstitucion
	 */
	public void resetParametros(int codigoInstitucion)
	{
		logger.info("\n\nENTROOOOOOOO 111111111");
		this.respuesta="";
		this.fechaRespIni="";
		this.fechaRespFin="";
		this.glosa="";
		this.glosaEntidad="";
		this.convenio="";
		this.fechaRadicacion="";
		this.consecutivoFactura="";
	}


	public String getIndicativoglosa() {
		return indicativoglosa;
	}

	public void setIndicativoglosa(String indicativoglosa) {
		this.indicativoglosa = indicativoglosa;
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
	 * @return the respuesta
	 */
	public String getRespuesta() {
		return respuesta;
	}

	/**
	 * @param respuesta the respuesta to set
	 */
	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}
	
	/**
	 * @return the fechaRespIni
	 */
	public String getFechaRespIni() {
		return fechaRespIni;
	}

	/**
	 * @param fechaRespIni the fechaRespIni to set
	 */
	public void setFechaRespIni(String fechaRespIni) {
		this.fechaRespIni = fechaRespIni;
	}

	/**
	 * @return the fechaRespFin
	 */
	public String getFechaRespFin() {
		return fechaRespFin;
	}

	/**
	 * @param fechaRespFin the fechaRespFin to set
	 */
	public void setFechaRespFin(String fechaRespFin) {
		this.fechaRespFin = fechaRespFin;
	}

	/**
	 * @return the glosa
	 */
	public String getGlosa() {
		return glosa;
	}

	/**
	 * @param glosa the glosa to set
	 */
	public void setGlosa(String glosa) {
		this.glosa = glosa;
	}
	
	/**
	 * @return the glosaEntidad
	 */
	public String getGlosaEntidad() {
		return glosaEntidad;
	}

	/**
	 * @param glosaEntidad the glosaEntidad to set
	 */
	public void setGlosaEntidad(String glosaEntidad) {
		this.glosaEntidad = glosaEntidad;
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
	 * @return the fechaRadicacion
	 */
	public String getFechaRadicacion() {
		return fechaRadicacion;
	}

	/**
	 * @param fechaRadicacion the fechaRadicacion to set
	 */
	public void setFechaRadicacion(String fechaRadicacion) {
		this.fechaRadicacion = fechaRadicacion;
	}

	/**
	 * @return the codigoRespuestaInsertada
	 */
	public String getCodigoRespuestaInsertada() {
		return codigoRespuestaInsertada;
	}

	/**
	 * @param codigoRespuestaInsertada the codigoRespuestaInsertada to set
	 */
	public void setCodigoRespuestaInsertada(String codigoRespuestaInsertada) {
		this.codigoRespuestaInsertada = codigoRespuestaInsertada;
	}

	/**
	 * @return the respuestasMap
	 */
	public HashMap getRespuestasMap() {
		return respuestasMap;
	}

	/**
	 * @param respuestasMap the respuestasMap to set
	 */
	public void setRespuestasMap(HashMap respuestasMap) {
		this.respuestasMap = respuestasMap;
	}
	
	public Object getRespuestasMap(String key){
		return this.respuestasMap.get(key);
	}
	
	public void setRespuestasMap(String key, Object value){
		this.respuestasMap.put(key, value);
	}

	/**
	 * @return the estadoGlosa
	 */
	public String getEstadoGlosa() {
		return estadoGlosa;
	}

	/**
	 * @param estadoGlosa the estadoGlosa to set
	 */
	public void setEstadoGlosa(String estadoGlosa) {
		this.estadoGlosa = estadoGlosa;
	}

	/**
	 * @return the estadoRespuesta
	 */
	public String getEstadoRespuesta() {
		return estadoRespuesta;
	}

	/**
	 * @param estadoRespuesta the estadoRespuesta to set
	 */
	public void setEstadoRespuesta(String estadoRespuesta) {
		this.estadoRespuesta = estadoRespuesta;
	}
	
	/**
	 * Metodo encargado de validar..
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
		ActionErrors errores= new ActionErrors();
        errores=super.validate(mapping,request);
        
        String fechaaux;
        
        if(this.getEstado().equals("consultarRespuestaGlosas"))
        {
        	if((!this.getFechaRespIni().equals("") && this.getFechaRespFin().equals("")) || (this.getFechaRespIni().equals("") && !this.getFechaRespFin().equals("")))
        		errores.add("descripcion",new ActionMessage("errors.required","La Fecha Inicial y Final "));
        	if((!this.getFechaRespIni().equals("") && !this.getFechaRespFin().equals("")))
        	{
        		if(!UtilidadFecha.compararFechas(this.getFechaRespFin().toString(), "00:00", this.getFechaRespIni().toString(), "00:00").isTrue())
					errores.add("descripcion",new ActionMessage("errors.invalid"," Fecha Respuesta Inicial "+this.getFechaRespIni().toString()+" mayor a la Fecha Respuesta Final "+this.getFechaRespFin().toString()+" "));
        		else
				{    					
					if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),"00:00",this.getFechaRespIni().toString(), "00:00").isTrue())
						errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual",this.getFechaRespIni().toString(),UtilidadFecha.getFechaActual()));					 	
					
					if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),"00:00",this.getFechaRespFin().toString(), "00:00").isTrue())
						errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual",this.getFechaRespFin().toString(),UtilidadFecha.getFechaActual()));						
					
					fechaaux=UtilidadFecha.incrementarDiasAFecha(this.getFechaRespIni().toString(), 180, false);
					
					if(!UtilidadFecha.compararFechas(fechaaux, "00:00", this.getFechaRespFin().toString(), "00:00").isTrue())
						errores.add("descripcion",new ActionMessage("errors.invalid","El rango entre Fecha Respuesta inicial y Fecha Respuesta final supera los 180 dias por lo tanto el rango elegido"));
				}
        	}
        	if(!this.fechaRadicacion.equals(""))
        	{
        		if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),"00:00",this.fechaRadicacion.toString(), "00:00").isTrue())
					errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual",this.fechaRadicacion.toString(),UtilidadFecha.getFechaActual()));
        	}
        	if(this.getFechaRespFin().equals("") && this.getFechaRespIni().equals("") && this.glosa.equals("") && this.glosaEntidad.equals("") && this.fechaRadicacion.equals("") && this.respuesta.equals("") && this.convenio.equals("-1") && this.consecutivoFactura.equals(""))
        		errores.add("descripcion",new ActionMessage("prompt.generico","Es requerido que por lo menos este parametrizado un criterio para iniciar la Busqueda."));
        	if(!this.glosaEntidad.equals("") && this.convenio.equals("-1"))
        		errores.add("descripcion",new ActionMessage("errors.required","El Convenio"));
        }
        
        if(!errores.isEmpty())
        {
        	this.respuestasMap=new HashMap();
        	this.respuestasMap.put("numRegistros", "0");
        }
        
        
        return errores;
    }



	public String getConsecutivoFactura() {
		return consecutivoFactura;
	}



	public void setConsecutivoFactura(String consecutivoFactura) {
		this.consecutivoFactura = consecutivoFactura;
	}
}