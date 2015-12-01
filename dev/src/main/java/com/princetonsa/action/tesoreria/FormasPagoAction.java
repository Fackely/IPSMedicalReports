/*
 * Created on 19/09/2005
 *
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

import com.princetonsa.actionform.tesoreria.FormasPagoForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.tesoreria.FormasPago;

/**
 * @author artotor
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FormasPagoAction extends Action 
{
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
			if(form instanceof FormasPagoForm)
			{


				//intentamos abrir una conexion con la fuente de datos 
				con = UtilidadBD.abrirConexion(); 
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				FormasPagoForm forma = (FormasPagoForm) form;
				FormasPago mundo= new FormasPago();
				UsuarioBasico usuario = getUsuarioBasicoSesion(request.getSession());
				String estado = forma.getEstado();
				forma.setMensaje(new ResultadoBoolean(false));
				Log4JManager.info("estado [FormasPagoAction.java]-->"+estado);
				if(estado == null)
				{
					Log4JManager.info("Estado no valido dentro del flujo de ConceptosCarteraAction (null) ");
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
					this.accionNuevaFormaPago(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("eliminar"))
				{
					this.eliminarFormaPago(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("ordenar"))
				{
					this.accionOrdenar(forma);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("continuar"))
				{
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
				Log4JManager.error("El form no es compatible con el form de ConceptosCarteraForm");
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
	private void accionGuardar(Connection con, FormasPagoForm forma, FormasPago mundo, UsuarioBasico usuario) 
	{
		int numReg=forma.getNumeroRegistro();
		int regEliminados=Integer.parseInt(forma.getMapaFormasPagoEliminado("registroseliminados")==null?"0":forma.getMapaFormasPagoEliminado("registroseliminados")+"");
		boolean enTransaccion=UtilidadBD.iniciarTransaccion(con);
		//se encuentra entre la transaccion
		{
			//eliminar.
			for(int el=0;el<regEliminados;el++)
			{
				if(enTransaccion)
				{
					enTransaccion=mundo.eliminarRegistro(con,Integer.parseInt(forma.getMapaFormasPagoEliminado("consecutivoEliminado_"+el)+""));
					this.generarLog(forma,mundo,usuario,true, el);
				}
				else
				{
					Log4JManager.error("ERROR DURANTE LA ELIMINACION DEL REGISTRO"+forma.getMapaFormasPagoEliminado("consecutivoEliminado_"+el));
					el=regEliminados;
				}
			}
			//insertar modificar.
	        for(int k=0;k<numReg;k++)
	        {
	        	if(enTransaccion)
	        	{
		        	//modificar
		        	if((forma.getMapaFormasPago("tiporegistro_"+k)+"").equals("BD")&&enTransaccion)
		        	{
		        		if(registroModificado(con,forma,mundo,k))
		        		{
		        			Log4JManager.info("===>Descripción Modificar: "+forma.getMapaFormasPago("descripcion_"+k)+"");
		        			enTransaccion=mundo.modificarRegistro(con,Integer.parseInt(forma.getMapaFormasPago("consecutivo_"+k)+""),forma.getMapaFormasPago("codigo_"+k)+"",forma.getMapaFormasPago("descripcion_"+k)+"",Integer.parseInt(forma.getMapaFormasPago("tipodetalle_"+k)+""),UtilidadTexto.getBoolean(forma.getMapaFormasPago("activo_"+k)+""), Integer.parseInt(forma.getMapaFormasPago("cuentacontable_"+k)+""), forma.getMapaFormasPago("indicativoconsignacion_"+k).toString(), forma.getMapaFormasPago("traslado_caja_recaudo_"+k).toString(), forma.getMapaFormasPago("req_traslado_caja_recaudo_"+k).toString());
		        			this.generarLog(forma,mundo,usuario,false, k);
		        		}
		        	}
		        	//insertar
		        	if((forma.getMapaFormasPago("tiporegistro_"+k)+"").equals("MEM")&& enTransaccion)
		        	{
		        		Log4JManager.info("===>Descripción Insertar: "+forma.getMapaFormasPago("descripcion_"+k)+"");
		        		enTransaccion=mundo.insertarRegistro(con,forma.getMapaFormasPago("codigo_"+k)+"",usuario.getCodigoInstitucionInt(),forma.getMapaFormasPago("descripcion_"+k)+"",Integer.parseInt(forma.getMapaFormasPago("tipodetalle_"+k)+""),UtilidadTexto.getBoolean(forma.getMapaFormasPago("activo_"+k)+""), Integer.parseInt(forma.getMapaFormasPago("cuentacontable_"+k)+""), forma.getMapaFormasPago("indicativoconsignacion_"+k).toString(), forma.getMapaFormasPago("traslado_caja_recaudo_"+k).toString(), forma.getMapaFormasPago("req_traslado_caja_recaudo_"+k).toString());
		        	}
	        	}
	        	else
	        	{
	        		Log4JManager.error("error modificando - insertando el registro ---> "+forma.getMapaFormasPago("tiporegistro_"+k)+" codigo-->"+forma.getMapaFormasPago("codigo_"+k));
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
	private void generarLog(FormasPagoForm forma, FormasPago mundo, UsuarioBasico usuario, boolean isEliminacion, int pos) 
	{
		String log = "";
		int tipoLog=0;
		if(isEliminacion)
		{
			log = 		 "\n   ============REGISTRO ELIMINADO=========== " +
			 "\n*  Código [" +forma.getMapaFormasPagoEliminado("codigoEliminado_"+pos)+""+"] "+
			 "\n*  Descripción ["+forma.getMapaFormasPagoEliminado("descripcionEliminado_"+pos)+""+"] " +
			 "\n*  Tipo [" +forma.getMapaFormasPagoEliminado("nomtipodetalleEliminado_"+pos)+""+"] "+
			 "\n*  Cuenta Contable [" +forma.getMapaFormasPagoEliminado("cuentacontableEliminado_"+pos)+""+"] "+
			 "\n*  Institucion ["+usuario.getInstitucion()+"] "+
			 "\n*  Activo ["+forma.getMapaFormasPagoEliminado("activoEliminado_"+pos)+""+"] " +
			 "\n*  Ind.Consignacion ["+forma.getMapaFormasPagoEliminado("indicativoconsignacionEliminado_"+pos)+""+"] " +
			 "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============INFORMACION ORIGINAL=========== " +
			 "\n*  Código [" +mundo.getCodigo()+"] "+
			 "\n*  Descripción ["+mundo.getDescripcion()+"] " +
			 "\n*  Tipo [" +mundo.getTipoDetalle()+"] "+
			 "\n*  Cuenta Contable [" +mundo.getCuentaContable()+"] "+
			 "\n*  Institucion [" +usuario.getInstitucion()+"] "+
			 "\n*  Activo ["+mundo.isActivo()+"] " +
			 "\n*  Ind.Consignacion ["+mundo.getIndicativoConsignacion()+""+"] " +
			 "\n   ===========INFORMACION MODIFICADA========== 	" +
			 "\n*  Código [" +forma.getMapaFormasPago("codigo_"+pos)+""+"] "+
			 "\n*  Descripción ["+forma.getMapaFormasPago("descripcion_"+pos)+""+"] " +
			 "\n*  Tipo [" +forma.getMapaFormasPago("tipodetalle_"+pos)+""+"] "+
			 "\n*  Cuenta Contable [" +forma.getMapaFormasPago("cuentacontable_"+pos)+""+"] "+
			 "\n*  Institucion [" +usuario.getInstitucion()+"] "+
			 "\n*  Activo ["+forma.getMapaFormasPago("activo_"+pos)+""+"] " +
			 "\n*  Consig Banco ["+forma.getMapaFormasPago("indicativoconsignacion_"+pos)+""+"] " +
			 "\n*  traslado caja recaudo ["+forma.getMapaFormasPago("traslado_caja_recaudo_"+pos)+""+"] " +
			 "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		LogsAxioma.enviarLog(ConstantesBD.logFormasPagoCodigo,log,tipoLog,usuario.getLoginUsuario());
	}

	
	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param k
	 * @return
	 */
	private boolean registroModificado(Connection con, FormasPagoForm forma, FormasPago mundo, int pos) 
	{
		return mundo.existeModificacion(con,Integer.parseInt(forma.getMapaFormasPago("consecutivo_"+pos)+""),forma.getMapaFormasPago("codigo_"+pos)+"",forma.getMapaFormasPago("descripcion_"+pos)+"",Integer.parseInt(forma.getMapaFormasPago("tipodetalle_"+pos)+""),UtilidadTexto.getBoolean(forma.getMapaFormasPago("activo_"+pos)+""), Integer.parseInt(forma.getMapaFormasPago("cuentacontable_"+pos)+""), forma.getMapaFormasPago("indicativoconsignacion_"+pos).toString(), forma.getMapaFormasPago("traslado_caja_recaudo_"+pos).toString(), forma.getMapaFormasPago("req_traslado_caja_recaudo_"+pos).toString());
	}

	/**
	 * @param forma
	 */
	private void accionOrdenar(FormasPagoForm forma) 
	{
		String[] indices={"consecutivo_","codigo_","institucion_","descripcion_","tipodetalle_","nomtipodetalle_","activo_","existerelacion_","tiporegistro_", "cuentacontable_", "indicativoconsignacion_", "traslado_caja_recaudo_", "req_traslado_caja_recaudo_"};
		forma.setMapaFormasPago(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getMapaFormasPago(),forma.getNumeroRegistro()));
		forma.setUltimoPatron(forma.getPatronOrdenar());
	}

	/**
	 * @param forma
	 */
	private void eliminarFormaPago(FormasPagoForm forma) 
	{
		int pos=forma.getRegEliminar();
		int numReg=forma.getNumeroRegistro();
		int ultimoRegistro=numReg-1;
		int regEliminados=Integer.parseInt(forma.getMapaFormasPagoEliminado("registroseliminados")==null?"0":forma.getMapaFormasPagoEliminado("registroseliminados")+"");
		if((forma.getMapaFormasPago("tiporegistro_"+pos)+"").equals("BD"))
		{
			forma.setMapaFormasPagoEliminado("consecutivoEliminado_"+regEliminados,forma.getMapaFormasPago("consecutivo_"+pos));
			forma.setMapaFormasPagoEliminado("codigoEliminado_"+regEliminados,forma.getMapaFormasPago("codigo_"+pos));
			forma.setMapaFormasPagoEliminado("institucionEliminado_"+regEliminados,forma.getMapaFormasPago("institucion_"+pos));
			forma.setMapaFormasPagoEliminado("descripcionEliminado_"+regEliminados,forma.getMapaFormasPago("descripcion_"+pos));
			forma.setMapaFormasPagoEliminado("tipodetalleEliminado_"+regEliminados,forma.getMapaFormasPago("tipodetalle_"+pos));
			forma.setMapaFormasPagoEliminado("nomtipodetalleEliminado_"+regEliminados,forma.getMapaFormasPago("nomtipodetalle_"+pos));
			forma.setMapaFormasPagoEliminado("activoEliminado_"+regEliminados,forma.getMapaFormasPago("activo_"+pos));
			forma.setMapaFormasPagoEliminado("cuentacontableEliminado_"+regEliminados,forma.getMapaFormasPago("cuentacontable_"+pos));
			forma.setMapaFormasPagoEliminado("indicativoconsignacionEliminado_"+regEliminados,forma.getMapaFormasPago("indicativoconsignacion_"+pos));
			forma.setMapaFormasPagoEliminado("traslado_caja_recaudo_"+regEliminados,forma.getMapaFormasPago("traslado_caja_recaudo_"+pos));
			forma.setMapaFormasPagoEliminado("req_traslado_caja_recaudo_"+regEliminados,forma.getMapaFormasPago("req_traslado_caja_recaudo_"+pos));
			forma.setMapaFormasPagoEliminado("registroseliminados",(regEliminados+1)+"");
		}
		for(int i=pos;i<ultimoRegistro;i++)
		{
			
			forma.setMapaFormasPago("consecutivo_"+i,forma.getMapaFormasPago("consecutivo_"+(i+1)));
			forma.setMapaFormasPago("codigo_"+i,forma.getMapaFormasPago("codigo_"+(i+1)));
			forma.setMapaFormasPago("institucion_"+i,forma.getMapaFormasPago("institucion_"+(i+1)));
			forma.setMapaFormasPago("descripcion_"+i,forma.getMapaFormasPago("descripcion_"+(i+1)));
			forma.setMapaFormasPago("tipodetalle_"+i,forma.getMapaFormasPago("tipodetalle_"+(i+1)));
			forma.setMapaFormasPago("nomtipodetalle_"+i,forma.getMapaFormasPago("nomtipodetalle_"+(i+1)));
			forma.setMapaFormasPago("activo_"+i,forma.getMapaFormasPago("activo_"+(i+1)));
			forma.setMapaFormasPago("cuentacontable_"+i,forma.getMapaFormasPago("cuentacontable_"+(i+1)));
			forma.setMapaFormasPago("indicativoconsignacion_"+i,forma.getMapaFormasPago("indicativoconsignacion_"+(i+1)));
			forma.setMapaFormasPago("traslado_caja_recaudo_"+i,forma.getMapaFormasPago("traslado_caja_recaudo_"+(i+1)));
			forma.setMapaFormasPago("req_traslado_caja_recaudo_"+i,forma.getMapaFormasPago("req_traslado_caja_recaudo_"+(i+1)));
			forma.setMapaFormasPago("tiporegistro_"+i,forma.getMapaFormasPago("tiporegistro_"+(i+1)));
			forma.setMapaFormasPago("existerelacion_"+i,forma.getMapaFormasPago("existerelacion_"+(i+1)));
			
		}
		forma.getMapaFormasPago().remove("consecutivo_"+ultimoRegistro);
		forma.getMapaFormasPago().remove("codigo_"+ultimoRegistro);
		forma.getMapaFormasPago().remove("institucion_"+ultimoRegistro);
		forma.getMapaFormasPago().remove("descripcion_"+ultimoRegistro);
		forma.getMapaFormasPago().remove("tipodetalle_"+ultimoRegistro);
		forma.getMapaFormasPago().remove("nomtipodetalle_"+ultimoRegistro);
		forma.getMapaFormasPago().remove("activo_"+ultimoRegistro);
		forma.getMapaFormasPago().remove("cuentacontable_"+ultimoRegistro);
		forma.getMapaFormasPago().remove("indicativoconsignacion_"+ultimoRegistro);
		forma.getMapaFormasPago().remove("traslado_caja_recaudo_"+ultimoRegistro);
		forma.getMapaFormasPago().remove("req_traslado_caja_recaudo_"+ultimoRegistro);
		forma.getMapaFormasPago().remove("tiporegistro_"+ultimoRegistro);
		forma.getMapaFormasPago().remove("existerelacion_"+ultimoRegistro);
		//actualizando numero de registros.
		forma.setNumeroRegistro(ultimoRegistro);
	}

	/**
	 * @param forma
	 */
	private void accionNuevaFormaPago(FormasPagoForm forma) 
	{
		forma.setMapaFormasPago("codigo_"+forma.getNumeroRegistro(),"");
		forma.setMapaFormasPago("descripcion_"+forma.getNumeroRegistro(),"");
		forma.setMapaFormasPago("tipodetalle_"+forma.getNumeroRegistro(),"-1");
		forma.setMapaFormasPago("nomtipodetalle_"+forma.getNumeroRegistro(),"Seleccione");
		forma.setMapaFormasPago("activo_"+forma.getNumeroRegistro(),true+"");
		forma.setMapaFormasPago("existerelacion_"+forma.getNumeroRegistro(),"false");
		forma.setMapaFormasPago("tiporegistro_"+forma.getNumeroRegistro(),"MEM");
		forma.setMapaFormasPago("cuentacontable_"+forma.getNumeroRegistro(), ConstantesBD.codigoNuncaValido+"");
		forma.setMapaFormasPago("indicativoconsignacion_"+forma.getNumeroRegistro(), ConstantesBD.acronimoSi);
		forma.setMapaFormasPago("traslado_caja_recaudo_"+forma.getNumeroRegistro(), ConstantesBD.acronimoSi);
		forma.setMapaFormasPago("req_traslado_caja_recaudo_"+forma.getNumeroRegistro(), ConstantesBD.acronimoNo);
		forma.setNumeroRegistro(forma.getNumeroRegistro()+1);
	}

	/**
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionCargarMapaBaseDatos(Connection con, FormasPagoForm forma, FormasPago mundo, UsuarioBasico usuario) 
	{
		mundo.cargarFormasPagos(con,usuario.getCodigoInstitucionInt());
		forma.setMapaFormasPago((HashMap)mundo.getMapaFormasPago().clone());
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
				Log4JManager.error("El usuario no esta cargado (null)");
			
			return usuario;
	}
}
