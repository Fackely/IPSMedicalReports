package com.princetonsa.mundo.glosas;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.glosas.DetalleGlosasDao;

public class DetalleGlosas
{
	Logger logger = Logger.getLogger(DetalleGlosas.class);
	private static DetalleGlosasDao getDetalleGlosasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleGlosasDao();
	}
}