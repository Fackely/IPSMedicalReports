
/*
 * Creado   20/06/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.dao.oracle;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.ConsecutivosDisponiblesDao;
import com.princetonsa.dao.sqlbase.SqlBaseConsecutivosDisponiblesDao;

/**
 * Esta clase implementa 
 * <code>ConsecutivosDisponiblesDao</code>, 
 * 
 * @version 1.0, 20/06/2005
 * @author <a href="mailto:acardona@PrincetonSA.com">Angela Cardona</a>
 * @author [restructurada 1/12/2005] <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class OracleConsecutivosDisponiblesDao implements ConsecutivosDisponiblesDao 
{

    /**
	 * metodo para insertar consecutivos disponibles.
	 * @param con Connection, conexión con la fuente de datos
	 * @param nombre String, nombre.	 
	 * @param anioVigencia String, año del consecutivo 
	 * @param valor String int, numero del consecutivo
	 * @param institucion int
	 * @return boolean, true efectivo, false de lo contrario.
	 */
	public boolean insertar (Connection con, String nombre,String valor, String anioVigencia, int institucion)
	{
	   return SqlBaseConsecutivosDisponiblesDao.insertar(con,nombre,valor,anioVigencia,institucion);
	}

	
	/**
	 * metodo para realizar la consulta de un
	 * consecutivo
	 * @param con Connection
	 * @param institucion int
	 * @param modulo int
	 * @param parametro String 
	 * @return HashMap
	 * @author jarloc
	 */
	public HashMap consultarConsecutivo (Connection con,int institucion,int modulo,String parametro)
	{
	    return SqlBaseConsecutivosDisponiblesDao.consultarConsecutivo(con, institucion, modulo, parametro);   
	}
	
	/**
	 * metodo para modificar consecutivos disponibles por institucion.
	 * @param con Connection, conexión con la fuente de datos
	 * @param nombre String, nombre del consecutivo
	 * @param anioVigencia String, fecha del consecutivo
	 * @param valor String, número del consecutivo
	 * @param institucion
	 * @return boolean, true efectivo, false de lo contrario.
	 * */
	public boolean modificar (Connection con, String nombre,String valor,String anioVigencia,int institucion, boolean checkbox)
	{
	    return SqlBaseConsecutivosDisponiblesDao.modificar (con, nombre,valor,anioVigencia,institucion,checkbox); 
	}
	
	/**
	 * Metodo para la actualizacion del valor
	 * @param con
	 * @param valor
	 * @param nombre
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean actualizarValorConsecutivoStr(	Connection con, 
																			String valor, 
																			String nombre,
																			int codigoInstitucion)
	{
	    return SqlBaseConsecutivosDisponiblesDao.actualizarValorConsecutivoStr(con, valor, nombre, codigoInstitucion);
	}
	/**
	 * metodo para consultar los consecutivos por modulo
	 * @param con Connection
	 * @param institucion int
	 * @param modulo int 
	 * @return HashMap
	 * @author jarloc
	 */
	public HashMap consultaConsecutivosXModulo (Connection con,int institucion,int modulo,ArrayList rest)
	{
	    return SqlBaseConsecutivosDisponiblesDao.consultaConsecutivosXModulo(con, institucion, modulo,rest);
	}
	
	/**
	 * Metodo para consultar los consecutivos de 
	 * inventarios para los casos de consecutivos por
	 * almacen y unico en el sistema
	 * @param con Connection
	 * @param nombre String 
	 * @param institucion int 
	 * @param esUnRegistro boolean
	 * @param transaccion int
	 * @return HashMap
	 * @author jarloc
	 */
	public HashMap consultaConsecutivosInventarios(Connection con,String nombre,int institucion,int almacen,boolean esUnRegistro,int transaccion)
	{
	    return SqlBaseConsecutivosDisponiblesDao.consultaConsecutivosInventarios(con, nombre, institucion,almacen,esUnRegistro,transaccion);
	}
	/**
	 * metodo para insertar consecutivos
	 * inventarios por almacen o unico
	 * en el sistema
	 * @param con Connection
	 * @param almacen int
	 * @param transaccion int
	 * @param institucion int
	 * @param nombre String
	 * @param valor String
	 * @param anioVig String
	 * @return booelan
	 * @author jarloc
	 */
	public boolean insertarConsecutivoInventarios (Connection con,int almacen,int transaccion,int institucion,String nombre,String valor,String anioVig)
	{
	    /**
		 * query para insertar consecutivo de inventario por almacen o unico
		 * en el sistema
		 */
		final String insertConsecutivoInventarioStr="INSERT INTO " +
																			"consecutivos_inventarios" +
																			"(codigo,almacen," +
																			"tipo_transaccion,institucion," +
																			"nombre,valor," +
																			"anio_vigencia) " +
																			"VALUES (seq_consecutivos_inventarios.nextval,?,?,?,?,?,?)";	
		return SqlBaseConsecutivosDisponiblesDao.insertarConsecutivoInventarios(con, almacen, transaccion, institucion, nombre, valor, anioVig, insertConsecutivoInventarioStr);
	}
	/**
	 * metodo para modificar consecutivo sinventarios
	 * @param con Connection 
	 * @param nombre String
	 * @param valor String
	 * @param anioVigencia String
	 * @param institucion int
	 * @param checkbox boolean
	 * @return boolean
	 * @author jarloc
	 */
	public boolean modificarConsecutivoInv (Connection con, String nombre,String valor,String anioVigencia,int institucion, boolean checkbox,int almacen,int transaccion)
	{
	    return SqlBaseConsecutivosDisponiblesDao.modificarConsecutivoInv(con, nombre, valor, anioVigencia, institucion, checkbox, almacen, transaccion);
	}
	
	/**
	 * metodo para obtener el valor de un consecutivo
	 * de los casos especiales de inventarios
	 * @param con Connection
	 * @param tipoTrans int, código del tipo de trnasacción
	 * @param almacen int, código del almacen
	 * @return int, valor del consecutivo
	 * @author jarloc
	 */
	public int obtenerConsecutivoInventario (Connection con,int tipoTrans,int almacen,int institucion)
	{
	    return SqlBaseConsecutivosDisponiblesDao.obtenerConsecutivoInventario(con, tipoTrans, almacen, institucion);
	}
	
	/**
	 * Metodo para actualiazar el valor del
	 * consecutivo de inventario
	 * @param con Connection
	 * @param valor int, valor para actualizar
	 * @param tipoTransaccion int, código del tipo de transacción
	 * @param almacen int, código del almacen
	 * @param institucion int, código de la institución
	 * @return boolean
	 * @author jarloc
	 */
	public boolean actualizarValorConsecutivoInventarios (Connection con,int valor,int tipoTransaccion,int almacen,int institucion)
	{
	    return SqlBaseConsecutivosDisponiblesDao.actualizarValorConsecutivoInventarios(con, valor, tipoTransaccion, almacen, institucion);
	}
    /**
     * metodo para consultar los modulos
     * @param con Connection
     * @return HashMap
     * @author jarloc
     */
    public HashMap consultaModulos (Connection con)
    {
        return SqlBaseConsecutivosDisponiblesDao.consultaModulos(con);
    }
    
    
    /**
     * Actualiza la informacion del consecutivo empresas institucion
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public boolean actualizarConsecutivoMultiEmpresa (Connection con,HashMap parametros)
	{
		return SqlBaseConsecutivosDisponiblesDao.actualizarConsecutivoMultiEmpresa(con, parametros);
	}
	
	/**
	 * Valida el tipo de consecutivo de facturas varias
	 * @param con
	 * @return
	 */
	public int validacionTipoConsecutivoFacturasVarias(Connection con)
	{
		return SqlBaseConsecutivosDisponiblesDao.validaTipoConsecutivoFacturasVarias(con);
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar si un consecutivo ha 
	 * sido usado
	 * 
	 * @param Connection con,String nombreParametro
	 * @return String
	 * @author, Angela Maria Aguirre
	 *
	 */
	public String consultarConsecutivoUsado(Connection con,String nombreParametro){
		return SqlBaseConsecutivosDisponiblesDao.consultarConsecutivoUsado(con, nombreParametro);
	}
}