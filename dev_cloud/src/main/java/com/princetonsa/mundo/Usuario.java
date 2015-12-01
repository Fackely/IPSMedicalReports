/*
 * @(#)Usuario.java
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
import com.princetonsa.dto.administracion.DtoPersonaContratoNomina;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.Answer;
import util.ConstantesBD;
import util.Encoder;
import util.UtilidadFecha;
import util.UtilidadTexto;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.UsuarioDao;


/**
 * Esta clase encapsula los atributos y la funcionalidad de un usuario del sistema.
 *
 * @version 1.3, Sep 29, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class Usuario extends Persona {
	/**
	 * Logs para informar erores
	 */
	private static Logger logger = Logger.getLogger(Usuario.class);
	
	/**
	 * El login del usuario en el sistema.
	 */
	private String loginUsuario=null;

	/**
	 * El password del usuario en el sistema.
	 */
	private String passwordUsuario;
	
	/**
	 * 
	 */
	private int diasInactivarUsuario;
	
	
	/**
	 * 
	 */
	private int diasCaducidadPWD;

	/**
	 * El o los roles que tiene el usuario de la aplicacion.
	 */
	private String [] rolesUsuario;

	/**
	 *codigo del cargo de la persona
	 */
	private String codigoCargo="";

	/**
	 * Nombre del cargo de la persona
	 */
	private String cargo="";
	
	/**
	 * Codigo de la instituciï¿½n a la que pertenece el usuario
	 */
	private String codigoInstitucion;

	/**
	 * Nombre de la instituciï¿½n a la que pertenece el usuario
	 */
	private String institucion;
	
	/**
	 * Código del centro de costo del usuario
	 */
	private String codigoCentroCosto;
	
	/**
	 * Nombre del centro de costo del usuario
	 */
	private String centroCosto;
	
	/**
	 * Mapa donde se almacenan los centros de costo del usuario
	 */
	private HashMap centrosCosto = new HashMap();
	
	/**
	 * ARRAYLIST donde se almacena los Contratos
	 */
	private ArrayList<DtoPersonaContratoNomina> listaPersonasContratoNomina; 
	
	
	
	/**
	 * Número de centros de costo del usuario
	 */
	private int numCentrosCosto;
   
	/**
	 * El DAO usado por el objeto <code>Usuario</code> para acceder a la fuente de datos.
	 */
	private static UsuarioDao usuarioDao = null;
	
	
	//Agregado por anexo 958	
	/**
	 * Este determina el contrato a parametrizar a una persona que haya sido parametrizada por itnerfaz sonria con indicativoInterfaz=S 
	 */
	private String contratoInterfaz="";
	

	/**
	 * 
	 */
	private String numeroContrato="";
	
	
	/**
	 * 
	 */
	private String fechaCreacion="";
	

	/**
	 * 
	 */
	private String horaCreacion="";

	/**
	 * 
	 */
	private String usuarioCreacion="";

	/**
	 * 
	 */
	private String fechaUltimaActivacionUsuario="";

	/**
	 * 
	 */
	private String fechaUltimaActivacionPassword="";
	

	/**
	 * 
	 */
	private String fechaUltimaCaducidadPasswd="";

	/**
	 * 
	 */
	private String fechaUltimaInactivacionUsuario="";
	
	
	/**
	 * 
	 */
	private String activo="";
	
	/***
	 * 
	 */
	private String passwdActivo;
	
	
	//Fin anexo 958

	/**
	 * Constructor vacio, necesario para poder usar esta clase como un JavaBean
	 */
	public Usuario () {
		this.clean();
	}

	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores validos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init (String tipoBD) {

		if (usuarioDao == null) {
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			usuarioDao = myFactory.getUsuarioDao();
		}

	}

	/**
	 * Retorna el login del usuario.
	 * @return login del usuario en el sistema
	 */
	public String getLoginUsuario() {
		return loginUsuario;
	}

	/**
	 * Retorna el password del usuario en el sistema.
	 * @return el password del usuario
	 */
	public String getPasswordUsuario() {
		return passwordUsuario;
	}

	/**
	 * Retorna un arreglo con los nombres de los roles del usuario en el sistema.
	 * @param encoded especifica si se debe o no retornar esta cadena como <i>character entities</i>
	 * de HTML (e.g., "ï¿½" como "&amp;aacute;"
	 * @return un arreglo con los roles del usuario en el sistema
	 */
	public String [] getRolesUsuario(boolean encoded) {
		if (encoded) {
			return getRolesUsuario();
		}
		else {
			return rolesUsuario;
		}
	}

	/**
	 * Retorna un arreglo con los nombres de los roles del usuario en el sistema.
	 * (codificado como <i>character entities</i> de HTML, e.g., "ï¿½" como "&amp;aacute;") .
	 * @return un arreglo con los roles del usuario en el sistema
	 */
	public String [] getRolesUsuario () {
		String [] resp = new String [rolesUsuario.length];
		for (int i = 0; i < resp.length; i++) {
			resp[i] = Encoder.encode(rolesUsuario[i]);
		}
		return resp;
	}

	/**
	 * Retorna el tamaño del arreglo de roles.
	 * @return tamaño del arreglo con los codigos de los roles del usuario
	 */
	public int getRolesUsuarioSize() {
		return rolesUsuario.length;
	}

	/**
	 * Retorna el codigo de la institucion a la cual pertenece este usuario.
	 * @return String el codigo de la institucion
	 */
	public String getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * Retorna el nombre de la instituciï¿½n a la cual pertenece el usuario.
	 * @param encoded especifica si se debe o no retornar esta cadena como <i>character entities</i>
	 * de HTML (e.g., "ï¿½" como "&amp;aacute;"
	 * @return la instituciï¿½n a la cual pertenece el usuario
	 */
	public String getInstitucion(boolean encoded) {
		if (encoded) {
			return getInstitucion();
		}
		else {
			return institucion;
		}
	}

	/**
	 * Retorna wl nombre de la instituciï¿½n. (codificado como <i>character
	 * entities</i> de HTML, e.g., "&" como "&amp;aacute;") .
	 * @return la instituciï¿½n
	 */
	public String getInstitucion() {

		return Encoder.encode(institucion);
	
	}

	/**
	 * Establece el login del usuario.
	 * @param loginUsuario el login del usuario que va a establecerse
	 */
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	/**
	 * Establece el password del usuario en el sistema.
	 * @param passwordUsuario el password que se va a establecer
	 */
	public void setPasswordUsuario(String passwordUsuario) {
		this.passwordUsuario = passwordUsuario;
	}

	/**
	 * Establece un arreglo con el o los nombres de el o los roles del usuario.
	 * @param rolesUsuario un arrego de enteros con el o los codigos de los roles del usuario que van a establecerse
	 */
	public void setRolesUsuario(String [] rolesUsuario) {
		this.rolesUsuario = rolesUsuario;
	}

	/**
	 * Establece el codigo de la institucion a la cual pertenece este usuario.
	 * @param codigoInstitucion El codigo de la institucion a establecer
	 */
	public void setCodigoInstitucion(String codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}


	/**
	 * Establece la institucion.
	 * @param institucion La institucion a establecer
	 */
	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}

	/**
	 * Este metodo inicializa en valores vacios (mas no nulos) los atributos del usuario.
	 */
	public void clean () 
	{
		super.clean();
		this.setLoginUsuario("");
		this.setPasswordUsuario("");
		this.setRolesUsuario(new String [0]);
		this.setCodigoInstitucion("");
		this.setCargo("");
		this.setCodigoCargo("");
		codigoCentroCosto="0";
		centroCosto="";
		this.numCentrosCosto = 0;
		this.centrosCosto = new HashMap();
		this.setContratoInterfaz("");
		this.setNumeroContrato("");
		this.diasInactivarUsuario=ConstantesBD.codigoNuncaValido;
		this.diasCaducidadPWD=ConstantesBD.codigoNuncaValido;
		this.setListaPersonasContratoNomina(new ArrayList<DtoPersonaContratoNomina>());
		this.fechaCreacion="";
		this.horaCreacion="";
		this.usuarioCreacion="";
		this.fechaUltimaActivacionUsuario="";
		this.fechaUltimaActivacionPassword="";
		this.activo=ConstantesBD.acronimoSi;
		this.passwdActivo=ConstantesBD.acronimoSi;
		this.fechaUltimaCaducidadPasswd="";
		this.fechaUltimaInactivacionUsuario="";
		
		
	}

	/**
	 * Este metodo inicializa en valores vacios (mas no nulos) los atributos del usuario, pero no
	 * limpia el valor del barrio
	 */
	public void clean2 () 
	{
		super.clean2();
		this.setLoginUsuario("");
		this.setPasswordUsuario("");
		this.setRolesUsuario(new String [0]);
		this.setCodigoInstitucion("");
		this.setCargo("");
		this.setCodigoCargo("");
		codigoCentroCosto="";
		centroCosto="";
		this.numCentrosCosto = 0;
		this.centrosCosto = new HashMap();
		this.setContratoInterfaz("");
		this.setNumeroContrato("");
		this.diasInactivarUsuario=ConstantesBD.codigoNuncaValido;
		this.diasCaducidadPWD=ConstantesBD.codigoNuncaValido;
		this.fechaCreacion="";
		this.horaCreacion="";
		this.usuarioCreacion="";
		this.fechaUltimaActivacionUsuario="";
		this.fechaUltimaActivacionPassword="";
		this.activo=ConstantesBD.acronimoSi;
		this.passwdActivo=ConstantesBD.acronimoSi;
		this.fechaUltimaCaducidadPasswd="";
		this.fechaUltimaInactivacionUsuario="";
	}

	/**
	 * Inicializa todas las variables del usuario que puedan venir en la forma "codigo-nombre".
	 * El tíico dato que viene de esta forma (aparte de los propios de persona)
	 * es el codigo de la instituciï¿½n
	 */
	public void inicializarVariables () 
	{

		super.inicializarVariables();
		String [] resultados;
		   
		resultados=UtilidadTexto.separarNombresDeCodigos(institucion, 1);
		codigoInstitucion=resultados[0];
		institucion=resultados[1];

		/** Se quito por ANEXO 255 - Cambio en autenticación de usuario **/
		//resultados=UtilidadTexto.separarNombresDeCodigos(centroCosto, 1);
		//codigoCentroCosto=resultados[0];
		//centroCosto=resultados[1];
		
	}

	/**
	 * Inserta un usuario en una fuente de datos, reutilizando una conexion existente, con los datos
	 * presentes en los atributos de este objeto.
	 * @param con una conexion abierta con una fuente de datos
	 * @return numero de usuarios insertados
	 */
	public int insertarUsuario (Connection con)  
	{
		String fechaActual=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con));
		String horaActual=UtilidadFecha.getHoraActual(con);
		return usuarioDao.insertarUsuario
		(
			con, getLoginUsuario(), getPasswordUsuario(), getRolesUsuario(false), getNumeroIdentificacion(), getCodigoTipoIdentificacion(),
			getCodigoDepartamentoId(), getCodigoCiudadId(), getCodigoPaisId(),
			getCodigoDepartamentoIdentificacion(),getCodigoCiudadIdentificacion(),getCodigoPaisIdentificacion(), getCodigoTipoPersona(), getDiaNacimiento(), getMesNacimiento(), getAnioNacimiento(),
			getCodigoEstadoCivil(), getCodigoSexo(),getPrimerNombrePersona(false), getSegundoNombrePersona(false), getPrimerApellidoPersona(false),
			getSegundoApellidoPersona(false),getDireccion(false), getCodigoDepartamento(), getCodigoCiudad(), getCodigoPais(),getCodigoBarrio(), getTelefono(), getEmail(),getCodigoCargo(), getCodigoInstitucion(),
			this.getCentrosCosto(),this.getCodigoLocalidad(),this.getIndicativoInterfaz(),this.getContratoInterfaz(),this.getDiasInactivarUsuario(),this.getDiasCaducidadPWD(),this.getUsuarioCreacion(),fechaActual,horaActual,fechaActual,fechaActual
		);
	}

	/**
	 * Dado un login, establece las propiedades de un objeto <code>Usuario</code> en los valores correspondientes.
	 * @param con una conexion abierta con una fuente de datos
	 * @param login login del usuario que se desea cargar
	 */
	public void cargarUsuario (Connection con, String login) throws SQLException {

		this.clean();
		Answer ans = usuarioDao.cargarUsuario(con, login);
		ResultSetDecorator rs = ans.getResultSet();
		this.llenarObjetoResultSet(rs);
		//se carga centros de costo del usuario***************+
		this.cargarCentrosCostoUsuario(con);
		//******************************************************
	}
	
	
	/**
	 * Método que centraliza el llenado de los datos
	 * que vienen de la fuente de datos. Se utiliza
	 * para no repetir código
	 * 
	 * @param rs
	 * @throws SQLException
	 */
	private void llenarObjetoResultSet (ResultSetDecorator rs) throws SQLException
	{
	    
		Vector v = new Vector();
		String posibleRol;

		if (rs.next()) {
			setCodigoPersona(rs.getInt("codigoPersona"));
			setNumeroIdentificacion(rs.getString("numeroIdentificacion"));
			setTipoIdentificacion(rs.getString("tipoIdentificacion"));
			setCodigoTipoIdentificacion(rs.getString("codigoTipoIdentificacion"));
			setPaisIdentificacion(rs.getString("paisIdentificacion"));
			setCiudadIdentificacion(rs.getString("ciudadIdentificacion"));
			setDepartamentoIdentificacion(rs.getString("deptoIdentificacion"));
			setCodigoPaisIdentificacion(rs.getString("codigoPaisIdentificacion"));
			setCodigoDepartamentoIdentificacion(rs.getString("codDepartamentoIdentificacion"));
			setCodigoCiudadIdentificacion(rs.getString("codigoCiudadIdentificacion"));
			setTipoPersona(rs.getString("tipoPersona"));
			setCodigoTipoPersona(rs.getString("codigoTipoPersona"));
			String [] fecha = UtilidadTexto.separarNombresDeCodigos(rs.getString("fechaNacimiento"), 2);
			setAnioNacimiento(fecha[0]);
			setMesNacimiento(fecha[1]);
			setDiaNacimiento(fecha[2]);
			setEstadoCivil(rs.getString("estadoCivil"));
			setCodigoEstadoCivil(rs.getString("codigoEstadoCivil"));
			setSexo(rs.getString("sexo"));
			setCodigoSexo(rs.getString("codigoSexo"));
			setPrimerNombrePersona(rs.getString("primerNombrePersona"));
			setSegundoNombrePersona(rs.getString("segundoNombrePersona"));
			setPrimerApellidoPersona(rs.getString("primerApellidoPersona"));
			setSegundoApellidoPersona(rs.getString("segundoApellidoPersona"));
			setPais(rs.getString("pais"));
			setCodigoPais(rs.getString("codigoPais"));
			setCiudad(rs.getString("ciudad"));
			setDepartamento(rs.getString("departamento"));
			setCodigoCiudad(rs.getString("codigoCiudad"));
			setCodigoDepartamento(rs.getString("codigoDepartamento"));
			setDireccion(rs.getString("direccion"));
			setBarrio(rs.getString("barrio"));
			setCodigoBarrio(rs.getString("codigoBarrio"));
			setTelefono(rs.getString("telefono"));
			setEmail(rs.getString("email"));
			setCodigoCargo(rs.getString("codcargo"));
			setCargo(rs.getString("nomcargo"));
			setLoginUsuario(rs.getString("loginUsuario"));
			setPasswordUsuario(rs.getString("passwordUsuario"));
			setInstitucion(rs.getString("institucion"));
			setCodigoInstitucion(rs.getString("codigoInstitucion"));
			setCodigoPaisId(rs.getString("codigoPaisId"));
			setCodigoDepartamentoId(rs.getString("codigoDeptoId"));
			setCodigoCiudadId(rs.getString("codigoCiudadId"));
			setPaisId(rs.getString("nombrePaisId"));
			setCiudadId(rs.getString("nombreCiudadId"));
			setDepartamentoId(rs.getString("nombreDeptoId"));
			
			posibleRol=rs.getString("rolUsuario");
			
			//Anexo 958
			setIndicativoInterfaz(rs.getString("indicativoInterfaz"));
			setContratoInterfaz(rs.getString("contratoInterfaz"));
			setNumeroContrato(rs.getString("numeroContrato"));
			
			
			setTelefonoCelular(rs.getString("telefonoCelular"));
			setTelefonoFijo(rs.getString("telefonoFijo"));
			

			setDiasInactivarUsuario(rs.getInt("diasInactivarUsuario"));
			setDiasCaducidadPWD(rs.getInt("diasCaducidadPasswd"));
			setFechaCreacion(rs.getString("fechaCreacion"));
			setHoraCreacion(rs.getString("horaCreacion"));
			setUsuarioCreacion(rs.getString("usuarioCreacion"));
			setFechaUltimaActivacionUsuario(rs.getString("fechaUltimaActivacionUsuario"));
			setFechaUltimaActivacionPassword(rs.getString("fechaUltimaActivacionPassword"));
			setActivo(rs.getString("activo"));
			setFechaUltimaCaducidadPasswd(rs.getString("fechaUltimaCaducidadPasswd"));
			setFechaUltimaInactivacionUsuario(rs.getString("fechaUltimaInactivacionUsuario"));
			setPasswdActivo(rs.getString("passwdactivo"));
			
			
			
			if (this.getContratoInterfaz()==null)
				this.setContratoInterfaz("");
			
			if (posibleRol!=null)
			{
				v.add(posibleRol);
			}
			//Como Oracle no maneja cadenas vacías
			//en caso de encontrar el campo en nulo
			//lo dejamos en un campo vacío
			if (this.getSegundoNombrePersona()==null)
			{
			    this.setSegundoNombrePersona("");
			}
			if (this.getSegundoApellidoPersona()==null)
			{
			    this.setSegundoApellidoPersona("");
			}
			if (this.getTelefono()==null)
			{
			    this.setTelefono("");
			}
			if (this.getEmail()==null)
			{
			    this.setEmail("");
			}
			
		}

		while (rs.next()) {
			v.add(rs.getString("rolUsuario"));
		}

		String [] roles = new String[v.size()];
		Iterator i = v.iterator(); int j=0;
		while (i.hasNext()) {
			roles[j] = (String) i.next();
			j++;
		}

		setRolesUsuario(roles);
	}

	/**
	 * Dado la idenficaciï¿½n de un usuario, establece las propiedades de un
	 * objeto <code>Usuario</code> en los valores correspondientes.
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoTipoIdentificacion El cï¿½digo del tipo de la identificaciï¿½n
	 * del usuario
	 * @param numeroIdentificacion El nï¿½mero de identificaciï¿½n del usuario
	 * @throws SQLException
	 */
	public void cargarUsuario (Connection con, String codigoTipoIdentificacion, String numeroIdentificacion) throws SQLException {

		this.clean();
		
		String login=this.buscarLogin(con,codigoTipoIdentificacion, numeroIdentificacion,"");
		
		if (login==null||login.equals(""))
			return;
		
		Answer ans = usuarioDao.cargarUsuario(con, login);
		this.llenarObjetoResultSet(ans.getResultSet());
		//se cargan los centros de costo del usuario**********
		this.cargarCentrosCostoUsuario(con);
		//*****************************************************
	}

	/**
		 * Dado la idenficaciï¿½n de un usuario, establece las propiedades de un
		 * objeto <code>Usuario</code> en los valores correspondientes.
		 * @param con una conexion abierta con una fuente de datos
		 * @param codigoTipoIdentificacion El cï¿½digo del tipo de la identificaciï¿½n
		 * del usuario
		 * @param numeroIdentificacion El nï¿½mero de identificaciï¿½n del usuario
		 * @throws SQLException
		 */

		public void cargarUsuario (Connection con, int codigoMedico) throws SQLException {

			this.clean();
		
			String login=this.buscarLogin(con,codigoMedico);
		
			if (login==null||login.equals(""))
				return;
		
			Answer ans = usuarioDao.cargarUsuario(con, login);
			this.llenarObjetoResultSet(ans.getResultSet());
			//se cargan los centros de costo del usuario**********
			this.cargarCentrosCostoUsuario(con);
			//*****************************************************
		}

		
		/**
		 * Método implementado para cargar los centros de costo de un usuario
		 * @param con
		 */
		private void cargarCentrosCostoUsuario(Connection con)
		{
			/**
			 * Las llaves del mapa son:
			 * codigo_ : codigo del centro de costo
			 * nombre_ : nombre del centro de costo
			 * codigocentroatencion_ :  codigo del centro de atencion
			 * nombrecentroatencion_ :  nombre del centro de atencion
			 */
			this.setCentrosCosto(usuarioDao.cargarCentrosCostoUsuario(con,this.loginUsuario));
			this.setNumCentrosCosto(Integer.parseInt(this.getCentrosCosto("numRegistros").toString()));
		}


	/**
	 * @param con
	 * @param codigoMedico
	 * @return
	 */
	private String buscarLogin(Connection con, int codigoMedico) throws SQLException {
		
		return usuarioDao.buscarLogin(con,codigoMedico);
	}
	
	/**
	 * Método para buscar el login de un usuario de forma estática enviando el tipo de la base de datos
	 * manualmente
	 * @param con
	 * @param codigoMedico
	 * @param tipoBD
	 * @return
	 */
	public static String buscarLoginEstatico(Connection con, int codigoMedico,String tipoBD)
	{
		String login = "";
		
		try
		{
			login = DaoFactory.getDaoFactory(tipoBD).getUsuarioDao().buscarLogin(con, codigoMedico);
		}
		catch(SQLException e)
		{
			logger.error("Error consultando el login del usuario: "+e);
		}
		
		return login;
	}
	
	

	/**
	 * Modifica un usuario en una fuente de datos, reutilizando una conexion existente, con los datos
	 * presentes en los atributos de este objeto. Notese que el login debe permanecer constante, cualquier otro
	 * dato puede cambiarse.
	 * @param con una conexion abierta con una fuente de datos
	 * @param el login del usuario que desea ser eliminado
	 * @return numero de usuarios modificados
	 */
	public int modificarUsuario (Connection con, String login, int codigoPersona,String usuarioModifica) throws SQLException 
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	
		int resp=0,resp1=super.modificarPersona(con,"empezar",ConstantesBD.tipoPersonaUsuario,0);
		int resp2=usuarioDao.modificarUsuario(
		con, getRolesUsuario(false), login, getCodigoCargo(), codigoPersona, getNumeroIdentificacion(), getCodigoTipoIdentificacion(), getCodigoPaisIdentificacion(),getCodigoDepartamentoIdentificacion(),getCodigoCiudadIdentificacion(),
		getCodigoTipoPersona(), getDiaNacimiento(), getMesNacimiento(), getAnioNacimiento(), getCodigoEstadoCivil(), getCodigoSexo(),
		getPrimerNombrePersona(false), getSegundoNombrePersona(false), getPrimerApellidoPersona(false), getSegundoApellidoPersona(false),
		getDireccion(false),getCodigoPais(), getCodigoDepartamento(), getCodigoCiudad(), getCodigoBarrio(), getTelefono(), getEmail(), getCentrosCosto(),this.getContratoInterfaz(),this.getActivo(),getDiasInactivarUsuario(),getDiasCaducidadPWD(),usuarioModifica	);

		if(resp1==1 && resp2==1){
			myFactory.endTransaction(con);
			resp=resp2;
		}else{
			
			myFactory.abortTransaction(con);
			resp=0;
		}

		return resp;
	}
	
	public int insertarUsuarioMismoProfesional(Connection con, String login, String password,String institucion)
	{
			DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			String fechaActual=UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con));
			String horaActual=UtilidadFecha.getHoraActual(con);
			try{
					int resp=0;
					int resp1=super.modificarPersona(con,ConstantesBD.inicioTransaccion,ConstantesBD.tipoPersonaUsuario,0);
					int resp2=usuarioDao.insertarUsuario
					(
							con, login, password, getRolesUsuario(false), getNumeroIdentificacion(), getCodigoTipoIdentificacion(),
							getCodigoDepartamentoId(), getCodigoCiudadId(),getCodigoPaisId(),
							getCodigoDepartamentoIdentificacion(),getCodigoCiudadIdentificacion(), getCodigoPaisIdentificacion(), getCodigoTipoPersona(), getDiaNacimiento(), getMesNacimiento(), getAnioNacimiento(),
							getCodigoEstadoCivil(), getCodigoSexo(),getPrimerNombrePersona(false), getSegundoNombrePersona(false), getPrimerApellidoPersona(false),
							getSegundoApellidoPersona(false),getDireccion(false), getCodigoDepartamento(), getCodigoCiudad(), getCodigoPais(),getCodigoBarrio(), getTelefono(), getEmail(), this.getCodigoCargo(), institucion,
							this.getCentrosCosto(),getCodigoLocalidad(),this.getIndicativoInterfaz(), this.getContratoInterfaz(),this.getDiasInactivarUsuario(),this.getDiasCaducidadPWD(),this.getUsuarioCreacion(),fechaActual,horaActual,fechaActual,fechaActual
						);
					if(resp1==1 && resp2==1){
						myFactory.endTransaction(con);
						resp=resp2;
					}else{
						
						myFactory.abortTransaction(con);
						resp=0;
					}
				
					return resp;
			}
			catch(SQLException e){
				logger.error("Error insertando nuevo usuario del Mismo Profesional en Usuario.java: "+e);
				return -1;
			}
		}
	/**
	 * Lista los roles disponibles en el sistema.
	 * @param con una conexion abierta con la fuente de datos
	 * @return el cï¿½digo en HTML que muestra los roles disponibles del sistema
	 */
	public String listaRoles (Connection con) throws SQLException {
		return usuarioDao.listarRoles(con);
	}

	/**
	 * Lista los roles disponibles en el sistema, con los roles a los que pertenece el usuario elegidos (<i>checked</i>)
	 * @param con una conexion abierta con la fuente de datos
	 * @param rolesUsuario roles a los cuales pertenece el usuario
	 * @return el HTML que muestra los roles disponibles del sistema
	 */
	public String listaRolesElegidos (Connection con) throws SQLException {
		
		return usuarioDao.listarRolesElegidos(con, getRolesUsuario(false));
	}

	 /**
 	  * Mï¿½todo que permite desactivar un usuario dado su login
 	  * @param con una conexion abierta con la fuente de datos
 	  * @param login del usuario que desea ser eliminado
 	  * @param codigoInstitucion cï¿½digo de la instituciï¿½n en la cual se va a desactivar un usuario
	  * @return nï¿½mero de usuarios desactivados
 	  */
	public int desactivarUsuario (Connection con, String login, String codigoInstitucion,String usuActual) throws SQLException
	{
		return usuarioDao.desactivarUsuario (con, login, codigoInstitucion,usuActual) ;
	}

	/**
 	 * Mï¿½todo que permite desactivar un usuario dada su identificaciï¿½n
	 * @param con una conexion abierta con la fuente de datos
	 * @param tipoId  Tipo de identificacion del usuario que se desea desactivar
	 * @param numeroId  Numero de identificacion del usuario que se desea desactivar
	 * @param codigoInstitucion cï¿½digo de la instituciï¿½n en la cual se va a desactivar un usuario
	 * @return nï¿½mero de usuarios desactivados
	 */
	public int desactivarUsuario (Connection con, String tipoId, String numeroId, String codigoInstitucion,String usuActual) throws SQLException
	{
		return usuarioDao.desactivarUsuario (con, tipoId, numeroId, codigoInstitucion,usuActual);
	}

	/**
	 * Mï¿½todo que permite activar un usuario dado su login
	 * @param con una conexion abierta con la fuente de datos
	 * @param login del usuario que desea ser activad
	 * @param codigoInstitucion cï¿½digo de la instituciï¿½n en la cual se va a activar un usuario
	 * @return nï¿½mero de usuarios activados
	 */
	public int activarUsuario (Connection con, String login, String codigoInstitucion,String usuActual) throws SQLException
	{
		return usuarioDao.activarUsuario(con, login, codigoInstitucion,usuActual);
	}

	/**
	 * Mï¿½todo que permite activar un usuario dada su identificaciï¿½n
	 * @param con una conexion abierta con la fuente de datos
	 * @param tipoId  Tipo de identificacion del usuario que se desea activar
	 * @param numeroId  Numero de identificacion del usuario que se desea activar
	 * @param codigoInstitucion cï¿½digo de la instituciï¿½n en la cual se va a activar un usuario
	 * @return nï¿½mero de usuarios activados
	 */
	public  int activarUsuario (Connection con, String tipoId, String numeroId, String codigoInstitucion,String usuActual) throws SQLException
	{
		return usuarioDao.activarUsuario(con, tipoId, numeroId, codigoInstitucion,usuActual);
	}
	
	/**
	 * Método auxiliar, que dados un número y un tipo de identificación retorna
	 * el login respectivo de ese usuario.
	 * @param con una conexión abierta con la BD
	 * @param tipoId tipo de identificación del usuario
	 * @param numeroId número de identificación del usuario
	 * @param activo:
	 * 'true' consultará login activo
	 * 'false' consultará login inactivo
	 * '' no se define si es login activo/inactivo
	 * @return cadena con el login del usuario o <b>null</b> si no existía un usuario con esos datos
	 */
	public String buscarLogin(Connection con, String tipoId, String numeroId,String activo) 
	{
		String login = "";
		
		try 
		{
			login =  usuarioDao.buscarLogin(con,tipoId, numeroId, activo);
		} 
		catch (SQLException e) 
		{
			logger.error("Error al buscarLogin: "+e);
		}
		return login;
	}
	

	/**
	 * @return
	 */
	public String getCentroCosto() {
		return centroCosto;
	}

	/**
	 * @return
	 */
	public String getCodigoCentroCosto() {
		return codigoCentroCosto;
	}

	/**
	 * @param string
	 */
	public void setCentroCosto(String string) {
		centroCosto = string;
	}

	/**
	 * @param string
	 */
	public void setCodigoCentroCosto(String string) {
		codigoCentroCosto = string;
	}

	/**
	 * @return Returns the centrosCosto.
	 */
	public HashMap getCentrosCosto() {
		return centrosCosto;
	}

	/**
	 * @param centrosCosto The centrosCosto to set.
	 */
	public void setCentrosCosto(HashMap centrosCosto) {
		this.centrosCosto = centrosCosto;
	}

	/**
	 * @return Returns the numCentrosCosto.
	 */
	public int getNumCentrosCosto() {
		return numCentrosCosto;
	}

	/**
	 * @param numCentrosCosto The numCentrosCosto to set.
	 */
	public void setNumCentrosCosto(int numCentrosCosto) {
		this.numCentrosCosto = numCentrosCosto;
	}
	
	/**
	 * @return Retorna un elemento del mapa centrosCosto.
	 */
	public Object getCentrosCosto(String key) {
		return centrosCosto.get(key);
	}

	/**
	 * @param Asigna un elemento al mapa centrosCosto.
	 */
	public void setCentrosCosto(String key,Object obj) {
		this.centrosCosto.put(key,obj);
	}

	/**
	 * Devuelve el nombre del cargo que tiene la persona
	 * @return
	 */
	public String getCargo() {
		return cargo;
	}
    
	/**
	 * Actualiza el nombre del cargo de la persona
	 * @param cargo
	 */
	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
    
	/**
	 * Devuelve el codigo del cargo que tiene la persona
	 * @return
	 */
	public String getCodigoCargo() {
		return codigoCargo;
	}
    
	/**
	 * Actualiza el codigo del Cargo que tiene la persona
	 * @param codigoCargo
	 */
	public void setCodigoCargo(String codigoCargo) {
		this.codigoCargo = codigoCargo;
	}

	public String getContratoInterfaz() {
		return contratoInterfaz;
	}

	public void setContratoInterfaz(String contratoInterfaz) {
		this.contratoInterfaz = contratoInterfaz;
	}

	public String getNumeroContrato() {
		return numeroContrato;
	}

	public void setNumeroContrato(String numeroContrato) {
		this.numeroContrato = numeroContrato;
	}

	public void setListaPersonasContratoNomina(
			ArrayList<DtoPersonaContratoNomina> listaPersonasContratoNomina) {
		this.listaPersonasContratoNomina = listaPersonasContratoNomina;
	}

	public ArrayList<DtoPersonaContratoNomina> getListaPersonasContratoNomina() {
		return listaPersonasContratoNomina;
	}

	public int getDiasInactivarUsuario() {
		return diasInactivarUsuario;
	}

	public void setDiasInactivarUsuario(int diasInactivarUsuario) {
		this.diasInactivarUsuario = diasInactivarUsuario;
	}

	public int getDiasCaducidadPWD() {
		return diasCaducidadPWD;
	}

	public void setDiasCaducidadPWD(int diasCaducidadPWD) {
		this.diasCaducidadPWD = diasCaducidadPWD;
	}

	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getHoraCreacion() {
		return horaCreacion;
	}

	public void setHoraCreacion(String horaCreacion) {
		this.horaCreacion = horaCreacion;
	}

	public String getUsuarioCreacion() {
		return usuarioCreacion;
	}

	public void setUsuarioCreacion(String usuarioCreacion) {
		this.usuarioCreacion = usuarioCreacion;
	}

	public String getFechaUltimaActivacionUsuario() {
		return fechaUltimaActivacionUsuario;
	}

	public void setFechaUltimaActivacionUsuario(String fechaUltimaActivacionUsuario) {
		this.fechaUltimaActivacionUsuario = fechaUltimaActivacionUsuario;
	}

	public String getFechaUltimaActivacionPassword() {
		return fechaUltimaActivacionPassword;
	}

	public void setFechaUltimaActivacionPassword(
			String fechaUltimaActivacionPassword) {
		this.fechaUltimaActivacionPassword = fechaUltimaActivacionPassword;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public String getFechaUltimaInactivacionUsuario() {
		return fechaUltimaInactivacionUsuario;
	}

	public void setFechaUltimaInactivacionUsuario(
			String fechaUltimaInactivacionUsuario) {
		this.fechaUltimaInactivacionUsuario = fechaUltimaInactivacionUsuario;
	}

	public String getFechaUltimaCaducidadPasswd() {
		return fechaUltimaCaducidadPasswd;
	}

	public void setFechaUltimaCaducidadPasswd(String fechaUltimaCaducidadPasswd) {
		this.fechaUltimaCaducidadPasswd = fechaUltimaCaducidadPasswd;
	}

	public String getPasswdActivo() {
		return passwdActivo;
	}

	public void setPasswdActivo(String passwdActivo) {
		this.passwdActivo = passwdActivo;
	}
	
	
	
	
	
	
}