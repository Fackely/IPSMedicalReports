/*
 * Creado el 23-sep-2005
 * por Julian Montoya
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.Collection;

/**
 * @author Julian Montoya
 * 
 * Princeton S.A. (ParqueSoft Manizales)
 */
@SuppressWarnings("rawtypes")
public interface AntecedentesOftalmologicosDao {
	
	/**
 	 * Funcion para retornar los tipos de enfermedes oftalmologicas personales 
 	 * @param con
 	 * @param codigoPaciente
 	 * @param codigoInstitucion
 	 * @return Coleccion con el listado de tipos de enfermedades personales parametrizadas
 	 */
	public Collection consultarTiposEnferOftalPersonales(Connection con, int codigoInstitucion, int paciente); 

     
     /**
	   	 * Funcion para retornar los tipos de enfermedes oftalmologicas familiares 
	   	 * @param con
	   	 * @param codigoPaciente
	   	 * @param codigoInstitucion
	   	 * @return Coleccion con el listado de tipos de enfermedades personales parametrizadas
	   	 */
	       
     public Collection consultarTiposEnferOftalfamiliares (Connection con, int codigoInstitucion, int paciente); 
     
     /**
      * Metodo para consultar los tipos de parentescos registrados  
      * @param con
      * @return
      */

     public Collection consultarTiposParentesco (Connection con); 
     
		
     /**
      * Funcion para insertar los tipos de enfermedades oftalmológicas para un determinado paciente  
      * @param con
      * @param paciente
      * @param tipoEnferOftal
      * @param desdeCuando (estimación desde cuando se tiene la enfermedad)
      * @param tratamiento (tratamiento que ha seguido)
     * @param actualizar
      * @return 
      */
     
     public int insertarEnferOftalPersoMedicos(Connection con, int paciente, int tipoEnferOftal, String desdeCuando, String tratamiento, int actualizar);
     
     /**
      * Funcion para insertar las observaciones de los antecedentes oftalmologicos personales medicos del paciente
      * @param con
      * @param paciente
      * @param observacion
      * @return
      */
     
     public int insertarEnferOftalPersonales(Connection con, int paciente, String observacion, int codAuxPaciente);

     /**
      * Funcion para insertar las observaciones de los antecedentes oftalmologicos personales medicos del paciente
      * @param con
      * @param paciente
      * @param observacion
      * @return
      */
     
     public int insertarEnferOftalFamiliares(Connection con, int paciente, String observacion, int codAuxPaciente);

     /**
      * Funcion para verificar si existe datos del paciente en la tabla ant_oftal_personales 
      * (indispensable para insertar datos en la tabla ant_oftal_perso_medicos) 
      * @param con
      * @param paciente : es el codigo del paciente
      * @return
      */
     
     public int verificarAntOftalPersonales(Connection con, int paciente);

     
     /**
      * Metodo para cargar hora de las observaciones generales personales de .los antecedentes Oftalmologicos 
      * @param con
      * @param paciente
      * @return
      */
     public String cargarObservaciones(Connection con, int paciente);
     
     /**
      * Metodo para cargar fecha de las observaciones generales personales de .los antecedentes Oftalmologicos 
      * @param con
      * @param paciente
      * @return
      */
     public String cargarHoraObservaciones(Connection con, int paciente);
     
     /**
      * Metodo para cargar las observaciones generales personales de .los antecedentes Oftalmologicos 
      * @param con
      * @param paciente
      * @return
      */
     public String cargarFechaObservaciones(Connection con, int paciente);

     /**
      * Metodo para cargar las observaciones generales familiares de .los antecedentes Oftalmologicos 
      * @param con
      * @param paciente
      * @return
      */
     public String cargarObservacionesFamiliares (Connection con, int paciente);
     
     /**
      * Metodo para cargar hora de las observaciones generales familiares de .los antecedentes Oftalmologicos 
      * @param con
      * @param paciente
      * @return
      */
     public String cargarHoraObservacionesFamiliares (Connection con, int paciente);
     
