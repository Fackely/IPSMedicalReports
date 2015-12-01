/*
 * @(#)SqlBaseEmbarazoDao.java
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
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadCadena;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;

/**
 * Esta clase implementa la funcionalidad común a todas (o casi todas)
 * las BD utilizadas en axioma, en general se trata de las consultas que
 * utilizan SQL estándar. Métodos particulares a Embarazo
 *
 *	@version 1.0, Apr 1, 2004
 */
public class SqlBaseEmbarazoDao 
{
	/**
	 * Log para manejar los problemas de esta clase 
	 */
	private static Logger logger = Logger.getLogger(SqlBaseEmbarazoDao.class);
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar un
	 * embarazo (En antecedentes GinecoObstetricos)
	 */
	private static final String insertarEmbarazoStr = "INSERT INTO ant_gineco_embarazo ( codigo_paciente, codigo, meses_gestacion, fecha_terminacion, trabajo_parto, otro_trabajo_parto, fin_embarazo, duracion_trabajo_parto, tiempo_ruptura_membranas, legrado) VALUES (?, (SELECT count(1)+1 FROM ant_gineco_embarazo WHERE codigo_paciente=?), ?, ?, ?, ?, "+ValoresPorDefecto.getValorTrueParaConsultas()+", ?, ?, ?)";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para modificar un
	 * embarazo (En antecedentes GinecoObstetricos)
	 */
	private static final String modificarEmbarazoStr = "UPDATE ant_gineco_embarazo SET meses_gestacion = ?, fecha_terminacion = ?, trabajo_parto = ?, otro_trabajo_parto = ?, duracion_trabajo_parto=?, tiempo_ruptura_membranas=?, legrado=? WHERE codigo_paciente = ? AND codigo = ?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para averiguar
	 * si un embarazo (En antecedentes GinecoObstetricos) ya fue insertado o no
	 */
	private static final String buscarEmbarazoStr = "SELECT count(1) as numResultados FROM ant_gineco_embarazo WHERE codigo_paciente = ? AND codigo = ?"; 
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para sacar todos los
	 * hijos de este embarazo
	 */
	private static final String cargarHijosStr = "SELECT numero_hijo AS numeroHijoEmbarazo, vivo AS vivo, otro_tipo_parto_vaginal AS otroTipoPartoVaginal, cesarea AS cesarea, aborto AS aborto, otro_tipo_parto AS otroTipoParto, peso AS peso, sexo AS sexo, lugar AS lugar FROM informacion_hijo_parto WHERE codigo_paciente = ? AND ant_gineco_embarazo = ?";
	
	/**
	 * Cadena constante con el <i>statement</i> necesario para buscar el
	 * código de este embarazo
	 */
	private static final String buscarCodigoEmbarazoStr = "SELECT max(codigo) AS codigoEmbarazo FROM ant_gineco_embarazo WHERE codigo_paciente=? ";
	
