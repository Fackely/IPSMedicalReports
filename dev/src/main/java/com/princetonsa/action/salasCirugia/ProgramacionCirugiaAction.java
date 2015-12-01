/*
 * Creado el 21-nov-2005
 * por Julian Montoya
 */
package com.princetonsa.action.salasCirugia;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Observable;
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
import util.Listado;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.salasCirugia.ProgramacionCirugiaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.ordenesmedicas.cirugias.SolicitudesCx;
import com.princetonsa.mundo.salasCirugia.Peticion;
import com.princetonsa.mundo.salasCirugia.ProgramacionCirugia;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;

/**
 * @author Julian Montoya
 * 
 * Princeton S.A. (ParqueSoft Manizales)
 */
public class ProgramacionCirugiaAction extends Action {
	
	
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(PreanestesiaAction.class);
	
	
	/**
	 * Método encargado de el flujo y control de la funcionalidad
	 * 
	 * @see org.apache.struts.action.Action#execute(ActionMapping, ActionForm, HttpServletRequest, HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping,
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response ) throws Exception
	{
		Connection con=null;
		try{
		if (response==null); //Para evitar que salga el warning
		
		if(form instanceof ProgramacionCirugiaForm)
		{
			
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}
				
 
				
				ProgramacionCirugiaForm forma = (ProgramacionCirugiaForm)form;
				HttpSession session=request.getSession();
				
				String estado = forma.getEstado(); 
				logger.warn("En ProgramacionCirugiaAction Estado   [    "+estado+"   ]\n\n");
				
				//-Aqui se debe recibir por el request el numero de la petición 
				int nroPeticion = forma.getNumeroPeticion();	
				
				
				

				
				if(estado == null)
				{
						forma.reset();	
						logger.warn("Estado no valido dentro del flujo de registro de programacion de Cirugias (null) ");
						request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
						UtilidadBD.cerrarConexion(con);
						return mapping.findForward("paginaError");
				}
				
				UsuarioBasico usuario = (UsuarioBasico)session.getAttribute("usuarioBasico");
				PersonaBasica paciente = (PersonaBasica)session.getAttribute("pacienteActivo");
				
				/**
				 * Se verifica que sea un usuario activo de la salud (Tarea de Xplanner 14124)
				 */
				if (!UtilidadValidacion.esUsuarioActivo(con, usuario.getLoginUsuario(), usuario.getCodigoInstitucionInt()) )
				{
				    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Usuario Inactivo", "error.salasCirugia.noActivoUsuario", true) ;
				}
			

