package com.princetonsa.action.historiaClinica;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;

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
import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.actionform.historiaClinica.JustificacionNoPosServForm;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.UtilidadJustificacionPendienteArtServ;
import com.princetonsa.mundo.historiaClinica.JustificacionNoPosServ;
import com.princetonsa.mundo.inventarios.FormatoJustServNopos;

/**
 * Clase para el manejo del workflow de la justificación de servicios no pos
 * Date: 2008-04-23
 * @author garias@princetonsa.com
 */
public class JustificacionNoPosServAction extends Action 
{
	
	/**
	 * logger 
	 * */
	Logger logger = Logger.getLogger(JustificacionNoPosServAction.class);
	
	/**
	 * Metodo excute del Action
	 */
    public ActionForward execute(   ActionMapping mapping,
            						ActionForm form,
            						HttpServletRequest request,
            						HttpServletResponse response ) throws Exception
            						{
    	Connection con = null;
    	try {
    		if(response==null);
    		if(form instanceof JustificacionNoPosServForm)
    		{
    			con = UtilidadBD.abrirConexion();

    			if(con == null)
    			{	
    				request.setAttribute("CodigoDescripcionError","errors.problemasBd");
    				return mapping.findForward("paginaError");
    			}
    			JustificacionNoPosServForm forma = (JustificacionNoPosServForm)form;
    			String estado = forma.getEstado();

    			ActionErrors errores = new ActionErrors();

    			logger.info("\n\n\n ESTADO (JUSTIFICACIÓN SERVICIOS NO POS) ---> "+estado+"\n\n");

    			UsuarioBasico usuario = (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");

    			if(paciente==null)
    				errores.add("Paciente", new ActionMessage("errors.required","Paciente"));

    			if(estado == null)
    			{
    				forma.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
    				logger.warn("Estado No Valido Dentro Del Flujo de Justificación de Servicios No POS (null)");
    				request.setAttribute("CodigoDescripcionError","Errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			}
    			else 
    				/*------------------------------
    				 * 		ESTADO > ingresar
    				 *-------------------------------*/
    				if(estado.equals("ingresar"))
    				{   
    					//Validaciones
    					errores = validarProfesionalDeLaSalud(con, forma, usuario, request, errores);
    					if(!errores.isEmpty())
    					{
    						saveErrors(request,errores);	
    						UtilidadBD.closeConnection(con);
    						return mapping.findForward("ingresar");	
    					}
    					UtilidadBD.closeConnection(con);
    					return mapping.findForward("ingresar");			
    				}
    				else 
    					/*------------------------------
    					 * 		ESTADO > adicionarJus
    					 *-------------------------------*/
    					if(estado.equals("adicionarJus"))
    					{
    						//request.getSession().setAttribute("MAPAJUS", forma.getJustificacionesServicios());
    						UtilidadBD.cerrarConexion(con);
    						return mapping.findForward("solicitudesJustificaciones");
    					}
    					else 
    						/*------------------------------
    						 * 		ESTADO > adicionarJusRango
    						 *-------------------------------*/
    						if(estado.equals("adicionarJusRango"))
    						{
    							//request.getSession().setAttribute("MAPAJUS", forma.getJustificacionesServicios());
    							UtilidadBD.cerrarConexion(con);
    							return mapping.findForward("resultadoConsultaRangos");
    						}
    						else 
    							/*------------------------------
    							 * 		ESTADO > adicionarJusCM
    							 *-------------------------------*/
    							if(estado.equals("adicionarJusCM"))
    							{
    								//request.getSession().setAttribute("MAPAJUS", forma.getJustificacionesServicios());
    								UtilidadBD.cerrarConexion(con);
    								return mapping.findForward("solicitudesJustificacionesCM");
    							}
    							else
    								/*------------------------------
    								 * 		ESTADO > adicionarJusRangoCM
    								 *-------------------------------*/
    								if(estado.equals("adicionarJusRangoCM"))
    								{
    									//request.getSession().setAttribute("MAPAJUS", forma.getJustificacionesServicios());
    									UtilidadBD.cerrarConexion(con);
    									return mapping.findForward("resultadoConsultaRangosCM");
    								}
    								else 
    									/*------------------------------
    									 * 		ESTADO > ingresarXPaciente
    									 *-------------------------------*/
    									if(estado.equals("ingresarXPaciente"))
    									{   
    										//Validaciones
    										errores = validarPacienteCargado(con, forma, usuario, paciente, request, errores);
    										errores = validarProfesionalDeLaSalud(con, forma, usuario, request, errores);
    										if(!errores.isEmpty())
    										{
    											saveErrors(request,errores);	
    											UtilidadBD.closeConnection(con);
    											return mapping.findForward("ingresar");	
    										}
    										//Consulta los ingresos por paciente que no tienen cuenta cerrada
    										forma.setIngresosPorPacienteMap(UtilidadesHistoriaClinica.consultarIngresosCuentaXPaciente(con, paciente.getCodigoPersona()));
    										UtilidadBD.closeConnection(con);
    										return mapping.findForward("ingresarXPaciente");			
    									}
    									else
    										/*------------------------------
    										 * 		ESTADO > solicitudesJustificaciones
    										 *-------------------------------*/
    										if(estado.equals("solicitudesJustificaciones"))
    										{    
    											forma.setEstadoAnterior(estado);
    											cargarSolicitudesJustificaciones(con, forma);
    											UtilidadBD.closeConnection(con);
    											return mapping.findForward("solicitudesJustificaciones");			
    										}
    										else 
    											/*------------------------------
    											 * 		ESTADO > ingresarXRangos
    											 *-------------------------------*/
    											if(estado.equals("ingresarXRangos"))
    											{    
    												//Validaciones
    												errores = validarProfesionalDeLaSalud(con, forma, usuario, request, errores);
    												if(!errores.isEmpty())
    												{
    													saveErrors(request,errores);	
    													UtilidadBD.closeConnection(con);
    													return mapping.findForward("ingresar");	
    												}

    												forma.setFuncionalidad("ingresarP");
    												accionIngresarRango(forma, con, mapping, usuario, paciente, request, response);

    												UtilidadBD.closeConnection(con);
    												return mapping.findForward("ingresarXRangos");			
    											}
    											else 
    												/*------------------------------
    												 * 		ESTADO > consultarModificarXRangos
    												 *-------------------------------*/
    												if(estado.equals("consultarModificarXRangos"))
    												{    
    													//Validaciones
    													/*errores = validarProfesionalDeLaSalud(con, forma, usuario, request, errores);
				if(!errores.isEmpty())
				{
					saveErrors(request,errores);	
					UtilidadBD.closeConnection(con);
					return mapping.findForward("ingresar");	
				}*/

    													forma.setFuncionalidad(validarPermisosModificar(con, usuario.getLoginUsuario(), "R"));

    													//cambiar

    													accionConsultarModificarXRango(forma, con, mapping, usuario, paciente, request, response);

    													UtilidadBD.closeConnection(con);
    													return mapping.findForward("consultarModificarXRangos");			
    												}
    												else 
    													/*------------------------------
    													 * 		ESTADO > consultarRangos
    													 *-------------------------------*/
    													if(estado.equals("consultarRangos"))
    													{    
    														//Validaciones
    														forma.setEstadoAnterior(estado);
    														errores = validarConsultarRangos(con, forma, usuario, request, errores);
    														if(!errores.isEmpty())
    														{
    															saveErrors(request,errores);	
    															UtilidadBD.closeConnection(con);
    															return mapping.findForward("ingresarXRangos");	
    														}

//    														if(forma.getFiltros().size()==0 && forma.getFechaInicial().equals("") && forma.getFechaFinal().eq){
//    															forma.setFiltros();
//    															forma.setFechaInicial(); 
//																forma.setFechaFinal();
//    														}
//    														
//    														request.setAttribute("filtros", forma.getFiltros().s);
//    														request.setAttribute("fechaInicial", forma.getFechaInicial());
//    														request.setAttribute("fechaFinal", forma.getFechaFinal());
//    														
//    														
//    														
//    														forma.setSolicitudesJustificacionesMap(mundo.cargarInfoIngresoRango(con,
//    																															forma.getFiltros(), 
//    																															forma.getCodigosServiciosInsertados(), 
//    																															forma.getFechaInicial(), 
//    																															forma.getFechaFinal()));
    														
    														
    														accionConsultarRangos(forma, con, usuario);

    														UtilidadBD.closeConnection(con);
    														return mapping.findForward("resultadoConsultaRangos");			
    													}
    			/*------------------------------
    			 * 		ESTADO > consultarRangosCM
    			 *-------------------------------*/
    			if(estado.equals("consultarRangosCM"))
    			{    
    				//Validaciones
    				errores = validarConsultarRangos(con, forma, usuario, request, errores);
    				errores = validarConsultarRangosCM(con, forma, usuario, request, errores);
    				if(!errores.isEmpty())
    				{
    					saveErrors(request,errores);	
    					UtilidadBD.closeConnection(con);
    					return mapping.findForward("consultarModificarXRangos");	
    				}



    				if(forma.getFiltros("tipoReporte").toString().equals("0") ){
    					accionConsultarRangosCMdetallado(forma, con, usuario);
    					UtilidadBD.closeConnection(con);
    					return mapping.findForward("resultadoConsultaRangosCM");
    				}

    				if(forma.getFiltros("tipoReporte").toString().equals("1") ){
    					accionConsultarRangosCMconsolidado(forma, con, usuario);
    					forma.setFuncionalidad(forma.getFuncionalidad()+"con");
    					UtilidadBD.closeConnection(con);
    					return mapping.findForward("resultadoConsultaRangosCMcon");
    				}



    			}
    			/*------------------------------
    			 * 		ESTADO > cargarPaciente
    			 *-------------------------------*/
    			if(estado.equals("cargarPaciente"))
    			{    
    				int	codpaciente = Integer.parseInt(forma.getSolicitudesJustificacionesMap().get("codpaciente_"+forma.getPosMap()).toString());
    				paciente.setCodigoPersona(codpaciente);
    				paciente.cargar(con, codpaciente);
    				paciente.cargarPaciente(con, codpaciente, usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");

    				UtilidadBD.closeConnection(con);

    				if(forma.getContPagina()==0){
    					forma.setContPagina(1);
    				}else 
    				if(forma.getContPagina()==1){
    					forma.setContPagina(0);
    				}
    				
    				logger.info("FUNCIONALIDAD > "+forma.getFuncionalidad());

    				if (forma.getFuncionalidad().equals("consultarModificarP") || forma.getFuncionalidad().equals("consultarModificarTodoP") || forma.getFuncionalidad().equals("consultarModificarEstadosP") || forma.getFuncionalidad().equals("ingresarP"))
    					return mapping.findForward("resultadoConsultaRangos");
    				else if (forma.getFuncionalidad().equals("consultarModificarR") || forma.getFuncionalidad().equals("consultarModificarTodoR") || forma.getFuncionalidad().equals("consultarModificarEstadosR"))
    					return mapping.findForward("resultadoConsultaRangosCM");
    			}
    			else 
    				/*------------------------------
    				 * 		ESTADO > buscarServicio
    				 *-------------------------------*/
    				if(estado.equals("buscarServicio"))
    				{
    					forma.setServiciosMap("numeroFilasMapaServicios", Integer.parseInt(forma.getServiciosMap("numeroFilasMapaServicios").toString())+1);
    					actualizarCodigosInsertados(forma);
    					if (forma.getFuncionalidad().equals("consultarModificarR") || forma.getFuncionalidad().equals("consultarModificarTodoR") || forma.getFuncionalidad().equals("consultarModificarEstadosR"))
    						return mapping.findForward("consultarModificarXRangos");
    					else 
    						return mapping.findForward("ingresarXRangos");
    				}	
    				else 
    					/*------------------------------
    					 * 		ESTADO > eliminarServicio
    					 *-------------------------------*/
    					if(estado.equals("eliminarServicio"))
    					{
    						forma.setServiciosMap("fueEliminadoServicio_"+forma.getPosMap(), "true");
    						actualizarCodigosInsertados(forma);
    						return mapping.findForward("ingresarXRangos");	
    					}
    					else 
    						/*------------------------------
    						 * 		ESTADO > cargarCentrosCosto
    						 *-------------------------------*/
    						if(estado.equals("cargarCentrosCosto"))
    						{
    							accionCargarCentrosCosto(forma,con,mapping,usuario,paciente,request,response);

    						}	
    						else
    							/*------------------------------
    							 * 		ESTADO > consultarModificar
    							 *-------------------------------*/
    							if(estado.equals("consultarModificar"))
    							{   
    								// Validaciones
    								errores = validarProfesionalDeLaSalud(con, forma, usuario, request, errores);
    								if(!errores.isEmpty())
    								{
    									saveErrors(request,errores);	
    									UtilidadBD.closeConnection(con);
    									return mapping.findForward("ingresar");	
    								}
    								UtilidadBD.closeConnection(con);
    								return mapping.findForward("consultarModificar");			
    							}
    							else
    								/*------------------------------
    								 * 		ESTADO > consultar
    								 *-------------------------------*/
    								if(estado.equals("consultar"))
    								{   
    									forma.setFuncionalidad("consultar");
    									UtilidadBD.closeConnection(con);
    									return mapping.findForward("consultarModificar");			
    								}
    			/*------------------------------
    			 * 		ESTADO > consultarModificarXPaciente
    			 *-------------------------------*/
    			if(estado.equals("consultarModificarXPaciente"))
    			{    			
    				//Validaciones
    				errores = validarPacienteCargado(con, forma, usuario, paciente, request, errores);
    				//errores = validarProfesionalDeLaSalud(con, forma, usuario, request, errores);
    				if(!errores.isEmpty())
    				{
    					saveErrors(request,errores);	
    					UtilidadBD.closeConnection(con);
    					return mapping.findForward("consultarModificar");	
    				}

    				forma.setFuncionalidad(validarPermisosModificar(con, usuario.getLoginUsuario(), "P"));

    				logger.info("PERMISO - PERMISO - PERMISO - PERMISO -"+forma.getFuncionalidad());


    				//Consulta los ingresos por paciente que no tienen cuenta cerrada
    				forma.setIngresosPorPacienteMap(UtilidadesHistoriaClinica.consultarIngresosXPaciente(con, paciente.getCodigoPersona()));
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("consultarModificarXPaciente");			
    			}
    			else
    				/*------------------------------
    				 * 		ESTADO > solicitudesJustificacionesCM
    				 *-------------------------------*/
    				if(estado.equals("solicitudesJustificacionesCM"))
    				{
    					cargarSolicitudesJustificacionesCM(con, forma);
    					UtilidadBD.closeConnection(con);
    					return mapping.findForward("solicitudesJustificacionesCM");			
    				}
    				else
    					/*------------------------------
    					 * 		ESTADO > ordenarIngresarXPaciente
			 -------------------------------*/
    					if (estado.equals("ordenarIngresarXPaciente"))
    					{
    						accionOrdenarIngresarXPaciente(forma);
    						UtilidadBD.closeConnection(con);
    						return mapping.findForward("ingresarXPaciente");
    					}
    			/*------------------------------
    			 * 		ESTADO > ordenarConsultarModificarXPaciente
			 -------------------------------*/
    			if (estado.equals("ordenarConsultarModificarXPaciente"))
    			{
    				accionOrdenarIngresarXPaciente(forma);
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("consultarModificarXPaciente");
    			}
    			else
    				/*------------------------------
    				 * 		ESTADO > ordenarIngresarXPaciente
			 -------------------------------*/
    				if (estado.equals("ordenarSolicitudesJustificaciones"))
    				{
    					accionOrdenarSolicitudesJustificaciones(forma);
    					UtilidadBD.closeConnection(con);
    					return mapping.findForward("solicitudesJustificaciones");
    				}
    				else
    					/*------------------------------
    					 * 		ESTADO > ordenarSolicitudesJustificacionesRango
			 -------------------------------*/
    					if (estado.equals("ordenarSolicitudesJustificacionesRango"))
    					{
    						accionOrdenarSolicitudesJustificacionesRango(forma);
    						UtilidadBD.closeConnection(con);
    						return mapping.findForward("resultadoConsultaRangos");
    					}
    					else
    						/*------------------------------
    						 * 		ESTADO > ordenarJustificacionesRangoCM
			 -------------------------------*/
    						if (estado.equals("ordenarJustificacionesRangoCM"))
    						{
    							accionOrdenarJustificacionesRangoCM(forma);
    							UtilidadBD.closeConnection(con);
    							return mapping.findForward("resultadoConsultaRangosCM");
    						}else

        						/*------------------------------
        						 * 		ESTADO > ordenarJustificacionesRangoCMcon
    			 -------------------------------*/
    						if(estado.equals("ordenarJustificacionesRangoCMcon")){
    							accionOrdenarJustificacionesRangoCM(forma);
    							UtilidadBD.closeConnection(con);
    							return mapping.findForward("resultadoConsultaRangosCMcon");
    						}
    			/*------------------------------
    			 * 		ESTADO > ordenarJustificacionesRangoCM
			 -------------------------------*/
    			if (estado.equals("ordenarJustificacionesRangoCM"))
    			{
    				accionOrdenarJustificacionesRangoCM(forma);
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("resultadoConsultaRangosCM");
    			}
    			/*------------------------------
    			 * 		ESTADO > ordenarConsultarModificarSolXPaciente
			 -------------------------------*/
    			if (estado.equals("ordenarConsultarModificarSolXPaciente"))
    			{
    				accionOrdenarConsultarModificarXPaciente(forma);
    				UtilidadBD.closeConnection(con);
    				return mapping.findForward("solicitudesJustificacionesCM");
    			}




    		}
    		return null;
    	} catch (Exception e) {
    		Log4JManager.error(e);
    		return null;
    	}
    	finally{
    		UtilidadBD.closeConnection(con);
    	}
    }

	



	private ActionErrors validarConsultarRangosCM(Connection con, JustificacionNoPosServForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionErrors errores) {
		if(!forma.getFiltros().containsKey("tipoReporte"))
			errores.add("Validación campo tipo reporte", new ActionMessage("errors.required","Tipo Reporte"));
		if(!forma.getFiltros().containsKey("tipoRompimiento"))
			errores.add("Validación campo tipo de rompimiento", new ActionMessage("errors.required","Tipo de Rompimiento"));
		return errores;
		
	}

	private void accionConsultarRangosCMdetallado(JustificacionNoPosServForm forma, Connection con, UsuarioBasico usuario) {

		JustificacionNoPosServ mundo=new JustificacionNoPosServ();
		
		logger.info("filtros >>>>>"+forma.getFiltros());
		
		forma.setSolicitudesJustificacionesMap(mundo.cargarInfoIngresoConsultarModificarRango(con,
									forma.getFiltros(), 
									forma.getCodigosServiciosInsertados(), 
									forma.getFechaInicial(), 
									forma.getFechaFinal()));

		forma.setFiltros("fechahora", UtilidadFecha.getFechaActual()+" - "+UtilidadFecha.getHoraActual());
		
	}
	
	private void accionConsultarRangosCMconsolidado(JustificacionNoPosServForm forma, Connection con, UsuarioBasico usuario) {

		JustificacionNoPosServ mundo=new JustificacionNoPosServ();
		
		forma.setSolicitudesJustificacionesMap(mundo.cargarInfoIngresoConsultarModificarRangoCon(con,
									forma.getFiltros(), 
									forma.getCodigosServiciosInsertados(), 
									forma.getFechaInicial(), 
									forma.getFechaFinal()));
		
		forma.setFiltros("fechahora", UtilidadFecha.getFechaActual()+" - "+UtilidadFecha.getHoraActual());
		
	}

	/**
	 * 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @param response
	 */
	private void accionConsultarModificarXRango(JustificacionNoPosServForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, HttpServletResponse response) {
		forma.resetIngresoXRango();
    	forma.setFiltros("centroAtencion",usuario.getCodigoCentroAtencion());
    	forma.setCodigoCentroAtencion(usuario.getCentroAtencion());
    	forma.setCodigoViaIngreso(ConstantesBD.codigoNuncaValido+"");
    	forma.setCentroAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(con, usuario.getCodigoInstitucionInt(),""));
    	forma.setViaIngresoPaciente(Utilidades.obtenerViasIngresoTipoPaciente(con));
    	forma.setCentrosCosto(UtilidadesManejoPaciente.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoAreaDirecto+"", false, 0));
    	forma.setConvenios(Utilidades.obtenerConvenios(con, "", "", true, "", true));
    	
    	HashMap mapa = new HashMap(); 
    	mapa.put("institucion", usuario.getCodigoInstitucion());
    	forma.setPisos(UtilidadesManejoPaciente.obtenerPisos(con, mapa));
    	forma.setProfesionales(UtilidadesManejoPaciente.obtenerProfesionales(con, usuario.getCodigoInstitucionInt(), true, false, "",""));
    	forma.setCargarsi(1);
    	accionCargarCentrosCosto(forma, con, mapping, usuario, paciente, request, response);
	}

	/**
	 * 
	 * @param con
	 * @param loginUsuario
	 * @param func
	 * @return
	 */
	private String validarPermisosModificar(Connection con, String loginUsuario, String func) {
		String permiso="";
		
		//P por paciente
		if (func.equals("P")){
			if (Utilidades.tieneRolFuncionalidad(con, loginUsuario, 744)){
				permiso = "consultar";
				if (Utilidades.tieneRolFuncionalidad(con, loginUsuario, 745)){
					permiso = "consultarModificarP";
					if (Utilidades.tieneRolFuncionalidad(con, loginUsuario, 747))
						permiso = "consultarModificarTodoP";
				}	
				else if (Utilidades.tieneRolFuncionalidad(con, loginUsuario, 746))
					permiso = "consultarModificarEstadosP";
			}
			else if (Utilidades.tieneRolFuncionalidad(con, loginUsuario, 745)){
				permiso = "consultarModificarP";
				if (Utilidades.tieneRolFuncionalidad(con, loginUsuario, 747))
					permiso = "consultarModificarTodoP";
			}
		}
		
		//R por rango
		if (func.equals("R")){
			if (Utilidades.tieneRolFuncionalidad(con, loginUsuario, 744)){
				permiso = "consultar";
				if (Utilidades.tieneRolFuncionalidad(con, loginUsuario, 745)){
					permiso = "consultarModificarR";
					if (Utilidades.tieneRolFuncionalidad(con, loginUsuario, 747))
						permiso = "consultarModificarTodoR";
				}	
				else if (Utilidades.tieneRolFuncionalidad(con, loginUsuario, 746))
					permiso = "consultarModificarEstadosR";
			}
			else if (Utilidades.tieneRolFuncionalidad(con, loginUsuario, 745)){
				permiso = "consultarModificarR";
				if (Utilidades.tieneRolFuncionalidad(con, loginUsuario, 747))
					permiso = "consultarModificarTodoR";
			}
		}
		return permiso;
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 */
	private void cargarSolicitudesJustificacionesCM(Connection con, JustificacionNoPosServForm forma) {
		int pos = forma.getPosMap();
		JustificacionNoPosServ mundo = new JustificacionNoPosServ();
		forma.setSolicitudesJustificacionesMap(mundo.consultarSolicitudesJustificacionesDiligenciadas(con, Integer.parseInt(forma.getIngresosPorPacienteMap("num_cuenta_"+pos).toString())));
		forma.setJustificacionesServicios(new HashMap());
		forma.setJustificacionesServicios("numRegistros", 0);
	}

	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenarSolicitudesJustificacionesRango(JustificacionNoPosServForm forma) {
    	String[] indices = {"codigo_centro_costo_","centro_costo_","codconvenio_","convenio_","solicitud_","fecha_","servicio_","cupsServicio_","ingreso_","subcuenta_","codviaingreso_","codtipopaciente_","viaingresotipopac_","fechahora_","noingreso_","valor_","tipoid_","nombrepac_","codpaciente_","codigoserv_","codigocuenta_","cantidad_",""};
		int numReg = Integer.parseInt(forma.getSolicitudesJustificacionesMap("numRegistros")+"");
		forma.setSolicitudesJustificacionesMap(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getSolicitudesJustificacionesMap(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setSolicitudesJustificacionesMap("numRegistros",numReg+"");
		forma.setSolicitudesJustificacionesMap("INDICES_MAPA",indices);
	}

	/**
	 * 
	 * @param forma
	 * @param con
	 * @param usuario
	 */
	private void accionConsultarRangos(JustificacionNoPosServForm forma, Connection con, UsuarioBasico usuario) {
    	JustificacionNoPosServ mundo=new JustificacionNoPosServ();
		forma.setSolicitudesJustificacionesMap(mundo.cargarInfoIngresoRango(con,
																			forma.getFiltros(), 
																			forma.getCodigosServiciosInsertados(), 
																			forma.getFechaInicial(), 
																			forma.getFechaFinal()));
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 * @param request
	 * @param errores
	 * @return
	 */
	private ActionErrors validarConsultarRangos(Connection con, JustificacionNoPosServForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionErrors errores) {
    	// Validacion Centro Atencion
		if (forma.getFiltros("centroAtencion").equals(""))
			errores.add("centro de atencion requerido",new ActionMessage("errors.required","Centro de Atención"));
		
		// Validacion rango fechas
		if(!forma.getFechaInicial().equals("")&&!forma.getFechaFinal().equals(""))
		{
			if(UtilidadFecha.validarFecha(forma.getFechaInicial())&&UtilidadFecha.validarFecha(forma.getFechaFinal()))
			{
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFechaInicial(),UtilidadFecha.getFechaActual()))
					errores.add("Fecha inicial mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","inicial","actual"));
				if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFechaFinal(),UtilidadFecha.getFechaActual()))
					errores.add("Fecha final mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","final","actual"));
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(forma.getFechaFinal(),forma.getFechaInicial()))
					errores.add("Fecha inicial mayor a la fecha final",new ActionMessage("errors.fechaAnteriorIgualActual","final","inicial"));
				else if(UtilidadFecha.numeroMesesEntreFechas(forma.getFechaInicial(),forma.getFechaFinal(),true)>3)
					errores.add("Rango de fechas > a 3 meses",new ActionMessage("errors.rangoMayorTresMeses","para consulta de justificaciones"));
			}
			else
			{
				if(!UtilidadFecha.validarFecha(forma.getFechaInicial()))
					errores.add("Fecha Inicial inválida",new ActionMessage("errors.formatoFechaInvalido","inicial"));
				if(!UtilidadFecha.validarFecha(forma.getFechaFinal()))
					errores.add("Fecha Final inválida",new ActionMessage("errors.formatoFechaInvalido","final"));
				
			}
		}
		else
		{
			if(forma.getFechaInicial().equals(""))
				errores.add("fecha Inicial requerida",new ActionMessage("errors.required","La Fecha Inicial"));
			else if(!UtilidadFecha.validarFecha(forma.getFechaInicial()))
				errores.add("Fecha Inicial inválida",new ActionMessage("errors.formatoFechaInvalido","inicial"));
			else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFechaInicial(),UtilidadFecha.getFechaActual()))
				errores.add("Fecha inicial mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","inicial","actual"));
			
			if(forma.getFechaFinal().equals(""))
				errores.add("fecha Final requerida",new ActionMessage("errors.required","La Fecha Final"));
			else if(!UtilidadFecha.validarFecha(forma.getFechaFinal()))
				errores.add("Fecha Final inválida",new ActionMessage("errors.formatoFechaInvalido","final"));
			else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(forma.getFechaFinal(),UtilidadFecha.getFechaActual()))
				errores.add("Fecha final mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","final","actual"));
		}
		return errores;
	}

	private void actualizarCodigosInsertados(JustificacionNoPosServForm forma) {
    	String codigosServicios="";
		for(int i=0; i<Integer.parseInt(forma.getServiciosMap("numeroFilasMapaServicios").toString()); i++){
			if (forma.getServiciosMap("fueEliminadoServicio_"+i).toString().equals("false")){
				codigosServicios += forma.getServiciosMap("codigoServicio_"+i).toString()+", ";
			}
		}
		codigosServicios+=ConstantesBD.codigoNuncaValido;
		forma.setCodigosServiciosInsertados(codigosServicios);
	}

	/**
     * 
     * @param forma
     * @param con
     * @param mapping
     * @param usuario
     * @param paciente
     * @param request
     * @param response
     */
	private void accionIngresarRango(JustificacionNoPosServForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, HttpServletResponse response) {
		forma.resetIngresoXRango();
		forma.setFiltros("centroAtencion",usuario.getCodigoCentroAtencion());
		forma.setCentroAtencion(UtilidadesManejoPaciente.obtenerCentrosAtencion(con, usuario.getCodigoInstitucionInt(),""));
		forma.setViaIngresoPaciente(Utilidades.obtenerViasIngresoTipoPaciente(con));
		forma.setCentrosCosto(UtilidadesManejoPaciente.obtenerCentrosCosto(con, usuario.getCodigoInstitucionInt(), ConstantesBD.codigoTipoAreaDirecto+"", false, 0));
		forma.setConvenios(Utilidades.obtenerConvenios(con, "", "", true, "", true));
		accionCargarCentrosCosto(forma, con, mapping, usuario, paciente, request, response);
	}
	
	/**
	 * 
	 * @param forma
	 * @param con
	 * @param mapping
	 * @param usuario
	 * @param paciente
	 * @param request
	 * @param response
	 * @return
	 */
	private void accionCargarCentrosCosto(JustificacionNoPosServForm forma, Connection con, ActionMapping mapping, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, HttpServletResponse response) {

		String resultado = "<respuesta>" +
		"<infoid>" +
			"<sufijo>ajaxBusquedaTipoSelect</sufijo>" +
			"<id-select>centrocosto</id-select>" +
			"<id-arreglo>centro-costo</id-arreglo>" + //nombre de la etiqueta de cada elemento
		"</infoid>";
		
		//	 aqui iteramos el arraylist de centros de costo para hacer el filtro por centros de atencion y via ingreso
		
		for(int i=0;i<forma.getCentrosCosto().size();i++)
		{
			if(forma.getCentrosCosto().get(i).get("codcentroatencion").toString().equals(""+forma.getCodigoCentroAtencion()+"")	)
			{
				if(forma.getCodigoViaIngreso().equals(ConstantesBD.codigoNuncaValido+""))
				{
					resultado += "<centro-costo>";
					resultado += "<codigo>"+forma.getCentrosCosto().get(i).get("codigo")+"</codigo>";
					resultado += "<descripcion>"+forma.getCentrosCosto().get(i).get("nomcentrocosto")+"</descripcion>";
					resultado += "</centro-costo>";
				}
				else 
				{
					if(forma.getCentrosCosto().get(i).get("viaingtipopac").toString().equals(""+forma.getCodigoViaIngreso()+""))
					{
						resultado += "<centro-costo>";
						resultado += "<codigo>"+forma.getCentrosCosto().get(i).get("codigo")+"</codigo>";
						resultado += "<descripcion>"+forma.getCentrosCosto().get(i).get("nomcentrocosto")+"</descripcion>";
						resultado += "</centro-costo>";
					}
				}
			}
		}
		
		resultado += "</respuesta>";

		logger.info("resultado >>>"+resultado);
		UtilidadBD.closeConnection(con);
		//**********SE GENERA RESPUESTA PARA AJAX EN XML**********************************************
		try
		{
			response.setContentType("text/xml");
			response.setHeader("Cache-Control", "no-cache");
			logger.info("va a introduciir el resultado ");
	        response.getWriter().write(resultado);
	        logger.info("paso");
		}
		catch(IOException e)
		{
			logger.error("Error al enviar respuesta AJAX: "+e);
		}
	}
	
	/**
	 * 
	 * @param con
	 * @param forma
	 * @param usuario
	 */
	private void guardarJusAntesPendiente(Connection con, JustificacionNoPosServForm forma, UsuarioBasico usuario) {
		FormatoJustServNopos fjsn = new FormatoJustServNopos();
		String consecutivoSolicitud="";
		int codigoSolicitud=ConstantesBD.codigoNuncaValido;
		int aux;
		//Recorremos las solicitudes
		for (int i=0; i<Integer.parseInt(forma.getSolicitudesJustificacionesMap("numRegistros").toString()); i++){
			//Preguntamos si se ha registrado alguna justificación
			if (forma.getJustificacionesServicios().containsKey(i+"_servicio")){
				String subcuentas[] = forma.getSolicitudesJustificacionesMap("subcuenta_"+i).toString().split(", ");
				aux = subcuentas.length;
				//Ingresamos la justificación No POS
				consecutivoSolicitud = fjsn.ingresarJustificacion(
                		con,
                		usuario.getCodigoInstitucionInt(),
                		usuario.getLoginUsuario(), 
                		forma.getJustificacionesServicios(), 
                		Integer.parseInt(forma.getSolicitudesJustificacionesMap("solicitud_"+i).toString()),
                		ConstantesBD.codigoNuncaValido,
                		i,
                		usuario.getCodigoPersona());
				//eliminamos el registro de la justificacion pendiente
				UtilidadJustificacionPendienteArtServ.eliminarJusNoposPendiente(con, Integer.parseInt(forma.getSolicitudesJustificacionesMap("solicitud_"+i).toString()), Integer.parseInt(forma.getSolicitudesJustificacionesMap("codigo_servicio_"+i).toString()), false);
				//Ingresamos el registro para otro responsable si lo tiene
				for(int j=1; j<aux; j++){
					fjsn.ingresarResponsable(con, consecutivoSolicitud, Integer.parseInt(subcuentas[j]), 1);
				}
			}
		}
	}

	private void accionOrdenarSolicitudesJustificaciones(JustificacionNoPosServForm forma) {
		String[] indices = {"codigo_centro_costo_", "centro_costo_", "codigo_convenio_", "convenio_", "solicitud_", "fecha_solicitud_", "codigo_servicio_", "servicio_", "cupsServicio_", "subcuenta_","cantidad_","cupsservicio_",""};
		int numReg = Integer.parseInt(forma.getSolicitudesJustificacionesMap("numRegistros")+"");
		forma.setSolicitudesJustificacionesMap(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getSolicitudesJustificacionesMap(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setSolicitudesJustificacionesMap("numRegistros",numReg+"");
		forma.setSolicitudesJustificacionesMap("INDICES_MAPA",indices);
	}

	/**
	 * 
	 * @param con
	 * @param forma
	 */
	private void cargarSolicitudesJustificaciones(Connection con, JustificacionNoPosServForm forma) {
		int pos = forma.getPosMap();
		JustificacionNoPosServ mundo = new JustificacionNoPosServ();
		forma.setSolicitudesJustificacionesMap(mundo.consultarSolicitudesJustificaciones(con, Integer.parseInt(forma.getIngresosPorPacienteMap("num_cuenta_"+pos).toString())));
		forma.setJustificacionesServicios(new HashMap());
		forma.setJustificacionesServicios("numRegistros", 0);
	}

	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenarIngresarXPaciente(JustificacionNoPosServForm forma) {
		String[] indices = {"centro_atencion_", "nom_centro_atencion_", "via_ingreso_", "num_ingreso_", "fecha_ingreso_", "fecha_egreso_", "estado_ingreso_", "nom_estado_ingreso_", "num_cuenta_", "nom_estado_cuenta_", "estado_cuenta_",""};
		int numReg = Integer.parseInt(forma.getIngresosPorPacienteMap("numRegistros")+"");
		forma.setIngresosPorPacienteMap(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getIngresosPorPacienteMap(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setIngresosPorPacienteMap("numRegistros",numReg+"");
		forma.setIngresosPorPacienteMap("INDICES_MAPA",indices);
	}
	
	private void accionOrdenarConsultarModificarXPaciente(JustificacionNoPosServForm forma) {
		logger.info("Patron -> "+forma.getPatronOrdenar());
		Utilidades.imprimirMapa(forma.getSolicitudesJustificacionesMap());
		String[] indices = {"codigo_centro_costo_", "centro_costo_", "codigo_convenio_", "convenio_", "solicitud_", "fecha_solicitud_", "cups_", "codigo_servicio_", "servicio_", "cupsServicio_", "subcuenta_", "fechajus_", "numjus_","profesionalresp_",""};
		int numReg = Integer.parseInt(forma.getSolicitudesJustificacionesMap("numRegistros")+"");
		forma.setSolicitudesJustificacionesMap(Listado.ordenarMapa(indices, forma.getPatronOrdenar(), forma.getUltimoPatron(), forma.getSolicitudesJustificacionesMap(), numReg));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setSolicitudesJustificacionesMap("numRegistros",numReg+"");
		forma.setSolicitudesJustificacionesMap("INDICES_MAPA",indices);
	}
	
	/**
	 * 
	 * @param forma
	 */
	private void accionOrdenarJustificacionesRangoCM(JustificacionNoPosServForm forma) {
		String[] indices = {"codigo_centro_costo_", 
							"centro_costo_", 
							"codconvenio_", 
							"convenio_", 
							"solicitud_", 
							"fecha_", 
							"servicio_",
							"cupsServicio_",
							"ingreso_", 
							"subcuenta_", 
							"codviaingreso_", 
							"codtipopaciente_",
							"viaingresotipopac_",
							"fechahora_",
							"noingreso_",
							"tipoid_",
							"nombrepac_",
							"codigoserv_",
							"codigocuenta_",
							"codpaciente_",
							"cama_",
							"nojus_",
							"cantotorden_",
							"cantotconv_",
							"estadojus_",
							"preciotarifa_",
							"cups_","",
							/** POR RANGO  **/
							"nomservicio_",
							"profesionalresp_",
							"cantidadord_",
							"precio_total_"};
		int numReg = Integer.parseInt(forma.getSolicitudesJustificacionesMap("numRegistros")+"");
		forma.setSolicitudesJustificacionesMap(Listado.ordenarMapaRompimiento(indices, 
				  forma.getPatronOrdenar(), 
				  forma.getUltimoPatron(), 
				  forma.getSolicitudesJustificacionesMap(), 
				  forma.getFiltros().get("tipoRompimiento").toString()));
		forma.setUltimoPatron(forma.getPatronOrdenar());
		forma.setSolicitudesJustificacionesMap("numRegistros",numReg+"");
		forma.setSolicitudesJustificacionesMap("INDICES_MAPA",indices);
	}

	/**
     * Función que valida si el paciente se encuentra cargado
     * @param con
     * @param forma
     * @param usuario
     * @param paciente
     * @param request
     * @param errores
     * @return
     */
    private ActionErrors validarPacienteCargado(Connection con, JustificacionNoPosServForm forma, UsuarioBasico usuario, PersonaBasica paciente, HttpServletRequest request, ActionErrors errores) {
    	if(paciente.getCodigoPersona()<=0)
			errores.add("Paciente Cargado", new ActionMessage("errors.required","Paciente Cargado"));
    	return errores;
	}

	/**
     * Función que valida que el usuario que ingresa sea un profesional de la salud
     * @param con
     * @param forma
     * @param usuario
     * @param request
     * @param errores
     * @return
     */
	private ActionErrors validarProfesionalDeLaSalud(Connection con, JustificacionNoPosServForm forma, UsuarioBasico usuario, HttpServletRequest request, ActionErrors errores) {
		if(!UtilidadesHistoriaClinica.validarEspecialidadProfesionalSalud(con, usuario, false))
			errores.add("Validación Ocupación Medico Especialista", new ActionMessage("errors.required","Validación Ocupación Medico Especialista"));
		return errores;
	}
}    