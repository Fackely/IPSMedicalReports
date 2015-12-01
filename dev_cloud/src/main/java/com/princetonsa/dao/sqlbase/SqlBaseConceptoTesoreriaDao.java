/*
 * Creado   Jun 16 2005
 * Modificado Sep 27 2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.RespuestaHashMap;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * @author Sebastián Gómez
 *
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la parametrización de Conceptos Ingreso Tesorería
 */
public class SqlBaseConceptoTesoreriaDao 
{
    /**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseConceptoTesoreriaDao.class);
	
	/**
	 * Cadena usada para cargar todos los conceptos de ingreso de tesoreria
	 * parametrizados por institución
	 */
	private static final String cargarConceptosTesoreriaStr="SELECT " +
		"c.codigo," +
		"c.descripcion," +
		"t.descripcion AS tipo_pago," +
		"c.rubro_presupuestal as rubropresupuestal,"+
		"CASE WHEN c.es_activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 'Activo' ELSE 'Inactivo' END AS activo " +
		"FROM conceptos_ing_tesoreria c " +
		"INNER JOIN tipo_ing_tesoreria t ON(t.codigo=c.codigo_tipo_ingreso) " +
		"WHERE " ;
		
		
	/**
	 *Cadena usada para insertar un concepto de ingreso de tesoreria 
	 */
	private static final String insertarConceptosTesoreriaStr="INSERT " +
			"INTO conceptos_ing_tesoreria " +
			"(codigo,descripcion,codigo_tipo_ingreso,valor,cuenta,tipo_docum_ingreso,tipo_docum_anulacion,codigo_centro_costo,nit_homologacion,institucion,es_activo,rubro_presupuestal) " +
			"VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
	
	/**
	 * Cadena para actualizar un concepto de ingreso de tesoreria
	 */
	private static final String actualizarConceptoTesoreriaStr="UPDATE " +
			"conceptos_ing_tesoreria SET " +
			"codigo=?, descripcion=?, codigo_tipo_ingreso=?, valor=?, cuenta=?, tipo_docum_ingreso=?, tipo_docum_anulacion=?, codigo_centro_costo=?, nit_homologacion=?, es_activo=?,rubro_presupuestal=? " +
			"WHERE codigo=? AND institucion=?";
	
	
	/**
	 * Cadena para eliminar un regitro de los conceptos de ingreso de tesoreria
	 */
	private static final String eliminarConceptoTesoreriaStr="DELETE FROM conceptos_ing_tesoreria where codigo=? and institucion=?"; 
	
