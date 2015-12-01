/*
 * Julio 21, 2009
 */
package com.princetonsa.dao.sqlbase.interfaz;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.administracion.DtoConceptosRetencion;
import com.princetonsa.dto.administracion.DtoTiposRetencion;
import com.princetonsa.dto.facturacion.DtoConceptoRetencionTercero;
import com.princetonsa.dto.interfaz.DtoCuentaContable;
import com.princetonsa.dto.interfaz.DtoRetencion;

/**
 * Clase para el manejo de los métodos genéricos
 * al acceso a la fuente de datos para las utilidades del 
 * módulo de interfaz de Axioma
 * @author Sebastián Gómez R.
 *
 */
public class SqlBaseUtilidadesInterfazDao 
{
	/**
	 * Manejador de log de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseUtilidadesInterfazDao.class);
	
	/**
	 * Cadena para consultar la cuenta contable de la parametrizacion cuenta interfaz convenio
	 */
	private static final String consultarCuentaContableInterfazConvenioStr = "SELECT # as valor FROM cuenta_convenio WHERE cod_convenio = ? and cod_tipo_cuenta = ?";
	
	
	private static final String consultarCuentaContableInterfazRegimenStr = "SELECT # as valor FROM cuenta_regimen WHERE acr_tipo_regimen = ? and cod_tipo_cuenta = ?";
	/**
	 * Cadena para consultar los datos de la cuenta contable
	 */
	private static final String consultarDatosCuentaContableStr  = "SELECT "+ 
		"cc.codigo as codigo, "+
		"cc.cuenta_contable as cuenta_contable, "+
		"cc.descripcion as descripcion, "+
		"cc.activo as activo, "+
		"cc.manejo_terceros as manejo_terceros, "+
		"cc.manejo_centros_costo as manejo_centros_costo, "+
		"cc.manejo_base_gravable as manejo_base_gravable, "+
		"cc.naturaleza_cuenta as naturaleza_cuenta, "+
		"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, "+
		"cc.anio_vigencia as anio_vigencia "+ 
		"from cuentas_contables cc "+ 
		"WHERE cc.codigo = ?";
	
	/**
	 * Cadena para consultarl a unidad funcional del centro de costo
	 */
	private static final String consultarUnidadFuncionalCentroCostoStr = "SELECT unidad_funcional,institucion from centros_costo where codigo = ?";
	
	/**
	 * Cadena para copnsultar los datos del servicio
	 */
	private static final String consultarDatosServicioStr = "SELECT especialidad,tipo_servicio,grupo_servicio from servicios where codigo = ?";
	
	/**
	 * Cadena para consultar el subgrupo, grupo y clase del artículo
	 */
	private static final String consultarSubgrupoGrupoClaseArticuloStr = "SELECT si.codigo as codigo_subgrupo, si.subgrupo as subgrupo,si.grupo as grupo,si.clase as clase from articulo a inner join subgrupo_inventario si on(si.codigo = a.subgrupo) WHERE a.codigo = ?";
	
	/*
	 * Cadena para obtener la cuetna subgrupo de inventario
	 */
	private static final String consultarCuentaSubgrupoInventarioStr = "SELECT "+ 
		"cc.codigo as codigo, "+
		"cc.cuenta_contable as cuenta_contable, "+
		"cc.descripcion as descripcion, "+
		"cc.activo as activo, "+
		"cc.manejo_terceros as manejo_terceros, "+
		"cc.manejo_centros_costo as manejo_centros_costo, "+
		"cc.manejo_base_gravable as manejo_base_gravable, "+
		"cc.naturaleza_cuenta as naturaleza_cuenta, "+
		"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, "+
		"cc.anio_vigencia as anio_vigencia "+ 
		"FROM interfaz.subgrupo_inv_unidad_fun si "+ 
		"INNER JOIN cuentas_contables cc ON(cc.codigo = si.cuenta_costo) "+  
		"WHERE si.subgrupo_inventario = ? and si.grupo_inventario = ? and si.clase_inventario = ? and si.unidad_funcional = ? and si.institucion = ?";
	
	/**
	 * Cadena para obtener la cuenta grupo de inventario
	 */
	private static final String consultarCuentaGrupoInventarioStr = "SELECT " +
		"cc.codigo as codigo, "+
		"cc.cuenta_contable as cuenta_contable, "+
		"cc.descripcion as descripcion, "+
		"cc.activo as activo, "+
		"cc.manejo_terceros as manejo_terceros, "+
		"cc.manejo_centros_costo as manejo_centros_costo, "+
		"cc.manejo_base_gravable as manejo_base_gravable, "+
		"cc.naturaleza_cuenta as naturaleza_cuenta, "+
		"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, "+
		"cc.anio_vigencia as anio_vigencia "+ 
		"FROM grupo_inv_unidad_fun gi "+ 
		"INNER JOIN cuentas_contables cc ON(cc.codigo = gi.cuenta_costo) "+  
		"WHERE gi.grupo_inventario = ? and gi.clase_inventario = ? and gi.unidad_funcional = ? and gi.institucion = ?";
	
	/**
	 * Cadena para obtener la cuenta clase de inventario
	 */
	private static final String consultarCuentaClaseInventarioStr = "SELECT "+ 
		"cc.codigo as codigo, "+
		"cc.cuenta_contable as cuenta_contable, "+
		"cc.descripcion as descripcion, "+
		"cc.activo as activo, "+
		"cc.manejo_terceros as manejo_terceros, "+
		"cc.manejo_centros_costo as manejo_centros_costo, "+
		"cc.manejo_base_gravable as manejo_base_gravable, "+
		"cc.naturaleza_cuenta as naturaleza_cuenta, "+
		"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, "+
		"cc.anio_vigencia as anio_vigencia "+ 
		"FROM clase_inv_unidad_fun ci "+ 
		"INNER JOIN cuentas_contables cc ON(cc.codigo = ci.cuenta_costo) "+  
		"where ci.clase_inventario = ? and ci.unidad_funcional = ? and ci.institucion = ?";
	
	/**
	 * Cadena que consulta cuenta contable de subgrupos
	 */
	private static final String consultarCuentaSubgrupoStr = "SELECT "+
		"cc.codigo as codigo, "+
		"cc.cuenta_contable as cuenta_contable, "+
		"cc.descripcion as descripcion, "+
		"cc.activo as activo, "+
		"cc.manejo_terceros as manejo_terceros, "+
		"cc.manejo_centros_costo as manejo_centros_costo, "+
		"cc.manejo_base_gravable as manejo_base_gravable, "+
		"cc.naturaleza_cuenta as naturaleza_cuenta, "+
		"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, "+
		"cc.anio_vigencia as anio_vigencia "+ 
		"FROM subgrupo_inventario si "+ 
		"INNER JOIN cuentas_contables cc ON(cc.codigo = si.#) "+  
		"where si.codigo = ? and si.grupo = ? and si.clase = ?";
	
	/**
	 * Cadena que consulta cuenta contable de grupos
	 */
	private static final String consultarCuentaGrupoStr = "SELECT "+ 
		"cc.codigo as codigo, "+
		"cc.cuenta_contable as cuenta_contable, "+
		"cc.descripcion as descripcion, "+
		"cc.activo as activo, "+
		"cc.manejo_terceros as manejo_terceros, "+
		"cc.manejo_centros_costo as manejo_centros_costo, "+
		"cc.manejo_base_gravable as manejo_base_gravable, "+
		"cc.naturaleza_cuenta as naturaleza_cuenta, "+
		"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, "+
		"cc.anio_vigencia as anio_vigencia "+ 
		"FROM grupo_inventario gi "+ 
		"INNER JOIN cuentas_contables cc ON(cc.codigo = gi.#) "+  
		"WHERE gi.codigo = ? and gi.clase = ?";
	
	/**
	 * Cadena que consulta cuenta contable de clases
	 */
	private static final String consultarCuentaClaseStr = "SELECT "+
		"cc.codigo as codigo, "+
		"cc.cuenta_contable as cuenta_contable, "+
		"cc.descripcion as descripcion, "+
		"cc.activo as activo, "+
		"cc.manejo_terceros as manejo_terceros, "+
		"cc.manejo_centros_costo as manejo_centros_costo, "+
		"cc.manejo_base_gravable as manejo_base_gravable, "+
		"cc.naturaleza_cuenta as naturaleza_cuenta, "+
		"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, "+
		"cc.anio_vigencia as anio_vigencia "+ 
		"FROM clase_inventario ci "+ 
		"INNER JOIN cuentas_contables cc ON(cc.codigo = ci.#) "+  
		"WHERE ci.codigo = ? ";
	
	/**
	 * Cadena que consulta cuenta articulo de inventario ingreso
	 */
	private static final String consultarCuentaArticuloInvIngresoStr = "SELECT "+ 
		"cc.codigo as codigo, "+ 
		"cc.cuenta_contable as cuenta_contable, "+ 
		"cc.descripcion as descripcion, "+ 
		"cc.activo as activo, "+ 
		"cc.manejo_terceros as manejo_terceros, "+ 
		"cc.manejo_centros_costo as manejo_centros_costo, "+ 
		"cc.manejo_base_gravable as manejo_base_gravable, "+ 
		"cc.naturaleza_cuenta as naturaleza_cuenta, "+ 
		"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, "+ 
		"cc.anio_vigencia "+  
		"FROM articulo_inv_cuenta_ing ai "+ 
		"INNER JOIN cuentas_contables cc ON(cc.codigo = ai.#) "+  
		"WHERE ai.centro_costo = ? and ai.articulo = ?";
	
	/**
	 * Cadena que consulta cuenta subgrupo de inventarios ingreso
	 */
	private static final String consultarCuentaSubgrupoInvIngresoStr = "SELECT "+ 
		"cc.codigo as codigo, "+ 
		"cc.cuenta_contable as cuenta_contable, "+ 
		"cc.descripcion as descripcion, "+ 
		"cc.activo as activo, "+ 
		"cc.manejo_terceros as manejo_terceros, "+ 
		"cc.manejo_centros_costo as manejo_centros_costo, "+ 
		"cc.manejo_base_gravable as manejo_base_gravable, "+ 
		"cc.naturaleza_cuenta as naturaleza_cuenta, "+ 
		"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, "+ 
		"cc.anio_vigencia "+  
		"FROM subgrupo_inv_cuenta_ing si "+ 
		"INNER JOIN cuentas_contables cc ON(cc.codigo = si.#) "+  
		"WHERE si.centro_costo = ? and si.subgrupo_inventario = ?";
	
