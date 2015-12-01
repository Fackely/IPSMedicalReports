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
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.administracion.TiposMonedaAction;
import com.princetonsa.actionform.facturacion.ParamArchivoPlanoColsanitasForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.ParamArchivoPlanoColsanitas;

/**
 * 
 * @author Andr&eacute;s Mauricio Guerrero Toro
 *
 */
public class ParamArchivoPlanoColsanitasAction extends Action {
	
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private Logger logger=Logger.getLogger(TiposMonedaAction.class);
	
	/**
	 * Indices
	 */
	String[] indices={"convenio_","unidad_","compania_","plan_","estabd_"};
	
	public ActionForward execute(ActionMapping mapping,ActionForm form,HttpServletRequest request,HttpServletResponse response)throws Exception
	{
		Connection con=null;
		try{
			if(form instanceof ParamArchivoPlanoColsanitasForm)
			{
				ParamArchivoPlanoColsanitasForm forma=(ParamArchivoPlanoColsanitasForm) form;
				String estado=forma.getEstado();
				logger.info("Estado-->>"+estado);
				con=UtilidadBD.abrirConexion();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());
				ParamArchivoPlanoColsanitas mundo=new ParamArchivoPlanoColsanitas();
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
					forma.resetConvenios();
					forma.setMaxPageItems(Utilidades.convertirAEntero(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
					forma.setConvenios(Utilidades.obtenerConvenios(con, "","",false,"",false));
					forma.setArchivoPlanoColsanitasMap(mundo.consultarArchivoPlanoColsanitas(con, ConstantesBD.codigoNuncaValido));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("consultar"))
				{

					if (Utilidades.convertirAEntero(forma.getConvenioCodigo()+"")==ConstantesBD.codigoNuncaValido)
					{
						forma.setArchivoPlanoColsanitasMap(mundo.consultarArchivoPlanoColsanitas(con, forma.getConvenioCodigo()));
						UtilidadBD.closeConnection(con);
						return mapping.findForward("principal");
					}
					else
					{
						forma.reset();
						forma.setArchivoPlanoColsanitasMap(mundo.consultarArchivoPlanoColsanitas(con, ConstantesBD.codigoNuncaValido));
						this.accionNuevo(forma);
						UtilidadBD.closeConnection(con);
						return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), forma.getMaxPageItems(), Integer.parseInt(forma.getArchivoPlanoColsanitasMap("numRegistros")+""), response, request, "paramArchivoPlanoColsanitas.jsp", true);
					}
					/*
				forma.setArchivoPlanoColsanitasMap(mundo.consultarArchivoPlanoColsanitas(con, forma.getConvenioCodigo()));
				int numReg=Utilidades.convertirAEntero(forma.getArchivoPlanoColsanitasMap().get("numRegistros")+"");
				if(numReg>0)
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else
				{
					if(Utilidades.convertirAEntero(forma.getConvenioCodigo()+"")!=ConstantesBD.codigoNuncaValido)
					{
						forma.reset();
						forma.setArchivoPlanoColsanitasMap(mundo.consultarArchivoPlanoColsanitas(con, ConstantesBD.codigoNuncaValido));
						numReg=Utilidades.convertirAEntero(forma.getArchivoPlanoColsanitasMap().get("numRegistros")+"");
						this.accionNuevo(forma);
						UtilidadBD.closeConnection(con);
					}
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), forma.getMaxPageItems(), Integer.parseInt(forma.getArchivoPlanoColsanitasMap("numRegistros")+""), response, request, "paramArchivoPlanoColsanitas.jsp", true);
				}*/
				}
				else if(estado.equals("guardar"))
				{
					this.accionGuardar(con,forma,mundo,usuario,mapping);
					forma.setMaxPageItems(Utilidades.convertirAEntero(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("eliminar"))
				{
					UtilidadBD.closeConnection(con);
					Utilidades.eliminarRegistroMapaGenerico(forma.getArchivoPlanoColsanitasMap(), forma.getArchivoPlanoColsanitasEliminadosMap(), forma.getIndiceEliminado(), indices, "numRegistros", "estabd_", ConstantesBD.acronimoSi, false);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(), forma.getMaxPageItems(), Utilidades.convertirAEntero(forma.getArchivoPlanoColsanitasMap("numRegistros")+""), response, request, "paramArchivoPlanoColsanitas.jsp", forma.getIndiceEliminado()==(Utilidades.convertirAEntero(forma.getArchivoPlanoColsanitasMap("numRegistros")+"")));
				}
				else if(estado.equals("continuar"))
				{
					UtilidadBD.closeConnection(con);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getArchivoPlanoColsanitasMap().get("numRegistros").toString()), response, request, "paramArchivoPlanoColsanitas.jsp",true);
				}
				else if (estado.equals("redireccion"))
				{
					UtilidadBD.closeConnection(con);
					if(UtilidadTexto.isEmpty(forma.getLinkSiguiente()))
						forma.setLinkSiguiente("../paramArchivoPlanoColsanitas/paramArchivoPlanoColsanitas.do?estado=continuar");
					response.sendRedirect(forma.getLinkSiguiente());
					return null;
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
	 * Accion para crear un nuevo registro en la tabla si despues de la consulta el convenio solicitado no estaba en la base de datos
	 * @param forma
	 */
	private void accionNuevo(ParamArchivoPlanoColsanitasForm forma)
	{
		int pos=Utilidades.convertirAEntero(forma.getArchivoPlanoColsanitasMap().get("numRegistros")+"");
		forma.setArchivoPlanoColsanitasMap("convenio_"+pos,forma.getConvenioCodigo());
		forma.setArchivoPlanoColsanitasMap("unidad_"+pos, "");
		forma.setArchivoPlanoColsanitasMap("compania_"+pos, "");
		forma.setArchivoPlanoColsanitasMap("plan_"+pos, "");
		forma.setArchivoPlanoColsanitasMap("estabd_"+pos, ConstantesBD.acronimoNo);
		forma.setArchivoPlanoColsanitasMap("numRegistros", (pos+1)+"");
	}
	
	/**
	 * Accion que realiza la eliminacion, modificacion e insercion de la parametrizacion de los archivos planos colsanitas segun sea el caso
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 */
	private void accionGuardar(Connection con,ParamArchivoPlanoColsanitasForm forma,ParamArchivoPlanoColsanitas mundo,UsuarioBasico usuario,ActionMapping mapping)
	{
		//logger.info("\n \n********************************* **********************************");
		//logger.info("\n el valor del los datos es "+forma.getArchivoPlanoColsanitasMap());
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		int numReg=Utilidades.convertirAEntero(forma.getArchivoPlanoColsanitasMap("numRegistros")+"");
		//ELIMINAR
		for(int i=0;i<Utilidades.convertirAEntero(forma.getArchivoPlanoColsanitasEliminadosMap("numRegistros")+"");i++)
		{
			if(mundo.eliminarArchivoPlanoColsanitas(con, Utilidades.convertirAEntero(forma.getArchivoPlanoColsanitasEliminadosMap("convenio_"+i)+"")))
			{
				Utilidades.generarLogGenerico(forma.getArchivoPlanoColsanitasEliminadosMap(), forma.getArchivoPlanoColsanitasMap(), usuario.getLoginUsuario(), true, i, ConstantesBD.logArchivoPlanoColsanitasCodigo, indices);
				transaccion=true;
			}
		}
		for(int i=0;i<numReg;i++)
		{
			//MODIFICAR
			HashMap mapaTemp=mundo.consultarArchivoPlanoColsanitas(con, Utilidades.convertirAEntero(forma.getArchivoPlanoColsanitasMap("convenio_"+i)+""));
			
			if((forma.getArchivoPlanoColsanitasMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoSi)&&this.existeModificacion(con,forma.getArchivoPlanoColsanitasMap(),mapaTemp,i,usuario,indices))
			{
				HashMap vo=new HashMap();
				vo.put("unidad", forma.getArchivoPlanoColsanitasMap("unidad_"+i));
				vo.put("compania", forma.getArchivoPlanoColsanitasMap("compania_"+i));
				vo.put("plan", forma.getArchivoPlanoColsanitasMap("plan_"+i));
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("usuario", usuario.getLoginUsuario());
				vo.put("convenio", forma.getArchivoPlanoColsanitasMap("convenio_"+i));
				transaccion=mundo.modificarArchivoPlanoColsanitas(con, vo);
			}
			//INSERTAR
			else if((forma.getArchivoPlanoColsanitasMap("estabd_"+i)+"").trim().equals(ConstantesBD.acronimoNo))
			{
				HashMap vo=new HashMap();
				vo.put("convenio", forma.getConvenioCodigo());
				vo.put("unidad", forma.getArchivoPlanoColsanitasMap("unidad_"+i));
				vo.put("compania", forma.getArchivoPlanoColsanitasMap("compania_"+i));
				vo.put("plan", forma.getArchivoPlanoColsanitasMap("plan_"+i));
				vo.put("institucion", usuario.getCodigoInstitucion());
				vo.put("usuario", usuario.getLoginUsuario());
				transaccion=mundo.insertarArchivoPlanoColsanitas(con, vo);
			}
		}
		if(transaccion)
		{
			forma.setMensaje(new ResultadoBoolean(true,"OPERACION REALIZADA CON EXITO!!!"));
			forma.setArchivoPlanoColsanitasMap(mundo.consultarArchivoPlanoColsanitas(con, ConstantesBD.codigoNuncaValido));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMensaje(new ResultadoBoolean(true,"NO SE PUDO INSERTAR EL REGISTRO."));
			UtilidadBD.abortarTransaccion(con);
		}
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
	private boolean existeModificacion(Connection con,HashMap mapa,HashMap mapaTemp,int pos,UsuarioBasico usuario,String[] indices)
	{
	
		for(int i=0;i<indices.length;i++)
		{
			if(mapaTemp.containsKey(indices[i]+"0")&&mapa.containsKey(indices[i]+""+pos))
			{
				if(!((mapaTemp.get(indices[i]+"0")+"").trim().equals((mapa.get(indices[i]+""+pos)+"").trim())))
				{
					Utilidades.generarLogGenerico(mapa, mapaTemp, usuario.getLoginUsuario(), false, pos, ConstantesBD.logArchivoPlanoColsanitasCodigo, indices);
					return true;
				}
			}
		}
		return false;
	}
	
}
