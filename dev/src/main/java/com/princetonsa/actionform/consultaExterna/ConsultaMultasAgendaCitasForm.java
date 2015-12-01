package com.princetonsa.actionform.consultaExterna;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.princetonsa.mundo.PersonaBasica;

import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.Utilidades;



public class ConsultaMultasAgendaCitasForm extends ActionForm {
	

private static Logger logger=Logger.getLogger(ConsultaMultasAgendaCitasForm.class);
	
private String estado;

private String fechainicial;

private String fechafinal;

private String centroAtencion;

private int centroAtencionSel;

private String indice;
private String ultimoIndice;
private String linkSiguiente;


public int getCentroAtencionSel() {
	return centroAtencionSel;
}









public void setCentroAtencionSel(int centroAtencionSel) {
	this.centroAtencionSel = centroAtencionSel;
}

private String unidadAgenda;

private String convenio;

private String estadoCita;

private String profesional;

private HashMap<String, Object> consultaMultasAgendaCitas=new HashMap<String, Object> ();


private HashMap<String, Object> centrosAtencionMap = new HashMap<String, Object>();

private HashMap<String, Object> unidadAgendaMap = new HashMap<String, Object>();



private ArrayList<HashMap<String, Object>> profesionalMap = new ArrayList<HashMap<String,Object>>();

private ArrayList<HashMap<String, Object>> conveniosMap = new ArrayList<HashMap<String,Object>>();

private ArrayList<HashMap<String, Object>> estadoCitaMap = new ArrayList<HashMap<String,Object>>();

private ResultadoBoolean mensaje = new ResultadoBoolean(false);



private String descripcion;

private int bandera;


public void clean(){
	
	consultaMultasAgendaCitas = new HashMap<String, Object>();
	
	unidadAgendaMap = new HashMap<String, Object>();
	
	profesionalMap = new ArrayList<HashMap<String,Object>>();
	
	conveniosMap = new ArrayList<HashMap<String,Object>>();
	
	estadoCitaMap = new ArrayList<HashMap<String,Object>>();
	
	this.centroAtencion="-1";
	
	this.consultaMultasAgendaCitas.put("numRegistros", "0");
	
    this.centroAtencionSel=-1;
	
	this.convenio="";
	
	this.profesional="";
	
	this.estadoCita="";
	
	this.fechafinal="";
	
	this.fechainicial="";
	
	}





public int getBandera() {
	return bandera;
}
public HashMap<String, Object> getUnidadAgendaMap() {
		return unidadAgendaMap;
	}
public void setUnidadAgendaMap(HashMap<String, Object> unidadAgendaMap) {
		this.unidadAgendaMap = unidadAgendaMap;
	}
public void setBandera(int bandera) {
	this.bandera = bandera;
}
public String getDescripcion() {
	return descripcion;
}




public void setDescripcion(String descripcion) {
	this.descripcion = descripcion;
}




	public String getIndice() {
	return indice;
}









public void setIndice(String indice) {
	this.indice = indice;
}









public String getUltimoIndice() {
	return ultimoIndice;
}









public void setUltimoIndice(String ultimoIndice) {
	this.ultimoIndice = ultimoIndice;
}









public String getLinkSiguiente() {
	return linkSiguiente;
}









public void setLinkSiguiente(String linkSiguiente) {
	this.linkSiguiente = linkSiguiente;
}









	public ArrayList<HashMap<String, Object>> getEstadoCitaMap() {
	return estadoCitaMap;
}




public ResultadoBoolean getMensaje() {
		return mensaje;
	}




	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}




public void setEstadoCitaMap(ArrayList<HashMap<String, Object>> estadoCitaMap) {
	this.estadoCitaMap = estadoCitaMap;
}




	public ArrayList<HashMap<String, Object>> getConveniosMap() {
	return conveniosMap;
}



public void setConveniosMap(ArrayList<HashMap<String, Object>> conveniosMap) {
	this.conveniosMap = conveniosMap;
}



	public ArrayList<HashMap<String, Object>> getProfesionalMap() {
	return profesionalMap;
}


