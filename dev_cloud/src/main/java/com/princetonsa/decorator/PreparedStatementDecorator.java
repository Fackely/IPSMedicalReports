package com.princetonsa.decorator;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.NClob;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;

/**
 * 
 * @author axioma
 *
 */
public class PreparedStatementDecorator implements PreparedStatement 
{
	private static final int URL_TYPE=-1; 
	private static boolean BDbanejaBoolean;
	static{
		int tipoBD = DaoFactory.getConstanteTipoBD(System.getProperty("TIPOBD"));
		switch (tipoBD) {
			case DaoFactory.ORACLE:
				BDbanejaBoolean=false;
			break;
			case DaoFactory.POSTGRESQL:
				BDbanejaBoolean=true;
			break;
	
			default:
				BDbanejaBoolean=true;
			break;
		}
	}
	private class ParametrosPreparedStatement{
		
		int numeroParametro;
		Object valor;
		int tipo;
		public ParametrosPreparedStatement(int parameterIndex, Object x, int tipo) {
			numeroParametro=parameterIndex;
			valor=x;
			this.tipo=tipo;
		}
		/**
		 * @return the numeroParametro
		 */
		public int getNumeroParametro() {
			return numeroParametro;
		}
		/**
		 * @param numeroParametro the numeroParametro to set
		 */
		@SuppressWarnings("unused")
		public void setNumeroParametro(int numeroParametro) {
			this.numeroParametro = numeroParametro;
		}
		/**
		 * @return the valor
		 */
		public Object getValor() {
			return valor;
		}
		/**
		 * @param valor the valor to set
		 */
		@SuppressWarnings("unused")
		public void setValor(Object valor) {
			this.valor = valor;
		}
		/**
		 * @return the tipo
		 */
		public int getTipo() {
			return tipo;
		}
		/**
		 * @param tipo the tipo to set
		 */
		@SuppressWarnings("unused")
		public void setTipo(int tipo) {
			this.tipo = tipo;
		}
	}
	
	Logger logger=Logger.getLogger(PreparedStatementDecorator.class);
	
	/**
	 * PreparedStatement
	 */
	private PreparedStatement ps;
	
	/**
	 * Lista de parametros pasados al PreparedStatement
	 */
	ArrayList<ParametrosPreparedStatement> parametros=new ArrayList<ParametrosPreparedStatement>();
	
	/**
	 * Sentencia utilizada
	 */
	private String sentenciaSql;
	
	/**
	 * Constructor
	 * @param ps
	 */
	public PreparedStatementDecorator(PreparedStatement ps) {
		super();
		this.ps = ps;
		parametros=new ArrayList<ParametrosPreparedStatement>();
	}

