
/*
 * Creado   11/04/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.actionform.cartera;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.princetonsa.dto.administracion.DtoFirmasValoresPorDefecto;
import com.princetonsa.mundo.facturacion.ValidacionesFactura;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * Clase para manejar
 *
 * @version 1.0, 11/04/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 * @author <a href="mailto:armando@PrincetonSA.com">Armando Osorio</a> 
 * @author <a href="mailto:sgomez@PrincetonSA.com">Sebastian Gomez</a>
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 */
public class CuentasCobroForm extends ActionForm 
{
	private Logger logger = Logger.getLogger(CuentasCobroForm.class);
    
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
    /**
     * almacena los diferentes estados, manejados
     * para generación CXC, movimientos CXC,
     * radicación CXC e inactivación factura.
     */
    private String estado;
    
    /**
     * almacena las diferentes acciones, manejados
     * para generación CXC, movimientos CXC,
     * radicación CXC e inactivación factura.
     */
    private String accion;
    
    /**
     * almacena el numero de la cuenta de cobro.
     */
    private double numCuentaCobro;
    
    /**
     * almacena las observaciones.
     */
    private String observaciones; 
    
    /**
     * almacena el usuario que realiza el movimiento
     */
    private String usuarioMovimiento;
    
    
    /**
     * Coleccion donde almaceno todas las facturas relacionadas
     * a un numero de cuenta de cobro
     */
     private Collection detalleFacturas=null;
     
     /**
      * almacena la observacion del respectivo
      * movimiento.
      */
     private String observacionMovimiento;
     
     /**
      * almacena el codigo de la via de
      * ingreso
      */
     private int viaIngreso;
     
     /**
      * para almacenar valores temporales
      * del formulario.
      */
     private String propiedadTempS;
     
     /**
      * para almacenar valores temporales
      * del formulario.
      */
     private int propiedadTempI;
     
     /**
      * Mapa para almacenar datos de la funcionalidad de
      * movimientos de cxc.
      */
     private HashMap mapMovimientos;
     
     /**
      * almacena el codigo del convenio
      */
     private int codigoConvenio;
     
     /**
      * Variable para manejar el valor inicial de la cuenta de cobro. 
      **/
     private double valorInicialCuenta;
     
     /**
      * Numero de Registro que se mostraran en el pager. 
      **/
     private int maxPageItems=10;
     
     /**
 	 * Este campo contiene el pageUrl para controlar el pager,
 	 *  y conservar los valores del hashMap mediante un submit de
 	 * JavaScript. (Integra pager -Valor Captura)
 	 */
     private String linkSiguiente;
     
     /**
 	 * Offset para el pager 
 	 */
 	private int offset=0;
 	
 	/**
 	 * Boolean para indicar si se estan seleccionando todas o des-seleccionando
 	 */
 	private boolean selecTodas;
     
 	/**
 	 * Numero de facturas que no se tomaro en cuenta.
 	 */
 	private int numeroFacturasNoAptas;
 	
 	/**
 	 * Numero de facturas Seleccionadas.
 	 */
 	private int numeroFacturasSeleccionadas;
 	
 	/**
 	 * Parametro que indica si debo mostrar el mensaje de que la cuenta no tiene facturas.
 	 */
 	private boolean mostrarMensaje;
 	
 	/**
 	 * almacena el log de movimientos CXC
 	 */
 	private String log;
 	
 	/**
 	 * almacena el motivo de la anulación de la cxc
 	 */
 	private String motivoAnulacion;
 	
 	/**
 	 * para almacenar las validaciones de 
 	 * devolucion de CXC
 	 */
 	private HashMap mapValidaciones;
 	
 	/**
 	 * Collection para las anulaciones de una cuenta de cobro
 	 */
 	private Collection anulacionCxC;

    //********************BUSQUEDA AVANZADA************************
    
    private Collection impresionDetallada;
    
    private int institucion;
    
    
    /**
     * Centro de atencion,
     * la informacion se encuentra de la siguiente manera:
     * codigo@@@@@descripcion
     */
    private String centroAtencion;
    /**
     * 
     */
    private int codigoCentroAtencion;
    
    /**
     * 
     */
    private String nombreCentroAtencion;
 	
    /**Attributos necesarion para el PopOut en la consulta de una cuenta de cobro
     * cuando su estado es anulada
    */
    private String fechaAnulacion;
    private String horaAnulacion;
    private String usuarioAnulacion;
    private String tipoAnulacion;
    private String criterio;
    private String textoBusqueda;
   
  
    //  ***********ATRIBUTOS ADICIONALES PARA INACTIVACION DE FACTURAS*********
    /**
     * almacena codigo del sistema de la factura
     */
    private int codigoFactura;
    /**
     * almacena consecutivo de la factura
     */
    private int consecutivoFactura;
    
    /**
     * consecutivo Factura para consulta
     */
    private String consecutivoFac;
    
    /**
     * almacena la fecha de elaboración de la factura o cuenta de cobro
     */
    private String fechaElaboracion;
    /**
     * almacena la fecha de aprobacion de la factura o cuenta de cobro
     */
    private String fechaAprobacion;
    /**
     * almacena la hora de elaboración de la factura o cuenta de cobro
     */
    private String horaElaboracion;
    /**
     * almacena el convenio de la factura
     */
    private String convenio;
    /**
     * almacena el valor de la factura
     */
    private double valor;
    
    /**
     * Almacena el valor de la factura como un String.
     */
    private String valorFactura;
    
    /**
     * Valor Total Facturas, sumatoria de los totales de todas las facturas.
     */
    private double valorTotalFacturas;
    /**
     * almacena la fecha de radicación de la cuenta de cobro
     * a la cual pertenece la factura
     */
    private String fechaRadicacion;
    /**
     * almacena el número de radicación de la cuenta de cobro
     */
    private String numeroRadicacion;
    