	/**
	 * Cadena usada para cargar un concepto de ingreso de tesoreria
	 * parametrizado por institución
	 */
	private static final String cargarConceptoTesoreriaStr="SELECT "+ 
		"c.codigo AS codigo,"+
		"c.descripcion AS descripcion,"+
		"c.codigo_tipo_ingreso AS tipo,"+
		"CASE WHEN c.valor IS NULL THEN '0' ELSE c.valor END AS valor,"+
		"getfiltrotesoreria(c.codigo_tipo_ingreso,c.valor) AS filtro,"+
		"t.descripcion AS nom_tipo,"+
		"CASE WHEN c.cuenta IS NULL THEN '' ELSE c.cuenta || '' END AS cuenta,"+
		"cc.descripcion AS nom_cuenta,"+
		"CASE WHEN c.tipo_docum_ingreso IS NULL THEN 0 ELSE c.tipo_docum_ingreso END AS doc_ingreso,"+
		"di.descripcion AS nom_doc_ingreso,"+
		"CASE WHEN c.tipo_docum_anulacion IS NULL THEN 0 ELSE c.tipo_docum_anulacion END AS doc_anulacion,"+
		"da.descripcion AS nom_doc_anulacion,"+
		"CASE WHEN c.codigo_centro_costo IS NULL THEN 0 ELSE c.codigo_centro_costo END AS centro_costo,"+
		"co.nombre AS nom_centro_costo,"+
		"CASE WHEN c.nit_homologacion IS NULL THEN 0 ELSE c.nit_homologacion END AS nit,"+
		"te.numero_identificacion AS nom_nit,"+
		"c.rubro_presupuestal as rubropresupuestal,"+
		"CASE WHEN c.es_activo IS NULL THEN "+ValoresPorDefecto.getValorFalseParaConsultas()+" ELSE c.es_activo END AS activo "+
		"FROM conceptos_ing_tesoreria c "+ 
		"INNER JOIN tipo_ing_tesoreria t ON(t.codigo=c.codigo_tipo_ingreso) "+ 
		"LEFT OUTER JOIN cuentas_contables cc ON(cc.codigo=c.cuenta) "+ 
		"LEFT OUTER JOIN tipo_doc_contabilidad di ON(di.codigo=c.tipo_docum_ingreso) "+ 
		"LEFT OUTER JOIN tipo_doc_contabilidad da ON(da.codigo=c.tipo_docum_anulacion) "+ 
		"LEFT OUTER JOIN centros_costo co ON(co.codigo=c.codigo_centro_costo) "+  
		"LEFT OUTER JOIN terceros te ON(te.codigo=c.nit_homologacion) "+
		"WHERE "+
		"c.codigo=? AND c.institucion=?";
	
	/**
	 * Cadena usada para realizar una consulta avanzada de los registros 
	 * de los conceptos de ingreso de tesorería
	 */
	private static final String busquedaConceptosTesoreriaStr="SELECT "+ 
		"c.codigo AS codigo,"+
		"c.descripcion AS descripcion,"+
		"t.descripcion AS tipo,"+
		"getfiltrotesoreria(c.codigo_tipo_ingreso,c.valor) AS valor,"+
		"cc.descripcion AS cuenta,"+
		"di.descripcion AS doc_ingreso,"+
		"da.descripcion AS doc_anulacion,"+
		"co.nombre AS centro_costo,"+
		"te.numero_identificacion AS nit,"+
		"c.rubro_presupuestal as rubropresupuestal,"+
		"c.es_activo AS activo," +
		"coalesce(rp.descripcion,'') as rubro "+ 
		"FROM conceptos_ing_tesoreria c "+ 
		"INNER JOIN tipo_ing_tesoreria t ON(t.codigo=c.codigo_tipo_ingreso) "+ 
		"LEFT OUTER JOIN cuentas_contables cc ON(cc.codigo=c.cuenta) "+ 
		"LEFT OUTER JOIN tipo_doc_contabilidad di ON(di.codigo=c.tipo_docum_ingreso) "+ 
		"LEFT OUTER JOIN tipo_doc_contabilidad da ON(da.codigo=c.tipo_docum_anulacion) "+ 
		"LEFT OUTER JOIN centros_costo co ON(co.codigo=c.codigo_centro_costo) "+  
		"LEFT OUTER JOIN terceros te ON(te.codigo=c.nit_homologacion) " +
		"LEFT OUTER JOIN  rubro_presupuestal rp on(rp.codigo=c.rubro_presupuestal) "+  
		"WHERE ";
	
	/**
	 * Consulta que retorna el número de veces que se encuentra un
	 * concepto usado en los registros de recibos de caja
	 */
	private static final String revisionUsoReciboCajaStr=" SELECT " +
			"count(1) AS resultado " +
			"FROM detalle_conceptos_rc WHERE concepto=? AND institucion=?";
	
	/**
	 * Cadena que carga los tipos pagos
	 */
	private static final String cargarTiposPagosStr = "SELECT codigo,descripcion FROM tipo_ing_tesoreria ORDER BY descripcion";
	
	/**
	 * Cadena que carga la descripcion del Tipo de Pago
	 */
	private static final String cargarTipoPagoEspecialStr = "SELECT descripcion FROM tipo_ing_tesoreria WHERE codigo=?";
	
