/*
 * Creado el 23-sep-2005
 * por Julian Montoya
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * @author Julian Montoya
 * 
 * Princeton S.A. (ParqueSoft Manizales)
 */
@SuppressWarnings("rawtypes")
public class SqlBaseAntecedentesOftalmologicosDao {

	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseOrdenMedicaDao.class);
	
	
    /**
   	 * Funcion para retornar los tipos de enfermedes oftalmologicas familiares 
   	 * @param con
   	 * @param codigoPaciente
   	 * @param codigoInstitucion
   	 * @return Coleccion con el listado de tipos de enfermedades personales parametrizadas
   	 */
	public static Collection consultarTiposEnferOftalfamiliares (Connection con, int codigoInstitucion, int paciente) 
	  {
		String consulta="";

		
      	consulta = " SELECT eoi.codigo as codigo, teo.nombre as nombre, 0 as indicador" +
      			   "		FROM tipo_enfer_oftal teo " +
				   "	   		 INNER JOIN enfer_oftal_inst eoi ON (eoi.enfer_oftal = teo.codigo) " + 
				   "	    		   WHERE eoi.institucion = ? " + 
				   "		       		 AND ( eoi.tipo_antecedente = " + ConstantesBD.codigoTipoAntecedentePerFam + " OR " +
				   " 					       eoi.tipo_antecedente = " +  ConstantesBD.codigoTipoAntecedenteFamiliar + ") "+
      			   " UNION ALL " +     
				   " SELECT oeo.codigo as codigo, oeo.nombre as nombre, 1 as indicador " +
				   "        FROM otro_enfer_oftal oeo " +
				   "			 INNER JOIN ant_oftal_fam_det_otro aofdo ON (aofdo.otro_enfer_oftal = oeo.codigo) " + 
				   " 			 WHERE aofdo.paciente = ? ";
      			   
		try
		{
			PreparedStatementDecorator consultarNov =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultarNov.setInt(1, codigoInstitucion);
			consultarNov.setInt(2, paciente);

			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarNov.executeQuery()));				
		} 
		catch (SQLException e)
		{
			logger.error("Error Consultado Tipos de enfermedades oftalmologicas familiares" + e.toString());
			return null;
		} 
	  }	
  
	  /**
       * Metodo para consultar los tipos de parentescos registrados  
       * @param con
       * @return
       */
      public static Collection consultarTiposParentesco (Connection con) 
      {
      	String consulta = " SELECT codigo as codigo, nombre as nombre FROM tipos_parentesco";
      			   
		try
		{
			PreparedStatementDecorator consultarNov =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarNov.executeQuery()));				
		} 
		catch (SQLException e)
		{
			logger.error("Error Consultado Tipos de parentescos familiares" + e.toString());
			return null;
		} 
      }	
	  
	   
	/**
 	 * Funcion para retornar los tipos de enfermedes oftalmologicas personales 
 	 * @param con
 	 * @param codigoPaciente
 	 * @param codigoInstitucion
 	 * @return Coleccion con el listado de tipos de enfermedades personales parametrizadas
 	 */
     //-----------------------------------------------Los tipos parametrizados
      public static Collection consultarTiposEnferOftalPersonales(Connection con, int codigoInstitucion, int paciente)
      {
		String consulta="";

		
      	consulta = " SELECT eoi.codigo as codigo, teo.nombre as nombre, 0 as indicador" +
      			   "		FROM tipo_enfer_oftal teo " +
				   "	   		 INNER JOIN enfer_oftal_inst eoi ON (eoi.enfer_oftal = teo.codigo) " + 
				   "	    		   WHERE eoi.institucion = ? " + 
				   "		       		 AND ( eoi.tipo_antecedente = " + ConstantesBD.codigoTipoAntecedentePersonal + 
				   "					 	   OR eoi.tipo_antecedente = " + ConstantesBD.codigoTipoAntecedentePerFam +" ) " +
      			   " UNION ALL " +     
				   " SELECT oeo.codigo as codigo, oeo.nombre as nombre, 1 as indicador " +
				   "        FROM otro_enfer_oftal oeo " +
				   "			 INNER JOIN ant_oftal_perso_med_otro aopdo ON (aopdo.otro_enfer_oftal = oeo.codigo) " + 
				   " 			 WHERE aopdo.paciente = ? ";
			
      	
      			   
		try
		{
			PreparedStatementDecorator consultarNov =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consultarNov.setInt(1, codigoInstitucion);
			consultarNov.setInt(2, paciente);

			//System.out.print("\n\n\n Institucion  ---->" + codigoInstitucion + " \n\n\n");
			//System.out.print("\n\n\n consultarTiposEnferOftalPersonales ---->" + consulta + " \n\n\n");

			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consultarNov.executeQuery()));				
		} 
		catch (SQLException e)
		{
			logger.error("Error Consultado Tipos de enfermedades oftalmologicas "+e.toString());
			return null;
		}
    }
      
      /**
       * Funcion para insertar los tipos de enfermedades oftalmológicas para un determinado paciente  
       * @param con
       * @param paciente
       * @param tipoEnferOftal
       * @param desdeCuando (estimación desde cuando se tiene la enfermedad)
       * @param tratamiento (tratamiento que ha seguido)
       * @return 
       */
      
      public static int insertarEnferOftalPersoMedicos(Connection con, int paciente, int tipoEnferOftal, String desdeCuando, String tratamiento, int actualizar)
      {
		PreparedStatementDecorator ps;
		int resp=0, codigo=0;
		String insertar = "";

		try
			{
			
			
			    //-verficar si ya existe registro del paciente
			    codigo = verificarAntOftalPersonales(con, paciente);
			    if (codigo != 1)
			    {	
			    	//-existe el registro del paciente
			    	if  (actualizar==0) 
			    	{
						insertar = " INSERT INTO ant_oftal_perso_medicos (paciente, enfer_oftal_inst, desde_cuando, tratamiento) " +
						           "                                      VALUES (?, ?, ?, ?) ";
			    		ps =  new PreparedStatementDecorator(con.prepareStatement(insertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						ps.setInt(1, codigo);			
						ps.setInt(2, tipoEnferOftal);				
						ps.setString(3, desdeCuando);
						ps.setString(4, tratamiento);					
						resp = ps.executeUpdate();
			    	}
			    	else
			    	{
						insertar = " UPDATE ant_oftal_perso_medicos set tratamiento = ?, desde_cuando = ?  WHERE paciente = ? AND enfer_oftal_inst = ?";
			    		ps =  new PreparedStatementDecorator(con.prepareStatement(insertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						ps.setString(1, tratamiento);
						ps.setString(2, desdeCuando);
						ps.setInt(3, codigo);
						ps.setInt(4, tipoEnferOftal);				
						resp = ps.executeUpdate();
			    	}
			    }
			    else
			    {
					insertar = " INSERT INTO ant_oftal_perso_medicos (paciente, enfer_oftal_inst, desde_cuando, tratamiento) " +
							   "                                      VALUES (?, ?, ?, ?) ";

			    	codigo = insertarEnferOftalPersonales(con,paciente,"",-1);  
			    	if (codigo != -1)
			    	{
						ps =  new PreparedStatementDecorator(con.prepareStatement(insertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						ps.setInt(1, codigo);			
						ps.setInt(2, tipoEnferOftal);				
						ps.setString(3, desdeCuando);
						ps.setString(4, tratamiento);					
						resp = ps.executeUpdate();
			    	}
			    	else
			    	{
						logger.warn(" Error en la inserción de datos (insertarEnferOftalPersoMedicos) : SqlBaseAntecedentesOftalmologicosDao ");	
					}
			    }
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de datos (insertarEnferOftalPersoMedicos) : SqlBaseAntecedentesOftalmologicosDao "+e.toString() );
					resp = 0;
			}
			return resp;
      } 
      
      /**
       * Funcion para verificar si existe datos del paciente en la tabla ant_oftal_personales 
       * (indispensable para insertar datos en la tabla ant_oftal_perso_medicos) 
       * @param con
       * @param paciente : es el codigo del paciente
       * @return
       */
      
      public static int verificarAntOftalPersonales(Connection con, int paciente)
      {
		String consulta="SELECT paciente FROM ant_oftal_personales where paciente = ? ";
		
		try
		{
			PreparedStatementDecorator obtenerCodigoStatement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			obtenerCodigoStatement.setInt(1, paciente);
			
			ResultSetDecorator resultado=new ResultSetDecorator(obtenerCodigoStatement.executeQuery());
			if(resultado.next())
			{
				return resultado.getInt("paciente");
			}
			else
			{
				return -1;
			}	
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error verificando codigo de paciente en la tabla ant_oftal_personales : SqlBaseAntecedentesOftalmologicosDao (verificarAntOftalPersonales) "+e.toString());
			return -1;
		}

      }

      /**
       * Funcion para verificar si existe datos del paciente en la tabla ant_oftal_personales 
       * (indispensable para insertar datos en la tabla ant_oftal_perso_medicos) 
       * @param con
       * @param paciente : es el codigo del paciente
       * @return
       */
      
      public static int verificarAntOftalFamiliares(Connection con, int paciente)
      {
		String consulta="SELECT paciente FROM ant_oftal_familiares where paciente = ? ";
		
		try
		{
			PreparedStatementDecorator obtenerCodigoStatement= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			obtenerCodigoStatement.setInt(1, paciente);
			
			ResultSetDecorator resultado=new ResultSetDecorator(obtenerCodigoStatement.executeQuery());
			if(resultado.next())
			{
				return resultado.getInt("paciente");
			}
			else
			{
				return -1;
			}	
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error verificando codigo de paciente en la tabla ant_oftal_familiares : SqlBaseAntecedentesOftalmologicosDao (verificarAntOftalPersonales) "+e.toString());
			return -1;
		}

      }

      
      /**
       * Funcion para insertar las observaciones de los antecedentes oftalmologicos personales medicos del paciente
       * @param con
       * @param paciente
       * @param observacion
       * @return
       */
      
      public static int insertarEnferOftalPersonales(Connection con, int paciente, String observacion, int codAuxPaciente)
      {
		PreparedStatementDecorator ps;
		int resp=0;
		String insertar = "";
		try
			{
			    //-verficar si ya existe registro del paciente
			    if (codAuxPaciente != -1)
			    {	
			    	insertar = " UPDATE ant_oftal_personales SET observaciones = ?, fecha='"+Utilidades.capturarFechaBD()+"', hora='"+UtilidadFecha.getHoraActual()+"' "+
			    			" WHERE paciente = ? ";


			    	ps =  new PreparedStatementDecorator(con.prepareStatement(insertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setString(1, observacion);
					ps.setInt(2, paciente);					
					ps.executeUpdate();
					resp = paciente;
			    }
			    else
			    {
					insertar = " INSERT INTO ant_oftal_personales (paciente, observaciones, fecha, hora) VALUES (?, ?, '"+Utilidades.capturarFechaBD()+"', '"+UtilidadFecha.getHoraActual()+"') ";
					ps =  new PreparedStatementDecorator(con.prepareStatement(insertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, paciente);					
					ps.setString(2, observacion);
					ps.executeUpdate();
					resp = paciente;
			    }
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de datos (insertarEnferOftalPersonales) : SqlBaseAntecedentesOftalmologicosDao "+e.toString() );
					resp = 0;
			}
			return resp;
      }

      /**
       * Funcion para insertar las observaciones de los antecedentes oftalmologicos personales medicos del paciente
       * @param con
       * @param paciente
       * @param observacion
       * @return
       */
      
      public static int insertarEnferOftalFamiliares (Connection con, int paciente, String observacion, int codAuxPaciente)
      {
		PreparedStatementDecorator ps;
		int resp=0;
		String insertar = "";
		try
			{
			    //-verficar si ya existe registro del paciente
			    if (codAuxPaciente != -1)
			    {	
			    	insertar = " UPDATE ant_oftal_familiares SET observaciones = ?, fecha='"+Utilidades.capturarFechaBD()+"', hora='"+UtilidadFecha.getHoraActual()+"' WHERE paciente = ? "; 
			    	
			    	ps =  new PreparedStatementDecorator(con.prepareStatement(insertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setString(1, observacion);
					ps.setInt(2, paciente);					
					ps.executeUpdate();
					resp = paciente;
			    }
			    else
			    {
					insertar = " INSERT INTO ant_oftal_familiares (paciente, observaciones, fecha, hora) VALUES (?, ?, '"+Utilidades.capturarFechaBD()+"', '"+UtilidadFecha.getHoraActual()+"') "; 
					ps =  new PreparedStatementDecorator(con.prepareStatement(insertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, paciente);					
					ps.setString(2, observacion);
					ps.executeUpdate();
					resp = paciente;
			    }
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de datos (insertarEnferOftalFamiliares) : SqlBaseAntecedentesOftalmologicosDao "+e.toString() );
					resp = 0;
			}
			return resp;
      }
      
      /** Metodo para insertar otro enfermedad oftal digitada por el paciente
        *  @param con
        *  @param secuencia
        *  @param string
        *  @return
  	    */
       public static  int insertarOtraEnferOftal(Connection con, String otroEnferOftal, String secuencia, String tabla )
       {
		PreparedStatementDecorator ps;
		int resp=0, codigo=0;
		String 	insertar = " INSERT INTO otro_enfer_oftal (codigo, nombre) VALUES (?, ?) ";
		
		try
			{
			    codigo = obtenerCodigoEnferOftal(con, secuencia, tabla);
			    //-verficar si el retorno del codigo tuvo exito
			    if (codigo != 0)
			    {	
					ps =  new PreparedStatementDecorator(con.prepareStatement(insertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, codigo);
					ps.setString(2, otroEnferOftal);
					ps.executeUpdate();
					resp = codigo;
			    }
			    else
			    {
					logger.warn(" Error al obtener el codigo de la nueva enfermedad oftalmologica");
					resp = 0;
			    }
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de datos (insertarOtraEnferOftal) : SqlBaseAntecedentesOftalmologicosDao "+e.toString() );
					resp = 0;
			}
			return resp;
       }
       
       
       public static  int insertarOtraEnferOftalFam(Connection con, String secuencia, String secuencia2,  String otroEnferOftal, int paciente, String tabla)
       {
		PreparedStatementDecorator ps;
		int resp=0, codigo=0, codigoSec=0;
		String 	insertar = " INSERT INTO ant_oftal_fam_det_otro (codigo, paciente, otro_enfer_oftal) VALUES (?, ?, ?) ";
		
		try
			{
			    //-Insertar en la tabla de otras enfermedades 
			    codigo = insertarOtraEnferOftal(con, otroEnferOftal, secuencia, tabla);
			    
			    if (codigo != 0)
			    {	
			    	 //-Obtener el codigo para retornarlo y poder insertar el detalle del otro 
				    codigoSec = obtenerCodigoEnferOftal(con, secuencia2, tabla);
				    
					ps =  new PreparedStatementDecorator(con.prepareStatement(insertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, codigoSec);
					ps.setInt(2, paciente);
					ps.setInt(3, codigo);  //-La nueva enfermedad

					ps.executeUpdate();
					resp = codigoSec;
			    }
			    else
			    {
					logger.warn(" Error al insertar en las nueva enfermedad ");
					resp = 0;
			    }
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de datos (insertarOtraEnferOftalFam) : SqlBaseAntecedentesOftalmologicosDao "+e.toString() );
					resp = 0;
			}
			return resp;
       }

       

   /**
   	 * Método para obtener el codigo de la siguiente enfermedad oftalmologica digitada por el usuario 
   	 * @param con : conexion
   	 * @param secuencia : string que tiene la secuencia
   	 * @return codigo del registro a insertar
   	 */
   	public static int obtenerCodigoEnferOftal(Connection con, String secuencia, String tabla)
   	{
   		String consultaSecuencia="SELECT "+secuencia+" "+tabla;
   		try
   		{
   			
   			PreparedStatementDecorator obtenerCodigoStatement= new PreparedStatementDecorator(con.prepareStatement(consultaSecuencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
   			ResultSetDecorator resultado=new ResultSetDecorator(obtenerCodigoStatement.executeQuery());
   			if(resultado.next())
   			{
   				return resultado.getInt("codigo");
   			}
   			return 0;
   		}
   		catch(SQLException e)
   		{
   			logger.warn(e+" Error en la consulta del codigo de otro enfermedad oftalmologica : SqlBaseAntecedentesOftalmologicosDao "+e.toString());   	
   			return 0;
   		}
   	}

    /**
      * Metodo para insertar la información de detalle de la "otra" enfermedad oftalmologica
 	  * @param con
 	  * @param paciente
 	  * @param codigo
 	  * @param Desde Cuando : tiempo estimado desde que se padece la enfermedad
 	  * @param Tratamiento : tratamiento que se esta llevando a cabo para ese tipo de enfermedad oftalmologica
 	  * @return
 	  */
   	
   	public static int insertarOtraEnferOftalDet(Connection con, int codigo, int paciente, String desdeCuando, String tratamiento, int actualizar) 
   	{
   		PreparedStatementDecorator ps;
		int resp=0; 
		String insertar = "";
		
		try	{
				 if (actualizar == 0)
				 {
					insertar = " INSERT INTO ant_oftal_perso_med_otro (paciente, otro_enfer_oftal, desde_cuando, tratamiento) " +
							   "							  		   VALUES (?,?,?,?)";
				 	ps =  new PreparedStatementDecorator(con.prepareStatement(insertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, paciente);					
					ps.setInt(2, codigo);					
					ps.setString(3, desdeCuando);
					ps.setString(4, tratamiento);
					resp = ps.executeUpdate();
				 }
				 else
				 {
					insertar = " UPDATE ant_oftal_perso_med_otro SET desde_cuando = ?, tratamiento = ?  WHERE paciente = ? AND otro_enfer_oftal = ? ";
					
					
				 	ps =  new PreparedStatementDecorator(con.prepareStatement(insertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				 	ps.setString(1, desdeCuando);
				 	ps.setString(2, tratamiento);
				 	ps.setInt(3, paciente);					
					ps.setInt(4, codigo);					
					resp = ps.executeUpdate();
				 }
				 
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de datos (insertarEnferOftalPersonales) : SqlBaseAntecedentesOftalmologicosDao "+e.toString() );
					resp = 0;
			}
			return resp;   	
   	}

   	/**
   	 * Metodo para consultar las enfermedades registradas de un paciente especifico   
   	 * @param con
   	 * @param paciente : codigo del paciente
   	 * @return
   	 */
   	
   	//-------------------------Las enfermedades registradas
	public static Collection consutarEnferOftalPersoPaciente(Connection con, int paciente) 
   	{
   		PreparedStatementDecorator ps;

		String query = " SELECT eoi.codigo as codigo, aopm.desde_cuando as desde_cuando, aopm.tratamiento as tratamiento, 0 as indicador, aop.fecha as fecha, aop.hora as hora " + 
   	 					  "		   FROM ant_oftal_perso_medicos aopm " +
						  " 			INNER JOIN ant_oftal_personales aop ON (aop.paciente = aopm.paciente) " +
						  "				INNER JOIN enfer_oftal_inst eoi ON (eoi.codigo = aopm.enfer_oftal_inst) " + 
						  "				INNER JOIN tipo_enfer_oftal teo ON (teo.codigo = eoi.enfer_oftal) " + 
			        	  "					  WHERE aopm.paciente = ? " +
						  "	UNION ALL " +
						  "	SELECT oeo.codigo as codigo, aopmo.desde_cuando as desde_cuando, aopmo.tratamiento as tratamiento, 1 as indicador, aop.fecha as fecha, aop.hora as hora " + 
						  "		   FROM ant_oftal_perso_med_otro aopmo " +
						  " 			INNER JOIN ant_oftal_personales aop ON (aop.paciente = aopmo.paciente) " +
						  "				INNER JOIN otro_enfer_oftal oeo ON (oeo.codigo = aopmo.otro_enfer_oftal) " + 
					      "					  WHERE aopmo.paciente = ? ";
		try	{
				ps =  new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, paciente);					
				ps.setInt(2, paciente);		
				Log4JManager.info(query);
				return UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery())); 
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error Consultando las enfermedades del paciente un espècifico (consutarEnferOftalPersoPaciente) : SqlBaseAntecedentesOftalmologicosDao "+e.toString() );
					return null;
			}
   	}

   	/**
   	 * Metodo para consultar los antecedentes quirurgicos del paciente   
   	 * @param con
   	 * @param paciente : codigo del paciente
   	 * @return
	*/
	public static Collection consutarEnferOftalPersoQuirurPaciente(Connection con, int paciente) 
   	{
   		PreparedStatementDecorator ps;
		String insertar = " SELECT codigo as codigo, nombre_procedimiento as nombre_procedimiento, fecha as fecha, causa as causa, fecha_ant as fecha_ant, hora_ant as hora_ant  " +
						  "        FROM ant_oftal_perso_quirur aopq" +
						  "             WHERE aopq.paciente = ? ";
		try	{
				ps =  new PreparedStatementDecorator(con.prepareStatement(insertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, paciente);								
				return UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery())); 
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error Consultando los procedimientos quirurgicos del paciente (consutarEnferOftalPersoPaciente) : SqlBaseAntecedentesOftalmologicosDao "+e.toString() );
					return null;
			}
   	}

	/**
  	 * Metodo para consultar las enfermedades familiares registradas anteriormente 
  	 * @param con
  	 * @param paciente
  	 * @return
  	 */
  	public static Collection consutarEnferOftalFamPadece(Connection con, int paciente) 
   	{
   		PreparedStatementDecorator ps;
		String query = "  SELECT aofd.paciente as paciente, aofd.codigo as codigo,  aofd.enfer_oftal_inst as enfer_oftal_inst, " +
						  "			aofdp.parentesco as parentesco, 0 as indicador, aofd.fecha as fecha, aofd.hora as hora " + 
		 				  "		    FROM ant_oftal_fam_det_padece aofdp " +
            			  " 			INNER JOIN  ant_oftal_fam_detalle aofd ON ( aofd.codigo  = aofdp.codigo_oftal_fam_det) " + 
						  "				INNER JOIN  tipos_parentesco tp ON ( tp.codigo = aofdp.parentesco ) " +
						  "			          WHERE aofd.paciente = ? " +
						  "	 UNION ALL " +         
						  "  SELECT aofdo.paciente as paciente, aofdo.codigo as codigo, aofdo.otro_enfer_oftal as enfer_oftal_inst, " +    
						  "		   			aofdpo.parentesco as parentesco, 1 as indicador, aofdo.fecha as fecha, aofdo.hora as hora " + 
						  "			   		    FROM ant_oftal_fam_det_pad_otro aofdpo " +    
						  " 	    			INNER JOIN  ant_oftal_fam_det_otro aofdo ON ( aofdo.codigo  = aofdpo.codigo_oftal_fam_det_otro) " +     
						  "		   				INNER JOIN  tipos_parentesco tp ON ( tp.codigo = aofdpo.parentesco ) " +
						  "		   			          WHERE aofdo.paciente = ? ";
		
		try	{
			   
			   
				ps =  new PreparedStatementDecorator(con.prepareStatement(query,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, paciente);								
				ps.setInt(2, paciente);								
				return UtilidadBD.resultSet2Collection(new ResultSetDecorator(ps.executeQuery())); 
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error Consultando las enfermedades oftalmologicas familiares del paciente (consutarEnferOftalFamPadece) : SqlBaseAntecedentesOftalmologicosDao "+e.toString() );
					return null;
			}
   	}
	
	/**
	 * Metodo para insertar los antecedentes Oftalmologicos personales quirurgicos
	 * @param con
	 * @param tipoProc : codigo del procedimiento oftalmologico
	 * @param proce : nombre del procedimiento oftalmologico
	 * @param fecha : fecha en que se realizo el procedimiento oftalmologico
	 * @param causa : causa por la cual se realizo el procedimiento oftalmologico
	 * @param act : tipo de accion : 0 -> insertar 1-> modificar
	 * @return
	 */
	
	public static int insertarEnferOftalPersoQuirur(Connection con, String secuencia, int tipoProc, int paciente,  String proce, String fecha, String causa, int act, String tabla) 
	{
   		PreparedStatementDecorator ps;
		int resp=0, codigoIndice=0; 
		String insertar = "";
		
		try	{
			   	
			   
				 if (act == 0)
				 {
					insertar = " INSERT INTO ant_oftal_perso_quirur (codigo, paciente, nombre_procedimiento, fecha, causa, fecha_ant, hora_ant) " +
							   "							  		 VALUES (?,?,?,?,?, '"+Utilidades.capturarFechaBD()+"', '"+UtilidadFecha.getHoraActual()+"')";
					
					codigoIndice = obtenerCodigoEnferOftal(con, secuencia, tabla); 
				 	ps =  new PreparedStatementDecorator(con.prepareStatement(insertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, codigoIndice);					
					ps.setInt(2, paciente);					
					ps.setString(3, proce);
					
					if (fecha.trim().equals(""))
					   ps.setString(4,null);
					else
					{
					   ps.setString(4, fecha);
					}
					
					ps.setString(5, causa);
					resp = ps.executeUpdate();
				 }
				 else
				 {
				 	
					insertar = " UPDATE ant_oftal_perso_quirur SET fecha = ?, causa = ?," +
							"fecha_ant='"+Utilidades.capturarFechaBD()+"', hora_ant='"+UtilidadFecha.getHoraActual()+"' " +
							" WHERE paciente = ? AND codigo = ? ";
				 	ps =  new PreparedStatementDecorator(con.prepareStatement(insertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					
					if (fecha.trim().equals(""))
					   ps.setString(1, null);
					else
						{
						   ps.setString(1, UtilidadFecha.conversionFormatoFechaABD(fecha));
						}

					ps.setString(2, causa);
					ps.setInt(3, paciente);
					ps.setInt(4, tipoProc);
					
					resp = ps.executeUpdate();
				 }
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de datos (insertarEnferOftalPersoQuirur) : SqlBaseAntecedentesOftalmologicosDao "+e.toString() );
					resp = 0;
			}
			return resp;   	
	}

	/**
	 * Metodo para insertar el detalle de los antecedentes Oftalmologicos familiares
	 * @param con
	 * @param paciente
	 * @param codigoEnfermedad
	 * @param actualiza : para saber si se actualiza o se inserta 
	 * @return
	 */
	public static int insertarEnferOftalFamDetalle(Connection con, String secuencia,  int paciente, int codigoEnfermedad, int actualiza, String tabla)
	{
   		PreparedStatementDecorator ps;
		int resp=0, codigoIndice=0; 
		String insertar = "";
		
		try	{
					insertar = " INSERT INTO ant_oftal_fam_detalle (codigo, paciente, enfer_oftal_inst, fecha, hora ) " +
							   "							  	   VALUES (?,?,?,'"+Utilidades.capturarFechaBD()+"','"+UtilidadFecha.getHoraActual()+"')";
					
					codigoIndice = obtenerCodigoEnferOftal(con, secuencia, tabla); 
				 	ps =  new PreparedStatementDecorator(con.prepareStatement(insertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setInt(1, codigoIndice);					
					ps.setInt(2, paciente);					
					ps.setInt(3, codigoEnfermedad);		

					ps.executeUpdate();
					resp = codigoIndice;
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de datos (insertarEnferOftalFamDetalle) : SqlBaseAntecedentesOftalmologicosDao "+e.toString() );
					resp = 0;
			}
			return resp;   	
	}  	
	
	/**
	 * Metodo para insertar los parentescos familiares que padecen alguna enfermedad
	 * @param con
	 * @param codigoDetalle : codigo de la tabla que ereda el codigo
	 * @param tipoParentesco : parentesco del paciente
	 * @param actualiza : bandera para indicar si se esta insertando o solo modificando
	 * @return
	 */
	public static int insertarEnferOftalFamDetallePadece(Connection con, int codigoDetalle, int tipoParentesco, int actualiza)
	{
   		PreparedStatementDecorator ps;
		int resp=0; 
		String insertar = "";
		
		try	{
			
				insertar = " INSERT INTO ant_oftal_fam_det_padece (codigo_oftal_fam_det, parentesco) VALUES (?,?)";
			
			 	ps =  new PreparedStatementDecorator(con.prepareStatement(insertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigoDetalle);					
				ps.setInt(2, tipoParentesco);					

				resp = ps.executeUpdate();
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de datos (insertarEnferOftalFamDetallePadece) : SqlBaseAntecedentesOftalmologicosDao "+e.toString() );
					resp = 0;
			}
			return resp;   	

	}

	/**
  	 * Metodo para insertar el detalle de las nuevas enfermedades oftalmologicas  familiares 
  	 * @param con
  	 * @param codigo_oftal_fam_det_otro
  	 * @param parentesco
  	 * @return
  	 */
	public static int insertarOtraEnferOftalFamDet(Connection con, int codigo_oftal_fam_det_otro, int parentesco)
	{
   		PreparedStatementDecorator ps;
		int resp=0; 
		String insertar = "";
		
		try	{
				insertar = " INSERT INTO ant_oftal_fam_det_pad_otro (codigo_oftal_fam_det_otro, parentesco) VALUES (?,?)";
 
			 	ps =  new PreparedStatementDecorator(con.prepareStatement(insertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setInt(1, codigo_oftal_fam_det_otro);					
				ps.setInt(2, parentesco);					

				resp = ps.executeUpdate();
			}
			catch(SQLException e)
			{
					logger.warn(e+" Error en la inserción de datos (insertarOtraEnferOftalFamDet) : SqlBaseAntecedentesOftalmologicosDao "+e.toString() );
					resp = 0;
			}
			return resp;   	

	}

	/**
     * Metodo para cargar las observaciones generales personales de .los antecedentes Oftalmologicos 
     * @param con
     * @param paciente
     * @return
     */
    public static String cargarObservaciones(Connection con, int paciente)
   	{
   		String consultaSecuencia="SELECT observaciones as observaciones, fecha as fecha, hora as hora FROM ant_oftal_personales WHERE paciente = ?";
   		try
   		{
   			
   			PreparedStatementDecorator obtenerCodigoStatement= new PreparedStatementDecorator(con.prepareStatement(consultaSecuencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
   			obtenerCodigoStatement.setInt(1, paciente);
   			
   			ResultSetDecorator resultado=new ResultSetDecorator(obtenerCodigoStatement.executeQuery());
   			if(resultado.next())
   			{
   				return resultado.getString("observaciones");
   			}
   			return "";
   		}
   		catch(SQLException e)
   		{
   			logger.warn(e+" Error en la consulta de las observaciones Generales : SqlBaseAntecedentesOftalmologicosDao "+e.toString());   	
   			return null;
   		}
   	}

	/**
     * Metodo para cargar las observaciones generales personales de .los antecedentes Oftalmologicos 
     * @param con
     * @param paciente
     * @return
     */
    public static String cargarObservacionesFamiliares(Connection con, int paciente)
   	{
   		String consultaSecuencia="SELECT observaciones as observaciones FROM ant_oftal_familiares WHERE paciente = ?";
   		try
   		{
   			
   			PreparedStatementDecorator obtenerCodigoStatement= new PreparedStatementDecorator(con.prepareStatement(consultaSecuencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
   			obtenerCodigoStatement.setInt(1, paciente);
   			
   			ResultSetDecorator resultado=new ResultSetDecorator(obtenerCodigoStatement.executeQuery());
   			if(resultado.next())
   			{
   				return resultado.getString("observaciones");
   			}
   			return "";
   		}
   		catch(SQLException e)
   		{
   			logger.warn(e+" Error en la consulta de las observaciones Generales familiares : SqlBaseAntecedentesOftalmologicosDao "+e.toString());   	
   			return null;
   		}
   	}
    
    
    
    /**
     * Metodo para cargar hora de las observaciones generales personales de .los antecedentes Oftalmologicos 
     * @param con
     * @param paciente
     * @return
     */
    public static String cargarHoraObservacionesFamiliares(Connection con, int paciente)
   	{
   		String consultaSecuencia="SELECT hora as hora FROM ant_oftal_familiares WHERE paciente = ?";
   		try
   		{
   			
   			PreparedStatementDecorator obtenerCodigoStatement= new PreparedStatementDecorator(con.prepareStatement(consultaSecuencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
   			obtenerCodigoStatement.setInt(1, paciente);
   			
   			ResultSetDecorator resultado=new ResultSetDecorator(obtenerCodigoStatement.executeQuery());
   			if(resultado.next())
   			{
   				return resultado.getString("hora");
   			}
   			return "";
   		}
   		catch(SQLException e)
   		{
   			logger.warn(e+" Error en la consulta de la hora Generales familiares : SqlBaseAntecedentesOftalmologicosDao "+e.toString());   	
   			return null;
   		}
   	}
    
    
    /**
     * Metodo para cargar fecha de las observaciones generales personales de .los antecedentes Oftalmologicos 
     * @param con
     * @param paciente
     * @return
     */
    public static String cargarFechaObservacionesFamiliares(Connection con, int paciente)
   	{
   		String consultaSecuencia="SELECT fecha as fecha FROM ant_oftal_familiares WHERE paciente = ?";
   		try
   		{
   			
   			PreparedStatementDecorator obtenerCodigoStatement= new PreparedStatementDecorator(con.prepareStatement(consultaSecuencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
   			obtenerCodigoStatement.setInt(1, paciente);
   			
   			ResultSetDecorator resultado=new ResultSetDecorator(obtenerCodigoStatement.executeQuery());
   			if(resultado.next())
   			{
   				return resultado.getString("fecha");
   			}
   			return "";
   		}
   		catch(SQLException e)
   		{
   			logger.warn(e+" Error en la consulta de la horaGenerales familiares : SqlBaseAntecedentesOftalmologicosDao "+e.toString());   	
   			return null;
   		}
   	}
    
    
    
    /**
     * Metodo para cargar hora de las observaciones generales personales de .los antecedentes Oftalmologicos 
     * @param con
     * @param paciente
     * @return
     */
    public static String cargarHoraObservaciones(Connection con, int paciente)
   	{
   		String consultaSecuencia="SELECT observaciones as observaciones, fecha as fecha, hora as hora FROM ant_oftal_personales WHERE paciente = ?";
   		try
   		{
   			
   			PreparedStatementDecorator obtenerCodigoStatement= new PreparedStatementDecorator(con.prepareStatement(consultaSecuencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
   			obtenerCodigoStatement.setInt(1, paciente);
   			
   			ResultSetDecorator resultado=new ResultSetDecorator(obtenerCodigoStatement.executeQuery());
   			if(resultado.next())
   			{
   				return resultado.getString("hora");
   			}
   			return "";
   		}
   		catch(SQLException e)
   		{
   			logger.warn(e+" Error en la consulta de las observaciones Generales : SqlBaseAntecedentesOftalmologicosDao "+e.toString());   	
   			return null;
   		}
   	}
    
    
    /**
     * Metodo para cargar fecha de las observaciones generales personales de .los antecedentes Oftalmologicos 
     * @param con
     * @param paciente
     * @return
     */
    public static String cargarFechaObservaciones(Connection con, int paciente)
   	{
   		String consultaSecuencia="SELECT observaciones as observaciones, fecha as fecha, hora as hora FROM ant_oftal_personales WHERE paciente = ?";
   		try
   		{
   			
   			PreparedStatementDecorator obtenerCodigoStatement= new PreparedStatementDecorator(con.prepareStatement(consultaSecuencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
   			obtenerCodigoStatement.setInt(1, paciente);
   			
   			ResultSetDecorator resultado=new ResultSetDecorator(obtenerCodigoStatement.executeQuery());
   			if(resultado.next())
   			{
   				return resultado.getString("fecha");
   			}
   			return "";
   		}
   		catch(SQLException e)
   		{
   			logger.warn(e+" Error en la consulta de las observaciones Generales : SqlBaseAntecedentesOftalmologicosDao "+e.toString());   	
   			return null;
   		}
   	}
   	
} //-  Fin de la Clase
