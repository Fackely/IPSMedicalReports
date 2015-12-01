package util.Busqueda;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadTexto;

/**
 * Form que contiene todos los datos específicos para generar la
 * busqueda de rublos presupuestales
 * 
 * Y adicionalmente hace el manejo de reset de la forma y de
 * validación de errores de datos de entrada.
 * @version 1,0. Dic 23 , 2008
 * @author <a href="mailto:aesilva@PrincetonSA.com">Andres Silva M</a>
 */
public class BusquedaRubloPresupuestalForm extends ValidatorForm
{
	/**
	 * codigo del Rublo
	 */
   private String codigoRublo;
   
   
   /**
    * descripcion de la busqueda
    */
   private String descripcionRublo;
   
   /**
    * Anio de Vigencia para el Rublo
    */
   private String anioVigenciaRublo;

   /**
    * estado de la accion
    */
   private String estado;
   
   /**
     * Colección con los datos del listado, ya sea para consulta,
     * como también para búsqueda avanzada (pager)
     */
    private HashMap resultados = new HashMap();
    
    /**
     * columna por la cual se quiere ordenar
     */
    private String columna;
    
    /**
     * ultima columna por la cual se ordeno
     */
    private String ultimaPropiedad;
   
    
    private String idHidden=""; 
    
    private String idDiv="";

	private int pos=0;
    
   /**
    * resetea los valores de la forma
    */
   public void reset()
   {
        this.codigoRublo="";
        this.descripcionRublo="";
        this.anioVigenciaRublo="";
        this.setResultados(new HashMap());
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
        ActionErrors errores= new ActionErrors();
        return errores;
    }

	/**
	 * @return the codigoRublo
	 */
	public String getCodigoRublo() {
		return codigoRublo;
	}
	
	/**
	 * @param codigoRublo the codigoRublo to set
	 */
	public void setCodigoRublo(String codigoRublo) {
		this.codigoRublo = codigoRublo;
	}
	
	/**
	 * @return the descripcionRublo
	 */
	public String getDescripcionRublo() {
		return descripcionRublo;
	}
	
	/**
	 * @param descripcionRublo the descripcionRublo to set
	 */
	public void setDescripcionRublo(String descripcionRublo) {
		this.descripcionRublo = descripcionRublo;
	}
	
	/**
	 * @return the anioVigenciaRublo
	 */
	public String getAnioVigenciaRublo() {
		return anioVigenciaRublo;
	}
	
	/**
	 * @param anioVigenciaRublo the anioVigenciaRublo to set
	 */
	public void setAnioVigenciaRublo(String anioVigenciaRublo) {
		this.anioVigenciaRublo = anioVigenciaRublo;
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
	 * @return the resultados
	 */
	public HashMap getResultados() {
		return resultados;
	}

	/**
	 * @param resultados the resultados to set
	 */
	public void setResultados(HashMap resultados) {
		this.resultados = resultados;
	}
	
	/**
	 * @param resultados the resultados to set
	 */
	public void setResultados(String key, Object values) {
		this.resultados.put(key, values);
	}

	/**
	 * @return the columna
	 */
	public String getColumna() {
		return columna;
	}
	
	/**
	 * @param columna the columna to set
	 */
	public void setColumna(String columna) {
		this.columna = columna;
	}
	
	/**
	 * @return the ultimaPropiedad
	 */
	public String getUltimaPropiedad() {
		return ultimaPropiedad;
	}
	
	/**
	 * @param ultimaPropiedad the ultimaPropiedad to set
	 */
	public void setUltimaPropiedad(String ultimaPropiedad) {
		this.ultimaPropiedad = ultimaPropiedad;
	}

	/**
	 * @return the idHidden
	 */
	public String getIdHidden() {
		return idHidden;
	}

	/**
	 * @param idHidden the idHidden to set
	 */
	public void setIdHidden(String idHidden) {
		this.idHidden = idHidden;
	}

	/**
	 * @return the idDiv
	 */
	public String getIdDiv() {
		return idDiv;
	}

	/**
	 * @param idDiv the idDiv to set
	 */
	public void setIdDiv(String idDiv) {
		this.idDiv = idDiv;
	}
    
	 /**
	 * @return the pos
	 */
	public int getPos() {
		return pos;
	}

	/**
	 * @param pos the pos to set
	 */
	public void setPos(int pos) {
		this.pos = pos;
	}
   
}