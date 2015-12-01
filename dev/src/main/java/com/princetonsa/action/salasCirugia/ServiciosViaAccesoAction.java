package com.princetonsa.action.salasCirugia;

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
import util.UtilidadSesion;
import util.Utilidades;
import util.ValoresPorDefecto;
import com.princetonsa.actionform.salasCirugia.ServiciosViaAccesoForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.salasCirugia.ServiciosViaAcceso;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class ServiciosViaAccesoAction extends Action {
	
	/**
	 * Objeto para manejar los logs de la clase
	 */
	private Logger logger=Logger.getLogger(ServiciosViaAccesoAction.class);
	
	//String[] indices={"fueEliminadoServicio_", "codigservicio_", "codigoTipoCirugia_", "observaciones_", "codigoServicio_", "codigoServicio_", "codigoAyudante_", "esNivelServicioContratado_", "numeroServicio_", "codigoCirujano_", "codigoCups_", "descripcionServicio_", "descripcionEspecialidad_", "esPos_", "descripcionServicio_", "codigo_", "codigoEspecialidad_", "valor_unitario_","estabd_"};
	String[] indices={"codigoServicio_","descripcionServicio_","codigo_","estabd_"};
	public ActionForward execute(ActionMapping mapping,ActionForm form, HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{
		if(form instanceof ServiciosViaAccesoForm)
		{
			
			 
			
			ServiciosViaAccesoForm forma=(ServiciosViaAccesoForm) form;
			String estado=forma.getEstado();
			logger.info("\n\nEstado-->> "+estado);
			con=UtilidadBD.abrirConexion();
			UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
			ServiciosViaAcceso mundo=new ServiciosViaAcceso();
			forma.setMensaje(new ResultadoBoolean(false));
			
			
			if(estado==null)
			{
				logger.warn("Estado no valido dentro del flujo de Servicios Via Acceso (null)");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
			else if(estado.equals("continuar"))
			{
				UtilidadBD.closeConnection(con);
				return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getServiciosViaAccesoMap().get("numRegistros").toString()), response, request, "serviciosViaAcceso.jsp",true);
				
			}
			else if (estado.equals("redireccion"))
			{
				/**UtilidadBD.closeConnection(con);
				if(UtilidadTexto.isEmpty(forma.getLinkSiguiente()))
					forma.setLinkSiguiente("../serviciosViaAcceso/serviciosViaAcceso.do?estado=continuar");
				response.sendRedirect(forma.getLinkSiguiente());*/
				UtilidadBD.closeConnection(con);
				response.sendRedirect(forma.getLinkSiguiente());
				return null;
			}
			else if(estado.equals("empezar"))
			{
				return this.accionEmpezar(con,forma,mundo,usuario,mapping);
			}
			else if(estado.equals("guardar"))
			{
				this.accionGuardar(con,forma,mundo,usuario,mapping);
				return mapping.findForward("principal");
			}
			else if(estado.equals("nuevo"))
			{
				this.accionNuevo(forma);
				UtilidadBD.closeConnection(con);
				return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), forma.getMaxPageItems(), Utilidades.convertirAEntero(forma.getServiciosViaAccesoMap().get("numRegistros")+""), response, request, "serviciosViaAcceso.jsp", true);
			}
			else if(estado.equals("eliminar"))
			{
				UtilidadBD.closeConnection(con);				
				Utilidades.eliminarRegistroMapaGenerico(forma.getServiciosViaAccesoMap(), forma.getServiciosViaAccesoEliminadosMap(), forma.getIndiceEliminado(), indices, "numRegistros", "estabd_", ConstantesBD.acronimoSi, false);
				forma.setCodigosServiciosInsertados("");
				return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), forma.getMaxPageItems(), Utilidades.convertirAEntero(forma.getServiciosViaAccesoMap().get("numRegistros")+""), response, request, "serviciosViaAcceso.jsp", forma.getIndiceEliminado()==(Utilidades.convertirAEntero(forma.getServiciosViaAccesoMap().get("numRegistros")+"")));
			}
			else
			{
				forma.reset();
				logger.warn("Estado no valido dentro del flujo de Tipos de Moneda");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
		}
		else
		{
			logger.error("El form no es compatible con el form de Servicios de via de acceso");
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
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con,ServiciosViaAccesoForm forma,ServiciosViaAcceso mundo,UsuarioBasico usuario,ActionMapping mapping)
	{
		forma.reset();
		forma.setServiciosViaAccesoMap(mundo.consultarServiciosViaAcceso(con));
		forma.setMaxPageItems(Utilidades.convertirAEntero(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 */
	private void accionGuardar(Connection con,ServiciosViaAccesoForm forma,ServiciosViaAcceso mundo,UsuarioBasico usuario,ActionMapping mapping)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		//eliminar
		for(int i=0;i<Utilidades.convertirAEntero(forma.getServiciosViaAccesoEliminadosMap().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarServiciosViaAcceso(con, Utilidades.convertirAEntero(forma.getServiciosViaAccesoEliminadosMap().get("codigoServicio_"+i)+"")))
			{
				Utilidades.generarLogGenerico(forma.getServiciosViaAccesoEliminadosMap(), forma.getServiciosViaAccesoMap(), usuario.getLoginUsuario(), true, i, ConstantesBD.logServiciosViaAccesoCodigo, indices);
				transaccion=true;
			}
		}
		//guardar
		for(int i=0;i<Utilidades.convertirAEntero(forma.getServiciosViaAccesoMap("numRegistros")+"");i++)
		{
			if(forma.getServiciosViaAccesoMap("estabd_"+i).equals(ConstantesBD.acronimoNo))
			{
				forma.setCodigoServicio(forma.getServiciosViaAccesoMap("codigoServicio_"+i)+"");
				HashMap vo=new HashMap();
				vo.put("codigoservicio", forma.getServiciosViaAccesoMap("codigoServicio_"+i));
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.insertarServiciosViaAcceso(con, vo);
			}
		}
		if(transaccion)
		{
			forma.setMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!"));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMensaje(new ResultadoBoolean(true,"NO SE PUDO INSERTAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
		
		accionEmpezar(con, forma, mundo, usuario, mapping);
	}
	
	/**
	 * Generacion de un nuevo registro
	 * @param forma
	 */
	private void accionNuevo(ServiciosViaAccesoForm forma)
	{
		int pos=Utilidades.convertirAEntero(forma.getServiciosViaAccesoMap().get("numRegistros")+"");
		forma.setServiciosViaAccesoMap("codigservicio_"+pos,"");
		forma.setServiciosViaAccesoMap("estabd_"+pos,ConstantesBD.acronimoNo);
		forma.setServiciosViaAccesoMap("numRegistros",(pos+1)+"");
	}

}
