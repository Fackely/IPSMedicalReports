/*
 * Nov 09, 2006
 */
package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadValidacion;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.InstitucionesSircDao;

/**
 * @author Sebastián Gómez 
 *
 *Clase que representa el Mundo con sus atributos y métodos de la funcionalidad
 * Parametrización de Instituciones SIRC
 */
public class InstitucionesSirc 
{

	Logger logger = Logger.getLogger(InstitucionesSirc.class);
	
	/**
	 * DAO para el manejo de InstitucionesSircDao
	 */
	private InstitucionesSircDao institucionesDao=null;
	
	/**
	 * Código
	 */
	private String codigo;
	/**
	 * Institución
	 */
	private int institucion;
	/**
	 * Descripción
	 */
	private String descripcion;	
	/**
	 * Datos del nivel de servicio
	 */
	private InfoDatosInt nivel;
	/**
	 * Datos del tipo de red
	 */
	private String tipoRed;
	/**
	 * Tipo Institucion Referencia
	 * */
	private String tipoReferencia;
	/**
	 * Tipo Institucion Ambulancia
	 * */
	private String tipoAmbulancia;
	/**
	 * Campo activo
	 */
	private String activo;
	/**
	 * Objeto que sirve para pasar los datos al DAO
	 */
	private HashMap campos;
	
	//*****************************************************************
	//**********INICIALIZADORES & CONSTRUCTORES***********************
	/**
	 * Constructor
	 */
	public InstitucionesSirc() {
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * método para inicializar los datos
	 *
	 */
	public void clean()
	{		
		this.codigo = "";
		this.institucion = 0;
		this.descripcion = "";	
		this.nivel = new InfoDatosInt(0,"");
		this.tipoRed = "";
		this.tipoReferencia="";
		this.tipoAmbulancia="";
		this.activo = "";
		this.campos = new HashMap();
	}
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (institucionesDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			institucionesDao = myFactory.getInstitucionesSircDao();
		}	
	}
	//****************************************************************************
	//********************MÉTODOS**************************************************
	/**
	 * Método que carga las instituciones SIRC por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarInstituciones(Connection con,int institucion)
	{
		HashMap mapTemp = new HashMap();
		mapTemp = institucionesDao.cargarInstituciones(con,institucion);	
		
		for (int i=0; i<Integer.parseInt(mapTemp.get("numRegistros").toString()); i++)
		{
			if(UtilidadValidacion.esInstitucionSircUsado(con,mapTemp.get("codigo_"+i).toString(),Integer.parseInt(mapTemp.get("institucion_"+i).toString())))
				mapTemp.put("es_usado_"+i, "true");			
		}
		return mapTemp; 
	}
	
	/**
	 * Método que carga las instituciones SIRC por institucion
	 * @param institucion
	 * @return
	 */
	public static HashMap cargarInstituciones(int institucion)
	{
		Connection con= UtilidadBD.abrirConexion();
		HashMap mapTemp = new HashMap();
		mapTemp = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getInstitucionesSircDao().cargarInstituciones(con,institucion);	
		UtilidadBD.closeConnection(con);
		return mapTemp; 
	}
	
	
	/**
	 * Método que carga los datos de una institucion SIRC
	 * @param con
	 * @return
	 */
	public boolean cargarInstitucion(Connection con)
	{
		HashMap mapInstitucion = institucionesDao.cargarInstitucion(con,campos);
		boolean exito = false;
		if(Integer.parseInt(mapInstitucion.get("numRegistros").toString())>0)
		{
			exito = true;			
			this.codigo = mapInstitucion.get("codigo_0").toString();
			this.institucion = Integer.parseInt(mapInstitucion.get("institucion_0").toString());			
			this.descripcion = mapInstitucion.get("descripcion_0").toString();
			this.nivel = new InfoDatosInt(Integer.parseInt(mapInstitucion.get("codigo_nivel_0").toString()),mapInstitucion.get("nombre_nivel_0").toString());			
			this.tipoRed = mapInstitucion.get("codigo_tipo_red_0").toString();
			this.tipoReferencia = mapInstitucion.get("codigo_tipo_refe_0").toString();
			this.tipoAmbulancia = mapInstitucion.get("codigo_tipo_ambu_0").toString();			
			this.activo = mapInstitucion.get("activo_0").toString();
		}
		
		return exito;
	}
	
