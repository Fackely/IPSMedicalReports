/*
 * Creado May 7, 2007
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * ComponentesPaquetesAction
 * com.princetonsa.action.facturacion
 * java version "1.5.0_07"
 */
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
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.facturacion.ComponentesPaquetesForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ComponentesPaquetes;

/**
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 7, 2007
 */
public class ComponentesPaquetesAction extends Action
{
	
	/**
	 * 
	 */
	private Logger logger=Logger.getLogger(ComponentesPaquetesAction.class);
	
	/**
	 * 
	 */
	public String[] indicesAgrupacionArticulos={"codigo_","paquete_","clase_","nomclase_","grupo_","nomgrupo_","subgrupo_","nomsubgrupo_","naturaleza_","nomnaturaleza_","nomgrupoespecial_","grupoespecial_", "cantidad_","tiporegistro_"};

	/**
	 * 
	 */
	public String[] indicesArticulos={"codigo_","paquete_","codigoArticulo_","descripcionArticulo_","cantidad_","tiporegistro_"};

	/**
	 * 
	 */
	public String[] indicesAgrupacionServicio={"codigo_","paquete_","gruposervicio_","acronimogruposervicio_","descgruposervicio_","tiposervicio_","nomtiposervicio_","especialidad_","nomespecialidad_","cantidad_","tiporegistro_"};
	
	/**
	 * 
	 */
	public String[] indicesServicios={"codigo_","paquete_","codigoServicio_","descripcionServicio_","principal_","cantidad_","tiporegistro_"};
	
