/*
 * Jul 21, 2009
 */
package com.princetonsa.dao.postgresql.interfaz;

import java.sql.Connection;
import java.util.ArrayList;

import util.ResultadoBoolean;

import com.princetonsa.dao.interfaz.UtilidadesInterfazDao;
import com.princetonsa.dao.sqlbase.interfaz.SqlBaseUtilidadesInterfazDao;
import com.princetonsa.dto.administracion.DtoConceptosRetencion;
import com.princetonsa.dto.administracion.DtoTiposRetencion;
import com.princetonsa.dto.facturacion.DtoConceptoRetencionTercero;
import com.princetonsa.dto.interfaz.DtoCuentaContable;
import com.princetonsa.dto.interfaz.DtoRetencion;

/**
 * Clase que maneja los m�todos  de postgres para el acceso
 * a la fuente de datos de los procesos gen�ricos del 
 * m�dulo de interfaz de Axioma
 * @author Sebasti�n G�mez R.
 *
 */
public class PostgresqlUtilidadesInterfazDao implements UtilidadesInterfazDao 
{
	/**
	 * M�todo para consultar los datos de la cuenta contable de la interfaz convenio
	 * @param con
	 * @param codigoConvenio
	 * @param tipoRegimen
	 * @param tipoCuenta
	 * @return
	 */
	public DtoCuentaContable consultarCuentaContableInterfazConvenio(Connection con,int codigoConvenio,String tipoRegimen,int tipoCuenta,boolean cuentaContable,boolean cuentaCxCapitacion)
	{
		return SqlBaseUtilidadesInterfazDao.consultarCuentaContableInterfazConvenio(con, codigoConvenio, tipoRegimen, tipoCuenta, cuentaContable,cuentaCxCapitacion);
	}
	
	/**
	 * M�todo para obtener la informacion de la cuenta contable de cuenta interfaz unidad funcional
	 * @param con
	 * @param codigoCentroCosto
	 * @param cuentaIngreso
	 * @param cuentaMedicamento
	 * @param cuentaIngresoVigAnterior
	 * @param cuentaMedVigAnterior
	 * @param cuentaContableCosto
	 * @return
	 */
	public DtoCuentaContable consultarCuentaContableInterfazUnidadFuncional(Connection con,int codigoCentroCosto,boolean cuentaIngreso,boolean cuentaMedicamento, boolean cuentaIngresoVigAnterior,boolean cuentaMedVigAnterior,boolean cuentaContableCosto)
	{
		return SqlBaseUtilidadesInterfazDao.consultarCuentaContableInterfazUnidadFuncional(con, codigoCentroCosto, cuentaIngreso, cuentaMedicamento, cuentaIngresoVigAnterior, cuentaMedVigAnterior, cuentaContableCosto);
	}
	
	/**
	 * M�todo para consultar la cuenta contabl de la interfaz de servicio
	 * @param con
	 * @param codigoServicio
	 * @param codigoCentroCosto
	 * @param cuentaIngreso
	 * @param cuentaIngresoVigAnterior
	 * @param cuentaContableCosto
	 * @return
	 */
	public DtoCuentaContable consultarCuentaContableInterfazServicio(Connection con,int codigoServicio,int codigoCentroCosto,boolean cuentaIngreso,boolean cuentaIngresoVigAnterior,boolean cuentaContableCosto)
	{
		return SqlBaseUtilidadesInterfazDao.consultarCuentaContableInterfazServicio(con, codigoServicio, codigoCentroCosto, cuentaIngreso, cuentaIngresoVigAnterior, cuentaContableCosto);
	}
	
	/**
	 * M�todo para consultar la cuenta contable de la ionterfaz inventarios
	 * @param con
	 * @param codigoArticulo
	 * @param codigoCentroCosto
	 * @return
	 */
	public DtoCuentaContable consultarCuentaContableInterfazInventarios(Connection con,int codigoArticulo,int codigoCentroCosto)
	{
		return SqlBaseUtilidadesInterfazDao.consultarCuentaContableInterfazInventarios(con, codigoArticulo, codigoCentroCosto);
	}
	
	/**
	 * M�todo que consulta la cuenta contable de la parametriacion de subgrupo/grupo/clase
	 * @param con
	 * @param codigoArticulo
	 * @param cuentaCosto
	 * @param cuentaInventario
	 * @return
	 */
	public DtoCuentaContable consultarCuentaContableSubgrupoGrupoClase(Connection con,int codigoArticulo,boolean cuentaCosto,boolean cuentaInventario)
	{
		return SqlBaseUtilidadesInterfazDao.consultarCuentaContableSubgrupoGrupoClase(con, codigoArticulo, cuentaCosto, cuentaInventario);
	}
	
	/**
	 * M�todo para consultar los datos de la cuenta contable
	 * @param con
	 * @param cuentaContable
	 * @return
	 */
	public DtoCuentaContable consultarDatosCuentaContable(Connection con,DtoCuentaContable cuentaContable)
	{
		return SqlBaseUtilidadesInterfazDao.consultarDatosCuentaContable(con, cuentaContable);
	}
	
	/**
	 * M�todo para consultar la cuenta contable de cuenta interfaz inventario ingreso
	 * @param con
	 * @param codigoCentroCosto
	 * @param codigoArticulo
	 * @param cuentaIngreso
	 * @param cuentaIngresoVigAnterior
	 * @return
	 */
	public DtoCuentaContable consultarCuentaContableInterfazInvIngreso(Connection con,int codigoCentroCosto,int codigoArticulo,boolean cuentaIngreso,boolean cuentaIngresoVigAnterior)
	{
		return SqlBaseUtilidadesInterfazDao.consultarCuentaContableInterfazInvIngreso(con, codigoCentroCosto, codigoArticulo, cuentaIngreso, cuentaIngresoVigAnterior);
	}
	
