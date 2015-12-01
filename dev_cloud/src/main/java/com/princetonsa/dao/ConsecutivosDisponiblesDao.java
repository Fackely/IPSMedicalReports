/*
 * Creado   16/06/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */

package com.princetonsa.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Interfaz para el acceder a la fuente de datos de 
 * consecutivos disponibles
 *
 * @version 1.0, 24/05/2005
 * @author <a href="mailto:acardona@PrincetonSA.com">Angela Cardona</a>
 * @author [restructurada 1/12/2005] <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */

public interface ConsecutivosDisponiblesDao 
{	
    /**
     * 
     * @param con
     * @param nombre    
     * @param valor
     * @param anioVigencia
     * @param institucion
     * @return
     */
	public boolean insertar (Connection con, String nombre, String valor, String anioVigencia, int institucion); 
	
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
	public HashMap consultarConsecutivo (Connection con,int institucion,int modulo,String parametro);
	
	/**
	 * Método usado para modificar un consecutivo disponible por institucion
	 * @param con
	 * @param nombre
	 * @param valor
	 * @param anioVigencia
	 * @param institucion
	 * @param checkbox
	 * @return
	 */
	public boolean modificar (Connection con, String nombre,String valor,String anioVigencia,int institucion, boolean checkbox);
	
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
																			int codigoInstitucion); 
	
	/**
	 * metodo para consultar los consecutivos por modulo
	 * @param con Connection
	 * @param institucion int
	 * @param modulo int 
	 * @return HashMap
	 * @author jarloc
	 */
	public HashMap consultaConsecutivosXModulo (Connection con,int institucion,int modulo,ArrayList rest);
	
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
	public HashMap consultaConsecutivosInventarios(Connection con,String nombre,int institucion,int almacen,boolean esUnRegistro,int transaccion);
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
	public boolean insertarConsecutivoInventarios (Connection con,int almacen,int transaccion,int institucion,String nombre,String valor,String anioVig);
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
	public boolean modificarConsecutivoInv (Connection con, String nombre,String valor,String anioVigencia,int institucion, boolean checkbox,int almacen,int transaccion);
	
	/**
	 * metodo para obtener el valor de un consecutivo
	 * de los casos especiales de inventarios
	 * @param con Connection
	 * @param tipoTrans int, código del tipo de trnasacción
	 * @param almacen int, código del almacen
	 * @return int, valor del consecutivo
	 * @author jarloc
	 */
	public int obtenerConsecutivoInventario (Connection con,int tipoTrans,int almacen,int institucion);
	
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
	public boolean actualizarValorConsecutivoInventarios (Connection con,int valor,int tipoTransaccion,int almacen,int institucion);
     /**
     * metodo para consultar los modulos
     * @param con Connection
     * @return HashMap
     * @author jarloc
     */
    public abstract HashMap consultaModulos (Connection con);
    
    
    /**
     * Actualiza la informacion del consecutivo empresas institucion
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public boolean actualizarConsecutivoMultiEmpresa (Connection con,HashMap parametros);
	
	/**
	 * Verifica el parametro de Tiop de consecutivo a manejar de Facturas Varias
	 * @param con
	 * @return
	 */
	public int validacionTipoConsecutivoFacturasVarias(Connection con);
	
	
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
	public String consultarConsecutivoUsado(Connection con,String nombreParametro);
	
}