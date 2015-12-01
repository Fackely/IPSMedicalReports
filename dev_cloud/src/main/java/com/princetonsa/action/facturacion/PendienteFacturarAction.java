package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.util.Vector;

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
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;

import com.princetonsa.actionform.facturacion.PendienteFacturarForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.PendienteFacturar;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

public class PendienteFacturarAction extends Action 
{
	
	/**
	 * 
	 */
	Logger logger =Logger.getLogger(PendienteFacturarAction.class);
	
	/**
	 * 
	 */
	private String[] indices={
								"codigomedico_",
								"tipoidentificacion_",
								"numeroidentificacion_",
								"apellidos_",
								"nombres_",
								"cantidad_",
								"cantidadtotal_",
								"total_",
								"valorcargado_"
							 };
	
	
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{
			if (form instanceof PendienteFacturarForm) 
			{
				PendienteFacturarForm forma=(PendienteFacturarForm) form;

				String estado=forma.getEstado();

				PendienteFacturar mundo=new PendienteFacturar();

				logger.info("Estado -->"+estado);

				con=UtilidadBD.abrirConexion();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());


				//forma.setMensaje(new ResultadoBoolean(false));
				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ConceptosCarteraAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar"))
				{
					forma.reset();
					return mapping.findForward("principal");
				}
				else if(estado.equals("continuar"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if (estado.equals("buscar"))
				{
					return this.accionConsultar(con, forma, mapping, usuario, request);
				}
				else if (estado.equals("imprimir"))
				{
					this.generarReporte(con, forma, mapping, request, usuario, mundo);                
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("resultadoBusqueda");
				}
				else if(estado.equals("ordernar"))
				{
					this.accionOrdenar(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("resultadoBusqueda");
				}
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.closeConnection(con);
					if(UtilidadTexto.isEmpty(forma.getLinkSiguiente()))
						forma.setLinkSiguiente("../honorariosPendientesFacturar/honorariosPendientesFacturar.do?estado=continuar");
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else if(estado.equals("continuar"))
				{
					UtilidadBD.closeConnection(con);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getConsulta("numRegistros").toString()), response, request, "resultadoBusquedaHonorarios.jsp",true);
				}
				else
				{
					forma.reset();
					logger.warn("Estado no valido dentro del flujo de EDAD CARTERA CAPITACION ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de EdadCarteraCapitacionForm");
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
	 * @param usuario
	 * @param request
	 * @return
	 */
	private ActionForward accionConsultar(Connection con, PendienteFacturarForm forma, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
//		Se instancia objeto mundo
		
		PendienteFacturar mundo = new PendienteFacturar();
		
		forma.setConsulta(mundo.consultarHonorariosPendientes(con, llenarMundoBusqueda(forma, mundo)));
		
		return mapping.findForward("resultadoBusqueda");
		
		
	}
	
	
	/**
	 * 
	 * @param forma
	 * @param mundo
	 * @return
	 */
	private boolean llenarMundoBusqueda(PendienteFacturarForm forma,PendienteFacturar mundo)
	{
		
		mundo.setFechaCorte("");
		if(!forma.getFechaCorte().equals(""))
		{
			mundo.setFechaCorte(forma.getFechaCorte());
		}
		mundo.setMedico("");
		if(!forma.getMedico().equals(""))
		{
			mundo.setMedico(forma.getMedico());
		}
		
		return false;
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
	private ActionForward generarReporte(Connection con, PendienteFacturarForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuarioActual, PendienteFacturar mundo) 
	{
		String nombreRptDesign = "HonorariosPendientesFacturar.rptdesign";
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		//PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,5);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getDescripcionTipoIdentificacion()+"         "+ins.getNit());     
        v.add(ins.getDireccion());
        v.add("Tels. "+ins.getTelefono());
        v.add("HONORARIOS PENDIENTES POR FACTURAR");
        comp.insertLabelInGridOfMasterPage(0,1,v);
        comp.insertLabelInGridPpalOfHeader(0,2, "");
        String filtroMedico = "      Profesional de la salud: ";
        if(Utilidades.convertirAEntero(forma.getMedico())>0)
        {
        	filtroMedico += UtilidadValidacion.obtenerNombrePersona(con, Utilidades.convertirAEntero(forma.getMedico())).toLowerCase();
        }
        else
        {
        	filtroMedico += "Todos";
        }
        comp.insertLabelInGridPpalOfHeader(2,0, "[Fecha corte: "+forma.getFechaCorte()+filtroMedico+"]");
        
        comp.insertLabelInGridPpalOfFooter(0, 0, UtilidadFecha.getFechaActual(con)+" - "+UtilidadFecha.getHoraActual(con));
        comp.insertLabelInGridPpalOfFooter(0, 1, "Usuario: "+usuarioActual.getLoginUsuario());
        
        
        //SE MODIFICA LA CONSULTA PARA QUE TOME TODOS LOS CODIGOS DE LOS DETALLES CARGO INSERTADOS
        comp.obtenerComponentesDataSet("HonorariosPendientes");            
        String oldQuery=comp.obtenerQueryDataSet();
        
        String condiciones="";
        if(!UtilidadTexto.isEmpty(forma.getMedico()))
        {
        	condiciones+=" AND codigo_medico="+forma.getMedico()+" ";
        }
        
        String newQuery=comp.obtenerQueryDataSet()+" AND fecha_corte='"+UtilidadFecha.conversionFormatoFechaABD(forma.getFechaCorte())+"'"+condiciones+ "AND tipo_solicitud not in(6,9,13,15) group by 1,2,3,4,5";
        comp.modificarQueryDataSet(newQuery);
        
        
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
        
        // se mandan los parámetros al reporte
       // newPathReport += "&fechaCorte="+UtilidadFecha.conversionFormatoFechaABD(forma.getFechaCorte());
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
        
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("resultadoBusqueda");
	}
	
	
	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenar(PendienteFacturarForm forma) 
	{
		int numReg=Utilidades.convertirAEntero(forma.getConsulta("numRegistros")+"");
		String cantidadTotal="", total="";
		cantidadTotal=forma.getConsulta("cantidadtotal")+"";
		total=forma.getConsulta("total")+"";
		forma.setConsulta(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getConsulta(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setConsulta("numRegistros",numReg+"");
		forma.setConsulta("cantidadtotal", cantidadTotal);
		forma.setConsulta("total", total);
	}
	
	
	

}
