 /*
 * Mayo 15, 2007
 */
package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.UtilidadFecha;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.ReferenciaDao;

/**
 * @author Sebastián Gómez 
 *
 *Clase que representa el Mundo con sus atributos y métodos de la funcionalidad
 * Referencia
 */
public class Referencia 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(Referencia.class);
	
	
	/**
	 * DAO para el manejo de ReferenciaDao
	 */
	private ReferenciaDao referenciaDao=null;
	
	/**
	 * Variable donde se almacena el consecutivo de la referencia
	 */
	private String consecutivoReferencia = "";
	
	//********CONSTRUCTORES E INICIALIZADORES********************
	
	/**
	 * Constructor
	 */
	public Referencia() {
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * método para inicializar los datos
	 *
	 */
	public void clean()
	{
		this.consecutivoReferencia = "";
	}
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (referenciaDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			referenciaDao = myFactory.getReferenciaDao();
		}	
	}
	
	/**
	 * Método para obtener una instancia del DAO de referencia
	 * @return
	 */
	public static ReferenciaDao referenciaDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getReferenciaDao();
	}
	//************METODOS****************************************
	/**
	 * Método que consulta la referencia
	 * Nota * Si se envia el parametro numeroReferencia lleno, entonces la consultar solo se hará por este parámetro
	 * y se ignoararán los demás
	 */
	public HashMap cargar(Connection con,int codigoPaciente,int idIngreso,String tipoReferencia,String numeroReferencia)
	{
		HashMap campos = new HashMap();
		campos.put("codigoPaciente", codigoPaciente+"");
		campos.put("ingreso", idIngreso+"");
		campos.put("tipoReferencia", tipoReferencia);
		campos.put("numeroReferencia",numeroReferencia);
		return referenciaDao.cargar(con, campos);
	}
	
	/**
	 * Método que guarda la informacion de la referencia
	 * @param con
	 * @param referencia
	 * @return
	 */
	public boolean guardar(Connection con,HashMap referencia)
	{
		this.consecutivoReferencia = referenciaDao.guardar(con, referencia);
		//Se verifica éxito del proceso
		if(!consecutivoReferencia.equals(""))
			return true;
		else
			return false;
	}
	
	/**
	 * Método implementado para realizar la búsqueda de las instituciones SIRC
	 * @param con
	 * @param tipoReferencia
	 * @param institucion
	 * @return
	 */
	public HashMap busquedaInstitucionesSirc(Connection con,String tipoReferencia,int institucion)
	{
		HashMap campos = new HashMap();
		campos.put("tipoReferencia",tipoReferencia);
		campos.put("institucion", institucion+"");
		return referenciaDao.busquedaInstitucionesSirc(con, campos);
	}
	
	/**
	 * Método que realiza la anulacion de referencias externas caducadas
	 * @param con
	 * @param codigoPaciente
	 * @param horasCaducidad
	 * @param usuario
	 * @return
	 */
	public static boolean anularReferenciasExternasCaducadas(Connection con,String codigoPaciente,String horasCaducidad,String loginUsuario)
	{
		//************CALCULO DE LA FECHA BASE************************************
		//Se pasa las horas caducadas en minutos
		int minutos = 0;
		String[] vector = horasCaducidad.split(":");
		logger.info("HORAS CADUCADAS=> "+horasCaducidad);
		minutos = Integer.parseInt(vector[1]) + (Integer.parseInt(vector[0])*60);
		logger.info("minutos calculados=> "+minutos);
		String[] fechaHoraBase = UtilidadFecha.incrementarMinutosAFechaHora(UtilidadFecha.getFechaActual(), UtilidadFecha.getHoraActual(), -minutos, false);
		//*******************************************************************************************************************
		logger.info("fecha hora basae=> "+fechaHoraBase[0]+" - "+fechaHoraBase[1]);
		//*******************SE HACE LLAMADO A LA ANULACION DE LAS REFERENCIAS EXTERNAS CADUCADAS***********************
		HashMap campos = new HashMap();
		campos.put("codigoPaciente",codigoPaciente);
		campos.put("fechaBase",UtilidadFecha.conversionFormatoFechaABD(fechaHoraBase[0]));
		campos.put("horaBase",fechaHoraBase[1]);
		campos.put("usuario",loginUsuario);
		
		boolean exito = referenciaDao().anularReferenciasExternasCaducadas(con, campos);
		//*************************************************************************************************************
		
		return exito;
	}
	//***********GETTERS & SETTERS********************************************
	/**
	 * @return the consecutivoReferencia
	 */
	public String getConsecutivoReferencia() {
		return consecutivoReferencia;
	}
	/**
	 * @param consecutivoReferencia the consecutivoReferencia to set
	 */
	public void setConsecutivoReferencia(String consecutivoReferencia) {
		this.consecutivoReferencia = consecutivoReferencia;
	}
}
