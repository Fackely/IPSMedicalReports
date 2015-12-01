/*
 * Creado May 22, 2007
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * DetalleInclusionesExclusiones
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
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.Utilidades;

import com.princetonsa.actionform.facturacion.DetalleInclusionesExclusionesForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.DetalleInclusionesExclusiones;
import com.princetonsa.mundo.facturacion.InclusionesExclusiones;

/**
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 22, 2007
 */
public class DetalleInclusionesExclusionesAction extends Action
{

	/**
	 * 
	 */
	private Logger logger=Logger.getLogger(DetalleInclusionesExclusionesAction.class);

	/**
	 * 
	 */
	private String[] indicesIncluExclu={"codigo_","codigoincluexclu_","nomincluexclu_","codigocc_","nomcc_","tiporegistro_"};
	

	/**
	 * 
	 */
	private String[] indicesAgrupacionArticulos={"codigo_","codigodetincluexclu_","clase_","nomclase_","grupo_","nomgrupo_","subgrupo_","nomsubgrupo_","naturaleza_","nomnaturaleza_","incluye_","cantidad_","tiporegistro_"};
	
	/**
	 * 
	 */
	private String[] indicesArticulos={"codigo_","codigodetincluexclu_","codigoArticulo_","descripcionArticulo_","incluye_","cantidad_","tiporegistro_"};
	
	/**
	 * 
	 */
	private String[] indicesAgrupacionServicio={"codigo_","codigodetincluexclu_","tipopos_","gruposervicio_","acronimogruposervicio_","descgruposervicio_","tiposervicio_","nomtiposervicio_","especialidad_","nomespecialidad_","incluye_","cantidad_","tiporegistro_"};
	
	/**
	 * 
	 */
	private String[] indicesServicios={"codigo_","codigodetincluexclu_","codigoServicio_","descripcionServicio_","incluye_","cantidad_","tiporegistro_"};


	
	
