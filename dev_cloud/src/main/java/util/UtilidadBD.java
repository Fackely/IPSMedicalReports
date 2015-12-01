/*
* @(#)UtilidadBD.java
*
* Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
*
* Lenguaje		: Java
* Compilador	: J2SDK 1.4.1_01
*
*/

package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.UtilidadesBDDao;
import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
* Clase con un conjunto de utilidades para efectuar operaciones
* relacionadas con Bases de Datos.
*
* @version Jul 1, 2003
* @author <a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
*/

public class UtilidadBD
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(UtilidadBD.class);
	
	/**
	* Dado un ResultSetDecorator (producto de una consulta SQL), lo encapsula en un
	* Collection de HashMaps, donde cada HashMap representa una fila del
	* RowSet; puedo recuperar el valor de cada columna dado su nombre y el
	* valor retornado es un Object del tipo correcto (String, Integer, etc.).
	* @param ars_rs ResultSetDecorator que se desea encapsular en un Collection
	* @return Collection de HashMaps copia del ResultSet
	*/
	public static Collection resultSet2Collection(ResultSetDecorator ars_rs) throws SQLException
	{
		return resultSet2Collection(ars_rs, true);
	}

	
	
	/**
	* Dado un ResultSetDecorator (producto de una consulta SQL), lo encapsula en un
	* Collection de HashMaps, donde cada HashMap representa una fila del
	* RowSet; puedo recuperar el valor de cada columna dado su nombre y el
	* valor retornado es un Object del tipo correcto (String, Integer, etc.).
	* @param ars_rs			ResultSetDecorator que se desea encapsular en un
	* 						Collection
	* @param lb_lowerCase	Indica si el nombre de las propiedades de cada
	*						elemento de la colección debe converirse a
	*						letras minúsculas
	* @return Collection de HashMaps copia del ResultSet
	*/
	public static Collection resultSet2Collection(ResultSetDecorator ars_rs, boolean ab_lowerCase) throws SQLException
	{
		Collection coleccion=new ArrayList();
		if(ars_rs!=null && ars_rs.getMetaData()!=null){
			ResultSetMetaData ars_rsm=ars_rs.getMetaData();
			while(ars_rs.next())
			{
				int numColumnas=ars_rsm==null?0:Utilidades.convertirAEntero(ars_rsm.getColumnCount()+"");
				HashMap mapa=new HashMap();
				for(int i=1;i<=ars_rsm.getColumnCount();i++)
				{
					if(ab_lowerCase)
						mapa.put((ars_rsm.getColumnLabel(i)).toLowerCase()+"",ars_rs.getObject(ars_rsm.getColumnLabel(i))==null||ars_rs.getObject(ars_rsm.getColumnLabel(i)).toString().equals(" ")?"":ars_rs.getObject(ars_rsm.getColumnLabel(i)));
					else
						mapa.put((ars_rsm.getColumnLabel(i))+"",ars_rs.getObject(ars_rsm.getColumnLabel(i))==null||ars_rs.getObject(ars_rsm.getColumnLabel(i)).toString().equals(" ")?"":ars_rs.getObject(ars_rsm.getColumnLabel(i)));
				}
				coleccion.add(mapa);
			}
			ars_rs.close();
		}
		logger.info("retornando coleccion");
		return coleccion;
		/*
		BasicDynaClass		lbdc_bdc;
		Collection			lc_c;
		DynaBean			ldb_newRow;
		DynaBean			ldb_oldRow;
		Iterator			li_i;
		ResultSetDynaClass	lrsdc_rsdc;

		lrsdc_rsdc	= new ResultSetDynaClass(ars_rs, ab_lowerCase);
		lbdc_bdc	= new BasicDynaClass("row", BasicDynaBean.class, lrsdc_rsdc.getDynaProperties() );
		lc_c		= new LinkedList();
		li_i		= lrsdc_rsdc.iterator();
		try
		{
			while(li_i.hasNext() )
			{
				ldb_oldRow = (DynaBean)li_i.next();
				ldb_newRow = lbdc_bdc.newInstance();
				PropertyUtils.copyProperties(ldb_newRow, ldb_oldRow);
		*/		
				/*
				for (int i=0;i<lrsdc_rsdc.getDynaProperties().length;i++)
				{
				    String nombreCampo=lrsdc_rsdc.getDynaProperties()[i].getName();
				    if (ldb_newRow.get(nombreCampo) instanceof BigDecimal)
				    {
				        BigDecimal h=(BigDecimal)ldb_newRow.get(nombreCampo);
				        if (sonIguales2(h.intValue(),h.doubleValue()))
				        {
					        ldb_newRow.set(nombreCampo, new Integer(h.intValue()));
				        }
				        else
				        {
				        }
				    }
				}*/
		/*
				lc_c.add(ldb_newRow);
			}
		}
		catch(IllegalAccessException iae)
		{
		}
		catch(InstantiationException ie)
		{
		}
		catch (InvocationTargetException ite)
		{
		}
		catch (NoSuchMethodException nsme)
		{
		}
		return lc_c;*/
	}

	public static boolean sonIguales2 (int numero1, double numero2)
	{
		if (numero1 + ConstantesBD.margenErrorDouble >= numero2 && numero2 + ConstantesBD.margenErrorDouble >= numero1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Método que crea un nuevo HashMap y lo llena
	 * con los datos presentes en un ResultSet,
	 * definiendo las columnas que se van a extraer
	 * del ResultSet
	 * 
	 * @param columnNames Arreglo de Strings con los
	 * nombres de las columnas que deseamos poner en
	 * el HashMap
	 * @param rs ResultSetDecorator del que extraeremos la
	 * información
	 * @param toLowerCase Si deseamos que deje los
	 * valores en minúscula
	 * @param use_ boolean que indica si se usará una
	 * sintaxis con [] o con subíndice _. Se recomienda
	 * trabajar con subíndices si se piensan cambiar los
	 * datos desde un jsp 
	 * @return
	 */
	public static RespuestaHashMap resultSet2HashMap(String[] columnNames, ResultSetDecorator rs, boolean toLowerCase, boolean use_ ){
	    RespuestaHashMap mapaAntiguo=new RespuestaHashMap();
	    mapaAntiguo.setTamanio(resultSet2HashMap(mapaAntiguo, columnNames, rs, toLowerCase, use_ ));
	    return mapaAntiguo;
	}
	
	/**
	 * Método que toma un HashMap previamente existente
	 * y lo llena con los datos presentes en un ResultSet,
	 * definiendo las columnas que se van a extraer
	 * del ResultSet. Retorna el número de elementos 
	 * agregados
	 * 
	 * @param mapaAntiguo Pareja mapa y tamaño que deseamos
	 * complementar
	 * @param columnNames Arreglo de Strings con los
	 * nombres de las columnas que deseamos poner en
	 * el HashMap
	 * @param rs ResultSetDecorator del que extraeremos la
	 * información
	 * @param toLowerCase Si deseamos que deje los
	 * valores en minúscula
	 * @param use_ boolean que indica si se usará una
	 * sintaxis con [] o con subíndice _. Se recomienda
	 * trabajar con subíndices si se piensan cambiar los
	 * datos desde un jsp 
	 * @return
	 */
	public static int resultSet2HashMap(RespuestaHashMap mapaAntiguo, String[] columnNames, ResultSetDecorator rs, boolean toLowerCase, boolean use_ )
	{
	    String keyValue = "";
	 	  String regValue = "";
	    int i, k=0;

	    if( rs == null)
	    {
	    	 mapaAntiguo.getMapa().put("numRegistros", (new Integer(0)));
	    	 return 0;
	    }
	    
	    try {
	    	if(rs.first() == false)
	        {
	    		    mapaAntiguo.getMapa().put("numRegistros", (new Integer(0)));
	            return 0;
	        }

	    	do{
	    		for( i=0; i<columnNames.length; i++){
	    		   if(!use_) 
	          keyValue = columnNames[i]  +"[" +k + "]";
	    		   else
	    		       keyValue = columnNames[i]  +"_" +k ;
	    		   
	    		 if(rs.getString(columnNames[i])==null)
	    		     regValue = "";
	    		 else
	    		     regValue = rs.getString(columnNames[i]); 
	        		          
	        	if( toLowerCase == true )
	        	    regValue = regValue.toLowerCase();

	          if(regValue == null)
	          	regValue = "";

	          mapaAntiguo.getMapa().put(keyValue, regValue); 
	         //logger.info("keyValue: " + keyValue + " - regValue: " + regValue);
	        }
			    k  +=  1;
	    	}while(rs.next());
	    	
	    	// adicionamos una entrada especial en el HashMap llamada "numRegistros" que contendra 
	    	// un Integer que almacena el valor de entradas completas ( registros ) 
	    	mapaAntiguo.getMapa().put("numRegistros", (new Integer(k)));
	    	
		   }
	     catch (Exception e) {
			   e.printStackTrace();
		   }
	   
	 	 //logger.info("El número de elementos en el mapa es : " + k);
	     return k;
	}

	/**
	 * Método que toma un HashMap previamente existente
	 * y lo llena con los datos presentes en un ResultSet,
	 * definiendo las columnas que se van a extraer
	 * del ResultSet. Retorna el número de elementos 
	 * agregados
	 * 
	 * @param mapaAntiguo Pareja mapa y tamaño que deseamos
	 * complementar
	 * @param columnNames Arreglo de Strings con los
	 * nombres de las columnas que deseamos poner en
	 * el HashMap
	 * @param rs ResultSetDecorator del que extraeremos la
	 * información
	 * @param toLowerCase Si deseamos que deje los
	 * valores en minúscula
	 * @param use_ boolean que indica si se usará una
	 * sintaxis con [] o con subíndice _. Se recomienda
	 * trabajar con subíndices si se piensan cambiar los
	 * datos desde un jsp 
	 * @return
	 */
	public static int resultSet2HashMapIndice(RespuestaHashMap mapaAntiguo, String[] columnNames, ResultSetDecorator rs, boolean toLowerCase, boolean use_, int index)
	{
		String keyValue = "";
	 	  String regValue = "";
	    int i, k=0;

	    if( rs == null)
	    {
	    	 mapaAntiguo.getMapa().put("numRegistros_"+index, (new Integer(0)));
	    	 return 0;
	    }
	    
	    try {
	    	if(rs.first() == false)
	        {
	    		    mapaAntiguo.getMapa().put("numRegistros_"+index, (new Integer(0)));
	            return 0;
	        }

	    	do{
	    		for( i=0; i<columnNames.length; i++){
	    		   if(!use_) 
	          keyValue = columnNames[i] +"_"+ index +"[" +k + "]";
	    		   else
	    		       keyValue = columnNames[i] + "_"+ index +"_" +k ;
	    		   
	    		 if(rs.getString(columnNames[i])==null)
	    		     regValue = "";
	    		 else
	    		     regValue = rs.getString(columnNames[i]); 
	        		          
	        	if( toLowerCase == true )
	        	    regValue = regValue.toLowerCase();

	          if(regValue == null)
	          	regValue = "";

	          mapaAntiguo.getMapa().put(keyValue, regValue); 
	         //logger.info("keyValue: " + keyValue + " - regValue: " + regValue);
	        }
			    k  +=  1;
	    	}while(rs.next());
	    	
	    	// adicionamos una entrada especial en el HashMap llamada "numRegistros" que contendra 
	    	// un Integer que almacena el valor de entradas completas ( registros ) 
	    	mapaAntiguo.getMapa().put("numRegistros", (new Integer(k)));
	    	mapaAntiguo.setTamanio(k);
	    	
		   }
	     catch (Exception e) {
			   e.printStackTrace();
		   }
	   
	 	 logger.info("El número de elementos en el mapa es : " + k);
	     return k;
	}

	
	/**
	 * Muchas de las consultas que se hacen en el sistema
	 * tienen como objetivo saber si existe o no al menos
	 * un registro. Como convención siempre se hace un
	 * count con el nombre numResultados. Este método
	 * existe para evitar la replicación de código
	 * (aunque existen muchas partes con este código
	 * legacy)
	 *  
	 * @param con Conexión con la fuente de datos
	 * @param llaveTabla Llave de la tabla a manejar
	 * @param numeroVecesUsoLlave Número de veces que
	 * se debe aplicar la llave en la consulta
	 * @param consulta String con la consulta a ejecutar
	 * @return
	 * @throws SQLException
	 */
	public static boolean countGenerico (Connection con, int llaveTabla, int numeroVecesUsoLlave, String consulta) throws SQLException
	{
		PreparedStatementDecorator ejecucionGenericaStatement=null;
		ResultSetDecorator rs=null;
		boolean result=false;
		try{
		int i=1;
			ejecucionGenericaStatement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			for (i=1;i<=numeroVecesUsoLlave;i++)
			{
				ejecucionGenericaStatement.setInt(1, llaveTabla);
			}
			rs=new ResultSetDecorator(ejecucionGenericaStatement.executeQuery());
			if (rs.next())
			{
			    if (rs.getInt("numResultados")>0)
			    {
			        result=true;
			    }
			    else
			    {
			        result=false;
			    }
			}
			else
			{
			    throw new SQLException ("No se encontraron resultados en un count en " + consulta);
			}
		}
		catch (Exception e) {
			logger.error("Error countGenerico", e);
		}
		finally{
			try{
				rs.close();
				ejecucionGenericaStatement.close();
			}
			catch (SQLException se) {
				logger.error("Error close ResulSet countGenerico", se);
			}
		}
		return result;
	}
	
	
	
	
	
	
	
	public static ResultSetDecorator executeSqlGenerico(Connection con, String consulta)	throws SQLException{
		PreparedStatementDecorator ejecucionGenericaStatement =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		ResultSetDecorator rs = new ResultSetDecorator(ejecucionGenericaStatement.executeQuery());
		return rs;
	}
	
	
	
	
	
	/**
	 * Método público genérico que agrupa la consulta común a la búsqueda
	 * para varios de los métodos definidos
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoTabla Código por el que se está buscando
	 * @param busquedaServicioEspecificaStr String con la sentencia 
	 * específica de la búsqueda deseada
	 * @param numParametros Número de veces que tiene que ir el
	 * código de la tabla en la búsqueda (Ej. Evolución revisa cargo- Caso
	 * Valoración Inicial o InterConsulta completa o la interconsulta incompleta,
	 * necesita 2)
	 * @return
	 * @throws SQLException
	 */
	public static int busquedaGenerica (Connection con, int codigoTabla, String busquedaServicioEspecificaStr, int numParametros, String nombreResultado) throws SQLException
	{
		PreparedStatementDecorator busquedaServicioGenericaStatement=null;
		ResultSetDecorator rs=null;
		try{
			int i=0;
			busquedaServicioGenericaStatement= new PreparedStatementDecorator(con.prepareStatement(busquedaServicioEspecificaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//Se pone <= porque los parámetros deben empezar en 1
			for (i=1;i<=numParametros;i++)
			{
				busquedaServicioGenericaStatement.setInt(i, codigoTabla);
			}
			rs=new ResultSetDecorator(busquedaServicioGenericaStatement.executeQuery());
			if (rs.next())
			{
				int resp=rs.getInt(nombreResultado);
				rs.close();
				return resp;
			}
			else
			{
				return 0;
			}
		}
		catch (Exception e) {
			logger.error("Error busquedaGenerica", e);
		}
		finally{
			try{
				rs.close();
				busquedaServicioGenericaStatement.close();
			}
			catch (SQLException se) {
				logger.error("Error close ResulSet busquedaGenerica", se);
			}
		}
		return 0;
	}
	
	public static UtilidadesBDDao utilidadBDDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadesBDao();
	}
	
	/**
	 * Metodo que retorna el año actural de una consecutivo dado su nombre y la conexion
	 * @param con
	 * @param nombreConsecutivo
	 * @param institucion
	 * @return
	 */
	public static String obtenerAnioActualTablaConsecutivos (Connection con,String nombreConsecutivo, int institucion)
	{
		String consulta="SELECT case when anio_vigencia is null then '' when anio_vigencia='-1' then '' else anio_vigencia end as anio_vigencia from consecutivos where nombre='"+nombreConsecutivo+"' and institucion="+institucion;
		ResultSetDecorator rs=null;
		try
		{
			rs=ejecucionGenericaResultSetDecorator(con,consulta);
			if(rs.next())
			{
				return rs.getString(1);
			}
		}
		catch(SQLException e)
		{
            logger.info("Se presento un error en  [UtilidadBD.obtenerAnioActualTablaConsecutivos]");
            e.printStackTrace();
		}
		finally{
			try{
				rs.close();
			}
			catch (SQLException se) {
				logger.error("Error close ResulSet obtenerAnioActualTablaConsecutivos", se);
			}
		}
		logger.info("No existe información del consecutivo->"+nombreConsecutivo);
		return ConstantesBD.codigoNuncaValido+"";
	}
	

	/**
	 * Metodo que retorna el valor actural de una consecutivo dado su nombre y la conexion
	 * @param con
	 * @param nombreConsecutivo
	 * @param institucion
	 * @return
	 */
	public static String obtenerValorActualTablaConsecutivos (Connection con,String nombreConsecutivo, int institucion)
	{
		String anioActual=UtilidadFecha.getMesAnoDiaFecha("anio", UtilidadFecha.getFechaActual(con))+"";
		String consulta="SELECT valor as valor from consecutivos where nombre='"+nombreConsecutivo+"' and institucion="+institucion +" and (anio_vigencia='"+anioActual+"' or anio_vigencia=''  or anio_vigencia=' ' or anio_vigencia is null)";
		ResultSetDecorator rs=null;
		try
		{
			rs=ejecucionGenericaResultSetDecorator(con,consulta);
			if(rs.next())
			{
				return rs.getString("valor");
			}
		}
		catch(SQLException e)
		{
            logger.error("Se presento un error en  [UtilidadBD.obtenerValorActualConsecutvio]", e);
		}
		finally{
			try{
				rs.close();
			}
			catch (SQLException se) {
				logger.error("Error close ResulSet UtilidadBD.obtenerValorActualConsecutvio", se);
			}
		}
		logger.info("No existe información del consecutivo->"+nombreConsecutivo);
		return ConstantesBD.codigoNuncaValido+"";
	}
	
	
	/**
	 * 
	 * @param nombreConsecutivo
	 * @param institucion
	 * @return
	 */
	public synchronized static String obtenerValorConsecutivoDisponible (String nombreConsecutivo, int institucion)
	{
		String resultado=ConstantesBD.codigoNuncaValido+"";
		String consulta="";
		String sentenciaBloqueo="";
		int tipoBD = DaoFactory.getConstanteTipoBD(System.getProperty("TIPOBD"));
		switch(tipoBD){
			case DaoFactory.POSTGRESQL:
				consulta="SELECT siguienteConsdisponible('"+nombreConsecutivo+"',"+institucion+",'"+UtilidadFecha.getMesAnioDiaActual("anio")+"') as valor";
				//sentenciaBloqueo="lock uso_consecutivos";
				break;
			case DaoFactory.ORACLE:
				consulta="select siguienteConsdisponible('"+nombreConsecutivo+"',"+institucion+",'"+UtilidadFecha.getMesAnioDiaActual("anio")+"') as valor from dual";
				//sentenciaBloqueo="lock TABLE uso_consecutivos IN SHARE MODE";
				break;
			default:
				return ConstantesBD.codigoNuncaValido+"";
		}
		
		resultado=SqlBaseUtilidadesDao.obtenerValorConsecutivoDisponible(consulta, sentenciaBloqueo);
		Log4JManager.info("consulta -->"+consulta+" resultado_consecutivo--->"+resultado);
		if(Utilidades.convertirAEntero(resultado)<0)
		{
			logger.info("error consecutivo:"+consulta+" resultado_consecutivo--->"+resultado);
		}
		return resultado;
	}
	
	/**
	 * 
	 * @param nombreConsecutivo
	 * @param institucion
	 * @param valor
	 * @return
	 */
	public static String obtenerAnioConsecutivo(String nombreConsecutivo, int institucion, String valor )
	{
		String resultado="";
		Connection con=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try
		{
			String consulta="SELECT case when anio_vigencia is null then '' when anio_vigencia='-1' then '' else anio_vigencia end as anio_vigencia from uso_consecutivos " +
			"where " +
			" nombre='"+nombreConsecutivo+"' and " +
			" institucion="+institucion+" and " +
			" valor="+valor;	
			con=UtilidadBD.abrirConexion();
			pst=con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs=pst.executeQuery();
			if(rs.next())
			{
				resultado=rs.getObject(1)+"";
			}
		}
		catch(SQLException se)
		{
            logger.error("Se presento un error SQL en  [UtilidadBD.obtenerAnioConsecutivo]", se);
        }
		catch(Exception e)
		{
            logger.error("Se presento un error en  [UtilidadBD.obtenerAnioConsecutivo]", e);
        }
		finally{
			try{
				if(rs!= null){
					rs.close();
				}
				if(pst!=null){
					pst.close();
				}
				UtilidadBD.closeConnection(con);
			}
			catch (SQLException e) {
				logger.error("ERROR cerrando Objetos Persistentes", e);
			}
		}
		return resultado;
	}
	
	/**
	 *  * Este metodo no puede recibir la conexion ya que no puede ir transaccional
	 * @param con
	 * @param nombreConsecutivo
	 * @param institucion
	 * @param valor
	 * @param con
	 * @return
	 */
	public synchronized static boolean cambiarUsoFinalizadoConsecutivo(Connection con, String nombreConsecutivo,int institucion,String valor,String usado, String finalizado)
	{
		boolean respuesta=false;
		PreparedStatement pst=null;
		try
		{
			String consulta="UPDATE administracion.uso_consecutivos set usado='"+usado+"',finalizado='"+finalizado+"' " +
			"where " +
					" nombre='"+nombreConsecutivo+"' and " +
					" institucion="+institucion+" and " +
					" valor="+valor+" and " +
					" (anio_vigencia='"+UtilidadFecha.getMesAnioDiaActual("anio")+"' or anio_vigencia=''  or anio_vigencia=' ' or anio_vigencia='-1' or anio_vigencia is null)";
			logger.info("finalizando consecutivo --->"+consulta);
			pst= con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			pst.executeUpdate();
			respuesta=true;
		}
		catch(SQLException se)
		{
            logger.error("Se presento un error SQL en  [UtilidadBD.cambiarUsoFinalizadoConsecutivo]",se);
		}
		catch(Exception e)
		{
            logger.error("Se presento un error SQL en  [UtilidadBD.cambiarUsoFinalizadoConsecutivo]",e);
		}
		finally{
			try{
				if(pst!=null){
					pst.close();
				}
			}
			catch (SQLException e) {
				logger.error("ERROR cerrando Objetos Persistentes", e);
			}
		}
		return respuesta;
	}
	
	/**
	 * 
	 * @param con
	 * @param nombreConsecutivo
	 * @param institucion
	 * @param valor
	 * @param usado
	 * @param finalizado
	 * @return
	 */
	public static boolean cambiarUsoFinalizadoConsecutivo( String nombreConsecutivo,int institucion,String valor,String usado, String finalizado)
	{
		boolean respuesta=false;
		Connection con=UtilidadBD.abrirConexion();
		try
		{
			respuesta=cambiarUsoFinalizadoConsecutivo(con, nombreConsecutivo, institucion, valor, usado, finalizado);
		}
		catch(Exception e)
		{
			logger.error("error finalizando cons:",e);
		}
		finally
		{
			UtilidadBD.closeConnection(con);
		}
		
		return respuesta;
	}
	

	
	
	/**
	 * Debido a que gran parte de las consultas de la aplicación 
	 * simplemente ejecutan una consulta, pasando un número de
	 * veces limitada la llave primaria y convirtiendo el
	 * ResultSetDecorator en colección, se creó este método genérico
	 * para ahorrar código repetido
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param llaveTabla Llave de la tabla a manejar
	 * @param numeroVecesUsoLlave Número de veces que
	 * se debe aplicar la llave en la consulta
	 * @param consulta String con la consulta a ejecutar
	 * @return
	 * @throws SQLException
	 */
	public static Collection ejecucionGenerica (Connection con, int llaveTabla, int numeroVecesUsoLlave, String consulta) throws SQLException
	{
	    ResultSetDecorator rs=ejecucionGenericaResultSetDecorator (con, llaveTabla, numeroVecesUsoLlave, consulta) ; 
		Collection aRetornar=UtilidadBD.resultSet2Collection(rs);
		rs.close();
		return aRetornar;
	}
	
	/**
	 * Debido a que gran parte de las consultas de la aplicación 
	 * simplemente ejecutan una consulta, pasando un número de
	 * veces limitada la llave primaria, retornando ResultSet,
	 * se creó este método genérico para ahorrar código repetido
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param llaveTabla Llave de la tabla a manejar
	 * @param numeroVecesUsoLlave Número de veces que
	 * se debe aplicar la llave en la consulta
	 * @param consulta String con la consulta a ejecutar
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetDecorator ejecucionGenericaResultSetDecorator (Connection con, int llaveTabla, int numeroVecesUsoLlave, String consulta) throws SQLException
	{
		int i=1;
		PreparedStatementDecorator ejecucionGenericaStatement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		for (i=1;i<=numeroVecesUsoLlave;i++)
		{
			ejecucionGenericaStatement.setInt(i, llaveTabla);
		}
		return new ResultSetDecorator(ejecucionGenericaStatement.executeQuery());
	}
	
	/**
	 * Debido a que gran parte de las consultas de la aplicación 
	 * simplemente ejecutan una consulta que retorna ResultSet,
	 * se creó este método genérico para ahorrar código repetido
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param consulta String con la consulta a ejecutar
	 * @return
	 * @throws SQLException
	 */
	public static ResultSetDecorator ejecucionGenericaResultSetDecorator (Connection con, String consulta) throws SQLException
	{
		PreparedStatementDecorator ejecucionGenericaStatement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		return new ResultSetDecorator(ejecucionGenericaStatement.executeQuery());
	}
	
	/**
	 * Debido a que gran parte de los updates de la aplicación 
	 * son similares, se creó este método para evitar la
	 * replicación de código. 
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param llaves arreglo de enteros con los que
	 * se va a llevar a cabo la actualización
	 * @param consulta String con el update a ejecutar
	 * @return
	 * @throws SQLException
	 */
	public static int updateGenerico (Connection con, int llaves[], String consulta) throws SQLException
	{
		int i=1;
		if (llaves==null || llaves.length<1)
		{
		    return 0;
		}
		PreparedStatementDecorator ejecucionGenericaStatement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		for (i=1;i<=llaves.length;i++)
		{
			ejecucionGenericaStatement.setInt(i, llaves[i-1]);
		}
		return ejecucionGenericaStatement.executeUpdate();
	}
	/**
	* Método en que se cierra la conexión (Buen manejo
	* recursos), revisa que no sea nula
	* 
	* @param con Conexión con la fuente de datos
	* @throws SQLException
	*/
	public static void cerrarConexion (Connection con) throws SQLException
	{
		try {
			if (con != null && !con.isClosed()) {
				con.close();
				Date fecha= new Date();
				SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy  HH:mm:ss");		
				logger.info("Cerrando conexion:  ### Número Conexiones ["+ dateFormatter.format(fecha)+"] - Activas: " + DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getNumeroConeccionesActivas()+" Inactivas: "+DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getNumeroConeccionesInactivas());
			}
		} catch (Exception e) {
			logger.error("Error al tratar de cerrar la conexion con la fuente de datos - cerrarConexion: " + e);
		}	
	}	
	/**
	 * Método en que se cierra la conexión (Buen manejo
	 * recursos), usado ante todo al momento de hacer un forward
	 * @param con Conexión con la fuente de datos
	 */
	public static void closeConnection (Connection con)
	{
	    try{
	        if (con != null && !con.isClosed()) {
	        	con.close();
	        	Date fecha= new Date();
	    		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy  HH:mm:ss");	
	    		logger.info("Close Connection ### Número Conexiones ["+ dateFormatter.format(fecha)+"] - Activas: " + DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getNumeroConeccionesActivas()+" Inactivas: "+DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getNumeroConeccionesInactivas());
	        }
	    }
	    catch(Exception e){
	        logger.error("Error al tratar de cerrar la conexion con la fuente de datos - closeConnection: " + e);
	    }
	}

	/**
	 * Metodo que abre una conexion, es requisito cerrarla (utilizar UtilidadBD.cerrarConexion)
	 * @return
	 */
	public static Connection abrirConexion ()
	{
	    Connection con;
	    try
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			con = myFactory.getConnection();
			return con;
		}
		catch(SQLException e) {
			logger.info("Error al tratar de abrir la conexion con la fuente de datos - abrirConexion: " + e);
			return null;
		}
	}
	
	/**
	 * Metodo que abre una conexion, es requisito cerrarla (utilizar UtilidadBD.cerrarConexion)
	 * @return
	 */
	public static Connection abrirConexionNoPool ()
	{
	    Connection con;
	    try
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			con = myFactory.getConnectionNoPool();
			return con;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
			logger.info("valor de error >> "+e);
			return null;
		}
	}
	
	/**
	 * Metodo que abre una conexion, es requisito cerrarla (utilizar UtilidadBD.cerrarConexion)
	 * @return
	 */
	public static Connection abrirConexion (String tipoBD)
	{
	    Connection con;
	    try
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(tipoBD);
			con = myFactory.getConnection();
			return con;
		}
		catch(SQLException e)
		{
			logger.error("Error al tratar de abrir la conexion con la fuente de datos - abrirConexion2: " + e);
			return null;
		}
	}
	
	
	
	/**
	 * Método que devuelve el siguiente valor de la secuencia para inserciones en BD 
	 * @param con
	 * @param nombreSecuencia
	 * @return
	 */
	public static int obtenerSiguienteValorSecuencia(Connection con, String nombreSecuencia)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).incrementarValorSecuencia(con, nombreSecuencia);
	}
	
	/**
	 * Método que devuelve el siguiente valor de la secuencia para inserciones en BD 
	 * @param con
	 * @param nombreSecuencia
	 * @return
	 */
	public static int obtenerSiguienteValorSecuencia(String nombreSecuencia)
	{
		Connection con = UtilidadBD.abrirConexion();
		int resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).incrementarValorSecuencia(con, nombreSecuencia);
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	/**
	 * Método que devuelve el ultimo valor de la secuencia para inserciones en BD 
	 * @param con
	 * @param nombreSecuencia
	 * @return
	 */
	public static int obtenerUltimoValorSecuencia(Connection con, String nombreSecuencia)
	{
		int valor = 0;
		try
		{
			valor = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).obtenerUltimoValorSecuencia(con, nombreSecuencia);
		}
		catch(SQLException e)
		{
			logger.error("Error capturando el ultimo valor de la secuencia "+nombreSecuencia+": "+e);
			valor = 0;
		}
		return valor;
			
	}

	/**
	 * 
	 * @param con
	 * @param string
	 * @param filtro
	 */
	public static boolean bloquearRegistro(Connection con, String cadenaBloqueo, ArrayList filtro) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).bloquearRegistroActualizacion(con,cadenaBloqueo,filtro);
	}

	/**
	 * Método implementado para cerrar objetos de persistencia
	 * @param ps
	 * @param rs
	 * @param con
	 */
	public static void cerrarObjetosPersistencia(Statement ps,ResultSet rs,Connection con)
	{
		utilidadBDDao().cerrarObjetosPersistencia(ps, rs, con);
	}
	
	

	/**
	 * @param con
	 * @return
	 */
	public static boolean iniciarTransaccion(Connection con) 
	{
		try {
			if(!con.isClosed())
				return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).beginTransaction(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @param con
	 */
	public static boolean iniciarTransaccionSinMensaje(Connection con)
	{
		try {
			if(!con.isClosed())
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).beginTransactionSinMensaje(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * @param con
	 * @return
	 */
	public static void abortarTransaccion(Connection con) 
	{
		try {
			if(!con.isClosed())
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransaction(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @param con
	 * @return
	 */
	public static void abortarTransaccionSinMensaje(Connection con) 
	{
		try {
			if(!con.isClosed())
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).abortTransactionSinMensaje(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param con
	 * @return
	 */
	public static void finalizarTransaccionSinMensaje(Connection con) 
	{
		try {
			if(!con.isClosed())
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).endTransactionSinMensaje(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @param con
	 * @return
	 */
	public static void finalizarTransaccion(Connection con) 
	{
		try {
			if(!con.isClosed())
				DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).endTransaction(con);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo implementado para realizar una busqueda avanzada,por medio de un HashMap 
	 * que contiene los objetos con los respectivos nombres de los campos y los valores
	 * de cada uno de ellos por los cuales se filtra la busqueda.
	 * <code>numRegBusqueda</code> es la llave que lleva por defecto el mapa, con el número de 
	 * registros que se van adicionando al mapa, para la busqueda por campos.
	 * <code>1. </code>El mapa contendra objetos de tipo InfoDatos(id,value,descripcion), para las 
	 * columnas por las cuales se filtrara la busqueda, siendo el id=campo de la tabla, 
	 * el value=valor a buscar y la descripcion=operador(=,!=,is,not is).
	 * El parametro value del InfoDatos varia de tipo segun el tipo de Dato que alvergue 
	 * el campo de la tabla, pudiendo ser Varchar,Integer ó Float. Por ello la clase InfoDatos 
	 * contiene 3 constructores distintos (String,String,String),(String,itn,String) y (String,double,String). 
	 * La llave del mapa para identificar los registros tendra el siguiente formato 
	 * <code><campo_index></code>,siendo el index un <code>int</code> que comienza en 0 y
	 * se incrementa sucesivamente segun el numero de registros.
	 * <code>2. </code>Si la consulta posee inner join con otras tablas la llave para
	 * identificar los objetos tiene el formato <code><inner_index></code>, que hace 
	 * referencia al objeto infodatos con el constructor(String id,String value),
	 * el id=nombre de la tabla y el value=los campos del inner(ej. a.codigo=f,codigo).
	 * @param con Connection, conexión con la fuente de datos
	 * @param mapa HashMap, Con los campos y valores.
	 * @return ResultSet, Resultado de la consulta
	 * @author jarloc
	 * @see com.princetonsa.mundo.cartera.AprobacionAjustesEmpresaDao#consultaGenerica(java.sql.Connection,HashMap)
	 */
	public static ResultSetDecorator consultaGenerica(Connection con,HashMap mapa)
	{	    
	    String consulta="",p1="",p2="",p3="",p4="";
	    InfoDatos campo = new InfoDatos();
	    boolean existeWhere=false;
	    try 
	    {   
	        if(!mapa.isEmpty())
		    {
	            consulta="SELECT";	
	            if(mapa.containsKey("distinct")&&UtilidadTexto.getBoolean(mapa.get("distinct")+""))
	            {
	            	consulta+=" distinct ";
	            }
	            consulta+=" "+mapa.get("camposTraer")+"";
                consulta+=" "+"FROM";
                consulta+=" "+mapa.get("tabla")+"";
                if(mapa.containsKey("numRegInner"))
                {
	                for(int k=0;k<Integer.parseInt(mapa.get("numRegInner")+"");k++)
	                {
	                    campo=(InfoDatos)mapa.get("inner_"+k);
	                    consulta+=" "+"INNER JOIN "+campo.getId()+" ON ("+campo.getValue()+")";
	                }
                }
                if(mapa.containsKey("numRegLeft"))
                {
	                for(int k=0;k<Integer.parseInt(mapa.get("numRegLeft")+"");k++)
	                {
	                    campo=(InfoDatos)mapa.get("left_"+k);
	                    consulta+=" "+"LEFT OUTER JOIN "+campo.getId()+" ON ("+campo.getValue()+")";
	                }
                }
                
                if(mapa.containsKey("numRegBusqueda"))
                {
		            for(int k=0;k<Integer.parseInt(mapa.get("numRegBusqueda")+"");k++)
			        {	                	                
		                campo=(InfoDatos)mapa.get("campoBusqueda_"+k);
		                String valor="";
		                //String valor=(campo.getValue().equals("-1")?(campo.getValueInt()==ConstantesBD.codigoNuncaValido?campo.getValueDouble()+"":campo.getValueInt()+""):"'"+campo.getValue()+"'");
		                if(campo.getValue().equals("-1"))
		                {
		                   if(campo.getValueInt()==ConstantesBD.codigoNuncaValido)
		                       valor=campo.getValueDouble()+"";
		                   else
		                       valor=campo.getValueInt()+"";
		                }
		                else
		                {	                    
		                    if(!campo.getValue().equals("null") && !campo.getDescripcion().equals("in") && !campo.getDescripcion().equals("between") && !campo.getDescripcion().equals("LIKE"))
		                        valor="'"+campo.getValue()+"'";
		                    else
		                        valor=campo.getValue();
		                }
		                if(campo.getDescripcion().equals("in"))
		                {
		                    p1="(";
		                    p2=")";
		                }	
		                if(campo.getDescripcion().equals("LIKE"))
		                {
		                  p3=" UPPER(";  
		                  p4=") ";
		                  p1=" UPPER('%";
		                  p2="%') ";
		                }
		                
		                
			            if(!existeWhere)
				        {       
			                consulta+=" WHERE "+p3+campo.getId()+p4+" "+campo.getDescripcion()+" "+p1+valor+p2;
				            existeWhere=true;			            
				        }
				        else
				        {
				            consulta+=" AND "+p3+campo.getId()+p4+" "+campo.getDescripcion()+" "+p1+valor+p2;
				        }
			            if(campo.getDescripcion().equals("in"))
		                {
		                    p1="";p2="";
		                }
			            if(campo.getDescripcion().equals("like1"))
		                {
		                  p3="";p4="";p1="";p2="";
		                }
			        }
                }
	            if(mapa.containsKey("order"))
                {
	                campo=(InfoDatos)mapa.get("order");
	                consulta+=" ORDER BY "+campo.getId()+" "+campo.getValue();	                
                }
		    }
	        Log4JManager.info("consulta avanzada->"+consulta);
	        PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            return new ResultSetDecorator(ps.executeQuery());
	    }
	    catch (SQLException e) 
	    {            
	        e.printStackTrace();
	        return null;
	    }	    
	}

	/**
	 * Metodo que recibe un resultSet y retorna un mapa con la informacion que el contiene,
	 * con un atributo llamado numRegistros que tiene el numero registros en el mapa
	 * y los key del mapa son los alias puestos en la consulta mas el sufijo _index ( _0 , _1,......, _(numRegistros-1) );
	 * @author artotor. Jorge Armando Osorio Velasquez
	 * @param rs
	 * @return
	 */
	public static HashMap<Object, Object> cargarValueObject(ResultSetDecorator rs)
	{
		HashMap<Object, Object> mapa=new HashMap<Object, Object>();
		int cont=0;
		mapa.put("numRegistros","0");
		try
		{
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					mapa.put((rsm.getColumnLabel(i)).toLowerCase()+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null||rs.getObject(rsm.getColumnLabel(i)).toString().equals(" ")?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			rs.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		mapa.put("numRegistros", cont+"");
		return (HashMap<Object, Object>)mapa.clone();
	}

	/**
	 * Metodo que recibe un resultSet y retorna un mapa con la informacion que el contiene,
	 * con un atributo llamado numRegistros que tiene el numero registros en el mapa
	 * y los key del mapa son los alias puestos en la consulta mas el sufijo _index ( _0 , _1,......, _(numRegistros-1) );
	 * @author artotor. Jorge Armando Osorio Velasquez
	 * @param rs
	 * @param indicesMayuscula, indica si los indices deben estar en mayuscula(true) o minuscula(false)
	 * @return
	 */
	public static HashMap<Object, Object> cargarValueObject(ResultSetDecorator rs,boolean indicesMayuscula)
	{
		HashMap<Object, Object> mapa=new HashMap<Object, Object>();
		int cont=0;
		mapa.put("numRegistros","0");
		try
		{
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					if(indicesMayuscula)
						mapa.put((rsm.getColumnLabel(i)+"").toUpperCase()+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null?"":rs.getObject(rsm.getColumnLabel(i)));
					else
						mapa.put((rsm.getColumnLabel(i)+"").toLowerCase()+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null?"":rs.getObject(rsm.getColumnLabel(i)));
				}
				cont++;
			}
			rs.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		mapa.put("numRegistros", cont+"");
		return (HashMap<Object, Object>)mapa.clone();
	}    
	
    /**
     * Metodo que recibe un resultSet y retorna un mapa con la informacion que el contiene,
     * los key del mapa son los alias puestos en la consulta mas el sufijo _index 
     * ( _0 , _1,......, _(numRegistros-1) );     
     * @param rs
     * @return
     */
    public static HashMap cargarValueObjectSinNumRegistros(ResultSetDecorator rs)
    {
        HashMap mapa=new HashMap();
        int cont=0;        
        try
        {
            ResultSetMetaData rsm=rs.getMetaData();
            while(rs.next())
            {
                for(int i=1;i<=rsm.getColumnCount();i++)
                {
                    mapa.put((rsm.getColumnLabel(i)).toLowerCase()+"_"+cont, rs.getObject(rsm.getColumnLabel(i)));
                }
                cont++;
            }
            if(cont!=0)
                mapa.put("numRegistros",cont+"");
            rs.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }        
        return (HashMap)mapa.clone();
    }

	/**
	 * Metodo que recibe un resultSet y retorna un mapa con la informacion que el contiene,
	 * con un atributo llamado numRegistros que tiene el numero registros en el mapa
	 * y los key del mapa son los alias puestos en la consulta mas el sufijo _index ( _0 , _1,......, _(numRegistros-1) );
	 * @author artotor. Jorge Armando Osorio Velasquez
	 * @param rs
	 * @param utilizarIndices Boleano que me indica si le pongo o no los indices
	 * @param cambiarAMayusculas Cuando encuentra un "_" cambia la siguiente letra a mayúscula
	 * @return
	 */
	public static HashMap cargarValueObject(ResultSetDecorator rs, boolean utilizarIndices, boolean cambiarAMayusculas)
	{
		HashMap mapa=new HashMap();
		int cont=0;
		mapa.put("numRegistros","0");
		try
		{
			ResultSetMetaData rsm=rs.getMetaData();
			while(rs.next())
			{
				for(int i=1;i<=rsm.getColumnCount();i++)
				{
					String alias=rsm.getColumnLabel(i).toLowerCase();
					if(cambiarAMayusculas)
					{
						
						int index=alias.indexOf("_");
						while(index>0)
						{
							index=alias.indexOf("_");
							try{
								String caracter=alias.charAt(index+1)+"";
								{
									alias=alias.replaceFirst("_"+caracter, caracter.toUpperCase());
								}
							}
							catch(IndexOutOfBoundsException e)
							{
								break;
							}
						}
						if(utilizarIndices)
						{
							mapa.put(alias+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null?"":rs.getObject(rsm.getColumnLabel(i)));
						}
						else
						{
							mapa.put(alias, rs.getObject(rsm.getColumnLabel(i))==null?"":rs.getObject(rsm.getColumnLabel(i)));
						}
					}
					else
					{
						if(utilizarIndices)
						{
							mapa.put(alias+"_"+cont, rs.getObject(rsm.getColumnLabel(i))==null?"":rs.getObject(rsm.getColumnLabel(i)));
						}
						else
						{
							mapa.put(alias, rs.getObject(rsm.getColumnLabel(i))==null?"":rs.getObject(rsm.getColumnLabel(i)));
						}
					}
				}
				cont++;
			}
			rs.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		mapa.put("numRegistros", cont+"");
		return (HashMap)mapa.clone();
	}
	/**
	 * Método para ingresar un dato a un prepardeStatement con todas las validaciones necesarias 
	 * @param stm
	 * @param valor
	 * @param posicion
	 * @param tipoDato
	 * @param puedeSerNull
	 * @param aceptaValor0 
	 * @return true en caso de que el dato se haya asignado en el statement
	 */
	public static boolean ingresarDatoAStatement(PreparedStatementDecorator stm, String valor, int posicion, int tipoDato, boolean puedeSerNull, boolean aceptaValor0)
	{
		if(stm==null)
		{
			logger.error("Debe iniciar el statement primero");
			return false;
		}
		try
		{
			if(valor==null)
			{
				if(puedeSerNull)
				{
					stm.setNull(posicion, tipoDato);
					return true;
				}
				else
				{
					return false;
				}
			}
			if(!aceptaValor0 && valor.equals("0"))
			{
				stm.setNull(posicion, tipoDato);
				return true;
			}
			if(valor.equals("") || valor.equals("-1"))
			{
				stm.setNull(posicion, tipoDato);
				return true;
			}
			switch (tipoDato)
			{
				case Types.INTEGER:
					try
					{
						stm.setInt(posicion, Integer.parseInt(valor));
						//logger.info("valor insertado="+Integer.parseInt(valor));
					}
					catch (NumberFormatException e)
					{
						logger.error("Cadena no representativa de un valor entero");
						return false;
					}
				break;
				case Types.FLOAT:
					try
					{
						stm.setFloat(posicion, Float.parseFloat(valor));
						//logger.info("valor insertado="+Integer.parseInt(valor));
					}
					catch (NumberFormatException e)
					{
						logger.error("Cadena no representativa de un valor flotante");
						return false;
					}
				break;
				case Types.DOUBLE:
					try
					{
						stm.setDouble(posicion, Double.parseDouble(valor));
						//logger.info("valor insertado="+Integer.parseInt(valor));
					}
					catch (NumberFormatException e)
					{
						logger.error("Cadena no representativa de un valor flotante");
						return false;
					}
				break;
				case Types.BOOLEAN:
					stm.setBoolean(posicion, UtilidadTexto.getBoolean(valor));
				break;
				case Types.VARCHAR:
					stm.setString(posicion, valor);
				break;
				default:
					stm.setString(posicion, valor);
				break;
			}
			return true;
		}
		catch (SQLException e)
		{
			logger.error("Error en el statement : "+e);
			return false;
		}
	}
	
	/**
	 * Metodo que indica si una consulta esta o no bloqueada (select for update) en caso de que sea verdadero entonces retorna true,
	 * POR FAVOR SOLO USAR ESTE METODO A NIVEL DEL DAO
	 * @param consulta
	 * @return
	 */
	public boolean estaConsultaBloqueada(String consulta)
	{
		return utilidadBDDao().estaConsultaBloqueada(consulta);
	}
	
	/**
	 * Metodo que devuelve la cantidad de veces
	 * que es utilizado un key en una tabla
	 * @param connection
	 * @param tabla
	 * @param nombreKey
	 * @param valuekey
	 * @return
	 */
	public static int estaUtilizadoEnTabla (Connection connection,String tabla,String nombreKey, int valuekey)
	{
		return utilidadBDDao().estaUtilizadoEnTabla(connection, tabla, nombreKey, valuekey);
	}

	/**
	 * 
	 * @param campo
	 * @param maximo
	 * @param minimo
	 * @return
	 */
	public static String convertirDatoClob(String campo, int maximo, int minimo) {
		int tipoBD = DaoFactory.getConstanteTipoBD(System.getProperty("TIPOBD"));
		String consulta="";
		switch(tipoBD){
				case DaoFactory.POSTGRESQL:
					consulta=" "+campo+" ";
					break;
				case DaoFactory.ORACLE:
					consulta="  dbms_lob.substr("+campo+", "+maximo+", "+minimo+") ";
					break;
				default:
					break;
			}
		return consulta;
	}

	/**
	 * 
	 */
	public static boolean actualizarNumeroProcesosBD() 
	{
		return utilidadBDDao().actualizarNumeroProcesosBD();
	}
	
	public static String ponerComillasAlias(String query) 
	{
		//quitamos primero los enter
		query=query.replaceAll("\n", " ");
		query=query.replaceAll(" FROM ", " from ");
		query=query.replaceAll("\tFROM ", " from ");
		query=query.replaceAll("\tFROM\t", " from ");
		query=query.replaceAll(" FROM\t", " from ");

		query=query.replaceAll(" AS ", " as ");
		query=query.replaceAll("\tAS ", " as ");
		query=query.replaceAll("\tAS\t", " as ");
		query=query.replaceAll(" AS\t", " as ");

		
		query=query.replaceAll(" As ", " as ");
		query=query.replaceAll("\tAs ", " as ");
		query=query.replaceAll("\tAs\t", " as ");
		query=query.replaceAll(" As\t", " as ");
		
		
		query=query.replaceAll(" aS ", " as ");
		query=query.replaceAll("\taS ", " as ");
		query=query.replaceAll("\taS\t", " as ");
		query=query.replaceAll(" aS\t", " as ");
		
		//verificamos la expresion " as XXXXXXXXX, "
		Pattern p = Pattern.compile("\\sas([\\s])+([\\w])+([\\s])*,");   // expresion
		Matcher m = p.matcher(query);  
		while(m.find()) 
		{
			query= query.replaceAll(m.group(), " as \""+deleteAsAndComma(m.group().toLowerCase()).trim()+"\",");
		}
		
		//verificamos la expresion " as XXXXXXX from "
		Pattern p1 = Pattern.compile("\\sas([\\s])+([\\w])+([\\s])*from");   // expresion
		Matcher m1 = p1.matcher(query);  
		while(m1.find()) 
		{
			query= query.replaceAll(m1.group(), " as \""+deleteAsAndFrom(m1.group().toLowerCase()).trim()+"\" from ");
		}
		
		logger.info("\n\n\nquery lower alias-->"+query+"\n\n\n");
		return query;

	}
	/**
	 * 
	 * @param lowerCase
	 * @return
	 */
	private static String deleteAsAndFrom(String query) 
	{
		query= query.replaceAll(" as ", " ");
		query= query.replaceAll(" from", " ");
		return query;
	}
	/**
	 * 
	 * @param query
	 * @return
	 */
	private static String deleteAsAndComma(String query) 
	{
		query= query.replaceAll(" as ", " ");
		query= query.replaceAll(",", " ");
		return query;
	}
	
	
	/**
	 * Método que se encarga de validar si se ha utilizado el consecutivo definido por centro de Atención
	 * teniendo en cuenta el año asociado al consecutivo si es enviado.
	 * 
	 * En el caso que no se envie el Año, se compara contra el valor retornado en el objeto {@link ResultadoBoolean}
	 * el cual contiene en el campo descripción el último valor asociado.
	 * 
	 * 
	 * @param nombreConsecutivo
	 * @param codigoInstitucion
	 * @param codigoCentroAtencion
	 * @param anio
	 * @return
	 */
	public static ResultadoBoolean isUtilizadoConsecutivoPorAnioCentroAten(String nombreConsecutivo, int codigoInstitucion,
			int codigoCentroAtencion, String anio)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true);
		
		String valor = "";
		String consulta="SELECT ucca.valor AS valor FROM administracion.uso_consecutivos_centro_aten ucca " +
							"WHERE " +
							" ucca.nombre=? AND " +
							" ucca.institucion=? AND " +
							" ucca.centro_atencion=? AND " +
							" ucca.usado = '"+ConstantesBD.acronimoSi+"' ";
		
		if(!UtilidadTexto.isEmpty(anio)){
			
			consulta += " AND ucca.anio_vigencia = ? " ;
		}
		
		consulta += " ORDER BY ucca.fecha_toma DESC LIMIT 1" ;
		
		Connection con=UtilidadBD.abrirConexion();

		try{
			
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			ps.setString(1, nombreConsecutivo);
			ps.setInt(2, codigoInstitucion);
			ps.setInt(3, codigoCentroAtencion);
			
			if(!UtilidadTexto.isEmpty(anio)){

				ps.setString(4, anio);
			}
			
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				valor = rs.getInt("valor")+"";
			}
		}
		catch(SQLException e)
		{
            logger.info("Se presento un error en  [UtilidadBD.obtenerAnioConsecutivo]");
            e.printStackTrace();
		}
		
		UtilidadBD.closeConnection(con);
		
		if(!UtilidadTexto.isEmpty(valor) && UtilidadTexto.isNumber(valor))
		{
			resultado.setDescripcion(valor);
			resultado.setResultado(false);
		}
		
		return resultado;
	}
	
}