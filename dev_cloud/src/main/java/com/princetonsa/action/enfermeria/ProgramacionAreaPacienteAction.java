package com.princetonsa.action.enfermeria;

import java.sql.Connection;
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
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.RespuestaValidacion;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.enfermeria.ProgramacionAreaPacienteForm;
import com.princetonsa.dao.sqlbase.enfermeria.SqlBaseProgramacionAreaPacienteDao;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.enfermeria.ProgramacionAreaPaciente;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;


/**
 * @version 1.0
 * @fecha 06/01/09
 * @author Jhony Alexander Duque A. y Diego Fernando Bedoya Castaño
 *
 */
public class ProgramacionAreaPacienteAction extends Action
{
	
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(ProgramacionAreaPacienteAction.class);
	
	//indices para el manejo de los datos
	String indicesListadoOrdenesMedicas[]=ProgramacionAreaPaciente.indicesListadoOrdenesMedicas;
	String indicesProgramacionAdmin[]=ProgramacionAreaPaciente.indicesProgramacionAdmin;
	
	@Override
	public ActionForward execute(   ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response ) throws Exception
	{
		
		Connection con = null;
		try{
			if(form instanceof ProgramacionAreaPacienteForm)
			{
				
				con = UtilidadBD.abrirConexion();
				
				//se verifica si la conexion esta nula
				if(con == null)
				{	
					// de ser asi se envia a una pagina de error. 
					request.setAttribute("CodigoDescripcionError","errors.problemasBd");
					return mapping.findForward("paginaError");
				}
				
				
				//se obtiene el usuario cargado en sesion.
				UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				
				//se obtiene el paciente cargado en sesion.
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				
				//se obtiene la institucion
				InstitucionBasica institucion = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
				
				//se instancia la forma
				ProgramacionAreaPacienteForm forma = (ProgramacionAreaPacienteForm)form;		
				
				//se instancia el mundo
				ProgramacionAreaPaciente mundo = new ProgramacionAreaPaciente();
				
				//optenemos el estado que contiene la forma.
				String estado = forma.getEstado();
				
				//se instancia la variable para manejar los errores.
				ActionErrors errores=new ActionErrors();
				
				forma.setMensaje(new ResultadoBoolean(false));
				
				
				
				
				logger.info("\n\n***************************************************************************");
				logger.info(" 	  EL ESTADO DE PROGRAMACION ADMINISTRACION DE MEDICAMENTOS ES ====>> "+estado);
				logger.info("\n***************************************************************************");
									
			
				// ESTADO --> NULL
				if(estado == null)
				{
					forma.reset(usuario.getCodigoInstitucionInt());
					request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
					UtilidadBD.cerrarConexion(con);
					 
					return mapping.findForward("paginaError");
				}			
				else 
					//ESTADO --> EMPEZARAREA
					if(estado.equals("empezarArea"))
					{
						forma.resetXPaciente();
						forma.setEsArea(ConstantesBD.acronimoSi);
						return this.accionEmpezarArea(forma, mundo, con, mapping, usuario);					   			
					}
					else 
						//ESTADO --> LISTAR HABITACIONES
						if(estado.equals("llenarHabitaciones"))
						{
							return this.accionLlenarHabitaciones(forma, mundo, con, mapping, usuario,1);					   			
						}
						else 
							//ESTADO --> BUSCAR PACIENTES
							if(estado.equals("buscarPacientes"))
							{
								return this.accionBuscarPacientes(forma, mundo, con, mapping, usuario, 1);					   			
							}
							else 
								//ESTADO --> EMPEZAR CONSULTA AREA
								if(estado.equals("empezarConsultaArea"))
								{
									return this.accionEmpezarConsultaArea(forma, mundo, con, mapping, usuario);					   			
								}
								else 
									//ESTADO --> LISTAR HABITACIONES DESDE CONSULTA
									if(estado.equals("llenarHabitacionesConsulta"))
									{
										return this.accionLlenarHabitaciones(forma, mundo, con, mapping, usuario,2);					   			
									}
									else 
										//ESTADO --> BUSCAR PACIENTES DESDE CONSULTA AREA
										if(estado.equals("buscarPacientesConsulta"))
										{
											return this.accionBuscarPacientes(forma, mundo, con, mapping, usuario, 2);					   			
										}
										else
											/*----------------------------------------------
											 * ESTADO =================>>>>>  IMPRIMIR REPORTE DESDE AREA
											 ---------------------------------------------*/
											if(estado.equals("imprimirPorArea"))
								    		{    			
								    			return this.accionImprimirReporteArea(con, forma, paciente, mapping, request, usuario);    			
								    		}
				
				
/*****************************************************************************************************************************************
 * Estados para el manejo de la funcionalidad por Paciente.
 *****************************************************************************************************************************************/
						else
							/*----------------------------------------------
							 * ESTADO =================>>>>>  EMPEZARPACIENTE
							 ---------------------------------------------*/
							if(estado.equals("empezarPaciente"))
							{
								//Se valida que el paciente este cargado en session
								RespuestaValidacion resp = UtilidadValidacion.esValidoPacienteCargado(con, paciente);
								if(!resp.puedoSeguir)
									if (forma.getTieneErroresPopUp().equals(ConstantesBD.acronimoSi))
										return ComunAction.accionSalirCasoError(mapping, request, con, logger, resp.textoRespuesta, resp.textoRespuesta, true);
								
								//se formatean los valores de la forma
								forma.resetXPaciente();
								
								
								
								//se verifica si tiene permiso para administrar medicamentos
								String error = UtilidadValidacion.esEnfermera(usuario);
								if (error.isEmpty())
									forma.setPermisoAdministrar(true);
								else
									forma.setPermisoAdministrar(false);
								
								//se hace la consulta del listado
								forma.setOrdenesMedicamentos(mundo.consultarListadoOrdenesMedicas(con, paciente.getCodigoCuenta()+"", paciente.getCodigoPersona()+"", "programacion"));
								forma.setOrdenesMedicamentos(indicesListadoOrdenesMedicas[6], paciente.getCodigoCuenta());
								forma.setOrdenesMedicamentos(indicesListadoOrdenesMedicas[7], paciente.getCodigoPersona());
								
								return mapping.findForward("programacionPaciente");					   			
							}
							else/*----------------------------------------------
								 * ESTADO =================>>>>>  ORDENARDETALLE
								 ---------------------------------------------*/
								if (estado.equals("ordenarListadoOrdenesMedicamentos"))
								{
									forma.setOrdenesMedicamentos(mundo.accionOrdenarMapa(forma.getOrdenesMedicamentos(), forma,indicesListadoOrdenesMedicas));
									UtilidadBD.closeConnection(con);
									return mapping.findForward("programacionPaciente");	
								}
								else /*----------------------------
									 * 	ESTADO ==> REDIRECCION
									 ----------------------------*/
									if (estado.equals("redireccion"))
									{
										UtilidadBD.closeConnection(con);
										response.sendRedirect(forma.getLinkSiguiente());
										return null;
									}
									else /*----------------------------
										 * 	ESTADO ==> PROGRAMACIONADMINISTRACION
										 ----------------------------*/
										if (estado.equals("programacionAdmin"))
										{
											mundo.organizarMedicamentosAdmistrar(forma);
											return mapping.findForward("programacionAdmin");	
										}
										else
											/*----------------------------------------------
											 * ESTADO =================>>>>>  EMPEZARPACIENTE LLAMADO DESDE AREAS
											 ---------------------------------------------*/
											if(estado.equals("empezarPacienteDesdeArea"))
											{
												String error = UtilidadValidacion.esEnfermera(usuario);
												if (error.isEmpty())
													forma.setPermisoAdministrar(true);
												else
													forma.setPermisoAdministrar(false);
												forma.setEsArea(ConstantesBD.acronimoSi);
												//se hace la consulta del listado llamandolo desde Area
												forma.setOrdenesMedicamentos(mundo.consultarListadoOrdenesMedicas(con, forma.getCodigoCuentaA()+"", forma.getCodigoPacienteA()+"","programacion"));
												
												return mapping.findForward("programacionPaciente");
											}
											else
												/*----------------------------------------------
												 * ESTADO =================>>>>>  PROGRMARREPROGRAMAR
												 ---------------------------------------------*/
												if(estado.equals("programarReprogramar"))
												{
													errores=validarProg(forma);
													
													if (!errores.isEmpty())
													{
														saveErrors(request,errores);
														forma.setTieneErroresPopUp(ConstantesBD.acronimoSi);
														UtilidadBD.closeConnection(con);
														return mapping.findForward("programacionAdmin");
													}
													forma.setTieneErroresPopUp(ConstantesBD.acronimoNo);
													mundo.guardar(con, forma, usuario,paciente);
													UtilidadBD.closeConnection(con);
													return mapping.findForward("programacionAdmin");
												}
												/*----------------------------------------------
												 * ESTADO =================>>>>>  VERPROGRAMACION
												 ---------------------------------------------*/
												if(estado.equals("verProgramacion"))
												{
													forma.setMostrarProgramacionAdmin(mundo.consultarProgramacionAdmin(con, Utilidades.convertirAEntero(forma.getCodigoAdmin()),ConstantesBD.acronimoNo));													
													return mapping.findForward("mostrarProgramacionAdmin");
												}
												else
													/*----------------------------------------------
													 * ESTADO =================>>>>>  EMPEZAR CONSULTA PACIENTE
													 ---------------------------------------------*/
													if(estado.equals("empezarConsultaPaciente"))
													{
														//Se valida que el paciente este cargado en session
														RespuestaValidacion resp = UtilidadValidacion.esValidoPacienteCargado(con, paciente);
														if(!resp.puedoSeguir)
															return ComunAction.accionSalirCasoError(mapping, request, con, logger, resp.textoRespuesta, resp.textoRespuesta, true);
														
														//se formatean los valores de la forma
														forma.resetXPaciente();
														
														//se hace la consulta del listado
														forma.setOrdenesMedicamentos(mundo.consultarListadoOrdenesMedicas(con, paciente.getCodigoCuenta()+"", paciente.getCodigoPersona()+"","consulta"));
														forma.setOrdenesMedicamentos(indicesListadoOrdenesMedicas[6], paciente.getCodigoCuenta());
														forma.setOrdenesMedicamentos(indicesListadoOrdenesMedicas[7], paciente.getCodigoPersona());
														
														//se verifica si tiene permiso para administrar medicamentos
														String error = UtilidadValidacion.esEnfermera(usuario);
														if (error.isEmpty())
															forma.setPermisoAdministrar(true);
														else
															forma.setPermisoAdministrar(false);
														
														return mapping.findForward("consultaProgramacionPaciente");					   			
													}
													else
														/*----------------------------------------------
														 * ESTADO =================>>>>>  EMPEZARPACIENTE LLAMADO DESDE CONSULTA AREAS
														 ---------------------------------------------*/
														if(estado.equals("empezarPacienteDesdeConsultaArea"))
														{
															//se hace la consulta del listado llamandolo desde Area
															forma.setOrdenesMedicamentos(mundo.consultarListadoOrdenesMedicas(con, forma.getCodigoCuentaA()+"", forma.getCodigoPacienteA()+"","consulta"));
															
															return mapping.findForward("consultaProgramacionPaciente");
														}
														else
															/*----------------------------------------------
															 * ESTADO =================>>>>>  IMPRIMIR REPORTE DESDE PACIENTE
															 ---------------------------------------------*/
															if(estado.equals("imprimirPorPaciente"))
												    		{    			
												    			return this.accionImprimirReportePaciente(con, forma, paciente, mapping, request, usuario);    			
												    		}
/*****************************************************************************************************************************************
 * Fin de Estados para el manejo de la funcionalidad por Paciente.
 *****************************************************************************************************************************************/
				
				
				
			}
		}catch (Exception e) {
			Log4JManager.error(e);
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
		return null;
	}
	
	
	//########################################  PACIENTE  #####################################################
	
	/**
	 * Metodo encargado de validar los datos requeridos de la programacion
	 */
	public ActionErrors validarProg (ProgramacionAreaPacienteForm forma)
	{
		ActionErrors errors = new ActionErrors();
		String hora=UtilidadFecha.getHoraActual();
		String fecha=UtilidadFecha.getFechaActual();
		
		
		
		//fecha no vacia
		if (!UtilidadCadena.noEsVacio(forma.getProgramacionAdmin(indicesProgramacionAdmin[6])+""))
			errors.add("descripcion",new ActionMessage("errors.required","La Fecha de programación"));
		else //formato fecha
			if(!UtilidadFecha.validarFecha(forma.getProgramacionAdmin(indicesProgramacionAdmin[6])+""))
				errors.add("formato fecha invalido", new ActionMessage("errors.formatoFechaInvalido","de programación "+forma.getProgramacionAdmin(indicesProgramacionAdmin[6])));
		
		//hora no vacia
		if (!UtilidadCadena.noEsVacio(forma.getProgramacionAdmin(indicesProgramacionAdmin[7])+""))
			errors.add("descripcion",new ActionMessage("errors.required","La Hora de programación"));
		else// formato hora
			if(!UtilidadFecha.validacionHora(forma.getProgramacionAdmin(indicesProgramacionAdmin[7])+"").puedoSeguir)
				errors.add("hora", new ActionMessage("errors.formatoHoraInvalido", "de programación "+forma.getProgramacionAdmin(indicesProgramacionAdmin[7])));

		if (errors.isEmpty())//se valida que sea menor que el sistema
			if (!UtilidadFecha.compararFechas(forma.getProgramacionAdmin(indicesProgramacionAdmin[6])+"" ,forma.getProgramacionAdmin(indicesProgramacionAdmin[7])+"",fecha, hora).isTrue())
					errors.add("descripcion",new ActionMessage("errors.fechaHoraAnteriorIgualActual","de la programación "+forma.getProgramacionAdmin(indicesProgramacionAdmin[6])+"  "+forma.getProgramacionAdmin(indicesProgramacionAdmin[7]),"del sistema "+fecha+" "+ hora));
		
		return errors;
	}
	
	
	/**
	 * Metodo para realizar la impresion por paciente
     * @param con
     * @param forma
     * @param mapping
     * @param request
     * @param usuario
     * @return
     */
    private ActionForward accionImprimirReportePaciente(Connection con, ProgramacionAreaPacienteForm forma, PersonaBasica paciente, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) {
    	String nombreRptDesign = "ConsultaProgramacionPaciente.rptdesign";
    	String nitInstitucion;
    	String codCuenta="", codPersona="";
    	
    	if(paciente.getCodigoCuenta() <= 0 || paciente.getCodigoPersona() <=0 )
    	{
    		codCuenta=forma.getCodigoCuentaA()+"";
    		codPersona=forma.getCodigoPacienteA()+"";
    	}
    	else
    	{
    		codCuenta=paciente.getCodigoCuenta()+"";
    		codPersona=paciente.getCodigoPersona()+"";
    	}
    	
    	String consulta =" SELECT getconsecutivosolicitud (ds.numero_solicitud) As numero_orden, " +
    			" getconsecutivoingreso (c.id_ingreso) AS sol, " +
    			" getfechaingreso(c.id,c.via_ingreso) AS fechaing, " +
    			" getnombrepersona(c.codigo_paciente) AS nombrep," +
			   " getCodArticuloAxiomaInterfaz(ds.articulo,'"+ConstantesIntegridadDominio.acronimoAxioma+"')||' '||getdescripcionarticulo(ds.articulo) As medicamento," +			   
			   " ds.dosis || ' ' || getunimedidaunidosisxunidad(ds.unidosis_articulo) As dosis, " +
			   " ds.via As via, " +
			   " ds.frecuencia || ' ' || ds.tipo_frecuencia As frecuencia," +
			   " getfechahorainiprog(ds.numero_solicitud,ds.articulo) AS fechahora," +
			   " getusuarioprograma(ds.numero_solicitud,ds.articulo) AS usuario, " +
			   " p.observaciones AS obs, " +
			   " getFechasHorasProgramacion(ds.numero_solicitud,ds.articulo) AS fechashoras " +
		   " FROM detalle_solicitudes ds " +
			   " INNER JOIN solicitudes_medicamentos sm ON (sm.numero_solicitud=ds.numero_solicitud)" +
			   " INNER JOIN solicitudes s ON (s.numero_solicitud=ds.numero_solicitud) " +
			   " INNER JOIN cuentas c ON (s.cuenta=c.id) " +
			   " INNER JOIN programacion_admin p ON (ds.numero_solicitud=p.numero_solicitud AND ds.articulo=p.articulo) " +
		   " WHERE  s.cuenta="+ codCuenta +" " +
	   		//con esta funcion se verifica que el ingreso esta abierto, 
	   		//y que la cuenta este en los estados indicados (que sea cuenta valida)	
		   		" AND getcuentavalidaxpaci("+ codPersona +")>=1 " +
		   		//se verifica que el estado de la solicitud sea solicitada o despachada
		   		" AND (" +
		   				"(getcodigoestadohcsol(ds.numero_solicitud)="+ConstantesBD.codigoEstadoHCDespachada+" AND (coalesce(getdespacho(ds.articulo,ds.numero_solicitud),0)-coalesce(gettotaladminfarmacia(ds.articulo,ds.numero_solicitud,"+ValoresPorDefecto.getValorFalseParaConsultas()+"),0))<>0 )" +
		   				"OR " +
		   				"(getcodigoestadohcsol(ds.numero_solicitud)="+ConstantesBD.codigoEstadoHCSolicitada+" AND (coalesce(getdespacho(ds.articulo,ds.numero_solicitud),0)-coalesce(gettotaladminfarmacia(ds.articulo,ds.numero_solicitud,"+ValoresPorDefecto.getValorFalseParaConsultas()+"),0))<>0 )" +
		   			 ")" +
		   		" AND getnumeroprogramaciones(ds.numero_solicitud,ds.articulo)>0" +
	   		" ORDER BY getdescripcionarticulo(ds.articulo) " ;
    	
    	logger.info("\n\nCONSULTA PACIENTE REPORTE>>>>>>>>>"+consulta);
    	
    	InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
    	
    	if(Utilidades.convertirAEntero(ins.getDigitoVerificacion()) != ConstantesBD.codigoNuncaValido)
        	nitInstitucion = Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+". "+ins.getNit()+" - "+ins.getDigitoVerificacion();
        else
        	nitInstitucion = Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+". "+ins.getNit();
		
		//Informacion del Cabezote
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"enfermeria/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 2, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,5);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(nitInstitucion);
        v.add(ins.getDireccion());
        v.add("Tels. "+ins.getTelefono());
        v.add("PROGRAMACION ADMINISTRACION DE MEDICAMENTOS POR PACIENTE");
        comp.insertLabelInGridOfMasterPage(0,1,v);
        comp.insertLabelInGridPpalOfFooter(1, 0, usuario.getLoginUsuario());
        
        comp.obtenerComponentesDataSet("ConsultaProgramacionPaciente");
        comp.modificarQueryDataSet(consulta);
         
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
         
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
         	request.setAttribute("newPathReport", newPathReport);
        }
    	
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("consultaProgramacionPaciente");
    }
    
