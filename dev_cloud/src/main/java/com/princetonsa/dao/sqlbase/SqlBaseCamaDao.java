/*
 * Created on 02-abr-2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import util.Answer;
import util.ConstantesBD;
import util.UtilidadFecha;
import util.Utilidades;

/**
 * @author juanda
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SqlBaseCamaDao {

	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar una cama desde la base de datos Genérica.
	 */
	private static final String cargarCamaStr="SELECT " +
		"c.numero_cama as numeroCama, " +
		"c.estado as estado, " +
		"c.centro_costo as codigoCentroCosto, " +
		"c.tipo_usuario_cama as codigoTipoUsuarioCama, " +
		"tuc.nombre as tipoUsuarioCama, " +
		"p.codigo_piso || '-' ||  c.numero_cama as descripcion, " +
		"c.habitacion, "+
		"'P' || p.codigo_piso || 'H' || h.codigo_habitac AS descripcion_habitacion " +
		"FROM camas1 c  " +
		"INNER JOIN tipos_usuario_cama tuc on(c.tipo_usuario_cama = tuc.codigo) " +
		"INNER JOIN habitaciones h ON(h.codigo = c.habitacion) " +
		"INNER JOIN pisos p on(p.codigo = h.piso) " +
		"WHERE c.codigo=? ";

	/**
	 * Cadena constante con el <i>statement</i> necesario para modificar 
	 * una cama en la base de datos Genérica.
	 */
	private static final String modificarCamaStr="UPDATE camas1 set numero_cama=?, estado=?, centro_costo=?, tipo_usuario_cama=? where codigo=?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar 
	 * el último uso de una cama en la base de datos Genérica.
	 */
	private static final String cargarUltimoUsoCamaStr = 	" SELECT hcp.fecha_traslado AS fecha, " +
																						"hcp.hora_traslado AS hora " +
																						"FROM his_cama_pac hcp " +
																						"WHERE hcp.codigo_cama = ? " +
																						"UNION " +
																						"SELECT e.fecha_egreso AS fecha, " +
																						"e.hora_egreso AS hora " +
																						"FROM admisiones_hospi ah, egresos e " +
																						"WHERE ah.cuenta = e.cuenta " +
																						"AND ah.cama = ?  " +
																						"ORDER BY fecha DESC, hora DESC ";
		
	/**
	 * Cadena constante con el <i>statement</i> necesario para cambiar el estado
	 * de una cama
	 */
	private static final String cambiarEstadoCamaStr = "UPDATE camas1 SET estado =? WHERE codigo =?";
																				
	/**
	 * Dada la identificacion de una cama, carga los datos correspondientes desde la base de datos Genérica.
	 * @param con una conexion abierta con una base de datos Genérica
	 * @param codigoCama el código de la cama que se desea cargar
	 * @return un <code>Answer</code> con los datos pedidos y una conexión abierta con la base de datos Genérica
	 */
	public static Answer cargarCama (Connection con, String codigoCama) throws SQLException
	{
		PreparedStatementDecorator cargarCamaStatement= new PreparedStatementDecorator(con.prepareStatement(cargarCamaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargarCamaStatement.setString(1, codigoCama);

		return new Answer(new ResultSetDecorator(cargarCamaStatement.executeQuery()), con);
	}

	/**
	 * Modifica una cama en una base de datos Genérica, reutilizando una conexion existente.
	 * @param con una conexion abierta con una base de datos Genérica
	 * @param codigoCama el código de la cama que desea modificar
	 * @param numeroCama número asignado a la cama
	 * @param estado estado de la cama
	 * @param codigoCentroCosto código del centro de costo al cual pertenece la cama
	 * @param codigoTipoUsuario código del tipo de usuario que va a usar la cama
	 * @return numero de camas modificadas
	 */
	public static int modificarCama (Connection con, String codigoCama, String numeroCama, int estado, String codigoCentroCosto, String codigoTipoUsuario) throws SQLException
	{
		PreparedStatementDecorator modificarCamaStatement= new PreparedStatementDecorator(con.prepareStatement(modificarCamaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		modificarCamaStatement.setString(1, numeroCama);
		modificarCamaStatement.setInt(2, estado);
		modificarCamaStatement.setString(3, codigoCentroCosto);
		modificarCamaStatement.setString(4, codigoTipoUsuario);
		modificarCamaStatement.setInt(5,Utilidades.convertirAEntero(codigoCama));

		return modificarCamaStatement.executeUpdate();
	}
	
	public static String[] getFechaHoraUltimoUsoCama(Connection con, int codCama) throws SQLException
	{
		PreparedStatementDecorator fechaHoraUltimoUsoSttmnt =  new PreparedStatementDecorator(con.prepareStatement(cargarUltimoUsoCamaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		fechaHoraUltimoUsoSttmnt.setInt(1, codCama);
		fechaHoraUltimoUsoSttmnt.setInt(2, codCama);

		ResultSetDecorator resultado = new ResultSetDecorator(fechaHoraUltimoUsoSttmnt.executeQuery());
		
		if( resultado.next() )
		{
			String[] fechaHora = new String[2];
			fechaHora[0] = UtilidadFecha.conversionFormatoFechaAAp(resultado.getString("fecha"));
			fechaHora[1] = UtilidadFecha.convertirHoraACincoCaracteres(resultado.getString("hora"));
			
			return fechaHora;
		}		
		
		return null;
	}

	/**
	 * Cambia el estado de la cama en la base de datos.
	 * @param	estado, nuevo estado de la cama
	 * @param	codigoCama, código de la cama que desea modificar 
	 */
	public static int cambiarEstadoCama(Connection con, String codigoCama, int estado) throws SQLException
	{
		PreparedStatementDecorator cambiarEstadoCamaStatement =  new PreparedStatementDecorator(con.prepareStatement(cambiarEstadoCamaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cambiarEstadoCamaStatement.setInt(1, estado);
		cambiarEstadoCamaStatement.setInt(2, Utilidades.convertirAEntero(codigoCama));
		
		return cambiarEstadoCamaStatement.executeUpdate();
	}

	/**
	 * Inserta una cama en una base de datos Genérica, reutilizando una conexion existente.
	 * @param con una conexion abierta con una base de datos Genérica
	 * @param numeroCama número asignado a la cama
	 * @param estado estado de la cama
	 * @param codigoCentroCosto código del centro de costo al cual pertenece la cama
	 * @param codigoTipoUsuario código del tipo de usuario que va a usar la cama
	 * @return número de camas insertadas
	 */
	public static int insertarCama (Connection con, String numeroCama, int estado, String codigoCentroCosto, String codigoTipoUsuario, String insertarCamaStr) throws SQLException
	{
		PreparedStatementDecorator insertarCamaStatement =  new PreparedStatementDecorator(con.prepareStatement(insertarCamaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		insertarCamaStatement.setString(1, numeroCama);
		insertarCamaStatement.setInt(2, estado);
		insertarCamaStatement.setString(3, codigoCentroCosto);
		insertarCamaStatement.setString(4, codigoTipoUsuario);

		return insertarCamaStatement.executeUpdate();
	}

}
