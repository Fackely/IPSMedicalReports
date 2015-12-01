/*
 * @(#)SqlBaseHijoBasicoDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */

package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatos;

import com.princetonsa.dao.DaoFactory;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a Hijo Básico
 *
 *	@version 1.0, Apr 1, 2004
 */
public class SqlBaseHijoBasicoDao 
{

	/**
	 * Log para manejar los problemas de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseHijoBasicoDao.class);
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar la
	 * información por hijo en los Antecedentes Gineco-Obstetricos
	 */
	private static final String insertarHijoBasicoStr = "INSERT INTO informacion_hijo_parto (codigo_paciente, ant_gineco_embarazo, numero_hijo, vivo, otro_tipo_parto_vaginal, cesarea, aborto, otro_tipo_parto, peso, sexo, lugar) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para modificar la
	 * información por hijo en los Antecedentes Gineco-Obstetricos
	 */
	private static final String modificarHijoBasicoStr = "UPDATE informacion_hijo_parto SET vivo = ?, otro_tipo_parto_vaginal = ?, cesarea = ?, aborto = ?, otro_tipo_parto = ?, peso=?, sexo=?, lugar=? WHERE codigo_paciente = ? AND ant_gineco_embarazo = ? AND numero_hijo = ?";

	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar los
	 * diferentes tipos de parto que se adapten al nacimiento de este
	 * hijo (Antecedentes Gineco- Obstetricos)
	 */
	
	private static final String insertarTipoPartoVaginalStr = "INSERT INTO inf_h_par_tip_par_vag (codigo_paciente, ant_gineco_embarazo, numero_hijo, tipo_parto) VALUES (?, ?, ?, ?)";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para cargar los
	 * diferentes tipos de parto correspondientes al nacimiento de este hijo
	 * (Antecedentes Gineco- Obstetricos)
	 */
	private static final String cargarTiposPartoStr = "SELECT etc.tipo_parto AS codigoTipoParto, tippar.nombre AS tipoParto FROM inf_h_par_tip_par_vag etc, tipos_parto tippar WHERE etc.tipo_parto = tippar.codigo AND etc.codigo_paciente = ? AND etc.ant_gineco_embarazo = ? AND etc.numero_hijo = ?";

	/**
	 * Cadena constante con el <i>statement</i> para saber si un tipo de parto
	 * en particular ya esta registrado para un niño en particular(Antecedentes
	 * Gineco- Obstetricos - Se usa para no volver a insertar un tipo ya
	 * existente)
	 */
	private static final String existeTipoPartoVaginalStr = "SELECT count(1) AS numResultados FROM inf_h_par_tip_par_vag WHERE codigo_paciente=? AND ant_gineco_embarazo=? AND numero_hijo=? AND tipo_parto =?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para saber si un hijo
	 * existe o no
	 */
	private static final String buscarHijoStr = "SELECT count(1) as numResultados FROM informacion_hijo_parto WHERE codigo_paciente = ? AND ant_gineco_embarazo = ? AND numero_hijo  = ?";
	
