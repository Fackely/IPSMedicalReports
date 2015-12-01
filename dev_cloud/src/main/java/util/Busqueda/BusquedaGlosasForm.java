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
import util.UtilidadTexto;


public class BusquedaGlosasForm extends ValidatorForm
{
	/*
	 * Variable para el manejo de loggs
	 */
	private Logger logger= Logger.getLogger(BusquedaGlosasForm.class);
	
	/**
	 * Variable para el manejo del estado
	 */
	private String estado;
	
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	/**
	 * Variable para la fecha de registro inicial
	 */
	private String fechaRegIni;
	
	/**
	 * Variable para la fecha de registro final
	 */
	private String fechaRegFin;
	
	/**
	 * Variable para almacenar el numero de la glosa
	 */
	private String glosa;
	
	/**
	 * Variable para almacenar el numero de la glosa por entidad
	 */
	private String glosaEntidad;
	
	
	/**
	 * Variable para almacenar la fecha de radicacion
	 */
	private String fechaRadicacion;
	
	/**
	 * Variable para almacenar el codigo del convenio
	 */
	private String convenio;
	
	/**
	 * Variable para almacenar el codigo del convenio
	 */
	private String indicativoglosa;
	
	/**
	 * Variable para almacenar las glosas de la consulta
	 */
	private HashMap glosasMap=new HashMap();
	
	/**
	 * String Patron Ordenar 
	 * **/
	private String patronOrdenar;
	
	/**
	 * String Ultimo Patron de Ordenamiento
	 * */
	private String ultimoPatron;
	
	/**
	 * Variable para la glosa seleccionada
	 */
	private String codigoGlosaInsertada;
	
	/**
	 * Variable para el manejo del estado de la GLosa
	 */
	private String estadoGlosa;
	
	private String nombreFormaRemite;

	
	private int numRegConvenios;
	
	private String conveniosSplit;
	
	/**
	 * 
	 */
	private String consecutivoFactura;
	
	/**
	 * Resetea los valores de la forma cuando
	 * se ingresa por la funcionalidad por Area
	 * @param codigoInstitucion
	 */
	public void reset(int codigoInstitucion)
	{
		this.estado="";
		this.patronOrdenar="";
		this.ultimoPatron="";
		this.glosasMap = new HashMap();
		this.glosasMap.put("numRegistros", "0");
		this.indicativoglosa="";
	}

	public int getNumRegConvenios() {
		return numRegConvenios;
	}


	public void setNumRegConvenios(int numRegConvenios) {
		this.numRegConvenios = numRegConvenios;
	}


	public String getNombreFormaRemite() {
		return nombreFormaRemite;
	}

	public void setNombreFormaRemite(String nombreFormaRemite) {
		this.nombreFormaRemite = nombreFormaRemite;
	}

	/**
	 * Resetea los parametros de busqueda
	 * @param codigoInstitucion
	 */
	public void resetParametros(int codigoInstitucion)
	{
		this.fechaRegIni="";
		this.consecutivoFactura="";
		this.fechaRegFin="";
		this.glosa="";
		this.glosaEntidad="";
		this.convenio="";
		this.fechaRadicacion="";
		this.nombreFormaRemite="";
		this.indicativoglosa="";
		this.numRegConvenios=0;
		this.conveniosSplit="";
	}

	public String getConveniosSplit() {
		return conveniosSplit;
	}

