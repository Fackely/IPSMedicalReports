package com.princetonsa.dao.sqlbase.facturacion;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.facturacion.EntidadesSubContratadas;



/**
 * 
 * @author Jhony Alexander Duque A.
 * 02/01/2008
 *
 */

public class SqlBaseEntidadesSubContratadasDao
{
	/**
	 * para manejar el log en la clase
	 */
	static Logger logger = Logger.getLogger(SqlBaseEntidadesSubContratadasDao.class);

	/**
	 * Cadena de insercion de entidades subcontratadas
	 */
	private static final String strCadenaInsercionEntidadesSubContratadas = "INSERT INTO entidades_subcontratadas (codigo," +
			" institucion,razon_social,tercero,codigo_minsalud,direccion,telefono,persona_contactar,observaciones,usuario_modifica," +
			" fecha_modifica, hora_modifica, codigo_pk,activo,cuenta_cxp,dias_vencimiento_factura, permite_estancia_pacientes) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Cadena de insercion del detalle de entidades sub contratadas
	 */
	private static final String strCadenaInsercionDetalle = "INSERT INTO det_entidades_subcontratadas  (codigo_ent_sub," +
			"fecha_inicial,fecha_final,esq_tar_serv,esq_tar_inv,usuario_modifica,fecha_modifica,hora_modifica,codigo) 	VALUES" +
			"(?,?,?,?,?,?,?,?,?) ";
		
	/**
	 * Cadena de Eliminacion de entidades subcontratadas
	 */
	private static final String strCadenaEliminacionEntidadesSubContratadas = "DELETE FROM entidades_subcontratadas WHERE codigo_pk=? AND institucion=?";
	
	/**
	 * Cadena de Eliminacion de entidades subcontratadas
	 */
	private static final String strCadenaEliminaciondetalle = "DELETE FROM det_entidades_subcontratadas WHERE codigo=?";
	
	/**
	 * Cadena de Modificacion de  entidades subcontratadas
	 */
	private static final String strCadenaModificacionEntidadesSubContratadas ="UPDATE entidades_subcontratadas SET  codigo=?," +
			" razon_social=?,tercero=?,codigo_minsalud=?,direccion=?,telefono=?,persona_contactar=?,observaciones=?,usuario_modifica=?," +
			" fecha_modifica=?, hora_modifica=?,dias_vencimiento_factura=?,activo=?,cuenta_cxp=?, permite_estancia_pacientes= ? WHERE codigo_pk=? AND institucion=?";
	
	/**
	 * Cadena de Modificacion de  det_entidades subcontratadas
	 */
	private static final String strCadenaModificaciondetalle ="UPDATE det_entidades_subcontratadas SET  fecha_inicial=?," +
			" fecha_final=?,esq_tar_serv=?,esq_tar_inv=?,usuario_modifica=?,fecha_modifica=?, hora_modifica=? WHERE codigo=?";
	
	
	/**
     * Cadena para la busqueda de la entidad en la tabla terceros
     */
    private static final String busquedaEntidadStr="SELECT codigo, numero_identificacion, descripcion FROM terceros WHERE 1=1 ";
	
	/**
	 * Cadena de Consulta de entidades subcontratadas
	 */
	private static final String strCadenaConsultaEntidadesSubContratadas  = "SELECT " +
			" esc.codigo_pk AS codigo_pk0," + 
			" esc.codigo AS codigo1," + 
			" esc.institucion AS institucion2," +
			" esc.razon_social AS razon_social3," +
			" esc.tercero AS tercero4," +
			" esc.codigo_minsalud AS codigo_minsalud5," +
			" esc.direccion AS direccion6," +
			" esc.telefono AS telefono7," +
			" esc.persona_contactar AS persona_contactar8," +
			" esc.observaciones AS observaciones9," +
			" t.numero_identificacion || ' - '||t.descripcion AS nombre_tercero20," +
			" esc.dias_vencimiento_factura AS diasVenFacturas," +
			" esc.cuenta_cxp AS cuentacxp," +
			" esc.activo AS activo," +
			" cc.cuenta_contable || ' - ' || cc.descripcion  AS cuentaContable," +
			" cc.anio_vigencia || ' - ' || cc.cuenta_contable AS cuentaContableAno," +
//Se elimina este campo por Incidencia Mantis 1221
//			" esc.centro_atencion_cub AS codigo_centro_atencion_cub," + 
			" esc.permite_estancia_pacientes AS estancia35," +  
			" administracion.getnomcentroatencion(esc.centro_atencion_cub) AS centro_atencion_cub," +
			" '"+ConstantesBD.acronimoSi+"' AS esta_bd10 " +
			" FROM entidades_subcontratadas esc " +
			" INNER JOIN terceros t ON (t.codigo=esc.tercero)" +
			" left outer JOIN cuentas_contables cc ON (cc.codigo=esc.cuenta_cxp)" +
			
