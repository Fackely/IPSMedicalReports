/*
 * Creado en Jun 13, 2006
 * Andrés Mauricio Ruiz Vélez
 * Princeton S.A (Parquesoft - Manizales)
 */
package com.princetonsa.dao.oracle.capitacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.capitacion.CuentaCobroCapitacionDao;
import com.princetonsa.dao.sqlbase.capitacion.SqlBaseCuentaCobroCapitacionDao;

public class OracleCuentaCobroCapitacionDao implements CuentaCobroCapitacionDao
{
	/**
	 * Método que consulta los cargues que cumplen con los parámetros de búsqueda
	 * ya sea por periodo o por convenio
	 * del paciente
	 * @param con
	 * @param fechaFinal
	 * @param convenio
	 * @return
	 */
	public HashMap consultarCargues(Connection con, String fechaFinal, String fechaInicial, int convenio)
	{
		return SqlBaseCuentaCobroCapitacionDao.consultarCargues (con, fechaFinal, fechaInicial, convenio);
	}
	
	/**
	 * Método que actualiza el contrato cargue con la cuenta de cobro y el valor total
	 * @param con
	 * @param codigoCargue
	 * @param consecutivoCxCCapitacion
	 * @param valorTotal
	 * @return
	 */
	public int actualizarContratoCargue(Connection con, int codigoCargue, int consecutivoCxCCapitacion, double valorTotal, int institucion)
	{
		return SqlBaseCuentaCobroCapitacionDao.actualizarContratoCargue (con, codigoCargue, consecutivoCxCCapitacion, valorTotal, institucion);
	}
	
	/**
	 * Método que actualiza el total a pagar en cargue_grupo_etareo, el valor total y
	 * cuenta de cobro en contrato_cargue
	 * @param con
	 * @param codigoCargue
	 * @param consecutivoCartera
	 * @param institucion
	 * @return valorTotalCargue
	 */
	public int actualizarCargueGrupoEtareo(Connection con, int codigoCargue, int consecutivoCartera, int institucion)
	{
		return SqlBaseCuentaCobroCapitacionDao.actualizarCargueGrupoEtareo (con, codigoCargue, consecutivoCartera, institucion);
	}
	
	/**
	 * Método que inserta la cuenta de cobro de capitación
	 * @param con
	 * @param numeroCuentaCobro
	 * @param convenioCxC
	 * @param estadoCartera
	 * @param loginUsuario
	 * @param valorInicialCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param observaciones
	 * @param codigoInstitucionInt
	 * @return
	 */
	public int insertarCuentaCobroCapitacion(Connection con, int numeroCuentaCobro, int convenioCxC, int estadoCartera, String loginUsuario, double valorInicialCuenta, String fechaInicial, String fechaFinal, String observaciones, int codigoInstitucion, String fechaElaboracion, String horaElaboracion, String centroAtencion)
	{
		return SqlBaseCuentaCobroCapitacionDao.insertarCuentaCobroCapitacion (con, numeroCuentaCobro, convenioCxC, estadoCartera, loginUsuario, valorInicialCuenta, fechaInicial, fechaFinal, observaciones, codigoInstitucion, fechaElaboracion, horaElaboracion, centroAtencion);
	}
	
	/**
	 * Método que consulta las cuentas de cobro por convenio, de acuerdo a la fecha 
	 * inicial y final
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap consultarCuentasCobroConvenio(Connection con, String fechaInicial, String fechaFinal, int codigoInstitucion)
	{
		return SqlBaseCuentaCobroCapitacionDao.consultarCuentasCobroConvenio (con, fechaInicial, fechaFinal, codigoInstitucion);
	}
	
	/**
	 * Método que consulta los contratos_cargue asociados a la cuenta de cobro
	 * @param con
	 * @param numeroCuentaCobro
	 * @param institucion
	 * @return
	 */
	public HashMap consultarDetalleCuentaCobro(Connection con, int numeroCuentaCobro, int institucion)
	{
		return SqlBaseCuentaCobroCapitacionDao.consultarDetalleCuentaCobro (con, numeroCuentaCobro, institucion);
	}
	
