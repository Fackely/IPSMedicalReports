/*
 * Mayo 03, 2006
 */
package com.princetonsa.mundo;

import java.sql.Connection;
import java.util.HashMap;

import util.UtilidadTexto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.UnidadFuncionalDao;

/**
 * @author Sebastián Gómez
 *
 * Objeto que representa la parametrización de Unidades Funcionales
 */
public class UnidadFuncional 
{
	/**
	 * DAO para el manejo de las Unidades Funcionales
	 */
	private UnidadFuncionalDao unidadDao = null;
	
	//************ATRIBUTOS**************************************
	/**
	 * Código de la unidad funcional
	 */
	private String codigo;
	/**
	 * Descripcion de la unidad funcional
	 */
	private String descripcion;
	/**
	 * Indicador de activación
	 */
	private boolean activo;
	/**
	 * código Institucion
	 */
	private int institucion;
	
	//*********CONTRUCTOR E INICIALIZADORES***********************************+
	/**
	 * Constructor
	 */
	public UnidadFuncional() {
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * método para inicializar los datos
	 *
	 */
	public void clean()
	{
		this.codigo= "";
		this.descripcion="";
		this.activo=false;
		this.institucion=0;
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (unidadDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			unidadDao = myFactory.getUnidadFuncionalDao();
		}	
	}
	
	
	//************MÉTODOS***************************************************+
	/**
	 * Método implementado para consultar el listado de las unidades
	 * funcionales por institucion
	 */
	public HashMap consultarListado(Connection con)
	{
		return unidadDao.consultar(con,this.institucion);
	}
	
	/**
	 * Método implementado para cargar los datos de un unidad funcional
	 * @param con
	 * @return
	 */
	public boolean cargar(Connection con)
	{
		boolean exito = false;
		HashMap datosUnidad = unidadDao.consultar(con,this.codigo,this.institucion);
		if(Integer.parseInt(datosUnidad.get("numRegistros").toString())>0)
		{
			this.codigo = datosUnidad.get("codigo").toString();
			this.descripcion = datosUnidad.get("descripcion").toString();
			this.activo = UtilidadTexto.getBoolean(datosUnidad.get("activo").toString());
			this.institucion = Integer.parseInt(datosUnidad.get("institucion").toString());
			exito = true;
		}
		
		return exito;
	}
	
	/**
	 * Método implementado para insertar una nueva unidad funcional
	 * @param con
	 * @return
	 */
	public int insertar(Connection con)
	{
		return unidadDao.insertar(con,this.codigo,this.descripcion,this.activo,this.institucion);
	}
	
	/**
	 * Método implementado para modificar una unidad funcional
	 * @param con
	 * @return
	 */
	public int modificar(Connection con)
	{
		return unidadDao.modificar(con,this.codigo,this.descripcion,this.activo,this.institucion);
	}
	
	/**
	 * Método implementado para elimina una unidad funcional
	 * @param con
	 * @return
	 */
	public int eliminar(Connection con)
	{
		return unidadDao.eliminar(con,this.codigo,this.institucion);
	}
	
	//*******GETTERS & SETTERS**********************************************
	
	/**
	 * @return Returns the activo.
	 */
	public boolean isActivo() {
		return activo;
	}
	/**
	 * @param activo The activo to set.
	 */
	public void setActivo(boolean activo) {
		this.activo = activo;
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
	
	
	

}
