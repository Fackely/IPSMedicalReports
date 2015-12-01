package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.MotivosSircDao;

public class MotivosSirc
{
	
	private MotivosSircDao objetoDao;
	
	private HashMap mapaMotivosSircTemp;
	
	/**
	 * 
	 *
	 */
	public MotivosSirc()
	{
		this.reset();
		this.init(System.getProperty("TIPOBD"));
	}

	/**
	 * 
	 * @param property
	 */
	private void init(String tipoBD)
	{
		if (objetoDao == null) 
		{
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			objetoDao = myFactory.getMotivosSircDao();
		}	
	}

	/**
	 * 
	 *
	 */
	private void reset()
	{
		this.mapaMotivosSircTemp=new HashMap();
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarMotivoSirc(Connection con,HashMap vo)
	{
		return objetoDao.insertarMotivoSirc(con, vo);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarMotivoSirc(Connection con,HashMap vo)
	{
		return objetoDao.modificarMotivoSirc(con, vo);
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public HashMap consultarMotivoSirc(Connection con,HashMap vo)
	{
		return objetoDao.consultarMotivoSirc(con, vo);
	}

	/**
	 * 
	 * @param con
	 * @param string
	 * @return
	 */
	public boolean eliminarRegistro(Connection con, String consecutivo)
	{
		return objetoDao.eliminarRegistro(con,consecutivo);
	}

	/**
	 * 
	 * @param con
	 * @param consecutivo
	 */
	public HashMap consultarMotivoSircEspecifico(Connection con, String consecutivo)
	{
		HashMap vo=new HashMap();
		vo.put("consecutivo", consecutivo);
		return objetoDao.consultarMotivoSirc(con, vo);
		
	}

	public HashMap getMapaMotivosSircTemp()
	{
		return mapaMotivosSircTemp;
	}

	public void setMapaMotivosSircTemp(HashMap mapaMotivosSircTemp)
	{
		this.mapaMotivosSircTemp = mapaMotivosSircTemp;
	}
}
