/*
 * @(#)SolicitudesCirugiaAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.action.ordenesmedicas.cirugias;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import org.apache.struts.action.ActionMessages;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.Listado;
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.facturacion.InfoResponsableCobertura;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.ordenesMedicas.UtilidadesOrdenesMedicas;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.ordenesmedicas.cirugias.SolicitudesCxForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.sqlbase.SqlBaseSolicitudesCxDao;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.mundo.Cuenta;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.Cobertura;
import com.princetonsa.mundo.facturacion.Servicios_ArticulosIncluidosEnOtrosProcedimientos;
import com.princetonsa.mundo.inventarios.FormatoJustServNopos;
import com.princetonsa.mundo.ordenesmedicas.OrdenMedica;
import com.princetonsa.mundo.ordenesmedicas.OrdenesAmbulatorias;
import com.princetonsa.mundo.ordenesmedicas.cirugias.HojaQuirurgica;
import com.princetonsa.mundo.ordenesmedicas.cirugias.SolicitudesCx;
import com.princetonsa.mundo.ordenesmedicas.procedimientos.RespuestaProcedimientos;
import com.princetonsa.mundo.salasCirugia.Peticion;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.servinte.axioma.bl.administracion.facade.AdministracionFacade;
import com.servinte.axioma.bl.manejoPaciente.facade.ManejoPacienteFacade;
import com.servinte.axioma.bl.ordenes.facade.OrdenesFacade;
import com.servinte.axioma.dto.facturacion.ContratoDto;
import com.servinte.axioma.dto.facturacion.ConvenioDto;
import com.servinte.axioma.dto.facturacion.MontoCobroDto;
import com.servinte.axioma.dto.manejoPaciente.AnulacionAutorizacionSolicitudDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionCapitacionDto;
import com.servinte.axioma.dto.manejoPaciente.AutorizacionPorOrdenDto;
import com.servinte.axioma.dto.manejoPaciente.DatosPacienteAutorizacionDto;
import com.servinte.axioma.dto.manejoPaciente.InfoSubCuentaDto;
import com.servinte.axioma.dto.ordenes.OrdenAutorizacionDto;
import com.servinte.axioma.dto.ordenes.ProfesionalEspecialidadesDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.Medicos;
import com.servinte.axioma.orm.delegate.facturacion.convenio.ContratosDelegate;

/**
 *   Action, controla todas las opciones dentro de las solicitudes de Cx 
 *   incluyendo los posibles casos de error y los casos de flujo.
 * @version 1.0, Nov 01, 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class SolicitudesCxAction extends Action 
{
    /**
     * Objeto para manejar los logs de esta clase
    */
    private Logger logger = Logger.getLogger(SolicitudesCxAction.class);

    /**
     * mapas que van ha contener la informacion antes de la modificacion
     * para generar la estructura del log
     */
    @SuppressWarnings("rawtypes")
	protected static HashMap logEncabezadoMap= new HashMap();
    @SuppressWarnings("rawtypes")
	protected static HashMap logServiciosMap= new HashMap();
    @SuppressWarnings("rawtypes")
	protected static HashMap logProfesionalesMap= new HashMap();
    @SuppressWarnings("rawtypes")
	protected static HashMap logArticulosMap= new HashMap();
    @SuppressWarnings("rawtypes")
	protected static HashMap logOtrosArticulosMap= new HashMap();
    
	private static final String MOTIVO_ANULACION = "Modificación en la solicitud de cirugía";
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public ActionForward execute(   ActionMapping mapping,
                                                    ActionForm form,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response ) throws Exception
                                                    {
    	Connection con=null;
    	try {
    		if (response==null); 
    		if(form instanceof SolicitudesCxForm)
    		{
    			try
    			{
    				con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();   
    			}
    			catch(SQLException e)
    			{
    				logger.warn("No se pudo abrir la conexión"+e.toString());
    			}

    			request.getSession().setAttribute("ocultarEncabezadoSolCx", null);
    			PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
    			UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
    			SolicitudesCxForm cxForm =(SolicitudesCxForm)form;

    			cxForm.setMostrarImprimirAutorizacion(false);
    			cxForm.setListaNombresReportes(new ArrayList<String>());

    			String estado=cxForm.getEstado();
    			logger.info("\n\nEl estado en SOLICITUDES CX es------->"+estado+"\n");
    			logger.info("------------>"+cxForm.getCadenaFiltroBusqueda());
    			if(estado == null) {
    				cxForm.resetTodo(); 
    				logger.warn("Estado no valido dentro del flujo de Solicitudes Cx (null) ");
    				request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
    			} 
    			else if(estado.equals("empezar") ||estado.equals("empezarPYP") || estado.equals("guardar") || estado.equals("guardarModificar") || estado.equals("guardarAnular"))
    			{    
    				ActionForward validacionesGenerales = this.validacionesAccesoUsuario(con, paciente, mapping, request, usuario, cxForm);
    				if (validacionesGenerales != null)
    				{
    					UtilidadBD.cerrarConexion(con);
    					return validacionesGenerales ;
    				}
    			}   
    			if(estado.equals("modificar")  || estado.equals("guardarModificar") || estado.equals("anular") || estado.equals("guardarAnular"))
    			{
    				ActionForward validacionesModificacion = this.validacionesAccesoModificarSol(con, paciente, mapping, request, usuario, cxForm);
    				cxForm.setJustificacionesServicios(new HashMap());
    				if (validacionesModificacion != null)
    				{
    					UtilidadBD.cerrarConexion(con);
    					return validacionesModificacion ;
    				}
    			}
    			if (estado.equals("empezar"))
    			{
    				return this.accionEmpezar(cxForm,mapping, con, paciente, usuario);
    			}
    			else if (estado.equals("empezarPYP"))
    			{
    				return this.accionEmpezarPYP(cxForm,mapping, con, paciente);
    			}
    			else if(estado.equals("empezarContinuar"))
    			{
    				return accionEmpezarContinuar(con,cxForm,mapping,request);

    			}
    			else if(estado.equals("buscarPeticiones"))
    			{
    				//this.llenarFechaAdmisionOApertura(con, paciente.getCodigoCuenta(), cxForm);
    				return this.accionBuscarPeticiones(cxForm, mapping, con, paciente, usuario);
    			}
    			else if(estado.equals("continuarPaginaPrincipal"))
    			{
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaPrincipal");
    			}
    			else if(estado.equals("guardar"))
    			{
    				request.getSession().removeAttribute("MAPAJUS");
    				return this.accionGuardar(con, request, mapping, cxForm, paciente, usuario);
    			}
    			else if(estado.equals("ordenar"))
    			{
    				return this.accionOrdenar(cxForm,mapping,request,con);
    			}
    			else if(estado.equals("cargarPeticionCaso2"))
    			{
    				return this.cargarInfoPeticionCaso2(cxForm, mapping, con, paciente, usuario);
    			}
    			else if(estado.equals("resumen"))
    			{
    				return this.accionConsulta(mapping, request, cxForm, con, paciente, usuario, true);
    			}
    			else if(estado.equals("consultar"))
    			{
    				if ( UtilidadCadena.noEsVacio( request.getParameter("nroOrden") ) )
    				{
    					cxForm.setNroOrdenMedica( Integer.parseInt( request.getParameter("nroOrden") ) ); 
    				}
    				else{ cxForm.setNroOrdenMedica(-1); }

    				//siempre se va ha cargar el paciente
    				/*if( paciente == null || paciente.getTipoIdentificacionPersona().equals("") )
                { */   
    				Solicitud sol= new Solicitud();
    				sol.cargar(con, Integer.parseInt(cxForm.getNumeroSolicitud()));
    				Cuenta cuenta= new Cuenta();
    				cuenta.cargarCuenta(con, sol.getCodigoCuenta()+"");
    				paciente.setCodigoPersona(cuenta.getPaciente().getCodigoPersona());
    				paciente.cargar(con,cuenta.getPaciente().getCodigoPersona());
    				paciente.cargarPaciente(con, cuenta.getPaciente().getCodigoPersona(), usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
    				//} 
    				return this.accionConsulta(mapping, request, cxForm, con, paciente, usuario, false);
    			}
    			else if(estado.equals("modificar"))
    			{
    				return this.accionModificar(mapping, cxForm, paciente, con);
    			}
    			else if(estado.equals("guardarModificar"))
    			{
    				request.getSession().removeAttribute("MAPAJUS");
    				return this.accionGuardarModificar(con, request, mapping, cxForm, paciente, usuario);
    			}
    			else if(estado.equals("continuarPaginaModificar"))
    			{
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaModificar");
    			}
    			else if(estado.equals("continuarPaginaConsultar"))
    			{
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaConsulta");
    			}
    			else if(estado.equals("anular"))
    			{
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaConsulta");
    			}
    			else if(estado.equals("comenzarAnular"))
    			{
    				cxForm.resetAnulacion();
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaAnular");
    			}
    			else if(estado.equals("guardarAnular"))
    			{
    				return accionGuardarAnular(con, request, mapping, cxForm, usuario, paciente, response);
    			}
    			else if(estado.equals("respuestaSolCentroCostoExterno"))
    			{
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaConsulta");
    			}
    			else if(estado.equals("comenzarRespuestaSolCentroCostoExterno"))
    			{
    				cxForm.setResultadosRespuestaSolCx("");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaRespuesta");
    			}
    			else if(estado.equals("guardarRespuestaSolCentroCostoExterno"))
    			{
    				return accionGuardarRespuestaSolCentroCostoExterno(con, request, mapping, cxForm, usuario);
    			}
    			else if(estado.equals("volverListado"))
    			{
    				UtilidadBD.cerrarConexion(con);
    				if(UtilidadTexto.isEmpty(cxForm.getTipoBusquedaBotonVolver()))
    					cxForm.setTipoBusquedaBotonVolver("medico");

    				if(cxForm.getTipoBusquedaBotonVolver().equals("solicitudesPendientes"))
    					return this.accionEmpezar(cxForm,mapping, con, paciente, usuario);
    				else
    				{              	
    					if(!cxForm.getCadenaFiltroBusqueda().toString().trim().equals("")) {
    						response.sendRedirect("../solicitudes/listarSolicitudes.do?estado="+cxForm.getTipoBusquedaBotonVolver()+"&tipoServicio=modificar&cadenaFiltroBusqueda="+cxForm.getCadenaFiltroBusqueda());                		
    					}else {
    						//hermorhu - MT6095
    						//Se quita el parametro restriccion=solicitudesCx para que al volver al listado devuelva todas las solicitudes no solo las de cirugia (Consultar/Modificar Ordenes)
    						response.sendRedirect("../solicitudes/listarSolicitudes.do?estado="+cxForm.getTipoBusquedaBotonVolver()+"&tipoServicio=modificar");
    					}
    				}
    			}
    			else if(estado.equals("volverResumenAtenciones"))
    			{
    				request.getSession().removeAttribute("ocultarEncabezadoSolCx");
    				response.sendRedirect("../resumenAtenciones/resumenAtenciones.do?estado=cirugias");
    			}
    			else if(estado.equals("ordenarColumna"))
    			{
    				return this.accionOrdenarMapa(con,cxForm,mapping);
    			}
    			else if(estado.equals("adicionarJus"))
    			{
    				request.getSession().setAttribute("MAPAJUS", cxForm.getJustificacionesServicios());
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaPrincipal");
    			}
    			else if(estado.equals("adicionarJusModificar"))
    			{
    				request.getSession().setAttribute("MAPAJUS", cxForm.getJustificacionesServicios());
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaModificar");
    			}
    			else if(estado.equals("eliminarServicioConInfoParto"))
    			{
    				try
    				{
    					int numRegistros = Utilidades.convertirAEntero(cxForm.getServiciosMap("numRegistros").toString());
        				Integer indiceServicioConInfoParto = null;
        				for (int i = 0; i < numRegistros; i++){
        					if(cxForm.getServiciosMap("tienInfoParto_"+i)!=null){
        						indiceServicioConInfoParto = i;
        					}
        	    		}
        				cxForm.getServiciosMap().put("fueEliminadoServicio_"+indiceServicioConInfoParto, true);
        				Integer consecutivoInformacionParto = Utilidades.convertirAEntero(cxForm.getServiciosMap("consecutivoInfoParto_"+indiceServicioConInfoParto).toString());
        				HojaQuirurgica.eliminarInformacionParto(con, Utilidades.convertirAEntero(cxForm.getServiciosMap("codigoInfoParto_"+indiceServicioConInfoParto).toString()));
        				SqlBaseSolicitudesCxDao.listaSecuenciaSolCirugiaPorServicio = new ArrayList<Integer>();
        				accionGuardarModificar(con, request, mapping, cxForm, paciente, usuario);
        				con = UtilidadBD.abrirConexion();
        				HojaQuirurgica.migrarInformacionParto(con, SqlBaseSolicitudesCxDao.listaSecuenciaSolCirugiaPorServicio.get(0), consecutivoInformacionParto);
        				SqlBaseSolicitudesCxDao.listaSecuenciaSolCirugiaPorServicio = new ArrayList<Integer>();
					}
    				catch (Exception e)
    				{
    					logger.equals(e);
					}
    				return accionConsulta(mapping, request, cxForm, con, paciente, usuario, true);
    			}
    			else
    			{
    				cxForm.resetTodo(); 
    				logger.warn("Estado no valido dentro del flujo de Solicitudes Cx (null) ");
    				request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
    				UtilidadBD.cerrarConexion(con);
    				return mapping.findForward("paginaError");
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
    
    
    /**
     * Método implementado para continuar con el flujo de la solicitud al seleccionar un servicio
     * @param con
     * @param cxForm
     * @param mapping
     * @param request
     * @return
     */
    @SuppressWarnings({ "deprecation", "rawtypes" })
	private ActionForward accionEmpezarContinuar(Connection con, SolicitudesCxForm cxForm, ActionMapping mapping, HttpServletRequest request) 
    {
    	//****************SE VERIFICA QUE SE HAYA SELECCIONADO UN SERVICIO QUIRURGICO COMO MÍNIMO******************************
    	ActionErrors errores = new ActionErrors();
    	int numServQx = cxForm.numeroServicioQxYPartosEncontrados(con);
    	
    	if(numServQx==0&&cxForm.getNumeroFilasMapaServicios()>0)
    	{
    		//SE eliminan todos los servicios
    		for(int i=0;i<cxForm.getNumeroFilasMapaServicios();i++)
    			cxForm.setServiciosMap("fueEliminadoServicio_"+i, "true");
    		
    		errores.add("",new ActionMessage("errors.minimoCampos","la selección de un servicio quirúrgico o partos y cesáreas","solicitud de cirugías"));
    		saveErrors(request, errores);
    	}
    		
    	//*********************************************************************************************************************
    	
    	
		///ahorrar recursos en caso de que se seleccione nuevo desde
        //el listado de las solicitudes pendientes;
        cxForm.setSolicitudesPendientesMap(new HashMap());
        
        
        logger.info("valor del mapa >> "+cxForm.getServiciosMap());
                
        UtilidadBD.closeConnection(con);
        return mapping.findForward("paginaSeleccionServicios");
	}



	/**
     * Este método especifica las acciones a realizar en el estado
     * empezar.
     * 
     * @param forma SolicitudesCXForm
     *              para pre-llenar datos si es necesario
     * @param request HttpServletRequest para obtener los 
     *              datos
     * @param mapping Mapping para manejar la navegación
     * @param con Conexión con la fuente de datos
     * @return ActionForward a la página  "seleccionarServicios.jsp"
     * @throws SQLException
     */
    @SuppressWarnings("rawtypes")
	private ActionForward accionEmpezar(    SolicitudesCxForm cxForm, 
                                                                ActionMapping mapping,
                                                                Connection con, 
                                                                PersonaBasica paciente, UsuarioBasico usuario) throws SQLException
    {
    	if(con.isClosed())
    	{
    		con=UtilidadBD.abrirConexion();
    	}
    	cxForm.setJustificacionesServicios(new HashMap());
    	cxForm.setCodigoServicioPostular(ConstantesBD.codigoNuncaValido);
    	cxForm.setPostularServicio(false);
    	cxForm.setSolPYP(false);
        cxForm.resetTodo();
        cxForm.setEstado("empezar");
        cxForm.setFechaSolicitud(UtilidadFecha.getFechaActual());
        cxForm.setHoraSolicitud(UtilidadFecha.getHoraActual());
        cxForm.setTiposAnestesia(Utilidades.obtenerTiposAnestesia(con, ""));
        this.llenarFechaAdmisionOApertura(con,paciente.getCodigoCuenta(), cxForm);
        
        //se evalua si existen solicitudes pendientes, en caso de ser asi entonces se
        //debe mostrar la pagina del listado
        SolicitudesCx mundoSolicitudesCx= new SolicitudesCx();
        cxForm.setSolicitudesPendientesMap(mundoSolicitudesCx.listadoSolicitudesCxPendientes(con, paciente.getCodigoPersona()+""));
        
        cxForm.setCodSexo(paciente.getCodigoSexo());
        
		//MT5061- hermorhu
		ArrayList<ProfesionalEspecialidadesDto> listProfesionalEspecialidades = null;
		
		AdministracionFacade administracionFacade = new AdministracionFacade();
		
		try {
			listProfesionalEspecialidades = (ArrayList<ProfesionalEspecialidadesDto>) administracionFacade.consultarProfesionalesEspecialidades(usuario.getCodigoInstitucionInt());
		} catch (IPSException ipse) {
			Log4JManager.error(ipse.getMessage(), ipse);
		}
		
		cxForm.setProfesionalEspecialidades(listProfesionalEspecialidades);	
        
        UtilidadBD.cerrarConexion(con);
        if(cxForm.getSolicitudesPendientesMap("numRegistros")!=null&&Integer.parseInt(cxForm.getSolicitudesPendientesMap("numRegistros").toString())>0)
        {
            cxForm.setMostrarLinkVolverListadoPeticiones(true);
            return mapping.findForward("paginaListadoSolicitudesPendientes");
        }
        return mapping.findForward("paginaSeleccionServicios");      
    }
    
    /**
     * Este método especifica las acciones a realizar en el estado
     * empezar.
     * 
     * @param forma SolicitudesCXForm
     *              para pre-llenar datos si es necesario
     * @param request HttpServletRequest para obtener los 
     *              datos
     * @param mapping Mapping para manejar la navegación
     * @param con Conexión con la fuente de datos
     * @return ActionForward a la página  "seleccionarServicios.jsp"
     * @throws SQLException
     */
    private ActionForward accionEmpezarPYP(    SolicitudesCxForm cxForm, 
                                                                ActionMapping mapping,
                                                                Connection con, 
                                                                PersonaBasica paciente) throws SQLException
    {
    	if(con.isClosed())
    	{
    		con=UtilidadBD.abrirConexion();
    	}
        cxForm.resetTodo();
        cxForm.setEstado("empezar");
        cxForm.setFechaSolicitud(UtilidadFecha.getFechaActual());
        cxForm.setHoraSolicitud(UtilidadFecha.getHoraActual());
        this.llenarFechaAdmisionOApertura(con,paciente.getCodigoCuenta(), cxForm);
        
        //se evalua si existen solicitudes pendientes, en caso de ser asi entonces se
        //debe mostrar la pagina del listado
        SolicitudesCx mundoSolicitudesCx= new SolicitudesCx();
        cxForm.setSolicitudesPendientesMap(mundoSolicitudesCx.listadoSolicitudesCxPendientes(con, paciente.getCodigoPersona()+""));
        
        
        UtilidadBD.cerrarConexion(con);
        return mapping.findForward("paginaSeleccionServicios");      
    }
    /**
     * Este método especifica las acciones a realizar en el estado
     * empezar.
     * 
     * @param forma SolicitudesCXForm
     *              para pre-llenar datos si es necesario
     * @param request HttpServletRequest para obtener los 
     *              datos
     * @param mapping Mapping para manejar la navegación
     * @param con Conexión con la fuente de datos
     * @param usuario 
     * @return ActionForward 
     * @throws SQLException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private ActionForward accionBuscarPeticiones(    SolicitudesCxForm cxForm, 
                                                                            ActionMapping mapping,
                                                                            Connection con,
                                                                            PersonaBasica paciente, UsuarioBasico usuario
                                                                         ) throws SQLException, IPSException
    {
        SolicitudesCx mundo = new SolicitudesCx();
        String codigosServiciosSeparadosPorComas="";
        String codigosPeticionesSeparadosPorComas="";
        cxForm.setEstado("continuarPaginaPrincipal");
        //cxForm.setTiposAnestesia(Utilidades.obtenerTiposAnestesia(con, ""));
        for (int i=0; i<cxForm.getNumeroFilasMapaServicios(); i ++)
        {    
            if(!cxForm.getServiciosMap("fueEliminadoServicio_"+i).toString().equals("true"))
            {
                codigosServiciosSeparadosPorComas+= (cxForm.getServiciosMap("codigoServicio_"+i)+",");
            }
        } 
        codigosServiciosSeparadosPorComas+="-1";
        Vector vector=mundo.cargarPeticionesSinOrdenAsociada(con, paciente.getCodigoPersona(), codigosServiciosSeparadosPorComas);
        
        //EN EL CASO DE QUE SEA VACIO ENTONCES NO EXISTE PETICION  ---- EL CASO ES 1-----
        if(vector.isEmpty())
        {    
            cxForm.getProfesionalesMap().put("numeroProfesionales", "0");
            cxForm.setCasoFlujo(1);
            UtilidadBD.cerrarConexion(con);
            return mapping.findForward("paginaPrincipal");
        }   
        //EN EL CASO DE QUE EXISTE 1 PETICION  ---- EL CASO ES 2-----
        else if(vector.size()==1)
        {          	
           cxForm.setCodigoPeticion(vector.get(0)+"");
           return this.cargarInfoPeticionCaso2(cxForm, mapping, con, paciente, usuario);
        }    
        //EN CASO DE + DE UNA PETICION --EL CASO 3 ----- SE ENVIA A PAGINA DE SELECCION DE LA PETICION
        else 
        {    
            Peticion mundoPeticion= new Peticion();
            for(int i=0; i<vector.size(); i++)
            {    
                codigosPeticionesSeparadosPorComas+= vector.get(i)+", ";
            }    
            codigosPeticionesSeparadosPorComas+=" -1";
            cxForm.setCasoFlujo(3);            
            
            cxForm.setCol(mundoPeticion.cargarEncabezadoPeticionSinRestricciones(con, codigosPeticionesSeparadosPorComas,null));
            UtilidadBD.cerrarConexion(con);
            logger.info("valor al final 3 >> ");
        	Utilidades.imprimirMapa(cxForm.getServiciosMap());
            return mapping.findForward("paginaListarPeticiones")  ;
        }      
        
     }
    
    /**
     * metodo que carga la informacion de la peticion y hace el forward a la pagina principal de ordenes cx
     * @param cxForm
     * @param mapping
     * @param con
     * @param paciente
     * @param usuario 
     * @return
     * @throws SQLException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private ActionForward cargarInfoPeticionCaso2(    SolicitudesCxForm cxForm, 
                                                                            ActionMapping mapping,
                                                                            Connection con,
                                                                            PersonaBasica paciente, 
                                                                            UsuarioBasico usuario
                                                                        ) throws SQLException, IPSException
    {
    	
    	logger.info("\n\nServiciosMap");
    	Utilidades.imprimirMapa(cxForm.getServiciosMap());
    	
    	//Asegurar parametros de la Justificacion No POS
    	HashMap<String , Object> datosJustificacion = new HashMap<String, Object>();
    	for(int i=0;i<cxForm.getNumeroFilasMapaServicios();i++)
    	{
    		datosJustificacion.put("justificar_"+i, cxForm.getServiciosMap("justificar_"+i));
    		datosJustificacion.put("cobertura_"+i, cxForm.getServiciosMap("cobertura_"+i));
    		datosJustificacion.put("subcuenta_"+i, cxForm.getServiciosMap("subcuenta_"+i));
    		datosJustificacion.put("esPos_"+i, cxForm.getServiciosMap("esPos_"+i));
    		datosJustificacion.put("codigoServicio_"+i, cxForm.getServiciosMap("codigoServicio_"+i));
    	}
    	
        cxForm.setEstado("continuarPaginaPrincipal");
        Peticion mundoPeticion= new Peticion();
        
        //se cargan los servicios
        cxForm.resetBusquedaServicios();
        cxForm.setServiciosMap(mundoPeticion.cargarServiciosDadasPeticiones(con, cxForm.getCodigoPeticion()));
        //se cargan los profesionales
        //cxForm.setProfesionalesMap(mundoPeticion.cargarProfesionalesPeticion(con, Integer.parseInt(cxForm.getCodigoPeticion())));
        
        //se cargan los articulos PRAMETRIZADOS
        cxForm.setArticulosMap(mundoPeticion.cargarMaterialesEspeciales2(con, cxForm.getCodigoPeticion()));
        
        
        // se carga la información del encabezado de la petion
        HashMap peticionMap= mundoPeticion.cargarDatosGeneralesPeticion2(con, paciente.getCodigoPersona(), Integer.parseInt(cxForm.getCodigoPeticion()),null);
        cxForm.setTipoAnestesia(Utilidades.convertirAEntero(peticionMap.get("tipoanestesia_0")+""));
        if(!(peticionMap.get("fecha_cirugia_0")+"").trim().equals(""))
        {    
            cxForm.setFechaEstimadaCirugia(UtilidadFecha.conversionFormatoFechaAAp((peticionMap.get("fecha_cirugia_0")+"")));
        }    
        cxForm.setDuracionAproximadaCirugia(UtilidadFecha.convertirHoraACincoCaracteres((peticionMap.get("duracion_0")+"")));
        if((peticionMap.get("requiere_uci_0")+"").trim().equals("f") || (peticionMap.get("requiere_uci_0")+"").trim().equals("false"))
        {    
            cxForm.setRequiereUci("false");
        }
        else if((peticionMap.get("requiere_uci_0")+"").trim().equals("t") || (peticionMap.get("requiere_uci_0")+"").trim().equals("true"))
            cxForm.setRequiereUci("true");
        else
            cxForm.setRequiereUci("");
        
        if((peticionMap.get("codigo_estado_0")+"").trim().equals(ConstantesBD.codigoEstadoPeticionPendiente+""))
            cxForm.setPuedoModificarPeticion(true);
        else
            cxForm.setPuedoModificarPeticion(false);
        
        // -Consultar los profesionales asociados a la peticion a consultar
        HashMap mapaMundo=mundoPeticion.cargarProfesionalesPeticion(con, Integer.parseInt(cxForm.getCodigoPeticion()),null);
        
        int numeroRegistros=((Integer)mapaMundo.get("numRegistros")).intValue();
        cxForm.getProfesionalesMap().put("numeroProfesionales", numeroRegistros+"");
        for (int i=0;i<numeroRegistros;i++)
        {
            cxForm.getProfesionalesMap().put("profesional_"+i, mapaMundo.get("codigo_medico_"+i));
            cxForm.getProfesionalesMap().put("especialidades_"+i, mapaMundo.get("codigo_especialidad_"+i));
            cxForm.getProfesionalesMap().put("tipo_participante_"+i, mapaMundo.get("codigo_tipo_profesional_"+i));
            //mapaForma.put(""+i, mapaMundo.get(""+i));
        }
        
        //Recuperar parametros de la justificacion No POS
        cxForm.setServiciosMap(recuperarParametrosJustificacion(con, datosJustificacion, cxForm.getServiciosMap(), paciente, usuario));
        
        cxForm.setCasoFlujo(2);
        UtilidadBD.cerrarConexion(con);
        
        return mapping.findForward("paginaPrincipal");
    }
    
    /**
     * recuperarParametrosJustificacion
     * @param con
     * @param datosJustificacion
     * @param serviciosMap
     * @param paciente
     * @param usuario
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private HashMap recuperarParametrosJustificacion(Connection con, HashMap<String, Object> datosJustificacion, HashMap serviciosMap, PersonaBasica paciente, UsuarioBasico usuario) throws IPSException {
    	
    	boolean valProfesionalSalud = UtilidadesHistoriaClinica.validarEspecialidadProfesionalSalud(con, usuario, false);
    	
    	for(int i=0; i<Utilidades.convertirAEntero(serviciosMap.get("numRegistros")+""); i++){
    		for(int j=0; j<Utilidades.convertirAEntero(datosJustificacion.get("numRegistros")+""); j++){
    			if (serviciosMap.get("codigoServicio_"+i).toString().equals(datosJustificacion.get("codigoServicio_"+j))){
    				serviciosMap.put("justificar_"+i, datosJustificacion.get("justificar_"+j));
    				serviciosMap.put("cobertura_"+i, datosJustificacion.get("cobertura_"+j));
    				serviciosMap.put("subcuenta_"+i, datosJustificacion.get("subcuenta_"+j));
    			}	
    		}
    		if(!serviciosMap.containsKey("justificar")){
    			
    			String justificar="";

        		//Evaluamos si el elemento es No POS
            	if (serviciosMap.get("esPos_"+i).toString().equals("NOPOS")){
            		
            		//Evaluamos la cobertura del Servicio
            		InfoResponsableCobertura infoResponsableCobertura= new InfoResponsableCobertura();
            		infoResponsableCobertura = Cobertura.validacionCoberturaServicio(con, paciente.getCodigoIngreso()+"", paciente.getCodigoUltimaViaIngreso(), paciente.getCodigoTipoPaciente(), Utilidades.convertirAEntero(serviciosMap.get("codigoServicio_"+i)+""), usuario.getCodigoInstitucionInt(), false, "" /*subCuentaCoberturaOPCIONAL*/);
            		serviciosMap.put("cobertura", infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo()+"");
            		serviciosMap.put("subcuenta", infoResponsableCobertura.getDtoSubCuenta().getSubCuenta()+"");

            		//Evaluamos si el convenio que cubre el servicio requiere de justificación de servicios
            		if (UtilidadesFacturacion.requiereJustificacioServ(con, infoResponsableCobertura.getDtoSubCuenta().getConvenio().getCodigo())){
            			justificar="true";
            			
            			// Validacion 'Especialidad profesional de la salud'
            			if (!valProfesionalSalud){
            				justificar="pendiente";
            			}
            		} else {
            			justificar="false";
            		}
            	}
            	serviciosMap.put("justificar", justificar);
    		}
    	}
    	return serviciosMap;
	}


	/**
     * Validaciones de acceso para modificar una solicitud cx
     * @param con
     * @param paciente
     * @param map
     * @param req
     * @param medico
     * @param cxForm
     * @return
     */
    @SuppressWarnings({ "deprecation", "rawtypes" })
	protected ActionForward validacionesAccesoModificarSol(Connection con, PersonaBasica paciente, ActionMapping map, HttpServletRequest req, UsuarioBasico medico, SolicitudesCxForm cxForm)
    {
        try
        {
            Solicitud sol= new Solicitud();
            sol.cargar(con, Integer.parseInt(cxForm.getNumeroSolicitud()));
            ActionErrors errores = new ActionErrors();
            
            if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion ||
               paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias ||
               paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoAmbulatorios
               )
            {    
            	String mensaje = UtilidadValidacion.esMedicoTratante(con,medico,paciente);
                if(!mensaje.equals(""))
                {
                	//se verifica si no se ha definido la ocupación medico especialista y general
                	// en parámetros generales
                	if(mensaje.equals("errors.noOcupacionMedica"))
                		errores.add("no se ha definido ocupación médico", new ActionMessage(mensaje));
                	else
                		errores.add("[No es medico tratante] Opcion disponible solo para el medico tratante", new ActionMessage("error.solicitudCx.medicoNoTratante"));
                    logger.warn("entra al error de Opcion disponible solo para el medico tratante");
                    saveErrors(req, errores);   
                }
            }
            /*
             * Tarea 3185
            else if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoAmbulatorios)
            {
                //se evalua el profesional que solicita
                if(sol.getCodigoMedicoSolicitante()!=medico.getCodigoPersona())
                {
                    errores.add("[No es profesional solicita] Opcion disponible solo para el profesional solicita", new ActionMessage("error.solicitudCx.profesionalNoSolicita"));
                    logger.warn("entra al error de Opcion disponible solo para el medico tratante");
                    saveErrors(req, errores);   
                }
            }
            */
            if(sol.getEstadoHistoriaClinica().getCodigo()!=ConstantesBD.codigoEstadoHCSolicitada)
            {
                errores.add("[error.solicitudCx.estadoHNoSolicitada]", new ActionMessage("error.solicitudCx.estadoHNoSolicitada"));
                logger.warn("entra al error de [error.solicitudCx.estadoHNoSolicitada]");
                saveErrors(req, errores);   
            }
            if(UtilidadValidacion.esHojaQxAsociadaSolicitud(con, cxForm.getNumeroSolicitud()))
            {
                errores.add("error.solicitudCx.existeInfoOtraFunc [Hoja Qx]", new ActionMessage("error.solicitudCx.existeInfoOtraFunc", "Hoja Qx"));
                logger.warn("entra al error de [error.solicitudCx.existeInfoOtraFunc] Hoja Qx");
                saveErrors(req, errores);
            }
            if(UtilidadValidacion.esHojaAnestesiaAsociadaSolicitud(con, cxForm.getNumeroSolicitud()))
            {
                errores.add("error.solicitudCx.existeInfoOtraFunc [Hoja Anestesia]", new ActionMessage("error.solicitudCx.existeInfoOtraFunc", "Hoja Anestesia"));
                logger.warn("entra al error de [error.solicitudCx.existeInfoOtraFunc] Hoja Anestesia");
                saveErrors(req, errores);
            }
            if(UtilidadValidacion.esNotaRecuperacionAsociadaSolicitud(con, cxForm.getNumeroSolicitud()))
            {
                errores.add("error.solicitudCx.existeInfoOtraFunc [Notas Recuperacion]", new ActionMessage("error.solicitudCx.existeInfoOtraFunc", "Notas Recuperación"));
                logger.warn("entra al error de [error.solicitudCx.existeInfoOtraFunc] Notas recuperación");
                saveErrors(req, errores);
            }
            if(UtilidadValidacion.esNotaGeneralEnfAsociadaSolicitud(con, cxForm.getNumeroSolicitud()))
            {
                errores.add("error.solicitudCx.existeInfoOtraFunc [Notas Generales]", new ActionMessage("error.solicitudCx.existeInfoOtraFunc", "Notas Generales"));
                logger.warn("entra al error de [error.solicitudCx.existeInfoOtraFunc] Notas Generales");
                saveErrors(req, errores);
            }
            if(UtilidadValidacion.esConsumoMaterialesAsociadaSolicitud(con, cxForm.getNumeroSolicitud()))
            {
                errores.add("error.solicitudCx.existeInfoOtraFunc [Consumo Materiales]", new ActionMessage("error.solicitudCx.existeInfoOtraFunc", "Consumo Materiales"));
                logger.warn("entra al error de [error.solicitudCx.existeInfoOtraFunc] Consumo Materiales");
                saveErrors(req, errores);
            }
            
            //en el caso de que no sea externo entonces se tiene una peticion asociada
            if(sol.getCentroCostoSolicitado().getCodigo()!=ConstantesBD.codigoCentroCostoExternos)
            {
                //se carga la información del encabezado de la petion
                Peticion mundoPeticion= new Peticion();
                HashMap peticionMap= mundoPeticion.cargarDatosGeneralesPeticion2(con, paciente.getCodigoPersona(), Integer.parseInt(cxForm.getCodigoPeticion()),null);
                cxForm.setTipoAnestesia(Utilidades.convertirAEntero(peticionMap.get("tipoanestesia_0")+""));
                if((peticionMap.get("codigo_estado_0")+"").trim().equals(ConstantesBD.codigoEstadoPeticionPendiente+"") ||
                   (peticionMap.get("codigo_estado_0")+"").trim().equals(ConstantesBD.codigoEstadoPeticionProgramada+"") ||
                   (peticionMap.get("codigo_estado_0")+"").trim().equals(ConstantesBD.codigoEstadoPeticionReprogramada+"") )
                {}
                else
                {
                    errores.add("error.solicitudCx.estadoPedido", new ActionMessage("error.solicitudCx.estadoPedido", peticionMap.get("estado_peticion_0")+""));
                    logger.warn("entra al error de [error.solicitudCx.estadoPedido]");
                    saveErrors(req, errores);
                }
            }
            
            if(!errores.isEmpty())
            {    
                if(cxForm.getEstado().equals("guardarModificar"))
                {
                    cxForm.setEstado("continuarPaginaModificar");
                    UtilidadBD.cerrarConexion(con);
                    return map.findForward("paginaModificar");
                }
                else
                {    
                    cxForm.setEstado("continuarPaginaConsultar");
                    UtilidadBD.cerrarConexion(con);
                    return map.findForward("paginaConsulta");
                }    
            }    
        }
        catch(SQLException e)
        {
           return null;
        }
        return null;
    }
    
    /**
     * Método que valida el acceso a la funcionalidad
     * @param con
     * @param paciente
     * @param map
     * @param req
     * @param cxForm 
     * @return
     */
	protected ActionForward validacionesAccesoUsuario(Connection con, PersonaBasica paciente, ActionMapping map, HttpServletRequest req, UsuarioBasico medico, SolicitudesCxForm cxForm)
    {
        try
        {
        	// Si el estado es empezar no viene de pyp
        	if(cxForm.getEstado().equals("empezar"))
        		cxForm.setSolPYP(false);
        	
            if(medico == null)
            {
                return ComunAction.accionSalirCasoError(map, req, con, logger, "No existe el usuario", "errors.usuario.noCargado", true);
            }
            else
            if( !UtilidadValidacion.esProfesionalSalud(medico) )
            {
                return ComunAction.accionSalirCasoError(map, req, con, logger, "Usuario  no es profesional de la salud", "errors.usuario.noAutorizado", true);
            }
            else
            if( paciente == null || paciente.getTipoIdentificacionPersona().equals("") )
            {
                return ComunAction.accionSalirCasoError(map, req, con, logger, "paciente null o sin  id", "errors.paciente.noCargado", true);
            }
            else 
            if((paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias
              || paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion)
              && !cxForm.isSolPYP())
            {  
            	String mensaje = UtilidadValidacion.esMedicoTratante(con,medico,paciente);
            	boolean esAdjunto=UtilidadValidacion.esAdjuntoCuenta(con, paciente.getCodigoCuenta(), medico.getLoginUsuario());
            	if(!mensaje.equals(""))
                {
            		if(!esAdjunto)
            		{	
            			return ComunAction.accionSalirCasoError(map,req,con, logger, "Opcion disponible solo para el medico tratante o adjunto ", "error.validacionessolicitud.medicoNoTratanteNiAdjunto", true);
            		}	
                }
            }
            else
            if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoAmbulatorios)
            {
                if(!UtilidadValidacion.esOcupacionQueRealizaCx(con, medico.getCodigoOcupacionMedica(), medico.getCodigoInstitucionInt()))
                {
                    return ComunAction.accionSalirCasoError(map, req, con, logger, "opcion no disponible", "error.solicitudCx.ambulatorios", true);
                }
            }
            else
            if (paciente.getCodigoCuenta()<1)
            {
                return ComunAction.accionSalirCasoError(map, req, con, logger, "El paciente no tiene la cuenta abierta", "errors.paciente.cuentaNoAbierta", true);
            }
            else
            if (paciente.getCodigoAdmision()>0)
            {
                if (UtilidadValidacion.tieneEgreso(con, paciente.getCodigoCuenta()))
                {
                    return ComunAction.accionSalirCasoError(map, req, con, logger, "El paciente es de urg/hosp pero ya tiene egreso médico", "error.solicitudgeneral.tieneOrdenSalida", true);
                }
                else if (!UtilidadValidacion.tieneValoraciones(con, paciente.getCodigoCuenta()))
                {
                    return ComunAction.accionSalirCasoError(map, req, con, logger, "El paciente es de urg/hosp pero no tiene valoracion inicial", "error.solicitudgeneral.tieneValoracionInicial", true);
                }
            }
            /*********************************************************************************************************************/
        	/** Se verifica que el ingreso no sea realizado atraves de entidades subcontratadas*/
			if (paciente.esIngresoEntidadSubcontratada())
			{
				req.setAttribute("codigoDescripcionError", "error.ingresoEntidadSubContratada");
				return map.findForward("paginaError");
			}
			/*********************************************************************************************************************/
            Cuenta objectCuentaActual= new Cuenta();
            objectCuentaActual.cargarCuenta(con, paciente.getCodigoCuenta()+"");
            
            //SE EVALUA LA VIA DE INGRESO
            if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias
               || paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion 
               || paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoAmbulatorios )
            {}
            else
            {
                logger.warn("Via de ingreso no válido (null)");           
                req.setAttribute("codigoDescripcionError", "error.solicitudCx.viaIngresoNoValida");
                UtilidadBD.cerrarConexion(con);
                return map.findForward("paginaError");
            }
            
            //**************************    VALIDACIONES DE LA CUENTA ACTUAL 
            //**************************    SI ESTA EN ESTADO DE FACTURACION
            //**************************    o SI ESTA EN ESTADO DE DISTRICION
            //primero se evalua si la cuenta actual esta en proceso de facturacion o en proceso de distribcion
            
            if(objectCuentaActual.getCodigoEstadoCuenta().equals(ConstantesBD.codigoEstadoCuentaProcesoFacturacion+""))
            {
                return ComunAction.accionSalirCasoError(map, req, con, logger, "Esta cuenta esta siendo facturada", "error.facturacion.cuentaEnProcesoFact1", true);
            }
            else if(objectCuentaActual.getCodigoEstadoCuenta().equals(""+ConstantesBD.codigoEstadoCuentaProcesoDistribucion))
            {
                return ComunAction.accionSalirCasoError(map, req, con, logger, "Esta cuenta esta siendo distribuida", "error.distribucion.cuentaEnProcesoDistricion", true);
            }
            
