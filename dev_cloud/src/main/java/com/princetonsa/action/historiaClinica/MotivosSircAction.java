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
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadSesion;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.historiaClinica.MotivosSircForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.MotivosSirc;

public class MotivosSircAction extends Action
{
	
	/**
	 * Cadena que contiene los indices del mapa.
	 */
	private static final String indicesMapa="consecutivo_,codigo_,descripcion_,tipomotivo_,activo_,tiporegistro_"; 
	
	 /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(MotivosSircAction.class);
	
	/**
	 * Método execute del action
	 */
	public ActionForward execute(	ActionMapping mapping, 	
													        ActionForm form, 
													        HttpServletRequest request, 
													        HttpServletResponse response) throws Exception
													        {

		Connection con = null;
		try {
			if(form instanceof MotivosSircForm)
			{

				con=UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				MotivosSircForm forma = (MotivosSircForm) form;

				UsuarioBasico usuario = null;
				usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());

				MotivosSirc mundo = new MotivosSirc();
				String estado = forma.getEstado();

				logger.warn("estado --> "+estado);

				forma.setMostrarMensaje(new ResultadoBoolean(false));


				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ConceptosCarteraAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}			
				else if (estado.equals("empezar"))
				{
					forma.reset();
					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
					this.accionConsultarMotivosSIRC(con,forma,mundo,usuario);
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
					this.accionNuevoMotivo(forma);
					UtilidadBD.closeConnection(con);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getMotivosSirc("numRegistros").toString()), response, request, "motivosSirc.jsp",true);
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
					this.accionConsultarMotivosSIRC(con,forma,mundo,usuario);
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
					logger.warn("Estado no valido dentro del flujo de MOTIVOS SIRC ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de MotivosSircForm");
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
	
	
	
	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenarMapa(MotivosSircForm forma)
	{
		String[] indices=indicesMapa.split(",");
		int numReg=Integer.parseInt(forma.getMotivosSirc("numRegistros")+"");
		forma.setMotivosSirc(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMotivosSirc(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setMotivosSirc("numRegistros",numReg+"");
	}



	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario 
	 */
	private void accionGuardarRegistros(Connection con, MotivosSircForm forma, MotivosSirc mundo, UsuarioBasico usuario)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getMotivosSircEliminado("numRegistros")+"");i++)
		{
			if(mundo.eliminarRegistro(con,forma.getMotivosSircEliminado("consecutivo_"+i)+""))
			{
				this.generarLog(forma, mundo, usuario, true, i);
				transaccion=true;
			}
		}
		
