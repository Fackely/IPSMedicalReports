/*
 * @(#)PostgresqlContratoDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import util.UtilidadFecha;

import com.princetonsa.dao.ContratoDao;
import com.princetonsa.dao.sqlbase.SqlBaseContratoDao;
import com.princetonsa.decorator.ResultSetDecorator;
import com.servinte.axioma.fwk.exception.BDException;

/**
 * Implementacion postgresql de las funciones de acceso a la fuente de datos
 * para un contrato
 *
 * @version 1.0, Abril 30 / 2004
 */
public class PostgresqlContratoDao implements ContratoDao
{
	/**
	 *  Insertar un contrato
	 */
	private final static String insertarContratoStr= 	"INSERT INTO contratos" +
																				"(" +
																				"codigo, " +
																				"convenio, " +
																				"fecha_inicial, " +
																				"fecha_final, " +
																				"numero_contrato, " +
																				"valor, " +
																				"acumulado, " +
																				"fecha_firma, " +
																				"limite_radicacion, " +
																				"dias_radicacion, " +
																				"controla_anticipos, " +
																				"maneja_cobertura, " +
																				"tipo_pago, " +
																				"upc, " +
																				"porcentaje_pyp," +
																				"contrato_secretaria," +
																				"porcentaje_upc, " +
																				"base," +
																				"req_auto_no_cobertura, " +
																				"sin_contrato, " +
																				"observaciones, " +
																				"paciente_paga_atencion,"+
																				"validar_abono_atencion_odo,"+
																				"maneja_tarifas_x_ca " +
																				") "+
																				" VALUES (nextval('seq_contratos')," +
																				"?,?,?,?," +
																				"?,?,?,?," +
																				"?,?,?,?," +
																				"?,?,?,?, " +
																				"?,?,?,?,?,?,?)";
 																				 
	/**
	 * inserta un contrato 
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param codigoConvenio, int, captura el convenio al cual corresponde el contrato
	 * @param numeroContrato, String, n�mero del contrato
	 * @param fechaInicial, String, fecha inicial del contrato
	 * @param fechaFinal, String, fecha final del contrato
	 * @return  int, 0 si  no inserta, 1 si inserta
	 */
	public  int insertar(	Connection con, 
										int codigoConvenio, 
										String numeroContrato, 
										String fechaInicial, 
										String  fechaFinal, 
										double valorContrato,
										double valorAcumulado,
										String fechaFirmaContrato,
										String diaLimiteRadicacion,
										String diasRadicacion,
										String controlaAnticipos,
										String manejaCobertura,
										String codigoTipoPago,
										String upc,
										String pyp,
										String contratoSecretaria,
										HashMap nivelAtencionMap,
										String porcentajeUpc,
										String base,
										String requiereAutorizacionNoCobertura,
										String sinContrato,
										String frmObservaciones,
										String pacientePagaAtencion, 
										String validarAbonoAtencionOdo, 
										boolean manejaTarifasXCA
									)
	{
		return SqlBaseContratoDao.insertar(	 con,  codigoConvenio, numeroContrato, 
																		 fechaInicial,  fechaFinal, valorContrato, valorAcumulado, 
																		 fechaFirmaContrato, diaLimiteRadicacion,
																		 diasRadicacion, controlaAnticipos,
																		 manejaCobertura, codigoTipoPago,
																		 upc,
																		 pyp,
																		 contratoSecretaria,
																		 insertarContratoStr,
																		 nivelAtencionMap,
																		 porcentajeUpc,
																		 base,
																		 requiereAutorizacionNoCobertura,
																		 sinContrato,
																		 frmObservaciones,pacientePagaAtencion,validarAbonoAtencionOdo,
																		 manejaTarifasXCA);
	}
										
	
	