	/**
	 * Cadena que carga los tipos de documentos de contabilidad
	 */
	private static final String cargarTiposDocContabilidadStr = "SELECT " +
		"codigo,descripcion " +
		"from tipo_doc_contabilidad " +
		"WHERE institucion = ? and es_activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" ORDER BY descripcion";
	
	/**
	 * Cadena que carga los centros de costo
	 */
	private static final String cargarCentrosCostoStr = "SELECT " +
		"codigo,nombre " +
		"FROM centros_costo " +
		"WHERE institucion = ? and es_activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" and " +
		"codigo not in ("+ConstantesBD.codigoCentroCostoNoSeleccionado+","+ConstantesBD.codigoCentroCostoTodos+") ORDER BY nombre";
	
	/**
	 * Cadena que carga los terceros
	 */
	private static final String cargarTercerosStr = "SELECT " +
		"codigo,numero_identificacion, descripcion " +
		"FROM terceros " +
		"WHERE institucion = ? and activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" ORDER BY numero_identificacion";
	
	
	/**
	 * Metodo que consulta la descripcion de un tipo de pago segun un codigo dado
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static String cargarTipoPagoEspecial (Connection con, int codigo)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(cargarTipoPagoEspecialStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigo);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				return rs.getString(1);
			else
				return "";
		}
		catch(SQLException e)
		{
			logger.error("Error cargando la descripcion del Tipo de Pago de SqlBaseConceptoTesoreriaDao: "+e);
			return "";
		}
	}
	
	/**
	 * Método usado para cargar los conceptos de ingreso de tesorería 
	 * parameteizados por institución
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap cargarConceptosTesoreria(Connection con,int institucion)
	{
		try
		{
			
			String consulta=cargarConceptosTesoreriaStr;
			consulta+=" c.institucion="+institucion;
			consulta+=" ORDER BY c.descripcion";
			Statement st=con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)),true,false);
	        st.close();
			return mapaRetorno;

		}
		catch(SQLException e)
		{
			logger.error("Error en cargarConceptosTesoreria de SqlBaseConceptoTesoreriaDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método usado para ingresar un nuevo concepto de tesoreria
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param tipo
	 * @param valor
	 * @param cuenta
	 * @param docum_ingreso
	 * @param docum_anulacion
	 * @param centroCosto
	 * @param nit
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public static int insertarConceptoTesoreria (
			Connection con, 
			String codigo, 
			String descripcion, 
			int tipo, 
			String valor,
			String cuenta, 
			int documIngreso, 
			int documAnulacion,
			int centroCosto, 
			int nit, 
			boolean activo,
			int institucion,String rubroPresupuestal)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(insertarConceptosTesoreriaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			pst.setString(1,codigo);
			pst.setString(2,descripcion);
			pst.setInt(3,tipo);
			
			//se verifica contenido del valor
			if(valor.equals("0"))
				pst.setNull(4,Types.VARCHAR);
			else
				pst.setString(4,valor);
			
			//se verifica contenido de la cuenta
			if(!cuenta.equals(""))
				pst.setObject(5,cuenta);
			else
				pst.setNull(5,Types.NUMERIC);
			
			//se verifica contenido del documento de ingreso
			if(documIngreso>0)
				pst.setInt(6,documIngreso);
			else
				pst.setNull(6,Types.INTEGER);
			
			//se verifica contenido del documento de anulación
			if(documAnulacion>0)
				pst.setInt(7,documAnulacion);
			else
				pst.setNull(7,Types.INTEGER);
			
			//se verifica contenido del centro de costo
			if(centroCosto>0)
				pst.setInt(8,centroCosto);
			else
				pst.setNull(8,Types.INTEGER);
			
			//se verifica contenido del nit de homologación
			if(nit>0)
				pst.setInt(9,nit);
			else
				pst.setNull(9,Types.INTEGER);
			
			pst.setInt(10,institucion);
			pst.setBoolean(11,activo);
			if(UtilidadTexto.isEmpty(rubroPresupuestal))
				pst.setObject(12, null);
			else
				pst.setObject(12, rubroPresupuestal);
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarConceptoTesoreria de SqlBaseConceptoTesoreriaDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Método usado para actualizar un registro de conceptos ingreso tesorería
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param tipo
	 * @param valor
	 * @param cuenta
	 * @param documIngreso
	 * @param documAnulacion
	 * @param centroCosto
	 * @param nit
	 * @param activo
	 * @param codigoAntiguo
	 * @return
	 */
	public static int actualizarConceptoTesoreria (Connection con,
			String codigo, 
			String descripcion, 
			int tipo, 
			String valor,
			String cuenta, 
			int documIngreso, 
			int documAnulacion,
			int centroCosto, 
			int nit, 
			boolean activo,
			String codigoAntiguo,
			int institucion,String rubroPresupuestal)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(actualizarConceptoTesoreriaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE " +
			"conceptos_ing_tesoreria SET " +
			"codigo=?, 
			descripcion=?, 
			codigo_tipo_ingreso=?, 
			valor=?, 
			cuenta=?, 
			tipo_docum_ingreso=?, 
			tipo_docum_anulacion=?, 
			codigo_centro_costo=?, 
			nit_homologacion=?, 
			es_activo=?,
			rubro_presupuestal=? " +
			"WHERE codigo=? 
			AND institucion=?
			 */
			
			pst.setString(1,codigo);
			pst.setString(2,descripcion);
			pst.setInt(3,tipo);
			
			//se verifica contenido del valor
			if(valor.equals("0"))
				pst.setNull(4,Types.VARCHAR);
			else
				pst.setString(4,valor);
			
			//se verifica contenido de la cuenta
			if(!cuenta.equals(""))
				pst.setDouble(5,Utilidades.convertirADouble(cuenta));
			else
				pst.setNull(5,Types.NUMERIC);
			
			//se verifica contenido del documento de ingreso
			if(documIngreso>0)
				pst.setInt(6,documIngreso);
			else
				pst.setNull(6,Types.INTEGER);
			
			//se verifica contenido del documento de anulación
			if(documAnulacion>0)
				pst.setInt(7,documAnulacion);
			else
				pst.setNull(7,Types.INTEGER);
			
			//se verifica contenido del centro de costo
			if(centroCosto>0)
				pst.setInt(8,centroCosto);
			else
				pst.setNull(8,Types.INTEGER);
			
			//se verifica contenido del nit de homologación
			if(nit>0)
				pst.setInt(9,nit);
			else
				pst.setNull(9,Types.INTEGER);
			
			pst.setBoolean(10,activo);
			if(UtilidadTexto.isEmpty(rubroPresupuestal))
				pst.setNull(11, Types.VARCHAR);
			else
				pst.setString(11, rubroPresupuestal);
			pst.setString(12,codigoAntiguo);
			pst.setInt(13,institucion);
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarConceptoTesoreria de SqlBaseConceptoTesoreriaDao: "+e);
			return ConstantesBD.codigoNuncaValido;
		}
	}
	
	/**
	 * Método para eliminar un concepto de ingreso tesoreria
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public static int eliminarConceptoTesoreria(Connection con,String codigo,int institucion)
	{
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(eliminarConceptoTesoreriaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,codigo);
			pst.setInt(2,institucion);
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en eliminarConceptoTesoreria de SqlBaseConceptoTesoreriaDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Método usado para cargar un registro de los conceptos de ingreso de tesoreria
	 * por su código y la institución
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public static HashMap cargarConceptoTesoreria(Connection con,String codigo,int institucion)
	{
		//columnas del listado
		String[] columnas={
				"codigo",
				"descripcion",
				"tipo",
				"valor",
				"filtro",
				"nom_tipo",
				"cuenta",
				"nom_cuenta",
				"doc_ingreso",
				"nom_doc_ingreso",
				"doc_anulacion",
				"nom_doc_anulacion",
				"centro_costo",
				"nom_centro_costo",
				"nit",
				"nom_nit",
				"activo",
				"rubropresupuestal"
			};
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(cargarConceptoTesoreriaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,codigo);
			pst.setInt(2,institucion);
			
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarConceptoTesoreria de SqlBaseConceptoTesoreriaDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método usado para la búsqueda avanzada de los conceptos de tesorería
	 * en la opción de consulta
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param tipo
	 * @param valor
	 * @param cuenta
	 * @param documIngreso
	 * @param documAnulacion
	 * @param centroCosto
	 * @param nit
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public static HashMap busquedaConceptosTesoreria(
			Connection con,
			String codigo,
			String descripcion,
			int tipo,
			String valor,
			String cuenta,
			int documIngreso,
			int documAnulacion,
			int centroCosto,
			int nit,
			String activo,
			int institucion)
	{
		//columnas del listado
		
		logger.info("VALOR-----> : "+valor);
		String[] columnas={
				"codigo",
				"descripcion",
				"tipo",
				"valor",
				"cuenta",
				"doc_ingreso",
				"doc_anulacion",
				"centro_costo",
				"nit",
				"activo",
				"rubropresupuestal",
				"rubro"
			};
		try
		{
			String consulta=busquedaConceptosTesoreriaStr;
			
			consulta=prepararBusqueda(consulta,codigo,descripcion,tipo,valor,cuenta,documIngreso,documAnulacion,centroCosto,nit,activo,institucion);
			
			Statement st=con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(st.executeQuery(consulta)),false,true);
			
			return listado.getMapa();
		}
		catch(SQLException e)
		{
			logger.error("Error en busquedaConceptosTesoreria de SqlBaseConceptosTesoreriaDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método implementado para verificar que el concepto de ingreso
	 * tesorería no se esté utilizando en otras funcionalidades
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public static boolean revisarUsoConcepto(Connection con,String codigo,int institucion)
	{
		try
		{
			//Se verifica si el concepto está siendo usaod en un recibo de caja*****************
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(revisionUsoReciboCajaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,codigo);
			pst.setInt(2,institucion);
			
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				int resultado=rs.getInt("resultado");
				if(resultado>0)
					return true;
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en revisarUsoConcepto de SqlBaseConceptosTesoreriaDao: "+e);
		}
		return false;
	}
	
	/**
	 * Método usado para la búsqueda avanzada de los registros vinculados
	 * con el ingreso/modificación de los conceptos de ingreso tesorería
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @param tipo
	 * @param valor
	 * @param cuenta
	 * @param documIngreso
	 * @param documAnulacion
	 * @param centroCosto
	 * @param nit
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public static HashMap busquedaConceptosTesoreria2(
			Connection con,
			String codigo,
			String descripcion,
			int tipo,
			String valor,
			String cuenta,
			int documIngreso,
			int documAnulacion,
			int centroCosto,
			int nit,
			String activo,
			int institucion)
	{
		//columnas del listado
		String[] columnas={
				"codigo",
				"descripcion",
				"tipo_pago",
				"activo",
				"rubropresupuestal"
			};
		try
		{
			String consulta=cargarConceptosTesoreriaStr;
			
			consulta=prepararBusqueda(consulta,codigo,descripcion,tipo,valor,cuenta,documIngreso,documAnulacion,centroCosto,nit,activo,institucion);
			Statement st=con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(st.executeQuery(consulta)),false,true);
			return listado.getMapa();
		}
		catch(SQLException e)
		{
			logger.error("Error en busquedaConceptosTesoreria2 de SqlBaseConceptosTesoreriaDao: "+e);
			return null;
		}
	}

	/**
	 * Método usado para preparar los datos de la consulta en las
	 * búsquedas avanzadas
	 * @param consulta
	 * @param codigo
	 * @param descripcion
	 * @param tipo
	 * @param valor
	 * @param cuenta
	 * @param documIngreso
	 * @param documAnulacion
	 * @param centroCosto
	 * @param nit
	 * @param activo
	 * @param institucion
	 * @return
	 */
	private static String prepararBusqueda(
			String consulta, 
			String codigo, 
			String descripcion, 
			int tipo, 
			String valor, 
			String cuenta, 
			int documIngreso, 
			int documAnulacion, 
			int centroCosto, 
			int nit, 
			String activo, 
			int institucion) 
	{
		consulta+=" c.institucion="+institucion;
		
		//parametrización de codigo
		if(!codigo.equals(""))
			consulta+=" AND c.codigo='"+codigo+"'";
		
		//parametrización de descripcion
		if(!descripcion.equals(""))
			consulta+=" AND UPPER(c.descripcion) like UPPER('%"+descripcion+"%')";
		
		//parametrización de tipo
		if(tipo>=0)
			consulta+=" AND c.codigo_tipo_ingreso="+tipo;
		
		
		//parametrización de valor
		if(!valor.equals("-1")&&!valor.equals(""))
		{
			
			if(valor.equals("0"))
				consulta+=" AND c.valor IS NULL";
			else
				consulta+=" AND c.valor='"+valor+"'";
		}
		
		//parametrizacion de cuenta
		if(!cuenta.equals(""))
			consulta+=" AND c.cuenta="+cuenta;
		
		//parametrización de tipo documento de ingreso
		if(documIngreso>0)
			consulta+=" AND c.tipo_docum_ingreso="+documIngreso;
		
		//parametrización de tipo de documento de anulación
		if(documAnulacion>0)
			consulta+=" AND c.tipo_docum_anulacion="+documAnulacion;
		
		//parametrización del centro de costo
		if(centroCosto>0)
			consulta+=" AND c.codigo_centro_costo="+centroCosto;
		
		//parametrizacion del nit de homologación
		if(nit>=0)
			consulta+=" AND c.nit_homologacion="+nit;
		
		//parametrizacion del campo activo
		if(!activo.equals(""))
		{
			if(UtilidadTexto.getBoolean(activo))
				consulta+=" AND c.es_activo="+ValoresPorDefecto.getValorTrueParaConsultas();
			else
				consulta+=" AND c.es_activo="+ValoresPorDefecto.getValorFalseParaConsultas();
		}
		consulta+=" ORDER BY c.descripcion";
		return consulta;
	}
	
	/**
	 * Método que carga los tipos de pagos
	 * @param con
	 * @return
	 */
	public static Collection cargarTiposPagos(Connection con)
	{
		try
		{
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(st.executeQuery(cargarTiposPagosStr)));
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarTiposPagos de SqlBaseConceptoTesoreriaDao: "+e);
			return new ArrayList();
		}
	}
	
	/**
	 * Método que carga los tipos de documentos de contabilidad
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static Collection cargarTiposDocContabilidad(Connection con,int institucion)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarTiposDocContabilidadStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,institucion);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(pst.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarTiposDocContabilidad de SqlBaseConceptoTesoreriaDao: "+e);
			return new ArrayList();
		}
	}
	
	/**
	 * Método que carga los centros de costo
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static Collection cargarCentrosCosto(Connection con,int institucion)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarCentrosCostoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,institucion);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(pst.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarCentrosCosto de SqlBaseConceptoTesoreriaDao: "+e);
			return new ArrayList();
		}
	}
	
	/**
	 * Método que carga los terceros
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static Collection cargarTerceros(Connection con,int institucion)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarTercerosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,institucion);
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(pst.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarTerceros de SQlBaseConceptoTesoreriaDao: "+e);
			return new ArrayList();
		}
	}
	


}