/*
 * @(#)ConsultaProfesionalPoolAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.action.pooles;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.UtilidadBD;

import com.princetonsa.actionform.pooles.ConsultaProfesionalPoolForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.pooles.ConsultaProfesionalPool;

/**
 * Clase encargada del control de la funcionalidad de Consulta de Profesional Pool

 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 17 /Mar/ 2006
 */
public class ConsultaProfesionalPoolAction extends Action
{
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 */
	private Logger logger= Logger.getLogger(ConsultaProfesionalPoolAction.class);

	/**
	 * Método excute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{

		Connection con = null;
		try{

			if(form instanceof ConsultaProfesionalPoolForm)
			{


				/**Intentamos abrir una conexion con la fuente de datos**/ 
				con = openDBConnection(con); 
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				ConsultaProfesionalPoolForm forma=(ConsultaProfesionalPoolForm)form;
				ConsultaProfesionalPool mundo =new ConsultaProfesionalPool();
				String estado = forma.getEstado();
				logger.warn("[ConsultaProfesionalPoolAction] estado->"+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ConsultaProfesionalPoolAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}

				else if(estado.equals("empezar"))
				{
					return this.accionEmpezar(mapping, forma, con);
				}
				else if(estado.equals("consultar"))
				{
					return this.accionConsultar(forma, mapping, con, mundo);
				}

			}
			else
			{
				logger.error("El form no es compatible con el form de ConsultaProfesionalesPool");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				return mapping.findForward("paginaError");
			}	
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
}



	/**
	 * Accion para empezar en el flujo de la funcionalidad de consulta de pooles de los profesionales de la salud
	 * @param mapping
	 * @param forma
	 * @param con
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionEmpezar(ActionMapping mapping, ConsultaProfesionalPoolForm forma, Connection con) throws Exception
	{
		forma.reset();
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");
	}
	
	
	
	/**
	 * Accion para realizar la busqueda de los pooles de un profesional de la salud
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param mundo
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionConsultar(ConsultaProfesionalPoolForm forma, ActionMapping mapping, Connection con, ConsultaProfesionalPool mundo) throws SQLException
	{
		forma.setMapaConsultaPoolProfesional(mundo.consultaProfesionalPool(con, forma.getCodigoMedico()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");		
	}
	
	
	
	
	/**
	 * Abrir la conceccion con la Base de Datos
	 * @param con
	 * @return
	 */
	public Connection openDBConnection(Connection con)
	{
		if(con != null)
		{
			return con;
		}
		try
		{
			String tipoBD = System.getProperty("TIPOBD");
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			con = myFactory.getConnection();
		}
		catch(Exception e)
		{
			logger.warn("Problemas con la base de datos al abrir la conexion "+e.toString());
			return null;
		}
	
		return con;
	}
}