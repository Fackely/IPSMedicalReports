/*
 * @(#)RolesFuncsBean.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package util;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

import javax.servlet.jsp.PageContext;

import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.RolesFuncsDao;
import com.princetonsa.mundo.Funcionalidad;
import com.princetonsa.mundo.Rol;

/**
 * Esta clase proporciona la funcionalidad de <i>worker bean</i> para la página
 * de asignación de roles (RolFunc.jsp).
 * @version 1.0, Nov 28, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class RolesFuncsBean {

	/**
	 * Rol actualmente elegido. 
	 */
	private String rol = "Seleccione un Rol";

	/**
	 * Funcionalidad actualmente elegida.
	 */
	private String funcionalidad;

	/**
	 * Indica si la funcionalidad debe o no ser accedida usando SSL 
	 */
	private String ssl;

	/**
	 * Nombre del rol como va a aparecer en web.xml
	 */
	private String nombreRol;

	/**
	 * Comentario del rol como va a aparecer en web.xml
	 */
	private String comentarioRol;

	/**
	 * Rol elegido para ser eliminado
	 */
	private String borrarRol;

	/**
	 * Referencia a RolEditor, la clase que manipula los roles en web.xml.
	 */
	private static RoleEditor roleEditor = null;

	/**
	 * DAO que accede a la base de datos para las operaciones de persistencia de
	 * la edición de roles.
	 */
	private static RolesFuncsDao rolesFuncsDao = null;

	/**
	 * Lista de las funcionalidades disponibles en el sistema. 
	 */
	private static String listaFuncionalidades = null;

	/**
	 * Lista de las funcionalidades disponibles en el sistema. 
	 */
	private static String listaFuncionalidadesRestringida = null;

	/**
	 * Primera cadena constante para abrir un tag option.
	 */
	private static final String p1 = "<option value=\"";

	/**
	 * Segunda cadena constante para abrir un tag option.
	 */
	private static final String p2 = "\">";

	/**
	 * Cadena constante para cerrar un tag option.
	 */
	private static final String p3 = "</option>";
	
	/**
	 * cadena con las restricciones para el borrado de 
	 * huerfanas 
	 */
	private String restriccionesHuerfanas="";
	
	/**
	 * Constructor vacío, necesario para poder usar esta clase como
	 * un JavaBean. Inmediatamente después de crear una nueva instancia
	 * de este objeto, DEBE invocarse init().
	 */
	public RolesFuncsBean() {
		clean();
	}

	/**
	 * Inicializa el acceso a la base de datos de este objeto, le indica la
	 * ubicación del archivo web.xml y lee de la base de datos la lista de
	 * posibles funcionalidades del sistema.
	 * @param webappDeploymentDescriptor <i>path</i> al archivo "web.xml" de la aplicación
	 * @param tipoBD tipo de la base de datos que se está utilizando
	 */
	public void init(String webappDeploymentDescriptor, String tipoBD) throws SQLException {

		clean();

		if (roleEditor == null) {
			roleEditor = new RoleEditor(webappDeploymentDescriptor);
		}
		if (rolesFuncsDao == null) {
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			rolesFuncsDao = myFactory.getRolesFuncsDao();
		}
		if (listaFuncionalidades == null) 
		{
			listaFuncionalidades = loadFuncs();
			listaFuncionalidadesRestringida = loadFuncsRestringida();
		}

	}

	/**
	 * Retorna el rol seleccionado actualmente.
	 * @return el rol seleccionado actualmente
	 */
	public String getRol() {
		return rol;
	}

	/**
	 * Retorna la funcionalidad seleccionada actualmente.
	 * @return la funcionalidad seleccionada actualmente
	 */
	public String getFuncionalidad() {
		return funcionalidad;
	}

	/**
	 * Retorna el estado de ssl.
	 * @return el estado de ssl
	 */
	public String getSsl() {
		return ssl;
	}

	/**
	 * Retorna el comentario del rol.
	 * @return el comentario del rol
	 */
	public String getComentarioRol() {
		return comentarioRol;
	}

	/**
	 * Retorna el nombre del rol.
	 * @return el nombre del rol
	 */
	public String getNombreRol() {
		return nombreRol;
	}

	/**
	 * Retorna el rol a ser borrado
	 * @return el rol a ser borrado.
	 */
	public String getBorrarRol() {
		return borrarRol;
	}

	/**
	 * Establece el rol.
	 * @param rol el rol a establecer
	 */
	public void setRol(String especialidad) {
		this.rol = especialidad;
	}

	/**
	 * Establece la funcionalidad.
	 * @param funcionalidad la funcionalidad a establecer
	 */
	public void setFuncionalidad(String funcionalidad) {
		this.funcionalidad = funcionalidad;
	}

	/**
	 * Establece el ssl.
	 * @param ssl el ssl a establecer
	 */
	public void setSsl(String ssl) {
		this.ssl = ssl;
	}

	/**
	 * Establece el comentario del rol.
	 * @param comentarioRol el comentario del rol a establecer
	 */
	public void setComentarioRol(String comentarioRol) {
		this.comentarioRol = comentarioRol;
	}

	/**
	 * Establece el nombre del rol.
	 * @param nombreRol el nombre del rol a establecer
	 */
	public void setNombreRol(String nombreRol) {
		this.nombreRol = nombreRol;
	}

	/**
	 * Establece el rol que va a ser borrado.
	 * @param borrarRol el rol que se desea borrar
	 */
	public void setBorrarRol(String borrarRol) {
		this.borrarRol = borrarRol;
	}

	/**
	 * Establece el valor del editor de roles.
	 * @param roleEditor el editor de roles que se desea establecer
	 */
	public static void setRoleEditor(RoleEditor roleEditor) {
		RolesFuncsBean.roleEditor = roleEditor;
	}

	/**
	 * Inicializa los atributos de este objeto en valores vacíos, mas no nulos.
	 * Nótese que 'rol' no se inicializa, por tener éste un valor por defecto.
	 */
	public void clean() {
		this.funcionalidad = "";
		this.ssl = "";
		this.nombreRol = "";
		this.comentarioRol = "";
		this.borrarRol = "";
	}

	/**
	 * Genera la lista de roles actualmente disponibles, como tags <i>option</i>
	 * de HTML. Limita la visualización del rol superadministrador (su rol debe
	 * llamarse superadministrador)
	 * @return lista de los roles disponibles
	 */
	public synchronized String listaRoles() {

		StringBuffer sb = new StringBuffer();
		String tmp = new String();

		Iterator i = roleEditor.getRoles().iterator();
		sb.append(p1).append(rol).append(p2).append(rol).append(p3);

		while (i.hasNext()) 
		{

			tmp = ((Rol) i.next()).getNombre().trim();

			//Imprimo todos los roles disponibles con excepción de superAdministrador
			
			if (!tmp.equals("superadministrador"))
			{
				sb.append(p1);
				sb.append(tmp);
				sb.append(p2);
				sb.append(tmp);
				sb.append(p3);
			}

		}

		return sb.toString();

	}

	/**
	 * Genera la lista de roles actualmente disponibles, como tags <i>option</i>
	 * de HTML. 
	 * @return lista de los roles disponibles
	 */
	public synchronized String listaRolesCompleta() {

		StringBuffer sb = new StringBuffer();
		String tmp = new String();

		Iterator i = roleEditor.getRoles().iterator();
		sb.append(p1).append(rol).append(p2).append(rol).append(p3);

		while (i.hasNext()) 
		{

			tmp = ((Rol) i.next()).getNombre().trim();

			//Imprimo todos los roles disponibles con excepción de superAdministrador
			
				sb.append(p1);
				sb.append(tmp);
				sb.append(p2);
				sb.append(tmp);
				sb.append(p3);

		}

		return sb.toString();

	}

	/**
	 * Retorna la lista de funcionalidades del sistema, como tags <i>option</i>
	 * de HTML
	 * @return lista de las funcionalidades disponibles
	 */
	public synchronized String listaFuncs() {
		return listaFuncionalidades;
	}

	/**
	 * Retorna la lista de funcionalidades del sistema, como tags <i>option</i>
	 * de HTML, filtrando la de superAdministrador
	 * @return lista de las funcionalidades disponibles
	 */
	public synchronized String listaFuncionalidades() {
		return listaFuncionalidadesRestringida;
	}



	/**
	 * Carga desde la fuente de datos las funcionalidades del sistema, como tags
	 * <i>option</i> de HTML para ser mostradas en la página de edición de
	 * roles/funcionalidades.
	 * @return las funcionalidades del sistema
	 */
	private synchronized String loadFuncs() throws SQLException {

		Answer ans = rolesFuncsDao.listaFuncs();
		StringBuffer sb = new StringBuffer();
		ResultSetDecorator rs = ans.getResultSet();
		String tmp = new String();

		while (rs.next()) {

			tmp = rs.getString("nombreFuncionalidad");

			sb.append(p1);
			// Código de la funcionalidad, como aparece en la BD
			sb.append(rs.getString("codigoFuncionalidad"));
			sb.append('-');
			// Nombre de la funcionalidad, pero sin tildes ni espacios (como debe aparecer en web.xml)
			sb.append(tmp);
			sb.append('-');
			// Dado el path de la funcionalidad, genera el patrón que permite conceder acceso a ella
			sb.append(UtilidadTexto.generatePattern(rs.getString("archivoFuncionalidad")));
			sb.append(p2);
			// Nombre de la funcionalidad, codificada como "character entities" de HTML
			sb.append(Encoder.encode(tmp));
			sb.append(p3);

		}

		Connection con = ans.getConnection();
		if (con != null && !con.isClosed()) {
			UtilidadBD.closeConnection(con);
			con = null;
		}

		return sb.toString();

	}
	
	/**
	 * Carga desde la fuente de datos las funcionalidades del sistema, como tags
	 * <i>option</i> de HTML para ser mostradas en la página de edición de
	 * roles/funcionalidades.
	 * @return las funcionalidades del sistema
	 */
	private synchronized String loadFuncsRestringida() throws SQLException {

		Answer ans = rolesFuncsDao.listaFuncs();
		StringBuffer sb = new StringBuffer();
		ResultSetDecorator rs = ans.getResultSet();
		String tmp = new String();

		while (rs.next()) 
		{

			int codigoFuncionalidad=rs.getInt("codigoFuncionalidad");
			tmp = rs.getString("nombreFuncionalidad");

			//Solo la mostramos (en restringido) si no
			//esta en el conjunto de funcionalidades
			//de super-administrador
			if (!UtilidadTexto.estaEnArreglo(ConstantesBD.codigosFuncionalidadesReservadasSuperAdministrador, codigoFuncionalidad))
			{
				sb.append(p1);
				// Código de la funcionalidad, como aparece en la BD
				sb.append(codigoFuncionalidad + "");
				sb.append('-');
				// Nombre de la funcionalidad, pero sin tildes ni espacios (como debe aparecer en web.xml)
				sb.append(tmp);
				sb.append('-');
				// Dado el path de la funcionalidad, genera el patrón que permite conceder acceso a ella
				sb.append(UtilidadTexto.generatePattern(rs.getString("archivoFuncionalidad")));
				sb.append(p2);
				// Nombre de la funcionalidad, codificada como "character entities" de HTML
				sb.append(Encoder.encode(tmp));
				sb.append(p3);
			}
		}

		Connection con = ans.getConnection();
		if (con != null && !con.isClosed()) {
			UtilidadBD.closeConnection(con);
			con = null;
		}

		return sb.toString();

	}


	/**
	 * Añade el nombreRol actualmente elegido a la lista de roles de la
	 * aplicación.
	 */
	public synchronized void anadirRol() {
		if (nombreRol != null && !nombreRol.trim().equals("")) {
			roleEditor.insertRole(new Rol(UtilidadTexto.removeAccents(comentarioRol).trim(), UtilidadTexto.flattenString(nombreRol).trim()));
		}
	}

	/**
	 * Quita el rol borrarRol (el actualmente elegido para ser eliminado) de la
	 * aplicación, y luego remueve todas las referencias a este rol que puedan
	 * estar presentes en la lista de roles- funcionalidades; cuidando de no
	 * permitir problemas de concurrencia. Esto es necesario pq varios objetos
	 * tienen una referencia a la lista de roles- funcionalidades, y deben
	 * liberar el "lock" sobre ésta antes de poder eliminar un objeto
	 * Funcionalidad.
	 */
	public synchronized void quitarRol() {

		String[] resp;
		Funcionalidad f;
		roleEditor.quitarRole(this.borrarRol.trim());

		try {

			Iterator i = roleEditor.getFuncionalidades().iterator();

			while (i.hasNext()) {

				f = (Funcionalidad) i.next();
				resp = UtilidadTexto.separarNombresDeCodigos(f.getFuncName(), 1);

				// Las funcionalidades con código "0" son internas a la aplicación y no deben removerse
				if (f.getRoleName().equals(borrarRol) && !resp[0].equals("0")) {
					roleEditor.getFuncionalidades().remove(f);
				}

			}

		}

		/* Esta excepción puede ocurrir si otro objeto tiene un lock sobre la lista de funcionalidades,
		 * que impidiría que se elimine una de las funcionalidades. Ante esta
		 * situación, duermo este Thread 10 milisegundos y vuelvo a intentar
		 * hasta que obtenga un lock sobre la lista y pueda borrar la
		 * funcionalidad deseada.
		 */
		catch (ConcurrentModificationException cme) {
			try {
				Thread.sleep(10);
			}
			catch (Exception e) {
			}
			quitarRol();
		}

	}

	/**
	 * Indica si puedo o no borrar el rol actualmente seleccionado para ser
	 * borrado (borrarRol) de la lista de roles. Un rol sólo puede ser borrado
	 * si ningún usuario lo tiene asignado.
	 * @return <b>true</b> si puedo borrar el rol, <b>false</b> si no
	 */
	public synchronized boolean puedoBorrarRol() throws SQLException {
		return rolesFuncsDao.puedoBorrarRol(this.borrarRol.trim());
	}

	/**
	 * Añade un nuevo objeto Funcionalidad (pareja de rol-funcionalidad) a la
	 * lista de funcionalidades, a partir de la funcionalidad, rol y opción de
	 * ssl actualmente elegidas.
	 */
	public synchronized void anadirFuncionalidad() {

		String[] resp = UtilidadTexto.separarNombresDeCodigos(funcionalidad, 2);
		boolean isSSL = (new Boolean(ssl)).booleanValue();

		/* Si se cambia el mensaje por defecto en el JSP ("Seleccione una Funcionalidad"), aquí 
		   también debe cambiarse la comparación respectiva */
		if (!funcionalidad.equals("Seleccione una Funcionalidad") && !rol.equals("Seleccione un Rol")) {
			roleEditor.insertFuncionalidad(new Funcionalidad((resp[0] + "-" + resp[1]).trim(), resp[2].trim(), rol.trim(), isSSL));
		}

	}

	/**
	 * Quita un objeto Funcionalidad, a partir de el rol y la funcionalidad
	 * elegidos actualmente.
	 */
	public synchronized void quitarFuncionalidad() {
		String[] resp = UtilidadTexto.separarNombresDeCodigos(funcionalidad, 2);
		this.restriccionesHuerfanas+=resp[0]+",";
		roleEditor.quitarFuncionalidad(rol.trim(), (resp[0] + "-" + resp[1]).trim());
		/*
		try
		{
		    this.eliminarHuerfanosEHijosPadreEliminadoYRol(resp[0], rol.trim());
		}
		catch (SQLException e)
		{
		    e.printStackTrace();
		}
		*/
	}

	/**
	 * Presenta una lista de loas funcionalidades asignadas al rol elegido
	 * actualmente. Esta lista será mostrada dentro de una textarea de HTML 
	 * @return lista de funcionalidades asignadas al rol actual
	 */
	public synchronized String funcsElegidas() 
	{
		String tmp = "";

		if (!this.rol.equals("Seleccione un Rol")) 
		{
			String[] resp;
			String codigoFuncHija="";
			Funcionalidad f;
			String tmp2 = "";
			boolean first = true;
			Iterator i = roleEditor.getFuncionalidades().iterator();
			tmp = "SSL  Funcionalidad\n---------------------------------------------------------\n";
			ArrayList arrayHijas= new ArrayList();
			////
			try
			{
			    arrayHijas=this.cargarListadoHijasSistema();
			}
			catch(SQLException sqle){};
			////
			while (i.hasNext()) 
			{
				f = (Funcionalidad) i.next();
				if (f.getRoleName().equals(this.rol)) 
				{
					resp = UtilidadTexto.separarNombresDeCodigos(f.getFuncName(), 1);
					for(int x=0; x<arrayHijas.size(); x++)
					{
					    codigoFuncHija=(arrayHijas.get(x).toString()).split("-")[0]+"";
					    if(codigoFuncHija.equals(resp[0]+""))
					    {
					        resp[0]="Hija";
					        x=arrayHijas.size();
					    }
					}
					
					if (!resp[0].equals("0")) 
					{
					    if(!resp[0].equals("Hija"))
					    {    
							tmp2 = (f.isSSL() ? "SI   " : "NO   ") + resp[1];
							if (!first) 
							{
								tmp += ("\n" + tmp2);
							}
							else 
							{
								tmp += tmp2;
								first = false;
							}
					    }	
					}
				}
			}
			tmp += "\n---------------------------------------------------------";
		}
		return tmp;
	}
	
	/**
	 * Método que carga en un Array todas las funcionalidades hijas del sistema,
	 * ojo hijas es diferente de huerfanas, una funcionalidad hija PUEDE ser huerfana.
	 * 
	 * @return
	 * @throws SQLException
	 */
	public ArrayList cargarListadoHijasSistema () throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    Connection con=myFactory.getConnection();
	    ArrayList resultados=rolesFuncsDao.listaFuncionalidadesHijas(con);
	    UtilidadBD.cerrarConexion(con);
	    return resultados;
	}
	
	/**
	 * Presenta una lista de las funcionalidades asignadas al rol elegido
	 * actualmente. Esta lista será usada para crear un arreglo de javascript.
	 * @return lista de funcionalidades asignadas al rol actual
	 */
	public synchronized String mostrarFuncionalidadesAsignadas() {

		Iterator i = roleEditor.getRoles().iterator(), j;
		String tmp = "", rolName = "";
		Funcionalidad f;
		String[] resp;

		while (i.hasNext()) {

			j = roleEditor.getFuncionalidades().iterator();
			rolName = ((Rol) i.next()).getNombre();
			tmp += "'";

			while (j.hasNext()) {

				f = (Funcionalidad) j.next();

				if (f.getRoleName().equals(rolName)) {

					resp = UtilidadTexto.separarNombresDeCodigos(f.getFuncName(), 1);

					if (!resp[0].equals("0")) {
						tmp += (f.isSSL() ? "SI   " : "NO   ") + resp[1] + "|";
					}

				}

			}

			tmp += "', ";

		}

		char[] tmpArray = tmp.toCharArray();
		int size = tmpArray.length;
		if (size > 2 && tmpArray[size - 2] == ',') {
			tmpArray[size - 2] = ' ';
		}
		tmp = (new String(tmpArray));
		return tmp.trim();

	}

	/**
	 * Método que se encarga de agregar todas las funcionalidades
	 * necesarias para el correcto comportamiento de las funcionalidades
	 * asignadas por el usuario (Ej. Atención Cita necesita Val.)
	 * 
	 * @throws SQLException
	 */
	private void agregarDependencias () throws SQLException
	{
		//Imprimir todos las funcionalidades dadas por roleEditor
		Iterator it=roleEditor.getFuncionalidades().iterator();
		
		ArrayList funcionalidadesDependientes=new ArrayList();
		Funcionalidad func;
		
		//HashMap para sacar los datos de dependencias sin 
		//necesidad de pasar conexiones al Dao
		HashMap dyna;
		
		//Iterador para recorrer todas las dependencias necesarias
		//para inserción
		Iterator it2;
		
		while (it.hasNext())
		{
			func=(Funcionalidad)it.next() ;
			String tmp[]=func.getFuncName().split("-", 2);
			this.rol=func.getRoleName();
			
			Collection col=rolesFuncsDao.obtenerFuncionalidadesDependientes(tmp[0]);
			it2=col.iterator();
			String codigoFunc, nombreFunc, pathCompleto;
			while (it2.hasNext())
			{
				dyna=(HashMap)it2.next();
			
				codigoFunc=(String)dyna.get("codigofunc");
				nombreFunc=(String)dyna.get("nombrefunc");
				pathCompleto=(String)dyna.get("pathcompleto");
				tmp=pathCompleto.split("/");
				pathCompleto="/" + tmp[0] +"/*"; 
				funcionalidadesDependientes.add(new Funcionalidad((codigoFunc + "-" + nombreFunc).trim(), pathCompleto.trim(), func.getRoleName(), false));
			}
		}

		it=funcionalidadesDependientes.iterator();
		while (it.hasNext())
		{
			func=(Funcionalidad)it.next();
			roleEditor.insertFuncionalidad(func);
		}
	}
	
	/**
	 * Metodo que elimina todas las funcionalidades huerfanas
	 * @throws SQLException
	 */
	private void eliminarHuerfanas () throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    Connection con=myFactory.getConnection();
	    ArrayList resultados=rolesFuncsDao.buscarHuerfanos(con,"");
	    String restriccionesCodigosAEliminar="-1,";
	    for (int i=0;i<resultados.size();i++)
	    {
	        Funcionalidad encont=(Funcionalidad)resultados.get(i);
	 	    roleEditor.getFuncionalidades().remove(encont);
	 	    restriccionesCodigosAEliminar+=encont.getFuncName().split("-")[0]+",";
	    }
	    restriccionesCodigosAEliminar+=this.restriccionesHuerfanas+" -1";
	    ///se vuelve a realizar el proceso para borrar los posibles hijos huerfanos
	    /// pero con la restrccion de los los codigos de las func anteriromente removidos
	    ArrayList segundosResultados= rolesFuncsDao.buscarHuerfanos(con, restriccionesCodigosAEliminar);
		for (int i=0;i<segundosResultados.size();i++)
		{
		    Funcionalidad encont=(Funcionalidad)segundosResultados.get(i);
		    roleEditor.getFuncionalidades().remove(encont);
		}
	    UtilidadBD.cerrarConexion(con);
	}
	
	/**
	 * Método para eliminar todos los huerfanos del sistema, incluyendo los hijos de algun padre que todavia esta en
	 * BD pero que ya ha sido seleccionado por el usuario en el boton 'Quitar' de la funcionalidad Crear Roles, 
	 * tambien filtrando en codigo java del sql el rol
	 */
	private void eliminarHuerfanosEHijosPadreEliminadoYRol (String codigoPadre, String rol) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    Connection con=myFactory.getConnection();
	    ArrayList resultados=rolesFuncsDao.busquedaHuerfanosEHijosPadreEliminadoYRol(con, codigoPadre, rol);
	    
	    for (int i=0;i<resultados.size();i++)
	    {
	        Funcionalidad encont=(Funcionalidad)resultados.get(i);
		    roleEditor.getFuncionalidades().remove(encont);
	    }
	    UtilidadBD.cerrarConexion(con);
	}

	/**
	 * Una vez que el usuario ha terminado de definir los roles y las
	 * funcionalidades asignadas a cada rol dentro de la aplicación, este método
	 * intentará guardar los cambios. Es de suma importancia entender que los
	 * cambios deben ocurrir en dos partes: en web.xml y en la BD, y que ambas
	 * partes deben tener información consistente entre ellas.
	 * @return <b>true</b> si pude guardar los cambios en la BD y en web.xml,
	 * <b>false</b> si no
	 */
	public synchronized boolean guardarCambios() throws SQLException {
		boolean sqlSaved = false, xmlSaved = false;
		
		this.eliminarHuerfanas();
		
		this.agregarDependencias();
		/* OJO : la invocación de este método cambia el estado interno de RoleEditor.Funcionalidades,
		   para que esté conforme a los requerimientos de web.xml. En particular, si hay por lo menos
		   UN rol que accede a una funcionalidad usando SSL, los demás roles también deberán
		   usar SSL para acceder dicha funcionalidad */

		roleEditor.setListaFuncRoles();

		/* El orden SI importa. Si algo sale mal durante la transacción en la BD, puedo hacer rollback.
		   Por otro lado, si primero escribo el xml y luego algo sale mal en la transacción con la BD,
		   ¿cómo podría hacer rollback en el archivo xml? (aparte de usar archivos temporales, claro...) */

		sqlSaved = rolesFuncsDao.guardarCambios(roleEditor.getRoles(), roleEditor.getFuncionalidades());

		if (sqlSaved) {
			xmlSaved = roleEditor.save();
		}

		return (sqlSaved && xmlSaved);

	}

	/**
	 * Elimina la propia referencia de este objeto del contexto de sesión. Nótese
	 * que sólo funciona si fue registrado con el nombre "rolFunc".
	 * @param pc contexto de la página desde la que se llama este método
	 */
	public void killSelf(PageContext pc) {
		pc.removeAttribute("rolFunc", PageContext.SESSION_SCOPE);
	}
  
    /**
     * @return Returns the restriccionesHuerfanas.
     */
    public String getRestriccionesHuerfanas() {
        return restriccionesHuerfanas;
    }
    /**
     * @param restriccionesHuerfanas The restriccionesHuerfanas to set.
     */
    public void setRestriccionesHuerfanas(String restriccionesHuerfanas) {
        this.restriccionesHuerfanas = restriccionesHuerfanas;
    }
}