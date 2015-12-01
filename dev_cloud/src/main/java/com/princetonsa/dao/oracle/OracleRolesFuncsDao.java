/*
 * @(#)OracleRolesFuncsDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import util.Answer;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.RolesFuncsDao;
import com.princetonsa.dao.sqlbase.SqlBaseRolesFuncsDao;

/**
 * Esta clase implementa en PostgeSQL el contrato de la <i>interface</i> <code>RolesFuncsDao</code>,
 * que proporciona servicios de acceso a bases de datos para el objeto RolesFuncs.
 *
 * @version 1.0, Nov 28, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class OracleRolesFuncsDao implements RolesFuncsDao
{//SIN PROBAR FUNC. SECUENCIA

    /**
	 * Cadena constante con el <i>statement</i> necesario para insertar un nuevo rol en la tabla de roles.
	 */
	private static final String insertarRolesStr = "INSERT INTO roles VALUES (seq_roles.nextval, ?)";

	/**
	 * Este método abre una nueva conexión con una BD Oracle, y retorna la conexión abierta
	 * y el <code>ResultSet</code> con toda la información sobre las funcionalidades del sistema.
	 * @return un objeto <code>Answer</code> que encapsula una conexión y el resultado de la consulta
	 */
	public Answer listaFuncs () throws SQLException 
	{
		return SqlBaseRolesFuncsDao.listaFuncs();
	}

	/**
	 * Este método abre una nueva conexión con una BD Oracle, y dado el nombre de un rol,
	 * determina si existen usuarios que lo tengan. Cierra la conexión antes de salir.
	 * @param nombreRol nombre del rol que se desea averiguar si tiene usuarios asignados a él.
	 * @return <b>true</b> si existe por lo menos un usuario que tenga el rol 'nombreRol',
	 * <b>false</b> si no
	 */
	public boolean puedoBorrarRol (String nombreRol) throws SQLException {
	    return SqlBaseRolesFuncsDao.puedoBorrarRol(nombreRol);
	}

	/**
	 * Este método abre una nueva conexión con la BD Oracle al iniciarse, y la cierra al salir. Toma
	 * un conjunto de roles y un conjunto de funcionalidades, y actualiza las tablas respectivas en la BD,
	 * cuidando de no introducir información redundante y que los datos insertados sean 100% consistentes
	 * con la información del archivo web.xml de la aplicación web. La información de web.xml está presente
	 * en los objetos 'roles' y 'funcionalidades', y se debe garantizar que el contenido de la BD sea consistente
	 * con el contenido de éstos objetos.
	 * @param roles conjunto de objetos <code>Rol</code>
	 * @param funcionalidades conjunto de objetos <code>Funcionalidad</code>
	 * @return <b>true</b> si se pudieron efectuar las inserciones, actualizaciones y eliminaciones necesarios
	 * en la BD (terminando con un COMMIT), <b>false</b> si falló alguna de las operaciones y fue necesario hacer un ROLLBACK.
	 */
	public boolean guardarCambios (final Set roles, final Set funcionalidades) throws SQLException {
	    return SqlBaseRolesFuncsDao.guardarCambios (roles, funcionalidades, insertarRolesStr);
	}

	/**
	 * Implementación del método que obtiene todas las funcionalidades
	 * que dependen de una particular en una BD Oracle
	 * 
	 * @see com.princetonsa.dao.RolesFuncsDao#obtenerFuncionalidadesDependientes (String ) throws SQLException
	 */
	public Collection obtenerFuncionalidadesDependientes ( String codigoFuncionalidadPadre) throws SQLException
	{
		return SqlBaseRolesFuncsDao.obtenerFuncionalidadesDependientes (codigoFuncionalidadPadre) ;
	}

	/**
	 * Implementación del método que busca las funcionalidades
	 * huérfanas en una BD Oracle
	 * 
	 * @see com.princetonsa.dao.RolesFuncsDao#buscarHuerfanos(Connection ) throws SQLException
	 */
	public ArrayList buscarHuerfanos(Connection con, String restricciones) throws SQLException
	{
	    return SqlBaseRolesFuncsDao.buscarHuerfanos(con, restricciones);
	}
	
	/**
	 * Método para buscar todos los huerfanos del sistema, incluyendo los hijos de algun padre que todavia esta en
	 * BD pero que ya ha sido seleccionado por el usuario en el boton 'Quitar' de la funcionalidad Crear Roles, tambien se
	 * hace el filtro por el rol.
	 * @param con
	 * @param codigoPadre
	 * @param rol
	 * @return
	 * @throws SQLException
	 */
	public ArrayList busquedaHuerfanosEHijosPadreEliminadoYRol(Connection con, String codigoPadre, String rol) throws SQLException
	{
	    return SqlBaseRolesFuncsDao.busquedaHuerfanosEHijosPadreEliminadoYRol(con, codigoPadre, rol);
	}
	
	/**
	 * Metodo que se encarga de consultar todas las funcionalidades hijas del sistema
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public ArrayList listaFuncionalidadesHijas(Connection con) throws SQLException
	{
	    return SqlBaseRolesFuncsDao.listaFuncionalidadesHijas(con, DaoFactory.ORACLE);
	}
	
	/**
	 * metodo que retorna el listado de roles del sistema
	 * @param con
	 * @return
	 */
    public Vector listadoRolesSistema(Connection con)
    {
    	return SqlBaseRolesFuncsDao.listadoRolesSistema(con);
    }
    
    /**
     * Metodo que carga toda la informacion de roles funcionalidades para poder hacer el 
     * <security-constraint> del web.xml, este mapa tiene el codigo - nombre -path funcionalidad
     * y el detalle_ que es otro mapa encapsulado con la información de los roles que tienen esa func 
     * @param con
     * @return
     */
	public HashMap cargarInformacionRolesFuncionalidades(Connection con)
	{
		return SqlBaseRolesFuncsDao.cargarInformacionRolesFuncionalidades(con);
	}
}