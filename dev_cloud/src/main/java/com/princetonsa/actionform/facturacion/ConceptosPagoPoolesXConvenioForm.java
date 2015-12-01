package com.princetonsa.actionform.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.mundo.UsuarioBasico;

import util.ConstantesBD;
import util.Utilidades;
import util.facturacion.UtilidadesFacturacion;


public class ConceptosPagoPoolesXConvenioForm extends ValidatorForm
{
	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	/**
	 * Para manjar los logger de la clase censocamasform
	 */
	Logger logger = Logger.getLogger(ConceptosPagoPoolesXConvenioForm.class);
	
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	
	
	/*-----------------------------------------------
	 * 				ATRIBUTOS DEL PAGER
	 ------------------------------------------------*/
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
	

	
	/*-----------------------------------------------
	 * 				FIN ATRIBUTOS DEL PAGER
	 ------------------------------------------------*/
	
	/*--------------------------------------------------
	 * 	 ATRIBUTOS DE CONCEPTOS PAGO POOLES X CONVENIO
	 ---------------------------------------------------*/
	 /**
	  *  HashMap que almacena la consulta de los
	  * Conceptos de pago de los pooles X convenio
	  */
	private HashMap conceptosPagoPoolesXConvenioMap;
	
	
	/**
	 *ArrayList Con los valores de los pooles. 
	 */
	private ArrayList<HashMap<String, Object>> poolesList;
	
	
	/**
	 *ArrayList Con los valores de los pooles. 
	 */
	private ArrayList<HashMap<String, Object>> conveniosList;
	
	/**
	 * ArrayList Con los valores de concepto de pagos de pooles.
	 */
	private ArrayList<HashMap<String, Object>> conceptosPagoPoolesList;
		
	/**
	 * ArrayList Con los valores de tipos de servicios.
	 */
	private ArrayList<HashMap<String, Object>> tiposServicioList;
	
	/**
	 * entero que almacena el codigo de la cuenta contable.
	 */
	private HashMap criteriosBusuqeda;
	
	/**
	 *encargado de manejar el estado dentro del action 
	 */
	private String estado;
	
	/**
	 * indica cual es el index dentro del hashmap que se va a eliminar
	 */
	private String indexEliminado;
	
	/**
	 * Hashmap con los conceptos eliminados
	 */
	private HashMap conceptosPagoPoolesXConvenioEliminadosMap;
	
	/**
	 * 
	 * 
	 */
	private String concepto;
	/*--------------------------------------------------
	 * 	FIN ATRIBUTOS DE CONCEPTOS PAGO POOLES X CONVENIO
	 ---------------------------------------------------*/

	
	/*-------------------------------------------------------------------
	 * METODOS SETTERS AND GETTERS DE CONCEPTOS PAGO POOLES X CONVENIO
	 -------------------------------------------------------------------*/
	
	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public void setConceptosPagoPoolesXConvenioEliminadosMap(
			HashMap conceptosPagoPoolesXConvenioEliminadosMap) {
		this.conceptosPagoPoolesXConvenioEliminadosMap = conceptosPagoPoolesXConvenioEliminadosMap;
	}

	public String getIndexEliminado() {
		return indexEliminado;
	}