	/*
	/**
	 * Implementación de Insertar un embarazo (de antecedentes gineco-
	 * obstetricos) en una BD Genérica
	 * 
	 * @see com.princetonsa.dao.EmbarazoDao#insertar (Connection , int , float ,
	 * String , int , String , int , String )
	 */
	/*
	 * Este método siempre tiene que ser transaccional
	 *
	public static int insertar (Connection con, int codigoPaciente, float mesesGestacion, String fechaTerminacion, int codigoComplicacion, String otraComplicacion, int codigoTrabajoParto, String otroTrabajoParto) throws SQLException
	{
		int resp0=0;
		if (con == null || con.isClosed()) 
		{
			DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			con = myFactory.getConnection();
		}
		
		PreparedStatementDecorator insertarEmbarazoStatement =  new PreparedStatementDecorator(con.prepareStatement(insertarEmbarazoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		insertarEmbarazoStatement.setInt(1, codigoPaciente);
		insertarEmbarazoStatement.setInt(2, codigoPaciente);
		insertarEmbarazoStatement.setFloat(3, mesesGestacion);

		if (UtilidadCadena.noEsVacio(fechaTerminacion))
		{
			insertarEmbarazoStatement.setString(4,fechaTerminacion);
		}
		else
		{
			insertarEmbarazoStatement.setString(4,null);
		}

		insertarEmbarazoStatement.setInt(5, codigoComplicacion);
		insertarEmbarazoStatement.setString(6, otraComplicacion);
		insertarEmbarazoStatement.setInt(7, codigoTrabajoParto);
		insertarEmbarazoStatement.setString(8, otroTrabajoParto);

		resp0 =  insertarEmbarazoStatement.executeUpdate();
		
		if (resp0<1)
		{
			return 0; 
		}
		else
		{
			PreparedStatementDecorator buscarCodigoEmbarazoStatement= new PreparedStatementDecorator(con.prepareStatement(buscarCodigoEmbarazoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			buscarCodigoEmbarazoStatement.setInt(1, codigoPaciente);
			
			ResultSetDecorator rs=buscarCodigoEmbarazoStatement.executeQuery());
			rs.next();
			
			return rs.getInt("codigoEmbarazo");
		}
	}*/

