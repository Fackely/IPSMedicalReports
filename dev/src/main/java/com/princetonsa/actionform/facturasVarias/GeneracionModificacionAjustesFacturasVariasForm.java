package com.princetonsa.actionform.facturasVarias;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.mundo.facturasVarias.GeneracionModificacionAjustesFacturasVarias;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

/**
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 */

public class GeneracionModificacionAjustesFacturasVariasForm extends ValidatorForm
{
	
	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	/**
	 * Para manejar los logger de la clase GeneracionModificacionAjustesFacturasVariasForm
	 */
	Logger logger = Logger.getLogger(GeneracionModificacionAjustesFacturasVariasForm.class);
	
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 ---------------------------------------------*/
		
	
	/*--------------------------------------------------------------------
	 * INDICES PARA EL MANEJO DE LOS KEY'S DE LOS MAPAS.
	 ---------------------------------------------------------------------*/
	private String [] indicesAjustes = GeneracionModificacionAjustesFacturasVarias.indicesAjustesFacturasVarias;
	
	private String [] indicesFacturasVarias = GeneracionModificacionAjustesFacturasVarias.indicesFacturasVarias;
	//-----------------------------------------------------------------------
	
	
	
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
	
	
	
	/*--------------------------------------------------------------
	 * 	TRIBUTOS DE GEMERACION MODIFICACION AJUSTES FACTURAS VARIAS
	 ---------------------------------------------------------------*/
	/**
	 * Estado de la forma.
	 */
	private String estado;
	
	/**
	 * Mapa que almacena los diferentes filtros que puede
	 * contener la busqueda de ajustes a facturas.
	 * este mapa no tiene y no necita un key numRegistros.
	 */
	private HashMap filtrosBusqueda;
	
	
	/**
	 * Mapa que almacena la informacion del ajuste.
	 * este mapa no necita un key numRegistros.
	 */
	private HashMap mapaAjustes;
	
	/**
	 * Mapa que almacena la informacion de la factura
	 */
	private HashMap mapaInfoFac;
	
	/**
	 * Listado con la informacion de la consulta
	 */
	private HashMap listado;
	
	/**
	 * String encargado de manejar la posicion dentro del
	 * mapa de la busqueda.
	 */
	private String index = ConstantesBD.codigoNuncaValido+"";
	
	/**
	 * 
	 */
	private ArrayList<HashMap<String, Object>> conceptosFacVar = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * Mapa donde se almacenan los datos antes de ser modificados
	 */
	private HashMap mapaAjustesClone = new HashMap ();
	
	private boolean operacionTrue = false;
	
	private String codigoDeudor;
	private String descripDeudor;
	private String tipoDeudor;
	
	
	/*-------------------------------------------------------------------
	 * 	FIN TRIBUTOS DE GEMERACION MODIFICACION AJUSTES FACTURAS VARIAS
	 -------------------------------------------------------------------*/

	
	

	/*---------------------------------------------------------
	 * 				METODOS GETTERS AND SETTERS
	 ----------------------------------------------------------*/
	//-----listado-----------------------------------------------------
	
	
	
	
	public HashMap getListado() {
		return listado;
	}

	public String getCodigoDeudor() {
		return codigoDeudor;
	}

	public void setCodigoDeudor(String codigoDeudor) {
		this.codigoDeudor = codigoDeudor;
	}

	public String getDescripDeudor() {
		return descripDeudor;
	}

	public void setDescripDeudor(String descripDeudor) {
		this.descripDeudor = descripDeudor;
	}

	public String getTipoDeudor() {
		return tipoDeudor;
	}

	public void setTipoDeudor(String tipoDeudor) {
		this.tipoDeudor = tipoDeudor;
	}

	public void setListado(HashMap listado) {
		this.listado = listado;
	}
	
	public Object getListado(String key) {
		return listado.get(key);
	}

	public void setListado(String key,Object value) {
		this.listado.put(key, value);
	}
	//------------------------------------------------------------------
	

	// ---estado---------------------------------------------------------
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	//--------------------------------------------------------------------

	
	// ---filtroBusqueda--------------------------------------------------
	public HashMap getFiltrosBusqueda() {
		return filtrosBusqueda;
	}

	public void setFiltrosBusqueda(HashMap filtrosBusqueda) {
		this.filtrosBusqueda = filtrosBusqueda;
	}

	public Object getFiltrosBusqueda(String key) {
		return filtrosBusqueda.get(key);
	}

	public void setFiltrosBusqueda(String key,Object value) {
		this.filtrosBusqueda.put(key, value);
	}
	//----------------------------------------------------------------------
	
	
	// ---mapaAjustes--------------------------------------------------------
	public HashMap getMapaAjustes() {
		return mapaAjustes;
	}

	public void setMapaAjustes(HashMap mapaAjustes) {
		this.mapaAjustes = mapaAjustes;
	}
	
