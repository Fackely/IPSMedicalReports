/*
 * @(#)RegistroDiagnosticos.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 * 
 * Created on 18-ago-2004
 *
 * Jorge Armando Osorio Velasquez
 * Princeton
 */
package com.princetonsa.mundo.parametrizacion;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;


import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.RegistroDiagnosticosDao;
import com.princetonsa.dao.sqlbase.SqlBaseRegistroDiagnosticos;
import com.princetonsa.mundo.atencion.Diagnostico;

/**
 * Clase encargada de manejar la interacción del
 * usuario con los datos almacenados previamente
 * en la fuente de datos
 *  
 * @author armando
 *
 * Princeton 18-ago-2004
 */
public class RegistroDiagnosticos {
    
    
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static RegistroDiagnosticosDao regDiagnosticosDao;
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(RegistroDiagnosticos.class);
		
	/**
	 * Codigo del diagnostico
	 */
	private String codigo;
	/**
	 * Código CIE asignado por el sistema
	 */
    private int CIE;
    
	/**
	 * Nombre del CIE asignado
	 */
    private String nombreTipoCIE;
    
    /**
     * Descripcion del diagnostico
     */
    private String descripcion;
    
    /**
     *Estado del diagnostico 
     */
    private boolean estado;
   
    /**
     * Sexo  del diagnostico
     */
    private int sexo;
    
    /**
     * Edad inicial en Dias del diagnostico
     */
    private int edadInicial;
    
    /**
     * Edad final  del diagnostico
     */
    private int edadFinal;
    
    /**
     * Es diagnostico Principal
     */
    private String esPrincipal;
    
    /**
     * Es diagnostico de Muerte
     */
    private String esMuerte;
    
