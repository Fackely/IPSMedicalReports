package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.GrupoEspecialArticulosDao;

public class GrupoEspecialArticulos 
{

	
	/**
	 * 
	 */
	private GrupoEspecialArticulosDao objetoDao;
	
	private HashMap mapaGrupo;
	
	
	/**
	 * 
	 */
	public GrupoEspecialArticulos() 
	{
		init(System.getProperty("TIPOBD"));
	}
	
	
	/**
	 * 
	 */
	public void reset()
	{
		this.mapaGrupo = new HashMap();
		this.mapaGrupo.put("numRegistros", "0");
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
			objetoDao=myFactory.getGrupoEspecialArticulosDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
			
		
	}

	
	/**
	 * 
	 * @return
	 */
	public GrupoEspecialArticulosDao getObjetoDao() {
		return objetoDao;
	}

	/**
	 * 
	 * @param objetoDao
	 */
	public void setObjetoDao(GrupoEspecialArticulosDao objetoDao) {
		this.objetoDao = objetoDao;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaGrupo() {
		return mapaGrupo;
	}

	/**
	 * 
	 * @param mapaGrupo
	 */
	public void setMapaGrupo(HashMap mapaGrupo) {
		this.mapaGrupo = mapaGrupo;
	}

	
	/**
	 * 
	 * @param con
	 * @param convertirAEntero
	 * @return
	 */
	public boolean eliminarRegistro(Connection con, int codigo) 
	{
		return objetoDao.eliminarRegistro(con, codigo);
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
	public HashMap consultarGrupoEspecifico(Connection con, int codigoGrupo) 
	{
		return objetoDao.consultarMotivoEspecifico(con, codigoGrupo);
	}

	/**
	 * 
	 * @param con
	 * @return
	 */
	public HashMap consultarGruposExistentes(Connection con) 
	{
		return objetoDao.consultarMotivosExistentes(con);
	}
	
	
}
