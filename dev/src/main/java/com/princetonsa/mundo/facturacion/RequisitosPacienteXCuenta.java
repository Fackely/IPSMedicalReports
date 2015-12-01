/*
 * @(#)RequisitosPacienteXCuenta.java
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import util.InfoDatosInt;
import util.RespuestaHashMap;
import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.RequisitosPacienteDao;

/**
 * Clase para registrar los tipos de requisitos
 * para la cuenta
 *
 * @version 1.0, 26/11/2004
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public class RequisitosPacienteXCuenta 
{
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static RequisitosPacienteDao reqDao;
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(RequisitosPacienteXCuenta.class);

	/**
	 * Código Cuenta
	 */
	private int codigoCuenta;
	
	/**
	 * Código - Descripcion Requisito del Paciente
	 */
	private InfoDatosInt requisitoPaciente;
	
	/**
	 * Indica si el requisito ya fue o no cumplido por el paciente
	 */
	private boolean requisitoCumplido;
	
	  /**
     * limpiar e inicializar atributos de la clase
     *
     */
    public void reset ()
    {
        this.requisitoPaciente= new InfoDatosInt();
        this.codigoCuenta=0;
        this.requisitoCumplido=false;
    }
    
	 /**
	  * Constructor vacio de la clase
	  *
	  */
	 public  RequisitosPacienteXCuenta ()
	 {
	     this.reset (); 
	     this.init (System.getProperty("TIPOBD"));
	 }
	 
	 /**
	  * Constructor con todos los campos
	  * @param codigo requisito paciente
	  * @param nombre requisito paciente
	  * @param codigoCuenta
	  * @param requisitoCumplido
	  */
	 public RequisitosPacienteXCuenta( 	int codigoRequisitoPaciente, 
	         												String descripcionRequisitoPaciente,
	         												int codigoCuenta,
	         												boolean requisitoCumplido)
	 {
	     this.requisitoPaciente = new InfoDatosInt(codigoRequisitoPaciente, descripcionRequisitoPaciente);
	     this.codigoCuenta=codigoCuenta;
	     this.requisitoCumplido=requisitoCumplido;
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
			reqDao = myFactory.getRequisitosPacienteDao();
			wasInited = (reqDao != null);
		}
		return wasInited;
	} 
	
	

	/**
	 * Método que carga los requisitos previos llenados por 
	 * un paciente (creación paciente) 
	 * 
	 * @param mapaAntiguo mapa a actualizar con los
	 * datos encontrados
	 * @param con Conexión con la fuente de datos
	 * @param idCuenta Id de la cuenta
	 * @param inice
	 * @return
	 * @throws SQLException
	 */
	public int cargarRequisitosPacienteXSubCuentaModificacionIndex(RespuestaHashMap mapaAntiguo, Connection con, int idCuenta, int indice) throws SQLException
	{
		String nombreColumnas[]={"codigoRequisito", "requisito", "cumplido"};
	    return UtilidadBD.resultSet2HashMapIndice(mapaAntiguo, nombreColumnas, reqDao.cargarRequisitosPacienteXSubCuentaModificacion(con, idCuenta), false, true, indice);
	}

	
	
	
	/**
	 * Adición de Sebastián
	 * Implementación del método que modifica un requisito de una SUBCUENTA previo
	 * llenado por un paciente para una BD Genérica
	 * 
	 * @see com.princetonsa.dao.RequisitosDao#modificarRequisitoPacienteCuenta (Connection , int , int,  boolean ) throws SQLException
	 */
	public int modificarRequisitoPacienteSubCuenta (Connection con, int idSubCuenta, int codigoRequisito, boolean cumplido){
			return reqDao.modificarRequisitoPacienteSubCuenta(con,idSubCuenta,codigoRequisito,cumplido);
		}
    /**
     * @return Returns the codigoCuenta.
     */
    public int getCodigoCuenta() {
        return codigoCuenta;
    }
    /**
     * @param codigoCuenta The codigoCuenta to set.
     */
    public void setCodigoCuenta(int codigoCuenta) {
        this.codigoCuenta = codigoCuenta;
    }
    /**
     * @return Returns the requisitoCumplido.
     */
    public boolean getRequisitoCumplido() {
        return requisitoCumplido;
    }
    /**
     * @param requisitoCumplido The requisitoCumplido to set.
     */
    public void setRequisitoCumplido(boolean requisitoCumplido) {
        this.requisitoCumplido = requisitoCumplido;
    }
    /**
     * @return Returns the requisitoPaciente.
     */
    public InfoDatosInt getRequisitoPaciente() {
        return requisitoPaciente;
    }
    /**
     * @param requisitoPaciente The requisitoPaciente to set.
     */
    public void setRequisitoPaciente(InfoDatosInt requisitoPaciente) {
        this.requisitoPaciente = requisitoPaciente;
    }
	/**
	 * Retorna el código del requisito
	 * @return
	 */
	public int getCodigoRequisito(){
		return requisitoPaciente.getCodigo();
	}
	/**
	 * Asigna el código del requisito
	 * @param codigoRequisito
	*/
	public void setCodigoRequisito(int codigoReqisito){
		this.requisitoPaciente.setCodigo(codigoReqisito);
	}
	/**
	 * Retorna la descripcion del requisito
	 * @return
	 */
	public String  getDescripcionRequisito(){
		return requisitoPaciente.getDescripcion();
	}
	/**
	 * Asigna la descripcion del requisito
	 * @param descripcionRequisito
	*/
	public void setDescripcionRequisito(String descripcionRequisito){
		this.requisitoPaciente.setDescripcion(descripcionRequisito);
	}

}
