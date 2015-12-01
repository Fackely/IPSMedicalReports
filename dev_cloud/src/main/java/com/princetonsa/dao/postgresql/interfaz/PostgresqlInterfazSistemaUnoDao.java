package com.princetonsa.dao.postgresql.interfaz;

import com.princetonsa.dao.interfaz.InterfazSistemaUnoDao;
import com.princetonsa.dao.sqlbase.interfaz.SqlBaseGeneracionInterfazDao;
import com.princetonsa.dao.sqlbase.interfaz.SqlBaseInterfazSistemaUnoDao;
import java.sql.Connection;
import java.util.HashMap;


/**
* @author Andrés Silva Monsalve
**/


public class PostgresqlInterfazSistemaUnoDao implements InterfazSistemaUnoDao 
{
	/**
	 * Inserta en interfaz Sistema Uno
	 * @param con
	 * @param parametros
	 * @return
	 */
		
	public Object setInterfazSistemaUno(Connection con, String tipo, String reproceso, String fechaInicial, String fechaFinal, String nombre, String path, String institucion, String usuario, int consecutivo)
	{
		return SqlBaseInterfazSistemaUnoDao.setInterfazSistemaUno(con, tipo, reproceso, fechaInicial, fechaFinal, nombre, path, institucion, usuario, consecutivo);
	}


	public HashMap consultarFacturas(Connection con, String fechaInicial, String fechaFinal, String contabilizado) 
	{
		
		return SqlBaseInterfazSistemaUnoDao.consultarFacturas(con, fechaInicial, fechaFinal, contabilizado);
	}


	public Object consultarCuentaContable(Connection con, String convenio, int tipoCuenta, String tipoRegimen, String institucion) {
		
		return SqlBaseInterfazSistemaUnoDao.consultarCuentaContable(con, convenio, tipoCuenta, tipoRegimen, institucion);
	}
	
	/**
	 * Método implementado para consultar la cuenta parametrizable 
	 * según el tipo y los parámetros enviados por el mapa paramCuentas
	 * @param con
	 * @param paramCuentas
	 * @return
	 */
	public String consultarCuentaParametrizable(Connection con,HashMap paramCuentas)
	{
			
		return SqlBaseInterfazSistemaUnoDao.consultarCuentaParametrizable(con,paramCuentas);
	}


	/**
	 * Metodo para Retornar el Valor de la Utilidad o Perdida cuando esta es diferente a cero
	 * @param con
	 * @param numeroSolicitud
	 * @param institucion
	 * @return
	 */
	public Object valorCuentaContable(Connection con, int numeroSolicitud, int institucion) {
		
		return SqlBaseInterfazSistemaUnoDao.valorCuentaContable(con, numeroSolicitud, institucion);
	}


	public Object centroCostoSolicitaArti(Connection con, int numeroSolicitud) {
		
		return SqlBaseInterfazSistemaUnoDao.centroCostoSolicitaArti(con, numeroSolicitud);
	}


	public Object centroCostoSolicitaServ(Connection con, int numeroSolicitud) {
		
		return SqlBaseInterfazSistemaUnoDao.centroCostoSolicitaServ(con, numeroSolicitud);
	}


	public Object nitConvenio(Connection con, int codigo) {
		
		return SqlBaseInterfazSistemaUnoDao.nitConvenio(con, codigo);
	}


	public Object tipoConvenio(Connection con, int codigo) {
		
		return SqlBaseInterfazSistemaUnoDao.tipoConvenio(con, codigo);
	}


	public HashMap datoEmpresa(Connection con, int codigo) {
		
		return SqlBaseInterfazSistemaUnoDao.datoEmpresa(con, codigo);
	}


	public boolean marcaFactura(Connection con, int codigo) {
		
		return SqlBaseInterfazSistemaUnoDao.marcaFactura(con, codigo);
	}


	public String naturalezaTrans(Connection con, String ccontable) {
		
		return SqlBaseInterfazSistemaUnoDao.naturalezaTrans(con, ccontable);
	}


	public HashMap consultarRecibosCaja(Connection con, String fechaInicial, String fechaFinal, String reproceso) 
	{
		
		return SqlBaseInterfazSistemaUnoDao.consultarRecibosCaja(con, fechaInicial, fechaFinal, reproceso);
	}


	public Object centroAtencionRc(Connection con, String codigoRc, String institucion) {
		
		return SqlBaseInterfazSistemaUnoDao.centroAtencionRc(con, codigoRc, institucion);
	}


	public Object idPacienteRc(Connection con, String codigoRc, String institucion) {
		
		return SqlBaseInterfazSistemaUnoDao.idPacienteRc(con, codigoRc, institucion);
	}


	public Object valorTotalRc(Connection con, String codigoRc, String institucion) {
		
		return SqlBaseInterfazSistemaUnoDao.valorTotalRc(con, codigoRc, institucion);
	}


	public Object usuarioCajero(Connection con, String codigoRc, String institucion) {
		
		return SqlBaseInterfazSistemaUnoDao.usuarioCajero(con, codigoRc, institucion);
	}


	public Object codigoCajaRc(Connection con, String codigoRc, String institucion) {
		
		return SqlBaseInterfazSistemaUnoDao.codigoCajaRc(con, codigoRc, institucion);
	}


	public Object tipoPagoRc(Connection con, String codigoRc, String institucion) {
		
		return SqlBaseInterfazSistemaUnoDao.tipoPagoRc(con, codigoRc, institucion);
	}


