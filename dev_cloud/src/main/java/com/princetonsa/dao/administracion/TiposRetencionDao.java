package com.princetonsa.dao.administracion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import util.ConstantesBD;
import util.UtilidadBD;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.dto.administracion.DtoDetTiposRetencionClaseInv;
import com.princetonsa.dto.administracion.DtoDetTiposRetencionConceptoFV;
import com.princetonsa.dto.administracion.DtoDetTiposRetencionGrupoSer;
import com.princetonsa.dto.administracion.DtoLogTipoRetencion;
import com.princetonsa.dto.administracion.DtoTiposRetencion;

/**
 * @author Víctor Hugo Gómez L.
 */

public interface TiposRetencionDao 
{
	/**
	 * Consulta Listado de Tipos de Retencion
	 * @param Connection con
	 * @param HashMap parametros
	 */
	public ArrayList<DtoTiposRetencion> cargarTiposRetencion(Connection con, HashMap parametros);
	
	/**
	 * 
	 * @param con
	 * @param dtoLogTipoRetencion
	 * @return
	 */
	public int insertarTipoRetencion(Connection con, DtoTiposRetencion dtoTipoRetencion);
	
	/**
	 * 
	 * @param con
	 * @param dtoTipoRetencion
	 * @return
	 */
	public int updateTipoRetencion(Connection con, DtoTiposRetencion dtoTipoRetencion);
	
	/**
	 * 
	 * @param con
	 * @param dtoLogTipoRetencion
	 * @return
	 */
	public int insertarLogTipoRetencion(Connection con, DtoLogTipoRetencion dtoLogTipoRetencion);
	
	/**
	 * 
	 * @param con
	 * @param dtoTipoRetencion
	 * @return
	 */
	public int deleteTipoRetencion(Connection con, DtoTiposRetencion dtoTipoRetencion);
	
	/**
	 * Verificacion Concepto retencion
	 * @param con
	 * @param parametros
	 * @return
	 */
	public boolean verificacionConceptoRetencion(Connection con, HashMap parametros);
	
	/**
	 * Verificacion Tipo Retencion Clase Inventario
	 * @param con
	 * @param parametros
	 * @return
	 */
	public boolean verificacionClaseInv(Connection con, HashMap parametros);
	
	/**
	 * Verificacion Tipo Retencion Clase Inventario
	 * @param con
	 * @param parametros
	 * @return
	 */
	public boolean verificacionGrupoSer(Connection con, HashMap parametros);
	
	/**
	 * Verificacion Tipo Retencion Clase Inventario
	 * @param con
	 * @param parametros
	 * @return
	 */
	public boolean verificacionConcFacturaVaria(Connection con, HashMap parametros);
	
	/**
	 * 
	 * @param con
	 * @param dtoTRGrupoSer
	 * @return
	 */
	public int insertarTRGrupoServicio(Connection con, DtoDetTiposRetencionGrupoSer dtoTRGrupoSer);
	
	/**
	 * 
	 * @param con
	 * @param dtoTRClaseInv
	 * @return
	 */
	public int insertarTRClaseInventario(Connection con, DtoDetTiposRetencionClaseInv dtoTRClaseInv);
	
	/**
	 * 
	 * @param con
	 * @param dtoTRClaseInv
	 * @return
	 */
	public int insertarTRConceptoFraVaria(Connection con, DtoDetTiposRetencionConceptoFV dtoTRConcFV);
	
	/**
	 * Inactivacion de Tipo Retencion Grupo Servicio
	 * @param con
	 * @param dtoTRGrupoSer
	 * @return
	 */
	public int inactivarTRGrupoServicio(Connection con, DtoDetTiposRetencionGrupoSer dtoTRGrupoSer);
		
	/**
	 * Inactivacion de Tipo Retencion Clase Inventario
	 * @param con
	 * @param dtoTRClaseInv
	 * @return
	 */
	public int inactivarTRClaseInventario(Connection con, DtoDetTiposRetencionClaseInv dtoTRClaseInv);
		
	/**
	 * Inactivacion de Tipo Retencion Clase Inventario
	 * @param con
	 * @param dtoTRClaseInv
	 * @return
	 */
	public int inactivarTRConceptoFraVarias(Connection con, DtoDetTiposRetencionConceptoFV dtoTRConceptoFV);
	
}
