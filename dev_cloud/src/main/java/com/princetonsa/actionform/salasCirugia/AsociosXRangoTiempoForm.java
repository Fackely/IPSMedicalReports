package com.princetonsa.actionform.salasCirugia;

import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import util.ConstantesBD;
import util.UtilidadTexto;



/**
 *@author Jhony Alexander Duque A.
 *jduque@princetonsa.com 
 */

public class AsociosXRangoTiempoForm extends ValidatorForm
{
	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	/**
	 * Para manjar los logger de la clase censocamasform
	 */
	Logger logger = Logger.getLogger(AsociosXRangoTiempoForm.class);
	
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
	
	/*-------------------------------------------------
	 * 	   ATRIBUTOS DE ASOCIOS X RANGO TIEMPO
	 -------------------------------------------------*/
	/**
	 * Atributo de estado para el manejo del action
	 */
	private String estado;
	
	private String indexEliminado;
	
	/*-------------------------------------------------
	 * 	  FIN ATRIBUTOS DE ASOCIOS X RANGO TIEMPO
	 -------------------------------------------------*/
	private HashMap asociosXRangoTiempo;
	
	private HashMap asociosXRangoTiempoAll;
	
	private HashMap asociosXRangoTiempoClone;
	
	private HashMap criteriosBusqueda;	
	
	private HashMap fechas;
	
	private HashMap fechasClone;
	
	private HashMap fehcasEliminadoMap;
	
	private HashMap asociosXRangoTiempoEliminadoMap; 
	
	private HashMap detalleCodigos;
	
	private HashMap tarifariosOficiales;
	
	private String index;
	
	private String indexDestino;
	
	private String origen;
	
	/*-------------------------------------------------
	 * 	   ATRIBUTOS DE LOS PARAMETROS DE BUSQUEDA
	 --------------------------------------------------*/
	/**
	 * alamcena los convenios 
	 */
	private ArrayList<HashMap<String, Object>>convenios;
	/**
	 * almacena los esquemas tarifarios
	 */
	private ArrayList<HashMap<String , Object>> esqTarfio;
	/**
	 *almacena los tipos de servicios 
	 */
	private ArrayList<HashMap<String , Object>> tiposServicio;
	/**
	 *almacena los tipos de cirugia 
	 */	
	private ArrayList<HashMap<String , Object>> tiposCirugia;
	/**
	 * almacena los tipos de Anestesia
	 */
	private ArrayList<HashMap<String , Object>> tiposAnestesia;
	/**
	 * almacena los asocios
	 */
	private ArrayList<HashMap<String , Object>> asocios;
	
	private ArrayList<HashMap<String, Object>> servicios;
	
	
	/*-------------------------------------------------
	 * 	 FIN  ATRIBUTOS DE LOS PARAMETROS DE BUSQUEDA
	 --------------------------------------------------*/
	
	
	/*------------------------------------------------------
	 * METODOS PARA EL MANEJO DE ASOCIOS X RANGO DE TIEMPO
	 -----------------------------------------------------*/
	
	public void resetPager ()
	{
		this.linkSiguiente ="";
		this.patronOrdenar ="";
		this.ultimoPatron  ="";
	}
	
	public void reset()
	{
		this.asociosXRangoTiempo = new HashMap ();
		this.setAsociosXRangoTiempo("numRegistros", 0);
		this.asociosXRangoTiempoEliminadoMap = new HashMap ();
		this.setAsociosXRangoTiempoEliminadoMap("numRegistros", 0);
		this.asociosXRangoTiempoClone = new HashMap ();
		this.setAsociosXRangoTiempoClone("numRegistros", 0);
		this.detalleCodigos = new HashMap ();
		this.setDetalleCodigos("numRegistros", 0);
		this.tarifariosOficiales = new HashMap ();
		this.setTarifariosOficiales("numRegistros", 0);
		this.asociosXRangoTiempoAll = new HashMap ();
		this.setAsociosXRangoTiempoAll("numRegistros", 0);
		this.origen="";
	
	}
	
	
	public void resetVigencias ()
	{
		this.fechas = new HashMap ();
		this.setFechas("numRegistros", 0);
		this.fehcasEliminadoMap = new HashMap ();
		this.setFehcasEliminadoMap("numRegistros", 0);
		this.fechasClone = new  HashMap();
		this.setFechasClone("numRegistros", 0);
	}
		