	/**
	 * M�todo implementado para verificar si una factura tiene un mismo grupo o diferentes
	 * @param con
	 * @param numeroDocumento
	 * @return
	 */
	public ResultadoBoolean validacionMismoGrupoServicioFactura(Connection con,String numeroDocumento,boolean honorarios)
	{
		return SqlBaseUtilidadesInterfazDao.validacionMismoGrupoServicioFactura(con, numeroDocumento,honorarios);
	}
	
	/**
	 * M�todo para obtener el mismo grupo de servicio de la autorzacion de servicios
	 * @param con
	 * @param numeroDocumento
	 * @return
	 */
	public ResultadoBoolean validacionMismoGrupoServicioAutorizacionEntidadSub(Connection con,String numeroDocumento)
	{
		return SqlBaseUtilidadesInterfazDao.validacionMismoGrupoServicioAutorizacionEntidadSub(con, numeroDocumento);
	}
	/**
	 * M�todo para validar si los medicamentos o despachos pertenecen a la misma/diferente clase
	 * @param con
	 * @param numeroDocumento
	 * @param codigoTipoDocumento
	 * @return
	 */
	public ResultadoBoolean validacionMismaClaseDespachoMedicamentos(Connection con,String numeroDocumento,int codigoTipoDocumento)
	{
		return SqlBaseUtilidadesInterfazDao.validacionMismaClaseDespachoMedicamentos(con, numeroDocumento, codigoTipoDocumento);
	}
	
	/**
	 * M�todo para validar misma clase de la factura
	 * @param con
	 * @param numeroDocumento
	 * @return
	 */
	public ResultadoBoolean validacionMismaClaseFactura(Connection con,String numeroDocumento)
	{
		return SqlBaseUtilidadesInterfazDao.validacionMismaClaseFactura(con, numeroDocumento);
	}
	/**
	 * M�todo implementado para consultar los tipos de retencion que aplican por clase, grupo y conceptode factura varia
	 * @param con
	 * @param codigoInstitucion
	 * @param codigoClase
	 * @param codigoGrupo
	 * @param codigoConcepto
	 * @return
	 */
	public ArrayList<DtoTiposRetencion> consultarTiposRetencionXClaseGrupoConceptoFV(Connection con,int codigoInstitucion,int codigoClase,int codigoGrupo,int codigoConcepto)
	{
		return SqlBaseUtilidadesInterfazDao.consultarTiposRetencionXClaseGrupoConceptoFV(con, codigoInstitucion, codigoClase, codigoGrupo, codigoConcepto);
	}
	
	/**
	 * M�todo que consulta los conceptos que aplican a los conceptos de retencion del tercero
	 * @param con
	 * @param tiposRetencion
	 * @param conceptos
	 * @param nitTercero
	 * @param integral
	 * @param fechaDocumento
	 * @return
	 */
	public ArrayList<DtoConceptoRetencionTercero> consultarConceptosRetencionXTercero(Connection con,ArrayList<DtoTiposRetencion> tiposRetencion,ArrayList<DtoConceptosRetencion> conceptos,String nitTercero,boolean integral,String fechaDocumento)
	{
		return SqlBaseUtilidadesInterfazDao.consultarConceptosRetencionXTercero(con, tiposRetencion, conceptos, nitTercero, integral,fechaDocumento);
	}
	
	/**
	 * M�todo para conultar las conceptos de retencion de la parametrizacion de interfaz sistema 1E
	 * @param con
	 * @param claseDocumento
	 * @param seccion
	 * @param codigoInstitucion
	 * @return
	 */
	public ArrayList<DtoConceptosRetencion> consultarConceptosRetencionParamS1E(Connection con,String claseDocumento,String seccion,int codigoInstitucion)
	{
		return SqlBaseUtilidadesInterfazDao.consultarConceptosRetencionParamS1E(con, claseDocumento, seccion, codigoInstitucion);
	}
	
	/**
	 * M�todo para calcular el valor de la retencion de cada concepto
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
	public DtoRetencion consultarTarifaRetencion(Connection con,ArrayList<DtoConceptoRetencionTercero> conceptos,boolean integral,int codigoGrupo,int codigoClase,int codigoConcepto,String fecha,double valorDocumento)
	{
		return SqlBaseUtilidadesInterfazDao.consultarTarifaRetencion(con, conceptos, integral, codigoGrupo, codigoClase, codigoConcepto, fecha, valorDocumento);
	}
	
	/**
	 * M�todo que verifica si una factura aplica para el calculo de la autoretencion
	 * @param con
	 * @param numeroDocumento
	 * @return
	 */
	public ResultadoBoolean validacionFacturaPacienteAutoretencion(Connection con,String numeroDocumento)
	{
		return SqlBaseUtilidadesInterfazDao.validacionFacturaPacienteAutoretencion(con, numeroDocumento);
	}
	
	/**
	 * M�todo que consulta el n�mero de servicios y articulos que tiene una factura
	 * @param con
	 * @param numeroDocumento
	 * @return
	 */
	public int[] consultarNumServiciosYArticulosFactura(Connection con,String numeroDocumento)
	{
		return SqlBaseUtilidadesInterfazDao.consultarNumServiciosYArticulosFactura(con, numeroDocumento);
	}
	
	/**
	 * M�todo para validar si la factura tiene iguales conceptos o distintos
	 * @param con
	 * @param numeroDocumento
	 * @return
	 */
	public ResultadoBoolean validacionMismoConceptoFacturaVaria(Connection con,String numeroDocumento)
	{
		return SqlBaseUtilidadesInterfazDao.validacionMismoConceptoFacturaVaria(con, numeroDocumento);
	}
}
