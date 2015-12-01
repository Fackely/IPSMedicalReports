
/*
 * Creado   20/12/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package util.inventarios;

import java.sql.Connection;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.UtilidadInventariosDao;

import util.ConstantesBD;
import util.UtilidadBD;


/**
 * Clase para implementar las validaciones
 * propias del modulo de inventarios, para
 * cada una de sus funcionalidades
 *
 * @version 1.0, 20/12/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class UtilidadValidacionInventarios 
{
    /**
	 * Metodo que retorna el DaoFactory de la funcionalidad
	 * @return
	 */
	private static UtilidadInventariosDao utilidadDao()  
	{
	    return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadInventariosDao();
	}    
    /**
	 * metodo para verificar sin un centro de 
     * costo es de tipo de area subalmacen 
	 * @param codCentroCosto int
	 * @param institucion int 
	 * @return boolean
	 */
	public static boolean esCentroCostoSubalmacen(int codCentroCosto,int institucion)
	{
	    int resp=ConstantesBD.codigoNuncaValido;
	    Connection con=UtilidadBD.abrirConexion();
	    resp=utilidadDao().obtenerTipoAreaCentroCosto(con,codCentroCosto,institucion);
	    UtilidadBD.closeConnection(con);
	    if(resp==ConstantesBD.codigoTipoAreaSubalmacen)
	        return true;
	    else
	        return false;
	}
	/**
	 * metodo para validar si un almacen es
	 * valido para realizar traslado
	 * @param con Connection
	 * @param institucion int
	 * @param tipoTransaccion int
	 * @param centroCosto int
	 * @return boolean
	 */
	public static boolean esAlmacenAutorizadoParaTraslado(int institucion,int tipoTransaccion,int centroCosto)
	{
	    Connection con=UtilidadBD.abrirConexion();
	    boolean resp=utilidadDao().esAlmacenAutorizadoParaTraslado(con, institucion, tipoTransaccion, centroCosto);
	    UtilidadBD.closeConnection(con);
	    return resp;
	}
}