    /**
	 * Metodo para realizar la impresion por Area
     * @param con
     * @param forma
     * @param mapping
     * @param request
     * @param usuario
     * @return
     */
    private ActionForward accionImprimirReporteArea(Connection con, ProgramacionAreaPacienteForm forma, PersonaBasica paciente, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario) {
    	String newQuery=SqlBaseProgramacionAreaPacienteDao.cadenaConsultaPaciente;
    	String nombreRptDesign = "ConsultaProgramacionArea.rptdesign";
    	String nitInstitucion;
    	
    	logger.info("\n\nCONSULTA REPORTE AREA>>>>>>>>>>>>>"+newQuery);
    	
    	InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
    	
    	if(Utilidades.convertirAEntero(ins.getDigitoVerificacion()) != ConstantesBD.codigoNuncaValido)
        	nitInstitucion = Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+". "+ins.getNit()+" - "+ins.getDigitoVerificacion();
        else
        	nitInstitucion = Utilidades.getDescripcionTipoIdentificacion(con,ins.getTipoIdentificacion())+". "+ins.getNit();
		
		//Informacion del Cabezote
        DesignEngineApi comp; 
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"enfermeria/",nombreRptDesign);
        comp.insertImageHeaderOfMasterPage1(0, 2, ins.getLogoReportes());
        comp.insertGridHeaderOfMasterPage(0,1,1,5);
        Vector v=new Vector();
        v.add(ins.getRazonSocial());
        v.add(nitInstitucion);
        v.add(ins.getDireccion());
        v.add("Tels. "+ins.getTelefono());
        v.add("PROGRAMACION ADMINISTRACION DE MEDICAMENTOS POR AREA");
        comp.insertLabelInGridOfMasterPage(0,1,v);
        comp.insertLabelInGridPpalOfFooter(1, 0, usuario.getLoginUsuario());
        
