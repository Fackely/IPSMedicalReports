/*
 * @(#)EstadoCuentaForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.actionform.resumenAtenciones;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.mundo.resumenAtenciones.EstadoCuenta;

import util.ConstantesBD;
import util.InfoDatosString;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.Administracion.UtilConversionMonedas;

/**
 * Forma para manejo presentación de la funcionalidad 
 * Estado de Cuenta. A diferencia de la mayoría de 
 * funcionalidades, el estado de cuenta no maneja
 * acción a finalizar porque es simplemente un
 * resumen
 * 
 *	@version 1.0, 5/08/2004
 */
public class EstadoCuentaForm extends ValidatorForm
{

	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private String estado = "";
	
	/**
	 * Se guarda el numero de registros del listado de solicitudes
	 */
	private int maxPageItems;
	
	
	//*************************ATRIBUTOS PARA EL LISTADO DE CUENTAS*********************************
	/**
	 * Listado de las cuentas del paciente
	 */
	private ArrayList<HashMap<String, Object>> listadoCuentas = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * Id ddel ingreso seleccionado
	 */
	private String idIngreso;
	
	/**
	 * Código de la cuenta para la que se está 
	 * mostrando en este momento su estado 
	 */
	private String idCuenta="";
	
	/**
	 * Variable con los valores S o N para saber si la cuenta elegida tiene asocio
	 */
	private String existeAsocio;
	
	/**
	 * Vía de inrgeso de la cuenta elegida
	 */
	private String viaIngreso = "";
	
	/**
	 * Estado de la cuenta
	 */
	private String estadoCuenta = "";
	
	/**
	 * Centro Atención de la cuenta seleccionada
	 */
	private String centroAtencion = "";
	
	/**
	 * Consecutivo del ingreso
	 */
	private String consecutivoIngreso = "";
	
	
	/**
	 * Fecha del ingreso
	 */
	private String fechaIngreso = "";
	
	/**
	 * Estado del ingreso
	 */
	private String estadoIngreso = "";
	
	//***********************ATRIBUTOS PARA EL LISTADO DE CONVENIOS********************************
	/**
	 * Listado de las convenios del paciente
	 */
	private ArrayList<HashMap<String, Object>> listadoConvenios = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * Id de la subcuenta (Convenio) elegida
	 */
	private String idSubCuenta;
	
	/**
	 * Convenio elegido
	 */
	private InfoDatosString convenio = new InfoDatosString();
	
	/**
	 * Codigo del tipo regimen
	 */
	private String codigoTipoRegimen;
	
	/**
	 * Nombre del estrato social elegido
	 */
	private String nombreEstratoSocial;
	
	/**
	 * Nombre del monto de cobro elegido
	 */
	private String nombreMontoCobro;
	
	/**
	 * Nombre del tipo de monto del conveino elegido
	 */
	private String nombreTipoMonto;
	
	/**
	 * Naturaleza paciente del convenio elegido
	 */
	private InfoDatosString naturaleza = new InfoDatosString();
	
	/**
	 * Indicador S/N para saber si el convenio tiene informacion adicional poliza
	 */
	private String esConvenioPoliza;
	
	/**
	 * Informacion de la empresa subcontratada
	 * */
	private String descEmpresaSubContratada;
	
	//**********************************************************************************************
	//*******************ATRIBUTOS RELACIONADOS CON LAS SOLICITUDES*********************************
	private HashMap<String, Object> solicitudes = new HashMap<String, Object>();
	private HashMap<String, Object> detalleSolicitud = new HashMap<String, Object>();
	private int numSolicitudes;
	private int posicion;
	
	private String valorTotalCargos;
	private String valorTotalCargosConvertido;
	private String valorTotalPaciente;
	private String valorTotalPacienteConvertido;
	private String valorTotalConvenio;
	private String valorTotalConvenioConvertido;
	private double valorTotalConvenioDouble;
	private String valorTotalAbonos;
	private String valorTotalAbonosConvertido;
	private String valorNetoPaciente;
	private String valorNetoPacienteConvertido;
	
	private String valorDevolucionPaciente;
	private String valorDevolucionPacienteConvertido;
	
	private String valorSaldoMontoAutorizado; //solo aplic apara venezuela
	private String valorSaldoMontoAutorizadoConvertido;
	
	//Datos de la solicitud seleccionada
	private String numeroSolicitud;
	private String consecutivoOrden;
	private int codigoTipoSolicitud;
	private String nombreTipoSolicitud;
	private boolean servicio; //para saber si la solicitud es de tipo sservicio o articulo
	
	//para la ordenacion
	private String indice;
	private String ultimoIndice;
	//para la paginacnion
	/**
     * Para controlar la página actual del pager.
     */
    private int offset = 0;
    
    /**
     * Para controlar el link siguiente del pager 
     */
    private String linkSiguiente = "";
    
    /**
     * Atributo para el manejo de la paginacion con memoria 
     */
    private int currentPageNumber = 1;
    
    //***********************************************************************************************
	//***********************CAMPOS RELACIONADOS CON LA BUSQUEDA AVANZADA***************************
	private HashMap<String, Object> busquedaAvanzada = new HashMap<String, Object>();
	//Estructuras para llenar los combo box
	private HashMap tiposSolicitud = new HashMap();
	private HashMap estadosMedicos = new HashMap();
	private HashMap estadosFacturacion = new HashMap();
	private HashMap centrosCosto = new HashMap();
	//***********************************************************************************************
	
	/**
	 * Colección para manejar el detalle de la
	 * cuenta durante el flujo de las solicitudes
	 */
	private Collection detalleCuenta=null;
	
	/**
	 * Colección para manejar las solicitudes
	 */
	private Collection solicitudesCuenta=null;
	
	
	/**
	 * Colección para manejar las solictudes 
	 * para la impresion resumida
	 */
	private Collection impresionSolicitudesCuenta=null;
	
	/**
	 * Colección para manejar las solictudes 
	 * para la impresion resumida detallada
	 */
	private Collection impresionDetSolicitudesCuenta = null;
	
	/**
	 * Colección para manejar la impresion del cabezote
	 * del estado de cuenta
	 */
	private Collection cabezoteImpresion=null;

	/************** BÚSQUEDA AVANZADA  ************/
	
	/**
	 * Boolean que indica el código con el que se va a
	 * buscar el código del tipo de servicio de una
	 * solicitud
	 */
	private int codigoTipoSolicitudBusqueda=ConstantesBD.codigoNuncaValido;
	
	/**
	 * Boolean que indica el código con el que se va a
	 * buscar el código del tipo de servicio de una
	 * solicitud
	 */
	private int numeroSolicitudBusqueda=ConstantesBD.codigoNuncaValido;
	
	/**
	 * Boolean que indica el código con el que se va a
	 * buscar el código del tipo de servicio de una
	 * solicitud
	 */
	private String fechaGrabacionBusqueda="";
	
	/**
	 * Boolean que indica el código con el que se va a
	 * buscar el código del tipo de servicio de una
	 * solicitud
	 */
	private int codigoEstadoMedicoBusqueda=ConstantesBD.codigoNuncaValido;
	
