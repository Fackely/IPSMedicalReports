package com.princetonsa.action.facturasVarias;

import java.sql.Connection;
import java.util.HashMap;
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
import util.UtilidadBD;
import util.Utilidades;

import com.princetonsa.actionform.facturasVarias.ConsultaImpresionPagosFacturasVariasForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturasVarias.ConsultaImpresionPagosFacturasVarias;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * Fecha: Abril de 2008
 * @author Mauricio Jaramillo
 */

public class ConsultaImpresionPagosFacturasVariasAction extends Action 
{

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger = Logger.getLogger(ConsultaImpresionPagosFacturasVariasAction.class);
	
	/**
	 * Método execute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try{
			if (form instanceof ConsultaImpresionPagosFacturasVariasForm) 
			{

				//Abrimos la conexion con la fuente de Datos 
				con = util.UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				ConsultaImpresionPagosFacturasVariasForm forma = (ConsultaImpresionPagosFacturasVariasForm) form;
				ConsultaImpresionPagosFacturasVarias mundo = new ConsultaImpresionPagosFacturasVarias();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				String estado = forma.getEstado();
				logger.warn("[ConsultaImpresionPagosFacturasVarias]--->Estado: "+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo estado is NULL");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					forma.reset();
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("buscar"))
				{
					this.accionBuscarPagosFacturasVarias(con, forma, mundo, usuario);
					if(Utilidades.convertirAEntero(forma.getListadoPagosFacturasVarias("numRegistros")+"") == 1)
					{
						forma.setPosAplicacionPagosFacturasVarias(0);
						this.cargarAplicacion(con, forma, mundo, usuario);
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("cargarDetalle");
					}
					else
					{
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("listado");
					}
				}
				else if (estado.equals("redireccion"))// estado para mantener los datos del pager
				{			    
					UtilidadBD.cerrarConexion(con);
					forma.getLinkSiguiente();
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else if(estado.equals("ordenar"))
				{
					this.ordenarListado(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("listado");
				}
				else if(estado.equals("cargarAplicacion"))
				{
					forma.setPosAplicacionPagosFacturasVarias(Utilidades.convertirAEntero(request.getParameter("posicion")+""));
					this.cargarAplicacion(con, forma, mundo, usuario);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("cargarDetalle");
				}
				else if(estado.equals("imprimir"))
				{
					this.generarReporte(con, forma, mapping, request, mundo, usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("cargarDetalle");
				}
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

			}
			else
			{
				logger.error("El form no es compatible con el form de ConsultaImpresionPagosFacturasVariasForm");
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
	 * Metodo que se encarga de generar el reporte
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param mundo
	 * @param usuario
	 */
	private void generarReporte(Connection con, ConsultaImpresionPagosFacturasVariasForm forma, ActionMapping mapping, HttpServletRequest request, ConsultaImpresionPagosFacturasVarias mundo, UsuarioBasico usuario)
	{
		String nombreRptDesign = "ConsultaImpresionPagosFacturasVarias.rptdesign";
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		//Informacion del Cabezote
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturasVarias/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v = new Vector();
        Vector cabezote = new Vector();
        v.add(ins.getRazonSocial());
        v.add(Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+"  -  "+ins.getNit());
        v.add(ins.getDireccion());
        v.add("Tels. "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1, v);
        comp.insertLabelInGridPpalOfHeader(1,1, "APLICACIÓN PAGOS FACTURAS VARIAS");
        comp.insertLabelInGridPpalOfHeader(3,0, "("+forma.getListadoPagosFacturasVarias("acronimotipo_"+forma.getPosAplicacionPagosFacturasVarias())+""+") No. "+forma.getListadoPagosFacturasVarias("documento_"+forma.getPosAplicacionPagosFacturasVarias())+""+" APLICACIÓN PAGOS FACTURAS VARIAS No. "+forma.getListadoPagosFacturasVarias("codigoaplicacion_"+forma.getPosAplicacionPagosFacturasVarias())+"");
        comp.insertLabelInGridPpalOfHeader(4,0, "ESTADO: "+forma.getListadoPagosFacturasVarias("nombreestadoaplicacion_"+forma.getPosAplicacionPagosFacturasVarias())+"");
        comp.insertLabelInGridPpalOfHeader(5,0, "ELABORADO  Fecha: "+forma.getListadoPagosFacturasVarias("fecha_"+forma.getPosAplicacionPagosFacturasVarias())+""+" Usuario: "+forma.getListadoPagosFacturasVarias("usuario_"+forma.getPosAplicacionPagosFacturasVarias())+"");
        
        if(Utilidades.convertirAEntero(forma.getListadoPagosFacturasVarias("estadoaplicacion_"+forma.getPosAplicacionPagosFacturasVarias())+"") == ConstantesBD.codigoEstadoAplicacionPagosAprobado)
        	comp.insertLabelInGridPpalOfHeader(6,0, "APROBADO   Fecha: "+forma.getListadoPagosFacturasVarias("fechaaprobanul_"+forma.getPosAplicacionPagosFacturasVarias())+""+" Usuario: "+forma.getListadoPagosFacturasVarias("usuarioaprobanul_"+forma.getPosAplicacionPagosFacturasVarias())+"");
        else if(Utilidades.convertirAEntero(forma.getListadoPagosFacturasVarias("estadoaplicacion_"+forma.getPosAplicacionPagosFacturasVarias())+"") == ConstantesBD.codigoEstadoAplicacionPagosAnulado)
        	comp.insertLabelInGridPpalOfHeader(6,0, "ANULADO   Fecha: "+forma.getListadoPagosFacturasVarias("fechaaprobanul_"+forma.getPosAplicacionPagosFacturasVarias())+""+" Usuario: "+forma.getListadoPagosFacturasVarias("usuarioaprobanul_"+forma.getPosAplicacionPagosFacturasVarias())+"");

        if(Utilidades.convertirAEntero(forma.getListadoPagosFacturasVarias("estadoaplicacion_"+forma.getPosAplicacionPagosFacturasVarias())+"") == ConstantesBD.codigoEstadoAplicacionPagosPendiente)
        	comp.insertLabelInGridPpalOfHeader(6,0, "DEUDOR: "+forma.getListadoPagosFacturasVarias("identificaciondeudor_"+forma.getPosAplicacionPagosFacturasVarias())+""+" - "+forma.getListadoPagosFacturasVarias("nombredeudor_"+forma.getPosAplicacionPagosFacturasVarias())+"");
        else
        	comp.insertLabelInGridPpalOfHeader(7,0, "DEUDOR: "+forma.getListadoPagosFacturasVarias("identificaciondeudor_"+forma.getPosAplicacionPagosFacturasVarias())+""+" - "+forma.getListadoPagosFacturasVarias("nombredeudor_"+forma.getPosAplicacionPagosFacturasVarias())+"");
        
        if(Utilidades.convertirAEntero(forma.getListadoPagosFacturasVarias("estadoaplicacion_"+forma.getPosAplicacionPagosFacturasVarias())+"") == ConstantesBD.codigoEstadoAplicacionPagosPendiente)
        	comp.insertLabelInGridPpalOfHeader(8,0, "OBSERVACIONES: "+forma.getListadoPagosFacturasVarias("observaciones_"+forma.getPosAplicacionPagosFacturasVarias())+"");
        else
        	comp.insertLabelInGridPpalOfHeader(9,0, "OBSERVACIONES: "+forma.getListadoPagosFacturasVarias("observaciones_"+forma.getPosAplicacionPagosFacturasVarias())+"");
        
        comp.obtenerComponentesDataSet("ConsultaImpresionPagosFacturasVarias");
        String newQuery = comp.obtenerQueryDataSet().replace("1=1", "aplicacion_pagos = "+forma.getListadoPagosFacturasVarias("codigoaplicacion_"+forma.getPosAplicacionPagosFacturasVarias()));
        
        //Se modifica el query
        comp.modificarQueryDataSet(newQuery);
        
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
        
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
	}

