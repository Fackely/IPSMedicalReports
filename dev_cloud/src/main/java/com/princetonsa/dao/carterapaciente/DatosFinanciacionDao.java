package com.princetonsa.dao.carterapaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.carteraPaciente.DtoDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoDeudor;
import com.princetonsa.dto.carteraPaciente.DtoDeudoresDatosFinan;
import com.princetonsa.dto.facturacion.DtoFactura;

/**
 * 
 * @author V�ctor G�mez L.
 *
 */
public interface DatosFinanciacionDao {

	/**
	 * insercion datos financiacion
	 * @param con
	 * @param dto
	 * @return
	 */
	public int insertDatosFinanciacion(Connection con, DtoDatosFinanciacion dto);
	
	/**
	 * metodo que carga el deudor y/o codeudor segun el documento de garantia asociado
	 * @param con
	 * @param parametros
	 * @return
	 */
	public DtoDeudoresDatosFinan obtenerDatosDeudorCO(Connection con, HashMap parametros);
	
	/**
	 * Obtener datos deudor y/o Codeudor
	 * @param con
	 * @param parametros
	 * @return
	 */
	public DtoDeudoresDatosFinan obtenerDatosDeudor(Connection con, HashMap parametros);
	
	/**
	 * Consulta los datos del responsable del paciente si existe
	 * @param con
	 * @param parametros
	 * @return
	 */
	public DtoDeudoresDatosFinan obtenerDatosResponsable(Connection con, HashMap parametros);
	
	/**
	 * verificar Existencia persona
	 * @param con
	 * @param parametros
	 * @return
	 */
	public DtoDeudoresDatosFinan verificarExistenciaPer(Connection con, HashMap parametros);
	
	/**
	 * obtener ingreso de facturas
	 * @param con
	 * @param parametros
	 * @return
	 */
	public HashMap obtenerIngresoFactura(Connection con, HashMap parametros);
	
	/**
	 * Actualizar deudor
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean actualizarDeudor(Connection con, DtoDeudoresDatosFinan dto);
	
	/**
	 * verificar la existencia de un documento de garantia asociado al ingreso de la factura
	 * @param con
	 * @param parametros
	 * @return
	 */
	public HashMap verificarDocumentoGarantia(Connection con, HashMap parametros);
	
	/**
	 * busquesa deudores
	 * @param con
	 * @param dtoe
	 * @return
	 */
	public ArrayList<DtoDeudor> busquedaDeudores(Connection con, DtoDeudor dtoe);
	
	/**
	 * actualizar datos Paciente
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean actualizarPaciente(Connection con, DtoDeudoresDatosFinan dto);
	
	/**
	 * actualizar responsable paciente
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean actualizarResponsablePac(Connection con, DtoDeudoresDatosFinan dto);
	
	/**
	 * verifica la existencia de responsable de paciente, segun el ingreso de la factura
	 * @param con
	 * @param parametros
	 * @return
	 */
	public String verificarResponsablePacIngFac(Connection con, HashMap parametros);

	/**
	 * actualizacion de documento de garantia segun requerimiento de anulacion de recibo de caja conconcepto paciente
	 * @param con
	 * @param parametros
	 * @return
	 */
	public boolean actualizarDocGarantiaAnuRC(Connection con, HashMap parametros);
	
	/**
	 * actualizar los datos del documento de garantia
	 * @param con
	 * @param parametros
	 * @return
	 */
	public boolean actualizarDocGarAsociado(Connection con, HashMap parametros);

	/**
	 * 
	 * @param tipoDocGarantia
	 * @return
	 */
	public String obtenerPrimerConsecutivoUsadoDocumento(String tipoDocGarantia,int institucion);

	/**
	 * 
	 * @param tipoDocGarantia
	 * @param consecutivoDocumento
	 * @param institucion
	 * @return
	 */
	public boolean esConsecutivoDocumentoUsado(String tipoDocGarantia,String consecutivoDocumento, int institucion);

	/**
	 * 
	 * @param facturaBusqueda
	 * @param tipoIdentificacionDeudor
	 * @param numeroIdentificacionDeudor
	 * @param tipoIdentificacionCodeudor
	 * @param numeroIdentificacionCodeudor
	 * @return
	 */
	public ArrayList<DtoFactura> consultarFacturas(String facturaBusqueda,String tipoIdentificacionDeudor, String numeroIdentificacionDeudor,String tipoIdentificacionCodeudor,String numeroIdentificacionCodeudor);

	public int consultaCodigoDocuGarantia(int ingreso, int consecutivo,String anioConsecutivo);
}
