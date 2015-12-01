/*
 * ExcepcionesNaturalezaPaciente.java 
 * Autor			:  mdiaz
 * Creado el	:  17-ago-2004
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 * */
package com.princetonsa.mundo.parametrizacion;


import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ExcepcionesNaturalezaPacienteDao;
import util.InfoDatos;
import util.InfoDatosInt;
import util.ResultadoBoolean;

/**
 * definicion y encapsulacion de la clase que define segun el regimen
 * que cubre al paciente cuales excepciones tiene para que al momento
 *  de facturar no tenga ningun valor a cargo.
 *
 * @version 1.0, 17-ago-2004
 * @author <a href="mailto:miguel@PrincetonSA.com">Miguel Arturo Diaz</a>
 */
public class ExcepcionesNaturalezaPaciente {
  
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private ExcepcionesNaturalezaPacienteDao excepcionesNaturalezaDao;
	
	/** codigo de esta excepcion */
	private int codigo;
  
	/** InfoDatos que almacena el regimen */
	private InfoDatos regimen;
  
	/** InfoDatos que almacena la naturaleza */
	private InfoDatosInt naturaleza;
	
	/** estado que indica si este objeto es actualmente activo o inactivo para la aplicacion */
	private boolean indicativoExcepcion;
	
	
	/** constructor por defecto */
	public ExcepcionesNaturalezaPaciente(){
		clean();
		init(System.getProperty("TIPOBD"));
	}
	

	/** constructor con los valores a establecer por la aplicacion */
	public ExcepcionesNaturalezaPaciente(int codigo, String codigoRegimen, int codigoNaturaleza, boolean activo){
		clean();
		this.codigo = codigo;
		this.regimen.setAcronimo(codigoRegimen);
		this.naturaleza.setCodigo(codigoNaturaleza);
		this.indicativoExcepcion = activo;
		init(System.getProperty("TIPOBD"));
	}

		
	/** constructor con los valores a establecer por la aplicacion */
	public ExcepcionesNaturalezaPaciente(int codigo, String codigoRegimen, String nombreRegimen, int codigoNaturaleza, String nombreNaturaleza, boolean activo){
		clean();
		this.codigo = codigo;
		this.regimen.setAcronimo(codigoRegimen);
    this.regimen.setNombre(nombreRegimen);
		this.naturaleza.setCodigo(codigoNaturaleza);
		this.naturaleza.setNombre(nombreNaturaleza);
		this.indicativoExcepcion = activo;
		init(System.getProperty("TIPOBD"));
	}
	
	
	
	public void clean(){
		this.codigo = 0;
		this.regimen = new InfoDatos();
		this.naturaleza = new InfoDatosInt();
		this.indicativoExcepcion = true;
	}
	
	
	/**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
		if ( this.excepcionesNaturalezaDao == null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			this.excepcionesNaturalezaDao = myFactory.getExcepcionesNaturalezaPacienteDao();
			if( this.excepcionesNaturalezaDao != null )
				return true;
		}

		return false;
	}
	
	
	/**
	 * @return Returns the activo.
	 */
	public boolean getIndicativoExcepcion() {
		return indicativoExcepcion;
	}
	/**
	 * @param activo The activo to set.
	 */
	public void setIndicativoExcepcion(boolean activo) {
		this.indicativoExcepcion = activo;
	}
/**
 * @return Returns the codigo.
 */
public int getCodigo() {
	return codigo;
}
/**
 * @param codigo The codigo to set.
 */
public void setCodigo(int codigo) {
	this.codigo = codigo;
}
	/**
	 * @return Returns the naturaleza.
	 */
	public InfoDatosInt getNaturaleza() {
		return naturaleza;
	}
	/**
	 * @param naturaleza The naturaleza to set.
	 */
	public void setNaturaleza(InfoDatosInt naturaleza) {
		this.naturaleza = naturaleza;
	}
	/**
	 * @return Returns the regimen.
	 */
	public InfoDatos getRegimen() {
		return regimen;
	}
	/**
	 * @param regimen The regimen to set.
	 */
	public void setRegimen(InfoDatos regimen) {
		this.regimen = regimen;
	}


  public int insertar(Connection con){
  	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getExcepcionesNaturalezaPacienteDao().insertar( con, this.regimen.getAcronimo(), this.naturaleza.getCodigo(), this.indicativoExcepcion );
  }

  public int modificar(Connection con){
  	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getExcepcionesNaturalezaPacienteDao().modificar( con, this.codigo, this.regimen.getAcronimo(), this.naturaleza.getCodigo(), this.indicativoExcepcion );
  }

  public int eliminar(Connection con){
  	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getExcepcionesNaturalezaPacienteDao().eliminar( con, this.codigo );
  }
  
  public HashMap buscar(Connection con, String[] selectedColumns, String where, String orderBy){
	  return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getExcepcionesNaturalezaPacienteDao().buscar(con, selectedColumns, where, orderBy);
  }
  
  public HashMap buscar(Connection con, int codigo){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getExcepcionesNaturalezaPacienteDao().buscar(con, codigo);
  }

	/*
	 * *************************************** OJO *********************************************
	 * @author Juan David Ramírez López
	 *
	 * Princeton S.A.
	 * 
	 * Este método es utilizado por facturación, favor consultar para su modificación
	 * Atte: Juan David
	 */
	/**
	 * Método para buscar la excepción de farmacia de acuerdo
	 * al tipo de regimen y la naturaleza del paciente
	 * @param con Conexión con la BD
	 * @param tipoRegimen Tipo del regimen del paciente
	 * @param naturalezaPaciente Naturaleza del paciente
	 * @return true si tiene excepción, false de lo contrario, false con descripción
	 * (Codificada ApplicationReources) en caso de error
	 */
	public ResultadoBoolean buscarExcepcionPorNaturaleza(Connection con, String tipoRegimen, int naturalezaPaciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getExcepcionesNaturalezaPacienteDao().buscarExcepcionPorNaturaleza(con, tipoRegimen, naturalezaPaciente);
	}

  
}
