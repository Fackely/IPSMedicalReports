package com.princetonsa.actionform.historiaClinica;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.Utilidades;



/**
 * Clase para el manejo de secciones y subsecciones x almacen
 * Date: 2008-05-09
 * @author lgchavez@princetonsa.com
 */
public class RegistroEventosAdversosForm extends ValidatorForm 
{
	
	/************************************************/
	//atributos para el uso del pager
	
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
	 * Mapa Ingresos. mapa donde se almacena la consulta inicial de la funcionalidad
	 */
	private HashMap ingresosMap;
	
	/**
	 * Centro atencion
	 */
	private String centroAtencion;
	
	/**
	 * Mapa de las areas o centros costo
	 */
	private HashMap centrosCosto;
	
	/**
	 * Mapa donde se almacenan los datos de la filtro
	 */
	private HashMap filtro;
	
	/**
	 * Mapa de motivos de la calificacion
	 */
	private HashMap eventos;
	
	private int ingreso;
	
	
	private int capa;
	
	private int codigo;
	
	private String fechaingreso;
	
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	
	/**
	 * 
	 */
	public void reset()
	{
		this.ingresosMap=new HashMap();
		this.ingresosMap.put("numRegistros", 0);
		this.centrosCosto=new HashMap();
		this.centrosCosto.put("numRegistros", 0);
		this.centroAtencion="";
		this.filtro=new HashMap();
		this.eventos=new HashMap();
		this.eventos.put("numRegistros", 0);
		this.capa=0;
	}
	
