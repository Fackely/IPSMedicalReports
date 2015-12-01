package com.princetonsa.action.manejoPaciente;

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
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.actionform.manejoPaciente.ValoresTipoReporteForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.ValoresTipoReporte;

public class ValoresTipoReporteAction extends Action 
{

	
	
	/**
	 * objeto para manejar los logs de esta clase
	 */
	Logger logger =Logger.getLogger(ValoresTipoReporteAction.class);
	
	/**
	 * 
	 */
	String[] indices={"codigo_","orden_","institucion_","nombreetiqueta_","activo_","tiporegistro_","rangoinicial_","rangofinal_"};
	
	
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{
			if (form instanceof ValoresTipoReporteForm) 
			{
				ValoresTipoReporteForm forma=(ValoresTipoReporteForm) form;

				String estado=forma.getEstado();

				logger.info("Estado -->"+estado);

				con=UtilidadBD.abrirConexion();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());

				ValoresTipoReporte mundo=new ValoresTipoReporte();

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ValoresTipoReporteAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					forma.reset();
					forma.resetMensaje();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("cargarReportes"))
				{
					forma.resetMensaje();
					forma.setCodigoReporte("");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("cargarInfo"))
				{
					forma.resetMensaje();
					forma.setMapaInfoReporte(mundo.consultarInfoTipoReporte(con, forma.getCodigoReporte()));
					forma.setMapaConsultaParametrizado(mundo.consultarParametrizacion(con, forma.getCodigoReporte()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("nuevo"))
				{
					forma.resetMensaje();
					this.accionNuevoRegistro(forma,usuario.getCodigoInstitucionInt());
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("eliminar"))
				{
					forma.resetMensaje();
					this.accionEliminar(con, forma, mundo, usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("guardar"))
				{
					//guardamos en BD.
					this.accionGuardar(con, forma, mundo, usuario);
					forma.setMapaConsultaParametrizado(mundo.consultarParametrizacion(con, forma.getCodigoReporte()));
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");

				}
				else
				{
					forma.reset();
					logger.warn("Estado no valido dentro del flujo de RANGOS DE VALORES POR REPORTE");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}		
			}
			else
			{
				logger.error("El form no es compatible con el form de ValoresTipoReporteForm");
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
	 * @param mundo
	 * @param usuario
	 */
	private void accionEliminar(Connection con, ValoresTipoReporteForm forma, ValoresTipoReporte mundo, UsuarioBasico usuario) 
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		if((forma.getMapaConsultaParametrizado("tiporegistro_"+forma.getIndiceEliminado())+"").trim().equalsIgnoreCase("BD"))
		{
	
			if(mundo.eliminarRegistro(con, forma.getMapaConsultaParametrizado("codigo_"+forma.getIndiceEliminado())+""))
			{
				forma.setMapaConsultaParametrizado(mundo.consultarParametrizacion(con, forma.getCodigoReporte()));
				transaccion=true;
			}
		}
		else if((forma.getMapaConsultaParametrizado("tiporegistro_"+forma.getIndiceEliminado())+"").trim().equalsIgnoreCase("MEM"))
		{
			Utilidades.eliminarRegistroMapaGenerico(forma.getMapaConsultaParametrizado(), forma.getMapaEliminado(), forma.getIndiceEliminado(), indices, "numRegistros", "tipoRegistro", "BD", false);
		}
		if(transaccion)
		{
			forma.setMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
		
	}


	/**
	 * 
	 * @param forma
	 * @param institucion
	 */
	private void accionNuevoRegistro(ValoresTipoReporteForm forma, int institucion) 
	{
		int pos=Integer.parseInt(forma.getMapaConsultaParametrizado("numRegistros")+"");
		forma.setMapaConsultaParametrizado("orden_"+pos,"");
		forma.setMapaConsultaParametrizado("institucion_"+pos,institucion+"");
		forma.setMapaConsultaParametrizado ("nombreetiqueta_"+pos,"");
		forma.setMapaConsultaParametrizado ("rangoinicial_"+pos,"");
		forma.setMapaConsultaParametrizado ("rangofinal_"+pos,"");
		forma.setMapaConsultaParametrizado ("activo_"+pos,ConstantesBD.acronimoSi);
		forma.setMapaConsultaParametrizado("tiporegistro_"+pos,"MEM");
		forma.setMapaConsultaParametrizado("numRegistros", (pos+1)+"");
		
	}
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardar(Connection con, ValoresTipoReporteForm forma, ValoresTipoReporte mundo, UsuarioBasico usuario)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		for(int i=0;i<Utilidades.convertirAEntero(forma.getMapaConsultaParametrizado("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getMapaConsultaParametrizado("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD"))
			{
				HashMap vo=new HashMap();
				vo.put("codigo",forma.getMapaConsultaParametrizado("codigo_"+i));
				vo.put("orden",forma.getMapaConsultaParametrizado("orden_"+i));
				vo.put("rangoinicial",forma.getMapaConsultaParametrizado("rangoinicial_"+i));
				vo.put("rangofinal",forma.getMapaConsultaParametrizado("rangofinal_"+i));
				vo.put("nombre",forma.getMapaConsultaParametrizado("nombreetiqueta_"+i));
				vo.put("activo",forma.getMapaConsultaParametrizado("activo_"+i));
				vo.put("usuariomodifica",usuario.getLoginUsuario());
				vo.put("fechamodifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				vo.put("horamodifica",UtilidadFecha.getHoraActual());
				transaccion=mundo.modificar(con, vo);
			}
			
			//insertar
			else if((forma.getMapaConsultaParametrizado("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigo",forma.getMapaConsultaParametrizado("codigo_"+i));
				vo.put("orden",forma.getMapaConsultaParametrizado("orden_"+i));
				vo.put("rangoinicial",forma.getMapaConsultaParametrizado("rangoinicial_"+i));
				vo.put("rangofinal",forma.getMapaConsultaParametrizado("rangofinal_"+i));
				vo.put("nombre",forma.getMapaConsultaParametrizado("nombreetiqueta_"+i));
				vo.put("reporte",forma.getCodigoReporte());
				vo.put("activo",forma.getMapaConsultaParametrizado("activo_"+i));
				vo.put("institucion",usuario.getCodigoInstitucion());
				vo.put("usuariomodifica",usuario.getLoginUsuario());
				vo.put("fechamodifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				vo.put("horamodifica",UtilidadFecha.getHoraActual());
				transaccion=mundo.insertar(con, vo);
			}
			
		}
		if(transaccion)
		{
			forma.setMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
	}
	
	
	
}