			" WHERE esc.institucion = ? AND esc.codigo_pk > 0";
	
	/**
	 * String de consulta de detalles de vigencias
	 */
	private static final String strCadenaConsultaDetalleVigencias = " SELECT " +
			" detsc.codigo AS codigo_det14," +
			" detsc.codigo_ent_sub AS codigo_ent_sub15," +
			" to_char(detsc.fecha_inicial,'DD/MM/YYYY') AS fecha_inicial16," +
			" to_char(detsc.fecha_final,'DD/MM/YYYY') AS fecha_final17," +
			" detsc.esq_tar_serv AS esq_tar_serv18," +
			" detsc.esq_tar_inv AS esq_tar_inv19," +
			"'"+ConstantesBD.acronimoSi+"' AS esta_bd21 " +
			" FROM det_entidades_subcontratadas detsc ";
	

	private static final String strCadenaConsultaDescripcionEntidadSubXIngreso = " SELECT getDescripEntidadSubXingreso(?)";
	
	
	
//Indices para manejar los valores de las consultas y los datos digitados por el usuario
private static final String [] indices = EntidadesSubContratadas.indices;


/**
 * Metodo encargado de consultar la descripcion
 * de la entidad subcontratada filtrando por el
 * ingreso
 * @param connection
 * @param ingreso
 * @return Descripcion entidad subcontratada.
 */
public static String getDescripcionEntidadSubXIngreso (Connection connection, String ingreso)
{
	String cadena = strCadenaConsultaDescripcionEntidadSubXIngreso;
	String result="";
	try 
	{
		PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
		ps.setObject(1, ingreso);
		ResultSetDecorator rs =new ResultSetDecorator(ps.executeQuery());
		
		if(rs.next())
			result = rs.getString(1);
		
	} 
	catch (SQLException e)
	{
		logger.info("\n problema consultando descripcion de entidad subcontratada "+e);
	}	
	
	return result;
}


/**
 * Metodo encargado de buscar
 * los datos de la tabla entidades_subcontratadas
 * @param connection
 * @param criterios
 * ------------------------------
 * 	KEY DEL MAPA CRITERIOS
 * ------------------------------
 * -- codigoPk0_
 * -- institucion2_
 * -- codigo1_
 * @return
 * ----------------------------------------------
 * EL MAPA QUE RETORNA TIENE LOS SIGUIENTES KEY
 * ----------------------------------------------
 * -- codigoPk0_
 * -- codigo1_
 * -- institucion2_
 * -- razonSocial3_
 * -- tercero4_
 * -- codigoMinsalud5_
 * -- direccion6_
 * -- telefono7_
 * -- personaContactar8_
 * -- observaciones9_
 * -- estaBd10_
 * -- nombreTercero20_
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public static HashMap buscar0 (Connection connection,HashMap criterios)
{
	logger.info("\n\n***********************************************");
	logger.info("\n ::::::ENTRE A BUSCAR0 SQL::::::::");
	logger.info("\n hashmap al entrar ==> "+criterios);
	logger.info("\n ***********************************************");
	
	String cadena = strCadenaConsultaEntidadesSubContratadas,where=" ";
	
	//1)se agrega el codigo pk
	if (criterios.containsKey(indices[0]) && !criterios.get(indices[0]).equals("") && !(criterios.get(indices[0])+"").equals("-1"))
		where +=" AND esc.codigo_pk="+criterios.get(indices[0]);
	
	//2)se agrega el codigo
	if (criterios.containsKey(indices[1]) && !criterios.get(indices[1]).equals(null) && !criterios.get(indices[1]).equals("") && !(criterios.get(indices[1])+"").equals("-1"))
		where +=" AND esc.codigo='"+criterios.get(indices[1])+"'";
	
	//3)se agrega la razon social
	if (criterios.containsKey(indices[3]) && !criterios.get(indices[3]).equals("") && !(criterios.get(indices[3])+"").equals("-1"))
		where+=" AND UPPER(esc.razon_social) LIKE UPPER('%"+criterios.get(indices[3])+"%')";
		
	//4)se agrega el tipo Id
	if (criterios.containsKey(indices[4]) && !criterios.get(indices[4]).equals("") && !(criterios.get(indices[4])+"").equals("-1"))
		where +=" AND esc.tercero="+criterios.get(indices[4]);
	
	HashMap mapa = new HashMap ();
	cadena+=where+=" ORDER BY razon_social";
	
	logger.info("Cadena : "+cadena);

	
	try 
	{
		PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
		//se  filtra por la institucion
		ps.setInt(1, Utilidades.convertirAEntero(criterios.get(indices[2])+""));
		
		mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
		
	}
	catch (SQLException e) 
	{
		logger.info("\n Problema Buscando los datos en la tabla entidades_subcontratadas "+e);
	}
	
	mapa.put("INDICES_MAPA", indices);
	
	return mapa;
}

/**
 * Metodo encargado de Buscar los detalles de
 * vigencias
 * @param connection
 * @param criterios
 * ----------------------------------------
 * KEY'S DEL HASHMAP CRITERIOS
 * ----------------------------------------
 * -- codigoPk0_
 * -- institucion2_
 * 
 * @return
 * -----------------------------------------
 * 		KEYS DEL MAPA QUE RETORNA
 * -----------------------------------------
 * -- codigoDet14_
 * -- codigoEntSub15_
 * -- fechaInicial16_
 * -- fechaFinal17_
 * -- esqTarServ18_
 * -- esqTarInv19_
 * -- estaBd10_
 * 
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public static HashMap buscar1 (Connection connection,HashMap criterios)
{
	//logger.info("\n\n***********************************************");
	logger.info("\n ::::::ENTRE A BUSCAR1 SQL::::::::");
	//logger.info("\n hashmap al entrar ==> "+criterios);
	//logger.info("\n ***********************************************");
	
	String cadena = strCadenaConsultaDetalleVigencias,where="WHERE 1=1 ",inner="";
	
	
	
	
	//1) codigo digitado
	if (criterios.containsKey(indices[1]) && !criterios.get(indices[1]).equals("") && !(criterios.get(indices[1])+"").equals("-1") && UtilidadCadena.noEsVacio(criterios.get(indices[1])+""))
	{
		inner+=" INNER JOIN entidades_subcontratadas esc ON (esc.codigo_pk=detsc.codigo_ent_sub) ";
		where +=" AND esc.codigo='"+criterios.get(indices[1])+"'";
	}
//		2) codigoEntSub15_
		if (criterios.containsKey(indices[15]) && !criterios.get(indices[15]).equals("") && !(criterios.get(indices[15])+"").equals("-1") && UtilidadCadena.noEsVacio(criterios.get(indices[15])+""))
		{
			inner+=" INNER JOIN entidades_subcontratadas esc ON (esc.codigo_pk=detsc.codigo_ent_sub) ";
			where +=" AND esc.codigo_pk="+criterios.get(indices[15]);
		}
		//	3) instiucion
		if (criterios.containsKey(indices[2]) && !criterios.get(indices[2]).equals("") && !(criterios.get(indices[2])+"").equals("-1"))
			where +=" AND esc.institucion="+criterios.get(indices[2]);
		
	HashMap mapa = new HashMap ();
	cadena+=inner+=where;
//	logger.info("\n cadena de busqueda "+cadena);
	try 
	{
		PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
	
		
		mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
		
	}
	catch (SQLException e) 
	{
		logger.info("\n Problema Buscando los datos en la tabla det_entidades_subcontratadas "+e);
	}
	//logger.info("\n el valor al salir es ==> "+mapa);
	mapa.put("INDICES_MAPA", indices);
	
	return mapa;
}



	
/**
 * Metodo encargado de insertar los datos en 
 * la tabla entidades_subcontratadas
 * @param connection
 * @param datos
 * ---------------------------------
 * 	 	KEY'S DEL HASHMAP DATOS
 * ---------------------------------
 * -- codigo1_ --> Requerido
 * -- institucion2_ --> Requerido 
 * -- razonSocial3_ --> Requerido
 * -- tercero4_ --> Requerido
 * -- codigoMinsalud5_ --> Requerido
 * -- direccion6_ --> Opcional
 * -- telefono7_ --> Opcional
 * -- personaContactar8_ --> Opcional
 * -- observaciones9_ --> Opcional
 * -- usuarioModifica11_ --> Requerido
 * @return
 */
@SuppressWarnings("rawtypes")
public static ResultadoBoolean insertar0 (Connection connection,HashMap datos)
{
	ResultadoBoolean resultado = new ResultadoBoolean(false,"");
	
	
	logger.info("\n\n***********************************************");
	logger.info("\n ::::::ENTRE A INSERTAR0 SQL::::::::");
	logger.info("\n hashmap al entrar ==> "+datos);
	logger.info("\n ***********************************************");
	/*{institucion2_0=2, codigoMinsalud5_0=111, tercero4_0=16085, codigo1_0=111, razonSocial3_0=11, diasVenFact=10,
	 *  cuentaCxp=3093, activo=true, usuarioModifica11_0=oscar, usuarios=sacostat}
	*/
	String cadena=strCadenaInsercionEntidadesSubContratadas;
	
	try 
	{
		PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
		logger.info("\n INSERT : "+cadena);
		logger.info("\n MAPA : "+datos);
		/**
		 * INSERT INTO entidades_subcontratadas (codigo," +
			" institucion,razon_social,tercero,codigo_minsalud,direccion,telefono,persona_contactar,observaciones,usuario_modifica," +
			" fecha_modifica, hora_modifica) values (?,?,?,?,?,?,?,?,?,?,?,?)
		 */
		Utilidades.imprimirMapa(datos);

		//1)el codigo digitado por el usuario
		ps.setString(1, datos.get(indices[1])+"");
		//2)la institucion
		ps.setInt(2, Utilidades.convertirAEntero(datos.get(indices[2])+""));
		//3)la razon social
		ps.setString(3, datos.get(indices[3])+"");
		//4)tercero
		ps.setInt(4, Utilidades.convertirAEntero(datos.get(indices[4])+""));
		//5)codigo_minsalud
		ps.setInt(5, Utilidades.convertirAEntero(datos.get(indices[5])+""));
		//6)direccion
		if (datos.containsKey(indices[6]) && !(datos.get(indices[6])+"").equals("") && !(datos.get(indices[6])+"").equals("-1"))
			ps.setString(6, datos.get(indices[6])+"");
		else
			ps.setNull(6, Types.VARCHAR);
		//7)telefono
		if (datos.containsKey(indices[7]) && !(datos.get(indices[7])+"").equals("") && !(datos.get(indices[7])+"").equals("-1"))
			ps.setString(7, datos.get(indices[7])+"");
		else
			ps.setNull(7, Types.VARCHAR);	
		//8)persona_contactar
		if (datos.containsKey(indices[8]) && !(datos.get(indices[8])+"").equals("") && !(datos.get(indices[8])+"").equals("-1"))
			ps.setString(8, datos.get(indices[8])+"");
		else
			ps.setNull(8, Types.VARCHAR);
		//8)observaciones
		if (datos.containsKey(indices[9]) && !(datos.get(indices[9])+"").equals("") && !(datos.get(indices[9])+"").equals("-1"))
			ps.setString(9, datos.get(indices[9])+"");
		else
			ps.setNull(9, Types.VARCHAR);
		
		//9)se agrega el usuario que modifica
		ps.setString(10, datos.get(indices[11])+"");
		//10)se agrega la feha modifica
		ps.setDate(11, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
		//11)se agrega la hora modifica
		ps.setString(12, UtilidadFecha.getHoraActual()+"");
		
		//codigo_pk
		int secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(connection, "seq_entidades_subcontratadas");
	
		ps.setDouble(13, secuencia);
		resultado.setDescripcion(secuencia+"");
		
		if(datos.get("activo").equals(true))
		{	
			ps.setString(14,ConstantesBD.acronimoSi);	
		}else
		{
			ps.setString(14,ConstantesBD.acronimoNo);
		}
		
		if(!datos.get("cuentaCxp").equals(""))
			ps.setString(15,datos.get("cuentaCxp")+"");
		else
			ps.setNull(15,Types.VARCHAR);

		if(!datos.get("diasVenFact").equals(""))
			ps.setString(16,datos.get("diasVenFact")+"");
		else
			ps.setNull(16,Types.INTEGER);
		
//Se elimina este campo por Incidencia Mantis 1221
//		ps.setInt(17, Utilidades.convertirAEntero(datos.get("centroAtencionCub")+""));
		
		//35)permite estancia pacientes
		if (datos.containsKey(indices[35]) && !(datos.get(indices[35])+"").equals("") && !(datos.get(indices[35])+"").equals("-1"))
			ps.setString(17, datos.get(indices[35])+"");
		else
			ps.setNull(17, Types.VARCHAR);
		
		
		if (ps.executeUpdate()>0){
			resultado.setResultado(true);
			resultado.setDescripcion(secuencia+"");
		}			
		
		return resultado;

	} 
	catch (SQLException e) 
	{
		logger.info("\n Problema insertando los datos en la tabla entidades_subcontratadas "+e);
	}
	resultado.setDescripcion(ConstantesBD.codigoNuncaValido+"");
	return resultado;
}


/**
 * Metodo encargado de insertar de los datos en 
 * la tabla det_entidades_subcontratadas
 * @param connection
 * @param datos
 * -----------------------------------
 * KEY'S DEL HASHMAP DATOS
 * -----------------------------------
 * -- codigoDet14_ --> Requerido
 * -- codigoEntSub15_ --> Requerido
 * -- fechaInicial16_ --> Requerido
 * -- fechaFinal17_ --> Requerido
 * -- esqTarServ18_ --> Requerido
 * -- esqTarInv19_ --> Requerido
 * 
 * @return
 */
@SuppressWarnings("rawtypes")
public static boolean insertar1 (Connection connection, HashMap datos)
{
	
	logger.info("\n\n***********************************************");
	logger.info("\n ::::::ENTRE A INSERTAR1 SQL::::::::");
	logger.info("\n hashmap al entrar ==> "+datos);
	logger.info("\n ***********************************************");
	
	String cadena=strCadenaInsercionDetalle;
	
	try 
	{
		PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
		
		
		/**
		 * INSERT INTO det_entidades_subcontratadas  (codigo_ent_sub," +
			"fecha_inicial,fecha_final,esq_tar_serv,esq_tar_inv,usuario_modifica,fecha_modifica,hora_modifica,codigo) 	VALUES" +
			"(?,?,?,?,?,?,?,?,?) 
		 */
		
		//1)codigo entidad sub contrada
		ps.setDouble(1, Utilidades.convertirADouble(datos.get(indices[15])+""));
		//2)fecha inicial
		ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(datos.get(indices[16])+"")));
		//3)fecha final
		ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(datos.get(indices[17])+"")));
		//4)esquema tarifario servicio
		ps.setInt(4, Utilidades.convertirAEntero(datos.get(indices[18])+""));
		//5)esquema tatifario inventario
		ps.setInt(5, Utilidades.convertirAEntero(datos.get(indices[19])+""));
		//6) Usuario modifica
		ps.setString(6, datos.get(indices[11])+"");
		//7) fecha modifica
		ps.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
		//8) hora modifica
		ps.setString(8, UtilidadFecha.getHoraActual());
		//9) codigo
		ps.setInt(9,UtilidadBD.obtenerSiguienteValorSecuencia(connection, "facturacion.seq_det_entid_subcontratadas"));
		
	
		if (ps.executeUpdate()>0)
			return true;

		
	} 
	catch (SQLException e)
	{
		logger.info("\n Problema insertando los datos en la tabla det_entidades_subcontratadas "+e);
	}
	
	
	
	return false;
}

