package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.FormatoJustServNoposDao;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * mundo del formato para la justificación de servicios no pos
 * @author axioma
 *
 */
public class FormatoJustServNopos
{
	//--- Atributos
	
	private static Logger logger = Logger.getLogger(Secciones.class);
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static FormatoJustServNoposDao formatoJustServNoposDao;
	
	/**
	 * 
	 */
	private String subcuenta;
	
	/**
	 * 
	 */
	private int institucion;
	
	/**
	 * 
	 */
	private String usuarioModifica;
	
	/**
	 * 
	 */
	private String servicio;
	
	/**
	 * 
	 */
	private String solicitud;

	/**
	 * 
	 */
	private int cantServicio;
	
	/**
	 * 
	 */
	private int codigoOrden;
	
	/**
	 * 
	 */
	private boolean provieneOrdenAmbulatoria;
	
	/**
	 *
	 *
	 */
	public FormatoJustServNopos()
	{
		this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * 
	 *
	 */
	public void reset() 
	{
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.usuarioModifica = "";
		this.servicio="";
		this.solicitud="";
	}

	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		if (myFactory != null)
		{
			formatoJustServNoposDao = myFactory.getFormatoJustServNoposDao();
			wasInited = (formatoJustServNoposDao != null);
		}
		return wasInited;
	}
	
	/**
	 * 
	 * @param con
	 * @param seccion
	 * @return
	 */
	public static HashMap consultar(Connection con, FormatoJustServNopos formatoJust, PersonaBasica paciente, UsuarioBasico usuario, boolean nueva)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoJustServNoposDao().consultar(con, formatoJust, paciente, usuario, nueva);
	}
	
	/**
	 * 
	 * @param con
	 * @param numJustificacion
	 * @return
	 */
	public static HashMap consultarDiagnosticos(Connection con, int numJustificacion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoJustServNoposDao().consultarDiagnosticos(con, numJustificacion);
	}
	
	/**
	 * Metodo para ingresar una justificación No Pos de Servicios.
	 * Los campos solicitud y ordenAmbulatoria son excluyentes!
	 * @param con
	 * @param institucion
	 * @param usuarioModifica
	 * @param justificacion
	 * @param solicitud
	 * @param ordenAmbulatoria
	 * @param servicio
	 * @param profesional
	 * @return
	 */
	public static String ingresarJustificacion(Connection con, int institucion, String usuarioModifica, HashMap justificacion, int solicitud, int ordenAmbulatoria, int servicio, int profesional)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoJustServNoposDao().ingresarJustificacion(con, institucion, usuarioModifica, justificacion, solicitud, ordenAmbulatoria, servicio, profesional);
	}
	
	/**
	 * Metodo para revisar existencia de justificaciones y modificarlas segun distribucion
	 * @param con
	 * @param numSol
	 * @param subCuenta
	 * @param cantidad
	 * @return
	 */
	public static int revisarJustificacionDistribucion(Connection con, String numSol, String subCuenta, String cantidad, String servicio, String usuario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoJustServNoposDao().revisarJustificacionDistribucion(con, numSol, subCuenta, cantidad, servicio, usuario);
	}
	
	/**
	 * Metodo para validar COnvenio y Articulo No POs
	 * @param con
	 * @param articulo
	 * @param subCuenta
	 * @return
	 */
	public static boolean validarArtConvJustificacion(Connection con, String servicio, String subCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoJustServNoposDao().validarArtConvJustificacion(con, servicio, subCuenta);
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa justificacion
	 * @param solicitud
	 * @return
	 */
	public static String actualizarJustificacion(Connection con, int institucion, String usuarioModifica, HashMap justificacion, int solicitud, int servicio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoJustServNoposDao().actualizarJustificacion(con, institucion, usuarioModifica, justificacion, solicitud, servicio);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap<String, Object> SubCuentas (Connection con, int numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoJustServNoposDao().subCuentas(con, numeroSolicitud);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param subcuenta
	 * @param cantidad
	 * @return
	 */
	public static boolean ingresarResponsable (Connection con, String numeroSolicitud, int subcuenta, int cantidad)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoJustServNoposDao().ingresarResponsable(con, numeroSolicitud, subcuenta, cantidad);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param servicio
	 * @return
	 */
	public static boolean existeJustificacion (Connection con, String numeroSolicitud, String servicio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoJustServNoposDao().existeJustificacion(con, numeroSolicitud, servicio);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param servicio
	 * @return
	 */
	public static HashMap<String, Object> consultarJustificacionHistorica(Connection con, String numeroSolicitud, String servicio) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFormatoJustServNoposDao().consultarJustificacionHistorica(con, numeroSolicitud, servicio);
	}
	
	/**
	 * @return the formatoJustServNoposDao
	 */
	public static FormatoJustServNoposDao getFormatoJustServNoposDao() {
		return formatoJustServNoposDao;
	}

	/**
	 * @param formatoJustServNoposDao the formatoJustServNoposDao to set
	 */
	public static void setFormatoJustServNoposDao(
			FormatoJustServNoposDao formatoJustServNoposDao) {
		FormatoJustServNopos.formatoJustServNoposDao = formatoJustServNoposDao;
	}

	/**
	 * @return the logger
	 */
	public static Logger getLogger() {
		return logger;
	}

	/**
	 * @param logger the logger to set
	 */
	public static void setLogger(Logger logger) {
		FormatoJustServNopos.logger = logger;
	}

	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return the solicitud
	 */
	public String getSolicitud() {
		return solicitud;
	}

	/**
	 * @param solicitud the solicitud to set
	 */
	public void setSolicitud(String solicitud) {
		this.solicitud = solicitud;
	}

	/**
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}

	/**
	 * @return the servicio
	 */
	public String getServicio() {
		return servicio;
	}

	/**
	 * @param servicio the servicio to set
	 */
	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	/**
	 * @return the cantServicio
	 */
	public int getCantServicio() {
		return cantServicio;
	}

	/**
	 * @param cantServicio the cantServicio to set
	 */
	public void setCantServicio(int cantServicio) {
		this.cantServicio = cantServicio;
	}

	/**
	 * @return the subcuenta
	 */
	public String getSubcuenta() {
		return subcuenta;
	}

	/**
	 * @param subcuenta the subcuenta to set
	 */
	public void setSubcuenta(String subcuenta) {
		this.subcuenta = subcuenta;
	}
	
	/**
	 * @return the codigoOrden
	 */
	public int getCodigoOrden() {
		return codigoOrden;
	}
	
	/**
	 * @param codigoOrden the codigoOrden to set
	 */
	public void setCodigoOrden(int codigoOrden) {
		this.codigoOrden = codigoOrden;
	}
	
	/**
	 * @return the provieneOrdenAmbulatoria
	 */
	public boolean isProvieneOrdenAmbulatoria() {
		return provieneOrdenAmbulatoria;
	}
	
	/**
	 * @param provieneOrdenAmbulatoria the provieneOrdenAmbulatoria to set
	 */
	public void setProvieneOrdenAmbulatoria(boolean provieneOrdenAmbulatoria) {
		this.provieneOrdenAmbulatoria = provieneOrdenAmbulatoria;
	}
	
}