    /**
     * Descripcion Sexo
     */
    private String nomhSexo;

    
    /**
     * @return Retorna el cIE.
     */
    public int getCIE() {
        return CIE;
    }
    /**
     * @param cie asigna el parametro CIE.
     */
    public void setCIE(int cie) {
        CIE = cie;
    }
    /**
     * @return Retorna el nombreTipoCIE.
     */
    public String getNombreTipoCIE() {
        return nombreTipoCIE;
    }
    /**
     * @param nombreTipoCIE Asigna el nombreTipoCIE.
     */
    public void setNombreTipoCIE(String nombreTipoCIE) {
        this.nombreTipoCIE = nombreTipoCIE;
    }
    /**
     * @return Retorna el codigo.
     */
    public String getCodigo() {
        return codigo;
    }
    /**
     * @param codigo asigna el parametro codigo.
     */
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    /**
     * @return Retorna el descripcion.
     */
    public String getDescripcion() {
        return descripcion;
    }
    /**
     * @param descripcion asigna el parametro descripcion.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    /**
     * @return Retorna el estado.
     */
    public boolean getEstado() {
        return estado;
    }
    /**
     * @param estado asigna el parametro estado.
     */
    public void setEstado(boolean estado) {
        this.estado = estado;
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
				regDiagnosticosDao = myFactory.getRegistroDiagnosticosDao();
				wasInited = (regDiagnosticosDao != null);
			}
			return wasInited;
	}
	/**
	 * Metodo para resetear los campos del mundo
	 *
	 */
	public void reset()
	{
	    codigo="";
	    CIE=-1;
	    descripcion="";
	    estado=true;
	    sexo = 0;
	    edadInicial = 0;
	    edadFinal = 0;
	    esPrincipal = "N";
	    esMuerte = "N";
	    nomhSexo = "";
	}	
	
	/**
	 * Constructor de la clase, inicializa en vacio todos los parámetros.
	 */		
	public RegistroDiagnosticos()
	{
	    reset();
		this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * Constructon de la clase, Inicializa con los datos correspondientes.
	 * @param cod, String, Codigo del diagnostico.
	 * @param tCIE, int, Tipo de CIE del diagnostico.
	 * @param des, String, descripcion del diagnostico.
	 * @param est, boolean, Estado del diagnostico (Activo/Inactivo).
	 * @param sex, String, sexo del diagnostico.
	 * param edaIni, int, Edad inicial del diagnostico.
	 * param edaFin, int, Edad final del diagnostico.
	 * @param esPri, String, indicador de si es principal del diagnostico.
	 * @param esMue, String, indicador de si es muerte del diagnostico.
	 */
	public RegistroDiagnosticos(String cod,int tCIE,String des,boolean est, int sex, int edaIni, int edaFin, String esPri, String esMue)
	{
	    this.codigo=cod;
	    this.CIE=tCIE;
	    this.descripcion=des;
	    this.estado=est;
	    
	    this.sexo = sex;
	    this.edadInicial = edaIni ;
	    this.edadFinal = edaFin;
	    this.esPrincipal = esPri;
	    this.esMuerte = esMue;
	    
	    this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoViaIngreso
	 * @param cuentas
	 * @return
	 */
	public Diagnostico obtenerDiagnostico(Connection con, int codigoViaIngreso, Vector cuentas)
	{
		Diagnostico diagnostico=new Diagnostico();
		
		if(codigoViaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion 
			|| codigoViaIngreso==ConstantesBD.codigoViaIngresoUrgencias)
		{
			String diagTempo="";
			diagTempo=cargarDiagnosticoHospUrg(con, Integer.parseInt(cuentas.get(0).toString()));
			logger.info("Diag tempo "+diagTempo);
			if(diagTempo!=null)
			{
				diagnostico.setAcronimo(diagTempo.split("-")[0]);
				diagnostico.setTipoCIE(Integer.parseInt(diagTempo.split("-")[1]));
				logger.info("Diagnostico de Salida: "+diagTempo);
			}
			else
			{
				diagnostico=null;
				logger.error("error consultando el diagnostico de salida de la cuenta "+cuentas.get(0));
			}
		}
		return diagnostico;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoViaIngreso
	 * @param numeroSolicitud
	 * @return
	 */
	public Diagnostico obtenerDiagnosticoConsultaExternaAmb(	Connection con, int codigoViaIngreso, int numeroSolicitud )
	{
		Diagnostico diagnostico=new Diagnostico();
		if(	codigoViaIngreso==ConstantesBD.codigoViaIngresoConsultaExterna 
			|| codigoViaIngreso==ConstantesBD.codigoViaIngresoAmbulatorios)
		{
			String diagTempo=cargarDiagnosticoConsultaExternaAmb(con, numeroSolicitud);
			if(diagTempo!=null)
			{
				diagnostico.setAcronimo(diagTempo.split("-")[0]);
				diagnostico.setTipoCIE(Integer.parseInt(diagTempo.split("-")[1]));
				logger.info("Diagnostico: "+diagTempo);
			}
			else
			{
				diagnostico=null;
				logger.error("error consultando el diagnostico para la solicitud "+numeroSolicitud);
			}
		}
		return diagnostico;
	}
	
	
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
	public static String cargarDiagnosticoHospUrg(Connection con, int idCuenta) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroDiagnosticosDao().cargarDiagnosticoHospUrg(con, idCuenta);
	}
	
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
	public static String cargarDiagnosticoConsultaExternaAmb(	Connection con,
	        												int numeroSolicitud) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroDiagnosticosDao().cargarDiagnosticoConsultaExternaAmb(con, numeroSolicitud);
	}
	
	/**
	 * Metodo para insertar un diagnosrico en la BD.
	 * @param con, Conexion
	 * @return -1 Si fallo la insercion
	 */
	public int insertarDiagnostico(Connection con)
	{	
	    return regDiagnosticosDao.insertarRegistroDiagnostico(con,codigo,CIE,descripcion,estado,sexo,edadInicial,edadFinal,esPrincipal,esMuerte);
	}
    /**
     * @param con, Conexion
     * @return -1 Si fallo la modificacion
     */
    public int modificarDiagnostico(Connection con) 
    {	//(Connection con,String descripcion,boolean activo,String codigo,int tipoCIE , int sexo , int edad_inicial , int edad_final, String es_principal, String es_muerte);
        return regDiagnosticosDao.modificarRegistroDiagnostico(con,descripcion,estado,codigo,CIE,sexo,edadInicial,edadFinal,esPrincipal,esMuerte);
    }	
	/**
	 * Metodo que realiza la consulta de un diagnostico especifico
	 * @param con, Conexion a la BD
	 * @param cod, Codigo del diagnostico
	 * @param tcie, codigo de CIE del diagnostico
	 */
	public void consultarDiagnostico(Connection con,String cod,int tcie)
	{
	    ResultSetDecorator rs;
	    try
	    {
		    rs=regDiagnosticosDao.consultarRegistroDiagnostico(con,cod,tcie);
		    rs.next();
		    this.codigo=rs.getString("codigo");
		    this.CIE=rs.getInt("tipoCIE");
		    this.nombreTipoCIE=rs.getString("nombreTipoCIE");
		    this.descripcion=rs.getString("descripcion");
		    this.estado=rs.getBoolean("estado");
		    //
		    this.sexo=rs.getInt("sexo");
		    this.edadInicial=rs.getInt("edad_inicial");
		    this.edadFinal=rs.getInt("edad_final");
		    this.esPrincipal=rs.getString("es_principal");
		    this.esMuerte=rs.getString("es_muerte");
		    this.nomhSexo =rs.getString("nomhSexo");
		    
	    }
	    catch(SQLException e)
	    {
	        logger.warn("Erro al obtener los datos"+e.toString());
	    }
	}
	/**
	 * Metodo que realiza la consulta avanzada sobre la tabla diagnosticos.
	 * @param con, Conexion
	 * @param conCodigo, boolean, Si se consulta por codigo
	 * @param conTipoCIE, boolean, Si se consulta por TipoCIE
	 * @param conDescripcion, boolean, Si se consulta por Descripcion
	 * @param conActivo
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
     * @see SqlBaseRegistroDiagnosticos.consultarRegistroDiagnosticoAvanzada 
	 * @return Colection, El resultado de la consulta.
	 */
	public Collection consultaAvanzada(Connection con,boolean conCodigo,boolean conTipoCIE,boolean conDescripcion, boolean conActivo, boolean conSexo,  boolean conEdadInicial,boolean conEdadFinal,boolean conEsPrincipal , boolean conEsMuerte)
	{
	    return regDiagnosticosDao.consultarRegistroDiagnosticoAvanzada(con,codigo,conCodigo,CIE,conTipoCIE,descripcion,conDescripcion,estado,conActivo, sexo ,  conSexo,  edadInicial,  conEdadInicial,  edadFinal,   conEdadFinal,  esPrincipal,  conEsPrincipal,  esMuerte ,  conEsMuerte);
	}
    /**
     * Metodo para eliminar un diagnostico
     * @param con, conexion.
     * @return -1 si produjo error la eliminacion.
     */
    public int eliminarDiagnostico(Connection con) 
    {
        return regDiagnosticosDao.eliminarRegistroDiagnostico(con,codigo,CIE);
    }
    
    /**
     * Metodo para verificar si existe un diagnostico.
     * @param con, conexion
     * @param acronimo, String, codigo del diagnostico por el que sa buscara-
     * @param codigoTipoCie, int, tipo CIE por el que se buscara.
     * @return boolean, exste o no.
     */
    public boolean existeDiagnostico(Connection con, String acronimo, int codigoTipoCie) 
    {
	    ResultSetDecorator rs;
        try 
        {
            rs=regDiagnosticosDao.consultarRegistroDiagnostico(con,acronimo,codigoTipoCie);
            return rs.next();
        } catch (SQLException e) {
            logger.warn("error realizando la verificacion"+e.toString());
        }
        return true;
    }
    
    
	/**
	 * Validacion que trae el ultimo diagnostico al que haya estado sujeto el paciente
	 *
	 * @param con
	 * @param cuenta
	 * @return ultimo diagnostico
	 */
	public String getUltimoDiagnosticoPaciente(Connection con,int cuenta) {
		// TODO Auto-generated method stub
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroDiagnosticosDao().getUltimoDiagnosticoPaciente(con, cuenta);
	}

    /**
     * Metodo que implementa el combo de sexo
     * @param con
     * @return
     */
    public  HashMap consultarSexo(Connection con)
    {
    	return regDiagnosticosDao.consultarSexo(con);
    }
	
	public int getSexo() {
		return sexo;
	}
	public void setSexo(int sexo) {
		this.sexo = sexo;
	}
	public int getEdadFinal() {
		return edadFinal;
	}
	public void setEdadFinal(int edadFinal) {
		this.edadFinal = edadFinal;
	}
	public int getEdadInicial() {
		return edadInicial;
	}
	public void setEdadInicial(int edadInicial) {
		this.edadInicial = edadInicial;
	}
	public String getEsMuerte() {
		return esMuerte;
	}
	public void setEsMuerte(String esMuerte) {
		this.esMuerte = esMuerte;
	}
	public String getEsPrincipal() {
		return esPrincipal;
	}
	public void setEsPrincipal(String esPrincipal) {
		this.esPrincipal = esPrincipal;
	}
	public String getNomhSexo() {
		return nomhSexo;
	}
	public void setNomhSexo(String nomSexo) {
		this.nomhSexo = nomSexo;
	}

}