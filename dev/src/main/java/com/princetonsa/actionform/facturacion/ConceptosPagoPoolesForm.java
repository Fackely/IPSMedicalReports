package com.princetonsa.actionform.facturacion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ObjetoReferencia;

import com.princetonsa.actionform.manejoPaciente.CensoCamasForm;


public class ConceptosPagoPoolesForm extends ValidatorForm
{
	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	/**
	 * Para manjar los logger de la clase censocamasform
	 */
	Logger logger = Logger.getLogger(ConceptosPagoPoolesForm.class);
	
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
	 * 			ATRIBUTOS DE CONCEPTOS PAGO POOLES
	 ---------------------------------------------------*/
	/**
	 * HashMap que almacena la consulta de los
	 * Conceptos de pago de los pooles
	 */
	private HashMap conceptosPagoPoolesMap;
	
	/**
	 * String estado; este es el encargado de administrar
	 * las acciones dentro del Action.
	 */
	private String estado;
	
	/**
	 * Hashmap con los conceptos eliminados
	 */
	private HashMap conceptosPagoPoolesEliminadosMap;
	/**
	 * indica cual es el index dentro del hashmap que se va a eliminar
	 */
	private String indexEliminado;
	
	
	/*--------------------------------------------------
	 * 		FIN ATRIBUTOS DE CONCEPTOS PAGO POOLES
	 ---------------------------------------------------*/

	/*-------------------------------------------
	 * 	GETTERS AND SETTERS
	 -------------------------------------------*/
	
	public HashMap getConceptosPagoPoolesEliminadosMap() {
		return this.conceptosPagoPoolesEliminadosMap;
	}

	public void setConceptosPagoPoolesEliminadosMap(
			HashMap conceptosPagoPoolesEliminadosMap) {
		this.conceptosPagoPoolesEliminadosMap = conceptosPagoPoolesEliminadosMap;
	}

	public Object getConceptosPagoPoolesEliminadosMap(String key)
	{
		return this.conceptosPagoPoolesEliminadosMap.get(key);
	}
	
	public void setConceptosPagoPoolesEliminadosMap(String key, Object value)
	{
		this.conceptosPagoPoolesEliminadosMap.put(key, value);
	}
	
	public HashMap getConceptosPagoPoolesMap() {
		return conceptosPagoPoolesMap;
	}

	public void setConceptosPagoPoolesMap(HashMap conceptosPagoPoolesMap) {
		this.conceptosPagoPoolesMap = conceptosPagoPoolesMap;
	}

	public Object getConceptosPagoPoolesMap (String key)
	{
		return this.conceptosPagoPoolesMap.get(key);
	}
	
	public void setConceptosPagoPoolesMap (String key,Object value)
	{
		this.conceptosPagoPoolesMap.put(key, value);
	}
	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getIndexEliminado() {
		return indexEliminado;
	}

