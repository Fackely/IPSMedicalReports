/*
 * @(#)IngresoGeneral.java
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
import com.princetonsa.dto.facturacion.DtoIngresosFactura;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.jsp.PageContext;

import org.apache.log4j.Logger;

import util.Answer;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.IngresoGeneralDao;

/**
 * Esta clase encapsula los atributos y la funcionalidad de un ingreso de paciente a la entidad que usa el sistema.
 *
 * @version 1.0, Oct 16, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class IngresoGeneral 
{
	/**
	 * Para hacer logs de esta clase.
	 */
	private static Logger logger = Logger.getLogger(IngresoGeneral.class);
	
	/**
	 * Numero de identificacion de este ingreso.
	 */
	private String idIngreso="";

	/**
	 * Año en el que se efectua el ingreso.
	 */
	private String anioIngreso="";

	/**
	 * Mes en el que se efectua el ingreso.
	 */
	private String mesIngreso="";

	/**
	 * Dia en el que se efectua el ingreso.
	 */
	private String diaIngreso="";

	/**
	 * Hora en la que se efectua el ingreso.
	 */
	private String horaIngreso="";

	/**
	 * Año en el que se efectua el egreso.
	 */
	private String anioEgreso="";

	/**
	 * Mes en el que se efectua el egreso.
	 */
	private String mesEgreso="";

	/**
	 * Dia en el que se efectua el egreso.
	 */
	private String diaEgreso="";

	/**
	 * Hora en la que se efectua el egreso.
	 */
	private String horaEgreso="";

	/**
	 * Nombre de la institución desde la cual se creo este ingreso
	 */
	private String institucion="";

	/**
	 * Código de la institución desde la cual se creo este ingreso
	 */
	private String codigoInstitucion="";
	
	/**
	 * Estado del ingreso
	 */
	private String estado;
	
	/**
	 * Login del usuario
	 */
	private String loginUsuario;
	
	/**
	 * Consecutivo del ingreso
	 */
	private String consecutivo;
	
	/**
	 * Anio del consecutivo
	 */
	private String anioConsecutivo;
	
	
	/**
	 * Centro de Atencion
	 */
	private int centroAtencion;
	
	/**
	 * Consecutivo del registro de paciente entidad subcontratada asociado al ingreso
	 */
	private String pacEntidadSubcontratada;
	

	/**
	 * Datos del paciente asociado a este ingreso
	 */
	private PersonaBasica paciente;

	/**
	 * El DAO usado por el objeto <code>IngresoGeneral</code> para acceder a la fuente de datos.
	 */
	private static IngresoGeneralDao ingresoGeneralDao = null;

	private String transplante=""; 	


	/**
	 * Constructor pasándole los atributos de la clase, incluido el paciente,
	 * pero excluyendo el idIngreso (este se genera en la BD).
	 * @param institucion institución a la cual pertenece este Ingreso
	 * @param pac objeto <code>PersonaBasica</code> con los datos básicos del paciente
	 * @param centroAtencion 
	 */
	public IngresoGeneral (String institucion, PersonaBasica pac, String estado, String loginUsuario,String consecutivo,String anioConsecutivo, int centroAtencion,String pacEntidadSubcontratada,String fechaIngreso,String horaIngreso,String transplante) {
		this.idIngreso = "";
		this.paciente = pac;
		this.institucion=institucion;
		this.estado = estado;
		this.loginUsuario = loginUsuario;
		this.consecutivo = consecutivo;
		this.anioConsecutivo = anioConsecutivo;
		this.centroAtencion = centroAtencion;
		this.pacEntidadSubcontratada = pacEntidadSubcontratada;
		this.horaIngreso = horaIngreso;
		if(fechaIngreso.equals(""))
		{
			this.anioIngreso = "";
			this.mesIngreso = "";
			this.diaIngreso = "";
		}
		else
		{
			String[] fecha = fechaIngreso.split("/");
			this.anioIngreso = fecha[2];
			this.mesIngreso = fecha[1];
			this.diaIngreso = fecha[0];
			
		}
		this.transplante=transplante;
		this.init(System.getProperty("TIPOBD"));
	}

	/**
	 * Constructor para el ingreso general
	 *
	 */
	public IngresoGeneral() 
	{
		this.paciente = new PersonaBasica();
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}

	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores validos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init (String tipoBD) {

		if (ingresoGeneralDao == null) {
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			ingresoGeneralDao = myFactory.getIngresoGeneralDao();
		}

	}

	/**
	 * metodo para cargar los ingresos y cuentas corresponduientes dados los estados,
	 * key={idingreso, nombreestadoingreso, codigoestadocuenta, nombreestadocuenta, codigoviaingreso, nombreviaingreso}
	 * @param con
	 * @param estadosIngresoVector
	 * @param estadosCuentaVector
	 * @return
	 */
	public static HashMap obtenerIngresosCuenta(Vector estadosIngresoVector, Vector estadosCuentaVector)
	{
		Connection con= UtilidadBD.abrirConexion();
		HashMap mapa= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getIngresoGeneralDao().obtenerIngresosCuenta(con, estadosIngresoVector, estadosCuentaVector);
		UtilidadBD.closeConnection(con);
		return mapa;
	}

	/**
	 * Método que modifica el ingreso insertando la hora y fecha actual de la BD ademas del indicativo de control post operatorio.
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public static boolean actualizarEstadoIngreso(Connection con, String idIngreso, String estado, String loginUsuario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getIngresoGeneralDao().actualizarEstadoIngreso(con, idIngreso, estado, loginUsuario);
	}
	
	/**
	 * Método que modifica el ingreso insertando la hora y fecha actual de la BD.
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public static boolean actualizarControlPostOperatorioCx(Connection con, String idIngreso, String estado, String loginUsuario)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getIngresoGeneralDao().actualizarControlPostOperatorioCx(con, idIngreso, estado, loginUsuario);
	}
	
	
	
	
	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static boolean existeIngresoAbierto(Connection con, String codigoPaciente, String idIngresoRestriccion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getIngresoGeneralDao().existeIngresoAbierto(con, codigoPaciente, idIngresoRestriccion);
	}
	
	/**
	 * 
	 * @param con
	 * @param mapaRestricciones
	 * @return
	 */
	public static HashMap<Object, Object> cargarListadoIngresos(Connection con, HashMap<Object, Object> mapaRestricciones)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getIngresoGeneralDao().cargarListadoIngresos(con, mapaRestricciones);
	}
	
	/**
	 * Retorna el año de ingreso.
	 * @return el año de ingreso
	 */
	public String getAnioIngreso() {
		return anioIngreso;
	}

	/**
	 * Retorna el dia de ingreso.
	 * @return el dia de ingreso
	 */
	public String getDiaIngreso() {
		return diaIngreso;
	}

	/**
	 * Retorna la identificacion de este ingreso en la base de datos.
	 * @return identificacion de este ingreso en la base de datos
	 */
	public String getIdIngreso() {
		return idIngreso;
	}

	/**
	 * Retorna el mes de ingreso.
	 * @return el mes de ingreso
	 */
	public String getMesIngreso() {
		return mesIngreso;
	}

	/**
	 * Retorna la hora de ingreso.
	 * @return la hora de ingreso
	 */
	public String getHoraIngreso() {
		return horaIngreso;
	}

	/**
	 * Retorna el codigo del tipo de identificacion del paciente ingresado.
	 * @return el codigo del tipo de identificacion del paciente
	 */
	public String getCodigoTipoIdentificacionPaciente() {
		return paciente.getCodigoTipoIdentificacionPersona();
	}

	/**
	 * Retorna el nombre completo del paciente.
	 * @param encoded especifica si se debe o no retornar esta cadena como <i>character entities</i>
	 * de HTML (e.g., "á" como "&amp;aacute;"
	 * @return el nombre del paciente
	 */
	public String getNombrePaciente(boolean encoded) {
		return paciente.getNombrePersona(encoded);
	}

	/**
	 * Retorna el nombre del paciente (completo, incluye apellidos).
	 * (codificado como <i>character entities</i> de HTML, e.g., "á" como "&amp;aacute;").
	 * @return el nombre del paciente
	 */
	public String getNombrePaciente() {
		return paciente.getNombrePersona();
	}

	/**
	 * Retorna el numero de identificacion paciente.
	 * @return el numero de identificacion paciente.
	 */
	public String getNumeroIdentificacionPaciente() {
		return paciente.getNumeroIdentificacionPersona();
	}

	/**
	 * Retorna el tipo de identificacion del paciente.
	 * @param encoded especifica si se debe o no retornar esta cadena como <i>character entities</i>
	 * de HTML (e.g., "á" como "&amp;aacute;"
	 * @return el tipo de identificacion del paciente
	 */
	public String getTipoIdentificacionPaciente(boolean encoded) {
		return paciente.getTipoIdentificacionPersona(encoded);
	}

	/**
	 * Retorna el tipo de identificacion del paciente.
	 * (codificado como <i>character entities</i> de HTML, e.g., "á" como "&amp;aacute;").
	 * @return el tipo de identificacion del paciente
	 */
	public String getTipoIdentificacionPaciente() {
		return paciente.getTipoIdentificacionPersona();
	}

	/**
	 * Returna el paciente objeto de este ingreso.
	 * @return el paciente
	 */
	public PersonaBasica getPaciente() {
		return paciente;
	}

	/**
	 * Retorna el año de Egreso.
	 * @return el año de Egreso
	 */
	public String getAnioEgreso() {
		return anioEgreso;
	}
	
	/**
	 * Retorna la fecha de ingreso
	 * @return
	 */
	public String getFechaIngreso()
	{
		String fechaIngreso = "";
		if(!this.anioIngreso.equals("")&&!this.mesIngreso.equals("")&&!this.diaIngreso.equals(""))
			fechaIngreso = this.diaIngreso + "/" + this.mesIngreso + "/" + this.anioIngreso;
		
		return fechaIngreso;
	}
	
	/**
	 * Retorna el dia de Egreso.
	 * @return el dia de Egreso
	 */
	public String getDiaEgreso() {
		return diaEgreso;
	}

	/**
	 * Retorna el mes de Egreso.
	 * @return el mes de Egreso
	 */
	public String getMesEgreso() {
		return mesEgreso;
	}

	/**
	 * Retorna la hora de Egreso.
	 * @return la hora de Egreso
	 */
	public String getHoraEgreso() {
		return horaEgreso;
	}

	/**
	 * Retorna el código de la institucion.
	 * @return String
	 */
	public String getInstitucion() {
		return institucion;
	}

	/**
	 * Establece el año de ingreso.
	 * @param anioIngreso El año de ingreso a establecer
	 */
	public void setAnioIngreso(String anioIngreso) {
		this.anioIngreso = anioIngreso;
	}

	/**
	 * Establece el dia del ingreso.
	 * @param diaIngreso El dia de ingreso a establecer
	 */
	public void setDiaIngreso(String diaIngreso) {
		this.diaIngreso = diaIngreso;
	}

	/**
	 * Establece la identificacion de este ingreso en la base de datos.
	 * @param idIngreso La identificacion en la base de datos del ingreso a establecer
	 */
	public void setIdIngreso(String idIngreso) {
		this.idIngreso = idIngreso;
	}

	/**
	 * Establece el mes de ingreso del paciente.
	 * @param mesIngreso El mes de ingreso a establecer
	 */
	public void setMesIngreso(String mesIngreso) {
		this.mesIngreso = mesIngreso;
	}

	/**
	 * Establece la hora de ingreso.
	 * @param horaIngreso la hora de ingreso a establecer
	 */
	public void setHoraIngreso(String horaIngreso) {
		this.horaIngreso = horaIngreso;
	}

	/**
	 * Establece el paciente.
	 * @param paciente el paciente a ser establecido
	 */
	public void setPaciente(PersonaBasica paciente) {
		this.paciente = paciente;
	}

	/**
	 * Establece el año de Egreso.
	 * @param anioEgreso El año de Egreso a establecer
	 */
	public void setAnioEgreso(String anioEgreso) {
		this.anioEgreso = anioEgreso;
	}

	/**
	 * Establece el dia del Egreso.
	 * @param diaEgreso El dia de Egreso a establecer
	 */
	public void setDiaEgreso(String diaEgreso) {
		this.diaEgreso = diaEgreso;
	}

	/**
	 * Establece el mes de Egreso del paciente.
	 * @param mesEgreso El mes de Egreso a establecer
	 */
	public void setMesEgreso(String mesEgreso) {
		this.mesEgreso = mesEgreso;
	}

	/**
	 * Establece la hora de Egreso.
	 * @param horaEgreso la hora de Egreso a establecer
	 */
	public void setHoraEgreso(String horaEgreso) {
		this.horaEgreso = horaEgreso;
	}

	/**
	 * Establece el nombre de la institucion.
	 * @param institucion El nombre de la institucion a establecer
	 */
	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}


	public String getTransplante() {
		return transplante;
	}

	public void setTransplante(String transplante) {
		this.transplante = transplante;
	}
	/**
	 * Este metodo inicializa en valores vacios (mas no nulos) los atributos del ingreso general
	 */
	public void clean () {

		this.paciente.clean();
		this.setAnioIngreso("");
		this.setDiaIngreso("");
		this.setIdIngreso("");
		this.setMesIngreso("");
		this.setHoraIngreso("");
		this.setDiaEgreso("");
		this.setMesEgreso("");
		this.setAnioEgreso("");
		this.setHoraEgreso("");
		this.setEstado("");
		this.setLoginUsuario("");
		this.setConsecutivo("");
		this.setAnioConsecutivo("");
		this.setCentroAtencion(0);
		this.setPacEntidadSubcontratada("");

	}

	/**
	 * Inserta un ingreso en una fuente de datos, reutilizando una conexion existente, con los datos
	 * presentes en los atributos de este objeto.
	 * @param con una conexion abierta con una fuente de datos
	 * @return numero de ingresos insertados
	 */
	public int insertarIngreso(Connection con) throws SQLException 
	{
	
		int posibleCodigo=ingresoGeneralDao.insertarIngreso(con, 
		this.paciente.getCodigoPersona(), getInstitucion(), getEstado(), getLoginUsuario(),getConsecutivo(),getAnioConsecutivo(),
		getCentroAtencion(),getPacEntidadSubcontratada(),getFechaIngreso(),getHoraIngreso(),getTransplante());
		if (posibleCodigo>0)
		{
			idIngreso=posibleCodigo+"";
		}
		else
		{
			this.idIngreso="";
		}
		
		return posibleCodigo;
	}

	/**
	 * Inserta un ingreso en una fuente de datos, reutilizando una conexion existente, con los datos
	 * presentes en los atributos de este objeto, especificando en que parte de
	 * la transacción se está 
	 * @param con una conexion abierta con una fuente de datos
	 * @return numero de ingresos insertados
	 */
	public int insertarIngresoTransaccional (Connection con, String estado)  
	{

		try
		{
			int posibleCodigo=ingresoGeneralDao.insertarIngresoTransaccional(con, 
			this.paciente.getCodigoPersona(), getInstitucion(), estado, getEstado(), getLoginUsuario(), getConsecutivo(), getAnioConsecutivo(),
			getCentroAtencion(),getPacEntidadSubcontratada(),getFechaIngreso(),getHoraIngreso(),getTransplante());
			if (posibleCodigo>0)
			{
				idIngreso=posibleCodigo+"";
			}
			else
			{
				this.idIngreso="";
			}
			
			return posibleCodigo;
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarIngresoTransaccional: "+e);
			return 0;
		}


	}


	/**
	 * Dada una identificacion de una ingreso, establece las propiedades de un objeto <code>Ingreso</code>
	 * en los valores correspondientes.
	 * @param con una conexion abierta con una fuente de datos
	 * @param idIngreso numero de identificacion del ingreso que se desea cargar
	 */
	public void cargarIngreso(Connection con, String idIngreso) throws SQLException {

		this.clean();
		Answer ans = ingresoGeneralDao.cargarIngreso(con, idIngreso);
		ResultSetDecorator rs = ans.getResultSet();

		if (rs.next()) {

			setIdIngreso(idIngreso);
			paciente.setNumeroIdentificacionPersona(rs.getString("numeroIdentificacion"));
			paciente.setCodigoTipoIdentificacionPersona(rs.getString("codigoTipoIdentificacion"));
			paciente.setTipoIdentificacionPersona(rs.getString("tipoIdentificacion"));
			paciente.setNombrePersona(rs.getString("primerNombre") + " " + rs.getString("segundoNombre") + " " + rs.getString("primerApellido") + " " + rs.getString("segundoApellido"));
			String fechaIngreso=rs.getString("fechaIngreso");
			String [] fecha;
			
			if(fechaIngreso!=null && !fechaIngreso.trim().equals(""))
			{	
				fecha = UtilidadTexto.separarNombresDeCodigos(rs.getString("fechaIngreso"), 2);
		    	setAnioIngreso(fecha[0]);
			    setMesIngreso(fecha[1]);
			    setDiaIngreso(fecha[2]); 
			    setHoraIngreso(rs.getString("horaIngreso"));
			}
			setInstitucion(rs.getString("institucion"));
			setCodigoInstitucion(rs.getString("codigoInstitucion"));

			String fechaEgreso=rs.getString("fechaEgreso");
			logger.info("\n String Fecha EGRESO >>>"+fechaEgreso );
			
			if (fechaEgreso!=null && !fechaEgreso.trim().equals(""))
			{
				logger.info("\n Entro si Fecha Egreso <> null  >>>" );
				fecha=UtilidadTexto.separarNombresDeCodigos(fechaEgreso, 2);
				setDiaEgreso(fecha[2]);
				setMesEgreso(fecha[1]);
				setAnioEgreso(fecha[0]);
				setHoraEgreso(rs.getString("horaIngreso"));
			}

		}

	}

	public void cargarPacienteDadoIngreso(Connection con, String idIngreso)
	{
		this.paciente= new PersonaBasica();
		int codigoPersona=ingresoGeneralDao.cargarPacienteDadoIngreso(con, idIngreso);
		try 
		{
			paciente.cargar(con, codigoPersona);
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Modifica un ingreso en una fuente de datos, reutilizando una conexion existente, con los datos
	 * presentes en los atributos de este objeto.
	 * @param con una conexion abierta con una fuente de datos
	 * @param idIngreso numero de identificacion de este ingreso
	 * @return numero de ingresos modificados
	 */
	public int modificarIngreso(Connection con, String idIngreso) throws SQLException {
		return ingresoGeneralDao.modificarIngreso(con, anioEgreso, mesEgreso, diaEgreso, horaEgreso, idIngreso);
	}
	
	/**
	 * Modifica un ingreso en una fuente de datos, reutilizando una conexion existente, con los datos
	 * presentes en los atributos de este objeto.
	 * @param con una conexion abierta con una fuente de datos
	 * @param idIngreso numero de identificacion de este ingreso
	 * @param estado de transaccionalidad
	 * @return numero de ingresos modificados
	 */
	public int modificarIngresoTransaccional (Connection con, String idIngreso, String estado ) throws SQLException
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
	        numElementosInsertados=ingresoGeneralDao.modificarFechaHoraIngreso(con,  idIngreso);
	        
	        if (numElementosInsertados<=0)
	        {
	            myFactory.abortTransaction(con);
	        }
	    }
	    catch (SQLException e)
	    {
	        myFactory.abortTransaction(con);
	        throw e;
	    }
	    
	    if (estado.equals(ConstantesBD.finTransaccion))
	    {
	        myFactory.endTransaction(con);
	    }
	    return numElementosInsertados;
	}
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public static int modificarFechaHoraEgresoIngreso (Connection con, String idIngreso ) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getIngresoGeneralDao().modificarFechaHoraIngreso(con, idIngreso);
	}
	
	/**
	 * Modifica un ingreso en una fuente de datos, reutilizando una conexion existente, con los datos
	 * presentes en los atributos de este objeto.
	 * @param con una conexion abierta con una fuente de datos
	 * @param idIngreso numero de identificacion de este ingreso
	 * @param estado de transaccionalidad
	 * @return numero de ingresos modificados
	 */
	public int modificarIngresoTransaccional2 (Connection con, String idIngreso, String estado ) throws SQLException
	{
	    int numElementosInsertados=0;
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    if (estado.equals(ConstantesBD.inicioTransaccion))
	        myFactory.beginTransaction(con);
	    
	    numElementosInsertados=ingresoGeneralDao.modificarFechaHoraIngreso(con,  idIngreso);
	    
	    if (estado.equals(ConstantesBD.finTransaccion))
	        myFactory.endTransaction(con);
	    return numElementosInsertados;
	}
	
	/**
	 * metodo que deja vacias (abierta) la fecha y hora de egreso en la table ingresos
	 * @param con
	 * @param idCuenta
	 * @return
	 * @throws SQLException
	 */
	public boolean reversarFechaHoraEgresoTransaccional(Connection con, int idCuenta, String estado) throws SQLException
	{
	    boolean insertados=false;
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
	        insertados=ingresoGeneralDao.reversarFechaHoraEgreso(con, idCuenta);
	        if (!insertados)
	        {
	            myFactory.abortTransaction(con);
	        }
	    }
	    catch (SQLException e)
	    {
	        myFactory.abortTransaction(con);
	        throw e;
	    }
	    if (estado.equals(ConstantesBD.finTransaccion))
	    {
	        myFactory.endTransaction(con);
	    }
	    return insertados;
	}
	

	/**
	 * Método para nsertar los datos adicional para la poliza del convenio que lo requiera
	 * @param con
	 * @param idCuenta
	 * @param nombreTitular
	 * @param apellidoTitular
	 * @param tipoId
	 * @param numeroId
	 * @param direccion
	 * @param telefono
	 * @param fechaAutorizacion
	 * @param numeroAutorizacion
	 * @param valorMonto
	 * @param usuario
	 * @return
	 */
	public int ingresarInfoAdicionalPoliza(Connection con, int codigoConvenio, int idCuenta, String nombreTitular, String apellidoTitular, String tipoId, String numeroId, String direccion, String telefono, String fechaAutorizacion, String numeroAutorizacion, int valorMonto, String usuario) 
	{
		return ingresoGeneralDao.ingresarInfoAdicionalPoliza(con, codigoConvenio, idCuenta, nombreTitular, apellidoTitular, tipoId, numeroId, direccion, telefono, fechaAutorizacion, numeroAutorizacion, valorMonto, usuario);
	}
	
	
	/**
	 * Meodo para actualizar los datos del titular de la poliza segun la cuenta
	 * @param con
	 * @param idCuenta
	 * @param nombreTitular
	 * @param apellidoTitular
	 * @param tipoId
	 * @param numeroId
	 * @param direccion
	 * @param telefono
	 * @param usuario
	 * @return
	 */
	public int actualizarInfoTitularPoliza(Connection con,int codigoConvenio, int idCuenta, String nombreTitular, String apellidoTitular, String tipoId, String numeroId, String direccion, String telefono) 
	{
		return ingresoGeneralDao.actualizarInfoTitularPoliza(con, codigoConvenio, idCuenta, nombreTitular, apellidoTitular, tipoId, numeroId, direccion, telefono);
	}
	
	/**
    * Método apra actualizar los datos de la autorizacion de la poliza segun una cuenta dada
    * @param con
    * @param idCuenta
    * @param fechaAutorizacion
    * @param numeroAutorizacion
    * @param valorMonto
    * @return
    */
	public int actualizarDatosAutorizacion(Connection con, int codigoConvenio, int idCuenta, String nombreTitular, String apellidoTitular, String tipoId, String numeroId, String direccion, String telefono, String fechaAutorizacion, String numeroAutorizacion, int valorMonto, String usuario, int codigo, String numeroAutoAux, int montoAux) 
	{
		return ingresoGeneralDao.actualizarDatosAutorizacion(con, codigoConvenio, idCuenta, nombreTitular, apellidoTitular, tipoId, numeroId, direccion, telefono, fechaAutorizacion, numeroAutorizacion, valorMonto, usuario, codigo, numeroAutoAux, montoAux);
	}
	
	
	/**
	 * Método para consultar los datos del titular de la poliza dada una cuenta
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public HashMap consultarInformacionTitularPoliza(Connection con, int idCuenta)
	{
		return ingresoGeneralDao.consultarInformacionTitularPoliza(con, idCuenta);
	}
	/**
	 * Método para actualizar los datos de la poliza
	 * @param con
	 * @param idCuenta
	 * @param codigoConvenio
	 * @param fechaAutorizacion
	 * @param numeroAutorizacion
	 * @param valorMonto
	 * @param usuario
	 * @return
	 */
	public int actualizarInformacionPoliza(Connection con, int idCuenta, int codigoConvenio, String fechaAutorizacion, String numeroAutorizacion, int valorMonto, String usuario, int codigo, String numeroAutoAux, int montoAux) 
	{
		return ingresoGeneralDao.actualizarInformacionPoliza(con, idCuenta, codigoConvenio, fechaAutorizacion, numeroAutorizacion, valorMonto, usuario, codigo, numeroAutoAux, montoAux);
	}
	
	/**
	 * Método para consultar los datos de la autorizacion de la poliza segun una cuenta dada
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public HashMap consultarDatosAutorizacionPoliza(Connection con , int codigoConvenio, int idCuenta)
	{
		return ingresoGeneralDao.consultarDatosAutorizacionPoliza(con, codigoConvenio, idCuenta);
	}
	
	/**
	 * Método para nsertar los datos adicional para la poliza del convenio que lo requiera
	 * @param con
	 * @param idCuenta
	 * @param nombreTitular
	 * @param apellidoTitular
	 * @param tipoId
	 * @param numeroId
	 * @param direccion
	 * @param telefono
	 * @param fechaAutorizacion
	 * @param numeroAutorizacion
	 * @param valorMonto
	 * @param usuario
	 * @return
	 */
	public int ingresarInfoAdicionalPolizaBasica(Connection con, int codigoConvenio, int idCuenta, String fechaAutorizacion, String numeroAutorizacion, int valorMonto, String usuario) 
	{
		return ingresoGeneralDao.ingresarInfoAdicionalPolizaBasica(con, codigoConvenio, idCuenta, fechaAutorizacion, numeroAutorizacion, valorMonto, usuario);
	}
	
	/**
	 * Elmimanr un monto autrorizado
	 * @param con
	 * @param codigo
	 * @return
	 */
	public int eliminarDatosInformacionPoliza(Connection con, int codigo)
	{
		return ingresoGeneralDao.eliminarDatosInformacionPoliza(con, codigo);
	}
	
	
	/**
	 * Este método se encarga de arrancar una transaccion al mejor modo
	 * de estados y demás (sirve si no se tiene un objeto que inicie la transacción)
	 * @param con una conexión abierta con una fuente de datos.
	 * @return
	 * @throws SQLException
	 */
	public int empezarTransaccion (Connection con) throws SQLException 
	{
		return ingresoGeneralDao.empezarTransaccion(con);
	}
	/**
	 * Elimina la propia referencia de este objeto del contexto de sesión. Nótese
	 * que sólo funciona si fue registrado con el nombre "ingreso".
	 * @param pc contexto de la página desde la que se llama este método
	 */
	public void killSelf (PageContext pc) {
		pc.removeAttribute("ingreso", PageContext.SESSION_SCOPE);
	}
	
	/**
	 * Método que carga un ingreso incompleto
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static HashMap<String, Object> cargarIngresoIncompleto(Connection con,String codigoPaciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getIngresoGeneralDao().cargarIngresoIncompleto(con, codigoPaciente);
	}
	
	/**
	 * Metodo para obtener los ingresos para factura odontologica dado un paciente,
	 * en este caso la cuenta puede estar activa y los ingresos en estado abierto o cerrado
	 * 
	 * @param codigoPaciente
	 * @return
	 */
	public static ArrayList<DtoIngresosFactura> obtenerIngresosFacturaOdontologica(int codigoPaciente)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getIngresoGeneralDao().obtenerIngresosFacturaOdontologica(codigoPaciente);
	}
	
	/**
	 * Método que realiza la anulacion de un ingreso que estaba incompleto por garantías
	 * @param con
	 * @param idIngreso
	 * @param codigoViaIngreso
	 * @param idCuenta
	 * @param codigoOrigenAdmision
	 * @param hospitalDia
	 * @param loginUsuario
	 *  
	 * @return
	 */
	public static boolean anulacionIngresoIncompleto(Connection con,String idIngreso,int codigoViaIngreso,String idCuenta,int codigoOrigenAdmision,String hospitalDia, String loginUsuario)
	{
		HashMap campos = new HashMap();
		campos.put("idIngreso", idIngreso);
		campos.put("codigoViaIngreso", codigoViaIngreso+"");
		campos.put("idCuenta", idCuenta);
		campos.put("codigoOrigenAdmision", codigoOrigenAdmision+"");
		campos.put("hospitalDia", hospitalDia);
		campos.put("loginUsuario", loginUsuario);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getIngresoGeneralDao().anulacionIngresoIncompleto(con, campos);
	}
	
	/**
	 * Método que consulta el consecutivo del ingreso
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public static String getConsecutivoIngreso(Connection con,String idIngreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getIngresoGeneralDao().getConsecutivoIngreso(con, idIngreso);
	}

	
	/**
	 * Metodo encargado de consultar si un ingreso fue por
	 * entidades subcontratadas o no.
	 * Adicionado por Jhony Alexander Duque A.
	 * 21/01/2008
	 * @param connection
	 * @param codigoIngreso
	 * @return S/N
	 * 
	 */
	public static String esIngresoComoEntidadSubContratada (Connection connection, int codigoIngreso)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getIngresoGeneralDao().esIngresoComoEntidadSubContratada(connection, codigoIngreso);
	}
	
	/**
	 * Metodo encargado de actualizar el campo transplante de la
	 * tabla ingresos. 
	 * @param connection
	 * @param criterios
	 * --------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------
	 * --transplante --> puede ser (DONANT,RECEPT)
	 * --ingreso
	 * @return true/false
	 */
	private static boolean actualizarTransplante (Connection connection, HashMap criterios)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getIngresoGeneralDao().actualizarTransplante(connection, criterios);
	}
	
	/**
	 * Metodo encargado de actualizar el campo transplante de la
	 * tabla ingresos. 
	 * @param connection
	 * @param criterios
	 * --------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------
	 * --transplante --> puede ser (DONANT,RECEPT)
	 * --ingreso
	 * @return true/false
	 */
	public static boolean actualizarTransplante (Connection connection, String transplante,String ingreso)
	{
		HashMap criterios = new HashMap ();
		criterios.put("transplante", transplante);
		criterios.put("ingreso", ingreso);
		return actualizarTransplante(connection, criterios);
	}
	
	
	/**
	 * Retorna el codigo de la institucion donde se hizo el ingreso.
	 * @return String con el codigo de la institucion 
	 */
	public String getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * Establece el codigo de la institucion donde se hizo el ingreso.
	 * @param codigoInstitucion El codigo de la institucion a establecer
	 */
	public void setCodigoInstitucion(String codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the loginUsuario
	 */
	public String getLoginUsuario() {
		return loginUsuario;
	}

	/**
	 * @param loginUsuario the loginUsuario to set
	 */
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	/**
	 * @return the anioConsecutivo
	 */
	public String getAnioConsecutivo() {
		return anioConsecutivo;
	}

	/**
	 * @param anioConsecutivo the anioConsecutivo to set
	 */
	public void setAnioConsecutivo(String anioConsecutivo) {
		this.anioConsecutivo = anioConsecutivo;
	}

	/**
	 * @return the consecutivo
	 */
	public String getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
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

	/**
	 * @return the pacEntidadSubcontratada
	 */
	public String getPacEntidadSubcontratada() {
		return pacEntidadSubcontratada;
	}

	/**
	 * @param pacEntidadSubcontratada the pacEntidadSubcontratada to set
	 */
	public void setPacEntidadSubcontratada(String pacEntidadSubcontratada) {
		this.pacEntidadSubcontratada = pacEntidadSubcontratada;
	}
	
	
	

}