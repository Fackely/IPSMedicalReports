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

import com.princetonsa.actionform.facturacion.InclusionesExclusionesForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.InclusionesExclusiones;

public class InclusionesExclusionesAction extends Action
{
	/**
	 * objeto para manejar los logs de esta clase
	 */
	
	Logger logger =Logger.getLogger(InclusionesExclusionesAction.class);
	
	
	/**
	 *  Matriz que contiene los indices del mapa.
	 */
	
	String[] indices={"codigo_","institucion_","nombre_","tiporegistro_"};
	
	
	
	/**
	 * Método excute del Action
	 */
	
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{
			if (form instanceof InclusionesExclusionesForm) 
			{
				InclusionesExclusionesForm forma=(InclusionesExclusionesForm) form;

				String estado=forma.getEstado();

				logger.info("Estado -->"+estado);

				con=UtilidadBD.abrirConexion();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());

				InclusionesExclusiones mundo=new InclusionesExclusiones();


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
					this.accionConsultarIncluExclu(con, forma, mundo, usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("continuar"))
				{
					logger.info("mapa-->"+forma.getIncluExcluMap());
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if (estado.equals("redireccion"))
				{
					logger.info("mapa-->"+forma.getIncluExcluMap());
					UtilidadBD.closeConnection(con);
					if(UtilidadTexto.isEmpty(forma.getLinkSiguiente()))
						forma.setLinkSiguiente("../inclusionesExclusiones/inclusionesExclusiones.do?estado=continuar");
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else if(estado.equals("nuevo"))
				{
					this.accionNuevoIncluExclu(forma);
					UtilidadBD.closeConnection(con);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getIncluExcluMap("numRegistros").toString()), response, request, "inclusionesExclusiones.jsp",true);
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
					this.accionConsultarIncluExclu(con,forma,mundo,usuario);
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
					logger.warn("Estado no valido dentro del flujo de INCLUSIONES - EXCLUSIONES ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de IncluExcluForm");
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
	
	private void accionGuardarRegistros(Connection con, InclusionesExclusionesForm forma, InclusionesExclusiones mundo, UsuarioBasico usuario)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		logger.info("mapaInclusionesExclusiones-->"+forma.getIncluExcluMap());
		
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getIncluExcluEliminadosMap("numRegistros")+"");i++)
		{
			if(mundo.eliminarRegistro(con,Integer.parseInt(forma.getIncluExcluEliminadosMap("institucion_"+i)+""), forma.getIncluExcluEliminadosMap("codigo_"+i)+""))
			{
				this.generarLog(forma, mundo, usuario, true, i);
				transaccion=true;
			}
		}
		
