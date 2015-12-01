package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.ReliquidacionTarifasDao;

/**
 * 
 * @author wilson
 *
 */
public class ReliquidacionTarifas 
{
	  /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ReliquidacionTarifas.class);
    
	/**
	 * DAO de este objeto, para trabajar con Servicio en
	 * la fuente de datos
	 */
    private static ReliquidacionTarifasDao reliquidacionDao;
    
    /**
     * Constructor
     */
    public ReliquidacionTarifas()
    {
    	this.reset();
    	this.init (System.getProperty("TIPOBD"));
    }
    
	/**
	 * resetea los atributos del objeto
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
	    if ( reliquidacionDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			reliquidacionDao= myFactory.getReliquidacionTarifasDao();
			if( reliquidacionDao!= null )
				return true;
		}
		return false;
	}
	
	/** 
	 * @param con
	 * @param codigoCentroAtencion
	 * @param codigoPaciente
	 * @return
	 */
	public static HashMap obtenerCuentasReliquidar (Connection con, int codigoCentroAtencion, String codigoPaciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getReliquidacionTarifasDao().obtenerCuentasReliquidar(con, codigoCentroAtencion, codigoPaciente);
	}
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public static HashMap obtenerSolicitudesReliquidar(Connection con, String subCuenta, String esServicios)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getReliquidacionTarifasDao().obtenerSolicitudesReliquidar(con, subCuenta, esServicios);
	}
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @param codigoDetalleCargoPadre
	 * @param esServicios
	 * @return
	 */
	public static HashMap obtenerComponentesPaqueteReliquidar(Connection con, String subCuenta, double codigoDetalleCargoPadre, String esServicios)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getReliquidacionTarifasDao().obtenerComponentesPaqueteReliquidar(con, subCuenta, codigoDetalleCargoPadre, esServicios);
	}
	
	/**
	 * 
	 * @param con
	 * @param loginUsuario
	 * @param resultadoExitoso
	 * @param esqTarServOriginal
	 * @param esqTarServNuevo
	 * @param esqTarArtOriginal
	 * @param esqTarArtNuevo
	 * @return
	 */
	public static boolean insertarReliquidacion(	Connection con, 
													String loginUsuario, 
													String resultadoExitoso, 
													int esqTarServOriginal, 
													int esqTarServNuevo,	
													int	esqTarArtOriginal,	
													int esqTarArtNuevo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getReliquidacionTarifasDao().insertarReliquidacion(con, loginUsuario, resultadoExitoso, esqTarServOriginal, esqTarServNuevo, esqTarArtOriginal, esqTarArtNuevo);
	}
    
}
