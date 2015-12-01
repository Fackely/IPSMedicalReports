/*
 * Created on 22/07/2005
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.mundo.cartera;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadBD;
import com.princetonsa.dao.AjustesEmpresaDao;
import com.princetonsa.dao.DaoFactory;

/**
 * CLASE PARA MANEJAR LOS AJUSTES ESPCÍFICOS DE UNA FACTURA.
 *
 * @version 1.0, Julio 22 / 2005	
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 */
public class AjustesFacturaEmpresa 
{
	/**
	 * DAO utilizado por el objeto parra acceder a la fuente de datos
	 */
	private static AjustesEmpresaDao ajustesDao = null;
    
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(AjustesFacturaEmpresa.class);

	/**
	 * Collection que contien el detalle de los ajustes de las facturas. 
	 */
	private Collection colDetalleFacturasAjustes;
	
	/**
	 * Objeto para manejar los ajustes de un servicio de una factura.
	 */
	private AjustesDetalleFacturaEmpresa ajustesDetalle;
	
	/**
     * Codigo del ajuste.
     */
	private double codigo;
   
	/**
	 * Codigo de la factura a la que se le esta aplicando el ajuste.
	 */
	private int factura;
	
	/**
	 * Metodo de ajuste que se utiliza(espcifico para el ajuste a nivel de servicios y articulos).
	 */
	private String metodoAjuste;
	
	/**
	 * Variable para manejar el valor total del ajuste para el servicio
	 */
	private double valorAjuste;
	
	/**
	 * Variable para manejar el concepto del ajuste.
	 */
	private String conceptoAjuste;
	
	/**
	 * Variable para el manejo de la institucion
	 */
	private int institucion;
	
	/**
	 * Variable para manejar el codigo de la cuenta de cobro a la que pertenece una factura
	 */
	private double cuentaCobro;
	
	private InfoDatosInt convenio;
	
	private int consecutivoFactura;
	
	
	/**
	 * Las manejo como String para evitar que java las pase a exponencial.
	 */
	private double ajusteDebito;
	private double ajusteCredito;
	private double valorPagos;
	private double saldoFactura;
    private double totalFactura;
    
    /**
     * 
     */
    private int codigoCentroAtencion;
    
    /**
     * 
     */
    private String nombreCentroAtencion;
	
