/*
 * @(#)HistoriaClinica.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadFecha;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.HistoriaClinicaDao;

/**
 * Esta clase encapsula los atributos y la funcionalidad de una Historia Clinica.
 *
 * @version 1.0, Oct 16, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class HistoriaClinica 
{
	
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	private Logger logger = Logger.getLogger(HistoriaClinica.class);
	
	/**
	 * C�digo del paciente
	 */
	private int codigoPaciente=0;

	/**
	 * El informaci�n de la historia clinica anterior .
	 */
	private String historiaClinicaAnterior;

	/**
	 * La fecha de apertura de la historia Cl�nica
	 */
	private String fechaApertura="";

	/**
	 * El DAO usado por el objeto <code>HistoriaClinica</code> para acceder a la fuente de datos.
	 */
	private static HistoriaClinicaDao historiaClinicaDao = null;


	/**
	 * Retorna el historiaClinicaAnterior.
	 * @return int el historiaClinicaAnterior de la historia cl�nica
	 */
	public String getHistoriaClinicaAnterior() {
		return historiaClinicaAnterior;
	}

	/**
	 * Establece el historiaClinicaAnterior.
	 * @param historiaClinicaAnterior El historiaClinicaAnterior de la historia cl�nica a establecer
	 */
	public void setHistoriaClinicaAnterior(String consecutivo) {
		this.historiaClinicaAnterior = consecutivo;
	}

	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores validos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init (String tipoBD) {

		if (historiaClinicaDao == null) {
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			historiaClinicaDao = myFactory.getHistoriaClinicaDao();
		}

	}
	
	/**
	 * Inserta una historia clinica en una fuente de datos, reutilizando
	 * una conexion existente. Maneja la transacci�n internamente 
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param historiaClinicaAnterior n�mero y observaciones sobre la historia
	 * cl�nica anterior de este paciente
	 * @param codigoPaciente c�digo del paciente al cual pertenece la historia cl�nica
	 * @return numero de historias insertados
	 */
	public int insertarHistoriaClinica (Connection con, String historiaClinicaAnterior, int codigoPaciente) throws SQLException
	{
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    int resp=this.insertarHistoriaClinicaTransaccional(con, historiaClinicaAnterior, codigoPaciente,ConstantesBD.inicioTransaccion);
	    if (resp>0)
	    {
	        myFactory.endTransaction(con);
	    }
	    else
	    {
	        myFactory.abortTransaction(con);
	    }
	    return resp;
	}

	/**
	 * Inserta una historia clinica en una fuente de datos, reutilizando
	 * una conexion existente. Soporta definir la parte donde se
	 * encuentra este m�todo en la transacci�n
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param historiaClinicaAnterior n�mero y observaciones sobre la historia
	 * cl�nica anterior de este paciente
	 * @param codigoPaciente c�digo del paciente al cual pertenece la historia cl�nica
	 * @estado estado Estado en el que se desea definir que va la transacci�n
	 * @return numero de historias insertados
	 */
	public int insertarHistoriaClinicaTransaccional (Connection con, String historiaClinicaAnterior, int codigoPaciente, String estado) throws SQLException
	{
		int numElementosInsertados=0;
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    if (estado.equals(ConstantesBD.inicioTransaccion))
	    {
	    	if (!myFactory.beginTransaction(con))
	        {
	        	myFactory.abortTransaction(con);
	        }
	    }
	    try
	    {
	    	
	    	numElementosInsertados=historiaClinicaDao.insertarHistoriaClinica(con, historiaClinicaAnterior, codigoPaciente);
	          
	        if (numElementosInsertados<=0)
	        {
	        	myFactory.abortTransaction(con);
	        }
	    }
	    catch (SQLException e)
	    {
	    	myFactory.abortTransaction(con);
	    	logger.error("Error al tratar de insertar la historia clinica del paciente : "+e);
	        throw e;
	    }
	      
	    if (estado.equals(ConstantesBD.finTransaccion))
	    {
	    	myFactory.endTransaction(con);
	    }
	    return numElementosInsertados;
	}

	/**
	 * M�todo que carga los datos de una historia cl�nica de
	 * acuerdo al c�digo de paciente establecido en el objeto
	 * 
	 * @param con Conexi�n con la fuente de datos
	 * @throws SQLException
	 */
	public void cargarHistoriaClinica(Connection con) throws SQLException
	{
		ResultSetDecorator rs=historiaClinicaDao.cargarHistoriaClinica(con, this.getCodigoPaciente());
		if (rs.next())
		{
			this.historiaClinicaAnterior=rs.getString("historiaClinicaAnterior");
			this.fechaApertura=UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechaApertura"));
		}
	}
	
	/**
	 * M�todo implementado para modificar la descripcion de la historia clinica previa del paciente
	 * @param con
	 * @param codigoPaciente
	 * @param historiaAnterior
	 * @return
	 */
	public int modificarHistoriaPrevia(Connection con)
	{
		return historiaClinicaDao.modificarHistoriaPrevia(con,this.codigoPaciente,this.historiaClinicaAnterior);
	}
	
	/**
	 * @return
	 */
	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	/**
	 * @param i
	 */
	public void setCodigoPaciente(int i) {
		codigoPaciente = i;
	}

	/**
	 * @return
	 */
	public String getFechaApertura() {
		return fechaApertura;
	}

	/**
	 * @param string
	 */
	public void setFechaApertura(String string) {
		fechaApertura = string;
	}

}