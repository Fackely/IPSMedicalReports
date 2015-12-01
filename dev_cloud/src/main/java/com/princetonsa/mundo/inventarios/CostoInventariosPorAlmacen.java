package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.actionform.inventarios.CostoInventariosPorAlmacenForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.CostoInventariosPorAlmacenDao;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Mauricio Jllo
 * Fecha: Junio de 2008
 */

public class CostoInventariosPorAlmacen
{

	/**
     * Constructor de la Clase
     */
    public CostoInventariosPorAlmacen()
    {
        this.init(System.getProperty("TIPOBD"));
    }
	
    /**
	 * DAO de este objeto, para trabajar con la fuente de datos
	 */
	private static CostoInventariosPorAlmacenDao aplicacionDao;
	
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
			aplicacionDao = myFactory.getCostoInventariosPorAlmacenDao();
			if( aplicacionDao!= null )
				return true;
		}
		return false;
	}
	
	/**
	 * Metodo que ejecuta la consulta del total
	 * de costos de inventarios por almacen
	 * @param con
	 * @param forma
	 * @return
	 */
	public HashMap consultarCostoInventarioPorAlmacen(Connection con, CostoInventariosPorAlmacenForm forma, UsuarioBasico usuario)
	{
		HashMap criterios = new HashMap();
		criterios.put("centroAtencion", forma.getCodigoCentroAtencion());
		criterios.put("centroCosto", forma.getCentroCostoSeleccionado());
		criterios.put("institucion", usuario.getCodigoInstitucion());
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCostoInventariosPorAlmacenDao().consultarCostoInventarioPorAlmacen(con, criterios);
	}

}