	/**
	 * Metodo que lleva el flujo de la fuincionalidad
	 */
	public ActionForward execute(	ActionMapping mapping, 	ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		Connection con=null;
		try{
			if (form instanceof ComponentesPaquetesForm)
			{
				ComponentesPaquetesForm forma=(ComponentesPaquetesForm)form;
				con=UtilidadBD.abrirConexion();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
				ComponentesPaquetes mundo=new ComponentesPaquetes();
				String estado=forma.getEstado();

				logger.info("ESTADO -->"+estado);

				forma.setMaxPageItems(ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()));

				forma.setMostrarMensaje(new ResultadoBoolean(false));

				if(estado.equals("empezar"))
				{
					forma.reset();
					return this.accionEmpezar(con,forma,mundo,usuario,mapping);
				}
				else if(estado.equals("detallePaquete"))
				{
					forma.setPaqueteSeleccionado(forma.getPaquetes().get(forma.getIndexSeleccionado()));
					return this.accionDetallePaquete(con,forma,mundo,usuario,mapping);
				}
				else if(estado.equals("nuevaAgrupacion"))
				{
					Utilidades.nuevoRegistroMapaGenerico(forma.getAgrupacionArticulos(),indicesAgrupacionArticulos,"numRegistros","tiporegistro_","MEM");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detallePaquete");

				}
				else if(estado.equals("eliminarAgrupacion"))
				{
					Utilidades.eliminarRegistroMapaGenerico(forma.getAgrupacionArticulos(),forma.getAgrupacionArticulosEliminados(),forma.getPosEliminar(),indicesAgrupacionArticulos,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detallePaquete");	
				}
				else if(estado.equals("nuevoArticulo"))
				{
					String[] indicesTemp={"codigo_","paquete_","cantidad_","tiporegistro_"};
					Utilidades.nuevoRegistroMapaGenerico(forma.getArticulos(),indicesTemp,"numRegistros","tiporegistro_","MEM");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detallePaquete");
				}
				else if(estado.equals("eliminarArticulo"))
				{
					Utilidades.eliminarRegistroMapaGenerico(forma.getArticulos(),forma.getArticulosEliminados(),forma.getPosEliminar(),indicesArticulos,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detallePaquete");	
				}
				else if(estado.equals("nuevaAgrupacionServicio"))
				{
					Utilidades.nuevoRegistroMapaGenerico(forma.getAgrupacionServicios(),indicesAgrupacionServicio,"numRegistros","tiporegistro_","MEM");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detallePaquete");

				}
				else if(estado.equals("eliminarAgrupacionServicio"))
				{
					Utilidades.eliminarRegistroMapaGenerico(forma.getAgrupacionServicios(),forma.getAgrupacionServiciosEliminados(),forma.getPosEliminar(),indicesAgrupacionServicio,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detallePaquete");	
				}
				else if(estado.equals("nuevoServicio"))
				{
					String[] indicesTemp={"codigo_","paquete_","principal_","cantidad_","tiporegistro_"};
					Utilidades.nuevoRegistroMapaGenerico(forma.getServicios(),indicesTemp,"numRegistros","tiporegistro_","MEM");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detallePaquete");
				}
				else if(estado.equals("eliminarServicio"))
				{
					Utilidades.eliminarRegistroMapaGenerico(forma.getServicios(),forma.getServiciosEliminados(),forma.getPosEliminar(),indicesServicios,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detallePaquete");	
				}

				else if(estado.equals("guardar"))
				{
					this.accionGuardar(con,forma,mundo,usuario);
					//consultar lo que se guardo.
					return this.accionDetallePaquete(con,forma,mundo,usuario,mapping);
				}
				else if(estado.equals("recargarPagina"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detallePaquete");	
				}
				else if(estado.equals("ordenarArticulos"))
				{
					this.accionOrdenarMapa(forma,true);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detallePaquete");
				}
				else if(estado.equals("ordenarServicios"))
				{
					this.accionOrdenarMapa(forma,false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detallePaquete");
				}
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("Error con la forma (Revisar StrutsConfig)");
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
	private void accionOrdenarMapa(ComponentesPaquetesForm forma,boolean articulos) 
	{
		if(articulos)
		{
			int numReg=Integer.parseInt(forma.getArticulos("numRegistros")+"");
			forma.setArticulos(Listado.ordenarMapa(indicesArticulos,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getArticulos(),numReg));
			forma.setUltimoPatron(forma.getPatronOrdenar());
			forma.setArticulos("numRegistros",numReg+"");
		}
		else
		{
			int numReg=Integer.parseInt(forma.getServicios("numRegistros")+"");
			forma.setServicios(Listado.ordenarMapa(indicesServicios,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getServicios(),numReg));
			forma.setUltimoPatron(forma.getPatronOrdenar());
			forma.setServicios("numRegistros",numReg+"");
		}
			
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardar(Connection con, ComponentesPaquetesForm forma, ComponentesPaquetes mundo, UsuarioBasico usuario)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		if(transaccion)
			transaccion=this.accionGuardarAgrupacionArticulos(con,forma,mundo,usuario,transaccion);
		if(transaccion)
			transaccion=this.accionGuardarArticulos(con,forma,mundo,usuario,transaccion);
		if(transaccion)
			transaccion=this.accionGuardarAgrupacionServicios(con,forma,mundo,usuario,transaccion);
		if(transaccion)
			transaccion=this.accionGuardarServicios(con,forma,mundo,usuario,transaccion);
		if(transaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
			forma.setMostrarMensaje(new ResultadoBoolean(true,"Proceso Exitoso."));
			
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			forma.setMostrarMensaje(new ResultadoBoolean(true,"Proceso No Exitoso."));
		}	
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param transaccion
	 * @return
	 */
	private boolean accionGuardarServicios(Connection con, ComponentesPaquetesForm forma, ComponentesPaquetes mundo, UsuarioBasico usuario, boolean transaccion)
	{
//		eliminar
		for(int i=0;i<Integer.parseInt(forma.getServiciosEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarServicios(con,forma.getServiciosEliminados().get("codigo_"+i)+""))
			{
				Utilidades.generarLogGenerico(forma.getServiciosEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logComponentesPaquetesCodigo,indicesServicios);
				transaccion=true;
			}
		}
		for(int i=0;i<Integer.parseInt(forma.getServicios("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getServicios("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma.getServicios(),mundo.consultarServiciosPaqueteLLave(con, forma.getServicios("codigo_"+i)+""),i,usuario,indicesServicios))
			{
				HashMap vo=new HashMap();
				vo.put("codigo", forma.getServicios("codigo_"+i)+"");
				vo.put("servicio", forma.getServicios("codigoServicio_"+i));
				vo.put("principal", forma.getServicios("principal_"+i));
				vo.put("cantidad", forma.getServicios("cantidad_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.modificarServicios(con, vo);
			}
			//insertar
			else if((forma.getServicios("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("paquete", forma.getPaqueteSeleccionado().getCodigo()+"");
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("servicio", forma.getServicios("codigoServicio_"+i));
				vo.put("principal", forma.getServicios("principal_"+i));
				vo.put("cantidad", forma.getServicios("cantidad_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.insertarServicios(con, vo);
			}
			
		}
		return transaccion;
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param transaccion
	 * @return
	 */
	private boolean accionGuardarAgrupacionServicios(Connection con, ComponentesPaquetesForm forma, ComponentesPaquetes mundo, UsuarioBasico usuario, boolean transaccion)
	{

		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getAgrupacionServiciosEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarAgrupacionServicios(con,forma.getAgrupacionServiciosEliminados().get("codigo_"+i)+""))
			{
				Utilidades.generarLogGenerico(forma.getAgrupacionServiciosEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logComponentesPaquetesCodigo,indicesAgrupacionServicio);
				transaccion=true;
			}
		}
		for(int i=0;i<Integer.parseInt(forma.getAgrupacionServicios("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getAgrupacionServicios("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma.getAgrupacionServicios(),mundo.consultarAgrupacionServiciosPaqueteLLave(con, forma.getAgrupacionServicios("codigo_"+i)+""),i,usuario,indicesAgrupacionServicio))
			{
				HashMap vo=new HashMap();
				vo.put("codigo", forma.getAgrupacionServicios("codigo_"+i)+"");
				vo.put("gruposervicio", forma.getAgrupacionServicios("gruposervicio_"+i));
				vo.put("tiposervicio", forma.getAgrupacionServicios("tiposervicio_"+i));
				vo.put("especialidad", forma.getAgrupacionServicios("especialidad_"+i));
				vo.put("cantidad", forma.getAgrupacionServicios("cantidad_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.modificarAgrupacionServicios(con, vo);
			}
			//insertar
			else if((forma.getAgrupacionServicios("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("paquete", forma.getPaqueteSeleccionado().getCodigo()+"");
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("gruposervicio", forma.getAgrupacionServicios("gruposervicio_"+i));
				vo.put("tiposervicio", forma.getAgrupacionServicios("tiposervicio_"+i));
				vo.put("especialidad", forma.getAgrupacionServicios("especialidad_"+i));
				vo.put("cantidad", forma.getAgrupacionServicios("cantidad_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.insertarAgrupacionServicios(con, vo);
			}
		}
		return transaccion;
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param transaccion
	 * @return
	 */
	private boolean accionGuardarArticulos(Connection con, ComponentesPaquetesForm forma, ComponentesPaquetes mundo, UsuarioBasico usuario, boolean transaccion)
	{
		
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getArticulosEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarArticulos(con,forma.getArticulosEliminados().get("codigo_"+i)+""))
			{
				Utilidades.generarLogGenerico(forma.getArticulosEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logComponentesPaquetesCodigo,indicesArticulos);
				transaccion=true;
			}
		}
		
		for(int i=0;i<Integer.parseInt(forma.getArticulos("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getArticulos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma.getArticulos(),mundo.consultarArticulosPaqueteLLave(con, forma.getArticulos("codigo_"+i)+""),i,usuario,indicesArticulos))
			{
				HashMap vo=new HashMap();
				vo.put("codigo", forma.getArticulos("codigo_"+i)+"");
				vo.put("articulo", forma.getArticulos("codigoArticulo_"+i));
				vo.put("cantidad", forma.getArticulos("cantidad_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.modificarArticulos(con, vo);
			}
			//insertar
			else if((forma.getArticulos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("paquete", forma.getPaqueteSeleccionado().getCodigo()+"");
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("articulo", forma.getArticulos("codigoArticulo_"+i));
				vo.put("cantidad", forma.getArticulos("cantidad_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.insertarArticulos(con, vo);
			}
			
		}
		return transaccion;
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private boolean accionGuardarAgrupacionArticulos(Connection con, ComponentesPaquetesForm forma, ComponentesPaquetes mundo, UsuarioBasico usuario,boolean transaccion)
	{
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getAgrupacionArticulosEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarAgrupacionArticulos(con,forma.getAgrupacionArticulosEliminados().get("codigo_"+i)+""))
			{
				Utilidades.generarLogGenerico(forma.getAgrupacionArticulosEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logComponentesPaquetesCodigo,indicesAgrupacionArticulos);
				transaccion=true;
			}
		}
		
		for(int i=0;i<Integer.parseInt(forma.getAgrupacionArticulos("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getAgrupacionArticulos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma.getAgrupacionArticulos(),mundo.consultarAgrupacionArticulosPaqueteLLave(con, forma.getAgrupacionArticulos("codigo_"+i)+""),i,usuario,indicesAgrupacionArticulos))
			{
				HashMap vo=new HashMap();
				vo.put("codigo", forma.getAgrupacionArticulos("codigo_"+i)+"");
				vo.put("clase", forma.getAgrupacionArticulos("clase_"+i));
				vo.put("grupo", forma.getAgrupacionArticulos("grupo_"+i));
				vo.put("subgrupo", forma.getAgrupacionArticulos("subgrupo_"+i));
				vo.put("naturaleza", forma.getAgrupacionArticulos("naturaleza_"+i));
				vo.put("grupoespecial", forma.getAgrupacionArticulos("grupoespecial_"+i));
				vo.put("cantidad", forma.getAgrupacionArticulos("cantidad_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.modificarAgrupacionArticulos(con, vo);
			}
			//insertar
			else if((forma.getAgrupacionArticulos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("paquete", forma.getPaqueteSeleccionado().getCodigo()+"");
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("clase", forma.getAgrupacionArticulos("clase_"+i));
				vo.put("grupo", forma.getAgrupacionArticulos("grupo_"+i));
				vo.put("subgrupo", forma.getAgrupacionArticulos("subgrupo_"+i));
				vo.put("naturaleza", forma.getAgrupacionArticulos("naturaleza_"+i));
				vo.put("grupoespecial", forma.getAgrupacionArticulos("grupoespecial_"+i));
				vo.put("cantidad", forma.getAgrupacionArticulos("cantidad_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.insertarAgrupacionArticulos(con, vo);
			}
		}
		return transaccion;
	}


	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @param mapaTemp
	 * @param pos
	 * @param usuario
	 * @param indices 
	 * @return
	 */
	private boolean existeModificacion(Connection con, HashMap mapa, HashMap mapaTemp, int pos, UsuarioBasico usuario, String[] indices)
	{
		for(int i=0;i<indices.length;i++)
		{
			if(mapaTemp.containsKey(indices[i]+"0")&&mapa.containsKey(indices[i]+""+pos))
			{
				if(!((mapaTemp.get(indices[i]+"0")+"").trim().equals((mapa.get(indices[i]+""+pos)+"").trim())))
				{
					Utilidades.generarLogGenerico(mapa, mapaTemp, usuario.getLoginUsuario(), false, pos,ConstantesBD.logComponentesPaquetesCodigo,indices);
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
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionDetallePaquete(Connection con, ComponentesPaquetesForm forma, ComponentesPaquetes mundo, UsuarioBasico usuario, ActionMapping mapping)
	{
		forma.resetMapasEliminacion();
		forma.setAgrupacionArticulos(mundo.consultarAgrupacionArticulosPaquete(con,forma.getPaqueteSeleccionado().getCodigo(),usuario.getCodigoInstitucionInt()));
		forma.setArticulos(mundo.consultarArticulosPaquete(con,forma.getPaqueteSeleccionado().getCodigo(),usuario.getCodigoInstitucionInt()));
		forma.setAgrupacionServicios(mundo.consultarAgrupacionServiciosPaquete(con,forma.getPaqueteSeleccionado().getCodigo(),usuario.getCodigoInstitucionInt()));
		forma.setServicios(mundo.consultarServiciosPaquete(con,forma.getPaqueteSeleccionado().getCodigo(),usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detallePaquete");
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
	private ActionForward accionEmpezar(Connection con, ComponentesPaquetesForm forma, ComponentesPaquetes mundo, UsuarioBasico usuario, ActionMapping mapping)
	{
		forma.setPaquetes(mundo.obtenerListadoPaquetes(con,usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}

}
