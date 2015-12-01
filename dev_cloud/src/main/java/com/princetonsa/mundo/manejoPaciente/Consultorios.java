package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.ConsultoriosDao;

/**
 * Mundo consultorios
 * @author Wilson Rios
 * wrios@princetonsa.com
 */
public class Consultorios 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static ConsultoriosDao consultoriosDao;
	
	/**
	 * centro de atencion
	 */
	private int centroAtencion;
	
	/**
	 * codigo (secuencia)
	 */
	private int codigo;
	
	/**
	 * codigo del consultorio
	 */
	private int codigoConsultorio;
	
	/**
	 * descripcion
	 */
	private String descripcion;
	
	/**
	 * activo S - N
	 */
	private String activo;
	
	/**
	 * 
	 */
	private String acronimoTipo;
	
	 /**
	 * resetea los atributos del objeto
	 *
	 */
	public void reset()
	{
		this.centroAtencion= ConstantesBD.codigoNuncaValido;
		this.codigo=ConstantesBD.codigoNuncaValido;
		this.codigoConsultorio=ConstantesBD.codigoNuncaValido;
		this.descripcion="";
		this.activo="";
		this.acronimoTipo="";
	}
	
	/**
	 * constructor de la clase
	 *
	 */
	public Consultorios() 
	{
		reset();
		this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		if (myFactory != null)
		{
			consultoriosDao = myFactory.getConsultoriosDao();
			wasInited = (consultoriosDao != null);
		}
		return wasInited;
	}

	/**
	 * Consulta los n consultorios x centro atencion 
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap consultoriosXCentroAtencionTipo(Connection con, int centroAtencion, String acronimoTipo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultoriosDao().consultoriosXCentroAtencion(con, centroAtencion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public HashMap consultarConsultorio(Connection con, int codigo)
	{
		return consultoriosDao.consultarConsultorio(con, codigo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public boolean insertar(Connection con, Consultorios consultorio)
	{
		return consultoriosDao.insertar(con, consultorio);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public boolean modificar(Connection con, Consultorios consultorio)
	{
		return consultoriosDao.modificar(con, consultorio);
	}
	
	/**
	 * @return the acronimoTipo
	 */
	public String getAcronimoTipo() {
		return acronimoTipo;
	}

	/**
	 * @param acronimoTipo the acronimoTipo to set
	 */
	public void setAcronimoTipo(String acronimoTipo) {
		this.acronimoTipo = acronimoTipo;
	}

	/**
	 * @return the activo
	 */
	public String getActivo() {
		return activo;
	}

	/**
	 * @param activo the activo to set
	 */
	public void setActivo(String activo) {
		this.activo = activo;
	}

	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the codigoConsultorio
	 */
	public int getCodigoConsultorio() {
		return codigoConsultorio;
	}

	/**
	 * @param codigoConsultorio the codigoConsultorio to set
	 */
	public void setCodigoConsultorio(int codigoConsultorio) {
		this.codigoConsultorio = codigoConsultorio;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * 
	 * @param con
	 * @param string
	 * @return
	 */
	public boolean eliminarRegistro(Connection con, int codigo)
	{
		return consultoriosDao.eliminarRegistro(con,codigo);
	}
	
	/**
	 * @return the centroAtencion
	 */
	public int getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

}