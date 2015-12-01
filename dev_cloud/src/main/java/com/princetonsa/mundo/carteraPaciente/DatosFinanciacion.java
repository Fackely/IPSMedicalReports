package com.princetonsa.mundo.carteraPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.carterapaciente.DatosFinanciacionDao;
import com.princetonsa.dto.carteraPaciente.DtoDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoDeudor;
import com.princetonsa.dto.carteraPaciente.DtoDeudoresDatosFinan;
import com.princetonsa.dto.facturacion.DtoFactura;

/**
 * 
 * @author V�ctor G�mez L.
 *
 */
public class DatosFinanciacion 
{
	
	static Logger logger = Logger.getLogger(DatosFinanciacion.class);
	
	/**
	 * Instancia DAO
	 * */
	public static DatosFinanciacionDao getDatosFinanciacionDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDatosFinanciacionDao();		
	}
	
	/**
	 * insercion datos financiacion
	 * @param con
	 * @param dto
	 * @return
	 */
	public static int insertDatosFinanciacion(Connection con, DtoDatosFinanciacion dto)
	{
		return getDatosFinanciacionDao().insertDatosFinanciacion(con, dto);
	}
	
	/**
	 * metodo que carga el deudor y/o codeudor segun el documento de garantia asociado
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static DtoDeudoresDatosFinan obtenerDatosDeudorCO(Connection con, int ingreso, int cod_institucion, String clase_deudor)
	{
		HashMap parametros = new HashMap();
		parametros.put("ingreso", ingreso);
		parametros.put("institucion", cod_institucion);
		parametros.put("clase_deudor", clase_deudor);
		return getDatosFinanciacionDao().obtenerDatosDeudorCO(con, parametros);
	}
	
	/**
	 * Obtener datos deudor y/o Codeudor
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static DtoDeudoresDatosFinan obtenerDatosDeudor(Connection con, int consecutivo_factura, String clase_deudor)
	{
		HashMap parametros = new HashMap();
		parametros.put("consecutivo_factura", consecutivo_factura);
		parametros.put("clase_deudor", clase_deudor);
		return getDatosFinanciacionDao().obtenerDatosDeudor(con, parametros);
	}
	
	/**
	 * Consulta los datos del responsable del paciente si existe
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static DtoDeudoresDatosFinan obtenerDatosResponsable(Connection con, int consecutivo_factura, String clase_deudor)
	{
		HashMap parametros = new HashMap();
		parametros.put("consecutivo_factura", consecutivo_factura);
		parametros.put("clase_deudor", clase_deudor);
		return getDatosFinanciacionDao().obtenerDatosResponsable(con, parametros);
	}
	
	/**
	 * verificar Existencia persona
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static DtoDeudoresDatosFinan verificarExistenciaPer(Connection con, String tipo_identificacion, String numero_identificacion, String clase_deudor)
	{
		HashMap parametros = new HashMap();
		parametros.put("tipo_identificacion", tipo_identificacion);
		parametros.put("numero_identificacion", numero_identificacion);
		parametros.put("clase_deudor", clase_deudor);
		return getDatosFinanciacionDao().verificarExistenciaPer(con, parametros);
	}
	
	/**
	 * obtener ingreso de facturas
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static HashMap obtenerIngresoFactura(Connection con, int consecutivo_factura)
	{
		HashMap parametros = new HashMap();
		parametros.put("consecutivo_factura", consecutivo_factura);
		return getDatosFinanciacionDao().obtenerIngresoFactura(con, parametros);
	}
	
	/**
	 * Actualizar deudor
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean actualizarDeudor(Connection con, DtoDeudoresDatosFinan dto)
	{
		return getDatosFinanciacionDao().actualizarDeudor(con, dto);
	}
	
	/**
	 * verificar la existencia de un documento de garantia asociado al ingreso de la factura
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static HashMap verificarDocumentoGarantia(Connection con, int consecutivo_factura, String tipo_documento)
	{
		HashMap parametros = new  HashMap();
		parametros.put("consecutivo_factura", consecutivo_factura);
		parametros.put("tipo_documento", tipo_documento);
		return getDatosFinanciacionDao().verificarDocumentoGarantia(con, parametros);
	}
	
	/**
	 * busquesa deudores
	 * @param con
	 * @param dtoe
	 * @return
	 */
	public static ArrayList<DtoDeudor> busquedaDeudores(Connection con, DtoDeudor dtoe)
	{
		return getDatosFinanciacionDao().busquedaDeudores(con, dtoe);
	}
	
	/**
	 * actualizar datos Paciente
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean actualizarPaciente(Connection con, DtoDeudoresDatosFinan dto)
	{
		return getDatosFinanciacionDao().actualizarPaciente(con, dto);
	}
	
	/**
	 * actualizar responsable paciente
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean actualizarResponsablePac(Connection con, DtoDeudoresDatosFinan dto)
	{
		return getDatosFinanciacionDao().actualizarResponsablePac(con, dto);
	}
	
	/**
	 * verifica la existencia de responsable de paciente, segun el ingreso de la factura
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static String verificarResponsablePacIngFac(Connection con, int consecutivo_factura)
	{
		HashMap parametros = new HashMap();
		parametros.put("consecutivo_factura", consecutivo_factura);
		return getDatosFinanciacionDao().verificarResponsablePacIngFac(con, parametros);
	}
	
	/**
	 * actualizacion de documento de garantia segun requerimiento de anulacion de recibo de caja conconcepto paciente
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static boolean actualizarDocGarantiaAnuRC(Connection con, int ingreso, String tipo_documento, int consecutivo, String anio_consecutivo)
	{
		HashMap parametros = new HashMap();
		parametros.put("ingreso", ingreso);
		parametros.put("tipo_documento", tipo_documento);
		parametros.put("consecutivo", consecutivo);
		parametros.put("anio_consecutivo", anio_consecutivo);
		return getDatosFinanciacionDao().actualizarDocGarantiaAnuRC(con, parametros);
	}
	
	/**
	 * actualizar los datos del documento de garantia
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static boolean actualizarDocGarAsociado(Connection con, String cartera, double valor, String usuario_gen, String usuario_mod, int ingreso, String tipo_documento, int consecutivo, String anio_consecutivo)
	{
		HashMap parametros = new HashMap();
		parametros.put("cartera", cartera);
		parametros.put("valor", valor);
		parametros.put("usuario_genera", usuario_gen);
		parametros.put("usuario_modifica", usuario_mod);
		parametros.put("ingreso", ingreso);
		parametros.put("tipo_documento", tipo_documento);
		parametros.put("consecutivo", consecutivo);
		parametros.put("anio_consecutivo", anio_consecutivo);
		return getDatosFinanciacionDao().actualizarDocGarAsociado(con, parametros);
	}

	
	/**
	 * 
	 * @param deudor
	 * @return
	 */
	public static int insertarDeudorCoGenerico(DtoDeudoresDatosFinan deudor) 
	{
		Connection con=UtilidadBD.abrirConexion();
		int resultado=DocumentosGarantia.insertarDeudorCo(con, llenarParametrosDeudorCodeudor(deudor));
		UtilidadBD.closeConnection(con);
		return resultado;
		
	}
	
	
	/**
	 * 
	 * @param deudor
	 * @return
	 */
	public static int insertarDeudorCoGenerico(Connection con,DtoDeudoresDatosFinan deudor) 
	{
		return DocumentosGarantia.insertarDeudorCo(con, llenarParametrosDeudorCodeudor(deudor));
		
	}
	
	/**
	 * llenar parametros deudor ingreso
	 * @param dto
	 * @return
	 */
	public static HashMap llenarParametrosDeudorCodeudor(DtoDeudoresDatosFinan dto)
	{
		HashMap parametros = new HashMap();
		logger.info("entra llenar deudor ingreso");
		parametros.put("institucion", dto.getInstitucion());
		parametros.put("claseDeudorCo", dto.getClaseDeudor());
		parametros.put("tipoDeudorCo", dto.getTipoDeudor());
		parametros.put("tipoIdentificacion", dto.getTipoIdentificacion());
		parametros.put("numeroIdentificacion", dto.getNumeroIdentificacion());
		parametros.put("codigoPais", dto.getPaisID());
		parametros.put("codigoDepartamento", dto.getDepartamentoID());
		parametros.put("codigoCiudad", dto.getCiudadID());
		parametros.put("primerNombre", dto.getPrimerNombre());
		parametros.put("segundoNombre", dto.getSegundoNombre());
		parametros.put("primerApellido", dto.getPrimerApellido());
		parametros.put("segundoApellido", dto.getSegundoApellido());
		parametros.put("direccionReside", dto.getDireccion());
		parametros.put("telefonoReside", dto.getTelefono());
		parametros.put("tipoOcupacion", dto.getOcupacion());
		parametros.put("ocupacion", dto.getDetalleocupacion());
		parametros.put("empresa", dto.getEmpresa());
		parametros.put("cargo", dto.getCargo());
		parametros.put("antiguedad", dto.getAntiguedad());
		parametros.put("direccionOficina", dto.getDireccionOficina());
		parametros.put("telefonoOficina", dto.getTelefonoOficina());
		parametros.put("usuarioModifica", dto.getUsuarioModifica());
		parametros.put("fechaModifica", UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		parametros.put("horaModifica", UtilidadFecha.getHoraActual());
		parametros.put("nombresReferencia", dto.getNombresReferenciaFamiliar());
		parametros.put("direccionReferencia", dto.getDireccionReferenciaFamiliar());
		parametros.put("telefonoReferencia", dto.getTelefonoReferenciaFamiliar());
		
		
		Utilidades.imprimirMapa(parametros);
		
		return parametros;
	}

	/**
	 * 
	 * @param tipoDocGarantia
	 * @return
	 */
	public static String obtenerPrimerConsecutivoUsadoDocumento(String tipoDocGarantia,int institucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDatosFinanciacionDao().obtenerPrimerConsecutivoUsadoDocumento(tipoDocGarantia,institucion);
	}

	/**
	 * 
	 * @param tipoDocGarantiaT\
	 * @param consecutivoDocumento
	 * @param institucion
	 * @return
	 */
	public static boolean esConsecutivoDocumentoUsado(String tipoDocGarantia, String consecutivoDocumento,int institucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDatosFinanciacionDao().esConsecutivoDocumentoUsado(tipoDocGarantia,consecutivoDocumento,institucion);
	}

	
	/**
	 * 
	 * @param facturaBusqueda
	 * @param tipoIdentificacionDeudor
	 * @param numeroIdentificacionDeudor
	 * @param tipoIdentificacionCodeudor
	 * @param numeroIdentificacionCodeudor
	 * @return
	 */
	public static ArrayList<DtoFactura> consultarFacturas(String facturaBusqueda, String tipoIdentificacionDeudor,String numeroIdentificacionDeudor, String tipoIdentificacionCodeudor,String numeroIdentificacionCodeudor) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDatosFinanciacionDao().consultarFacturas(facturaBusqueda,tipoIdentificacionDeudor,numeroIdentificacionDeudor,tipoIdentificacionCodeudor,numeroIdentificacionCodeudor);
	}

	public static int consultaCodigoDocuGarantia(int ingreso,int consecutivo, String anioConsecutivo) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDatosFinanciacionDao().consultaCodigoDocuGarantia(ingreso,consecutivo,anioConsecutivo);
	}
}
