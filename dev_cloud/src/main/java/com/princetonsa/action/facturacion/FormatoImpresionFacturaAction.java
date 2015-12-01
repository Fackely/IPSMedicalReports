/*
 * @(#)FormatoImpresionFacturaAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.action.facturacion;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.UtilidadBD;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.facturacion.FormatoImpresionFacturaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.FormatoImpresionFactura;

/**
 * Clase encargada del control de la funcionalidad de Formato de Impresion de Factura

 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 11 /Ene/ 2006
 */
public class FormatoImpresionFacturaAction extends Action
{
	/**
	 * Objeto para almacenar los logs que aparezcan en el Action 
	 */
	private Logger logger= Logger.getLogger(FormatoImpresionFacturaAction.class);
	boolean esNuevo=false;

	/**
	 * Método excute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try{
			if(form instanceof FormatoImpresionFacturaForm)
			{


				/**Intentamos abrir una conexion con la fuente de datos**/ 
				con = openDBConnection(con); 
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				FormatoImpresionFacturaForm forma=(FormatoImpresionFacturaForm)form;
				HttpSession session = request.getSession();
				UsuarioBasico usuario= (UsuarioBasico)session.getAttribute("usuarioBasico");
				FormatoImpresionFactura mundo =new FormatoImpresionFactura();
				String estado = forma.getEstado();
				logger.info("[FormatoImpresionFacturaAction] estado->"+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de FormatoImpresionFacturaAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}

