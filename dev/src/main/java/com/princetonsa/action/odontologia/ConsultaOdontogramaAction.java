package com.princetonsa.action.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadLog;
import util.ValoresPorDefecto;
import util.Administracion.UtilidadesAdministracion;
import util.odontologia.InfoDetallePlanTramiento;
import util.odontologia.InfoPlanTratamiento;

import com.mercury.mundo.odontologia.Odontograma;
import com.princetonsa.actionform.odontologia.ConsultaOdontogramaForm;
import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.administracion.DtoUsuario;
import com.princetonsa.dto.odontologia.DtoCitaOdontologica;
import com.princetonsa.dto.odontologia.DtoOdontograma;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoOdo;
import com.princetonsa.dto.odontologia.DtoPresupuestoOdontologico;
import com.princetonsa.enu.general.CarpetasArchivos;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.odontologia.CitaOdontologica;
import com.princetonsa.mundo.odontologia.OdontogramaMundo;
import com.princetonsa.mundo.odontologia.PlanTratamiento;
import com.princetonsa.mundo.odontologia.PresupuestoOdontologico;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * Action para la consulta de odontograma
 * 
 * @author Ronald Angel
 *
 * Apr 27, 2010 - 4:00:51 PM
 */
public class ConsultaOdontogramaAction extends Action 
{
	/**
	 * logger del action
	 */
	private Logger logger = Logger.getLogger(ConsultaOdontogramaAction.class);

	@Override
	public ActionForward execute(	ActionMapping mapping, 
									ActionForm form,
									HttpServletRequest request, 
									HttpServletResponse response)throws Exception 
	{
		if (form instanceof ConsultaOdontogramaForm ) 
		{
			ConsultaOdontogramaForm  forma = (ConsultaOdontogramaForm) form;
			logger.info("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxestado-->" + forma.getEstado());
			UsuarioBasico usuario = (UsuarioBasico) request.getSession().getAttribute("usuarioBasico");
			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
			InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			
			
			ActionForward validaciones= validarPaciente(mapping, request, paciente, usuario);
			if(validaciones!=null)
			{	
				return validaciones;
			}	
			if (forma.getEstado().equals("empezar")) 
			{
				return accionEmpezar(mapping, forma, usuario,paciente);
			}
			else if (forma.getEstado().equals("detalle")) 
			{
				return accionDetalle(mapping, forma, usuario);
			}
			else if (forma.getEstado().equals("mostrar")) 
			{
				return accionMostrar(mapping, forma, usuario , paciente,request);
			}
			
			else if(forma.getEstado().equals("consultaPlanTratamientoEvolucion"))
			{
			
				accionCargarDetallePlantratamiento(forma, usuario, paciente);
				return mapping.findForward("paginaDetallePlan");
			}
			else if(forma.getEstado().equals("imprimirOdontograma"))
			{
				accionCargarImpresionOdontograma(institucionBasica, paciente, usuario , forma) ;
				return mapping.findForward("imprimirPlan"); 
			}
		}
		return null;
	}

	
	/**
	 * ACCION CARGAR INFORMACION RESPECTIVA DE LA IMPRESION DEL ODONTOGRAMA 
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param dtoPlan
	 * @param institucionBasica
	 * @param paciente
	 * @param usuario
	 */
	private void accionCargarImpresionOdontograma(	InstitucionBasica institucionBasica,
													PersonaBasica paciente, UsuarioBasico usuario,
													ConsultaOdontogramaForm forma) 
	
	{
		
		DtoPlanTratamientoOdo dtoPlan = new DtoPlanTratamientoOdo();
		
		dtoPlan.setIngreso(forma.getDtoPlanTratamiento().getIngreso());
		dtoPlan.setFechaGrabacion(forma.getDtoPlanTratamiento().getFechaGrabacion());
		dtoPlan.setHoraGrabacion(forma.getDtoPlanTratamiento().getHoraGrabacion());

		
		forma.setInfoEncabezadoReporte(OdontogramaMundo.cargarInformacionEncabezadoReporte(dtoPlan , institucionBasica, paciente, usuario));
		DtoUsuario dtoUsu= new DtoUsuario();
		dtoUsu.setLogin(usuario.getLoginUsuario());
		DtoPersonas dtoPersona = new DtoPersonas();
		dtoPersona=UtilidadesAdministracion.cargarPersona(dtoUsu);
		forma.setUsuarioModifica(dtoPersona.getPrimerApellido()+" "+dtoPersona.getSegundoApellido()+ " "+dtoPersona.getPrimerNombre()+" "+dtoPersona.getSegundoNombre());
		
	}


	
	/**
	 * ACCION CARGAR DETALLE PLANTRATAMIENTO 
	 * @author Edgar Carvajal Ruiz
	 * @param forma
	 * @param usuario
	 * @param paciente
	 */
	private void accionCargarDetallePlantratamiento(
													ConsultaOdontogramaForm forma,
													UsuarioBasico usuario,
													PersonaBasica paciente) 
	