        String criterios="";
        if(!forma.getFechaIniProg().equals(""))
        	criterios+="Fecha Programación: "+forma.getFechaIniProg()+" ";
        if(!forma.getHoraIniProg().equals(""))
        	criterios+="Hora Programación: "+forma.getHoraIniProg()+" ";
        if(!forma.getArea().equals("-1"))
        	criterios+="Area: "+Utilidades.obtenerNombreCentroCosto(Utilidades.convertirAEntero(forma.getArea()), usuario.getCodigoInstitucionInt())+" ";
        if(!forma.getPiso().equals("-1"))
        	criterios+="Piso: "+Utilidades.obtenerNombrePiso(con, forma.getPiso())+" ";
        String nomHabitacion="";
        if(!forma.getHabitacion().equals("-1"))
        {
        	for(int i=0;i<Utilidades.convertirAEntero(forma.getHabitacionesMap("numRegistros")+"");i++)
        	{
        		if(forma.getHabitacion().equals(forma.getHabitacionesMap("codigo_"+i).toString()))
        			nomHabitacion=forma.getHabitacionesMap("nombre_"+i).toString();
        	}
        	criterios+="Habitación: "+nomHabitacion+" ";
        }
        comp.insertLabelInGridPpalOfHeader(5, 0, criterios);
        
