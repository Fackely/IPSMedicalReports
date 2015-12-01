/*
 * @(#)WebXmlCreateConsole.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.5.0_06
 *
 */
package com.princetonsa.webCreateXml;
import java.sql.Connection;

import org.axioma.util.log.Log4JManager;

/**
 * Clase para la generacion del web.xml desde la BD, la idea de esta clase
 * es que puede ser ejecutada desde consola recibiendo dentro de los args del main:
 * 1: PathWebXml
 * 2: JDBCDriver
 * 3: DataBaseURL
 * 4: User
 * 5: PasswordBD
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class WebXmlCreateConsole 
{
	/**
	 * main
	 * @param args
	 */
	public static void main(String[] args) 
	{
		String pathWebXml=args[0];
		String xJDBCDriver=args[1];
		String xDatabaseURL= args[2];
		String xUser= args[3];
		String xPassword= args[4];
		
		WebXmlCreate mundoWebXmlCreate= new WebXmlCreate();
		Connection con;
		
		con=XconnectionDB.getConnectionDB(xJDBCDriver, xDatabaseURL, xUser, xPassword);
		mundoWebXmlCreate.cargarDocument(pathWebXml);
		mundoWebXmlCreate.llenarRolesSistema(con);
		mundoWebXmlCreate.cargarInformacionRolesFuncionalidades(con);
		boolean parseCorrecto=mundoWebXmlCreate.crearSegmentoRolesYRolesFuncionalidades();
		if(!parseCorrecto)
			Log4JManager.error("NO PARSEO CORRECTAMENTE");
		XconnectionDB.closeConnectionDB(con);
	}
}
