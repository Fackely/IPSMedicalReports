package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatos;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;

import com.mercury.dao.sqlbase.odontologia.SqlBaseAntecedentesOdontologiaDao;
import com.mercury.mundo.odontologia.AntecedenteOdontologia;
import com.mercury.mundo.odontologia.TratamientoPrevio;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.historiaClinica.componentes.DtoAntecedentesOdontologicosAnt;

public class SqlBaseVerResumenOdontologicoDao {

	private static Logger logger=Logger.getLogger(SqlBaseVerResumenOdontologicoDao.class);

	/**
	 * Cadena Sql para consultar si un Paciente tiene Antecedentes
	 */
	private static final String existeAntecedentePacienteStr= 
	        "SELECT count(*) as antecedentes "+
	        "FROM historiaclinica.antecedentes_pacientes "+
	        "WHERE codigo_paciente=?";
	
	/**
	 *  Cadena Sql para consultar las observaciones de un Antecedente de Odontologia
	 */
	private static String cargarAntecedenteOdontoPrevioStr= "SELECT " +
			"cod_paciente AS codpaciente, " +
			"observaciones AS observaciones " +
			"FROM historiaclinica.antecedente_odontologia " +
			"WHERE cod_paciente = ? ";
	
	/**
	 * 
	 */
	private final static String consultarHabitosOdontologiaOtrosStr = "SELECT "+
        "cod_paciente as codpaciente, " +
        "codigo as codigo, " +
        "nombre as nombre, " +
        "observaciones as observaciones " +
        "FROM historiaclinica.habito_odo_otr where cod_paciente = ? ";
	
	
	/**
	 * 
	 */
	 private final static String existeHabitoOdontologiaOtroStr = "SELECT COUNT(*) as tothabitootro " +
	        "FROM historiaclinica.habito_odo_otr " +
	        "WHERE cod_paciente = ? and " +
	        "codigo = ?";
	
	 
	  private final static String existeHabitoOdontologiaStr = "SELECT COUNT(*) as tothabito " +
	        "FROM historiaclinica.habito_odo " +
	        "WHERE cod_paciente = ? and " +
	        "cod_tipo_habito_odo_inst = ?";	 
	/**
	 * 
	 */
	 private final static String consultarHabitosOdontologiaStr= "SELECT "+
	        "cod_paciente as codpaciente, " +
	        "cod_tipo_habito_odo_inst as codtipohabitoodo, " +
	        "getNombreTipoHabitoOdoInst(cod_tipo_habito_odo_inst) as nomtipohabitoodo, " +
	        "observaciones as observaciones " +
	        "FROM historiaclinica.habito_odo where cod_paciente = ? ";
	
	 /**
	  * 
	  */
	 private final static String consultarTraumatismosOdontologiaStr = "SELECT "+
	        "cod_paciente as codpaciente, " +
	        "cod_tipo_traumatismo_odo_inst as codtipotraumatismoodo, " +
	        "getNombTipoTraumatismoOdoInst(cod_tipo_traumatismo_odo_inst) as nomtipotraumatismoodo, " +
	        "observaciones as observaciones " +
	        "FROM historiaclinica.traumatismo_odo WHERE  cod_paciente = ?";
	   
	  /**
	   * 
	   */
	  private final static String existeTraumatismoOdontologiaStr = "SELECT COUNT(*) as tottraumatismo " +
	        "FROM historiaclinica.traumatismo_odo " +
	        "WHERE cod_paciente = ? AND  " +
	        "cod_tipo_traumatismo_odo_inst = ?";
	 
	  /**
	   * 
	   */
	  private final static String consultarTraumatismosOdontologiaOtrosStr= "SELECT "+
	        "cod_paciente as codpaciente, " +
	        "codigo as codigo, " +
	        "nombre as nombre, " +
	        "observaciones as observaciones " +
	        "FROM historiaclinica.traumatismo_odo_otr WHERE cod_paciente=?";
	    
	  
	  /**
	   * 
	   */
	    private final static String existeTraumatismoOdontologiaOtroStr =
	        "SELECT COUNT(*) as tottraumatismootro " +
	        "FROM historiaclinica.traumatismo_odo_otr " +
	        "WHERE cod_paciente = ? and " +
	        "codigo = ?";
	  
	  
	  
