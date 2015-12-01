package com.princetonsa.dao.util;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import com.princetonsa.dao.sqlbase.util.SqlBaseConsultasBirtDao;
import com.princetonsa.dto.carteraPaciente.DtoDeudor;
import com.princetonsa.dto.carteraPaciente.DtoDocumentosGarantia;

/**
 * Interface de ConsultasBirt
 * @author Giovanny Arias
 * cgarias@princetonsa.com
 */
public interface ConsultasBirtDao{
	
	/**
	 * @param mundo
	 * @return consulta SQL
	 */
	public String furipsAccidenteDeTransito(int ingreso, int codigoMedicoTratante, int codigoReclamacion);
	
	/**
	 * @param mundo
	 * @return consulta SQL
	 */
	public String informeAccidenteDeTransito(int ingreso, int codigoMedicoTratante);
	
	/**
	 * @param codigoReclamacion 
	 * @param mundo
	 * @return consulta SQL
	 */
	public String furipsEventoCatastrofico(int ingreso, int codigoMedicoTratante, int codigoReclamacion);
	
	/**
	 * @param ingreso 
	 * @param mundo
	 * @return consulta SQL
	 */
	public String informeEventoCatastrofico(int ingreso, int codigoMedicoTratante);
	
	/**
	 * @param mundo
	 * @return consulta SQL
	 */
	public String furpro(int ingreso, int codigoReclamacion);
	
	/**
	 * @param mundo
	 * @return consulta SQL
	 */
	public String informeFurpro(int ingreso);
	
	/**
	 * @param ingreso
	 * @param mes en formato 'nombreMes YYYY'
	 * @return consulta SQL
	 */
	public String perfilDeFarmacoterapia(int ingreso, String mes);

	/**
	 * @param tipoBusqueda (codigo, descripcion, claseGrupoSubgrupo)
	 * @param almacen
	 * @param clase
	 * @param grupo
	 * @param subgrupo
	 * @param mostrarExt
	 * @param articulo
	 * @param institucion
	 * @return consulta SQL
	 */
	public String existenciasDeInventario(String tipoBusqueda, int almacen, int clase, int grupo, int subgrupo, String mostrarExt, String articulo, int institucion);
	
	/**
	 * @param tipoBusqueda (codigo, descripcion, claseGrupoSubgrupo)
	 * @param almacen
	 * @param clase
	 * @param grupo
	 * @param subgrupo
	 * @param mostrarExt
	 * @param articulo
	 * @param institucion
	 * @return consulta SQL
	 */
	public String existenciasDeInventario2(
			String tipoBusqueda, int almacen, int clase, int grupo, int subgrupo, String mostrarExt, String articulo, int institucion);
	
	/**
	 * @param oldQuery
	 * @param consecutivosOrdenesInsertadas
	 * @param institucion
	 * @return
	 */
	public String modificarConsultaOrdenesAmbServicios(String oldQuery,  Vector<String> consecutivosOrdenesInsertadas, int institucion);

	/**
	 * Mï¿½todo que retorna la sentencia SQL para crear el reporte de 
	 * Listado de facturas auditadas
	 * @param con
	 * @param fechaAuditoriaInicial
	 * @param fechaAuditoriaFinal
	 * @param facturaInicial
	 * @param facturaFinal
	 * @param codigoConvenio
	 * @param codigoContrato
	 * @param numeroPreGlosa
	 * @param codigoInstitucionInt
	 */
	public String listadoFacturasAuditadas(Connection con, String fechaAuditoriaInicial, String fechaAuditoriaFinal, String facturaInicial, String facturaFinal, int codigoConvenio, int codigoContrato, String numeroPreGlosa, int codigoInstitucionInt);
	
	
	/**
	 * Mï¿½todo que retorna la sentencia SQL para crear el reporte de 
	 * Formato Suba Conciliacion Glosas
	 * @param con
	 * @param fechaAuditoriaInicial
	 * @param fechaAuditoriaFinal
	 * @param facturaInicial
	 * @param facturaFinal
	 * @param codigoConvenio
	 * @param codigoContrato
	 * @param numeroPreGlosa
	 * @param codigoInstitucionInt
	 */
	public String formatoSubaConciliacionFacturas(Connection con, String codConciliacion);
	
	
	/**
	 * Mï¿½todo que retorna la sentencia SQL de facturas pacientes para crear el reporte de 
	 * movimientos totales por tipo de documento
	 * @param con
	 * @param criterios
	 * @return
	 */
	public String listadoFacturasPacientes(Connection con, HashMap criterios);
	
