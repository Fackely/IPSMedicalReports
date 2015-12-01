package com.princetonsa.action.historiaClinica;


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
import util.UtilidadBD;
import util.UtilidadSesion;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.historiaClinica.ServiciosSircForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.ServiciosSirc;

public class ServiciosSircAction extends Action {
	
	
	/**
	 * Cadena que contiene los indices del mapa.
	 */
	private static final String indicesMapa="codigo_,institucion_,descripcion_,activo_,tiporegistro_"; 
	
	/**
	 * Cadena que contiene los indices del mapa de Eliminado de Detalle.
	 */
	private static final String indicesMapaServicioDet="servicio_sirc_,institucion_,servicio_,tiporegistro_";
	
	 /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(ServiciosSircAction.class);
	
	
	public ActionForward execute ( ActionMapping mapping, 	
							        ActionForm form, 
							        HttpServletRequest request, 
							        HttpServletResponse response) throws Exception
							        {

		Connection con = null;

		try {
			if(form instanceof ServiciosSircForm)
			{
				con=UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				ServiciosSircForm forma = (ServiciosSircForm) form;

				UsuarioBasico usuario = null;
				usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());

				ServiciosSirc mundo = new ServiciosSirc();
				String estado = forma.getEstado();

				logger.warn("\n\n Valor del Estado -> "+forma.getEstado()+"\n\n");

				if(estado == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}			
				else if (estado.equals("empezar"))
				{
					forma.reset();
					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
					this.accionConsultarServiciosSIRC(con,forma,mundo,usuario);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}	
				else if (estado.equals("consultar"))
				{				
					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
					this.accionConsultarServiciosSIRC(con, forma, mundo, usuario);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");			
				}			
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.closeConnection(con);
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}			
				else if(estado.equals("nuevo"))
				{
					this.accionNuevoServicio(forma);
					UtilidadBD.closeConnection(con);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getServiciosSirc("numRegistros").toString()), response, request, "serviciosSirc.jsp",true);				
				}			
				else if(estado.equals("eliminar"))
				{
					UtilidadBD.closeConnection(con);
					return this.accionEliminarRegistro(forma,request,response,mapping);
				}			
				else if(estado.equals("guardar"))
				{
					//guardamos en BD.
					this.accionGuardarRegistros(con,forma,mundo,usuario);
					//limipiamos el form
					forma.reset();
					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
					this.accionConsultarServiciosSIRC(con, forma, mundo, usuario);

					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");

				}						
				else if(estado.equals("ordenar"))
				{
					this.accionOrdenarMapa(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}

				//********************************************** Servicios SIRC Detalle
				else if(estado.equals("volverServicioSirc"))
				{
					return UtilidadSesion.redireccionar("serviciosSirc.jsp",ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getServiciosSircDetalleMap("numRegistros").toString()), response, request, "serviciosSirc.jsp",false);				
				}
				else if(estado.equals("agregarServicioBusqueda"))
				{
					this.accionNuevoServicioSircDetalle(forma, usuario);
					return UtilidadSesion.redireccionar("ingresarServiciosSircDetalle.jsp",ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getServiciosSirc("numRegistros").toString()), response, request, "ingresarServiciosSircDetalle.jsp",false);				
				}
				else if (estado.equals("consultarServiciosSircDetalle"))
				{				
					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
					this.accionConsultarServiciosSircDetalle(con,forma,mundo,usuario,"detallle");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("ingresarServiciosSircDetalle");
				}		
				else if(estado.equals("destinoServicioSircDetalle"))
				{
					this.accionConsultarServiciosSircDetalle(con,forma,mundo,usuario,"detalle");		
					UtilidadBD.cerrarConexion(con);	
					return UtilidadSesion.redireccionar("ingresarServiciosSircDetalle.jsp",ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getServiciosSircDetalleMap("numRegistros").toString()), response, request, "ingresarServiciosSircDetalle.jsp",false);				
				}			
				else if(estado.equals("guardarServicioSircDetalle"))
				{
					this.accionGuardarRegistrosSircDetalle(con,forma,mundo,usuario);			
					this.accionConsultarServiciosSircDetalle(con, forma, mundo, usuario,"detalle");				
					UtilidadBD.cerrarConexion(con);				
					return UtilidadSesion.redireccionar("ingresarServiciosSircDetalle.jsp",ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getServiciosSircDetalleMap("numRegistros").toString()), response, request, "ingresarServiciosSircDetalle.jsp",false);
				}
				else if(estado.equals("eliminarServSircDetalle"))
				{
					UtilidadBD.closeConnection(con);
					return this.accionEliminarRegistroServiciosSircDet(forma, request, response, mapping);
				}

				else if(estado.equals("volverServicioSircConsulta"))
				{
					return UtilidadSesion.redireccionar("serviciosSirc.jsp",ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getServiciosSircDetalleMap("numRegistros").toString()), response, request, "serviciosSirc.jsp",false);				
				}		

				else if(estado.equals("destinoServicioSircDetalleConsulta"))
				{
					this.accionConsultarServiciosSircDetalle(con,forma,mundo,usuario,"detalle");		
					UtilidadBD.cerrarConexion(con);	
					return UtilidadSesion.redireccionar("consultarServiciosSircDetalle.jsp",ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getServiciosSircDetalleMap("numRegistros").toString()), response, request, "consultarServiciosSircDetalle.jsp",false);				
				}
				//**********************************************************************
				else
				{
					forma.reset();
					logger.warn("Estado no valido dentro del flujo de SERVICIOS SIRC ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}			
			}
			else
			{
				logger.error("El form no es compatible con el form de ServiciosSircForm");
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

	private void accionOrdenarMapa(ServiciosSircForm forma)
	{
		String[] indices=indicesMapa.split(",");
		int numReg=Integer.parseInt(forma.getServiciosSirc("numRegistros")+"");
		forma.setServiciosSirc(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getServiciosSirc(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setServiciosSirc("numRegistros",numReg+"");		
		
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardarRegistros(Connection con, ServiciosSircForm forma, ServiciosSirc mundo, UsuarioBasico usuario) {

		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		HashMap vo=new HashMap();
		
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getServiciosSircEliminado("numRegistros")+"");i++)
		{
			vo.put("servicio_sirc",(forma.getServiciosSircEliminado("codigo_"+i)+""));
			vo.put("institucion",usuario.getCodigoInstitucionInt());	
			
			mundo.eliminarServiciosSircDetalle(con,vo);
			if(!(forma.getServiciosSircEliminado("codigo_"+i) == null) &&  (mundo.eliminarRegistro(con,Utilidades.convertirAEntero((forma.getServiciosSircEliminado("codigo_"+i)+"")),Integer.parseInt((forma.getServiciosSircEliminado("institucion_"+i)+"")))))
				{
					this.generarLog(forma, mundo, usuario, true, i);
					transaccion=true;
				}			
		}
		
		for(int i=0;i<Integer.parseInt(forma.getServiciosSirc("numRegistros")+"");i++)
		{
			//modificar		
			
			vo.put("codigo",forma.getServiciosSirc("codigo_"+i));
			vo.put("institucion",forma.getServiciosSirc("institucion_"+i));
			
			if((forma.getServiciosSirc("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma,mundo,vo,i,usuario))
			{				
				vo.put("codigo",forma.getServiciosSirc("codigo_"+i));
				vo.put("institucion",forma.getServiciosSirc("institucion_"+i));
				vo.put("descripcion",forma.getServiciosSirc("descripcion_"+i));
				vo.put("activo",forma.getServiciosSirc("activo_"+i));			
				transaccion=mundo.modificarServicioSirc(con, vo);
			}
			
			//insertar
			else if((forma.getServiciosSirc("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{			
				vo.put("codigo",forma.getServiciosSirc("codigo_"+i));
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("descripcion",forma.getServiciosSirc("descripcion_"+i));
				vo.put("activo",forma.getServiciosSirc("activo_"+i));				
				transaccion=mundo.insertarServicioSirc(con, vo);
			}			
		}
		
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
	 */
	private void accionGuardarRegistrosSircDetalle(Connection con, ServiciosSircForm forma, ServiciosSirc mundo, UsuarioBasico usuario) {

		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		HashMap vo=new HashMap();		
		
		//eliminar
		
		logger.warn("\n\n valor del mapa eliminacion "+forma.getServiciosSircDetalleEliminadoMap()+"\n\n");
		for(int i=0;i<Integer.parseInt(forma.getServiciosSircDetalleEliminadoMap("numRegistros")+"");i++)			
		{
			vo.put("servicio_sirc",forma.getServiciosSircDetalleEliminadoMap("servicio_sirc_"+i));
			vo.put("institucion",forma.getServiciosSircDetalleEliminadoMap("institucion_"+i));
			vo.put("servicio",forma.getServiciosSircDetalleEliminadoMap("servicio_"+i));			
			
			if(mundo.eliminarServiciosSircDetalle(con,vo))			
			{
				this.generarLogServicioDetalle(forma, mundo, usuario, true, i);
				mundo.CodigosInsertados(forma.getServiciosSircDetalleMap("codServInsert").toString(),2,forma.getServiciosSircDetalleEliminadoMap("servicio_"+i).toString());
				transaccion=true;
			}
		}
		
		for(int i=0;i<Integer.parseInt(forma.getServiciosSircDetalleMap("numRegistros")+"");i++)
		{			
			//insertar
			if((forma.getServiciosSircDetalleMap("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				vo.put("servicio_sirc",forma.getServiciosSircDetalleMap("servicio_sirc_"+i));
				vo.put("institucion",forma.getServiciosSircDetalleMap("institucion_"+i));
				vo.put("servicio",forma.getServiciosSircDetalleMap("servicio_"+i));								
				transaccion=mundo.insertarServiciosSircDetalle(con, vo);
			}
		}
		
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
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param isEliminacion
	 * @param pos
	 */
	private void generarLog(ServiciosSircForm forma, ServiciosSirc mundo, UsuarioBasico usuario, boolean isEliminacion, int pos) {

		String log = "";
		int tipoLog=0;
		String[] indices=indicesMapa.split(",");
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ "+forma.getServiciosSircEliminado(indices[i]+""+pos)+" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ "+mundo.getMapaServiciosSircTemp().get(indices[i]+"0")+" ]";
			}
			log += 		 "\n   ============INFORMACION MODIFICADA=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ "+forma.getServiciosSirc(indices[i]+""+pos)+" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		LogsAxioma.enviarLog(ConstantesBD.logServiciosSircCodigo,log,tipoLog,usuario.getLoginUsuario());
		
		
	}
	
	/**
	 * 
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param isEliminacion
	 * @param pos
	 */
	private void generarLogServicioDetalle(ServiciosSircForm forma, ServiciosSirc mundo, UsuarioBasico usuario, boolean isEliminacion, int pos) {

		String log = "";
		int tipoLog=0;
		String[] indices=indicesMapaServicioDet.split(",");
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO DETALLE ELIMINADO=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ "+forma.getServiciosSircDetalleEliminadoMap(indices[i]+""+pos)+" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}		
		LogsAxioma.enviarLog(ConstantesBD.logServiciosSircCodigo,log,tipoLog,usuario.getLoginUsuario());	
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param HashMap campos
	 * @param pos
	 * @param usuario
	 * @return
	 */
	private boolean existeModificacion(Connection con, ServiciosSircForm forma, ServiciosSirc mundo, HashMap campos, int pos, UsuarioBasico usuario) {
		
		HashMap temp=mundo.consultarServicioSirc(con,campos);
		logger.warn("\n\n valor temp en modificacion "+temp+"\n\n");
		String[] indices=indicesMapa.split(",");	
		
		for(int i=0;i<indices.length;i++)
		{
			if( !(temp.get(indices[i]+"0").toString().equals(forma.getServiciosSirc(indices[i]+""+pos)+"")))
			{
				mundo.setMapaServiciosSircTemp(temp);
				this.generarLog(forma, mundo, usuario, false, pos);
				return true;
			}
		}
		
		return false;		
	}

	/**
	 * 
	 * @param forma
	 * @param request
	 * @param response
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarRegistro(ServiciosSircForm forma, HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) 
	{
		int numRegMapEliminados=Integer.parseInt(forma.getServiciosSircEliminado("numRegistros")+"");
		int ultimaPosMapa=(Integer.parseInt(forma.getServiciosSirc("numRegistros")+"")-1);
		
		
		//poner la informacion en el otro mapa.
		String[] indices=indicesMapa.split(",");
		for(int i=0;i<indices.length;i++)
		{   //solo pasar al mapa los registros que son de BD
			if((forma.getServiciosSirc("tiporegistro_"+forma.getPosEliminar())+"").trim().equalsIgnoreCase("BD"))
			{   ///logear
				forma.setServiciosSircEliminado(indices[i]+""+numRegMapEliminados, forma.getServiciosSirc(indices[i]+""+forma.getPosEliminar()));
			}
		}
		
		//acomodar los registros del mapa en su nueva posicion
		for(int i=forma.getPosEliminar();i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indices.length;j++)
			{	///logear
				forma.setServiciosSirc(indices[j]+""+i,forma.getServiciosSirc(indices[j]+""+(i+1)));
			}
		}
		
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indices.length;j++)
		{
			forma.getServiciosSirc().remove(indices[j]+""+ultimaPosMapa);
		}
		
		//ahora actualizamo el numero de registros en el mapa.
		forma.setServiciosSirc("numRegistros",ultimaPosMapa);
		forma.setServiciosSircEliminado("numRegistros", (numRegMapEliminados+1));
		
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getServiciosSirc("numRegistros").toString()), response, request, "serviciosSirc.jsp",forma.getPosEliminar()==ultimaPosMapa);
	}
	
	/**
	 * 
	 * @param forma
	 * @param request
	 * @param response
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEliminarRegistroServiciosSircDet(ServiciosSircForm forma, HttpServletRequest request, HttpServletResponse response, ActionMapping mapping) 
	{
		ServiciosSirc mundo = new ServiciosSirc();
		int numRegMapEliminados=Integer.parseInt(forma.getServiciosSircDetalleEliminadoMap("numRegistros")+"");
		int ultimaPosMapa=(Integer.parseInt(forma.getServiciosSircDetalleMap("numRegistros")+"")-1);
		
		
		//poner la informacion en el otro mapa.
		String[] indices=indicesMapaServicioDet.split(",");
		
		forma.setServiciosSircDetalleMap("codServInsert",mundo.CodigosInsertados(forma.getServiciosSircDetalleMap("codServInsert").toString(),2,forma.getServiciosSircDetalleMap("servicio_"+forma.getPosEliminar()).toString()));
		
		for(int i=0;i<indices.length;i++)
		{   //solo pasar al mapa los registros que son de BD
			if((forma.getServiciosSircDetalleMap("tiporegistro_"+forma.getPosEliminar())+"").trim().equals("BD"))
			{   ///logear
				forma.setServiciosSircDetalleEliminadoMap(indices[i]+""+numRegMapEliminados, forma.getServiciosSircDetalleMap(indices[i]+""+forma.getPosEliminar()));
			}
		}
		
		if((forma.getServiciosSircDetalleMap("tiporegistro_"+forma.getPosEliminar())+"").trim().equals("BD"))
			forma.setServiciosSircDetalleEliminadoMap("numRegistros", (numRegMapEliminados+1));
		
		//acomodar los registros del mapa en su nueva posicion
		for(int i=forma.getPosEliminar();i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indices.length;j++)
			{	///logear
				forma.setServiciosSircDetalleMap(indices[j]+""+i,forma.getServiciosSircDetalleMap(indices[j]+""+(i+1)));
			}
		}
		
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indices.length;j++)
		{
			forma.getServiciosSircDetalleMap().remove(indices[j]+""+ultimaPosMapa);
		}
		
		//ahora actualizamo el numero de registros en el mapa.
		forma.setServiciosSircDetalleMap("numRegistros",ultimaPosMapa);			
		
		return UtilidadSesion.redireccionar("ingresarServiciosSircDetalle.jsp",forma.getMaxPageItems(),Integer.parseInt(forma.getServiciosSircDetalleMap("numRegistros").toString()), response, request, "ingresarServiciosSircDetalle.jsp",forma.getPosEliminar()==ultimaPosMapa);
	}
	

	/**
	 * 
	 * @param forma
	 */
	private void accionNuevoServicio(ServiciosSircForm forma) 
	{
		int pos = Integer.parseInt(forma.getServiciosSirc("numRegistros")+ "" ) ;		
		forma.setServiciosSirc("codigo_"+pos , "");
		forma.setServiciosSirc("institucion_"+pos ,"");
		forma.setServiciosSirc("descripcion_"+pos,"");
		forma.setServiciosSirc("activo_"+pos ,ConstantesBD.acronimoSi);
		forma.setServiciosSirc("tiporegistro_"+pos, "MEM");
		forma.setServiciosSirc("numRegistros" , (pos +1) + "");	
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionConsultarServiciosSIRC(Connection con, ServiciosSircForm forma, ServiciosSirc mundo, UsuarioBasico usuario) {
		// 
		HashMap vo = new HashMap();
		vo.put("institucion", usuario.getCodigoInstitucion());
		forma.setServiciosSirc( mundo.consultarServicioSirc(con, vo));		
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionConsultarServiciosSircDetalle(Connection con, ServiciosSircForm forma, ServiciosSirc mundo, UsuarioBasico usuario,String estado) 
	{	 
		HashMap vo = new HashMap();		
		vo.put("servicio_sirc",forma.getServiciosSirc("codigo_"+forma.getIndexServSirc()));
		vo.put("institucion", usuario.getCodigoInstitucion());
		forma.setEstado(estado);
		forma.setServiciosSircDetalleMap(mundo.consultarServicioSircDetalle(con, vo));
		forma.setServiciosSircDetalleMap("ubicacionServicioSirc",forma.getServiciosSirc("codigo_"+forma.getIndexServSirc())+" : "+forma.getServiciosSirc("descripcion_"+forma.getIndexServSirc()));
		
	}
	
	/**
	 * Adicciona un Nuevo registro al HashMap de Detalle de Servicio Sirc 
	 * ServiciosSircForm forma
	 * */
	private void accionNuevoServicioSircDetalle(ServiciosSircForm forma, UsuarioBasico usuario)
	{
		ServiciosSirc mundo = new ServiciosSirc();
		int pos = (Integer.parseInt(forma.getServiciosSircDetalleMap("numRegistros").toString()))-1;
		forma.setServiciosSircDetalleMap("servicio_sirc_"+pos,forma.getServiciosSirc("codigo_"+forma.getIndexServSirc()));
		forma.setServiciosSircDetalleMap("institucion_"+pos,usuario.getCodigoInstitucionInt());
		forma.setServiciosSircDetalleMap("servicio_"+pos,forma.getServiciosSircDetalleMap("codigoAxiomaNueva"));
		forma.setServiciosSircDetalleMap("descripcion_"+pos,forma.getServiciosSircDetalleMap("descripcionNueva"));		
		forma.setServiciosSircDetalleMap("tiporegistro_"+pos,"MEM");
		forma.setServiciosSircDetalleMap("codServInsert",mundo.CodigosInsertados(forma.getServiciosSircDetalleMap("codServInsert").toString(),1,forma.getServiciosSircDetalleMap("codigoAxiomaNueva").toString()));
	}
}