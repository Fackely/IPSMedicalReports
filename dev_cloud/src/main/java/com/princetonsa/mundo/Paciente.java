/*
 * @(#)Paciente.java
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import org.apache.log4j.Logger;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.PacienteDao;
import com.princetonsa.dto.interfaz.DtoInterfazPaciente;
import com.princetonsa.dto.manejoPaciente.DtoResponsablePaciente;
import com.princetonsa.dto.odontologia.DtoBeneficiarioPaciente;
import com.princetonsa.dto.odontologia.DtoMotivoCitaPaciente;
import com.princetonsa.mundo.carteraPaciente.DocumentosGarantia;

import util.Answer;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Encoder;
import util.LogsAxioma;
import util.RespuestaInsercionPersona;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadEmail;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.interfaces.ConstantesBDInterfaz;
import util.interfaces.UtilidadBDInterfaz;
import util.manejoPaciente.UtilidadesManejoPaciente;

/**
 * Esta clase encapsula los atributos y la funcionalidad de un paciente.
 *
 * @version 1.0
 * @since Octubre 7, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">Óscar Andrés López P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andrés Camacho P.</a>
 */
public class Paciente extends Persona {

	private Logger logger = Logger.getLogger(Paciente.class);

	/**
	 * Consecutivo de la historia clínica del paciente
	 */
	private String numeroHistoriaClinica = "";
	
	/**
	 * Año del consecutivo de la historia de clínica del paciente
	 */
	private String anioHistoriaClinica = "";
	
	
	/**
	 * Codigo de la zona de domicilio del paciente.
	 */
	private String codigoZonaDomicilio;

	/**
	 * Zona de domicilio del paciente (Rural, Urbana).
	 */
	private String zonaDomicilio;

	/**
	 * Código de la ocupación del paciente.
	 */
	private String codigoOcupacion;

	/**
	 * Tipo de sangre del paciente (Profesión).
	 */
	private String ocupacion;

	/**
	 * Código de la ocupación del paciente.
	 */
	private String codigoTipoSangre;

	/**
	 * Ocupación del paciente (Profesión).
	 */
	private String tipoSangre;

	/**
	 * Foto del paciente (Si es null, toma una por defecto)
	 */

	private String foto;
	
	/**
	 * Código del grupo poblacional
	 */
	private String codigoGrupoPoblacional;
	
	/**
	 * Nombre del grupo poblacional
	 */
	private String nombreGrupoPoblacional;
	
	/**
	 * Código de la religion del paciente
	 */
	private String codigoReligion;
	
	/**
	 * Nombre de la religión del paciente
	 */
	private String nombreReligion;
	
	
	/**
	 * variable para la lee_escribe
	 */
	private Boolean infoHistoSistemaAnt = true;
	
	
	/**
	 * String para las observaciones generales en la sección de mas información en el cabezote
	 */
	private String observacionesGenerales;

	/**
	 * Este String se utiliza para almacenar el nombre de la base de datos
	 * ya que es necesario, al imprimir los datos del paciente tanto de cuenta
	 * como clï¿½nicos, inicializar los daos de estos objetos.
	 * Este String no tiene ni get ni set, no se puede acceder desde afuera,
	 * solo se inicializa al momento de hacer un init (de la bd)
	 */

	private String tipoBD;

	//******************** Datos Paciente Odontología **************
	/**
	 * Arreglo de beneficiarios del paciente odontología
	 */
	private ArrayList<DtoBeneficiarioPaciente> beneficiariosPac;
	
	/**
	 * Motivo de cita del paciente Odontología
	 */
	private DtoMotivoCitaPaciente motivoCitaPaciente;
	
	/**
	 * fecha del motivo cita
	 */
	private String fechaMotivoCita;
	
	/**
	 *  hora del motivo cita
	 */
	private String horaMotivoCita;
	
	/**
	 * observaciones del Motivo cita 
	 */
	private String ObservacionesMotivoCita;
	
	/**
	 * Variable de paciente activo
	 */
	private String activo;
	
	/**
	 *  Convenio Reserva cita
	 */
	private String convenioReserva;
	
	
	private int numeroHijos;
	
	/**
	 * Centro de atencion duenio
	 */
	private int centroAtencionDuenio;
	//*****************************************************************
	
	/**
	 * Mapa donde se almacena los datos Triage del paciente
	 */
	private HashMap datosTriage = new HashMap();

	/**
	 * El DAO usado por el objeto <code>Paciente</code> para acceder a la fuente de datos.
	 */
	private static PacienteDao pacienteDao = null;
	
	private String estadoTransaccion  = "";

	/**
	 * Código del Ingreso del paciente;
	 */
	private int codigoIngreso;
	
	/**
	 * Constructor vacío, necesario para poder usar esta clase como un JavaBean
	 */
	public Paciente () {
		this.init(System.getProperty("TIPOBD"));
		this.clean();
	}

	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores validos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init (String tipoBD) {

		this.tipoBD=tipoBD;
		if (pacienteDao == null) {
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			pacienteDao = myFactory.getPacienteDao();
		}

	}	
	
	/**
	 * Este metodo inicializa en valores vacios (mas no nulos) los atributos del paciente.
	 */
	public void clean () {

		super.clean();
		this.setNumeroHistoriaClinica("");
		this.setAnioHistoriaClinica("");
		this.setCodigoZonaDomicilio("");
		this.setZonaDomicilio("");
		this.setCodigoOcupacion("");
		this.setOcupacion("");
		this.setTipoSangre("");
		this.setCodigoTipoSangre("");
		
		this.setCodigoGrupoPoblacional("");
		this.setNombreGrupoPoblacional("");
		this.setCodigoReligion("");
		this.setNombreReligion("");
		this.setActivo(ConstantesBD.acronimoSi);
		this.setFechaMotivoCita("");
		this.setHoraMotivoCita("");
		this.setConvenioReserva("");
		this.setObservacionesMotivoCita("");
		this.beneficiariosPac= new ArrayList<DtoBeneficiarioPaciente>();
		this.motivoCitaPaciente= new DtoMotivoCitaPaciente();
		this.numeroHijos=0;
		this.estadoTransaccion = ConstantesBD.inicioTransaccion;
		this.centroAtencionDuenio=ConstantesBD.codigoNuncaValido;
		this.setCodigoIngreso(ConstantesBD.codigoNuncaValido);
	}

	/**
	 * Este metodo inicializa en valores vacios (mas no nulos) los atributos del paciente, pero no
	 * limpia el valor del barrio.
	 */
	public void clean2 () {

		super.clean2();
		/*Se comentan estas líneas porque se estaba borrando el número de la historia clinica en la modificación
		 * y se cambiaba por un número diferente cada vez*/
		//this.setNumeroHistoriaClinica("");
		//this.setAnioHistoriaClinica("");
		this.setCodigoZonaDomicilio("");
		this.setZonaDomicilio("");
		this.setCodigoOcupacion("");
		this.setOcupacion("");
		this.setTipoSangre("");
		this.setCodigoTipoSangre("");
		this.setCodigoGrupoPoblacional("");
		this.setNombreGrupoPoblacional("");
		this.setCodigoReligion("");
		this.setNombreReligion("");
	}

	/**
	 * Inicializa todas las variables del paciente que puedan venir en la forma "codigo-nombre".
	 */
	public void inicializarVariables () {

		String [] resultados;

		super.inicializarVariables();

		resultados = UtilidadTexto.separarNombresDeCodigos(zonaDomicilio, 1);
		codigoZonaDomicilio = resultados[0];
		zonaDomicilio = resultados[1];

		resultados = UtilidadTexto.separarNombresDeCodigos(ocupacion, 1);
		codigoOcupacion = resultados[0];
		ocupacion = resultados[1];

		resultados = UtilidadTexto.separarNombresDeCodigos(tipoSangre, 1);
		codigoTipoSangre = resultados[0];
		tipoSangre = resultados[1];
	}

	/**
	 * Inserta un paciente en una fuente de datos, reutilizando una conexion existente, con los datos
	 * presentes en los atributos de este objeto.
	 * @param con una conexion abierta con una fuente de datos
	 * @return numero de pacientes insertados
	 */
	public RespuestaInsercionPersona insertarPaciente (Connection con,int codigoInstitucion) throws SQLException {
		
		RespuestaInsercionPersona temp=pacienteDao.insertarPaciente
		(
			con,
			getNumeroHistoriaClinica(), getAnioHistoriaClinica(),
			getCodigoZonaDomicilio(), getCodigoOcupacion(),
			getNumeroIdentificacion(), getCodigoTipoIdentificacion(),
			getCodigoDepartamentoId(),getCodigoCiudadId(),
			getCodigoDepartamentoIdentificacion(),
			getCodigoCiudadIdentificacion(), getCodigoTipoPersona(), getDiaNacimiento(), getMesNacimiento(), getAnioNacimiento(), getCodigoEstadoCivil(),
			getCodigoSexo(), getPrimerNombrePersona(false), getSegundoNombrePersona(false), getPrimerApellidoPersona(false), getSegundoApellidoPersona(false),
			getDireccion(false), getCodigoDepartamento(), getCodigoCiudad(), getCodigoBarrio(), getTelefono(), getEmail(), getCodigoTipoSangre(),
			foto, codigoInstitucion, getCentro_atencion(), getEtnia(), getLee_escribe(), getEstudio(), getCodigoGrupoPoblacional(),
			getCodigoPaisId(), getCodigoPaisIdentificacion(), getCodigoPais(), getCodigoLocalidad(), getCodigoReligion(), getInfoHistoSistemaAnt(), getTelefonoCelular(), getTelefonoFijo(),
			getFechaMotivoCita(),getHoraMotivoCita(),getObservacionesMotivoCita(),getActivo(),getConvenioReserva(), getNumeroHijos()
		);
		return temp;
	}

