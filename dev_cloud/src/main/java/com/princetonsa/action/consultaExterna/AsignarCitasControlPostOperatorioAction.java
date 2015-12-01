package com.princetonsa.action.consultaExterna;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
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
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.RespuestaValidacion;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.Utilidades;
import util.facturacion.InfoResponsableCobertura;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.consultaExterna.AsignarCitasControlPostOperatorioForm;
import com.princetonsa.dto.manejoPaciente.DtoCuentas;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.agenda.Cita;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.Contrato;
import com.princetonsa.mundo.consultaExterna.AsignarCitasControlPostOperatorio;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.princetonsa.mundo.ordenesmedicas.cirugias.SolicitudesCx;
import com.princetonsa.mundo.salasCirugia.Peticion;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.mundo.solicitudes.SolicitudProcedimiento;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * @author Mauricio Jllo
 * Fecha Mayo de 2008
 */
public class AsignarCitasControlPostOperatorioAction extends Action
{

	/**
	 * Objeto para manejar los logs de esta clase
	 */
	Logger logger = Logger.getLogger(AsignarCitasControlPostOperatorioAction.class);
	
	/**
	 * Método execute del Action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{

		Connection con = null;
		try {

			if (form instanceof AsignarCitasControlPostOperatorioForm) 
			{

				//Abrimos la conexion con la fuente de Datos 
				con = util.UtilidadBD.abrirConexion();
				if(con == null)
				{
					request.setAttribute("codigoDescripcionError", "errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				AsignarCitasControlPostOperatorioForm forma = (AsignarCitasControlPostOperatorioForm) form;
				AsignarCitasControlPostOperatorio mundo = new AsignarCitasControlPostOperatorio();
				HttpSession session = request.getSession();
				UsuarioBasico usuario = Utilidades.getUsuarioBasicoSesion(request.getSession());
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
				String estado = forma.getEstado();
				logger.warn("[AsignarCitasControlPostOperatorio]--->Estado: "+estado);

				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo estado is NULL");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if(estado.equals("empezar"))
				{
					return accionEmpezar(con, forma, request, paciente, mundo, usuario, mapping);
				}
				else if(estado.equals("asignar"))
				{
					return accionResponderCita(con, forma, mundo, mapping, request, response, usuario, paciente);
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
				logger.error("El form no es compatible con el form de AsignarCitasControlPostOperatorioForm");
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
	 * Método que carga los datos de las citas reservadas de Control
	 * Post Operatorio a las variables establecidas en la forma
	 * @param pos 
	 * @param citasForm
	 */
	private void cargarDatosReservaCitasControlPostOperatorio(AsignarCitasControlPostOperatorioForm forma) 
	{
		forma.setCodigoPaciente(forma.getBusquedaCitasReservadas("codigopaciente_"+forma.getPosicion())+"");
		forma.setCodigoCita(forma.getBusquedaCitasReservadas("codigocita_"+forma.getPosicion())+"");
		forma.setCodigoAgenda(forma.getBusquedaCitasReservadas("agenda_"+forma.getPosicion())+"");
		forma.setCodigoEstadoCita(forma.getBusquedaCitasReservadas("codigocita_"+forma.getPosicion())+"");
		forma.setEstadoLiquidacionCita(forma.getBusquedaCitasReservadas("estadoliquidacion_"+forma.getPosicion())+"");
		forma.setNumeroSolicitudCita(forma.getBusquedaCitasReservadas("numerosolicitud_"+forma.getPosicion())+"");
		forma.setCodigoServicioCita(forma.getBusquedaCitasReservadas("servicio_"+forma.getPosicion())+"");
		forma.setCodigoEspecialidad(forma.getBusquedaCitasReservadas("especialidad_"+forma.getPosicion())+"");
		forma.setCodigoCentroCosto(forma.getBusquedaCitasReservadas("centrocosto_"+forma.getPosicion())+"");
		forma.setCodigoTipoServicio(forma.getBusquedaCitasReservadas("tiposervicio_"+forma.getPosicion())+"");
	}
	
