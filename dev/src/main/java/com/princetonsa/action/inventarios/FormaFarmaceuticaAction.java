package com.princetonsa.action.inventarios;

import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import util.inventarios.UtilidadInventarios;

import com.princetonsa.actionform.inventarios.FormaFarmaceuticaForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.FormaFarmaceutica;

public class FormaFarmaceuticaAction extends Action
{

	
/////
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(FormaFarmaceuticaAction.class);
	
	/**
	 * Método execute del action
	 */
	public ActionForward execute(	ActionMapping mapping, 	
													        ActionForm form, 
													        HttpServletRequest request, 
													        HttpServletResponse response) throws Exception
													        {
		Connection con = null;
		try{


			if(form instanceof FormaFarmaceuticaForm)
			{


				//intentamos abrir una conexion con la fuente de datos 
				con = UtilidadBD.abrirConexion(); 
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				FormaFarmaceuticaForm forma = (FormaFarmaceuticaForm) form;
				FormaFarmaceutica mundo= new FormaFarmaceutica();

				UsuarioBasico usuario = getUsuarioBasicoSesion(request.getSession());
				String estado = forma.getEstado();
				logger.warn("estado [Formas Farmaceuticas]-->"+estado);
				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de FormasFarmaceuticasAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}			
				//***********************FORMAS FARMACEUTICAS************************************
				else if (estado.equals("empezar"))
				{
					return accionEmpezar(con,forma,mundo,usuario,mapping);
				}
				else if (estado.equals("redireccion"))
				{
					return accionRedireccion(con,forma,response);
				}
				else if(estado.equals("nuevo"))
				{
					this.accionNuevoRegistro(forma);
					UtilidadBD.cerrarConexion(con);
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),Integer.parseInt(forma.getFormasFarma("numRegistros").toString()), response, request, "formasFarmaceuticas.jsp",true);
				}
				else if(estado.equals("guardar"))
				{
					this.accionGuardar(con,forma,mundo,usuario);
					forma.reset();
					mundo.reset();
					try
					{
						forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
					}
					catch(Exception e)
					{
						forma.setMaxPageItems(10);
					}
					forma.setEstado("guardar");
					this.accionCargarMapaBaseDatos(con,forma,mundo,usuario);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("eliminarForma"))
				{
					this.eliminarRegistro(forma);
					UtilidadBD.cerrarConexion(con);
					int numRegistros = Integer.parseInt(forma.getFormasFarma("numRegistros").toString());
					return UtilidadSesion.redireccionar(forma.getLinkSiguiente(),forma.getMaxPageItems(),numRegistros, response, request, "formasFarmaceuticas.jsp",forma.getRegEliminar()==numRegistros);
				}

