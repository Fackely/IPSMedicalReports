/*
 * @author armando
 */
package com.princetonsa.action.pyp;

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
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.pyp.TiposProgramaForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.pyp.TiposPrograma;

/**
 * 
 * @author armando
 *
 */
public class TiposProgramaAction extends Action 
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(TiposProgramaAction.class);
	
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

			if(form instanceof TiposProgramaForm)
			{


				//intentamos abrir una conexion con la fuente de datos 
				con = UtilidadBD.abrirConexion(); 
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				TiposProgramaForm forma=(TiposProgramaForm)form;
				TiposPrograma mundo=new TiposPrograma();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				String estado = forma.getEstado();
				logger.warn("estado [TiposProgramaAction.java]-->"+estado);
				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ConceptosCarteraAction (null) ");
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
					forma.setProcesoRealizado(ConstantesBD.acronimoNo);
					this.accionNuevoRegistro(forma,usuario);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("ordenar"))
				{
					forma.setProcesoRealizado(ConstantesBD.acronimoNo);
					this.accionOrdenar(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("eliminar"))
				{
					forma.setProcesoRealizado(ConstantesBD.acronimoNo);
					this.eliminarRegistro(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("guardar"))
				{
					String help = ConstantesBD.acronimoNo;
					this.accionGuardar(con,forma,mundo,usuario);
					if(forma.getProcesoRealizado().equals(ConstantesBD.acronimoSi))
						help = ConstantesBD.acronimoSi;
					forma.reset();
					mundo.reset();
					forma.setProcesoRealizado(help);
					this.accionCargarMapaBaseDatos(con,forma,mundo,usuario);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");
				}
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}

			}
			logger.error("El form no es compatible con el form de TiposProgramaForm");
			request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
			return mapping.findForward("paginaError");

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
	private void accionGuardar(Connection con, TiposProgramaForm forma, TiposPrograma mundo, UsuarioBasico usuario) 
	{
		int numReg=Integer.parseInt(forma.getTiposPrograma("numRegistros")==null?"0":forma.getTiposPrograma("numRegistros")+"");
		int regEliminados=Integer.parseInt(forma.getTiposProgamaEliminados("numRegistros")==null?"0":forma.getTiposProgamaEliminados("numRegistros")+"");
		boolean enTransaccion=UtilidadBD.iniciarTransaccion(con);
		//se encuentra entre la transaccion
		{
			//eliminar.
			for(int el=0;el<regEliminados;el++)
			{
				if(enTransaccion)
				{
					enTransaccion=mundo.eliminarRegistro(con,forma.getTiposProgamaEliminados("codigoEliminado_"+el)+"",Integer.parseInt(forma.getTiposProgamaEliminados("institucionEliminado_"+el)+""));
					this.generarLog(forma,mundo,usuario,true, el);
				}
				else
				{
					logger.error("ERROR DURANTE LA ELIMINACION DEL REGISTRO - TiposProgramaAction[accionGuardar] "+forma.getTiposProgamaEliminados("codigo_"+el));
					el=regEliminados;
				}
			}
			//insertar modificar.
	        for(int k=0;k<numReg;k++)
	        {
	        	if(enTransaccion)
	        	{
		        	//modificar
		        	if((forma.getTiposPrograma("tiporegistro_"+k)+"").equals("BD")&&enTransaccion)
		        	{
		        		if(registroModificado(con,forma,mundo,k))
		        		{
		        			enTransaccion=mundo.modificarRegistro(con,forma.getTiposPrograma("codigo_"+k)+"",Integer.parseInt(forma.getTiposPrograma("institucion_"+k)+""),forma.getTiposPrograma("descripcion_"+k)+"",UtilidadTexto.getBoolean(forma.getTiposPrograma("activo_"+k)+""));
		        			this.generarLog(forma,mundo,usuario,false, k);
		        		}
		        	}
		        	//insertar
		        	if((forma.getTiposPrograma("tiporegistro_"+k)+"").equals("MEM")&& enTransaccion)
		        	{
		        		enTransaccion=mundo.insertarRegistro(con,forma.getTiposPrograma("codigo_"+k)+"",usuario.getCodigoInstitucionInt(),forma.getTiposPrograma("descripcion_"+k)+"",UtilidadTexto.getBoolean(forma.getTiposPrograma("activo_"+k)+""));
		        	}
	        	}
	        	else
	        	{
	        		logger.error("ERROR DURANTE LA MODIFICACION DEL REGISTRO - TiposProgramaAction[accionGuardar]"+forma.getTiposPrograma("tiporegistro_"+k)+" codigo-->"+forma.getTiposPrograma("codigo_"+k));
	        		k=numReg;
	        	}
	        }
		}
		if(enTransaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
			forma.setProcesoRealizado(ConstantesBD.acronimoSi);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			forma.setProcesoRealizado(ConstantesBD.acronimoNo);
		}
	}

	/**
	 * 
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param b
	 * @param k
	 */
	private void generarLog(TiposProgramaForm forma, TiposPrograma mundo, UsuarioBasico usuario,  boolean isEliminacion, int pos) 
	{
		String log = "";
		int tipoLog=0;
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO=========== " +
			 "\n*  Código [" +forma.getTiposProgamaEliminados("codigoEliminado_"+pos)+""+"] "+
			 "\n*  Descripción ["+forma.getTiposProgamaEliminados("descripcionEliminado_"+pos)+""+"] " +
			 "\n*  Activo ["+forma.getTiposProgamaEliminados("activoEliminado_"+pos)+""+"] " +
			 "\n*  Institucion ["+usuario.getInstitucion()+"] "+
			 "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL=========== " +
			 "\n*  Código [" +mundo.getCodigo()+"] "+
			 "\n*  Descripción ["+mundo.getDescripcion()+"] " +
			 "\n*  Activo ["+mundo.isActivo()+"] " +
			 "\n*  Institucion ["+usuario.getInstitucion()+"] "+
			 "\n   ===========INFORMACION MODIFICADA========== 	" +
			 "\n*  Código [" +forma.getTiposPrograma("codigo_"+pos)+""+"] "+
			 "\n*  Descripción ["+forma.getTiposPrograma("descripcion_"+pos)+""+"] " +
			 "\n*  Activo ["+forma.getTiposPrograma("activo_"+pos)+""+"] " +
			 "\n*  Institucion ["+usuario.getInstitucion()+"] "+
			 "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		LogsAxioma.enviarLog(ConstantesBD.logTiposProgramaPYPCodigo,log,tipoLog,usuario.getLoginUsuario());
	}
	

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param pos
	 * @return
	 */
	private boolean registroModificado(Connection con, TiposProgramaForm forma, TiposPrograma mundo, int pos) 
	{
		return mundo.existeModificacion(con,forma.getTiposPrograma("codigo_"+pos)+"",Integer.parseInt(forma.getTiposPrograma("institucion_"+pos)+""),forma.getTiposPrograma("descripcion_"+pos)+"",UtilidadTexto.getBoolean(forma.getTiposPrograma("activo_"+pos)+""));
	}


	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenar(TiposProgramaForm forma) 
	{
		int numReg=Integer.parseInt(forma.getTiposPrograma("numRegistros")==null?"0":forma.getTiposPrograma("numRegistros")+"");
		String[] indices={"codigo_","institucion_","descripcion_","activo_","tiporegistro_"};
		forma.setTiposPrograma(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getTiposPrograma(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setTiposPrograma("numRegistros",numReg+"");
	}

	/**
	 * 
	 * @param forma
	 */
	private void eliminarRegistro(TiposProgramaForm forma)
	{
		int pos=forma.getRegEliminar();
		int numReg=Integer.parseInt(forma.getTiposPrograma("numRegistros")==null?"0":forma.getTiposPrograma("numRegistros")+"");
		int ultimoRegistro=numReg-1;
		int regEliminados=Integer.parseInt(forma.getTiposProgamaEliminados("numRegistros")==null?"0":forma.getTiposProgamaEliminados("numRegistros")+"");
		if((forma.getTiposPrograma("tiporegistro_"+pos)+"").equals("BD"))
		{
			forma.setTiposProgamaEliminados("codigoEliminado_"+regEliminados,forma.getTiposPrograma("codigo_"+pos));
			forma.setTiposProgamaEliminados("institucionEliminado_"+regEliminados,forma.getTiposPrograma("institucion_"+pos));
			forma.setTiposProgamaEliminados("descripcionEliminado_"+regEliminados,forma.getTiposPrograma("descripcion_"+pos));
			forma.setTiposProgamaEliminados("activoEliminado_"+regEliminados,forma.getTiposPrograma("activo_"+pos));
			forma.setTiposProgamaEliminados("numRegistros",(regEliminados+1)+"");
		}
		for(int i=pos;i<ultimoRegistro;i++)
		{
			
			forma.setTiposPrograma("codigo_"+i,forma.getTiposPrograma("codigo_"+(i+1)));
			forma.setTiposPrograma("institucion_"+i,forma.getTiposPrograma("institucion_"+(i+1)));
			forma.setTiposPrograma("descripcion_"+i,forma.getTiposPrograma("descripcion_"+(i+1)));
			forma.setTiposPrograma("activo_"+i,forma.getTiposPrograma("activo_"+(i+1)));
			forma.setTiposPrograma("tiporegistro_"+i,forma.getTiposPrograma("tiporegistro_"+(i+1)));
			
		}
		forma.getTiposPrograma().remove("codigo_"+ultimoRegistro);
		forma.getTiposPrograma().remove("institucion_"+ultimoRegistro);
		forma.getTiposPrograma().remove("descripcion_"+ultimoRegistro);
		forma.getTiposPrograma().remove("activo_"+ultimoRegistro);
		forma.getTiposPrograma().remove("tiporegistro_"+ultimoRegistro);
		//actualizando numero de registros.
		forma.setTiposPrograma("numRegistros",ultimoRegistro+"");
	}

	/**
	 * 
	 * @param forma
	 * @param usuario 
	 */
	private void accionNuevoRegistro(TiposProgramaForm forma, UsuarioBasico usuario) 
	{
		int index=Integer.parseInt(forma.getTiposPrograma("numRegistros")+"");
		forma.setTiposPrograma("codigo_"+index,"");
		forma.setTiposPrograma("institucion_"+index,usuario.getCodigoInstitucion()+"");
		forma.setTiposPrograma("descripcion_"+index,"");
		forma.setTiposPrograma("activo_"+index,"true");
		forma.setTiposPrograma("tiporegistro_"+index,"MEM");
		index++;
		forma.setTiposPrograma("numRegistros",index+"");
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionCargarMapaBaseDatos(Connection con, TiposProgramaForm forma, TiposPrograma mundo, UsuarioBasico usuario) 
	{
		mundo.cargarInformacion(con,usuario.getCodigoInstitucionInt());
		forma.setTiposPrograma((HashMap)mundo.getTiposProgama().clone());
	}
}