	public Object getMapaAjustes(String key) {
		return mapaAjustes.get(key);
	}

	public void setMapaAjustes(String key,Object value) {
		this.mapaAjustes.put(key, value);
	}
	//---------------------------------------------------------------------
	
	
	// ---mapaInfoFac--------------------------------------------------------

	public HashMap getMapaInfoFac() {
		return mapaInfoFac;
	}

	public void setMapaInfoFac(HashMap mapaInfoFac) {
		this.mapaInfoFac = mapaInfoFac;
	}

	public Object getMapaInfoFac(String key	) {
		return mapaInfoFac.get(key);
	}

	public void setMapaInfoFac(String key,Object value) {
		this.mapaInfoFac.put(key, value);
	}
	//----------------------------------------------------------------------
	
	
	//----Manejo pager --------------------------------------------------
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
	//----------------------------------------------------------------
	
	//----index--------------------------------------------------------

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}
	//------------------------------------------------------------------
	
	//----conceptos facturas varias --------------------

	public ArrayList<HashMap<String, Object>> getConceptosFacVar() {
		return conceptosFacVar;
	}

	public void setConceptosFacVar(
			ArrayList<HashMap<String, Object>> conceptosFacVar) {
		this.conceptosFacVar = conceptosFacVar;
	}
	
	//--------------------------------------------------
	
	
	//----- Ajustes Clone -------------------------------------
	public HashMap getMapaAjustesClone() {
		return mapaAjustesClone;
	}

	public void setMapaAjustesClone(HashMap mapaAjustesClone) {
		this.mapaAjustesClone = mapaAjustesClone;
	}
	
	public Object getMapaAjustesClone(String key) {
		return mapaAjustesClone.get(key);
	}

	public void setMapaAjustesClone(String key,Object value) {
		this.mapaAjustesClone.put(key, value);
	}
	//-----------------------------------------------------------
	
///----operacionTrue -------------------------------------------	
	public boolean isOperacionTrue() {
		return operacionTrue;
	}

	public void setOperacionTrue(boolean operacionTrue) {
		this.operacionTrue = operacionTrue;
	}
	