	/**
	 * Variable que me indica si una factura es del sistema.
	 */
	private boolean facturaSistema;
	    
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
				ajustesDao = myFactory.getAjustesEmpresaDao();
				wasInited = (ajustesDao != null);
			}
			return wasInited;
	}
	
	
		
	
	public AjustesFacturaEmpresa()
	{
		this.reset();
		this.init (System.getProperty("TIPOBD"));
	}

	/**
	 * 
	 */
	private void reset() 
	{
		this.colDetalleFacturasAjustes=new ArrayList();
		this.ajustesDetalle=new AjustesDetalleFacturaEmpresa();
		this.codigo=ConstantesBD.codigoNuncaValido;
		this.factura=ConstantesBD.codigoNuncaValido;
		this.metodoAjuste="";
		this.valorAjuste=ConstantesBD.codigoNuncaValido;
		this.conceptoAjuste="";
		this.institucion=ConstantesBD.codigoNuncaValido;	
		this.cuentaCobro=ConstantesBD.codigoNuncaValido;
		this.convenio=new InfoDatosInt();
		this.consecutivoFactura=ConstantesBD.codigoNuncaValido;
		this.codigoCentroAtencion=ConstantesBD.codigoNuncaValido;
		this.nombreCentroAtencion="";
	}




	/**
	 * @return Returns the ajustesDetalle.
	 */
	public AjustesDetalleFacturaEmpresa getAjustesDetalle() {
		return ajustesDetalle;
	}
	/**
	 * @param ajustesDetalle The ajustesDetalle to set.
	 */
	public void setAjustesDetalle(AjustesDetalleFacturaEmpresa ajustesDetalle) {
		this.ajustesDetalle = ajustesDetalle;
	}
	/**
	 * @return Returns the codigo.
	 */
	public double getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo The codigo to set.
	 */
	public void setCodigo(double codigo) {
		this.codigo = codigo;
	}
	/**
	 * @return Returns the colDetalleFacturasAjustes.
	 */
	public Collection getColDetalleFacturasAjustes() {
		return colDetalleFacturasAjustes;
	}
	/**
	 * @param colDetalleFacturasAjustes The colDetalleFacturasAjustes to set.
	 */
	public void setColDetalleFacturasAjustes(
			Collection colDetalleFacturasAjustes) {
		this.colDetalleFacturasAjustes = colDetalleFacturasAjustes;
	}
	/**
	 * @return Returns the conceptoAjuste.
	 */
	public String getConceptoAjuste() {
		return conceptoAjuste;
	}
	/**
	 * @param conceptoAjuste The conceptoAjuste to set.
	 */
	public void setConceptoAjuste(String conceptoAjuste) {
		this.conceptoAjuste = conceptoAjuste;
	}
	/**
	 * @return Returns the factura.
	 */
	public int getFactura() {
		return factura;
	}
	/**
	 * @param factura The factura to set.
	 */
	public void setFactura(int factura) {
		this.factura = factura;
	}
	/**
	 * @return Returns the metodoAjuste.
	 */
	public String getMetodoAjuste() {
		return metodoAjuste;
	}
	/**
	 * @param metodoAjuste The metodoAjuste to set.
	 */
	public void setMetodoAjuste(String metodoAjuste) {
		this.metodoAjuste = metodoAjuste;
	}
	/**
	 * @return Returns the valorAjuste.
	 */
	public double getValorAjuste() {
		return valorAjuste;
	}
	/**
	 * @param valorAjuste The valorAjuste to set.
	 */
	public void setValorAjuste(double valorAjuste) {
		this.valorAjuste = valorAjuste;
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
	/**
	 * @return Returns the iteDetalleFacturasAjuste.
	 */
	public Iterator getIteDetalleFacturasAjuste() 
	{
		return this.colDetalleFacturasAjustes.iterator();
	}


	
	
	
	/**
	 * @param con
	 * @param codigoFactura
	 * @param castigoCartera
	 * @param ajustesCxCRadicada
	 */
	public boolean insertarAjusteFacturaEmpresa(Connection con) 
	{
		return (ajustesDao.ingresarAjustesFactura(con,this.codigo,this.factura,this.metodoAjuste,this.valorAjuste,this.conceptoAjuste,this.institucion)>0);
	}


	/**
	 * @param con
	 * @param codigoFactura
	 * @param castigoCartera
	 * @param ajustesCxCRadicada
	 * @param modificacion
	 * @param codigoAjuste
	 */
	public boolean cargarUnaFactura(Connection con, int codigoFactura, boolean castigoCartera, boolean ajustesCxCRadicada, boolean modificacion, double codigoAjuste) 
	{
		ResultSetDecorator rs=ajustesDao.cargarUnaFactura(con,codigoFactura,castigoCartera,ajustesCxCRadicada,modificacion,codigoAjuste);
		try
		{
			if(rs.next())
			{
				this.factura=codigoFactura;
				this.consecutivoFactura=rs.getInt("consecutivofactura");
				this.codigoCentroAtencion=rs.getInt("codigocentroatencion");
				this.nombreCentroAtencion=rs.getString("nombrecentroatencion");
				this.cuentaCobro=rs.getDouble("cuentacobro");
				this.convenio.setCodigo(rs.getInt("codigoconvenio"));
				this.convenio.setNombre(rs.getString("nombreconvenio"));
				this.ajusteCredito=rs.getDouble("ajustescredito");
				this.ajusteDebito=rs.getDouble("ajustesdebito");
				this.valorPagos=rs.getDouble("pagos");
				this.saldoFactura=rs.getDouble("saldofactura");
				this.facturaSistema=rs.getBoolean("facturasistema");
                this.totalFactura=rs.getDouble("totalfactura");
				return true;
			}
			else
			{
				logger.error("La consulta no arrojo resultados.");
			}
		}
		catch(SQLException e)
		{
			logger.error("Error(Exception) al obtener los datos del ResultSetDecorator y cargarlos en el Objeto.");
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * @return Returns the cuentaCobro.
	 */
	public double getCuentaCobro() {
		return cuentaCobro;
	}
	/**
	 * @param cuentaCobro The cuentaCobro to set.
	 */
	public void setCuentaCobro(double cuentaCobro) {
		this.cuentaCobro = cuentaCobro;
	}
	/**
	 * @return Returns the convenio.
	 */
	public InfoDatosInt getConvenio() {
		return convenio;
	}
	/**
	 * @param convenio The convenio to set.
	 */
	public void setConvenio(InfoDatosInt convenio) {
		this.convenio = convenio;
	}
	/**
	 * @return Returns the ajusteCredito.
	 */
	public double getAjusteCredito() {
		return ajusteCredito;
	}
	/**
	 * @param ajusteCredito The ajusteCredito to set.
	 */
	public void setAjusteCredito(double ajusteCredito) {
		this.ajusteCredito = ajusteCredito;
	}
	/**
	 * @return Returns the ajusteDebito.
	 */
	public double getAjusteDebito() {
		return ajusteDebito;
	}
	/**
	 * @param ajusteDebito The ajusteDebito to set.
	 */
	public void setAjusteDebito(double ajusteDebito) {
		this.ajusteDebito = ajusteDebito;
	}
	/**
	 * @return Returns the saldoFactura.
	 */
	public double getSaldoFactura() {
		return saldoFactura;
	}
	/**
	 * @param saldoFactura The saldoFactura to set.
	 */
	public void setSaldoFactura(double saldoFactura) {
		this.saldoFactura = saldoFactura;
	}
	/**
	 * @return Returns the valorPagos.
	 */
	public double getValorPagos() {
		return valorPagos;
	}
	/**
	 * @param valorPagos The valorPagos to set.
	 */
	public void setValorPagos(double valorPagos) {
		this.valorPagos = valorPagos;
	}
	/**
	 * @return Returns the facturaSistema.
	 */
	public boolean isFacturaSistema() {
		return facturaSistema;
	}
	/**
	 * @param facturaSistema The facturaSistema to set.
	 */
	public void setFacturaSistema(boolean facturaSistema) {
		this.facturaSistema = facturaSistema;
	}




	/**
	 * @param con
	 * @return
	 */
	public boolean existeAjuste(Connection con) 
	{
		String cadena="SELECT codigo,factura from  ajus_fact_empresa where codigo="+this.codigo+" and factura="+this.factura;
		ResultSetDecorator rs;
		try {
			rs = UtilidadBD.executeSqlGenerico(con,cadena);
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}




	/**
	 * @param con
	 * @return
	 */
	public boolean updateAjusteFacturaEmpresa(Connection con) 
	{
		return (ajustesDao.updateAjusteFacturaEmpresa(con,this.codigo,this.factura,this.metodoAjuste,this.valorAjuste,this.conceptoAjuste,this.institucion)>0);
	}
	
	public static boolean existeAjuste(Connection con,double codigo,int factura)
	{
		String cadena="SELECT codigo,factura from  ajus_fact_empresa where codigo="+codigo+" and factura="+factura;
		ResultSetDecorator rs;
		try {
			rs = UtilidadBD.executeSqlGenerico(con,cadena);
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	
	
	/**
	 * Metodo que mira si un campo específico de una ajuste de Factura que EXISTA fue modificado.
	 * @param con
	 * @param codigo
	 * @param factura
	 * @param campo
	 * @param valor
	 * @return
	 */
	public boolean existeModificacionAjusteFacturaCampo(Connection con,String campo,String valor)
	{
		String cadena="SELECT count(1) as registros from  ajus_fact_empresa where codigo="+this.codigo+" and factura="+this.factura+" and "+campo +" = '"+valor+"'";
		ResultSetDecorator rs;
		try 
		{
			rs = UtilidadBD.executeSqlGenerico(con,cadena);
			if(rs.next());
			{
				return rs.getInt("registros")<=0;
			}
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
	
	
	/**
	 * Metodo que mira si un campo específico de una ajuste de Factura que EXISTA fue modificado.
	 * @param con
	 * @param codigo
	 * @param factura
	 * @param campo
	 * @param valor
	 * @return
	 */
	public static boolean existeModificacionAjusteFacturaCampo(Connection con,double codigo,int factura,String campo,String valor)
	{
		String cadena="SELECT codigo,factura from  ajus_fact_empresa where codigo="+codigo+" and factura="+factura+" and "+campo +" = "+valor;
		ResultSetDecorator rs;
		try 
		{
			rs = UtilidadBD.executeSqlGenerico(con,cadena);
			return rs.next();
		} catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}




	/**
	 * @param con
	 * @param codFacturas
	 */
	public boolean eliminarAjusteFacturaNoEstan(Connection con, String codFacturas) 
	{
		return (ajustesDao.eliminarAjusteFacturaNoEstan(con,this.codigo,codFacturas)>0);
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
        this.totalFactura = totalFactura;
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
}