	/**
	 * Inserta un paciente en una fuente de datos, reutilizando una conexion existente, con los datos
	 * presentes en los atributos de este objeto. Este metodo recibe ademas otro parametro
	 * llamado correo origen, donde se especifica de quien se va a enviar el correo para
	 * que el usuario puede crear una cuenta como usuario. En el futuro y por medio de una
	 * clase auxiliar se accedera a un archivo html/xml donde se consultara la informacion
	 * a enviar (Formato etc. ) por ahora esta cableada la informaciï¿½n desde acï¿½
	 * @param con una conexion abierta con una fuente de datos
	 * @param maquinaSmtp Mï¿½quina de correo a utilizar
	 * @param correoOrigen Un campo de texto donde se especifica a nombre de quien saldra el correo que identifica a quien envio el mensaje
	 * @param codigoInstitucion cï¿½digo de la instituciï¿½n donde se va a insertar al paciente
	 * @return numero de pacientes insertados
	 */
	public RespuestaInsercionPersona insertarPaciente (Connection con, String correoOrigen, String codigoInstitucion) throws SQLException{


		RespuestaInsercionPersona res = insertarPaciente(con,Integer.parseInt(codigoInstitucion));
		
		//En este punto ya sabemos que se pudo o no insertar bien
		//(ya sea que salio una excepciï¿½n o si saliï¿½ un resultado
		//en 0. Es en este punto donde envio el correo pidiendole
		//la activacion si no es nulo o vacio
		String email=getEmail();
		try
		{
			if (res!=null&&email!=null&&!email.equals(""))
			{
				
				String direccion = System.getProperty("com.princetonsa.axioma.webPath") + "/activacion/activacion.jsp?primeraVez=true&codigoInstitucion=" + codigoInstitucion + "&numeroIdentificacionPaciente=" + getNumeroIdentificacion() + "&tipoIdentificacionPaciente=" + getCodigoTipoIdentificacion() + "&email=" + email;
				
				String link="<A HREF=\"" + direccion + "\">link</A>";
				UtilidadEmail.enviarCorreo(correoOrigen, email, "Bienvenido a Axioma. Para permitirle un mejor acceso a su información clínica, lo invitamos a que se registre como usuario en nuestro sistema.<BR> Haga click en el siguiente " + link +". Si no tiene un navegador gráfico, por favor copie la siguiente dirección y péguela en su navegador favorito:<br>" + direccion , "Activación de Cuenta en Sistema de Salud");
				
			}
		}
		catch (Exception e)
		{
		    logger.error("No fué posible enviar el correo electrónico " + e.toString());
		}
		return res;
	}


	/**
	 * Dado un tipo de identificacion y un numero de documento, establece las propiedades de un objeto <code>Paciente</code>
	 * en los valores correspondientes.
	 * @param con una conexion abierta con una fuente de datos
	 * @param tipoId  Tipo de identificacion del paciente que se desea cargar
	 * @param numeroId  Numero de identificacion del paciente que se desea cargar
	 */
	public void cargarPaciente(Connection con, int codigoPaciente) throws SQLException
	{
		this.clean();
		
		Answer ans = pacienteDao.cargarPaciente(con, codigoPaciente);
		ResultSetDecorator rs = ans.getResultSet();
		try {
		if (rs.next())
		{
			setCodigoPersona(codigoPaciente);
			setNumeroIdentificacion(rs.getString("numeroIdentificacion"));
			setTipoIdentificacion(rs.getString("tipoIdentificacion"));
			setCodigoTipoIdentificacion(rs.getString("codigoTipoIdentificacion"));
			setCiudadIdentificacion(rs.getString("ciudadIdentificacion"));
			setCodigoDepartamentoIdentificacion(rs.getString("codDepartamentoIdentificacion"));
			setCodigoCiudadIdentificacion(rs.getString("codigoCiudadIdentificacion"));
			setCodigoPaisIdentificacion(rs.getString("codigoPaisIdentificacion"));
			setPaisIdentificacion(rs.getString("paisIdentificacion"));
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
			setCodigoCiudad(rs.getString("codigoCiudad"));
			setCodigoDepartamento(rs.getString("codigoDepartamento"));
			setCodigoPais(rs.getString("codigoPais"));
			setPais(rs.getString("pais"));
			setDireccion(rs.getString("direccion"));
			setBarrio(rs.getString("barrio"));
			setCodigoBarrio(rs.getString("codigoBarrio"));
			setNumeroHijos(rs.getInt("nrohijos"));
			setTipoidenAcronimo(rs.getString("acronimoTipoIden"));
			setCodigoIngreso(rs.getInt("codigoIngreso"));
			
			
			String codigoLocalidad = rs.getString("codigoLocalidad");
			
			if(codigoLocalidad==null)
			{
				codigoLocalidad = "";
			}
			setCodigoLocalidad(codigoLocalidad);
			String localidad = rs.getString("localidad");
			if(localidad==null)
			{
				localidad = "";
			}
			setLocalidad(localidad);
			setTelefono(rs.getString("telefono"));
			if(rs.getLong("telefono_fijo")!=ConstantesBD.codigoNuncaValido)
				setTelefonoFijo(rs.getString("telefono_fijo")+"");
			if(rs.getLong("telefono_celular")!=ConstantesBD.codigoNuncaValido)
				setTelefonoCelular(rs.getString("telefono_celular"));
			setEmail(rs.getString("email"));
			setCodigoZonaDomicilio(rs.getString("codigoZonaDomicilio"));
			setZonaDomicilio(rs.getString("zonaDomicilio"));
			setCodigoOcupacion(rs.getString("codigoOcupacion"));
			setOcupacion(rs.getString("ocupacion"));
			setCodigoTipoSangre(rs.getString("codigoTipoSangre"));
			setTipoSangre(rs.getString("tipoSangre"));
			setDepartamento(rs.getString("departamento"));
			setDepartamentoIdentificacion(rs.getString("departamentoIdentificacion"));
			setFoto(rs.getString("foto"));
			
			setCentro_atencion(rs.getInt("centro_atencion"));
			setNombreCentroAtencion(rs.getString("nombre_centro_atencion"));
			setEtnia(rs.getInt("etnia"));
			setNombreEtnia(rs.getString("nombre_etnia"));
			setEstudio(rs.getInt("estudio"));
			setNombreEstudio(rs.getString("nombre_estudio"));
			setLee_escribe(rs.getBoolean("lee_escribe"));
			setCodigoCiudadId(rs.getString("codigoCiudadId"));
			setCodigoDepartamentoId(rs.getString("codigoDeptoId"));
			setCiudadId(rs.getString("nombreCiudadId"));
			setDepartamentoId(rs.getString("nombreDeptoId"));
			setCodigoPaisId(rs.getString("codigoPaisId"));
			setPaisId(rs.getString("nombrePaisId"));
			setCodigoGrupoPoblacional(rs.getString("grupoPoblacional"));
			setNombreGrupoPoblacional(ValoresPorDefecto.getIntegridadDominio(getCodigoGrupoPoblacional())+"");
			setNumeroHistoriaClinica(rs.getString("historiaClinica"));
			setAnioHistoriaClinica(rs.getString("anioHistoriaClinica"));
			setCodigoReligion(rs.getString("codigoReligion"));
			setNombreReligion(rs.getString("religion"));
			setInfoHistoSistemaAnt(rs.getBoolean("histo_sistema_anterior"));
			
			if(rs.getString("fechamotivocita")==null)
				this.setFechaMotivoCita("");
			else
			     setFechaMotivoCita(rs.getString("fechamotivocita"));
			
			if(rs.getString("horamotivocita")==null)
			    this.setHoraMotivoCita("");
			else
			     setHoraMotivoCita(rs.getString("horamotivocita"));
			
			if(rs.getString("obsmotivocita")==null)
				this.setObservacionesMotivoCita("");
			else
				setObservacionesMotivoCita(rs.getString("obsmotivocita"));
			
			if(rs.getString("activopaciente")==null)
				this.setActivo("");
			else
				setActivo(rs.getString("activopaciente"));
			
			if(rs.getString("convenioreserva")==null)
				this.setConvenioReserva("");
			else
				setConvenioReserva(rs.getString("convenioreserva"));
			
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
		} catch (Exception e) {
					
				}finally{
					if(rs!=null){
						try{
							rs.close();
							
						}catch(Exception exception){
							
						}
					}
				}

	}

	/**
	* Modifica un paciente en una fuente de datos, reutilizando una conexion existente, con los datos
	* presentes en los atributos de este objeto. Notese que el codigo del tipo de identificacion
	* y el numero de identificacion deben permanecer constantes, cualquier otro dato puede cambiarse.
	* @param ac_con		Conexión con una fuente de datos
	* @param tipoId  Tipo de identificacion del paciente que se desea cargar
	* @param numeroId  Numero de identificacion del paciente que se desea cargar
	* @return	1	Si la modificaciòn de la persona fue exitoso
	*			0	Error de base de datos
	*			-1	Si la combinación tipo/número de documento ya existe
	*			-2	Si la fecha de nacimiento es posterior a la actual
	*			-3	La conexión a base de datos no válida
	*			-5  problema al modificar en la interfaz
	*/
	public int modificarPaciente(Connection ac_con,UsuarioBasico usuario, Paciente pacienteAnterior)throws SQLException
	{
		int li_resp;
		
		//Se verifica si el paciente tenía numero de historia clínica
		//de lo cotnrario se le asigna uno
		if(this.numeroHistoriaClinica==null||this.getNumeroHistoriaClinica().equals("")||this.getNumeroHistoriaClinica().equals(ConstantesBD.codigoNuncaValido+""))
		{
			this.setNumeroHistoriaClinica(UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoHistoriaClinica, usuario.getCodigoInstitucionInt()));
			this.setAnioHistoriaClinica(UtilidadBD.obtenerAnioConsecutivo(ConstantesBD.nombreConsecutivoHistoriaClinica, usuario.getCodigoInstitucionInt(), this.getNumeroHistoriaClinica()));
		}
		
		logger.info("ENTRÉ A MODIFICAR PACIENTE!!!!!!!")	;
		//MODIFICACION DE LA PERSONA
		if( (li_resp = super.modificarPersona(ac_con, this.estadoTransaccion,ConstantesBD.tipoPersonaPaciente,usuario.getCodigoInstitucionInt()) ) == 1)
		{
			logger.info("MODIFIQUÉ EL PACIENTE!!!!");
			
			//MANEJO DE LA INTERFAZ DE TESORERIOA
			if(this.manejoInterfazTesoreria(usuario.getLoginUsuario(), usuario.getCodigoInstitucionInt()).isTrue())
				li_resp = 1;
			else
				li_resp = -5;
			
			logger.info("EXITO EN EL MANEJO DE INTERFAZ!!! "+li_resp);
			logger.info(" ACTIVO PACIENTE!!! "+getActivo());
			
			if(li_resp>0)
			{
				//MODIFICACION DEL PACIENTE
				li_resp = pacienteDao.modificarPaciente(
						ac_con,
						getCodigoPersona(),
						getCodigoOcupacion(),
						getCodigoTipoSangre(),
						getCodigoZonaDomicilio(),
						getFoto(),
						getCentro_atencion(),
						getEtnia(),
						getLee_escribe(),
						getEstudio(),
						getCodigoGrupoPoblacional(),
						getCodigoReligion(), 
						getInfoHistoSistemaAnt(),
						getNumeroHistoriaClinica(),
						getAnioHistoriaClinica(),
						getFechaMotivoCita(),
						getHoraMotivoCita(),
						getObservacionesMotivoCita(),
						getActivo(),
						getConvenioReserva(),
						getNumeroHijos()
					);	
				logger.info("MODIFICÓ EL PACIENTE? "+li_resp);
				
				//GENERACIOIN DEL LOG Y ACTUALIZACION DE DATOS DEL DEUDOR
				if(li_resp>0)
				{
					if(this.generarLog(ac_con, usuario, pacienteAnterior))
						li_resp = 1;
					else
						li_resp = 0;
					
					logger.info("SE GENERÓ EXITOSAMENTE EL LOG!!! ????? "+li_resp);
				}
			}
		}
		
		if(this.estadoTransaccion.equals(ConstantesBD.inicioTransaccion))
		{
			this.estadoTransaccion = ConstantesBD.finTransaccion;
		}
		
		
		if(this.estadoTransaccion.equals(ConstantesBD.finTransaccion))
		{
			if(li_resp>0)
				UtilidadBD.finalizarTransaccion(ac_con);
			else
				UtilidadBD.abortarTransaccion(ac_con);
		}

		return li_resp;
	}

	
	/**
	 * Metodo para insertar Motivo de cita paciente Odonotlogia
	 * @param con
	 * @param motivoCitaPaciente
	 * @return
	 */
	public static int insertarMotivoCitaPacOdontologia(Connection con, DtoMotivoCitaPaciente motivoCitaPaciente)
	{			
		return pacienteDao.insertarMotivoCitaPacOdontologia(con, motivoCitaPaciente);
		
	}
	