	/**
	 * Metodo que ejecuta la consulta que se muestra al principio de la funcionalidad.
	 * Citas Reservadas por Control Post-Operatorio
	 * @param con
	 * @param forma
	 * @param request 
	 * @param paciente 
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 * @return
	 */
	private ActionForward accionEmpezar(Connection con, AsignarCitasControlPostOperatorioForm forma, HttpServletRequest request, PersonaBasica paciente, AsignarCitasControlPostOperatorio mundo, UsuarioBasico usuario, ActionMapping mapping)
	{
		forma.reset();
		int contador = 0;
		/**
		 * Validacion Cargado Paciente
		 */
		if(paciente.getCodigoPersona()<1)
		    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.noCargado", "errors.paciente.noCargado", true);
		else
		{
			logger.info("\n=====>Codigo del Paciente: "+paciente.getCodigoPersona());
			forma.setBusquedaCitasReservadas(mundo.consultarCitasReservadas(con, paciente.getCodigoPersona()));
			int numRegistros = Utilidades.convertirAEntero(forma.getBusquedaCitasReservadas("numRegistros")+"");
			logger.info("Numero de Registros: "+numRegistros);
			for(int i=0; i < numRegistros; i++)
			{
				if(forma.getBusquedaCitasReservadas("sumatoria_"+i).equals(ConstantesBD.acronimoSi))
				{
					forma.setMensajeAdvertencia(new ResultadoBoolean(true, "El paciente tiene reserva de cita por control post-operatorio con servicios de tipo procedimiento y/o no cruento. No se permite asignar por este flujo."));
					contador++;
				}
			}
			if(contador == numRegistros)
				forma.setMostrarCitas(true);
		}
			
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	/**
	 * Metodo que ejecuta la asignacion de Citas Reservadas por Control Post-Operatorio
	 * @param con
	 * @param forma
	 * @param mundo 
	 * @param mapping
	 * @param request
	 * @param response
	 * @param usuario
	 * @param paciente 
	 * @return
	 */
	private ActionForward accionResponderCita(Connection con, AsignarCitasControlPostOperatorioForm forma, AsignarCitasControlPostOperatorio mundo, ActionMapping mapping, HttpServletRequest request, HttpServletResponse response, UsuarioBasico usuario, PersonaBasica paciente) throws IPSException
	{
		forma.setMensaje(new ResultadoBoolean(false, ""));
		ActionErrors errores = new ActionErrors();
		int numRegistros = Utilidades.convertirAEntero(forma.getBusquedaCitasReservadas("numRegistros")+"");
		int numErrores = 0, contador = 0;
		boolean huboErrores = false, tieneCargos = false;
		
		//Validamos que el paciente tenga ingreso cargado en sesion
		boolean pacienteCuentaAbierta = false;
		if(paciente.getCodigoCuenta()>0)
			pacienteCuentaAbierta = true;
		else
			pacienteCuentaAbierta = false;
		
		logger.info("=====>Paciente con Ingreso Abierto: "+pacienteCuentaAbierta);
		for(int i=0; i < numRegistros; i++)
		{
			if(UtilidadCadena.noEsVacio(forma.getBusquedaCitasReservadas("hdasignar_"+i)+"") && (forma.getBusquedaCitasReservadas("hdasignar_"+i)).equals(ConstantesBD.acronimoSi))
			{
				logger.info("\n===>Check Asignar en la posicion "+i+": "+forma.getBusquedaCitasReservadas("hdasignar_"+i));
				numErrores = errores.size();
				//**********SE PASA LA INFORMACION DEL MAPA A LAS VARIABLES****************************
				forma.setPosicion(i);
				cargarDatosReservaCitasControlPostOperatorio(forma);
				//*************************************************************************************
				UtilidadBD.iniciarTransaccion(con);
				//Se verifa Valores por defecto
				errores = procesoCitaReservadaSinLiquidar(con, paciente, forma, mundo, mapping, request, errores, pacienteCuentaAbierta);
				logger.info("\n=====>NUM ERRORES: "+numErrores);
				logger.info("\n=====>ERRORES SIZE: "+errores.size());
				if(numErrores!=errores.size())
				{
					//aborto transaccion
					UtilidadBD.abortarTransaccion(con);
				}
				else
				{
					//finalizo transaccion
					UtilidadBD.finalizarTransaccion(con);
					huboErrores = true;
				}
			}
			
		}
		
		if(huboErrores)
		{
			forma.setMensaje(new ResultadoBoolean(true, "OPERACIÓN REALIZADA CON ÉXITO!!!"));
			tieneCargos = UtilidadesFacturacion.puedoCerrarIngreso(con, paciente.getCodigoIngreso());
			logger.info("\n====>Puedo Cerrar Ingreso: "+tieneCargos);
			if (tieneCargos)
			{
				IngresoGeneral.actualizarEstadoIngreso(con, paciente.getCodigoIngreso()+"", ConstantesIntegridadDominio.acronimoEstadoCerrado, usuario.getLoginUsuario());
				Cuenta cuenta = new Cuenta();
				int cerrarCuenta = cuenta.cambiarEstadoCuentaNoTransaccional(con, paciente.getCodigoCuenta(), ConstantesBD.codigoEstadoCuentaCerrada);
				logger.info("\n====>Cerrar Cuenta: "+cerrarCuenta);
				UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);
			}
		}
		
		if(!errores.isEmpty())
		{
			this.accionEmpezar(con, forma, request, paciente, mundo, usuario, mapping);
			saveErrors(request, errores);
			return mapping.findForward("principal");
		}
		else
		{
			forma.resetMapaCitas();
			logger.info("\n=====>Codigo del Paciente: "+paciente.getCodigoPersona());
			forma.setBusquedaCitasReservadas(mundo.consultarCitasReservadas(con, paciente.getCodigoPersona()));
			numRegistros = Utilidades.convertirAEntero(forma.getBusquedaCitasReservadas("numRegistros")+"");
			for(int i=0; i < numRegistros; i++)
			{
				if(forma.getBusquedaCitasReservadas("sumatoria_"+i).equals(ConstantesBD.acronimoSi))
				{
					forma.setMensajeAdvertencia(new ResultadoBoolean(true, "El paciente tiene reserva de cita por control post-operatorio con servicios de tipo procedimiento y/o no cruento. No se permite asignar por este flujo."));
					contador++;
				}
			}
			if(contador == numRegistros)
				forma.setMostrarCitas(true);
			
			UtilidadBD.closeConnection(con);
			return mapping.findForward("principal");
		}
	}

