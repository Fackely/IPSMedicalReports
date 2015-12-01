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
import util.LogsAxioma;
import util.UtilidadBD;
import util.Utilidades;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.facturacion.IndicativoCargoViaIngresoServicioForm;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.IndicativoCargoViaIngresoServicio;



public class IndicativoCargoViaIngresoServicioAction extends Action
{
	 /**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(IndicativoCargoViaIngresoServicioAction.class);

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
			if (form instanceof IndicativoCargoViaIngresoServicioForm)
			{

				con=UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}

				IndicativoCargoViaIngresoServicioForm forma=(IndicativoCargoViaIngresoServicioForm)form;

				UsuarioBasico usuario = null;
				usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());

				IndicativoCargoViaIngresoServicio mundo=new IndicativoCargoViaIngresoServicio();

				String estado = forma.getEstado();

				logger.info("88888888888888888888888888888888888888888888888888");
				logger.warn("estado --> "+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de IndicativoSolicitudGrupoServiciosAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}			
				else if (estado.equals("empezar"))
				{
					forma.reset();
					forma.setViasIngreso((HashMap)Utilidades.obtenerViasIngreso(con,false));

					//alejo
					forma.setGruposServicios(mundo.consultarGruposServiciosProcedimientosInstitucion(con, usuario.getCodigoInstitucion()));
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}


				else if(estado.equals("seleccionadaViaIngreso")) {
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}

				else if(estado.equals("cargarTipoPaciente")) {
					logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< Cargo Tipo Paciente");
					/*
			  	logger.info("Mapa Grupos Servicios");
				Utilidades.imprimirMapa(forma.getGruposServicios());

				logger.info("Mapa Procedimientos");
				Utilidades.imprimirMapa(forma.getProcedimientos());
				logger.info("Mapa Vias Ingreso");
				Utilidades.imprimirMapa(forma.getViasIngreso());
					 */

					forma.setTipoPacienteMap(UtilidadesManejoPaciente.obtenerTiposPacienteXViaIngreso(con, forma.getViaIngreso()));

					if(forma.getNumTipoPacienteMap()==1)
					{
						logger.info("333333333333- se metio al IF");
						forma.setGruposServicios(mundo.consultarGruposServiciosProcedimientosInstitucion(con, usuario.getCodigoInstitucion()));

						HashMap<String, Object> elemento = (HashMap<String, Object>)forma.getTipoPacienteMap().get(0);
						forma.setTipoPaciente(elemento.get("codigoTipoPaciente")+ConstantesBD.separadorSplit+elemento.get("nombreTipoPaciente"));
					}
					else {
						forma.resetGrupoServicio();
					}

					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}



				else if(estado.equals("cargarProcedimientos"))
				{
					forma.setProcedimientos(new HashMap());
					forma.setProcedimientos("numRegistros","0");
					forma.setProcedimientos(mundo.consultarServiciosGrupoServicioViaIngreso(con, forma.getGrupoServicio(), forma.getViaIngreso(), forma.getTipoPaciente()));
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("detalle");
				}


				else if (estado.equals("consultarGrupServ"))
				{
					forma.setGruposServicios(mundo.consultarGruposServiciosProcedimientosInstitucion(con, usuario.getCodigoInstitucion()));
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("guardar"))
				{

					this.accionGuardarParametrizacion(con,forma,mundo,usuario);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("detalle");
				}
				else
				{
					forma.reset();
					logger.warn("Estado no valido dentro del flujo de IndicativoSolicitudGrupoServicios ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de IndicativoSolicitudGrupoServiciosAction");
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
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 */
	private void accionGuardarParametrizacion(Connection con, IndicativoCargoViaIngresoServicioForm forma, IndicativoCargoViaIngresoServicio mundo, UsuarioBasico usuario)
	{
		int tamanioMapa=Integer.parseInt(forma.getProcedimientos("numRegistros")+"");
		boolean enTransaccion=UtilidadBD.iniciarTransaccion(con);
		
		String [] dato= forma.getTipoPaciente().split(ConstantesBD.separadorSplit);
		logger.info("codigo tipo paciente >>>>"+dato[0]);
		
		
		for(int i=0;i<tamanioMapa;i++)
		{
			if(!mundo.actualizarServicioProcedimientoViaIngreso(con, forma.getViaIngreso(),forma.getProcedimientos("codigo_"+i)+"",usuario.getCodigoInstitucion(),dato[0], forma.getProcedimientos("cargosolicitud_"+i)+"", forma.getProcedimientos("cargoproceso_"+i)+""))
			{
				enTransaccion=false;
				i=tamanioMapa;
			}
			
		}
		if(enTransaccion)
		{
			this.generarLog(forma,usuario);
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}
	}

	private void generarLog(IndicativoCargoViaIngresoServicioForm forma, UsuarioBasico usuario)
	{
		String log = "";
		int tipoLog=2;
		log = 		 "\n   ============INDICATIVO CARGO POR VIA DE INGRESO Y GRUPO DE SERVICIOS/SERVICIO MODIFICADO=========== "+
					"\n Codigo Grupo: "+forma.getGrupoServicio()+" ";
		LogsAxioma.enviarLog(ConstantesBD.logIndicativoCargoViaIngresoGrupoServicioCodigo,log,tipoLog,usuario.getLoginUsuario());
	}
}
