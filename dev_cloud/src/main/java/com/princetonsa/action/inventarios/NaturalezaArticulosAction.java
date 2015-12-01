package com.princetonsa.action.inventarios;

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

import com.princetonsa.actionform.inventarios.NaturalezaArticulosForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.NaturalezaArticulos;


public class NaturalezaArticulosAction extends Action
{
	
	
	Logger logger =Logger.getLogger(NaturalezaArticulosAction.class);
	
	
	String[] indices={"acronimo_","institucion_","nombre_","espos_","codigorips_","codigo_interfaz_","tiporegistro_", "esmedicamento_"};
	

	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{

		Connection con=null;
		try{
			if (form instanceof NaturalezaArticulosForm) 
			{
				NaturalezaArticulosForm forma=(NaturalezaArticulosForm) form;

				String estado=forma.getEstado();

				logger.info("Estado -->"+estado);

				con=UtilidadBD.abrirConexion();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());


				NaturalezaArticulos mundo=new NaturalezaArticulos();


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
					this.accionConsultarNaturalezaArticulos(con, forma, mundo);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("continuar"))
				{
					logger.info("mapa-->"+forma.getNaturalezaArticulosMap());
					UtilidadBD.closeConnection(con);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getNaturalezaArticulosMap("numRegistros").toString()), response, request, "naturalezaArticulos.jsp",true);

				}
				else if (estado.equals("redireccion"))
				{
					logger.info("mapa-->"+forma.getNaturalezaArticulosMap());
					UtilidadBD.closeConnection(con);
					if(UtilidadTexto.isEmpty(forma.getLinkSiguiente()))
						forma.setLinkSiguiente("../naturalezaArticulos/naturalezaArticulos.do?estado=continuar");
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
				}
				else if(estado.equals("nuevo"))
				{
					this.accionNuevoNaturalezaArticulos(forma);
					UtilidadBD.closeConnection(con);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getNaturalezaArticulosMap("numRegistros").toString()), response, request, "naturalezaArticulos.jsp",true);
				}
				else if(estado.equals("eliminar"))
				{
					UtilidadBD.closeConnection(con);
					Utilidades.eliminarRegistroMapaGenerico(forma.getNaturalezaArticulosMap(),forma.getNaturalezaArticulosEliminadosMap(),forma.getPosEliminar(), indices, "numRegistros", "tiporegistro_", ConstantesBD.acronimoSi, false);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getNaturalezaArticulosMap("numRegistros").toString()), response, request, "naturalezaArticulos.jsp",forma.getPosEliminar()==(Integer.parseInt(forma.getNaturalezaArticulosMap("numRegistros")+"")));
				}
				else if(estado.equals("guardar"))
				{

					//guardamos en BD.
					this.accionGuardarRegistros(con,forma,mundo,usuario);

					//limipiamos el form
					forma.reset();
					forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
					this.accionConsultarNaturalezaArticulos(con,forma,mundo);
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
					logger.warn("Estado no valido dentro del flujo de NATURALEZA DE ARTICULOS ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de NaturalezaArticulosForm");
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
	
	
	private void accionGuardarRegistros(Connection con, NaturalezaArticulosForm forma, NaturalezaArticulos mundo, UsuarioBasico usuario)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		logger.info("mapaNaturalezaArticulos-->"+forma.getNaturalezaArticulosMap());
		
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getNaturalezaArticulosEliminadosMap("numRegistros")+"");i++)
		{
			if(mundo.eliminarRegistro(con,Integer.parseInt(forma.getNaturalezaArticulosEliminadosMap("institucion_"+i)+""), forma.getNaturalezaArticulosEliminadosMap("acronimo_"+i)+""))
			{
				this.generarLog(forma, mundo, usuario, true, i);
				transaccion=true;
			}
		}
		
		for(int i=0;i<Integer.parseInt(forma.getNaturalezaArticulosMap("numRegistros")+"");i++)
		{
			//modificar
			if((forma.getNaturalezaArticulosMap("tiporegistro_"+i)+"").trim().equals(ConstantesBD.acronimoSi)&&this.existeModificacion(con,forma,mundo,i,usuario,forma.getNaturalezaArticulosMap("acronimo_"+i)+""))
			{
				HashMap vo=new HashMap();
				vo.put("acronimo",forma.getNaturalezaArticulosMap("acronimo_"+i));
				vo.put("nombre",forma.getNaturalezaArticulosMap("nombre_"+i));
				vo.put("es_pos",forma.getNaturalezaArticulosMap("espos_"+i));
				vo.put("codigo_rips",forma.getNaturalezaArticulosMap("codigorips_"+i));
				vo.put("codigo_interfaz",forma.getNaturalezaArticulosMap("codigo_interfaz_"+i));
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("usuario_modifica",usuario.getLoginUsuario());
				vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				vo.put("hora_modifica",UtilidadFecha.getHoraActual());
				vo.put("esmedicamento", forma.getNaturalezaArticulosMap("esmedicamento_"+i));
				transaccion=mundo.modificar(con, vo);
			}
			
			//insertar
			else if((forma.getNaturalezaArticulosMap("tiporegistro_"+i)+"").trim().equals(ConstantesBD.acronimoNo))
			{
				HashMap vo=new HashMap();
				vo.put("acronimo",forma.getNaturalezaArticulosMap("acronimo_"+i));
				vo.put("nombre",forma.getNaturalezaArticulosMap("nombre_"+i));
				vo.put("es_pos",forma.getNaturalezaArticulosMap("espos_"+i));
				vo.put("codigo_rips",forma.getNaturalezaArticulosMap("codigorips_"+i));
				vo.put("codigo_interfaz",forma.getNaturalezaArticulosMap("codigo_interfaz_"+i));
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("usuario_modifica",usuario.getLoginUsuario());
				vo.put("fecha_modifica",UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
				vo.put("hora_modifica",UtilidadFecha.getHoraActual());
				vo.put("esmedicamento", forma.getNaturalezaArticulosMap("esmedicamento_"+i));
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
	 * Generar Log
	 * */
	private void generarLog(NaturalezaArticulosForm forma, NaturalezaArticulos mundo, UsuarioBasico usuario, boolean isEliminacion, int pos) 
	{
		String log = "";
		int tipoLog=0;
		
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ "+(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(forma.getNaturalezaArticulosEliminadosMap(indices[i]+""+pos)+""):forma.getNaturalezaArticulosEliminadosMap(indices[i]+""+pos))+" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(mundo.getNaturalezaArticulosMap().get(indices[i]+"0")+""):mundo.getNaturalezaArticulosMap().get(indices[i]+"0")) + " ]";
			}
			log += 		 "\n   ============INFORMACION MODIFICADA=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +(indices[i].trim().equals("tipomotivo_")?ValoresPorDefecto.getIntegridadDominio(forma.getNaturalezaArticulosMap(indices[i]+""+pos)+""):forma.getNaturalezaArticulosMap(indices[i]+""+pos)) +" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		
		LogsAxioma.enviarLog(ConstantesBD.logNaturalezaArticulosCodigo,log,tipoLog,usuario.getLoginUsuario());
	}
	
	
	/**
	 * Existe modificacion
	 * */
	private boolean existeModificacion(Connection con, NaturalezaArticulosForm forma, NaturalezaArticulos mundo,int pos, UsuarioBasico usuario, String acronimo)
	{
		HashMap temp=mundo.consultarNaturalezaArticulosEspecifico(con, acronimo);
		for(int i=0;i<indices.length;i++)
		{
			if(temp.containsKey(indices[i]+"0")&&forma.getNaturalezaArticulosMap().containsKey(indices[i]+""+pos))
			{
				if(!((temp.get(indices[i]+"0")+"").trim().equals((forma.getNaturalezaArticulosMap(indices[i]+""+pos)+"").trim())))
				{
					mundo.setNaturalezaArticulosMap(temp);
					this.generarLog(forma, mundo, usuario, false, pos);
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Accion Nueva Naturaleza de Articulo
	 * */
	private void accionNuevoNaturalezaArticulos(NaturalezaArticulosForm forma)
	{
		int pos=Integer.parseInt(forma.getNaturalezaArticulosMap("numRegistros")+"");
		forma.setNaturalezaArticulosMap("acronimo_"+pos,"");
		forma.setNaturalezaArticulosMap("nombre_"+pos,"");
		forma.setNaturalezaArticulosMap("espos_"+pos,"");
		forma.setNaturalezaArticulosMap("codigorips_"+pos,"");
		forma.setNaturalezaArticulosMap("codigo_interfaz_"+pos,"");
		forma.setNaturalezaArticulosMap("institucion_"+pos,"");	
		forma.setNaturalezaArticulosMap("tiporegistro_"+pos,ConstantesBD.acronimoNo);
		forma.setNaturalezaArticulosMap("esmedicamento_"+pos, ConstantesBD.acronimoNo);
		forma.setNaturalezaArticulosMap("numRegistros", (pos+1)+"");
		
	}
	
	/**
	 * Accion ordenar mapa
	 * */
	private void accionOrdenarMapa(NaturalezaArticulosForm forma)
	{
		
		int numReg=Integer.parseInt(forma.getNaturalezaArticulosMap("numRegistros")+"");
		forma.setNaturalezaArticulosMap(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getNaturalezaArticulosMap(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setNaturalezaArticulosMap("numRegistros",numReg+"");
	}
	
	
	/**
	 * Accion consuLtar Naturaleza Articulos
	 */
	private void accionConsultarNaturalezaArticulos(Connection con, NaturalezaArticulosForm forma, NaturalezaArticulos mundo)
	{
		mundo.consultarNaturalezaArticulosExistentes(con);
		forma.setNaturalezaArticulosMap((HashMap)mundo.getNaturalezaArticulosMap().clone());
	}
	
}
