/*
 * @(#)Cuenta.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2002. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */

package com.princetonsa.mundo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;

import util.Answer;
import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.Encoder;
import util.InfoDatos;
import util.InfoDatosInt;
import util.ResultadoBoolean;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.ValoresPorDefecto;

import com.princetonsa.dao.CuentaDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.sqlbase.SqlBaseCuentaDao;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoDatosResponsableFacturacion;
import com.princetonsa.dto.facturacion.DtoFacturaAgrupada;
import com.princetonsa.dto.facturacion.DtoInstitucion;
import com.princetonsa.dto.historiaClinica.DtoValoracionObservaciones;
import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.dto.manejoPaciente.DtoCuentas;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.princetonsa.mundo.historiaClinica.Plantillas;
import com.princetonsa.mundo.historiaClinica.Valoraciones;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * Esta clase encapsula los atributos y la funcionalidad de la cuenta de un paciente.
 *
 * @version 1.0, Oct 16, 2002
 * @author 	<a href="mailto:Oscar@PrincetonSA.com">&Oacute;scar Andr&eacute;s L&oacute;pez P.</a>, <a href="mailto:Camilo@PrincetonSA.com">Camilo Andr&eacute;s Camacho P.</a>
 */

public class Cuenta 
{

	private static Logger logger = Logger.getLogger(Cuenta.class);
	
	/**
	 * Data Transfer Object de la cuenta
	 */
	DtoCuentas cuenta;
	
	/**
	 * Numero de identificacion de la cuenta.
	 */
	private String idCuenta;

	/**
	 * Ao de apertura de la cuenta.
	 */
	private String anioApertura;

	/**
	 * Mes de apertura de la cuenta.
	 */
	private String mesApertura;

	/**
	 * Dia de apertura de la cuenta.
	 */
	private String diaApertura;
	
	/**
	 *Hora de apertura de la cuenta 
	 */
	private String horaApertura;
	
	/**
	 * Estado actual de la cuenta (Cuenta Activa, Facturada, Cancelada, Con Devolucion, Factura Anulada, Cerrada, Factura Manual, Factura Manual Cancelada).
	 */
	private String estadoCuenta;

	/**
	 * Codigo del estado de la cuenta.
	 */
	private String codigoEstadoCuenta;

	/**
	 * Convenio por defecto para esta cuenta.
	 */
	private String convenio;

	/**
	 * Codigo del Convenio por defecto para esta cuenta
	 */
	private String codigoConvenio;

	/**
	 * Cdigo del monto.
	 */
	private String codigoMonto;

	/**
	 * Tipo de paciente.
	 */
	private String tipoPaciente;

	/**
	 * Cdigo del tipo de paciente
	 */
	private String codigoTipoPaciente;

	/**
	 * Vï¿½ de ingreso.
	 */
	private String viaIngreso;

	/**
	 * Cdigo de la vï¿½ de ingreso.
	 */
	private String codigoViaIngreso;

	/**
	 * Tipo de rï¿½imen.
	 */
	private String tipoRegimen;

	/**
	 * Cdigo del tipo de rï¿½imen
	 */
	private String codigoTipoRegimen;

	/**
	 * Tipo de afiliado.
	 */
	private String tipoAfiliado;

	/**
	 * Cdigo del tipo de afiliado.
	 */
	private String codigoTipoAfiliado;

	/**
	 * Estrato social.
	 */
	private String estrato;

	/**
	 * Cdigo del estrato social.
	 */
	private String codigoEstrato;

	/**
	 * Naturaleza del paciente.
	 */
	private String naturalezaPaciente;

	/**
	 * Cdigo de la naturaleza del paciente.
	 */
	private String codigoNaturalezaPaciente;

	/**
	 * Indica si se trata o no de un accidente de trï¿½sito.
	 */
	private boolean indicativoAccidenteTransito;
	
	/**
	 * Codigo Tipo Evento de la cuenta
	 */
	private String codigoTipoEvento;
	
	/**
	 * Nombre del tipo de evento
	 */
	private String nombreTipoEvento;
	
	/**
	 * Código del Convenio Arp Afiliado
	 */
	private String codigoArpAfiliado;
	
	/**
	 * Nombre del convenio Arp Afiliado
	 */
	private String nombreArpAfiliado;
	
	/**
	 * Indicador si la cuenta es de paciente desplazado
	 */
	private boolean desplazado;

	/**
	 * Nmero de pliza.
	 */
	private String numeroPoliza;

	/**
	 * Nmero de carnet.
	 */
	private String numeroCarnet;
	/**
	 * usuario que crea la cuenta
	 */
	private String usuario;
	
	/**
	 * Código origen admision
	 */
	private int codigoOrigenAdmision;
	
	/**
	 * Nombre origen admision
	 */
	private String nombreOrigenAdmision;
	/**
	 * codigo area
	 */
	private int codigoArea;
	
	/**
	 * Nombre del ï¿½rea
	 */
	private String nombreArea;
	
		
	private int codigoContrato;
	
	
	/**
	 * Codigo de la entidad subcontratada
	 * */
	private String codigoEntidadSub;
	
	

	/**
	 * Cdigo de ingreso.
	 */
	private int codigoIngreso;

	private String empresa;
	/**
	 * Datos del paciente asociado a esta cuenta
	 */
	private PersonaBasica paciente;

	/**
	 * El DAO usado por el objeto <code>Cuenta</code> para acceder a la fuente de datos.
	 */
	private static CuentaDao cuentaDao = null;
	
	//*************ATRIBUTOS ADICIONAL PACIENTE REFERIDO**************************
	/**
	 * Indica si la cuenta tendrï¿½ datos de paciente referido
	 */
	private boolean pacienteReferido;
	/**
	 * Instituciï¿½n que refiere
	 */
	private String institucionRefiere;
	/**
	 * Profesional que refiere
	 */
	private String profesionalRefiere;
	/**
	 * Especialidad que refiere
	 */
	private InfoDatos especialidadRefiere;
	//****************************************************************************
	//*********ATRIBUTOS PARA BUSQUEDA DE CUENTAS******************************
	/**
	 * Fecha inicial de apertura cuenta
	 */
	private String fechaInicial;
	/**
	 * Fecha Final de apertura cuenta
	 */
	private String fechaFinal;
	/**
	 * Cuenta inicial
	 */
	private int cuentaInicial;
	/**
	 * Cuenta final
	 */
	private int cuentaFinal;
	
	/**
	 * Centro de Atención
	 */
	private int centroAtencion;
	
	/**
	 * Código del centro de atención
	 */
	private int codigoCentroAtencion;
	
	/**
	 * Nombre del centro de atención
	 */
	private String nombreCentroAtencion;
	
	//***********************************************************

	/**
	 * Constructor vacio, necesario para poder usar esta clase como un JavaBean
	 */
	public Cuenta () {
		this.paciente = new PersonaBasica();
		this.clean();
		this.init();
	}
	
	/**
	 * Constructor DTO Cuenta, necesario para poder usar esta clase como un JavaBean
	 */
	public Cuenta (DtoCuentas cuenta) 
	{
		this.cuenta = cuenta;
		this.init();
	}

	/**
	 * Constructor que recibe todos los parï¿½etros en su forma codigo-nombre
	 * para que despuï¿½ el mï¿½odo inicializar variables los ponga en su estado
	 * real
	 * 
	 */
	public Cuenta (
			String anioApertura, String mesApertura, String diaApertura, 
			String estadoCuenta, String codigoEstadoCuenta, String convenio, 
			String tipoPaciente, String viaIngreso, String tipoRegimen, 
			String tipoAfiliado, String estrato, String naturalezaPaciente, 
			String codigoMonto, boolean indicativoAccidenteTransito,String tipoEvento, 
			String arpAfiliado,boolean desplazado,
			String numeroPoliza,String numeroCarnet,
			String origenAdmision,
			boolean pacienteReferido,
			String institucionRefiere, String profesionalRefiere, String especialidadRefiere,
			String usuario,int codigoArea,String contrato,PersonaBasica pac, int codigoIngreso)
	{
		this.anioApertura=anioApertura;
		this.mesApertura=mesApertura;
		this.diaApertura=diaApertura;
		this.estadoCuenta=estadoCuenta;
		this.codigoEstadoCuenta=codigoEstadoCuenta;
		this.convenio=convenio;
		this.tipoPaciente=tipoPaciente;
		this.viaIngreso=viaIngreso;
		this.tipoRegimen=tipoRegimen;
		this.tipoAfiliado=tipoAfiliado;
		this.estrato=estrato;
		this.naturalezaPaciente=naturalezaPaciente;
		this.codigoMonto=codigoMonto;
		this.indicativoAccidenteTransito=indicativoAccidenteTransito;
		this.numeroCarnet=numeroCarnet;
		this.numeroPoliza=numeroPoliza;
		this.usuario = usuario;
		this.paciente=pac;
		this.codigoIngreso=codigoIngreso;
		this.pacienteReferido = pacienteReferido;
		if(pacienteReferido)
		{
			this.institucionRefiere = institucionRefiere;
			this.profesionalRefiere = profesionalRefiere;
			this.especialidadRefiere = new InfoDatos(Integer.parseInt(especialidadRefiere.split(ConstantesBD.separadorSplit)[0]),especialidadRefiere.split(ConstantesBD.separadorSplit)[1]);
			
		}
		else
		{
			this.institucionRefiere = "";
			this.profesionalRefiere = "";
			this.especialidadRefiere = new InfoDatos(ConstantesBD.codigoEspecialidadMedicaNinguna,"");
		}
		this.codigoArea = codigoArea;
		this.codigoContrato = contrato.equals("")?0:Integer.parseInt(contrato);
		this.codigoTipoEvento = tipoEvento;
		this.codigoArpAfiliado = arpAfiliado;
		this.codigoOrigenAdmision = Integer.parseInt(origenAdmision);
		this.desplazado = desplazado;
		this.init();
	}

	/** Inicializa el acceso a bases de datos de este objeto */
	public void init()
	{
		/* Obtener el DAO que encapsula las operaciones de BD de este objeto */
		if(cuentaDao == null)
			cuentaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCuentaDao();
	}

