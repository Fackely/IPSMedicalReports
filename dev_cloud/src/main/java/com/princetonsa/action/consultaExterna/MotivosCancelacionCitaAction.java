package com.princetonsa.action.consultaExterna;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.actionform.consultaExterna.MotivosCancelacionCitaForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.consultaExterna.MotivosCancelacionCita;


public class MotivosCancelacionCitaAction extends Action 
{

	
	
	/**
	 * objeto para manejar los logs de esta clase
	 */
	
	Logger logger =Logger.getLogger(MotivosCancelacionCitaAction.class);
	
	/**
	 * 
	 */
	private static String[] indicesMotivos={"codigo_","descripcion_","activo_","tipocancelacion_","tiporegistro_"};
	
	
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		
		try {
		
		if (form instanceof MotivosCancelacionCitaForm) 
		{
			MotivosCancelacionCitaForm forma=(MotivosCancelacionCitaForm) form;
			
			ActionErrors errores = new ActionErrors();
			
			String estado=forma.getEstado();
			
			logger.info("Estado -->"+estado);
			
			con = UtilidadBD.abrirConexion();
			UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
			
			MotivosCancelacionCita mundo=new MotivosCancelacionCita();
			
			forma.setMensaje(new ResultadoBoolean(false));
			
			if(estado == null)
			{
				logger.warn("Estado no valido dentro del flujo de AprobacionAnulacionDevolucionesAction (null) ");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
			else if(estado.equals("empezar"))
			{
				forma.reset();
				this.accionConsultarMotivos(con, forma, mundo);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("principal");
			}
			else if(estado.equals("nuevo"))
			{
				Utilidades.nuevoRegistroMapaGenerico(forma.getMapaMotivos(),indicesMotivos,"numRegistros","tiporegistro_","MEM");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("principal");
			}
			else if(estado.equals("eliminarMotivo"))
			{
				//eliminar especialidad
				Utilidades.eliminarRegistroMapaGenerico(forma.getMapaMotivos(),forma.getMapaMotivosEliminados(),forma.getIndiceMotivoEliminar(),indicesMotivos,"numRegistros","tiporegistro_","BD",false);
				forma.setIndiceMotivoEliminar(ConstantesBD.codigoNuncaValido);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("principal");
			}
			else if(estado.equals("guardar"))
			{
				
				//guardamos en BD.
				this.accionGuardarRegistros(con,forma,mundo,usuario);

				//limipiamos el form
				forma.reset();
				this.accionConsultarMotivos(con,forma,mundo);
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("principal");

			}
			else
			{
				forma.reset();
				logger.warn("Estado no valido dentro del flujo de MOTIVOS CANCELACION CITA ");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}	
		}
		else
		{
			logger.error("El form no es compatible con el form de MotivosCancelacionCitaForm");
			request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
			return mapping.findForward("paginaError");
		}
		
		} catch (Exception e) {
			Log4JManager.error(e);
			return null;
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardarRegistros(Connection con, MotivosCancelacionCitaForm forma, MotivosCancelacionCita mundo, UsuarioBasico usuario)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		logger.info("mapaPaquertes-->"+forma.getMapaMotivos());
		
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getMapaMotivosEliminados("numRegistros")+"");i++)
		{
			if(mundo.eliminarRegistro(con, Utilidades.convertirAEntero(forma.getMapaMotivosEliminados("codigo_"+i)+"")))
			{
				transaccion=true;
			}
		}
		
		for(int i=0;i<Integer.parseInt(forma.getMapaMotivos("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getMapaMotivos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma,mundo,Integer.parseInt(forma.getMapaMotivos("codigo_"+i)+""),i,usuario))
			{
				HashMap vo=new HashMap();
				vo.put("codigo",forma.getMapaMotivos("codigo_"+i));
				vo.put("descripcion",forma.getMapaMotivos("descripcion_"+i));
				vo.put("tipo_cancelacion",forma.getMapaMotivos("tipocancelacion_"+i));
				vo.put("activo",forma.getMapaMotivos("activo_"+i));
				vo.put("usuario_modifica",usuario.getLoginUsuario());
				vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				vo.put("hora_modifica",UtilidadFecha.getHoraActual());
				
				transaccion=mundo.modificar(con, vo);
			}
			
			//insertar
			else if((forma.getMapaMotivos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("descripcion",forma.getMapaMotivos("descripcion_"+i));
				vo.put("tipo_cancelacion",forma.getMapaMotivos("tipocancelacion_"+i));
				vo.put("activo",forma.getMapaMotivos("activo_"+i));
				vo.put("usuario_modifica",usuario.getLoginUsuario());
				vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				vo.put("hora_modifica",UtilidadFecha.getHoraActual());
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
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param codigoMotivo
	 * @param pos
	 * @param usuario
	 * @return
	 */
	private boolean existeModificacion(Connection con, MotivosCancelacionCitaForm forma, MotivosCancelacionCita mundo,int codigoMotivo,int pos, UsuarioBasico usuario)
	{
		HashMap temp=mundo.consultarMotivoEspecifico(con, codigoMotivo);
		for(int i=0;i<indicesMotivos.length;i++)
		{
			if(temp.containsKey(indicesMotivos[i]+"0")&&forma.getMapaMotivos().containsKey(indicesMotivos[i]+""+pos))
			{
				if(!((temp.get(indicesMotivos[i]+"0")+"").trim().equals(forma.getMapaMotivos(indicesMotivos[i]+""+pos)+"")))
				{
					mundo.setMapaMotivo(temp);
					return true;
				}
			}
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 */
	private void accionConsultarMotivos(Connection con, MotivosCancelacionCitaForm forma, MotivosCancelacionCita mundo)
	{
		forma.setMapaMotivos(mundo.consultarMotivosExistentes(con));
	}
	
	
	
}
