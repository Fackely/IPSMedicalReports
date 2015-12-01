/*
 * Oct 22, 2007
 * Proyect axioma
 * Paquete com.princetonsa.action.historiaClinica
 * @author Jorge Armando Osorio Velasquez
 * Compilador Java 1.5.0_07-b03
 */
package com.princetonsa.action.historiaClinica;

import java.sql.Connection;
import java.sql.SQLException;

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
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.historiaClinica.RegistroTerapiasGrupalesForm;
import com.princetonsa.dto.cargos.DtoCargoDirectoHC;
import com.princetonsa.dto.cargos.DtoDiagnosticosCargoDirectoHC;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.CargosDirectos;
import com.princetonsa.mundo.cargos.UtilidadJustificacionPendienteArtServ;
import com.princetonsa.mundo.historiaClinica.RegistroTerapiasGrupales;
import com.princetonsa.mundo.inventarios.FormatoJustServNopos;
import com.princetonsa.mundo.medicamentos.DespachoMedicamentos;
import com.princetonsa.mundo.solicitudes.Solicitud;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
public class RegistroTerapiasGrupalesAction extends Action 
{

	private Logger logger=Logger.getLogger(RegistroTerapiasGrupalesAction.class);
	
	/**
	 * M�todo execute del action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		Connection con = null;
		try {
			if (form instanceof RegistroTerapiasGrupalesForm)
			{
				con = UtilidadBD.abrirConexion();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());

				if(usuario == null)
				{
					logger.warn("Profesional de la salud no v�lido (null)");			
					request.setAttribute("codigoDescripcionError", "errors.usuario.noCargado");
					return mapping.findForward("paginaError");
				}
				else if (!UtilidadValidacion.esProfesionalSalud(usuario))
				{
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Usuario NO personal de la salud. ", "errors.usuario.noAutorizado", true) ;
				}

				RegistroTerapiasGrupalesForm forma = (RegistroTerapiasGrupalesForm) form;
				RegistroTerapiasGrupales mundo=new RegistroTerapiasGrupales();
				String estado=forma.getEstado();

				logger.info("[RegistroTerapiasGrupalesAction]  --> " + estado);

				if(estado.equals("empezar"))
				{	
					if(ValoresPorDefecto.getEntidadManejaHospitalDia(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoNo))
					{

						forma.reset(usuario);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("seleccion");

					}
					if(ValoresPorDefecto.getEntidadManejaHospitalDia(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi))
					{

						forma.reset(usuario);
						UtilidadBD.closeConnection(con);
						return mapping.findForward("seccion");

					}
					return mapping.findForward("principal");
				}	


				else if(estado.equals("empezarNuevo"))
				{
					forma.reset(usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("empezarOtro"))
				{
					//forma.reset(usuario.getCodigoInstitucionInt(),usuario.getCodigoCentroAtencion()+"");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("alterno");
				}
				else if(estado.equals("centroCosto"))
				{
					forma.reset(usuario);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("seleccion");
				}
				else if(estado.equals("nuevoServicio"))
				{
					forma.setTipoServicio(Utilidades.obtenerTipoServicio(con,forma.getServicios("codigoServicio_0")+""));
					forma.setFinalidadesProcedimientos(Utilidades.obtenerFinalidadesServicio(con, Utilidades.convertirAEntero(forma.getServicios("codigoServicio_0")+""),usuario.getCodigoInstitucionInt()));
					return accionEmpezar(forma,mundo, con, mapping, usuario);
				}
				else if(estado.equals("nuevoServicioCentro"))
				{
					forma.setTipoServicio(Utilidades.obtenerTipoServicio(con,forma.getServicios("codigoServicio_0")+""));
					forma.setFinalidadesProcedimientos(Utilidades.obtenerFinalidadesServicio(con, Utilidades.convertirAEntero(forma.getServicios("codigoServicio_0")+""),usuario.getCodigoInstitucionInt()));
					return accionEmpezarCentro(forma,mundo, con, mapping, usuario);
				}
				else if(estado.equals("guardar"))
				{
					request.getSession().removeAttribute("MAPAJUS");
					logger.info("\n\n Especialidad Seleccionada >>"+forma.getEspecialidadProfResponde());

					return this.accionGuardar(con,forma,mundo,usuario,mapping,request);
				}
				else if(estado.equals("seleccionarTodas"))
				{
					logger.info("\n\nBOOOOOOOO>>>>>>>>>>"+forma.getBotonjus());
					this.accionSelDesTodosPacientes(con,forma);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("adicionarJus"))
				{
					request.getSession().setAttribute("MAPAJUS", forma.getJustificacionesServicios());
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("alterno");
				}
				else
				{
					forma.reset(usuario);
					logger.warn("Estado no valido dentro del flujo ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de RegistroTerapiasGrupalesForm");
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
	 */
	private void accionSelDesTodosPacientes(Connection con, RegistroTerapiasGrupalesForm forma) 
	{
		for(int i=0;i<Utilidades.convertirAEntero(forma.getPaciente("numRegistros")+"");i++)
		{
			forma.setPaciente("seleccionado_"+i,forma.getSelTodas());
		}
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @param mapping
	 * @param request 
	 * @return
	 */
	private ActionForward accionGuardar(Connection con, RegistroTerapiasGrupalesForm forma, RegistroTerapiasGrupales mundo, UsuarioBasico usuario, ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores=new ActionErrors();
		String temporalCodigoServicio="";
		
		//Anexo 550.*********************************************
		errores = validacionesFechaCPInformacionRIPS(con,forma,usuario);
		if(!errores.isEmpty())
		{
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("principal");
		}
		//******************************************************
		
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		//Se inserta la informaci�n de historia cl�nica del cargo directo
		CargosDirectos mundoCargosDirectos = new CargosDirectos();
		mundoCargosDirectos.setCargoDirectoHC(this.cargarVOTerapiaGrupal(forma,usuario));
		logger.info("\n\n Especialidad Seleccionada en  mundoCargosDirectos >>"+mundoCargosDirectos.getCargoDirectoHC().getEspecialidadProfesional());
		
		int codigoTerapia=mundoCargosDirectos.insertar(con);
		
		transaccion=codigoTerapia>0;
		//si fue exitosa se relaciona la terapia a cada cuenta del paciente
		if(transaccion)
			transaccion=mundo.insertarCuentasTerapia(con,codigoTerapia,forma.getPaciente());
		
		
		//si todo va bien. generar el cargo directo.
		if(transaccion)
		{
			transaccion=generarCargoDirecto(con,forma,mundo,usuario,codigoTerapia);
		}
		if(transaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("resumen");
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);			
			errores.add("error.errorEnBlanco",new ActionMessage("error.errorEnBlanco","No se pudo solicitar la Terapia"));
			saveErrors(request, errores);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("principal");
		}
	}
	
