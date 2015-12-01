/*
 * @(#)PersonaBasica.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.mundo;

import java.io.Serializable;
import java.sql.Connection;
import java.util.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.Encoder;
import util.TipoNumeroId;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.ValoresPorDefecto;
import util.odontologia.UtilidadOdontologia;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.PersonaBasicaDao;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.manejoPaciente.DtoOtrosIngresosPaciente;
import com.princetonsa.dto.odontologia.DtoAntecedentesAlerta;
/*
* Tipo Modificacion: Segun incidencia 7055
* usuario: jesrioro
* Fecha: 30/05/2013
* Descripcion: Se  implementa  serializable  para  poder generar  el  reporte  de  HC  en  el  contexto de  reportes            
*/
/**
 * Esta clase es una version reducida de <code>Persona</code>, con los datos minimos para identificar de manera unica a una persona.
 *
 * @version 1.0, Oct 17, 2002
 * @author     <a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */
public class PersonaBasica implements Observer, HttpSessionBindingListener,Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Para hacer logs de esta funcionalidad.
	 */	
	transient private Logger logger = Logger.getLogger(PersonaBasica.class);
	/**
	 * Verificar si un paciente tiene o no manejo conjunto
	 */
	private boolean manejoConjunto;
	
	/**
	 * El DAO usado por el objeto <code>PersonaBasica</code> para acceder a la fuente de datos.
	 */
	private static PersonaBasicaDao personaBasicaDao;

	/**
	 * Código de la persona en la fuente de datos
	 */
	private int codigoPersona;

	/**
	 * Numero de Identificacion de la persona.
	 */
	private String numeroIdentificacionPersona;

	/**
	 * Tipo de identificacion de la persona (e.g., cedula, tarjeta de identidad, etc.).
	 */
	private String tipoIdentificacionPersona;

	/**
	 * Codigo del tipo de identificacion de la persona.
	 */
	private String codigoTipoIdentificacionPersona;

	/**
	 * Nombres y apellidos de la persona.
	 */
	private String nombrePersona;
	private String apellidosNombresPersona;

	/**
	 * Edad de la persona, en años
	 */
	private int edad;

	/**
	 * Edad de la persona, en años, meses, días según corresponda
	 */
	private String edadDetallada;
	
	/**
	 * Edad en días del paciente
	 */
	private int edadDias;
	
	/**
	 * Edad en meses del paciente 
	 */
	private int edadMeses;

	/**
	 * Código del sexo de la persona (1 - Masculino, 2 - Femenino)
	 */
	private int    codigoSexo;
	private String sexo;
	private String tipoSangre;

	/**
	 * Anio de nacimiento
	 */
	private String anioNacimiento = "1990";

	/**
	 * Mes de nacimiento
	 */
	private String mesNacimiento = "1";

	/**
	 * Dia de nacimiento
	 */
	private String diaNacimiento = "1";

	/*
	 * ESTOS DATOS SE PUEDEN PONER EN OTRA CLASE LLAMADA PacienteBasico.java,
	 * si se considera necesario u oportuno. Los Datos a partir de este punto
	 * son necesarios para la visualización ya que personaBasica se almacenará
	 * en sesión.
	 */

	/**
	 * Según la vía de ingreso, la información de este campo es:
	 * 1-Hospitalización := Centro de costo del médico tratante, asignado en primera instancia durante la admisión de hospitalización
	 * 3-Urgencias := Centro de costo de urgencias
	 * 4-Consulta Externa := Pendiente por definir
	 * 2-Ambulatorios := Pendiente por definir
	 * @version Sept. 23 de 2003
	 */
	private String area;

    /**
     * codigo del area
     */
    private int codigoArea;
    
	/**
	 * Nombre de la cama asignada al paciente.
	 * 1-Hospitalización := Dato en la admisión de hospitalización
	 * 3-Urgencias := Dato en la admisión de urgencias, si el paciente ha sido ingresado a cama de observación
	 * 4-Consulta Externa := Pendiente por definir
	 * 2-Ambulatorios := Pendiente por definir
	 */
	private String cama;
	
	/**
	 * Código de la cama
	 */
	private int codigoCama;

	/**
	 * Código del tipo de régimen. Sirve para que la presentación sepa que
	 * debe mostrar responsable cuenta o convenio responsable
	 */
	private char codigoTipoRegimen;
	
	/**
	 * Nombre del regime al que pertenece el pacienteo
	 * @author armando
	 */
	private String nombreTipoRegimen;
		
	/**
	 * Nombre de la institucion a la que pertenece el pacienteo
	 * @author armando
	 */
	private String nombreEmpresa;

	/**
	 * Nombre del convenio responsable de esta cuenta si estamos trabajando con
	 * un convenio en particular o el Responsable de la cuenta si estamos
	 * trabajando en un tipo de regimen Particular (P) u Otros (O)
	 */
	private String convenioPersonaResponsable;

	/**
	 * Código del ingreso abierto. Si es 0 la presentación sabe que debe
	 * hacerselo saber al usuario
	 */
	private int codigoIngreso;
	
	/**
	 * Consecutivo del ingreso del paciente, tiene la forma 99999-YYYY (anio)
	 */
	private String consecutivoIngreso;

	/**
	 * Código de la cuenta abierta. Si es 0 la presentación sabe que debe
	 * hacerselo saber al usuario
	 */
	private int codigoCuenta;
	
	
	/**
	 * Array de Cuentas del paciente
	 * */
	private ArrayList<CuentasPaciente> cuentasPacienteArray;

	/**
	 * Código de la admisión abierta. Si es 0 no hay ninguna abierta para este
	 * ingreso. Como muchos ingresos no tienen admisión, la  presentación no
	 * debe manejar este campo un poco diferente al de la cuenta / ingreso. Si
	 * es una admisión Hospitalaria el año de la admisión es 0, si es diferente
	 * de 0, es una admision de Urgencias
	 */
	private int codigoAdmision;

	/**
	 * Año de la Admision de Urgencias. Queda en 0 si no hay admisión de
	 * urgencias.
	 */
	private int anioAdmision;

	/**
	 * Nombre de la última vía de Ingreso
	 */
	private String ultimaViaIngreso;

	/**
	 * Código de la última vía de Ingreso
	 */
	private int codigoUltimaViaIngreso;


	/**
	 * Código del convenio (Este parámetro solo
	 * se carga al ser usado el get, muchas veces no
	 * se necesita). El get interactua con el de 
	 * codigoTarifarioActual para optimizar la carga
	 * de la información
	 */
	private int codigoConvenio;

	/**
	 * Código del convenio, el mismo comportamiento
	 * de convenio y tarifario actual
	 */
	private int codigoContrato;
	
	/**
	 * Fecha Ingreso del Paciente
	 */
	private String fechaIngreso;
	
	/**
	 * Hora ingreso del paciente
	 */
	private String horaIngreso;

	/**
	 * Institución a la que pertenece este paciente.
	 */
	private String tipoInstitucion = "";
	
	/**
	 * Centro de Atencion para validar el paciente
	 */
	private String tipoCentroAtencion = "-1";

	/**
	 * Boolean que me indica si existe asocio de cuenta
	 * para este paciente
	 */
	private boolean existeAsocio;
	
	/**
	 * Entero con el número de la cuenta asociada
	 */
	private int codigoCuentaAsocio;
	
	/**
	 * Boolean que me indica si el contrato que tiene el paciente
	 * se encuentra vencido
	 */
	private boolean estaContratoVencido;

	/**
	 * Observable al cual está registrado este Observer.
	 */
	private Observable observable;
	
	/**
	 * Primer Nombre
	 */
	private String primerNombre;
	
	/**
	 * Segundo Nombre
	 */
	private String segundoNombre;
	
	/**
	 * Primer Apellido
	 */
	private String primerApellido;
	
	/**
	 * Segundo Apellido
	 */
	private String segundoApellido;

	/**
	 * Direccion persona
	 */
	private String direccion;

	/**
	 * Teléfono persona
	 */
	private String telefono;
	
	/**
	 * 
	 */
	private String telefonoFijo;
	
	/**
	 * Teléfono Celular Persona
	 */
	private String telefonoCelular;
	
	/**
	 * Fecha nacimiento persona
	 */
	private String fechaNacimiento;
	
	/**
	 * Fecha Naciemiento tipo Date
	 */
	private Date fechaNacimientoTipoDate;

	/**
	 * Nombre del pais de vivienda del paciente
	 */
	private String nombrePaisVivienda;
	private String codigoPaisVivienda;
	/**
	 * Nombre de la ciudad de vivienda
	 */
	private String nombreCiudadVivienda;
	private String codigoCiudadVivienda;
	
	/**
	 * Nombre del departamento de vivienda
	 */
	private String nombreDeptoVivienda;
	private String codigoDeptoVivienda;
	
	/**
	 * Codigo del centro de atencion PYP del paciente
	 */
	private int codigoCentroAtencionPYP;
	
	/**
	 * Nombre del centro de atencion PYP del paciente
	 */
	private String nombreCentroAtencionPYP;
	
	/**
	 * Indica si el paciente tiene cuenta activa con convenio por pyp
	 */
	private boolean cuentaActivaConvenioPyP;
	
	/**
	 * Indica si el paciente tiene cuenta activa con convenio capitado
	 */
	private boolean convenioCapitado;
	
	
	/**
	 * variable para la Etnia
	 */
	private int etnia = 0;
	
	/**
	 * variable para la lee_escribe
	 */
	private Boolean lee_escribe = true;
	
	/**
	 * variable para la lee_escribe
	 */
	private int estudio = 0;
	
	private String email;
	
	/**
	 * Variable que almacena el codigo del grupo poblacional
	 */
	private String codigoGrupoPoblacional;
	
	/**
	 * Variable que almacena el nombre del grupo poblacional
	 */
	private String nombreGrupoPoblacional;
	
	/**
	 * Variable indica el tipo evento
	 * */
	private String tipoEvento;
	
	/**
	 * Variable indica el Nombre del tipo evento
	 * */
	private String nombreTipoEvento;
	
	/**
	 * Variable indica el Numero de Historia Clinica
	 * */
	private String historiaClinica;	
	
	/**
	 * tipo de identificacion del responsable de paciente
	 * */
	private String tipoIdResponPaciente;
	
	/**
	 * numero identificacion responsable paciente 
	 * */
	private String numeroIdResponPaciente;
	
	
	/**
	 * ArrayList contiene el listado de Otros Ingresos del Paciente
	 * */
	private ArrayList<DtoOtrosIngresosPaciente> listadoOtrosIngresosPaciente;

	/**
	 * ArrayList contiene el listado de Todos los Ingresos del Paciente
	 * */
	private ArrayList<DtoOtrosIngresosPaciente> listadoTodosIngresosPaciente;

	/**
	 * Variable para verificar si el paciente es de hospital día
	 */
	private boolean hospitalDia;
	
	/**
	 * Variable que almacena el codigo del tipo de paciente
	 */
	private String codigoTipoPaciente;
	
	/**
	 * Variable que almacena el nombre del tipo de paciente
	 */
	private String nombreTipoPaciente;
	
	/**
	 * Consecutivo del registro de paciente en entidad subcontratada
	 */
	private String pacEntidadSubcontratada;
	
	/**
	 * Datos del lugar de expedicion
	 */
	private String nombreCiudadExpedicion;
	private String nombrePaisExpedicion;
	private String nombreDepartamentoExpedicion;


	/**
	 * atributo adicionado por el anexo 655
	 * de transplante.
	 * Esta variable puede tener tres valoes
	 * -- DONANT
	 * -- RECEPT
	 * -- ""-->vacio
	 */
	private String transplante;
	
	/**
	 * almacena el nombre del transplante
	 */
	private String nombreTransplante;
	
	/**
	 * array list de antecedentes de alerta
	 */
	private ArrayList<DtoAntecedentesAlerta> antecedentesAlerta;
	
	
	/**Campos para guardar en caso de que no existan en usuarios capitados anexo 922*/
	private String clasificacionSE;
	private String numeroFicha;
	private String tipoIdEmpleador;
	private String numIdEmpleador;
	private String razonSociEmpleador;
	private String tipoIdCotizante;
	private String numIdCotizante;
	private String nombresCotizante;
	private String apellidosCotizante;
	private int parentesco;
	private int centroAtencionInt;
	private String tipoAfiliado;
	private String excepcionMonto; 	
	private String pais;
	private String departamento;
	private String municipio;
	private String localidad;
	private String barrio;
	private String fechaNacimientoTipoString;
	/** almacena verdadero cuando la persona es capitado 'Validacion para funcionalidad SubirPacienteMasivo'*/
	private boolean esCapitado;
	private int lineaInconsCamposPersona;
	/**
	 *Atributo que contiene el estado embarazo de la paciente 
	 */
	private Boolean estadoEmbarazada;
	
	
	private String fechaMuerte;

	/**
	 * Constructor de <code>PersonaBasica</code>.
	 */
	public PersonaBasica()
	{
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}

	/**
	 * Constructor de <code>PersonaBasica</code> que recibe solamente codigos.
	 */
	public PersonaBasica(
		String numeroIdentificacionPersona,
		String codigoTipoIdentificacionPersona
	)
	{
		this.clean();
		this.init(System.getProperty("TIPOBD"));
		this.numeroIdentificacionPersona		 = numeroIdentificacionPersona;
		this.codigoTipoIdentificacionPersona     = codigoTipoIdentificacionPersona;
		
	}
	/**
		 * Constructor de <code>PersonaBasica</code> que recibe codigo usuario.
		 */
		public PersonaBasica(
			int codigoPersona
		)
		{
			this.clean();
			this.init(System.getProperty("TIPOBD"));
			this.codigoPersona		 = codigoPersona;

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
		boolean    wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

		if(myFactory != null)
		{
			personaBasicaDao     = myFactory.getPersonaBasicaDao();
			wasInited			 = (personaBasicaDao != null);
		}

		return wasInited;
	}

	/**
	 * Retorna el codigo del tipo de identificacion de la persona ingresada.
	 * @return el codigo del tipo de identificacion de la persona
	 */
	public String getCodigoTipoIdentificacionPersona()
	{
		return codigoTipoIdentificacionPersona;
	}

	/**
	 * Retorna el nombre de la persona (completo, incluye apellidos).
	 * (codificado como <i>character entities</i> de HTML, e.g., "á" como "&amp;aacute;").
	 * @return el nombre de la persona
	 */
	public String getNombrePersona()
	{
		return Encoder.encode(nombrePersona);
	}

	public String getApellidosNombresPersona()
	{
		return Encoder.encode(apellidosNombresPersona);
	}
	
	/**
	 * Retorna el nombre de la persona (completo, incluye apellidos).
	 * @param encoded especifica si se debe o no retornar esta cadena como <i>character entities</i>
	 * de HTML (e.g., "á" como "&amp;aacute;"
	 * @return el nombre de la persona
	 */
	public String getNombrePersona(boolean encoded)
	{
		if(encoded)
			return getNombrePersona();
		else

			return nombrePersona;
	}
	
	public String getApellidosNombresPersona(boolean encoded)
	{
		if(encoded)
			return getApellidosNombresPersona();
		else
			return apellidosNombresPersona;
			
	}
	

	/**
	 * Retorna el numero de identificacion persona.
	 * @return el numero de identificacion persona.
	 */
	public String getNumeroIdentificacionPersona()
	{
		return numeroIdentificacionPersona;
	}

	/**
	 * Retorna el tipo de identificacion de la persona.
	 * @param encoded especifica si se debe o no retornar esta cadena como <i>character entities</i>
	 * de HTML (e.g., "á" como "&amp;aacute;"
	 * @return el tipo de identificacion de la persona
	 */
	public String getTipoIdentificacionPersona(boolean encoded)
	{
		if(encoded)
			return getTipoIdentificacionPersona();
		else

			return tipoIdentificacionPersona;
	}

	/**
	 * Retorna el tipo de identificacion de la persona.
	 * (codificado como <i>character entities</i> de HTML, e.g., "á" como "&amp;aacute;").
	 * @return el tipo de identificacion de la persona
	 */
	public String getTipoIdentificacionPersona()
	{
		return Encoder.encode(tipoIdentificacionPersona);
	}
