/*
 * Creado   08/08/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.mundo.capitacion;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.capitacion.CierresCarteraCapitacionDao;

import util.UtilidadBD;
import util.UtilidadFecha;

/**
 * Clase para manejar los metodos para el proceso de 
 * cierre cartera capitacion, para confirmar en el sistema
 * los saldos iniciales ingresados y deacuerdo a la 
 * fecha de corte para el cierre permite identificar las
 * facturas a esa fecha de corte que ya tienen toda la 
 * gestión realizada correspondiente a radicación, pago,
 * ajustes ..., y genera un indicativo como cerradas.
 *
 * @version 1.0, 08/08/2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rios</a>
 */
public class CierresCarteraCapitacion 
{
	/**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(CierresCarteraCapitacion.class);	
	/**
	 * DAO de este objeto, para trabajar con cierre de saldos iniciales 
	 * cartera en la fuente de datos
	 */    
    private static CierresCarteraCapitacionDao cierreDao;
	
    /**
     * almacena el año de cierre
     */
    private String year;
    
    /**
     * almacena el mes de cierre
     */
    private String mes;
    
    /**
     * almacena las observaciones
     */
    private String observaciones;
    
   /**
	* almacena el codigo del cierre
	*/
    private String codCierre;
	   
   /**
    * fecha en que se genero el cierre
    */
    private String fechaGeneracion;
   
   /**
    * hora en que se genero el cierre
    */
    private String horaGeneracion;

    /**
     * usuario que realiza el cierre
     */
    private String usuario;
    
    /**
     * Metodo para inicializar los atributos de la clase.
     *
     */
    public void reset ()
    {
    	this.year="";
    	this.mes="";
    	this.observaciones="";
    	this.codCierre= "";
    	this.fechaGeneracion="";
    	this.horaGeneracion="";
    }
    
