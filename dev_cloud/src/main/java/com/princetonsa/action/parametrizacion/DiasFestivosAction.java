/*
 * TarifasInventarioAction.java 
 * Autor		:  Juan David Ramírez
 * Creado el	:  12-Abr-2005
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.2_04
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 * */
package com.princetonsa.action.parametrizacion;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import com.princetonsa.actionform.parametrizacion.DiasFestivosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.parametrizacion.DiasFestivos;

/**
 * 
 * @author Juan David Ramírez López
 *
 * Princeton S.A.
 */
public class DiasFestivosAction extends Action
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(DiasFestivosAction.class);

	/**
	 * Cierra la conexión en el caso que esté abierta
	 * @param con
	 * @throws SQLException
	 */
	private void cerrarConexion(Connection con) 
	{
		try
		{
			if(con!=null && !con.isClosed())
				UtilidadBD.closeConnection(con);
		}
		catch(SQLException e)
		{
			logger.error("no se pudo cerrar la conexion con la base de datos: "+e );
		}
	}

	/**
	 * Método execute del action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		Connection con = null;
		
		try
		{
			DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			con = myFactory.getConnection();
		}
		catch(Exception e)
		{
			logger.error("Problemas con la base de datos "+e);
			request.setAttribute("codigoDescripcionError", "errors.problemasBd");
			return mapping.findForward("paginaError");
		}
		
		UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
		if( usuario == null )
		{
			this.cerrarConexion(con);				
			request.setAttribute("codigoDescripcionError", "errors.usuario.noCargado");
			return mapping.findForward("paginaError");				
		}		

		if(form instanceof DiasFestivosForm)
		{
			DiasFestivosForm forma=(DiasFestivosForm)form;
			DiasFestivos diasFestivos=new DiasFestivos();
			String estado=forma.getEstado();
			
			logger.warn("Estado --> "+estado);
			
			if(estado.equals("ordenar"))
			{
				cerrarConexion(con);
				return accionOrdenar(forma, mapping);
			}
			if(estado.equals("empezar"))
			{
				return accionEmpezar(con, forma, diasFestivos, mapping);
			}
			else if(estado.equals("guardar"))
			{
				return accionGuardar(con, forma, diasFestivos, mapping);
			}
			else if(estado.equals("guardarModificacion"))
			{
				String log=llenadoLog(forma, true);
				llenarMundo(forma, diasFestivos);
				diasFestivos.modificar(con);
				cerrarConexion(con);
				return mapping.findForward("inicio");
			}
			else if(estado.equals("eliminar"))
			{
				String log=llenadoLog(forma, false);
				llenarMundo(forma, diasFestivos);
				diasFestivos.eliminar(con);
				cerrarConexion(con);
				return mapping.findForward("inicio");
			}
			else if(estado.equals("consultar"))
			{
				forma.reset();
				forma.setListado(diasFestivos.listar(con, forma.getAnio()));
				cerrarConexion(con);
				return mapping.findForward("inicio");
			}
			else if(estado.equals("consultarNuevoAnio"))
			{
				forma.setListado(diasFestivos.listar(con, forma.getAnio()));
				cerrarConexion(con);
				return mapping.findForward("inicio");
			}
		}
		this.cerrarConexion(con);
		request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
		return mapping.findForward("paginaError");			
	}

	private ActionForward accionOrdenar(DiasFestivosForm forma, ActionMapping mapping)
	{
		try
		{
			forma.setListado(Listado.ordenarColumna(new ArrayList(forma.getListado()),forma.getUltimaColumna(),forma.getColumna()));
			forma.setUltimaColumna(forma.getColumna());
		}
		catch (IllegalAccessException e)
		{
			logger.error("Error ordenando el listado : "+e);
		}
		return mapping.findForward("inicio");
	}

	/**
	 * Método que controla el flujo para guardar un día festivo
	 * @param con
	 * @param forma
	 * @param diasFestivos
	 * @param mapping
	 * @return
	 */
	private ActionForward accionGuardar(Connection con, DiasFestivosForm forma, DiasFestivos diasFestivos, ActionMapping mapping)
	{
		llenarMundo(forma, diasFestivos);
		diasFestivos.insertar(con);
		cerrarConexion(con);
		return mapping.findForward("inicio");
	}

	/**
	 * Método que controla el flujo de inicio de la funcionalidad
	 * @param con Conexión con la BD
	 * @param forma
	 * @param mapping
	 * @param diasFestivos
	 * @return Página de inicio
	 */
	private ActionForward accionEmpezar(Connection con, DiasFestivosForm forma, DiasFestivos diasFestivos, ActionMapping mapping)
	{
		diasFestivos.reset();
		forma.reset();
		cerrarConexion(con);
		return mapping.findForward("inicio");
	}

	/**
	 * Método que crea el log
	 * @param forma
	 * @param esModificacion
	 * @return
	 */
	private String llenadoLog(DiasFestivosForm forma, boolean esModificacion)
	{
		String log;
		if(esModificacion)
		{
			log=" -================= INFORMACIÓN ORIGINAL =================-\n";
		}
		else
		{
			log=" -================= INFORMACIÓN ELIMINADA =================-\n";
		}
		log+=
			" Fecha\t\t\t\t["+forma.getFechaAnt()+"]\n"+
			" Descripción\t\t\t["+forma.getDescAnterior()+"]\n"+
			" Tipo\t\t\t\t["+forma.getTipoAnterior()+"]\n\n";

		if(esModificacion)
		{
			log+=" -===========INFORMACIÓN DESPUES DE LA MODIFICACION ===========-\n"+
			" Fecha\t\t\t\t["+forma.getFechaAnt()+"]\n"+
			" Descripción\t\t\t["+forma.getDescAnterior()+"]\n"+
			" Tipo\t\t\t\t["+forma.getTipoAnterior()+"]\n\n";
		}
		return log;
	}

	/**
	 * Método que copia las propiedades del mundo a la forma
	 * @param forma
	 * @param diasFestivos
	 */
	private void llenarMundo(DiasFestivosForm forma, DiasFestivos diasFestivos)
	{
		try
		{
			PropertyUtils.copyProperties(diasFestivos, forma);
			diasFestivos.setFecha(UtilidadFecha.conversionFormatoFechaABD(diasFestivos.getFecha()));
		}
		catch (Exception e)
		{
			logger.error("Error copiando las propiedades del mundo a la forma : "+e);
		}
	}

	/*
	 * Todavía no utilizo este método 
	/**
	 * Método que copia las propiedades de la forma al mundo
	 * @param forma
	 * @param diasFestivos
	 */
	/*private void llenarForm(DiasFestivosForm forma, DiasFestivos diasFestivos)
	{
		try
		{
			PropertyUtils.copyProperties(forma, diasFestivos);
			forma.setFecha(UtilidadFecha.conversionFormatoFechaAAp(forma.getFecha()));
		}
		catch (Exception e)
		{
			logger.error("Error copiando las propiedades del mundo a la forma : "+e);
		}
	}*/

}
