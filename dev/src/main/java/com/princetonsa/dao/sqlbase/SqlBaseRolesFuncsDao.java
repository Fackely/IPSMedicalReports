/*
 * @(#)SqlBaseRolesFuncsDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */

package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.Answer;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.Funcionalidad;
import com.princetonsa.mundo.Rol;
import com.princetonsa.mundo.Roles;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a RolesFuncs
 *
 *	@version 1.0, May 31, 2004
 */
public class SqlBaseRolesFuncsDao 
{

	private static Logger logger = Logger.getLogger(SqlBaseRolesFuncsDao.class);
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para consultar toda la información 
	 * sobre las funcionalidades parametrizables.
	 */
	private static final String listaFuncsStr = "SELECT codigo_func AS codigoFuncionalidad, nombre_Func AS nombreFuncionalidad, archivo_func AS archivoFuncionalidad FROM funcionalidades where es_parametrizable=" + ValoresPorDefecto.getValorTrueParaConsultas() + " ORDER BY nombreFuncionalidad";

	/**
	 * Cadena constante con el <i>statement</i> necesario para determinar cuántos usuarios tiene un rol determinado.
	 */
	private static final String usuariosEnRolStr = "SELECT COUNT(1) AS numeroUsuarios FROM roles_usuarios WHERE nombre_rol = ?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para consultar los nombres de los roles.
	 */
	private static final String nombreRolesStr = "SELECT nombre_rol AS nombreRol FROM roles";

	/**
	 * Cadena constante con el <i>statement</i> necesario para borrar las funcionalidades de su respectiva tabla.
	 */
	private static final String borrarFuncionalidadesStr = "DELETE FROM roles_funcionalidades";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para borrar un rol de la tabla de roles.
	 */
	private static final String borrarRolesStr = "DELETE FROM roles WHERE nombre_rol = ?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar una nueva funcionalidad en la tabla de funcionalidades.
	 */
	private static final String insertarFuncionalidadesStr = "INSERT INTO roles_funcionalidades VALUES (?, ?, ?)";

	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar todos los
	 * roles que existen en el sistema
	 */
	private static final String buscarRolesSistemaStr="select nombre_rol from roles";
    
	/**
	 * carga el listado de roles existentes en el sistema ordenados alfabeticamente por el nombre
	 */
	private static final String listadoRolesSistemaStr= "SELECT nombre_rol AS nombreRol FROM roles ORDER BY nombre_rol";
	
