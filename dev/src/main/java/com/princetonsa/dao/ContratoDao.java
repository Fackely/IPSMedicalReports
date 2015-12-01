/*
 * @(#)ContratoDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import com.princetonsa.decorator.ResultSetDecorator;
import com.servinte.axioma.fwk.exception.BDException;

/**
 *  Interfaz para el acceder a la fuente de datos de un contrato
 *
 * @version 1.0, Abril 30 / 2004
 * @author wrios
 */

public interface ContratoDao
{
	/**
	 * inserta un contrato 
	 * @param sinContrato 
	 * @param validarAbonoAtencionOdo 
	 * @param pacientePagaAtencion 
	 * @param con, Connection, conexion abierta con una fuente de datos
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
										String requiereAutorizacionNoCobertura, String sinContrato,
										String frmObservaciones, 
										String pacientePagaAtencion, 
										String validarAbonoAtencionOdo,
										boolean manejaTarifasXCA
										);
	
	/** 
	 * M�todo que indica si el contrato puede 
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
																		);
	
	
	
	/**
	 * M�todo que  carga  los datos de un contrato para mostrarlos en el resumen
	 * en una BD PostgresSQL o Hsqldb 
	 */
	public ResultSetDecorator cargar(Connection con, String codigoContrato) throws SQLException;

	/**
	 * Modifica un contrato dado su c�digo con los param�tros dados.
	 * @param validarAbonoAtencionOdo 
	 * @param pacientePagaAtencion 
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
											boolean manejaTarifasXCA); 

	/**
	 * M�todo que contiene el Resulset de los datos de la tabla contratos
	 * para posteriorm/t ser mostrados en el pager
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @return Resultset con todos los datos de la tabla contratos
	 * @throws SQLException
	 */
	public  ResultSetDecorator listado(Connection con, int codigoInstitucion) throws SQLException;
	
	/**
	 * M�todo que elimina un contrato seg�n su c�digo, s�lo se puede eliminar 
	 * si el contrato tiene  fechas >= al sistema 
	 */
	public  int eliminarContrato(Connection con, int codigo);
		
