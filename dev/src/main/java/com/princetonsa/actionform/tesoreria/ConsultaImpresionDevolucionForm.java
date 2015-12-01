package com.princetonsa.actionform.tesoreria;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import org.apache.log4j.Logger;
import util.UtilidadFecha;
import util.Utilidades;
import com.princetonsa.action.tesoreria.ConsultaImpresionDevolucionAction;

public class ConsultaImpresionDevolucionForm extends ValidatorForm
{
	Logger logger = Logger.getLogger(ConsultaImpresionDevolucionAction.class);
	
	/**
	 * estado del formulario
	 */
	private String estado;
	
	/**
	 * String Patron Ordenar 
	 * **/
	private String patronOrdenar;
	
	/**
	 * String Ultimo Patron de Ordenamiento
	 * */
	private String ultimoPatron;
	
	/**
	 * 
	 */
	private String indexMap;
	
	/**
	 * 
	 */
	private HashMap consultaMap;
	
	/**
	 * 
	 */
	private String devolucionI;
	
	/**
	 * 
	 */
	private String devolucionF;
	
	/**
	 * 
	 */
	private String tipoId;
	
	/**
	 * 
	 */
	private String numeroId;
	
	/**
	 * Codigo del centro de atencion
	 */
	private String centroAtencion;
	
	/**
	 *
	 */
	private HashMap centrosAtencionMap;
	
	/**
	 * 
	 */
	private HashMap devolucionesMap;
	
	/**
	 * 
	 */
	private HashMap detalleDMap;
	
	/**
	 * Codigo de la caja
	 */
	private String caja;
	
	/**
	 *
	 */
	private HashMap cajasMap;
	
	/**
	 * 
	 */
	private String motivo;
	
	/**
	 * 
	 */
	private HashMap motivosMap;
	
	/**
	 * 
	 */
	private String estadoD;
	
	/**
	 * Fecha inicial de la Devolucion
	 */
	private String fechaini;
	
	/**
	 * Fecha final de la Devolucion
	 */
	private String fechafin;
	
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
        ActionErrors errores= new ActionErrors();
        errores=super.validate(mapping,request);
        
