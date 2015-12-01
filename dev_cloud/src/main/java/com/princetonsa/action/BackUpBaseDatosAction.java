/*
 * @(#)BackUpBaseDatosAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.action;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.UtilidadBD;

import com.princetonsa.actionform.BackUpBaseDatosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;

/** 
 * Clase encargada del control de la funcionalidad de Back Up de la Base de Datos

 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 03 /Ago/ 2005
 */
public class BackUpBaseDatosAction extends Action
{
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 */
	private Logger logger= Logger.getLogger(BackUpBaseDatosAction.class);

	/**
	 * Método excute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		if(form instanceof BackUpBaseDatosForm)
	    {
	        Connection con = null;
		    
		    /**Intentamos abrir una conexion con la fuente de datos**/ 
			con = openDBConnection(con); 
			if(con == null)
			{
				request.setAttribute("codigoDescripcionError", "errors.problemasBd");
				return mapping.findForward("paginaError");
			}
			
			BackUpBaseDatosForm formBackUp=(BackUpBaseDatosForm)form;
		
			HttpSession session = request.getSession();
			UsuarioBasico medico= (UsuarioBasico)session.getAttribute("usuarioBasico");
			
			String estado = formBackUp.getEstado();
			logger.warn("[BackUpBaseDatosAction] estado->"+estado);
			
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo de BackUpBaseDatosAction (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				this.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
			
			else if(estado.equals("empezar"))
			{
				formBackUp.reset();
				this.cerrarConexion(con);
				return mapping.findForward("inicioBackUp");
			}
			
			else if(estado.equals("realizarBackUp"))
			{
				return this.accionRealizarBackUp(mapping, con, formBackUp, medico);
			}	
	    }
		else
		{
			logger.error("El form no es compatible con el form de Back Up de Base de Datos");
			request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
			return mapping.findForward("paginaError");
		}	
		return null;
	}



	/**
	 * Accion para realizar el back up de la base de datos
	 * @param mapping
	 * @param con
	 * @param formBackUp
	 * @param medico
	 * @return
	 */
	private ActionForward accionRealizarBackUp(ActionMapping mapping, Connection con, BackUpBaseDatosForm formBackUp,  UsuarioBasico medico)
	{
		if(formBackUp.getCriterio().equals("Local"))
		{
			/**PENDIENTE**/
		}
		else if(formBackUp.getCriterio().equals("Servidor"))
		{
			formBackUp.setIndicador(0);
			if(BackUpBaseDatos.realizarBackUp(ConstantesBD.logBackUpBaseDatosPostgresCodigo,medico.getLoginUsuario()))
			{
				formBackUp.setRealizoBackUp("SI");
				logger.info("El indicador SI==>"+formBackUp.getIndicador());
			}
			else
			{
				formBackUp.setRealizoBackUp("NO");
				logger.info("El indicador NO==>"+formBackUp.getIndicador());
			}
			formBackUp.setIndicador(1);
			logger.info("El indicador alf FINAl==>"+formBackUp.getIndicador());
		}
		
		this.cerrarConexion(con);
		return mapping.findForward("inicioBackUp");
	}
 
	   
		
		
		
	/**
	 * Abrir la conexion con la base de datos
	 * @param con
	 * @return
	 */
	 public Connection openDBConnection(Connection con)
		{

			if(con != null)
				return con;
			
			try{
				String tipoBD = System.getProperty("TIPOBD");
				DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
				con = myFactory.getConnection();
			}
			catch(Exception e)
			{
				logger.warn("Problemas con la base de datos al abrir la conexion");
				return null;
			}
		
			return con;
		}
	 
	 
	 /**
	  * Método en que se cierra la conexión (Buen manejo
	  * recursos), usado ante todo al momento de hacer un forward
	  * @param con Conexión con la fuente de datos
	  */
	 public void cerrarConexion (Connection con)
		{
				try
				{
					if (con!=null&&!con.isClosed())
					{
	                    UtilidadBD.closeConnection(con);
					}
				}
				catch(Exception e){
					logger.error("Error al tratar de cerrar la conexion con la fuente de datos Back Up de Base de Datos Action. \n Excepcion: " +e);
				}
		}		

}