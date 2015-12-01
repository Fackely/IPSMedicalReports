package com.princetonsa.dao.postgresql.odontologia;

import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.odontologia.MotivosAtencionOdontologicaDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseMotivosAtencionOdontologicaDao;
import com.princetonsa.dto.odontologia.DtoMotivosAtencion;


public class PostgresqlMotivosAtencionOdontologicaDao implements MotivosAtencionOdontologicaDao
{
	public HashMap consultarTiposMotivo()
	{
		return SqlBaseMotivosAtencionOdontologicaDao.consultarTiposMotivo();
	}
	
	public boolean insertarMotivoAtencionO(String codigoNuevo, String nombreNuevo,int tipoNuevo, int institucion, String usuario)
	{
		return SqlBaseMotivosAtencionOdontologicaDao.insertarMotivoAtencionO(codigoNuevo, nombreNuevo, tipoNuevo, institucion, usuario);
	}
	
	public ArrayList<DtoMotivosAtencion> consultarMotivoAtencionO(ArrayList<Integer> tipos, int institucion) 
	{
		return SqlBaseMotivosAtencionOdontologicaDao.consultarMotivoAtencionO(tipos, institucion);
	}
	
	public boolean modificarMotivoAtencionO(int codigoPk, String codigoNuevo,String nombreNuevo, int tipoNuevo, int codigoInstitucionInt,String loginUsuario)
	{
		return SqlBaseMotivosAtencionOdontologicaDao.modificarMotivoAtencionO( codigoPk, codigoNuevo, nombreNuevo, tipoNuevo, codigoInstitucionInt, loginUsuario);
	}
	
	public boolean eliminarMotivoAtencionO(int codigoPk)
	{
		return SqlBaseMotivosAtencionOdontologicaDao.eliminarMotivoAtencionO(codigoPk);
	}
}