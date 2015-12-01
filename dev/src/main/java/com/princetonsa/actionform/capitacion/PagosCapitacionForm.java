/*
 * Creado en Jul 6, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.actionform.capitacion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadCadena;
import util.UtilidadFecha;

/**
 * Form de Aplicación de Pagos de Capitación
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales) 
 * @version Jul 6, 2006
 */
public class PagosCapitacionForm extends ValidatorForm
{
	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Estado para el manejo del flujo de la funcionalidad
	 */
	private String estado;
	
	/**
	 * Estado anterior para que no se vuelva abrir la página de imprimir
	 * al hacer el back
	 */
	private String estadoAnterior;
	
//	---------------------------------------------------DECLARACIÓN DE LOS ATRIBUTOS-----------------------------------------------------------//
	
	/**
     * Mapa que contiene los documentos pendientes de aplicar aprobar pagos 
     * de convenios capitados
     */
    private HashMap mapaDocumentos;
    
    /**
     * Código del índice del documento de pago que se modificara o aplicará.
     */
    private int indiceDocumentoPago;
    
    /**
     * Mapa que contiene los conceptos de pago del documento
     */
    private HashMap mapaConceptosPago;
    
    /**
     * Mapa que contiene los conceptos de pago del documento
     * para las aplicaciones aprobadas y mostrarlas de consulta
     */
    private HashMap mapaConceptosPagoAprobados;
    
    /**
     * Observación de la aplicación del pago de capitación
     */
    private String observacionesAplicacion;
    
    /**
     * Fecha de la aplicacion del pago
     */
    private String fechaAplicacion;
    
    /**
     * Posicion del concepto que se eliminará
     */
    private int conEliminar;
    
    /**
     * Motivo por el que se anula una aplicacion
     */
    private String motivoAnulacion;
    
    //---------------Detalle de pago por cuenta de cobro de capitación ------------//
    /**
     * Campo que guarda la cuenta de cobro digitada para buscar en el detalle
     * de pago de por cuenta de cobro
     */
    private String cuentaCobroBuscar;
    
    /**
     * Mapa para manejar las aplicaciones de pagos del grupo de cuentas de cobro
     * de capitación
     */
    private HashMap mapaPagosCXC;
    
    /**
     * Mapa que contiene las cuentas de cobro para la busqueda de las cuentas de cobro
     * de capitación
     */
    private HashMap mapaBusquedaCXC;
    
    /**
     * Código de la cuenta de cobro e buscar en el popup de cuentas cobro
     */
    private String cxcBusAvanzada;
    
//  --------------------Pager del listado ------------------------------//
    /**
     * Número de registros por pager
     */
    private int maxPageItems;
    
    /**
     * Variables para manejar el paginador
     */
    private int index;
	private String linkSiguiente;
	
	/**
     * Almacena el indice por el cual 
     * se va ordenar el HashMap
     */
    private String patronOrdenar;
    
    /**
     * Almacena el ultimo indice por el 
     * cual se ordeno el HashMap
     */
    private String ultimoPatron;
    
    //----------------------------Búsqueda Avanzada Aplicación Pagos y Aprobación ----------------------------//
    /**
     * Tipo del documento por el que se hace la busqueda(Recibos Caja - RC )
     * Se utiliza en busqueda avanzada de aplicación pagos y en la búsqueda de la aprobación
     */
    private int tipoDocBusqueda;
    
    /**
     * Número del documento por el que se hace la busquenda.
     * Se utiliza en busqueda avanzada de aplicación pagos y en la búsqueda de la aprobación
     */
    private String documentoBusqueda;
    
    /**
     * Fecha del documento para hacer la busqueda.
     */
    private String fechaDocBusqueda;
    
    /**
     * Convenio para busqueda.
     * Se utiliza en busqueda avanzada de aplicación pagos y en la búsqueda de la aprobación
     */
    private int convenioBusqueda;
    
    /**
     * Convenio para busqueda.
     * Se utiliza en la búsqueda de la aprobación
     */
    private String consecutivoPagoBusqueda;
    
    //----------------------------------------------Aprobación de Pagos de Capitación -----------------------------------------------------------//
    /**
     * Mapa para manejar las aplicaciones de pago en estado pendiente, para 
     * realizarles la aprobacion
     */
    private HashMap mapaApliPagos;
    