	/**
	 * Metodo para modificar Motivo Cita paciente odonotologia
	 * @param con
	 * @param motivoCitaPaciente
	 * @return
	 */
	public static int modificarMotivoCitaPacOdontologia(Connection con, DtoMotivoCitaPaciente motivoCitaPaciente)
	{			
		return pacienteDao.modificarMotivoCitaPacOdontologia(con, motivoCitaPaciente);
		
	}
	
	/**
	 * Metodo para consultar el motivo de cita de un paciente
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public static DtoMotivoCitaPaciente consultarMotivoCitaPacOdontologia(Connection con, int codigoPaciente)
	{			
		return pacienteDao.consultarMotivoCitaPacOdontologia(con, codigoPaciente);
		
	}
	
	
	/**
	 * Metodo para insertar Beneficiario Paciente
	 * @param con
	 * @param beneficiario
	 * @param loginUsuario
	 * @return
	 */
	public static int insertarBeneficiarioPaciente(Connection con, DtoBeneficiarioPaciente beneficiario, String loginUsuario, int codInstitucion)
	{
		return pacienteDao.insertarBeneficiarioPaciente(con,beneficiario, loginUsuario, codInstitucion);
	}
	
	
	public static int modificarBeneficiarioPaciente(Connection con, DtoBeneficiarioPaciente beneficiario, String loginUsuario, int codInstitucion)throws SQLException
	{
		return pacienteDao.modificarBeneficiarioPaciente(con, beneficiario,loginUsuario, codInstitucion);	
	}
	
	/**
	 * 
	 */
	public ArrayList<DtoBeneficiarioPaciente> consultarBenficiariosPaciente	(Connection con, int codigo) {
		
		return pacienteDao.consultarBeneficiariosPaciente(con, codigo);
	}
	
	/**
	 * Crea un usuario con base a este paciente. Por defecto
	 * el usuario se crea con perfil de paciente.
	 * Despuï¿½s, si el paciente necesita algun otro rol / permiso puede pedirlo al
	 * administrador. OJO, tengo que tener un mï¿½todo de validacion que me
	 * permita evitar el caso en que un paciente selecciona un login ya tomado
	 * @param con una conexion abierta con una fuente de datos
	 * @param tipoBD tipo de la BD usada
	 * @param tipoId tipo de identificaciï¿½n del paciente
	 * @param numeroId numero de la identificaciï¿½n del paciente
	 * @param login posible login que va a usar el usuario creado
	 * @param password posible password que va a usar el usuario creado
	 * @param codigoInstitucion cï¿½digo de la instituciï¿½n a la que el paciente pertenece
	 * @return nï¿½mero de usuarios pacientes creados (1 si se creo 0 si no)
	 */
	public static int crearUsuarioPaciente (Connection con, String tipoBD, String tipoId, String numeroId, String login, String password, String codigoInstitucion) throws SQLException
	{
		int resp=0;

		//A continuaciï¿½n vamos a crear un nuevo usuario
		//con un constructor vacio. Despuï¿½s vamos a llenar los
		//mï¿½nimos datos necesarios para poder insertar un
		//usuario
		Usuario us=new Usuario();
		us.clean();

		us.setLoginUsuario(login);
		us.setPasswordUsuario(password);

		//Ahora insertamos el perfil por defecto
		String rolesUsuario[]= new String[1];
		rolesUsuario[0]="paciente";

		us.setRolesUsuario(rolesUsuario);

		us.setNumeroIdentificacion(numeroId);
		us.setCodigoTipoIdentificacion(tipoId);
		us.setCodigoInstitucion(codigoInstitucion);
		us.init(tipoBD);

		//Se debe insertar con un centro de costo
		//válido, sin embargo el paciente no lo debe
		//ver así  que utilizamos a UtilidadValidacion

		us.setCodigoCentroCosto((UtilidadValidacion.existeCentroCostoValido(con)).textoRespuesta);
		resp=us.insertarUsuario(con);

		//Como no necesitamos mas este objeto, lo
		//dejamos en null para que el recolector lo
		//libere en su siguiente pasada

		us=null;

		return resp;

	}

