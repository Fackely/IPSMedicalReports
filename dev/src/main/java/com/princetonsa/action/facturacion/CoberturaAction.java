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
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.facturacion.CoberturaForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.Cobertura;

/**
 * Clase para el manejo del workflow de la funcionalidad
 * @author Jose Eduardo Arias Doncel
 * jearias@princetonsa.com
 */

public class CoberturaAction extends Action
{
	/**
	 * Objeto para manejar el log de la clase
	 * */	
	private Logger logger = Logger.getLogger(CoberturaAction.class);
	
	/**
	 * Metodo execute del action
	 * @param ActionMapping mapping
	 * @param ActionForm form
	 * @param HttpServletRequest request
	 * @param HttpServletResponse response
	 * @return ActionForward
	 * */
	public ActionForward execute(ActionMapping mapping, 
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response) throws Exception
								 {
		Connection con = null;
		try{
			if(response==null);
			if(form instanceof CoberturaForm)		 
			{

				con = UtilidadBD.abrirConexion();

				if(con == null)
				{
					request.setAttribute("CodigoDescripcionError","errors.problemasBd");
					return mapping.findForward("paginaError");				 
				}

				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				CoberturaForm forma = (CoberturaForm)form;
				String estado = forma.getEstado();

				logger.warn("n\n\nEl estado en CoberturaAction es------->"+estado+"\n");		 

				if (estado == null)
				{				 
					forma.reset();
					logger.warn("Estado no Valido dentro del flujo de Coberturas (null)");
					request.setAttribute("CodigDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginError");			  
				}


				else if(estado.equals("empezar"))
				{
					return this.accionEmpezarConsulta(con,forma, mapping,usuario,"consultar");				 				 		  
				}			 

				else if(estado.equals("nuevo"))
				{
					UtilidadBD.closeConnection(con);
					this.accionNuevo(forma);	
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()),Integer.parseInt(forma.getCoberturaMap("numRegistros").toString()), response, request, "coberturaParametrizacion.jsp",true);
				}			 

				else if(estado.equals("eliminarCampo"))
				{	 				 
					return this.accionEliminarCampo(forma,request,response,mapping, usuario.getCodigoInstitucionInt());				 
				}

				else if(estado.equals("guardar"))
				{				 	
					return this.accionGuardarRegistros(con,forma, mapping,usuario);				 
				}

				else if(estado.equals("ordenarConsultar"))
				{
					this.accionOrdenarMapa(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");					
				}

				else if (estado.equals("redireccion"))
				{
					UtilidadBD.closeConnection(con);
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}			 

				else if(estado.equals("ordenar"))
				{
					this.accionOrdenarMapa(forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}


				else
				{
					forma.reset();
					logger.warn("Estado no valido dentro del flujo de Coberturas -> "+estado);
					request.setAttribute("CodigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.closeConnection(con);
					mapping.findForward("paginaError");
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
	
	//------------------------- Fin Forward
		
	/**
	 * Consulta Avanzada de Cobertura
	 * @param conn
	 * @param forma
	 * @param mapping
	 * @return ActionForward
	 * */
	private ActionForward accionEmpezarConsulta(Connection con, CoberturaForm forma, ActionMapping mapping, UsuarioBasico usuario, String estado)
	{
		forma.reset();
		
		forma.setManejoEspecial(ValoresPorDefecto.getManejoEspecialInstitucionesOdontologia(usuario.getCodigoInstitucionInt()));
		forma.setCoberturaMap("numRegistros","0");
		forma.setCoberturaEliminadosMap("numRegistros","0")	;
		
		forma.setCoberturaConsultaMap("flagInicio","true");
		forma.setCoberturaConsultaMap("institucionC",usuario.getCodigoInstitucionInt());
		
		forma.setCoberturaMap(Cobertura.consultaCoberturaAvanzada(con,forma.getCoberturaConsultaMap()));	
		
		forma.setEstado(estado);
	    UtilidadBD.closeConnection(con);
	    return mapping.findForward("principal");		
	}	
	
	
	
	/**
	 * Genera un Nuevo Registro en el HashMap de CoberturaMap
	 *@param forma
	 * */
	private void accionNuevo(CoberturaForm forma)
	{		
		int pos = Integer.parseInt(forma.getCoberturaMap("numRegistros")+"");
		forma.setCoberturaMap("codigo_"+pos,"");		
		forma.setCoberturaMap("descripcion_"+pos,"");
		forma.setCoberturaMap("observacion_"+pos,"");	
		forma.setCoberturaMap("tipocobertura_"+pos,"");	
		forma.setCoberturaMap("estado_"+pos,ConstantesBD.acronimoSi);
		forma.setCoberturaMap("estabd_"+pos,ConstantesBD.acronimoNo);
		forma.setCoberturaMap("depende_"+pos,ConstantesBD.acronimoNo);
		forma.setCoberturaMap("numRegistros",(pos+1)+"");
	}	
	
	
	
	/**
	 * Guarda, Modifica o Elimina un Registro en Cobertura
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param usuario
	 * @return ActionForward
	 * */
	private ActionForward accionGuardarRegistros(Connection con, CoberturaForm forma, ActionMapping mapping,UsuarioBasico usuario)
	{
		boolean transacction = UtilidadBD.iniciarTransaccion(con);
		String estadoEliminar="ninguno";
		Cobertura mundo = new Cobertura();
		
		
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getCoberturaEliminadosMap("numRegistros")+"");i++)
		{
			if(mundo.eliminarCobertura(con,(forma.getCoberturaEliminadosMap("codigo_"+i)+"").toString().trim(),usuario.getCodigoInstitucionInt()))			
			{				
				 this.generarlog(forma, new HashMap(), usuario, true, i);
				 estadoEliminar="operacionTrue";
				 transacction=true;						 
			}
		}		
		
		for(int i=0;i<Integer.parseInt(forma.getCoberturaMap("numRegistros")+"");i++)
		{			
			//modificar			
			if((forma.getCoberturaMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoSi)&&
					this.existeModificacion(con,forma,mundo,forma.getCoberturaMap("codigoOld_"+i).toString(),Integer.parseInt(usuario.getCodigoInstitucion()),i,usuario))
			{				
				transacction = mundo.modificarCobertura(con,llenarCrearMundo(forma,i,usuario));
				estadoEliminar="operacionTrue";
			}
			
			//insertar
			else if((forma.getCoberturaMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoNo))
			{
				transacction = mundo.insertarCobertura(con,llenarCrearMundo(forma,i,usuario));
				estadoEliminar="operacionTrue";
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
		
		return this.accionEmpezarConsulta(con,forma, mapping,usuario,estadoEliminar);		 		
	}
	
	
	
		
	/**
	 * @param forma
	 * */
	private void accionOrdenarMapa(CoberturaForm forma)
	{
		String[] indices = (String[])forma.getCoberturaMap("INDICES_MAPA");
		int numReg = Integer.parseInt(forma.getCoberturaMap("numRegistros")+"");
		forma.setCoberturaMap(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getCoberturaMap(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setCoberturaMap("numRegistros",numReg+"");
		forma.setCoberturaMap("INDICES_MAPA",indices);		
	}
	
	
	
	/**
	 * Unicializa un objeto Cobertura con los datos.
	 * @param forma
	 * @param indice
	 * */
	private Cobertura llenarCrearMundo(CoberturaForm forma, int indice, UsuarioBasico usuario)
	{
		Cobertura mundo = new Cobertura();		
		mundo.setCodigoCobertura(forma.getCoberturaMap().get("codigo_"+indice).toString().trim());				
		
		if(forma.getCoberturaMap().get("depende_"+indice).toString().trim().equals(ConstantesBD.acronimoNo) && (forma.getCoberturaMap().get("estabd_"+indice).toString().equals(ConstantesBD.acronimoSi)))
			mundo.setCodigoCoberturaOld(forma.getCoberturaMap().get("codigoOld_"+indice).toString().trim());
		else
			mundo.setCodigoCoberturaOld(forma.getCoberturaMap().get("codigo_"+indice).toString().trim());
			
		
		mundo.setInstitucion(usuario.getCodigoInstitucionInt());
		mundo.setDescripcionCobertura(forma.getCoberturaMap().get("descripcion_"+indice).toString().trim());
		mundo.setObservacionCobertura(forma.getCoberturaMap().get("observacion_"+indice).toString().trim());
		mundo.setEstadoCobertura(forma.getCoberturaMap().get("estado_"+indice).toString());	
		if(ValoresPorDefecto.getManejoEspecialInstitucionesOdontologia(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoNo)){
			mundo.setTipoCobertura(ConstantesIntegridadDominio.acronimoTipoCoberturaGeneral);
		}else{
			mundo.setTipoCobertura(forma.getCoberturaMap().get("tipocobertura_"+indice).toString());
		}
			
		logger.info("TIPO DE COBERTURA ------------------------------------------------>"+mundo.getTipoCobertura());
		mundo.setUsuarioModifica(usuario.getLoginUsuario());		
		mundo.setFechaModifica(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		mundo.setHoraModifica(UtilidadFecha.getHoraActual());
		return mundo;		
	}
	
	
	
	/**
	 * verifica si el registro se ha modificado
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param String codigoCobertura
	 * @param int institucion
	 * @param int pos
	 * */
	private boolean existeModificacion(Connection con,CoberturaForm forma,Cobertura mundo, String codigoCobertura, int institucion, int pos, UsuarioBasico usuario)
	{
		HashMap temp = mundo.consultaCoberturaBasica(con, codigoCobertura, institucion);
		String[] indices = (String[])forma.getCoberturaMap("INDICES_MAPA");		
				
		for(int i=0;i<indices.length;i++)
		{		
			if(temp.containsKey(indices[i]+"0")&&forma.getCoberturaMap().containsKey(indices[i]+pos))
			{				
				if(!((temp.get(indices[i]+"0")+"").trim().equals((forma.getCoberturaMap(indices[i]+pos)).toString().trim())))
				{
					this.generarlog(forma, temp, usuario, false, pos);
					return true;
				}				
			}
		}		
		return false;		
	}
		
	
	
	/**
	 * Elimina un Registro del HashMap de Cobertura y Adicciona el Registro Eliminado en el HashMap de Cobertura Eliminados
	 * @param forma
	 * @param request
	 * @param response
	 * @param mapping
	 * @return ActionForward
	 */
	private ActionForward accionEliminarCampo(CoberturaForm forma, HttpServletRequest request, HttpServletResponse response, ActionMapping mapping, int codigoInstitucion) 
	{
		int numRegMapEliminados=Integer.parseInt(forma.getCoberturaEliminadosMap("numRegistros")+"");
		
		int ultimaPosMapa=(Integer.parseInt(forma.getCoberturaMap("numRegistros")+"")-1);
		
		//poner la informacion en el otro mapa.
		String[] indices= (String[])forma.getCoberturaMap("INDICES_MAPA");		
		
		for(int i=0;i<indices.length;i++)
		{ 
			//solo pasar al mapa los registros que son de BD
			if((forma.getCoberturaMap("estabd_"+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
			{
				forma.setCoberturaEliminadosMap(indices[i]+""+numRegMapEliminados, forma.getCoberturaMap(indices[i]+""+forma.getIndexEliminado()));
			}
		}		
		
		if((forma.getCoberturaMap("estabd_"+forma.getIndexEliminado())+"").trim().equals(ConstantesBD.acronimoSi))
		{			
			forma.setCoberturaEliminadosMap("numRegistros", (numRegMapEliminados+1));
		}
		
		//acomodar los registros del mapa en su nueva posicion
		for(int i=forma.getIndexEliminado();i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indices.length;j++)
			{
				forma.setCoberturaMap(indices[j]+""+i,forma.getCoberturaMap(indices[j]+""+(i+1)));
			}
		}
		
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indices.length;j++)
		{
			forma.getCoberturaMap().remove(indices[j]+""+ultimaPosMapa);
		}
		
		//ahora actualizamos el numero de registros en el mapa.
		forma.setCoberturaMap("numRegistros",ultimaPosMapa);		
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), ValoresPorDefecto.getMaxPageItemsInt(codigoInstitucion),Integer.parseInt(forma.getCoberturaMap("numRegistros").toString()), response, request, "coberturaParametrizacion.jsp",forma.getIndexEliminado()==ultimaPosMapa);
	}
	
	
	
	/**
	 * General el Documento Log 
	 * @param forma
	 * @param HashMap temp
	 * @param UsuarioBasico usuario
	 * @param isEliminacion
	 * @param pos
	 * */
	private void generarlog(CoberturaForm forma, HashMap mapaCoberturaTemp, UsuarioBasico usuario, boolean isEliminacion ,int pos )
	{
		String log = "";
		int tipoLog=0;
		String[] indices= (String[])forma.getCoberturaMap("INDICES_MAPA");
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ "+(indices[i].trim().equals("acronimotipo_")?ValoresPorDefecto.getIntegridadDominio(forma.getCoberturaEliminadosMap(indices[i]+""+pos)+""):forma.getCoberturaEliminadosMap(indices[i]+""+pos))+" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("acronimotipo_")?ValoresPorDefecto.getIntegridadDominio(mapaCoberturaTemp.get(indices[i]+"0")+""):mapaCoberturaTemp.get(indices[i]+"0")) + " ]";
			}
			log += 		 "\n   ============INFORMACION MODIFICADA=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("acronimotipo_")?ValoresPorDefecto.getIntegridadDominio(forma.getCoberturaMap(indices[i]+""+pos)+""):forma.getCoberturaMap(indices[i]+""+pos)) +" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		LogsAxioma.enviarLog(ConstantesBD.logCoberturaCodigo,log,tipoLog,usuario.getLoginUsuario());	
	}
	
	
	
}