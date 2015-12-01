package com.princetonsa.action.facturasVarias;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.Listado;
import util.UtilidadBD;
import util.Utilidades;

import com.princetonsa.actionform.facturasVarias.ConsultaFacturasVariasForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturasVarias.ConsultaFacturasVarias;
import com.princetonsa.mundo.facturasVarias.GenModFacturasVarias;

public class ConsultaFacturasVariasAction extends Action 
{
	
			
			
	/**
	 * objeto para manejar los logs de esta clase
	 */
	
	Logger logger =Logger.getLogger(ConsultaFacturasVariasAction.class);		
	
	
	/**
	 * 
	 */
	private String[] indices={
								"codigofactura_",
								"tipodeudor_",
								"consecutivo_",
								"fechaelaboracion_",
								"numeroidentificacion_",
								"descripcionconcepto_",
								"valorfactura_",
								"estadofactura_",
								"descripcion_",
								"deudor_"
							  };
	
	
	/**
	 * Método excute del Action
	 */
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{
			if (form instanceof ConsultaFacturasVariasForm) 
			{
				ConsultaFacturasVariasForm forma=(ConsultaFacturasVariasForm) form;

				String estado=forma.getEstado();

				logger.info("Estado -->"+estado);

				con=UtilidadBD.abrirConexion();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());

				ConsultaFacturasVarias mundo=new ConsultaFacturasVarias();

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ConceptosCarteraAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					forma.reset();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("buscar"))
				{
					//forma.reset();
					forma.setMapaResultadoFacturas(mundo.BusquedaFacturas(con ,forma.getFechaInicial(), forma.getFechaFinal(), forma.getFactura(), forma.getEstadosFactura(), forma.getTipoDeudor(), forma.getDeudor()));
					int numReg=Utilidades.convertirAEntero(forma.getMapaResultadoFacturas("numRegistros")+"");
					if(numReg==1)
					{	
						forma.setDetalleFactura(mundo.consultaDetalleFactura(con, Utilidades.convertirAEntero(forma.getMapaResultadoFacturas().get("codigofactura_0")+""), forma.getMapaResultadoFacturas().get("tipodeudor_0")+""));
						UtilidadBD.closeConnection(con);
						return mapping.findForward("detalle");
					}
					forma.setPatronOrdenar("consecutivo_");
					accionOrdenar(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listado");
				}
				else if(estado.equals("buscarNuevo"))
				{
					//forma.reset();
					forma.setMapaResultadoFacturas(mundo.BusquedaFacturas(con ,forma.getFechaInicial(), forma.getFechaFinal(), forma.getFactura(), forma.getEstadosFactura(), forma.getTipoDeudor(), forma.getDeudor()));
					int numReg=Utilidades.convertirAEntero(forma.getMapaResultadoFacturas("numRegistros")+"");
					if(numReg==1)
					{	
						forma.reset();
						UtilidadBD.closeConnection(con);
						return mapping.findForward("principal");
					}
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listado");
				}
				else if(estado.equals("detalleFactura"))
				{
					this.accionCargarDetalleFactura(con, forma, mundo, usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");
				}
				else if(estado.equals("nuevaBusqueda"))
				{
					forma.reset();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("ordernar"))
				{
					this.accionOrdenar(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listado");
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
					this.generarReporte(con, forma, mapping, request, usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");
				}
				else
				{
					forma.reset();
					logger.warn("Estado no valido dentro del flujo de CONSULTA FACTURAS VARIAS ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}		}
			else
			{
				logger.error("El form no es compatible con el form de ConsultaFacturasVariasForm");
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
	 * 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request
	 * @param usuarioActual
	 * @return
	 */
	private ActionForward generarReporte(Connection con, ConsultaFacturasVariasForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuarioActual) 
	{
		
//		String nombreRptDesign = "" ;
//		if(Utilidades.convertirAEntero(ValoresPorDefecto.getFormatoFacturaVaria(usuarioActual.getCodigoInstitucionInt()))==ConstantesBD.codigoFormatoImpresionEstandar) // carta
//		{
//			nombreRptDesign = "ConsultaImpresionFacturasVarias.rptdesign";
//		}
//		if(Utilidades.convertirAEntero(ValoresPorDefecto.getFormatoFacturaVaria(usuarioActual.getCodigoInstitucionInt()))==ConstantesBD.codigoFormatoImpresionSonria) // POS
//		{
//			nombreRptDesign = "ConsultaImpresionFacturasVariasPOS.rptdesign";
//		}
//			
//		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
//		
		//Informacion del Cabezote
//		DesignEngineApi comp;
//		comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturasVarias/",nombreRptDesign);
//		
//		comp.insertGridHeaderOfMasterPageWithName(0, 1, 1, 2, "titulo");
//		Vector v = new Vector();
//		
//		if(forma.getEstadosFactura().equals(ConstantesIntegridadDominio.acronimoEstadoAnulado))
//		{
//			v.add(forma.getEstadosFactura());
//		}
//		v.add(institucion.getRazonSocial());
//		v.add(institucion.getTipoIdentificacion()+" "+institucion.getNit());
//		v.add("Sucursal: "+usuarioActual.getCentroAtencion());
//		
//		comp.insertLabelInGridOfMasterPage(0, 1, v);
//
//		boolean manejaMultiInstitucion= UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(usuarioActual.getCodigoInstitucionInt()));
//		int codigoInstitucion=0;
//		
//		if(manejaMultiInstitucion){
//			codigoInstitucion= Utilidades.convertirAEntero(institucion.getCodigo());
//		}
//		else{
//			codigoInstitucion= usuarioActual.getCodigoInstitucionInt();
//		}
//		
//		comp.obtenerComponentesDataSet("ConsultaFacturasVarias");
//		String newquery=ConsultasBirt.impresionFacturaVaria(codigoInstitucion,Utilidades.convertirAEntero(forma.getDetalleFactura().get("consecutivo_0")+""), manejaMultiInstitucion);
//		comp.modificarQueryDataSet(newquery);
//		
//		//debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
//		comp.lowerAliasDataSet();
//		String newPathReport = comp.saveReport1(false);
//		comp.updateJDBCParameters(newPathReport);
//		// se mandan los parámetros al reporte
//		newPathReport += "&institucion="+codigoInstitucion+"&consecutivoFactura="+Utilidades.convertirAEntero(forma.getDetalleFactura().get("consecutivo_0")+"");
//		
//		GenModFacturasVarias mundo = new GenModFacturasVarias ();
//		
//		mundo.imprimirFacturaVaria(codigoInstitucion, usuarioActual.getCodigoInstitucionInt(), Utilidades.convertirAEntero(forma.getDetalleFactura().get("consecutivo_0")+""));
//		
		//--------------------------
//		Vector v = new Vector();
//        v.add(institucion.getRazonSocial());
//        v.add(usuarioActual.getCentroAtencion());
//        v.add(Utilidades.getDescripcionTipoIdentificacion(con,institucion.getTipoIdentificacion())+"  -  "+institucion.getNit());
//        v.add(institucion.getDireccion());
//        v.add("Tels. "+institucion.getTelefono());
//        comp.insertLabelInGridOfMasterPage(0, 1, v);
//        
//        comp.insertLabelInGridPpalOfFooter(0, 0, "Usuario: "+usuarioActual.getLoginUsuario());
//        comp.insertLabelInGridPpalOfFooter(0, 1, "Fecha: "+UtilidadFecha.getFechaActual());
//        
//        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
//      	comp.lowerAliasDataSet();
//		String newPathReport = comp.saveReport1(false);
//        comp.updateJDBCParameters(newPathReport);
//        // se mandan los parámetros al reporte
//        newPathReport += "&institucion="+usuarioActual.getCodigoInstitucion()+"&consecutivoFactura="+Utilidades.convertirAEntero(forma.getDetalleFactura().get("consecutivo_0")+"");
//        logger.info(newPathReport);
        
		
		InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		GenModFacturasVarias genModFacturasVariasMundo = new GenModFacturasVarias();
	
		String newPathReport = genModFacturasVariasMundo.imprimirFacturaVaria(usuarioActual.getCodigoInstitucionInt(), Utilidades.convertirAEntero(institucion.getCodigo()),
								Utilidades.convertirAEntero(forma.getDetalleFactura().get("consecutivo_0")+""), usuarioActual);
		
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalle");
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionCargarDetalleFactura(Connection con, ConsultaFacturasVariasForm forma, ConsultaFacturasVarias mundo, UsuarioBasico usuario)
	{
		forma.setDetalleFactura(mundo.consultaDetalleFactura(con, Utilidades.convertirAEntero(forma.getFacturaVaria()), forma.getTipoDeudorFac()));
		
	}
	
	
	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenar(ConsultaFacturasVariasForm forma) 
	{
		int numReg=Utilidades.convertirAEntero(forma.getMapaResultadoFacturas("numRegistros")+"");
		forma.setMapaResultadoFacturas(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaResultadoFacturas(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setMapaResultadoFacturas("numRegistros",numReg+"");
		
	}
	

}