	public Vector toHtmlFacturacion (Connection con, String codigoInstitucion) throws SQLException
	{
		Vector aImprimir= new Vector(50,10);

		//Lo primero que se debe hacer es validar que el usuario pueda ver este paciente
		//esta validaciï¿½n no se pone en el JSP pues no es una restricciï¿½n de funcionalidad,
		//sino de seguridad

		if (!UtilidadValidacion.puedoImprimirPaciente(con, this.getCodigoTipoIdentificacion(), this.getNumeroIdentificacion(), codigoInstitucion))
		{
			aImprimir.add("Este Paciente no existe y/o su instituci&oacute;n no tiene acceso al mismo");
			return aImprimir;
		}
		String meses[]={"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

		//Empezamos a dibujar la tabla
		aImprimir.add("<table width=\"100%\" border=\"1\" cellpadding=\"2\" cellspacing=\"1\"><tr><td align=\"center\" valign=\"top\"><p>&nbsp;</p><p>&nbsp;");
		aImprimir.add("</p><p>Tel&eacute;fono - ");

		String telefono=this.getTelefono();
		if (telefono==null||telefono.equals(""))
		{
			aImprimir.add("No disponible");
		}
		else
		{
			aImprimir.add( telefono );
		}
		aImprimir.add("</p><p>Email - ");

		String email=this.getEmail();
		if (email==null||email.equals(""))
		{
			aImprimir.add("No disponible</p>");
		}
		else
		{
			aImprimir.add( this.getEmail() + "</p>");
		}

		aImprimir.add("</td><td valign=\"top\"><p>&nbsp;</p><p align=\"center\"><b><font size=\"+1\">");
		aImprimir.add(this.getPrimerNombrePersona() + " " + this.getSegundoNombrePersona() + " " + this.getPrimerApellidoPersona() + " " + this.getSegundoApellidoPersona());

		aImprimir.add("</font></b></p><dl><dt><div class=\"ch\">Identificaci&oacute;n:</div></dt><dd><div class=\"ch\">");
		aImprimir.add(this.getTipoIdentificacion());
		aImprimir.add("</div></dd><dd>");
		aImprimir.add(this.getNumeroIdentificacion());

		aImprimir.add("</dd><dt>Fecha y Lugar de Nacimiento:</dt><dd>");
		aImprimir.add(this.getDiaNacimiento() + " " + meses[ Integer.parseInt(this.getMesNacimiento()) - 1] + " " + this.getAnioNacimiento()  + " (" + this.getEdad() + ")");

		aImprimir.add("<dd><div class=\"ch\">");
		aImprimir.add(UtilidadTexto.fraseAEstandar( this.getCiudadIdentificacion() ) + ", " + UtilidadTexto.fraseAEstandar( this.getDepartamentoIdentificacion() )  );

		aImprimir.add("</div></dd><dt>Datos Generales:</dt><dd>Estado Civil - ");
		aImprimir.add( this.getEstadoCivil() );

		aImprimir.add( "/a</dd><dd>Tipo de Sangre - ");
		aImprimir.add(tipoSangre);

		aImprimir.add("</dd><dd>Sexo - ");
		aImprimir.add(this.getSexo());

		aImprimir.add("</dd><dt>Lugar de Residencia:</dt><dd>");
		aImprimir.add(this.getDireccion());

		aImprimir.add("</dd><dd>");

		String barrio=this.getBarrio();
		String temp[]=barrio.split("-");
		barrio=UtilidadTexto.fraseAEstandar(temp[1]);
		if (temp[0].charAt(0)=='0')
		{
			aImprimir.add(UtilidadTexto.fraseAEstandar(this.getCiudad()) + ", " +  UtilidadTexto.fraseAEstandar(this.getDepartamento()) );
		}
		else
		{
			aImprimir.add(barrio + ",  " + UtilidadTexto.fraseAEstandar(this.getCiudad() ) + ", " +  UtilidadTexto.fraseAEstandar( this.getDepartamento()) );
		}

		aImprimir.add("</dd><dt>Contacto:</dt><dd>Telefono -");

		//Declaramos una variable para no tener que cargar el mï¿½todo tres
		//veces (revisar si es null, si es vacio e imprimir)
		if (telefono==null||telefono.equals(""))
		{
			aImprimir.add("No disponible");
		}
		else
		{
			aImprimir.add( telefono );
		}

		aImprimir.add("</dd><dd>Email - ");
		if (email==null||email.equals(""))
		{
			aImprimir.add("No disponible");
		}
		else
		{
			aImprimir.add( this.getEmail() );
		}
		aImprimir.add("</dd></dl></td></tr>");

		//Sacamos la lista de CuentasPaciente

		CuentasPaciente cuentasPaciente=new CuentasPaciente(this.getCodigoPersona(), codigoInstitucion);
		String prueba[]=cuentasPaciente.cargarCuentasPaciente(con, tipoBD);
		if (prueba!=null)
		{
			aImprimir.add("<tr><td align=\"center\" valign=\"top\">Cuentas</td><td valign=\"top\">");

			Cuenta cuenta;

			for (int i=0;i<prueba.length;i++)
			{
				cuenta= new Cuenta();
				cuenta.init(tipoBD);
				cuenta.cargarCuenta(con,prueba[i]);
				aImprimir.addAll(cuenta.toHtml());
			}
			aImprimir.add("<p>&nbsp;</p></td></tr>");

		}


		aImprimir.add("</table>");

		return aImprimir;
	}

	public Vector toHtmlFacturacion2 (Connection con, String codigoInstitucion, int cuentasImprimir[]) throws SQLException
	{
		Vector aImprimir= new Vector(50,10);
		//Lo primero que se debe hacer es validar que el usuario pueda ver este paciente
		//esta validaciï¿½n no se pone en el JSP pues no es una restricciï¿½n de funcionalidad,
		//sino de seguridad

		if (!UtilidadValidacion.puedoImprimirPaciente(con, this.getCodigoTipoIdentificacion(), this.getNumeroIdentificacion(), codigoInstitucion))
		{
			aImprimir.add
			(
				"<tr bgcolor=\"#FFFFFF\">" +
					"<td colspan=\"2\">" +
						"Este paciente no existe y/o su instituci&oacute;n no tiene acceso al mismo" +
					"</td>" +
				"</tr>"
			);
			return aImprimir;
		}
		String meses[]={"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

		aImprimir.add
		(
			"<tr>" +
				"<td colspan=\"2\" class=\"Subtitulo\">" +
					"Paciente: " + this.getPrimerApellidoPersona() + " " + this.getSegundoApellidoPersona() + ", " + this.getPrimerNombrePersona() + " " + this.getSegundoNombrePersona() +
				"</td>" +
			"</tr>"
		);

		aImprimir.add
		(
			"<tr bgcolor=\"#FFFFFF\">" +
				"<td align=\"right\">" +
					"Tipo de Identificaci&oacute;n:" +
				"</td>" +
				"<td align=\"left\">" +
					this.getTipoIdentificacion() +
				"</td>" +
			"</tr>"
		);

		aImprimir.add
		(
			"<tr bgcolor=\"#FFFFFF\">" +
				"<td align=\"right\">" +
					"N&uacute;mero de Identificaci&oacute;n:" +
				"</td>" +
				"<td align=\"left\">" +
					this.getNumeroIdentificacion() +
				"</td>" +
			"</tr>"
		);

		String telefono = this.getTelefono();
		if (telefono == null || telefono.equals("") )
			telefono = "No disponible";

		aImprimir.add
		(
			"<tr bgcolor=\"#FFFFFF\">" +
				"<td align=\"right\">" +
					"Tel&eacute;fono:" +
				"</td>" +
				"<td align=\"left\">" +
					telefono +
				"</td>" +
			"</tr>"
		);

		aImprimir.add
		(
			"<tr bgcolor=\"#FFFFFF\">" +
				"<td align=\"right\">" +
					"Lugar de Nacimiento:" +
				"</td>" +
				"<td align=\"left\">" +
					UtilidadTexto.fraseAEstandar( this.getCiudadIdentificacion() ) + ", " + UtilidadTexto.fraseAEstandar( this.getDepartamentoIdentificacion() ) +
				"</td>" +
			"</tr>"
		);

		aImprimir.add
		(
			"<tr bgcolor=\"#FFFFFF\">" +
				"<td align=\"right\">" +
					"Fecha de Nacimiento:" +
				"</td>" +
				"<td align=\"left\">" +
					this.getDiaNacimiento() + " " + meses[ Integer.parseInt(this.getMesNacimiento()) - 1] + " " + this.getAnioNacimiento()  + " (" + this.getEdad() + ")" +
				"</td>" +
			"</tr>"
		);

		aImprimir.add
		(
			"<tr bgcolor=\"#FFFFFF\">" +
				"<td align=\"right\">" +
					"Estado Civil:" +
				"</td>" +
				"<td align=\"left\">" +
					this.getEstadoCivil() +
				"</td>" +
			"</tr>"
		);

		aImprimir.add
		(
			"<tr bgcolor=\"#FFFFFF\">" +
				"<td align=\"right\">" +
					"Tipo de Sangre:" +
				"</td>" +
				"<td align=\"left\">" +
					tipoSangre +
				"</td>" +
			"</tr>"
		);

		aImprimir.add
		(
			"<tr bgcolor=\"#FFFFFF\">" +
				"<td align=\"right\">" +
					"Sexo:" +
				"</td>" +
				"<td align=\"left\">" +
					this.getSexo() +
				"</td>" +
			"</tr>"
		);

		aImprimir.add
		(
			"<tr bgcolor=\"#FFFFFF\">" +
				"<td align=\"right\">" +
					"Direcci&oacute;n:" +
				"</td>" +
				"<td align=\"left\">" +
					this.getDireccion() +
				"</td>" +
			"</tr>"
		);

		String barrio			= this.getBarrio();
		String residencia	= new String();
		String temp[]			= barrio.split("-");

		barrio = UtilidadTexto.fraseAEstandar(temp[1]);
		if (temp[0].charAt(0) == '0')
			residencia = UtilidadTexto.fraseAEstandar(this.getCiudad() ) + ", " +  UtilidadTexto.fraseAEstandar(this.getDepartamento() );
		else
			residencia =  barrio + ",  " + UtilidadTexto.fraseAEstandar(this.getCiudad() ) + ", " +  UtilidadTexto.fraseAEstandar( this.getDepartamento() );

		aImprimir.add
		(
			"<tr bgcolor=\"#FFFFFF\">" +
				"<td align=\"right\">" +
					"Lugar de Residencia:" +
				"</td>" +
				"<td align=\"left\">" +
					residencia +
				"</td>" +
			"</tr>"
		);

		String email=this.getEmail();
		if (email==null||email.equals("") )
			email = "No disponible";

		aImprimir.add
		(
			"<tr bgcolor=\"#FFFFFF\">" +
				"<td align=\"right\">" +
					"Email:" +
				"</td>" +
				"<td align=\"left\">" +
					email +
				"</td>" +
			"</tr>"
		);

		//Sacamos la lista de CuentasPaciente
		CuentasPaciente cuentasPaciente=new CuentasPaciente(this.getCodigoPersona(), codigoInstitucion);
		String prueba[]=cuentasPaciente.cargarCuentasPaciente(con, tipoBD);

		if(prueba!=null)
		{
			Cuenta cuenta;

			for (int i=0;i<prueba.length;i++)
			{
				cuenta= new Cuenta();
				cuenta.init(tipoBD);
				cuenta.cargarCuenta(con,prueba[i]);

				if (i == 0&&(this.tieneElementosDiferentes0(cuentasImprimir)))
				{
					aImprimir.add
					(
						"<tr>" +
							"<td colspan=\"2\" class=\"Subtitulo\">" +
								"Cuentas del Paciente" +
							"</td>" +
						"</tr>"
					);
				}

				aImprimir.addAll(cuenta.toHtml2(cuentasImprimir) );
			}
		}

		return aImprimir;
	}
	
	/**
	 * Método privado que revisa si un arreglo tiene
	 * elementos diferentes a 0
	 * 
	 * @param arreglo
	 * @return
	 */
	private boolean tieneElementosDiferentes0 (int arreglo[])
	{
	    int i;
	    for (i=0;i<arreglo.length;i++)
	    {
	        if (arreglo[i]!=0)
	        {
	            return true;
	        }
	    }
	    return false;
	}
	
	
	/**
	 * Método implementado para insertar el paciente al triage
	 * @param con
	 * @return
	 */
	public int insertarPacienteTriage(Connection con)
	{
		return pacienteDao.insertarPacienteTriage(con,this.datosTriage);
	}
	
	/**
	 * Método implementado para generar el log del paciente
	 * @param con 
	 * @param usuarioActual
	 * @param pacAnt = datos anteriores del paciente
	 */
	public boolean generarLog(Connection con, UsuarioBasico usuarioActual, Paciente pacAnt) 
	{
		boolean huboModificacion = huboModificacion(pacAnt);
		boolean exito = true;
		
		if(huboModificacion)
		{
			try
			{
				cargarPaciente(con, this.getCodigoPersona());
			}
			catch(SQLException e)
			{
				logger.error("Error cargando paciente al generar el log :"+e);
			}
			
			String log="\n            ====INFORMACION ORIGINAL DEL PACIENTE "+this.getCodigoPersona()+"===== " +
			"\n*  Número Historia Clínica ["+pacAnt.getNumeroHistoriaClinica()+" "+(!pacAnt.getAnioHistoriaClinica().equals("")?"("+pacAnt.getAnioHistoriaClinica()+")":"")+"] "+
			"\n*  Tipo Identificación [" +pacAnt.getTipoIdentificacion(false)+"] "+
			"\n*  Número Identificación [" +pacAnt.getNumeroIdentificacion()+"] "+
			"\n*  País Identificación ["+pacAnt.getPaisId()+"] "+
			"\n*  Ciudad Identificación [" +pacAnt.getCiudadId()+" ("+pacAnt.getDepartamentoId()+")"+"] "+
			"\n*  País de Nacimiento ["+pacAnt.getPaisIdentificacion()+"] "+
			"\n*  Ciudad de Nacimiento [" +pacAnt.getCiudadIdentificacion(false)+" ("+pacAnt.getDepartamentoIdentificacion(false)+")"+"] "+
			"\n*  Fecha de Nacimineto [" +pacAnt.getDiaNacimiento()+"/"+pacAnt.getMesNacimiento()+"/"+pacAnt.getAnioNacimiento()+"] "+
			"\n*  Estado Civil [" +pacAnt.getEstadoCivil(false)+"] "+
			"\n*  Sexo [" +pacAnt.getSexo()+"] "+
			"\n*  Apellidos y Nombres [" +pacAnt.getApellidos(false)+" "+pacAnt.getNombres(false)+"] "+
			"\n*  Dirección [" +pacAnt.getDireccion(false)+"] "+
			"\n*  País Vivienda ["+pacAnt.getPais()+"] "+
			"\n*  Ciudad Vivienda [" +pacAnt.getCiudad(false)+" ("+pacAnt.getDepartamento()+")"+"] "+
			"\n*  Barrio [" +pacAnt.getBarrio(false)+"] "+
			"\n*  Localidad ["+pacAnt.getLocalidad()+"] "+
			"\n*  Telefono [" +pacAnt.getTelefono()+"] "+
			"\n*  Telefono Celular [" +pacAnt.getTelefonoCelular()+"] "+
			"\n*  Email [" +pacAnt.getEmail()+"] "+
			"\n*  Tipo Persona [" +pacAnt.getTipoPersona(false)+"] "+
			"\n*  Ocupación [" +pacAnt.getOcupacion(false)+"] "+
			"\n*  Tipo Sangre [" +pacAnt.getTipoSangre()+"] "+
			"\n*  Zona Domicilio [" +pacAnt.getZonaDomicilio(false)+"] "+
			"\n*  Foto [" +pacAnt.getFoto()+"] "+
			"\n*  Centro Atención [" +pacAnt.getNombreCentroAtencion()+"] "+
			"\n*  Etnia [" +pacAnt.getNombreEtnia()+"] "+
			"\n*  Estudio [" +pacAnt.getNombreEstudio()+"] "+
			"\n*  Lee y Escribe [" +(pacAnt.getLee_escribe()?"Sí":"No")+"] "+
			"\n*  Grupo Poblacional [" +pacAnt.getNombreGrupoPoblacional()+"] "+
			"\n*  Religión [" +pacAnt.getNombreReligion()+"] ";
			
			 log+="\n\n            ====INFORMACION DESPUÉS DE LA MODIFICACIÓN DEL PACIENTE "+this.getCodigoPersona()+"===== " +
			 "\n*  Número Historia Clínica ["+this.getNumeroHistoriaClinica()+" "+(!this.getAnioHistoriaClinica().equals("")?"("+this.getAnioHistoriaClinica()+")":"")+"] "+
			 	"\n*  Tipo Identificación [" +this.getTipoIdentificacion(false)+"] "+
				"\n*  Número Identificación [" +this.getNumeroIdentificacion()+"] "+
				"\n*  País Identificación ["+this.getPaisId()+"] "+
				"\n*  Ciudad Identificación [" +this.getCiudadId()+" ("+this.getDepartamentoId()+")"+"] "+
				"\n*  País de Nacimiento ["+this.getPaisIdentificacion()+"] "+
				"\n*  Ciudad de Nacimiento [" +this.getCiudadIdentificacion(false)+" ("+this.getDepartamentoIdentificacion(false)+")"+"] "+
				"\n*  Fecha de Nacimineto [" +this.getDiaNacimiento()+"/"+this.getMesNacimiento()+"/"+this.getAnioNacimiento()+"] "+
				"\n*  Estado Civil [" +this.getEstadoCivil(false)+"] "+
				"\n*  Sexo [" +this.getSexo()+"] "+
				"\n*  Apellidos y Nombres [" +this.getApellidos(false)+" "+this.getNombres(false)+"] "+
				"\n*  Dirección [" +this.getDireccion(false)+"] "+
				"\n*  País Vivienda ["+this.getPais()+"] "+
				"\n*  Ciudad Vivienda [" +this.getCiudad(false)+" ("+this.getDepartamento()+")"+"] "+
				"\n*  Barrio [" +this.getBarrio(false)+"] "+
				"\n*  Localidad ["+this.getLocalidad()+"] "+
				"\n*  Telefono [" +this.getTelefono()+"] "+
				"\n*  Telefono Celular [" +this.getTelefonoCelular()+"] "+
				"\n*  Email [" +this.getEmail()+"] "+
				"\n*  Tipo Persona [" +this.getTipoPersona(false)+"] "+
				"\n*  Ocupación [" +this.getOcupacion(false)+"] "+
				"\n*  Tipo Sangre [" +this.getTipoSangre()+"] "+
				"\n*  Zona Domicilio [" +this.getZonaDomicilio(false)+"] "+
				"\n*  Foto [" +this.getFoto()+"] "+
				"\n*  Centro Atención [" +this.getNombreCentroAtencion()+"] "+
				"\n*  Etnia [" +this.getNombreEtnia()+"] "+
				"\n*  Estudio [" +this.getNombreEstudio()+"] "+
				"\n*  Lee y Escribe [" +(this.getLee_escribe()?"Sí":"No")+"] "+
				"\n*  Grupo Poblacional [" +this.getNombreGrupoPoblacional()+"] "+
				"\n*  Religión [" +this.getNombreReligion()+"] ";
			 
			   log+="\n========================================================\n\n\n " ;
			   LogsAxioma.enviarLog(ConstantesBD.logModificarPacienteCodigo, log, ConstantesBD.tipoRegistroLogModificacion,usuarioActual.getLoginUsuario());
			   
			   //***************ADICIONALMENTE A LA GENERACION DEL LOG SE ACTUALIZA INFORMACION DEL DEUDOR (SI EXISTE)**********************
			   exito = DocumentosGarantia.actualizarDatosPersonaDocGarantia(
					  con, 
					  usuarioActual.getCodigoInstitucionInt(), 
					  ConstantesBD.codigoNuncaValido, 
					  this.getCodigoPersona(), 
					  ConstantesIntegridadDominio.acronimoPaciente, 
					  this.getCodigoTipoIdentificacion(), 
					  this.getNumeroIdentificacion());
			   //*****************************************************************************************************************************
		}
		return exito;
	}

	/**
	 * Método que verifica si hubo modificacion del paciente
	 * @param pacienteAnterior
	 * @return
	 */
	private boolean huboModificacion(Paciente pacienteAnterior) 
	{
		boolean hubo = false;
		
		
		if(!this.getCodigoTipoIdentificacion().equals(pacienteAnterior.getCodigoTipoIdentificacion()))
		{
			//logger.info("diferente tipo identificacion!!!!");
			hubo = true;
		}
		if(!this.getNumeroIdentificacion().equals(pacienteAnterior.getNumeroIdentificacion()))
		{
			//logger.info("diferente numero identificacion!!!!");
			hubo = true;
		}
		if(!this.getCodigoDepartamentoId().equals(pacienteAnterior.getCodigoDepartamentoId()))
		{
			//logger.info("diferente depto de identificacion!!!!");
			hubo = true;
		}	
		
		if(!this.getCodigoCiudadId().equals(pacienteAnterior.getCodigoCiudadId()))
		{
			//logger.info("diferente ciudad identificacion!!!!");
			hubo = true;
		}
		if(!this.getCodigoPaisId().equals(pacienteAnterior.getCodigoPaisId()))
		{
			hubo = true;
		}
		if(!this.getCodigoDepartamentoIdentificacion().equals(pacienteAnterior.getCodigoDepartamentoIdentificacion()))
		{
			//logger.info("diferente depto nacimiento!!!!");
			hubo = true;
		}
		if(!this.getCodigoCiudadIdentificacion().equals(pacienteAnterior.getCodigoCiudadIdentificacion()))
		{
			//logger.info("diferente ciudad nacimiento!!!!");
			hubo = true;
		}
		if(!this.getCodigoPaisIdentificacion().equals(pacienteAnterior.getCodigoPaisIdentificacion()))
		{
			hubo = true;
		}
		if(!this.getDiaNacimiento().equals(pacienteAnterior.getDiaNacimiento()))
		{
			//logger.info("diferente dia de nacimiento!!!!");
			hubo = true;
		}
		if(!this.getMesNacimiento().equals(pacienteAnterior.getMesNacimiento()))
		{
			//logger.info("diferente mes de nacimiento!!!!");
			hubo = true;
		}
		if(!this.getAnioNacimiento().equals(pacienteAnterior.getAnioNacimiento()))
		{
			//logger.info("diferente anio de nacimiento!!!!");
			hubo = true;
		}
		if(!this.getCodigoEstadoCivil().equals(pacienteAnterior.getCodigoEstadoCivil()))
		{
			//logger.info("diferente estado civil!!!!");
			hubo = true;
		}
		if(!this.getCodigoSexo().equals(pacienteAnterior.getCodigoSexo()))
		{
			//logger.info("diferente sexo!!!!");
			hubo = true;
		}
		if(!this.getPrimerNombrePersona(false).equals(pacienteAnterior.getPrimerNombrePersona(false)))
		{
			//logger.info("diferente primer nombre persona!!!!");
			hubo = true;
		}
		if(!this.getSegundoNombrePersona(false).equals(pacienteAnterior.getSegundoNombrePersona(false)))
		{
			//logger.info("diferente segundo nombre!!!!");
			hubo = true;
		}
		if(!this.getPrimerApellidoPersona(false).equals(pacienteAnterior.getPrimerApellidoPersona(false)))
		{
			//logger.info("diferente primer apellido!!!!");
			hubo = true;
		}
		if(!this.getSegundoApellidoPersona(false).equals(pacienteAnterior.getSegundoApellidoPersona(false)))
		{
			//logger.info("diferente segundo apellido!!!!");
			hubo = true;
		}
		if(!this.getDireccion(false).equals(pacienteAnterior.getDireccion(false)))
		{
			//logger.info("diferente direccion!!!!");
			hubo = true;
		}
		if(!this.getCodigoDepartamento().equals(pacienteAnterior.getCodigoDepartamento()))
		{
			//logger.info("diferente cdepto vivienda!!!!");
			hubo = true;
		}
		if(!this.getCodigoCiudad().equals(pacienteAnterior.getCodigoCiudad()))
		{
			//logger.info("diferente ciudad vivienda!!!!");
			hubo = true;
		}
		if(!this.getCodigoPais().equals(pacienteAnterior.getCodigoPais()))
		{
			hubo = true;
		}
		if(!this.getCodigoBarrio().equals(pacienteAnterior.getCodigoBarrio()))
		{
			//logger.info("diferente barrio!!!!");
			hubo = true;
		}
		if(!this.getCodigoLocalidad().equals(pacienteAnterior.getCodigoLocalidad()))
		{
			hubo = true;
		}
		if(!this.getTelefono().equals(pacienteAnterior.getTelefono()))
		{
			//logger.info("diferente teléfono!!!!");
			hubo = true;
		}
		if(!this.getEmail().equals(pacienteAnterior.getEmail()))
		{
			//logger.info("diferente email!!!!");
			hubo = true;
		}
		if(!this.getCodigoTipoPersona().equals(pacienteAnterior.getCodigoTipoPersona()))
		{
			//logger.info("diferente tipo persona!!!!");
			hubo = true;
		}
		if(!this.getCodigoOcupacion().equals(pacienteAnterior.getCodigoOcupacion()))
		{
			//logger.info("diferente ocupacion!!!!");
			hubo = true;
		}
		if(!this.getCodigoTipoSangre().equals(pacienteAnterior.getCodigoTipoSangre()))
		{
			//logger.info("diferente tipo sangre!!!!");
			hubo = true;
		}
		if(!this.getCodigoZonaDomicilio().equals(pacienteAnterior.getCodigoZonaDomicilio()))
		{
			//logger.info("diferente zona domicilio!!!!");
			hubo = true;
		}
		if(this.getFoto()!=null&&!this.getFoto().equals(pacienteAnterior.getFoto()))
		{
			//logger.info("diferente foto!!!!");
			hubo = true;
		}
		if(this.getCentro_atencion()!=pacienteAnterior.getCentro_atencion())
		{
			//logger.info("diferente centro atencion!!!!");
			hubo = true;
		}
		if(this.getEtnia()!=pacienteAnterior.getEtnia())
		{
			//logger.info("diferente etnia!!!!");
			hubo = true;
		}
		if(this.getLee_escribe().booleanValue()!=pacienteAnterior.getLee_escribe().booleanValue())
		{
			//logger.info("diferente lee y es riba!!!! "+this.getLee_escribe()+"="+pacienteAnterior.getLee_escribe());
			hubo = true;
		}
		if(this.getEstudio()!=pacienteAnterior.getEstudio())
		{
			//logger.info("diferente estudio!!!!");
			hubo = true;
		}
		if(!this.getCodigoGrupoPoblacional().equals(pacienteAnterior.getCodigoGrupoPoblacional()))
		{
			//logger.info("diferente grupo poblacional!!!!");
			hubo = true;
		}
		if(!this.getCodigoReligion().equals(pacienteAnterior.getCodigoReligion()))
		{
			hubo = true;
		}
		
		
		
		return hubo;
	}
	
	/**
	 * 
	 * @param login 
	 * @param con
	 * @param paciente
	 * @param idIngreso
	 * @param institucion
	 * @return
	 */
	public ResultadoBoolean manejoInterfazTesoreria(String login, int institucion) 
	{
		ResultadoBoolean resp = new ResultadoBoolean(true,"");
		
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getInterfazPaciente(institucion)))
		{
			UtilidadBDInterfaz utilidadBD = new UtilidadBDInterfaz();
			
			
			//Se verifica si existe registro para el paciente
			DtoInterfazPaciente dto = utilidadBD.cargarPaciente(this.getCodigoPersona()+"", institucion);
			if(UtilidadTexto.getBoolean(dto.isError()+""))
			{
				return new ResultadoBoolean(false,dto.getMensaje());
			}
			
			dto.setInstitucion(institucion);
			
			if(!dto.getCodigo().equals(""))
			{
				dto.setTipoIdentificiacion(this.getCodigoTipoIdentificacion());
				dto.setNumeroIdentificacion(this.getNumeroIdentificacion());
				dto.setPrimerNombre(this.getPrimerNombrePersona(false));
				dto.setSegundoNombre(this.getSegundoNombrePersona(false));
				dto.setPrimerApellido(this.getPrimerApellidoPersona(false));
				dto.setSegundoApellido(this.getSegundoApellidoPersona(false));
				dto.setFechaNacimiento(this.getDiaNacimiento()+"/"+this.getMesNacimiento()+"/"+this.getAnioNacimiento());
				
//				SE carga el sexo del paciente
				if(Utilidades.convertirAEntero(this.getCodigoSexo())==ConstantesBD.codigoSexoMasculino)
				{
					dto.setSexo(ConstantesBDInterfaz.sexoMasculino);
				}	
				else
				{	
					if(Utilidades.convertirAEntero(this.getCodigoSexo())==ConstantesBD.codigoSexoFemenino)
						dto.setSexo(ConstantesBDInterfaz.sexoFemenino);
					else
						dto.setSexo(ConstantesBDInterfaz.sexoAmbos);
				}
				
				dto.setUsuario(login);
				//Se modifica el registro de la interfaz de tesoreria
				resp = utilidadBD.modificarPaciente(dto);
				logger.info("EXITO EN LA MODIFICACION DEL PACIENTE (INTERFAZ)? "+resp);
			}
		}
		
		return resp;
		
	}

