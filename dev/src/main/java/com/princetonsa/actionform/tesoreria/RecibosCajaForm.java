
/*
 * Creado   28/09/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.actionform.tesoreria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.Administracion.UtilConversionMonedas;

import com.princetonsa.dto.carteraPaciente.DtoDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoDeudor;
import com.princetonsa.dto.consultaExterna.DtoConceptoFacturaVaria;
import com.princetonsa.dto.facturasVarias.DtoRecibosCaja;
import com.princetonsa.dto.tesoreria.DtoConceptosIngTesoreria;
import com.princetonsa.dto.tesoreria.DtoDetallePagosBonos;
import com.princetonsa.dto.tesoreria.DtoEntidadesFinancieras;


/**
 * 
 *
 * @version 1.0, 28/09/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
@SuppressWarnings("serial")
public class RecibosCajaForm extends ActionForm 
{
    /**    * estado del workflow     */
    private String estado;

    /**      * almacena el nombre de la entidad/persona que esta realizando el pago     */
    private String recibidoDe;
    
    /**     * mapa de recibos de caja     */
    @SuppressWarnings("unchecked")
	private HashMap mapaConvenios;

    /**     * mapa de recibos de caja     */
    @SuppressWarnings("unchecked")
    private HashMap mapaConceptos;
    
    /**
     * 
     */
    @SuppressWarnings("unchecked")
    private HashMap mapaConceptosAuxiliarFlujoFact;
    
    /** almacena la posición del concepto seleccionado para adicionar un nuevo registro    */
    private int posSelConcepto;
    
    /** true si se el tipo de concepto debe generar un popUp    */
    private String abrirPopUp;    
    
    /** almacena el tipo de ingreso del concepto    */
    private int codigoTipoIngresoTesoreria;
    
    /** consecutivo de la factura para realizar busqueda     */
    private int consecutivoFact;
    
    /** almacena la fecha de la factura para realizar la busqueda     */
    private String fechaFact;
    
    /** almacena el nombre del paciente para realizar la busqueda     */
    private String identificacionPaciente;
    
    /** almacena las facturas consultadas     */
    @SuppressWarnings("unchecked")
    private HashMap mapaFacturas;
    
    /** tipo de identificacion del paciente     */
    private String tipoIdentificacion;
    
    /** almacena el campo por el cual se ordena     */
    private String patronOrdenar;
    
    /** ultimo patron por el cual se ordeno;     */
    private String ultimoPatron;
    
    /** almacena el valor del concepto segun su tipo, ej, si es por paciente el valor, puede ser ninguno o una via de ingreso     */
    private String valorConcepto;
    
    /** almacena la posición del registro que se elimina    */
    private int posEliminarC;      
    
    /** primer nombre del paciente     */
    private String primerNombre;
    
    /** Segundo Nombre del paciente    */
    private String segundoNombre;
    
    /** primer apellido del paciente     */
    private String primerApellido;
    
    /** segundo apellido del paciente     */
    private String segundoApellido;  
    
    /** almacena los datos de los pacientes    */
   @SuppressWarnings("unchecked")
   private HashMap mapaPacientes;
    
    /** almacena el numero del registro seleccionado     */
    private int regSeleccionado;
    
    /** almacena las observaciones     */
    private String observaciones;    
    
    /** *     */
    private String observacionesImprimir;
    
    /** almacena los valores de los conceptos cuando se selecciona un registro.     */
    @SuppressWarnings("unchecked")
    private HashMap valoresConceptos;
    
    /** almacena los datos de los registros de formas de pago    */
    @SuppressWarnings("unchecked")
    private HashMap mapaFormasPago;
    
    /** almacena el número del registro seleccionado para formas de pago     */
    private int posSelFormaPago;
    
    /** almacena el codigo del tipo detalle     */
    private int codigoTipoDetalleFormaPago;
    
    /** almacena los datos de los cheques     */
    @SuppressWarnings("unchecked")
    private HashMap mapaCheques;
    
    /** almacena los datos de las tarjetas    */
    @SuppressWarnings("unchecked")
    private HashMap mapaTarjetas;
    
    /** almacena el numero de cheques en el HashMap Cheques     */
    private int numRegCheques;
    
    /** almacena el numero de tarjetas en el mapa de tarjetas     */
    private int numRegTarjetas;
    
    /** almacena el tipo de popUp que se abre para la modificacacion     */
    private String tipoDePoUp;
    
    /** almacena el total de los valores de todos los conceptos ingresados     */
    private String totalConceptos;
    
    /** almacena el total de todos los valores de los pagos insertados     */
    private String totalPagos;
    
    /** almacena el numero del recibo de caja solo para el resumen     */
    private String numReciboCaja;
    
    /**
     * 
     */
    private String consecutivoReciboCaja;
    
    @SuppressWarnings("unchecked")
    private HashMap mapaTotalPagos;
    
    
    ///***CAMPOS UTILIZADOS PARA GENERAR RECIBOS CAJA DESDE LA FACTURA UNICA***///
    /** codigoConceptoIngTesoreria utilizada para generar el recibo de caja para una factura unica     */
    private String codigoConceptoIngTesoreriaFacturaUnica="";
    
    /**
     * codigoConceptoIngTesoreria utilizada para generar
     * el recibo de caja para una factura unica 
     */
    private String descConceptoIngTesoreriaFacturaUnica="";
    
    /**
     * indica si el flujo es de factura
     */
    private String esWorflowFactura="";
    
    /**
     * Atributo que indica si se esta generando un recibo de caja
     * desde la funcionalidad de Agenda odontológica
     */
    private boolean flujoAgendaOdontologica;
    
    
    private String pais;
    
    private String paisPlaza;
    
    private String ciudad;
    
    /**
     * 
     */
    private boolean manejaConversionMoneda;
    
    /**
     * 
     */
    private HashMap tiposMonedaTagMap;
    
    /**
     * 
     */
    private String indexTipoMoneda;
    
    /**
     * 
     */
    private int indexConversionOriginal;
    //*************************************************************************//
    
    /**
     * 
     */
    private ResultadoBoolean mensaje;
    
    
    /**
     * 
     */
    private String empresaFiltro;
    
    /**
     * 
     */
    private String convenioFiltro;
    
    /**
     * 
     */
    private String identificacionTerceroFiltro;
    
    // cambio anexo 791 - cambio multas citas incumplidas
    /**
     * 
     */
    private ArrayList<DtoConceptoFacturaVaria> conceptoFVarias;
    /**
     * 
     */
    private String conceptoFacVarias;
    
    /**
     * 
     */
    private ArrayList<DtoRecibosCaja> recibosCaja;
    private String nroFactura;
    private String tipoDeudor;
    private String codigoDeudor;
    private String descripcionDeudor;
    private String codigoMultaCita;
    private String fechaGenMultaCita;
    private String mostrarMultasCitas;
    
    private String closeVenBusq;
    private String tipoFiltro;
    
    //manejo de la busqueda del deudor
    private String idHiddenCodigo; 
	private String hacerSubmit;
	private String idHiddenDescripcion;
	private String idDiv;
	private String vieneTipoDeudor;
	private String tipoDeudo;
	
	
	private String ultimaPropiedad;
	private String columna;
    //---------------------------------------------------
    
    
	//*******************************************************************************
	// Anexo 762
	/**
	 * Datos de Financiacion
	 */
	private DtoDatosFinanciacion datosFinanciacion;
	
	/**
	 * tipos de identificacion
	 */
	private ArrayList<HashMap<String, Object>> arrayTipIdent;
	
	/**
	 * 
	 */
	private int ingresoFac; 
	
	/**
	 * Atributos para el manejo de las secciones
	 */
	private String seccionDeudor;
	private String seccionCodeudor;
	
	/**
	 * atributo existencia doc garanctia a asociar 
	 */
	private String existDocGarAso;
	
	/**
	 * atributo validacion parametros generales 
	 */
	private String paranGenVal;
	
	/**
	 * atributo para la validacion del ingreso de los datos del codeudor
	 */
	private String regCodeudor;
	/**
	 * 
	 */
	private String messAdver;
	
	/**
	 * 
	 */
	private DtoDeudor deudor;
	private ArrayList<DtoDeudor> resultBusDeu = new ArrayList<DtoDeudor>();
	private TreeSet<String> rompimientoPac;
	
	private String isRomp;
	private String paginaLinkSiguiente;
	private String patronOrdenarDeudores;
	private int posDeudorSel;
	private String valorFormaPago;
	private String existResPacIngFac;
	private String datFinanGuarExis; 
	//*******************************************************************************
	
	
	//************* ANEXO 875 ***************
	private String tipoAtencion;
	//************* ANEXO 875 ***************
	
     /**
     * 
     */
    private String tipoRegimenAcronimo="";
    
  //Anexo 958
	private ArrayList<DtoConceptosIngTesoreria> listadoConceptos;
	private String concepto;
	private String docSoporte;
	private String beneficiario;
	private String valor;
	private String llegoDeFV;
    
	
	//ORDENAMIENTO GENERICO
	 private String esDescendente;
    
    /**
     * Detalle para la forma de pago de bonos
     */
	private ArrayList<DtoDetallePagosBonos> formaPagoBonos;
	
	/**
	 * Lsitado de las entidades financieras
	 */
	private ArrayList<DtoEntidadesFinancieras> entidadesFinancieras;
	
	/**
	 * Campo temporal para imprimir la factura de la venta tarjeta en el recibo de caja
	 * se hace as&iacute; por falta de tiempo, la l&oacute;gica es que se llenen los datos del
	 * recibo de caja
	 */
	@Deprecated
	private String facturaTarjeta="";

	/**
	 * Campo temporal para imprimir el compardo de la venta tarjeta en el recibo de caja
	 * se hace as&iacute; por falta de tiempo, la l&oacute;gica es que se llenen los datos del
	 * recibo de caja
	 */
	@Deprecated
	private String comprador="";
	
	
	/**
	 * Atributo para Maneja Si la vista aplica venta
	 */
	private String aplicaVentaTarjeta="";
	
	
	
	/**
	 * Atributo que indica la actividad que se está realizando en la agenda odontológica
	 */
	private String actividadAgenda;
	
	/**
	 * Atributo que contiene la informacion de los conceptos de tipo
	 * Abono
	 */
	private HashMap mapaConceptosFlujoAbonosAgenda;
	
	/**
	 * Atributo que indica si se debe mostrar el consecutivo de la Factura Varia
	 */
	private boolean mostrarConsecutivoFv;
	
	
	/**
	 * Indica si se debe cerrar la ventana /popup abierto
	 */
	private String cerrarVentanita = "N";
    
	
    /**
	 * Atributo que indica para cerrar el popup de tarjetas de credito/debito
	 */
	private boolean cerrarPopupTarjetas = false;
	
    /**
     * reset de la clase
     *
     */
    public void reset (boolean resetNumReciboCaja,int institucion, HttpServletRequest request)
    {        
       this.mapaConvenios=new HashMap();
       this.mapaConceptos=new HashMap();
       this.mapaConceptosAuxiliarFlujoFact= new HashMap();
       this.mapaConceptosFlujoAbonosAgenda= new HashMap();
       this.mapaFacturas=new HashMap();
       this.mapaPacientes=new HashMap ();
       this.valoresConceptos=new HashMap();
       this.mapaFormasPago=new HashMap();  
       this.mapaCheques=new HashMap();
       this.mapaTarjetas=new HashMap();
       this.abrirPopUp="";       
       this.fechaFact="";
       this.identificacionPaciente="";       
       this.tipoIdentificacion="";
       this.valorConcepto="";      
       this.patronOrdenar="";
       this.ultimoPatron="";       
       this.primerNombre="";
       this.segundoNombre="";
       this.primerApellido="";
       this.segundoApellido="";          
       this.observaciones="";
       
       if(resetNumReciboCaja)
    	   this.observacionesImprimir="";
       
       this.recibidoDe="";
       this.posSelConcepto=ConstantesBD.codigoNuncaValido;
       this.posSelFormaPago=ConstantesBD.codigoNuncaValido;
       this.regSeleccionado=ConstantesBD.codigoNuncaValido;
       this.codigoTipoDetalleFormaPago=ConstantesBD.codigoNuncaValido;
       this.codigoTipoIngresoTesoreria=ConstantesBD.codigoNuncaValido;
       this.consecutivoFact=ConstantesBD.codigoNuncaValido;
       this.posEliminarC=ConstantesBD.codigoNuncaValido;
       this.numRegCheques=0;
       this.numRegTarjetas=ConstantesBD.codigoNuncaValido;
       this.tipoDePoUp=""; 
       this.totalConceptos="0";
       this.totalPagos="0";
       this.pais="";
       this.paisPlaza="";
       this.ciudad="";
       
       if(resetNumReciboCaja)
       {
           this.numReciboCaja="";
           this.consecutivoReciboCaja="";
       }
       this.mapaTotalPagos=new HashMap();
       this.manejaConversionMoneda=UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaConversionMonedaExtranjera(institucion));
       
       this.tiposMonedaTagMap= UtilConversionMonedas.obtenerTiposMonedaTagMap(institucion, /*mostrarMonedaManejaInstitucion*/false);
       this.indexTipoMoneda=ConstantesBD.codigoNuncaValido+""+ConstantesBD.separadorSplit+ConstantesBD.codigoNuncaValido;
       
       this.empresaFiltro="";
       this.convenioFiltro="";
       this.identificacionTerceroFiltro="";
       
       
       // cambio anexo 791 - cambio multas citas incumplidas
       this.conceptoFVarias = new ArrayList<DtoConceptoFacturaVaria>();
       this.recibosCaja = new ArrayList<DtoRecibosCaja>(); 
       this.conceptoFacVarias =request==null?"":request.getParameter("conceptoFacVarias");
       this.nroFactura=request==null?"":request.getParameter("nroFactura");
       this.tipoDeudor =request==null?"":request.getParameter("tipoDeudor");
       this.codigoDeudor =request==null?"":request.getParameter("codigoDeudor");
       this.descripcionDeudor = "" ;
       this.codigoMultaCita = "";
       this.fechaGenMultaCita = "";
       this.closeVenBusq = ConstantesBD.acronimoNo;
       this.mostrarMultasCitas = ConstantesBD.acronimoNo;
       this.tipoFiltro = "";
       // manejo de la busqueda del deudor
       this.idHiddenCodigo = "" ; 
       this.hacerSubmit = "" ;
       this.idHiddenDescripcion = "" ;
       this.idDiv = "" ;
       this.vieneTipoDeudor = "" ;
       this.tipoDeudo = "" ;
       
       this.ultimaPropiedad = "";
       this.columna = "";
       
       //************************************
       // Anexo 762
       this.datosFinanciacion = new DtoDatosFinanciacion();
       this.arrayTipIdent = new ArrayList<HashMap<String, Object>>();
       this.ingresoFac = ConstantesBD.codigoNuncaValido;
       this.seccionDeudor = ConstantesBD.acronimoNo;
       this.seccionCodeudor = ConstantesBD.acronimoNo;
       this.regCodeudor = ConstantesBD.acronimoNo;
       this.existDocGarAso = ConstantesBD.acronimoNo;
       this.paranGenVal = ConstantesBD.acronimoNo;
       this.messAdver= "";
       this.deudor = new DtoDeudor();
       this.resultBusDeu = new ArrayList<DtoDeudor>();
       this.rompimientoPac = new TreeSet<String>();
       this.isRomp = ConstantesBD.acronimoNo;
       this.paginaLinkSiguiente = "";
       this.patronOrdenarDeudores= "";
       this.posDeudorSel = ConstantesBD.codigoNuncaValido;
       this.valorFormaPago = "";
       this.existResPacIngFac = ConstantesBD.acronimoNo;
       this.datFinanGuarExis = ConstantesBD.acronimoNo;
       //************************************
       
	   //************* ANEXO 875 ***************
	   this.tipoAtencion = "";
	   //************* ANEXO 875 ***************
	   
	   //Anexo 958
		this.listadoConceptos=new ArrayList<DtoConceptosIngTesoreria>();
		this.concepto=request==null?"":request.getParameter("concepto");
		this.docSoporte=request==null?"":request.getParameter("docSoporte");
		this.beneficiario=request==null?"":request.getParameter("beneficiario");
		this.valor="";
		this.llegoDeFV=ConstantesBD.acronimoNo;
		this.setEsDescendente("");
		
		this.formaPagoBonos=new ArrayList<DtoDetallePagosBonos>();
		this.setAplicaVentaTarjeta(ConstantesBD.acronimoNo);
		
		//this.flujoAgendaOdontologica = false;
		
		this.actividadAgenda = "";
		
		this.setMostrarConsecutivoFv(false);
		cerrarVentanita = "N";
		
		cerrarPopupTarjetas = false;
    }

    public void  resetInfoVenta()
    {
    	this.facturaTarjeta="";
		this.comprador="";
    }

    
    
    /**
     * 
     * @param resetNumReciboCaja
     * @param institucion
     * @param copiaTempo -> al momento de cambiar el concepto trae los datos del concepto seleccionado 
     */
    public void resetConcepto(boolean resetNumReciboCaja,int institucion, HashMap copiaTempo) {        
       this.mapaConceptos=new HashMap();
       this.mapaConceptosAuxiliarFlujoFact= new HashMap();
       this.mapaConceptosFlujoAbonosAgenda= new HashMap();
       //this.posSelConcepto= Utilidades.convertirAEntero(copiaTempo.get("seleccion")+"");
       //this.mapaConceptos.put("codigo_concepto_"+ this.posSelConcepto, copiaTempo.get(""))
       
       this.valoresConceptos=new HashMap();
       this.valorConcepto="";      
       this.totalConceptos="0";

       this.mapaConvenios=new HashMap();
       this.mapaFacturas=new HashMap();
       this.mapaPacientes=new HashMap ();
       this.mapaFormasPago=new HashMap();  
       this.mapaCheques=new HashMap();
       this.mapaTarjetas=new HashMap();
       this.abrirPopUp="";       
       this.fechaFact="";
       this.identificacionPaciente="";       
       this.tipoIdentificacion="";
       this.patronOrdenar="";
       this.ultimoPatron="";       
       this.primerNombre="";
       this.segundoNombre="";
       this.primerApellido="";
       this.segundoApellido="";          
       this.observaciones="";
       
       this.recibidoDe="";
       this.posSelFormaPago=ConstantesBD.codigoNuncaValido;
       this.regSeleccionado=ConstantesBD.codigoNuncaValido;
       this.codigoTipoDetalleFormaPago=ConstantesBD.codigoNuncaValido;
       this.codigoTipoIngresoTesoreria=ConstantesBD.codigoNuncaValido;
       this.consecutivoFact=ConstantesBD.codigoNuncaValido;
       this.posEliminarC=ConstantesBD.codigoNuncaValido;
       this.numRegCheques=0;
       this.numRegTarjetas=ConstantesBD.codigoNuncaValido;
       this.tipoDePoUp=""; 
       this.totalPagos="0";
       this.pais="";
       this.paisPlaza="";
       this.ciudad="";
       this.consecutivoReciboCaja="";
       this.mapaTotalPagos=new HashMap();
       this.manejaConversionMoneda=UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaConversionMonedaExtranjera(institucion));
       
       this.tiposMonedaTagMap= UtilConversionMonedas.obtenerTiposMonedaTagMap(institucion, /*mostrarMonedaManejaInstitucion*/false);
       this.indexTipoMoneda=ConstantesBD.codigoNuncaValido+""+ConstantesBD.separadorSplit+ConstantesBD.codigoNuncaValido;
       
       this.empresaFiltro="";
       this.convenioFiltro="";
       this.identificacionTerceroFiltro="";
       
       if(resetNumReciboCaja){
    	   this.observacionesImprimir="";
           this.numReciboCaja="";
       }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
	 * Metodo de validación
	 * @param mapping
	 * @param request
	 * @return errores ActionError, especifica los errores.
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		int pos=0;
		if(this.estado.equals("generarReciboCaja"))
		{
		    if(this.recibidoDe.equals(""))
		    {
		        errores.add("recibidoDe requerido", new ActionMessage("errors.required", "El campo recibido de"));
		    }
		    /*if(Utilidades.convertirAEntero(this.mapaConceptos.get("numReg")+"")==1)
		    {
		        errores.add("minimo un concepto", new ActionMessage("error.recibosCaja.minimoUnConceptoRequerido"));
		    }*/
		    
		    Utilidades.imprimirMapa(this.mapaConceptos);
		    
		    for(int k=0;k<Utilidades.convertirAEntero(this.mapaConceptos.get("numReg")+"");k++)
		    {
		    
		    	
		      String desc=(this.mapaConceptos.get("descripcion_concepto_"+k)+"");
		    
		      if(Utilidades.convertirAEntero(this.mapaConceptos.get("codigoTipoConcepto_"+k)+"")==ConstantesBD.codigoTipoIngresoTesoreriaNinguno)
		      {
		          if((this.mapaConceptos.get("nombreBeneficiario_"+k)+"").equals(""))
			      {	
		              pos=k;pos++;
		              errores.add("minimo un concepto", new ActionMessage("errors.required","EL NOMBRE DEL BENEFICIARIO PARA EL CONCEPTO "+desc+" NUMERO DE REGISTRO "+pos+""));   
			      }
		          if((this.mapaConceptos.get("tipoBeneficiario_"+k)+"").equals(""))
			      {	
		              pos=k;pos++;
		              errores.add("minimo un concepto", new ActionMessage("errors.required","EL TIPO IDENTIFICACION DEL BENEFICIARIO PARA EL CONCEPTO "+desc+" NUMERO DE REGISTRO "+pos+""));   
			      }
		          if((this.mapaConceptos.get("numeroBeneficiario_"+k)+"").equals(""))
			      {	
		              pos=k;pos++;
		              errores.add("minimo un concepto", new ActionMessage("errors.required","EL NUMERO IDENTIFICACION DEL BENEFICIARIO PARA EL CONCEPTO "+desc+" NUMERO DE REGISTRO "+pos+""));   
			      }
		          if((this.mapaConceptos.get("valorConcepto_"+k)+"").equals(""))
			      {	
		              pos=k;pos++;
		              errores.add("minimo un concepto", new ActionMessage("errors.required","EL VALOR PARA EL CONCEPTO "+desc+" NUMERO DE REGISTRO "+pos+""));   
			      }
		      }
	          if(Utilidades.convertirAEntero(this.mapaConceptos.get("codigoTipoConcepto_"+k)+"")==ConstantesBD.codigoTipoIngresoTesoreriaPacientes)	
	          {
	              if((this.mapaConceptos.get("poseeRegistroSeleccionado_"+k)+"").equals("false"))
			      {		              
		              pos=k;pos++;
		              errores.add("minimo un concepto", new ActionMessage("error.recibosCaja.faltaEleccionDelRegistro","UNA FACTURA",desc,pos+""));
			      }
	              if((this.mapaConceptos.get("docSoporte_"+k)+"").trim().equals(""))
	              {
	                  pos=k;pos++;
		              errores.add("minimo un concepto", new ActionMessage("errors.required","EL DOCUMENTO PARA EL CONCEPTO "+desc+" NUMERO DE REGISTRO "+pos+""));  
	              }
	              if((this.mapaConceptos.get("nombreBeneficiario_"+k)+"").equals(""))
			      {	
		              pos=k;pos++;
		              errores.add("minimo un concepto", new ActionMessage("errors.required","EL NOMBRE DEL BENEFICIARIO PARA EL CONCEPTO "+desc+" NUMERO DE REGISTRO "+pos+""));   
			      }
		          if((this.mapaConceptos.get("tipoBeneficiario_"+k)+"").equals(""))
			      {	
		              pos=k;pos++;
		              errores.add("minimo un concepto", new ActionMessage("errors.required","EL TIPO IDENTIFICACION DEL BENEFICIARIO PARA EL CONCEPTO "+desc+" NUMERO DE REGISTRO "+pos+""));   
			      }
		          if((this.mapaConceptos.get("numeroBeneficiario_"+k)+"").equals(""))
			      {	
		              pos=k;pos++;
		              errores.add("minimo un concepto", new ActionMessage("errors.required","EL NUMERO IDENTIFICACION DEL BENEFICIARIO PARA EL CONCEPTO "+desc+" NUMERO DE REGISTRO "+pos+""));   
			      }
		          if((this.mapaConceptos.get("valorConcepto_"+k)+"").equals(""))
			      {	
		              pos=k;pos++;
		              errores.add("minimo un concepto", new ActionMessage("errors.required","EL VALOR PARA EL CONCEPTO "+desc+" NUMERO DE REGISTRO "+pos+""));   
			      }		          
		          else
		          {
		        	  try
		        	  {
		        		  if(Double.parseDouble(this.mapaConceptos.get("valorConcepto_"+k)+"")>Double.parseDouble(this.valoresConceptos.get("valorConcepto_"+k)+""))
		        		  {
		        			  pos=k;pos++;
		        			  errores.add("minimo un concepto", new ActionMessage("error.recibosCaja.valorConceptoMayor",desc+"",pos+""));  
		        		  }
				      }
		        	  catch (NumberFormatException e) 
				      {
		        		  pos=k;pos++;
	        			  errores.add("minimo un concepto", new ActionMessage("error.errorEnBlanco","EL VALOR PARA EL CONCEPTO "+desc+" NUMERO DE REGISTRO "+pos+" DEBE SER UN NUMERO DECIMAL POSITIVO."));
				      }
		          }
	          }
	          if(Utilidades.convertirAEntero(this.mapaConceptos.get("codigoTipoConcepto_"+k)+"")==ConstantesBD.codigoTipoIngresoTesoreriaConvenios
	        		  ||Utilidades.convertirAEntero(this.mapaConceptos.get("codigoTipoConcepto_"+k)+"")==ConstantesBD.codigoTipoIngresoTesoreriaAnticipoConvenioOdon)	
	          {
	        	  
	              if((this.mapaConceptos.get("poseeRegistroSeleccionado_"+k)+"").equals("false"))
			      {		              
		              pos=k;pos++;
		              errores.add("minimo un concepto", new ActionMessage("error.recibosCaja.faltaEleccionDelRegistro","UN CONVENIO",desc,pos+""));
			      }
	              if((this.mapaConceptos.get("nombreBeneficiario_"+k)+"").equals(""))
			      {	
		              pos=k;pos++;
		              errores.add("minimo un concepto", new ActionMessage("errors.required","EL NOMBRE DEL BENEFICIARIO PARA EL CONCEPTO "+desc+" NUMERO DE REGISTRO "+pos+""));   
			      }
	              
	              //if(!UtilidadTexto.getBoolean(this.getEsWorflowFactura()))
	              //{	
	              	 // Tipo de Identificacion No Aplica Ingresos Tesoreria Convenios
			          /*if((this.mapaConceptos.get("tipoBeneficiario_"+k)+"").equals(""))
				      {	
			              pos=k;pos++;
			              errores.add("minimo un concepto", new ActionMessage("errors.required","EL TIPO IDENTIFICACION DEL BENEFICIARIO PARA EL CONCEPTO "+desc+" NUMERO DE REGISTRO "+pos+""));   
				      }*/
	              //}    
		          if((this.mapaConceptos.get("numeroBeneficiario_"+k)+"").equals(""))
			      {	
		              pos=k;pos++;
		              errores.add("minimo un concepto", new ActionMessage("errors.required","EL NUMERO IDENTIFICACION DEL BENEFICIARIO PARA EL CONCEPTO "+desc+" NUMERO DE REGISTRO "+pos+""));   
			      }
		          if((this.mapaConceptos.get("valorConcepto_"+k)+"").equals(""))
			      {	
		              pos=k;pos++;
		              errores.add("minimo un concepto", new ActionMessage("errors.required","EL VALOR PARA EL CONCEPTO "+desc+" NUMERO DE REGISTRO "+pos+""));   
			      }
	          }
	          if(Utilidades.convertirAEntero(this.mapaConceptos.get("codigoTipoConcepto_"+k)+"")==ConstantesBD.codigoTipoIngresoTesoreriaAbonos)	
	          {
	              if((this.mapaConceptos.get("poseeRegistroSeleccionado_"+k)+"").equals("false"))
			      {		              
		              pos=k;pos++;
		              errores.add("minimo un concepto", new ActionMessage("error.recibosCaja.faltaEleccionDelRegistro","UN PACIENTE",desc,pos+""));
			      }
	              if((this.mapaConceptos.get("nombreBeneficiario_"+k)+"").equals(""))
			      {	
		              pos=k;pos++;
		              errores.add("minimo un concepto", new ActionMessage("errors.required","EL NOMBRE DEL BENEFICIARIO PARA EL CONCEPTO "+desc+" NUMERO DE REGISTRO "+pos+""));   
			      }
		          if((this.mapaConceptos.get("tipoBeneficiario_"+k)+"").equals(""))
			      {	
		              pos=k;pos++;
		              errores.add("minimo un concepto", new ActionMessage("errors.required","EL TIPO IDENTIFICACION DEL BENEFICIARIO PARA EL CONCEPTO "+desc+" NUMERO DE REGISTRO "+pos+""));   
			      }
		          if((this.mapaConceptos.get("numeroBeneficiario_"+k)+"").equals(""))
			      {	
		              pos=k;pos++;
		              errores.add("minimo un concepto", new ActionMessage("errors.required","EL NUMERO IDENTIFICACION DEL BENEFICIARIO PARA EL CONCEPTO "+desc+" NUMERO DE REGISTRO "+pos+""));   
			      }
		          if((this.mapaConceptos.get("valorConcepto_"+k)+"").equals(""))
			      {	
		              pos=k;pos++;
		              errores.add("minimo un concepto", new ActionMessage("errors.required","EL VALOR PARA EL CONCEPTO "+desc+" NUMERO DE REGISTRO "+pos+""));   
			      }
	          }	
	          if(Utilidades.convertirAEntero(this.mapaConceptos.get("codigoTipoConcepto_"+k)+"")==ConstantesBD.codigoTipoIngresoTesoreriaCarteraParticular)	
	          {
	              if((this.mapaConceptos.get("nombreBeneficiario_"+k)+"").equals(""))
			      {	
		              pos=k;pos++;
		              errores.add("minimo un concepto", new ActionMessage("errors.required","EL NOMBRE DEL BENEFICIARIO PARA EL CONCEPTO "+desc+" NUMERO DE REGISTRO "+pos+""));   
			      }
		          if((this.mapaConceptos.get("tipoBeneficiario_"+k)+"").equals(""))
			      {	
		              pos=k;pos++;
		              errores.add("minimo un concepto", new ActionMessage("errors.required","EL TIPO IDENTIFICACION DEL BENEFICIARIO PARA EL CONCEPTO "+desc+" NUMERO DE REGISTRO "+pos+""));   
			      }
		          if((this.mapaConceptos.get("numeroBeneficiario_"+k)+"").equals(""))
			      {	
		              pos=k;pos++;
		              errores.add("minimo un concepto", new ActionMessage("errors.required","EL NUMERO IDENTIFICACION DEL BENEFICIARIO PARA EL CONCEPTO "+desc+" NUMERO DE REGISTRO "+pos+""));   
			      }  
		          if((this.mapaConceptos.get("valorConcepto_"+k)+"").equals(""))
			      {	
		              pos=k;pos++;
		              errores.add("minimo un concepto", new ActionMessage("errors.required","EL VALOR PARA EL CONCEPTO "+desc+" NUMERO DE REGISTRO "+pos+""));   
			      }
	          }
	          if(Utilidades.convertirAEntero(this.mapaConceptos.get("codigoTipoConcepto_"+k)+"")==ConstantesBD.codigoTipoIngresoTesoreriaOtrasCxC)	
	          {
	              if((this.mapaConceptos.get("nombreBeneficiario_"+k)+"").equals(""))
			      {	
		              pos=k;pos++;
		              errores.add("minimo un concepto", new ActionMessage("errors.required","EL NOMBRE DEL BENEFICIARIO PARA EL CONCEPTO "+desc+" NUMERO DE REGISTRO "+pos+""));   
			      }
		          if((this.mapaConceptos.get("tipoBeneficiario_"+k)+"").equals(""))
			      {	
		              pos=k;pos++;
		              errores.add("minimo un concepto", new ActionMessage("errors.required","EL TIPO IDENTIFICACION DEL BENEFICIARIO PARA EL CONCEPTO "+desc+" NUMERO DE REGISTRO "+pos+""));   
			      }
		          if((this.mapaConceptos.get("numeroBeneficiario_"+k)+"").equals(""))
			      {	
		              pos=k;pos++;
		              errores.add("minimo un concepto", new ActionMessage("errors.required","EL NUMERO IDENTIFICACION DEL BENEFICIARIO PARA EL CONCEPTO "+desc+" NUMERO DE REGISTRO "+pos+""));   
			      } 
		          if((this.mapaConceptos.get("valorConcepto_"+k)+"").equals(""))
			      {	
		              pos=k;pos++;
		              errores.add("minimo un concepto", new ActionMessage("errors.required","EL VALOR PARA EL CONCEPTO "+desc+" NUMERO DE REGISTRO "+pos+""));   
			      }
	          }
		    }
		    //validación de los totales del recibo
		    
		    if(Utilidades.convertirADouble(this.totalConceptos) == 0)
		    {
		    	errores.add("total conceptos", new ActionMessage("error.recibosCaja.valorConceptoCero"));
		    }
		    
		    if(Utilidades.convertirADouble(this.totalConceptos)!=Utilidades.convertirADouble(this.totalPagos))
		    {
		        errores.add("totales descuadrados", new ActionMessage("error.recibosCaja.totalesReciboDescuadrado")); 
		    }
		    if(!this.mapaFormasPago.isEmpty())
		    {
		    	//validaciones de los campos requeridos de formas de pago
		    	for(int k=0;k<Utilidades.convertirAEntero(this.mapaFormasPago.get("numReg")+"")-1;k++)
		    	{			   
		    		if(!this.mapaFormasPago.get("tipoDetalle_"+pos).equals(ConstantesBD.codigoTipoDetalleFormasPagoBono+""))
		    		{
		    			if((this.mapaFormasPago.get("entidadFormaPago_"+k)+"").equals(""))
			    		{
			    			pos=k;
			    			errores.add("entidad requerida", new ActionMessage("errors.required","La entidad para el registro número "+(++pos+"")+" de la forma de pago"));  
			    		}
		    		}
		    	}
		    	//validar que la información necesaria de los cheques se encuentre ingresada
		    	if(!this.mapaCheques.isEmpty())
		    	{			        
		    		for(int k=0;k<Utilidades.convertirAEntero(this.mapaCheques.get("numReg")+"");k++)
		    		{
		    			if(this.mapaCheques.containsKey("numeroRegistroFormaPago_"+k))//depende de la posicion que se genero el cheque, debido a la misma posicion en el mapa de formas de pago
		    			{
		    				//en caso de que exista entonces toca verificar que no hallan cambiado la forma de pago
		    				pos=Utilidades.convertirAEntero(this.mapaCheques.get("numeroRegistroFormaPago_"+k)+"");
		    				if(this.mapaFormasPago.get("tipoDetalle_"+pos).equals(ConstantesBD.codigoTipoDetalleFormasPagoCheque+""))
		    				{
		    					if((this.mapaCheques.get("informacionCompletaDelCheque_"+k)+"").equals("false"))
		    					{
		    						errores.add("totales descuadrados", new ActionMessage("error.recibosCaja.reciboCajaDescuadrado",(++pos)+""));
		    					}
		    				}
		    				else
		    					this.mapaCheques.put("esOtraFormaPago_"+k, "true");
		    			}
		    		}
		    	}
		    	//validar que la informacón necesaria de las tarjetas de credito se encuentre ingresada
		    	if(!this.mapaTarjetas.isEmpty())
		    	{
		    		for(int k=0;k<Utilidades.convertirAEntero(this.mapaTarjetas.get("numReg")+"");k++)
		    		{
		    			if(this.mapaTarjetas.containsKey("numeroRegistroFormaPago_"+k))//depende de la posicion que se genero el cheque, debido a la misma posicion en el mapa de formas de pago
		    			{
		    				//en caso de que exista entonces toca verificar que no hallan cambiado la forma de pago
		    				pos=Utilidades.convertirAEntero(this.mapaTarjetas.get("numeroRegistroFormaPago_"+k)+"");
		    				if(this.mapaFormasPago.get("tipoDetalle_"+pos).equals(ConstantesBD.codigoTipoDetalleFormasPagoTarjeta+""))
		    				{
		    					if((this.mapaTarjetas.get("informacionCompletaTarjeta_"+k)+"").equals("false"))
		    					{
		    						errores.add("totales descuadrados", new ActionMessage("error.recibosCaja.reciboCajaDescuadrado",(++pos)+""));
		    					}
						    }
				            else
				            	this.mapaTarjetas.put("esOtraFormaPago_"+k, "true");
				    	}    
				    }  
			    }
			    
			    // validar que la informacion necesarioa para formas de pago pagare o letra se encuentre ingresadas
			    String help = "";
			    for(int z=0;z<Utilidades.convertirAEntero(this.mapaFormasPago.get("numReg")+"")-1;z++)
			    {			      
			    	if(Utilidades.convertirAEntero(this.mapaFormasPago.get("tipoDetalle_"+z)+"")==ConstantesBD.codigoTipoDetalleFormasPagoPagare
			    			|| Utilidades.convertirAEntero(this.mapaFormasPago.get("tipoDetalle_"+z)+"")==ConstantesBD.codigoTipoDetalleFormasPagoLetra)
			    	{
			    		if(!this.datFinanGuarExis.equals(ConstantesBD.acronimoSi))
			    		{	  
			    			help = Utilidades.convertirAEntero(this.mapaFormasPago.get("tipoDetalle_"+z)+"")==ConstantesBD.codigoTipoDetalleFormasPagoPagare?"Pagaré":"Letra";
			    			pos=z;
			    			errores.add("descripcion", new ActionMessage("errors.notEspecific","Es requerido el Diligenciamiento de los Datos de Financiacón de la Forma de Pago "+help+" de la posición["+(++pos+"")+"]. "));  
			    		}
		        	  
			    		if(!this.mapaFormasPago.get("valorFormaPago_"+z).toString().equals(""))
			    		{
		        		  
			    			if(this.datosFinanciacion.getValorForPago().doubleValue() != Utilidades.convertirADouble(this.mapaFormasPago.get("valorFormaPago_"+z).toString()))
			    			{
			    				help = Utilidades.convertirAEntero(this.mapaFormasPago.get("tipoDetalle_"+z)+"")==ConstantesBD.codigoTipoDetalleFormasPagoPagare?"Pagaré":"Letra";
			    				pos=z;
			    				errores.add("descripcion", new ActionMessage("errors.notEspecific","EL valor de la Forma de Pago "+help+" de la posición["+(++pos+"")+"] no coincide con la sumatoria de la cuotas. Por Favor Verificar"));
			    			}
			    		}
			    	}
			    }
			    
		    }
		}
		
		if(!errores.isEmpty()){
			this.setCodigoTipoIngresoTesoreria(ConstantesBD.codigoNuncaValido);
		    this.setCodigoTipoDetalleFormaPago(ConstantesBD.codigoNuncaValido);
		}
		return errores;
	}
	
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
     * @return Retorna recibidoDe.
     */
    public String getRecibidoDe() {
        return recibidoDe;
    }
    /**
     * @param recibidoDe Asigna recibidoDe.
     */
    public void setRecibidoDe(String recibidoDe) {
        this.recibidoDe = recibidoDe;
    }
    /**
     * @return Retorna mapaConvenios.
     */
    public HashMap getMapaConvenios() {
        return mapaConvenios;
    }
    /**
     * @param mapaReciboCaja Asigna mapaConvenios.
     */
    public void setMapaConvenios(HashMap mapaConvenios) {
        this.mapaConvenios = mapaConvenios;
    }
    /**
     * @return Retorna mapaConvenios.
     */
    public Object getMapaConvenios(String key) {
        return mapaConvenios.get(key);
    }
    
    /**
     * @param mapaReciboCaja Asigna mapaConvenios.
     */
    public void setMapaConvenios(String key,Object value) {
        this.mapaConvenios.put(key,value);
    }
    /**
     * @return Retorna mapaConceptos.
     */
    public HashMap getMapaConceptos() {
        return mapaConceptos;
    }
    /**
     * @param mapaConceptos Asigna mapaConceptos.
     */
    public void setMapaConceptos(HashMap mapaConceptos) {
        this.mapaConceptos = mapaConceptos;
    }
    /**
     * @return Retorna mapaConceptos.
     */
    public Object getMapaConceptos(String key) {
        return mapaConceptos.get(key);
    }
    /**
     * @param mapaConceptos Asigna mapaConceptos.
     */
    public void setMapaConceptos(String key,Object value) {
        this.mapaConceptos.put(key,value);
    }
    /**
     * @return Retorna posSelConcepto.
     */
    public int getPosSelConcepto() {
        return posSelConcepto;
    }
    /**
     * @param posSelConcepto Asigna posSelConcepto.
     */
    public void setPosSelConcepto(int posSelConcepto) {
        this.posSelConcepto = posSelConcepto;
    }
    /**
     * @return Retorna abrirPopUp.
     */
    public String getAbrirPopUp() {
        return abrirPopUp;
    }
    /**
     * @param abrirPopUpConcepto Asigna abrirPopUp.
     */
    public void setAbrirPopUp(String abrirPopUp) {
        this.abrirPopUp = abrirPopUp;
    }
    /**
     * @return Retorna codigoTipoIngresoTesoreria.
     */
    public int getCodigoTipoIngresoTesoreria() {
        return codigoTipoIngresoTesoreria;
    }
    /**
     * @param codigoTipoIngresoTesoreria Asigna codigoTipoIngresoTesoreria.
     */
    public void setCodigoTipoIngresoTesoreria(int codigoTipoIngresoTesoreria) {
        this.codigoTipoIngresoTesoreria = codigoTipoIngresoTesoreria;
    }
    /**
     * @return Retorna consecutivoFact.
     */
    public int getConsecutivoFact() {
        return consecutivoFact;
    }
    /**
     * @param consecutivoFact Asigna consecutivoFact.
     */
    public void setConsecutivoFact(int consecutivoFact) {
        this.consecutivoFact = consecutivoFact;
    }
    /**
     * @return Retorna fechaFact.
     */
    public String getFechaFact() {
        return fechaFact;
    }
    /**
     * @param fechaFact Asigna fechaFact.
     */
    public void setFechaFact(String fechaFact) {
        this.fechaFact = fechaFact;
    }
    /**
     * @return Retorna identificacionPaciente.
     */
    public String getIdentificacionPaciente() {
        return identificacionPaciente;
    }
    /**
     * @param paciente Asigna identificacionPaciente.
     */
    public void setIdentificacionPaciente(String identificacionPaciente) {
        this.identificacionPaciente = identificacionPaciente;
    }
    /**
     * @return Retorna mapaFacturas.
     */
    public HashMap getMapaFacturas() {
        return mapaFacturas;
    }
    /**
     * @param mapaFacturas Asigna mapaFacturas.
     */
    public void setMapaFacturas(HashMap mapaFacturas) {
        this.mapaFacturas = mapaFacturas;
    }
    /**
     * @return Retorna mapaFacturas.
     */
    public Object getMapaFacturas(String key) {
        return mapaFacturas.get(key);
    }
    /**
     * @param mapaFacturas Asigna mapaFacturas.
     */
    public void setMapaFacturas(String key,Object value) {
        this.mapaFacturas.put(key,value);
    }
    /**
     * @return Retorna tipoIdentificacion.
     */
    public String getTipoIdentificacion() {
        return tipoIdentificacion;
    }
    /**
     * @param tipoIdentificacion Asigna tipoIdentificacion.
     */
    public void setTipoIdentificacion(String tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }
    /**
     * @return Retorna valorConcepto.
     */
    public String getValorConcepto() {
        return valorConcepto;
    }
    /**
     * @param valorConcepto Asigna valorConcepto.
     */
    public void setValorConcepto(String valorConcepto) {
        this.valorConcepto = valorConcepto;
    }    
    /**
     * @return Retorna patronOrdenar.
     */
    public String getPatronOrdenar() {
        return patronOrdenar;
    }
    /**
     * @param patronOrdenar Asigna patronOrdenar.
     */
    public void setPatronOrdenar(String patronOrdenar) {
        this.patronOrdenar = patronOrdenar;
    }
    /**
     * @return Retorna ultimoPatron.
     */
    public String getUltimoPatron() {
        return ultimoPatron;
    }
    /**
     * @param ultimoPatron Asigna ultimoPatron.
     */
    public void setUltimoPatron(String ultimoPatron) {
        this.ultimoPatron = ultimoPatron;
    }
    /**
     * @return Retorna posEliminarC.
     */
    public int getPosEliminarC() {
        return posEliminarC;
    }
    /**
     * @param posEliminarC Asigna posEliminarC.
     */
    public void setPosEliminarC(int posEliminarC) {
        this.posEliminarC = posEliminarC;
    }
    /**
     * @return Retorna primerApellido.
     */
    public String getPrimerApellido() {
        return primerApellido;
    }
    /**
     * @param primerApellido Asigna primerApellido.
     */
    public void setPrimerApellido(String primerApellido) {
        this.primerApellido = primerApellido;
    }
    /**
     * @return Retorna primerNombre.
     */
    public String getPrimerNombre() {
        return primerNombre;
    }
    /**
     * @param primerNombre Asigna primerNombre.
     */
    public void setPrimerNombre(String primerNombre) {
        this.primerNombre = primerNombre;
    }
    /**
     * @return Retorna segundoApellido.
     */
    public String getSegundoApellido() {
        return segundoApellido;
    }
    /**
     * @param segundoApellido Asigna segundoApellido.
     */
    public void setSegundoApellido(String segundoApellido) {
        this.segundoApellido = segundoApellido;
    }
    /**
     * @return Retorna segundoNombre.
     */
    public String getSegundoNombre() {
        return segundoNombre;
    }
    /**
     * @param segundoNombre Asigna segundoNombre.
     */
    public void setSegundoNombre(String segundoNombre) {
        this.segundoNombre = segundoNombre;
    }
    /**
     * @return Retorna mapaPacientes.
     */
    public HashMap getMapaPacientes() {
        return mapaPacientes;
    }
    /**
     * @param mapaPacientes Asigna mapaPacientes.
     */
    public void setMapaPacientes(HashMap mapaPacientes) {
        this.mapaPacientes = mapaPacientes;
    }
    /**
     * @return Retorna mapaPacientes.
     */
    public Object getMapaPacientes(String key) {
        return mapaPacientes.get(key);
    }
    /**
     * @param mapaPacientes Asigna mapaPacientes.
     */
    public void setMapaPacientes(String key, Object value) {
        this.mapaPacientes.put(key,value);
    }
    /**
     * @return Retorna regSeleccionado.
     */
    public int getRegSeleccionado() {
        return regSeleccionado;
    }
    /**
     * @param regSeleccionado Asigna regSeleccionado.
     */
    public void setRegSeleccionado(int regSeleccionado) {
        this.regSeleccionado = regSeleccionado;
    }
    /**
     * @return Retorna observaciones.
     */
    public String getObservaciones() {
        return observaciones;
    }
    /**
     * @param observaciones Asigna observaciones.
     */
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
    /**
     * @return Retorna valoresConceptos.
     */
    public HashMap getValoresConceptos() {
        return valoresConceptos;
    }
    /**
     * @param valoresConceptos Asigna valoresConceptos.
     */
    public void setValoresConceptos(HashMap valoresConceptos) {
        this.valoresConceptos = valoresConceptos;
    }
    /**
     * @return Retorna valoresConceptos.
     */
    public Object getValoresConceptos(String key) {
        return valoresConceptos.get(key);
    }
    /**
     * @param valoresConceptos Asigna valoresConceptos.
     */
    public void setValoresConceptos(String key,Object value) {
        this.valoresConceptos.put(key,value);
    }
    /**
     * @return Retorna mapaFormasPago.
     */
    public HashMap getMapaFormasPago() {
        return mapaFormasPago;
    }
    /**
     * @param mapaFormasPago Asigna mapaFormasPago.
     */
    public void setMapaFormasPago(HashMap mapaFormasPago) {
        this.mapaFormasPago = mapaFormasPago;
    }
    /**
     * @return Retorna mapaFormasPago.
     */
    public Object getMapaFormasPago(String key) {
        return mapaFormasPago.get(key);
    }
    /**
     * @param mapaFormasPago Asigna mapaFormasPago.
     */
    public void setMapaFormasPago(String key,Object value) {
        this.mapaFormasPago.put(key,value);
    }
    /**
     * @return Retorna posSelFormaPago.
     */
    public int getPosSelFormaPago() {
        return posSelFormaPago;
    }
    /**
     * @param posSelFormaPago Asigna posSelFormaPago.
     */
    public void setPosSelFormaPago(int posSelFormaPago) {
        this.posSelFormaPago = posSelFormaPago;
    }
    /**
     * @return Retorna codigoTipoDetalleFormaPago.
     */
    public int getCodigoTipoDetalleFormaPago() {
        return codigoTipoDetalleFormaPago;
    }
    /**
     * @param codigoTipoDetalleFormaPago Asigna codigoTipoDetalleFormaPago.
     */
    public void setCodigoTipoDetalleFormaPago(int codigoTipoDetalleFormaPago) {
        this.codigoTipoDetalleFormaPago = codigoTipoDetalleFormaPago;
    }
    /**
     * @return Retorna mapaCheques.
     */
    public HashMap getMapaCheques() {
        return mapaCheques;
    }
    /**
     * @param mapaCheques Asigna mapaCheques.
     */
    public void setMapaCheques(HashMap mapaCheques) {
        this.mapaCheques = mapaCheques;
    }
    /**
     * @return Retorna mapaCheques.
     */
    public Object getMapaCheques(String key) {
        return mapaCheques.get(key);
    }
    /**
     * @param mapaCheques Asigna mapaCheques.
     */
    public void setMapaCheques(String key,Object value) {
        this.mapaCheques.put(key,value);
    }
    /**
     * @return Retorna mapaTarjetas.
     */
    public HashMap getMapaTarjetas() {
        return mapaTarjetas;
    }
    /**
     * @param mapaTarjetas Asigna mapaTarjetas.
     */
    public void setMapaTarjetas(HashMap mapaTarjetas) {
        this.mapaTarjetas = mapaTarjetas;
    }
    /**
     * @return Retorna mapaTarjetas.
     */
    public Object getMapaTarjetas(String key) {
        return mapaTarjetas.get(key);
    }
    /**
     * @param mapaTarjetas Asigna mapaTarjetas.
     */
    public void setMapaTarjetas(String key,Object value) {
        this.mapaTarjetas.put(key,value);
    }    
    /**
     * @return Retorna numRegCheques.
     */
    public int getNumRegCheques() {
        return numRegCheques;
    }
    /**
     * @param numRegCheques Asigna numRegCheques.
     */
    public void setNumRegCheques(int numRegCheques) {
        this.numRegCheques = numRegCheques;
    }
    /**
     * @return Retorna numRegTarjetas.
     */
    public int getNumRegTarjetas() {
        return numRegTarjetas;
    }
    /**
     * @param numRegTarjetas Asigna numRegTarjetas.
     */
    public void setNumRegTarjetas(int numRegTarjetas) {
        this.numRegTarjetas = numRegTarjetas;
    }
    /**
     * @return Retorna tipoDePoUp.
     */
    public String getTipoDePoUp() {
        return tipoDePoUp;
    }
    /**
     * @param tipoDePoUp Asigna tipoDePoUp.
     */
    public void setTipoDePoUp(String tipoDePoUp) {
        this.tipoDePoUp = tipoDePoUp;
    }    
    /**
     * @return Retorna totalConceptos.
     */
    public String getTotalConceptos() {
        return totalConceptos;
    }
    /**
     * @param totalConceptos Asigna totalConceptos.
     */
    public void setTotalConceptos(String totalConceptos) {
        this.totalConceptos = totalConceptos;
    }
    /**
     * @return Retorna totalPagos.
     */
    public String getTotalPagos() {
        return totalPagos;
    }
    /**
     * @param totalPagos Asigna totalPagos.
     */
    public void setTotalPagos(String totalPagos) {
        this.totalPagos = totalPagos;
    }
    /**
     * @return Retorna numReciboCaja.
     */
    public String getNumReciboCaja() {
        return numReciboCaja;
    }
    /**
     * @param numReciboCaja Asigna numReciboCaja.
     */
    public void setNumReciboCaja(String numReciboCaja) {
        this.numReciboCaja = numReciboCaja;
    }
	public HashMap getMapaTotalPagos() {
		return mapaTotalPagos;
	}
	public void setMapaTotalPagos(HashMap mapaTotalPagos) {
		this.mapaTotalPagos = mapaTotalPagos;
	}
	/**
	 * @return Returns the codigoConceptoIngTesoreriaFacturaUnica.
	 */
	public String getCodigoConceptoIngTesoreriaFacturaUnica() {
		return codigoConceptoIngTesoreriaFacturaUnica;
	}
	/**
	 * @param codigoConceptoIngTesoreriaFacturaUnica The codigoConceptoIngTesoreriaFacturaUnica to set.
	 */
	public void setCodigoConceptoIngTesoreriaFacturaUnica(
			String codigoConceptoIngTesoreriaFacturaUnica) {
		this.codigoConceptoIngTesoreriaFacturaUnica = codigoConceptoIngTesoreriaFacturaUnica;
	}
	/**
	 * @return Returns the descConceptoIngTesoreriaFacturaUnica.
	 */
	public String getDescConceptoIngTesoreriaFacturaUnica() {
		return descConceptoIngTesoreriaFacturaUnica;
	}
	/**
	 * @param descConceptoIngTesoreriaFacturaUnica The descConceptoIngTesoreriaFacturaUnica to set.
	 */
	public void setDescConceptoIngTesoreriaFacturaUnica(
			String descConceptoIngTesoreriaFacturaUnica) {
		this.descConceptoIngTesoreriaFacturaUnica = descConceptoIngTesoreriaFacturaUnica;
	}
	/**
	 * @return Returns the esWorflowFactura.
	 */
	public String getEsWorflowFactura() {
		return esWorflowFactura;
	}
	/**
	 * @param esWorflowFactura The esWorflowFactura to set.
	 */
	public void setEsWorflowFactura(String esWorflowFactura) {
		this.esWorflowFactura = esWorflowFactura;
	}

	/**
	 * 
	 * @return
	 */	
	public String getPais() {
		return pais;
	}
	
	/**
	 * 
	 * @param pais
	 */
	public void setPais(String pais) {
		this.pais = pais;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getPaisPlaza() {
		return paisPlaza;
	}
	
	/**
	 * 
	 * @param paisPlaza
	 */
	public void setPaisPlaza(String paisPlaza) {
		this.paisPlaza = paisPlaza;
	}
	public String getCiudad() {
		return ciudad;
	}
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}
	

	/**
	 * @return the tiposMonedaTagMap
	 */
	public HashMap getTiposMonedaTagMap() {
		return tiposMonedaTagMap;
	}

	/**
	 * @param tiposMonedaTagMap the tiposMonedaTagMap to set
	 */
	public void setTiposMonedaTagMap(HashMap tiposMonedaTagMap) {
		this.tiposMonedaTagMap = tiposMonedaTagMap;
	}

	/**
	 * @return the tiposMonedaTagMap
	 */
	public Object getTiposMonedaTagMap(Object key) {
		return tiposMonedaTagMap.get(key);
	}

	/**
	 * @param tiposMonedaTagMap the tiposMonedaTagMap to set
	 */
	public void setTiposMonedaTagMap(Object key, Object value) {
		this.tiposMonedaTagMap.put(key, value);
	}
	/**
	 * @return the manejaConversionMoneda
	 */
	public boolean isManejaConversionMoneda() {
		return manejaConversionMoneda;
	}
	/**
	 * @param manejaConversionMoneda the manejaConversionMoneda to set
	 */
	public void setManejaConversionMoneda(boolean manejaConversionMoneda) {
		this.manejaConversionMoneda = manejaConversionMoneda;
	}
	/**
	 * @return the indexTipoMoneda
	 */
	public String getIndexTipoMoneda() {
		return indexTipoMoneda;
	}
	/**
	 * @param indexTipoMoneda the indexTipoMoneda to set
	 */
	public void setIndexTipoMoneda(String indexTipoMoneda) {
		this.indexTipoMoneda = indexTipoMoneda;
	}
	/**
	 * @return the indexConversionOriginal
	 */
	public int getIndexConversionOriginal() {
		return indexConversionOriginal;
	}
	/**
	 * @param indexConversionOriginal the indexConversionOriginal to set
	 */
	public void setIndexConversionOriginal(int indexConversionOriginal) {
		this.indexConversionOriginal = indexConversionOriginal;
	}
	/**
	 * @return the convenioFiltro
	 */
	public String getConvenioFiltro() {
		return convenioFiltro;
	}
	/**
	 * @param convenioFiltro the convenioFiltro to set
	 */
	public void setConvenioFiltro(String convenioFiltro) {
		this.convenioFiltro = convenioFiltro;
	}
	/**
	 * @return the empresaFiltro
	 */
	public String getEmpresaFiltro() {
		return empresaFiltro;
	}
	/**
	 * @param empresaFiltro the empresaFiltro to set
	 */
	public void setEmpresaFiltro(String empresaFiltro) {
		this.empresaFiltro = empresaFiltro;
	}
	/**
	 * @return the identificacionTerceroFiltro
	 */
	public String getIdentificacionTerceroFiltro() {
		return identificacionTerceroFiltro;
	}
	/**
	 * @param identificacionTerceroFiltro the identificacionTerceroFiltro to set
	 */
	public void setIdentificacionTerceroFiltro(String identificacionTerceroFiltro) {
		this.identificacionTerceroFiltro = identificacionTerceroFiltro;
	}
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}
	/**
	 * @return the observacionesImprimir
	 */
	public String getObservacionesImprimir() {
		return observacionesImprimir;
	}
	/**
	 * @param observacionesImprimir the observacionesImprimir to set
	 */
	public void setObservacionesImprimir(String observacionesImprimir) {
		this.observacionesImprimir = observacionesImprimir;
	}

	/**
	 * @return the conceptoFVarias
	 */
	public ArrayList<DtoConceptoFacturaVaria> getConceptoFVarias() {
		return conceptoFVarias;
	}

	/**
	 * @param conceptoFVarias the conceptoFVarias to set
	 */
	public void setConceptoFVarias(
			ArrayList<DtoConceptoFacturaVaria> conceptoFVarias) {
		this.conceptoFVarias = conceptoFVarias;
	}

	/**
	 * @return the conceptoFacVarias
	 */
	public String getConceptoFacVarias() {
		return conceptoFacVarias;
	}

	/**
	 * @param conceptoFacVarias the conceptoFacVarias to set
	 */
	public void setConceptoFacVarias(String conceptoFacVarias) {
		this.conceptoFacVarias = conceptoFacVarias;
	}

	/**
	 * @return the idHiddenCodigo
	 */
	public String getIdHiddenCodigo() {
		return idHiddenCodigo;
	}

	/**
	 * @param idHiddenCodigo the idHiddenCodigo to set
	 */
	public void setIdHiddenCodigo(String idHiddenCodigo) {
		this.idHiddenCodigo = idHiddenCodigo;
	}

	/**
	 * @return the hacerSubmit
	 */
	public String getHacerSubmit() {
		return hacerSubmit;
	}

	/**
	 * @param hacerSubmit the hacerSubmit to set
	 */
	public void setHacerSubmit(String hacerSubmit) {
		this.hacerSubmit = hacerSubmit;
	}

	/**
	 * @return the idHiddenDescripcion
	 */
	public String getIdHiddenDescripcion() {
		return idHiddenDescripcion;
	}

	/**
	 * @param idHiddenDescripcion the idHiddenDescripcion to set
	 */
	public void setIdHiddenDescripcion(String idHiddenDescripcion) {
		this.idHiddenDescripcion = idHiddenDescripcion;
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
	 * @return the vieneTipoDeudor
	 */
	public String getVieneTipoDeudor() {
		return vieneTipoDeudor;
	}

	/**
	 * @param vieneTipoDeudor the vieneTipoDeudor to set
	 */
	public void setVieneTipoDeudor(String vieneTipoDeudor) {
		this.vieneTipoDeudor = vieneTipoDeudor;
	}

	/**
	 * @return the tipoDeudo
	 */
	public String getTipoDeudo() {
		return tipoDeudo;
	}

	/**
	 * @param tipoDeudo the tipoDeudo to set
	 */
	public void setTipoDeudo(String tipoDeudo) {
		this.tipoDeudo = tipoDeudo;
	}

	/**
	 * @return the codigoDeudor
	 */
	public String getCodigoDeudor() {
		return codigoDeudor;
	}

	/**
	 * @param codigoDeudor the codigoDeudor to set
	 */
	public void setCodigoDeudor(String codigoDeudor) {
		this.codigoDeudor = codigoDeudor;
	}

	/**
	 * @return the descripcionDeudor
	 */
	public String getDescripcionDeudor() {
		return descripcionDeudor;
	}

	/**
	 * @param descripcionDeudor the descripcionDeudor to set
	 */
	public void setDescripcionDeudor(String descripcionDeudor) {
		this.descripcionDeudor = descripcionDeudor;
	}

	/**
	 * @return the tipoDeudor
	 */
	public String getTipoDeudor() {
		return tipoDeudor;
	}

	/**
	 * @param tipoDeudor the tipoDeudor to set
	 */
	public void setTipoDeudor(String tipoDeudor) {
		this.tipoDeudor = tipoDeudor;
	}

	/**
	 * @return the codigoMultaCita
	 */
	public String getCodigoMultaCita() {
		return codigoMultaCita;
	}

	/**
	 * @param codigoMultaCita the codigoMultaCita to set
	 */
	public void setCodigoMultaCita(String codigoMultaCita) {
		this.codigoMultaCita = codigoMultaCita;
	}

	/**
	 * @return the fechaGenMultaCita
	 */
	public String getFechaGenMultaCita() {
		return fechaGenMultaCita;
	}

	/**
	 * @param fechaGenMultaCita the fechaGenMultaCita to set
	 */
	public void setFechaGenMultaCita(String fechaGenMultaCita) {
		this.fechaGenMultaCita = fechaGenMultaCita;
	}

	/**
	 * @return the nroFactura
	 */
	public String getNroFactura() {
		return nroFactura;
	}

	/**
	 * @param nroFactura the nroFactura to set
	 */
	public void setNroFactura(String nroFactura) {
		this.nroFactura = nroFactura;
	}

	/**
	 * @return the recibosCaja
	 */
	public ArrayList<DtoRecibosCaja> getRecibosCaja() {
		return recibosCaja;
	}

	/**
	 * @param recibosCaja the recibosCaja to set
	 */
	public void setRecibosCaja(ArrayList<DtoRecibosCaja> recibosCaja) {
		this.recibosCaja = recibosCaja;
	}
	
	public void resetBusqFacVarias()
	{
		this.recibosCaja.clear();
	}
	
	
	/**
	 * @return the readOnly
	 */
	public String getCloseVenBusq() {
		return closeVenBusq;
	}

	/**
	 * @param readOnly the readOnly to set
	 */
	public void setCloseVenBusq(String closeVenBusq) {
		this.closeVenBusq = closeVenBusq;
	}

	/**
	 * @return the mostrarMultasCitas
	 */
	public String getMostrarMultasCitas() {
		return mostrarMultasCitas;
	}

	/**
	 * @param mostrarMultasCitas the mostrarMultasCitas to set
	 */
	public void setMostrarMultasCitas(String mostrarMultasCitas) {
		this.mostrarMultasCitas = mostrarMultasCitas;
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
	 * @return the propiedad
	 */
	public String getColumna() {
		return columna;
	}

	/**
	 * @param propiedad the propiedad to set
	 */
	public void setColumna(String columna) {
		this.columna = columna;
	}

	/**
	 * @return the tipoFiltro
	 */
	public String getTipoFiltro() {
		return tipoFiltro;
	}

	/**
	 * @param tipoFiltro the tipoFiltro to set
	 */
	public void setTipoFiltro(String tipoFiltro) {
		this.tipoFiltro = tipoFiltro;
	}
	
	/**
	 * @return the tipoRegimenAcronimo
	 */
	public String getTipoRegimenAcronimo() {
		return tipoRegimenAcronimo;
	}

	/**
	 * @param tipoRegimenAcronimo the tipoRegimenAcronimo to set
	 */
	public void setTipoRegimenAcronimo(String tipoRegimenAcronimo) {
		this.tipoRegimenAcronimo = tipoRegimenAcronimo;
	}
	
	public void resetBusq()
	{
	   this.recibosCaja.clear(); 
	   this.conceptoFacVarias = "";
       this.nroFactura="";
       this.tipoDeudor = "";
       this.codigoDeudor = ""; 
       this.descripcionDeudor = "" ;
       this.codigoMultaCita = "";
       this.fechaGenMultaCita = "";
	}




	/**
	 * @return the mapaConceptosAuxiliarFlujoFact
	 */
	public HashMap getMapaConceptosAuxiliarFlujoFact() {
		return mapaConceptosAuxiliarFlujoFact;
	}

	/**
	 * @return the mapaConceptosAuxiliarFlujoFact
	 */
	public Object getMapaConceptosAuxiliarFlujoFact(Object key) {
		return mapaConceptosAuxiliarFlujoFact.get(key);
	}


	/**
	 * @param mapaConceptosAuxiliarFlujoFact the mapaConceptosAuxiliarFlujoFact to set
	 */
	public void setMapaConceptosAuxiliarFlujoFact(
			HashMap mapaConceptosAuxiliarFlujoFact) {
		this.mapaConceptosAuxiliarFlujoFact = mapaConceptosAuxiliarFlujoFact;
	}
	
	//*********************************************************************************
	// Anexo 762
	/**
	 * @return the datosFinanciacion
	 */
	public DtoDatosFinanciacion getDatosFinanciacion() {
		return datosFinanciacion;
	}

	/**
	 * @param datosFinanciacion the datosFinanciacion to set
	 */
	public void setDatosFinanciacion(DtoDatosFinanciacion datosFinanciacion) {
		this.datosFinanciacion = datosFinanciacion;
	}

	/**
	 * @return the arrayTipIdent
	 */
	public ArrayList<HashMap<String, Object>> getArrayTipIdent() {
		return arrayTipIdent;
	}

	/**
	 * @param arrayTipIdent the arrayTipIdent to set
	 */
	public void setArrayTipIdent(ArrayList<HashMap<String, Object>> arrayTipIdent) {
		this.arrayTipIdent = arrayTipIdent;
	}

	/**
	 * @return the seccionDeudor
	 */
	public String getSeccionDeudor() {
		return seccionDeudor;
	}

	/**
	 * @param seccionDeudor the seccionDeudor to set
	 */
	public void setSeccionDeudor(String seccionDeudor) {
		this.seccionDeudor = seccionDeudor;
	}

	/**
	 * @return the seccionCodeudor
	 */
	public String getSeccionCodeudor() {
		return seccionCodeudor;
	}

	/**
	 * @param seccionCodeudor the seccionCodeudor to set
	 */
	public void setSeccionCodeudor(String seccionCodeudor) {
		this.seccionCodeudor = seccionCodeudor;
	}

	/**
	 * @return the regCodeudor
	 */
	public String getRegCodeudor() {
		return regCodeudor;
	}

	/**
	 * @param regCodeudor the regCodeudor to set
	 */
	public void setRegCodeudor(String regCodeudor) {
		this.regCodeudor = regCodeudor;
	}

	/**
	 * @return the existDocGarAso
	 */
	public String getExistDocGarAso() {
		return existDocGarAso;
	}

	/**
	 * @param existDocGarAso the existDocGarAso to set
	 */
	public void setExistDocGarAso(String existDocGarAso) {
		this.existDocGarAso = existDocGarAso;
	}

	/**
	 * @return the paranGenVal
	 */
	public String getParanGenVal() {
		return paranGenVal;
	}

	/**
	 * @param paranGenVal the paranGenVal to set
	 */
	public void setParanGenVal(String paranGenVal) {
		this.paranGenVal = paranGenVal;
	}

	/**
	 * @return the messAdver
	 */
	public String getMessAdver() {
		return messAdver;
	}

	/**
	 * @param messAdver the messAdver to set
	 */
	public void setMessAdver(String messAdver) {
		this.messAdver = messAdver;
	}

	/**
	 * @return the deudor
	 */
	public DtoDeudor getDeudor() {
		return deudor;
	}

	/**
	 * @param deudor the deudor to set
	 */
	public void setDeudor(DtoDeudor deudor) {
		this.deudor = deudor;
	}

	/**
	 * @return the resultBusDeu
	 */
	public ArrayList<DtoDeudor> getResultBusDeu() {
		return resultBusDeu;
	}

	/**
	 * @param resultBusDeu the resultBusDeu to set
	 */
	public void setResultBusDeu(ArrayList<DtoDeudor> resultBusDeu) {
		this.resultBusDeu = resultBusDeu;
	}

	/**
	 * rompimiento paciente 
	 * @return
	 */
	public TreeSet<String> getRompimientoPac()
	{
		for(int i=0;i<this.getResultBusDeu().size();i++)
			this.rompimientoPac.add(this.getResultBusDeu().get(i).getNombreCompletoPac());
		return this.rompimientoPac;
		//+" "+this.getResultBusDeu().get(i).getTipoIdentificacionPac()+" "+this.getResultBusDeu().get(i).getNumeroIdentificacionPac()
	}

	/**
	 * @return the isRomp
	 */
	public String getIsRomp() {
		return isRomp;
	}

	/**
	 * @param isRomp the isRomp to set
	 */
	public void setIsRomp(String isRomp) {
		this.isRomp = isRomp;
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
	 * @return the patronOrdenarDeudores
	 */
	public String getPatronOrdenarDeudores() {
		return patronOrdenarDeudores;
	}

	/**
	 * @param patronOrdenarDeudores the patronOrdenarDeudores to set
	 */
	public void setPatronOrdenarDeudores(String patronOrdenarDeudores) {
		this.patronOrdenarDeudores = patronOrdenarDeudores;
	}

	/**
	 * @return the posDeudorSel
	 */
	public int getPosDeudorSel() {
		return posDeudorSel;
	}

	/**
	 * @param posDeudorSel the posDeudorSel to set
	 */
	public void setPosDeudorSel(int posDeudorSel) {
		this.posDeudorSel = posDeudorSel;
	}

	/**
	 * @return the valorFormaPago
	 */
	public String getValorFormaPago() {
		return valorFormaPago;
	}

	/**
	 * @param valorFormaPago the valorFormaPago to set
	 */
	public void setValorFormaPago(String valorFormaPago) {
		this.valorFormaPago = valorFormaPago;
	}

	/**
	 * @return the existResPacIngFac
	 */
	public String getExistResPacIngFac() {
		return existResPacIngFac;
	}

	/**
	 * @param existResPacIngFac the existResPacIngFac to set
	 */
	public void setExistResPacIngFac(String existResPacIngFac) {
		this.existResPacIngFac = existResPacIngFac;
	}

	/**
	 * @return the ingresoFac
	 */
	public int getIngresoFac() {
		return ingresoFac;
	}

	/**
	 * @param ingresoFac the ingresoFac to set
	 */
	public void setIngresoFac(int ingresoFac) {
		this.ingresoFac = ingresoFac;
	}

	/**
	 * @return the datFinanGuarExis
	 */
	public String getDatFinanGuarExis() {
		return datFinanGuarExis;
	}

	/**
	 * @param datFinanGuarExis the datFinanGuarExis to set
	 */
	public void setDatFinanGuarExis(String datFinanGuarExis) {
		this.datFinanGuarExis = datFinanGuarExis;
	}
	//*********************************************************************************

	/**
	 * @return the tipoAtencion
	 */
	public String getTipoAtencion() {
		return tipoAtencion;
	}

	/**
	 * @param tipoAtencion the tipoAtencion to set
	 */
	public void setTipoAtencion(String tipoAtencion) {
		this.tipoAtencion = tipoAtencion;
	}

	//Anexo 958
	public ArrayList<DtoConceptosIngTesoreria> getListadoConceptos() {
		return listadoConceptos;
	}

	public void setListadoConceptos(
			ArrayList<DtoConceptosIngTesoreria> listadoConceptos) {
		this.listadoConceptos = listadoConceptos;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public String getDocSoporte() {
		return docSoporte;
	}

	public void setDocSoporte(String docSoporte) {
		this.docSoporte = docSoporte;
	}

	public String getBeneficiario() {
		return beneficiario;
	}

	public void setBeneficiario(String beneficiario) {
		this.beneficiario = beneficiario;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getLlegoDeFV() {
		return llegoDeFV;
	}

	public void setLlegoDeFV(String llegoDeFV) {
		this.llegoDeFV = llegoDeFV;
	}

	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}

	public String getEsDescendente() {
		return esDescendente;
	}

	/**
	 * @return Retorna atributo formaPagoBonos
	 */
	public ArrayList<DtoDetallePagosBonos> getFormaPagoBonos()
	{
		return formaPagoBonos;
	}

	/**
	 * @param formaPagoBonos Asigna atributo formaPagoBonos
	 */
	public void setFormaPagoBonos(ArrayList<DtoDetallePagosBonos> formaPagoBonos)
	{
		this.formaPagoBonos = formaPagoBonos;
	}

	/**
	 * @return Retorna atributo entidadesFinancieras
	 */
	public ArrayList<DtoEntidadesFinancieras> getEntidadesFinancieras()
	{
		return entidadesFinancieras;
	}

	/**
	 * @param entidadesFinancieras Asigna atributo entidadesFinancieras
	 */
	public void setEntidadesFinancieras(
			ArrayList<DtoEntidadesFinancieras> entidadesFinancieras)
	{
		this.entidadesFinancieras = entidadesFinancieras;
	}
	
	/**
	 * @return Retorna atributo entidadesFinancieras
	 */
	public boolean getPostularAutomaticamenteTarjetaFinanciera()
	{
		return entidadesFinancieras.size()==1;
	}

	/**
	 * @return Retorna atributo facturaTarjeta
	 */
	@Deprecated
	public String getFacturaTarjeta()
	{
		return facturaTarjeta;
	}

	/**
	 * @param facturaTarjeta Asigna atributo facturaTarjeta
	 */
	@Deprecated
	public void setFacturaTarjeta(String facturaTarjeta)
	{
		this.facturaTarjeta = facturaTarjeta;
	}




	/**
	 * @return Retorna atributo comprador
	 */
	public String getComprador()
	{
		return comprador;
	}




	/**
	 * @param comprador Asigna atributo comprador
	 */
	public void setComprador(String comprador)
	{
		this.comprador = comprador;
	}

	public void setAplicaVentaTarjeta(String aplicaVentaTarjeta) {
		this.aplicaVentaTarjeta = aplicaVentaTarjeta;
	}

	public String getAplicaVentaTarjeta() {
		return aplicaVentaTarjeta;
	}

	/**
	 * @param flujoAgendaOdontologica the flujoAgendaOdontologica to set
	 */
	public void setFlujoAgendaOdontologica(boolean flujoAgendaOdontologica) {
		this.flujoAgendaOdontologica = flujoAgendaOdontologica;
	}

	/**
	 * @return the flujoAgendaOdontologica
	 */
	public boolean isFlujoAgendaOdontologica() {
		return flujoAgendaOdontologica;
	}

	/**
	 * @param actividadAgenda the actividadAgenda to set
	 */
	public void setActividadAgenda(String actividadAgenda) {
		this.actividadAgenda = actividadAgenda;
	}

	/**
	 * @return the actividadAgenda
	 */
	public String getActividadAgenda() {
		return actividadAgenda;
	}
	
	/**
	 * @return the mapaConceptosFlujoAbonosAgenda
	 */
	public HashMap getMapaConceptosFlujoAbonosAgenda() {
		return mapaConceptosFlujoAbonosAgenda;
	}

	/**
	 * @return the mapaConceptosFlujoAbonosAgenda
	 */
	public Object getMapaConceptosFlujoAbonosAgenda(Object key) {
		return mapaConceptosFlujoAbonosAgenda.get(key);
	}


	/**
	 * @param mapaConceptosFlujoAbonosAgenda the mapaConceptosFlujoAbonosAgenda to set
	 */
	public void setMapaConceptosFlujoAbonosAgenda(HashMap mapaConceptosFlujoAbonosAgenda) {
		
		this.mapaConceptosFlujoAbonosAgenda = mapaConceptosFlujoAbonosAgenda;
	}

	/**
	 * @param mostrarConsecutivoFv the mostrarConsecutivoFv to set
	 */
	public void setMostrarConsecutivoFv(boolean mostrarConsecutivoFv) {
		this.mostrarConsecutivoFv = mostrarConsecutivoFv;
	}

	/**
	 * @return the mostrarConsecutivoFv
	 */
	public boolean isMostrarConsecutivoFv() {
		return mostrarConsecutivoFv;
	}

	public String getConsecutivoReciboCaja() {
		return consecutivoReciboCaja;
	}

	public void setConsecutivoReciboCaja(String consecutivoReciboCaja) {
		this.consecutivoReciboCaja = consecutivoReciboCaja;
	}
	
	public String getCerrarVentanita() {
		return cerrarVentanita;
	}
	
	public void setCerrarVentanita(String cerrarVentanita) {
		this.cerrarVentanita = cerrarVentanita;
	}

	/**
	 * @return the cerrarPopupTarjetas
	 */
	public boolean isCerrarPopupTarjetas() {
		return cerrarPopupTarjetas;
	}

	/**
	 * @param cerrarPopupTarjetas the cerrarPopupTarjetas to set
	 */
	public void setCerrarPopupTarjetas(boolean cerrarPopupTarjetas) {
		this.cerrarPopupTarjetas = cerrarPopupTarjetas;
	}
}