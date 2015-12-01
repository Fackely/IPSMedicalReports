package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.actionform.facturacion.CostoVentasPorInventarioForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.CostoVentasPorInventarioDao;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Mauricio Jllo
 * Fecha: Junio de 2008
 */

public class CostoVentasPorInventario
{

	/**
     * Constructor de la Clase
     */
    public CostoVentasPorInventario()
    {
        this.init(System.getProperty("TIPOBD"));
    }
	
    /**
	 * DAO de este objeto, para trabajar con la fuente de datos
	 */
	private static CostoVentasPorInventarioDao aplicacionDao;
	
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
			aplicacionDao = myFactory.getCostoVentasPorInventarioDao();
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
	public static HashMap consultarCostoVentasPorInventario(Connection con, CostoVentasPorInventarioForm forma, UsuarioBasico usuario)
	{
		HashMap criterios = new HashMap();
		criterios.put("centroAtencion", forma.getCodigoCentroAtencion());
		criterios.put("centroCostoAlmacen", forma.getCentroCostoSeleccionadoAlmacen());
		criterios.put("centroCostoSolicitante", forma.getCentroCostoSeleccionadoSolicitante());
		criterios.put("tipoInventario", forma.getTipoInventario());
		criterios.put("institucion", usuario.getCodigoInstitucion());
		criterios.put("fechaInicial", forma.getFechaInicial());
		criterios.put("fechaFinal", forma.getFechaFinal());
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCostoVentasPorInventarioDao().consultarCostoVentasPorInventario(con, criterios);
	}
	
}