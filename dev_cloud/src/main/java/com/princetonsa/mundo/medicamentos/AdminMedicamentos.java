/*
 * @(#)AdminMedicamentos.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_01
 *
 */
package com.princetonsa.mundo.medicamentos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.dao.AdminMedicamentosDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.DevolucionAFarmaciaDao;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.enfermeria.DtoAdministracionMedicamentosBasico;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Clase para el manejo de admin de medicamentos
 * 
 * @version 1.0, Sept 16, 2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan López </a>
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos </a>
 */
public class AdminMedicamentos {
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static AdminMedicamentosDao adminDao;

	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(AdminMedicamentos.class);

	/**
	 * Número de la solicitud
	 */
	private int numeroSolicitud;

	/**
	 * Fecha de la solicitud
	 */
	private String fechaSolicitud;

	/**
	 * Hora de solicitud
	 */
	private String horaSolicitud;

	/**
	 * medico que solicita
	 */
	private String medicoSolicitante;

	/**
	 * número de autorización
	 */
	//private String numeroAutorizacion;

	/**
	 * 
	 */
	private int codigoEstadoMedico;
	
	/**
	 * Estado Médico de la solicitud
	 */
	private String estadoMedico;

	/**
	 * Observaciones Generales de la solicitud
	 */
	private String observacionesGenerales;

	/**
	 * 
	 */
	private int farmaciaDespacho;
	
	/**
	 * Código del consecutivo de orden médica
	 */
	private int orden;
	
	/**
	 * mapa utilizado para el RESUMEN 
	 */
	@SuppressWarnings("rawtypes")
	private HashMap resumenAdminMap;
	
	/**
	 * resetea los datos pertinentes
	 */
	@SuppressWarnings("rawtypes")
	public void reset() 
	{
		this.numeroSolicitud = 0;
		this.fechaSolicitud = "";
		this.horaSolicitud = "";
		this.medicoSolicitante = "";
		//this.numeroAutorizacion = "";
		this.codigoEstadoMedico=0;
		this.estadoMedico = "";
		this.observacionesGenerales = "";
		this.farmaciaDespacho=0;
		this.orden=0;
		this.resumenAdminMap=new HashMap();
	}

	/**
	 * Constructor de la clase, inicializa en vacio todos los parámetros
	 */
	public AdminMedicamentos() 
	{
		reset();
		this.init(System.getProperty("TIPOBD"));
	}

	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su
	 * respectivo DAO.
	 * 
	 * @param tipoBD
	 *                  el tipo de base de datos que va a usar este objeto (e.g.,
	 *                  Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD son
	 *                  los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true </b> si la inicialización fue exitosa, <code>false</code>
	 *             si no.
	 */
	public boolean init(String tipoBD) 
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

		if (myFactory != null) {
			adminDao = myFactory.getAdminMedicamentosDao();
			wasInited = (adminDao != null);
		}
		return wasInited;
	}

