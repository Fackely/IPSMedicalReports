/*
 * @(#)ValidacionesSolicitud.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_03
 *
 */

package com.princetonsa.mundo.solicitudes;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.ValidacionesSolicitudDao;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadValidacion;
import util.ordenesMedicas.UtilidadesOrdenesMedicas;

/**
 * Clase encargada de las validaciones para las Solicitudes
 * 
 *	@version 1.0, Feb 14, 2004
 */

public class ValidacionesSolicitud
{
	
	
    
    /**
	 * Número de la solicitud que se piensa validar
	 */
	private int numeroSolicitud;
	
	/**
	 * Médico sobre el que se quieren revisar permisos
	 */
	private UsuarioBasico medico;
	
	/**
	 * Paciente al que se le creo esta solicitud
	 */
	private PersonaBasica paciente;

	/**
	 * Código del centro de costo al que
	 * se hizó esta solicitud
	 */
	private int codigoCentroCostoSolicitado;
	
	/**
	 * Código del centro de costo del médico que
	 * se hizó esta solicitud
	 */
	private int codigoCentroCostoQueSolicita;
	
	/**
	 * Código de la ocupación al que
	 * se hizo esta solicitud
	 */
	private int codigoOcupacionSolicitada;
	
	/**
	 * Código de la especialidad a la 
	 * que se hizo esta solicitud
	 */
	private int codigoEspecialidadSolicitada;

	/**
	 * Entero para almacenar el código del 
	 * centro de costo tratante de la cuenta 
	 * de esta solicitud
	 */
	private int codigoCentroCostoTratante;
	
	/**
	 * Código del centro de costo del paciente cargado en session
	 */
	private int codigoCentroCostoPaciente;
	
	/**
	 * Para hacer los logs de la aplicación
	 */
	protected Logger logger = Logger.getLogger(ValidacionesSolicitud.class);

	/**
	 * Boolean que indica si esta solicitud existe
	 */	
	private boolean existe;
	
	/**
	 * Entero con el código manejado por el tipo
	 * de esta solicitud
	 */
	private int codigoTipoSolicitud; 
	
	/**
	 * Boolean que indica si este médico fué
	 * el que creo la solicitud
	 */
	private boolean esCreador;
	
	/**
	 * Objeto ResultadoBoolean que me dice si se
	 * puede responder la solicitud específicada y
	 * en caso "no" porque
	 */
	private ResultadoBoolean puedoResponder;
	
	/**
	 * Objeto ResultadoBoolean que me dice si se
	 * puede interpretar la solicitud específicada y
	 * en caso "no", la razón 
	 */
	private ResultadoBoolean puedoInterpretar;
	
	/**
	 * Objeto ResultadoBoolean que me dice si se
	 * puede modificar la solicitud específicada y
	 * en caso "no", la razón . Caso Solicitud Respondida
	 */
	private ResultadoBoolean puedoModificarSolicitudRespondida;

	/**
	 * Objeto ResultadoBoolean que me dice si se
	 * puede modificar la solicitud específicada y
	 * en caso "no", la razón . Caso Solicitud Solicitada
	 */
	private ResultadoBoolean puedoModificarSolicitudSolicitada;
	
	/**
	 * Objeto ResultadoBoolean que me dice si se
	 * puede modificar la solicitud específicada y
	 * en caso "no", la razón . Caso Solicitud Respondida
	 */
	private ResultadoBoolean puedoModificarSolicitudInterpretada;

	/**
	 * Objeto ResultadoBoolean que me dice si el
	 * médico es el tratante para la cuenta de la
	 * solicitud
	 */
	private ResultadoBoolean esTratante;
	
	/**
	 * Objeto ResultadoBoolean que me dice si el
	 * médico es adjunto para la cuenta de la 
	 * solicitud
	 */
	private ResultadoBoolean esAdjunto;
	
	/**
	 * Objeto ResultadoBoolean que me dice si 
	 * se pidio por solo concepto (Aplica solo
	 * para interconsultas)
	 */
	private ResultadoBoolean esSoloConcepto;
	
	/**
	 * Objeto ResultadoBoolean que me dice si 
	 * se pidio para volver tratante (Aplica solo
	 * para interconsultas)
	 */
	private ResultadoBoolean tieneSolicitudVolverTratante;
	
	/**
	 * Objeto ResultadoBoolean que me dice si 
	 * se pidio para volver tratante (Aplica solo
	 * para interconsultas)
	 */
	private ResultadoBoolean tieneSolicitudAdjunto;
	
	
	/**
	 * Boolean que dice si esta solicitud fué llenada
	 * con un elemento parametrizado o digitado por
	 * el usuario
	 */
	private boolean esOtros;
	
	
	
	/**
	 * Boolean que dice si la solicitud está anulada
	 */
	private boolean estaAnulada;
	
	/**
	 * Entero con el código del médico creador de la 
	 * solicitud
	 */
	private int codigoMedicoCreador;
	
	/**
	 * Boolean que me dice si la cuenta de la solicitud 
	 * está abierta
	 */
	private boolean estaCuentaSolicitudAbierta;
	
	/**
	 * Boolean que me asegura que la cuenta de la solicitud
	 * tiene ordenSalida
	 */
	private boolean cuentaSolicitudSinOrdenSalida;
	
	/**
	 * Código del médico que realizó la intrepretación
	 */
	private int codigoMedicoInterpretacion;
	
	/**
	 * Boolean que me indica si puedo anular una solicitud de medicamentos
	 */
	private ResultadoBoolean puedoAnularModificarSolicitudMedicamentos;
	
	/**
	 * Boolean que me indica si puedo suspender medicamentos de la solicitud
	 */
	private ResultadoBoolean puedoSuspenderSolicitudMedicamentos;
	