        comp.obtenerComponentesDataSet("ConsultaProgramacionArea");
        comp.modificarQueryDataSet(newQuery);
         
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        comp.updateJDBCParameters(newPathReport);
         
        if(!newPathReport.equals(""))
        {
        	request.setAttribute("isOpenReport", "true");
         	request.setAttribute("newPathReport", newPathReport);
        }
    	
    	UtilidadBD.closeConnection(con);
		return mapping.findForward("consultaProgramacionArea");
    }
	
	//######################################## FIN PACIENTE  #####################################################
	
	//########################################  AREAS  #####################################################
	
	/**
     * Inicia en el forward de Programacion por Area 
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionEmpezarArea(ProgramacionAreaPacienteForm forma, ProgramacionAreaPaciente mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.reset(usuario.getCodigoInstitucionInt());
		forma.setAreasMap(mundo.listarAreas(con, usuario.getCodigoCentroAtencion()));
		forma.setPisosMap(mundo.listarPisos(con, usuario.getCodigoCentroAtencion()));
			
		UtilidadBD.closeConnection(con);
		return mapping.findForward("programacionArea");
		
	}
	
	/**
     * Inicia en el forward de Consulta Programacion por Area 
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionEmpezarConsultaArea(ProgramacionAreaPacienteForm forma, ProgramacionAreaPaciente mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario) 
	{
		forma.reset(usuario.getCodigoInstitucionInt());
		forma.setAreasMap(mundo.listarAreas(con, usuario.getCodigoCentroAtencion()));
		forma.setPisosMap(mundo.listarPisos(con, usuario.getCodigoCentroAtencion()));
			
		UtilidadBD.closeConnection(con);
		return mapping.findForward("consultaProgramacionArea");
	}
	
	
	/**
     * Llena las Habitaciones segun el Piso Seleccionado 
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionLlenarHabitaciones(ProgramacionAreaPacienteForm forma, ProgramacionAreaPaciente mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario, int remite) 
	{		
		forma.setHabitacionesMap(mundo.listarHabitaciones(con, Utilidades.convertirAEntero(forma.getPiso())));
			
		UtilidadBD.closeConnection(con);
		if(remite==1)
			return mapping.findForward("programacionArea");
		return mapping.findForward("consultaProgramacionArea");
		
	}
	
	/**
     * Buscar los Pacientes filtrados por parametros 
     * @param forma
     * @param con
     * @param mapping
     * @return
     */
	private ActionForward accionBuscarPacientes(ProgramacionAreaPacienteForm forma, ProgramacionAreaPaciente mundo, Connection con, ActionMapping mapping, UsuarioBasico usuario, int remite) 
	{
		if(remite==2)
			forma.setPacientesMap(mundo.ConsultaPacientes(con, forma.getFechaIniProg(), forma.getFechaFinProg(), forma.getArea(), forma.getPiso(), forma.getHabitacion(), forma.getCheckP(), forma.getHoraIniProg(), forma.getHoraFinProg(), 1));
		else
			forma.setPacientesMap(mundo.ConsultaPacientes(con, forma.getFechaIniOrden(), forma.getFechaFinOrden(), forma.getArea(), forma.getPiso(), forma.getHabitacion(), forma.getCheckP(), forma.getFechaIniProg(), forma.getFechaFinProg(), 2));
			
		UtilidadBD.closeConnection(con);
		if(remite==2)
			return mapping.findForward("consultaProgramacionArea");
		return mapping.findForward("programacionArea");
		
	}
	
	//###################################### FIN AREAS ###########################################################
	
	
		
	
	
}