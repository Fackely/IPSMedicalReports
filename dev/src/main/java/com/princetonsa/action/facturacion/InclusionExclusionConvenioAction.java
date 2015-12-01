/*
 * Creado May 23, 2007
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * InclusionExclusionConvenioAction
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
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.actionform.facturacion.InclusionExclusionConvenioForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.InclusionExclusionConvenio;

/**
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 23, 2007
 */
public class InclusionExclusionConvenioAction extends Action
{
	/**
	 * 
	 */
	private Logger logger=Logger.getLogger(InclusionExclusionConvenioAction.class);
	
	/**
	 * 
	 */
	private static String[] indicesInclusionesExclusiones={"codigoincluexclucc_","nomcentrocosto_","nomincluexclu_","prioridad_","tiporegistro_","fechavigencia_","fechavigenciaanterior_"};
	
	/**
	 * 
	 */
	private static String[] indicesExcepciones={"codigo_","centrocosto_","nomcentrocosto_","tiporegistro_","fechavigencia_","fechavigenciaanterior_"};
	
	

	/**
	 * 
	 */
	private String[] indicesAgrupacionArticulos={"codigo_","codigoexcepcion_","clase_","nomclase_","grupo_","nomgrupo_","subgrupo_","nomsubgrupo_","naturaleza_","nomnaturaleza_","incluye_","cantidad_","tiporegistro_","fechavigencia_"};
	
	/**
	 * 
	 */
	private String[] indicesArticulos={"codigo_","codigoExcepcion_","codigoArticulo_","descripcionArticulo_","incluye_","cantidad_","tiporegistro_","fechavigencia_"};
	
	/**
	 * 
	 */
	private String[] indicesAgrupacionServicio={"codigo_","codigoexcepcion_","tipopos_","gruposervicio_","acronimogruposervicio_","descgruposervicio_","tiposervicio_","nomtiposervicio_","especialidad_","nomespecialidad_","incluye_","cantidad_","tiporegistro_","fechavigencia_"};
	