				else if(estado.equals("empezar"))
				{
					return this.accionEmpezar(mapping, forma, con, usuario, mundo);
				}
				else if(estado.equals("modificar"))
				{
					return this.accionModificar(forma, mapping, con, mundo, usuario);
				}
				else if(estado.equals("nuevo"))
				{
					return this.accionNuevo(mapping, forma, con, mundo, usuario);
				}
				else if(estado.equals("guardarModificacion"))
				{
					return this.accionGuardarModificacion(forma, request, mapping, con, mundo);
				}
				else if(estado.equals("guardarNuevo"))
				{
					return this.accionGuardarNuevo(forma, request, mapping, con, mundo, usuario);
				}
				else if(estado.equals("agregarNuevaFirma"))
				{
					return this.accionAgregarFirma(forma, response, con);
				}
				else if(estado.equals("borrarFirmas"))
				{
					forma.resetFirmas();
					forma.setMapaFirmas("numRegistros", 0+"");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaPrincipal");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de Formato de Impresion de Factura");
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
	 * Acción de empezar el flujo de la funcionalidad solo cargando los formatos de
	 * impresión de factura existentes
	 * @param mapping
	 * @param forma
	 * @param con
	 * @param usuario
	 * @param mundo
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionEmpezar(ActionMapping mapping, FormatoImpresionFacturaForm forma, Connection con, UsuarioBasico usuario, FormatoImpresionFactura mundo) throws Exception
	{
		forma.reset();
		forma.setMapaFormatosPrevios(mundo.consultarFormatosExistentes(con, usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaSeleccion");
	}
	
	
	/**
	 * Accion para generar un formato de impresion de factura Nuevo
	 * @param mapping
	 * @param forma
	 * @param con
	 * @return
	 * @throws Exception
	 */
	private ActionForward accionNuevo(ActionMapping mapping, FormatoImpresionFacturaForm forma, Connection con, FormatoImpresionFactura mundo, UsuarioBasico usuario) throws Exception
	{
		forma.reset();
		esNuevo=true;
		forma.setMapaFormatosPrevios(mundo.consultarFormatosExistentes(con, usuario.getCodigoInstitucionInt()));
		forma.setMapaFormatoBasico(mundo.consultarFormatoFacturaBasico(con, usuario.getCodigoInstitucionInt(), 0));
		
		forma.getMapaFormatoBasico().put("esnuevo", "si");
		
		forma.setMapaSeccionPpal(mundo.consultarDatosSeccionPpal(con, 0));
		forma.setMapaDatosSecEncabezado(mundo.consultarDatosSecEncabezado(con, 0));
		forma.setMapaSeccionEncabezado(mundo.consultarTodoSubSecciones(con, 0));
		forma.setMapaSecServicios(mundo.consultarSecServicios(con, 0));
		forma.setMapaSecArticulos(mundo.consultarSecArticulos(con, 0));
		forma.setMapaSecTotales(mundo.consultarSecTotales(con, 0));
		forma.setMapaSecNotaPie(mundo.consultarDatosSecNotaPie(con, 0));
		forma.setMapaFirmas(mundo.consultarFirmas(con, 0));
		
		forma.setMapaDetNivelServ((HashMap)forma.getMapaSecServicios("detallenivel"));
		forma.setMapaDetNivelArt((HashMap)forma.getMapaSecArticulos("detallenivel"));
		forma.setMapaCamposTotales((HashMap)forma.getMapaSecTotales("detallecampo"));
		
		
		
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");
	}
	
	
	
	
	/**
	 * Accion para modificar un formato de impresion de factura
	 * seleccionado de los ya existentes
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param mundo
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionModificar(FormatoImpresionFacturaForm forma, ActionMapping mapping, Connection con, FormatoImpresionFactura mundo, UsuarioBasico usuario) throws SQLException
	{
		int codigoFormato=forma.getCodigoFormato();
		forma.setMapaFormatoBasico(mundo.consultarFormatoFacturaBasico(con, usuario.getCodigoInstitucionInt(), codigoFormato));
		forma.getMapaFormatoBasico().put("esnuevo", "no");
		forma.setMapaSeccionPpal(mundo.consultarDatosSeccionPpal(con, codigoFormato));
		forma.setMapaDatosSecEncabezado(mundo.consultarDatosSecEncabezado(con, codigoFormato));
		forma.setMapaSeccionEncabezado(mundo.consultarTodoSubSecciones(con, codigoFormato));
		forma.setMapaSecServicios(mundo.consultarSecServicios(con, codigoFormato));
		forma.setMapaSecArticulos(mundo.consultarSecArticulos(con, codigoFormato));
		forma.setMapaSecTotales(mundo.consultarSecTotales(con, codigoFormato));
		forma.setMapaSecNotaPie(mundo.consultarDatosSecNotaPie(con, codigoFormato));
		forma.setMapaFirmas(mundo.consultarFirmas(con, codigoFormato));
		
		
		forma.setMapaDetNivelServ((HashMap)forma.getMapaSecServicios("detallenivel"));
		forma.setMapaDetNivelArt((HashMap)forma.getMapaSecArticulos("detallenivel"));
		forma.setMapaCamposTotales((HashMap)forma.getMapaSecTotales("detallecampo"));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaPrincipal");		
	}
	
	
	/**
	 * Accion para guardar la modificacion de un formato de factura 
	 * de presupuesto existente
	 * @param forma
	 * @param request
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @param mundo
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionGuardarModificacion(FormatoImpresionFacturaForm forma, HttpServletRequest request, ActionMapping mapping, Connection con, FormatoImpresionFactura mundo) throws SQLException
	{	
		int resp=0;
		UtilidadBD.iniciarTransaccion(con);
		resp+=mundo.actualizarFormatoImpresionBasico(con, forma.getMapaFormatoBasico(), forma.getCodigoFormato());
		resp+=mundo.actualizarDatosSecPpal(con, forma.getMapaSeccionPpal(), forma.getCodigoFormato());
		resp+=mundo.actualizarDatosBasicosSecEncabezado(con, forma.getMapaDatosSecEncabezado(), forma.getCodigoFormato());
		resp+=mundo.actualizarTodoSubSecciones(con, forma.getCodigoFormato(), forma.getMapaSeccionEncabezado());
		resp+=mundo.actualizarSecServicios(con, forma.getCodigoFormato(), forma.getMapaSecServicios());
		resp+=mundo.actualizarSecArticulos(con, forma.getCodigoFormato(), forma.getMapaSecArticulos());
		resp+=mundo.actualizarSecTotales(con, forma.getCodigoFormato(), forma.getMapaSecTotales());
		resp+=mundo.actualizarDatosSecNotaPie(con, forma.getCodigoFormato(), forma.getMapaSecNotaPie());
		resp+=mundo.actualizarFirmasSecNotaPie(con, forma.getCodigoFormato(), forma.getMapaFirmas());
		if(resp==9)
		{
			UtilidadBD.finalizarTransaccion(con);
			forma.setMensajeExitoso("La Modificación del Formato de Impresión de Factura: "+forma.getMapaFormatoBasico("nombreformato").toString().toUpperCase()+" fue exitosa.");
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			return ComunAction.accionSalirCasoError(mapping, request,con,logger, "no se guardo modificacion","error.manejoPaciente.formatoImpresionFactura.noSeGuardoModificacion", true);
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaSeleccion");		
	}
	
	
	/**
	 * Accion para guardar un Nuevo formato de impresion de presupuesto
	 * @param forma
	 * @param request
	 * @param mapping
	 * @param con
	 * @param mundo
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionGuardarNuevo(FormatoImpresionFacturaForm forma, HttpServletRequest request, ActionMapping mapping, Connection con, FormatoImpresionFactura mundo, UsuarioBasico usuario) throws SQLException
	{
		
		
		
		
		for(int i=0;i<Integer.parseInt(forma.getMapaSeccionEncabezado("numRegistros").toString());i++)
		{
			HashMap mapaTemp=new HashMap();
			int k=0;
			for(k=0;k<Integer.parseInt(forma.getMapaCamposSecEncabe("numRegistros_"+i).toString());k++)
			{
				mapaTemp.put("codcampo_"+k,forma.getMapaCamposSecEncabe("codcampo_"+i+"_"+k));
				mapaTemp.put("codseccion_"+k,forma.getMapaCamposSecEncabe("codseccion_"+i+"_"+k));
				mapaTemp.put("campo_"+k,forma.getMapaCamposSecEncabe("campo_"+i+"_"+k));
				mapaTemp.put("imprimir_"+k,forma.getMapaCamposSecEncabe("imprimir_"+i+"_"+k));
				mapaTemp.put("orden_"+k,forma.getMapaCamposSecEncabe("orden_"+i+"_"+k));
			}
			mapaTemp.put("numRegistros",k+"");
			forma.getMapaSeccionEncabezado().put("camposeccion_"+i,mapaTemp);
		}

		
		int resp=0;
		UtilidadBD.iniciarTransaccion(con);
		resp+=mundo.insertarFormatoImpresionBasico(con, forma.getMapaFormatoBasico(), usuario.getCodigoInstitucionInt());
		int codigoFormato=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(con, "seq_formato_impresion_factura");
		resp+=mundo.insertarDatosSecPpal(con, forma.getMapaSeccionPpal(), codigoFormato);
		resp+=mundo.insertarDatosBasicosSecEncabezado(con, forma.getMapaDatosSecEncabezado(), codigoFormato);
		resp+=mundo.insertarTodoSubSecciones(con, codigoFormato, forma.getMapaSeccionEncabezado());
		resp+=mundo.insertarSecServicios(con, codigoFormato, forma.getMapaSecServicios());
		resp+=mundo.insertarSecArticulos(con, codigoFormato, forma.getMapaSecArticulos());
		resp+=mundo.insertarSecTotales(con, codigoFormato, forma.getMapaSecTotales());
		resp+=mundo.insertarDatosSecNotaPie(con, codigoFormato, forma.getMapaSecNotaPie());
		resp+=mundo.insertarFirmasSecNotaPie(con, codigoFormato, forma.getMapaFirmas());
		if(resp==9)
		{
			UtilidadBD.finalizarTransaccion(con);
			forma.setMensajeExitoso("El nuevo Formato de Impresión de Factura se guardó exitosamente.");
			forma.setMapaFormatosPrevios(mundo.consultarFormatosExistentes(con, usuario.getCodigoInstitucionInt()));
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			return ComunAction.accionSalirCasoError(mapping, request,con,logger, "no se guardo nuevo","error.manejoPaciente.formatoImpresionFactura.noSeGuardoInformacionNueva", true);
		}
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("paginaSeleccion");		
	}
		
	
	/**
	 * Accion para agregar una nueva firma
	 * @param forma
	 * @param response 
	 * @param request 
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionAgregarFirma(FormatoImpresionFacturaForm forma, HttpServletResponse response, Connection con) throws SQLException
	{
		int index=Integer.parseInt(forma.getMapaFirmas("numRegistros").toString());
		forma.getMapaFirmas().put("codigoformato_"+index, forma.getCodigoFormato());
		forma.getMapaFirmas().put("codfirma_"+index, "");
		forma.getMapaFirmas().put("nomfirma_"+index, "");
		forma.getMapaFirmas().put("descripcionfirma_"+index, "");
		index=index+1;
		forma.setMapaFirmas("numRegistros", index+"");
				
		UtilidadBD.closeConnection(con);
		try
		{
			response.sendRedirect("../ingresarModificarFormatoImpresionFactura/ingresarModificar.jsp#abajo");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
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