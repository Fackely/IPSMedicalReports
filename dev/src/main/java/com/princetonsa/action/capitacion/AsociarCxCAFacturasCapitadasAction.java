/*
 * @(#)AsociarCxCAFacturasCapitadasAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.action.capitacion;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.Listado;
import util.UtilidadBD;

import com.princetonsa.actionform.capitacion.AsociarCxCAFacturasCapitadasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.capitacion.AsociarCxCAFacturasCapitadas;
import com.princetonsa.mundo.capitacion.CuentaCobroCapitacion;
import com.princetonsa.mundo.facturacion.Factura;

/**
 *   Action, controla todas las opciones dentro del registro de
 *   AsociarCxCAFacturasCapitadas, incluyendo los posibles casos de error. 
 *   Y los casos de flujo.
 * @version 1.0, Julio 31, 2006
 * @author wrios
 */
public class AsociarCxCAFacturasCapitadasAction extends Action
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(AsociarCxCAFacturasCapitadasAction.class);
		
	
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
		if(form instanceof AsociarCxCAFacturasCapitadasForm)
		{
			
			try
			{
				con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
			}
			catch(SQLException e)
			{
				logger.warn("No se pudo abrir la conexión"+e.toString());
			}
						
			AsociarCxCAFacturasCapitadasForm forma =(AsociarCxCAFacturasCapitadasForm)form;
			HttpSession session=request.getSession();		
			UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
			String estado=forma.getEstado(); 
			logger.warn("Estado AsociarCxCAFacturasCapitadas-->"+estado);
				
			if(estado == null)
			{
				forma.reset();	
				logger.warn("Estado no valido dentro del flujo de Asociar CxC A Facturas Capitadas (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
			else if (estado.equals("empezar"))
			{
				return this.accionEmpezar(forma,mapping, con);
			}
			else if(estado.equals("busquedaAvanzada"))
			{
				return this.accionBusquedaAvanzada(forma,mapping,con, "");
			}
			else if(estado.equals("redireccion"))
			{
				UtilidadBD.cerrarConexion(con);
				forma.setEstado("busquedaAvanzada");
				response.sendRedirect(forma.getLinkSiguiente());
				return null;
			}
			else if(estado.equals("activarTodosLosChecks"))
			{
				return this.accionActivarInactivarTodosLosChecks(con, forma, mapping, "si", response);
			}
			else if(estado.equals("inactivarTodosLosChecks"))
			{
				return this.accionActivarInactivarTodosLosChecks(con, forma, mapping, "no", response);
			}
			else if(estado.equals("ordenar"))
			{
				this.accionOrdenar(forma);
				UtilidadBD.cerrarConexion(con);
			    return mapping.findForward("principal");
			}
			else if(estado.equals("continuarMostrarErrores"))
			{
				UtilidadBD.cerrarConexion(con);
				forma.setEstado("busquedaAvanzada");
				return mapping.findForward("principal");
			}
			else if(estado.equals("guardar"))
			{
				return this.accionGuardar(forma,mapping, usuario, con, request);
			}
			else
			{
				forma.reset();
				logger.warn("Estado no valido dentro del flujo de Asociar CxC A Facturas Capitadas ");
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
	 * Este método especifica las acciones a realizar en el estado
	 * empezar.
	 * 
	 * @param forma AsociarCxCAFacturasCapitadasForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página principal "AsociarCxCAFacturasCapitadas.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar(	AsociarCxCAFacturasCapitadasForm forma, 
											ActionMapping mapping, 
											Connection con) throws SQLException
	{
		//Limpiamos lo que venga del form
		forma.reset();
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");		
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * busquedaAvanzada.
	 * 
	 * @param forma AsociarCxCAFacturasCapitadasForm
	 * 				para pre-llenar datos si es necesario
	 * @param request HttpServletRequest para obtener los 
	 * 				datos
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward a la página "AsociarCxCAFacturasCapitadas.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionBusquedaAvanzada(	AsociarCxCAFacturasCapitadasForm forma, 
													ActionMapping mapping, 
													Connection con, String mensaje
												) throws SQLException
	{
		forma.setEstado("busquedaAvanzada");
		AsociarCxCAFacturasCapitadas mundo= new AsociarCxCAFacturasCapitadas();
		mundo.reset();
		forma.setListadoMap(mundo.busquedaCuentasCobroAAsociar(con, forma.getCriteriosBusquedaMap()));
		forma.setMensaje(mensaje);
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * acti
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param valorChecks
	 * @param response
	 * @return
	 * @throws SQLException
	 * @throws IOException
	 */
	private ActionForward accionActivarInactivarTodosLosChecks(	Connection con, 
																AsociarCxCAFacturasCapitadasForm forma, 
																ActionMapping mapping, 
																String valorChecks,
																HttpServletResponse response) throws SQLException, IOException
	{
		forma.setCheckSeleccionTodos(valorChecks);
		for(int w=0; w<=Integer.parseInt(forma.getListadoMap("numRegistros").toString()); w++)
		{
			forma.setListadoMap("seleccion_"+w, valorChecks);
		}
		UtilidadBD.cerrarConexion(con);
		forma.setEstado("busquedaAvanzada");
		if(forma.getLinkSiguiente().equals(""))
			return mapping.findForward("principal");
		else
			response.sendRedirect(forma.getLinkSiguiente());
		return null;
	}
	
	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenar(AsociarCxCAFacturasCapitadasForm forma) 
	{
		forma.setEstado("busquedaAvanzada");
		int numReg=Integer.parseInt(forma.getListadoMap("numRegistros")==null?"0":forma.getListadoMap("numRegistros")+"");
		String[] indices={"numerocuentacobro_","codigoconvenio_","nombreconvenio_","fechaelaboracion_","fechaelaboracionbd_","valorinicial_", "fechainicial_","fechainicialbd_", "fechafinal_", "fechafinalbd_", "seleccion_"};
		forma.setListadoMap(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getListadoMap(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setListadoMap("numRegistros",numReg+"");
	}
	
	/**
	 * accion de guardar el asocio 
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @param con
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionGuardar(	AsociarCxCAFacturasCapitadasForm forma, 
											ActionMapping mapping,
											UsuarioBasico usuario,
											Connection con,
											HttpServletRequest request) throws SQLException
	{
		AsociarCxCAFacturasCapitadas mundo= new AsociarCxCAFacturasCapitadas();
		mundo.reset();
		//se inicia la transaccion
		UtilidadBD.iniciarTransaccion(con);
		Vector vectorFacturasSeleccionadas= new Vector();
		String mensaje="LA INSERCIÓN DEL ASOCIO CxC A FACTURAS  FUE EXITOSO PARA LAS CUENTAS COBRO ";
		String mensajeAdvertencia="LAS CUENTAS DE COBRO ";
		boolean centinelaAdvertencia= false;
		boolean centinelaMensaje=false;
		
		
		for(int w=0; w<Integer.parseInt(forma.getListadoMap("numRegistros").toString()); w++)
		{
			forma.setListadoMap("numeroFacturasAsociadas_"+w,"0");
			if(forma.getListadoMap("seleccion_"+w).equals("si"))
			{
				HashMap facturasAAsociarMap= mundo.seleccionFacturasAAsociar(con, forma.getListadoMap("fechainicial_"+w).toString(), forma.getListadoMap("fechafinal_"+w).toString(), Integer.parseInt(forma.getListadoMap("codigoconvenio_"+w).toString()), vectorFacturasSeleccionadas);
				
				// si llega a este punto entonces encontro facturas a asociar
				if(Integer.parseInt(facturasAAsociarMap.get("numRegistros").toString())>0)
				{
					forma.setListadoMap("numeroFacturasAsociadas_"+w,facturasAAsociarMap.get("numRegistros").toString());
					mensaje+=" "+forma.getListadoMap("numerocuentacobro_"+w)+", ";
					centinelaMensaje=true;
					//1. SE INSERTA EN LA ESTRUCTURA BASICA
					boolean inserto=mundo.insertar(con, forma.getListadoMap("numerocuentacobro_"+w).toString(), forma.getListadoMap("contabilizado_"+w).toString(), Integer.parseInt(forma.getListadoMap("codigoconvenio_"+w).toString()), usuario.getLoginUsuario(), usuario.getCodigoInstitucionInt(), Integer.parseInt(facturasAAsociarMap.get("numRegistros").toString()) );
					if(!inserto)
					{
						logger.warn("error.capitacion.asociosCxC.noInserto error 1-> no inserto estructura basica");
						UtilidadBD.abortarTransaccion(con);
						UtilidadBD.closeConnection(con);
						return accionSalidaErrores(forma, request, mapping, w);
					}
					for(int x=0; x<Integer.parseInt(facturasAAsociarMap.get("numRegistros").toString()); x++)
					{
						vectorFacturasSeleccionadas.add( facturasAAsociarMap.get("codigo_"+x).toString() );
						inserto= Factura.updateNumeroCuentaCobroCapitadaEnFactura(con, forma.getListadoMap("numerocuentacobro_"+w).toString(), facturasAAsociarMap.get("codigo_"+x).toString());
						if(!inserto)
						{
							logger.warn("error.capitacion.asociosCxC.noInserto error 2-> no actualizo el numero de la cuenta de cobro en la factura");
							UtilidadBD.abortarTransaccion(con);
							UtilidadBD.closeConnection(con);
							return accionSalidaErrores(forma, request, mapping, w);
						}
					}
					
					if(facturasAAsociarMap.containsKey("TOTAL_INGRESOS"))
					{
						CuentaCobroCapitacion cuentaCobroCapitada= new CuentaCobroCapitacion();
						inserto=cuentaCobroCapitada.updateTotalIngresosDiferenciaCuenta(con, forma.getListadoMap("numerocuentacobro_"+w).toString(), facturasAAsociarMap.get("TOTAL_INGRESOS").toString());
						if(!inserto)
						{
							logger.warn("error.capitacion.asociosCxC.noInserto error 3-> no actualizo el total ingresos y diferencia cuenta en la cuenta cobro");
							UtilidadBD.abortarTransaccion(con);
							UtilidadBD.closeConnection(con);
							return accionSalidaErrores(forma, request, mapping, w);
						}
					}
					else
					{
						logger.warn("error.capitacion.asociosCxC.noInserto error 3-> mapa no tiene key TOTAL_INGRESOS ");
						UtilidadBD.abortarTransaccion(con);
						UtilidadBD.closeConnection(con);
						return accionSalidaErrores(forma, request, mapping, w);
					}
				}
				//si llega a este punto entonces no existian facturas a asociar para esa cuenta de cobro y se debe advertir al usuario 
				else
				{
					centinelaAdvertencia=true;
					mensajeAdvertencia+=" "+forma.getListadoMap("numerocuentacobro_"+w)+", ";
				}
				
			}
		}
		
		String mensajeFinal="";
		if(centinelaMensaje)
		{
			mensajeFinal= mensaje;
		}
		if(centinelaAdvertencia)
		{	
			mensajeAdvertencia+=" NO TIENEN FACTURAS ASOCIADAS, POR LO TANTO NO SE ACTUALIZARON.";
			mensajeFinal+=" <br> "+mensajeAdvertencia;
		}
		
		//UtilidadBD.abortarTransaccion(con);
		UtilidadBD.finalizarTransaccion(con);
		//NO SE DEBE CERRAR CONEXION, VA HA LA ACCION BUSQUEDA AVANZADA
		return this.accionResumen(forma, mapping, con, mensajeFinal);
	}
	
	
	/**
	 * accion resuemn
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param mensaje
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionResumen(	AsociarCxCAFacturasCapitadasForm forma, 
											ActionMapping mapping, 
											Connection con, String mensaje
										) throws SQLException
	{
		forma.setEstado("resumen");
		forma.setMensaje(mensaje);
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("resumen");
	}
	
	
	/**
	 * salida a la pagina principal con 
	 * @param forma
	 * @param request
	 * @param mapping
	 * @param indice
	 * @return
	 */
	private ActionForward accionSalidaErrores(AsociarCxCAFacturasCapitadasForm forma, HttpServletRequest request, ActionMapping mapping, int indice)
	{
		ActionErrors errores= new ActionErrors();
		forma.setEstado("busquedaAvanzada");
		errores.add("error.capitacion.asociosCxC.noInserto", new ActionMessage("error.capitacion.asociosCxC.noInserto", forma.getListadoMap("numerocuentacobro_"+indice).toString(), forma.getListadoMap("nombreconvenio_"+indice).toString()));
		saveErrors(request,errores);
		return mapping.findForward("principal");
	}
	
	
}