	/**
	 * Método que obtiene todos los resultados de las solicitudes de
	 * medicamentos filtradas decuerdo al codigo del paciente para mostrarlos en
	 * el listado
	 * 
	 * @param con
	 * @param institucion 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection listadoSolMedXPaciente(Connection con, int codigoPaciente, int institucion) 
	{
		Collection coleccion = null;
		try 
		{
			coleccion = adminDao.listadoSolMedXPaciente(con, codigoPaciente,institucion);
			java.util.Iterator it=coleccion.iterator();
			while(it.hasNext())
			{
				HashMap map=(HashMap) it.next();
				map.put("color",adminDao.consultarColorTriage(con, Utilidades.convertirAEntero(map.get("codigocuenta").toString())));
			}
		} 
		catch (Exception e) 
		{
			logger.warn("Error mundo admin medicamentos " + e.toString());
			coleccion = null;
		}
		return coleccion;
	}

	/**
	 * Método que obtiene todos los resultados de las solicitudes de
	 * medicamentos filtradas decuerdo al AREA para mostrarlos en el listado
	 * 
	 * @param con
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Collection listadoSolMedXArea(Connection con, int areaFiltro, int codigoIntitucion, int pisoFiltro, int habitacionFiltro, int camaFiltro, String fechaInicialFiltro, String fechaFinalFiltro,int viaIngresoFiltro) 
	{
		adminDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAdminMedicamentosDao();
		HashMap mapa= new HashMap<String, Object>();		
		mapa.put("areaFiltro", areaFiltro);
		mapa.put("codigoInstitucion", codigoIntitucion);
		mapa.put("pisoFiltro", pisoFiltro);
		mapa.put("habitacionFiltro", habitacionFiltro);
		mapa.put("camaFiltro", camaFiltro);
		mapa.put("fechaInicialFiltro", fechaInicialFiltro);
		mapa.put("fechaFinalFiltro", fechaFinalFiltro);
		mapa.put("viaIngresoFiltro", viaIngresoFiltro);
		
		Collection coleccion = null;
		try 
		{
			coleccion = adminDao.listadoSolMedXArea(con, mapa);
			java.util.Iterator it=coleccion.iterator();
			while(it.hasNext())
			{
				HashMap map=(HashMap) it.next();
				map.put("color",adminDao.consultarColorTriage(con, Utilidades.convertirAEntero(map.get("codigocuenta").toString())));
			}
		} 
		catch (Exception e) 
		{
			logger.warn("Error mundo admin medicamentos " + e.toString());
			coleccion = null;
		}
		return coleccion;
	}

	/**
	 * Método para cargar los datos básicos de la solicitud
	 * 
	 * @param con
	 *                  conexión
	 * @param numeroSolicitud
	 * @return @throws
	 *             SQLException
	 */
	public boolean encabezadoSolicitudMedicamentos(Connection con, int numeroSolicitud) throws SQLException
	{
		ResultSetDecorator rs = adminDao.encabezadoSolicitudMedicamentos(con, numeroSolicitud);
		if (rs.next()) 
		{
			this.numeroSolicitud = rs.getInt("numeroSolicitud");
			this.orden= rs.getInt("orden");			
			this.fechaSolicitud = rs.getString("fechaSolicitud");
			this.horaSolicitud = rs.getString("horaSolicitud");
			this.codigoEstadoMedico= rs.getInt("codigoestadomedico");
			this.medicoSolicitante = rs.getString("medicoSolicitante");
			//this.numeroAutorizacion = rs.getString("numeroAutorizacion");
			this.estadoMedico = rs.getString("estadoMedico");
			this.observacionesGenerales = rs.getString("observacionesGenerales");
			this.farmaciaDespacho= rs.getInt("farmaciaDespacho");
			return true;
		} 
		else
			return false;
	}
	
	/**
	 * Carga el Listado de las solicitudes de medicamentos para un numeroSolicitud determinado
	 * @param con, Connection, conexión abierta con una fuente de datos
	 * @param numeroSolicitud
	 * @param codigoInstitucion Código de la institución del ususario
	 * @return 
	 */
	@SuppressWarnings("rawtypes")
	public HashMap listadoMedicamentos(Connection con, int numeroSolicitud, int codigoInstitucion, boolean mostrarFinalizados)
	{
		return adminDao.listadoMedicamentos(con, numeroSolicitud, codigoInstitucion, mostrarFinalizados);
	}

	/**
	 * Metodo que obtiene todos los registros de insumos.
	 * 
	 * @param con Connection conexion con la fuente de datos.
	 * @param numeroSolicitud,listadoInsumos
	 *                  codigo del n&uacute;mero de la solicitud.
	 * @return Collection.
	 */
	@SuppressWarnings("rawtypes")
	public HashMap listadoInsumos(Connection con, int numeroSolicitud, boolean mostrarFinalizados) 
	{
		return adminDao.listadoInsumos(con, numeroSolicitud, mostrarFinalizados);
	}