	/**
	 * Boolean que me indica si la solicitud tiene despachos.
	 */
	private boolean tieneDespachos;
	/**
	 * Estado de solicitud solicitada.
	 */
	private boolean estadoSolicitada;
	/**
	 * Estado de facturacio pendiente.
	 */
	private boolean estadoFacturacionPendiente;
	/**
	 * El DAO usado por el objeto <code>ValidacionesSolicitud</code> 
	 * para acceder a la fuente de datos.
	 */
	private static ValidacionesSolicitudDao validacionesSolicitudDao;
	
	
	/**
	 * Método que limpia el objeto <code>ValidacionesSolicitud</code>
	 */
	private void clean ()
	{
		existe=false;
		numeroSolicitud=0;
		codigoTipoSolicitud=0;
		esCreador=false;
		esOtros=false;
		
		this.codigoCentroCostoSolicitado=0;
		this.codigoCentroCostoQueSolicita=0;
		this.codigoOcupacionSolicitada=0;
		
		codigoCentroCostoTratante=0;
		codigoMedicoCreador=0;
		
		//Por defecto el código de la especialidad solicitada
		//lo ponemos en otros y si alguna solicitud en particular
		//lo cambia, se debe manejar como interconsultas
		this.codigoEspecialidadSolicitada=ConstantesBD.codigoOtros;
		
		puedoResponder=new ResultadoBoolean (false);
		puedoResponder.setDescripcion("error.validacionessolicitud.noExisteSolicitud");
		puedoInterpretar=new ResultadoBoolean (false);
		puedoInterpretar.setDescripcion("error.validacionessolicitud.noExisteSolicitud");
		esTratante=new ResultadoBoolean (false);
		esTratante.setDescripcion("error.validacionessolicitud.noExisteSolicitud");
		esAdjunto=new ResultadoBoolean (false);
		esAdjunto.setDescripcion("error.validacionessolicitud.noExisteSolicitud");
		esSoloConcepto=new ResultadoBoolean (false);
		esSoloConcepto.setDescripcion("error.validacionessolicitud.noExisteSolicitud");
		
		puedoModificarSolicitudRespondida=new ResultadoBoolean (false);
		puedoModificarSolicitudRespondida.setDescripcion("error.validacionessolicitud.noExisteSolicitud");

		puedoModificarSolicitudSolicitada=new ResultadoBoolean (false);
		puedoModificarSolicitudSolicitada.setDescripcion("error.validacionessolicitud.noExisteSolicitud");

		this.puedoModificarSolicitudInterpretada=new ResultadoBoolean (false);
		this.puedoModificarSolicitudInterpretada.setDescripcion("error.validacionessolicitud.noExisteSolicitud");

		tieneSolicitudVolverTratante=new ResultadoBoolean (false);
		tieneSolicitudVolverTratante.setDescripcion("error.validacionessolicitud.noExisteSolicitud");
		tieneSolicitudAdjunto=new ResultadoBoolean (false);
		tieneSolicitudAdjunto.setDescripcion("error.validacionessolicitud.noExisteSolicitud");
		
		puedoAnularModificarSolicitudMedicamentos=new ResultadoBoolean (false);
		puedoAnularModificarSolicitudMedicamentos.setDescripcion("error.validacionessolicitud.noExisteSolicitud");
		
		puedoSuspenderSolicitudMedicamentos=new ResultadoBoolean (false);
		puedoSuspenderSolicitudMedicamentos.setDescripcion("error.validacionessolicitud.noExisteSolicitud");
		
		this.estaAnulada=false;
		this.estaCuentaSolicitudAbierta=false;
		this.cuentaSolicitudSinOrdenSalida=false;
		
		this.codigoMedicoInterpretacion=0;
		
		
		
		tieneDespachos=false;
		estadoFacturacionPendiente=false;
		estadoSolicitada=false;
		
	}
	