				if (estado.equals("menu"))
				{
					forma.setEstado("menu");
					forma.setEsProgramacion(true);
					
					UtilidadBD.cerrarConexion(con);
					return mapping.findForward("menu");
				} 
				else if( usuario == null )
				{
				    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No existe el usuario", "errors.usuario.noCargado", true);
				}
				else if (estado.equals("paciente"))
				{
					if( paciente == null || paciente.getTipoIdentificacionPersona().equals("") )
					{
					    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "paciente null o sin  id", "errors.paciente.noCargado", true);
					}
					else
					{
						forma.setEstadoAnterior("paciente"); 
						return accionConsultarPeticionesPaciente(forma, mapping, con, paciente, usuario );
					}
				}
				else if (estado.equals("peticion"))
				{
					forma.setEstadoAnterior("peticion"); 
				    return this.accionConsultarPeticionesGenerales(forma, mapping, con, nroPeticion, usuario, paciente);
				}
				else if (estado.equals("empezar"))
				{					
					if ( forma.getEstadoAnterior().equals("peticion") )
					{
						//-Se debe  Cargar el paciente..
						ProgramacionCirugia mundo = new ProgramacionCirugia();
						
						//-Debe buscar el codigo de la persona en la BD 
						int codigoPersona = mundo.cargarCodigoPersona(con, nroPeticion);   
					
						PersonaBasica pb=new PersonaBasica();
						pb.cargar(con,codigoPersona); 
						pb.cargarPaciente2(con, codigoPersona, usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
					
						// Código necesario para registrar este paciente como Observer
						Observable observable = (Observable) this.servlet.getServletContext().getAttribute("observable");
						if (observable != null) 
						{
							pb.setObservable(observable);
							// Si ya lo habíamos añadido, la siguiente línea no hace nada
							observable.addObserver(pb);
						}

						//subir paciente a sesion
						request.getSession().setAttribute("pacienteActivo",pb);
						paciente =(PersonaBasica) request.getSession().getAttribute("pacienteActivo");
					}
					
					forma.setNroFilasGrilla(nroFilasGrilla(usuario.getCodigoInstitucionInt()));					
				    return this.accionEmpezar(forma, mapping, con, usuario, nroPeticion, "empezar", forma.getEstadoAnterior(), 0);
				}
				else if(estado.equals("salir"))
				{
				    return this.accionSalir(forma, mapping, con, usuario.getLoginUsuario(), paciente, usuario);
				}
				else if(estado.equals("ordenar"))  ///-El Ordenar de las Peticiones de un paciente
				{
				    return this.accionOrdenar(forma, mapping, con, "listaPeticionesPaciente");
				}
				else if(estado.equals("ordenarGeneral")) //-el ordenar general de todas las peticiones de los pacientes
				{
				    return this.accionOrdenar(forma, mapping, con, "listaPeticionesGenerales");
				}
				else if(estado.equals("busqueda")) //-La Busqueda para las programadas
				{
					return this.accionBusquedaAvanzada(forma, mapping, con);
				}
				else if(estado.equals("resultadoBusquedaPeticiones"))
				{
					if (forma.getEsProgramacion())
					{
						return this.accionResultadoBusquedaAvanzada(forma, mapping, con, response, request);
					}
					else
					{
						return this.accionResultadoBusquedaAvanzadaProgramadas(forma, mapping, con, response, request);
					}
				}
				else if(estado.equals("resumen"))
				{
				    return this.accionResumen(forma, mapping, con);
				}
				else if(estado.equals("navegar"))
				{
					 return this.accionEmpezar(forma, mapping, con, usuario, nroPeticion, "navegar", forma.getEstadoAnterior(), 0);
				}
				else if(estado.equals("listaPeticionesProgramadas"))
				{	
					forma.setEstadoAnterior("listaPeticionesProgramadas");
					forma.setEsProgramacion(false);
					forma.setEstadoInsercion(0); //-Para no mostrar ningun mensaje en el jsp cuando se listen las peticiones por primera vez 
					return this.accionListaPeticionesProgramadas(forma, mapping, con, usuario);
				}
				else if(estado.equals("busquedaPeticionesProgramadas")) //-Busqueda Prog....
				{					
				    return this.accionBusquedaAvanzada(forma, mapping, con);
				}
				else if(estado.equals("buscarPeticionesProgramadas"))
				{
				    return this.accionBusquedaAvanzadaPeticionesProgramadas(forma, mapping, con);
				}
				else if(estado.equals("ordenarProgramadas")) 
				{
				    return this.accionOrdenarProgramadas(forma, mapping, con);
				}
				else if(estado.equals("eliminarProgramacion")) 
				{
				    return this.accionEliminarProgramacion(forma, mapping, con, usuario);
				}
				else if(estado.equals("cancelarProgramacion"))
				{
					return this.accionCancelarProgramacion(forma, mapping, con, usuario);
				}
				else if(estado.equals("reprogramacion")) 
				{
					forma.setNroFilasGrilla(nroFilasGrilla(usuario.getCodigoInstitucionInt()));
					forma.setEstadoAnterior("listaPeticionesProgramadas");
					return this.accionEmpezar(forma, mapping, con, usuario, nroPeticion, "reprogramacion", forma.getEstadoAnterior(),0);
				}
				else if(estado.equals("guardarReprogramacion")) 
				{
					return this.accionSalirReprogramacion(forma, mapping, con, usuario);
				}
				//---------------------------------------------------------------------------------------- 
				//---------------------------------------------------------------------------------------- 
				//---------------------------------------------------------------------------------------- 
				//---------------------------------------------------------------------------------------- 
				//---------Estados Para La Funcionalidad de CONSULTA de Programación de Cirugias. 
				//---------------------------------------------------------------------------------------- 
				//---------------------------------------------------------------------------------------- 
				//---------------------------------------------------------------------------------------- 
				//---------------------------------------------------------------------------------------- 
				else if(estado.equals("menuConsultar")) 
				{
					return this.accionConsultarEmpezar(forma, mapping, con, usuario.getLoginUsuario());
				}
				else if(estado.equals("consultarPaciente")) 
				{
					if( paciente == null || paciente.getTipoIdentificacionPersona().equals("") )
					{
					    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "paciente null o sin  id", "errors.paciente.noCargado", true);
					}
					else
					{
						if ( paciente.getCodigoUltimaViaIngreso() != ConstantesBD.codigoViaIngresoConsultaExterna )
						{
							return this.accionConsultarPaciente(forma, mapping, con, paciente.getCodigoPersona());
						}
						else
						{
						    //-return ComunAction.accionSalirCasoError(mapping, request, con, logger, "paciente null o sin  id", "error.salasCirugia.viaAmbulatorio", true);
						    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "paciente null o sin  id", "error.salasCirugia.viaNoHospitalizacionNoAmbulatorio", true);
						}
					}
				}
				else if(estado.equals("ordenarListadoCirugias")) 
				{
					return this.accionOrdenarPeticiones(forma, mapping, con);
				}
				else if(estado.equals("consultarRango")) 
				{
					return this.accionBusquedaConsulta( mapping, con);
				}
				else if(estado.equals("buscarConsultar")) 
				{
					return this.accionBuscarConsultar(forma, mapping, con);
				}
				else if(estado.equals("ordenarListadoCirugiasRangos"))
				{
					return this.accionOrdenarPeticionesRangos(forma, mapping, con);
				}
				else if(estado.equals("generarReportePaciente"))
				{
					return this.accionGenerarReportePaciente(forma, mapping, con, request, paciente, usuario);
				}
				else if(estado.equals("generarReporteRangos"))
				{
					return this.accionGenerarReporteRangos(forma, mapping, con, request, paciente, usuario);
				}
				//Redirecciona el flujo a la funcionalidad de Pedido Quirurgico
				else if(estado.equals("irPedidoQuirurgico"))
				{
					accionRedireccionarFlujoFuncionalidad(forma,response);					
				}
				else
				{
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
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
	 * 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 */
	private ActionForward accionCancelarProgramacion(ProgramacionCirugiaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) {
		
		ProgramacionCirugia mundo = new ProgramacionCirugia();
		//-Eliminar la programacion
		HashMap mapaP=new HashMap();
		mapaP.put("numRegistros", 0);
		
		//se consulta el registro que se va a replicar
		mapaP=mundo.consultaProgramacionSalasQx(con, forma.getNumeroPeticion());
		//se obtiene el motivo del popup
		mapaP.put("motivo", forma.getMotivoCancelacion());
		//se obtiene el usuario responsable de la operacion "cancelacion"
		mapaP.put("usuario", usuario.getLoginUsuario());
		//se inserta el nuevo registro en cancelacion_prog_salas_qx
		int x=mundo.insertarCancelacionProgramacionSalasQx(con,mapaP);
		
		if(x>0)
		{
			//se elimina el registro de la tabla programacion_salas_qx
			forma.setEstadoInsercion( mundo.eliminarProgramacion(con, forma.getNumeroPeticion()) );
		}
		
		try{
			return accionListaPeticionesProgramadas(forma, mapping, con, usuario);	
		}
		catch(SQLException e){
			logger.info("\n\n\n\n << ERROR EN LA CANCELACION DE PROGRAMACION >> \n\n\n\n"+e);
		}
		return null;

	}


	/**
	 * Metodo para generar reporte del listado de peticiones programadas resultado de la busqueda abanzada.
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param request 
	 * @param paciente 
	 * @param usuario 
	 * @return
	 * @throws SQLException 
	 */

	private ActionForward accionGenerarReporteRangos(ProgramacionCirugiaForm forma, ActionMapping mapping, Connection con, HttpServletRequest request, PersonaBasica paciente, UsuarioBasico usuario) throws SQLException 
    {
    	
        DesignEngineApi comp;
        InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"interfaz/","programacionRangos.rptdesign");
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        Vector v=new Vector();
        

        v.add("                 "); v.add("  Institución	 : " + usuario.getInstitucion() ); 	 v.add("                 ");		
        v.add("                 "); v.add("  				   ");		 						 v.add("                 ");
        v.add("                 "); v.add("  				   ");		 						 v.add("                 ");		

        boolean entro = false; int nroFilas = 3;
        //-Si se hizo la busqueda por fechas insertarlas en el reporte.
        if ( !forma.getFechaIniCirugia().trim().equals("") || !forma.getFechaFinCirugia().trim().equals("") )
        {
        	v.add("                 ");	v.add("Parametros de Busqueda ");  v.add("                 ");
			v.add("				    "); v.add("                		  ");  v.add("                 ");
        	v.add("                 "); v.add("Fecha Inicial Cirugía: " + forma.getFechaIniCirugia() ); v.add("Fecha Final Cirugía: " + forma.getFechaFinCirugia());
        	
        	//v.add("Fecha Final   Cirugía:  ");		v.add("Fecha Final Cirugía: " + forma.getFechaFinCirugia() );  
        	entro = true; nroFilas+=2;
        }
        if ( !forma.getFechaIniPeticion().trim().equals("") || !forma.getFechaFinPeticion().trim().equals("") )
        {
        	if (!entro) 
        	{
            	v.add("                 ");	v.add("Parametros de Busqueda ");  v.add("                 ");
    			v.add("				    "); v.add("                		  ");  v.add("                 ");
	        	nroFilas+=2;
        	}
        	v.add("                        "); v.add("Fecha Inicial Petición: " +  forma.getFechaIniPeticion() ); v.add("Fecha Final Petición: " + forma.getFechaFinPeticion() ); 
        	//v.add("Fecha Final	 Petición:  "); v.add( forma.getFechaFinPeticion() );
        	nroFilas+=2;
        }

        
        
        
        //-En la columna 1 Fila 0 del Grid de Encabezado se insertara una tabla de 3 columna y 3 Filas 
        comp.insertGridHeaderOfMasterPage(0, 1, 3, nroFilas);
        comp.insertLabelInGridOfMasterPage(0, 1, v);
      
        
        //-----Barrer el Mapa e insertarlo en un vector.
        HashMap mp = forma.getMapaPeticiones();
        
    	int nroReg = 0;
		//-Colocar los cirujanos
		if ( UtilidadCadena.noEsVacio( mp.get("numRegistros") +"" ) ) { nroReg = Integer.parseInt( mp.get("numRegistros") +"" );	}
		
        
        String columnas [] = {"sala_","codigo_peticion_", "paciente_", "fecha_cirugia_", "hora_cirugia_",  "serviciof_",
        					  "cirujanof_", "tipo_anestesia_", "anestesiologof_", "observacionesf_", "empresa_"};

        //-Insertar la informacion en el Mapa del Reporte.
        comp.insertarMapaEnReporte(columnas, forma.getMapaPeticiones(), 1);        
        
	    //--Insertar EL Nombre Del Usuario Que Imprimio El Reporte. 
	    comp.insertarUsuarioImprimio(usuario.getNombreUsuario());
	    
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        request.setAttribute("isOpenReport", "true");
        request.setAttribute("newPathReport", newPathReport);
        comp.updateJDBCParameters(newPathReport);
        
        UtilidadBD.cerrarConexion(con);
        return mapping.findForward("listadoConsultaRangos");
    }

	
	/**
	 * Metodo para generar reporte del listado de peticiones programadas del paciente.
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param request 
	 * @param paciente 
	 * @param usuario 
	 * @return
	 * @throws SQLException 
	 */
    private ActionForward accionGenerarReportePaciente(ProgramacionCirugiaForm forma, ActionMapping mapping, Connection con, HttpServletRequest request, PersonaBasica paciente, UsuarioBasico usuario) throws SQLException 
    {
        DesignEngineApi comp;        
        comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"interfaz/","programacion.rptdesign");
        InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
        comp.insertImageHeaderOfMasterPage1(0, 0, ins.getLogoReportes());
        
        //-En la columna 1 Fila 0 del Grid de Encabezado se insertara una tabla de 1 columna y 4 Filas 
        comp.insertGridHeaderOfMasterPage(0, 1, 2, 3);

        Vector v=new Vector();
        v.add("                 "); v.add("  Institución	 : " + usuario.getInstitucion() ); 			
        v.add("                 "); v.add("  Paciente   	 : " + paciente.getNombrePersona() );		 
        v.add("                 "); v.add("  Identificación  : " +  paciente.getCodigoTipoIdentificacionPersona()  +"  " +  paciente.getNumeroIdentificacionPersona() ); v.add("                 ");
        comp.insertLabelInGridOfMasterPage(0, 1, v);
      
        //-----Barrer el Mapa e insertarlo en un vector.
        HashMap mp = forma.getMapaPeticiones();
        
    	int nroReg = 0;
		//-Colocar los cirujanos
		if ( UtilidadCadena.noEsVacio( mp.get("numRegistros") +"" ) ) { nroReg = Integer.parseInt( mp.get("numRegistros") +"" );	}
		
	
		/*String columnas [] = {"codigo_peticion_", "fecha_cirugia_", "hora_cirugia_", "serviciof_",  "cirujanof_",
        					  "tipo_anestesia_", "anestesiologof_", "observacionesf_", "empresa_"}; */
		
		String[] columnas = {"sala_","codigo_peticion_", "fecha_cirugia_", "hora_cirugia_",  "serviciof_", "cirujanof_", 
							 "tipo_anestesia_", "anestesiologof_", "observacionesf_", "empresa_"};
    
		
        //-- Insertar la informacion en el Mapa del Reporte.
        comp.insertarMapaEnReporte(columnas, forma.getMapaPeticiones(), 1);        
        
	    //-- Insertar EL Nombre Del Usuario Que Imprimio El Reporte. 
	    comp.insertarUsuarioImprimio(usuario.getNombreUsuario());
	    
        //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
        request.setAttribute("isOpenReport", "true");
        request.setAttribute("newPathReport", newPathReport);
        comp.updateJDBCParameters(newPathReport);
        UtilidadBD.cerrarConexion(con);
        return mapping.findForward("listadoConsultaPaciente"); 
    	
    }

	

	/**
	 * Metodo Para Ordenar el listado de peticiones del paciente
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionOrdenarPeticionesRangos(ProgramacionCirugiaForm forma, ActionMapping mapping, Connection con) throws SQLException
	{
		String[] indices = {"codigo_peticion_", "paciente_", "fecha_cirugia_", "hora_cirugia_",  
							"tipo_anestesia_", "empresa_", "sala_", "cirujanof_", "anestesiologof_",  "observacionesf_"};

		int nroReg = 0;
		if (UtilidadCadena.noEsVacio(forma.getMapaPeticiones("numRegistros")+""))
		{
			nroReg = Integer.parseInt( forma.getMapaPeticiones("numRegistros") + "");
		}
		
        forma.getMapaPeticiones().putAll(Listado.ordenarMapa(indices,
        											forma.getPatronOrdenar(),
										            forma.getUltimoPatron(),
										            forma.getMapaPeticiones(),
										            nroReg ));
        
        forma.setMapaPeticiones("numRegistros", nroReg+"");
        forma.setUltimoPatron(forma.getPatronOrdenar());
        
        UtilidadBD.cerrarConexion(con);
        return mapping.findForward("listadoConsultaRangos");
	}

	/**
	 * Metodo que retorna los resultados de la busqueda avanzada, de la funcionalidad de Consulta de Cirugias.
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException 
	 */
	
	private ActionForward accionBuscarConsultar(ProgramacionCirugiaForm forma, ActionMapping mapping, Connection con) throws SQLException
	{
		ProgramacionCirugia mundo = new ProgramacionCirugia();
		
		//-- Llenar en el mundo la informacion clave con que buscar las peticiones...
		llenarMundo(forma, mundo);
		
		//-- Cargar las peticiones de acuerdo a los parametros de busqueda 
		forma.setMapaPeticiones(mundo.cargarListadoConsultaPeticionesBusqueda(con));

		//-- Consultar los servicios asociadas a las peticiones de la persona especifica.
		HashMap mp = new HashMap();  
		mp = mundo.cargarListadoConsultaServiciosBusqueda(con, forma.getMapaPeticiones());
		mp.put("numRegServicios", mp.get("numRegistros"));
		mp.remove("numRegistros"); 
		forma.getMapaPeticiones().putAll(mp);


		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("listadoConsultaRangos");
	}


	/**
	 * Metodo para retonrnar la pagina de busqueda avanzada.
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionBusquedaConsulta(ActionMapping mapping, Connection con) throws SQLException
	{
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("busquedaConsulta");
	}


	/**
	 * Metodo Para Ordenar el listado de peticiones del paciente
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionOrdenarPeticiones(ProgramacionCirugiaForm forma, ActionMapping mapping, Connection con) throws SQLException
	{
		String[] indices = {"codigo_peticion_", "fecha_cirugia_", "hora_cirugia_", "cirujanof_", 
							"tipo_anestesia_", "anestesiologof_", "observacionesf_", "empresa_", "sala_"};

		int nroReg = 0;
		if (UtilidadCadena.noEsVacio(forma.getMapaPeticiones("numRegistros")+""))
		{
			nroReg = Integer.parseInt( forma.getMapaPeticiones("numRegistros") + "");
		}
		
        forma.getMapaPeticiones().putAll(Listado.ordenarMapa(indices,
        											forma.getPatronOrdenar(),
										            forma.getUltimoPatron(),
										            forma.getMapaPeticiones(),
										            nroReg ));
        
        forma.setMapaPeticiones("numRegistros", nroReg+"");
        forma.setUltimoPatron(forma.getPatronOrdenar());
        
        UtilidadBD.cerrarConexion(con);
        return mapping.findForward("listadoConsultaPaciente");
	}

	
	/**
	 * Metodo para listar todas las cirugias programadas para un paciente especifico. Para la funcionalidad
	 * de Consulta de Cirugias. 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param loginUsuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionConsultarPaciente(ProgramacionCirugiaForm forma, ActionMapping mapping, Connection con, int  codigoPersona) throws SQLException
	{
		ProgramacionCirugia mundo = new ProgramacionCirugia();
		HashMap mp = new HashMap();
		
		//--Consultar el listado de las peticiones que estan en estado programada y reprogramada   
		forma.setMapaPeticiones( mundo.cargarListadoPeticiones(con, codigoPersona) );
		
		//--Consultar los servicios asociadas a las peticiones de la persona especifica.
		mp = mundo.cargarServiciosPeticion(con, codigoPersona, forma.getMapaPeticiones());
		mp.put("numRegServicios", mp.get("numRegistros"));
		mp.remove("numRegistros"); 
		forma.getMapaPeticiones().putAll(mp);

		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("listadoConsultaPaciente");
	}

	/**
	 * Metodo para mostrar el menu de tercer nivel de Consulta de programacion de cirugias.  
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param loginUsuario
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionConsultarEmpezar(ProgramacionCirugiaForm forma, ActionMapping mapping, Connection con, String loginUsuario) throws SQLException 
	{
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("menuConsultar");
	}

	/**
	 * Metodo para reprograma una peticion de Cirugia
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionSalirReprogramacion(ProgramacionCirugiaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) throws SQLException
	{
		ProgramacionCirugia mundo = new ProgramacionCirugia();
		llenarMundo(forma, mundo);


		
		//-Insertar la programacion y enviar el estado de la inserción a la forma 
		forma.setEstadoInsercion(mundo.insertarProgamacion(con, usuario.getLoginUsuario(), forma.getNumeroPeticion(), 1)); //-- El 1 es para indicar que se va a reprogramar
		
		if ( forma.getEstadoInsercion() > 0)
		{
			return accionListaPeticionesProgramadas(forma,mapping, con, usuario);				
		}
		else
		{
			return accionEmpezar(forma, mapping, con, usuario,forma.getNumeroPeticion(), "reprogramacion", forma.getEstadoAnterior(), forma.getEstadoInsercion());
		}
	}

	/**
	 * Metodo para programar una peticion de cirugia....
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	/*private ActionForward accionReprogramacion(ProgramacionCirugiaForm forma, ActionMapping mapping, Connection con) throws SQLException
	{
		ProgramacionCirugia mundo = new ProgramacionCirugia();

		//-Eliminar la programacion
		mundo.eliminarProgramacion(con, forma.getNumeroPeticion());
		
		//--Consultar el listado de las peticiones que estan en estado programada y reprogramada   
		forma.setListaSalasProgramadas( mundo.cargarListadoPeticionesProgramadas(con) );
		
		//--Consultar los servicios asociadas a las peticiones al    
		forma.setListaServicios(mundo.cargarInformacionServiciosPeticion(con, -2));

		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("listaPeticionesProgramadas");
	}*/	
	
	/**
	 * Metodo para eliminar la programacion para una peticion y dejar esta en estado disponible 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario 
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionEliminarProgramacion(ProgramacionCirugiaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) throws SQLException
	{
		ProgramacionCirugia mundo = new ProgramacionCirugia();
		//-Eliminar la programacion
		forma.setEstadoInsercion( mundo.eliminarProgramacion(con, forma.getNumeroPeticion()) );
		return accionListaPeticionesProgramadas(forma, mapping, con, usuario);
	}	

	/**
	 * Metodo para mostrar el listado de la busqueda de las peticiones programadas o reprogramadas
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionBusquedaAvanzadaPeticionesProgramadas(ProgramacionCirugiaForm forma, ActionMapping mapping, Connection con) throws SQLException
	{
		ProgramacionCirugia mundo = new ProgramacionCirugia();

		//--Llenar en el mundo la informacion clave con que buscar las peticiones...
		llenarMundo(forma, mundo);
		
		//--Consultar el listado de las peticiones que estan en estado programada y reprogramada de acuerdo a 
		//--Los parametros de busqueda.
		forma.setListaSalasProgramadas( mundo.cargarListadoPeticionesBusquedaReprogramacion(con) );
		
		//--Consultar los servicios deque estan en estado programada y reprogramada de acuerdo a 
		//--Los parametros de busqueda.
		forma.setListaServicios(mundo.cargarInformacionServiciosPeticionReprogramacion(con));
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("listaPeticionesProgramadas");
	}

	/**
	 * Metodo para ordenar el listado de peticiones programadas y reprogramadas 
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException
	 * @throws IllegalAccessException
	 */
	
	private ActionForward accionOrdenarProgramadas(ProgramacionCirugiaForm forma, ActionMapping mapping, Connection con) throws SQLException, IllegalAccessException
	{
		forma.setListaSalasProgramadas(Listado.ordenarColumna(new ArrayList(forma.getListaSalasProgramadas()), forma.getUltimoPatronOrdenar(), forma.getPatronOrdenar()));
		forma.setUltimoPatronOrdenar(forma.getPatronOrdenar());
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("listaPeticionesProgramadas");
	}

	
	/**
	 * Metodo pàra cargar el listado de las peticiones programadas y reprogramadas...
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param usuario 
	 * @param nroPeticion
	 * @return
	 * @throws SQLException
	 */
	
	private ActionForward accionListaPeticionesProgramadas(ProgramacionCirugiaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario) throws SQLException
	{
		ProgramacionCirugia mundo = new ProgramacionCirugia();
		
		//--Consultar el listado de las peticiones que estan en estado programada y reprogramada   
		forma.setListaSalasProgramadas( mundo.cargarListadoPeticionesProgramadas(con, usuario.getCodigoCentroAtencion()) );
	
		//--Consultar los servicios asociadas a las peticiones al                 
		forma.setListaServicios(mundo.cargarInformacionServiciosPeticion(con, -2, usuario.getCodigoCentroAtencion(), -1)); //- -1 es el codigoCuentaPaciente (que para este caso no se necesita)

		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("listaPeticionesProgramadas");
		
		/*ProgramacionCirugia mundo = new ProgramacionCirugia();
			
		//--Consultar el listado de las peticiones que estan en estado programada y reprogramada   
		forma.setListaSalasProgramadas( mundo.cargarListadoPeticionesProgramadas(con) );
		
		//--Consultar los servicios asociadas a las peticiones al    
		forma.setListaServicios( mundo.cargarInformacionServiciosBusquedaProgramadas(con));

		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("listaPeticionesProgramadas");*/
	}

	

	/**
	 * Metodo para mostrar los resultados de la busqueda avanzada
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param response
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionResultadoBusquedaAvanzada(ProgramacionCirugiaForm forma, ActionMapping mapping, Connection con, HttpServletResponse response, HttpServletRequest request) throws SQLException
	{
		ProgramacionCirugia mundo = new ProgramacionCirugia();
		
		//-Llenar en el mundo la informacion clave con que buscar las peticiones...
		llenarMundo(forma, mundo);
		
		//--Cargar las peticiones de acuerdo a los parametros de busqueda
		forma.setListaPeticiones(mundo.cargarListadoPeticionesBusqueda(con));

		//--Consultar los servicios asociadas del las peticiones de acuerdo a los parametros de busqueda   
		forma.setListaServicios(mundo.cargarInformacionServiciosBusqueda(con));

		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("listaPeticionesGenerales");
	}

	/**
	 * Metodo para mostrar los resultados de la busqueda avanzada para las peticiones programadas y reprogramadas
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param response
	 * @param request
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionResultadoBusquedaAvanzadaProgramadas(ProgramacionCirugiaForm forma, ActionMapping mapping, Connection con, HttpServletResponse response, HttpServletRequest request) throws SQLException
	{
		ProgramacionCirugia mundo = new ProgramacionCirugia();
		
		//-Llenar en el mundo la informacion clave con que buscar las peticiones...
		llenarMundo(forma, mundo);
		
		//--Consultar el listado de las peticiones que estan en estado programada y reprogramada   
		forma.setListaSalasProgramadas( mundo.cargarListadoPeticionesBusquedaReprogramacion(con) );

		//--Consultar los servicios asociadas a las peticiones al    
		forma.setListaServicios( mundo.cargarInformacionServiciosBusquedaProgramadas(con));

		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("listaPeticionesProgramadas");
	}
	
	
	

	
	

	/**
	 * Metodo para llevar la información del form al mundo 
	 * @param forma
	 * @param mundo
	 */
	private void llenarMundo(ProgramacionCirugiaForm forma, ProgramacionCirugia mundo) 
	{
		mundo.setNroIniServicio(forma.getNroIniServicio());	
		mundo.setNroFinServicio(forma.getNroFinServicio());	
		mundo.setFechaIniPeticion(forma.getFechaIniPeticion());	
		mundo.setFechaFinPeticion(forma.getFechaFinPeticion());	
		mundo.setFechaIniCirugia(forma.getFechaIniCirugia());	
		mundo.setFechaFinCirugia(forma.getFechaFinCirugia());	
		mundo.setProfesional(forma.getProfesional());
		mundo.setEstadoPeticion(forma.getEstadoPeticion());
		
		mundo.setCentroAtencion(forma.getCentroAtencion());
		
		//-Datos de la programacion de Cirugia 
		mundo.setFechaProgramacion(forma.getFechaProgramacion());
		mundo.setHoraInicioProgramacion(forma.getHoraInicioProgramacion());
		mundo.setHoraFinProgramacion(forma.getHoraFinProgramacion());
		mundo.setNroSala(forma.getNroSala());
	}


	/**
	 * Metodo Para lanzar la busqueda avanzada, pero solamente para las peticiones programadas y reprogramadas
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionBusquedaAvanzada(ProgramacionCirugiaForm forma, ActionMapping mapping, Connection con) throws SQLException 
	{
		forma.reset();
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("busqueda");
	}
	

	/**
	 * Metodo para ordenar las columnas de los listados
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param pagina
	 * @return
	 * @throws SQLException
	 * @throws IllegalAccessException
	 * 
	 * */
	private ActionForward accionOrdenar(ProgramacionCirugiaForm forma, ActionMapping mapping, Connection con, String pagina) throws SQLException, IllegalAccessException
	{
		forma.setEstado("paciente");
		
		
		
		
		if ( forma.getPatronOrdenar().equals("id") )
		{
			forma.setUltimoPatronOrdenar("id");
			forma.setPatronOrdenar("codigo");
			forma.setListaPeticiones(Listado.ordenarColumna(new ArrayList(forma.getListaPeticiones()), forma.getUltimoPatronOrdenar(), forma.getPatronOrdenar()));

			forma.setUltimoPatronOrdenar("codigo");
			forma.setPatronOrdenar("id");
		}
		
		forma.setListaPeticiones(Listado.ordenarColumna(new ArrayList(forma.getListaPeticiones()), forma.getUltimoPatronOrdenar(), forma.getPatronOrdenar()));
		forma.setUltimoPatronOrdenar(forma.getPatronOrdenar());
		
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward(pagina);
	}


	/**
	 * Metodo para listar las peticiones por paciente especifico con estado pendiente
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param paciente
	 * @param usuario 
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionConsultarPeticionesPaciente(ProgramacionCirugiaForm forma, ActionMapping mapping, Connection con, PersonaBasica paciente, UsuarioBasico usuario) throws SQLException
	{
		ProgramacionCirugia mundoProgramacionCirugia = new ProgramacionCirugia();
		
		//--Es para que no muestre mensajes el jsp
		forma.setEstadoInsercion(0);
		
		llenarMundo(forma, mundoProgramacionCirugia);
		
		//--Cargar las peticiones asociadas a un paciente especifico....
		forma.setListaPeticiones(mundoProgramacionCirugia.cargarListadoPeticionesPaciente(con, paciente.getCodigoPersona(), usuario.getCodigoCentroAtencion(), paciente.getCodigoCuenta()));

		//--Consultar los servicios asociadas a las peticiones....   
		forma.setListaServicios(mundoProgramacionCirugia.cargarInformacionServiciosPeticion(con, paciente.getCodigoPersona(), usuario.getCodigoCentroAtencion(), paciente.getCodigoCuenta()));

		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("listaPeticionesPaciente");
	}

	
	/**
	 * Metodo para listar las peticiones por paciente especifico con estado pendiente
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param codigoPersona
	 * @param usuario 
	 * @param paciente 
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionConsultarPeticionesGenerales(ProgramacionCirugiaForm forma, ActionMapping mapping, Connection con, int codigoPersona, UsuarioBasico usuario, PersonaBasica paciente) throws SQLException
	{
		ProgramacionCirugia mundoProgramacionCirugia = new ProgramacionCirugia();
		
		//--Es para que no muestre mensajes el jsp
		forma.setEstadoInsercion(0);

		//--Cargar las peticiones asociadas a un paciente especifico....
		//--El -1 es para indicar que liste todas las peticiones ... 
		forma.setListaPeticiones(mundoProgramacionCirugia.cargarListadoPeticionesPaciente(con, -1, usuario.getCodigoCentroAtencion(), paciente.getCodigoCuenta()));  

		//--Consultar los servicios asociadas a las peticiones....   
		forma.setListaServicios(mundoProgramacionCirugia.cargarInformacionServiciosPeticion(con, -1, usuario.getCodigoCentroAtencion(),  paciente.getCodigoCuenta()));

		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("listaPeticionesGenerales");
	}


	/**
	 * Metodo para Guardar la información generada  
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param paciente
	 * @param usuario 
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionSalir(ProgramacionCirugiaForm forma, ActionMapping mapping, Connection con, String loginUsuario, PersonaBasica paciente, UsuarioBasico usuario) throws SQLException
	{
		ProgramacionCirugia mundo = new ProgramacionCirugia();
		llenarMundo(forma, mundo);
		
		//-Insertar la programacion y enviar el estado de la inserción a la forma 
		forma.setEstadoInsercion(mundo.insertarProgamacion(con, loginUsuario, forma.getNumeroPeticion(),0));
		
		//-Informacion que se debe cargar para mostrar el listado de peticiones por paciente 
		if (forma.getEstadoAnterior().equals("paciente"))
		{
			if ( forma.getEstadoInsercion() > 0 )
			{
				llenarMundo(forma, mundo);
				
				//--Cargar las peticiones asociadas a un paciente especifico....
				forma.setListaPeticiones(mundo.cargarListadoPeticionesPaciente(con, paciente.getCodigoPersona(), usuario.getCodigoCentroAtencion(), paciente.getCodigoCuenta()));
	
				//--Consultar los servicios asociadas a las peticiones....   
				forma.setListaServicios(mundo.cargarInformacionServiciosPeticion(con, paciente.getCodigoPersona(), usuario.getCodigoCentroAtencion(),  paciente.getCodigoCuenta()));
	
				forma.setEstado("paciente");
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("listaPeticionesPaciente");
			}
			else
			{
				//-Lo Coloco en la accion empezar de nuevo
				return accionEmpezar(forma, mapping, con,usuario,forma.getNumeroPeticion(), "empezar", forma.getEstadoAnterior(), forma.getEstadoInsercion());
			}
		}
		else  //-Entonces el origen es peticion (Listado de peticiones Generales)
		{
			if ( forma.getEstadoInsercion() > 0 )
			{
				//--Cargar las peticiones asociadas a un paciente especifico....
				//--El -1 es para indicar que liste todas las peticiones ... 
				forma.setListaPeticiones(mundo.cargarListadoPeticionesPaciente(con, -1, usuario.getCodigoCentroAtencion(), paciente.getCodigoCuenta()));  
	
				//--Consultar los servicios asociadas a las peticiones....   
				forma.setListaServicios(mundo.cargarInformacionServiciosPeticion(con, -1, usuario.getCodigoCentroAtencion(),  paciente.getCodigoCuenta() ));
	
				UtilidadBD.cerrarConexion(con);
				return mapping.findForward("listaPeticionesGenerales");
			}	
			else
			{
				return accionEmpezar(forma, mapping, con,usuario,forma.getNumeroPeticion(), "empezar", forma.getEstadoAnterior(), forma.getEstadoInsercion());
			}
		}
	}

	/**
	 * Metodo para mantener en el resumen de la adicion
	 * @param forma
	 * @param mapping
	 * @param con
	 * @return
	 * @throws SQLException 
	 */
	private ActionForward accionResumen(ProgramacionCirugiaForm forma, ActionMapping mapping, Connection con) throws SQLException
	{
		
		forma.setEstado("resumen");
		UtilidadBD.cerrarConexion(con);
		return mapping.findForward("resumen");
	}
	

	/**
	 * Este metodo es para establecer las acciones que se deben realizar 
	 * al entrar a la funcionalidad de Programacion de Cirugias.....
	 * @param forma
	 * @param mapping
	 * @param con
	 * @param nroPeticion
	 * @param i
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionEmpezar	(ProgramacionCirugiaForm forma, ActionMapping mapping, Connection con, UsuarioBasico usuario,int nroPeticion, String estado, String estadoAnterior, int estadoInsercion) throws SQLException
	{
		ProgramacionCirugia mundoProgramacionCirugia = new ProgramacionCirugia();
		Peticion peticion = new Peticion();
		SolicitudesCx sol = new SolicitudesCx();
		
		//-Para saber si se van a mostrar mensajes de error o no en la pajina de programacion
		forma.setEstadoInsercion(estadoInsercion);
		
		//-Debemos almacenar la fecha de programacion para listar la ocupacion de las
		//-salas para ese dia.
		String fechaCirugia = forma.getFechaProgramacion();
		
			//---Si la fecha de cirugia en la peticion es menor a la fecha del sistema o no hay fecha de cirugia
			//---se debe mostrar la programacion de las salas con la fecha actual.  
			if ( UtilidadCadena.noEsVacio(fechaCirugia) && UtilidadFecha.esFechaMenorQueOtraReferencia(fechaCirugia, UtilidadFecha.getFechaActual()) )
			{
			 	fechaCirugia = UtilidadFecha.getFechaActual();
			 	forma.setFechaProgramacion(fechaCirugia);
			}
	
		//-Limpiar la forma
		forma.reset();	
		forma.setFechaProgramacion(fechaCirugia);
		
		//-Verificar si la petición tiene asociada la solicitud 
		int[] solicitudYPeticion=UtilidadValidacion.estaPeticionAsociada(con, nroPeticion);
		int numeroSolicitud=solicitudYPeticion[1];
		
		//--No existe una peticion asociada a una orden
		if (numeroSolicitud == 0)  
		{
			forma.setMapaServicios(peticion.cargarServiciosPeticionResultados(con, nroPeticion,null));
			forma.setMapaServicios("estaAsociada","false");
		}
		else
		{
			forma.setMapaServicios(sol.cargarServiciosXSolicitudCx(con,  (numeroSolicitud+""),false));
			forma.setMapaServicios("estaAsociada","true");
		}
		
		//--Consultar las informacion de peticion asociada....
		forma.setInformacionPeticion(mundoProgramacionCirugia.cargarInformacionPeticion(con, nroPeticion));
		
		//--Consultar la información de las salas que son de tipo cirugia
		forma.setListaSalas(mundoProgramacionCirugia.cargarSalas(con));
		
		//--Consultar el listado de las Salas Que tienen programacion Registrada  
		forma.setListaSalasProgramadas( mundoProgramacionCirugia.cargarProgramacionSalas(con, fechaCirugia) ); 
		
		//--Llenar la forma... 
		llenarForm(forma, mundoProgramacionCirugia);
		
		forma.setEstado(estado);
		forma.setEstadoAnterior(estadoAnterior);

		UtilidadBD.cerrarConexion(con);
		accionValidarIngresoFuncionalidadPCX(usuario, forma);
		return mapping.findForward("principal");
	}


	/**
	 * Metodo para almacenar informacion generada de consultas a la base de datos ...
	 * @param forma
	 * @param mundoProgramacionCirugia
	 */
	private void llenarForm(ProgramacionCirugiaForm forma, ProgramacionCirugia mundoProgramacionCirugia)
	{
		forma.setFechaPeticion(mundoProgramacionCirugia.getFechaPeticion());
		forma.setHoraPeticion(mundoProgramacionCirugia.getHoraPeticion());
		forma.setEstadoPeticion(mundoProgramacionCirugia.getEstadoPeticion());
		forma.setFechaEstimadaCirugia(mundoProgramacionCirugia.getFechaEstimadaCirugia());	
		forma.setSolicitante(mundoProgramacionCirugia.getSolicitante());	
		forma.setDuracion(mundoProgramacionCirugia.getDuracion());	
		forma.setTipoAnestesiaPreanestesia(mundoProgramacionCirugia.getTipoAnestesiaPreanestesia());	
	}
	
	//-Para tener el numero de filas que tiene la grilla 
	//-de acuerdo a la hora inicial y final en la parametrizacion
	private int nroFilasGrilla( int institucion)
	{
	   Date dia1 = new Date();
	   Date dia2 = new Date();
	   
	   String horaIni = ValoresPorDefecto.getHoraInicioProgramacionSalas(institucion);
	   String horaFin = ValoresPorDefecto.getHoraFinProgramacionSalas(institucion);
	   
	   if (!UtilidadCadena.noEsVacio(horaFin) || !UtilidadCadena.noEsVacio(horaFin))
	   {
	   	return 0;
	   }
	   
	   dia1.setHours( Integer.parseInt( horaIni.split(":")[0] ) );
	   dia1.setMinutes( Integer.parseInt( horaIni.split(":")[1] ) );

	   dia2.setHours( Integer.parseInt( horaFin.split(":")[0] ) );
	   dia2.setMinutes( Integer.parseInt( horaFin.split(":")[1] ) );
	   
	   int horas = 0;

	   while ( !dia1.after(dia2) )
	    {
	   		 dia1.setMinutes(dia1.getMinutes()+30);

	   		 if ( !dia1.after(dia2) )
	   			horas++;
	    }
	   
	   return ++horas;
	 }
	
	
	/**
	 * Verifica si el usuario tiene permisos para ingresar a la funcionalidad de Pedido Quirurgico
	 * @param UsuarioBasico usuario
	 * @param ProgramacionCirugiaForm forma
	 * */
	private void accionValidarIngresoFuncionalidadPCX(UsuarioBasico usuario,ProgramacionCirugiaForm forma)
	{
		if(Utilidades.tieneRolFuncionalidad(usuario.getLoginUsuario(),328))		
			forma.setIndicadorPermisoFunc(ConstantesBD.acronimoSi);
		else
			forma.setIndicadorPermisoFunc(ConstantesBD.acronimoNo);
	}	
	
	
	/**
	 * Redirecciona el flujo hacia otra funcionalidad
	 * @param ProgramacionCirugiaForm forma
	 * @param HttpServletResponse response
	 * */
	private void accionRedireccionarFlujoFuncionalidad(ProgramacionCirugiaForm forma, HttpServletResponse response)
	{
		try
		{
			response.sendRedirect("../materialesQx/pedidoQx.do?estado=empezarPedidoFunOut&indicadorFuncOut=programacionQx&numeroPeticion="+forma.getNumeroPeticion()+"&fechaPeticion="+forma.getFechaPeticion()+"&horaPeticion="+forma.getHoraPeticion()+"&estadoPeticion="+forma.getEstadoPeticion());	
		}
		catch(IOException e)
		{
			logger.info("error al retornar a la funcionalidad de Pedido Quirurgico >> "+e);		
		}			
	}
}