	/**
	 * 
	 * @param numeroSolicitud
	 * @param codigoArticulo
	 * @return
	 */
	public static int obtenerNumeroDosisAdministradas(int numeroSolicitud, int codigoArticulo)
	{
		int nro=0;
		Connection con= UtilidadBD.abrirConexion();
		nro=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAdminMedicamentosDao().obtenerNumeroDosisAdministradas(con, numeroSolicitud, codigoArticulo);
		UtilidadBD.closeConnection(con);
		return nro;
	}
	
	/**
	 * Metodo empleado para insertar los datos de una Administraci&oacute;n de
	 * Medicamentos.
	 * @param con, Connection con la fuente de datos.
	 * @param numeroSolicitud, codigo del n&uacute;mero de Solicitud.
	 * @param centroCosto, Centro de costo.
	 * @param usuario, Usuario
	 * @return int 1 efectivo, 0 de lo contrario.
	 */
	public int insertarAdmin(Connection con,int numeroSolicitud, int centroCosto, String usuario)
	{
		return adminDao.insertarAdmin(con, numeroSolicitud, centroCosto, usuario);
	}
	
	
	/**
	 * Metodo para insertar los datos detalle administraci&oacute;n
	 * en la tabla de <code>detalle_admin</code>.
	 * @param con Connection conexion con la fuente de datos.
	 * @param unidadesDosisSaldos Unidades completas que se administraron de saldos de otros pacientes.
	 * @param articulo, codigo del articulo.
	 * @param administracion, codigo de la administraci&oacute;n al que pertenece el detalle.
	 * @param fecha, fecha 
	 * @param hora, hora
	 * @param cantidad, consumo realizado por parte de la enfermeria.
	 * @param observaciones, observaciones.
	 * @param esTraidoUsuario, boolean (true) traido por el usuario, false de lo contrario.
	 * @return int 1 efectivo, 0 de lo contrario.
	 */ 
	public int insertarDetalleAdmin(Connection con,
									int articulo, int artppal, int administracion, 
									String fecha, String hora, int cantidad, 
									String observaciones, String esTraidoUsuario,
									String lote, String fechaVencimiento, String unidadesDosisSaldos,
									String adelantoXNecesidad, String nadaViaOral, String usuarioRechazo)
	{
		return adminDao.insertarDetalleAdmin(con, articulo, artppal, administracion, fecha, hora, cantidad, observaciones, esTraidoUsuario, lote, fechaVencimiento,unidadesDosisSaldos, adelantoXNecesidad, nadaViaOral, usuarioRechazo);
	}
	
