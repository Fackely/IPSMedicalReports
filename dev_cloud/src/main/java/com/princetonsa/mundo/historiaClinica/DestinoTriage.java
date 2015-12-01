/*
 * Mayo 31, 2006
 */
package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.DestinoTriageDao;


/**
 * @author Sebastián Gómez 
 *
 *Clase que representa el Mundo con sus atributos y métodos de la funcionalidad
 * Parametrización de Destinos Triage
 */
public class DestinoTriage 
{

	/**
	 * DAO para el manejo de DestinoTriageDao
	 */
	private DestinoTriageDao destinoDao=null;
	
	//*********ATRIBUTOS******************************************+
	
	/**
	 * Código del destino Triage
	 */
	private String codigo;
	
	/**
	 * Nombre del destino Triage
	 */
	private String nombre;
	
	/**
	 * Indicador para saber sid estino va para admision de urgencias
	 */
	private boolean indicadorAdminUrgencia;
	
	/**
	 * Institucion al cual hace parte el destino
	 */
	private String institucion;
	
	
	//*****************************************************************
	//**********INICIALIZADORES & CONSTRUCTORES***********************
	/**
	 * Constructor
	 */
	public DestinoTriage() {
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
		this.nombre = "";
		this.indicadorAdminUrgencia = false;
		this.institucion = "";
	}
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (destinoDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			destinoDao = myFactory.getDestinoTriageDao();
		}	
	}
	//****************************************************************************
	//********************MÉTODOS**************************************************
	/**
	 * Método implementado para cargar los destinos triage de una institucion
	 */
	public HashMap cargar(Connection con)
	{
		return destinoDao.cargar(con,this.institucion,this.codigo);
	}
	
	/**
	 * Método implementado para insertar un destino triage
	 * @param con
	 * @return
	 */
	public int insertar(Connection con)
	{
		return destinoDao.insertar(con,this.codigo,this.nombre,this.indicadorAdminUrgencia,this.institucion);
	}
	
	/**
	 * Método implementado para modificar un destino triage
	 * @param con
	 * @return
	 */
	public int modificar(Connection con)
	{
		return destinoDao.modificar(con,this.nombre,this.indicadorAdminUrgencia,this.codigo,this.institucion);
	}
	
	/**
	 * Método implementado para eliminar un destino triage
	 * @param con
	 * @return
	 */
	public int eliminar(Connection con)
	{
		return destinoDao.eliminar(con,this.codigo,this.institucion);
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
	 * @return Returns the indicadorAdminUrgencia.
	 */
	public boolean isIndicadorAdminUrgencia() {
		return indicadorAdminUrgencia;
	}
	/**
	 * @param indicadorAdminUrgencia The indicadorAdminUrgencia to set.
	 */
	public void setIndicadorAdminUrgencia(boolean indicadorAdminUrgencia) {
		this.indicadorAdminUrgencia = indicadorAdminUrgencia;
	}
	/**
	 * @return Returns the institucion.
	 */
	public String getInstitucion() {
		return institucion;
	}
	/**
	 * @param institucion The institucion to set.
	 */
	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}
	/**
	 * @return Returns the nombre.
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre The nombre to set.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	
}