public void setProfesionalMap(ArrayList<HashMap<String, Object>> profesionalMap) {
	this.profesionalMap = profesionalMap;
}


	public HashMap<String, Object> getCentrosAtencionMap() {
		return centrosAtencionMap;
	}
	
	
	
	public void setCentrosAtencionMap(HashMap<String, Object> centrosAtencionMap) {
		this.centrosAtencionMap = centrosAtencionMap;
	}
	
	

	public int getCentrosAtencionMap1 ()
	{
		return Utilidades.convertirAEntero(this.centrosAtencionMap.get("numRegistros")+"", true);
	}
	
	public Object getCentrosAtencionMap(String key)
	{
		return centrosAtencionMap.get(key);
	}
	
	
	public void setCentrosAtencionMap(String key, Object value)
	{
		this.centrosAtencionMap.put(key, value);
	}
	
	
	public HashMap<String, Object> getConsultaMultasAgendaCitas() {
		return consultaMultasAgendaCitas;
	}
	
	
	
	public void setConsultaMultasAgendaCitas(
			HashMap<String, Object> consultaMultasAgendaCitas) {
		this.consultaMultasAgendaCitas = consultaMultasAgendaCitas;
	}
	
	
	public Object getConsultaMultasAgendaCitas(String key){
		return consultaMultasAgendaCitas.get(key);
	}
	
	public void setConsultaMultasAgendaCitas(String key, Object value){
		this.consultaMultasAgendaCitas.put(key, value);
	}
	
	
	public int getConsultaMultasAgendaCitas1 ()
	{
		return Utilidades.convertirAEntero(this.consultaMultasAgendaCitas.get("numRegistros")+"", true);
	}
	
	public String getEstado() {
		return estado;
	}
	
	public String getFechainicial() {
		return fechainicial;
	}
	public void setFechainicial(String fechainicial) {
		this.fechainicial = fechainicial;
	}
	public String getFechafinal() {
		return fechafinal;
	}
	public void setFechafinal(String fechafinal) {
		this.fechafinal = fechafinal;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	
	public String getCentroAtencion() {
		return centroAtencion;
	}
	
	
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}
	
	
	public String getUnidadAgenda() {
		return unidadAgenda;
	}
	
	
	public void setUnidadAgenda(String unidadAgenda) {
		this.unidadAgenda = unidadAgenda;
	}
	
	
	public String getConvenio() {
		return convenio;
	}
	
	
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}
	
	
	public String getEstadoCita() {
		return estadoCita;
	}
	
	
	public void setEstadoCita(String estadoCita) {
		this.estadoCita = estadoCita;
	}
	
	
	public String getProfesional() {
		return profesional;
	}
	
	
	public void setProfesional(String profesional) {
		this.profesional = profesional;
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)  
    {
		
		ActionErrors errores= new ActionErrors();
		

	
        
       if(this.estado.equals("buscar"))
        {
    	   
    	   
    	   boolean fechaInicialValida = false;
    	   boolean fechaFinalValida = false;
        	
        	if(this.fechainicial.equals(""))
        	{
        		
        		
        		errores.add("descripcion",new ActionMessage("errors.required","La fecha inicial"));
        		this.estado="rango";	
        	}
        	else if(!UtilidadFecha.validarFecha(this.fechainicial))
        	{
        		errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido","inicial"));
        		this.estado="rango";
        	}
        	else
        	{
        		fechaInicialValida = true;
        	}
        	
        	if(this.fechafinal.equals(""))
        	{
        		errores.add("descripcion",new ActionMessage("errors.required","La fecha final"));
        		this.estado="rango";
        		
        	}
        	else if(!UtilidadFecha.validarFecha(this.fechafinal))
        	{
        		errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido","final"));
        		this.estado="rango";
        	}
        	else
        	{
        		fechaFinalValida = true;
        	}
        	
        	if(fechaFinalValida&&(UtilidadFecha.conversionFormatoFechaABD(this.getFechafinal())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
			{
				errores.add("descripcion", new ActionMessage("errors.notEspecific", " La fecha final "+this.fechafinal+" debes ser menor o igual a la fecha del sistema"));
				this.estado="rango";
			}
        	if(fechaInicialValida&&(UtilidadFecha.conversionFormatoFechaABD(this.getFechainicial())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
			{
				errores.add("descripcion", new ActionMessage("errors.notEspecific", " La fecha inicial "+this.fechainicial+" debes ser menor o igual a la fecha del sistema"));
				this.estado="rango";
			}
        	if(fechaInicialValida&&fechaFinalValida&&!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechainicial, fechafinal))
        	{
                errores.add("La fecha inicial debe ser menor o igual que la fecha final", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "inicial", "final"));
                this.estado="rango";
        	}
        	
        	
          }
  
	    return errores;     
   }
}