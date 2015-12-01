/*
 *Sep 20, 2005
 *
 */
package com.princetonsa.actionform.salasCirugia;

import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import util.ConstantesBD;
import util.UtilidadCadena;
import com.princetonsa.mundo.salasCirugia.ExcepcionAsocioTipoSala;

/**
 *Clase que almacena y carga la información utilizada para la funcionalidad
 *Parametrización de Excepcion Asocio de Tipo Sala
 * Modificada el 21/11/07 por Jhony Alexander Duque A.
 * por documento 525 --> Cambio en Funcionalidades de parametrizacion
 * 						 Cirugia por DyT.
 */
public class ExcepcionAsocioTipoSalaForm extends ValidatorForm {
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ExcepcionAsocioTipoSalaForm.class);
	
	
	
	
	/**
	 * Variable usada para almacenar el estado del controlador
	 */
	private String estado;
	
	
	/*-----------------------------------------------------------------
	 *  Atributos adicionados por Jhony Alexander Duque A.
	 *  por modificacion por documento 525 Cambio en funcionalidades
	 *  de parametrizacion cirugia por DyT
	 -----------------------------------------------------------------*/
	
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
	
	/**
	 * ArrayList con la informacion de tipos de sala
	 */
	private ArrayList<HashMap<String , Object>> tipoSala;
	
	/**
	 * ArrayList con la informacion de los esuqemas tarifarios 
	 * Particulares y Generales
	 */
	private ArrayList<HashMap<String, Object>> esqTarFio;
	
	/**
	 *almacena los tipos de servicios 
	 */
	private ArrayList<HashMap<String , Object>> tiposServicio;
	/**
	 *almacena los tipos de cirugia 
	 */	
	private ArrayList<HashMap<String , Object>> tiposCirugia;

	/**
	 * almacena los asocios
	 */
	private ArrayList<HashMap<String , Object>> asocios;

	private ArrayList<HashMap<String, Object>> tiposLiquidacion;
	/**
	 * Esta varible me almacena el valor del esquema tarifario 
	 * y el tipo de sala selecionado, para realizar la busqueda
	 */
	private  HashMap criteriosBusqueda;
	
	
	/**
	 * en este HashMap se almacena la informacion de Excepciones
	 * por tipo de sala
	 */
	private HashMap excepTipoSala;
	
	/**
	 * en este HashMap se almacena la informacion original de Excepciones
	 * por tipo de sala
	 */
	private HashMap excepTipoSalaClone;
	
	/**
	 * en este HashMap se almacena la informacion de Excepciones
	 * por tipo de sala que se eliminan
	 */
	private HashMap excepTipoSalaEliminados;
	
	
	private String indexEliminado;
	
	
	private boolean secBusquedaAvanzada;
	
	
	private String [] indices = ExcepcionAsocioTipoSala.indices;
	
	/*--------------------------------------------------------------------
	 * Fin de atributos adicionados.
	 -------------------------------------------------------------------*/
	

	/*----------------------------------------------------
	 * Metodos setters And Getters Adicionados
	 -----------------------------------------------------*/

	
	
		
	public String getIndexEliminado() {
		return indexEliminado;
	}


	public void setIndexEliminado(String indexEliminado) {
		this.indexEliminado = indexEliminado;
	}


	public ArrayList<HashMap<String, Object>> getTiposLiquidacion() {
		return tiposLiquidacion;
	}


	public void setTiposLiquidacion(
			ArrayList<HashMap<String, Object>> tiposLiquidacion) {
		this.tiposLiquidacion = tiposLiquidacion;
	}




	public HashMap getExcepTipoSala() {
		return excepTipoSala;
	}


	public void setExcepTipoSala(HashMap excepTipoSala) {
		this.excepTipoSala = excepTipoSala;
	}


	public Object getExcepTipoSala(String key) {
		return excepTipoSala.get(key);
	}


	public void setExcepTipoSala(String key,Object value) {
		excepTipoSala.put(key, value);
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


	public String getUltimoPatron() {
		return ultimoPatron;
	}


	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}


	public ArrayList<HashMap<String, Object>> getTipoSala() {
		return tipoSala;
	}


	public void setTipoSala(ArrayList<HashMap<String, Object>> tipoSala) {
		this.tipoSala = tipoSala;
	}
	

	public HashMap getCriteriosBusqueda() {
		return criteriosBusqueda;
	}


	public void setCriteriosBusqueda(HashMap criteriosBusqueda) {
		this.criteriosBusqueda = criteriosBusqueda;
	}

	

	public Object getCriteriosBusqueda(String key) {
		return criteriosBusqueda.get(key);
	}


	public void setCriteriosBusqueda(String key,Object value) {
		this.criteriosBusqueda.put(key, value);
	}

	
	

	public ArrayList<HashMap<String, Object>> getEsqTarFio() {
		return esqTarFio;
	}


	public void setEsqTarFio(ArrayList<HashMap<String, Object>> esqTarFio) {
		this.esqTarFio = esqTarFio;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	

	public ArrayList<HashMap<String, Object>> getAsocios() {
		return asocios;
	}


	public void setAsocios(ArrayList<HashMap<String, Object>> asocios) {
		this.asocios = asocios;
	}


	public ArrayList<HashMap<String, Object>> getTiposCirugia() {
		return tiposCirugia;
	}


	public void setTiposCirugia(ArrayList<HashMap<String, Object>> tiposCirugia) {
		this.tiposCirugia = tiposCirugia;
	}


	public ArrayList<HashMap<String, Object>> getTiposServicio() {
		return tiposServicio;
	}


	public void setTiposServicio(ArrayList<HashMap<String, Object>> tiposServicio) {
		this.tiposServicio = tiposServicio;
	}
	
	

	public HashMap getExcepTipoSalaEliminados() {
		return excepTipoSalaEliminados;
	}


	public void setExcepTipoSalaEliminados(HashMap excepTipoSalaEliminados) {
		this.excepTipoSalaEliminados = excepTipoSalaEliminados;
	}
	

	public Object getExcepTipoSalaEliminados(String key) {
		return excepTipoSalaEliminados.get(key);
	}


	public void setExcepTipoSalaEliminados(String key,Object value) {
		this.excepTipoSalaEliminados.put(key, value);
	}
	
	
	public HashMap getExcepTipoSalaClone() {
		return excepTipoSalaClone;
	}


	public void setExcepTipoSalaClone(HashMap excepTipoSalaClone) {
		this.excepTipoSalaClone = excepTipoSalaClone;
	}

	
	public Object getExcepTipoSalaClone(String key) {
		return excepTipoSalaClone.get(key);
	}


	public void setExcepTipoSalaClone(String key, Object value) {
		this.excepTipoSalaClone.put(key, value);
	}

	
	
	/*-------------------------------------------------------
	 * Fin de metodos Setters And Getters Adicionados
	 -------------------------------------------------------*/
	
	/*-------------------------------------------------------
	 * Metodos adicionales
	 -------------------------------------------------------*/
	
	public void resetPager ()
	{
		this.linkSiguiente ="";
		this.patronOrdenar ="";
		this.ultimoPatron  ="";
	}
	

	public void resetBusqueda()
	{
		this.criteriosBusqueda = new HashMap ();
		this.setCriteriosBusqueda("numRegistros", 0);
	}


	public void reset()
	{
		this.excepTipoSala = new HashMap ();
		this.setExcepTipoSala("numRegistros", 0);
		this.excepTipoSalaEliminados = new HashMap ();
		this.setExcepTipoSalaEliminados("numRegistros", 0);
		this.excepTipoSalaClone = new HashMap ();
		this.setExcepTipoSalaClone("numRegistros", 0);
		this.secBusquedaAvanzada=false;
	}

	public boolean isSecBusquedaAvanzada() {
		return secBusquedaAvanzada;
	}


	public void setSecBusquedaAvanzada(boolean secBusquedaAvanzada) {
		this.secBusquedaAvanzada = secBusquedaAvanzada;
	}


	public void resetSelect ()
	{
		this.tiposServicio = new ArrayList<HashMap<String,Object>>();
		this.asocios = new ArrayList<HashMap<String,Object>>();
		this.tiposCirugia = new ArrayList<HashMap<String,Object>>();

	}

 
public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
{
	ActionErrors errores = new ActionErrors();
	errores=super.validate(mapping, request);
	

	if (this.estado.equals("buscar"))
	{
		//1) se valida de que se seleccione el esquema tarifario
		if ((this.getCriteriosBusqueda(indices[15])+"").equals("-1"))
		{
			errores.add("descripcion",new ActionMessage("errors.required","La Seleccion del Esquema Tarifario"));
			this.setEstado("empezar");
		}
		//2) se valida de que se seleccione el tipo de sala
		if ((this.getCriteriosBusqueda(indices[10])+"").equals(ConstantesBD.codigoNuncaValido+""))
		{
			errores.add("descripcion",new ActionMessage("errors.required","La Seleccion del Tipo de Sala"));
			this.setEstado("empezar");
		}
		
	}
	
	if (this.estado.equals("busquedaavanzada"))
	{
		//1) se valida de que se seleccione el esquema tarifario
		if ((this.getCriteriosBusqueda(indices[15])+"").equals("-1"))
		{
			errores.add("descripcion",new ActionMessage("errors.required","La Seleccion del Esquema Tarifario"));
			this.setEstado("consultar");
		}
		
	}
	
	
	if (this.estado.equals("guardar"))
	{
		logger.info("\n ESTOY EN EL GUARDAR DEL FORM "+ getExcepTipoSala());
		for (int i= 0; i < Integer.parseInt(this.getExcepTipoSala("numRegistros")+"");i++)
		{
			if ((this.getExcepTipoSala(indices[8]+i)+"").equals(ConstantesBD.acronimoNo))
			{
				//1) se valida que el asocio que no este vacio
				if ((this.getExcepTipoSala(indices[6]+i)+"").equals(""))
					errores.add("descripcion",new ActionMessage("errors.required","La Seleccion del Asocio del registro "+(i+1)));
				
				//2) se valida que el tipo de liquidacion no este vacio
				if ((this.getExcepTipoSala(indices[4]+i)+"").equals(""))
					errores.add("descripcion",new ActionMessage("errors.required","La Seleccion del Tipo liquidación del registro "+(i+1)));
				else
				{
					//3) se valida que el campo cantidad que no este vacio
					if ((this.getExcepTipoSala(indices[5]+i)+"").equals(""))
					{
						if ((this.getExcepTipoSala(indices[4]+i)+"").equals("3"))
							errores.add("descripcion",new ActionMessage("errors.required","El valor del porcentaje del registro "+(i+1)));
						else
							if ((this.getExcepTipoSala(indices[4]+i)+"").equals("1"))
								errores.add("descripcion",new ActionMessage("errors.required","El valor de las unidades del registro "+(i+1)));
							else
								if ((this.getExcepTipoSala(indices[4]+i)+"").equals("2"))
									errores.add("descripcion",new ActionMessage("errors.required","El valor del registro "+(i+1)));
						
					}
					
					if ((this.getExcepTipoSala(indices[4]+i)+"").equals("3"))
						if (!UtilidadCadena.noEsVacio(this.getExcepTipoSala(indices[7]+i)+""))
							errores.add("descripcion",new ActionMessage("errors.required","La Seleccion de liquidar sobre tarifa del registro "+(i+1)));
				
				}
			}
			
			//se debe de validar el Unique que consta de los siguientes campos:
			//-- tipo de servicio
			//-- tipo de cirugia
			//-- asocio
			//-- esquema tarifario
			//-- tipo de sala
			
			String unique1=this.getExcepTipoSala(indices[2]+i)+""+this.getExcepTipoSala(indices[3]+i)+this.getExcepTipoSala(indices[6]+i)+
						   this.getExcepTipoSala(indices[11]+i)+this.getExcepTipoSala(indices[12]+i)+this.getExcepTipoSala(indices[10]+i);
			
			for (int k= 0; k!=i && k < Integer.parseInt(this.getExcepTipoSala("numRegistros")+"");k++)
			{
				String unique2=this.getExcepTipoSala(indices[2]+k)+""+this.getExcepTipoSala(indices[3]+k)+this.getExcepTipoSala(indices[6]+k)+
				   this.getExcepTipoSala(indices[11]+k)+this.getExcepTipoSala(indices[12]+k)+this.getExcepTipoSala(indices[10]+k);
				
				/*logger.info("\n :::::::::::Las cadenas a comparar son :::::::::::: k->"+k+" i->"+i);
				logger.info("\n::: unique1 ==>"+unique1);
				logger.info("\n::: unique2 ==>"+unique2);
				logger.info("\n ::::::::::::::::::::::::::::::::::::::::::::::::::::");
				*/
				if (unique1.equals(unique2))
				{
					logger.info("\n :::::::::::entre a son iguales :::::::::::: k->"+k+" i->"+i);
					errores.add("descripcion",new ActionMessage("error.noRegistroMismaInformacion","Tipo de Servicio, Tipo de Cirugía, Asocio, Esquema Tarifario y Tipo de Sala en el registro "+(i+1)));		
				}
			
			}
			
		}
	}
	
	
	
	
	
	return errores;
}
	

	
	/*-------------------------------------------------------
	 * Fin Metodos Adicionales
	 --------------------------------------------------------*/
	
}