	/**
	 * Método implementado para realizar el proceso de atender citas resevadas sin liquidar,
	 * incluye creación automática de cuenta y solicitudes
	 * @param con
	 * @param paciente
	 * @param mundo 
	 * @param citasForm
	 * @param mapping 
	 * @param request
	 * @param pacienteCuentaAbierta 
	 * @param errores2 
	 * @return
	 */
	private ActionErrors procesoCitaReservadaSinLiquidar(Connection con, PersonaBasica paciente, AsignarCitasControlPostOperatorioForm forma, AsignarCitasControlPostOperatorio mundo, ActionMapping mapping, HttpServletRequest request, ActionErrors errores, boolean pacienteCuentaAbierta) throws IPSException 
	{
		HashMap infoReserva = new HashMap();
		UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
		ArrayList<DtoSubCuentas> temSubCuentas = new ArrayList<DtoSubCuentas>();
		String [] temp = new String [0];
		boolean controlPostOperatorio = false;
		
		int codigoSubCuenta = ConstantesBD.codigoNuncaValido;
		
		//Variables necesarias----------------------------------------------------------------------
		String idCuenta = "";
		//Se consulta la información de la reserva------------------------------------------------------------------------
		infoReserva = Cita.consultaCamposAdicionalesReserva(con, forma.getCodigoCita());
		
		if(Integer.parseInt(infoReserva.get("numRegistros").toString())>0)
		{
			//CASO 1) CUENTA ABIERTA**************************************************************************
			if(paciente.getCodigoCuenta()>0)
			{
				//Actualizamos el Campo Control-post-Operatorio. Colocar el campo por Control Post-Operatorio en True
				controlPostOperatorio = mundo.controlPostOperatorio(con, paciente.getCodigoIngreso());
				if(!controlPostOperatorio)
				{
					errores.add("No se actualizo Control Post-Operatorio",new ActionMessage("errors.noSeGraboInformacion","DEL INGRESO DEL PACIENTE"));
					return errores;
				}
				idCuenta = paciente.getCodigoCuenta()+"";
				Cuenta cuenta = new Cuenta();
				cuenta.cargar(con,idCuenta);
					
				boolean existeConvenio = false;
				//Se verifica si algun convenio de la cuenta tiene la misma informacion de la reserva
				for(int i=0;i<cuenta.getCuenta().getConvenios().length;i++)
				{
					if(cuenta.getCuenta().getConvenios()[i].getConvenio().getCodigo()==Integer.parseInt(infoReserva.get("codigoConvenio").toString())&&
						cuenta.getCuenta().getConvenios()[i].getClasificacionSocioEconomica()==Integer.parseInt(infoReserva.get("codigoEstratoSocial").toString())&&
						cuenta.getCuenta().getConvenios()[i].getTipoAfiliado().equals(infoReserva.get("codigoTipoAfiliado").toString())&&
						cuenta.getCuenta().getConvenios()[i].getContrato()==Integer.parseInt(infoReserva.get("codigoContrato").toString())&&
						cuenta.getCuenta().getConvenios()[i].getMontoCobro()==Integer.parseInt(infoReserva.get("codigoMonto").toString())
							)
					{
						existeConvenio = true;
						codigoSubCuenta = Utilidades.obtenerSubCuentaIngreso(con, paciente.getCodigoIngreso());
						logger.info("====>Codigo de la SubCuenta Cuenta Abierta cuando existe informcion de Convenio Igual: "+codigoSubCuenta);
					}
				}
				
				logger.info("====>Existe Convenio: "+existeConvenio);
				
				//Se verifica si el convenio principal de la cuenta cumple con la misma información de la reserva--------------------------------------------
				if(!existeConvenio)
				{
					if(pacienteCuentaAbierta)
					{
						logger.info("\n====>Error los datos del Convenio no corresponde con el del Ingreso");
						errores.add("Datos Cuenta Inconsistencia",new ActionMessage("error.cita.datosCuentaNoCorresponden"));
						return errores;
					}
					else
					{	
						logger.info("\n====>Los datos del Convenio son iguales al del Convenio");
						try
						{	
							HashMap datosSubCuenta = new HashMap();
							datosSubCuenta.put("convenio", Integer.parseInt(infoReserva.get("codigoConvenio").toString()));
							datosSubCuenta.put("naturalezaPaciente", ConstantesBD.codigoNaturalezaPacientesNinguno);
							datosSubCuenta.put("tipoAfiliado", infoReserva.get("codigoTipoAfiliado").toString());
							datosSubCuenta.put("clasificacion", Integer.parseInt(infoReserva.get("codigoEstratoSocial").toString()));
							datosSubCuenta.put("montoCobro", Integer.parseInt(infoReserva.get("codigoMonto").toString()));
							datosSubCuenta.put("contrato", Integer.parseInt(infoReserva.get("codigoContrato").toString()));
							datosSubCuenta.put("ingreso", paciente.getCodigoIngreso());
							datosSubCuenta.put("codPaciente", paciente.getCodigoPersona());
							datosSubCuenta.put("nroPrioridad", 1);
							datosSubCuenta.put("facturado", ConstantesBD.acronimoNo);
							codigoSubCuenta = mundo.insertarSubCuenta(con, usuario.getLoginUsuario(), datosSubCuenta);
							logger.info("====>Codigo de la SubCuenta Cuenta Abierta: "+codigoSubCuenta);
						}
						catch(Exception e)
						{
							e.printStackTrace();
						}
					}
				}
			
			}
			//CASO 2) CUENTA CERRADA ***************************************************************************
			else
			{
				//***************SE TOMA Y SE VALIDA EL CONSECUTIVO DE INGRESO**********************************************************
				String valorConsecutivoIngreso=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoIngresos, usuario.getCodigoInstitucionInt());
				String anioConsecutivoIngreso=UtilidadBD.obtenerAnioConsecutivo(ConstantesBD.nombreConsecutivoIngresos, usuario.getCodigoInstitucionInt(),valorConsecutivoIngreso);
				if(!UtilidadCadena.noEsVacio(valorConsecutivoIngreso) || valorConsecutivoIngreso.equals("-1"))
					errores.add("Falta consecutivo disponible",new ActionMessage("error.paciente.faltaDefinirConsecutivo","el ingreso"));
				else
				{
					try
					{
						Integer.parseInt(valorConsecutivoIngreso);
					}
					catch(Exception e)
					{
						logger.error("Error en validacionConsecutivoDisponibleIngreso:  "+e);
						errores.add("Consecutivo no es entero", new ActionMessage("errors.integer","el consecutivo del ingreso"));
					}
				}
				
				//Si hay lagun error con el consecutivo de ingreso se debe manejar el error
				if(!errores.isEmpty())
					return errores;
				//***********************************************************************************************************************
				
				IngresoGeneral ingreso = new IngresoGeneral(
					usuario.getCodigoInstitucion(), 
					paciente,ConstantesIntegridadDominio.acronimoEstadoAbierto,
					usuario.getLoginUsuario(),
					valorConsecutivoIngreso,
					anioConsecutivoIngreso,
					usuario.getCodigoCentroAtencion(),
					"", //sin codigo paciente entidad subcontratada
					"","",""
				);
				RespuestaValidacion resp1;
				int idIngreso = 0;
				
				//Validaciones iniciales del ingreso y de la cuenta---------------------------------------------------------------
				resp1 = UtilidadValidacion.validacionIngresarIngreso(con, ingreso.getCodigoTipoIdentificacionPaciente(), ingreso.getNumeroIdentificacionPaciente(), ingreso.getInstitucion() );
				//----------------------------------------------------------------------------------------------------
				
				if(resp1.puedoSeguir)
				{
					if(Integer.parseInt(infoReserva.get("numRegistros").toString())>0)
					{
						//Se crea nuevo ingreso-------------------------------------------------------------------------------------------
						idIngreso = ingreso.insertarIngresoTransaccional(con, ConstantesBD.continuarTransaccion);
						//Colocar el campo por Control Post-Operatorio en True
						controlPostOperatorio = mundo.controlPostOperatorio(con, idIngreso);
						if(!controlPostOperatorio)
						{
							errores.add("No se actualizo Control Post-Operatorio",new ActionMessage("errors.noSeGraboInformacion","DEL INGRESO DEL PACIENTE"));
							return errores;
						}
						UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoIngresos,usuario.getCodigoInstitucionInt(), valorConsecutivoIngreso, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
						//Se inserta la cuenta -----------------------------------------------------------------------------------------
						DtoCuentas dtoCuenta = new DtoCuentas();
						//Se llenan los datos de la cuenta
						dtoCuenta.setCodigoEstado(ConstantesBD.codigoEstadoCuentaActiva);
						dtoCuenta.setCodigoViaIngreso(ConstantesBD.codigoViaIngresoConsultaExterna);
						dtoCuenta.setDesplazado(paciente.getCodigoGrupoPoblacional().equals(ConstantesIntegridadDominio.acronimoDesplazados)?true:false);
						dtoCuenta.setCodigoOrigenAdmision(ConstantesBD.codigoOrigenAdmisionHospitalariaEsConsultaExterna);
						dtoCuenta.setCodigoArea(Utilidades.convertirAEntero(forma.getCodigoCentroCosto()));
						dtoCuenta.setCodigoTipoPaciente(ConstantesBD.tipoPacienteAmbulatorio);
						dtoCuenta.setCodigoPaciente(paciente.getCodigoPersona()+"");
						dtoCuenta.setLoginUsuario(usuario.getLoginUsuario());
						dtoCuenta.setIdIngreso(idIngreso+"");
						//Se llenan los datos del convenio principal
						DtoSubCuentas dtoSubCuenta = new DtoSubCuentas();
						dtoSubCuenta.setConvenio(new InfoDatosInt(Integer.parseInt(infoReserva.get("codigoConvenio").toString()),infoReserva.get("nombreConvenio").toString()));
						dtoSubCuenta.setNaturalezaPaciente(ConstantesBD.codigoNaturalezaPacientesNinguno);
						dtoSubCuenta.setTipoAfiliado(infoReserva.get("codigoTipoAfiliado").toString());
						dtoSubCuenta.setClasificacionSocioEconomica(Integer.parseInt(infoReserva.get("codigoEstratoSocial").toString()));
						dtoSubCuenta.setMontoCobro(Integer.parseInt(infoReserva.get("codigoMonto").toString()));
						dtoSubCuenta.setContrato(Integer.parseInt(infoReserva.get("codigoContrato").toString()));
						dtoSubCuenta.setIngreso(idIngreso);
						dtoSubCuenta.setLoginUsuario(usuario.getLoginUsuario());
						dtoSubCuenta.setCodigoPaciente(paciente.getCodigoPersona());
						dtoSubCuenta.setNroPrioridad(1);
						dtoSubCuenta.setFacturado(ConstantesBD.acronimoNo);
						
						Contrato mundoContrato = new Contrato();
						mundoContrato.cargar(con, dtoSubCuenta.getContrato()+"");
						
						//Se asigna el convenio a la cuenta
						dtoCuenta.setConvenios(new DtoSubCuentas[1]);
						dtoCuenta.getConvenios()[0] = dtoSubCuenta;
						
						Cuenta mundoCuenta = new Cuenta();
						mundoCuenta.setCuenta(dtoCuenta);
						ResultadoBoolean resp2 = mundoCuenta.guardar(con);
						
						if(!resp2.isTrue())
						{
							errores.add("No se creó cuenta",new ActionMessage("errors.noSeGraboInformacion","DE LA CUENTA DEL PACIENTE"));
							return errores;
						}
						
						idCuenta = resp2.getDescripcion();
						
						//Consultamos el codigo de la Subcuenta ingresado anteriormente
						//para poderlo enviar en la solicitud
						temSubCuentas = UtilidadesHistoriaClinica.obtenerResponsablesIngreso(con, idIngreso, true, temp, false, "" /*subCuentaCoberturaOPCIONAL*/,dtoCuenta.getCodigoViaIngreso());
						Utilidades.imprimirArrayList(temSubCuentas);
						codigoSubCuenta = Utilidades.convertirAEntero(((DtoSubCuentas)temSubCuentas.get(0)).getSubCuenta()+"");
						
						//Se carga de nuevo el paciente (para actualizar los datos de la cuenta)----------------------------------------------------
						try 
						{
							paciente.cargar(con, Utilidades.convertirAEntero(forma.getCodigoPaciente()));
							paciente.cargarPaciente2(con, Utilidades.convertirAEntero(forma.getCodigoPaciente()), usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
						} 
						catch (SQLException e) 
						{
							logger.error("Error cargando los datos del paciente en procesoCitaReservadaSinLiquidar:"+e);
						}
					}
					else
					{
						if(infoReserva.get("atributoError")==null)
							errores.add("Datos Cuenta Inconsistencia",new ActionMessage(infoReserva.get("llaveError").toString()));
						else
							errores.add("Datos Cuenta Inconsistencia",new ActionMessage(infoReserva.get("llaveError").toString(),infoReserva.get("atributoError").toString()));
						UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoIngresos,usuario.getCodigoInstitucionInt(), valorConsecutivoIngreso, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
						return errores;
					}
					
				}
				else
				{
					request.setAttribute("descripcionError", resp1.textoRespuesta);
					UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoIngresos,usuario.getCodigoInstitucionInt(), valorConsecutivoIngreso, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
					return errores;
				}
			}
		}
		else
		{
			if(infoReserva.get("atributoError")==null)
				errores.add("Datos Cuenta Inconsistencia",new ActionMessage(infoReserva.get("llaveError").toString()));
			else
				errores.add("Datos Cuenta Inconsistencia",new ActionMessage(infoReserva.get("llaveError").toString(),infoReserva.get("atributoError").toString()));
			
			return errores;
		}
		
		
		//**********SEGUN TIPO DE SERVICIO SE GENERA SOLICITUD CARGO**********************************************
		//Servicios Consultas
		if(forma.getCodigoTipoServicio().equals(ConstantesBD.codigoServicioInterconsulta+""))
			errores = generarSolicitudConsulta(con, forma, errores, usuario, paciente, idCuenta, codigoSubCuenta);
		//Servicios Procedimientos
		else if(forma.getCodigoTipoServicio().equals(ConstantesBD.codigoServicioProcedimiento+""))
			errores = generarSolicitudProcedimiento(con, forma, errores, usuario, paciente, idCuenta, codigoSubCuenta);
		//SErvicios No Cruents
		else if(forma.getCodigoTipoServicio().equals(ConstantesBD.codigoServicioNoCruentos+""))
			errores = generarSolicitudCirugia(con, forma, errores, usuario, paciente, idCuenta, codigoSubCuenta);
		//-------------------------------------------------------------------------
		
		if(!errores.isEmpty())
		{
			return errores;
		}
		
		return errores;
	}
	