	public void setIndexEliminado(String indexEliminado) {
		this.indexEliminado = indexEliminado;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public ArrayList<HashMap<String, Object>> getConceptosPagoPoolesList() {
		return conceptosPagoPoolesList;
	}

	public void setConceptosPagoPoolesList(
			ArrayList<HashMap<String, Object>> conceptosPagoPoolesList) {
		this.conceptosPagoPoolesList = conceptosPagoPoolesList;
	}

	public HashMap getConceptosPagoPoolesXConvenioMap() {
		return conceptosPagoPoolesXConvenioMap;
	}
	
	public Object getConceptosPagoPoolesXConvenioMap(String key) {
		return conceptosPagoPoolesXConvenioMap.get(key);
	}

	public void setConceptosPagoPoolesXConvenioMap(
			HashMap conceptosPagoPoolesXConvenioMap) {
		this.conceptosPagoPoolesXConvenioMap = conceptosPagoPoolesXConvenioMap;
	}
	
	public void setConceptosPagoPoolesXConvenioMap(String key,Object value) {
		this.conceptosPagoPoolesXConvenioMap.put(key, value);
	}
	

	public ArrayList<HashMap<String, Object>> getConveniosList() {
		return conveniosList;
	}

	public void setConveniosList(ArrayList<HashMap<String, Object>> conveniosList) {
		this.conveniosList = conveniosList;
	}

	
	public HashMap getCriteriosBusuqeda() {
		return criteriosBusuqeda;
	}

	public Object getCriteriosBusuqeda(String key) {
		return criteriosBusuqeda.get(key);
	}
	
	public void setCriteriosBusuqeda(HashMap criteriosBusuqeda) {
		this.criteriosBusuqeda = criteriosBusuqeda;
	}
	
	public void setCriteriosBusuqeda(String key,Object value) {
		this.criteriosBusuqeda.put(key, value);
	}

	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	public ArrayList<HashMap<String, Object>> getPoolesList() {
		return poolesList;
	}

	public void setPoolesList(ArrayList<HashMap<String, Object>> poolesList) {
		this.poolesList = poolesList;
	}

	public ArrayList<HashMap<String, Object>> getTiposServicioList() {
		return tiposServicioList;
	}

	public void setTiposServicioList(
			ArrayList<HashMap<String, Object>> tiposServicioList) {
		this.tiposServicioList = tiposServicioList;
	}

	public String getUltimoPatron() {
		return ultimoPatron;
	}

	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}
	
	
	
	public HashMap getConceptosPagoPoolesXConvenioEliminadosMap() {
		return conceptosPagoPoolesXConvenioEliminadosMap;
	}
	
	public Object getConceptosPagoPoolesXConvenioEliminadosMap(String key) {
		return conceptosPagoPoolesXConvenioEliminadosMap.get(key);
	}

	public void setConceptosPagoPoolesXConvenioEliminadosMap(String key, Object value) {
		this.conceptosPagoPoolesXConvenioEliminadosMap.put(key, value);
	}
	
	
	/*---------------------------------------------------------------------
	 * FIN METODOS SETTERS AND GETTERS DE CONCEPTOS PAGO POOLES X CONVENIO
	 ---------------------------------------------------------------------*/
	
	/*--------------------------------------------------
	 * 	  METOOS DE CONCEPTOS PAGO POOLES X CONVENIO
	 ---------------------------------------------------*/
	
	public void init (Connection connection , UsuarioBasico usuario)
	{
		HashMap parametros = new HashMap ();
		parametros.put("institucion", usuario.getCodigoInstitucionInt());
		//se cargan todos los pooles para mortrarlos en el select
		this.poolesList = UtilidadesFacturacion.obtenerPooles(connection, parametros);
		//se cargan todos los convenios existentes
		this.conveniosList = Utilidades.obtenerConvenios(connection, "", "", false, "", false);
		//se cargan todos los conceptos de pooles.		
		this.conceptosPagoPoolesList = UtilidadesFacturacion.obtenerConceptosPagoPooles(connection, parametros);
		//se cargan todo los tipos de servicio.
		this.tiposServicioList = UtilidadesFacturacion.obtenerTiposServicio(connection, parametros);
	}
	

