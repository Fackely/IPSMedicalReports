package com.princetonsa.mundo.consultaExterna;

import java.sql.Connection; 
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.consultaExterna.MotivosCancelacionCitaDao;

public class MotivosCancelacionCita 
{
	
	
	/**
	 * 
	 */
	private MotivosCancelacionCitaDao objetoDao;
	
	/**
	 * 
	 */
	private HashMap mapaMotivo;
	
	
	/**
	 * 
	 *
	 */
	public MotivosCancelacionCita() 
	{
		init(System.getProperty("TIPOBD"));
		this.reset();
	}
	
	
	/**
	 *  
	 *
	 */
	public void reset()
	{
		this.mapaMotivo=new HashMap();
		this.mapaMotivo.put("numRegistros", "0");
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
			objetoDao=myFactory.getMotivosCancelacionCitaDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
			
		
	}
	
	
	/**
	 * 
	 * @return
	 */
	public HashMap getMapaMotivo() {
		return mapaMotivo;
	}

	/**
	 * 
	 * @param mapaMotivo
	 */
	public void setMapaMotivo(HashMap mapaMotivo) {
		this.mapaMotivo = mapaMotivo;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param string
	 * @return
	 */
	public boolean eliminarRegistro(Connection con, int codigoMotivo) 
	{
		return objetoDao.eliminarRegistro(con, codigoMotivo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificar(Connection con, HashMap vo) 
	{
		return objetoDao.modificar(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertar(Connection con, HashMap vo) 
	{
		return objetoDao.insertar(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param codigoMotivo
	 * @return
	 */
	public HashMap consultarMotivoEspecifico(Connection con, int codigoMotivo) 
	{
		return objetoDao.consultarMotivoEspecifico(con, codigoMotivo);
	}

	/**
	 * @param con
	 */
	public HashMap consultarMotivosExistentes(Connection con) 
	{
		return objetoDao.consultarMotivosExistentes(con,new HashMap());		
	}
	
	/**
	 * @param con
	 */
	public HashMap consultarMotivosExistentes(Connection con, String codigosInsertados) 
	{
		HashMap parametros = new HashMap();
		parametros.put("codigosInsertados",codigosInsertados);
		return objetoDao.consultarMotivosExistentes(con,parametros);		
	}
	
	//************INICIO TAREA 61450*******************
	/**
	 * @param con
	 */
	public HashMap consultarMotivosExistentes(Connection con, String codigosInsertados, String tipoCancelacion) 
	{
		HashMap parametros = new HashMap();
		parametros.put("codigosInsertados",codigosInsertados);
		parametros.put("tipoCancelacion",tipoCancelacion);
		return objetoDao.consultarMotivosExistentes(con,parametros);		
	}
	//************FIN TAREA 61450**********************
	
}