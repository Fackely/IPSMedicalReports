/*
 * Marzo 28, del 2007
 */
package com.princetonsa.action.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.inventarios.ArticulosFechaVencimientoForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.ArticulosFechaVencimiento;
import com.princetonsa.pdf.ArticulosFechaVencimientoPdf;

/**
 * @author Sebastián Gómez 
 *
 * Clase usada para controlar los procesos de la funcionalidad
 * consulta de articulos x fecha de vencimiento
 */
public class ArticulosFechaVencimientoAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ArticulosFechaVencimientoAction.class);
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,
													ActionForm form,
													HttpServletRequest request,
													HttpServletResponse response ) throws Exception
													{

		Connection con=null;
		//SE ABRE CONEXION
		try{

			if (response==null); //Para evitar que salga el warning
			if(form instanceof ArticulosFechaVencimientoForm)
			{

				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				//OBJETOS A USAR
				ArticulosFechaVencimientoForm articuloForm =(ArticulosFechaVencimientoForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				String estado=articuloForm.getEstado(); 
				logger.warn("estado ArticulosFechaVencimientoAction-->"+estado);


				if(estado == null)
				{
					articuloForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Articulos x fecha de vencimiento (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar")||estado.equals("cambioFecha")||estado.equals("busquedaAvanzada"))
				{
					return accionEmpezar(con,articuloForm,mapping,usuario);
				}
				else if (estado.equals("ordenar"))
				{
					return accionOrdenar(con,articuloForm,mapping);
				}
				else if (estado.equals("redireccion"))
				{
					return accionRedireccion(con,articuloForm,response,mapping,request);
				}
				else if (estado.equals("imprimir"))
				{
					return accionImprimir(con,articuloForm,mapping,request,usuario);
				}
				else
				{
					articuloForm.reset();
					logger.warn("Estado no valido dentro del flujo de ArticulosFechaVencimientoAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
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
	 * Método implementado para efectuar la impresion 
	 * @param con
	 * @param articuloForm
	 * @param mapping
	 * @param request
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionImprimir(Connection con, ArticulosFechaVencimientoForm articuloForm, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) 
	{
		String nombreArchivo;
        Random r=new Random();
        nombreArchivo="/ArticulosFechaVencimiento_" + r.nextInt()  +".pdf";
        
        //Se instancia mundo de ArticulosFechaVencimiento
        ArticulosFechaVencimiento articulos = new ArticulosFechaVencimiento();
        articulos.setFechaCorte(articuloForm.getFecha());
        //se verifica si el listado viene de busqueda avanzada
        if(articuloForm.isBusquedaAvanzada())
        {
        	articulos.setCodigoArticulo(articuloForm.getCodigoArticulo());
        	articulos.setDescripcionArticulo(articuloForm.getDescripcionArticulo());
        }
        
        //se realiza la consulta
        HashMap consulta = articulos.consultaImpresionArticulosXFecha(con);
        consulta.put("descripcionBusqueda", articuloForm.isBusquedaAvanzada()?articuloForm.getDescripcionArticulo():"");
        consulta.put("codigoBusqueda", articuloForm.isBusquedaAvanzada()?articuloForm.getCodigoArticulo():"");
        consulta.put("fechaBusqueda", articuloForm.getFecha());
        //se manda la impresion
        ArticulosFechaVencimientoPdf.imprimir(con,ValoresPorDefecto.getFilePath() +nombreArchivo, consulta, usuario, request);
		
        UtilidadBD.closeConnection(con);
	    request.setAttribute("nombreArchivo", nombreArchivo);
	    return mapping.findForward("abrirPdf");
	}

	/**
	 * Método implementado para paginar el listado de articulos por fecha de vencimiento
	 * @param con
	 * @param articuloForm
	 * @param response
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionRedireccion(Connection con, ArticulosFechaVencimientoForm articuloForm, HttpServletResponse response, ActionMapping mapping, HttpServletRequest request) 
	{
		try
		{
			
		    UtilidadBD.cerrarConexion(con);
			response.sendRedirect(articuloForm.getLinkSiguiente());
			return null;
		}
		catch(Exception e)
		{
			logger.error("Error en accionRedireccion de ArticulosFechaVencimientoAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en ArticulosFechaVencimientoAction", "errors.problemasDatos", true);
		}
	}

	/**
	 * Método implementado para realizar la ordenacion del listado de articulos
	 * @param con
	 * @param articuloForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenar(Connection con, ArticulosFechaVencimientoForm articuloForm, ActionMapping mapping) 
	{
		//columnas del listado
		String[] indices = {
				"codigoArticulo_",
				"descripcionArticulo_",
				"unidadMedida_",
				"lote_",
				"fechaVencimiento_",
				"existencias_",
				"detalle_"
			};

		
		//Se pasa la fecha a formato BD
		for(int i=0;i<articuloForm.getNumListado();i++)
			articuloForm.setListado("fechaVencimiento_"+i,UtilidadFecha.conversionFormatoFechaABD(articuloForm.getListado("fechaVencimiento_"+i).toString()));
		
		articuloForm.setListado(Listado.ordenarMapa(indices,
				articuloForm.getIndice(),
				articuloForm.getUltimoIndice(),
				articuloForm.getListado(),
				articuloForm.getNumListado()));
		
		///Se pasa la fecha a formato Aplicacion
		for(int i=0;i<articuloForm.getNumListado();i++)
			articuloForm.setListado("fechaVencimiento_"+i,UtilidadFecha.conversionFormatoFechaAAp(articuloForm.getListado("fechaVencimiento_"+i).toString()));
		
		
		articuloForm.setListado("numRegistros",articuloForm.getNumListado()+"");
		
		articuloForm.setUltimoIndice(articuloForm.getIndice());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

	/**
	 * Método implementado para iniciar el flujo de la consulta de articulos x fecha de vencimiento
	 * @param con
	 * @param articuloForm
	 * @param mapping
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, ArticulosFechaVencimientoForm articuloForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		if(articuloForm.getEstado().equals("empezar"))
		{
			articuloForm.reset();
			//Por defecto se asigna la fecha actual
			articuloForm.setFecha(UtilidadFecha.getFechaActual());
			
			String maxPageItems = ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt());
			if(!maxPageItems.equals(""))
				articuloForm.setMaxPageItems(Integer.parseInt(maxPageItems));
			else
				articuloForm.setMaxPageItems(10);
		
			//se toma la fecha del sistema
			articuloForm.setFechaActual(articuloForm.getFecha());
		}
			
		//Se instancia objeto ArticulosFechaVencimiento
		ArticulosFechaVencimiento articulos = new ArticulosFechaVencimiento();
		articulos.setFechaCorte(articuloForm.getFecha());
		
		//Si es busqueda Avanzada se adicionan los campos
		if(articuloForm.getEstado().equals("busquedaAvanzada"))
		{
			articuloForm.setBusquedaAvanzada(true);
			articulos.setCodigoArticulo(articuloForm.getCodigoArticulo());
			articulos.setDescripcionArticulo(articuloForm.getDescripcionArticulo());
		}
		else
			articuloForm.setBusquedaAvanzada(false);
		
		articuloForm.setListado(articulos.consultarArticulosXFecha(con, usuario.getCodigoInstitucionInt()));
		articuloForm.setNumListado(Integer.parseInt(articuloForm.getListado("numRegistros").toString()));
		articuloForm.setOffset(0);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

}