	/**
	 *  M�todo que indica si el contrato puede 
	 *  ser insertado o no en un rango de fechas
	 *  que pertenezcan al mismo convenio
	*/
	public boolean puedoInsertarContrato(	Connection con, 
																		int convenio, 
																		String fechaInicial, 
																		String fechaFinal,
																		String estadoInsertaOModifica,
																		String fechaInicialAntigua,
																		String fechaFinalAntigua
																		) 
	{
		return SqlBaseContratoDao.puedoInsertarContrato(con,convenio,fechaInicial,fechaFinal,estadoInsertaOModifica, fechaInicialAntigua, fechaFinalAntigua);																	
	}
	
	
	/**
	 * M�todo que  carga  los datos de un contrato para mostrarlos en el resumen
	 * en una BD PostgresSQL o Hsqldb 
	 */
	public ResultSetDecorator cargar(Connection con, String codigoContrato) throws SQLException
	{
		return SqlBaseContratoDao.cargar(con,codigoContrato);
	}
	
	
	/**
	 * Modifica un contrato dado su c�digo con los param�tros dados.
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param codigo, int, codigo del contrato
	 * @param numeroContrato, String, n�mero del contrato
	 * @param fechaInicial, String, fecha inicial del contrato
	 * @param fechaFinal, String, fecha final del contrato
	 * @return  int, 0 si  no inserta, 1 si inserta
	 */																				
	public  int modificar (	Connection con, 
											int codigo, 
											String numeroContrato,
											String fechaInicial, 
											String  fechaFinal, 
											double valorContrato,
											double valorAcumulado,
											String fechaFirmaContrato,
											String diaLimiteRadicacion,
											String diasRadicacion,
											String controlaAnticipos,
											String manejaCobertura,
											String codigoTipoPago,
											String upc,
											String pyp,
											String contratoSecretaria,
											HashMap nivelAtencionMap,
											String porcentajeUpc,
											String base,
											String requiereAutorizacionNoCobertura, 
											String sinContrato, 
											String frmObservaciones,

											String pacientePagaAtencion, 
											String validarAbonoAtencionOdo,
											boolean manejaTarifasXCA)
	{
		return SqlBaseContratoDao.modificar(	 con,  codigo, numeroContrato, 
																			fechaInicial,  fechaFinal,  
																			valorContrato, valorAcumulado,
																			fechaFirmaContrato, diaLimiteRadicacion,
																			diasRadicacion, controlaAnticipos,
																			manejaCobertura,
																			codigoTipoPago, upc, pyp,
																			contratoSecretaria, nivelAtencionMap,
																			porcentajeUpc, base, requiereAutorizacionNoCobertura, sinContrato,
																			frmObservaciones,pacientePagaAtencion,validarAbonoAtencionOdo, manejaTarifasXCA)	;									 
	}

	/**
	 * M�todo que contiene el Resulset de los datos de la tabla contratos
	 * para posteriorm/t ser mostrados en el pager
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @return Resultset con todos los datos de la tabla contratos
	 * @throws SQLException
	 */
	public  ResultSetDecorator listado(Connection con, int codigoInstitucion) throws SQLException
	{
		return SqlBaseContratoDao.listado(con, codigoInstitucion);
	}
	
	/**
	 * M�todo que contiene el Resulset de todas las empresas buscadas
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @return Resultset con todos los datos de la tabla empresas
	 * @throws SQLException
	 */
	public ResultSetDecorator busqueda(	Connection con, 
													String nombreConvenio, 
													String numeroContrato,
													String eleccionFechaBusqueda,
													String fechaInicial, 
													String  fechaFinal, 
													int codigoInstitucion,
													double valorContrato,
													double valorAcumulado,
													String nombreTipoPago,
													String upc,
													String pyp,
													String contratoSecretaria,
													String busquedaNivelAtencion,
													String porcentajeUpc,
													String base,
													String requiereAutorizacionNoCobertura, 
													String sinContrato, String esquemaTarInventario, String esquemaTarProcedimiento,
													String frmObservaciones,
													String manejaTarifasXCA
													) throws SQLException
	{
		return SqlBaseContratoDao.busqueda(	con,nombreConvenio,numeroContrato,
																			eleccionFechaBusqueda,
																			fechaInicial,fechaFinal, 
																			codigoInstitucion, valorContrato, valorAcumulado,
																			nombreTipoPago, upc, pyp, contratoSecretaria, busquedaNivelAtencion, 
																			porcentajeUpc,base,
																			requiereAutorizacionNoCobertura, sinContrato,esquemaTarInventario,esquemaTarProcedimiento,
																			frmObservaciones, manejaTarifasXCA);												
	}

