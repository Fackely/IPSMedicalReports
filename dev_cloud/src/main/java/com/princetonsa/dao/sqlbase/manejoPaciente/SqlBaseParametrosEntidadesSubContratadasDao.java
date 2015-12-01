package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.manejoPaciente.ParametrosEntidadesSubContratadas;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.Utilidades;


/**
 * 104/01/2008
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 *
 */

public class SqlBaseParametrosEntidadesSubContratadasDao
{
	
	/**
	 *Mensajes de error 
	 */
	static Logger logger = Logger.getLogger(SqlBaseParametrosEntidadesSubContratadasDao.class);

	/*----------------------------------------------------------------------------------------
	 *                         ATRIBUTOS PARAMETROS ENTIDADESSUBCONTRATADAS
	 -----------------------------------------------------------------------------------------*/
	
	/**
	 * String de consulta que conulta los datos de la tabla 
	 * det_param_entid_subcontratada
	 */
	private static final String strCadenaConsultaParametros="SELECT " +
																" dpes.codigo AS codigo0," +
																" dpes.codigo_param AS codigo_param1," +
																" dpes.convenio AS convenio2," +
																" coalesce(dpes.valor,'N') AS valor3," +
																" dpes.via_ingreso AS via_ingreso4, " +
																" dpes.nombre AS nombre5," +
																" dpes.centro_atencion AS centro_atencion9," +
																"'"+ConstantesBD.acronimoSi+"' AS esta_bd8 " +
															" FROM det_param_entid_subcontratada dpes " +
															" WHERE dpes.institucion =? AND dpes.codigo_param=?";	
	/**
	 * String de insercion de datos en la tabla det_param_entid_subcontratada
	 */
	private static final String strCadenaInsercionParametros = "INSERT INTO det_param_entid_subcontratada (codigo_param,convenio," +
															   "valor,usuario_modifica,fecha_modifica,hora_modifica,via_ingreso,institucion,nombre," +
															   "centro_atencion,codigo)" +
															   " VALUES (?,?,?,?,?,?,?,?,?,?,?)";
	
	

	private static final String strCadenaActualizacionParametros = "UPDATE det_param_entid_subcontratada SET valor=?,usuario_modifica=?," +
																    "fecha_modifica=?,hora_modifica=? WHERE institucion=? AND codigo_param=? AND nombre=?";
	
	
	
		
	private static final String [] indices = ParametrosEntidadesSubContratadas.indices;
	/*----------------------------------------------------------------------------------------
	 *                     FIN ATRIBUTOS PARAMETROS ENTIDADESSUBCONTRATADAS
	 -----------------------------------------------------------------------------------------*/
	
	/*----------------------------------------------------------------------------------------
	 *             METODOS PARA EL TRABAJO CON PARAMETROS ENTIDADES SUBCONTRATADAS
	 -----------------------------------------------------------------------------------------*/
	

	
	
	
	