	public HashMap formasPagoRc(Connection con, String codigoRc, String institucion) {
		
		return SqlBaseInterfazSistemaUnoDao.formasPagoRc(con, codigoRc, institucion);
	}


	public HashMap cContableyNaturalezaRc(Connection con, int formaPago) {
		
		return SqlBaseInterfazSistemaUnoDao.cContableyNaturaleza(con, formaPago);
	}


	public String tipoConceptoRc(Connection con, String codigoRc, String institucion) {
		
		return SqlBaseInterfazSistemaUnoDao.tipoConceptoRc(con, codigoRc, institucion);
	}


	public String conceptoIngTesoreriaRc(Connection con, String codigoRc, String institucion, String conceptoRc) {
		
		return SqlBaseInterfazSistemaUnoDao.conceptoIngTesoreriaRc(con, codigoRc, institucion, conceptoRc);
	}


	public String cuentaConvenioRc(Connection con, String codigoRc, String institucion, String conceptoRc) {
		
		return SqlBaseInterfazSistemaUnoDao.cuentaConvenioRc(con, codigoRc, institucion, conceptoRc);
	}


	public String cuentaRegimenRc(Connection con, String codigoRc, String institucion, String conceptoRc) {
		
		return SqlBaseInterfazSistemaUnoDao.cuentaRegimenRc(con, codigoRc, institucion, conceptoRc);
	}


	public String cuentaPacienteConvenioRc(Connection con, String codigoRc, String institucion, String conceptoRc) {
		
		return SqlBaseInterfazSistemaUnoDao.cuentaPacienteConvenioRc(con, codigoRc, institucion, conceptoRc);
	}


	public String cuentaPacienteRegimenRc(Connection con, String codigoRc, String institucion, String conceptoRc) {
		
		return SqlBaseInterfazSistemaUnoDao.cuentaPacienteRegimenRc(con, codigoRc, institucion, conceptoRc);
	}


	public String conceptoPagoRc(Connection con, String codigoRc, String institucion) {
		
		return SqlBaseInterfazSistemaUnoDao.conceptoPagoRc(con, codigoRc, institucion);
	}


	public String descripConceptoPagoRc(Connection con, String codigoRc, String institucion) {
		
		return SqlBaseInterfazSistemaUnoDao.descripConceptoPagoRc(con, codigoRc, institucion);
	}


	public String interfazConvenioPacienteRc(Connection con, String codigoRc, String institucion) {
		
		return SqlBaseInterfazSistemaUnoDao.interfazConvenioPacienteRc(con, codigoRc, institucion);
	}


	public String bancoChequeRc(Connection con, String codigoRc, String institucion) {
		
		return SqlBaseInterfazSistemaUnoDao.bancoChequeRc(con, codigoRc, institucion);
	}

	
	public boolean marcaReciboCaja(Connection con, String codigoRc) {
		
		return SqlBaseInterfazSistemaUnoDao.marcaReciboCaja(con, codigoRc);
	}


	public int estadoRc(Connection con, String codigoRc, String institucion) {
		
		return SqlBaseInterfazSistemaUnoDao.estadoRc(con, codigoRc, institucion);
	}
	
	
	public String naturalezaConceptoRc(Connection con, String codigoRc, String institucion) {
		
		return SqlBaseInterfazSistemaUnoDao.naturalezaConceptoRc(con, codigoRc, institucion);
	}
	
	
	public String valorConceptoRc(Connection con, String codigoRc, String institucion) {
		
		return SqlBaseInterfazSistemaUnoDao.valorConceptoRc(con, codigoRc, institucion);
	}


	public String digitoVerificadorTercero(Connection con, int codigo) {
		
		return SqlBaseInterfazSistemaUnoDao.digitoVerificadorTercero(con, codigo);
	}


	public int tipoTercero(Connection con, int codigo) {
		
		return SqlBaseInterfazSistemaUnoDao.tipoTercero(con, codigo);
	}
	
	public String claseClienteRc(Connection con, String codigoRc, String institucion) 
	{
		
		return SqlBaseInterfazSistemaUnoDao.claseClienteRc(con, codigoRc, institucion);
	}
	
	public int diasVencimientoFactura(Connection con, String convenio) 
	{
		
		return SqlBaseInterfazSistemaUnoDao.diasVencimientoFactura(con, convenio);
	}
	
	public HashMap nombresPacienteRc(Connection con, String codigoRc) {
		
		return SqlBaseInterfazSistemaUnoDao.nombresPacienteRc(con, codigoRc);
	}


	public String cuentaContablePaquete(Connection con, int numeroSolicitud, boolean utilidad) {
		
		return SqlBaseInterfazSistemaUnoDao.cuentaContablePaquete(con, numeroSolicitud, utilidad);
	}
	
	public String responsablesRC(Connection con, String responsable) {
		
		return SqlBaseInterfazSistemaUnoDao.responsablesRC(con, responsable);
	}

	public String centroCostoConceptoRc(Connection con, String codigoRc, String institucion) {
		
		return SqlBaseInterfazSistemaUnoDao.centroCostoConceptoRc(con, codigoRc, institucion);
	}

	public String acronimoTipoConvenio(Connection con, int codigo) {
		
		return SqlBaseInterfazSistemaUnoDao.acronimoTipoConvenio(con, codigo);
	}

	public HashMap nombresPacienteFac(Connection con, int codigoPaciente) {
		
		return SqlBaseInterfazSistemaUnoDao.nombresPacienteFac(con, codigoPaciente);
	}
}