	public void resetSelect ()
	{
		this.tiposServicio = new ArrayList<HashMap<String,Object>>();
		this.tiposAnestesia = new ArrayList<HashMap<String,Object>>();
		this.tiposCirugia = new ArrayList<HashMap<String,Object>>();
		this.servicios = new ArrayList<HashMap<String,Object>>();
	}
	
	public void  resetBusqueda ()
	{
		this.criteriosBusqueda = new HashMap ();
		this.setAsociosXRangoTiempo("numRegistros", 0);
		this.convenios = new ArrayList<HashMap<String,Object>>();
		this.esqTarfio = new ArrayList<HashMap<String,Object>>();
		
	}
	
	
	
	/*---------------------------------------------------------
	 * FIN METODOS PARA EL MANEJO DE ASOCIOS X RANGO DE TIEMPO
	 --------------------------------------------------------*/
	
	/*-----------------------------------------------------
	 * 			METODOS GETTERS AND SETTERS
	 -----------------------------------------------------*/
	public HashMap getAsociosXRangoTiempo() {
		return asociosXRangoTiempo;
	}

	public void setAsociosXRangoTiempo(HashMap asociosXRangoTiempo) {
		this.asociosXRangoTiempo = asociosXRangoTiempo;
	}

	
	public Object getAsociosXRangoTiempo(String key) {
		return asociosXRangoTiempo.get(key);
	}
	
	public void setAsociosXRangoTiempo(String key,Object value) {
		this.asociosXRangoTiempo.put(key, value);
	}
	
	
	public HashMap getCriteriosBusqueda() {
		return criteriosBusqueda;
	}
	
	public Object getCriteriosBusqueda(String key) {
		return criteriosBusqueda.get(key);
	}

	public void setCriteriosBusqueda(HashMap atributosbusqueda) {
		this.criteriosBusqueda = atributosbusqueda;
	}

	public void setCriteriosBusqueda(String key,Object value) {
		this.criteriosBusqueda.put(key, value);
	}
	
	public ArrayList<HashMap<String, Object>> getConvenios() {
		return convenios;
	}

	public void setConvenios(ArrayList<HashMap<String, Object>> convenios) {
		this.convenios = convenios;
	}

	public ArrayList<HashMap<String, Object>> getEsqTarfio() {
		return esqTarfio;
	}

