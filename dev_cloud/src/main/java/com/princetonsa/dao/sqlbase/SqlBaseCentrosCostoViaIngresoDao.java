
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesValoresPorDefecto;
import util.UtilidadBD;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * Clase para las transacciones de Centros de Costo por Via de Ingreso
 * @version 1.0  15 /May/ 2006
 */
public class SqlBaseCentrosCostoViaIngresoDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseCentrosCostoViaIngresoDao.class);
	
	/**
	 * Cadena con el statement necesario para consultar los centros de costo asociados a una via de ingreso
	 */
	private final static String consultarCentrosCostoViaIngresoStr=" SELECT ccvi.codigo as codigo, " +
														 		   " ccvi.centro_costo as codigocentrocosto, " +
														 		   " cc.nombre as nombrecentrocosto, " +
														 		   " ccvi.via_ingreso as codigoviaingreso, " +
														 		   " vi.nombre as nombreviaingreso, " +
														 		   " ccvi.tipo_paciente as tipopaciente, " +
														 		   " 'si' as existebd " +
														 		   " FROM centro_costo_via_ingreso ccvi " +
														 		   " INNER JOIN centros_costo cc ON(ccvi.centro_costo=cc.codigo) " +
														 		   " INNER JOIN vias_ingreso vi ON(ccvi.via_ingreso=vi.codigo) " +
														 		   " WHERE ccvi.institucion = ? "+
														 		   " ORDER BY vi.nombre ASC, cc.nombre ASC ";
														 		   
	
	
	
	/**
	 * Hace la modificación de los datos de los centros de costo por via de ingreso
	 */
	private final static String modificarCentroCostoViaIngresoStr=" UPDATE centro_costo_via_ingreso SET " +
																  " centro_costo= ? , " +
																  " via_ingreso = ? ," +
																  " tipo_paciente= ? " +
																  " WHERE codigo = ?" +
																  " AND institucion = ? ";
	
	
	/**
	 * Cadena con el statement necesario para saber si existe un centro de costo por via de ingreso
	 */
	private final static String existeCentroCostoViaIngresoStr=" SELECT count(1) as cantidad " +
													 		   " FROM centro_costo_via_ingreso " +
													 		   " WHERE codigo = ? " ;
	
	
	/**
	 * Cadena con el statement necesario para eliminar un centro de costo por via de ingreso dado su codigo
	 */
	private final static String eliminarCentroCostoViaIngresoStr=" DELETE FROM centro_costo_via_ingreso " +
															     " WHERE codigo = ? ";
	
	
	/**
	 * Método para consultar los centros de cotso por via de ingreso
	 * segun la insitucion de los centros de costo
	 * @param con
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarCentrosCostoViaIngreso(Connection con, int institucion, int centroAtencion) 
	{
		String consulta= consultarCentrosCostoViaIngresoStr;
		if(centroAtencion>0)
		{
			
			String consultaCentrosCostoXValorDefecto=" AND CCVI.centro_costo  IN " +
					"(SELECT tvci.centros_costo " +
					"FROM trans_validas_x_cc_inven tvci " +
					"INNER JOIN tipos_trans_inventarios tti " +
					"ON (tvci.tipos_trans_inventario = tti.consecutivo) " +
					"WHERE tvci.institucion = ? " +
					"AND tti.activo     = " + ValoresPorDefecto.getValorTrueParaConsultas()+ " "+
					"AND tti.consecutivo = ? "+
					" GROUP BY tvci.centros_costo ) ";
			
			consulta =" SELECT " +
			 		   " DISTINCT ccvi.centro_costo as codigocentrocosto, " +
			 		   " cc.nombre as nombrecentrocosto " +
			 		   " FROM manejopaciente.centro_costo_via_ingreso ccvi " +
			 		   " INNER JOIN administracion.centros_costo cc ON(ccvi.centro_costo=cc.codigo) " +
			 		   " INNER JOIN manejopaciente.vias_ingreso vi ON(ccvi.via_ingreso=vi.codigo) " +
			 		   " WHERE ccvi.institucion = ? "+
					   " AND cc.centro_atencion = "+centroAtencion+" "+
					   consultaCentrosCostoXValorDefecto +
					   " ORDER BY nombrecentrocosto ASC ";
		}
		
		logger.info("\n\nconsultarCentrosCostoViaIngreso-->"+consulta+"\n\n");
		
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1 , institucion);
			
			if(centroAtencion>0)
			{
				ps.setInt(2 , institucion);
				
				ps.setInt(3, Integer.parseInt(ValoresPorDefecto.getCodigoTransSoliPacientes(institucion, true)));
			}
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
			ps.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en consultarCentrosCostoViaIngreso : [SqlBaseCentrosCostoViaIngresoDao] "+e.toString() );
		}
		return null;
	}
	
	
	/**
	 * Método para eliminar un cento ed costo x via de ingreso
	 * @param con
	 * @param codigo
	 * @return
	 * @throws SQLException
	 */
	public static int eliminarCentroCostoViaIngreso(Connection con, int codigo) throws SQLException
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarCentroCostoViaIngresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			return ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en eliminarCentroCostoViaIngreso : [SqlBaseCentrosCostoViaIngresoDao] "+e.toString() );
			return Integer.parseInt(e.getSQLState());
		}
		
	}
	
	/**
	 * Método para modificar un centro de costo x via de ingreso
	 * @param con
	 * @param codigoCentroCosto
	 * @param codigoViaIngreso
	 * @param codigo
	 * @return
	 * @throws SQLException
	 */
	public static int modificarCentroCostoViaIngreso(Connection con, int codigoCentroCosto, int codigoViaIngreso, int codigo, int institucion, String codigoTipoPaciente) throws SQLException 
	{
		int resp=0;	
		try
		{
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(modificarCentroCostoViaIngresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoCentroCosto);
			ps.setInt(2, codigoViaIngreso);
			ps.setString(3, codigoTipoPaciente);
			ps.setInt(4, codigo);
			ps.setInt(5, institucion);
		
			resp=ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en modificarCentroCostoViaIngreso : [SqlBaseCentrosCostoViaIngresoDao] "+e.toString());
			resp=0;			
		}	
		return resp;	
	}
	
	/**
	 * Método para insertar un nuevo centro de costo.
	 * Si existe lo que hace es modificarlo de lo contrario lo Inserta
	 * @param con
	 * @param codigo
	 * @param codigoCentroCosto
	 * @param codigoViaIngreso
	 * @param insertarCentroCostoViaIngresoStr
	 * @return
	 * @throws SQLException
	 */
	public static int insertarCentrosCostoViaIngreso(Connection con, int codigo, int codigoCentroCosto, int codigoViaIngreso, int institucion, String insertarCentroCostoViaIngresoStr, String codigoTipoPaciente) throws SQLException
	{
	   ResultSetDecorator rs ;
	   int resp = 0;
	   int temp = 0;
		try
		{
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(existeCentroCostoViaIngresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigo);
			rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				temp=rs.getInt("cantidad");
			}
			//Si existe un centro de costo por via de ingreso lo que hacemos es modificarlo
			if(temp>0)
			{
				//logger.info("\n estoy modificando->> "+codigoCentroCosto +"  codigoViaIngreso ->>"+codigoViaIngreso+" codigoTipoPaciente->>"+codigoTipoPaciente+" codigo->>"+codigo);
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(modificarCentroCostoViaIngresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigoCentroCosto);
				ps.setInt(2, codigoViaIngreso);
				ps.setString(3, codigoTipoPaciente);
				ps.setInt(4, codigo);
				ps.setInt(5, institucion);
				resp = ps.executeUpdate();
			}
			else
			{
				//Insertamos un nuevo Centro de Costo por via de ingreso
				//logger.info("\n estoy insertando codigoCentroCosto->> "+codigoCentroCosto +"  codigoViaIngreso ->>"+codigoViaIngreso+" codigoTipoPaciente->>"+codigoTipoPaciente);
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarCentroCostoViaIngresoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigoCentroCosto);
				ps.setInt(2, codigoViaIngreso);
				ps.setString(3, codigoTipoPaciente);
				ps.setInt(4, institucion);
				resp = ps.executeUpdate();
			}
		}
		catch(SQLException e)
		{
				logger.warn(e+" Error en insertarCentrosCostoViaIngreso : [insertarCentrosCostoViaIngreso] "+e.toString() );
				resp=0;
		}
		return resp;
	}
	
}