package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.actionform.facturacion.CostoInventarioPorFacturarForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.CostoInventarioPorFacturarDao;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Mauricio Jaramillo
 * Fecha: Junio de 2007
 */

public class CostoInventarioPorFacturar
{

	/**
     * Constructor de la Clase
     */
    public CostoInventarioPorFacturar()
    {
        this.init(System.getProperty("TIPOBD"));
    }
	
    /**
	 * DAO de este objeto, para trabajar con la fuente de datos
	 */
	private static CostoInventarioPorFacturarDao aplicacionDao;
	
	/**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
	    if ( aplicacionDao == null ) 
		{ 
	    	// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			aplicacionDao = myFactory.getCostoInventarioPorFacturarDao();
			if( aplicacionDao!= null )
				return true;
		}
		return false;
	}
	
	/**
	 * Metodo que llena el HashMap con los datos
	 * de la consulta de Costo de Inventarios por
	 * Facturar
	 * @param con
	 * @param forma
	 * @param usuario 
	 * @return
	 */
	public static HashMap consultarCostoInventarioPorFacturar(Connection con, CostoInventarioPorFacturarForm forma, UsuarioBasico usuario)
	{
		HashMap criterios = new HashMap();
		criterios.put("centroAtencion", forma.getCodigoCentroAtencion());
		criterios.put("centroCosto", forma.getCentroCostoSeleccionado());
		criterios.put("tipoInventario", forma.getTipoInventario());
		criterios.put("institucion", usuario.getCodigoInstitucion());
		criterios.put("anoCorte", forma.getAnoCorte());
		criterios.put("mesCorte", forma.getMesCorte());
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCostoInventarioPorFacturarDao().consultarCostoInventarioPorFacturar(con, criterios);
	}
	
}