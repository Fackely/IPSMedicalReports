/*
 * @(#)ActivacionMedicoBean.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package util;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.princetonsa.mundo.Usuario;
import com.princetonsa.mundo.UsuarioBasico;

/**
 *  Esta clase es un bean que se utiliza para activar y desactivar Usuarios-
 *
 * @version 1.0, Jan 15, 2003
 * @author <a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s
 * &oacute;pez P.</a>
 * @author <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho
 * P.</a>,
 * @author <a href="mailto:Diego@PrincetonSA.com">Diego Ram&iacute;rez </a>
 */

public class ActivacionUsuarioBean {

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(ActivacionUsuarioBean.class);
	
	/**
	 * Arreglo de Strings donde se reciben los logins de todos los usuarios que
	 * se desean activar/desactivar.
	 */
	private String [] login;

	/**
	 * Método que limpia todos los atributos del bean
	 */
	public void clean()	{
		login = new String [0];
	}

	/**
	 * Método para activar todos los usuarios cuyo login  esta en el bean
	 * @param con una conexion abierta con una fuente de datos
	 * @param tipoBD nombre del tipo de Base de Datos utilizada
	 * @param codigoInstitucion El código de la institución donde permanece el
	 * usuario inactivo
	 * @return String[] con información util a mostrar al usuario
	 */

	public String[] activarUsuarios (Connection con, String tipoBD, String codigoInstitucion,String usuActual) throws SQLException
	{

		String respuesta[] ;
		if (codigoInstitucion==null||codigoInstitucion.equals("")||login==null||login.length<1)
		{
			respuesta=new String [1];
			respuesta[0]="Por Favor seleccione alg&uacute;n Usuario";
			return respuesta;
		}

		Usuario us=new Usuario();
		respuesta=new String [login.length];
		us.init(tipoBD);
		int i;
		for (i=0;i<login.length;i++)
		{
			if (us.activarUsuario(con,login[i],codigoInstitucion,usuActual) <1)
				respuesta[i]="Usuario con login " + login[i] + " no pudo ser activado";
			else
				respuesta[i]="Usuario con login " + login[i] + " fue activado correctamente";
		}
		return respuesta;
	}

	public String[] activarUsuarios (Connection con, String tipoBD, String codigoTipoIdentificacion[], String numeroIdentificacion[], String codigoInstitucion,String usuActual) throws SQLException
	{

		String respuesta[] ;
		if (codigoInstitucion==null||codigoInstitucion.equals("")||numeroIdentificacion==null||numeroIdentificacion.length<1||codigoTipoIdentificacion==null||codigoTipoIdentificacion.length<1)
		{
			respuesta=new String [1];
			respuesta[0]="Por Favor seleccione alg&uacute;n Usuario";
			return respuesta;
		}
		Usuario us=new Usuario();
		respuesta=new String [login.length];
		us.init(tipoBD);
		int i;
		//activarUsuario (Connection con, String tipoId, String numeroId, String codigoInstitucion)
		for (i=0;i<login.length;i++)
		{
			if (us.activarUsuario(con,codigoTipoIdentificacion[i], numeroIdentificacion[i],codigoInstitucion,usuActual) <1)
				respuesta[i]="Usuario con login " + login[i] + " no pudo ser activado";
			else
				respuesta[i]="Usuario con login " + login[i] + " fue activado correctamente";
		}
		return respuesta;
	}


	/**
	 * Método para desactivar todos los usuarios cuyo login  esta en el bean
	 * @param con una conexion abierta con una fuente de datos
	 * @param tipoBD nombre del tipo de Base de Datos utilizada
	 * @param codigoInstitucion El código de la institución donde permanece el
	 * usuario inactivo
	 * @return String[] con información util a mostrar al usuario
	 */
	public String[] desactivarUsuarios (Connection con, String tipoBD, String codigoInstitucion,String usuActual) throws SQLException
	{

		String respuesta[] ;
		if (codigoInstitucion==null||codigoInstitucion.equals("")||login==null||login.length<1)
		{
			respuesta=new String [1];
			respuesta[0]="Por Favor seleccione alg&uacute;n Usuario";
			return respuesta;
		}

		respuesta=new String [login.length];
		
		int i;
		for (i=0;i<login.length;i++)
		{
			//primero se intenta desactivar el médico en el
			//caso de que el usuario sea médico y ese médico
			//solo tenga un usuario
			respuesta[i]=this.desactivacion(con,login[i],tipoBD,usuActual);
		}
		return respuesta;
	}

