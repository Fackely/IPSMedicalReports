/*
 * Creado en May 10, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.CentroCostoGrupoServicioDao;
import com.princetonsa.dao.DaoFactory;

public class CentroCostoGrupoServicio
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(CentroCostoGrupoServicio.class);
	
	/**
	 * Interfaz para acceder a la fuente de datos
	 */
	private CentroCostoGrupoServicioDao  centroCostoGrupoServicioDao = null;
	
//	---------------------------------------------------DECLARACIÓN DE LOS ATRIBUTOS-----------------------------------------------------------//
	
	/**
	 * Campo que guarda el centro de atención seleccionado
	 */
	private int centroAtencion;

	/**
	 * Mapa que contiene la información de los centros de costo x grupos servicios
	 */
	private HashMap mapa;
	
//	---------------------------------------------------FIN DE LA DECLARACIÓN DE LOS ATRIBUTOS-----------------------------------------------------------//
	
	/**
	 * Constructor de la clase, inicializa en vacío todos los atributos
	 */
	public CentroCostoGrupoServicio ()
	{
		reset();
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Este método inicializa los atributos de la clase con valores vacíos
	 */
	public void reset()
	{
		this.mapa=new HashMap();
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

		if (myFactory != null)
		{
			centroCostoGrupoServicioDao = myFactory.getCentroCostoGrupoServicioDao();
			wasInited = (centroCostoGrupoServicioDao != null);
		}
		return wasInited;
	}
	
	/**
	 * Método que cargar los centros de costo x grupos de servicio parametrizados en la institución 
	 * para el centro de atención seleccionado
	 * @param con
	 * @param centroAtencion
	 * @return HashMap
	 */
	public HashMap consultarCentrosCostoGrupoServicio(Connection con, int centroAtencion)
	{
		return centroCostoGrupoServicioDao.consultarCentrosCostoGrupoServicio(con, centroAtencion);
	}
	
	
	/**
	 * Método que inserta los centros de costo x grupo de servicios al centro de atención seleccionado
	 * @param con
	 * @return
	 * @throws SQLException
	 */
	public boolean insertarCentroCostoGrupoServicio(Connection con) throws SQLException {
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp=-1;
		boolean error=false;
		
		if (centroCostoGrupoServicioDao==null) {
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (CentroCostoGrupoServicioDao - insertarCentroCostoGrupoServicio )");
		}
		
		//Iniciamos la transacción
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
		if (this.getMapa() != null) {
			int numRegistros=Integer.parseInt(this.getMapa("numRegistros")+"");

			for (int i=0; i<numRegistros; i++) {
				String estaGrabado=(String)this.getMapa("esta_grabado_"+i);

				int grupoServicio=Integer.parseInt(this.getMapa("codigo_gservicio_"+i)+"");
				int centroCosto=Integer.parseInt(this.getMapa("codigo_ccosto_"+i)+"");
				
				int consecutivo;// = ConstantesBD.codigoNuncaValido;

				//logger.info("<<<<<<<<< consecutivo: " + consecutivo);
				logger.info("<<<<<<<<< Mpa consecu: " + this.getMapa("consecutivo_"+i));
				
				
				if( (this.getMapa("consecutivo_"+i).toString() != null) && (this.getMapa("consecutivo_"+i).toString() != "")) {
					consecutivo = Integer.parseInt(this.getMapa("consecutivo_"+i)+"");
					logger.info("<<<<<<<<< Hay valor del consecutivo");
				}
				else
					this.setMapa("consecutivo_"+i, 0);
				
				//-----------Se verifica si el registro está grabado para así insertarlo (1 -> Esta grabado,  0->Está grabado)---------//
				if (estaGrabado.equals("0")) {
					/*logger.info("\n CENTRO ATENCION "+this.centroAtencion+"\n");
					logger.info("\n GRUPO SERVICIO "+grupoServicio+"\n");
					logger.info("\n CENTRO COSTO "+centroCosto+"\n");*/
								
					resp = centroCostoGrupoServicioDao.insertarCentroCostoGrupoServicio (con, this.centroAtencion, grupoServicio, centroCosto, Integer.parseInt(this.getMapa("consecutivo_"+i)+""));
				}
				//si no actualizo el consecutivo
				else {
					//if(consecutivo != ConstantesBD.codigoNuncaValido)
						resp = centroCostoGrupoServicioDao.actualizarCentroCostoGrupoServicio (con, this.centroAtencion, grupoServicio, centroCosto, Integer.parseInt(this.getMapa("consecutivo_"+i)+""));
					//else
						//resp = 0;
				}

				if (resp <0) {
					error=true;
					break;
				}
				
			}//for
				
		}//if mapa!=null

		//Se finaliza la transacción cuando hay error en una inserción de datos o no se logró inicializar la transacción
		if (!inicioTrans || error) {
			myFactory.abortTransaction(con);
			return false;
		}
		else {
		    myFactory.endTransaction(con);
		}
		
		return true;	
	}
	
	/**
	 * Método que elimina el registro seleccionado de centros costo x grupo servicio
	 * @param con
	 * @param centroAtencionElim
	 * @param grupoServicioElim
	 * @param centroCostoElim
	 * @return
	 * @throws SQLException
	 */
	public boolean eliminarCentroCostoGrupoServicio(Connection con, int centroAtencionElim, int grupoServicioElim, int centroCostoElim) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		int resp=-1;
				
		if (centroCostoGrupoServicioDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (CentroCostoGrupoServicioDao - eliminarCentroCostoGrupoServicio )");
		}
		
		//Iniciamos la transacción
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
		resp = centroCostoGrupoServicioDao.eliminarCentroCostoGrupoServicio (con, centroAtencionElim, grupoServicioElim, centroCostoElim);
		
		//Se finaliza la transacción cuando hay error en una inserción de datos o no se logró inicializar la transacción
		if (!inicioTrans || resp<0)
			{
				myFactory.abortTransaction(con);
				return false;
			}
		else
			{
			    myFactory.endTransaction(con);
			}
		
		return true;	
	}
	
//	---------------------------------------- SETS Y GETS ---------------------------------------------------//
	
	/**
	 * @return Retorna the centroAtencion.
	 */
	public int getCentroAtencion()
	{
		return centroAtencion;
	}

	/**
	 * @param centroAtencion The centroAtencion to set.
	 */
	public void setCentroAtencion(int centroAtencion)
	{
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return Retorna the mapa.
	 */
	public HashMap getMapa()
	{
		return mapa;
	}

	/**
	 * @param mapa The mapa to set.
	 */
	public void setMapa(HashMap mapa)
	{
		this.mapa = mapa;
	}
	
	/**
	 * @return Retorna mapa.
	 */
	public Object getMapa(Object key) {
		return mapa.get(key);
	}
	/**
	 * @param Asigna dato.
	 */
	public void setMapa(Object key, Object dato) {
		this.mapa.put(key, dato);
	}

	

}
