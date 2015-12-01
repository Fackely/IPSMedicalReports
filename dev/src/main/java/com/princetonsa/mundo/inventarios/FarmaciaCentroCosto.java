package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.FarmaciaCentroCostoDao;

/**
 * mundo de Farmacia_x_Centro_Costo
 * @author lgchavez
 *
 */
public class FarmaciaCentroCosto
{
	//--- Atributos
	private static Logger logger = Logger.getLogger(Secciones.class);
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static FarmaciaCentroCostoDao FarmaciaCentroCostoDao;
	
	/**
	 * 
	 * */
	private int codigo;
	
	/**
	 * 
	 */
	private int centroAtencion;
	
	/**
	 * 
	 */
	private int centroCosto;
	
	/**
	 * 
	 */
	private int institucion;
	
	/**
	 * 
	 */
	private String usuarioModifica;
	
	/**
	 * 
	 */
	private int codigo_farmacia_cc;
	
	/**
	 * 
	 */
	private int farmacia;
	
	
	
	/**
	 * 
	 *
	 */
	public FarmaciaCentroCosto()
	{
		this.reset();
		this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * 
	 *
	 */
	private void reset() 
	{
		this.codigo = ConstantesBD.codigoNuncaValido;
		this.centroAtencion=ConstantesBD.codigoNuncaValido;
		this.centroCosto=ConstantesBD.codigoNuncaValido;
		this.codigo_farmacia_cc=ConstantesBD.codigoNuncaValido;
		this.farmacia=ConstantesBD.codigoNuncaValido;
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.usuarioModifica="";
		this.farmacia=ConstantesBD.codigoNuncaValido;
		
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
			FarmaciaCentroCostoDao = myFactory.getFarmaciaCentroCostoDao();
			wasInited = (FarmaciaCentroCostoDao != null);
		}
		return wasInited;
	}
	
/**
 * 
 * @param con
 * @param farmaciacentrocosto
 * @return
 */
	public static boolean insertar(Connection con, FarmaciaCentroCosto farmaciacentrocosto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFarmaciaCentroCostoDao().insertar(con, farmaciacentrocosto);
	}
	
	
/**
 * 
 * @param con
 * @param farmaciacentrocosto
 * @return
 */
	public static boolean insertartrans(Connection con, FarmaciaCentroCosto farmaciacentrocosto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFarmaciaCentroCostoDao().insertartrans(con, farmaciacentrocosto);
	}
	
	/**
	 * 
	 * @param con
	 * @param farmaciacentrocosto
	 * @return
	 */
	public static boolean insertardet(Connection con, FarmaciaCentroCosto farmaciacentrocosto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFarmaciaCentroCostoDao().insertardet(con, farmaciacentrocosto);
	}

/**
 * 
 * @param con
 * @param codigo
 * @param farmacia
 * @return
 */
	public static boolean eliminar(Connection con, int codigo, int farmacia)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFarmaciaCentroCostoDao().eliminar(con, codigo, farmacia);
	}
	
/**
 * 
 * @param con
 * @param farmaciacentrocosto
 * @return
 */
	public static HashMap consultar(Connection con, FarmaciaCentroCosto farmaciacentrocosto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getFarmaciaCentroCostoDao().consultar(con, farmaciacentrocosto);
	}

	
	/**
	 * 
	 * @return
	 */
	public static FarmaciaCentroCostoDao getFarmaciaCentroCostoDao() {
		return FarmaciaCentroCostoDao;
	}

	/**
	 * 
	 * @param farmaciaCentroCostoDao
	 */
	public static void setFarmaciaCentroCostoDao(
			FarmaciaCentroCostoDao farmaciaCentroCostoDao) {
		FarmaciaCentroCostoDao = farmaciaCentroCostoDao;
	}

	/**
	 * 
	 * @return
	 */
	public int getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * 
	 * @param centroAtencion
	 */
	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * 
	 * @return
	 */
	public int getCentroCosto() {
		return centroCosto;
	}

	/**
	 * 
	 * @param centroCosto
	 */
	public void setCentroCosto(int centroCosto) {
		this.centroCosto = centroCosto;
	}

	
	/**
	 * 
	 * @return
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * 
	 * @param codigo
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	
	/**
	 * 
	 * @return
	 */
	public int getCodigo_farmacia_cc() {
		return codigo_farmacia_cc;
	}

	/**
	 * 
	 * @param codigo_farmacia_cc
	 */
	public void setCodigo_farmacia_cc(int codigo_farmacia_cc) {
		this.codigo_farmacia_cc = codigo_farmacia_cc;
	}

	/**
	 * 
	 * @return
	 */
	public int getFarmacia() {
		return farmacia;
	}

	/**
	 * 
	 * @param farmacia
	 */
	public void setFarmacia(int farmacia) {
		this.farmacia = farmacia;
	}

	/**
	 * 
	 * @return
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * 
	 * @param institucion
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	/**
	 * 
	 * @return
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}

	/**
	 * 
	 * @param usuarioModifica
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}
	
		
}



