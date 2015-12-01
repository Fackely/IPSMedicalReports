package com.princetonsa.dao.parametrizacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;

import util.RangosConsecutivos;

public interface CentrosAtencionDao 
{
	/**
	 * Metodo que inserta un centro de atencion
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param activo
	 * @param codInstitucion
	 * @param codUpgd
	 * @param codInstitucionSIRC
	 * @param empresaInstitucion
	 * @param direccion
	 * @param pais
	 * @param departamento
	 * @param ciudad
	 * @param telefono
	 * @param regionCobertura
	 * @param categoriaAtencion
	 * @param codigoInterfaz
	 * @param piePaginaPresupuestoOdon
	 * @param resolucion
	 * @param preFactura
	 * @param rangoInicialFactura
	 * @param rangoFinalFactura
	 * @param resolucionFacturaVaria
	 * @param prefFacturaVaria
	 * @param rangoInicialFacturaVaria
	 * @param rangoFinalFacturaVaria
	 * @param codigoEntidadSubcontratada
	 * @return
	 * @throws SQLException
	 */
	public int insertarCentroAtencion(Connection con, String codigo, String descripcion, String activo, 
					int codInstitucion, String codUpgd, String codInstitucionSIRC, String empresaInstitucion,
					String direccion, String pais , String departamento , String ciudad , String telefono , 
					double regionCobertura , double categoriaAtencion, String codigoInterfaz,String piePaginaPresupuestoOdon ,  
					String resolucion, String  preFactura,BigDecimal rangoInicialFactura,  BigDecimal rangoFinalFactura, 
					String resolucionFacturaVaria, String prefFacturaVaria, BigDecimal rangoInicialFacturaVaria, 
					BigDecimal rangoFinalFacturaVaria, String codigoEntidadSubcontratada) throws SQLException;
	
	/**
	 * 
	 * Método para actualizar la información de un centro de atención
	 * 
	 * @param con
	 * @param consecutivo
	 * @param descripcion
	 * @param activo
	 * @param codUpgdm
	 * @param codInstitucionSIRC
	 * @param empresaInstitucion
	 * @param direccion
	 * @param telefono
	 * @param pais
	 * @param departamento
	 * @param ciudad
	 * @param regionCobertura
	 * @param categoriaAtencion
	 * @param codInterfaz
	 * @param piePagina
	 * @param resolucion
	 * @param preFactura
	 * @param rangoInicialFactura
	 * @param rangoFinalFactura
	 * @param resolucionFacturaVaria
	 * @param preFacturaVaria
	 * @param rangoInicialFacturaVaria
	 * @param rangoFinalFacturaVaria
	 * @param codigoEntidadSubcontratada
	 * @throws SQLException
	 */
	public void actualizarCentroAtencion(Connection con, int consecutivo, String descripcion, String activo, String codUpgdm, 
						String codInstitucionSIRC, String empresaInstitucion,String direccion ,String telefono, String pais,
						String departamento, String ciudad, double regionCobertura, double categoriaAtencion, String codInterfaz, 
						String piePagina, String resolucion, String preFactura, BigDecimal rangoInicialFactura, BigDecimal rangoFinalFactura, 
						String resolucionFacturaVaria, String preFacturaVaria ,BigDecimal rangoInicialFacturaVaria , 
						BigDecimal rangoFinalFacturaVaria, String codigoEntidadSubContratada) throws SQLException;

	/**
	 * Método para eliminar un centro de atención
	 * @param con
	 * @param consecutivo
	 * @throws SQLException
	 */
	public void eliminarCentroAtencion(Connection con, int consecutivo) throws SQLException;
	
	/**
	 * Método para consultar la información de un centro de atención
	 * @param con
	 * @param consecutivo
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarCentroAtencion(Connection con, int consecutivo) throws SQLException;
	
	/**
	 * Método para consultar los centros de atención de una institución especificada
	 * @param con
	 * @param codInstitucion
	 * @return
	 * @throws SQLException
	 */
	public Collection consultarCentrosAtencionInst(Connection con, int codInstitucion) throws SQLException;
	
	/**
	 *Metodo que cargar el combo de Codigos UPGD 
	 * @param con
	 * @return
	 */
	public HashMap cargarVigiUnidadesPrimarias(Connection con,  int codInstitucion, int codConsecutivo);
	
	/**
	 * Metodo que carga los codigos de Instituciones SIRC; sin incluir los que ya estan asignados a algun centro de atencion
	 * @param con
	 * @param codInstitucion
	 * */
	public HashMap cargarInstitucionesSIRC(Connection con, int codInstitucion);
	
	/**
	 * 
	 * @param con
	 * @param consecutivoCentroAtencion
	 * @return
	 */
	public double obtenerEmpresaInstitucionCentroAtencion( Connection con, int consecutivoCentroAtencion);

	/**
	 * Metodo que consulta el rango inicial y final de los consecutivos de facturacion
	 * @param centroAtencion
	 * @return
	 */
	public RangosConsecutivos obtenerRangosFacturacionXCentroAtencion(int centroAtencion);
	
	/**
	 * 
	 * Metodo para obtener el pie de pagina del presupuesto
	 * @param centroAtencion
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public String obtenerPiePaginaPresupuesto(int centroAtencion);
	
	
	/**
	 * Metodo que carga las entidades subcotnratadas activas y que tiene asociados
	 * centros de costo identificados como SubAlmacen
	 * @param con
	 * @param codInstitucion
	 * */
	public HashMap cargarEntidadesSubcontratadas(Connection con, int codInstitucion, int centroAtencion);
}