    /**
     * Código de la aplicación de pago que se va a aprobar
     */
    private String codigoAplicacionAprobar;
    
    /**
     * Fecha de aprobación de la aplicación de pago
     */
    private String fechaAprobacionPago;
    
    /**
     * Indice de la aplicación de pago seleccionada para aprobar
     */
    private int indiceAprobacionPago;
    
    /**
     * Mapa con el detalle de pagos por CxC de la
     * aplicación ya sea aprobada o consultadas
     */
    private HashMap mapaDetallePagos;
    
    //----------------------------------------------Consulta de Pagos de Capitación ---------------------------------------------------------------//
    /**
     * Estado de pago en la búsqueda de la consulta de pagos capitación
     */
    private int estadoPagoBusqueda;
    
    /**
     * Mapa con el listado de pagos consultados
     */
    private HashMap mapaConsultaPagos;
    
    /**
     * Indice de la aplicación de pago que se va ha imprimir
     */
    private int indiceImprimirPago;
    
    private ResultadoBoolean mostrarMensaje=new ResultadoBoolean(false,"");
    
  //----------------------------------------------------FINAL DE LA DECLARACIÓN DE ATRIBUTOS ------------------------------------------//
    /**
	 * Método para limpiar los atributos de la clase
	 */
	public void reset()
	{
		this.mapaDocumentos=new HashMap();
		this.mapaDocumentos.put("numRegistros", "0");
		this.mapaConceptosPago=new HashMap();
		this.mapaConceptosPago.put("numRegistros", "0");
		this.mapaConceptosPagoAprobados=new HashMap();
		this.mapaConceptosPagoAprobados.put("numRegistros", "0");
		
		this.indiceDocumentoPago=ConstantesBD.codigoNuncaValido;
		this.observacionesAplicacion="";
		this.fechaAplicacion=UtilidadFecha.getFechaActual();
		this.conEliminar=ConstantesBD.codigoNuncaValido;
		this.motivoAnulacion="";
		
		//---------Para Ordenar y paginar----------------//
		this.maxPageItems=0;
		this.patronOrdenar="";
    	this.ultimoPatron="";
    	
    	//---------Búsqueda Avanzada Aplicación pagos-------------//
    	this.tipoDocBusqueda=ConstantesBD.codigoNuncaValido;
        this.documentoBusqueda="";
        this.fechaDocBusqueda="";
        this.convenioBusqueda=ConstantesBD.codigoNuncaValido;
        
        //-------Detalle de pagos por cuenta de cobro --------//
        this.cuentaCobroBuscar="";
        this.cxcBusAvanzada="";
        this.mapaBusquedaCXC=new HashMap();
        this.mapaBusquedaCXC.put("numRegistros", "0");
        this.mapaPagosCXC=new HashMap();
        this.mapaPagosCXC.put("numRegistros", "0");
        
        //-------------Aprobación de Pagos ----------------//
        this.consecutivoPagoBusqueda = "";
        this.mapaApliPagos=new HashMap();
        this.mapaApliPagos.put("numRegistros", "0");
        this.codigoAplicacionAprobar="";
        this.fechaAprobacionPago=UtilidadFecha.getFechaActual();
        this.indiceAprobacionPago=ConstantesBD.codigoNuncaValido;
        this.mapaDetallePagos=new HashMap();
        this.mapaDetallePagos.put("numRegistros", "0");
        
        //----------Consulta de Pagos de Capitación ------------------------//
        this.estadoPagoBusqueda=ConstantesBD.codigoNuncaValido;
        this.mapaConsultaPagos=new HashMap();
        this.mapaConsultaPagos.put("numRegistros", "0");
        this.indiceImprimirPago=ConstantesBD.codigoNuncaValido;
        
        this.estadoAnterior="";
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
		
		//-----Si el estado es guardar conceptos de pago de la aplicación ---//
		if(estado.equals("guardarAplicacionConceptos"))
		{
			//----------Se valida la fecha de aplicación de pagos ------------//
			if (!UtilidadCadena.noEsVacio(fechaAplicacion))
				{
				errores.add("Fecha Aplicacion vacio", new ActionMessage("errors.required","La Fecha de Aplicación"));
				}
			else if (!UtilidadFecha.validarFecha(fechaAplicacion))
				{
				errores.add("Fecha Aplicación Invalido", new ActionMessage("errors.formatoFechaInvalido", " de Aplicación"));
				}
			else 
				{
				//---- Validar que la fecha de aplicación sea menor o igual a la fecha del sistema-----//
				if((UtilidadFecha.conversionFormatoFechaABD(this.fechaAplicacion)).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
					{
						errores.add("fechaAplicacionMenor", new ActionMessage("errors.fechaPosteriorIgualAOtraDeReferencia", "de aplicación", "actual"));
					}
				//---- Validar que la fecha de aplicación sea mayor o igual a la fecha de generación del documento-----//
				if((UtilidadFecha.conversionFormatoFechaABD(this.fechaAplicacion)).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.mapaDocumentos.get("fecha_documento_"+indiceDocumentoPago)+""))<0)
					{
						errores.add("fechaAplicacionMayor", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia", "de aplicación", "de generación del documento"));
					}
				}
			
			if(this.mapaConceptosPago!=null)
			{
				int nroRegistros=Integer.parseInt(this.mapaConceptosPago.get("numRegistros")+"");
				
				for(int i=0; i<nroRegistros; i++)
				{
					//------Se verifica el valor base ------------//
					if(UtilidadCadena.noEsVacio(this.mapaConceptosPago.get("valor_base_"+i)+""))
					{
						double valorBase=Double.parseDouble(this.mapaConceptosPago.get("valor_base_"+i)+"");
						if(valorBase < 0)
						{
							errores.add("Mayor/igual Cero", new ActionMessage("errors.MayorIgualQue", "El Valor Base ["+(i+1)+"]", "0"));
						}
					}
					else
					{
						errores.add("Valor Base vacio", new ActionMessage("errors.required","El Valor Base ["+(i+1)+"]"));
					}
					
					//------Se verifica el porcentaje ------------//
					if(UtilidadCadena.noEsVacio(this.mapaConceptosPago.get("porcentaje_"+i)+""))
					{
						int resp=UtilidadCadena.validarPorcentajeString((this.mapaConceptosPago.get("porcentaje_"+i)+""), 2, ".");
						
						//float porcentaje=Float.parseFloat(this.mapaConceptosPago.get("porcentaje_"+i)+"");
						//if(porcentaje < 0)
						if(resp==1 || resp==4)
						{
							errores.add("Mayor/igual Cero", new ActionMessage("errors.MayorIgualQue", "El Porcentaje ["+(i+1)+"]", "0"));
						}
						//else if(porcentaje > 100)
						if(resp==2 || resp==5)
						{
							errores.add("Menor/igual Cien", new ActionMessage("errors.MenorIgualQue", "El Porcentaje ["+(i+1)+"]", "100"));
						}
						
						if(resp==3 || resp==4 || resp==5)
						{
							errores.add("Decimales Porcentaje", new ActionMessage("errors.numDecimales", "El Porcentaje ["+(i+1)+"]", "2"));
						}
													
					}//if
					else
					{
						errores.add("Porcentaje vacio", new ActionMessage("errors.required","El Porcentaje ["+(i+1)+"]"));
					}
					//------Se verifica el valor concepto ------------//
					if(UtilidadCadena.noEsVacio(this.mapaConceptosPago.get("valor_concepto_"+i)+""))
					{
						double valorConcepto=Double.parseDouble(this.mapaConceptosPago.get("valor_concepto_"+i)+"");
						if(valorConcepto <= 0)
						{
							errores.add("Mayo Cero", new ActionMessage("errors.MayorQue", "El Valor Concepto ["+(i+1)+"]", "0"));
						}
					}
					else
					{
						errores.add("ValorConcepto vacio", new ActionMessage("errors.required","El Valor Concepto ["+(i+1)+"]"));
					}
				}//for
			}//if mapaConceptosPago!=null
		}//if estado es guardarConceptos
		
		//-----Si el estado es buscar aplicación pagos aprobar  ---//
		if(estado.equals("buscarPagosAprobar"))
			{
			//---------------------Se realiza la validación de los parámetros de búsqueda --------------------------------------//
			if (this.tipoDocBusqueda==ConstantesBD.codigoNuncaValido && !UtilidadCadena.noEsVacio(this.documentoBusqueda) && !UtilidadCadena.noEsVacio(this.consecutivoPagoBusqueda) && this.convenioBusqueda==ConstantesBD.codigoNuncaValido)
			{
				errores.add("noHayParametros", new ActionMessage("errors.requridoMinimoUnParametroParaEjecutarConsulta", "UN"));
			}
				
			}//if buscarPagosAprobar
		
		//-----Si el estado es buscar pagos de consulta  ---//
		if(estado.equals("buscarPagosConsulta"))
			{
			//---------------------Se realiza la validación de los parámetros de búsqueda --------------------------------------//
			if (this.tipoDocBusqueda==ConstantesBD.codigoNuncaValido && !UtilidadCadena.noEsVacio(this.documentoBusqueda) && !UtilidadCadena.noEsVacio(this.consecutivoPagoBusqueda) && this.estadoPagoBusqueda==ConstantesBD.codigoNuncaValido && this.convenioBusqueda==ConstantesBD.codigoNuncaValido)
			{
				errores.add("noHayParametros", new ActionMessage("errors.requridoMinimoUnParametroParaEjecutarConsulta", "UN"));
			}
				
			}//if buscarPagosAprobar
		
		return errores;
		
	}

//	-------------------------------------------------------------SETS Y GETS----------------------------------------------------------------//
	
