/*
 * @(#)RolesFuncsDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Set;
import java.util.Vector;

import util.Answer;

/**
 * Esta <i>interface</i> define el contrato de operaciones que deben implementar
 * los objetos que presten el servicio de acceso a una fuente de datos para el
 * objeto <code>RolesFuncs</code>.
 *
 * @version 1.0, Nov 28, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public interface RolesFuncsDao {

	/**
	 * Este método abre una nueva conexión con una fuente de datos, y retorna la conexión abierta
	 * y el <code>ResultSet</code> con toda la información sobre las funcionalidades del sistema.
	 * @return un objeto <code>Answer</code> que encapsula una conexión y el resultado de la consulta
	 */
	public Answer listaFuncs () throws SQLException;

	/**
	 * Este método abre una nueva conexión con una fuente de datos, y dado el nombre de un rol,
	 * determina si existen usuarios que lo tengan. Cierra la conexión antes de salir.
	 * @param nombreRol nombre del rol que se desea averiguar si tiene usuarios asignados a él.
	 * @return <b>true</b> si existe por lo menos un usuario que tenga el rol 'nombreRol',
	 * <b>false</b> si no
	 */
	public boolean puedoBorrarRol (String nombreRol) throws SQLException;

	/**
	 * Este método abre una nueva conexión con la fuente de datos al iniciarse, y la cierra al salir. Toma
	 * un conjunto de roles y un conjunto de funcionalidades, y actualiza la información respectiva en la fuente de datos,
	 * cuidando de no introducir información redundante y que los datos insertados sean 100% consistentes
	 * con la información del archivo web.xml de la aplicación web. La información de web.xml está presente
	 * en los objetos 'roles' y 'funcionalidades', y se debe garantizar que el contenido de la fuente de datos sea consistente
	 * con el contenido de éstos objetos.
	 * @param roles conjunto de objetos <code>Rol</code>
	 * @param funcionalidades conjunto de objetos <code>Funcionalidad</code>
	 * @return <b>true</b> si se pudieron efectuar las inserciones, actualizaciones y eliminaciones necesarias
	 * en la fuente de datos (terminando con un COMMIT), <b>false</b> si falló alguna de las operaciones y
	 * fue necesario hacer un ROLLBACK.
	 */
	public boolean guardarCambios (Set roles, Set funcionalidades) throws SQLException;
	
	/**
	 * Método que devuelve todas las funcionalidades que dependen 
	 * de una dada, por medio de un ResultSetDecorator el cual tiene dos columnas,
	 * en la primera va la unión del código y nombre de la funcionalidad y
	 * en la segunda el path de la funcionalidad
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoFuncionalidadPadre
	 * @return
	 * @throws SQLException
	 */
	public Collection obtenerFuncionalidadesDependientes (String codigoFuncionalidadPadre) throws SQLException;
	
	/**
	 * Método que busca las funcionalidades huérfanas del
	 * sistema y las devuelve como un ArrayList de objetos 
	 * de tipo Funcionalidad.
	 * 
	 * @param con Conexión con la fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public ArrayList buscarHuerfanos(Connection con, String restricciones) throws SQLException;
	
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
	public ArrayList busquedaHuerfanosEHijosPadreEliminadoYRol(Connection con, String codigoPadre, String rol) throws SQLException;
	
	/**
	 * Metodo que se encarga de consultar todas las funcionalidades hijas del sistema
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public ArrayList listaFuncionalidadesHijas(Connection con) throws SQLException;
	
	/**
	 * metodo que retorna el listado de roles del sistema
	 * @param con
	 * @return
	 */
    public Vector listadoRolesSistema(Connection con);
    
    /**
     * Metodo que carga toda la informacion de roles funcionalidades para poder hacer el 
     * <security-constraint> del web.xml, este mapa tiene el codigo - nombre -path funcionalidad
     * y el detalle_ que es otro mapa encapsulado con la información de los roles que tienen esa func 
     * @param con
     * @return
     */
	public HashMap cargarInformacionRolesFuncionalidades(Connection con);
		
}