@SuppressWarnings("rawtypes")
public static boolean insertarViasCentros (Connection connection, HashMap datos)
{
	
	logger.info("\n\n***********************************************");
	logger.info("\n ::::::ENTRE A INSERTARVIASCENTROS SQL::::::::");
	logger.info("\n hashmap al entrar ==> "+datos);
	logger.info("\n ***********************************************");
	
	String cadena=strCadenaInsercionDetalle;
	
	try 
	{
		PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
		
		
		/**
		 * INSERT INTO estancia_via_ing_centro_costo (codigo_pk," +
			"via_ingreso, centro_costo, entidad_subcontratada) 	VALUES" +
			"(?,?,?,?) 
		 */
		
		//19) viasIngreso
		ps.setInt(19, Utilidades.convertirAEntero(datos.get(indices[36])+""));
		//20) centrosCosto
		ps.setInt(20, Utilidades.convertirAEntero(datos.get(indices[37])+""));
		//1)codigo entidad sub contrada
		ps.setString(1, datos.get(indices[1])+"");
		
		if (ps.executeUpdate()>0)
			return true;

		
	} 
	catch (SQLException e)
	{
		logger.info("\n Problema insertando los datos en la tabla estancia_via_ing_centro_costo "+e);
	}
	
	return false;
}