	/**
	 * Implementación de la inserción de un hijo (antecedentes Gineco-
	 * Obstetricos) para una BD Genérica
	 * @param peso
	 * @param sexo
	 * @param lugar
	 * 
	 * @see com.princetonsa.dao.HijoBasicoDao#insertar(Connection, int, int, int, boolean, String, boolean, boolean, String)
	 */
	public static int insertar (Connection con, int codigoPaciente, int numeroEmbarazo, int numeroHijo, boolean vivo, String otroTipoPartoVaginal, boolean cesarea, boolean aborto, String otroTipoParto, ArrayList tiposPartoVaginal, String peso, int sexo, String lugar) throws SQLException
	{
		int resp1=0, i;
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		try
		{
		
			//Como hay varios insert es transaccional
			//por eso ponemos el autocommit en falso
			con.setAutoCommit(false);

			//Definimos una variable de tipo InfoDatos para sacar todas los posibles partos
			//vaginales de este hijo
			 
			InfoDatos tipoPartoIndividual;
			
			//Llegue a insertar
			
			PreparedStatementDecorator insertarHijoBasicoStatement= new PreparedStatementDecorator(con.prepareStatement(insertarHijoBasicoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			insertarHijoBasicoStatement.setInt(1, codigoPaciente);
			insertarHijoBasicoStatement.setInt(2, numeroEmbarazo);
			insertarHijoBasicoStatement.setInt(3, numeroHijo);
			insertarHijoBasicoStatement.setBoolean(4, vivo);
			insertarHijoBasicoStatement.setString(5, otroTipoPartoVaginal);
			insertarHijoBasicoStatement.setBoolean(6, cesarea);
			insertarHijoBasicoStatement.setBoolean(7, aborto);
			insertarHijoBasicoStatement.setString(8, otroTipoParto);
			insertarHijoBasicoStatement.setString(9, peso);
			if(sexo==ConstantesBD.codigoSexoMasculino || sexo==ConstantesBD.codigoSexoFemenino)
			{
				insertarHijoBasicoStatement.setInt(10, sexo);
			}
			else
			{
				insertarHijoBasicoStatement.setString(10, null);
			}
			insertarHijoBasicoStatement.setString(11, lugar);

	
			resp1=insertarHijoBasicoStatement.executeUpdate();
			
			//Como debo revisar con un if dentro del while si salio
			//bien, voy a revisar de una vez resp1
			if (resp1<1)
			{
				// Terminamos la transaccion, sea con un rollback o un commit.
				myFactory.abortTransaction(con);
				return 0;
			}

	
			if (tiposPartoVaginal!=null)
			{
				PreparedStatementDecorator insertarTipoPartoVaginalStatement=null;
				
				for (i=0;i<tiposPartoVaginal.size();i++)
				{
					tipoPartoIndividual=(InfoDatos)tiposPartoVaginal.get(i);
					insertarTipoPartoVaginalStatement= new PreparedStatementDecorator(con.prepareStatement(insertarTipoPartoVaginalStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					insertarTipoPartoVaginalStatement.setInt(1, codigoPaciente);
					insertarTipoPartoVaginalStatement.setInt(2, numeroEmbarazo);
					insertarTipoPartoVaginalStatement.setInt(3, numeroHijo);
					insertarTipoPartoVaginalStatement.setInt(4, tipoPartoIndividual.getCodigo());
					
					if (insertarTipoPartoVaginalStatement.executeUpdate() < 1 )
					{
						myFactory.abortTransaction(con);
						return 0;
					}
					
				}
				
				//Si llego a este punto es porque todo salio bien
				myFactory.endTransaction(con);
				con.setAutoCommit(true);

			}
		}
		catch (SQLException e)
		{
			//Por alguna razón no salió bien
			logger.warn(e);
			throw e;
		}
		
		return resp1;
	}

	/**
	 * Implementación de la inserción de un hijo (antecedentes Gineco-
	 * Obstetricos), permitiendo definir el nivel de transaccionalidad para 
	 * una BD Genérica
	 * @param peso
	 * @param sexo
	 * @param lugar
	 * 
	 * @see com.princetonsa.dao.HijoBasicoDao#insertarTransaccional(Connection, int, int, int, boolean, String, boolean, boolean, String, String)
	 */
	public static int insertarTransaccional (Connection con, int codigoPaciente, int numeroEmbarazo, int numeroHijo, boolean vivo, String otroTipoPartoVaginal, boolean cesarea, boolean aborto, String otroTipoParto, ArrayList tiposPartoVaginal, String estado, String peso, int sexo, String lugar) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		try
		{
			int resp0=0, resp1=0, i;

			//Definimos una variable de tipo InfoDatos para sacar todas los posibles partos
			//vaginales de este hijo
			 
			InfoDatos tipoPartoIndividual;

			if (con == null || con.isClosed()) 
			{
				//Como es transaccional NO voy a tratar de abrir una nueva conexión, sino que mandaré una excepction
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			
			if (estado==null)
			{
				throw new SQLException ("Error SQL: No se seleccionó ningun estado para la transacción");
			}
					
			if (estado.equals("empezar"))
			{
				con.setAutoCommit(false);
				myFactory.beginTransaction(con);
				resp0=1;
			}	
			else
			{
				//De todas maneras así no sea transacción debo dejar en claro que todo salio bien
				resp0=1;
			}
	
			PreparedStatementDecorator insertarHijoBasicoStatement= new PreparedStatementDecorator(con.prepareStatement(insertarHijoBasicoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			insertarHijoBasicoStatement.setInt(1, codigoPaciente);
			insertarHijoBasicoStatement.setInt(2, numeroEmbarazo);
			insertarHijoBasicoStatement.setInt(3, numeroHijo);
			insertarHijoBasicoStatement.setBoolean(4, vivo);
			insertarHijoBasicoStatement.setString(5, otroTipoPartoVaginal);
			insertarHijoBasicoStatement.setBoolean(6, cesarea);
			insertarHijoBasicoStatement.setBoolean(7, aborto);
			insertarHijoBasicoStatement.setString(8, otroTipoParto);
			insertarHijoBasicoStatement.setString(9, peso);
			if(sexo==ConstantesBD.codigoSexoMasculino || sexo==ConstantesBD.codigoSexoFemenino)
			{
				insertarHijoBasicoStatement.setInt(10, sexo);
			}
			else
			{
				insertarHijoBasicoStatement.setString(10, null);
			}
			insertarHijoBasicoStatement.setString(11, lugar);
	
			resp1= insertarHijoBasicoStatement.executeUpdate();

			//Como debo revisar con un if dentro del while si salio
			//bien, voy a revisar de una vez resp1
			if (resp1<1)
			{
				// Terminamos la transaccion, sea con un rollback o un commit.
				myFactory.abortTransaction(con);
				return 0;
			}
			
			if (tiposPartoVaginal!=null)
			{
				PreparedStatementDecorator insertarTipoPartoVaginalStatement=null;
				
				for (i=0;i<tiposPartoVaginal.size();i++)
				{
					tipoPartoIndividual=(InfoDatos)tiposPartoVaginal.get(i);
					insertarTipoPartoVaginalStatement= new PreparedStatementDecorator(con.prepareStatement(insertarTipoPartoVaginalStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					insertarTipoPartoVaginalStatement.setInt(1, codigoPaciente);
					insertarTipoPartoVaginalStatement.setInt(2, numeroEmbarazo);
					insertarTipoPartoVaginalStatement.setInt(3, numeroHijo);
					insertarTipoPartoVaginalStatement.setInt(4, tipoPartoIndividual.getCodigo());

					if (insertarTipoPartoVaginalStatement.executeUpdate() < 1 )
					{
						myFactory.abortTransaction(con);
						return 0;
					}
					
				}
			}
			
			if (resp0<1||resp1<1)
			{
				// Terminamos la transaccion, sea con un rollback o un commit.
				myFactory.abortTransaction(con);
				return 0;
			}
			else
			{
				if (estado.equals("finalizar"))
				{
					myFactory.endTransaction(con);
					con.setAutoCommit(true);
				}
			}		
			return resp1;

		}
		catch (SQLException e)
		{
			//Por alguna razón no salió bien
			logger.warn(e);
			throw e;
		}
		
	}

	/**
	 * Implementación de la modificación de un hijo (antecedentes Gineco-
	 * Obstetricos) para una BD Genérica
	 * @param peso
	 * @param sexo
	 * @param lugar
	 * 
	 * @see com.princetonsa.dao.HijoBasicoDao#modificar(Connection, int, int, int, boolean, String, boolean, boolean, String, ArrayList, String, int, String)
	 */
	public static int modificar (Connection con, int codigoPaciente, int numeroEmbarazo, int numeroHijo, boolean vivo, String otroTipoPartoVaginal, boolean cesarea, boolean aborto, String otroTipoParto, ArrayList tiposPartoVaginal, String peso, int sexo, String lugar) throws SQLException
	{
		int resp1=0, i;

		try
		{
		
			DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			//Como hay varios insert es transaccional
			//por eso ponemos el autocommit en falso
			con.setAutoCommit(false);

			//Definimos una variable de tipo InfoDatos para sacar todas los posibles partos
			//vaginales de este hijo
			 
			InfoDatos tipoPartoIndividual;
			
			PreparedStatementDecorator modificarHijoBasicoStatement= new PreparedStatementDecorator(con.prepareStatement(modificarHijoBasicoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			modificarHijoBasicoStatement.setBoolean(1, vivo);
			modificarHijoBasicoStatement.setString(2, otroTipoPartoVaginal);
			modificarHijoBasicoStatement.setBoolean(3, cesarea);
			modificarHijoBasicoStatement.setBoolean(4, aborto);
			modificarHijoBasicoStatement.setString(5, otroTipoParto);
			modificarHijoBasicoStatement.setString(6, peso);
			if(sexo==ConstantesBD.codigoSexoMasculino || sexo==ConstantesBD.codigoSexoFemenino)
			{
				modificarHijoBasicoStatement.setInt(7, sexo);
			}
			else
			{
				modificarHijoBasicoStatement.setString(7, null);
			}
			modificarHijoBasicoStatement.setString(8, lugar);
			modificarHijoBasicoStatement.setInt(9, codigoPaciente);
			modificarHijoBasicoStatement.setInt(10, numeroEmbarazo);
			modificarHijoBasicoStatement.setInt(11, numeroHijo);

			
			resp1=modificarHijoBasicoStatement.executeUpdate();
			
			//Como debo revisar con un if dentro del while si salio
			//bien, voy a revisar de una vez resp1
			if (resp1<1)
			{
				// Terminamos la transaccion, sea con un rollback o un commit.
				myFactory.abortTransaction(con);
				return 0;
			}

	
			if (tiposPartoVaginal!=null)
			{
				
				
				PreparedStatementDecorator insertarTipoPartoVaginalStatement=null;
				
				for (i=0;i<tiposPartoVaginal.size();i++)
				{

					tipoPartoIndividual=(InfoDatos)tiposPartoVaginal.get(i);

					//Debo insertar este tipo de parto SOLO si no existe
					//por eso debo validar
					
					if (!existeTipoPartoVaginal (con, codigoPaciente, numeroEmbarazo, numeroHijo, tipoPartoIndividual.getCodigo()))
					{
						insertarTipoPartoVaginalStatement= new PreparedStatementDecorator(con.prepareStatement(insertarTipoPartoVaginalStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						insertarTipoPartoVaginalStatement.setInt(1, codigoPaciente);
						insertarTipoPartoVaginalStatement.setInt(2, numeroEmbarazo);
						insertarTipoPartoVaginalStatement.setInt(3, numeroHijo);
						insertarTipoPartoVaginalStatement.setInt(4, tipoPartoIndividual.getCodigo());
						
						if (insertarTipoPartoVaginalStatement.executeUpdate() < 1 )
						{
							myFactory.abortTransaction(con);
							return 0;
						}
					}
				}
				
				//Si llego a este punto es porque todo salio bien
				myFactory.endTransaction(con);
				con.setAutoCommit(true);
			}
		}
		catch (SQLException e)
		{
			//Por alguna razón no salió bien
			logger.warn(e);
			throw e;
		}
		
		return resp1;
	}

	/**
	 * Implementación de la modificación de un hijo (antecedentes Gineco-
	 * Obstetricos), permitiendo definir el nivel de transaccionalidad para 
	 * una BD Genérica
	 * @param peso
	 * @param sexo
	 * @param lugar
	 * 
	 * @see com.princetonsa.dao.HijoBasicoDao#modificarTransaccional(Connection, int, int, int, boolean, String, boolean, boolean, String, ArrayList, String, String, int, String)
	 */
	public static int modificarTransaccional (Connection con, int codigoPaciente, int numeroEmbarazo, int numeroHijo, boolean vivo, String otroTipoPartoVaginal, boolean cesarea, boolean aborto, String otroTipoParto, ArrayList tiposPartoVaginal, String estado, String peso, int sexo, String lugar) throws SQLException
	{

		try
		{
			DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			int resp0=0, resp1=0, i;

			//Definimos una variable de tipo InfoDatos para sacar todas los posibles partos
			//vaginales de este hijo
			 
			InfoDatos tipoPartoIndividual;
			
			if (con == null || con.isClosed()) 
			{
				//Como es transaccional NO voy a tratar de abrir una nueva conexión, sino que mandaré una excepction
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			
			if (estado==null)
			{
				throw new SQLException ("Error SQL: No se seleccionó ningun estado para la transacción");
			}
					
			if (estado.equals("empezar"))
			{
				con.setAutoCommit(false);
				myFactory.beginTransaction(con);
				resp0 = 1;
			}	
			else
			{
				//De todas maneras así no sea transacción debo dejar en claro que todo salio bien
				resp0=1;
			}
	
			PreparedStatementDecorator modificarHijoBasicoStatement= new PreparedStatementDecorator(con.prepareStatement(modificarHijoBasicoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			modificarHijoBasicoStatement.setBoolean(1, vivo);
			modificarHijoBasicoStatement.setString(2, otroTipoPartoVaginal);
			modificarHijoBasicoStatement.setBoolean(3, cesarea);
			modificarHijoBasicoStatement.setBoolean(4, aborto);
			modificarHijoBasicoStatement.setString(5, otroTipoParto);
			modificarHijoBasicoStatement.setString(6, peso);
			if(sexo==ConstantesBD.codigoSexoMasculino || sexo==ConstantesBD.codigoSexoFemenino)
			{
				modificarHijoBasicoStatement.setInt(7, sexo);
			}
			else
			{
				modificarHijoBasicoStatement.setString(7, null);
			}
			modificarHijoBasicoStatement.setString(8, lugar);
			modificarHijoBasicoStatement.setInt(9, codigoPaciente);
			modificarHijoBasicoStatement.setInt(10, numeroEmbarazo);
			modificarHijoBasicoStatement.setInt(11, numeroHijo);

			
			resp1=modificarHijoBasicoStatement.executeUpdate();

			//Como debo revisar con un if dentro del while si salio
			//bien, voy a revisar de una vez resp1
			if (resp1<1)
			{
				// Terminamos la transaccion, sea con un rollback o un commit.
				myFactory.abortTransaction(con);
			}
			
			if (tiposPartoVaginal!=null)
			{
				PreparedStatementDecorator insertarTipoPartoVaginalStatement=null;
				
				for (i=0;i<tiposPartoVaginal.size();i++)
				{
					tipoPartoIndividual=(InfoDatos)tiposPartoVaginal.get(i);
					//Debo insertar este tipo de parto SOLO si no existe
					//por eso debo validar
					
					if (!existeTipoPartoVaginal (con, codigoPaciente, numeroEmbarazo, numeroHijo, tipoPartoIndividual.getCodigo()))
					{
						insertarTipoPartoVaginalStatement= new PreparedStatementDecorator(con.prepareStatement(insertarTipoPartoVaginalStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						insertarTipoPartoVaginalStatement.setInt(1, codigoPaciente);
						insertarTipoPartoVaginalStatement.setInt(2, numeroEmbarazo);
						insertarTipoPartoVaginalStatement.setInt(3, numeroHijo);
						insertarTipoPartoVaginalStatement.setInt(4, tipoPartoIndividual.getCodigo());						
						
						if (insertarTipoPartoVaginalStatement.executeUpdate() < 1 )
						{
							myFactory.abortTransaction(con);
							return 0;
						}
					}
					
				}
			}
			
			if (resp0<1||resp1<1)
			{
				// Terminamos la transaccion, sea con un rollback o un commit.
				myFactory.abortTransaction(con);
				return 0;
			}
			else
			{
				if (estado.equals("finalizar"))
				{
					myFactory.endTransaction(con);
					con.setAutoCommit(true);
				}
			}		
			return resp1;

		}
		catch (SQLException e)
		{
			//Por alguna razón no salió bien
			logger.warn(e);
			throw e;
		}
		
	}


	/**
	 * Implementación de cargar tipos de parto vaginal para 
	 * una BD Genérica
	 * 
	 * @see com.princetonsa.dao.HijoBasicoDao#cargarTiposPartoVaginal(Connection, int, int, int)
	 */
	public static ResultSetDecorator cargarTiposPartoVaginal (Connection con, int codigoPaciente, int numeroEmbarazo, int numeroHijo) throws SQLException
	{
		PreparedStatementDecorator cargarTiposPartoStatement= new PreparedStatementDecorator(con.prepareStatement(cargarTiposPartoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargarTiposPartoStatement.setInt(1, codigoPaciente);
		cargarTiposPartoStatement.setInt(2, numeroEmbarazo);
		cargarTiposPartoStatement.setInt(3, numeroHijo);
		return new ResultSetDecorator(cargarTiposPartoStatement.executeQuery());
	}

	/**
	 * Implementación de la búsqueda de un hijo en la Base de Datos Genérica
	 * 
	 * @see com.princetonsa.dao.HijoBasicoDao#existeHijo(Connection, int, int,
	 * int)
	 */
	public static boolean existeHijo (Connection con, int codigoPaciente, int numeroEmbarazo, int numeroHijo) throws SQLException
	{
		PreparedStatementDecorator buscarHijoStatement= new PreparedStatementDecorator(con.prepareStatement(buscarHijoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		buscarHijoStatement.setInt(1, codigoPaciente);
		buscarHijoStatement.setInt(2, numeroEmbarazo);
		buscarHijoStatement.setInt(3, numeroHijo);
		
		ResultSetDecorator rs=new ResultSetDecorator(buscarHijoStatement.executeQuery());
		if (rs.next())
		{
			if (rs.getInt("numResultados")<1)
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		else
		{
			//Si esta consulta no da un resultado (es un count) es porque algo muy, pero
			//muy malo esta pasando
			throw new SQLException ("Error Desconocido en la búsqueda de un hijo");
		}
	}

	/**
	 * Implementación de la búsqueda de un tipo de parto vaginal 
	 * en la Base de Datos Genérica
	 * 
	 * @see com.princetonsa.dao.HijoBasicoDao#existeTipoPartoVaginal (Connection con, int codigoPaciente, int numeroEmbarazo, int numeroHijo, int codigoTipoParto) throws SQLException
	 */
	public static boolean existeTipoPartoVaginal (Connection con, int codigoPaciente, int numeroEmbarazo, int numeroHijo, int codigoTipoParto) throws SQLException
	{
		PreparedStatementDecorator existeTipoPartoVaginalStatement= new PreparedStatementDecorator(con.prepareStatement(existeTipoPartoVaginalStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		existeTipoPartoVaginalStatement.setInt(1, codigoPaciente);
		existeTipoPartoVaginalStatement.setInt(2, numeroEmbarazo);
		existeTipoPartoVaginalStatement.setInt(3, numeroHijo);
		existeTipoPartoVaginalStatement.setInt(4, codigoTipoParto);
		
		ResultSetDecorator rs=new ResultSetDecorator(existeTipoPartoVaginalStatement.executeQuery());

		if (rs.next())
		{
			if (rs.getInt("numResultados")<1)
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		else
		{
			//Si esta consulta no da un resultado (es un count) es porque algo muy, pero
			//muy malo esta pasando
			throw new SQLException ("Error Desconocido en la búsqueda de un hijo");
		}
	}

}