	/**
	 * Método constructor de ValidacionesSolicitud que recibe
	 * todos los datos necesarios para validar (La validación
	 * se ejecuta cuando se utiliza el método cargar)
	 * 
	 * @param numeroSolicitud Número de la solicitud a validar
	 * @param medico Médico que intenta trabajar con esta
	 * solicitud
	 * @param paciente Paciente al que se va a responder
	 * esta solicitud
	 */
	public ValidacionesSolicitud (Connection con,int numeroSolicitud, UsuarioBasico medico, PersonaBasica paciente) throws SQLException
	{
		this.clean();
		this.numeroSolicitud=numeroSolicitud;
		this.medico=medico;

		this.paciente=paciente;
		if (this.medico==null)
		{
			//Si el médico es nulo, para evitar errores trabajamos
			//con el usuarioBasico por defecto PERO mandamos un
			//error en los logs
			this.medico=new UsuarioBasico();
			logger.error("El medico dado en la validación de permisos de la solicitud es nulo");
		}
		if (this.paciente==null)
		{
			this.paciente=new PersonaBasica();
			logger.error("El paciente dado en la validación de permisos de la solicitud es nulo");
		}
		this.init(System.getProperty("TIPOBD"));
		this.cargar(con);
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
			validacionesSolicitudDao = myFactory.getValidacionesSolicitudDao();
			wasInited = (validacionesSolicitudDao != null);
		}
		return wasInited;
	}
	
	/**
	 * Método que carga todos los valores que (posiblemente)
	 * se necesiten al momento de validar
	 * 
	 * @param con Conexión con la fuente de datos
	 * @throws SQLException
	 */
	private void cargar (Connection con) throws SQLException
	{
		ResultSetDecorator rs=validacionesSolicitudDao.cargarDatosBasicos(con, numeroSolicitud);
		int idCuenta=0, codigoEstadoHistoriaClinica;

		//UsuarioBasico medico=new UsuarioBasico();
		if (rs.next())
		{
			this.existe=true;
			idCuenta=paciente.getCodigoCuenta();
			//idCuenta=rs.getInt("codigoCuenta");
			//si no funciona quitar lo siguiente y descomentar la linea anterior.
			//
			this.codigoTipoSolicitud=rs.getInt("codigoTipoSolicitud");
			
			//Se asigna el código del centro de costo del paciente cargado
			this.codigoCentroCostoPaciente=paciente.getCodigoUltimaViaIngreso();
			
			if(this.codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudInterconsulta)
				this.codigoEspecialidadSolicitada = UtilidadesOrdenesMedicas.obtenerEspecialidadSolicitadaInterconsulta(con, this.numeroSolicitud+"").getCodigo();
			
			
			//Las validaciones solo existen para los casos de Interconsulta
			//y Procedimientos 
			if (this.codigoTipoSolicitud!=ConstantesBD.codigoTipoSolicitudInterconsulta&&
				this.codigoTipoSolicitud!=ConstantesBD.codigoTipoSolicitudProcedimiento&&
				this.codigoTipoSolicitud!=ConstantesBD.codigoTipoSolicitudCita&&
				this.codigoTipoSolicitud!=ConstantesBD.codigoTipoSolicitudMedicamentos)
			{
				this.mensajesOtroTipoSolicitud();
				return;
			}

			codigoEstadoHistoriaClinica=rs.getInt("codigoEstadoHistoriaClinica");
			
			if (codigoEstadoHistoriaClinica==ConstantesBD.codigoEstadoHCInterpretada)
			{
				codigoMedicoInterpretacion=rs.getInt("codigoMedicoInterpretacion");
				//logger.info("ValidacionesSolicitud --> codigoMedicoInterpretacion "+codigoMedicoInterpretacion);
			}
			this.codigoCentroCostoSolicitado=rs.getInt("codigoCentroCostoSolicitado");
			this.codigoCentroCostoQueSolicita=rs.getInt("codigoCentroCostoQueSolicita");
			
			
			
			this.codigoOcupacionSolicitada=rs.getInt("codigoOcupacionSolicitada");

			this.codigoMedicoCreador=rs.getInt("codigoMedicoCreador");
			
			
			if (codigoMedicoCreador==this.medico.getCodigoPersona())
			{
				esCreador=true;
			}
			else
			{
				esCreador=false;
			}
			
			//Vamos a validar si es tratante, adjunto o solo concepto
			//Debo utilizar el método lento porque la cuenta del paciente
			//dado puede NO coincidir con la cuenta buscada (Ej. Cuenta
			//antigua)
			//logger.info("cc solicitado-->"+this.codigoCentroCostoSolicitado);
			//logger.info("cc solicita-->"+this.codigoCentroCostoQueSolicita);
			//codigoCentroCostoTratante=UtilidadValidacion.getCodigoCentroCostoTratanteMetodoLento(con, idCuenta, this.medico.getCodigoInstitucionInt());
			codigoCentroCostoTratante=paciente.getCodigoArea();
			//logger.info("cc tratante lento-->"+codigoCentroCostoTratante);
			//logger.info("cc tratante -->"+UtilidadValidacion.esMedicoTratante(con,medico,paciente));
			//logger.info("*********************************************************************************************");
			if (codigoCentroCostoTratante==medico.getCodigoCentroCosto())
			{
				//Si es tratante NO hay necesidad de averiguar
				//adjunto/ concepto
				esTratante.setResultado(true);
				
				esAdjunto.setResultado(false);
				esAdjunto.setDescripcion("error.validacionessolicitud.adjunto.esTratante");
			}
			else if (UtilidadValidacion.esAdjuntoCuenta(con, idCuenta, medico.getLoginUsuario()))
			{
				esAdjunto.setResultado(true);
				
				esTratante.setResultado(false);
				esTratante.setDescripcion("error.validacionessolicitud.tratante.esAdjunto");
			}
			else
			{
				esAdjunto.setResultado(false);
				esAdjunto.setDescripcion("error.validacionessolicitud.medicoNoTratanteNiAdjunto");
				
				esTratante.setResultado(false);
				esTratante.setDescripcion("error.validacionessolicitud.medicoNoTratanteNiAdjunto");
			}
			
			
			//Para los casos de responder e interpretar
			//se repiten dos validaciones, cuenta abierta
			//y sin egreso
			
			estaCuentaSolicitudAbierta=(paciente.getCodigoCuenta()==idCuenta);
			
			//Si no está trabajando con la cuenta abierta, hay otro caso
			//que debemos tener en cuenta, el asocio de cuenta
			if (!estaCuentaSolicitudAbierta&&paciente.getExisteAsocio())
			{
				if (paciente.getCodigoCuentaAsocio()==idCuenta)
				{
					estaCuentaSolicitudAbierta=true;
				}
			}
			
			//No inicializo la de egreso acá porque por defecto viene
			//en false y siempre está combinada con la de la cuenta
			//abierta
			
			if (estaCuentaSolicitudAbierta)
			{
				//Si tiene la cuenta abierta y sin orden de salida
				//la puede responder SI el estado medico es 
				//solicitada y el de facturacion Pendiente
				
				cuentaSolicitudSinOrdenSalida=!UtilidadValidacion.tieneEgreso(con, idCuenta) ;
				
				//Si la cuenta NO tiene orden de salida 
				//o es asocio con las condiciones que cambian
				//a estaCuentaSolicitudAbierta
				if (cuentaSolicitudSinOrdenSalida||paciente.getExisteAsocio())
				{
					validacionParticularResponder (codigoEstadoHistoriaClinica);
					validacionParticularInterpretar(codigoEstadoHistoriaClinica);
				}
				else
				{
					this.puedoResponder.setResultado(false);
					this.puedoResponder.setDescripcion("error.validacionessolicitud.responder.conOrdenSalida");
					
					this.puedoInterpretar.setResultado(false);
					this.puedoInterpretar.setDescripcion("error.validacionessolicitud.interpretar.conOrdenSalida");
				}
			}
			else
			{
				this.puedoResponder.setResultado(false);
				this.puedoResponder.setDescripcion("error.validacionessolicitud.responder.cuentaNoAbierta");

				this.puedoInterpretar.setResultado(false);
				this.puedoInterpretar.setDescripcion("error.validacionessolicitud.interpretar.cuentaNoAbierta");
			}
			
			/**
			 * Nota * Se quitó esta validación porque ya no se están realizando solicitudes sin servicio
			 * if (validacionesSolicitudDao.esTipoOtros(con, numeroSolicitud, codigoTipoSolicitud))
			{
				this.esOtros=true;
			}
			else
			{
				this.esOtros=false;
			}**/
			
			if (validacionesSolicitudDao.estaSolicitudAnulada(con, numeroSolicitud))
			{
				this.estaAnulada=true;
			}
			else
			{
				this.estaAnulada=false;
			}
			ResultadoBoolean respTemp=this.validacionPuedoModificar(con, codigoEstadoHistoriaClinica);
			
			
			
			if (respTemp.isTrue())
			{
				if (codigoEstadoHistoriaClinica==ConstantesBD.codigoEstadoHCSolicitada || codigoEstadoHistoriaClinica==ConstantesBD.codigoEstadoHCTomaDeMuestra)
				{
					this.puedoModificarSolicitudSolicitada=respTemp;
					this.puedoModificarSolicitudInterpretada=new ResultadoBoolean(false);
					this.puedoModificarSolicitudInterpretada.setDescripcion("error.validacionessolicitud.modificar.falloCasoInterpretado");
					this.puedoModificarSolicitudRespondida=new ResultadoBoolean(false);
					this.puedoModificarSolicitudRespondida.setDescripcion("error.validacionessolicitud.modificar.falloCasoRespondido");
				}
				if (codigoEstadoHistoriaClinica==ConstantesBD.codigoEstadoHCRespondida)
				{
					//Se puede modificar la respuesta pero NO la interp
					this.puedoModificarSolicitudRespondida=respTemp;
					this.puedoModificarSolicitudSolicitada=new ResultadoBoolean(false);
					this.puedoModificarSolicitudSolicitada.setDescripcion("error.validacionessolicitud.modificar.falloCasoRespondido");

					if(codigoCentroCostoQueSolicita==this.medico.getCodigoCentroCosto())
					{
						this.puedoModificarSolicitudInterpretada=new ResultadoBoolean(true);
					}
					else
					{
						this.puedoModificarSolicitudInterpretada=new ResultadoBoolean(false);
						this.puedoModificarSolicitudInterpretada.setDescripcion("error.validacionessolicitud.modificar.falloCasoInterpretado");
					}
				}
				else if (codigoEstadoHistoriaClinica==ConstantesBD.codigoEstadoHCInterpretada)
				{
					//Se puede modificar la Interpretacion pero NO la resp
					this.puedoModificarSolicitudInterpretada=respTemp;
					this.puedoModificarSolicitudRespondida=new ResultadoBoolean(false);
					this.puedoModificarSolicitudRespondida.setDescripcion("error.validacionessolicitud.modificar.falloCasoInterpretado");
					
					this.puedoModificarSolicitudSolicitada=new ResultadoBoolean(false);
					this.puedoModificarSolicitudSolicitada.setDescripcion("error.validacionessolicitud.modificar.falloCasoSolicitada");
				}
				/*
				 * --------------------------------- OJO --------------------------------------	*
				 * ESTE CASO ES UTILIZADO EXCLUSIVAMENTE PARA LAS SOLICITUDES DE MEDICAMENTOS
				 * ASI QUE NO APLICA PARA ESTADOS "INTERPRETADA" Y "RESPONDIDA"
				 */
				else if(codigoEstadoHistoriaClinica!=ConstantesBD.codigoEstadoHCSolicitada)
				{
				
					this.puedoModificarSolicitudInterpretada=new ResultadoBoolean(false);
					this.puedoModificarSolicitudInterpretada.setDescripcion("error.validacionessolicitud.modificar.falloCasoInterpretado");
					this.puedoModificarSolicitudRespondida=new ResultadoBoolean(false);
					this.puedoModificarSolicitudRespondida.setDescripcion("error.validacionessolicitud.modificar.falloCasoRespondido");

					this.puedoModificarSolicitudSolicitada=new ResultadoBoolean(false);
					this.puedoModificarSolicitudSolicitada.setDescripcion("error.validacionessolicitud.modificar.falloCasoSolicitada");
				}
			}
			else
			{
				
				this.puedoModificarSolicitudInterpretada=respTemp;
				this.puedoModificarSolicitudRespondida=respTemp;
				this.puedoModificarSolicitudSolicitada=respTemp;
			}
			
			
			/*caso de tomas de muestra*/
			
			if (this.codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudProcedimiento) 
			{
				if (codigoEstadoHistoriaClinica==ConstantesBD.codigoEstadoHCSolicitada || codigoEstadoHistoriaClinica==ConstantesBD.codigoEstadoHCTomaDeMuestra)
				{
					this.puedoModificarSolicitudSolicitada=respTemp;
					this.puedoModificarSolicitudInterpretada=new ResultadoBoolean(false);
					this.puedoModificarSolicitudInterpretada.setDescripcion("error.validacionessolicitud.modificar.falloCasoInterpretado");
					this.puedoModificarSolicitudRespondida=new ResultadoBoolean(false);
					this.puedoModificarSolicitudRespondida.setDescripcion("error.validacionessolicitud.modificar.falloCasoRespondido");
				}
			}	
			
			
			//Aqui vienen los casos especiales
			
			//Caso 1: Centro de costo externo
			//Si la cuenta esta abierta, sin orden de salida
			//con los estados para responder y su centro
			//de costo es externo, solo el creador la 
			//puede responder, al resto de casos les 
			//mostramos un mensaje de error
			
			//*************************************
			/**
			 * Nota * Se quitó esta parte porque el concepto de centro costo externo se quitó hace ya mucho tiempo
			 * 
			 * 
			if (estaCuentaSolicitudAbierta&&cuentaSolicitudSinOrdenSalida&&
				codigoEstadoHistoriaClinica==ConstantesBD.codigoEstadoHCSolicitada&&
				this.codigoCentroCostoSolicitado==ConstantesBD.codigoCentroCostoExternos)
			{
				if (this.esCreador && !paciente.getExisteAsocio())
				{
					
					this.puedoResponder.setResultado(true);
				}
				else if(this.esTratante.isTrue() && paciente.getExisteAsocio())
				{
					
					this.puedoResponder.setResultado(true);
				}
				else
				{
					
					this.puedoResponder.setResultado(false);
					this.puedoResponder.setDescripcion("error.validacionessolicitud.responder.tipoOtros");
					this.tieneSolicitudVolverTratante.setDescripcion("error.validacionessolicitud.responder.tipoOtros");
					this.tieneSolicitudAdjunto.setDescripcion("error.validacionessolicitud.responder.tipoOtros");
					this.esSoloConcepto.setDescripcion("error.validacionessolicitud.responder.tipoOtros");
				}
			}
			
			if (this.codigoCentroCostoSolicitado==ConstantesBD.codigoCentroCostoExternos)
			{
				//verificacion si es centro tratante codigoCentroCostoTratante==medico.getCodigoCentroCosto()
				if (esTratante.isTrue()&&(codigoEstadoHistoriaClinica==ConstantesBD.codigoEstadoHCRespondida))
				{
					this.puedoModificarSolicitudRespondida.setResultado(true);
					this.puedoModificarSolicitudSolicitada.setResultado(false);
				}
				else
				{
					this.puedoModificarSolicitudRespondida.setResultado(false);
					this.puedoModificarSolicitudRespondida.setDescripcion("error.validacionessolicitud.modificar.noTratante.centroCostoExterno");

					this.puedoModificarSolicitudSolicitada.setResultado(true);
					//this.puedoModificarSolicitudSolicitada.setDescripcion("error.validacionessolicitud.modificar.noTratante.centroCostoExterno");
				}
				this.puedoModificarSolicitudInterpretada.setResultado(false);
				this.puedoModificarSolicitudInterpretada.setDescripcion("error.validacionessolicitud.modificar.interpretado.centroCostoExterno");
				if(this.esTratante.isTrue()&&codigoEstadoHistoriaClinica==ConstantesBD.codigoEstadoHCInterpretada)
				{
					this.puedoModificarSolicitudInterpretada.setResultado(true);
				}
			}
			**/
            
			/******************************VALIDACIONES SOLICITUDES TIPO CITA*******************************************************
			if (this.codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudCita
				||this.codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudConsultaGinecoObstetrica)
			{
				this.puedoResponder.setResultado(true);
				
				if (codigoEstadoHistoriaClinica==ConstantesBD.codigoEstadoHCSolicitada)
				{
					if (validacionesSolicitudDao.puedoResponderSolicitudCasoConsultaExterna(con, this.numeroSolicitud, this.medico.getCodigoPersona()))
						this.puedoResponder.setResultado(true);
					else
					{
						
						
						
						/**OJO!! este codigo fue ingresado con el fin de que las solicitudes de Interconsulta con centro
						 * de costo Consulta Externa se pudieran responder. El código de este ELSE estaba antes como estan los 
						 * comentarios en verde (ver oid=[4368])**

						//- Se adiciono this.codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudProcedimiento al if
						//- Porque tambien se debe poder responder Solicitudes de procedimientos 
						//- Tarea Xplanner2  Axioma Calidad-Sep1605  Tarea Id ---> 2320. 
						
						logger.info("this.codigoTipoSolicitud "+this.codigoTipoSolicitud);
						
						if(this.codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudInterconsulta
							|| this.codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudInterconsultaGinecoObstetrica
							|| this.codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudProcedimiento
							|| this.codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudCita
							|| this.codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudConsultaGinecoObstetrica
						)
						{
							this.puedoResponder.setResultado(true);
						}
						else{
							this.puedoResponder.setResultado(false);
							this.puedoResponder.setDescripcion("error.validacionessolicitud.consultaExterna.responder");
						}
						/**----------------------------------------------------------------------------------- **
						/*this.puedoResponder.setResultado(false);
							this.puedoResponder.setDescripcion("error.validacionessolicitud.consultaExterna.responder");
						 *
						 
					}
				}
				if (codigoEstadoHistoriaClinica==ConstantesBD.codigoEstadoHCRespondida)
				{
					if (validacionesSolicitudDao.puedoModificarSolicitudCasoConsultaExterna(con, this.numeroSolicitud))
					{
						this.puedoModificarSolicitudRespondida.setResultado(true);
					}
					else
					{
						this.puedoModificarSolicitudRespondida.setResultado(false);
						this.puedoModificarSolicitudRespondida.setDescripcion("error.validacionessolicitud.consultaExterna.modificarRespuesta");
					}
				}
			}
			//*******************************************************************************************************************/
			
			//validaciones para la soliciutd de medicamentos.
			if(this.codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudMedicamentos)
			{	
				tieneDespachos=validacionesSolicitudDao.solicitudMedicamentosTieneDespachos(con,this.numeroSolicitud);
				estadoFacturacionPendiente=validacionesSolicitudDao.solicitudEstadoFacturacionPendiente(con,numeroSolicitud);
				estadoSolicitada=validacionesSolicitudDao.solicitudEstadoSolicitada(con,numeroSolicitud);
				boolean estadoDespachada=validacionesSolicitudDao.solicitudEstadoDespachada(con,numeroSolicitud);
				if(!tieneDespachos && estadoFacturacionPendiente && estadoSolicitada && !estaAnulada)
				{
					puedoAnularModificarSolicitudMedicamentos.setResultado(true);
					puedoSuspenderSolicitudMedicamentos.setResultado(true);
					puedoSuspenderSolicitudMedicamentos.setDescripcion("");
				}
		
				else
				{
					if(/*estadoFacturacionPendiente &&*/ !estaAnulada && (estadoSolicitada || estadoDespachada))
					{
						puedoSuspenderSolicitudMedicamentos.setResultado(true);
						puedoSuspenderSolicitudMedicamentos.setDescripcion("");
					}
					puedoAnularModificarSolicitudMedicamentos.setResultado(false);
					
					if(estaAnulada)
						puedoAnularModificarSolicitudMedicamentos.setDescripcion("error.validacionessolicitud.yaAnulada");
					else if(!estadoSolicitada)
						puedoAnularModificarSolicitudMedicamentos.setDescripcion("error.validacionessolicitud.medicamentos.anular.estadoNoSolicitada");
					else if(!estadoFacturacionPendiente)
						puedoAnularModificarSolicitudMedicamentos.setDescripcion("error.validacionessolicitud.medicamentos.anular.estadoFacturacionNoPendiente");
					else if(tieneDespachos)
						puedoAnularModificarSolicitudMedicamentos.setDescripcion("error.validacionessolicitud.medicamentos.tieneDespachos");
					
				}
			}
			else
			{
				tieneDespachos=false;
				estadoFacturacionPendiente=false;
				estadoSolicitada=false;
				puedoAnularModificarSolicitudMedicamentos.setResultado(false);
				puedoAnularModificarSolicitudMedicamentos.setDescripcion("error.validacionessolicitud.medicamentos.anular.diferente");
			}
			
		}
		//Si no existe la solicitud
		//se dejan los valores por defecto (No existe, 0 permisos)
		else
		{
			this.clean();		
		}
	}

	/**
	 * Validación particular para saber si se puede responder
	 * esta solicitud, dadas las condiciones iniciales
	 * 
	 * @param codigoEstadoFacturacion Código del estado
	 * de facturación de la solicitud
	 * @param codigoEstadoHistoriaClinica Código de historia
	 * clínica de la solicitud
	 */
	private void validacionParticularResponder (int codigoEstadoHistoriaClinica)
	{
		if (codigoEstadoHistoriaClinica==ConstantesBD.codigoEstadoHCSolicitada)
		{
			this.puedoResponder=this.medicoCumpleRestriccion(medico, codigoEspecialidadSolicitada, codigoOcupacionSolicitada, codigoCentroCostoSolicitado, codigoTipoSolicitud);
		}
		else
		{
			this.puedoResponder.setResultado(false);
			this.puedoResponder.setDescripcion("error.validacionessolicitud.responder.estadoHNoSolicitada");
		}
	}

	
	
	/**
	 * Método que verifica si una solicitud tiene cambio de tratante
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean tieneSolicitudCambioTratante(Connection con,int numeroSolicitud)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesSolicitudDao().tieneSolicitudCambioTratante(con, numeroSolicitud);
	}

	/**
	 * Validación particular para saber si se puede interpretar
	 * esta solicitud, dadas las condiciones iniciales
	 * 
	 * @param codigoEstadoHistoriaClinica Código de historia
	 * clínica de la solicitud
	 */
	private void validacionParticularInterpretar (int codigoEstadoHistoriaClinica)
	{
		if (codigoEstadoHistoriaClinica==ConstantesBD.codigoEstadoHCRespondida)
		{
			this.puedoInterpretar.setResultado(true);
		}
		else
		{
			this.puedoInterpretar.setResultado(false);
			this.puedoInterpretar.setDescripcion("error.validacionessolicitud.interpretar.estadoHNoRespondida");
		}
	}

	private ResultadoBoolean validacionPuedoModificar (Connection con, int codigoEstadoHistoriaClinica) throws SQLException
	{
		ResultadoBoolean respuestaPuedoModificar=new ResultadoBoolean (false);
		respuestaPuedoModificar.setDescripcion("error.validacionessolicitud.combinacionEstadosInvalida");
		
		if (this.estaAnulada)
		{
			respuestaPuedoModificar.setDescripcion("error.validacionessolicitud.yaAnulada");
		}
		
		if (this.codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudInterconsulta )
		{
			if (codigoEstadoHistoriaClinica==ConstantesBD.codigoEstadoHCSolicitada)
			{
				
				if (this.medico.getCodigoCentroCosto()==codigoCentroCostoTratante || UtilidadValidacion.esMedicoTratante(con, medico, paciente).equals(""))
				{
					respuestaPuedoModificar.setResultado(true);
				}
				else
				{
					respuestaPuedoModificar.setResultado(false);
					respuestaPuedoModificar.setDescripcion("error.validacionessolicitud.modificar.interconsulta.soloTratante");
				}
				return respuestaPuedoModificar;
			}
		}
		
//////////////////////SOLICITUD DE PROCEDIMIENTOS.		
		else if ((codigoEstadoHistoriaClinica==ConstantesBD.codigoEstadoHCSolicitada || codigoEstadoHistoriaClinica==ConstantesBD.codigoEstadoHCTomaDeMuestra)&&this.codigoTipoSolicitud==ConstantesBD.codigoTipoSolicitudProcedimiento)
		{
			/*logger.info("\n ENTRO A SOLICITUD PROCEDIMIENTO \n");	
			logger.info("Centro costo-->"+this.codigoCentroCostoPaciente+"\n");
			logger.info("Medico nombre-->"+medico.getNombreRegistroMedico()+"\n");*/
		
			//Se verifica si el paciente es del centro de costo consulta externa o ambularios y si el usuario es médico
			if(this.codigoCentroCostoPaciente==ConstantesBD.codigoViaIngresoConsultaExterna || this.codigoCentroCostoPaciente==ConstantesBD.codigoViaIngresoAmbulatorios)
			{
				if(UtilidadValidacion.esMedico(medico).equals(""))
				{
					respuestaPuedoModificar.setResultado(true);
					return respuestaPuedoModificar;
				}
				else
				{
					respuestaPuedoModificar.setResultado(false);
					respuestaPuedoModificar.setDescripcion(UtilidadValidacion.esMedico(medico));
					return respuestaPuedoModificar;
				}
			}
			
			if (this.codigoCentroCostoSolicitado==ConstantesBD.codigoCentroCostoExternos)
			{
				if (esTratante.isTrue())
				{
					respuestaPuedoModificar.setResultado(true);
				}
				else
				{
					respuestaPuedoModificar.setResultado(false);
					respuestaPuedoModificar.setDescripcion("error.validacionessolicitud.modificar.procedimiento.soloCreador");
				}
				return respuestaPuedoModificar;
			}
			
			//logger.info("\n\n\n\nes tratante-->"+this.esTratante.isTrue());
			//El tratante lo puede modificar
			if (this.esTratante.isTrue() || UtilidadValidacion.esMedicoTratante(con, medico, paciente).equals(""))
			{
				respuestaPuedoModificar.setResultado(true);
				return respuestaPuedoModificar;
			}
			//logger.info("\n\n\n\nes adjunto-->"+this.esAdjunto.isTrue());
			//El adjunto también, pero solo si el la creo
			if (this.esAdjunto.isTrue()&&this.codigoCentroCostoQueSolicita==medico.getCodigoCentroCosto())
			{
				respuestaPuedoModificar.setResultado(true);
				return respuestaPuedoModificar;
			}
			//Esta validacion no aplica ya que si el paciente es remitido a hospitalizacion permite que el medico que solicito así sea el de urgencias
			//pueda modificar la solicitud.
			/*if(this.codigoCentroCostoQueSolicita==medico.getCodigoCentroCosto())
			{
				respuestaPuedoModificar.setResultado(true);
				return respuestaPuedoModificar;
			}
			else
				if(this.codigoCentroCostoQueSolicita!=medico.getCodigoCentroCosto())
			{
				respuestaPuedoModificar.setResultado(false);
				return respuestaPuedoModificar;
			}*/
		}

		//De este punto en adelante las validaciones son exactamente
		//las mismas tanto para procedimientos como interconsultas
		//(Se valida al principio)
		if (codigoEstadoHistoriaClinica==ConstantesBD.codigoEstadoHCRespondida)
		{
			if (this.estaCuentaSolicitudAbierta)
			{
				if (this.cuentaSolicitudSinOrdenSalida)
				{
					respuestaPuedoModificar.setResultado(true);
				}
				else
				{
					respuestaPuedoModificar.setResultado(false);
					respuestaPuedoModificar.setDescripcion("error.validacionessolicitud.modificar.estadoRespondido.cuentaConOrdenSalida");
				}
			}
			else
			{
				respuestaPuedoModificar.setResultado(false);
				respuestaPuedoModificar.setDescripcion("error.validacionessolicitud.modificar.estadoRespondido.cuentaNoAbierta");
			}
		}

		if (codigoEstadoHistoriaClinica==ConstantesBD.codigoEstadoHCInterpretada)
		{
			
			String mensaje = UtilidadValidacion.esMedicoTratante(con,medico,paciente); 
			if(mensaje.equals(""))
			{
				respuestaPuedoModificar.setResultado(true);
			}
			else
			{
				respuestaPuedoModificar.setResultado(false);
				//se verifica si se ha definido la ocupación médico especialista y general
				//de parámetros generales
				if(mensaje.equals("errors.noOcupacionMedica"))
					respuestaPuedoModificar.setDescripcion(mensaje);
				else
					respuestaPuedoModificar.setDescripcion("error.validacionessolicitud.modificar.estadoInterpretado.medicoNoInterpreto");
			}
			//validaciones caso solicitudes a centros de costo externo
			if((this.codigoCentroCostoSolicitado==ConstantesBD.codigoCentroCostoExternos)&&mensaje.equals(""))
			{
				respuestaPuedoModificar.setResultado(true);
			}
		}
		
		if(this.codigoCentroCostoSolicitado!=this.medico.getCodigoCentroCosto()&&
			respuestaPuedoModificar.getDescripcion().equals("error.validacionessolicitud.combinacionEstadosInvalida")&&
			!respuestaPuedoModificar.isTrue())
		{
			//logger.info("\n\n\n\nerror.validacionessolicitud.noCentroCostoUsuario");
			respuestaPuedoModificar.setDescripcion("error.validacionessolicitud.noCentroCostoUsuario");
		}
		//Esta validacion no aplica ya que si el paciente es remitido a hospitalizacion permite que el medico que solicito así sea el de urgencias
		//pueda modificar la solicitud. Cuando se hace la hospitalizacion el sistema actualiza el medico tratante no el codigo del centro de costo que solicita.
		/*if(this.codigoCentroCostoQueSolicita==medico.getCodigoCentroCosto())
		{
			respuestaPuedoModificar.setResultado(true);
			return respuestaPuedoModificar;
		}*/
		//logger.info("respuestaPuedoModificar-->"+respuestaPuedoModificar.getDescripcion()+"  "+respuestaPuedoModificar.isTrue());		
		return respuestaPuedoModificar; 
	}

	/**
	 * Método que revisa si el médico cumple con 
	 * las restricciones trabajar con esta solicitud
	 *  
	 * @param medico Médico a revisar
	 * @param codigoEspecialidadSolicitada Código de la
	 * especialidad solicitada
	 * @param codigoOcupacionSolicitada Código de la
	 * ocupación solicitada
	 * @param codigoCentroCostoSolicitado Código del
	 * centro de costo solicitado
	 * @param codigoTipoSolicitud2 
	 * @return
	 */
	public ResultadoBoolean medicoCumpleRestriccion (UsuarioBasico medico, int codigoEspecialidadSolicitada, int codigoOcupacionSolicitada, int codigoCentroCostoSolicitado, int codigoTipoSolicitud2)
	{
		ResultadoBoolean respuesta=new ResultadoBoolean (false);
		respuesta.setDescripcion("error.validacionessolicitud.noExisteSolicitud");
		
		//el siguiente if se colocó por requerimieno 777
		if(codigoTipoSolicitud2!=ConstantesBD.codigoTipoSolicitudInterconsulta)
		{	
			if (medico.getCodigoCentroCosto()!=codigoCentroCostoSolicitado&&codigoCentroCostoSolicitado!=ConstantesBD.codigoOtros)
			{
				respuesta.setResultado(false);
				respuesta.setDescripcion("error.validacionessolicitud.noCentroCosto");
				return respuesta;
			}
		}	
		
		if (medico.getCodigoOcupacionMedica()!=codigoOcupacionSolicitada&&codigoOcupacionSolicitada!=ConstantesBD.codigoOtros)
		{
			respuesta.setResultado(false);
			respuesta.setDescripcion("error.validacionessolicitud.noOcupacion");
			return respuesta;
		}
		if (!medico.tieneEspecialidad(codigoEspecialidadSolicitada)&&codigoEspecialidadSolicitada!=ConstantesBD.codigoOtros)
		{
			respuesta.setResultado(false);
			respuesta.setDescripcion("error.validacionessolicitud.noEspecialidad");
			return respuesta;
		}

		//No se salio por ninguna otra parte, luego cumplio con los requerimientos
		respuesta.setResultado(true);
		return respuesta;
	}

	/**
	 * Método que dice si esta solicitud existe
	 * @return
	 */
	public boolean existe()
	{
		return this.existe;
	}

	/**
	 * Método que dice si se puede intrepretar
	 * esta solicitud
	 * @return
	 */
	public ResultadoBoolean puedoInterpretar()
	{
		return this.puedoInterpretar;
	}

	/**
	 * Método que dice si este médico es el creador
	 * @return
	 */
	public boolean esCreador()
	{
		return this.esCreador;
	}

	/**
	 * Método que devuelve el código del tipo
	 * de esta solicitud
	 * @return
	 */
	public int getCodigoTipoSolicitud ()
	{
		return this.codigoTipoSolicitud;
	}

	/**
	 * Método que dice si esta solicitud puede
	 * ser respondida
	 * @return
	 */
	public ResultadoBoolean puedoResponder()
	{
		return this.puedoResponder;
	}

	/**
	 * Método que dice si este médico es el
	 * tratante
	 * @return
	 */
	public ResultadoBoolean esTratante()
	{
		return esTratante;
	}

	/**
	 * Método que dice si este médico es el
	 * adjunto
	 * @return
	 */
	public ResultadoBoolean esAdjunto()
	{
		return esAdjunto;
	}

	/**
	 * Método que dice si hay una solicitud para
	 * volver tratante
	 * @return
	 */
	public ResultadoBoolean tieneSolicitudVolverTratante()
	{
		return tieneSolicitudVolverTratante;
	}
	
	/**
	 * Método que dice si hay una solicitud para
	 * volver adjunto
	 * @return
	 */
	public ResultadoBoolean tieneSolicitudAdjunto()
	{
		return tieneSolicitudAdjunto;
	}
	
	/**
	 * Método que dice si la solicitud para
	 * volver adjunto
	 * @return
	 */
	public ResultadoBoolean esSoloConcepto()
	{
		return esSoloConcepto;
	}

	/**
	 * Método que dice si esta solicitud es de tipo
	 * otros
	 * @return
	 */
	public boolean esTipoOtro()
	{
		return this.esOtros;
	}

	/**
	 * Método que dice si esta solicitud ya fué anulada
	 * @return
	 */	
	public boolean getEstaAnulada ()
	{
		return estaAnulada;
	}
	
	/**
	 * Método que me dice si se puede modificar esta solicitud
	 * @return
	 */	
	public ResultadoBoolean puedoModificarSolicitudRespondida()
	{
		return this.puedoModificarSolicitudRespondida;
	}
	/**
	 * Método que me dice si se puede modificar esta solicitud
	 * @return
	 */	
	public ResultadoBoolean puedoModificarSolicitudSolicitada()
	{
		return puedoModificarSolicitudSolicitada;
	}
	/**
	 * Método que me dice si se puede modificar esta solicitud
	 * @return
	 */	
	public ResultadoBoolean puedoModificarSolicitudInterpretada()
	{
		return this.puedoModificarSolicitudInterpretada;
	}

	public void mensajesOtroTipoSolicitud()
	{
		existe=true;
		esCreador=false;
		esOtros=false;
		
		this.codigoCentroCostoSolicitado=0;
		this.codigoCentroCostoQueSolicita=0;
		this.codigoOcupacionSolicitada=0;
		
		codigoCentroCostoTratante=0;
		codigoMedicoCreador=0;
		
		//Por defecto el código de la especialidad solicitada
		//lo ponemos en otros y si alguna solicitud en particular
		//lo cambia, se debe manejar como interconsultas
		this.codigoEspecialidadSolicitada=ConstantesBD.codigoOtros;
		
		puedoResponder=new ResultadoBoolean (false);
		puedoResponder.setDescripcion("error.validacionessolicitud.validacionesInexistentesTipoSolicitud");
		puedoInterpretar=new ResultadoBoolean (false);
		puedoInterpretar.setDescripcion("error.validacionessolicitud.validacionesInexistentesTipoSolicitud");
		esTratante=new ResultadoBoolean (false);
		esTratante.setDescripcion("error.validacionessolicitud.validacionesInexistentesTipoSolicitud");
		esAdjunto=new ResultadoBoolean (false);
		esAdjunto.setDescripcion("error.validacionessolicitud.validacionesInexistentesTipoSolicitud");
		esSoloConcepto=new ResultadoBoolean (false);
		esSoloConcepto.setDescripcion("error.validacionessolicitud.validacionesInexistentesTipoSolicitud");
		
		puedoModificarSolicitudRespondida=new ResultadoBoolean (false);
		puedoModificarSolicitudRespondida.setDescripcion("error.validacionessolicitud.validacionesInexistentesTipoSolicitud");
		
		this.puedoModificarSolicitudInterpretada=new ResultadoBoolean (false);
		this.puedoModificarSolicitudInterpretada.setDescripcion("error.validacionessolicitud.validacionesInexistentesTipoSolicitud");

		tieneSolicitudVolverTratante=new ResultadoBoolean (false);
		tieneSolicitudVolverTratante.setDescripcion("error.validacionessolicitud.validacionesInexistentesTipoSolicitud");
		tieneSolicitudAdjunto=new ResultadoBoolean (false);
		tieneSolicitudAdjunto.setDescripcion("error.validacionessolicitud.validacionesInexistentesTipoSolicitud");

		
	
		tieneDespachos=false;
		this.estaCuentaSolicitudAbierta=false;
		this.cuentaSolicitudSinOrdenSalida=false;
		
		this.codigoMedicoInterpretacion=0;
		
		//para solicitudes de medicamentos
		puedoAnularModificarSolicitudMedicamentos=new ResultadoBoolean (false);
		puedoAnularModificarSolicitudMedicamentos.setDescripcion("error.validacionessolicitud.medicamentos.anular.diferente");
		puedoSuspenderSolicitudMedicamentos=new ResultadoBoolean (false);
		puedoSuspenderSolicitudMedicamentos.setDescripcion("error.validacionessolicitud.medicamentos.anular.diferente");
		tieneDespachos=false;
		estadoFacturacionPendiente=false;
		estadoSolicitada=false;

	}


	
    



	/**
	 * @return Retorna el tieneDespachos.
	 */
	public boolean tieneDespachos() {
		return tieneDespachos;
	}
	/**
	 * @return Retorna el estadoFacturacion.
	 */
	public boolean estadoFacturacionPendiente() {
		return estadoFacturacionPendiente;
	}
	/**
	 * @return Retorna el estadoSolicitada.
	 */
	public boolean estadoSolicitada() {
		return estadoSolicitada;
	}
	/**
	 * @return Retorna el puedoAnularModificarSolicitudMedicamentos.
	 */
	public ResultadoBoolean puedoAnularModificarSolicitudMedicamentos() {
		return puedoAnularModificarSolicitudMedicamentos;
	}
	
	/**
	 * Método para obtener la institucion en el momento de hacer la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @param esEvolucion
	 * @return
	 */
	public static String[] obtenerInstitucion(Connection con, int numeroSolicitud, boolean esEvolucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesSolicitudDao().obtenerInstitucion(con, numeroSolicitud, esEvolucion);
	}
	
	/**
	 * Método para verificar si el médico puede o no hacer solicitud de procedimientos
	 * para un paciente con vía de ingreso "Consulta Externa"
	 * @param con
	 * @param codigoCuenta
	 * @param codigoMedico
	 * @return true si puede solicitar, false de lo contrario
	 */
	public static boolean tieneCitasAtendidas(Connection con, int codigoCuenta, int codigoMedico)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getValidacionesSolicitudDao().tieneCitasAtendidas(con, codigoCuenta, codigoMedico);
	}
	
	/**
	 * @return Retorna puedoSuspenderSolicitudMedicamentos.
	 */
	public ResultadoBoolean puedoSuspenderSolicitudMedicamentos()
	{
		return puedoSuspenderSolicitudMedicamentos;
	}

	
}
