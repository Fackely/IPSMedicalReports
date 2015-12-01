package com.princetonsa.mundo;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.UbicacionGeograficaDao;
import com.princetonsa.dao.manejoPaciente.ViasIngresoDao;

public class UbicacionGeografica {
	
	/**
	 * Intefaz para acceder y comunicarse con la fuente de datos
	 */
	private UbicacionGeograficaDao objetoDao;
	

///////////////////////////////////////INICIO DE LOS MAPAS///////////////////////////////////////	
	
	/**
	 * Mapa de pais
	 */
	private HashMap paisMap;
	
	/**
	 * Mapa de departamento
	 */
	private HashMap departamentoMap;
	
	/**
	 * Mapa de ciudad
	 */
	private HashMap ciudadMap;
	
	/**
	 * Mapa de localidad
	 */
	private HashMap localidadMap;
	
	/**
	 * Mapa de barrio
	 */
	private HashMap barrioMap;

///////////////////////////////////////FIN DE LOS MAPAS///////////////////////////////////////
	
	
	
	
	
	/**
	 * Inicializa el acceso a la base de datos de este objeto, obteniendo su respectivo DAO.
	 * param tipoBD el tipo de bases de datos que va a usar este objeto.
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores validos para tipoBD.
	 * son los nombres y constantes definidos en <code>DaoFactory</code>
	 * @return <b>true</b> si la inicializacion fue exitosa, <code>false</code> si no.
	 */
	
	private boolean init(String tipoBD) 
	{
		if(objetoDao==null)
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(tipoBD);
			objetoDao=myFactory.getUbicacionGeograficaDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
			
		
	}
	
	/**
	 * resetea los atributos del objeto.
	 *
	 */
	
	private void reset() 
	{
		paisMap=new HashMap();
    	paisMap.put("numRegistros","0");
    	
    	departamentoMap=new HashMap();
    	departamentoMap.put("numRegistros","0");
    	
    	ciudadMap=new HashMap();
    	ciudadMap.put("numRegistros","0");
    	
    	localidadMap=new HashMap();
    	localidadMap.put("numRegistros","0");
    	
    	barrioMap=new HashMap();
    	barrioMap.put("numRegistros","0");
        
   	}
	
	/**
	 *  Constructor de la clase
	 */
	public UbicacionGeografica() 
	{
		init(System.getProperty("TIPOBD"));
		this.reset();
	}
	
	
	
////////////////////////////INICIO INSERTAR//////////////////////////////////	

	/**
	 * Insertar pais
	 * */
	public boolean insertarPais(Connection con,HashMap vo)
	{
		return objetoDao.insertarPais(con, vo);
	}
	
	/**
	 * Insertar departamento
	 * */
	public boolean insertarDepartamento(Connection con,HashMap vo)
	{
		return objetoDao.insertarDepartamento(con, vo);
	}
	
	/**
	 * Insertar ciudad
	 * */
	public boolean insertarCiudad(Connection con,HashMap vo)
	{
		return objetoDao.insertarCiudad(con, vo);
	}
	
	/**
	 * Insertar localidad
	 * */
	public boolean insertarLocalidad(Connection con,HashMap vo)
	{
		return objetoDao.insertarLocalidad(con, vo);
	}
	
	/**
	 * Insertar barrio
	 * */
	public boolean insertarBarrio(Connection con,HashMap vo)
	{
		return objetoDao.insertarBarrio(con, vo);
	}

////////////////////////////FIN INSERTAR//////////////////////////////////
	
	
	
	
	
////////////////////////////INICIO CONSULTAR//////////////////////////////////
	
	/**
	 * Consultar pais
	 * */
	public HashMap consultarPais(Connection con) 
	{
		HashMap vo=new HashMap();
		//vo.put("codigo", codigo);
		this.paisMap=objetoDao.consultarPais(con,vo);
		return objetoDao.consultarPais(con, vo);
	}
	
	/**
	 * Consultar departamento
	 * */
	public HashMap consultarDepartamento(Connection con, String codigo_pais) 
	{
		HashMap vo=new HashMap();
		vo.put("codigo_pais", codigo_pais);
		return objetoDao.consultarDepartamento(con, vo);
	}
	
	
	/**
	 * Consultar ciudad
	 * */
	public HashMap consultarCiudad(Connection con, String codigo_departamento, String codigo_pais) 
	{
		HashMap vo=new HashMap();
		vo.put("codigo_departamento", codigo_departamento);
		vo.put("codigo_pais", codigo_pais);
		return objetoDao.consultarCiudad(con, vo);
	}
	
