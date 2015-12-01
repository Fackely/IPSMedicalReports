package util.reportes;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import org.apache.log4j.Logger;

import util.UtilidadFecha;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.sqlbase.util.SqlBaseConsultasBirtDao;
import com.princetonsa.dao.util.ConsultasBirtDao;
import com.princetonsa.dto.carteraPaciente.DtoDeudor;
import com.princetonsa.dto.carteraPaciente.DtoDocumentosGarantia;
  
/**
 * Mundo de consultas para reportes en BIRT
 * @author Giovanny Arias
 * cgarias@princetonsa.com
 */
public class ConsultasBirt
{
	private static Logger logger = Logger.getLogger(ConsultasBirt.class);
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static ConsultasBirtDao consultasBirtDao;
	
	/**
	 *
	 */
	public ConsultasBirt()
	{
		this.reset();
		this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 *
	 */
	public void reset() 
	{
		
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores vï¿½lidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicializaciï¿½n fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		if (myFactory != null)
		{
			consultasBirtDao = myFactory.getConsultasBirtDao();
			wasInited = (consultasBirtDao != null);
		}
		return wasInited;
	}
	
	/**
	 * Mï¿½todo que retorna la sentencia SQL para crear el reporte de 
	 * FURIPS (Formato Unico de Reclamaciï¿½n de los Prestadores de Servicios de Salud)
	 * Para Accidente de Transito
	 * @param ingreso
	 * @param codigoMedicoTratante 
	 * @return consulta SQL
	 */
	public static String furipsAccidenteDeTransito(int factura, int codigoMedicoTratante,int codigoReclamacion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().furipsAccidenteDeTransito(factura, codigoMedicoTratante,codigoReclamacion);
	}
	
	/**
	 * Mï¿½todo que retorna la sentencia SQL para crear el reporte de 
	 * FURIPS (Formato Unico de Reclamaciï¿½n de los Prestadores de Servicios de Salud)
	 * Para Accidente de Transito
	 * @param ingreso
	 * @param codigoMedicoTratante 
	 * @return consulta SQL
	 */
	public static String informeAccidenteDeTransito(int ingreso, int codigoMedicoTratante)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().informeAccidenteDeTransito(ingreso, codigoMedicoTratante);
	}
	
	/**
	 * Mï¿½todo que retorna la sentencia SQL para crear el reporte de 
	 * FURIPS (Formato Unico de Reclamaciï¿½n de los Prestadores de Servicios de Salud)
	 * Para Evento Catastrï¿½fico
	 * @param ingreso
	 * @return consulta SQL
	 */
	public static String furipsEventoCatastrofico(int ingreso, int codigoMedicoTratante,int codigoReclamacion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().furipsEventoCatastrofico(ingreso, codigoMedicoTratante,codigoReclamacion);
	}
	