	/**
	 * Cadena que consulta cuenta grupo de inventarios ingreso
	 */
	private static final String consultarCuentaGrupoInvIngresoStr = "SELECT "+
		"cc.codigo as codigo, "+ 
		"cc.cuenta_contable as cuenta_contable, "+ 
		"cc.descripcion as descripcion, "+ 
		"cc.activo as activo, "+ 
		"cc.manejo_terceros as manejo_terceros, "+ 
		"cc.manejo_centros_costo as manejo_centros_costo, "+ 
		"cc.manejo_base_gravable as manejo_base_gravable, "+ 
		"cc.naturaleza_cuenta as naturaleza_cuenta, "+ 
		"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, "+ 
		"cc.anio_vigencia "+  
		"FROM  grupo_inv_cuenta_ing gi "+ 
		"INNER JOIN cuentas_contables cc ON(cc.codigo = gi.#) "+   
		"WHERE gi.centro_costo = ? and gi.grupo_inventario = ? and gi.clase_inventario = ?";
	
	/**
	 * Cadena que consulta la cuenta clase de inventarios ingreso
	 */
	private static final String consultarCuentaClaseInvIngresoStr = "SELECT "+ 
		"cc.codigo as codigo, "+ 
		"cc.cuenta_contable as cuenta_contable, "+ 
		"cc.descripcion as descripcion, "+ 
		"cc.activo as activo, "+ 
		"cc.manejo_terceros as manejo_terceros, "+ 
		"cc.manejo_centros_costo as manejo_centros_costo, "+ 
		"cc.manejo_base_gravable as manejo_base_gravable, "+ 
		"cc.naturaleza_cuenta as naturaleza_cuenta, "+ 
		"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, "+ 
		"cc.anio_vigencia "+  
		"FROM clase_invent_cuenta_ing ci "+ 
		"INNER JOIN cuentas_contables cc ON(cc.codigo = ci.#) "+   
		"WHERE ci.centro_costo = ? and ci.clase_inventario = ?";
	
	/**
	 * Método para consultar los datos de la cuenta contable de la interfaz convenio
	 * @param con
	 * @param codigoConvenio
	 * @param tipoRegimen
	 * @param tipoCuenta
	 * @return
	 */
	public static DtoCuentaContable consultarCuentaContableInterfazConvenio(Connection con,int codigoConvenio,String tipoRegimen,int tipoCuenta,boolean cuentaContable,boolean cuentaCxCapitacion)
	{
		DtoCuentaContable cuenta = new DtoCuentaContable();
		try
		{
			String cuentaFiltro = "valor";
			if(cuentaContable)
			{
				cuentaFiltro = "valor";
			}
			else if(cuentaCxCapitacion)
			{
				cuentaFiltro = "cuenta_contable_cxc_cap";
			}
			
			String consulta = consultarCuentaContableInterfazConvenioStr.replace("#", cuentaFiltro);
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoConvenio);
			pst.setInt(2,tipoCuenta);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				cuenta.setCodigo(rs.getString("valor"));
			}
			pst.close();
			rs.close();
			
			
			if(cuenta.getCodigo().equals(""))
			{
				consulta = consultarCuentaContableInterfazRegimenStr.replace("#", cuentaFiltro);
				pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setString(1,tipoRegimen);
				pst.setInt(2,tipoCuenta);
				rs = new ResultSetDecorator(pst.executeQuery());
				if(rs.next())
				{
					cuenta.setCodigo(rs.getString("valor"));
				}
				pst.close();
				rs.close();
			}
			
			
			if(!cuenta.getCodigo().equals(""))
			{
				pst = new PreparedStatementDecorator(con.prepareStatement(consultarDatosCuentaContableStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1,Integer.parseInt(cuenta.getCodigo()));
				rs = new ResultSetDecorator(pst.executeQuery());
				if(rs.next())
				{
					cuenta.setCuentaContable(rs.getString("cuenta_contable"));
					cuenta.setDescripcion(rs.getString("descripcion"));
					cuenta.setActivo(UtilidadTexto.getBoolean(rs.getString("activo")));
					cuenta.setManejaTerceros(UtilidadTexto.getBoolean(rs.getString("manejo_terceros")));
					cuenta.setManejoCentrosCosto(UtilidadTexto.getBoolean(rs.getString("manejo_centros_costo")));
					cuenta.setManejoBaseGravable(UtilidadTexto.getBoolean(rs.getString("manejo_base_gravable")));
					cuenta.setNaturalezaCuenta(rs.getString("naturaleza_cuenta"));
					cuenta.setCodigoInstitucion(rs.getInt("institucion"));
					cuenta.setAnioVigencia(rs.getString("anio_vigencia"));
				}
				rs.close();
				pst.close();
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarCuentaContableInterfazConvenio: "+e);
		}
		return cuenta;
	}
	
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
	public static DtoCuentaContable consultarCuentaContableInterfazUnidadFuncional(Connection con,int codigoCentroCosto,boolean cuentaIngreso,boolean cuentaMedicamento, boolean cuentaIngresoVigAnterior,boolean cuentaMedVigAnterior,boolean cuentaContableCosto)
	{
		DtoCuentaContable cuenta = new DtoCuentaContable();
		try
		{
			//***************SE COSNULTA LA UNIDAD FUNICONAL Y LA INSTITUCION DEL CENTRO DE COSTO**************************+
			String codigoUnidadFuncional = "";
			int codigoInstitucion = ConstantesBD.codigoNuncaValido;
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consultarUnidadFuncionalCentroCostoStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoCentroCosto);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				codigoUnidadFuncional = rs.getString("unidad_funcional");
				codigoInstitucion = rs.getInt("institucion");
			}
			pst.close();
			rs.close();
			//*****************************************************************************************************************
			
			String cuentaFiltro = "cuenta_ingreso";
			
			if(cuentaIngreso)
			{
				cuentaFiltro = "cuenta_ingreso";
			}
			else if(cuentaMedicamento)
			{
				cuentaFiltro = "cuenta_medicamento";
			}
			else if(cuentaIngresoVigAnterior)
			{
				cuentaFiltro = "cuenta_ingreso_vig_anterior";
			}
			else if(cuentaMedVigAnterior)
			{
				cuentaFiltro = "cuenta_med_vig_anterior";
			}
			else if(cuentaContableCosto)
			{
				cuentaFiltro = "cuenta_contable_costo";
			}
			
