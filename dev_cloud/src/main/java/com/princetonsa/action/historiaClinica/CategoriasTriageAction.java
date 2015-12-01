/*
 * @author armando
 */
package com.princetonsa.action.historiaClinica;

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

import com.princetonsa.actionform.historiaClinica.CategoriasTriageForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.CategoriasTriage;

/**
 * 
 * @author artotor
 *
 */
public class CategoriasTriageAction extends Action 
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(CategoriasTriageAction.class);
	
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

			if(form instanceof CategoriasTriageForm)
			{

				//intentamos abrir una conexion con la fuente de datos 
				con = UtilidadBD.abrirConexion(); 
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				CategoriasTriageForm forma = (CategoriasTriageForm) form;
				CategoriasTriage mundo= new CategoriasTriage();
				UsuarioBasico usuario = getUsuarioBasicoSesion(request.getSession());
				String estado = forma.getEstado();
				logger.warn("estado [CategoriasTriageAction.java]-->"+estado);
				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de CategoriasTriageAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}			
				else if (estado.equals("empezar"))
				{
					forma.reset();
					this.accionCargarMapaBaseDatos(con,forma,mundo,usuario);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");
				}

				else if(estado.equals("nuevo"))
				{
					this.accionNuevoRegistro(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");
				}

				else if(estado.equals("eliminarCalificacion"))
				{
					this.eliminarRegistro(con, forma, mundo, usuario);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("guardar"))
				{
					this.accionGuardar(con,forma,mundo,usuario);
					//repetir la consulata y le que se hace en estado empezar
					forma.reset();
					mundo.reset();
					this.accionCargarMapaBaseDatos(con,forma,mundo,usuario);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("ordenar"))
				{
					this.accionOrdenar(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("destinosCategorias"))
				{
					if(Integer.parseInt(forma.getIndexDetalle()+"") != ConstantesBD.codigoNuncaValido)
						forma.setDestinosCategoria((HashMap)forma.getCategorias("destino_"+forma.getIndexDetalle()));

					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("categoriasDestinos");
				}
				else if(estado.equals("volver"))
				{
					forma.setCategorias("destino_"+forma.getIndexDetalle(),forma.getDestinosCategoria().clone());
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("nuevoDestino"))
				{
					this.accionNuevoDestino(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("categoriasDestinos");
				}
				else if(estado.equals("eliminarDestino"))
				{
					this.eliminarRegistroDestino(con,forma,mundo);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("categoriasDestinos");
				}
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de CategoriasTriageForm");
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
	 * @param con 
	 * @param forma
	 * @param mundo 
	 */
	private void eliminarRegistroDestino(Connection con, CategoriasTriageForm forma, CategoriasTriage mundo) 
	{
		int pos=forma.getRegEliminar();
		int numReg=Integer.parseInt(forma.getDestinosCategoria("numRegistros")==null?"0":forma.getDestinosCategoria("numRegistros")+"");
		int ultimoRegistro=numReg-1;
		for(int i=pos;i<ultimoRegistro;i++)
		{
			
			forma.setDestinosCategoria("destino_"+i,forma.getDestinosCategoria("destino_"+(i+1)));
			
		}
		forma.getDestinosCategoria().remove("destino_"+ultimoRegistro);
		forma.setDestinosCategoria("numRegistros",ultimoRegistro+"");
	}

	/**
	 * 
	 * @param forma
	 */
	private void accionNuevoDestino(CategoriasTriageForm forma) 
	{
		int index=Integer.parseInt(forma.getDestinosCategoria("numRegistros")+"");

		forma.setDestinosCategoria("destino_"+index,"-1");
		index++;
		forma.setDestinosCategoria("numRegistros",index+"");		
	}

	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenar(CategoriasTriageForm forma) 
	{
		int numReg=Integer.parseInt(forma.getCategorias("numRegistros")==null?"0":forma.getCategorias("numRegistros")+"");
		String[] indices={"consecutivo_","codigo_","institucion_","descripcion_","color_","destino_","existerelacion_","tiporegistro_"};
		forma.setCategorias(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getCategorias(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setCategorias("numRegistros",numReg+"");

	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardar(Connection con, CategoriasTriageForm forma, CategoriasTriage mundo, UsuarioBasico usuario) 
	{
		ActionErrors errores=new ActionErrors();
		int numReg=Integer.parseInt(forma.getCategorias("numRegistros")==null?"0":forma.getCategorias("numRegistros")+"");
		int regEliminados=Integer.parseInt(forma.getCategoriasEliminadas("registroseliminados")==null?"0":forma.getCategoriasEliminadas("registroseliminados")+"");
		boolean enTransaccion=UtilidadBD.iniciarTransaccion(con);
		//se encuentra entre la transaccion
		{
			//eliminar.
			for(int el=0;el<regEliminados;el++)
			{
				
				if(enTransaccion)
				{
					enTransaccion=mundo.eliminarRegistro(con,Integer.parseInt(forma.getCategoriasEliminadas("consecutivoEliminado_"+el)+""));
					this.generarLog(forma,mundo,usuario,true, el);
				}
				else
				{
					logger.error("ERROR DURANTE LA ELIMINACION DEL REGISTRO "+forma.getCategoriasEliminadas("consecutivoEliminado_"+el));
					errores.add("REGISTRO USADO", new ActionMessage("error.registroUtilizado","<B>"+forma.getCategoriasEliminadas("codigoEliminado_"+el)+" - "+forma.getCategoriasEliminadas("descripcionEliminado_"+el)+"</B>","NO SE PUEDE ELIMINAR"));  
				}
			}
			//insertar modificar.
	        for(int k=0;k<numReg;k++)
	        {
	        	if(enTransaccion)
	        	{
		        	//modificar
		        	if((forma.getCategorias("tiporegistro_"+k)+"").equals("BD")&&enTransaccion)
		        	{
		        		mundo.actualizarRelacionesCategoriasDestinos(con,Integer.parseInt(forma.getCategorias("consecutivo_"+k)+""),(HashMap) forma.getCategorias("destino_"+k));
		        		if(registroModificado(con,forma,mundo,k))
		        		{
		        			enTransaccion=mundo.modificarRegistro(con,Integer.parseInt(forma.getCategorias("consecutivo_"+k)+""),forma.getCategorias("codigo_"+k)+"",forma.getCategorias("descripcion_"+k)+"",Integer.parseInt(forma.getCategorias("color_"+k)+""));
		        			this.generarLog(forma,mundo,usuario,false, k);
		        		}
		        	}
		        	//insertar
		        	if((forma.getCategorias("tiporegistro_"+k)+"").equals("MEM")&& enTransaccion)
		        	{
		        		
		        		enTransaccion=mundo.insertarRegistro(con,forma.getCategorias("codigo_"+k)+"",usuario.getCodigoInstitucionInt(),forma.getCategorias("descripcion_"+k)+"",Integer.parseInt(forma.getCategorias("color_"+k)+""));
		        		mundo.cargarConsecutivoCategoriaTriage(con,forma.getCategorias("codigo_"+k)+"",usuario.getCodigoInstitucionInt(),forma.getCategorias("descripcion_"+k)+"",Integer.parseInt(forma.getCategorias("color_"+k)+""));
		        		mundo.insertarRelacionesCategoriasDestinos(con,mundo.getConsecutivo(),(HashMap) forma.getCategorias("destino_"+k));
		        	}
	        	} 
	        	else
	        	{
	        		logger.error("error modificando - insertando el registro ---> "+forma.getCategorias("tiporegistro_"+k)+" codigo-->"+forma.getCategorias("codigo_"+k));
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
	private boolean registroModificado(Connection con, CategoriasTriageForm forma, CategoriasTriage mundo, int pos) 
	{
		return mundo.existeModificacion(con,Integer.parseInt(forma.getCategorias("consecutivo_"+pos)+""),Integer.parseInt(forma.getCategorias("codigo_"+pos)+""),forma.getCategorias("descripcion_"+pos)+"",Integer.parseInt(forma.getCategorias("color_"+pos)+""));
	}

	/**
	 * 
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param isEliminacion
	 * @param pos
	 */
	private void generarLog(CategoriasTriageForm forma, CategoriasTriage mundo, UsuarioBasico usuario, boolean isEliminacion, int pos) 
	{
		String log = "";
		int tipoLog=0;
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO=========== " +
			 "\n*  Código [" +forma.getCategoriasEliminadas("codigoEliminado_"+pos)+""+"] "+
			 "\n*  Descripción ["+forma.getCategoriasEliminadas("descripcionEliminado_"+pos)+""+"] " +
			 "\n*  Color [" +forma.getCategoriasEliminadas("colorEliminado_"+pos)+""+"] "+
			 "\n*  Institucion ["+usuario.getInstitucion()+"] "+
			 "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL=========== " +
			 "\n*  Código [" +mundo.getCodigo()+"] "+
			 "\n*  Descripción ["+mundo.getDescripcion()+"] " +
			 "\n*  Color [" +mundo.getColor()+"] "+
			 "\n*  Institucion ["+usuario.getInstitucion()+"] "+
			 "\n   ===========INFORMACION MODIFICADA========== 	" +
			 "\n*  Código [" +forma.getCategorias("codigo_"+pos)+""+"] "+
			 "\n*  Descripción ["+forma.getCategorias("descripcion_"+pos)+""+"] " +
			 "\n*  Color [" +forma.getCategorias("color_"+pos)+""+"] "+
			 "\n*  Institucion ["+usuario.getInstitucion()+"] "+
			 "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		LogsAxioma.enviarLog(ConstantesBD.logCategoriasTriageCodigo,log,tipoLog,usuario.getLoginUsuario());
	}

	/**
	 * 
	 * @param forma
	 */
//	private void eliminarRegistro(CategoriasTriageForm forma)
	
	private void eliminarRegistro(Connection con, CategoriasTriageForm forma, CategoriasTriage mundo, UsuarioBasico usuario) 
	{
		
		ActionErrors errores=new ActionErrors();
		
		int pos=forma.getRegEliminar();
		int numReg=Integer.parseInt(forma.getCategorias("numRegistros")==null?"0":forma.getCategorias("numRegistros")+"");
		int ultimoRegistro=numReg-1;
		int regEliminados=Integer.parseInt(forma.getCategoriasEliminadas("registroseliminados")==null?"0":forma.getCategoriasEliminadas("registroseliminados")+"");

		if((forma.getCategorias("tiporegistro_"+pos)+"").equals("BD"))
		{
			forma.setCategoriasEliminadas("consecutivoEliminado_"+regEliminados,forma.getCategorias("consecutivo_"+pos));
			forma.setCategoriasEliminadas("codigoEliminado_"+regEliminados,forma.getCategorias("codigo_"+pos));
			forma.setCategoriasEliminadas("institucionEliminado_"+regEliminados,forma.getCategorias("institucion_"+pos));
			forma.setCategoriasEliminadas("descripcionEliminado_"+regEliminados,forma.getCategorias("descripcion_"+pos));
			forma.setCategoriasEliminadas("colorEliminado_"+regEliminados,forma.getCategorias("color_"+pos));
			forma.setCategoriasEliminadas("destinoEliminado_"+regEliminados,forma.getCategorias("destino_"+pos));
			forma.setCategoriasEliminadas("registroseliminados",(regEliminados+1)+"");
		}

		for(int i=pos;i<ultimoRegistro;i++)
		{
			forma.setCategorias("consecutivo_"+i,forma.getCategorias("consecutivo_"+(i+1)));
			forma.setCategorias("codigo_"+i,forma.getCategorias("codigo_"+(i+1)));
			forma.setCategorias("institucion_"+i,forma.getCategorias("institucion_"+(i+1)));
			forma.setCategorias("descripcion_"+i,forma.getCategorias("descripcion_"+(i+1)));
			forma.setCategorias("color_"+i,forma.getCategorias("color_"+(i+1)));
			forma.setCategorias("destino_"+i,forma.getCategorias("destino_"+(i+1)));
			forma.setCategorias("tiporegistro_"+i,forma.getCategorias("tiporegistro_"+(i+1)));
			forma.setCategorias("existerelacion_"+i,forma.getCategorias("existerelacion_"+(i+1)));
			
		}
		
		forma.getCategorias().remove("consecutivo_"+ultimoRegistro);
		forma.getCategorias().remove("codigo_"+ultimoRegistro);
		forma.getCategorias().remove("institucion_"+ultimoRegistro);
		forma.getCategorias().remove("descripcion_"+ultimoRegistro);
		forma.getCategorias().remove("color_"+ultimoRegistro);
		forma.getCategorias().remove("destino_"+ultimoRegistro);
		forma.getCategorias().remove("tiporegistro_"+ultimoRegistro);
		forma.getCategorias().remove("existerelacion_"+ultimoRegistro);

		//actualizando numero de registros.
		forma.setCategorias("numRegistros",ultimoRegistro+"");
		
		
		boolean enTransaccion=UtilidadBD.iniciarTransaccion(con);
		{
		
			//se encuentra entre la transaccion	//eliminar.
			for(int el=0;el<regEliminados;el++)
			{
				
				if(enTransaccion)
				{
					enTransaccion=mundo.eliminarRegistro(con,Integer.parseInt(forma.getCategoriasEliminadas("consecutivoEliminado_"+el)+""));
					this.generarLog(forma,mundo,usuario,true, el);
				}
				else
				{
					logger.error("ERROR DURANTE LA ELIMINACION DEL REGISTRO "+forma.getCategoriasEliminadas("consecutivoEliminado_"+el));
					errores.add("REGISTRO USADO", new ActionMessage("error.registroUtilizado","<B>"+forma.getCategoriasEliminadas("codigoEliminado_"+el)+" - "+forma.getCategoriasEliminadas("descripcionEliminado_"+el)+"</B>","NO SE PUEDE ELIMINAR"));  
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
	 * @param forma
	 */
	private void accionNuevoRegistro(CategoriasTriageForm forma) 
	{
		int index=Integer.parseInt(forma.getCategorias("numRegistros")+"");
		forma.setCategorias("codigo_"+index,"");
		forma.setCategorias("institucion_"+index,"");
		forma.setCategorias("descripcion_"+index,"");
		forma.setCategorias("color_"+index,"-1");
		HashMap mapaTemp=new HashMap();
		mapaTemp.put("numRegistros","0");
		forma.setCategorias("destino_"+index,mapaTemp.clone());
		forma.setCategorias("existerelacion_"+index,"false");
		forma.setCategorias("tiporegistro_"+index,"MEM");
		index++;
		forma.setCategorias("numRegistros",index+"");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionCargarMapaBaseDatos(Connection con, CategoriasTriageForm forma, CategoriasTriage mundo, UsuarioBasico usuario) 
	{
		mundo.cargarInformacion(con,usuario.getCodigoInstitucionInt());
		forma.setCategorias((HashMap)mundo.getCategorias().clone());
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
}
