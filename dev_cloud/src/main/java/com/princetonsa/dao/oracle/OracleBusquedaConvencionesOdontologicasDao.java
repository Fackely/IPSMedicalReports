package com.princetonsa.dao.oracle;

import java.util.ArrayList;

import util.InfoDatosStr;

import com.princetonsa.dao.BusquedaConvencionesOdontologicasDao;
import com.princetonsa.dao.sqlbase.SqlBaseBusquedaConvencionesOdontologicasDao;

public class OracleBusquedaConvencionesOdontologicasDao implements BusquedaConvencionesOdontologicasDao {

	@Override
	public ArrayList<InfoDatosStr> consultarConvencionesOdontologicas(int codigoInstitucionInt, boolean porTipo, String tipo) {
		
		return SqlBaseBusquedaConvencionesOdontologicasDao.consultarConvencionesOdontologicas(codigoInstitucionInt,porTipo,tipo);
	}

	@Override
	public ArrayList<InfoDatosStr> consultarImagenesConvencionesOdontologicas() {
		
		return SqlBaseBusquedaConvencionesOdontologicasDao.consultarImagenesConvencionesOdontologicas();
	}

	@Override
	public ArrayList<InfoDatosStr> consultarTramasConvencionesOdontologicas() {
		
		return SqlBaseBusquedaConvencionesOdontologicasDao.consultarTramasConvencionesOdontologicas();
	}
	
	/**
	 * cadena que consulta las imagenes base de una institucion
	 * @param institucion
	 * @return
	 */
	public ArrayList<InfoDatosStr> consultarImagenesBase(int institucion)
	{
		return SqlBaseBusquedaConvencionesOdontologicasDao.consultarImagenesBase(institucion);
	}
}
