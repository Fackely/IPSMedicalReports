package com.princetonsa.dao.postgresql.administracion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.administracion.TiposRetencionDao;
import com.princetonsa.dao.sqlbase.administracion.SqlBaseTiposRetencionDao;
import com.princetonsa.dto.administracion.DtoDetTiposRetencionClaseInv;
import com.princetonsa.dto.administracion.DtoDetTiposRetencionConceptoFV;
import com.princetonsa.dto.administracion.DtoDetTiposRetencionGrupoSer;
import com.princetonsa.dto.administracion.DtoLogTipoRetencion;
import com.princetonsa.dto.administracion.DtoTiposRetencion;

public class PostgresqlTiposRetencionDao  implements TiposRetencionDao
{
	/**
	 * Consulta Listado de Tipos de Retencion
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public ArrayList<DtoTiposRetencion> cargarTiposRetencion(Connection con, HashMap parametros)
	{
		return SqlBaseTiposRetencionDao.cargarTiposRetencion(con, parametros);
	}
	
	/**
	 * 
	 * @param con
	 * @param dtoLogTipoRetencion
	 * @return
	 */
	public int insertarTipoRetencion(Connection con, DtoTiposRetencion dtoTipoRetencion)
	{
		return SqlBaseTiposRetencionDao.insertarTipoRetencion(con, dtoTipoRetencion);
	}
	
	/**
	 * 
	 * @param con
	 * @param dtoTipoRetencion
	 * @return
	 */
	public int updateTipoRetencion(Connection con, DtoTiposRetencion dtoTipoRetencion)
	{
		return SqlBaseTiposRetencionDao.updateTipoRetencion(con, dtoTipoRetencion);
	}
	
	/**
	 * 
	 * @param con
	 * @param dtoLogTipoRetencion
	 * @return
	 */
	public int insertarLogTipoRetencion(Connection con, DtoLogTipoRetencion dtoLogTipoRetencion)
	{
		return SqlBaseTiposRetencionDao.insertarLogTipoRetencion(con, dtoLogTipoRetencion);
	}
	
	/**
	 * 
	 * @param con
	 * @param dtoTipoRetencion
	 * @return
	 */
	public int deleteTipoRetencion(Connection con, DtoTiposRetencion dtoTipoRetencion)
	{
		return SqlBaseTiposRetencionDao.deleteTipoRetencion(con, dtoTipoRetencion);
	}
	
	/**
	 * Verificacion Concepto retencion
	 * @param con
	 * @param parametros
	 * @return
	 */
	public boolean verificacionConceptoRetencion(Connection con, HashMap parametros)
	{
		return SqlBaseTiposRetencionDao.verificacionConceptoRetencion(con, parametros);
	}
	
	/**
	 * Verificacion Tipo Retencion Clase Inventario
	 * @param con
	 * @param parametros
	 * @return
	 */
	public boolean verificacionClaseInv(Connection con, HashMap parametros)
	{
		return SqlBaseTiposRetencionDao.verificacionClaseInv(con, parametros);
	}
	
	/**
	 * Verificacion Tipo Retencion Clase Inventario
	 * @param con
	 * @param parametros
	 * @return
	 */
	public boolean verificacionGrupoSer(Connection con, HashMap parametros)
	{
		return SqlBaseTiposRetencionDao.verificacionGrupoSer(con, parametros);
	}
	
	/**
	 * Verificacion Tipo Retencion Clase Inventario
	 * @param con
	 * @param parametros
	 * @return
	 */
	public boolean verificacionConcFacturaVaria(Connection con, HashMap parametros)
	{
		return SqlBaseTiposRetencionDao.verificacionConcFacturaVaria(con, parametros);
	}
	
	/**
	 * 
	 * @param con
	 * @param dtoTRGrupoSer
	 * @return
	 */
	public int insertarTRGrupoServicio(Connection con, DtoDetTiposRetencionGrupoSer dtoTRGrupoSer)
	{
		return SqlBaseTiposRetencionDao.insertarTRGrupoServicio(con, dtoTRGrupoSer);
	}
	
	/**
	 * 
	 * @param con
	 * @param dtoTRClaseInv
	 * @return
	 */
	public int insertarTRClaseInventario(Connection con, DtoDetTiposRetencionClaseInv dtoTRClaseInv)
	{
		return SqlBaseTiposRetencionDao.insertarTRClaseInventario(con, dtoTRClaseInv);
	}
	
	/**
	 * 
	 * @param con
	 * @param dtoTRClaseInv
	 * @return
	 */
	public int insertarTRConceptoFraVaria(Connection con, DtoDetTiposRetencionConceptoFV dtoTRConcFV)
	{
		return SqlBaseTiposRetencionDao.insertarTRConceptoFraVaria(con, dtoTRConcFV);
	}
	
	/**
	 * Inactivacion de Tipo Retencion Grupo Servicio
	 * @param con
	 * @param dtoTRGrupoSer
	 * @return
	 */
	public int inactivarTRGrupoServicio(Connection con, DtoDetTiposRetencionGrupoSer dtoTRGrupoSer)
	{
		return SqlBaseTiposRetencionDao.inactivarTRGrupoServicio(con, dtoTRGrupoSer);
	}
		
	/**
	 * Inactivacion de Tipo Retencion Clase Inventario
	 * @param con
	 * @param dtoTRClaseInv
	 * @return
	 */
	public int inactivarTRClaseInventario(Connection con, DtoDetTiposRetencionClaseInv dtoTRClaseInv)
	{
		return SqlBaseTiposRetencionDao.inactivarTRClaseInventario(con, dtoTRClaseInv);
	}
		
	/**
	 * Inactivacion de Tipo Retencion Clase Inventario
	 * @param con
	 * @param dtoTRClaseInv
	 * @return
	 */
	public int inactivarTRConceptoFraVarias(Connection con, DtoDetTiposRetencionConceptoFV dtoTRConceptoFV)
	{
		return SqlBaseTiposRetencionDao.inactivarTRConceptoFraVarias(con, dtoTRConceptoFV); 
	}
}
