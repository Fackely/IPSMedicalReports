
/*
 * Creado   21/11/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.HashMap;

/**
 * 
 *
 * @version 1.0, 21/11/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public interface TransaccionesValidasXCCDao 
{

    /**
     * metodo para generar la consulta de 
     * transacciones validas
     * @param con Connection
     * @param institucion int
     * @return HashMap
     */
    public HashMap generarConsulta(Connection con,int institucion,int codCentroCosto,boolean esVerificar, int transaccionFiltro);
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
    public boolean generarInsert(Connection con,int codCC,int codTipoTrans,int codClaseInv,int codGrupoInv,int institucion);
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
    public boolean gerarUpdate(Connection con,int codigo,int codCC,int codTipoTrans,int codClaseInv,int codGrupoInv,int institucion);
    /**
     * metodo para eliminar registros de transacciones
     * validas
     * @param con Connection
     * @param codigo int
     * @param institucion int
     * @return boolean
     */
    public boolean generarDelete(Connection con, int codigo, int institucion);
    /**
     * metodo para consultar los valores
     * actuales de los registros de transacciones
     * validas
     * @param con Connection
     * @param codigo int
     * @param institucion int
     * @return HashMap
     */
    public HashMap generarConsultaCamposAModificarStr(Connection con,int codigo,int institucion);
    /**
     * metodo para consultar los códigos
     * de los registros insertados en la BD
     * @param con Connection
     * @param institucion int 
     * @return HashMap
     */
    public  HashMap generarConsultaRegistrosBD(Connection con,int institucion);
    
    /**
	 * Método que carga las Transacciones de Inventarios según el centro
	 * de costo seleccionado
	 * @param con
	 * @param codCentroCosto
	 * @return
	 */
	public HashMap<String, Object> cargarTransaccionesInventarios(Connection con, int codCentroCosto, int institucion);
	
	/**
	 * @param con
	 * @param consecutivoTrans
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public boolean eliminarTransaccion(Connection con, String consecutivoTrans, int centroCosto, int institucion);
	
	/**
	 * Método que inserta una nueva Transacción 
	 * @param con
	 * @param criterios
	 * @return
	 */
	public boolean insertarTransaccion(Connection con, HashMap<String, Object> criterios);
	
}