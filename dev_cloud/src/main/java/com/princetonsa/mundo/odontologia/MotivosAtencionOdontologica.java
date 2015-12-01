package com.princetonsa.mundo.odontologia;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.odontologia.MotivosAtencionOdontologicaDao;
import com.princetonsa.dto.odontologia.DtoMotivosAtencion;


public class MotivosAtencionOdontologica{
	
	Logger logger = Logger.getLogger(MotivosAtencionOdontologica.class);
	
	private static MotivosAtencionOdontologicaDao getMotivosAtencionOdontologicaDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMotivosAtencionOdontologicaDao();
	}

	public HashMap consultarTiposMotivo() {
		return getMotivosAtencionOdontologicaDao().consultarTiposMotivo();
	}

	public boolean insertarMotivoAtencionO(String codigoNuevo,String nombreNuevo, int tipoNuevo, int institucion,String usuario) {
		return getMotivosAtencionOdontologicaDao().insertarMotivoAtencionO(codigoNuevo, nombreNuevo, tipoNuevo, institucion, usuario);
	}
	
	public ArrayList<DtoMotivosAtencion> consultarMotivoAtencionO(ArrayList<Integer> tipos, int institucion) 
	{
		return getMotivosAtencionOdontologicaDao().consultarMotivoAtencionO(tipos, institucion);
	}

	public boolean modificarMotivoAtencionO(int codigoPk, String codigoNuevo,String nombreNuevo, int tipoNuevo, int codigoInstitucionInt,String loginUsuario) {
		return getMotivosAtencionOdontologicaDao().modificarMotivoAtencionO(codigoPk, codigoNuevo, nombreNuevo, tipoNuevo, codigoInstitucionInt, loginUsuario);
	}

	public boolean eliminarMotivoAtencionO(int codigoPk) {
		return getMotivosAtencionOdontologicaDao().eliminarMotivoAtencionO(codigoPk);
	}	
}