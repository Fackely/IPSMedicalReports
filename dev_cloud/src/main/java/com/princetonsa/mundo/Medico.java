/*
 * @(#)medico.java
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
import java.util.HashMap;

import util.Answer;
import util.ConstantesBD;
import util.Encoder;
import util.InfoIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.MedicoDao;
import com.princetonsa.mundo.inventarios.UsuariosXAlmacen;
import com.princetonsa.mundo.pooles.MedicosXPool;

import org.apache.log4j.Logger;
/**
 * Esta clase encapsula los atributos y la funcionalidad de un m�dico.
 *
 * @version 1.3, Sep 29, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class Medico extends Persona {
	
	private static Logger logger = Logger.getLogger(Medico.class);
	/**
	 * Especialidades del m�dico.
	 */
	private Especialidades especialidades;

	/**
	 * Numero de registro del m�dico.
	 */
	private String numeroRegistro;
	
	/**
	 * C�digo ciudad de registro
	 */
	private String codigoCiudadRegistro;
	/**
	 * C�digo departamento del registro
	 */
	private String codigoDepartamentoRegistro;
	
	/**
	 * Codigo pais del registro
	 */
	private String codigoPaisRegistro;
	
	/**
	 * Ciudad del registro
	 */
	private String ciudadRegistro;
	/**
	 * Departamento del registro
	 */
	private String departamentoRegistro;
	
	/**
	 * Pais del Registro
	 */
	private String paisRegistro;

	/**
	 * Codigo de la ocupaci�n m�dica del profesional de la salud.
	 */
	private String codigoOcupacionMedica;

	/**
	 * El nombre de la ocupaci�n m�dica del profesional de la salud.
	 */
	private String ocupacionMedica;

	/**
	 * Dia de vinculaci�n del m�dico.
	 */
	private String diaVinculacion;

	/**
	 * Mes de vinculaci�n del m�dico.
	 */
	private String mesVinculacion;

	/**
	 * A�o de vinculaci�n del m�dico.
	 */
	private String anioVinculacion;

	/**
	 * Codigo del tipo de vinculaci�n del m�dico.
	 */
	private String codigoTipoVinculacion;

	/**
	 * Nombre del tipo de vinculaci�n del m�dico.
	 */
	private String tipoVinculacion;

	
	
	/**
	 * Mapa para almacenar los centros de costo del
	 * m�dico
	 */
	private HashMap centrosCosto;
	
	
	/**
	 * Numero de registros en el mapa centrosCosto 
	 */
	private int numCentrosCosto;
	
	private String firmaDigital;
	
	//*******ATRIBUTOS PARA EL MANEJO DE LAS FARMACIAS***********************
	private HashMap farmacias = new HashMap();
	private int numFarmacias;
	
	//******ATRIBUTOS PARA EL MANEJO DE LOS POOLES**************************
	private HashMap pooles = new HashMap();
	private int numPooles ;

	//Atributo convencion 
	private int convencion;

	/**
	 * 
	 */
	private InfoIntegridadDominio tipoLiquidacion;
	
	
	public int getConvencion() {
		return convencion;
	}

	public void setConvencion(int convencion) {
		this.convencion = convencion;
	}

	/**
	 * El DAO usado por el objeto <code>Medico</code> para acceder a la fuente de datos.
	 */
	private static MedicoDao medicoDao = null;

	/**
	 * Constructor vacio, necesario para poder usar esta clase como un JavaBean
	 */
	public Medico () {
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

		if (medicoDao == null) {
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			medicoDao = myFactory.getMedicoDao();
	
		}
	}

	/**
	 * Retorna el codigo de la ocupacionMedica del m�dico.
	 * @return codigo de la ocupacionMedica del m�dico
	 */
	public String getCodigoOcupacionMedica() {
		return codigoOcupacionMedica;
	}

	/**
	 * Retorna la ocupacionMedica del m�dico.
	 * @param encoded especifica si se debe o no retornar esta cadena como <i>character entities</i>
	 * de HTML (e.g., "�" como "&amp;aacute;"
	 * @return la ocupacionMedica del m�dico
	 */
	public String getCategoria(boolean encoded) {
		if (encoded) {
			return getOcupacionMedica();
		}
		else {
			return ocupacionMedica;
		}
	}

	/**
	 * Retorna la ocupacionMedica del m�dico.
	 * (codificado como <i>character entities</i> de HTML, e.g., "�" como "&amp;aacute;") .
	 * @return la ocupacionMedica del m�dico
	 */
	public String getOcupacionMedica() {
		return Encoder.encode(ocupacionMedica);
	}
	
	/**
	 * Retorna el centro de costo del m�dico.
	 * @param encoded especifica si se debe o no retornar esta cadena como <i>character entities</i>
	 * de HTML (e.g., "�" como "&amp;aacute;"
	 * @return el centro de costo del m�dico
	 */
	
	/**
	 * Retorna las especialidades del m�dico.
	 * @return las especialidades del m�dico
	 */
	public Especialidades getEspecialidades() {
		return especialidades;
	}

	/**
	 * Retorna el dia de vinculaci�n del m�dico.
	 * @return el dia de vinculaci�n del m�dico
	 */
	public String getDiaVinculacion() {
		return diaVinculacion;
	}

	/**
	 * Retorna el mes de vinculaci�n del m�dico.
	 * @return el mes de vinculaci�n del m�dico
	 */
	public String getMesVinculacion() {
		return mesVinculacion;
	}

	/**
	 * Retorna el a�o de vinculaci�n del m�dico.
	 * @return el a�o de vinculaci�n del m�dico
	 */
	public String getAnioVinculacion() {
		return anioVinculacion;
	}

	/**
	 * Retorna el codigo del tipo de vinculaci�n del m�dico.
	 * @return el codigo del tipo de vinculaci�n del m�dico
	 */
	public String getCodigoTipoVinculacion() {
		return codigoTipoVinculacion;
	}

	/**
	 * Retorna el tipo de vinculaci�n del m�dico.
	 * @param encoded especifica si se debe o no retornar esta cadena como <i>character entities</i>
	 * de HTML (e.g., "�" como "&amp;aacute;"
	 * @return el tipo de vinculaci�n del m�dico
	 */
	public String getTipoVinculacion(boolean encoded) {
		if (encoded) {
			return getTipoVinculacion();
		}
		else {
			return tipoVinculacion;
		}
	}

	/**
	 * Retorna el tipo de vinculaci�n del m�dico.
	 * (codificado como <i>character entities</i> de HTML, e.g., "�" como "&amp;aacute;") .
	 * @return el tipo de vinculaci�n del m�dico
	 */
	public String getTipoVinculacion() {
		return Encoder.encode(tipoVinculacion);
	}

	/**
	 * Retorna el numero de registro del m�dico.
	 * @return el numero de registro del m�dico
	 */
	public String getNumeroRegistro() {
		return numeroRegistro;
	}

	

	/**
	 * Establece el dia de vinculaci�n del m�dico.
	 * @param diaVinculacion el dia en que el m�dico se vincul�
	 */
	public void setDiaVinculacion(String diaVinculacion) {
		this.diaVinculacion = diaVinculacion;
	}

	/**
	 * Establece el mes de vinculaci�n del m�dico.
	 * @param mesVinculacion el mes en el que el m�dico se vincul�
	 */
	public void setMesVinculacion(String mesVinculacion) {
		this.mesVinculacion = mesVinculacion;
	}

	/**
	 * Establece el a�o de vinculaci�n del m�dico.
	 * @param anioVinculacion el a�o en el que el m�dico se vincul�
	 */
	public void setAnioVinculacion(String anioVinculacion) {
		this.anioVinculacion = anioVinculacion;
	}

	/**
	 * Establece el codigo de la ocupacionMedica.
	 * @param codigoOcupacionMedica el codigo de la ocupacionMedica que va a ser establecido
	 */
	public void setCodigoOcupacionMedica(String codigoCategoria) {
		this.codigoOcupacionMedica = codigoCategoria;
	}

	/**
	 * Establece el codigo del tipo de vinculaci�n.
	 * @param codigoTipoVinculacion el codigo del tipo de vinculaci�n que va a ser establecido
	 */
	public void setCodigoTipoVinculacion(String codigoTipoVinculacion) {
		this.codigoTipoVinculacion = codigoTipoVinculacion;
	}

	/**
	 * Establece el texto de la ocupacionMedica.
	 * @param ocupacionMedica el texto de la ocupacionMedica que va a ser establecido
	 */
	public void setOcupacionMedica(String textoCategoria) {
		this.ocupacionMedica = textoCategoria;
	}

	/**
	 * Establece las especialidades.
	 * @param especialidades las especialidades que van a ser establecidas
	 */
	public void setEspecialidades(Especialidades especialidades) 
	{
		this.especialidades = especialidades;
	}

	/**
	 * Establece el texto del tipo de vinculaci�n.
	 * @param tipoVinculacion el texto del tipo de vinculaci�n que va a ser establecido
	 */
	public void setTipoVinculacion(String textoTipoVinculacion) {
		this.tipoVinculacion = textoTipoVinculacion;
	}

	/**
	 * Establece el numero de registro del m�dico.
	 * @param numeroRegistro el numero de registro que va a ser establecido
	 */
	public void setNumeroRegistro(String numeroRegistro) {
		this.numeroRegistro = numeroRegistro;
	}

	/**
	 * Este metodo inicializa en valores vacios (mas no nulos) los atributos del m�dico.
	 */
	public void clean () {

		super.clean();
		this.especialidades = new Especialidades();
		this.setCodigoOcupacionMedica("");
		this.setOcupacionMedica("");
		this.setDiaVinculacion("");
		this.setMesVinculacion("");
		this.setAnioVinculacion("");
		this.setCodigoTipoVinculacion("");
		this.tipoLiquidacion= new InfoIntegridadDominio();
		this.setTipoVinculacion("");
		this.setNumeroRegistro("");
		
		this.centrosCosto = new HashMap();
		this.numCentrosCosto = 0;
		
		this.farmacias = new HashMap();
		this.numFarmacias = 0;
		this.pooles = new HashMap();
		this.numPooles = 0;
		this.firmaDigital = "";
		this.convencion=ConstantesBD.codigoNuncaValido;

	}

	/**
	 * Este metodo inicializa en valores vacios (mas no nulos) los atributos del m�dico, pero no
	 * limpia el valor del barrio ni el de las especialidades.
	 */
	public void clean2 () {

		super.clean2();
		this.setCodigoOcupacionMedica("");
		this.setOcupacionMedica("");
		this.setDiaVinculacion("");
		this.setMesVinculacion("");
		this.setAnioVinculacion("");
		this.setCodigoTipoVinculacion("");
		this.setTipoVinculacion("");
		this.setNumeroRegistro("");
		this.tipoLiquidacion= new InfoIntegridadDominio();
	}

	/**
	 * Inicializa todas las variables del m�dico que puedan venir en la forma "codigo-nombre".
	 */
	public void inicializarVariables () {

		String [] resultados;

		
		super.inicializarVariables();

		resultados=UtilidadTexto.separarNombresDeCodigos(ocupacionMedica, 1);
		codigoOcupacionMedica=resultados[0];
		ocupacionMedica=resultados[1];

		resultados=UtilidadTexto.separarNombresDeCodigos(tipoVinculacion, 1);
		codigoTipoVinculacion=resultados[0];
		tipoVinculacion=resultados[1];

		/** SE COMENTARI� POR ANEXO 255 - CAMBIO AUTENTICACION DE USUARIO **/
		//resultados=UtilidadTexto.separarNombresDeCodigos(centroCosto, 1);
		//codigoCentroCosto=resultados[0];
		//centroCosto=resultados[1];

	}

	/**
	 * Inserta un m�dico en una fuente de datos, reutilizando una conexion existente, con los datos
	 * presentes en los atributos de este objeto.
	 * @param con una conexion abierta con una fuente de datos
	 * @return n�mero de m�dicos insertados
	 */
	public int insertarMedico (Connection con) throws SQLException {

		setConvencion(UtilidadBD.obtenerSiguienteValorSecuencia(con,"odontologia.seq_convencion_profesional"));
		return medicoDao.insertarMedico(
			con, especialidades, getCodigoOcupacionMedica(), getDiaVinculacion(), getMesVinculacion(), getAnioVinculacion(),
			getCodigoTipoVinculacion(), getNumeroIdentificacion(), getCodigoTipoIdentificacion(),
			getCodigoDepartamentoId(), getCodigoCiudadId(),getCodigoPaisId(),
			getCodigoDepartamentoIdentificacion(),getCodigoCiudadIdentificacion(),getCodigoPaisIdentificacion(),
			getCodigoTipoPersona(), getDiaNacimiento(), getMesNacimiento(), getAnioNacimiento(), getCodigoEstadoCivil(), getCodigoSexo(),
			getPrimerNombrePersona(false), getSegundoNombrePersona(false), getPrimerApellidoPersona(false), getSegundoApellidoPersona(false),
			getDireccion(false), getCodigoDepartamento(), getCodigoCiudad(), getCodigoPais(), getCodigoBarrio(), getTelefono(), getEmail(), getNumeroRegistro(),
			getCodigoDepartamentoRegistro(),getCodigoCiudadRegistro(),getCodigoPaisRegistro(),getFirmaDigital(),getConvencion(), this.getTipoLiquidacion().getAcronimo(),this.getTelefonoFijo(),this.getTelefonoCelular()
			
		);
	}

	/**
	 * Dado un tipo de identificacion y un numero de documento, establece las propiedades de un objeto <code>Medico</code>
	 * en los valores correspondientes.
	 * @param con conexion abierta con una fuente de datos
	 * @param tipoId  Tipo de identificacion del m�dico que se desea cargar
	 * @param numeroId  N�mero de identificacion del m�dico que se desea cargar
	 */
	public void cargarMedico (Connection con, int codigoMedico) throws SQLException {

		this.clean();
		Answer ans= medicoDao.cargarMedico(con,codigoMedico);
		ResultSetDecorator rs = ans.getResultSet();
		
		if (rs.next()) {
			setCodigoPersona(codigoMedico);
			setNumeroIdentificacion(rs.getString("numeroIdentificacion"));

			setTipoIdentificacion(rs.getString("tipoIdentificacion"));
			setCodigoTipoIdentificacion(rs.getString("codigoTipoIdentificacion"));
			setCiudadIdentificacion(rs.getString("ciudadIdentificacion"));
			setCodigoCiudadId(rs.getString("codigoCiudadId"));
			setCodigoDepartamentoId(rs.getString("codigoDeptoId"));
			setCodigoPaisId(rs.getString("codigoPaisId"));
			setCiudadId(rs.getString("nombreCiudadId"));
			setDepartamentoId(rs.getString("nombreDeptoId"));
			setPaisId(rs.getString("nombrePaisId"));
			setCodigoDepartamentoIdentificacion(rs.getString("codDepartamentoIdentificacion"));
			setCodigoCiudadIdentificacion(rs.getString("codigoCiudadIdentificacion"));
			setDepartamentoIdentificacion(rs.getString("departamentoIdentificacion"));
			setPaisIdentificacion(rs.getString("paisIdentificacion"));
			setCodigoPaisIdentificacion(rs.getString("codigoPaisIdentificacion"));
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
			setCiudad(rs.getString("ciudad"));
			setPais(rs.getString("pais"));
			setDepartamento(rs.getString("departamento"));
			setCodigoCiudad(rs.getString("codigoCiudad"));
			setCodigoDepartamento(rs.getString("codigoDepartamento"));
			setCodigoPais(rs.getString("codigoPais"));
			setDireccion(rs.getString("direccion"));
			setBarrio(rs.getString("barrio"));
			setCodigoBarrio(rs.getString("codigoBarrio"));
			
			//Tarea 156583
			if (!rs.getString("telefono").equals(ConstantesBD.codigoNuncaValido+""))
				setTelefono(rs.getString("telefono"));
			else
				setTelefono("");
			
			setEmail(rs.getString("email"));
			setOcupacionMedica(rs.getString("ocupacionMedica"));
			setCodigoOcupacionMedica(rs.getString("codigoOcupacionMedica"));
			especialidades.setEspecialidad(rs.getString("codigoEspecialidad"), rs.getString("especialidad"), rs.getBoolean("activaSistema"));
			setTipoVinculacion(rs.getString("tipoVinculacion"));
			setCodigoTipoVinculacion(rs.getString("codigoTipoVinculacion"));
			String [] fechaVinculacion = UtilidadTexto.separarNombresDeCodigos(rs.getString("fechaVinculacion"), 2);
			setAnioVinculacion(fechaVinculacion[0]);
			setMesVinculacion(fechaVinculacion[1]);
			setDiaVinculacion(fechaVinculacion[2]);
			setNumeroRegistro(rs.getString("numeroRegistro"));
			setCodigoPaisRegistro(rs.getString("codigoPaisRegistro"));
			setCodigoDepartamentoRegistro(rs.getString("codigoDeptoRegistro"));
			setCodigoCiudadRegistro(rs.getString("codigoCiudadRegistro"));
			setCiudadRegistro(rs.getString("nombreCiudadRegistro"));
			setDepartamentoRegistro(rs.getString("nombreDeptoRegistro"));
			setPaisRegistro(rs.getString("nombrePaisRegistro"));
			setFirmaDigital(rs.getString("firmaDigital"));
			setConvencion(rs.getInt("convencion"));
			setTipoLiquidacion(new InfoIntegridadDominio(rs.getString("tipoliquidacion")));
			
			if (!rs.getString("telefonofijo").equals(ConstantesBD.codigoNuncaValido+""))
				setTelefonoFijo(rs.getString("telefonofijo"));
			else
				setTelefonoFijo("");
			if (!rs.getString("telefonocelular").equals(ConstantesBD.codigoNuncaValido+""))
				setTelefonoCelular(rs.getString("telefonocelular"));
			else
				setTelefonoCelular("");
			//Como Oracle no maneja cadenas vac�as
			//en caso de encontrar el campo en nulo
			//lo dejamos en un campo vac�o
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
			especialidades.setEspecialidad(rs.getString("codigoEspecialidad"), rs.getString("especialidad"), rs.getBoolean("activaSistema"));
		}
		rs.close();

	}

	/**
	 * Modifica un m�dico en una fuente de datos, reutilizando una conexion existente, con los datos
	 * presentes en los atributos de este objeto. Notese que el codigo del tipo de identificacion
	 * y el numero de identificacion deben permanecer constantes, cualquier otro dato puede cambiarse.
	 * @param con una conexion abierta con una fuente de datos
	 * @param tipoId tipo de identificacion del m�dico
	 * @param numeroId numero de la identificacion del m�dico
	 * @return n�mero de m�dicos modificados
	 */
	public int modificarMedico (Connection con, int codigoMedico) throws SQLException {
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	
		int resp=0,resp1=super.modificarPersona(con,"empezar",ConstantesBD.tipoPersonaMedico,0);
		
		int resp2 = medicoDao.modificarMedico(
			con, codigoMedico, especialidades, getCodigoOcupacionMedica(), getDiaVinculacion(), getMesVinculacion(), getAnioVinculacion(),
			getCodigoTipoVinculacion(),  getNumeroRegistro(),getCodigoDepartamentoRegistro(),getCodigoCiudadRegistro(), getCodigoPaisRegistro(),getFirmaDigital(),
			getConvencion(), this.getTipoLiquidacion().getAcronimo()
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

	/**
	 * Crea un usuario con base a este m�dico. Por defecto (actualmente)
	 * el usuario se crea con perfil de m�dico.
	 * Despu�s, si el m�dico necesita algun otro rol / permiso puede pedirlo al
	 * administrador. OJO, tengo que tener un m�todo de validacion que me
	 * permita evitar el caso en que un medico selecciona un login ya tomado
	 * @param con una conexion abierta con una fuente de datos
	 * @param tipoBD tipo de la BD usada
	 * @param tipoId tipo de identificacion del m�dico
	 * @param numeroId numero de la identificacion del m�dico
	 * @param login posible login que va a usar el usuario creado
	 * @param password posible password que va a usar el usuario creado
	 * @param codigoInstitucion c�digo de la instituci�n a la que el m�dico pertenece
	 * @return n�mero de usuarios medicos creados (1 si se creo 0 si no)
	 */
	public int crearUsuarioMedico (Connection con, String tipoBD, String tipoId, String numeroId, String login, String password, String codigoInstitucion,String usuarioCreacion) throws SQLException
	{
		int resp=0;

		//A continuaci�n vamos a crear un nuevo usuario
		//con un constructor vacio. Despu�s vamos a llenar los
		//datos necesarios para que usuario se de cuenta que ya
		//existe una persona con ese codigo y que unicamente lo
		//cree.
		
		
		Usuario us=new Usuario();
		us.clean();

		us.setLoginUsuario(login);
		us.setPasswordUsuario(password);

		//Ahora insertamos el perfil por defecto
		String rolesUsuario[]= new String[0];
		//rolesUsuario[0]="medico";
		//ahora se omite el rol del usuario (oid=3134 xplanner2)
		
		

		us.setRolesUsuario(rolesUsuario);

		us.setNumeroIdentificacion(numeroId);
		us.setCodigoTipoIdentificacion(tipoId);
		us.setCodigoInstitucion(codigoInstitucion);
		us.setCentrosCosto(this.getCentrosCosto());
		us.setNumCentrosCosto(this.getNumCentrosCosto());
		
		

		us.setFechaCreacion(UtilidadFecha.getFechaActual());
		us.setHoraCreacion(UtilidadFecha.getHoraActual());
		us.setUsuarioCreacion(usuarioCreacion);
		
		
		us.init(tipoBD);
		resp=us.insertarUsuario(con);
         
		//Como no necesitamos mas este objeto, lo
		//dejamos en null para que el recolector lo
		//libere en su siguiente pasada

		us=null;
		
		return resp;

	}

	
	
	/**
	 * 
	 * @param con
	 * @param codigoMedico
	 * @return
	 */
	public static String obtenerFirmaDigitalMedico(int codigoMedico)
	{
		Connection con= UtilidadBD.abrirConexion();
		String resultado=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMedicoDao().obtenerFirmaDigitalMedico(con, codigoMedico);
		UtilidadBD.closeConnection(con);
		return resultado;
	}
	
	/**
	 * Dados el n�mero y tipo de identificaci�n de un m�dico, lo desactiva
	 *
	 * @param con una conexion abierta con la fuente de datos
	 * @param tipoId codigo de la identificacion del medico
	 * @param numeroId numero de identificacion del medico
	 * @param codigoInstitucion c�digo de la instituci�n de la que se va a
	 * desactivar el m�dico
	 * @return int 1 si pudo desactivar el m�dico, 0 si no pudo desactivarlo
	 */

	public int desactivarMedico (Connection con, String codigoInstitucion) throws SQLException
	{
		return medicoDao.desactivarMedico(con,  this.getCodigoPersona(), codigoInstitucion);
	}

	/**
	 * Dados el n�mero y tipo de identificaci�n de un m�dico, lo activa
	*
	* @param con una conexion abierta con la BD PostgreSQL
	* @param tipoId codigo de la identificacion del medico
	* @param numeroId numero de identificacion del medico
	* @param codigoInstitucion c�digo de la instituci�n de la que se va a
	* desactivar el m�dico
	* @return int 1 si pudo activar el m�dico, 0 si no pudo activarlo
	*/
   public int activarMedico (Connection con,String codigoInstitucion) throws SQLException
   {
		return medicoDao.activarMedico(con,this.getCodigoPersona(), codigoInstitucion);
   }
   
   /**
    * M�todo que inserta las farmacias del profesional de la salud
    * @param con
    * @return
    */
   public boolean insertarFarmacias(Connection con,String loginUsuario,int institucion)
   {
	  boolean exito = true;
	  UtilidadBD.iniciarTransaccion(con);
	  for(int i=0;i<this.numFarmacias;i++)
	  {
		  String[] vector = this.getFarmacias(i+"").toString().split(ConstantesBD.separadorSplit);
		  if(!UsuariosXAlmacen.insertarUsuario(con, Integer.parseInt(vector[0]), loginUsuario, institucion))
		  {
			  exito = false;
			  i = this.numFarmacias;
		  }
	  }
	  
	  if(exito)
		  UtilidadBD.finalizarTransaccion(con);
	  else
		  UtilidadBD.abortarTransaccion(con);
	  return exito;
   }
   
   /**
    * M�todo que inserta los pooles del profesional de la salud
    * @param con
    * @return
    */
   public boolean insertarPooles(Connection con)
   {
	  boolean exito = true;
	  UtilidadBD.iniciarTransaccion(con);
	  for(int i=0;i<this.numPooles;i++)
	  {
		  String[] vector = this.getPooles(i+"").toString().split(ConstantesBD.separadorSplit);
		  MedicosXPool medicoXPool = new MedicosXPool();
		  if(!medicoXPool.insertarDatos(con, UtilidadFecha.getFechaActual(),"", "","", this.getCodigoPersona(), Integer.parseInt(vector[0]), 0))
		  {
			  exito = false;
			  i = this.numPooles;
		  }
	  }
	  
	  if(exito)
		  UtilidadBD.finalizarTransaccion(con);
	  else
		  UtilidadBD.abortarTransaccion(con);
	  return exito;
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
 * @return Retorna un elemento del mapa centrosCosto.
 */
public Object getCentrosCosto(String key) {
	return centrosCosto.get(key);
}

/**
 * @param asigna un elemento al mapa centrosCosto.
 */
public void setCentrosCosto(String key,Object obj) 
{
	this.centrosCosto.put(key,obj);
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
 * @return Returns the ciudadRegistro.
 */
public String getCiudadRegistro() {
	return ciudadRegistro;
}

/**
 * @param ciudadRegistro The ciudadRegistro to set.
 */
public void setCiudadRegistro(String ciudadRegistro) 
{
	this.ciudadRegistro = ciudadRegistro;
}

/**
 * @return Returns the codigoCiudadRegistro.
 */
public String getCodigoCiudadRegistro() 
{
	return codigoCiudadRegistro;
}

/**
 * @param codigoCiudadRegistro The codigoCiudadRegistro to set.
 */
public void setCodigoCiudadRegistro(String codigoCiudadRegistro) 
{
	if(!codigoCiudadRegistro.equals(""))
	{
		String[] vector = codigoCiudadRegistro.split(ConstantesBD.separadorSplit);
		if(vector.length>1)
		{
			this.codigoCiudadRegistro = vector[1];
			this.codigoDepartamentoRegistro = vector[0];
		}
		else
			this.codigoCiudadRegistro = codigoCiudadRegistro;
	}
	else
	{
		this.codigoCiudadRegistro = "";
		this.codigoDepartamentoRegistro = "";
	}
	
}

/**
 * @return Returns the codigoDepartamentoRegistro.
 */
public String getCodigoDepartamentoRegistro() {
	return codigoDepartamentoRegistro;
}

/**
 * @param codigoDepartamentoRegistro The codigoDepartamentoRegistro to set.
 */
public void setCodigoDepartamentoRegistro(String codigoDepartamentoRegistro) {
	this.codigoDepartamentoRegistro = codigoDepartamentoRegistro;
}

/**
 * @return Returns the departamentoRegistro.
 */
public String getDepartamentoRegistro() {
	return departamentoRegistro;
}

/**
 * @param departamentoRegistro The departamentoRegistro to set.
 */
public void setDepartamentoRegistro(String departamentoRegistro) {
	this.departamentoRegistro = departamentoRegistro;
}

/**
 * @return the farmacias
 */
public HashMap getFarmacias() {
	return farmacias;
}

/**
 * @param farmacias the farmacias to set
 */
public void setFarmacias(HashMap farmacias) {
	this.farmacias = farmacias;
}

/**
 * @return Retorna elemento del mapa farmacias
 */
public Object getFarmacias(String key) {
	return farmacias.get(key);
}

/**
 * @param Asigna elemento al mapa farmacias
 */
public void setFarmacias(String key,Object obj) {
	this.farmacias.put(key,obj);
}

/**
 * @return the numFarmacias
 */
public int getNumFarmacias() {
	return numFarmacias;
}

/**
 * @param numFarmacias the numFarmacias to set
 */
public void setNumFarmacias(int numFarmacias) {
	this.numFarmacias = numFarmacias;
}

/**
 * @return the numPooles
 */
public int getNumPooles() {
	return numPooles;
}

/**
 * @param numPooles the numPooles to set
 */
public void setNumPooles(int numPooles) {
	this.numPooles = numPooles;
}

/**
 * @return the pooles
 */
public HashMap getPooles() {
	return pooles;
}

/**
 * @param pooles the pooles to set
 */
public void setPooles(HashMap pooles) {
	this.pooles = pooles;
}

/**
 * @return Retorna elemento del mapa pooles
 */
public Object getPooles(String key) {
	return pooles.get(key);
}

/**
 * @param Asigna elemento al mapa pooles 
 */
public void setPooles(String key,Object obj) {
	this.pooles.put(key,obj);
}

public String getCodigoPaisRegistro() {
	return codigoPaisRegistro;
}

public void setCodigoPaisRegistro(String codigoPaisRegistro) {
	this.codigoPaisRegistro = codigoPaisRegistro;
}

public String getPaisRegistro() {
	return paisRegistro;
}

public void setPaisRegistro(String paisRegistro) {
	this.paisRegistro = paisRegistro;
}

/**
 * @return the firmaDigital
 */
public String getFirmaDigital() {
	return firmaDigital;
}

/**
 * @param firmaDigital the firmaDigital to set
 */
public void setFirmaDigital(String firmaDigital) {
	this.firmaDigital = firmaDigital;
}

/**
 * @return the tipoLiquidacion
 */
public InfoIntegridadDominio getTipoLiquidacion() {
	return tipoLiquidacion;
}

/**
 * @return the tipoLiquidacion
 */
public String getAcronimoTipoLiquidacion() {
	return tipoLiquidacion.getAcronimo();
}

/**
 * @return the nombre
 */
public String getNombreTipoLiquidacionSelect() {
	return UtilidadTexto.isEmpty(this.getTipoLiquidacion().getNombre())?"Seleccione":this.getTipoLiquidacion().getNombre();
}

/**
 * @param tipoLiquidacion the tipoLiquidacion to set
 */
public void setTipoLiquidacion(InfoIntegridadDominio tipoLiquidacion) {
	this.tipoLiquidacion = tipoLiquidacion;
}




/**
 * 
 * @param codigoMedico
 * @return
 */
public static String obtenerTipoLiquidacionPool(int codigoMedico) 
{
	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMedicoDao().obtenerTipoLiquidacionPool(codigoMedico);
}


}