	/**
	 * Método usado para verificar si el ususario a desactivar
	 * es un médico, se verifica también si en el caso de que sea médico
	 * tenga más usuarios y en el caso de que solo tenga un usuario
	 * se desactiva el médico también, en el caso de que tenga más de 1
	 * usuario no se deasctiva el médico, pero el usuario sí.
	 * @param con
	 * @param login
	 * @param tipoBD
	 */
	private String desactivacion(Connection con, String login, String tipoBD,String usuActual) 
	{
		String respuesta="";
		try
		{
			
			
			//se carga usuario
			UsuarioBasico usuario = new UsuarioBasico();
			usuario.cargarUsuarioBasico(con,login);
			
			//se verifica que el usuario no tenga cajas asociadas
			if(Utilidades.numCajasAsociadasUsuario(usuario.getLoginUsuario())<=0)
			{
				//se verifica si es medico
				boolean esMedico=UtilidadValidacion.existeMedico(con,tipoBD,usuario.getCodigoTipoIdentificacion(),usuario.getNumeroIdentificacion());
				if(esMedico)
				{
					Collection usuarios=usuario.cargarUsuariosMismoProfesional(con,usuario.getCodigoTipoIdentificacion(),usuario.getNumeroIdentificacion(),true);
					//si solo hay un usuario se desactiva el médico también
					if(usuarios.size()==1)
					{
						//se verifica que el medico se encuentre inactivo
						if(!UtilidadValidacion.estaMedicoInactivo(con,usuario.getCodigoPersona(),usuario.getCodigoInstitucionInt()))
						{	
						
							String aux[]=new String[1];
							ActivacionMedicoBean activacionMedico=new ActivacionMedicoBean();
							aux[0]=usuario.getCodigoPersona()+"";
							activacionMedico.setIdentificacion(aux);
							activacionMedico.inicializarVariables();
							String[] resultado=activacionMedico.desactivarMedicos(con,tipoBD,usuario.getCodigoInstitucion());
							
							logger.info("Res: " + resultado);
							Utilidades.imprimirArreglo(resultado);
							
							if(resultado[0].endsWith("correctamente"))
								respuesta=this.desactivarSoloUnUsuario(con,login,tipoBD,usuario.getCodigoInstitucion(),usuActual);
							else {
								respuesta = "Usuario con login " + login + " no pudo ser desactivado."+ConstantesBD.separadorSplit+ resultado[0];
							}
						}
						else
							respuesta=this.desactivarSoloUnUsuario(con,login,tipoBD,usuario.getCodigoInstitucion(),usuActual);
					}
					else
						respuesta=this.desactivarSoloUnUsuario(con,login,tipoBD,usuario.getCodigoInstitucion(),usuActual);
				}
				else
					respuesta=this.desactivarSoloUnUsuario(con,login,tipoBD,usuario.getCodigoInstitucion(),usuActual);
			}
			else
				respuesta="Usuario con login " + login + " no pudo ser desactivado porque se encuentra como cajero activo";
			
			return respuesta;
		}

		catch(SQLException e)
		{
			respuesta="Usuario con login " + login + " no pudo ser desactivado. Ya se encuentra Inactivado";
			logger.error("Error en desactivacion de ActivacionUsuarioBean: "+e);
			return respuesta;
		}
	}

	/**
	 * Método para desactivar un usuario
	 * @param con
	 * @param login
	 * @param tipoBD
	 * @param codigoInstitucion
	 * @param 
	 * @return
	 */
	private String desactivarSoloUnUsuario(Connection con, String login,String tipoBD, String codigoInstitucion,String usuActual) 
	{
		String respuesta="";
		try
		{
			Usuario us=new Usuario();
			us.init(tipoBD);
			//se desactiva el usuario
			int resp=us.desactivarUsuario(con,login,codigoInstitucion,usuActual);
			if (resp < 1)
				respuesta="Usuario con login " + login + " no pudo ser desactivado. Ya se encuentra Inactivo.";
			else
				respuesta="Usuario con login " + login + " fue desactivado correctamente";
			return respuesta;
		}
		catch(SQLException e)
		{
			respuesta="Usuario con login " + login + " no pudo ser desactivado. Ya se encuentra Inactivo.";
			logger.error("Error en desactivarSoloUnUsuario de ActivacionUsuarioBean: "+e);
			return respuesta;
		}
	}

	public String[] desactivarUsuarios (Connection con, String tipoBD, String codigoTipoIdentificacion[], String numeroIdentificacion[], String codigoInstitucion,String usuActual) throws SQLException
	{

		String respuesta[] ;
		if (codigoInstitucion==null||codigoInstitucion.equals("")||login==null||login.length<1)
		{
			respuesta=new String [1];
			respuesta[0]="Por Favor seleccione alg&uacute;n Usuario";
			return respuesta;
		}

		respuesta=new String [login.length];
		Usuario us=new Usuario();
		us.init(tipoBD);
		int i;

		for (i=0;i<login.length;i++)
		{
			if (us.desactivarUsuario(con,codigoTipoIdentificacion[i], numeroIdentificacion[i],codigoInstitucion,usuActual) <1)
			respuesta[i]="Usuario con login " + login[i] + " no pudo ser desactivado";

			else
				respuesta[i]="Usuario con login " + login[i] + " fue desactivado correctamente";
		}
		return respuesta;
	}

	/**
	 * Retorna  un arreglo de String que contiene los login de los usuarios a
	 * activar / desactivar.
	 * @return String[] con un arreglo de String que contiene los login de los
	 * usuarios
	 */
	public String[] getLogin() {
		return login;
	}

	/**
	 * Establece el arreglo de Strings que contienen el conjunto de login.
	 * @param login El arreglo de login a establecer
	 */
	public void setLogin(String[] login) {
		this.login = login;
	}

}