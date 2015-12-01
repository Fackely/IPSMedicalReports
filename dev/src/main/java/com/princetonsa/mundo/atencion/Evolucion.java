/*
 * @(#)Evolucion.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo.atencion;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.Vector;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.EvolucionDao;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.princetonsa.mundo.solicitudes.SolicitudEvolucion;

import org.apache.log4j.Logger;

/**
 * Esta clase encapsula los atributos y funcionalidad de la Evoluci�n de un paciente.
 *
 * @version 1.0, May 15, 2003
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>
 */

public class Evolucion implements EvolucionInterface 
{

	/**
	 * Logger para manejar los errores que se presenten en esta
	 * evoluci�n 
	 */
	private static Logger logger = Logger.getLogger(Evolucion.class);

	/**
	 * C�digo constante que identifica una evoluci�n general, seg�n
	 * los valores definidos en la tabla especialidades_val.
	 */
	public static final int GENERAL = 0;

	/**
	 * C�digo constante que identifica una evoluci�n de urgencias,
	 * seg�n los valores definidos en la tabla especialidades_val. 
	 */
	public static final int URGENCIAS = 1;

	/**
	 * C�digo constante que identifica una evoluci�n de hospitalizaci�n,
	 * seg�n los valores definidos en la tabla especialidades_val. 
	 */
	public static final int HOSPITALARIA = 2;

	/**
	 * Indica si esta evoluci�n se debe o no cobrar al paciente;
	 */
	private boolean cobrable;

	/**
	 * Indica si se ha dado o no la orden de salida al paciente.
	 */
	private boolean ordenSalida;

	/**
	 * El c�digo del tipo de esta evoluci�n es 0, de 'General'. Las clases que extiendan
	 * de esta, en su m�todo clean(), deben hacer setTipoEvolucion() con su c�digo respectivo.
	 * Posibles valores son :
	 * <ul>
	 *   <li>Evolucion.GENERAL : General</li>
	 *   <li>Evolucion.URGENCIAS : Urgencias</li>
	 *   <li>Evolucion.HOSPITALARIA : Hospitalizaci�n</li>
	 * </ul>
	 * Estos c�digos son los definidos en la tabla especialidades_val
	 */
	private int tipoEvolucion;

	/**
	 * C�digo de la cuenta asociada a esta evoluci�n.
	 */
	private int idCuenta;

	/**
	 * C�digo del centro de costo al que pertenece el m�dico que realiza esta evoluci�n.
	 */
	private int idCentroCosto;

	/**
	 * C�digo de esta evoluci�n.
	 */
	private int codigo;

	/**
	 * Fecha en la que se realiza esta evoluci�n.
	 */
	private String fechaEvolucion;

	/**
	 * Fecha en la que se graba esta evoluci�n.
	 */
	private String fechaGrabacion;

	/**
	 * Hora en la que se realiza esta evoluci�n.
	 */
	private String horaEvolucion;

	/**
	 * Hora en la que se graba esta evoluci�n.
	 */
	private String horaGrabacion;

	/**
	 * Fecha en la que fue grabada esta valoraci�n
	 * en formato de la aplicaci�n.
	 */
	private String fechaGrabacionFormatoAp;

	/**
	 * Informaci�n suministrada por el paciente.
	 */
	private String informacionDadaPaciente;

	/**
	 * Descripci�n de las complicaciones de la enfermedad del paciente.
	 */
	private String descripcionComplicacion;

	/**
	 * Tratamiento prescrito al paciente.
	 */
    private String tratamiento;

	/**
	 * Resultados del tratamiento prescrito al paciente.
	 */
	private String resultadosTratamiento;

	/**
	 * Cambios en el manejo dado al paciente.
	 */
	private String cambiosManejo;

	/**
	 * Observaciones generales de la evoluci�n.
	 */
	private String observaciones;
	
	/**
	 * C�digo de la valoraci�n a la que encuentra
	 * atada esta evoluci�n
	 */
	private int codigoValoracion;
	
	/**
	 * C�digo del recargo para esta evoluci�n
	 */
	private int recargo=ConstantesBD.codigoTipoRecargoSinRecargo;

	/**
	 * C�digo del recargo para esta evoluci�n
	 */
	private String nombreRecargo="";

	/**
	 * Hallazgos m�s importantes de esta evoluci�n.
	 */
	private String hallazgosImportantes;

	/**
	 * Procedimientos quir�rgicos u obst�tricos en esta evoluci�n.
	 */
	private String procedimientosQuirurgicosObstetricos;

	/**
	 * Fecha y resultado de los ex�menes diagn�sticos.
	 */
	private String fechaYResultadoExamenesDiagnostico;
	
	/**
	 * Boolean que me indica si debo mostrar la cancelaci�n
	 * del tratante o no
	 */
	private boolean deboMostrarCancelacionTratante=false;

	/**
	 * Pron�stico del paciente.
	 */
	private String pronostico;

	/**
	 * Signos vitales del paciente.
	 */
	private Collection signosVitales;

	/**
	 * Diagn�sticos presuntivos del paciente.
	 */
	private Collection diagnosticosPresuntivos;

	/**
	 * Diagn�sticos definitivos del paciente.
	 */
	private Collection diagnosticosDefinitivos;

	/**
	 * C�digo y nombre del diagn�stico de complicaci�n.
	 */
	private Diagnostico diagnosticoComplicacion;
	
	/**
	 * Tipo de diagn�stico principal
	 */
	private int tipoDiagnosticoPrincipal;

	/**
	 * Tipo de diagn�stico principal
	 */
	private String nombreTipoDiagnosticoPrincipal;

	/**
	 * M�dico que hace esta evoluci�n.
	 */
	private UsuarioBasico medico;

	/**
	 * El DAO usado por el objeto <code>Evolucion</code> para acceder a la fuente de datos.
	 */
	private static EvolucionDao evolucionDao;

	/**
	 * Convertit medico a tratanete
	 */
	private char convertirMedicoTratante;
	