	/**
	 * Implementación de Insertar un embarazo (de antecedentes gineco-
	 * obstetricos) en una BD Genérica, soportando Transaccionalidad.
	 * @param secuencia Secuencia
	 * @param secuenciaOtras Otra Secuencia otras complicaciones
	 * @param duracion
	 * @param tiempoRupturaMembranas
	 * @param legrado
	 * @see com.princetonsa.dao.EmbarazoDao#insertarTransaccional(Connection,
	 * int, int, String, int, String, String)
	 */
	public static int insertarTransaccional (Connection con, int codigoPaciente, float mesesGestacion, String fechaTerminacion, int[] complicaciones, Vector otraComplicacion, int codigoTrabajoParto, String otroTrabajoParto, String estado, String secuencia, String secuenciaOtras, String duracion, String tiempoRupturaMembranas, String legrado) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		
		int resp0=0, resp1=0, codigoAntecedente=0;
		try
		{

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

			PreparedStatementDecorator buscarCodigoEmbarazoStatement= new PreparedStatementDecorator(con.prepareStatement(buscarCodigoEmbarazoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			buscarCodigoEmbarazoStatement.setInt(1, codigoPaciente);
			
			ResultSetDecorator rs=new ResultSetDecorator(buscarCodigoEmbarazoStatement.executeQuery());
			rs.next();
			
			codigoAntecedente= rs.getInt("codigoEmbarazo")+1;
			
			PreparedStatementDecorator insertarEmbarazoStatement =  new PreparedStatementDecorator(con.prepareStatement(insertarEmbarazoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			insertarEmbarazoStatement.setInt(1, codigoPaciente);
			insertarEmbarazoStatement.setInt(2, codigoPaciente);
			insertarEmbarazoStatement.setFloat(3, mesesGestacion);

			if( UtilidadCadena.noEsVacio(fechaTerminacion) )
			{
				insertarEmbarazoStatement.setString(4,fechaTerminacion);
			}
			else
			{
				insertarEmbarazoStatement.setString(4,null);
			}
			//insertarEmbarazoStatement.setInt(5, codigoComplicacion);
			//insertarEmbarazoStatement.setString(6, otraComplicacion);
			
			
			insertarEmbarazoStatement.setInt(5, codigoTrabajoParto);
			insertarEmbarazoStatement.setString(6, otroTrabajoParto);
			insertarEmbarazoStatement.setString(7, duracion);
			insertarEmbarazoStatement.setString(8, tiempoRupturaMembranas);
			insertarEmbarazoStatement.setString(9, legrado);
			resp1= insertarEmbarazoStatement.executeUpdate();

			String insertarComplicacionEmbarazoStr="INSERT INTO complicaciones_gineco(codigo, antecedente, paciente, complicacion) VALUES ("+secuencia+",?,?,?)";
			insertarEmbarazoStatement= new PreparedStatementDecorator(con.prepareStatement(insertarComplicacionEmbarazoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			for(int i=0; i<complicaciones.length; i++)
			{
				if(complicaciones[i]!=0)
				{
					insertarEmbarazoStatement.setInt(1, codigoAntecedente);
					insertarEmbarazoStatement.setInt(2, codigoPaciente);
					insertarEmbarazoStatement.setInt(3, complicaciones[i]);
					insertarEmbarazoStatement.executeUpdate();
				}
			}
			
			/*
			 * Insertar otras cimplicaciones
			 */
			if(otraComplicacion!=null && !otraComplicacion.isEmpty())
			{
				insertarComplicacionEmbarazoStr="INSERT INTO compli_gineco_otra(codigo, antecedente, paciente, otra_complicacion) VALUES ("+secuenciaOtras+",?,?,?)";
				insertarEmbarazoStatement= new PreparedStatementDecorator(con.prepareStatement(insertarComplicacionEmbarazoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				for(int i=0; i<otraComplicacion.size(); i++)
				{
					insertarEmbarazoStatement.setInt(1, codigoAntecedente);
					insertarEmbarazoStatement.setInt(2, codigoPaciente);
					insertarEmbarazoStatement.setString(3, otraComplicacion.elementAt(i)+"");
					insertarEmbarazoStatement.executeUpdate();
				}
			}
			
			if (resp0<1||resp1<1||codigoAntecedente<1)
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
			//Retornamos el código recien insertado
			return codigoAntecedente;
		}
		catch (SQLException e)
		{
			//Por alguna razón no salió bien
			e.printStackTrace();
			logger.warn(e);
			throw e;
		}
	}
	
	/*
	/**
	 * Implementación de Modificar un embarazo (de antecedentes gineco-
	 * obstetricos) en una BD Genérica.
	 * 
	 * @see com.princetonsa.dao.EmbarazoDao#modificar(Connection, int, int, int,
	 * String, int, String)
	 */
	/*
	public static int modificar (Connection con, int codigoPaciente, int codigoEmbarazo, float mesesGestacion, String fechaTerminacion, int[] complicacion, String otraComplicacion, int codigoTrabajoParto, String otroTrabajoParto) throws SQLException
	{
		if (con == null || con.isClosed()) 
		{
			DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			con = myFactory.getConnection();
		}

		PreparedStatementDecorator modificarEmbarazoStatement =  new PreparedStatementDecorator(con.prepareStatement(modificarEmbarazoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		modificarEmbarazoStatement.setFloat(1, mesesGestacion);
				
		if( UtilidadCadena.noEsVacio(fechaTerminacion) )
		{
			modificarEmbarazoStatement.setString(2, fechaTerminacion);
		}
		else
		{
			modificarEmbarazoStatement.setString(2, null);
		}		
		
		//modificarEmbarazoStatement.setInt(3, complicacion);
		modificarEmbarazoStatement.setString(4, otraComplicacion);
		modificarEmbarazoStatement.setInt(5, codigoTrabajoParto);
		modificarEmbarazoStatement.setString(6, otroTrabajoParto);
		modificarEmbarazoStatement.setInt(7, codigoPaciente);
		modificarEmbarazoStatement.setInt(8, codigoEmbarazo);
		
		
		return modificarEmbarazoStatement.executeUpdate();
		
	}*/
	
	/**
	 * Implementación de Modificar un embarazo (de antecedentes gineco-
	 * obstetricos) en una BD Genérica, soportando Transaccionalidad.
	 * @param secuencia Secuencia para manejar las complicaciones
	 * @param secuenciaOtras Secuencia para manejar otras complicaciones 
	 * @param duracion
	 * @param tiempoRupturaMembranas
	 * @param legrado
	 * @see com.princetonsa.dao.EmbarazoDao#modificarTransaccional(Connection,
	 * int, int, int, String, int, String, String)
	 */
	public static int modificarTransaccional (Connection con, int codigoPaciente, int codigoEmbarazo, float mesesGestacion, String fechaTerminacion, int[] complicaciones, Vector otraComplicacion, int codigoTrabajoParto, String otroTrabajoParto, String estado, String secuencia, String secuenciaOtras, String duracion, String tiempoRupturaMembranas, String legrado) throws SQLException
	{
		int resp0=0, resp1=0;
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		try
		{

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

			PreparedStatementDecorator modificarEmbarazoStatement =  new PreparedStatementDecorator(con.prepareStatement(modificarEmbarazoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			modificarEmbarazoStatement.setFloat(1, mesesGestacion);
			if( UtilidadCadena.noEsVacio(fechaTerminacion) )
			{
				modificarEmbarazoStatement.setString(2, fechaTerminacion);
			}
			else
			{
				modificarEmbarazoStatement.setString(2, null);
			}		
			//modificarEmbarazoStatement.setInt(3, complicacion);
			//modificarEmbarazoStatement.setString(3, otraComplicacion);
			modificarEmbarazoStatement.setInt(3, codigoTrabajoParto);
			modificarEmbarazoStatement.setString(4, otroTrabajoParto);
			modificarEmbarazoStatement.setString(5, duracion);
			modificarEmbarazoStatement.setString(6, tiempoRupturaMembranas);
			modificarEmbarazoStatement.setString(7, legrado);

			modificarEmbarazoStatement.setInt(8, codigoPaciente);
			modificarEmbarazoStatement.setInt(9, codigoEmbarazo);
			
			resp1= modificarEmbarazoStatement.executeUpdate();

			/*
			 * Modificar complicaciones
			 */
			String insertarComplicacionEmbarazoStr="INSERT INTO complicaciones_gineco(codigo, antecedente, paciente, complicacion) VALUES ("+secuencia+",?,?,?)";
			modificarEmbarazoStatement= new PreparedStatementDecorator(con.prepareStatement(insertarComplicacionEmbarazoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			for(int i=0; i<complicaciones.length; i++)
			{
				PreparedStatementDecorator existeStm= new PreparedStatementDecorator(con.prepareStatement("SELECT count(1) AS numResultados FROM complicaciones_gineco WHERE antecedente=? AND complicacion=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				existeStm.setInt(1, codigoEmbarazo);
				existeStm.setInt(2, complicaciones[i]);
				ResultSetDecorator resultado=new ResultSetDecorator(existeStm.executeQuery());
				boolean existe=false;
				if(resultado.next())
				{
					existe=resultado.getInt("numResultados")>0;
				}
				if(complicaciones[i]!=0 && !existe)
				{
					modificarEmbarazoStatement.setInt(1, codigoEmbarazo);
					modificarEmbarazoStatement.setInt(2, codigoPaciente);
					modificarEmbarazoStatement.setInt(3, complicaciones[i]);
					modificarEmbarazoStatement.executeUpdate();
				}
			}
			
			/*
			 * Modificar otras cimplicaciones
			 */
			if(otraComplicacion!=null && !otraComplicacion.isEmpty())
			{
				insertarComplicacionEmbarazoStr="INSERT INTO compli_gineco_otra(codigo, antecedente, paciente, otra_complicacion) VALUES ("+secuenciaOtras+",?,?,?)";
				modificarEmbarazoStatement= new PreparedStatementDecorator(con.prepareStatement(insertarComplicacionEmbarazoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				for(int i=0; i<otraComplicacion.size(); i++)
				{
					PreparedStatementDecorator existeStm= new PreparedStatementDecorator(con.prepareStatement("SELECT count(1) AS numResultados FROM compli_gineco_otra WHERE antecedente=? AND otra_complicacion=?",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					existeStm.setInt(1, codigoEmbarazo);
					existeStm.setString(2, otraComplicacion.elementAt(i)+"");
					ResultSetDecorator resultado=new ResultSetDecorator(existeStm.executeQuery());
					boolean existe=false;
					if(resultado.next())
					{
						existe=resultado.getInt("numResultados")>0;
					}
					if(!existe)
					{
						modificarEmbarazoStatement.setInt(1, codigoEmbarazo);
						modificarEmbarazoStatement.setInt(2, codigoPaciente);
						modificarEmbarazoStatement.setString(3, otraComplicacion.elementAt(i)+"");
						modificarEmbarazoStatement.executeUpdate();
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
	 * Implementación de la cargada de hijos en una Base de Datos Genérica
	 * 
	 * @see com.princetonsa.dao.EmbarazoDao#cargarHijos(Connection, String, String, int)
	 */

	public static ResultSetDecorator cargarHijos (Connection con, int codigoPaciente, int numeroEmbarazo) throws SQLException
	{
		PreparedStatementDecorator cargarHijosStatement= new PreparedStatementDecorator(con.prepareStatement(cargarHijosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		cargarHijosStatement.setInt(1, codigoPaciente);
		cargarHijosStatement.setInt(2, numeroEmbarazo);
		
		return new ResultSetDecorator(cargarHijosStatement.executeQuery());
	}


	/**
	 * Implementación de la búsqueda de un embarazo en una base de datos
	 * Genérica
	 * 
	 * @see com.princetonsa.dao.EmbarazoDao#existeEmbarazo(Connection, int, int)
	 */
	public static boolean existeEmbarazo(Connection con, int codigoPaciente, int numeroEmbarazo) throws SQLException
	{
		PreparedStatementDecorator buscarEmbarazoStatement= new PreparedStatementDecorator(con.prepareStatement(buscarEmbarazoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		buscarEmbarazoStatement.setInt (1, codigoPaciente);
		buscarEmbarazoStatement.setInt(2, numeroEmbarazo);
		
		ResultSetDecorator rs=new ResultSetDecorator(buscarEmbarazoStatement.executeQuery());
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
			throw new SQLException ("Error Desconocido en la búsqueda de un embarazo");
		}

	}
	
	/**
	 * Método para ingresar complicaciones
	 * @param con
	 * @param secuencia
	 * @param codigoEmbarazo
	 * @param codigoPaciente
	 * @param complicaciones
	 * @param complicacionesOtras
	 * @return numero de inserciones hechas en la BD
	 */
	public static int ingresarComplicaciones(Connection con, String secuencia, String secuenciaOtras, int codigoEmbarazo, int codigoPaciente, int[] complicaciones, Vector complicacionesOtras)
	{
		try
		{
			String sentencia="INSERT INTO complicaciones_gineco(codigo, antecedente, paciente, complicacion) VALUES("+secuencia+",?,?,?)";
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int contador=0;
			for(int i=0; i<complicaciones.length;i++)
			{
				
				pst.setInt(1, codigoEmbarazo);
				pst.setInt(2, codigoPaciente);
				pst.setInt(3, complicaciones[i]);
				pst.executeUpdate();
				contador++;
			}
			sentencia="INSERT INTO compli_gineco_otra(codigo, antecedente, paciente, otra_complicacion) VALUES("+secuenciaOtras+",?,?,?)";
			for(int i=0; i<complicacionesOtras.size();i++)
			{
				pst= new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1, codigoEmbarazo);
				pst.setInt(2, codigoPaciente);
				pst.setString(3, (String)complicacionesOtras.elementAt(i));
				pst.executeUpdate();
				contador++;
			}
			return contador;
		}
		catch (SQLException e)
		{
			logger.error("Error insertando las complicaciones : "+e);
			return 0;
		}
	}

}