//          VALIDAR PARAMETRO GENERAL REGISTRO EVOLUCIONES PARA ORDENES -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
    		String  parametroEvoluciones = Solicitud.consultarParametroEvoluciones(con); 
    		boolean validacionViaIngreso=false;
    		
    		if(parametroEvoluciones.equals(ConstantesBD.acronimoSi))
    		{
    			logger.info("<<<<<<< PARAMETRO EVOLUCIONES ->>>>>>"+parametroEvoluciones+" | ESTA ACTIVADA | ");
    			
    			logger.info("FECHAAAAAAAA->"+UtilidadFecha.conversionFormatoFechaABD(cxForm.getFechaSolicitud())+"<-");
    			
    			int codigoCuenta = paciente.getCodigoCuenta();
    			
    			if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion)
    			{
    				logger.info("ES VIA INGRESO HOSPITALIZACION");
    				validacionViaIngreso=true;
    			}
    			else
    				if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias && 
    						UtilidadValidacion.tieneValoracionConConductaCamaObservacion(con, codigoCuenta))
    				{
    					logger.info("ES VIA INGRESO URGENCIAS EN OBSERVACION");
    					validacionViaIngreso=true;
    				}
    			
    			// PREGUNTO SI PASA EL FILTRO DE LA VIA INGRESO
    			if(validacionViaIngreso == true)
    			{
    				String fechaValoracionInicial = UtilidadesOrdenesMedicas.consultarFechaValoracionInicial(con, paciente.getCodigoCuenta());
    				logger.info(".......... FECHA VALORACION INICIAL ----->"+fechaValoracionInicial);
    				logger.info(".......... FECHA SOLICITUD--------- ----->"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
    				if(! fechaValoracionInicial.equals(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())))
    				{
    					// PERMITO GENERAR LA SOLICITUD SIN VALIDAR AUE EXISTAN LAS EVOLUCIONES
    					boolean evoluciones = UtilidadValidacion.tieneEvolucionesParaElDia(con, paciente.getCodigoCuenta(), UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
    					
    					if(evoluciones == false)
    					{
    						return ComunAction.accionSalirCasoError(map, req, con, logger, "Registrar Evolucion al Paciente antes de Continuar", "error.solicitudgeneral.validarEvolucion", true);
    					}
    					//	return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Registrar Evolucion al Paciente antes de Continuar", "error.solicitudgeneral.validarEvolucion", true);
    				}
    			}
    		}
    		
    		//-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
            
            
            
        }
        catch(SQLException e)
        {
           return null;
        }
        return null;
    }
    
    /**
     * Método privado que inserta la solicitud básica en una transaccion, retorna el numeroSolicitud
     * @param con
     * @param cxForm
     * @param paciente
     * @param user
     * @return numeroSolicitud
     * @throws SQLException
     */
    private int insertarSolicitudBasicaTransaccional(   Connection con, 
                                                                            SolicitudesCxForm cxForm,
                                                                            PersonaBasica paciente,
                                                                            UsuarioBasico user
                                                                           ) throws SQLException
    {
        Solicitud objectSolicitud= new Solicitud();
        int numeroSolicitudInsertado=0;
        objectSolicitud.clean();
        objectSolicitud.setFechaSolicitud(cxForm.getFechaSolicitud());
        objectSolicitud.setHoraSolicitud(cxForm.getHoraSolicitud());
        objectSolicitud.setTipoSolicitud(new InfoDatosInt(ConstantesBD.codigoTipoSolicitudCirugia));
        //objectSolicitud.setNumeroAutorizacion(cxForm.getAutorizacion());
        objectSolicitud.setEspecialidadSolicitante(new InfoDatosInt(cxForm.getCodigoEspecialidadOrdena()));
        objectSolicitud.setOcupacionSolicitado(new InfoDatosInt(ConstantesBD.codigoOcupacionMedicaNinguna));
        objectSolicitud.setCentroCostoSolicitante(new InfoDatosInt(paciente.getCodigoArea()));
        objectSolicitud.setCodigoMedicoSolicitante(user.getCodigoPersona());
        
        objectSolicitud.setCentroCostoSolicitado(new InfoDatosInt(cxForm.getCodigoCentroCostoSolicitado()));
        objectSolicitud.setCodigoCuenta(paciente.getCodigoCuenta());
        
        if(cxForm.getCodigoCentroCostoSolicitado()== ConstantesBD.codigoCentroCostoExternos){
            objectSolicitud.setCobrable(false);
        }else{
            objectSolicitud.setCobrable(true);
        }
        
        objectSolicitud.setVaAEpicrisis(true);
        objectSolicitud.setUrgente(cxForm.getUrgente());
        objectSolicitud.setEstadoHistoriaClinica(new InfoDatosInt(ConstantesBD.codigoEstadoHCSolicitada));
        objectSolicitud.setSolPYP(cxForm.isSolPYP());
        
        //Guarda la informacion de la Justificacion de la Solicitud
        objectSolicitud.setJustificacionSolicitud(
        		UtilidadTexto.agregarTextoAObservacionFechaGrabacion(
        				cxForm.getJustificacionSolicitud(),
        				"",
        				user,
        				false) );
        
        //Se agrega diagnóstico a la solicitud por Anexo 174 V 1.50 
        DtoDiagnostico dtoDiagnostico = Utilidades.getDiagnosticoPacienteCuenta(paciente.getCodigoCuenta());
	  	objectSolicitud.setDtoDiagnostico(dtoDiagnostico);
	   	cxForm.setDtoDiagnostico(dtoDiagnostico);	   	
        try
        {
            numeroSolicitudInsertado=objectSolicitud.insertarSolicitudGeneralTransaccional(con, ConstantesBD.inicioTransaccion);
            
            /**
             * Se inserta el registro de alerta para registro de enfermería MT-3438
             */
            if(numeroSolicitudInsertado > 0 && UtilidadValidacion.esMedico(user).equals("")) {
				OrdenMedica ordenMedica = new OrdenMedica();
				ordenMedica.registrarAlertaOrdenMedica(con, 
					ConstantesBD.seccionCirugias, 
					new Long(paciente.getCodigoCuenta()), user.getLoginUsuario());
            }
        }
        catch(SQLException sqle)
        {
            logger.warn("Error en la transaccion del insert en la solicitud básica");
            return 0;
        }
        return numeroSolicitudInsertado;
    }
    
    /**
     * metodo que llena la fecha de admision o apertura dependiendo de la via de ingreso, estos campos son requeridos para
     * las validaciones de inserción 
     * @param con
     * @param codigoCuenta
     * @param cxForm
     */
    private void llenarFechaAdmisionOApertura(Connection con, int codigoCuenta, SolicitudesCxForm cxForm)
    {
        Cuenta cuenta = new Cuenta();
        try {
            cuenta.cargarCuenta(con,codigoCuenta+"");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if((cuenta.getCodigoViaIngreso().equals(ConstantesBD.codigoViaIngresoAmbulatorios+"")))
        {
            cxForm.setFechaAdmisionOApertura(UtilidadFecha.conversionFormatoFechaAAp(UtilidadValidacion.obtenerFechaAperturaCuenta(con, codigoCuenta)));
            cxForm.setHoraAdmisionOApertura(UtilidadFecha.convertirHoraACincoCaracteres(cuenta.getHoraApertura()));
            cxForm.setComentario("de la apertura de la cuenta");
        }
        else
        {    
            if(cuenta.getCodigoViaIngreso().equals(ConstantesBD.codigoViaIngresoHospitalizacion+"") || 
               cuenta.getCodigoViaIngreso().equals(ConstantesBD.codigoViaIngresoUrgencias+""))
            {
                try {
                    cxForm.setFechaAdmisionOApertura(UtilidadFecha.conversionFormatoFechaAAp(UtilidadValidacion.getFechaAdmision(con, Integer.parseInt(cuenta.getIdCuenta()))));
                    cxForm.setHoraAdmisionOApertura(UtilidadFecha.convertirHoraACincoCaracteres(UtilidadValidacion.getHoraAdmision(con, Integer.parseInt(cuenta.getIdCuenta()))));
                } catch (NumberFormatException e1) {
                    e1.printStackTrace();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                cxForm.setComentario("de la admisión");
            }
        }   
    }
    
    /**
     * Método que centraliza todos los inserts para las solicitudes de cx
     * @param con
     * @param request
     * @param mapping
     * @param cxForm
     * @param paciente
     * @param user
     * @return
     * @throws SQLException
     */
    @SuppressWarnings({"rawtypes", "deprecation" })
	private ActionForward accionGuardar   (   Connection con, 
                                                                  HttpServletRequest request,
                                                                  ActionMapping mapping, 
                                                                  SolicitudesCxForm cxForm,
                                                                  PersonaBasica paciente,
                                                                  UsuarioBasico user
                                                              ) throws SQLException, IPSException
    {
    	ActionErrors errores = null;
    	
    	try{
    		errores = new ActionErrors();
    		
	    	//----------------Validacion Ingreso Cuidados Especiales---------------//
	    	int aux=Utilidades.verificarValoracionIngresoCuidadosEspeciales(con, paciente.getCodigoIngreso());
			if(aux==3)
			{
				return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El paciente es de cuidados especiales pero no tiene valoracion inicial", "error.solicitudgeneral.tieneValoracionInicialCuidadosEspeciales", true);
			}
			//--------------------------------------------------------------------//
	    	
	    	
	        SolicitudesCx mundoSolCx= new SolicitudesCx();
	        boolean esCentroCostoSolicitadoExterno=false;
	        
	        if(cxForm.getCodigoCentroCostoSolicitado()==ConstantesBD.codigoCentroCostoExternos)
	            esCentroCostoSolicitadoExterno=true;
	        else
	            esCentroCostoSolicitadoExterno=false;
	        //primero se valida que no existan ordenes con los servicios ingresados
	        /*String codigosServiciosSeparadosPorComas="";
	        for (int i=0; i<cxForm.getNumeroFilasMapaServicios(); i ++)
	        {    
	            if(!cxForm.getServiciosMap("fueEliminadoServicio_"+i).toString().equals("true"))
	            {
	                codigosServiciosSeparadosPorComas += (cxForm.getServiciosMap("codigoServicio_"+i)+",");
	            }    
	        } 
	        codigosServiciosSeparadosPorComas+="-1";
	        cxForm.setMensajeAdvertenciaVector(mundoSolCx.existeOrdenConEstadoMedicoPendienteYServicio(con, paciente.getCodigoPersona(), codigosServiciosSeparadosPorComas));
	        if(cxForm.getMensajeAdvertenciaVector().size()>0)
	        {
	            cxForm.setEstado("mostrarMensajeValidacion");
	            UtilidadBD.cerrarConexion(con);
	            return mapping.findForward("paginaPrincipal");
	        }*/
	        //fin validacion
	        
	        
	        
	//      VALIDAR PARAMETRO GENERAL REGISTRO EVOLUCIONES PARA ORDENES -*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
			String  parametroEvoluciones = Solicitud.consultarParametroEvoluciones(con); 
			boolean validacionViaIngreso=false;
			
			if(parametroEvoluciones.equals(ConstantesBD.acronimoSi))
			{
				logger.info("<<<<<<< PARAMETRO EVOLUCIONES ->>>>>>"+parametroEvoluciones+" | ESTA ACTIVADA | ");
				
				logger.info("FECHAAAAAAAA->"+UtilidadFecha.conversionFormatoFechaABD(cxForm.getFechaSolicitud())+"<-");
				
				int codigoCuenta = paciente.getCodigoCuenta();
				
				if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion)
				{
					logger.info("ES VIA INGRESO HOSPITALIZACION");
					validacionViaIngreso=true;
				}
				else
					if(paciente.getCodigoUltimaViaIngreso()==ConstantesBD.codigoViaIngresoUrgencias && 
							UtilidadValidacion.tieneValoracionConConductaCamaObservacion(con, codigoCuenta))
					{
						logger.info("ES VIA INGRESO URGENCIAS EN OBSERVACION");
						validacionViaIngreso=true;
					}
				
				// PREGUNTO SI PASA EL FILTRO DE LA VIA INGRESO
				if(validacionViaIngreso == true)
				{
					String fechaValoracionInicial = UtilidadesOrdenesMedicas.consultarFechaValoracionInicial(con, paciente.getCodigoCuenta());
					logger.info(".......... FECHA VALORACION INICIAL ----->"+fechaValoracionInicial);
					logger.info(".......... FECHA SOLICITUD--------- ----->"+UtilidadFecha.conversionFormatoFechaABD(cxForm.getFechaSolicitud()));
					if(! fechaValoracionInicial.equals(UtilidadFecha.conversionFormatoFechaABD(cxForm.getFechaSolicitud())))
					{
						// PERMITO GENERAR LA SOLICITUD SIN VALIDAR AUE EXISTAN LAS EVOLUCIONES
						boolean evoluciones = UtilidadValidacion.tieneEvolucionesParaElDia(con, paciente.getCodigoCuenta(), UtilidadFecha.conversionFormatoFechaABD(cxForm.getFechaSolicitud()));
						
						if(evoluciones == false)
							return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Registrar Evolucion al Paciente antes de Continuar", "error.solicitudgeneral.validarEvolucion", true);
					}
				}
			}
			
			//-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*--*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-
	        
	        
	        UtilidadBD.iniciarTransaccion(con);
	        int numeroSolicitud=ConstantesBD.codigoNuncaValido;
	        int codigoPeticion=ConstantesBD.codigoNuncaValido;
	        if(!cxForm.getCodigoPeticion().trim().equals("")){
	            codigoPeticion= Integer.parseInt(cxForm.getCodigoPeticion());
	        	cxForm.setCirugiaAsociadaPeticion(true);//Indica que la Cx se esta asociando con la peticion
	        }
	        boolean inserto=false;
	        
	        //tomo el servicio de consecutivo numero 1 y evaluo la cobertura para insertar la subCuenta
	        double subCuenta= obtenerSubCuentaResponsableCX(con, cxForm, paciente, user);
	        logger.info("\n\n SUBCUENTA RESPONSABLE---->"+subCuenta+"\n\n");
	        
	        //SI EL CASO DE FLUJO ES 1 ENTONCES TOCA INSERTAR LA PETICION
	        if(cxForm.getCasoFlujo()==1)
	        {
	            //PERO SE PUEDE DAR EL CASO DE QUE EL CENTRO DE COSTO ES EXTERNO ENTONCES SE GRABA LA 
	            //ORDEN SIN CREAR NI HACER RELACION A NINGUNA PETICION
	            if(!esCentroCostoSolicitadoExterno)
	            {   
	                int codigoPeticionYnumeroInserciones[]= this.insertarTodaPeticionTransaccional(con, cxForm, paciente, user);
	                int numeroInserciones= codigoPeticionYnumeroInserciones[0];
	                codigoPeticion=codigoPeticionYnumeroInserciones[1];
	                if(numeroInserciones<1)
	                {
	                    DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
	                    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar la estructura de peticion (SolicitudesCxAction)", "error.solicitudCx.transaccion", true);
	                }
	                else
	                {
	                    cxForm.setCodigoPeticion(codigoPeticion+"");
	                }
	            }   
	        }
	        else if(cxForm.getCasoFlujo()==2)
	        {
	            Peticion mundoPeticion= new Peticion();
	            if(!esCentroCostoSolicitadoExterno)
	            {
	                inserto=mundoPeticion.actualizarFechaDuracionRequiereUciPeticion(con, cxForm.getFechaEstimadaCirugia(), cxForm.getDuracionAproximadaCirugia(), cxForm.getCodigoPeticion(), cxForm.getRequiereUci());
	                if(!inserto)
	                {
	                    DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
	                    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar la info de peticion (SolicitudesCxAction)", "error.solicitudCx.transaccion", true);
	                }
	            }    
	        }
	        
	        //LUEGO SE HACE UN INSERT DE UNA SOLICITUD GENERAL
	        try
	        {
	            numeroSolicitud= this.insertarSolicitudBasicaTransaccional(con, cxForm, paciente, user);
	            cxForm.setNumeroSolicitud(numeroSolicitud+"");
	            cxForm.getListaInfoRespoCoberturaCx().get(0).getDtoSubCuenta().getSolicitudesSubcuenta().get(0).setNumeroSolicitud(numeroSolicitud+"");
	            
	            if(numeroSolicitud<=0)
	            {
	                DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
	                return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar la solicitud básica (SolicitudesCxAction)", "error.solicitudCx.transaccion", true);
	            }
	        }
	        catch(SQLException sqle)
	        {
	            DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
	            return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar la solicitud básica (SolicitudesCxAction)", "error.solicitudCx.transaccion", true);
	        }
	        
	        //SE INSERTA LA SOLICITUD DE CX GENERAL
	        inserto=mundoSolCx.insertarSolicitudCxGeneralTransaccional1(con, numeroSolicitud+"", cxForm.getCodigoPeticion(),  esCentroCostoSolicitadoExterno, ConstantesBD.continuarTransaccion, subCuenta,ConstantesIntegridadDominio.acronimoIndicativoCargoCirugia);
	        if(!inserto)
	        {
	            DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
	            return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar la solicitud general (SolicitudesCxAction)", "error.solicitudCx.transaccion", true);
	        }
	        
	        //LUEGO SE INSERTAN LOS SERVICIOS DE LA ORDEN
	        String temporalCodigoServicio="";
	        int temporalCodigoCirugia= ConstantesBD.codigoNuncaValido;
	        int temporalConsecutivo= ConstantesBD.codigoNuncaValido;
	        int temporalCodigoCirujano= ConstantesBD.codigoNuncaValido;
	        int temporalCodigoAyudante= ConstantesBD.codigoNuncaValido;
	        String temporalObservaciones="";
	        
	        // Objeto formato de justificación de servicios No POS
	        FormatoJustServNopos fjsn = new FormatoJustServNopos();
	        
	        for (int i=0; i<cxForm.getNumeroFilasMapaServicios(); i ++)
	        {    
	        	logger.info("antes de pregunta eliminado "+i);
	            if(!cxForm.getServiciosMap("fueEliminadoServicio_"+i).toString().equals("true"))
	            {
	            	logger.info("después de pregunta eliminado "+i);
	                temporalCodigoServicio= (cxForm.getServiciosMap("codigoServicio_"+i)+"");
	                temporalCodigoCirugia=ConstantesBD.codigoNuncaValido;
	                temporalConsecutivo= Integer.parseInt(cxForm.getServiciosMap("numeroServicio_"+i)+"");
	                temporalCodigoCirujano= Integer.parseInt(cxForm.getServiciosMap("codigoCirujano_"+i)+"");
	                temporalCodigoAyudante=Integer.parseInt(cxForm.getServiciosMap("codigoAyudante_"+i)+"");
	                temporalObservaciones=UtilidadTexto.deshacerCodificacionHTML(cxForm.getServiciosMap("observaciones_"+i)+"");
	                
	                // SI EL INDICADOR DE JUSTIFICAR ES PENDIENTE NO SE PERMITE SOLICITAR EL SERVICIO
	            	String justificar=(String)cxForm.getServiciosMap("justificar_"+i);
	            	
	            	//Cuando viene desde peticion viene null
	                if (justificar == null || (justificar!=null && !cxForm.getServiciosMap("justificar_"+i).toString().equals("pendiente"))){
		                mundoSolCx.insertarSolicitudCxXServicioTransaccional(
		                	con, 
		                	numeroSolicitud+"", 
		                	temporalCodigoServicio, 
		                	temporalCodigoCirugia, 
		                	temporalConsecutivo, 
		                	ConstantesBD.codigoNuncaValido, //esquema tarifario
		                	ConstantesBD.codigoNuncaValidoDouble, //grupo o uvr
		                	user.getCodigoInstitucionInt(), 
		                	/*"", -- autorizacion*/
		                	ConstantesBD.codigoNuncaValido, //finalidad
		                	temporalObservaciones, 
		                	"", //via Cx 
		                	"", //indicativo bilateral
		                	"", //indicativo via de acceso
		                	ConstantesBD.codigoNuncaValido, //codigo de la especialidad
		                	"", //liquidar servicio
		                	ConstantesBD.continuarTransaccion,
		                	cxForm.getServiciosMap("cubierto_"+i).toString(),
		                	Integer.parseInt(cxForm.getServiciosMap("contrato_convenio_"+i).toString()));
	                
	                	//Deja pendiente la justificación
	    				//UtilidadJustificacionPendienteArtServ.insertarJusNP(con, numeroSolicitud, Integer.parseInt(cxForm.getServiciosMap("codigoServicio_"+i).toString()), user.getLoginUsuario(), false);
	    			}
	                //SI EL INDICADOR DE JUSTIFICAR ES VERDADERO SE HACE EL INGRESO DE LA JUSTIFICACIÓN NO POS
	                if (!UtilidadTexto.isEmpty(cxForm.getServiciosMap("justificar_"+i)+"") && cxForm.getServiciosMap("justificar_"+i).toString().equals("true")){
	                	//cxForm.getServiciosMap().put(i+"_servicio", cxForm.getServiciosMap("codigoServicio_"+i));
	                	HashMap justificacion=(HashMap) request.getSession().getAttribute(cxForm.getServiciosMap("codigoServicio_"+i)+"MAPAJUSSERV");
	                	fjsn.ingresarJustificacion(
	                		con,
	                		user.getCodigoInstitucionInt(), 
	                		user.getLoginUsuario(), 
	                		//cxForm.getJustificacionesServicios(), 
	                		justificacion,
	                		numeroSolicitud,
	                		ConstantesBD.codigoNuncaValido,
	                		Integer.parseInt(cxForm.getServiciosMap("codigoServicio_"+i).toString()),
	                		user.getCodigoPersona());
	                	cxForm.setJustificar(cxForm.getServiciosMap("justificar_"+i).toString());
	                }
	            }
	        }
	        
	        //LUEGO SE INSERTAN LOS ARTICULOS PARAMETRIZADOS DE LA ORDEN
	        int temporalCantidad= ConstantesBD.codigoNuncaValido;
	        int temporalCodigoArticulo= ConstantesBD.codigoNuncaValido;
	        int codigoInsertadoTablaArticulosSolCx=ConstantesBD.codigoNuncaValido;
	        
	        for (int i=0; i<cxForm.getNumeroFilasMapaArticulos(); i ++)
	        {    
	            if(!cxForm.getArticulosMap("fueEliminadoArticulo_"+i).toString().equals("true"))
	            {
	                temporalCantidad=Integer.parseInt(cxForm.getArticulosMap("cantidadDespachadaArticulo_"+i)+"");
	                temporalCodigoArticulo= Integer.parseInt(cxForm.getArticulosMap("codigoArticulo_"+i)+"");
	                
	                codigoInsertadoTablaArticulosSolCx=mundoSolCx.insertarSolicitudCxXArticuloGeneralTransaccional(con, numeroSolicitud+"", temporalCantidad, ConstantesBD.continuarTransaccion);
	                if(codigoInsertadoTablaArticulosSolCx==ConstantesBD.codigoNuncaValido)
	                {
	                    DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
	                    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar los articulos (general) de la solicitud cx (SolicitudesCxAction)", "error.solicitudCx.transaccion", true);
	                }
	                
	                inserto= mundoSolCx.insertarDetalleXArticuloTransaccional(con, codigoInsertadoTablaArticulosSolCx, temporalCodigoArticulo, ConstantesBD.continuarTransaccion);
	                if(!inserto)
	                {
	                    DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
	                    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar los articulos parametrizados de la solicitud cx (SolicitudesCxAction)", "error.solicitudCx.transaccion", true);
	                }
	            }
	        }  
	        
	        //LUEGO SE INSERTAN LOS OTROS ARTICULOS (NO PARAMETRIZADOS) DE LA ORDEN 
	        String temporalDescripcionOtroArticulo="";
	        temporalCantidad= ConstantesBD.codigoNuncaValido;
	        
	        for (int i=0; i<cxForm.getNumeroFilasMapaOtrosArticulos(); i ++)
	        {    
	            if(!cxForm.getOtrosArticulosMap("fueEliminadoOtrosArticulo_"+i).toString().equals("true"))
	            {
	                temporalDescripcionOtroArticulo=(cxForm.getOtrosArticulosMap("descripcionOtrosArticulo_"+i)+"");
	                temporalCantidad= Integer.parseInt(cxForm.getOtrosArticulosMap("cantdesotrosarticulos_"+i)+"");
	                
	                codigoInsertadoTablaArticulosSolCx=mundoSolCx.insertarSolicitudCxXArticuloGeneralTransaccional(con, numeroSolicitud+"", temporalCantidad, ConstantesBD.continuarTransaccion);
	                if(codigoInsertadoTablaArticulosSolCx==ConstantesBD.codigoNuncaValido)
	                {
	                    DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
	                    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar los articulos (general) de la solicitud cx (SolicitudesCxAction)", "error.solicitudCx.transaccion", true);
	                }
	                
	                inserto= mundoSolCx.insertarDetalleXOtrosArticulosTransaccional(con, codigoInsertadoTablaArticulosSolCx, temporalDescripcionOtroArticulo, ConstantesBD.continuarTransaccion);
	                if(!inserto)
	                {
	                    DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
	                    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar los articulos NO parametrizados de la solicitud cx (SolicitudesCxAction)", "error.solicitudCx.transaccion", true);
	                }
	            }
	        }
	        
	        //LUEGO SE INSERTAN LOS OTROS PROFESIONALES 
	        int numeroProfesionales=Integer.parseInt(cxForm.getProfesionalesMap().get("numeroProfesionales")+"");
	        for(int i=0; i<numeroProfesionales;i++)
	        {
	            
	            for (int k=0; k<cxForm.getNumeroFilasMapaServicios(); k++)
	            {    
	                if(!cxForm.getServiciosMap("fueEliminadoServicio_"+k).toString().equals("true"))
	                {
	                    temporalCodigoCirujano= Integer.parseInt(cxForm.getServiciosMap("codigoCirujano_"+k)+"");
	                    temporalCodigoAyudante=Integer.parseInt(cxForm.getServiciosMap("codigoAyudante_"+k)+"");
	                    
	                    if((cxForm.getProfesionalesMap().get("profesional_"+i)+"").equals(temporalCodigoCirujano+"") || 
	                            (cxForm.getProfesionalesMap().get("profesional_"+i)+"").equals(temporalCodigoAyudante+""))
	                    {
	                        /* no insertarlos */
	                        k=cxForm.getNumeroFilasMapaServicios();
	                    }
	                    else
	                    {
	                        inserto= mundoSolCx.insertarOtrosProfesionalesSolCxTransaccional(    con, 
	                                numeroSolicitud+"", 
	                                Integer.parseInt(cxForm.getProfesionalesMap().get("profesional_"+i)+""), 
	                                user.getCodigoInstitucionInt(), 
	                                Integer.parseInt(cxForm.getProfesionalesMap().get("tipo_participante_"+i)+""), 
	                                Integer.parseInt(cxForm.getProfesionalesMap().get("especialidades_"+i)+""),
	                                ConstantesBD.continuarTransaccion);
	                        k=cxForm.getNumeroFilasMapaServicios();
	                        
	                        if(!inserto)
	                        {
	                            DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
	                            return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar los otros profesionales de la solicitud cx (SolicitudesCxAction)", "error.solicitudCx.transaccion", true);
	                        }
	                    }
	                }
	            }    
	        }
	                        
	        //si todo salio bien entonces
	        UtilidadBD.finalizarTransaccion(con);
	      
	        try {
				//VALIDACIONES PARA GENERACION DE AUTORIZACION O ASOCIACION CON LA/LAS NUEVAS SOLICITUDES
				cargarInfoVerificarGeneracionAutorizacion(con, cxForm, user, paciente, errores);
				saveErrors(request, errores);
			}catch (IPSException e) { 				
				Log4JManager.error(e);
				ActionMessages mensajeError = new ActionMessages();
				mensajeError.add("", new ActionMessage(String.valueOf(e.getErrorCode())));
				saveErrors(request, mensajeError);
			}
			
	        cxForm.setEstado("resumen");
	        return this.accionConsulta(mapping, request, cxForm, con, paciente, user, true);	
			
    	} catch(SQLException e) {
			Log4JManager.error("Error en accionGuardarCirugia: ", e);
			UtilidadBD.abortarTransaccion(con);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al guardar la cirugia ", "error.solicitudCx.transaccion", true);
		} catch (Exception e) {
			UtilidadBD.abortarTransaccion(con);
			Log4JManager.error(e.getMessage(),e);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al guardar la cirugia ", "error.solicitudCx.transaccion", true);
		}
    }    
    
    /**
     * Método que se encarga de obtenerSubCuentaResponsableCX
     * 
     * @param con
     * @param cxForm
     * @param paciente
     * @param usuario
     * @return double
     */
    @SuppressWarnings("unchecked")
	private double obtenerSubCuentaResponsableCX(Connection con, SolicitudesCxForm cxForm, PersonaBasica paciente, UsuarioBasico usuario) throws IPSException {
    	ArrayList<InfoResponsableCobertura> listaInfoResponsableCoberturaSolicitudCx = null;
    	DtoSolicitudesSubCuenta dtoSolicitudesSubCuenta = null;
    	double subCuenta=ConstantesBD.codigoNuncaValido;
    	
    	//hermorhu - MT5642
    	//Version 2.5 - Se realiza validacion de Cobertura -437- para cada uno de los servicios cx
    	
    		int codigoViaIngreso= Cuenta.obtenerCodigoViaIngresoCuenta(con, paciente.getCodigoCuenta()+"");
    		
    	listaInfoResponsableCoberturaSolicitudCx = new ArrayList<InfoResponsableCobertura>();
    	
    	int numServicios = cxForm.getNumeroFilasMapaServicios();
    	
    	for (int i=0 ; i < numServicios ; i++) {    
            if(!cxForm.getServiciosMap("fueEliminadoServicio_"+i).toString().equals("true")) {
            	InfoResponsableCobertura infoResponsableCobertura = 
            			Cobertura.validacionCoberturaServicio(con, paciente.getCodigoIngreso()+"", codigoViaIngreso, paciente.getCodigoTipoPaciente(), Integer.parseInt(cxForm.getServiciosMap("codigoServicio_"+i)+""), usuario.getCodigoInstitucionInt(), cxForm.isSolPYP(), "" /*subCuentaCoberturaOPCIONAL*/);
            
            	dtoSolicitudesSubCuenta = new DtoSolicitudesSubCuenta();
            	dtoSolicitudesSubCuenta.setNumeroSolicitud(cxForm.getNumeroSolicitud());
        		dtoSolicitudesSubCuenta.getServicio().setId(cxForm.getServiciosMap("codigoServicio_"+i)+"");
        		infoResponsableCobertura.getDtoSubCuenta().getSolicitudesSubcuenta().add(dtoSolicitudesSubCuenta);
        		
    			if(infoResponsableCobertura.getInfoCobertura().incluido() && infoResponsableCobertura.getInfoCobertura().existe()) {
    				cxForm.getServiciosMap().put("cubierto_"+i, ConstantesBD.acronimoSi);
    	  		} else {
    	  			cxForm.getServiciosMap().put("cubierto_"+i, ConstantesBD.acronimoNo);
    	  		}
    			cxForm.getServiciosMap().put("contrato_convenio_"+i, infoResponsableCobertura.getDtoSubCuenta().getContrato());
    			
    			listaInfoResponsableCoberturaSolicitudCx.add(infoResponsableCobertura);
            }
        }
    	
    	subCuenta = listaInfoResponsableCoberturaSolicitudCx.get(0).getDtoSubCuenta().getSubCuentaDouble();

    	cxForm.setListaInfoRespoCoberturaCx(listaInfoResponsableCoberturaSolicitudCx);

    	return subCuenta;
	}



	/**
     * accion ordenar cuando se va por el caso de n peticiones
     * @param cxForm
     * @param mapping
     * @param request
     * @param con
     * @return
     * @throws SQLException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private ActionForward accionOrdenar(    SolicitudesCxForm cxForm,
                                                                ActionMapping mapping,
                                                                HttpServletRequest request, 
                                                                Connection con) throws SQLException
    {
        try
        {
            cxForm.setCol(Listado.ordenarColumna(new ArrayList(cxForm.getCol()),cxForm.getUltimaPropiedad(),cxForm.getColumna()));
            cxForm.setUltimaPropiedad(cxForm.getColumna());
            UtilidadBD.cerrarConexion(con);
        }
        catch(Exception e)
        {
            logger.warn("Error en el listado de las peticiones qx");
            UtilidadBD.cerrarConexion(con);
            cxForm.resetTodo();
            ArrayList atributosError = new ArrayList();
            atributosError.add(" Listado peticiones qx");
            request.setAttribute("codigoDescripcionError", "errors.invalid");               
            request.setAttribute("atributosError", atributosError);
            return mapping.findForward("paginaError");      
        }
        return mapping.findForward("paginaListarPeticiones")  ;
    }   
    
    /**
     * Metodo que reutiliza el insertar Estructura peticion del objeto 'Peticion' y retorna un vector de 2
     * posiciones Numero de inserciónes en la BD (posición 0) y codigo de la petición (posición 1)
     * @param con
     * @param cxForm
     * @param paciente
     * @param user
     * @return
     * @throws SQLException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private int[] insertarTodaPeticionTransaccional( Connection con, SolicitudesCxForm cxForm, PersonaBasica paciente, UsuarioBasico user) throws SQLException
    {
        //PRIMERO SE ARMA EL MAPA CON EL ENCABEZADO DE LA PETICION
        HashMap peticionEncabezadoMap= new HashMap();
        Cuenta mundoCuenta= new Cuenta();
        mundoCuenta.cargarCuenta(con, paciente.getCodigoCuenta()+"");
        peticionEncabezadoMap.put("tipoPaciente",   mundoCuenta.getCodigoTipoPaciente());
        peticionEncabezadoMap.put("fechaPeticion", cxForm.getFechaSolicitud());
        peticionEncabezadoMap.put("horaPeticion", cxForm.getHoraSolicitud());
        peticionEncabezadoMap.put("duracion", cxForm.getDuracionAproximadaCirugia());
        peticionEncabezadoMap.put("solicitante", user.getCodigoPersona()+"");
        peticionEncabezadoMap.put("fechaEstimada", cxForm.getFechaEstimadaCirugia());
        peticionEncabezadoMap.put("requiereUci", cxForm.getRequiereUci());
        peticionEncabezadoMap.put("tipoAnestesia", cxForm.getTipoAnestesia());
	        //De acuerdo al cambio realizado en peticiones se hace insercion de campos (urgente-codigoIngreso-diagnostico) DCU 167 v1.50
        	DtoDiagnostico diagnostico=new DtoDiagnostico();
	        diagnostico=Utilidades.getDiagnosticoPacienteIngreso(con, paciente.getCodigoIngreso());
	        peticionEncabezadoMap.put("acronimoDiagnostico", diagnostico.getAcronimoDiagnostico());
	        peticionEncabezadoMap.put("tipoDiagnostico", diagnostico.getTipoCieDiagnostico());
	        peticionEncabezadoMap.put("urgente", cxForm.getUrgente());
        //SEGUNDO EL MAPA DE PROFESIONALES PERO ES EL MISMO ENTONCES NO SE MODIFICA
        
        //TERCERO EL MAPA DE SERVICIOS, SOLO SE LE ADICIONA EL KEY numeroFilasMapaServicios
        HashMap serviciosPeticionMap=(HashMap)cxForm.getServiciosMap().clone();
        serviciosPeticionMap.put("numeroFilasMapaServicios", cxForm.getNumeroFilasMapaServicios()+"");
        
        //CUARTO EL MAPA DE ARTICULOS - MATERIALES (PARAMETRIZADOS ESTA DIFERENTE EN LA PETICION ENTONCES TOCA)=
        //SE HACE EL DE LOS PARAMETRIZADOS
        HashMap articulosPeticionMap=(HashMap)cxForm.getArticulosMap().clone();
        articulosPeticionMap.put("numeroMateriales", cxForm.getNumeroFilasMapaArticulos()+"");
        //AHORA LOS NO PARAMETRIZADOS
        
        String temporalDescripcionOtroArticulo="";
        String temporalCantidad="";
        
        int numeroOtrosMateriales=0;
        for (int i=0; i<cxForm.getNumeroFilasMapaOtrosArticulos(); i ++)
        {    
            if(!cxForm.getOtrosArticulosMap("fueEliminadoOtrosArticulo_"+i).toString().equals("true"))
            {
                temporalDescripcionOtroArticulo=cxForm.getOtrosArticulosMap("descripcionOtrosArticulo_"+i)+"";
                temporalCantidad= cxForm.getOtrosArticulosMap("cantdesotrosarticulos_"+i)+"";
                
                articulosPeticionMap.put("descripcionOtro_"+i, temporalDescripcionOtroArticulo);
                articulosPeticionMap.put("cantidadOtro_"+i, temporalCantidad);
                numeroOtrosMateriales++;
            }
        }
        articulosPeticionMap.put("numeroOtrosMateriales", numeroOtrosMateriales+"");
        
        Peticion mundoPeticion= new Peticion();
//        int codigoPeticionYNumeroInserciones[]= mundoPeticion.insertar(con, peticionEncabezadoMap, serviciosPeticionMap, 
//        		cxForm.getProfesionalesMap(), articulosPeticionMap, paciente.getCodigoPersona(), paciente.getCodigoIngreso(), 
//        		user, true, false, contrato, cubierto);
        
        int codigoPeticionYNumeroInserciones[]= mundoPeticion.insertar(con, peticionEncabezadoMap, serviciosPeticionMap, 
        		cxForm.getProfesionalesMap(), articulosPeticionMap, paciente.getCodigoPersona(), paciente.getCodigoIngreso(), 
        		user, true, false);
        
        return codigoPeticionYNumeroInserciones;
    }
    
    /**
     * accion modificar
     * @param mapping
     * @param cxForm
     * @param con
     * @return
     * @throws SQLException
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private ActionForward accionModificar(  ActionMapping mapping,
                                                               SolicitudesCxForm cxForm,
                                                               PersonaBasica paciente,
                                                               Connection con
                                                               ) throws SQLException
    {
        //EN ESTE PUNTO TODOS LOS MAPAS VIENEN CARGADOS DE LA CONSULTA
        //PERO EL DE LOS PROFESIONALES TOCA ADECUARLO CON LAS KEY NECESARIAS
        //DESARROLLADAS EN LA PETICION UTILIZANDO W3C
        cxForm.setTiposAnestesia(Utilidades.obtenerTiposAnestesia(con, ""));
        this.llenarFechaAdmisionOApertura(con, paciente.getCodigoCuenta(), cxForm);
        if(cxForm.getProfesionalesMap().containsKey("numRegistros"))
        {    
            HashMap clonProfesionalesMap=(HashMap)cxForm.getProfesionalesMap().clone();
            cxForm.setProfesionalesMap(new HashMap());
            int numeroRegistros=((Integer)clonProfesionalesMap.get("numRegistros")).intValue();
            cxForm.getProfesionalesMap().put("numeroProfesionales", numeroRegistros+"");
            for (int i=0;i<numeroRegistros;i++)
            {
                cxForm.getProfesionalesMap().put("profesional_"+i, clonProfesionalesMap.get("codigoProfesional_"+i));
                cxForm.getProfesionalesMap().put("especialidades_"+i, clonProfesionalesMap.get("codigoEspecialidad_"+i));
                cxForm.getProfesionalesMap().put("tipo_participante_"+i, clonProfesionalesMap.get("codigoTipoParticipante_"+i));
            }
        }    
        UtilidadBD.cerrarConexion(con);
        return mapping.findForward("paginaModificar");
    }
    
    /**
     * Accion consulta
     * @param mapping
     * @param con
     * @return
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
	private ActionForward accionConsulta(  ActionMapping mapping,
                                                                HttpServletRequest request,                                 
                                                                SolicitudesCxForm cxForm,                                         
                                                                Connection con,
                                                                PersonaBasica paciente,
                                                                UsuarioBasico usuario,
                                                                boolean esResumen
                                                             ) throws SQLException
    {
        String temporalNumeroSol= cxForm.getNumeroSolicitud();
        int temporalIndicador = cxForm.getIndicador();
        SolicitudesCx mundoSolicitudesCx= new SolicitudesCx();
        
        
        boolean cargoExitosamente= false;
        //se resetea todo
        cxForm.resetTodo();
        cxForm.setNumeroSolicitud(temporalNumeroSol);
        cxForm.setIndicador(temporalIndicador);
        //se carga el encabezado
        cargoExitosamente=cargarEncabezadoSolicitud(con, cxForm);
        if(!cargoExitosamente)
        {
            return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No se cargo el encabezado de la solicitud cx", "error.solicitudCx.cargar", true);
        }
        //ahora se carga el codigo de la peticion asociada
        String temporalCodPeticion="";
        temporalCodPeticion= mundoSolicitudesCx.cargarCodPeticionDadoNumSolCx(con, cxForm.getNumeroSolicitud());
        //si es cero entonces es que la solicitud se realizo a centro costo externo
        if(temporalCodPeticion.equals("0"))
        {}
        //si es -1 entonces error
        else if(temporalCodPeticion.equals("-1"))
        {
            return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No se cargo la informacion de la solicitud cx para el numero Solicitud="+cxForm.getNumeroSolicitud(), "error.solicitudCx.cargar", true);
        }
        else
        {
            cxForm.setCodigoPeticion(temporalCodPeticion);
            this.cargarEncabezadoPeticion(con, cxForm, paciente);
        }
        
        //ahora se cargan los servicios
        cxForm.setServiciosMap(mundoSolicitudesCx.cargarServiciosXSolicitudCx(con, cxForm.getNumeroSolicitud(),false));
        
        //Se consulta por los servicios que tengan informacion de parto
        com.princetonsa.dao.sqlbase.SqlBaseSolicitudesCxDao.serviciosConInformacionParto(con, cxForm);
        
        /**DCU 174->Cambio 1.52********************************************/
		//Se cambia en la presentacion la validacion de cambio en la descripcion
        if(!Utilidades.isEmpty(cxForm.getListaServiciosImpimirOrden()))
		{
        	for(int k=0;k<Utilidades.convertirAEntero(cxForm.getServiciosMap("numRegistros").toString());k++)
			{				
				cxForm.getServiciosMap().put("descripcionServicio_"+k, cxForm.getListaServiciosImpimirOrden().get(k));
			}
		}
        /*****************************************************************/
        
        //ahora se cargan los otros profesionales
        cxForm.setProfesionalesMap(mundoSolicitudesCx.cargarProfesionalesXSolicitudesCx(con, cxForm.getNumeroSolicitud()));
        int numeroRegistros=((Integer)cxForm.getProfesionalesMap("numRegistros")).intValue();
        cxForm.getProfesionalesMap().put("numeroProfesionales", numeroRegistros+"");
        
        //ahora se cargan los articulos
        cxForm.setArticulosMap(mundoSolicitudesCx.cargarArticulosXSolicitudesCx(con, cxForm.getNumeroSolicitud()));
        
        //ahora se cargan los otros articulos
        cxForm.setOtrosArticulosMap(mundoSolicitudesCx.cargarOtrosArticulosXSolicitudesCx(con, cxForm.getNumeroSolicitud()));
        
        //ahora se carga los datos de la anulación en el caso de que la solicitud se encuentre anulada
        if(cxForm.getCodigoEstadoMedico()==ConstantesBD.codigoEstadoHCAnulada)
        {
        	cxForm.setDatosAnulacion(mundoSolicitudesCx.cargarAnulacionSolCx(con,Integer.parseInt(cxForm.getNumeroSolicitud())));
        	cxForm.setDatosAnulacion("hora_0",UtilidadFecha.convertirHoraACincoCaracteres(cxForm.getDatosAnulacion("hora_0")+""));
        }
        
        //Carga el Dto de Solicitud de Procedimientos
        cxForm.setArrayArticuloIncluidoDto(RespuestaProcedimientos.cargarArticulosIncluidosSolicitudDto(con,cxForm.getNumeroSolicitud()));		
        cxForm.setArticulosMap("abrir_seccion_art_incluidos",ConstantesBD.acronimoSi);
		
        
        UtilidadBD.cerrarConexion(con);
        if(esResumen){
        	logger.info("ES RESUMEN");
            return mapping.findForward("paginaResumen");
        }
        else
        {
        	logger.info("NO ES RESUMEN");
            this.llenarEstructuraLogs(cxForm);
            return mapping.findForward("paginaConsulta");
        }    
    }
    
    /**
     * metodo que carga el encabezadiode la solicitud reutilizando el objeto de
     * Solicitud ya existente
     * @param con
     * @param cxForm
     * @return
     */
    private boolean cargarEncabezadoSolicitud(  Connection con,
                                                                        SolicitudesCxForm cxForm)
    {
        Solicitud mundoSolicitud= new Solicitud();
        boolean cargo=false;
        try
        {
            cargo=mundoSolicitud.cargar(con, Integer.parseInt(cxForm.getNumeroSolicitud()));
            if(!cargo)
                return cargo;
            else
            {
                cxForm.setFechaSolicitud(mundoSolicitud.getFechaSolicitud());
                cxForm.setHoraSolicitud(mundoSolicitud.getHoraSolicitud());
                cxForm.setCodigoCentroCostoSolicitado(mundoSolicitud.getCentroCostoSolicitado().getCodigo());
                cxForm.setNombreCentroCostoSolicitado(mundoSolicitud.getCentroCostoSolicitado().getNombre());
                //cxForm.setAutorizacion(mundoSolicitud.getNumeroAutorizacion());
                cxForm.setCodigoEspecialidadOrdena(mundoSolicitud.getEspecialidadSolicitante().getCodigo());
                cxForm.setNombreEspecialidadOrdena(mundoSolicitud.getEspecialidadSolicitante().getNombre());
                cxForm.setCodigoMedicoSolicita(mundoSolicitud.getCodigoMedicoSolicitante());
                cxForm.setCodigoEstadoMedico(mundoSolicitud.getEstadoHistoriaClinica().getCodigo());
                cxForm.setUrgente(mundoSolicitud.getUrgente());
                cxForm.setConsecutivoOrdenMedica(mundoSolicitud.getConsecutivoOrdenesMedicas()+"");
                cxForm.setJustificacionSolicitud(mundoSolicitud.getJustificacionSolicitud());
                return cargo;
            }
        }
        catch(SQLException e)
        {
            logger.warn("No cargo el encabezado de Solicitudes Cx (null) para el numeroSolicitud= "+cxForm.getNumeroSolicitud());
            return false;
        }
    }
    
    /**
     * Metodo que llena toda la estructura de los logs antes de ser modificados
     * @param cxForm
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
	protected void llenarEstructuraLogs(SolicitudesCxForm cxForm)
    {
        logEncabezadoMap= new HashMap();
        
        //solicitud basica
        logEncabezadoMap.put("fechaSolicitud", cxForm.getFechaSolicitud());
        logEncabezadoMap.put("horaSolicitud", cxForm.getHoraSolicitud());
        logEncabezadoMap.put("codigoCentroCostoSolicitado", cxForm.getCodigoCentroCostoSolicitado()+"");
        //logEncabezadoMap.put("autorizacion"," ");
        logEncabezadoMap.put("codigoEspecialidadOrdena", cxForm.getCodigoEspecialidadOrdena()+"");
        logEncabezadoMap.put("codigoMedicoSolicita", cxForm.getCodigoMedicoSolicita()+"");
        
        //peticion
        if(cxForm.getCodigoCentroCostoSolicitado()!=ConstantesBD.codigoCentroCostoExternos)
        {    
            logEncabezadoMap.put("codigoPeticion", cxForm.getCodigoPeticion());
            logEncabezadoMap.put("fechaEstimadaCirugia", cxForm.getFechaEstimadaCirugia());
            logEncabezadoMap.put("duracionAproximada", cxForm.getDuracionAproximadaCirugia());
            logEncabezadoMap.put("requiereUci", cxForm.getRequiereUci());
        }    
        
        //servicios
        logServiciosMap=(HashMap) cxForm.getServiciosMap().clone();
        
        //profesionales
        logProfesionalesMap= (HashMap) cxForm.getProfesionalesMap().clone();
        
        //articulos
        logArticulosMap= (HashMap) cxForm.getArticulosMap().clone();
        
        //otros articulos
        logOtrosArticulosMap= (HashMap) cxForm.getOtrosArticulosMap().clone();
    }
    
    
    /**
     * Metodo que carga la informacion de la peticion
     * @param con
     * @param cxForm
     * @param paciente
     */
    @SuppressWarnings("rawtypes")
	private void cargarEncabezadoPeticion(Connection con, SolicitudesCxForm cxForm, PersonaBasica paciente)
    {
        Peticion mundoPeticion= new Peticion();
        //se carga la información del encabezado de la petion
        HashMap peticionMap= mundoPeticion.cargarDatosGeneralesPeticion2(con, paciente.getCodigoPersona(), Integer.parseInt(cxForm.getCodigoPeticion()),null);
        cxForm.setTipoAnestesia(Utilidades.convertirAEntero(peticionMap.get("tipoanestesia_0")+""));
        cxForm.setNombreTipoAnestesia(peticionMap.get("nombretipoanestesia_0")+"");
        if(!(peticionMap.get("fecha_cirugia_0")+"").trim().equals(""))
        {    
            cxForm.setFechaEstimadaCirugia(UtilidadFecha.conversionFormatoFechaAAp((peticionMap.get("fecha_cirugia_0")+"")));
        }    
        cxForm.setDuracionAproximadaCirugia(UtilidadFecha.convertirHoraACincoCaracteres((peticionMap.get("duracion_0")+"")));
        if((peticionMap.get("requiere_uci_0")+"").trim().equals("f") || (peticionMap.get("requiere_uci_0")+"").trim().equals("false"))
        {    
            cxForm.setRequiereUci("false");
        }    
        else if((peticionMap.get("requiere_uci_0")+"").trim().equals("t") || (peticionMap.get("requiere_uci_0")+"").trim().equals("true"))
            cxForm.setRequiereUci("true");
        else
            cxForm.setRequiereUci("");
        
        if((peticionMap.get("codigo_estado_0")+"").trim().equals(ConstantesBD.codigoEstadoPeticionPendiente+""))
        {
            cxForm.setPuedoModificarPeticion(true);
        }
        else
        {
            cxForm.setPuedoModificarPeticion(false);
        }
    }
    
    
    /**
     * Método que centraliza todos los inserts para las solicitudes de cx
     * @param con
     * @param request
     * @param mapping
     * @param cxForm
     * @param paciente
     * @param user
     * @return
     * @throws SQLException
     */
    private ActionForward accionGuardarModificar   (       Connection con, 
                                                                                  HttpServletRequest request,
                                                                                  ActionMapping mapping, 
                                                                                  SolicitudesCxForm cxForm,
                                                                                  PersonaBasica paciente,
                                                                                  UsuarioBasico user
                                                                              ) throws SQLException, IPSException
    {
        SolicitudesCx mundoSolCx= new SolicitudesCx();
        UtilidadBD.iniciarTransaccion(con);
        boolean insertoElimino=false;
        boolean insertaAlertaEnfermeria = false;
        Integer[] consecutivoInformacionParto = null;
       
        ActionErrors errores = new ActionErrors();
        
        //SE ACTUALIZA LA INFO DE LA SOLICITUD BASICA
        Solicitud mundoSolicitudBasica= new Solicitud();
        //mundoSolicitudBasica.actualizarNumeroAutorizacionTransaccional(con, cxForm.getAutorizacion(), Integer.parseInt(cxForm.getNumeroSolicitud()), ConstantesBD.continuarTransaccion);
        mundoSolicitudBasica.actualizarPrioridadUrgenteTransaccional(con, Integer.parseInt(cxForm.getNumeroSolicitud()), cxForm.getUrgente(), ConstantesBD.continuarTransaccion);
        
        if(!cxForm.getJustificacionSolicitudNueva().equals(""))
        {
        	Solicitud.actualizarJustificacionSolicitud(
        			con, 
        			UtilidadTexto.agregarTextoAObservacionFechaGrabacion(
        					cxForm.getJustificacionSolicitud(),
        					cxForm.getJustificacionSolicitudNueva(),
        					user,
        					false),
        			cxForm.getNumeroSolicitud());        	
        	insertaAlertaEnfermeria = true;
        }        
        
        // SE ACTUALIZA LA INFORMACION DE LA PETICION
        Peticion mundoPeticion= new Peticion();
        if(cxForm.getCodigoCentroCostoSolicitado()!=ConstantesBD.codigoCentroCostoExternos)
        {
            insertoElimino=mundoPeticion.actualizarFechaDuracionRequiereUciPeticion(con, cxForm.getFechaEstimadaCirugia(), cxForm.getDuracionAproximadaCirugia(), cxForm.getCodigoPeticion(), cxForm.getRequiereUci());
            if(!insertoElimino)
            {
                DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
                return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar la info de peticion (SolicitudesCxAction)", "error.solicitudCx.transaccion", true);
            }
        }
        
        //SE ELIMINAN LOS SERVICIOS PARA ESE NUMERO DE SOLICITUD Y SE INSERTAN DE NUEVO 
        // MOTIVO--->(MENOR TIEMPO DESARROLLO REQUERIMIENTOS 2005-11-24)
        insertoElimino=mundoSolCx.eliminarServiciosXSolicitudCx(con, cxForm.getNumeroSolicitud());
        if(!insertoElimino)
        {
        	//Si no elimino puede ser porque el servicio tiene informacion de parto asociada
        	try
        	{
        		UtilidadBD.abortarTransaccion(con);
    			UtilidadBD.cerrarConexion(con);
        		consecutivoInformacionParto = HojaQuirurgica.getConsecutivoInformacionPartoYeliminar(Utilidades.convertirAEntero(cxForm.getNumeroSolicitud()));
			}
        	catch (Exception e)
        	{

        		logger.error(e);
			}
        	finally
        	{
        		con = UtilidadBD.abrirConexion();
        		UtilidadBD.iniciarTransaccion(con);
        		insertoElimino=mundoSolCx.eliminarServiciosXSolicitudCx(con, cxForm.getNumeroSolicitud());
			}
        }
        //Si nuevamente no se puede eliminar es porque algo anda mal 
        if(!insertoElimino)
        {	
            DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
            return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el eliminar la info de los servicios (SolicitudesCxAction)", "error.solicitudCx.transaccion", true);
        }
        // SE INSERTAN 
        else
        {
            //LUEGO SE INSERTAN LOS SERVICIOS DE LA ORDEN
            String temporalCodigoServicio="";
            int temporalCodigoCirugia= ConstantesBD.codigoNuncaValido;
            int temporalConsecutivo= ConstantesBD.codigoNuncaValido;
            String temporalObservaciones= "";
            
            //hermorhu - MT5642
            //Se realiza el llamado al metodo obtenerSubCuentaResponsableCX que realiza la validacion de Cobertura 437
            obtenerSubCuentaResponsableCX(con, cxForm, paciente, user);
            
            for (int i=0; i<cxForm.getNumeroFilasMapaServicios(); i ++)
            {    
                if(!cxForm.getServiciosMap("fueEliminadoServicio_"+i).toString().equals("true"))
                {
                	Utilidades.imprimirMapa(cxForm.getServiciosMap());
                	
                    temporalCodigoServicio= (cxForm.getServiciosMap("codigoServicio_"+i)+"");
                    temporalCodigoCirugia=Integer.parseInt(cxForm.getServiciosMap("codigoTipoCirugia_"+i)+"");
                    temporalConsecutivo= Integer.parseInt(cxForm.getServiciosMap("numeroServicio_"+i)+"");
                    temporalObservaciones=UtilidadTexto.deshacerCodificacionHTML(cxForm.getServiciosMap("observaciones_"+i)+"");
                  
                    mundoSolCx.insertarSolicitudCxXServicioTransaccional(
                        	con, 
                        	cxForm.getNumeroSolicitud(), 
                        	temporalCodigoServicio, 
                        	temporalCodigoCirugia, 
                        	temporalConsecutivo, 
                        	ConstantesBD.codigoNuncaValido, //esquema tarifario
                        	ConstantesBD.codigoNuncaValidoDouble, //grupo o uvr
                        	user.getCodigoInstitucionInt(), 
                        	/*"", -- autorizacion*/
                        	ConstantesBD.codigoNuncaValido, //finalidad
                        	temporalObservaciones, 
                        	"", //via Cx 
                        	"", //indicativo bilateral
                        	"", //indicativo via de acceso
                        	ConstantesBD.codigoNuncaValido, //codigo de la especialidad
                        	"", //liquidar servicio
                        	ConstantesBD.continuarTransaccion,
                        	cxForm.getServiciosMap("cubierto_"+i).toString(),
                        	Integer.parseInt(cxForm.getServiciosMap("contrato_convenio_"+i).toString()));
                                    
                }
                /**
                 * MT 5018 Version Cambio 1.5
                 * @author leoquico
                 * @fecha 12/04/2013
                 */
                //validacion
                if (!UtilidadTexto.isEmpty(cxForm.getServiciosMap("justificar_"+i)+"") && cxForm.getServiciosMap("justificar_"+i).toString().equals("true")){
                
                	HashMap justificacion=(HashMap) request.getSession().getAttribute(cxForm.getServiciosMap("codigoServicio_"+i)+"MAPAJUSSERV");
                	// Objeto formato de justificación de servicios No POS
        	        FormatoJustServNopos fjsn = new FormatoJustServNopos();
        	        //Insertar Solicitud
        	        fjsn.ingresarJustificacion(
	                		con,
	                		user.getCodigoInstitucionInt(), 
	                		user.getLoginUsuario(), 
	                		//cxForm.getJustificacionesServicios(), 
	                		justificacion,
	                		Utilidades.convertirAEntero(cxForm.getNumeroSolicitud()),
	                		ConstantesBD.codigoNuncaValido,
	                		Integer.parseInt(cxForm.getServiciosMap("codigoServicio_"+i).toString()),
	                		user.getCodigoPersona());
        	        
                	
                }
                
                //Se asocia la informacion del parto del servicio que se elimino, al NUEVO servicio
                if(SqlBaseSolicitudesCxDao.secuenciaSolCirugiaPorServicio!=null && consecutivoInformacionParto!=null && consecutivoInformacionParto[1]!=null && consecutivoInformacionParto[1].equals(Utilidades.convertirAEntero(temporalCodigoServicio))){
                	HojaQuirurgica.migrarInformacionParto(con, SqlBaseSolicitudesCxDao.secuenciaSolCirugiaPorServicio, consecutivoInformacionParto[0]);
                	SqlBaseSolicitudesCxDao.secuenciaSolCirugiaPorServicio=null;
                }
            }  
        }
        
        
        //SE ELIMINAN LOS ARTICULOS PARA ESE NUMERO DE SOLICITUD Y SE INSERTAN DE NUEVO 
        // MOTIVO--->(MENOR TIEMPO DESARROLLO REQUERIMIENTOS 2005-11-24)
        
        insertoElimino=mundoSolCx.eliminarArticulosXSolicitudCx(con, cxForm.getNumeroSolicitud());
        if(!insertoElimino)
        {
            DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
            return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el eliminar la info de los ARTICULOS (SolicitudesCxAction)", "error.solicitudCx.transaccion", true);
        }
        else
        {    
            //LUEGO SE INSERTAN LOS ARTICULOS PARAMETRIZADOS DE LA ORDEN
            int temporalCantidad= ConstantesBD.codigoNuncaValido;
            int temporalCodigoArticulo= ConstantesBD.codigoNuncaValido;
            int codigoInsertadoTablaArticulosSolCx=ConstantesBD.codigoNuncaValido;
            
            for (int i=0; i<cxForm.getNumeroFilasMapaArticulos(); i ++)
            {    
                if(!cxForm.getArticulosMap("fueEliminadoArticulo_"+i).toString().equals("true"))
                {
                    temporalCantidad=Integer.parseInt(cxForm.getArticulosMap("cantidadDespachadaArticulo_"+i)+"");
                    temporalCodigoArticulo= Integer.parseInt(cxForm.getArticulosMap("codigoArticulo_"+i)+"");
                    
                    codigoInsertadoTablaArticulosSolCx=mundoSolCx.insertarSolicitudCxXArticuloGeneralTransaccional(con, cxForm.getNumeroSolicitud(), temporalCantidad, ConstantesBD.continuarTransaccion);
                    if(codigoInsertadoTablaArticulosSolCx==ConstantesBD.codigoNuncaValido)
                    {
                        DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
                        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar los articulos (general) de la solicitud cx (SolicitudesCxAction)", "error.solicitudCx.transaccion", true);
                    }
                    
                    insertoElimino= mundoSolCx.insertarDetalleXArticuloTransaccional(con, codigoInsertadoTablaArticulosSolCx, temporalCodigoArticulo, ConstantesBD.continuarTransaccion);
                    if(!insertoElimino)
                    {
                        DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
                        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar los articulos parametrizados de la solicitud cx (SolicitudesCxAction)", "error.solicitudCx.transaccion", true);
                    }
                }
            }  
            
            //LUEGO SE INSERTAN LOS OTROS ARTICULOS (NO PARAMETRIZADOS) DE LA ORDEN 
            String temporalDescripcionOtroArticulo="";
            temporalCantidad= ConstantesBD.codigoNuncaValido;
            
            for (int i=0; i<cxForm.getNumeroFilasMapaOtrosArticulos(); i ++)
            {    
                if(!cxForm.getOtrosArticulosMap("fueEliminadoOtrosArticulo_"+i).toString().equals("true"))
                {
                    temporalDescripcionOtroArticulo=(cxForm.getOtrosArticulosMap("descripcionOtrosArticulo_"+i)+"");
                    temporalCantidad= Integer.parseInt(cxForm.getOtrosArticulosMap("cantdesotrosarticulos_"+i)+"");
                    
                    codigoInsertadoTablaArticulosSolCx=mundoSolCx.insertarSolicitudCxXArticuloGeneralTransaccional(con, cxForm.getNumeroSolicitud(), temporalCantidad, ConstantesBD.continuarTransaccion);
                    if(codigoInsertadoTablaArticulosSolCx==ConstantesBD.codigoNuncaValido)
                    {
                        DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
                        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar los articulos (general) de la solicitud cx (SolicitudesCxAction)", "error.solicitudCx.transaccion", true);
                    }
                    
                    insertoElimino= mundoSolCx.insertarDetalleXOtrosArticulosTransaccional(con, codigoInsertadoTablaArticulosSolCx, temporalDescripcionOtroArticulo, ConstantesBD.continuarTransaccion);
                    if(!insertoElimino)
                    {
                        DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
                        return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar los articulos NO parametrizados de la solicitud cx (SolicitudesCxAction)", "error.solicitudCx.transaccion", true);
                    }
                }
            }
            
        }    
        
        
        // SE ELIMINAN LOS PROFESIONALES PARA ESE NUMERO DE SOLICITUD Y SE INSERTAN DE NUEVO 
        // MOTIVO--->(MENOR TIEMPO DESARROLLO REQUERIMIENTOS 2005-11-24)
        
        insertoElimino=mundoSolCx.eliminarOtrosProfesionalesXSolicitudCx(con, cxForm.getNumeroSolicitud());
        if(!insertoElimino)
        {
            DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
            return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el eliminar la info de los profesionales (SolicitudesCxAction)", "error.solicitudCx.transaccion", true);
        }
        else
        {    
            //LUEGO SE INSERTAN LOS OTROS PROFESIONALES 
            int numeroProfesionales=Integer.parseInt(cxForm.getProfesionalesMap().get("numeroProfesionales")+"");
            for(int i=0; i<numeroProfesionales;i++)
            {
                
                for (int k=0; k<cxForm.getNumeroFilasMapaServicios(); k++)
                {    
                    if(!cxForm.getServiciosMap("fueEliminadoServicio_"+k).toString().equals("true"))
                    {
                        String temporalCodigoCirujano= cxForm.getServiciosMap("codigoCirujano_"+k)+"";
                        String temporalCodigoAyudante=cxForm.getServiciosMap("codigoAyudante_"+k)+"";
                        
                        if((cxForm.getProfesionalesMap().get("profesional_"+i)+"").equals(temporalCodigoCirujano) || 
                                (cxForm.getProfesionalesMap().get("profesional_"+i)+"").equals(temporalCodigoAyudante))
                        {
                            /* no insertarlos */
                            k=cxForm.getNumeroFilasMapaServicios();
                        }
                        else
                        {
                            insertoElimino= mundoSolCx.insertarOtrosProfesionalesSolCxTransaccional(    con, 
                                    cxForm.getNumeroSolicitud(), 
                                    Integer.parseInt(cxForm.getProfesionalesMap().get("profesional_"+i)+""), 
                                    user.getCodigoInstitucionInt(), 
                                    Integer.parseInt(cxForm.getProfesionalesMap().get("tipo_participante_"+i)+""), 
                                    Integer.parseInt(cxForm.getProfesionalesMap().get("especialidades_"+i)+""),
                                    ConstantesBD.continuarTransaccion);
                            k=cxForm.getNumeroFilasMapaServicios();
                            
                            if(!insertoElimino)
                            {
                                DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
                                return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar los otros profesionales de la solicitud cx (SolicitudesCxAction)", "error.solicitudCx.transaccion", true);
                            }
                        }
                    }
                }    
            }
        }   
        
        //Actualiza la informacion de los articulos incluidos dentro del procedimiento
        for(int i = 0; i < cxForm.getArrayArticuloIncluidoDto().size(); i++)
        {        	
        	Servicios_ArticulosIncluidosEnOtrosProcedimientos.modificarCantidadArticulosIncluidosSolicitudProcedimientos(
        			con, 
        			cxForm.getArrayArticuloIncluidoDto().get(i).getSolicitudPpal(),
        			cxForm.getArrayArticuloIncluidoDto().get(i).getArticuloIncluido(),
        			cxForm.getArrayArticuloIncluidoDto().get(i).getCantidad(),       			
        			user.getLoginUsuario());
        }        
        
        /**
         * Se inserta el registro de alerta para registro de enfermería MT-3438
         */
        if (insertaAlertaEnfermeria || insertoElimino) {
        	if(UtilidadValidacion.esMedico(user).equals("")) {
				OrdenMedica ordenMedica = new OrdenMedica();
				ordenMedica.registrarAlertaOrdenMedica(con, 
					ConstantesBD.seccionCirugias, 
					new Long(paciente.getCodigoCuenta()), user.getLoginUsuario());
			}
		}
        
        UtilidadBD.finalizarTransaccion(con);
        
        //Se valida que alguno de los servicios halla sido agregado o eliminado
        boolean esModificacion = false;
        
        for(int i=0 ; i < cxForm.getNumeroFilasMapaServicios() ; i++){
        	if((cxForm.getServiciosMap("estaBD_"+i) == null && cxForm.getServiciosMap("fueEliminadoServicio_"+i).toString().isEmpty())
        			|| (cxForm.getServiciosMap("estaBD_"+i) != null && UtilidadTexto.getBoolean(cxForm.getServiciosMap("fueEliminadoServicio_"+i).toString()))){
        		esModificacion = true;
        		break;
        	}
        }
        
        //Validaciones para la generacion de autorizacion en la modificacion de la solicitud
        cargarInfoVerificarGeneracionAutorizacionEnModificacion(request, cxForm, user, paciente, errores,esModificacion);
        
        //NO SE PUEDE CERRAR LA CONEXION
        //UtilidadBD.cerrarConexion(con);
        this.generarLog(cxForm, user);
        cxForm.setEstado("resumen");
        return this.accionConsulta(mapping, request, cxForm, con, paciente, user, true);
    }
    
    
    /**
     * Flujo para insertar la anulacion de una solicitud cx
     * @param con
     * @param request
     * @param mapping
     * @param cxForm
     * @param user
     * @param paciente
     * @param response
     * @return
     * @throws SQLException
     * @throws IOException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private ActionForward accionGuardarAnular   (Connection con, 
                                                 HttpServletRequest request,
                                                 ActionMapping mapping, 
                                                 SolicitudesCxForm cxForm,
                                                 UsuarioBasico user, PersonaBasica paciente, HttpServletResponse response) throws SQLException, IOException
    {
    	SolicitudesCx mundoSolCx = null;
    	ActionMessages errores = new ActionMessages();
    	try{
	        mundoSolCx = new SolicitudesCx();
	        UtilidadBD.iniciarTransaccion(con);
	        boolean inserto=false;
	        
	        inserto=mundoSolCx.insertarAnulacionSolCx(con, cxForm.getNumeroSolicitud(), cxForm.getCodigoMotivoAnulacionCxXInstitucion(), user.getLoginUsuario(), cxForm.getComentariosAnulacion());
	        if(!inserto)
	        {
	            DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
	            return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar la anulacion de la solicitud cx (SolicitudesCxAction)", "error.solicitudCx.transaccionAnular", true);
	        }
	        
	        Solicitud mundoSolicitud= new Solicitud();
	        ResultadoBoolean rb=mundoSolicitud.cambiarEstadosSolicitudTransaccional(con, Integer.parseInt(cxForm.getNumeroSolicitud()), ConstantesBD.codigoEstadoFAnulada, ConstantesBD.codigoEstadoHCAnulada, ConstantesBD.continuarTransaccion );
	        if(!rb.isTrue())
	        {
	            DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
	            return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar la anulacion de la solicitud cx (SolicitudesCxAction)", "error.solicitudCx.transaccionAnular", true);
	        }
	        ManejoPacienteFacade manejoPacienteFacade = new ManejoPacienteFacade();
	        boolean existeAutorizacionPorPeticion=manejoPacienteFacade.existeAutorizacionCapitacionGeneradaPorPeticion(Integer.valueOf(cxForm.getNumeroSolicitud()));
	        if(existeAutorizacionPorPeticion){
	        	Peticion mundoPeticion= new Peticion();
	            inserto= mundoPeticion.desAsociarPeticionSolicitud(con, Integer.valueOf(cxForm.getNumeroSolicitud()));
	            if(!inserto)
	            {
	                DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
	                return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error al desasociar la petición de la solicitud (SolicitudesCxAction)", "error.solicitudCx.transaccionAnular", true);
	            }
	        }
	        else{
		        if(cxForm.getCodigoCentroCostoSolicitado()!=ConstantesBD.codigoCentroCostoExternos)
		        {    
		            Peticion mundoPeticion= new Peticion();
		            inserto= mundoPeticion.actualizarEstadoPeticion(con, ConstantesBD.codigoEstadoPeticionAnulada, cxForm.getCodigoPeticion());
		            if(!inserto)
		            {
		                DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
		                return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar la anulacion de la peticion cx (SolicitudesCxAction)", "error.solicitudCx.transaccionAnular", true);
		            }
		        }    
	        }       
	        
	        ///cambio en pyp y ordenes ambulatorias////
	        if(Utilidades.esSolicitudPYP(con,Integer.parseInt(cxForm.getNumeroSolicitud())))
	        {
	        	String codActProg=Utilidades.obtenerCodigoActividadProgramaPypPacienteDadaSolicitud(con,Integer.parseInt(cxForm.getNumeroSolicitud()));
	        	if(Integer.parseInt(Utilidades.obtenerCodigoOrdenAmbulatoriaActividad(con,cxForm.getNumeroSolicitud()))>0)
	        	{
	        		Utilidades.actualizarEstadoActividadProgramaPypPaciente(con,codActProg,ConstantesBD.codigoEstadoProgramaPYPProgramado,user.getLoginUsuario(),"");
	        		Utilidades.asignarSolicitudToActividadPYP(con,Utilidades.obtenerCodigoOrdenAmbulatoriaActividad(con,cxForm.getNumeroSolicitud()),cxForm.getNumeroSolicitud());
	        	}
	        	else
	        	{
	        		Utilidades.actualizarEstadoActividadProgramaPypPaciente(con,codActProg,ConstantesBD.codigoEstadoProgramaPYPCancelado,user.getLoginUsuario(),"SOLICITUD CANCELADA");
	        	}
	        }
	        //si la solicitud es de orden ambulatoria
	        String codigoOrden=Utilidades.obtenerCodigoOrdenAmbulatoria(con,cxForm.getNumeroSolicitud());
	        if(Integer.parseInt(codigoOrden)>0)
	    	{
	        	OrdenesAmbulatorias.actualizarEstadoOrdenAmbulatoria(con,codigoOrden,ConstantesBD.codigoEstadoOrdenAmbulatoriaPendiente);
	        	HashMap campos=new HashMap();
	        	campos.put("estadoOrden",ConstantesBD.codigoEstadoOrdenAmbulatoriaPendiente);
	        	campos.put("numeroSolicitud",ConstantesBD.codigoNuncaValido);
	        	campos.put("numeroOrden",codigoOrden);
	        	OrdenesAmbulatorias.actualizarSolicitudEnOrdenAmbulatoria(con,campos);
	    	}
	        ///fin cambio en pyp y ordenes ambulatorias////
	        
	        UtilidadBD.finalizarTransaccion(con);
	        
	        /**INICIO ANULACION SOLICITUD Y AUTORIZACION----------------------------------------------*/
			//Validaciones para la solicitud que se anula, si esta asociada a una autorizacion
	        if(!existeAutorizacionPorPeticion){
	        	cargarInfoParaAnulacionAutorizacion(cxForm,user);
	        }
			/**FIN ANULACION SOLICITUD Y AUTORIZACION-------------------------------------------------------*/
	        
	        
	        /**
	         * Se inserta el registro de alerta para registro de enfermería MT-3438
	         */
	        if (inserto && Integer.parseInt(codigoOrden) < 1 && UtilidadValidacion.esMedico(user).equals("")) {
	        	OrdenMedica ordenMedica = new OrdenMedica();
	        	ordenMedica.registrarAlertaOrdenMedica(con, 
	        			ConstantesBD.seccionCirugias, 
	        			new Long(paciente.getCodigoCuenta()), user.getLoginUsuario());
	        }   
	        
	        UtilidadBD.cerrarConexion(con);
	        if(cxForm.getTipoBusquedaBotonVolver().equals("solicitudesPendientes"))
	            return this.accionEmpezar(cxForm,mapping, con, paciente, user);
	        else
	            response.sendRedirect("../solicitudes/listarSolicitudes.do?estado="+cxForm.getTipoBusquedaBotonVolver()+"&tipoServicio=modificar&estadoAnulacion=" + inserto + "&nroOrden=" + cxForm.getNroOrdenMedica());
	        
	
	        /*UtilidadBD.finalizarTransaccion(con);
	        UtilidadBD.cerrarConexion(con);
	        return mapping.findForward("paginaAnularExitoso");*/
	        
    	}catch(IPSException ipse){
			UtilidadBD.abortarTransaccion(con);
			errores.add("ERROR Negocio", new ActionMessage(ipse.getErrorCode().toString()));
			if(!errores.isEmpty()){
				saveErrors(request, errores);
			}
			return mapping.findForward("listado");
		}
		catch(Exception e){
			UtilidadBD.abortarTransaccion(con);
			Log4JManager.error(e);
			errores.add("ERROR no Controlado", new ActionMessage("errors.notEspecific", e.getMessage()));
		}
		if(!errores.isEmpty()){
			saveErrors(request, errores);
		}
		return null;
    }
    
    
    /**
     * Flujo para insertar la anulacion de una solicitud cx
     * @param con
     * @param request
     * @param mapping
     * @param cxForm
     * @param user
     * @return
     * @throws SQLException
     */
    private ActionForward accionGuardarRespuestaSolCentroCostoExterno   (     Connection con, 
                                                                                                                    HttpServletRequest request,
                                                                                                                    ActionMapping mapping, 
                                                                                                                    SolicitudesCxForm cxForm,
                                                                                                                    UsuarioBasico user
                                                                                                                ) throws SQLException
    {
        SolicitudesCx mundoSolCx= new SolicitudesCx();
        UtilidadBD.iniciarTransaccion(con);
        boolean inserto=false;
        
        inserto=mundoSolCx.insertarRespuestaSolCxCentroCostoExterno(con, cxForm.getNumeroSolicitud(), cxForm.getResultadosRespuestaSolCx());
        if(!inserto)
        {
            DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
            return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar la respuesta de la solicitud cx (SolicitudesCxAction)", "error.solicitudCx.transaccionRespuesta", true);
        }
        
        Solicitud mundoSolicitud= new Solicitud();
        ResultadoBoolean rb=mundoSolicitud.cambiarEstadosSolicitudTransaccional(con, Integer.parseInt(cxForm.getNumeroSolicitud()), ConstantesBD.codigoEstadoFExterno, ConstantesBD.codigoEstadoHCInterpretada, ConstantesBD.continuarTransaccion );
        if(!rb.isTrue())
        {
            DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
            return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar la respuesta de la solicitud cx (SolicitudesCxAction)", "error.solicitudCx.transaccionRespuesta", true);
        }
        
        int ins=mundoSolicitud.actualizarMedicoRespondeTransaccional(con, Integer.parseInt(cxForm.getNumeroSolicitud()), user,ConstantesBD.continuarTransaccion);
        if(ins<1)
        {
            DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
            return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar la respuesta de la solicitud cx (SolicitudesCxAction)", "error.solicitudCx.transaccionRespuesta", true);
        }
        
        UtilidadBD.finalizarTransaccion(con);
        UtilidadBD.cerrarConexion(con);
        return mapping.findForward("paginaResponderExitoso");
    }
    
    /**
     * metodo para ordenar
     * @param con
     * @param form
     * @param mapping
     * @return
     */
    private ActionForward accionOrdenarMapa(Connection con, SolicitudesCxForm form, ActionMapping mapping) 
    {
        int numRegTemp=0;
        numRegTemp=Integer.parseInt(form.getSolicitudesPendientesMap("numRegistros")+"");        
        String[] indices={
                                    "numeroSolicitud_",
                                    "codigoPeticion_",
                                    "fechaCirugia_",
                                    "consecutivoOrden_",
                                    "nombreMedicoSolicitante_",
                                    "nombreEspecialidadSolicitante_",
                                    "detalle_"
                                };
        form.setSolicitudesPendientesMap(Listado.ordenarMapa(indices,
                                                            form.getPatronOrdenar(),
                                                            form.getUltimoPatron(),
                                                            form.getSolicitudesPendientesMap(),
                                                            numRegTemp));
        form.setSolicitudesPendientesMap("numRegistros",numRegTemp+"");
        form.setUltimoPatron(form.getPatronOrdenar());
        UtilidadBD.closeConnection(con);
        return mapping.findForward("paginaListadoSolicitudesPendientes");
    }
    
    /**
     * metodo que genera todo el log de modificacion de la solicitud
     * @param cxForm
     * @param usuario
     */
    private void generarLog(SolicitudesCxForm cxForm, UsuarioBasico usuario)
    {
        String log;
        log="\n           ======INFORMACION ORIGINAL SOLICITUD CX===== " +
        "\n********  INFORMACIÓN DE LA SOLICITUD BÁSICA " +
        "\n*  Número Solicitud [" +cxForm.getNumeroSolicitud()+"] " +
        "\n*  Fecha Solicitud ["+logEncabezadoMap.get("fechaSolicitud")+"] " +
        "\n*  Hora Solicitud ["+logEncabezadoMap.get("horaSolicitud")+"]" +
        "\n*  Cod Centro Costo Solicitado ["+logEncabezadoMap.get("codigoCentroCostoSolicitado")+"]" +
        //"\n*  Autorización ["+logEncabezadoMap.get("autorizacion")+"]" +
        "\n*  Código especialidad ordena ["+logEncabezadoMap.get("codigoEspecialidadOrdena")+"]" +
        "\n*  Código Médico Solicita ["+logEncabezadoMap.get("codigoMedicoSolicita")+"]";
        
        if(cxForm.getCodigoCentroCostoSolicitado()!=ConstantesBD.codigoCentroCostoExternos)
        {    
            log+="\n******** INFORMACIÓN DE LA PETICION " +
            "\n*  Código Petición ["+logEncabezadoMap.get("codigoPeticion")+"]" +
            "\n*  Fecha estimada Cx ["+logEncabezadoMap.get("fechaEstimadaCirugia")+"] " +
            "\n*  Duración Aproximada ["+logEncabezadoMap.get("duracionAproximada")+"]" +
            "\n*  Requiere Uci ["+logEncabezadoMap.get("requiereUci")+"]";
        }    
        
        log+="\n******** INFORMACIÓN DE LOS SERVICIOS ";
        for (int i=0; i<Utilidades.convertirAEntero(logServiciosMap.get("numRegistros")+""); i ++)
        {    
            log+="\n*  Código Servicio ["+logServiciosMap.get("codigoServicio_"+i)+"]" +
            "\n*  Código Tipo Cx["+logServiciosMap.get("codigoTipoCirugia_"+i)+"]" +
            "\n*  No Servicio ["+logServiciosMap.get("numeroServicio_"+i)+"]" +
            "\n*  Código Cirujano ["+logServiciosMap.get("codigoCirujano_"+i)+"]" +
            "\n*  Código Ayudante["+logServiciosMap.get("codigoAyudante_"+i)+"]" +
            "\n*  Observaciones["+UtilidadTexto.deshacerCodificacionHTML((String)logServiciosMap.get("observaciones_"+i))+"]";
        }     
        
        log+="\n******** INFORMACIÓN DE LOS ARTICULOS ";
        for (int i=0; i<Utilidades.convertirAEntero((logArticulosMap.get("numRegistros")+"")); i ++)
        {    
            log+="\n* Código Artículo ["+logArticulosMap.get("codigoArticulo_"+i)+"]" +
            "\n*  Cantidad ["+logArticulosMap.get("cantidadDespachadaArticulo_"+i)+"] ";
        }  
        
        log+="\n******** INFORMACIÓN DE LOS OTROS ARTICULOS ";
        for (int i=0; i<Utilidades.convertirAEntero((logOtrosArticulosMap.get("numRegistros")+"")); i ++)
        {    
            log+="\n*  Desc. Otro Artículo["+logOtrosArticulosMap.get("descripcionOtrosArticulo_"+i)+"]" +
            "\n*  Cantidad ["+logOtrosArticulosMap.get("cantdesotrosarticulos_"+i)+"] ";
        }
        
        log+="\n******** INFORMACIÓN DE LOS OTROS PROFESIONALES ";
        if(cxForm.getProfesionalesMap().containsKey("numRegistros"))
        {    
            for (int i=0;i<Utilidades.convertirAEntero((logProfesionalesMap.get("numRegistros")+""));i++)
            {
                log+="\n*  Código Profesional ["+logProfesionalesMap.get("codigoProfesional_"+i)+"]" +
                "\n*  Código Especialidad ["+logProfesionalesMap.get("codigoEspecialidad_"+i)+"] " +
                "\n*  Código Tipo Participante ["+logProfesionalesMap.get("codigoTipoParticipante_"+i)+"] ";
            }
        } 
        log+="\n          =====INFORMACION DESPUES DE LA MODIFICACION SOLICITUDES CX===== " +
        "\n********  INFORMACIÓN DE LA SOLICITUD BÁSICA " +
        "\n*  Número Solicitud [" +cxForm.getNumeroSolicitud()+"] " +
        "\n*  Fecha Solicitud ["+cxForm.getFechaSolicitud()+"] " +
        "\n*  Hora Solicitud ["+cxForm.getHoraSolicitud()+"]" +
        "\n*  Cod Centro Costo Solicitado ["+cxForm.getCodigoCentroCostoSolicitado()+"]" +
        //"\n*  Autorización ["+cxForm.getAutorizacion()+"]" +
        "\n*  Código especialidad ordena ["+cxForm.getCodigoEspecialidadOrdena()+"]" +
        "\n*  Código Médico Solicita ["+cxForm.getCodigoMedicoSolicita()+"]";
        
        if(cxForm.getCodigoCentroCostoSolicitado()!=ConstantesBD.codigoCentroCostoExternos)
        {    
            log+="\n******** INFORMACIÓN DE LA PETICION " +
            "\n*  Código Petición ["+cxForm.getCodigoPeticion()+"]" +
            "\n*  Fecha estimada Cx ["+cxForm.getFechaEstimadaCirugia()+"] " +
            "\n*  Duración Aproximada ["+cxForm.getDuracionAproximadaCirugia()+"]" +
            "\n*  Requiere Uci ["+cxForm.getRequiereUci()+"]";
        }    
        
        log+="\n******** INFORMACIÓN DE LOS SERVICIOS ";
        for (int i=0; i<cxForm.getNumeroFilasMapaServicios(); i ++)
        {    
            if(!cxForm.getServiciosMap("fueEliminadoServicio_"+i).toString().equals("true"))
            {
                log+="\n* Código Servicio ["+cxForm.getServiciosMap("codigoServicio_"+i)+"]" +
                "\n*  Código Tipo Cx ["+cxForm.getServiciosMap("codigoTipoCirugia_"+i)+"]" +
                "\n*  No. Servicio ["+cxForm.getServiciosMap("numeroServicio_"+i)+"]" +
                "\n*  Código Cirujano ["+cxForm.getServiciosMap("codigoCirujano_"+i)+"]" +
                "\n*  Código Ayudante ["+cxForm.getServiciosMap("codigoAyudante_"+i)+"]"+
                "\n*  Observaciones["+UtilidadTexto.deshacerCodificacionHTML((String)cxForm.getServiciosMap("observaciones_"+i))+"]";;
            }
        }   
        
        log+="\n******** INFORMACIÓN DE LOS ARTICULOS ";
        for (int i=0; i<cxForm.getNumeroFilasMapaArticulos(); i ++)
        {    
            if(!cxForm.getArticulosMap("fueEliminadoArticulo_"+i).toString().equals("true"))
            {
                log+="\n*  Código Artículo ["+cxForm.getArticulosMap("codigoArticulo_"+i)+"]" +
                "\n*  Cantidad ["+cxForm.getArticulosMap("cantidadDespachadaArticulo_"+i)+"] ";
            }
        }  
        
        log+="\n******** INFORMACIÓN DE LOS OTROS ARTICULOS ";
        for (int i=0; i<Utilidades.convertirAEntero((logOtrosArticulosMap.get("numRegistros")+"")); i ++)
        {    
            if(!cxForm.getOtrosArticulosMap("fueEliminadoOtrosArticulo_"+i).toString().equals("true"))
            {
                log+="\n*  Desc Otro Artículo ["+cxForm.getOtrosArticulosMap("descripcionOtrosArticulo_"+i)+"]" +
                "\n*  Cantidad ["+cxForm.getOtrosArticulosMap("cantdesotrosarticulos_"+i)+"] ";
            }   
        }
        
        log+="\n******** INFORMACIÓN DE LOS OTROS PROFESIONALES ";
        int numeroProfesionales=Utilidades.convertirAEntero(cxForm.getProfesionalesMap().get("numeroProfesionales")+"");
        for(int i=0; i<numeroProfesionales;i++)
        {
            for (int k=0; k<cxForm.getNumeroFilasMapaServicios(); k++)
            {    
                if(!cxForm.getServiciosMap("fueEliminadoServicio_"+k).toString().equals("true"))
                {
                    String temporalCodigoCirujano= cxForm.getServiciosMap("codigoCirujano_"+k)+"";
                    String temporalCodigoAyudante=cxForm.getServiciosMap("codigoAyudante_"+k)+"";
                    
                    if((cxForm.getProfesionalesMap().get("profesional_"+i)+"").equals(temporalCodigoCirujano) || 
                            (cxForm.getProfesionalesMap().get("profesional_"+i)+"").equals(temporalCodigoAyudante))
                    {
                        /* no insertarlos */
                        k=cxForm.getNumeroFilasMapaServicios();
                    }
                    else
                    {
                        log+="\n*  Código Profesional ["+cxForm.getProfesionalesMap("profesional_"+i)+"]" +
                        "\n*  Código Especialidad ["+cxForm.getProfesionalesMap("especialidades_"+i)+"] " +
                        "\n*  Código Tipo Participante ["+cxForm.getProfesionalesMap("tipo_participante_"+i)+"] ";
                        k=cxForm.getNumeroFilasMapaServicios();
                    }
                }
            }  
        }
        
        log+="\n INSTITUCION ["+usuario.getCodigoInstitucion()+"]";
        log+="\n========================================================\n\n\n " ;
        LogsAxioma.enviarLog(ConstantesBD.logSolicitudesCxCodigo, log, ConstantesBD.tipoRegistroLogModificacion,usuario.getInformacionGeneralPersonalSalud());
    }
      
    
	/**
	 * Método que se encarga de validar si se generar autorizacion para el servicio de la orden.
	 * DCU 174 - Solicitud de Cirugias v2.1
	 * MT 4681
	 * 
	 * @author Camilo Gomez
	 * @param con
	 * @param solicitarForm
	 * @param usuario
	 * @param errores
	 * @throws IPSException
	 */
	private void cargarInfoVerificarGeneracionAutorizacion(Connection con,SolicitudesCxForm cxForm,
			UsuarioBasico usuario, PersonaBasica paciente, ActionErrors errores)throws IPSException
	{
		DtoSubCuentas dtoSubCuenta = null;
		AutorizacionCapitacionDto autorizacionCapitacionDto = null;
		MontoCobroDto montoCobroAutorizacion				= null;
		DatosPacienteAutorizacionDto datosPacienteAutorizar = null;
		OrdenAutorizacionDto ordenAutorizacionDto 			= null;
		List<OrdenAutorizacionDto> listaOrdenesAutorizar	= null;
		List<ServicioAutorizacionOrdenDto> listaServiciosPorAutorizar = null;
		ContratoDto contratoDto = null;
		ConvenioDto convenioDto = null;
		DtoSolicitudesSubCuenta dtoSolicitudSubCuenta = null; 
		ManejoPacienteFacade manejoPacienteFacade = null;
		OrdenesFacade ordenesFacade	= null;
		List<AutorizacionCapitacionDto> listaAutorizacionCapitacion = null;
		try{
			UtilidadBD.iniciarTransaccion(con);
			listaOrdenesAutorizar 	= new ArrayList<OrdenAutorizacionDto>();
			ordenesFacade			= new OrdenesFacade();
						
			if(cxForm.getListaInfoRespoCoberturaCx() != null && !cxForm.getListaInfoRespoCoberturaCx().isEmpty()) {
				
				dtoSubCuenta =  cxForm.getListaInfoRespoCoberturaCx().get(0).getDtoSubCuenta();
				dtoSolicitudSubCuenta = cxForm.getListaInfoRespoCoberturaCx().get(0).getDtoSubCuenta().getSolicitudesSubcuenta().get(0);
				
				convenioDto = new ConvenioDto();
				convenioDto.setCodigo(dtoSubCuenta.getConvenio().getCodigo());
				convenioDto.setNombre(dtoSubCuenta.getConvenio().getNombre());
				if(dtoSubCuenta.getDatosConvenio().getManejaPreupuestoCapitacion()!=null &&
						dtoSubCuenta.getDatosConvenio().getManejaPreupuestoCapitacion().equals(ConstantesBD.acronimoSiChar+"")){
					convenioDto.setConvenioManejaPresupuesto(true);
				}else{
					convenioDto.setConvenioManejaPresupuesto(false);
				}
				contratoDto = new ContratoDto();
				contratoDto.setConvenio(convenioDto);
				contratoDto.setCodigo(dtoSubCuenta.getContrato());
				contratoDto.setNumero(dtoSubCuenta.getNumeroContrato());
				
				ordenAutorizacionDto = new OrdenAutorizacionDto();
				ordenAutorizacionDto.setCodigoOrden(Utilidades.convertirALong(dtoSolicitudSubCuenta.getNumeroSolicitud()+""));
				ordenAutorizacionDto.setOtroCodigoOrden(Utilidades.convertirALong(cxForm.getCodigoPeticion()+""));
				ordenAutorizacionDto.setConsecutivoOrden(dtoSolicitudSubCuenta.getConsecutivoSolicitud());
				ordenAutorizacionDto.setContrato(contratoDto);
				ordenAutorizacionDto.setCodigoCentroCostoEjecuta(cxForm.getCodigoCentroCostoSolicitado());
				ordenAutorizacionDto.setEsPyp(cxForm.isSolPYP());
				ordenAutorizacionDto.setCodigoViaIngreso(paciente.getCodigoUltimaViaIngreso());
				ordenAutorizacionDto.setClaseOrden(ConstantesBD.claseOrdenOrdenMedica);
				ordenAutorizacionDto.setTipoOrden(ConstantesBD.codigoTipoSolicitudCirugia);
			
				//Se consultan datos del servicio
				listaServiciosPorAutorizar = null;
				listaServiciosPorAutorizar = ordenesFacade.obtenerServiciosPorAutorizar(Utilidades.convertirAEntero(ordenAutorizacionDto.getCodigoOrden()+""),
						ordenAutorizacionDto.getClaseOrden(), ordenAutorizacionDto.getTipoOrden());
				for(ServicioAutorizacionOrdenDto cirugia: listaServiciosPorAutorizar){
					cirugia.setAutorizar(true);
				}
				
				if(cxForm.isCirugiaAsociadaPeticion()){
					ordenAutorizacionDto.setClaseOrden(ConstantesBD.claseOrdenPeticion);
				}
				
				if(dtoSolicitudSubCuenta.isUrgenteSolicitud()){
					ordenAutorizacionDto.setEsUrgente(true);
					listaServiciosPorAutorizar.get(0).setUrgente(ConstantesBD.acronimoSiChar);
				}else{
					ordenAutorizacionDto.setEsUrgente(false);
					listaServiciosPorAutorizar.get(0).setUrgente(ConstantesBD.acronimoNoChar);
				}
				
				ordenAutorizacionDto.setServiciosPorAutorizar(listaServiciosPorAutorizar);
				listaOrdenesAutorizar.add(ordenAutorizacionDto);
			}	
			boolean manejaMonto = UtilidadTexto.isEmpty(dtoSubCuenta.getMontoCobro()) || dtoSubCuenta.getMontoCobro() == 0 ? false : true;
			
			datosPacienteAutorizar	= new DatosPacienteAutorizacionDto();
			datosPacienteAutorizar.setCodigoPaciente(dtoSubCuenta.getCodigoPaciente());
			datosPacienteAutorizar.setCodigoIngresoPaciente(dtoSubCuenta.getIngreso());
			datosPacienteAutorizar.setTipoPaciente(dtoSubCuenta.getTipoPaciente());
			datosPacienteAutorizar.setClasificacionSocieconomica(dtoSubCuenta.getClasificacionSocioEconomica());
			datosPacienteAutorizar.setTipoAfiliado(dtoSubCuenta.getTipoAfiliado());
			datosPacienteAutorizar.setNaturalezaPaciente(dtoSubCuenta.getNaturalezaPaciente());
			datosPacienteAutorizar.setCodConvenioCuenta(paciente.getCodigoConvenio());
			datosPacienteAutorizar.setCuentaAbierta(true);
			datosPacienteAutorizar.setCuentaManejaMontos(manejaMonto);
			datosPacienteAutorizar.setPorcentajeMontoCuenta(dtoSubCuenta.getPorcentajeMontoCobro());
			
			montoCobroAutorizacion	= new MontoCobroDto();
			montoCobroAutorizacion.setCantidadMonto(dtoSubCuenta.getCantidadMontoGeneral());
			montoCobroAutorizacion.setCodDetalleMonto(dtoSubCuenta.getMontoCobro());
			montoCobroAutorizacion.setPorcentajeMonto(dtoSubCuenta.getPorcentajeMontoGeneral());
			montoCobroAutorizacion.setTipoDetalleMonto(dtoSubCuenta.getTipoDetalleMonto());
			montoCobroAutorizacion.setTipoMonto(dtoSubCuenta.getTipoMonto());
			montoCobroAutorizacion.setValorMonto(dtoSubCuenta.getValorMontoGeneral());
			
			autorizacionCapitacionDto = new AutorizacionCapitacionDto();
			autorizacionCapitacionDto.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
			autorizacionCapitacionDto.setLoginUsuario(usuario.getLoginUsuario());
			autorizacionCapitacionDto.setCodigoPersonaUsuario(usuario.getCodigoPersona());
			autorizacionCapitacionDto.setTipoAutorizacion(ConstantesIntegridadDominio.acronimoAutomatica);
			autorizacionCapitacionDto.setCentroAtencion(usuario.getCodigoCentroAtencion());
			autorizacionCapitacionDto.setDatosPacienteAutorizar(datosPacienteAutorizar);
			autorizacionCapitacionDto.setMontoCobroAutorizacion(montoCobroAutorizacion);
			autorizacionCapitacionDto.setOrdenesAutorizar(listaOrdenesAutorizar);
			
			//Proceso que se encarga de verificar si se generara o asociara autorizacion para la orden.
			manejoPacienteFacade = new ManejoPacienteFacade();
			listaAutorizacionCapitacion = manejoPacienteFacade.verificarGenerarAutorizacionCapitacion(autorizacionCapitacionDto);
			
			
			if(listaAutorizacionCapitacion!=null && !listaAutorizacionCapitacion.isEmpty())
			{	//Se adiciona mensaje para los servicio que no se autorizaron
				manejoPacienteFacade.obtenerMensajesError(listaAutorizacionCapitacion, errores);

			}
			UtilidadBD.finalizarTransaccion(con);
		}catch (IPSException ipsme) {
			UtilidadBD.abortarTransaccion(con);
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			UtilidadBD.abortarTransaccion(con);
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	
	/**
	 * Metodo que se encarga de enviar los datos necesarios para validar la anulacion de la
	 * autorizacion
	 * 
	 * @param cxForm
	 * @param usuario
	 * @throws IPSException
	 */
	public void cargarInfoParaAnulacionAutorizacion(SolicitudesCxForm cxForm,
			UsuarioBasico usuario)throws IPSException
	{
		AnulacionAutorizacionSolicitudDto anulacionDto	= null;
		ManejoPacienteFacade manejoPacienteFacade		= null;
		Medicos medicos	= null;
		try{
			anulacionDto= new AnulacionAutorizacionSolicitudDto();
			anulacionDto.setMotivoAnulacion(cxForm.getComentariosAnulacion());
			anulacionDto.setFechaAnulacion(UtilidadFecha.getFechaActualTipoBD());
			anulacionDto.setHoraAnulacion(UtilidadFecha.getHoraActual());
			medicos		= new Medicos(); 
			medicos.setCodigoMedico(usuario.getCodigoPersona());
			anulacionDto.setMedicoAnulacion(medicos);
			anulacionDto.setLoginUsuarioAnulacion(usuario.getLoginUsuario());
			anulacionDto.setNumeroSolicitud(Utilidades.convertirAEntero(cxForm.getNumeroSolicitud()));
			
			manejoPacienteFacade = new ManejoPacienteFacade();
			manejoPacienteFacade.validarAnulacionAutorizacionCapitaSolictud(anulacionDto,
					ConstantesBD.claseOrdenOrdenMedica,ConstantesBD.codigoTipoSolicitudCirugia,null,usuario.getCodigoInstitucionInt());
			
		}catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	
	/**
	 * @param con
	 * @param request
	 * @param cxForm
	 * @param usuario
	 * @param paciente
	 * @param errores
	 * @param esModificacion
	 * @throws IPSException
	 * @author hermorhu
	 */
	private void cargarInfoVerificarGeneracionAutorizacionEnModificacion(HttpServletRequest request, SolicitudesCxForm cxForm, UsuarioBasico usuario, PersonaBasica paciente, ActionErrors errores, boolean esModificacion) throws IPSException{
        
		OrdenesFacade ordenesFacade = new OrdenesFacade();
        ManejoPacienteFacade manejoPacienteFacade = new ManejoPacienteFacade();
        
        List<AutorizacionPorOrdenDto> listaAutorizacionesPorOrden = null;
        List<AutorizacionCapitacionDto> listaAutorizacionCapitacion = null;
        
        AutorizacionCapitacionDto autorizacionCapitacionDto = null;
        DatosPacienteAutorizacionDto datosPacienteAutorizacion = null;
        MontoCobroDto montoCobroAutorizacion = null;
        List<OrdenAutorizacionDto> listaOrdenesAutorizar = null;
        OrdenAutorizacionDto ordenAutorizacionDto = null;
        ContratoDto contratoDto = null;
		ConvenioDto convenioDto = null;
        List<ServicioAutorizacionOrdenDto> listaServiciosPorAutorizar = null;
        
		try{

			listaOrdenesAutorizar = new ArrayList<OrdenAutorizacionDto>();
			
			contratoDto = new ContratoDto();
			convenioDto = new ConvenioDto();
			ContratosDelegate contratosDelegate = new ContratosDelegate();
			Contratos contrato = contratosDelegate.findById(paciente.getCodigoContrato());
			contratoDto.setCodigo(contrato.getCodigo());
			contratoDto.setNumero(contrato.getNumeroContrato());
			convenioDto.setCodigo(contrato.getConvenios().getCodigo());
			convenioDto.setNombre(contrato.getConvenios().getNombre());
			if(contrato.getConvenios().getManejaPresupCapitacion() != null){
			    convenioDto.setConvenioManejaPresupuesto(contrato.getConvenios().getManejaPresupCapitacion().equals(ConstantesBD.acronimoSiChar) ? true : false);
			}
			contratoDto.setConvenio(convenioDto);

			ordenAutorizacionDto = new OrdenAutorizacionDto();
			ordenAutorizacionDto.setCodigoOrden(Utilidades.convertirALong(cxForm.getNumeroSolicitud().toString()));
			ordenAutorizacionDto.setOtroCodigoOrden(Utilidades.convertirALong(cxForm.getCodigoPeticion().toString()));
			ordenAutorizacionDto.setContrato(contratoDto);
			ordenAutorizacionDto.setCodigoCentroCostoEjecuta(cxForm.getCodigoCentroCostoSolicitado());
			ordenAutorizacionDto.setEsPyp(cxForm.isSolPYP());
			ordenAutorizacionDto.setCodigoViaIngreso(paciente.getCodigoUltimaViaIngreso());
			ordenAutorizacionDto.setClaseOrden(ConstantesBD.claseOrdenOrdenMedica);
			ordenAutorizacionDto.setTipoOrden(ConstantesBD.codigoTipoSolicitudCirugia);
			
			listaServiciosPorAutorizar = ordenesFacade.obtenerServiciosPorAutorizar(Utilidades.convertirAEntero(ordenAutorizacionDto.getCodigoOrden()+""),
					ordenAutorizacionDto.getClaseOrden(), ordenAutorizacionDto.getTipoOrden());
			for(ServicioAutorizacionOrdenDto cirugia: listaServiciosPorAutorizar){
				cirugia.setAutorizar(true);
			}
		
			if(cxForm.isCirugiaAsociadaPeticion()){
				ordenAutorizacionDto.setClaseOrden(ConstantesBD.claseOrdenPeticion);
			}
			
			if(cxForm.getUrgente()) {
				ordenAutorizacionDto.setEsUrgente(true);
				listaServiciosPorAutorizar.get(0).setUrgente(ConstantesBD.acronimoSiChar);
			}else {
				ordenAutorizacionDto.setEsUrgente(false);
				listaServiciosPorAutorizar.get(0).setUrgente(ConstantesBD.acronimoNoChar);
			}
			
			ordenAutorizacionDto.setServiciosPorAutorizar(listaServiciosPorAutorizar);
			listaOrdenesAutorizar.add(ordenAutorizacionDto);
			
			InfoSubCuentaDto dtoDatosSubCuenta = manejoPacienteFacade.consultarInfoSubCuentaPorIngresoPorConvenio(paciente.getCodigoIngreso(), paciente.getCodigoConvenio());
			datosPacienteAutorizacion = new DatosPacienteAutorizacionDto();
			datosPacienteAutorizacion.setCodigoPaciente(paciente.getCodigoPersona());
			datosPacienteAutorizacion.setCodigoIngresoPaciente(paciente.getCodigoIngreso());
			datosPacienteAutorizacion.setTipoPaciente(paciente.getCodigoTipoPaciente());
			datosPacienteAutorizacion.setClasificacionSocieconomica(dtoDatosSubCuenta.getCodigoEstratoSocial());
			datosPacienteAutorizacion.setTipoAfiliado(String.valueOf(dtoDatosSubCuenta.getCodigoTipoAfiliado()));
			datosPacienteAutorizacion.setNaturalezaPaciente(dtoDatosSubCuenta.getCodigoNaturaleza());
			datosPacienteAutorizacion.setCodConvenioCuenta(paciente.getCodigoConvenio());
			datosPacienteAutorizacion.setCuentaAbierta(true);
			datosPacienteAutorizacion.setCuentaManejaMontos(dtoDatosSubCuenta.isSubCuentaManejaMontos());
			datosPacienteAutorizacion.setPorcentajeMontoCuenta(dtoDatosSubCuenta.getSubCuentaPorcentajeMonto());
			
			montoCobroAutorizacion	= new MontoCobroDto();
			montoCobroAutorizacion.setCantidadMonto(dtoDatosSubCuenta.getMontoCobro().getCantidadMonto());
			montoCobroAutorizacion.setCodDetalleMonto(dtoDatosSubCuenta.getMontoCobro().getCodDetalleMonto());
			montoCobroAutorizacion.setPorcentajeMonto(dtoDatosSubCuenta.getMontoCobro().getPorcentajeMonto());
			montoCobroAutorizacion.setTipoDetalleMonto(dtoDatosSubCuenta.getMontoCobro().getTipoDetalleMonto());
			montoCobroAutorizacion.setTipoMonto(dtoDatosSubCuenta.getMontoCobro().getTipoMonto());
			montoCobroAutorizacion.setValorMonto(dtoDatosSubCuenta.getMontoCobro().getValorMonto());
			
			autorizacionCapitacionDto = new AutorizacionCapitacionDto();
			autorizacionCapitacionDto.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
			autorizacionCapitacionDto.setLoginUsuario(usuario.getLoginUsuario());
			autorizacionCapitacionDto.setCodigoPersonaUsuario(usuario.getCodigoPersona());
			autorizacionCapitacionDto.setTipoAutorizacion(ConstantesIntegridadDominio.acronimoAutomatica);
			autorizacionCapitacionDto.setCentroAtencion(usuario.getCodigoCentroAtencion());
			autorizacionCapitacionDto.setDatosPacienteAutorizar(datosPacienteAutorizacion);
			autorizacionCapitacionDto.setMontoCobroAutorizacion(montoCobroAutorizacion);
			autorizacionCapitacionDto.setOrdenesAutorizar(listaOrdenesAutorizar);

			//valida que existan ordenes con Autorizaciones de Capitacion Subcontratada en estado Autorizada	
			List<String> estados = new ArrayList<String>();
			estados.add(ConstantesIntegridadDominio.acronimoAutorizado);
			listaAutorizacionesPorOrden = ordenesFacade.obtenerAutorizacionCapitacion(ConstantesBD.claseOrdenOrdenMedica, ConstantesBD.codigoTipoSolicitudCirugia , Long.parseLong(cxForm.getNumeroSolicitud()), estados);
			
			//Si no tiene asociada Autorizacion de Capitacion Subcontratada 
			if(listaAutorizacionesPorOrden == null || listaAutorizacionesPorOrden.isEmpty()){
				
				listaAutorizacionCapitacion = manejoPacienteFacade.verificarGenerarAutorizacionCapitacion(autorizacionCapitacionDto);
				
				if(listaAutorizacionCapitacion!=null && !listaAutorizacionCapitacion.isEmpty()){	
					//Se adiciona mensaje para los servicio que no se autorizaron
					manejoPacienteFacade.obtenerMensajesError(listaAutorizacionCapitacion, errores);
				}
			}
			//Si tiene asociada Autorizacion de Capitacion Subcontratada
			else {
				//Valida si se elimino o agrego algun servicio (si se hizo modificacion)
				if(esModificacion){
					
					AnulacionAutorizacionSolicitudDto anulacionAutorizacionDto= new AnulacionAutorizacionSolicitudDto();
					anulacionAutorizacionDto.setMotivoAnulacion(MOTIVO_ANULACION);
					anulacionAutorizacionDto.setFechaAnulacion(UtilidadFecha.getFechaActualTipoBD());
					anulacionAutorizacionDto.setHoraAnulacion(UtilidadFecha.getHoraActual());
					anulacionAutorizacionDto.setLoginUsuarioAnulacion(usuario.getLoginUsuario());
					
					boolean anulacionExitosa = false;
					anulacionExitosa = ordenesFacade.procesoAnulacionAutorizacion(ConstantesBD.claseOrdenOrdenMedica, ConstantesBD.codigoTipoSolicitudCirugia, listaAutorizacionesPorOrden, anulacionAutorizacionDto, usuario.getCodigoInstitucionInt());
					
					if(anulacionExitosa){
						//validacion de igual convenio/contrato para autorizar solicitud cx
						listaAutorizacionCapitacion = manejoPacienteFacade.verificarGenerarAutorizacionCapitacion(autorizacionCapitacionDto);
						
						if(listaAutorizacionCapitacion!=null && !listaAutorizacionCapitacion.isEmpty()){	
							//Se adiciona mensaje para los servicio que no se autorizaron
							manejoPacienteFacade.obtenerMensajesError(listaAutorizacionCapitacion, errores);
						}
						
					}else {
						errores.add("", new ActionMessage("errors.autorizacion.noSeAnulaOrden"));
					}
				}
			}
			
			if(!errores.isEmpty()){
				saveErrors(request, errores);
			}
			
		}catch (IPSException ipse) {
			Log4JManager.error(ipse.getMessage(), ipse);
			throw ipse;
		}
		catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
}