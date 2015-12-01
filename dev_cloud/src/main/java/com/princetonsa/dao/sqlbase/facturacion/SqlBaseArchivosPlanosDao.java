package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dto.facturacion.DtoArchivoPlanoColsa;

import util.ConstantesBD;
import util.InfoDatosString;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;


/**
 * @author Jose Eduardo Arias Doncel
 * */
public class SqlBaseArchivosPlanosDao
{

	
	private static Logger logger = Logger.getLogger(SqlBaseArchivosPlanosDao.class);
	//********************Atributos
	
	//Cadena de consulta del registro mas reciente del historial de Archivos Planos
	public static String strCadenaConsultaHistorialArchivosPlanos = "SELECT " +
			"institucion,"+
			"convenio," +
			"numero_envio," +			
			"numero_cuenta_cobro,"+
			"fecha_inicial_fact,"+
			"fecha_final_fact,"+
			"fecha_envio,"+
			"secuencia,"+
			"ruta,"+
			"usuario_modifica,"+
			"fecha_modifica,"+
			"hora_modifica "+
			"FROM historial_busq_archivoplano " +
			"WHERE fecha_modifica || ' ' || hora_modifica = max(fecha_modifica || ' ' || hora_modifica) " +
			"AND institucion = ? ";
	
	//Cadena de consulta la ruta del registro mas reciente del historial de Archivos Planos
	public static String strConsultaRutaHistorialArchivosPlanos = "SELECT " +			
			"ruta "+			
			"FROM historial_busq_archivoplano " +
			"WHERE institucion = ? " +
			"ORDER BY fecha_modifica  desc, hora_modifica desc "+ValoresPorDefecto.getValorLimit1()+" 1 ";	
	
	
	//Cadena de Consulta de convenios Parametrica Archivo Plano
	public static String strConsultaConveniosParametrica = "SELECT " +
			"ap.convenio, " +
			"c.nombre || ' - ' || ap.convenio AS descripcion," +
			"ap.unidad_economica AS unidad_economica," +
			"ap.identif_compania AS identif_compania," +
			"ap.identif_plan AS identif_plan " +
			"FROM archivo_plano_colsanitas ap " +
			"INNER JOIN convenios c ON (c.codigo = ap.convenio) " +
			"WHERE ap.institucion = ? " +
			"ORDER BY c.nombre DESC ";
	
	
	//Cadena de Consulta de Cuentas de Cobro
	public static String strConsultaCuentasCobro = "SELECT " +
			"cc.numero_cuenta_cobro, " +
			"cc.convenio, " +
			"c.nombre AS descripcion || ' - ' ||  c.codigo," +
			"ap.unidad_economica AS unidad_economica," +
			"ap.identif_compania AS identif_compania," +
			"ap.identif_plan AS identif_plan " +
			"FROM cuentas_cobro cc " +
			"INNER JOIN convenios c ON (c.codigo = cc.convenio) " +
			"INNER JOIN archivo_plano_colsanitas ap ON (ap.convenio = cc.convenio)" +			
			"WHERE cc.institucion = ? " +
			"AND (cc.estado = '"+ConstantesBD.codigoEstadoCarteraGenerado+"' OR cc.estado ='"+ConstantesBD.codigoEstadoCarteraRadicada+"') " +
			"AND cc.numero_cuenta_cobro = ?  " +
			"AND cc.convenio IN (SELECT ap.convenio FROM archivo_plano_colsanitas ap WHERE ap.institucion = ? )";			
	
	
	//Insertar en Historial de Archivos Planos
	public static String strCadenaInsercionHistorialArchivosPlanos = "INSERT INTO historial_busq_archivoplano VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Consulta la informacion de un convenio parametrizado en archivo plano colsanitas
	 * */
	public static String strConsultaConvenioParamBasico = "SELECT " +			
			"unidad_economica," +
			"identif_compania," +
			"identif_plan " +
			"FROM archivo_plano_colsanitas " +
			"WHERE convenio = ? " +
			"AND institucion = ? ";
	
	
	/**
	 * Consulta la informacion basica de la institucion
	 * */
	public static String strConsultaInstitucion = "SELECT " +
			"codigo, " +
			"nit," +
			"razon_social " +
			"FROM instituciones " +
			"WHERE codigo = ? ";
	
	//*****************************CONSULTAS PARA LA GENERACIÓN DE LOS ARCHIVOS PLANOS COLSANITAS*******************************
	