	public PreparedStatementDecorator(Connection con, String sentenciaSql){
		super();
		parametros=new ArrayList<ParametrosPreparedStatement>();
		try {
			PreparedStatement psPreparedStatement=con.prepareStatement(sentenciaSql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			this.ps=psPreparedStatement;
		} catch (SQLException e) {
			logger.info("Error creando el Prepared Statement");
			e.printStackTrace();
		}
		this.sentenciaSql=sentenciaSql;
	}

	@Override
	public void addBatch() throws SQLException {
		this.getPs().addBatch();
	}

	@Override
	public void clearParameters() throws SQLException {
		this.getPs().clearParameters();
		parametros.clear();
	}

	@Override
	public boolean execute() throws SQLException {
		return this.getPs().execute();
	}

	@Override
	public ResultSet executeQuery() throws SQLException {
		return this.getPs().executeQuery();
	}

	@Override
	public int executeUpdate() throws SQLException {
		return this.getPs().executeUpdate();
	}

	@Override
	public ResultSetMetaData getMetaData() throws SQLException {
		return this.getPs().getMetaData();
	}

	@Override
	public ParameterMetaData getParameterMetaData() throws SQLException {
		return this.getPs().getParameterMetaData();
	}

	@Override
	public void setArray(int parameterIndex, Array x) throws SQLException {
		this.getPs().setArray(parameterIndex, x);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, x, Types.ARRAY));
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x)
			throws SQLException {
		this.getPs().setAsciiStream(parameterIndex, x);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, x, Types.OTHER));
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		this.getPs().setAsciiStream(parameterIndex, x, length);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, x, Types.OTHER));
	}

	@Override
	public void setAsciiStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		this.getPs().setAsciiStream(parameterIndex, x, length);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, x, Types.OTHER));
	}

	@Override
	public void setBigDecimal(int parameterIndex, BigDecimal x)
			throws SQLException {
		this.getPs().setBigDecimal(parameterIndex, x);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, x, Types.BIGINT));
	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x)
			throws SQLException {
		this.getPs().setBinaryStream(parameterIndex, x);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, x, Types.BINARY));
	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		this.getPs().setBinaryStream(parameterIndex, x, length);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, x, Types.BINARY));
	}

	@Override
	public void setBinaryStream(int parameterIndex, InputStream x, long length)
			throws SQLException {
		this.getPs().setBinaryStream(parameterIndex, x, length);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, x, Types.BINARY));
	}

	@Override
	public void setBlob(int parameterIndex, Blob x) throws SQLException {
		this.getPs().setBlob(parameterIndex, x);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, x, Types.BLOB));
	}

	@Override
	public void setBlob(int parameterIndex, InputStream inputStream)
			throws SQLException {
		this.getPs().setBinaryStream(parameterIndex, inputStream);
	}

	@Override
	public void setBlob(int parameterIndex, InputStream inputStream, long length)
			throws SQLException {
		this.getPs().setBlob(parameterIndex, inputStream, length);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, inputStream, Types.BLOB));
	}

	@Override
	public void setBoolean(int parameterIndex, boolean x) throws SQLException {
		this.getPs().setBoolean(parameterIndex, x);		
		parametros.add(new ParametrosPreparedStatement(parameterIndex, x, Types.BOOLEAN));
	}

	@Override
	public void setByte(int parameterIndex, byte x) throws SQLException {
		this.getPs().setByte(parameterIndex, x);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, x, Types.SMALLINT));
	}

	@Override
	public void setBytes(int parameterIndex, byte[] x) throws SQLException {
		this.getPs().setBytes(parameterIndex, x);
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader)
			throws SQLException {
		this.getPs().setCharacterStream(parameterIndex, reader);
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader, int length)
			throws SQLException {
		this.getPs().setCharacterStream(parameterIndex, reader, length);
	}

	@Override
	public void setCharacterStream(int parameterIndex, Reader reader,
			long length) throws SQLException {
		this.getPs().setCharacterStream(parameterIndex, reader, length);
	}

	@Override
	public void setClob(int parameterIndex, Clob x) throws SQLException {
		this.getPs().setClob(parameterIndex, x);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, x, Types.CLOB));
	}

	@Override
	public void setClob(int parameterIndex, Reader reader) throws SQLException {
		this.getPs().setClob(parameterIndex, reader);
	}

	@Override
	public void setClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		this.getPs().setClob(parameterIndex, reader, length);
	}

	@Override
	public void setDate(int parameterIndex, Date x) throws SQLException {
		this.getPs().setDate(parameterIndex, x);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, x, Types.DATE));
	}

	@Override
	public void setDate(int parameterIndex, Date x, Calendar cal)
			throws SQLException {
		this.getPs().setDate(parameterIndex, x, cal);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, x, Types.DATE));
	}

	@Override
	public void setDouble(int parameterIndex, double x) throws SQLException {
		this.getPs().setDouble(parameterIndex, x);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, x, Types.DOUBLE));
	}

	@Override
	public void setFloat(int parameterIndex, float x) throws SQLException {
		this.getPs().setFloat(parameterIndex, x);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, x, Types.FLOAT));
	}

	@Override
	public void setInt(int parameterIndex, int x) throws SQLException {
		this.getPs().setInt(parameterIndex, x);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, x, Types.INTEGER));
	}

	@Override
	public void setLong(int parameterIndex, long x) throws SQLException {
		this.getPs().setLong(parameterIndex, x);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, x, Types.BIGINT));
	}

	@Override
	public void setNCharacterStream(int parameterIndex, Reader value)
			throws SQLException {
		this.getPs().setNCharacterStream(parameterIndex, value);
	}

	@Override
	public void setNCharacterStream(int parameterIndex, Reader value,
			long length) throws SQLException {
		this.getPs().setNCharacterStream(parameterIndex, value, length);
	}

	@Override
	public void setNClob(int parameterIndex, NClob value) throws SQLException {
		this.getPs().setNClob(parameterIndex, value);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, value, Types.NCLOB));
	}

	@Override
	public void setNClob(int parameterIndex, Reader reader) throws SQLException {
		this.getPs().setNClob(parameterIndex, reader);
	}

	@Override
	public void setNClob(int parameterIndex, Reader reader, long length)
			throws SQLException {
		this.getPs().setNClob(parameterIndex, reader, length);
	}

	@Override
	public void setNString(int parameterIndex, String value)
			throws SQLException {
		this.getPs().setNString(parameterIndex, value);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, value, Types.NVARCHAR));
	}

	@Override
	public void setNull(int parameterIndex, int sqlType) throws SQLException {
		this.getPs().setNull(parameterIndex, sqlType);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, "NULL", Types.NULL));
	}

	@Override
	public void setNull(int parameterIndex, int sqlType, String typeName)
			throws SQLException {
		this.getPs().setNull(parameterIndex, sqlType, typeName);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, "NULL", Types.NULL));
	}

	@Override
	public void setObject(int parameterIndex, Object x) throws SQLException {
		this.getPs().setObject(parameterIndex, x);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, x, Types.JAVA_OBJECT));
	}

	@Override
	public void setObject(int parameterIndex, Object x, int targetSqlType)
			throws SQLException {
		this.getPs().setObject(parameterIndex, x, targetSqlType);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, x, Types.JAVA_OBJECT));
	}

	@Override
	public void setObject(int parameterIndex, Object x, int targetSqlType,
			int scaleOrLength) throws SQLException {
		this.getPs().setObject(parameterIndex, x, targetSqlType, scaleOrLength);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, x, Types.JAVA_OBJECT));
	}

	@Override
	public void setRef(int parameterIndex, Ref x) throws SQLException {
		this.getPs().setRef(parameterIndex, x);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, x, Types.REF));
	}

	@Override
	public void setRowId(int parameterIndex, RowId x) throws SQLException {
		this.getPs().setRowId(parameterIndex, x);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, x, Types.ROWID));
	}

	@Override
	public void setSQLXML(int parameterIndex, SQLXML xmlObject)
			throws SQLException {
		this.getPs().setSQLXML(parameterIndex, xmlObject);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, xmlObject, Types.SQLXML));
	}

	@Override
	public void setShort(int parameterIndex, short x) throws SQLException {
		this.getPs().setShort(parameterIndex, x);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, x, Types.SMALLINT));
	}

	@Override
	public void setString(int parameterIndex, String x) throws SQLException {
		this.getPs().setString(parameterIndex, x);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, x, Types.VARCHAR));
	}

	@Override
	public void setTime(int parameterIndex, Time x) throws SQLException {
		this.getPs().setTime(parameterIndex, x);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, x, Types.TIME));
	}

	@Override
	public void setTime(int parameterIndex, Time x, Calendar cal)
			throws SQLException {
		this.getPs().setTime(parameterIndex, x, cal);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, x, Types.TIME));
	}

	@Override
	public void setTimestamp(int parameterIndex, Timestamp x)
			throws SQLException {
		this.getPs().setTimestamp(parameterIndex, x);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, x, Types.TIMESTAMP));
	}

	@Override
	public void setTimestamp(int parameterIndex, Timestamp x, Calendar cal)
			throws SQLException {
		this.getPs().setTimestamp(parameterIndex, x, cal);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, x, Types.TIMESTAMP));
	}

	@Override
	public void setURL(int parameterIndex, URL x) throws SQLException {
		this.getPs().setURL(parameterIndex, x);
		parametros.add(new ParametrosPreparedStatement(parameterIndex, x, PreparedStatementDecorator.URL_TYPE));
	}

	@Override
	public void setUnicodeStream(int parameterIndex, InputStream x, int length)
			throws SQLException {
		throw new SQLException ("Este metodo ya esta depreciado, favor no utilizarlo");
		/*
		this.getPs().setUnicodeStream(parameterIndex, x, length);
		*/
	}

	@Override
	public void addBatch(String arg0) throws SQLException {
		this.getPs().addBatch(arg0);
	}

	@Override
	public void cancel() throws SQLException {
		this.getPs().cancel();
	}

	@Override
	public void clearBatch() throws SQLException {
		this.getPs().clearBatch();
	}

	@Override
	public void clearWarnings() throws SQLException {
		this.getPs().clearWarnings();
	}

	@Override
	public void close() throws SQLException {
		this.getPs().close();
		
	}

	/**
	 * Cerrar el PreparedStatement, evitando que queden cursores abiertos
	 * a la BD
	 */
	public void cerrarPreparedStatement(){
		try
		{
			this.getPs().close();
		} catch (SQLException e)
		{
			Log4JManager.error("Error cerrando el prepared statement", e);
		}
		
	}

	@Override
	public boolean execute(String arg0) throws SQLException {
		return this.getPs().execute(arg0);
	}

	@Override
	public boolean execute(String arg0, int arg1) throws SQLException {
		return this.getPs().execute(arg0, arg1);
	}

	@Override
	public boolean execute(String arg0, int[] arg1) throws SQLException {
		return this.getPs().execute(arg0, arg1);
	}

	@Override
	public boolean execute(String arg0, String[] arg1) throws SQLException {
		return this.getPs().execute(arg0,arg1);
	}

	@Override
	public int[] executeBatch() throws SQLException {
		return this.getPs().executeBatch();
	}

	@Override
	public ResultSet executeQuery(String arg0) throws SQLException {
		return this.getPs().executeQuery(arg0);
	}

	@Override
	public int executeUpdate(String arg0) throws SQLException {
		return this.getPs().executeUpdate(arg0);
	}

	@Override
	public int executeUpdate(String arg0, int arg1) throws SQLException {
		return this.getPs().executeUpdate(arg0,arg1);
	}

	@Override
	public int executeUpdate(String arg0, int[] arg1) throws SQLException {
		return this.getPs().executeUpdate(arg0, arg1);
	}

	@Override
	public int executeUpdate(String arg0, String[] arg1) throws SQLException {
		return this.getPs().executeUpdate(arg0, arg1);
	}

	@Override
	public Connection getConnection() throws SQLException {
		return this.getPs().getConnection();
	}

	@Override
	public int getFetchDirection() throws SQLException {
		return this.getPs().getFetchDirection();
	}

	@Override
	public int getFetchSize() throws SQLException {
		return this.getPs().getFetchSize();
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		return this.getPs().getGeneratedKeys();
	}

	@Override
	public int getMaxFieldSize() throws SQLException {
		return this.getPs().getMaxFieldSize();
	}

	@Override
	public int getMaxRows() throws SQLException {
		return this.getPs().getMaxRows();
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		return this.getPs().getMoreResults();
	}

	@Override
	public boolean getMoreResults(int arg0) throws SQLException {
		return this.getPs().getMoreResults(arg0);
	}

	@Override
	public int getQueryTimeout() throws SQLException {
		return this.getPs().getQueryTimeout();
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		return this.getPs().getResultSet();
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		return this.getPs().getResultSetConcurrency();
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		return this.getPs().getResultSetHoldability();
	}

	@Override
	public int getResultSetType() throws SQLException {
		return this.getPs().getResultSetType();
	}

	@Override
	public int getUpdateCount() throws SQLException {
		return this.getPs().getUpdateCount();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		return this.getPs().getWarnings();
	}

	@Override
	public boolean isClosed() throws SQLException {
		return this.getPs().isClosed();
	}

	@Override
	public boolean isPoolable() throws SQLException {
		return this.getPs().isPoolable();
	}

	@Override
	public void setCursorName(String arg0) throws SQLException {
		this.getPs().setCursorName(arg0);
	}

	@Override
	public void setEscapeProcessing(boolean arg0) throws SQLException {
		this.getPs().setEscapeProcessing(arg0);
	}

	@Override
	public void setFetchDirection(int arg0) throws SQLException {
		this.getPs().setFetchDirection(arg0);
	}

	@Override
	public void setFetchSize(int arg0) throws SQLException {
		this.getPs().setFetchSize(arg0);
	}

	@Override
	public void setMaxFieldSize(int arg0) throws SQLException {
		this.getPs().setMaxFieldSize(arg0);
	}

	@Override
	public void setMaxRows(int arg0) throws SQLException {
		this.getPs().setMaxRows(arg0);
	}

	@Override
	public void setPoolable(boolean arg0) throws SQLException {
		this.getPs().setPoolable(arg0);
	}

	@Override
	public void setQueryTimeout(int arg0) throws SQLException {
		this.getPs().setQueryTimeout(arg0);
	}

	@Override
	public boolean isWrapperFor(Class<?> arg0) throws SQLException {
		return this.getPs().isWrapperFor(arg0);
	}

	@Override
	public <T> T unwrap(Class<T> arg0) throws SQLException {
		return this.getPs().unwrap(arg0);
	}

	public PreparedStatement getPs() {
		return this.ps;
	}

	public void setPs(PreparedStatement ps) {
		this.ps = ps;
	}

	
	/**
	 * Destructor
	 */
	protected void finalize() throws Throwable
	{
		try {
			this.ps.close();
	    } catch(SQLException e) { 
	    	Log4JManager.error("FINALIZANDO PreparedStatementDecorator : " + e);
	    } finally {
	    	super.finalize();
	    }
	} 
	
	public String toString()
	{
		if(sentenciaSql!=null)
		{
			String parametrosStr;
			int numeroParametros=parametros.size();
			parametrosStr="Namero de parametros "+numeroParametros;
			String sentencia=sentenciaSql;
			for(int i=0; i<numeroParametros; i++)
			{
				ParametrosPreparedStatement pps=parametros.get(i);
				if(pps.getNumeroParametro()==i+1)
				{
					parametrosStr+="\nParametro "+pps.getNumeroParametro()+" Valor: "+pps.getValor();
					Object valor=pps.getValor();
					if(valor!=null)
					{
						switch(pps.getTipo())
						{
							case Types.INTEGER:
								sentencia=sentencia.replaceFirst("\\?", valor.toString());
							break;
							case Types.SMALLINT:
								sentencia=sentencia.replaceFirst("\\?", valor.toString());
							break;
							case Types.TINYINT:
								sentencia=sentencia.replaceFirst("\\?", valor.toString());
							break;
							case Types.BIGINT:
								sentencia=sentencia.replaceFirst("\\?", valor.toString());
							break;
							case Types.DOUBLE:
								sentencia=sentencia.replaceFirst("\\?", valor.toString());
							break;
							case Types.FLOAT:
								sentencia=sentencia.replaceFirst("\\?", valor.toString());
							break;
							case Types.DECIMAL:
								sentencia=sentencia.replaceFirst("\\?", valor.toString());
							break;
							case Types.VARCHAR:
								sentencia=sentencia.replaceFirst("\\?", "'"+valor.toString()+"'");
							break;
							case Types.NVARCHAR:
								sentencia=sentencia.replaceFirst("\\?", "'"+valor.toString()+"'");
							break;

							case Types.JAVA_OBJECT:
								sentencia=sentencia.replaceFirst("\\?", valor.toString());
							break;

							case Types.BINARY:
								sentencia=sentencia.replaceFirst("\\?", valor.toString());
							break;
							case Types.BOOLEAN:
								if(BDbanejaBoolean)
								{
									sentencia=sentencia.replaceFirst("\\?", valor.toString());
								}
								else
								{
									if((Boolean)valor)
									{
										sentencia=sentencia.replaceFirst("\\?", "'1'");
									}
									else
									{
										sentencia=sentencia.replaceFirst("\\?", "'0'");
									}
								}
							break;

							case Types.DATE:
								Date fecha=(Date) valor;
								sentencia=sentencia.replaceFirst("\\?", "'"+fecha.toString()+"'");
							break;

							case Types.ARRAY:
							break;

							case PreparedStatementDecorator.URL_TYPE:
								sentencia=sentencia.replaceFirst("\\?", "'"+valor.toString()+"'");
							break;

							case Types.NULL:
								sentencia=sentencia.replaceFirst("\\?", "null");
							break;

							default:
								logger.info("Objeto no soprtado para impresion");
						}
					}
					else
					{
						sentencia=sentencia.replaceFirst("\\?", "null");
					}
				}
				else{
					parametrosStr+="\nParametro "+pps.getNumeroParametro()+" Valor: [Objeto no soportado para impresion]";
					sentencia=sentencia.replaceFirst("\\?", "[Tipo no Soportado]");
				}
				
			}
			
			return parametrosStr+"\n"+sentencia;
		}
		return ps.toString();
	}

	@Override
	public void closeOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}
}
