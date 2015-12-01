package com.princetonsa.mundo.salasCirugia;

import java.sql.Connection;
import java.util.HashMap;


import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.salasCirugia.AsociosXTipoServicioDao;


/**
 * 
 * @author juan sebastian castaño
 * Mundo para la funcionalidad de asocios X Tipo de Servicio
 * Martes 19 de febrero del 2008
 */
public class AsociosXTipoServicio {

	
	private AsociosXTipoServicioDao asoXTipoServDao = null;
	
	/**
	 * Codigo institucion en sesion
	 */
	private int institucion;
	
	/**
	 * Acronimo del tipo de servicio
	 */
	private String tipo_servicio;
	
	/**
	 * Codigo del tipo_asocio
	 */
	private int asocio;
	
	/**
	 * Codigo servicio
	 */
	private int servicio;
	
	/**
	 *Activo 
	 */
	private String activo;
	
	/**
	 * Login usuario en sesion
	 */
	private String usuario;
	
	
	/**
	 * codigo de registro de serv_aso
	 */
	private int codigo;
	
	
	/**
	 * POsicion del registro a eliminar
	 */
	private int codigoModificarPos;
	
	
	/**
	 * Constructor
	 */
	public AsociosXTipoServicio() {
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * método para inicializar los datos
	 *
	 */
	public void clean()
	{
		this.asoXTipoServDao = null;
		this.institucion = 0;
		this.tipo_servicio = "";
		this.asocio = 0;
		this.servicio = 0;
		this.activo = "";
		this.usuario = "";
		this.codigo=0;
		this.codigoModificarPos=0;
		
	}
	
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (asoXTipoServDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			asoXTipoServDao = myFactory.getAsociosXTipoServicioDao();
		}	
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
	 * Consulta de todos los servicios asocioados
	 * @param con
	 * @return
	 */
	public HashMap<String, Object> cargarServiciosAsocios(Connection con){
		return asoXTipoServDao.cargarServiciosAsocios(con, institucion);
	}
	
	
	/**
	 * Metodo para cargar todos los tipos de servicio en un select
	 * @param con
	 * @return
	 */
	
	public HashMap<String, Object> cargarTiposServicio(Connection con)
	{
		return asoXTipoServDao.cargarTiposServicio(con);
	}
	
	/**
	 * Metodo para cargar todos los tipos de asocio dependiendo de la institucion en un select
	 * @param con
	 * @return
	 */
	
	public HashMap<String, Object> cargarTiposAsocios(Connection con)
	{
		return asoXTipoServDao.cargarTiposAsocios(con, institucion);
	}
	/**
	 * Metodo de indercion de tipos asocios
	 * @param con
	 * @return
	 */
	public boolean insertarTiposAsocios (Connection con)
	{
		return asoXTipoServDao.insertarTiposAsocios(con, tipo_servicio, asocio, servicio, institucion, usuario);
	}

	/**
	 * Metodo de eliminacion de un servicio Asocio
	 * @param con
	 * @return
	 */
	public boolean eliminarServAsocio(Connection con)
	{
		return asoXTipoServDao.eliminarServAsocio(con, institucion, codigo);
	}
	
	
	
	/**
	 * Metodo de modificacion de un registro de servicio asocios
	 * @param con
	 * @return
	 */
	public boolean modificarServAsocio (Connection con)
	{
		return asoXTipoServDao.modificarServAsocio(con, codigo, tipo_servicio, asocio, servicio, institucion, usuario);
	}
	
	
	/**
	 * @return the activo
	 */
	public String getActivo() {
		return activo;
	}

	/**
	 * @param activo the activo to set
	 */
	public void setActivo(String activo) {
		this.activo = activo;
	}

	/**
	 * @return the asocio
	 */
	public int getAsocio() {
		return asocio;
	}

	/**
	 * @param asocio the asocio to set
	 */
	public void setAsocio(int asocio) {
		this.asocio = asocio;
	}

	/**
	 * @return the servicio
	 */
	public int getServicio() {
		return servicio;
	}

	/**
	 * @param servicio the servicio to set
	 */
	public void setServicio(int servicio) {
		this.servicio = servicio;
	}

	/**
	 * @return the tipo_servicio
	 */
	public String getTipo_servicio() {
		return tipo_servicio;
	}

	/**
	 * @param tipo_servicio the tipo_servicio to set
	 */
	public void setTipo_servicio(String tipo_servicio) {
		this.tipo_servicio = tipo_servicio;
	}

	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the codigoModificarPos
	 */
	public int getCodigoModificarPos() {
		return codigoModificarPos;
	}

	/**
	 * @param codigoModificarPos the codigoModificarPos to set
	 */
	public void setCodigoModificarPos(int codigoModificarPos) {
		this.codigoModificarPos = codigoModificarPos;
	}
	
	
}