     /**
      * Metodo para cargar fecha de las observaciones generales familiares de .los antecedentes Oftalmologicos 
      * @param con
      * @param paciente
      * @return
      */
     public String cargarFechaObservacionesFamiliares (Connection con, int paciente);

     
     /**
      * Funcion para verificar si existe datos del paciente en la tabla ant_oftal_familiares 
      * (indispensable para insertar datos en la tabla ant_oftal_fam_detalle) 
      * @param con
      * @param paciente : es el codigo del paciente
      * @return
      */
     
     public int verificarAntOftalFamiliares(Connection con, int paciente);


	/**  Metodo para insertar otro enfermedad oftal digitada por el paciente
	 * @param con
	 * @param Nombre : nombre de la emfermedad
	 * @param Paciente 
	 * @return
	 */
	 public int insertarOtraEnferOftal(Connection con, String nombre);

	 /**
	  *   Metodo para insertar otro enfermedad oftal familiar digitada por el paciente 
  	  * @param con
	  * @param Nombre : nombre de la emfermedad
	  * @return
	  */
	 public int insertarOtraEnferOftalFam(Connection con, String nombre, int paciente);



	 /**
  	  * Metodo para insertar la información de detalle de la "otra" enfermedad oftalmologica
	  * @param con
	  * @param paciente
	  * @param codigo
	  * @param Desde Cuando : tiempo estimado desde que se padece la enfermedad
	  * @param Tratamiento : tratamiento que se esta llevando a cabo para ese tipo de enfermedad oftalmologica
	  * @return
	  */
	  public int insertarOtraEnferOftalDet(Connection con, int codigo, int paciente, String desdeCuando, String tratamiento, int actualizar);
	  
	  /**
	   * Metodo para consultar las enfermedades registradas de un paciente especifico   
	   * @param con
	   * @param paciente : codigo del paciente
	   * @return
	   */
	  public  Collection consutarEnferOftalPersoPaciente(Connection con, int paciente);


	/**
	 * Metodo para insertar los antecedentes Oftalmologicos personales quirurgicos
	 * @param con
	 * @param tipoProc : codigo del procedimiento oftalmologico
	 * @param paciente : codigo del paciente 
	 * @param proce : nombre del procedimiento oftalmologico
	 * @param fecha : fecha en que se realizo el procedimiento oftalmologico
	 * @param causa : causa por la cual se realizo el procedimiento oftalmologico
	 * @param act : tipo de accion : 0 -> insertar 1-> modificar
	 * @return
	 */
	public int insertarEnferOftalPersoQuirur(Connection con, int tipoProc, int paciente, String proce, String fecha, String causa, int act); 

	
	/**
	 * Metodo para insertar el detalle de los antecedentes Oftalmologicos familiares
	 * @param con
	 * @param paciente
	 * @param codigoEnfermedad
	 * @param actualiza : para saber si se actualiza o se inserta 
	 * @return
	 */
	public int insertarEnferOftalFamDetalle(Connection con, int paciente, int codigoEnfermedad, int actualiza);
		
	/**
	 * Metodo para insertar los parentescos familiares que padecen alguna enfermedad
	 * @param con
	 * @param codigoDetalle : codigo de la tabla que ereda el codigo
	 * @param tipoParentesco : parentesco del paciente
	 * @param actualiza : bandera para indicar si se esta insertando o solo modificando
	 * @return
	 */
	public int insertarEnferOftalFamDetallePadece(Connection con, int codigoDetalle, int tipoParentesco, int actualiza);
		
	
 	/**
   	 * Metodo para consultar los antecedentes quirurgicos del paciente   
   	 * @param con
   	 * @param paciente : codigo del paciente
   	 * @return
   	 */
	
	public Collection consutarEnferOftalPersoQuirurPaciente(Connection con, int paciente);

	/**
  	 * Metodo para consultar las enfermedades familiares registradas anteriormente 
  	 * @param con
  	 * @param paciente
  	 * @return
  	 */
  	public Collection consutarEnferOftalFamPadece(Connection con, int paciente);


  	/**
  	 * Metodo para insertar el detalle de las nuevas enfermedades oftalmologicas  familiares 
  	 * @param con
  	 * @param codigo_oftal_fam_det_otro
  	 * @param parentesco
  	 * @return
  	 */
	public int insertarOtraEnferOftalFamDet(Connection con, int codigo_oftal_fam_det_otro, int parentesco); 

}