	public void reset ()
	{
		this.conceptosPagoPoolesList = new ArrayList<HashMap<String,Object>>();
		this.conceptosPagoPoolesXConvenioMap = new HashMap ();
		this.conceptosPagoPoolesXConvenioMap.put("numRegistros", 0);
		this.poolesList = new ArrayList<HashMap<String,Object>> ();
		this.conveniosList = new ArrayList<HashMap<String,Object>> ();
		this.tiposServicioList = new ArrayList<HashMap<String,Object>> ();
		this.criteriosBusuqeda = new HashMap ();
		this.criteriosBusuqeda.put("numRegistros", 0);
		this.conceptosPagoPoolesXConvenioEliminadosMap = new HashMap ();
		this.conceptosPagoPoolesXConvenioEliminadosMap.put("numRegistros", 0);
	}

	
	public void resetpager()
	{
		
		this.linkSiguiente = "";
		this.patronOrdenar = "";
		this.ultimoPatron = "";
		
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errores = new ActionErrors();
		errores=super.validate(mapping, request);
		
	//	logger.info("el valor de mapa criterios de busqueda al entrar al validate es "+this.getCriteriosBusuqeda());
		//logger.info("el valor de mapa concepto al entrar al validate es "+this.getConceptosPagoPoolesXConvenioMap());
		
		if (estado.equals("buscar"))
		{
			if(this.getCriteriosBusuqeda("codigopool").toString().trim().equals(""))
			{
				errores.add("descripcion",new ActionMessage("errors.required","El Pool"));
				this.setEstado("inicial");
			}
			if(this.getCriteriosBusuqeda("codigoconvenio").toString().trim().equals(""))
			{
				this.setEstado("inicial");
				errores.add("descripcion",new ActionMessage("errors.required","El Convenio"));
			}
		}
		else						
				if (estado.equals("guardar"))
				{
					
					
					
					if(this.getCriteriosBusuqeda("codigopool").toString().trim().equals(""))
						errores.add("descripcion",new ActionMessage("errors.required","El Pool"));
					
					if(this.getCriteriosBusuqeda("codigoconvenio").toString().trim().equals(""))
						errores.add("descripcion",new ActionMessage("errors.required","El Convenio"));
					
					int numReg=Integer.parseInt(this.getConceptosPagoPoolesXConvenioMap("numRegistros")+"");
					
					for (int i=0;i<numReg;i++)
					{
						//logger.info("el valor de mapa concepto al entrar al validate es "+this.getConceptosPagoPoolesXConvenioMap());
						
							if(this.getConceptosPagoPoolesXConvenioMap("codigoconcepto_"+i).toString().trim().equals(""))
								errores.add("descripcion",new ActionMessage("errors.required","El Concepto del registro "+(i+1)));
							
							if(this.getConceptosPagoPoolesXConvenioMap("codigotiposervicio_"+i).toString().trim().equals(""))
								errores.add("descripcion",new ActionMessage("errors.required","El Tipo de Servicio del registro "+(i+1)));
							
							if(this.getConceptosPagoPoolesXConvenioMap("porcentaje_"+i).toString().trim().equals(""))
								errores.add("descripcion",new ActionMessage("errors.required","El Porcentaje del registro "+(i+1)));
							else
								try 
								{
									if(Float.parseFloat(this.getConceptosPagoPoolesXConvenioMap("porcentaje_"+i).toString())<0 || Float.parseFloat(this.getConceptosPagoPoolesXConvenioMap("porcentaje_"+i).toString())>100 )
										errores.add("descripcion",new ActionMessage("errors.porcentajeMayor100","El Porcentaje del registro "+(i+1)));
								
								} catch (Exception e) 
								{
									errores.add("descripcion",new ActionMessage("errors.porcentajeMayor100","El Porcentaje del registro "+(i+1)));
								}
								
							for (int j=0;j<numReg;j++)
							{
								
								if (j!=i)
									if ((this.getConceptosPagoPoolesXConvenioMap("codigopool_"+i).toString()+this.getConceptosPagoPoolesXConvenioMap("codigoconvenio_"+i).toString()+this.getConceptosPagoPoolesXConvenioMap("codigoconcepto_"+i).toString()+this.getConceptosPagoPoolesXConvenioMap("codigotiposervicio_"+i).toString()).equals((this.getConceptosPagoPoolesXConvenioMap("codigopool_"+j).toString()+this.getConceptosPagoPoolesXConvenioMap("codigoconvenio_"+j).toString()+this.getConceptosPagoPoolesXConvenioMap("codigoconcepto_"+j).toString()+this.getConceptosPagoPoolesXConvenioMap("codigotiposervicio_"+j).toString())))
									{	
										if (this.getConceptosPagoPoolesXConvenioMap("estabd_"+i).toString().equals("N"))
										{
											errores.add("descripcion",new ActionMessage("error.noRegistroMismaInformacion","pool, convenio, concepto y tipo de servicio en el registro "+(i+1)));
											j=numReg;
										}
									}
							}
							
					}
			
			
				}
		
		
		return errores;
	}

	
	
	
	
	/*--------------------------------------------------
	 * 	FIN METOOS DE CONCEPTOS PAGO POOLES X CONVENIO
	 ---------------------------------------------------*/
	
	
}