	/**
	 * Mï¿½todo que retorna la sentencia SQL para crear el reporte de 
	 * FURIPS (Formato Unico de Reclamaciï¿½n de los Prestadores de Servicios de Salud)
	 * Para Evento Catastrï¿½fico
	 * @param ingreso
	 * @return consulta SQL
	 */
	public static String informeEventoCatastrofico(int ingreso, int codigoMedicoTratante)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().informeEventoCatastrofico(ingreso, codigoMedicoTratante);
	}
	
	/**
	* Mï¿½todo que retorna la sentencia SQL para crear el reporte de 
	* FURPRO (Formato Unico de Reclamaciï¿½n para victimas de eventos terroristas 
	* por servicios de  rehabilitacion y adaptaciï¿½n de prï¿½tesis)
	* @param ingreso
	* @return consulta SQL
	*/
	public static String furpro(int ingreso,int codigoReclamacion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().furpro(ingreso,codigoReclamacion);
	}
	
	/**
	* Mï¿½todo que retorna la sentencia SQL para crear el reporte de 
	* FURPRO (Formato Unico de Reclamaciï¿½n para victimas de eventos terroristas 
	* por servicios de  rehabilitacion y adaptaciï¿½n de prï¿½tesis)
	* @param ingreso
	* @return consulta SQL
	*/
	public static String informeFurpro(int ingreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().informeFurpro(ingreso);
	}
	
	/**
	 * Mï¿½todo que retorna la sentencia SQL para crear el reporte de 
	 * Perfil de Farmacoterapia
	 * @param ingreso
	 * @param mes en formato 'nombreMes YYYY'
	 * @return consulta SQL
	 */
	public static String perfilDeFarmacoterapia(int ingreso, String mes)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().perfilDeFarmacoterapia(ingreso, mes);
	}
	
	/**
	 * Mï¿½todo que retorna la sentencia SQL para crear el reporte de 
	 * Existencias
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
	public static String existenciasDeInventario(String tipoBusqueda, int almacen, int clase, int grupo, int subgrupo, String mostrarExt, String articulo, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().existenciasDeInventario(tipoBusqueda,almacen,clase,grupo,subgrupo,mostrarExt,articulo, institucion);
	}
	
	/**
	 * Mï¿½todo que retorna la sentencia SQL para crear el reporte de 
	 * Existencias con la diferencia que consulta son las existencias en la tabla articulos_almacen
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
	public static String existenciasDeInventario2(String tipoBusqueda, int almacen, int clase, int grupo, int subgrupo, String mostrarExt, String articulo, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().existenciasDeInventario2(tipoBusqueda,almacen,clase,grupo,subgrupo,mostrarExt,articulo, institucion);
	}
	
	/**
	 * 
	 * @param oldQuery
	 * @param consecutivosOrdenesInsertadas
	 * @param institucion
	 * @return
	 */
	public static String modificarConsultaOrdenesAmbServicios(String oldQuery,  Vector<String> consecutivosOrdenesInsertadas, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().modificarConsultaOrdenesAmbServicios(oldQuery, consecutivosOrdenesInsertadas, institucion);
	}

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
	public static String listadoFacturasAuditadas(Connection con, String fechaAuditoriaInicial, String fechaAuditoriaFinal, String facturaInicial, String facturaFinal, int codigoConvenio, int codigoContrato, String numeroPreGlosa, int codigoInstitucionInt) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().listadoFacturasAuditadas(con, fechaAuditoriaInicial, fechaAuditoriaFinal, facturaInicial, facturaFinal, codigoConvenio, codigoContrato, numeroPreGlosa, codigoInstitucionInt);
	}
	
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
	public static String formatoSubaConciliacion(Connection con, String codConciliacion, String codigoTarifario, int institucion, String tipo) 
	{
		if(tipo.equals("e"))
			return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().formatoSubaConciliacionEncabezado(con, codConciliacion);
		if(tipo.equals("f"))
			return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().formatoSubaConciliacionFacturas(con, codConciliacion);
		if(tipo.equals("s"))
			return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().formatoSubaConciliacionSolicitudes(con, codConciliacion, codigoTarifario, institucion, tipo);
		return null;
	}
	
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
	public static String reportePagosCarteraPaciente(String fechaIni, String fechaFin, int anioIni, int anioFin, int centroAtencion, String tipoDoc, String tipoPeriodo) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().reportePagosCarteraPaciente(fechaIni, fechaFin, anioIni, anioFin, centroAtencion, tipoDoc, tipoPeriodo);
	}
	
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
	public static String consultarPazYSalvo(String codPyS) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().consultarPazYSalvo(codPyS);
	}
	
	/**
	 * Mï¿½todo que retorna la sentencia SQL de facturas pacientes para crear el reporte de 
	 * movimientos totales por tipo de documento
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static String listadoFacturasPacientes(Connection con, HashMap criterios) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().listadoFacturasPacientes(con, criterios);
	}
	
	/**
	 * Mï¿½todo que retorna la sentencia SQL de ingresos para crear el reporte de 
	 * movimientos totales por tipo de documento
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static String listadoIngresos(Connection con, HashMap criterios)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().listadoIngresos(con, criterios);
	}
	
	/**
	 * Mï¿½todo que retorna la sentencia SQL de facturas varias para crear el reporte de 
	 * movimientos totales por tipo de documento
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static String listadoFacturasVarias(Connection con, HashMap criterios)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().listadoFacturasVarias(con, criterios);
	}
	
	/**
	 * Mï¿½todo que retorna la sentencia SQL de Recibos Caja para crear el reporte de 
	 * movimientos totales por tipo de documento
	 * @param con
	 * @param criterios
	 * @return
	 */
	public static String listadoRecibosCaja(Connection con, HashMap criterios)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().listadoRecibosCaja(con, criterios);
	}
	
	public static String ImpresionGlosaFactura(Connection con, String codigoFactura, int codigoInstitucionInt, String codigoGlosa)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().ImpresionGlosaFactura(con, codigoFactura, codigoInstitucionInt, codigoGlosa);
	}
	
	/**
	 * Metodo encargado de organizar la consulta
	 * de datos gnerales del egreso para la
	 * impresion de la boleta de salida
	 * @param cuenta
	 * @return
	 */
	public static String consultaEgreso (String cuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().consultaEgreso(cuenta);
	}
	
	/**
	 * Metodo encargado de consulta la informacion
	 * de la facturas para imprimir la boleta de salida
	 * @param ingreso
	 * @return
	 */
	public static String consultaFacturas(String ingreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().consultaFacturas(ingreso);
	}
	
	/**
	 * Metodo encargado de consulta la informacion
	 * de la cuenta para imprimir la boleta de salida
	 * @param ingreso
	 * @return
	 */
	public static String consultaSalida(String ingreso){
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().consultaSalida(ingreso);
	}
	
	
	public static String reporteEstadoCarteraYGlosas(String fechaCorte, String tipoConvenio, int convenio, int institucionInt, int consulta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().reporteEstadoCarteraYGlosas(fechaCorte, tipoConvenio, convenio, institucionInt, consulta);
	}
	
	/**
	 * Metodo encargado de retornar la consulta para reporte de facturacion evento por radicar
	 * @param fechaCorte
	 * @param tipoConvenio
	 * @param convenio
	 * @param institucionInt
	 * @param consulta
	 * @return
	 */
	public static String reporteFacturacionEventoRadicar(String centroAtencion, String Convenio, String fechaElabIni, String fechaElabFin, String factIni, String factFin, String viaIngreso, int consulta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().reporteFacturacionEventoRadicar(centroAtencion, Convenio, fechaElabIni, fechaElabFin, factIni, factFin, viaIngreso, consulta);
	}
		
	/**
	 * Mï¿½todo que retorna la sentencia SQL para crear el reporte de 
	 * impresion respuesta de glosa 
	 * @param ingreso
	 * @return
	 */
	public static String impresionRespuestaGlosaStandar(String codigoRespuestaGlosa)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().impresionRespuestaGlosaStandar(codigoRespuestaGlosa);
	}
	
	/**
	 * Mï¿½todo que retorna la sentencia SQL para crear el reporte de 
	 * impresion del detalle correspondiente a la factura de una glosa
	 * @param codigoGlosa
	 * @param codigoInstitucion
	 * @return
	 */
	public static String consultarDetalleFacturasGlosa(String codigoGlosa, int codigoInstitucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().consultarDetalleFacturasGlosa(codigoGlosa,codigoInstitucion);
	}
	
	/**
	 * Metodo qeu retorna la sentencia SQL para crear el reporte de Consulta Admision
	 * @param codigoCuenta
	 * @return
	 */
	public static String consultarNumeroAutorizacion(int codigoCuenta, int codSubCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().consultarNumeroAutorizacion(codigoCuenta, codSubCuenta);
		
	}
	
	
	/**
	 * Metodo que retorna la sentencia SQL para crear el reporte de Prescripcion Ambulatoria de Medicamentos con al info del paciente
	 * @param codigoPaciente
	 * @return
	 */
	public static String impresionInfoPacienteMedicamentosControlEspecial(int codigoPaciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().impresionInfoPacienteMedicamentosControlEspecial(codigoPaciente);
	}
	
	/**
	 * Metodo que retorna la sentencia SQL para crear el reporte de Prescripcion Ambulatoria de Medicamentos con los medicamentos de control especial
	 * @param codigoPaciente
	 * @return
	 */
	public static String impresionInfoOrden(String nroOrden)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().impresionInfoOrden(nroOrden);
	}
	
	/**
	 * Metodo que retorna la sentencia SQL para crear el reporte de Prescripcion Ambulatoria de Medicamentos con los medicamentos de control especial
	 * @param codigoPaciente
	 * @return
	 */
	public static String impresionInfoMedico(String idMedico)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().impresionInfoMedico(idMedico);
	}
	
	/**
	 * Mï¿½todo que retorna la consulta Birt para el reporte detallado por solicitud del estado de 
	 * @param estadoAction
	 * @param remite
	 * @param idSubCuenta
	 * @param numeroSolicitud
	 * @return
	 */
	public static String consultaImprimirDetSolConEstadoCuenta(String estadoAction,int remite,String idSubCuenta,String numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().consultaImprimirDetSolConEstadoCuenta(estadoAction, remite, idSubCuenta, numeroSolicitud);
	}
	
	/**
	 * M&eacute;todo para retornar la consulta Birt para el reporte detallado por solicitud con despacho de equivalentes 
	 * @author Diana Ruiz
	 * @param idSubCuenta
	 * @param numeroSolicitud
	 * @return
	 */
	
	public static String consultaImprimirDetSolConEstadoCuentaEquivalente(String idSubCuenta,String numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().consultaImprimirDetSolConEstadoCuentaEquivalente(idSubCuenta, numeroSolicitud);
	}
	
	/**
	 * Mï¿½todo que retorna la consulta de la impresion detallada por item en el estado de la cuenta
	 * @param estadoAction
	 * @param remite
	 * @param idSubCuenta
	 * @param numeroSolicitud
	 * @return
	 */
	public static String consultaImprimirItemEstadoCuenta(String estadoAction,int remite,String idSubCuenta,String numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().consultaImprimirItemEstadoCuenta(estadoAction, remite, idSubCuenta, numeroSolicitud);
	}
	
	/**
	 * M&eacute;todo para retornar la consulta Birt para el reporte detallado por item con despacho de equivalentes 
	 * @author Diana Ruiz
	 * @param idSubCuenta
	 * @param numeroSolicitud
	 * @return
	 */
	public static String consultaImprimirItemEstadoCuentaEquivalente(String idSubCuenta,String numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().consultaImprimirItemEstadoCuentaEquivalente(idSubCuenta, numeroSolicitud);
	}
	
	/**
	 * Mï¿½todo para obtener la consulta de la consulta de impresion por item del estado de la cuenta
	 * @param idSubCuenta
	 * @return
	 */
	public static String consultaImprimirItem2EstadoCuenta(String idSubCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().consultaImprimirItem2EstadoCuenta(idSubCuenta);
	}
	
	/**
	 * Mï¿½todo que retorna la consulta del estado de la cuenta
	 * @param idIngreso
	 * @param remite
	 * @return
	 */
	public static String consultaImprimirResSolConEstadoCuenta(String idIngreso,int remite)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().consultaImprimirResSolConEstadoCuenta(idIngreso, remite);
	}
	
	/**
	 * M&eacute;todo para retornar la consulta Birt para el reporte resumido por solicitud con despacho de equivalentes 
	 * @author Diana Ruiz
	 * @param idIngreso
	 * @param remite
	 * @return
	 */
	
	public static String consultaImprimirResSolConEstadoCuentaEquivalente(String idIngreso,int remite)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().consultaImprimirResSolConEstadoCuentaEquivalente(idIngreso, remite);
	}
	
	
	/**
	 * Mï¿½todo para retornar la consulta de la impresion de estado de la cuenta
	 * @param idIngreso
	 * @param remite
	 * @return
	 */
	public static String consultaImprimirDetsolCueEstadoCuenta(String idIngreso,int remite)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().consultaImprimirDetsolCueEstadoCuenta(idIngreso, remite);
	}
	
	/**
	 * Mï¿½todo que consulta la impresion del estado de la cuenta
	 * @param idIngreso
	 * @param remite
	 * @return
	 */
	public static  String consultaImprimirResSolCueEstadoCuenta(String idIngreso,int remite)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().consultaImprimirResSolCueEstadoCuenta(idIngreso, remite);
	}

	/**
	 * Metodo que devuelve la consulta utilizada en el reporte de consultaInventarioFisicoArticulos1
	 * @param con
	 * @param string
	 * @param almacen
	 * @param patronOrdenar
	 * @return
	 */
	public static String articulosConInventarioFisico(Connection con,String articulos, int almacen, String patronOrdenar) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().articulosConInventarioFisico(con,articulos,almacen,patronOrdenar);
	}

	/**
	 * Metodo que devuelve la consulta utilizada en el reporte de ConsultaInventarioFisicoArticulos
	 * @param con
	 * @param string
	 * @param almacen
	 * @param patronOrdenar
	 * @return
	 */
	public static String articulosSinInventarioFisico(Connection con, String articulos, int almacen, String patronOrdenar) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().articulosSinInventarioFisico(con,articulos,almacen,patronOrdenar);
	}
	
	public static String consultarExtractosDeudor(Connection con,DtoDeudor dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().consultarExtractosDeudor(dto);
	}
	
	public static String reportePagosCarteraPacientePagos(int codigoPk,String fechaIni, String fechaFin,int anioIni, int anioFin,String tipoPeriodo){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().reportePagosCarteraPacientePagos(codigoPk, fechaIni, fechaFin, anioIni, anioFin, tipoPeriodo);
	}
	
	public static String consultarDocumentosCarteraPaciente(DtoDocumentosGarantia dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().consultarDocumentosCarteraPaciente(dto);
	}

	public static String reporteFacturasReiteradas(String fechaCte, String tipoconvenio,int convenio, int institucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().reporteFacturasReiteradas(fechaCte, tipoconvenio, convenio, institucion);
	}
	
	public static String EdadGlosaFechaRadicacion(HashMap criterios)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().EdadGlosaFechaRadicacion(criterios);
	}
	
	public static String ReporteFacturasVencidasNoObjetadas(HashMap criterios)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().ReporteFacturasVencidasNoObjetadas(criterios);
	}
	
	/**
	 * Método para obtener la consulta de la carta de instrucciones de garantía
	 * @param idIngreso
	 * @return
	 */
	public static String cartaInstruccionesGarantia(String idIngreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().cartaInstruccionesGarantia(idIngreso);
	}
	
	/**
	 * Método para obtener las consultas de cada DATESET del reporte 
	 * docgarantiaActaCompromiso.rptdesign
	 * @param parametros
	 * @return
	 */
	public static ArrayList<String> actaCompromisoCondicionesIngreso(String idIngreso,String tipoDocumento,String consecutivo, String anioConsecutivo)
	{
		HashMap parametros = new HashMap();
		parametros.put("idIngreso", idIngreso);
		parametros.put("tipoDocumento", tipoDocumento);
		parametros.put("consecutivo", consecutivo);
		parametros.put("anioConsecutivo", anioConsecutivo);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().actaCompromisoCondicionesIngreso(parametros);
	}
	
	/**
	 * Método para obtener la consulta del DATaset del reporte docGarantiaPaghaere1.rptdesign
	 * @param parametros
	 * @return
	 */
	public static String docGarantiaPagare1(String idIngreso,String tipoDocumento,String estado)
	{
		HashMap parametros = new HashMap();
		parametros.put("idIngreso", idIngreso);
		parametros.put("tipoDocumento", tipoDocumento);
		parametros.put("estado", estado);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().docGarantiaPagare1(parametros);
	}
	
	/**
	 * Método para obtener la cosnulta del DATASET de docGarantiaCheque1.rptdesign 
	 * @param parametros
	 * @return
	 */
	public static String docGarantiaCheque1(String idIngreso,String consecutivo,String tipoDocumento,String anioConsecutivo,String codigoInstitucion)
	{
		HashMap parametros = new HashMap();
		parametros.put("idIngreso", idIngreso);
		parametros.put("consecutivo", consecutivo);
		parametros.put("tipoDocumento", tipoDocumento);
		parametros.put("anioConsecutivo", anioConsecutivo);
		parametros.put("codigoInstitucion", codigoInstitucion);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().docGarantiaCheque1(parametros);
	}
	
	/**
	 * 
	 */
	public static String impresionConceptosReciboCaja(int institucion,String numeroRC)
	{
		HashMap parametros = new HashMap();
		parametros.put("nrorc", numeroRC);
		parametros.put("institucion", institucion);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().impresionConceptosReciboCaja(parametros);
	}
	
	
	/**
	 * 
	 */
	public static String impresionFacturaVaria(int institucion,int consecutivofacvar, boolean manejaMultiInstitucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().impresionFacturaVaria(institucion, consecutivofacvar, manejaMultiInstitucion);
	}
	
	
	/**
	 * 
	 */
	public static String impresionTotalesReciboCaja(int institucion,String numeroRC)
	{
		HashMap parametros = new HashMap();
		parametros.put("nrorc", numeroRC);
		parametros.put("institucion", institucion);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().impresionTotalesReciboCaja(parametros);
	}
	
	/**
	 * Metodo que devuelve la consulta utilizada en el reporte de Honorarios Medicos Valor Honorario
	 * @param con
	 * @param string
	 * @param almacen
	 * @param patronOrdenar
	 * @return
	 */
	public static String honorariosMedicosValHonorario(HashMap filtros) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().honorariosMedicosValHonorario(filtros);
	}
	
	/**
	 * Metodo que devuelve la consulta utilizada en el reporte de Honorarios Medicos Valor Honorario
	 * @param con
	 * @param string
	 * @param almacen
	 * @param patronOrdenar
	 * @return
	 */
	public static String honorariosMedicosValFacturaValHonorario(HashMap filtros) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().honorariosMedicosValFacturaValHonorario(filtros);
	}
	
	
	
	/**
	 * CONSULTAR LAS NOTAS POR CODIGO
	 * @param codigo
	 * @return
	 */
	public static String consultarNotasPorCodigo(int codigo) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().consultarNotasPorCodigo(codigo);
	}
	
	
	/**
	 * 
	 * @param string
	 * @return
	 */
	public static String consultaUltimosSignosRE(String idCuenta) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().consultaUltimosSignosRE(idCuenta);
	}
	
	/**
	 * Metodo que devuelve la consulta del DATASET RompimientoPiso
	 * @return
	 */
	public static String consultarRompimientoPiso()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().consultarRompimientoPiso();
	}
	
	/**
	 * Metodo que devuelve la consulta del DATASET RompimientoPiso1
	 * @return
	 */
	public static String consultarRompimientoPiso1() 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().consultarRompimientoPiso1();
	}
	
	/**
	 * Metodo que devuelve la consulta del DATASET RompimientoCentroCosto
	 * @return
	 */
	public static String consultarRompimientoCentroCosto() 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().consultarRompimientoCentroCosto();
	}
	
	/**
	 * Metodo que devuelve la consulta del DATASET RompimientoCentroCosto1
	 * @return
	 */
	public static String consultarRompimientoCentroCosto1() 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().consultarRompimientoCentroCosto1();
	}
	
	/**
	 * Metodo que devuelve la consulta del DATASET RompimientoEstadoJus
	 * @return
	 */
	public static String consultarRompimientoEstadoJus() 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().consultarRompimientoEstadoJus();
	}
	
	/**
	 * Metodo que devuelve la consulta del DATASET RompimientoEstadoJus1
	 * @return
	 */
	public static String consultarRompimientoEstadoJus1() 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultasBirtDao().consultarRompimientoEstadoJus1();
	}
}	


