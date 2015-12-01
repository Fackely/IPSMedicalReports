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
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.facturacion.ServiciosGruposEsteticosForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ServiciosGruposEsteticos;


/**
 * Clase para el manejo del workflow de la funcionalidad
 * @author axioma
 *
 */

public class ServiciosGruposEsteticosAction extends Action 
{
	
	/**
	 * objeto para manejar los logs de esta clase
	 */
	
	Logger logger =Logger.getLogger(ServiciosGruposEsteticosAction.class);
	
	
	/**
	 *  Matriz que contiene los indices del mapa.
	 */
	
	String[] indices={"codigo_","institucion_","descripcion_","activo_","tiporegistro_","codigoantiguo_"};
	String[] indicesServicios={"institucion_","codigoServicio_","descripcionServicio_","gruposesteticos_","tiporegistro_"};
	
	
	
	/**
	 * Método excute del Action
	 */
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{
			if (form instanceof ServiciosGruposEsteticosForm) 
			{
				ServiciosGruposEsteticosForm forma=(ServiciosGruposEsteticosForm) form;

				String estado=forma.getEstado();

				logger.info("Estado -->"+estado);

				con=UtilidadBD.abrirConexion();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());

				ServiciosGruposEsteticos mundo=new ServiciosGruposEsteticos();


				forma.setMensaje(new ResultadoBoolean(false));
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
					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())+""));
					this.accionConsultarGruposEsteticos(con, forma, mundo, usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("continuar"))
				{
					logger.info("mapa-->"+forma.getServiciosEsteticosMap());
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getGruposEsteticosMap("numRegistros").toString()), response, request, "serviciosGruposEsteticos.jsp",false);

					/*
				UtilidadBD.closeConnection(con);
				return mapping.findForward("principal");
					 */
				}
				else if(estado.equals("continuarServi"))
				{
					logger.info("mapa-->"+forma.getServiciosEsteticosMap());
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getGruposEsteticosMap("numRegistros").toString()), response, request, "serviciosEsteticos.jsp",false);

					/*
				UtilidadBD.closeConnection(con);
				return mapping.findForward("principal");
					 */
				}
				else if (estado.equals("redireccion"))
				{
					logger.info("mapa-->"+forma.getServiciosEsteticosMap());
					UtilidadBD.closeConnection(con);
					if(UtilidadTexto.isEmpty(forma.getLinkSiguiente()))
						forma.setLinkSiguiente("../serviciosGruposEsteticos/serviciosGruposEsteticos.do?estado=continuar");
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else if (estado.equals("redireccionServi"))
				{
					logger.info("mapa-->"+forma.getServiciosEsteticosMap());
					UtilidadBD.closeConnection(con);
					if(UtilidadTexto.isEmpty(forma.getLinkSiguiente()))
						forma.setLinkSiguiente("../serviciosGruposEsteticos/serviciosGruposEsteticos.do?estado=continuarServi");
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else if(estado.equals("nuevo"))
				{
					this.accionNuevoGrupo(forma,usuario.getCodigoInstitucionInt());
					UtilidadBD.closeConnection(con);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getGruposEsteticosMap("numRegistros").toString()), response, request, "serviciosGruposEsteticos.jsp",true);
				}
				else if(estado.equals("nuevoServicio"))
				{
					this.accionNuevoServicioEstetico(forma);
					UtilidadBD.closeConnection(con);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getServiciosEsteticosMap("numRegistros").toString()), response, request, "serviciosEsteticos.jsp",true);
				}
				else if(estado.equals("eliminar"))
				{
					UtilidadBD.closeConnection(con);
					return this.accionEliminarServicio(forma,request,response,mapping);
				}
				else if(estado.equals("guardar"))
				{

					//guardamos en BD.
					this.accionGuardarGrupo(con, forma, mundo, usuario);

					//limipiamos el form
					forma.reset();
					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
					this.accionConsultarGruposEsteticos(con,forma,mundo,usuario);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");

				}

				else if(estado.equals("guardarDetalle"))
				{

					//guardamos en BD.
					this.accionGuardarServicios(con,forma,mundo,usuario);

					this.accionConsultarServiciosEsteticos(con,forma,mundo,usuario);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("detalle");

				}
				else if(estado.equals("ordenar"))
				{
					this.accionOrdenarMapaServicio(forma);
					UtilidadBD.closeConnection(con);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getServiciosEsteticosMap("numRegistros").toString()), response, request, "serviciosEsteticos.jsp",true);
				}
				else if(estado.equals("volver"))
				{  
					UtilidadBD.cerrarConexion(con);
					response.sendRedirect("serviciosGruposEsteticos.do?estado=empezar");
					return null;
				}
				else if(estado.equals("eliminarGrupo"))
				{
					UtilidadBD.closeConnection(con);
					return accionEliminarGrupo(forma,request,response,mapping);
				}
				else if(estado.equals("ordenarGrupo"))
				{
					this.accionOrdenarMapaGrupo(forma);
					UtilidadBD.closeConnection(con);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getGruposEsteticosMap("numRegistros").toString()), response, request, "serviciosGruposEsteticos.jsp",true);
				}		
				else if(estado.equals("detalleGrupoEstetico"))
				{

					this.accionConsultarServiciosEsteticos(con, forma, mundo, usuario);
					UtilidadBD.cerrarConexion(con);
					forma.setLinkSiguiente("");
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), forma.getMaxPageItems(), Integer.parseInt(forma.getServiciosEsteticosMap("numRegistros").toString()), response, request, "serviciosEsteticos.jsp", false);
				}
				else
				{
					forma.reset();
					logger.warn("Estado no valido dentro del flujo de SERVICIOS POR GRUPOS ESTETICOS ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de ServiciosGruposEsteticosForm");
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
	
	private void accionGuardarServicios(Connection con, ServiciosGruposEsteticosForm forma, ServiciosGruposEsteticos mundo, UsuarioBasico usuario)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		logger.info("mapaServiciosGruposEsteticos-->"+forma.getServiciosEsteticosMap());
		
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getServiciosEsteticosEliminadosMap("numRegistros")+"");i++)
		{
			if(mundo.eliminarServicio(con,Integer.parseInt(forma.getServiciosEsteticosEliminadosMap("institucion_"+i)+""),Integer.parseInt(forma.getServiciosEsteticosEliminadosMap("codigoServicio_"+i)+"")))
			{
				
				//this.generarLog(forma, mundo, usuario, true, i);
				Utilidades.generarLogGenerico(forma.getServiciosEsteticosEliminadosMap(), new HashMap(), usuario.getLoginUsuario(), true, i, ConstantesBD.logServiciosGruposEsteticosCodigo, indicesServicios);
				transaccion=true;
			}
		}
		for(int i=0;i<Integer.parseInt(forma.getServiciosEsteticosMap("numRegistros")+"");i++)
		{
			/*//modificar
			if((forma.getServiciosEsteticosMap("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma,mundo,Integer.parseInt(forma.getServiciosEsteticosMap("institucion_"+i)+""),i,usuario,forma.getServiciosEsteticosMap("gruposesteticos_"+i)+"",Integer.parseInt(forma.getServiciosEsteticosMap("codigoServicio_"+i)+"")))
			{
				HashMap vo=new HashMap();
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("servicio",forma.getServiciosEsteticosMap("codigoServicio_"+i));
				vo.put("grupos_esteticos",forma.getGruposEsteticosMap("codigo_"+forma.getIndexSeleccionado()));
				transaccion=mundo.modificarServicio(con, vo);
			}*/
			
			//insertar
			if((forma.getServiciosEsteticosMap("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("servicio",forma.getServiciosEsteticosMap("codigoServicio_"+i));
				vo.put("grupos_esteticos",forma.getGruposEsteticosMap("codigo_"+forma.getIndexSeleccionado()));
				transaccion=mundo.insertarServicio(con, vo);
			}
			
		}
		
		if(transaccion)
		{
			forma.setMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!!!"));
			UtilidadBD.finalizarTransaccion(con);
		}
		/*else
		{
			forma.setMensaje(new ResultadoBoolean(true,"NO SE PUDO INGRESAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}*/
	}
	
	
	/**
	 * 
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param isEliminacion
	 * @param pos
	 */
	
	private void generarLog(ServiciosGruposEsteticosForm forma, ServiciosGruposEsteticos mundo, UsuarioBasico usuario, boolean isEliminacion, int pos) 
	{
		String log = "";
		int tipoLog=0;
		
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ "+(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(forma.getServiciosEsteticosEliminadosMap(indices[i]+""+pos)+""):forma.getServiciosEsteticosEliminadosMap(indices[i]+""+pos))+" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(mundo.getServiciosEsteticosMap().get(indices[i]+"0")+""):mundo.getServiciosEsteticosMap().get(indices[i]+"0")) + " ]";
			}
			log += 		 "\n   ============INFORMACION MODIFICADA=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(forma.getServiciosEsteticosMap(indices[i]+""+pos)+""):forma.getServiciosEsteticosMap(indices[i]+""+pos)) +" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		
		LogsAxioma.enviarLog(ConstantesBD.logServiciosGruposEsteticosCodigo,log,tipoLog,usuario.getLoginUsuario());
	}
	
	
	
	



	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param institucion
	 * @param pos
	 * @param usuario
	 * @param codigo
	 * * @return
	 */
	
	/*private boolean existeModificacion(Connection con, ServiciosGruposEsteticosForm forma, ServiciosGruposEsteticos mundo,int institucion,int pos, UsuarioBasico usuario, String grupos_esteticos, int servicio)
	{
		HashMap temp=mundo.consultarServicioEsteticoEspecifico(con, institucion, grupos_esteticos, servicio);
		for(int i=0;i<indicesServicios.length;i++)
		{
			if(temp.containsKey(indicesServicios[i]+"0")&&forma.getServiciosEsteticosMap().containsKey(indicesServicios[i]+""+pos))
			{
				if(!((temp.get(indicesServicios[i]+"0")+"").trim().equals(forma.getServiciosEsteticosMap(indicesServicios[i]+""+pos)+"")))
				{
					mundo.setServiciosEsteticosMap(temp);
					this.generarLog(forma, mundo, usuario, false, pos);
					return true;
				}
			}
		}
		return false;
	}*/
	
	
	
	/**
	 * 
	 * @param forma
	 * @param request
	 * @param response
	 * @param mapping
	 * @return
	 */
	
	private ActionForward accionEliminarServicio(ServiciosGruposEsteticosForm forma, HttpServletRequest request, HttpServletResponse response, ActionMapping mapping)
	{
		int numRegMapEliminados=Integer.parseInt(forma.getServiciosEsteticosEliminadosMap("numRegistros")+"");
		int ultimaPosMapa=(Integer.parseInt(forma.getServiciosEsteticosMap("numRegistros")+"")-1);
		
		//poner la informacion en el otro mapa.
		
//		solo pasar al mapa los registros que son de BD
		if((forma.getServiciosEsteticosMap("tiporegistro_"+forma.getPosEliminar())+"").trim().equalsIgnoreCase("BD"))
		{
			for(int i=0;i<indicesServicios.length;i++)
			{
			
				forma.setServiciosEsteticosEliminadosMap(indicesServicios[i]+""+numRegMapEliminados, forma.getServiciosEsteticosMap(indicesServicios[i]+""+forma.getPosEliminar()));
			}
			forma.setServiciosEsteticosEliminadosMap("numRegistros", (numRegMapEliminados+1));
		}
		
		//acomodar los registros del mapa en su nueva posicion
		for(int i=forma.getPosEliminar();i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indicesServicios.length;j++)
			{
				forma.setServiciosEsteticosMap(indicesServicios[j]+""+i,forma.getServiciosEsteticosMap(indicesServicios[j]+""+(i+1)));
			}
		}
		
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indicesServicios.length;j++)
		{
			forma.getServiciosEsteticosMap().remove(indicesServicios[j]+""+ultimaPosMapa);
		}
		
		//ahora actualizamo el numero de registros en el mapa.
		forma.setServiciosEsteticosMap("numRegistros",ultimaPosMapa);
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getServiciosEsteticosMap("numRegistros").toString()), response, request, "serviciosEsteticos.jsp",forma.getPosEliminar()==ultimaPosMapa);

		
	}
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionConsultarServiciosEsteticos(Connection con, ServiciosGruposEsteticosForm forma, ServiciosGruposEsteticos mundo, UsuarioBasico usuario)
	{
		HashMap vo=new HashMap();
		vo.put("institucion", usuario.getCodigoInstitucion());
		vo.put("codigo", forma.getGruposEsteticosMap("codigo_"+forma.getIndexSeleccionado()));
		mundo.consultarServiciosEsteticosExistentes(con,vo);
		forma.setServiciosEsteticosMap((HashMap)mundo.getServiciosEsteticosMap().clone());
	}
	
	
	/**
	 * 
	 * @param forma
	 */	
	private void accionNuevoServicioEstetico(ServiciosGruposEsteticosForm forma)
	{
		int pos=Integer.parseInt(forma.getServiciosEsteticosMap("numRegistros")+"");
		forma.setServiciosEsteticosMap("institucion_"+pos,"");
		//se asignan desde la busqueda.
		//forma.setServiciosEsteticosMap("codigoServicio_"+pos,"");
		//forma.setServiciosEsteticosMap("descripcionServicio_"+pos,"");
		forma.setServiciosEsteticosMap("gruposesteticos_"+pos,"");
		forma.setServiciosEsteticosMap("tiporegistro_"+pos,"MEM");
		forma.setServiciosEsteticosMap("numRegistros", (pos+1)+"");
	}
	
	
	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenarMapaServicio(ServiciosGruposEsteticosForm forma)
	{
		
		int numReg=Integer.parseInt(forma.getServiciosEsteticosMap("numRegistros")+"");
		forma.setServiciosEsteticosMap(Listado.ordenarMapa(indicesServicios,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getServiciosEsteticosMap(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setServiciosEsteticosMap("numRegistros",numReg+"");
	}
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private ActionForward accionEliminarGrupo(ServiciosGruposEsteticosForm forma, HttpServletRequest request, HttpServletResponse response, ActionMapping mapping)
	{
		int numRegMapEliminados=Integer.parseInt(forma.getGruposEsteticosEliminadosMap("numRegistros")+"");
		int ultimaPosMapa=(Integer.parseInt(forma.getGruposEsteticosMap("numRegistros")+"")-1);
		
		//poner la informacion en el otro mapa.
		
		//solo pasar al mapa los registros que son de BD
		if((forma.getGruposEsteticosMap("tiporegistro_"+forma.getPosEliminar())+"").trim().equalsIgnoreCase("BD"))
		{
			for(int i=0;i<indices.length;i++)
			{
			
				forma.setGruposEsteticosEliminadosMap(indices[i]+""+numRegMapEliminados, forma.getGruposEsteticosMap(indices[i]+""+forma.getPosEliminar()));
			}
			forma.setGruposEsteticosEliminadosMap("numRegistros", (numRegMapEliminados+1));
		}
		
		//acomodar los registros del mapa en su nueva posicion
		for(int i=forma.getPosEliminar();i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indices.length;j++)
			{
				forma.setGruposEsteticosMap(indices[j]+""+i,forma.getGruposEsteticosMap(indices[j]+""+(i+1)));
			}
		}
		
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indices.length;j++)
		{
			forma.getGruposEsteticosMap().remove(indices[j]+""+ultimaPosMapa);
		}
		
		//ahora actualizamo el numero de registros en el mapa.
		forma.setGruposEsteticosMap("numRegistros",ultimaPosMapa);
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getGruposEsteticosMap("numRegistros").toString()), response, request, "serviciosGruposEsteticos.jsp",forma.getPosEliminar()==ultimaPosMapa);

		
	}
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardarGrupo(Connection con, ServiciosGruposEsteticosForm forma, ServiciosGruposEsteticos mundo, UsuarioBasico usuario)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		logger.info("mapaServiciosGruposEsteticos-->"+forma.getGruposEsteticosMap());
		
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getGruposEsteticosEliminadosMap("numRegistros")+"");i++)
		{
			if(mundo.eliminarRegistro(con,Integer.parseInt(forma.getGruposEsteticosEliminadosMap("institucion_"+i)+""), forma.getGruposEsteticosEliminadosMap("codigo_"+i)+""))
			{
				Utilidades.generarLogGenerico(forma.getGruposEsteticosEliminadosMap(), new HashMap(), usuario.getLoginUsuario(), true, i, ConstantesBD.logServiciosGruposEsteticosCodigo, indices);
				//this.generarLog(forma, mundo, usuario, true, i);
				transaccion=true;
			}
		}
		
		for(int i=0;i<Integer.parseInt(forma.getGruposEsteticosMap("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getGruposEsteticosMap("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacionGrupo(con,forma,mundo,Integer.parseInt(forma.getGruposEsteticosMap("institucion_"+i)+""),i,usuario,forma.getGruposEsteticosMap("codigoantiguo_"+i)+""))
			{
				HashMap vo=new HashMap();
				vo.put("codigo",forma.getGruposEsteticosMap("codigo_"+i));
				vo.put("descripcion",forma.getGruposEsteticosMap("descripcion_"+i));
				vo.put("activo",forma.getGruposEsteticosMap("activo_"+i));
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("usuario_modifica",usuario.getLoginUsuario());
				vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				vo.put("hora_modifica",UtilidadFecha.getHoraActual());
				vo.put("codigoOld",forma.getGruposEsteticosMap("codigoantiguo_"+i));
				transaccion=mundo.modificar(con, vo);
			}
			
			//insertar
			else if((forma.getGruposEsteticosMap("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigo",forma.getGruposEsteticosMap("codigo_"+i));
				vo.put("descripcion",forma.getGruposEsteticosMap("descripcion_"+i));
				vo.put("activo",forma.getGruposEsteticosMap("activo_"+i));
				vo.put("institucion", usuario.getCodigoInstitucion());
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
	 * @param forma
	 */
	private void accionOrdenarMapaGrupo(ServiciosGruposEsteticosForm forma)
	{
		
		int numReg=Integer.parseInt(forma.getGruposEsteticosMap("numRegistros")+"");
		forma.setGruposEsteticosMap(Listado.ordenarMapa(indices,forma.getPatronOrdenarGrupos(),forma.getUltimoPatronGrupos(),forma.getGruposEsteticosMap(),numReg));
		forma.setUltimoPatronGrupos(forma.getPatronOrdenarGrupos());
		forma.setGruposEsteticosMap("numRegistros",numReg+"");
	}
	
	
	/**
	 * 
	 * @param forma
	 * @param institucion
	 */
	private void accionNuevoGrupo(ServiciosGruposEsteticosForm forma, int institucion)
	{
		int pos=Integer.parseInt(forma.getGruposEsteticosMap("numRegistros")+"");
		forma.setGruposEsteticosMap("codigo_"+pos,"");
		forma.setGruposEsteticosMap("institucion_"+pos,institucion+"");
		forma.setGruposEsteticosMap("descripcion_"+pos,"");
		forma.setGruposEsteticosMap("activo_"+pos,ConstantesBD.acronimoSi);
		forma.setGruposEsteticosMap("tiporegistro_"+pos,"MEM");
		forma.setGruposEsteticosMap("numRegistros", (pos+1)+"");
	}
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionConsultarGruposEsteticos(Connection con, ServiciosGruposEsteticosForm forma, ServiciosGruposEsteticos mundo, UsuarioBasico usuario)
	{
		mundo.consultarGruposEsteticosExistentes(con,usuario.getCodigoInstitucionInt());
		forma.setGruposEsteticosMap((HashMap)mundo.getGruposEsteticosMap().clone());
	}
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param institucion
	 * @param pos
	 * @param usuario
	 * @param codigo
	 * @return
	 */
	private boolean existeModificacionGrupo(Connection con, ServiciosGruposEsteticosForm forma, ServiciosGruposEsteticos mundo,int institucion,int pos, UsuarioBasico usuario, String codigo)
	{
		HashMap temp=mundo.consultarGrupoEsteticoEspecifico(con, institucion, codigo);
		for(int i=0;i<indices.length;i++)
		{
			if(temp.containsKey(indices[i]+"0")&&forma.getGruposEsteticosMap().containsKey(indices[i]+""+pos))
			{
				if(!((temp.get(indices[i]+"0")+"").trim().equals(forma.getGruposEsteticosMap(indices[i]+""+pos)+"")))
				{
					mundo.setGruposEsteticosMap(temp);
					Utilidades.generarLogGenerico(forma.getGruposEsteticosMap(), temp, usuario.getLoginUsuario(), false, pos, ConstantesBD.logServiciosGruposEsteticosCodigo, indices);
					//this.generarLog(forma, mundo, usuario, false, pos);
					return true;
				}
			}
		}
		return false;
	}
		

	

}