	public void setEsqTarfio(ArrayList<HashMap<String, Object>> esqTarfio) {
		this.esqTarfio = esqTarfio;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
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

	public ArrayList<HashMap<String, Object>> getAsocios() {
		return asocios;
	}

	public void setAsocios(ArrayList<HashMap<String, Object>> asocios) {
		this.asocios = asocios;
	}

	public ArrayList<HashMap<String, Object>> getTiposAnestesia() {
		return tiposAnestesia;
	}

	public void setTiposAnestesia(ArrayList<HashMap<String, Object>> tiposAnestesia) {
		this.tiposAnestesia = tiposAnestesia;
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

	public String getIndexEliminado() {
		return indexEliminado;
	}

	public void setIndexEliminado(String indexEliminado) {
		this.indexEliminado = indexEliminado;
	}

	public ArrayList<HashMap<String, Object>> getServicios() {
		return servicios;
	}

	public void setServicios(ArrayList<HashMap<String, Object>> servicios) {
		this.servicios = servicios;
	}

	public HashMap getAsociosXRangoTiempoEliminadoMap() {
		return asociosXRangoTiempoEliminadoMap;
	}

	public Object getAsociosXRangoTiempoEliminadoMap(String key) {
		return asociosXRangoTiempoEliminadoMap.get(key);
	}
	
	public void setAsociosXRangoTiempoEliminadoMap(
			HashMap asociosXRangoTiempoEliminadoMap) {
		this.asociosXRangoTiempoEliminadoMap = asociosXRangoTiempoEliminadoMap;
	}
	
	public void setAsociosXRangoTiempoEliminadoMap(String key,Object value) {
		this.asociosXRangoTiempoEliminadoMap.put(key, value);
	}

	public HashMap getAsociosXRangoTiempoClone() {
		return asociosXRangoTiempoClone;
	}

	public Object getAsociosXRangoTiempoClone(String key) {
		return asociosXRangoTiempoClone.get(key);
	}
	
	public void setAsociosXRangoTiempoClone(HashMap asociosXRangoTiempoClone) {
		this.asociosXRangoTiempoClone = asociosXRangoTiempoClone;
	}
	
	public void setAsociosXRangoTiempoClone(String key,Object value) {
		this.asociosXRangoTiempoClone.put(key, value);
	}

	public HashMap getDetalleCodigos() {
		return detalleCodigos;
	}
	
	public Object getDetalleCodigos(String key) {
		return detalleCodigos.get(key);
	}

	public void setDetalleCodigos(HashMap detalleCodigos) {
		this.detalleCodigos = detalleCodigos;
	}

	public void setDetalleCodigos(String key, Object value) {
		this.detalleCodigos.put(key, value);
	}
	
	public HashMap getTarifariosOficiales() {
		return tarifariosOficiales;
	}
	
	public Object getTarifariosOficiales(String key) {
		return tarifariosOficiales.get(key);
	}

	public void setTarifariosOficiales(HashMap tarifariosOficiales) {
		this.tarifariosOficiales = tarifariosOficiales;
	}
	
	public void setTarifariosOficiales(String key,Object value) {
		this.tarifariosOficiales.put(key, value);
	}
	
	
	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}
	
	
	public HashMap getAsociosXRangoTiempoAll() {
		return asociosXRangoTiempoAll;
	}

	public void setAsociosXRangoTiempoAll(HashMap asociosXRangoTiempoAll) {
		this.asociosXRangoTiempoAll = asociosXRangoTiempoAll;
	}
	
	
	public Object getAsociosXRangoTiempoAll(String key) {
		return asociosXRangoTiempoAll.get(key);
	}

	public void setAsociosXRangoTiempoAll(String key,Object value) {
		this.asociosXRangoTiempoAll.put(key, value);
	}
	
	public HashMap getFechas() {
		return fechas;
	}
	
	public Object getFechas(String key) {
		return fechas.get(key);
	}

	public void setFechas(String key,Object value) {
		this.fechas.put(key, value);
	}

	public void setFechas(HashMap fechas) {
		this.fechas = fechas;
	}
	
	public HashMap getFehcasEliminadoMap() {
		return fehcasEliminadoMap;
	}

	public void setFehcasEliminadoMap(HashMap fehcasEliminadoMap) {
		this.fehcasEliminadoMap = fehcasEliminadoMap;
	}

	
	public Object getFehcasEliminadoMap(String key) {
		return fehcasEliminadoMap.get(key);
	}

	public void setFehcasEliminadoMap(String key,Object value) {
		this.fehcasEliminadoMap.put(key, value);
	}
	

	public HashMap getFechasClone() {
		return fechasClone;
	}

	public void setFechasClone(HashMap fechasClone) {
		this.fechasClone = fechasClone;
	}

	
	public Object getFechasClone(String key) {
		return fechasClone;
	}

	public void setFechasClone(String key,Object value) {
		this.fechasClone.put(key, value);
	}

	
	public String getIndexDestino() {
		return indexDestino;
	}

	public void setIndexDestino(String indexDestino) {
		this.indexDestino = indexDestino;
	}
	
	
	public String getOrigen() {
		return origen;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}
	/*-----------------------------------------------------
	 * 			METODOS GETTERS AND SETTERS
	 -----------------------------------------------------*/
	
	/*---------------------------------------------------
	 * 						METODO VALIDATE
	 ------------------------------------------------------*/
	
	

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		errores=super.validate(mapping, request);
	
		boolean ban = true;
		if (estado.equals("buscar"))
		{/*
			// se valida que se escoja un criterio de busqueda
			if (this.getCriteriosBusqueda("CONVEN").equals("-1") && this.getCriteriosBusqueda("TARFIO").equals("-1") )
			{
				errores.add("descripcion",new ActionMessage("errors.required","Seleccionar uno de los dos campos"));
				this.setEstado("inicial");
				
			}
			
			//se valida si el criterio de busqueda es el convenio, de ser asi
			//es obligatorio que tenga fecha inicial y fecha final
			if (!this.getCriteriosBusqueda("CONVEN").equals("-1"))
			{
				//se verifica si la fecha inicial y final esta vacia
				if (this.getCriteriosBusqueda("fechaIni").equals("") && this.getCriteriosBusqueda("fechaFin").equals("") )
				{
					errores.add("descripcion",new ActionMessage("errors.required","La fecha Inicial y Final "));
					this.setEstado("inicial");
				}
				else
				{	//se varifica si la fecha inicial esta vacia
					if (this.getCriteriosBusqueda("fechaIni").equals(""))
					{
						errores.add("descripcion",new ActionMessage("errors.required","La fecha Inicial "));
						this.setEstado("inicial");
					}
					
					//se varifica si la fecha final esta vacia
					if (this.getCriteriosBusqueda("fechaFin").equals(""))
					{
						errores.add("descripcion",new ActionMessage("errors.required","La fecha Final "));
						this.setEstado("inicial");
					}
					else
					{
						//se verifica si la fecha final es menor que la fecha actual del sistema, de ser asi saca el error
						if( UtilidadFecha.esFechaMenorQueOtraReferencia(this.getCriteriosBusqueda("fechaFin").toString(),UtilidadFecha.getFechaActual()))
							errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual","Final "+this.getCriteriosBusqueda("fechaFin").toString(),"Actual "+UtilidadFecha.getFechaActual()));
						
						//se verifica si la fecha final es menor que la fecha inicial, de ser asi saca el error
						if( UtilidadFecha.esFechaMenorQueOtraReferencia(this.getCriteriosBusqueda("fechaFin").toString(),this.getCriteriosBusqueda("fechaIni")+""))
							errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual","Final "+this.getCriteriosBusqueda("fechaFin").toString(),"Inicial "+this.getCriteriosBusqueda("fechaIni")+""));
					}
					
						
				}
			}
			
			
				*/
		}
		else
			if (estado.equals("guardar"))
			{
				
			
				logger.info("\n\n ::::::::: ENTRO A GUARDAR ::::::::::::::::::");
				for (int i= 0; i < Integer.parseInt(this.getAsociosXRangoTiempo("numRegistros")+"");i++)
				{
					
					//se pregunta por los que no se encuentran en la BD
					//if ((this.getAsociosXRangoTiempo("estaBd_"+i)+"").equals(ConstantesBD.acronimoNo))
					//{
//*************************************************************************************************************
						//::::::::::::::.VALIDACION DEL CODIGO CUPS:::::::::::::::::::::::
						
						HashMap detalle = new HashMap();
						detalle = (HashMap)this.getAsociosXRangoTiempo("detalle_"+i);

					//se valida que por lo menos tenga el codigo CUPS digitado
						if (detalle.containsKey("numRegistros") && Integer.parseInt(detalle.get("numRegistros")+"")>0)
						{
							for (int j=0;j<Integer.parseInt(detalle.get("numRegistros")+"");j++)
							{
								if (detalle.containsKey("codTarOfi_"+j) && Integer.parseInt((detalle.get("codTarOfi_"+j)+""))==ConstantesBD.codigoTarifarioCups)
								{ 
									
									if (( detalle.get("valorCodigo_"+j)+"").equals(""))
									{ 
										errores.add("descripcion",new ActionMessage("errors.required","El Codigo Cups del registro "+(i+1)));
									}
								}
							}
						}
						else
							errores.add("descripcion",new ActionMessage("errors.required","El Codigo Cups del registro "+(i+1)));

							
									
//*************************************************************************************************		
									
					//::::::::::::::.VALIDACION TIPO TIEMPO BASE :::::::::::::::::::::::
					if ((this.getAsociosXRangoTiempo("tipoTiempoBase_"+i)+"").equals(""))
						errores.add("descripcion",new ActionMessage("errors.required","Tipo Tiempo Base del registro "+(i+1)));
					
					//::::::::::::::.VALIDACION ASOCIO:::::::::::::::::::::::
					if ((this.getAsociosXRangoTiempo("asocio_"+i)+"").equals(""))
						errores.add("descripcion",new ActionMessage("errors.required","Asocio del registro "+(i+1)));
					
					//::::::::::::::.VALIDACION LIQUIDAR POR:::::::::::::::::
					if ((this.getAsociosXRangoTiempo("liquidarpor_"+i)+"").equals(""))
						errores.add("descripcion",new ActionMessage("errors.required","Liquidar Por del registro "+(i+1)));
					
//*************************************************************************************************		
					//::::::::::::::.VALIDACION MINUTOS RANGO INICIAL:::::::::::::::::::::::
					//se valida de que minutos rango inicial es un numero.
					try 
					{
						Integer.parseInt(this.getAsociosXRangoTiempo("minRangIni_"+i)+"");
						
					} catch (Exception e) 
					
					{
						errores.add("descripcion",new ActionMessage("errors.integerMayorIgualQue","Minutos Rango Inicial del registro "+(i+1),"0"));
					}
					
					
					try 
					{
						//se valida que minutos rango inicial es un entero mayor que cero
						if (Integer.parseInt(this.getAsociosXRangoTiempo("minRangIni_"+i)+"")<0)
							errores.add("descripcion",new ActionMessage("errors.integerMayorIgualQue","Minutos Rango Inicial del registro "+(i+1),"0"));
					} catch (Exception e) 
					
					{
						
					}		
					
					
//*************************************************************************************************************					
					//::::::::::::::.VALIDACION MINUTOS RANGO FINAL::::::::::::::::::::::::
					
					//se valida de que minutos rango inicial es un numero.
					try 
					{
						Integer.parseInt(this.getAsociosXRangoTiempo("minRangFin_"+i).toString());
						
					} catch (Exception e) 
					
					{
						errores.add("descripcion",new ActionMessage("errors.integerMayorIgualQue","Minutos Rango Final del registro "+(i+1),"el Rango Inicial"));
					}
					
					
					try 
					{
						//se valida que minutos rango inicial es un entero mayor que cero
						if (Integer.parseInt(this.getAsociosXRangoTiempo("minRangFin_"+i)+"")<Integer.parseInt(this.getAsociosXRangoTiempo("minRangIni_"+i)+""))
							errores.add("descripcion",new ActionMessage("errors.integerMayorIgualQue","Minutos Rango Final del registro "+(i+1),"el Rango Inicial del registro "+(i+1)));
					} catch (Exception e) 
					
					{
						
					}	
//*********************************************************************************************************************************************
					//::::::::::::::.VALIDACION VALOR ASOCIO:::::::::::::::::::::::
					try 
					{
						Double.parseDouble(this.getAsociosXRangoTiempo("valorAsocio_"+i)+"");
						
					} catch (Exception e) 
					
					{
						ban=false;
						errores.add("descripcion",new ActionMessage("errors.float","El Valor Asocio "+this.getAsociosXRangoTiempo("valorAsocio_"+i)+" del registro "+(i+1)));
					}
					
					
				
						
//						aqui se valida si cumple con la cantidad de digitos enteros y decimales.			
					if (ban)
					{
						String [] tmp =UtilidadTexto.separarParteEnteraYDecimal(this.getAsociosXRangoTiempo("valorAsocio_"+i)+"");
					//aqui se valida la parte entera
						if (tmp[0].length()>11)
							errores.add("descripcion",new ActionMessage("error.cantidadParteEnteraGene", "valor del Asocio "+this.getAsociosXRangoTiempo("valorAsocio_"+i)+" del registro "+(i+1),"11"));
						//aqui se valida la parte decimal
						if (tmp[1].length()>2)
							errores.add("descripcion",new ActionMessage("error.cantidadParteDecimalGene","valor del Asocio "+this.getAsociosXRangoTiempo("valorAsocio_"+i)+" del registro "+(i+1),"2"));
							
						ban=true;
					}
					
//**************************************************************************************************************************************************					
					//::::::::::::::.VALIDACION MINUTOS FRACCION ADICIONAL:::::::::::::::::::::::
					
					//se valida de que minutos rango inicial es un numero.
					try 
					{
						if (!(this.getAsociosXRangoTiempo("minFracAdic_"+i)+"").equals(""))
							Integer.parseInt(this.getAsociosXRangoTiempo("minFracAdic_"+i)+"");
						
					} catch (Exception e) 
					
					{
						errores.add("descripcion",new ActionMessage("errors.integerMayorIgualQue","Minutos Fracción adicional del registro "+(i+1),"0"));
					}
					
					
					try 
					{
						//se valida que minutos rango inicial es un entero mayor que cero
						if (!(this.getAsociosXRangoTiempo("minFracAdic_"+i)+"").equals(""))
							if (Integer.parseInt(this.getAsociosXRangoTiempo("minFracAdic_"+i)+"")<0)
								errores.add("descripcion",new ActionMessage("errors.integerMayorIgualQue","Minutos Fracción adicional del registro "+(i+1),"0"));
					} catch (Exception e) 
					
					{
						
					}		
					
					
	//****************************************************************************************************************************************				
					//::::::::::::::.VALIDACION VALOR FRACCION ADICONAL:::::::::::::::::::::::

					try 
					{	
						if (!(this.getAsociosXRangoTiempo("minFracAdic_"+i)+"").equals(""))
						Double.parseDouble(this.getAsociosXRangoTiempo("valorFracAdic_"+i)+"");
						
					} catch (Exception e) 
					
					{
						ban=false;
						errores.add("descripcion",new ActionMessage("errors.float","El Valor Fracción Adicional "+this.getAsociosXRangoTiempo("valorFracAdic_"+i)+" del registro "+(i+1)));
					}
					
					//aqui se valida si cumple con la cantidad de digitos enteros y decimales.		
					if (!(this.getAsociosXRangoTiempo("minFracAdic_"+i)+"").equals(""))
						if (ban)
						{
							String [] tmp =UtilidadTexto.separarParteEnteraYDecimal(this.getAsociosXRangoTiempo("valorFracAdic_"+i)+"");
							//aqui se valida la parte entera
							if (tmp[0].length()>11)
								errores.add("descripcion",new ActionMessage("error.cantidadParteEnteraGene","valor de la Fracción adicional "+this.getAsociosXRangoTiempo("valorFracAdic_"+i)+" del registro "+(i+1),"11"));
							//aqui se valida la parte decimal
							if (tmp[1].length()>2)
								errores.add("descripcion",new ActionMessage("error.cantidadParteDecimalGene","valor de la Fracción adicional "+this.getAsociosXRangoTiempo("valorFracAdic_"+i)+" del registro "+(i+1),"2"));
								
							ban=true;
						}
						
					
			//	}
					
					
					
					
//					*************************************************************************************************************
					
					
					//se valida que sea de convenio
				
					for (int k= 0; k!=i && k < Integer.parseInt(this.getAsociosXRangoTiempo("numRegistros")+"");k++)
					{
						String comp="",comp2="";
						
						
					
						//::::::::::::::.VALIDACION DE CRUZE ENTRE RANGO MINUTOS::::::::::::::
					
							//cadena de comparacion con los datos que no pueden repetirse de la tupla
							comp=this.getAsociosXRangoTiempo("tipoCirugia_"+i)+""+this.getAsociosXRangoTiempo("tipoServicio_"+i)+""+
							this.getAsociosXRangoTiempo("tipoAnestesia_"+i)+""+this.getAsociosXRangoTiempo("asocio_"+i)
							+""+this.getAsociosXRangoTiempo("tipoTiempoBase_"+i); 
							
							comp2=this.getAsociosXRangoTiempo("tipoCirugia_"+k)+""+this.getAsociosXRangoTiempo("tipoServicio_"+k)+""+
							this.getAsociosXRangoTiempo("tipoAnestesia_"+k)+""+this.getAsociosXRangoTiempo("asocio_"+k)
							+""+this.getAsociosXRangoTiempo("tipoTiempoBase_"+k);
						
						logger.info("\n\n::::::::::::::::LAS CADENAS A COMPARAR SON: ==>");
							logger.info("\n*********** COMP ==>"+comp);
							logger.info("\n **********COMP2 ==>"+comp2);
							
							if (comp.equals(comp2))
							{
								logger.info("\n\n::: ENTRE A LA COMPARACION *** 1");
								int ranIniOri=0, ranFinOri=0,ranIni=0, ranFin=0;
								
								//se valida que los rangos no esten vacios
								if (!(this.getAsociosXRangoTiempo("minRangIni_"+i)+"").equals("") && !(this.getAsociosXRangoTiempo("minRangIni_"+k)+"").equals("") && !(this.getAsociosXRangoTiempo("minRangFin_"+i)+"").equals("") && !(this.getAsociosXRangoTiempo("minRangIni_"+k)+"").equals("") && !(this.getAsociosXRangoTiempo("minRangFin_"+k)+"").equals(""))
								{	
									logger.info("\n el mapa --> "+this.getAsociosXRangoTiempo()+" i-->"+i+"  k-->"+k);
									logger.info("\n\n::: ENTRE A LA COMPARACION *** 2");
									try 
									{
										ranIni=Integer.parseInt(this.getAsociosXRangoTiempo("minRangIni_"+i)+"");
										ranFin=Integer.parseInt(this.getAsociosXRangoTiempo("minRangFin_"+i)+"");
										ranIniOri=Integer.parseInt(this.getAsociosXRangoTiempo("minRangIni_"+k)+"");
										ranFinOri=Integer.parseInt(this.getAsociosXRangoTiempo("minRangFin_"+k)+"");
										logger.info("\n\n::: ENTRE A LA COMPARACION *** 3");
										
									} catch (Exception e) 
									
									{
										logger.error("\n:* problema al pasar los minutos de rango a entero ");
									}
									
									if (ranIni<=ranFinOri)
									{
										logger.info("\n\n::: ENTRE A LA COMPARACION *** 4");
										String [] mensaje={"minutos",ranIni+"",ranFin+" del registro "+(i+1),"minutos",ranIniOri+"",ranFinOri+" del registro "+(k+1)+"."};
										errores.add("descripcion",new ActionMessage("error.rangoGenerico",mensaje));
									}
								}
							}
								
								
					
					}
											
											
			}
		
		}
		
		return errores;
	}

	

	

	
	
	
	
}