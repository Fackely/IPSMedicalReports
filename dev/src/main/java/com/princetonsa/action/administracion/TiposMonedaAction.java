package com.princetonsa.action.administracion;

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
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.administracion.TiposMonedaForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.administracion.TiposMoneda;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class TiposMonedaAction extends Action 
{
	
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private Logger logger=Logger.getLogger(TiposMonedaAction.class);
	
	/**
	 * Indices
	 */
	String[] indices={"codigomoneda_","descripcion_","simbolo_","estabd_","codigo_"};

	
	/**
	 * Metodo excecute del action
	 */
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{
		if(form instanceof TiposMonedaForm)
		{
			TiposMonedaForm forma=(TiposMonedaForm) form;
			String estado=forma.getEstado();
			logger.info("Estado-->> "+estado);
			con=UtilidadBD.abrirConexion();
			UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
			TiposMoneda mundo=new TiposMoneda();
			forma.setMensaje(new ResultadoBoolean(false));
			if(estado==null)
			{
				logger.warn("Estado no valido dentro del flujo de Tipod de Moneda (null)");
				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
			else if(estado.equals("empezar"))
			{
				forma.reset();
				forma.setMaxPageItems(Utilidades.convertirAEntero(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
				this.accionConsultarTiposMoneda(con,forma,mundo);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("principal");
			}
			else if(estado.equals("continuar"))
			{
				UtilidadBD.closeConnection(con);
				return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getTiposMonedaMap("numRegistros").toString()), response, request, "tiposMoneda.jsp",true);
				
			}
			else if (estado.equals("redireccion"))
			{
				UtilidadBD.closeConnection(con);
				if(UtilidadTexto.isEmpty(forma.getLinkSiguiente()))
					forma.setLinkSiguiente("../tiposMoneda/tiposMoneda.do?estado=continuar");
				response.sendRedirect(forma.getLinkSiguiente());
				return null;
			}
			else if(estado.equals("nuevoRegistro"))
			{
				this.accionNuevoTipoMoneda(forma);
				UtilidadBD.closeConnection(con);
				return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), forma.getMaxPageItems(), Utilidades.convertirAEntero(forma.getTiposMonedaMap().get("numRegistros")+""), response, request, "tiposMoneda.jsp", true);
			}
			else if(estado.equals("guardar"))
			{
				this.accionGuardarTiposMoneda(con,forma,mundo,usuario,mapping,request);
				forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
				this.accionConsultarTiposMoneda(con, forma, mundo);
				UtilidadBD.closeConnection(con);
				return mapping.findForward("principal");
			}
			else if(estado.equals("eliminar"))
			{
				UtilidadBD.closeConnection(con);
				Utilidades.eliminarRegistroMapaGenerico(forma.getTiposMonedaMap(), forma.getTiposMonedaEliminadosMap(), forma.getIndiceEliminado(), indices, "numRegistros", "estabd_", ConstantesBD.acronimoSi, false);
				return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), forma.getMaxPageItems(), Utilidades.convertirAEntero(forma.getTiposMonedaMap("numRegistros")+""), response, request, "tiposMoneda.jsp", forma.getIndiceEliminado()==(Utilidades.convertirAEntero(forma.getTiposMonedaMap("numRegistros")+"")));
			}
			else
			{
				forma.reset();
				logger.warn("Estado no valido dentro del flujo de Tipos de Moneda");
				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
				UtilidadBD.closeConnection(con);
				return mapping.findForward("paginaError");
			}
		}
		else
		{
			logger.error("El form no es compatible con el form de Articulo de Catalogo");
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
	 * Accion para la consulta de los tipos de monedas
	 * @param con
	 * @param forma
	 * @param mundo
	 */
	private void accionConsultarTiposMoneda(Connection con,TiposMonedaForm forma,TiposMoneda mundo)
	{
		forma.setTiposMonedaMap(mundo.consultarTiposMoneda(con));
		forma.setTiposMonedaOriginalMap(mundo.consultarTiposMoneda(con));
	}
	
	/**
	 * Generacion de un nuevo registro
	 * @param forma
	 */
	private void accionNuevoTipoMoneda(TiposMonedaForm forma)
	{
		int pos=Utilidades.convertirAEntero(forma.getTiposMonedaMap().get("numRegistros")+"");
		forma.setTiposMonedaMap("codigomoneda_"+pos,"");
		forma.setTiposMonedaMap("descripcion_"+pos,"");
		forma.setTiposMonedaMap("simbolo_"+pos,"");
		forma.setTiposMonedaMap("estabd_"+pos,ConstantesBD.acronimoNo);
		forma.setTiposMonedaMap("numRegistros",(pos+1)+"");
	}
	
	/**
	 * Accion guardar los tipos de monedas
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 */
	private void accionGuardarTiposMoneda(
			Connection con,
			TiposMonedaForm forma,
			TiposMoneda mundo,
			UsuarioBasico usuario,
			ActionMapping mapping,
			HttpServletRequest request)
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		int numReg=Utilidades.convertirAEntero(forma.getTiposMonedaMap().get("numRegistros")+"");
		ActionErrors errores = new ActionErrors();
		//ELIMINAR
		for(int i=0;i<Integer.parseInt(forma.getTiposMonedaEliminadosMap("numRegistros")+"");i++)
		{
			if(mundo.eliminarTiposMoneda(con,Utilidades.convertirAEntero(forma.getTiposMonedaEliminadosMap("codigo_"+i)+"")))
			{
				this.generarLogGenerico(forma.getTiposMonedaEliminadosMap(), new HashMap(), usuario.getLoginUsuario(), true, i, ConstantesBD.logTiposMonedaCodigo, indices);
				transaccion=true;
			}
		}
		for(int i=0;i<numReg;i++)
		{
			//MODIFICAR
			if((forma.getTiposMonedaMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoSi))
			{
				HashMap vo=new HashMap();
				
				/*
				 * Validación del campo Simbolo para que no exceda los 4 caracteres definidos en la tabla
				 * simbolo_
				 */
				HashMap<String, Object> criterios = new HashMap<String, Object>();
				criterios.put("simbolo", forma.getTiposMonedaMap("simbolo_"+i));
				String simbolo=criterios.get("simbolo")+"";
				int longitudSimbolo = simbolo.length();
				logger.info("===> accionGuardarTiposMoneda: Simbolo: "+simbolo);
				logger.info("===> accionGuardarTiposMoneda: Longitud Simbolo: "+longitudSimbolo);
				if(longitudSimbolo<=4)
				{
					logger.info("===> accionGuardarTiposMoneda: La longitud del Simbolo es MENOR o IGUAL que 4");
					forma.setSimboloMenorA4(true);
					
					/*
					 * Flujo normal de la acción
					 */
					vo.put("codigomoneda", forma.getTiposMonedaMap("codigomoneda_"+i));
					vo.put("descripcion", forma.getTiposMonedaMap("descripcion_"+i));
					vo.put("simbolo", forma.getTiposMonedaMap("simbolo_"+i));
					vo.put("institucion", usuario.getCodigoInstitucion());
					vo.put("usuario", usuario.getLoginUsuario());
					vo.put("codigo", forma.getTiposMonedaMap("codigo_"+i));
					transaccion=mundo.modificarTiposMoneda(con, vo);
					
					logger.info("MODIFICADO--->"+forma.getTiposMonedaMap());
					logger.info("original--->"+forma.getTiposMonedaOriginalMap());
					
					this.generarLogGenerico(forma.getTiposMonedaMap(), forma.getTiposMonedaOriginalMap(), usuario.getLoginUsuario(), false, i, ConstantesBD.logTiposMonedaCodigo, indices);
				}
				else
				{
					logger.info("===> accionGuardarTiposMoneda: La longitud del Texto es MAYOR que 4");
					forma.setSimboloMenorA4(false);
					errores.add("Simbolo", new ActionMessage(
							"errors.required", "El Símbolo: "+simbolo+" No Puede Sobrepasar los 4 Caracteres, "));
				}
			}
			//INSERTAR
			else if((forma.getTiposMonedaMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoNo))
			{
				/*
				 * Validación del campo Simbolo para que no exceda los 4 caracteres definidos en la tabla
				 * simbolo_
				 */
				HashMap<String, Object> criterios = new HashMap<String, Object>();
				criterios.put("simbolo", forma.getTiposMonedaMap("simbolo_"+i));
				String simbolo=criterios.get("simbolo")+"";
				int longitudSimbolo = simbolo.length();
				logger.info("===> accionGuardarTiposMoneda: Simbolo: "+simbolo);
				logger.info("===> accionGuardarTiposMoneda: Longitud Simbolo: "+longitudSimbolo);
				if(longitudSimbolo<=4)
				{
					logger.info("===> accionGuardarTiposMoneda: La longitud del Simbolo es MENOR o IGUAL que 4");
					forma.setSimboloMenorA4(true);
					
					/*
					 * Flujo normal de la acción
					 */
					HashMap vo=new HashMap();
					vo.put("codigomoneda", forma.getTiposMonedaMap("codigomoneda_"+i));
					vo.put("descripcion", forma.getTiposMonedaMap("descripcion_"+i));
					vo.put("simbolo", forma.getTiposMonedaMap("simbolo_"+i));
					vo.put("institucion", usuario.getCodigoInstitucion());
					vo.put("usuario", usuario.getLoginUsuario());
					transaccion=mundo.insertarTiposMoneda(con, vo);
					
					this.generarLogGenerico(forma.getTiposMonedaMap(), forma.getTiposMonedaOriginalMap(), usuario.getLoginUsuario(), false, i, ConstantesBD.logTiposMonedaCodigo, indices);
				}
				else
				{
					logger.info("===> accionGuardarTiposMoneda: La longitud del Texto es MAYOR que 4");
					forma.setSimboloMenorA4(false);
					errores.add("Simbolo", new ActionMessage(
							"errors.required", "El Símbolo: "+simbolo+" No Puede Sobrepasar los 4 Caracteres, "));
				}
			}
		}
		if(transaccion)
		{
			if(!errores.isEmpty())
			{
				logger.info("===> Entré al if de accionGuardarTiposMoneda, Si hay errores");
				saveErrors(request, errores);
			}
			else
			{
				forma.setMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!"));
				UtilidadBD.finalizarTransaccion(con);
			}
		}
		else
		{
			forma.setMensaje(new ResultadoBoolean(true,"NO SE PUDO INSERTAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
	}
	
	/**
	 * Metodo para generar los logs sencillos dinamicamentem, recibe 2 mapas con la informacion antes y despues de la modificacion.
	 * en caso de ser un log eliminacion, solo es necesario enviar el mapa de registros eliminados. 
	 * @param mapaModificado
	 * @param mapaOriginal
	 * @param usuario
	 * @param isEliminacion
	 * @param posicionRegModificado
	 * @param constanteTipoArchivo
	 * @param indices 
	 */
	public static void generarLogGenerico(HashMap mapaModificado, HashMap mapaOriginal, String usuario, boolean isEliminacion, int posicionRegModificado, int constanteTipoArchivo, String[] indices)
	{
		String log = "";
		int tipoLog=0;
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ "+mapaModificado.get(indices[i]+""+posicionRegModificado)+" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +mapaOriginal.get(indices[i]+""+posicionRegModificado) + " ]";
			}
			log += 		 "\n   ============INFORMACION MODIFICADA=========== ";
			for(int i=0;i<(indices.length-1);i++)
			{
				log+= "\n  "+indices[i].replace("_", " ")+" [ " +mapaModificado.get(indices[i]+""+posicionRegModificado) +" ]";
			}
			log+= "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		LogsAxioma.enviarLog(constanteTipoArchivo,log,tipoLog,usuario);
	}

}