	/**
	 * Consultar localidad
	 * */
	public HashMap consultarLocalidad(Connection con, String codigo_ciudad, String codigo_departamento, String codigo_pais) 
	{
		HashMap vo=new HashMap();
		vo.put("codigo_ciudad", codigo_ciudad);
		vo.put("codigo_departamento", codigo_departamento);
		vo.put("codigo_pais", codigo_pais);
		return objetoDao.consultarLocalidad(con, vo); 
	}
	
	/**
	 * Consultar barrio
	 * */
	public HashMap consultarBarrio(Connection con, String localidad, String codigo_ciudad, String codigo_departamento, String codigo_pais) 
	{
		HashMap vo=new HashMap();
		vo.put("codigo_localidad", localidad);
		vo.put("codigo_ciudad", codigo_ciudad);
		vo.put("codigo_departamento", codigo_departamento);
		vo.put("codigo_pais", codigo_pais);
		return objetoDao.consultarBarrio(con, vo);
	}

////////////////////////////FIN CONSULTAR//////////////////////////////////
	
	
	
	
	
////////////////////////////INICIO MODIFICAR//////////////////////////////////
	
	/**
	 * Modificar pais
	 * */
	public boolean modificarPais(Connection con,HashMap vo)
	{
		return objetoDao.modificarPais(con, vo);
	}
	
	/**
	 * Modificar departamento
	 * */
	public boolean modificarDepartamento(Connection con,HashMap vo)
	{
		return objetoDao.modificarDepartamento(con, vo);
	}
	
	/**
	 * Modificar ciudad
	 * */
	public boolean modificarCiudad(Connection con,HashMap vo)
	{
		return objetoDao.modificarCiudad(con, vo);
	}
	
	/**
	 * Modificar localidad
	 * */
	public boolean modificarLocalidad(Connection con,HashMap vo)
	{
		return objetoDao.modificarLocalidad(con, vo);
	}
	
	/**
	 * Modificar barrio
	 * */
	public boolean modificarBarrio(Connection con,HashMap vo)
	{
		return objetoDao.modificarBarrio(con, vo);
	}
	
////////////////////////////FIN MODIFICAR//////////////////////////////////
	
	
	
	
	
////////////////////////////INICIO ELIMINAR//////////////////////////////////
	
	/**
	 * eliminar pais
	 * */
	public boolean eliminarPais(Connection con, String codigo)
	{
		return objetoDao.eliminarPais(con,codigo);
	}
	
	/**
	 * eliminar departamento
	 * */
	public boolean eliminarDepartamento(Connection con, String codigo_departamento,String codigo_pais)
	{
		return objetoDao.eliminarDepartamento(con,codigo_departamento,codigo_pais);
	}
	
	/**
	 * eliminar ciudad
	 * */
	public boolean eliminarCiudad(Connection con, String codigo_ciudad, String codigo_departamento, String codigo_pais)
	{
		return objetoDao.eliminarCiudad(con,codigo_ciudad,codigo_departamento,codigo_pais);
	}
	
	/**
	 * eliminar localidad
	 * */
	public boolean eliminarLocalidad(Connection con, String codigo_localidad,String codigo_ciudad, String codigo_departamento, String codigo_pais)
	{
		return objetoDao.eliminarLocalidad(con,codigo_localidad,codigo_ciudad,codigo_departamento,codigo_pais);
	}
	
	/**
	 * eliminar barrio
	 * */
	public boolean eliminarBarrio(Connection con, int codigo)
	{
		return objetoDao.eliminarBarrio(con,codigo);
	}
	
////////////////////////////FIN ELIMINAR//////////////////////////////////
	
	
	
	

	public HashMap getBarrioMap() {
		return barrioMap;
	}

	public void setBarrioMap(HashMap barrioMap) {
		this.barrioMap = barrioMap;
	}

	public HashMap getCiudadMap() {
		return ciudadMap;
	}

	public void setCiudadMap(HashMap ciudadMap) {
		this.ciudadMap = ciudadMap;
	}

	public HashMap getDepartamentoMap() {
		return departamentoMap;
	}

	public void setDepartamentoMap(HashMap departamentoMap) {
		this.departamentoMap = departamentoMap;
	}

	public HashMap getLocalidadMap() {
		return localidadMap;
	}

	public void setLocalidadMap(HashMap localidadMap) {
		this.localidadMap = localidadMap;
	}

	public HashMap getPaisMap() {
		return paisMap;
	}

	public void setPaisMap(HashMap paisMap) {
		this.paisMap = paisMap;
	}

	
	
}