	/**
	 * Boolean que indica el código con el que se va a
	 * buscar el código del tipo de servicio de una
	 * solicitud
	 */
	private int codigoEstadoFacturacionBusqueda=ConstantesBD.codigoNuncaValido;
	
	/**
	 * Boolean que indica el código con el que se va a
	 * buscar el código del centro de costo solicitante 
	 * de una solicitud
	 */
	private int codigoCentroCostoSolicitanteBusqueda=ConstantesBD.codigoNuncaValido;
	
	/**
	 * Boolean que indica el código con el que se va a
	 * buscar el código del centro de costo solicitado 
	 * de una solicitud
	 */
	private int codigoCentroCostoSolicitadoBusqueda=ConstantesBD.codigoNuncaValido;
	
	
	
	/**
	 * Double donde se guarda el valor de los abonos, se
	 * calcula segun el estado de la cuenta
	 */
	private double valorAbonos=0.00;
	

	
	/**
	 * Entero para almacenar el consecutivo de las solicitudes
	 * traido desde la base de datos en la tabla solicitudes
	 */
	private int consecutivoOrdenesMedicas=0;
	
	/**
	 * Double para almacenar el valor del recargo traido
	 * desde la base de datos
	 */
	private double recargo=0.00;
	
	private String tipoServicio="";
	
	
	/**
	 * Valrible que almacena la ultima propiedad por la que se ordeno.
	 */
	private String ultimaPropiedad;
	
	/**
	 * Columna por la que se desea ordenar
	 */
	private String columna;
	
	/****************Datos para la impresion de los totales de una cuenta********************/
	private String totalCargos="";
	private String totalConvenio="";
	private String totalPaciente="";
	private String temp="";

	
     /**
      * Fecha de la solicitud
      */
     private String fechaSolicitud;
	
     
    //***********ATRIBUTOS PARA LA SOLICITUDES DE CIRUGIA********************************
    
   
    
    /**
     * Datos del encabezado de la solicitud Qx.
     */
    private HashMap encabezadoSolicitud = new HashMap();
    
    /**
     * Datos de las Cirugias de la solicitud Qx
     */
    private HashMap cirugias = new HashMap();
    
    /**
     * Número de cirugias de la solicitud Qx.
     */
    private int numCirugias;
    
    /**
     * Datos de los asocios de cada cirugias en la orden Qx.
     */
    private HashMap asocios = new HashMap();
    
    /**
     * Datos de la hoja Qx.
     */
    private HashMap hojaQx = new HashMap();
    
    /**
     * Datos de la hoja de anestesia
     */
    private HashMap hojaAnestesia = new HashMap();
    
    /**
     * Arreglo que almacena los materialesEspeciales
     */
    private ArrayList<HashMap<String, Object>> materialesEspeciales = new ArrayList<HashMap<String,Object>>();
    //************************************************************************************
	
    
    //************* CONVERSION MONEDAS***************************////
    /**
     * 
     */
    private int index;
    
    /**
     * 
     */
    private boolean manejaConversionMoneda;
    
    /**
     * 
     */
    private HashMap tiposMonedaTagMap;
    //***************************************************************
    
    /**
     * 
     */
    private HashMap esquemasInventario;
    
    /**
     * 
     */
    private HashMap esquemasProcedimientos;
    
	/**
	 * @return Returns the fechaSolicitud.
	 */
	
    private String MostrarArticPrinciDespachoCero;
    
    public String getFechaSolicitud()
	{
		return fechaSolicitud;
	}
	/**
	 * @param fechaSolicitud The fechaSolicitud to set.
	 */
	public void setFechaSolicitud(String fechaSolicitud)
	{
		this.fechaSolicitud=fechaSolicitud;
	}
	public double getValorAbonos() 
	{
		return valorAbonos;
	}
	
	public void setValorAbonos(double valorAbonos) 
	{
		this.valorAbonos = valorAbonos;
	}
	public String getColumna() 
	{
		return columna;
	}
	
	public void setColumna(String columna) 
	{
		this.columna = columna;
	}
	public String getUltimaPropiedad()
	{
		return ultimaPropiedad;
	}
	
	
	public void setUltimaPropiedad(String ultimaPropiedad) 
	{
		this.ultimaPropiedad = ultimaPropiedad;
	}
	/**
	 * @return (Metodo GET) Returns the temp.
	 */
	public String getTemp()
	{
		return temp;
	}
	/**
	 * @param temp The temp to set (Metodo SET).
	 */
	public void setTemp(String temp)
	{
		this.temp= temp;
	}
	/**
	 * @return (Metodo GET) Returns the totalCargos.
	 */
	public String getTotalCargos()
	{
		return totalCargos;
	}
	/**
	 * @param totalCargos The totalCargos to set (Metodo SET).
	 */
	public void setTotalCargos(String totalCargos)
	{
		this.totalCargos= totalCargos;
	}
	/**
	 * @return (Metodo GET) Returns the totalConvenio.
	 */
	public String getTotalConvenio()
	{
		return totalConvenio;
	}
	/**
	 * @param totalConvenio The totalConvenio to set (Metodo SET).
	 */
	public void setTotalConvenio(String totalConvenio)
	{
		this.totalConvenio= totalConvenio;
	}
	/**
	 * @return (Metodo GET) Returns the totalPaciente.
	 */
	public String getTotalPaciente()
	{
		return totalPaciente;
	}
	/**
	 * @param totalPaciente The totalPaciente to set (Metodo SET).
	 */
	public void setTotalPaciente(String totalPaciente)
	{
		this.totalPaciente= totalPaciente;
	}
	
	
	
	/****************Fin de lod datos totales para la impresion**********************************/
	
	/**
	 * Reset NO estandar, para limpiar al terminar todo el proceso, NO al
	 * cambiar de página. Limpia T ODOS los datos menos el estado 
	 */
	
