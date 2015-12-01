/*
 * Marzo 28, del 2007
 */
package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.ArticulosFechaVencimientoDao;

/**
 * @author Sebastián Gómez 
 *
 *Clase que representa el Mundo con sus atributos y métodos de la funcionalidad
 * de consulta articulos x fecha de vencimiento
 */
public class ArticulosFechaVencimiento 
{
	/**
	 * DAO para el manejo de ArticulosFechaVencimientoDao
	 */
	private ArticulosFechaVencimientoDao articulosDao = null;
	
	/**
	 * Fecha de filtro de consulta
	 */
	private String fechaCorte;
	
	/**
	 * Código del articulo para la busqueda avanzada
	 */
	private String codigoArticulo;
	
	/**
	 * Descripcion del articulo para la busqueda avanzada
	 */
	private String descripcionArticulo;
	
	//*****************************************************************
	//**********INICIALIZADORES & CONSTRUCTORES***********************
	/**
	 * Constructor
	 */
	public ArticulosFechaVencimiento() 
	{
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * método para inicializar los datos
	 *
	 */
	public void clean()
	{
			this.fechaCorte = "";
			this.codigoArticulo = "";
			this.descripcionArticulo = "";
	}
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (articulosDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			articulosDao = myFactory.getArticulosFechaVencimientoDao();
		}	
	}
	
	/**
	 * Método que retorna el DAO instanciado de Hoja Gastos
	 * @return
	 */
	public static ArticulosFechaVencimientoDao articulosDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getArticulosFechaVencimientoDao();
	}
	
	//****************************************************************************
	//********************MÉTODOS**************************************************
	/**
	 * Método implementado para consultar las existencias de una articulo por fecha de vencimiento
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultarArticulosXFecha(Connection con, int institucion)
	{
		HashMap campos = new HashMap();
		campos.put("fecha",this.fechaCorte);
		campos.put("codigoArticulo", this.codigoArticulo);
		campos.put("descripcionArticulo",this.descripcionArticulo);
		campos.put("institucion", institucion);
		return articulosDao.consultarArticulosXFecha(con, campos);
	}
	
	/**
	 * Método implementado para consultar la impresion de las existencias de una articulo por fecha de vencimiento y por almacen
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap consultaImpresionArticulosXFecha(Connection con)
	{
		HashMap campos = new HashMap();
		campos.put("fecha",this.fechaCorte);
		campos.put("codigoArticulo", this.codigoArticulo);
		campos.put("descripcionArticulo",this.descripcionArticulo);
		return articulosDao.consultaImpresionArticulosXFecha(con, campos);
	}
	
	
	//****************************************************************************
	//********************GETTERS & SETTERS**************************************************
	
	/**
	 * @return the codigoArticulo
	 */
	public String getCodigoArticulo() {
		return codigoArticulo;
	}
	/**
	 * @param codigoArticulo the codigoArticulo to set
	 */
	public void setCodigoArticulo(String codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}
	/**
	 * @return the descripcionArticulo
	 */
	public String getDescripcionArticulo() {
		return descripcionArticulo;
	}
	/**
	 * @param descripcionArticulo the descripcionArticulo to set
	 */
	public void setDescripcionArticulo(String descripcionArticulo) {
		this.descripcionArticulo = descripcionArticulo;
	}
	/**
	 * @return the fechaCorte
	 */
	public String getFechaCorte() {
		return fechaCorte;
	}
	/**
	 * @param fechaCorte the fechaCorte to set
	 */
	public void setFechaCorte(String fechaCorte) {
		this.fechaCorte = fechaCorte;
	}
}