	/**
	 * Returna la foto del paciente (null si no tiene).
	 * @return String con la foto del paciente
	 */
	public String getFoto() {
		return foto;
	}

	/**
	 * Establece la la foto del paciente.
	 * @param foto La la foto del paciente a establecer
	 */
	public void setFoto(String foto) {
		this.foto = foto;
	}
	
		
	/**
	 * Carga la informacion del responsable de paciente
	 * @param Connection con
	 * @param String tipoIdentificacion
	 * @param String numeroIdentificacion
	 * */
	public DtoResponsablePaciente getResponsablePaciente(Connection con, String tipoIdentificacion, String numeroIdentificacion)
	{		
		return  UtilidadesManejoPaciente.cargarResponsablePaciente(con, tipoIdentificacion, numeroIdentificacion);		
	}		

	/**
	 * Adición de Sebastián
	 * Método para verificar que un paciente nuevo que se vaya a ingresar no tenga 
	 * los mismos nombres y apellidos de otro paciente que ya esté registrado en el sistema
	 * @param con
	 * @param primerNombre
	 * @param segundoNombre
	 * @param primerApellido
	 * @param segundoApellido
	 * @return
	 */
	public ArrayList<HashMap<String, Object>> validarPacienteIgualNombre(Connection con,String primerNombre,String segundoNombre,String primerApellido,String segundoApellido,String tipoID,String numeroID){
		
		return pacienteDao.validarPacienteIgualNombre(con,primerNombre,segundoNombre,primerApellido,segundoApellido,tipoID,numeroID);
		
	}
	
	
	/**
	 * Adición de Sebastián
	 * Método para verificar que un paciente nuevo que se vaya a ingresar no tenga 
	 * los mismos nombres y apellidos de otro paciente que ya esté registrado en el sistema
	 * @param con
	 * @param primerNombre
	 * @param segundoNombre
	 * @param primerApellido
	 * @param segundoApellido
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> validarPacienteIgualNombreStatico(Connection con,String primerNombre,String segundoNombre,String primerApellido,String segundoApellido,String tipoID,String numeroID)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPacienteDao().validarPacienteIgualNombre(con,primerNombre,segundoNombre,primerApellido,segundoApellido,tipoID,numeroID);
		
	}
	
	
	/**
	 * Método que actualiza el grupo poblacional de un paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public int actualizarGrupoPoblacional(Connection con)
	{
		HashMap campos = new HashMap();
		campos.put("codigoPaciente", this.getCodigoPersona());
		campos.put("grupoPoblacional", this.codigoGrupoPoblacional);
		return pacienteDao.actualizarGrupoPoblacional(con, campos);
	}
	

	/**
	 * @return Returns the datosTriage.
	 */
	public HashMap getDatosTriage() {
		return datosTriage;
	}