	public void setConveniosSplit(String conveniosSplit) {
		this.conveniosSplit = conveniosSplit;
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
	 * @return the fechaRegIni
	 */
	public String getFechaRegIni() {
		return fechaRegIni;
	}

	/**
	 * @param fechaRegIni the fechaRegIni to set
	 */
	public void setFechaRegIni(String fechaRegIni) {
		this.fechaRegIni = fechaRegIni;
	}

	/**
	 * @return the fechaRegFin
	 */
	public String getFechaRegFin() {
		return fechaRegFin;
	}

	/**
	 * @param fechaRegFin the fechaRegFin to set
	 */
	public void setFechaRegFin(String fechaRegFin) {
		this.fechaRegFin = fechaRegFin;
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
	 * @return the glosasMap
	 */
	public HashMap getGlosasMap() {
		return glosasMap;
	}

	/**
	 * @param glosasMap the glosasMap to set
	 */
	public void setGlosasMap(HashMap glosasMap) {
		this.glosasMap = glosasMap;
	}
	
	public Object getGlosasMap(String key) {
		return glosasMap.get(key);
	}

	public void setGlosasMap(String key, Object value) {
		this.glosasMap.put(key, value);
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
	 * @return the codigoGlosaInsertada
	 */
	public String getCodigoGlosaInsertada() {
		return codigoGlosaInsertada;
	}

	/**
	 * @param codigoGlosaInsertada the codigoGlosaInsertada to set
	 */
	public void setCodigoGlosaInsertada(String codigoGlosaInsertada) {
		this.codigoGlosaInsertada = codigoGlosaInsertada;
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
	 * Metodo encargado de validar..
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
		ActionErrors errores= new ActionErrors();
        errores=super.validate(mapping,request);
        
        String fechaaux;
        
        if(this.getEstado().equals("consultarGlosas"))
        {
        	if((!this.getFechaRegIni().equals("") && this.getFechaRegFin().equals("")) || (this.getFechaRegIni().equals("") && !this.getFechaRegFin().equals("")))
        		errores.add("descripcion",new ActionMessage("errors.required","La Fecha Inicial y Final "));
        	if((!this.fechaRegIni.equals("") && !this.fechaRegFin.equals("")))
        	{
        		if(!UtilidadFecha.compararFechas(this.fechaRegFin.toString(), "00:00", this.fechaRegIni.toString(), "00:00").isTrue())
					errores.add("descripcion",new ActionMessage("errors.invalid"," Fecha Inicial "+this.fechaRegIni.toString()+" mayor a la Fecha Final "+this.fechaRegFin.toString()+" "));
        		else
				{    					
					if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),"00:00",this.fechaRegIni.toString(), "00:00").isTrue())
						errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual",this.fechaRegIni.toString(),UtilidadFecha.getFechaActual()));					 	
					
					if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),"00:00",this.fechaRegFin.toString(), "00:00").isTrue())
						errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual",this.fechaRegFin.toString(),UtilidadFecha.getFechaActual()));						
					
					fechaaux=UtilidadFecha.incrementarDiasAFecha(this.fechaRegIni.toString(), 90, false);
					
					if(!UtilidadFecha.compararFechas(fechaaux, "00:00", this.fechaRegFin.toString(), "00:00").isTrue())
						errores.add("descripcion",new ActionMessage("errors.invalid","El rango entre Fecha inicial y Fecha final supera los 90 dias por lo tanto el rango elegido"));						
				}
        	}
        	if(!this.fechaRadicacion.equals(""))
        	{
        		if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),"00:00",this.fechaRadicacion.toString(), "00:00").isTrue())
					errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual",this.fechaRadicacion.toString(),UtilidadFecha.getFechaActual()));
        	}
        	if(this.fechaRegFin.equals("") && this.fechaRegIni.equals("") && this.glosa.equals("") && this.glosaEntidad.equals("") && this.fechaRadicacion.equals("") && this.convenio.equals("-1") && UtilidadTexto.isEmpty(this.consecutivoFactura))
        		errores.add("descripcion",new ActionMessage("prompt.generico","Es requerido que por lo menos este parametrizado un criterio para iniciar la Busqueda."));
        	if(!this.glosaEntidad.equals("") && this.convenio.equals("-1"))
        		errores.add("descripcion",new ActionMessage("errors.required","El Convenio"));
        }
        
        
        
        if(!errores.isEmpty())
        {
        	this.glosasMap=new HashMap();
        	this.glosasMap.put("numRegistros", "0");
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