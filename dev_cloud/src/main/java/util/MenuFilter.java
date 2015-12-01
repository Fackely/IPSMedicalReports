/*
 * @(#)MenuFilter.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.axioma.util.log.Log4JManager;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.MenuDao;
import com.princetonsa.dto.administracion.DtoModulo;
import com.princetonsa.mundo.NewMenu;
import com.princetonsa.mundo.ObservableBD;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.facturacion.DistribucionCuenta;
import com.princetonsa.mundo.facturacion.Factura;
import com.princetonsa.mundo.historiaClinica.Valoraciones;
import com.princetonsa.mundo.odontologia.PresupuestoOdontologico;
import com.princetonsa.util.birt.reports.ParamsBirtApplication;
import com.servinte.axioma.cron.AdministradorCron;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IConsecutivosSistemaServicio;
import com.sysmedica.util.UtilidadGenArchivos;

/**
 * Este filtro procesa los datos de la pagina de login; guarda en el objeto implicito <i>session</i> el
 * java.security.Principal del usuario, sus roles y sus menus, e inicializa la BD que se va a utilizar
 *
 * @version 1.0, Sep 24, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class MenuFilter implements Filter 
{

	/**
	 * El tipo de Base de Datos usado por toda la aplicacion (e.g., Oracle, PostgreSQL, etc.).
	 */
	private static String tipoBD = "";

	
	/**
	 * Esta Hashtable contiene la lista de los modulos y sus funcionalidades
	 * correspondientes. Al construir el objeto menuElementos para subirlo a la
	 * sesion, nos apoyaremos en modulosFuncionalidades para agrupar las
	 * funcionalidades del usuario.
	 */
	@SuppressWarnings("rawtypes")
	private static final Hashtable modulosFuncionalidades = new Hashtable();

	/**
	 * Inicializa el filtro.
	 * @param filterConfig parametro de configuracion del filtro
	 */
	@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
	public void init (FilterConfig filterConfig) throws ServletException {

		// Estos valores se leen del archivo descriptor de la aplicacion, web.xml

		String driver     		= filterConfig.getInitParameter("DRIVER");
		String protocolo 		= filterConfig.getInitParameter("PROTOCOLO");
		String usuario    		= filterConfig.getInitParameter("USUARIO");
		String password   		= filterConfig.getInitParameter("PASSWORD");
		
		/*Propiedad para manejo del parser Xml en roles*/
		System.setProperty("javax.xml.transform.TransformerFactory", "com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl");
		//System.setProperty("javax.xml.transform.TransformerFactory", "org.apache.xalan.processor.TransformerFactoryImpl");
		
		/* Parámetro de servidor de correo elctrónico */
		String smtpServer = filterConfig.getInitParameter("SMTPSERVER");

		/*Parámetro de ruta raíz de la aplicación*/
		String webPath    = filterConfig.getInitParameter("WEBPATH");
        
		
		/*Párametro de el path del birt para trabajo con Java code*/
        String birtRunTime = filterConfig.getServletContext().getInitParameter("BIRTRUNTIMEPATH");
        /*Párametro de el path del birt para ubicar los reports*/
        String reportsPathBirt = filterConfig.getServletContext().getInitParameter("REPORTSPATH");
        /*Párametro de el path del birt para obtener el url de navegación*/
        String birtViewerPath = filterConfig.getServletContext().getInitParameter("BIRTVIEWERPATH");
        //Párametro de el path del birt para almacenar los designs temporales/
        String birtReportsPathTemp = filterConfig.getServletContext().getInitParameter("REPORTSPATHTEMP");
        
        /// LOG4J CONFIGURACIÓN
        
        Logger log=Logger.getAnonymousLogger();
		log.info("\n\n\n Servlet Context name -> "+filterConfig.getServletContext().getServletContextName()+" REPORTSPATH -->"+reportsPathBirt+"\n" +
        					"REPORTSPATHTEMP --> "+birtReportsPathTemp+"\n\n\n");
        
        //se llena el objeto de los parametros de inicializacion del bith
        ParamsBirtApplication.reset();
        ParamsBirtApplication.setBirtRuntimePath(birtRunTime);
        ParamsBirtApplication.setBirtViewerPath(birtViewerPath);
        ParamsBirtApplication.setDriver(driver);
        ParamsBirtApplication.setPassword(password);
        ParamsBirtApplication.setReportsPath(reportsPathBirt);
        ParamsBirtApplication.setReportsPathTemp(birtReportsPathTemp);
        ParamsBirtApplication.setUrlBD(protocolo);
        ParamsBirtApplication.setUserBD(usuario);

        // Inicio Configuración de log4j.
		String logs = filterConfig.getInitParameter("LOGSLOG4J");
		Log4JManager.setConfigFile(logs);
		Log4JManager.loadConfig(false);
		// FIN LOG4J CONFIGURACIÓN
		
		//Configuración logs aplicación
		LogsAxioma.setRutaLogs(filterConfig.getInitParameter("RUTALOGSAPLICACION"));
		
		//Configuración backUp aplicación
		BackUpBaseDatos.setRutaBackUp(filterConfig.getInitParameter("RUTABACKUPAPLICACION"));

		///Nombre del Contexto

		int maxActive = Integer.parseInt(filterConfig.getInitParameter("MAXACTIVE"));
		//byte whenExhaustedAction = Byte.parseByte(filterConfig.getInitParameter("WHENEXHAUSTEDACTION"), 10);
		//int removeAbandonedTimeout = Integer.parseInt(filterConfig.getInitParameter("REMOVEABANDONEDTIMEOUT"));
		int maxIdle = Integer.parseInt(filterConfig.getInitParameter("MAXIDLE"));
		int maxWait = Integer.parseInt(filterConfig.getInitParameter("MAXWAIT"));
		// Esta propiedad será accedida a lo largo de toda la aplicación para determinar el tipo de BD a utilizar
		tipoBD = filterConfig.getServletContext().getInitParameter("TIPOBD");
		System.setProperty("TIPOBD", tipoBD);
		
		System.setProperty("PAIS", filterConfig.getServletContext().getInitParameter("PAIS"));
		System.setProperty("FIRMADIGITAL", filterConfig.getServletContext().getInitParameter("FIRMADIGITAL"));
		String codInst=filterConfig.getServletContext().getInitParameter("CODIGOINSTITUCION");
		System.setProperty("CODIGOINSTITUCION", filterConfig.getServletContext().getInitParameter("CODIGOINSTITUCION"));
		
		// Esta propiedad será accedida a lo largo de toda la aplicacion para obtener el path de interfaz laboratorios
		String interfazLab = filterConfig.getInitParameter("RUTAINTERFAZLABORATORIOS");
		ValoresPorDefecto.setFilePathInterfazLaboratorios(interfazLab);
		

		// Elijo la BD según lo especificado en el archivo de configuración
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		myFactory.init(driver, protocolo, usuario, password, maxActive, maxIdle, maxWait);

		// Rutina para cargar los valores por defecto de la aplicación
		Connection con = null;
		try	{
			con = myFactory.getConnection();
			if(con!=null)
			{
				ValoresPorDefecto.iniciarValoresPorDefecto();
				ValoresPorDefecto.cargarValoresIniciales(con);
			}
			else
			{
				log.info("Error cargando los valores por defecto del sistema");
			}
			int facturasCanceladas=Factura.cancelarTodosLosProcesosDeFacturacion(con);
			if(facturasCanceladas>0)
			{
				log.warning("SE CANCELARON "+facturasCanceladas+" PROCESOS DE FACTURACIÓN QUE SE ENCONTRABAN PENDIENTES");
			}
			int distribucionesCanceladas=DistribucionCuenta.cancelarTodosLosProcesosDeDistribucion(con);
			if(distribucionesCanceladas>0)
			{
				log.warning("SE CANCELARON "+distribucionesCanceladas+" PROCESOS DE DISTRIBUCION QUE SE ENCONTRABAN PENDIENTES");
			}
			int pacientesEnValoracionCancelados=Valoraciones.cancelarPacientesEnEvaloracion(con);
			
			int presupuestosCancelados= PresupuestoOdontologico.cancelarTodosLosProcesosDePresupuesto(con);
			if(presupuestosCancelados>0)
			{
				log.warning("SE LIBERARON "+presupuestosCancelados+" PROCESOS DE PRESUPUESTO OSONTOLOGICO");
			}
			
			//borrar usuarios logeados
			UtilidadSesion.eliminarUsuariosSession(con);
			
			
			//ACTUALIZAMOS EL FORMATO DE FECHA
			//UtilidadFecha.actualizarFormatoFechaBD();
			
			//ACTUALIZAMOS EL NUMERO DE PROCESOS DE LA BD
			UtilidadBD.actualizarNumeroProcesosBD();
			
			// Definir los parámetros del sistema
			String filePath =  filterConfig.getInitParameter("FILEPATH");
			boolean isSeparatorLinux=System.getProperty("file.separator").equals("/");
			if(isSeparatorLinux)
				ValoresPorDefecto.setFilePath(filePath);
			else
				ValoresPorDefecto.setFilePath(filePath.replace('/','\\')+"\\");

			String filePathRAP =  filterConfig.getInitParameter("REPORTESARCHIVOPLANO");
			if(isSeparatorLinux)
				ValoresPorDefecto.setFilePathReporteArchivosPlanos(filePathRAP);
			else
				ValoresPorDefecto.setFilePathReporteArchivosPlanos(filePathRAP.replace('/','\\')+"\\");
			
			System.setProperty("ARCHIVOSPLANOS", filterConfig.getInitParameter("ARCHIVOSPLANOS"));
			
			String adjuntos = filterConfig.getInitParameter("ADJUNTOS");
			System.setProperty("ADJUNTOS", adjuntos);
			
	        //Parametro corte curvas de crecimiento y desarrollo anteriores
	        System.setProperty("FECHACORTECURVASCRECIMIENTO", filterConfig.getInitParameter("FECHACORTECURVASCRECIMIENTO"));
	        
	        //Parametro corte curvas de crecimiento y desarrollo anteriores
	        System.setProperty("HORACORTECURVASCRECIMIENTO", filterConfig.getInitParameter("HORACORTECURVASCRECIMIENTO"));
			
//			Definicion del Parametro ReportPath
			String reportPath =  filterConfig.getInitParameter("REPORTPATH");
			isSeparatorLinux=System.getProperty("file.separator").equals("/");
			if(isSeparatorLinux)
				ValoresPorDefecto.setReportPath(reportPath);
			else
				ValoresPorDefecto.setReportPath(reportPath.replace('/','\\')+"\\");

			String reportUrl = filterConfig.getInitParameter("REPORTURL");
			ValoresPorDefecto.setReportUrl(reportUrl);
			
			
			String fotosPath =  filterConfig.getInitParameter("FOTOSPATH");
			if(isSeparatorLinux)
				ValoresPorDefecto.setFotosPath(fotosPath);
			else
				ValoresPorDefecto.setFotosPath(fotosPath.replace('/','\\'));
			
			String fotosUrl = filterConfig.getInitParameter("FOTOSURL");
			System.setProperty("FOTOSURL", fotosUrl);
			
			// Fijar el servidor SMPT para el envio de correos
			System.setProperty("mail.smtp.host", smtpServer);

			//Fijar la ruta raíz de la aplicación
			System.setProperty("com.princetonsa.axioma.webPath", webPath);
	        
			String mostrarjsp=filterConfig.getInitParameter("MOSTRAR_JSP");
			if(mostrarjsp!=null)
			{
				if(mostrarjsp.equalsIgnoreCase("true") || mostrarjsp.equalsIgnoreCase("t") || mostrarjsp.equalsIgnoreCase("si"))
					ValoresPorDefecto.setMostrarNombreJSP(true);
				else if(mostrarjsp.equalsIgnoreCase("false") || mostrarjsp.equalsIgnoreCase("f") || mostrarjsp.equalsIgnoreCase("no"))
					ValoresPorDefecto.setMostrarNombreJSP(false);
				else
				{
					ValoresPorDefecto.setMostrarNombreJSP(false);
				}
			}
			else
			{
				ValoresPorDefecto.setMostrarNombreJSP(false);
			}
			
			
			String popUpNavegacion = filterConfig.getInitParameter("POPUP_NAVEGACION");
			if(popUpNavegacion!=null)
			{
				if(popUpNavegacion.equalsIgnoreCase("true") || popUpNavegacion.equalsIgnoreCase("t") || popUpNavegacion.equalsIgnoreCase("si"))
					ValoresPorDefecto.setPopUpNavegacion(true);
				else if(popUpNavegacion.equalsIgnoreCase("false") || popUpNavegacion.equalsIgnoreCase("f") || popUpNavegacion.equalsIgnoreCase("no"))
					ValoresPorDefecto.setPopUpNavegacion(false);
				else
				{
					ValoresPorDefecto.setPopUpNavegacion(false);
				}
			}
			else
			{
				ValoresPorDefecto.setPopUpNavegacion(false);
			}
			
			//Otra de las cosas que definiremos en este punto es el directorio de imagenes
			String directorioImagenes = filterConfig.getServletContext().getInitParameter("IMAGENES");
			String axiomaBase = filterConfig.getInitParameter("BASE_AXIOMA");
			if(isSeparatorLinux)
			{
				ValoresPorDefecto.setDirectorioImagenes(axiomaBase+directorioImagenes.replaceFirst("..",""));
			}
			else
			{
				ValoresPorDefecto.setDirectorioImagenes(axiomaBase+directorioImagenes.replaceFirst("..","").replace('/','\\'));
			}
			
			ValoresPorDefecto.setDirectorioAxiomaBase(axiomaBase);
			/*
			 * @todo (Ver util.MenuFilter) La idea es que el directorio imagenes no se maneje como propiedad del sistema.
			 * Este cambio se debe hacer en todo lado
			 * así que por eso no he quitado la propiedad.
			 * La forma correcta de hacerlo es utilizar "ValoresPorDefecto.getDirectoriImagenes()"
			 */
			System.setProperty("directorioImagenes", directorioImagenes);
			
			if(filterConfig.getInitParameter("tiempoSesion")==null)
			{
				log.info("No se encuentra definido el tiempo de la sesión, favor revisar su web.xml (tiempoSesion)");
				System.exit(1);
			}
			
			System.setProperty("tiempoSesion", filterConfig.getInitParameter("tiempoSesion"));
			
		
			
			//Ahora inicializamos la Hashtable de modulosFuncionalidades
			HashMap modulosMap=myFactory.getMenuDao().obtenerMenus();
			
			for(int i=0;i<Integer.parseInt(modulosMap.get("numRegistros")+"");i++)
				modulosFuncionalidades.put(modulosMap.get("func_"+i),modulosMap.get("modulo_"+i));
			
			
			//Se comenta este código porque ya no se utiliza 02/02/2012
//			Constants constants = new Constants();
//			Constants.init();
//			filterConfig.getServletContext().setAttribute("constants",constants);

			/*
			 * Las siguientes líneas son las encargadas de registrar un Observable en el contexto de
			 * application, que va a notificar a los objetos UsuarioBasico y PersonaBasica cuando haya
			 * cambios en alguno de sus datos.
			 */

			if (filterConfig.getServletContext().getAttribute("observable") == null) {
				filterConfig.getServletContext().setAttribute("observable", new ObservableBD());
			}

			// Fijar Ruta para archivos epidemiologia
			UtilidadGenArchivos.setRutaEpidemiologia(filterConfig.getServletContext().getInitParameter("RUTAEPIDEMIOLOGIA"));
			
			//SE TOMA EL CLIENTE
			ValoresPorDefecto.setCliente(filterConfig.getServletContext().getInitParameter("CLIENTE"));
			
			System.setProperty(ConstantesBD.nombreSistema, filterConfig.getServletContext().getInitParameter("NOMBRESISTEMA"));

			/*IConsecutivosSistemaServicio consecutivosDisponiblesServicio = AdministracionFabricaServicio.crearConsecutivosDisponiblesServicio();
			UtilidadTransaccion.getTransaccion().begin();
			consecutivosDisponiblesServicio.inicializarConsecutivosDisponibles();
			UtilidadTransaccion.getTransaccion().commit();*/
			
			//AdministradorCron.iniciarProcesos(Utilidades.convertirAEntero(codInst));
			
		} catch (SQLException e) {
			Log4JManager.error("ERROR OBTENIENDO LA CONEXIÓN POR FAVOR VERIFIQUE LOS PARÁMETROS DE CONEXIÓN CON LA BD", e);
		} catch (Exception e2) {
			Log4JManager.error("ERROR INICIALIZANDO SERVICIOS", e2);
		} finally {
			try {
				UtilidadBD.cerrarConexion(con);
			} catch (SQLException sqle) {
				Log4JManager.error("ERROR CERRANDO LA CONEXIÓN", sqle);
			}
		}
		
	}

	/**
	 * Intercepta un request dirigido al menu principal de la aplicacion, y se asegura que este reciba
	 * los datos de roles y archivos con menus necesarios para generar dinamicamente el menu de cada usuario
	 * con base en sus roles.
	 * @param req <i>request</i> proveniente de la pagina de login
	 * @param resp <i>response</i> dirigida a la pagina de menu principal
	 * @param filterChain cadena de filtros, manejada por el <i>web container</i>.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void doFilter (ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {

		HttpSession session = ((HttpServletRequest)req).getSession();
		UsuarioBasico usuario = (UsuarioBasico) session.getAttribute("usuarioBasico");
		String principal = null;
//		if (((HttpServletRequest)req).getUserPrincipal()!=null)
		if(usuario!=null)
		{

			// Nótese que "req" es un HttpServletRequest, luego puedo hacer un cast
	
			principal=usuario.getLoginUsuario();
			Hashtable elementosMenu = (Hashtable) session.getAttribute("elementosMenu");
			PersonaBasica pacienteActivo = (PersonaBasica) session.getAttribute("pacienteActivo");
			UtilidadSesion estadoSesion= (UtilidadSesion)session.getAttribute("estadoSesion");
			
			
			if (session.getMaxInactiveInterval()<3600)
			{
				//Si no tiene al menos una hora de inactividad, la establecemos..
				session.setMaxInactiveInterval(3600);
			}
			try 
			{
	
				// Obtengo la conexión que voy a utilizar durante el proceso de filtrado
				DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
				Connection con = null;
				
/*				if(usuario==null)
				{
					if (con == null || con.isClosed()) 
						con = myFactory.getConnection();
					
					Logger log=Logger.getAnonymousLogger();
					log.info("usuario logueado: "+principal+new Date());
					log.info("Logueado desde "+req.getRemoteAddr()+" "+req.getRemoteHost());
	
					// Almaceno el login del usuario de la aplicacion en el objeto session
					usuario = new UsuarioBasico();
					usuario.cargarUsuarioBasico(con, principal);
					
					if (usuario.getEsSoloPaciente())
					{
						//pacienteActivo.car
						// Código necesario para notificar a todos los observadores que el ingreso/cuenta/admisión del paciente en sesión pudo haber cambiado
						TipoNumeroId identificacion = new TipoNumeroId(usuario.getCodigoTipoIdentificacion(), usuario.getNumeroIdentificacion());
						if (pacienteActivo==null)
						{
							pacienteActivo=new PersonaBasica();
						}
						pacienteActivo.cargar(con, identificacion);
						pacienteActivo.cargarPaciente2(con, identificacion, usuario.getCodigoInstitucion(),usuario.getCodigoCentroAtencion()+"");
						// Código necesario para registrar este paciente como Observer
						Observable observable = (Observable) session.getServletContext().getAttribute("observable");
						if (observable != null) {
							pacienteActivo.setObservable(observable);
							// Si ya lo habíamos añadido, la siguiente línea no hace nada
							observable.addObserver(pacienteActivo);
						}
						session.setAttribute("pacienteActivo", pacienteActivo);
					}
						
					
					session.setAttribute("usuarioBasico", usuario);
					session.setAttribute("loginUsuario", usuario.getLoginUsuario());
	
					int codigoSession=UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_login_usuarios");
					session.setAttribute("codigoSession", codigoSession);
	
					UtilidadSesion.insertarUsuarioSession(con,codigoSession,usuario.getLoginUsuario(),session.getId());
				}*/
				
				
				//Ahora vamos a ver si ya se definió la propiedad
				//flagCentinela
				if (System.getProperty("flagCentinela")==null)
				{
					if (con == null || con.isClosed()) 
					{
						con = myFactory.getConnection();
					}
					System.setProperty("flagCentinela", UtilidadValidacion.obtenerValorCentinela());
					if (con != null && !con.isClosed()) 
					{
	                    UtilidadBD.closeConnection(con);
	
					}
				}
	
				if (elementosMenu == null || elementosMenu.size() < 1) {
	
					if (con == null || con.isClosed()) {
						con = myFactory.getConnection();
					}
	
					elementosMenu = new Hashtable();
	
					// Obtengo el DAO que encapsula las operaciones de BD de este objeto
					MenuDao menuDao = myFactory.getMenuDao();
	
					
					// newMenu
					ArrayList<DtoModulo> newMenu = NewMenu.cargar(con, principal);
					session.setAttribute("newMenu", newMenu);
					session.setAttribute("JSCookMenu", NewMenu.crearJSCookMenu(newMenu));
					
					
					
					// Las funcionalidades del usuario estan en la BD
					ArrayList<HashMap<String, String>> resultado = menuDao.obtenerRoles(con, principal);
	
					for (HashMap<String, String> hashMap : resultado) {
						//Ahora separar las funcionalidades e insertarlas en objetos ModuloMenu, luego insertarlos en elementosMenu
						// Traer la funcionalidad
						FuncionalidadMenu func = new FuncionalidadMenu(hashMap.get("nombreFuncionalidad"), hashMap.get("archivoFuncionalidad"), hashMap.get("codigoFuncionalidad"));
						// Buscar el modulo donde pertenece segun hashtable
						String mod = (String)modulosFuncionalidades.get(func.getCodigoFuncionalidad());
	
						if (mod == null) { // no está en la lista de modulosFuncionalidades
							//OJO aca debemos poner un log de log4j
						}
						else {
							// Ver si este modulo ya esta en elementosMenu
							if(!elementosMenu.containsKey(mod)){
								// No: crear un ModuloMenu nuevo en elementosMenu con este nombre
								elementosMenu.put(mod, new ModuloMenu(mod));
							}
							// Cuarto ingresar funcionalidad al vector de funcionalidades de este ModuloMenu dentro de elementosMenu
							ModuloMenu modulo = (ModuloMenu)elementosMenu.get(mod);
							modulo.funcionalidades.add(func);
						}
					}
					
					// Almaceno los elementos del menu, es decir modulos y funcionalidades, en el objeto session
					session.setAttribute("elementosMenu", elementosMenu);
				}
				if (con != null && !con.isClosed()) {
	                UtilidadBD.closeConnection(con);
	
				}
	
				if (estadoSesion == null) {
					estadoSesion = new UtilidadSesion();
					session.setAttribute("estadoSesion", estadoSesion);
				}
	
				/*
				 * Por último ponemos un objeto de tipo PersonaBasica en memoria (Manejo del paciente actual),
				 * que aunque no tiene nada, es necesario al momento de la presentación (para que el JSP no
				 * se tenga que preocupar de nulos y demás).
				 */
				if (pacienteActivo == null) {
	
					pacienteActivo = new PersonaBasica();
	
					pacienteActivo.setAnioAdmision(0);
					pacienteActivo.setCodigoAdmision(0);
					pacienteActivo.setCodigoCuenta(0);
					pacienteActivo.setCodigoIngreso(0);
					pacienteActivo.setCodigoSexo(0);
					pacienteActivo.setCodigoTipoIdentificacionPersona("");
					pacienteActivo.setCodigoTipoRegimen(' ');
					pacienteActivo.setCodigoUltimaViaIngreso(0);
					pacienteActivo.setConvenioPersonaResponsable("");
					pacienteActivo.setEdad(0);
					pacienteActivo.setNombrePersona("No se ha cargado paciente");
					pacienteActivo.setNumeroIdentificacionPersona("");
					pacienteActivo.setTipoIdentificacionPersona("");
					pacienteActivo.setUltimaViaIngreso("");
	
					session.setAttribute("pacienteActivo", pacienteActivo);
	
				}
	
			}	catch (SQLException sqle) {
					// Lanzo excepciones SQL usando ServletException
					throw new ServletException("Excepcion SQL en MenuFilter", sqle);
			}

		}
		
		filterChain.doFilter(req, res);
	}

	/**
	 * Metodo para realizar labores de limpieza del filtro al terminar la ejecucion de la aplicacion web.
	 */
	public void destroy ()
	{
		System.setProperty("TIPOBD", "");
		System.setProperty("directorioImagenes", "");
		String codInst=System.getProperty("CODIGOINSTITUCION");
		if(codInst != null && !codInst.isEmpty()){
			AdministradorCron.finalizarProcesos(Integer.valueOf(codInst));
		}
	}

}