/*
 * @(#)MovimientoFacturas.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
package com.princetonsa.mundo.cartera;

import java.sql.Connection;
import java.util.Collection;

import org.apache.log4j.Logger;

import util.InfoDatosInt;
import util.Rangos;
import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.MovimientoFacturasDao;

/**
 * Clase para el manejo del registro de cartera tanto
 * para empresas como para particulares
 * @version 1.0, Abril 07, 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class MovimientoFacturas 
{
	/**
	 * DAO utilizado por el objeto parra acceder a la fuente de datos
	 */
	private static MovimientoFacturasDao movimientoFacturasDao = null;

    /**
     * cod - desc (razon social)
     */
    private InfoDatosInt empresa;
    
    /**
     * cod - nombre 
     */
    private InfoDatosInt convenio;
    
    /**
     * Rangos de Factura Inicial - Factura Final
     */
    private Rangos facturaRangos;
    
    /**
     * Rangos de Fecha inicial factura - Fecha Final factura
     */
    private Rangos fechaFacturaRangos;
    
    /**
     * Rangos del valor Factura Inicial - valor Factura final
     */
    private Rangos valorFacturaRangos;
    
    /**
     * Rangos numero cuenta cobro
     */
    private Rangos numeroCuentaCobroRangos;
    
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(MovimientoFacturas.class);

	
	/**
     * cod - nombre centro atencion
     */
    private InfoDatosInt centroAtencion;
    
    /**
     * 
     */
    private String empresaInstitucion;
	
	/**
	 * Constructor Vacio
	 *
	 */
	public MovimientoFacturas()
	{
	    reset();
		this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * resetea los valores
	 *
	 */
	public void reset()
	{
	    this.empresa= new InfoDatosInt();
	    this.convenio= new InfoDatosInt();
	    this.facturaRangos= new Rangos();
	    this.fechaFacturaRangos= new Rangos();
	    this.valorFacturaRangos= new Rangos();
	    this.numeroCuentaCobroRangos= new Rangos();
	    this.centroAtencion= new InfoDatosInt();
	    this.empresaInstitucion="";
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
			boolean wasInited = false;
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
	
			if (myFactory != null)
			{
				movimientoFacturasDao = myFactory.getMovimientoFacturasDao();
				wasInited = (movimientoFacturasDao != null);
			}
			return wasInited;
	}
	
	/**
	 * Método que contiene los resultados de la búsqueda del movimiento de facturas
	 * según los criterios dados en la búsqueda avanzada. 
	 * @param con
	 * @return
	 */
	public Collection resultadoBusquedaAvanzada(Connection con, int codigoInstitucion)
	{
		movimientoFacturasDao= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMovimientoFacturasDao();
		Collection coleccion=null;
		try
		{	
				coleccion=UtilidadBD.resultSet2Collection(movimientoFacturasDao.busquedaAvanzada(		con, 
																										this.empresa, 
																										this.convenio, 
																										this.facturaRangos, 
																										this.fechaFacturaRangos, 
																										this.valorFacturaRangos, 
																										this.numeroCuentaCobroRangos,
																										codigoInstitucion,
																										this.getCodigoCentroAtencion(),this.empresaInstitucion));
		}
		catch(Exception e)
		{
			logger.warn("Error mundo movimiento facturas " +e.toString());
			coleccion=null;
		}
		return coleccion;		
	}
	
    /**
     * @return Returns the convenio.
     */
    public InfoDatosInt getConvenio() {
        return convenio;
    }
    
    /**
     * Returna  la desc del convenio
     * @return
     */
    public String getNombreConvenio(){
        return this.convenio.getNombre();
    }
 
    /**
     * Retorna el cod del convenio
     * @return
     */
    public int getCodigoConvenio(){
        return this.convenio.getCodigo();
    }
    
    /**
     * @param convenio The convenio to set.
     */
    public void setConvenio(InfoDatosInt convenio) {
        this.convenio = convenio;
    }
    
    /**
     * Asigna el cod del convenio
     * @param codigo
     */
    public void setCodigoConvenio(int codigo){
        this.convenio.setCodigo(codigo);
    }
    
    /**
     * Asigna el nombre del convenio
     * @param nom
     */
    public void setNombreConvenio(String nom){
        this.convenio.setNombre(nom);
    }
    
    /**
     * @return Returns the empresa.
     */
    public InfoDatosInt getEmpresa() {
        return empresa;
    }
    
    /**
     * Retorna el codigo de la empresa
     * @return
     */
    public int getCodigoEmpresa(){
        return empresa.getCodigo();
    }
    
    /**
     * Retorna la descripcion de la empresa
     * @return
     */
    public String getDescripcionEmpresa(){
        return empresa.getDescripcion();
    }
    
    /**
     * @param empresa The empresa to set.
     */
    public void setEmpresa(InfoDatosInt empresa) {
        this.empresa = empresa;
    }
    
    /**
     * Asigna el codigo de la empresa
     * @param cod
     */
    public void setCodigoEmpresa(int cod){
        this.empresa.setCodigo(cod);
    }
    
    public void setDescripcionEmpresa(String desc){
        this.empresa.setDescripcion(desc);
    }
    
    /**
     * @return Returns the facturaRangos.
     */
    public Rangos getFacturaRangos() {
        return facturaRangos;
    }
    
    /**
     * Retorna el rangoInicial de la factura
     * @return
     */
    public String getRangoInicialFactura(){
        return facturaRangos.getRangoInicial();
    }
    
    /**
     * Retorna el rangoFinal de la factura
     * @return
     */
    public String getRangoFinalFactura(){
        return facturaRangos.getRangoFinal();
    }
    
    /**
     * Retorna el codigo del estado de la factura
     * @return
     */
    public int getCodigoEstadoFactura(){
        return facturaRangos.getCodigoEstado();
    }
    
    /**
     * Retorna la desc del estado de la factura
     * @return
     */
    public String getDescipcionEstadoFactura(){
        return facturaRangos.getDescripcionEstado();
    }
    
    /**
     * @param facturaRangos The facturaRangos to set.
     */
    public void setFacturaRangos(Rangos facturaRangos) {
        this.facturaRangos = facturaRangos;
    }
    
    /**
     * Asigna el rango inicial de la factura
     * @param ini
     */
    public void setRangoInicialFactura(String ini){
        this.facturaRangos.setRangoInicial(ini);
    }
    
    /**
     * Asigna el rango finall de la factura
     * @param fin
     */
    public void setRangoFinalFactura(String fin){
        this.facturaRangos.setRangoFinal(fin);
    }
    
    /**
     * Asigna el cod del estado de la factura
     * @param cod
     */
    public void setCodigoEstadoFactura(int cod){
        this.facturaRangos.setCodigoEstado(cod);
    }
    
    /**
     * Asigna la desc del estado de la factura
     * @param cod
     */
    public void setDescripcionEstadoFactura(String desc){
        this.facturaRangos.setDescripcionEstado(desc);
    }
    
    /**
     * @return Returns the fechaFacturaRangos.
     */
    public Rangos getFechaFacturaRangos() {
        return fechaFacturaRangos;
    }
    
    /**
     * Retorna rango inicial de la fecha
     * @return
     */
    public String getRangoInicialFechaFactura(){
        return fechaFacturaRangos.getRangoInicial();
    }
    
    /**
     * Retorna rango final de la fecha
     * @return
     */
    public String getRangoFinalFechaFactura(){
        return fechaFacturaRangos.getRangoFinal();
    }
    
    /**
     * @param fechaFacturaRangos The fechaFacturaRangos to set.
     */
    public void setFechaFacturaRangos(Rangos fechaFacturaRangos) {
        this.fechaFacturaRangos = fechaFacturaRangos;
    }
    
    /**
     * Asigna el rangoInicial de la fecha de factura
     * @param ini
     */
    public void setRangoInicialFechaFactura(String ini){
        this.fechaFacturaRangos.setRangoInicial(ini);
    }
    
    /**
     * Asigna el rangoFinal de la fecha de factura
     * @param ini
     */
    public void setRangoFinalFechaFactura(String fin){
        this.fechaFacturaRangos.setRangoFinal(fin);
    }
    
    /**
     * @return Returns the numeroCuentaCobroRangos.
     */
    public Rangos getNumeroCuentaCobroRangos() {
        return numeroCuentaCobroRangos;
    }
    
    /**
     * Retorna rango inicial del numero de la cuenta de cobro
     * @return
     */
    public String getRangoInicialNumeroCuentaCobro(){
        return numeroCuentaCobroRangos.getRangoInicial();
    }
    
    /**
     * Retorna rango final del numero de cuenta de cobro 
     * @return
     */
    public String getRangoFinalNumeroCuentaCobro(){
        return numeroCuentaCobroRangos.getRangoFinal();
    }
    
    /**
     * @param numeroCuentaCobroRangos The numeroCuentaCobroRangos to set.
     */
    public void setNumeroCuentaCobroRangos(Rangos numeroCuentaCobroRangos) {
        this.numeroCuentaCobroRangos = numeroCuentaCobroRangos;
    }
    
    /**
     * Asigna el rangoInicial de NumeroCuentaCobro
     * @param ini
     */
    public void setRangoInicialNumeroCuentaCobro(String ini){
        this.numeroCuentaCobroRangos.setRangoInicial(ini);
    }
    
    /**
     * Asigna el rangoFinal de NumeroCuentaCobro
     * @param ini
     */
    public void setRangoFinalNumeroCuentaCobro(String fin){
        this.numeroCuentaCobroRangos.setRangoFinal(fin);
    }
    
    /**
     * @return Returns the valorFacturaRangos.
     */
    public Rangos getValorFacturaRangos() {
        return valorFacturaRangos;
    }
    
    /**
     * Retorna rango inicial ValorFactura
     * @return
     */
    public String getRangoInicialValorFactura(){
        return valorFacturaRangos.getRangoInicial();
    }
    
    /**
     * Retorna rango final ValorFactura
     * @return
     */
    public String getRangoFinalValorFactura(){
        return valorFacturaRangos.getRangoFinal();
    }

	/**
     * @param valorFacturaRangos The valorFacturaRangos to set.
     */
    public void setValorFacturaRangos(Rangos valorFacturaRangos) {
        this.valorFacturaRangos = valorFacturaRangos;
    }
    
    /**
     * Asigna el rangoInicial de ValorFactura
     * @param ini
     */
    public void setRangoInicialValorFactura(String ini){
        this.valorFacturaRangos.setRangoInicial(ini);
    }
    
    /**
     * Asigna el rangoFinal de ValorFactura
     * @param ini
     */
    public void setRangoFinalValorFactura(String fin){
        this.valorFacturaRangos.setRangoFinal(fin);
    }
    
    /**
	 * @return Returns the centroAtencion.
	 */
	public String getNombreCentroAtencion() {
		return centroAtencion.getNombre();
	}

	/**
	 * @param centroAtencion The centroAtencion to set.
	 */
	public void setNombreCentroAtencion(String nom) {
		this.centroAtencion.setNombre(nom);
	}
    
	/**
	 * @return Returns the centroAtencion.
	 */
	public int getCodigoCentroAtencion() {
		return centroAtencion.getCodigo();
	}

	/**
	 * @param centroAtencion The centroAtencion to set.
	 */
	public void setCodigoCentroAtencion(int cod) {
		this.centroAtencion.setCodigo(cod);
	}

	
	
    /**
	 * @return Returns the centroAtencion.
	 */
	public InfoDatosInt getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion The centroAtencion to set.
	 */
	public void setCentroAtencion(InfoDatosInt centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public String getEmpresaInstitucion() {
		return empresaInstitucion;
	}

	public void setEmpresaInstitucion(String empresaInstitucion) {
		this.empresaInstitucion = empresaInstitucion;
	}
    
}
