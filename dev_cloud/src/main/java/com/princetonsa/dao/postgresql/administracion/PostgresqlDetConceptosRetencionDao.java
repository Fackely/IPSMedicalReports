package com.princetonsa.dao.postgresql.administracion;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dao.administracion.DetConceptosRetencionDao;
import com.princetonsa.dao.sqlbase.administracion.SqlBaseDetConceptosRetencionDao;
import com.princetonsa.dto.administracion.DtoDetConceptosRetencion;
import com.princetonsa.dto.administracion.DtoDetVigConRet;
import com.princetonsa.dto.administracion.DtoDetVigConRetencion;

public class PostgresqlDetConceptosRetencionDao implements DetConceptosRetencionDao 
{
	public ArrayList<DtoDetConceptosRetencion> consultaDetConceptosRetencion()
	{
		return SqlBaseDetConceptosRetencionDao.consultaDetConceptosRetencion();
	}
	
	public boolean insertarDetConceptosRetencion(DtoDetConceptosRetencion dto)
	{
		return SqlBaseDetConceptosRetencionDao.insertarDetConceptosRetencion(dto);
	}
	
	public boolean validarExisteDetConceptosRetencion(DtoDetConceptosRetencion dto)
	{
		return SqlBaseDetConceptosRetencionDao.validarExisteDetConceptosRetencion(dto);
	}
	
	public boolean validarDetalleXConcepto(String consecutivo)
	{
		return SqlBaseDetConceptosRetencionDao.validarDetalleXConcepto(consecutivo);
	}
	
	public ArrayList<DtoDetVigConRetencion> consultarDetVigenciaConceptosRetencion(String consecutivo)
	{
		return SqlBaseDetConceptosRetencionDao.consultarDetVigenciaConceptosRetencion(consecutivo);
	}
	
	public boolean insertarDetVigConRetencion (DtoDetVigConRetencion dto) 
	{
		return SqlBaseDetConceptosRetencionDao.insertarDetVigConRetencion(dto);
	}
	
	public ArrayList<DtoDetVigConRet> consultarDetRetXGrupoServicio(String consecutivo)
	{
		return SqlBaseDetConceptosRetencionDao.consultarDetRetXGrupoServicio(consecutivo);
	}
	
	public boolean insertarDetRetXGrupoServicio (DtoDetVigConRet dto)
	{
		return SqlBaseDetConceptosRetencionDao.insertarDetRetXGrupoServicio(dto);
	}
	
	public boolean inactivarDetRetXGrupoServicio (DtoDetVigConRet dto)
	{
		return SqlBaseDetConceptosRetencionDao.inactivarDetRetXGrupoServicio(dto);
	}
	
	public ArrayList<DtoDetVigConRet> consultarDetRetXClaseInv(String consecutivo)
	{
		return SqlBaseDetConceptosRetencionDao.consultarDetRetXClaseInv(consecutivo);
	}
	
	public boolean insertarDetRetXClaseInv(DtoDetVigConRet dto)
	{
		return SqlBaseDetConceptosRetencionDao.insertarDetRetXClaseInv(dto);
	}
	
	public boolean inactivarDetRetXClaseInv(DtoDetVigConRet dto)
	{
		return SqlBaseDetConceptosRetencionDao.inactivarDetRetXClaseInv(dto);
	}
	
	public ArrayList<DtoDetVigConRet> consultarDetRetXConceptos(String consecutivo)
	{
		return SqlBaseDetConceptosRetencionDao.consultarDetRetXConceptos(consecutivo);
	}
	
	public boolean insertarDetRetXConcepto (DtoDetVigConRet dto)
	{
		return SqlBaseDetConceptosRetencionDao.insertarDetRetXConcepto(dto);
	}
	
	public boolean inactivarDetRetXConcepto(DtoDetVigConRet dto)
	{
		return SqlBaseDetConceptosRetencionDao.inactivarDetRetXConcepto(dto);
	}
	
	public boolean detPoseeDetalles(String consecutivo)
	{
		return SqlBaseDetConceptosRetencionDao.detPoseeDetalles(consecutivo);
	}
	
	public boolean inactivarDetVigConRetencion(DtoDetVigConRetencion dto)
	{
		return SqlBaseDetConceptosRetencionDao.inactivarDetVigConRetencion(dto);
	}
	
	public boolean ingresarLog(DtoDetVigConRetencion dto)
	{
		return SqlBaseDetConceptosRetencionDao.ingresarLog(dto);
	}
	
	public boolean actualizarGrupo (int consecutivoNuevo, int consecutivoViejo)
	{
		return SqlBaseDetConceptosRetencionDao.actualizarGrupo(consecutivoNuevo, consecutivoViejo);
	}
	
	public boolean actualizarClase (int consecutivoNuevo, int consecutivoViejo)
	{
		return SqlBaseDetConceptosRetencionDao.actualizarClase(consecutivoNuevo,consecutivoViejo);
	}
	
	public boolean actualizarCfv (int consecutivoNuevo, int consecutivoViejo)
	{
		return SqlBaseDetConceptosRetencionDao.actualizarCfv(consecutivoNuevo,consecutivoViejo);
	}
	
	public boolean poseeDetalles(int consecutivo)
	{
		return SqlBaseDetConceptosRetencionDao.poseeDetalles(consecutivo);
	}
}