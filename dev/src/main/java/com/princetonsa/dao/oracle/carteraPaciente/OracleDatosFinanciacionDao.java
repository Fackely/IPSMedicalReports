package com.princetonsa.dao.oracle.carteraPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.carterapaciente.DatosFinanciacionDao;
import com.princetonsa.dao.sqlbase.carteraPaciente.SqlBaseDatosFinanciacionDao;
import com.princetonsa.dto.carteraPaciente.DtoDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoDeudor;
import com.princetonsa.dto.carteraPaciente.DtoDeudoresDatosFinan;
import com.princetonsa.dto.facturacion.DtoFactura;

/**
 * 
 * @author V�ctor G�mez L.
 *
 */
public class OracleDatosFinanciacionDao implements DatosFinanciacionDao{
	
	/**
	 * insercion datos financiacion
	 * @param con
	 * @param dto
	 * @return
	 */
	public int insertDatosFinanciacion(Connection con, DtoDatosFinanciacion dto)
	{
		return SqlBaseDatosFinanciacionDao.insertDatosFinanciacion(con, dto);
	}
	
	/**
	 * metodo que carga el deudor y/o codeudor segun el documento de garantia asociado
	 * @param con
	 * @param parametros
	 * @return
	 */
	public DtoDeudoresDatosFinan obtenerDatosDeudorCO(Connection con, HashMap parametros)
	{
		return SqlBaseDatosFinanciacionDao.obtenerDatosDeudorCO(con, parametros);
	}
	
	/**
	 * Obtener datos deudor y/o Codeudor
	 * @param con
	 * @param parametros
	 * @return
	 */
	public DtoDeudoresDatosFinan obtenerDatosDeudor(Connection con, HashMap parametros)
	{
		return SqlBaseDatosFinanciacionDao.obtenerDatosDeudor(con, parametros);
	}
	
	/**
	 * Consulta los datos del responsable del paciente si existe
	 * @param con
	 * @param parametros
	 * @return
	 */
	public DtoDeudoresDatosFinan obtenerDatosResponsable(Connection con, HashMap parametros)
	{
		return SqlBaseDatosFinanciacionDao.obtenerDatosResponsable(con, parametros);
	}
	
	/**
	 * verificar Existencia persona
	 * @param con
	 * @param parametros
	 * @return
	 */
	public DtoDeudoresDatosFinan verificarExistenciaPer(Connection con, HashMap parametros)
	{
		return SqlBaseDatosFinanciacionDao.verificarExistenciaPer(con, parametros);
	}
	
	/**
	 * obtener ingreso de facturas
	 * @param con
	 * @param parametros
	 * @return
	 */
	public HashMap obtenerIngresoFactura(Connection con, HashMap parametros)
	{
		return SqlBaseDatosFinanciacionDao.obtenerIngresoFactura(con, parametros);
	}
	
	/**
	 * Actualizar deudor
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean actualizarDeudor(Connection con, DtoDeudoresDatosFinan dto)
	{
		return SqlBaseDatosFinanciacionDao.actualizarDeudor(con, dto);
	}
	
	/**
	 * verificar la existencia de un documento de garantia asociado al ingreso de la factura
	 * @param con
	 * @param parametros
	 * @return
	 */
	public HashMap verificarDocumentoGarantia(Connection con, HashMap parametros)
	{
		return SqlBaseDatosFinanciacionDao.verificarDocumentoGarantia(con, parametros);
	}
	
	/**
	 * busquesa deudores
	 * @param con
	 * @param dtoe
	 * @return
	 */
	public ArrayList<DtoDeudor> busquedaDeudores(Connection con, DtoDeudor dtoe)
	{
		return SqlBaseDatosFinanciacionDao.busquedaDeudores(con, dtoe);
	}
	
	/**
	 * actualizar datos Paciente
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean actualizarPaciente(Connection con, DtoDeudoresDatosFinan dto)
	{
		return SqlBaseDatosFinanciacionDao.actualizarPaciente(con, dto);
	}
	
	/**
	 * actualizar responsable paciente
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean actualizarResponsablePac(Connection con, DtoDeudoresDatosFinan dto)
	{
		return SqlBaseDatosFinanciacionDao.actualizarResponsablePac(con, dto);
	}
	
	/**
	 * verifica la existencia de responsable de paciente, segun el ingreso de la factura
	 * @param con
	 * @param parametros
	 * @return
	 */
	public String verificarResponsablePacIngFac(Connection con, HashMap parametros)
	{
		return SqlBaseDatosFinanciacionDao.verificarResponsablePacIngFac(con, parametros);
	}
	
	/**
	 * actualizacion de documento de garantia segun requerimiento de anulacion de recibo de caja conconcepto paciente
	 * @param con
	 * @param parametros
	 * @return
	 */
	public boolean actualizarDocGarantiaAnuRC(Connection con, HashMap parametros)
	{
		return SqlBaseDatosFinanciacionDao.actualizarDocGarantiaAnuRC(con, parametros);
	}
	
	/**
	 * actualizar los datos del documento de garantia
	 * @param con
	 * @param parametros
	 * @return
	 */
	public boolean actualizarDocGarAsociado(Connection con, HashMap parametros)
	{
		return SqlBaseDatosFinanciacionDao.actualizarDocGarAsociado(con, parametros);
	}
	
	

	/**
	 * 
	 * @param tipoDocGarantia
	 * @return
	 */
	public String obtenerPrimerConsecutivoUsadoDocumento(String tipoDocGarantia,int institucion)
	{
		return SqlBaseDatosFinanciacionDao.obtenerPrimerConsecutivoUsadoDocumento(tipoDocGarantia,institucion);
	}

	@Override
	public boolean esConsecutivoDocumentoUsado(String tipoDocGarantia,String consecutivoDocumento, int institucion) 
	{
		return SqlBaseDatosFinanciacionDao.esConsecutivoDocumentoUsado(tipoDocGarantia,consecutivoDocumento,institucion);
	}
	
	

	@Override
	public ArrayList<DtoFactura> consultarFacturas(String facturaBusqueda,String tipoIdentificacionDeudor, String numeroIdentificacionDeudor,String tipoIdentificacionCodeudor,String numeroIdentificacionCodeudor) 
	{
		return SqlBaseDatosFinanciacionDao.consultarFacturas(facturaBusqueda,tipoIdentificacionDeudor,numeroIdentificacionDeudor,tipoIdentificacionCodeudor,numeroIdentificacionCodeudor);
	}
	
	public int consultaCodigoDocuGarantia(int ingreso, int consecutivo,String anioConsecutivo)
	{
		return SqlBaseDatosFinanciacionDao.consultaCodigoDocuGarantia(ingreso, consecutivo,anioConsecutivo);
	}
}