/**
 * Metodo encargado de eliminar los datos de la tabla
 * entidades_subcontratadas y sus respectivos detalles.
 * @param connection
 * @param criterios
 * ---------------------------------
 * KEY'S DEL HASHMAP CRITERIOS
 * -----------------------------------
 * -- codigoPk0_
 * -- institucion2_
 * @return
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public static boolean eliminarEntidadConDetalle (Connection connection, HashMap criterios)
{
	logger.info("\n ::::: ENTRE A ELIMINAR ENTIDAD Y DETALLE :::::: ");
	
	HashMap detalle = new HashMap ();
	boolean operacionTrue=true;
	
	
	detalle.putAll(buscar1(connection, criterios));
	
	logger.info("\n el valor del detalle "+ detalle);
	
	for (int i=0;i<Integer.parseInt(detalle.get("numRegistros")+"") && operacionTrue ;i++)
	{
		HashMap crit = new HashMap ();
			crit.put(indices[14], detalle.get(indices[14]+i));
		
		//elimina
			
		operacionTrue=eliminar1(connection, crit);
	
	}
	
	if (operacionTrue)
	{
		if (eliminar0(connection, criterios));
			return true;
	}
	
	
	return false;
}


/**
 * Metodo encargado de Eliminar los datos de
 * la tabla entidades_subcontratadas
 * @param connection
 * @param criterios
 * -------------------------------------
 * 		KEY'S DEL HASHMAP CRITERIOS
 * -------------------------------------
 * -- codigoPk0_
 * -- institucion2_
 * @return
 */