		for(int i=0;i<Integer.parseInt(forma.getMotivosSirc("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getMotivosSirc("tiporegistro_"+i)+"").trim().equalsIgnoreCase("BD")&&this.existeModificacion(con,forma,mundo,forma.getMotivosSirc("consecutivo_"+i)+"",i,usuario))
			{
				HashMap vo=new HashMap();
				vo.put("descripcion",forma.getMotivosSirc("descripcion_"+i));
				vo.put("tipomotivo",forma.getMotivosSirc("tipomotivo_"+i));
				vo.put("activo",forma.getMotivosSirc("activo_"+i));
				vo.put("consecutivo",forma.getMotivosSirc("consecutivo_"+i));
				transaccion=mundo.modificarMotivoSirc(con, vo);
			}
			//insertar
			else if((forma.getMotivosSirc("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("codigo",forma.getMotivosSirc("codigo_"+i));
				vo.put("descripcion",forma.getMotivosSirc("descripcion_"+i));
				vo.put("tipomotivo",forma.getMotivosSirc("tipomotivo_"+i));
				vo.put("activo",forma.getMotivosSirc("activo_"+i));
				vo.put("institucion", usuario.getCodigoInstitucion());
				transaccion=mundo.insertarMotivoSirc(con, vo);
			}
		}
		
		if(transaccion)
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"PROCESO EXITOSO."));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMostrarMensaje(new ResultadoBoolean(true,"NO SE PUDO FINALIZAR EL PROCESO EXITOSAMENTE."));
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
	private void generarLog(MotivosSircForm forma, MotivosSirc mundo, UsuarioBasico usuario, boolean isEliminacion, int pos) 
	{
		String log = "";
		int tipoLog=0;
		String[] indices=indicesMapa.split(",");
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ "+(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(forma.getMotivosSircEliminado(indices[i]+""+pos)+""):forma.getMotivosSircEliminado(indices[i]+""+pos))+" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(mundo.getMapaMotivosSircTemp().get(indices[i]+"0")+""):mundo.getMapaMotivosSircTemp().get(indices[i]+"0")) + " ]";
			}
			log += 		 "\n   ============INFORMACION MODIFICADA=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(forma.getMotivosSirc(indices[i]+""+pos)+""):forma.getMotivosSirc(indices[i]+""+pos)) +" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		LogsAxioma.enviarLog(ConstantesBD.logMotivosSircCodigo,log,tipoLog,usuario.getLoginUsuario());
	}


	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario 
	 * @return
	 */
	private boolean existeModificacion(Connection con, MotivosSircForm forma, MotivosSirc mundo,String consecutivo,int pos, UsuarioBasico usuario)
	{
		HashMap temp=mundo.consultarMotivoSircEspecifico(con, consecutivo);
		String[] indices=indicesMapa.split(",");
		for(int i=0;i<indices.length;i++)
		{
			if(temp.containsKey(indices[i]+"0")&&forma.getMotivosSirc().containsKey(indices[i]+""+pos))
			{
				if(!((temp.get(indices[i]+"0")+"").trim().equals(forma.getMotivosSirc(indices[i]+""+pos)+"")))
				{
					mundo.setMapaMotivosSircTemp(temp);
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
	 * @param response 
	 * @param request 
	 * @param mapping 
	 */
	private ActionForward accionEliminarRegistro(MotivosSircForm forma, HttpServletRequest request, HttpServletResponse response, ActionMapping mapping)
	{
		int numRegMapEliminados=Integer.parseInt(forma.getMotivosSircEliminado("numRegistros")+"");
		int ultimaPosMapa=(Integer.parseInt(forma.getMotivosSirc("numRegistros")+"")-1);
		
		//poner la informacion en el otro mapa.
		String[] indices=indicesMapa.split(",");
//		solo pasar al mapa los registros que son de BD
		if((forma.getMotivosSirc("tiporegistro_"+forma.getPosEliminar())+"").trim().equalsIgnoreCase("BD"))
		{
			for(int i=0;i<indices.length;i++)
			{
			
				forma.setMotivosSircEliminado(indices[i]+""+numRegMapEliminados, forma.getMotivosSirc(indices[i]+""+forma.getPosEliminar()));
			}
			forma.setMotivosSircEliminado("numRegistros", (numRegMapEliminados+1));
		}
		
		//acomodar los registros del mapa en su nueva posicion
		for(int i=forma.getPosEliminar();i<ultimaPosMapa;i++)
		{
			for(int j=0;j<indices.length;j++)
			{
				forma.setMotivosSirc(indices[j]+""+i,forma.getMotivosSirc(indices[j]+""+(i+1)));
			}
		}
		
		//ahora eliminamos el ultimo registro del mapa.
		for(int j=0;j<indices.length;j++)
		{
			forma.getMotivosSirc().remove(indices[j]+""+ultimaPosMapa);
		}
		
		//ahora actualizamo el numero de registros en el mapa.
		forma.setMotivosSirc("numRegistros",ultimaPosMapa);
		return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getMotivosSirc("numRegistros").toString()), response, request, "motivosSirc.jsp",forma.getPosEliminar()==ultimaPosMapa);

		
	}


	/**
	 * 
	 * @param forma
	 */
	private void accionNuevoMotivo(MotivosSircForm forma)
	{
		int pos=Integer.parseInt(forma.getMotivosSirc("numRegistros")+"");
		forma.setMotivosSirc("consecutivo_"+pos,"");
		forma.setMotivosSirc("codigo_"+pos,"");
		forma.setMotivosSirc("descripcion_"+pos,"");
		forma.setMotivosSirc("tipomotivo_"+pos,"");
		forma.setMotivosSirc("activo_"+pos,"true");
		forma.setMotivosSirc("tiporegistro_"+pos,"MEM");
		forma.setMotivosSirc("numRegistros", (pos+1)+"");
	}

	/**
	 * 
	 * @param con
	 * @param mundo 
	 * @param forma 
	 * @param usuario 
	 */
	private void accionConsultarMotivosSIRC(Connection con, MotivosSircForm forma, MotivosSirc mundo, UsuarioBasico usuario)
	{
		HashMap vo=new HashMap();
		vo.put("institucion", usuario.getCodigoInstitucion());
		forma.setMotivosSirc(mundo.consultarMotivoSirc(con, vo));
	}

}
