/*
 * @(#)RegistroDiagnosticosDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 * 
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

import com.princetonsa.decorator.ResultSetDecorator;

/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>RegistroDiagnosticos</code>.
 * 
 * @author armando
 */
public interface RegistroDiagnosticosDao 
{
    /**
     * Metodo para insertar un registro en la tabla diagnosticos
     * @param con Conexion
     * @param codigo Codigo del diagnostico
     * @param tipoCIE CIE del diagnostico
     * @param descripcion, La descripcion del diagnostico.
     * @param activo Para manejar el estado del diagnostico si es activo o inactivo
     * @param sexo Para manejar el sexo del diagnostico si masculino, fenmenino o sin restriccion
     * @param edad_inicial Para manejar la edad inicial del diagnostico 
     * @param edad_final Para manejar la edad final del diagnostico 
     * @param es_principal Para manejar el indicativo del  diagnostico si es principal o no
     * @param es_muerte Para manejar el indicativo del  diagnostico si es de muerte o no
     * @return Integer -1 si la insercion fallo. 
     */
	public int insertarRegistroDiagnostico(Connection con, String codigo, int tipoCIE,String descripcion, boolean activo, int sexo , int edad_inicial , int edad_final, String es_principal, String es_muerte );

    /**
     * Metodo para modificar un diagnostico
     * @param con Conexion
     * @param descripcion Nuevo valor del campo descripcion
     * @param activo Nuevo valor del campo estado
     * @param sexo Para manejar el sexo del diagnostico si masculino, fenmenino o sin restriccion
     * @param edad_inicial Para manejar la edad inicial del diagnostico 
     * @param edad_final Para manejar la edad final del diagnostico 
     * @param es_principal Para manejar el indicativo del  diagnostico si es principal o no
     * @param es_muerte Para manejar el indicativo del  diagnostico si es de muerte o no
     * @return Integer -1 si la insercion fallo.
     */
	public  int modificarRegistroDiagnostico(Connection con,String descripcion,boolean activo,String codigo,int tipoCIE , int sexo , int edad_inicial , int edad_final, String es_principal, String es_muerte);
    
    /**
     * Metodo que eliminar Un diagnostico, solo lo elimina si el diagnostico no esta siendo utilizado en la BD
     * @param con Conexion
     * @param codigo Codigo del diaganostico a eliminar
     * @param tipoCIE CIE del diagnostico a eliminar
     * @return Integer -1 si no se pudo eliminar debido a que ese diagnosticos es usado.
     */
    public int eliminarRegistroDiagnostico(Connection con,String codigo,int tipoCIE);
    /**
     * Metodo que realiza una consulta de un diagnostcio, llevando como parametros los campos de la llave primaria, y retornando un ResultSetDecorator con los resultados de la consulta.
     * @param con Conexion
     * @param codigo Codigo del diagnostico
     * @param tipoCIE CIE del diagnostico
     * @return ResultSetDecorator resultado de la consulta.
     */
    public ResultSetDecorator consultarRegistroDiagnostico(Connection con,String codigo,int tipoCIE);
    
    /**
     * Metodo que realiza una busqueda avanzada en la tabla diagnostico por cualquir de sus columnas.
     * @param con Conexxion.
     * @param codigo Codigo del diagnostico.
     * @param conCodigo Si se busca por el codigo del diagnostico.
     * @param tipoCIE CIE del diagnostico.
     * @param conTipoCIE Si se busca por el tipo_cie del diagnostico.
     * @param descripcion Descripcion del diagnostico.
     * @param conDescripcion si se busca por la descripcion del diagnostico.
     * @param activo Estado del diagnostico
     * @param conActivo
     * @param conActivo Si se busca por el estado del diagnostico 
     * @param sexo Para manejar el sexo del diagnostico si masculino, fenmenino o sin restriccion
     * @param consexo si se busca por el sexo del diagnostico.     
     * @param edad_inicial Para manejar la edad inicial del diagnostico
     * @param conedad_inicial si se busca por la edad_inicial del diagnostico. 
     * @param edad_final Para manejar la edad final del diagnostico 
     * @param conedad_final si se busca por la edad_final del diagnostico.
     * @param es_principal Para manejar el indicativo del  diagnostico si es principal o no
     * @param cones_principal si se busca por el indicativo es_principal del diagnostico.
     * @param es_muerte Para manejar el indicativo del  diagnostico si es de muerte o no
     * @param cones_muerte si se busca por el indicativo de es_muerte del diagnostico. 
     * @return La collecion con los datos obtenidos en la consulta.
     */
    public  Collection consultarRegistroDiagnosticoAvanzada(Connection con,String codigo,boolean conCodigo,int tipoCIE,boolean conTipoCIE,String descripcion,boolean conDescripcion,boolean activo, boolean conActivo, int sexo , boolean conSexo, int edadInicial, boolean conEdadInicial, int edadFinal,  boolean conEdadFinal, String esPrincipal, boolean conEsPrincipal, String esMuerte , boolean conEsMuerte);    

    /**
     * Metodo que implementa el combo de sexo
     * @param con
     * @return
     */
    public  HashMap consultarSexo(Connection con);
    
    
	/**
	 * Método que carga el diagnóstico de egreso de una cuenta específica para
	 * vías de ingreso hospitalización y urgencias
	 * 
	 * @param con
	 *            Conexión con la BD
	 * @param idCuenta
	 *            Codigo de la cuenta del paciente
	 * @return Acrónimo del diagnóstico buscado
	 */
	public String cargarDiagnosticoHospUrg(Connection con, int idCuenta);
	
	/**
	 * Método que carga el diagnóstico de egreso de una cuenta específica para
	 * la vía de ingreso consulta externa
	 * 
	 * @param con
	 *            Conexión con la BD
	 * @param numeroSolicitud
	 *            Solicitud que generó la cita
	 * @return Acrónimo del diagnóstico buscado
	 */
	public String cargarDiagnosticoConsultaExternaAmb(	Connection con,
	        												int numeroSolicitud) ;
	/**
	 * Validacion que trae el ultimo diagnostico al que haya estado sujeto el paciente
	 *
	 * @param con
	 * @param cuenta
	 * @return ultimo diagnostico
	 */
	public String getUltimoDiagnosticoPaciente(Connection con,int cuenta);
}