	/**
	 * Generar la solicitud de consulta
	 * @param con
	 * @param citasForm
	 * @param errores
	 * @param idCuenta 
	 * @param paciente 
	 * @param usuario 
	 * @param codigoSubCuenta 
	 * @return
	 */
	private ActionErrors generarSolicitudConsulta(Connection con, AsignarCitasControlPostOperatorioForm forma, ActionErrors errores, UsuarioBasico usuario, PersonaBasica paciente, String idCuenta, int codigoSubCuenta) 
	{
		//Se inserta solicitud de cita (generar cargo) -----------------------------------------------------------------------------------
		Cita cita = new Cita();
		cita.setCodigo(Integer.parseInt(forma.getCodigoCita()));
		
		boolean lb_generada=false;
		
		Cargos cargos=new Cargos();
		try
		{
			cita.setCodigoAreaPaciente(paciente.getCodigoArea());
			cita.setCodigoCentroCostoSolicitado(Utilidades.convertirAEntero(forma.getCodigoCentroCosto()));
			cita.setCodigoEspecialidadSolicitante(Utilidades.convertirAEntero(forma.getCodigoEspecialidad()));
			cita.setSolPYP(false);
			
			logger.info("====>Id Cuenta: "+idCuenta);
			logger.info("====>Servicio Cita: "+forma.getCodigoServicioCita());
			logger.info("====>Centro Costo: "+forma.getCodigoCentroCosto());
			logger.info("====>Codigo SubCuenta: "+codigoSubCuenta);
			
			//usuario.getCodigoOcupacionMedica()
			lb_generada =cita.generarSolicitud(
				con,
				ConstantesBD.continuarTransaccion,
				usuario.getCodigoOcupacionMedica(),
				Integer.parseInt(idCuenta),
				Utilidades.convertirAEntero(forma.getCodigoServicioCita()),
				/*"",*/ 
				paciente.getCodigoArea(), 
				Utilidades.convertirAEntero(forma.getCodigoCentroCosto()),
				usuario.getLoginUsuario(),
				true);
			
			cita.actualizarEstadoCitaTransaccional(con, ConstantesBD.codigoEstadoCitaAsignada, cita.getCodigo(), ConstantesBD.continuarTransaccion, usuario.getLoginUsuario());
			
			forma.setNumeroSolicitudCita(cita.getNumeroSolicitud()+"");
			forma.setBusquedaCitasReservadas("numerosolicitud_"+forma.getPosicion(), cita.getNumeroSolicitud());
			
		}//try
		catch(Exception e)
		{
			logger.error("Error generando el cargo para la cita: "+e);
		}
		
		if(lb_generada )
		{
			try
			{
				logger.error("**********************SE PUSO UN TRY CACTH EN LA GENERACION DEL CARGO********************");
				//cargo.generarCargoCita(con, cita.getNumeroSolicitud(), usuario.getLoginUsuario());
				cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	con, 
																					usuario, 
																					paciente, 
																					false/*dejarPendiente*/, 
																					cita.getNumeroSolicitud(), 
																					ConstantesBD.codigoTipoSolicitudCita /*codigoTipoSolicitudOPCIONAL*/, 
																					Integer.parseInt(idCuenta), 
																					ConstantesBD.codigoCentroCostoConsultaExterna/*codigoCentroCostoEjecutaOPCIONAL*/, 
																					Utilidades.convertirAEntero(forma.getCodigoServicioCita())/*codigoServicioOPCIONAL*/, 
																					1/*cantidadServicioOPCIONAL*/, 
																					ConstantesBD.codigoNuncaValidoDouble/*valorTarifaOPCIONAL*/, 
																					ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
																					/*"", -- numeroAutorizacionOPCIONNAL*/
																					"" /*esPortatil*/,
																					true /*excento*/,"",
																					codigoSubCuenta+"" /*subCuentaCoberturaOPCIONAL*/
																				);
			}
			catch(Exception e)
			{
				logger.error("**************************************************************************************************");
				logger.error("***********ERROR AL GENERAR EL CARGO EN ATENCION CITAS: "+e+"*********************************************");
				logger.error("**************************************************************************************************");
				errores.add("No se grabó información de la solicitud y cargo cita",new ActionMessage("errors.ingresoDatos","la generación del cargo del servicio. Proceso Cancelado. Por favor intentar de nuevo, si el problema persiste reportarlo al adminsitrador del sistema"));
			}
		}
		else
			errores.add("No se grabó información de la solicitud y cargo cita",new ActionMessage("errors.ingresoDatos","solicitud y cargo de la cita. Proceso Cancelado"));
		
