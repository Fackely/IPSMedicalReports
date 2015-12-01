/*
 * @(#)CierreCuentaAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */
package com.princetonsa.action;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

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
import util.ElementoApResource;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.interfaces.ConstantesBDInterfaz;
import util.interfaces.UtilidadBDInterfaz;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.CierreCuentaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.interfaz.DtoInterfazPaciente;
import com.princetonsa.dto.manejoPaciente.DtoReingresoSalidaHospiDia;
import com.princetonsa.mundo.Cama;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.ObservableBD;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.TrasladoCamas;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Egreso;
import com.princetonsa.mundo.facturacion.EntidadesSubContratadas;
import com.princetonsa.mundo.facturacion.ValidacionesCierreCuenta;
import com.princetonsa.mundo.manejoPaciente.PacientesEntidadesSubCon;
import com.princetonsa.mundo.manejoPaciente.ReingresoHospitalDia;
import com.princetonsa.mundo.solicitudes.Solicitud;

/**
 * Action, controla todas el workflow para el cierre de cuentas 
 * @version 1.0, Mayo 3, 2004
 * @author <a href="mailto:sgomez@PrincetonSA.com">Sebastian Gomez</a>,
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Rios</a>
 */
public class CierreCuentaAction extends Action
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(CierreCuentaAction.class);
		
	/**
	 * Mï¿½todo encargado de el flujo y control de la funcionalidad
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
			if(form instanceof CierreCuentaForm)
			{

				//SE ABRE CONEXION
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexiï¿½n"+e.toString());
				}

				//OBJETOS A USAR
				CierreCuentaForm cierreCuentaForm =(CierreCuentaForm)form;
				HttpSession session=request.getSession();		
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				String estado=cierreCuentaForm.getEstado(); 
				logger.warn("[CierreCuentaAction] -->"+estado);

				//**************VALIDACIONES*******************************************
				//verificar si es null (Paciente estï¿½ cargado)
				if((paciente==null || paciente.getTipoIdentificacionPersona().equals(""))&&!estado.equals("consultarTodos")&&!estado.equals("busquedaTodos")&&!estado.equals("ordenarTodos")){
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente no cargado", "errors.paciente.noCargado", true);
				}
				//verificar si hay ingreso cargado
				if(paciente.getCodigoIngreso()==0&&estado.equals("empezar")){
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No hay ingreso cargado en sesion", "errors.paciente.noIngresoSesion", true);
				}

				/**
				 * Validar concurrencia
				 * Si ya está en proceso de facturación, no debe dejar entrar
				 **/
				if(UtilidadValidacion.estaEnProcesofacturacion(con, paciente.getCodigoPersona(), "") )
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoFact", "error.facturacion.cuentaEnProcesoFact", true);
				}
				/**
				 * Validar concurrencia
				 * Si ya está en proceso de distribucion, no debe dejar entrar
				 **/
				else if(UtilidadValidacion.estaEnProcesoDistribucion(con, paciente.getCodigoPersona(), usuario.getLoginUsuario()) )
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error.facturacion.cuentaEnProcesoDistribucion", "error.facturacion.cuentaEnProcesoDistribucion", true);
				}
				//*********************************************************************

				if(estado == null)
				{
					cierreCuentaForm.reset();	
					logger.warn("Estado no valido dentro del flujo de registro del  Cierre de la Cuenta (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("empezar")||estado.equals("empezarAsocio"))
				{
					return this.accionEmpezar(cierreCuentaForm,mapping,paciente, con,request);
				}
				else if (estado.equals("cerrar"))
				{
					return this.accionCerrar(cierreCuentaForm,mapping,paciente,usuario,con,request);
				}
				//estados para la consulta por paciente
				else if(estado.equals("consultarPaciente"))
				{
					return this.accionConsultarPaciente(cierreCuentaForm,mapping,paciente,con);
				}
				else if(estado.equals("ordenarPaciente"))
				{
					return this.accionOrdenarPaciente(cierreCuentaForm,mapping,con);
				}
				//estados para la cosnulta por Todos
				else if(estado.equals("consultarTodos"))
				{
					return this.accionConsultarTodos(cierreCuentaForm,mapping,con,usuario);
				}
				else if(estado.equals("busquedaTodos"))
				{
					return this.accionBusquedaTodos(cierreCuentaForm,mapping,con);
				}
				else if(estado.equals("ordenarTodos"))
				{
					return this.accionOrdenarTodos(cierreCuentaForm,mapping,con);
				}
				else
				{
					cierreCuentaForm.reset();
					logger.warn("Estado no valido dentro del flujo de registro del Cierre de la Cuenta(null) ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
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
	 * @param cierreCuentaForm
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward accionOrdenarTodos(CierreCuentaForm cierreCuentaForm, ActionMapping mapping, Connection con) throws SQLException 
	{
		String[] indices={
				"id_",
				"nombrePaciente_",
				"numeroIdentificacion_",
				"tipoIdentificacion_",
				"codigoViaIngreso_",
				"viaIngreso_",
				"codigoConvenio_",
				"convenio_",
				"fechaApertura_",
				"fechaCierre_",
				"horaCierre_",
				"usuarioCierre_",
				"motivo_",
				"centro_atencion_",
				"descripcionentidadsub_"
		};
		
		int numeroElementos=Integer.parseInt(cierreCuentaForm.getListadoCuentas().get("numero_elementos")+"");
		
		cierreCuentaForm.setListadoCuentas(Listado.ordenarMapa(indices,
				cierreCuentaForm.getIndice(),
				cierreCuentaForm.getUltimoIndice(),
				cierreCuentaForm.getListadoCuentas(),
				numeroElementos));
		
		cierreCuentaForm.getListadoCuentas().put("numero_elementos",numeroElementos+"");
		cierreCuentaForm.setUltimoIndice(cierreCuentaForm.getIndice());
		cierreCuentaForm.setEstado("busquedaTodos");
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("consultaRangos");
	}

	/**
	 * @param cierreCuentaForm
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward accionBusquedaTodos(CierreCuentaForm cierreCuentaForm, ActionMapping mapping, Connection con) {
		
		Cuenta cuenta= new Cuenta();
		cierreCuentaForm.setListadoCuentas(cuenta.consultarCuentasCerradas(con,
				cierreCuentaForm.getFechaCierreInicial(),
				cierreCuentaForm.getFechaCierreFinal(),
				cierreCuentaForm.getCuentaInicial(),
				cierreCuentaForm.getCuentaFinal(),
				cierreCuentaForm.getCentroAtencion(),
				cierreCuentaForm.getCodigoEntidadSub()				
				));
		try
		{
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e)
		{
			logger.error("Error cerrando la conexiï¿½n "+e);
			return null;
		}
		return mapping.findForward("consultaRangos");
	}

	/**
	 * @param cierreCuentaForm
	 * @param mapping
	 * @param paciente
	 * @param con
	 * @return
	 */
	private ActionForward accionConsultarTodos(CierreCuentaForm cierreCuentaForm, ActionMapping mapping,Connection con,UsuarioBasico usuario) throws SQLException 
	{
		cierreCuentaForm.reset();
		//almacena la informacion de las empresas subcontratadas
		cierreCuentaForm.setEntidadesSubList(UtilidadesManejoPaciente.obtenerEntidadesSubcontratadas(con, usuario.getCodigoInstitucionInt()));
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("consultaRangos");
		
	}

	/**
	 * @param cierreCuentaForm
	 * @param mapping
	 * @param con
	 * @return
	 */
	private ActionForward accionOrdenarPaciente(CierreCuentaForm cierreCuentaForm, ActionMapping mapping, Connection con) throws SQLException
	{
		String[] indices={	"id_",
							"codigoViaIngreso_",
							"viaIngreso_",
							"codigoConvenio_",
							"convenio_",
							"fechaApertura_",
							"fechaCierre_",
							"horaCierre_",
							"usuarioCierre_",
							"motivo_",
							"centro_atencion_",
							"descripcionentidadsub_"};
		
		int numeroElementos=Integer.parseInt(cierreCuentaForm.getListadoCuentas().get("numero_elementos")+"");
		
		cierreCuentaForm.setListadoCuentas(Listado.ordenarMapa(indices,
				cierreCuentaForm.getIndice(),
				cierreCuentaForm.getUltimoIndice(),
				cierreCuentaForm.getListadoCuentas(),
				numeroElementos));
		
		cierreCuentaForm.getListadoCuentas().put("numero_elementos",numeroElementos+"");
		cierreCuentaForm.setUltimoIndice(cierreCuentaForm.getIndice());
		
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("consultaPaciente");
	}

	/**
	 * @param cierreCuentaForm
	 * @param mapping
	 * @param paciente
	 * @param con
	 * @return
	 */
	private ActionForward accionConsultarPaciente(CierreCuentaForm cierreCuentaForm, ActionMapping mapping, PersonaBasica paciente, Connection con) throws SQLException
	{
		Cuenta cuenta= new Cuenta();
		
		//se consulta listado de cuentas cerradas por paciente
		cierreCuentaForm.setListadoCuentas(cuenta.consultarCuentasCerradasPaciente(con,paciente.getCodigoPersona()));
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("consultaPaciente");
	}

	/**
	 * @param cierreCuentaForm
	 * @param mapping
	 * @param paciente
	 * @param usuario
	 * @param con
	 * @param request
	 * @return
	 */
	private ActionForward accionCerrar(CierreCuentaForm cierreCuentaForm, ActionMapping mapping, PersonaBasica paciente, UsuarioBasico usuario, Connection con, HttpServletRequest request) throws SQLException 
	{
		
		Cuenta cuenta=new Cuenta();
		
		//Se verifica asocio
		if(paciente.getExisteAsocio()&&paciente.getCodigoCuentaAsocio()>0)
		{
			
			int codigoCuentaAsocio=paciente.getCodigoCuentaAsocio();
			int codigoCuenta=paciente.getCodigoCuenta();
			logger.info("ID de la cuenta que se estï¿½ cerrando=> "+cierreCuentaForm.getIdCuenta());
			//se verifica cual es la cuenta que se cierre del asocio
			//1) se verifica si es la Hospitalizacion
			if(codigoCuenta==cierreCuentaForm.getIdCuenta())
			{
				logger.info("¡VOY A CERRAR A CUENTA DE HOSPITALIZACION! codigoCuenta=> "+codigoCuenta);
				UtilidadBD.iniciarTransaccion(con);
				int prueba=cuenta.desAsociarCuenta(con, usuario.getLoginUsuario(), paciente.getCodigoIngreso()+"","");
				int resp0=cuenta.cambiarEstadoCuentaTransaccional2(con,ConstantesBD.codigoEstadoCuentaCerrada,codigoCuenta,ConstantesBD.continuarTransaccion);
				int resp1=cuenta.registrarCierreCuenta(con,codigoCuenta,usuario.getLoginUsuario(),cierreCuentaForm.getMotivoCierre(),ConstantesBD.continuarTransaccion);
				int resp2=cuenta.cambiarEstadoCuentaTransaccional2(con,ConstantesBD.codigoEstadoCuentaActiva,codigoCuentaAsocio,ConstantesBD.continuarTransaccion);
				//se carga la ultima cama
				int codigoCama = Utilidades.getUltimaCamaTraslado(con,paciente.getCodigoCuenta());
				//se cambia el estado de la cama y se finaliza el traslado de cama
				Cama cama = new Cama();
				logger.info("codigo de la cama=> "+codigoCama);
				
				int	resp3=1;
				if(codigoCama>0)
				{
					resp3 = cama.cambiarEstadoCama(con,codigoCama+"",ConstantesBD.codigoEstadoCamaDisponible);
					
					if(resp3>0)
					{
						TrasladoCamas traslado = new TrasladoCamas();
						resp3 = traslado.actualizarFechaHoraFinalizacionNoTransaccional(con,codigoCuenta,UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),"")?1:0;
						
					}
				}
				int resp=this.cambiarEstadoValoracionInicial(con,codigoCuenta,ConstantesBD.continuarTransaccion);
				//Manejo de la interfaz de tesoreria
				int resp4 = this.manejoInterfazTesoreria(paciente.getCodigoPersona(), usuario.getCodigoInstitucionInt()); 
				
				logger.info("Hubo desasocio=> "+prueba+" otro reusltado=>"+resp+", resp0=>"+resp0+", resp1=>"+resp1+", resp2=>"+resp2+", resp3=>"+resp3+", resp4=>"+resp4+", prueba=>"+prueba);
				if(resp>0&&resp0>0&&resp1>0&&resp2>0&&resp3>0&&resp4>0&&prueba>0)
				{
					UtilidadBD.finalizarTransaccion(con);
					//se cambia el estado de la solicitud valoraciï¿½n 
					//atendido
					this.setObservable(paciente);
				}
				else
				{
					ActionErrors errores = new ActionErrors();
					errores.add("No se grabó informacion",new ActionMessage("errors.noSeGraboInformacion","DEL CIERRE DE CUENTA"));
					saveErrors(request,errores);
					cierreCuentaForm.setEstado("empezar");
					UtilidadBD.abortarTransaccion(con);
				}
						
				
			}
			//2) es la cuenta asociada de Urgencias
			else
			{
				logger.info("¡VOY A CERRAR A CUENTA DE URGENCIAS! codigoCuentaAsocio=> "+codigoCuentaAsocio);
				UtilidadBD.iniciarTransaccion(con);
				int prueba=cuenta.desAsociarCuenta(con, usuario.getLoginUsuario(), paciente.getCodigoIngreso()+"","");
				int resp0=cuenta.cambiarEstadoCuentaTransaccional2(con,ConstantesBD.codigoEstadoCuentaCerrada,codigoCuentaAsocio,ConstantesBD.continuarTransaccion);
				int resp1=cuenta.registrarCierreCuenta(con,codigoCuentaAsocio,usuario.getLoginUsuario(),cierreCuentaForm.getMotivoCierre(),ConstantesBD.continuarTransaccion);
				int resp=this.cambiarEstadoValoracionInicial(con,codigoCuentaAsocio,ConstantesBD.continuarTransaccion);
				//Manejo de la interfaz de tesoreria
				int resp2 = this.manejoInterfazTesoreria(paciente.getCodigoPersona(), usuario.getCodigoInstitucionInt());
				
				
				
				logger.info("Hubo desasocio=> "+prueba+" otro reusltado=>"+resp+", resp0=>"+resp0+", resp1=>"+resp1+", resp2=>"+resp2+", prueba=>"+prueba);
				if(resp>0&&resp0>0&&resp1>0&&resp2>0&&prueba>0)
				{
					UtilidadBD.finalizarTransaccion(con);
					this.setObservable(paciente);
				}
				else
				{
					ActionErrors errores = new ActionErrors();
					errores.add("No se grabó informacion",new ActionMessage("errors.noSeGraboInformacion","DEL CIERRE DE CUENTA"));
					saveErrors(request,errores);
					cierreCuentaForm.setEstado("empezar");
					UtilidadBD.abortarTransaccion(con);
				}
						
				
			}
		}
		else
		{
			logger.info("Datos antes de cerrar!!!");
			logger.info("identificacion paciente=> "+paciente.getTipoIdentificacionPersona()+paciente.getNumeroIdentificacionPersona());
			logger.info("codigo cuenta a cerrar=> "+paciente.getCodigoCuenta());
			
			
			int prueba = 1;
			if(paciente.getExisteAsocio()&&paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
				prueba=cuenta.desAsociarCuenta(con, usuario.getLoginUsuario(), paciente.getCodigoIngreso()+"","");
			
			if(prueba>0)
			{
			
				int resp=cuenta.registrarCierreCuenta(con,paciente.getCodigoCuenta(),usuario.getLoginUsuario(),cierreCuentaForm.getMotivoCierre(),ConstantesBD.inicioTransaccion);
				int resp0 = 0;
				int resp1 = 0;
				int resp2 = 0;
				int resp3 = 0;
				int resp4 = 0;
				int resp6 = 0;
				
				boolean resp5 = false;
				logger.info("Éxito al registrar el cierre=> "+resp);
				if((paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion||
					paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)&&!paciente.esIngresoEntidadSubcontratada())
				{
					resp0=cuenta.cambiarEstadoCuentaTransaccional2(con,ConstantesBD.codigoEstadoCuentaCerrada,paciente.getCodigoCuenta(),ConstantesBD.continuarTransaccion);
					logger.info("Éxito al cambiar estado de la cuenta=> "+resp);
					resp1=this.cambiarEstadoValoracionInicial(con,paciente.getCodigoCuenta(),ConstantesBD.continuarTransaccion);
					logger.info("Éxito al cambiar el estado de la valoraciï¿½n inicial=> "+resp);
					
				}
				else
				{
					resp0=cuenta.cambiarEstadoCuentaTransaccional2(con,ConstantesBD.codigoEstadoCuentaCerrada,paciente.getCodigoCuenta(),ConstantesBD.continuarTransaccion);
					resp1 = 1;
				}
				
				
				
				//se verifica via ingreso
				if(paciente.getCodigoUltimaViaIngreso()!=ConstantesBD.codigoViaIngresoConsultaExterna&&
						paciente.getCodigoUltimaViaIngreso()!=ConstantesBD.codigoViaIngresoAmbulatorios)
				{
					
					
					IngresoGeneral objetoIngreso= new IngresoGeneral();
					
					//si tuvo valoraciï¿½n debiï¿½ haber tenido egreso
					if(UtilidadValidacion.tieneValoraciones(con,paciente.getCodigoCuenta()))
					{
						Egreso objetoEgreso= new Egreso();
						logger.info("codigo cuenta a cerrar=> "+paciente.getCodigoCuenta());
						logger.info("Se cargó egreso=> "+objetoEgreso.cargarFechaHoraEgreso(con, paciente.getCodigoCuenta()));
						String fechaTemp[]=objetoEgreso.getFechaEgreso().trim().split("-");
						objetoIngreso.setAnioEgreso( fechaTemp[2]   );
						objetoIngreso.setMesEgreso(  fechaTemp[1]  );
						objetoIngreso.setDiaEgreso(  fechaTemp[0]  );
						objetoIngreso.setHoraEgreso(  objetoEgreso.getHoraEgreso()  );
						
						resp2 = 1;
					}
					else
					{
						String fechaTemp[]=UtilidadFecha.getFechaActual().trim().split("/");
						objetoIngreso.setAnioEgreso( fechaTemp[2]   );
						objetoIngreso.setMesEgreso(  fechaTemp[0]  );
						objetoIngreso.setDiaEgreso(  fechaTemp[1]  );
						objetoIngreso.setHoraEgreso(  UtilidadFecha.getHoraActual() );
						
						//liberacion de la cama para hospitalizacion
						if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion&&
							!paciente.isHospitalDia()&&!paciente.esIngresoEntidadSubcontratada())
						{
							//se carga la ultima cama
							int codigoCama = Utilidades.getUltimaCamaTraslado(con,paciente.getCodigoCuenta());
							
							//Se verifica que tenga cama para hacer la liberacion
							if(codigoCama>0)
							{
								///se cambia el estado de la cama
								Cama cama = new Cama();
								resp2=cama.cambiarEstadoCama(con,codigoCama+"",ConstantesBD.codigoEstadoCamaDisponible);
								TrasladoCamas traslado = new TrasladoCamas();
								if(resp2>0)
								{
									if(traslado.actualizarFechaHoraFinalizacionNoTransaccional(con,paciente.getCodigoCuenta(),UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),""))
										resp2 = 1;
									else
										resp2 = 0;
								}
							}
							else
								resp2 = 1;
						}
						//Si es hospital día se registra la salida
						else if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion&&paciente.isHospitalDia())
						{
							
							//Se elimina el registro de reingreso de hospital día 
							DtoReingresoSalidaHospiDia dtoReingreso = new DtoReingresoSalidaHospiDia();
							dtoReingreso.setCuenta(paciente.getCodigoCuenta()+"");
							resp2 = ReingresoHospitalDia.eliminarReingresoSalidaHospitalDiaXCuenta(con, dtoReingreso);
						}
						else
							resp2 = 1;
						
					}
					resp3=objetoIngreso.modificarIngresoTransaccional2(con, paciente.getCodigoIngreso()+"",ConstantesBD.continuarTransaccion);
					resp5 = IngresoGeneral.actualizarEstadoIngreso(con, paciente.getCodigoIngreso()+"", ConstantesIntegridadDominio.acronimoEstadoCerrado, usuario.getLoginUsuario());
					logger.info("Hubo modificacion de ingreso=> "+resp3+" otro reusltado=>"+resp);
				}
				else
				{
					resp2 = 1;
					
					IngresoGeneral objetoIngreso= new IngresoGeneral();
					
					String fechaTemp[]=UtilidadFecha.getFechaActual().trim().split("/");
					
					
					objetoIngreso.setAnioEgreso( fechaTemp[2]   );
					objetoIngreso.setMesEgreso(  fechaTemp[0]  );
					objetoIngreso.setDiaEgreso(  fechaTemp[1]  );
					objetoIngreso.setHoraEgreso(  UtilidadFecha.getHoraActual() );
					resp3=objetoIngreso.modificarIngresoTransaccional2(con, paciente.getCodigoIngreso()+"",ConstantesBD.continuarTransaccion);
					resp5 = IngresoGeneral.actualizarEstadoIngreso(con, paciente.getCodigoIngreso()+"", ConstantesIntegridadDominio.acronimoEstadoCerrado, usuario.getLoginUsuario());
					logger.info("Hubo modificacion de ingreso=> "+resp3+" otro reusltado=>"+resp);
					
				}
				//Manejo de la interfaz de tesoreria
				resp4 = this.manejoInterfazTesoreria(paciente.getCodigoPersona(), usuario.getCodigoInstitucionInt());
				
				//REversion del registro paciente entidad subcontratada
				if(paciente.esIngresoEntidadSubcontratada())
					resp6 = PacientesEntidadesSubCon.reversarPacienteEntidadSubcontratada(con, paciente.getPacEntidadSubcontratada(), usuario.getLoginUsuario());
				else
					resp6 = 1;
				
				logger.info("resp: "+resp+", resp0: "+resp0+", resp1: "+resp1+", resp2: "+resp2+", resp3: "+resp3+", resp4: "+resp4+", resp5: "+resp5+", resp6: "+resp6);
				
				if(resp>0&&resp0>0&&resp1>0&&resp2>0&&resp3>0&&resp4>0&&resp5&&resp6>0)
				{
					UtilidadBD.finalizarTransaccion(con);
					this.setObservable(paciente);
				}
				else
				{
					ActionErrors errores = new ActionErrors();
					errores.add("No se grabó informacion",new ActionMessage("errors.noSeGraboInformacion","DEL CIERRE DE CUENTA"));
					saveErrors(request,errores);
					cierreCuentaForm.setEstado("empezar");
					UtilidadBD.abortarTransaccion(con);
					
				}
			}
			else
			{
				ActionErrors errores = new ActionErrors();
				errores.add("No se grabó informacion",new ActionMessage("errors.noSeGraboInformacion","DEL DESASOCIO DE CUENTAS"));
				saveErrors(request,errores);
				cierreCuentaForm.setEstado("empezar");
			}
		}
		
		
		logger.info("codigo de la persona=> "+paciente.getCodigoPersona());
		UtilidadesManejoPaciente.cargarPaciente(con, usuario, paciente, request);
		UtilidadBD.cerrarConexion(con);
		
		//Se limpian variables en sesiï¿½n
		if(request.getSession().getAttribute("erroresValidacionCierreCuenta")!=null)
			request.getSession().removeAttribute("erroresValidacionCierreCuenta");
		
		return mapping.findForward("principal");
	}

	/**
	 * Mï¿½todo usado para cambiar el estado de la valoracion
	 * inicial en el caso de que no se haya atendido
	 * @param con
	 * @param codigoCuenta
	 * @param finTransaccion
	 */
	private int cambiarEstadoValoracionInicial(Connection con, int codigoCuenta, String finTransaccion) {
		
		try
		{
			//se verifica que la cuenta no haya atendido la valoracion
			if(!UtilidadValidacion.tieneValoraciones(con,codigoCuenta))
			{
				int numeroSolicitud=UtilidadValidacion.getCodigoSolicitudValoracionInicial(con,codigoCuenta);
				logger.info("Nï¿½mero de solicitud de la cuenta=> "+numeroSolicitud);
				Solicitud solicitud=new Solicitud();
				//se anula la solicitud relacionada con la valoraciï¿½n
				ResultadoBoolean resultado= solicitud.cambiarEstadosSolicitudTransaccional(con,
						numeroSolicitud,
						ConstantesBD.codigoEstadoFacturacionAnulada,
						ConstantesBD.codigoEstadoHCAnulada,
						finTransaccion);
				logger.info("ï¿½xito al cambiar el estado solicitud=> "+resultado.isTrue()+" "+resultado.getDescripcion());
				if(resultado.isTrue())
					return 1;
				else
					return -1;
			}
			//de lo contrario se finaliza la transacciï¿½n
			else
			{
				//DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).endTransaction(con);
				return 1;
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en cambiarEstadoValoracionInicial de CierreCuentaAction: "+e);
			
			return -1;
		}
		
	}

	/**
	 * Este mï¿½todo especifica las acciones a realizar en el estado
	 * empezar.
	 * 
	 * @param CierreCuentaForm cierreCuentaForm
	 * 				para pre-llenar datos si es necesario
	 * @param mapping Mapping para manejar la navegaciï¿½n
	 * @param paciente
	 * @param con Conexiï¿½n con la fuente de datos
	 * @param request
	 * @return ActionForward a la pï¿½gina principal "cierreCuenta.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar(	CierreCuentaForm cierreCuentaForm, 
																ActionMapping mapping, 
																PersonaBasica paciente, Connection con, HttpServletRequest request) throws SQLException
	{
		
		//Limpiamos variables en sesion
		if(request.getSession().getAttribute("erroresValidacionCierreCuenta")!=null)
			request.getSession().removeAttribute("erroresValidacionCierreCuenta");
		///objetos de uso
		Cuenta cuenta=new Cuenta();
		int id;
		
		//se verifica el estado pues puede que el estado sea tambiï¿½n empezarAsocio
		//proveniente de la selecciï¿½n de una de las cuentas de un asocio
		if(cierreCuentaForm.getEstado().equals("empezar"))
		{
			///Limpiamos lo que venga del form
			cierreCuentaForm.reset();
			//se revisa que haya asocio
			if(paciente.getExisteAsocio()&&paciente.getCodigoCuentaAsocio()!=0)
			{
				//se llena una estructura con los datos de las cuentas asociadas
				cuenta.cargar(con,paciente.getCodigoCuenta()+"");
				this.llenarDatosCuenta(con,cuenta,cierreCuentaForm,"principal");
				cuenta.cargar(con,paciente.getCodigoCuentaAsocio()+"");
				this.llenarDatosCuenta(con,cuenta,cierreCuentaForm,"asociada");
				UtilidadBD.cerrarConexion(con);
				//se redirecciona a una pï¿½gina para elegir el asocio
				return mapping.findForward("casoAsocio");
			}
			else
			{
				cuenta.cargar(con,paciente.getCodigoCuenta()+"");
				id=Integer.parseInt(cuenta.getCuenta().getIdCuenta());
			}
		}
		else
		{
			//caso de cuenta asociada (estado = empezarAsocio)
			id=cierreCuentaForm.getIdCuenta();
			cuenta.cargar(con,id+"");
			//se reubica de nuevo al estado empezar
			cierreCuentaForm.setEstado("empezar");
		}
		//********************************************************************************
		//*******************VALIDACIONES************************************************
		Collection errores=new ArrayList();
		
		//1)Validar si la cuenta estï¿½ abierta o asociada
		if(!ValidacionesCierreCuenta.validarEstadoCuenta(con,id)){
			errores=this.editarErrores("error.facturacion.cierreCuenta.estadoCuenta",errores);
		}
		
		//Esta validación se quito por la Tarea 46623. La cual argumenta que se puede cerrar 
		//la cuenta para pacientes con varios convenios
		//2)Validar que la cuenta solo tenga un convenio asociado
		/*if(cuenta.getCuenta().getConvenios().length>1)
			errores=this.editarErrores("error.facturacion.cierreCuenta.variosConvenios",errores);*/
		
		//3)Validar si hay solicitudes de servicios pendientes
		if(!ValidacionesCierreCuenta.validarCargosSeviciosXCuenta(con,id)){
			errores=this.editarErrores("error.facturacion.cierreCuenta.cargosServiciosXCuenta",errores);
		}
		//4)Validar si hay solicitudes de farmacia pendientes
		if(!ValidacionesCierreCuenta.validarCargosMedicamentosXCuenta(con,id)){
			errores=this.editarErrores("error.facturacion.cierreCuenta.cargosMedicamentosXCuenta",errores);
		}
		
		//5)Validar se hay solicitudes en estado pendiente
		if(!ValidacionesCierreCuenta.validarEstadosFactSolicitudes(con,id)){
			errores=this.editarErrores("error.facturacion.cierreCuenta.estadosFactSolicitudes",errores);
		}
	
		//VALIDACIONES SEGï¿½N Vï¿½A DE INGRESO
		//Consulta Externa
		if(cuenta.getCuenta().getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoConsultaExterna)
			if(ValidacionesCierreCuenta.validarSolicitudesConsultaExterna(con,id))
				errores=this.editarErrores("error.facturacion.cierreCuenta.solicitudesConsultaExterna",errores);
		
		//Hospitalizacion
		if(cuenta.getCuenta().getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion)
		{
			if(UtilidadValidacion.tieneValoraciones(con,id))
			{
				//A) Se verifica que haya habido egreso
				if(!UtilidadValidacion.existeEgresoCompleto(con,id))
				{
					errores=this.editarErrores("error.facturacion.cierreCuenta.egreso",errores);
				}
				//B) se verifica el estado de facturaciï¿½n de la valoracion
				if(!ValidacionesCierreCuenta.validarEstadoFactSolicitudValoracion(con,id))
					errores=this.editarErrores("error.facturacion.cierreCuenta.estadoFactSolVal",errores);
			}
			
			
		}
		
		//Urgencias
		if(cuenta.getCuenta().getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias)
		{
			if(UtilidadValidacion.tieneValoraciones(con,id))
			{
			    //A) se verifica si la cuenta tiene egreso
			    if(!UtilidadValidacion.existeEgresoAutomatico(con, id).puedoSeguir)
			    {
			        if(!UtilidadValidacion.existeEgresoCompleto(con,id))
			        {
						logger.info("Entrï¿½ aqui existe !!!!!!!!");
						errores=this.editarErrores("error.facturacion.cierreCuenta.egreso",errores);
					}
			    }
			    //B) se verifica el estado de facturaciï¿½n de la valoracion
				if(!ValidacionesCierreCuenta.validarEstadoFactSolicitudValoracion(con,id))
					errores=this.editarErrores("error.facturacion.cierreCuenta.estadoFactSolVal",errores);
			}
		}
		
		//SE suben errores a la sesiï¿½n
		if(errores.size()>0)
			request.getSession().setAttribute("erroresValidacionCierreCuenta",errores);	
		
		//********************************************************************************
		
		
		this.llenarForm(cierreCuentaForm,cuenta,con);
		
		
		//****************SE LLENAN LOS WARNINGS********************************************
		ElementoApResource advertencia01 = new ElementoApResource("error.facturacion.cierreCuenta.mensajeGeneral");
		cierreCuentaForm.getAdvertencias().add(advertencia01);
		//Se pregunta si el paciente es de entidad subcontratyadas
		if(paciente.esIngresoEntidadSubcontratada())
		{
			ElementoApResource advertencia02 = new ElementoApResource("errors.paciente.mensajeSubcontratacion");
			advertencia02.agregarAtributo(EntidadesSubContratadas.getDescripcionEntidadSubXIngreso(con, paciente.getCodigoIngreso()+""));
			cierreCuentaForm.getAdvertencias().add(advertencia02);
		}
		//*************************************************************************************
		
		UtilidadBD.cerrarConexion(con);
		
		return mapping.findForward("principal");		
	}

	/**
	 * Mï¿½todo para llenar lsod atos generales de la cuenta en caso de aoscio
	 * @param con
	 * @param cuenta
	 * @param cierreCuentaForm
	 * @param indice
	 */
	private void llenarDatosCuenta(Connection con, Cuenta cuenta, CierreCuentaForm cierreCuentaForm, String indice) {
		cierreCuentaForm.setDatosAsocio(indice+"_id",cuenta.getCuenta().getIdCuenta());
		cierreCuentaForm.setDatosAsocio(indice+"_fecha",cuenta.getCuenta().getFechaApertura()+" "+cuenta.getCuenta().getHoraApertura());
		Egreso egreso=new Egreso();
		egreso.cargarFechaHoraEgreso(con,Integer.parseInt(cuenta.getCuenta().getIdCuenta()));
		cierreCuentaForm.setDatosAsocio(indice+"_egreso",egreso.getFechaEgresoFormatoAp()+" "+egreso.getHoraEgresoCincoCaracteres());
		cierreCuentaForm.setDatosAsocio(indice+"_via",cuenta.getCuenta().getDescripcionViaIngreso());
		cierreCuentaForm.setDatosAsocio(indice+"_estado",cuenta.getCuenta().getDescripcionEstado());
		
	}

	/**
	 * Mï¿½todo para cargar en la Forma los datos de la cuenta que se va a cerrar
	 * @param cierreCuentaForm
	 * @param cuenta
	 * @param con
	 */
	private void llenarForm(CierreCuentaForm cierreCuentaForm, Cuenta cuenta, Connection con) {
		
		String[] fecha = cuenta.getCuenta().getFechaApertura().split("/");
		//se llena fecha y hora de apertura
		cierreCuentaForm.setFechaAperturaCuenta(
				UtilidadFecha.obtenerNombreMes(Integer.parseInt(fecha[1]))+
				" "+fecha[0]+"/"+fecha[2]+"  Hora:"+cuenta.getCuenta().getHoraApertura());
		
		cierreCuentaForm.setNombreConvenio(cuenta.getCuenta().getConvenios()[0].getConvenio().getNombre());
		cierreCuentaForm.setDescripcionViaIngreso(cuenta.getCuenta().getDescripcionViaIngreso());
		cierreCuentaForm.setDescripcionClasificacionSocioEconomica(cuenta.getCuenta().getConvenios()[0].getDescripcionClasificacionSocioEconomica());
		cierreCuentaForm.setNombreTipoAfiliado(cuenta.getCuenta().getConvenios()[0].getDescripcionTipoAfiliado());
		
		//Se editan los montos
		cierreCuentaForm.setNombreMontoCobro(cuenta.getCuenta().getConvenios()[0].getDescripcionMontoCobro());
		
		
		cierreCuentaForm.setNombreTipoRegimen(Utilidades.obtenerTipoRegimenConvenio(con, cuenta.getCuenta().getConvenios()[0].getConvenio().getCodigo()+"").split("-")[1]);
		
		//id de la cuenta 
		cierreCuentaForm.setIdCuenta(Integer.parseInt(cuenta.getCuenta().getIdCuenta()));
	}
	
	/**
	 * Mï¿½todo usado para cargar en sesiï¿½n los errores encontrados en la validaciï¿½n
	 * del cierre de cuentas
	 * @param mensajes_error
	 * @param request
	 * @param errores2
	 */
	private Collection editarErrores(String mensajes_error,Collection errores2) {
		Collection errores=errores2;
			
		
		//divisiï¿½n para obtener los atributos
		String[] sub_cadena=mensajes_error.split("@");
		
		//se revisa si el errore tiene atributos
		if(sub_cadena.length>1){
			//Se agrega la etiqueta a un objeto ElementoApResource
			
			
			ElementoApResource elem=new ElementoApResource(sub_cadena[0]);
			elem.agregarAtributo(sub_cadena[1]);
			errores.add(elem);
			
		}
		else{
			//Se agrega las etiquetas a un objeto ElemwentoApResource
			ElementoApResource elem=new ElementoApResource(mensajes_error);
			errores.add(elem);
		}
		
		return errores;
		
	}
	
	 /**
	* Mï¿½todo para hacer que el paciente
	* pueda ser visto por todos los usuario en la aplicacion
	* @param paciente
	*/
	private void setObservable(PersonaBasica paciente)
	{
		ObservableBD observable = (ObservableBD)getServlet().getServletContext().getAttribute("observable"); 
		if (observable != null) {
			synchronized (observable) {
				observable.setChanged();
				observable.notifyObservers(new Integer(paciente.getCodigoPersona()));
			}
		}
	}
	
	/**
	 * Método que inactiva el registro de interfaz del ingreso de la cuenta que se va a cerrar
	 * @param con
	 * @param paciente
	 * @param institucion
	 * @return
	 */
	private int manejoInterfazTesoreria(int codigoPaciente,  int institucion) 
	{
		ResultadoBoolean resp = new ResultadoBoolean(true,"");
		
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazPaciente(institucion)))
		{
			UtilidadBDInterfaz utilidadBD = new UtilidadBDInterfaz();
			
			//Se verifica si existe registro para el paciente
			DtoInterfazPaciente dto = utilidadBD.cargarPaciente(codigoPaciente+"", institucion);
			dto.setInstitucion(institucion);
			
			if(!dto.getCodigo().equals(""))
			{
				
				dto.setEstadoIngreso(ConstantesBDInterfaz.codigoEstadoIngresoInactivoPaciente);
				//Se modifica el registro de la interfaz de tesoreria
				resp = utilidadBD.modificarPaciente(dto);
			}
		}
		
		return resp.isTrue()?1:0;
		
	}
}
