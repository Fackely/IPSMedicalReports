package com.princetonsa.action.agendaProcedimiento;

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
import org.apache.struts.action.ActionMessage;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadSesion;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.agendaProcedimiento.UnidadProcedimientoForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.agendaProcedimiento.UnidadProcedimiento;


public class UnidadProcedimientoAction extends Action
{	
	
	/**
	 * Objeto para manejar el log de la clase
	 * */
	Logger logger = Logger.getLogger(UnidadProcedimientoAction.class);
	
	public ActionForward execute(ActionMapping mapping,
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response) throws Exception 
	{
		
		Connection con = null;
		
		try{
		if(response==null);

		if(form instanceof UnidadProcedimientoForm)		 
		{
			
			con = UtilidadBD.abrirConexion();
			 
			if(con == null)
			{
				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
				return mapping.findForward("paginaError");				 
			 }
			 
			 UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
			 UnidadProcedimientoForm forma = (UnidadProcedimientoForm)form;
			 String estado = forma.getEstado();
			 
			 logger.warn("n\n\nEl estado en CoberturaAction es------->"+estado+"\n");			 
			 
			 if (estado == null)
			 {				 
				 forma.reset();
				 logger.warn("Estado no Valido dentro del flujo de Unidad de Procedimiento (null)");
				 request.setAttribute("CodigDescripcionError", "errors.estadoInvalido");
			  	 UtilidadBD.cerrarConexion(con);
			  	 return mapping.findForward("paginaError");			  
			 }
 			 
			 else if(estado.equals("empezar"))
			 {
				 return this.accionConsultarUnidadProc(con, forma,mapping,usuario,"consultar");			 				 				 		  
			 }
			 
			 
			 else if(estado.equals("nuevoUnidadProc"))
			 {
				 UtilidadBD.closeConnection(con);
				 this.accionNuevoUnidadProc(forma);	
				 return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getUnidadProcMap("numRegistros").toString()), response, request, "unidadProcedimiento.jsp",true);
			 }
			 
			 			 
			 else if(estado.equals("eliminarUnidadProc"))
			 {	 				 
				 return this.accionEliminarUnidadProc(forma,mapping,request,response,usuario.getCodigoInstitucionInt());				 
			 }
			 
			 
			 else if(estado.equals("guardarUnidadProc"))
			 {
				 return this.accionGuardarUnidadProc(con,forma,mapping,usuario);
			 }
			 
			 else if(estado.equals("ordenarUnidadProc"))
			 {				 
				 this.accionOrdenarMapa(forma);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("principal");					
			}	
			 
					 
			 //************************************************************
			 
			else if(estado.equals("destinoServicioUndProc"))
			{
				this.accionConsultarServicioUndProc(con, forma,mapping,usuario,"principal");	
				forma.setWhoConsultaServicios("padre"); //indica a quien pertenece el pop up de busqueda de servicio
				return UtilidadSesion.redireccionar("servicioUndProc.jsp",ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getUnidadProcMap("numRegistros").toString()), response, request, "servicioUndProc.jsp",false);		    
			}			
			 
