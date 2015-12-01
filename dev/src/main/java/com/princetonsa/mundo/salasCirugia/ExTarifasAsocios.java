/*
 * Marzo 16, 2006
 */
package com.princetonsa.mundo.salasCirugia;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.salasCirugia.ExTarifasAsociosDao;

/**
 * @author Sebastián Gómez
 *
 * Clase que representa el Mundo con sus atributos y métodos de la funcionalidad
 * Parametrización de Excepciones Tarifas Asocios
 */
public class ExTarifasAsocios 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(ExTarifasAsocios.class);
	
	/**
	 * DAO para el manejo de ExTarifasAsocios
	 */
	private ExTarifasAsociosDao excepcionDao=null;
	
	//*****************CONSTRUCTOR E INICIALIZADORES***********************+
	/**
	 * Constructor
	 */
	public ExTarifasAsocios() {
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * método para inicializar los datos
	 *
	 */
	public void clean()
	{
		this.codigo = 0;
		this.codigoConvenio = 0;
		this.codigoServicio = 0;
		this.codigoAsocio = 0;
		this.porcentaje = 0;
		this.valor = 0;
		this.institucion = 0;
		this.incremento = false;
	}
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		
		if (excepcionDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			excepcionDao = myFactory.getExTarifasAsociosDao();
		}
			
	}
	
	//*************ATRIBUTOS**************************************************
	
	/**
	 * Codigo axioma del registro de excepciones
	 */
	private int codigo;
	
	/**
	 * Código del convenio
	 */
	private int codigoConvenio;
	
	/**
	 * Código del servicio
	 */
	private int codigoServicio;
	
	/**
	 * código Axioma del Asocio
	 */
	private int codigoAsocio;
	
	/**
	 * Porcentaje de la excepción
	 */
	private double porcentaje;
	
	/**
	 * Valor de la excepción
	 */
	private double valor;
	
	/**
	 * Indicador de calculo INCREMENTO/DECREMENTO en la excepción
	 */
	private boolean incremento;
	/**
	 * Código de la institucion
	 */
	private int institucion;
	
	//*************MÉTODOS****************************************************
	/**
	 * Insertar informacion tabla encabezado
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean insertarEncabezado(Connection con, HashMap parametros)
	{
		return excepcionDao.insertarEncabezado(con, parametros);
	}
	
	
	public HashMap cargarTipoPaciente(Connection con,HashMap datos)
	{
		for(int i=0; i<Integer.parseInt(datos.get("numRegistros").toString()); i++)
			datos.put("codigoH_"+i,UtilidadesManejoPaciente.obtenerTiposPacienteXViaIngreso(con,datos.get("codigo_"+i).toString()));						
			
			
		return datos;
	}
	
	
	/**
	 * Modificar informacion tabla encabezado
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean modificarEncabezado(Connection con, HashMap parametros)
	{
		return excepcionDao.modificarEncabezado(con, parametros);
	}
	
	
	/**
	 * Eliminar informacion tabla encabezado
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean EliminarEncabezado(Connection con, HashMap parametros)
	{
		return excepcionDao.EliminarEncabezado(con, parametros);
	}
	
	
	/**
	 * Consultar informacion tabla Encabezado
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public HashMap consultarEncabezado(Connection con, HashMap parametros)
	{
		return excepcionDao.consultarEncabezado(con, parametros);
	}
	
	/**
	 * Insertar informacion tabla media
	 * @param Connection con
	 * @param HashMap parametros
	 * @param String strInsertarMedia
	 * @return 
	 * */	
	public boolean insertarMedia(Connection con, HashMap parametros)
	{
		return excepcionDao.insertarMedia(con, parametros);
	}
	
	/**
	 * Modificar informacion tabla media
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean modificarMedia(Connection con, HashMap parametros)
	{
		return excepcionDao.modificarMedia(con, parametros); 
	}
	
	/**
	 * Consultar informacion tabla Media
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public HashMap consultarMedia(Connection con, HashMap parametros)
	{
		return excepcionDao.consultarMedia(con, parametros);
	}
	
	/**
	 * Eliminar informacion tabla Media
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean EliminarMedia(Connection con, HashMap parametros)
	{
		return excepcionDao.EliminarMedia(con, parametros);
	}
	
	/**
	 * Insertar informacion tabla Detalle
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean insertarDetalle(Connection con, HashMap parametros)
	{
		return  excepcionDao.insertarDetalle(con, parametros);
	}
	
	/**
	 * Modificar informacion tabla Detalle
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean modificarDetalle(Connection con, HashMap parametros)
	{
		return excepcionDao.modificarDetalle(con, parametros);
	}
	
	/**
	 * Eliminar informacion tabla Detalle
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public boolean EliminarDetalle(Connection con, HashMap parametros)
	{
		return excepcionDao.EliminarDetalle(con, parametros);
	}
	
	/**
	 * Consultar informacion tabla Detalle
	 * @param Connection con
	 * @param HashMap parametros
	 * @return 
	 * */	
	public HashMap consultarDetalle(Connection con, HashMap parametros)
	{
		return excepcionDao.consultarDetalle(con, parametros);
	}
	
	
	//*************GETTERS & SETTERS*******************************************
	public int getCodigoConvenio() 
	{
		return codigoConvenio;
	}
	public void setCodigoConvenio(int codigoConvenio) 
	{
		this.codigoConvenio = codigoConvenio;
	}
	public int getInstitucion() 
	{
		return institucion;
	}
	public void setInstitucion(int institucion) 
	{
		this.institucion = institucion;
	}
	public int getCodigoAsocio() {
		return codigoAsocio;
	}
	public void setCodigoAsocio(int codigoAsocio) {
		this.codigoAsocio = codigoAsocio;
	}
	public int getCodigoServicio() {
		return codigoServicio;
	}
	public void setCodigoServicio(int codigoServicio) {
		this.codigoServicio = codigoServicio;
	}
	public boolean isIncremento() {
		return incremento;
	}
	public void setIncremento(boolean incremento) {
		this.incremento = incremento;
	}
	public double getPorcentaje() {
		return porcentaje;
	}
	public void setPorcentaje(double porcentaje) {
		this.porcentaje = porcentaje;
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	public int getCodigo() {
		return codigo;
	}
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
}