	/**
	 * Mï¿½todo que retorna la sentencia SQL de ingresos para crear el reporte de 
	 * movimientos totales por tipo de documento
	 * @param con
	 * @param criterios
	 * @return
	 */
	public String listadoIngresos(Connection con, HashMap criterios);
	
	/**
	 * Mï¿½todo que retorna la sentencia SQL de facturas varias para crear el reporte de 
	 * movimientos totales por tipo de documento
	 * @param con
	 * @param criterios
	 * @return
	 */
	public String listadoFacturasVarias(Connection con, HashMap criterios);
	
	/**
	 * Mï¿½todo que retorna la sentencia SQL de Recibos Caja para crear el reporte de 
	 * movimientos totales por tipo de documento
	 * @param con
	 * @param criterios
	 * @return
	 */
	public String listadoRecibosCaja(Connection con, HashMap criterios);
	
	/**
	 * Mï¿½todo que retorna la sentencia SQL para crear el reporte de
	 * Impresion Glosa Factura
	 * @param con
	 * @param codigoFactura
	 * @param codigoInstitucionInt
	 */
	public String ImpresionGlosaFactura(Connection con, String codigoFactura, int codigoInstitucionInt, String codigoGlosa);
	
	
	/**
	 * Metodo encargado de organizar la consulta
	 * de datos gnerales del egreso para la
	 * impresion de la boleta de salida
	 * @param cuenta
	 * @return
	 */
	public String consultaEgreso (String cuenta);
	
	/**
	 * Metodo encargado de consulta la informacion
	 * de la facturas para imprimir la boleta de salida
	 * @param ingreso
	 * @return
	 */
	public String consultaFacturas(String ingreso);
	
	/**
	 * Metodo encargado de consulta la informacion
	 * de la cuenta para imprimir la boleta de salida
	 * @param ingreso
	 * @return
	 */
	public String consultaSalida(String ingreso);

	/**
	 * Mï¿½todo que retorna la sentencia SQL para crear el reporte de 
	 * impresion respuesta de glosa 
	 * @param ingreso
	 * @return
	 */
	public String impresionRespuestaGlosaStandar(String codigoRespuestaGlosa);
	
	/**
	 * Mï¿½todo que retorna la sentencia SQL para crear el reporte de 
	 * impresion del detalle correspondiente a la factura de una glosa 
	 * @param codigoGlosa
	 * @param codigoInstitucion
	 * @return
	 */
	public String consultarDetalleFacturasGlosa(String codigoGlosa, int codigoInstitucion);
	
	
	/**
	 * Mï¿½todo que retorna el numero de autorizacion
	 * @param codigoSubcuenta
	 * @return
	 */
	public String consultarNumeroAutorizacion(int codigoCuenta, int codSubCuenta);
	

	/**
	 * Mï¿½todo que la cadena con al info del paciente
	 * @param codigoSubcuenta
	 * @return
	 */
	public String impresionInfoPacienteMedicamentosControlEspecial(int codigoPaciente);
	
	/**
	 * Mï¿½todo que retorna la cadena para consultar la indormacion de medicamentos de control especial
	 * @param nroOrden
	 * @return
	 */
	public String impresionInfoOrden(String nroOrden);
	
	/**
	 * Mï¿½todo que retorna la cadena para consultar la indormacion de medicamentos de control especial
	 * @param nroOrden
	 * @return
	 */
	public String impresionInfoMedico(String idMedico);
	