	/**
	 * @param datosTriage The datosTriage to set.
	 */
	public void setDatosTriage(HashMap datosTriage) {
		this.datosTriage = datosTriage;
	}
	
	/**
	 * @return Retorna un elemento del mapa datosTriage.
	 */
	public Object getDatosTriage(String key) {
		return datosTriage.get(key);
	}

	/**
	 * @param Asigna un elemento al mapa datosTriage.
	 */
	public void setDatosTriage(String key,Object obj) {
		this.datosTriage.put(key,obj);
	}

	/**
	 * @return the codigoGrupoPoblacional
	 */
	public String getCodigoGrupoPoblacional() {
		return codigoGrupoPoblacional;
	}

	/**
	 * @param codigoGrupoPoblacional the codigoGrupoPoblacional to set
	 */
	public void setCodigoGrupoPoblacional(String codigoGrupoPoblacional) {
		this.codigoGrupoPoblacional = codigoGrupoPoblacional;
		setNombreGrupoPoblacional(ValoresPorDefecto.getIntegridadDominio(codigoGrupoPoblacional)+"");
	}

	/**
	 * @return the nombreGrupoPoblacional
	 */
	public String getNombreGrupoPoblacional() {
		return nombreGrupoPoblacional;
	}

	/**
	 * @param nombreGrupoPoblacional the nombreGrupoPoblacional to set
	 */
	public void setNombreGrupoPoblacional(String nombreGrupoPoblacional) {
		this.nombreGrupoPoblacional = nombreGrupoPoblacional;
	}

