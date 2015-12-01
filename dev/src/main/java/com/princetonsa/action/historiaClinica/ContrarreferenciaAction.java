package com.princetonsa.action.historiaClinica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
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
import util.InfoDatosString;
import util.Listado;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadSesion;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.historiaClinica.ContrarreferenciaForm;
import com.princetonsa.mundo.IngresoGeneral;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.Medico;
import com.princetonsa.mundo.ObservableBD;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.historiaClinica.Contrarreferencia;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

public class ContrarreferenciaAction extends Action 
{
	
	Logger logger =Logger.getLogger(ContrarreferenciaAction.class);
	
	
	String[] indices={"numeroreferencia_","institucion_","institucionsirc_","consecutivopuntoatencion_","anioconsecutivo_","institucionsircdestino_","fecharemision_","horaremision_","hallazgosclinicos_","examenfisico_","tratamientosinstaurados_","recomendaciones_","relacionanexos_","profesionalresponde_","estado_","usuariofinaliza_","fechafinaliza_","horafinaliza_","tiporegistro_"};
	
	
	String[] indicesReferencia={"numeroreferencia_","solicitud_","centrocosto_","paciente_","ingreso_","institucionsircsolicita_","nombreinstitucionsolicita_","fechareferencia_","horareferencia_","paciente_","tipoatencion_","tiporegistro_","codigopaciente_"};
	
	String[] indicesPaciente={"numeroreferencia_","fechareferencia_","horareferencia_","tipoatencion_","institucionsircsolicita_","nombreinstitucionsolicita_","tiporegistro_"};
	
	String[] indicesResultadosProcedimientos={"numerocontrarreferencia_","numerosolicitud_","hora_","descripcion_","interpretacion_","fecha_","tiporegistro_"};
	
