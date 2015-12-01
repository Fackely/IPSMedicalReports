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
import java.util.HashMap;

import util.ConstantesBD;
import util.UtilidadBD;
import com.princetonsa.dao.AjustesEmpresaDao;
import com.princetonsa.dao.DaoFactory;

/**
 * CLASE PARA MANEJAR LOS AJUSTES ESPCÍFICOS DE UN SERVICIO EN UNA FACTURA.
 *
 * @version 1.0, Julio 22 / 2005	
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 */
public class AjustesDetalleFacturaEmpresa
{
	/**
	 * DAO utilizado por el objeto parra acceder a la fuente de datos
	 */
	private static AjustesEmpresaDao ajustesDao = null;
    
	/**
     * Codigo del ajuste.
     */
	private double codigo;
	
	/**
	 * 
	 */
	private int codigopk;
   
	/**
	 * @return the codigopk
	 */
	public int getCodigopk() {
		return codigopk;
	}


	/**
	 * @param codigopk the codigopk to set
	 */
	public void setCodigopk(int codigopk) {
		this.codigopk = codigopk;
	}


	/**
	 * Codigo de la factura a la que se le esta aplicando el ajuste.
	 */
	private int factura;
	
	/**
	 * Codigo del detalle de la factura solicitud(Alli encontramos los servicios y los articulos de una factura).
	 */
	private int detFactSolicitud;
	
	/**
	 * Codigo Pool del medico que responde.
	 */
	private int codigoPool;
	
	/**
	 * Nombre Pool del Medico que Responde
	 */
	private String nombrePool;
	
	/**
	 * Codigo del medico que responde.
	 */
	private int codigoMedicoResponde;
	
	/**
	 * Metodo de ajuste que se utiliza(espcifico para el ajuste a nivel de servicios y articulos).
	 */
	private String metodoAjuste;
	
	/**
	 * Variable para manejar el valor total del ajuste para el servicio
	 */
	private double valorAjuste;
	
	/**
	 * Variable para manejar el valor del ajuste para el pool del servicio
	 */
	private double valorAjustePool;
	
	/**
	 * Variable para manejar el valor del ajuste para la institucion
	 */
	private double valorAjusteInstitucion;
	
	/**
	 * Variable para manejar el concepto del ajuste.
	 */
	private String conceptoAjuste;
	
	/**
	 * Variable para manejar la institucion
	 */
	private int institucion;
	
	private int numeroSolicitud;
	private int codigoServicio;
	private String nombreServicio;
	private int codigoMedico;
	private String nombreMedico;
	private double saldo;
    
    private HashMap asociosServicio;
    