	//Consulta de Informacion a partir del Numero de Envio
	public static String strConsultaFacturas = "SELECT " +
			"f.cod_paciente," +
			"getidpaciente(f.cod_paciente) AS id_paciente," +
			"getnombrepersona(f.cod_paciente) AS nombre_paciente," +
			"f.cuenta," +
			"f.codigo," +
			"f.consecutivo_factura, " +
			"CASE WHEN f.pref_factura IS NULL THEN '' ELSE f.pref_factura END AS pref_factura," +
			"CASE WHEN f.valor_total IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE f.valor_total END AS valor_total," +
			"f.tipo_monto," +
			"CASE WHEN f.valor_bruto_pac IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE f.valor_bruto_pac END AS valor_bruto_pac, "+
			"CASE WHEN getFechaPlanoColsa(f.cuenta,'ing') IS NULL THEN '' ELSE to_char(getFechaPlanoColsa(f.cuenta,'ing'),'YYYYMMDD') END AS fecha_ingreso,"+
			"CASE WHEN getFechaPlanoColsa(f.cuenta,'sal') IS NULL THEN '' ELSE to_char(getFechaPlanoColsa(f.cuenta,'sal'),'YYYYMMDD') END AS fecha_salida,"+			
			"CASE WHEN sc.numero_solicitud_volante IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE sc.numero_solicitud_volante END AS numero_solicitud_volante," +
			"to_char(f.fecha,'YYYYMMDD') AS fecha," +
			"CASE WHEN vi.identificador IS NULL THEN '' ELSE btrim(vi.identificador) END AS identificador, " +
			"vi.nombre AS nombre_viaingreso " +			
			"FROM facturas f " +
			"INNER JOIN sub_cuentas sc ON (f.sub_cuenta = sc.sub_cuenta) " +
			"INNER JOIN vias_ingreso vi ON (vi.codigo = f.via_ingreso) " ;		
	
	//**************************************************************************************************************************
		
	
	
	//********************Metodos
	
