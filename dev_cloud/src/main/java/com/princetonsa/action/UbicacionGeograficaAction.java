package com.princetonsa.action;

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
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.UbicacionGeograficaForm;
import com.princetonsa.mundo.UbicacionGeografica;
import com.princetonsa.mundo.UsuarioBasico;

public class UbicacionGeograficaAction extends Action {
	
	Logger logger=Logger.getLogger(UbicacionGeograficaAction.class);
	
	String[] indicesPais={"tiporegistro_","codigo_pais_","descripcion_"};
	String[] indicesDepartamento={"tiporegistro_","codigo_departamento_","descripcion_","codigo_pais_"};
	String[] indicesCiudad={"tiporegistro_","codigo_departamento_","codigo_ciudad_","descripcion_","codigo_pais_","localidad_"};
	String[] indicesLocalidad={"tiporegistro_","codigo_localidad_","descripcion_","codigo_pais_","codigo_departamento_","codigo_ciudad_"};
	String[] indicesBarrio={"tiporegistro_","codigo_departamento_","codigo_ciudad_","codigo_barrio_","descripcion_","codigo_pais_","codigo_localidad_","codigo_"};
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con =null;
		try{
		if (form instanceof UbicacionGeograficaForm) 
		{
			UbicacionGeograficaForm forma=(UbicacionGeograficaForm) form;
			String estado=forma.getEstado();
			logger.info("Estado-->> "+estado);
			con = UtilidadBD.abrirConexion();
			UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
			UbicacionGeografica mundo=new UbicacionGeografica();
			forma.setMensaje(new ResultadoBoolean(false));
			if (estado==null)
			{
				logger.warn("Estado no valido dentro del flujo de UbicacionGeograficaAction (null)");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
			else if(estado.equals("empezar"))
			{
				forma.reset();
				forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())+""));
				this.accionConsultarPais(con, forma, mundo);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("principal");
			}
			else if(estado.equals("continuar"))
			{
				logger.info("mapa-->"+forma.getPaisMap());
				UtilidadBD.closeConnection(con);
				return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getPaisMap("numRegistros").toString()), response, request, "ubicacionGeografica.jsp",true);
			}
			else if (estado.equals("redireccion"))
			{
				UtilidadBD.closeConnection(con);
				if(UtilidadTexto.isEmpty(forma.getLinkSiguiente()))
					forma.setLinkSiguiente("../ubicacionGeografica/ubicacionGeografica.do?estado=continuar");
				response.sendRedirect(forma.getLinkSiguiente());
				return null;
			}
			else if(estado.equals("departamento"))
			{
				return this.accionDepartamento(con,forma,mundo,usuario,mapping);				
			}
			else if(estado.equals("ciudad"))
			{
				return this.accionCiudad(con,forma,mundo,usuario,mapping);				
			}
			else if(estado.equals("localidad"))
			{
				return this.accionLocalidad(con,forma,mundo,usuario,mapping);				
			}
			else if(estado.equals("barrio"))
			{
				return this.accionBarrio(con,forma,mundo,usuario,mapping);				
			}
			else if(estado.equals("nuevoPais"))
			{
				this.accionNuevoPais(forma);
				UtilidadBD.closeConnection(con);
				return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getPaisMap("numRegistros").toString()), response, request, "ubicacionGeografica.jsp",true);
			}
			else if(estado.equals("nuevoDepartamento"))
			{
				this.accionNuevoDepartamento(forma);
				UtilidadBD.closeConnection(con);
				return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getDepartamentoMap("numRegistros").toString()), response, request, "departamento.jsp",true);
			}
			else if(estado.equals("nuevoCiudad"))
			{
				this.accionNuevoCiudad(forma);
				UtilidadBD.closeConnection(con);
				return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getCiudadMap("numRegistros").toString()), response, request, "ciudad.jsp",true);
			}
			else if(estado.equals("nuevoLocalidad"))
			{
				this.accionNuevoLocalidad(forma);
				UtilidadBD.closeConnection(con);
				return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getLocalidadMap("numRegistros").toString()), response, request, "localidad.jsp",true);
			}
			else if(estado.equals("nuevoBarrio"))
			{
				this.accionNuevoBarrio(forma);
				UtilidadBD.closeConnection(con);
				return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getBarrioMap("numRegistros").toString()), response, request, "barrio.jsp",true);
			}
			else if(estado.equals("guardarPais"))
			{
				this.accionGuardarPais(con,forma,mundo,usuario);
				forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
				this.accionConsultarPais(con,forma,mundo);
				return mapping.findForward("principal");
			}
			else if(estado.equals("guardarDepartamento"))
			{
				this.accionGuardarDepartamento(con,forma,mundo,usuario);
				forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
				this.accionDepartamento(con,forma,mundo,usuario,mapping);
				return mapping.findForward("departamento");
			}
			else if(estado.equals("guardarCiudad"))
			{
				this.accionGuardarCiudad(con,forma,mundo,usuario);
				forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
				this.accionCiudad(con,forma,mundo,usuario,mapping);
				return mapping.findForward("ciudad");
			}
			else if(estado.equals("guardarLocalidad"))
			{
				this.accionGuardarLocalidad(con,forma,mundo,usuario);
				forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
				this.accionLocalidad(con,forma,mundo,usuario,mapping);
				return mapping.findForward("localidad");
			}
			else if(estado.equals("guardarBarrio"))
			{
				this.accionGuardarBarrio(con,forma,mundo,usuario);
				forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
				this.accionBarrio(con,forma,mundo,usuario,mapping);
				return mapping.findForward("barrio");
			}
			else if(estado.equals("ordenarPais"))
			{
				this.accionOrdenarMapaPais(forma);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("principal");
			}
			else if(estado.equals("ordenarDepartamento"))
			{
				this.accionOrdenarMapaDepartamento(forma);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("departamento");
			}
			else if(estado.equals("ordenarCiudad"))
			{
				this.accionOrdenarMapaCiudad(forma);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("ciudad");
			}
			else if(estado.equals("ordenarLocalidad"))
			{
				this.accionOrdenarMapaLocalidad(forma);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("localidad");
			}
			else if(estado.equals("ordenarBarrio"))
			{
				this.accionOrdenarMapaBarrio(forma);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("barrio");
			}
			else if(estado.equals("eliminarPais"))
			{
				UtilidadBD.closeConnection(con);
				Utilidades.eliminarRegistroMapaGenerico(forma.getPaisMap(),forma.getPaisEliminadoMap(),forma.getPosEliminar(), indicesPais, "numRegistros", "tiporegistro_", "BD", false);
				return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getPaisMap("numRegistros").toString()), response, request, "ubicacionGeografica.jsp",forma.getPosEliminar()==(Integer.parseInt(forma.getPaisMap("numRegistros")+"")));
			}
			else if(estado.equals("eliminarDepartamento"))
			{
				UtilidadBD.closeConnection(con);
				Utilidades.eliminarRegistroMapaGenerico(forma.getDepartamentoMap(),forma.getDepartamentoEliminadoMap(),forma.getPosEliminar(), indicesDepartamento, "numRegistros", "tiporegistro_", "BD", false);
				return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getDepartamentoMap("numRegistros").toString()), response, request, "departamento.jsp",forma.getPosEliminar()==(Integer.parseInt(forma.getDepartamentoMap("numRegistros")+"")));
			}
			else if(estado.equals("eliminarCiudad"))
			{
				UtilidadBD.closeConnection(con);
				Utilidades.eliminarRegistroMapaGenerico(forma.getCiudadMap(),forma.getCiudadEliminadoMap(),forma.getPosEliminar(), indicesCiudad, "numRegistros", "tiporegistro_", "BD", false);
				return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getCiudadMap("numRegistros").toString()), response, request, "ciudad.jsp",forma.getPosEliminar()==(Integer.parseInt(forma.getCiudadMap("numRegistros")+"")));
			}
			else if(estado.equals("eliminarLocalidad"))
			{
				UtilidadBD.closeConnection(con);
				Utilidades.eliminarRegistroMapaGenerico(forma.getLocalidadMap(),forma.getLocalidadEliminadoMap(),forma.getPosEliminar(), indicesLocalidad, "numRegistros", "tiporegistro_", "BD", false);
				return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getLocalidadMap("numRegistros").toString()), response, request, "localidad.jsp",forma.getPosEliminar()==(Integer.parseInt(forma.getLocalidadMap("numRegistros")+"")));
			}
			else if(estado.equals("eliminarBarrio"))
			{
				UtilidadBD.closeConnection(con);
				Utilidades.eliminarRegistroMapaGenerico(forma.getBarrioMap(),forma.getBarrioEliminadoMap(),forma.getPosEliminar(), indicesBarrio, "numRegistros", "tiporegistro_", "BD", false);
				return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getBarrioMap("numRegistros").toString()), response, request, "barrio.jsp",forma.getPosEliminar()==(Integer.parseInt(forma.getBarrioMap("numRegistros")+"")));
			}
			else
			{
				forma.reset();
				logger.warn("Estado no valido dentro del flujo de Ubicación Geográfica");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("paginaError");
			}
		}
		else
		{
			logger.error("El form no es compatible con el form de UbicacionGeograficaForm");
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
	 * Accion consultar pais
	 */
	private void accionConsultarPais(Connection con, UbicacionGeograficaForm forma, UbicacionGeografica mundo)
	{
		mundo.consultarPais(con);
		forma.setPaisMap((HashMap)mundo.getPaisMap().clone());
		logger.info("\n\n");
		logger.info("Valor de Mapa Consultado >> "+forma.getPaisMap());
		logger.info("\n\n");
	}
	
	/**
	 * accion departamento
	 * */
	private ActionForward accionDepartamento(Connection con, UbicacionGeograficaForm forma, UbicacionGeografica mundo,UsuarioBasico usuario, ActionMapping mapping)
	{
		forma.setDepartamentoMap(mundo.consultarDepartamento(con, forma.getPaisMap("codigo_pais_"+forma.getIndexSeleccionado())+""));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("departamento");
	}
	/**
	 * accion ciudad
	 * */
	private ActionForward accionCiudad(Connection con, UbicacionGeograficaForm forma, UbicacionGeografica mundo,UsuarioBasico usuario, ActionMapping mapping)
	{
		forma.setCiudadMap(mundo.consultarCiudad(con, forma.getDepartamentoMap("codigo_departamento_"+forma.getIndexSeleccionadoDepartamento())+"", forma.getPaisMap("codigo_pais_"+forma.getIndexSeleccionado())+""));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("ciudad");
	}
	/**
	 * accion localidad
	 * */
	private ActionForward accionLocalidad(Connection con, UbicacionGeograficaForm forma, UbicacionGeografica mundo,UsuarioBasico usuario, ActionMapping mapping)
	{
		forma.setLocalidadMap(mundo.consultarLocalidad(con, forma.getCiudadMap("codigo_ciudad_"+forma.getIndexSeleccionadoCiudad())+"",forma.getDepartamentoMap("codigo_departamento_"+forma.getIndexSeleccionadoDepartamento())+"", forma.getPaisMap("codigo_pais_"+forma.getIndexSeleccionado())+""));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("localidad");
	}
	/**
	 * accion barrio
	 * */
	private ActionForward accionBarrio(Connection con, UbicacionGeograficaForm forma, UbicacionGeografica mundo,UsuarioBasico usuario, ActionMapping mapping)
	{
		forma.setBarrioMap(mundo.consultarBarrio(con, forma.getLocalidadMap().get("codigo_localidad_"+forma.getIndexSeleccionadoLocalidad())+"", forma.getCiudadMap("codigo_ciudad_"+forma.getIndexSeleccionadoCiudad())+"",forma.getDepartamentoMap("codigo_departamento_"+forma.getIndexSeleccionadoDepartamento())+"", forma.getPaisMap("codigo_pais_"+forma.getIndexSeleccionado())+""));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("barrio");
	}
	
	/**
	 * Accion Nuevo pais
	 * */
	private void accionNuevoPais(UbicacionGeograficaForm forma)
	{
		int pos=Integer.parseInt(forma.getPaisMap("numRegistros")+"");
		forma.setPaisMap("codigo_pais_"+pos,"");
		forma.setPaisMap("descripcion_"+pos,"");
		forma.setPaisMap("tiporegistro_"+pos,"MEM");
		forma.setPaisMap("numRegistros",(pos+1)+"");
	}
	
	/**
	 * Accion Nuevo departamento
	 * */
	private void accionNuevoDepartamento(UbicacionGeograficaForm forma)
	{
		int pos=Integer.parseInt(forma.getDepartamentoMap("numRegistros")+"");
		forma.setDepartamentoMap("codigo_departamento_"+pos,"");
		forma.setDepartamentoMap("descripcion_"+pos,"");
		forma.setDepartamentoMap("codigo_pais_"+pos,forma.getPaisMap("codigo_pais_"+forma.getIndexSeleccionado())+"");
		forma.setDepartamentoMap("tiporegistro_"+pos,"MEM");
		forma.setDepartamentoMap("numRegistros",(pos+1)+"");
	}
	
	/**
	 * Accion Nuevo ciudad
	 * */
	private void accionNuevoCiudad(UbicacionGeograficaForm forma)
	{
		int pos=Integer.parseInt(forma.getCiudadMap("numRegistros")+"");
		forma.setCiudadMap("codigo_departamento_"+pos,forma.getDepartamentoMap("codigo_departamento_"+forma.getIndexSeleccionadoDepartamento())+"");
		forma.setCiudadMap("codigo_ciudad_"+pos,"");
		forma.setCiudadMap("descripcion_"+pos,"");
		forma.setCiudadMap("codigo_pais_"+pos,forma.getPaisMap("codigo_pais_"+forma.getIndexSeleccionado())+"");
		forma.setCiudadMap("localidad_"+pos,"");
		forma.setCiudadMap("tiporegistro_"+pos,"MEM");
		forma.setCiudadMap("numRegistros",(pos+1)+"");
	}
	
	/**
	 * Accion Nuevo localidad
	 * */
	private void accionNuevoLocalidad(UbicacionGeograficaForm forma)
	{
		int pos=Integer.parseInt(forma.getLocalidadMap("numRegistros")+"");
		forma.setLocalidadMap("codigo_localidad_"+pos,"");
		forma.setLocalidadMap("descripcion_"+pos,"");
		forma.setLocalidadMap("codigo_pais_"+pos,forma.getPaisMap("codigo_pais_"+forma.getIndexSeleccionado())+"");
		forma.setLocalidadMap("codigo_departamento_"+pos,forma.getDepartamentoMap("codigo_departamento_"+forma.getIndexSeleccionadoDepartamento())+"");
		forma.setLocalidadMap("codigo_ciudad_"+pos,forma.getCiudadMap("codigo_ciudad_"+forma.getIndexSeleccionadoCiudad()));
		forma.setLocalidadMap("tiporegistro_"+pos,"MEM");
		forma.setLocalidadMap("numRegistros",(pos+1)+"");
	}
	
	/**
	 * Accion Nuevo barrio
	 * */
	private void accionNuevoBarrio(UbicacionGeograficaForm forma)
	{
		int pos=Integer.parseInt(forma.getBarrioMap("numRegistros")+"");
		forma.setBarrioMap("codigo_departamento_"+pos,forma.getDepartamentoMap("codigo_departamento_"+forma.getIndexSeleccionadoDepartamento())+"");
		forma.setBarrioMap("codigo_ciudad_"+pos,forma.getCiudadMap("codigo_ciudad_"+forma.getIndexSeleccionadoCiudad()));
		forma.setBarrioMap("codigo_barrio_"+pos,"");
		forma.setBarrioMap("descripcion_"+pos,"");
		forma.setBarrioMap("codigo_pais_"+pos,forma.getPaisMap("codigo_pais_"+forma.getIndexSeleccionado())+"");
		forma.setBarrioMap("codigo_localidad_"+pos,"");
		forma.setBarrioMap("codigo_"+pos,"");
		forma.setBarrioMap("tiporegistro_"+pos,"MEM");
		forma.setBarrioMap("numRegistros",(pos+1)+"");
	}
	
	/**
	 * Accion Guardar Pais
	 * */
	private void accionGuardarPais(Connection con, UbicacionGeograficaForm forma, UbicacionGeografica mundo, UsuarioBasico usuario)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getPaisEliminadoMap("numRegistros")+"");i++)
		{
			if(mundo.eliminarPais(con,forma.getPaisEliminadoMap("codigo_pais_"+i)+""))
			{
				Utilidades.generarLogGenerico(forma.getPaisEliminadoMap(), new HashMap(), usuario.getLoginUsuario(), true, i, ConstantesBD.logUbicacionGeograficaCodigo, indicesPais);
				transaccion=true;
			}
		}
		
		for(int i=0;i<Integer.parseInt(forma.getPaisMap("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getPaisMap("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma.getPaisMap(),mundo.consultarPais(con),i,usuario,indicesPais))
			{
				HashMap vo=new HashMap();
				vo.put("codigo_pais",forma.getPaisMap("codigo_pais_"+i));
				vo.put("descripcion",forma.getPaisMap("descripcion_"+i));
				vo.put("usuario_modifica",usuario.getLoginUsuario());
				vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				vo.put("hora_modifica",UtilidadFecha.getHoraActual());
				transaccion=mundo.modificarPais(con, vo);
			}
			
			//insertar
			else if((forma.getPaisMap("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigo_pais",forma.getPaisMap("codigo_pais_"+i));
				vo.put("descripcion",forma.getPaisMap("descripcion_"+i));
				vo.put("usuario_modifica",usuario.getLoginUsuario());
				vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				vo.put("hora_modifica",UtilidadFecha.getHoraActual());
				transaccion=mundo.insertarPais(con, vo);
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
	 * Accion Guardar Departamento
	 * */
	private void accionGuardarDepartamento(Connection con, UbicacionGeograficaForm forma, UbicacionGeografica mundo, UsuarioBasico usuario)
	{
		
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getDepartamentoEliminadoMap("numRegistros")+"");i++)
		{
			if(mundo.eliminarDepartamento(con,forma.getDepartamentoEliminadoMap("codigo_departamento_"+i)+"",forma.getPaisMap("codigo_pais_"+forma.getIndexSeleccionado())+""))
			{
				Utilidades.generarLogGenerico(forma.getDepartamentoEliminadoMap(), new HashMap(), usuario.getLoginUsuario(), true, i, ConstantesBD.logUbicacionGeograficaCodigo, indicesDepartamento);
				transaccion=true;
			}
		}
		for(int i=0;i<Integer.parseInt(forma.getDepartamentoMap("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getDepartamentoMap("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma.getDepartamentoMap(),mundo.consultarDepartamento(con,forma.getPaisMap("codigo_pais_"+forma.getIndexSeleccionado())+""),i,usuario,indicesDepartamento))
			{
				HashMap vo=new HashMap();
				vo.put("codigo_departamento",forma.getDepartamentoMap("codigo_departamento_"+i));
				vo.put("descripcion",forma.getDepartamentoMap("descripcion_"+i));
				vo.put("codigo_pais",forma.getPaisMap("codigo_pais_"+forma.getIndexSeleccionado()));
				vo.put("usuario_modifica",usuario.getLoginUsuario());
				vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				vo.put("hora_modifica",UtilidadFecha.getHoraActual());
				transaccion=mundo.modificarDepartamento(con, vo);
			}
			
			//insertar
			else if((forma.getDepartamentoMap("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigo_departamento",forma.getDepartamentoMap("codigo_departamento_"+i));
				vo.put("descripcion",forma.getDepartamentoMap("descripcion_"+i));
				vo.put("codigo_pais",forma.getPaisMap("codigo_pais_"+forma.getIndexSeleccionado()));
				vo.put("usuario_modifica",usuario.getLoginUsuario());
				vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				vo.put("hora_modifica",UtilidadFecha.getHoraActual());
				transaccion=mundo.insertarDepartamento(con, vo);
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
	 * Accion Guardar Ciudad
	 * */
	private void accionGuardarCiudad(Connection con, UbicacionGeograficaForm forma, UbicacionGeografica mundo, UsuarioBasico usuario)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getCiudadEliminadoMap("numRegistros")+"");i++)
		{
			if(mundo.eliminarCiudad(con,forma.getCiudadEliminadoMap("codigo_ciudad_"+i)+"",forma.getDepartamentoMap("codigo_departamento_"+forma.getIndexSeleccionadoDepartamento())+"",forma.getPaisMap("codigo_pais_"+forma.getIndexSeleccionado())+""))
			{
				Utilidades.generarLogGenerico(forma.getCiudadEliminadoMap(), new HashMap(), usuario.getLoginUsuario(), true, i, ConstantesBD.logUbicacionGeograficaCodigo, indicesCiudad);
				transaccion=true;
			}
		}
		for(int i=0;i<Integer.parseInt(forma.getCiudadMap("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getCiudadMap("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma.getCiudadMap(),mundo.consultarCiudad(con,forma.getDepartamentoMap("codigo_departamento_"+forma.getIndexSeleccionadoDepartamento())+"",forma.getPaisMap("codigo_pais_"+forma.getIndexSeleccionado())+""),i,usuario,indicesCiudad))
			{
				HashMap vo=new HashMap();
				vo.put("codigo_departamento",forma.getDepartamentoMap("codigo_departamento_"+forma.getIndexSeleccionadoDepartamento()));
				vo.put("codigo_ciudad",forma.getCiudadMap("codigo_ciudad_"+i));
				vo.put("descripcion",forma.getCiudadMap("descripcion_"+i));
				vo.put("codigo_pais",forma.getPaisMap("codigo_pais_"+forma.getIndexSeleccionado()));
				vo.put("localidad",forma.getCiudadMap("localidad_"+i));
				vo.put("usuario_modifica",usuario.getLoginUsuario());
				vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				vo.put("hora_modifica",UtilidadFecha.getHoraActual());
				transaccion=mundo.modificarCiudad(con, vo);
			}
			
			//insertar
			else if((forma.getCiudadMap("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigo_departamento",forma.getDepartamentoMap("codigo_departamento_"+forma.getIndexSeleccionadoDepartamento()));
				vo.put("codigo_ciudad",forma.getCiudadMap("codigo_ciudad_"+i));
				vo.put("descripcion",forma.getCiudadMap("descripcion_"+i));
				vo.put("codigo_pais",forma.getPaisMap("codigo_pais_"+forma.getIndexSeleccionado()));
				vo.put("localidad",forma.getCiudadMap("localidad_"+i));
				vo.put("usuario_modifica",usuario.getLoginUsuario());
				vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				vo.put("hora_modifica",UtilidadFecha.getHoraActual());
				transaccion=mundo.insertarCiudad(con, vo);
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
	 * Accion Guardar Localidad
	 * */
	private void accionGuardarLocalidad(Connection con, UbicacionGeograficaForm forma, UbicacionGeografica mundo, UsuarioBasico usuario)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getLocalidadEliminadoMap("numRegistros")+"");i++)
		{
			if(mundo.eliminarLocalidad(con,forma.getLocalidadEliminadoMap("codigo_localidad_"+i)+"",forma.getCiudadMap("codigo_ciudad_"+forma.getIndexSeleccionadoCiudad())+"",forma.getDepartamentoMap("codigo_departamento_"+forma.getIndexSeleccionadoDepartamento())+"",forma.getPaisMap("codigo_pais_"+forma.getIndexSeleccionado())+""))
			{
				Utilidades.generarLogGenerico(forma.getLocalidadEliminadoMap(), new HashMap(), usuario.getLoginUsuario(), true, i, ConstantesBD.logUbicacionGeograficaCodigo, indicesLocalidad);
				transaccion=true;
			}
		}
		
		for(int i=0;i<Integer.parseInt(forma.getLocalidadMap("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getLocalidadMap("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma.getLocalidadMap(),mundo.consultarLocalidad(con,forma.getCiudadMap("codigo_ciudad_"+forma.getIndexSeleccionadoCiudad())+"",forma.getDepartamentoMap("codigo_departamento_"+forma.getIndexSeleccionadoDepartamento())+"",forma.getPaisMap("codigo_pais_"+forma.getIndexSeleccionado())+""),i,usuario,indicesLocalidad))
			{
				HashMap vo=new HashMap();
				vo.put("codigo_localidad",forma.getLocalidadMap("codigo_localidad_"+i));
				vo.put("descripcion",forma.getLocalidadMap("descripcion_"+i));
				vo.put("codigo_pais",forma.getPaisMap("codigo_pais_"+forma.getIndexSeleccionado()));
				vo.put("codigo_departamento",forma.getDepartamentoMap("codigo_departamento_"+forma.getIndexSeleccionadoDepartamento()));
				vo.put("codigo_ciudad",forma.getCiudadMap("codigo_ciudad_"+forma.getIndexSeleccionadoCiudad()));
				vo.put("usuario_modifica",usuario.getLoginUsuario());
				vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				vo.put("hora_modifica",UtilidadFecha.getHoraActual());
				transaccion=mundo.modificarLocalidad(con, vo);
			}
			
			//insertar
			else if((forma.getLocalidadMap("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigo_localidad",forma.getLocalidadMap("codigo_localidad_"+i));
				vo.put("descripcion",forma.getLocalidadMap("descripcion_"+i));
				vo.put("codigo_pais",forma.getPaisMap("codigo_pais_"+forma.getIndexSeleccionado()));
				vo.put("codigo_departamento",forma.getDepartamentoMap("codigo_departamento_"+forma.getIndexSeleccionadoDepartamento()));
				vo.put("codigo_ciudad",forma.getCiudadMap("codigo_ciudad_"+forma.getIndexSeleccionadoCiudad()));
				vo.put("usuario_modifica",usuario.getLoginUsuario());
				vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				vo.put("hora_modifica",UtilidadFecha.getHoraActual());
				transaccion=mundo.insertarLocalidad(con, vo);
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
	 * Accion Guardar Barrio
	 * */
	private void accionGuardarBarrio(Connection con, UbicacionGeograficaForm forma, UbicacionGeografica mundo, UsuarioBasico usuario)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getBarrioEliminadoMap("numRegistros")+"");i++)
		{
			if(mundo.eliminarBarrio(con,Integer.parseInt(forma.getBarrioEliminadoMap("codigo_"+i)+"")))
			{
				Utilidades.generarLogGenerico(forma.getBarrioEliminadoMap(), new HashMap(), usuario.getLoginUsuario(), true, i, ConstantesBD.logUbicacionGeograficaCodigo, indicesBarrio);
				transaccion=true;
			}
		}
		
		for(int i=0;i<Integer.parseInt(forma.getBarrioMap("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getBarrioMap("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma.getBarrioMap(),mundo.consultarBarrio(con,forma.getLocalidadMap().get("codigo_localidad_"+forma.getIndexSeleccionadoLocalidad())+"",forma.getCiudadMap("codigo_ciudad_"+forma.getIndexSeleccionadoCiudad())+"",forma.getDepartamentoMap("codigo_departamento_"+forma.getIndexSeleccionadoDepartamento())+"",forma.getPaisMap("codigo_pais_"+forma.getIndexSeleccionado())+""),i,usuario,indicesBarrio))
			{
				HashMap vo=new HashMap();
				vo.put("codigo_departamento",forma.getDepartamentoMap("codigo_departamento_"+forma.getIndexSeleccionadoDepartamento()));
				vo.put("codigo_ciudad",forma.getCiudadMap("codigo_ciudad_"+forma.getIndexSeleccionadoCiudad()));
				vo.put("codigo_barrio",forma.getBarrioMap("codigo_barrio_"+i));
				vo.put("descripcion",forma.getBarrioMap("descripcion_"+i));
				vo.put("codigo_pais",forma.getPaisMap("codigo_pais_"+forma.getIndexSeleccionado()));
				vo.put("codigo_localidad",forma.getLocalidadMap("codigo_localidad_"+forma.getIndexSeleccionadoLocalidad()));
				vo.put("codigo",forma.getBarrioMap("codigo_"+i));
				vo.put("usuario_modifica",usuario.getLoginUsuario());
				vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				vo.put("hora_modifica",UtilidadFecha.getHoraActual());
				transaccion=mundo.modificarBarrio(con, vo);
			}
			
			//insertar
			else if((forma.getBarrioMap("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigo_departamento",forma.getDepartamentoMap("codigo_departamento_"+forma.getIndexSeleccionadoDepartamento()));
				vo.put("codigo_ciudad",forma.getCiudadMap("codigo_ciudad_"+forma.getIndexSeleccionadoCiudad()));
				vo.put("codigo_barrio",forma.getBarrioMap("codigo_barrio_"+i));
				vo.put("descripcion",forma.getBarrioMap("descripcion_"+i));
				vo.put("codigo_pais",forma.getPaisMap("codigo_pais_"+forma.getIndexSeleccionado()));
				vo.put("codigo_localidad",forma.getLocalidadMap("codigo_localidad_"+forma.getIndexSeleccionadoLocalidad()));
				vo.put("codigo",forma.getBarrioMap("codigo_"+i));
				vo.put("usuario_modifica",usuario.getLoginUsuario());
				vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				vo.put("hora_modifica",UtilidadFecha.getHoraActual());
				transaccion=mundo.insertarBarrio(con, vo);
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
	 * Existe modificacion
	 * */
	private boolean existeModificacion(Connection con, HashMap mapa, HashMap mapaTemp, int pos, UsuarioBasico usuario, String[] indices)
	{
		for(int i=0;i<indices.length;i++)
		{
			if(mapaTemp.containsKey(indices[i]+"0")&&mapa.containsKey(indices[i]+""+pos))
			{
				System.out.print((mapaTemp.get(indices[i]+"0")+"").trim()+"---"+((mapa.get(indices[i]+""+pos)+"").trim()));
				if(!((mapaTemp.get(indices[i]+"0")+"").trim().equals((mapa.get(indices[i]+""+pos)+"").trim())))
				{
					Utilidades.generarLogGenerico(mapa, mapaTemp, usuario.getLoginUsuario(), false, pos,ConstantesBD.logUbicacionGeograficaCodigo,indices);
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Accion ordenar mapa pais
	 * */
	private void accionOrdenarMapaPais(UbicacionGeograficaForm forma)
	{
		
		int numReg=Integer.parseInt(forma.getPaisMap("numRegistros")+"");
		forma.setPaisMap(Listado.ordenarMapa(indicesPais,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getPaisMap(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setPaisMap("numRegistros",numReg+"");
	}
	
	/**
	 * Accion ordenar mapa departamento
	 * */
	private void accionOrdenarMapaDepartamento(UbicacionGeograficaForm forma)
	{
		
		int numReg=Integer.parseInt(forma.getDepartamentoMap("numRegistros")+"");
		forma.setDepartamentoMap(Listado.ordenarMapa(indicesDepartamento,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getDepartamentoMap(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setDepartamentoMap("numRegistros",numReg+"");
	}
	
	/**
	 * Accion ordenar mapa ciudad
	 * */
	private void accionOrdenarMapaCiudad(UbicacionGeograficaForm forma)
	{
		
		int numReg=Integer.parseInt(forma.getCiudadMap("numRegistros")+"");
		forma.setCiudadMap(Listado.ordenarMapa(indicesCiudad,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getCiudadMap(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setCiudadMap("numRegistros",numReg+"");
	}
	
	/**
	 * Accion ordenar mapa localidad
	 * */
	private void accionOrdenarMapaLocalidad(UbicacionGeograficaForm forma)
	{
		
		int numReg=Integer.parseInt(forma.getLocalidadMap("numRegistros")+"");
		forma.setLocalidadMap(Listado.ordenarMapa(indicesLocalidad,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getLocalidadMap(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setLocalidadMap("numRegistros",numReg+"");
	}
	
	/**
	 * Accion ordenar mapa barrio
	 * */
	private void accionOrdenarMapaBarrio(UbicacionGeograficaForm forma)
	{
		
		int numReg=Integer.parseInt(forma.getBarrioMap("numRegistros")+"");
		forma.setBarrioMap(Listado.ordenarMapa(indicesBarrio,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getBarrioMap(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setBarrioMap("numRegistros",numReg+"");
	}
}