	/**
	 * Método que instanca el dato de cuenta
	 * @return
	 */
	public static CuentaDao cuentaDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCuentaDao();
	}
	
	
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores validos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init (String tipoBD) {

		if (cuentaDao == null) {
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			cuentaDao = myFactory.getCuentaDao();
		}

	}



	/**
	 * Retorna el ao de apertura de la cuenta.
	 * @return el ao de apertura
	 */
	public String getAnioApertura() {
		return anioApertura;
	}

	/**
	 * Retorna el codigo de la cuenta.
	 * @return el codigo de la cuenta
	 */
	public String getIdCuenta() {
		return idCuenta;
	}

	/**
	 * Retorna el codigo del estado de la cuenta.
	 * @return el codigo del estado de la cuenta
	 */
	public String getCodigoEstadoCuenta() {
		return codigoEstadoCuenta;
	}

	/**
	 * Retorna el dia de apertura de la cuenta.
	 * @return el dia de apertura
	 */
	public String getDiaApertura() {
		return diaApertura;
	}

	/**
	 * Retorna el estado de la cuenta del paciente.
	 * @param encoded especifica si se debe o no retornar esta cadena como <i>character entities</i>
	 * de HTML (e.g., "ï¿½ como "&amp;aacute;"
	 * @return eel estado de la cuenta
	 */
	public String getEstadoCuenta(boolean encoded) {
		if (encoded) {
			return getEstadoCuenta();
		}
		else {
			return estadoCuenta;
		}
	}

	/**
	 * Retorna el estado de la cuenta.
	 * (codificado como <i>character entities</i> de HTML, e.g., "ï¿½ como "&amp;aacute;").
	 * @return el estado de la cuenta
	 */
	public String getEstadoCuenta() {
		return Encoder.encode(estadoCuenta);
	}

	/**
	 * Retorna el mes de apertura de la cuenta.
	 * @return el mes de apertura
	 */
	public String getMesApertura() {
		return mesApertura;
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
	 * de HTML (e.g., "ï¿½ como "&amp;aacute;"
	 * @return el nombre del paciente
	 */
	public String getNombrePaciente(boolean encoded) {
		return paciente.getNombrePersona(encoded);
	}

	/**
	 * Retorna el nombre del paciente (completo, incluye apellidos).
	 * (codificado como <i>character entities</i> de HTML, e.g., "ï¿½ como "&amp;aacute;").
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
	 * de HTML (e.g., "ï¿½ como "&amp;aacute;"
	 * @return el tipo de identificacion del paciente
	 */
	public String getTipoIdentificacionPaciente(boolean encoded) {
		return paciente.getTipoIdentificacionPersona(encoded);
	}

	/**
	 * Retorna el tipo de identificacion del paciente.
	 * (codificado como <i>character entities</i> de HTML, e.g., "ï¿½ como "&amp;aacute;").
	 * @return el tipo de identificacion del paciente
	 */
	public String getTipoIdentificacionPaciente() {
		return paciente.getTipoIdentificacionPersona();
	}

	/**
	 * Retorna el codigo del convenio.
	 * @return codigo del convenio
	 */
	public String getCodigoConvenio() {
		return codigoConvenio;
	}

	/**
	 * Retorna el convenio (texto).
	 * @return el nombre del convenio
	 */
	public String getConvenio() {
		return convenio;
	}

	/**
	 * Retorna el codigo del estrato.del paciente por el que se genera esta cuenta
	 * @return String con el codigo del estrato
	 */
	public String getCodigoEstrato() {
		return codigoEstrato;
	}

	/**
	 * Retorna el codigo de la naturaleza del paciente por el que se genera esta cuenta
	 * @return String con el codigo de la naturaleza del paciente
	 */
	public String getCodigoNaturalezaPaciente() {
		return codigoNaturalezaPaciente;
	}

	/**
	 * Retorna el cdigo del tipo de afiliacin que tiene el paciente por el que se genera esta cuenta
	 * @return String con el cdigo del tipo de afiliacin
	 */
	public String getCodigoTipoAfiliado() {
		return codigoTipoAfiliado;
	}

	/**
	 * Retorna el cdigo del tipo de paciente por el que se genera esta cuenta
	 * @return String con el cdigo del tipo de paciente
	 */
	public String getCodigoTipoPaciente() {
		return codigoTipoPaciente;
	}

	/**
	 * Retorna el codigo del tipo de regimen al que pertenece el paciente por el que se genera esta cuenta
	 * @return String con el codigo del tipo de regimen
	 */
	public String getCodigoTipoRegimen() {
		return codigoTipoRegimen;
	}

	/**
	 * Retorna el codigo de la via de ingreso del paciente por el que se genera esta cuenta.
	 * @return String con el codigo de la via de ingreso
	 */
	public String getCodigoViaIngreso() {
		return codigoViaIngreso;
	}

	/**
	 * Retorna el estrato del paciente por el que se genera esta cuenta
	 * @return String con el estrato del paciente
	 */
	public String getEstrato() {
		return estrato;
	}

	/**
	 * Retorna el hecho si el paciente entr por accidente de trï¿½sito.
	 * @return boolean
	 */
	public boolean isIndicativoAccidenteTransito() {
		return indicativoAccidenteTransito;
	}

	/**
	 * Retorna la naturaleza del paciente por el que se genera esta cuenta.
	 * @return String con la naturaleza del paciente
	 */
	public String getNaturalezaPaciente() {
		return naturalezaPaciente;
	}

	/**
	 * Retorna el numero del carnet del paciente por el que se genera esta cuenta
	 * @return String con el numero del carnet
	 */
	public String getNumeroCarnet() {
		return numeroCarnet;
	}

	/**
	 * Retorna el numero de poliza que tiene el paciente por el que se genera esta cuenta
	 * @return String con el numero de poliza
	 */
	public String getNumeroPoliza() {
		return numeroPoliza;
	}

	/**
	 * Retorna los datos bï¿½icos de un paciente, en un objeto de tipo <code>PersonaBasica</code>.
	 * @return PersonaBasica
	 */
	public PersonaBasica getPaciente() {
		return paciente;
	}

	/**
	 * Retorna el tipo de afiliacin que tiene el paciente por el que se genera esta cuenta
	 * @return String con el tipo de afiliacin
	 */
	public String getTipoAfiliado() {
		return tipoAfiliado;
	}

	/**
	 * Retorna el tipo del paciente, por quien se estï¿½creando esta cuenta.
	 * @return String con el tipo del paciente
	 */
	public String getTipoPaciente() {
		return tipoPaciente;
	}

	/**
	 * Retorna el tipo de regimen del paciente, por quien se estï¿½creando esta cuenta.
	 * @return String con el tipo de regimen del paciente
	 */
	public String getTipoRegimen() {
		return tipoRegimen;
	}

	/**
	 * Retorna la via de ingreso del paciente, por quien se estï¿½creando esta cuenta.
	 * @return String con la via de ingreso del paciente
	 */
	public String getViaIngreso() {
		return viaIngreso;
	}

	/**
	 * Retorna el codigo del ingreso al que estï¿½asociada esta cuenta.
	 * @return int con el codigo del ingreso
	 */
	public int getCodigoIngreso() {
		return codigoIngreso;
	}

	/**
	 * Retorna el cdigo del monto.
	 * @return el cdigo del monto
	 */
	public String getCodigoMonto() {
		return codigoMonto;
	}

	/**
	 * Establece el ao de apertura de la cuenta.
	 * @param anioApertura el ao de apertura a establecer
	 */
	public void setAnioApertura(String anioApertura) {
		this.anioApertura = anioApertura;
	}

	/**
	 * Establece el codigo de la cuenta.
	 * @param idCuenta el codigo de la cuenta a establecer
	 */
	public void setIdCuenta(String codigoCuenta) {
		this.idCuenta = codigoCuenta;
	}

	/**
	 * Establece el codigo del estado de la cuenta.
	 * @param codigoEstadoCuenta el codigo del estado de la cuenta a establecer
	 */
	public void setCodigoEstadoCuenta(String codigoEstadoCuenta) {
		this.codigoEstadoCuenta = codigoEstadoCuenta;
	}

	/**
	 * Establece el dia de apertura de la cuenta.
	 * @param diaApertura el dia de apertura a establecer
	 */
	public void setDiaApertura(String diaApertura) {
		this.diaApertura = diaApertura;
	}

	/**
	 * Establece el estado de la cuenta.
	 * @param estadoCuenta el estado de la cuenta a establecer
	 */
	public void setEstadoCuenta(String estadoCuenta) {
		this.estadoCuenta = estadoCuenta;
	}

	/**
	 * Establece el mes de apertura de la cuenta.
	 * @param mesApertura el mes de apertura a establecer
	 */
	public void setMesApertura(String mesApertura) {
		this.mesApertura = mesApertura;
	}

	/**
	 * Establece el codigo del convenio.
	 * @param codigoConvenio El codigo del convenio a establecer
	 */
	public void setCodigoConvenio(String codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	/**
	 * Establece el convenio.
	 * @param convenio El convenio a establecer
	 */
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}

	/**
	 * Establece el codigo del estrato del paciente, por quien se estï¿½creando esta cuenta.
	 * @param codigoEstrato El codigo del estrato a establecer
	 */
	public void setCodigoEstrato(String codigoEstrato) {
		this.codigoEstrato = codigoEstrato;
	}

	/**
	 * Establece el codigo de la naturaleza del paciente, por quien se estï¿½creando esta cuenta.
	 * @param codigoNaturalezaPaciente The codigoNaturalezaPaciente a establecer
	 */
	public void setCodigoNaturalezaPaciente(String codigoNaturalezaPaciente) {
		this.codigoNaturalezaPaciente = codigoNaturalezaPaciente;
	}

	/**
	 * Establece el codigo del tipo de afiliado del paciente, por quien se estï¿½creando esta cuenta.
	 * @param codigoTipoAfiliado El codigo del tipo de afiliado del paciente a establecer
	 */
	public void setCodigoTipoAfiliado(String codigoTipoAfiliado) {
		this.codigoTipoAfiliado = codigoTipoAfiliado;
	}

	/**
	 * Establece el codigo del tipo del paciente, por quien se estï¿½creando esta cuenta.
	 * @param codigoTipoPaciente El codigo del tipo de paciente a establecer
	 */
	public void setCodigoTipoPaciente(String codigoTipoPaciente) {
		this.codigoTipoPaciente = codigoTipoPaciente;
	}

	/**
	 * Establece el codigo del tipo de regimen por la que ingreso paciente, por quien se estï¿½creando esta cuenta.
	 * @param codigoTipoRegimen El codigo del tipo de regimen a establecer
	 */
	public void setCodigoTipoRegimen(String codigoTipoRegimen) {
		this.codigoTipoRegimen = codigoTipoRegimen;
	}

	/**
	 * Establece el codigo de la via por la que ingreso paciente, por quien se estï¿½creando esta cuenta.
	 * @param codigoViaIngreso El codigo de la via a establecer
	 */
	public void setCodigoViaIngreso(String codigoViaIngreso) {
		this.codigoViaIngreso = codigoViaIngreso;
	}

	/**
	 * Establece el estrato del paciente, por quien se estï¿½creando esta cuenta.
	 * @param estrato El estrato a establecer
	 */
	public void setEstrato(String estrato) {
		this.estrato = estrato;
	}

	/**
	 * Establece el hecho si el paciente entr por accidente de trï¿½sito
	 * @param indicativoAccidenteTransito El indicativo del accidente de transito a establecer
	 */
	public void setIndicativoAccidenteTransito(boolean indicativoAccidenteTransito) {
		this.indicativoAccidenteTransito = indicativoAccidenteTransito;
	}

	/**
	 * Establece la naturaleza del paciente, por quien se estï¿½creando esta cuenta.
	 * @param naturalezaPaciente The naturalezaPaciente a establecer
	 */
	public void setNaturalezaPaciente(String naturalezaPaciente) {
		this.naturalezaPaciente = naturalezaPaciente;
	}

	/**
	 * Establece el numero del carnet del paciente, por quien se estï¿½creando esta cuenta.
	 * @param numeroCarnet El numero de carnet a establecer
	 */
	public void setNumeroCarnet(String numeroCarnet) {
		this.numeroCarnet = numeroCarnet;
	}

	/**
	 * Establece el numero de poliza del paciente, por quien se estï¿½creando esta cuenta.
	 * @param numeroPoliza El numeroPoliza a establecer
	 */
	public void setNumeroPoliza(String numeroPoliza) {
		this.numeroPoliza = numeroPoliza;
	}

	/**
	 * Establece el paciente.
	 * @param paciente El paciente a establecer
	 */
	public void setPaciente(PersonaBasica paciente) {
		this.paciente = paciente;
	}

	/**
	 * Establece el tipo de afiliacin.del paciente, por quien se estï¿½creando esta cuenta.
	 * @param tipoAfiliado E tipo de afiliacin a establecer
	 */
	public void setTipoAfiliado(String tipoAfiliado) {
		this.tipoAfiliado = tipoAfiliado;
	}

	/**
	 * Establece el tipo del paciente, por quien se estï¿½creando esta cuenta.
	 * @param tipoPaciente El tipo del paciente a establecer
	 */
	public void setTipoPaciente(String tipoPaciente) {
		this.tipoPaciente = tipoPaciente;
	}

	/**
	 * Establece el tipo de regimen del paciente, por quien se estï¿½creando esta cuenta.
	 * @param tipoRegimen El tipo de regimen a establecer
	 */
	public void setTipoRegimen(String tipoRegimen) {
		this.tipoRegimen = tipoRegimen;
	}

	/**
	 * Establece la via de ingreso del paciente, por quien se estï¿½creando esta cuenta.
	 * @param viaIngreso La via de ingreso a establecer
	 */
	public void setViaIngreso(String viaIngreso) {
		this.viaIngreso = viaIngreso;
	}

	/**
	 * Establece el codigo del ingreso al que estï¿½asociada esta cuenta.
	 * @param codigoIngreso El codigo del ingreso a establecer
	 */
	public void setCodigoIngreso(int codigoIngreso) {
		this.codigoIngreso = codigoIngreso;
	}

	/**
	 * Establece el cdigo del monto.
	 * @param codigoMonto el cdigo del monto a establecer
	 */
	public void setCodigoMonto(String codigoMonto) {
		this.codigoMonto = codigoMonto;
	}

	/**
	 * Este metodo inicializa en valores vacios (mas no nulos) los atributos de la cuenta.
	 */
	public void clean () {

		this.paciente.clean();
		this.setAnioApertura("");
		this.setIdCuenta("");
		this.setCodigoEstadoCuenta("");
		this.setDiaApertura("");
		this.setEstadoCuenta("");
		this.setMesApertura("");
		this.setCodigoConvenio("");
		this.setConvenio("");
		this.setCodigoTipoEvento("");
		this.setNombreTipoEvento("");
		this.setCodigoArpAfiliado("");
		this.setNombreArpAfiliado("");
		this.setDesplazado(false);
		this.setNumeroCarnet("");
		this.setNumeroPoliza("");
		this.usuario = "";
		this.codigoArea = 0;
		this.nombreArea = "";
		this.codigoOrigenAdmision = 0;
		this.nombreOrigenAdmision = "";
		this.codigoContrato = 0;
		
		//**datos paciente referido*****
		this.pacienteReferido = false;
		this.institucionRefiere = "";
		this.profesionalRefiere = "";
		this.especialidadRefiere = new InfoDatos(ConstantesBD.codigoEspecialidadMedicaNinguna,"");
		
		
		//***datos consulta cuentas*********
		this.fechaInicial = "";
		this.fechaFinal = "";
		this.cuentaInicial = 0;
		this.cuentaFinal = 0;
		
		this.codigoCentroAtencion=0;
		this.nombreCentroAtencion="";
		this.cuenta = new DtoCuentas();
		this.codigoEntidadSub = "";

	}

	/**
	 * Inicializa todas las variables del ingreso que puedan venir en la forma "codigo-nombre".
	 */
	public void inicializarVariables () {

		String [] resultados;

		//Primero los datos del convenio
		resultados = UtilidadTexto.separarNombresDeCodigos(convenio, 1);
		codigoConvenio = resultados[0];
		convenio = resultados[1];

		//Ahora los datos del tipo de paciente
		resultados = UtilidadTexto.separarNombresDeCodigos(tipoPaciente, 1);
		codigoTipoPaciente= resultados[0];
		tipoPaciente = resultados[1];

		//Ahora la via de ingreso
		resultados = UtilidadTexto.separarNombresDeCodigos(viaIngreso, 1);
		codigoViaIngreso= resultados[0];
		viaIngreso= resultados[1];

		//Ahora el tipo de Regimen
		resultados = UtilidadTexto.separarNombresDeCodigos(tipoRegimen, 1);
		codigoTipoRegimen= resultados[0];
		tipoRegimen= resultados[1];

		//Ahora el tipo de Afiliado
		resultados = UtilidadTexto.separarNombresDeCodigos(tipoAfiliado, 1);
		codigoTipoAfiliado= resultados[0];
		tipoAfiliado= resultados[1];

		//Ahora el estrato
		resultados = UtilidadTexto.separarNombresDeCodigos(estrato, 1);
		codigoEstrato= resultados[0];
		estrato= resultados[1];

		//Ahora la naturaleza del Paciente
		resultados = UtilidadTexto.separarNombresDeCodigos(naturalezaPaciente, 1);
		codigoNaturalezaPaciente= resultados[0];
		naturalezaPaciente= resultados[1];

	}

	
	/**
	 * Mï¿½todo que carga la fecha de creaciï¿½n de la cuenta
	 * 
	 * @param con Conexiï¿½n con la fuente de datos
	 * @param idCuenta Nï¿½mero de la cuenta
	 * @throws SQLException
	 */
	public void cargarFechaCreacionCuenta (Connection con, int idCuenta) throws SQLException 
	{
		String arregloFechaCuenta[]=(cuentaDao.buscarFechaCreacionCuenta(con, idCuenta)).split("-",3);
		this.anioApertura=arregloFechaCuenta[0];
		this.mesApertura=arregloFechaCuenta[1];
		this.diaApertura=arregloFechaCuenta[2];
	}

	/**
	 * Dada una identificacion de una cuenta, establece las propiedades de un objeto <code>Cuenta</code>
	 * en los valores correspondientes.
	 * @param con una conexion abierta con una fuente de datos
	 * @param idCuenta numero de identificacion de la cuenta que se desea cargar
	 */
	public void cargarCuenta(Connection con, String idCuenta)  
	{

		try
		{
			this.clean();
			Answer ans = cuentaDao.cargarCuenta(con, idCuenta);
			ResultSetDecorator rs = ans.getResultSet();
	
			if (rs.next()) {
	
				setIdCuenta(idCuenta);
				paciente.setNumeroIdentificacionPersona(rs.getString("numeroIdentificacion"));
				paciente.setCodigoTipoIdentificacionPersona(rs.getString("codigoTipoIdentificacion"));
				paciente.setTipoIdentificacionPersona(rs.getString("tipoIdentificacion"));
				paciente.setNombrePersona(rs.getString("primerNombre") + " " + rs.getString("segundoNombre") + " " + rs.getString("primerApellido") + " " + rs.getString("segundoApellido"));
				paciente.setCodigoPersona(rs.getInt("codigoPaciente"));
				String [] fecha = UtilidadTexto.separarNombresDeCodigos(rs.getString("fechaApertura"), 2);
				setAnioApertura(fecha[0]);
				setMesApertura(fecha[1]);
				setDiaApertura(fecha[2]);
				setHoraApertura(rs.getString("horaApertura"));
				setCodigoEstadoCuenta(rs.getString("codigoEstadoCuenta"));
				setEstadoCuenta(rs.getString("estadoCuenta"));
				setCodigoConvenio(rs.getString("codigoConvenio"));
				setConvenio(rs.getString("convenio"));
				setCodigoTipoPaciente(rs.getString("codigoTipoPaciente"));
				setTipoPaciente(rs.getString("tipoPaciente"));
				setCodigoViaIngreso(rs.getString("codigoViaIngreso"));
				setViaIngreso(rs.getString("viaIngreso"));
				setCodigoTipoRegimen(rs.getString("codigoTipoRegimen"));
				setTipoRegimen(rs.getString("tipoRegimen"));
				setCodigoTipoAfiliado(rs.getString("codigoTipoAfiliado"));
				setTipoAfiliado(rs.getString("tipoAfiliado"));
				setCodigoEstrato(rs.getString("codigoEstrato"));
				setEstrato(rs.getString("estrato"));
				setCodigoNaturalezaPaciente(rs.getString("codigoNaturalezaPaciente"));
				setNaturalezaPaciente(rs.getString("naturalezaPaciente"));
				setNumeroPoliza(rs.getString("numeroPoliza"));
				setNumeroCarnet(rs.getString("numeroCarnet"));
				setUsuario(rs.getString("usuario"));
				setCodigoIngreso(rs.getInt("codigoIngreso"));
				setCodigoMonto(rs.getString("codigoMontoCobro"));
				setCodigoArea(rs.getInt("codigoArea"));
				setNombreArea(rs.getString("nombreArea"));
				
				setCodigoContrato(rs.getInt("codigoContrato"));
				empresa=rs.getString("empresa");
				setCodigoCentroAtencion(rs.getInt("codigoCentroAtencion"));
				setNombreCentroAtencion(rs.getString("nombreCentroAtencion"));
				this.setIndicativoAccidenteTransito(rs.getBoolean("IndicativoAccidente"));
				setCodigoTipoEvento(rs.getString("codigoTipoEvento"));
				setNombreTipoEvento(ValoresPorDefecto.getIntegridadDominio(getCodigoTipoEvento())==null?"":ValoresPorDefecto.getIntegridadDominio(getCodigoTipoEvento()).toString());
				setCodigoArpAfiliado(rs.getString("codigoConvenioArp"));
				setNombreArpAfiliado(rs.getString("nombreConvenioArp"));
				setDesplazado(UtilidadTexto.getBoolean(rs.getString("desplazado")));
				setCodigoOrigenAdmision(rs.getInt("codigoOrigenAdmision"));
				setNombreOrigenAdmision(rs.getString("nombreOrigenAdmision"));
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarCuenta de Cuenta:"+e);
		}

	}

	public void cargarAfiliacionClasificacionSocEconomica(Connection con, String idCuenta,String idIngreso, String idConvenio){
		try {
			Answer ans = cuentaDao.cargarAfiliacionClasificacionSocEconomica(con, idCuenta, idIngreso, idConvenio);
			ResultSetDecorator rs = ans.getResultSet();			
			
			if (rs.next()) {
				setIdCuenta(idCuenta);
				
				setCodigoTipoAfiliado(rs.getString("codigoTipoAfiliado"));
				setTipoAfiliado(rs.getString("tipoAfiliado"));
				
				setCodigoConvenio(rs.getString("codigoConvenio"));
				setConvenio(rs.getString("convenio"));
				
				setCodigoEstrato(rs.getString("codigoEstrato"));
				setEstrato(rs.getString("estrato"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	/**
	 * Mï¿½todo que cambia el estado de una cuenta a asociada
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param idCuenta Nï¿½mero de la cuenta a la cual se quiere
	 * dejar en estado asociado
	 * @param usuario Login del usuario que realiza el asocio de
	 * cuenta
	 * @return
	 * @throws SQLException
	 */
	public int asociarCuenta (Connection con, int idCuenta,int idIngreso, String usuario) throws SQLException
	{
		return cuentaDao.asociarCuenta(con, idCuenta,idIngreso, usuario);
	}
	
	
	/**
	 * Método que realiza el asocio de cuenta estático
	 * @param con
	 * @param idCuenta
	 * @param idIngreso
	 * @param usuario
	 * @return
	 */
	public static int asociarCuentaEstatico (Connection con, int idCuenta,int idIngreso, String usuario)
	{
		try
		{
			return cuentaDao().asociarCuenta(con, idCuenta,idIngreso, usuario);
		}
		catch(SQLException e)
		{
			logger.error("Error realizando el asocio de cuenta: "+e);
			return ConstantesBD.codigoNuncaValido;
		}
	}

	/**
	 * Mï¿½todo que desactiva un asocio
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente
	 * @param usuario Login del usuario que realiza el asocio de
	 * cuenta
	 * @return
	 * @throws SQLException
	 */
	public int desAsociarCuenta (Connection con, String usuario, String idIngreso, String viaIngreso) throws SQLException
	{
		return cuentaDao.desAsociarCuenta(con, usuario, idIngreso,  viaIngreso);
	}

	/**
	 * Mï¿½todo que deshace el asocio en la facturacion independiente
	 * @param con
	 * @param codigoCuentaFinal
	 * @param usuario
	 * @return
	 */
	public int desAsociarCuentaFacturacion (Connection con, int codigoCuentaFinal, String usuario)
	{
		return cuentaDao.desAsociarCuentaFacturacion(con, codigoCuentaFinal, usuario);
	}

	/**
	 * Mï¿½todo que completa el proceso de asocio de cuenta (almacenar
	 * la cuenta final), soportando definiciï¿½n de estado de transacciï¿½n en
	 * una transacciï¿½n mayor
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPaciente Cï¿½digo del paciente sobre el que se
	 * desea realizar la operaciï¿½n
	 * @param nuevaCuenta Cï¿½digo de la cuenta final
	 * @param estado Estado de la transacciï¿½n
	 * @param institucion -> Instituciï¿½n del mï¿½dico  tratante
	 * @param codigoFarmacia 
	 * @param existenMedicamentosXDespachar 
	 * @return
	 * @throws SQLException
	 */
	public int completarAsocioCuentaTransaccional(Connection con, String idIngreso, int nuevaCuenta, int codigoMedicoTratante, int codigoCentroCostoTratante, String estado, int institucion, String viaIngreso, boolean existenMedicamentosXDespachar, int codigoFarmacia) 
	{
		int codigoCuentaVieja=0;
		try
		{
			if (estado.equals("finalizar"))
			{
				//Como hay una operaciï¿½n mas abajo, en caso de finalizar
				//es la otra operaciï¿½n la que se debe encargar de terminar
				//la transacciï¿½n 
				codigoCuentaVieja=cuentaDao.completarAsocioCuentaTransaccional(con, nuevaCuenta, "continuar", idIngreso, viaIngreso) ;
			}
			else
			{
				codigoCuentaVieja=cuentaDao.completarAsocioCuentaTransaccional(con,  nuevaCuenta, estado, idIngreso,  viaIngreso) ;
			}
		
			//El completar asocio siempre me devuelve un nï¿½mero vï¿½lido, si
			//no el mismo considera que hubo un error y se encarga de manejar
			//los problemas de transacciï¿½n
			if (codigoCuentaVieja>0)
			{
				
				Solicitud sol= new Solicitud ();
				
				
				if (estado.equals("empezar"))
				{
					//Si me dicen empezar esto ya lo hizo la operaciï¿½n de cuenta
					sol.moverPorAsocio(con, codigoCuentaVieja, nuevaCuenta, codigoMedicoTratante, codigoCentroCostoTratante, "continuar", institucion);
				}
				else
				{
					sol.moverPorAsocio(con, codigoCuentaVieja, nuevaCuenta, codigoMedicoTratante, codigoCentroCostoTratante, estado, institucion);
				}
				
				if(existenMedicamentosXDespachar)
					if(sol.actualizarCentrosCostoMedicamentosXDespachar(con, nuevaCuenta, codigoCentroCostoTratante, codigoFarmacia)<=0)
						codigoCuentaVieja=0;
				
				
			}
		}
		catch (SQLException e)
		{
			logger.error("Excepciï¿½n en completarAsocioCuentaTransaccional " + e);
		}
		
		return codigoCuentaVieja;
	}




	public int copiarValoracionAsocioCuenta (
			Connection con, 
			int numeroSolicitudFuturaValoracionHosp, 
			int codigoCuentaVieja, 
			int codigoIngreso, 
			String fechaAUsar, 
			String horaAUsar,
			String viaIngresoCuentaVieja, PersonaBasica paciente, UsuarioBasico usuarioActual)  
	{
		int numeroSolicitudValoracionUrgencias=0;
		logger.info("valor de via ingreso cuenta vieja >> "+viaIngresoCuentaVieja);
		try
		{
			//Se consulta la información parametrizable
			DtoPlantilla plantilla = Plantillas.cargarPlantillaXSolicitud(
				con, 
				usuarioActual.getCodigoInstitucionInt(), 
				viaIngresoCuentaVieja.equals(ConstantesBD.codigoViaIngresoHospitalizacion+"")?ConstantesCamposParametrizables.funcParametrizableValoracionHospitalizacion:ConstantesCamposParametrizables.funcParametrizableValoracionUrgencias, 
				ConstantesBD.codigoNuncaValido, 
				ConstantesBD.codigoNuncaValido, 
				ConstantesBD.codigoNuncaValido, 
				Plantillas.obtenerCodigoPlantillaXIngreso(con, paciente.getCodigoIngreso(), paciente.getCodigoPersona(), numeroSolicitudValoracionUrgencias), 
				true, 
				paciente.getCodigoPersona(), 
				paciente.getCodigoIngreso(), 
				numeroSolicitudValoracionUrgencias,
				ConstantesBD.codigoNuncaValido,
				ConstantesBD.codigoNuncaValido,
				false);
			
			Valoraciones mundoValoracion = new Valoraciones();
			mundoValoracion.setPlantilla(plantilla);
			
			
			
			numeroSolicitudValoracionUrgencias=UtilidadValidacion.obtenerNumeroSolicitudPrimeraValoracion(con, codigoCuentaVieja);
			logger.info("NUMERO DE SOLICITUD CUENTA ANTERIOR=> "+numeroSolicitudValoracionUrgencias);
			mundoValoracion.setNumeroSolicitud(numeroSolicitudValoracionUrgencias+"");
			mundoValoracion.cargarHospitalizacion(con, usuarioActual, paciente, false);
			
			mundoValoracion.getValoracionHospitalizacion().setNumeroSolicitud(numeroSolicitudFuturaValoracionHosp+"");
			mundoValoracion.getValoracionHospitalizacion().setFechaValoracion(fechaAUsar);
			mundoValoracion.getValoracionHospitalizacion().setHoraValoracion(horaAUsar);
			
			//Se limpian los consecutivos de las observaciones para que se puedan insertar
			for(DtoValoracionObservaciones observacion:mundoValoracion.getValoracionHospitalizacion().getObservaciones())
				observacion.setConsecutivo(""); 
			
			logger.info("valor entro a auxiliar caso asocio numero de observaciones encontradas ");
			String diagnosticoTexto[]=UtilidadValidacion.obtenerDiagnosticoParaNuevaValoracionHospEnAsocioCuenta (con, codigoCuentaVieja,viaIngresoCuentaVieja) ;
			logger.info("valor del diagnostico >> "+diagnosticoTexto[0]+" >> "+diagnosticoTexto[1]);
			//logger.info("Codigo paciente DtoHistoriaMenstrual justo despues de consultar=> "+mundoValoracion.getValoracionHospitalizacion().getHistoriaMenstrual().getCodigoPaciente());
			
			
			
			Diagnostico diagIngreso=new Diagnostico();
			diagIngreso.setAcronimo(diagnosticoTexto[0]);
			diagIngreso.setTipoCIE(Integer.parseInt(diagnosticoTexto[1]));
			
			mundoValoracion.getValoracionHospitalizacion().setDiagnosticoIngreso(diagIngreso);
			mundoValoracion.getValoracionHospitalizacion().setCodigoOrigenAdmision(ConstantesBD.codigoOrigenAdmisionHospitalariaEsUrgencias);
			
			
			
			//Se eliminan los diagnosticos relacionados del mapa porque todavía están en el ArrayList del DTO
			HashMap diagRelacionados = new HashMap();
			diagRelacionados.put("numRegistros","0");
			mundoValoracion.setDiagnosticosRelacionados(diagRelacionados);
			
			logger.info("diagnosticos => "+mundoValoracion.getValoracionHospitalizacion().getDiagnosticos().size());
			
			//logger.info("Codigo paciente DtoHistoriaMenstrual justo antes de insertar=> "+mundoValoracion.getValoracionHospitalizacion().getHistoriaMenstrual().getCodigoPaciente());
			mundoValoracion.insertarHospitalizacion(con, paciente, false);
			
			Iterator iterador = mundoValoracion.getErrores().get();
			while(iterador.hasNext())
			{
				ActionMessage mensaje = (ActionMessage)iterador.next();
				logger.info("Error enconstrado: "+mensaje.getKey());
				for(int i=0;i<mensaje.getValues().length;i++)
					logger.info("Atributo ["+i+"]: "+mensaje.getValues()[i]);
			}
			
			return mundoValoracion.getErrores().isEmpty()?1:0;
			
			
		}
		catch(SQLException e)
		{
			logger.error("error en copiarValoracionAsocioCuenta: "+e);
			return 0;
		}		
	}
	
	/**Prueba de transaccional que no sirve
	public int copiarValoracionAsocioCuentaTransaccional (Connection con, int numeroSolicitudFuturaValoracionHosp, int codigoCuentaVieja, int codigoIngreso, String estado) throws SQLException
	{
		int numeroSolicitudValoracionUrgencias=0, resp=0;
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		if (estado.equals("empezar"))
		{
			myFactory.beginTransaction(con);
		}

		try
		{
			int respuestaValoracion[]=ValoracionUrgencias.getCodigoYEsPediatricaValoracionUrgencias(con, codigoCuentaVieja);
			numeroSolicitudValoracionUrgencias=respuestaValoracion[0];

			//Revisamos si fuï¿½ una Valoraciï¿½n Urgencias Pedï¿½atrica o no
			if (respuestaValoracion[1]==0)
			{
				//No fuï¿½ Pediatrica
				.println("NO Fuï¿½ Val Pediatrica" + numeroSolicitudValoracionUrgencias);
				ValoracionInterconsulta val=new ValoracionInterconsulta(new Valoracion());
				val.setNumeroSolicitud(numeroSolicitudValoracionUrgencias);
				val.cargar(con);

				resp=this.auxiliarCasoAsocio(con, codigoCuentaVieja, numeroSolicitudFuturaValoracionHosp, codigoIngreso, val);
			}
			else
			{
			
				ValoracionPediatrica valPediatrica=new ValoracionPediatrica(new ValoracionInterconsulta(new Valoracion()));
				valPediatrica.setNumeroSolicitud(numeroSolicitudValoracionUrgencias);
				valPediatrica.cargar(con);
			
				resp=this.auxiliarCasoAsocio(con, codigoCuentaVieja, numeroSolicitudFuturaValoracionHosp, codigoIngreso, valPediatrica);
			}
		
			if (resp==0)
			{
				myFactory.abortTransaction(con);
				logger.error("No fuï¿½ posible copiar la valoraciï¿½n de hospitalizaciï¿½n a urgencias");
				throw new SQLException ("No fuï¿½ posible copiar la valoraciï¿½n de hospitalizaciï¿½n a urgencias");
			}
			else
			{
				if (estado.equals("finalizar"))
				{
					myFactory.endTransaction(con);
					return resp;
				}
			}
		}
		catch (SQLException e)
		{
			myFactory.abortTransaction(con);
			logger.error("No fuï¿½ posible copiar la valoraciï¿½n de hospitalizaciï¿½n a urgencias" + e);
			throw new SQLException ("No fuï¿½ posible copiar la valoraciï¿½n de hospitalizaciï¿½n a urgencias");
		}
		return resp;
	}
	Error que se obtiene:
	
	ERROR [HttpProcessor[8443][4]] (Cuenta.java:1100) - No fuï¿½ posible 
	copiar la valoraciï¿½n de hospitalizaciï¿½n a urgencias
	java.sql.SQLException: ERROR:  insert or update on table "epicrisis" 
	violates foreign key constraint "fk_epicrisis_ingresos"
	
	*/
	
	
	public Vector toHtml2 (int cuentasImprimir[]) throws SQLException
	{
	    int i;
	    for (i=0;i<cuentasImprimir.length;i++)
	    {
	        if (cuentasImprimir[i]==Integer.parseInt(idCuenta) )
	        {
	            return this.toHtml2();
	        }
	    }
	    return new Vector (5,1); 
	}
	
	/**
	 * Mï¿½todo que imprime la cuenta en html
	 * @return
	 * @throws SQLException
	 */
	public Vector toHtml () throws SQLException
	{
		Vector aImprimir= new Vector(50,10);
		String meses[]={"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

		aImprimir.add("<dt><dd><a href=\"/modificarCuenta/cuenta.jsp?codigoTipoIdentificacionPaciente="+paciente.getTipoIdentificacionPersona()+"&numeroIdentificacionPaciente="+paciente.getNumeroIdentificacionPersona()+"&codigoCuenta="+idCuenta+"\">Modificar</a></dd></dt>");
        aImprimir.add("<dt>Fecha y Hora de creacin de la Cuenta:</dt>");
		aImprimir.add("<dd>" + diaApertura + " " + meses[ Integer.parseInt(mesApertura) - 1] + " " + anioApertura + "</dd>");
		aImprimir.add("<dt>N&uacute;mero de la cuenta</dt>");
		aImprimir.add("<dd>" + idCuenta + "</dd>");
		aImprimir.add("<dt>Convenio</dt>");
		aImprimir.add("<dd>" + Encoder.encode(convenio) + "</dd>");
		aImprimir.add("<dt>Empresa</dt>" +
								"<dd>" + empresa + "</dd>" +
								"<dt>Informaci&oacute;n de la Cuenta:</dt>"); 
		aImprimir.add("<dd>Estrato: "  + estrato + "</font></dd>" );
		if (numeroCarnet !=null&&!numeroCarnet .equals(""))
		{
			aImprimir.add("<dd>N&uacute;mero Carnet - " + numeroCarnet + "</dd>");
		}
		else
		{
			aImprimir.add("<dd>N&uacute;mero Carnet - No necesita, debido a sus condiciones de Ingreso</dd>");
		}

		if (numeroPoliza !=null&&!numeroPoliza .equals(""))
		{
			aImprimir.add("<dd>N&uacute;mero Poliza - " + numeroPoliza + "</dd>");
		}
		else
		{
			aImprimir.add("<dd>N&uacute;mero Poliza - No necesita, debido a sus condiciones de Ingreso</dd>");
		}
		aImprimir.add("<dd>V&iacute;a Ingreso: "  + viaIngreso + "</font></dd>" );
		aImprimir.add("<dd>Tipo Paciente: "  + tipoPaciente + "</font></dd>" );
		aImprimir.add("<dd>Naturaleza Paciente: "  + naturalezaPaciente + "</font></dd>" );
		aImprimir.add("<dd>Estado Cuenta: "  + estadoCuenta + "</font></dd>" );
		aImprimir.add("<dd>Tipo R&eacute;gimen: "  + tipoRegimen + "</font></dd>" );
		aImprimir.add("<dd>Tipo Afiliado: "  + tipoAfiliado + "</font></dd>" );
		aImprimir.add("<dt>Procedimientos efectuados:</dt>");

		aImprimir.add("<dt></dt></dl>");
		
		return aImprimir;
	}

	public Vector toHtml2 () throws SQLException
	{
		Vector aImprimir= new Vector(50,10);
		String meses[]={"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};

		aImprimir.add
		(
			"<tr bgcolor=\"#FFFFFF\">" +
				"<td align=\"right\">" +
					"N&uacute;mero de Cuenta:" +
				"</td>" +
		"<td align=\"left\">"
		);
		
		aImprimir.add(
					idCuenta +
				"</td>" +
			"</tr>"
		);

		aImprimir.add
		(
			"<tr bgcolor=\"#FFFFFF\">" +
				"<td align=\"right\">" +
					"Fecha de Creaci&oacute;n:" +
				"</td>" +
				"<td align=\"left\">" +
					diaApertura + " " + meses[ Integer.parseInt(mesApertura) - 1] + " " + anioApertura +
				"</td>" +
			"</tr>"
		);

		aImprimir.add
		(
			"<tr bgcolor=\"#FFFFFF\">" +
				"<td align=\"right\">" +
					"Convenio:" +
				"</td>" +
				"<td align=\"left\">" +
					Encoder.encode(convenio) +
				"</td>" +
			"</tr>"
		);

		/*
		 * Esta empresa no necesita ser mostrarda, lo miportante es el convenio
		 *
		aImprimir.add
		(
			"<tr bgcolor=\"#FFFFFF\">" +
				"<td align=\"right\">" +
					"Empresa:" +
				"</td>" +
				"<td align=\"left\">" +
					empresa +
				"</td>" +
			"</tr>"
		);
		 */

		aImprimir.add
		(
			"<tr bgcolor=\"#FFFFFF\">" +
				"<td align=\"right\">" +
					"Estrato:" +
				"</td>" +
				"<td align=\"left\">" +
					estrato +
				"</td>" +
			"</tr>"
		);

		aImprimir.add
		(
			"<tr bgcolor=\"#FFFFFF\">" +
				"<td align=\"right\">" +
					"V&iacute;a de Ingreso:" +
				"</td>" +
				"<td align=\"left\">" +
					viaIngreso +
				"</td>" +
			"</tr>"
		);

		String carnet;

		if(numeroCarnet !=null&&!numeroCarnet .equals("") )
			carnet = numeroCarnet;
		else
			carnet = "No necesita, debido a sus condiciones de ingreso";

		aImprimir.add
		(
			"<tr bgcolor=\"#FFFFFF\">" +
				"<td align=\"right\">" +
					"N&uacute;mero de Carnet:" +
				"</td>" +
				"<td align=\"left\">" +
					carnet +
				"</td>" +
			"</tr>"
		);

		String poliza;

		if(numeroPoliza !=null&&!numeroPoliza .equals("") )
			poliza = numeroPoliza;
		else
			poliza = "No necesita, debido a sus condiciones de Ingreso";

		aImprimir.add
		(
			"<tr bgcolor=\"#FFFFFF\">" +
				"<td align=\"right\">" +
					"N&uacute;mero de Poliza:" +
				"</td>" +
				"<td align=\"left\">" +
					poliza +
				"</td>" +
			"</tr>"
		);

		aImprimir.add
		(
			"<tr bgcolor=\"#FFFFFF\">" +
				"<td align=\"right\">" +
					"Tipo de Paciente:" +
				"</td>" +
				"<td align=\"left\">" +
					tipoPaciente +
				"</td>" +
			"</tr>"
		);

		aImprimir.add
		(
			"<tr bgcolor=\"#FFFFFF\">" +
				"<td align=\"right\">" +
					"Naturaleza del Paciente:" +
				"</td>" +
				"<td align=\"left\">" +
					naturalezaPaciente +
				"</td>" +
			"</tr>"
		);

		aImprimir.add
		(
			"<tr bgcolor=\"#FFFFFF\">" +
				"<td align=\"right\">" +
					"Estado de Cuenta:" +
				"</td>" +
				"<td align=\"left\">" +
					estadoCuenta +
				"</td>" +
			"</tr>"
		);

		aImprimir.add
		(
			"<tr bgcolor=\"#FFFFFF\">" +
				"<td align=\"right\">" +
					"Tipo de R&eacute;gimen:" +
				"</td>" +
				"<td align=\"left\">" +
					tipoRegimen +
				"</td>" +
			"</tr>"
		);

		aImprimir.add
		(
			"<tr bgcolor=\"#FFFFFF\">" +
				"<td align=\"right\">" +
					"Tipo de Afiliado:" +
				"</td>" +
				"<td align=\"left\">" +
					tipoAfiliado +
				"</td>" +
			"</tr>"
		);

		if (codigoEstadoCuenta.equals("0"))
		{
			aImprimir.add
			(
				"<tr bgcolor=\"#FFFFFF\">" +
					"<td align=\"center\" colspan=\"2\">" +
						"<a href=\"../cuenta/modificarCuenta.do?estado=empezar\">" +
							"<font face=\"Arial, Helvetica, sans-serif\" size=\"2\" color=\"#006699\">" +
								"Modificar Cuenta" +
							"</font>" +
						"</a>" +
					"</td>" +
				"</tr>"
			);
		}

		return aImprimir;
	}
	
	/**
	 * Para consultar directamente el centro de costo de los medicos tratantes asociados a una cuenta.
	 * Retorna cadena vacia si no encuentra nada u ocurre un error en la consulta a la bd
	 * @param con Conexiï¿½n con la fuente de datos
	 * @param idCuenta Id de la cuenta
	 * @return
	 */
	public static String getCentroCostoTratante(Connection con, int idCuenta, int codigoCentroCostoTratante)
	{
		try
		{
			return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCuentaDao().getCentroCostoTratante(con, idCuenta, codigoCentroCostoTratante);
		}
		catch (SQLException sql)
		{
			logger.warn("Error consultando el centro de costo tratante asociado a la cuenta "+idCuenta+".\n"+sql);
			return "";	
		}
	}
	
	/**
	 * Para consultar directamente el centro de costo de los medicos tratantes asociados a una cuenta.
	 * Retorna cadena vacia si no encuentra nada u ocurre un error en la consulta a la bd
	 * @param con Conexiï¿½n con la fuente de datos
	 * @param idCuenta Id de la cuenta
	 * @return
	 */
	public static String getCentroCostoTratante(Connection con, int idCuenta, String codigoInstitucion) throws SQLException
	{
	    return UtilidadValidacion.getNombreCentroCosto(con, UtilidadValidacion.getCodigoCentroCostoTratanteMetodoLento(con, idCuenta, Integer.parseInt(codigoInstitucion)));
	}
	
    /**
     * Para consultar directamente el codigo del centro de costo de los medicos tratantes asociados a una cuenta.
     * Retorna valor negativo si no encuentra nada u ocurre un error en la consulta a la bd
     * @param con Conexiï¿½n con la fuente de datos
     * @param idCuenta Id de la cuenta
     * @return
     */
    public static int getCodigoCentroCostoTratante(Connection con, int idCuenta, String codigoInstitucion) throws SQLException
    {
        return UtilidadValidacion.getCodigoCentroCostoTratanteMetodoLento(con, idCuenta, Integer.parseInt(codigoInstitucion));
    }
    
	/**
	 * Para consultar directamente los datos del tipo del ingreso. Arreglo que: 
	 * Primero posiciï¿½n -> El codigo de la via de ingreso asociado a la cuenta dada por parametro.
	 * Segunda posiciï¿½n ->Si la via de ingreso es urgencias u hospitalizacion, retorna el codigo de la admisiï¿½n en caso contrario -1.
	 * Retorna -1 en ambas posiciones si no encuentra nada, null si ocurrio una SQLException
	 * @param con
	 * @param idCuenta
	 * @return
	 */	
	public static int[] getTipoIngreso(Connection con, int idCuenta)
	{
		int CODIGO_HOSPITALIZACION = 1;
		int CODIGO_URGENCIAS = 3;		
		ResultSetDecorator admision;
		
		try
		{	
			//Busco en hospitalizacion	
			admision = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAdmisionHospitalariaDao().getBasicoAdmision(con, idCuenta);			
			if (admision != null && admision.next()) 
				return new int[]{CODIGO_HOSPITALIZACION, admision.getInt("idAdmision")}; 
			//Si no se encontro, busco en urgencias			
			admision = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getAdmisionUrgenciasDao().getBasicoAdmision(con, idCuenta);
			if (admision != null && admision.next()) 
				return new int[]{CODIGO_URGENCIAS, admision.getInt("idAdmision")};
			//Caso Consulta Externa y Ambulatorios pendiente por definir
			return new int[]{-1,-1};		
		}
		catch (SQLException sql)
		{
			logger.warn("Error consultando el centro de costo tratante asociado a la cuenta "+idCuenta+".\n"+sql);
			return null;	
		}		
	}
	
		/**
	 * Cambia el estado de la cuenta transaccionalmente
	 * @param con
	 * @param codigoEstadoCuenta
	 * @param idCuenta
	 * @param estado
	 * @return
	 * @throws SQLException
	 */
	public int cambiarEstadoCuentaTransaccional (Connection con, int codigoEstadoCuenta, int idCuenta, String estado ) throws SQLException
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
	        numElementosInsertados=cuentaDao.cambiarEstadoCuenta(con, codigoEstadoCuenta, idCuenta);
	        
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
	 * Cambia el estado de la cuenta transaccionalmente
	 * @param con
	 * @param codigoEstadoCuenta
	 * @param idCuenta
	 * @param estado
	 * @return
	 * @throws SQLException
	 */
	public int cambiarEstadoCuentaTransaccional2 (Connection con, int codigoEstadoCuenta, int idCuenta, String estado ) throws SQLException
	{
	    int numElementosInsertados=0;
	    DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
	    if (estado.equals(ConstantesBD.inicioTransaccion))
	        myFactory.beginTransaction(con);
	        
	    numElementosInsertados=cuentaDao.cambiarEstadoCuenta(con, codigoEstadoCuenta, idCuenta);
	    
	    if (estado.equals(ConstantesBD.finTransaccion))
	        myFactory.endTransaction(con);
	    return numElementosInsertados;
	}
	
	
	/**
	 * Metodo para cambiar el estado de una cuenta
	 * @param id
	 * @param estado
	 * @return
	 */
	public int cambiarEstadoCuentaNoTransaccional(Connection con,int id,int estado)
	{
		return cuentaDao.cambiarEstadoCuenta(con,estado,id);
	}
	
	
	/**
	 * Metodo que retorna el valor total de una cuenta.
	 * @param con, Conexion
	 * @param idCuenta, id Cuenta.
	 * @return
	 */
	public float getValorTotalCuenta(Connection con,int idCuenta)
	{
		return cuentaDao.getValorTotalCuenta(con,idCuenta);
	}
	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo usado para cargar la ï¿½ltima cuenta Urgencias Asociada del paciente
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public String cargarCuentaUrgenciasAsociada(Connection con,int codigoIngreso)
	{
		
		return cuentaDao.cargarCuentaUrgenciasAsociada(con,codigoIngreso);
		
		
	}
	
	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo para registrar el LOG tipo base de datos del cierre de la cuenta
	 * en el mï¿½dulo de facturaciï¿½n
	 * @param con
	 * @param idCuenta
	 * @param usuario
	 * @param motivo
	 * @param estado
	 * @return
	 */
	public int registrarCierreCuenta(Connection con,int idCuenta, String usuario, String motivo, String estado){
		return cuentaDao.registrarCierreCuenta(con,idCuenta,usuario,motivo,estado);
	}
	
	/**
	 * Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo para consultar las cuentas cerradas del paciente
	 * @param con
	 * @param codigoPaciente
	 * @return listado de cuentas cerradas
	 */
	public HashMap consultarCuentasCerradasPaciente(Connection con,int codigoPaciente){
		HashMap listadoCuentas=new HashMap();
		try{
		
			ResultSetDecorator rs=cuentaDao.consultarCuentasCerradasPaciente(con,codigoPaciente);
			
			int cont=0;
			while(rs.next())
			{
				listadoCuentas.put("id_"+cont,rs.getString("id"));
				listadoCuentas.put("codigoViaIngreso_"+cont,rs.getString("codigo_via_ingreso"));
				listadoCuentas.put("viaIngreso_"+cont,rs.getString("via_ingreso"));
				listadoCuentas.put("codigoConvenio_"+cont,rs.getString("codigo_convenio"));
				listadoCuentas.put("convenio_"+cont,rs.getString("convenio"));
				listadoCuentas.put("fechaApertura_"+cont,rs.getString("fecha_apertura"));
				listadoCuentas.put("fechaCierre_"+cont,rs.getString("fecha_cierre"));
				listadoCuentas.put("horaCierre_"+cont,rs.getString("hora_cierre"));
				listadoCuentas.put("usuarioCierre_"+cont,rs.getString("usuario_cierre"));
				listadoCuentas.put("motivo_"+cont,rs.getString("motivo"));
				listadoCuentas.put("centro_atencion_"+cont,rs.getString("centro_atencion"));
				listadoCuentas.put("descripcionentidadsub_"+cont,rs.getString("descripcionentidadsub"));
				
				cont++;
			}
			
			listadoCuentas.put("numero_elementos",cont+"");
			return listadoCuentas;
		}
		catch(SQLException e)
		{
			logger.error("Error consultando las cuentas cerradas del paciente en Cuenta: "+e);
			return listadoCuentas;
		}
	}
	
	/**
	 *  Adiciï¿½n Sebastiï¿½n
	 * Mï¿½todo para consultar las cuentas cerradas por rangos de fechas y/o id's de cuentas
	 * @param con
	 * @param fechaCierreInicial
	 * @param fechaCierreFinal
	 * @param idCuentaInicial
	 * @param idCuentaFinal
	 * @param centroAtencio
	 * @param String codigoEntidadSub 
	 * @return listado de cuentas cerradas (cuenta,paciente(nombre,id,numero),via_ingreso,responsable,fecha_apertura,fecha_hora_cierre,usuario,motivo)
	 */
	public HashMap consultarCuentasCerradas(Connection con,String fechaCierreInicial,String fechaCierreFinal,int idCuentaInicial,int idCuentaFinal, int centroAtencion, String codigoEntidadSub)
	{
		HashMap listadoCuentas= new HashMap();
		try
		{
			ResultSetDecorator rs=cuentaDao.consultarCuentasCerradas(con,fechaCierreInicial,fechaCierreFinal,idCuentaInicial,idCuentaFinal, centroAtencion,codigoEntidadSub);
			
			int cont=0;
			
			while(rs.next())
			{
				listadoCuentas.put("id_"+cont,rs.getString("id"));
				listadoCuentas.put("nombrePaciente_"+cont,rs.getString("nombre_paciente"));
				listadoCuentas.put("tipoIdentificacion_"+cont,rs.getString("tipo_identificacion"));
				listadoCuentas.put("numeroIdentificacion_"+cont,rs.getString("numero_identificacion"));
				listadoCuentas.put("codigoViaIngreso_"+cont,rs.getString("codigo_via_ingreso"));
				listadoCuentas.put("viaIngreso_"+cont,rs.getString("via_ingreso"));
				listadoCuentas.put("codigoConvenio_"+cont,rs.getString("codigo_convenio"));
				listadoCuentas.put("convenio_"+cont,rs.getString("convenio"));
				listadoCuentas.put("fechaApertura_"+cont,rs.getString("fecha_apertura"));
				listadoCuentas.put("fechaCierre_"+cont,rs.getString("fecha_cierre"));
				listadoCuentas.put("horaCierre_"+cont,rs.getString("hora_cierre"));
				listadoCuentas.put("usuarioCierre_"+cont,rs.getString("usuario_cierre"));
				listadoCuentas.put("motivo_"+cont,rs.getString("motivo"));
				listadoCuentas.put("centro_atencion_"+cont,rs.getString("centro_atencion"));
				listadoCuentas.put("descripcionentidadsub_"+cont,rs.getString("descripcionentidadsub"));			
				
				cont++;
			}
			
			listadoCuentas.put("numero_elementos",cont+"");
			return listadoCuentas;
		}
		catch(SQLException e)
		{
			logger.error("Error consultando las cuentas cerradas por rangos en Cuenta: "+e);
			return listadoCuentas;
		}
	}
	
	/**
	 * Mï¿½todo implementado para consultar las cuentas
	 * @param con
	 * @return
	 */
	public HashMap consultarCuentas(Connection con)
	{
		return cuentaDao.consultarCuentas(
			con,this.fechaInicial,this.fechaFinal,
			this.cuentaInicial,this.cuentaFinal,
			Integer.parseInt(this.codigoEstadoCuenta),this.usuario,
			Integer.parseInt(this.codigoViaIngreso),
			this.tipoPaciente,
			Integer.parseInt(this.codigoConvenio), 
			this.centroAtencion,
			this.codigoTipoEvento,
			this.codigoEntidadSub);
	}
	
	/**
	 * Mï¿½todo implementado para consultar las cuentas
	 * de un paciente especï¿½fico
	 * @param con
	 * @return
	 */
	public HashMap consultarCuentas(Connection con,int codigoPaciente)
	{
		return cuentaDao.consultarCuentas(con,codigoPaciente);
	}
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static int obtenerTipoComplejidad (Connection con, String idCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCuentaDao().obtenerTipoComplejidad(con, idCuenta);
	}
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static int obtenerCentroAtencionCuenta (Connection con, String idCuenta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCuentaDao().obtenerCentroAtencionCuenta(con, idCuenta);
	}
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static int obtenerCodigoViaIngresoCuenta(Connection con, String idCuenta) throws IPSException
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCuentaDao().obtenerCodigoViaIngresoCuenta(con, idCuenta);
	}
	
	 /**
	  * Metodo encargado de trasladarle una cuenta a un ingreso
	  * @author Jhony Alexander Duque A.
	  * @param connection
	  * @param datos
	  * -----------------------------
	  * KEY'S DEL MAPA DATOS
	  * -----------------------------
	  * -- ingresoTraslado (Requerido)--> ingreso a donde se va a trasladar la cuenta
	  * -- estadoCuenta (Requerido) --> estado en el cual queda la cuenta 
	  * -- ingresoCuenta (Requerido) -->  ingreso donde se encuentra la cuenta a trasladar
	  * 
	  * @return
	 * @throws IPSException 
	  */
	 public static boolean trasladarCuentaAingreso (Connection connection,String ingresoTraslado,String estadoCuenta,String ingresoCuenta) throws IPSException
	 {
		 logger.info("\n entre trasladarCuentaAingreso ingreso cuenta ["+ingresoTraslado+"]");
		 HashMap datos = new HashMap();
		 datos.put("ingresoTraslado", ingresoTraslado);
		 datos.put("estadoCuenta", estadoCuenta);
		 datos.put("ingresoCuenta", ingresoCuenta);
		 
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCuentaDao().trasladarCuentaAingreso(connection, datos);
	 }
	
	
	 /**
	  * Metodo encargado de realizar un asocio de cuentas 
	  * ingresando la cuenta inicial y final 
	  * @author Jhony Alexander Duque A.
	  * @param connection
	  * @param datos
	  * ------------------------------
	  * KEY'S DEL MAPA DATOS
	  * ------------------------------
	  * -- usuario (Requerido)  
	  * -- cuentaInicial (Requerido)  
	  * -- cuentaFinal (Requerido)  
	  * -- activo (Requerido) 
	  * -- ingreso (Requerido) 
	  * 
	  * @return
	  */
	 public static boolean asociosCuentaTotal (Connection connection,String usuario,String cuentaInicial,String cuentaFinal,String activo, String ingreso)
	 {
		 HashMap datos = new HashMap ();
		 datos.put("usuario", usuario);
		 datos.put("cuentaInicial", cuentaInicial);
		 datos.put("cuentaFinal", cuentaFinal);
		 datos.put("activo", activo);
	     datos.put("ingreso", ingreso);
	
		 
		 return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCuentaDao().asociosCuentaTotal(connection, datos);
	 }
	 
	 
	/**
	 * @return Returns the horaApertura.
	 */
	public String getHoraApertura() {
		return horaApertura;
	}
	
	/**
	 * @param horaApertura The horaApertura to set.
	 */
	public void setHoraApertura(String horaApertura) {
		this.horaApertura = horaApertura;
	}

	/**
	 * @return Retorna el codigo de la especialidadRefiere.
	 */
	public int getCodigoEspecialidadRefiere() {
		return especialidadRefiere.getCodigo();
	}

	/**
	 * @param Asigna el codigoEspecialidadRefiere .
	 */
	public void setCodigoEspecialidadRefiere(int codigo) {
		this.especialidadRefiere.setCodigo(codigo);
	}
	
	/**
	 * @return Retorna el nombre de la especialidadRefiere.
	 */
	public String getNombreEspecialidadRefiere() {
		return especialidadRefiere.getNombre();
	}

	/**
	 * @param Asigna el nombreEspecialidadRefiere .
	 */
	public void setNombreEspecialidadRefiere(String nombre) {
		this.especialidadRefiere.setNombre(nombre);
	}

	/**
	 * @return Returns the institucionRefiere.
	 */
	public String getInstitucionRefiere() {
		return institucionRefiere;
	}

	/**
	 * @param institucionRefiere The institucionRefiere to set.
	 */
	public void setInstitucionRefiere(String institucionRefiere) {
		this.institucionRefiere = institucionRefiere;
	}

	/**
	 * @return Returns the pacienteReferido.
	 */
	public boolean isPacienteReferido() {
		return pacienteReferido;
	}

	/**
	 * @param pacienteReferido The pacienteReferido to set.
	 */
	public void setPacienteReferido(boolean pacienteReferido) {
		this.pacienteReferido = pacienteReferido;
	}

	/**
	 * @return Returns the profesionalRefiere.
	 */
	public String getProfesionalRefiere() {
		return profesionalRefiere;
	}

	/**
	 * @param profesionalRefiere The profesionalRefiere to set.
	 */
	public void setProfesionalRefiere(String profesionalRefiere) {
		this.profesionalRefiere = profesionalRefiere;
	}

	/**
	 * @return Returns the cuentaFinal.
	 */
	public int getCuentaFinal() {
		return cuentaFinal;
	}

	/**
	 * @param cuentaFinal The cuentaFinal to set.
	 */
	public void setCuentaFinal(int cuentaFinal) {
		this.cuentaFinal = cuentaFinal;
	}

	/**
	 * @return Returns the cuentaInicial.
	 */
	public int getCuentaInicial() {
		return cuentaInicial;
	}

	/**
	 * @param cuentaInicial The cuentaInicial to set.
	 */
	public void setCuentaInicial(int cuentaInicial) {
		this.cuentaInicial = cuentaInicial;
	}

	/**
	 * @return Returns the fechaFinal.
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * @param fechaFinal The fechaFinal to set.
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * @return Returns the fechaInicial.
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}

	/**
	 * @param fechaInicial The fechaInicial to set.
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	/**
	 * @return Returns the usuario.
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario The usuario to set.
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
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

	/**
	 * @return Returns the nombreArea.
	 */
	public String getNombreArea() {
		return nombreArea;
	}

	/**
	 * @param nombreArea The nombreArea to set.
	 */
	public void setNombreArea(String nombreArea) {
		this.nombreArea = nombreArea;
	}



	/**
	 * @return Returns the codigoContrato.
	 */
	public int getCodigoContrato() {
		return codigoContrato;
	}

	/**
	 * @param codigoContrato The codigoContrato to set.
	 */
	public void setCodigoContrato(int codigoContrato) {
		this.codigoContrato = codigoContrato;
	}

	public int getCentroAtencion() {
		return centroAtencion;
	}

	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public int getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}

	public void setCodigoCentroAtencion(int codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}

	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	/**
	 * @return the codigoArpAfiliado
	 */
	public String getCodigoArpAfiliado() {
		return codigoArpAfiliado;
	}

	/**
	 * @param codigoArpAfiliado the codigoArpAfiliado to set
	 */
	public void setCodigoArpAfiliado(String codigoArpAfiliado) {
		this.codigoArpAfiliado = codigoArpAfiliado;
	}

	/**
	 * @return the codigoTipoEvento
	 */
	public String getCodigoTipoEvento() {
		return codigoTipoEvento;
	}

	/**
	 * @param codigoTipoEvento the codigoTipoEvento to set
	 */
	public void setCodigoTipoEvento(String codigoTipoEvento) {
		this.codigoTipoEvento = codigoTipoEvento;
	}

	/**
	 * @return the desplazado
	 */
	public boolean isDesplazado() {
		return desplazado;
	}

	/**
	 * @param desplazado the desplazado to set
	 */
	public void setDesplazado(boolean desplazado) {
		this.desplazado = desplazado;
	}

	/**
	 * @return the nombreArpAfiliado
	 */
	public String getNombreArpAfiliado() {
		return nombreArpAfiliado;
	}

	/**
	 * @param nombreArpAfiliado the nombreArpAfiliado to set
	 */
	public void setNombreArpAfiliado(String nombreArpAfiliado) {
		this.nombreArpAfiliado = nombreArpAfiliado;
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
	 * @return the codigoOrigenAdmision
	 */
	public int getCodigoOrigenAdmision() {
		return codigoOrigenAdmision;
	}

	/**
	 * @param codigoOrigenAdmision the codigoOrigenAdmision to set
	 */
	public void setCodigoOrigenAdmision(int codigoOrigenAdmision) {
		this.codigoOrigenAdmision = codigoOrigenAdmision;
	}

	/**
	 * @return the nombreOrigenAdmision
	 */
	public String getNombreOrigenAdmision() {
		return nombreOrigenAdmision;
	}

	/**
	 * @param nombreOrigenAdmision the nombreOrigenAdmision to set
	 */
	public void setNombreOrigenAdmision(String nombreOrigenAdmision) {
		this.nombreOrigenAdmision = nombreOrigenAdmision;
	}
	
	
	/**
	 * Método implementado para guardar la información de la cuenta
	 * @param con
	 * @return
	 */
	public ResultadoBoolean guardar(Connection con)
	{
		return cuentaDao.guardar(con, cuenta);
	}
	
	/**
	 * Método que carga la informacion de la cuenta con sus convenios
	 * @param con
	 * @param idCuenta
	 * @return
	 * Nota * Si no se encuentra la cuenta el atributo idCuenta es vacío
	 */
	public boolean cargar(Connection con,String idCuenta)
	{
		this.cuenta = cuentaDao.cargar(con, idCuenta);
		boolean existe = false;
		if(!cuenta.getIdCuenta().equals(""))
			existe = true;
		return existe;
	}
	
	/**
	 * Método que consulta los datos generales de las cuentas de un asocio
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> listarCuentasAsocio(Connection con,String idIngreso)
	{
		return utilidadDao().listarCuentasAsocio(con, idIngreso);
	}
	
	/**
	 * Método que consulta el tipo de evento de la cuenta
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public static String obtenerCodigoTipoEventoCuenta(Connection con,String idCuenta)
	{
		return utilidadDao().obtenerCodigoTipoEventoCuenta(con, idCuenta);
	}
	
	/**
	 * Método que actualiza el tipo evento de la cuenta
	 * @param con
	 * @param tipoEvento
	 * @param idCuenta
	 * @return
	 */
	public static int actualizarTipoEventoCuenta(Connection con,String tipoEvento,String idCuenta)
	{
		HashMap campos = new HashMap();
		campos.put("tipoEvento",tipoEvento);
		campos.put("idCuenta",idCuenta);
		return utilidadDao().actualizarTipoEventoCuenta(con, campos);
	}
	
	/**
	 * Método que verifica si se puede modificar el área de una cuenta
	 * @param con
	 * @param campos
	 * @return
	 */
	public static boolean puedoModificarAreaCuenta(Connection con,String idCuenta,int codigoViaIngreso,boolean existeAsocio)
	{
		HashMap campos = new HashMap();
		campos.put("idCuenta",idCuenta);
		campos.put("codigoViaIngreso",codigoViaIngreso);
		campos.put("existeAsocio",existeAsocio);
		return utilidadDao().puedoModificarAreaCuenta(con, campos);
	}
	
	/**
	 * Método para actualizar el convenio arp afiliado de la cuenta
	 * @param con
	 * @param idCuenta
	 * @param convenioArpAfiliado
	 * @return
	 */
	public static int actualizarConvenioArpAfiliadoCuenta(Connection con,String idCuenta,String convenioArpAfiliado)
	{
		HashMap campos = new HashMap();
		campos.put("idCuenta", idCuenta);
		campos.put("codigoConvenioArpAfiliado", convenioArpAfiliado);
		return utilidadDao().actualizarConvenioArpAfiliadoCuenta(con, campos);
	}
	
	/**
	 * Actualizar el número de la póliza en el registro de accidentes de tránsito
	 * @param con Conexión con la BD
	 * @param idIngreso código del ingreso
	 * @param convenio código del convenio
	 * @param nroPoliza Número de póliza a actualizar
	 * @return Número de elementos actualizados
	 */
	public int actualizarNroPolizaConvenioAccidenteTransito(Connection con, int idIngreso, int convenio, String nroPoliza)
	{
		return utilidadDao().actualizarNroPolizaConvenioAccidenteTransito(con, idIngreso, convenio, nroPoliza);
	}

	
	/**
	 * Método que actualiza el convenio en los cargos, cuando se realiza modificacion del convenio
	 * en la modificacion de cuentas
	 * @param con
	 * @param campos
	 * @return
	 */
	public int actualizarConvenioEnCargos(Connection con,int codigoConvenio,String idSubCuenta)
	{
		HashMap campos = new HashMap();
		campos.put("codigoConvenio", codigoConvenio+"");
		campos.put("idSubCuenta", idSubCuenta);
		return cuentaDao.actualizarConvenioEnCargos(con, campos);
	}
	
	/**
	 * Consultar area(centro de costos) y via ingreso de una cuenta
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return [idCentroCosto,nombreCentroCosto,idViaIngreso,nombreViaIngreso]
	 * @throws Exception 
	 * @throws SQLException 
	 */
	public String[] consultarAreaYViaIngreso(Connection con,int codigoCuenta) throws SQLException, Exception
	{
		
		return cuentaDao.consultarAreaYViaIngreso(con, codigoCuenta);
	}
	
	/**
	 * 
	 * @param con
	 * @param codCuenta
	 * @return
	 */
	public static boolean esCuentaFinalAsocio(Connection con, int codCuenta)
	{
		return utilidadDao().esCuentaFinalAsocio(con, codCuenta);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroIngreso
	 * @param esAsocio
	 * @return
	 */
	public static HashMap<Object, Object> obtenerViasIngresoTipoPacDadoIngreso(Connection con, int numeroIngreso, String esAsocio)
	{
		return utilidadDao().obtenerViasIngresoTipoPacDadoIngreso(con, numeroIngreso, esAsocio);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroIngreso
	 * @param viasIngreso
	 * @return
	 */
	public static Vector<String> obtenerCuentasIngreso(Connection con, int numeroIngreso, boolean epicrisis)
	{
		return utilidadDao().obtenerCuentasIngreso(con, numeroIngreso, epicrisis);
	}
	
	
	
	
	/**
	 * Método que retorna el DAO de Cuenta
	 * @return
	 */
	public static CuentaDao utilidadDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCuentaDao(); 
	}

	/**
	 * @return the cuenta
	 */
	public DtoCuentas getCuenta() {
		return cuenta;
	}

	/**
	 * @param cuenta the cuenta to set
	 */
	public void setCuenta(DtoCuentas cuenta) {
		this.cuenta = cuenta;
	}

	/**
	 * @return the codigoEntidadSub
	 */
	public String getCodigoEntidadSub() {
		return codigoEntidadSub;
	}

	/**
	 * @param codigoEntidadSub the codigoEntidadSub to set
	 */
	public void setCodigoEntidadSub(String codigoEntidadSub) {
		this.codigoEntidadSub = codigoEntidadSub;
	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	public static String obtenerFechaVigenciaTopeCuenta(String idCuenta) 
	{
		String fecha=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCuentaDao().obtenerFechaVigenciaTopeCuenta(idCuenta);
		logger.info("*******************************************************************************************************");
		logger.info("*******************************************************************************************************");
		logger.info("FECHA VIGENCIA->"+fecha);
		logger.info("*******************************************************************************************************");
		logger.info("*******************************************************************************************************");
		return fecha;
	}

	
	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 * @throws SQLException
	 */
	public String obtenerIngresoXNumeroFactura(Connection con,String numeroFactura) throws SQLException
	{
		return cuentaDao.obtenerIngresoXNumeroFactura(con, numeroFactura);
	}
	
	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 * @throws SQLException
	 */
	public    DtoInstitucion consultarDatosInstitucionXFactura(Connection con,String numeroFactura) throws SQLException
	{
		return cuentaDao.consultarDatosInstitucionXFactura(con, numeroFactura);
	}
	

	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 * @throws SQLException
	 */
	public  String obtenerDatosAnulacionFactura(Connection con,String numeroFactura) throws SQLException
	{
		return cuentaDao.obtenerDatosAnulacionFactura(con, numeroFactura);
	}
	
	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 * @throws SQLException
	 */
	public  String obtenerCentroAtencionFactura(Connection con,String numeroFactura) throws SQLException
	{
		return cuentaDao.obtenerCentroAtencionFactura(con, numeroFactura);
	}
	
	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 * @throws SQLException
	 */
	public  String obtenerNumeroFacturaAsociada(Connection con,String numeroFactura) throws SQLException
	{
		return cuentaDao.obtenerNumeroFacturaAsociada(con, numeroFactura);
	}
	
	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 * @throws SQLException
	 */
	public  DtoDatosResponsableFacturacion obtenerResponsable(Connection con,String numeroFactura) throws SQLException
	{
		return cuentaDao.obtenerResponsable(con, numeroFactura);
	}
	
	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 * @throws SQLException
	 */
	public  String obtenerFechaGeneracionFactura(Connection con,String numeroFactura) throws SQLException
	{
		return cuentaDao.obtenerFechaGeneracionFactura(con, numeroFactura);
	}
	
	
	
	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 * @throws SQLException
	 */
	public  List<DtoFacturaAgrupada> articulosFacturaAgrupada(Connection con,String numeroFactura)throws SQLException
	{
		return cuentaDao.articulosFacturaAgrupada(con, numeroFactura);
	}

	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 * @throws SQLException
	 */
	public  List<DtoFacturaAgrupada> serviciosFacturaAgrupada(Connection con,String numeroFactura)throws SQLException
	{
		return cuentaDao.serviciosFacturaAgrupada(con, numeroFactura);
	}


	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 * @throws SQLException
	 */
	public  List<DtoFacturaAgrupada> cirugiasFacturaAgrupada(Connection con,String numeroFactura,Integer codigoInstitucion)throws SQLException
	{
		return cuentaDao.cirugiasFacturaAgrupada(con, numeroFactura, codigoInstitucion);
	}
	

	
	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 * @throws SQLException
	 */
	public  List<DtoFacturaAgrupada> paquetesFacturaAgrupada(Connection con,String numeroFactura)throws SQLException
	{
		return cuentaDao.paquetesFacturaAgrupada(con, numeroFactura);
	}
	
	
	/**
	 * @param con
	 * @param codigoConvenio
	 * @return
	 * @throws SQLException
	 */
	public  boolean formatoValoresFormatoFacturaAgrupada(Connection con,String codigoConvenio)throws SQLException
	{
		return cuentaDao.formatoValoresFormatoFacturaAgrupada(con, codigoConvenio);
	}
	
	/**
	 * @see com.princetonsa.dao.CuentaDao#consultarNumeroAutorizacion(java.sql.Connection, java.lang.Integer, java.lang.Integer)
	 */
	public  Integer consultarNumeroAutorizacion(Connection con,Integer convenioId, Integer ingresoConsecutivo) throws SQLException
	{
		return cuentaDao.consultarNumeroAutorizacion(con, convenioId, ingresoConsecutivo);
	}
	
	
	/**
	 * @see com.princetonsa.dao.CuentaDao#obtenerDatosCentroCostoXSolcitud(Connection, int)
	 */	
	public InfoDatosInt obtenerDatosCentroCostoXSolcitud(Connection con, int idSolicitud) throws IPSException{
		return cuentaDao.obtenerDatosCentroCostoXSolcitud(con, idSolicitud);		
	}

}