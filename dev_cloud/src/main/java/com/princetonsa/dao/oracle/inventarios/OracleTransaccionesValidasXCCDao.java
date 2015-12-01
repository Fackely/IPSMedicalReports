
/*
 * Creado   21/11/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.dao.oracle.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.TransaccionesValidasXCCDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseTransaccionesValidasXCCDao;

/**
 * 
 *
 * @version 1.0, 21/11/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class OracleTransaccionesValidasXCCDao implements  TransaccionesValidasXCCDao 
{
    /**
     * metodo para generar la consulta de 
     * transacciones validas
     * @param con Connection
     * @param institucion int
     * @return HashMap
     */
    public HashMap generarConsulta(Connection con,int institucion,int codCentroCosto,boolean esVerificar, int transaccionFiltro)
    {
        return SqlBaseTransaccionesValidasXCCDao.generarConsulta(con, institucion,codCentroCosto,esVerificar, transaccionFiltro);
    }
    /**
     * metodo para insertar los registros
     * de transacciones validas
     * @param con Connection
     * @param codCC int 
     * @param codTipoTrans int
     * @param codClaseInv int
     * @param codGrupoInv int
     * @param institucion int    
     * @return boolean
     */
    public boolean generarInsert(Connection con,int codCC,int codTipoTrans,int codClaseInv,int codGrupoInv,int institucion)
    {
        /**
         * query para insertar transacciones validas
         */
        final String insertTransaccionesValidasStr="INSERT INTO " +
														    		"trans_validas_x_cc_inven" +
														    		"(codigo,centros_costo," +
														    		"tipos_trans_inventario," +
														    		"clase_inventario," +
														    		"grupo_inventario," +
														    		"institucion) " +
														    		"VALUES(seq_trans_validas_x_cc_inven.NEXTVAL,?,?,?,?,?)";
        return SqlBaseTransaccionesValidasXCCDao.generarInsert(con, codCC, codTipoTrans, codClaseInv, codGrupoInv, institucion, insertTransaccionesValidasStr, "seq_trans_validas_x_cc_inven.NEXTVAL");
    }
    /**
     * metodo para actualizar los registros de 
     * transacciones validas
     * @param con Connection
     * @param codigo int
     * @param codCC int 
     * @param codTipoTrans int
     * @param codClaseInv int
     * @param codGrupoInv int
     * @param institucion int 
     * @return boolean 
     */
    public boolean gerarUpdate(Connection con,int codigo,int codCC,int codTipoTrans,int codClaseInv,int codGrupoInv,int institucion)
    {
        return SqlBaseTransaccionesValidasXCCDao.gerarUpdate(con, codigo, codCC, codTipoTrans, codClaseInv, codGrupoInv, institucion);
    }
    /**
     * metodo para eliminar registros de transacciones
     * validas
     * @param con Connection
     * @param codigo int
     * @param institucion int
     * @return boolean
     */
    public boolean generarDelete(Connection con, int codigo, int institucion)
    {
        return SqlBaseTransaccionesValidasXCCDao.generarDelete(con, codigo, institucion);
    }
    /**
     * metodo para consultar los valores
     * actuales de los registros de transacciones
     * validas
     * @param con Connection
     * @param codigo int
     * @param institucion int
     * @return HashMap
     */
    public HashMap generarConsultaCamposAModificarStr(Connection con,int codigo,int institucion)
    {
        return SqlBaseTransaccionesValidasXCCDao.generarConsultaCamposAModificarStr(con, codigo, institucion);
    }
    /**
     * metodo para consultar los códigos
     * de los registros insertados en la BD
     * @param con Connection
     * @param institucion int 
     * @return HashMap
     */
    public  HashMap generarConsultaRegistrosBD(Connection con,int institucion)
    {
        return SqlBaseTransaccionesValidasXCCDao.generarConsultaRegistrosBD(con, institucion);
    } 
 
    /**
	 * Método que carga las Transacciones de Inventarios según el centro
	 * de costo seleccionado
	 * @param con
	 * @param codCentroCosto
	 * @return
	 */
	public HashMap<String, Object> cargarTransaccionesInventarios(Connection con, int codCentroCosto, int institucion)
	{
		return SqlBaseTransaccionesValidasXCCDao.cargarTransaccionesInventarios(con, codCentroCosto, institucion);
	}
    
	/**
	 * @param con
	 * @param consecutivoTrans
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public boolean eliminarTransaccion(Connection con, String consecutivoTrans, int centroCosto, int institucion)
	{
		return SqlBaseTransaccionesValidasXCCDao.eliminarTransaccion(con, consecutivoTrans, centroCosto, institucion);
	}
	
	/**
	 * Método que inserta una nueva Transacción 
	 * @param con
	 * @param criterios
	 * @return
	 */
	public boolean insertarTransaccion(Connection con, HashMap<String, Object> criterios)
	{
		return SqlBaseTransaccionesValidasXCCDao.insertarTransaccion(con, criterios);
	}
	
}