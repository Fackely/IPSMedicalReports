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
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.actionform.facturasVarias.AprobacionAnulacionPagosFacturasVariasForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturasVarias.AprobacionAnulacionPagosFacturasVarias;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * Fecha: Abril de 2008
 * @author axioma
 * Mauricio Jaramillo H.
 */

public class AprobacionAnulacionPagosFacturasVariasAction extends Action
{

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger = Logger.getLogger(PagosFacturasVariasAction.class);
	
	/**
	 * Método execute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try{

			if (form instanceof AprobacionAnulacionPagosFacturasVariasForm) 
			{

				//Abrimos la conexion con la fuente de Datos 
				con = util.UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				AprobacionAnulacionPagosFacturasVariasForm forma = (AprobacionAnulacionPagosFacturasVariasForm) form;
				AprobacionAnulacionPagosFacturasVarias mundo = new AprobacionAnulacionPagosFacturasVarias();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				String estado = forma.getEstado();
				forma.setMensaje(new ResultadoBoolean(false));
				logger.warn("[AprobacionAnulacionPagosFacturasVarias]--->Estado: "+estado);

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
					this.accionConsultarAplicacionesPagosFacturasVarias(con, forma, mundo, usuario);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("cargar_aproanul"))
				{
					int posicion = Utilidades.convertirAEntero(request.getParameter("posicion")+"");
					int codAplicacion = Utilidades.convertirAEntero(request.getParameter("codAplicacionPago"));
					forma.setPosicion(posicion);
					forma.setFechaGenDoc(request.getParameter("fechaDocumento"));
					forma.setCodAplicacionPago(codAplicacion);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("guardar"))
				{
					return this.accionGuardar(con, forma, mundo, usuario, mapping);
				}
				else if(estado.equals("busquedaAvanzada"))
				{
					this.accionBusquedaAvanzada(con, forma, mundo, usuario);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("deudor"))
				{
					this.accionBusquedaFacturasDeudores(con, forma, mundo, usuario);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				//ESTADO UTILIZADO PARA EL PAGER
				else if (estado.equals("redireccion")) 
				{			    
					UtilidadBD.cerrarConexion(con);
					forma.getLinkSiguiente();
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else if(estado.equals("imprimir"))
				{
					return this.accionImprimir(con, forma, mundo, usuario, mapping, request);
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
				logger.error("El form no es compatible con el form de AprobacionAnulacionPagosFacturasVariasForm");
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
	 * Metodo que permite imprimir la aprobacion 
	 * o anulacion del pago de facturas varias
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionImprimir(Connection con, AprobacionAnulacionPagosFacturasVariasForm forma, AprobacionAnulacionPagosFacturasVarias mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request)
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
        comp.insertLabelInGridPpalOfHeader(3,0, "("+forma.getListadoAplicaciones("acronimotipo_"+forma.getPosicion())+""+") No. "+forma.getListadoAplicaciones("documento_"+forma.getPosicion())+""+" APLICACIÓN PAGOS FACTURAS VARIAS No. "+forma.getListadoAplicaciones("codigoaplicacion_"+forma.getPosicion())+"");
        comp.insertLabelInGridPpalOfHeader(4,0, "ESTADO: "+Utilidades.obtenerNombreEstadoAplicacionPagos(con, forma.getEstadoAprobAnul()));
        comp.insertLabelInGridPpalOfHeader(5,0, "ELABORADO  Fecha: "+forma.getListadoAplicaciones("fecha_"+forma.getPosicion())+""+" Usuario: "+forma.getListadoAplicaciones("usuario_"+forma.getPosicion())+"");
        comp.insertLabelInGridPpalOfHeader(6,0, Utilidades.obtenerNombreEstadoAplicacionPagos(con, forma.getEstadoAprobAnul())+" Fecha: "+forma.getFechaAprobAnul()+" Usuario: "+usuario.getLoginUsuario());
        comp.insertLabelInGridPpalOfHeader(7,0, "DEUDOR: "+forma.getListadoAplicaciones("identificaciondeudor_"+forma.getPosicion())+""+" - "+forma.getListadoAplicaciones("nombredeudor_"+forma.getPosicion())+"");
        comp.insertLabelInGridPpalOfHeader(9,0, "OBSERVACIONES: "+forma.getListadoAplicaciones("observaciones_"+forma.getPosicion())+"");
        
        comp.obtenerComponentesDataSet("ConsultaImpresionPagosFacturasVarias");
        String newQuery = comp.obtenerQueryDataSet().replace("1=1", "aplicacion_pagos = "+forma.getCodAplicacionPago());
        
        logger.info("\n====>Consulta Pagos Facturas Varias: "+newQuery);
        
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
        
        UtilidadBD.closeConnection(con);
		return mapping.findForward("resumen");
	}

	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionConsultarAplicacionesPagosFacturasVarias(Connection con, AprobacionAnulacionPagosFacturasVariasForm forma, AprobacionAnulacionPagosFacturasVarias mundo, UsuarioBasico usuario)
	{
		forma.setListadoAplicaciones(mundo.consultarAplicacionesPagosFacturasVarias(con, usuario.getCodigoInstitucionInt()));
	}

	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param mapping 
	 * @param pos
	 */
	private ActionForward accionGuardar(Connection con, AprobacionAnulacionPagosFacturasVariasForm forma, AprobacionAnulacionPagosFacturasVarias mundo, UsuarioBasico usuario, ActionMapping mapping)
	{
		boolean transaccion = false; 
		HashMap vo = new HashMap();
		UtilidadBD.iniciarTransaccion(con);
		this.accionBusquedaFacturasDeudores(con, forma, mundo, usuario);
		vo.put("codigoAplicacion", forma.getCodAplicacionPago());
		vo.put("pagos", forma.getListadoAplicaciones("pagos_"+forma.getPosicion()));
		vo.put("estado", forma.getEstadoAprobAnul());
		vo.put("usuarioaprobanul", usuario.getLoginUsuario());
		vo.put("motivoanulacion", forma.getMotivoAprobAnul());
		vo.put("fechaaprobanul", UtilidadFecha.conversionFormatoFechaABD(forma.getFechaAprobAnul()));
		vo.put("horaaprobanul", UtilidadFecha.getHoraActual());
		if(vo.get("estado").equals(ConstantesBD.codigoEstadoAplicacionPagosAprobado))
		{
			transaccion = mundo.modificarAprobado(con, vo);
			for(int i=0; i<Utilidades.convertirAEntero(forma.getListadoFacturasAplicacion("numRegistros")+""); i++)
			{
				vo.put("codigoFacturaVarias", forma.getListadoFacturasAplicacion("codigofactura_"+i));
				vo.put("valorPagoFacturaVarias", forma.getListadoFacturasAplicacion("valorpago_"+i));
				transaccion = mundo.modificarAprobadoFacturas(con, vo);
			}
		}
		else if(vo.get("estado").equals(ConstantesBD.codigoEstadoAplicacionPagosAnulado))
			transaccion = mundo.modificarAnulado(con, vo);
		
		logger.info("===>Transaccion: "+transaccion);
		if(transaccion)
		{
			forma.setListadoAplicaciones("nombreEstadoAproAnul", Utilidades.obtenerNombreEstadoAplicacionPagos(con, forma.getEstadoAprobAnul()));
			UtilidadBD.finalizarTransaccion(con);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("resumen");
		}
		else
		{
			forma.setMensaje(new ResultadoBoolean(true,"SE PRESENTO INCONVENIENTES EN LA APROBACIÓN/ANULACIÓN DEL DOCUMENTO "+forma.getListadoAplicaciones("documento_"+forma.getPosicion())));
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("principal");
		}
	}
	
	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionBusquedaAvanzada(Connection con, AprobacionAnulacionPagosFacturasVariasForm forma, AprobacionAnulacionPagosFacturasVarias mundo, UsuarioBasico usuario)
    {
		HashMap vo = new HashMap();
		vo.put("tipo", forma.getTipoDocumento()+"");
		vo.put("documento", forma.getNumeroDocumento()+"");
		vo.put("fecha", forma.getFechaDocumento()+"");
		vo.put("deudor", forma.getDeudorDocumento()+"");
		vo.put("institucion", usuario.getCodigoInstitucionInt()+"");
		forma.setListadoAplicaciones(mundo.busquedaAvanzada(con,vo));
    }
	
	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionBusquedaFacturasDeudores(Connection con, AprobacionAnulacionPagosFacturasVariasForm forma, AprobacionAnulacionPagosFacturasVarias mundo, UsuarioBasico usuario)
	{
		int codigoDeudor = Utilidades.convertirAEntero(forma.getListadoAplicaciones("codigodeudor_"+forma.getPosicion())+"");
		int codigoAplicacionPago = Utilidades.convertirAEntero(forma.getListadoAplicaciones("codigoaplicacion_"+forma.getPosicion())+"");
		forma.setListadoFacturasAplicacion(mundo.busquedaFacturasDeudores(con, codigoDeudor, codigoAplicacionPago));
	}
	
}