/*
 * Julio 16 del 2007
 */
package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.UtilidadTexto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.CondicionesXServicioDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseCondicionesXServiciosDao;


/**
 * @author Andrés Eugenio Silva Monsalve 
 *
 *Clase que representa el Mundo con sus atributos y métodos de la funcionalidad
 * Parametrización de Condiciones por Servicios
 */
public class CondicionesXServicios 
{
	
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(CondicionesXServicios.class);
	
	/**
	 * DAO para el manejo de CondicionesXServicioDao
	 */
	private CondicionesXServicioDao condiDao=null;
	
	/**
	 * Mapa de los servicios
	 */
	private HashMap mapaServicios = new HashMap();
	
	/**
	 * Número de los servicios
	 */
	private int numServicios;
	
	/**
	 * Mapa de los condiciones
	 */
	private HashMap mapaCondiciones = new HashMap();
	
	/**
	 * Número de los artículos
	 */
	private int numCondiciones;
	
	//*****************************************************************
	//**********INICIALIZADORES & CONSTRUCTORES***********************
	/**
	 * Constructor
	 */
	public CondicionesXServicios() 
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
		this.mapaServicios = new HashMap();
		this.numServicios = 0;
		this.mapaCondiciones = new HashMap();
		this.numCondiciones = 0;
	}
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (condiDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			condiDao = myFactory.getCondicionesXServicioDao();
		}	
	}
	
	/**
	 * Método que retorna el DAO instanciado de Condiciones por Servicio
	 * @return
	 */
	public static CondicionesXServicioDao condiDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCondicionesXServicioDao();
	}
	
	//****************************************************************************
	//********************MÉTODOS**************************************************
	
	/**
	 * Método implementado para consultar el consecutivo x servicio
	 * de la parametrizacion de condiciones por servicio
	 * @param con
	 * @param codigoServicio
	 * @param institucion
	 * @return
	 */
	public static int consultarConsecutivoXServicio(Connection con,String codigoServicio,String institucion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoServicio",codigoServicio);
		campos.put("institucion",institucion);
		return condiDao().consultarConsecutivoXServicio(con, campos);
	}
	
	/**
	 * Método implementado para consultar la parametrizacion de una condicion de servicios por consecutivo
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public void consultarCondicionServiciosXConsecutivo(Connection con,int consecutivo)
	{
		HashMap campos = new HashMap();
		campos.put("consecutivo",consecutivo+"");
		//logger.info("\n entre a consultarCondicionServiciosXConsecutivo"+consecutivo);
		HashMap respuesta = SqlBaseCondicionesXServiciosDao.consultarCondicionesXServicioXConsecutivo(con, campos);
		
		this.mapaServicios = (HashMap)respuesta.get("mapaServicios");
		this.mapaCondiciones = (HashMap)respuesta.get("mapaCondiciones");
		
		this.numServicios = Integer.parseInt(this.mapaServicios.get("numRegistros").toString());
		this.numCondiciones = Integer.parseInt(this.mapaCondiciones.get("numRegistros").toString());
		
	}
	
	
	/**
	 * Método implementado para guardar la condicion de servicios
	 * @param con
	 * @param campos
	 * @return
	 */
	public int guardar(Connection con,int consecutivo,String institucion)
	{
		HashMap campos = new HashMap();
		campos.put("consecutivo",consecutivo+"");
		campos.put("institucion", institucion);
		campos.put("mapaServicios",this.mapaServicios);
		campos.put("mapaCondiciones",this.mapaCondiciones);
		campos.put("numServicios",this.numServicios+"");
		campos.put("numCondiciones",this.numCondiciones+"");
		
		return condiDao.guardar(con, campos);
	}
	
	/**
	 * Método que compara los datos de la base de datos y los de la aplicacion para verificar
	 * si una condicion de servicios fue modificada
	 * @param con
	 * @param consecutivo
	 *  @param mapaServiciosNuevo
	 * @param numServiciosNuevo
	 * @param mapaCondicionesNuevo 
	 * @param numCondicionesNuevo  
	 * @return
	 */
	public HashMap huboModificacion(Connection con, int consecutivo,HashMap mapaServiciosNuevo,int numServiciosNuevo, HashMap mapaCondicionesNuevo, int numCondicionesNuevo) 
	{
		HashMap mapa = new HashMap();
		boolean modificado = false;
		
		logger.info("\n\n ************ entre a huboModificacion VALORES *************");
		logger.info("\n consecutivo --> "+consecutivo);
		logger.info("\n numServiciosNuevo --> "+numServiciosNuevo);
		logger.info("\n numCondicionesNuevo --> "+numCondicionesNuevo);
		logger.info("\n mapaServiciosNuevo --> "+mapaServiciosNuevo);
		logger.info("\n mapaCondicionesNuevo --> "+mapaCondicionesNuevo);
		
		//se consulta la informacion de la hoja de gastos
		consultarCondicionServiciosXConsecutivo(con, consecutivo);
	
		//se verifica que el numero de servicios o el número de condiciones no haya cambiado
		
		if(obtenerNumRegistrosAIngresar(mapaServiciosNuevo, numServiciosNuevo)!=this.numServicios||
			obtenerNumRegistrosAIngresar(mapaCondicionesNuevo, numCondicionesNuevo)!=this.numCondiciones)
		{
			logger.info("\n **********1*************");
			modificado = true;
		}
		else
		{
			logger.info("\n **********2*************");
			//se verifica que tal vez se hayan modificado las cantidades de los condiciones o hay condiciones diferentes
			boolean encontrado = false; //avisa si la condicion se encuentra en el mapa nuevo
			for(int i=0;i<this.numCondiciones;i++)
			{
				encontrado = false;
				for(int j=0;j<numCondicionesNuevo;j++)
				{
					logger.info("\n **********3*************");
					logger.info("i->"+i+" j->"+j+" eliminar -->"+mapaCondicionesNuevo.get("eliminar_"+j)+" CodigoArticulo -->"+mapaCondiciones.get("codigoCondicion_"+i)+"  codigoNuevo -->"+mapaCondicionesNuevo.get("codigoCondicion_"+j));
					if(!UtilidadTexto.getBoolean(mapaCondicionesNuevo.get("eliminar_"+j).toString())&&
						mapaCondiciones.get("codigoCondicion_"+i).toString().equals(mapaCondicionesNuevo.get("codigoCondicion_"+j).toString()))
					{
						logger.info("\n **********4*************");
						if((mapaCondiciones.get("descripcionCondicion_"+i)+"").equals(mapaCondicionesNuevo.get("descripcionCondicion_"+j)+""))
						{
							logger.info("\n **********5*************");
							modificado = true;
							i = this.numCondiciones;
							j = numCondicionesNuevo;
						}
						else
						{
							encontrado = true;
						}
						
					}	
				}
				
				//si el articulo no esta en el mapa nuevo quiere decir que hubo modificacion
				if(encontrado == false)
				{
					modificado = true;
					i = this.numCondiciones;
				}
			}
		}
		
		if(modificado)
		{
			mapa.put("modificado", "true");
			mapa.put("mapaServicios",this.mapaServicios);
			mapa.put("numServicios",this.numServicios+"");
			mapa.put("mapaCondiciones",this.mapaCondiciones);
			mapa.put("numCondiciones",this.numCondiciones);
		}
		else
			mapa.put("modificado", "false");
		logger.info("\n\n ************ entre a huboModificacion *************");
		logger.info("el mapa es "+mapa);
		return mapa;
	}
	
	/**
	 * Método que obtiene el número de servicios que se van a eliminar
	 * @param mapa
	 * @param numRegistros
	 * @return
	 */
	private int obtenerNumRegistrosAIngresar(HashMap mapa, int numRegistros) 
	{
		int cantidad = 0;
		
		for(int i=0;i<numRegistros;i++)
		{
			if(!UtilidadTexto.getBoolean(mapa.get("eliminar_"+i).toString()))
				cantidad ++;
		}
		
		return cantidad;
	}
	
	/**
	 * Método que consulta las condiciones para la toma del servicio
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> obtenerCondicionesTomaXServicio(Connection con,int codigoServicio,int institucion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoServicio", codigoServicio+"");
		campos.put("institucion",institucion+"");
		return condiDao().obtenerCondicionesTomaXServicio(con, campos);
	}
	
	//****************************************************************************
	//********************GETTERS & SETTERS**************************************************
	
	/**
	 * @return the mapaCondiciones
	 */
	public HashMap getMapaCondiciones() {
		return mapaCondiciones;
	}
	/**
	 * @param mapaCondiciones the mapaCondiciones to set
	 */
	public void setMapaCondiciones(HashMap mapaCondiciones) {
		this.mapaCondiciones = mapaCondiciones;
	}
	/**
	 * @return the mapaServicios
	 */
	public HashMap getMapaServicios() {
		return mapaServicios;
	}
	/**
	 * @param mapaServicios the mapaServicios to set
	 */
	public void setMapaServicios(HashMap mapaServicios) {
		this.mapaServicios = mapaServicios;
	}
	/**
	 * @return the numCondiciones
	 */
	public int getNumCondiciones() {
		return numCondiciones;
	}
	/**
	 * @param numCondiciones the numCondiciones to set
	 */
	public void setNumCondiciones(int numCondiciones) {
		this.numCondiciones = numCondiciones;
	}
	/**
	 * @return the numServicios
	 */
	public int getNumServicios() {
		return numServicios;
	}
	/**
	 * @param numServicios the numServicios to set
	 */
	public void setNumServicios(int numServicios) {
		this.numServicios = numServicios;
	}
	
	
}
