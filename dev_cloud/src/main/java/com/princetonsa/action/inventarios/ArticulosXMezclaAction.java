/**
 * Juan David Ramírez 31/05/2006
 * Princeton S.A.
 */
package com.princetonsa.action.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadTexto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.inventarios.ArticulosXMezclaForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.ArticulosXMezcla;

/**
 * 
 * @author Juan David Ramírez
 *
 */
public class ArticulosXMezclaAction extends Action
{
	/**
	 * Manejador de logs de la clase
	 */
	Logger logger = Logger.getLogger(ArticulosXMezclaAction.class);

	/**
	 * Método que ejecuta el flujo de la funcionalidad
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Connection con =null;
		try
		{

			if (form instanceof ArticulosXMezclaForm)
			{
				UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");

				ArticulosXMezclaForm forma = (ArticulosXMezclaForm) form;
				con = UtilidadBD.abrirConexion();

				String estado = forma.getEstado();

				logger.warn("Estado ArticulosXMezclaAction " + estado);

				if (estado.equalsIgnoreCase("empezar"))
				{
					return accionEmpezar(con, mapping, usuario, request, forma);
				}
				else if (estado.equalsIgnoreCase("consultar"))
				{
					return accionEmpezar(con, mapping, usuario, request, forma);
				}
				else if (estado.equalsIgnoreCase("cambiarMezcla"))
				{
					return accionCambiarMezcla(con, mapping, usuario, request, forma);
				}
				else if (estado.equalsIgnoreCase("agregarNuevo"))
				{
					return accionAgregarNuevo(con, mapping, forma);
				}
				else if (estado.equalsIgnoreCase("eliminar"))
				{
					return accionEliminar(con, mapping, forma);
				}
				else if (estado.equalsIgnoreCase("guardar"))
				{
					return accionGuardar(con, mapping, usuario, request, forma);
				}
				else if (estado.equalsIgnoreCase("ordenar"))
				{
					cerrarConexion(con);
					return accionOrdenar(forma, mapping);
				}
				return ComunAction.accionSalirCasoError(mapping, request, null, logger, "errors.estadoInvalido", "errors.estadoInvalido", true);
			}
			return ComunAction.accionSalirCasoError(mapping, request, null, logger, "errors.formaTipoInvalido", "errors.formaTipoInvalido", true);

		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
}
	
	/**
	 * Método para ordenar por columnas
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenar(ArticulosXMezclaForm forma, ActionMapping mapping)
	{
		String[] indices={"articulo_", "nombre_"};
		HashMap mapaArticulos=forma.getMapaArticulos();
		int numRegistros=Integer.parseInt(mapaArticulos.get("numRegistros").toString());
		mapaArticulos=Listado.ordenarMapa(indices, forma.getPropiedad(), forma.getUltimaPropiedad(), mapaArticulos, numRegistros);
		mapaArticulos.put("numRegistros", numRegistros);
		forma.setMapaArticulos(mapaArticulos);
		if(!forma.getUltimaPropiedad().equalsIgnoreCase(forma.getPropiedad()))
		{
			forma.setUltimaPropiedad(forma.getPropiedad());
		}
		else
		{
			forma.setUltimaPropiedad("");
		}
		return mapping.findForward("principal");
	}

	/**
	 * Método para guardar los cambios en la funcionalidad
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @param forma
	 * @return
	 */
	private ActionForward accionGuardar(Connection con, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request, ArticulosXMezclaForm forma)
	{
		ArticulosXMezcla mundo=new ArticulosXMezcla();
		HashMap mapaArticulos=forma.getMapaArticulos();
		int mezcla=forma.getMezcla();
		HashMap mapaBD=mundo.consultar(con, usuario.getCodigoInstitucionInt(), mezcla);
		if(mapaBD==null)
		{
			return ComunAction.accionSalirCasoError(mapping, request, null, logger, "errors.problemasBd", "errors.problemasBd", true);
		}
		int numRegistros= Integer.parseInt(mapaArticulos.get("numRegistros").toString());
		int numRegistrosBD=  Integer.parseInt(mapaBD.get("numRegistros").toString());
		
		Vector insertados=new Vector();
		
		for(int i=0; i<numRegistros; i++)
		{
			boolean esInsertado=true;
			int articulo=Integer.parseInt(mapaArticulos.get("articulo_"+i).toString());
			for(int j=0; j<numRegistrosBD; j++)
			{
				int articuloBD=Integer.parseInt(mapaBD.get("articulo_"+j).toString());
				if(articulo==articuloBD)
				{
					esInsertado=false;
					break;
				}
			}
			if(esInsertado)
			{
				insertados.add(articulo);
			}
		}

		String log="";

		
		Vector eliminados=new Vector();
		for(int i=0; i<numRegistrosBD; i++)
		{
			boolean esEliminado=true;
			int articuloBD= Integer.parseInt(mapaBD.get("articulo_"+i).toString());
			for(int j=0; j<numRegistros; j++)
			{
				int articulo=Integer.parseInt(mapaArticulos.get("articulo_"+j).toString());
				if(articulo==articuloBD)
				{
					esEliminado=false;
					break;
				}
			}
			if(esEliminado)
			{
				eliminados.add(articuloBD);
				log+="\n            ====REGISTRO ELIMINADO===== " +
				"\n*  Código Articulo ["+articuloBD+"] "+
				"\n*  Nombre Articulo ["+mapaBD.get("nombre_"+i)+"] ";

			}
		}
		
		LogsAxioma.enviarLog(ConstantesBD.logArticulosXMezclaCodigo, log, ConstantesBD.tipoRegistroLogEliminacion, usuario.getLoginUsuario());
				
		mundo.ingresarModificar(con, insertados, eliminados, mezcla);
		
		forma.setMapaArticulos(mundo.consultar(con, usuario.getCodigoInstitucionInt(), mezcla));

		forma.setUltimaPropiedad("");
		forma.setPropiedad("");
		
		cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Eliminar un registro del mapa
	 * @param con
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionEliminar(Connection con, ActionMapping mapping, ArticulosXMezclaForm forma)
	{
		logger.info("mapaArticulos-->"+forma.getMapaArticulos());
		int numRegistros=Integer.parseInt(forma.getMapaArticulos("numRegistros").toString());
		for (int i = forma.getEliminado(); i <numRegistros ; i++)
		{
			try
			{
				logger.info("articulo->"+forma.getMapaArticulos("articulo_"+(i+1)).toString());
				if(UtilidadTexto.isEmpty(forma.getMapaArticulos("articulo_"+(i+1)).toString()))
				{
					logger.info("1");
					forma.removeMapaArticulos("articulo_"+i);
					logger.info("2");
					forma.removeMapaArticulos("nombre_"+i);
				}
				else
				{
					logger.info("3");
					forma.setMapaArticulos("articulo_"+i, forma.getMapaArticulos("articulo_"+(i+1)).toString());
					logger.info("4");
					forma.setMapaArticulos("nombre_"+i, forma.getMapaArticulos("nombre_"+(i+1)).toString());
				}
				logger.info("sale!!");
			}
			catch (Exception e) 
			{
				forma.removeMapaArticulos("articulo_"+i);
				logger.info("2");
				forma.removeMapaArticulos("nombre_"+i);
			}
		}
		numRegistros--;
		forma.setMapaArticulos("numRegistros", numRegistros+"");
		forma.setUltimaPropiedad("");
		forma.setPropiedad("");
		cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Agregar un nuevo registro al mapa
	 * @param con
	 * @param mapping
	 * @param forma
	 * @return
	 */
	private ActionForward accionAgregarNuevo(Connection con, ActionMapping mapping, ArticulosXMezclaForm forma)
	{
		logger.info("mapaArticulos-->"+forma.getMapaArticulos());
		forma.setUltimaPropiedad("");
		forma.setPropiedad("");
		cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método que busca los articulos dada una mezcla específica
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @param forma
	 * @return
	 */
	private ActionForward accionCambiarMezcla(Connection con, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request, ArticulosXMezclaForm forma)
	{
		ArticulosXMezcla mundo=new ArticulosXMezcla();
		int mezcla=forma.getMezcla();
		if(mezcla!=0)
		{
			HashMap mapaArticulos=mundo.consultar(con, usuario.getCodigoInstitucionInt(), mezcla);
			forma.setListadoArticulos(mundo.consultarTipos(con, usuario.getCodigoInstitucionInt(), 2));
			if(mapaArticulos==null)
			{
				return ComunAction.accionSalirCasoError(mapping, request, null, logger, "errors.problemasBd", "errors.problemasBd", true);
			}
			forma.setMapaArticulos(mapaArticulos);
			int numRegistros=Integer.parseInt(mapaArticulos.get("numRegistros").toString());
			if(numRegistros>0)
			{
				forma.setUltimaPropiedad("articulo_");
				forma.setPropiedad("articulo_");
			}
			else
			{
				forma.setUltimaPropiedad("");
				forma.setPropiedad("");
			}
		}
		cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método para manejo del flujo
	 * al entrar a la funcionalidad
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param request
	 * @param forma 
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request, ArticulosXMezclaForm forma)
	{
		ArticulosXMezcla mundo=new ArticulosXMezcla();
		forma.reset();
		Collection mezclas=mundo.consultarTipos(con, usuario.getCodigoInstitucionInt(), 1);
		forma.setListadoMezclas(mezclas);
		if(mezclas.size()==1)
		{
			Iterator iterador=mezclas.iterator();
			if(iterador.hasNext())
			{
				HashMap fila=(HashMap)iterador.next();
				forma.setMezcla(Integer.parseInt(fila.get("consecutivo")+""));
				return accionCambiarMezcla(con, mapping, usuario, request, forma);
			}
		}
		cerrarConexion(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método para cerrar la conexión
	 * @param con
	 */
	private void cerrarConexion(Connection con)
	{
		try
		{
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e)
		{
			logger.error("Error cerrando la conexión "+e);
		}
	}
}