	public void setIndexEliminado(String indexEliminado) {
		this.indexEliminado = indexEliminado;
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
	
	
	
	
	
	/*-------------------------------------------
	 * 	GETTERS AND SETTERS
	 -------------------------------------------*/
	
	
	/*------------------------------------
	 * 				METODOS 
	 ------------------------------------*/
	/**
	 * Metodo encargado de inicializar las variables
	 */
	public void reset ()
	{
		this.conceptosPagoPoolesMap = new HashMap ();
		this.conceptosPagoPoolesMap.put("numRegistros", 0);
		this.conceptosPagoPoolesEliminadosMap = new HashMap ();
		this.conceptosPagoPoolesEliminadosMap.put("numRegistros", 0);
		this.indexEliminado = "";
		this.linkSiguiente = "";
		this.patronOrdenar = "";
		this.ultimoPatron = "";
		
	}
	
	
	
	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, 
			HttpServletRequest request) {
		
		ActionErrors errores = new ActionErrors();
		errores=super.validate(mapping, request);
		if (estado.equals("guardar"))
		{
			int numReg=Integer.parseInt(this.getConceptosPagoPoolesMap("numRegistros")+"");
			
			for (int i=0;i<numReg;i++)
			{
				if(this.getConceptosPagoPoolesMap("codigoconcepto_"+i).toString().trim().equals(""))
					errores.add("descripcion",new ActionMessage("errors.required","El codigo del registro "+(i+1)));
				
				if(this.getConceptosPagoPoolesMap("descripcionconcepto_"+i).toString().trim().equals(""))
					errores.add("descripcion",new ActionMessage("errors.required","La descripción del registro "+(i+1)));
				
				if(this.getConceptosPagoPoolesMap("tipoconcepto_"+i).toString().trim().equals(""))
					errores.add("descripcion",new ActionMessage("errors.required","El tipo del registro "+(i+1)));
				
			}
			
			/*-------------------------------------------------------
			 * VALIDA SI EXISTEN CODIGOS, "DESCRIPCION Y TIPO" IGUALES
			 --------------------------------------------------------*/
			HashMap codigosComparados = new HashMap();
			String descripcion = "";
			String descripciontipo = "";
			String aux1 = "";
			String aux2 = "";
			String auxdesctip1 = "";
			String auxdesctip2 = "";
			
			for(int i=0;i<numReg;i++)
			{
				//se almacena el primer codigo del HashMap a ser comparado.
				aux1=this.getConceptosPagoPoolesMap("codigoconcepto_"+i).toString();
				//se arma un registro concatenando la descripcion y el tipo en la primera posicion del HashMap para ser comparados.
				auxdesctip1=this.getConceptosPagoPoolesMap("descripcionconcepto_"+i).toString()+this.getConceptosPagoPoolesMap("tipoconcepto_"+i).toString();
				
				for(int j=numReg-1;j>i;j--)
				{
					//se almacena el ultimo codigo del HashMap para compararlo con el primero
					aux2=this.getConceptosPagoPoolesMap("codigoconcepto_"+j).toString();
					//se arma el otro registro a ser comparado, concatenando la descripcion y el tipo en la ultima posicion del HashMap. 
					auxdesctip2=this.getConceptosPagoPoolesMap("descripcionconcepto_"+j).toString()+this.getConceptosPagoPoolesMap("tipoconcepto_"+j).toString();
					
					//se compara el codigo que se encuentra en la posicion "i"
					//del HashMap con el que se encuentra en la posicion "j".
					if(aux1.compareToIgnoreCase(aux2)==0&&!aux1.equals("")
						&&!aux2.equals("")&&!codigosComparados.containsValue(aux1))
					{
						if(descripcion.equals(""))
							descripcion = (i+1) + "";
						descripcion += "," + (j+1);
						
					}
					//se compara la descripcion y el tipo que se encuentra en la posicion "i"
					//del HashMap con el que se encuentra en la posicion "j".
					if(auxdesctip1.compareToIgnoreCase(auxdesctip2)==0&&!auxdesctip1.equals("")
							&&!auxdesctip2.equals("")&&!codigosComparados.containsValue(aux1))
						{
							
							if(descripciontipo.equals(""))
								descripciontipo = (i+1) + "";
							descripciontipo += "," + (j+1);
							
						}
					
					
				}
				//se pregunta si se genero alguna descripcion
				//de codigos repetidos para adicionarla a los errores.
				if(!descripcion.equals(""))
				{
					//se adiciona el error.
					errores.add("códigos iguales", 
							new ActionMessage("error.salasCirugia.igualesGeneral",
								"códigos","en los registros Nº "+descripcion));
				}
			
				//se pregunta si se genero alguna descripciontipo
				//de descripcion y tipo repetidos para adicionarla a los errores.
				if(!descripciontipo.equals(""))
				{
					//se adiciona el error.
					errores.add("Descripcion y Tipo iguales", 
							new ActionMessage("error.salasCirugia.igualesGeneral",
								"Descripcion y Tipo","en los registros Nº "+descripciontipo));
				}
				
				descripcion = "";
				descripciontipo = "";
				
				//se almacenan los codigos ya comparados
				//a fin no volverlos a comparar. 
				codigosComparados.put(i+"",aux1);
				
				
			}
			/*-----------------------------------------------------------------
			 * FIN VALIDA SI EXISTEN CODIGOS, "DESCRIPCION Y TIPO" IGUALES
			 -----------------------------------------------------------------*/
			
		}
		
				
		return errores;
	}
	
	/*------------------------------------
	 * 			FIN	METODOS 
	 ------------------------------------*/
	
}