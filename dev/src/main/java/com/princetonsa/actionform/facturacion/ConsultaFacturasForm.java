/*
 * @(#)ConsultaFacturasForm.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.mundo.facturacion.ConsultaFacturas;

/**
 * Forma para manejo presentación de la funcionalidad 
 * Consulta de Facturas. 
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 09 /Jun/ 2005
 */
public class ConsultaFacturasForm extends ActionForm
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	private String codigoFactura;
	
	/**
	 * 
	 */
	private double empresaInstitucion;
	
	/**
	 * 
	 */
	private HashMap empresasInstitucionMap;
	
	/**
	 * Estado en el que se encuentra el proceso.
	 */
	private  String estado = "";
	
	/**
	 * Cuenta del paciente cargado
	 */
	private int cuenta;
	
	/**
	 * Descripcion del Servicio
	 */
	private String  descripcionServicio;
	
	/**
	 * Fecha Y Hora de elaboracion De la Factura
	 */
	private String fechaHoraElaboracion;
	
	
	/**
	 * almacena los datos de la consulta
	 */
	private HashMap mapaFacturasPaciente;
	
	/**
	 * almacena los datos de la consulta de una factura en especifico
	 */
	private HashMap mapaDetalleFactura;
	
	/**
	 * almacena el numero de filas en el HashMap mapaActualizacion
	 */
	private int numeroElementos;
	
	/**
	 * Patron de ordenamiento por columnas
	 */
	private String patronOrdenar;
	
	/**
	 * String ultimo patron de ordenamiento
	 */
	private String ultimoPatron;
	
	/**
	 * String del responsable de la factura
	 */
	private String responsable;
	
	/**
	 * Via de Ingreso
	 */
	private String viaIngreso;
	
	/**
	 * Estado de la factura 
	 */
	private String estadoFactura;
	
	/**
	 * Esatdo del paciente con respecto a la Factura
	 */
	private String estadoPaciente;
	
	/**
 	 * Este campo contiene el pageUrl para controlar el pager,
 	 *  y conservar los valores del hashMap mediante un submit de
 	 * JavaScript. (Integra pager -Valor Captura)
 	 */
     private String linkSiguiente;

          
     /**
      * Poscicion del mapa en la consulta de facturas
      */
     private int posicionMapa;
     
     /**
      * Mapa para la factura en la busqueda Avanzada
      */
     private HashMap mapaFacturasTodos;
     
     /**
      * String de la fecha inicial para la busqueda de Facturas
      */
     private String fechaElaboracionInicial;
     
     /**
      * String de la fehca final para la busqueda de Facturas
      */
     private String fechaElaboracionFinal;
     
     /**
      * int para el numero de factura en la busqueda de facturas
      */
     private int facturaInicial;
     
     /**
      * int para el numero de factura de la busqueda de facturas
      */
     private int facturaFinal;
     
     /**
      * Total de la Factura
      */
     private double totalFactura;
     
     /**
      * Total del Resposable de la factura
      */
     private double totalResonsable;
     
     /**
      * Total bruto a cargo del paciente
      */
     private double totalBrutoPaciente;
     
     /**
      * Total de Descuentos
      */
     private double totalDescuentos;
     
     /**
      * Total de Abonos
      */
     private double totalAbonos;
     
     /**
      * Total del neto a cargo del Paciente
      */
     private double totalNetoPaciente;
     
     /**
      * Monto de Cobro
      */
     private double montoCobro;
     
     /**
      * Numero del Recibo de caja asociado a la factura
      */
     private int reciboCaja;
     
     /**
      * Numero de la Cuenta de Cobro a al a	que pertenece la factura
      */
     private double cuentaCobro;
     
     /**
      * para almacenar valores temporales
      * del formulario.
      */
     private int propiedadTempI;
     
     /**
      * para almacenar valores temporales
      * del formulario.
      */
     private int propiedadTempViaIngreso;
     
     /**
      * para almacenar valores temporales
      * del formulario.
      */
     private int propiedadTempEFactura;
     
     /**
      * para almacenar valores temporales
      * del formulario.
      */
     private int propiedadTempEPaciente;
     
     
     /**
      * Centro de costo de los servicios y/o artiuclos
      */
     private String area;
     
     /**
      * Código del servicio y/o ariticulo
      */
     private int codigo;
     
     /**
      * Cantidad de Servicios y/o Articulos
      */
     private int cantidad;
     
     /**
      * Valor Unitario del Servicio  y/o  Articulo
      */
     private double valorUnitario;
     
     /**
      * Valor Total de Servicio y/o Articulo
      */
     private double valorTotal;
     
     /**
      * Mapa para las Solicitudes de una factura
      */
     private HashMap mapaSolicitudesFactura;
     
     /**
 	 * Offset para el pager 
 	 */
 	private int offset=0;

	/**
	 * Listado para el listado de recibos de caja asociados a una factura....
	 */
 	
 	private Collection listaRecibos;  
 	
 	/**
 	 * 
 	 */
 	private boolean imprimirAnexo;
 	/**
 	 * 
 	 */
 	private String tipoAnexo;
 	
 	/**
 	 * Usuario que realizo la factura
 	 */
 	private String usuario;
 	
 	/**
 	 * codigo del centro de atencion
 	 */
 	private int codigoCentroAtencion;
 	
 	/**
 	 * 
 	 */
 	private HashMap entidadesSubcontratadasMap;
 	
 	/**
 	 * 
 	 */
 	private double entidadSubcontratada;
 	 	
 	/**
 	 * Tipo de reporte seleccionado
 	 */
 	private String tipoReporte;
 	
 	/**
 	 * Tipo de salida seleccionado (Imprimir o Archivo Plano)
 	 */
 	private String tipoSalida;
 	
 	/**
 	 * Tipo de Paciente 
 	 */
 	private String tipoPaciente;
 	
 	/**
	 * Mensaje generacion del archivo
	 */
	private ResultadoBoolean mensaje = new ResultadoBoolean(false);
    
	/**
	 * Path completo del archivo generado
	 */
	private String pathArchivoTxt;
	
	/**
	 * Controla si se genera el archivo o no?
	 */
	private boolean archivo;
	
	/**
	 * Tipo de Impresion Factura
	 */
	private String tipoImpresion;
	
	/**
	 * Anexo de Cuenta
	 */
	private String anexoC;
	
	/**
	 * formato de Impresion del Convenio
	 */
	private String formatoConvenio;
 	
	/**
	 * Sin errores en el validate
	 */
	private boolean errores;
	
	/**
	 * Valida si se genero el archivo .zip para informar al usuario
	 */
	private boolean zip;
 	
	/**
	 * String que guarda el nombre del formato de impresión para Venezuela
	 */
	private String formatoImpresionVenezuela;
	
	private String nombreArchivoGenerado = "";
	
	private String nombreArchivoGeneradoOriginal = "";
	
	/**
	 * 
	 */
	private boolean tienePermisoImprimirOriginalFactura;
	
	private Integer tipoImpresionPermiso=ConstantesBD.codigoNuncaValido;
	
	/**
	 * Numero Autorizacion
	 */
   private String numeroAutorizacion;
	
	public void reset ()
	{
		this.codigoFactura="";
		this.empresaInstitucion=ConstantesBD.codigoNuncaValidoDoubleNegativo;
		this.empresasInstitucionMap= new HashMap();
		this.empresasInstitucionMap.put("numRegistros", "0");
		
		this.mapaDetalleFactura = new HashMap ();
		this.mapaFacturasPaciente = new HashMap ();
		this.mapaFacturasTodos = new HashMap ();
		this.entidadesSubcontratadasMap=new HashMap();
		this.entidadesSubcontratadasMap.put("numRegistros", "0");
		this.entidadSubcontratada=ConstantesBD.codigoNuncaValidoDoubleNegativo;
		
		this.tipoPaciente="-1";
		this.estado="";
		this.cuenta=0;
		this.linkSiguiente="";
		this.fechaHoraElaboracion="";
		this.descripcionServicio="";
		this.area="";
		this.cantidad=0;
		this.codigo=0;
		this.cuentaCobro=0;
		this.facturaFinal=0;
		this.facturaInicial=0;
		this.fechaElaboracionFinal="";
		this.fechaElaboracionInicial="";
		this.montoCobro=0;
		this.reciboCaja=0;
		this.totalAbonos=0;
		this.totalBrutoPaciente=0;
		this.totalDescuentos=0;
		this.totalFactura=0;
		this.totalNetoPaciente=0;
		this.totalResonsable=0;
		this.valorTotal=0;
		this.valorUnitario=0;
		this.responsable="";
		this.viaIngreso="";
		this.estadoFactura="";
		this.estadoPaciente="";
	 	this.listaRecibos = new ArrayList();
	 	this.imprimirAnexo=false;
	 	this.tipoAnexo="";
	 	this.usuario="";
	 	this.codigoCentroAtencion=ConstantesBD.codigoNuncaValido;
	 	this.viaIngreso="-1";
	 	this.tipoReporte = ConstantesIntegridadDominio.acronimoTipoReporteConvenioPaciente;
	 	this.tipoSalida = ConstantesIntegridadDominio.acronimoTipoSalidaImpresion;
	 	this.pathArchivoTxt = "";
	 	this.archivo = false;
	 	this.errores = false;
	 	this.zip = false;
	 	this.tipoImpresion="";
	 	this.anexoC="";
	 	this.formatoConvenio="";
	 	this.formatoImpresionVenezuela = "";
	 	this.numeroAutorizacion="";
	 	nombreArchivoGenerado = "";
	 	nombreArchivoGeneradoOriginal = "";
	}
	
	/**
	 * Reset único para los mapas
	 */
	public void resetMapa()
	{
		this.mapaDetalleFactura = new HashMap ();
		this.mapaFacturasPaciente = new HashMap ();
		this.mapaFacturasTodos = new HashMap ();
		
		this.entidadesSubcontratadasMap=new HashMap();
		this.entidadesSubcontratadasMap.put("numRegistros", "0");
		
		this.empresasInstitucionMap=new HashMap();
		this.empresasInstitucionMap.put("numRegistros", "0");
		
	}
	
	/**
	 * @return Returns the usuario.
	 */
	public String getUsuario()
	{
		return usuario;
	}


	/**
	 * @param usuario The usuario to set.
	 */
	public void setUsuario(String usuario)
	{
		this.usuario=usuario;
	}


	/**
	 * @return Returns the propiedadTempEFactura.
	 */
	public int getPropiedadTempEFactura()
	{
		return propiedadTempEFactura;
	}
	/**
	 * @param propiedadTempEFactura The propiedadTempEFactura to set.
	 */
	public void setPropiedadTempEFactura(int propiedadTempEFactura)
	{
		this.propiedadTempEFactura= propiedadTempEFactura;
	}
	/**
	 * @return Returns the propiedadTempEPaciente.
	 */
	public int getPropiedadTempEPaciente()
	{
		return propiedadTempEPaciente;
	}
	/**
	 * @param propiedadTempEPaciente The propiedadTempEPaciente to set.
	 */
	public void setPropiedadTempEPaciente(int propiedadTempEPaciente)
	{
		this.propiedadTempEPaciente= propiedadTempEPaciente;
	}
	/**
	 * @return Returns the propiedadTempViaIngreso.
	 */
	public int getPropiedadTempViaIngreso()
	{
		return propiedadTempViaIngreso;
	}
	/**
	 * @param propiedadTempViaIngreso The propiedadTempViaIngreso to set.
	 */
	public void setPropiedadTempViaIngreso(int propiedadTempViaIngreso)
	{
		this.propiedadTempViaIngreso= propiedadTempViaIngreso;
	}
	/**
	 * @return Returns the propiedadTempI.
	 */
	public int getPropiedadTempI()
	{
		return propiedadTempI;
	}
	/**
	 * @param propiedadTempI The propiedadTempI to set.
	 */
	public void setPropiedadTempI(int propiedadTempI)
	{
		this.propiedadTempI= propiedadTempI;
	}
	/**
	 * @return Returns the estadoFactura.
	 */
	public String getEstadoFactura()
	{
		return estadoFactura;
	}
	/**
	 * @param estadoFactura The estadoFactura to set.
	 */
	public void setEstadoFactura(String estadoFactura)
	{
		this.estadoFactura= estadoFactura;
	}
	/**
	 * @return Returns the estadoPaciente.
	 */
	public String getEstadoPaciente()
	{
		return estadoPaciente;
	}
	/**
	 * @param estadoPaciente The estadoPaciente to set.
	 */
	public void setEstadoPaciente(String estadoPaciente)
	{
		this.estadoPaciente= estadoPaciente;
	}
	/**
	 * @return Returns the responsable.
	 */
	public String getResponsable()
	{
		return responsable;
	}
	/**
	 * @param responsable The responsable to set.
	 */
	public void setResponsable(String responsable)
	{
		this.responsable= responsable;
	}
	/**
	 * @return Returns the viaIngreso.
	 */
	public String getViaIngreso()
	{
		return viaIngreso;
	}
	/**
	 * @param viaIngreso The viaIngreso to set.
	 */
	public void setViaIngreso(String viaIngreso)
	{
		this.viaIngreso= viaIngreso;
	}
	/**
	 * @param mapaSolicitudesFactura The mapaSolicitudesFactura to set.
	 */
	public void setMapaSolicitudesFactura(HashMap mapaSolicitudesFactura)
	{
		this.mapaSolicitudesFactura= mapaSolicitudesFactura;
	}
	/**
	 * @return Returns the area.
	 */
	public String getArea()
	{
		return area;
	}
	/**
	 * @param area The area to set.
	 */
	public void setArea(String area)
	{
		this.area= area;
	}
	/**
	 * @return Returns the cantidad.
	 */
	public int getCantidad()
	{
		return cantidad;
	}
	/**
	 * @param cantidad The cantidad to set.
	 */
	public void setCantidad(int cantidad)
	{
		this.cantidad= cantidad;
	}
	/**
	 * @return Returns the codigo.
	 */
	public int getCodigo()
	{
		return codigo;
	}
	/**
	 * @param codigo The codigo to set.
	 */
	public void setCodigo(int codigo)
	{
		this.codigo= codigo;
	}
	/**
	 * @return Returns the cuentaCobro.
	 */
	public double getCuentaCobro()
	{
		return cuentaCobro;
	}
	/**
	 * @param cuentaCobro The cuentaCobro to set.
	 */
	public void setCuentaCobro(double cuentaCobro)
	{
		this.cuentaCobro= cuentaCobro;
	}
	/**
	 * @return Returns the facturaFinal.
	 */
	public int getFacturaFinal()
	{
		return facturaFinal;
	}
	/**
	 * @param facturaFinal The facturaFinal to set.
	 */
	public void setFacturaFinal(int facturaFinal)
	{
		this.facturaFinal= facturaFinal;
	}
	/**
	 * @return Returns the facturaInicial.
	 */
	public int getFacturaInicial()
	{
		return facturaInicial;
	}
	/**
	 * @param facturaInicial The facturaInicial to set.
	 */
	public void setFacturaInicial(int facturaInicial)
	{
		this.facturaInicial= facturaInicial;
	}
	/**
	 * @return Returns the fechaElaboracionFinal.
	 */
	public String getFechaElaboracionFinal()
	{
		return fechaElaboracionFinal;
	}
	/**
	 * @param fechaElaboracionFinal The fechaElaboracionFinal to set.
	 */
	public void setFechaElaboracionFinal(String fechaElaboracionFinal)
	{
		this.fechaElaboracionFinal= fechaElaboracionFinal;
	}
	/**
	 * @return Returns the fechaElaboracionInicial.
	 */
	public String getFechaElaboracionInicial()
	{
		return fechaElaboracionInicial;
	}
	/**
	 * @param fechaElaboracionInicial The fechaElaboracionInicial to set.
	 */
	public void setFechaElaboracionInicial(String fechaElaboracionInicial)
	{
		this.fechaElaboracionInicial= fechaElaboracionInicial;
	}
	/**
	 * @return Returns the montoCobro.
	 */
	public double getMontoCobro()
	{
		return montoCobro;
	}
	/**
	 * @param montoCobro The montoCobro to set.
	 */
	public void setMontoCobro(double montoCobro)
	{
		this.montoCobro= montoCobro;
	}
	/**
	 * @return Returns the reciboCaja.
	 */
	public int getReciboCaja()
	{
		return reciboCaja;
	}
	/**
	 * @param reciboCaja The reciboCaja to set.
	 */
	public void setReciboCaja(int reciboCaja)
	{
		this.reciboCaja= reciboCaja;
	}
	/**
	 * @return Returns the totalAbonos.
	 */
	public double getTotalAbonos()
	{
		return totalAbonos;
	}
	/**
	 * @param totalAbonos The totalAbonos to set.
	 */
	public void setTotalAbonos(double totalAbonos)
	{
		this.totalAbonos= totalAbonos;
	}
	/**
	 * @return Returns the totalBrutoPaciente.
	 */
	public double getTotalBrutoPaciente()
	{
		return totalBrutoPaciente;
	}
	/**
	 * @param totalBrutoPaciente The totalBrutoPaciente to set.
	 */
	public void setTotalBrutoPaciente(double totalBrutoPaciente)
	{
		this.totalBrutoPaciente= totalBrutoPaciente;
	}
	/**
	 * @return Returns the totalDescuentos.
	 */
	public double getTotalDescuentos()
	{
		return totalDescuentos;
	}
	/**
	 * @param totalDescuentos The totalDescuentos to set.
	 */
	public void setTotalDescuentos(double totalDescuentos)
	{
		this.totalDescuentos= totalDescuentos;
	}
	/**
	 * @return Returns the totalFactura.
	 */
	public double getTotalFactura()
	{
		return totalFactura;
	}
	/**
	 * @param totalFactura The totalFactura to set.
	 */
	public void setTotalFactura(double totalFactura)
	{
		this.totalFactura= totalFactura;
	}
	/**
	 * @return Returns the totalNetoPaciente.
	 */
	public double getTotalNetoPaciente()
	{
		return totalNetoPaciente;
	}
	/**
	 * @param totalNetoPaciente The totalNetoPaciente to set.
	 */
	public void setTotalNetoPaciente(double totalNetoPaciente)
	{
		this.totalNetoPaciente= totalNetoPaciente;
	}
	/**
	 * @return Returns the totalResonsable.
	 */
	public double getTotalResonsable()
	{
		return totalResonsable;
	}
	/**
	 * @param totalResonsable The totalResonsable to set.
	 */
	public void setTotalResonsable(double totalResonsable)
	{
		this.totalResonsable= totalResonsable;
	}
	/**
	 * @return Returns the valorTotal.
	 */
	public double getValorTotal()
	{
		return valorTotal;
	}
	/**
	 * @param valorTotal The valorTotal to set.
	 */
	public void setValorTotal(double valorTotal)
	{
		this.valorTotal= valorTotal;
	}
	/**
	 * @return Returns the valorUnitario.
	 */
	public double getValorUnitario()
	{
		return valorUnitario;
	}
	/**
	 * @param valorUnitario The valorUnitario to set.
	 */
	public void setValorUnitario(double valorUnitario)
	{
		this.valorUnitario= valorUnitario;
	}
	/**
	 * @return Returns the posicionMapa.
	 */
	public int getPosicionMapa()
	{
		return posicionMapa;
	}
	/**
	 * @param posicionMapa The posicionMapa to set.
	 */
	public void setPosicionMapa(int posicionMapa)
	{
		this.posicionMapa= posicionMapa;
	}
	
	/**
	 * @return Returns the descripcionServicio.
	 */
	public String getDescripcionServicio()
	{
		return descripcionServicio;
	}
	/**
	 * @param descripcionServicio The descripcionServicio to set.
	 */
	public void setDescripcionServicio(String descripcionServicio)
	{
		this.descripcionServicio= descripcionServicio;
	}
	
	/**
	 * @return Returns the fechaHoraElaboracion
	 */
	public String getFechaHoraElaboracion()
	{
		return fechaHoraElaboracion;
	}
	/**
	 * @param fechaHoraElaboracion The fechaSolicitud to set.
	 */
	public void setFechaHoraElaboracion(String fechaHoraElaboracion)
	{
		this.fechaHoraElaboracion= fechaHoraElaboracion;
	}
	
	/**
	 * @return Returns the linkSiguiente.
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
		this.linkSiguiente= linkSiguiente;
	}
	/**
	 * @return Returns the offset.
	 */
	public int getOffset()
	{
		return offset;
	}
	/**
	 * @param offset The offset to set.
	 */
	public void setOffset(int offset)
	{
		this.offset= offset;
	}
	
	/**
	 * @return Returns the cuenta.
	 */
	public int getCuenta()
	{
		return cuenta;
	}
	/**
	 * @param cuenta The cuenta to set.
	 */
	public void setCuenta(int cuenta)
	{
		this.cuenta= cuenta;
	}
	
    /**
     * @return Retorna el estado.
     */
    public  String getEstado()
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
	 * @return Returns the mapaDetalleFactura.
	 */
	public HashMap getMapaDetalleFactura()
	{
		return mapaDetalleFactura;
	}
	
	/**
	 * @param mapaDetalleFactura The mapaDetalleFactura to set.
	 */
	public void setMapaDetalleFactura(HashMap mapaDetalleFactura)
	{
		this.mapaDetalleFactura= mapaDetalleFactura;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaDetalleFactura(String key, Object value) 
	{
		mapaDetalleFactura.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaDetalleFactura(String key) 
	{
		return mapaDetalleFactura.get(key);
	}
	
	/**
	 * @return Returns the mapaFacturasPaciente.
	 */
	public HashMap getMapaFacturasPaciente()
	{
		return mapaFacturasPaciente;
	}
	
	/**
	 * @param mapaFacturasPaciente The mapaFacturasPaciente to set.
	 */
	public void setMapaFacturasPaciente(HashMap mapaFacturasPaciente)
	{
		this.mapaFacturasPaciente= mapaFacturasPaciente;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaFacturasPaciente(String key, Object value) 
	{
		mapaFacturasPaciente.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaFacturasPaciente(String key) 
	{
		return mapaFacturasPaciente.get(key);
	}

	
	/**
	 * @return Returns the numeroElementos.
	 */
	public int getNumeroElementos()
	{
		return numeroElementos;
	}
	/**
	 * @param numeroElementos The numeroElementos to set.
	 */
	public void setNumeroElementos(int numeroElementos)
	{
		this.numeroElementos= numeroElementos;
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
	 * @return Retorna el ultimoPatron.
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}
	/**
	 * @param ultimoPatron Asigna el ultimoPatron.
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}
	
	/**
	 * @return Returns the mapaFacturasTodos.
	 */
	public HashMap getMapaFacturasTodos()
	{
		return mapaFacturasTodos;
	}
	
	/**
	 * @param mapaFacturasTodos The mapaFacturasTodos to set.
	 */
	public void setMapaFacturasTodos(HashMap mapaFacturasTodos)
	{
		this.mapaFacturasTodos= mapaFacturasTodos;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaFacturasTodos(String key, Object value) 
	{
		mapaFacturasTodos.put(key, value);
	}
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaFacturasTodos(String key) 
	{
		return mapaFacturasTodos.get(key);
	}
	
	/**
	 * @return Returns the mapaSolicitudesFactura.
	 */
	public HashMap getMapaSolicitudesFactura()
	{
		return mapaSolicitudesFactura;
	}
	
	/**
	 * @param mapaSolicitudesFactura The mapaSolicitudesFactura to set.
	 */
	public void getMapaSolicitudesFactura(HashMap mapaSolicitudesFactura)
	{
		this.mapaSolicitudesFactura= mapaSolicitudesFactura;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setMapaSolicitudesFactura(String key, Object value) 
	{
		mapaSolicitudesFactura.put(key, value);
	}
	
	/**
	 * @param key
	 * @return
	 */
	public Object getMapaSolicitudesFactura(String key) 
	{
		return mapaSolicitudesFactura.get(key);
	}

	
	/**
	 * @return Retorna listaRecibos.
	 */
	public Collection getListaRecibos() {
		return listaRecibos;
	}
	/**
	 * @param Asigna listaRecibos.
	 */
	public void setListaRecibos(Collection listaRecibos) {
		this.listaRecibos = listaRecibos;
	}

	
	
	/**
	 * Función de validación: 
	 * @param mapping
	 * @param request
	 * @return ActionError que especifica el error
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		
		ActionErrors errores = new ActionErrors();
		/*********************************************************************************/
		if(estado.equals("resultadoBusqueda"))
		{
			if(		this.getFacturaInicial()==0&&this.getFacturaFinal()==0&&this.getFechaElaboracionInicial().trim().equals("")
					&&this.getFechaElaboracionFinal().trim().equals("")&&this.getPropiedadTempI()==-1
					&&this.getPropiedadTempViaIngreso()==-1&&this.getPropiedadTempEFactura()==-1
					&&this.getPropiedadTempEPaciente()==-1&&this.getUsuario().equals("-1")
					&&this.getEmpresaInstitucion()==ConstantesBD.codigoNuncaValidoDoubleNegativo
					&&this.getEntidadSubcontratada()==ConstantesBD.codigoNuncaValidoDoubleNegativo
					&&this.getTipoSalida().trim().equals("")
					&&this.getTipoReporte().trim().equals(""))
			{
				errores.add("Definir algun campo para la Busqueda", new ActionMessage("error.facturacion.camposBusquedaNoDefinidos","Definir algun campo para la Busqueda"));
			}
			
			if((this.getFacturaInicial()>0&&this.getFacturaFinal()==0))
			{
				errores.add("Definir Factura Final", new ActionMessage("error.facturacion.rangosFacturaFinalNoDefinidos","Definir Factura Final"));
			}
			if((this.getFacturaInicial()==0&&this.getFacturaFinal()>0))
			{
				errores.add("Definir Factura Inicial", new ActionMessage("error.facturacion.rangosFacturaInicialNoDefinidos","Definir Factura Inicial"));
			}
			if(this.getFacturaFinal()>0&&this.getFacturaInicial()>0){
				if((this.getFacturaFinal()<this.getFacturaInicial()))
				{
					errores.add("Rango Facturas", new ActionMessage("error.facturacion.facturaInicialMayorFacturaFinal","Rango Facturas"));
				}
			}
			if(this.getFacturaInicial()<0)
			{
				errores.add("errors.integer", new ActionMessage("errors.integer","El Rango Inicial de Facturas"));
			}
			if(this.getFacturaFinal()<0)
			{
				errores.add("errors.integer", new ActionMessage("errors.integer","El Rango Final de Facturas"));
			}
			if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaElaboracionInicial().trim())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual().trim()))>0)
			{
				errores.add("Fecha Elaboracion Inicial mayor a la fecha del Sistema", new ActionMessage("errors.fechaPosteriorIgualActual","Elaboracion Inicial", " Actual"));
			}
			if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaElaboracionFinal())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
			{
				errores.add("Fecha Elaboracion Final mayor a la fecha del Sistema", new ActionMessage("errors.fechaPosteriorIgualActual","Elaboracion Final", " Actual"));
			}
			if((UtilidadFecha.conversionFormatoFechaABD(this.getFechaElaboracionFinal())).compareTo(UtilidadFecha.conversionFormatoFechaABD(this.getFechaElaboracionInicial()))<0)
			{
				errores.add("Fecha Elaboracion Final menor a Fecha Elaboracion Incial", new ActionMessage("errors.fechaPosteriorIgualActual","La Fecha de Elaboracion Final", " de Elaboracion Inicial"));				
			}
			if((!this.getFechaElaboracionInicial().equals(""))&&(this.getFechaElaboracionFinal().equals("")))
			{
				errores.add("Definir Fecha Elaboracion Final", new ActionMessage("error.facturacion.rangosFechaFinalNoDefinidos","Definir Fecha de Elaboracion Final"));
			}
			if((this.getFechaElaboracionInicial().trim().equals("")&&!this.getFechaElaboracionFinal().trim().equals("")))
			{
				errores.add("Definir Fecha Elaboracion Inicial", new ActionMessage("error.facturacion.rangosFechaInicialNoDefinidos","Definir Fecha Elaboracion Inicial"));
			}
		
			/*Validaciones Anexo 633 Tipo de Salida, Tipo de Reporte Requerido
			Fecha Elaboracion Inicial y Fecha Elaboracion Final requeridos si
			el tipo de reporte es por Convenio o por Facturacion/Radicacion
			*/
			if(this.tipoSalida==null || this.tipoSalida.trim().equals(""))
			{
				errores.add("codigo", new ActionMessage("errors.required","El Tipo Salida "));
			}
			if(this.tipoReporte==null ||this.tipoReporte.trim().equals(""))
			{
				errores.add("codigo", new ActionMessage("errors.required","El Tipo Reporte "));
			}
			
			//si los errores son empty entonces validamos que se seleccione o un rango de fechas de 6 meses o un rango de consecutivos de facturas que no supere los 200000, segun requerimiento documentacion x error courrido en suba el 2009-07-08
			if(errores.isEmpty())
			{
				
				if(UtilidadTexto.isEmpty(this.getFechaElaboracionInicial().trim()) 
					&& UtilidadTexto.isEmpty(this.getFechaElaboracionFinal().trim())
					&& this.getFacturaInicial()<=0 
					&& this.getFacturaFinal()<=0)
				{
					errores.add("codigo", new ActionMessage("errors.required","Para realizar la busqueda debe seleccionar o el rango de fechas o el rango de facturas"));
					this.errores=true;
				}
				else
				{
					if((!this.getFechaElaboracionInicial().trim().equals("")&&!this.getFechaElaboracionFinal().trim().equals("")))
					{
						int nroMeses = UtilidadFecha.numeroMesesEntreFechas(this.fechaElaboracionInicial, this.fechaElaboracionFinal,true);
						//nroMeses = nroMeses + 1;
						if (nroMeses > 6)
						{
							errores.add("rango consulta mayor", new ActionMessage("errors.rangoMayorMeses", "para consultar", "SEIS (6)"));
							this.errores=true;
						}
					}
					
					if(errores.isEmpty())
					{	
						double numeroFacturasEncontradas= ConsultaFacturas.cantidadRegistrosBusqueda(this.getFacturaInicial(), this.getFacturaFinal(), this.getFechaElaboracionInicial(), this.getFechaElaboracionFinal(), this.getPropiedadTempI(), this.getPropiedadTempViaIngreso(), this.getTipoPaciente(), this.getPropiedadTempEFactura(), this.getPropiedadTempEPaciente(), usuario, this.getCodigoCentroAtencion(), this.getEntidadSubcontratada(), this.getEmpresaInstitucion());
						
						if(numeroFacturasEncontradas>200000)
						{
							errores.add("", new ActionMessage("error.errorEnBlanco", "El resultado de la consulta supera los 200.000 registros, por favor seleccionar un rango menor en los filtros"));
							this.errores=true;
						}
					}
				}
			}	
		}
		if(errores.isEmpty())
		{
			this.errores = false;
		}
		return errores;
	}


	public boolean isImprimirAnexo() {
		return imprimirAnexo;
	}


	public void setImprimirAnexo(boolean imprimirAnexo) {
		this.imprimirAnexo = imprimirAnexo;
	}


	public String getTipoAnexo() {
		return tipoAnexo;
	}


	public void setTipoAnexo(String tipoAnexo) {
		this.tipoAnexo = tipoAnexo;
	}


	/**
	 * @return Returns the codigoCentroAtencion.
	 */
	public int getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}


	/**
	 * @param codigoCentroAtencion The codigoCentroAtencion to set.
	 */
	public void setCodigoCentroAtencion(int codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}


	public double getEmpresaInstitucion() {
		return empresaInstitucion;
	}


	public void setEmpresaInstitucion(double empresaInstitucion) {
		this.empresaInstitucion = empresaInstitucion;
	}


	public String getCodigoFactura() {
		return codigoFactura;
	}


	public void setCodigoFactura(String codigoFactura) {
		this.codigoFactura = codigoFactura;
	}


	/**
	 * @return the entidadesSubcontratadasMap
	 */
	public HashMap getEntidadesSubcontratadasMap() {
		return entidadesSubcontratadasMap;
	}


	/**
	 * @param entidadesSubcontratadasMap the entidadesSubcontratadasMap to set
	 */
	public void setEntidadesSubcontratadasMap(HashMap entidadesSubcontratadasMap) {
		this.entidadesSubcontratadasMap = entidadesSubcontratadasMap;
	}


	/**
	 * @return the entidadSubcontratada
	 */
	public double getEntidadSubcontratada() {
		return entidadSubcontratada;
	}


	/**
	 * @param entidadSubcontratada the entidadSubcontratada to set
	 */
	public void setEntidadSubcontratada(double entidadSubcontratada) {
		this.entidadSubcontratada = entidadSubcontratada;
	}


	/**
	 * @return the empresasInstitucionMap
	 */
	public HashMap getEmpresasInstitucionMap() {
		return empresasInstitucionMap;
	}


	/**
	 * @param empresasInstitucionMap the empresasInstitucionMap to set
	 */
	public void setEmpresasInstitucionMap(HashMap empresasInstitucionMap) {
		this.empresasInstitucionMap = empresasInstitucionMap;
	}


	/**
	 * @return the tipoReporte
	 */
	public String getTipoReporte() {
		return tipoReporte;
	}


	/**
	 * @param tipoReporte the tipoReporte to set
	 */
	public void setTipoReporte(String tipoReporte) {
		this.tipoReporte = tipoReporte;
	}


	/**
	 * @return the tipoSalida
	 */
	public String getTipoSalida() {
		return tipoSalida;
	}


	/**
	 * @param tipoSalida the tipoSalida to set
	 */
	public void setTipoSalida(String tipoSalida) {
		this.tipoSalida = tipoSalida;
	}


	/**
	 * @return the archivo
	 */
	public boolean isArchivo() {
		return archivo;
	}


	/**
	 * @param archivo the archivo to set
	 */
	public void setArchivo(boolean archivo) {
		this.archivo = archivo;
	}


	/**
	 * @return the mensaje
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}


	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}


	/**
	 * @return the pathArchivoTxt
	 */
	public String getPathArchivoTxt() {
		return pathArchivoTxt;
	}


	/**
	 * @param pathArchivoTxt the pathArchivoTxt to set
	 */
	public void setPathArchivoTxt(String pathArchivoTxt) {
		this.pathArchivoTxt = pathArchivoTxt;
	}


	/**
	 * @return the errores
	 */
	public boolean isErrores() {
		return errores;
	}


	/**
	 * @param errores the errores to set
	 */
	public void setErrores(boolean errores) {
		this.errores = errores;
	}

	/**
	 * @return the tipoPaciente
	 */
	public String getTipoPaciente() {
		return tipoPaciente;
	}


	/**
	 * @param tipoPaciente the tipoPaciente to set
	 */
	public void setTipoPaciente(String tipoPaciente) {
		this.tipoPaciente = tipoPaciente;
	}

	/**
	 * @return
	 */
	public boolean isZip() {
		return zip;
	}

	/**
	 * @param zip
	 */
	public void setZip(boolean zip) {
		this.zip = zip;
	}


	public String getTipoImpresion() {
		return tipoImpresion;
	}


	public void setTipoImpresion(String tipoImpresion) {
		this.tipoImpresion = tipoImpresion;
	}


	public String getAnexoC() {
		return anexoC;
	}


	public void setAnexoC(String anexoC) {
		this.anexoC = anexoC;
	}

	public String getFormatoConvenio() {
		return formatoConvenio;
	}

	public void setFormatoConvenio(String formatoConvenio) {
		this.formatoConvenio = formatoConvenio;
	}

	/**
	 * @return
	 */
	public String getFormatoImpresionVenezuela() {
		return formatoImpresionVenezuela;
	}

	/**
	 * @param formatoImpresionVenezuela
	 */
	public void setFormatoImpresionVenezuela(String formatoImpresionVenezuela) {
		this.formatoImpresionVenezuela = formatoImpresionVenezuela;
	}

	/**
	 * @return the numeroAutorizacion
	 */
	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}

	/**
	 * @param numeroAutorizacion the numeroAutorizacion to set
	 */
	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}

	/**
	 * @return the nombreArchivoGenerado
	 */
	public String getNombreArchivoGenerado() {
		return nombreArchivoGenerado;
	}

	/**
	 * @param nombreArchivoGenerado the nombreArchivoGenerado to set
	 */
	public void setNombreArchivoGenerado(String nombreArchivoGenerado) {
		this.nombreArchivoGenerado = nombreArchivoGenerado;
	}

	/**
	 * @return the permisoImprimirOriginalFactura
	 */
	public boolean isTienePermisoImprimirOriginalFactura() {
		return tienePermisoImprimirOriginalFactura;
	}

	/**
	 * @param permisoImprimirOriginalFactura the permisoImprimirOriginalFactura to set
	 */
	public void setTienePermisoImprimirOriginalFactura(
			boolean permisoImprimirOriginalFactura) {
		this.tienePermisoImprimirOriginalFactura = permisoImprimirOriginalFactura;
	}

	/**
	 * @return the tipoImpresionPermiso
	 */
	public Integer getTipoImpresionPermiso() {
		return tipoImpresionPermiso;
	}

	/**
	 * @param tipoImpresionPermiso the tipoImpresionPermiso to set
	 */
	public void setTipoImpresionPermiso(Integer tipoImpresionPermiso) {
		this.tipoImpresionPermiso = tipoImpresionPermiso;
	}

	/**
	 * @return the nombreArchivoGeneradoOriginal
	 */
	public String getNombreArchivoGeneradoOriginal() {
		return nombreArchivoGeneradoOriginal;
	}

	/**
	 * @param nombreArchivoGeneradoOriginal the nombreArchivoGeneradoOriginal to set
	 */
	public void setNombreArchivoGeneradoOriginal(
			String nombreArchivoGeneradoOriginal) {
		this.nombreArchivoGeneradoOriginal = nombreArchivoGeneradoOriginal;
	}



	
	
	
	
}