	/**
	 * Consulta el ultimo Registro del Historial de Archivos Planos
	 * @param Connection con
	 * @param int institucion
	 * */
	public static HashMap getUltimoHistorialArchivosPlanos(Connection con,int institucion)
	{			
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strCadenaConsultaHistorialArchivosPlanos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1,institucion);
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,true);
			ps.close();
			return mapaRetorno;
			
		}
		catch (SQLException e) {
			e.printStackTrace();				
		}
		
		return null;
	}	
	
	
	/**
	 * Consulta la ruta del ultimo Registro del Historial de Archivos Planos
	 * @param Connection con
	 * @param int institucion
	 * */
	public static ArrayList<HashMap<String,Object>> getConveniosArchivosPlanos(Connection con,int institucion)
	{			
		try
		{
			ArrayList<HashMap<String,Object>> resultado = new ArrayList<HashMap<String,Object>>();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultaConveniosParametrica,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ResultSetDecorator rs;			
			ps.setObject(1,institucion);
			
			rs =new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				HashMap<String,Object> mapa = new HashMap<String, Object>();
				mapa.put("convenio",rs.getObject(1));
				mapa.put("descripcion",rs.getObject(2));
				resultado.add(mapa);
			}
			
			return resultado;
			
		}
		catch (SQLException e) {
			e.printStackTrace();				
		}
		
		return null;	
	}
	
	
	/**
	 * Consulta los convenios parametrizados en Archivo Plano Colsanitas
	 * @param Connection con
	 * @param int institucion
	 * */
	public static String getUltimaRutaHistorialArchivosPlanos(Connection con,int institucion)
	{			
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultaRutaHistorialArchivosPlanos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs;
			ps.setInt(1,institucion);
			
			rs =new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
				return rs.getString(1);			
			
		}
		catch (SQLException e) {
			e.printStackTrace();				
		}
		
		return "";
	}
	
	
	
	/**
	 * Insertar en Historial de Archivos Planos
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static boolean setHistorialArhivosPlanos(Connection con,HashMap parametros)
	{
		try
		{				
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strCadenaInsercionHistorialArchivosPlanos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));		
			
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			
			if(parametros.get("convenio").equals(""))
			{
				ps.setNull(2,Types.NULL);				
			}
			else				
				ps.setInt(2,Utilidades.convertirAEntero(parametros.get("convenio").toString()));
			
			
			if(parametros.get("numeroEnvio").equals(""))
				ps.setNull(3,Types.NULL);
			else			
				ps.setString(3,parametros.get("numeroEnvio").toString());
			
			if(parametros.get("numeroCuentaCobro").equals(""))
				ps.setNull(4,Types.NULL);
			else				
				ps.setDouble(4,Utilidades.convertirADouble(parametros.get("numeroCuentaCobro").toString()));
			
			if(parametros.get("fechaInicial").equals(""))
				ps.setNull(5,Types.NULL);
			else			
				ps.setDate(5,Date.valueOf(parametros.get("fechaInicialABD").toString()));
			
			if(parametros.get("fechaFinal").equals(""))
				ps.setNull(6,Types.NULL);
			else
				ps.setDate(6,Date.valueOf(parametros.get("fechaFinalABD").toString()));			
						
			ps.setDate(7,Date.valueOf(parametros.get("fechaEnvioABD").toString()));
			
			ps.setInt(8,Utilidades.convertirAEntero(parametros.get("secuencia").toString()));
			ps.setString(9,parametros.get("ruta").toString());
			ps.setString(10,parametros.get("usuarioModifica").toString());
			ps.setDate(11,Date.valueOf(parametros.get("fechaModifica").toString()));
			ps.setString(12,parametros.get("horaModifica").toString());
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	/**
	 * Consulta las cuentas de cobro
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static HashMap getCuentasCobro(Connection con, HashMap parametros)
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultaCuentasCobro,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setInt(1,Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			ps.setDouble(2,Utilidades.convertirADouble(parametros.get("cuentaCobro").toString()));
			ps.setInt(3,Utilidades.convertirAEntero(parametros.get("institucion").toString()));
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,true);
			ps.close();
			return mapaRetorno;
			
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	

	
	/**
	 * Consulta las facturas para la generacion del archivo Plano Colsanitas
	 * @param Connection con 
	 * @param HashMap parametros
	 * @param Base de la Consulta, indica con que parametros se realizara la busqueda (1. a partir del numero de Envio, 
	 * 2. a partir del convenio, 
	 * 3. a partir de la cuenta de cobro)
	 * */
	public static ArrayList<DtoArchivoPlanoColsa> getFacturasArchivoPlano(Connection con, HashMap parametros,int baseConsulta)
	{
		String cadena = strConsultaFacturas;
		
		ArrayList<DtoArchivoPlanoColsa> array = new ArrayList<DtoArchivoPlanoColsa>();
		DtoArchivoPlanoColsa dto;
		
		ResultSetDecorator rs;
		
		switch (baseConsulta) {
		case 1:
			cadena+= "WHERE f.consecutivo_factura IN ("+parametros.get("codigoFactura")+") "+
			"AND f.convenio = " +parametros.get("convenio")+" "+
			"AND f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" "+
			"ORDER BY f.consecutivo_factura ASC ";
			break;
		case 2:
			cadena+= "WHERE f.convenio = "+parametros.get("convenio")+" "+
			"AND f.fecha BETWEEN '"+parametros.get("fechaInicialABD")+"' AND '"+parametros.get("fechaFinalABD")+"' "+
			"AND f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" " +
			"ORDER BY f.consecutivo_factura ASC ";
			break;
		case 3:
			cadena+= "WHERE f.convenio = "+parametros.get("convenio")+" "+
			"AND f.numero_cuenta_cobro = "+parametros.get("numeroCuentaCobro")+" "+
			"AND f.estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada+" " +
			"ORDER BY f.consecutivo_factura ASC ";
			break;
		}			
		
	
		try
		{				
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			rs =new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				dto = new DtoArchivoPlanoColsa();
				dto.setCodigoPaciente(rs.getString(1));
				dto.setIdentificacionPaciente(rs.getString(2));
				dto.setNombrePaciente(rs.getString(3));				
				dto.setCuenta(rs.getString(4));
				dto.setCodigoFactura(rs.getString(5));
				dto.setNumeroFactura(rs.getString(6));
				dto.setPrefijoFactura(rs.getString(7));
				dto.setValorTotalFactura(rs.getDouble(8));
				dto.setTipoMonto(rs.getString(9));
				dto.setValorRecaudoCuotasModeradoras(rs.getDouble(10));
				dto.setValorRecaudoCopagos(rs.getDouble(10));
				dto.setFechaIngreso(rs.getString(11));
				dto.setFechaSalida(rs.getString(12));
				dto.setNumeroSolicitudVolante(rs.getString(13));
				dto.setFechaElaboracionFactura(rs.getString(14));
				dto.setIdentificador(rs.getString(15));
				dto.setDescripcionViaIngreso(rs.getString(16));
				array.add(dto);
			}
			
			if(array.size() <= 0)
				logger.info("valor de la cadena sql de consulta de facturas >>  "+cadena);
			
			return array;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		
		return array;		
	}	
	

	
		
	/**
	 * Consulta datos basicos de los convenios ingresados en la parametrica Archivo Planos Colsanitas
	 * @param Connection con
	 * @param int convenio
	 * @param int institucion
	 * */
	public static InfoDatosString getConvenioBasicoParametrizacion(Connection con, int convenio,int institucion)
	{
		InfoDatosString info = new InfoDatosString();		
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultaConvenioParamBasico,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs;
			ps.setInt(1,convenio);
			ps.setInt(2,institucion);
			rs =new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				info.setCodigo(rs.getString(1));
				info.setNombre(rs.getString(2));
				info.setDescripcion(rs.getString(3));				
			}
			
			return info;
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return info;
	}
	
	
	/**
	 * Consulta datos basicos de las instituciones
	 * @param Connection con
	 * @param int institucion
	 * */
	public static InfoDatosString getInfoBasicaInstitucion(Connection con,int institucion)
	{
		InfoDatosString info = new InfoDatosString();		
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(strConsultaInstitucion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs;			
			ps.setInt(1,institucion);
			rs =new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				info.setCodigo(rs.getString(1)); //codigo 
				info.setNombre(rs.getString(2)); //nit
				info.setDescripcion(rs.getString(3)); //razon social				
			}
			
			return info;
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return info;
	}
}