package com.princetonsa.action.inventarios;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.inventarios.DevolucionInventariosPacienteForm;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.DevolucionInventariosPaciente;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class DevolucionInventariosPacienteAction extends Action {
	
	/**
	 * Para manejar los logs
	 */
	private Logger logger=Logger.getLogger(DevolucionInventariosPacienteAction.class);
	
	/**
	 * Indices del mapa de ingreso/cuenta del paciente cargado
	 */
	private String[] indicesIngresoCuenta={"idingreso_","consecutivoingreso_","estadoingreso_","fechaingreso_","horaingreso_",
											"fechaegreso_","horaegreso_","cuenta_","codestadocuenta_",
											"nomestadocuenta_","codviaingreso_","nomviaingreso_"};
	
	/**
	 * Indices del mapa de pedidos por paciente cargado
	 */
	private String[] indicesPedidos={"codigo_","fecha_","codfarmacia_","codcentrodevuelve_",
										"nombrefarmacia_","nombrecentrodevuelve_"};
	
	/**
	 * Indices del mapa de las solicitudes
	 */
	private String[] indicesSolicitudes={"solicitud_","codfarmacia_","nombrefarmacia_","orden_","nombrecentrodevuelve_"};
	
	/**
	 * Indices del mapa de los detalles de las solicitudes
	 */
	private String[] indicesDetalleSolicitudes={"articulo_","cantidad_","lote_","fechavencimiento_"};
	
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request,HttpServletResponse response)
	{

		Connection con=null;
		try{

			if(form instanceof DevolucionInventariosPacienteForm) 
			{
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
				PersonaBasica paciente=(PersonaBasica)request.getSession().getAttribute("pacienteActivo");

				DevolucionInventariosPacienteForm forma=(DevolucionInventariosPacienteForm) form;
				DevolucionInventariosPaciente mundo=new DevolucionInventariosPaciente();

				con=UtilidadBD.abrirConexion();

				String estado=forma.getEstado();

				logger.warn("Estado [DevolucionInventariosPacienteAction]-->> "+estado);

				//Validar que el paciente este cargado en sesion
				if(paciente.getCodigoPersona()<1)
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente no Cargado", "errors.paciente.noCargado", true);
				}
				forma.setMensaje(new ResultadoBoolean(false));
				if(estado.equals("empezar"))
				{
					forma.reset();
					forma.setIngresoCuentaPaciente(mundo.consultarIngresoCuentaPaciente(con, paciente.getCodigoPersona(), usuario.getCodigoCentroAtencion()));
					if(Utilidades.convertirAEntero(forma.getIngresoCuentaPaciente().get("numRegistros")+"",false)>0)
					{
						UtilidadBD.closeConnection(con);
						return mapping.findForward("listadoIngresos");
					}
					else
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Estado Cuenta no permite Devolucion", "error.devolucionInventariosPaciente.cuentaNoPermiteDevolucion", true);
					}
				}
				else if(estado.equals("devolucion"))
				{
					this.accionCargarDevolucion(con, forma, mundo, paciente);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listadoADevolver");
				}
				else if(estado.equals("listadoSolicitudes"))
				{
					this.accionCargarSolicitudes(con, forma, mundo, paciente);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listadoADevolver");
				}
				else if(estado.equals("listadoPedidos"))
				{
					this.accionCargarPedidos(con, forma, mundo, paciente);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listadoADevolver");
				}
				else if(estado.equals("detalleDevolucion"))
				{
					this.accionCargarDetalleSolicitud(con, forma, mundo, usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalleADevolver");
				}
				else if(estado.equals("generarReporte"))
				{                
					this.generarReporte(con, forma, mapping, request, usuario,paciente);                
					UtilidadBD.closeConnection(con);
					return mapping.findForward("resumenDevolucion");
				}
				else if(estado.equals("guardarDevolucion"))
				{
					//Valida que la fecha de devolucion no sea menor a la fecha de despacho
					ActionErrors errores = new ActionErrors();
					forma.setFechaHoraDespacho(mundo.consultarFechaHoraDespachoSolicitudes(con, Utilidades.convertirAEntero(forma.getDespacho())));
					int numReg=Utilidades.convertirAEntero(forma.getFechaHoraDespacho().get("numRegistros")+"");
					for(int i=0;i<numReg;i++)
					{
						if(!UtilidadFecha.compararFechas(forma.getFecha(),forma.getHora(),UtilidadFecha.conversionFormatoFechaAAp(forma.getFechaHoraDespacho().get("fecha_"+i)+""),forma.getFechaHoraDespacho().get("hora_"+i)+"").isTrue())
						{
							errores.add("Fecha/Hora Devolucion anterior a fecha despacho",new ActionMessage("errors.fechaHoraAnteriorIgualActual","de la devolución","del despacho de la solicitud "));
						}
					}
					if(errores.isEmpty())
					{
						this.accionGuardarDevolucion(con,forma,mundo,paciente,usuario);
						forma.setDevolucionMap(mundo.consultaDevolucionSolicitudes(con, forma.getDevolucion()));
						forma.setDetalleDevolucionMap(mundo.consultaDetalleDevolucionSolicitudes(con, forma.getDevolucion()));
						UtilidadBD.closeConnection(con);
						return mapping.findForward("resumenDevolucion");
					}
					else
					{
						saveErrors(request, errores);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("detalleADevolver");
					}
				}
				else
				{
					forma.reset();
					logger.warn("Estado no valido dentro del flujo de Devolucion de Inventarios Paciente ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de DevolucionInventariosPacienteForm");
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
	
	private void accionCargarDevolucion(Connection con,DevolucionInventariosPacienteForm forma,DevolucionInventariosPaciente mundo,PersonaBasica paciente)
	{
		forma.setPedidos(mundo.consultarPedidos(con, paciente.getCodigoPersona()));
		forma.setSolicitudes(mundo.consultarSolicitudes(con, paciente.getCodigoPersona()));
	}
	
	private void accionCargarPedidos(Connection con, DevolucionInventariosPacienteForm forma, DevolucionInventariosPaciente mundo,PersonaBasica paciente)
	{
		forma.setPedidos(mundo.consultarPedidos(con, paciente.getCodigoPersona()));
	}
	
	private void accionCargarSolicitudes(Connection con, DevolucionInventariosPacienteForm forma, DevolucionInventariosPaciente mundo,PersonaBasica paciente)
	{
		forma.setSolicitudes(mundo.consultarSolicitudes(con, paciente.getCodigoPersona()));
	}
	
	private void accionGuardarDevolucion(Connection con,DevolucionInventariosPacienteForm forma, DevolucionInventariosPaciente mundo,PersonaBasica paciente,UsuarioBasico usuario)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		HashMap<String, Object> vo=new HashMap<String, Object>();
		vo.put("observaciones", forma.getObservaciones());
		vo.put("fecha", UtilidadFecha.conversionFormatoFechaABD(forma.getFecha()));
		vo.put("hora", forma.getHora());
		vo.put("usuario", usuario.getLoginUsuario());
		vo.put("codcentrodevuelve", forma.getSolicitudes().get("codcentrodevuelve_"+forma.getIndiceDetalle()));
		vo.put("codfarmacia", forma.getSolicitudes().get("codfarmacia_"+forma.getIndiceDetalle()));
		vo.put("motivo", forma.getMotivo());
		vo.put("institucion", usuario.getCodigoInstitucion());
		forma.setDevolucion(mundo.insertarDevolucionSolicitudes(con, vo));
		if(forma.getDevolucion()>0)
		{
			for(int i=0;i<Utilidades.convertirAEntero(forma.getDetalleSolicitudes().get("numRegistros")+"");i++)
			{
				HashMap<String, Object> map=new HashMap<String, Object>();
				map.put("devolucion", forma.getDevolucion());
				map.put("numerosolicitud", forma.getSolicitudes().get("solicitud_"+forma.getIndiceDetalle())+"");
				map.put("articulo", forma.getDetalleSolicitudes().get("articulo_"+i)+"");
				map.put("cantidad", forma.getDetalleSolicitudes().get("cantidaddevolver_"+i)+"");
				map.put("lote", forma.getDetalleSolicitudes().get("lote_"+i));
				map.put("fechavencimiento", forma.getDetalleSolicitudes().get("fechavencimiento_"+i));
				mundo.insertarDetalleDevolucionSolicitudes(con, map);
			}
		}
		else
		{
			transaccion=false;
		}
		if(transaccion)
		{
			forma.setMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!"));
			logger.info("OPERACION REALIZADA");
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMensaje(new ResultadoBoolean(true,"NO SE PUDO INSERTAR EL REGISTRO."));
			logger.info("OPERACION NO REALIZADA");
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.closeConnection(con);
		}
	}
	
	private void accionCargarDetalleSolicitud(Connection con, DevolucionInventariosPacienteForm forma, DevolucionInventariosPaciente mundo,UsuarioBasico usuario)
	{
		forma.setDetalleSolicitudes(mundo.consultaDetalleSolicitudes(con, Utilidades.convertirAEntero(forma.getDespacho())));
		forma.setUsuario(usuario.getLoginUsuario());
		forma.setFecha(UtilidadFecha.getFechaActual());
		forma.setHora(UtilidadFecha.getHoraActual());
	}
	
	private ActionForward generarReporte(Connection con, DevolucionInventariosPacienteForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuarioActual, PersonaBasica paciente) 
	{
		String nombreRptDesign = "DevolucionInventariosPaciente.rptdesign";
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		//PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"inventarios/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getTipoIdentificacion()+"         "+ins.getNit());     
        v.add(ins.getDireccion());
        v.add("Tels. "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        comp.insertLabelInGridPpalOfHeader(2,1, "                    DEVOLUCION INVENTARIOS PACIENTE.");
        comp.insertLabelInGridPpalOfHeader(2,0, "Fecha Devolucion: "+forma.getFecha()+"   "+forma.getHora());
        
        
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
        
        // se mandan los parámetros al reporte
        newPathReport += "&codigoDevolucion="+forma.getDevolucion();
        	//"&subCuenta="+forma.getResponsableCuenta().getSubCuenta();
        	
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
        
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("resumenDevolucion");
	}

}