	/**
	 * @return the anioHistoriaClinica
	 */
	public String getAnioHistoriaClinica() {
		return anioHistoriaClinica;
	}

	/**
	 * @param anioHistoriaClinica the anioHistoriaClinica to set
	 */
	public void setAnioHistoriaClinica(String anioHistoriaClinica) {
		this.anioHistoriaClinica = anioHistoriaClinica;
	}

	/**
	 * @return the numeroHistoriaClinica
	 */
	public String getNumeroHistoriaClinica() {
		return numeroHistoriaClinica;
	}

	/**
	 * @param numeroHistoriaClinica the numeroHistoriaClinica to set
	 */
	public void setNumeroHistoriaClinica(String numeroHistoriaClinica) {
		this.numeroHistoriaClinica = numeroHistoriaClinica;
	}

	/**
	 * @return the codigoReligion
	 */
	public String getCodigoReligion() {
		return codigoReligion;
	}

	/**
	 * @param codigoReligion the codigoReligion to set
	 */
	public void setCodigoReligion(String codigoReligion) {
		this.codigoReligion = codigoReligion;
	}

	/**
	 * @return the nombreReligion
	 */
	public String getNombreReligion() {
		return nombreReligion;
	}

	/**
	 * @param nombreReligion the nombreReligion to set
	 */
	public void setNombreReligion(String nombreReligion) {
		this.nombreReligion = nombreReligion;
	}
	
	/**
	 * Método para insertar una observacion para un paciente
	 * @param con
	 * @param codigoPaciente
	 * @param observaciones
	 * @return
	 * @throws SQLException
	 */
	public int insertarObservacionPaciente( Connection con, int codigoPaciente, String observaciones) throws SQLException
	{
		return pacienteDao.insertarObservacionPaciente(con, codigoPaciente, observaciones);
	}
	
