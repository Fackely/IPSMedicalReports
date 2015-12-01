package com.princetonsa.action.parametrizacion;

import java.sql.Connection;
import java.util.HashMap;

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
import util.UtilidadBD;
import util.UtilidadSesion;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.parametrizacion.ServiciosAutomaticoForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.parametrizacion.Servicios;


/**
 * Clase para el manejo del automatico de la funcionalidad
 * @author Andres Silva Monsalve
 * aesilva@princetonsa.com
 */

public class ServiciosAutomaticoAction extends Action
{
	/**
	 * Objeto para manejar el log de la clase
	 * */	
	private Logger logger = Logger.getLogger(ServiciosAutomaticoAction.class);
	
	//
	private Servicios servicios;
		
	/**
	 * Metodo execute del action
	 * @param ActionMapping mapping
	 * @param ActionForm form
	 * @param HttpServletRequest request
	 * @param HttpServletResponse response
	 * @return ActionForward
	 * */
	public ActionForward execute(ActionMapping mapping, 
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response) throws Exception
								 {

		Connection con = null;
		try {
			if(response==null);
			if(form instanceof ServiciosAutomaticoForm)		 
			{
				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("CodigoDescripcionError","errors.problemasBd");
					return mapping.findForward("paginaError");				 
				}

				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				ServiciosAutomaticoForm forma = (ServiciosAutomaticoForm)form;
				String estado = forma.getEstado();

				logger.warn("n\n\nEl estado en ServiciosAutomaticoAction es------->"+estado+"\n");		 

				if (estado == null)
				{				 
					forma.reset();
					logger.warn("Estado no Valido dentro del flujo de Coberturas (null)");
					request.setAttribute("CodigDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginError");			  
				}			
				//
				else if(estado.equals("empezar"))
				{
					forma.reset();
					return this.accionEmpezarConsulta(con,forma, mapping,usuario);				 				 		  
				}

				//
				else if (estado.equals("redireccion"))
				{
					this.accioncargarServicio(forma);
					return UtilidadSesion.redireccionar("serviciosAutomaticos.jsp",ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getServiciosAutomaticosMap("numRegistros").toString()), response, request, "serviciosAutomaticos.jsp",false);				
				}
				else if (estado.equals("paginacion"))
				{
					return accionRedireccion(con,forma,response,mapping,request);
				}
				else if (estado.equals("ordenar"))
				{
					return accionOrdenar(con,forma,mapping);
				}

				else if(estado.equals("guardar"))
				{
					//					guardamos en BD.
					this.accionGuardarServicio(con,forma,usuario);
					//limipiamos el form
					//	forma.reset();
					//forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
					//this.accionConsultarServiciosSIRC(con, forma, mundo, usuario);

					forma.reset();
					return this.accionEmpezarConsulta(con, forma, mapping, usuario);
					//  return mapping.findForward("principal");
				}

			}
			return null;
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}
	//------------------------- Fin Forward
	
	/**
	 * Metodo para cargar 
	 */
	public ActionForward accionEmpezarConsulta(Connection con, ServiciosAutomaticoForm forma, ActionMapping mapping, UsuarioBasico usuario)
	{
		servicios = new Servicios();
		forma.setServiciosAutomaticosMap(servicios.cargarServiciosautomatico(con,ConstantesBD.acronimoSi));
		return mapping.findForward("principal");
	}
	
	/**
	 * 
	 * @param forma
	 * En el popup de busqueda se hace una seleccion, este metodo pasa los datos de esa selección al serviciosAutomaticos.jsp
	 * la idea es que pueda ingresar mas servicios a esta lista que se carguen automaticamente con el check box
	 * 
	 */
	public void accioncargarServicio(ServiciosAutomaticoForm forma)
	{
		servicios = new Servicios();
		int pos = Integer.parseInt(forma.getServiciosAutomaticosMap("numRegistros").toString());
		forma.setServiciosAutomaticosMap("codigo_"+(pos),forma.getServiciosAutomaticosMap("servicio_"+pos));
		forma.setServiciosAutomaticosMap("descripcion_"+(pos),forma.getServiciosAutomaticosMap("nomservicio_"+pos));
		forma.setServiciosAutomaticosMap("automatico_"+(pos),ConstantesBD.acronimoSi);	
		forma.setServiciosAutomaticosMap("automatico_old_"+(pos),ConstantesBD.acronimoNo);
		//Se aumenta la posición del registro para la nueva ubicación de la busqueda seleccionada
		pos= pos+1;
		forma.setServiciosAutomaticosMap("numRegistros",pos);
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * Llamo al HashMap que contiene los datos necesarios para la funcionalidad del automatico, tomo el total de registros que contienen el 
	 * automatico activo, recorro los registros y guardo el codigo y el estado del automatico en este caso acronimoBDsi en la base de datos
	 * para que se siga cargando. 
	 * @param usuario 
	 */
	private void accionGuardarServicio(Connection con, ServiciosAutomaticoForm forma, UsuarioBasico usuario) 
	{
		logger.info("Va en el accionGuardarServicio que pertenece al Action");
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		HashMap vo=new HashMap();
		servicios = new Servicios();
		int pos = Integer.parseInt(forma.getServiciosAutomaticosMap("numRegistros").toString());
		
		for(int i=0; i < pos; i++)
		{
					
			logger.info("posicion de I --->   "+i);
			logger.info("posicion de pos --->   "+pos);
			vo.put("codigo",forma.getServiciosAutomaticosMap("codigo_"+i));
			vo.put("automatico",forma.getServiciosAutomaticosMap("automatico_"+i));				
			transaccion=servicios.actualizarAutomatico(con, vo);
			
			if(transaccion)
			{
				if(!forma.getServiciosAutomaticosMap("automatico_"+i).toString().equals(forma.getServiciosAutomaticosMap("automatico_old_"+i).toString()))
				{
					HashMap mapaOriginal = new HashMap();
					mapaOriginal.put("codigo_0", forma.getServiciosAutomaticosMap("codigo_"+i));
					mapaOriginal.put("descripcion_0", forma.getServiciosAutomaticosMap("descripcion_"+i));
					mapaOriginal.put("automatico_0", forma.getServiciosAutomaticosMap("automatico_old_"+i));
					
					String[] indices = {"codigo_","descripcion_","automatico_","estaBD_"};
					Utilidades.generarLogGenerico(forma.getServiciosAutomaticosMap(), mapaOriginal, usuario.getLoginUsuario(), false, i, ConstantesBD.logServiciosAutomaticosCodigo, indices);
				}
				
			}
		}
	   
		UtilidadBD.finalizarTransaccion(con);
			
	}
	
	/**
	 * METODO QUE SE ENCARGA DE HACER EL PAGER
	 * @param con
	 * @param forma
	 * @param response
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionRedireccion(Connection con, ServiciosAutomaticoForm forma, HttpServletResponse response, ActionMapping mapping, HttpServletRequest request) 
	{
		try
		{
			
		    UtilidadBD.closeConnection(con);
		    forma.setEstado("empezar");
			response.sendRedirect(forma.getLinkSiguiente());
			return null;
		}
		catch(Exception e)
		{
			logger.error("Error en accionRedireccion : "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en serviciosAutomaticosAction", "errors.problemasDatos", true);
		}
	}
	
	
	
	/**
	 * Método que realiza la ordenacion del listado de servicios esteticos
	 * @param con
	 * @param forma
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenar(Connection con, ServiciosAutomaticoForm forma, ActionMapping mapping) 
	{
		//columnas del listado
		String[] indices = {
				"codigo_",
				"descripcion_",
				"automatico_",
				"automatico_old_"
			};
		
		int numRegistros = Integer.parseInt(forma.getServiciosAutomaticosMap("numRegistros").toString());
		logger.error("\n\n     COLUMNA : ==================>        "+forma.getColumna()+"\n");
		logger.error("\n\n     ULTIMA COLUMNA : ===========>        "+forma.getUltimaColumna()+"\n");		
		logger.error("\n\n     NUMERO DE REGISTROS ========>        "+numRegistros+"\n");
		logger.error("\n\n     INDICE : ===================>        "+indices+"\n");
		logger.error("\n\n     MAPA : =====================>        "+forma.getServiciosAutomaticosMap()+"\n");
		
		forma.setServiciosAutomaticosMap(Listado.ordenarMapa(indices,
				forma.getColumna(),
				forma.getUltimaColumna(),
				forma.getServiciosAutomaticosMap(),
				numRegistros));
		
		
		forma.setServiciosAutomaticosMap("numRegistros",numRegistros+"");
		
		forma.setUltimaColumna(forma.getColumna());
		forma.setEstado("empezar");
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	
					
}