	/**
	 * Mï¿½todo que retorna la consulta Birt para el reporte detallado por solicitud del estado de 
	 * @param estadoAction
	 * @param remite
	 * @param idSubCuenta
	 * @param numeroSolicitud
	 * @return
	 */
	public String consultaImprimirDetSolConEstadoCuenta(String estadoAction,int remite,String idSubCuenta,String numeroSolicitud);
	
	public String consultaImprimirDetSolConEstadoCuentaEquivalente(String idSubCuenta,String numeroSolicitud);
	
	/**
	 * Mï¿½todo que retorna la consulta de la impresion detallada por item en el estado de la cuenta
	 * @param estadoAction
	 * @param remite
	 * @param idSubCuenta
	 * @param numeroSolicitud
	 * @return
	 */
	public String consultaImprimirItemEstadoCuenta(String estadoAction,int remite,String idSubCuenta,String numeroSolicitud);
	
	/**
	 * M&eacute;todo para retornar la consulta Birt para el reporte detallado por item con despacho de equivalentes 
	 * @author Diana Ruiz
	 * @param idSubCuenta
	 * @param numeroSolicitud
	 * @return
	 */	
	public String consultaImprimirItemEstadoCuentaEquivalente(String idSubCuenta,String numeroSolicitud);
	
	/**
	 * Mï¿½todo para obtener la consulta de la consulta de impresion por item del estado de la cuenta
	 * @param idSubCuenta
	 * @return
	 */
	public String consultaImprimirItem2EstadoCuenta(String idSubCuenta);
	
	/**
	 * Mï¿½todo que retorna la consulta del estado de la cuenta
	 * @param idIngreso
	 * @param remite
	 * @return
	 */
	public String consultaImprimirResSolConEstadoCuenta(String idIngreso,int remite);
	
	/**
	 * M&eacute;todo para retornar la consulta Birt para el reporte resumido por solicitud con despacho de equivalentes 
	 * @author Diana Ruiz
	 * @param idIngreso
	 * @param remite
	 * @return
	 */
	public String consultaImprimirResSolConEstadoCuentaEquivalente(String idIngreso,int remite);
	
	/**
	 * Mï¿½todo para retornar la consulta de la impresion de estado de la cuenta
	 * @param idIngreso
	 * @param remite
	 * @return
	 */
	public String consultaImprimirDetsolCueEstadoCuenta(String idIngreso,int remite);
	
	/**
	 * Mï¿½todo que consulta la impresion del estado de la cuenta
	 * @param idIngreso
	 * @param remite
	 * @return
	 */
	public String consultaImprimirResSolCueEstadoCuenta(String idIngreso,int remite);

	/**
	 * Metodo que devuelve la consulta utilizada en el reporte de consultaInventarioFisicoArticulos1
	 * @param con
	 * @param string
	 * @param almacen
	 * @param patronOrdenar
	 * @return
	 */
	public String articulosConInventarioFisico(Connection con, String articulos,int almacen, String patronOrdenar);

	/**
	 * Metodo que devuelve la consulta utilizada en el reporte de consultaInventarioFisicoArticulos
	 * @param con
	 * @param string
	 * @param almacen
	 * @param patronOrdenar
	 * @return
	 */
	public String articulosSinInventarioFisico(Connection con, String articulos, int almacen, String patronOrdenar);

	public String consultarPazYSalvo(String codPyS);	
	
	public String consultarExtractosDeudor(DtoDeudor dto);

	public String reportePagosCarteraPaciente(String fechaIni, String fechaFin,
			int anioIni, int anioFin, int centroAtencion, String tipoDoc,
			String tipoPeriodo);
	
	public String reportePagosCarteraPacientePagos(int codigoPk,String fechaIni, String fechaFin,
			int anioIni, int anioFin, 
			String tipoPeriodo); 
	
	public String consultarDocumentosCarteraPaciente(DtoDocumentosGarantia dto);