	/**
	 * 
	 * @param con
	 * @param acumuladoAdminDosisMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean insertarActualizarAcumuladoAdminDosis(Connection con, HashMap acumuladoAdminDosisMap)
	{
		return adminDao.insertarActualizarAcumuladoAdminDosis(con, acumuladoAdminDosisMap);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param articulo
	 * @param loginUsuario
	 * @return
	 */
	public static boolean insertarFinalizacionAdminArticulo(Connection con, int numeroSolicitud, int articulo, String loginUsuario)
	{
		return adminDao.insertarFinalizacionAdminArticulo(con, numeroSolicitud, articulo, loginUsuario);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param devolcionesAFarmaciaMap
	 * @param observacionesDevolucion
	 * @param numeroSolicitud
	 * @param usuario
	 * @param farmaciaDespacho
	 * @param motivoDevolucion
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean generarDevolucionAutomatica(	Connection con,
														HashMap devolucionesAFarmaciaMap,
														String observacionesDevolucion,
														int numeroSolicitud,
														UsuarioBasico usuario,
														int farmaciaDespacho,
														String motivoDevolucion)
	{
		//para que no saque warning
		DevolucionAFarmaciaDao devolucionDao=null;
		boolean wasInited = false;
		DaoFactory myFactory    = DaoFactory.getDaoFactory( (String)System.getProperty("TIPOBD") );
		if (myFactory != null) 
		{
			devolucionDao = myFactory.getDevolucionAFarmaciaDao();
			wasInited = (adminDao != null);
		}
		if(wasInited)
		{
			int codigoUltimaSequenceDevolucion=devolucionDao.insertarDevolucionMedicamentos(	con,observacionesDevolucion,
																								UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),
																								UtilidadFecha.getHoraActual(), 
																								usuario.getLoginUsuario(), 
																								ConstantesBD.codigoEstadoDevolucionGenerada,
																								usuario.getCodigoCentroCosto(), 
																								farmaciaDespacho,
																								ConstantesBD.codigoTipoDevolucionAutomatica,
                                                                                                motivoDevolucion,
                                                                                                usuario.getCodigoInstitucionInt()
                                                                                                );
			if(codigoUltimaSequenceDevolucion<1)
				return false;
			else
			{
				int respuesta=0;
				for(int i=0; i<Integer.parseInt(devolucionesAFarmaciaMap.get("numRegistros").toString());i++)
				{
					respuesta= devolucionDao.insertarDetalleDevolucionMedicamentos(	con, 
																					codigoUltimaSequenceDevolucion, 
																					numeroSolicitud, 
																					Integer.parseInt(devolucionesAFarmaciaMap.get("codigoArtMedDevolucion_"+i)+""), 
																					Integer.parseInt(devolucionesAFarmaciaMap.get("cantidadMedDevolver_"+i)+""),
																					devolucionesAFarmaciaMap.get("lote_"+i).toString(),
																					devolucionesAFarmaciaMap.get("fechaVencimientoLote_"+i).toString()
																					);
					
				 	if(respuesta<1)
				 		return false;
				}//for
			}//else
			return true;
		}//if	
		else
			return false;
	}
	
	
	
	
	/**
	 * Se separo el metodo para evitar conflictos con el manajo de mapas con la funcionalidad administracion de medicamentos.
	 * Método para cargar los datos pertinentes al resumen de la primera parte,
	 * donde se carga la info básica del articulo y las cantidades de
	 * (Farmacia-Paciente-Total-Despacho)
	 * 
	 * @param con
	 *                  conexión
	 * @param numeroSolicitud
	 * @return @throws
	 *             SQLException
	 */
	public int resumenAdmiMedicamentosPart1(Connection con, int numeroSolicitud)throws SQLException 
	{
		ResultSetDecorator rs = adminDao.resumenAdmiMedicamentos(con, numeroSolicitud,true);
		int i = 0;
		while (rs.next()) 
		{
			this.setResumenAdminMap("resumenCodigoArt_" + i, rs.getInt("codigo") + "");
			this.setResumenAdminMap("resumenArt_" + i, rs.getInt("codigo") + " "
					+ rs.getString("descripcion") + ", Conc: "
					+ rs.getString("concentracion") + ", FF: "
					+ rs.getString("formaFarmaceutica") + " , UM: "
					+ rs.getString("unidadMedida"));
			this.setResumenAdminMap("RAdosis_"+ i, rs.getString("dosis"));
			this.setResumenAdminMap("RAfrecuencia_"+i,rs.getString("frecuencia"));
			this.setResumenAdminMap("RAvia_"+i,rs.getString("via"));
			this.setResumenAdminMap("resumenDespachoTotal_" + i, rs.getString("despachoTotal"));
			this.setResumenAdminMap("resumenAdminFarmacia_" + i, rs.getString("totalAdministradoFarmacia"));
			this.setResumenAdminMap("resumenAdminPaciente_" + i, rs.getString("totalAdministradoPaciente"));
			this.setResumenAdminMap("resumenTotalAdmin_" + i, rs.getString("totalAdministrado"));
			this.setResumenAdminMap("cantidadunidosis_" + i, rs.getObject("cantidadunidosis"));
			this.setResumenAdminMap("unidadmedidaunidosis_" + i, rs.getObject("unidadmedidaunidosis"));
			this.setResumenAdminMap("esmultidosis_" + i, rs.getObject("esmultidosis"));
			this.setResumenAdminMap("totaldespachosaldos_" + i, rs.getObject("totaldespachosaldos"));
			this.setResumenAdminMap("adelanto_x_necesidad_" + i, rs.getObject("adelanto_x_necesidad"));
			this.setResumenAdminMap("nada_via_oral_" + i, rs.getObject("nada_via_oral"));
			this.setResumenAdminMap("usuario_rechazo_" + i, rs.getObject("usuario_rechazo"));
			i++;
		}
		this.setResumenAdminMap("numRegistrosPart1", i+"");
		return i;
	}
	
	
	/**
	 * Método para cargar los datos pertinentes al resumen de la parte 2, donde
	 * se carga la info de los articulos.
	 * 
	 * @param con
	 *                  conexión
	 * @param numeroSolicitud
	 * @return @throws
	 *             SQLException
	 */
	public int resumenAdmiMedicamentosPart2(Connection con, int numeroSolicitud)throws SQLException
	{
		ResultSetDecorator rs = adminDao.resumenAdmiMedicamentos(con, numeroSolicitud,false);
		int i = 0;
		while (rs.next()) 
		{
			this.setResumenAdminMap("codigoArt_" + i, rs.getInt("articulo") + "");
			this.setResumenAdminMap("cantidadArt_" + i, rs.getInt("cantidad") + "");
			this.setResumenAdminMap("fechaHoraAdminArt_" + i, rs.getString("fechaAdministracion")+ " - " + rs.getString("horaAdministracion"));
			this.setResumenAdminMap("observacionesArt_" + i, rs.getString("observaciones"));
			this.setResumenAdminMap("usuarioFechaHoraArt_" + i, rs.getString("usuario")
					+ " - "
					+ rs.getString("fechaGrabacion")
					+ " - "
					+ rs.getString("horaGrabacion"));
			this.setResumenAdminMap("traidoPacienteArt_" + i, rs.getString("traidoPaciente"));
			this.setResumenAdminMap("adelantoXNecesidad_" + i, rs.getObject("adelanto_x_necesidad"));
			this.setResumenAdminMap("nadaViaOral_" + i, rs.getObject("nada_via_oral"));
			this.setResumenAdminMap("usuarioRechazo_" + i, rs.getObject("usuario_rechazo"));
			i++;
		}
		this.setResumenAdminMap("numRegistrosPart2", i+"");
		return i;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String fechaUltimaAdministracion(Connection con, int numeroSolicitud)
	{
		return adminDao.fechaUtlimaAdministracion(con, numeroSolicitud);
	}
	
	/**
	 * Método para consultar los medicamentos despachados para las
	 * diferentes solicitudes que pueda tener un paciente
	 * @param con
	 * @param codigoCuenta
	 * @param nroSolicitudes 
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Collection consultaMedicamentosDespachadosPaciente(Connection con, int codigoCuenta, Vector nroSolicitudes)
	{
		adminDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"))	.getAdminMedicamentosDao();
		Collection coleccion = null;
		try 
		{
			coleccion = adminDao.consultaMedicamentosDespachadosPaciente(con, codigoCuenta, nroSolicitudes);
		}
		catch (Exception e) 
		{
			logger.warn("Error mundo  consultaMedicamentosDespachadosPaciente" + e.toString());
			coleccion = null;
		}
		return coleccion;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Método para cargar los datos pertinentes para mostrar en la impresión,
	 * debido a que se definió una nueva estructura en el pdf y no se pudo reutilizar los datos cargados
	 * en el mapa con antelación. OJO se reutilizara el mapa de devolucion para no hacer una nueva declaracion
	 * 
	 * @param con
	 *                  conexión
	 * @param numeroSolicitud
	 * @return @throws
	 *             SQLException
	 */
	/*public int impresionAdmiMedicamentosPart1(Connection con, int numeroSolicitud)
			throws SQLException {
		ResultSetDecorator rs = adminDao.resumenAdmiMedicamentos(con, numeroSolicitud,
				true);
		this.devolucionMap.clear();
		
		int i = 0;

		while (rs.next()) {
		    
			this.setDevolucionMap("impresionCodigoArt_" + i, rs.getInt("codigo") + "");
			this.setDevolucionMap("impresionDescripcion_"+i, rs.getString("descripcion"));
			this.setDevolucionMap("impresionConcentracion_"+i, rs.getString("concentracion"));
			this.setDevolucionMap("impresionFormaFarmaceutica_"+i, rs.getString("formaFarmaceutica"));
			this.setDevolucionMap("impresionUnidadMedida_"+i, rs.getString("unidadMedida"));
			this.setDevolucionMap("impresionDosis_"+i, rs.getString("dosis"));
			this.setDevolucionMap("impresionFrecuencia_"+i, rs.getString("frecuencia")+" "+rs.getString("tipoFrecuencia"));
			this.setDevolucionMap("impresionVia_"+i, rs.getString("via"));
			this.setDevolucionMap("impresionCantidad_"+i, rs.getInt("cantidad")+"");
			this.setDevolucionMap("impresionObservaciones_"+i, rs.getString("observaciones"));
			this.setDevolucionMap("impresionDespachoTotal_" + i, rs
					.getString("despachoTotal"));
			this.setDevolucionMap("impresionAdminFarmacia_" + i, rs
					.getString("totalAdministradoFarmacia"));
			this.setDevolucionMap("impresionAdminPaciente_" + i, rs
					.getString("totalAdministradoPaciente"));
			this.setDevolucionMap("impresionTotalAdmin_" + i, rs
					.getString("totalAdministrado"));
			i++;
		}
		return i;
	}*/

	
	
	
	/**
	 * Método que obtiene todos los resultados de las solicitudes internas de
	 * medicamentos filtradas decuerdo al centro de costo del usuario para
	 * mostrarlos en el listado
	 * @param con
	 * @param codigoInstitucion
	 * 
	 * @return
	 */
	/*public Collection listadoSolicitudesMedicamentos(Connection con,
			int numeroSolicitud, int codigoInstitucion) {
		adminDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"))
				.getAdminMedicamentosDao();
		Collection coleccion = null;
		try {
			coleccion = UtilidadBD
					.resultSet2Collection(adminDao
							.listadoSolicitudesPorNumeroSolicitud(con,
									numeroSolicitud, codigoInstitucion));
		} catch (Exception e) {
			logger.warn("Error mundo despacho medicamentos " + e.toString());
			coleccion = null;
		}
		return coleccion;
	}*/

	
	




	
		
	
	
	
	
	
	/**
	 * Método para consultar los medicamentos despachados para las
	 * diferentes solicitudes que pueda tener un paciente
	 * @param con
	 * @param codigoCuenta
	 * @param nroSolicitudes 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Collection consultaMedicamentosDespachadosPacienteEstatico(String numeroSolicitud)
	{
		Connection con=UtilidadBD.abrirConexion();
		Vector sol=new Vector();
		sol.add(numeroSolicitud);
		adminDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"))	.getAdminMedicamentosDao();
		Collection coleccion = null;
		try 
		{
			coleccion = adminDao.consultaMedicamentosDespachadosPaciente(con, ConstantesBD.codigoNuncaValido, sol);
		}
		catch (Exception e) 
		{
		e.printStackTrace();
		coleccion = null;
		}
		UtilidadBD.closeConnection(con);
		return coleccion;
	}

	
	/**
		  * @return Returns the numeroSolicitud.
		  */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}