	/**
	 * @return Returns the observacionesGenerales.
	 */
	public String getObservacionesGenerales()
	{
		return observacionesGenerales;
	}
	/**
	 * @param observacionesGenerales The observacionesGenerales to set.
	 */
	public void setObservacionesGenerales(String observacionesGenerales)
	{
		this.observacionesGenerales=observacionesGenerales;
	}
	/**
	 * Retorna el codigo de ocupacion.
	 * @return codigo de ocupacion
	 */
	public String getCodigoOcupacion() {
		return codigoOcupacion;
	}

	/**
	 * Retorna el codigo de la zona de domicilio.
	 * @return codigo de la zona de domicilio
	 */
	public String getCodigoZonaDomicilio() {
		return codigoZonaDomicilio;
	}

	/**
	 * Retorna la ocupacion del paciente.
	 * @param encoded especifica si se debe o no retornar esta cadena como <i>character entities</i>
	 * de HTML (e.g., "ï¿½" como "&amp;aacute;"
	 * @return la ocupacion del paciente
	 */
	public String getOcupacion(boolean encoded) {
		if (encoded) {
			return getOcupacion();
		}
		else {
			return ocupacion;
		}
	}

	/**
	 * Retorna la ocupacion del paciente.
	 * (codificado como <i>character entities</i> de HTML, e.g., "ï¿½" como "&amp;aacute;").
	 * @return ocupacion del paciente
	 */
	public String getOcupacion() {
		return Encoder.encode(ocupacion);
	}

	/**
	 * Retorna la zona de domicilio del paciente.
	 * @param encoded especifica si se debe o no retornar esta cadena como <i>character entities</i>
	 * de HTML (e.g., "ï¿½" como "&amp;aacute;"
	 * @return zona de domicilio del paciente
	 */
	public String getZonaDomicilio(boolean encoded) {
		if (encoded) {
			return getZonaDomicilio();
		}
		else {
			return zonaDomicilio;
		}
	}

	/**
	 * Retorna la zona de domicilio del paciente.
	 * (codificado como <i>character entities</i> de HTML, e.g., "ï¿½" como "&amp;aacute;").
	 * @return zona de domicilio del paciente
	 */
	public String getZonaDomicilio() {
		return Encoder.encode(zonaDomicilio);
	}

	/**
	 * Retorna el codigo del tipo de sangre.
	 * @return String con el codigo del tipo de sangre
	 */
	public String getCodigoTipoSangre() {
		return codigoTipoSangre;
	}

	/**
	 * Retorna el tipo de sangre (texto).
	 * @return String con el tipo de sangre
	 */
	public String getTipoSangre() {
		return tipoSangre;
	}

	/**
	 * Establece el codigo de ocupacion del paciente.
	 * @param codigoOcupacion el codigo de ocupacion a ser establecido
	 */
	public void setCodigoOcupacion(String codigoOcupacion) {
		this.codigoOcupacion = codigoOcupacion;
	}

	/**
	 * Establece el codigo de la zona de domicilio del paciente.
	 * @param codigoZonaDomicilio el codigo de la zona de domicilio a ser establecido
	 */
	public void setCodigoZonaDomicilio(String codigoZonaDomicilio) {
		this.codigoZonaDomicilio = codigoZonaDomicilio;
	}

	/**
	 * Establece la ocupacion del paciente.
	 * @param ocupacion la ocupacion a ser establecida
	 */
	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}

	/**
	 * Establece la zona de domicilio del paciente.
	 * @param zonaDomicilio la zona de domicilio a ser establecida
	 */
	public void setZonaDomicilio(String zonaDomicilio) {
		this.zonaDomicilio = zonaDomicilio;
	}

	/**
	 * Establece el codigo del tipo de sangre.
	 * @param codigoTipoSangre El codigoTipoSangre a establecer
	 */
	public void setCodigoTipoSangre(String codigoTipoSangre) {
		this.codigoTipoSangre = codigoTipoSangre;
	}

	/**
	 * Establece el tipo de sangre.
	 * @param tipoSangre el tipo de sangre a establecer
	 */
	public void setTipoSangre(String tipoSangre) {
		this.tipoSangre = tipoSangre;
	}

	/**
	 * @return the infoHistoSistemaAnt
	 */
	public Boolean getInfoHistoSistemaAnt() {
		return infoHistoSistemaAnt;
	}

	/**
	 * @param infoHistoSistemaAnt the infoHistoSistemaAnt to set
	 */
	public void setInfoHistoSistemaAnt(Boolean infoHistoSistemaAnt) {
		this.infoHistoSistemaAnt = infoHistoSistemaAnt;
	}

	/**
	 * @return the beneficiariosPac
	 */
	public ArrayList<DtoBeneficiarioPaciente> getBeneficiariosPac() {
		return beneficiariosPac;
	}

	/**
	 * @param beneficiariosPac the beneficiariosPac to set
	 */
	public void setBeneficiariosPac(
			ArrayList<DtoBeneficiarioPaciente> beneficiariosPac) {
		this.beneficiariosPac = beneficiariosPac;
	}

	/**
	 * @return the motivoCitaPaciente
	 */
	public DtoMotivoCitaPaciente getMotivoCitaPaciente() {
		return motivoCitaPaciente;
	}

	/**
	 * @param motivoCitaPaciente the motivoCitaPaciente to set
	 */
	public void setMotivoCitaPaciente(DtoMotivoCitaPaciente motivoCitaPaciente) {
		this.motivoCitaPaciente = motivoCitaPaciente;
	}

	/**
	 * @return the fechaMotivoCita
	 */
	public String getFechaMotivoCita() {
		return fechaMotivoCita;
	}

	/**
	 * @param fechaMotivoCita the fechaMotivoCita to set
	 */
	public void setFechaMotivoCita(String fechaMotivoCita) {
		this.fechaMotivoCita = fechaMotivoCita;
	}

	/**
	 * @return the horaMotivoCita
	 */
	public String getHoraMotivoCita() {
		return horaMotivoCita;
	}

	/**
	 * @param horaMotivoCita the horaMotivoCita to set
	 */
	public void setHoraMotivoCita(String horaMotivoCita) {
		this.horaMotivoCita = horaMotivoCita;
	}

	/**
	 * @return the observacionesMotivoCita
	 */
	public String getObservacionesMotivoCita() {
		return ObservacionesMotivoCita;
	}

	/**
	 * @param observacionesMotivoCita the observacionesMotivoCita to set
	 */
	public void setObservacionesMotivoCita(String observacionesMotivoCita) {
		ObservacionesMotivoCita = observacionesMotivoCita;
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
	 * @return the convenioReserva
	 */
	public String getConvenioReserva() {
		return convenioReserva;
	}

	/**
	 * @param convenioReserva the convenioReserva to set
	 */
	public void setConvenioReserva(String convenioReserva) {
		this.convenioReserva = convenioReserva;
	}

	/**
	 * @return the numeroHijos
	 */
	public int getNumeroHijos() {
		return numeroHijos;
	}

	/**
	 * @param numeroHijos the numeroHijos to set
	 */
	public void setNumeroHijos(int numeroHijos) {
		this.numeroHijos = numeroHijos;
	}

	/**
	 * @return the estadoTransaccion
	 */
	public String getEstadoTransaccion() {
		return estadoTransaccion;
	}

	/**
	 * @param estadoTransaccion the estadoTransaccion to set
	 */
	public void setEstadoTransaccion(String estadoTransaccion) {
		this.estadoTransaccion = estadoTransaccion;
	}

	public int getCentroAtencionDuenio() {
		return centroAtencionDuenio;
	}

	public void setCentroAtencionDuenio(int centroAtencionDuenio) {
		this.centroAtencionDuenio = centroAtencionDuenio;
	}
	
	/**
	 * 
	 * Metodo para insertar el centro de atencion duenio cuando no exista
	 * @param con
	 * @param codigoPaciente
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public static boolean actualizarCentroAtencionDuenioPaciente(Connection con, int codigoPaciente, int centroAtencionDuenio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPacienteDao().actualizarCentroAtencionDuenioPaciente(con, codigoPaciente, centroAtencionDuenio);
	}

	/**
	 * @param codigoIngreso the codigoIngreso to set
	 */
	public void setCodigoIngreso(int codigoIngreso) {
		this.codigoIngreso = codigoIngreso;
	}

	/**
	 * @return the codigoIngreso
	 */
	public int getCodigoIngreso() {
		return codigoIngreso;
	}
	
	/**
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public String obtenerFechaHoraIngreso(Connection con,String codigoPaciente){
		
		return pacienteDao.consultarFechaHoraIngreso(con, codigoPaciente);
	}
	
	/**
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public  String consultarCentroAtencionPaciente(Connection con,String codigoPaciente){
		return pacienteDao.consultarCentroAtencionPaciente(con, codigoPaciente);
	}
	
	
	
}