			 else if(estado.equals("agregarServicioBusqueda"))
			 {
				 UtilidadBD.closeConnection(con);				 
				 if(forma.getWhoConsultaServicios().equals("padre"))
				 {
					 this.accionNuevoServicioUndProc(forma);				
					 return UtilidadSesion.redireccionar("servicioUndProc.jsp",ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getServicioUndProcMap("numRegistros").toString()), response, request, "servicioUndProc.jsp",true);					 
				 }
				 else if(forma.getWhoConsultaServicios().equals("hijo"))
				 {					 
					 this.accionNuevoServicioDetalle(forma);
					 return mapping.findForward("principalServicioDet");					 					 
				 }							
			 }				
			 
			 else if(estado.equals("volverUndProc"))
			 {
				 return UtilidadSesion.redireccionar("unidadProcedimiento.jsp",ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getUnidadProcMap("numRegistros").toString()), response, request, "unidadProcedimiento.jsp",false);
			 }
			 
			 else if(estado.equals("ordenarServicioUndProc"))
			 {
				 this.accionOrdenarMapaServicio(forma);
				 UtilidadBD.closeConnection(con);
				 return mapping.findForward("principalServicio");				 
			 }
			 
			 else if(estado.equals("guardarServicioUndProc"))				 
			 {				
				 return this.accionGuardarServUndProc(con,forma,mapping,request,usuario);				 
			 }
			 
			 else if(estado.equals("eliminarServicioUndProc"))
			 {
				 return this.accionEliminarServUndProc(forma,mapping,request,response,usuario.getCodigoInstitucionInt());		 				 
			 }			 
			 //************************************************************
			 
			 else if(estado.equals("guardarDetalle"))
			 {
				 return this.accionGuardarDetalle(con,forma,mapping,usuario);			 
			 }
			 
			 else if(estado.equals("destinoSerDetalleUndProc"))
			 {
				this.accionConsultarServicioDetalle(con,forma,mapping,usuario,"principalServicioDet");
				forma.setWhoConsultaServicios("hijo"); //indica a quien pertenece el pop up de busqueda de servicio
				return UtilidadSesion.redireccionar("servicioDetUndProc.jsp",ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getServicioDetalleMap("numRegistros").toString()), response, request, "servicioDetUndProc.jsp",false);		    		  
			 }			 
			 
			 else if(estado.equals("volverServUndProc"))
			 {
				 forma.setWhoConsultaServicios("padre"); //indica a quien pertenece el pop up de busqueda de servicio
				 return UtilidadSesion.redireccionar("servicioUndProc.jsp",ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getServicioUndProcMap("numRegistros").toString()), response, request, "servicioUndProc.jsp",false);		 				 
			 }
			 
			 else if(estado.equals("agregarCondicionBusqueda"))
			 {
				 this.accionNuevoExamenDetalle(forma);
				 return mapping.findForward("principalServicioDet");				 
			 }
			 
			 else if(estado.equals("agregarArticuloBusqueda"))
			 {
				 this.accionNuevoArticuloDetalle(forma);
				 return mapping.findForward("principalServicioDet");
			 }
			 
			 else if(estado.equals("eliminarServicioDetalle"))
			 {
				 return this.accionEliminarDetalle(forma,mapping,request,response,usuario.getCodigoInstitucionInt(),"servicio"); 				 
			 }	 
			 
			 else if(estado.equals("eliminarExamenDetalle"))
			 {
				 return this.accionEliminarDetalle(forma,mapping,request,response,usuario.getCodigoInstitucionInt(),"examen"); 				 
			 }
			 
			 else if(estado.equals("eliminarArticuloDetalle"))
			 {
				 return this.accionEliminarDetalle(forma,mapping,request,response,usuario.getCodigoInstitucionInt(),"articulo"); 				 
			 }
			 
			 //************************************************************
			 
				
			else if (estado.equals("redireccion"))
			{
				UtilidadBD.closeConnection(con);
				response.sendRedirect(forma.getLinkSiguiente());
				return null;
			}
		} 
		
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}	
	
	
	//***************************************Fin del Action Forward********************************************
	/**
	 * 
	 * */
	public ActionForward accionConsultarUnidadProc(Connection con,UnidadProcedimientoForm forma,ActionMapping mapping, UsuarioBasico usuario,String estado)
	{
		forma.reset();
		forma.setUnidadProcEliminadoMap("numRegistros","0");
		forma.setUnidadProcMap("numRegistros","0");		
		forma.setUnidadProcMap(UnidadProcedimiento.consultarUnidadProcBasica(con,ConstantesBD.codigoNuncaValido,usuario.getCodigoInstitucionInt()));
		forma.setEstado(estado);		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");		
	}
	
	//*************************************************************
	
	/**
	 * Inserta un nuevo registro vacio en el mapa
	 * */
	public void accionNuevoUnidadProc(UnidadProcedimientoForm forma)
	{
		int pos = Integer.parseInt(forma.getUnidadProcMap("numRegistros").toString());		
		forma.setUnidadProcMap("descripcion_"+pos,"");
		forma.setUnidadProcMap("especialidad_"+pos,"");
		forma.setUnidadProcMap("activo_"+pos,ConstantesBD.acronimoSi);
		forma.setUnidadProcMap("estabd_"+pos,ConstantesBD.acronimoNo);
		forma.setUnidadProcMap("numRegistros",(pos+1)+"");
	}

	//*************************************************************
	
	/**
	 * Unicializa un objeto Unidad de Procedimiento
	 * @param forma
	 * @param indice
	 * */
	private UnidadProcedimiento llenarCrearMundoUndProc(UnidadProcedimientoForm forma, int indice, UsuarioBasico usuario)
	{
		UnidadProcedimiento mundo = new UnidadProcedimiento();
		if(forma.getUnidadProcMap().containsKey("codigo_"+indice))
		 mundo.setCodigoUndProce(Integer.parseInt(forma.getUnidadProcMap("codigo_"+indice).toString()));
		
		mundo.setInstitucion(usuario.getCodigoInstitucionInt());
		mundo.setCodigoEspeciali(Integer.parseInt(forma.getUnidadProcMap("especialidad_"+indice).toString()));
		mundo.setDescripUndProce(forma.getUnidadProcMap("descripcion_"+indice).toString());
		mundo.setActivoUndProce(forma.getUnidadProcMap("activo_"+indice).toString());
		return mundo;				
	}
	
	
	//*************************************************************
	
	/**
	 * @param con
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * */
	private ActionForward accionResumen(Connection con, ActionMapping mapping, UnidadProcedimientoForm forma, String estado)
	{
		forma.reset();
		forma.setUnidadProcMap("numRegistros","0");
		forma.setUnidadProcEliminadoMap("numRegistros","0")	;
		
		forma.setServicioUndProcMap("numRegistro","0");
		forma.setServicioEliminadoUndProcMap("nunRegistro", "0");
		forma.setEstado(estado);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");		
	}
	
	//*************************************************************
	
	/**
	 * Ordena el Mapa Unidad de Procedimiento
	 * @param forma
	 * */
	private void accionOrdenarMapa(UnidadProcedimientoForm forma)
	{
		String[] indices = (String[])forma.getUnidadProcMap("INDICES_MAPA");
		int numReg = Integer.parseInt(forma.getUnidadProcMap("numRegistros")+"");
		forma.setUnidadProcMap(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getUnidadProcMap(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setUnidadProcMap("numRegistros",numReg+"");
		forma.setUnidadProcMap("INDICES_MAPA",indices);		
	}
	
	//*************************************************************
	
	
	/**
	 * Ordena el Mapa de Servicios por Unidad de Procedimiento
	 * @param forma
	 * */
	private void accionOrdenarMapaServicio(UnidadProcedimientoForm forma)
	{
		String[] indices = (String[])forma.getServicioUndProcMap("INDICES_MAPA");
		int numReg = Integer.parseInt(forma.getServicioUndProcMap("numRegistros")+"");
		forma.setServicioUndProcMap(Listado.ordenarMapa(indices, forma.getPatronOrdenarServUndProc(), forma.getUltimoPatronServUndProc(), forma.getServicioUndProcMap(), numReg));
		forma.setUltimoPatronServUndProc(forma.getPatronOrdenarServUndProc());
		forma.setServicioUndProcMap("numRegistros",numReg+"");
		forma.setServicioUndProcMap("INDICES_MAPA",indices);		
	}
	
	//*************************************************************
	
	/**
	 * elimina un registro del formulario de Unidad Procedimiento 
	 * */
	public ActionForward accionEliminarUnidadProc(UnidadProcedimientoForm forma, ActionMapping mapping, HttpServletRequest request, HttpServletResponse response, int codigoInstitucion)
	{
		int numRegMapEliminados=Integer.parseInt(forma.getUnidadProcEliminadoMap("numRegistros")+"");
		
		int ultimaPosMapa=(Integer.parseInt(forma.getUnidadProcMap("numRegistros")+"")-1);
		
		//poner la informacion en el otro mapa.
		String[] indices= (String[])forma.getUnidadProcMap("INDICES_MAPA");		
				
		for(int i=0;i<indices.length;i++)
		{ 
			//solo pasar al mapa los registros que son de BD
			if((forma.getUnidadProcMap("estabd_"+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
			{
				forma.setUnidadProcEliminadoMap(indices[i]+""+numRegMapEliminados, forma.getUnidadProcMap(indices[i]+""+forma.getIndexEliminado()));
			}
		}		
		
		//aumento el numRegistros de registros eliminados de la base de datos
		if((forma.getUnidadProcMap("estabd_"+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
		{			
			forma.setUnidadProcEliminadoMap("numRegistros", (numRegMapEliminados+1));
		}
		
		//acomodar los registros del mapa en su nueva posicion
		for(int i=forma.getIndexEliminado();i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indices.length;j++)
			{
				forma.setUnidadProcMap(indices[j]+""+i,forma.getUnidadProcMap(indices[j]+""+(i+1)));
			}
		}
		
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indices.length;j++)
		{
			forma.getUnidadProcMap().remove(indices[j]+""+ultimaPosMapa);
		}
		
		//ahora actualizamos el numero de registros en el mapa.
		forma.setUnidadProcMap("numRegistros",ultimaPosMapa);		
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(codigoInstitucion),Integer.parseInt(forma.getUnidadProcMap("numRegistros").toString()), response, request, "unidadProcedimiento.jsp",forma.getIndexEliminado()==ultimaPosMapa);
	}
	
	
	//*************************************************************
	
	
	/**
	 * Guarda un Registro de Unidad de Procedimiento
	 * */
	public ActionForward accionGuardarUnidadProc(Connection con, UnidadProcedimientoForm forma, ActionMapping mapping,UsuarioBasico usuario)
	{
		boolean transacction = UtilidadBD.iniciarTransaccion(con);
		String estado="cosultar";
		UnidadProcedimiento mundo = new UnidadProcedimiento();
		
		
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getUnidadProcEliminadoMap("numRegistros")+"");i++)
		{
			if(mundo.consultaDetalleCuantos(con, Integer.parseInt((forma.getUnidadProcEliminadoMap("codigo_"+i)+"").toString().trim()),ConstantesBD.codigoNuncaValido)>0)			
				transacction = mundo.eliminarServiciosDetTodoUndProc(con,Integer.parseInt((forma.getUnidadProcEliminadoMap("codigo_"+i)+"").toString().trim()),ConstantesBD.codigoNuncaValido,ConstantesBD.codigoNuncaValido);
			
			if(mundo.consultaServicioUndProcCuantos(con,Integer.parseInt((forma.getUnidadProcEliminadoMap("codigo_"+i)+"").toString().trim()))>0)
				transacction = mundo.eliminarServiciosTodoUndProc(con,Integer.parseInt((forma.getUnidadProcEliminadoMap("codigo_"+i)+"").toString().trim()));
				
			if(mundo.eliminarUnidadProcedimiento(con,Integer.parseInt((forma.getUnidadProcEliminadoMap("codigo_"+i)+"").toString().trim()),usuario.getCodigoInstitucionInt()))
				{
					estado="operacionTrue";
					this.generarlogUndProc(forma, new HashMap(), usuario, true ,i );					
					transacction=true;						
				}						
		}		
		
		for(int i=0;i<Integer.parseInt(forma.getUnidadProcMap("numRegistros")+"");i++)
		{			
			//modificar			
			if((forma.getUnidadProcMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoSi)&&
					this.existeModificacion(con,forma,mundo,usuario,Integer.parseInt(forma.getUnidadProcMap("codigo_"+i).toString()),usuario.getCodigoInstitucionInt(),i))
			{				
				transacction = mundo.modificarUnidadProcedimiento(con,llenarCrearMundoUndProc(forma,i,usuario));				
				estado="operacionTrue";
			}
			
			//insertar
			else if((forma.getUnidadProcMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoNo))
			{
				transacction = mundo.insertarUnidadProcedimiento(con,llenarCrearMundoUndProc(forma,i,usuario));				
				estado="operacionTrue";
			}			
		}
		
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(con);			
			logger.info("----->INSERTO 100% COBERTURAS");
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}
		
		return this.accionConsultarUnidadProc(con, forma, mapping, usuario, estado);	 				
	}
	
	//*************************************************************
	
	/**
	 * verifica si el registro se ha modificado
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param String codigoUnidadProcedimiento
	 * @param int institucion
	 * @param int pos
	 * */
	private boolean existeModificacion(Connection con,UnidadProcedimientoForm forma,UnidadProcedimiento mundo, UsuarioBasico usuario, int codigoUndProce, int institucion, int pos)
	{
		HashMap temp = UnidadProcedimiento.consultarUnidadProcBasica(con, codigoUndProce, institucion);
		String[] indices = (String[])forma.getUnidadProcMap("INDICES_MAPA");		
				
		for(int i=0;i<indices.length;i++)
		{		
			if(temp.containsKey(indices[i]+"0")&&forma.getUnidadProcMap().containsKey(indices[i]+pos))
			{				
				if(!((temp.get(indices[i]+"0")+"").trim().equals((forma.getUnidadProcMap(indices[i]+pos)).toString().trim())))
				{
					this.generarlogUndProc(forma, temp, usuario, false ,pos );										
					return true;
				}				
			}
		}		
		return false;		
	}
	
	//*************************************************************
	
	/**
	 * Consulta los Servicios por Unidad de Procedimiento
	 * @param Connection con
	 * @param UnidadProcedimientoForm forma
	 * @param ActionMapping mapping
	 * @param UsuarioBasico usuario
	 * */
	public void accionConsultarServicioUndProc(Connection con,UnidadProcedimientoForm forma,ActionMapping mapping, UsuarioBasico usuario, String estado)
	{			
		forma.setServicioUndProcMap("numRegistros","0");
		forma.setServicioEliminadoUndProcMap("numRegistros", "0");		
		forma.setServicioUndProcMap(UnidadProcedimiento.consultarServicioUnidadProcBasica(con,ConstantesBD.codigoNuncaValido,forma.getIndexUndProc()));		
		forma.setServicioUndProcMap("ubicacionUndProc",forma.getUnidadProcMap("codigo_"+forma.getUbicacionConfig()).toString()+" : "+forma.getUnidadProcMap("descripcion_"+forma.getUbicacionConfig()).toString());	
		forma.setEstado(estado);		
		UtilidadBD.closeConnection(con);				
	} 	
	//*************************************************************
	
	/**
	 * pendiente Mundo
	 * */
	
	/**
	 * Ingresa un nuevo registro al formulario
	 * @param UnidadProcedimineto forma 
	 * */
	public void accionNuevoServicioUndProc(UnidadProcedimientoForm forma)
	{
		UnidadProcedimiento mundo = new UnidadProcedimiento();
		int pos = Integer.parseInt(forma.getServicioUndProcMap("numRegistros").toString());		
		forma.setServicioUndProcMap("codigo_"+(pos-1),"");		
		forma.setServicioUndProcMap("codigoundproce_"+(pos-1),forma.getIndexUndProc());
		forma.setServicioUndProcMap("codigoservicio_"+(pos-1),forma.getServicioUndProcMap("codigoAxiomaNueva").toString());						 
		
		forma.setServicioUndProcMap("codServInsert",mundo.CodigosInsertados(forma.getServicioUndProcMap("codServInsert").toString(),1,forma.getServicioUndProcMap("codigoAxiomaNueva").toString()));
		forma.setServicioUndProcMap("descripcion_"+(pos-1),forma.getServicioUndProcMap("descripcionNueva").toString());		
		forma.setServicioUndProcMap("tiempo_"+(pos-1),"");
		forma.setServicioUndProcMap("estabd_"+(pos-1),ConstantesBD.acronimoNo);	
	}
	//*************************************************************
	
	/**
	 * Guarda un Registro en Servicios por Unidad de Procedimiento
	 * */				    	
	public ActionForward accionGuardarServUndProc(Connection con,UnidadProcedimientoForm forma, ActionMapping mapping, HttpServletRequest request,UsuarioBasico usuario)
	{
		ActionErrors errores = new ActionErrors();
		boolean transacction = UtilidadBD.iniciarTransaccion(con);
		String estado="cosultar";
		UnidadProcedimiento mundo = new UnidadProcedimiento();			
		 		 		 
		 int numReg = Integer.parseInt(forma.getServicioUndProcMap("numRegistros").toString());
		 for(int i=0; i<numReg; i++)
		 {
			if(forma.getServicioUndProcMap("hora_"+i).toString().trim().equals("00") && forma.getServicioUndProcMap("minuto_"+i).toString().trim().equals("00"))					
				errores.add("descripcion",new ActionMessage("errors.invalid","El Tiempo Debe Ser Superior a 00:00. Canmpo "+(i+1)));					
		 }
		 
		 if(!errores.isEmpty())
		 {
		  saveErrors(request,errores);
		  return mapping.findForward("principalServicio");
		 } 
		 
		
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getServicioEliminadoUndProcMap("numRegistros")+"");i++)
		{
			if(mundo.consultaDetalleCuantos(con,Integer.parseInt((forma.getServicioEliminadoUndProcMap("codigoundproce_"+i)+"").toString().trim()),Integer.parseInt((forma.getServicioEliminadoUndProcMap("codigo_"+i)+"").toString().trim()))>0)			
				transacction = mundo.eliminarServiciosDetTodoUndProc(con,Integer.parseInt((forma.getServicioEliminadoUndProcMap("codigoundproce_"+i)+"").toString().trim()),Integer.parseInt((forma.getServicioEliminadoUndProcMap("codigo_"+i)+"").toString().trim()),ConstantesBD.codigoNuncaValido);

			if(mundo.eliminarServiciosUnidadProcedimiento(con,Integer.parseInt((forma.getServicioEliminadoUndProcMap("codigo_"+i)+"").toString().trim()),Integer.parseInt((forma.getServicioEliminadoUndProcMap("codigoundproce_"+i)+"").toString().trim())))			
			{				
				estado="operacionTrue";
				this.generarlogServUndProc(forma, new HashMap(), usuario, true ,i );				
				transacction=true;						 
			}				
		}	
		
		for(int i=0;i<Integer.parseInt(forma.getServicioUndProcMap("numRegistros")+"");i++)
		{			
			//modificar			
			if((forma.getServicioUndProcMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoSi)&&
					this.existeModificacionServUndProc(con,forma,mundo,usuario,Integer.parseInt(forma.getServicioUndProcMap("codigo_"+i).toString()),Integer.parseInt(forma.getServicioUndProcMap("codigoundproce_"+i).toString()),i))
			{				
				transacction = mundo.modificarServiciosUnidadProcedimiento(con,llenarCrearMundoServUndProc(forma,i));								
				estado="operacionTrue";
			}
			
			//insertar
			else if((forma.getServicioUndProcMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoNo))
			{				
				transacction = mundo.insertarServiciosUnidadProcedimiento(con,llenarCrearMundoServUndProc(forma,i));								
				estado="operacionTrue";
			}			
		}
		
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(con);			
			logger.info("----->INSERTO 100% COBERTURAS");
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}
		
		this.accionConsultarServicioUndProc(con,forma,mapping,usuario,estado);
		return mapping.findForward("principalServicio");	
	}
	
	//*************************************************************
	
	public UnidadProcedimiento llenarCrearMundoServUndProc(UnidadProcedimientoForm forma,int indice)
	{
		UnidadProcedimiento mundo = new UnidadProcedimiento();	
		
		if(forma.getServicioUndProcMap("codigo_"+indice).toString().equals(""))
			mundo.setCodigoConsecutivoServiUndproce(ConstantesBD.codigoNuncaValido);
		else
			mundo.setCodigoConsecutivoServiUndproce(Integer.parseInt(forma.getServicioUndProcMap("codigo_"+indice).toString()));
		
		mundo.setCodigoUndProce(Integer.parseInt(forma.getServicioUndProcMap("codigoundproce_"+indice).toString()));
		mundo.setCodigoServicioUndProce(Integer.parseInt(forma.getServicioUndProcMap("codigoservicio_"+indice).toString()));
		mundo.setTiempoServi(forma.getServicioUndProcMap("hora_"+indice).toString()+":"+forma.getServicioUndProcMap("minuto_"+indice).toString());	
		return mundo;
	}
	
	//*************************************************************
	
	/**
	 * elimina un registro del formulario de Unidad Procedimiento 
	 * */
	public ActionForward accionEliminarServUndProc(UnidadProcedimientoForm forma, ActionMapping mapping, HttpServletRequest request, HttpServletResponse response, int codigoInstitucion)
	{
		
		UnidadProcedimiento mundo = new UnidadProcedimiento();
		
		//numero de regitros eliminados hasta el momento
		int numRegMapEliminados=Integer.parseInt(forma.getServicioEliminadoUndProcMap("numRegistros")+"");
		
		//numero de registros de servicios
		int ultimaPosMapa=(Integer.parseInt(forma.getServicioUndProcMap("numRegistros")+"")-1);
		
		//poner la informacion en el otro mapa.
		String[] indices= (String[])forma.getServicioUndProcMap("INDICES_MAPA");
		
		//elimina el codigo de la cadena de consulta general de servicios
		forma.setServicioUndProcMap("codServInsert",mundo.CodigosInsertados(forma.getServicioUndProcMap("codServInsert").toString(),2,forma.getServicioUndProcMap("codigoservicio_"+forma.getIndexEliminado()).toString()));
				
		for(int i=0;i<indices.length;i++)
		{ 
			//guarda los registros eliminados que son de la base de datos en el mapa de eliminados
			if((forma.getServicioUndProcMap("estabd_"+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
			{
				forma.setServicioEliminadoUndProcMap(indices[i]+""+numRegMapEliminados, forma.getServicioUndProcMap(indices[i]+""+forma.getIndexEliminado()));
			}
		}		
		
		//aumento el numRegistros de registros eliminados de la base de datos
		if((forma.getServicioUndProcMap("estabd_"+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
		{			
			forma.setServicioEliminadoUndProcMap("numRegistros", (numRegMapEliminados+1));
		}
		
		//acomodar los registros del mapa en su nueva posicion
		for(int i=forma.getIndexEliminado();i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indices.length;j++)
			{
				forma.setServicioUndProcMap(indices[j]+""+i,forma.getServicioUndProcMap(indices[j]+""+(i+1)));
			}
		}
		
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indices.length;j++)
		{
			forma.getServicioUndProcMap().remove(indices[j]+""+ultimaPosMapa);
		}
		
		//ahora actualizamos el numero de registros en el mapa.
		forma.setServicioUndProcMap("numRegistros",ultimaPosMapa);		
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(codigoInstitucion),Integer.parseInt(forma.getServicioUndProcMap("numRegistros").toString()), response, request, "servicioUndProc.jsp",forma.getIndexEliminado()==ultimaPosMapa);
	}
	//*************************************************************
	
	/**
	 * verifica si el registro se ha modificado
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param int pos 
	 * @param int codigoServiUndProce 
	 * @param int codigoUndProce
	 * */
	private boolean existeModificacionServUndProc(Connection con,UnidadProcedimientoForm forma,UnidadProcedimiento mundo,UsuarioBasico usuario, int codigoServiUndProce, int codigoUndProce, int pos)
	{
		HashMap temp = UnidadProcedimiento.consultarServicioUnidadProcBasica(con, codigoServiUndProce, codigoUndProce);
		String[] indices = (String[])forma.getServicioUndProcMap("INDICES_MAPA");		
				
		for(int i=0;i<indices.length;i++)
		{		
			if(temp.containsKey(indices[i]+"0")&&forma.getServicioUndProcMap().containsKey(indices[i]+pos))
			{				
				if(!((temp.get(indices[i]+"0")+"").trim().equals((forma.getServicioUndProcMap(indices[i]+pos)).toString().trim())))
				{					
					this.generarlogServUndProc(forma, temp, usuario, false ,pos );					
					return true;
				}				
			}
		}		
		return false;		
	}
	
	//*************************************************************
	
	/**
	 * Consulta los Servicios por Unidad de Procedimiento
	 * @param Connection con
	 * @param UnidadProcedimientoForm forma
	 * @param ActionMapping mapping
	 * @param usuario
	 * @param estado	  
	 * */
	private void accionConsultarServicioDetalle(Connection con, UnidadProcedimientoForm forma, ActionMapping mapping, UsuarioBasico usuario,String estado)		
	{
		UnidadProcedimiento mundo = new UnidadProcedimiento();
		forma.setServicioDetalleMap("numRegistros",0);
		forma.setExamenDetalleMap("numRegistros",0);
		forma.setArticulosDetalleMap("numRegistros",0);
		forma.setDetalleEliminadoMap("numRegistros",0);	
		
		forma.setServicioDetalleMap(UnidadProcedimiento.consultarServicioDetalles(con,ConstantesBD.codigoNuncaValido,forma.getIndexServUndProcConsecutivo(),"servicio"));
		forma.setExamenDetalleMap(UnidadProcedimiento.consultarServicioDetalles(con,ConstantesBD.codigoNuncaValido,forma.getIndexServUndProcConsecutivo(),"examen"));		
		forma.setArticulosDetalleMap(UnidadProcedimiento.consultarServicioDetalles(con,ConstantesBD.codigoNuncaValido,forma.getIndexServUndProcConsecutivo(),"articulo"));
		forma.setServicioDetalleMap("codigoservicioInsert",mundo.CodigosInsertados(forma.getServicioDetalleMap("codigoservicioInsert").toString(),1,forma.getIndexServUndProc()+""));
		forma.setServicioDetalleMap("ubicacionUndProc",forma.getServicioUndProcMap("ubicacionUndProc"));
		forma.setServicioDetalleMap("ubicacionServ",forma.getServicioUndProcMap("descripcion_"+forma.getUbicacionConfig()).toString());
		forma.setEstado(estado);
		UtilidadBD.closeConnection(con);
	}
	
	//*************************************************************
	
	/**
	 * Adicciona un nuevo registro de tipo servicio detalle
	 * @param UnidadProcedimiento form
	 * */
	private void accionNuevoServicioDetalle(UnidadProcedimientoForm forma)
	{
		UnidadProcedimiento mundo = new UnidadProcedimiento();
		int pos = Integer.parseInt(forma.getServicioDetalleMap("numRegistros").toString())-1;		
		forma.setServicioDetalleMap("codigoserviundproce_"+pos,forma.getIndexServUndProc());
		forma.setServicioDetalleMap("codigo_"+pos,"");
		
		forma.setServicioDetalleMap("codigoservicioInsert",mundo.CodigosInsertados(forma.getServicioDetalleMap("codigoservicioInsert").toString(),1,forma.getServicioDetalleMap("codigoAxiomaNueva").toString()));
		
		forma.setServicioDetalleMap("codigoservicio_"+pos,forma.getServicioDetalleMap("codigoAxiomaNueva").toString());
		forma.setServicioDetalleMap("descripcion_"+pos,forma.getServicioDetalleMap("descripcionNueva").toString());		
		forma.setServicioDetalleMap("estabd_"+pos,ConstantesBD.acronimoNo);		
	}
	
	//*************************************************************
	
	/**
	 * Adicciona un nuevo registro de tipo Condicion Toma de Examen detalle
	 * @param UnidadProcedimiento form
	 * */
	private void accionNuevoExamenDetalle(UnidadProcedimientoForm forma)
	{
		UnidadProcedimiento mundo = new UnidadProcedimiento();
		int pos = Integer.parseInt(forma.getExamenDetalleMap("numRegistros").toString())-1;		
		forma.setExamenDetalleMap("codigoserviundproce_"+pos,forma.getIndexServUndProc());
		forma.setExamenDetalleMap("codigo_"+pos,"");
		
		forma.setExamenDetalleMap("codigoexamenInsert",mundo.CodigosInsertados(forma.getExamenDetalleMap("codigoexamenInsert").toString(),1,forma.getExamenDetalleMap("codigoExamenNuevo").toString()));
		
		forma.setExamenDetalleMap("codigoexamen_"+pos,forma.getExamenDetalleMap("codigoExamenNuevo").toString());
		forma.setExamenDetalleMap("descripcion_"+pos,forma.getExamenDetalleMap("descripcionExamenNuevo").toString());		
		forma.setExamenDetalleMap("estabd_"+pos,ConstantesBD.acronimoNo);	
	}
	
	//*************************************************************
	/**
	 * elimina un registro del formulario de Unidad Procedimiento 
	 * */
	public ActionForward accionEliminarDetalle(UnidadProcedimientoForm forma, ActionMapping mapping, HttpServletRequest request, HttpServletResponse response, int codigoInstitucion, String fuente)
	{
		
		UnidadProcedimiento mundo = new UnidadProcedimiento();
		
		//numero de regitros eliminados hasta el momento
		int numRegMapEliminados=Integer.parseInt(forma.getDetalleEliminadoMap("numRegistros")+"");
		
		//numero de registros de Detalle (Servicio, Examen, Articulo)
		int ultimaPosMapa=0;
		
		//almacena los indices del HashMap
		String[] indices={""};
		
		//HashMap temporal
		HashMap universal = new HashMap();
		
		if(fuente.equals("servicio"))
		{
			ultimaPosMapa=(Integer.parseInt(forma.getServicioDetalleMap("numRegistros")+"")-1);
			//elimino el codigo insertado de la lista de codigos insertados de la busqueda
			forma.setServicioDetalleMap("codigoservicioInsert",mundo.CodigosInsertados(forma.getServicioDetalleMap("codigoservicioInsert").toString(),2,forma.getServicioDetalleMap("codigoservicio_"+forma.getIndexEliminado()).toString()));	
			indices= (String[])forma.getServicioDetalleMap("INDICES_MAPA");			
			universal = forma.getServicioDetalleMap();			
		}	
		
		else if (fuente.equals("examen"))
		{
			ultimaPosMapa=(Integer.parseInt(forma.getExamenDetalleMap("numRegistros")+"")-1);
			//elimino el codigo insertado de la lista de codigos insertados de la busqueda
			forma.setExamenDetalleMap("codigoexamenInsert",mundo.CodigosInsertados(forma.getExamenDetalleMap("codigoexamenInsert").toString(),2,forma.getExamenDetalleMap("codigoexamen_"+forma.getIndexEliminado()).toString()));
			indices= (String[])forma.getExamenDetalleMap("INDICES_MAPA");
			universal = forma.getExamenDetalleMap();
		}
		
		else if (fuente.equals("articulo"))
		{
			ultimaPosMapa=(Integer.parseInt(forma.getArticulosDetalleMap("numRegistros")+"")-1);
			//elimino el codigo insertado de la lista de codigos insertados de la busqueda
			forma.setArticulosDetalleMap("codigoarticuloInsert",mundo.CodigosInsertados(forma.getArticulosDetalleMap("codigoarticuloInsert").toString(),2,forma.getArticulosDetalleMap("codigoarticulo_"+forma.getIndexEliminado()).toString()));
			indices= (String[])forma.getArticulosDetalleMap("INDICES_MAPA");
			universal = forma.getArticulosDetalleMap();
		}	
		
		//elimina el codigo de la cadena de consulta general de servicios			
		for(int i=0;i<indices.length;i++)
		{ 
			//guarda los registros eliminados que son de la base de datos en el mapa de eliminados
			if((universal.get("estabd_"+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
			{
				forma.setDetalleEliminadoMap(indices[i]+""+numRegMapEliminados, universal.get(indices[i]+""+forma.getIndexEliminado()));
				forma.setDetalleEliminadoMap("tipo_"+numRegMapEliminados,fuente);
			}
		}			
		
		//aumento el numRegistros de registros eliminados de la base de datos
		if((universal.get("estabd_"+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
		{			
			forma.setDetalleEliminadoMap("numRegistros", (numRegMapEliminados+1));
		}
		
		//acomodar los registros del mapa en su nueva posicion
		for(int i=forma.getIndexEliminado();i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indices.length;j++)
			{
				universal.put(indices[j]+""+i,universal.get(indices[j]+""+(i+1)));
			}
		}
		
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indices.length;j++)
		{
			universal.remove(indices[j]+""+ultimaPosMapa);
		}
		
		//ahora actualizamos el numero de registros en el mapa.
		universal.put("numRegistros",ultimaPosMapa);		
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(codigoInstitucion),Integer.parseInt(universal.get("numRegistros").toString()), response, request, "servicioDetUndProc.jsp",forma.getIndexEliminado()==ultimaPosMapa);
	}
	//*************************************************************
	/**
	 *Adicciona un nuevo registro de tipo Articulos detalle
	 * @param UnidadProcedimientoForm forma
	 * */
	public void accionNuevoArticuloDetalle(UnidadProcedimientoForm forma)
	{
		UnidadProcedimiento mundo = new UnidadProcedimiento();
		int pos = Integer.parseInt(forma.getArticulosDetalleMap("numRegistros").toString());		
		forma.setArticulosDetalleMap("codigoserviundproce_"+pos,forma.getIndexServUndProc());
		forma.setArticulosDetalleMap("codigo_"+pos,"");		
		forma.setArticulosDetalleMap("codigoarticulo_"+pos,forma.getArticulosDetalleMap("codigoArticulo_"+pos));
		forma.setArticulosDetalleMap("descripcion_"+pos,forma.getArticulosDetalleMap("descripcionArticulo_"+pos));		
		forma.setArticulosDetalleMap("codigoarticuloInsert",mundo.CodigosInsertados(forma.getArticulosDetalleMap("codigoarticuloInsert").toString(),1,forma.getArticulosDetalleMap("codigoArticulo_"+pos).toString()));
		forma.setArticulosDetalleMap("estabd_"+pos, ConstantesBD.acronimoNo);
		forma.setArticulosDetalleMap("numRegistros",pos+1);
	}
	
	//*************************************************************	
	/**
	 * Guarda registros del detalle del servicio
	 * @param Connection con
	 * @param UnidadProcedimiento forma
	 * @param ActionMapping mapping
	 * @param int institucion
	 * */
	public ActionForward accionGuardarDetalle(Connection con, UnidadProcedimientoForm forma, ActionMapping mapping, UsuarioBasico usuario)
	{
		boolean transacction = UtilidadBD.iniciarTransaccion(con);
		String estado="consultar";
		UnidadProcedimiento mundo = new UnidadProcedimiento();
		
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getDetalleEliminadoMap("numRegistros")+"");i++)
		{
			if(mundo.eliminarServiciosDetUnidadProcedimiento(con,Integer.parseInt((forma.getDetalleEliminadoMap("codigo_"+i)+"").toString().trim()),Integer.parseInt((forma.getDetalleEliminadoMap("codigoserviundproce_"+i)+"").toString().trim()))) 			
			{
				if(forma.getDetalleEliminadoMap("tipo_"+i).equals("servicio"))
					this.generarlogDetalle(forma.getServicioDetalleMap(),forma.getDetalleEliminadoMap(),usuario,i);					
				else if(forma.getDetalleEliminadoMap("tipo_"+i).equals("examen"))
					this.generarlogDetalle(forma.getExamenDetalleMap(),forma.getDetalleEliminadoMap(),usuario,i);					
				else if(forma.getDetalleEliminadoMap("tipo_"+i).equals("articulo"))
					this.generarlogDetalle(forma.getArticulosDetalleMap(),forma.getDetalleEliminadoMap(),usuario,i);			
				
				estado="operacionTrue";
				transacction=true;						 
			}
		}		
		
		//insertar
		
		for(int i=0;i<Integer.parseInt(forma.getServicioDetalleMap("numRegistros")+"");i++)
		{		
			if((forma.getServicioDetalleMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoNo))
			{
				transacction =  mundo.insertarServiciosDetUnidadProcedimiento(con,llenarCrearMundoDetalle(forma,Integer.parseInt(forma.getServicioDetalleMap("codigoservicio_"+i).toString()),ConstantesBD.codigoNuncaValido,ConstantesBD.codigoNuncaValido));
				estado="operacionTrue";
			}		
		}
		
		for(int i=0;i<Integer.parseInt(forma.getExamenDetalleMap("numRegistros")+"");i++)
		{				
			if((forma.getExamenDetalleMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoNo))
			{
				transacction =  mundo.insertarServiciosDetUnidadProcedimiento(con,llenarCrearMundoDetalle(forma,ConstantesBD.codigoNuncaValido,Integer.parseInt(forma.getExamenDetalleMap("codigoexamen_"+i).toString()),ConstantesBD.codigoNuncaValido));							
				estado="operacionTrue";
			}
		}	
		
		for(int i=0;i<Integer.parseInt(forma.getArticulosDetalleMap("numRegistros")+"");i++)
		{			
			if((forma.getArticulosDetalleMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoNo))
			{
				transacction = mundo.insertarServiciosDetUnidadProcedimiento(con,llenarCrearMundoDetalle(forma,ConstantesBD.codigoNuncaValido,ConstantesBD.codigoNuncaValido,Integer.parseInt(forma.getArticulosDetalleMap("codigoarticulo_"+i).toString())));			
				estado="operacionTrue";
			}
		}
		
		if(transacction)
		{
			UtilidadBD.finalizarTransaccion(con);			
			logger.info("----->INSERTO 100% COBERTURAS");
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}
		
		this.accionConsultarServicioDetalle(con, forma, mapping, usuario, estado);		
		return mapping.findForward("principalServicioDet");						
	}
	
	
	//*************************************************************	
	/**
	 * Llena un objeto de Unidad de Procedimiento 
	 * @param UnidadProcedimientoForm forma
	 * @param int indice 
	 * */
	public UnidadProcedimiento llenarCrearMundoDetalle(UnidadProcedimientoForm forma,int servicio, int examen, int articulo)
	{
		UnidadProcedimiento mundo = new UnidadProcedimiento();
		mundo.setCodigoUndProce(forma.getIndexUndProc());
		mundo.setCodigoConsecutivoServiUndproce(forma.getIndexServUndProcConsecutivo());
		mundo.setCodigoServicio(servicio);
		mundo.setCodigoExamenct(examen);	
		mundo.setCodigoArticulo(articulo);
		return mundo;
	}	
	
	
    //*************************************************************
	/**
	 * General el Documento Log 
	 * @param UniadProcedimiento forma
	 * @param HashMap temp
	 * @param UsuarioBasico usuario
	 * @param isEliminacion
	 * @param pos
	 * */
	private void generarlogUndProc(UnidadProcedimientoForm forma, HashMap mapaUndProcTemp, UsuarioBasico usuario, boolean isEliminacion ,int pos )
	{
		String log = "";
		int tipoLog=0;
		String[] indices= (String[])forma.getUnidadProcMap("INDICES_MAPA");
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO UNIDAD PROCEDIMIENTO=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ "+(indices[i].trim().equals("acronimotipo_")?ValoresPorDefecto.getIntegridadDominio(forma.getUnidadProcEliminadoMap(indices[i]+""+pos)+""):forma.getUnidadProcEliminadoMap(indices[i]+""+pos))+" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL UNIDAD PROCEDIMIENTO=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("acronimotipo_")?ValoresPorDefecto.getIntegridadDominio(mapaUndProcTemp.get(indices[i]+"0")+""):mapaUndProcTemp.get(indices[i]+"0")) + " ]";
			}
			log += 		 "\n   ============INFORMACION MODIFICADA UNIDAD PROCEDIMIENTO=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("acronimotipo_")?ValoresPorDefecto.getIntegridadDominio(forma.getUnidadProcMap(indices[i]+""+pos)+""):forma.getUnidadProcMap(indices[i]+""+pos)) +" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		
		LogsAxioma.enviarLog(ConstantesBD.logUnidadProcedimientoCodigo,log,tipoLog,usuario.getLoginUsuario());	
	}
	
//	*************************************************************
	/**
	 * General el Documento Log Servicio
	 * @param UnidadProcedimiento forma
	 * @param HashMap temp
	 * @param UsuarioBasico usuario
	 * @param isEliminacion
	 * @param pos
	 * */
	private void generarlogServUndProc(UnidadProcedimientoForm forma, HashMap mapaServicioUndProcTemp, UsuarioBasico usuario, boolean isEliminacion ,int pos )
	{
		String log = "";
		int tipoLog=0;
		
		String[] indices =(String[])forma.getServicioUndProcMap("INDICES_MAPA");
		
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO SERVICIO X UNIDAD PROCEDIMIENTO=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ "+(indices[i].trim().equals("acronimotipo_")?ValoresPorDefecto.getIntegridadDominio(forma.getServicioEliminadoUndProcMap(indices[i]+""+pos)+""):forma.getServicioEliminadoUndProcMap(indices[i]+""+pos))+" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL SERVICIO X UNIDAD PROCEDIMIENTO=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("acronimotipo_")?ValoresPorDefecto.getIntegridadDominio(mapaServicioUndProcTemp.get(indices[i]+"0")+""):mapaServicioUndProcTemp.get(indices[i]+"0")) + " ]";
			}
			log += 		 "\n   ============INFORMACION MODIFICADA SERVICIO X UNIDAD PROCEDIMIENTO=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("acronimotipo_")?ValoresPorDefecto.getIntegridadDominio(forma.getServicioUndProcMap(indices[i]+""+pos)+""):forma.getServicioUndProcMap(indices[i]+""+pos)) +" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		
		LogsAxioma.enviarLog(ConstantesBD.logUnidadProcedimientoCodigo,log,tipoLog,usuario.getLoginUsuario());	
	}
	
	//*************************************************************
	/**
	 * General el Documento Log 
	 * @param forma
	 * @param HashMap temp
	 * @param UsuarioBasico usuario
	 * @param isEliminacion
	 * @param pos
	 * */
	private void generarlogDetalle(HashMap mapa, HashMap eliminadoMap, UsuarioBasico usuario ,int pos )
	{
		logger.warn("\n\n valor del mapa log : "+mapa+"\n\n");
		String log = "";
		int tipoLog=0;
		String[] indices= (String[])mapa.get("INDICES_MAPA");
		
		log = 		 "\n   ============REGISTRO ELIMINADO DETALLE DE SERVICIO X UNIDAD DE PROCEDIMIENTO=========== ";
		for(int i=0;i<(indices.length-1);i++)
		{
			log+= "\n  "+indices[i].replace("_", " ")+" [ "+(indices[i].trim().equals("acronimotipo_")?ValoresPorDefecto.getIntegridadDominio(eliminadoMap.get(indices[i]+""+pos)+""):eliminadoMap.get(indices[i]+""+pos))+" ]";
		}
		log+= "\n========================================================\n\n\n ";
		tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
				
		LogsAxioma.enviarLog(ConstantesBD.logUnidadProcedimientoCodigo,log,tipoLog,usuario.getLoginUsuario());	
	}	
}