@SuppressWarnings("rawtypes")
public static boolean eliminar0 (Connection connection, HashMap criterios)
{
	logger.info("\n ::::: ENTRE A ELIMINAR0 :::::: ");
	logger.info("\n ::::: CRITERIOS :::::: "+criterios);
	
	String cadena = strCadenaEliminacionEntidadesSubContratadas;
	try 
	{
	
		PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
		
		logger.info("\n\n\n CADENA ELIMINAR = "+cadena);
		/**
		 * DELETE FROM entidades_subcontratadas WHERE codigo_pk=? AND institucion=?"
		 */
		
		//codigopk0_
		ps.setDouble(1, Utilidades.convertirADouble(criterios.get(indices[15])+""));
		logger.info("\n Utilidades.convertirADouble(criterios.get(indices[15])+) : "+Utilidades.convertirADouble(criterios.get(indices[15])+""));
		//institucion
		ps.setInt(2, Utilidades.convertirAEntero(criterios.get(indices[2])+""));
		
		logger.info("\n Utilidades.convertirAEntero(criterios.get(indices[2])+) : "+Utilidades.convertirAEntero(criterios.get(indices[2])+""));
		
		if (ps.executeUpdate()>0)
			return true;
		
		
	} 
	catch (SQLException e)
	{
		logger.info("\n Problema eliminando los datos en la tabla entidades_subcontratadas "+e);
	}
	
	return false;
	
}


