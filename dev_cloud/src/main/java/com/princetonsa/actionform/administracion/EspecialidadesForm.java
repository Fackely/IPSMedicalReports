package com.princetonsa.actionform.administracion;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dto.administracion.DtoEspecialidades;
import com.princetonsa.dto.administracion.DtoTiposMoneda;

/**
 * @author Víctor Hugo Gómez L.
 */

public class EspecialidadesForm extends ValidatorForm 
{
	/**
     * Objeto para manejar los logs de esta clase
     */
    private Logger logger = Logger.getLogger(EspecialidadesForm.class);
    
    /**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * Listado Especialidades 
	 */
	private ArrayList<DtoEspecialidades> especialidades = new ArrayList<DtoEspecialidades>(); 
	
	/**
	 * Listado de Centros Costo
	 */
	private ArrayList<HashMap<String, Object>> centrosCosto = new ArrayList<HashMap<String, Object>>(); 
	
	
	/**
	 * Posicion
	 */
	private int posEspecialidad;
	
	/**
	 * pagina
	 */
	private String paginaLinkSiguiente;
	
	/**
	 * Max items
	 */
	private int maxItems;
	
	/**
	 * offset 
	 */
	private int offset;
	
	/**
	 * Busqueda Avanzada
	 */
	private String busquedaAvanzada;
	
	// Atributos Busqueda Avanzada
	private String codigoBus;
	private String descripcionBus;
	private String codigoCentroCostoBus;
	private String mostrarBusCon;
	private String patronOrdenar;
	private String mostrarOrdRes;
	
	/**
	 * Mensaje operacion
	 */
	private boolean tieneError;
	
	/**
     * resetea los atributos del form
     */
	public void reset()
    {
    	this.especialidades = new ArrayList<DtoEspecialidades>();
    	this.centrosCosto = new ArrayList<HashMap<String, Object>>();
    	this.posEspecialidad = ConstantesBD.codigoNuncaValido;
    	this.paginaLinkSiguiente = "";
    	this.maxItems = 0;
    	// Atributo  
    	this.busquedaAvanzada = ConstantesBD.acronimoNo;
    	this.codigoBus = "";
    	this.descripcionBus = "";
    	this.codigoCentroCostoBus = "";
    	this.patronOrdenar="";
    	this.mostrarOrdRes=ConstantesBD.acronimoSi;
    	this.mostrarBusCon = ConstantesBD.acronimoNo;
    	this.tieneError = false;
    	this.offset = 0;
    }

	/**
    * Valida  las propiedades que han sido establecidas para este request HTTP,
    * y retorna un objeto <code>ActionErrors</code> que encapsula los errores de
    * validación encontrados. Si no se encontraron errores de validación,
    * retorna <code>null</code>.
    * @param mapping el mapeado usado para elegir esta instancia
    * @param request el <i>servlet request</i> que está siendo procesado en este momento
    * @return un objeto <code>ActionErrors</code> con los (posibles) errores encontrados al validar este formulario,
    * o <code>null</code> si no se encontraron errores.
    */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
    	ActionErrors errores = new ActionErrors();
		int pagina = 0;
		int confilaaxu=0;
		if(estado.equals("guardar"))
        {
			for(int i=0;i<this.especialidades.size();i++)
	    	{
				if((confilaaxu)==this.getMaxItems())
					confilaaxu=0;
	    		if(this.especialidades.get(i).getEliminar().equals(ConstantesBD.acronimoNo))
	    		{
	        		if( ((i+1)%this.getMaxItems())==0)
	        			pagina = ((i+1)/this.getMaxItems());
	        		else{
	        			pagina = ((i+1)/this.getMaxItems())+1;
	        		}
	    		
	        		if(this.especialidades.get(i).getConsecutivo().equals(""))
	        		{
	        			errores.add("consecutivo", new ActionMessage("errors.required","El Campo Código [Pag - "+pagina+"] [Fila - "+(confilaaxu+1)+"] "));
	        		}else{
	        			
	        			for(int j=0;j<this.getEspecialidades().size();j++)
	        			{
	        				if(j!=i)
	        					if(this.getEspecialidades().get(i).getConsecutivo().equalsIgnoreCase(this.especialidades.get(j).getConsecutivo()) 
	        							&& this.getEspecialidades().get(i).getModificar().equals(ConstantesBD.acronimoSi))
	        						errores.add("consecutivo", new ActionMessage("errors.notEspecific","El Campo Código [Pag - "+pagina+"] [Fila - "+(confilaaxu+1)+"] esta Repetido"));
	        				
	        			}
	        		}
	        		if(this.especialidades.get(i).getConsecutivo().length()>5)
	        			errores.add("consecutivo",new ActionMessage("errors.maxlength","Código [Pag - "+pagina+"] [Fila - "+(confilaaxu+1)+"] ","5"));
	        		if(UtilidadCadena.tieneCaracteresEspecialesGeneral(this.especialidades.get(i).getConsecutivo()))
	        			errores.add("consecutivo",new ActionMessage("errors.caracteresInvalidos","El Campo Código [Pag - "+pagina+"] [Fila - "+(confilaaxu+1)+"] "));
	        		
	        		if(this.getEspecialidades().get(i).getDescripcion().equals(""))
	        		{
	        			errores.add("descripcion", new ActionMessage("errors.required","El Campo Descripción [Pag - "+pagina+"] [Fila - "+(confilaaxu+1)+"] "));
	        		}else{
	        			for(int j=0;j<this.getEspecialidades().size();j++)
	        			{
	        				if(j!=i)
	        					if(this.especialidades.get(i).getDescripcion().equalsIgnoreCase(this.especialidades.get(j).getDescripcion())
	        							&& this.especialidades.get(i).getModificar().equals(ConstantesBD.acronimoSi))
	        						errores.add("consecutivo", new ActionMessage("errors.notEspecific","El Campo Descripción [Pag - "+pagina+"] [Fila - "+(confilaaxu+1)+"] esta Repetido"));
	        			}
	        		}
	        		
	        		if(this.especialidades.get(i).getDescripcion().length()>120)
	        			errores.add("consecutivo",new ActionMessage("errors.maxlength","Código [Pag - "+pagina+"] [Fila - "+(confilaaxu+1)+"] ","120"));
	        		if(UtilidadCadena.tieneCaracteresEspecialesGeneral(this.especialidades.get(i).getDescripcion()))
	        			errores.add("consecutivo",new ActionMessage("errors.caracteresInvalidos","El Campo Descripción [Pag - "+pagina+"] [Fila - "+(confilaaxu+1)+"] "));
	        		if(this.especialidades.get(i).getTipoEspecialidad().equals(ConstantesBD.codigoNuncaValido+""))
	        		{
	        			errores.add("tipoEspecialidad",new ActionMessage("errors.required","El Campo Tipo Especialidad [Pag - "+pagina+"] [Fila - "+(confilaaxu+1)+"] "));
	        		}
	    		}
	    		confilaaxu++;
	    	}
			if(!errores.isEmpty())
				this.setTieneError(true);
        }
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
	 * @return the especialidades
	 */
	public ArrayList<DtoEspecialidades> getEspecialidades() {
		return especialidades;
	}