	{
	
		InfoPlanTratamiento dtoInfoPlanTratamiento = PlanTratamiento.accionCargarOdontogramaHistoricoPlanTratamiento(forma.getDtoOdontograma(), paciente.getCodigoIngreso(), Boolean.TRUE , usuario.getCodigoInstitucionInt(), Boolean.TRUE );
		
		if(dtoInfoPlanTratamiento!=null)
		{	
			forma.setDtoPlanTratamiento(dtoInfoPlanTratamiento);
			
			for (InfoDetallePlanTramiento dto: forma.getDtoPlanTratamiento().getSeccionHallazgosDetalle())
			{
				dto.getCodigoPkDetalle();
			}
		}
	}

	/**
	 * 
	 * Metodo para validar que exista un paciente cargado en session
	 * @param mapping
	 * @param request
	 * @param paciente
	 * @param usuario
	 * @return
	 * @author   Ronald Angel
	 * @version  1.0.0 
	 * @see
	 */
	private ActionForward validarPaciente(	ActionMapping mapping,
											HttpServletRequest request, 
											PersonaBasica paciente,
											UsuarioBasico usuario) 
	{
		Connection con = UtilidadBD.abrirConexion();
		if(paciente == null|| paciente.getCodigoTipoIdentificacionPersona().equals(""))
		{
			logger.warn("Paciente no válido (null)");			
			request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaError");
		}
		UtilidadBD.closeConnection(con);
		return null;
	}
	
	/**
	 * 
	 * Metodo que ejecuta la accion empezar, carga la jsp menuConsultaOdontograma.jsp, el listado de ingresos
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @return
	 * @author   Ronald Angel
	 * @version  1.0.0 
	 * @see
	 */
	private ActionForward accionEmpezar(	ActionMapping mapping,
											ConsultaOdontogramaForm forma, 
											UsuarioBasico usuario , 
											PersonaBasica paciente) 
	{
		forma.reset();
		forma.setTipoRelacion(ValoresPorDefecto.getUtilizanProgramasOdontologicosEnInstitucion(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi)?"Programa":"Servicio");
		forma.setArrayIngresos(Odontograma.cargarIngresos(paciente.getCodigoPersona(), ConstantesBD.codigoViaIngresoConsultaExterna, usuario.getCodigoInstitucionInt()));
		return mapping.findForward("paginaPrincipal");
	}
	
	/**
	 * 
	 * Metodo que seleccionado el ingreso lista los odontogramas ligados a ese ingreso, 
	 * No.  	 Fecha  	 Hora  	 Tipo  	 Profesional  	 Especialidad Profesional .
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @return
	 * @author   Ronald Angel
	 * @version  1.0.0 
	 * @see
	 */
	private ActionForward accionDetalle(	ActionMapping mapping,
											ConsultaOdontogramaForm forma, 
											UsuarioBasico usuario) 
	{
		DtoOdontograma dtoWhere = new DtoOdontograma();
		dtoWhere.setIngreso(new InfoDatosInt(Integer.parseInt(forma.getArrayIngresos().get(forma.getPosArray()).getIngreso()), ""));
		dtoWhere.setInstitucion(usuario.getCodigoInstitucionInt());
		dtoWhere.getIngreso().setCodigo2(ConstantesBD.codigoViaIngresoConsultaExterna);
		forma.setArrayOdontogramas(Odontograma.cargar(dtoWhere));
		for(DtoOdontograma odo : forma.getArrayOdontogramas())
		{
			odo.setEspecialidadesMedico(UtilidadesAdministracion.obtenerEspecialidadesMedicoPorLogin(odo.getUsuarioModifica().getUsuarioModifica()));
		}
		return mapping.findForward("paginaDetalle");
	}
	
	/**
	 * 
	 * Metodo para mostrar la imagen de odontograma y su respectiva informacion
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @return
	 * @author   Ronald Angel
	 * @version  1.0.0 
	 * @see
	 */
	private ActionForward accionMostrar(	ActionMapping mapping,
											ConsultaOdontogramaForm forma, 
											UsuarioBasico usuario , 
											PersonaBasica paciente , 
											HttpServletRequest request) 
	{
		forma.setDtoOdontograma(forma.getArrayOdontogramas().get(forma.getPosArrayDetalle()));
		ActionForward forward= cargarPlanTratamiento(forma, paciente, request, mapping ,usuario );
		if(forward!=null)
		{
			return forward;
		}
		//cargarConveniosContratoPresupuesto(forma, paciente);
		return mapping.findForward("paginaDetalle");
	}

