package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.ViasIngresoDao;

public class ViasIngreso {
	
	/**
	 * Intefaz para acceder y comunicarse con la fuente de datos
	 */
	private ViasIngresoDao objetoDao;
	
	/**
	 * Mapa de Vias de Ingreso
	 */
	private HashMap viasIngresoMap;
	
	/**
	 * Mapa de Garantia Paciente
	 * */
	public HashMap consultarGarantiaPaciente(Connection con, int codigo)
	{
		return objetoDao.consultarGarantiaPaciente(con,codigo);
	}
	
	/**
	 * Número de tipo paciente por vía de ingreso
	 * */
	public int consultarNoTipoPacViaIng(Connection con, int codigo)
	{
		return objetoDao.consultarNoTipoPacViaIng(con,codigo);
	}
	
	/**
	 *  Constructor de la clase
	 */
	public ViasIngreso() 
	{
		init(System.getProperty("TIPOBD"));
		this.reset();
	}
	
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
			objetoDao=myFactory.getViasIngresoDao();
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
		viasIngresoMap=new HashMap();
    	viasIngresoMap.put("numRegistros","0");
   	}
	
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean existeVerificacionDerechosViaIngreso( Connection con, int codigoViaIngreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getViasIngresoDao().existeVerificacionDerechosViaIngreso(con, codigoViaIngreso);
	}
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean existeVerificacionCierreNotasEnferViaIngreso( Connection con, int codigoViaIngreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getViasIngresoDao().existeVerificacionCierreNotasEnferViaIngreso(con, codigoViaIngreso);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean existeVerificacionEpicrisisFinalizadaViaIngreso( Connection con, int codigoViaIngreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getViasIngresoDao().existeVerificacionEpicrisisFinalizadaViaIngreso(con, codigoViaIngreso);
	}
	
	/**
	 * Insertar
	 * */
	public boolean insertar(Connection con,HashMap vo)
	{
		return objetoDao.insertar(con, vo);
	}
	
	/**
	 * Consultar vias de ingreso especifico
	 * */
	public HashMap consultarViasIngresoEspecifico(Connection con,int codigo)
	{
		HashMap vo=new HashMap();
		vo.put("codigo", codigo);
		return (HashMap)objetoDao.consultarViasIngresoExistentes(con,vo).clone();
	}
	
	/**
	 * Consultar
	 * */
	public void consultarViasIngresoExistentes(Connection con) 
	{
		HashMap vo=new HashMap();
		//vo.put("codigo", codigo);
		this.viasIngresoMap=objetoDao.consultarViasIngresoExistentes(con,vo);
	}
	
	/**
	 * Modificar
	 * */
	public boolean modificar(Connection con,HashMap vo)
	{
		return objetoDao.modificar(con, vo);
	}
	
	/**
	 * eliminar
	 * */
	public boolean eliminarRegistro(Connection con, int codigo)
	{
		return objetoDao.eliminarRegistro(con,codigo);
	}

	public HashMap getViasIngresoMap() {
		return viasIngresoMap;
	}

	public void setViasIngresoMap(HashMap viasIngresoMap) {
		this.viasIngresoMap = viasIngresoMap;
	}
	
	public boolean insertarGarantiaPaciente(Connection con, HashMap vo)
	{
		return objetoDao.insertarGarantiaPaciente(con,vo);
	}
	
	public boolean modificarGarantiaPaciente(Connection con, HashMap vo)
	{
		return objetoDao.modificarGarantiaPaciente(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param viaIngreso
	 * @return
	 */
	public static int obtenerConvenioDefecto(Connection con, int viaIngreso) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getViasIngresoDao().obtenerConvenioDefecto(con,viaIngreso);
	}

}
