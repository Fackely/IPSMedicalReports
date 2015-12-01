/*
 * @author artotor
 */

package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ImpresionFacturaDao;
import com.princetonsa.dao.sqlbase.SqlBaseImpresionFacturaDao;
import com.princetonsa.dto.facturacion.DtoDatosRepsonsableFactura;


/**
 * Clase creada para contener los diferentes metodos utilizado en la impresion de una factura, tanto el estatico, como el dinamico.
 * @author artotor
 *
 */
public class ImpresionFactura {
	
	/**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(ImpresionFactura.class);
	
	/**
	 * DAO de este objeto, para trabajar con Imprimir Factura
	 * en la fuente de datos
	 */    
    private static ImpresionFacturaDao impFacturaDao;

    
    
    /**
     * Método que limpia este objeto
     * 
     */
	public void reset()
    {
    }
    
	/**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
	    if ( impFacturaDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			impFacturaDao= myFactory.getImpresionFacturaDao();
			if( impFacturaDao!= null )
				return true;
		}
		return false;
	}

	
	/**
	 * 
	 *
	 */
	public ImpresionFactura()
	{
		this.reset();
		init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Metodo que realiza la consulta especifica para el encabezado de una impresion de factura en formato estatico.
	 * @param con
	 * @param codigoFactira
	 * @return
	 */
	public HashMap consultarSeccionPacienteFormatoEstatico(Connection con, String codigoFactira) 
	{
		HashMap temp=impFacturaDao.consultarSeccionPacienteFormatoEstatico(con,codigoFactira);
		return (HashMap)temp.clone();
	}
	
	/**
	 * Metodo que realiza la consulta especifica para el encabezado de una impresion de factura en formato Versalles.
	 * @param con
	 * @param codigoFactira
	 * @return
	 */
	public HashMap consultarSeccionPacienteFormatoVersalles(Connection con, String codigoFactira) 
	{
		HashMap temp=impFacturaDao.consultarSeccionPacienteFormatoVersalles(con,codigoFactira);
		return (HashMap)temp.clone();
	}

     /**
     * metodo para consultar la información de la institución
     * @param con Connection
     * @param codigoInstitucion int 
     * @return  HashMap
     */
    public HashMap consultarSeccionInstitucionFormatoEstatico( Connection con, int codigoInstitucion, String codigoFactura, double empresaInstitucion )
    {
        HashMap temp=impFacturaDao.consultarSeccionInstitucionFormatoEstatico(con, codigoInstitucion, codigoFactura, empresaInstitucion);
        return (HashMap)temp.clone();
    }
    
    /**
     * metodo para consultar la información de la institución para el formato de Impresion Versalles
     * @param con Connection
     * @param codigoInstitucion int 
     * @return  HashMap
     */
    public HashMap consultarSeccionInstitucionFormatoVersalles( Connection con, int codigoInstitucion, String codigoFactura, double empresaInstitucion )
    {
        HashMap temp=impFacturaDao.consultarSeccionInstitucionFormatoVersalles(con, codigoInstitucion, codigoFactura, empresaInstitucion);
        return (HashMap)temp.clone();
    }
    
    /**
     * metodo para consultar los servicios de una factura
     * @param con Connection
     * @param codigoFactira String
     * @return HashMap
     */
    public HashMap consultarSeccionServiciosFormatoEstatico( Connection con, String codigoFactira )
    {
        HashMap temp=impFacturaDao.consultarSeccionServiciosFormatoEstatico(con, codigoFactira);
        return (HashMap)temp.clone();
    }
    
    /**
     * metodo para consultar los servicios de una factura en formato de impresion de Versalles
     * @param con Connection
     * @param codigoFactira String
     * @return HashMap
     */
    public HashMap consultarSeccionServiciosFormatoVersalles( Connection con, String codigoFactira)
    {
        HashMap temp=impFacturaDao.consultarSeccionServiciosFormatoVersalles(con, codigoFactira);
        return (HashMap)temp.clone();
    }
    
    /**
     * metodo para consultar el detalle de los servicios
     * @param con Connection
     * @param codigoDetalleFactura 
     * @return HashMap
     */
    public HashMap consultarSeccionDetServiciosFormatoEstatico( Connection con,String codigoDetalleFactura )
    {
        HashMap temp=impFacturaDao.consultarSeccionDetServiciosFormatoEstatico(con, codigoDetalleFactura);
        return (HashMap)temp.clone();
    }
    
    /**
     * metodo para consultar el detalle de los servicios para el formato de impresion Versalles
     * @param con Connection
     * @param codigoDetalleFactura 
     * @return HashMap
     */
    public HashMap consultarSeccionDetServiciosFormatoVersalles( Connection con,String codigoDetalleFactura )
    {
        HashMap temp=impFacturaDao.consultarSeccionDetServiciosFormatoVersalles(con, codigoDetalleFactura);
        return (HashMap)temp.clone();
    }
    
    /**
     * metodo para consultar los articulos de la factura
     * @param con Connection
     * @param codigoFactira  String 
     * @return HashMap
     */
    public HashMap consultarSeccionArticulosFormatoEstatico( Connection con,String codigoFactira, String filtrarXInsumos )
    {
        HashMap temp=impFacturaDao.consultarSeccionArticulosFormatoEstatico(con, codigoFactira, filtrarXInsumos);
        return (HashMap)temp.clone();  
    }
    
    /**
     * metodo para consultar los articulos de la factura para el formato de impresion Versalles
     * @param con Connection
     * @param codigoFactira  String 
     * @return HashMap
     */
    public HashMap consultarSeccionArticulosFormatoVersalles( Connection con,String codigoFactira, String filtrarXInsumos )
    {
        HashMap temp=impFacturaDao.consultarSeccionArticulosFormatoVersalles(con, codigoFactira, filtrarXInsumos);
        return (HashMap)temp.clone();  
    }
	
	/**
	 * Metodo que consulta valor letras factura por convenio
	 * y pasa valor a letras
	 * @param con
	 * @param codConvenio
	 * @return
	 */
	public HashMap consultarValorLetrasValor(Connection con, String codConvenio)
	{
		return impFacturaDao.consultarValorLetrasValor(con, codConvenio);
	}
	
	
	/*-------------------------------------------------------METODOS CREADOS POR ARMANDO PARA LA IMPRESION DE LOS ANEXOS------------------------------*/
	/**
	 * Metodo que realiza la consulta de los anexos de medicmanetos por fechas.
	 * Utilizado en la impresion de los anexos
	 * @param con
	 * @param codigoFactira
	 * @return
	 */
	public HashMap anexoSolicitudesMedicamentosFechaFactura(Connection con, String codigoFactira) 
	{
		HashMap temp=impFacturaDao.anexoSolicitudesMedicamentosFechaFactura(con,codigoFactira);
		return (HashMap)temp.clone();
	}
	
	
	/**
	 * Metodo que realiza la consulta de los anexos de medicmanetos por Orden y Fehas.
	 * Utilizado en la impresion de los anexos
	 * @param con
	 * @param codigoFactira
	 * @return
	 */
	public HashMap anexoSolicitudesMedicamentosOrdenFechaFactura(Connection con, String codigoFactira) 
	{
		HashMap temp=impFacturaDao.anexoSolicitudesMedicamentosOrdenFechaFactura(con,codigoFactira);
		return (HashMap)temp.clone();
	}

	/**
	 * 
	 * @param con
	 * @param codSubCuenta
	 * @return
	 */
	public HashMap consultarInfoCitaDadaCuenta(Connection con, String codSubCuenta) 
	{
		HashMap temp=impFacturaDao.consultarInfoCitaDadaCuenta(con,codSubCuenta);
		return (HashMap)temp.clone();
	}
	
	/**
	 * @param con
	 * @param cuenta
	 * @param convenio
	 * @return
	 * @throws SQLException
	 */
	public   DtoDatosRepsonsableFactura obtenerNombreResponsable(Connection con,Integer cuenta, Integer convenio) throws SQLException
	{
		return  impFacturaDao.obtenerNombreResponsable(con, cuenta, convenio);
	}
	
	/**
	 * @param con
	 * @param convenio
	 * @param ingreso
	 * @return
	 * @throws SQLException
	 */
	public  String consultarTipoMontoFactura(Connection con,Integer convenio, Integer ingreso) throws SQLException
	{
		return impFacturaDao.consultarTipoMontoFactura(con, convenio, ingreso);
	}

}
