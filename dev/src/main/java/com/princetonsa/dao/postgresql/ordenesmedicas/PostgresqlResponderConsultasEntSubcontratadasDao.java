package com.princetonsa.dao.postgresql.ordenesmedicas;

import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.ordenesmedicas.ResponderConsultasEntSubcontratadasDao;
import com.princetonsa.dao.sqlbase.ordenesmedicas.SqlBaseResponderConsultasEntSubcontratadasDao;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;

public class PostgresqlResponderConsultasEntSubcontratadasDao implements
		ResponderConsultasEntSubcontratadasDao {

	@Override
	public ArrayList<DtoSolicitudesSubCuenta> obtenerListadoSolicitudes(int codigoPersona, int entidadSubcontratada, String tar,String especialidadesProf) {
		
		return SqlBaseResponderConsultasEntSubcontratadasDao.obtenerListadoSolicitudes(codigoPersona, entidadSubcontratada, tar, especialidadesProf);
	}

	@Override
	public ArrayList<DtoSolicitudesSubCuenta> obtenerSolicitudesXRango(HashMap parametrosBusqueda, String tar) {
		
		return SqlBaseResponderConsultasEntSubcontratadasDao.obtenerSolicitudesXRango(parametrosBusqueda,tar);
	}

}
