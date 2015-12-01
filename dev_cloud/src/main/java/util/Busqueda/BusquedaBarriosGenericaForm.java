/*
 * Jun 15, 2007
 */
package util.Busqueda;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

/**
 * 
 * @author Sebastián Gómez Rivillas
 * 
 * Form que contiene todos los datos específicos para generar la
 * busqueda de barrios genérica
 *
 */
public class BusquedaBarriosGenericaForm extends ValidatorForm 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1259379096124450927L;

	/**
	    * estado de la accion
	    */
	   private String estado;
	   
	   /**
	    * Código de la ciudad
	    */
	   private String codigoCiudad = "";
	   
	   /**
	    * Codigo del departamento
	    */
	   private String codigoDepartamento = "";
	   
	   /**
	    * Código del pais
	    */
	   private String codigoPais = "";
	   
	  
	   
	   /**
	    * Código del barrio
	    */
	   private String criterioBarrio = "";
	   
	   //Atributos para el manejo del resultado********************+
	   private String idCodigoBarrio = "";
	   private String idNombreBarrio = "";
	   private String divBarrio = "";
	   private String idCodigoLocalidad = "";
	   private String idNombreLocalidad = "";
	   private String divLocalidad = "";
	   
	   /**
	    * Lista de los barrios consultados
	    */
	   private ArrayList<HashMap<String,Object>> barrios = new ArrayList<HashMap<String,Object>>();
	   
	   /**
	    * Número de registros encontrados
	    */
	   private int numRegistros = 0;
	   
	   //Atributos para la ordenacion
	   private String indice = "";
	   private String ultimoIndice = "";
	   
	   /**
	    * resetea los valores de la forma
	    */
	   public void reset()
	   {
		   this.estado = "";
		   
		   //Atributos de la busqueda
		   this.codigoCiudad = "";
		   this.codigoDepartamento = "";
		   this.codigoPais = "";
		   this.criterioBarrio = "";
		   
		   //Atributos para el manejo del resultado
		   this.idCodigoBarrio = "";
		   this.idNombreBarrio = "";
		   this.divBarrio = "";
		   this.idCodigoLocalidad = "";
		   this.idNombreLocalidad = "";
		   this.divLocalidad = "";
		   
		   this.barrios = new ArrayList<HashMap<String,Object>>();
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
	 * @return the codigoCiudad
	 */
	public String getCodigoCiudad() {
		return codigoCiudad;
	}

	/**
	 * @param codigoCiudad the codigoCiudad to set
	 */
	public void setCodigoCiudad(String codigoCiudad) {
		this.codigoCiudad = codigoCiudad;
	}

	/**
	 * @return the codigoDepartamento
	 */
	public String getCodigoDepartamento() {
		return codigoDepartamento;
	}

	/**
	 * @param codigoDepartamento the codigoDepartamento to set
	 */
	public void setCodigoDepartamento(String codigoDepartamento) {
		this.codigoDepartamento = codigoDepartamento;
	}

	

	/**
	 * @return the divLocalidad
	 */
	public String getDivLocalidad() {
		return divLocalidad;
	}

	/**
	 * @param divLocalidad the divLocalidad to set
	 */
	public void setDivLocalidad(String divLocalidad) {
		this.divLocalidad = divLocalidad;
	}

	/**
	 * @return the idCodigoLocalidad
	 */
	public String getIdCodigoLocalidad() {
		return idCodigoLocalidad;
	}

	/**
	 * @param idCodigoLocalidad the idCodigoLocalidad to set
	 */
	public void setIdCodigoLocalidad(String idCodigoLocalidad) {
		this.idCodigoLocalidad = idCodigoLocalidad;
	}

	/**
	 * @return the idNombreLocalidad
	 */
	public String getIdNombreLocalidad() {
		return idNombreLocalidad;
	}

	/**
	 * @param idNombreLocalidad the idNombreLocalidad to set
	 */
	public void setIdNombreLocalidad(String idNombreLocalidad) {
		this.idNombreLocalidad = idNombreLocalidad;
	}

	/**
	 * @return the codigoPais
	 */
	public String getCodigoPais() {
		return codigoPais;
	}

	/**
	 * @param codigoPais the codigoPais to set
	 */
	public void setCodigoPais(String codigoPais) {
		this.codigoPais = codigoPais;
	}

	/**
	 * @return the criterioBarrio
	 */
	public String getCriterioBarrio() {
		return criterioBarrio;
	}

	/**
	 * @param criterioBarrio the criterioBarrio to set
	 */
	public void setCriterioBarrio(String criterioBarrio) {
		this.criterioBarrio = criterioBarrio;
	}

	/**
	 * @return the divBarrio
	 */
	public String getDivBarrio() {
		return divBarrio;
	}

	/**
	 * @param divBarrio the divBarrio to set
	 */
	public void setDivBarrio(String divBarrio) {
		this.divBarrio = divBarrio;
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
	 * @return the idCodigoBarrio
	 */
	public String getIdCodigoBarrio() {
		return idCodigoBarrio;
	}

	/**
	 * @param idCodigoBarrio the idCodigoBarrio to set
	 */
	public void setIdCodigoBarrio(String idCodigoBarrio) {
		this.idCodigoBarrio = idCodigoBarrio;
	}

	/**
	 * @return the idNombreBarrio
	 */
	public String getIdNombreBarrio() {
		return idNombreBarrio;
	}

	/**
	 * @param idNombreBarrio the idNombreBarrio to set
	 */
	public void setIdNombreBarrio(String idNombreBarrio) {
		this.idNombreBarrio = idNombreBarrio;
	}

	/**
	 * @return the barrios
	 */
	public ArrayList<HashMap<String, Object>> getBarrios() {
		return barrios;
	}

	/**
	 * @param barrios the barrios to set
	 */
	public void setBarrios(ArrayList<HashMap<String, Object>> barrios) {
		this.barrios = barrios;
	}

	/**
	 * @return the numRegistros
	 */
	public int getNumRegistros() {
		return numRegistros;
	}

	/**
	 * @param numRegistros the numRegistros to set
	 */
	public void setNumRegistros(int numRegistros) {
		this.numRegistros = numRegistros;
	}

	/**
	 * @return the indice
	 */
	public String getIndice() {
		return indice;
	}

	/**
	 * @param indice the indice to set
	 */
	public void setIndice(String indice) {
		this.indice = indice;
	}

	/**
	 * @return the ultimoIndice
	 */
	public String getUltimoIndice() {
		return ultimoIndice;
	}

	/**
	 * @param ultimoIndice the ultimoIndice to set
	 */
	public void setUltimoIndice(String ultimoIndice) {
		this.ultimoIndice = ultimoIndice;
	}

}