	String[] indicesDiagnosticos={"codigo_","numerocontrarreferencia_","acronimodiagnostico_","tipocie_","principal_","tiporegistro_"};






	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception
	{
		Connection con = null;
		try {
			if (form instanceof ContrarreferenciaForm) 
			{
				ContrarreferenciaForm forma=(ContrarreferenciaForm) form;

				Contrarreferencia mundo=new Contrarreferencia();
				String estado=forma.getEstado();

				logger.info("Estado -->"+estado);

				con = UtilidadBD.abrirConexion();
				HttpSession session=request.getSession();
				UsuarioBasico usuario=Utilidades.getUsuarioBasicoSesion(request.getSession());



				UtilidadValidacion.esProfesionalSalud(usuario);

				//usuario.getNombreUsuario();
				//usuario.getNumeroRegistroMedico();
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				usuario.getInformacionGeneralPersonalSalud();

				if(!UtilidadValidacion.esProfesionalSalud(usuario) && !UtilidadValidacion.esMedico(usuario).equals("") && UtilidadCadena.noEsVacio(UtilidadValidacion.esEnfermera(usuario))) 
				{
					return ComunAction.accionSalirCasoError(mapping,request,con, logger, "No profesional" , "errors.noProfesionalSalud", true);

				}
				if(UtilidadValidacion.estaMedicoInactivo(con, usuario.getCodigoPersona() , usuario.getCodigoInstitucionInt()))
				{
					return ComunAction.accionSalirCasoError(mapping,request,con, logger, "No profesional Activo" , "errors.profesionalSaludInactivo", true);

				}

				//forma.setMensaje(new ResultadoBoolean(false));
				if(estado == null)
				{
					logger.warn("Estado no valido dentro del flujo de ContrarreferenciaAction (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					UtilidadBD.closeConnection(con);
					return mapping.findForward("paginaError");
				}

				if(Utilidades.obtenerInstitucionSirCentroAtencion(con, usuario.getCodigoCentroAtencion()).getCodigo().equals(ConstantesBD.codigoNuncaValido+""))
					return ComunAction.accionSalirCasoError(mapping,request,con, logger, "No Codigo SIRC" , "error.historiaClinica.contrarreferencia.nocodigoSirc", true);

				forma.setMaxPageItems(ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()));
				if (estado.equals("empezar"))
				{	
					if(paciente.getCodigoPersona()<1)
					{
						return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente no Cargado", "errors.paciente.noCargado", true);
					}
					forma.setContrarreferenciaMap(mundo.consultarReferenciaPaciente(con, forma.getTipoReferencia(), forma.getEstadoReferencia(), paciente.getCodigoPersona()));
					/*if(mundo.consultarReferenciaPaciente(con, forma.getTipoReferencia(), forma.getEstadoReferencia(), paciente.getCodigoPersona()).size()==1)
				{
					return this.accionDetalle(con,forma,mundo,mapping,usuario,false);
				}*/

					inicializarValores(forma,con,usuario);
					forma.setContrarreferenciaMap(mundo.consultarReferenciaPaciente(con, forma.getTipoReferencia(), forma.getEstadoReferencia(), paciente.getCodigoPersona()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if (estado.equals("empezarArea"))
				{	
					inicializarValores(forma,con,usuario);

					forma.setCodigoCentros(Utilidades.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(),  ConstantesBD.codigoTipoAreaDirecto+"", false,usuario.getCodigoCentroAtencion(),false));
					forma.setContrarreferenciaMap(mundo.consultarReferenciaArea(con, forma.getTipoReferencia(), forma.getEstadoReferencia(), forma.getCentrosCosto()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("cargarInfo"))
				{
					forma.setContrarreferenciaMap(mundo.consultarReferenciaArea(con, forma.getTipoReferencia(), forma.getEstadoReferencia(), forma.getCentrosCosto()));
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if (estado.equals("empezarContra"))
				{	
					inicializarValores(forma,con,usuario);


					forma.setCodigoCentros(Utilidades.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoAreaDirecto+"", false,usuario.getCodigoCentroAtencion(),false));

					if(forma.getEstadoRegistro().equals(""))
					{
						forma.setEstadoRegistro(ConstantesIntegridadDominio.acronimoEstadoPendiente);
					}
					if(forma.getEstadoRegistro().equals(ConstantesIntegridadDominio.acronimoEstadoFinalizado))
					{
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("resumen");
					}
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("detalle");

				}
				else if(estado.equals("detalleContra"))
				{
					return this.accionDetalle(con,forma,mundo,mapping,usuario,false);
				}
				else if(estado.equals("detalleContraArea"))
				{
					int codigoPaciente=Integer.parseInt(forma.getContrarreferenciaMap("codigopaciente_"+forma.getIndexSeleccionado())+"");
					UtilidadSesion.notificarCambiosObserver(codigoPaciente, getServlet().getServletContext());
					paciente.setCodigoPersona(codigoPaciente);
					paciente.cargar(con,codigoPaciente);
					paciente.cargarPaciente(con,codigoPaciente,usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
					setObservable(paciente);
					return this.accionDetalle(con,forma,mundo,mapping,usuario,false);
				}
				else if(estado.equals("guardar"))
				{
					return this.accionGuardarRegistros(con,forma,mundo,usuario,request,mapping);
				}
				else if (estado.equals("busquedaResultadosExamenes"))
				{
					return accionBusquedaResultadosExamenes(con,forma,mapping,usuario);
				}
				else if(estado.equals("eliminarProcedimiento"))
				{
					Utilidades.eliminarRegistroMapaGenerico(forma.getResultadosProcedimientos(), forma.getResultadosProcedimientosEliminados(), forma.getPosEliminar(), indicesResultadosProcedimientos, "numRegistros", "tiporegistro_", "BD", false);
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("detalle");
				}
				else if(estado.equals("ordenarProcedimientos"))
				{
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("busquedaResultadosExamenes");
				}
				else if(estado.equals("redireccion"))
				{
					return accionRedireccion(con,forma,response,mapping,request);
				}
				else if(estado.equals("ordenar"))
				{
					this.accionOrdenarMapa(forma,indicesPaciente);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				else if(estado.equals("ordenarArea"))
				{
					this.accionOrdenarMapa(forma,indicesReferencia);
					UtilidadBD.closeConnection(con);
					return mapping.findForward("principal");
				}
				//**************SECCION EXCLUSICA PARA HISTORIA DE ATENCIONES***********************************
				else if (estado.equals("historiaAtenciones") || estado.equals("resumenConsulta"))
				{
					return accionResumenContrareferencia(con,forma,mundo,mapping,usuario, paciente);
				}
				//************************************************************************************************
				else if(estado.equals("generarReporte"))
				{                
					this.generarReporte(con, forma, usuario, request);                
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("resumen");
				}
				else
				{
					inicializarValores(forma,con,usuario);

					logger.warn("Estado no valido dentro del flujo de CONTRARREFERENCIA ");
					request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("paginaError");
				}
			}
			else
			{
				logger.error("El form no es compatible con el form de ContrarreferenciaForm");
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
	 * @param usuario
	 * @param request
	 */
	private void generarReporte(Connection con, ContrarreferenciaForm forma, UsuarioBasico usuario, HttpServletRequest request) 
	{
		DesignEngineApi comp;  
		InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
		comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"historiaClinica/referenciaContrareferencia/","Contrareferencia.rptdesign");
		
		String firmaDigital = Medico.obtenerFirmaDigitalMedico(forma.getProfesionalResponde());
		String pathFirmaDigital = ValoresPorDefecto.getFilePath()+System.getProperty("file.separator")+System.getProperty("FIRMADIGITAL")+System.getProperty("file.separator")+firmaDigital;
		
		//RA - Se mantiene el formato Original
		if(forma.getContrarreferenciaMap("tipred")!=null && forma.getContrarreferenciaMap("tipred").toString().equals(ConstantesIntegridadDominio.acronimoRA)) {

			//imagenes
			comp.insertImageHeaderOfMasterPage1(0, 0, ValoresPorDefecto.getDirectorioImagenes()+"secretaria_salud.gif");
	        comp.insertImageHeaderOfMasterPage1(0, 2, ins.getLogoReportes());

	        //comp.insertLabelInGridPpalOfHeader(1,0,ins.getRazonSocial());
	        comp.insertLabelInGridPpalOfHeader(0,1, "CONTRAREFERENCIA \n" + "SISTEMA INTEGRAL DE REFERENCIA Y CONTRAREFERENCIA \n" + "RESPUESTA SOLICITUD DE SERVICIOS");
	        //aca iria tambien lo del nombre de la institucion
		}

		else { //RN - 68183
	        comp.insertImageHeaderOfMasterPage1(0, 2, ins.getLogoReportes());
	        comp.insertGridHeaderOfMasterPage(0,1,1,4);
	
	        //institucion.getLogoReportes()
		        
	        Vector v = new Vector();
	        v.add(ins.getRazonSocial());
	        v.add(Utilidades.getDescripcionTipoIdentificacion(con, ins.getTipoIdentificacion()) + "  -  " + ins.getNit());
	        v.add(ins.getDireccion());
	        v.add("Tels. " + ins.getTelefono());
	
	        comp.insertLabelInGridOfMasterPage(0, 1, v);
	        comp.insertLabelInGridPpalOfHeader(1, 1, "SISTEMA INTEGRAL DE REFERENCIA Y CONTRAREFERENCIA ");
		}
		
		if (!UtilidadTexto.isEmpty(firmaDigital))
	        	comp.insertImageBodyPage(0, 0, pathFirmaDigital, "firma");
		
		//Obtenemos el DataSet Conducta y lo modificamos
        comp.obtenerComponentesDataSet("Conducta");
		String newQuery = comp.obtenerQueryDataSet().replace("c.numero_referencia_contra=?", "c.numero_referencia_contra= "+forma.getNumeroReferenciaContra());
        logger.info("=====>Consulta en el BIRT: "+newQuery);
        //Se modifica el query
        comp.modificarQueryDataSet(newQuery);
        
      //Obtenemos el DataSet DatosControl1 y lo modificamos
        comp.obtenerComponentesDataSet("DatosControl1");
		String newQuery1 = comp.obtenerQueryDataSet().replace("numero_referencia_contra=?", "numero_referencia_contra = "+forma.getNumeroReferenciaContra());
        logger.info("=====>Consulta en el BIRT: "+newQuery1);
        //Se modifica el query
        comp.modificarQueryDataSet(newQuery1);
		
      //Obtenemos el DataSet Dx y lo modificamos
        comp.obtenerComponentesDataSet("Dx");
		String newQuery2 = comp.obtenerQueryDataSet().replace("dc.numero_referencia_contra=?", "dc.numero_referencia_contra = "+forma.getNumeroReferenciaContra()+" ");
        logger.info("=====>Consulta en el BIRT: "+newQuery2);
        //Se modifica el query
        comp.modificarQueryDataSet(newQuery2);
        
      //Obtenemos el DataSet HallazgosClinicos y lo modificamos
        comp.obtenerComponentesDataSet("HallazgosClinicos");
		String newQuery3 = comp.obtenerQueryDataSet().replace("numero_referencia_contra=?", " numero_referencia_contra = "+forma.getNumeroReferenciaContra()+" ");
        logger.info("=====>Consulta en el BIRT: "+newQuery3);
        //Se modifica el query
        comp.modificarQueryDataSet(newQuery3);
        
        
      //Obtenemos el DataSet Identificacion Paciente y lo modificamos
        comp.obtenerComponentesDataSet("IdentificacionPaciente");
		String newQuery4 = comp.obtenerQueryDataSet().replace("i.id=?", " i.id = "+forma.getIngreso()+" ");
        logger.info("=====>Consulta en el BIRT: "+newQuery4);
        //Se modifica el query
        comp.modificarQueryDataSet(newQuery4);
        
      //Obtenemos el DataSet Institucion Respuesta y lo modificamos
        comp.obtenerComponentesDataSet("InstitucionRespuesta");
		String newQuery5 = comp.obtenerQueryDataSet().replace("numero_referencia_contra=?", " numero_referencia_contra =  "+forma.getNumeroReferenciaContra()+" ");
        logger.info("=====>Consulta en el BIRT: "+newQuery5);
        //Se modifica el query
        comp.modificarQueryDataSet(newQuery5);
      
		//Obtenemos el DataSet Numero Solicitud y lo modificamos
        comp.obtenerComponentesDataSet("NumeroSolicitud");
		String newQuery6 = comp.obtenerQueryDataSet().replace("c.numero_referencia_contra=?", " c.numero_referencia_contra =  "+forma.getNumeroReferenciaContra()+" ");
        logger.info("=====>Consulta en el BIRT: "+newQuery6);
        //Se modifica el query
        comp.modificarQueryDataSet(newQuery6);  		
        
      //Obtenemos el DataSet Resultados Procedimientos y lo modificamos
        comp.obtenerComponentesDataSet("ResultadosProcedimientos");
		String newQuery7 = comp.obtenerQueryDataSet().replace("numero_referencia_contra=?", " numero_referencia_contra =  "+forma.getNumeroReferenciaContra()+" ");
        logger.info("=====>Consulta en el BIRT: "+newQuery7);
        //Se modifica el query
        comp.modificarQueryDataSet(newQuery7);
        
      //Obtenemos el DataSet Tratamientos Recomendacion Relacion Anexoss y lo modificamos
        comp.obtenerComponentesDataSet("tratamientosRecomendaRelacionAnexos");
		String newQuery8 = comp.obtenerQueryDataSet().replace("numero_referencia_contra=?", " numero_referencia_contra =  "+forma.getNumeroReferenciaContra()+" ");
        logger.info("=====>Consulta en el BIRT: "+newQuery8);
        //Se modifica el query
        comp.modificarQueryDataSet(newQuery8); 
        
			
        		
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
		logger.info("\n \n New Path >> "+newPathReport);
        if(!newPathReport.equals("")) {
        	request.setAttribute("isOpenReport", "true");
        	request.setAttribute("newPathReport", newPathReport);
       	}
        
        comp.updateJDBCParameters(newPathReport);
		
		
		/* imagenes asi estaba originalmente con solo esto
		comp.insertImageHeaderOfMasterPage1(0, 0, ValoresPorDefecto.getDirectorioImagenes()+"secretaria_salud.gif");
        comp.insertImageHeaderOfMasterPage1(0, 2, ins.getLogoReportes());
        comp.insertLabelInGridPpalOfHeader(1,0,ins.getRazonSocial());
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        if(!newPathReport.equals("")) {
            request.setAttribute("isOpenReport", "true");
            request.setAttribute("newPathReport", newPathReport);
        }            
        comp.updateJDBCParameters(newPathReport); */
	}

	/**
	 * Método implementado para invocar el resumen de la contrarreferencia
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	private ActionForward accionResumenContrareferencia(Connection con, ContrarreferenciaForm forma, Contrarreferencia mundo, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		String numeroReferencia = forma.getNumeroReferenciaContra()+"";
		String linkVolver="";
		if(!UtilidadTexto.isEmpty(forma.getBotonVolver()))
			linkVolver=forma.getBotonVolver();
		int ingreso=forma.getIngreso();
		
		/*cargo el paciente en sesion*/
		logger.info("cidugipersona->"+paciente.getCodigoPersona());
		IngresoGeneral i=new IngresoGeneral();
		try 
		{
			i.cargarIngreso(con, ingreso+"");
			i.cargarPacienteDadoIngreso(con, ingreso+"");
			logger.info("cod persona del ingreso->"+i.getPaciente().getCodigoPersona());
			paciente.setCodigoPersona(i.getPaciente().getCodigoPersona());
		    paciente.cargar(con, i.getPaciente().getCodigoPersona());
			paciente.cargarPaciente(con, i.getPaciente().getCodigoPersona(), usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
		} 
	    catch (SQLException e) 
	    {
			e.printStackTrace();
		}
		/*fin cargar pac en sesion*/
		
		
		forma.reset();
		
		forma.setBotonVolver(linkVolver);
		forma.setIngreso(ingreso);
		//Se consulta la contrarreferencia contra
		mundo.consultarContrarreferencia(con,usuario.getCodigoInstitucionInt(),numeroReferencia);
		forma.setNumeroReferenciaContra(Integer.parseInt(numeroReferencia));
		
		//cargar los atributos del form.
		if(Integer.parseInt(mundo.getContrarreferenciaMap().get("numRegistros")+"")>0)
		{
			forma.setFechaRemision(UtilidadFecha.conversionFormatoFechaAAp(mundo.getContrarreferenciaMap().get("fecharemision")+""));
			forma.setHoraRemision(mundo.getContrarreferenciaMap().get("horaremision")+"");
			forma.setNumeroReferenciaContra(Integer.parseInt(mundo.getContrarreferenciaMap().get("numerocontrarreferencia")+""));
			forma.setInstitucionOrigen(mundo.getContrarreferenciaMap().get("institucionsircorigen")+"");
			forma.setConsecutivoInt(Integer.parseInt(mundo.getContrarreferenciaMap().get("consecutivopuntoatencion")+""));
			forma.setConsecutivoPuntoAtencion(mundo.getContrarreferenciaMap().get("consecutivopuntoatencion")+"");
			forma.setConsecutivoAnio(mundo.getContrarreferenciaMap().get("anioconsecutivo")+"");
			forma.setInstitucionDestino(mundo.getContrarreferenciaMap().get("institucionsircdestino")+"");
			forma.setHallazgosClinicos(mundo.getContrarreferenciaMap().get("hallazgosclinicos")+"");
			forma.setExamenFisico(mundo.getContrarreferenciaMap().get("examenfisicos")+"");
			forma.setTratamientosInstaurados(mundo.getContrarreferenciaMap().get("tratamientosinstaurados")+"");
			forma.setRecomendaciones(mundo.getContrarreferenciaMap().get("recomendaciones")+"");
			forma.setRelacionAnexos(mundo.getContrarreferenciaMap().get("relacionanexos")+"");
			forma.setProfesionalResponde(Integer.parseInt(mundo.getContrarreferenciaMap().get("profesionalresponde")+""));
			forma.setEstadoRegistro(mundo.getContrarreferenciaMap().get("estado")+"");
			forma.setUsuarioFinaliza(mundo.getContrarreferenciaMap().get("usuariofinaliza")+"");
			forma.setFechaFinaliza(UtilidadFecha.conversionFormatoFechaAAp(mundo.getContrarreferenciaMap().get("fechafinaliza")+""));
			forma.setHoraFinaliza(mundo.getContrarreferenciaMap().get("horafinaliza")+"");
			forma.setNombreInstitucionOrigen(mundo.getContrarreferenciaMap().get("nombreinstitucionorigen")+"");

			logger.info("+++++++++++++***********************");
			
			Utilidades.imprimirMapa(mundo.getContrarreferenciaMap());

			forma.setResultadosProcedimientos(mundo.consultarResultadosProcedimiento(con,forma.getNumeroReferenciaContra()));
			forma.setEsModificacion(true);
		}
		
		if(forma.isEsModificacion())
		{
			forma.setConductasSeguir(mundo.cargarConductasSeguirContrareferencia(con,forma.getNumeroReferenciaContra()));
			forma.setResultadosProcedimientos(mundo.consultarResultadosProcedimiento(con,forma.getNumeroReferenciaContra()));
			forma.setDiagnosticos(mundo.consultarDiagnosticos(con,forma.getNumeroReferenciaContra()));
			forma.setNumDiagnosticos(Integer.parseInt(forma.getDiagnosticos("numRegistros")+""));

		}
		
		//Se prepara la informacion de la contrarreferencia para mostrarla en el resumen
		forma.setContrarreferenciaMap("nombreinstitucionsolicita_"+forma.getIndexSeleccionado(), mundo.getContrarreferenciaMap().get("nombreinstituciondestino")+"");
		forma.setContrarreferenciaMap("numeroreferencia_"+forma.getIndexSeleccionado(), forma.getNumeroReferenciaContra());
		forma.setContrarreferenciaMap("tipred", mundo.getContrarreferenciaMap().get("tipred")+"");
		forma.setContrarreferenciaMap("tipred1", mundo.getContrarreferenciaMap().get("tipred1")+"");

		
		UtilidadBD.closeConnection(con);
		
		return mapping.findForward("resumen");
	}




	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param mapping
	 * @param usuario
	 * @param esResumen 
	 * @return
	 */
	private ActionForward accionDetalle(Connection con, ContrarreferenciaForm forma, Contrarreferencia mundo, ActionMapping mapping, UsuarioBasico usuario, boolean esResumen) 
	{

 		forma.setIngreso(Integer.parseInt(forma.getContrarreferenciaMap("ingreso_"+forma.getIndexSeleccionado())+""));
		this.accionConsultarContrarreferencia(con, forma, mundo, usuario);
		if(forma.isEsModificacion())
		{
			forma.setConductasSeguir(mundo.cargarConductasSeguirContrareferencia(con,forma.getNumeroReferenciaContra()));
			forma.setResultadosProcedimientos(mundo.consultarResultadosProcedimiento(con,forma.getNumeroReferenciaContra()));
			forma.setDiagnosticos(mundo.consultarDiagnosticos(con,forma.getNumeroReferenciaContra()));
			forma.setNumDiagnosticos(Integer.parseInt(forma.getDiagnosticos("numRegistros")+""));
		}
		else
		{
			forma.setConductasSeguir(mundo.cargarConductasSeguirContrareferencia(con,ConstantesBD.codigoNuncaValido));
		}
		UtilidadBD.closeConnection(con);
		if(esResumen)
			return mapping.findForward("resumen");
		return mapping.findForward("detalle");
	}




	/**
	 * 
	 * @param con
	 * @param form
	 * @param response
	 * @param mapping
	 * @param request
	 * @return
	 */
	private ActionForward accionRedireccion(Connection con, ContrarreferenciaForm forma, HttpServletResponse response, ActionMapping mapping, HttpServletRequest request) 
	{
		try
		{
			
		    UtilidadBD.cerrarConexion(con);
			response.sendRedirect(forma.getLinkSiguiente());
			return null;
		}
		catch(Exception e)
		{
			logger.error("Error en accionRedireccion de ContrarreferenciaAction: "+e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Error en ContrarreferenciaAction", "errors.problemasDatos", true);
		}
	}




	/**
	 * @param forma 
	 * 
	 *
	 */
	private void inicializarValores(ContrarreferenciaForm forma,Connection con,UsuarioBasico usuario)
	{
		forma.reset();

		String consecutivo = UtilidadBD.obtenerValorActualTablaConsecutivos(con,ConstantesBD.nombreConsecutivoContrarreferencia,usuario.getCodigoInstitucionInt());
		String anioConsecutivo=UtilidadBD.obtenerAnioActualTablaConsecutivos(con, ConstantesBD.nombreConsecutivoContrarreferencia, usuario.getCodigoInstitucionInt());

		forma.setAnioConsecutivoPuntoAtencion(anioConsecutivo);
		forma.setConsecutivoPuntoAtencion(consecutivo);

		InfoDatosString infoTempo=Utilidades.obtenerInstitucionSirCentroAtencion(con, usuario.getCodigoCentroAtencion());
		forma.setInstitucionOrigen(infoTempo.getCodigo());
		forma.setNombreInstitucionOrigen(infoTempo.getNombre());
		forma.setCentrosCosto(usuario.getCodigoCentroCosto());
	}
	
	/**
	 * 
	 * 
	 * @param paciente
	 * @param mapping
	 * @param request
	 * @param con
	 * @return
	 */
	protected ActionForward accionValidacionesAcceso(	PersonaBasica paciente, 
													ActionMapping mapping, 
													HttpServletRequest request, 
													Connection con)
	{
		if(paciente.getCodigoPersona()<1)
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "paciente null o sin  id", "errors.paciente.noCargado", true);
		}
		else if(paciente.getCodigoCuenta()<1)
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "PACIENTE SIN CUENTA ACTIVA", "errors.paciente.cuentaNoAbierta", true);
		}
		else if(!UtilidadValidacion.esContrarreferencia(con, paciente.getCodigoIngreso()))
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "NO CONTRARREFERENCIA", "error.noContrarreferencia", true);
		}
		return null;
	}
	
	
	
	
	private ActionForward accionBusquedaResultadosExamenes(Connection con, ContrarreferenciaForm forma, ActionMapping mapping, UsuarioBasico usuario) 
	{
		if(forma.getTipoReferencia().equals(ConstantesIntegridadDominio.acronimoExterna))
		{
			forma.setResultadosProcedimientos(UtilidadesHistoriaClinica.obtenerInterpretacionProcedimientosIngreso(con, forma.getIngreso(), usuario.getCodigoInstitucionInt(),forma.getProcedimientosInsertados()));
		}
		forma.setMaxPageItems(ValoresPorDefecto.getMaxPageItemsInt(usuario.getCodigoInstitucionInt()));
		UtilidadBD.closeConnection(con);
		return mapping.findForward("busquedaResultadosExamenes");
	}
	
		
	private void accionOrdenarMapa(ContrarreferenciaForm forma,String[] indices)
	{
		int numReg=Integer.parseInt(forma.getContrarreferenciaMap("numRegistros")+"");
		forma.setContrarreferenciaMap(Listado.ordenarMapa(indices,forma.getPatronOrdenar(),forma.getUltimoPatron(),forma.getContrarreferenciaMap(),numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setContrarreferenciaMap("numRegistros",numReg+"");
		
	}
	
	
	private void accionConsultarContrarreferencia(Connection con, ContrarreferenciaForm forma, Contrarreferencia mundo, UsuarioBasico usuario)
	{
		forma.setNumeroReferenciaContra(Integer.parseInt(forma.getContrarreferenciaMap("numeroreferencia_"+forma.getIndexSeleccionado())+""));
		forma.setProfesionalResponde(usuario.getCodigoPersona());
		mundo.consultarContrarreferencia(con,usuario.getCodigoInstitucionInt(),forma.getContrarreferenciaMap("numeroreferencia_"+forma.getIndexSeleccionado())+"");
		if((Integer.parseInt(forma.getContrarreferenciaMap("numRegistros")+"")>0))
		{
			forma.setHallazgosClinicos(UtilidadesHistoriaClinica.getUltimaAnamnesisIngreso(con, Integer.parseInt(forma.getContrarreferenciaMap("ingreso_"+forma.getIndexSeleccionado())+"")));
			
		}
		
		
		forma.setDiagnosticos(mundo.getUltimosDiagnosticosIngreso(con, Integer.parseInt(forma.getContrarreferenciaMap("ingreso_"+forma.getIndexSeleccionado())+"")));
		forma.setNumDiagnosticos(Integer.parseInt(forma.getDiagnosticos("numRegistros")+""));
		
		
		//cargar los atributos del form.
		if(Integer.parseInt(mundo.getContrarreferenciaMap().get("numRegistros")+"")>0)
		{
			forma.setFechaRemision(UtilidadFecha.conversionFormatoFechaAAp(mundo.getContrarreferenciaMap().get("fecharemision")+""));
			forma.setHoraRemision(mundo.getContrarreferenciaMap().get("horaremision")+"");
			forma.setNumeroReferenciaContra(Integer.parseInt(mundo.getContrarreferenciaMap().get("numerocontrarreferencia")+""));
			forma.setInstitucionOrigen(mundo.getContrarreferenciaMap().get("institucionsircorigen")+"");
			forma.setConsecutivoInt(Integer.parseInt(mundo.getContrarreferenciaMap().get("consecutivopuntoatencion")+""));
			forma.setConsecutivoAnio(mundo.getContrarreferenciaMap().get("anioconsecutivo")+"");
			forma.setInstitucionDestino(mundo.getContrarreferenciaMap().get("institucionsircdestino")+"");
			forma.setHallazgosClinicos(mundo.getContrarreferenciaMap().get("hallazgosclinicos")+"");
			forma.setExamenFisico(mundo.getContrarreferenciaMap().get("examenfisicos")+"");
			forma.setTratamientosInstaurados(mundo.getContrarreferenciaMap().get("tratamientosinstaurados")+"");
			forma.setRecomendaciones(mundo.getContrarreferenciaMap().get("recomendaciones")+"");
			forma.setRelacionAnexos(mundo.getContrarreferenciaMap().get("relacionanexos")+"");
			forma.setProfesionalResponde(Integer.parseInt(mundo.getContrarreferenciaMap().get("profesionalresponde")+""));
			forma.setEstado(mundo.getContrarreferenciaMap().get("estado")+"");
			forma.setUsuarioFinaliza(mundo.getContrarreferenciaMap().get("usuariofinaliza")+"");
			forma.setFechaFinaliza(UtilidadFecha.conversionFormatoFechaAAp(mundo.getContrarreferenciaMap().get("fechafinaliza")+""));
			forma.setHoraFinaliza(mundo.getContrarreferenciaMap().get("horafinaliza")+"");
			forma.setNombreInstitucionOrigen(mundo.getContrarreferenciaMap().get("nombreinstitucionorigen")+"");
			forma.setResultadosProcedimientos(mundo.consultarResultadosProcedimiento(con,forma.getNumeroReferenciaContra()));
			forma.setEsModificacion(true);
				
		}
	}
	
	
	
	
	
	private ActionForward accionGuardarRegistros(Connection con, ContrarreferenciaForm forma, Contrarreferencia mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping)
	{
		
		if(forma.isEsModificacion())
		{
			return this.accionModificarContrarreferencia(con,forma,mundo,usuario,request,mapping);
		}
		else
		{
			return this.accionInsertarContrarreferencia(con,forma,mundo,usuario,request,mapping);
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
	 */
	private ActionForward accionInsertarContrarreferencia(Connection con, ContrarreferenciaForm forma, Contrarreferencia mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) 
	{
		//se toma el consecutivo al momento de guardar.
		
		String consecutivo = UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoContrarreferencia,usuario.getCodigoInstitucionInt());
		String anioConsecutivo=UtilidadBD.obtenerAnioConsecutivo(ConstantesBD.nombreConsecutivoContrarreferencia, usuario.getCodigoInstitucionInt(),consecutivo);


		forma.setAnioConsecutivoPuntoAtencion(anioConsecutivo);
		forma.setConsecutivoPuntoAtencion(consecutivo);
		
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		transaccion=mundo.insertar(con, this.cargarVO(forma,usuario));
		if(transaccion)
			transaccion=this.accionGuardarProcedimientos(con, forma, mundo, usuario);
		if(transaccion)
			transaccion=this.accionGuardarDiagnosticos(con, forma, mundo, usuario);
		if(transaccion)
			transaccion=this.accionGuardarConductasSeguir(con, forma, mundo, usuario);
		
		
		if(transaccion)
		{
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoContrarreferencia,usuario.getCodigoInstitucionInt(), consecutivo, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
			UtilidadBD.finalizarTransaccion(con);
			return this.accionDetalle(con, forma, mundo, mapping, usuario,true);
		}
		else
		{
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoContrarreferencia,usuario.getCodigoInstitucionInt(), consecutivo, ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
			UtilidadBD.abortarTransaccion(con);
			logger.warn("Problemas con la base de datos ");
            request.setAttribute("codigoDescripcionError", "errors.problemasBd");
            return mapping.findForward("paginaError");
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
	 */
	private ActionForward accionModificarContrarreferencia(Connection con, ContrarreferenciaForm forma, Contrarreferencia mundo, UsuarioBasico usuario, HttpServletRequest request, ActionMapping mapping) 
	{
		boolean transaccion=UtilidadBD.iniciarTransaccion(con);
		
		transaccion=mundo.modificar(con,this.cargarVO(forma,usuario));
		//modificar solprocedi, diagnosticos
		
		if(transaccion)
			transaccion=this.accionGuardarProcedimientos(con, forma, mundo, usuario);
		if(transaccion)
			transaccion=this.accionGuardarDiagnosticos(con, forma, mundo, usuario);
		if(transaccion)
			transaccion=this.accionGuardarConductasSeguir(con, forma, mundo, usuario);


		if(transaccion)
		{
			UtilidadBD.finalizarTransaccion(con);
			return this.accionDetalle(con, forma, mundo, mapping, usuario,true);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
			logger.warn("Problemas con la base de datos ");
            request.setAttribute("codigoDescripcionError", "errors.problemasBd");
            return mapping.findForward("paginaError");
		}
	}




	/**
	 * 
	 * @param con
	 * @param forma
	 * @param mundo
	 * @param usuario
	 * @return
	 */
	private boolean accionGuardarConductasSeguir(Connection con, ContrarreferenciaForm forma, Contrarreferencia mundo, UsuarioBasico usuario) 
	{
		// codigo | descripcion | requierevalor | valor | activo | tiporegistro
		boolean transaccion=true;
		for(int i=0;i<Integer.parseInt(forma.getConductasSeguir("numRegistros")+"");i++)
		{
			if((forma.getConductasSeguir("tiporegistro_"+i)+"").trim().equals("BD")&&(forma.getConductasSeguir("activo_"+i)+"").trim().equals(ConstantesBD.acronimoNo))
			{
				transaccion=mundo.eliminarConductaSeguir(con,forma.getNumeroReferenciaContra(),forma.getConductasSeguir("codigo_"+i)+"");
			}
			else if((forma.getConductasSeguir("tiporegistro_"+i)+"").trim().equals("MEM")&&(forma.getConductasSeguir("activo_"+i)+"").trim().equals(ConstantesBD.acronimoSi))
			{
				transaccion=mundo.insertarConductaSeguir(con,forma.getNumeroReferenciaContra(),forma.getConductasSeguir("codigo_"+i)+"",forma.getConductasSeguir("valor_"+i)+"");
			}
			
		}
		return transaccion;
	}




	/**
	 * 
	 * @param forma
	 * @param usuario
	 * @return
	 */
	private HashMap cargarVO(ContrarreferenciaForm forma, UsuarioBasico usuario)
	{
			HashMap vo=new HashMap();
			vo.put("numero_referencia_contra",forma.getNumeroReferenciaContra());
			vo.put("institucion",usuario.getCodigoInstitucion());
			vo.put("institucion_sirc_origen",forma.getInstitucionOrigen());
			vo.put("consecutivo_punto_atencion",forma.getConsecutivoPuntoAtencion());
			vo.put("anio_consecutivo",forma.getConsecutivoAnio());
			vo.put("institucion_sirc_destino",forma.getContrarreferenciaMap("institucionsircsolicita_"+forma.getIndexSeleccionado()));
			vo.put("fecha_remision",forma.getFechaRemision());
			vo.put("hora_remision",forma.getHoraRemision());
			vo.put("hallazgos_clinicos",forma.getHallazgosClinicos());
			vo.put("examen_fisicos",forma.getExamenFisico());
			vo.put("tratamientos_instaurados",forma.getTratamientosInstaurados());
			vo.put("recomendaciones",forma.getRecomendaciones());
			vo.put("relacion_anexos",forma.getRelacionAnexos());
			vo.put("profesional_responde",forma.getProfesionalResponde());
			vo.put("estado",forma.getEstadoRegistro());
			if(forma.getEstadoRegistro().equals(ConstantesIntegridadDominio.acronimoEstadoFinalizado))
			{
				vo.put("usuario_finaliza",usuario.getLoginUsuario());
				vo.put("fecha_finaliza",UtilidadFecha.getFechaActual());
				vo.put("hora_finaliza",UtilidadFecha.getHoraActual());
			}
			else
			{
				vo.put("usuario_finaliza","");
				vo.put("fecha_finaliza","");
				vo.put("hora_finaliza","");
			}
			vo.put("usuario_modifica",usuario.getLoginUsuario());
			vo.put("fecha_modifica",UtilidadFecha.getFechaActual());
			vo.put("hora_modifica",UtilidadFecha.getHoraActual());
			
			
			return vo;
	}
	
	
	
	private boolean accionGuardarProcedimientos(Connection con, ContrarreferenciaForm forma, Contrarreferencia mundo, UsuarioBasico usuario)
	{
		boolean transaccion=true;
		
		//eliminar
		for(int i=0;i<Integer.parseInt(forma.getResultadosProcedimientosEliminados("numRegistros")+"");i++)
		{
			if(mundo.eliminarProcedimientos(con,forma.getResultadosProcedimientosEliminados("numerocontrarreferencia_"+i)+"", forma.getResultadosProcedimientosEliminados("numerosolicitud_"+i)+""))
			{
				transaccion=true;
			}
		}
		
		for(int i=0;i<Integer.parseInt(forma.getResultadosProcedimientos("numRegistros")+"");i++)
		{
			//insertar
			if((forma.getResultadosProcedimientos("tiporegistro_"+i)+"").trim().equalsIgnoreCase("MEM"))
			{
				HashMap vo=new HashMap();
				vo.put("numero_referencia_contra",forma.getNumeroReferenciaContra()+"");
				vo.put("numero_solicitud",forma.getResultadosProcedimientos("numero_solicitud_"+i));
				vo.put("hora",forma.getResultadosProcedimientos("hora_"+i));
				vo.put("descripcion",forma.getResultadosProcedimientos("descripcion_"+i));
				vo.put("interpretacion",forma.getResultadosProcedimientos("interpretacion_"+i));
				vo.put("fecha",forma.getResultadosProcedimientos("fecha_"+i));
				transaccion=mundo.insertarProcedimientos(con, vo);
			}
			
		}
		
		return transaccion;
	}
	
	
	
	private boolean accionGuardarDiagnosticos(Connection con, ContrarreferenciaForm forma, Contrarreferencia mundo, UsuarioBasico usuario)
	{
		boolean transaccion=true;
		//eliminar los diagnosticos existentes.
		mundo.eliminarDiagnosticos(con,forma.getNumeroReferenciaContra());
		
		//insertar
		if(forma.getDiagnosticos().containsKey("principal")&&!(forma.getDiagnosticos().get("principal")+"").trim().equals(""))
		{
	
	
			HashMap vo=new HashMap();
			String[] diagnostico=(forma.getDiagnosticos("principal")+"").split(ConstantesBD.separadorSplit);
			vo.put("numerocontrarreferencia",forma.getNumeroReferenciaContra()+"");
			vo.put("acronimodiagnostico",diagnostico[0]);
			if(diagnostico.length>1)
				vo.put("tipocie",diagnostico[1]);
			else
				vo.put("tipocie","");
			vo.put("principal",ConstantesBD.acronimoSi);
			transaccion=mundo.insertarDiagnosticos(con, vo);
		}		

		for(int i=0;i<forma.getNumDiagnosticos();i++)
		{	
		
			if(forma.getDiagnosticos().containsKey("checkRel_"+i)&&UtilidadTexto.getBoolean(forma.getDiagnosticos().get("checkRel_"+i)+""))
			{
				HashMap vo=new HashMap();
				vo.clear();
				String[] diagnosticoRel=(forma.getDiagnosticos("relacionado_"+i)+"").split(ConstantesBD.separadorSplit);
				vo.put("numerocontrarreferencia",forma.getNumeroReferenciaContra()+"");
				vo.put("acronimodiagnostico",diagnosticoRel[0]);
				vo.put("tipocie",diagnosticoRel[1]);
				vo.put("principal",ConstantesBD.acronimoNo);
				transaccion=mundo.insertarDiagnosticos(con, vo);
			}
		}
		return transaccion;
	}
	
	/**
	* Método para hacer que el paciente
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
}