	/**
	 * Método que inserta una institucion SIRC
	 * @param con
	 * @return
	 */
	public int insertar(Connection con)
	{
		return institucionesDao.insertar(con,campos);
	}
	
	/**
	 * Método que modifica una institucion SIRC
	 * @param con
	 * @param campos
	 * @return
	 */
	public int modificar(Connection con)
	{
		return institucionesDao.modificar(con,campos);
	}
	
	
	/**
	 * Método que elimina una institucion SIRC
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public int eliminar(Connection con, String codigo, int institucion)
	{
		return institucionesDao.eliminar(con, codigo, institucion);
	}
		
	/**
	 * Método que carga los niveles de servicio por institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarNivelesServicio(Connection con,int institucion)
	{
		return institucionesDao.cargarNivelesServicio(con,institucion);
	}
	//****************************************************************************
	//********************GETTERS & SETTERS**************************************************
	/**
	 * @return Returns the activo.
	 */
	public String isActivo() {
		return activo;
	}
	/**
	 * @param activo The activo to set.
	 */
	public void setActivo(String activo) {
		this.activo = activo;
	}
	/**
	 * @return Returns the activo.
	 */
	public String getActivo() {
		return activo;
	}
	/**
	 * @return Returns the campos.
	 */
	public HashMap getCampos() {
		return campos;
	}
	/**
	 * @param campos The campos to set.
	 */
	public void setCampos(HashMap campos) {
		this.campos = campos;
	}
	/**
	 * @return Retorna elemento del mapa campos.
	 */
	public Object getCampos(String key) {
		return campos.get(key);
	}
	/**
	 * @param Asigna elemento al mapa campos.
	 */
	public void setCampos(String key,Object obj) {
		this.campos.put(key,obj);
	}
	/**
	 * @return Returns the codigo.
	 */
	public String getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo The codigo to set.
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}	
	/**
	 * @return Returns the descripcion.
	 */
	public String getDescripcion() {
		return descripcion;
	}
	/**
	 * @param descripcion The descripcion to set.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	/**
	 * @return Returns the institucion.
	 */
	public int getInstitucion() {
		return institucion;
	}
	/**
	 * @param institucion The institucion to set.
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}
	/**
	 * @return Returns the institucionesDao.
	 */
	public InstitucionesSircDao getInstitucionesDao() {
		return institucionesDao;
	}
	/**
	 * @param institucionesDao The institucionesDao to set.
	 */
	public void setInstitucionesDao(InstitucionesSircDao institucionesDao) {
		this.institucionesDao = institucionesDao;
	}
	/**
	 * @return Returns the nivel.
	 */
	public InfoDatosInt getNivel() {
		return nivel;
	}
	/**
	 * @param nivel The nivel to set.
	 */
	public void setNivel(InfoDatosInt nivel) {
		this.nivel = nivel;
	}
	/**
	 * @return Returns the tipoRed.
	 */
	public String getTipoRed() {
		return tipoRed;
	}
	/**
	 * @param tipoRed The tipoRed to set.
	 */
	public void setTipoRed(String tipoRed) {
		this.tipoRed = tipoRed;
	}
	/**
	 * @return the tipoAmbulancia
	 */
	public String getTipoAmbulancia() {
		return tipoAmbulancia;
	}
	/**
	 * @param tipoAmbulancia the tipoAmbulancia to set
	 */
	public void setTipoAmbulancia(String tipoAmbulancia) {
		this.tipoAmbulancia = tipoAmbulancia;
	}
	/**
	 * @return the tipoReferencia
	 */
	public String getTipoReferencia() {
		return tipoReferencia;
	}
	/**
	 * @param tipoReferencia the tipoReferencia to set
	 */
	public void setTipoReferencia(String tipoReferencia) {
		this.tipoReferencia = tipoReferencia;
	}
	
	
}