	/*
	Eliminación 3er Nivel 
	
	--(A)Busca todas las funcionalidades no parametrizables
	--del rol 'prueba'
	SELECT rf1.codigo_func, f1.nombre_func from roles_funcionalidades rf1 INNER JOIN funcionalidades f1 ON (rf1.codigo_func=f1.codigo_func) where rf1.nombre_rol='prueba' and f1.es_parametrizable=false
	
	--(B)Busca todas las funcionalidades parametrizables
	--del rol 'prueba'
	SELECT rf2.codigo_func from roles_funcionalidades rf2 INNER JOIN funcionalidades f2 ON (rf2.codigo_func=f2.codigo_func) where rf2.nombre_rol='prueba' and f2.es_parametrizable=true
	
	--(C)Necesitamos saber todas funcionalidades generadas a través de (B) -- Hijas Cuyo padre es (B) --
	
	SELECT df1.funcionalidad_hija as generada from dependencias_func df1 where df1.funcionalidad_padre IN (
	  (B)
	)
	
	--(D)Debemos buscar todas las funcionalidades no
	--parametrizables que NO fueron generadas
	SELECT rf3.codigo_func, f3.nombre_func from roles_funcionalidades rf3 INNER JOIN funcionalidades f3 ON (rf3.codigo_func=f3.codigo_func) where rf3.nombre_rol='prueba' and f3.es_parametrizable=false and rf3.codigo_func NOT IN (C)
	
	
	
	
	
	
	SELECT rf3.codigo_func, f3.nombre_func from roles_funcionalidades rf3 INNER JOIN funcionalidades f3 ON (rf3.codigo_func=f3.codigo_func) where rf3.nombre_rol='prueba' and f3.es_parametrizable=false and rf3.codigo_func NOT IN (
	
	
		SELECT df1.funcionalidad_hija as generada from dependencias_func df1 where df1.funcionalidad_padre IN (
		
		
		
		
			SELECT rf2.codigo_func from roles_funcionalidades rf2 INNER JOIN funcionalidades f2 ON (rf2.codigo_func=f2.codigo_func) where rf2.nombre_rol='prueba' and f2.es_parametrizable=true
		
		
		
		
		
		)
	
		
	
	
	)
	
	 * 
	 */
	
	
	/**
	 * cadena para buscar todos los huerfanos del sistema, incluyendo los hijos de algun padre que todavia esta en
	 * BD pero que ya ha sido seleccionado por el usuario en el boton 'Quitar' de la funcionalidad Crear Roles,
	 */
	private static final String busquedaDeTodosHuerfanosIncluidoPadreStr="SELECT " +
																												"rf3.codigo_func, f3.nombre_func, " +
																												"f3.archivo_func " +
																												"from roles_funcionalidades rf3 " +
																												"INNER JOIN funcionalidades f3 ON (rf3.codigo_func=f3.codigo_func) " +
																												"where " +
																												"rf3.nombre_rol=? " +
																												"and f3.es_parametrizable=" + ValoresPorDefecto.getValorFalseParaConsultas()+ " " +
																												"and rf3.codigo_func " +
																												"NOT IN ("+
																																"SELECT " +
																																"df1.funcionalidad_hija as generada " +
																																"from " +
																																"dependencias_func df1 " +
																																"where " +
																																"df1.funcionalidad_padre " +
																																"IN ("+
																																			"SELECT " +
																																			"rf2.codigo_func " +
																																			"from " +
																																			"roles_funcionalidades rf2 " +
																																			"INNER JOIN funcionalidades f2 ON (rf2.codigo_func=f2.codigo_func) " +
																																			"where " +
																																			"rf2.nombre_rol=? " +
																																			"and f2.es_parametrizable=" + ValoresPorDefecto.getValorTrueParaConsultas()+" " +
																																			"and df1.funcionalidad_padre<>? "+
																																	 ")"+
						                                                                                                     ")";
    
	/**
	 * Lista de las funcionalidades hijas del sistema (cod-nombre)
	 */
	private static final String listaFuncionalidadesHijasStr="SELECT distinct rf3.codigo_func AS codigoHija, f3.nombre_func AS nombreHija from administracion.roles_funcionalidades rf3 INNER JOIN funcionalidades f3 ON (rf3.codigo_func=f3.codigo_func)";
	private static final String listaFuncionalidadesHijasPos=" where f3.es_parametrizable=false ";
	private static final String listaFuncionalidadesHijasOra="where f3.es_parametrizable=0 ";
	
	/**
	 * lista de funcionalidades x roles
	 */
	private static final String listadoFuncionalidadesXRolesStr="SELECT DISTINCT f.codigo_func AS codigoFuncionalidad, f.nombre_func AS nombreFuncionalidad, f.archivo_func AS pathFuncionalidad, rf.isssl AS isSSL FROM funcionalidades f, roles r, roles_funcionalidades rf WHERE rf.codigo_func=f.codigo_func AND r.nombre_rol=rf.nombre_rol ORDER BY nombreFuncionalidad ";
	
	/**
	 * detalle de los roles que contienen una funcionalidad especifica
	 */
	private static final String detalleRolXFuncionalidadStr= "SELECT nombre_rol AS nombreRol FROM roles_funcionalidades WHERE codigo_func=? ORDER BY nombreRol";
	