	/**
	 * @param numeroSolicitud
	 *                  The numeroSolicitud to set.
	 */
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	/**
	 * @return Returns the estadoMedico.
	 */
	public String getEstadoMedico() {
		return estadoMedico;
	}

	/**
	 * @param estadoMedico
	 *                  The estadoMedico to set.
	 */
	public void setEstadoMedico(String estadoMedico) {
		this.estadoMedico = estadoMedico;
	}

	/**
	 * @return Returns the fechaSolicitud.
	 */
	public String getFechaSolicitud() {
		return fechaSolicitud;
	}

	/**
	 * @param fechaSolicitud
	 *                  The fechaSolicitud to set.
	 */
	public void setFechaSolicitud(String fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}

	/**
	 * @return Returns the horaSolicitud.
	 */
	public String getHoraSolicitud() {
		return horaSolicitud;
	}

	/**
	 * @param horaSolicitud
	 *                  The horaSolicitud to set.
	 */
	public void setHoraSolicitud(String horaSolicitud) {
		this.horaSolicitud = horaSolicitud;
	}

	/**
	 * @return Returns the medicoSolicitante.
	 */
	public String getMedicoSolicitante() {
		return medicoSolicitante;
	}

	/**
	 * @param medicoSolicitante
	 *                  The medicoSolicitante to set.
	 */
	public void setMedicoSolicitante(String medicoSolicitante) {
		this.medicoSolicitante = medicoSolicitante;
	}