	/**
	 * M�todo que elimina un contrato seg�n su c�digo, s�lo se puede eliminar 
	 * si el contrato tiene  fechas >= al sistema 
	 */
	public  int eliminarContrato(Connection con, int codigo)
	{
		return SqlBaseContratoDao.eliminarContrato(con,codigo);
	}	
	/**
	 * Adiciones Sebasti�n
	 * M�todo que carga el c�digo del contrato de una subcuenta o cuenta
	 * @param con
	 * @param id
	 * @param opcion si es true es cuenta y si es false es subcuenta
	 * @return
	 */
	public int cargarCodigoContrato(Connection con,int id,boolean opcion){
		return SqlBaseContratoDao.cargarCodigoContrato(con,id,opcion);
	}
	/**
	 * Actualiza unicamente el valor acumulado del contrato, dado su c�digo, este valor se actualiza en la generacion
	 * de las facturas con el VrContrato
	 * @param con
	 * @param codigo
	 * @param valorAcumASumar
	 * @return
	 */
	public int actualizarValorAcumulado(		Connection con, 
																int codigo, 
																double valorAcumASumar)
	{
	    return SqlBaseContratoDao.actualizarValorAcumulado(con, codigo, valorAcumASumar);
	}
	
	/**
	 * Adici�n Sebasti�n
	 * M�todo que carga los datos del contrato a partir del id de la cuenta
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public HashMap cargarContratoPorSubCuenta(Connection con,double subCuenta)
	{
		return SqlBaseContratoDao.cargarContratoPorSubCuenta(con,subCuenta);
	}
	
	/**
	 * carga los niveles
	 * @param con
	 * @param codigoContrato
	 * @return
	 */
	public HashMap cargarNivelesContratos(Connection con, String codigoContrato)
	{
		return SqlBaseContratoDao.cargarNivelesContratos(con, codigoContrato);
	}
	