	/**
	 * Manejo del balkance de liquidos
	 */
	private Vector balanceLiquidos;
	
	/**
	 * codigo de la conducta a seguir
	 */
	private int codigoConductaSeguir;
	
	/**
	 * 
	 */
	private String acronimoTipoReferencia;
	
	/**
	 * Crea un nuevo objeto <code>Evolucion</code>.
	 */
	public Evolucion() {
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}

	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores v�lidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicializaci�n fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD) {

		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

		if (myFactory != null) {
			evolucionDao = myFactory.getEvolucionDao();
			wasInited = (evolucionDao != null);
		}

		return wasInited;

	}
	
	/**
	 * mapa que carga la informacion de conducta a seguir de la utltinma evolucion insertada,
	 * en caso de no encontrar informacion retorna null
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static HashMap conductaASeguirUltimaInsertada(Connection con, int idCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEvolucionDao().conductaASeguirUltimaInsertada(con, idCuenta);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEvolucion
	 * @return
	 */
	public static HashMap conductaASeguirDadaEvol(Connection con, int codigoEvolucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEvolucionDao().conductaASeguirDadaEvol(con, codigoEvolucion);
	}
	
	
	/**
	 * mapa que contiene las conductas a seguir
	 * @param con
	 * @return
	 */
	public static HashMap conductasASeguirMap()
	{
		Connection con= UtilidadBD.abrirConexion();
		HashMap conductasSeguirMap=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEvolucionDao().conductasASeguirMap(con);
		UtilidadBD.closeConnection(con);
		return conductasSeguirMap;
	}
	
	/**
	 * Retorna los cambios en el manejo del paciente.
	 * @return los cambios en el manejo del paciente
	 */
	public String getCambiosManejo() {
		return cambiosManejo;
	}

	/**
	 * Retorna la descripci�n de la complicaci�n.
	 * @return la descripci�n de la complicaci�n
	 */
	public String getDescripcionComplicacion() {
		return descripcionComplicacion;
	}

	/**
	 * Retorna el diagn�stico de la complicaci�n.
	 * @return el diagn�stico de la complicaci�n
	 */
	public Diagnostico getDiagnosticoComplicacion() {
		return diagnosticoComplicacion;
	}

	/**
	 * Retorna los diagn�sticos definitivos del paciente.
	 * @return los diagn�sticos definitivos del paciente
	 */
	public Collection getDiagnosticosDefinitivos() {
		return diagnosticosDefinitivos;
	}

	/**
	 * Retorna los diagn�sticos presuntivos del paciente.
	 * @return los diagn�sticos presuntivos del paciente
	 */
	public Collection getDiagnosticosPresuntivos() {
		return diagnosticosPresuntivos;
	}

	/**
	 * Retorna la fecha en que se hizo esta evoluci�n.
	 * @return la fecha en que se hizo esta evoluci�n
	 */
	public String getFechaEvolucion() {
		return fechaEvolucion;
	}

	/**
	 * Retorna la fecha en que se grab� esta evoluci�n.
	 * @return la fecha en que se grab� esta evoluci�n
	 */
	public String getFechaGrabacion() {
		return fechaGrabacion;
	}

	/**
	 * Retorna la fecha y resultados de los ex�menes diagn�sticos.
	 * @return fecha y resultados de los ex�menes diagn�sticos
	 */
	public String getFechaYResultadoExamenesDiagnostico() {
		return fechaYResultadoExamenesDiagnostico;
	}

	/**
	 * Retorna los hallazgos m�s importantes de esta evoluci�n.
	 * @return los hallazgos m�s importantes de esta evoluci�n
	 */
	public String getHallazgosImportantes() {
		return hallazgosImportantes;
	}

	/**
	 * Retorna la hora en la que se efectu� la evoluci�n.
	 * @return la hora en la que se efectu� la evoluci�n
	 */
	public String getHoraEvolucion() {
		return horaEvolucion;
	}

	/**
	 * Retorna la hora en la que se grab� la evoluci�n.
	 * @return la hora en la que se grab� la evoluci�n
	 */
	public String getHoraGrabacion() {
		return horaGrabacion;
	}

	/**
	 * Retorna el c�digo del centro de costo al cual pertenece el m�dico que
	 * realiza esta evoluci�n.
	 * @return c�digo del centro de costo
	 */
	public int getIdCentroCosto() {
		return idCentroCosto;
	}

	/**
	 * Retorna el c�digo de la cuenta asociada a esta evoluci�n.
	 * @return c�digo de la cuenta
	 */
	public int getIdCuenta() {
		return idCuenta;
	}

	/**
	 * Retorna la informaci�n suministrada por el paciente.
	 * @return la informaci�n suministrada por el paciente
	 */
	public String getInformacionDadaPaciente() {
		return informacionDadaPaciente;
	}

	/**
	 * Retorna el usuario que efectu� esta evoluci�n.
	 * @return el usuario que efectu� esta evoluci�n
	 */
	public UsuarioBasico getMedico() {
		return medico;
	}

	/**
	 * Retorna el n�mero de la solicitud de esta evoluci�n.
	 * @return el n�mero de la solicitud de esta evoluci�n
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * Retorna las observaciones generales de esta evoluci�n.
	 * @return las observaciones generales de esta evoluci�n
	 */
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * Retorna si hubo o no una orden de salida en esta evoluci�n.
	 * @return <b>true</b> si en esta evoluci�n hubo orden de salida, <b>false</b> si no 
	 */
	public boolean isOrdenSalida() {
		return ordenSalida;
	}

	/**
	 * Retorna los procedimientos quir�rgicos y obst�tricos en esta evoluci�n.
	 * @return procedimientos quir�rgicos y obst�tricos en esta evoluci�n
	 */
	public String getProcedimientosQuirurgicosObstetricos() {
		return procedimientosQuirurgicosObstetricos;
	}

	/**
	 * Retorna el pron�stico de esta evoluci�n.
	 * @return el pron�stico de esta evoluci�n
	 */
	public String getPronostico() {
		return pronostico;
	}

	/**
	 * Retorna los resultados del tratamiento efectuado al paciente.
	 * @return los resultados del tratamiento efectuado al paciente
	 */
	public String getResultadosTratamiento() {
		return resultadosTratamiento;
	}

	/**
	 * Retorna los signos vitales del paciente. 
	 * @return los signos vitales del paciente
	 */
	public Collection getSignosVitales() {
		return signosVitales;
	}

	/**
	 * Retorna el tipo de esta evoluci�n.
	 * @return el tipo de esta evoluci�n
	 */
	public int getTipoEvolucion() {
		return tipoEvolucion;
	}

	/**
	 * Retorna el tratamiento prescrito para este paciente.
	 * @return el tratamiento prescrito para este paciente
	 */
	public String getTratamiento() {
		return tratamiento;
	}

	/**
	 * Dice si esta evoluci�n debe o no ser cobrada.
	 * @return <b>true</b> si debemos cobrar esta evoluci�n, <b>false</b> si es gratis ;)
	 */
	public boolean isCobrable() {
		return cobrable;
	}

	/**
	 * Establece los cambios en el manejo dado al paciente.
	 * @param cambiosManejo los cambios en el manejo dado al paciente a establecer
	 */
	public void setCambiosManejo(String cambiosManejo) {
		this.cambiosManejo = cambiosManejo;
	}

	/**
	 * Establece si esta evoluci�n es o no cobrable.
	 * @param cobrable <b>true</b> si esta evoluci�n se va a cobrar, <b>false</b> si es gratis ;)
	 */
	public void setCobrable(boolean cobrable) {
		this.cobrable = cobrable;
	}

	/**
	 * Establece la descripci�n de las complicaciones.
	 * @param descripcionComplicacion la descripci�n de las complicaciones a establecer
	 */
	public void setDescripcionComplicacion(String descripcionComplicacion) {
		this.descripcionComplicacion = descripcionComplicacion;
	}

	/**
	 * Establece el diagn�stico de la complicaci�n.
	 * @param diagnosticoComplicacion el diagn�stico de la complicaci�n a establecer
	 */
	public void setDiagnosticoComplicacion(Diagnostico diagnosticoComplicacion) {
		this.diagnosticoComplicacion = diagnosticoComplicacion;
	}

	/**
	 * Establece los diagn�sticos definitivos del paciente.
	 * @param diagnosticosDefinitivos los diagn�sticos definitivos a establecer
	 */
	public void setDiagnosticosDefinitivos(Collection diagnosticosDefinitivos) {
		this.diagnosticosDefinitivos = diagnosticosDefinitivos;
	}

	/**
	 * Establece los diagn�sticos presuntivos del paciente.
	 * @param diagnosticosPresuntivos los diagn�sticos presuntivos a establecer
	 */
	public void setDiagnosticosPresuntivos(Collection diagnosticosPresuntivos) {
		this.diagnosticosPresuntivos = diagnosticosPresuntivos;
	}

	/**
	 * Establece la fecha en la que fue realizada esta evoluci�n.
	 * @param fechaEvolucion la fecha a establecer
	 */
	public void setFechaEvolucion(String fechaEvolucion) {
		this.fechaEvolucion = fechaEvolucion;
	}

	/**
	 * Establece la fecha de grabaci�n real de esta evoluci�n.
	 * @param fechaGrabacion la fecha de grabaci�n a establecer
	 */
	public void setFechaGrabacion(String fechaGrabacion) {
		this.fechaGrabacion = fechaGrabacion;
	}

	/**
	 * Establece la fecha y resultado de los ex�menes de diagn�sticos.
	 * @param fechaYResultadoExamenesDiagnostico la fecha y resultado de los ex�menes de diagn�sticos a establecer
	 */
	public void setFechaYResultadoExamenesDiagnostico(String fechaYResultadoExamenesDiagnostico) {
		this.fechaYResultadoExamenesDiagnostico = fechaYResultadoExamenesDiagnostico;
	}

	/**
	 * Establece los hallazgos m�s importantes de esta evoluci�n.
	 * @param hallazgosImportantes los hallazgos m�s importantes a establecer
	 */
	public void setHallazgosImportantes(String hallazgosImportantes) {
		this.hallazgosImportantes = hallazgosImportantes;
	}

	/**
	 * Establece la hora en que fue realizada esta evoluci�n.
	 * @param horaEvolucion la hora a establecer.
	 */
	public void setHoraEvolucion(String horaEvolucion) {
		String [] time = horaEvolucion.split(":");
		this.horaEvolucion = time[0] + ":" + time[1];
	}

	/**
	 * Establece la hora de grabaci�n real de esta evoluci�n.
	 * @param horaGrabacion la hora de grabaci�n a establecer
	 */
	public void setHoraGrabacion(String horaGrabacion) {
		//String [] time = horaGrabacion.split(":");
		//this.horaGrabacion = time[0] + ":" + time[1];
		//Es necesario cargarlo completo para las comparaciones
		this.horaGrabacion=horaGrabacion;
	}

	/**
	 * Establece el c�digo del centro de costo al cual pertenece el m�dico que
	 * realiza esta evoluci�n.
	 * @param idCentroCosto c�digo del centro de costo a establecer
	 */
	public void setIdCentroCosto(int idCentroCosto) {
		this.idCentroCosto = idCentroCosto;
	}

	/**
	 * Establece el c�digo de la cuenta asociada a esta evoluci�n.
	 * @param idCuenta c�digo de la cuenta a establecer
	 */
	public void setIdCuenta(int idCuenta) {
		this.idCuenta = idCuenta;
	}

	/**
	 * Establece la informaci�n suministrada por el paciente.
	 * @param informacionDadaPaciente la informaci�n suministrada a establecer
	 */
	public void setInformacionDadaPaciente(String informacionDadaPaciente) {
		this.informacionDadaPaciente = informacionDadaPaciente;
	}

	/**
	 * Establece el m�dico responsable de esta evoluci�n.
	 * @param medico el m�dico responsable a establecer
	 */
	public void setMedico(UsuarioBasico medico) {
		this.medico = medico;
	}

	/**
	 * Establece el n�mero de solicitud de esta evoluci�n.
	 * @param codigo el n�mero de solicitud a establecer
	 */
	public void setCodigo(int numeroSolicitud) {
		this.codigo = numeroSolicitud;
	}

	/**
	 * Establece las observaciones de esta evoluci�n.
	 * @param observaciones las observaciones a establecer
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * Indica si en esta evoluci�n hubo o no una orden de salida del paciente.
	 * @param ordenSalida <b>true</b> si esta evoluci�n tiene una orden de salida, <b>false</b> si no
	 */
	public void setOrdenSalida(boolean ordenSalida) {
		this.ordenSalida = ordenSalida;
	}

	/**
	 * Establece los procedimientos quir�rgicos u obst�tricos.
	 * @param procedimientosQuirurgicosObstetricos los procedimientos quir�rgicos u obst�tricos a establecer
	 */
	public void setProcedimientosQuirurgicosObstetricos(String procedimientosQuirurgicosObstetricos) {
		this.procedimientosQuirurgicosObstetricos = procedimientosQuirurgicosObstetricos;
	}

	/**
	 * Establece el pron�stico de esta evoluci�n.
	 * @param pronostico pron�stico a establecer
	 */
	public void setPronostico(String pronostico) {
		this.pronostico = pronostico;
	}

	/**
	 * Establece los resultados del tratamiento dado al paciente.
	 * @param resultadosTratamiento los resultados a establecer
	 */
	public void setResultadosTratamiento(String resultadosTratamiento) {
		this.resultadosTratamiento = resultadosTratamiento;
	}

	/**
	 * Establece los signos vitales del paciente.
	 * @param signosVitales los signos vitales a establecer
	 */
	public void setSignosVitales(Collection signosVitales) {
		this.signosVitales = signosVitales;
	}

	/**
	 * Retorna el tipo de esta evoluci�n.
	 * @param tipoEvolucion el tipo de evoluci�n a establecer
	 */
	public void setTipoEvolucion(int tipoEvolucion) {
		this.tipoEvolucion = tipoEvolucion;
	}

	/**
	 * Establece el tratamiento suministrado al paciente.
	 * @param tratamiento el tratamiento a establecer
	 */
	public void setTratamiento(String tratamiento) {
		this.tratamiento = tratamiento;
	}

	/**
	 * Este m�todo inicializa en valores vac�os, -mas no nulos- los atributos de este objeto.
	 */
	public void clean() {

		this.cambiosManejo = "";
		this.cobrable = false;
		this.descripcionComplicacion = "";
		this.diagnosticoComplicacion = new Diagnostico();
		this.diagnosticosDefinitivos = new TreeSet(new DiagnosticoComparator());
		this.diagnosticosPresuntivos = new TreeSet(new DiagnosticoComparator());
		this.fechaEvolucion = "";
		this.fechaGrabacion = "";
		this.fechaYResultadoExamenesDiagnostico = "";
		this.hallazgosImportantes = "";
		this.horaEvolucion = "";
		this.horaGrabacion = "";
		this.nombreRecargo="";
		this.idCentroCosto = -1;
		this.idCuenta = -1;
		this.informacionDadaPaciente = "";
		this.medico = new UsuarioBasico();
		this.codigo = -1;
		this.observaciones = "";
		this.ordenSalida = false;
		this.procedimientosQuirurgicosObstetricos = "";
		this.pronostico = "";
		this.resultadosTratamiento = "";
		this.signosVitales = new ArrayList();
		this.tipoEvolucion = GENERAL;
		this.tratamiento = "";
		this.codigoValoracion=-1;
		this.fechaGrabacionFormatoAp="";
		this.deboMostrarCancelacionTratante=false;
		this.recargo=ConstantesBD.codigoTipoRecargoSinRecargo;
		this.tipoDiagnosticoPrincipal=0;
		this.nombreTipoDiagnosticoPrincipal="";
		this.convertirMedicoTratante='p';
		
		this.codigoConductaSeguir=ConstantesBD.codigoNuncaValido;
		this.acronimoTipoReferencia="";
	}

	/**
	 * Inserta esta evoluci�n en sus tablas respectivas, y establece el valor
	 * de n�mero de solicitud de este objeto en su valor adecuado. Este m�todo
	 * maneja su transaccionalidad internamente.
	 * @param balanceLiquidos @todo
	 * @param Una conexi�n abierta con la fuente de datos
	 * 
	 * @return El n�mero de la solicitud de evoluci�n, o 0 si hubo alg�n error
	 * @see com.princetonsa.dao.EvolucionDao#insertar(Connection, int, int, boolean, String, int, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, boolean, Collection, Collection, Collection, int)
	 */
	public int insertar(Connection con, PersonaBasica paciente, UsuarioBasico medico, Vector balanceLiquidos, int codigoConductaASeguir, String acronimoTipoReferencia) throws Exception 
	{
		int codigo=this.insertarTransaccional(con, paciente, medico, ConstantesBD.inicioTransaccion, balanceLiquidos,codigoConductaASeguir, acronimoTipoReferencia);
		UtilidadBD.finalizarTransaccion(con);
		return codigo;
	}

	/**
	 * Inserta esta evoluci�n en sus tablas respectivas, y establece el valor
	 * de n�mero de solicitud de este objeto en su valor adecuado. Este m�todo
	 * maneja su transaccionalidad internamente basado en el valor del 'estado'.
	 * @param estado de la transacci�n. posibles valores son "empezar", "continuar", "finalizar".
	 * @param balanceLiquidos @todo
	 * @param Una conexi�n abierta con la fuente de datos
	 * @return El n�mero de la solicitud de evoluci�n, o 0 si hubo alg�n error
	 * @see com.princetonsa.dao.EvolucionDao#insertarTransaccional(Connection, String, int, int, boolean, String, int, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, boolean, Collection, Collection, Collection, int) 
	 */
	public int insertarTransaccional(Connection con, PersonaBasica paciente, UsuarioBasico medico, String estado, Vector balanceLiquidos, int codigoConductaASeguir, String acronimoTipoReferencia) throws Exception 
	{
		int codigo =0;
		if (this.cobrable)
		{
			if (estado.equals(ConstantesBD.finTransaccion))
			{
				codigo = evolucionDao.insertarTransaccional(
					con, ConstantesBD.continuarTransaccion, 
					cobrable, diagnosticoComplicacion.getAcronimo(),
					diagnosticoComplicacion.getTipoCIE(), 
					fechaEvolucion, horaEvolucion,
					informacionDadaPaciente, 
					descripcionComplicacion,
					tratamiento, 
					resultadosTratamiento,
					cambiosManejo,
					hallazgosImportantes, 
					procedimientosQuirurgicosObstetricos, 
					fechaYResultadoExamenesDiagnostico, 
					pronostico, observaciones, 
					medico.getCodigoPersona(), 
					ordenSalida, diagnosticosPresuntivos, 
					diagnosticosDefinitivos, signosVitales, 
					tipoEvolucion, codigoValoracion, recargo,
					tipoDiagnosticoPrincipal,
					medico.getInformacionGeneralPersonalSalud(), balanceLiquidos,
					paciente.getCodigoArea(),
					codigoConductaASeguir, acronimoTipoReferencia
				);
			}
			else
			{
				codigo = evolucionDao.insertarTransaccional(
					con, estado, cobrable, diagnosticoComplicacion.getAcronimo(),
					diagnosticoComplicacion.getTipoCIE(), fechaEvolucion, horaEvolucion,
					informacionDadaPaciente, descripcionComplicacion, tratamiento, resultadosTratamiento, cambiosManejo,
					hallazgosImportantes, procedimientosQuirurgicosObstetricos, fechaYResultadoExamenesDiagnostico, pronostico,
					observaciones, medico.getCodigoPersona(), 
					ordenSalida, diagnosticosPresuntivos, diagnosticosDefinitivos, signosVitales, tipoEvolucion, codigoValoracion, recargo,
					tipoDiagnosticoPrincipal,medico.getInformacionGeneralPersonalSalud(), balanceLiquidos,
					paciente.getCodigoArea(),
					codigoConductaASeguir, acronimoTipoReferencia
				);
			}
			SolicitudEvolucion sol=new SolicitudEvolucion();
			llenarSolicitudEvolucion (sol, codigo, fechaEvolucion, horaEvolucion, medico, UtilidadValidacion.getCodigoCentroCostoTratanteMetodoLento(con, paciente.getCodigoCuenta(), medico.getCodigoInstitucionInt()), paciente);
		
			int codigoSolicitud=sol.insertarSolicitudEvolucionTransaccional(con, ConstantesBD.continuarTransaccion);
			sol.actualizarMedicoRespondeTransaccional(con, codigoSolicitud, medico, ConstantesBD.continuarTransaccion);
			ArrayList array=Utilidades.obtenerPoolesMedico(con,UtilidadFecha.getFechaActual(),medico.getCodigoPersona());
			if(array.size()==1)
			{
				sol.actualizarPoolSolicitud(con,codigoSolicitud,Integer.parseInt(array.get(0)+""));
			}
			//Debemos actualizar la solicitud para que 
			//ponga los datos del m�dico que responde
			
			//si no genera cargo pendiente ni exitoso entonces debe abortar la transaccion
			//numeroSOLICITUD-> codigoSolicitud y codigoEvolcucion=codigo
			
			//GENERACION DEL CARGO Y SUBCUENTA - EVALUACION COBERTURA 
		    Cargos cargos= new Cargos();
		    boolean inserto= cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	con, 
				    																			medico, 
				    																			paciente, 
				    																			false/*dejarPendiente*/, 
				    																			codigoSolicitud, 
				    																			ConstantesBD.codigoTipoSolicitudEvolucion /*codigoTipoSolicitudOPCIONAL*/, 
				    																			getIdCuenta(con, codigoSolicitud) /*codigoCuentaOPCIONAL*/, 
				    																			ConstantesBD.codigoNuncaValido /*codigoCentroCostoEjecutaOPCIONAL*/, 
				    																			ConstantesBD.codigoNuncaValido /*codigoServicioOPCIONAL*/, 
				    																			1 /*cantidadServicioOPCIONAL*/, 
				    																			ConstantesBD.codigoNuncaValidoDouble/*valorTarifaOPCIONAL*/, 
				    																			codigo /*codigoEvolucionOPCIONAL*/,
				    																			/* "" -- numeroAutorizacionOPCIONAL*/
				    																			""/*esPortatil*/,false,fechaEvolucion,
				    																			"" /*subCuentaCoberturaOPCIONAL*/);
					
			if(!inserto)
			{
				logger.error("Error generando cargo ");
				UtilidadBD.abortarTransaccion(con);
			}
			
			
			/** EL PROBLEMA ES AQU� **/
			ResultadoBoolean res=null;
			if(UtilidadValidacion.esMedicoTratante(con,medico,paciente).equals(""))
				res=sol.cambiarEstadosSolicitudTransaccional(con, codigoSolicitud, 0, ConstantesBD.codigoEstadoHCInterpretada, ConstantesBD.continuarTransaccion);
			else
			{
			    //@todo hacer las validaciones 
			    if(this.getConvertirMedicoTratante()=='s')
			    {
			        Solicitud solicitudObject= new Solicitud();
			        solicitudObject.cargar(con, this.codigoValoracion);
			        if(solicitudObject.getTipoSolicitud().getCodigo()==ConstantesBD.codigoTipoSolicitudInterconsulta)
			        {
			            res=solicitudObject.interpretarSolicitudInterconsulta(con, "-", medico);
			        }
			        else
			        {
			            res=sol.cambiarEstadosSolicitudTransaccional(con, codigoSolicitud, 0, ConstantesBD.codigoEstadoHCRespondida, ConstantesBD.continuarTransaccion);	
			        }
			    }
			    else 
			        res=sol.cambiarEstadosSolicitudTransaccional(con, codigoSolicitud, 0, ConstantesBD.codigoEstadoHCRespondida, ConstantesBD.continuarTransaccion);
			}
			if(!res.isTrue())
			{
				logger.error("Error cambiando los estados de la solicitud n�mero "+res.getDescripcion());
			}
			this.setCodigo(codigo);
			return codigo;
		}
		else
		{
			codigo = evolucionDao.insertarTransaccional(
				con, estado, cobrable, diagnosticoComplicacion.getAcronimo(),
				diagnosticoComplicacion.getTipoCIE(), fechaEvolucion, horaEvolucion,
				informacionDadaPaciente, descripcionComplicacion, tratamiento, resultadosTratamiento, cambiosManejo,
				hallazgosImportantes, procedimientosQuirurgicosObstetricos, fechaYResultadoExamenesDiagnostico, pronostico,
				observaciones, medico.getCodigoPersona(), 
				ordenSalida, diagnosticosPresuntivos, diagnosticosDefinitivos, signosVitales, tipoEvolucion, codigoValoracion, recargo,
				tipoDiagnosticoPrincipal,medico.getInformacionGeneralPersonalSalud(), balanceLiquidos, paciente.getCodigoArea(),
				codigoConductaASeguir, acronimoTipoReferencia
			);
			this.setCodigo(codigo);
			return codigo;
		}
	}
	
	/**
	 * 
	 * @param sol
	 * @param codigoEvolucion
	 * @param fechaEvolucion
	 * @param horaEvolucion
	 * @param medico
	 * @param centroCostoTratante
	 * @param paciente
	 */
	public void llenarSolicitudEvolucion (SolicitudEvolucion sol, int codigoEvolucion, String fechaEvolucion, String horaEvolucion, UsuarioBasico medico, int centroCostoTratante, PersonaBasica paciente)
	{
		sol.setCodigoEvolucion(codigoEvolucion);
		sol.setFechaSolicitud(fechaEvolucion);
		sol.setHoraSolicitud(horaEvolucion);
		sol.setTipoSolicitud(new InfoDatosInt (ConstantesBD.codigoTipoSolicitudEvolucion, ""));
		sol.setCobrable(true);
		
		sol.setEspecialidadSolicitante(new InfoDatosInt(ConstantesBD.codigoEspecialidadMedicaTodos, ""));
		sol.setOcupacionSolicitado(new InfoDatosInt(ConstantesBD.codigoOcupacionMedicaTodos));
		
		//El m�dico que lo crea NO es el solicitante, b�sicamente porque
		//la interconsulta es la responsable de su modificaci�n
		sol.setCentroCostoSolicitante(new InfoDatosInt(paciente.getCodigoArea(), ""));
		sol.setCentroCostoSolicitado(new InfoDatosInt(paciente.getCodigoArea(), ""));
		sol.setCodigoMedicoSolicitante(medico.getCodigoPersona());
		sol.setCodigoCuenta(idCuenta);
		sol.setVaAEpicrisis(false);
		sol.setUrgente(false);
	}

	/**
	 * Carga una evoluci�n desde la fuente de datos, y pone todos los datos asociados a ella
	 * en un objeto <code>Evolucion</code>.
	 * @param con conexi�n abierta con la fuente de datos
	 * @param codigo n�mero de solicitud de la evoluci�n que se desea cargar
	 * @return <b>true</b> si se pudo cargar una evoluci�n con el n�mero de
	 * solicitud dado, <b>false</b> si no se pudo (posiblemente, pq no exist�a una
	 * evoluci�n con ese n�mero de solicitud)
	 * @see com.princetonsa.dao.EvolucionDao#cargarEvolucionSignosVitales(Connection, int)
	 */
	public boolean cargar(Connection con, int numeroSolicitud) throws SQLException {
		// Limpiamos los datos viejos antes de cargar lo nuevo
		clean();

		// Cargamos los datos base

		ResultSetDecorator rs = evolucionDao.cargarEvolucionBase(con, numeroSolicitud);

		if (rs.next()) 
		{
			this.codigo = rs.getInt("codigo");
			this.idCuenta = rs.getInt("idCuenta");
			this.idCentroCosto = rs.getInt("idCentroCosto");
			this.cobrable = rs.getBoolean("cobrable");
			this.diagnosticoComplicacion.setNombre(rs.getString("nombreDiagnosticoComplicacion"));
			this.diagnosticoComplicacion.setAcronimo(rs.getString("acronimoDiagnosticoComplicacin"));
			this.diagnosticoComplicacion.setTipoCIE(rs.getInt("tipoCIEDiagnosticoComplicacion"));
			this.fechaEvolucion =  UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechaEvolucion"));
			this.fechaGrabacion = rs.getString("fechaGrabacion");
			this.fechaGrabacionFormatoAp=UtilidadFecha.conversionFormatoFechaAAp(fechaGrabacion);
			this.setHoraEvolucion(rs.getString("horaEvolucion"));
			this.setHoraGrabacion(rs.getString("horaGrabacion"));
			this.informacionDadaPaciente = rs.getString("informacionDadaPaciente");
			this.descripcionComplicacion = rs.getString("descripcionComplicacion");
			this.tratamiento = rs.getString("tratamiento");
			this.resultadosTratamiento = rs.getString("resultadosTratamiento");
			this.cambiosManejo = rs.getString("cambiosManejo");
			this.hallazgosImportantes = rs.getString("hallazgosImportantes");
			//this.procedimientosQuirurgicosObstetricos = rs.getString("procedimientosQuirurgicosObste");
			this.procedimientosQuirurgicosObstetricos =  "" + rs.getInt("procedimientosQuirurgicosObste");
			this.fechaYResultadoExamenesDiagnostico = rs.getString("fechaYResultadoExamenesDiagnos");
			this.pronostico = rs.getString("pronostico");
			this.observaciones = rs.getString("observaciones");
			this.medico.cargarUsuarioBasico(con, rs.getString("tipoIdentificacionMedico"),rs.getString("numeroIdentificacionMedico"));
			this.ordenSalida = rs.getBoolean("ordenSalida");
			this.tipoEvolucion = rs.getInt("tipoEvolucion");
			this.codigoValoracion=rs.getInt("codigoValoracion");
			this.deboMostrarCancelacionTratante=evolucionDao.deboMostrarCancelacionTratante(con, codigo);
			this.recargo=rs.getInt("codigoRecargo");
			this.nombreRecargo=rs.getString("nombreRecargo");
			this.tipoDiagnosticoPrincipal=rs.getInt("tipoDiagnosticoPrincipal");
			this.nombreTipoDiagnosticoPrincipal=rs.getString("nombreTipoDiagnosticoPrincipal");
		}

		else {
			return false;
		}

		// Cargamos los datos de diagn�sticos presuntivos y definitivos (si los hay)

		rs = evolucionDao.cargarEvolucionDiagnosticos(con, numeroSolicitud);

		while (rs.next()) {

			Diagnostico diagnostico = new Diagnostico();

			diagnostico.setAcronimo(rs.getString("acronimo"));
			diagnostico.setNombre(rs.getString("nombre").trim());
			diagnostico.setNumero(rs.getInt("numero"));
			diagnostico.setPrincipal(rs.getBoolean("principal"));
			diagnostico.setTipoCIE(rs.getInt("tipoCIE"));

			if (rs.getBoolean("definitivo")) {
				this.diagnosticosDefinitivos.add(diagnostico);
			}
			else {
				this.diagnosticosPresuntivos.add(diagnostico);
			}

		}

		// Cargamos los datos de signos vitales (si los hay)

		rs = evolucionDao.cargarEvolucionSignosVitales(con, numeroSolicitud);

		while (rs.next()) {

			SignoVital signoVital = new SignoVital();

			signoVital.setCodigo(rs.getInt("codigo"));
			signoVital.setDescripcion(rs.getString("descripcion"));
			signoVital.setUnidadMedida(rs.getString("unidadMedida"));
			signoVital.setValorSignoVital(rs.getString("valorSignoVital"));
			signoVital.setValue(rs.getString("nombre"));

			this.signosVitales.add(signoVital);

		}
		
		
		this.balanceLiquidos=cargarEvolucionBalanceLiquidos(con, this.codigo);
		if(balanceLiquidos==null)
		{
			return false;
		}

		return true;

	}
	
	/**
	 * M�todo que retorna el n�mero de signos 
	 * vitales.
	 * @return
	 */
	public int getNumSignosVitales ()
	{
		return this.signosVitales.size();
	}

	/**
	 * M�todo que retorna el n�mero de diagnosticos
	 * presuntivos
	 * 
	 * @return
	 */
	public int getNumDiagnosticosPresuntivos()
	{
		return this.diagnosticosPresuntivos.size();
	}
	
	/**
	 * M�todo que retorna el n�mero de diagnosticos
	 * definitivos
	 * 
	 * @return
	 */
	public int getNumDiagnosticosDefinitivos()
	{
		return this.diagnosticosDefinitivos.size();
	}

	/**
	 * Este m�todo dice si para esta evoluci�n hay datos objetivos
	 * @return
	 */
	public boolean getHayDatosObjetivos ()
	{
		if (this.getNumSignosVitales()>0||!this.hallazgosImportantes.equals("")||!this.procedimientosQuirurgicosObstetricos.equals("")||!this.fechaYResultadoExamenesDiagnostico.equals(""))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Este m�todo dice si para esta evoluci�n hay an�lisis
	 * @return
	 */
	public boolean getHayAnalisis()
	{
		if (!this.resultadosTratamiento.equals("")||!this.descripcionComplicacion.equals(""))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	/**
	 * Este m�todo dice si para esta evoluci�n hay plan
	 * @return
	 */
	public boolean getHayPlan()
	{
		if (!this.tratamiento.equals("")||!this.cambiosManejo.equals("")||!this.pronostico.equals("")||!this.observaciones.equals(""))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Este m�todo dice si para esta evoluci�n hay diagnosticos
	 * @return
	 */
	private boolean getHayDiagnosticos()
	{
		if (this.diagnosticosDefinitivos.size()==0&&this.diagnosticosPresuntivos.size()==0&&this.diagnosticoComplicacion.getAcronimo().equals("1"))
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	/**
	 * Retorna el numero de cuenta asociado al numero de solicitud dado por parametro
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static int getIdCuenta(Connection con, int numeroSolicitud) 
	{
		try
		{		
			EvolucionDao evolucionDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEvolucionDao();
			return evolucionDao.getIdCuenta(con, numeroSolicitud);
		}
		catch (SQLException sql)
		{
			
			logger.warn(sql);
			return -1;
		}	
	}	
	/**
	 * @return
	 */
	public int getCodigoValoracion() {
		return codigoValoracion;
	}

	/**
	 * @param i
	 */
	public void setCodigoValoracion(int i) {
		codigoValoracion = i;
	}

	/**
	 * M�todo que retorna la hora en que el usuario 
	 * efectivamente lleno esta evoluci�n
	 * @return
	 */
	public String getHoraGrabacionCincoCaracteres()
	{
		if (horaGrabacion==null)
		{
			return "";
		}
		else
		{
			return horaGrabacion.substring(0, 5);
		}
	}

	/**
	 * M�todo que retorna la fecha en que el usuario 
	 * grabo efectivamente esta evoluci�n
	 * @return
	 */
	public String getFechaGrabacionFormatoAp()
	{
		return fechaGrabacionFormatoAp;
	}

    /**
     * @return Returns the deboMostrarCancelacionTratante.
     */
    public boolean getDeboMostrarCancelacionTratante()
    {
        return deboMostrarCancelacionTratante;
    }
    /**
     * @param deboMostrarCancelacionTratante The deboMostrarCancelacionTratante to set.
     */
    public void setDeboMostrarCancelacionTratante(
            boolean deboMostrarCancelacionTratante)
    {
        this.deboMostrarCancelacionTratante = deboMostrarCancelacionTratante;
    }
    /**
     * @return Returns the recargo.
     */
    public int getRecargo()
    {
        return recargo;
    }
    /**
     * @param recargo The recargo to set.
     */
    public void setRecargo(int recargo)
    {
        this.recargo = recargo;
    }
    /**
     * @return Returns the nombreRecargo.
     */
    public String getNombreRecargo()
    {
        return nombreRecargo;
    }
    /**
     * @param nombreRecargo The nombreRecargo to set.
     */
    public void setNombreRecargo(String nombreRecargo)
    {
        this.nombreRecargo = nombreRecargo;
    }
    
    public boolean getLiquidos()
    {
    	return !(fechaYResultadoExamenesDiagnostico.equals("") &&
    			resultadosTratamiento.equals("") &&
				tratamiento.equals("") &&
				cambiosManejo.equals("") &&
				observaciones.equals(""));
    }

	/**
	 * @return Retorna tipoDiagnosticoPrincipal.
	 */
	public int getTipoDiagnosticoPrincipal()
	{
		return tipoDiagnosticoPrincipal;
	}
	/**
	 * @param tipoDiagnosticoPrincipal Asigna tipoDiagnosticoPrincipal.
	 */
	public void setTipoDiagnosticoPrincipal(int tipoDiagnosticoPrincipal)
	{
		this.tipoDiagnosticoPrincipal = tipoDiagnosticoPrincipal;
	}
	/**
	 * @return Retorna nombreTipoDiagnosticoPrincipal.
	 */
	public String getNombreTipoDiagnosticoPrincipal()
	{
		return nombreTipoDiagnosticoPrincipal;
	}
	/**
	 * @param nombreTipoDiagnosticoPrincipal Asigna nombreTipoDiagnosticoPrincipal.
	 */
	public void setNombreTipoDiagnosticoPrincipal(
			String nombreTipoDiagnosticoPrincipal)
	{
		this.nombreTipoDiagnosticoPrincipal = nombreTipoDiagnosticoPrincipal;
	}
    /**
     * @return Returns the convertirMedicoTratante.
     */
    public char getConvertirMedicoTratante() {
        return convertirMedicoTratante;
    }
    /**
     * @param convertirMedicoTratante The convertirMedicoTratante to set.
     */
    public void setConvertirMedicoTratante(char convertirMedicoTratante) {
        this.convertirMedicoTratante = convertirMedicoTratante;
    }
    
	/**
	 * M�todo para cargar el balance de liquidos de la evoluci�n
	 * @param con
	 * @param codigo
	 * @return
	 * @throws SQLException
	 */
	public Vector cargarEvolucionBalanceLiquidos(Connection con, int codigo)
	{
		try
		{
			Vector balance=new Vector();
			ResultSetDecorator resultado=evolucionDao.cargarEvolucionBalanceLiquidos(con, codigo);
			while(resultado.next())
			{
				Vector fila=new Vector();
				fila.add(resultado.getString("nombre"));
				fila.add(resultado.getInt("codigo")+"");
				fila.add(resultado.getString("valor"));
				balance.add(fila);
			}
			return balance;
		}
		catch (SQLException e)
		{
			logger.error("Error cargando el balance de liquidos : "+e);
			return null;
		}
	}

	/**
	 * @return Retorna balanceLiquidos.
	 */
	public Vector getBalanceLiquidos()
	{
		return balanceLiquidos;
	}

	/**
	 * @param balanceLiquidos Asigna balanceLiquidos.
	 */
	public void setBalanceLiquidos(Vector balanceLiquidos)
	{
		this.balanceLiquidos = balanceLiquidos;
	}

	/**
	 * @return the acronimoTipoReferencia
	 */
	public String getAcronimoTipoReferencia() {
		return acronimoTipoReferencia;
	}

	/**
	 * @param acronimoTipoReferencia the acronimoTipoReferencia to set
	 */
	public void setAcronimoTipoReferencia(String acronimoTipoReferencia) {
		this.acronimoTipoReferencia = acronimoTipoReferencia;
	}

	/**
	 * @return the codigoConductaSeguir
	 */
	public int getCodigoConductaSeguir() {
		return codigoConductaSeguir;
	}

	/**
	 * @param codigoConductaSeguir the codigoConductaSeguir to set
	 */
	public void setCodigoConductaSeguir(int codigoConductaSeguir) {
		this.codigoConductaSeguir = codigoConductaSeguir;
	}
	
	/**
	 * Metodo para Consultar el Parametro General de Controla Interpretacion
	 * @param con
	 * @return
	 */
	public static String consultarParametroControlaInterpretacion(Connection con)
	{
		return evolucionDao.consultarParametroControlaInterpretacion(con);
	}
	
	/**
	 * Metodo para Consultar las Especialidades de la Cuenta
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public HashMap consultarEspecialidadesCuenta(Connection con, int codigoCuenta) 
	{
		return evolucionDao.consultarEspecialidadesCuenta(con, codigoCuenta);
	}
 
	
	
	public HashMap validarEspecialidadesMedico(Connection con, int codigoMedicoEvo, int codigoMedicoSol) 
	{
		return evolucionDao.validarEspecialidadesMedico(con, codigoMedicoEvo, codigoMedicoSol);
	}
	
	
	
}