//---------------------------------------------------------------

	/*---------------------------------------------------------
	 * 				FIN METODOS GETTERS AND SETTERS
	 ----------------------------------------------------------*/

	
	
	
	/*-------------------------------------------------------
	 * 				  METODOS ADICIONALES
	 --------------------------------------------------------*/
	
	public void reset ()
	{
		this.filtrosBusqueda = new HashMap ();
		this.setTipoDeudor("");
		this.setCodigoDeudor("");
		this.setDescripDeudor("");
		this.listado = new HashMap ();
		this.setListado("numRegistros", 0);
		this.linkSiguiente = "";
		this.patronOrdenar = "";
		this.ultimoPatron="";
		this.conceptosFacVar = new ArrayList<HashMap<String,Object>>();
		this.mapaAjustesClone = new HashMap ();
		this.operacionTrue=false;
		
	}
	
	
	public void resetFactura ()
	{
		this.mapaInfoFac = new HashMap ();
	}
	
	public void resetAjsutes()
	{
		this.mapaAjustes = new HashMap ();
		this.operacionTrue=false;
	}

	public void resetBusqueda()
	{
		this.listado = new HashMap ();
		this.setListado("numRegistros", 0);
	}
		
	
	/*-------------------------------------------------------
	 * 				FIN METODOS ADICIONALES
	 --------------------------------------------------------*/
	
	/*-------------------------------------------------------
	 * 					METODO VALIDATE
	 --------------------------------------------------------*/

	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
	
		ActionErrors errores = new ActionErrors();
		errores=super.validate(mapping, request);
		
		boolean val=true,ban=true;
		if (estado.equals("guardar"))
		{

			//1) se valida que la factura este cargada
			if ((this.getMapaInfoFac(indicesFacturasVarias[13]+"0")+"").equals("") || (this.getMapaInfoFac(indicesFacturasVarias[13]+"0")+"").equals("null"))
			{
				errores.add("descripcion",new ActionMessage("error.NoCargar","la Factura"));
				val=false;
			}
						
			//2) se valida que venga el tipo de ajuste
			if ((this.getMapaAjustes(indicesAjustes[2]+"0")).equals("") || (this.getMapaAjustes(indicesAjustes[2]+"0")).equals("null") )
				errores.add("descripcion",new ActionMessage("errors.required","El tipo de ajuste"));
				
			//3) se valida requerido el concepto
			if ((this.getMapaAjustes(indicesAjustes[5]+"0")).equals("") || (this.getMapaAjustes(indicesAjustes[5]+"0")).equals("null") )
				errores.add("descripcion",new ActionMessage("errors.required","El Concepto"));
			
			//4)se valida como requerido la fecha del ajuste
			if ((this.getMapaAjustes(indicesAjustes[3]+"0")).equals("") || (this.getMapaAjustes(indicesAjustes[3]+"0")).equals("null") )
			{
				errores.add("descripcion",new ActionMessage("errors.required","La Fecha"));
				val=false;
			}

			//5)validacion de que la fecha final sea mayor o igual a la fecha inicial
			if (val)
			{
				logger.info(">>>>>>>"+UtilidadFecha.conversionFormatoFechaAAp(this.getMapaInfoFac(indicesFacturasVarias[15]+"0")+""));
				logger.info("-------"+UtilidadFecha.conversionFormatoFechaAAp(this.getMapaAjustes(indicesAjustes[3]+"0")+""));
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(this.getMapaInfoFac(indicesFacturasVarias[15]+"0")+"") , UtilidadFecha.conversionFormatoFechaAAp(this.getMapaAjustes(indicesAjustes[3]+"0")+"")))
					errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual", this.getMapaAjustes(indicesAjustes[3]+"0")+"", this.getMapaInfoFac(indicesFacturasVarias[15]+"0")+""));
				else//6)se valida que la fecha sea menor a la actual
					if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.conversionFormatoFechaAAp(this.getMapaAjustes(indicesAjustes[3]+"0")+""),UtilidadFecha.getFechaActual()))
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", this.getMapaAjustes(indicesAjustes[3]+"0")+"", "Actual "+UtilidadFecha.getFechaActual()));
			}
			//7) se valida como requerido el valor del ajuste
			if ((this.getMapaAjustes(indicesAjustes[6]+"0")).equals("") || (this.getMapaAjustes(indicesAjustes[6]+"0")).equals("null"))
			{
				errores.add("descripcion",new ActionMessage("errors.required","El valor Ajuste"));
				ban=false;
			}
			else
			{
				//8) se valida que el valor del ajuste sea un numero
				try 
				{	
					
					Double.parseDouble(this.getMapaAjustes(indicesAjustes[6]+"0")+"");
					
				} catch (Exception e) 
				
				{
					ban=false;
					errores.add("descripcion",new ActionMessage("errors.float","El Valor del Ajuste "+this.getMapaAjustes(indicesAjustes[6]+"0")));
				}
				
				if (ban)
				{
										
					
					//9)  se valida los que el valor ingresado en valor ajuste tenga un maximo de 2 decimales
					String [] tmp =UtilidadTexto.separarParteEnteraYDecimal(this.getMapaAjustes(indicesAjustes[6]+"0")+"");
					
					if (tmp[0].length()>10)
						errores.add("descripcion",new ActionMessage("error.cantidadParteEnteraGene","Valor del Ajuste "+this.getMapaAjustes(indicesAjustes[6]+"0"),"10"));

					if (tmp[1].length()>2)
						errores.add("descripcion",new ActionMessage("error.cantidadParteDecimalGene","Valor del Ajuste "+this.getMapaAjustes(indicesAjustes[6]+"0"),"2"));
										
					
				}
				
			
			}
			
			//aqui se hacen las validaciones cuando es modificacion
		//	if (this.getMapaAjustes().containsKey(indicesAjustes[9]+"0") &&  (this.getMapaAjustes(indicesAjustes[9]+"0")+"").equals(ConstantesBD.acronimoSi))
			//{
				
				boolean ban2=ban;
				if (ban)//10)aqui se valida que sea el campo valor del ajuste sea > 0
					if (Utilidades.convertirADouble(this.getMapaAjustes(indicesAjustes[6]+"0")+"")<1)
					{
						errores.add("descripcion",new ActionMessage("errors.debeSerNumeroMayor","El valor del Ajuste","0"));
						ban2=false;
					}
				
				//aqui se examina si el tipo del ajuse es credito
				if ((this.getMapaAjustes(indicesAjustes[2]+"0")+"").equals(ConstantesIntegridadDominio.acronimoCredito))
				{
					logger.info("\n saldo fac --> "+Utilidades.convertirADouble(this.getMapaInfoFac(indicesFacturasVarias[11]+"0")+"")+" -->"+Utilidades.convertirADouble(this.getMapaAjustes(indicesAjustes[6]+"0")+""));
					if (ban2)//11)se valida que el valor del ajuste sea menor o igual al saldo de la factura.
						if (Utilidades.convertirADouble(this.getMapaInfoFac(indicesFacturasVarias[11]+"0")+"") < Utilidades.convertirADouble(this.getMapaAjustes(indicesAjustes[6]+"0")+""))
							errores.add("descripcion",new ActionMessage("errors.debeSerNumeroMenorIgual","El valor del Ajuste",UtilidadTexto.formatearValores(this.getMapaInfoFac(indicesFacturasVarias[11]+"0")+"")));
				}
				
				
					
		//	}
				
				
				
		}
		
		
		return errores ;
	}

	


	

	



	/*-------------------------------------------------------
	 * 					FIN METODO VALIDATE
	 --------------------------------------------------------*/
	
}