	/**
	 * Método que consulta la información de la cuenta de cobro de acuerdo
	 * al numero de la cuenta de cobro y a la institución
	 * @param con
	 * @param numeroCuentaCobro
	 * @param codigoInstitucion
	 * @param estadoCartera
	 * @return
	 */
	public HashMap consultarCuentaCobro(Connection con, int numeroCuentaCobro, int codigoInstitucion, int estadoCartera)
	{
		return SqlBaseCuentaCobroCapitacionDao.consultarCuentaCobro (con, numeroCuentaCobro, codigoInstitucion, estadoCartera);
	}
	
	/**
	 * Método que realiza la busqueda avanzada de las cuentas de cobro de acuerdo
	 * a los parámetros de búsqueda
	 * @param con
	 * @param cuentaCobro
	 * @param cuentaCobroFinal
	 * @param convenio
	 * @param fechaElaboracion
	 * @param estadoCuentaCobro
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap consultaAvanzadaCuentaCobro(Connection con, String cuentaCobro, String cuentaCobroFinal,int convenio, String fechaElaboracion, int estadoCuentaCobro, String fechaInicial, String fechaFinal, int codigoInstitucion)
	{
		return SqlBaseCuentaCobroCapitacionDao.consultaAvanzadaCuentaCobro (con, cuentaCobro, cuentaCobroFinal, convenio, fechaElaboracion, estadoCuentaCobro, fechaInicial, fechaFinal, codigoInstitucion);
	}
	
	/**
	 * Método que realiza la anulación de la cuenta de cobro, modificando su estado y
	 * liberando los cargues asociados a ella (se ponen en null)
	 * @param con
	 * @param cuentaCobro
	 * @param motivoAnulacion
	 * @param loginUsuario
	 * @param codigoInstitucion
	 * @return
	 */
	public int anularCuentaCobro(Connection con, String cuentaCobro, String motivoAnulacion, String loginUsuario, int codigoInstitucion)
	{
		return SqlBaseCuentaCobroCapitacionDao.anularCuentaCobro (con, cuentaCobro, motivoAnulacion, loginUsuario, codigoInstitucion);
	}
	
	/**
	 * Método que consulta los cargues que cumplen con los paràmetros en modificar
	 * @param con
	 * @param cuentaCobro
	 * @param fechaFinal
	 * @param codigoConvenio
	 * @return
	 */
	public HashMap consultarCarguesModificar(Connection con, int cuentaCobro, String fechaFinal, int codigoConvenio)
	{
		return SqlBaseCuentaCobroCapitacionDao.consultarCarguesModificar (con, cuentaCobro, fechaFinal, codigoConvenio);
	}
	
	/**
	 * Método que libera los cargues asociados a una cuenta de cobro de acuerdo
	 * a la fecha final 
	 * @param con
	 * @param cuentaCobro
	 * @param institucion
	 * @param fechaFinal
	 * @return
	 */
	public int liberarCarguesCuentaCobro(Connection con, int cuentaCobro, int institucion, String fechaFinal)
	{
		return SqlBaseCuentaCobroCapitacionDao.liberarCarguesCuentaCobro (con, cuentaCobro, institucion, fechaFinal);
	}
	
	/**
	 * Método que actualiza una cuenta de cobro
	 * @param con
	 * @param cuentaCobro
	 * @param loginUsuario
	 * @param valorInicialCuenta
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param observaciones
	 * @param codigoInstitucion
	 * @return
	 */
	public int actualizarCuentaCobroCapitacion(Connection con, int cuentaCobro, String loginUsuario, double valorInicialCuenta, String fechaInicial, String fechaFinal, String observaciones, int codigoInstitucion)
	{
		return SqlBaseCuentaCobroCapitacionDao.actualizarCuentaCobroCapitacion (con, cuentaCobro, loginUsuario, valorInicialCuenta, fechaInicial, fechaFinal, observaciones, codigoInstitucion);
	}
	