//	"cue.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaProcesoFacturacion+") and "
	/**
	 * Retorna el código del sexo de la persona.
	 * @return el código del sexo de la persona
	 */
	public int getCodigoSexo()
	{
		return codigoSexo;
	}

	/**
	 * Retorna la edad de la persona.
	 * @return la edad de la persona
	 */
	public int getEdad()
	{
		return edad;
	}

	/**
	 * Returns the anioAdmision.
	 * @return int
	 */
	public int getAnioAdmision()
	{
		return anioAdmision;
	}

	/**
	 * Returns the codigoAdmision.
	 * @return int
	 */
	public int getCodigoAdmision()
	{
		return codigoAdmision;
	}

	/**
	 * Returns the codigoCuenta.
	 * @return int
	 */
	public int getCodigoCuenta()
	{
		return codigoCuenta;
	}

	/**
	 * Returns the codigoIngreso.
	 * @return int
	 */
	public int getCodigoIngreso()
	{
		return codigoIngreso;
	}

	/**
	 * Returns the codigoTipoRegimen.
	 * @return char
	 */
	public char getCodigoTipoRegimen()
	{
		return codigoTipoRegimen;
	}

	/**
	 * Returns the codigoUltimaViaIngreso.
	 * @return int
	 */
	public int getCodigoUltimaViaIngreso()
	{
		return codigoUltimaViaIngreso;
	}

	/**
	 * Returns the convenioPersonaResponsable.
	 * @return String
	 */
	public String getConvenioPersonaResponsable()
	{
		return convenioPersonaResponsable;
	}

	/**
	 * Returns the ultimaViaIngreso.
	 * @return String
	 */
	public String getUltimaViaIngreso()
	{
		return ultimaViaIngreso;
	}

	/**
	 * @return La edad de la persona.
	 */
	public String getEdadDetallada()
	{
		return edadDetallada;
	}

	/**
	 * Retorna el código de la institución a la que pertenece esta persona.
	 * @return el código de la institución a la que pertenece esta persona
	 */
	public String getTipoInstitucion()
	{
		return tipoInstitucion;
	}
	
	/**
	 * @return Returns the tipoCentroAtencion.
	 */
	public String getTipoCentroAtencion() {
		return tipoCentroAtencion;
	}

	/**
	 * Retorna la fecha de ingreso del paciente
	 * @return la fecha de ingreso del paciente
	 */
	public String getFechaIngreso() 
	{
		return fechaIngreso;
	}
	
	
	/**
	 * @return the horaIngreso
	 */
	public String getHoraIngreso() {
		return horaIngreso;
	}

	/**
	 * @param horaIngreso the horaIngreso to set
	 */
	public void setHoraIngreso(String horaIngreso) {
		this.horaIngreso = horaIngreso;
	}

	/**
	 * Establece el codigo del tipo de identificacion de la persona a ingresar.
	 * @param codigoTipoIdentificacionPersona el codigo del tipo de identificacion de la persona a establecer
	 */
	public void setCodigoTipoIdentificacionPersona(
		String codigoTipoIdentificacionPersona
	)
	{
		this.codigoTipoIdentificacionPersona = codigoTipoIdentificacionPersona;
	}

	/**
	 * Establece los nombres y apellidos de la persona a ingresar.
	 * @param nombrePersona el nombre de la persona a establecer
	 */
	public void setNombrePersona(String nombrePersona)
	{
		this.nombrePersona = nombrePersona;
	}

	/**
	 * Establece el numero de identificacion de la persona a ingresar.
	 * @param numeroIdentificacionPersona el numero de identificacion de la persona a establecer
	 */
	public void setNumeroIdentificacionPersona(
		String numeroIdentificacionPersona
	)
	{
		this.numeroIdentificacionPersona = numeroIdentificacionPersona;
	}

	/**
	 * Establece el tipo de identificacion de la persona a ingresar.
	 * @param tipoIdentificacionPersona el nombre del tipo de identificacion de la persona a establecer
	 */
	public void setTipoIdentificacionPersona(String tipoIdentificacionPersona)
	{
		this.tipoIdentificacionPersona = tipoIdentificacionPersona;
	}

	/**
	 * Establece el código del sexo de la persona.
	 * @param codigoSexo el código del sexo a establecer
	 */
	public void setCodigoSexo(int codigoSexo)
	{
		this.codigoSexo = codigoSexo;
	}

	/**
	 * Establece la edad de la persona.
	 * @param edad la edad de la persona
	 */
	public void setEdad(int edad)
	{
		this.edad = edad;
	}

	/**
	 * Sets the anioAdmision.
	 * @param anioAdmision The anioAdmision to set
	 */
	public void setAnioAdmision(int anioAdmision)
	{
		this.anioAdmision = anioAdmision;
	}

	/**
	 * Sets the codigoAdmision.
	 * @param codigoAdmision The codigoAdmision to set
	 */
	public void setCodigoAdmision(int codigoAdmision)
	{
		this.codigoAdmision = codigoAdmision;
	}

	/**
	 * Sets the codigoCuenta.
	 * @param codigoCuenta The codigoCuenta to set
	 */
	public void setCodigoCuenta(int codigoCuenta)
	{
		this.codigoCuenta = codigoCuenta;
	}

	/**
	 * Sets the codigoIngreso.
	 * @param codigoIngreso The codigoIngreso to set
	 */
	public void setCodigoIngreso(int codigoIngreso)
	{
		this.codigoIngreso = codigoIngreso;
	}

	/**
	 * Sets the codigoTipoRegimen.
	 * @param codigoTipoRegimen The codigoTipoRegimen to set
	 */
	public void setCodigoTipoRegimen(char codigoTipoRegimen)
	{
		this.codigoTipoRegimen = codigoTipoRegimen;
	}

	/**
	 * Sets the codigoUltimaViaIngreso.
	 * @param codigoUltimaViaIngreso The codigoUltimaViaIngreso to set
	 */
	public void setCodigoUltimaViaIngreso(int codigoUltimaViaIngreso)
	{
		this.codigoUltimaViaIngreso = codigoUltimaViaIngreso;
	}

	/**
	 * Sets the convenioPersonaResponsable.
	 * @param convenioPersonaResponsable The convenioPersonaResponsable to set
	 */
	public void setConvenioPersonaResponsable(
		String convenioPersonaResponsable
	)
	{
		this.convenioPersonaResponsable = convenioPersonaResponsable;
	}

	/**
	 * Sets the ultimaViaIngreso.
	 * @param ultimaViaIngreso The ultimaViaIngreso to set
	 */
	public void setUltimaViaIngreso(String ultimaViaIngreso)
	{
		this.ultimaViaIngreso = ultimaViaIngreso;
	}

	/**
	 * Establece la institución a la que pertenece esta p.ersona
	 * @param tipoInstitucion la institución a establecer
	 */
	public void setTipoInstitucion(String tipoInstitucion)
	{
		this.tipoInstitucion = tipoInstitucion;
	}	

	/**
	 * @param tipoCentroAtencion The tipoCentroAtencion to set.
	 */
	public void setTipoCentroAtencion(String tipoCentroAtencion) {
		this.tipoCentroAtencion = tipoCentroAtencion;
	}

	/**
	 * Establece la fecha de ingreso del paciente
	 * @param la fecha de ingreso del paciente
	 */
	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
	/**
	 * Observable al cual está registrado este Observer.
	 * @param observable observable de este observer
	 */
	public void setObservable(Observable observable)
	{
		this.observable = observable;
	}
	
	/**
	 * Este metodo inicializa en valores vacios (mas no nulos) los atributos de la persona.
	 */
	public void clean()
	{
		this.tipoIdentificacionPersona			= "";
		this.codigoPersona						= -1;
		this.codigoTipoIdentificacionPersona	= "";
		this.numeroIdentificacionPersona		= "";
		this.nombrePersona						= "";
		this.apellidosNombresPersona			= "";
		this.edad								= -1;
		this.edadDetallada						= "";
		this.codigoSexo							= -1;
		this.sexo								= "";
		this.tipoSangre							= "";
		this.fechaIngreso                   = "";
		this.horaIngreso = "";
		this.consecutivoIngreso = "";
		this.email = "";
		this.codigoGrupoPoblacional = "";
		this.nombreGrupoPoblacional = "";
		this.tipoIdResponPaciente = "";
		this.numeroIdResponPaciente = "";

		this.historiaClinica = "";
		this.cama     = "";
		this.area     = "";
        this.codigoArea=ConstantesBD.codigoNuncaValido;

		this.anioNacimiento     = "1990";
		this.mesNacimiento	    = "1";
		this.diaNacimiento	    = "1";

		
		this.codigoContrato=0;
		this.codigoConvenio=0;
		this.nombrePaisVivienda = "";
		this.nombreCiudadVivienda = "";
		this.nombreDeptoVivienda = "";
		this.codigoPaisVivienda = "";
		this.codigoCiudadVivienda = "";
		this.codigoDeptoVivienda = "";
		this.codigoCentroAtencionPYP = 0;
		this.nombreCentroAtencionPYP = "";
		this.cuentaActivaConvenioPyP = false;
		this.convenioCapitado = false;
		this.hospitalDia = false;
		this.pacEntidadSubcontratada = "";
		this.codigoTipoPaciente = "";
		this.nombreTipoPaciente = "";
		
		this.nombrePaisExpedicion = "";
		this.nombreDepartamentoExpedicion = "";
		this.nombreCiudadExpedicion = "";
		this.cuentasPacienteArray = new ArrayList();
	
		this.antecedentesAlerta = new ArrayList<DtoAntecedentesAlerta>();
		
		/**Campos para guardar en caso de que no existan en usuarios capitados Anexo 922*/	 
		this.clasificacionSE="";
		this.esCapitado=false;
		this.numeroFicha="";
		this.tipoIdEmpleador="";
		this.numIdEmpleador="";
		this.razonSociEmpleador="";
		this.tipoIdCotizante="";
		this.numIdCotizante="";
		this.nombresCotizante="";
		this.apellidosCotizante="";
		this.parentesco=ConstantesBD.codigoNuncaValido;
		this.centroAtencionInt=ConstantesBD.codigoNuncaValido;
		this.tipoAfiliado="";
		this.excepcionMonto=""; 	
		this.pais="";
		this.departamento="";
		this.municipio="";
		this.localidad="";
		this.barrio="";
		this.fechaNacimientoTipoString="";
		this.lineaInconsCamposPersona=ConstantesBD.codigoNuncaValido;
		this.estadoEmbarazada=false;
		this.fechaMuerte="";
		
		//Ahora limpiamos los propios del paciente
		cleanPaciente();
	}

	/**
	 * Este metodo limpia en valores vacios (mas no nulos) los atributos del paciente.
	 */
	public void cleanPaciente()
	{
		this.convenioPersonaResponsable     = "";
		this.codigoTipoRegimen			    = ' ';
		this.nombreTipoRegimen 				= "";
		this.codigoIngreso				    = 0;
		this.codigoCuenta				    = 0;
		this.cuentasPacienteArray = new ArrayList();
		this.codigoAdmision				    = 0;
		this.anioAdmision				    = 0;
		this.ultimaViaIngreso			    = "";
		this.codigoUltimaViaIngreso		    = 0;
		this.fechaIngreso                   = "";
		this.horaIngreso = "";

		
		this.tipoEvento						="";
		this.nombreTipoEvento				="";		
		
		this.codigoContrato=0;
		this.codigoConvenio=0;
		this.existeAsocio=false;
		this.codigoCuentaAsocio=0;
		this.estaContratoVencido=false;
		this.transplante="";
		this.nombreTransplante="";
		this.listadoOtrosIngresosPaciente = new ArrayList<DtoOtrosIngresosPaciente>();
		this.listadoTodosIngresosPaciente = new ArrayList<DtoOtrosIngresosPaciente>();
		/*this.etnia = 0;
		this.estudio = 0;
		this.lee_escribe = false;*/
				
	}

	/**
	 * Implementación de la interfaz Observer, sirve para avisarle
	 * a una instancia de este objeto que debe re-cargar sus datos
	 * desde la fuente de datos.
	 * @param o Objeto observado
	 * @param arg Código de la persona
	 * que cambió
	 */
	public void update(Observable o, Object arg)
	{
		int li_codigoPersona;
		int idIngreso = ConstantesBD.codigoNuncaValido;
		
		String[] vector = arg.toString().split(ConstantesBD.separadorSplit);

		li_codigoPersona = Integer.parseInt(vector[0]);
		
		if(vector.length>1)
		{
			idIngreso = Integer.parseInt(vector[1]);
		}

		logger.info("id ingreso "+idIngreso);
		
		if(li_codigoPersona == codigoPersona)
		{
			try
			{
				Connection ac_con;

				ac_con = DaoFactory.getDaoFactory(System.getProperty("TIPOBD") ).getConnection();

				cargar(ac_con, li_codigoPersona);
				if(idIngreso<=0)
				{
					cargarPaciente(ac_con, li_codigoPersona);
				}
				else
				{
					cargarPacienteXingreso(ac_con, idIngreso+"", this.tipoInstitucion, this.tipoCentroAtencion);
				}
				
				//En ocasiones quedaba abierta, con las revisiones
				//el tiempo alcanza
				if (ac_con!=null&&!ac_con.isClosed())
				{
					UtilidadBD.closeConnection(ac_con);
				}
			}
			catch(SQLException sqle_e)
			{
			}
		}
	}

	/**
	 * Listener que le indica a este objeto cuándo fue añadido a una sesión.
	 * @param event objeto que encapsula este evento
	 */
	public void valueBound(HttpSessionBindingEvent event){}

	/**
	 * Listener que le indica a este objeto cuándo fue removido de una sesión.
	 * @param event objeto que encapsula este evento
	 */
	public void valueUnbound(HttpSessionBindingEvent event)
	{
		// Cuando me remueven de un session, bien sea explícitamente (session.removeAttribute()),
		// por un logout (session.invalidate()), o por timeout, me des-registro como Observer
		if(observable != null)
			observable.deleteObserver(this);
	}

	/**
	 * Carga una <code>PersonaBasica</code> desde una fuente de datos, buscando
	 * por el id suministrado. Establece los atributos del objeto con los datos
	 * cargados.
	 * @param con una conexion abierta con una fuente de datos
	 * @param id pareja tipo/numero de identificación de la persona básica
	 */
	public void cargar(Connection con, TipoNumeroId id)	throws SQLException
	{
		clean();

		//ResultSetDecorator rs = personaBasicaDao.cargar(con, id.getTipoId(), id.getNumeroId(), this);

		personaBasicaDao.cargar(con, id.getTipoId(), id.getNumeroId(), this);
		
		/*if(rs.next())
		{
			this.llenarEnCarga(rs);
			String fechaSistema = UtilidadFecha.getFechaActual(con);
			String fechaNacimiento = UtilidadFecha.conversionFormatoFechaAAp(this.fechaNacimiento);
			this.edadMeses = UtilidadFecha.numeroMesesEntreFechas(fechaNacimiento, fechaSistema,false);
			this.edadDias = UtilidadFecha.numeroDiasEntreFechas(fechaNacimiento, fechaSistema);
		}
		rs.close();*/
	}

	
	public boolean cargar(Connection con, int codigoPersona) throws SQLException
	{
		clean();

		//ResultSetDecorator rs = personaBasicaDao.cargar(con, this);
		
		return personaBasicaDao.cargar(con, codigoPersona, this);
		
		/*if(rs.next())
		{
			this.llenarEnCarga(rs);
			String fechaSistema = UtilidadFecha.getFechaActual(con);
			if(!UtilidadTexto.isEmpty(this.fechaNacimiento))
			{
				String fechaNacimiento = UtilidadFecha.conversionFormatoFechaAAp(this.fechaNacimiento);
				this.edadMeses = UtilidadFecha.numeroMesesEntreFechas(fechaNacimiento, fechaSistema,false);
				this.edadDias = UtilidadFecha.numeroDiasEntreFechas(fechaNacimiento, fechaSistema);
			}
			return true;
		}
		rs.close();*/
	}
	
	
	
	public boolean cargarObjeto(Connection con, int codigoPersona) throws SQLException
	{
		clean();

		//ResultSetDecorator rs = personaBasicaDao.cargar(con, codigoPersona);
		this.codigoPersona = codigoPersona;
		personaBasicaDao.cargarObjeto(con, this);

		/*
		if(rs.next())
		{
			this.llenarEnCarga(rs);
			String fechaSistema = UtilidadFecha.getFechaActual(con);
			if(!UtilidadTexto.isEmpty(this.fechaNacimiento))
			{
				String fechaNacimiento = UtilidadFecha.conversionFormatoFechaAAp(this.fechaNacimiento);
				this.edadMeses = UtilidadFecha.numeroMesesEntreFechas(fechaNacimiento, fechaSistema,false);
				this.edadDias = UtilidadFecha.numeroDiasEntreFechas(fechaNacimiento, fechaSistema);
			}
			return true;
		}
		rs.close();
		*/
		
		return false;
	}
	
	
	
	
	
	/**
	 * Consulta los Ingresos que poseea el paciente cargado en session, muestra los ingresos
	 * sin importar su estado abierto o cerrado, valida que las cuentas al ingreso sean Cuenta valida
	 *  
	 * @param int codigoPaciente 
	 * */
	public void cargarOtrosIngresosPaciente(Connection con, int codigoPersona)
	{
		this.listadoOtrosIngresosPaciente = personaBasicaDao.cargarOtrosIngresosPaciente(con, codigoPersona);		
//		logger.info("\n\nlistado ingresos paciente:: "+this.listadoOtrosIngresosPaciente+"");
	}

	/**
	 * Consulta los Ingresos que poseea el paciente cargado en session, muestra los ingresos
	 * sin importar su estado abierto o cerrado, no valida estados de cuenta
	 *
	 * @param con Conexion con la BD
	 * @param int codigoPaciente 
	 * @return Retorna boolean, true en caso de que tuviera al menos un ingreso, false de lo contrario
	 */
	public boolean cargarTodosIngresos(Connection con, int codigoPaciente)
	{
		this.listadoTodosIngresosPaciente=personaBasicaDao.consultaTodosIngresos(con, codigoPaciente);
		if(listadoTodosIngresosPaciente.size()>0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	
	public ArrayList<DtoOtrosIngresosPaciente> getListadoTodosIngresosPaciente() {
		return listadoTodosIngresosPaciente;
	}

	public void setListadoTodosIngresosPaciente(
			ArrayList<DtoOtrosIngresosPaciente> listadoTodosIngresosPaciente) {
		this.listadoTodosIngresosPaciente = listadoTodosIngresosPaciente;
	}

	/**
	* Carga la informacion del Ingreso dado por la busqueda de Otros Ingresos
	* @param Connection con
	* @param String ingreso 
	*/
	public void cargarPacienteXingreso(Connection ac_con, String ingreso, String codigoInstitucion,String codigoCentroAtencion)	
	{
		logger.info("\n ingreso -->"+ingreso+"   codigoInstitucion-->"+codigoInstitucion+"  -->"+codigoCentroAtencion);
		try
		{
			cargarPaciente2(
				ac_con,
				personaBasicaDao.cargarPacienteXingreso(ac_con, ingreso),
				codigoInstitucion,codigoCentroAtencion			
			);
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Metodo para consultar si un ingreso esta marcado como Preingreso Prendiente
	 * @param codigoIngreso
	 * @return
	 */
	public boolean consultarPreingresoP(int codigoIngreso)
	{
		return personaBasicaDao.consultarPreingresoP(codigoIngreso);
	}
	
	/**
	 * Metodo que consulta si un Ingreso es un Reingreso
	 * @param codigoIngreso
	 * @return
	 */
	public boolean consultarReingreso(int codigoIngreso)
	{
		return personaBasicaDao.consultarReingreso(codigoIngreso);
	}
	
	
	private void llenarEnCarga (ResultSetDecorator rs ) throws SQLException
	{
		this.codigoPersona=rs.getInt("codigoPersona");
		this.tipoIdentificacionPersona =rs.getString("tipoIdentificacionPersona");
		this.codigoTipoIdentificacionPersona =rs.getString("codTipoIdentificacionPer");
		this.numeroIdentificacionPersona =rs.getString("numeroIdentificacionPersona");
		this.nombrePersona				 = rs.getString("nombrePersona");
		this.apellidosNombresPersona     = rs.getString("nombrePersona2");
		this.codigoSexo					 = rs.getInt("codigoSexo");
		this.sexo						 = rs.getString("sexo");
		this.tipoSangre					 = rs.getString("tipoSangre");
		
		this.primerNombre=rs.getString("primer_nombre");
		this.segundoNombre=rs.getString("segundo_nombre");
		this.primerApellido=rs.getString("primer_apellido");
		this.segundoApellido=rs.getString("segundo_apellido");
		
		this.direccion=rs.getString("direccion");
		this.telefono=rs.getString("telefono");
		this.telefonoFijo=rs.getString("telefonofijo");
		this.telefonoCelular=rs.getString("telefonocelular");
		this.fechaNacimiento=rs.getString("fechaNacimiento");
		this.email=rs.getString("email");

		if(!UtilidadTexto.isEmpty(this.fechaNacimiento))
		{
			StringTokenizer fechaTokenizer   =
				new StringTokenizer(this.fechaNacimiento, "-");
			this.anioNacimiento     = fechaTokenizer.nextToken();
			this.mesNacimiento	    = fechaTokenizer.nextToken();
			this.diaNacimiento	    = fechaTokenizer.nextToken();
		}
		String fechaMuerte = rs.getString("fecha_muerte");	
		this.fechaMuerte=fechaMuerte;
		this.setHistoriaClinica(rs.getString("historia_clinica"));						
		
		if(fechaMuerte==null||fechaMuerte.equals(""))
		{
			this.edad =
				Persona.calcularEdad(
					diaNacimiento,
					mesNacimiento,
					anioNacimiento
				);
			this.edadDetallada =
				Persona.calcularEdadDetallada(
					anioNacimiento,
					mesNacimiento,
					diaNacimiento
				);
		}
		else
		{
			int diaMuerte = Integer.parseInt(fechaMuerte.split("/")[0]);
			int mesMuerte = Integer.parseInt(fechaMuerte.split("/")[1]);
			int anioMuerte = Integer.parseInt(fechaMuerte.split("/")[2]);
			
			this.edad =
				UtilidadFecha.calcularEdad(
						diaNacimiento,
						mesNacimiento,
						anioNacimiento,
						diaMuerte,
						mesMuerte,
						anioMuerte
				);
			this.edadDetallada =
				UtilidadFecha.calcularEdadDetallada(
					Integer.parseInt(anioNacimiento),
					Integer.parseInt(mesNacimiento),
					Integer.parseInt(diaNacimiento),
					diaMuerte+"",
					mesMuerte+"",
					anioMuerte+""
				);
		}
		
		this.nombreCiudadVivienda = rs.getString("ciudad");
		this.nombreDeptoVivienda = rs.getString("depto");
		this.nombrePaisVivienda = rs.getString("pais");
		
		this.codigoCiudadVivienda = rs.getString("codigo_ciudad");
		this.codigoDeptoVivienda = rs.getString("codigo_depto");
		this.codigoPaisVivienda = rs.getString("codigo_pais");
		
		this.nombrePaisExpedicion = rs.getString("pais_expedicion");
		this.nombreCiudadExpedicion = rs.getString("ciudad_expedicion");
		this.nombreDepartamentoExpedicion = rs.getString("depto_expedicion");
		this.barrio=rs.getString("barrio")+"#"+rs.getString("codigo_barrio");
		
		this.setCodigoCentroAtencionPYP(rs.getInt("codigo_centro_atencion_pyp"));
		this.setNombreCentroAtencionPYP(rs.getString("nombre_centro_atencion_pyp"));
		
		this.setLee_escribe( UtilidadTexto.getBoolean(rs.getString("lee_escribe")) );
		
		
		this.setEstudio( rs.getInt("estudio") );
		this.setEtnia( rs.getInt("etnia") );
		
		this.setCodigoGrupoPoblacional(rs.getString("grupo_poblacional"));
		this.setNombreGrupoPoblacional(ValoresPorDefecto.getIntegridadDominio(this.getCodigoGrupoPoblacional())+"");
		
		//************************** Antecedentes de Alerta *******************************************
		this.setAntecedentesAlerta(UtilidadOdontologia.obtenerAntecedentesAlerta(this.getCodigoPersona()));
		//************************** Antecedentes de Alerta *******************************************
		
		//Se adiciona clasificacion SE, tipoAfiliado y nro_carnet(numero_ficha) 
		/*this.setClasificacionSE(rs.getString("clasificacionse"));
		this.setTipoAfiliado(rs.getString("tipoafiliado"));
		this.setNumeroFicha(rs.getString("numerocarnet"));*/
	}


	/**
	 * Sobrecarga del método anterior, solamente recibe como parámetro la
	 * conexión y usa el estado interno de los atributos de este objeto para
	 * cargar una persona básica.
	 * @param con una conexion abierta con una fuente de datos
	 */
	public void cargar(Connection con)
	throws SQLException
	{
		cargar(
			con,
			new TipoNumeroId(
				this.codigoTipoIdentificacionPersona,
				this.numeroIdentificacionPersona
			)
		);
	}

	/**
	 * Este método carga un Paciente y todos los datos que se necesiten del
	 * mismo para visualizar y demás. Esta separada de la función cargar porque
	 * muchos de estos datos a veces existen (lo que exige varias consultas) y
	 * además porque la funcionalidad NO es para todas las personas, sino para
	 * los pacientes en particular
	 *
	 * @param con una conexion abierta con una fuente de datos
	 * @throws SQLException
	 */
	private void cargarPaciente(Connection ac_con, int ai_codigoPersona)
	throws SQLException
	{
		cargarPaciente(ac_con, ai_codigoPersona, this.tipoInstitucion,this.tipoCentroAtencion);
	}
	

	/**
	* Este método carga un Paciente y todos los datos que se necesiten del
	* mismo para visualizar y demás. Esta separada de la función cargar porque
	* muchos de estos datos a veces existen (lo que exige varias consultas) y
	* además porque la funcionalidad NO es para todas las personas, sino para
	* los pacientes en particular. También, establece el valor de tipoInstitucion
	* de este paciente. (Este método cargaba de forma tradicional, usando un
	* arreglo, mientras se probaba la funcionalidad usando una tabla de Hash
	* (cargarPaciente2), como esta funcionó correctamente, ahora cargar es
	* simplemente una referencia a cargarPaciente2)
	*
	* @param con una conexion abierta con una fuente de datos
	* @throws SQLException
	*/
	public void cargarPaciente(Connection con, int ai_codigoPersona, String codigoInstitucion,String codigoCentroAtencion)
	throws SQLException
	{
		this.cargarPaciente2(con, ai_codigoPersona, codigoInstitucion,codigoCentroAtencion);
	}

	/**
	* Este método carga un Paciente y todos los datos que se necesiten del
	* mismo para visualizar y demás. Esta separada de la función cargar porque
	* muchos de estos datos a veces existen (lo que exige varias consultas) y
	* además porque la funcionalidad NO es para todas las personas, sino para
	* los pacientes en particular. También, establece el valor de tipoInstitucion
	* de este paciente. (Este método cargaba de forma tradicional, usando un
	* arreglo, mientras se probaba la funcionalidad usando una tabla de Hash
	* (cargarPaciente2), como esta funcionó correctamente, ahora cargar es
	* simplemente una referencia a cargarPaciente2)
	*
	* @param con una conexion abierta con una fuente de datos
	* @throws SQLException
	*/
	public void cargarPaciente(Connection con, TipoNumeroId id, String codigoInstitucion,String codigoCentroAtencion)
	throws SQLException
	{
		this.cargarPaciente2(con, id, codigoInstitucion,codigoCentroAtencion);
	}

	/**
	* Este método carga un Paciente y todos los datos que se necesiten del
	* mismo para visualizar y demás. Esta separada de la función cargar porque
	* muchos de estos datos a veces existen (lo que exige varias consultas) y
	* además porque la funcionalidad NO es para todas las personas, sino para
	* los pacientes en particular. También, establece el valor de tipoInstitucion
	* de este paciente.
	*
	* @param con una conexion abierta con una fuente de datos
	* @throws SQLException
	*/
	public void cargarPaciente2(Connection ac_con, int ai_codigoPersona, String codigoInstitucion,String codigoCentroAtencion)
	throws SQLException
	{
		cargarPaciente2(
			ac_con,
			personaBasicaDao.cargarPaciente2(ac_con, ai_codigoPersona, codigoInstitucion,codigoCentroAtencion,true),
			codigoInstitucion,codigoCentroAtencion
		);
	}
	
	/**
	 * Este método carga un Paciente y todos los datos que se necesiten del
	 * mismo para visualizar y demás. Esta separada de la función cargar porque
	 * muchos de estos datos a veces existen (lo que exige varias consultas) y
	 * además porque la funcionalidad NO es para todas las personas, sino para
	 * los pacientes en particular. También, establece el valor de tipoInstitucion
	 * de este paciente.
	 *
	 * @param con una conexion abierta con una fuente de datos
	 * @throws SQLException
	 */
	public void cargarPaciente2(
		Connection ac_con,
		TipoNumeroId atni_id,
		String codigoInstitucion,
		String codigoCentroAtencion
	)
	throws SQLException
	{
		cargarPaciente2(
			ac_con,
			personaBasicaDao.cargarPaciente2(
				ac_con,
				atni_id.getTipoId(),
				atni_id.getNumeroId(),
				codigoInstitucion,
				codigoCentroAtencion,
				true
			),
			codigoInstitucion,
			codigoCentroAtencion
		);
	}
	
	
	

	private void cargarPaciente2(Connection ac_con, Hashtable aht_h, String codigoInstitucion,String codigoCentroAtencion)
	throws SQLException
	{
		String	ls_tmp;

		tipoInstitucion = codigoInstitucion;
		tipoCentroAtencion = codigoCentroAtencion;

		cleanPaciente();
		this.cargarOtrosIngresosPaciente(ac_con, this.codigoPersona);
		if(aht_h != null)
		{
			/*
			* 0	codigoTipoRegimen
			* 1	convenioPersonaResponsable
			* 2	codigoIngreso
			* 3	codigoCuenta
			* 4	codigoAdmision
			* 5	anioAdmision
			* 6 fechaIngreso
			* 7 horaIngreso
			* 8	ultimaViaIngreso
			* 9	codigoUltimaViaIngreso
			*/

			/*
				Por convención si un dato no existe el cargar lo devuelve en "".
				Primero sacamos el codigoTipoRegimen
			*/
			ls_tmp = (String)aht_h.get("codigoTipoRegimen");

			if( (ls_tmp != null) && !ls_tmp.equals(""))
				codigoTipoRegimen = ls_tmp.charAt(0);
			
			ls_tmp = (String)aht_h.get("nombreTipoRegimen");

			if( (ls_tmp != null) && !ls_tmp.equals(""))
				nombreTipoRegimen = ls_tmp;

			convenioPersonaResponsable =
				(String)aht_h.get("convenioPersonaResponsable");

			ls_tmp = (String)aht_h.get("codigoIngreso");
			
			logger.info("EL CODGIO DEL INGRESO ES===============> "+ls_tmp);
			
			if((ls_tmp != null) && !ls_tmp.equals(""))
			{
				codigoIngreso = Integer.parseInt(ls_tmp);
				consecutivoIngreso = aht_h.get("consecutivoIngreso").toString();
			}

			ls_tmp = (String)aht_h.get("codigoCuenta");
			if((ls_tmp != null) && !ls_tmp.equals(""))
			{
				codigoCuenta = Integer.parseInt(ls_tmp);
			}
			
			ls_tmp = (String)aht_h.get("codigoCuenta");
			if((ls_tmp != null) && !ls_tmp.equals(""))
			{
				codigoCuenta = Integer.parseInt(ls_tmp);
			}
						
			if((aht_h.get("cuentasPaciente") != null) && !aht_h.get("cuentasPaciente").toString().equals(""))
			{				
				cuentasPacienteArray = (ArrayList)aht_h.get("cuentasPaciente");
			}
			
			ls_tmp = (String)aht_h.get("tipoIdResponsable");
			if((ls_tmp != null) && !ls_tmp.equals(""))
				tipoIdResponPaciente = ls_tmp;
			
			
			ls_tmp = (String)aht_h.get("idResponsable");
			if((ls_tmp != null) && !ls_tmp.equals(""))
				numeroIdResponPaciente = ls_tmp;
				
			
			ls_tmp = (String)aht_h.get("codigoAdmision");

			if((ls_tmp != null) && !ls_tmp.equals(""))
				codigoAdmision = Integer.parseInt(ls_tmp);

			ls_tmp = (String)aht_h.get("anioAdmision");

			if((ls_tmp != null) && !ls_tmp.equals(""))
				anioAdmision = Integer.parseInt(ls_tmp);
			
			this.fechaIngreso = (String)aht_h.get("fechaIngreso");
			this.horaIngreso = (String)aht_h.get("horaIngreso");
			
			ultimaViaIngreso = (String)aht_h.get("viaIngreso");

			//Caso en el que no tiene via de ingreso
			if((ultimaViaIngreso == null) || ultimaViaIngreso.equals(""))
				ultimaViaIngreso = "Ninguno";
			
			ls_tmp = (String)aht_h.get("codigoViaIngreso");

			if((ls_tmp != null) && !ls_tmp.equals(""))
				codigoUltimaViaIngreso = Integer.parseInt(ls_tmp);
			
			logger.info("LA ULTIMA VIA DE INGRESO ES===============> "+codigoUltimaViaIngreso);
			
			
			//Si tenemos una admisión de urgencias u hospitalizacion obtenemos el dato de la cama de la admisión
			if(
				(this.codigoUltimaViaIngreso == ConstantesBD.codigoViaIngresoHospitalizacion) ||
				(this.codigoUltimaViaIngreso == ConstantesBD.codigoViaIngresoUrgencias)
			)
			{

				this.cama =
					Admision.getCama(
						ac_con,
						this.codigoAdmision,
						this.codigoUltimaViaIngreso,
						this.codigoPersona
					);
				/*String a[]=Admision.getCamaCompleta(
						ac_con,
						this.codigoAdmision,
						this.codigoUltimaViaIngreso
					);*/
				this.codigoCama =
					Integer.parseInt(Admision.getCamaCompleta(
						ac_con,
						this.codigoAdmision,
						this.codigoUltimaViaIngreso,
						this.codigoPersona
					)[2]);

			//Si es consulta externa o ambulatorios esta pendiente por definir
			}
			else
			{
				this.cama = "";
			}			
			
			
			////////////////////////////////////////////////////////////////////////////////
			///adicionado por anexo 655 transplante
			if (UtilidadCadena.noEsVacio(aht_h.get("transplante")+""))
			{
				this.setTransplante(aht_h.get("transplante")+"");
				this.setNombreTransplante(ValoresPorDefecto.getIntegridadDominio(this.getTransplante())+"");
			}
			else
			{
				this.setTransplante("");
				this.setNombreTransplante("");
			}
			////////////////////////////////////////////////////////////////////////////////////
			
			//Una vez revisado lo del contrato y convenio miramos
			//el asocio de la cuenta
			ls_tmp=(String)aht_h.get("esCuentaAsociada");
			if (ls_tmp!=null&&ls_tmp.equals("true"))
			{
				this.existeAsocio=true;
				ls_tmp=(String)aht_h.get("codigoCuentaAsociada");

				if (ls_tmp!=null&&!ls_tmp.equals(""))
				{
					this.codigoCuentaAsocio=Integer.parseInt(ls_tmp);
				}
				else
				{
					this.codigoCuentaAsocio=0;
				}
			}
			else
			{
				this.existeAsocio=false;
			}
			logger.info("Paso por aca %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%");
			//Si la cuenta es mayor que 0 revisamos tarifario, convenio, contrato
			//Después revisamos los datos de asocio de cuenta / cuentaAsociada
			if (codigoCuenta >0)
			{
				//Obteniendo el area y cama del paciente
				//Dado el idCuenta, obtenemos el nombre del centro de costo de los medicos tratantes
				logger.info("APSO POR ACÁ===============> "+codigoCuenta);
				String datosArea = UtilidadValidacion.getAreaPaciente(ac_con,this.codigoCuenta);
				this.area = datosArea.split(ConstantesBD.separadorSplit)[1];
	            //obteniendo el codigo del area
	            this.codigoArea= Integer.parseInt(datosArea.split(ConstantesBD.separadorSplit)[0]);
	            
				
				//No tiene else porque en este caso se debe cargar
				//el último tarifario
				
				ls_tmp = (String)aht_h.get("codigoConvenio");
				if (!ls_tmp.equals(""))
				{
					this.codigoConvenio=Integer.parseInt(ls_tmp);
				}
				else
				{
					this.codigoConvenio=0;
				}
				
				ls_tmp = (String)aht_h.get("codigoContrato");
				if (!ls_tmp.equals(""))
				{
					this.codigoContrato=Integer.parseInt(ls_tmp);
					this.estaContratoVencido=false;
				}
				else
				{
					//Si el contrato es vacío debemos cargar el último y decir que
					//estaba vencido
					this.estaContratoVencido=true;
					int arregloVencido=personaBasicaDao.getCodigoUltimoContrato( ac_con, this.codigoConvenio);
					this.codigoContrato=arregloVencido;
				}
				//-Verificar que el paciente es PYP
				this.cuentaActivaConvenioPyP = UtilidadValidacion.tieneCuentaPacienteConvenioPyP(ac_con, this);
				this.convenioCapitado = UtilidadValidacion.esConvenioCapitado(ac_con,this.codigoConvenio);
				this.hospitalDia = UtilidadTexto.getBoolean(aht_h.get("hospitalDia").toString());
				this.codigoTipoPaciente = aht_h.get("codigoTipoPaciente").toString();
				this.nombreTipoPaciente = aht_h.get("nombreTipoPaciente").toString();
			}
			
			this.setManejoConjunto(personaBasicaDao.tieneManejoConjunto(ac_con, this.getCodigoPersona()));
			
			this.setTipoEvento((String)aht_h.get("tipoEvento"));
			this.setNombreTipoEvento(ValoresPorDefecto.getIntegridadDominio((String)aht_h.get("tipoEvento")).toString());
			
			//Se toma el registro de pacientes entidades subcontratadas
			if(!UtilidadTexto.isEmpty(aht_h.get("pacEntidadSubcontratada")+""))
				this.setPacEntidadSubcontratada((String)aht_h.get("pacEntidadSubcontratada"));
		}
		//Si no se encontró información de un ingreso abierto, pero el paciente 
		//tiene un ingreso cerrado, entonces se carga automáticamente
		if(this.codigoIngreso<=0&&this.listadoOtrosIngresosPaciente.size()==1)
		{
			DtoOtrosIngresosPaciente otroIngreso = (DtoOtrosIngresosPaciente)this.listadoOtrosIngresosPaciente.get(0);
			
			if(otroIngreso.getCentroAtencion().equals(codigoCentroAtencion))
				cargarPacienteXingreso(ac_con, otroIngreso.getIngreso(), codigoInstitucion, codigoCentroAtencion);
		}
		
		/**
		 * Se cargas todos los ingresos paciente para mostrarlos
		 * en el link de ingresos previos del paciente en el encabezado
		 * MT-1846. diecorqu
		 */
		this.cargarTodosIngresos(ac_con, this.codigoPersona);
	}
	
	
	public void cargarEmpresaRegimen(Connection con, int cod,String codRegimen) throws SQLException
	{
		
		personaBasicaDao.cargarEmpresaRazon(con,cod,codRegimen, this);
		/*ResultSetDecorator rs;
		rs=personaBasicaDao.cargarEmpresaRazon(con,cod,codRegimen, this);
		rs.next();
		this.nombreEmpresa=rs.getString("empresa");
		this.nombreTipoRegimen=rs.getString("regimen");*/
	}

	
	
	/**
	 * @return
	 */
	public String getSexo()
	{
		return sexo;
	}

	/**
	 * @return
	 */
	public String getTipoSangre()
	{
		return tipoSangre;
	}

	/**
	 * @return
	 */
	public String getArea()
	{
		return area;
	}

	/**
	 * @return
	 */
	public String getCama()
	{
		return cama;
	}

	/**
	 * @param string
	 */
	public void setArea(String string)
	{
		area = string;
	}

	/**
	 * @param string
	 */
	public void setCama(String string)
	{
		cama = string;
	}

	/**
	 * @return
	 */
	public String getAnioNacimiento()
	{
		return anioNacimiento;
	}

	/**
	 * @return
	 */
	public String getDiaNacimiento()
	{
		return diaNacimiento;
	}

	/**
	 * @return
	 */
	public String getMesNacimiento()
	{
		return mesNacimiento;
	}

	/**
	 * Método que calcula la edad de esta persona dada una
	 * fecha particular
	 * @param diaFechaDada Día para el cual se quiere sacar la fecha
	 * @param mesFechaDada Mes para el cual se quiere sacar la fecha
	 * @param anioFechaDada Año para el cual se quiere sacar la fecha
	 * @return
	 */
	public int calcularEdadEnFecha(
		String diaFechaDada,
		String mesFechaDada,
		String anioFechaDada
	)
	{
		return UtilidadFecha.calcularEdad(
			this.diaNacimiento,
			this.mesNacimiento,
			this.anioNacimiento,
			diaFechaDada,
			mesFechaDada,
			anioFechaDada
		);
	}

	/**
	 * Método que calcula la edad de esta persona dada una
	 * fecha particular
	 * @param diaFechaDada Día para el cual se quiere sacar la fecha
	 * @param mesFechaDada Mes para el cual se quiere sacar la fecha
	 * @param anioFechaDada Año para el cual se quiere sacar la fecha
	 * @return
	 */
	public int calcularEdadEnFecha(
		int diaFechaDada,
		int mesFechaDada,
		int anioFechaDada
	)
	{
		return UtilidadFecha.calcularEdad(
			this.diaNacimiento,
			this.mesNacimiento,
			this.anioNacimiento,
			diaFechaDada,
			mesFechaDada,
			anioFechaDada
		);
	}

	/**
	 * Dados el dia, mes y año de nacimiento de una persona,
	 * calcula su edad en la fecha dada, con las siguientes consideraciones :
	 * edad &lt; 1 mes : edad en días
	 * edad &lt; 1 año : edad en meses y días
	 * 1 año &lt;= edad &lt;= 5 años : edad en años y meses
	 * edad &gt; 5 años : edad en años
	 * @param anioNacimiento año en el cual nació la persona
	 * @param mesNacimiento mes en el cual nació la persona
	 * @param diaNacimiento día en el cual nació la persona
	 * @param diaComparacion Dia con respecto al cual se desea sacar la edad
	 * @param mesComparacion Mes con respecto al cual se desea sacar la edad
	 * @param anioComparacion Año con respecto al cual se desea sacar la edad
	 * @return la edad de la persona, como se explicó arriba
	 */
	public String calcularEdadEnFechaDetallada(
		int diaFechaDada,
		int mesFechaDada,
		int anioFechaDada
	)
	{
		return UtilidadFecha.calcularEdadDetallada(
			Integer.parseInt(this.anioNacimiento),
			Integer.parseInt(this.mesNacimiento),
			Integer.parseInt(diaNacimiento),
			diaFechaDada,
			mesFechaDada,
			anioFechaDada
		);
	}

	/**
	 * Dados el dia, mes y año de nacimiento de una persona,
	 * calcula su edad en la fecha dada, con las siguientes consideraciones :
	 * edad &lt; 1 mes : edad en días
	 * edad &lt; 1 año : edad en meses y días
	 * 1 año &lt;= edad &lt;= 5 años : edad en años y meses
	 * edad &gt; 5 años : edad en años
	 * @param anioNacimiento año en el cual nació la persona
	 * @param mesNacimiento mes en el cual nació la persona
	 * @param diaNacimiento día en el cual nació la persona
	 * @param diaComparacion Dia con respecto al cual se desea sacar la edad
	 * @param mesComparacion Mes con respecto al cual se desea sacar la edad
	 * @param anioComparacion Año con respecto al cual se desea sacar la edad
	 * @return la edad de la persona, como se explicó arriba
	 */
	public String calcularEdadEnFechaDetallada(
		String diaFechaDada,
		String mesFechaDada,
		String anioFechaDada
	)
	{
		return UtilidadFecha.calcularEdadDetallada(
			Integer.parseInt(this.anioNacimiento),
			Integer.parseInt(this.mesNacimiento),
			Integer.parseInt(diaNacimiento),
			diaFechaDada,
			mesFechaDada,
			anioFechaDada
		);
	}

	/** Obtiene la fecha de nacimiento de la persona */
	public String getFechaNacimiento()
	{
		return diaNacimiento + "/" + mesNacimiento + "/" + anioNacimiento;
	}
	
	/** Obtiene la fecha de nacimiento de la persona */
	public String getFechaNacimientoOriginal()
	{
		return this.fechaNacimiento;
	}

	
	/**
	 * Retorna el código de la persona en la fuente de datos
	 * @return int
	 */
	public int getCodigoPersona()
	{
		return codigoPersona;
	}

	/**
	 * Asigna el código de la persona en la fuente de datos
	 * @param codigoPersona The codigoPersona to set
	 */
	public void setCodigoPersona(int codigoPersona)
	{
		this.codigoPersona = codigoPersona;
	}


	/**
	 * Método que carga el código del convenio
	 * dada una cuenta
	 * 
	 * @param con Conexión con la fuente de datos
	 * @return
	 */
	public int getCodigoConvenio ()
	{
		return codigoConvenio;
	}
	
	/**
	 * Método que carga el código del contrato
	 * M?todo que asigna el c?digo del convenio
	 * dada una cuenta
	 * 
	 * @param codigoConvenio
	 */
	public void setCodigoConvenio (int codigoConvenio)
	{
		this.codigoConvenio = codigoConvenio;
	}
	
	/**
	 * M?todo que carga el c?digo del contrato
	 * dada una cuenta
	 * 
	 * @param con Conexión con la fuente de datos
	 * @return
	 * @throws SQLException
	 */
	public int getCodigoContrato () throws SQLException
	{
		return codigoContrato;
	}

	/**
	 * @return
	 */
	public int getCodigoCuentaAsocio() {
		return codigoCuentaAsocio;
	}

	/**
	 * @return
	 */
	public boolean getEstaContratoVencido() {
		return estaContratoVencido;
	}

	/**
	 * @return
	 */
	public boolean getExisteAsocio() {
		return existeAsocio;
	}

	/**
	 * @return Retorna manejoConjunto.
	 */
	public boolean getManejoConjunto()
	{
		return manejoConjunto;
	}
	/**
	 * @param manejoConjunto Asigna manejoConjunto.
	 */
	public void setManejoConjunto(boolean manejoConjunto)
	{
		this.manejoConjunto = manejoConjunto;
	}
	/**
	 * @return Retorna el nombreTipoRegimen.
	 */
	public String getNombreTipoRegimen() {
		return nombreTipoRegimen;
	}
	/**
	 * @param nombreTipoRegimen Asigna el nombreTipoRegimen.
	 */
	public void setNombreTipoRegimen(String nombreTipoRegimen) {
		this.nombreTipoRegimen = nombreTipoRegimen;
	}
	/**
	 * @return Retorna el nombreEmpresa.
	 */
	public String getNombreEmpresa() {
		return nombreEmpresa;
	}
	/**
	 * @param nombreEmpresa Asigna el nombreEmpresa.
	 */
	public void setNombreEmpresa(String nombreEmpresa) {
		this.nombreEmpresa = nombreEmpresa;
	}
	/**
	 * @return Retorna codigoCama.
	 */
	public int getCodigoCama()
	{
		return codigoCama;
	}
	/**
	 * @param codigoCama Asigna codigoCama.
	 */
	public void setCodigoCama(int codigoCama)
	{
		this.codigoCama = codigoCama;
	}
	
	/**
	 * Método que dado el código de una cuenta devuelve 
	 * la fecha y hora de apertura de la misma, si esta existe
	 * 
	 * @param con Conexión con la fuente de datos 
	 * @param idCuenta Código de la cuenta
	 * @return
	 * @throws SQLException
	 */
	public static String getFechaHoraApertura (Connection con, int idCuenta) throws SQLException
	{
	    return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPersonaBasicaDao().getFechaHoraApertura (con, idCuenta) ;
	}

	/**
	 * @return Retorna primerApellido.
	 */
	public String getPrimerApellido()
	{
		return primerApellido;
	}

	/**
	 * @param primerApellido Asigna primerApellido.
	 */
	public void setPrimerApellido(String primerApellido)
	{
		this.primerApellido = primerApellido;
	}

	/**
	 * @return Retorna primerNombre.
	 */
	public String getPrimerNombre()
	{
		return primerNombre;
	}

	/**
	 * @param primerNombre Asigna primerNombre.
	 */
	public void setPrimerNombre(String primerNombre)
	{
		this.primerNombre = primerNombre;
	}

	/**
	 * @return Retorna segundoApellido.
	 */
	public String getSegundoApellido()
	{
		return segundoApellido;
	}

	/**
	 * @param segundoApellido Asigna segundoApellido.
	 */
	public void setSegundoApellido(String segundoApellido)
	{
		this.segundoApellido = segundoApellido;
	}

	/**
	 * @return Retorna segundoNombre.
	 */
	public String getSegundoNombre()
	{
		return segundoNombre;
	}

	/**
	 * @param segundoNombre Asigna segundoNombre.
	 */
	public void setSegundoNombre(String segundoNombre)
	{
		this.segundoNombre = segundoNombre;
	}

	/**
	 * @return Retorna direccion.
	 */
	public String getDireccion()
	{
		return direccion;
	}

	/**
	 * @param direccion Asigna direccion.
	 */
	public void setDireccion(String direccion)
	{
		this.direccion = direccion;
	}

	/**
	 * @return Retorna telefono.
	 */
	public String getTelefono()
	{
		return telefono;
	}

	/**
	 * @param telefono Asigna telefono.
	 */
	public void setTelefono(String telefono)
	{
		this.telefono = telefono;
	}

	/**
	 * @param fechaNacimiento Asigna fechaNacimiento.
	 */
	public void setFechaNacimiento(String fechaNacimiento)
	{
		this.fechaNacimiento = fechaNacimiento;
	}
    /**
     * @return Returns the codigoArea.
     */
    public int getCodigoArea() {
        return codigoArea;
    }
    /**
     * @param codigoArea The codigoArea to set.
     */
    public void setCodigoArea(int codigoArea) {
        this.codigoArea = codigoArea;
    }

	public String getNombreCiudadVivienda() {
		return nombreCiudadVivienda;
	}

	public void setNombreCiudadVivienda(String nombreCiudadVivienda) {
		this.nombreCiudadVivienda = nombreCiudadVivienda;
	}

	public String getNombreDeptoVivienda() {
		return nombreDeptoVivienda;
	}

	public void setNombreDeptoVivienda(String nombreDeptoVivienda) {
		this.nombreDeptoVivienda = nombreDeptoVivienda;
	}

	public void setAnioNacimiento(String anioNacimiento) {
		this.anioNacimiento = anioNacimiento;
	}

	public void setDiaNacimiento(String diaNacimiento) {
		this.diaNacimiento = diaNacimiento;
	}

	public void setMesNacimiento(String mesNacimiento) {
		this.mesNacimiento = mesNacimiento;
	}

	/**
	 * @return Returns the codigoCentroAtencionPYP.
	 */
	public int getCodigoCentroAtencionPYP() {
		return codigoCentroAtencionPYP;
	}

	/**
	 * @param codigoCentroAtencionPYP The codigoCentroAtencionPYP to set.
	 */
	public void setCodigoCentroAtencionPYP(int codigoCentroAtencionPYP) {
		this.codigoCentroAtencionPYP = codigoCentroAtencionPYP;
	}

	/**
	 * @return Returns the nombreCentroAtencionPYP.
	 */
	public String getNombreCentroAtencionPYP() {
		return nombreCentroAtencionPYP;
	}

	/**
	 * @param nombreCentroAtencionPYP The nombreCentroAtencionPYP to set.
	 */
	public void setNombreCentroAtencionPYP(String nombreCentroAtencionPYP) {
		this.nombreCentroAtencionPYP = nombreCentroAtencionPYP;
	}

	public int getEstudio() {
		return estudio;
	}

	public void setEstudio(int estudio) {
		this.estudio = estudio;
	}

	public int getEtnia() {
		return etnia;
	}

	public void setEtnia(int etnia) {
		this.etnia = etnia;
	}

	public Boolean getLee_escribe() {
		return lee_escribe;
	}

	public void setLee_escribe(Boolean lee_escribe) {
		this.lee_escribe = lee_escribe;
	}   
	
	/**
	 * @return Returns the tieneCuentActivaConvenioPyP.
	 */
	public boolean tieneCuentaActivaConvenioPyP()
	{
		return cuentaActivaConvenioPyP;
	}

	/**
	 * @return Returns the email.
	 */
	public String getEmail() 
	{
		return email;
	}

	/**
	 * @param email The email to set.
	 */
	public void setEmail(String email) 
	{
		this.email = email;
	}

	/**
	 * @return Returns the convenioCapitado.
	 */
	public boolean isConvenioCapitado() {
		return convenioCapitado;
	}

	/**
	 * @param convenioCapitado The convenioCapitado to set.
	 */
	public void setConvenioCapitado(boolean convenioCapitado) {
		this.convenioCapitado = convenioCapitado;
	}

	/**
	 * @return Returns the cuentaActivaConvenioPyP.
	 */
	public boolean isCuentaActivaConvenioPyP() {
		return cuentaActivaConvenioPyP;
	}

	/**
	 * @param cuentaActivaConvenioPyP The cuentaActivaConvenioPyP to set.
	 */
	public void setCuentaActivaConvenioPyP(boolean cuentaActivaConvenioPyP) {
		this.cuentaActivaConvenioPyP = cuentaActivaConvenioPyP;
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
	 * @return the nombrePaisVivienda
	 */
	public String getNombrePaisVivienda() {
		return nombrePaisVivienda;
	}

	/**
	 * @param nombrePaisVivienda the nombrePaisVivienda to set
	 */
	public void setNombrePaisVivienda(String nombrePaisVivienda) {
		this.nombrePaisVivienda = nombrePaisVivienda;
	}
	
	/**
	 * @return the tipoEvento
	 */
	public String getTipoEvento() {
		return tipoEvento;
	}

	/**
	 * @param tipoEvento the tipoEvento to set
	 */
	public void setTipoEvento(String tipoEvento) {
		this.tipoEvento = tipoEvento;
	}

	/**
	 * @return the historiaClinica
	 */
	public String getHistoriaClinica() {
		return historiaClinica;
	}

	/**
	 * @param historiaClinica the historiaClinica to set
	 */
	public void setHistoriaClinica(String historiaClinica) {
		this.historiaClinica = historiaClinica;
	}

	/**
	 * @return the nombreTipoEvento
	 */
	public String getNombreTipoEvento() {
		return nombreTipoEvento;
	}

	/**
	 * @param nombreTipoEvento the nombreTipoEvento to set
	 */
	public void setNombreTipoEvento(String nombreTipoEvento) {
		this.nombreTipoEvento = nombreTipoEvento;
	}

	/**
	 * @return the listadoOtrosIngresosPaciente
	 */
	public ArrayList<DtoOtrosIngresosPaciente> getListadoOtrosIngresosPaciente() {
		return listadoOtrosIngresosPaciente;
	}

	/**
	 * @param listadoOtrosIngresosPaciente the listadoOtrosIngresosPaciente to set
	 */
	public void setListadoOtrosIngresosPaciente(
			ArrayList<DtoOtrosIngresosPaciente> listadoOtrosIngresosPaciente) {
		this.listadoOtrosIngresosPaciente = listadoOtrosIngresosPaciente;
	}
	
	public int getSize()
	{
		return this.listadoOtrosIngresosPaciente.size();
	}
	
	/**
	 * 
	 * @param paciente
	 * @return
	 */
	public static PersonaBasica cargarAsocioEnCuentaPpal(PersonaBasica paciente)
	{
		if(paciente.codigoCuenta<=0 && paciente.getExisteAsocio() && paciente.getCodigoCuentaAsocio()>0)
		{
			paciente.codigoCuenta=paciente.getCodigoCuentaAsocio();
			paciente.codigoCuentaAsocio=0;
			paciente.existeAsocio=false;
		}
		return paciente;
	}
	
	/**
	 * @return the numeroIdResponPaciente
	 */
	public String getNumeroIdResponPaciente() {
		return numeroIdResponPaciente;
	}

	/**
	 * @param numeroIdResponPaciente the numeroIdResponPaciente to set
	 */
	public void setNumeroIdResponPaciente(String numeroIdResponPaciente) {
		this.numeroIdResponPaciente = numeroIdResponPaciente;
	}

	/**
	 * @return the tipoIdResponPaciente
	 */
	public String getTipoIdResponPaciente() {
		return tipoIdResponPaciente;
	}

	/**
	 * @param tipoIdResponPaciente the tipoIdResponPaciente to set
	 */
	public void setTipoIdResponPaciente(String tipoIdResponPaciente) {
		this.tipoIdResponPaciente = tipoIdResponPaciente;
	}

	/**
	 * @param existeAsocio the existeAsocio to set
	 */
	public void setExisteAsocio(boolean existeAsocio) {
		this.existeAsocio = existeAsocio;
	}

	/**
	 * @return the hospitalDia
	 */
	public boolean isHospitalDia() {
		return hospitalDia;
	}

	/**
	 * @param hospitalDia the hospitalDia to set
	 */
	public void setHospitalDia(boolean hospitalDia) {
		this.hospitalDia = hospitalDia;
	}

	/**
	 * @return the consecutivoIngreso
	 */
	public String getConsecutivoIngreso() {
		return consecutivoIngreso;
	}

	/**
	 * @param consecutivoIngreso the consecutivoIngreso to set
	 */
	public void setConsecutivoIngreso(String consecutivoIngreso) {
		this.consecutivoIngreso = consecutivoIngreso;
	}

	/**
	 * @return the pacEntidadSubcontratada
	 */
	public String getPacEntidadSubcontratada() {
		return pacEntidadSubcontratada;
	}
	
	/**
	 * Para verificar si el paciente tiene ingreso de paciente de entidad subcontratada
	 * @return
	 */
	public boolean esIngresoEntidadSubcontratada()
	{
		return pacEntidadSubcontratada.equals("")?false:true;
	}

	/**
	 * @param pacEntidadSubcontratada the pacEntidadSubcontratada to set
	 */
	public void setPacEntidadSubcontratada(String pacEntidadSubcontratada) {
		this.pacEntidadSubcontratada = pacEntidadSubcontratada;
	}

	/**
	 * @return the nombreCiudadExpedicion
	 */
	public String getNombreCiudadExpedicion() {
		return nombreCiudadExpedicion;
	}

	/**
	 * @param nombreCiudadExpedicion the nombreCiudadExpedicion to set
	 */
	public void setNombreCiudadExpedicion(String nombreCiudadExpedicion) {
		this.nombreCiudadExpedicion = nombreCiudadExpedicion;
	}

	/**
	 * @return the nombreDepartamentoExpedicion
	 */
	public String getNombreDepartamentoExpedicion() {
		return nombreDepartamentoExpedicion;
	}

	/**
	 * @param nombreDepartamentoExpedicion the nombreDepartamentoExpedicion to set
	 */
	public void setNombreDepartamentoExpedicion(String nombreDepartamentoExpedicion) {
		this.nombreDepartamentoExpedicion = nombreDepartamentoExpedicion;
	}

	/**
	 * @return the nombrePaisExpedicion
	 */
	public String getNombrePaisExpedicion() {
		return nombrePaisExpedicion;
	}

	/**
	 * @param nombrePaisExpedicion the nombrePaisExpedicion to set
	 */
	public void setNombrePaisExpedicion(String nombrePaisExpedicion) {
		this.nombrePaisExpedicion = nombrePaisExpedicion;
	}

	/**
	 * @return the codigoTipoPaciente
	 */
	public String getCodigoTipoPaciente() {
		return codigoTipoPaciente;
	}

	/**
	 * @param codigoTipoPaciente the codigoTipoPaciente to set
	 */
	public void setCodigoTipoPaciente(String codigoTipoPaciente) {
		this.codigoTipoPaciente = codigoTipoPaciente;
	}

	/**
	 * @return the nombreTipoPaciente
	 */
	public String getNombreTipoPaciente() {
		return nombreTipoPaciente;
	}

	/**
	 * @param nombreTipoPaciente the nombreTipoPaciente to set
	 */
	public void setNombreTipoPaciente(String nombreTipoPaciente) {
		this.nombreTipoPaciente = nombreTipoPaciente;
	}

	/**
	 * @return the cuentasPacienteArray
	 */
	public ArrayList<CuentasPaciente> getCuentasPacienteArray() {
		return cuentasPacienteArray;
	}
	
	/**
	 * @return the CuentasPaciente
	 */
	public CuentasPaciente getCuentasPacienteArray(int pos) {
		return (CuentasPaciente)cuentasPacienteArray.get(pos);
	}

	/**
	 * @param cuentasPacienteArray the cuentasPacienteArray to set
	 */
	public void setCuentasPacienteArray(
			ArrayList<CuentasPaciente> cuentasPacienteArray) {
		this.cuentasPacienteArray = cuentasPacienteArray;
	}
	
	public String getCuentasPacienteSeparadoComas()
	{
		String respuesta = "";
		for(int i = 0; i<this.cuentasPacienteArray.size(); i++)		
			respuesta += this.cuentasPacienteArray.get(i).getCodigoCuenta().toString()+",";			
		
		respuesta = respuesta.substring(0,respuesta.length());
		
		return respuesta;
	}
	
	public String getNombreTransplante() {
		return nombreTransplante;
	}

	public void setNombreTransplante(String nombreTransplante) {
		this.nombreTransplante = nombreTransplante;
	}

	public String getTransplante() {
		return transplante;
	}

	public void setTransplante(String transplante) {
		this.transplante = transplante;
	}
	
	//*****************************************************************************************
	
	/**
	 * Devuelve la posicion donde se ubica la cuenta filtrada por los parametros
	 * -1 si no existen posicion para la cuenta
	 * @param ArrayList cuentasPacienteArray (requerido)
	 * @param String viaIngreso
	 * @param String tipoPaciente (opcional)
	 * */
	public int getPosCuentaPaciente(int cuenta)
	{
		if(cuenta > 0)
		{
			for(int i=0; i<cuentasPacienteArray.size(); i++)
			{
				if(((CuentasPaciente)this.cuentasPacienteArray.get(i)).getCodigoCuenta().equals(cuenta+""))
					return i;
			}
		}
		
		return ConstantesBD.codigoNuncaValido;		
	}
	
	//*****************************************************************************************	
	
	/**
	 * Devuelve la posicion donde se ubica la cuenta filtrada por los parametros
	 * -1 si no existen posicion para la cuenta
	 * @param ArrayList cuentasPacienteArray (requerido)
	 * @param String viaIngreso
	 * @param String tipoPaciente (opcional)
	 * */
	public int getPosCuentaPaciente(ArrayList cuentasPacienteArray, String viaIngreso, String tipoPaciente)
	{
		if(cuentasPacienteArray!=null &&  cuentasPacienteArray.size() > 0)
		{
			for(int i=0; i<cuentasPacienteArray.size(); i++)
			{
				if(viaIngreso.equals(ConstantesBD.codigoViaIngresoUrgencias+"") ||
						viaIngreso.equals(""))
				{
					if(((CuentasPaciente)cuentasPacienteArray.get(i)).getCodigoViaIngreso().equals(viaIngreso))
						return i;
				}
				else if(viaIngreso.equals(ConstantesBD.codigoViaIngresoHospitalizacion+""))
				{
					if( ((CuentasPaciente)cuentasPacienteArray.get(i)).getCodigoViaIngreso().equals(viaIngreso) &&
							((CuentasPaciente)cuentasPacienteArray.get(i)).getCodigoTipoPaciente().equals(tipoPaciente))
						return i;
				}					
			}
		}
		
		return ConstantesBD.codigoNuncaValido;
	}	
	
	//*********************************************************************************************
	
	/**
	 * Método que retorna la edad del paciente en meses 
	 */
	public int getEdadMeses()
	{
		return this.edadMeses;
	}
	
	/**
	 * Método que retorna la edad del paciente en días
	 * @param con
	 * @return
	 */
	public int getEdadDias()
	{
		return this.edadDias;
	}

	/**
	 * @param edadDias the edadDias to set
	 */
	public void setEdadDias(int edadDias) {
		this.edadDias = edadDias;
	}

	/**
	 * @param edadMeses the edadMeses to set
	 */
	public void setEdadMeses(int edadMeses) {
		this.edadMeses = edadMeses;
	}

	/**
	 * @return the codigoPaisVivienda
	 */
	public String getCodigoPaisVivienda() {
		return codigoPaisVivienda;
	}

	/**
	 * @param codigoPaisVivienda the codigoPaisVivienda to set
	 */
	public void setCodigoPaisVivienda(String codigoPaisVivienda) {
		this.codigoPaisVivienda = codigoPaisVivienda;
	}

	/**
	 * @return the codigoCiudadVivienda
	 */
	public String getCodigoCiudadVivienda() {
		return codigoCiudadVivienda;
	}

	/**
	 * @param codigoCiudadVivienda the codigoCiudadVivienda to set
	 */
	public void setCodigoCiudadVivienda(String codigoCiudadVivienda) {
		this.codigoCiudadVivienda = codigoCiudadVivienda;
	}

	/**
	 * @return the codigoDeptoVivienda
	 */
	public String getCodigoDeptoVivienda() {
		return codigoDeptoVivienda;
	}

	/**
	 * @param codigoDeptoVivienda the codigoDeptoVivienda to set
	 */
	public void setCodigoDeptoVivienda(String codigoDeptoVivienda) {
		this.codigoDeptoVivienda = codigoDeptoVivienda;
	}

	/**
	 * @return the telefonoCelular
	 */
	public String getTelefonoCelular() {
		return telefonoCelular;
	}

	/**
	 * @param telefonoCelular the telefonoCelular to set
	 */
	public void setTelefonoCelular(String telefonoCelular) {
		this.telefonoCelular = telefonoCelular;
	}

	/**
	 * @return the telefonoFijo
	 */
	public String getTelefonoFijo() {
		return telefonoFijo;
	}

	/**
	 * @param telefonoFijo the telefonoFijo to set
	 */
	public void setTelefonoFijo(String telefonoFijo) {
		this.telefonoFijo = telefonoFijo;
	}

	/**
	 * @return the antecedentesAlerta
	 */
	public ArrayList<DtoAntecedentesAlerta> getAntecedentesAlerta() {
		return antecedentesAlerta;
	}

	/**
	 * @param antecedentesAlerta the antecedentesAlerta to set
	 */
	public void setAntecedentesAlerta(
			ArrayList<DtoAntecedentesAlerta> antecedentesAlerta) {
		this.antecedentesAlerta = antecedentesAlerta;
	}

	
	/**
	 * 
	 * M&eacute;todo que se encarga de devolver los tel&eacute;fonos del paciente.
	 * Mostrando en orden Tel&eacute;fono Celular, Tel&eacute;fono Fijo y Otros tel&eacute;fonos 
	 * (Otros tel&eacute;fonos s&oacute;lo en el caso de no tener registro en tel&eacute;fono celular o tel&eacute;fono fijo)
	 * 
	 * @param forma
	 * @return
	 */
	public String getTelefonosPaciente (){
		
		String telefonos = "";
		
		if(!UtilidadTexto.isEmpty(getTelefonoCelular())){
			
			telefonos +=  getTelefonoCelular();
		}
		
		if (!UtilidadTexto.isEmpty(getTelefonoFijo())){
			
			if(!UtilidadTexto.isEmpty(telefonos)){
				
				telefonos +=  " - ";
			}
			
			telefonos += getTelefonoFijo();
		}
		
		
		if (UtilidadTexto.isEmpty(getTelefonoCelular()) || UtilidadTexto.isEmpty(getTelefonoFijo())
				&& !UtilidadTexto.isEmpty(getTelefono())){
			
			if(!UtilidadTexto.isEmpty(telefonos)){
				
				telefonos +=  " - ";
			}
			
			telefonos += getTelefono();
		}
		
		return telefonos;
	}

	public void setFechaNacimientoTipoDate(Date fechaNacimientoTipoDate) {
		this.fechaNacimientoTipoDate = fechaNacimientoTipoDate;
	}

	public Date getFechaNacimientoTipoDate() {
		return fechaNacimientoTipoDate;
	}

	public void setEsCapitado(boolean esCapitado) {
		this.esCapitado = esCapitado;
	}

	public boolean isEsCapitado() {
		return esCapitado;
	}

	public void setNumeroFicha(String numeroFicha) {
		this.numeroFicha = numeroFicha;
	}

	public String getNumeroFicha() {
		return numeroFicha;
	}

	/**
	 * @return the tipoIdEmpleador
	 */
	public String getTipoIdEmpleador() {
		return tipoIdEmpleador;
	}

	/**
	 * @param tipoIdEmpleador the tipoIdEmpleador to set
	 */
	public void setTipoIdEmpleador(String tipoIdEmpleador) {
		this.tipoIdEmpleador = tipoIdEmpleador;
	}

	/**
	 * @return the numIdEmpleador
	 */
	public String getNumIdEmpleador() {
		return numIdEmpleador;
	}

	/**
	 * @param numIdEmpleador the numIdEmpleador to set
	 */
	public void setNumIdEmpleador(String numIdEmpleador) {
		this.numIdEmpleador = numIdEmpleador;
	}

	/**
	 * @return the razonSociEmpleador
	 */
	public String getRazonSociEmpleador() {
		return razonSociEmpleador;
	}

	/**
	 * @param razonSociEmpleador the razonSociEmpleador to set
	 */
	public void setRazonSociEmpleador(String razonSociEmpleador) {
		this.razonSociEmpleador = razonSociEmpleador;
	}

	/**
	 * @return the tipoIdCotizante
	 */
	public String getTipoIdCotizante() {
		return tipoIdCotizante;
	}

	/**
	 * @param tipoIdCotizante the tipoIdCotizante to set
	 */
	public void setTipoIdCotizante(String tipoIdCotizante) {
		this.tipoIdCotizante = tipoIdCotizante;
	}

	/**
	 * @return the numIdCotizante
	 */
	public String getNumIdCotizante() {
		return numIdCotizante;
	}

	/**
	 * @param numIdCotizante the numIdCotizante to set
	 */
	public void setNumIdCotizante(String numIdCotizante) {
		this.numIdCotizante = numIdCotizante;
	}

	/**
	 * @return the nombresCotizante
	 */
	public String getNombresCotizante() {
		return nombresCotizante;
	}

	/**
	 * @param nombresCotizante the nombresCotizante to set
	 */
	public void setNombresCotizante(String nombresCotizante) {
		this.nombresCotizante = nombresCotizante;
	}

	/**
	 * @return the apellidosCotizante
	 */
	public String getApellidosCotizante() {
		return apellidosCotizante;
	}

	/**
	 * @param apellidosCotizante the apellidosCotizante to set
	 */
	public void setApellidosCotizante(String apellidosCotizante) {
		this.apellidosCotizante = apellidosCotizante;
	}

	/**
	 * @return the parentesco
	 */
	public int getParentesco() {
		return parentesco;
	}

	/**
	 * @param parentesco the parentesco to set
	 */
	public void setParentesco(int parentesco) {
		this.parentesco = parentesco;
	}

	/**
	 * @return the centroAtencionInt
	 */
	public int getCentroAtencionInt() {
		return centroAtencionInt;
	}

	/**
	 * @param centroAtencionInt the centroAtencionInt to set
	 */
	public void setCentroAtencionInt(int centroAtencionInt) {
		this.centroAtencionInt = centroAtencionInt;
	}

	/**
	 * @return the tipoAfiliado
	 */
	public String getTipoAfiliado() {
		return tipoAfiliado;
	}

	/**
	 * @param tipoAfiliado the tipoAfiliado to set
	 */
	public void setTipoAfiliado(String tipoAfiliado) {
		this.tipoAfiliado = tipoAfiliado;
	}

	/**
	 * @return the excepcionMonto
	 */
	public String getExcepcionMonto() {
		return excepcionMonto;
	}

	/**
	 * @param excepcionMonto the excepcionMonto to set
	 */
	public void setExcepcionMonto(String excepcionMonto) {
		this.excepcionMonto = excepcionMonto;
	}

	/**
	 * @return the pais
	 */
	public String getPais() {
		return pais;
	}

	/**
	 * @param pais the pais to set
	 */
	public void setPais(String pais) {
		this.pais = pais;
	}

	/**
	 * @return the departamento
	 */
	public String getDepartamento() {
		return departamento;
	}

	/**
	 * @param departamento the departamento to set
	 */
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	/**
	 * @return the municipio
	 */
	public String getMunicipio() {
		return municipio;
	}

	/**
	 * @param municipio the municipio to set
	 */
	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	/**
	 * @return the localidad
	 */
	public String getLocalidad() {
		return localidad;
	}

	/**
	 * @param localidad the localidad to set
	 */
	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	/**
	 * @return the barrio
	 */
	public String getBarrio() {
		return barrio;
	}

	/**
	 * @param barrio the barrio to set
	 */
	public void setBarrio(String barrio) {
		this.barrio = barrio;
	}

	public void setClasificacionSE(String clasificacionSE) {
		this.clasificacionSE = clasificacionSE;
	}

	public String getClasificacionSE() {
		return clasificacionSE;
	}

	public void setFechaNacimientoTipoString(String fechaNacimientoTipoString) {
		this.fechaNacimientoTipoString = fechaNacimientoTipoString;
	}

	public String getFechaNacimientoTipoString() {
		return fechaNacimientoTipoString;
	}

	public void setLineaInconsCamposPersona(int lineaInconsCamposPersona) {
		this.lineaInconsCamposPersona = lineaInconsCamposPersona;
	}

	public int getLineaInconsCamposPersona() {
		return lineaInconsCamposPersona;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public void setApellidosNombresPersona(String apellidosNombresPersona) {
		this.apellidosNombresPersona = apellidosNombresPersona;
	}

	public void setEdadDetallada(String edadDetallada) {
		this.edadDetallada = edadDetallada;
	}

	public void setTipoSangre(String tipoSangre) {
		this.tipoSangre = tipoSangre;
	}

	public void setCodigoContrato(int codigoContrato) {
		this.codigoContrato = codigoContrato;
	}

	public void setCodigoCuentaAsocio(int codigoCuentaAsocio) {
		this.codigoCuentaAsocio = codigoCuentaAsocio;
	}

	public void setEstaContratoVencido(boolean estaContratoVencido) {
		this.estaContratoVencido = estaContratoVencido;
	}

	/**
	 * @return the estadoEmbarazada
	 */
	public Boolean getEstadoEmbarazada() {
		return estadoEmbarazada;
	}

	/**
	 * @param estadoEmbarazada the estadoEmbarazada to set
	 */
	public void setEstadoEmbarazada(Boolean estadoEmbarazada) {
		this.estadoEmbarazada = estadoEmbarazada;
	}
	

	/**
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public  String obtenerViaEgreso(Connection con,String codigoPaciente){
		return personaBasicaDao.obtenerViaEgreso(con, codigoPaciente);
	}
	
	/**
	 * @param con
	 * @param numeroSolciitud
	 * @return lista de especialidades
	 */
	public String consultarEspecialidadMedicoXSolicitud(Connection con, Integer numeroSolciitud){
		return personaBasicaDao.consultarEspecialidadMedicoXSolicitud(con, numeroSolciitud);
	}
	
	
	
	/**
	 * @param con
	 * @param codigoPaciente
	 * @param idIngreso
	 * @return fehca y hora de ingreso 
	 */
	public String consultarFechaHoraIngreso(Connection con,String codigoPaciente,String idIngreso){
		return personaBasicaDao.consultarFechaHoraIngreso(con, codigoPaciente, idIngreso);
	}

	/**
	 * @return the fechaMuerte
	 */
	public String getFechaMuerte() {
		return fechaMuerte;
	}

	/**
	 * @param fechaMuerte the fechaMuerte to set
	 */
	public void setFechaMuerte(String fechaMuerte) {
		this.fechaMuerte = fechaMuerte;
	}
	
	
	
}
