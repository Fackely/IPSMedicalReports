package com.princetonsa.action.inventarios;

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
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.actionform.inventarios.GrupoEspecialArticulosForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.GrupoEspecialArticulos;

public class GrupoEspecialArticulosAction extends Action 
{

	
	
	/**
	 * objeto para manejar los logs de esta clase
	 */
	Logger logger =Logger.getLogger(GrupoEspecialArticulosAction.class);
	
	
	/**
	 * 
	 */
	private static String[] indicesGrupos={"codigopk_","codigo_","descripcion_","tiporegistro_"};
	
	
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{

		Connection con=null;
		try{

			if (form instanceof GrupoEspecialArticulosForm) 
			{
				GrupoEspecialArticulosForm forma=(GrupoEspecialArticulosForm) form;

				String estado=forma.getEstado();

				logger.info("Estado -->"+estado);

				con=UtilidadBD.abrirConexion();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());

				GrupoEspecialArticulos mundo=new GrupoEspecialArticulos();

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de GrupoEspecialArticulosAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					forma.reset();
					forma.resetMensaje();
					this.accionConsultarGrupos(con, forma, mundo);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("nuevo"))
				{
					forma.resetMensaje();
					Utilidades.nuevoRegistroMapaGenerico(forma.getMapaGrupoArticulos(),indicesGrupos,"numRegistros","tiporegistro_","MEM");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("guardar"))
				{
					//guardamos en BD.
					this.accionGuardarRegistros(con,forma,mundo,usuario);

					//limipiamos el form
					forma.reset();
					this.accionConsultarGrupos(con, forma, mundo);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("eliminarMotivo"))
				{
					//eliminar especialidad
					forma.resetMensaje();
					Utilidades.eliminarRegistroMapaGenerico(forma.getMapaGrupoArticulos(),forma.getMapaGruposEliminados(),forma.getIndiceGrupoEliminar(),indicesGrupos,"numRegistros","tiporegistro_","BD",false);
					forma.setIndiceGrupoEliminar(ConstantesBD.codigoNuncaValido);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("ordernar"))
				{
					forma.resetMensaje();
					this.accionOrdenar(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				//ESTADO UTILIZADO PARA EL PAGER
				else if (estado.equals("redireccion")) 
				{			    
					forma.resetMensaje();
					UtilidadBD.cerrarConexion(con);
					forma.getLinkSiguiente();
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else
				{
					forma.reset();
					logger.warn("Estado no valido dentro del flujo de GRUPO ESPECIAL ARTICULOS ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}		
			}
			else
			{
				logger.error("El form no es compatible con el form de GrupoEspecialArticulosForm");
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
	 * @param forma
	 */
	private void accionOrdenar(GrupoEspecialArticulosForm forma) 
	{
		int numReg=Utilidades.convertirAEntero(forma.getMapaGrupoArticulos().get("numRegistros")+"");
		forma.setMapaGrupoArticulos(Listado.ordenarMapa(indicesGrupos,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaGrupoArticulos(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setMapaGrupoArticulos("numRegistros",numReg+"");
	}
	
	
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardarRegistros(Connection con, GrupoEspecialArticulosForm forma, GrupoEspecialArticulos mundo, UsuarioBasico usuario)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		logger.info("mapaPaquertes-->"+forma.getMapaGrupoArticulos());
		
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getMapaGruposEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarRegistro(con, Utilidades.convertirAEntero(forma.getMapaGruposEliminados().get("codigopk_"+i)+"")))
			{
				transaccion=true;
			}
		}
		
		for(int i=0;i<Integer.parseInt(forma.getMapaGrupoArticulos().get("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getMapaGrupoArticulos().get("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma,mundo,Integer.parseInt(forma.getMapaGrupoArticulos().get("codigopk_"+i)+""),i,usuario))
			{
				HashMap vo=new HashMap();
				vo.put("codigo",forma.getMapaGrupoArticulos().get("codigo_"+i));
				vo.put("codigopk",forma.getMapaGrupoArticulos().get("codigopk_"+i));
				vo.put("descripcion",forma.getMapaGrupoArticulos().get("descripcion_"+i));
				vo.put("usuario_modifica",usuario.getLoginUsuario());
				vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				vo.put("hora_modifica",UtilidadFecha.getHoraActual());
				
				transaccion=mundo.modificar(con, vo);
			}
			//insertar
			else if((forma.getMapaGrupoArticulos().get("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigo",forma.getMapaGrupoArticulos().get("codigo_"+i));
				vo.put("codigopk",forma.getMapaGrupoArticulos().get("codigopk_"+i));
				vo.put("descripcion",forma.getMapaGrupoArticulos().get("descripcion_"+i));
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
	private boolean existeModificacion(Connection con, GrupoEspecialArticulosForm forma, GrupoEspecialArticulos mundo,int codigoGrupo,int pos, UsuarioBasico usuario)
	{
		HashMap temp=mundo.consultarGrupoEspecifico(con, codigoGrupo);
		for(int i=0;i<indicesGrupos.length;i++)
		{
			if(temp.containsKey(indicesGrupos[i]+"0")&&forma.getMapaGrupoArticulos().containsKey(indicesGrupos[i]+""+pos))
			{
				if(!((temp.get(indicesGrupos[i]+"0")+"").trim().equals(forma.getMapaGrupoArticulos().get(indicesGrupos[i]+""+pos)+"")))
				{
					mundo.setMapaGrupo(temp);
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
	private void accionConsultarGrupos(Connection con, GrupoEspecialArticulosForm forma, GrupoEspecialArticulos mundo)
	{
		forma.setMapaGrupoArticulos(mundo.consultarGruposExistentes(con));
	}
	
	
	
}