	/**
	 * Método que consulta el listado de facturas asociadas a la 
	 * cuenta de cobro
	 * @param con
	 * @param cuentaCobro
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap consultarFacturasCxC(Connection con, int cuentaCobro, int codigoInstitucion)
	{
		return SqlBaseCuentaCobroCapitacionDao.consultarFacturasCxC (con, cuentaCobro, codigoInstitucion);
	}
	
	/**
	 * Mètodo que realiza la búsqueda avanzada de las facturas
	 * @param con
	 * @param numeroFactura
	 * @param fechaFactura
	 * @param viaIngreso
	 * @param codigoInstitucion
	 * @param cuentaCobroCapitacion
	 * @return
	 */
	public HashMap consultaAvanzadaFacturasCxC(Connection con, String numeroFactura, String fechaFactura, int viaIngreso, int codigoInstitucion, String cuentaCobroCapitacion)
	{
		return SqlBaseCuentaCobroCapitacionDao.consultaAvanzadaFacturasCxC (con, numeroFactura, fechaFactura, viaIngreso, codigoInstitucion, cuentaCobroCapitacion);
	}
	
	/**
	 * metodo que actualiza los campos de total_ingresos y diferencia_cuenta de las cuentas de cobro capitadas
	 * @param con
	 * @param numeroCuentaCobro
	 * @param totalIngresos
	 * @return
	 */
	public boolean updateTotalIngresosDiferenciaCuenta(Connection con, String numeroCuentaCobro, String totalIngresos)
	{
		return SqlBaseCuentaCobroCapitacionDao.updateTotalIngresosDiferenciaCuenta(con, numeroCuentaCobro, totalIngresos);
	}
	
	/**
	 * Método que consulta los contratos_cargue asociados al listado
	 * de cuentas de cobro enviado por parámetro, separados por coma
	 * se utiliza en la impresión
	 * @param con
	 * @param listadoCuentasCobro
	 * @return
	 */
	public HashMap consultarDetalleCuentasCobroImpresion(Connection con, String listadoCuentasCobro)
	{
		return SqlBaseCuentaCobroCapitacionDao.consultarDetalleCuentasCobroImpresion(con, listadoCuentasCobro);
	}
	
	/**
	 * indica si existe o no un numero de cuenta de cobro dado x institucion
	 * @param con
	 * @param numeroCxC
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean existeNumeroCxC (Connection con, String numeroCxC, int codigoInstitucion)
	{
		return SqlBaseCuentaCobroCapitacionDao.existeNumeroCxC(con, numeroCxC, codigoInstitucion);
	}
	
	/**
	 * Método que consulta el detalle de los cargues de tipo grupo etáreo para mostrarse en la impresión
	 * @param con
	 * @param listadoContratosCargue
	 * @return
	 */
	public HashMap consultarDetalleGrupoEtareo(Connection con, String listadoContratosCargue)
	{
		return SqlBaseCuentaCobroCapitacionDao.consultarDetalleGrupoEtareo(con, listadoContratosCargue);
	}

	/**
	 * 
	 */
	public HashMap consultarCuentasCobroConvenio(Connection con, int institucion, String cuentasCobro)
	{
		return SqlBaseCuentaCobroCapitacionDao.consultarCuentasCobroConvenio(con,institucion,cuentasCobro);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoCargue
	 * @param codigoInstitucionInt
	 * @param totalUsuarios
	 * @param upc
	 * @param totalUsuariosOld
	 * @param updOld
	 * @param usuario
	 * @return
	 */
	public int actualizarUpcTotalUserContratoCargue(Connection con, int codigoCargue, int codigoInstitucionInt, String totalUsuarios, String upc, String totalUsuariosOld, String upcOld, String usuario)
	{
		return SqlBaseCuentaCobroCapitacionDao.actualizarUpcTotalUserContratoCargue(con,codigoCargue,codigoInstitucionInt,totalUsuarios,upc,totalUsuariosOld,upcOld,usuario);
	}

	/**
	 * 
	 */
	public HashMap consultarNumeroCargue(Connection con, String fechaFinal, String fechaInicial, int convenioCapitado) 
	{
		return SqlBaseCuentaCobroCapitacionDao.consultarNumeroCargue(con, fechaFinal, fechaInicial, convenioCapitado);
	}
	

}