	/**
	 * obtiene el contrato de una cuenta dada
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public  int getContratoCuenta(Connection con, String idCuenta)
	{
		return SqlBaseContratoDao.getContratoCuenta(con, idCuenta);
	}

	/**
	 * metodo que valida que no exista el mismo numero de Contrato para el convenio cuando las fechas se traslapen
	 * @param con
	 * @param convenioAInsertar
	 * @param numeroContratoAInsertar
	 * @param fechaInicialAInsertar
	 * @param fechaFinalAInsertar
	 * @param codigoContratoCuandoEsModificacion
	 * @return
	 */
	public HashMap validacionNumeroContrato(Connection con, String convenioAInsertar, String numeroContratoAInsertar, String fechaInicialAInsertar, String fechaFinalAInsertar, String codigoContratoCuandoEsModificacion)
	{
		return SqlBaseContratoDao.validacionNumeroContrato(con, convenioAInsertar, numeroContratoAInsertar, fechaInicialAInsertar, fechaFinalAInsertar, codigoContratoCuandoEsModificacion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoContrato
	 * @return
	 */
	public boolean estaContratoVencido(Connection con, int codigoContrato)
	{
		return SqlBaseContratoDao.estaContratoVencido(con, codigoContrato);
	}
	
	/**
	 * metodo que indica si un conterato maneja o no cobertura
	 * @param con
	 * @param codigoContrato
	 * @return
	 */
	public boolean manejaCobertura(Connection con, int codigoContrato) throws BDException
	{
		return SqlBaseContratoDao.manejaCobertura(con, codigoContrato);
	}
	
	
	/**
	 * metodo que obiene los cpntratos vencidos dado la cuenta y sus contratos
	 * @param con
	 * @param cuenta
	 * @param contratos
	 * @return
	 */
	public HashMap obtenerContratosVencidosXCuenta (Connection con, String cuenta, Vector contratos)
	{
		return SqlBaseContratoDao.obtenerContratosVencidosXCuenta(con, cuenta, contratos);
	}
	
	/**
	 * 
	 * @param con
	 * @param contratos
	 * @return
	 */
	public HashMap obtenerContratosVencidos(Connection con, Vector contratos)
	{
		return SqlBaseContratoDao.obtenerContratosVencidos(con, contratos);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param contratos
	 * @return
	 */
	public HashMap obtenerContratosTopesCompletos (Connection con, Vector contratos, String ingreso)
	{
		return SqlBaseContratoDao.obtenerContratosTopesCompletos(con, contratos, ingreso);
	}


	/**
	 * N
	 */
	public boolean acumuladoMenorValorContrato(Connection con, int contrato) 
	{
		return SqlBaseContratoDao.acumuladoMenorValorContrato(con, contrato);
	}



	/**
	 * 
	 */
	public String obtenerNumeroContrato(Connection con, int codigoContrato) 
	{
		return SqlBaseContratoDao.obtenerNumeroContrato(con, codigoContrato);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoContrato
	 * @return
	 */
	public boolean requiereAutorizacionXNoCobertura(Connection con, int codigoContrato)
	{
		return SqlBaseContratoDao.requiereAutorizacionXNoCobertura(con, codigoContrato);
	}



	/**
	 * 
	 */
	public HashMap cargarEsquemasTarifarioInventarios(Connection con, String codigoContrato) throws BDException
	{
		return SqlBaseContratoDao.cargarEsquemasTarifarioInventarios(con, codigoContrato);
	}



	/**
	 * 
	 */
	public HashMap cargarEsquemasTarifarioProcedimientos(Connection con, String codigoContrato) throws BDException
	{
		return SqlBaseContratoDao.cargarEsquemasTarifarioProcedimientos(con, codigoContrato);
	}



	
	public HashMap consultarEsquemaInventarioLLave(Connection con, String codigoEsquema) 
	{
		return SqlBaseContratoDao.consultarEsquemaInventarioLLave(con,codigoEsquema);
	}



	public HashMap consultarEsquemaProcedimientoLLave(Connection con, String codigoEsquema) 
	{
		return SqlBaseContratoDao.consultarEsquemaProcedimientoLLave(con,codigoEsquema);
	}



	public boolean eliminarEsquema(Connection con, String codigoEsquema, boolean esInventario) 
	{
		return SqlBaseContratoDao.eliminarEsquema(con,codigoEsquema,esInventario);
	}



	public boolean insertarEsquemasInventario(Connection con, HashMap vo) 
	{
		return SqlBaseContratoDao.insertarEsquemasInventario(con,vo);
	}



	public boolean insertarEsquemasProcedimientos(Connection con, HashMap vo) 
	{
		return SqlBaseContratoDao.insertarEsquemasProcedimientos(con,vo);
	}



	public boolean modificarEsquemasInventario(Connection con, HashMap vo) 
	{
		return SqlBaseContratoDao.modificarEsquemasInventario(con,vo);
	}



	public boolean modificarEsquemasProcedimientos(Connection con, HashMap vo) 
	{
		return SqlBaseContratoDao.modificarEsquemasProcedimientos(con,vo);
	}



	/**
	 * 
	 */
	public HashMap obtenerEsquemasTarifariosInventariosVigentes(Connection con,int codigoIngreso, String fecha) 
	{
		String consulta="from esquemasvigentesinvcontratos('"+UtilidadFecha.conversionFormatoFechaABD(fecha)+"') tabla";
		return SqlBaseContratoDao.obtenerEsquemasTarifariosInventariosVigentes(con,codigoIngreso,consulta);
	}



	/**
	 * 
	 */
	public HashMap obtenerEsquemasTarifariosProcedimientosVigentes(Connection con,int codigoIngreso, String fecha) 
	{
		String consulta="from esquemasvigentesprocontratos('"+UtilidadFecha.conversionFormatoFechaABD(fecha)+"') tabla";
		return SqlBaseContratoDao.obtenerEsquemasTarifariosProcedimientosVigentes(con,codigoIngreso,consulta);
	}


	/**
	 * 
	 */
	public HashMap obtenerEsquemasTarifariosInventariosVigentesFecha(Connection con, String contrato, String fecha)
	{		
		String consulta="from esquemasvigentesinvcontratos('"+UtilidadFecha.conversionFormatoFechaABD(fecha)+"') tabla";
		return SqlBaseContratoDao.obtenerEsquemasTarifariosInventariosVigentesFecha(con,contrato,consulta);
	}



	/**
	 * 
	 */
	public HashMap obtenerEsquemasTarifariosProcedimientosVigentesFecha(Connection con, String contrato, String fecha) 
	{
		String consulta="from esquemasvigentesprocontratos('"+UtilidadFecha.conversionFormatoFechaABD(fecha)+"') tabla";
		return SqlBaseContratoDao.obtenerEsquemasTarifariosProcedimientosVigentesFecha(con,contrato,consulta);
	}

	@Override
	public boolean pacientePagaAtencion(int codigoContrato)
	{
		return SqlBaseContratoDao.pacientePagaAtencion(codigoContrato);
	}

	@Override
	public boolean pacienteValidaBonoAtenOdo(int codigoContrato)
	{
		return SqlBaseContratoDao.pacienteValidaBonoAtenOdo(codigoContrato);
	}

	@Override
	public boolean controlaAnticipos(int codigoContrato)
	{
		return SqlBaseContratoDao.controlaAnticipos(codigoContrato);
	}

}