	/**
	 * @return Retorna the estado.
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	/**
	 * @return Retorna the index.
	 */
	public int getIndex()
	{
		return index;
	}

	/**
	 * @param index The index to set.
	 */
	public void setIndex(int index)
	{
		this.index = index;
	}

	/**
	 * @return Retorna the linkSiguiente.
	 */
	public String getLinkSiguiente()
	{
		return linkSiguiente;
	}

	/**
	 * @param linkSiguiente The linkSiguiente to set.
	 */
	public void setLinkSiguiente(String linkSiguiente)
	{
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * @return Retorna the mapaDocumentos.
	 */
	public HashMap getMapaDocumentos()
	{
		return mapaDocumentos;
	}

	/**
	 * @param mapaDocumentos The mapaDocumentos to set.
	 */
	public void setMapaDocumentos(HashMap mapaDocumentos)
	{
		this.mapaDocumentos = mapaDocumentos;
	}
	
	/**
	 * @return Retorna mapaDocumentos.
	 */
	public Object getMapaDocumentos(String key) {
		return mapaDocumentos.get( key );
	}

	/**
	 * @param Asigna mapaDocumentos.
	 */
	public void setMapaDocumentos(String key, String dato) {
		this.mapaDocumentos.put(key, dato);
	}

	/**
	 * @return Retorna the maxPageItems.
	 */
	public int getMaxPageItems()
	{
		return maxPageItems;
	}

	/**
	 * @param maxPageItems The maxPageItems to set.
	 */
	public void setMaxPageItems(int maxPageItems)
	{
		this.maxPageItems = maxPageItems;
	}

	/**
	 * @return Retorna the patronOrdenar.
	 */
	public String getPatronOrdenar()
	{
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar The patronOrdenar to set.
	 */
	public void setPatronOrdenar(String patronOrdenar)
	{
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return Retorna the ultimoPatron.
	 */
	public String getUltimoPatron()
	{
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatron The ultimoPatron to set.
	 */
	public void setUltimoPatron(String ultimoPatron)
	{
		this.ultimoPatron = ultimoPatron;
	}

	/**
	 * @return Retorna the convenioBusqueda.
	 */
	public int getConvenioBusqueda()
	{
		return convenioBusqueda;
	}

	/**
	 * @param convenioBusqueda The convenioBusqueda to set.
	 */
	public void setConvenioBusqueda(int convenioBusqueda)
	{
		this.convenioBusqueda = convenioBusqueda;
	}

	/**
	 * @return Retorna the documentoBusqueda.
	 */
	public String getDocumentoBusqueda()
	{
		return documentoBusqueda;
	}

	/**
	 * @param documentoBusqueda The documentoBusqueda to set.
	 */
	public void setDocumentoBusqueda(String documentoBusqueda)
	{
		this.documentoBusqueda = documentoBusqueda;
	}

	/**
	 * @return Retorna the fechaDocBusqueda.
	 */
	public String getFechaDocBusqueda()
	{
		return fechaDocBusqueda;
	}

	/**
	 * @param fechaDocBusqueda The fechaDocBusqueda to set.
	 */
	public void setFechaDocBusqueda(String fechaDocBusqueda)
	{
		this.fechaDocBusqueda = fechaDocBusqueda;
	}

	/**
	 * @return Retorna the tipoDocBusqueda.
	 */
	public int getTipoDocBusqueda()
	{
		return tipoDocBusqueda;
	}

	/**
	 * @param tipoDocBusqueda The tipoDocBusqueda to set.
	 */
	public void setTipoDocBusqueda(int tipoDocBusqueda)
	{
		this.tipoDocBusqueda = tipoDocBusqueda;
	}

	/**
	 * @return Retorna the indiceDocumentoPago.
	 */
	public int getIndiceDocumentoPago()
	{
		return indiceDocumentoPago;
	}

	/**
	 * @param indiceDocumentoPago The indiceDocumentoPago to set.
	 */
	public void setIndiceDocumentoPago(int indiceDocumentoPago)
	{
		this.indiceDocumentoPago = indiceDocumentoPago;
	}

	/**
	 * @return Retorna the mapaConceptosPago.
	 */
	public HashMap getMapaConceptosPago()
	{
		return mapaConceptosPago;
	}

	/**
	 * @param mapaConceptosPago The mapaConceptosPago to set.
	 */
	public void setMapaConceptosPago(HashMap mapaConceptosPago)
	{
		this.mapaConceptosPago = mapaConceptosPago;
	}	
	
	/**
	 * @return Retorna mapaConceptosPago.
	 */
	public Object getMapaConceptosPago(String key) {
		return mapaConceptosPago.get( key );
	}

	/**
	 * @param Asigna mapaConceptosPago.
	 */
	public void setMapaConceptosPago(String key, String dato) {
		this.mapaConceptosPago.put(key, dato);
	}

	/**
	 * @return Retorna the observacionesAplicacion.
	 */
	public String getObservacionesAplicacion()
	{
		return observacionesAplicacion;
	}

	/**
	 * @param observacionesAplicacion The observacionesAplicacion to set.
	 */
	public void setObservacionesAplicacion(String observacionesAplicacion)
	{
		this.observacionesAplicacion = observacionesAplicacion;
	}

	/**
	 * @return Retorna the fechaAplicacion.
	 */
	public String getFechaAplicacion()
	{
		return fechaAplicacion;
	}

	/**
	 * @param fechaAplicacion The fechaAplicacion to set.
	 */
	public void setFechaAplicacion(String fechaAplicacion)
	{
		this.fechaAplicacion = fechaAplicacion;
	}

	/**
	 * @return Retorna the conEliminar.
	 */
	public int getConEliminar()
	{
		return conEliminar;
	}

	/**
	 * @param conEliminar The conEliminar to set.
	 */
	public void setConEliminar(int conEliminar)
	{
		this.conEliminar = conEliminar;
	}

	/**
	 * @return Retorna the motivoAnulacion.
	 */
	public String getMotivoAnulacion()
	{
		return motivoAnulacion;
	}

	/**
	 * @param motivoAnulacion The motivoAnulacion to set.
	 */
	public void setMotivoAnulacion(String motivoAnulacion)
	{
		this.motivoAnulacion = motivoAnulacion;
	}

	/**
	 * @return Retorna the cuentaCobroBuscar.
	 */
	public String getCuentaCobroBuscar()
	{
		return cuentaCobroBuscar;
	}

	/**
	 * @param cuentaCobroBuscar The cuentaCobroBuscar to set.
	 */
	public void setCuentaCobroBuscar(String cuentaCobroBuscar)
	{
		this.cuentaCobroBuscar = cuentaCobroBuscar;
	}

	/**
	 * @return Retorna the mapaBusquedaCXC.
	 */
	public HashMap getMapaBusquedaCXC()
	{
		return mapaBusquedaCXC;
	}

	/**
	 * @param mapaBusquedaCXC The mapaBusquedaCXC to set.
	 */
	public void setMapaBusquedaCXC(HashMap mapaBusquedaCXC)
	{
		this.mapaBusquedaCXC = mapaBusquedaCXC;
	}
	
	/**
	 * @return Retorna mapaBusquedaCXC.
	 */
	public Object getMapaBusquedaCXC(String key) {
		return mapaBusquedaCXC.get( key );
	}

	/**
	 * @param Asigna mapaBusquedaCXC.
	 */
	public void setMapaBusquedaCXC(String key, String dato) {
		this.mapaBusquedaCXC.put(key, dato);
	}

	/**
	 * @return Retorna the mapaPagosCXC.
	 */
	public HashMap getMapaPagosCXC()
	{
		return mapaPagosCXC;
	}

	/**
	 * @param mapaPagosCXC The mapaPagosCXC to set.
	 */
	public void setMapaPagosCXC(HashMap mapaPagosCXC)
	{
		this.mapaPagosCXC = mapaPagosCXC;
	}
	
	/**
	 * @return Retorna mapaPagosCXC.
	 */
	public Object getMapaPagosCXC(String key) {
		return mapaPagosCXC.get( key );
	}

	/**
	 * @param Asigna mapaPagosCXC.
	 */
	public void setMapaPagosCXC(String key, String dato) {
		this.mapaPagosCXC.put(key, dato);
	}

	/**
	 * @return Retorna the cxcBusAvanzada.
	 */
	public String getCxcBusAvanzada()
	{
		return cxcBusAvanzada;
	}

	/**
	 * @param cxcBusAvanzada The cxcBusAvanzada to set.
	 */
	public void setCxcBusAvanzada(String cxcBusAvanzada)
	{
		this.cxcBusAvanzada = cxcBusAvanzada;
	}

	/**
	 * @return Retorna the consecutivoPagoBusqueda.
	 */
	public String getConsecutivoPagoBusqueda()
	{
		return consecutivoPagoBusqueda;
	}

	/**
	 * @param consecutivoPagoBusqueda The consecutivoPagoBusqueda to set.
	 */
	public void setConsecutivoPagoBusqueda(String consecutivoPagoBusqueda)
	{
		this.consecutivoPagoBusqueda = consecutivoPagoBusqueda;
	}

	/**
	 * @return Retorna the mapaApliPagos.
	 */
	public HashMap getMapaApliPagos()
	{
		return mapaApliPagos;
	}

	/**
	 * @param mapaApliPagos The mapaApliPagos to set.
	 */
	public void setMapaApliPagos(HashMap mapaApliPagos)
	{
		this.mapaApliPagos = mapaApliPagos;
	}

	/**
	 * @return Retorna mapaApliPagos.
	 */
	public Object getMapaApliPagos(String key) {
		return mapaApliPagos.get( key );
	}

	/**
	 * @param Asigna mapaApliPagos.
	 */
	public void setMapaApliPagos(String key, String dato) {
		this.mapaApliPagos.put(key, dato);
	}

	/**
	 * @return Retorna the codigoAplicacionAprobar.
	 */
	public String getCodigoAplicacionAprobar()
	{
		return codigoAplicacionAprobar;
	}

	/**
	 * @param codigoAplicacionAprobar The codigoAplicacionAprobar to set.
	 */
	public void setCodigoAplicacionAprobar(String codigoAplicacionAprobar)
	{
		this.codigoAplicacionAprobar = codigoAplicacionAprobar;
	}

	/**
	 * @return Retorna the fechaAprobacionPago.
	 */
	public String getFechaAprobacionPago()
	{
		return fechaAprobacionPago;
	}

	/**
	 * @param fechaAprobacionPago The fechaAprobacionPago to set.
	 */
	public void setFechaAprobacionPago(String fechaAprobacionPago)
	{
		this.fechaAprobacionPago = fechaAprobacionPago;
	}

	/**
	 * @return Retorna the indiceAprobacionPago.
	 */
	public int getIndiceAprobacionPago()
	{
		return indiceAprobacionPago;
	}

	/**
	 * @param indiceAprobacionPago The indiceAprobacionPago to set.
	 */
	public void setIndiceAprobacionPago(int indiceAprobacionPago)
	{
		this.indiceAprobacionPago = indiceAprobacionPago;
	}

	/**
	 * @return Retorna the estadoPagoBusqueda.
	 */
	public int getEstadoPagoBusqueda()
	{
		return estadoPagoBusqueda;
	}

	/**
	 * @param estadoPagoBusqueda The estadoPagoBusqueda to set.
	 */
	public void setEstadoPagoBusqueda(int estadoPagoBusqueda)
	{
		this.estadoPagoBusqueda = estadoPagoBusqueda;
	}

	/**
	 * @return Retorna the mapaConsultaPagos.
	 */
	public HashMap getMapaConsultaPagos()
	{
		return mapaConsultaPagos;
	}

	/**
	 * @param mapaConsultaPagos The mapaConsultaPagos to set.
	 */
	public void setMapaConsultaPagos(HashMap mapaConsultaPagos)
	{
		this.mapaConsultaPagos = mapaConsultaPagos;
	}
	
	/**
	 * @return Retorna mapaConsultaPagos.
	 */
	public Object getMapaConsultaPagos(String key) {
		return mapaConsultaPagos.get( key );
	}

	/**
	 * @param Asigna mapaConsultaPagos.
	 */
	public void setMapaConsultaPagos(String key, String dato) {
		this.mapaConsultaPagos.put(key, dato);
	}

	/**
	 * @return Retorna the indiceImprimirPago.
	 */
	public int getIndiceImprimirPago()
	{
		return indiceImprimirPago;
	}

	/**
	 * @param indiceImprimirPago The indiceImprimirPago to set.
	 */
	public void setIndiceImprimirPago(int indiceImprimirPago)
	{
		this.indiceImprimirPago = indiceImprimirPago;
	}

	/**
	 * @return Retorna the mapaDetallePagos.
	 */
	public HashMap getMapaDetallePagos()
	{
		return mapaDetallePagos;
	}

	/**
	 * @param mapaDetallePagos The mapaDetallePagos to set.
	 */
	public void setMapaDetallePagos(HashMap mapaDetallePagos)
	{
		this.mapaDetallePagos = mapaDetallePagos;
	}
	
	/**
	 * @return Retorna mapaDetallePagos.
	 */
	public Object getMapaDetallePagos(String key) {
		return mapaDetallePagos.get( key );
	}

	/**
	 * @param Asigna mapaConsultaPagos.
	 */
	public void setMapaDetallePagos(String key, String dato) {
		this.mapaDetallePagos.put(key, dato);
	}

	/**
	 * @return Retorna the estadoAnterior.
	 */
	public String getEstadoAnterior()
	{
		return estadoAnterior;
	}

	/**
	 * @param estadoAnterior The estadoAnterior to set.
	 */
	public void setEstadoAnterior(String estadoAnterior)
	{
		this.estadoAnterior = estadoAnterior;
	}
	
	/**
	 * @return Retorna the mapaConceptosPagoAprobados.
	 */
	public HashMap getMapaConceptosPagoAprobados()
	{
		return mapaConceptosPagoAprobados;
	}

	/**
	 * @param mapaConceptosPagoAprobados The mapaConceptosPagoAprobados to set.
	 */
	public void setMapaConceptosPagoAprobados(HashMap mapaConceptosPagoAprobados)
	{
		this.mapaConceptosPagoAprobados = mapaConceptosPagoAprobados;
	}	
	
	/**
	 * @return Retorna mapaConceptosPagoAprobados.
	 */
	public Object getMapaConceptosPagoAprobados(String key) {
		return mapaConceptosPagoAprobados.get( key );
	}

	/**
	 * @param Asigna mapaConceptosPago.
	 */
	public void setMapaConceptosPagoAprobados(String key, String dato) {
		this.mapaConceptosPagoAprobados.put(key, dato);
	}

	public ResultadoBoolean getMostrarMensaje() {
		return mostrarMensaje;
	}

	public void setMostrarMensaje(ResultadoBoolean mostrarMensaje) {
		this.mostrarMensaje = mostrarMensaje;
	}

	
}