	/**
	 * @return Returns the numeroAutorizacion.
	 */
	/*
	public String getNumeroAutorizacion() {
		return numeroAutorizacion;
	}
	*/
	/**
	 * @param numeroAutorizacion
	 *                  The numeroAutorizacion to set.
	 */
	/*
	public void setNumeroAutorizacion(String numeroAutorizacion) {
		this.numeroAutorizacion = numeroAutorizacion;
	}
	*/
	/**
	 * @return Returns the observacionesGenerales.
	 */
	public String getObservacionesGenerales() {
		return observacionesGenerales;
	}

	/**
	 * @param observacionesGenerales
	 *                  The observacionesGenerales to set.
	 */
	public void setObservacionesGenerales(String observacionesGenerales) {
		this.observacionesGenerales = observacionesGenerales;
	}

	/**
	 * @return Returns the farmaciaDespacho.
	 */
	public int getFarmaciaDespacho() {
		return farmaciaDespacho;
	}
	/**
	 * @param farmaciaDespacho The farmaciaDespacho to set.
	 */
	public void setFarmaciaDespacho(int farmaciaDespacho) {
		this.farmaciaDespacho = farmaciaDespacho;
	}
	
    /**
     * @return Returns the orden.
     */
    public int getOrden() {
        return orden;
    }
    /**
     * @param orden The orden to set.
     */
    public void setOrden(int orden) {
        this.orden = orden;
    }

