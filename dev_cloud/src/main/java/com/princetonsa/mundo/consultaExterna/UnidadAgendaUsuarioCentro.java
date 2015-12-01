package com.princetonsa.mundo.consultaExterna;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.consultaExterna.UnidadAgendaUsuarioCentroDao;
import com.princetonsa.mundo.UsuarioBasico;

public class UnidadAgendaUsuarioCentro
{
	private static Logger logger = Logger.getLogger(UnidadAgendaUsuarioCentro.class);
	
	/**
	 * Codigo del centro de atencion
	 */
	private String centroAtencion;
	
	/**
	 * Codigo de la Unidad
	 */
	private String unidad;
	
	/**
	 * Codigo Actividad
	 */
	private String actividad;
	
	/**
	 * Login del Usuario Autorizado
	 */
	private String usuario;
	
	/**
	 * Codigo de la Institucion
	 */
	private String institucion;
	
	/**
	 * Login del Usuario Modifica
	 */
	private String usuarioM;
	
	/**
	 * HashMap con los nombres generados de las actividades adicionadas 
	 */
	private HashMap actividadesGeneradosM;
	
	/**
	 * Codigo Registro  Modificar
	 */
	private String codigoM;
	
	/**
	 * Tipo de atencion de la unidad
	 */
	private String tipoAtencion;
	
	
	public String getTipoAtencion() {
		return tipoAtencion;
	}

	public void setTipoAtencion(String tipoAtencion) {
		this.tipoAtencion = tipoAtencion;
	}

	/**
	 * Buscas la posicion del codigo dentro del mapa de actividades
	 * @param HashMap mapa
	 * @param String codigo
	 * */
	public static int buscarPosArrayActividades(HashMap mapa, String codigo)
	{
		for(int i=0;i<Integer.parseInt(mapa.get("numRegistros").toString());i++)
		{
			if(codigo.equals(mapa.get("codigo_"+i).toString()))			
				return i;			
		}
		
		return ConstantesBD.codigoNuncaValido;
	}	
	
	/**
	 * Retorna el Get del Dao
	 * @return
	 */
	private static UnidadAgendaUsuarioCentroDao getUnidadAgendaUsuarioCentroDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUnidadAgendaUsuarioCentroDao();
	}
	
	/**
	 * Metodo para Consultar Unidad Agenda Principal
	 * @param con
	 * @return
	 */
	public static HashMap<String, Object> consultaUA (Connection con, int centroAtencion,String usuario)
	{
		return getUnidadAgendaUsuarioCentroDao().consultaUA(con, centroAtencion,usuario);
	}
	
	public static HashMap<String, Object> consultaUA1 (Connection con, int centroAtencion,String usuario)
	{
		return getUnidadAgendaUsuarioCentroDao().consultaUA1(con, centroAtencion,usuario);
	}
	
	/**
	 * Metodo para consultar Unidades de Agenda por Usuario por Centro de Atencion Generica
	 * @param con
	 * @param centroAtencion
	 * @param unidadAgenda
	 * @param usuarioAutorizado
	 * @return
	 */
	public static HashMap<String, Object> consultaUAP (Connection con, int centroAtencion, int unidadAgenda, String usuarioAutorizado)
	{
		return getUnidadAgendaUsuarioCentroDao().consultaUAP(con, centroAtencion, unidadAgenda, usuarioAutorizado);
	}
	
	public static HashMap<String, Object> consultaUnidades (Connection con, int centroAtencion)
	{
		return getUnidadAgendaUsuarioCentroDao().consultaUnidades(con, centroAtencion);
	}
	
	public static HashMap<String, Object> consultaUsuarios (Connection con)
	{
		return getUnidadAgendaUsuarioCentroDao().consultaUsuarios(con);
	}
	
	public static HashMap<String, Object> consultaActividades (Connection con,UsuarioBasico user)
	{
		return getUnidadAgendaUsuarioCentroDao().consultaActividades(con,user);
	}
	
	public static boolean insertarUnidad(Connection con, UnidadAgendaUsuarioCentro mundo)
	{
		return getUnidadAgendaUsuarioCentroDao().insertarUnidad(con, mundo);
	}
	
	public static boolean insertarActividadxUnidad(Connection con, UnidadAgendaUsuarioCentro mundo)
	{
		return getUnidadAgendaUsuarioCentroDao().insertarActividadxUnidad(con, mundo);
	}
	
	public static boolean eliminar(Connection con, int codigo)
	{
		return getUnidadAgendaUsuarioCentroDao().eliminar(con, codigo);
	}
	
	public static boolean modificar(Connection con, UnidadAgendaUsuarioCentro mundo)
	{
		return getUnidadAgendaUsuarioCentroDao().modificar(con, mundo);
	}
	
	public static boolean insertarActModificar(Connection con, UnidadAgendaUsuarioCentro mundo, int codigoAct)
	{
		return getUnidadAgendaUsuarioCentroDao().insertarActModificar(con, mundo, codigoAct);
	}
	
	public static boolean eliminarActModificar(Connection con, UnidadAgendaUsuarioCentro mundo, int codigoAct)
	{
		return getUnidadAgendaUsuarioCentroDao().eliminarActModificar(con, mundo, codigoAct);
	}

	public HashMap getActividadesGeneradosM() {
		return actividadesGeneradosM;
	}

	public void setActividadesGeneradosM(HashMap actividadesGeneradosM) {
		this.actividadesGeneradosM = actividadesGeneradosM;
	}
	
	public Object getActividadesGeneradosM(String key) {
		return actividadesGeneradosM.get(key);
	}

	public void setActividadesGeneradosM(String key, Object value) {
		this.actividadesGeneradosM.put(key, value);
	}

	public String getCentroAtencion() {
		return centroAtencion;
	}

	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public String getUnidad() {
		return unidad;
	}

	public void setUnidad(String unidad) {
		this.unidad = unidad;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getUsuarioM() {
		return usuarioM;
	}

	public void setUsuarioM(String usuarioM) {
		this.usuarioM = usuarioM;
	}

	public String getInstitucion() {
		return institucion;
	}

	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}

	public String getActividad() {
		return actividad;
	}

	public void setActividad(String actividad) {
		this.actividad = actividad;
	}

	public String getCodigoM() {
		return codigoM;
	}

	public void setCodigoM(String codigoM) {
		this.codigoM = codigoM;
	}
}