	/**
	 * Implementación del método que obtiene todas las funcionalidades
	 * que dependen de una particular en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.RolesFuncsDao#obtenerFuncionalidadesDependientes (Connection , String ) throws SQLException
	 */
	public static Collection obtenerFuncionalidadesDependientes (String codigoFuncionalidadPadre) throws SQLException
	{
		Connection con=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();
		
		String obtenerFuncionalidadesDependientesStr="SELECT func.codigo_func || '' as codigoFunc, func.nombre_func as nombreFunc, func.archivo_func as pathCompleto from dependencias_func depfunc INNER JOIN funcionalidades func ON (depfunc.funcionalidad_hija=func.codigo_func) where depfunc.funcionalidad_padre=" + codigoFuncionalidadPadre;
		PreparedStatementDecorator obtenerFuncionalidadesDependientesStatement= new PreparedStatementDecorator(con.prepareStatement(obtenerFuncionalidadesDependientesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		Collection col=UtilidadBD.resultSet2Collection(new ResultSetDecorator(obtenerFuncionalidadesDependientesStatement.executeQuery()));
		if (con!=null&&!con.isClosed())
		{
			UtilidadBD.closeConnection(con);
		}
		return col;
	}

	/**
	 * Este método abre una nueva conexión con una BD (Según TIPODB parametrizado), y retorna la conexión abierta
	 * y el <code>ResultSet</code> con toda la información sobre las funcionalidades del sistema.
	 * @return un objeto <code>Answer</code> que encapsula una conexión y el resultado de la consulta
	 */
	public static Answer listaFuncs () throws SQLException {
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		Connection con = myFactory.getConnection();
		return new Answer (new ResultSetDecorator( new PreparedStatementDecorator(con.prepareStatement(listaFuncsStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet)).executeQuery()), con);
	}

	/**
	 * Este método abre una nueva conexión con una BD Genérica, y dado el nombre de un rol,
	 * determina si existen usuarios que lo tengan. Cierra la conexión antes de salir.
	 * @param nombreRol nombre del rol que se desea averiguar si tiene usuarios asignados a él.
	 * @return <b>true</b> si existe por lo menos un usuario que tenga el rol 'nombreRol',
	 * <b>false</b> si no
	 */
	public static boolean puedoBorrarRol (String nombreRol) throws SQLException {

		boolean puedo = false;

		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		Connection con = myFactory.getConnection();
		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(usuariosEnRolStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ps.setString(1, nombreRol);
		ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());

		if (rs.next()) {
			puedo = (rs.getInt("numeroUsuarios") > 0) ? false : true;
		}

		if (con != null && !con.isClosed()) {
			UtilidadBD.closeConnection(con);
			con = null;
		}

		return puedo;
	}
	
	/**
	 * Método que busca las funcionalidades huérfanas del
	 * sistema y las devuelve como un ArrayList de objetos 
	 * de tipo Funcionalidad.
	 * 
	 * @param con Conexión con la fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public static ArrayList buscarHuerfanos(Connection con, String restricciones) throws SQLException
	{
	    if(!restricciones.equals(""))
	        restricciones=" and rf2.codigo_func NOT IN ("+restricciones+") ";
	    /**
		 * Cadena constante con el <i>statement</i> necesario para buscar todos las
		 * funcionalidades huérfanas en el sistema. Se considera que una funcionalidad
		 * es huérfana si no la parametrizó el usuario (parametrizable es false y no
		 * hay una funcionalidad que sea padre de ella en el sistema- Una func. padre
		 * debe ser parametrizable)
		 */
		String busquedaDeHuerfanosStr="SELECT rf3.codigo_func, f3.nombre_func, f3.archivo_func from roles_funcionalidades rf3 INNER JOIN funcionalidades f3 ON (rf3.codigo_func=f3.codigo_func) where rf3.nombre_rol=? and f3.es_parametrizable=" + ValoresPorDefecto.getValorFalseParaConsultas()+ " and rf3.codigo_func NOT IN ("+
							"SELECT df1.funcionalidad_hija as generada from dependencias_func df1 where df1.funcionalidad_padre IN ("+
							"SELECT rf2.codigo_func from roles_funcionalidades rf2 INNER JOIN funcionalidades f2 ON (rf2.codigo_func=f2.codigo_func) where rf2.nombre_rol=? and f2.es_parametrizable=" + ValoresPorDefecto.getValorTrueParaConsultas()+ " "+restricciones+" "+ 
								")"+
							")";
		logger.info("busquedaDeHuerfanosStr--------------->"+busquedaDeHuerfanosStr);
		
	    String nombreRol;
	    ArrayList resultado=new ArrayList();
	    

	    PreparedStatementDecorator busquedaEliminarParaRolStatement;
	    ResultSetDecorator rs2, rs1=new ResultSetDecorator( new PreparedStatementDecorator(con.prepareStatement(buscarRolesSistemaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet)).executeQuery());
	    logger.info("buscarRolesSistemaStr--------------->"+buscarRolesSistemaStr);
	    while (rs1.next())
	    {
	        nombreRol=rs1.getString("nombre_rol");
	        busquedaEliminarParaRolStatement= new PreparedStatementDecorator(con.prepareStatement(busquedaDeHuerfanosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	        busquedaEliminarParaRolStatement.setString(1, nombreRol);
	        busquedaEliminarParaRolStatement.setString(2, nombreRol);
	        rs2=new ResultSetDecorator(busquedaEliminarParaRolStatement.executeQuery());
	        while (rs2.next())
	        {
	            Funcionalidad func=new Funcionalidad((rs2.getInt("codigo_func") + "-" + UtilidadTexto.flattenString(rs2.getString("nombre_func"))).trim(), rs2.getString("archivo_func").trim(), nombreRol, false);
	            resultado.add(func);
	        }
	    }
	    return resultado;
	}
	
	/**
	 * Metodo que se encarga de consultar todas las funcionalidades hijas del sistema
	 * @param con
	 * @param TipoBD 
	 * @return
	 * @throws SQLException
	 */
	public static ArrayList listaFuncionalidadesHijas(Connection con, int TipoBD) throws SQLException
	{
		String funcionalidades=listaFuncionalidadesHijasStr;
		switch(TipoBD){
		case DaoFactory.ORACLE:
			funcionalidades+=listaFuncionalidadesHijasOra;
		break;
		case DaoFactory.POSTGRESQL:
			funcionalidades+=listaFuncionalidadesHijasPos;
			break;
		     default:
		}
	    ArrayList resultado=new ArrayList();
	    logger.info("funcionalidades******************"+funcionalidades);
	    PreparedStatementDecorator busquedaHijasSistema=  new PreparedStatementDecorator(con.prepareStatement(funcionalidades,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    ResultSetDecorator rs=new ResultSetDecorator(busquedaHijasSistema.executeQuery());
	    while(rs.next())
	    {
	        resultado.add(rs.getInt("codigoHija")+"-"+UtilidadTexto.flattenString(rs.getString("nombreHija")).trim());
	    }
	    return resultado;
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
	public static ArrayList busquedaHuerfanosEHijosPadreEliminadoYRol(Connection con, String codigoPadre, String rol) throws SQLException
	{
	    String nombreRol;
	    ArrayList resultado=new ArrayList();
	    PreparedStatementDecorator busquedaEliminarParaRolStatement;
	    ResultSetDecorator rs2, rs1=new ResultSetDecorator( new PreparedStatementDecorator(con.prepareStatement(buscarRolesSistemaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet)).executeQuery());
	    while (rs1.next())
	    {
	        nombreRol=rs1.getString("nombre_rol");
	        if(nombreRol.trim().equals(rol))
	        {    
		        busquedaEliminarParaRolStatement= new PreparedStatementDecorator(con.prepareStatement(busquedaDeTodosHuerfanosIncluidoPadreStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		        busquedaEliminarParaRolStatement.setString(1, nombreRol);
		        busquedaEliminarParaRolStatement.setString(2, nombreRol);
		        busquedaEliminarParaRolStatement.setInt(3, Integer.parseInt(codigoPadre));
		        
		        rs2=new ResultSetDecorator(busquedaEliminarParaRolStatement.executeQuery());
		        while (rs2.next())
		        {
		            Funcionalidad func=new Funcionalidad((rs2.getInt("codigo_func") + "-" + UtilidadTexto.flattenString(rs2.getString("nombre_func"))).trim(), rs2.getString("archivo_func").trim(), nombreRol, false);
		            resultado.add(func);
		        }
	        }    
	    }
	    return resultado;
	}
	
	/**
	 * Este método abre una nueva conexión con la BD Genérica al iniciarse, y la cierra al salir. Toma
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
	public static boolean guardarCambios (final Set roles, final Set funcionalidades, String insertarRolesStr) throws SQLException {
		boolean saved = false;
		Funcionalidad func;
		Rol rol;

		/* sacamos una copia local 'deep' del conjunto de roles */
		Roles tmpRoles = new Roles();
		Iterator i = roles.iterator();
		while (i.hasNext()) {
			rol = (Rol) i.next();
			tmpRoles.addRol(new Rol(rol.getDescripcion(), rol.getNombre()));
		}
		Set myRoles = tmpRoles.getRoles();

		/* Esta operación debe ser una transacción. Aquí la iniciamos */
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		Connection con = myFactory.getConnection();
		boolean resp0;

		//Como la validación de este método funciona al revés
		//(Si alguno de los boolean es true es porque algo salió mal)
		//Acá hacemos una negación para resp0 y el inicio de la transacción
		resp0=!(myFactory.beginTransaction(con));

		/* Insertamos los roles nuevos. Primero, averigüo cuáles ya están en la BD */
		Roles rolesViejos = new Roles();
		Roles copiaRolesViejos = new Roles();
		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(nombreRolesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());

		String nombreRol = "";
		while (rs.next()) {
			nombreRol = rs.getString("nombreRol").trim();
			rolesViejos.addRol(new Rol("",nombreRol));
			copiaRolesViejos.addRol(new Rol("",nombreRol));
		}
		rs.close();

		/* Borramos todos los roles_funcionalidades viejos */
		boolean resp1 = false;
		ps =  new PreparedStatementDecorator(con.prepareStatement(borrarFuncionalidadesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		try {
			ps.execute();
		}	catch (SQLException sqle) {
				logger.error("Error guardando roles: "+sqle);
				resp1 = true;
		}

		/* Eliminamos de la BD los roles que NO están en web.xml y sabemos NO existen usuarios que los utilicen */
		boolean resp2 = false;
		rolesViejos.getRoles().removeAll(myRoles);
		i = rolesViejos.getRoles().iterator();
		int [] tmp = new int [0];
		ps =  new PreparedStatementDecorator(con.prepareStatement(borrarRolesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		try {
			while (i.hasNext()) {

				rol = (Rol) i.next();
				ps.clearParameters();
				ps.setString(1, rol.getNombre().trim());
				ps.addBatch();

			}
			tmp = ps.executeBatch();

		}	catch (SQLException sqle) {
				Log4JManager.info(sqle);
				
				resp2 = true;
		}

		/* Debemos verificar que *todos* los DELETE tuvieron éxito */
		int size = tmp.length, k;
		for (k = 0; k < size; k++) {
			if (tmp[k] < 0 && tmp[k] != Statement.SUCCESS_NO_INFO) {
				resp2 = true;
				break;
			}
		}

		/* Obtengo la diferencia entre los roles viejos y nuevos, sólo inserto los nuevos */
		boolean resp3 = false;
		myRoles.removeAll(copiaRolesViejos.getRoles());
		i = myRoles.iterator();
		ps =  new PreparedStatementDecorator(con.prepareStatement(insertarRolesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

		try {
			while (i.hasNext()) {

				rol = (Rol) i.next();
				ps.clearParameters();
				ps.setString(1, rol.getNombre().trim());
				ps.addBatch();

			}
			tmp = ps.executeBatch();

		}	catch (SQLException sqle) {
				resp3 = true;
		}

		/* Debemos verificar que *todos* los INSERT tuvieron éxito */
		size = tmp.length;
		for (k = 0; k < size; k++) {
			if (tmp[k] < 0 && tmp[k] != Statement.SUCCESS_NO_INFO) {
				resp3 = true;
				break;
			}
		}

		/* Insertamos todas las funcionalidades, viejas y nuevas */
		boolean resp4 = false;
		String [] resp = new String [0];
		i = funcionalidades.iterator();
		ps =  new PreparedStatementDecorator(con.prepareStatement(insertarFuncionalidadesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		try {

			while (i.hasNext()) {

				func = (Funcionalidad) i.next();
				resp = UtilidadTexto.separarNombresDeCodigos(func.getFuncName(), 1);

				if (!resp[0].trim().equals("0")) {

					ps.clearParameters();
					ps.setString(1, func.getRoleName().trim());
					ps.setInt(2, Integer.parseInt(resp[0]));
					ps.setBoolean(3, func.isSSL());
					ps.addBatch();

				}

			}
			tmp = ps.executeBatch();

		}	catch (SQLException sqle) {
				resp4 = true;
		}

		/* Debemos verificar que *todos* los INSERT tuvieron éxito */
		size = tmp.length;
		for (k = 0; k < size; k++) {
			if (tmp[k] < 0 && tmp[k] != Statement.SUCCESS_NO_INFO) {
				resp4 = true;
				break;
			}
		}
		
		/* Terminamos la transacción, bien sea con un COMMIT o un ROLLBACK */
		if (resp0 || resp1 || resp2 || resp3 || resp4)	
		{
			myFactory.abortTransaction(con);
			saved = false;
		}

		else 
		{
			myFactory.endTransaction(con);
			saved = true;
		}

		if (con != null && !con.isClosed()) {
			con.setAutoCommit(true);
			ps.close();
			UtilidadBD.closeConnection(con);
			con = null;
		}

		return saved;
	}
	
	
	/**
	 * metodo que retorna el listado de roles del sistema
	 * @param con
	 * @return
	 */
    public static Vector listadoRolesSistema(Connection con)
    {
    	Vector rolesVector= new Vector();
    	int index=0;
    	try
		{
    		ResultSetDecorator rs = new ResultSetDecorator( new PreparedStatementDecorator(con.prepareStatement(listadoRolesSistemaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet)).executeQuery());
    		while(rs.next())
    		{
    			rolesVector.add(index, UtilidadTexto.flattenString(rs.getString("nombreRol")));
    			index++;
    		}
    		return rolesVector;
    	}
    	catch(SQLException e)
		{
    		logger.error("Error en listadoRolesSistema de SqlBaseRolesFuncsDao: "+e);
    		return null;
		}
    }
	
    
    /**
     * Metodo que carga toda la informacion de roles funcionalidades para poder hacer el 
     * <security-constraint> del web.xml, este mapa tiene el codigo - nombre -path funcionalidad
     * y el detalle_ que es otro mapa encapsulado con la información de los roles que tienen esa func 
     * @param con
     * @return
     */
	public static HashMap cargarInformacionRolesFuncionalidades(Connection con)
    {
        HashMap map= new HashMap();
        try
        {
            String[] colums={   "codigoFuncionalidad",
                                "nombreFuncionalidad",
                                "pathFuncionalidad",
                                "isSSL"
                            };
            
            PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(listadoFuncionalidadesXRolesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            map=UtilidadBD.resultSet2HashMap(colums,new ResultSetDecorator(cargarStatement.executeQuery()), false, true).getMapa();
            for(int i=0; i<Integer.parseInt(map.get("numRegistros").toString()) ; i++)
            {
                String codigoFuncionalidadStr= map.get("codigoFuncionalidad_"+i).toString();
                map.put("detalle_"+i,  detalleRolXFuncionalidad(con, codigoFuncionalidadStr));
            }
            return map;
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en cargarInformacionRolesFuncionalidades de : SqlBaseRolesFuncsDao "+e.toString());
            return map;
        }
    }
    
    
    /**
     * metodo que carga un mapa con los roles que contienen una funcionalidad especifica
     * @param con
     * @param codigoFuncionalidad
     * @return
     */
    private static HashMap detalleRolXFuncionalidad(Connection con, String codigoFuncionalidad)
    {
        HashMap map= new HashMap();
        try
        {
            String[] colums={ "nombreRol"};          
            PreparedStatementDecorator cargarStatement= new PreparedStatementDecorator(con.prepareStatement(detalleRolXFuncionalidadStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            cargarStatement.setString(1,codigoFuncionalidad);;
            map=UtilidadBD.resultSet2HashMap(colums,new ResultSetDecorator(cargarStatement.executeQuery()), false, true).getMapa();
            map.put("numColumnas", colums.length+"");
            return map;
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en detalleRolXFuncionalidad de : SqlBaseRolesFuncsDao "+e.toString());
            return map;
        }
    }
    
}