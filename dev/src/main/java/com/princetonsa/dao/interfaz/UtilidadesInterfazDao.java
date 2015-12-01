/*
 * Jul 21, 2009
 */
package com.princetonsa.dao.interfaz;

import java.sql.Connection;
import java.util.ArrayList;

import util.ResultadoBoolean;

import com.princetonsa.dto.administracion.DtoConceptosRetencion;
import com.princetonsa.dto.administracion.DtoTiposRetencion;
import com.princetonsa.dto.facturacion.DtoConceptoRetencionTercero;
import com.princetonsa.dto.interfaz.DtoCuentaContable;
import com.princetonsa.dto.interfaz.DtoRetencion;

/**
 * 
 * Interfaz para el manejo de los métodos genéricos
 * dentro del módulode interfaz de Axioma
 * @author Sebastián Gómez R.
 *
 */
public interface UtilidadesInterfazDao 
{
	/**
	 * Método para consultar los datos de la cuenta contable de la interfaz convenio
	 * @param con
	 * @param codigoConvenio
	 * @param tipoRegimen
	 * @param tipoCuenta
	 * @return
	 */
	public DtoCuentaContable consultarCuentaContableInterfazConvenio(Connection con,int codigoConvenio,String tipoRegimen,int tipoCuenta,boolean cuentaContable,boolean cuentaCxCapitacion);
	
	/**
	 * Método para obtener la informacion de la cuenta contable de cuenta interfaz unidad funcional
	 * @param con
	 * @param codigoCentroCosto
	 * @param cuentaIngreso
	 * @param cuentaMedicamento
	 * @param cuentaIngresoVigAnterior
	 * @param cuentaMedVigAnterior
	 * @param cuentaContableCosto
	 * @return
	 */
	public DtoCuentaContable consultarCuentaContableInterfazUnidadFuncional(Connection con,int codigoCentroCosto,boolean cuentaIngreso,boolean cuentaMedicamento, boolean cuentaIngresoVigAnterior,boolean cuentaMedVigAnterior,boolean cuentaContableCosto);
	
	/**
	 * Método para consultar la cuenta contabl de la interfaz de servicio
	 * @param con
	 * @param codigoServicio
	 * @param codigoCentroCosto
	 * @param cuentaIngreso
	 * @param cuentaIngresoVigAnterior
	 * @param cuentaContableCosto
	 * @return
	 */
	public DtoCuentaContable consultarCuentaContableInterfazServicio(Connection con,int codigoServicio,int codigoCentroCosto,boolean cuentaIngreso,boolean cuentaIngresoVigAnterior,boolean cuentaContableCosto);
	
	/**
	 * Método para consultar la cuenta contable de la ionterfaz inventarios
	 * @param con
	 * @param codigoArticulo
	 * @param codigoCentroCosto
	 * @return
	 */
	public DtoCuentaContable consultarCuentaContableInterfazInventarios(Connection con,int codigoArticulo,int codigoCentroCosto);
	
	/**
	 * Método que consulta la cuenta contable de la parametriacion de subgrupo/grupo/clase
	 * @param con
	 * @param codigoArticulo
	 * @param cuentaCosto
	 * @param cuentaInventario
	 * @return
	 */
	public DtoCuentaContable consultarCuentaContableSubgrupoGrupoClase(Connection con,int codigoArticulo,boolean cuentaCosto,boolean cuentaInventario);
	
	/**
	 * Método para consultar los datos de la cuenta contable
	 * @param con
	 * @param cuentaContable
	 * @return
	 */
	public DtoCuentaContable consultarDatosCuentaContable(Connection con,DtoCuentaContable cuentaContable);
	
	/**
	 * Método para consultar la cuenta contable de cuenta interfaz inventario ingreso
	 * @param con
	 * @param codigoCentroCosto
	 * @param codigoArticulo
	 * @param cuentaIngreso
	 * @param cuentaIngresoVigAnterior
	 * @return
	 */
	public DtoCuentaContable consultarCuentaContableInterfazInvIngreso(Connection con,int codigoCentroCosto,int codigoArticulo,boolean cuentaIngreso,boolean cuentaIngresoVigAnterior);
	