	/**
	 * @return the resumenAdminMap
	 */
	@SuppressWarnings("rawtypes")
	public HashMap getResumenAdminMap() {
		return resumenAdminMap;
	}

	/**
	 * @param resumenAdminMap the resumenAdminMap to set
	 */
	@SuppressWarnings("rawtypes")
	public void setResumenAdminMap(HashMap resumenAdminMap) {
		this.resumenAdminMap = resumenAdminMap;
	}

	/**
	 * @return the resumenAdminMap
	 */
	public Object getResumenAdminMap(Object key) {
		return resumenAdminMap;
	}

	/**
	 * @param resumenAdminMap the resumenAdminMap to set
	 */
	@SuppressWarnings("unchecked")
	public void setResumenAdminMap(Object key, Object value) {
		this.resumenAdminMap.put(key, value);
	}

	/**
	 * @return the codigoEstadoMedico
	 */
	public int getCodigoEstadoMedico() {
		return codigoEstadoMedico;
	}

	/**
	 * @param codigoEstadoMedico the codigoEstadoMedico to set
	 */
	public void setCodigoEstadoMedico(int codigoEstadoMedico) {
		this.codigoEstadoMedico = codigoEstadoMedico;
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static ArrayList<DtoAdministracionMedicamentosBasico> cargarResumenAdministracion(Connection con, int numeroSolicitud) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAdminMedicamentosDao().cargarResumenAdministracion(con,numeroSolicitud);
	}

}