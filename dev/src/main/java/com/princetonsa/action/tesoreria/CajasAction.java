/*
 * Action para el manejo de la funcionalidad cajas
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

import com.princetonsa.actionform.tesoreria.CajasForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.tesoreria.Cajas;

/**
 * @author artotor
 */
public class CajasAction extends Action 
{
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(CajasAction.class);
	
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

			if(form instanceof CajasForm)
			{


				//intentamos abrir una conexion con la fuente de datos 
				con = UtilidadBD.abrirConexion(); 
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				CajasForm forma = (CajasForm) form;
				Cajas mundo= new Cajas();
				UsuarioBasico usuario = getUsuarioBasicoSesion(request.getSession());
				String estado = forma.getEstado();
				forma.setMensaje(new ResultadoBoolean(false));
				logger.warn("estado [CajasAction.java]-->"+estado);
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
					//-----Se asigna el centro de atención del usuario por defecto cuando empieza ---------//
					forma.setCentroAtencion(usuario.getCodigoCentroAtencion());
					this.accionCargarMapaBaseDatos(con,forma,mundo,usuario);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("nuevo"))
				{
					this.accionNuevaCaja(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("eliminarCaja"))
				{
					this.eliminarCaja(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("guardar"))
				{
					mundo.setCentroAtencion(forma.getCentroAtencion());
					//----Se guarda el centro de atención temporalmente para después de guardar poder consultar -----//
					int centroAtencionTemp=forma.getCentroAtencion();
					this.accionGuardar(con,forma,mundo,usuario);
					//repetir la consulata y le que se hace en estado empezar
					forma.reset();
					mundo.reset();
					forma.setCentroAtencion(centroAtencionTemp);
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
				else if(estado.equals("cargarCajasXCentroAtencion"))
				{
					this.accionCargarMapaBaseDatos(con,forma,mundo,usuario);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("seleccionarTipo"))
				{
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
	 * @param forma
	 */
	private void accionOrdenar(CajasForm forma) 
	{
		int numReg=Integer.parseInt(forma.getMapaCajas("numeroregistros")==null?"0":forma.getMapaCajas("numeroregistros")+"");
		String[] indices={"consecutivo_","codigo_","institucion_","descripcion_","tipo_","descripciontipo_","activo_", "centroatencion_","existerelacion_","tiporegistro_", "valorBase_"};
		forma.setMapaCajas(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaCajas(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setMapaCajas("numeroregistros",numReg+"");
	}

	/**
	 * @param con
	 * @param form
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardar(Connection con, CajasForm forma, Cajas mundo, UsuarioBasico usuario) 
	{
		int numReg=Integer.parseInt(forma.getMapaCajas("numeroregistros")==null?"0":forma.getMapaCajas("numeroregistros")+"");
		int regEliminados=Integer.parseInt(forma.getMapaCajasEliminado("registroseliminados")==null?"0":forma.getMapaCajasEliminado("registroseliminados")+"");
		boolean enTransaccion=UtilidadBD.iniciarTransaccion(con);
		//se encuentra entre la transaccion
		{
			//eliminar.
			String nombreCentroAtencion=Utilidades.obtenerNombreCentroAtencion(con, forma.getCentroAtencion());
			for(int el=0;el<regEliminados;el++)
			{
				if(enTransaccion)
				{
					enTransaccion=mundo.eliminarRegistro(con,Integer.parseInt(forma.getMapaCajasEliminado("consecutivoEliminado_"+el)+""));
					this.generarLog(forma,mundo,usuario,true, el, nombreCentroAtencion);
				}
				else
				{
					logger.error("ERROR DURANTE LA ELIMINACION DE LA CAJA CON CONSECUTIVO"+forma.getMapaCajasEliminado("consecutivoEliminado_"+el));
					el=regEliminados;
				}
			}
			//insertar modificar.
	        for(int k=0;k<numReg;k++)
	        {
	        	Double valorBase=null;
	        	try{
	        		if(forma.getMapaCajas("valorBase_"+k)!=null && Integer.parseInt(forma.getMapaCajas("tipo_"+k)+"")==ConstantesBD.codigoTipoCajaRecaudado)
	        		{
	        			valorBase=Double.parseDouble(forma.getMapaCajas("valorBase_"+k)+"");
	        		}
	        	}catch (NumberFormatException e) {
				}
	        	
	        	if(enTransaccion)
	        	{
		        	//modificar
		        	if((forma.getMapaCajas("tiporegistro_"+k)+"").equals("BD")&&enTransaccion)
		        	{
		        		if(registroModificado(con,forma,mundo,k, valorBase))
		        		{
		        			enTransaccion=mundo.modificarRegistro(con,Integer.parseInt(forma.getMapaCajas("consecutivo_"+k)+""),Integer.parseInt(forma.getMapaCajas("codigo_"+k)+""),forma.getMapaCajas("descripcion_"+k)+"",Integer.parseInt(forma.getMapaCajas("tipo_"+k)+""),UtilidadTexto.getBoolean(forma.getMapaCajas("activo_"+k)+""), valorBase);
		        			this.generarLog(forma,mundo,usuario,false, k, nombreCentroAtencion);
		        		}
		        	}
		        	//insertar
		        	if((forma.getMapaCajas("tiporegistro_"+k)+"").equals("MEM")&& enTransaccion)
		        	{
		        		enTransaccion=mundo.insertarRegistro(con,Integer.parseInt(forma.getMapaCajas("codigo_"+k)+""),usuario.getCodigoInstitucionInt(),forma.getMapaCajas("descripcion_"+k)+"",Integer.parseInt(forma.getMapaCajas("tipo_"+k)+""),UtilidadTexto.getBoolean(forma.getMapaCajas("activo_"+k)+""), valorBase);
		        	}
	        	}
	        	else
	        	{
	        		logger.error("error modificando - insertando el registro ---> "+forma.getMapaCajas("tiporegistro_"+k)+" codigo-->"+forma.getMapaCajas("codigo_"+k));
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
	 * @param nombreCentroAtencion
	 */
	private void generarLog(CajasForm forma, Cajas mundo, UsuarioBasico usuario, boolean isEliminacion, int pos, String nombreCentroAtencion) 
	{
		String log = "";
		int tipoLog=0;
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO=========== " +
			 "\n*  Código [" +forma.getMapaCajasEliminado("codigoEliminado_"+pos)+""+"] "+
			 "\n*  Descripción ["+forma.getMapaCajasEliminado("descripcionEliminado_"+pos)+""+"] " +
			 "\n*  Tipo [" +forma.getMapaCajasEliminado("descripciontipoEliminado_"+pos)+""+"] "+
			 "\n*  Activo ["+forma.getMapaCajasEliminado("activoEliminado_"+pos)+""+"] " +
			 "\n*  Institucion ["+usuario.getInstitucion()+"] "+
			 "\n*  Centro de Atención ["+nombreCentroAtencion+"] "+
			 "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL=========== " +
			 "\n*  Código [" +mundo.getCodigo()+"] "+
			 "\n*  Descripción ["+mundo.getDescripcion()+"] " +
			 "\n*  Tipo [" +mundo.getTipo()+"] "+
			 "\n*  Activo ["+mundo.isActivo()+"] " +
			 "\n*  Institucion ["+usuario.getInstitucion()+"] "+
			 "\n*  Centro de Atención ["+nombreCentroAtencion+"] "+
			 "\n   ===========INFORMACION MODIFICADA========== 	" +
			 "\n*  Código [" +forma.getMapaCajas("codigo_"+pos)+""+"] "+
			 "\n*  Descripción ["+forma.getMapaCajas("descripcion_"+pos)+""+"] " +
			 "\n*  Tipo [" +forma.getMapaCajas("tipo_"+pos)+""+"] "+
			 "\n*  Activo ["+forma.getMapaCajas("activo_"+pos)+""+"] " +
			 "\n*  Institucion ["+usuario.getInstitucion()+"] "+
			 "\n*  Centro de Atención ["+nombreCentroAtencion+"] "+
			 "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		LogsAxioma.enviarLog(ConstantesBD.logCajasCodigo,log,tipoLog,usuario.getLoginUsuario());
	}

	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param valorBase
	 * @param k
	 * @return
	 */
	private boolean registroModificado(Connection con, CajasForm forma, Cajas mundo, int pos, Double valorBase) 
	{
		return mundo.existeModificacion(con,Integer.parseInt(forma.getMapaCajas("consecutivo_"+pos)+""),Integer.parseInt(forma.getMapaCajas("codigo_"+pos)+""),forma.getMapaCajas("descripcion_"+pos)+"",Integer.parseInt(forma.getMapaCajas("tipo_"+pos)+""),UtilidadTexto.getBoolean(forma.getMapaCajas("activo_"+pos)+""), valorBase);
	}

	/**
	 * @param forma
	 */
	private void eliminarCaja(CajasForm forma) 
	{
		int pos=forma.getRegEliminar();
		int numReg=Integer.parseInt(forma.getMapaCajas("numeroregistros")==null?"0":forma.getMapaCajas("numeroregistros")+"");
		int ultimoRegistro=numReg-1;
		int regEliminados=Integer.parseInt(forma.getMapaCajasEliminado("registroseliminados")==null?"0":forma.getMapaCajasEliminado("registroseliminados")+"");
		if((forma.getMapaCajas("tiporegistro_"+pos)+"").equals("BD"))
		{
			forma.setMapaCajasEliminado("consecutivoEliminado_"+regEliminados,forma.getMapaCajas("consecutivo_"+pos));
			forma.setMapaCajasEliminado("codigoEliminado_"+regEliminados,forma.getMapaCajas("codigo_"+pos));
			forma.setMapaCajasEliminado("institucionEliminado_"+regEliminados,forma.getMapaCajas("institucion_"+pos));
			forma.setMapaCajasEliminado("descripcionEliminado_"+regEliminados,forma.getMapaCajas("descripcion_"+pos));
			forma.setMapaCajasEliminado("tipoEliminado_"+regEliminados,forma.getMapaCajas("tipo_"+pos));
			forma.setMapaCajasEliminado("descripciontipoEliminado_"+regEliminados,forma.getMapaCajas("descripciontipo_"+pos));
			forma.setMapaCajasEliminado("activoEliminado_"+regEliminados,forma.getMapaCajas("activo_"+pos));
			forma.setMapaCajasEliminado("centroAtencionEliminado_"+regEliminados,forma.getMapaCajas("centroatencion_"+pos));
			forma.setMapaCajasEliminado("registroseliminados",(regEliminados+1)+"");
		}
		for(int i=pos;i<ultimoRegistro;i++)
		{
			
			forma.setMapaCajas("consecutivo_"+i,forma.getMapaCajas("consecutivo_"+(i+1)));
			forma.setMapaCajas("codigo_"+i,forma.getMapaCajas("codigo_"+(i+1)));
			forma.setMapaCajas("institucion_"+i,forma.getMapaCajas("institucion_"+(i+1)));
			forma.setMapaCajas("descripcion_"+i,forma.getMapaCajas("descripcion_"+(i+1)));
			forma.setMapaCajas("tipo_"+i,forma.getMapaCajas("tipo_"+(i+1)));
			forma.setMapaCajas("descripciontipo_"+i,forma.getMapaCajas("descripciontipo_"+(i+1)));
			forma.setMapaCajas("activo_"+i,forma.getMapaCajas("activo_"+(i+1)));
			forma.setMapaCajas("centroatencion_"+i,forma.getMapaCajas("centroatencion_"+(i+1)));
			forma.setMapaCajas("tiporegistro_"+i,forma.getMapaCajas("tiporegistro_"+(i+1)));
			forma.setMapaCajas("existerelacion_"+i,forma.getMapaCajas("existerelacion_"+(i+1)));
			
		}
		forma.getMapaCajas().remove("consecutivo_"+ultimoRegistro);
		forma.getMapaCajas().remove("codigo_"+ultimoRegistro);
		forma.getMapaCajas().remove("institucion_"+ultimoRegistro);
		forma.getMapaCajas().remove("descripcion_"+ultimoRegistro);
		forma.getMapaCajas().remove("tipo_"+ultimoRegistro);
		forma.getMapaCajas().remove("descripciontipo_"+ultimoRegistro);
		forma.getMapaCajas().remove("activo_"+ultimoRegistro);
		forma.getMapaCajas().remove("centroatencion_"+ultimoRegistro);
		forma.getMapaCajas().remove("tiporegistro_"+ultimoRegistro);
		forma.getMapaCajas().remove("existerelacion_"+ultimoRegistro);
		//actualizando numero de registros.
		forma.setMapaCajas("numeroregistros",ultimoRegistro+"");
	}

	/**
	 * @param forma
	 * @param mundo
	 */
	private void accionNuevaCaja(CajasForm forma) 
	{
		int index=Integer.parseInt(forma.getMapaCajas("numeroregistros")+"");
		forma.setMapaCajas("codigo_"+index,"");
		forma.setMapaCajas("institucion_"+index,"");
		forma.setMapaCajas("descripcion_"+index,"");
		forma.setMapaCajas("tipo_"+index,"-1");
		forma.setMapaCajas("descripciontipo_"+index,"Seleccione");
		forma.setMapaCajas("activo_"+index,"true");
		forma.setMapaCajas("centroatencion_"+index, forma.getCentroAtencion());
		forma.setMapaCajas("existerelacion_"+index,"false");
		forma.setMapaCajas("tiporegistro_"+index,"MEM");
		index++;
		forma.setMapaCajas("numeroregistros",index+"");
	}

	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionCargarMapaBaseDatos(Connection con, CajasForm forma, Cajas mundo, UsuarioBasico usuario) 
	{
		//--------Se pasa del form al mundo el valor del centro de atención ------------//
		mundo.setCentroAtencion(forma.getCentroAtencion());
		
		mundo.cargarInformacion(con,usuario.getCodigoInstitucionInt());
		forma.setMapaCajas((HashMap)mundo.getMapaCajas().clone());
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