    /**
     * 
     */
    private HashMap detallePaquete;
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
	
	
	public AjustesDetalleFacturaEmpresa()
	{
		this.reset();
		this.init (System.getProperty("TIPOBD"));
	}
	/**
	 * 
	 */
	private void reset() 
	{
		codigo=ConstantesBD.codigoNuncaValido;
		codigopk=ConstantesBD.codigoNuncaValido;
		factura=ConstantesBD.codigoNuncaValido;
		detFactSolicitud=ConstantesBD.codigoNuncaValido;
		codigoPool=ConstantesBD.codigoNuncaValido;
		codigoMedicoResponde=ConstantesBD.codigoNuncaValido;
		metodoAjuste="";
		valorAjuste=ConstantesBD.codigoNuncaValido;
		valorAjustePool=ConstantesBD.codigoNuncaValido;
		valorAjusteInstitucion=ConstantesBD.codigoNuncaValido;
		conceptoAjuste="";
		institucion=ConstantesBD.codigoNuncaValido;
        this.asociosServicio=new HashMap();
        this.detallePaquete=new HashMap();
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
	 * @return Returns the codigoMedicoResponde.
	 */
	public int getCodigoMedicoResponde() {
		return codigoMedicoResponde;
	}
	/**
	 * @param codigoMedicoResponde The codigoMedicoResponde to set.
	 */
	public void setCodigoMedicoResponde(int codigoMedicoResponde) {
		this.codigoMedicoResponde = codigoMedicoResponde;
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
	 * @return Returns the detFactSolicitud.
	 */
	public int getDetFactSolicitud() {
		return detFactSolicitud;
	}
	/**
	 * @param detFactSolicitud The detFactSolicitud to set.
	 */
	public void setDetFactSolicitud(int detFactSolicitud) {
		this.detFactSolicitud = detFactSolicitud;
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
	 * @return Returns the pool.
	 */
	public int getCodigoPool() {
		return codigoPool;
	}
	/**
	 * @param pool The pool to set.
	 */
	public void setCodigoPool(int pool) {
		this.codigoPool = pool;
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
	 * @return Returns the valorAjusteInstitucion.
	 */
	public double getValorAjusteInstitucion() {
		return valorAjusteInstitucion;
	}
	/**
	 * @param valorAjusteInstitucion The valorAjusteInstitucion to set.
	 */
	public void setValorAjusteInstitucion(double valorAjusteInstitucion) {
		this.valorAjusteInstitucion = valorAjusteInstitucion;
	}
	/**
	 * @return Returns the valorAjustePool.
	 */
	public double getValorAjustePool() {
		return valorAjustePool;
	}
	/**
	 * @param valorAjustePool The valorAjustePool to set.
	 */
	public void setValorAjustePool(double valorAjustePool) {
		this.valorAjustePool = valorAjustePool;
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
	 * @param con
	 * @param codigoFactura
	 * @param servicios
	 */
	public ResultSetDecorator cargarDetalleFactura(Connection con, int codigoFactura, boolean servicios) 
	{
		return ajustesDao.cargarDetalleFactura(con,codigoFactura,servicios);
	}
	/**
	 * @return Returns the codigoMedico.
	 */
	public int getCodigoMedico() {
		return codigoMedico;
	}
	/**
	 * @param codigoMedico The codigoMedico to set.
	 */
	public void setCodigoMedico(int codigoMedico) {
		this.codigoMedico = codigoMedico;
	}
	/**
	 * @return Returns the codigoServicio.
	 */
	public int getCodigoServicio() {
		return codigoServicio;
	}
	/**
	 * @param codigoServicio The codigoServicio to set.
	 */
	public void setCodigoServicio(int codigoServicio) {
		this.codigoServicio = codigoServicio;
	}
	/**
	 * @return Returns the nombreMedico.
	 */
	public String getNombreMedico() {
		return nombreMedico;
	}
	/**
	 * @param nombreMedico The nombreMedico to set.
	 */
	public void setNombreMedico(String nombreMedico) {
		this.nombreMedico = nombreMedico;
	}
	/**
	 * @return Returns the nombrePool.
	 */
	public String getNombrePool() {
		return nombrePool;
	}
	/**
	 * @param nombrePool The nombrePool to set.
	 */
	public void setNombrePool(String nombrePool) {
		this.nombrePool = nombrePool;
	}
	/**
	 * @return Returns the nombreServicio.
	 */
	public String getNombreServicio() {
		return nombreServicio;
	}
	/**
	 * @param nombreServicio The nombreServicio to set.
	 */
	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}
	/**
	 * @return Returns the numeroSolicitud.
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}
	/**
	 * @param numeroSolicitud The numeroSolicitud to set.
	 */
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}
	/**
	 * @return Returns the saldo.
	 */
	public double getSaldo() {
		return saldo;
	}
	/**
	 * @param saldo The saldo to set.
	 */
	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}


	/**
	 * @param con
	 * @return
	 */
	public boolean insertarAjuste(Connection con) 
	{
		this.codigopk=ajustesDao.ingresarAjustedDetalleFactura(con,this.codigo,this.factura,this.detFactSolicitud,this.codigoPool,this.codigoMedicoResponde,this.metodoAjuste,this.valorAjuste,this.valorAjustePool,this.valorAjusteInstitucion,this.conceptoAjuste,this.institucion);
		return codigopk>0;
	}


	/**
	 * @param con
	 * @return
	 */
	public boolean existeAjuste(Connection con)
	{
		String cadena="SELECT codigo from  ajus_det_fact_empresa where codigo="+this.codigo+" and factura="+this.factura;
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
	public boolean updateAjuste(Connection con) 
	{
		return (ajustesDao.updateAjustedDetalleFactura(con,this.codigopk,this.codigo,this.factura,this.detFactSolicitud,this.metodoAjuste,this.valorAjuste,this.valorAjustePool,this.valorAjusteInstitucion,this.conceptoAjuste,this.institucion)>0);
	}


	/**
	 * @param con
	 * @param codigo2
	 * @param codigoFactura
	 * @param servicio
	 * @return
	 */
	public ResultSetDecorator cargarDetalleAjusteFactura(Connection con, double codigoAjuste, int codigoFactura, boolean servicio) 
	{
		return ajustesDao.cargarDetalleAjusteFactura(con,codigoAjuste,codigoFactura,servicio);
	}


	/**
	 * @param con
	 * @param codigoFactura
	 * @param i
	 * @return
	 */
	public ResultSetDecorator cargarDetalleFacturasArticuloAgrupado(Connection con, int codigoFactura, int codigoArticuloAgrupacion) 
	{
		return ajustesDao.cargarDetalleFacturasArticuloAgrupado(con,codigoFactura,codigoArticuloAgrupacion);
	}


	/**
	 * @param con
	 * @return
	 */
	public boolean eliminarAjusteServicio(Connection con) 
	{
		return (ajustesDao.eliminarAjusteServicio(con,this.codigo,this.factura)>0);
	}


	/**
	 * @param con
	 * @param codigoFactura
	 * @param campoBusqueda
	 * @param valorCampoBusqueda
	 * @param servicio
	 * @return
	 */
	public ResultSetDecorator cargarDetalleFacturaAvanzada(Connection con, int codigoFactura, String campoBusqueda, String valorCampoBusqueda, boolean servicios) 
	{
		return ajustesDao.cargarDetalleFacturaAvanzada(con,codigoFactura,campoBusqueda,valorCampoBusqueda,servicios);
	}


    public HashMap cargarAsociosServiciosCirugia(Connection con)
    {
        this.asociosServicio=ajustesDao.cargarAsociosServiciosCirugia(con,detFactSolicitud);
        this.asociosServicio.put("institucion",this.institucion+"");
        this.asociosServicio.put("codigoajuste",this.codigo+"");
        this.asociosServicio.put("factura", this.factura+"");
        this.asociosServicio.put("codigopkserart", this.codigopk);
        return (HashMap)this.asociosServicio;
    }

    public HashMap cargarDetallePaquete(Connection con)
    {
        this.detallePaquete=ajustesDao.cargarDetallePaquete(con,detFactSolicitud);
        this.detallePaquete.put("institucion",this.institucion+"");
        this.detallePaquete.put("codigoajuste",this.codigo+"");
        this.detallePaquete.put("factura", this.factura+"");
        this.asociosServicio.put("codigopkserart", this.codigopk);
        return (HashMap)this.detallePaquete;
    }

    /**
     * @return Returns the asociosServicio.
     */
    public HashMap getAsociosServicio()
    {
        return asociosServicio;
    }


    /**
     * @param asociosServicio The asociosServicio to set.
     */
    public void setAsociosServicio(HashMap asociosServicio)
    {
        this.asociosServicio = asociosServicio;
    }


    /**
     * 
     * @param con
     * @return
     */
    public boolean insertarAsociosServicio(Connection con)
    {
        return ajustesDao.insertarAsociosServicio(con,this.asociosServicio);
    }
    
    /**
     * 
     * @param con
     * @return
     */
    public boolean insertarDetallePaquetes(Connection con)
    {
    	return ajustesDao.insertarDetallePaquetes(con,this.detallePaquete);
    }


    public boolean eliminarAjusteServicioAsocios(Connection con)
    {
        return (ajustesDao.eliminarAjusteServicioAsocios(con,this.codigopk,this.codigo,this.factura,this.detFactSolicitud)>0);
    }
    
    /**
     * 
     * @param con
     * @return
     */
    public boolean eliminarAjusteDetallePaquetes(Connection con)
    {
    	return (ajustesDao.eliminarAjusteDetallePaquetes(con,this.codigopk,this.codigo,this.factura,this.detFactSolicitud)>0);
    }


    /**
     * Metodo que actualiza el valor del ajuste institucion y pool para los
     * servicios de cirugia, ya que debe tomar la sumatoria del los servicios de los asocios.
     * @param con
     */
    public boolean updateValAjusteInstPoolSerCx(Connection con)
    {
        return (ajustesDao.updateValAjusteInstPoolSerCx(con,this.codigopk,this.codigo,this.factura,this.detFactSolicitud)>0);
    }


    public HashMap consultarServiciosAsociosAjustes(Connection con)
    {
        return (ajustesDao.consultarServiciosAsociosAjustes(con,this.codigopk,this.codigo,this.factura,this.detFactSolicitud));
    }


	/**
	 * @return the detallePaquete
	 */
	public HashMap getDetallePaquete() {
		return detallePaquete;
	}


	/**
	 * @param detallePaquete the detallePaquete to set
	 */
	public void setDetallePaquete(HashMap detallePaquete) {
		this.detallePaquete = detallePaquete;
	}
}
