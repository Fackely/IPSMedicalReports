package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.ValoresTipoReporteDao;

public class ValoresTipoReporte 
{

	
	/**
	 * Intefaz para acceder y comunicarse con la fuente de datos
	 */
	private ValoresTipoReporteDao objetoDao;
	
	
	/**
	 * 
	 */
	public ValoresTipoReporte()
	{
		init(System.getProperty("TIPOBD"));
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
			objetoDao=myFactory.getValoresTipoReporteDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
			
		
	}


	/**
	 * 
	 * @param con
	 * @param codigoReporte
	 * @return
	 */
	public HashMap consultarInfoTipoReporte(Connection con, String codigoReporte) 
	{
		return objetoDao.consultarInfoTipoReporte(con, codigoReporte);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoReporte
	 * @return
	 */
	public HashMap consultarParametrizacion(Connection con, String codigoReporte) 
	{
		return objetoDao.consultarParametrizacion(con, codigoReporte);
	}

	/**
	 * 
	 * @param con
	 * @param string
	 * @return
	 */
	public boolean eliminarRegistro(Connection con, String codigo) 
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
	

	 /*** Método implementado para verificar si existe parametrizacion de los valores
	 * de un tipo de reporte específico
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean existeParametrizacionValoresTipoReporte(Connection con,int tipoReporte,int codigoInstitucion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoTipoReporte", tipoReporte);
		campos.put("codigoInstitucion", codigoInstitucion);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValoresTipoReporteDao().existeParametrizacionValoresTipoReporte(con, campos);
	}
	 
}