				else if(estado.equals("ordenar"))
				{
					this.accionOrdenar(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");
				}

				//***************************************************************************************************
				//*******************VIAS DE ADMINISTRACION**********************************************************
				else if(estado.equals("detalleViasAdmin"))
				{

					forma.setViasAdminForma((HashMap)forma.getFormasFarma("viaAdmin_"+forma.getIndexDetalle()));
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("viasAdmin");
				}
				else if(estado.equals("volver"))
				{
					return accionVolver(con,forma,mapping,request);

				}
				else if(estado.equals("nuevaVia"))
				{
					this.accionNuevaVia(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("viasAdmin");
				}
				else if(estado.equals("eliminarVia"))
				{
					this.eliminarRegistroVia(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("viasAdmin");
				}
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				//*******************************************************************************************************
			}
			else
			{
				logger.error("El form no es compatible con el form de FormaFarmaceuticaForm");
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
	 * Método implementado para retornar al listado de formas farmaceuticas 
	 * @param con
	 * @param forma
	 * @param mapping
	 * @param request 
	 * @return
	 */
	private ActionForward accionVolver(Connection con, FormaFarmaceuticaForm forma, ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		
		
		//Se verifica si hay alguna via de administracion vacía**********************************************************
		HashMap temp=new HashMap();
		temp=(HashMap)forma.getViasAdminForma();
		//temp=(HashMap)forma.getFormasFarma("viaAdmin_"+forma.getIndexDetalle());
		int numRegistros = Integer.parseInt(temp.get("numRegistros")+"");
		for(int j=0;j<numRegistros;j++)
		{
			if((temp.get("viaAdmin_"+j)+"").trim().equals("0"))
				 errores.add("VIA DE ADMINISTRACION REQUERIDO", new ActionMessage("errors.required","La Via de Administracion en el registro N° "+(j+1)));
		}
		
		//Se verifica si hay repetidos**********************************************************
		errores = Listado.validarRegistrosRepetidos(errores, temp, "viaAdmin_", "0", "vías");
		
		
		UtilidadBD.closeConnection(con);
		forma.setFormasFarma("viaAdmin_"+forma.getIndexDetalle(),forma.getViasAdminForma().clone());
		if(errores.isEmpty())
		    return mapping.findForward("paginaPrincipal");
		else
		{
			saveErrors(request, errores);
			return mapping.findForward("viasAdmin");
		}
		
		
	}

	/**
	 * Método implementado para paginar el listado de formas farmacéuticas
	 * @param con
	 * @param forma
	 * @param response
	 * @return
	 */
	private ActionForward accionRedireccion(Connection con, FormaFarmaceuticaForm forma, HttpServletResponse response) 
	{
		try 
		{
			UtilidadBD.closeConnection(con);
			response.sendRedirect(forma.getLinkSiguiente());
		} catch (IOException e) 
		{
			logger.error("Error realizando la redireccion del listado de formas farmaceuticas:" +e);
		}
		return null;
	}

	/**
	 * Método que inicia el flujo de formas farmaceuticas
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, FormaFarmaceuticaForm forma, FormaFarmaceutica mundo, UsuarioBasico usuario, ActionMapping mapping) 
	{
		forma.reset();
		try
		{
			forma.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
		}
		catch(Exception e)
		{
			forma.setMaxPageItems(10);
		}
	    this.accionCargarMapaBaseDatos(con,forma,mundo,usuario);
	    UtilidadBD.closeConnection(con);
	    return mapping.findForward("paginaPrincipal");
	}

	/**
	 * 
	 * @param con 
	 * @param forma
	 * @param mundo 
	 */
	private void eliminarRegistroVia(FormaFarmaceuticaForm forma) 
	{
		int pos=forma.getRegEliminar();
		int numReg=Integer.parseInt(forma.getViasAdminForma("numRegistros")==null?"0":forma.getViasAdminForma("numRegistros")+"");
		int ultimoRegistro=numReg-1;
		for(int i=pos;i<ultimoRegistro;i++)
		{
			
			forma.setViasAdminForma("viaAdmin_"+i,forma.getViasAdminForma("viaAdmin_"+(i+1)));
			
		}
		forma.getViasAdminForma().remove("viaAdmin_"+ultimoRegistro);
		forma.setViasAdminForma("numRegistros",ultimoRegistro+"");
		forma.setFormasFarma("viaAdmin_"+forma.getIndexDetalle(), forma.getViasAdminForma());
	}

	/**
	 * 
	 * @param forma
	 */
	private void accionNuevaVia(FormaFarmaceuticaForm forma) 
	{
		int index=Integer.parseInt(forma.getViasAdminForma("numRegistros")+"");

		forma.setViasAdminForma("viaAdmin_"+index,"-1");
		index++;
		forma.setViasAdminForma("numRegistros",index+"");		
	}

	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenar(FormaFarmaceuticaForm forma) 
	{
		int numReg=Integer.parseInt(forma.getFormasFarma("numRegistros")==null?"0":forma.getFormasFarma("numRegistros")+"");
		String[] indices={"consecutivo_","institucion_","descripcion_","viaAdmin_","existerelacion_","tiporegistro_"};		
		forma.setFormasFarma(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getFormasFarma(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setFormasFarma("numRegistros",numReg+"");

	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardar(Connection con, FormaFarmaceuticaForm forma, FormaFarmaceutica mundo, UsuarioBasico usuario) 
	{
		ActionErrors errores=new ActionErrors();
		int numReg=Integer.parseInt(forma.getFormasFarma("numRegistros")==null?"0":forma.getFormasFarma("numRegistros")+"");
		int regEliminados=Integer.parseInt(forma.getFormasEliminadas("registroseliminados")==null?"0":forma.getFormasEliminadas("registroseliminados")+"");
		boolean enTransaccion=UtilidadBD.iniciarTransaccion(con);
		//se encuentra entre la transaccion
		{
			//eliminar.
			for(int el=0;el<regEliminados;el++)
			{
				
				if(enTransaccion)
				{
					enTransaccion=mundo.eliminarRegistro(con,forma.getFormasEliminadas("consecutivoEliminado_"+el)+"",usuario.getCodigoInstitucionInt());
					
					this.generarLog(con,forma,mundo,usuario,true, el);
				}
				else
				{
					logger.error("ERROR DURANTE LA ELIMINACION DEL REGISTRO "+forma.getFormasEliminadas("consecutivoEliminado_"+el));
					errores.add("REGISTRO USADO", new ActionMessage("error.registroUtilizado","<B>"+forma.getFormasEliminadas("consecutivoEliminado_"+el)+" - "+forma.getFormasEliminadas("descripcionEliminado_"+el)+"</B>","NO SE PUEDE ELIMINAR"));  
				}
			}
			//insertar modificar.
	        for(int k=0;k<numReg;k++)
	        {
	        	if(enTransaccion)
	        	{
		        	//modificar
		        	if((forma.getFormasFarma("tiporegistro_"+k)+"").equals("BD")&&enTransaccion)
		        	{
		        		if(registroModificado(con,forma,mundo,k,usuario))
		        		{
			        		mundo.actualizarRelacionesFormasFarmaViasAdmin(con,forma.getFormasFarma("consecutivo_"+k)+"".toString().trim(),usuario.getCodigoInstitucionInt(),(HashMap) forma.getFormasFarma("viaAdmin_"+k));
		        			enTransaccion=mundo.modificarRegistro(con,forma.getFormasFarma("consecutivo_"+k)+"",forma.getFormasFarma("descripcion_"+k)+"", usuario.getCodigoInstitucionInt());		        			
		        			this.generarLog(con,forma,mundo,usuario,false, k);
		        		}
		        		
		        	}
		        	//insertar
		        	if((forma.getFormasFarma("tiporegistro_"+k)+"").equals("MEM")&& enTransaccion)
		        	{
		        		
		        		enTransaccion=mundo.insertarRegistro(con,forma.getFormasFarma("consecutivo_"+k)+"",usuario.getCodigoInstitucionInt(),forma.getFormasFarma("descripcion_"+k)+"");		        		
		        		mundo.insertarRelacionesFormaFarmaViasAdmin(con,forma.getFormasFarma("consecutivo_"+k)+"".toString(),usuario.getCodigoInstitucionInt(),(HashMap) forma.getFormasFarma("viaAdmin_"+k));
		        	}
	        	} 
	        	else
	        	{
	        		logger.error("error modificando - insertando el registro ---> "+forma.getFormasFarma("tiporegistro_"+k));
	        		k=numReg;
	        	}
	        }
		}
		if(enTransaccion)
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
	 * @param k
	 * @return
	 */
	private boolean registroModificado(Connection con, FormaFarmaceuticaForm forma, FormaFarmaceutica mundo, int pos, UsuarioBasico usuario) 
	{
		
		return mundo.existeModificacion(con,forma.getFormasFarma("consecutivo_"+pos).toString(),usuario.getCodigoInstitucionInt(),forma.getFormasFarma("descripcion_"+pos).toString(),(HashMap) forma.getFormasFarma("viaAdmin_"+pos));
	}

	/**
	 * 
	 * @param con 
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param isEliminacion
	 * @param pos
	 */
	private void generarLog(Connection con, FormaFarmaceuticaForm forma, FormaFarmaceutica mundo, UsuarioBasico usuario, boolean isEliminacion, int pos) 
	{
		String log = "";
		int tipoLog=0;
		if(isEliminacion)
		{


			log = 		 "\n   ============REGISTRO ELIMINADO=========== " +
			 "\n*  Código [" +forma.getFormasEliminadas("consecutivoEliminado_"+pos)+""+"] "+
			 "\n*  Descripción ["+forma.getFormasEliminadas("descripcionEliminado_"+pos)+""+"] " +
			 "\n*  Institucion ["+usuario.getInstitucion()+"] ";
			 /**"\n*  Vías de Administración: ";
			
			//se toman las vías
			HashMap vias = (HashMap) forma.getFormasEliminadas("viaEliminado_"+pos);
			for(int i=0;i<Integer.parseInt(vias.get("numRegistros").toString());i++)
				log += "\n      "+(i+1)+". "+UtilidadInventarios.obtenerNombreViaAdminInstitucion(con, Integer.parseInt(vias.get("viaAdmin_"+i).toString()));**/
			 
			 log+="\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL=========== " +
			 "\n*  Código ["+mundo.getConsecutivo()+"] " +
			 "\n*  Descripción ["+mundo.getDescripcion()+"] " +
			 "\n*  Institucion ["+usuario.getInstitucion()+"] "+
			 "\n*  Vías de Administración: ";
				
			//se toman las vías
			HashMap vias = (HashMap) mundo.getVias();
			for(int i=0;i<Integer.parseInt(vias.get("numRegistros").toString());i++)
				log += "\n      "+(i+1)+". "+UtilidadInventarios.obtenerNombreViaAdminInstitucion(con, Integer.parseInt(vias.get("viaAdmin_"+i).toString()));
			 log+="\n   ===========INFORMACION MODIFICADA========== 	" +
			 "\n*  Código [" +forma.getFormasFarma("consecutivo_"+pos)+""+"] "+
			 "\n*  Descripción ["+forma.getFormasFarma("descripcion_"+pos)+""+"] " +
			 "\n*  Institucion ["+usuario.getInstitucion()+"] "+
			 "\n*  Vías de Administración: ";	
			//se toman las vías
			vias = (HashMap) forma.getFormasFarma("viaAdmin_"+pos);
			for(int i=0;i<Integer.parseInt(vias.get("numRegistros").toString());i++)
				log += "\n      "+(i+1)+". "+UtilidadInventarios.obtenerNombreViaAdminInstitucion(con, Integer.parseInt(vias.get("viaAdmin_"+i).toString()));
			 log+="\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		LogsAxioma.enviarLog(ConstantesBD.logFormaFarmaceuticaCodigo,log,tipoLog,usuario.getLoginUsuario());
		
	}

	/**
	 * 
	 * @param forma
	 */
	private void eliminarRegistro(FormaFarmaceuticaForm forma) 
	{
		int pos=forma.getRegEliminar();
		int numReg=Integer.parseInt(forma.getFormasFarma("numRegistros")==null?"0":forma.getFormasFarma("numRegistros")+"");
		int ultimoRegistro=numReg-1;
		int regEliminados=Integer.parseInt(forma.getFormasEliminadas("registroseliminados")==null?"0":forma.getFormasEliminadas("registroseliminados")+"");
		if((forma.getFormasFarma("tiporegistro_"+pos)+"").equals("BD"))
		{
			forma.setFormasEliminadas("consecutivoEliminado_"+regEliminados,forma.getFormasFarma("consecutivo_"+pos));
			forma.setFormasEliminadas("institucionEliminado_"+regEliminados,forma.getFormasFarma("institucion_"+pos));
			forma.setFormasEliminadas("descripcionEliminado_"+regEliminados,forma.getFormasFarma("descripcion_"+pos));
			forma.setFormasEliminadas("viaEliminado_"+regEliminados,forma.getFormasFarma("viaAdmin_"+pos));
			forma.setFormasEliminadas("registroseliminados",(regEliminados+1)+"");
		}
		for(int i=pos;i<ultimoRegistro;i++)
		{
			
			forma.setFormasFarma("consecutivo_"+i,forma.getFormasFarma("consecutivo_"+(i+1)));
			forma.setFormasFarma("institucion_"+i,forma.getFormasFarma("institucion_"+(i+1)));
			forma.setFormasFarma("descripcion_"+i,forma.getFormasFarma("descripcion_"+(i+1)));
			forma.setFormasFarma("viaAdmin_"+i,forma.getFormasFarma("viaAdmin_"+(i+1)));
			forma.setFormasFarma("tiporegistro_"+i,forma.getFormasFarma("tiporegistro_"+(i+1)));
			forma.setFormasFarma("existerelacion_"+i,forma.getFormasFarma("existerelacion_"+(i+1)));
			
		}
		forma.getFormasFarma().remove("consecutivo_"+ultimoRegistro);
		forma.getFormasFarma().remove("institucion_"+ultimoRegistro);
		forma.getFormasFarma().remove("descripcion_"+ultimoRegistro);
		forma.getFormasFarma().remove("viaAdmin_"+ultimoRegistro);
		forma.getFormasFarma().remove("tiporegistro_"+ultimoRegistro);
		forma.getFormasFarma().remove("existerelacion_"+ultimoRegistro);
		//actualizando numero de registros.
		forma.setFormasFarma("numRegistros",ultimoRegistro+"");
	}

	/**
	 * 
	 * @param forma
	 */
	private void accionNuevoRegistro(FormaFarmaceuticaForm forma) 
	{
		int index=Integer.parseInt(forma.getFormasFarma("numRegistros")+"");
		forma.setFormasFarma("consecutivo_"+index,"");
		forma.setFormasFarma("institucion_"+index,"");
		forma.setFormasFarma("descripcion_"+index,"");
		HashMap mapaTemp=new HashMap();
		mapaTemp.put("numRegistros","0");
		forma.setFormasFarma("viaAdmin_"+index,mapaTemp.clone());
		forma.setFormasFarma("existerelacion_"+index,"false");
		forma.setFormasFarma("tiporegistro_"+index,"MEM");
		index++;
		forma.setFormasFarma("numRegistros",index+"");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionCargarMapaBaseDatos(Connection con, FormaFarmaceuticaForm forma, FormaFarmaceutica mundo, UsuarioBasico usuario) 
	{
		mundo.cargarInformacion(con,usuario.getCodigoInstitucionInt());
		forma.setFormasFarma((HashMap)mundo.getFormas().clone());
	}

	/**
	 * 
	 * @param session
	 * @return
	 */
	private UsuarioBasico getUsuarioBasicoSesion(HttpSession session)
	{
	    UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
			if(usuario == null)
			    logger.warn("El usuario no esta cargado (null)");
			
			return usuario;
	}
	
/////	

}