    /**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
		if ( cierreDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			cierreDao= myFactory.getCierresCarteraCapitacionDao();
			if( cierreDao!= null )
				return true;
		}

		return false;
	}
	
	/**
	 * Constructor
	 *
	 */
	public CierresCarteraCapitacion ()
	{
	    this.reset();
	    init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * metodo para cargar el resumen desde la
	 * BD.
	 * @param con Connection
	 */
	public void cargarResumenCierreCarteraCapitacion(Connection con, int codigoInstitucion, String tipoCierre)
	{	   
	    try 
	    {
		    ResultSetDecorator rs1=cierreDao.ultimoCodigoCierre(con, codigoInstitucion, tipoCierre);
	        if(rs1.next())
		    {
	            this.codCierre = rs1.getString("seq_cierres_saldos_cap");		    
		        ResultSetDecorator rs=cierreDao.cargarResumenCierreCarteraCapitacion(con,this.codCierre, codigoInstitucion);    
	            while (rs.next())
	            {
	            	this.year = rs.getInt("year")+"";
	            	this.mes = rs.getInt("mes")+"";
	            	this.fechaGeneracion = UtilidadFecha.conversionFormatoFechaAAp(rs.getDate("fecha_generacion")+"");
	            	this.horaGeneracion = rs.getString("hora_generacion");
	            	this.usuario = rs.getString("usuario");
	            	this.observaciones = rs.getString("observaciones");              
	            }
		    }
        } catch (SQLException e) 
        {           
            e.printStackTrace();
        }
	}

	/**
	 * metodo que verifica la existencia de cuentas de cobro radicadas dada una fecha y mes cierre
	 * @param yearCierre
	 * @param mesCierre
	 * @param codigoInstitucion
	 * @return
	 */
	public static boolean existenCxCCapitadasRadicadasDadaFecha( String yearCierre, String mesCierre, int codigoInstitucion )
	{
		Connection con= null;
		con=UtilidadBD.abrirConexion();
		boolean existe=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCierresCarteraCapitacionDao().existenCxCCapitadasRadicadasDadaFecha(con, yearCierre, mesCierre, codigoInstitucion);
		try 
		{
			UtilidadBD.cerrarConexion(con);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return existe;
	}
	
	/**
	 * metodo que evalua si existe o no cierre para una anio y fecha dados,
	 * si la fecha es vacia entoncees no le pone la restriccion
	 * @param con
	 * @param yearCierre
	 * @param tipoCierre
	 * @return
	 */
	public static boolean existeCierre(String yearCierre1, String tipoCierre1, int codigoInstitucion)
	{
		Connection con= null;
		con=UtilidadBD.abrirConexion();
		boolean existe=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCierresCarteraCapitacionDao().existeCierre(con, yearCierre1, tipoCierre1, codigoInstitucion);
		try 
		{
			UtilidadBD.cerrarConexion(con);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return existe;
	}
	
	/**
	 * metodo que obtiene el resumen de los valores de los cierres de cartera para un anio y tipo cierre dado
	 * @param yearCierre
	 * @param codigoInstitucion
	 * @param tipoCierre
	 * @return
	 */
	public static HashMap getResumenValoresCierresCartera(String yearCierre, int codigoInstitucion, String tipoCierre)
	{
		Connection con= null;
		con=UtilidadBD.abrirConexion();
		HashMap mapa=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCierresCarteraCapitacionDao().getResumenValoresCierresCartera(con,yearCierre, codigoInstitucion, tipoCierre);
		try 
		{
			UtilidadBD.cerrarConexion(con);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * metodo que obtiene los valores de los pagos - ajustes debito credito y valor cartera -  para el cierre anual
	 *  
	 * @param codigoInstitucion
	 * @param yearCierre
	 * @return
	 */
	public static HashMap getPagosAjustesValorCarteraXConvenioCierreAnual(int codigoInstitucion, String yearCierre)
	{
		Connection con= null;
		con=UtilidadBD.abrirConexion();
		HashMap mapa=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCierresCarteraCapitacionDao().getPagosAjustesValorCarteraXConvenioCierreAnual(con, codigoInstitucion, yearCierre);
		try 
		{
			UtilidadBD.cerrarConexion(con);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * Inserta el detalle del cierre cartera x convenio
	 * @param con
	 * @param codigoCierre
	 * @param codigoConvenio
	 * @param valorCartera
	 * @param valorAjusteDebito
	 * @param valorAjusteCredito
	 * @param valorPago
	 * @return
	 */
	public boolean insertDetalleCierreCarteraXConvenio(Connection con, String codigoCierre, int codigoConvenio, String valorCartera, String valorAjusteDebito, String valorAjusteCredito, String valorPago )
	{
		return cierreDao.insertDetalleCierreCarteraXConvenio(con, codigoCierre, codigoConvenio, valorCartera, valorAjusteDebito, valorAjusteCredito, valorPago);
	}
	
	/**
	 * metodo que obtiene los valores de los pagos - ajustes debito credito y valor cartera - para posteriormente insertarlos en el cierre
	 * (DEBE TENER LOS ATRIBUTOS YEAR - MES SET EN EL OBJETO) 
	 * @param con
	 * @param codigoInstitucion
	 * @param yearCierre
	 * @param mesCierre
	 * @return
	 */
	public HashMap getPagosAjustesValorCarteraXConvenio(Connection con, int codigoInstitucion)
	{
		return cierreDao.getPagosAjustesValorCarteraXConvenio(con, codigoInstitucion, this.year, this.mes);
	}
	
	/**
	 * metodo que inserta el cierre de cartera capitacion
	 * RETORNA el codigo insertado
	 * @param con
	 * @param codigoInstitucion
	 * @param yearCierre
	 * @param mesCierre
	 * @param observaciones
	 * @param tipoCierre
	 * @param loginUsuario
	 * @return
	 */
	public int insertCierreCarteraCapitacion(Connection con, int codigoInstitucion, String tipoCierre )
	{
		return cierreDao.insertCierreCarteraCapitacion(con, codigoInstitucion, this.year, this.mes, this.observaciones, tipoCierre, this.usuario );
	}
	
	/**
	 * metodo para evaluar si las CxC estan radicadas para un anio dado
	 * @param con
	 * @param yearCierre
	 * @return
	 */
	public static HashMap estanCxCRadicadasParaCierreAnual (String yearCierre, int codigoInstitucion)
	{
		Connection con= null;
		con=UtilidadBD.abrirConexion();
		HashMap mapa=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCierresCarteraCapitacionDao().estanCxCRadicadasParaCierreAnual(con, yearCierre, codigoInstitucion);
		try 
		{
			UtilidadBD.cerrarConexion(con);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	
	/**
	 * @return Returns the codCierre.
	 */
	public String getCodCierre() {
		return codCierre;
	}

	/**
	 * @param codCierre The codCierre to set.
	 */
	public void setCodCierre(String codCierre) {
		this.codCierre = codCierre;
	}

	/**
	 * @return Returns the fechaGeneracion.
	 */
	public String getFechaGeneracion() {
		return fechaGeneracion;
	}

	/**
	 * @param fechaGeneracion The fechaGeneracion to set.
	 */
	public void setFechaGeneracion(String fechaGeneracion) {
		this.fechaGeneracion = fechaGeneracion;
	}

	/**
	 * @return Returns the horaGeneracion.
	 */
	public String getHoraGeneracion() {
		return horaGeneracion;
	}

	/**
	 * @param horaGeneracion The horaGeneracion to set.
	 */
	public void setHoraGeneracion(String horaGeneracion) {
		this.horaGeneracion = horaGeneracion;
	}

	/**
	 * @return Returns the mes.
	 */
	public String getMes() {
		return mes;
	}

	/**
	 * @param mes The mes to set.
	 */
	public void setMes(String mes) {
		this.mes = mes;
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
	 * @return Returns the usuario.
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario The usuario to set.
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return Returns the year.
	 */
	public String getYear() {
		return year;
	}

	/**
	 * @param year The year to set.
	 */
	public void setYear(String year) {
		this.year = year;
	}
	
	
	

}