	/**
	 * M�todo que contiene el Resulset de todas las empresas buscadas
	 * @param esquemaTarProcedimiento 
	 * @param esquemaTarInventario 
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
													) throws SQLException;
	
	/**
	 * Adiciones Sebasti�n
	 * M�todo que carga el c�digo del contrato de una subcuenta o cuenta
	 * @param con
	 * @param id
	 * @param opcion si es true es cuenta y si es false es subcuenta
	 * @return
	 */
	public int cargarCodigoContrato(Connection con,int id,boolean opcion);
	
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
																double valorAcumASumar);
	
	/**
	 * Adici�n Sebasti�n
	 * M�todo que carga los datos del contrato a partir del id de la cuenta
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public HashMap cargarContratoPorSubCuenta(Connection con,double subCuenta);
	
	/**
	 * carga los niveles
	 * @param con
	 * @param codigoContrato
	 * @return
	 */
	public HashMap cargarNivelesContratos(Connection con, String codigoContrato);
	
	/**
	 * obtiene el contrato de una cuenta dada
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public int getContratoCuenta(Connection con, String idCuenta);
	
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
	public HashMap validacionNumeroContrato(Connection con, String convenioAInsertar, String numeroContratoAInsertar, String fechaInicialAInsertar, String fechaFinalAInsertar, String codigoContratoCuandoEsModificacion);
	
	/**
	 * 
	 * @param con
	 * @param codigoContrato
	 * @return
	 */
	public boolean estaContratoVencido(Connection con, int codigoContrato);
	
	/**
	 * metodo que indica si un conterato maneja o no cobertura
	 * @param con
	 * @param codigoContrato
	 * @return
	 */
	public boolean manejaCobertura(Connection con, int codigoContrato) throws BDException;
	
	
	/**
	 * metodo que obiene los cpntratos vencidos dado la cuenta y sus contratos
	 * @param con
	 * @param cuenta
	 * @param contratos
	 * @return
	 */
	public HashMap obtenerContratosVencidosXCuenta (Connection con, String cuenta, Vector contratos);

	/**
	 * 
	 * @param con
	 * @param contratos
	 * @return
	 */
	public HashMap obtenerContratosTopesCompletos (Connection con, Vector contratos, String cuenta);
	
	
	/**
	 * 
	 * @param con
	 * @param contrato
	 * @return
	 */
	public boolean acumuladoMenorValorContrato(Connection con, int contrato);
	
	
	/**
	 * 
	 * @param con
	 * @param contratos
	 * @return
	 */
	public HashMap obtenerContratosVencidos(Connection con, Vector contratos);

	/**
	 * 
	 * @param con
	 * @param codigoContrato
	 * @return
	 */
	public String obtenerNumeroContrato(Connection con, int codigoContrato);
	
	/**
	 * 
	 * @param con
	 * @param codigoContrato
	 * @return
	 */
	public boolean requiereAutorizacionXNoCobertura(Connection con, int codigoContrato);

	/**
	 * 
	 * @param con
	 * @param codigoContratoStr
	 * @return
	 */
	public HashMap cargarEsquemasTarifarioInventarios(Connection con, String codigoContratoStr) throws BDException;

	/**
	 * 
	 * @param con
	 * @param codigoContratoStr
	 * @return
	 */
	public HashMap cargarEsquemasTarifarioProcedimientos(Connection con, String codigoContratoStr) throws BDException;

	/**
	 * 
	 * @param con
	 * @param codigoEsquema
	 * @param esInventario
	 * @return
	 */
	public boolean eliminarEsquema(Connection con, String codigoEsquema, boolean esInventario);

	/**
	 * 
	 * @param con
	 * @param codigoEsquema
	 * @return
	 */
	public HashMap consultarEsquemaInventarioLLave(Connection con, String codigoEsquema);

	/**
	 * 
	 * @param con
	 * @param codigoEsquema
	 * @return
	 */
	public HashMap consultarEsquemaProcedimientoLLave(Connection con, String codigoEsquema);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarEsquemasInventario(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarEsquemasInventario(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean modificarEsquemasProcedimientos(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean insertarEsquemasProcedimientos(Connection con, HashMap vo);

	/**
	 * 
	 * @param con
	 * @param codigoIngreso 
	 * @param fecha
	 * @return
	 */
	public HashMap obtenerEsquemasTarifariosInventariosVigentes(Connection con, int codigoIngreso, String fecha);

	/**
	 * 
	 * @param con
	 * @param codigoIngreso 
	 * @param fecha
	 * @return
	 */
	public HashMap obtenerEsquemasTarifariosProcedimientosVigentes(Connection con, int codigoIngreso, String fecha);
	
	
	/**
	 * 
	 * @param con
	 * @param contrato
	 * @param fecha
	 * @return
	 */
	public HashMap obtenerEsquemasTarifariosInventariosVigentesFecha(Connection con, String contrato, String fecha);

	/**
	 * 
	 * @param con
	 * @param contrato
	 * @param fecha
	 * @return
	 */
	public HashMap obtenerEsquemasTarifariosProcedimientosVigentesFecha(Connection con, String contrato, String fecha);
	
	
	/**
	 * 	
	 * @param codigoContrato
	 * @param codigoConvenio
	 * @return
	 */
	public boolean pacientePagaAtencion( int codigoContrato);
	
		
	/**
	 * 
	 * @param codigoContrato
	 * @param codigoConvenio
	 * @return
	 */
	public boolean pacienteValidaBonoAtenOdo( int codigoContrato);	
	
	/**
	 * 	
	 * @param codigoContrato
	 * @param codigoConvenio
	 * @return
	 */
	public boolean controlaAnticipos( int codigoContrato);

}