	/**
	 * @param especialidades the especialidades to set
	 */
	public void setEspecialidades(ArrayList<DtoEspecialidades> especialidades) {
		this.especialidades = especialidades;
	}

	/**
	 * @return the centrosCosto
	 */
	public ArrayList<HashMap<String, Object>> getCentrosCosto() {
		return centrosCosto;
	}

	/**
	 * @param centrosCosto the centrosCosto to set
	 */
	public void setCentrosCosto(ArrayList<HashMap<String, Object>> centrosCosto) {
		this.centrosCosto = centrosCosto;
	}

	/**
	 * @return the posEspecialidad
	 */
	public int getPosEspecialidad() {
		return posEspecialidad;
	}

	/**
	 * @param posEspecialidad the posEspecialidad to set
	 */
	public void setPosEspecialidad(int posEspecialidad) {
		this.posEspecialidad = posEspecialidad;
	}

	/**
	 * @return the paginaLinkSiguiente
	 */
	public String getPaginaLinkSiguiente() {
		return paginaLinkSiguiente;
	}

	/**
	 * @param paginaLinkSiguiente the paginaLinkSiguiente to set
	 */
	public void setPaginaLinkSiguiente(String paginaLinkSiguiente) {
		this.paginaLinkSiguiente = paginaLinkSiguiente;
	}

	/**
	 * @return the maxItems
	 */
	public int getMaxItems() {
		return maxItems;
	}

	/**
	 * @param maxItems the maxItems to set
	 */
	public void setMaxItems(int maxItems) {
		this.maxItems = maxItems;
	}

	/**
	 * @return the busquedaAvanzada
	 */
	public String getBusquedaAvanzada() {
		return busquedaAvanzada;
	}

	/**
	 * @param busquedaAvanzada the busquedaAvanzada to set
	 */
	public void setBusquedaAvanzada(String busquedaAvanzada) {
		this.busquedaAvanzada = busquedaAvanzada;
	}

	/**
	 * @return the codigoBus
	 */
	public String getCodigoBus() {
		return codigoBus;
	}

	/**
	 * @param codigoBus the codigoBus to set
	 */
	public void setCodigoBus(String codigoBus) {
		this.codigoBus = codigoBus;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcionBus() {
		return descripcionBus;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcionBus(String descripcionBus) {
		this.descripcionBus = descripcionBus;
	}

	/**
	 * @return the codigoCentroCosto
	 */
	public String getCodigoCentroCostoBus() {
		return codigoCentroCostoBus;
	}

	/**
	 * @param codigoCentroCosto the codigoCentroCosto to set
	 */
	public void setCodigoCentroCostoBus(String codigoCentroCostoBus) {
		this.codigoCentroCostoBus = codigoCentroCostoBus;
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
	 * @return the mostrarOrdRes
	 */
	public String getMostrarOrdRes() {
		return mostrarOrdRes;
	}

	/**
	 * @param mostrarOrdRes the mostrarOrdRes to set
	 */
	public void setMostrarOrdRes(String mostrarOrdRes) {
		this.mostrarOrdRes = mostrarOrdRes;
	}

	/**
	 * @return the mostrarBusCon
	 */
	public String getMostrarBusCon() {
		return mostrarBusCon;
	}

	/**
	 * @param mostrarBusCon the mostrarBusCon to set
	 */
	public void setMostrarBusCon(String mostrarBusCon) {
		this.mostrarBusCon = mostrarBusCon;
	}

	/**
	 * @return the tieneError
	 */
	public boolean isTieneError() {
		return tieneError;
	}

	/**
	 * @param tieneError the tieneError to set
	 */
	public void setTieneError(boolean tieneError) {
		this.tieneError = tieneError;
	}

	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}

	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}
	
}