	public void reset (int codigoInstitucion)
	{
		this.estado="";
		this.maxPageItems = 0;
		
		//*********ATRIBUTOS PARA EL LISTADO DE CUENTAS**********************+
		this.listadoCuentas = new ArrayList<HashMap<String,Object>>();
		this.idIngreso = "";
		this.idCuenta = "";
		this.existeAsocio = "";
		this.viaIngreso = "";
		this.estadoCuenta = "";
		this.centroAtencion = "";
		this.consecutivoIngreso = "";
		this.fechaIngreso = "";
		this.estadoIngreso = "";
		
		//*********ATRIBUTOS PARA EL LISTADO DE CONVENIOS*********************
		this.listadoConvenios = new ArrayList<HashMap<String,Object>>();
		this.idSubCuenta = "";
		this.convenio = new InfoDatosString("","");
		this.codigoTipoRegimen = "";
		this.nombreEstratoSocial = "";
		this.nombreMontoCobro = "";
		this.nombreTipoMonto = "";
		this.naturaleza = new InfoDatosString("","");
		this.esConvenioPoliza = "";
		
		//*******ATRIBUTOS RELACIONADOS CON LAS SOLICITUDES*****************
		this.solicitudes = new HashMap<String, Object>();
		this.detalleSolicitud = new HashMap<String, Object>();
		this.numSolicitudes = 0;
		this.posicion = 0;
		
		this.valorTotalCargos = "";
		this.valorTotalCargosConvertido="";
		this.valorTotalConvenio = "";
		this.valorTotalConvenioConvertido="";
		this.valorTotalConvenioDouble = 0;
		this.valorTotalPaciente = "";
		this.valorTotalPacienteConvertido="";
		
		this.valorTotalAbonos = "";
		this.valorTotalAbonosConvertido = "";
		this.valorNetoPaciente = "";
		this.valorNetoPacienteConvertido="";
		this.valorDevolucionPaciente = "";
		this.valorDevolucionPacienteConvertido="";
		this.valorSaldoMontoAutorizado = "";
		this.valorSaldoMontoAutorizadoConvertido="";
		this.MostrarArticPrinciDespachoCero="";
		
		//atributos de la solicitud
		this.numeroSolicitud = "";
		this.consecutivoOrden = "";
		this.codigoTipoSolicitud = 0;
		this.nombreTipoSolicitud = "";
		this.servicio = false;
		
		//para ordenacion
		this.indice = "";
		this.ultimoIndice = "";
		//para paginacion
		this.linkSiguiente = "";
		this.offset = 0;
		
		//****************************************************************
		//***********CAMPOS RELACIONADOS CON LA BUSQUEDA AVANZADA***********
		this.busquedaAvanzada = new HashMap<String, Object>();
		this.tiposSolicitud = new HashMap();
		this.estadosMedicos = new HashMap();
		this.centrosCosto = new HashMap();
		this.estadosFacturacion = new HashMap();
		
		
		
		
		
		
		
		
		
		this.detalleCuenta=null;
		this.solicitudesCuenta=null;
		this.impresionSolicitudesCuenta=null;
		this.impresionDetSolicitudesCuenta = null;
		this.cabezoteImpresion=null;
		this.codigoTipoSolicitudBusqueda=ConstantesBD.codigoNuncaValido;
		this.numeroSolicitudBusqueda=ConstantesBD.codigoNuncaValido;
		this.fechaGrabacionBusqueda="";
		this.codigoEstadoMedicoBusqueda=ConstantesBD.codigoNuncaValido;
		this.codigoEstadoFacturacionBusqueda=ConstantesBD.codigoNuncaValido;
		this.codigoCentroCostoSolicitanteBusqueda=ConstantesBD.codigoNuncaValido;
		this.codigoCentroCostoSolicitadoBusqueda=ConstantesBD.codigoNuncaValido;
	
		this.valorAbonos = 0.00;
		this.consecutivoOrdenesMedicas=0;
		this.recargo=0.00;
		this.tipoServicio="";
		this.totalCargos = "";
		this.totalConvenio = "";
		this.totalPaciente = "";
		
		//datos de las solicitudes cirugia
		this.encabezadoSolicitud = new HashMap();
		this.cirugias = new HashMap();
		this.numCirugias = 0;
		this.asocios = new HashMap();
		this.hojaQx = new HashMap();
		this.hojaAnestesia = new HashMap();
		this.materialesEspeciales = new ArrayList<HashMap<String,Object>>();
		
		//CONVERSION MONEDAS
		this.index=ConstantesBD.codigoNuncaValido;
        this.manejaConversionMoneda=UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaConversionMonedaExtranjera(codigoInstitucion));
        this.inicializarTagMap(codigoInstitucion);
        