			//**************************BUSQUEDA CUENTA CONTABLE POR UNIDAD FUNCIONAL****************************************
			String consulta = "SELECT "+
				"cc.codigo as codigo, "+
				"cc.cuenta_contable as cuenta_contable, "+
				"cc.descripcion as descripcion, "+
				"cc.activo as activo, "+	
				"cc.manejo_terceros as manejo_terceros, "+
				"cc.manejo_centros_costo as manejo_centros_costo, "+
				"cc.manejo_base_gravable as manejo_base_gravable, "+
				"cc.naturaleza_cuenta as naturaleza_cuenta, "+
				"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, "+
				"cc.anio_vigencia as anio_vigencia "+ 
				"from unidad_fun_cuenta_ing uf "+ 
				"INNER JOIN cuentas_contables cc ON(cc.codigo = uf."+cuentaFiltro+") "+  
				"WHERE uf.unidad_funcional = ? and uf.institucion = ?";
			pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setString(1,codigoUnidadFuncional);
			pst.setInt(2,codigoInstitucion);
			rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				cuenta.setCodigo(rs.getString("codigo"));
				cuenta.setCuentaContable(rs.getString("cuenta_contable"));
				cuenta.setActivo(UtilidadTexto.getBoolean(rs.getString("activo")));
				cuenta.setManejaTerceros(UtilidadTexto.getBoolean(rs.getString("manejo_terceros")));
				cuenta.setManejoCentrosCosto(UtilidadTexto.getBoolean(rs.getString("manejo_centros_costo")));
				cuenta.setManejoBaseGravable(UtilidadTexto.getBoolean(rs.getString("manejo_base_gravable")));
				cuenta.setNaturalezaCuenta(rs.getString("naturaleza_cuenta"));
				cuenta.setCodigoInstitucion(rs.getInt("institucion"));
				cuenta.setAnioVigencia(rs.getString("anio_vigencia"));
			}
			pst.close();
			rs.close();
			
			//*******************BUSQUEDA CUENTA CONTABLE POR CENTRO DE COSTO*****************************************************************
			if(cuenta.getCodigo().equals(""))
			{
				consulta = "SELECT "+
				"cc.codigo as codigo, "+
				"cc.cuenta_contable as cuenta_contable, "+
				"cc.descripcion as descripcion, "+
				"cc.activo as activo, "+	
				"cc.manejo_terceros as manejo_terceros, "+
				"cc.manejo_centros_costo as manejo_centros_costo, "+
				"cc.manejo_base_gravable as manejo_base_gravable, "+
				"cc.naturaleza_cuenta as naturaleza_cuenta, "+
				"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, "+
				"cc.anio_vigencia as anio_vigencia "+ 
				"from unidad_fun_cuenta_ing_cc uf "+ 
				"INNER JOIN cuentas_contables cc ON(cc.codigo = uf."+cuentaFiltro+") "+  
				"WHERE uf.unidad_funcional = ? and uf.institucion = ? and uf.centro_costo = ?";
				pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setString(1,codigoUnidadFuncional);
				pst.setInt(2,codigoInstitucion);
				pst.setInt(3,codigoCentroCosto);
				rs = new ResultSetDecorator(pst.executeQuery());
				if(rs.next())
				{
					cuenta.setCodigo(rs.getString("codigo"));
					cuenta.setCuentaContable(rs.getString("cuenta_contable"));
					cuenta.setActivo(UtilidadTexto.getBoolean(rs.getString("activo")));
					cuenta.setManejaTerceros(UtilidadTexto.getBoolean(rs.getString("manejo_terceros")));
					cuenta.setManejoCentrosCosto(UtilidadTexto.getBoolean(rs.getString("manejo_centros_costo")));
					cuenta.setManejoBaseGravable(UtilidadTexto.getBoolean(rs.getString("manejo_base_gravable")));
					cuenta.setNaturalezaCuenta(rs.getString("naturaleza_cuenta"));
					cuenta.setCodigoInstitucion(rs.getInt("institucion"));
					cuenta.setAnioVigencia(rs.getString("anio_vigencia"));
				}
				pst.close();
				rs.close();
			}
			//***********************************************************************************************************************************
		}
		catch(SQLException e)
		{
			logger.error("Error consultarCuentaContableInterfazUnidadFuncional: "+e);
		}
		return cuenta;
	}
	
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
	public static DtoCuentaContable consultarCuentaContableInterfazServicio(Connection con,int codigoServicio,int codigoCentroCosto,boolean cuentaIngreso,boolean cuentaIngresoVigAnterior,boolean cuentaContableCosto)
	{
		DtoCuentaContable cuenta = new DtoCuentaContable();
		int i=0;
		try
		{
			//*****************sE CONSULTAN LOS DATOS DEL SERVICIO*********************************************
			int codigoEspecialidad = ConstantesBD.codigoNuncaValido;
			int codigoGrupoServicio = ConstantesBD.codigoNuncaValido;
			String tipoServicio = "";
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consultarDatosServicioStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//logger.info("consulta a arreglar "+consultarDatosServicioStr);
			pst.setInt(1,codigoServicio);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				codigoEspecialidad = rs.getInt("especialidad");
				codigoGrupoServicio = rs.getInt("grupo_servicio");
				tipoServicio = rs.getString("tipo_servicio");
			}
			pst.close();
			rs.close();
			//*************************************************************************************************
			
			//************SE TOMA EL FILTRO DE CUENTA A REALIZAR***********************************************
			String cuentaFiltro = "cuenta_ingreso";
			if(cuentaIngreso)
			{
				cuentaFiltro = "cuenta_ingreso";
			}
			else if(cuentaIngresoVigAnterior)
			{
				cuentaFiltro = "cuenta_ingreso_vig_anterior";
			}
			else if(cuentaContableCosto)
			{
				cuentaFiltro = "cuenta_contable_costo";
			}
			//****************************************************************************************************
			
			while(cuenta.getCuentaContable().equals("")&&i<=1)
			{
				if(i==1)
				{
					codigoCentroCosto = ConstantesBD.codigoCentroCostoTodos;
				}
			
				//********************CONSULTA CUENTA CONTABLE X SERVICIO Y CENTRO DE COSTO***************************+
				String consulta = "SELECT "+ 
					"cc.codigo as codigo, "+
					"cc.cuenta_contable as cuenta_contable, "+
					"cc.descripcion as descripcion, "+
					"cc.activo as activo, "+	
					"cc.manejo_terceros as manejo_terceros, "+
					"cc.manejo_centros_costo as manejo_centros_costo, "+
					"cc.manejo_base_gravable as manejo_base_gravable, "+
					"cc.naturaleza_cuenta as naturaleza_cuenta, "+
					"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, "+
					"cc.anio_vigencia as anio_vigencia "+ 
					"FROM servicio_cuenta_ingreso sc "+ 
					"INNER JOIN cuentas_contables cc ON(cc.codigo = sc."+cuentaFiltro+") "+  
					"WHERE sc.servicio = ? and sc.centro_costo = ?";
				pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setInt(1,codigoServicio);
				pst.setInt(2,codigoCentroCosto);
				rs = new ResultSetDecorator(pst.executeQuery());
				if(rs.next())
				{
					cuenta.setCodigo(rs.getString("codigo"));
					cuenta.setCuentaContable(rs.getString("cuenta_contable"));
					cuenta.setActivo(UtilidadTexto.getBoolean(rs.getString("activo")));
					cuenta.setManejaTerceros(UtilidadTexto.getBoolean(rs.getString("manejo_terceros")));
					cuenta.setManejoCentrosCosto(UtilidadTexto.getBoolean(rs.getString("manejo_centros_costo")));
					cuenta.setManejoBaseGravable(UtilidadTexto.getBoolean(rs.getString("manejo_base_gravable")));
					cuenta.setNaturalezaCuenta(rs.getString("naturaleza_cuenta"));
					cuenta.setCodigoInstitucion(rs.getInt("institucion"));
					cuenta.setAnioVigencia(rs.getString("anio_vigencia"));
				}
				pst.close();
				rs.close();
				//******************************************************************************************************
				
				//*******************CONSULTA CUENTA CONTABLE X ESPECIALIDAD********************************************
				//Si todavpia no se ha encotnrado la cuenta se busca por especialidad
				if(cuenta.getCodigo().equals(""))
				{
					consulta = "SELECT "+ 
					"cc.codigo as codigo, "+
					"cc.cuenta_contable as cuenta_contable, "+
					"cc.descripcion as descripcion, "+
					"cc.activo as activo, "+	
					"cc.manejo_terceros as manejo_terceros, "+
					"cc.manejo_centros_costo as manejo_centros_costo, "+
					"cc.manejo_base_gravable as manejo_base_gravable, "+
					"cc.naturaleza_cuenta as naturaleza_cuenta, "+
					"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, "+
					"cc.anio_vigencia as anio_vigencia "+ 
					"FROM especi_serv_cue_ing es "+ 
					"INNER JOIN cuentas_contables cc ON(cc.codigo = es."+cuentaFiltro+") "+  
					"WHERE es.grupo_servicio = ? and es.tipo_servicio = ? and es.centro_costo = ? and (es.especialidad_servicio = ? or  es.especialidad_servicio = ?) "+ 
					"order by es.especialidad_servicio desc";
					pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst.setInt(1,codigoGrupoServicio);
					pst.setString(2,tipoServicio);
					pst.setInt(3,codigoCentroCosto);
					pst.setInt(4, codigoEspecialidad);
					pst.setInt(5, ConstantesBD.codigoEspecialidadMedicaTodos);
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
					{
						cuenta.setCodigo(rs.getString("codigo"));
						cuenta.setCuentaContable(rs.getString("cuenta_contable"));
						cuenta.setActivo(UtilidadTexto.getBoolean(rs.getString("activo")));
						cuenta.setManejaTerceros(UtilidadTexto.getBoolean(rs.getString("manejo_terceros")));
						cuenta.setManejoCentrosCosto(UtilidadTexto.getBoolean(rs.getString("manejo_centros_costo")));
						cuenta.setManejoBaseGravable(UtilidadTexto.getBoolean(rs.getString("manejo_base_gravable")));
						cuenta.setNaturalezaCuenta(rs.getString("naturaleza_cuenta"));
						cuenta.setCodigoInstitucion(rs.getInt("institucion"));
						cuenta.setAnioVigencia(rs.getString("anio_vigencia"));
					}
					pst.close();
					rs.close();
				}
				//********************************************************************************************************
				
				//**************CONSULTA CUENTA CONTABLE X TIPO SERVICIO*******************************************************
				//Si no se ha encontrado la cuenta se busca por tipo servicio
				if(cuenta.getCodigo().equals(""))
				{
					consulta = "SELECT "+ 
						"cc.codigo as codigo, "+
						"cc.cuenta_contable as cuenta_contable, "+
						"cc.descripcion as descripcion, "+
						"cc.activo as activo, "+	
						"cc.manejo_terceros as manejo_terceros, "+
						"cc.manejo_centros_costo as manejo_centros_costo, "+
						"cc.manejo_base_gravable as manejo_base_gravable, "+
						"cc.naturaleza_cuenta as naturaleza_cuenta, "+
						"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, "+
						"cc.anio_vigencia as anio_vigencia "+  
						"FROM tipo_servicio_cuenta_ing ts "+ 
						"INNER JOIN cuentas_contables cc ON(cc.codigo = ts."+cuentaFiltro+") "+  
						"WHERE ts.grupo_servicio = ? and (ts.tipo_servicio = ? or ts.tipo_servicio = ?) and ts.centro_costo = ? "+ 
						"order by ts.tipo_servicio desc";
					pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst.setInt(1,codigoGrupoServicio);
					pst.setString(2,tipoServicio);
					pst.setString(3,"1"); //simboliza todos los tipos de servicio
					pst.setInt(4,codigoCentroCosto);
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
					{
						cuenta.setCodigo(rs.getString("codigo"));
						cuenta.setCuentaContable(rs.getString("cuenta_contable"));
						cuenta.setActivo(UtilidadTexto.getBoolean(rs.getString("activo")));
						cuenta.setManejaTerceros(UtilidadTexto.getBoolean(rs.getString("manejo_terceros")));
						cuenta.setManejoCentrosCosto(UtilidadTexto.getBoolean(rs.getString("manejo_centros_costo")));
						cuenta.setManejoBaseGravable(UtilidadTexto.getBoolean(rs.getString("manejo_base_gravable")));
						cuenta.setNaturalezaCuenta(rs.getString("naturaleza_cuenta"));
						cuenta.setCodigoInstitucion(rs.getInt("institucion"));
						cuenta.setAnioVigencia(rs.getString("anio_vigencia"));
					}
					pst.close();
					rs.close();
				}
				//**************************************************************************************************************
				
				//******************CONSULTA CUENTA CONTABLE X GRUPO SERVICIO*****************************************************
				//Si no se ha encontrado la cuenta se busca por grupo servicio
				if(cuenta.getCodigo().equals(""))
				{
					consulta = "SELECT "+
						"cc.codigo as codigo, "+
						"cc.cuenta_contable as cuenta_contable, "+
						"cc.descripcion as descripcion, "+
						"cc.activo as activo, "+	
						"cc.manejo_terceros as manejo_terceros, "+
						"cc.manejo_centros_costo as manejo_centros_costo, "+
						"cc.manejo_base_gravable as manejo_base_gravable, "+
						"cc.naturaleza_cuenta as naturaleza_cuenta, "+
						"coalesce(cc.institucion,"+ConstantesBD.codigoNuncaValido+") as institucion, "+
						"cc.anio_vigencia as anio_vigencia "+  
						"FROM grupo_servicio_cue_ingr gs "+ 
						"INNER JOIN cuentas_contables cc ON(cc.codigo = gs."+cuentaFiltro+") "+  
						"WHERE (gs.grupo_servicio = ? or gs.grupo_servicio = ?) and gs.centro_costo = ? "+ 
						"order by gs.grupo_servicio desc";
					pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst.setInt(1,codigoGrupoServicio);
					pst.setInt(2,ConstantesBD.codigoNuncaValido); //simboliza todos los grupos de servicio
					pst.setInt(3,codigoCentroCosto);
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
					{
						cuenta.setCodigo(rs.getString("codigo"));
						cuenta.setCuentaContable(rs.getString("cuenta_contable"));
						cuenta.setActivo(UtilidadTexto.getBoolean(rs.getString("activo")));
						cuenta.setManejaTerceros(UtilidadTexto.getBoolean(rs.getString("manejo_terceros")));
						cuenta.setManejoCentrosCosto(UtilidadTexto.getBoolean(rs.getString("manejo_centros_costo")));
						cuenta.setManejoBaseGravable(UtilidadTexto.getBoolean(rs.getString("manejo_base_gravable")));
						cuenta.setNaturalezaCuenta(rs.getString("naturaleza_cuenta"));
						cuenta.setCodigoInstitucion(rs.getInt("institucion"));
						cuenta.setAnioVigencia(rs.getString("anio_vigencia"));
					}
					pst.close();
					rs.close();
				}
				//*****************************************************************************************************************
				
				i++;
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarCuentaContableInterfazServicio: ",e);
		}
		return cuenta;
	}
	
	/**
	 * Método para consultar la cuenta contable de la ionterfaz inventarios
	 * @param con
	 * @param codigoArticulo
	 * @param codigoCentroCosto
	 * @return
	 */
	public static DtoCuentaContable consultarCuentaContableInterfazInventarios(Connection con,int codigoArticulo,int codigoCentroCosto)
	{
		DtoCuentaContable cuenta = new DtoCuentaContable();
		try
		{
			//***********SE TOMA LA UNIDAD FUNCIONAL DEL CENTRO DE COSTO************************************
			String codigoUnidadFuncional = "";
			int codigoInstitucion = ConstantesBD.codigoNuncaValido;
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consultarUnidadFuncionalCentroCostoStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoCentroCosto);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				codigoUnidadFuncional = rs.getString("unidad_funcional");
				codigoInstitucion = rs.getInt("institucion");
			}
			pst.close();
			rs.close();
			
			//************SE TOMA EL SUBGRUPO,GRUPO Y CLASE DEL ARTICULO*****************************************
			int codigoSubgrupo = ConstantesBD.codigoNuncaValido;
			int codigoGrupo = ConstantesBD.codigoNuncaValido;
			int codigoClase = ConstantesBD.codigoNuncaValido;
			pst = new PreparedStatementDecorator(con.prepareStatement(consultarSubgrupoGrupoClaseArticuloStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoArticulo);
			rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				codigoSubgrupo = rs.getInt("subgrupo");
				codigoGrupo = rs.getInt("grupo");
				codigoClase = rs.getInt("clase");
			}
			pst.close();
			rs.close();
			//***************************************************************************************************
			
			//*******************************SE TOMA LA CUENTA SUBGRUPO INTERFAZ INVENTARIO*************************************
			pst = new PreparedStatementDecorator(con.prepareStatement(consultarCuentaSubgrupoInventarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoSubgrupo);
			pst.setInt(2,codigoGrupo);
			pst.setInt(3,codigoClase);
			pst.setString(4,codigoUnidadFuncional);
			pst.setInt(5,codigoInstitucion);
			rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				cuenta.setCodigo(rs.getString("codigo"));
				cuenta.setCuentaContable(rs.getString("cuenta_contable"));
				cuenta.setActivo(UtilidadTexto.getBoolean(rs.getString("activo")));
				cuenta.setManejaTerceros(UtilidadTexto.getBoolean(rs.getString("manejo_terceros")));
				cuenta.setManejoCentrosCosto(UtilidadTexto.getBoolean(rs.getString("manejo_centros_costo")));
				cuenta.setManejoBaseGravable(UtilidadTexto.getBoolean(rs.getString("manejo_base_gravable")));
				cuenta.setNaturalezaCuenta(rs.getString("naturaleza_cuenta"));
				cuenta.setCodigoInstitucion(rs.getInt("institucion"));
				cuenta.setAnioVigencia(rs.getString("anio_vigencia"));
			}
			pst.close();
			rs.close();
			//********************************************************************************************************************
			//*******************************SE TOMA LA CUENTA GRUPO INTERFAZ INVENTARIO********************************************
			if(cuenta.getCodigo().equals(""))
			{
				pst = new PreparedStatementDecorator(con.prepareStatement(consultarCuentaGrupoInventarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1,codigoGrupo);
				pst.setInt(2,codigoClase);
				pst.setString(3,codigoUnidadFuncional);
				pst.setInt(4,codigoInstitucion);
				rs = new ResultSetDecorator(pst.executeQuery());
				
				if(rs.next())
				{
					cuenta.setCodigo(rs.getString("codigo"));
					cuenta.setCuentaContable(rs.getString("cuenta_contable"));
					cuenta.setActivo(UtilidadTexto.getBoolean(rs.getString("activo")));
					cuenta.setManejaTerceros(UtilidadTexto.getBoolean(rs.getString("manejo_terceros")));
					cuenta.setManejoCentrosCosto(UtilidadTexto.getBoolean(rs.getString("manejo_centros_costo")));
					cuenta.setManejoBaseGravable(UtilidadTexto.getBoolean(rs.getString("manejo_base_gravable")));
					cuenta.setNaturalezaCuenta(rs.getString("naturaleza_cuenta"));
					cuenta.setCodigoInstitucion(rs.getInt("institucion"));
					cuenta.setAnioVigencia(rs.getString("anio_vigencia"));
				}
				pst.close();
				rs.close();
			}
			//************************************************************************************************************************
			//*******************SE TOMA LA CUENTA CLASE INTERFAZ INVENTARIO***********************************************************
			if(cuenta.getCodigo().equals(""))
			{
				pst = new PreparedStatementDecorator(con.prepareStatement(consultarCuentaClaseInventarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1,codigoClase);
				pst.setString(2,codigoUnidadFuncional);
				pst.setInt(3,codigoInstitucion);
				rs = new ResultSetDecorator(pst.executeQuery());
				
				if(rs.next())
				{
					cuenta.setCodigo(rs.getString("codigo"));
					cuenta.setCuentaContable(rs.getString("cuenta_contable"));
					cuenta.setActivo(UtilidadTexto.getBoolean(rs.getString("activo")));
					cuenta.setManejaTerceros(UtilidadTexto.getBoolean(rs.getString("manejo_terceros")));
					cuenta.setManejoCentrosCosto(UtilidadTexto.getBoolean(rs.getString("manejo_centros_costo")));
					cuenta.setManejoBaseGravable(UtilidadTexto.getBoolean(rs.getString("manejo_base_gravable")));
					cuenta.setNaturalezaCuenta(rs.getString("naturaleza_cuenta"));
					cuenta.setCodigoInstitucion(rs.getInt("institucion"));
					cuenta.setAnioVigencia(rs.getString("anio_vigencia"));
				}
				pst.close();
				rs.close();
			}
			//**************************************************************************************************************************
			//*****************SE TOMA LA CUENTA CLASE PARA UNIDAD FUNCIONAL TODAS INTERFAZ INVENTARIOS**********************************
			if(cuenta.getCodigo().equals(""))
			{
				pst = new PreparedStatementDecorator(con.prepareStatement(consultarCuentaClaseInventarioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1,codigoClase);
				pst.setString(2,ConstantesBD.codigoNuncaValido+""); //TODAS LA UNIDADES FUNCIONALES
				pst.setInt(3,codigoInstitucion);
				rs = new ResultSetDecorator(pst.executeQuery());
				
				if(rs.next())
				{
					cuenta.setCodigo(rs.getString("codigo"));
					cuenta.setCuentaContable(rs.getString("cuenta_contable"));
					cuenta.setActivo(UtilidadTexto.getBoolean(rs.getString("activo")));
					cuenta.setManejaTerceros(UtilidadTexto.getBoolean(rs.getString("manejo_terceros")));
					cuenta.setManejoCentrosCosto(UtilidadTexto.getBoolean(rs.getString("manejo_centros_costo")));
					cuenta.setManejoBaseGravable(UtilidadTexto.getBoolean(rs.getString("manejo_base_gravable")));
					cuenta.setNaturalezaCuenta(rs.getString("naturaleza_cuenta"));
					cuenta.setCodigoInstitucion(rs.getInt("institucion"));
					cuenta.setAnioVigencia(rs.getString("anio_vigencia"));
				}
				pst.close();
				rs.close();
			}
			//***************************************************************************************************************************
			
		}
		catch(SQLException e)
		{
			logger.error("Error consultarCuentaContableInterfazInventarios: "+e);
		}
		return cuenta;
	}
	
	/**
	 * Método que consulta la cuenta contable de la parametriacion de subgrupo/grupo/clase
	 * @param con
	 * @param codigoArticulo
	 * @param cuentaCosto
	 * @param cuentaInventario
	 * @return
	 */
	public static DtoCuentaContable consultarCuentaContableSubgrupoGrupoClase(Connection con,int codigoArticulo,boolean cuentaCosto,boolean cuentaInventario)
	{
		DtoCuentaContable cuenta = new DtoCuentaContable();
		try
		{
			//*******************TOMAR LA CUENTA FILTRO*****************************************
			String cuentaFiltro = "cuenta_inventario";
			if(cuentaCosto)
			{
				cuentaFiltro = "cuenta_costo";
			}
			else if(cuentaInventario)
			{
				cuentaFiltro = "cuenta_inventario";
			}
			//************************************************************************************
			
			///************SE TOMA EL SUBGRUPO,GRUPO Y CLASE DEL ARTICULO*****************************************
			int codigoSubgrupo = ConstantesBD.codigoNuncaValido;
			int codigoGrupo = ConstantesBD.codigoNuncaValido;
			int codigoClase = ConstantesBD.codigoNuncaValido;
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consultarSubgrupoGrupoClaseArticuloStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoArticulo);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				codigoSubgrupo = rs.getInt("subgrupo");
				codigoGrupo = rs.getInt("grupo");
				codigoClase = rs.getInt("clase");
			}
			pst.close();
			rs.close();
			//***************************************************************************************************
			
			//****************CONSULTA CUENTA SUBGRUPO************************************************************
			String consulta = consultarCuentaSubgrupoStr.replace("#", cuentaFiltro);
			pst = new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoSubgrupo);
			pst.setInt(2,codigoGrupo);
			pst.setInt(3,codigoClase);
			rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				cuenta.setCodigo(rs.getString("codigo"));
				cuenta.setCuentaContable(rs.getString("cuenta_contable"));
				cuenta.setActivo(UtilidadTexto.getBoolean(rs.getString("activo")));
				cuenta.setManejaTerceros(UtilidadTexto.getBoolean(rs.getString("manejo_terceros")));
				cuenta.setManejoCentrosCosto(UtilidadTexto.getBoolean(rs.getString("manejo_centros_costo")));
				cuenta.setManejoBaseGravable(UtilidadTexto.getBoolean(rs.getString("manejo_base_gravable")));
				cuenta.setNaturalezaCuenta(rs.getString("naturaleza_cuenta"));
				cuenta.setCodigoInstitucion(rs.getInt("institucion"));
				cuenta.setAnioVigencia(rs.getString("anio_vigencia"));
			}
			pst.close();
			rs.close();
			//*****************************************************************************************************
			//******************CONSULTA CUENTA GRUPO****************************************************************
			if(cuenta.getCodigo().equals(""))
			{
				consulta = consultarCuentaGrupoStr.replace("#", cuentaFiltro);
				pst = new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1,codigoGrupo);
				pst.setInt(2,codigoClase);
				rs = new ResultSetDecorator(pst.executeQuery());
				if(rs.next())
				{
					cuenta.setCodigo(rs.getString("codigo"));
					cuenta.setCuentaContable(rs.getString("cuenta_contable"));
					cuenta.setActivo(UtilidadTexto.getBoolean(rs.getString("activo")));
					cuenta.setManejaTerceros(UtilidadTexto.getBoolean(rs.getString("manejo_terceros")));
					cuenta.setManejoCentrosCosto(UtilidadTexto.getBoolean(rs.getString("manejo_centros_costo")));
					cuenta.setManejoBaseGravable(UtilidadTexto.getBoolean(rs.getString("manejo_base_gravable")));
					cuenta.setNaturalezaCuenta(rs.getString("naturaleza_cuenta"));
					cuenta.setCodigoInstitucion(rs.getInt("institucion"));
					cuenta.setAnioVigencia(rs.getString("anio_vigencia"));
				}
				pst.close();
				rs.close();
			}
			//*******************************************************************************************************
			//******************CONSULTA CUENTA CLASE *****************************************************************
			if(cuenta.getCodigo().equals(""))
			{
				consulta = consultarCuentaClaseStr.replace("#", cuentaFiltro);
				pst = new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1,codigoClase);
				rs = new ResultSetDecorator(pst.executeQuery());
				if(rs.next())
				{
					cuenta.setCodigo(rs.getString("codigo"));
					cuenta.setCuentaContable(rs.getString("cuenta_contable"));
					cuenta.setActivo(UtilidadTexto.getBoolean(rs.getString("activo")));
					cuenta.setManejaTerceros(UtilidadTexto.getBoolean(rs.getString("manejo_terceros")));
					cuenta.setManejoCentrosCosto(UtilidadTexto.getBoolean(rs.getString("manejo_centros_costo")));
					cuenta.setManejoBaseGravable(UtilidadTexto.getBoolean(rs.getString("manejo_base_gravable")));
					cuenta.setNaturalezaCuenta(rs.getString("naturaleza_cuenta"));
					cuenta.setCodigoInstitucion(rs.getInt("institucion"));
					cuenta.setAnioVigencia(rs.getString("anio_vigencia"));
				}
				pst.close();
				rs.close();
			}
			//***********************************************************************************************************
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarCuentaContableSubgrupoGrupoClase: "+e);
		}
		return cuenta;
	}
	
	/**
	 * Método para consultar los datos de la cuenta contable
	 * @param con
	 * @param cuentaContable
	 * @return
	 */
	public static DtoCuentaContable consultarDatosCuentaContable(Connection con,DtoCuentaContable cuentaContable)
	{
		try
		{
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consultarDatosCuentaContableStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Utilidades.convertirAEntero(cuentaContable.getCodigo()));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				cuentaContable.setCuentaContable(rs.getString("cuenta_contable"));
				cuentaContable.setDescripcion(rs.getString("descripcion"));
				cuentaContable.setActivo(UtilidadTexto.getBoolean(rs.getString("activo")));
				cuentaContable.setManejaTerceros(UtilidadTexto.getBoolean(rs.getString("manejo_terceros")));
				cuentaContable.setManejoCentrosCosto(UtilidadTexto.getBoolean(rs.getString("manejo_centros_costo")));
				cuentaContable.setManejoBaseGravable(UtilidadTexto.getBoolean(rs.getString("manejo_base_gravable")));
				cuentaContable.setNaturalezaCuenta(rs.getString("naturaleza_cuenta"));
				cuentaContable.setCodigoInstitucion(rs.getInt("institucion"));
				cuentaContable.setAnioVigencia(rs.getString("anio_vigencia"));
			}
			rs.close();
			pst.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarDatosCuentaContable: "+e);
		}
		return cuentaContable;
	}
	
	
	/**
	 * Método para consultar la cuenta contable de cuenta interfaz inventario ingreso
	 * @param con
	 * @param codigoCentroCosto
	 * @param codigoArticulo
	 * @param cuentaIngreso
	 * @param cuentaIngresoVigAnterior
	 * @return
	 */
	public static DtoCuentaContable consultarCuentaContableInterfazInvIngreso(Connection con,int codigoCentroCosto,int codigoArticulo,boolean cuentaIngreso,boolean cuentaIngresoVigAnterior)
	{
		DtoCuentaContable cuentaContable = new DtoCuentaContable();
		try
		{
			String cuentaFiltro = "";
			int i=0;
			if(cuentaIngreso)
			{
				cuentaFiltro = "cuenta_ingreso";
			}
			else if(cuentaIngresoVigAnterior)
			{
				cuentaFiltro = "cuenta_ingreso_vig_anterior";
			}
			
			///************SE TOMA EL SUBGRUPO,GRUPO Y CLASE DEL ARTICULO*****************************************
			int codigoSubgrupo = ConstantesBD.codigoNuncaValido;
			int codigoGrupo = ConstantesBD.codigoNuncaValido;
			int codigoClase = ConstantesBD.codigoNuncaValido;
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consultarSubgrupoGrupoClaseArticuloStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoArticulo);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				codigoSubgrupo = rs.getInt("codigo_subgrupo");
				codigoGrupo = rs.getInt("grupo");
				codigoClase = rs.getInt("clase");
			}
			pst.close();
			rs.close();
			//***************************************************************************************************
			
			while(cuentaContable.getCodigo().equals("")&&i<=1)
			{
				if(i==1)
				{
					codigoCentroCosto = ConstantesBD.codigoCentroCostoTodos;
				}
			
				//*************** CONSULTAR CUENTA ARTICULO X CENTRO COSTO ***********************************************
				pst = new PreparedStatementDecorator(con.prepareStatement(consultarCuentaArticuloInvIngresoStr.replace("#", cuentaFiltro), ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setInt(1,codigoCentroCosto);
				pst.setInt(2,codigoArticulo);
				rs = new ResultSetDecorator(pst.executeQuery());
				if(rs.next())
				{
					cuentaContable.setCodigo(rs.getString("codigo"));
					cuentaContable.setCuentaContable(rs.getString("cuenta_contable"));
					cuentaContable.setDescripcion(rs.getString("descripcion"));
					cuentaContable.setActivo(UtilidadTexto.getBoolean(rs.getString("activo")));
					cuentaContable.setManejaTerceros(UtilidadTexto.getBoolean(rs.getString("manejo_terceros")));
					cuentaContable.setManejoCentrosCosto(UtilidadTexto.getBoolean(rs.getString("manejo_centros_costo")));
					cuentaContable.setManejoBaseGravable(UtilidadTexto.getBoolean(rs.getString("manejo_base_gravable")));
					cuentaContable.setNaturalezaCuenta(rs.getString("naturaleza_cuenta"));
					cuentaContable.setCodigoInstitucion(rs.getInt("institucion"));
					cuentaContable.setAnioVigencia(rs.getString("anio_vigencia"));
				}
				pst.close();
				rs.close();
				//***********************************************************************************************************
				//***************** CONSULTAR CUENTA SUBGRUPO X CENTRO COSTO***************************************************+
				if(cuentaContable.getCuentaContable().equals(""))
				{
					pst = new PreparedStatementDecorator(con.prepareStatement(consultarCuentaSubgrupoInvIngresoStr.replace("#", cuentaFiltro),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setInt(1,codigoCentroCosto);
					pst.setInt(2,codigoSubgrupo);
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
					{
						cuentaContable.setCodigo(rs.getString("codigo"));
						cuentaContable.setCuentaContable(rs.getString("cuenta_contable"));
						cuentaContable.setDescripcion(rs.getString("descripcion"));
						cuentaContable.setActivo(UtilidadTexto.getBoolean(rs.getString("activo")));
						cuentaContable.setManejaTerceros(UtilidadTexto.getBoolean(rs.getString("manejo_terceros")));
						cuentaContable.setManejoCentrosCosto(UtilidadTexto.getBoolean(rs.getString("manejo_centros_costo")));
						cuentaContable.setManejoBaseGravable(UtilidadTexto.getBoolean(rs.getString("manejo_base_gravable")));
						cuentaContable.setNaturalezaCuenta(rs.getString("naturaleza_cuenta"));
						cuentaContable.setCodigoInstitucion(rs.getInt("institucion"));
						cuentaContable.setAnioVigencia(rs.getString("anio_vigencia"));
					}
					pst.close();
					rs.close();
				}
				//*****************************************************************************************************************
				//******************CONSULTAR CUENTA GRUPO X CENTRO COSTO ***********************************************************
				if(cuentaContable.getCuentaContable().equals(""))
				{
					pst = new PreparedStatementDecorator(con.prepareStatement(consultarCuentaGrupoInvIngresoStr.replace("#", cuentaFiltro),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setInt(1,codigoCentroCosto);
					pst.setInt(2,codigoGrupo);
					pst.setInt(3,codigoClase);
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
					{
						cuentaContable.setCodigo(rs.getString("codigo"));
						cuentaContable.setCuentaContable(rs.getString("cuenta_contable"));
						cuentaContable.setDescripcion(rs.getString("descripcion"));
						cuentaContable.setActivo(UtilidadTexto.getBoolean(rs.getString("activo")));
						cuentaContable.setManejaTerceros(UtilidadTexto.getBoolean(rs.getString("manejo_terceros")));
						cuentaContable.setManejoCentrosCosto(UtilidadTexto.getBoolean(rs.getString("manejo_centros_costo")));
						cuentaContable.setManejoBaseGravable(UtilidadTexto.getBoolean(rs.getString("manejo_base_gravable")));
						cuentaContable.setNaturalezaCuenta(rs.getString("naturaleza_cuenta"));
						cuentaContable.setCodigoInstitucion(rs.getInt("institucion"));
						cuentaContable.setAnioVigencia(rs.getString("anio_vigencia"));
					}
					pst.close();
					rs.close();
				}
				//*********************************************************************************************************************
				//*******************CONSULTAR CUENTA CLASE X CENTRO COSTO**************************************************************
				if(cuentaContable.getCuentaContable().equals(""))
				{
					pst = new PreparedStatementDecorator(con.prepareStatement(consultarCuentaClaseInvIngresoStr.replace("#", cuentaFiltro),ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setInt(1,codigoCentroCosto);
					pst.setInt(2,codigoClase);
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
					{
						cuentaContable.setCodigo(rs.getString("codigo"));
						cuentaContable.setCuentaContable(rs.getString("cuenta_contable"));
						cuentaContable.setDescripcion(rs.getString("descripcion"));
						cuentaContable.setActivo(UtilidadTexto.getBoolean(rs.getString("activo")));
						cuentaContable.setManejaTerceros(UtilidadTexto.getBoolean(rs.getString("manejo_terceros")));
						cuentaContable.setManejoCentrosCosto(UtilidadTexto.getBoolean(rs.getString("manejo_centros_costo")));
						cuentaContable.setManejoBaseGravable(UtilidadTexto.getBoolean(rs.getString("manejo_base_gravable")));
						cuentaContable.setNaturalezaCuenta(rs.getString("naturaleza_cuenta"));
						cuentaContable.setCodigoInstitucion(rs.getInt("institucion"));
						cuentaContable.setAnioVigencia(rs.getString("anio_vigencia"));
					}
					pst.close();
					rs.close();
				}
				//************************************************************************************************************************
				
				i++;
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarCuentaContableInterfazInvIngreso: "+e);
		}
		return cuentaContable;
	}
	
	/**
	 * Método implementado para verificar si una factura tiene un mismo grupo o diferentes
	 * @param con
	 * @param numeroDocumento
	 * @return
	 */
	public static ResultadoBoolean validacionMismoGrupoServicioFactura(Connection con,String numeroDocumento,boolean honorarios)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true,"");
		try
		{
			int contador = 0;
			PreparedStatementDecorator pst = null;
			String consulta  = "";
			
			if(honorarios)
			{
			
				consulta= "select * from "+ 
					"( "+ 
						"SELECT "+
						"s.grupo_servicio as grupo "+ 
						"FROM facturas f "+
						"INNER JOIN det_factura_solicitud dfs ON(dfs.factura = f.codigo) "+
						"INNER JOIN servicios s ON(s.codigo = dfs.servicio) "+
						"WHERE f.consecutivo_factura = ? and dfs.tipo_solicitud not in ("+ConstantesBD.codigoTipoSolicitudCirugia+","+ConstantesBD.codigoTipoSolicitudPaquetes+") "+ 
						"UNION "+ 
						"SELECT "+ 
						"s.grupo_servicio as grupo "+ 
						"FROM facturas f "+
						"INNER JOIN det_factura_solicitud dfs ON(dfs.factura = f.codigo) "+ 
						"INNER JOIN asocios_det_factura adf ON(adf.codigo = dfs.codigo) "+ 
						"INNER JOIN servicios s ON(s.codigo = adf.servicio_asocio) "+
						"WHERE f.consecutivo_factura = ? "+  
						"UNION "+ 
						"SELECT "+ 
						"s.grupo_servicio as grupo "+ 
						"FROM facturas f "+
						"INNER JOIN det_factura_solicitud dfs ON(dfs.factura = f.codigo) "+ 
						"INNER JOIN paquetizacion_det_factura pdf ON(pdf.codigo_det_fact = dfs.codigo) "+ 
						"INNER JOIN servicios s ON(s.codigo = pdf.servicio) "+
						"WHERE f.consecutivo_factura = ? "+  
					") t "+  
					"group by t.grupo";
				pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setLong(1,Long.parseLong(numeroDocumento));
				pst.setLong(2,Long.parseLong(numeroDocumento));
				pst.setLong(3,Long.parseLong(numeroDocumento));
			}
			else
			{
				consulta = "SELECT "+
					"s.grupo_servicio as grupo "+ 
					"FROM facturas f "+
					"INNER JOIN det_factura_solicitud dfs ON(dfs.factura = f.codigo) "+
					"INNER JOIN servicios s ON(s.codigo = dfs.servicio) "+
					"WHERE f.consecutivo_factura = ? "+ 
					"group by s.grupo_servicio" ;
				pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setLong(1,Long.parseLong(numeroDocumento));
			}
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				resultado.setDescripcion(rs.getString("grupo"));
				contador++;
			}
			pst.close();
			rs.close();
			
			//Si había mas de un grupo, entonces hay distintos grupos para la factura
			if(contador>1)
			{
				resultado.setResultado(false);
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en validacionMismoGrupoFactura: "+e);
		}
		return resultado;
	}
	
	/**
	 * Método para obtener el mismo grupo de servicio de la autorzacion de servicios
	 * @param con
	 * @param numeroDocumento
	 * @return
	 */
	public static ResultadoBoolean validacionMismoGrupoServicioAutorizacionEntidadSub(Connection con,String numeroDocumento)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true,"");
		try
		{
			int contador = 0;
			String consulta = "SELECT " +
				"s.grupo_servicio as grupo " +
				"from autorizaciones_entidades_sub ae " + 
				
				//PermitirAutorizarDiferenteDeSolicitudes
				"inner join manejopaciente.autorizaciones_ent_sub_servi aas ON(aas.autorizacion_ent_sub = ae.consecutivo) " +
				
				"inner join servicios s ON(s.codigo = aas.servicio) " +
				"WHERE " +
				"ae.consecutivo_autorizacion = ?";
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setString(1,numeroDocumento);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				resultado.setDescripcion(rs.getString("grupo"));
				contador++;
			}
			pst.close();
			rs.close();
			
			///Si había mas de un grupo, entonces hay distintos grupos para la autorizacion
			if(contador>1)
			{
				resultado.setResultado(false);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en validacionMismoGrupoServicioAutorizacionEntidadSub: "+e);
		}
		return resultado;
	}
	
	/**
	 * Método para validar si los medicamentos o despachos pertenecen a la misma/diferente clase
	 * @param con
	 * @param numeroDocumento
	 * @param codigoTipoDocumento
	 * @return
	 */
	public static ResultadoBoolean validacionMismaClaseDespachoMedicamentos(Connection con,String numeroDocumento,int codigoTipoDocumento)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true,"");
		try
		{
			PreparedStatementDecorator pst = null;
			ResultSetDecorator rs = null;
			String consulta = "";
			int contador = 0;
			
			switch(codigoTipoDocumento)
			{
				case ConstantesBD.codigoTipoDocInteDespachoMed:
				case ConstantesBD.codigoTipoDocInteCargosDirectosArt:
				case ConstantesBD.codigoTipoDocInteAnulaCargosDirectosArticulo:
					consulta = "SELECT "+ 
						"si.clase as codigo_clase "+ 
						"FROM solicitudes s "+ 
						"INNER JOIN despacho d ON(d.numero_solicitud = s.numero_solicitud) "+ 
						"INNER JOIN detalle_despachos dd ON(dd.despacho = d.orden) "+ 
						"INNER JOIN articulo a ON(a.codigo = dd.articulo) "+ 
						"INNER JOIN subgrupo_inventario si ON(si.codigo = a.subgrupo) "+
						"WHERE "+ 
						"s.consecutivo_ordenes_medicas = ? "+ 
						"group by si.clase";
					
				break;
				case ConstantesBD.codigoTipoDocInteDespaPedidoInsumo:
				case ConstantesBD.codigoTipoDocInteDespachoPedidoQx:
					consulta = "SELECT "+ 
						"si.clase as codigo_clase "+ 
						"FROM despacho_pedido dd "+  
						"INNER JOIN detalle_despacho_pedido ddp ON(ddp.pedido = dd.pedido) "+
						"INNER JOIN articulo a ON(a.codigo = ddp.articulo) "+ 
						"INNER JOIN subgrupo_inventario si ON(si.codigo = a.subgrupo) "+ 
						"WHERE "+ 
						" dd.consecutivo = ? "+ 
						"group by si.clase";	
					
				break;
				case ConstantesBD.codigoTipoDocInteDevolPedidoInsumo:
				case ConstantesBD.codigoTipoDocInteDevolucionPedidoQx:
					consulta = "SELECT "+ 
						"si.clase as codigo_clase "+ 
						"FROM detalle_devol_pedido ddp "+ 
						"INNER JOIN articulo a ON(a.codigo = ddp.articulo) "+ 
						"INNER JOIN subgrupo_inventario si ON(si.codigo = a.subgrupo) "+ 
						"WHERE "+ 
						"ddp.devolucion = ? "+ 
						"group by si.clase";
					
				break;
				case ConstantesBD.codigoTipoDocInteDevolucionMedi:
					//Primero se obtiene la solicitud de ea devolucion
					consulta = " select numero_solicitud from detalle_devol_med WHERE devolucion = ?";
					pst = new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setLong(1, Long.parseLong(numeroDocumento));
					rs = new ResultSetDecorator(pst.executeQuery());
					if(rs.next())
					{
						numeroDocumento = rs.getString("numero_solicitud");
					}
					pst.close();
					rs.close();
					consulta = "SELECT "+ 
						"si.clase as codigo_clase "+ 
						"FROM solicitudes s "+ 
						"INNER JOIN despacho d ON(d.numero_solicitud = s.numero_solicitud) "+ 
						"INNER JOIN detalle_despachos dd ON(dd.despacho = d.orden) "+ 
						"INNER JOIN articulo a ON(a.codigo = dd.articulo) "+ 
						"INNER JOIN subgrupo_inventario si ON(si.codigo = a.subgrupo) "+
						"WHERE "+ 
						"s.numero_solicitud = ? "+ 
						"group by si.clase";
				break;
			}
			
			
			pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setLong(1, Long.parseLong(numeroDocumento));
			rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				resultado.setDescripcion(rs.getString("codigo_clase"));
				contador++;
			}
			pst.close();
			rs.close();
			
			//Si había mas de una clase, entonces hay distintas clases para el documento
			if(contador>1)
			{
				resultado.setResultado(false);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en validacionMismaClaseDespachoMedicamentos: "+e);
		}
		return resultado;
	}
	
	/**
	 * Método para validar misma clase de la factura
	 * @param con
	 * @param numeroDocumento
	 * @return
	 */
	public static ResultadoBoolean validacionMismaClaseFactura(Connection con,String numeroDocumento)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true,"");
		try
		{
			int contador = 0;
			String consulta = "SELECT "+
				"si.clase as codigo_clase "+
				"FROM facturas f "+
				"INNER JOIN det_factura_solicitud dfs ON(dfs.factura = f.codigo) "+
				"INNER JOIN articulo a ON(a.codigo = dfs.articulo) "+
				"INNER JOIN subgrupo_inventario si ON(si.codigo = a.subgrupo) "+
				"WHERE "+
				"f.consecutivo_factura = ? "+
				"group by si.clase";
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setLong(1,Long.parseLong(numeroDocumento));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			while(rs.next())
			{
				resultado.setDescripcion(rs.getString("codigo_clase"));
				contador++;
			}
			pst.close();
			rs.close();
			
			//Si había mas de una clase, entonces hay distintas clases para el documento
			if(contador>1)
			{
				resultado.setResultado(false);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en validacionMismaClaseFactura "+e);
		}
		return resultado;
	}
	
	/**
	 * Método para validar si la factura tiene iguales conceptos o distintos
	 * @param con
	 * @param numeroDocumento
	 * @return
	 */
	public static ResultadoBoolean validacionMismoConceptoFacturaVaria(Connection con,String numeroDocumento)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true,"");
		try
		{
			int contador = 0;
			String consulta = "SELECT fv.concepto as codigo_concepto FROM facturas_varias fv WHERE fv.consecutivo = ?";
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setLong(1,Long.parseLong(numeroDocumento));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				contador++;
				resultado.setDescripcion(rs.getString("codigo_concepto"));
			}
			pst.close();
			rs.close();
			
			//Si había mas de un concepto, entonces hay distintos conceptos para el documento
			if(contador>1)
			{
				resultado.setResultado(false);
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en validacionMismoConceptoFacturaVaria: "+e);
		}
		return resultado;
	}
	
	/**
	 * Método implementado para consultar los tipos de retencion que aplican por clase, grupo y conceptode factura varia
	 * @param con
	 * @param codigoInstitucion
	 * @param codigoClase
	 * @param codigoGrupo
	 * @param codigoConcepto
	 * @return
	 */
	public static ArrayList<DtoTiposRetencion> consultarTiposRetencionXClaseGrupoConceptoFV(Connection con,int codigoInstitucion,int codigoClase,int codigoGrupo,int codigoConcepto)
	{
		ArrayList<DtoTiposRetencion> tiposRetencion = new ArrayList<DtoTiposRetencion>();
		try
		{
			String consulta = "";
			PreparedStatementDecorator pst = null;
			ResultSetDecorator rs = null;
			
			//********************BUSDQUEDA DE TIPOS DE RETENCION POR CLASE************************************
			if(codigoClase>0)
			{
				consulta = "SELECT "+
					"tr.consecutivo as consecutivo, "+
					"tr.codigo as codigo, "+
					"tr.descripcion as descripcion, "+
					"tr.sigla as sigla, "+
					"tr.codigo_interfaz as codigo_interfaz "+ 
					"FROM administracion.tipos_retencion_clase_inv trc "+ 
					"INNER JOIN administracion.tipos_retencion tr ON(tr.consecutivo = trc.tipo_retencion) "+ 
					"WHERE trc.clase = ? and trc.activo = '"+ConstantesBD.acronimoSi+"' and tr.institucion = ? and tr.activo = '"+ConstantesBD.acronimoSi+"'";
				pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setInt(1,codigoClase);
				pst.setInt(2,codigoInstitucion);
				
			}
			//*********************************************************************************************
			//******************BUSQUEDA DE TIPOS DE RETENCION POR GRUPO*************************************
			if(codigoGrupo>0)
			{
				consulta = "SELECT "+
					"tr.consecutivo as consecutivo, "+
					"tr.codigo as codigo, "+
					"tr.descripcion as descripcion, "+
					"tr.sigla as sigla, "+
					"tr.codigo_interfaz as codigo_interfaz "+ 
					"FROM administracion.tipos_retencion_grupo_ser trg "+ 
					"INNER JOIN administracion.tipos_retencion tr ON(tr.consecutivo = trg.tipo_retencion) "+ 
					"WHERE trg.grupo = ? and trg.activo = '"+ConstantesBD.acronimoSi+"' and tr.institucion = ? and tr.activo = '"+ConstantesBD.acronimoSi+"'";
				pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setInt(1,codigoGrupo);
				pst.setInt(2,codigoInstitucion);
			}
			//************************************************************************************************
			//***************BUSQUEDA DE TIPOS DE RETENCION POR CONCEPTO***************************************
			if(codigoConcepto>0)
			{
				consulta = "SELECT "+
					"tr.consecutivo as consecutivo, "+
					"tr.codigo as codigo, "+
					"tr.descripcion as descripcion, "+
					"tr.sigla as sigla, "+
					"tr.codigo_interfaz as codigo_interfaz "+ 
					"FROM administracion.tipos_retencion_conc_fv trc "+ 
					"INNER JOIN administracion.tipos_retencion tr ON(tr.consecutivo = trc.tipo_retencion) "+ 
					"WHERE trc.concepto = ? and trc.activo = '"+ConstantesBD.acronimoSi+"' and tr.institucion = ? and tr.activo = '"+ConstantesBD.acronimoSi+"'"; 
				pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setInt(1,codigoConcepto);
				pst.setInt(2,codigoInstitucion);
			}
			//*************************************************************************************************
			
			
			if(codigoClase>0 || codigoGrupo > 0 || codigoConcepto>0)
			{
				rs = new ResultSetDecorator(pst.executeQuery());
				while(rs.next())
				{
					DtoTiposRetencion tipoRetencion = new DtoTiposRetencion();
					tipoRetencion.setConsecutivo(rs.getInt("consecutivo"));
					tipoRetencion.setCodigo(rs.getString("codigo"));
					tipoRetencion.setDescripcion(rs.getString("descripcion"));
					tipoRetencion.setSigla(rs.getString("sigla"));
					tipoRetencion.setCodigoInterfaz(rs.getString("codigo_interfaz"));
					tiposRetencion.add(tipoRetencion);
				}
				pst.close();
				rs.close();
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarTiposRetencionXClaseGrupoConceptoFV: "+e);
		}
		return tiposRetencion;
	}
	
	/**
	 * Método para conultar las conceptos de retencion de la parametrizacion de interfaz sistema 1E
	 * @param con
	 * @param claseDocumento
	 * @param seccion
	 * @param codigoInstitucion
	 * @return
	 */
	public static ArrayList<DtoConceptosRetencion> consultarConceptosRetencionParamS1E(Connection con,String claseDocumento,String seccion,int codigoInstitucion)
	{
		ArrayList<DtoConceptosRetencion> conceptos = new ArrayList<DtoConceptosRetencion>();
		try
		{
			String consulta = "SELECT "+
				"coalesce(cp.concepto,"+ConstantesBD.codigoNuncaValido+") as consecutivo_concepto "+ 
				"FROM interfaz.conceptos_param_1e cp "+ 
				"INNER JOIN interfaz.param_generales_1e p ON(p.consecutivo = cp.param_generales_1e) "+ 
				"WHERE cp.clase_documento = ? and cp.seccion = ? and cp.activo = '"+ConstantesBD.acronimoSi+"' and p.institucion = ?";
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,claseDocumento);
			pst.setString(2,seccion);
			pst.setInt(3, codigoInstitucion);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				DtoConceptosRetencion concepto = new DtoConceptosRetencion();
				concepto.setConsecutivo(rs.getString("consecutivo_concepto"));
				conceptos.add(concepto);
			}
			rs.close();
			pst.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarConceptosRetencionParamS1E: "+e);
		}
		return conceptos;
	}
	
	
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
	public static ArrayList<DtoConceptoRetencionTercero> consultarConceptosRetencionXTercero(Connection con,ArrayList<DtoTiposRetencion> tiposRetencion,ArrayList<DtoConceptosRetencion> conceptos,String nitTercero,boolean integral, String fechaDocumento)
	{
		ArrayList<DtoConceptoRetencionTercero> conceptosTercero = new ArrayList<DtoConceptoRetencionTercero>();
		try
		{
			String consulta = "SELECT "+
				"cr.consecutivo as consecutivo_concepto, "+
				"cr.tipo_retencion as tipo_retencion, "+
				"cr.codigo_concepto as codigo_concepto, "+
				"cr.descripcion_concepto as descripcion, "+
				"coalesce(cr.codigo_interfaz,'') as codigo_interfaz, "+
				"coalesce(cr.cuenta_retencion,"+ConstantesBD.codigoNuncaValido+") as cuenta_retencion, "+ 
				"coalesce(cr.cuenta_db_autoretencion,"+ConstantesBD.codigoNuncaValido+") as cuenta_db_auto, "+
				"coalesce(cr.cuenta_cr_autoretencion,"+ConstantesBD.codigoNuncaValido+") as cuenta_cr_auto, " +
				"crt.consecutivo as consecutivo_con_tercero, "+  
				"crt.tipo_aplicacion as codigo_tipo_aplicacion, "+ 
				"tar.nombre as nombre_tipo_aplicacion, "+ 
				"crt.ind_agente_retenedor as ind_agente "+ 
				"FROM facturacion.terceros t "+ 
				"INNER JOIN administracion.conceptos_retencion_tercero crt ON(crt.tercero = t.codigo) "+ 
				"INNER JOIN administracion.conceptos_retencion cr ON(cr.consecutivo = crt.concepto_retencion) "+ 
				"INNER JOIN facturacion.tipos_aplicacion_ret tar ON(tar.codigo = crt.tipo_aplicacion) "+
				"WHERE t.numero_identificacion = ? and crt.activo = '"+ConstantesBD.acronimoSi+"' and cr.activo = '"+ConstantesBD.acronimoSi+"' ";
			PreparedStatementDecorator pst = null;
			ResultSetDecorator rs = null;
			
			if(!integral)
			{
				//NO INTEGRAL: los tipos de retencion deben coincidir con los tipos de retencion de los conceptos de retencion del tercero (al menos 1)
				String codigosTipoRetencion = "";
				for(DtoTiposRetencion tipoRet:tiposRetencion)
				{
					codigosTipoRetencion += (codigosTipoRetencion.equals("")?"":",") + tipoRet.getConsecutivo();
				}
				
				consulta += " and cr.tipo_retencion in ("+codigosTipoRetencion+") ";
				//Se toman solo los conceptos que sean no integrales
				consulta += " and cr.consecutivo in (" +
					"SELECT dv.concepto_retencion " +
					"from administracion.det_vig_con_retencion dv " +
					"inner join administracion.det_conceptos_retencion dcr on(dcr.consecutivo = dv.det_concepto_retencion) " +
					"WHERE " +
					"dv.concepto_retencion = cr.consecutivo and "+ 
					"dv.activo = '"+ConstantesBD.acronimoSi+"' and "+ 
					"dv.indicativo_integral = '"+ConstantesBD.acronimoNo+"' and "+ 
					"dcr.fecha_vigencia_inicial <=  ?" +
					") ";
				pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setString(1,nitTercero);
				pst.setDate(2,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaDocumento)));
				logger.info("consulta tercero: "+consulta.replace("?", "'"+nitTercero+"'"));
				rs = new ResultSetDecorator(pst.executeQuery());
				while(rs.next())
				{
					DtoConceptoRetencionTercero conceptoTercero = new DtoConceptoRetencionTercero();
					conceptoTercero.setConsecutivo(rs.getString("consecutivo_concepto"));
					conceptoTercero.getTipoRet().setConsecutivo(rs.getInt("tipo_retencion"));
					conceptoTercero.setCodigoConcepto(rs.getString("codigo_concepto"));
					conceptoTercero.setDescripcion(rs.getString("descripcion"));
					conceptoTercero.setCodigoInterfaz(rs.getString("codigo_interfaz"));
					conceptoTercero.getCuentaRetencion().setCodigo(rs.getString("cuenta_retencion"));
					conceptoTercero.getCuentaDBAutoretencion().setCodigo(rs.getString("cuenta_db_auto"));
					conceptoTercero.getCuentaCRAutoretencion().setCodigo(rs.getString("cuenta_cr_auto"));
					conceptoTercero.setConsecutivoConceptoTercero(rs.getString("consecutivo_con_tercero"));
					conceptoTercero.getTipoAplicacion().setCodigo(rs.getInt("codigo_tipo_aplicacion"));
					conceptoTercero.getTipoAplicacion().setNombre(rs.getString("nombre_tipo_aplicacion"));
					conceptoTercero.setIndicativoAgenteRetenedor(UtilidadTexto.getBoolean(rs.getString("ind_agente")));
					conceptosTercero.add(conceptoTercero);
				}
				pst.close();
				rs.close();
			}
			else
			{
				//INTEGRAL: los conceptos base deben coincidor con los conceptos de retencion del tercero (TODOS)
				String codigosConceptos = "";
				for(DtoConceptosRetencion concepto:conceptos)
				{
					codigosConceptos += (codigosConceptos.equals("")?"":",") + concepto.getConsecutivo();
				}
				consulta += " and crt.concepto_retencion in ("+codigosConceptos+") ";
				///Se toman solo los conceptos que sean no integrales
				consulta += " and cr.consecutivo in (" +
					"SELECT dv.concepto_retencion " +
					"from administracion.det_vig_con_retencion dv " +
					"inner join administracion.det_conceptos_retencion dcr on(dcr.consecutivo = dv.det_concepto_retencion) " +
					"WHERE " +
					"dv.concepto_retencion = cr.consecutivo and "+ 
					"dv.activo = '"+ConstantesBD.acronimoSi+"' and "+ 
					"dv.indicativo_integral = '"+ConstantesBD.acronimoSi+"' and "+ 
					"dcr.fecha_vigencia_inicial <=  ?" +
					") ";
				pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setString(1,nitTercero);
				pst.setDate(2,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaDocumento)));
				rs = new ResultSetDecorator(pst.executeQuery());
				while(rs.next())
				{
					DtoConceptoRetencionTercero conceptoTercero = new DtoConceptoRetencionTercero();
					conceptoTercero.setConsecutivo(rs.getString("consecutivo_concepto"));
					conceptoTercero.getTipoRet().setConsecutivo(rs.getInt("tipo_retencion"));
					conceptoTercero.setCodigoConcepto(rs.getString("codigo_concepto"));
					conceptoTercero.setDescripcion(rs.getString("descripcion"));
					conceptoTercero.setCodigoInterfaz(rs.getString("codigo_interfaz"));
					conceptoTercero.getCuentaRetencion().setCodigo(rs.getString("cuenta_retencion"));
					conceptoTercero.getCuentaDBAutoretencion().setCodigo(rs.getString("cuenta_db_auto"));
					conceptoTercero.getCuentaCRAutoretencion().setCodigo(rs.getString("cuenta_cr_auto"));
					conceptoTercero.setConsecutivoConceptoTercero(rs.getString("consecutivo_con_tercero"));
					conceptoTercero.getTipoAplicacion().setCodigo(rs.getInt("codigo_tipo_aplicacion"));
					conceptoTercero.getTipoAplicacion().setNombre(rs.getString("nombre_tipo_aplicacion"));
					conceptoTercero.setIndicativoAgenteRetenedor(UtilidadTexto.getBoolean(rs.getString("ind_agente")));
					conceptosTercero.add(conceptoTercero);
				}
				pst.close();
				rs.close();
				
				//Si no corresponde con los conceptos buscados quiere decir que no se encontró nada
				/*
				 * Esta validación se comenta x la tarea 150551
				 * 
				 * if(conceptos.size()!=conceptosTercero.size())
				{
					conceptosTercero = new ArrayList<DtoConceptoRetencionTercero>();
				}*/
			}
			
			
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarConceptosRetencionXTercero:  "+e);
		}
		return conceptosTercero; 
	}
	
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
	public static DtoRetencion consultarTarifaRetencion(Connection con,ArrayList<DtoConceptoRetencionTercero> conceptos,boolean integral,int codigoGrupo,int codigoClase,int codigoConcepto,String fecha,double valorDocumento)
	{
		DtoRetencion retencion = new DtoRetencion();
		String consulta = "";
		try
		{
			PreparedStatementDecorator pst = null;
			ResultSetDecorator rs = null;
			//Se toman los codigos de los conceptos 
			for(DtoConceptoRetencionTercero concepto:conceptos)
			{
				if(integral)
				{
					//INTEGRAL ***************************************************
					consulta = "SELECT "+ 
						"dvc.concepto_retencion as concepto, "+
						"dc.fecha_vigencia_inicial as fecha, "+ 
						"coalesce(dvc.base_minima,"+ConstantesBD.codigoNuncaValido+") as base_minima, "+
						"coalesce(dvc.porcentaje_ret_int,"+ConstantesBD.codigoNuncaValido+") as porcentaje "+ 
						"from administracion.det_vig_con_retencion dvc "+ 
						"INNER JOIN administracion.det_conceptos_retencion dc ON(dc.consecutivo = dvc.det_concepto_retencion) "+ 
						"WHERE " +
						"dvc.concepto_retencion = ? and " +
						"dvc.activo = '"+ConstantesBD.acronimoSi+"' and " +
						"dvc.indicativo_integral = '"+ConstantesBD.acronimoSi+"' and " +
						"dc.fecha_vigencia_inicial <= ? "+
						"order by concepto, fecha desc, base_minima desc";
					pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst.setInt(1,Integer.parseInt(concepto.getConsecutivo()));
					pst.setDate(2,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fecha)));
				}
				else
				{
					//NO INTEGRAL **********************************************
					String tablaFiltro = "";
					String campoFiltro = "";
					int codigoCampo = ConstantesBD.codigoNuncaValido;
					if(codigoClase>0)
					{
						tablaFiltro = "clase";
						campoFiltro = "clase";
						codigoCampo = codigoClase;
					}
					else if(codigoGrupo>0)
					{
						tablaFiltro = "grupo";
						campoFiltro = "grupo";
						codigoCampo = codigoGrupo;
					}
					else if (codigoConcepto>0)
					{
						tablaFiltro = "cfv";
						campoFiltro = "concepto";
						codigoCampo = codigoConcepto;
					}
					consulta = "SELECT "+ 
						"dvc.concepto_retencion as concepto, "+
						"dc.fecha_vigencia_inicial as fecha, "+ 
						"dvg.base_minima as base_minima, "+
						"dvg.porcentaje_ret_int as porcentaje "+ 
						"from administracion.det_vig_con_retencion dvc "+ 
						"INNER JOIN administracion.det_conceptos_retencion dc ON(dc.consecutivo = dvc.det_concepto_retencion) "+ 
						"INNER JOIN administracion.det_vig_con_ret_"+tablaFiltro+" dvg ON(dvg.det_vig_con_retencion = dvc.consecutivo) "+ 
						"WHERE " +
						"dvc.concepto_retencion = ? and " +
						"dvc.activo = '"+ConstantesBD.acronimoSi+"' and " +
						"dvc.indicativo_integral = '"+ConstantesBD.acronimoNo+"' and " +
						"dc.fecha_vigencia_inicial <= ? and " +
						"dvg."+campoFiltro+" = ? and " +
						"dvg.activo = '"+ConstantesBD.acronimoSi+"' " +
						"order by concepto, fecha desc, base_minima desc ";
					pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst.setInt(1,Integer.parseInt(concepto.getConsecutivo()));
					pst.setDate(2,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fecha)));
					pst.setInt(3,codigoCampo);
					
					//logger.info("consulta de retencion no integral: "+consulta+", concepto: "+concepto.getConsecutivo()+", fecha: "+fecha+", codigoCampo: "+codigoCampo);
				}
				
				rs = new ResultSetDecorator(pst.executeQuery());
				if(rs.next())
				{
					//SWe verifica que hayan datos nuevos
					if(rs.getDouble("base_minima") >=0 && rs.getDouble("porcentaje") >= 0)
					{
						if(rs.getDouble("base_minima")<=valorDocumento)
						{
							double valorRetencion = Math.ceil((valorDocumento*rs.getDouble("porcentaje")) / 100);
							concepto.setValorRetencion(UtilidadTexto.formatearValores(valorRetencion, "0.00"));
							concepto.setValorBaseGravable(UtilidadTexto.formatearValores(valorDocumento+"", "0.00"));
							concepto.setCalculoRetencionExitoso(true);
						}
						else
						{
							concepto.setCancelarRetencion(true);
						}
						
					}
				}
				rs.close();
				pst.close();
			} //Fin FOR
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarTarifaRetencion: "+e+" >> "+consulta);
		}
		retencion.setConceptos(conceptos);
		return retencion;
		
	}
	
	/**
	 * Método que verifica si una factura aplica para el calculo de la autoretencion
	 * @param con
	 * @param numeroDocumento
	 * @return
	 */
	public static ResultadoBoolean validacionFacturaPacienteAutoretencion(Connection con,String numeroDocumento)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(false,"");
		try
		{
			String consulta = "SELECT count(1) as cuenta "+
				"from facturas f "+
				"INNER JOIN convenios c ON(c.codigo = f.convenio) "+
				"WHERE f.consecutivo_factura = ? and (c.tipo_contrato <> "+ConstantesBD.codigoTipoContratoCapitado+" or c.tipo_regimen = '"+ConstantesBD.codigoTipoRegimenParticular+"')"; 
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setLong(1,Long.parseLong(numeroDocumento));
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				if(rs.getInt("cuenta")>0)
				{
					resultado.setResultado(true);
				}
			}
			rs.close();
			pst.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en validacionFacturaPacienteAutoretencion: "+e);
		}
		return resultado;
	}
	
	/**
	 * Método que consulta el número de servicios y articulos que tiene una factura
	 * @param con
	 * @param numeroDocumento
	 * @return
	 */
	public static int[] consultarNumServiciosYArticulosFactura(Connection con,String numeroDocumento)
	{
		int[] numeros ={0,0};
		try
		{
			String consulta = "SELECT "+
				"count(1) as numero "+ 
				"from facturas f "+ 
				"INNER JOIN det_factura_solicitud dfs on(dfs.factura = f.codigo) "+ 
				"WHERE "+ 
				"f.consecutivo_factura = ? and dfs.servicio is not null";
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setLong(1,Long.parseLong(numeroDocumento));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				numeros[0] = rs.getInt("numero");
			}
			rs.close();
			pst.close();
			
			consulta = "SELECT "+
			"count(1) as numero "+ 
			"from facturas f "+ 
			"INNER JOIN det_factura_solicitud dfs on(dfs.factura = f.codigo) "+ 
			"WHERE "+ 
			"f.consecutivo_factura = ? and dfs.articulo is not null";
			pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setLong(1,Long.parseLong(numeroDocumento));
			rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				numeros[1] = rs.getInt("numero");
			}
			rs.close();
			pst.close();
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarNumServiciosYArticulosFactura: "+e);
		}
		return numeros;
	}
	
}