		return errores;
	}
	
	/**
	 * Método implementado para generar la solicitud de procedimiento
	 * @param con
	 * @param citasForm
	 * @param errores
	 * @param usuario
	 * @param paciente
	 * @param idCuenta
	 * @param codigoSubCuenta 
	 * @return
	 */
	private ActionErrors generarSolicitudProcedimiento(Connection con, AsignarCitasControlPostOperatorioForm forma, ActionErrors errores, UsuarioBasico usuario, PersonaBasica paciente, String idCuenta, int codigoSubCuenta) 
	{
		Cita cita = new Cita();
		cita.setCodigo(Integer.parseInt(forma.getCodigoCita()));
		SolicitudProcedimiento	solicitud= new SolicitudProcedimiento();
		
		//String numeroAutorizacion = "";
		int codigoServicio = Utilidades.convertirAEntero(forma.getCodigoServicioCita());
		int codigoCentroCosto = Utilidades.convertirAEntero(forma.getCodigoCentroCosto());
		
		try
		{
			//********************************GENERACION DE LA SOLICITUD DE PROCEDIMIENTOS*********************************************
			solicitud.setFechaSolicitud(UtilidadFecha.getFechaActual(con));
			solicitud.setHoraSolicitud(UtilidadFecha.getHoraActual(con));
			solicitud.setSolPYP(false);
			solicitud.setTipoSolicitud(new InfoDatosInt (ConstantesBD.codigoTipoSolicitudProcedimiento, ""));
			solicitud.setCobrable(true);
			//solicitud.setNumeroAutorizacion(numeroAutorizacion);
			solicitud.setEspecialidadSolicitante(new InfoDatosInt(ConstantesBD.codigoEspecialidadMedicaNinguna, ""));
			solicitud.setOcupacionSolicitado(new InfoDatosInt(usuario.getCodigoOcupacionMedica(), ""));
			solicitud.setCentroCostoSolicitante(new InfoDatosInt(paciente.getCodigoArea(), ""));
			solicitud.setCentroCostoSolicitado(new InfoDatosInt(codigoCentroCosto, ""));
			solicitud.setCodigoMedicoSolicitante(ConstantesBD.codigoNuncaValido);
			solicitud.setCodigoCuenta(Integer.parseInt(idCuenta));
			solicitud.setDatosMedico("");
			solicitud.setVaAEpicrisis(false);
			solicitud.setUrgente(false);
			solicitud.setComentario("");
			solicitud.setNombreOtros("");
			solicitud.setTieneCita(true);
			
			int numeroDocumento=solicitud.numeroDocumentoSiguiente(con);

			/*Obtener el codigo de la finalidad*/
			ArrayList<HashMap<String, Object>> finalidades = Utilidades.obtenerFinalidadesServicio(con, codigoServicio, usuario.getCodigoInstitucionInt());
			//Si solo hay una finalidad se asigna automaticamente, de lo contrario se deja vacío
			if(finalidades.size()==1)
				solicitud.setFinalidad(Integer.parseInt(((HashMap)finalidades.get(0)).get("codigo").toString()));
			else
				solicitud.setFinalidad(0);
					
			/* Obtener el código del servicio de la solicitud */
			solicitud.setCodigoServicioSolicitado(codigoServicio);
			//por defecto en la creacion de la solicitud la finalizacion de la respuesta multiple es false;
			solicitud.setFinalizadaRespuesta(false);
			//el indicativo de si la solicitud es de respuesta multiple depende de la parametrizacion del servicio
			solicitud.setRespuestaMultiple(false);
			/* Obtener dato soicitud múltiple*/
			solicitud.setMultiple(false);

			InfoDatosInt centroCosto=new InfoDatosInt();
			centroCosto.setCodigo(codigoCentroCosto);
			solicitud.setCentroCostoSolicitado(centroCosto);
			solicitud.setFrecuencia(ConstantesBD.codigoNuncaValido);

			/* Genera ls solicitud */
			int numeroSolicitud = solicitud.insertarTransaccional(con, ConstantesBD.continuarTransaccion, numeroDocumento, Integer.parseInt(idCuenta), false,ConstantesBD.codigoNuncaValido+"");
			forma.setNumeroSolicitudCita(numeroSolicitud+"");
			forma.setBusquedaCitasReservadas("numerosolicitud_"+forma.getPosicion(), numeroSolicitud);
			//citasForm.setMapaCitas("numeroSolicitud_"+citasForm.getPosCita()+"_"+citasForm.getPosServicio(), numeroSolicitud);
			
			cita.actualizarEstadoCitaTransaccional(con, ConstantesBD.codigoEstadoCitaAsignada, cita.getCodigo(), ConstantesBD.continuarTransaccion, usuario.getLoginUsuario());
						
			 //GENERACION DEL CARGO Y SUBCUENTA - EVALUACION COBERTURA 
		    Cargos cargos= new Cargos();
		    cargos.setPyp(false);
		    boolean inserto= cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	con, 
				usuario, 
				paciente, 
				false/*dejarPendiente*/, 
				solicitud.getNumeroSolicitud(), 
				ConstantesBD.codigoTipoSolicitudProcedimiento /*codigoTipoSolicitudOPCIONAL*/, 
				Integer.parseInt(idCuenta)/*codigoCuentaOPCIONAL*/, 
				solicitud.getCentroCostoSolicitado().getCodigo()/*codigoCentroCostoEjecutaOPCIONAL*/, 
				solicitud.getCodigoServicioSolicitado()/*codigoServicioOPCIONAL*/, 
				1 /*cantidadServicioOPCIONAL*/, 
				ConstantesBD.codigoNuncaValidoDouble/*valorTarifaOPCIONAL*/, 
				ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
				/*numeroAutorizacion,*/
				"" /*esPortatil*/,
				false /*excento*/,"",
				codigoSubCuenta+"" /*subCuentaCoberturaOPCIONAL*/);
						
						
			if(!inserto)
			{
				errores.add("",new ActionMessage("errors.noSeGraboInformacion",
						"AL GENERAR LA SOLICITUD Y EL CARGO PARA EL SERVICIO "+forma.getCodigoServicioCita()));
			}
			//***************************************************************************************************************************
			else
			{
				//**********************ASIGNACION DE LA SOLICITUD A LA CITA***************************************************************
				
				HashMap servicios = Cita.consultarServiciosCita(con, cita.getCodigo()+"", usuario.getCodigoInstitucionInt());
				logger.info("NUMERO DER EGISTROS ENCOTNRADO============> "+servicios.get("numRegistros")+" "+servicios);
				
				if(cita.asignarSolicitud(con, numeroSolicitud, codigoServicio, ConstantesBD.continuarTransaccion,usuario.getLoginUsuario())<=0)
					errores.add("",new ActionMessage("errors.noSeGraboInformacion",
							"AL ACTUALIZAR LA SOLICITUD DEL SERVICIO "+forma.getCodigoServicioCita()+" "));
				//**************************************************************************************************************************
				
			}			
			
		}
		catch(Exception e)
		{
			logger.error("Error al generar la solicitud de procedimientos de la cita: "+e);
			errores.add("",new ActionMessage("errors.noSeGraboInformacion",
					"AL GENERAR LA SOLICITUD PARA EL SERVICIO "+forma.getCodigoServicioCita()+" "));
		}

		return errores;
	}
	
	/**
	 * Método que realiza la generacion de la solicitud de cirugía
	 * @param con
	 * @param citasForm
	 * @param errores
	 * @param usuario
	 * @param paciente
	 * @param idCuenta
	 * @param codigoSubCuenta 
	 * @return
	 */
	private ActionErrors generarSolicitudCirugia(Connection con, AsignarCitasControlPostOperatorioForm forma, ActionErrors errores, UsuarioBasico usuario, PersonaBasica paciente, String idCuenta, int codigoSubCuenta) throws IPSException 
	{
		Cita cita = new Cita();
		cita.setCodigo(Integer.parseInt(forma.getCodigoCita()));
		SolicitudesCx mundoSolCx= new SolicitudesCx();
			
		//String numeroAutorizacion = "";
		int codigoServicio = Utilidades.convertirAEntero(forma.getCodigoServicioCita());
		int codigoCentroCosto = Utilidades.convertirAEntero(forma.getCodigoCentroCosto());
		String observaciones = "";
		int codigoPeticion = 0, numeroSolicitud=0, resp1 = 0;
		boolean resp0 = false;
		
		//*************SE INSERTA PRIMERO LA PETICION QX ********************************************
		//Datos del encabezado de la peticion
        HashMap peticionEncabezadoMap= new HashMap();
        peticionEncabezadoMap.put("tipoPaciente",   UtilidadesManejoPaciente.obtenerTipoPacienteCuenta(con, idCuenta).getAcronimo());
        peticionEncabezadoMap.put("fechaPeticion", UtilidadFecha.getFechaActual(con));
        peticionEncabezadoMap.put("horaPeticion", UtilidadFecha.getHoraActual(con));
        peticionEncabezadoMap.put("duracion", "");
        peticionEncabezadoMap.put("solicitante", usuario.getCodigoPersona()+"");
        peticionEncabezadoMap.put("fechaEstimada", "");
        peticionEncabezadoMap.put("requiereUci", ConstantesBD.acronimoNo);
        peticionEncabezadoMap.put("programable", ConstantesBD.acronimoNo);
        
        //Datos del servicio
        HashMap serviciosPeticionMap=new HashMap();
        serviciosPeticionMap.put("numeroFilasMapaServicios", "1");
        serviciosPeticionMap.put("fueEliminadoServicio_0", "false");
        serviciosPeticionMap.put("codigoServicio_0", codigoServicio);
        serviciosPeticionMap.put("codigoEspecialidad_0", ""); //va vaío
        serviciosPeticionMap.put("codigoTipoCirugia_0", "-1"); //no se ingresa informacion
        serviciosPeticionMap.put("numeroServicio_0", "1");
        serviciosPeticionMap.put("observaciones_0", observaciones);
        
        //Datos de los artículos
        HashMap articulosPeticionMap= new HashMap();
        articulosPeticionMap.put("numeroMateriales", "0");
        articulosPeticionMap.put("numeroOtrosMateriales", "0");
        
        //Datos de los porfesionales
        HashMap profesionalesPeticionMap= new HashMap();
        profesionalesPeticionMap.put("numeroProfesionales", "0");
        
        Peticion mundoPeticion= new Peticion();
        int codigoPeticionYNumeroInserciones[]= mundoPeticion.insertar(
        	con, 
        	peticionEncabezadoMap, 
        	serviciosPeticionMap, 
        	profesionalesPeticionMap, 
        	articulosPeticionMap, 
        	paciente.getCodigoPersona(),
        	ConstantesBD.codigoNuncaValido,
        	usuario, true, false);
        
        //logger.info("resultado insercion peticion=> N° inserciones: "+codigoPeticionYNumeroInserciones[0]+", peticion: "+codigoPeticionYNumeroInserciones[1]);
        
        //Se verifica número de inserciones
        if(codigoPeticionYNumeroInserciones[0]<1)
        	errores.add("",new ActionMessage("errors.noSeGraboInformacion",
        		"DE LA PETICION Qx PARA EL SERVICIO "+forma.getCodigoServicioCita()));
        else
        	codigoPeticion = codigoPeticionYNumeroInserciones[1];
		//*******************************************************************************************
	       
	        //*******************SE INSERTA UNA SOLICITUD BÁSICA*******************************************
	        Solicitud objectSolicitud= new Solicitud();
	        
	        objectSolicitud.clean();
	        objectSolicitud.setFechaSolicitud(UtilidadFecha.getFechaActual(con));
	        objectSolicitud.setHoraSolicitud(UtilidadFecha.getHoraActual(con));
	        objectSolicitud.setTipoSolicitud(new InfoDatosInt(ConstantesBD.codigoTipoSolicitudCirugia));
	        //objectSolicitud.setNumeroAutorizacion(numeroAutorizacion);
	        objectSolicitud.setEspecialidadSolicitante(new InfoDatosInt(ConstantesBD.codigoEspecialidadMedicaNinguna));
	        objectSolicitud.setOcupacionSolicitado(new InfoDatosInt(ConstantesBD.codigoNuncaValido));
	        objectSolicitud.setCentroCostoSolicitante(new InfoDatosInt(paciente.getCodigoArea()));
	       // objectSolicitud.setCodigoMedicoSolicitante(usuario.getCodigoPersona());
	        objectSolicitud.setCodigoMedicoSolicitante(ConstantesBD.codigoNuncaValido);
	        
	        objectSolicitud.setCentroCostoSolicitado(new InfoDatosInt(codigoCentroCosto));
	        objectSolicitud.setCodigoCuenta(Integer.parseInt(idCuenta));
	        
	        objectSolicitud.setCobrable(true);
	        
	        objectSolicitud.setVaAEpicrisis(false);
	        objectSolicitud.setUrgente(false);
	        objectSolicitud.setEstadoHistoriaClinica(new InfoDatosInt(ConstantesBD.codigoEstadoHCSolicitada));
	        objectSolicitud.setSolPYP(false);
	        objectSolicitud.setTieneCita(true);
	        //Campo se actualiza despues de generado el cargo
	        objectSolicitud.setLiquidarAsocio(ConstantesBD.acronimoSi);
	        
	        try
	        {
	            numeroSolicitud=objectSolicitud.insertarSolicitudGeneralTransaccional(con, ConstantesBD.continuarTransaccion);
	        }
	        catch(SQLException sqle)
	        {
	            logger.warn("Error al generar la solicitud basica de cirugías: "+sqle);
	            errores.add("",new ActionMessage("errors.noSeGraboInformacion",
	            		"DE LA SOLICITUD PARA EL SERVICIO "+forma.getCodigoServicioCita()));
	            
	        }
	        cita.actualizarEstadoCitaTransaccional(con, ConstantesBD.codigoEstadoCitaAsignada, cita.getCodigo(), ConstantesBD.continuarTransaccion, usuario.getLoginUsuario());
	        //***********************************************************************************************
			
	        forma.setNumeroSolicitudCita(numeroSolicitud+"");
	        forma.setBusquedaCitasReservadas("numerosolicitud_"+forma.getPosicion(), numeroSolicitud);
	        
			//*******************SE CALCULA LA SUBCUENTA DE LA COBERTURA**************************************
	        double subCuenta=ConstantesBD.codigoNuncaValido;
			InfoResponsableCobertura infoResponsableCobertura= new InfoResponsableCobertura();;
			infoResponsableCobertura=Cobertura.validacionCoberturaServicio(con, paciente.getCodigoIngreso()+"", paciente.getCodigoUltimaViaIngreso(), paciente.getCodigoTipoPaciente(), codigoServicio, usuario.getCodigoInstitucionInt(),false, "" /*subCuentaCoberturaOPCIONAL*/);
			subCuenta=infoResponsableCobertura.getDtoSubCuenta().getSubCuentaDouble();
	        //************************************************************************************************
			
			//*************SE INSERTA LA SOLICITUD DE CIRUGIA ************************************************
			//1) Se inserta encabezado de la cirugia
			try 
			{
				resp0 = mundoSolCx.insertarSolicitudCxGeneralTransaccional1(
																			con, 
																			numeroSolicitud+"", 
																			codigoPeticion+"", 
																			false, 
																			ConstantesBD.continuarTransaccion, 
																			subCuenta,
																			ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruento);				
			
			} catch (SQLException e) 
			{
				resp0 = false;
				logger.error("Error al insertar solicitud general de cirugía: "+e);
			}
			if(!resp0)
				 errores.add("",new ActionMessage("errors.noSeGraboInformacion",
	        		"DE LA SOLICITUD Qx PARA EL SERVICIO "+forma.getCodigoServicioCita()));
			else
			{
				//2) Se inserta el detalle de la cirugia (El servicio)
				 
					resp1 = mundoSolCx.insertarSolicitudCxXServicioTransaccional(
							con, 
							numeroSolicitud+"", 
							codigoServicio+"", 
							ConstantesBD.codigoNuncaValido, //codigo tipo cirugia
							1,
							ConstantesBD.codigoNuncaValido, // esquema tarifario
							ConstantesBD.codigoNuncaValidoDouble, //grupo o uvr 
							usuario.getCodigoInstitucionInt(), 
							/*numeroAutorizacion,*/ 
							ConstantesBD.codigoNuncaValido, //finalidad 
							observaciones, 
							"", //via Cx 
							"",  // indicativo bilateral
							"",  //indicativo via de acceso
							ConstantesBD.codigoNuncaValido, // codigo especialidad
							"", //liquidar servicio 
							ConstantesBD.continuarTransaccion,
							"",
							null);
				
				
				if(resp1<=0)
					errores.add("",new ActionMessage("errors.noSeGraboInformacion",
			        		"DEL DETALLE DE LA SOLICITUD Qx PARA EL SERVICIO "+forma.getCodigoServicioCita()
			        		));
			}
			
			//*******************************************************************************************************
			if(resp0 && resp1 > 0)
			{
				//**************SE ACTUALIZA EL NUMERO DE SOLICITUD EN LA CITA********************************************
				if(cita.asignarSolicitud(con, numeroSolicitud, codigoServicio, ConstantesBD.continuarTransaccion,usuario.getLoginUsuario())<=0)
				{
					errores.add("",new ActionMessage("errors.noSeGraboInformacion",
							"AL ACTUALIZAR LA SOLICITUD DEL SERVICIO "+forma.getCodigoServicioCita()));
				}
				else
				{
					//Cambiar el indicador de liquidacion de asocios a No por haber generado el cargo sin asocios
					if(!objectSolicitud.cambiarLiquidacionAsociosSolicitud(con, numeroSolicitud, ConstantesBD.acronimoNo))
						errores.add("",new ActionMessage("errors.noSeGraboInformacion",
								"AL ACTUALIZAR EL CAMPO LIQUIDAR ASOCIOS DE LA SOLICITUD "+numeroSolicitud));						
				}
				//**************************************************************************************************************************
			}
			//*******************************************************************************************************
			
			return errores;
	}
	
}