	/**
	 * 
	 */
	private String[] indicesServicios={"codigo_","codigoExcepcion_","codigoServicio_","descripcionServicio_","incluye_","cantidad_","tiporegistro_","fechavigencia_"};

			
	/**
	 * Metodo que lleva el flujo de la fuincionalidad
	 */
	public ActionForward execute(	ActionMapping mapping, 	ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		Connection con=null;
		try{
			if(form instanceof InclusionExclusionConvenioForm)
			{
				InclusionExclusionConvenioForm forma=(InclusionExclusionConvenioForm)form;
				InclusionExclusionConvenio mundo=new InclusionExclusionConvenio();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
				con=UtilidadBD.abrirConexion();
				String estado=forma.getEstado();

				logger.info("ESTADO -->"+estado);


				if(estado.equals("empezar"))
				{
					forma.reset();
					forma.setConvenios(Utilidades.obtenerConvenios(con, "","",false,"",false));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("cargarContratos"))
				{
					forma.setContrato(ConstantesBD.codigoNuncaValido);
					forma.setContratos(Utilidades.obtenerContratos(con,forma.getCodigoConvenio(),true,false));
					if(forma.getContratos().size()==1)
					{
						forma.setContrato(Integer.parseInt(((HashMap)(forma.getContratos().get(0))).get("codigo")+""));
						return this.accionCargarInfo(con,forma,mundo,usuario,mapping);
					}
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("cargarInfo"))
				{
					return this.accionCargarInfo(con,forma,mundo,usuario,mapping);
				}
				else if(estado.equals("nuevaIncluExclu"))
				{
					Utilidades.nuevoRegistroMapaGenerico(forma.getInclusionesExclusiones(),indicesInclusionesExclusiones,"numRegistros","tiporegistro_","MEM");
					int numRegistros=Utilidades.convertirAEntero(forma.getInclusionesExclusiones().get("numRegistros")+"");
					forma.setInclusionesExclusiones("prioridad_"+(numRegistros-1),numRegistros);
					forma.setInclusionesExclusiones("fechavigencia_"+(numRegistros-1),UtilidadFecha.getFechaActual(con));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("eliminarIncluExclu"))
				{
					Utilidades.eliminarRegistroMapaGenerico(forma.getInclusionesExclusiones(),forma.getInclusionesExclusionesEliminados(),forma.getPosEliminar(),indicesInclusionesExclusiones,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("nuevaExcepcion"))
				{
					Utilidades.nuevoRegistroMapaGenerico(forma.getExcepciones(),indicesExcepciones,"numRegistros","tiporegistro_","MEM");
					forma.setExcepciones("fechavigencia_"+(Utilidades.convertirAEntero(forma.getExcepciones("numRegistros")+"")-1),UtilidadFecha.getFechaActual(con));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("eliminarExcepcion"))
				{
					Utilidades.eliminarRegistroMapaGenerico(forma.getExcepciones(),forma.getExcepcionesEliminados(),forma.getPosEliminar(),indicesExcepciones,"numRegistros","tiporegistro_","BD",false);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("guardarGeneral"))
				{
					return this.accionGuardarGeneral(con, forma, mundo, usuario, mapping);
				}
				else if(estado.equals("detalleExcepcion"))
				{
					forma.setExcepcionesAnterior(false);
					return this.accionDetalleExcepcion(con,forma,mundo,usuario,mapping);
				}
				else if(estado.equals("detalleExcepcionAnterior"))
				{
					forma.setExcepcionesAnterior(true);
					return this.accionDetalleExcepcionAnterior(con,forma,mundo,usuario,mapping);
				}
				else if(estado.equals("nuevaAgrupacion"))
				{
					Utilidades.nuevoRegistroMapaGenerico(forma.getAgrupacionArticulos(),indicesAgrupacionArticulos,"numRegistros","tiporegistro_","MEM");
					forma.getAgrupacionArticulos().put("fechavigencia_"+(Utilidades.convertirAEntero(forma.getAgrupacionArticulos().get("numRegistros")+"")-1), forma.getFechaVigencia()+"");
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
					String[] indicesTemp={"codigo_","codigoexcepcion_","incluye_","cantidad_","tiporegistro_"};
					Utilidades.nuevoRegistroMapaGenerico(forma.getArticulos(),indicesTemp,"numRegistros","tiporegistro_","MEM");
					forma.getArticulos().put("fechavigencia_"+(Utilidades.convertirAEntero(forma.getArticulos().get("numRegistros")+"")-1), forma.getFechaVigencia()+"");
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
					forma.getAgrupacionServicios().put("fechavigencia_"+(Utilidades.convertirAEntero(forma.getAgrupacionServicios().get("numRegistros")+"")-1), forma.getFechaVigencia()+"");
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
					String[] indicesTemp={"codigo_","codigoexcepcion_","incluye_","cantidad_","tiporegistro_"};
					Utilidades.nuevoRegistroMapaGenerico(forma.getServicios(),indicesTemp,"numRegistros","tiporegistro_","MEM");
					forma.getServicios().put("fechavigencia_"+(Utilidades.convertirAEntero(forma.getServicios().get("numRegistros")+"")-1), forma.getFechaVigencia()+"");

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
					return this.accionDetalleExcepcion(con,forma,mundo,usuario,mapping);
				}
				else if(estado.equals("anteriores"))
				{
					forma.setExcepcionesAnteriores(mundo.obtenerExcepciones(con,usuario.getCodigoInstitucionInt(),forma.getContrato(),false,false));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");				
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
	private void accionGuardar(Connection con, InclusionExclusionConvenioForm forma, InclusionExclusionConvenio mundo, UsuarioBasico usuario)
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
		}
		else
		{
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
	private boolean accionGuardarServicios(Connection con, InclusionExclusionConvenioForm forma, InclusionExclusionConvenio mundo, UsuarioBasico usuario, boolean transaccion)
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
				vo.put("fechavigencia", UtilidadFecha.conversionFormatoFechaABD(forma.getServicios("fechavigencia_"+i)+""));
				transaccion=mundo.modificarServicios(con, vo);
			}
			//insertar
			else if((forma.getServicios("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigoExcepcion", forma.getExcepciones("codigo_"+forma.getIndexSeleccionado())+"");
				vo.put("servicio", forma.getServicios("codigoServicio_"+i));
				vo.put("incluye", forma.getServicios("incluye_"+i));
				vo.put("cantidad", forma.getServicios("cantidad_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("fechavigencia", UtilidadFecha.conversionFormatoFechaABD(forma.getServicios("fechavigencia_"+i)+""));
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
	private boolean accionGuardarAgrupacionServicios(Connection con, InclusionExclusionConvenioForm forma, InclusionExclusionConvenio mundo, UsuarioBasico usuario, boolean transaccion)
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
				vo.put("fechavigencia", UtilidadFecha.conversionFormatoFechaABD(forma.getAgrupacionServicios("fechavigencia_"+i)+""));
				transaccion=mundo.modificarAgrupacionServicios(con, vo);
			}
			//insertar
			else if((forma.getAgrupacionServicios("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigoExcepcion", forma.getExcepciones("codigo_"+forma.getIndexSeleccionado())+"");
				vo.put("tipopos", forma.getAgrupacionServicios("tipopos_"+i));
				vo.put("gruposervicio", forma.getAgrupacionServicios("gruposervicio_"+i));
				vo.put("tiposervicio", forma.getAgrupacionServicios("tiposervicio_"+i));
				vo.put("especialidad", forma.getAgrupacionServicios("especialidad_"+i));
				vo.put("incluye", forma.getAgrupacionServicios("incluye_"+i));
				vo.put("cantidad", forma.getAgrupacionServicios("cantidad_"+i));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("fechavigencia", UtilidadFecha.conversionFormatoFechaABD(forma.getAgrupacionServicios("fechavigencia_"+i)+""));
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
	private boolean accionGuardarArticulos(Connection con, InclusionExclusionConvenioForm forma, InclusionExclusionConvenio mundo, UsuarioBasico usuario, boolean transaccion)
	{
		//eliminar
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
				vo.put("fechavigencia", UtilidadFecha.conversionFormatoFechaABD(forma.getArticulos("fechavigencia_"+i)+""));
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.modificarArticulos(con, vo);
			}
			//insertar
			else if((forma.getArticulos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigoExcepcion", forma.getExcepciones("codigo_"+forma.getIndexSeleccionado())+"");
				vo.put("articulo", forma.getArticulos("codigoArticulo_"+i));
				vo.put("incluye", forma.getArticulos("incluye_"+i));
				vo.put("cantidad", forma.getArticulos("cantidad_"+i));
				vo.put("fechavigencia", UtilidadFecha.conversionFormatoFechaABD(forma.getArticulos("fechavigencia_"+i)+""));
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
	private boolean accionGuardarAgrupacionArticulos(Connection con, InclusionExclusionConvenioForm forma, InclusionExclusionConvenio mundo, UsuarioBasico usuario, boolean transaccion)
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
				vo.put("fechavigencia", UtilidadFecha.conversionFormatoFechaABD(forma.getAgrupacionArticulos("fechavigencia_"+i)+""));
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.modificarAgrupacionArticulos(con, vo);
			}
			//insertar
			else if((forma.getAgrupacionArticulos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigoExcepcion", forma.getExcepciones("codigo_"+forma.getIndexSeleccionado())+"");
				vo.put("clase", forma.getAgrupacionArticulos("clase_"+i));
				vo.put("grupo", forma.getAgrupacionArticulos("grupo_"+i));
				vo.put("subgrupo", forma.getAgrupacionArticulos("subgrupo_"+i));
				vo.put("naturaleza", forma.getAgrupacionArticulos("naturaleza_"+i));
				vo.put("subgrupo", forma.getAgrupacionArticulos("subgrupo_"+i));
				vo.put("naturaleza", forma.getAgrupacionArticulos("naturaleza_"+i));
				vo.put("incluye", forma.getAgrupacionArticulos("incluye_"+i));
				vo.put("cantidad", forma.getAgrupacionArticulos("cantidad_"+i));
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("fechavigencia", UtilidadFecha.conversionFormatoFechaABD(forma.getAgrupacionArticulos("fechavigencia_"+i)+""));
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
	private ActionForward accionDetalleExcepcion(Connection con, InclusionExclusionConvenioForm forma, InclusionExclusionConvenio mundo, UsuarioBasico usuario, ActionMapping mapping)
	{
		forma.resetMapasEliminacion();
		forma.setAgrupacionArticulos(mundo.consultarAgrupacionArticulos(con,forma.getExcepciones("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setArticulos(mundo.consultarArticulos(con,forma.getExcepciones("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setAgrupacionServicios(mundo.consultarAgrupacionServicios(con,forma.getExcepciones("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setServicios(mundo.consultarServicios(con,forma.getExcepciones("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setFechaVigencia(""+forma.getExcepciones("fechavigencia_"+forma.getIndexSeleccionado()));
		forma.setNomCentroCosto(""+forma.getExcepciones("nomcentrocosto_"+forma.getIndexSeleccionado()));
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
	private ActionForward accionDetalleExcepcionAnterior(Connection con, InclusionExclusionConvenioForm forma, InclusionExclusionConvenio mundo, UsuarioBasico usuario, ActionMapping mapping)
	{
		forma.resetMapasEliminacion();
		forma.setAgrupacionArticulos(mundo.consultarAgrupacionArticulos(con,forma.getExcepcionesAnteriores().get("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setArticulos(mundo.consultarArticulos(con,forma.getExcepcionesAnteriores().get("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setAgrupacionServicios(mundo.consultarAgrupacionServicios(con,forma.getExcepcionesAnteriores().get("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setServicios(mundo.consultarServicios(con,forma.getExcepcionesAnteriores().get("codigo_"+forma.getIndexSeleccionado())+""));
		forma.setFechaVigencia(""+forma.getExcepcionesAnteriores().get("fechavigencia_"+forma.getIndexSeleccionado()));
		forma.setNomCentroCosto(""+forma.getExcepcionesAnteriores().get("nomcentrocosto_"+forma.getIndexSeleccionado()));
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
	private ActionForward accionGuardarGeneral(Connection con, InclusionExclusionConvenioForm forma, InclusionExclusionConvenio mundo, UsuarioBasico usuario, ActionMapping mapping)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		if(transaccion)
			transaccion=this.accionGuardarIncluExclu(con,forma,mundo,usuario,transaccion);
		if(transaccion)
			transaccion=this.accionGuardarExcepciones(con,forma,mundo,usuario,transaccion);
		if(transaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}	
		return this.accionCargarInfo(con,forma,mundo,usuario,mapping);
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
	private boolean accionGuardarExcepciones(Connection con, InclusionExclusionConvenioForm forma, InclusionExclusionConvenio mundo, UsuarioBasico usuario, boolean transaccion)
	{
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getExcepcionesEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarExepciones(con,forma.getExcepcionesEliminados().get("codigo_"+i)+""))
			{
				Utilidades.generarLogGenerico(forma.getExcepcionesEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logCoberturaConvenioCodigo,indicesExcepciones);
				transaccion=true;
			}
		}
		
		for(int i=0;i<Integer.parseInt(forma.getExcepciones("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getExcepciones("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma.getExcepciones(),mundo.consultarExcepcionLLave(con, forma.getExcepciones("codigo_"+i)+""),i,usuario,indicesExcepciones))
			{
				HashMap vo=new HashMap();
				vo.put("codigo", forma.getExcepciones("codigo_"+i)+"");
				vo.put("centroCosto", forma.getExcepciones("centrocosto_"+i));
				vo.put("fechavigencia", UtilidadFecha.conversionFormatoFechaABD(forma.getExcepciones("fechavigencia_"+i)+""));
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("institucion", usuario.getCodigoInstitucion());
				
				transaccion=mundo.modificarExcepcion(con, vo);
			}
			//insertar
			else if((forma.getExcepciones("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigo", forma.getExcepciones("codigo_"+i)+"");
				vo.put("codigoContrato", forma.getContrato());
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("centroCosto", forma.getExcepciones("centrocosto_"+i));
				vo.put("fechavigencia", UtilidadFecha.conversionFormatoFechaABD(forma.getExcepciones("fechavigencia_"+i)+""));
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.insertarExcepcion(con, vo);
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
	private boolean accionGuardarIncluExclu(Connection con, InclusionExclusionConvenioForm forma, InclusionExclusionConvenio mundo, UsuarioBasico usuario, boolean transaccion)
	{
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getInclusionesExclusionesEliminados().get("numRegistros")+"");i++)
		{
			if(mundo.eliminarInclusionExclusion(con,forma.getInclusionesExclusionesEliminados().get("codigoincluexclucc_"+i)+"",forma.getContrato(),usuario.getCodigoInstitucionInt()))
			{
				Utilidades.generarLogGenerico(forma.getInclusionesExclusionesEliminados(), new HashMap(), usuario.getLoginUsuario(), true, i,ConstantesBD.logCoberturaConvenioCodigo,indicesInclusionesExclusiones);
				transaccion=true;
			}
		}
		
		for(int i=0;i<Integer.parseInt(forma.getInclusionesExclusiones("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getInclusionesExclusiones("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma.getInclusionesExclusiones(),mundo.consultarIncluExcluLLave(con, forma.getInclusionesExclusiones("codigoincluexclucc_"+i)+"",forma.getContrato(),usuario.getCodigoInstitucionInt()),i,usuario,indicesInclusionesExclusiones))
			{
				HashMap vo=new HashMap();
				vo.put("codigoIncluExcluCC", forma.getInclusionesExclusiones("codigoincluexclucc_"+i));
				vo.put("prioridad", forma.getInclusionesExclusiones("prioridad_"+i));
				vo.put("codigoContrato", forma.getContrato()+"");
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("fechavigencia", UtilidadFecha.conversionFormatoFechaABD(forma.getInclusionesExclusiones("fechavigencia_"+i)+""));
				transaccion=mundo.modificarIncluExclu(con, vo);
			}
			if((forma.getInclusionesExclusiones("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigoIncluExcluCC", forma.getInclusionesExclusiones("codigoincluexclucc_"+i));
				vo.put("prioridad", forma.getInclusionesExclusiones("prioridad_"+i));
				vo.put("codigoContrato", forma.getContrato()+"");
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("fechavigencia", UtilidadFecha.conversionFormatoFechaABD(forma.getInclusionesExclusiones("fechavigencia_"+i)+""));
				transaccion=mundo.insertarInclusionExclusion(con, vo);
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
	private ActionForward accionCargarInfo(Connection con, InclusionExclusionConvenioForm forma, InclusionExclusionConvenio mundo, UsuarioBasico usuario, ActionMapping mapping)
	{
		//incializar la informacion de los select
		forma.setCentrosCosto(Utilidades.obtenerCentrosCosto(con,usuario.getCodigoInstitucionInt(),ConstantesBD.codigoTipoAreaDirecto+ConstantesBD.separadorSplit+ConstantesBD.codigoTipoAreaSubalmacen,false,ConstantesBD.codigoNuncaValido,false));
		forma.setInclusiones(mundo.obtenerDetalleInclusionesExclusiones(con,usuario.getCodigoInstitucionInt()));
		
		forma.setExcepcionesAnteriores(new HashMap());
		forma.getExcepcionesAnteriores().put("numRegistros", "0");

		
		forma.setInclusionesExclusiones(mundo.obtenerInclusionesExclusiones(con,usuario.getCodigoInstitucionInt(),forma.getContrato()));
		forma.setExcepciones(mundo.obtenerExcepciones(con,usuario.getCodigoInstitucionInt(),forma.getContrato(),true,false));
		
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
					Utilidades.generarLogGenerico(mapa, mapaTemp, usuario.getLoginUsuario(), false, pos,ConstantesBD.logCoberturaConvenioCodigo,indices);
					return true;
				}
			}
		}
		return false;
	}
}
