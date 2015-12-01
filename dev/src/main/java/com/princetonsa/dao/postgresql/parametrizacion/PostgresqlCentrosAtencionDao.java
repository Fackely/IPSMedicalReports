package com.princetonsa.dao.postgresql.parametrizacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import util.RangosConsecutivos;

import com.princetonsa.dao.parametrizacion.CentrosAtencionDao;
import com.princetonsa.dao.sqlbase.parametrizacion.SqlBaseCentrosAtencionDao;

public class PostgresqlCentrosAtencionDao implements CentrosAtencionDao
{
	
	@Override
	public int insertarCentroAtencion(Connection con, String codigo,
			String descripcion, String activo, int codInstitucion,
			String codUpgd, String codInstitucionSIRC,
			String empresaInstitucion, String direccion, String pais,
			String departamento, String ciudad, String telefono,
			double regionCobertura, double categoriaAtencion, 
			String codigoInterfaz,String piePaginaPresupuestoOdon ,
			String resolucion, String preFactura,BigDecimal rangoInicialFactura, 
			BigDecimal rangoFinalFactura, String resolucionFacturaVaria, String preFacturaVaria,
			BigDecimal rangoInicialFacturaVaria , BigDecimal rangoFinalFacturaVaria, String codigoEntidadSubcontratada)
			throws SQLException {
		
		return SqlBaseCentrosAtencionDao.insertarCentroAtencion(con, codigo, descripcion, activo, codInstitucion, codUpgd, 
						codInstitucionSIRC, empresaInstitucion, direccion, pais, departamento, ciudad, telefono, regionCobertura, 
						categoriaAtencion, codigoInterfaz, piePaginaPresupuestoOdon, 	 resolucion,  preFactura, rangoInicialFactura, 
						rangoFinalFactura, resolucionFacturaVaria, preFacturaVaria , rangoInicialFacturaVaria , rangoFinalFacturaVaria,
						codigoEntidadSubcontratada);
	}

	public void eliminarCentroAtencion(Connection con, int consecutivo)
			throws SQLException
	{
		SqlBaseCentrosAtencionDao.eliminarCentroAtencion(con, consecutivo);
	}

	public HashMap consultarCentroAtencion(Connection con, int consecutivo)
			throws SQLException
	{
		return SqlBaseCentrosAtencionDao.consultarCentroAtencion(con, consecutivo);
	}

	public Collection consultarCentrosAtencionInst(Connection con,
			int codInstitucion) throws SQLException
	{
		return SqlBaseCentrosAtencionDao.consultarCentrosAtencionInst(con, codInstitucion);
	}
	
	public HashMap cargarVigiUnidadesPrimarias(Connection con, int codInstitucion, int codConsecutivo)
	{
		return SqlBaseCentrosAtencionDao.cargarVigiUnidadesPrimarias(con,codInstitucion, codConsecutivo);		
	}
	
	/**
	 * Metodo que carga los codigos de Instituciones SIRC; sin incluir los que ya estan asignados a algun centro de atencion
	 * @param con
	 * @param codInstitucion
	 * */
	public HashMap cargarInstitucionesSIRC(Connection con, int codInstitucion)
	{
		return SqlBaseCentrosAtencionDao.cargarInstitucionesSIRC(con, codInstitucion);		
	}	
	
	/**
	 * 
	 * @param con
	 * @param consecutivoCentroAtencion
	 * @return
	 */
	public double obtenerEmpresaInstitucionCentroAtencion(Connection con, int consecutivoCentroAtencion)
	{
		return SqlBaseCentrosAtencionDao.obtenerEmpresaInstitucionCentroAtencion(con, consecutivoCentroAtencion);
	}

	
	
	@Override
	public RangosConsecutivos obtenerRangosFacturacionXCentroAtencion(
			int centroAtencion) {
		return SqlBaseCentrosAtencionDao.obtenerRangosFacturacionXCentroAtencion(centroAtencion);
	}
	
	public void actualizarCentroAtencion(Connection con, int consecutivo,String descripcion, String activo, 
			String codUpgd, String codInstitucionSIRC, String empresaInstitucion,String direccion,String telefono, 
			String pais,String departamento, String ciudad, double regionCobertura, double categoriaAtencion, 
			String codInterfaz, String piePagina, 	String resolucion, String preFactura,
			BigDecimal rangoInicialFactura, BigDecimal rangoFinalFactura, String resolucionFacturaVaria, 
			String preFacturaVaria ,BigDecimal rangoInicialFacturaVaria , BigDecimal rangoFinalFacturaVaria, 
			String codigoEntidadSubcontratada) throws SQLException 
	{
		SqlBaseCentrosAtencionDao.actualizarCentroAtencion(con, consecutivo, descripcion, activo, codUpgd, codInstitucionSIRC, empresaInstitucion,
				direccion ,telefono,  pais, departamento,  ciudad, regionCobertura, categoriaAtencion, codInterfaz, piePagina, resolucion, 
				preFactura , rangoInicialFactura , rangoFinalFactura, resolucionFacturaVaria, preFacturaVaria , rangoInicialFacturaVaria , 
				rangoFinalFacturaVaria, codigoEntidadSubcontratada);
	}

	
	@Override
	public String obtenerPiePaginaPresupuesto(int centroAtencion) {
		return SqlBaseCentrosAtencionDao.obtenerPiePaginaPresupuesto(centroAtencion);
	}

	/* (non-Javadoc)
	 * @see com.princetonsa.dao.parametrizacion.CentrosAtencionDao#cargarEntidadesSubcontratadas(java.sql.Connection, int)
	 */
	@Override
	public HashMap cargarEntidadesSubcontratadas(Connection con,
			int codInstitucion, int centroAtencion) {
		return SqlBaseCentrosAtencionDao.cargarEntidadesSubcontratadas(con, codInstitucion, centroAtencion);
	}
}