	/**
	 *reset filtro 
	 */
	public void reset1()
	{
		this.centrosCosto=new HashMap();
		this.centrosCosto.put("numRegistros", 0);
		this.filtro=new HashMap();
		this.eventos=new HashMap();
		this.eventos.put("numRegistros", 0);
		this.capa=0;
		this.codigo=ConstantesBD.codigoNuncaValido;
		this.ingreso=ConstantesBD.codigoNuncaValido;
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
        ActionErrors errors= new ActionErrors();
        errors=super.validate(mapping,request);
        
        if (this.estado.equals("guardarNuevo"))
	        {
	        //validacion de evento
	        	if(!filtro.containsKey("evento") || (filtro.containsKey("evento") && filtro.get("evento").toString().equals(""+ConstantesBD.codigoNuncaValido+"")))
	        	{
	        		errors.add("Motivo", new ActionMessage("errors.required", "Evento Adverso "));	
	        	}
        	//validaciond e tipo de evento
	        	if(!filtro.containsKey("tipoEvento") || (filtro.containsKey("tipoEvento") && filtro.get("tipoEvento").toString().equals("")))
	        	{
	        		errors.add("Motivo", new ActionMessage("errors.required", "Tipo de Evento Adverso "));	
	        	}
	        //validacion de indice gestionado
	        	if(!filtro.containsKey("gestionado") || (filtro.containsKey("gestionado") && filtro.get("gestionado").toString().equals("")))
	        	{
	        		errors.add("Motivo", new ActionMessage("errors.required", "Indicativo gestionado"));	
	        	}
        	//validacion de fecha
	        	if(!filtro.containsKey("fechar") || (filtro.containsKey("fechar") && filtro.get("fechar").toString().equals("")))
	        	{
	        		errors.add("Motivo", new ActionMessage("errors.required", "Fecha de Registro"));	
	        	}
	        	else{
	        			
	        			if(UtilidadFecha.validarFecha(filtro.get("fechar").toString()))
						{
							if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(filtro.get("fechar").toString()),UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual())))
								errors.add("Fecha de Registro mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","Registro","Actual"));
						}
						else
						{
							if(!UtilidadFecha.validarFecha(filtro.get("fechar").toString()))
								errors.add("Fecha de Registro inválida",new ActionMessage("errors.formatoFechaInvalido"," fecha de registro"));
						}
	        			
	        			if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(this.fechaingreso), UtilidadFecha.conversionFormatoFechaAAp(filtro.get("fechar").toString())))
	        			{
	        				errors.add("Motivo",new ActionMessage("errors.fechaPosteriorIgualActual"," Ingreso "," Registro "));
	        			}
						
				}
	        //validacion de hora
	        	if(!filtro.containsKey("horar") || (filtro.containsKey("horar") && filtro.get("horar").toString().equals("")))
	        	{
	        		errors.add("Motivo", new ActionMessage("errors.required", "Hora de Registro"));	
	        	}
	        	else
	        	{
	        		if(!UtilidadFecha.validacionHora(filtro.get("horar").toString()).puedoSeguir)
	        		{
	        			errors.add("Motivo", new ActionMessage("errors.formatoHoraInvalido", "Hora de Registro"));
	        		}
	        	}
	        	
	        	
	        	if (filtro.get("observaciones").toString().length()>3999)
	        	{
	        		errors.add("Motivo", new ActionMessage("error.historiaClinica.tamanioTextoExcedido", "Observaciones"));
	        	}
	        	
	        	
	    
	        }
        
        
        
        if(this.estado.equals("modificar"))
	        {
	        //validacion de fecha
        	if(!filtro.containsKey("fechar_0") || (filtro.containsKey("fechar_0") && filtro.get("fechar_0").toString().equals("")))
	        	{
	        		errors.add("Motivo", new ActionMessage("errors.required", "Fecha de Registro"));	
	        	}
	        	else{
        			if(UtilidadFecha.validarFecha(filtro.get("fechar_0").toString()))
					{
        				Utilidades.imprimirMapa(this.filtro);
        				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(filtro.get("fechar_0").toString()),UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual())))
							errors.add("Fecha de Registro mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","Registro","Actual"));
					}
					else
					{
						if(!UtilidadFecha.validarFecha(filtro.get("fechar_0").toString()))
							errors.add("Fecha de Registro inválida",new ActionMessage("errors.formatoFechaInvalido","inicial"));
					}
        			
        			if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(this.fechaingreso), UtilidadFecha.conversionFormatoFechaAAp(filtro.get("fechar_0").toString())))
        			{
        				errors.add("Motivo",new ActionMessage("errors.fechaPosteriorIgualActual"," Ingreso "," Registro "));
        			}

					
	        	}
	        	
	        	
	        	//validacion de hora
	        	if(!filtro.containsKey("horar_0") || (filtro.containsKey("horar_0") && filtro.get("horar_0").toString().equals("")))
	        	{
	        		errors.add("Motivo", new ActionMessage("errors.required", "Hora de Registro"));	
	        	}
	        	else
	        	{
	        		if(!UtilidadFecha.validacionHora(filtro.get("horar_0").toString()).puedoSeguir)
	        		{
	        			errors.add("Motivo", new ActionMessage("errors.formatoHoraInvalido", "Hora de Registro"));
	        		}
	        	}
	        	
	        	
	        	Utilidades.imprimirMapa(filtro);
	        	
	        	
	        	
	        	if (filtro.get("observaciones").toString().length()>3999)
	        	{
	        		errors.add("Motivo", new ActionMessage("error.historiaClinica.tamanioTextoExcedido", "Observaciones"));
	        	}
	        	
	        	
	        	
	        	
	        }
        
        
        
        
    	if(!errors.isEmpty())
    		this.estado="mostrarCapa";
        
        return errors;        
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
	 * @return the ingresosMap
	 */
	public HashMap getIngresosMap() {
		return ingresosMap;
	}

	/**
	 * @param ingresosMap the ingresosMap to set
	 */
	public void setIngresosMap(HashMap ingresosMap) {
		this.ingresosMap = ingresosMap;
	}

	/**
	 * @return the centroAtencion
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
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

	/**
	 * @return the eventos
	 */
	public HashMap getEventos() {
		return eventos;
	}

	/**
	 * @param eventos the eventos to set
	 */
	public void setEventos(HashMap eventos) {
		this.eventos = eventos;
	}

	/**
	 * @return the filtro
	 */
	public HashMap getFiltro() {
		return filtro;
	}

	/**
	 * @param filtro the filtro to set
	 */
	public void setFiltro(HashMap filtro) {
		this.filtro = filtro;
	}
	
	/**
	 * @param filtro the filtro to set
	 */
	public void setFiltro(String key, Object o) {
		this.filtro.put(key, o);
	}
	
	/**
	 * @param key
	 * @return
	 */	
	public Object getFiltro(String key) 
	{
		return this.filtro.get(key);
	}
	
	/**
	 * @return the capa
	 */
	public int getCapa() {
		return capa;
	}

	/**
	 * @param capa the capa to set
	 */
	public void setCapa(int capa) {
		this.capa = capa;
	}

	/**
	 * @return the ingreso
	 */
	public int getIngreso() {
		return ingreso;
	}

	/**
	 * @param ingreso the ingreso to set
	 */
	public void setIngreso(int ingreso) {
		this.ingreso = ingreso;
	}

	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the fechaingreso
	 */
	public String getFechaingreso() {
		return fechaingreso;
	}

	/**
	 * @param fechaingreso the fechaingreso to set
	 */
	public void setFechaingreso(String fechaingreso) {
		this.fechaingreso = fechaingreso;
	}

	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	


    
    
}