	/**
	 * 
	 * Metodo para cargar el plan de tratamiento ligado al odontograma seleccionado
	 * @param forma
	 * @param paciente
	 * @param request
	 * @param mapping
	 * @return
	 * @author   Ronald Angel
	 * @version  1.0.0 
	 * @see
	 */
	private ActionForward cargarPlanTratamiento(	ConsultaOdontogramaForm forma,
													PersonaBasica paciente, 
													HttpServletRequest request, 
													ActionMapping mapping,
													UsuarioBasico usuario) 
	{
		
	
		
		//TODO DEFINIR EL PARAMETRO GENERAL MANEJA PROGRAMAS Y  SERVICIOS 
		
		InfoPlanTratamiento dtoInfoPlanTratamiento = PlanTratamiento.accionCargarOdontogramaHistoricoPlanTratamiento(forma.getDtoOdontograma(), paciente.getCodigoIngreso(), Boolean.TRUE , usuario.getCodigoInstitucionInt() , Boolean.FALSE );
		
		
		
		if(dtoInfoPlanTratamiento!=null)
		{	
			/*
			 * Settear informacion Dto PLAN TRATAMIENTO
			 */
			forma.setDtoPlanTratamiento(dtoInfoPlanTratamiento);
			
			
			/*
			 * CARGAR LOS PROGRAMA SERVICIOS PROXIMA CITA
			 */
			accionCargarProgramaServicioProximaCita(forma, usuario,	dtoInfoPlanTratamiento);
		
	
			
			/*
			 * CARGAR INFORMACION PROFESIONAL DE LA SALUD 
			 */
			accionCargarProfesionalSalud(forma);
		}
		else
		{
			
			logger.warn("NO EXISTE PLAN TRATAMIENTO INICIAL");
			ActionErrors errores = new ActionErrors(); 
			errores.add("", new ActionMessage("error.errorEnBlanco", "Paciente no tiene Plan de Tratamiento Activo"));
			saveErrors(request, errores);
			return mapping.findForward("paginaErroresActionErrors");
		
		}
	
		return null; 
	}

	

	/**
	 * ACCION CARGAR PROGRAMAS SERVICIOS PROXIMA CITA 
	 * @author Edgar Carvajal Ruiz
	 * @param forma
	 * @param usuario
	 * @param dtoInfoPlanTratamiento
	 */
	private void accionCargarProgramaServicioProximaCita(
			ConsultaOdontogramaForm forma, UsuarioBasico usuario,
			InfoPlanTratamiento dtoInfoPlanTratamiento) 
	
	{
		DtoPlanTratamientoOdo dtoPlan = new DtoPlanTratamientoOdo();
		dtoPlan.setCodigoPk(dtoInfoPlanTratamiento.getCodigoPk());
		dtoPlan.setCodigoEvolucion(new BigDecimal(forma.getDtoOdontograma().getEvolucion()));
		DtoCitaOdontologica dtoCita= CitaOdontologica.cargarCitaXEvolucionPlanTratamiento(dtoPlan);
		forma.setFechaCita(dtoCita.getFechaProgramacion()); // TODO VALIDAR ESTA FECHA 
		forma.setListaProgramasProximaCita(PlanTratamiento.consultarProximaCitaProgramasServicios(forma.getDtoOdontograma(), dtoPlan, usuario.getCodigoInstitucionInt()));
	}


	
	
	/**
	 * ACCION CARGAR PROFESIONAL DE LA SALUD
	 * @author Edgar Carvajal Ruiz
	 * @param forma
	 */
	private void accionCargarProfesionalSalud(ConsultaOdontogramaForm forma)
	{
		
		DtoUsuario dtoUsuario = new DtoUsuario();
		dtoUsuario.setLogin(forma.getDtoOdontograma().getUsuarioModifica().getUsuarioModifica());
		DtoPersonas dtoPersona= UtilidadesAdministracion.cargarPersona(dtoUsuario);
		forma.setNombreCompletoProfesional( dtoPersona.getPrimerNombre()+" "+dtoPersona.getSegundoNombre()+" "+dtoPersona.getPrimerApellido()+" "+dtoPersona.getSegundoApellido() );
		forma.setListaEspecialidadesProfesional(UtilidadesAdministracion.obtenerEspecialidadesMedicoPorLogin(forma.getDtoOdontograma().getUsuarioModifica().getUsuarioModifica()));
	}
	
	
	/**
	 * 
	 * Metodo para hacer la impresion de odontograma evolucion
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @return
	 * @author   Ronald Angel
	 * @version  1.0.0 
	 * @see}
	 *@deprecated
	 */
	@SuppressWarnings("unchecked")
	