	/**
	 * Metodo encargado de modificar los datos en la tabla 
	 * det_param_entid_subcontratada, puediendo filtrarlos
	 * por convenio, via de ingreso, institucion, nombre, seccion
	 * @param connection
	 * @param datos
	 * ---------------------------------------------
	 * 			KEY'S DEL HASHMAP DATOS
	 * ---------------------------------------------
	 * -- codigoParam1_ --> Requerido
	 * -- convenio2_ --> Opcional
	 * -- valor3_ --> --> Opcional
	 * -- usuarioModifica7_ -->Requerido
	 * -- viaIngreso4_ --> Opcional
	 * -- institucion6_ -->Requerido
	 * -- nombre5_ -->Requerido
	 @return boolean 
	 */
	public static boolean modificarParametros (Connection connection, HashMap datos)
	{
		//logger.info("\n entro a modificarParametros ==> "+datos);
		
		String cadena = strCadenaActualizacionParametros, where= "";
		//convenio
		if ( UtilidadCadena.noEsVacio(datos.get(indices[2])+"") && !(datos.get(indices[2])+"").equals(ConstantesBD.codigoNuncaValido+""))
			where+=" AND convenio="+datos.get(indices[2]);
		//via ingreso
		if (UtilidadCadena.noEsVacio(datos.get(indices[4])+"") && !(datos.get(indices[4])+"").equals(ConstantesBD.codigoNuncaValido+""))
			where+=" AND via_ingreso="+datos.get(indices[4]);
		
		//se pregunta por la centros de atencion
		if (UtilidadCadena.noEsVacio(datos.get(indices[9])+"") && !(datos.get(indices[9])+"").equals(ConstantesBD.codigoNuncaValido+""))
			where+=" AND centro_atencion="+datos.get(indices[9]);
		
		cadena+=where;
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * UPDATE det_param_entid_subcontratada SET 
			 * valor=?,
			 * usuario_modifica=?,
			 * fecha_modifica=?,
			 * hora_modifica=? 
			 * WHERE institucion=? 
			 * AND codigo_param=? 
			 * AND nombre=?
			 */
			
			
			//valor
			if (datos.containsKey(indices[3]) && !(datos.get(indices[3])+"").equals("") && !(datos.get(indices[3])+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setString(1, datos.get(indices[3])+"");
			else
				ps.setNull(1, Types.VARCHAR);
			
			//usuario_Modifica
			ps.setString(2, datos.get(indices[7])+"");
			//fecha modifica
			ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			//hora modifica
			ps.setString(4, UtilidadFecha.getHoraActual());
			//institucion
			ps.setInt(5, Utilidades.convertirAEntero(datos.get(indices[6])+""));
			//codigo_pram
			ps.setInt(6, Utilidades.convertirAEntero(datos.get(indices[1])+""));
			//nombre
			ps.setString(7, datos.get(indices[5])+"");
			
			if (ps.executeUpdate()>0)
				return true; 
			
		} 
		catch (SQLException e) 
		{
			logger.error("\n\n ::::::::::: Problema actualizando los datos en la tabla det_param_entid_subcontratada "+e);
		}
		return false;
	}
	
	
	/**
	 * Metodo encargado de guardar los datos en la tabla 
	 * det_param_entid_subcontratada
	 * @param connection
	 * @param datos
	 * ---------------------------------------------
	 * 			KEY'S DEL HASHMAP DATOS
	 * ---------------------------------------------
	 * -- codigoParam1_
	 * -- convenio2_
	 * -- valor3_
	 * -- usuarioModifica7_
	 * -- viaIngreso4_
	 * -- institucion6_
	 * -- nombre5_
	 @return boolean 
	 */
	public static boolean guardarParametros (Connection connection, HashMap datos)
	{
		
		//logger.info("\n entro a guardarParametros ==> "+datos);
		
		String cadena =strCadenaInsercionParametros;
		
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			/**
			 * INSERT INTO det_param_entid_subcontratada (codigo_param,convenio," +
															   "valor,usuario_modifica,fecha_modifica,hora_modifica,via_ingreso,institucion,nombre," +
															   "centro_atencion,codigo)" +
															   " VALUES (?,?,?,?,?,?,?,?,?,?)
			 */
			
			
			//codigo_pram
			ps.setInt(1, Utilidades.convertirAEntero(datos.get(indices[1])+""));
			
			//convenio
			if (datos.containsKey(indices[2]) && !(datos.get(indices[2])+"").equals("") && !(datos.get(indices[2])+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setInt(2, Utilidades.convertirAEntero(datos.get(indices[2])+""));
			else
				ps.setNull(2, Types.INTEGER);
			
			//valor
			if (datos.containsKey(indices[3]) && !(datos.get(indices[3])+"").equals("") && !(datos.get(indices[3])+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setString(3, datos.get(indices[3])+"");
			else
				ps.setNull(3, Types.VARCHAR);
			
			//usuario_Modifica
			ps.setString(4, datos.get(indices[7])+"");
			//fecha modifica
			ps.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
			//hora modifica
			ps.setString(6, UtilidadFecha.getHoraActual()+"");
			
			
			//via ingreso
			if (datos.containsKey(indices[4]) && !(datos.get(indices[4])+"").equals("") && !(datos.get(indices[4])+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setInt(7, Utilidades.convertirAEntero(datos.get(indices[4])+""));
			else
				ps.setNull(7, Types.INTEGER);
			
			//institucion
			ps.setInt(8, Utilidades.convertirAEntero(datos.get(indices[6])+""));
			//nombre
			ps.setString(9, datos.get(indices[5])+"");
			
			//centro atencion
			if (datos.containsKey(indices[9]) && !(datos.get(indices[9])+"").equals("") && !(datos.get(indices[9])+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setInt(10, Utilidades.convertirAEntero(datos.get(indices[9])+""));
			else
				ps.setNull(10, Types.INTEGER);
			
			ps.setInt(11,UtilidadBD.obtenerSiguienteValorSecuencia(connection, "seq_det_param_entid_subcontrat"));
				
			if (ps.executeUpdate()>0)
				return true; 
			
		} 
		catch (SQLException e) 
		{
			logger.error("\n\n :::::::::::Problema insertando datos en la tabla det_param_entid_subcontratada "+e);
		}
		
		
		return false;
	}
	
	
	/**
	 * Metodo encargado de consultar los datos
	 * del la tabla det_param_entid_subcontratada
	 * @param
	 * HashMap parametros
	 * ----------------------------------------------
	 * 			KEY'S DEL HASHMAP PARAMETROS
	 * ----------------------------------------------
	 *  -- codigoParam1_ --> Requerido
	 *  -- convenio2_ --> Opcional
	 *  -- viaIngreso4_ --> Opcional
	 *  -- institucion6_ --> Requerido
	 *  @return 
	 *  HashMap mapa 
	 *  -----------------------------------------------
	 *  			KEY'S DEL HASHMAP MAPA
	 *  -----------------------------------------------
	 *  -- codigo0_
	 *  -- codigoParam1_
	 *  -- convenio2_
	 *  -- valor3_
	 *  -- viaIngreso4_
	 *  -- nombre5_
	 *  -- institucion6_
	 *  -- usuarioModifica7_
	 */
	public static HashMap consultarParametros (Connection connection, HashMap parametros)
	{
		
		logger.info("\n\n entre a consultarParametros ==> "+parametros+"\n\n");
		
		HashMap mapa = new HashMap ();
		String cadena = strCadenaConsultaParametros, where = "  ";
		//se pregunta por el convenio
		if (UtilidadCadena.noEsVacio(parametros.get(indices[2])+"")  && !(parametros.get(indices[2])+"").equals(ConstantesBD.codigoNuncaValido+""))
			where+=" AND dpes.convenio="+parametros.get(indices[2]);
		//se pregunta por la via de ingreso
		if (UtilidadCadena.noEsVacio(parametros.get(indices[4])+"") && !(parametros.get(indices[4])+"").equals(ConstantesBD.codigoNuncaValido+""))
			where+=" AND dpes.via_ingreso="+parametros.get(indices[4]);
		//nombre --> que es el acronimo en la tabla integridad_dominio
		if (UtilidadCadena.noEsVacio(parametros.get(indices[5])+""))
			where+=" AND dpes.nombre='"+parametros.get(indices[5])+"'";
				
		//se pregunta por la centros de atencion
		if (UtilidadCadena.noEsVacio(parametros.get(indices[9])+"") && !(parametros.get(indices[9])+"").equals(ConstantesBD.codigoNuncaValido+""))
			where+=" AND dpes.centro_atencion="+parametros.get(indices[9]);
		
		cadena+=where;
		
		logger.info("\n cadena de consulta de consultarParametros es ==> "+cadena);
		try 
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(connection.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			//institucion
			ps.setInt(1, Utilidades.convertirAEntero(parametros.get(indices[6])+""));
			//codigo_param
			ps.setInt(2, Utilidades.convertirAEntero(parametros.get(indices[1])+""));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			
						
		}
		catch (SQLException e)
		{
			logger.info("\n Problema consultando los datos de la tabla det_param_entid_subcontratada "+e);
		}
		
		logger.info("\n \n al salir del sql "+mapa);
		mapa.put("INDICES_MAPA", indices);
		
		return mapa;
	}
	
	
	
	
	
	/*----------------------------------------------------------------------------------------
	 *            FIN METODOS PARA EL TRABAJO CON PARAMETROS ENTIDADES SUBCONTRATADAS
	 -----------------------------------------------------------------------------------------*/
	
}