        if(this.estado.equals("buscarD"))
        {
        	if(this.getFechaini().equals("")&&!this.getFechafin().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","La Fecha Inicial "));
        	
        	if(this.getFechafin().equals("")&&!this.getFechaini().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","La Fecha Final "));
			
        	if(!this.getFechaini().equals("") && !this.getFechafin().equals(""))
			{
				if(!this.getFechafin().equals("") && !this.getFechaini().equals("")){
					if(!UtilidadFecha.compararFechas(this.getFechafin().toString(), "00:00", this.getFechaini().toString(), "00:00").isTrue())
					{
						errores.add("descripcion",new ActionMessage("errors.invalid"," Fecha Inicial "+this.getFechaini().toString()+" mayor a la Fecha Final "+this.getFechafin().toString()+" "));
					}
					else
					{    					
						if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),"00:00",this.getFechaini().toString(), "00:00").isTrue())
						 	errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual",this.getFechaini().toString(),UtilidadFecha.getFechaActual()));
						
						if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),"00:00",this.getFechafin().toString(), "00:00").isTrue())
							errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual",this.getFechafin().toString(),UtilidadFecha.getFechaActual()));
						
					}
				}
			}
        	
        	if(this.devolucionI.equals("")&&!this.devolucionF.equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","La Devolucion Inicial "));
        	
        	if(this.devolucionF.equals("")&&!this.devolucionI.equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","La Devolucion Final "));
        	
        	if(!this.devolucionI.equals("") && !this.devolucionF.equals(""))
        	{
        		if(Integer.parseInt(this.devolucionF) < Integer.parseInt(this.devolucionI))
            	{
            		errores.add("descripcion",new ActionMessage("errors.invalid","Devolucion Final "));
            	}
        	}
        	else{
        		if((!this.devolucionI.equals("") && this.devolucionF.equals("")))
        		{
        			errores.add("descripcion",new ActionMessage("errors.required","El numero de Devolucion Final "));
        		}
        		else{
        			if((this.devolucionI.equals("") && !this.devolucionF.equals("")))
            		{
            			errores.add("descripcion",new ActionMessage("errors.required","El numero de Devolucion Inicial "));
            		}
        		}
        	}
        	
        	if(!this.tipoId.equals("") && this.numeroId.equals(""))
        	{
        		errores.add("descripcion",new ActionMessage("errors.required","El numero de Identificacion del Beneficiario "));
        	}
        	else{
        		if(this.tipoId.equals("") && !this.numeroId.equals(""))
            	{
        			errores.add("descripcion",new ActionMessage("errors.required","El tipo de Identificacion del Beneficiario "));
            	}
        	}
        	
        	int parametrosBusqueda=0;
        	parametrosBusqueda=(!this.fechaini.trim().equals(""))?parametrosBusqueda+1:parametrosBusqueda;
        	parametrosBusqueda=(!this.devolucionI.trim().equals(""))?parametrosBusqueda+1:parametrosBusqueda;
        	parametrosBusqueda=(!this.motivo.trim().equals("-1"))?parametrosBusqueda+1:parametrosBusqueda;
        	parametrosBusqueda=(!this.centroAtencion.trim().equals("-1"))?parametrosBusqueda+1:parametrosBusqueda;
        	parametrosBusqueda=(!this.estadoD.trim().equals("-1"))?parametrosBusqueda+1:parametrosBusqueda;
        	parametrosBusqueda=(!this.tipoId.trim().equals(""))?parametrosBusqueda+1:parametrosBusqueda;
        	parametrosBusqueda=(!this.caja.trim().equals("-1"))?parametrosBusqueda+1:parametrosBusqueda;
        	if(parametrosBusqueda<2)
        	{
        		errores.add("descripcion",new ActionMessage("error.errorEnBlanco","Es requerido que por lo menos se seleccionen 2 criterios para la busqueda. "));
        		
        	}
        	
        }
        return errores;
    }
        
	
	
	
	public String getFechafin() {
		return fechafin;
	}




	public void setFechafin(String fechafin) {
		this.fechafin = fechafin;
	}




	public String getFechaini() {
		return fechaini;
	}




	public void setFechaini(String fechaini) {
		this.fechaini = fechaini;
	}




	public void reset( int codigoInstitucion, int centroAtencion)
	{
		this.centroAtencion=centroAtencion+"";
		this.centrosAtencionMap= Utilidades.obtenerCentrosAtencion(codigoInstitucion);
		this.devolucionesMap = new HashMap();
		this.devolucionesMap.put("numRegistros", "0");
		this.motivosMap = new HashMap();
		this.motivosMap.put("numeRegistros", "0");
		this.consultaMap = new HashMap();
		this.consultaMap.put("numeRegistros", "0");
		this.cajasMap = new HashMap();
		this.cajasMap.put("numeRegistros", "0");
		this.fechafin="";
		this.fechaini="";
		this.devolucionF="";
		this.devolucionI="";
		this.tipoId="";
		this.numeroId="";
		this.patronOrdenar="";
		this.motivo="";
		this.caja="";
		this.ultimoPatron="";
		this.estadoD="";
	}
	
	
	

	public HashMap getConsultaMap() {
		return consultaMap;
	}

	public void setConsultaMap(HashMap consultaMap) {
		this.consultaMap = consultaMap;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getIndexMap() {
		return indexMap;
	}

	public void setIndexMap(String indexMap) {
		this.indexMap = indexMap;
	}




	public String getDevolucionF() {
		return devolucionF;
	}




	public void setDevolucionF(String devolucionF) {
		this.devolucionF = devolucionF;
	}




	public String getDevolucionI() {
		return devolucionI;
	}




	public void setDevolucionI(String devolucionI) {
		this.devolucionI = devolucionI;
	}




	public String getNumeroId() {
		return numeroId;
	}




	public void setNumeroId(String numeroId) {
		this.numeroId = numeroId;
	}




	public String getTipoId() {
		return tipoId;
	}




	public void setTipoId(String tipoId) {
		this.tipoId = tipoId;
	}




	public String getCentroAtencion() {
		return centroAtencion;
	}




	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}




	public HashMap getCentrosAtencionMap() {
		return centrosAtencionMap;
	}




	public void setCentrosAtencionMap(HashMap centrosAtencionMap) {
		this.centrosAtencionMap = centrosAtencionMap;
	}




	public String getCaja() {
		return caja;
	}




	public void setCaja(String caja) {
		this.caja = caja;
	}




	public HashMap getCajasMap() {
		return cajasMap;
	}




	public void setCajasMap(HashMap cajasMap) {
		this.cajasMap = cajasMap;
	}




	public String getMotivo() {
		return motivo;
	}




	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}




	public HashMap getMotivosMap() {
		return motivosMap;
	}




	public void setMotivosMap(HashMap motivosMap) {
		this.motivosMap = motivosMap;
	}




	public String getEstadoD() {
		return estadoD;
	}




	public void setEstadoD(String estadoD) {
		this.estadoD = estadoD;
	}




	public HashMap getDevolucionesMap() {
		return devolucionesMap;
	}




	public void setDevolucionesMap(HashMap devolucionesMap) {
		this.devolucionesMap = devolucionesMap;
	}
	
	public Object getDevolucionesMap(String key) {
		return devolucionesMap.get(key);
	}


	public void setDevolucionesMap(String key, Object value) {
		this.devolucionesMap.put(key, value);
	}


	public HashMap getDetalleDMap() {
		return detalleDMap;
	}




	public void setDetalleDMap(HashMap detalleDMap) {
		this.detalleDMap = detalleDMap;
	}




	public String getPatronOrdenar() {
		return patronOrdenar;
	}




	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}




	public String getUltimoPatron() {
		return ultimoPatron;
	}




	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}
}