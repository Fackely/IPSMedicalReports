/*
 * Created on Jun 10, 2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadFecha;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.RipsDao;
import com.princetonsa.dao.sqlbase.SqlBaseRipsDao;

/**
 * @author sebacho
 *
 * @todo To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PostgresqlRipsDao implements RipsDao {

	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(PostgresqlRipsDao.class);
	
	/**
	 * Cadena para insertar los datos Rips de un registro tipo cita
	 */
	private final String insertarDatorRipsCitaStr="INSERT INTO rips_consultorios "+
		"(codigo,convenio,fecha_inicial,fecha_final,servicio,tipo_diagnostico,diagnostico_principal," +
		"tipo_cie_ppal,diagnostico_rel1,tipo_cie_rel1,diagnostico_rel2,tipo_cie_rel2,diagnostico_rel3," +
		"tipo_cie_rel3,causa_externa,finalidad_consulta,valor_total,valor_copago,valor_empresa,autorizacion," +
		"paciente,cuenta,numero_solicitud,institucion,fecha_atencion) "+
		"VALUES (nextval('seq_rips_consultorios'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Cadena para insertar Rips de un registro que no es cita pero tiene servicio
	 * asociado
	 */
	private final String insertarDatorRipsNoCitaStr="INSERT INTO rips_consultorios " +
		"(codigo,convenio,fecha_inicial,fecha_final,servicio,diagnostico_principal,tipo_cie_ppal," +
		"diagnostico_rel1,tipo_cie_rel1,diagnostico_rel2,tipo_cie_rel2,valor_total,autorizacion," +
		"ambito_realizacion,personal_atiende,forma_realizacion,paciente,cuenta,numero_solicitud,institucion,fecha_atencion) " +
		"VALUES (nextval('seq_rips_consultorios'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Cadena para insertar Rips de un registro que tiene o no tiene solicitud o es
	 * un registro OTRO, que no tenían un servicio asociado
	 */
	private final String insertarDatorRipsSinServicioStr="INSERT INTO rips_consultorios " +
		"(codigo,convenio,fecha_inicial,fecha_final,servicio,tipo_diagnostico,diagnostico_principal,tipo_cie_ppal," +
		"diagnostico_rel1,tipo_cie_rel1,diagnostico_rel2,tipo_cie_rel2,diagnostico_rel3,tipo_cie_rel3," +
		"causa_externa,finalidad_consulta,valor_total,valor_copago,valor_empresa,autorizacion," +
		"paciente,cuenta,numero_solicitud,institucion,fecha_atencion) " +
		"VALUES (nextval('seq_rips_consultorios'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Cadena para actualizar los datos del registro RIPS que va hacer parte de la generación de archivos
	 */
	private final String actualizarDatosRipsStr="UPDATE rips_consultorios SET "+ 
		"numero_factura=?, numero_remision=(SELECT last_value FROM seq_numero_remision), rips=true, fecha_factura=?, " +
		"fecha_generacion=?, hora_generacion=?, fecha_remision=?,fecha_atencion=?, usuario=? WHERE codigo=?";
	/**
	 * Método para consultar los datos del archivo AF por Factura
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param convenio
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaFacturaAF(Connection con, String fechaInicial,String fechaFinal, int convenio,int codigoInstitucion, boolean esAxRips, String numeroFactura) {
		return SqlBaseRipsDao.consultaFacturaAF(con,fechaInicial,fechaFinal,convenio,codigoInstitucion,  esAxRips,  numeroFactura);
	}

	/**
	 * Método para consultar los datos del archivo AF por cuenta de cobro
	 * @param con
	 * @param numeroCuentaCobro
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCuentaCobroAF(Connection con,double numeroCuentaCobro,int codigoInstitucion) {
		return SqlBaseRipsDao.consultaCuentaCobroAF(con,numeroCuentaCobro,codigoInstitucion);
	}
	
	/**
	 * Método para consultar los registros del archivo AD por factura
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param convenio
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaFacturaAD(Connection con,String fechaInicial,String fechaFinal,int convenio,int codigoInstitucion,boolean esAxRips, String numeroFactura)
	{
		return SqlBaseRipsDao.consultaFacturaAD(con,fechaInicial,fechaFinal,convenio,codigoInstitucion, esAxRips,  numeroFactura);
	}

	/**
	 * Método para consultar los registros del archivo AD por cuenta de cobro
	 * @param con
	 * @param numeroCuentaCobro
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCuentaCobroAD(Connection con,double numeroCuentaCobro,int codigoInstitucion)
	{
		return SqlBaseRipsDao.consultaCuentaCobroAD(con,numeroCuentaCobro,codigoInstitucion);
	}

	/**
	 * Método para cosnultar los registros del archivo US por factura
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param convenio
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaFacturaUS(Connection con,String fechaInicial,String fechaFinal,int convenio,int codigoInstitucion,boolean esAxRips, String numeroFactura)
	{
		return SqlBaseRipsDao.consultaFacturaUS(con,fechaInicial,fechaFinal,convenio,codigoInstitucion, esAxRips,  numeroFactura);
	}
	
	/**
	 * Método para consultar los datos del archivo US por cuenta de cobro
	 * @param con
	 * @param numeroCuentaCobro
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCuentaCobroUS(Connection con,double numeroCuentaCobro,int codigoInstitucion)
	{
		return SqlBaseRipsDao.consultaCuentaCobroUS(con,numeroCuentaCobro,codigoInstitucion);
	}
	
	/**
	 * Método para consultar los datos del archivo AC por factura
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param convenio
	 * @param institucion
	 * @param tarifarioOficial
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaFacturaAC(Connection con,String fechaInicial,String fechaFinal,int convenio,int codigoInstitucion,int tarifarioOficial, boolean esAxRips, String numeroFactura)
	{
		return SqlBaseRipsDao.consultaFacturaAC(con,fechaInicial,fechaFinal,convenio,codigoInstitucion,tarifarioOficial,  esAxRips,  numeroFactura);
	}
	
	/**
	 * Método para consultar los datos del archivo AC por cuenta de cobro
	 * @param con
	 * @param numeroCuentaCobro
	 * @param institucion
	 * @param tarifarioOficial
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCuentaCobroAC(Connection con,double numeroCuentaCobro,int codigoInstitucion,int tarifarioOficial)
	{
		return SqlBaseRipsDao.consultaCuentaCobroAC(con,numeroCuentaCobro,codigoInstitucion,tarifarioOficial);
	}
	
	/**
	 * Método para consultar los datos del archivo AH 
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param cuenta cobro
	 * @param convenio
	 * @param esFactura
	 * @param institucion
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaAH(Connection con,String fechaInicial,String fechaFinal,
			double cuentaCobro,int convenio,int codigoInstitucion,boolean esFactura, boolean esAxRips, String numeroFactura)
	{
		return SqlBaseRipsDao.consultaAH(con,fechaInicial,fechaFinal,cuentaCobro,convenio,codigoInstitucion,esFactura,  esAxRips,  numeroFactura);
	}
	
	/**
	 * Método para consultar los datos del archivo AU
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param cuentaCobro
	 * @param convenio
	 * @param codigoInstitucion
	 * @param esFactura
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaAU(Connection con,String fechaInicial, String fechaFinal,
			double cuentaCobro,int convenio,int codigoInstitucion,boolean esFactura, boolean esAxRips, String numeroFactura)
	{
		return SqlBaseRipsDao.consultaAU(con,fechaInicial,fechaFinal,cuentaCobro,convenio,codigoInstitucion,esFactura,  esAxRips,  numeroFactura);
	}
	
	/**
	 * Método para consultar los datos del archivo AM
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param cuentaCobro
	 * @param convenio
	 * @param codigoInstitucion
	 * @param esFactura
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaAM(Connection con,String fechaInicial, String fechaFinal,
			double cuentaCobro,int convenio,int codigoInstitucion,boolean esFactura, boolean esAxRips, String numeroFactura)
	{
		return SqlBaseRipsDao.consultaAM(con,fechaInicial,fechaFinal,cuentaCobro,convenio,codigoInstitucion,esFactura,  esAxRips,  numeroFactura);
	}
	
	/**
	 * Método para consultar los datos del archivo AT
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param cuentaCobro
	 * @param convenio
	 * @param codigoInstitucion
	 * @param tarifarioOficial
	 * @param esFactura
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaAT(Connection con,String fechaInicial, String fechaFinal,
			double cuentaCobro,int convenio,int codigoInstitucion,int tarifarioOficial,boolean esFactura,boolean esAxRips, String numeroFactura)
	{
		return SqlBaseRipsDao.consultaAT(con,fechaInicial,fechaFinal,cuentaCobro,convenio,codigoInstitucion,tarifarioOficial,esFactura,esAxRips, numeroFactura);
	}
	
	/**
	 * Método para consultar los datos del archivo AP
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param cuentaCobro
	 * @param convenio
	 * @param codigoInstitucion
	 * @param tarifarioOficial
	 * @param esFactura
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaAP(Connection con,String fechaInicial, String fechaFinal,
			double cuentaCobro,int convenio,int codigoInstitucion,int tarifarioOficial,boolean esFactura,boolean esAxRips, String numeroFactura)
	{
		//return SqlBaseRipsDao.consultaAP(con,fechaInicial,fechaFinal,cuentaCobro,convenio,codigoInstitucion,tarifarioOficial,esFactura);
		return SqlBaseRipsDao.consultaAP(con,fechaInicial,fechaFinal,cuentaCobro,convenio,codigoInstitucion,tarifarioOficial,esFactura, esAxRips,  numeroFactura);
	}
	
	/**
	 * Método para consultar los datos del archivo AN
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param cuentaCobro
	 * @param convenio
	 * @param codigoInstitucion
	 * @param esFactura
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaAN(Connection con,String fechaInicial, String fechaFinal,
			double cuentaCobro,int convenio,int codigoInstitucion,boolean esFactura,boolean esAxRips, String numeroFactura)
	{
		return SqlBaseRipsDao.consultaAN(con,fechaInicial,fechaFinal,cuentaCobro,convenio,codigoInstitucion,esFactura, esAxRips,  numeroFactura);
	}
	
	/**
	 * Método para consultar los registros del RIPS por Rangos
	 * @param con
	 * @param codigoConvenio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaRegistrosPorRangos(Connection con,int codigoConvenio,String fechaInicial,String fechaFinal)
	{
		return SqlBaseRipsDao.consultaRegistrosPorRangos(con,codigoConvenio,fechaInicial,fechaFinal,"true");
	}
	
	/**
	 * Método para consultar los registros del RIPS por Rangos Paciente
	 * @param con
	 * @param idCuenta
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaRegistrosRangosPaciente(Connection con,int idCuenta)
	{
		return SqlBaseRipsDao.consultaRegistrosRangosPaciente(con,idCuenta,"true");
	}
	/**
	 * Método usado para cargar la información rips de un cita.
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap cargarDatosRipsCita(Connection con,int numeroSolicitud)
	{
		return SqlBaseRipsDao.cargarDatosRipsCita(con,numeroSolicitud);
	}
	
	/**
	 * Método usado para cargar parte de los datos rips de un solicitud diferente a cita que tiene cargada un servicio
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap cargarDatosRipsNoCita(Connection con,int numeroSolicitud)
	{
		return SqlBaseRipsDao.cargarDatosRipsNoCita(con,numeroSolicitud);
	}
	
	/**
	 * Método usado para consultar servicios por codigo CUPS
	 * @param con
	 * @param codigoCups
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultarServicios(Connection con,String codigoCups)
	{
		return SqlBaseRipsDao.consultarServicios(con,codigoCups);
	}
	
	/**
	 * Método para insertar los datos Rips de un registro tipo cita
	 * retorna el consecutivo automático del registro en la tabla 
	 * @param con
	 * @param codigoConvenio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoServicio
	 * @param tipoDiagnostico
	 * @param diagPrincipal
	 * @param ciePrincipal
	 * @param diagRel1
	 * @param cieRel1
	 * @param diagRel2
	 * @param cieRel2
	 * @param diagRel3
	 * @param cieRel3
	 * @param causaExterna
	 * @param finalidadConsulta
	 * @param valorTotal
	 * @param valorCopago
	 * @param valorEmpresa
	 * @param autorizacion
	 * @param paciente
	 * @param cuenta
	 * @param solicitud
	 * @param institucion
	 * @return
	 */
	public int insertarDatorRipsCita(
			Connection con,int codigoConvenio,String fechaInicial,String fechaFinal,
			int codigoServicio,int tipoDiagnostico,String diagPrincipal,int ciePrincipal,
			String diagRel1,int cieRel1,String diagRel2,int cieRel2,String diagRel3,int cieRel3,
			int causaExterna,String finalidadConsulta,double valorTotal,double valorCopago,
			double valorEmpresa,String autorizacion,int paciente,int cuenta,int solicitud,
			int institucion,String fechaAtencion)
	
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(insertarDatorRipsCitaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoConvenio);
			pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
			pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
			pst.setInt(4,codigoServicio);
			pst.setInt(5,tipoDiagnostico);
			pst.setString(6,diagPrincipal);
			pst.setInt(7,ciePrincipal);
			//-----verificación de los datos que no son requeridos
			if(cieRel1==-1)
			{
				pst.setNull(8,Types.VARCHAR);
				pst.setNull(9,Types.INTEGER);
			}
			else
			{
				pst.setString(8,diagRel1);
				pst.setInt(9,cieRel1);
			}
			if(cieRel2==-1)
			{
				pst.setNull(10,Types.VARCHAR);
				pst.setNull(11,Types.INTEGER);
			}
			else
			{
				pst.setString(10,diagRel2);
				pst.setInt(11,cieRel2);
			}
			if(cieRel3==-1)
			{
				pst.setNull(12,Types.VARCHAR);
				pst.setNull(13,Types.INTEGER);
			}
			else
			{
				pst.setString(12,diagRel3);
				pst.setInt(13,cieRel3);
			}
			//-----------------------------
			pst.setInt(14,causaExterna);
			pst.setString(15,finalidadConsulta);
			pst.setDouble(16,valorTotal);
			pst.setDouble(17,valorCopago);
			pst.setDouble(18,valorEmpresa);
			pst.setString(19,autorizacion);
			pst.setInt(20,paciente);
			pst.setInt(21,cuenta);
			pst.setInt(22,solicitud);
			pst.setInt(23,institucion);
			pst.setString(24,fechaAtencion);
			
			int resp=pst.executeUpdate();
			
			if(resp>0)
			{
				resp=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(con, "seq_rips_consultorios");
				return resp;
			}
			else
			{
				return -1;
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarDatosRipsCita de PostgresqlRipsDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Método usado para insertar un registro RIPS que nos ea cita
	 * pero que tiene un servicio asociado, retorna el consecutivo automático
	 * del registro
	 * @param con
	 * @param codigoConvenio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoServicio
	 * @param diagPrincipal
	 * @param ciePrincipal
	 * @param diagRelacionado
	 * @param cieRelacionado
	 * @param diagComplicacion
	 * @param cieComplicacion
	 * @param valorTotal
	 * @param autorizacion
	 * @param ambitoRealizacion
	 * @param personalAtiende
	 * @param formaRealizacion
	 * @param paciente
	 * @param cuenta
	 * @param solicitud
	 * @param institucion
	 * @return
	 */
	public int insertarDatosRipsNoCita(
			Connection con,int codigoConvenio,String fechaInicial,String fechaFinal,
			int codigoServicio,String diagPrincipal,int ciePrincipal,String diagRelacionado,
			int cieRelacionado,String diagComplicacion,int cieComplicacion,double valorTotal,
			String autorizacion,int ambitoRealizacion,int personalAtiende,int formaRealizacion,
			int paciente,int cuenta,int solicitud,int institucion,String fechaAtencion)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(insertarDatorRipsNoCitaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoConvenio);
			pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
			pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
			pst.setInt(4,codigoServicio);
			//-----verificación de los datos que no son requeridos
			if(ciePrincipal==-1)
			{
				pst.setNull(5,Types.VARCHAR);
				pst.setNull(6,Types.INTEGER);
			}
			else
			{
				pst.setString(5,diagPrincipal);
				pst.setInt(6,ciePrincipal);
			}
			if(cieRelacionado==-1)
			{
				pst.setNull(7,Types.VARCHAR);
				pst.setNull(8,Types.INTEGER);
			}
			else
			{
				pst.setString(7,diagRelacionado);
				pst.setInt(8,cieRelacionado);
			}
			if(cieComplicacion==-1)
			{
				pst.setNull(9,Types.VARCHAR);
				pst.setNull(10,Types.INTEGER);
			}
			else
			{
				pst.setString(9,diagComplicacion);
				pst.setInt(10,cieComplicacion);
			}
			
			//-----------------------------
			pst.setDouble(11,valorTotal);
			pst.setString(12,autorizacion);
			
			pst.setInt(13,ambitoRealizacion);
			
			if(personalAtiende==-1)
				pst.setNull(14,Types.INTEGER);
			else
				pst.setInt(14,personalAtiende);
			
			if(formaRealizacion==-1)
				pst.setNull(15,Types.INTEGER);
			else
				pst.setInt(15,formaRealizacion);
			
			pst.setInt(16,paciente);
			pst.setInt(17,cuenta);
			
			if(solicitud==-1)
				pst.setNull(18,Types.INTEGER);
			else
				pst.setInt(18,solicitud);
			
			pst.setInt(19,institucion);
			pst.setString(20,fechaAtencion);
			
			int resp=pst.executeUpdate();
			
			if(resp>0)
			{
				resp=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(con, "seq_rips_consultorios");
				return resp;
			}
			else
			{
				return -1;
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarDatosRipsNoCita de PostgresqlRipsDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Método para insertar un registro de una solicitud, o no solicitud
	 * ú registro OTRO que no tenían un servicio asociado
	 * @param con
	 * @param codigoConvenio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoServicio
	 * @param diagPrincipal
	 * @param ciePrincipal
	 * @param diagRelacionado1
	 * @param cieRelacionado1
	 * @param diagRelacionado2
	 * @param cieRelacionado2
	 * @param diagRelacionado3
	 * @param cieRelacionado3
	 * @param causaExterna
	 * @param finalidadConsulta
	 * @param valorTotal
	 * @param valorCopago
	 * @param valorEmpresa
	 * @param autorizacion
	 * @param paciente
	 * @param cuenta
	 * @param numeroSolicitud
	 * @param institucion
	 * @return
	 */
	public int insertarDatosRipsSinServicio(
			Connection con,int codigoConvenio,String fechaInicial,String fechaFinal,
			int codigoServicio,int tipoDiagnostico,String diagPrincipal,int ciePrincipal,String diagRelacionado1,
			int cieRelacionado1,String diagRelacionado2,int cieRelacionado2,
			String diagRelacionado3,int cieRelacionado3,int causaExterna,String finalidadConsulta,
			double valorTotal,double valorCopago,double valorEmpresa,String autorizacion,
			int paciente,int cuenta,int numeroSolicitud,int institucion,String fechaAtencion)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(insertarDatorRipsSinServicioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoConvenio);
			pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
			pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
			pst.setInt(4,codigoServicio);
			pst.setInt(5,tipoDiagnostico);
			pst.setString(6,diagPrincipal);
			pst.setInt(7,ciePrincipal);
			
			//-----verificación de los datos que no son requeridos
			if(cieRelacionado1==-1)
			{
				pst.setNull(8,Types.VARCHAR);
				pst.setNull(9,Types.INTEGER);
			}
			else
			{
				pst.setString(8,diagRelacionado1);
				pst.setInt(9,cieRelacionado1);
			}
			if(cieRelacionado2==-1)
			{
				pst.setNull(10,Types.VARCHAR);
				pst.setNull(11,Types.INTEGER);
			}
			else
			{
				pst.setString(10,diagRelacionado2);
				pst.setInt(11,cieRelacionado2);
			}
			if(cieRelacionado3==-1)
			{
				pst.setNull(12,Types.VARCHAR);
				pst.setNull(13,Types.INTEGER);
			}
			else
			{
				pst.setString(12,diagRelacionado3);
				pst.setInt(13,cieRelacionado3);
			}
			
			//-----------------------------
			pst.setInt(14,causaExterna);
			pst.setString(15,finalidadConsulta);
			pst.setDouble(16,valorTotal);
			pst.setDouble(17,valorCopago);
			pst.setDouble(18,valorEmpresa);
			pst.setString(19,autorizacion);
			
			
			pst.setInt(20,paciente);
			pst.setInt(21,cuenta);
			
			if(numeroSolicitud==-1)
				pst.setNull(22,Types.INTEGER);
			else
				pst.setInt(22,numeroSolicitud);
			
			pst.setInt(23,institucion);
			pst.setString(24,fechaAtencion);
			
			int resp=pst.executeUpdate();
			
			if(resp>0)
			{
				resp=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(con, "seq_rips_consultorios");
				return resp;
			}
			else
			{
				return -1;
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarDatosRipsSinServicio de PostgresqlRipsDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Método que consulta la tarifa del servicio y calcula el valor del copago según la cuenta
	 * @param con
	 * @param codigoServicio
	 * @param idCuenta
	 * @return valor total (separadorSplit) valor copago
	 */
	public String consultarValoresServicio(Connection con,int codigoServicio,int idCuenta)
	{
		return SqlBaseRipsDao.consultarValoresServicio(con,codigoServicio,idCuenta);
	}
	
	/**
	 * Método para consultar los servicio por descripcion
	 * @param con
	 * @param criterio
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultarServicioXNombre(Connection con,String criterio)
	{
		return SqlBaseRipsDao.consultarServicioXNombre(con,criterio);
	}
	
	/**
	 * Método para cargar el resumen de un registro RIPS
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultarRegistroRips(Connection con,int consecutivo)
	{
		return SqlBaseRipsDao.consultarRegistroRips(con,consecutivo);
	}
	
	/**
	 * Método para actualizar los registros RIPS que van a hacer parte de la generación
	 * de archivos (tienen el checkbox RIPS activado)
	 * @param con
	 * @param numeroFactura
	 * @param fechaFactura
	 * @param fechaGeneracion
	 * @param login
	 * @return
	 */
	public int actualizarDatosRips(Connection con,int consecutivo, String numeroFactura,String fechaFactura,String fechaRemision,String fechaAtencion,String login)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(actualizarDatosRipsStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,numeroFactura);
			pst.setString(2,UtilidadFecha.conversionFormatoFechaABD(fechaFactura));
			pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
			pst.setString(4,UtilidadFecha.convertirHoraACincoCaracteres(UtilidadFecha.getHoraActual()));
			pst.setString(5,UtilidadFecha.conversionFormatoFechaABD(fechaRemision));
			pst.setString(6,UtilidadFecha.conversionFormatoFechaABD(fechaAtencion));
			pst.setString(7,login);
			pst.setInt(8,consecutivo);
			
			int resp=pst.executeUpdate();
			
			if(resp>0)
			{
				
				resp=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(con, "seq_numero_remision");
				
				return resp;
			}
			else
			{
				
				return -1;
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarDatosRips de PostgresqlRipsDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Método para aumentar el número de secuencia de la remisión
	 * @param con
	 * @return
	 */
	public int siguienteSecuenciaRemision(Connection con)
	{
		String consulta="select nextval('seq_numero_remision') AS secuencia";
		try
		{
			Statement st=con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			
			ResultSetDecorator rs=  new ResultSetDecorator(st.executeQuery(consulta));
			
			if(rs.next())
			{
				return rs.getInt("secuencia");
			}
			else
			{
				return -1;
			} 
		}
		catch(SQLException e)
		{
			logger.error("Error en siguienteSecuenciaRemision de PostgresqlRipsDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Método para consultar los registros del archivo AF
	 * del RIPS consultorios
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaConsultoriosAF(Connection con,String numeroFactura,String numeroRemision)
	{
		return SqlBaseRipsDao.consultaConsultoriosAF(con,numeroFactura,numeroRemision);
	}
	
	/**
	 * Método para consultar los registros del archivo AD
	 * del RIPS consultorios
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaConsultoriosAD(Connection con,String numeroFactura,String numeroRemision)
	{
		return SqlBaseRipsDao.consultaConsultoriosAD(con,numeroFactura,numeroRemision);
	}
	
	/**
	 * Método para consultar los registros del archivo US
	 * del RIPS consultorios
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaConsultoriosUS(Connection con,String numeroFactura,String numeroRemision)
	{
		return SqlBaseRipsDao.consultaConsultoriosUS(con,numeroFactura,numeroRemision);
	}
	
	/**
	 * Método para consultar los registros del archivo AC
	 * del RIPS consultorios
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaConsultoriosAC(Connection con,String numeroFactura,String numeroRemision)
	{
		return SqlBaseRipsDao.consultaConsultoriosAC(con,numeroFactura,numeroRemision);
	}
	
	/**
	 * Método para consultar los registros del archivo AP
	 * del RIPS consultorios
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaConsultoriosAP(Connection con,String numeroFactura,String numeroRemision)
	{
		return SqlBaseRipsDao.consultaConsultoriosAP(con,numeroFactura,numeroRemision);
	}
	
	
	/**
	 * Método usado para consultar la generación de RIPS consultorios
	 * @param con
	 * @param codigoConvenio
	 * @param fechaFactura
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaGeneracionRips(Connection con,int codigoConvenio,String fechaFactura)
	{
		return SqlBaseRipsDao.consultaGeneracionRips(con,codigoConvenio,fechaFactura);
	}
	
	/**
	 * Método para actualizar los datos de un registro RIPS ya insertado
	 * @param con
	 * @param consecutivo
	 * @param valorTotal
	 * @param valorCopago
	 * @param valorEmpresa
	 * @param autorizacion
	 * @return
	 */
	public int actualizarDatosRips(Connection con,int consecutivo,double valorTotal,
			double valorCopago,double valorEmpresa,String autorizacion)
	{
		return SqlBaseRipsDao.actualizarDatosRips(con,consecutivo,valorTotal,valorCopago,valorEmpresa,autorizacion);
		
	}
	
	/**
	 * Método para verificar si una solicitud ya está registrada en RIPS
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public int verificarExistenciaRegistroRips(Connection con,int numeroSolicitud)
	{
		return SqlBaseRipsDao.verificarExistenciaRegistroRips(con,numeroSolicitud);
	}
	
	/**
	 * Método para consultar los datos de un servicio
	 * @param con
	 * @param codigoServicio
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultarServicio(Connection con,int codigoServicio)
	{
		return SqlBaseRipsDao.consultarServicio(con,codigoServicio);
	}
	
	/**
	 * Método usado para insertar una excepcion de rips consultorios
	 * desde la valoración
	 * @param con
	 * @param numeroSolicitud
	 * @param institucion
	 * @param rips
	 * @return
	 */
	public int insertarExcepcionRipsConsultorios(Connection con,int numeroSolicitud,int institucion,boolean rips)
	{
		return SqlBaseRipsDao.insertarExcepcionRipsConsultorios(con,numeroSolicitud,institucion,rips);
	}
	
	/**
	 * Método para consultar la excepcion rips de un registro
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean consultarExcepcionRipsConsultorios(Connection con,int numeroSolicitud,int institucion)
	{
		return SqlBaseRipsDao.consultarExcepcionRipsConsultorios(con,numeroSolicitud,institucion);
	}
	
	/**
	 * Método que consulta de datos de la cuenta de cobro de capitacion
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultarDatosCxCCapitacion(Connection con,HashMap campos)
	{
		return SqlBaseRipsDao.consultarDatosCxCCapitacion(con, campos);
	}
	
	/**
	 * Método para consultar los RIPS del archivo AD en Capitacion
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCapitacionAD(Connection con,HashMap campos)
	{
		return SqlBaseRipsDao.consultaCapitacionAD(con, campos);
	}
	
	/**
	 * Método que consulta los RIPS del archivo US Capitacion
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCapitacionUS(Connection con,HashMap campos)
	{
		return SqlBaseRipsDao.consultaCapitacionUS(con, campos);
	}
	
	/**
	 * Método que consulta los RIPS del archivo AC Capitacion
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCapitacionAC(Connection con,HashMap campos)
	{
		return SqlBaseRipsDao.consultaCapitacionAC(con, campos);
	}
	
	/**
	 * Método que consulta los rips AP Capitacion
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCapitacionAP(Connection con,HashMap campos)
	{
		return SqlBaseRipsDao.consultaCapitacionAP(con, campos);
	}
	
	/**
	 * Método que consulta los rips del archivo AM Capitacion
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCapitacionAM(Connection con,HashMap campos)
	{
		return SqlBaseRipsDao.consultaCapitacionAM(con, campos);
	}
	
	/**
	 * Método que consulta los rips AT Capitacion
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCapitacionAT(Connection con,HashMap campos)
	{
		return SqlBaseRipsDao.consultaCapitacionAT(con, campos);
	}
	
	/**
	 * Método que consulta los rips de AU Capitacion
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCapitacionAU(Connection con,HashMap campos)
	{
		return SqlBaseRipsDao.consultaCapitacionAU(con, campos);
	}
	
	/**
	 * Método implementado para consultar los rips AH Capitacion
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCapitacionAH(Connection con,HashMap campos)
	{
		return SqlBaseRipsDao.consultaCapitacionAH(con, campos);
	}
	
	/**
	 * Método que consulta los rips del archivo AN
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCapitacionAN(Connection con,HashMap campos)
	{
		return SqlBaseRipsDao.consultaCapitacionAN(con, campos);
	}
	
	
	/**
	 * Metodo de consulta del convenio de una factura consultada en la interfaz de ax_rips
	 * @param con
	 * @param nroFactura
	 * @return
	 */
	public int consulartConvenioXNroFactura(Connection con,int nroFactura)
	{
		return SqlBaseRipsDao.consulartConvenioXNroFactura(con, nroFactura);
	}
	
	
	/**
	 * Metodo de consulta del convenio de una factura
	 * @param con
	 * @param nroFactura
	 * @return
	 */
	public int consultarConvenioFactura(Connection con,int nroFactura)
	{
		return SqlBaseRipsDao.consultarConvenioFactura(con, nroFactura);
	}

	@Override
	public String consultarNombreTercero(Connection con, String nitTercero) 
	{
		return SqlBaseRipsDao.consultarNombreTercero(con, nitTercero);
	}

	@Override
	public String consultarNitConvenio(Connection con, int codigoConvenio) 
	{
		return SqlBaseRipsDao.consultarNitConvenio(con, codigoConvenio);
	}
}