	private ActionForward accionImprimirOdontogramaEvolucion(	ActionMapping mapping, 
																ConsultaOdontogramaForm forma,
																UsuarioBasico usuario, 
																PersonaBasica paciente,
																HttpServletRequest request) 
	{
		String nombreRptDesign = "OdontogramaEvolucion.rptdesign";	  

		logger.info("EL INDICATIVO ------------------------------>"+forma.getArrayOdontogramas().get(forma.getPosArrayDetalle()).getIndicativo() +"-"+forma.getPosArrayDetalle());
		logger.info(UtilidadLog.obtenerString(forma.getArrayOdontogramas().get(forma.getPosArrayDetalle()), true));
		//Parametros Generacion reporte
		String sqlDetalle=Odontograma.retornarConsultaOdontograma(forma.getArrayOdontogramas().get(forma.getPosArrayDetalle()).getCodigoPk(), forma.getArrayOdontogramas().get(forma.getPosArrayDetalle()).getIndicativo(), forma.getArrayOdontogramas().get(forma.getPosArrayDetalle()).getIngreso().getCodigo(), ConstantesIntegridadDominio.acronimoDetalle , false , false);
		String sqlOtros=Odontograma.retornarConsultaOdontograma(forma.getArrayOdontogramas().get(forma.getPosArrayDetalle()).getCodigoPk(), forma.getArrayOdontogramas().get(forma.getPosArrayDetalle()).getIndicativo(), forma.getArrayOdontogramas().get(forma.getPosArrayDetalle()).getIngreso().getCodigo(), ConstantesIntegridadDominio.acronimoOtro , false , false);
		String sqlBoca=Odontograma.retornarConsultaOdontograma(forma.getArrayOdontogramas().get(forma.getPosArrayDetalle()).getCodigoPk(), forma.getArrayOdontogramas().get(forma.getPosArrayDetalle()).getIndicativo(), forma.getArrayOdontogramas().get(forma.getPosArrayDetalle()).getIngreso().getCodigo(), ConstantesIntegridadDominio.acronimoBoca , false , false);
		String sqlGarantia=Odontograma.retornarConsultaOdontograma(forma.getArrayOdontogramas().get(forma.getPosArrayDetalle()).getCodigoPk(), forma.getArrayOdontogramas().get(forma.getPosArrayDetalle()).getIndicativo(), forma.getArrayOdontogramas().get(forma.getPosArrayDetalle()).getIngreso().getCodigo(), "" , false , true);
		String sqlInclusion=Odontograma.retornarConsultaOdontograma(forma.getArrayOdontogramas().get(forma.getPosArrayDetalle()).getCodigoPk(), forma.getArrayOdontogramas().get(forma.getPosArrayDetalle()).getIndicativo(), forma.getArrayOdontogramas().get(forma.getPosArrayDetalle()).getIngreso().getCodigo(), "" , true , false);

		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica"); 
		DesignEngineApi comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"odontologia/",nombreRptDesign);
		// Inserta Logo
		comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
		//Crea una grilla en la posicion 0,1 con una fila y 4 columnas
		comp.insertGridHeaderOfMasterPage(0,1,1,4);
		Vector v=new Vector();
		v.add("HOSPITAL:" + ins.getRazonSocial());
		v.add("NIT:" + ins.getNit());
		v.add("ODONTOGRAMA DE EVOLUCIÓN Y PLAN DE TRATAMIENTO");
		comp.insertLabelInGridOfMasterPage(0,1,v);

		//************************
		comp.insertGridHeaderOfMasterPageWithName(1,0,1,2, "datos odontograma2");
		Vector v4=new Vector();
		v4.add("PACIENTE:"+paciente.getNombrePersona());        
		v4.add("CENTRO DE ATENCIÓN:" + usuario.getCentroAtencion());
		comp.insertLabelInGridOfMasterPage(1,0,v4);

