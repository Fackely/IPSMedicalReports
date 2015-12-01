/*
 * @(#)AdminMedicamentosAction.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.action.medicamentos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

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
import util.Listado;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.inventarios.UtilidadInventarios;
import util.reportes.ConsultasBirt;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.medicamentos.AdminMedicamentosForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.manejoPaciente.DtoCentroCostoViaIngreso;
import com.princetonsa.mundo.Articulo;
import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.historiaClinica.Epicrisis1;
import com.princetonsa.mundo.medicamentos.AdminMedicamentos;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.pdf.AdministracionMedicamentosPdf;
import com.princetonsa.util.birt.reports.DesignEngineApi;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.ICentroCostoViaIngresoMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IViasIngresoMundo;

/**
 *   Action, controla todas las opciones dentro de la admin de medicamentos 
 * 	 incluyendo los posibles casos de error y los casos de flujo.
 * @version 1.0, Sept 16, 2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 * @author <a href="mailto:joan@PrincetonSA.com">Joan Arlen L&oacute;pez Correa.</a>
 */
public class AdminMedicamentosAction extends Action   
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(AdminMedicamentosAction.class);

	public ActionForward execute(	ActionMapping mapping,
													ActionForm form,
													HttpServletRequest request,
													HttpServletResponse response ) throws Exception
													{
		Connection con=null;
		try {
			if (response==null); //Para evitar que salga el warning
			if(form instanceof AdminMedicamentosForm)
			{
				try
				{
					con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConnection();	
				}
				catch(SQLException e)
				{
					logger.warn("No se pudo abrir la conexión"+e.toString());
				}

				UsuarioBasico usuario= (UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
				PersonaBasica paciente = (PersonaBasica)request.getSession().getAttribute("pacienteActivo");
				AdminMedicamentosForm adminForm =(AdminMedicamentosForm)form;
				ActionForward validacionesGenerales = this.validacionesAccesoUsuario(mapping, request);

				if (validacionesGenerales != null)
				{
					UtilidadBD.closeConnection(con);
					return validacionesGenerales ;
				}

				String estado=adminForm.getEstado();
				logger.warn("\n [AdminoMedicamentosAction] estado -->"+estado+"\n");

				if(estado == null)
				{
					adminForm.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(), true);	
					UtilidadBD.cerrarConexion(con);
					logger.warn("Estado no valido dentro del flujo de Admin. Medicamentos (null) ");
					request.setAttribute("codigoDescripcionError", "errors.estadoInvalido");
					return mapping.findForward("paginaError");
				}
				else if (estado.equals("seleccionMenu"))
				{
					return this.accionSeleccionMenu(adminForm, mapping, con, estado, usuario, request);
				}
				else if (estado.equals("empezarListadoPaciente"))
				{
					return this.accionEmpezarListadoPaciente(adminForm, mapping,request, con, estado, paciente, usuario);
				}
				else if (estado.equals("filtroArea"))
				{
					return this.accionFiltroArea(adminForm, mapping, con, estado, usuario);
				}
				else if (estado.equals("filtroXViaingreso"))
				{
					return this.accionFiltroViaIngreso(adminForm, mapping, con);
				}
				else if (estado.equals("empezarListadoArea"))
				{
					return this.accionEmpezarListadoArea(adminForm, mapping, con, estado, usuario.getCodigoInstitucionInt(), paciente, usuario);
				}
				else if(estado.equals("ordenarSolicitudes"))
				{
					return this.accionOrdenarSolicitudes(adminForm, mapping, request, con, usuario);
				}
				else if(estado.equals("detalleSolicitud"))
				{
					return this.accionDetalleSolicitudes(adminForm, mapping, request,usuario, con,false);   
				}
				else if(estado.equals("detalleSolicitudInfoAdic"))
				{
					return this.accionDetalleSolicitudesInfoAdic(adminForm, mapping, request,usuario, con,false);
				}
				else if(estado.equals("salirGuardar"))
				{
					return this.accionSalirGuardar(adminForm,mapping,con,request,usuario,paciente);
				}
				else if(estado.equals("resumen"))
				{
					return this.accionResumenAdmin(adminForm,mapping,con);
				}
				else if(estado.equals("imprimirPerfilFarmacoterapia"))
				{
					return this.accionImprimirPerfilFarmacoterapia(adminForm,mapping,request,usuario,paciente,con);
				}
				/*JEILONES - Se agrega la accion para retornar a la pagina anterior listado de solicitudes */
				else if (estado.equals("volverAdminMedicamentos")) {
					return this.accionVolverAdminMedicamentos(adminForm, mapping, con);
				}
				
				else if(estado.equals("imprimir"))
				{
					String nombreArchivo;
					Random r=new Random();
					nombreArchivo="/aBorrar" + r.nextInt()  +".pdf";
					AdministracionMedicamentosPdf.pdfAdminMedicamentos(ValoresPorDefecto.getFilePath() + nombreArchivo,adminForm,usuario,paciente,con, request);
					UtilidadBD.cerrarConexion(con);
					request.setAttribute("nombreArchivo", nombreArchivo);
					request.setAttribute("nombreVentana", "Administracion Medicamentos");
					return mapping.findForward("abrirPdf");
				}
				/****************************************************************************************************************
				 * ADICIONADO PARA EL MANEJO DE LA FUNCIONALIDAD DUMMY
				 ****************************************************************************************************************/
				else
					if (estado.equals("empezarDummy"))
					{

						adminForm.setBotonVolverEstadoListado(estado);
						return this.accionDetalleSolicitudes(adminForm, mapping, request,usuario, con,true);   


					}


				/****************************************************************************************************************
				 * FIN ADICIONADO PARA EL MANEJO DE LA FUNCIONALIDAD DUMMY
				 ****************************************************************************************************************/
					else
					{
						adminForm.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(), true);	
						UtilidadBD.cerrarConexion(con);
						logger.warn("Estado no valido dentro del flujo de Admin Medicamentos (null) ");
						request.setAttribute("codigoDescripcionError", "errors.formaTipoInvalido");
						return mapping.findForward("paginaError");
					}
			}	
			return null;	
		} catch (Exception e) {
			Log4JManager.error(e);
			e.printStackTrace();
			return null;
		}
		finally{
			UtilidadBD.closeConnection(con);
		}
	}			
	
	
	 /**
	 * Este método especifica las acciones a realizar en el estado
	 * resumenAdmin AdminMedicamentos
	 * @param adminForm AdminMedicamentosForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
     * @param usuario
     * @param request
	 * @return ActionForward  a la página "seleccionSolicitudMedicamentos.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionSeleccionMenu	(	AdminMedicamentosForm adminForm,
													ActionMapping mapping,
													Connection con,
													String estado, UsuarioBasico usuario, HttpServletRequest request) throws SQLException 
	{
		/**
		 * Se verifica que sea profesional de la salud
		 */
		if (!UtilidadValidacion.esProfesionalSalud(usuario))
		{
		    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Usuario NO personal de la salud trató de ingresar evolución. Como usuario que no es profesional de la salud usted no esta autorizado para llenar una evolución", "errors.usuario.noAutorizado", true) ;
		}
		adminForm.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(),true);
		adminForm.setEstado(estado);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("menuSeleccion")	;	
	}
	
    /**
	 * Este método especifica las acciones a realizar en el estado
	 * empezarListadoPaciente AdminMedicamentos
	 * @param adminForm AdminMedicamentosForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward  a la página "listadoSolicitudesMedicamentos.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEmpezarListadoPaciente	(	AdminMedicamentosForm adminForm,
															ActionMapping mapping,
															HttpServletRequest request,
															Connection con,
															String estado,
															PersonaBasica paciente,
															UsuarioBasico usuario) throws SQLException 
	{
		//se realiza el reset de los datos
		adminForm.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(),true);
		adminForm.setEstado(estado);
		
		if( paciente == null || paciente.getTipoIdentificacionPersona().equals("") )
		{
			logger.warn("El paciente no es válido (null)");
			request.setAttribute("codigoDescripcionError", "errors.paciente.noCargado");
			UtilidadBD.closeConnection(con);
			return mapping.findForward("paginaError");
		}
		 
		if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getCodigo().trim().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.ingresoEstadoDiferente", "errors.ingresoEstadoDiferenteAbierto", true);
		}
		if(UtilidadValidacion.esCuentaValida(con, paciente.getCodigoCuenta())<1)
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.cuentaNoValida", "errors.paciente.cuentaNoValida", true);
		}
		
		if ( paciente.getCodigoCuenta() == 0 )
		{
		    UtilidadBD.closeConnection(con);
		    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Paciente sin cuenta Abierta", "errors.paciente.cuentaNoAbierta", true);
		}
		else if( paciente != null || ! paciente.getTipoIdentificacionPersona().equals("") )
		{	
			AdminMedicamentos mundo= new AdminMedicamentos();
			if(adminForm.getCol().isEmpty())
			{
				adminForm.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(), true);
				logger.info("entra!!!!!!!");
				adminForm.setCol(mundo.listadoSolMedXPaciente(con,paciente.getCodigoPersona(),usuario.getCodigoInstitucionInt()));
			}
			else
			{
				adminForm.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(), false);
			}
			adminForm.setEstado(estado);
			adminForm.setBotonVolverEstadoListado(estado);
			
			UtilidadBD.closeConnection(con);
			return mapping.findForward("listarSolicitudes")	;
		}	
		return null;
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * filtroArea AdminMedicamentos
	 * @param adminForm AdminMedicamentosForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward  a la página "filtroArea.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionFiltroArea			(	AdminMedicamentosForm adminForm,
														ActionMapping mapping,
														Connection con,
														String estado,
														UsuarioBasico usuario) throws SQLException 
	{
		adminForm.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(), true);
		adminForm.setEstado(estado);
		
		IViasIngresoMundo viasIngresoMundo = ManejoPacienteFabricaMundo.crearViasIngresoMundo();
		adminForm.setListadoViasIngreso(viasIngresoMundo.buscarViasIngreso());
		
		UtilidadBD.closeConnection(con);
		return mapping.findForward("filtroArea");
	}
	
	/**
	 * Este método especifica las acciones a realizar en el estado
	 * empezarListadoArea AdminMedicamentos
	 * @param adminForm AdminMedicamentosForm
	 * @param mapping Mapping para manejar la navegación
	 * @param con Conexión con la fuente de datos
	 * @return ActionForward  a la página "listadoSolicitudesMedicamentos.jsp"
	 * @throws SQLException
	 */
	private ActionForward accionEmpezarListadoArea		(	AdminMedicamentosForm adminForm,
															ActionMapping mapping,
															Connection con,
															String estado,
															int codigoIntitucion,
															PersonaBasica paciente, UsuarioBasico usuario) throws SQLException 
	{
		//int codigoCC=adminForm.getCodigoCentroCosto();
		String fechaInicial=adminForm.getFechaInicialFiltro();
		String fechaFinal=adminForm.getFechaFinalFiltro();
		int areaFiltro= adminForm.getAreaFiltro();
		int camaFiltro=adminForm.getCamaFiltro();
		int habitacionFiltro=adminForm.getHabitacionFiltro();
		int pisoFiltro= adminForm.getPisoFiltro();
		int viaIngresoFiltro=adminForm.getViaIngresoFiltro();
		
		//adminForm.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion());
		//adminForm.setCodigoCentroCosto(codigoCC);
		AdminMedicamentos mundo= new AdminMedicamentos();
		if(adminForm.getCol().isEmpty())
		{	
			adminForm.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(),true);
			logger.info("ENTRA!!!!!!!!!!!!!");
			
			adminForm.setCol(mundo.listadoSolMedXArea(con,areaFiltro, codigoIntitucion, pisoFiltro, habitacionFiltro, camaFiltro, fechaInicial, fechaFinal,viaIngresoFiltro));
		}
		else
		{
			adminForm.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(),false);
		}
		adminForm.setFechaInicialFiltro(fechaInicial);
		adminForm.setFechaFinalFiltro(fechaFinal);
		adminForm.setAreaFiltro(areaFiltro);
		adminForm.setCamaFiltro(camaFiltro);
		adminForm.setHabitacionFiltro(habitacionFiltro);
		adminForm.setPisoFiltro(pisoFiltro);
		adminForm.setEstado(estado);
		adminForm.setBotonVolverEstadoListado(estado);
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listarSolicitudes")	;
	}
	
	/**
	 * Método de ordenamiento del pager, según la columna que sea seleccionada. 
	 * @param adminForm
	 * @param mapping
	 * @param request
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	private ActionForward accionOrdenarSolicitudes(	AdminMedicamentosForm adminForm,
													ActionMapping mapping,
													HttpServletRequest request, 
													Connection con,
													UsuarioBasico usuario) throws SQLException
	{
		try
		{
			adminForm.setCol(Listado.ordenarColumna(new ArrayList(adminForm.getCol()),adminForm.getUltimaPropiedad(),adminForm.getColumna()));
			adminForm.setUltimaPropiedad(adminForm.getColumna());
			UtilidadBD.closeConnection(con);
		}
		catch(Exception e)
		{
			logger.warn("Error en el listado de solicitudes medicamentos ");
			UtilidadBD.closeConnection(con);
			adminForm.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(), true);
			ArrayList atributosError = new ArrayList();
			atributosError.add(" Listado SolicitudesMedicamentos");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");		
		}
		UtilidadBD.closeConnection(con);
		return mapping.findForward("listarSolicitudes")	;
	}
	
	
	
	
	/**Este metodo se encarga de permitir el ingreso a la
     * pagina de detalle administracion y cargar los respectivos datos
     * de medicamentos e insumos.
     * @param adminForm
     * @param mapping
     * @param request
     * @param con Connection conexion con la fuente de datos.
     * @return ActionForward
     * @throws SQLException
     */
    private ActionForward accionDetalleSolicitudesInfoAdic(	AdminMedicamentosForm adminForm, 
            										ActionMapping mapping, 
            										HttpServletRequest request, 
            										UsuarioBasico usuario,
            										Connection con,boolean isDummy) throws SQLException
    {	

        //SE CARGA LA INFORMACION DE admin solicitud
	    adminForm.setInfoAdminSolicitud(AdminMedicamentos.cargarResumenAdministracion(con,adminForm.getNumeroSolicitud()));
	    UtilidadBD.closeConnection(con);
    	return mapping.findForward("detalleSolicitudInfoAdic")	;
    }
	
	
	/**Este metodo se encarga de permitir el ingreso a la
     * pagina de detalle administracion y cargar los respectivos datos
     * de medicamentos e insumos.
     * @param adminForm
     * @param mapping
     * @param request
     * @param con Connection conexion con la fuente de datos.
     * @return ActionForward
     * @throws SQLException
     */
    private ActionForward accionDetalleSolicitudes(	AdminMedicamentosForm adminForm, 
            										ActionMapping mapping, 
            										HttpServletRequest request, 
            										UsuarioBasico usuario,
            										Connection con,boolean isDummy) throws SQLException
    {	
        AdminMedicamentos mundo= new AdminMedicamentos();
        //SE CARGA LA INFORMACION DEL ENCABEZADO
        boolean validarCargar= mundo.encabezadoSolicitudMedicamentos(con, adminForm.getNumeroSolicitud());
        if(validarCargar)
	    {
        	llenarForm(adminForm,mundo);
	    	//SE CARGA LA INFORMACION DE LOS MEDICAMENTOS
	    	adminForm.setMedicamentosMap(mundo.listadoMedicamentos(con, adminForm.getNumeroSolicitud(), usuario.getCodigoInstitucionInt(), false));
	    	
	    	logger.info("\n\n\nGIOOOOOOOOOOOOO");
	    	Utilidades.imprimirMapa(adminForm.getMedicamentosMap());
	    	
	    	//CARGAMOS EL NUMERO DE ADMINISTRACIONES TOTALES QUE EXISTE X ARTICULO
	    	adminForm.setMedicamentosMap(cargarNumeroAdministracionesTotalesXArticulo(adminForm.getMedicamentosMap(), adminForm.getNumeroSolicitud()));
	    	
	    	HashMap<String, Object> infoLoteMedMap = new HashMap<String,Object>();
	    	
	    	for(int i=0 ; i<Integer.parseInt(adminForm.getMedicamentosMap("numRegistros").toString()) ; i++){
	    		HashMap<String,Object> mapa= UtilidadInventarios.obtenerLotesDespachoNoAdministradosTotalmente(String.valueOf(adminForm.getNumeroSolicitud()), adminForm.getMedicamentosMap("codigo_"+i).toString());
	    		infoLoteMedMap.put("medicamento_"+i, mapa);
	    	}
	    	 
	    	adminForm.setInfoLoteMedicamentosMap(infoLoteMedMap);
	    	
	    	
	    	adminForm.setInsumosMap(mundo.listadoInsumos(con,adminForm.getNumeroSolicitud(), false));
	    	
	    	HashMap<String, Object> infoLoteInsMap = new HashMap<String,Object>();
	    	
	    	for(int i=0 ; i<Integer.parseInt(adminForm.getInsumosMap("numRegistros").toString()) ; i++){
	    		HashMap<String,Object> mapa= UtilidadInventarios.obtenerLotesDespachoNoAdministradosTotalmente(String.valueOf(adminForm.getNumeroSolicitud()), adminForm.getInsumosMap("codigo_"+i).toString());
	    		infoLoteInsMap.put("insumo_"+i, mapa);
	    	}
	    	
	    	adminForm.setInfoLoteInsumosMap(infoLoteInsMap);
	    	
	        PersonaBasica persona= (PersonaBasica) request.getSession().getAttribute("pacienteActivo");
	        persona.setCodigoPersona(adminForm.getCodigoPaciente());
	        persona.cargar(con,adminForm.getCodigoPaciente());
	        persona.cargarPaciente(con, adminForm.getCodigoPaciente(), usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
	        
	        
	        ActionErrors errores = new ActionErrors();
			if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, persona.getCodigoIngreso()).getCodigo().trim().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
			{
				errores.add("errors.ingresoEstadoDiferenteAbierto", new ActionMessage("errors.ingresoEstadoDiferenteAbierto"));
			}
			if(UtilidadValidacion.esCuentaValida(con, persona.getCodigoCuenta())<1)
			{
				errores.add("errors.paciente.cuentaNoValida", new ActionMessage("errors.paciente.cuentaNoValida"));
			}
			//adminForm.setBotonVolverEstadoListado("empezarDummy");
			UtilidadBD.cerrarConexion(con);
			if(errores.isEmpty())
				 return mapping.findForward("detalleSolicitud")	;
			else
				if (!isDummy)
				{
					saveErrors(request, errores);
					return mapping.findForward("listarSolicitudes");		
				}
				else
				{
					saveErrors(request, errores);
					return mapping.findForward("paginaError");
				}
	        
	    }
	    else
	    {
	        logger.warn("Número de solicitud inválido "+adminForm.getNumeroSolicitud());
			UtilidadBD.closeConnection(con);
			adminForm.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(), true);
			ArrayList atributosError = new ArrayList();
			atributosError.add("Número de solicitud");
			request.setAttribute("codigoDescripcionError", "errors.invalid");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");
	    }
    }
	
    /**
     * 
     * @param medicamentosMap
     * @return
     */
    private HashMap cargarNumeroAdministracionesTotalesXArticulo(HashMap medicamentosMap, int numeroSolicitud) 
    {
    	logger.info("\n\n*******************SETEANDO EL NUMERO DE ADMINISTRACIONES TOTALES****************************************************************************************************");
    	logger.info("numRegistros " +medicamentosMap.get("numRegistros"));
    	
    	Utilidades.imprimirMapa(medicamentosMap);
    	
		for(int w=0; w<Utilidades.convertirAEntero(medicamentosMap.get("numRegistros")+""); w++)
		{
			logger.info("Cantidad Total Dosis: " + medicamentosMap.get("cantidadtotaldosisordenada_"+w));
			logger.info("Cantidad Unidosis Art: " + medicamentosMap.get("cantidadunidosisarticulo_"+w));

			if( Integer.parseInt(medicamentosMap.get("cantidadtotaldosisordenada_"+w).toString())>0
	        		&& Double.parseDouble(medicamentosMap.get("cantidadunidosisarticulo_"+w).toString())>0)
	        {
				int numeroAdministracionesTotales=AdminMedicamentos.obtenerNumeroDosisAdministradas(numeroSolicitud, Integer.parseInt(medicamentosMap.get("codigo_"+w).toString()));
				String temporal=String.valueOf(medicamentosMap.get("codigosustitutoprincipal_"+w));
				String codigoArt=medicamentosMap.get("codigo_"+w).toString();
				if(!temporal.equals("vacio"))
				{
					codigoArt=temporal.split("@")[1];
					numeroAdministracionesTotales=numeroAdministracionesTotales+AdminMedicamentos.obtenerNumeroDosisAdministradas(numeroSolicitud, Integer.parseInt(codigoArt));
				}
				medicamentosMap.put("numeroAdministracionesTotales_"+codigoArt, numeroAdministracionesTotales);
				
				logger.info("numeroAdministracionesTotales_->"+numeroAdministracionesTotales+" art->"+medicamentosMap.get("codigo_"+w));
				
	        }
		}
		
		logger.info("***********************************************************************************************************************\n\n");
		
		return medicamentosMap;
	}


	/**Metodo encargado de guardar los datos correspondientes a 
	 * insumos y medicamentos.
     * @param adminForm , AdminMedicamentosForm
     * @param con Connection conexion con la fuente de datos.
     * @return ActionForward
	 * @throws SQLException
     */
    private ActionForward accionSalirGuardar(	AdminMedicamentosForm adminForm, 
            									ActionMapping mapping,Connection con,
            									HttpServletRequest request,
            									UsuarioBasico usuario,
												PersonaBasica paciente ) throws IPSException
    {
    	AdminMedicamentos mundo= new AdminMedicamentos();
    	
    	
    	this.finalizarArticulosPrincipalesConSustituto(adminForm,usuario);
    	
    	//PRIMERO VALIDAMOS LA CONCURRENCIA
    	ActionForward validacionConcurrenciaForward= validacionesConcurrencia(con, adminForm, request, mapping, usuario); 
    	if(validacionConcurrenciaForward!=null)
    		return validacionConcurrenciaForward;
    	
    	//SEGUNDO SE HACE EL CALCULO DE LAS UNIDADES DE CONSUMO PARA LOS MEDICAMENTIS QUE TIENEN EL KEY leerunidadesconsumidas_ EN FALSE
    	//Y QUE TIENEN ACTIVO EL CHECK DE ADMINISTRAR
    	HashMap acumuladoAdminDosisMap= calculoUnidadesConsumo(adminForm);
    	
    	ActionForward validacionesLotesForward= validacionesLotes(con, adminForm, request, mapping, usuario, acumuladoAdminDosisMap);
    	if(validacionesLotesForward!=null)
    		return validacionesLotesForward;
    	
    	//SE EVALUA SI SE DEBE HACER DEVOLUCIONES A FARMACIA
    	ActionForward devolucionesAFarmaciaForward= activarPopupDevolucionesAFarmacia(con, adminForm, request, mapping, usuario, acumuladoAdminDosisMap, mundo);
    	logger.info("SE EVALUA SI SE DEBE HACER DEVOLUCIONES A FARMACIA >> "+devolucionesAFarmaciaForward);
	    if(devolucionesAFarmaciaForward!=null)
	    	return devolucionesAFarmaciaForward;
    	
    	//TERCERO INICIALIZAMOS LA TRANSACION 
    	UtilidadBD.iniciarTransaccion(con);
    	
    	//CUARTO SE INGRESA LA ADMINISTRACION BASICA
    	int codigoAdministracion=mundo.insertarAdmin(con, adminForm.getNumeroSolicitud(), usuario.getCodigoCentroCosto(), usuario.getLoginUsuario());
    	if(codigoAdministracion<=0)
    	{
    		logger.warn("Número de solicitud: "+adminForm.getNumeroSolicitud());
    		adminForm.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(), true);
			UtilidadBD.abortarTransaccion(con);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar AdminMedicamentos", "error.adminMedicamentos.errorTransaccion", true);
    	}
    	
    	//QUINTO SE INGRESA EL DETALLE DE LA ADMINISTRACION
    	ActionForward ingresoDetalleForward= ingresarDetalleAdmin(con,codigoAdministracion, adminForm, usuario, mundo, acumuladoAdminDosisMap, request, mapping);
    	if(ingresoDetalleForward!=null)
    	{
    		logger.warn("Número de solicitud: "+adminForm.getNumeroSolicitud());
    		UtilidadBD.abortarTransaccion(con);
			return ingresoDetalleForward;
    	}
    	
    	//SEXTO SE INSERTA EL ACUMULADO ADMIN DOSIS, esto solo aplica para medicamentos
    	boolean ingresoAcumulado= mundo.insertarActualizarAcumuladoAdminDosis(con, acumuladoAdminDosisMap);
    	if(!ingresoAcumulado)
    	{
    		logger.warn("Número de solicitud: "+adminForm.getNumeroSolicitud());
    		adminForm.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(), true);
			UtilidadBD.abortarTransaccion(con);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar AdminMedicamentos", "error.adminMedicamentos.errorTransaccion", true);
    	}
    	
    	//SEPTIMO SE GENERA CARGO EN EL MOMENTO DE ACUMULAR UNA UNIDAD DE CONSUMO, 
    	boolean generoCargoExitoso=this.generarCargoMedicamentosXArticulo(con, acumuladoAdminDosisMap, usuario, paciente, adminForm);
    	if(!generoCargoExitoso)
    	{
    		logger.warn("Número de solicitud: "+adminForm.getNumeroSolicitud());
    		adminForm.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(), true);
			UtilidadBD.abortarTransaccion(con);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar AdminMedicamentos", "error.adminMedicamentos.errorTransaccion", true);
    	}
    	
    	//SE INSERTAN LAS POSIBLES FINALIZACIONES DE LOS ARTICULOS
    	boolean insertoFinalizacion= this.insertarFinalizacionAdminArticulo(con, adminForm, usuario);
    	if(!insertoFinalizacion)
    	{
    		logger.warn("Número de solicitud: "+adminForm.getNumeroSolicitud());
    		adminForm.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(), true);
			UtilidadBD.abortarTransaccion(con);
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar AdminMedicamentos", "error.adminMedicamentos.errorTransaccion", true);
    	}
    	
    	//SE HACEN LAS DEVOLUCIONES A FARMACIA
    	if(!adminForm.getObservacionesDevolucion().equals("") 
    		&& Integer.parseInt(adminForm.getDevolucionMap("numRegistros").toString())>0)
    	{
    		boolean insertoDevolucionMed= AdminMedicamentos.generarDevolucionAutomatica(con, adminForm.getDevolucionMap(), adminForm.getObservacionesDevolucion(), adminForm.getNumeroSolicitud(), usuario, adminForm.getFarmaciaDespacho(), adminForm.getMotivoDevolucion());
    		if(!insertoDevolucionMed)
        	{
        		logger.warn("Número de solicitud: "+adminForm.getNumeroSolicitud());
        		adminForm.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(),true);
    			UtilidadBD.abortarTransaccion(con);
    			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar AdminMedicamentos", "error.adminMedicamentos.errorTransaccion", true);
        	}
    	}
    	
    	//SE VERIFICA QUE TODOS LOS ARTICULOS ESTEN FINALIZADOS PARA PODER CAMBIAR LOS ESTADOS DE LA SOLICITUD
    	if(this.estanTodosArticulosFinalizados(adminForm))
    	{	
    		//bandera para indicar cuando no se debe mostrar boton Administrar Nuevamente
    		adminForm.setArticulosFinalizados(true);
    		
    		if(Cargos.existenCargosPendientesXSolicitud(con, adminForm.getNumeroSolicitud()+""))
    		{
    			Solicitud.cambiarEstadosSolicitudStatico(con, adminForm.getNumeroSolicitud(), ConstantesBD.codigoEstadoFPendiente, ConstantesBD.codigoEstadoHCAdministrada);
    		}
    		else
    		{
    			Solicitud.cambiarEstadosSolicitudStatico(con, adminForm.getNumeroSolicitud(), 0, ConstantesBD.codigoEstadoHCAdministrada);
    		}
    		
    		//si se va ha cambiar el estado entonces se dio el check finalizar, en cuyo caso se debe actualizar la informacion de pyp
			if(Utilidades.esSolicitudPYP(con, adminForm.getNumeroSolicitud()))
			{
				//primero se actualiza la actividad a estado ejecutado
				String codigoActividad=Utilidades.obtenerCodigoActividadProgramaPypPacienteDadaSolicitud(con,adminForm.getNumeroSolicitud());
				Utilidades.actualizarEstadoActividadProgramaPypPaciente(con,codigoActividad,ConstantesBD.codigoEstadoProgramaPYPEjecutado,usuario.getLoginUsuario(),"");
				
				//segundo se actualiza el acumulado de la actividad 
				if(Utilidades.actualizarAcumuladoPYP(con, adminForm.getNumeroSolicitud()+"", usuario.getCodigoCentroAtencion()+"")<=0)
				{
					logger.warn("Error actualizando el acumulado PYP de la solicitud= "+ adminForm.getNumeroSolicitud());
					UtilidadBD.abortarTransaccion(con);
					adminForm.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(),true);
					return ComunAction.accionSalirCasoError(mapping, request, con, logger, "error en el guardar AdminMedicamentos", "error.adminMedicamentos.errorTransaccion", true);
				}
			}
			
			//SE INSERTA LA INFORMACION AUTOMATICA DE EPICRISIS DE LAS JUSTIFICACIONES NO POS
	    	Epicrisis1.insertarInfoAutomaticaJusArticulosEpicrisis(con, adminForm.getNumeroSolicitud()+"", usuario, paciente, codigoAdministracion+"");
	    	
			//para efectos de rendimiento se mantiene la coleccion con el listado de medicamentos en memoria, 
			//esto con el fin de q cuando le den boton volver no tenga que realizar nuevamente la consulta
			//entonces se debe eliminar ese campo de la coleccion. CAMBIO ORDENADO POR JULIAN GOMEZ 2007-11-19
			Iterator it=adminForm.getCol().iterator();
			HashMap mapa, mapaAEliminar=null;
			String numeroSolicitud="";
			while (it.hasNext())
		    {
				mapa=(HashMap)it.next();
				numeroSolicitud=mapa.get("numerosolicitud")+"";
				logger.info("\n numeroSolicitud="+numeroSolicitud);
				if(numeroSolicitud.equals(adminForm.getNumeroSolicitud()+""))
		    	{
		    		logger.info("!!!!!!!!!!!!!SON IGUALES, REMOVEMOS");
		    		mapaAEliminar=mapa;
		    	}
		    }
			
			if(mapaAEliminar!=null)
			{
				adminForm.getCol().remove(mapaAEliminar);
			}
			
		}
    	
    	// SE ACTUALIZA EL NUEMRO DE AUTORIZACION
    	/*
    	Solicitud sol= new Solicitud();
    	sol.actualizarNumeroAutorizacionTransaccional(con, adminForm.getNumeroAutorizacion(), adminForm.getNumeroSolicitud(), ConstantesBD.continuarTransaccion);
    	*/
    	
    	UtilidadBD.finalizarTransaccion(con);
    	//UtilidadBD.closeConnection(con);
    	//La conexion se cierra en el resumen
    	return this.accionResumenAdmin(adminForm, mapping, con);
    }
    
    /**
     * 
     * @param adminForm
     * @param usuario 
     */
    private void finalizarArticulosPrincipalesConSustituto(AdminMedicamentosForm adminForm, UsuarioBasico usuario) 
    {
    	/****PARTE DE MEDICAMENTOS****/
    	if(!UtilidadTexto.getBoolean(ValoresPorDefecto.getMostrarAdminMedicamentosArticulosDespachoCero(usuario.getCodigoInstitucionInt())))
    	{
    		for(int w=0; w<Integer.parseInt(adminForm.getMedicamentosMap("numRegistros").toString()); w++)
			{
	    		String sustituto=String.valueOf(adminForm.getMedicamentosMap("codigosustitutoprincipal_"+w));
				if(!sustituto.equals("vacio"))
				{
		    		if(!adminForm.getMedicamentosMap("finalizararticulo_"+w).toString().equals(ConstantesBD.acronimoNo))
					{
			    		for(int j=0; j<Integer.parseInt(adminForm.getMedicamentosMap("numRegistros").toString()); j++)
						{
				    		if(adminForm.getMedicamentosMap("codigo_"+w).toString().equals(adminForm.getMedicamentosMap("tienesustituto_"+j).toString()))
							{
					    		adminForm.setMedicamentosMap("finalizararticulo_"+j,ConstantesBD.acronimoSi);
							}
						}
					}
				}
			}
    	}
    }


	/**
     * 
     * @param con
     * @param adminForm
     * @param request
     * @param mapping
     * @param usuario
     * @param acumuladoAdminDosisMap
     * @param mundo
     * @return
     */
    private ActionForward activarPopupDevolucionesAFarmacia(	Connection con, AdminMedicamentosForm adminForm, HttpServletRequest request, 
    															ActionMapping mapping, UsuarioBasico usuario, HashMap acumuladoAdminDosisMap,
    															AdminMedicamentos mundo) 
    {
    	logger.info("\n\n ***********************************LLEGA A activarPopupDevolucionesAFarmacia************************************");
    	//si las observaviones de la devolucion no son vacias debe enviar null para no volver a mostrar el popup
    	if(adminForm.getObservacionesDevolucion().equals(""))
    	{
    		/*se resetea el valor*/
	        adminForm.setMostrarMensajeSaldoPendienteAdministrar(false);
	        adminForm.resetHashMapDevolucion();
	        int numReg= Integer.parseInt(adminForm.getDevolucionMap("numRegistros").toString());
	        
	        //lo primero es evaluar x articulo si esta finalizado y si debe hacer devolucion
	        for(int w=0; w<Integer.parseInt(adminForm.getMedicamentosMap("numRegistros").toString()); w++)
			{
	        	logger.info("articulo="+adminForm.getMedicamentosMap("codigo_"+w));
	        	logger.info("finalizar="+adminForm.getMedicamentosMap("finalizararticulo_"+w));
	        	logger.info("lote="+adminForm.getMedicamentosMap("lote_"+w));
	        	logger.info("fechaVencimientolote="+adminForm.getMedicamentosMap("fechavencimientolote_"+w));
				if(adminForm.getMedicamentosMap("finalizararticulo_"+w).toString().equals(ConstantesBD.acronimoSi))
				{
					int cantidad=0; 
	    			if(adminForm.getMedicamentosMap("leerunidadesconsumidas_"+w).toString().equals(ConstantesBD.acronimoNo) )	
	    				cantidad=obtenerIncrementoCantidadXDosis(w, acumuladoAdminDosisMap, adminForm);
	    			else
	    				cantidad=Integer.parseInt(adminForm.getMedicamentosMap("unidadesconsumidas_"+w).toString());
	    			
	    			//restamos las cantidades de saldos, para hacer el calculo correcto de la devolucion
	    			cantidad=cantidad-Utilidades.convertirAEntero(adminForm.getMedicamentosMap("unidadessaldos_"+w)+"");
	    			
	    			
	    			logger.info("cantidad calculada o en MEM="+cantidad);
	    			int cantidadDespachadaFarmacia=0;
	    			if(!UtilidadTexto.isEmpty(adminForm.getMedicamentosMap("unidadesdespachadas_"+w).toString())
	    				&& UtilidadTexto.isNumber(adminForm.getMedicamentosMap("unidadesdespachadas_"+w).toString()))
	    			{	
	    				cantidadDespachadaFarmacia= Integer.parseInt(adminForm.getMedicamentosMap("unidadesdespachadas_"+w).toString());
	    			}	
	    			logger.info("cantidad despachada="+cantidadDespachadaFarmacia);
	    			int cantidadAdministradaFarmacia=Integer.parseInt(adminForm.getMedicamentosMap("unidadesconsumidasxfarmacia_"+w).toString());
	    			logger.info("cantidad Administrada x farmacia sin mem="+cantidadAdministradaFarmacia);
	    			//si la cantidad calculada no es traida por el paciente entonces se debe incrementar en la cantidadAdministradaFarmacia
	    			if(adminForm.getMedicamentosMap("traidopaciente_"+w).toString().equals(ConstantesBD.acronimoNo))
	    				cantidadAdministradaFarmacia+=cantidad;  
	    			logger.info("cantidad Administrada x farmacia con mem="+cantidadAdministradaFarmacia+ " traido pac="+adminForm.getMedicamentosMap("traidopaciente_"+w));
	    			
	    			if(cantidadDespachadaFarmacia>cantidadAdministradaFarmacia)
	    			{	
	    				logger.info("LA CANTIDAD DESPACHADA ES MAYOR A LA ADMINISTRADA!!!! DEVOLUCION="+(cantidadDespachadaFarmacia-cantidadAdministradaFarmacia));
	    				//se deben diferenciar los articulos por el lote al cual pertenecen
	    				//si el articulo NO maneja lotes
				    	if(!Articulo.articuloManejaLote(con, Integer.parseInt(adminForm.getMedicamentosMap("codigo_"+w).toString()), usuario.getCodigoInstitucionInt()))
						{
		    				adminForm.setDevolucionMap("codigoArtMedDevolucion_"+numReg,adminForm.getMedicamentosMap("codigo_"+w)+"");
						 	adminForm.setDevolucionMap("descripcionArtMedDevolucion_"+numReg,adminForm.getMedicamentosMap("descripcion_"+w)+"");
						 	adminForm.setDevolucionMap("cantidadMedDevolver_"+numReg,(cantidadDespachadaFarmacia-cantidadAdministradaFarmacia)+"");
						 	adminForm.setDevolucionMap("lote_"+numReg,adminForm.getMedicamentosMap("lote_"+w)+"");
						 	adminForm.setDevolucionMap("fechaVencimientoLote_"+numReg,adminForm.getMedicamentosMap("fechavencimientolote_"+w)+"");
						 	adminForm.setDevolucionMap("existenordenesante_"+numReg,adminForm.getMedicamentosMap("existenordenesante_"+w)+"");
						 	adminForm.setDevolucionMap("indexfinalizar_"+numReg,w);
						 	adminForm.setDevolucionMap("confirmarfinalizar_"+numReg,ConstantesBD.acronimoSi);						 	
						 	numReg++;
						}
				    	else
				    	{
				    		HashMap cantidadesADevolverArticuloXLoteMap= obtenerCantidadesADevolverArticuloXLote(con, adminForm.getMedicamentosMap("codigo_"+w).toString(), adminForm.getMedicamentosMap("descripcion_"+w).toString(), adminForm.getMedicamentosMap("lote_"+w).toString(), adminForm.getMedicamentosMap("fechavencimientolote_"+w).toString(), adminForm.getMedicamentosMap("traidopaciente_"+w).toString(), cantidad, adminForm);
				    		for(int x=0; x<Integer.parseInt(cantidadesADevolverArticuloXLoteMap.get("numRegistros").toString()); x++)
				    		{
				    			adminForm.setDevolucionMap("codigoArtMedDevolucion_"+numReg, cantidadesADevolverArticuloXLoteMap.get("codigoArtMedDevolucion_"+x)+"");
							 	adminForm.setDevolucionMap("descripcionArtMedDevolucion_"+numReg, cantidadesADevolverArticuloXLoteMap.get("descripcionArtMedDevolucion_"+x)+"");
							 	adminForm.setDevolucionMap("cantidadMedDevolver_"+numReg, cantidadesADevolverArticuloXLoteMap.get("cantidadMedDevolver_"+x)+"");
							 	adminForm.setDevolucionMap("lote_"+numReg, cantidadesADevolverArticuloXLoteMap.get("lote_"+x)+"");
							 	adminForm.setDevolucionMap("fechaVencimientoLote_"+numReg, cantidadesADevolverArticuloXLoteMap.get("fechavencimientolote_"+x)+"");
							 	adminForm.setDevolucionMap("existenordenesante_"+numReg,adminForm.getMedicamentosMap("existenordenesante_"+w)+"");
							 	adminForm.setDevolucionMap("indexfinalizar_"+numReg,w);
							 	adminForm.setDevolucionMap("confirmarfinalizar_"+numReg,ConstantesBD.acronimoSi);
							 	numReg++;
				    		}
				    	}
	    			}	
				}
			}
	        
	        logger.info("\nMAPADEVOLUCIONES MEDICAMENTOS="+adminForm.getDevolucionMap());
	        
	        //ahora se hace la evaluacion para los insumos
	        /**PARTE DE INSUMOS**/
			if(adminForm.getFinalizarInsumos().equals(ConstantesBD.acronimoSi))
			{	
				for(int w=0; w<(Integer.parseInt(adminForm.getInsumosMap("numRegistros").toString())); w++)
				{
					logger.info("numReg->"+adminForm.getInsumosMap("numRegistros"));
					logger.info("articulo->"+adminForm.getInsumosMap("codigo_"+w));
					logger.info("loteinsumo->"+adminForm.getInsumosMap("loteinsumo_"+w));
					logger.info("fechavencimientoloteinsumo->"+adminForm.getInsumosMap("fechavencimientoloteinsumo_"+w));
					int cantidadDespachadaFarmacia= Integer.parseInt(adminForm.getInsumosMap("cantidaddespacho_"+w).toString());
					logger.info("cantidadDespachadaFarmacia->"+cantidadDespachadaFarmacia);
					int cantidadAdministradaFarmacia= Integer.parseInt(adminForm.getInsumosMap("consumofarmacia_"+w).toString());
					int cantidadAAdministrar=0;
					if(adminForm.getInsumosMap("traidopacienteinsumo_"+w).toString().equals(ConstantesBD.acronimoNo))
					{
						cantidadAAdministrar=Integer.parseInt(adminForm.getInsumosMap("consumo_"+w).toString());
						cantidadAdministradaFarmacia+=cantidadAAdministrar;
					}
					logger.info("cantidadAdministradaFarmacia->"+cantidadAdministradaFarmacia);
					if(cantidadDespachadaFarmacia>cantidadAdministradaFarmacia)
	    			{	
						logger.info("entra a asignar decolucion!!!!!");
						if(!Articulo.articuloManejaLote(con, Integer.parseInt(adminForm.getInsumosMap("codigo_"+w).toString()), usuario.getCodigoInstitucionInt()))
						{
			    			adminForm.setDevolucionMap("codigoArtMedDevolucion_"+numReg,adminForm.getInsumosMap("codigo_"+w)+"");
						 	adminForm.setDevolucionMap("descripcionArtMedDevolucion_"+numReg,adminForm.getInsumosMap("descripcion_"+w)+"");
						 	adminForm.setDevolucionMap("cantidadMedDevolver_"+numReg,(cantidadDespachadaFarmacia-cantidadAdministradaFarmacia)+"");
						 	adminForm.setDevolucionMap("lote_"+numReg,adminForm.getInsumosMap("loteinsumo_"+w)+"");
						 	adminForm.setDevolucionMap("fechaVencimientoLote_"+numReg,adminForm.getInsumosMap("fechavencimientoloteinsumo_"+w)+"");
						 	
						 	adminForm.setDevolucionMap("existenordenesante_"+numReg,adminForm.getInsumosMap("existenordenesante_"+w)+"");
						 	adminForm.setDevolucionMap("indexfinalizar_"+numReg,ConstantesBD.acronimoNo);
						 	adminForm.setDevolucionMap("confirmarfinalizar_"+numReg,ConstantesBD.acronimoSi);
						 	
						 	numReg++;
						}
						else
				    	{
				    		HashMap cantidadesADevolverArticuloXLoteMap= obtenerCantidadesADevolverArticuloXLote(con, adminForm.getInsumosMap("codigo_"+w).toString(), adminForm.getInsumosMap("descripcion_"+w).toString(), adminForm.getInsumosMap("loteinsumo_"+w).toString(), adminForm.getInsumosMap("fechavencimientoloteinsumo_"+w).toString(), adminForm.getInsumosMap("traidopacienteinsumo_"+w).toString(), cantidadAAdministrar, adminForm);
				    		for(int x=0; x<Integer.parseInt(cantidadesADevolverArticuloXLoteMap.get("numRegistros").toString()); x++)
				    		{
				    			adminForm.setDevolucionMap("codigoArtMedDevolucion_"+numReg, cantidadesADevolverArticuloXLoteMap.get("codigoArtMedDevolucion_"+x)+"");
							 	adminForm.setDevolucionMap("descripcionArtMedDevolucion_"+numReg, cantidadesADevolverArticuloXLoteMap.get("descripcionArtMedDevolucion_"+x)+"");
							 	adminForm.setDevolucionMap("cantidadMedDevolver_"+numReg, cantidadesADevolverArticuloXLoteMap.get("cantidadMedDevolver_"+x)+"");
							 	adminForm.setDevolucionMap("lote_"+numReg, cantidadesADevolverArticuloXLoteMap.get("lote_"+x)+"");
							 	adminForm.setDevolucionMap("fechaVencimientoLote_"+numReg, cantidadesADevolverArticuloXLoteMap.get("fechavencimientolote_"+x)+"");
							 	
								adminForm.setDevolucionMap("existenordenesante_"+numReg,adminForm.getInsumosMap("existenordenesante_"+w)+"");
							 	adminForm.setDevolucionMap("indexfinalizar_"+numReg,ConstantesBD.acronimoNo);
							 	adminForm.setDevolucionMap("confirmarfinalizar_"+numReg,ConstantesBD.acronimoSi);							 	
							 	
							 	numReg++;
				    		}
				    	}
					}
				}
			}
	        
			logger.info("\nMAPADEVOLUCIONES MEDICAMENTOS - INSUMOS=>"+adminForm.getDevolucionMap());
        	
	        adminForm.setDevolucionMap("numRegistros", numReg+"");
	        if(numReg>0)
	        {
	        	adminForm.setMostrarMensajeSaldoPendienteAdministrar(true);
	        	UtilidadBD.closeConnection(con);		
    	        return mapping.findForward("detalleSolicitud")	;
	        }
    	}    
        return null;
    }


    /**
     * 
     * @param con
     * @param adminForm
     * @return
     */
	private HashMap obtenerCantidadesADevolverArticuloXLote(Connection con, String codigoArticulo, String descripcionArticulo, 
															String lote, String fechaVencimientoLote, 
															String traidoPaciente, int cantidadAAdministrarFarmacia, AdminMedicamentosForm adminForm) 
	{
		HashMap lotesDespachoNoAdministradosTotalmente=UtilidadInventarios.obtenerLotesDespachoNoAdministradosTotalmente(con, adminForm.getNumeroSolicitud()+"", codigoArticulo);
		HashMap cantidadesADevolverArticuloXLoteMap=new HashMap();
		int numReg=0;
		
		if(traidoPaciente.equals(ConstantesBD.acronimoSi))
			cantidadAAdministrarFarmacia=0;
		
		for(int w=0; w<Integer.parseInt(lotesDespachoNoAdministradosTotalmente.get("numRegistros").toString()); w++)
		{
			//si el lote que se pretende administrar es el mismo al consultado entonces en este lote se debe descontar las que estan en mem y
			//evaluar  si se debe hacer o no devolucion para ese lote
			if(lote.equals(lotesDespachoNoAdministradosTotalmente.get("lote_"+w).toString()) 
				&& fechaVencimientoLote.equals(lotesDespachoNoAdministradosTotalmente.get("fechavencimiento_"+w).toString()))
			{
				int cantidadADevolver= Integer.parseInt(lotesDespachoNoAdministradosTotalmente.get("existenciaxlote_"+w).toString())-cantidadAAdministrarFarmacia;
				if(cantidadADevolver>0)
				{	
					cantidadesADevolverArticuloXLoteMap.put("codigoArtMedDevolucion_"+numReg, codigoArticulo);
					cantidadesADevolverArticuloXLoteMap.put("descripcionArtMedDevolucion_"+numReg, descripcionArticulo);
					cantidadesADevolverArticuloXLoteMap.put("cantidadMedDevolver_"+numReg,cantidadADevolver);
					cantidadesADevolverArticuloXLoteMap.put("lote_"+numReg, lotesDespachoNoAdministradosTotalmente.get("lote_"+w).toString());
					cantidadesADevolverArticuloXLoteMap.put("fechavencimientolote_"+numReg, lotesDespachoNoAdministradosTotalmente.get("fechavencimiento_"+w).toString());
					numReg++;
				}	
			}
			//de lo contrario se devuelve el total de existencias  de ese lote
			else
			{
				cantidadesADevolverArticuloXLoteMap.put("codigoArtMedDevolucion_"+numReg, codigoArticulo);
				cantidadesADevolverArticuloXLoteMap.put("descripcionArtMedDevolucion_"+numReg, descripcionArticulo);
				cantidadesADevolverArticuloXLoteMap.put("cantidadMedDevolver_"+numReg,lotesDespachoNoAdministradosTotalmente.get("existenciaxlote_"+w).toString());
				cantidadesADevolverArticuloXLoteMap.put("lote_"+numReg,lotesDespachoNoAdministradosTotalmente.get("lote_"+w).toString());
				cantidadesADevolverArticuloXLoteMap.put("fechavencimientolote_"+numReg,lotesDespachoNoAdministradosTotalmente.get("fechavencimiento_"+w).toString());
				numReg++;
			}
		}
		cantidadesADevolverArticuloXLoteMap.put("numRegistros", numReg+"");
		return cantidadesADevolverArticuloXLoteMap;
	}


	/**
     * Validaciones de los lotes, no se hizo en el form porque era requerido hacer la cantidad del calculo
     * @param con
     * @param adminForm
     * @param request
     * @param mapping
     * @param usuario
     * @param acumuladoAdminDosisMap
     * @return
     */
	private ActionForward validacionesLotes(Connection con, AdminMedicamentosForm adminForm, HttpServletRequest request, ActionMapping mapping, UsuarioBasico usuario, HashMap acumuladoAdminDosisMap) 
	{
		ActionErrors errores= new ActionErrors();
		 //VALIDACIONES DE LOS LOTES para medicamentos
		for(int w=0; w< Integer.parseInt(adminForm.getMedicamentosMap("numRegistros").toString()); w++) 
		{
			//solo se valida para los medicamentos que tienen el centinela adminitrar en true
			if(adminForm.getMedicamentosMap("administrar_"+w).toString().equals(ConstantesBD.acronimoSi))
			{	
				//se incrementa la cantidad
    			int cantidad=0; 
    			if(adminForm.getMedicamentosMap("leerunidadesconsumidas_"+w).toString().equals(ConstantesBD.acronimoNo) )	
    				cantidad=obtenerIncrementoCantidadXDosis(w, acumuladoAdminDosisMap, adminForm);
    			else
    				cantidad=Integer.parseInt(adminForm.getMedicamentosMap("unidadesconsumidas_"+w).toString());
			    //LO PRIMERO QUE SE DEBE HACER ES VERIFICAR SI FUE TRAIDO POR EL PACIENTE, Y QUE LA ADMINISTRACION SEA MAYOR QUE CERO, SI ES ASI ENTONCES NO SE VALIDA LOTES
			    if(adminForm.getMedicamentosMap("traidopaciente_"+w).toString().equals(ConstantesBD.acronimoNo) && cantidad>0)
			    {
			    	//si el articulo maneja lotes
			    	if(Articulo.articuloManejaLote(con, Integer.parseInt(adminForm.getMedicamentosMap("codigo_"+w).toString()), usuario.getCodigoInstitucionInt()))
					{
						//como maneja lote entonces la info no puede ser vacia
						if(UtilidadTexto.isEmpty(adminForm.getMedicamentosMap("lote_"+w).toString()))
						{
							errores.add("", new ActionMessage("errors.required","La información de Lote para el Medicamento "+adminForm.getMedicamentosMap("codigo_"+w).toString()));
						}
						//si ya tiene informacion de lote entonces la cantidad administrada no puede ser mayor a la existente en el lote
						else
						{
							int existenciasLote= Integer.parseInt(adminForm.getMedicamentosMap("existenciaxlote_"+w).toString());
							if(cantidad>existenciasLote)
							{
								//errores.add("", new ActionMessage("errors.debeSerNumeroMenorIgual", "El Total Administrado para el medicamento "+adminForm.getMedicamentosMap("codigo_"+w), "la cantidad de existencias "+existenciasLote+" del lote "+adminForm.getMedicamentosMap("lote_"+w)));
								errores.add("", new ActionMessage("errors.cantidadMayor", "Para el medicamento "+adminForm.getMedicamentosMap("codigo_"+w)+", La cantidad + Total Administrado Farmacia(F) "));
							}
						}
					}
			    }
		    }	
		}
		
		//VALIDACIONES DE LOS LOTES insumos
        for(int w=0; w < Integer.parseInt(adminForm.getInsumosMap("numRegistros").toString()); w ++ ) 
	    {
	    	int cantidadAdministradaInsumo= Integer.parseInt(adminForm.getInsumosMap("consumo_"+w).toString());
	    	//LO PRIMERO QUE SE DEBE HACER ES VERIFICAR SI FUE TRAIDO POR EL PACIENTE, Y QUE LA ADMINISTRACION SEA MAYOR QUE CERO, SI ES ASI ENTONCES NO SE VALIDA LOTES
	    	if(adminForm.getInsumosMap("traidopacienteinsumo_"+w).toString().equals(ConstantesBD.acronimoNo) && cantidadAdministradaInsumo>0)
	    	{
	    		//si el articulo maneja lotes
	    		if(Articulo.articuloManejaLote(con, Integer.parseInt( adminForm.getInsumosMap("codigo_"+w).toString()), usuario.getCodigoInstitucionInt()))
				{
					//como maneja lote entonces la info no puede ser vacia
					if(UtilidadTexto.isEmpty(adminForm.getInsumosMap("loteinsumo_"+w).toString()))
					{
						errores.add("", new ActionMessage("errors.required","La información de Lote para el Insumo "+adminForm.getInsumosMap("codigo_"+w).toString()));
					}
					//si ya tiene informacion de lote entonces la cantidad administrada no puede ser mayor a la existente en el lote
					else
					{
						int existenciasLoteInsumo= Integer.parseInt(adminForm.getInsumosMap("existenciaxloteinsumo_"+w).toString());
						if(cantidadAdministradaInsumo>existenciasLoteInsumo)
						{
							errores.add("", new ActionMessage("errors.debeSerNumeroMenorIgual", "La cantidad Administrada "+cantidadAdministradaInsumo+" para el Insumo "+adminForm.getInsumosMap("codigo_"+w), "la cantidad de existencias "+existenciasLoteInsumo+" del lote "+adminForm.getInsumosMap("loteinsumo_"+w)));
						}
					}
				}
	    	}
	     }
		
		if(!errores.isEmpty())
    	{
			saveErrors(request,errores);
    		UtilidadBD.closeConnection(con);
    		return mapping.findForward("detalleSolicitud");
    	}
		
		return null;
	}


	/**
     * 
     * @param adminForm
     */
    private HashMap calculoUnidadesConsumo(AdminMedicamentosForm adminForm) 
    {
    	logger.info("\n\n\n*****************************ENTRA calculoUnidadesConsumo ****************************************");
    	HashMap acumuladoAdminDosisMap= new HashMap();
    	int numReg=0;
    	acumuladoAdminDosisMap.put("numRegistros", numReg+"");
    	
    	//SE HACE EL CALCULO DE LAS UNIDADES DE CONSUMO PARA LOS MEDICAMENTOS QUE TIENEN EL KEY leerunidadesconsumidas_ EN FALSE
    	//Y QUE TIENEN ACTIVO EL CHECK DE ADMINISTRAR
    	for(int w=0; w<Integer.parseInt(adminForm.getMedicamentosMap("numRegistros").toString()); w++)
    	{
    		logger.info("articulo==>"+adminForm.getMedicamentosMap("codigo_"+w).toString());
    		logger.info("leerunidadesconsumidas==>"+adminForm.getMedicamentosMap("leerunidadesconsumidas_"+w).toString());
    		logger.info("administrar==>"+adminForm.getMedicamentosMap("administrar_"+w).toString());
    		
    		if(adminForm.getMedicamentosMap("leerunidadesconsumidas_"+w).toString().equals(ConstantesBD.acronimoNo) 
    			&& adminForm.getMedicamentosMap("administrar_"+w).toString().equals(ConstantesBD.acronimoSi))
    		{
    			logger.info("\n cumplecondiciones administrar=S y leerUnidadesConsumidas=N entra!!!!!!!!!");
    			double cantidadConsumoAAcumular= 0;
    			/*ESTA CANTIDAD DE CONSUMO A ACUMULAR  SE SUMA A LAS ANTERIORES ADMINISTRACIONES*/
    			int cantidadConsumoAAcumularAprox=0;
    			logger.info("multidosis? "+adminForm.getMedicamentosMap("multidosis_"+w));
    			if(adminForm.getMedicamentosMap("multidosis_"+w).toString().equals(ConstantesBD.acronimoNo))
    			{
    				logger.info("dosis-->"+adminForm.getMedicamentosMap("dosis_"+w).toString()+" cantidadunidosis->"+adminForm.getMedicamentosMap("cantidadunidosisarticulo_"+w).toString());
    				cantidadConsumoAAcumular= Double.parseDouble(adminForm.getMedicamentosMap("dosis_"+w).toString())/Double.parseDouble(adminForm.getMedicamentosMap("cantidadunidosisarticulo_"+w).toString());
    				logger.info("************************cantidadConsumoAAcumular-->"+cantidadConsumoAAcumular);
    				try
    				{
    					cantidadConsumoAAcumularAprox=UtilidadTexto.aproximarSiguienteUnidad(cantidadConsumoAAcumular+"");
    				}
    				catch (Exception e) 
    				{
    					logger.error("error aproximando a la sgt unidad!!!!");
    					cantidadConsumoAAcumularAprox=0;
					}
    				logger.info("\n---------->cantidadConsumoAAcumularAprox="+cantidadConsumoAAcumularAprox);
    			}
    			else
    			{
    				int nroTotalDosisAdmin=Integer.parseInt(adminForm.getMedicamentosMap("numerodosisadminfarmacia_"+w).toString())
    										+Integer.parseInt(adminForm.getMedicamentosMap("numerodosisadminpaciente_"+w).toString());
    				//toca sumarle la actual
    				nroTotalDosisAdmin++;
    				logger.info("nroTotalDosiAdmin="+nroTotalDosisAdmin);
    				double dosisAcumulada= Double.parseDouble(adminForm.getMedicamentosMap("dosis_"+w).toString()) * nroTotalDosisAdmin;
    				logger.info("dosisAcumulada="+dosisAcumulada+ " dosis->"+adminForm.getMedicamentosMap("dosis_"+w));
    				int primeraParte= UtilidadTexto.aproximarSiguienteUnidad((dosisAcumulada/Double.parseDouble(adminForm.getMedicamentosMap("cantidadunidosisarticulo_"+w).toString()))+"");
    				logger.info("Primera parte->"+primeraParte);
    				int cantidadUnidadesConsumoAcumuladasAdminAnteriores= Integer.parseInt(adminForm.getMedicamentosMap("unidadesadminfarmacia_"+w).toString())
    									+ Integer.parseInt(adminForm.getMedicamentosMap("unidadesadminpaciente_"+w).toString());
    				logger.info("cantidadUnidadesConsumoAcumuladasAdminAnteriores->"+cantidadUnidadesConsumoAcumuladasAdminAnteriores);
    				int cantidadConsumoAAcumularActualAdmin=primeraParte-cantidadUnidadesConsumoAcumuladasAdminAnteriores;
    				cantidadConsumoAAcumularAprox=  cantidadConsumoAAcumularActualAdmin;
    				logger.info("\n---------->cantidadConsumoAAcumularAprox="+cantidadConsumoAAcumularAprox);
    			}
    			
    			int incrementoDosisAdminFar=0;
    			int incrementoDosisAdminPac=0;
    			int incrementoUnidadesAdminFarmacia=0;
    			int incrementoUnidadesAdminPaciente=0;
    			acumuladoAdminDosisMap.put("articulo_"+numReg, adminForm.getMedicamentosMap("codigo_"+w));
    			acumuladoAdminDosisMap.put("numeroSolicitud_"+numReg, adminForm.getNumeroSolicitud());
    			acumuladoAdminDosisMap.put("fecha_"+numReg, adminForm.getMedicamentosMap("fecha_"+w));
    			if(adminForm.getMedicamentosMap("traidopaciente_"+w).toString().equals(ConstantesBD.acronimoNo))
    			{	
    				incrementoDosisAdminFar++;
    				incrementoUnidadesAdminFarmacia=cantidadConsumoAAcumularAprox;
    			}	
    			else
    			{	
    				incrementoDosisAdminPac++;
    				incrementoUnidadesAdminPaciente=cantidadConsumoAAcumularAprox;
    			}	
    			acumuladoAdminDosisMap.put("incrementoNumeroDosisAdminFar_"+numReg, incrementoDosisAdminFar+"");
    			acumuladoAdminDosisMap.put("incrementoNumeroDosisAdminPac_"+numReg, incrementoDosisAdminPac+"");
    			acumuladoAdminDosisMap.put("incrementoUnidadesAdminFarmacia_"+numReg, incrementoUnidadesAdminFarmacia+"");
    			acumuladoAdminDosisMap.put("incrementoUnidadesAdminPaciente_"+numReg, incrementoUnidadesAdminPaciente+"");
    			
    			//se incrementa la cantidad
    			numReg++;
    		}
    	}
    	acumuladoAdminDosisMap.put("numRegistros", numReg);
    	
    	logger.info("\n acumuladoAdminDosisMap->"+acumuladoAdminDosisMap);
		return acumuladoAdminDosisMap;
	}

    /**
     * 
     * @param con
     * @param codigoAdministracion
     * @param adminForm
     * @param usuario
     * @param mundo
     * @param acumuladoAdminDosisMap
     * @param request
     * @param mapping
     * @return
     */
    private ActionForward ingresarDetalleAdmin(	Connection con, 
    											int codigoAdministracion, 
    											AdminMedicamentosForm adminForm, 
    											UsuarioBasico usuario, 
    											AdminMedicamentos mundo, 
    											HashMap acumuladoAdminDosisMap, 
    											HttpServletRequest request,
    											ActionMapping mapping) 
    {
    	//PARTE DE MEDICAMENTOS
    	for(int w=0; w<Integer.parseInt(adminForm.getMedicamentosMap("numRegistros").toString()); w++)
    	{
    		if((adminForm.getMedicamentosMap("administrar_"+w)+"").equals(ConstantesBD.acronimoSi)
    			|| (adminForm.getMedicamentosMap("nadaviaoral_"+w)+"").equals(ConstantesBD.acronimoSi)
    			|| (adminForm.getMedicamentosMap("usuariorechazo_"+w)+"").equals(ConstantesBD.acronimoSi))
    		{
    			//se incrementa la cantidad
    			int cantidad; 
    			
    			if(adminForm.getMedicamentosMap("nadaviaoral_"+w).toString().equals(ConstantesBD.acronimoSi) || adminForm.getMedicamentosMap("usuariorechazo_"+w).toString().equals(ConstantesBD.acronimoSi))
    				cantidad=0;
    			else {
	    			if(adminForm.getMedicamentosMap("leerunidadesconsumidas_"+w).toString().equals(ConstantesBD.acronimoNo) )	
	    				cantidad=obtenerIncrementoCantidadXDosis(w, acumuladoAdminDosisMap, adminForm);
	    			else
	    				cantidad=Integer.parseInt(adminForm.getMedicamentosMap("unidadesconsumidas_"+w).toString());
    			}	
    			if(mundo.insertarDetalleAdmin(	con, Integer.parseInt(adminForm.getMedicamentosMap("codigo_"+w).toString()),
    										Integer.parseInt(adminForm.getMedicamentosMap("artppal_"+w).toString()),
    										codigoAdministracion, 
    										adminForm.getMedicamentosMap("fecha_"+w).toString(), 
    										adminForm.getMedicamentosMap("hora_"+w).toString(), 
    										cantidad, 
    										adminForm.getMedicamentosMap("observacionnueva_"+w).toString(), 
    										adminForm.getMedicamentosMap("traidopaciente_"+w).toString(), 
    										adminForm.getMedicamentosMap("lote_"+w).toString(), 
    										adminForm.getMedicamentosMap("fechavencimientolote_"+w).toString(),
    										adminForm.getMedicamentosMap("unidadessaldos_"+w).toString(),
    										adminForm.getMedicamentosMap("adelantoxnecesidad_"+w).toString(),
    										adminForm.getMedicamentosMap("nadaviaoral_"+w).toString(),
    										adminForm.getMedicamentosMap("usuariorechazo_"+w).toString())<=0)
    			{
    				logger.info("NO inserto el detalle de la admin articulo="+adminForm.getMedicamentosMap("codigo_"+w)+" ");
    				ActionErrors errores = new ActionErrors();
    				errores.add("", new ActionMessage("error.errorEnBlanco", "El despacho es menor a la cantidad a administrar del medicamento "+adminForm.getMedicamentosMap("codigo_"+w)));
    				saveErrors(request, errores);	
    				//LA CONEXION SE ABORTA Y CIERRA EN EL LLAMADO DE ESTE METODO									
    				return mapping.findForward("detalleSolicitud");
    			}
    		}
    	}
    	    	
    	//PARTE DE INSUMOS
    	for(int w=0; w<Integer.parseInt(adminForm.getInsumosMap("numRegistros").toString()); w++)
    	{
    		if(Integer.parseInt(adminForm.getInsumosMap("consumo_"+w).toString())>0)
    		{   
    			if(!adminForm.getMedicamentosMap().containsKey("unidadessaldos_"+w))
    				adminForm.setMedicamentosMap("unidadessaldos_"+w, "");
    			if(!adminForm.getMedicamentosMap().containsKey("adelantoxnecesidad_"+w))
    				adminForm.setMedicamentosMap("adelantoxnecesidad_"+w, "");
    			if(!adminForm.getMedicamentosMap().containsKey("nadaviaoral_"+w))
    				adminForm.setMedicamentosMap("nadaviaoral_"+w, "");
    			if(!adminForm.getMedicamentosMap().containsKey("usuariorechazo_"+w))
    				adminForm.setMedicamentosMap("usuariorechazo_"+w, "");
    			
    			Utilidades.imprimirMapa(adminForm.getMedicamentosMap());
    			if(mundo.insertarDetalleAdmin(	con, Integer.parseInt(adminForm.getInsumosMap("codigo_"+w).toString()), 
    					ConstantesBD.codigoNuncaValido,
						codigoAdministracion, 
						UtilidadFecha.getFechaActual(), 
						UtilidadFecha.getHoraActual(), 
						Integer.parseInt(adminForm.getInsumosMap("consumo_"+w).toString()), 
						"", 
						adminForm.getInsumosMap("traidopacienteinsumo_"+w).toString(), 
						adminForm.getInsumosMap("loteinsumo_"+w).toString(), 
						adminForm.getInsumosMap("fechavencimientoloteinsumo_"+w).toString(),
						adminForm.getMedicamentosMap("unidadessaldos_"+w)+"",
						adminForm.getMedicamentosMap("adelantoxnecesidad_"+w).toString(),
						adminForm.getMedicamentosMap("nadaviaoral_"+w).toString(),
						adminForm.getMedicamentosMap("usuariorechazo_"+w).toString())<=0)
    			{
    				logger.info("NO inserto el detalle de la admin insumo="+adminForm.getInsumosMap("codigo_"+w)+" ");
    				ActionErrors errores = new ActionErrors();
    				errores.add("", new ActionMessage("error.errorEnBlanco", "El despacho es menor a la cantidad a administrar del insumo "+adminForm.getInsumosMap("codigo_"+w)));
    				saveErrors(request, errores);	
    				//LA CONEXION SE ABORTA Y CIERRA EN EL LLAMADO DE ESTE METODO									
    				return mapping.findForward("detalleSolicitud");
    			}
    		}
    	}
//    	LA CONEXION SE ABORTA Y CIERRA EN EL LLAMADO DE ESTE METODO
		return null;
	}
    
    /**
     * 
     * @param medicamentosMap
     * @param acumuladoAdminDosisMap
     * @return
     */
    private int obtenerIncrementoCantidadXDosis(int indiceMapaMedicamentos, HashMap acumuladoAdminDosisMap, AdminMedicamentosForm adminForm) 
    {
    	for(int w=0; w<Integer.parseInt(acumuladoAdminDosisMap.get("numRegistros").toString());w++)
    	{
    		if(acumuladoAdminDosisMap.get("articulo_"+w).toString().equals(adminForm.getMedicamentosMap("codigo_"+indiceMapaMedicamentos).toString()))
    		{
    			int cantidad= Integer.parseInt(acumuladoAdminDosisMap.get("incrementoUnidadesAdminFarmacia_"+w).toString())
    									+Integer.parseInt(acumuladoAdminDosisMap.get("incrementoUnidadesAdminPaciente_"+w).toString());
    			
    			return cantidad;
    		}
    	}
		return 0;
	}

	/**
     * 
     * @param con
     * @param acumuladoAdminDosisMap
     * @param usuario
     * @param paciente
	 * @param adminForm 
     * @return
     */
    protected boolean generarCargoMedicamentosXArticulo(Connection con, HashMap acumuladoAdminDosisMap, UsuarioBasico usuario, PersonaBasica paciente, AdminMedicamentosForm adminForm) throws IPSException
    {
    	
    	/***PARTE DE MEDICAMENTOS****/
    	//CargoMedicamentos cargoMedicamentos= new CargoMedicamentos();
    	//se verifica si se esta finalizando un articulo sin administración, este caso puede darse para un equivalente, y si la administracion del primcipal es 0 deberia quedar en cargado.
    	//para este caso, el articulo principal si no tiene administracion, queda con un cargo generado con cantidad 0 y en estado pendiente, este se debe dejar cargado y con cantidad 0.
    	
		/**se verifican los articulos que se finalizaran y que no tienen administracion, es decir esta administracion total es 0 se les debe generar el cargo.**/
    	logger.info("\n\n\n\n");
		for(int w=0; w<(Integer.parseInt(adminForm.getMedicamentosMap("numRegistros").toString())); w++)
		{
			if(adminForm.getMedicamentosMap("finalizararticulo_"+w).toString().equals(ConstantesBD.acronimoSi))
			{
				boolean generarCargo=true;
				for(int a=0; a<Integer.parseInt(acumuladoAdminDosisMap.get("numRegistros").toString()); a++)
		    	{
					if(Integer.parseInt(acumuladoAdminDosisMap.get("articulo_"+a).toString())==Integer.parseInt(adminForm.getMedicamentosMap("codigo_"+w).toString()))
    				{
						generarCargo=false;
						a=Integer.parseInt(acumuladoAdminDosisMap.get("numRegistros").toString());
    				}
		    	}
				if(generarCargo)
				{
					Cargos cargos= new Cargos();
	    			
	    			Utilidades.imprimirMapa(acumuladoAdminDosisMap);
	    			
	    			if(!cargos.recalcularCargoArticuloXMedicamentoAdmin(con, adminForm.getNumeroSolicitud(), Integer.parseInt(adminForm.getMedicamentosMap("codigo_"+w).toString()), usuario, paciente, ""/*obnservaciones*/,""))
	    			{
	    				
	    			}
		    	}
			}
		}
    		
    	for(int w=0; w<Integer.parseInt(acumuladoAdminDosisMap.get("numRegistros").toString()); w++)
    	{
    		//if(Integer.parseInt(acumuladoAdminDosisMap.get("incrementoUnidadesAdminFarmacia_"+w).toString())>0)
    		{
    			//if(!cargoMedicamentos.generarCargoXSolicitudArticulo(con, Integer.parseInt(acumuladoAdminDosisMap.get("numeroSolicitud_"+w).toString()), Integer.parseInt(acumuladoAdminDosisMap.get("articulo_"+w).toString()), paciente, usuario))
    			Cargos cargos= new Cargos();
    			
    			Utilidades.imprimirMapa(acumuladoAdminDosisMap);
    			
    			if(!cargos.recalcularCargoArticuloXMedicamentoAdmin(con, Integer.parseInt(acumuladoAdminDosisMap.get("numeroSolicitud_"+w).toString()), Integer.parseInt(acumuladoAdminDosisMap.get("articulo_"+w).toString()), usuario, paciente, ""/*obnservaciones*/,acumuladoAdminDosisMap.get("fecha_"+w)+""))
    			{
    				return false;
    			}
    		}
    	}
    	
    	//luego se insertan los que leen las unidades de consumo de una
    	for(int w=0; w<Integer.parseInt(adminForm.getMedicamentosMap("numRegistros").toString()); w++)
    	{
    		if(adminForm.getMedicamentosMap("leerunidadesconsumidas_"+w).toString().equals(ConstantesBD.acronimoSi))
    		{
    			if(Integer.parseInt(adminForm.getMedicamentosMap("unidadesconsumidas_"+w).toString())>0
    				|| (adminForm.getMedicamentosMap("finalizararticulo_"+w).toString().equals(ConstantesBD.acronimoSi)))
    			{
    				//if(!cargoMedicamentos.generarCargoXSolicitudArticulo(con, adminForm.getNumeroSolicitud(), Integer.parseInt(adminForm.getMedicamentosMap("codigo_"+w).toString()), paciente, usuario))
    				Cargos cargos= new Cargos();
    				
    				if(!cargos.recalcularCargoArticuloXMedicamentoAdmin(con, adminForm.getNumeroSolicitud(), Integer.parseInt(adminForm.getMedicamentosMap("codigo_"+w).toString()), usuario, paciente, "" /*observaciones*/,acumuladoAdminDosisMap.get("fecha_"+w)+""))	
    				{
    					return false;
    				}
    			}
    		}
    	}
    	
    	/***PARTE DE INSUMOS****/
    	
    	
    	
    	
    	for(int w=0; w<Integer.parseInt(adminForm.getInsumosMap("numRegistros").toString()); w++)
    	{
    		if(Integer.parseInt(adminForm.getInsumosMap("consumo_"+w).toString())>0|| adminForm.getFinalizarInsumos().equals(ConstantesBD.acronimoSi))
    		{	
    			//if(!cargoMedicamentos.generarCargoXSolicitudArticulo(con, adminForm.getNumeroSolicitud(), Integer.parseInt(adminForm.getInsumosMap("codigo_"+w).toString()), paciente, usuario))
    			Cargos cargos= new Cargos();
				
				if(!cargos.recalcularCargoArticuloXMedicamentoAdmin(con, adminForm.getNumeroSolicitud(), Integer.parseInt(adminForm.getInsumosMap("codigo_"+w).toString()), usuario, paciente, "" /*observaciones*/,acumuladoAdminDosisMap.get("fecha_"+w)+""))	
				{
					return false;
				}
    		}
    	}
    	
    	return true;
    }
    