    /**
     * La fecha inicial para la cuenta de cobro.
     */
    private String fechaInicial;
    
    /**
     * La fecha final de la cuenta de cobro.
     */
    private String fechaFinal;
    
    /**
     * Mapa para manejar las generalidades de las cuentas de cobro.
     */
    private HashMap cuentasCobroMapa;
    
    /**
     * HashMapa para cargar las facturas que posiblemente
     * perteneceran a una Cuenta de Cobro.
     */
    private HashMap facturasCxC;
    
    /**
     * El numero de facturas 
     */
    private int numeroFacturasCxC;
    
    /**
     * Variable para manejar la fecha de elaboracion de una factura
     */
    private String fechaElaboracionFactura;
    
    /**
     * Variable para almacena la via de ingreso de una factura.
     */
    private String viaIngresoFactura;
    /**
     * 
     * almacena las observaciones de la radicacion
     */
    private String observacionesRadicacion;
    
    /**
     * Indica si se debe mostrar el centro de Atencion en la pagina
     */
    private boolean mostrarCentroAtencion;
    
    
    /**
     * Nit del convenio
     */
    private String NitConvenio;
    
    /**
     * Direccion del convenio
     */
    private String direccionConvenio;
    
    /**
     * Telefono del convenio
     */
    private String telefonoConvenio;
    
    /**
     * Campo que se utiliza para verificar si la impresión será con Anexos
     */
    private boolean impresionAnexos;
    /**
     * 
     */
    private HashMap facturasMismoConsecutivo;
    /**
     * 
     */
    private boolean mostrarPopUpFacturasMismoConsecutivo;
    
    /**
     * 
     */
    private String nombreEntidadFactura;
    
    /**
     * 
     */
    private int indiceConsecutivoCargar;
    
    /**
     * Mapa para amnejar las El Estado de la Glosa Correspondiente a la Factura a Inactivar
     */
    private HashMap<String, Object> estadoGlosaFacturaMap;
    
    /**
     * 
     */
    private boolean cuentaCobroTieneAjusPen=false;

    
  //Agregado por Anexo 992
    
    private ArrayList<DtoFirmasValoresPorDefecto> listadoFirmas;
    
    //Fin anexo 992
    
    
    
    /**
     *Flag que contiene si existe un reporte Detallado 
     */
    private String existeReporteDetallado;
    
	/**
	 * @return the cuentaCobroTieneAjusPen
	 */
	public boolean isCuentaCobroTieneAjusPen() {
		return cuentaCobroTieneAjusPen;
	}

	/**
	 * @param cuentaCobroTieneAjusPen the cuentaCobroTieneAjusPen to set
	 */
	public void setCuentaCobroTieneAjusPen(boolean cuentaCobroTieneAjusPen) {
		this.cuentaCobroTieneAjusPen = cuentaCobroTieneAjusPen;
	}

