/*
 * Created on 20/09/2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.action.tesoreria;

import java.sql.Connection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.actionform.tesoreria.EntidadesFinancierasForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.tesoreria.EntidadesFinancieras;

/**
 * Clase para el control del flujo de las entidades financieras
 * @author Armando Osorio
 * @version 1.1
 */
public class EntidadesFinancierasAction extends Action 
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(EntidadesFinancierasAction.class);
	
	/**
	 * M&eacute;todo execute del action
	 */
	public ActionForward execute(	ActionMapping mapping, 	
													        ActionForm form, 
													        HttpServletRequest request, 
													        HttpServletResponse response) throws Exception
													        {
		Connection con = null;
		try{

			if(form instanceof EntidadesFinancierasForm)
			{


				//intentamos abrir una conexion con la fuente de datos 
				con = UtilidadBD.abrirConexion(); 
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				EntidadesFinancierasForm forma=(EntidadesFinancierasForm) form;
				UsuarioBasico usuario = getUsuarioBasicoSesion(request.getSession());
				EntidadesFinancieras mundo=new EntidadesFinancieras();
				String estado = forma.getEstado();
				forma.setMensaje(new ResultadoBoolean(false));
				logger.warn("estado [EntidadesFinancierasAction.java]-->"+estado);
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
					this.accionNuevaEntidadFinancera(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("eliminar"))
				{
					this.eliminarEntidadFinanciera(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("ordenar"))
				{
					this.accionOrdenar(forma);
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
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de ConceptosCarteraForm");
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
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardar(Connection con, EntidadesFinancierasForm forma, EntidadesFinancieras mundo, UsuarioBasico usuario) 
	{
		int numReg=forma.getNumeroRegistro();
		int regEliminados=Integer.parseInt(forma.getMapaEntidadesFinancierasEliminado("registroseliminados")==null?"0":forma.getMapaEntidadesFinancierasEliminado("registroseliminados")+"");
		boolean enTransaccion=UtilidadBD.iniciarTransaccion(con);
		//se encuentra entre la transaccion
		{
			//eliminar.
			for(int el=0;el<regEliminados;el++)
			{
				if(enTransaccion)
				{
					enTransaccion=mundo.eliminarRegistro(con,Integer.parseInt(forma.getMapaEntidadesFinancierasEliminado("consecutivoEliminado_"+el)+""));
					this.generarLog(forma,mundo,usuario,true, el);
				}
				else
				{
					logger.error("ERROR DURANTE LA ELIMINACION DEL REGISTRO"+forma.getMapaEntidadesFinancierasEliminado("consecutivoEliminado_"+el));
					el=regEliminados;
				}
			}
			//insertar modificar.
	        for(int k=0;k<numReg;k++)
	        {
	        	if(enTransaccion)
	        	{
		        	//modificar
		        	if((forma.getMapaEntidadesFinancieras("tiporegistro_"+k)+"").equals("BD")&&enTransaccion)
		        	{
		        		if(registroModificado(con,forma,mundo,k))
		        		{
		        			enTransaccion=mundo.modificarRegistro(con,Integer.parseInt(forma.getMapaEntidadesFinancieras("consecutivo_"+k)+""),forma.getMapaEntidadesFinancieras("codigo_"+k)+"",Integer.parseInt(forma.getMapaEntidadesFinancieras("codigotercero_"+k)+""),Integer.parseInt(forma.getMapaEntidadesFinancieras("codigotipo_"+k)+""),UtilidadTexto.getBoolean(forma.getMapaEntidadesFinancieras("activo_"+k)+""));
		        			this.generarLog(forma,mundo,usuario,false, k);
		        		}
		        	}
		        	//insertar
		        	if((forma.getMapaEntidadesFinancieras("tiporegistro_"+k)+"").equals("MEM")&& enTransaccion)
		        	{
		        		enTransaccion=mundo.insertarRegistro(con,forma.getMapaEntidadesFinancieras("codigo_"+k)+"",usuario.getCodigoInstitucionInt(),Integer.parseInt(forma.getMapaEntidadesFinancieras("codigotercero_"+k)+""),Integer.parseInt(forma.getMapaEntidadesFinancieras("codigotipo_"+k)+""),UtilidadTexto.getBoolean(forma.getMapaEntidadesFinancieras("activo_"+k)+""));
		        	}
	        	}
	        	else
	        	{
	        		logger.error("error modificando - insertando el registro ---> "+forma.getMapaEntidadesFinancieras("tiporegistro_"+k)+" codigo-->"+forma.getMapaEntidadesFinancieras("codigo_"+k));
	        		k=numReg;
	        	}
	        }
		}
		if(enTransaccion)
		{
			forma.setMensaje(new ResultadoBoolean(true, "OPERACIÓN REALIZADA CON ÉXITO"));
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			forma.setMensaje(new ResultadoBoolean(true, "OPERACIÓN NO EXITOSA. POR FAVOR VERIFICAR"));
			UtilidadBD.abortarTransaccion(con);
		}
	}
	
	/**
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param b
	 * @param el
	 */
	private void generarLog(EntidadesFinancierasForm forma, EntidadesFinancieras mundo, UsuarioBasico usuario, boolean isEliminacion, int pos) 
	{
		String log = "";
		int tipoLog=0;
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO=========== " +
			 "\n*  Código [" +forma.getMapaEntidadesFinancierasEliminado("codigoEliminado_"+pos)+""+"] "+
			 "\n*  Descripción ["+forma.getMapaEntidadesFinancierasEliminado("terceroEliminado_"+pos)+""+"] " +
			 "\n*  Tipo [" +forma.getMapaEntidadesFinancierasEliminado("descripciontipoEliminado_"+pos)+""+"] "+
			 "\n*  Institucion [" +usuario.getInstitucion()+"] "+
			 "\n*  Activo ["+forma.getMapaEntidadesFinancierasEliminado("activoEliminado_"+pos)+""+"] " +
			 "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL=========== " +
			 "\n*  Código [" +mundo.getCodigo()+"] "+
			 "\n*  Tercero ["+mundo.getTercero()+"] " +
			 "\n*  Tipo [" +mundo.getDesTipo()+"] "+
			 "\n*  Activo ["+mundo.isActivo()+"] " +
			 "\n*  Intitucion ["+usuario.getInstitucion()+"] " +
			 "\n   ===========INFORMACION MODIFICADA========== 	" +
			 "\n*  Código [" +forma.getMapaEntidadesFinancieras("codigo_"+pos)+""+"] "+
			 "\n*  Tercero ["+forma.getMapaEntidadesFinancieras("tercero_"+pos)+""+"] " +
			 "\n*  Tipo [" +forma.getMapaEntidadesFinancieras("descripciontipo_"+pos)+""+"] "+
			 "\n*  Activo ["+forma.getMapaEntidadesFinancieras("activo_"+pos)+""+"] " +
			 "\n*  Intitucion ["+usuario.getInstitucion()+"] " +
			 "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		LogsAxioma.enviarLog(ConstantesBD.logEntidadesFinancierasCodigo,log,tipoLog,usuario.getLoginUsuario());
	}

	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param k
	 * @return
	 */
	private boolean registroModificado(Connection con, EntidadesFinancierasForm forma, EntidadesFinancieras mundo, int pos)
	{
		return mundo.existeModificacion(con,Integer.parseInt(forma.getMapaEntidadesFinancieras("consecutivo_"+pos)+""),forma.getMapaEntidadesFinancieras("codigo_"+pos)+"",Integer.parseInt(forma.getMapaEntidadesFinancieras("codigotercero_"+pos)+""),Integer.parseInt(forma.getMapaEntidadesFinancieras("codigotipo_"+pos)+""),UtilidadTexto.getBoolean(forma.getMapaEntidadesFinancieras("activo_"+pos)+""));
	}

	/**
	 * @param forma
	 */
	private void accionOrdenar(EntidadesFinancierasForm forma) 
	{
		String[] indices={"consecutivo_","codigo_","institucion_","codigotercero_","descripciontercero_","tercero_","codigotipo_","descripciontipo_","activo_","existerelacion_","tiporegistro_", "nombre_tercero_"};
		Utilidades.imprimirMapa(forma.getMapaEntidadesFinancieras());
		forma.setMapaEntidadesFinancieras(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaEntidadesFinancieras(),forma.getNumeroRegistro()));
		forma.setUltimoPatron(forma.getPatronOrdenar());	
	}
	
	/**
	 * @param forma
	 */
	private void eliminarEntidadFinanciera(EntidadesFinancierasForm forma) 
	{
		int pos=forma.getRegEliminar();
		int numReg=forma.getNumeroRegistro();
		int ultimoRegistro=numReg-1;
		int regEliminados=Integer.parseInt(forma.getMapaEntidadesFinancierasEliminado("registroseliminados")==null?"0":forma.getMapaEntidadesFinancierasEliminado("registroseliminados")+"");

		if((forma.getMapaEntidadesFinancieras("tiporegistro_"+pos)+"").equals("BD"))
		{
			forma.setMapaEntidadesFinancierasEliminado("consecutivoEliminado_"+regEliminados,forma.getMapaEntidadesFinancieras("consecutivo_"+pos));
			forma.setMapaEntidadesFinancierasEliminado("codigoEliminado_"+regEliminados,forma.getMapaEntidadesFinancieras("codigo_"+pos));
			forma.setMapaEntidadesFinancierasEliminado("codigoterceroEliminado_"+regEliminados,forma.getMapaEntidadesFinancieras("codigotercero_"+pos));
			forma.setMapaEntidadesFinancierasEliminado("institucionEliminado_"+regEliminados,forma.getMapaEntidadesFinancieras("institucion_"+pos));
			forma.setMapaEntidadesFinancierasEliminado("terceroEliminado_"+regEliminados,forma.getMapaEntidadesFinancieras("tercero_"+pos));
			forma.setMapaEntidadesFinancierasEliminado("codigotipoEliminado_"+regEliminados,forma.getMapaEntidadesFinancieras("codigotipo_"+pos));
			forma.setMapaEntidadesFinancierasEliminado("descripciontipoEliminado_"+regEliminados,forma.getMapaEntidadesFinancieras("descripciontipo_"+pos));
			forma.setMapaEntidadesFinancierasEliminado("activoEliminado_"+regEliminados,forma.getMapaEntidadesFinancieras("activo_"+pos));
			forma.setMapaEntidadesFinancierasEliminado("registroseliminados",(regEliminados+1)+"");
		}
		for(int i=pos;i<ultimoRegistro;i++)
		{
			forma.setMapaEntidadesFinancieras("consecutivo_"+i,forma.getMapaEntidadesFinancieras("consecutivo_"+(i+1)));
			forma.setMapaEntidadesFinancieras("codigo_"+i,forma.getMapaEntidadesFinancieras("codigo_"+(i+1)));
			forma.setMapaEntidadesFinancieras("codigotercero_"+i,forma.getMapaEntidadesFinancieras("codigotercero_"+(i+1)));
			forma.setMapaEntidadesFinancieras("descripciontercero_"+i,forma.getMapaEntidadesFinancieras("descripciontercero_"+(i+1)));
			forma.setMapaEntidadesFinancieras("tercero_"+i,forma.getMapaEntidadesFinancieras("tercero_"+(i+1)));
			forma.setMapaEntidadesFinancieras("codigotipo_"+i,forma.getMapaEntidadesFinancieras("codigotipo_"+(i+1)));
			forma.setMapaEntidadesFinancieras("descripciontipo_"+i,forma.getMapaEntidadesFinancieras("descripciontipo_"+(i+1)));
			forma.setMapaEntidadesFinancieras("activo_"+i,forma.getMapaEntidadesFinancieras("activo_"+(i+1)));
			forma.setMapaEntidadesFinancieras("existerelacion_"+i,forma.getMapaEntidadesFinancieras("existerelacion_"+(i+1)));
			forma.setMapaEntidadesFinancieras("tiporegistro_"+i,forma.getMapaEntidadesFinancieras("tiporegistro_"+(i+1)));
		}
		forma.getMapaEntidadesFinancieras().remove("consecutivo_"+ultimoRegistro);
		forma.getMapaEntidadesFinancieras().remove("codigo_"+ultimoRegistro);
		forma.getMapaEntidadesFinancieras().remove("codigotercero_"+ultimoRegistro);
		forma.getMapaEntidadesFinancieras().remove("descripciontercero_"+ultimoRegistro);
		forma.getMapaEntidadesFinancieras().remove("tercero_"+ultimoRegistro);
		forma.getMapaEntidadesFinancieras().remove("codigotipo_"+ultimoRegistro);
		forma.getMapaEntidadesFinancieras().remove("descripciontipo_"+ultimoRegistro);
		forma.getMapaEntidadesFinancieras().remove("activo_"+ultimoRegistro);
		forma.getMapaEntidadesFinancieras().remove("existerelacion_"+ultimoRegistro);
		forma.getMapaEntidadesFinancieras().remove("tiporegistro_"+ultimoRegistro);
		forma.setNumeroRegistro(ultimoRegistro);
	}
	/**
	 * @param forma
	 */
	private void accionNuevaEntidadFinancera(EntidadesFinancierasForm forma) 
	{
		
		forma.setMapaEntidadesFinancieras("codigo_"+forma.getNumeroRegistro(),"");
		forma.setMapaEntidadesFinancieras("codigotercero_"+forma.getNumeroRegistro(),"-1");
		forma.setMapaEntidadesFinancieras("descripciontercero_"+forma.getNumeroRegistro(),"");
		forma.setMapaEntidadesFinancieras("tercero_"+forma.getNumeroRegistro(),"Seleccione");
		forma.setMapaEntidadesFinancieras("codigotipo_"+forma.getNumeroRegistro(),"-1");
		forma.setMapaEntidadesFinancieras("descripciontipo_"+forma.getNumeroRegistro(),"Seleccione");
		forma.setMapaEntidadesFinancieras("activo_"+forma.getNumeroRegistro(),true+"");
		forma.setMapaEntidadesFinancieras("existerelacion_"+forma.getNumeroRegistro(),false+"");
		forma.setMapaEntidadesFinancieras("tiporegistro_"+forma.getNumeroRegistro(),"MEM");
		forma.setNumeroRegistro(forma.getNumeroRegistro()+1);
	}
	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionCargarMapaBaseDatos(Connection con, EntidadesFinancierasForm forma, EntidadesFinancieras mundo, UsuarioBasico usuario) 
	{
		mundo.cargarEntidadesFinancieras(con,usuario.getCodigoInstitucionInt());
		forma.setMapaEntidadesFinancieras((HashMap)mundo.getMapaEntidadesFinancieras().clone());
		forma.setNumeroRegistro(mundo.getNumeroRegistros());
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