private ActionForward accionImprimirPerfilFarmacoterapia (AdminMedicamentosForm adminForm, ActionMapping mapping, HttpServletRequest request, UsuarioBasico usuario, PersonaBasica paciente, Connection con){
		
		HashMap perfilFarmacoterapia = UtilidadesHistoriaClinica.obtenerPerfilFarmacoterapia(con, paciente.getCodigoIngreso());
		Vector archivosGenerados=new Vector();
		int numArchivos=0;
		String mes="";
		
		HashMap archivosGeneradosBirt=new HashMap();
        archivosGeneradosBirt.put("numRegistros", "0");
        
        for(int  i = 0 ; i < Integer.parseInt(perfilFarmacoterapia.get("num_meses")+"") ; i++)
		{
        	// Obtenemos el mes para el cual se va a generar el reporte
        	mes = perfilFarmacoterapia.get("meses").toString().split(ConstantesBD.separadorSplit)[i];
        	
        	String nombreRptDesign = "perfilDeFarmacoterapia.rptdesign";
			
			//EstadisticasServicios mundo = new EstadisticasServicios();
			
			InstitucionBasica ins = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
			String viaIngreso="";
			String tipoPaciente="";
			Vector v;
			
			//***************** INFORMACIÓN DEL CABEZOTE
		    DesignEngineApi comp; 
		    comp = new DesignEngineApi (ParamsBirtApplication.getReportsPath()+"historiaClinica/",nombreRptDesign);
		    
		    // Logo
		    comp.insertImageHeaderOfMasterPage1(1, 2, ins.getLogoReportes());
		    
		    // Nombre Institución, titulo y rango de fechas
		    comp.insertGridHeaderOfMasterPageWithName(0,0,1,2, "titulo");
		    v=new Vector();
		    v.add(ins.getRazonSocial());
		    v.add("PERFIL DE FARMACOTERAPIA - "+mes);
		    comp.insertLabelInGridOfMasterPage(0,0,v);
		    
		    // Paciente, entidad, nro Ingreso, fecha ingreso
		    comp.insertGridHeaderOfMasterPageWithName(1,0,1,4, "info1");
		    v=new Vector();
		    v.add("Paciente: "+paciente.getNombrePersona());
		    v.add("Entidad: "+paciente.getConvenioPersonaResponsable());
		    v.add("Nro Ingreso: "+paciente.getConsecutivoIngreso());
		    v.add("Fecha Ingreso: "+paciente.getFechaIngreso());
		    comp.insertLabelInGridOfMasterPageWithProperties(1,0,v,"left");
		    
		    // Id, sexo, edad, cama
		    comp.insertGridHeaderOfMasterPageWithName(1,1,1,4, "info2");
		    v=new Vector();
		    v.add("Nro. Doc: "+paciente.getTipoIdentificacionPersona(false)+" "+paciente.getNumeroIdentificacionPersona());
		    v.add("sexo: "+paciente.getSexo());
		    v.add("Edad: "+paciente.getEdadDetallada());
		    v.add("Cama: "+paciente.getCama());
		    comp.insertLabelInGridOfMasterPageWithProperties(1,1,v,"left");
		    
		    // Fecha hora de proceso y usuario
		    comp.insertLabelInGridPpalOfFooter(0,1,"Fecha: "+UtilidadFecha.getFechaActual()+" Hora: "+UtilidadFecha.getHoraActual());
		    comp.insertLabelInGridPpalOfFooter(0,0,"Usuario: "+usuario.getLoginUsuario());
		    //******************
			
		    //***************** NUEVA CONSULTA DEL REPORTE
		    String consultaSQL = ConsultasBirt.perfilDeFarmacoterapia(paciente.getCodigoIngreso(), mes);
		   
		   //****************** MODIFICAR CONSULTA
		    comp.obtenerComponentesDataSet("dataSet");
		   String newquery=comp.obtenerQueryDataSet();
		   comp.modificarQueryDataSet(consultaSQL);
		   
		   logger.info("Query >>> "+consultaSQL);
		   
		   //debemos arreglar los alias para que funcione oracle, debido a que este los convierte a MAYUSCULAS
      	comp.lowerAliasDataSet();
		String newPathReport = comp.saveReport1(false);
		   comp.updateJDBCParameters(newPathReport);        
		   
		   if(!newPathReport.equals(""))
		    {
			   archivosGeneradosBirt.put("isOpenReport_"+i, "true");
			   archivosGeneradosBirt.put("newPathReport_"+i, newPathReport);
			   numArchivos++;
		    }
		}
        
        archivosGeneradosBirt.put("numRegistros", numArchivos);
        request.setAttribute("archivos", archivosGenerados);
	    request.setAttribute("archivosBirt", archivosGeneradosBirt);
        request.setAttribute("nombreVentana", "hola");
		UtilidadBD.closeConnection(con);		
		return mapping.findForward("abrirNPdfBirt");
	}
    
    /**
     * 
     * @param con
     * @param adminForm
     * @param usuario
     * @return
     */
    private boolean insertarFinalizacionAdminArticulo(Connection con, AdminMedicamentosForm adminForm, UsuarioBasico usuario) 
    {
    	/**PARTE DE MEDICAMENTOS**/
    	logger.info("\n\n\n\n");
		for(int w=0; w<(Integer.parseInt(adminForm.getMedicamentosMap("numRegistros").toString())); w++)
		{
			
			logger.info("finalizacion >> "+adminForm.getMedicamentosMap("finalizararticulo_"+w).toString()+" >> "+adminForm.getMedicamentosMap("descripcion_"+w).toString());
			
			if(adminForm.getMedicamentosMap("finalizararticulo_"+w).toString().equals(ConstantesBD.acronimoSi))
			{
				if(!AdminMedicamentos.insertarFinalizacionAdminArticulo(con, adminForm.getNumeroSolicitud(), Integer.parseInt(adminForm.getMedicamentosMap("codigo_"+w).toString()), usuario.getLoginUsuario()))
				{
					logger.info("error articulo-->"+adminForm.getMedicamentosMap("codigo_"+w));
					return false;
				}
			}
		}
		
		logger.info("\n\n\n\n");
		
		/**PARTE DE INSUMOS**/
		if(adminForm.getFinalizarInsumos().equals(ConstantesBD.acronimoSi))
		{	
			for(int w=0; w<(Integer.parseInt(adminForm.getInsumosMap("numRegistros").toString())); w++)
			{
				if(!AdminMedicamentos.insertarFinalizacionAdminArticulo(con, adminForm.getNumeroSolicitud(), Integer.parseInt(adminForm.getInsumosMap("codigo_"+w).toString()), usuario.getLoginUsuario()))
				{
					logger.info("error articulo-->"+adminForm.getMedicamentosMap("codigo_"+w));
					return false;
				}
			}
		}	
		return true;
	}
    
    /**
     * 
     * @param con
     * @param adminForm
     * @return
     */
    private boolean estanTodosArticulosFinalizados(AdminMedicamentosForm adminForm) 
    {
    	/****PARTE DE MEDICAMENTOS****/
		for(int w=0; w<Integer.parseInt(adminForm.getMedicamentosMap("numRegistros").toString()); w++)
		{
			if(adminForm.getMedicamentosMap("finalizararticulo_"+w).toString().equals(ConstantesBD.acronimoNo))
			{
				return false;
			}
		}
		/****PARET DE INSUMOS***/
		if(adminForm.getFinalizarInsumos().equals(ConstantesBD.acronimoNo))
		{
			return false;
		}
    	return true;
	}
    
   /**
     * 
     * @param adminForm
     * @param mundo
     */
	protected void llenarForm(AdminMedicamentosForm adminForm, AdminMedicamentos mundo)
	{
		adminForm.setNumeroSolicitud(mundo.getNumeroSolicitud());
		adminForm.setOrden(mundo.getOrden());			
		adminForm.setFechaSolicitud(UtilidadFecha.conversionFormatoFechaAAp(mundo.getFechaSolicitud()));
		adminForm.setHoraSolicitud(mundo.getHoraSolicitud());
		adminForm.setMedicoSolicitante(mundo.getMedicoSolicitante());
		//adminForm.setNumeroAutorizacion(mundo.getNumeroAutorizacion());
		adminForm.setEstadoMedico(mundo.getEstadoMedico());
		adminForm.setCodigoEstadoMedico(mundo.getCodigoEstadoMedico());
		adminForm.setObservacionesGenerales(mundo.getObservacionesGenerales());
		adminForm.setFarmaciaDespacho(mundo.getFarmaciaDespacho());
	}
	
	/**
	 * Método que implementa las validaciones generales de entrada 
	 * @param map ActionMapping
	 * @param req HttpServletRequest
	 * @return ActionForward a la paginaError si existe un acceso errado
	 */
	protected ActionForward validacionesAccesoUsuario(ActionMapping map, HttpServletRequest req)
	{
			HttpSession session=req.getSession();
			UsuarioBasico medico = (UsuarioBasico)session.getAttribute("usuarioBasico");
			String mensajeError = UtilidadValidacion.esEnfermera(medico);
			
			if(medico == null)
			{
					logger.warn("Profesional de la salud no válido (null)");			
					req.setAttribute("codigoDescripcionError", "errors.usuario.noCargado");
					return map.findForward("paginaError");
			}
			else
			if( !mensajeError.equals("") )
			{
					req.setAttribute("codigoDescripcionError", mensajeError);
					return map.findForward("paginaError");
			}		
		return null;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param adminForm
	 * @param request
	 * @param mapping
	 * @return
	 */
	protected ActionForward validacionesConcurrencia(Connection con, AdminMedicamentosForm adminForm, HttpServletRequest request, ActionMapping mapping, UsuarioBasico usuario)
	{
		//se debe validar por concurrencia que la solicitud este solicitada PENDIENTE
		Solicitud solicitud= new Solicitud();
		try 
		{
			solicitud.cargar(con, adminForm.getNumeroSolicitud());
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
    	if( (solicitud.getEstadoHistoriaClinica().getCodigo()!=ConstantesBD.codigoEstadoHCSolicitada && solicitud.getEstadoHistoriaClinica().getCodigo()!=ConstantesBD.codigoEstadoHCDespachada) )
		{
			UtilidadBD.closeConnection(con);
			logger.warn("Error en el estado de la solicitud número:"+adminForm.getNumeroSolicitud()+ " estado HC -> "+solicitud.getEstadoHistoriaClinica());
			adminForm.reset(usuario.getCodigoInstitucionInt(), usuario.getCodigoCentroAtencion(),true);
			ArrayList atributosError = new ArrayList();
			atributosError.add("Administrar");
			atributosError.add("Pendiente");
			atributosError.add("Solicitada - Despachada");
			request.setAttribute("codigoDescripcionError", "error.solicitudgeneral.estados");				
			request.setAttribute("atributosError", atributosError);
			return mapping.findForward("paginaError");
		}
    	return null;
	}
	
        /**
		 * Este método especifica las acciones a realizar en el estado
		 * seleccionMenu AdminMedicamentos
		 * @param adminForm AdminMedicamentosForm
		 * @param mapping Mapping para manejar la navegación
		 * @param con Conexión con la fuente de datos
		 * @return ActionForward  a la página "seleccionSolicitudMedicamentos.jsp"
		 * @throws SQLException
		 */
		private ActionForward accionResumenAdmin	(	AdminMedicamentosForm adminForm,
														ActionMapping mapping,
														Connection con)  
		{
			AdminMedicamentos mundo= new AdminMedicamentos();
			adminForm.setEstado("resumen");
			int tamanioMapa=0;
			try 
			{
				tamanioMapa = mundo.resumenAdmiMedicamentosPart1(con,adminForm.getNumeroSolicitud());
			} 
			catch (SQLException e) 
			{
				e.printStackTrace();
			}
			
			if(tamanioMapa>0)
			{
				int tamanio2Mapa=0;
				try 
				{
					tamanio2Mapa = mundo.resumenAdmiMedicamentosPart2(con,adminForm.getNumeroSolicitud());
				} 
				catch (SQLException e) 
				{
					e.printStackTrace();
				}
				if(tamanio2Mapa>0)
				{
					adminForm.setResumenAdminMap(mundo.getResumenAdminMap());
				}
				UtilidadBD.closeConnection(con);
				return mapping.findForward("resumenAdmin")	;
			}
			else
			{
				UtilidadBD.closeConnection(con);
				return mapping.findForward("resumenVacio");
			}
		}

		
		/**
		 * 
		 * @return
		 */
		private ActionForward accionFiltroViaIngreso(AdminMedicamentosForm adminForm,
													ActionMapping mapping,
													Connection con)
		{
			ArrayList<DtoCentroCostoViaIngreso> centrosCostoViaIngreso=new ArrayList<DtoCentroCostoViaIngreso>();
			ICentroCostoViaIngresoMundo centroCostoViaIngresoMundo=ManejoPacienteFabricaMundo.crearCentroCostoViaIngresoMundo(); 
			centrosCostoViaIngreso=centroCostoViaIngresoMundo.listarCentrosCostoXViaIngreso(adminForm.getViaIngresoFiltro());
			adminForm.setListaCentroCostosViaIngreso(centrosCostoViaIngreso);
			UtilidadBD.closeConnection(con);
			return mapping.findForward("filtroArea");
			
		}
		/**
		 * 
		 * @return
		 */
		private ActionForward accionVolverAdminMedicamentos(AdminMedicamentosForm adminForm,
													ActionMapping mapping,
													Connection con)
		{
			UtilidadBD.closeConnection(con);
			return mapping.findForward("listarSolicitudes");
			
		}
}