		for(int i=0;i<Integer.parseInt(forma.getIncluExcluMap("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getIncluExcluMap("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma,mundo,Integer.parseInt(forma.getIncluExcluMap("institucion_"+i)+""),i,usuario,forma.getIncluExcluMap("codigo_"+i)+""))
			{
				HashMap vo=new HashMap();
				vo.put("codigo",forma.getIncluExcluMap("codigo_"+i));
				vo.put("nombre",forma.getIncluExcluMap("nombre_"+i));
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("usuario_modifica",usuario.getLoginUsuario());
				vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				vo.put("hora_modifica",UtilidadFecha.getHoraActual());
				transaccion=mundo.modificar(con, vo);
			}
			
			//insertar
			else if((forma.getIncluExcluMap("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigo",forma.getIncluExcluMap("codigo_"+i));
				vo.put("nombre",forma.getIncluExcluMap("nombre_"+i));
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
	
	private void generarLog(InclusionesExclusionesForm forma, InclusionesExclusiones mundo, UsuarioBasico usuario, boolean isEliminacion, int pos) 
	{
		String log = "";
		int tipoLog=0;
		
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ "+(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(forma.getIncluExcluEliminadosMap(indices[i]+""+pos)+""):forma.getIncluExcluEliminadosMap(indices[i]+""+pos))+" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(mundo.getIncluExcluMap().get(indices[i]+"0")+""):mundo.getIncluExcluMap().get(indices[i]+"0")) + " ]";
			}
			log += 		 "\n   ============INFORMACION MODIFICADA=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(forma.getIncluExcluMap(indices[i]+""+pos)+""):forma.getIncluExcluMap(indices[i]+""+pos)) +" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		
		LogsAxioma.enviarLog(ConstantesBD.logInclusionesExclusionesCodigo,log,tipoLog,usuario.getLoginUsuario());
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
	
	private boolean existeModificacion(Connection con, InclusionesExclusionesForm forma, InclusionesExclusiones mundo,int institucion,int pos, UsuarioBasico usuario, String codigo)
	{
		HashMap temp=mundo.consultarIncluExcluEspecifico(con, institucion, codigo);
		for(int i=0;i<indices.length;i++)
		{
			if(temp.containsKey(indices[i]+"0")&&forma.getIncluExcluMap().containsKey(indices[i]+""+pos))
			{
				if(!((temp.get(indices[i]+"0")+"").trim().equals(forma.getIncluExcluMap(indices[i]+""+pos)+"")))
				{
					mundo.setIncluExcluMap(temp);
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
	
	private ActionForward accionEliminarRegistro(InclusionesExclusionesForm forma, HttpServletRequest request, HttpServletResponse response, ActionMapping mapping)
	{
		int numRegMapEliminados=Integer.parseInt(forma.getIncluExcluEliminadosMap("numRegistros")+"");
		int ultimaPosMapa=(Integer.parseInt(forma.getIncluExcluMap("numRegistros")+"")-1);
		
		//poner la informacion en el otro mapa.
		
//		solo pasar al mapa los registros que son de BD
		if((forma.getIncluExcluMap("tiporegistro_"+forma.getPosEliminar())+"").trim().equalsIgnoreCase("BD"))
		{
			for(int i=0;i<indices.length;i++)
			{
			
				forma.setIncluExcluEliminadosMap(indices[i]+""+numRegMapEliminados, forma.getIncluExcluMap(indices[i]+""+forma.getPosEliminar()));
			}
			forma.setIncluExcluEliminadosMap("numRegistros", (numRegMapEliminados+1));
		}
		
		//acomodar los registros del mapa en su nueva posicion
		for(int i=forma.getPosEliminar();i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indices.length;j++)
			{
				forma.setIncluExcluMap(indices[j]+""+i,forma.getIncluExcluMap(indices[j]+""+(i+1)));
			}
		}
		
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indices.length;j++)
		{
			forma.getIncluExcluMap().remove(indices[j]+""+ultimaPosMapa);
		}
		
		//ahora actualizamo el numero de registros en el mapa.
		forma.setIncluExcluMap("numRegistros",ultimaPosMapa);
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getIncluExcluMap("numRegistros").toString()), response, request, "inclusionesExclusiones.jsp",forma.getPosEliminar()==ultimaPosMapa);

		
	}
	
	
	
	
	/**
	 * 
	 * @param forma
	 */	
	
	private void accionNuevoIncluExclu(InclusionesExclusionesForm forma)
	{
		int pos=Integer.parseInt(forma.getIncluExcluMap("numRegistros")+"");
		forma.setIncluExcluMap("codigo_"+pos,"");
		forma.setIncluExcluMap("institucion_"+pos,"");
		forma.setIncluExcluMap("nombre_"+pos,"");
		forma.setIncluExcluMap("tiporegistro_"+pos,"MEM");
		forma.setIncluExcluMap("numRegistros", (pos+1)+"");
	}
	
	
	/**
	 * 
	 * @param forma
	 */
	
	private void accionOrdenarMapa(InclusionesExclusionesForm forma)
	{
		
		int numReg=Integer.parseInt(forma.getIncluExcluMap("numRegistros")+"");
		forma.setIncluExcluMap(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getIncluExcluMap(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setIncluExcluMap("numRegistros",numReg+"");
	}
	
	
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	
	private void accionConsultarIncluExclu(Connection con, InclusionesExclusionesForm forma, InclusionesExclusiones mundo, UsuarioBasico usuario)
	{
		mundo.consultarIncluExcluExistentes(con,usuario.getCodigoInstitucionInt());
		forma.setIncluExcluMap((HashMap)mundo.getIncluExcluMap().clone());
	}

}