		//*******************************
		comp.insertGridHeaderOfMasterPageWithName(1,1,1,2, "datos odontograma3");
		Vector v5=new Vector();
		v5.add("IDENTIFICACIÓN:"+paciente.getTipoIdentificacionPersona(false) + "  -  " +paciente.getNumeroIdentificacionPersona());        
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			v5.add("FECHA DE REGISTRO: " + PersonaBasica.getFechaHoraApertura(con, paciente.getCodigoCuenta()));
			UtilidadBD.cerrarConexion(con);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}

		comp.insertLabelInGridOfMasterPage(1,1,v5);

		//***************************************

		comp.insertGridHeaderOfMasterPageWithName(1,2,1,2, "datos odontograma4");
		Vector v6=new Vector();
		v6.add("EDAD:"+paciente.getEdad() + "  años");        
		v6.add("SEXO:" + paciente.getSexo());
		comp.insertLabelInGridOfMasterPage(1,2,v6);        
		//Cargar presupuesto

		DtoPresupuestoOdontologico dtoPresupuesto = new DtoPresupuestoOdontologico();
		dtoPresupuesto = PresupuestoOdontologico.cargarPresupuestoContratado(new BigDecimal(paciente.getCodigoIngreso()));

		if (dtoPresupuesto != null)
		{
			comp.insertGridHeaderOfMasterPageWithName(2,0,1,1, "datos p1");
			Vector vp=new Vector();
			vp.add("DATOS PRESUPUESTO:   "+"\n"+ "Contratado : SI" );
			comp.insertLabelInGridOfMasterPage(2,0,vp);

			//*******************************
			comp.insertGridHeaderOfMasterPageWithName(2,1,1,1, "datos p2");
			Vector vp2=new Vector();
			vp2.add("Número de Presupuesto:"+ dtoPresupuesto.getConsecutivo());        
			comp.insertLabelInGridOfMasterPage(2,1,vp2);

			comp.insertGridHeaderOfMasterPageWithName(2,2,1,1, "datos p3");
			Vector vp3=new Vector();
			vp3.add("Fecha de Contrato:"+ dtoPresupuesto.getFechaUsuarioGenera().getFechaModifica());        
			comp.insertLabelInGridOfMasterPage(2,2,vp3);
		}

		// Cargar Plan de Tratamiento
		DtoPlanTratamientoOdo dtoPlan = new DtoPlanTratamientoOdo();
		ArrayList<String> estados = new ArrayList<String>();
		estados.add(ConstantesIntegridadDominio.acronimoEstadoActivo);

		dtoPlan.setCodigoPk(PlanTratamiento.obtenerUltimoCodigoPlanTratamiento(paciente.getCodigoIngreso(),  estados, ConstantesBD.acronimoNo /*porConfirma*/));
		dtoPlan = PlanTratamiento.consultarPlanTratamiento(dtoPlan);

		comp.insertGridHeaderOfMasterPageWithName(3,0,1,1, "datos t1");
		Vector vt1=new Vector();

		vt1.add("DATOS PLAN DE TRATAMIENTO:"+ "\n"+ "  estado :"+ValoresPorDefecto.getIntegridadDominio(dtoPlan.getEstado()));        
		comp.insertLabelInGridOfMasterPage(3,0,vt1);
		comp.insertGridHeaderOfMasterPageWithName(3,1,1,1, "datos t2");
		Vector vt2=new Vector();
		vt2.add("Fecha Grabación:"+ dtoPlan.getFechaGrabacion());        
		comp.insertLabelInGridOfMasterPage(3,1,vt2);

		comp.insertGridHeaderOfMasterPageWithName(3,2,1,1, "datos t3");
		Vector vt3=new Vector();
		vt3.add("Fecha Ultima Evolución:"+ dtoPlan.getUsuarioModifica().getFechaModifica());        
		comp.insertLabelInGridOfMasterPage(3,2,vt3);
		comp.insertLabelInGridPpalOfHeader(4,0, "ODONTOGRAMA DE EVOLUCIÓN");
		comp.insertImageBodyPage(0, 0 , ValoresPorDefecto.getDirectorioImagenes()+forma.getDtoOdontograma().getImagen() , "imagenOdontograma");
		comp.obtenerComponentesDataSet("consultaDetalle") ;    
		comp.modificarQueryDataSet(sqlDetalle);
		comp.obtenerComponentesDataSet("consultaOtros") ;    
		comp.modificarQueryDataSet(sqlOtros);
		comp.obtenerComponentesDataSet("consultaBoca") ;    
		comp.modificarQueryDataSet(sqlBoca);
		comp.obtenerComponentesDataSet("consultaGarantias") ;    
		comp.modificarQueryDataSet(sqlGarantia);
		comp.obtenerComponentesDataSet("consultaIncluiones") ;    
		comp.modificarQueryDataSet(sqlInclusion);
		//debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
		comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
		comp.updateJDBCParameters(newPathReport);

		if(!newPathReport.equals(""))
		{
			request.setAttribute("isOpenReport", "true");
			request.setAttribute("newPathReport", newPathReport);
		}

		forma.setEstado("mostrar");
		return mapping.findForward("paginaDetalle");
	}

	/**
	 * 
	 * Metodo para imprimir el odontograma de valoracion inicial
	 * @param mapping
	 * @param forma
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @return
	 * @author   Ronald Angel
	 * @version  1.0.0 
	 * @see
	 * @deprecated
	 */
	@SuppressWarnings({ "unchecked"})
	private ActionForward accionImprimirOdontograma(	ActionMapping mapping,
														ConsultaOdontogramaForm forma, 
														UsuarioBasico usuario,
														PersonaBasica paciente, 
														HttpServletRequest request) 
	{
		String nombreRptDesign = "odontogramaPlan.rptdesign";	   	             

		logger.info("EL INDICATIVO ------------------------------>"+forma.getArrayOdontogramas().get(forma.getPosArrayDetalle()).getIndicativo() +"-"+forma.getPosArrayDetalle());
		logger.info(UtilidadLog.obtenerString(forma.getArrayOdontogramas().get(forma.getPosArrayDetalle()), true));
		String sqlDetalle=Odontograma.retornarConsultaOdontograma(forma.getArrayOdontogramas().get(forma.getPosArrayDetalle()).getCodigoPk(), forma.getArrayOdontogramas().get(forma.getPosArrayDetalle()).getIndicativo(), forma.getArrayOdontogramas().get(forma.getPosArrayDetalle()).getIngreso().getCodigo(), ConstantesIntegridadDominio.acronimoDetalle , false , false);
		String sqlOtros=Odontograma.retornarConsultaOdontograma(forma.getArrayOdontogramas().get(forma.getPosArrayDetalle()).getCodigoPk(), forma.getArrayOdontogramas().get(forma.getPosArrayDetalle()).getIndicativo(), forma.getArrayOdontogramas().get(forma.getPosArrayDetalle()).getIngreso().getCodigo(), ConstantesIntegridadDominio.acronimoOtro , false , false);
		String sqlBoca=Odontograma.retornarConsultaOdontograma(forma.getArrayOdontogramas().get(forma.getPosArrayDetalle()).getCodigoPk(), forma.getArrayOdontogramas().get(forma.getPosArrayDetalle()).getIndicativo(), forma.getArrayOdontogramas().get(forma.getPosArrayDetalle()).getIngreso().getCodigo(), ConstantesIntegridadDominio.acronimoBoca , false , false);
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica"); 
		DesignEngineApi comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"odontologia/",nombreRptDesign);

		// Inserta Logo
		comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());

		//Crea una grilla en la posicion 0,1 con una fila y 4 columnas
		comp.insertGridHeaderOfMasterPage(0,1,1,4);
		Vector v=new Vector();
		v.add("HOSPITAL:" + ins.getRazonSocial());
		v.add("NIT:" + ins.getNit());
		v.add("ODONTOGRAMA");
		comp.insertLabelInGridOfMasterPage(0,1,v);

		comp.insertGridHeaderOfMasterPageWithName(1,0,1,2, "datos odontograma2");
		Vector v4=new Vector();
		v4.add("PACIENTE:"+paciente.getNombrePersona());        
		v4.add("CENTRO DE ATENCIÓN:" + usuario.getCentroAtencion());
		comp.insertLabelInGridOfMasterPage(1,0,v4);

		comp.insertGridHeaderOfMasterPageWithName(1,1,1,2, "datos odontograma3");
		Vector v5=new Vector();
		v5.add("IDENTIFICACIÓN:"+paciente.getTipoIdentificacionPersona(false) + "  -  " +paciente.getNumeroIdentificacionPersona());        
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			v5.add("FECHA DE REGISTRO: " + PersonaBasica.getFechaHoraApertura(con, paciente.getCodigoCuenta()));
			UtilidadBD.cerrarConexion(con);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		comp.insertLabelInGridOfMasterPage(1,1,v5);

		//***************************************
		comp.insertGridHeaderOfMasterPageWithName(1,2,1,2, "datos odontograma4");
		Vector v6=new Vector();
		v6.add("EDAD:"+paciente.getEdad() + "  años");        
		v6.add("SEXO:" + paciente.getSexo());
		comp.insertLabelInGridOfMasterPage(1,2,v6);        

		comp.insertLabelInGridPpalOfHeader(2,0, "ODONTOGRAMA DE DIANOSTICO");
		comp.insertImageBodyPage(0, 0 , CarpetasArchivos.IMAGENES_ODONTODX.getRutaFisica()+System.getProperty("file.separator")+forma.getDtoOdontograma().getImagen() , "imagenOdontograma");
		comp.obtenerComponentesDataSet("consultaDetalle") ;    
		comp.modificarQueryDataSet(sqlDetalle);
		comp.obtenerComponentesDataSet("consultaOtros") ;    
		comp.modificarQueryDataSet(sqlOtros);
		comp.obtenerComponentesDataSet("consultaBoca") ;    
		comp.modificarQueryDataSet(sqlBoca);
		comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
		comp.updateJDBCParameters(newPathReport);

		if(!newPathReport.equals(""))
		{
			request.setAttribute("isOpenReport", "true");
			request.setAttribute("newPathReport", newPathReport);
		}

		forma.setEstado("mostrar");
		return mapping.findForward("paginaDetalle");
	}
	
	
	
	/**
	 * 
	 * @author axioma
	 * @return
	 */
	private ActionForward imprimirOdontogramaHistorico(ActionMapping mapping ,
														ConsultaOdontogramaForm forma, UsuarioBasico usuario, 
														PersonaBasica paciente,
														HttpServletRequest request )
	
	{
		
		
		
		
		
		
		//NOMBRE REPORTE
		String nombreRptDesign = "PlanTratamientoOdontogramaEvolucion.rptdesign";
		
        //Parametros Generacion reporte
        String parame="";  
        
        //DIRECCIONE DE IMAGENES 
        String directorioImagenes = System.getProperty("directorioImagenes");
	    
       // ARMANDO EL DTO PLAN DE TRATAMIENTO
	    
        DtoPlanTratamientoOdo dtoPlanTratamiento = new DtoPlanTratamientoOdo();
	    dtoPlanTratamiento.setCodigoPlanHistorico(forma.getDtoPlanTratamiento().getCodigoPk());
	    dtoPlanTratamiento.setIngreso(forma.getDtoPlanTratamiento().getIngreso() );
	    dtoPlanTratamiento.setEstado(forma.getDtoPlanTratamiento().getEstado());
	     
	   
	    //Cargar las consultas
	    
	    String sqlDetalle=Odontograma.retornarConsultaOdontograma(dtoPlanTratamiento, ConstantesIntegridadDominio.acronimoDetalle,null  /*TIPOS GARANTIA o INCLUSION*/ );
	    String sqlOtros=Odontograma.retornarConsultaOdontograma(dtoPlanTratamiento, ConstantesIntegridadDominio.acronimoOtro, null /*TIPOS GARANTIA O INCLUSION */ );
	    String sqlBoca=Odontograma.retornarConsultaOdontograma(dtoPlanTratamiento, ConstantesIntegridadDominio.acronimoBoca, null /*TIPOS GARANTIA O INCLUSION */);
	    String sqlGarantia=Odontograma.retornarConsultaOdontograma(dtoPlanTratamiento, null/* SECCION*/, ConstantesIntegridadDominio.acronimoGarantia);
	    String sqlInclusion=Odontograma.retornarConsultaOdontograma(dtoPlanTratamiento, null/* SECCION*/, ConstantesIntegridadDominio.acronimoInclusion);
	    String sqlInclusionBoca= Odontograma.retornarConsultaOdontograma(dtoPlanTratamiento,ConstantesIntegridadDominio.acronimoBoca, ConstantesIntegridadDominio.acronimoInclusion);
	        
      
       		
         // CARGAR LA INSTITUCION BASICA      
        InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica"); 
        DesignEngineApi comp; 
  
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"odontologia/",nombreRptDesign);

        //CARGAR UBICACION DEL LOG DEPENDIENDO PARAMETRO GENERAL
        if(ins.getUbicacionLogo().equals(ConstantesIntegridadDominio.acronimoUbicacionIzquierda))
        	comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        else
        	comp.insertImageHeaderOfMasterPage1(0, 2, ins.getLogoReportes());
        
        //Crea una grilla en la posicion 0,1 con una fila y 4 columnas
        comp.insertGridHeaderOfMasterPage(0,1,1,4);
        comp.insertLabelInGridPpalOfFooter(0,0,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
	    comp.insertLabelInGridPpalOfFooter(0,1,"Usuario: "+usuario.getLoginUsuario());
        
        //ARMAR INFORMACION BASICA DEL REPORTE
        Vector v=new Vector();
        v.add("HOSPITAL :" + ins.getRazonSocial());
        v.add("NIT :" + ins.getNit());
        v.add("ODONTOGRAMA DE EVOLUCIÓN Y PLAN DE TRATAMIENTO");
        
        comp.insertLabelInGridOfMasterPage(0,1,v);
        comp.insertGridHeaderOfMasterPageWithName(1,0,1,2, "datos odontograma2");
        Vector v4=new Vector();
        v4.add("PACIENTE:"+paciente.getNombrePersona());        
        v4.add("CENTRO DE ATENCIÓN:" + usuario.getCentroAtencion());
        
        comp.insertLabelInGridOfMasterPage(1,0,v4);
        comp.insertGridHeaderOfMasterPageWithName(1,1,1,2, "datos odontograma3");
        Vector v5=new Vector();
        v5.add("IDENTIFICACIÓN:"+paciente.getTipoIdentificacionPersona(false) + "  -  " +paciente.getNumeroIdentificacionPersona());        
        comp.insertLabelInGridOfMasterPage(1,1,v5);
        comp.insertGridHeaderOfMasterPageWithName(1,2,1,2, "datos odontograma4");
        Vector v6=new Vector();
        v6.add("EDAD:"+paciente.getEdad() + "  años");        
        v6.add("SEXO:" + paciente.getSexo());
        comp.insertLabelInGridOfMasterPage(1,2,v6);        
        
        
        	        	       
        //Cargar presupuesto
        DtoPresupuestoOdontologico dtoPresupuesto = new DtoPresupuestoOdontologico();
        dtoPresupuesto = PresupuestoOdontologico.cargarPresupuestoContratado(new BigDecimal(paciente.getCodigoIngreso()));
        if ( dtoPresupuesto != null)
        {	
	        comp.insertGridHeaderOfMasterPageWithName(2,0,1,1, "datos p1");
	        if(ValoresPorDefecto.getValidaPresupuestoOdoContratado(usuario.getCodigoInstitucionInt()).equals(ConstantesBD.acronimoSi))
	        {	
	            Vector vp=new Vector();
		        vp.add("DATOS PRESUPUESTO:   "+"\n"+ "Contratado : SI" );
		        comp.insertLabelInGridOfMasterPage(2,0,vp);
		        //*******************************
		        comp.insertGridHeaderOfMasterPageWithName(2,1,1,1, "datos p2");
		        Vector vp2=new Vector();
		        vp2.add("Número de Presupuesto:"+ dtoPresupuesto.getConsecutivo());        
		        comp.insertLabelInGridOfMasterPage(2,1,vp2);
		        comp.insertGridHeaderOfMasterPageWithName(2,2,1,1, "datos p3");
		        Vector vp3=new Vector();
		        vp3.add("Fecha de Contrato:"+ dtoPresupuesto.getFechaUsuarioGenera().getFechaModifica());        
		        comp.insertLabelInGridOfMasterPage(2,2,vp3);
	        
	        //*********************************
	        }
        }
        
        // Cargar Plan de Tratamiento
       // DtoPlanTratamientoOdo dtoPlan = new DtoPlanTratamientoOdo();
       // ArrayList<String> estados = new ArrayList<String>();
       // estados.add(ConstantesIntegridadDominio.acronimoEstadoActivo);
        
        // no aplica 
       // dtoPlan.setCodigoPk(PlanTratamiento.obtenerUltimoCodigoPlanTratamiento(paciente.getCodigoIngreso(),  estados, "" /*porConfirmar*/)	);
        //dtoPlan = forma.getDtoPlanTratamiento();
        
        
        comp.insertGridHeaderOfMasterPageWithName(3,0,1,1, "datos t1");
        Vector vt1=new Vector();
        
        vt1.add("DATOS PLAN DE TRATAMIENTO:"+ "\n"+ "  estado :"+ValoresPorDefecto.getIntegridadDominio(dtoPlanTratamiento.getEstado()));        
        //*************
        comp.insertLabelInGridOfMasterPage(3,0,vt1);
        comp.insertGridHeaderOfMasterPageWithName(3,1,1,1, "datos t2");
        Vector vt2=new Vector();
        vt2.add("Fecha Grabación:"+ UtilidadFecha.conversionFormatoFechaAAp(dtoPlanTratamiento.getFechaGrabacion()));        
        comp.insertLabelInGridOfMasterPage(3,1,vt2);
        comp.insertGridHeaderOfMasterPageWithName(3,2,1,1, "datos t3");
        Vector vt3=new Vector();
        vt3.add("Fecha Ultima Evolución:"+  UtilidadFecha.conversionFormatoFechaAAp(dtoPlanTratamiento.getUsuarioModifica().getFechaModifica()));        
        comp.insertLabelInGridOfMasterPage(3,2,vt3);
        comp.insertLabelInGridPpalOfHeader(4,0, "ODONTOGRAMA DE EVOLUCIÓN");
       
        
        
        /*
         * ODONTOGRAMA DE DIAGNOSTICO
         */
        DtoOdontograma dtoOdontograma = new DtoOdontograma();
        logger.info(" Odontograma de Diagnostico="+dtoPlanTratamiento.getOdontogramaDiagnostico());
        ArrayList<DtoOdontograma> tmpOdontogramaDiagnostico = new ArrayList<DtoOdontograma>();
        
         
        /*
         * SI E PLAN DE TRATAMIENTO ESTA ACTIVO CARGAR EL ODONTOGRAMA DE DIAGNOSTICO 
         */
        
        //CARGA LA IMAGENE DEL ODONTOLOGRAMA DE DIAGNOSTICO
        if(forma.getDtoPlanTratamiento().getEstado().equals(ConstantesIntegridadDominio.acronimoEstadoActivo))
        {
        	dtoOdontograma.setCodigoPk(forma.getDtoOdontograma().getCodigoPk());
	        dtoOdontograma.setIndicativo(ConstantesIntegridadDominio.acronimoOdontogramaDiagnostico);
        	tmpOdontogramaDiagnostico= Odontograma.cargar(dtoOdontograma);
        	if (tmpOdontogramaDiagnostico.size() > 0)
 	        {	
 	        	dtoOdontograma = tmpOdontogramaDiagnostico.get(0);
 	        	logger.info("Imagen de Odontolograma -->"+CarpetasArchivos.IMAGENES_ODONTODX.getRutaFisica()+System.getProperty("file.separator")+dtoOdontograma.getImagen());
 	        	comp.insertImageBodyPage(0, 0 , CarpetasArchivos.IMAGENES_ODONTODX.getRutaFisica()+System.getProperty("file.separator")+dtoOdontograma.getImagen() , "imagenOdon");
 	        }
	        
        }
        // CARGA LA IMAGEN DEL ODONTOGRAMA DE EVOLUCION
        else
        {
        		logger.info("ODONTOGRAMA DE EVOLUCION ");
        		logger.info("odontograma codigo evolucion->"+dtoPlanTratamiento.getCodigoEvolucion());
        		
        		dtoOdontograma.setEvolucion(dtoPlanTratamiento.getCodigoEvolucion().doubleValue());
        		dtoOdontograma.setIndicativo(ConstantesIntegridadDominio.acronimoOdontogramaTratamiento);
        		tmpOdontogramaDiagnostico= Odontograma.cargar(dtoOdontograma);
        		if (tmpOdontogramaDiagnostico.size() > 0)
	 	        {
	 	        	dtoOdontograma = tmpOdontogramaDiagnostico.get(0);
	 	        	logger.info("Imagen de Odontolograma -->"+CarpetasArchivos.IMAGENES_ODONTOEVO.getRutaFisica()+System.getProperty("file.separator")+dtoOdontograma.getImagen());
	 	        	comp.insertImageBodyPage(0, 0 , CarpetasArchivos.IMAGENES_ODONTOEVO.getRutaFisica()+System.getProperty("file.separator")+dtoOdontograma.getImagen() , "imagenOdon");
	 	        }
        }
        
        
        
        //SETTEAR LAS CONSULTAS EN EL REPORTE
        comp.obtenerComponentesDataSet("consultaDetalle") ;    
        comp.modificarQueryDataSet(sqlDetalle);
      	comp.obtenerComponentesDataSet("consultaOtros") ;    
        comp.modificarQueryDataSet(sqlOtros);
      	comp.obtenerComponentesDataSet("consultaBoca") ;    
        comp.modificarQueryDataSet(sqlBoca);
      	comp.obtenerComponentesDataSet("consultaGarantias") ;    
        comp.modificarQueryDataSet(sqlGarantia);
        comp.obtenerComponentesDataSet("consultaInclusiones") ;    
        comp.modificarQueryDataSet(sqlInclusion);
        comp.obtenerComponentesDataSet("consultaInclusionesBoca") ; 
        comp.modificarQueryDataSet(sqlInclusionBoca);
        
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	
        comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
        
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
        }
             
        forma.setEstado("mostrar");
        return mapping.findForward("paginaDetalle");
	}
	
}