	  /**
	   * 
	   */
	  private final static String consultarTratamientosOdontologiaPreviosStr = "SELECT "+
	        "top.cod_paciente AS codpaciente, " +
	        "top.codigo AS codigo, " +
	        "top.tipo_tratamiento As tipotratamiento, " +
	        "top.fecha_inicio AS fechainicio, " +
	        "top.fecha_finalizacion AS fechafinalizacion, " +
	        "top.descripcion AS descripcion, " +
	        "tod.observaciones AS fechainiciacion, " +
	        "administracion.getNombrePersona2(tod.cod_medico) AS nombremedico " +
	        "FROM historiaclinica.tratamiento_odo_previo top " +
	        "INNER JOIN historiaclinica.tratamiento_odo tod ON (tod.cod_paciente = top.cod_paciente)" +
	        "WHERE  top.cod_paciente = ? ";
	    
	  
	  /**
	   * 
	   */
	    private final static String existeTratamientoOdontologiaPrevioStr =
	        "SELECT COUNT(*) as tottratamientoprevio " +
	        "FROM historiaclinica.tratamiento_odo_previo " +
	        "WHERE cod_paciente = ? AND " +
	        "codigo = ?";
	 
	
	/**
	 * 
	 * @param codigoPaciente
	 * @return
	 */
	public static DtoAntecedentesOdontologicosAnt cagarAntecedentesOdontoPrevios(int codigoPaciente) {
		
		
		logger.info("entro BUSQEDA SQL Antecedentes Odontologicos");
		
		Connection con = null;
		con = UtilidadBD.abrirConexion();
		DtoAntecedentesOdontologicosAnt dtoAntecedentes = new DtoAntecedentesOdontologicosAnt();
		PreparedStatementDecorator consultarAnt= null;
		PreparedStatementDecorator consultarHabitos= null;
		ResultSetDecorator rsAnt = null;
		PreparedStatementDecorator consultarHabitosOtro= null;
		ResultSetDecorator rsHab = null;
		PreparedStatementDecorator consultarTraumatismos= null;
		ResultSetDecorator rsHabitosOtros = null;
		PreparedStatementDecorator consultarTraumatismosOtro= null;
		ResultSetDecorator rsTraumatismos = null;
		PreparedStatementDecorator consultarTratdOdontoPrev= null;
		ResultSetDecorator rsTraumatismosOtros = null;
		ResultSetDecorator rsTratamientosPrevios = null;
		try
        {
            consultarAnt= new PreparedStatementDecorator(con.prepareStatement(cargarAntecedenteOdontoPrevioStr));
            consultarAnt.setInt(1, codigoPaciente);        
            rsAnt = new ResultSetDecorator(consultarAnt.executeQuery());
		
          if(rsAnt.next())
           {
        	  dtoAntecedentes.setCodPaciente(codigoPaciente);
        	  dtoAntecedentes.setObservaciones(rsAnt.getString("observaciones"));
             try
        	    {
                  consultarHabitos= new PreparedStatementDecorator(con.prepareStatement(consultarHabitosOdontologiaStr));
                 consultarHabitos.setInt(1, codigoPaciente);
                  rsHab = new ResultSetDecorator(consultarHabitos.executeQuery());
                   
                 while(rsHab.next())
                   {
                       InfoDatos habito = new InfoDatos(
                               rsHab.getInt("codtipohabitoodo"),
                               rsHab.getString("nomtipohabitoodo"),
                               rsHab.getString("observaciones"));
                       dtoAntecedentes.getHabitos().add(habito);
                   }
        	      
        	      }
                    catch(SQLException e)
                     {
                     logger.info("Error en consultar los Habitos Odontologicos");	
                     logger.warn(e);
                     
                     throw e;
                     } 
        	  
               try
                 {
                  consultarHabitosOtro= new PreparedStatementDecorator(con.prepareStatement(consultarHabitosOdontologiaOtrosStr));
                  consultarHabitosOtro.setInt(1, codigoPaciente);
                  rsHabitosOtros = new ResultSetDecorator(consultarHabitosOtro.executeQuery());
                       
                   while(rsHabitosOtros.next())
                     {
                	   InfoDatos habitoOtro = new InfoDatos(
                               rsHabitosOtros.getInt("codigo"),
                               rsHabitosOtros.getString("nombre"),
                               rsHabitosOtros.getString("observaciones"));
                       dtoAntecedentes.getHabitos().add(habitoOtro);
                     }
            	      
            	  }
                 catch(SQLException e)
                 {
                 logger.info("Error en consultar los Otros Habitos Odontologicos");	
                 logger.warn(e);
                 
                 throw e;
                 } 
                    
             
                 try
                 {
                  consultarTraumatismos= new PreparedStatementDecorator(con.prepareStatement(consultarTraumatismosOdontologiaStr));
                  consultarTraumatismos.setInt(1, codigoPaciente);
                  rsTraumatismos = new ResultSetDecorator(consultarTraumatismos.executeQuery());
                       
                   while(rsTraumatismos.next())
                     {
                	   InfoDatos traumatismo = new InfoDatos(
                               rsTraumatismos.getInt("codtipotraumatismoodo"),
                               rsTraumatismos.getString("nomtipotraumatismoodo"),
                               rsTraumatismos.getString("observaciones"));
                       dtoAntecedentes.getTraumatismos().add(traumatismo);
                     }
            	      
            	  }
                 catch(SQLException e)
                 {
                 logger.info("Error en consultar Traumatismos");	
                 logger.warn(e);
                 
                 throw e;
                 } 
               
                 try
                 {
                  consultarTraumatismosOtro= new PreparedStatementDecorator(con.prepareStatement(consultarTraumatismosOdontologiaOtrosStr));
                  consultarTraumatismosOtro.setInt(1, codigoPaciente);
                  rsTraumatismosOtros = new ResultSetDecorator(consultarTraumatismosOtro.executeQuery());
                       
                   while(rsTraumatismosOtros.next())
                     {
                	   InfoDatos traumatismoOtro = new InfoDatos(
                               rsTraumatismosOtros.getInt("codigo"),
                               rsTraumatismosOtros.getString("nombre"),
                               rsTraumatismosOtros.getString("observaciones"));
                       dtoAntecedentes.getTraumatismosOtros().add(traumatismoOtro);
                     }
            	      
            	  }
                 catch(SQLException e)
                 {
                 logger.info("Error en consultar Otros Traumatismos");	
                 logger.warn(e);
                 
                 throw e;
                 }     
                 
             
                 
                 try
                 {
                  consultarTratdOdontoPrev= new PreparedStatementDecorator(con.prepareStatement(consultarTratamientosOdontologiaPreviosStr));
                  consultarTratdOdontoPrev.setInt(1, codigoPaciente);
                  rsTratamientosPrevios = new ResultSetDecorator(consultarTratdOdontoPrev.executeQuery());
                       
                   while(rsTratamientosPrevios.next())
                     {
                	   TratamientoPrevio tratamientoPrevio = new TratamientoPrevio(
                               rsTratamientosPrevios.getInt("codigo"),
                               rsTratamientosPrevios.getString("tipotratamiento"),
                               UtilidadFecha.conversionFormatoFechaAAp(rsTratamientosPrevios.getString("fechainicio")),
                               UtilidadFecha.conversionFormatoFechaAAp(rsTratamientosPrevios.getString("fechafinalizacion")),
                               rsTratamientosPrevios.getString("descripcion"));
                       dtoAntecedentes.getTratamientosPrevios().add(tratamientoPrevio);
                     }
            	      
            	  }
                 catch(SQLException e)
                 {
                 logger.info("Error en consultar Otros Traumatismos");	
                 logger.warn(e);
                 
                 throw e;
                 }      
                        
         }
		
        }
        catch(SQLException e)
        {
        	logger.info("Error Cargando Informacion de los Antecendentes Odontologicos");
            logger.warn(e);
     
        }finally{
			if (consultarAnt != null){
				try{
					consultarAnt.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseVerResumenOdontologicoDao " + sqlException.toString() );
				}
			}
			if (consultarHabitos != null){
				try{
					consultarHabitos.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseVerResumenOdontologicoDao " + sqlException.toString() );
				}
			}
			if (rsAnt != null){
				try{
					rsAnt.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseVerResumenOdontologicoDao " + sqlException.toString() );
				}
			}
			if (consultarHabitosOtro != null){
				try{
					consultarHabitosOtro.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseVerResumenOdontologicoDao " + sqlException.toString() );
				}
			}
			if (rsHab != null){
				try{
					rsHab.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseVerResumenOdontologicoDao " + sqlException.toString() );
				}
			}
			if (consultarTraumatismos != null){
				try{
					consultarTraumatismos.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseVerResumenOdontologicoDao " + sqlException.toString() );
				}
			}
			if (consultarTraumatismosOtro != null){
				try{
					consultarTraumatismosOtro.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseVerResumenOdontologicoDao " + sqlException.toString() );
				}
			}
			if (consultarTratdOdontoPrev != null){
				try{
					consultarTratdOdontoPrev.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseVerResumenOdontologicoDao " + sqlException.toString() );
				}
			}
			if (rsTraumatismosOtros != null){
				try{
					rsTraumatismosOtros.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseVerResumenOdontologicoDao " + sqlException.toString() );
				}
			}
			if (rsTratamientosPrevios != null){
				try{
					rsTratamientosPrevios.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseVerResumenOdontologicoDao " + sqlException.toString() );
				}
			}
			if (rsHabitosOtros != null){
				try{
					rsHabitosOtros.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseVerResumenOdontologicoDao " + sqlException.toString() );
				}
			}
			if (rsTraumatismos != null){
				try{
					rsTraumatismos.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseVerResumenOdontologicoDao " + sqlException.toString() );
				}
			}
			UtilidadBD.closeConnection(con);
		}
        
		return dtoAntecedentes;
	}
	
	
	
}
