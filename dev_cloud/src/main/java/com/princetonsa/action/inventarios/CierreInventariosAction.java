/*
 * Created on 19/12/2005
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 *
 */
package com.princetonsa.action.inventarios;

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
import util.UtilidadBD;
import util.Utilidades;
import util.inventarios.ConstantesBDInventarios;
import util.inventarios.UtilidadInventarios;

import com.princetonsa.actionform.inventarios.CierreInventariosForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.inventarios.CierreInventarios;

/**
 * @version 1.0, 19/12/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class CierreInventariosAction extends Action
{

    /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */ 
	private Logger logger = Logger.getLogger(CierreInventariosAction.class);
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


			if(form instanceof CierreInventariosForm)
			{


				//intentamos abrir una conexion con la fuente de datos 
				con = UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				CierreInventariosForm forma=(CierreInventariosForm) form;
				CierreInventarios mundo= new CierreInventarios();
				String estado = forma.getEstado();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());

				logger.info("\n\n*******************************CierreInventariosAction->"+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ConceptoTesoreriaAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					forma.reset();
					forma.inicializarMesAnioCierre(usuario.getCodigoInstitucionInt());
					forma.setExisteCierreInicial(!UtilidadInventarios.fechaCierreInicial(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.codigoNuncaValido+""));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaPrincipal");
				}
				else if(estado.equals("generar"))
				{
					if(forma.isExisteCierre())
					{
						UtilidadBD.closeConnection(con);
						return mapping.findForward("paginaPrincipal");
					}
					this.accionCargarMovimientosInventarios(con,forma,mundo);
					/*if(Utilidades.convertirAEntero(mundo.getMovimientos().get("numRegistros")+"")<=0)
					{
						ActionErrors error=new ActionErrors();
						error.add("error fecha sin movimientos", new ActionMessage("error.cierre.noMovimientosFecha",forma.getMesCierre()+"/"+forma.getAnioCierre()));
						saveErrors(request, error);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("paginaPrincipal");
					}*/
					if(this.accionGenerarCierre(con,forma,mundo,usuario))
					{
						UtilidadBD.closeConnection(con);
						return mapping.findForward("resumen");
					}
					else
					{
						ActionErrors error=new ActionErrors();
						error.add("ERROR GENERANDO EL CIERRE.", new ActionMessage("error.errorEnBlanco","Error Generando el Cierre, Inconsistencias en los datos."));
						saveErrors(request, error);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("paginaPrincipal");
					}
				}
				else if(estado.equals("eliminar"))
				{
					mundo.eliminarCierreInventarios(con,forma.getCodigoCierre());
					generarLog(forma,usuario,true);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("resumen");
				}
				else if(estado.equals("reprocesar"))
				{
					//tomar el tipo de cierre, del cierre a reprocesar.
					/*if(ConstantesBDInventarios.tipoCierreAnual==UtilidadInventarios.obtenerTipoCierre(con,forma.getCodigoCierre()))
					{
						forma.setCierreFinal(true);
						forma.setCierreInicial(false);
					}
					else if(ConstantesBDInventarios.tipoCierreInicial==UtilidadInventarios.obtenerTipoCierre(con,forma.getCodigoCierre()))
					{
						forma.setCierreFinal(false);
						forma.setCierreInicial(true);
					}
					else
					{
						forma.setCierreFinal(false);
						forma.setCierreInicial(false);
					}*/

					this.accionCargarMovimientosInventarios(con,forma,mundo);
					/*if(Utilidades.convertirAEntero(mundo.getMovimientos().get("numRegistros")+"")<=0)
					{
						ActionErrors error=new ActionErrors();
						error.add("error fecha sin movimientos", new ActionMessage("error.cierre.noMovimientosFecha",forma.getMesCierre()+"/"+forma.getAnioCierre()));
						saveErrors(request, error);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("paginaPrincipal");
					}*/
					mundo.eliminarCierreInventarios(con,forma.getCodigoCierre());

					if(this.accionGenerarCierre(con,forma,mundo,usuario))
					{
						generarLog(forma,usuario,false);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("resumen");
					}
					else
					{
						ActionErrors error=new ActionErrors();
						error.add("ERROR GENERANDO EL CIERRE.", new ActionMessage("error.errorEnBlanco","Error Generando el Cierre, Inconsistencias en los datos."));
						saveErrors(request, error);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("paginaPrincipal");
					}
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
				logger.error("El form no es compatible con el form de CierreInventariosForm");
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
	
	private boolean accionGenerarCierre(Connection con, CierreInventariosForm forma, CierreInventarios mundo, UsuarioBasico usuario)
	{
		HashMap mapa=new HashMap();
		mapa.put("institucion",forma.getInstitucion()+"");
		mapa.put("aniocierre",forma.getAnioCierre());
		mapa.put("mescierre",forma.getMesCierre());
		mapa.put("observaciones",forma.getObservacionesCierre());
		if(forma.isCierreFinal())
			mapa.put("tipocierre",ConstantesBDInventarios.tipoCierreAnual+"");
		else if(forma.isCierreInicial())
			mapa.put("tipocierre",ConstantesBDInventarios.tipoCierreInicial+"");
		else 
			mapa.put("tipocierre",ConstantesBDInventarios.tipoCierreMensual+"");
		mapa.put("usuario",usuario.getLoginUsuario());
		mundo.setEncabezadoCirre(mapa);
		return mundo.generarCierre(con);
	}
	/**
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param b
	 * @param el
	 */
	private void generarLog(CierreInventariosForm forma, UsuarioBasico usuario, boolean isEliminacion) 
	{
		String log = "";
		int tipoLog=0;
		if(isEliminacion)
		{
			log = 		 "\n   ============CIERRE ELIMINADO=========== " +
			 "\n*  Mes [" +forma.getMesCierre()+"] "+
			 "\n*  Año ["+forma.getAnioCierre()+"] " +
			 "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogEliminacion;
		}
		else
		{
			log = 		 "\n   ============CIERRE REPROCESADO=========== " +
			 "\n*  Mes [" +forma.getMesCierre()+"] "+
			 "\n*  Año ["+forma.getAnioCierre()+"] " +
			 "\n========================================================\n\n\n ";
			tipoLog=ConstantesBD.tipoRegistroLogModificacion;
		}
		LogsAxioma.enviarLog(ConstantesBD.logCierreInventariosCodigo,log,tipoLog,usuario.getLoginUsuario());
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 */
	private void accionCargarMovimientosInventarios(Connection con, CierreInventariosForm forma, CierreInventarios mundo)
	{
		String mesFinal="";
		if(forma.getMesCierre().equals("02"))
			mesFinal="28/"+forma.getMesCierre();
		else if(forma.getMesCierre().equals("04")||forma.getMesCierre().equals("06")||forma.getMesCierre().equals("09")||forma.getMesCierre().equals("11"))
			mesFinal="30/"+forma.getMesCierre();
		else
			mesFinal="31/"+forma.getMesCierre();

		
		mundo.cargarMovimientosInventarios(con,forma.getInstitucion(),"01/"+forma.getMesCierre()+"/"+forma.getAnioCierre(),mesFinal+"/"+forma.getAnioCierre());
	}
}