	/**
	 * Metodo que tiene todos los inserts necesarios para la generacion de cargos directos 
	 * @param con
	 * @param mundo 
	 * @param codigoTerapia 
	 * @param request
	 * @param mapping
	 * @param cargosForm
	 * @param codigoCuenta
	 * @param paciente
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	private boolean generarCargoDirecto(Connection con,RegistroTerapiasGrupalesForm forma,RegistroTerapiasGrupales mundo, UsuarioBasico usuario, int codigoTerapia)
	{
	    int numeroSolicitud=ConstantesBD.codigoNuncaValido;
	    boolean inserto=false;
	    
	    int temporalCodigoServicio=Utilidades.convertirAEntero(forma.getServicios("codigoServicio_0")+"");
	    int temporalCantidadServicio=1;
	    //String temporalNumeroAutorizacion="";
	    int temporalCodigoCentroCosto=usuario.getCodigoCentroCosto();
	    int temporalCodigoMedicoResponde=usuario.getCodigoPersona();
	    int temporalCodigoTipoRecargo=  ConstantesBD.codigoTipoRecargoSinRecargo;
	    
	    for (int i=0; i<Utilidades.convertirAEntero(forma.getPaciente("numRegistros")+""); i ++)
	    {    
	        if(UtilidadTexto.getBoolean(forma.getPaciente("seleccionado_"+i)+""))
	        {
	            
			    //PRIMERO SE HACE UN INSERT DE UNA SOLICITUD GENERAL
			    try
			    { 
			        numeroSolicitud= this.insertarSolicitudBasicaTransaccional(con, forma, Utilidades.convertirAEntero(forma.getPaciente("cuenta_"+i)+""), ConstantesBD.codigoTipoSolicitudCargosDirectosServicios, temporalCodigoCentroCosto/*, temporalNumeroAutorizacion*/);
			        if(numeroSolicitud<=0)
			        {
			        	return false;
			        }
			    }
			    catch(SQLException sqle)
			    {
			        return false;
			    }
			    
			    // se hace una actulizacion del medico que responde
			    try
			    {
			       inserto = this.actaulizarMedicoRespondeTransaccional(con, numeroSolicitud, temporalCodigoMedicoResponde);
			       if(!inserto)
				   {
			    	   return false;
				   }
			    }
			    catch(SQLException sqle)
			    {
			        return false;
			    }
			    
			    //SE INSERTA EL USUARIO QUE HIZO EL CARGO DIR Y EL TIPO DE RECARGO
			    inserto = this.insertarInfoCargosDirectosTransaccional(con, numeroSolicitud, usuario.getLoginUsuario(), temporalCodigoTipoRecargo, temporalCodigoServicio,codigoTerapia);
			    if(!inserto)
			    {
			    	 return false;
			    }
			    
			    inserto=this.cambiarEstadoMedicoSolicitudTransaccional(con, numeroSolicitud/*, temporalNumeroAutorizacion*/);
			    if(!inserto)
			    {
			        return false;
			    }
			    
			    try
			      {
			       inserto=this.actualizarEspecialidadProfResponde(con, numeroSolicitud, Utilidades.convertirAEntero(forma.getEspecialidadProfResponde()));
			       if(!inserto)
				   {
			    	   return false;
				   } 
			      }
			      catch(SQLException sqle)
			      {
			        return false;
			      }
			    
			    //GENERACION DEL CARGO Y SUBCUENTA - EVALUACION COBERTURA 
			    Cargos cargos= new Cargos();
			    inserto= cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	con, 
			    																			usuario,
			    																			Utilidades.convertirAEntero(forma.getPaciente("cuenta_"+i)+""),
			    																			Utilidades.convertirAEntero(forma.getPaciente("ingreso_"+i)+""),
			    																			false/*dejarPendiente*/, 
			    																			numeroSolicitud, 
			    																			ConstantesBD.codigoTipoSolicitudCargosDirectosServicios /*codigoTipoSolicitudOPCIONAL*/, 
			    																			temporalCodigoCentroCosto/*codigoCentroCostoEjecutaOPCIONAL*/, 
			    																			temporalCodigoServicio/*codigoServicioOPCIONAL*/, 
			    																			temporalCantidadServicio/*cantidadServicioOPCIONAL*/, 
			    																			ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
			    																			ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
			    																			/* "" -- numeroAutorizacionOPCIONAL,*/
			    																			"" /*esPortatil*/,forma.getFechaCP());

			    if(!inserto)
			    {
			        return false;
			    }
			    else
			    {
			    	//Objeto formato de justificaci�n de servicios No POS		
			        FormatoJustServNopos fjsn = new FormatoJustServNopos();
			        //SI EL INDICADOR DE JUSTIFICAR ES VERDADERO O PENDIENTE SE HACE EL INGRESO DE LA JUSTIFICACI�N NO POS
	                if (forma.getServicios("justificar_0").toString().equals("pendiente")){
	    				UtilidadJustificacionPendienteArtServ.insertarJusNP(con, numeroSolicitud, temporalCodigoServicio, 1, usuario.getLoginUsuario(), false, false, Utilidades.convertirAEntero(cargos.getDtoDetalleCargo().getCodigoSubcuenta()+""), "");
	    			}
	                if (forma.getServicios("justificar_0").toString().equals("true")){
	                	logger.info("CODIGO INSTITUCION>>>>>>>>>>"+usuario.getCodigoInstitucionInt());
	                	logger.info("LOGIN USUARIO>>>>>>>>>>>>>>>"+usuario.getLoginUsuario());
	                	logger.info("MAPA JUSTIFICACIONES SERVICIOS>>>>>>>>>>>>>>>"+forma.getJustificacionesServicios());
	                	logger.info("NUMERO SOLICITUD>>>>>>>>>>>>>>>"+numeroSolicitud);
	                	fjsn.ingresarJustificacion(con,
	                								usuario.getCodigoInstitucionInt(), 
							                		usuario.getLoginUsuario(), 
							                		forma.getJustificacionesServicios(), 
							                		numeroSolicitud,
							                		ConstantesBD.codigoNuncaValido,
							                		0,
							                		usuario.getCodigoPersona());
	                }
			    }
	        }
	    }  
	    
	   return true;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param temporalNumeroAutorizacion
	 * @return
	 */
	private boolean cambiarEstadoMedicoSolicitudTransaccional(Connection con, int numeroSolicitud/*, String numeroAutorizacion*/) 
	{
		int i=0;
	    int inserto= ConstantesBD.codigoNuncaValido;
	    DespachoMedicamentos despacho= new DespachoMedicamentos();
	    despacho.setNumeroSolicitud(numeroSolicitud);
	    try
	    {
	       inserto =despacho.cambiarEstadoMedicoSolicitudTransaccional(con, ConstantesBD.continuarTransaccion, ConstantesBD.codigoEstadoHCCargoDirecto/*, numeroAutorizacion*/); 
	    }
	    catch(SQLException sqle)
	    {
	        logger.warn("Error en el insert del cambiar esatdo de la solicitud transaccional con indice ="+i +"   error-->"+sqle);
	        return false;
	    }
	    if (inserto<1)
	    {
				return false;
		}
		return true;
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param loginUsuario
	 * @param codigoTerapia 
	 * @param temporalCodigoTipoRecargo
	 * @param temporalCodigoServicio
	 * @return
	 */
	private boolean insertarInfoCargosDirectosTransaccional(Connection con, int numeroSolicitud, String loginUsuario, int tipoRecargo, int codigoServicioSolicitado, int codigoTerapia) 
	{
		CargosDirectos cargo= new CargosDirectos();
		
		cargo.llenarMundoCargoDirecto(numeroSolicitud,loginUsuario,tipoRecargo,codigoServicioSolicitado,codigoTerapia+"",true,"");
		
	    int resultado=cargo.insertar(con);
	    if(resultado<=0)
	       return false;
	    else
	        return true;
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param temporalCodigoMedicoResponde
	 * @return
	 * @throws SQLException 
	 */
	private boolean actaulizarMedicoRespondeTransaccional(Connection con, int numeroSolicitud, int codigoMedico) throws SQLException 
	{
		Solicitud objetoSol= new Solicitud();
	    UsuarioBasico medico= new UsuarioBasico();
	    medico.cargarUsuarioBasico(con, codigoMedico);
	    int resultado = objetoSol.actualizarMedicoRespondeTransaccional(con, numeroSolicitud, medico, ConstantesBD.continuarTransaccion);
	    
	    if(resultado<=0)
	       return false;
	    else
	        return true;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codEspecialidad
	 * @return
	 * @throws SQLException
	 */
	private boolean actualizarEspecialidadProfResponde(Connection con, int numeroSolicitud, int codEspecialidad)throws SQLException
	{
		Solicitud objetoSol= new Solicitud();
		 int resultado = objetoSol.actualizarEspecialidadTransaccional(con, numeroSolicitud, codEspecialidad, ConstantesBD.continuarTransaccion);
		    
		    if(resultado<=0)
		       return false;
		    else
		        return true;
	}

	/**
	 * 
	 * @param con
	 * @param cargosForm
	 * @param codigoCuenta
	 * @param paciente
	 * @param user
	 * @param tipoSolicitud
	 * @param centroCostoSolicitado
	 * @param numeroAutorizacion
	 * @return
	 * @throws SQLException
	 */
	private int insertarSolicitudBasicaTransaccional(Connection con,RegistroTerapiasGrupalesForm forma,int codigoCuenta,int tipoSolicitud,int centroCosto/*,String numeroAutorizacion*/) throws SQLException
	{
		Solicitud objectSolicitud= new Solicitud();
		int numeroSolicitudInsertado=0;
		objectSolicitud.clean();
		objectSolicitud.setFechaSolicitud(forma.getFechaSolicitud());
		objectSolicitud.setHoraSolicitud(forma.getHoraSolicitud());
		objectSolicitud.setTipoSolicitud(new InfoDatosInt(tipoSolicitud));
		//objectSolicitud.setNumeroAutorizacion(numeroAutorizacion);
		objectSolicitud.setEspecialidadSolicitante(new InfoDatosInt(ConstantesBD.codigoEspecialidadMedicaNinguna));
		objectSolicitud.setOcupacionSolicitado(new InfoDatosInt(ConstantesBD.codigoOcupacionMedicaNinguna));
		
		objectSolicitud.setCentroCostoSolicitante(new InfoDatosInt(centroCosto));
		
		objectSolicitud.setCentroCostoSolicitado(new InfoDatosInt(centroCosto));
		objectSolicitud.setCodigoCuenta(codigoCuenta);
		objectSolicitud.setCobrable(true);
		objectSolicitud.setVaAEpicrisis(false);
		objectSolicitud.setUrgente(false);
		//primero lo inserto como pendiente, pero si mas adelante es exitoso el cargo entonces le hago un update a  cargada
		objectSolicitud.setEstadoHistoriaClinica(new InfoDatosInt(ConstantesBD.codigoEstadoHCCargoDirecto));
		try
		{ 
		numeroSolicitudInsertado=objectSolicitud.insertarSolicitudGeneralTransaccional(con, ConstantesBD.continuarTransaccion);
		}
		catch(SQLException sqle)
		{
		logger.warn("Error en la transaccion del insert en la solicitud b�sica");
		return 0;
		}
		return numeroSolicitudInsertado;
	}
	

	/**
	 * M�todo que carga el Value Object desde la forma
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private  DtoCargoDirectoHC cargarVOTerapiaGrupal(RegistroTerapiasGrupalesForm forma, UsuarioBasico usuario) 
	{
		forma.setDiagnosticos("numRegistros",forma.getNumDiagnosticos()+"");
		logger.info("\n\n Especialidad Seleccionada en cargarVOTerapiaGrupal >>"+forma.getEspecialidadProfResponde());
		
		DtoCargoDirectoHC dtoCargoDirectoHC = new DtoCargoDirectoHC();
		dtoCargoDirectoHC.setCargado(true);
		dtoCargoDirectoHC.setFechaSolicitud(UtilidadFecha.conversionFormatoFechaABD(forma.getFechaCP()));
		dtoCargoDirectoHC.setHoraSolicitud(forma.getHoraSolicitud());
		dtoCargoDirectoHC.setCodigoServicio(Utilidades.convertirAEntero(forma.getServicios("codigoServicio_0")+"", true));
		dtoCargoDirectoHC.setCodigoTipoServicio(forma.getTipoServicio());
		dtoCargoDirectoHC.setObservaciones(forma.getObservaciones());
		dtoCargoDirectoHC.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
		dtoCargoDirectoHC.setCodigoCausaExterna(forma.getCausaExternaConsulta());
		dtoCargoDirectoHC.setManejaRips(forma.isValidarRips());
		dtoCargoDirectoHC.setCodigoFinalidadConsulta(forma.getFinalidadConsulta());
		dtoCargoDirectoHC.setCodigoFinalidadProcedimiento(forma.getFinalidadProcedimiento());
		dtoCargoDirectoHC.setLoginUsuarioModifica(usuario.getLoginUsuario());
		dtoCargoDirectoHC.setTipo(ConstantesIntegridadDominio.acronimoTerapiaGrupal);
		dtoCargoDirectoHC.setEspecialidadProfesional(forma.getEspecialidadProfResponde());
		
		//Se verifica si hubo diagn�sticos
		if(!UtilidadTexto.isEmpty(forma.getDiagnosticos("principal")+""))
		{
			//Se a�ade el diagn�stico principal
			DtoDiagnosticosCargoDirectoHC dtoDiagnostico = new DtoDiagnosticosCargoDirectoHC();
			String[] diagnostico = forma.getDiagnosticos("principal").toString().split(ConstantesBD.separadorSplit);
			dtoDiagnostico.setAcronimoDiagnostico(diagnostico[0]);
			dtoDiagnostico.setCieDiagnostico(Integer.parseInt(diagnostico[1]));
			dtoDiagnostico.setCodigoTipoDiagnostico(forma.getTipoDiagnosticoPrincipal());
			dtoDiagnostico.setPrincipal(true);
			
			dtoCargoDirectoHC.getDiagnosticos().add(dtoDiagnostico);
			
			//Se a�ade el resto de los diagn�sticos relacioandos
			for(int i=0;i<Utilidades.convertirAEntero(forma.getDiagnosticos("numRegistros")+"", true);i++)
			{
				//Solo se a�aden los chequeados
				if(UtilidadTexto.getBoolean(forma.getDiagnosticos("checkRel_"+i)+""))
				{
					DtoDiagnosticosCargoDirectoHC dtoDiagRel = new DtoDiagnosticosCargoDirectoHC();
					diagnostico = forma.getDiagnosticos("relacionado_"+i).toString().split(ConstantesBD.separadorSplit);
					dtoDiagRel.setAcronimoDiagnostico(diagnostico[0]);
					dtoDiagRel.setCieDiagnostico(Integer.parseInt(diagnostico[1]));
					dtoCargoDirectoHC.getDiagnosticos().add(dtoDiagRel);
				}
			}
		}
		
		return dtoCargoDirectoHC;
	}

	/**
	 * 
	 * @param forma
	 * @param mundo 
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezar(RegistroTerapiasGrupalesForm forma, RegistroTerapiasGrupales mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.setSexoServicio(Utilidades.obtenerSexoServicio(con,forma.getServicios("codigoServicio_0")+""));
		forma.setPaciente(mundo.getpacientesTerapiasGrupales(con,"",usuario.getCodigoCentroAtencion(),forma.getSexoServicio()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("principal");
	}
	
	
	/**
	 * 
	 * @param forma
	 * @param mundo
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionEmpezarCentro(RegistroTerapiasGrupalesForm forma, RegistroTerapiasGrupales mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.setSexoServicio(Utilidades.obtenerSexoServicio(con,forma.getServicios("codigoServicio_0")+""));
		forma.setPaciente(mundo.getpacientesTerapiasGrupalesCentro(con,"",usuario.getCodigoCentroAtencion(),forma.getSexoServicio(),forma.getCentroCosto()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("alterno");
	}
	
	//*****************************************************************************************************************************
	//Anexo 550********************************************************************************************************************
	
	/**
	 * Realiza validaciones sobre la fecha de Consulta o Procedimiento de la Informaci�n RIPS
	 * @param Connection con
	 * @param RegistroTerapiasGrupalesForm forma
	 * */
	private ActionErrors validacionesFechaCPInformacionRIPS(Connection con,RegistroTerapiasGrupalesForm forma,UsuarioBasico usuario)
	{
		PersonaBasica pacienteMundo;
		ActionErrors errores = new ActionErrors();
		
		if(forma.getTipoServicio().equals(ConstantesBD.codigoServicioInterconsulta+"") || 
				forma.getTipoServicio().equals(ConstantesBD.codigoServicioProcedimiento+""))
		{
			for(int i=0;i<Utilidades.convertirAEntero(forma.getPaciente().get("numRegistros")+"");i++)
			{
				if(UtilidadTexto.getBoolean(forma.getPaciente().get("seleccionado_"+i)+""))						
				{					
					//Valida si el Servicio es de tipo Solicitud o Procedimiento la fecha sea menor a la fecha de Ingreso del Paciente					
					try
					{
						pacienteMundo = new PersonaBasica();
						pacienteMundo.cargar(con,Integer.parseInt(forma.getPaciente().get("codigopaciente_"+i).toString()));
						pacienteMundo.cargarPaciente(con,
								Integer.parseInt(forma.getPaciente().get("codigopaciente_"+i).toString()), 
										usuario.getCodigoInstitucionInt()+"", 
										usuario.getCodigoCentroAtencion()+"");
						
						//Valida que la fecha sea menor igual que la fecha del sistema				
						if(UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),"00:00",forma.getFechaCP(),"00:00").isTrue())
						{
							//Valida que la fecha sea mayor igual a la fecha del ingreso del paciente
							if(!UtilidadFecha.compararFechas(forma.getFechaCP(),"00:00",pacienteMundo.getFechaIngreso(),"00:00").isTrue())
							{
								if(forma.getTipoServicio().equals(ConstantesBD.codigoServicioInterconsulta+""))
									errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual"," de Consulta "+forma.getFechaCP()," de Ingreso "+pacienteMundo.getFechaIngreso()+" del Paciente: "+pacienteMundo.getApellidosNombresPersona()+" "+pacienteMundo.getTipoIdentificacionPersona(false)+" "+pacienteMundo.getNumeroIdentificacionPersona()));
								else if(forma.getTipoServicio().equals(ConstantesBD.codigoServicioProcedimiento+""))
									errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual"," de Procedimiento "+forma.getFechaCP()," de Ingreso "+pacienteMundo.getFechaIngreso()+" del Paciente: "+pacienteMundo.getApellidosNombresPersona()+" "+pacienteMundo.getTipoIdentificacionPersona(false)+" "+pacienteMundo.getNumeroIdentificacionPersona()));
							}
						}					
						else
						{
							if(forma.getTipoServicio().equals(ConstantesBD.codigoServicioInterconsulta+""))
								errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual"," de Consulta "+forma.getFechaCP()," Actual "+UtilidadFecha.getFechaActual()));
							else if(forma.getTipoServicio().equals(ConstantesBD.codigoServicioProcedimiento+""))
								errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual"," de Procedimiento "+forma.getFechaCP()," Actual "+UtilidadFecha.getFechaActual()));
						}
					}
					catch (Exception e) {
						e.printStackTrace();
					}			
				}
			}
		}
		
		return errores;
	}
	
	//*****************************************************************************************************************************
}