        this.esquemasInventario=new HashMap<String, Object>();
        this.esquemasInventario.put("numRegistros", "0");
        this.esquemasProcedimientos=new HashMap<String, Object>();
        this.esquemasProcedimientos.put("numRegistros", "0");
		
	}
	
	/**
     * 
     * @param codigoInstitucion
     */
    public void inicializarTagMap (int codigoInstitucion)
    {
    	tiposMonedaTagMap= UtilConversionMonedas.obtenerTiposMonedaTagMap(codigoInstitucion, /*mostrarMonedaManejaInstitucion*/false);
    }
	
	
	
	
	/**
	 * @return (Metodo GET) Returns the tipoServicio.
	 */
	public String getTipoServicio()
	{
		return tipoServicio;
	}
	/**
	 * @param tipoServicio The tipoServicio to set (Metodo SET).
	 */
	public void setTipoServicio(String tipoServicio)
	{
		this.tipoServicio= tipoServicio;
	}
	/**
	 * @return (Metodo GET) Returns the recargo.
	 */
	public double getRecargo()
	{
		return recargo;
	}
	/**
	 * @param recargo The recargo to set (Metodo SET).
	 */
	public void setRecargo(double recargo)
	{
		this.recargo= recargo;
	}
	/**
	 * @return Retorna el consecutivoOrdenesMedicas.
	 */
	public int getConsecutivoOrdenesMedicas()
	{
		return consecutivoOrdenesMedicas;
	}
	/**
	 * @param Asigna consecutivoOrdenesMedicas para consecutivoOrdenesMedicas. 
	 */
	public void setConsecutivoOrdenesMedicas(int consecutivoOrdenesMedicas)
	{
		this.consecutivoOrdenesMedicas = consecutivoOrdenesMedicas;
	}
    /**
     * @return Retorna el estado.
     */
    public String getEstado()
    {
        return estado;
    }
    
    /**
     * @param estado El estado a establecer.
     */
    public void setEstado(String estado)
    {
        this.estado = estado;
    }
    
    /**
     * @return Retorna el idCuenta.
     */
    public String getIdCuenta()
    {
        return idCuenta;
    }
    
    /**
     * @param idCuenta El idCuenta a establecer.
     */
    public void setIdCuenta(String idCuenta)
    {
        this.idCuenta = idCuenta;
    }
    /**
     * @return Returns the detalleCuenta.
     */
    public Collection getDetalleCuenta()
    {
        return detalleCuenta;
    }
    /**
     * @return Returns the solicitudesCuenta.
     */
    public Collection getSolicitudesCuenta()
    {
        return solicitudesCuenta;
    }
    
	public void setDetalleCuenta(Collection detalleCuenta) 
	{
		this.detalleCuenta = detalleCuenta;
	}
	
	public void setSolicitudesCuenta(Collection solicitudesCuenta) 
	{
		this.solicitudesCuenta = solicitudesCuenta;
	}
    /**
     * @return Returns the impresionSolicitudesCuenta
     */
    public Collection getImpresionSolicitudesCuenta()
    {
    	return impresionSolicitudesCuenta;
    }
    /**
     * @return Returns the impresionDetSolicitudesCuenta
     */
    public Collection getImpresionDetSolicitudesCuenta()
    {
    	return impresionDetSolicitudesCuenta;
    }
    
    
	/**
	 * @return (Metodo GET) Returns the cabezoteImpresion.
	 */
	public Collection getCabezoteImpresion()
	{
		return cabezoteImpresion;
	}

	
	/**
     * Método que me devuelve el número de solicitudes
     * que hay en la colección solicitudesCuenta
     * @return
     */
    public int getNumeroSolicitudes ()
    {
        if (this.solicitudesCuenta!=null)
        {
            return this.solicitudesCuenta.size();
        }
        else
        {
            return 0;
        }
    }
    
    /**
     * Método que me devuelve el número de solicitudes
     * que hay en la colección solicitudesCuenta
     * @return
     */
    public int getNumeroSolicitudesImpresion ()
    {
        if (this.impresionSolicitudesCuenta!=null)
        {
            return this.impresionSolicitudesCuenta.size();
        }
        else
        {
            return 0;
        }
    }
   
    /**
     * @return Returns the codigoCentroCostoSolicitadoBusqueda.
     */
    public int getCodigoCentroCostoSolicitadoBusqueda()
    {
        return codigoCentroCostoSolicitadoBusqueda;
    }
    /**
     * @param codigoCentroCostoSolicitadoBusqueda The codigoCentroCostoSolicitadoBusqueda to set.
     */
    public void setCodigoCentroCostoSolicitadoBusqueda(
            int codigoCentroCostoSolicitadoBusqueda)
    {
        this.codigoCentroCostoSolicitadoBusqueda = codigoCentroCostoSolicitadoBusqueda;
    }
    /**
     * @return Returns the codigoCentroCostoSolicitanteBusqueda.
     */
    public int getCodigoCentroCostoSolicitanteBusqueda()
    {
        return codigoCentroCostoSolicitanteBusqueda;
    }
    /**
     * @param codigoCentroCostoSolicitanteBusqueda The codigoCentroCostoSolicitanteBusqueda to set.
     */
    public void setCodigoCentroCostoSolicitanteBusqueda(
            int codigoCentroCostoSolicitanteBusqueda)
    {
        this.codigoCentroCostoSolicitanteBusqueda = codigoCentroCostoSolicitanteBusqueda;
    }
    /**
     * @return Returns the codigoEstadoFacturacionBusqueda.
     */
    public int getCodigoEstadoFacturacionBusqueda()
    {
        return codigoEstadoFacturacionBusqueda;
    }
    /**
     * @param codigoEstadoFacturacionBusqueda The codigoEstadoFacturacionBusqueda to set.
     */
    public void setCodigoEstadoFacturacionBusqueda(
            int codigoEstadoFacturacionBusqueda)
    {
        this.codigoEstadoFacturacionBusqueda = codigoEstadoFacturacionBusqueda;
    }
    /**
     * @return Returns the codigoEstadoMedicoBusqueda.
     */
    public int getCodigoEstadoMedicoBusqueda()
    {
        return codigoEstadoMedicoBusqueda;
    }
    /**
     * @param codigoEstadoMedicoBusqueda The codigoEstadoMedicoBusqueda to set.
     */
    public void setCodigoEstadoMedicoBusqueda(int codigoEstadoMedicoBusqueda)
    {
        this.codigoEstadoMedicoBusqueda = codigoEstadoMedicoBusqueda;
    }
    /**
     * @return Returns the codigoTipoSolicitudBusqueda.
     */
    public int getCodigoTipoSolicitudBusqueda()
    {
        return codigoTipoSolicitudBusqueda;
    }
    /**
     * @param codigoTipoSolicitudBusqueda The codigoTipoSolicitudBusqueda to set.
     */
    public void setCodigoTipoSolicitudBusqueda(int codigoTipoSolicitudBusqueda)
    {
        this.codigoTipoSolicitudBusqueda = codigoTipoSolicitudBusqueda;
    }
    /**
     * @return Returns the fechaGrabacionBusqueda.
     */
    public String getFechaGrabacionBusqueda()
    {
        return fechaGrabacionBusqueda;
    }
    /**
     * @param fechaGrabacionBusqueda The fechaGrabacionBusqueda to set.
     */
    public void setFechaGrabacionBusqueda(String fechaGrabacionBusqueda)
    {
        this.fechaGrabacionBusqueda = fechaGrabacionBusqueda;
    }
    /**
     * @return Returns the numeroSolicitudBusqueda.
     */
    public int getNumeroSolicitudBusqueda()
    {
        return numeroSolicitudBusqueda;
    }
    /**
     * @param numeroSolicitudBusqueda The numeroSolicitudBusqueda to set.
     */
    public void setNumeroSolicitudBusqueda(int numeroSolicitudBusqueda)
    {
        this.numeroSolicitudBusqueda = numeroSolicitudBusqueda;
    }

    /**
     * Método por el cual sabemos si vamos a buscar
     * por el campo tipo de solicitud
     * @return
     */
    public boolean getBuscarTipoSolicitud ()
    {	
        return this.codigoTipoSolicitudBusqueda!=ConstantesBD.codigoNuncaValido;
    }
    
    /**
     * Método por el cual sabemos si vamos a buscar
     * por el campo número de solicitud
     * @return
     */
    public boolean getBuscarNumeroSolicitud()
    {
        return this.numeroSolicitudBusqueda>0;
    }
    /**
     * Metodo por el cual sabemos si vamos a buscar
     * por el campo consecutivo ordenes medicas
     * @return
     */
    
    public boolean getBuscarConsecutivoOrdenesMedicas()
    {
    	return this.consecutivoOrdenesMedicas>0;
    }
    
    /**
     * Método por el cual sabemos si vamos a buscar
     * por el campo fecha de grabación
     * @return
     */
    public boolean getBuscarFechaGrabacion()
    {
        if (this.fechaGrabacionBusqueda!=null&&!this.fechaGrabacionBusqueda.equals(""))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
	/**
	 * Método por el cual sabemos si vamos a buscar
	 * por el estado médico de la solicitud
	 * @return
	 */
    public boolean getBuscarEstadoMedicoSolicitud ()
    {
        return this.codigoEstadoMedicoBusqueda!=ConstantesBD.codigoNuncaValido;
    }
    
	/**
	 * Método por el cual sabemos si vamos a buscar
	 * por el estado de facturación de la solicitud
	 * @return
	 */
    public boolean getBuscarEstadoFacturacionSolicitud()
    {
        return this.codigoEstadoFacturacionBusqueda!=ConstantesBD.codigoNuncaValido;
    }
    
	/**
	 * Método por el cual sabemos si vamos a buscar
	 * por el centro de costo que solicita
	 * @return
	 */
    public boolean getBuscarCentroCostoSolicita()
    {
        return this.codigoCentroCostoSolicitanteBusqueda!=ConstantesBD.codigoNuncaValido;
    }
    
	/**
	 * Método por el cual sabemos si vamos a buscar
	 * por el centro de costo solicitado
	 * @return
	 */
    public boolean getBuscarCentroCostoSolicitado()
    {
        return this.codigoCentroCostoSolicitadoBusqueda!=ConstantesBD.codigoNuncaValido;
    }
   

  
    
    
    
	/**
	 * @return Returns the linkSiguiente.
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}
	/**
	 * @param linkSiguiente The linkSiguiente to set.
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		if(estado.equals("busquedaAvanzada"))
		{
			if(!this.getFechaGrabacionBusqueda().equals(""))
			{
				if(!UtilidadFecha.validarFecha(this.getFechaGrabacionBusqueda()))
				{
					errores.add("fecha inicial", new ActionMessage("errors.formatoFechaInvalido",this.getFechaGrabacionBusqueda()));
					
				}
			}
		}
		return errores;
	}
    
    
    
    
	
	/**
	 * @return Returns the encabezadoSolicitud.
	 */
	public HashMap getEncabezadoSolicitud() {
		return encabezadoSolicitud;
	}
	/**
	 * @param encabezadoSolicitud The encabezadoSolicitud to set.
	 */
	public void setEncabezadoSolicitud(HashMap encabezadoSolicitud) {
		this.encabezadoSolicitud = encabezadoSolicitud;
	}
	/**
	 * @return Retorna un elemento del mapa encabezadoSolicitud.
	 */
	public Object getEncabezadoSolicitud(String key) {
		return encabezadoSolicitud.get(key);
	}
	/**
	 * @param Asigna un elemento al mapa encabezadoSolicitud.
	 */
	public void setEncabezadoSolicitud(String key,Object obj) {
		this.encabezadoSolicitud.put(key,obj);
	}
	/**
	 * @return Returns the asocios.
	 */
	public HashMap getAsocios() {
		return asocios;
	}
	/**
	 * @param asocios The asocios to set.
	 */
	public void setAsocios(HashMap asocios) {
		this.asocios = asocios;
	}
	/**
	 * @return Retorna un elemento del mapa asocios.
	 */
	public Object getAsocios(String key) {
		return asocios.get(key);
	}
	/**
	 * @param Asigna un elemento al mapa asocios.
	 */
	public void setAsocios(String key,Object obj) {
		this.asocios.put(key,obj);
	}
	/**
	 * @return Returns the cirugias.
	 */
	public HashMap getCirugias() {
		return cirugias;
	}
	/**
	 * @param cirugias The cirugias to set.
	 */
	public void setCirugias(HashMap cirugias) {
		this.cirugias = cirugias;
	}
	/**
	 * @return Retorna un elemento del mapa cirugias.
	 */
	public Object getCirugias(String key) {
		return cirugias.get(key);
	}
	/**
	 * @param Asigna un elemento al mapa cirugias.
	 */
	public void setCirugias(String key,Object obj) {
		this.cirugias.put(key,obj);
	}
	/**
	 * @return Returns the numCirugias.
	 */
	public int getNumCirugias() {
		return numCirugias;
	}
	/**
	 * @param numCirugias The numCirugias to set.
	 */
	public void setNumCirugias(int numCirugias) {
		this.numCirugias = numCirugias;
	}
	/**
	 * @return Returns the hojaQx.
	 */
	public HashMap getHojaQx() {
		return hojaQx;
	}
	/**
	 * @param hojaQx The hojaQx to set.
	 */
	public void setHojaQx(HashMap hojaQx) {
		this.hojaQx = hojaQx;
	}
	/**
	 * @return Retorna un elemento del mapa hojaQx.
	 */
	public Object getHojaQx(String key) {
		return hojaQx.get(key);
	}
	/**
	 * @param Asigna un elemento del mapa hojaQx.
	 */
	public void setHojaQx(String key,Object obj) {
		this.hojaQx.put(key,obj);
	}
	
	/**
	 * @return Returns the hojaAnestesia.
	 */
	public HashMap getHojaAnestesia() {
		return hojaAnestesia;
	}
	/**
	 * @param hojaAnestesia The hojaAnestesia to set.
	 */
	public void setHojaAnestesia(HashMap hojaAnestesia) {
		this.hojaAnestesia = hojaAnestesia;
	}
	/**
	 * @return Retorna un elemento del mapa hojaAnestesia.
	 */
	public Object getHojaAnestesia(String key) {
		return hojaAnestesia.get(key);
	}
	/**
	 * @param Asigna un elemento al mapa hojaAnestesia.
	 */
	public void setHojaAnestesia(String key,Object obj) {
		this.hojaAnestesia.put(key,obj);
	}
	/**
	 * @param impresionDetSolicitudesCuenta The impresionDetSolicitudesCuenta to set.
	 */
	public void setImpresionDetSolicitudesCuenta(
			Collection impresionDetSolicitudesCuenta) {
		this.impresionDetSolicitudesCuenta = impresionDetSolicitudesCuenta;
	}
	/**
	 * @param impresionSolicitudesCuenta The impresionSolicitudesCuenta to set.
	 */
	public void setImpresionSolicitudesCuenta(Collection impresionSolicitudesCuenta) {
		this.impresionSolicitudesCuenta = impresionSolicitudesCuenta;
	}
	/**
	 * @return the listadoCuentas
	 */
	public ArrayList<HashMap<String, Object>> getListadoCuentas() {
		return listadoCuentas;
	}
	/**
	 * @param listadoCuentas the listadoCuentas to set
	 */
	public void setListadoCuentas(ArrayList<HashMap<String, Object>> listadoCuentas) {
		this.listadoCuentas = listadoCuentas;
	}
	/**
	 * @return the listadoCuentas
	 */
	public int getSizeListadoCuentas() {
		return listadoCuentas.size();
	}
	/**
	 * @return the idIngreso
	 */
	public String getIdIngreso() {
		return idIngreso;
	}
	/**
	 * @param idIngreso the idIngreso to set
	 */
	public void setIdIngreso(String idIngreso) {
		this.idIngreso = idIngreso;
	}
	/**
	 * @return the convenio
	 */
	public String getCodigoConvenio() {
		return convenio.getCodigo();
	}
	/**
	 * @param convenio the convenio to set
	 */
	public void setCodigoConvenio(String convenio) {
		this.convenio.setCodigo(convenio);
	}
	
	/**
	 * @return the convenio
	 */
	public String getNombreConvenio() {
		return convenio.getNombre();
	}
	/**
	 * @param convenio the convenio to set
	 */
	public void setNombreConvenio(String convenio) {
		this.convenio.setNombre(convenio);
	}
	/**
	 * @return the idSubCuenta
	 */
	public String getIdSubCuenta() {
		return idSubCuenta;
	}
	/**
	 * @param idSubCuenta the idSubCuenta to set
	 */
	public void setIdSubCuenta(String idSubCuenta) {
		this.idSubCuenta = idSubCuenta;
	}
	/**
	 * @return the listadoConvenios
	 */
	public ArrayList<HashMap<String, Object>> getListadoConvenios() {
		return listadoConvenios;
	}
	/**
	 * @return the listadoConvenios
	 */
	public int getSizeListadoConvenios() {
		return listadoConvenios.size();
	}
	/**
	 * @param listadoConvenios the listadoConvenios to set
	 */
	public void setListadoConvenios(
			ArrayList<HashMap<String, Object>> listadoConvenios) {
		this.listadoConvenios = listadoConvenios;
	}
	/**
	 * @return the existeAsocio
	 */
	public String getExisteAsocio() {
		return existeAsocio;
	}
	/**
	 * @param existeAsocio the existeAsocio to set
	 */
	public void setExisteAsocio(String existeAsocio) {
		this.existeAsocio = existeAsocio;
	}
	/**
	 * @return the viaIngreso
	 */
	public String getViaIngreso() {
		return viaIngreso;
	}
	/**
	 * @param viaIngreso the viaIngreso to set
	 */
	public void setViaIngreso(String viaIngreso) {
		this.viaIngreso = viaIngreso;
	}
	/**
	 * @return the estadoCuenta
	 */
	public String getEstadoCuenta() {
		return estadoCuenta;
	}
	/**
	 * @param estadoCuenta the estadoCuenta to set
	 */
	public void setEstadoCuenta(String estadoCuenta) {
		this.estadoCuenta = estadoCuenta;
	}
	/**
	 * @return the codigoTipoRegimen
	 */
	public String getCodigoTipoRegimen() {
		return codigoTipoRegimen;
	}
	/**
	 * @param codigoTipoRegimen the codigoTipoRegimen to set
	 */
	public void setCodigoTipoRegimen(String codigoTipoRegimen) {
		this.codigoTipoRegimen = codigoTipoRegimen;
	}
	/**
	 * @return the naturaleza
	 */
	public String getCodigoNaturaleza() {
		return naturaleza.getCodigo();
	}
	/**
	 * @param naturaleza the naturaleza to set
	 */
	public void setNombreNaturaleza(String naturaleza) {
		this.naturaleza.setNombre(naturaleza);
	}
	/**
	 * @return the naturaleza
	 */
	public String getNombreNaturaleza() {
		return naturaleza.getNombre();
	}
	/**
	 * @param naturaleza the naturaleza to set
	 */
	public void setCodigoNaturaleza(String naturaleza) {
		this.naturaleza.setCodigo(naturaleza);
	}

	/**
	 * @return the nombreEstratoSocial
	 */
	public String getNombreEstratoSocial() {
		return nombreEstratoSocial;
	}
	/**
	 * @param nombreEstratoSocial the nombreEstratoSocial to set
	 */
	public void setNombreEstratoSocial(String nombreEstratoSocial) {
		this.nombreEstratoSocial = nombreEstratoSocial;
	}
	/**
	 * @return the nombreMontoCobro
	 */
	public String getNombreMontoCobro() {
		return nombreMontoCobro;
	}
	/**
	 * @param nombreMontoCobro the nombreMontoCobro to set
	 */
	public void setNombreMontoCobro(String nombreMontoCobro) {
		this.nombreMontoCobro = nombreMontoCobro;
	}
	/**
	 * @return the nombreTipoMonto
	 */
	public String getNombreTipoMonto() {
		return nombreTipoMonto;
	}
	/**
	 * @param nombreTipoMonto the nombreTipoMonto to set
	 */
	public void setNombreTipoMonto(String nombreTipoMonto) {
		this.nombreTipoMonto = nombreTipoMonto;
	}
	/**
	 * @return the solicitudes
	 */
	public HashMap<String, Object> getSolicitudes() {
		return solicitudes;
	}
	/**
	 * @param solicitudes the solicitudes to set
	 */
	public void setSolicitudes(HashMap<String, Object> solicitudes) {
		this.solicitudes = solicitudes;
	}
	/**
	 * @return the solicitudes
	 */
	public Object getSolicitudes(String key) {
		return solicitudes.get(key);
	}
	/**
	 * @param solicitudes the solicitudes to set
	 */
	public void setSolicitudes(String key,Object obj) {
		this.solicitudes.put(key,obj);
	}
	/**
	 * @return the numSolicitudes
	 */
	public int getNumSolicitudes() {
		return numSolicitudes;
	}
	/**
	 * @param numSolicitudes the numSolicitudes to set
	 */
	public void setNumSolicitudes(int numSolicitudes) {
		this.numSolicitudes = numSolicitudes;
	}
	/**
	 * @return the esConvenioPoliza
	 */
	public String getEsConvenioPoliza() {
		return esConvenioPoliza;
	}
	/**
	 * @param esConvenioPoliza the esConvenioPoliza to set
	 */
	public void setEsConvenioPoliza(String esConvenioPoliza) {
		this.esConvenioPoliza = esConvenioPoliza;
	}
	/**
	 * @return the maxPageItems
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}
	/**
	 * @param maxPageItems the maxPageItems to set
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
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
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}
	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
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
	/**
	 * @return the valorDevolucionPaciente
	 */
	public String getValorDevolucionPaciente() {
		return valorDevolucionPaciente;
	}
	/**
	 * @param valorDevolucionPaciente the valorDevolucionPaciente to set
	 */
	public void setValorDevolucionPaciente(String valorDevolucionPaciente) {
		this.valorDevolucionPaciente = valorDevolucionPaciente;
	}
	/**
	 * @return the valorNetoPaciente
	 */
	public String getValorNetoPaciente() {
		return valorNetoPaciente;
	}
	/**
	 * @param valorNetoPaciente the valorNetoPaciente to set
	 */
	public void setValorNetoPaciente(String valorNetoPaciente) {
		this.valorNetoPaciente = valorNetoPaciente;
	}
	/**
	 * @return the valorTotalAbonos
	 */
	public String getValorTotalAbonos() {
		return valorTotalAbonos;
	}
	/**
	 * @param valorTotalAbonos the valorTotalAbonos to set
	 */
	public void setValorTotalAbonos(String valorTotalAbonos) {
		this.valorTotalAbonos = valorTotalAbonos;
	}
	/**
	 * @return the valorTotalCargos
	 */
	public String getValorTotalCargos() {
		return valorTotalCargos;
	}
	/**
	 * @param valorTotalCargos the valorTotalCargos to set
	 */
	public void setValorTotalCargos(String valorTotalCargos) {
		this.valorTotalCargos = valorTotalCargos;
	}
	/**
	 * @return the valorTotalConvenio
	 */
	public String getValorTotalConvenio() {
		return valorTotalConvenio;
	}
	/**
	 * @param valorTotalConvenio the valorTotalConvenio to set
	 */
	public void setValorTotalConvenio(String valorTotalConvenio) {
		this.valorTotalConvenio = valorTotalConvenio;
	}
	/**
	 * @return the valorTotalPaciente
	 */
	public String getValorTotalPaciente() {
		return valorTotalPaciente;
	}
	/**
	 * @param valorTotalPaciente the valorTotalPaciente to set
	 */
	public void setValorTotalPaciente(String valorTotalPaciente) {
		this.valorTotalPaciente = valorTotalPaciente;
	}
	/**
	 * Método implementado para llenar los totales a la forma
	 * @param estadoCuenta2
	 */
	public void llenarFormTotales(EstadoCuenta estadoCuenta) 
	{
		this.valorTotalCargos = UtilidadTexto.formatearValores(estadoCuenta.getValorTotalCargos());
		this.valorTotalPaciente = UtilidadTexto.formatearValores(estadoCuenta.getValorTotalPaciente());
		this.valorTotalConvenio = UtilidadTexto.formatearValores(estadoCuenta.getValorTotalConvenio());
		this.valorTotalConvenioDouble = estadoCuenta.getValorTotalConvenio();
		this.valorTotalAbonos = UtilidadTexto.formatearValores(estadoCuenta.getValorTotalAbonos());
		
		if(estadoCuenta.getValorDevolucionPaciente()>0)
		{
			this.valorDevolucionPaciente = UtilidadTexto.formatearValores(estadoCuenta.getValorDevolucionPaciente());
			this.valorNetoPaciente = "";
		}
		else
		{
			this.valorNetoPaciente = UtilidadTexto.formatearValores(estadoCuenta.getValorNetoPaciente());
			this.valorDevolucionPaciente = "";
		}
		
		//Solo aplica paara venezuela
		if(estadoCuenta.getValorSaldoMontoAutorizado()>0)
			this.valorSaldoMontoAutorizado = UtilidadTexto.formatearValores(estadoCuenta.getValorSaldoMontoAutorizado());
		else
			this.valorSaldoMontoAutorizado = "";
		
		if(this.manejaConversionMoneda && this.index>=0)
		{
			double factorConversion= Double.parseDouble(this.getTiposMonedaTagMap("factorconversion_"+index)+"");
			this.setValorTotalCargosConvertido(UtilidadTexto.formatearValores(estadoCuenta.getValorTotalCargos()/factorConversion));
			this.setValorTotalPacienteConvertido(UtilidadTexto.formatearValores(estadoCuenta.getValorTotalPaciente()/factorConversion));
			this.setValorTotalConvenioConvertido(UtilidadTexto.formatearValores(estadoCuenta.getValorTotalConvenio()/factorConversion));
			this.setValorTotalAbonosConvertido(UtilidadTexto.formatearValores(estadoCuenta.getValorTotalAbonos()/factorConversion));
			
			if(estadoCuenta.getValorDevolucionPaciente()>0)
			{
				this.valorDevolucionPacienteConvertido = UtilidadTexto.formatearValores(estadoCuenta.getValorDevolucionPaciente()/factorConversion);
				this.valorNetoPacienteConvertido = "";
			}
			else
			{
				this.valorNetoPacienteConvertido = UtilidadTexto.formatearValores(estadoCuenta.getValorNetoPaciente()/factorConversion);
				this.valorDevolucionPacienteConvertido = "";
			}
			
			//Solo aplica paara venezuela
			if(estadoCuenta.getValorSaldoMontoAutorizado()>0)
				this.valorSaldoMontoAutorizadoConvertido = UtilidadTexto.formatearValores(estadoCuenta.getValorSaldoMontoAutorizado()/factorConversion);
			else
				this.valorSaldoMontoAutorizadoConvertido = "";
			
		}
		
		
	}
	/**
	 * @return the valorSaldoMontoAutorizado
	 */
	public String getValorSaldoMontoAutorizado() {
		return valorSaldoMontoAutorizado;
	}
	/**
	 * @param valorSaldoMontoAutorizado the valorSaldoMontoAutorizado to set
	 */
	public void setValorSaldoMontoAutorizado(String valorSaldoMontoAutorizado) {
		this.valorSaldoMontoAutorizado = valorSaldoMontoAutorizado;
	}
	/**
	 * @return the valorTotalConvenioDouble
	 */
	public double getValorTotalConvenioDouble() {
		return valorTotalConvenioDouble;
	}
	/**
	 * @param valorTotalConvenioDouble the valorTotalConvenioDouble to set
	 */
	public void setValorTotalConvenioDouble(double valorTotalConvenioDouble) {
		this.valorTotalConvenioDouble = valorTotalConvenioDouble;
	}
	/**
	 * @return the codigoTipoSolicitud
	 */
	public int getCodigoTipoSolicitud() {
		return codigoTipoSolicitud;
	}
	/**
	 * @param codigoTipoSolicitud the codigoTipoSolicitud to set
	 */
	public void setCodigoTipoSolicitud(int codigoTipoSolicitud) {
		this.codigoTipoSolicitud = codigoTipoSolicitud;
	}
	/**
	 * @return the nombreTipoSolicitud
	 */
	public String getNombreTipoSolicitud() {
		return nombreTipoSolicitud;
	}
	/**
	 * @param nombreTipoSolicitud the nombreTipoSolicitud to set
	 */
	public void setNombreTipoSolicitud(String nombreTipoSolicitud) {
		this.nombreTipoSolicitud = nombreTipoSolicitud;
	}
	/**
	 * @return the numeroSolicitud
	 */
	public String getNumeroSolicitud() {
		return numeroSolicitud;
	}
	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(String numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}
	/**
	 * @return the posicion
	 */
	public int getPosicion() {
		return posicion;
	}
	/**
	 * @param posicion the posicion to set
	 */
	public void setPosicion(int posicion) {
		this.posicion = posicion;
	}
	/**
	 * @return the consecutivoOrden
	 */
	public String getConsecutivoOrden() {
		return consecutivoOrden;
	}
	/**
	 * @param consecutivoOrden the consecutivoOrden to set
	 */
	public void setConsecutivoOrden(String consecutivoOrden) {
		this.consecutivoOrden = consecutivoOrden;
	}
	/**
	 * @return the detalleSolicitud
	 */
	public HashMap<String, Object> getDetalleSolicitud() {
		return detalleSolicitud;
	}
	/**
	 * @param detalleSolicitud the detalleSolicitud to set
	 */
	public void setDetalleSolicitud(HashMap<String, Object> detalleSolicitud) {
		this.detalleSolicitud = detalleSolicitud;
	}
	
	/**
	 * @return the detalleSolicitud
	 */
	public Object getDetalleSolicitud(String key) {
		return detalleSolicitud.get(key);
	}
	/**
	 * @param detalleSolicitud the detalleSolicitud to set
	 */
	public void setDetalleSolicitud(String key,Object obj) {
		this.detalleSolicitud.put(key,obj);
	}
	/**
	 * @return the servicio
	 */
	public boolean isServicio() {
		return servicio;
	}
	/**
	 * @param servicio the servicio to set
	 */
	public void setServicio(boolean servicio) {
		this.servicio = servicio;
	}
	/**
	 * @return the busquedaAvazada
	 */
	public HashMap<String, Object> getBusquedaAvanzada() {
		return busquedaAvanzada;
	}
	/**
	 * @param busquedaAvazada the busquedaAvazada to set
	 */
	public void setBusquedaAvanzada(HashMap<String, Object> busquedaAvazada) {
		this.busquedaAvanzada = busquedaAvazada;
	}
	/**
	 * @return the busquedaAvazada
	 */
	public Object getBusquedaAvanzada(String key) {
		return busquedaAvanzada.get(key);
	}
	/**
	 * @param busquedaAvazada the busquedaAvazada to set
	 */
	public void setBusquedaAvanzada(String key,Object obj) {
		this.busquedaAvanzada.put(key, obj);
	}
	/**
	 * @return the estadosMedicos
	 */
	public HashMap getEstadosMedicos() {
		return estadosMedicos;
	}
	/**
	 * @param estadosMedicos the estadosMedicos to set
	 */
	public void setEstadosMedicos(HashMap estadosMedicos) {
		this.estadosMedicos = estadosMedicos;
	}
	/**
	 * @return the tiposSolicitud
	 */
	public HashMap getTiposSolicitud() {
		return tiposSolicitud;
	}
	/**
	 * @param tiposSolicitud the tiposSolicitud to set
	 */
	public void setTiposSolicitud(HashMap tiposSolicitud) {
		this.tiposSolicitud = tiposSolicitud;
	}
	/**
	 * @return the centrosCosto
	 */
	public HashMap getCentrosCosto() {
		return centrosCosto;
	}
	/**
	 * @param centrosCosto the centrosCosto to set
	 */
	public void setCentrosCosto(HashMap centrosCosto) {
		this.centrosCosto = centrosCosto;
	}
	/**
	 * @return the centroAtencion
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}
	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}
	/**
	 * @return the consecutivoIngreso
	 */
	public String getConsecutivoIngreso() {
		return consecutivoIngreso;
	}
	/**
	 * @param consecutivoIngreso the consecutivoIngreso to set
	 */
	public void setConsecutivoIngreso(String consecutivoIngreso) {
		this.consecutivoIngreso = consecutivoIngreso;
	}
	/**
	 * @return the estadoIngreso
	 */
	public String getEstadoIngreso() {
		return estadoIngreso;
	}
	/**
	 * @param estadoIngreso the estadoIngreso to set
	 */
	public void setEstadoIngreso(String estadoIngreso) {
		this.estadoIngreso = estadoIngreso;
	}
	/**
	 * @return the fechaIngreso
	 */
	public String getFechaIngreso() {
		return fechaIngreso;
	}
	/**
	 * @param fechaIngreso the fechaIngreso to set
	 */
	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
	
	/**
	 * @return the manejaConversionMoneda
	 */
	public boolean getManejaConversionMoneda() {
		return manejaConversionMoneda;
	}

	/**
	 * @param manejaConversionMoneda the manejaConversionMoneda to set
	 */
	public void setManejaConversionMoneda(boolean manejaConversionMoneda) {
		this.manejaConversionMoneda = manejaConversionMoneda;
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
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	/**
	 * @return the valorTotalCargosConvertido
	 */
	public String getValorTotalCargosConvertido() {
		return valorTotalCargosConvertido;
	}
	/**
	 * @param valorTotalCargosConvertido the valorTotalCargosConvertido to set
	 */
	public void setValorTotalCargosConvertido(String valorTotalCargosConvertido) {
		this.valorTotalCargosConvertido = valorTotalCargosConvertido;
	}
	/**
	 * @return the valorTotalPacienteConvertido
	 */
	public String getValorTotalPacienteConvertido() {
		return valorTotalPacienteConvertido;
	}
	/**
	 * @param valorTotalPacienteConvertido the valorTotalPacienteConvertido to set
	 */
	public void setValorTotalPacienteConvertido(String valorTotalPacienteConvertido) {
		this.valorTotalPacienteConvertido = valorTotalPacienteConvertido;
	}
	/**
	 * @return the valorTotalConvenioConvertido
	 */
	public String getValorTotalConvenioConvertido() {
		return valorTotalConvenioConvertido;
	}
	/**
	 * @param valorTotalConvenioConvertido the valorTotalConvenioConvertido to set
	 */
	public void setValorTotalConvenioConvertido(String valorTotalConvenioConvertido) {
		this.valorTotalConvenioConvertido = valorTotalConvenioConvertido;
	}
	/**
	 * @return the valorTotalAbonosConvertido
	 */
	public String getValorTotalAbonosConvertido() {
		return valorTotalAbonosConvertido;
	}
	/**
	 * @param valorTotalAbonosConvertido the valorTotalAbonosConvertido to set
	 */
	public void setValorTotalAbonosConvertido(String valorTotalAbonosConvertido) {
		this.valorTotalAbonosConvertido = valorTotalAbonosConvertido;
	}
	/**
	 * @return the valorDevolucionPacienteConvertido
	 */
	public String getValorDevolucionPacienteConvertido() {
		return valorDevolucionPacienteConvertido;
	}
	/**
	 * @param valorDevolucionPacienteConvertido the valorDevolucionPacienteConvertido to set
	 */
	public void setValorDevolucionPacienteConvertido(
			String valorDevolucionPacienteConvertido) {
		this.valorDevolucionPacienteConvertido = valorDevolucionPacienteConvertido;
	}
	/**
	 * @return the valorNetoPacienteConvertido
	 */
	public String getValorNetoPacienteConvertido() {
		return valorNetoPacienteConvertido;
	}
	/**
	 * @param valorNetoPacienteConvertido the valorNetoPacienteConvertido to set
	 */
	public void setValorNetoPacienteConvertido(String valorNetoPacienteConvertido) {
		this.valorNetoPacienteConvertido = valorNetoPacienteConvertido;
	}
	/**
	 * @return the valorSaldoMontoAutorizadoConvertido
	 */
	public String getValorSaldoMontoAutorizadoConvertido() {
		return valorSaldoMontoAutorizadoConvertido;
	}
	/**
	 * @param valorSaldoMontoAutorizadoConvertido the valorSaldoMontoAutorizadoConvertido to set
	 */
	public void setValorSaldoMontoAutorizadoConvertido(
			String valorSaldoMontoAutorizadoConvertido) {
		this.valorSaldoMontoAutorizadoConvertido = valorSaldoMontoAutorizadoConvertido;
	}
	/**
	 * @return the descEmpresaSubContratada
	 */
	public String getDescEmpresaSubContratada() {
		return descEmpresaSubContratada;
	}
	/**
	 * @param descEmpresaSubContratada the descEmpresaSubContratada to set
	 */
	public void setDescEmpresaSubContratada(String descEmpresaSubContratada) {
		this.descEmpresaSubContratada = descEmpresaSubContratada;
	}
	/**
	 * @return the materialesEspeciales
	 */
	public ArrayList<HashMap<String, Object>> getMaterialesEspeciales() {
		return materialesEspeciales;
	}
	/**
	 * @param materialesEspeciales the materialesEspeciales to set
	 */
	public void setMaterialesEspeciales(
			ArrayList<HashMap<String, Object>> materialesEspeciales) {
		this.materialesEspeciales = materialesEspeciales;
	}
	
	/**
	 * Método que retorna el tamaño de los materiales especiales
	 * @return
	 */
	public int getNumMaterialesEspeciales()
	{
		return this.materialesEspeciales.size();
	}
	public HashMap getEsquemasInventario() {
		return esquemasInventario;
	}
	public void setEsquemasInventario(HashMap esquemasInventario) {
		this.esquemasInventario = esquemasInventario;
	}
	public HashMap getEsquemasProcedimientos() {
		return esquemasProcedimientos;
	}
	public void setEsquemasProcedimientos(HashMap esquemasProcedimientos) {
		this.esquemasProcedimientos = esquemasProcedimientos;
	}
	/**
	 * @return the currentPageNumber
	 */
	public int getCurrentPageNumber() {
		return currentPageNumber;
	}
	/**
	 * @param currentPageNumber the currentPageNumber to set
	 */
	public void setCurrentPageNumber(int currentPageNumber) {
		this.currentPageNumber = currentPageNumber;
	}
	public HashMap getEstadosFacturacion() {
		return estadosFacturacion;
	}
	public void setEstadosFacturacion(HashMap estadosFacturacion) {
		this.estadosFacturacion = estadosFacturacion;
	}
	public void setMostrarArticPrinciDespachoCero(String mostrarArticPrinciDespachoCero) {
		MostrarArticPrinciDespachoCero = mostrarArticPrinciDespachoCero;
	}
	public String getMostrarArticPrinciDespachoCero() {
		return MostrarArticPrinciDespachoCero;
	}
}
