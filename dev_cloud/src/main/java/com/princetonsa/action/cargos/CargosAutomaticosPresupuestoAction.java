package com.princetonsa.action.cargos;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ordenesMedicas.UtilidadesOrdenesMedicas;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.cargos.CargosAutomaticosPresupuestoForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.CargosAutomaticosPresupuesto;
import com.princetonsa.mundo.cargos.CargosDirectos;
import com.princetonsa.mundo.cargos.UtilidadJustificacionPendienteArtServ;
import com.princetonsa.mundo.medicamentos.DespachoMedicamentos;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.fwk.exception.IPSException;


public class CargosAutomaticosPresupuestoAction extends Action 
{
	
	
	/**
	 * 
	 */
	private Logger logger = Logger.getLogger(CargosAutomaticosPresupuestoAction.class);
	
	
	
	/**
	 * 
	 */
	public ActionForward execute(	ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response ) throws Exception
			{

		Connection con=null;
		try{
			if (response==null); //Para evitar que salga el warning
			if(form instanceof CargosAutomaticosPresupuestoForm)
			{


				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				CargosAutomaticosPresupuestoForm forma =(CargosAutomaticosPresupuestoForm)form;
				CargosAutomaticosPresupuesto mundo = new CargosAutomaticosPresupuesto();
				forma.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
				String estado=forma.getEstado();

				logger.warn("El estado en los cargos automaticos es------->"+estado);

				if(estado.equals("empezar"))
				{    
					ActionForward validacionesGenerales = this.validacionesAccesoUsuario(con, paciente,  mapping, request, forma, usuario,mundo);
					if (validacionesGenerales != null)
					{
						UtilidadBD.cerrarConexion(con);
						return validacionesGenerales ;
					}
				}	

				if(estado == null)
				{
					forma.reset();	
					logger.warn("Estado no valido dentro del flujo de Cargos Directos (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
				//estado donde se realizarán las validaciones de los estados de la cuenta
				else if (estado.equals("empezar"))
				{
					ArrayList<InfoDatosInt> arrayVacio= new ArrayList<InfoDatosInt>();
					arrayVacio.add(new InfoDatosInt(ConstantesBD.codigoNuncaValido, "Seleccione"));
					forma.reset();
					forma.setServiciosAutomaticos(mundo.cargarServiciosAutomaticos(con,paciente.getCodigoIngreso()));
					for(int i=0;i<Integer.parseInt(forma.getServiciosAutomaticos().get("numRegistros")+"");i++)
						forma.getEspProfResponde().add(i,arrayVacio);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("guardarServicios"))
				{
					return this.flujoInsertarServicios(con, request, mapping, forma, paciente.getCodigoCuenta(), paciente, usuario);
				}
				else if(estado.equals("empezarContinuar"))
				{
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("generarReporte"))
				{                
					this.generarReporte(con, forma, mapping, request, usuario,paciente);                
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("resumen");
				}
				// Cambio Segun Anexo 809
				else if(estado.equals("filtrarEspMedicoResponde"))
				{
					return accionFiltrarEspecialidProfResponde(con,forma,response);
				}
				// Fin Cambio Segun Anexo 809
				else
				{
					forma.reset();	
					logger.warn("Estado no valido dentro del flujo de Cargos Directos (null) ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
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
	 * 
	 * @param con
	 * @param paciente
	 * @param map
	 * @param req
	 * @param forma
	 * @param user
	 * @return
	 */
	protected ActionForward validacionesAccesoUsuario( Connection con, PersonaBasica paciente, 
            											ActionMapping map, HttpServletRequest req,
            											CargosAutomaticosPresupuestoForm forma, UsuarioBasico user,CargosAutomaticosPresupuesto mundo)
	{
		try
		{
			//Se verifica que el paciente se encuentre cargado en sesión
			if(paciente == null|| paciente.getCodigoTipoIdentificacionPersona().equals(""))
			{
				logger.warn("Paciente no válido (null)");			
				req.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
				return map.findForward("paginaError");
			}
			
			if(!mundo.pacienteTienePresupuesto(con, paciente.getCodigoIngreso()))
				return ComunAction.accionSalirCasoError(map, req, con, logger, "error.cargo.pacienteSinPresupuesto", "error.cargo.pacienteSinPresupuesto", true);
			
			/*if((!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getCodigo().trim().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))||(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getCodigo().trim().equals(ConstantesIntegridadDominio.acronimoEstadoCerrado)))
			{
				return ComunAction.accionSalirCasoError(map, req, con, logger, "errors.ingresoEstadoDiferente", "errors.ingresoEstadoDiferenteAbierto", true);
			}*/
			if(UtilidadValidacion.esCuentaValida(con, paciente.getCodigoCuenta())<1)
			{
				return ComunAction.accionSalirCasoError(map, req, con, logger, "errors.paciente.cuentaNoValida", "errors.paciente.cuentaNoValida", true);
			}
		 }
	    catch(Exception e)
	    {
	        logger.warn("Error en las validaciones de acceso al usuario "+e.toString());
	        req.setAttribute("codigoDescripcionError", "error.facturacion.cuentaEnProcesoFact");
			return map.findForward("paginaError");
	    }
		return null;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param request
	 * @param mapping
	 * @param cargosForm
	 * @param codigoCuenta
	 * @param paciente
	 * @param user
	 * @return
	 * @throws SQLException
	 */
	private ActionForward flujoInsertarServicios		(	Connection con, 
																			HttpServletRequest request,
																			ActionMapping mapping, 
																			CargosAutomaticosPresupuestoForm forma,
																			int codigoCuenta,
																			PersonaBasica paciente,
																			UsuarioBasico user
																			) throws SQLException, IPSException
	{
		
		CargosAutomaticosPresupuesto mundo = new CargosAutomaticosPresupuesto();
		int numeroSolicitud=ConstantesBD.codigoNuncaValido;
		boolean inserto=false;
		
		int temporalCodigoServicio=ConstantesBD.codigoNuncaValido;
		int temporalCantidadServicio=ConstantesBD.codigoNuncaValido;
		//String temporalNumeroAutorizacion="";
		int temporalCodigoCentroCostoEjecuta=ConstantesBD.codigoNuncaValido;
		int temporalCodigoMedicoResponde=ConstantesBD.codigoNuncaValido;
		int temporalCodigoTipoRecargo= ConstantesBD.codigoNuncaValido;
		int temporalCodigoPool= ConstantesBD.codigoNuncaValido;
		int temporalCodigoEspProfResp= ConstantesBD.codigoNuncaValido;
		
		forma.setCodigosDetallesCargo(new Vector());
		for (int i=0; i<Integer.parseInt(forma.getServiciosAutomaticos("numRegistros")+""); i ++)
		{    
			if(UtilidadTexto.getBoolean(forma.getServiciosAutomaticos("seleccionado_"+i)+""))
			{
				temporalCodigoServicio= Integer.parseInt(forma.getServiciosAutomaticos("codigoservicio_"+i)+"");
				temporalCantidadServicio= Integer.parseInt(forma.getServiciosAutomaticos("cantidad_"+i)+"");
				//temporalNumeroAutorizacion=forma.getServiciosAutomaticos("autorizacionServicio_"+i)+"";
				temporalCodigoCentroCostoEjecuta= Integer.parseInt(forma.getServiciosAutomaticos("centroCosto_"+i)+"");
				temporalCodigoMedicoResponde= Integer.parseInt(forma.getServiciosAutomaticos("medicoTratante_"+i)+"");
				//temporalCodigoTipoRecargo= Integer.parseInt(forma.getServiciosAutomaticos("codigoRecargo_"+i)+"");
				temporalCodigoPool= Integer.parseInt(forma.getServiciosAutomaticos("poolMedico_"+i)+"");
				temporalCodigoEspProfResp = Utilidades.convertirAEntero(forma.getServiciosAutomaticos("espMedicoTratante_"+i)+"");
			
				//PRIMERO SE HACE UN INSERT DE UNA SOLICITUD GENERAL
				try
				{ 
					
					numeroSolicitud= this.insertarSolicitudBasicaTransaccional(con, forma, codigoCuenta, paciente, user, ConstantesBD.codigoTipoSolicitudCargosDirectosServicios, temporalCodigoCentroCostoEjecuta,temporalCodigoEspProfResp);
					forma.setNumeroSolicitudGenerado(numeroSolicitud+"");
					
					if(numeroSolicitud<=0)
					{
						DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar la solicitud básica (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
					}
				}
				catch(SQLException sqle)
				{
					DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar la solicitud básica (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
				}
				
				if(temporalCodigoPool>=0)
			    {    
			        inserto = this.actualizarPoolXSolicitud(con, numeroSolicitud, temporalCodigoPool);
			        if(!inserto)
			    	{
			    	    DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
			    	    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el actualizar el pool x medico (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
			    	}
			    }
				
				// se hace una actulizacion del medico que responde
			    try
			    {
			       inserto = this.actaulizarMedicoRespondeTransaccional(con, numeroSolicitud, temporalCodigoMedicoResponde);
			       if(!inserto)
			    	{
			    	    DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
			    	    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el actualizar medico responde (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
			    	}
			    }
			    catch(SQLException sqle)
			    {
			        DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
			        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el actualizar medico responde (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
			    }
				
				//SE INSERTA EL USUARIO QUE HIZO EL CARGO DIR Y EL TIPO DE RECARGO
				inserto = this.insertarInfoCargosDirectosTransaccional(con, numeroSolicitud, user.getLoginUsuario(), temporalCodigoTipoRecargo, temporalCodigoServicio);
				if(!inserto)
				{
					DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el insertar cargos dir (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
				}
				
				inserto=this.cambiarEstadoMedicoSolicitudTransaccional(con, numeroSolicitud/*, temporalNumeroAutorizacion*/);
			    if(!inserto)
			    {
			        DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
			        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al cambiar el estado medico de la solicitud (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
			    }
				
				
				double valorTarifaOpcional=ConstantesBD.codigoNuncaValidoDouble;
			    
			    //GENERACION DEL CARGO Y SUBCUENTA - EVALUACION COBERTURA 
			    Cargos cargos= new Cargos();
			    inserto= cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	con, 
			    																			user, 
			    																			paciente, 
			    																			false/*dejarPendiente*/, 
			    																			numeroSolicitud, 
			    																			ConstantesBD.codigoTipoSolicitudCargosDirectosServicios /*codigoTipoSolicitudOPCIONAL*/, 
			    																			codigoCuenta, 
			    																			temporalCodigoCentroCostoEjecuta/*codigoCentroCostoEjecutaOPCIONAL*/, 
			    																			temporalCodigoServicio/*codigoServicioOPCIONAL*/, 
			    																			temporalCantidadServicio/*cantidadServicioOPCIONAL*/, 
			    																			valorTarifaOpcional/*valorTarifaOPCIONAL*/, 
			    																			ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
			    																			/* "" -- numeroAutorizacionOPCIONAL*/
			    																			"" /*esPortatil*/,false,"","" /*subCuentaCoberturaOPCIONAL*/);
			   	
			    logger.info("cod det cargo-->"+cargos.getDtoDetalleCargo().getCodigoDetalleCargo());
			    forma.setCodigoDetalleCargo(cargos.getDtoDetalleCargo().getCodigoDetalleCargo()+"");
			    
			    if(!inserto)
			    {
			        DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
			        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al tratar de generar el cargo (CargosDirectosAction)", "error.cargosDirectos.medicamentos", true);
			    }
			    else
			    {
			    	this.validarInsertarJustificacionS(con, user, numeroSolicitud, forma, temporalCodigoServicio, cargos.getDtoDetalleCargo().getCodigoConvenio());
			    }
			    HashMap tempo=mundo.cargarTarifas(con, numeroSolicitud, temporalCodigoServicio);
			    forma.setServiciosAutomaticos("valorunitario_"+i, tempo.get("valorunitario")+"");
			    forma.setServiciosAutomaticos("valortotal_"+i, tempo.get("valortotal")+"");
			}
		}  
		
		Solicitud sol= new Solicitud();
		sol.cargar(con, numeroSolicitud);
		forma.setConsecutivoOrdenMedica(sol.getConsecutivoOrdenesMedicas()+"");
		
		//si llega a este punto entonces se inserta el log
		//this.generarLogTarifasServicios(con, cargosForm, user);
		
		//si todo salio bien entonces 
		this.terminarTransaccion(con);
		//DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("resumen");
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
	private int insertarSolicitudBasicaTransaccional(	Connection con, 
																		CargosAutomaticosPresupuestoForm forma,
																		int codigoCuenta,
																		PersonaBasica paciente,
																		UsuarioBasico user,
																		int tipoSolicitud,
																		int centroCostoSolicitado,
																		int espProfResponde
																	   ) throws SQLException
	{
		Solicitud objectSolicitud= new Solicitud();
		int numeroSolicitudInsertado=0;
		objectSolicitud.clean();
		objectSolicitud.setFechaSolicitud(UtilidadFecha.getFechaActual());
		objectSolicitud.setHoraSolicitud(UtilidadFecha.getHoraActual());
		objectSolicitud.setTipoSolicitud(new InfoDatosInt(tipoSolicitud));
		//objectSolicitud.setNumeroAutorizacion("");
		objectSolicitud.setEspecialidadSolicitadaOrdAmbulatorias(espProfResponde);
		objectSolicitud.setEspecialidadSolicitante(new InfoDatosInt(ConstantesBD.codigoEspecialidadMedicaNinguna));
		objectSolicitud.setOcupacionSolicitado(new InfoDatosInt(ConstantesBD.codigoOcupacionMedicaNinguna));
		
		//objectSolicitud.setCentroCostoSolicitante(new InfoDatosInt(UtilidadValidacion.getCodigoCentroCostoTratante(con,paciente, user.getCodigoInstitucionInt())));
		objectSolicitud.setCentroCostoSolicitante(new InfoDatosInt(paciente.getCodigoArea()));
		
		objectSolicitud.setCentroCostoSolicitado(new InfoDatosInt(centroCostoSolicitado));
		objectSolicitud.setCodigoCuenta(codigoCuenta);
		objectSolicitud.setCobrable(true);
		objectSolicitud.setVaAEpicrisis(false);
		objectSolicitud.setUrgente(false);
		//primero lo inserto como pendiente, pero si mas adelante es exitoso el cargo entonces le hago un update a  cargada
		objectSolicitud.setEstadoHistoriaClinica(new InfoDatosInt(ConstantesBD.codigoEstadoHCCargoDirecto));
		try
		{ 
			numeroSolicitudInsertado=objectSolicitud.insertarSolicitudGeneralTransaccional(con, ConstantesBD.inicioTransaccion);
		}
		
		catch(SQLException sqle)
		{
			logger.warn("Error en la transaccion del insert en la solicitud básica");
			return 0;
		}
		
			return numeroSolicitudInsertado;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoPool
	 * @return
	 * @throws SQLException
	 */
	private boolean actualizarPoolXSolicitud(Connection con, int numeroSolicitud, int codigoPool) throws SQLException
	{
	    Solicitud solciitud= new Solicitud();
	    int resultado= solciitud.actualizarPoolSolicitud(con, numeroSolicitud, codigoPool);
	    if(resultado<=0)
	       return false;
	    else
	        return true;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoMedico
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
	 * @param loginUsuario
	 * @param tipoRecargo
	 * @param codigoServicioSolicitado
	 * @return
	 * @throws SQLException
	 */
	private boolean insertarInfoCargosDirectosTransaccional(Connection con, int numeroSolicitud, String loginUsuario, int tipoRecargo, int codigoServicioSolicitado) throws SQLException
	{
	    CargosDirectos cargo= new CargosDirectos();
	    
	    cargo.llenarMundoCargoDirecto(numeroSolicitud,loginUsuario,tipoRecargo,codigoServicioSolicitado,"",true,"");
	    
	    int resultado=cargo.insertar(con);
	    if(resultado<=0)
	       return false;
	    else
	        return true;
	}
	
	
	/**
	 * 
	 * @param con
	 * @throws SQLException
	 */
	public void terminarTransaccion(Connection con) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    myFactory.endTransaction(con);
	}
	
	
	
	private ActionForward generarReporte(Connection con, CargosAutomaticosPresupuestoForm forma, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuarioActual, PersonaBasica paciente) 
	{
		String nombreRptDesign = "CargosAutomaticosPresupuesto.rptdesign";
		
		InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		
		//PRIMERO SE INSERTA LA INFORMACION DEL CABEZOTE************************************************
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"facturacion/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 0, institucionBasica.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        Vector v=new Vector();
        v.add(institucionBasica.getRazonSocial());
        v.add(institucionBasica.getTipoIdentificacion()+"         "+institucionBasica.getNit());     
        v.add(institucionBasica.getDireccion());
        v.add("Tels. "+institucionBasica.getTelefono());
        comp.insertLabelInGridOfMasterPage(0,1,v);
        comp.insertLabelInGridPpalOfHeader(2,1, "                  Cargos Automaticos Presupuesto.");
        comp.insertLabelInGridPpalOfHeader(2,0, "Fecha Impresion: "+UtilidadFecha.getFechaActual(con)+"   "+UtilidadFecha.getHoraActual(con));
        
        
        //SE MODIFICA LA CONSULTA PARA QUE TOME TODOS LOS CODIGOS DE LOS DETALLES CARGO INSERTADOS
        comp.obtenerComponentesDataSet("SolicitudesGeneradas");            
        String oldQuery=comp.obtenerQueryDataSet();
        String newQuery=comp.obtenerQueryDataSet().replaceAll("-1", UtilidadTexto.convertirVectorACodigosSeparadosXComas(forma.getCodigosDetallesCargo(), false));
        comp.modificarQueryDataSet(newQuery);
        
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
        
        // se mandan los parámetros al reporte
        newPathReport += "&ingreso="+paciente.getCodigoIngreso();
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
        
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("resumen");
	}
	
	
	/**
	 * Metodo que cambia el estado medico de la solicitud en una transaccion
	 * @param con
	 * @param numeroSolicitud
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
	 * Metodo para validar el servicio y el convenio NOPOS e insertar la justificacion pendiente del servicio
	 * @param codigoConvenio 
	 */
	private boolean validarInsertarJustificacionS(Connection con, UsuarioBasico user, int numeroSolicitud, CargosAutomaticosPresupuestoForm forma, int codigoServicio, int codigoConvenio) throws IPSException
	{
		if(UtilidadJustificacionPendienteArtServ.validarNOPOS(con, numeroSolicitud, codigoServicio, false, false, codigoConvenio))
		{
			double subcuenta = Cargos.obtenerCodigoSubcuentaDetalleCargo(con, codigoServicio, numeroSolicitud, codigoConvenio, false);
			if(UtilidadJustificacionPendienteArtServ.insertarJusNP(con, numeroSolicitud, codigoServicio, 1, user.getLoginUsuario(), false, false, Utilidades.convertirAEntero(subcuenta+""),""))
			{
				forma.setJustificacionNoPosMap("mensaje_0", "SERVICIO ["+codigoServicio+"] DE NATURALEZA NO POS REQUIERE DE JUSTIFICACIÓN NO POS.");
			}
		}
		return true;
	}
	
	// Cambios Segun Anexo 809
	private ActionForward accionFiltrarEspecialidProfResponde(Connection con,CargosAutomaticosPresupuestoForm cargosForm,HttpServletResponse response) 
	{
		logger.info(" >>>  ENTRO A FILTRAR ESPECIALIDADES MEDICO RESPONDE ");
		String resultado = "<respuesta>" +
			"<infoid>" +
				"<sufijo>ajaxBusquedaTipoSelect</sufijo>" +
				"<id-select>espMedicoTratante_"+cargosForm.getIndice()+"</id-select>" +
				"<id-arreglo>especialidades</id-arreglo>" +
			"</infoid>" ;
		
		//consultar especialidades medicos
		logger.info("indice >>>>"+cargosForm.getIndice());
		cargosForm.getEspProfResponde().get(cargosForm.getIndice()).clear();
		cargosForm.getEspProfResponde().add(cargosForm.getIndice(),
				UtilidadesOrdenesMedicas.obtenerEspecialidadProfesionalEjecutan(
						Utilidades.convertirAEntero(cargosForm.getCodigoProfResponde())));
		
		/*resultado += "<especialidades>";
		resultado += "<codigo></codigo>";
		resultado += "<descripcion>Seleccione</descripcion>";
		resultado += "</especialidades>";	
		*/
		
		logger.info("tamano array >>>> "+cargosForm.getEspProfResponde().get(cargosForm.getIndice()).size());
		for(int i=0; i< cargosForm.getEspProfResponde().get(cargosForm.getIndice()).size();i++)
		{
			logger.info("valor de la consulta >>>> "+cargosForm.getEspProfResponde().get(cargosForm.getIndice()).get(i).getNombre());
			resultado += "<especialidades>";
			resultado += "<codigo>"+cargosForm.getEspProfResponde().get(cargosForm.getIndice()).get(i).getCodigo()+"</codigo>";
			resultado += "<descripcion>"+cargosForm.getEspProfResponde().get(cargosForm.getIndice()).get(i).getNombre()+"</descripcion>";
		    resultado += "</especialidades>";	
			
		}
		resultado += "</respuesta>";
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.reset();
			response.resetBuffer();
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			response.getWriter().flush();			
	        response.getWriter().write(resultado);
	        response.getWriter().close();
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX en accionFiltrarUnidadesAgenda: "+e);
		}
		return null;
	}
	// Fin Cambios Segun Anexo 809

}
