
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;

/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * Clase para las transacciones de Grupos Etareos por Convenio
 * @version 1.0  26 /May/ 2006
 */
public class SqlBaseGruposEtareosDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseGruposEtareosDao.class);
	
	
	/**
	 * Cadena con el statement necesario para consultar los grupos etareos existentes para un convenio
	 */
	private final static String consultarGruposEtareosStr=" SELECT ge.codigo as codigogrupoetareo, " +
														  " ge.convenio as codigoconvenio, " +
														  " ge.institucion as institucion, " +
														  " ge.edad_final as edadfinal, " +
														  " ge.edad_inicial as edadinicial, " +
														  " ge.fecha_inicial as fechainicial, " +
														  " ge.fecha_final as fechafinal, " +
														  " case when ge.sexo is null then -2 else ge.sexo end as codigosexo, " +
														  " case when ge.sexo is null then 'Ambos' else s.nombre end as nombresexo, " +
														  " ge.valor as valor, " +
														  " ge.porcentaje_pyp as pyp, " +
														  " 'si' as existebd " +
														  " FROM grupos_etareos_x_convenio ge " +
														  " LEFT OUTER JOIN sexo s ON(ge.sexo=s.codigo) " +
														  " WHERE ge.institucion = ? " +
														  " AND ge.convenio = ? " +
														  " AND fecha_inicial >= ? " +
														  " AND fecha_final <= ? " +
														  " ORDER BY ge.edad_inicial ASC, ge.edad_final ASC, s.nombre ASC ";
	
	/**
	 * Cadena con el statement necesario para modificar los grupos etareos
	 */
	private final static String modificarGrupoEtareoStr=" UPDATE grupos_etareos_x_convenio " +
														" SET edad_inicial = ? ," +
														" edad_final = ?, " +
														" sexo = ?, " +
														" fecha_inicial = ?," +
														" fecha_final = ?, " +
														" valor = ?, " +
														" porcentaje_pyp = ?" +
														" WHERE codigo = ? " ;
	
	/**
	 * Cadena con el statement necesario para verificar si existe un grupo etareo determinado
	 */
	private final static String existeGrupoEtareoStr=" SELECT count(1) as cantidad " +
													 " FROM grupos_etareos_x_convenio " +
													 " WHERE codigo= ? ";
	
	/**
	 * Cadena con el statement necesario para eliminar un grupo etareo
	 */
	private final static String eliminarGrupoEtareoStr=" DELETE FROM grupos_etareos_x_convenio " +
    												   " WHERE codigo = ? ";
	
	
	/**
	 * Método para consultar los grupos etareos en la base de datos
	 * segun unos parametros de busqueda
	 * @param con
	 * @param fechaIncial
	 * @param fechaFinal
	 * @param codigoConvenio
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public static HashMap consultarGruposEtareos (Connection con, String fechaInicial, String fechaFinal, int codigoConvenio, int institucion) throws SQLException
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarGruposEtareosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, institucion);
			ps.setInt(2, codigoConvenio);
			ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaInicial)));
			ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaFinal)));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
			ps.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en consultarGruposEtareos : [SqlBaseGruposEtareosDao] "+e.toString() );
			return null;
		}	    
	}
	
	/**
	 * Método para insertar un grupo etareo determinado. Admas antes de insertarlo verifica si existe, en 
	 * caso de que exista lo que hace es modificarlo de lo contrario lo inserta
	 * @param con
	 * @param codigo
	 * @param codigoConvenio
	 * @param institucion
	 * @param edadInicial
	 * @param edadFinal
	 * @param sexo
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param valor
	 * @param porcentajePyP
	 * @param insertarGrupoEtareoStr
	 * @return
	 * @throws SQLException
	 */
	public static int insertarGruposEtareos(Connection con, String codigo, int codigoConvenio, int institucion , String edadInicial, String edadFinal, int sexo, String fechaInicial, String fechaFinal, String valor, String porcentajePyP, String insertarGrupoEtareoStr) throws SQLException
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
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(existeGrupoEtareoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, codigo);
			rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				temp=rs.getInt("cantidad");
			}
			//Si existe el grupo etareo lo que hacemos es modificarlo
			if(temp>0)
			{
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(modificarGrupoEtareoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setString(1, edadInicial);
				ps.setString(2, edadFinal);
				if(sexo == ConstantesBD.codigoSexoAmbos)
				{
					ps.setObject(3, null);
				}
				else
				{
					ps.setInt(3, sexo);
				}
				ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaInicial)));
				ps.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaFinal)));
				ps.setString(6, valor);
				if(porcentajePyP.equals(""))
				{
					ps.setString(7, null);
				}
				else
				{
					ps.setString(7, porcentajePyP);
				}
				ps.setString(8, codigo);
				
				resp = ps.executeUpdate();
			}
			else
			{
				//Insertamos un nuevo Centro de Costo por via de ingreso
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarGrupoEtareoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigoConvenio);
				ps.setInt(2, institucion);
				ps.setString(3, edadInicial);
				ps.setString(4, edadFinal);
				if(sexo == ConstantesBD.codigoSexoAmbos)
				{
					ps.setObject(5, null);
				}
				else
				{
					ps.setInt(5, sexo);
				}
				ps.setDate(6, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaInicial)));
				ps.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaFinal)));
				ps.setString(8, valor);
				if(porcentajePyP.equals(""))
				{
					ps.setString(9, null);
				}
				else
				{
					ps.setString(9, porcentajePyP);
				}
				resp = ps.executeUpdate();
			}
		}
		catch(SQLException e)
		{
				logger.warn(e+" Error en insertarGruposEtareos : [SqlBaseGruposEtareosDao] "+e.toString() );
				resp=0;
		}
		return resp;
	}
	
	/**
	 * Método para eliminar un grupo etareo dado su codigo
	 * @param con
	 * @param codigo
	 * @return
	 * @throws SQLException
	 */
	public static int eliminarGrupoEtareo(Connection con, int codigo) throws SQLException
	{
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(eliminarGrupoEtareoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			return ps.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en eliminarGrupoEtareo : [SqlBaseGruposEtareosDao] "+e.toString() );
			return Integer.parseInt(e.getSQLState());
		}
		
	}
	
}