	/**
	 * Método implementado para verificar si una factura tiene un mismo grupo o diferentes
	 * @param con
	 * @param numeroDocumento
	 * @return
	 */
	public ResultadoBoolean validacionMismoGrupoServicioFactura(Connection con,String numeroDocumento,boolean honorarios);
	
	/**
	 * Método para obtener el mismo grupo de servicio de la autorzacion de servicios
	 * @param con
	 * @param numeroDocumento
	 * @return
	 */
	public ResultadoBoolean validacionMismoGrupoServicioAutorizacionEntidadSub(Connection con,String numeroDocumento);
	
	/**
	 * Método para validar si los medicamentos o despachos pertenecen a la misma/diferente clase
	 * @param con
	 * @param numeroDocumento
	 * @param codigoTipoDocumento
	 * @return
	 */
	public ResultadoBoolean validacionMismaClaseDespachoMedicamentos(Connection con,String numeroDocumento,int codigoTipoDocumento);
	
	/**
	 * Método para validar misma clase de la factura
	 * @param con
	 * @param numeroDocumento
	 * @return
	 */
	public ResultadoBoolean validacionMismaClaseFactura(Connection con,String numeroDocumento);
	/**
	 * Método implementado para consultar los tipos de retencion que aplican por clase, grupo y conceptode factura varia
	 * @param con
	 * @param codigoInstitucion
	 * @param codigoClase
	 * @param codigoGrupo
	 * @param codigoConcepto
	 * @return
	 */
	public ArrayList<DtoTiposRetencion> consultarTiposRetencionXClaseGrupoConceptoFV(Connection con,int codigoInstitucion,int codigoClase,int codigoGrupo,int codigoConcepto);
	
	/**
	 * Método que consulta los conceptos que aplican a los conceptos de retencion del tercero
	 * @param con
	 * @param tiposRetencion
	 * @param conceptos
	 * @param nitTercero
	 * @param integral
	 * @param fechaDocumento 
	 * @return
	 */
	public ArrayList<DtoConceptoRetencionTercero> consultarConceptosRetencionXTercero(Connection con,ArrayList<DtoTiposRetencion> tiposRetencion,ArrayList<DtoConceptosRetencion> conceptos,String nitTercero,boolean integral, String fechaDocumento);
	
	/**
	 * Método para conultar las conceptos de retencion de la parametrizacion de interfaz sistema 1E
	 * @param con
	 * @param claseDocumento
	 * @param seccion
	 * @param codigoInstitucion
	 * @return
	 */
	public ArrayList<DtoConceptosRetencion> consultarConceptosRetencionParamS1E(Connection con,String claseDocumento,String seccion,int codigoInstitucion);
	
	/**
	 * Método para calcular el valor de la retencion de cada concepto
	 * @param con
	 * @param conceptos
	 * @param integral
	 * @param codigoGrupo
	 * @param codigoClase
	 * @param codigoConcepto
	 * @param fecha
	 * @param valorDocumento
	 * @return
	 */
	public DtoRetencion consultarTarifaRetencion(Connection con,ArrayList<DtoConceptoRetencionTercero> conceptos,boolean integral,int codigoGrupo,int codigoClase,int codigoConcepto,String fecha,double valorDocumento);
	
	/**
	 * Método que verifica si una factura aplica para el calculo de la autoretencion
	 * @param con
	 * @param numeroDocumento
	 * @return
	 */
	public ResultadoBoolean validacionFacturaPacienteAutoretencion(Connection con,String numeroDocumento);
	
	/**
	 * Método que consulta el número de servicios y articulos que tiene una factura
	 * @param con
	 * @param numeroDocumento
	 * @return
	 */
	public int[] consultarNumServiciosYArticulosFactura(Connection con,String numeroDocumento);
	
	/**
	 * Método para validar si la factura tiene iguales conceptos o distintos
	 * @param con
	 * @param numeroDocumento
	 * @return
	 */
	public ResultadoBoolean validacionMismoConceptoFacturaVaria(Connection con,String numeroDocumento);
}