	public String formatoSubaConciliacionEncabezado(Connection con,String codConciliacion);

	public String formatoSubaConciliacionSolicitudes(Connection con,String codConciliacion, String codigoTarifario, int institucion,String tipo);

	public String reporteEstadoCarteraYGlosas(String fechaCorte,String tipoConvenio, int convenio, int institucionInt, int consulta);

	public String reporteFacturasReiteradas(String fechaCte,String tipoconvenio, int convenio, int institucion);

	public String reporteFacturacionEventoRadicar(String centroAtencion,String convenio, String fechaElabIni, String fechaElabFin,String factIni, String factFin, String viaIngreso, int consulta);
	
	public String EdadGlosaFechaRadicacion(HashMap criterios);
	
	public String ReporteFacturasVencidasNoObjetadas(HashMap criterios);
	
	/**
	 * Método para obtener la consulta de la carta de instrucciones de garantía
	 * @param idIngreso
	 * @return
	 */
	public String cartaInstruccionesGarantia(String idIngreso);
	
	/**
	 * Método para obtener las consultas de cada DATESET del reporte 
	 * docgarantiaActaCompromiso.rptdesign
	 * @param parametros
	 * @return
	 */
	public ArrayList<String> actaCompromisoCondicionesIngreso(HashMap parametros);
	
	/**
	 * Método para obtener la consulta del DATaset del reporte docGarantiaPaghaere1.rptdesign
	 * @param parametros
	 * @return
	 */
	public String docGarantiaPagare1(HashMap parametros);
	
	/**
	 * Método para obtener la cosnulta del DATASET de docGarantiaCheque1.rptdesign 
	 * @param parametros
	 * @return
	 */
	public String docGarantiaCheque1(HashMap parametros);
	
	/**
	 * 
	 */
	public String impresionConceptosReciboCaja(HashMap parametros);
	
	/**
	 * 
	 */
	public String impresionTotalesReciboCaja(HashMap parametros);

/**
	 * Metodo que devuelve la consulta utilizada en el reporte de Honorarios Medicos Valor Honorario
	 * @param con
	 * @param string
	 * @param almacen
	 * @param patronOrdenar
	 * @return
	 */
	public String honorariosMedicosValHonorario(HashMap filtros);

	/**
	 * Metodo que devuelve la consulta utilizada en el reporte de Honorarios Medicos Valor Honorario
	 * @param con
	 * @param string
	 * @param almacen
	 * @param patronOrdenar
	 * @return
	 */
	public String honorariosMedicosValFacturaValHonorario(HashMap filtros);
	
	
	/**
	 * 
	 * @param codigo
	 * @return
	 */
	public  String consultarNotasPorCodigo(int codigo);

	public String impresionFacturaVaria(int institucion, int consecutivofacvar,
			boolean manejaMultiInstitucion);
	
	
	

	public String consultaUltimosSignosRE(String idCuenta);
	
	/**
	 * Metodo que devuelve la consulta del DATASET RompimientoPiso
	 * @return
	 */
	public String consultarRompimientoPiso();
	
	/**
	 * Metodo que devuelve la consulta del DATASET RompimientoPiso1
	 * @return
	 */
	public String consultarRompimientoPiso1();
	
	/**
	 * Metodo que devuelve la consulta del DATASET RompimientoCentroCosto
	 * @return
	 */
	public String consultarRompimientoCentroCosto();
	
	/**
	 * Metodo que devuelve la consulta del DATASET RompimientoCentroCosto1
	 * @return
	 */
	public String consultarRompimientoCentroCosto1();
	
	/**
	 * Metodo que devuelve la consulta del DATASET RompimientoEstadoJus
	 * @return
	 */
	public String consultarRompimientoEstadoJus();
	
	/**
	 * Metodo que devuelve la consulta del DATASET RompimientoEstadoJus1
	 * @return
	 */
	public String consultarRompimientoEstadoJus1();
}