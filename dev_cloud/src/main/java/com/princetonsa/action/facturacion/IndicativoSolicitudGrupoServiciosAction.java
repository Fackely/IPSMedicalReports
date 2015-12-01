package com.princetonsa.action.facturacion;

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
import util.LogsAxioma;
import util.UtilidadBD;
import util.Utilidades;

import com.princetonsa.actionform.facturacion.IndicativoSolicitudGrupoServiciosForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.IndicativoSolicitudGrupoServicios;

public class IndicativoSolicitudGrupoServiciosAction extends Action
{
	 /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(IndicativoSolicitudGrupoServiciosAction.class);
	
	/**
	 * Método execute del action
	 */
	public ActionForward execute(	ActionMapping mapping, 	
													        ActionForm form, 
													        HttpServletRequest request, 
													        HttpServletResponse response) throws Exception
													        {
		Connection con = null;
		try{
			if (form instanceof IndicativoSolicitudGrupoServiciosForm)
			{

				con=UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				IndicativoSolicitudGrupoServiciosForm forma = (IndicativoSolicitudGrupoServiciosForm) form;

				UsuarioBasico usuario = null;
				usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());

				IndicativoSolicitudGrupoServicios mundo=new IndicativoSolicitudGrupoServicios();

				String estado = forma.getEstado();

				logger.warn("estado --> "+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de IndicativoSolicitudGrupoServiciosAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}			
				else if (estado.equals("empezar"))
				{
					forma.reset();
					forma.setGruposServicios(mundo.consultarGruposServiciosInstitucion(con, usuario.getCodigoInstitucion(), ConstantesBD.codigoTipoGrupoServiciosLaboratorios));
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("cargarProcedimientos"))
				{
					forma.setProcedimientos(new HashMap());
					forma.setProcedimientos("numRegistros","0");
					forma.setProcedimientos(mundo.consultarServiciosGrupoServicioTipo(con, forma.getGrupoServicio(), ConstantesBD.codigoServicioProcedimiento+""));
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("guardar"))
				{
					this.accionGuardarParametrizacion(con,forma,mundo,usuario);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("activarDesactivarTomaMuestra"))
				{
					for(int i=0;i<Integer.parseInt(forma.getProcedimientos("numRegistros")+"");i++)
						forma.setProcedimientos("tomamuestra_"+i, forma.isActivar()?"S":"N");
					UtilidadBD.cerrarConexion(con);
					//retornamos null, ya que no esperamos retorno alguno
					return null;
				}
				else if(estado.equals("activarDesactivarRespuestaMultiple"))
				{
					for(int i=0;i<Integer.parseInt(forma.getProcedimientos("numRegistros")+"");i++)
						forma.setProcedimientos("respuestamultiple_"+i, forma.isActivar()?"S":"N");
					UtilidadBD.cerrarConexion(con);
					//retornamos null, ya que no esperamos retorno alguno
					return null;
				}			
				else
				{
					forma.reset();
					logger.warn("Estado no valido dentro del flujo de IndicativoSolicitudGrupoServicios ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de IndicativoSolicitudGrupoServiciosAction");
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
	private void accionGuardarParametrizacion(Connection con, IndicativoSolicitudGrupoServiciosForm forma, IndicativoSolicitudGrupoServicios mundo, UsuarioBasico usuario)
	{
		int tamanioMapa=Integer.parseInt(forma.getProcedimientos("numRegistros")+"");
		boolean enTransaccion=UtilidadBD.iniciarTransaccion(con);
		for(int i=0;i<tamanioMapa;i++)
		{
			if(!mundo.actualizarServicioProcedimiento(con, forma.getProcedimientos("codigo_"+i)+"", forma.getProcedimientos("tomamuestra_"+i)+"", forma.getProcedimientos("respuestamultiple_"+i)+""))
			{
				enTransaccion=false;
				i=tamanioMapa;
			}
			
		}
		if(enTransaccion)
		{
			this.generarLog(forma,usuario);
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}
		
		//cargar nuevamente la parametrizacion
		forma.setProcedimientos(new HashMap());
		forma.setProcedimientos("numRegistros","0");
		forma.setProcedimientos(mundo.consultarServiciosGrupoServicioTipo(con, forma.getGrupoServicio(), ConstantesBD.codigoServicioProcedimiento+""));
	}

	/**
	 * 
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void generarLog(IndicativoSolicitudGrupoServiciosForm forma,  UsuarioBasico usuario)
	{
		String log = "";
		int tipoLog=2;
		log = 		 "\n   ============INDICATIVO SOLICITUD POR GRUPO DE SERVICIOS MODIFICADO=========== "+
					"\n Codigo Grupo: "+forma.getGrupoServicio()+" ";
		LogsAxioma.enviarLog(ConstantesBD.logIndicativosolicitudGrupoServicioCodigo,log,tipoLog,usuario.getLoginUsuario());
	}
}
