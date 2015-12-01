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

import com.princetonsa.actionform.facturacion.PaquetesForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.Paquetes;


/**
 * Clase para el manejo del workflow de la funcionalidad
 * @author axioma
 *
 */


public class PaquetesAction extends Action 
{
	
	/**
	 * objeto para manejar los logs de esta clase
	 */
	
	Logger logger =Logger.getLogger(PaquetesAction.class);
	
	
	/**
	 *  Matriz que contiene los indices del mapa.
	 */
	
	String[] indices={"codigopaquete_","codigopaqueteoriginal_","institucion_","descripcion_","servicio_","nomservicio_","distribucioncosto_","cuentacontmayval_","cuentacontmenval_","tiporegistro_"};
	
	
	
	/**
	 * Método excute del Action
	 */
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{
			if (form instanceof PaquetesForm) 
			{
				PaquetesForm forma=(PaquetesForm) form;

				String estado=forma.getEstado();

				logger.info("Estado -->"+estado);

				con=UtilidadBD.abrirConexion();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());

				Paquetes mundo=new Paquetes();


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
					this.accionConsultarPaquetes(con, forma, mundo, usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("continuar"))
				{
					logger.info("mapa-->"+forma.getPaquetesMap());
					UtilidadBD.closeConnection(con);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getPaquetesMap("numRegistros").toString()), response, request, "paquetes.jsp",true);

				}
				else if (estado.equals("redireccion"))
				{
					logger.info("mapa-->"+forma.getPaquetesMap());
					UtilidadBD.closeConnection(con);
					if(UtilidadTexto.isEmpty(forma.getLinkSiguiente()))
						forma.setLinkSiguiente("../paquetes/paquetes.do?estado=continuar");
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else if(estado.equals("nuevo"))
				{
					this.accionNuevoPaquete(forma);
					UtilidadBD.closeConnection(con);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getPaquetesMap("numRegistros").toString()), response, request, "paquetes.jsp",true);
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
					this.accionConsultarPaquetes(con,forma,mundo,usuario);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");

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
					logger.warn("Estado no valido dentro del flujo de PAQUETES ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de PaquetesForm");
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
	
	private void accionGuardarRegistros(Connection con, PaquetesForm forma, Paquetes mundo, UsuarioBasico usuario)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		logger.info("mapaPaquertes-->"+forma.getPaquetesMap());
		
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getPaquetesEliminadosMap("numRegistros")+"");i++)
		{
			if(mundo.eliminarRegistro(con,Integer.parseInt(forma.getPaquetesEliminadosMap("institucion_"+i)+""), forma.getPaquetesEliminadosMap("codigopaqueteoriginal_"+i)+""))
			{
				this.generarLog(forma, mundo, usuario, true, i);
				transaccion=true;
			}
		}
		
		for(int i=0;i<Integer.parseInt(forma.getPaquetesMap("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getPaquetesMap("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma,mundo,Integer.parseInt(forma.getPaquetesMap("institucion_"+i)+""),i,usuario,forma.getPaquetesMap("codigopaqueteoriginal_"+i)+""))
			{
				HashMap vo=new HashMap();
				vo.put("codigo_paquete",forma.getPaquetesMap("codigopaquete_"+i));
				vo.put("descripcion",forma.getPaquetesMap("descripcion_"+i));
				vo.put("servicio",forma.getPaquetesMap("servicio_"+i));
				vo.put("distribucion_costo",forma.getPaquetesMap("distribucioncosto_"+i));
				vo.put("cuenta_cont_may_val",forma.getPaquetesMap("cuentacontmayval_"+i));
				vo.put("cuenta_cont_men_val",forma.getPaquetesMap("cuentacontmenval_"+i));
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("usuario_modifica",usuario.getLoginUsuario());
				vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				vo.put("hora_modifica",UtilidadFecha.getHoraActual());
				vo.put("codigo_paquete_original",forma.getPaquetesMap("codigopaqueteoriginal_"+i));
				
				transaccion=mundo.modificar(con, vo);
			}
			
			//insertar
			else if((forma.getPaquetesMap("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigo_paquete",forma.getPaquetesMap("codigopaquete_"+i));
				vo.put("descripcion",forma.getPaquetesMap("descripcion_"+i));
				vo.put("servicio",forma.getPaquetesMap("servicio_"+i));
				vo.put("distribucion_costo",forma.getPaquetesMap("distribucioncosto_"+i));
				vo.put("cuenta_cont_may_val",forma.getPaquetesMap("cuentacontmayval_"+i));
				vo.put("cuenta_cont_men_val",forma.getPaquetesMap("cuentacontmenval_"+i));
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
	 * @param mundo
	 * @param usuario
	 * @param isEliminacion
	 * @param pos
	 */
	
	private void generarLog(PaquetesForm forma, Paquetes mundo, UsuarioBasico usuario, boolean isEliminacion, int pos) 
	{
		String log = "";
		int tipoLog=0;
		
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ "+(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(forma.getPaquetesEliminadosMap(indices[i]+""+pos)+""):forma.getPaquetesEliminadosMap(indices[i]+""+pos))+" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(mundo.getPaquetesMap().get(indices[i]+"0")+""):mundo.getPaquetesMap().get(indices[i]+"0")) + " ]";
			}
			log += 		 "\n   ============INFORMACION MODIFICADA=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(forma.getPaquetesMap(indices[i]+""+pos)+""):forma.getPaquetesMap(indices[i]+""+pos)) +" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		
		LogsAxioma.enviarLog(ConstantesBD.logPaquetesCodigo,log,tipoLog,usuario.getLoginUsuario());
	}
	
	
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param institucion
	 * @param pos
	 * @param usuario
	 * @param codigoPaquete
	 * @return
	 */
	
	private boolean existeModificacion(Connection con, PaquetesForm forma, Paquetes mundo,int institucion,int pos, UsuarioBasico usuario, String codigoPaquete)
	{
		HashMap temp=mundo.consultarPaqueteEspecifico(con, institucion, codigoPaquete);
		for(int i=0;i<indices.length;i++)
		{
			if(temp.containsKey(indices[i]+"0")&&forma.getPaquetesMap().containsKey(indices[i]+""+pos))
			{
				if(!((temp.get(indices[i]+"0")+"").trim().equals(forma.getPaquetesMap(indices[i]+""+pos)+"")))
				{
					mundo.setPaquetesMap(temp);
					this.generarLog(forma, mundo, usuario, false, pos);
					return true;
				}
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
	
	private ActionForward accionEliminarRegistro(PaquetesForm forma, HttpServletRequest request, HttpServletResponse response, ActionMapping mapping)
	{
		int numRegMapEliminados=Integer.parseInt(forma.getPaquetesEliminadosMap("numRegistros")+"");
		int ultimaPosMapa=(Integer.parseInt(forma.getPaquetesMap("numRegistros")+"")-1);
		
		//poner la informacion en el otro mapa.
		
//		solo pasar al mapa los registros que son de BD
		if((forma.getPaquetesMap("tiporegistro_"+forma.getPosEliminar())+"").trim().equalsIgnoreCase("BD"))
		{
			for(int i=0;i<indices.length;i++)
			{
			
				forma.setPaquetesEliminadosMap(indices[i]+""+numRegMapEliminados, forma.getPaquetesMap(indices[i]+""+forma.getPosEliminar()));
			}
			forma.setPaquetesEliminadosMap("numRegistros", (numRegMapEliminados+1));
		}
		
		//acomodar los registros del mapa en su nueva posicion
		for(int i=forma.getPosEliminar();i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indices.length;j++)
			{
				forma.setPaquetesMap(indices[j]+""+i,forma.getPaquetesMap(indices[j]+""+(i+1)));
			}
		}
		
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indices.length;j++)
		{
			forma.getPaquetesMap().remove(indices[j]+""+ultimaPosMapa);
		}
		
		//ahora actualizamo el numero de registros en el mapa.
		forma.setPaquetesMap("numRegistros",ultimaPosMapa);
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getPaquetesMap("numRegistros").toString()), response, request, "paquetes.jsp",forma.getPosEliminar()==ultimaPosMapa);

		
	}
	
	
	
	
	/**
	 * 
	 * @param forma
	 */	
	
	private void accionNuevoPaquete(PaquetesForm forma)
	{
		int pos=Integer.parseInt(forma.getPaquetesMap("numRegistros")+"");
		forma.setPaquetesMap("codigopaquete_"+pos,"");
		forma.setPaquetesMap("codigopaqueteoriginal_"+pos,"");
		forma.setPaquetesMap("institucion_"+pos,"");
		forma.setPaquetesMap("descripcion_"+pos,"");
		forma.setPaquetesMap("servicio_"+pos,"");
		forma.setPaquetesMap("nomservicio_"+pos,"");
		forma.setPaquetesMap("distribucioncosto_"+pos,"");
		forma.setPaquetesMap("cuentacontmayval_"+pos,"");
		forma.setPaquetesMap("cuentacontmenval_"+pos,"");
		forma.setPaquetesMap("tiporegistro_"+pos,"MEM");
		forma.setPaquetesMap("numRegistros", (pos+1)+"");
	}
	
	
	/**
	 * 
	 * @param forma
	 */
	
	private void accionOrdenarMapa(PaquetesForm forma)
	{
		
		int numReg=Integer.parseInt(forma.getPaquetesMap("numRegistros")+"");
		forma.setPaquetesMap(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getPaquetesMap(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setPaquetesMap("numRegistros",numReg+"");
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	
	private void accionConsultarPaquetes(Connection con, PaquetesForm forma, Paquetes mundo, UsuarioBasico usuario)
	{
		mundo.consultarPaquetesExistentes(con,usuario.getCodigoInstitucionInt());
		forma.setPaquetesMap((HashMap)mundo.getPaquetesMap().clone());
	}
	
	

}