/**
 * Metodo encargado de eliminar los datos de la 
 * tabla det_entidades_subcontratadas	
 * @param connection
 * @param criterios
 * ----------------------------
 * KEY'S DEL HASHMAP CRITERIOS
 * ----------------------------
 *  -- codigoDet14_
 * @return
 */
@SuppressWarnings("rawtypes")
public static boolean eliminar1 (Connection connection, HashMap criterios)
{
	//logger.info("\n\n***********************************************");
	logger.info("\n ::::::ENTRE A ELIMINAR1 SQL::::::::");
/*	logger.info("\n hashmap al entrar ==> "+criterios );
	logger.info("\n ***********************************************");
	*/
	
	String cadena = strCadenaEliminaciondetalle;
	
	
	try
	{
		PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		//codigo detalle
		ps.setDouble(1, Utilidades.convertirADouble(criterios.get(indices[14])+""));
		logger.info("Utilidades.convertirADouble(criterios.get(indices[14]): "+Utilidades.convertirADouble(criterios.get(indices[14])+""));
		
		if (ps.executeUpdate()>0)
			return true;
	}
	catch (SQLException e)
	{
		logger.info("\n Problema eliminando los datos en la tabla det_entidades_subcontratadas "+e);	
	}
	
	return false;
}

/**
 * Metodo encargado de modificar los datos del 
 * detalle 
 * @param connection
 * @param criterios
 * -------------------------------------------
 * 		KEY'S DEL HASHMAP CRITERIOS
 * -------------------------------------------
 * -- fechaInicial16_
 * -- fechaFinal17_
 * -- esqTarServ18_
 * -- esqTarInv19_
 * -- usuarioModifica11_0
 * -- codigoDet14_
 * @return
 */
@SuppressWarnings("rawtypes")
public static boolean modificar1 (Connection connection, HashMap criterios)
{
	logger.info("\n\n***********************************************");
	logger.info("\n ::::::ENTRE A MODIFICAR1 SQL::::::::");
	logger.info("\n hashmap al entrar ==> "+criterios );
	logger.info("\n ***********************************************");
	
	
	String cadena = strCadenaModificaciondetalle;
	
	try
	{
		PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
		/**
		 * UPDATE det_entidades_subcontratadas SET  
		 * fecha_inicial=?,
		 * fecha_final=?,
		 * esq_tar_serv=?,
		 * esq_tar_inv=?,
		 * usuario_modifica=?,
		 * fecha_modifica=?, 
		 * hora_modifica=? WHERE codigo=?
		 */
		
		//1)fecha inicial
		ps.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(criterios.get(indices[16])+"")));
		//2)fecha final
		ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(criterios.get(indices[17])+"")));
	
		
		//3)esquema tarifario servicio
		ps.setInt(3, Utilidades.convertirAEntero(criterios.get(indices[18])+""));
		//4)esquema tatifario inventario
		ps.setInt(4, Utilidades.convertirAEntero(criterios.get(indices[19])+""));
		//5) Usuario modifica
		ps.setString(5, criterios.get(indices[11])+"");
		//6) fecha modifica
		ps.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
		//7) hora modifica
		ps.setString(7, UtilidadFecha.getHoraActual());
		
		ps.setDouble(8, Utilidades.convertirADouble(criterios.get(indices[14])+""));
	
		if (ps.executeUpdate()>0)
			return true;
	}
	catch (SQLException e)
	{
		logger.info("\n Problema modificando los datos en la tabla det_entidades_subcontratadas "+e);	
	}
	
	return false;
}



/**
 * metodo encargado de modificar los datos 
 * de la tabla entidades_subcontratadas
 * @param connection
 * @param datos
 * -----------------------------------------
 * 		  KEY'S DEL HASHMAP DATOS
 * -----------------------------------------
 * -- codigo1_ --> Requerido
 * -- institucion2_ --> Requerido 
 * -- razonSocial3_ --> Requerido
 * -- tercero4_ --> Requerido
 * -- codigoMinsalud5_ --> Requerido
 * -- direccion6_ --> Opcional
 * -- telefono7_ --> Opcional
 * -- personaContactar8_ --> Opcional
 * -- observaciones9_ --> Opcional
 * -- usuarioModifica11_ --> Requerido
 * -- codigoPk0_  --> Requerido
 * @return
 */