	/**
	 * Metodo que carga el detalle de la aplicacion de pago seleccionada.
	 * Si la busqueda solo arroja un resultado lo carga en el detalle de una vez. 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void cargarAplicacion(Connection con, ConsultaImpresionPagosFacturasVariasForm forma, ConsultaImpresionPagosFacturasVarias mundo, UsuarioBasico usuario)
	{
		int codigoAplicacion = Utilidades.convertirAEntero(forma.getListadoPagosFacturasVarias("codigoaplicacion_"+forma.getPosAplicacionPagosFacturasVarias())+"");
		forma.setSecConceptos(mundo.buscarConceptos(con, usuario.getCodigoInstitucionInt(), codigoAplicacion));
		forma.setSecFacturas(mundo.buscarFacturas(con, usuario.getCodigoInstitucionInt(), codigoAplicacion));
	}

	/**
	 * Metodo que consulta las aplicaciones de pagos de factura varias
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionBuscarPagosFacturasVarias(Connection con, ConsultaImpresionPagosFacturasVariasForm forma, ConsultaImpresionPagosFacturasVarias mundo, UsuarioBasico usuario)
	{
		HashMap criterios = new HashMap();
		criterios.put("tipo", forma.getTipo());
		criterios.put("numeroDocumento", forma.getNumeroDocumento());
		criterios.put("consecutivoPago", forma.getConsecutivoPago());
		criterios.put("estadoPago", forma.getEstadoPago());
		criterios.put("fechaInicial", forma.getFechaInicial());
		criterios.put("fechaFinal", forma.getFechaFinal());
		forma.setListadoPagosFacturasVarias(mundo.buscarPagosFacturasVarias(con, usuario.getCodigoInstitucionInt(), criterios));
	}

	/**
	 * Metodo utilizado para ordenar el listado de pagos facturas varias
	 * @param forma
	 */
	private void ordenarListado(ConsultaImpresionPagosFacturasVariasForm forma)
	{
		String[] indices={
                		"codigotipo_",
                		"destipo_",
                		"acronimotipo_",
                		"documento_",
                		"codigopago_",
                		"fechadocumento_",
                		"codigoaplicacion_",
                		"estadoaplicacion_",
                		"nombreestadoaplicacion_",
                		"valorconceptos_",
                		"valorfacturas_",
                		"codigodeudor_",
                		"nombredeudor_",
                		"identificaciondeudor_",
                		};
		int numReg=Integer.parseInt(forma.getListadoPagosFacturasVarias("numRegistros")+"");
		forma.setListadoPagosFacturasVarias(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getListadoPagosFacturasVarias(), numReg));
		forma.setListadoPagosFacturasVarias("numRegistros", numReg+"");
		forma.setUltimoPatron(forma.getPatronOrdenar());
    }
	
}