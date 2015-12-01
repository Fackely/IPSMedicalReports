package com.princetonsa.dao.interfaz;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import com.princetonsa.dao.sqlbase.interfaz.SqlBaseInterfazSistemaUnoDao;


/**
 * 
 *@author Andrés Silva Monsalve 
 *
 */

public interface InterfazSistemaUnoDao 
{
	/**
	 * Inserta en interfaz Sistema Uno
	 * @param con
	 * @param usuario 
	 * @param institucion 
	 * @param consecutivo 
	 * @param parametros
	 * @return
	 */
	

	public Object setInterfazSistemaUno(Connection con, String tipo, String reproceso, String fechaInicial, String fechaFinal, String nombre, String path, String institucion, String usuario, int consecutivo);


	public HashMap consultarFacturas(Connection con, String fechaInicial, String fechaFinal, String contabilizado);


	public Object consultarCuentaContable(Connection con, String convenio, int tipoCuenta, String tipoRegimen, String institucion);
	
	
	/**
	 * Método implementado para consultar la cuenta parametrizable 
	 * según el tipo y los parámetros enviados por el mapa paramCuentas
	 * @param con
	 * @param paramCuentas
	 * @return
	 */
	public String consultarCuentaParametrizable(Connection con,HashMap paramCuentas);


	public Object valorCuentaContable(Connection con, int numeroSolicitud, int institucion);


	public Object centroCostoSolicitaArti(Connection con, int numeroSolicitud);


	public Object centroCostoSolicitaServ(Connection con, int numeroSolicitud);


	public Object nitConvenio(Connection con, int codigo);


	public Object tipoConvenio(Connection con, int codigo);


	public HashMap datoEmpresa(Connection con, int codigo);


	public boolean marcaFactura(Connection con, int codigo);


	public String naturalezaTrans(Connection con, String ccontable);


	public HashMap consultarRecibosCaja(Connection con, String fechaInicial, String fechaFinal, String reproceso);


	public Object centroAtencionRc(Connection con, String codigoRc, String institucion);


	public Object idPacienteRc(Connection con, String codigoRc, String institucion);


	public Object valorTotalRc(Connection con, String codigoRc, String institucion);


	public Object usuarioCajero(Connection con, String codigoRc, String institucion);


	public Object codigoCajaRc(Connection con, String codigoRc, String institucion);


	public Object tipoPagoRc(Connection con, String codigoRc, String institucion);


	public HashMap formasPagoRc(Connection con, String codigoRc, String institucion);


	public HashMap cContableyNaturalezaRc(Connection con, int formaPago);


	public String tipoConceptoRc(Connection con, String codigoRc, String institucion);


	public String conceptoIngTesoreriaRc(Connection con, String codigoRc, String institucion, String conceptoRc);


	public String cuentaConvenioRc(Connection con, String codigoRc, String institucion, String conceptoRc);


	public String cuentaRegimenRc(Connection con, String codigoRc, String institucion, String conceptoRc);


	public String cuentaPacienteConvenioRc(Connection con, String codigoRc, String institucion, String conceptoRc);


	public String cuentaPacienteRegimenRc(Connection con, String codigoRc, String institucion, String conceptoRc);


	public String conceptoPagoRc(Connection con, String codigoRc, String institucion);


	public String descripConceptoPagoRc(Connection con, String codigoRc, String institucion);


	public String interfazConvenioPacienteRc(Connection con, String codigoRc, String institucion);


	public String bancoChequeRc(Connection con, String codigoRc, String institucion);


	public boolean marcaReciboCaja(Connection con, String codigoRc);


	public int estadoRc(Connection con, String codigoRc, String institucion);


	public String naturalezaConceptoRc(Connection con, String codigoRc, String institucion);


	public String valorConceptoRc(Connection con, String codigoRc, String institucion);


	public String digitoVerificadorTercero(Connection con, int codigo);


	public int tipoTercero(Connection con, int codigo);


	public String claseClienteRc(Connection con, String codigoRc, String institucion);


	public int diasVencimientoFactura(Connection con, String convenio);


	public HashMap nombresPacienteRc(Connection con, String codigoRc);


	public String cuentaContablePaquete(Connection con, int numeroSolicitud, boolean utilidad);


	public String responsablesRC(Connection con, String responsable);


	public String centroCostoConceptoRc(Connection con, String codigoRc, String institucion);


	public String acronimoTipoConvenio(Connection con, int codigo);


	public HashMap nombresPacienteFac(Connection con, int codigoPaciente);
	
}
//