@SuppressWarnings("rawtypes")
public static boolean modificar0 (Connection connection,HashMap datos)
{
	logger.info("\n\n***********************************************");
	logger.info("\n ::::::ENTRE A MODIFICAR0 SQL::::::::");
	logger.info("\n hashmap al entrar ==> "+datos);
	logger.info("\n ***********************************************");
	
	Utilidades.imprimirMapa(datos);
	String cadena=strCadenaModificacionEntidadesSubContratadas;
	
	
	/**
	 * codigo=?," +
			" razon_social=?,tercero=?,codigo_minsalud=?,dereccion=?,telefono=?,persona_contactar=?,observaciones=?,usuario_modifica=?," +
			" fecha_modifica=?, hora_modifica=?, centro_atencion_cub = ?  WHERE codigo_pk=? AND institucion=?";
	
	 */
	try 
	{
		PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
	logger.info("MODIFICAR : "+cadena);	
		/**
		 * UPDATE entidades_subcontratadas SET  codigo=?," +
			" razon_social=?,tercero=?,codigo_minsalud=?,direccion=?,telefono=?,persona_contactar=?,observaciones=?,usuario_modifica=?," +
			" fecha_modifica=?, hora_modifica=? WHERE codigo_pk=? AND institucion=?
		 */
		
		
		//1)el codigo digitado por el usuario
		ps.setString(1, datos.get(indices[1])+"");
		//2)la razon social
		ps.setString(2, datos.get(indices[3])+"");
		//3)tercero
		ps.setObject(3, datos.get(indices[4]));
		//4)codigo_minsalud
		ps.setString(4, datos.get(indices[5])+"");
		//5)direccion
		if (datos.containsKey(indices[6]) && !(datos.get(indices[6])+"").equals("") && !(datos.get(indices[6])+"").equals(ConstantesBD.codigoNuncaValido+""))
			ps.setString(5, datos.get(indices[6])+"");
		else
			ps.setNull(5, Types.VARCHAR);
		//6)telefono
		if (datos.containsKey(indices[7]) && !(datos.get(indices[7])+"").equals("") && !(datos.get(indices[7])+"").equals(ConstantesBD.codigoNuncaValido+""))
			ps.setString(6, datos.get(indices[7])+"");
		else
			ps.setNull(6, Types.VARCHAR);	
		//7)persona_contactar
		if (datos.containsKey(indices[8]) && !(datos.get(indices[8])+"").equals("") && !(datos.get(indices[8])+"").equals(ConstantesBD.codigoNuncaValido+""))
			ps.setString(7, datos.get(indices[8])+"");
		else
			ps.setNull(7, Types.VARCHAR);
		//8)observaciones
		if (datos.containsKey(indices[9]) && !(datos.get(indices[9])+"").equals("") && !(datos.get(indices[9])+"").equals(ConstantesBD.codigoNuncaValido+""))
			ps.setString(8, datos.get(indices[9])+"");
		else
			ps.setNull(8, Types.VARCHAR);
		
		//9)se agrega el usuario que modifica
		ps.setString(9, datos.get(indices[11])+"");
		//10)se agrega la feha modifica
		ps.setDate(10, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
		//11)se agrega la hora modifica
		ps.setString(11, UtilidadFecha.getHoraActual());
		
		ps.setString(12, datos.get("diasvenfacturas")+"");
			
		String temp=datos.get("activo")+"";	
		logger.info("temp:*"+temp+"*");
		if(UtilidadTexto.getBoolean(temp))
		{
			ps.setString(13,ConstantesBD.acronimoSi);	
		}
		else
		{
			ps.setString(13,ConstantesBD.acronimoNo);
		}
		//ps.setInt(14, Utilidades.convertirAEntero(datos.get("cuentaCxp")+""));
		
		if(!datos.get("cuentaCxp").equals(""))
			ps.setString(14,datos.get("cuentaCxp")+"");
		else
			ps.setNull(14,Types.VARCHAR);
	
//Se elimina este campo por Incidencia Mantis 1221		
//		ps.setInt(15, Utilidades.convertirAEntero(datos.get("centroAtencionCub")+""));
	
		//35)permite estancia pacientes
		if (datos.containsKey(indices[35]) && !(datos.get(indices[35])+"").equals("") && !(datos.get(indices[35])+"").equals("-1"))
			ps.setString(15, datos.get(indices[35])+"");
		else
			ps.setNull(15, Types.VARCHAR);
		
		
		//15) codigo pk
		ps.setDouble(16, Utilidades.convertirADouble(datos.get(indices[0])+""));
		
		//16-)la institucion
		ps.setInt(17, Utilidades.convertirAEntero(datos.get(indices[2])+""));
		
		
		
		Log4JManager.info("********************************"+datos.get(indices[35])+"");
		
		
		
		if (ps.executeUpdate()>0)
			return true;

	} 
	catch (SQLException e) 
	{
		logger.info("\n Problema modificando los datos en la tabla entidades_subcontratadas "+e);
	}

	return false;
}


/**
 * Busqueda del tercero, 
 * este metodo busca por numero de identificacion
 * o por la descripcion 
 */
@SuppressWarnings("rawtypes")
public static Collection buscarEntidad(Connection con, String numIdent, String descripcionEntidad)
{
	String cadena=busquedaEntidadStr;
	if(!numIdent.equals(""))
	{
		cadena+="AND numero_identificacion='"+numIdent+"' ";
	}
	if(!descripcionEntidad.equals(""))
	{
		cadena+="AND UPPER(descripcion) LIKE UPPER('%"+descripcionEntidad+"%')";
	}
	cadena+=" ORDER BY descripcion";
	try
	{
		PreparedStatementDecorator busqueda= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		return UtilidadBD.resultSet2Collection(new ResultSetDecorator(busqueda.executeQuery()));
	}
	catch(SQLException e)
	{
		logger.info("Error en la B&uacute;squeda de la Entidad "+e);
		return null;
	}
}

private static String insertarUsuarioEntidadSub="INSERT INTO facturacion.usuarios_entidad_sub " +
												"(consecutivo, entidad_subcontratada, usuario, " +
												"usuario_modifica, fecha_modifica, hora_modifica) " +
												"VALUES(?,?,?,?,CURRENT_DATE,? ) ";

@SuppressWarnings("rawtypes")
public static boolean insertarUsuarioEntidadSub( Connection con, HashMap criterios)
{
	String cadena=insertarUsuarioEntidadSub;
		
	try 
	{
		PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
		int valorseq=UtilidadBD.obtenerSiguienteValorSecuencia(con, "facturacion.seq_usuarios_entidades_sub");
		
		Utilidades.imprimirMapa(criterios);
		logger.info("valor secuencia >>>"+valorseq);
		
		ps.setInt(1, valorseq);
		ps.setInt(2, Utilidades.convertirAEntero(criterios.get("entidadSubcontratada")+""));
		ps.setString(3, criterios.get("usuario")+"");
		ps.setString(4, criterios.get("usuarioModifica")+"");
		ps.setString(5, UtilidadFecha.getHoraActual());
		
	
		if (ps.executeUpdate()>0)
			return true;

		
	} 
	catch (SQLException e)
	{
		logger.info("\n Problema insertando los datos en la tabla usuarios_entidad_sub "+e);
	}
	
	return false;
}


@SuppressWarnings("unchecked")
public static HashMap<String, Object> usuariosEntidadSub(Connection connection,
		int consecutivo) {
	
String consulta="SELECT ues.consecutivo, " +
					 "usu.login AS login, " +
					 "getnombrepersona(usu.codigo_persona) AS nombre          "+
					" FROM entidades_subcontratadas esc " +
					" left outer JOIN facturacion.usuarios_entidad_sub ues ON (ues.entidad_subcontratada=esc.codigo_pk)" +
					" left outer JOIN administracion.usuarios usu ON (ues.usuario=usu.login)" +
					" WHERE esc.codigo_pk = ?";
	
	HashMap<String, Object> resultados = new HashMap<String, Object>();
	
	PreparedStatementDecorator ps;
	
	try
	{
		ps= new PreparedStatementDecorator(connection.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		logger.info("consulta : "+consulta+" codigo : "+consecutivo);
		ps.setInt(1, consecutivo);
		resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
		
	
	}
	catch (SQLException e)
	{
		logger.info("\n\n ERROR. CONSULTANDO MOTIVOS ANULACION SQL------>>>>>>"+e);
		
		e.printStackTrace();
	}
	return resultados;	
}


public static boolean eliminarusu(Connection connection, String descripcion) {
	
	logger.info("CONSECUTIVO ---> "+descripcion);
	
	
	String cadena = "DELETE FROM facturacion.usuarios_entidad_sub WHERE consecutivo=? ";
	try 
	{
	
		PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
		
		ps.setString(1, descripcion);
		
		/**
		 * DELETE FROM entidades_subcontratadas WHERE codigo_pk=? AND institucion=?"
		 */
		
		//codigopk0_
	
		
		//institucion
		
		if (ps.executeUpdate()>0)
			return true;
		
		
	} 
	catch (SQLException e)
	{
		logger.info("\n Problema eliminando los datos en la tabla usuarios_entidad_sub "+e);
	}
	
	return false;
}

	
	
}