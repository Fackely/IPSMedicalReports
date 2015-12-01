/*
 * @(#)MovimientoFacturasAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.action.cartera;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

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
import util.glosas.UtilidadesGlosas;

import com.princetonsa.actionform.cartera.MovimientoFacturasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cartera.MovimientoFacturas;

/**
 *   Action, controla todas las opciones dentro de la consulta de
 *   movimiento de facturas, incluyendo los posibles casos de error. 
 *   Y los casos de flujo.
 * @version 1.0, Abril 12, 2005
 * @author wrios
 */
public class MovimientoFacturasAction extends Action
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(MovimientoFacturasAction.class);
		
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
		try{
		if (response==null); //Para evitar que salga el warning
		if(form instanceof MovimientoFacturasForm)
		{
				
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}
						
				MovimientoFacturasForm movForm =(MovimientoFacturasForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				String estado=movForm.getEstado(); 
				String accion= movForm.getAccion();
				logger.warn("Estado MovimientoFacturas-->"+estado+" accion== "+accion);
				
				if(estado == null)
				{
					movForm.reset();	
					logger.warn("Estado no valido dentro del flujo de movimiento facturas (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar"))
				{
					return this.accionEmpezar(movForm,mapping, con, usuario);
				}
				else if(estado.equals("resultadoBusquedaAvanzada"))
				{
				    if(accion.equals("ordenar"))
				    {
				        return this.accionOrdenar(movForm,mapping,request,con);
				    }
				    else
				    {    
				        return this.accionResultadoBusquedaAvanzada(movForm,mapping,con, Integer.parseInt(usuario.getCodigoInstitucion()));
				    }   
				}
				else if(estado.equals("detalle"))
				{
				    return this.accionDetalle(movForm, mapping, con); 
				}
				else if(estado.equals("volver"))
				{
					movForm.setCodigoCentroAtencion(usuario.getCodigoCentroAtencion());
					movForm.setEstado("resultadoBusquedaAvanzada");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("mostrarDetalleGlosa"))
				{
					accionMostrarDetalleGlosa(movForm, con);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("detalleGlosa");
				}
				else
				{
					movForm.reset();
					logger.warn("Estado no valido dentro del flujo de movimiento facturas) ");
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
	 * 
	 * @param movForm
	 * @param mapping
	 * @param con
	 * @return
	 */
	private void accionMostrarDetalleGlosa(MovimientoFacturasForm movForm, Connection con) {
		movForm.setDetFacturaGlosa(UtilidadesGlosas.obtenerDetalleGlosaFactura(con, movForm.getCodigoGlosa()));
	}

	/**
	 * Método que especifica las acciones a realizar cunado se empieza el proceso
	 * de consulta de movimiento de facturas
	 * @param movForm
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar(	MovimientoFacturasForm movForm, 
																ActionMapping mapping, 
																Connection con,
																UsuarioBasico usuario) throws SQLException
	{
		//Limpiamos lo que venga del form
		movForm.reset();
		movForm.setCodigoCentroAtencion(usuario.getCodigoCentroAtencion());
		movForm.setEstado("empezar");
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");		
	}
	
	/**
	 * Accion a realizar para obtener la collection con los resultados de la busqueda
	 * @param movForm
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionResultadoBusquedaAvanzada(	MovimientoFacturasForm movForm, 
																							ActionMapping mapping, 
																							Connection con,
																							int codigoInstitucion) throws SQLException
	{
		MovimientoFacturas mundoMov= new MovimientoFacturas();
		mundoMov.reset();
		movForm.setEstado("resultadoBusquedaAvanzada");
		movForm.setAccion("");
		enviarItemsSeleccionadosBusqueda(movForm, mundoMov);
		movForm.setCol(mundoMov.resultadoBusquedaAvanzada(con, codigoInstitucion));
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Envia los valores pertinentes a los criterios de busqueda
	 * @param movForm
	 * @param mundoMov
	 */
	private void enviarItemsSeleccionadosBusqueda(MovimientoFacturasForm movForm, MovimientoFacturas mundoMov)
	{
	    //info de la empresa
	    mundoMov.setCodigoEmpresa(movForm.getCodigoEmpresa());
	    mundoMov.setDescripcionEmpresa(movForm.getDescripcionEmpresa());
	    //info del convenio
	    mundoMov.setCodigoConvenio(movForm.getCodigoConvenio());
	    mundoMov.setNombreConvenio(movForm.getNombreConvenio());
	    //info del rango de factura
	    mundoMov.setRangoInicialFactura(movForm.getFacturaRangoInicial());
	    mundoMov.setRangoFinalFactura(movForm.getFacturaRangoFinal());
	    //estado de la factura
	    mundoMov.setCodigoEstadoFactura(movForm.getCodigoEstadoFactura());
	    mundoMov.setDescripcionEstadoFactura("");
	    //info rango de fecha
	    mundoMov.setRangoInicialFechaFactura(movForm.getFechaRangoInicial());
	    mundoMov.setRangoFinalFechaFactura(movForm.getFechaRangoFinal());
	    //valor cartera
	    mundoMov.setRangoInicialValorFactura(movForm.getValorFacturaRangoInicial());
	    mundoMov.setRangoFinalValorFactura(movForm.getValorFacturaRangoFinal());
	    //No. cuenta cobro
	    mundoMov.setRangoInicialNumeroCuentaCobro(movForm.getNumeroCuentaCobroRangoInicial());
	    mundoMov.setRangoFinalNumeroCuentaCobro(movForm.getNumeroCuentaCobroRangoFinal());
	    
	    mundoMov.setCodigoCentroAtencion(movForm.getCodigoCentroAtencion());
	    
	    mundoMov.setEmpresaInstitucion(movForm.getEmpresaInstitucion());
	}
	
	/**
	 * Accion ue ordena la collection por alguno de sus atributos
	 * @param movForm
	 * @param mapping
	 * @param request
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionOrdenar(	MovimientoFacturasForm movForm,
																ActionMapping mapping,
																HttpServletRequest request, 
																Connection con) throws SQLException
	{
		try
		{
			movForm.setCol(Listado.ordenarColumna(new ArrayList(movForm.getCol()),movForm.getUltimaPropiedad(),movForm.getColumna()));
			movForm.setUltimaPropiedad(movForm.getColumna());
			UtilidadBD.cerrarConexion(con);
		}
		catch(Exception e)
		{
			logger.warn("Error en el ordenamiento del movimiento de facturas ");
			UtilidadBD.cerrarConexion(con);
			movForm.reset();
			ArrayList atributosError = new ArrayList();
			atributosError.add(" Listado movimiento de facturas");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");		
		}
		movForm.setAccion("");
		return mapping.findForward("principal")	;
	}	
	
	/**
	 * Flujo para mostrar l pagina del detalle de movimiento
	 * @param movForm
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionDetalle(	MovimientoFacturasForm movForm, 
															ActionMapping mapping, 
															Connection con) throws SQLException
	{
		//Limpiamos lo que venga del form
		movForm.reset();
		
		movForm.setGlosas(UtilidadesGlosas.obtenerGlosasFactura(con, movForm.getCodigoFactura()+""));
		
		movForm.setEstado("detalle");
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("detalle");		
	}
	
}
