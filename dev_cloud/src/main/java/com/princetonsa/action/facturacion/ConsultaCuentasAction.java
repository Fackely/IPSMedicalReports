/*
 *May 08, 2006 
 */
package com.princetonsa.action.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;

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
import util.ConstantesIntegridadDominio;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.facturacion.ConsultaCuentasForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.manejoPaciente.DtoRequsitosPaciente;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.ObservableBD;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * 
 * @author sgomez
 * Action usado para controlar las consultas de cuentas
 * de pacientes por rangos y por paciente
 */
public class ConsultaCuentasAction extends Action 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ConsultaCuentasAction.class);
	
	/**
	 * M�todo encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(	ActionMapping mapping,
													ActionForm form,
													HttpServletRequest request,
													HttpServletResponse response ) throws Exception
													{
		Connection con=null;
		try{
			if (response==null); //Para evitar que salga el warning
			if(form instanceof ConsultaCuentasForm)
			{

				//SE ABRE CONEXION
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexi�n"+e.toString());
				}

				//OBJETOS A USAR
				ConsultaCuentasForm consultaForm =(ConsultaCuentasForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				String estado=consultaForm.getEstado(); 
				logger.warn("\n\n En ConsultaCuentasAction El Estado   ["+estado+"]  \n\n");

				//**********VALIDACIONES***************************************
				//verificar si es null (Paciente est� cargado)
				if((paciente==null || paciente.getTipoIdentificacionPersona().equals(""))&&estado.equals("consultarPaciente")){
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente no cargado", "errors.paciente.noCargado", true);
				}
				//**************************************************************

				if(estado == null)
				{
					consultaForm.reset();	
					logger.warn("Estado no valido dentro del flujo de Consulta Cuentas (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar"))
				{
					return accionEmpezar(con,consultaForm,mapping,usuario);
				}
				else if (estado.equals("consultarRangos"))
				{
					return accionConsultar(con,consultaForm,mapping,usuario,paciente,false);
				}
				else if (estado.equals("volverListado"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("listado");
				}
				else if (estado.equals("redireccion"))
				{
					return accionRedireccion(con,consultaForm,response,mapping,request);
				}
				else if (estado.equals("ordenar"))
				{
					return accionOrdenar(con,consultaForm,mapping);
				}
				else if (estado.equals("imprimir"))
				{
					return accionImprimir(con,consultaForm,mapping,usuario,request);
				}
				else if (estado.equals("detalle"))
				{
					return accionDetalle(con,consultaForm,mapping,request,usuario);
				}
				else if (estado.equals("consultarPaciente"))
				{	
					return accionConsultar(con,consultaForm,mapping,usuario,paciente,true);
				}
				else if (estado.equals("cargarTipoPaciente"))
				{
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else
				{
					consultaForm.reset();
					logger.warn("Estado no valido dentro del flujo de consulta cuentas (null) ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}

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
	 * M�todo implementado para consultar el detalle de una cuenta
	 * @param con
	 * @param consultaForm
	 * @param mapping
	 * @param request 
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionDetalle(Connection con, ConsultaCuentasForm consultaForm, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) 
	{
		logger.info("c�digo del paciente=> "+consultaForm.getCodigoPaciente());
		//*****SE VERIFICA SI SE DEBE CARGAR PACIENTE EN SESI�N****************
		if(!consultaForm.getCodigoPaciente().equals(""))
		{
			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			ObservableBD observable = (ObservableBD)servlet.getServletContext().getAttribute("observable");
			try 
			{
				paciente.cargar(con, Integer.parseInt(consultaForm.getCodigoPaciente()));
				paciente.cargarPaciente2(con, Integer.parseInt(consultaForm.getCodigoPaciente()), usuario.getCodigoInstitucion(), usuario.getCodigoCentroAtencion()+"");
			} 
			catch (Exception e) 
			{
				logger.info("Error en accionDetalle: "+e);
			}
			observable.addObserver(paciente);
			UtilidadSesion.notificarCambiosObserver(Integer.parseInt(consultaForm.getCodigoPaciente()),servlet.getServletContext());
		}
		//**********************************************************************
		
		//se instancia objeto Cuenta
		Cuenta cuenta = new Cuenta();
		try 
		{
			cuenta.cargar(con,consultaForm.getIdCuenta()+"");
			this.llenarForm(con,consultaForm,cuenta);
			
			UtilidadBD.closeConnection(con);
			return mapping.findForward("detalle");
		} 
		catch (Exception e) 
		{
			logger.error("Error en accionDetalle de ConsultaCuentasAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en ConsultaCuentasAction", "errors.problemasBd", true);
		}
	}


	/**
	 * M�todo implementado para llenar el detalle de la cuenta
	 * en la forma
	 * @param con 
	 * @param consultaForm
	 * @param cuenta
	 */
	private void llenarForm(Connection con, ConsultaCuentasForm consultaForm, Cuenta cuenta) 
	{
		
		//*********DATOS DEL ENCABEZADO*******************************************************
		consultaForm.setDetalle("nombrePaciente",cuenta.getCuenta().getDescripcionPaciente());
		consultaForm.setDetalle("idCuenta",cuenta.getCuenta().getIdCuenta());
		consultaForm.setDetalle("ingreso",cuenta.getCuenta().getConsecutivoIngreso());
		consultaForm.setDetalle("estadoCuenta",cuenta.getCuenta().getDescripcionEstado());
		consultaForm.setDetalle("fechaApertura",cuenta.getCuenta().getFechaApertura());
		consultaForm.setDetalle("usuario",cuenta.getCuenta().getLoginUsuario());
		consultaForm.setDetalle("viaIngreso",cuenta.getCuenta().getDescripcionViaIngreso());
		
		
		//********DATOS DE LA CUENTA Y CONVENIO PRINCIPAL ******************************
		consultaForm.setDetalle("nombreConvenio",cuenta.getCuenta().getConvenios()[0].getConvenio().getNombre());
		consultaForm.setDetalle("numeroContrato",cuenta.getCuenta().getConvenios()[0].getNumeroContrato());
		consultaForm.setDetalle("valorUtilizadoSoat",cuenta.getCuenta().getConvenios()[0].getValorUtilizadoSoat());
		consultaForm.setDetalle("fechaAfiliacion",cuenta.getCuenta().getConvenios()[0].getFechaAfiliacion());
		consultaForm.setDetalle("semanasCotizacion",cuenta.getCuenta().getConvenios()[0].getSemanasCotizacion());
		consultaForm.setDetalle("nombreTipoComplejidad",cuenta.getCuenta().getDescripcionTipoComplejidad());
		consultaForm.setDetalle("nombreEstratoSocial",cuenta.getCuenta().getConvenios()[0].getDescripcionClasificacionSocioEconomica());
		consultaForm.setDetalle("nombreTipoAfiliado",cuenta.getCuenta().getConvenios()[0].getDescripcionTipoAfiliado());
		consultaForm.setDetalle("nombreMontoCobro",cuenta.getCuenta().getConvenios()[0].getDescripcionMontoCobro());
		consultaForm.setDetalle("nombreTipoRegimen",Utilidades.obtenerTipoRegimenConvenio(con, cuenta.getCuenta().getConvenios()[0].getConvenio().getCodigo()+"").split("-")[1]);
		consultaForm.setDetalle("nombreTipoPaciente",cuenta.getCuenta().getDescripcionTipoPaciente());
		consultaForm.setDetalle("nombreNaturaleza",cuenta.getCuenta().getConvenios()[0].getDescripcionNaturalezaPaciente());
		consultaForm.setDetalle("numeroCarnet",cuenta.getCuenta().getConvenios()[0].getNroCarnet());
		consultaForm.setDetalle("nombreTipoEvento",cuenta.getCuenta().getDescripcionTipoEvento());
		consultaForm.setDetalle("nombreArpAfiliado",cuenta.getCuenta().getDescripcionConvenioArpAfiliado());
		consultaForm.setDetalle("numeroPoliza",cuenta.getCuenta().getConvenios()[0].getNroPoliza());
		consultaForm.setDetalle("nombreOrigenAdmision",cuenta.getCuenta().getDescripcionOrigenAdmision());
		consultaForm.setDetalle("autorizacionIngreso",cuenta.getCuenta().getConvenios()[0].getNroAutorizacion());
		consultaForm.setDetalle("nombreArea",cuenta.getCuenta().getDescripcionArea());
		consultaForm.setDetalle("hospitalDia",cuenta.getCuenta().isHospitalDia()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		
		consultaForm.setDetalle("esConvenioPoliza",cuenta.getCuenta().getConvenios()[0].isSubCuentaPoliza()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		if(cuenta.getCuenta().getConvenios()[0].isSubCuentaPoliza())
		{
			consultaForm.setDetalle("apellidosPoliza",cuenta.getCuenta().getConvenios()[0].getTitularPoliza().getApellidos());
			consultaForm.setDetalle("nombresPoliza",cuenta.getCuenta().getConvenios()[0].getTitularPoliza().getNombres());
			consultaForm.setDetalle("descripcionTipoIdPoliza",cuenta.getCuenta().getConvenios()[0].getTitularPoliza().getDescripcionTipoIdentificacion());
			consultaForm.setDetalle("numeroIdPoliza",cuenta.getCuenta().getConvenios()[0].getTitularPoliza().getNumeroIdentificacion());
			consultaForm.setDetalle("direccionPoliza",cuenta.getCuenta().getConvenios()[0].getTitularPoliza().getDireccion());
			consultaForm.setDetalle("telefonoPoliza",cuenta.getCuenta().getConvenios()[0].getTitularPoliza().getTelefono());
			consultaForm.setDetalle("numDatosPoliza",cuenta.getCuenta().getConvenios()[0].getTitularPoliza().getSizeInformacionPoliza());
			
			double saldo = 0;
			for(int i=0;i<cuenta.getCuenta().getConvenios()[0].getTitularPoliza().getSizeInformacionPoliza();i++)
			{
				consultaForm.setDetalle("fechaPoliza_"+i,cuenta.getCuenta().getConvenios()[0].getTitularPoliza().getFechaInformacionPoliza(i));
				consultaForm.setDetalle("autorizacionPoliza_"+i,cuenta.getCuenta().getConvenios()[0].getTitularPoliza().getAutorizacionInformacionPoliza(i));
				consultaForm.setDetalle("valorPoliza_"+i,cuenta.getCuenta().getConvenios()[0].getTitularPoliza().getValorInformacionPoliza(i));
				saldo += Double.parseDouble(consultaForm.getDetalle("valorPoliza_"+i).toString());
			}
			consultaForm.setDetalle("saldoPoliza",UtilidadTexto.formatearValores(saldo));
			
			
			
		}
		
		//****************************SE CARGAN LOS REQUISITOS DEL PACIENTE********************************************************
		//Se cargan los requisitos del paciente que el convenio ten�a registrados
		int numReqIngreso = 0;
		int numReqEgreso = 0;
		for(int i=0;i<cuenta.getCuenta().getConvenios()[0].getRequisitosPaciente().size();i++)
		{
			DtoRequsitosPaciente requisitos = (DtoRequsitosPaciente)cuenta.getCuenta().getConvenios()[0].getRequisitosPaciente().get(i);
			if(requisitos.getTipo().equals(ConstantesIntegridadDominio.acronimoIngreso))
			{
				consultaForm.setDetalle("codigoReqIngreso_"+numReqIngreso, requisitos.getCodigo()+"");
				consultaForm.setDetalle("descripcionReqIngreso_"+numReqIngreso, requisitos.getDescripcion());
				consultaForm.setDetalle("cumplidoReqIngreso_"+numReqIngreso, requisitos.isCumplido()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				numReqIngreso++;
				
			}
			else if(requisitos.getTipo().equals(ConstantesIntegridadDominio.acronimoEgreso))
			{
				consultaForm.setDetalle("codigoReqEgreso_"+numReqEgreso, requisitos.getCodigo()+"");
				consultaForm.setDetalle("descripcionReqEgreso_"+numReqEgreso, requisitos.getDescripcion());
				consultaForm.setDetalle("cumplidoReqEgreso_"+numReqEgreso, requisitos.isCumplido()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				numReqEgreso++;
			}
		}
		consultaForm.setDetalle("numReqIngreso", numReqIngreso+"");
		consultaForm.setDetalle("numReqEgreso", numReqEgreso+"");
		consultaForm.setDetalle("tieneRequisitosPaciente", (numReqEgreso>0||numReqIngreso>0)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		//*******************************************************************************************************************************
		
		//****************************SE CARGAN LOS DEMAS CONVENIOS DE LA CUENTA*****************************************************+
		int cont = 0;
		for(int i=1;i<cuenta.getCuenta().getConvenios().length;i++)
		{
			
			consultaForm.setVariosConvenios("nombreConvenio_"+cont, cuenta.getCuenta().getConvenios()[i].getConvenio().getNombre());
			consultaForm.setVariosConvenios("numeroContrato_"+cont, cuenta.getCuenta().getConvenios()[i].getNumeroContrato());
			consultaForm.setVariosConvenios("valorUtilizadoSoat_"+cont, cuenta.getCuenta().getConvenios()[i].getValorUtilizadoSoat());
			consultaForm.setVariosConvenios("fechaAfiliacion_"+cont, cuenta.getCuenta().getConvenios()[i].getFechaAfiliacion());
			consultaForm.setVariosConvenios("semanasCotizacion_"+cont, cuenta.getCuenta().getConvenios()[i].getSemanasCotizacion()+"");
			consultaForm.setVariosConvenios("nombreEstratoSocial_"+cont, cuenta.getCuenta().getConvenios()[i].getDescripcionClasificacionSocioEconomica());
			consultaForm.setVariosConvenios("nombreTipoAfiliado_"+cont, cuenta.getCuenta().getConvenios()[i].getDescripcionTipoAfiliado());
			consultaForm.setVariosConvenios("nombreMontoCobro_"+cont, cuenta.getCuenta().getConvenios()[i].getDescripcionMontoCobro());
			consultaForm.setVariosConvenios("nombreTipoRegimen_"+cont, Utilidades.obtenerTipoRegimenConvenio(con, cuenta.getCuenta().getConvenios()[i].getConvenio().getCodigo()+"").split("-")[1]);
			consultaForm.setVariosConvenios("nombreNaturaleza_"+cont, cuenta.getCuenta().getConvenios()[i].getDescripcionNaturalezaPaciente());
			consultaForm.setVariosConvenios("numeroCarnet_"+cont, cuenta.getCuenta().getConvenios()[i].getNroCarnet());
			consultaForm.setVariosConvenios("numeroPoliza_"+cont, cuenta.getCuenta().getConvenios()[i].getNroPoliza());
			consultaForm.setVariosConvenios("autorizacionIngreso_"+cont, cuenta.getCuenta().getConvenios()[i].getNroAutorizacion());
			
			//Se carga la informacion de poliza del convenio ----------------------------------------------------------------------------
			if(cuenta.getCuenta().getConvenios()[i].isSubCuentaPoliza())
			{
				consultaForm.setVariosConvenios("esConvenioPoliza_"+cont, ConstantesBD.acronimoSi);
				consultaForm.setVariosConvenios("apellidosPoliza_"+cont, cuenta.getCuenta().getConvenios()[i].getTitularPoliza().getApellidos());
				consultaForm.setVariosConvenios("nombresPoliza_"+cont, cuenta.getCuenta().getConvenios()[i].getTitularPoliza().getNombres());
				consultaForm.setVariosConvenios("tipoIdPoliza_"+cont, cuenta.getCuenta().getConvenios()[i].getTitularPoliza().getCodigoTipoIdentificacion());
				consultaForm.setVariosConvenios("descripcionTipoIdPoliza_"+cont, cuenta.getCuenta().getConvenios()[i].getTitularPoliza().getDescripcionTipoIdentificacion());
				consultaForm.setVariosConvenios("numeroIdPoliza_"+cont, cuenta.getCuenta().getConvenios()[i].getTitularPoliza().getNumeroIdentificacion());
				consultaForm.setVariosConvenios("direccionPoliza_"+cont, cuenta.getCuenta().getConvenios()[i].getTitularPoliza().getDireccion());
				consultaForm.setVariosConvenios("telefonoPoliza_"+cont, cuenta.getCuenta().getConvenios()[i].getTitularPoliza().getTelefono());
				
				consultaForm.setVariosConvenios("numDatosPoliza_"+cont, cuenta.getCuenta().getConvenios()[i].getTitularPoliza().getSizeInformacionPoliza()+"");
				double saldo = 0;
				
				for(int j=0;j<cuenta.getCuenta().getConvenios()[i].getTitularPoliza().getSizeInformacionPoliza();j++)
				{
					consultaForm.setVariosConvenios("codigoPoliza_"+cont+"_"+j, cuenta.getCuenta().getConvenios()[i].getTitularPoliza().getCodigoInformacionPoliza(j));
					consultaForm.setVariosConvenios("fechaPoliza_"+cont+"_"+j, cuenta.getCuenta().getConvenios()[i].getTitularPoliza().getFechaInformacionPoliza(j));
					consultaForm.setVariosConvenios("autorizacionPoliza_"+cont+"_"+j, cuenta.getCuenta().getConvenios()[i].getTitularPoliza().getAutorizacionInformacionPoliza(j));
					consultaForm.setVariosConvenios("valorPoliza_"+cont+"_"+j, cuenta.getCuenta().getConvenios()[i].getTitularPoliza().getValorInformacionPoliza(j));
					saldo += Double.parseDouble(cuenta.getCuenta().getConvenios()[i].getTitularPoliza().getValorInformacionPoliza(j));
				}
				consultaForm.setVariosConvenios("saldoPoliza_"+cont, UtilidadTexto.formatearValores(saldo,"0.00"));
				
			}
			else
				consultaForm.setVariosConvenios("esConvenioPoliza_"+cont, ConstantesBD.acronimoNo);
			
			//Se cargan los requisitos del paciente del convenio -----------------------------------------------------------------
			//1Se cargan los requisirtos que el convenio tiene hasta el momento registrados
			numReqIngreso = 0;
			numReqEgreso = 0;
			for(int j=0;j<cuenta.getCuenta().getConvenios()[i].getRequisitosPaciente().size();j++)
			{
				DtoRequsitosPaciente requisitos = (DtoRequsitosPaciente)cuenta.getCuenta().getConvenios()[i].getRequisitosPaciente().get(j);
				if(requisitos.getTipo().equals(ConstantesIntegridadDominio.acronimoIngreso))
				{
					consultaForm.setVariosConvenios("codigoReqIngreso_"+cont+"_"+numReqIngreso, requisitos.getCodigo()+"");
					consultaForm.setVariosConvenios("descripcionReqIngreso_"+cont+"_"+numReqIngreso, requisitos.getDescripcion());
					consultaForm.setVariosConvenios("cumplidoReqIngreso_"+cont+"_"+numReqIngreso, requisitos.isCumplido()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
					numReqIngreso++;
					
				}
				else if(requisitos.getTipo().equals(ConstantesIntegridadDominio.acronimoEgreso))
				{
					consultaForm.setVariosConvenios("codigoReqEgreso_"+cont+"_"+numReqEgreso, requisitos.getCodigo()+"");
					consultaForm.setVariosConvenios("descripcionReqEgreso_"+cont+"_"+numReqEgreso, requisitos.getDescripcion());
					consultaForm.setVariosConvenios("cumplidoReqEgreso_"+cont+"_"+numReqEgreso, requisitos.isCumplido()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
					numReqEgreso++;
				}
			}
			consultaForm.setVariosConvenios("numReqIngreso_"+cont, numReqIngreso+"");
			consultaForm.setVariosConvenios("numReqEgreso_"+cont, numReqEgreso+"");
			consultaForm.setVariosConvenios("tieneRequisitosPaciente_"+cont, (numReqEgreso>0||numReqIngreso>0)?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			
			//se llena la informacion de verificacion de derechos -----------------------------------------------------
			consultaForm.setVariosConvenios("tieneVerificacionDerechos_"+cont, 
					cuenta.getCuenta().getConvenios()[i].isSubCuentaVerificacionDerechos()?
							ConstantesBD.acronimoSi:
							ConstantesBD.acronimoNo);
			
			if(cuenta.getCuenta().getConvenios()[i].isSubCuentaVerificacionDerechos())
			{
				consultaForm.setVariosConvenios("nombreEstado_"+cont, cuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getDescripcionEstado());
				consultaForm.setVariosConvenios("nombreTipo_"+cont, cuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getDescripcionTipo());
				consultaForm.setVariosConvenios("numero_"+cont, cuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getNumero());
				consultaForm.setVariosConvenios("personaSolicita_"+cont, cuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getPersonaSolicita());
				consultaForm.setVariosConvenios("fechaSolicitud_"+cont, cuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getFechaSolicitud());
				consultaForm.setVariosConvenios("horaSolicitud_"+cont, cuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getHoraSolicitud());
				consultaForm.setVariosConvenios("personaContactada_"+cont, cuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getPersonaContactada());
				consultaForm.setVariosConvenios("fechaVerificacion_"+cont, cuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getFechaVerificacion());
				consultaForm.setVariosConvenios("horaVerificacion_"+cont, cuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getHoraVerificacion());
				consultaForm.setVariosConvenios("porcentajeCobertura_"+cont, cuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getPorcentajeCobertura());
				consultaForm.setVariosConvenios("cuotaVerificacion_"+cont, cuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getCuotaVerificacion());
				consultaForm.setVariosConvenios("observaciones_"+cont, cuenta.getCuenta().getConvenios()[i].getVerificacionDerechos().getObservaciones());
			}
			
			
			cont++;
		}
		consultaForm.setVariosConvenios("numRegistros", cont+"");
		consultaForm.setDetalle("seccionVariosConvenios", ConstantesBD.acronimoNo);
		//***********************************************************************************************************************
		//*******************************SE CARGA LA VERIFICACION DE DERECHOS***************************************************************
		//se llena la informacion de verificacion de derechos
		consultaForm.setDetalle("seccionVerificacionDerechos", ConstantesBD.acronimoNo);
		consultaForm.setDetalle("tieneVerificacionDerechos",cuenta.getCuenta().getConvenios()[0].isSubCuentaVerificacionDerechos()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		
		//Se verifica si hay verificacion de derechos
		if(cuenta.getCuenta().getConvenios()[0].isSubCuentaVerificacionDerechos())
		{
			
			consultaForm.setVerificacion("nombreEstado", cuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getDescripcionEstado());
			consultaForm.setVerificacion("nombreTipo", cuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getDescripcionTipo());
			consultaForm.setVerificacion("numero", cuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getNumero());
			consultaForm.setVerificacion("personaSolicita", cuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getPersonaSolicita());
			consultaForm.setVerificacion("fechaSolicitud", cuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getFechaSolicitud());
			consultaForm.setVerificacion("horaSolicitud", cuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getHoraSolicitud());
			consultaForm.setVerificacion("personaContactada", cuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getPersonaContactada());
			consultaForm.setVerificacion("fechaVerificacion", cuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getFechaVerificacion());
			consultaForm.setVerificacion("horaVerificacion", cuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getHoraVerificacion());
			consultaForm.setVerificacion("porcentajeCobertura", cuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getPorcentajeCobertura());
			consultaForm.setVerificacion("cuotaVerificacion", cuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getCuotaVerificacion());
			consultaForm.setVerificacion("observaciones", cuenta.getCuenta().getConvenios()[0].getVerificacionDerechos().getObservaciones());
			
		}
		
		
		
		//*********************************************************************************************************************************
		
		//***************************SE CARGA EL RESPONSABLE DEL PACIENTE*******************************************************************
		
		//Se llena el responsable del paciente
		consultaForm.setDetalle("seccionResponsablePaciente", ConstantesBD.acronimoNo);
		consultaForm.setDetalle("tieneResponsablePaciente", cuenta.getCuenta().isTieneResponsablePaciente()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		
		if(cuenta.getCuenta().isTieneResponsablePaciente())
		{
			consultaForm.setResponsable("nombrePaisExpedicion", cuenta.getCuenta().getResponsablePaciente().getDescripcionPaisExpedicion());
			consultaForm.setResponsable("nombrePais", cuenta.getCuenta().getResponsablePaciente().getDescripcionPais());
			consultaForm.setResponsable("descripcionTipoIdentificacion", cuenta.getCuenta().getResponsablePaciente().getDescripcionTipoIdentificacion());
			consultaForm.setResponsable("numeroIdentificacion", cuenta.getCuenta().getResponsablePaciente().getNumeroIdentificacion());
			consultaForm.setResponsable("nombreCiudadExpedicion", cuenta.getCuenta().getResponsablePaciente().getDescripcionCiudadExpedicion());
			consultaForm.setResponsable("nombreCiudad", cuenta.getCuenta().getResponsablePaciente().getDescripcionCiudad());
			consultaForm.setResponsable("primerApellido", cuenta.getCuenta().getResponsablePaciente().getPrimerApellido());
			consultaForm.setResponsable("segundoApellido", cuenta.getCuenta().getResponsablePaciente().getSegundoApellido());
			consultaForm.setResponsable("primerNombre", cuenta.getCuenta().getResponsablePaciente().getPrimerNombre());
			consultaForm.setResponsable("segundoNombre", cuenta.getCuenta().getResponsablePaciente().getSegundoNombre());
			consultaForm.setResponsable("direccion", cuenta.getCuenta().getResponsablePaciente().getDireccion());
			consultaForm.setResponsable("nombreBarrio", cuenta.getCuenta().getResponsablePaciente().getDescripcionBarrio());
			consultaForm.setResponsable("telefono", cuenta.getCuenta().getResponsablePaciente().getTelefono());
			consultaForm.setResponsable("fechaNacimiento", cuenta.getCuenta().getResponsablePaciente().getFechaNacimiento());
			consultaForm.setResponsable("relacionPaciente", cuenta.getCuenta().getResponsablePaciente().getRelacionPaciente());
		}
		//****************************************************************************************************************************
		
	
		
	}


	/**
	 * M�todo implementado para imprimir el listado de cuentas
	 * @param con
	 * @param consultaForm
	 * @param mapping
	 * @param request 
	 * @param usuario 
	 * @return
	 */
	private ActionForward accionImprimir(Connection con, ConsultaCuentasForm consultaForm, ActionMapping mapping, UsuarioBasico usuario, HttpServletRequest request) 
	{
		String rutaImagen="";
		//Directorio Imagenes
		String directorioImagenes = ValoresPorDefecto.getDirectorioImagenes();
		String nombreRptDesign = "ConsultaCuentas.rptdesign";
		InstitucionBasica ins= new InstitucionBasica();
		ins.cargarXConvenio(usuario.getCodigoInstitucionInt(), consultaForm.getConvenio());
	
		int pos=ins.getLogoReportes().lastIndexOf("/");
		int pos2=ins.getLogoReportes().lastIndexOf("\\");
					
		if (pos != -1){
			rutaImagen= directorioImagenes+ins.getLogoReportes().substring(pos+1,ins.getLogoReportes().length());
		} else if (pos2 != -1){
				rutaImagen= directorioImagenes+ins.getLogoReportes().substring(pos2+1,ins.getLogoReportes().length());
		}else{
			rutaImagen= directorioImagenes+ins.getLogoReportes();
		}
		
		//PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, rutaImagen);
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(ins.getDescripcionTipoIdentificacion()+"         "+ins.getNit());     
        if(!ins.getActividadEconomica().equals(""))//si no se posee actividad economica no mostrar el campo
        	v.add("Actividad Econ�mica: "+ins.getActividadEconomica());
        v.add(ins.getDireccion()+"          "+ins.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        comp.insertLabelInGridPpalOfHeader(1,0, "LISTADO CUENTAS");
        
        comp.insertLabelInGridPpalOfFooter(2, 0, "Usuario: " + usuario.getLoginUsuario());
        //SEGUNDO SE MODIFICA LA CONSULTA 
        comp.obtenerComponentesDataSet("listadoCuentas");            
        String oldQuery=comp.obtenerQueryDataSet();
        
       
        //se filtra la consulta
        oldQuery += " WHERE (to_char(c.fecha_apertura, 'YYYY-MM-DD') BETWEEN '"+
        	UtilidadFecha.conversionFormatoFechaABD(consultaForm.getFechaInicial())+"' AND '"+
        	UtilidadFecha.conversionFormatoFechaABD(consultaForm.getFechaFinal())+"') ";
        //se verifica estado cuenta
        if(consultaForm.getEstadoCuenta()>-1)
        	oldQuery += " AND c.estado_cuenta = "+consultaForm.getEstadoCuenta();
        //se verifica cuenta inicial y final
		if(!consultaForm.getCuentaInicial().equals("")&&
			!consultaForm.getCuentaFinal().equals("")&&
			Integer.parseInt(consultaForm.getCuentaInicial())>0&&
			Integer.parseInt(consultaForm.getCuentaFinal())>0)
			oldQuery += " AND (c.id BETWEEN "+consultaForm.getCuentaInicial()+" AND "+consultaForm.getCuentaFinal()+") ";
		
		//se verifica via de ingreso
		if(consultaForm.getViaIngreso()>0)
			oldQuery += " AND c.via_ingreso = "+consultaForm.getViaIngreso();
		
		//se verifica convenio
		if(consultaForm.getConvenio()>0)
			oldQuery += " AND getExisteConvenioIngreso(c.id_ingreso,"+consultaForm.getConvenio()+") = '"+ConstantesBD.acronimoSi+"' ";
		
		//se verifica usuario
		if(!consultaForm.getUsuario().equals(""))
			oldQuery += " AND c.usuario_modifica = '"+consultaForm.getUsuario()+"'"; 
		
		//se verifica tipo evento
		if(!consultaForm.getCodigoTipoEvento().equals(""))
			oldQuery += " AND c.tipo_evento = '"+consultaForm.getCodigoTipoEvento()+"'";
		
		//se verifica centro de atencion
		if(consultaForm.getCentroAtencion()>0)
			oldQuery += " AND cc.centro_atencion = '"+consultaForm.getCentroAtencion()+"'";
        
		
		//La ordenaci�n debe ser igual a la del listado actual
		//oldQuery += " ORDER BY "+consultaForm.getRegistroOrdenacion()+" cuenta ASC ";
		oldQuery += " ORDER BY c.id ASC ";
		
		logger.info("\n\nOOOLDDDD QUERYYY RANGOS CUENTAS>>>>>>>>"+oldQuery+"\n\n");
		
		//se actualiza Query
        comp.modificarQueryDataSet(oldQuery);
        
        //SE ENVIAN LOS ATRIBUTOS AL JSP PARA IMPRIMIR
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
        //SE ACTUALIZAN LOS PARAMETROS DE CONEXION CON LA BD
        comp.updateJDBCParameters(newPathReport);
        
        UtilidadBD.closeConnection(con);
		return mapping.findForward("listado");
	}

	/**
	 * M�todo implementado para ordenar el listado por una columna espec�fica
	 * @param con
	 * @param consultaForm
	 * @param mapping
	 * @return
	 */
	private ActionForward accionOrdenar(Connection con, ConsultaCuentasForm consultaForm, ActionMapping mapping) 
	{
		//columnas del listado
		String[] indices={
				"cuenta_",
				"fecha_",
				"hora_",
				"tipo_identificacion_",
				"numero_identificacion_",
				"estadocuenta_",
				"usuario_",
				"viaingreso_",
				"ingreso_",
				"paciente_",
				"convenio_",
				"tipo_evento_",
				"centro_atencion_",
				"codigo_paciente_",
				"hospital_dia_",
				"descripcionentidadsub_",
				"indpre_"
			};
		
		int numeroElementos = consultaForm.getNumRegistros();
		
		//Se prepara el campo v�a de ingreso para ordenacion
		//se debe tomar en cuenta el campo hospital d�a
		for(int i=0;i<numeroElementos;i++)
			consultaForm.setListado("viaingreso_"+i, consultaForm.getListado("viaingreso_"+i)+"-"+consultaForm.getListado("hospital_dia_"+i));
		
		//edici�n del registro de ordenaci�n
		String registro = consultaForm.getRegistroOrdenacion();
		String porcion =  consultaForm.getIndice().split("_")[0];
		if(consultaForm.getIndice().equals(consultaForm.getUltimoIndice()))
			porcion += " DESC,";
		else
			porcion += " ASC,";
		registro = porcion + registro;
		consultaForm.setRegistroOrdenacion(registro);
		
		logger.info("entr� a la ordenacion del listado!!!!!!!");
		consultaForm.setListado(Listado.ordenarMapa(indices,
				consultaForm.getIndice(),
				consultaForm.getUltimoIndice(),
				consultaForm.getListado(),
				consultaForm.getNumRegistros()));
		logger.info("TERMIN� la ordenacion del listado!!!!!!!");
		consultaForm.setListado("numRegistros",numeroElementos+"");
		consultaForm.setUltimoIndice(consultaForm.getIndice());
		
		//Se reestablece el campo de v�a de ingreso
		for(int i=0;i<numeroElementos;i++)
			consultaForm.setListado("viaingreso_"+i, consultaForm.getListado("viaingreso_"+i).toString().split("-")[0]);
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listado");
	}

	/**
	 * M�todo implementado para paginar el listado de cuentas
	 * @param con
	 * @param consultaForm
	 * @param response
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionRedireccion(Connection con, ConsultaCuentasForm consultaForm, HttpServletResponse response, ActionMapping mapping, HttpServletRequest request) 
	{
		try
		{
			
			UtilidadBD.closeConnection(con);
			response.sendRedirect(consultaForm.getLinkSiguiente());
			return null;
		}
		catch(Exception e)
		{
			logger.error("Error en accionRedireccion de ConsultaCuentasAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en ConsultaCuentasAction", "errors.problemasDatos", true);
		}
	}

	/**
	 * M�todo implementado para listar las cuentas
	 * @param con
	 * @param consultaForm
	 * @param mapping
	 * @param usuario
	 * @param paciente 
	 * @param esPaciente 
	 * @return
	 */
	private ActionForward accionConsultar(Connection con, ConsultaCuentasForm consultaForm, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, boolean esPaciente) 
	{
	
//		se instancia mundo de Cuenta
		Cuenta cuenta=new Cuenta();
	
		
		
		
		
		//si la b�squeda es por rangos se llena el mundo Cuenta
		if(!esPaciente)
		{
			//se llena el objeto cuenta con los datos de la forma
			this.llenarMundo(consultaForm,cuenta);
		}
		
		//se asigna el max page items
		consultaForm.setMaxPageItems(Integer.parseInt(ValoresPorDefecto.getMaxPageItems(usuario.getCodigoInstitucionInt())));
		
		//se consulta el listado de cuentas

		if(esPaciente)
		{
		
			consultaForm.setListado(cuenta.consultarCuentas(con,paciente.getCodigoPersona()));
		}	
		else
		{
		
			consultaForm.setListado(cuenta.consultarCuentas(con));
		}	
		consultaForm.setNumRegistros(Integer.parseInt(consultaForm.getListado("numRegistros").toString()));
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listado");
	}

	/**
	 * 
	 * @param consultaForm
	 * @param cuenta
	 */
	private void llenarMundo(ConsultaCuentasForm consultaForm, Cuenta cuenta) 
	{
		cuenta.setFechaInicial(consultaForm.getFechaInicial());
		cuenta.setFechaFinal(consultaForm.getFechaFinal());
		cuenta.setCodigoEstadoCuenta(consultaForm.getEstadoCuenta()+"");
		
		if(!consultaForm.getCuentaInicial().equals(""))
			cuenta.setCuentaInicial(Integer.parseInt(consultaForm.getCuentaInicial()));
		if(!consultaForm.getCuentaFinal().equals(""))
			cuenta.setCuentaFinal(Integer.parseInt(consultaForm.getCuentaFinal()));
		
		cuenta.setUsuario(consultaForm.getUsuario());
		cuenta.setCodigoViaIngreso(consultaForm.getViaIngreso()+"");
		cuenta.setTipoPaciente(consultaForm.getTipoPaciente()+"");
		cuenta.setCodigoConvenio(consultaForm.getConvenio()+"");
		cuenta.setCodigoTipoEvento(consultaForm.getCodigoTipoEvento());
		
		cuenta.setCentroAtencion(consultaForm.getCentroAtencion());

		cuenta.setCodigoEntidadSub(consultaForm.getCodigoEntidadSub()+"");
	}

	/**
	 * M�todo implementado para inicializar la funcionalidad de consulta cuentas por rangos 
	 * @param con
	 * @param consultaForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, ConsultaCuentasForm consultaForm, ActionMapping mapping, UsuarioBasico usuario) 
	{
		consultaForm.reset();
		//almacena la informacion de las empresas subcontratadas
		consultaForm.setEntidadesSubList(UtilidadesManejoPaciente.obtenerEntidadesSubcontratadas(con, usuario.getCodigoInstitucionInt()));
		
		consultaForm.setInstitucion(usuario.getCodigoInstitucion());
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * M�todo en que se cierra la conexi�n (Buen manejo
	 * recursos), usado ante todo al momento de hacer un forward
	 * @param con Conexi�n con la fuente de datos
	 */
	private void cerrarConexion (Connection con)
	{
	    try
		{
	        UtilidadBD.cerrarConexion(con);
	    }
	    catch(Exception e)
		{
	        logger.error(e+"Error al tratar de cerrar la conexion con la fuente de datos. \n Excepcion: " +e.toString());
	    }
	}
}