	/**
	 * inicializa los datos del formulario
	 */
    public void reset()
    {
       	this.setNumCuentaCobro(0);
    	this.setCodigoFactura(0);
    	this.setConsecutivoFactura(0);
    	this.consecutivoFac="";
    	this.setConvenio("");
    	this.setFechaElaboracion("");
    	this.setFechaRadicacion("");
    	this.setObservaciones("");
    	this.setValor(0);
    	this.detalleFacturas=null;
    	this.cuentaCobroTieneAjusPen=false;
    	this.fechaInicial="";
    	this.fechaFinal="";
    	this.observacionMovimiento="";
    	this.observacionesRadicacion="";
    	this.viaIngreso = -1;
    	this.propiedadTempS="";
    	this.propiedadTempI=-1;
        this.facturasCxC=new HashMap();
        this.numeroFacturasCxC=0;
        this.mapMovimientos = new HashMap();
        this.cuentasCobroMapa=new HashMap();
        this.valorTotalFacturas=0;
        this.valorInicialCuenta=0;
        this.codigoConvenio = 0;
        this.linkSiguiente = "";
        this.selecTodas=false;
        this.fechaElaboracionFactura="";
        this.viaIngresoFactura="";
        this.valorFactura="";
        this.numeroFacturasNoAptas=0;
        this.numeroFacturasSeleccionadas=0;
        this.mostrarMensaje=false;
        this.log="";
        this.motivoAnulacion = "";
        this.mapValidaciones = new HashMap ();
        this.impresionDetallada=null;
        this.numeroRadicacion="";
        this.criterio="";
        this.textoBusqueda="";
        this.codigoCentroAtencion=ConstantesBD.codigoNuncaValido;
        this.nombreCentroAtencion="Todos";
        this.NitConvenio = "";
        this.direccionConvenio = "";
        this.telefonoConvenio = "";
        this.centroAtencion="";
        this.mostrarCentroAtencion = false;
        this.impresionAnexos = false;
        this.facturasMismoConsecutivo=new HashMap();
        this.facturasMismoConsecutivo.put("numRegistros", "0");
        this.mostrarPopUpFacturasMismoConsecutivo=false;
        this.nombreEntidadFactura="";
        this.indiceConsecutivoCargar=ConstantesBD.codigoNuncaValido;
        this.estadoGlosaFacturaMap=new HashMap<String, Object>();
        this.estado="false";
        
        //Anexo 992
        this.listadoFirmas=new ArrayList<DtoFirmasValoresPorDefecto>();
        this.existeReporteDetallado="";
        
    }

	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * Función de validación
	 * @param mapping
	 * @param request
	 * @return ActionError que especifica el error
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		
		ActionErrors errores = new ActionErrors();
		if(estado.equals("guardarInactivacion"))
		{
			if(this.getConsecutivoFactura()==0){
				errores.add("Número de Factura", new ActionMessage("errors.required", "El número de la Factura"));
			}
			if(this.getNumCuentaCobro()==0){
				errores.add("Número de Cuenta de Cobro", new ActionMessage("errors.required", "El número de la Cuenta de Cobro"));
			}
		}
		if(estado.equals("buscarCuentaCobro")){
			if(this.getNumCuentaCobro()==0&&this.getConsecutivoFactura()==0){
				errores.add("Número de Radicación", new ActionMessage("errors.required", "El No de Factura o No de Cuenta de Cobro"));
			}
		}
		if(estado.equals("guardarRadicacion"))
		{
			if(this.getNumCuentaCobro()==0||this.getFechaElaboracion().equals("")){
				errores.add("Número de Cuenta de Cobro", new ActionMessage("error.radicacion.noCargadaCuentaCobro", "radicar"));
			}
			else{
				if(this.getNumeroRadicacion().equals("")){
					errores.add("Número de Radicación", new ActionMessage("errors.required", "El número de Radicación"));
				}
				if(this.getFechaRadicacion().equals("")){
					errores.add("Fecha de Radicación", new ActionMessage("errors.required", "La fecha de Radicación"));
				}
				else{
					if(!UtilidadFecha.validarFecha(this.getFechaRadicacion())){
						errores.add("fecha de radicación", new ActionMessage("errors.formatoFechaInvalido",this.getFechaRadicacion()));
					}
					else{
						// se verifica que la fecha de radicación no sea mayor a la actual
						if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaRadicacion())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0){
							errores.add("fecha de radicación", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "de radicación", "actual"));
							}
						// se verifica que la fecha de radicación no sea menor a la fecha de elaboracion de cuenta de cobro
						if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaElaboracion())).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.getFechaRadicacion()))>0){
							errores.add("fecha de radicación", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "de radicación", "de elaboración de cuenta de cobro"));
							}
						// se verifica que la fecha de aprobacion no sea menor a la fecha de radicacion
						if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaAprobacion())).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.getFechaRadicacion()))>0){
							errores.add("fecha de radicación", new ActionMessage("errors.fechaAnteriorIgualActual", "La fecha de radicación", "la fecha de aprobación de la cuenta de cobro"));
							}
					}
				}
			}			
		}
		
		if(estado.equals("aprobarCuentaCobro"))
		{
			if(this.numCuentaCobro == 0.0 || (this.numCuentaCobro+"").equals("0.0"))
			{
				errores.add("Número de Cuenta de Cobro", new ActionMessage("error.radicacion.noCargadaCuentaCobro", "realizar Aprobacion"));				
			}
		}
		
		//validacion para generacion cuenta x facturas
		if(estado.equals("cargarFactura"))
		{
			boolean reset=false;
			if(this.consecutivoFac.equals("") || this.consecutivoFac.equals("NULL") || this.consecutivoFac==null)
			{
				errores.add("Consecutivo Factura", new ActionMessage("errors.required","El consecutivo de la factura"));
				reset=true;
			}
			else
			{
				if(!UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(institucion)))
				{
					this.codigoFactura=Utilidades.obtenerCodigoFactura(Utilidades.convertirAEntero(consecutivoFac),this.institucion);
					if(this.codigoFactura==ConstantesBD.codigoNuncaValido)
					{
						errores.add("Consecutivo Factura", new ActionMessage("error.facturaInexistente"));
						reset=true;
					}
					else
					{
						String[] estadoVector=Utilidades.obtenerEstadoFactura(this.codigoFactura).split(ConstantesBD.separadorSplit);
						if(Utilidades.convertirAEntero(estadoVector[0])==ConstantesBD.codigoEstadoFacturacionFacturada)
						{
							if(ValidacionesFactura.facturaTieneAjustesPendientes(this.codigoFactura))
							{
								errores.add("factura con ajustes", new ActionMessage("error.facturaConAjustesPendientes"));
								reset=true;
							}
							if(!ValidacionesFactura.facturaTieneSaldoPendiente(this.codigoFactura))
							{
								errores.add("factura saldo cero", new ActionMessage("error.facturaSaldoPendienteCero"));
								reset=true;
							}
							if(Utilidades.obtenerCuentaCobroFactura(this.codigoFactura)!=ConstantesBD.codigoNuncaValido)
							{
								errores.add("tiene cuenta de cobro", new ActionMessage("error.facturaTienCuentaCobro"));
								reset=true;
							}
						}
						else
						{
							errores.add("estado diferente", new ActionMessage("error.facturaEstadoDiferenteFacturada",estadoVector[1]));
							reset=true;
						}
					}
				}
			}
			if(reset)
			{
				String consTemp=this.consecutivoFac;
				this.reset();
				this.consecutivoFac=consTemp;
			}
		}
		if(estado.equals("generarXFacturas"))
		{
			if(this.consecutivoFac.equals("") || this.consecutivoFac.equals("NULL") || this.consecutivoFac==null || this.codigoFactura<=0)
			{
				errores.add("Factura", new ActionMessage("error.cargarFactura"));
			}
		}
		//fin validacion para generacion cuenta x facturas
		if(estado.equals("continuarGeneracion"))
		{
			boolean errorFecha=false;
			boolean fechaNull=false;
			boolean viaIngresoSeleccionada=false;
			if(this.convenio.equals("null")||this.convenio==null||this.convenio.equals("")||this.convenio.equals("-1"))
			{
				errores.add("Convenio", new ActionMessage("errors.required", "El Convenio"));
			}
			//validacion de vias de ingreso
			//generar una variable que me dice el numero de vias de ingreso seleccionada y incrementar o decrementa pos javascript
			//falta verificar que la fecha no sea nula
			int numViasIngreso=Utilidades.convertirAEntero(cuentasCobroMapa.get("numeroViasIngreso")+"");
			for(int i=0;i<numViasIngreso;i++)
			{
				if(UtilidadTexto.getBoolean(cuentasCobroMapa.get("viaIngresoSeleccionada_"+i)+""))
				{
					viaIngresoSeleccionada=true;
					i=numViasIngreso;
				}
					
			}
			if(!viaIngresoSeleccionada)
			{
				errores.add("Vias ingreso", new ActionMessage("errors.required", "La Via de Ingreso"));
			}
			if(fechaInicial.equals("")||fechaInicial.equals("null")||fechaInicial==null)
			{
				errores.add("fecha Inicial", new ActionMessage("errors.required", "La fecha inicial"));
				fechaNull=true;
			}
			if(fechaFinal.equals("")||fechaFinal.equals("null")||fechaFinal==null)
			{
				errores.add("fecha Fianl", new ActionMessage("errors.required", "La fecha final"));
				fechaNull=true;
			}
			if(!fechaNull)
			{
				if(!UtilidadFecha.validarFecha(this.fechaInicial))
				{
					errores.add("fecha de Inicial", new ActionMessage("errors.formatoFechaInvalido",this.fechaInicial));
					errorFecha=true;
				}
				if(!UtilidadFecha.validarFecha(this.fechaFinal))
				{
					errores.add("fecha de Fianl", new ActionMessage("errors.formatoFechaInvalido",this.fechaFinal));
					errorFecha=true;
				}
				if(!errorFecha)
				{
					if((UtilidadFecha.conversionFormatoFechaABD(this.fechaInicial)).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
					{
						errores.add("fecha de Inicial", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Inicial", "Actual"));
					}
					if((UtilidadFecha.conversionFormatoFechaABD(this.fechaFinal)).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
					{
						errores.add("fecha de Final", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Final", "Actual"));
					}
					if((UtilidadFecha.conversionFormatoFechaABD(this.fechaInicial)).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaFinal))>0)
					{
						errores.add("fecha de Inicial-Final", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Inicial", "Final"));
					}				
				}
			}
			
		}
		
		if(this.estado.equals("continuarMovimiento") || this.estado.equals("continuarModificacion") )
		{
		    int contErr=0,k=0,pos=0;
		    boolean existeErrorFecha = false;
		    if(!(getMapMovimientos("ultimoIndex")+"").equals(null) 
		            && !(getMapMovimientos("ultimoIndex")+"").equals("null")
		            && !(getMapMovimientos("ultimoIndex")+"").equals(""))
		    {
			    for( k = 0; k < Utilidades.convertirAEntero(getMapMovimientos("ultimoIndex")+"") ; k++)
		        {
			        if(getMapMovimientos().containsKey("codViaIngresoTemp_"+k) 
		                    && getMapMovimientos().containsKey("esCheckActivoTemp_"+k)
		                    && !getMapMovimientos("codViaIngresoTemp_"+k).equals(null))
		            {
			            pos ++;
			            if( getMapMovimientos("esCheckActivoTemp_"+k).equals("off") )
			            {
			                contErr ++; 
			            }
		            }
		        }
		    }
		    if(this.estado.equals("continuarMovimiento"))
		    {
			    if((getMapMovimientos("ultimoIndex")+"").equals(null) 
			            || (getMapMovimientos("ultimoIndex")+"").equals("null")
			            || (getMapMovimientos("ultimoIndex")+"").equals(""))
			    {
			        errores.add("cuenta cobro", new ActionMessage("errors.required", "La cuenta de cobro"));
			    }
		    }
		    if(contErr==pos)
		    {
		        errores.add("Vias ingreso", new ActionMessage("errors.required", "La Via de Ingreso"));
		    }
		    if(fechaInicial.equals("")||fechaInicial.equals("null")||fechaInicial==null)
			{
				errores.add("fecha Inicial", new ActionMessage("errors.required", "La fecha inicial"));
				existeErrorFecha = true;
			}
			if(fechaFinal.equals("")||fechaFinal.equals("null")||fechaFinal==null)
			{
				errores.add("fecha Fianl", new ActionMessage("errors.required", "La fecha final"));		
				existeErrorFecha = true;
			}
			if(!existeErrorFecha)
			{
				if(!UtilidadFecha.validarFecha(this.fechaInicial))
				{
					errores.add("fecha de Inicial", new ActionMessage("errors.formatoFechaInvalido",this.fechaInicial));				
				}
				if(!UtilidadFecha.validarFecha(this.fechaFinal))
				{
					errores.add("fecha de Final", new ActionMessage("errors.formatoFechaInvalido",this.fechaFinal));				
				}
				if(!UtilidadFecha.validarFecha(this.fechaElaboracion))
				{
					errores.add("fecha de Elaboracion", new ActionMessage("errors.formatoFechaInvalido",this.fechaElaboracion));				
				}
			}
		}
		return errores;
	}	
	
    //***********************************************************************
    //*************GETTERS & SETTERS****************************************
	
	/**
    * Método que me devuelve el número de facturas
    * que hay en la colección detalleFacturas
    * @return
    */
   public int getNumeroFacturasImpresion ()
   {
       if (this.detalleFacturas!=null)
       {
           return this.detalleFacturas.size();
       }
       else
       {
           return 0;
       }
   }
   
	/**
	 * @return Returns the fechaFinal.
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}
	/**
	 * @param fechaFinal The fechaFinal to set.
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	/**
	 * @return Returns the fechaInicial.
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}
	/**
	 * @param fechaInicial The fechaInicial to set.
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	/**
	 * @return Returns the facturas.
	 */
	public HashMap getFacturasCxC() {
		return facturasCxC;
	}
	/**
	 * @param facturas The facturas to set.
	 */
	public void setFacturasCxC(HashMap facturas) {
		this.facturasCxC = facturas;
	}
	/**
	 * @return Returns the facturas.
	 */
	public Object getFacturaCxC(String key) {
		return facturasCxC.get(key);
	}
	/**
	 * @param facturas The facturas to set.
	 */
	public void setFacturasCxC(String key,Object value) 
	{
		this.facturasCxC.put(key,value);
	}
	/**
	 * @return Returns the numeroFacturasCxC.
	 */
	public int getNumeroFacturasCxC() {
		return numeroFacturasCxC;
	}
	/**
	 * @param numeroFacturasCxC The numeroFacturasCxC to set.
	 */
	public void setNumeroFacturasCxC(int numeroFacturasCxC) {
		this.numeroFacturasCxC = numeroFacturasCxC;
	}
	
    /**
     * @return Retorna mapMovimientos.
     */
    public HashMap getMapMovimientos() {
        return mapMovimientos;
    }
    /**
     * @param mapMovimientos Asigna mapMovimientos.
     */
    public void setMapMovimientos(HashMap mapMovimientos) {
        this.mapMovimientos = mapMovimientos;
    }
    
   /**
    * Retorna un objeto
    * @param key, llave del dato
    * @return Object
    */
    public Object getMapMovimientos(String key) {
        return mapMovimientos.get(key);
    }
    /**
     * Asigna un dato por medio de la llave
     * @param key, llave del dato
     * @param value, dato
     */
    public void setMapMovimientos(String key, Object value) {
        this.mapMovimientos.put(key,value);
    }
	/**
	 * @return Returns the cuentasCobroMapa.
	 */
	public HashMap getCuentasCobroMapaCompleto() {
		return cuentasCobroMapa;
	}
	/**
	 * @param cuentasCobroMapa The cuentasCobroMapa to set.
	 */
	public void setCuentasCobroMapaCompleto(HashMap cuentasCobroMapa) {
		this.cuentasCobroMapa = cuentasCobroMapa;
	}
	
	public void setCuentasCobroMapa(String key,Object value)
	{
		this.cuentasCobroMapa.put(key,value);
	}
	
	public Object getCuentasCobroMapa(String key)
	{
		return cuentasCobroMapa.get(key);
	}
	/**
	 * @return Returns the valorTotalFacturas.
	 */
	public double getValorTotalFacturas() {
		return valorTotalFacturas;
	}
	/**
	 * @param valorTotalFacturas The valorTotalFacturas to set.
	 */
	public void setValorTotalFacturas(double valorTotalFacturas) {
		this.valorTotalFacturas = valorTotalFacturas;
	}
	
	 /**
     * @return Retorna codigoConvenio.
     */
    public int getCodigoConvenio() {
        return codigoConvenio;
    }
    /**
     * @param codigoConvenio Asigna codigoConvenio.
     */
    public void setCodigoConvenio(int codigoConvenio) {
        this.codigoConvenio = codigoConvenio;
    }
	/**
	 * @return Returns the valorInicialCuenta.
	 */
	public double getValorInicialCuenta() {
		return valorInicialCuenta;
	}
	/**
	 * @param valorInicialCuenta The valorInicialCuenta to set.
	 */
	public void setValorInicialCuenta(double valorInicialCuenta) {
		this.valorInicialCuenta = valorInicialCuenta;
	}
	/**
	 * @param detalleFacturas The detalleFacturas to set.
	 */
	public void setDetalleFacturas(Collection detalleFacturas) {
		this.detalleFacturas = detalleFacturas;
	}
	/**
	 * @return Returns the maxPageIndex.
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}
	/**
	 * @param maxPageIndex The maxPageIndex to set.
	 */
	public void setMaxPageItems(int maxPageIndex) {
		this.maxPageItems = maxPageIndex;
	}
    	
    /**
     * @return Retorna linkSiguiente.
     */
    public String getLinkSiguiente() {
        return linkSiguiente;
    }
    /**
     * @param linkSiguiente Asigna linkSiguiente.
     */
    public void setLinkSiguiente(String linkSiguiente) {
        this.linkSiguiente = linkSiguiente;
    }
    
    /**
	 * Retorna Offset del pager
	 * @return
	 */
	public int getOffset()
	{
		return offset;
	}

	/**
	 * Asigna Offset del pager
	 * @param i
	 */
	public void setOffset(int i) 
	{
		offset = i;
	}
	/**
	 * @return Returns the selecTodas.
	 */
	public boolean isSelecTodas() {
		return selecTodas;
	}
	/**
	 * @param selecTodas The selecTodas to set.
	 */
	public void setSelecTodas(boolean selecTodas) {
		this.selecTodas = selecTodas;
	}
	/**
	 * @return Returns the consecutivoFac.
	 */
	public String getConsecutivoFac() {
		return consecutivoFac;
	}
	/**
	 * @param consecutivoFac The consecutivoFac to set.
	 */
	public void setConsecutivoFac(String consecutivoFac) {
		this.consecutivoFac = consecutivoFac.trim();
	}
	/**
	 * @return Returns the fechaElaboracionFactura.
	 */
	public String getFechaElaboracionFactura() {
		return fechaElaboracionFactura;
	}
	/**
	 * @param fechaElaboracionFactura The fechaElaboracionFactura to set.
	 */
	public void setFechaElaboracionFactura(String fechaElaboracionFactura) {
		this.fechaElaboracionFactura = fechaElaboracionFactura;
	}
	/**
	 * @return Returns the fecha Aprobacion
	 */
	public String getFechaAprobacion() {
		return fechaAprobacion;
	}
	/**
	 * @param fechaAprobacion The fechaAprobacion to set.
	 */
	public void setFechaAprobacion(String fechaAprobacion) {
		this.fechaAprobacion = fechaAprobacion;
	}
	/**
	 * @return Returns the viaIngresoFactura.
	 */
	public String getViaIngresoFactura() {
		return viaIngresoFactura;
	}
	/**
	 * @param viaIngresoFactura The viaIngresoFactura to set.
	 */
	public void setViaIngresoFactura(String viaIngresoFactura) {
		this.viaIngresoFactura = viaIngresoFactura;
	}
	/**
	 * @return Returns the valorFactura.
	 */
	public String getValorFactura() {
		return valorFactura;
	}
	/**
	 * @param valorFactura The valorFactura to set.
	 */
	public void setValorFactura(String valorFactura) {
		this.valorFactura = valorFactura;
	}
	/**
	 * @return Returns the numeroFacturasNoAptas.
	 */
	public int getNumeroFacturasNoAptas() {
		return numeroFacturasNoAptas;
	}
	/**
	 * @param numeroFacturasNoAptas The numeroFacturasNoAptas to set.
	 */
	public void setNumeroFacturasNoAptas(int numeroFacturasNoAptas) {
		this.numeroFacturasNoAptas = numeroFacturasNoAptas;
	}
	/**
	 * @return Returns the numeroFacturasSeleccionadas.
	 */
	public int getNumeroFacturasSeleccionadas() {
		return numeroFacturasSeleccionadas;
	}
	/**
	 * @param numeroFacturasSeleccionadas The numeroFacturasSeleccionadas to set.
	 */
	public void setNumeroFacturasSeleccionadas(int numeroFacturasSeleccionadas) {
		this.numeroFacturasSeleccionadas = numeroFacturasSeleccionadas;
	}
	/**
	 * @return Returns the mostrarMensaje.
	 */
	public boolean getMostrarMensaje() {
		return mostrarMensaje;
	}
	/**
	 * @param mostrarMensaje The mostrarMensaje to set.
	 */
	public void setMostrarMensaje(boolean mostrarMensaje) {
		this.mostrarMensaje = mostrarMensaje;
	}
	
    /**
     * @return Retorna log.
     */
    public String getLog() {
        return log;
    }
    /**
     * @param log Asigna log.
     */
    public void setLog(String log) {
        this.log = log;
    }
    
    /**
     * @return Retorna motivoAnulacion.
     */
    public String getMotivoAnulacion() {
        return motivoAnulacion;
    }
    /**
     * @param motivoAnulacion Asigna motivoAnulacion.
     */
    public void setMotivoAnulacion(String motivoAnulacion) {
        this.motivoAnulacion = motivoAnulacion;
    }
    
    /**
     * @return Retorna mapValidaciones.
     */
    public HashMap getMapValidaciones() {
        return mapValidaciones;
    }
    /**
     * @param mapValidaciones Asigna mapValidaciones.
     */
    public void setMapValidaciones(HashMap mapValidaciones) {
        this.mapValidaciones = mapValidaciones;
    }
    /**
     * @return Retorna mapValidaciones.
     */
    public Object getMapValidaciones(String key) {
        return mapValidaciones.get(key);
    }
    /**
     * @param mapValidaciones Asigna mapValidaciones.
     */
    public void setMapValidaciones(String key, Object value) {
        this.mapValidaciones.put(key,value);
    }
	/**
	 * @return Returns the fechaAnulacion.
	 */
	public String getFechaAnulacion()
	{
		return fechaAnulacion;
	}
	/**
	 * @param fechaAnulacion The fechaAnulacion to set.
	 */
	public void setFechaAnulacion(String fechaAnulacion)
	{
		this.fechaAnulacion= fechaAnulacion;
	}
	/**
	 * @return Returns the horaAnulacion.
	 */
	public String getHoraAnulacion()
	{
		return horaAnulacion;
	}
	/**
	 * @param horaAnulacion The horaAnulacion to set.
	 */
	public void setHoraAnulacion(String horaAnulacion)
	{
		this.horaAnulacion= horaAnulacion;
	}
	/**
	 * @return Returns the tipoAnulacion.
	 */
	public String getTipoAnulacion()
	{
		return tipoAnulacion;
	}
	/**
	 * @param tipoAnulacion The tipoAnulacion to set.
	 */
	public void setTipoAnulacion(String tipoAnulacion)
	{
		this.tipoAnulacion= tipoAnulacion;
	}
	/**
	 * @return Returns the usuarioAnulacion.
	 */
	public String getUsuarioAnulacion()
	{
		return usuarioAnulacion;
	}
	/**
	 * @param usuarioAnulacion The usuarioAnulacion to set.
	 */
	public void setUsuarioAnulacion(String usuarioAnulacion)
	{
		this.usuarioAnulacion= usuarioAnulacion;
	}
	
	
	/**
	 * @return Returns the criterio.
	 */
	public String getCriterio()
	{
		return criterio;
	}
	/**
	 * @param criterio The criterio to set.
	 */
	public void setCriterio(String criterio)
	{
		this.criterio= criterio;
	}
	/**
	 * @return Returns the textoBusqueda.
	 */
	public String getTextoBusqueda()
	{
		return textoBusqueda;
	}
	/**
	 * @param textoBusqueda The textoBusqueda to set.
	 */
	public void setTextoBusqueda(String textoBusqueda)
	{
		this.textoBusqueda= textoBusqueda;
	}
	/**
	 * @return Returns the institucion.
	 */
	public int getInstitucion() {
		return institucion;
	}
	/**
	 * @param institucion The institucion to set.
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}
//	***********************************************************************
    //*************GETTERS & SETTERS****************************************
    
    
    /**
     * @return Retorna estado.
     */
    public String getEstado() {
        return estado;
    }
    /**
     * @param estado Asigna estado.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
    /**
     * @return Retorna accion.
     */
    public String getAccion() {
        return accion;
    }
    /**
     * @param accion Asigna accion.
     */
    public void setAccion(String accion) {
        this.accion = accion;
    }
    
	/**
	 * @return Returns the codigoFactura.
	 */
	public int getCodigoFactura() {
		return codigoFactura;
	}
	/**
	 * @param codigoFactura The codigoFactura to set.
	 */
	public void setCodigoFactura(int codigoFactura) {
		this.codigoFactura = codigoFactura;
	}
	/**
	 * @return Returns the consecutivoFactura.
	 */
	public int getConsecutivoFactura() {
		return consecutivoFactura;
	}
	/**
	 * @param consecutivoFactura The consecutivoFactura to set.
	 */
	public void setConsecutivoFactura(int consecutivoFactura) {
		this.consecutivoFactura = consecutivoFactura;
	}
	/**
	 * @return Returns the convenio.
	 */
	public String getConvenio() {
		return convenio;
	}
	/**
	 * @param convenio The convenio to set.
	 */
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}
	/**
	 * @return Returns the fechaElaboracion.
	 */
	public String getFechaElaboracion() {
		return fechaElaboracion;
	}
	/**
	 * @param fechaElaboracion The fechaElaboracion to set.
	 */
	public void setFechaElaboracion(String fechaElaboracion) {
		this.fechaElaboracion = fechaElaboracion;
	}
	/**
	 * @return Returns the fechaRadicacion.
	 */
	public String getFechaRadicacion() {
		return fechaRadicacion;
	}
	/**
	 * @param fechaRadicacion The fechaRadicacion to set.
	 */
	public void setFechaRadicacion(String fechaRadicacion) {
		this.fechaRadicacion = fechaRadicacion;
	}
	/**
	 * @return Returns the numCuentaCobro.
	 */
	public double getNumCuentaCobro() {
		return numCuentaCobro;
	}
	/**
	 * @return Returns the impresionDetallada.
	 */
	public Collection getImpresionDetallada()
	{
		return impresionDetallada;
	}
	/**
	 * @param impresionDetallada The impresionDetallada to set (Metodo SET).
	 */
	public void setImpresionDetallada(Collection impresionDetallada)
	{
		this.impresionDetallada= impresionDetallada;
	}
	/**
	 * @return Returns the detalleFacturas.
	 */
	public Collection getDetalleFacturas()
	{
		return detalleFacturas;
	}
	/**
	 * @return  Returns the anulacionCxC.
	 */
	public Collection getAnulacionCxC()
	{
		return anulacionCxC;
	}
	/**
	 * @param anulacionCxC The anulacionCxC to set.
	 */
	public void setAnulacionCxC(Collection anulacionCxC)
	{
		this.anulacionCxC= anulacionCxC;
	}
	
	/**
	 * @param numCuentaCobro The numCuentaCobro to set.
	 */
	public void setNumCuentaCobro(double numCuentaCobro) {
		this.numCuentaCobro = numCuentaCobro;
	}
	/**
	 * @return Returns the observaciones.
	 */
	public String getObservaciones() {
		return observaciones;
	}
	/**
	 * @param observaciones The observaciones to set.
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	/**
	 * @return Returns the usuarioMovimiento.
	 */
	public String getUsuarioMovimiento() {
		return usuarioMovimiento;
	}
	/**
	 * @param usuarioMovimiento The usuarioMovimiento to set.
	 */
	public void setUsuarioMovimiento(String usuarioMovimiento) {
		this.usuarioMovimiento = usuarioMovimiento;
	}
	/**
	 * @return Returns the valor.
	 */
	public double getValor() {
		return valor;
	}
	/**
	 * @param valor The valor to set.
	 */
	public void setValor(double valor) {
		this.valor = valor;
	}
	/**
	 * @return Returns the numeroRadicacion.
	 */
	public String getNumeroRadicacion() {
		return numeroRadicacion;
	}
	/**
	 * @param numeroRadicacion The numeroRadicacion to set.
	 */
	public void setNumeroRadicacion(String numeroRadicacion) {
		this.numeroRadicacion = numeroRadicacion;
	}
	/**
	 * @return Returns the observacionesRadicacion.
	 */
	public String getObservacionesRadicacion() {
		return observacionesRadicacion;
	}
	/**
	 * @param observacionesRadicacion The observacionesRadicacion to set.
	 */
	public void setObservacionesRadicacion(String observacionesRadicacion) {
		this.observacionesRadicacion = observacionesRadicacion;
	}	
    /**
     * @return Retorna observacionMovimiento.
     */
    public String getObservacionMovimiento() {
        return observacionMovimiento;
    }
    /**
     * @param observacionMovimiento Asigna observacionMovimiento.
     */
    public void setObservacionMovimiento(String observacionMovimiento) {
        this.observacionMovimiento = observacionMovimiento;
    }
    /**
     * @return Retorna viaIngreso.
     */
    public int getViaIngreso() {
        return viaIngreso;
    }
    /**
     * @param viaIngreso Asigna viaIngreso.
     */
    public void setViaIngreso(int viaIngreso) {
        this.viaIngreso = viaIngreso;
    }
	/**
	 * @return Returns the horaElaboracion.
	 */
	public String getHoraElaboracion() {
		return horaElaboracion;
	}
	/**
	 * @param horaElaboracion The horaElaboracion to set.
	 */
	public void setHoraElaboracion(String horaElaboracion) {
		this.horaElaboracion = horaElaboracion;
	}	
    /**
     * @return Retorna propiedadTemp.
     */
    public String getPropiedadTempS() {
        return propiedadTempS;
    }
    /**
     * @param propiedadTemp Asigna propiedadTemp.
     */
    public void setPropiedadTempS(String propiedadTempS) {
        this.propiedadTempS = propiedadTempS;
    }
    /**
     * @return Retorna propiedadTempI.
     */
    public int getPropiedadTempI() {
        return propiedadTempI;
    }
    /**
     * @param propiedadTempI Asigna propiedadTempI.
     */
    public void setPropiedadTempI(int propiedadTempI) {
        this.propiedadTempI = propiedadTempI;
    }

	public int getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}

	public void setCodigoCentroAtencion(int codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}

	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	public String getCentroAtencion() {
		if(this.centroAtencion.trim().equals(""))
			this.centroAtencion=this.codigoCentroAtencion+ConstantesBD.separadorSplit+this.nombreCentroAtencion;
		return centroAtencion;
	}

	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
		if(!this.centroAtencion.trim().equals(""))
		{
			String[] temp=this.centroAtencion.split(ConstantesBD.separadorSplit);
			this.codigoCentroAtencion=Utilidades.convertirAEntero(temp[0]);
			this.nombreCentroAtencion=temp[1];
		}
	}

	/**
	 * @return Returns the mostrarCentroAtencion.
	 */
	public boolean isMostrarCentroAtencion() {
		return mostrarCentroAtencion;
	}

	/**
	 * @param mostrarCentroAtencion The mostrarCentroAtencion to set.
	 */
	public void setMostrarCentroAtencion(boolean mostrarCentroAtencion) {
		this.mostrarCentroAtencion = mostrarCentroAtencion;
	}

	/**
	 * @return the impresionAnexos
	 */
	public boolean isImpresionAnexos() {
		return impresionAnexos;
	}

	/**
	 * @param impresionAnexos the impresionAnexos to set
	 */
	public void setImpresionAnexos(boolean impresionAnexos) {
		this.impresionAnexos = impresionAnexos;
	}

	/**
	 * @return the direccionConvenio
	 */
	public String getDireccionConvenio() {
		return direccionConvenio;
	}

	/**
	 * @param direccionConvenio the direccionConvenio to set
	 */
	public void setDireccionConvenio(String direccionConvenio) {
		this.direccionConvenio = direccionConvenio;
	}

	/**
	 * @return the nitConvenio
	 */
	public String getNitConvenio() {
		return NitConvenio;
	}

	/**
	 * @param nitConvenio the nitConvenio to set
	 */
	public void setNitConvenio(String nitConvenio) {
		NitConvenio = nitConvenio;
	}

	/**
	 * @return the telefonoConvenio
	 */
	public String getTelefonoConvenio() {
		return telefonoConvenio;
	}

	/**
	 * @param telefonoConvenio the telefonoConvenio to set
	 */
	public void setTelefonoConvenio(String telefonoConvenio) {
		this.telefonoConvenio = telefonoConvenio;
	}

	public HashMap getFacturasMismoConsecutivo() {
		return facturasMismoConsecutivo;
	}

	public void setFacturasMismoConsecutivo(HashMap facturasMismoConsecutivo) {
		this.facturasMismoConsecutivo = facturasMismoConsecutivo;
	}

	public Object getFacturasMismoConsecutivo(String key) {
		return facturasMismoConsecutivo.get(key);
	}

	public void setFacturasMismoConsecutivo(String key,Object value) {
		this.facturasMismoConsecutivo.put(key, value);
	}

	public boolean isMostrarPopUpFacturasMismoConsecutivo() {
		return mostrarPopUpFacturasMismoConsecutivo;
	}

	public void setMostrarPopUpFacturasMismoConsecutivo(
			boolean mostrarPopUpFacturasMismoConsecutivo) {
		this.mostrarPopUpFacturasMismoConsecutivo = mostrarPopUpFacturasMismoConsecutivo;
	}

	public String getNombreEntidadFactura() {
		return nombreEntidadFactura;
	}

	public void setNombreEntidadFactura(String nombreEntidadFactura) {
		this.nombreEntidadFactura = nombreEntidadFactura;
	}

	public int getIndiceConsecutivoCargar() {
		return indiceConsecutivoCargar;
	}

	public void setIndiceConsecutivoCargar(int indiceConsecutivoCargar) {
		this.indiceConsecutivoCargar = indiceConsecutivoCargar;
	}

	/**
	 * @return the listadoGlosasMap
	 */
	public HashMap<String, Object> getEstadoGlosaFacturaMap() {
		return estadoGlosaFacturaMap;
	}

	/**
	 * @param listadoGlosasMap the listadoGlosasMap to set
	 */
	public void setEstadoGlosaFacturaMap(HashMap<String, Object> estadoGlosaFacturaMap) {
		this.estadoGlosaFacturaMap = estadoGlosaFacturaMap;
	}

	/**
	 * @return the listadoGlosasMap
	 */
	public Object getEstadoGlosaFacturaMap(String llave) {
		return estadoGlosaFacturaMap.get(llave);
	}

	/**
	 * @param listadoGlosasMap the listadoGlosasMap to set
	 */
	public void setEstadoGlosaFacturaMap(String llave, Object obj) {
		this.estadoGlosaFacturaMap.put(llave, obj);
	}
	
	
	//Anexo 992
	public ArrayList<DtoFirmasValoresPorDefecto> getListadoFirmas() {
		return listadoFirmas;
	}

	public void setListadoFirmas(ArrayList<DtoFirmasValoresPorDefecto> listadoFirmas) {
		this.listadoFirmas = listadoFirmas;
	}

	/**
	 * @return the existeReporteDetallado
	 */
	public String getExisteReporteDetallado() {
		return existeReporteDetallado;
	}

	/**
	 * @param existeReporteDetallado the existeReporteDetallado to set
	 */
	public void setExisteReporteDetallado(String existeReporteDetallado) {
		this.existeReporteDetallado = existeReporteDetallado;
	}

	
}