	/**
	 * Metodo que lleva el flujo de la fuincionalidad
	 */
	public ActionForward execute(	ActionMapping mapping, 	ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		Connection con=null;
		try{
			if(form instanceof DetalleInclusionesExclusionesForm)
			{
				DetalleInclusionesExclusionesForm forma=(DetalleInclusionesExclusionesForm)form;
				DetalleInclusionesExclusiones mundo=new DetalleInclusionesExclusiones();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
				con=UtilidadBD.abrirConexion();
				String estado=forma.getEstado();

				logger.info("ESTADO -->"+estado);


				if(estado.equals("empezar"))
				{
					forma.resetMensaje();
					return this.accionEmpezar(con,forma,mundo,usuario,mapping);
				}
				else if(estado.equals("nuevaIncluExclu"))
				{
					Utilidades.nuevoRegistroMapaGenerico(forma.getInclusionesExclusiones(),indicesIncluExclu,"numRegistros","tiporegistro_","MEM");
					forma.resetMensaje();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("eliminarIncluExclu"))
				{
					Utilidades.eliminarRegistroMapaGenerico(forma.getInclusionesExclusiones(),forma.getInclusionesExclusionesEliminados(),forma.getPosEliminar(),indicesIncluExclu,"numRegistros","tiporegistro_","BD",false);
					forma.resetMensaje();
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("guardarIncluExclu"))
				{
					return this.accionGuardarIncluExclu(con, forma, mundo, usuario, mapping);
				}
				else if(estado.equals("detalleIncluExclu"))
				{
					return this.accionDetalleIncluExclu(con,forma,mundo,usuario,mapping);
				}
				else if(estado.equals("nuevaAgrupacion"))
				{
					Utilidades.nuevoRegistroMapaGenerico(forma.getAgrupacionArticulos(),indicesAgrupacionArticulos,"numRegistros","tiporegistro_","MEM");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");

				}
				else if(estado.equals("eliminarAgrupacion"))
				{
					Utilidades.eliminarRegistroMapaGenerico(forma.getAgrupacionArticulos(),forma.getAgrupacionArticulosEliminados(),forma.getPosEliminar(),indicesAgrupacionArticulos,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");	
				}

				else if(estado.equals("nuevoArticulo"))
				{
					String[] indicesTemp={"codigo_","codigodetincluexclu_","incluye_","cantidad_","tiporegistro_"};
					Utilidades.nuevoRegistroMapaGenerico(forma.getArticulos(),indicesTemp,"numRegistros","tiporegistro_","MEM");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");
				}
				else if(estado.equals("eliminarArticulo"))
				{
					Utilidades.eliminarRegistroMapaGenerico(forma.getArticulos(),forma.getArticulosEliminados(),forma.getPosEliminar(),indicesArticulos,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");	
				}
				else if(estado.equals("nuevaAgrupacionServicio"))
				{
					Utilidades.nuevoRegistroMapaGenerico(forma.getAgrupacionServicios(),indicesAgrupacionServicio,"numRegistros","tiporegistro_","MEM");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");

				}
				else if(estado.equals("eliminarAgrupacionServicio"))
				{
					Utilidades.eliminarRegistroMapaGenerico(forma.getAgrupacionServicios(),forma.getAgrupacionServiciosEliminados(),forma.getPosEliminar(),indicesAgrupacionServicio,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");	
				}
				else if(estado.equals("nuevoServicio"))
				{
					String[] indicesTemp={"codigo_","codigodetincluexclu_","incluye_","cantidad_","tiporegistro_"};
					Utilidades.nuevoRegistroMapaGenerico(forma.getServicios(),indicesTemp,"numRegistros","tiporegistro_","MEM");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");
				}
				else if(estado.equals("eliminarServicio"))
				{
					Utilidades.eliminarRegistroMapaGenerico(forma.getServicios(),forma.getServiciosEliminados(),forma.getPosEliminar(),indicesServicios,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("detalle");	
				}
				else if(estado.equals("guardar"))
				{
					this.accionGuardar(con,forma,mundo,usuario);
					//consultar lo que se guardo.
					return this.accionDetalleIncluExclu(con,forma,mundo,usuario,mapping);
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
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardar(Connection con, DetalleInclusionesExclusionesForm forma, DetalleInclusionesExclusiones mundo, UsuarioBasico usuario)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		forma.resetMensaje();
		
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
			forma.getMensaje().setResultado(true);
			forma.getMensaje().setDescripcion("Proceso Realizado Exitosamente");
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.getMensaje().setDescripcion("Se ha producido un error en el proceso");
			UtilidadBD.abortarTransaccion(con);
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
	private boolean accionGuardarServicios(Connection con, DetalleInclusionesExclusionesForm forma, DetalleInclusionesExclusiones mundo, UsuarioBasico usuario, boolean transaccion)
	{
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getServiciosEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarServicios(con,forma.getServiciosEliminados().get("codigo_"+i)+""))
			{
				Utilidades.generarLogGenerico(forma.getServiciosEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logDetalleInclusionesExclusionesCodigo,indicesServicios);
				transaccion=true;
			}
		}
		for(int i=0;i<Integer.parseInt(forma.getServicios("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getServicios("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma.getServicios(),mundo.consultarServiciosLLave(con, forma.getServicios("codigo_"+i)+""),i,usuario,indicesServicios))
			{
				HashMap vo=new HashMap();
				vo.put("codigo", forma.getServicios("codigo_"+i)+"");
				vo.put("servicio", forma.getServicios("codigoServicio_"+i));
				vo.put("incluye", forma.getServicios("incluye_"+i));
				vo.put("cantidad", forma.getServicios("cantidad_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.modificarServicios(con, vo);
			}
			//insertar
			else if((forma.getServicios("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigoDetIncluExclu", forma.getInclusionesExclusiones("codigo_"+forma.getIndexSeleccionado())+"");
				vo.put("servicio", forma.getServicios("codigoServicio_"+i));
				vo.put("incluye", forma.getServicios("incluye_"+i));
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
	private boolean accionGuardarAgrupacionServicios(Connection con, DetalleInclusionesExclusionesForm forma, DetalleInclusionesExclusiones mundo, UsuarioBasico usuario, boolean transaccion)
	{
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getAgrupacionServiciosEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarAgrupacionServicios(con,forma.getAgrupacionServiciosEliminados().get("codigo_"+i)+""))
			{
				Utilidades.generarLogGenerico(forma.getAgrupacionServiciosEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logDetalleInclusionesExclusionesCodigo,indicesAgrupacionServicio);
				transaccion=true;
			}
		}
		for(int i=0;i<Integer.parseInt(forma.getAgrupacionServicios("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getAgrupacionServicios("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma.getAgrupacionServicios(),mundo.consultarAgrupacionServiciosLLave(con, forma.getAgrupacionServicios("codigo_"+i)+""),i,usuario,indicesAgrupacionServicio))
			{
				HashMap vo=new HashMap();
				vo.put("codigo", forma.getAgrupacionServicios("codigo_"+i)+"");
				vo.put("tipopos", forma.getAgrupacionServicios("tipopos_"+i));
				vo.put("gruposervicio", forma.getAgrupacionServicios("gruposervicio_"+i));
				vo.put("tiposervicio", forma.getAgrupacionServicios("tiposervicio_"+i));
				vo.put("especialidad", forma.getAgrupacionServicios("especialidad_"+i));
				vo.put("incluye", forma.getAgrupacionServicios("incluye_"+i));
				vo.put("cantidad", forma.getAgrupacionServicios("cantidad_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.modificarAgrupacionServicios(con, vo);
			}
			//insertar
			else if((forma.getAgrupacionServicios("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigoDetIncluExclu", forma.getInclusionesExclusiones("codigo_"+forma.getIndexSeleccionado())+"");
				vo.put("tipopos", forma.getAgrupacionServicios("tipopos_"+i));
				vo.put("gruposervicio", forma.getAgrupacionServicios("gruposervicio_"+i));
				vo.put("tiposervicio", forma.getAgrupacionServicios("tiposervicio_"+i));
				vo.put("especialidad", forma.getAgrupacionServicios("especialidad_"+i));
				vo.put("incluye", forma.getAgrupacionServicios("incluye_"+i));
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
	private boolean accionGuardarArticulos(Connection con, DetalleInclusionesExclusionesForm forma, DetalleInclusionesExclusiones mundo, UsuarioBasico usuario, boolean transaccion)
	{
//		eliminar
		for(int i=0;i<Integer.parseInt(forma.getArticulosEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarArticulos(con,forma.getArticulosEliminados().get("codigo_"+i)+""))
			{
				Utilidades.generarLogGenerico(forma.getArticulosEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logDetalleInclusionesExclusionesCodigo,indicesArticulos);
				transaccion=true;
			}
		}
		for(int i=0;i<Integer.parseInt(forma.getArticulos("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getArticulos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma.getArticulos(),mundo.consultarArticulosLLave(con, forma.getArticulos("codigo_"+i)+""),i,usuario,indicesArticulos))
			{
				HashMap vo=new HashMap();
				vo.put("codigo", forma.getArticulos("codigo_"+i)+"");
				vo.put("articulo", forma.getArticulos("codigoArticulo_"+i));
				vo.put("incluye", forma.getArticulos("incluye_"+i));
				vo.put("cantidad", forma.getArticulos("cantidad_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.modificarArticulos(con, vo);
			}
			//insertar
			else if((forma.getArticulos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigoDetIncluExclu", forma.getInclusionesExclusiones("codigo_"+forma.getIndexSeleccionado())+"");
				vo.put("articulo", forma.getArticulos("codigoArticulo_"+i));
				vo.put("incluye", forma.getArticulos("incluye_"+i));
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
	 * @param transaccion
	 * @return
	 */
	private boolean accionGuardarAgrupacionArticulos(Connection con, DetalleInclusionesExclusionesForm forma, DetalleInclusionesExclusiones mundo, UsuarioBasico usuario, boolean transaccion)
	{
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getAgrupacionArticulosEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarAgrupacionArticulos(con,forma.getAgrupacionArticulosEliminados().get("codigo_"+i)+""))
			{
				Utilidades.generarLogGenerico(forma.getAgrupacionArticulosEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logDetalleInclusionesExclusionesCodigo,indicesAgrupacionArticulos);
				transaccion=true;
			}
		}
		
		for(int i=0;i<Integer.parseInt(forma.getAgrupacionArticulos("numRegistros")+"");i++)
		{
			if((forma.getAgrupacionArticulos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma.getAgrupacionArticulos(),mundo.consultarAgrupacionArticulosLLave(con, forma.getAgrupacionArticulos("codigo_"+i)+""),i,usuario,indicesAgrupacionArticulos))
			{
				HashMap vo=new HashMap();
				vo.put("codigo", forma.getAgrupacionArticulos("codigo_"+i)+"");
				vo.put("clase", forma.getAgrupacionArticulos("clase_"+i));
				vo.put("grupo", forma.getAgrupacionArticulos("grupo_"+i));
				vo.put("subgrupo", forma.getAgrupacionArticulos("subgrupo_"+i));
				vo.put("naturaleza", forma.getAgrupacionArticulos("naturaleza_"+i));
				vo.put("incluye", forma.getAgrupacionArticulos("incluye_"+i));
				vo.put("cantidad", forma.getAgrupacionArticulos("cantidad_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.modificarAgrupacionArticulos(con, vo);
			}
			//insertar
			else if((forma.getAgrupacionArticulos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigoDetIncluExclu", forma.getInclusionesExclusiones("codigo_"+forma.getIndexSeleccionado())+"");
				vo.put("clase", forma.getAgrupacionArticulos("clase_"+i));
				vo.put("grupo", forma.getAgrupacionArticulos("grupo_"+i));
				vo.put("subgrupo", forma.getAgrupacionArticulos("subgrupo_"+i));
				vo.put("naturaleza", forma.getAgrupacionArticulos("naturaleza_"+i));
				vo.put("subgrupo", forma.getAgrupacionArticulos("subgrupo_"+i));
				vo.put("naturaleza", forma.getAgrupacionArticulos("naturaleza_"+i));
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("incluye", forma.getAgrupacionArticulos("incluye_"+i));
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
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionDetalleIncluExclu(Connection con, DetalleInclusionesExclusionesForm forma, DetalleInclusionesExclusiones mundo, UsuarioBasico usuario, ActionMapping mapping)
	{
		forma.resetMapasEliminacion();
		forma.setAgrupacionArticulos(mundo.consultarAgrupacionArticulos(con,forma.getInclusionesExclusiones("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setArticulos(mundo.consultarArticulos(con,forma.getInclusionesExclusiones("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setAgrupacionServicios(mundo.consultarAgrupacionServicios(con,forma.getInclusionesExclusiones("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setServicios(mundo.consultarServicios(con,forma.getInclusionesExclusiones("codigo_"+forma.getIndexSeleccionado())+""));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("detalle");
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
	private ActionForward accionGuardarIncluExclu(Connection con, DetalleInclusionesExclusionesForm forma, DetalleInclusionesExclusiones mundo, UsuarioBasico usuario, ActionMapping mapping)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getInclusionesExclusionesEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarInclusionesExclusiones(con,forma.getInclusionesExclusionesEliminados().get("codigo_"+i)+""))
			{
				Utilidades.generarLogGenerico(forma.getInclusionesExclusionesEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logDetalleInclusionesExclusionesCodigo,indicesIncluExclu);
				transaccion=true;
			}
		}
		for(int i=0;i<Integer.parseInt(forma.getInclusionesExclusiones("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getInclusionesExclusiones("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma.getInclusionesExclusiones(),mundo.consultarInclusionExclusionLLave(con, forma.getInclusionesExclusiones("codigo_"+i)+""),i,usuario,indicesIncluExclu))
			{
				HashMap vo=new HashMap();
				vo.put("codigo",forma.getInclusionesExclusiones("codigo_"+i)+"");
				vo.put("codigoIncluExclu", forma.getInclusionesExclusiones("codigoincluexclu_"+i)+"");
				vo.put("codigoCC", forma.getInclusionesExclusiones("codigocc_"+i));
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.modificarInclusionExclusion(con, vo);
			}
			//insertar
			else if((forma.getInclusionesExclusiones("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigoIncluExclu", forma.getInclusionesExclusiones("codigoincluexclu_"+i)+"");
				vo.put("codigoCC", forma.getInclusionesExclusiones("codigocc_"+i));
				vo.put("prioridad", forma.getInclusionesExclusiones("prioridad_"+i));
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.insertarInclusionExclusion(con, vo);
			}
		}
		if(transaccion)
		{
			forma.setMensaje(new ResultadoBoolean(true, "PROCESO REALIZADO CON ÉXITO !!!"));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMensaje(new ResultadoBoolean(true, "SE PRESENTARON PROBLEMAS AL GUARDAR !!!"));
			UtilidadBD.abortarTransaccion(con);
		}	
		return this.accionEmpezar(con,forma,mundo,usuario,mapping);
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
	private ActionForward accionEmpezar(Connection con, DetalleInclusionesExclusionesForm forma, DetalleInclusionesExclusiones mundo, UsuarioBasico usuario, ActionMapping mapping)
	{
		forma.reset();
		forma.setCentrosCosto(Utilidades.obtenerCentrosCosto(con,usuario.getCodigoInstitucionInt(),ConstantesBD.codigoTipoAreaDirecto+"",false,ConstantesBD.codigoNuncaValido,false));
		forma.setInclusiones(InclusionesExclusiones.consultarInclusionesExclusiones(con,usuario.getCodigoInstitucionInt()));
		forma.setInclusionesExclusiones(mundo.consultarInclusionesExclusiones(con,usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
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
					Utilidades.generarLogGenerico(mapa, mapaTemp, usuario.getLoginUsuario(), false, pos,ConstantesBD.logDetalleInclusionesExclusionesCodigo,indices);
					return true;
				}
			}
		}
		return false;
	}
}
