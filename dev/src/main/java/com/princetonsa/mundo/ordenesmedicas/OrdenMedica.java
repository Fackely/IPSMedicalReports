/*
 * Creado en May 31, 2005
 */
package com.princetonsa.mundo.ordenesmedicas;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.inventarios.UtilidadInventarios;

import com.princetonsa.actionform.ordenesmedicas.OrdenMedicaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.OrdenMedicaDao;
import com.princetonsa.dao.enfermeria.RegistroEnfermeriaDao;
import com.princetonsa.dto.comun.DtoResultado;
import com.princetonsa.dto.enfermeria.DtoRegistroAlertaOrdenesMedicas;
import com.princetonsa.dto.manejoPaciente.DtoObservacionesGeneralesOrdenesMedicas;
import com.princetonsa.dto.ordenesmedicas.DtoPrescripcionDialisis;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.UtilidadJustificacionPendienteArtServ;
import com.princetonsa.mundo.enfermeria.ProgramacionCuidadoEnfer;
import com.princetonsa.mundo.inventarios.FormatoJustArtNopos;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public class OrdenMedica  
{
	
/**
 * Para hacer logs de esta funcionalidad.
*/
	private static Logger logger = Logger.getLogger(OrdenMedica.class);	

//	 SECCION DE INFORMACION GENERAL
    
/**
 * Código de la orden médica
 */ 
private int codigoOrden;	
	
	/**
  * Fecha de grabacion de la orden
  */
	private String fechaGrabacion;
	
/**
 * Hora de grabación de la orden
 */
	private String horaGrabacion;
	
/**
 * Fecha de admisión del paciente
 */
	private String fechaAdmision;
	
/**
 * Hora de admisión del paciente
 */
	private String horaAdmision;
	
  /**
  * Fecha de ingreso de la orden
  */
   private String fechaOrden;
  
  /**
   * Hora de ingreso de la orden 
   */
   private String horaOrden;
  
  //SECCION TIPO DE MONITOREO
  
  /**
   * Tipo de monitoreo
   */
  private int tipoMonitoreo;
  
  
  //SECCION DE SOPORTE RESPIRATORIO
  
  /**
   * Cantidad de litros de oxígeno por minuto solicitados por el médico
   */
   private float cantidadSoporteRespiratorio;
  
  /**
   * Equipo usado en la oxigenoterapia
   */
   private int equipoElemento;
  
    /**
   * Campo descripción para ingresar observaciones adicionales a la oxígeno-terapia.
   */
   private String descripcionSoporteRespiratorio;
   
   /**
    * Campo para guardar la descripción del soporte respiratorio de la cuenta de urgencias
    * cuando se realiza el asocio de cuentas, y de esta forma poder mostrar esta información en el formulario
    */
   private String descripcionSoporteUrgencias;
  
  /**
   * Campo para indicar si se finaliza el soporte respiratorio
   */
   private boolean finalizarSoporte;
   
   /**
    * Campo que guarda si tiene o no oxígeno terapia
    */
   private String oxigenoTerapia;
  
   /**
    * Campo que guarda el codigo del encabezado que contiene el ultimo soporte respiratorio 
    */
   private int codigoEncabezadoSoporteRespira;
   
   /**
    * Campo que guarda la descripcion soporte respiratorio a modificar 
    */
   private String descripcionIndivSoporteRespira = "";
  
  //--------------------SECCION DIETA
  
   /**
    * Campo de chequeo para determinar si el paciente tiene nutrición oral
    */
   
   private boolean nutricionOral;
   
   /**
    * Campo de chequeo para determinar si el paciente tiene nutrición parenteral
    */
   
   private boolean nutricionParenteral;
   
   
   /**
    * Campo Volumen total de las cantidades de nutricion parenteral 
    */
   private String volumenTotal;
   
   /**
    * 
    * */
   private String unidadVolumenTotal;
   
   /**
    * Campo Velocidad infusion  
    */
   private String velocidadInfusion; 

   /**
    * Mezcla de los articulos
    */
   private int mezcla;
   
   /**
   * Campo descripción para ingresar observaciones adicionales a la dieta del paciente
   */
  private String descripcionDieta;
  
  private String descripcionDietaParenteral;
  
  /**
   * Campo para guardar la descripción dieta de la cuenta de urgencias
   * cuando se realiza el asocio de cuentas, y de esta forma poder mostrar esta información en el formulario
   */
  private String descripcionDietaUrgencias;
  
 
  /**
   * Campo para seleccionar el centro de costo que se desea depache la nutrición parenteral
   */
  private int farmacia;
  
  /**
   * Campo para indicar que se finaliza la dieta
   */
  private boolean finalizarDieta;
  
  
  /**
   * Campo para indicar que se finaliza la dieta
   */
  private boolean finalizarDietaEnfermeria;
  
  
  /**
   * Coleccion para traer el Listado de tipos de nutricion Oral   
   */
  private Collection listadoNutOral;
  
  /**
   * Campo que guarda las nutriciones orales y parenterales
   */
  
  private HashMap mapa;
  
  /**
   * Campo para guardar algun otro tipo de nutricion Oral
   */
  
  private String otroNutORal;
  
  /**
   * Vector con los códigos de tipo nutrición oral
   */
  private Vector codigosNutOral;
  
  /**
   * Vector con los códigos de otros tipos de nutrición oral
   */
  private Vector codigosOtroNutOral;
  
   
  //------------------SECCION DE CUIDADOS DE ENFERMERIA
  
  /**
   * Campo para guardar los cuidades de enfermería del paciente
   */
  private ArrayList cuidadosEnfermeria;
  
  private String OtroCuidadoEnf;
  
  //SECCION OBSERVACIONES GENERALES
    
  /**
   * Campo para registrar las observaciones generales de la orden médica. 
   */
  private String observacionesGenerales;
  
  /**
   * Campo para guardar las observaciones generales de la cuenta de urgencias
   * cuando se realiza el asocio de cuentas, y de esta forma poder mostrar esta información en el formulario
   */
  private String observacionesGralesUrgencias;
  
  /**
   * Vector que guarda las descripciones de los tipos de nutrición oral que no está parametrizados
   * en hospitalización
   */
  private Vector otrasNutricionesOrales;
  
  /**
	* Interfaz para acceder la fuente de datos
	*/
	private OrdenMedicaDao ordenMedicaDao = null;
	
	  //------------------------------------------------------ ORDEN DE LA HOJA NEUROLÓGICA ---------------------------------------------//
    /**
     * Campo para guardar si presenta hoja neurológica
     */
    private String presentaHojaNeuro;
    
    /**
     * Campo para guardar las observaciones en la orden
     * de la hoja neurológica
     */
    private String observacionesHojaNeuro;
    
    /**
     * Campo para indicar si se finaliza la orden de la
     * hoja neurológica
     */
    private String finalizadaHojaNeuro;
  
    /**
     * 
     */
  	private String dosificacion;
  	
	/**
	 * Constructor de la clase, inicializa en vacio todos los parámetros
	 */		
	public OrdenMedica ()
	{
		reset();
		this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * Este método inicializa en valores vacíos, -mas no nulos- los atributos de este objeto.
	 */
	public void reset() 
	{
		this.codigoOrden=0;
		this.fechaGrabacion="";
		this.horaGrabacion="";
		this.fechaAdmision="";
		this.horaAdmision="";
		this.fechaOrden="";
		this.horaOrden="";
		this.tipoMonitoreo=0;
		this.cantidadSoporteRespiratorio=0;
		this.equipoElemento=0;
		this.descripcionSoporteRespiratorio="";
		this.finalizarSoporte=false;
		this.oxigenoTerapia="";
		this.descripcionDieta="";
		this.farmacia=0;
		this.mapa=new HashMap();
		this.finalizarDieta=false;
		this.finalizarDietaEnfermeria=false;
		this.cuidadosEnfermeria=new ArrayList();
		this.observacionesGenerales="";		
		this.otroNutORal = "";
		this.codigosNutOral=new Vector();
		this.codigosOtroNutOral = new Vector();
		this.observacionesGralesUrgencias = "";
		this.descripcionSoporteUrgencias = "";
		this.descripcionDietaUrgencias = "";
		this.otrasNutricionesOrales=new Vector();
		
		//----------Orden Hoja Neurológica -----------//
		this.presentaHojaNeuro="";
		this.observacionesHojaNeuro="";
		this.finalizadaHojaNeuro="";
		this.dosificacion="";

		volumenTotal = "";
		this.unidadVolumenTotal = "";
	    velocidadInfusion = "0"; 

	    OtroCuidadoEnf = "";
	    
	    this.codigoEncabezadoSoporteRespira = 0;
	    this.descripcionIndivSoporteRespira = "";
	}
	
	public String getDosificacion() {
		return dosificacion;
	}

	public void setDosificacion(String dosificacion) {
		this.dosificacion = dosificacion;
	}

	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean  init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);

		if (myFactory != null)
		{
			ordenMedicaDao = myFactory.getOrdenMedicaDao();
			wasInited = (ordenMedicaDao != null);
		}
		return wasInited;
	}
	
	/**
	 * Método para obtener el dao de ordenes medicas de forma estática
	 * @return
	 */
	public static OrdenMedicaDao ordenMedicaDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOrdenMedicaDao();
	}

	/**
	 * @param con
	 * @param codHistoricosParent
	 */
	public int finalizarParenteral(Connection con, Vector codHistoricosParent)
	{
		return ordenMedicaDao.finalizarParenteral(con, codHistoricosParent);
	}

	
	/**
	 * Método para insertar una orden médica
	 * @param con una conexion abierta con una fuente de datos
	 * @param cuenta
	 * @param login
	 * @param datosMedica
	 * @return número de filas insertadas (1 o 0)
	 * @throws SQLException
	 */	
	public DtoResultado insertarOrdenMedica (Connection con, int cuenta,boolean manejarTransaccion,Boolean tieneDatos) throws SQLException 
	{
		if (ordenMedicaDao==null)		
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (OrdenMedicaDao - insertarOrdenMedica )");
				
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans=false;		
		if(manejarTransaccion)
			inicioTrans=UtilidadBD.iniciarTransaccion(con);
		DtoResultado respuesta=ordenMedicaDao.insertarOrdenMedica(
				con,
				cuenta,
				this.descripcionSoporteRespiratorio,
				this.descripcionDieta,
				this.observacionesGenerales, 
				this.descripcionDietaParenteral,tieneDatos);
		
		if(manejarTransaccion)
		{
			if (!inicioTrans|| respuesta==null){
			   UtilidadBD.abortarTransaccion(con);
			}
			else{			
			    UtilidadBD.finalizarTransaccion(con);			
		}
		}
		
		return respuesta;
	}
		
	/**
	 * Método para asociar el encabezado historico de la orden
	 * con el detalle de la observación de la orden para poder manejar
	 * las observaciones cronológicamente
	 * 
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoPkDetalleObservacion
	 * @param codigoPkEncabezado
	 * @throws SQLException
	 */	
	public void asociarDetalleObservacionEncabezado (Connection con, int codigoPkDetalleObservacion, int codigoPkEncabezado) throws SQLException 
	{
		if (ordenMedicaDao==null)		
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (OrdenMedicaDao - insertarOrdenMedica )");
				
		ordenMedicaDao.asociarDetalleObservacionEncabezado(con, codigoPkDetalleObservacion, codigoPkEncabezado);
	}
	
	/**
	 * Metodo para insertar el encabezado historico de todas las ordenes (nutricion--soporte-monitoreo)  
	 * @param con
	 * @param codigoOrden2
	 * @param login
	 * @param datosMedico
	 * @return
	 * @throws SQLException
	 */
	public int insertarEncabezadoOrdenMedica(
			Connection con,
			int codigoOrden,
			String login,
			String datosMedico,
			boolean manejarTransaccion) throws SQLException
	{

		int  resp1=0;			
		if (ordenMedicaDao==null)		
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (OrdenMedicaDao - insertarEncabezadoOrdenMedica )");
		
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans=false;
		
		if(manejarTransaccion)
			inicioTrans=UtilidadBD.iniciarTransaccion(con);
			
		resp1=ordenMedicaDao.insertarEncabezadoOrdenMedica(
				con,
				codigoOrden,
				this.fechaOrden,
				this.horaOrden,
				login,
				datosMedico);		
		
		if(manejarTransaccion)
		{
			if (!inicioTrans||resp1<1)
			{
			    UtilidadBD.abortarTransaccion(con);
				return -1;
			}
			else
			{
			    UtilidadBD.finalizarTransaccion(con);
			}
		}
		
		return resp1;
	}
	
	/**
	 * Metodo para insertar el encabezado historico de todas las ordenes (nutricion--soporte-monitoreo)  
	 * @param con
	 * @param codigoOrden2
	 * @param login
	 * @param datosMedico
	 * @return
	 * @throws SQLException
	 */
	public int insertarEncabezadoOrdenHojaNeurologica(
			Connection con,
			int codigoOrden,
			String login,
			String datosMedico,
			boolean manejarTransaccion) throws SQLException
	{

		int  resp2;			
		if (ordenMedicaDao==null)		
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (OrdenMedicaDao - insertarEncabezadoOrdenMedica )");
		
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans=false;
		
		if(manejarTransaccion)
			inicioTrans=UtilidadBD.iniciarTransaccion(con);
		
		//-------Se inserta o modifica la orden de la Hoja Neurológica ------------//
		resp2 = ordenMedicaDao.insertarModificarOrdenHojaNeurologica(
				con,
				codigoOrden,
				UtilidadTexto.getBoolean(this.presentaHojaNeuro),
				this.observacionesHojaNeuro,
				UtilidadTexto.getBoolean(this.finalizadaHojaNeuro),
				this.fechaOrden,
				this.horaOrden,
				login);
		
		if(manejarTransaccion)
		{
			if (!inicioTrans||resp2<1)
			{
			    UtilidadBD.abortarTransaccion(con);
				return -1;
			}
			else		
			    UtilidadBD.finalizarTransaccion(con);			
		}
		return resp2;
	}

	
	/**
	 * Método para insertar el tipo de monitoreo a una orden médica
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoOrden
	 * @param login
	 * @param datosMedico
	 * @return número de filas insertadas (1 o 0)
	 * @throws SQLException
	 */
	public int insertarOrdenTipoMonitoreo (Connection con, int codigoEncabezado,boolean manejarTransaccion) throws SQLException 
	{

		int  resp1=0;
			
		if (ordenMedicaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (OrdenMedicaDao - insertarOrdenMedica )");
		}
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans=false;
		
		if(manejarTransaccion)
		{
			inicioTrans=UtilidadBD.iniciarTransaccion(con);
		}

		resp1=ordenMedicaDao.insertarOrdenTipoMonitoreo (con, codigoEncabezado, this.tipoMonitoreo);
	
		if(manejarTransaccion)
		{
			if (!inicioTrans||resp1<1  )
			{
				 UtilidadBD.abortarTransaccion(con);
				return -1;
			}
			else
			{
				UtilidadBD.finalizarTransaccion(con);
			}
		}
		return resp1;
	}

	/**
	 * Metodo enargado de actualizar la descripcion del sopoorte respiratorio
	 * @param con
	 * @return
	 * @author hermorhu
	 */
	public boolean actualizarDescripcionSoporteRespiratorio (Connection con) {
		
		
		ordenMedicaDao.actualizarDescripcionSoporteRespiratorio(con, codigoEncabezadoSoporteRespira, descripcionIndivSoporteRespira);
		
		return true;
	}
	
	/**
	 * Método para insertar el soporte respiratorio  a una orden médica
	 * @param con una conexion abierta con una fuente de datos
	 * @param codigoOrden
	 * @param login
	 * @param datosMedico
	 * @return número de filas insertadas (1 o 0)
	 * @throws SQLException
	 */
	public int insertarOrdenSoporteRespiratorio (Connection con, int codigoEncabezado,boolean manejarTransaccion) throws SQLException 
	{

		int  resp1=0;
			
		if (ordenMedicaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (OrdenMedicaDao - insertarOrdenSoporteRespiratorio )");
		}
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans=false;
		
		if(manejarTransaccion)
			inicioTrans=UtilidadBD.iniciarTransaccion(con);
		
		resp1=ordenMedicaDao.insertarOrdenSoporteRespiratorio (	con, codigoEncabezado, this.equipoElemento, this.cantidadSoporteRespiratorio, this.oxigenoTerapia, descripcionIndivSoporteRespira);
	
		if(manejarTransaccion)
		{
			if (!inicioTrans||resp1<1  )
			{
			    UtilidadBD.abortarTransaccion(con);
				return -1;
			}
			else
			{
			    UtilidadBD.finalizarTransaccion(con);
			}
		}
		return resp1;
	}

	

	/**
	 * Metodo para Insertar los cuidados de emfermeria seleccionados (Detalle)
	 *   
	 * @param con
	 * @param codigoEncabezado
	 * @return
	 * @throws SQLException
	 */

	public int insertarDetalleOrdenCuidadoEnf(
			Connection con, 
			int codigoEncabezado,
			boolean manejarTransaccion, 
			int codigoIngreso,
			String usuarioModifica) throws SQLException
	{

		int  resp1=0;
		int  codTipoCuidado=0;
			
		if (ordenMedicaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (OrdenMedicaDao - insertarOrdenCuidadoEnf )");
		}
		//------Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans=false;
		
		if(manejarTransaccion)
			inicioTrans=UtilidadBD.iniciarTransaccion(con);
		
		//-------Barrer el HashMap para insertar los datos seleccionados   

		Vector codigosCuidadoEnf =(Vector) this.getMapa("codigosCuidadoEnf");
		Vector tiposCuidadoEnf =(Vector) this.getMapa("tiposCuidadoEnf");

		logger.info("\n====>Por aca");
		if (codigosCuidadoEnf != null && tiposCuidadoEnf != null)
		{
			logger.info("\n====>Por aca1");
			logger.info("\n====>Cuidado Enfermeria: "+codigosCuidadoEnf.size());
			for(int i=0; i<codigosCuidadoEnf.size();i++)
			{
				logger.info("\n====>Por aca2");
				//------El codigo del tipo de cuidado de enfermeria 
				int tipoCuidado = Utilidades.convertirAEntero(codigosCuidadoEnf.elementAt(i)+"");
				
				//------El tipo de cuidado de enfermeria 0->Parametrizado   1->Otro cuidado
				int tipoCuidadoEnfer = Utilidades.convertirAEntero(tiposCuidadoEnf.elementAt(i)+"");
				
				//------Extraer  
				String presenta = (String)this.getMapa("presenta_"+tipoCuidado);
				
				//-Verificar que se haya seleccionado el tipo de cuidado
				String descripcion = (String)this.getMapa("cuidadosEnfDes_"+tipoCuidado);
								
				//-Verificar que se haya seleccionado el tipo de cuidado
				if (UtilidadCadena.noEsVacio(presenta) || (UtilidadCadena.noEsVacio(descripcion)) )
				{
					if(UtilidadTexto.getBoolean(this.getMapa("indicativoControlEspecial_"+tipoCuidado)+""))
					{
						descripcion+="\n"+usuarioModifica;
					}
					
					
					logger.info("\n====>Por aca3");
					if(tipoCuidadoEnfer == 0)
					{				
						resp1=ordenMedicaDao.insertarDetalleOrdenCuidadoEnf(con, codigoEncabezado, tipoCuidado, presenta, descripcion, -1);
					}
					else
					{					
						resp1=ordenMedicaDao.insertarDetalleOrdenCuidadoEnf(con, codigoEncabezado, -1, presenta, descripcion, tipoCuidado);
					}
							
				}	
			}
		}//if codigosCuidadoEnf != null
				
		//logger.info("\n\n\n se inserta otro valor >> "+this.OtroCuidadoEnf+" >> "+UtilidadTexto.getBoolean((String)this.getMapa("presentaOtro"))+" >> "+Utilidades.convertirAEntero(this.getMapa("frecuencia_otroCuidadoEnf").toString())+" >> "+Utilidades.convertirAEntero(this.getMapa("periodo_otroCuidadoEnf").toString()));
		
		//--------Tratar de insertar Otro Detalle cuidados enfermeria 
		String presenta = (String)this.getMapa("presentaOtro");	
		if (UtilidadCadena.noEsVacio(presenta) 
				&& UtilidadCadena.noEsVacio(this.OtroCuidadoEnf) 
					&& UtilidadTexto.getBoolean(presenta))
		{			
			String descripcion = (String)this.getMapa("descripcionOtro");
			codTipoCuidado=insertarOtroTipoCuidadoEnf(con);
			
			if(codTipoCuidado != -1 && codTipoCuidado != 0)
				resp1=ordenMedicaDao.insertarDetalleOrdenCuidadoEnf(con, codigoEncabezado, -1, presenta, descripcion, codTipoCuidado);
			
			//Evalua si se inserta el otro registro con frecuencia y periodo
			if(Utilidades.convertirAEntero(this.getMapa("frecuencia_otroCuidadoEnf").toString()) > 0 
					|| Utilidades.convertirAEntero(this.getMapa("periodo_otroCuidadoEnf").toString()) > 0)
			{
				if(codTipoCuidado > 0						 
						&& ProgramacionCuidadoEnfer.insertarFrecuenciasCuidados(
							con,
							codigoIngreso,
							ConstantesBD.codigoNuncaValido,
							codTipoCuidado,
							Utilidades.convertirAEntero(this.getMapa("frecuencia_otroCuidadoEnf").toString()),
							Utilidades.convertirAEntero(this.getMapa("tipofrecuencia_otroCuidadoEnf").toString()),
							true,
							Utilidades.convertirAEntero(this.getMapa("periodo_otroCuidadoEnf").toString()),
							Utilidades.convertirAEntero(this.getMapa("tipoperiodo_otroCuidadoEnf").toString()),
							usuarioModifica)<0)
						resp1 = ConstantesBD.codigoNuncaValido;
					else
						resp1 = 1;
			}
		}
		else if(UtilidadCadena.noEsVacio(this.OtroCuidadoEnf))
		{
			//Se ingresa un nuevo registro de la frecuencia y periodo del nuevo cuidado
			if(Utilidades.convertirAEntero(this.getMapa("frecuencia_otroCuidadoEnf").toString()) > 0 
					|| Utilidades.convertirAEntero(this.getMapa("periodo_otroCuidadoEnf").toString()) > 0)
			{				
				codTipoCuidado=insertarOtroTipoCuidadoEnf(con);
				
				if(codTipoCuidado != -1 && codTipoCuidado != 0)
					resp1=ordenMedicaDao.insertarDetalleOrdenCuidadoEnf(
							con, 
							codigoEncabezado,
							-1,
							"",
							"",
							codTipoCuidado);
								
				if(codTipoCuidado > 0						 
					&& ProgramacionCuidadoEnfer.insertarFrecuenciasCuidados(
						con,
						codigoIngreso,
						ConstantesBD.codigoNuncaValido,
						codTipoCuidado,
						Utilidades.convertirAEntero(this.getMapa("frecuencia_otroCuidadoEnf").toString()),
						Utilidades.convertirAEntero(this.getMapa("tipofrecuencia_otroCuidadoEnf").toString()),
						true,
						Utilidades.convertirAEntero(this.getMapa("periodo_otroCuidadoEnf").toString()),
						Utilidades.convertirAEntero(this.getMapa("tipoperiodo_otroCuidadoEnf").toString()),
						usuarioModifica)<0)
					resp1 = ConstantesBD.codigoNuncaValido;
				else
					resp1 = 1;			
			}					
		}
		
		if(manejarTransaccion)
		{
			if (!inicioTrans||resp1<1)
			{
			    UtilidadBD.abortarTransaccion(con);
				return -1;
			}
			else
			{
			    UtilidadBD.finalizarTransaccion(con);
			}
		}
		
		return resp1;	 	
	}
	
	/**
	 * Metodo encargado de suspender una mezcla, esta mezcla puede ser suspendida desde ordenes de medicamentos,
	 * para suspender una mezcla se debe poner el el campo finaliza_sol en true.
	 * @param connection
	 * @param supender
	 * @param codigoHistoEnca
	 * @return
	 */
	public boolean suspenderMezcla (Connection connection, String supender,int codigoHistoEnca,String usuario)
	{
		return ordenMedicaDao.suspenderMezcla(connection, supender, codigoHistoEnca,usuario);
	}
	
	/**
	 * Metodo encargado de Finalizar una Mezcla, esto se hace desde el registro de enfermeria.
	 * Se actualiza el campo que se llama suspendido en true, y se llenan los campos de fecha, hora y usuario
	 * finaliza
	 * @param connection
	 * @param finalizar
	 * @param codigoHistoEnca
	 * @param usuario
	 * @return
	 */
	public boolean finalizarMezcla (Connection connection,String finalizar,int codigoHistoEnca,String usuario )
	{
		return ordenMedicaDao.finalizarMezcla(connection, finalizar, codigoHistoEnca, usuario);
	}

	/**
	 * Metodo para insertar los tipo de nutricion (oral y parenteral)
	 * 
	 * @param con -> Conexion
	 * @param codigoEncabezado -> int (es el encabezado de la orden de la dieta)
	 * @param resp -> Indica si se inserta nutrición oral y nutrición parenteral
	 * @param paciente 
	 * @param usuario 
	 * @throws SQLException
	 */	
	public int insertarNutricionParental(
			Connection con, 
			int codigoEncabezado,
			int resp,
			UsuarioBasico usuario,
			PersonaBasica paciente,
			OrdenMedicaForm ordenMedicaForm,
			boolean manejarTransaccion) throws SQLException, IPSException
	{
		int resp1=0;
		int codOtro=0;
		int resp3=0;
		String esMedicamento = "";
		
		if (ordenMedicaDao==null)
		{
			throw new SQLException ("\n No Insertar los Tipos de Nutricion ");
		}
		
		//------Iniciamos la transacción
		boolean inicioTrans=false;
		
		if(manejarTransaccion)
			inicioTrans=UtilidadBD.iniciarTransaccion(con);
		else
			inicioTrans=true;
		
		if (resp == 2 || resp==3)
		{
			if(this.getMapa("codigosParent") != null)
			{
				Vector codigosParent =(Vector) this.getMapa("codigosParent");
				for(int i=0; i<codigosParent.size();i++)
				{
					int tipoParent = Utilidades.convertirAEntero(codigosParent.elementAt(i)+"");
					String volumen = (String)this.getMapa("tipoParent_"+tipoParent);
						
					String unidadVolumen = "";						
					if(this.getMapaCompleto().containsKey("unidad_volumen_"+tipoParent))
						unidadVolumen = (String)this.getMapa("unidad_volumen_"+tipoParent);
						
					if(UtilidadCadena.noEsVacio(volumen))
					{
						if(this.getMapaCompleto().containsKey("espos_"+i) 
								&& this.getMapa("espos_"+i).toString().equals("false")
									&& ordenMedicaForm.getJustificacionMap().containsKey(tipoParent+"_pendiente")
										&& !UtilidadesHistoriaClinica.validarEspecialidadProfesionalSalud(con, usuario, true))
						{
							logger.info("\n***********************************************************************\n" +
										"*[No se genera justificacion segun validacion profesional de la salud]*\n" +
										"***********************************************************************\n");
						}
						else
						{
							esMedicamento = ConstantesBD.acronimoSi;
							if(this.getMapa().containsKey("esmedicamento_"+tipoParent) 
									&& this.getMapa("esmedicamento_"+tipoParent).toString().equals(ConstantesBD.acronimoNo))
								esMedicamento = ConstantesBD.acronimoNo;
								
							logger.info("\n\n\n\n [ENTRO A INSERTAR SOLICITUDES_MEDICAMENTOS ARTICULO] <<<<"+tipoParent+">>>>");
							//--------El 2 como parametro es para enviar al insertar en la tabla de detalle de nutricion Parentreral  (orden_nutricion_parente) 
							resp1=ordenMedicaDao.insertarNutricion(
									con,
									codigoEncabezado,
									tipoParent,
									Float.parseFloat(volumen),
									unidadVolumen,
									2,
									usuario,paciente,
									esMedicamento);
						}
							
						if(manejarTransaccion)
						{
							if (!inicioTrans||resp1<1)
							{
							    UtilidadBD.abortarTransaccion(con);
								return -1;
							}
							else
							{
							    UtilidadBD.finalizarTransaccion(con);
							}
						}
					}	//if
					else
					{
						//en caso de que la solicitud quede en pendiente de completar.
						resp1=1;
					}
				}//for
			}//if nutricionParenteral=true	

			//-----rutinas para pasar la información de los medicamentos insertados desde el jsp
			//Insercion de datos de otros exámenes de laboratorio
			String codigosOtrosExam=this.getMapa("codigosOtros")+"";
			if (codigosOtrosExam!=null   && !codigosOtrosExam.equals(""))
			{
				String[] codOtrosExamenes=codigosOtrosExam.split("-");
				
	
				for (int z=0; z < codOtrosExamenes.length; z++)
				{
					String volumen = (String)this.getMapa("tipoParent_"+codOtrosExamenes[z]);
					
					String unidadVolumen = "";						
					if(this.getMapaCompleto().containsKey("unidad_volumen_"+codOtrosExamenes[z]))
						unidadVolumen = (String)this.getMapa("unidad_volumen_"+codOtrosExamenes[z]);
					
					if(UtilidadCadena.noEsVacio(volumen))
					{	
						if(this.getMapaCompleto().containsKey("espos_"+z)
								&& this.getMapa("espos_"+z).toString().equals("false")
								&& ordenMedicaForm.getJustificacionMap().containsKey(Utilidades.convertirAEntero(codOtrosExamenes[z])+"_pendiente")
								&& !UtilidadesHistoriaClinica.validarEspecialidadProfesionalSalud(con, usuario, true)
								)
						{
						logger.info("\n***********************************************************************\n" +
									"*[No se genera justificacion segun validacion profesional de la salud]*\n" +
									"***********************************************************************\n");
						}else
						{
						logger.info("\n\n\n\n [ENTRO A INSERTAR SOLICITUDES_MEDICAMENTOS ARTICULO] <<<<"+Utilidades.convertirAEntero(codOtrosExamenes[z])+">>>>");
						
						esMedicamento = ConstantesBD.acronimoSi;
						if(this.getMapa().containsKey("esmedicamento_"+codOtrosExamenes[z]) 
								&& this.getMapa("esmedicamento_"+codOtrosExamenes[z]).toString().equals(ConstantesBD.acronimoNo))
							esMedicamento = ConstantesBD.acronimoNo;
						
						
						resp1=ordenMedicaDao.insertarNutricion(
								con,
								codigoEncabezado, 
								Utilidades.convertirAEntero(codOtrosExamenes[z]),
								Float.parseFloat(volumen),
								unidadVolumen,
								2,
								usuario,paciente,
								esMedicamento);
						}
						
						if(manejarTransaccion)
						{
							if (!inicioTrans||resp1<1)
							{
							    UtilidadBD.abortarTransaccion(con);
								return -1;
							}
							else
							{
							    UtilidadBD.finalizarTransaccion(con);
							}
						}
					}	
				}//for
			}//if		
		}//if resp=2 o resp=3
		return resp1;
	}
	
	/**
	 * Metodo para insertar los tipo de nutricion (oral y parenteral)
	 * 
	 * @param con -> Conexion
	 * @param codigoEncabezado -> int (es el encabezado de la orden de la dieta)
	 * @param resp -> Indica si se inserta nutrición oral y nutrición parenteral
	 * @param paciente 
	 * @param usuario 
	 * @throws SQLException
	 */	
	public int insertarNutricionOral(
			Connection con, 
			int codigoEncabezado,
			int resp,
			UsuarioBasico usuario,
			PersonaBasica paciente,
			OrdenMedicaForm ordenMedicaForm,
			boolean manejarTransaccion) throws SQLException, IPSException
	{
		int resp1=0;
		int codOtro=0;
		int resp3=0;		
		
		if (ordenMedicaDao==null)
		{
			throw new SQLException ("\n No Insertar los Tipos de Nutricion ");
		}
		
		//------Iniciamos la transacción
		boolean inicioTrans=false;
		
		if(manejarTransaccion)
			inicioTrans=UtilidadBD.iniciarTransaccion(con);
		else
			inicioTrans=true;
		
		if(resp==1 || resp==3)
		{
			if (this.nutricionOral)
			{
				String tempOtroOral=this.getOtroNutORal();
				/*--------Se miran los nuevos tipos nutrición oral que no están parametrizados en hospitalización para insertarlos como otros tipos
			     ---------de nutrición oral*/
				
				if(this.otrasNutricionesOrales!=null && !this.otrasNutricionesOrales.isEmpty())
				{
					for(int x=0; x<otrasNutricionesOrales.size(); x++)
					{
						this.setOtroNutORal(otrasNutricionesOrales.elementAt(x).toString());
						codOtro=insertarOtroNutricionOral(con);
					}
					
					this.setOtrasNutricionesOrales(new Vector());	
				}
				
				if(this.getMapa("codigosNutOral") != null)
				{
					//------Insertar detalle orden nutricion Oral 
					Vector codigos=(Vector) this.getMapa("codigosNutOral");
					for(int i=0; i<codigos.size();i++)
						{
							int tipoNut=Utilidades.convertirAEntero(codigos.elementAt(i)+"");
							
							if(this.getMapa("tipoNut_"+tipoNut) != null)
							{
								String tipoNutricion= (String)this.getMapa("tipoNut_"+tipoNut);
								if(UtilidadCadena.noEsVacio(tipoNutricion))
								{							
									//--------El 1 como parametro es para enviar al insertar en la tabla de detalle de nutricion Oral (orden_nutricion_oral)
									if(tipoNutricion!=null && !tipoNutricion.equals("0"))
									{ 
										if(this.getMapaCompleto().containsKey("espos_"+i) 
												&& this.getMapa("espos_"+i).toString().equals("false") 
												&& ordenMedicaForm.getJustificacionMap().containsKey(tipoNutricion+"_pendiente")
												&& !UtilidadesHistoriaClinica.validarEspecialidadProfesionalSalud(con, usuario, true))
										{
										logger.info("\n***********************************************************************\n" +
													"*[No se genera justificacion segun validacion profesional de la salud]*\n" +
													"***********************************************************************\n");
										}else
										{											
											logger.info("\n\n\n\n [ENTRO A INSERTAR SOLICITUDES_MEDICAMENTOS ARTICULO] <<<<"+tipoNutricion+">>>>");
											
											resp1=ordenMedicaDao.insertarNutricion(
													con,
													codigoEncabezado, 
													Utilidades.convertirAEntero(tipoNutricion),
													-1,
													"",
													1,
													usuario,paciente,
													"");
										}
										
										if(manejarTransaccion)
										{
											if (!inicioTrans||resp1<1)
											{
											    UtilidadBD.abortarTransaccion(con);
												return -1;
											}
											else
											{
											    UtilidadBD.finalizarTransaccion(con);
											}
										}
									}			
								}
							}	//if
						}//for
				}	//if codigosNutOral != null
				
				//------------------------------INSERCIÓN DE LOS OTROS TIPOS DE NUTRICIÓN ORAL---------------------------------------------------------------------//
				if(this.getMapa("codigosOtroNutOral") != null)
				{
					//------Insertar detalle de otra nutrición Oral 
					Vector codigosOtro=(Vector) this.getMapa("codigosOtroNutOral");
					for(int i=0; i<codigosOtro.size();i++)
						{
							int tipoNutOtro=Utilidades.convertirAEntero(codigosOtro.elementAt(i)+"");
							
							if(this.getMapa("tipoNutOtro_"+tipoNutOtro) != null)
							{
								String tipoNutricionOtro= (String)this.getMapa("tipoNutOtro_"+tipoNutOtro);
								if(UtilidadCadena.noEsVacio(tipoNutricionOtro))
								{							
									if(tipoNutricionOtro!=null && !tipoNutricionOtro.equals("0"))
									{ 
										resp3=ordenMedicaDao.insertarDetalleOtroNutriOral(con, codigoEncabezado, Utilidades.convertirAEntero(tipoNutricionOtro));
										if(manejarTransaccion)
										{
											if (!inicioTrans||resp3<1)
											{
											    UtilidadBD.abortarTransaccion(con);
												return -1;
											}
											else
											{
											    UtilidadBD.finalizarTransaccion(con);
											}
										}
									}			
								}
							}	//if
						}//for
				}	//if codigosOtroNutOral != null
				
				//------Si otra nutrición oral es diferente de vació se inserta---------//
				this.setOtroNutORal(tempOtroOral);
				if(!this.otroNutORal.equals(""))
				{
					codOtro=insertarOtroNutricionOral(con);
					
					insertarDetalleOtroNutriOral(con, codigoEncabezado, codOtro,manejarTransaccion);
				}
			}//if nutricionOral
		}//if resp=1 o resp3
		return resp1;
	}

	
	/**
	 * Metodo para insertar la informacion en la tabla Orden Mezcla
	 * @param con
	 * @param numeroSolicitud @todo
	 * @param codigoOrden
	 * @param cuenta
	 * @param login
	 * @param datosMedico
	 * @return
	 * @throws SQLException
	 */
	public int insertarOrdenMezcla(
			Connection con, 
			int codigoEncabezado,
			int numeroSolicitud,
			String codigoInstitucion,
			boolean manejarTransaccion) throws SQLException
	{
		
		int  resp1=0;
			
		if (ordenMedicaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (OrdenMedicaDao - insertarOrdenDieta )");
		}
		
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans=false;		
		if(manejarTransaccion)
			inicioTrans=UtilidadBD.iniciarTransaccion(con);
		
		resp1=ordenMedicaDao.insertarOrdenDieta(
				con,
				codigoEncabezado,
				this.volumenTotal,
				this.unidadVolumenTotal,
				this.velocidadInfusion,
				this.farmacia,
				this.nutricionOral, 
				this.nutricionParenteral, 
				this.finalizarDieta,
				this.mezcla,
				numeroSolicitud,
				codigoInstitucion,
				this.dosificacion);
		
		//logger.info("valor resp1: "+resp1);
		
		if(manejarTransaccion)
		{
			if (!inicioTrans||resp1<1)
			{
			    UtilidadBD.abortarTransaccion(con);
				return -1;
			}
			else
			{
			    UtilidadBD.finalizarTransaccion(con);
			}
		}
		return resp1;
	}
	
	
	/**
	 * Metodo para insertar la informacion en la tabla Orden_Dieta
	 * @param con
	 * @param numeroSolicitud @todo
	 * @param codigoOrden
	 * @param cuenta
	 * @param login
	 * @param datosMedico
	 * @return
	 * @throws SQLException
	 */
	public int insertarOrdenDieta(
			Connection con, 
			int codigoEncabezado,			
			String codigoInstitucion,
			boolean manejarTransaccion) throws SQLException
	{
		
		int  resp1=0;
			
		if (ordenMedicaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (OrdenMedicaDao - insertarOrdenDieta )");
		}
		
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans=false;		
		if(manejarTransaccion)
			inicioTrans=UtilidadBD.iniciarTransaccion(con);
		
		resp1=ordenMedicaDao.insertarOrdenDieta(
				con,
				codigoEncabezado,
				"",
				"",
				"",
				ConstantesBD.codigoNuncaValido,
				this.nutricionOral, 
				this.nutricionParenteral, 
				this.finalizarDieta,
				ConstantesBD.codigoNuncaValido,
				ConstantesBD.codigoNuncaValido,
				codigoInstitucion,
				"");
		logger.info("valor respuesta de la insercion: "+resp1);
		
		if(manejarTransaccion)
		{
			if (!inicioTrans||resp1<1)
			{
			    UtilidadBD.abortarTransaccion(con);
				return -1;
			}
			else
			{
			    UtilidadBD.finalizarTransaccion(con);
			}
		}
		return resp1;
	}
	
	/**
	 * Metodo para insertar otra nutrición oral
	 * @param con
	  * @return
	 * @throws SQLException
	 */
	public int insertarOtroNutricionOral(Connection con) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (ordenMedicaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (OrdenMedicaDao - insertarOtroNutricionOral )");
		}
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
		
		resp1=ordenMedicaDao.insertarOtroNutricionOral(con, this.otroNutORal);
		
		if (!inicioTrans||resp1<1)
		{
		    myFactory.abortTransaction(con);
			return -1;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		return resp1;
	}
	
	/**
	 * Metodo para insertar el detalle de otro nutrición oral
	 * @param con
	 * @param codigoDieta
	 * @param codigoOtroOral
	  * @return
	 * @throws SQLException
	 */
	public int insertarDetalleOtroNutriOral(Connection con, int codigoDieta, int codigoOtroOral,boolean manejarTransaccion) throws SQLException
	{
		int  resp1=0;
			
		if (ordenMedicaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (OrdenMedicaDao - insertarDetalleOtroNutriOral )");
		}
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans=false;
		
		if(manejarTransaccion)
		inicioTrans=UtilidadBD.iniciarTransaccion(con);
		
		
		resp1=ordenMedicaDao.insertarDetalleOtroNutriOral(con, codigoDieta, codigoOtroOral);
		
		if(manejarTransaccion)
		{
			if (!inicioTrans||resp1<1)
			{
			    UtilidadBD.abortarTransaccion(con);
				return -1;
			}
			else
			{
			    UtilidadBD.finalizarTransaccion(con);
			}
		}
		
		return resp1;
	}
	
	/**
	 * Funcion Para retornar una collecion con el listado de los tipos de nutricion Oral
	 * @param con
	 * @param mezcla
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param inicioEncabezado @todo
	 * @param finEncabezado @todo
	 * @param codigo de la institucion
	 * @param codigo del centro_costo
	 * @param Nro Consulta parametro que indica la informacion a sacar
	 *        1  Listado de tipos de nutricion Oral        
	 * 		  2  Listado de tipos de nutricion parenteral
	 * 		  3  Listado de los tipos de cuidados de emfermeria
	 * 		  4  Listado de tipos de elemnetos en el soporte respiratorio
	 *  	  5  Listado de tipos de monitoreo	
	 * @return Collection 
	 */

     public Collection consultarTipos(Connection con, int institucion, int centro_costo, int nroConsulta, int mezcla, int codigoCuenta, int cuentaAsocio, int inicioEncabezado, int finEncabezado) 
     {
		OrdenMedicaDao ordenMedicaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOrdenMedicaDao();
		Collection coleccion=null;
		try
		{	
			coleccion = ordenMedicaDao.consultarTipos(con, institucion, centro_costo, nroConsulta, mezcla, codigoCuenta, cuentaAsocio, inicioEncabezado, finEncabezado);
		}
		catch(Exception e)
		{
		  logger.warn("Error al Consultar tablas  parametrizables para Orden Medicas " +e.toString());
		  coleccion=null;
		}
		return coleccion;		
	 }

     
 	/**
 	 * Metodo para consultar los historicos del soporte respiratorio
 	 * @param con
 	 * @param codigoCuenta
 	 * @param cuentaAsocio
 	 * @return
 	 */
 	 
      public Collection consultarSoporteRespiraHisto(Connection con, int cuenta, int cuentaAsocio) 
      {
 		OrdenMedicaDao ordenMedicaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOrdenMedicaDao();
 		Collection coleccion=null;
 		try
 		{	
 			coleccion = ordenMedicaDao.consultarSoporteRespiraHisto(con,cuenta, cuentaAsocio);
 		}
 		catch(Exception e)
 		{
 		  logger.warn("Error al Consultar tablas  parametrizables para Orden Medicas " +e.toString());
 		  coleccion=null;
 		}
 		return coleccion;		
 	 }

 	
     /**
 	 * Funcion para retornar los historicos de los monitoreos a una persona especifica 
 	 * @param con
 	 * @param codigoCuenta
 	 * @param cuentaAsocio
 	 * @return
 	 */
     
      public Collection consultarMonitoreosHisto(Connection con, int cuenta, int cuentaAsocio) 
      {
 		OrdenMedicaDao ordenMedicaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOrdenMedicaDao();
 		Collection coleccion=null;
 		try
 		{	
 			coleccion = ordenMedicaDao.consultarMonitoreosHisto(con,cuenta, cuentaAsocio);
 		}
 		catch(Exception e)
 		{
 		  logger.warn("Error al Consultar tablas  parametrizables para Orden Medicas " +e.toString());
 		  coleccion=null;
 		}
 		return coleccion;		
 	 }

     
     
     /**
      * Funcion para consultar los historicos de nutricion parenteral 
      * @param con
     * @param codigoCuenta
     * @param cuentaAsocio
     * @param mezcla @todo
      * @return
      */
     public Collection consultarNutricionParentHisto(Connection con, int codigoCuenta, int cuentaAsocio, int mezcla)
     {
		OrdenMedicaDao ordenMedicaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOrdenMedicaDao();
		Collection coleccion=null;
		try
		{
			coleccion = ordenMedicaDao.consultarNutricionParentHisto(con, codigoCuenta, cuentaAsocio, mezcla);
		}
		catch(Exception e)
		{
		  logger.warn("Error al Consultar tablas  parametrizables para Orden Medicas " +e.toString());
		  coleccion=null;
		}
		return coleccion;		
     }

     
     /**
      * Funcion para consultar los historicos de nutricion parenteral 
      * @param con
      * @param codigoCuenta
      * @param cuentaAsocio
      * @return
      */
     public Collection consultarNutricionOralHisto(Connection con, int codigoCuenta, int cuentaAsocio)
     {
		OrdenMedicaDao ordenMedicaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOrdenMedicaDao();
		Collection coleccion=null;
		try
		{
			coleccion = ordenMedicaDao.consultarNutricionOralHisto(con, codigoCuenta, cuentaAsocio); 
		}
		catch(Exception e)
		{
		  logger.warn("Error al Consultar los historicos de dietas orales  " +e.toString());
		  coleccion=null;
		}
		return coleccion;		
     }

     /**
 	 * Metodo para consultar los historicos de cuidado enfermeria 
 	 * @param con
 	 * @param codigoCuenta
 	 * @param cuentaAsocio
 	 * @param institucion
 	 * @param centroCosto
     * @param fechaFinal 
     * @param fechaInicial 
 	 * @return
 	 */
 	public Collection consultarCuidadosEnfHisto(Connection con, int codigoCuenta, int cuentaAsocio, int institucion, int centroCosto, String fechaInicial, String fechaFinal)
 	{
		OrdenMedicaDao ordenMedicaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOrdenMedicaDao();
		Collection coleccion=null;
		try
		{
			coleccion = ordenMedicaDao.consultarCuidadosEnfHisto(con, codigoCuenta, cuentaAsocio, institucion, centroCosto,fechaInicial,fechaFinal);
		}
		catch(Exception e)
		{
		  logger.warn("Error al Consultar los historicos de cuidados de Enfermeria " +e.toString());
		  coleccion=null;
		}
		return coleccion;		


 	}
     

     /**
      * Metodo para consultar la orden medica de un paciente especifico 
      * @param con
      * @param codigoCuenta
      * @param asocioCuenta si es true se carga la información específica de asocio de cuentas 
      * 											sino con el número de cuenta normal se carga toda la información 
      * @return true si existe una orden médica para el paciente sino retorna false
      */
     public boolean cargarOrdenMedica(Connection con, int codigoCuenta, boolean asocioCuenta) 
     {
    	 
     	OrdenMedicaDao ordenMedicaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOrdenMedicaDao();
		Collection coleccion=null;
				
		try
		{	
			coleccion = ordenMedicaDao.cargarOrdenMedica(con, codigoCuenta);
			List<DtoObservacionesGeneralesOrdenesMedicas> listaObservacion=ordenMedicaDao.consultarObservacionesOrdenMedica(con, codigoCuenta);
			String historicoDescripcionSoporteRespiratorio = ordenMedicaDao.consultarDescripcionSoporteRespiratorio(con, codigoCuenta);
			
			Iterator ite=coleccion.iterator();
			
			if (!asocioCuenta)
			{
				if(ite.hasNext())
				{
					HashMap col=(HashMap) ite.next();
					this.codigoOrden = Utilidades.convertirAEntero(col.get("codigo")+"");
					this.fechaOrden = (col.get("fecha_orden")+"").equals("null")?UtilidadFecha.getFechaActual():(col.get("fecha_orden")+"");
					this.horaOrden = (col.get("hora_orden")+"").equals("null")?UtilidadFecha.getHoraActual():(col.get("hora_orden")+"");
					//this.fechaOrden = UtilidadFecha.getFechaActual();
					//this.horaOrden = UtilidadFecha.getHoraActual();
					this.descripcionSoporteRespiratorio = historicoDescripcionSoporteRespiratorio != null ? historicoDescripcionSoporteRespiratorio : "";
					this.descripcionDieta = (col.get("descripcion_dieta_oral")+"").equals("null")  ? "" : (col.get("descripcion_dieta_oral")+"");
					this.descripcionDietaParenteral = (col.get("descripcion_dieta_par")+"").equals("null")  ? "" : (col.get("descripcion_dieta_par")+"");
					this.observacionesGenerales = "";
					
					
					for (int i = 0; i < listaObservacion.size(); i++) {
						if(listaObservacion.get(i)!=null){
							this.observacionesGenerales +="\n\n";
							if(listaObservacion.get(i).getFechaGeneracion()!=null){
								this.observacionesGenerales +="\n"+listaObservacion.get(i).getFechaGeneracion();
							}
							
							if(listaObservacion.get(i).getFechaOrden()!=null){
								this.observacionesGenerales +="\n"+listaObservacion.get(i).getFechaOrden();
							}
							
							if(listaObservacion.get(i).getObservaciones()!=null){
								this.observacionesGenerales +="\n"+listaObservacion.get(i).getObservaciones();
							}
							
							if(listaObservacion.get(i).getDatosMedico()!=null){
								this.observacionesGenerales +="\n"+listaObservacion.get(i).getDatosMedico();
							}
							
						
						}
					}
					
				
					
					//this.observacionesGenerales = (col.get("observaciones_generales")+"").equals("null")  ? "" : (col.get("observaciones_generales")+"");
					
					
					//System.out.print("\n\nObservaciones Generales ------------>" +this.observacionesGenerales + "<-------------\n\n");
					return true;
				}
				else
				{
				 return false;	
				}
			}
			else
			{
				if(ite.hasNext())
				{
					HashMap col=(HashMap) ite.next();
					//this.codigoOrden = Utilidades.convertirAEntero(col.get("codigo")+"");
					this.fechaOrden = (col.get("fecha_orden")+"").equals("null")?UtilidadFecha.getFechaActual():(col.get("fecha_orden")+"");
					this.horaOrden = (col.get("hora_orden")+"").equals("null")?UtilidadFecha.getHoraActual():(col.get("hora_orden")+"");
					//this.fechaOrden = UtilidadFecha.getFechaActual();
					//this.horaOrden = UtilidadFecha.getHoraActual();
					this.descripcionSoporteUrgencias = (col.get("descripcion_soporte")+"").equals("null")  ? "" : (col.get("descripcion_soporte")+"");
					this.descripcionDietaUrgencias = (col.get("descripcion_dieta_oral")+"").equals("null")  ? "" : (col.get("descripcion_dieta_oral")+"");
					
					if(listaObservacion.get(0)!=null){
						this.observacionesGenerales =listaObservacion.get(0).getFechaGeneracion()+
						"\n"+listaObservacion.get(0).getFechaOrden()+
						"\n"+listaObservacion.get(0).getObservaciones()+
						"\n"+listaObservacion.get(0).getDatosMedico() ;
					}else{
						this.observacionesGenerales = "";
					}
					
					
					//this.observacionesGralesUrgencias = (col.get("observaciones_generales")+"").equals("null")  ? "" : (col.get("observaciones_generales")+"");
					
					return true;
				}
				else
				{
				 return false;	
				}
			}
			
		}
		catch(Exception e)
		{
		  logger.warn("Error al Consultar la orden médica " +e.toString());
		  coleccion=null;
		}
		 return false;		
     }

     /**
      * Metodo para cargar la información del último tipo de monitoreo ingresado en la orden médica
      * @param con
      * @param CodigoCuenta
      * @return True si existe tipo monitoreo para la orden sino retorna false
      */
     
     public boolean cargarTipoMonitoreo(Connection con, int codigoCuenta) 
     {
     	OrdenMedicaDao ordenMedicaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOrdenMedicaDao();
		Collection coleccion=null;
		
		try
		{	
			coleccion = ordenMedicaDao.cargarTipoMonitoreo(con, codigoCuenta);
			
			Iterator ite=coleccion.iterator();
			if(ite.hasNext())
			{
				HashMap col=(HashMap) ite.next();
				this.tipoMonitoreo = Utilidades.convertirAEntero(col.get("codigo")+"");	
				return true;
			}
			else
			{
				return false;	
			}
			
		}
		catch(Exception e)
		{
		  logger.warn("Error al Consultar Tabla Orden Tipo Monitoreo " +e.toString());
		  coleccion=null;
		}
		 return false;		
     }
     
     
     /**
      * Metodo para consultar y cargar la información en la sección soporte respiratorio 
      * @param con
      * @param codigoCuenta
      * @param asocio
      * @param institucion
      * @param centroCosto
      * @param ordenMedicaForm
      * @return true si existe un soporte respiratario para el paciente sino retorna false
      */
     public boolean cargarSoporteRespiratorio(Connection con, int codigoCuenta, boolean asocio, int institucion, int centroCosto) 
     {
     	OrdenMedicaDao ordenMedicaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOrdenMedicaDao();
		Collection coleccion=null;
		
		try
		{	
			coleccion = ordenMedicaDao.cargarSoporteRespiratorio(con, codigoCuenta, asocio, institucion, centroCosto);
			
			Iterator ite=coleccion.iterator();
			if(ite.hasNext())
			{
				HashMap col=(HashMap) ite.next();
				this.cantidadSoporteRespiratorio = Float.parseFloat(col.get("cantidad")+"");
				this.equipoElemento = (col.get("equipo_elemento")+"").equals("null") ? -1: Utilidades.convertirAEntero(col.get("equipo_elemento")+"");		
				this.oxigenoTerapia = (col.get("oxigeno_terapia")+"").equals("null")  ? "" : (col.get("oxigeno_terapia")+"");
								
				this.codigoEncabezadoSoporteRespira = Integer.parseInt((col.get("codigo_histo_enca")+""));	
				this.descripcionIndivSoporteRespira = (col.get("descripcion")+"").equals("null") ? "" : (col.get("descripcion")+"");	
								
				return true;
			}
			else
			{
			 return false;	
			}
			
			
		}
		catch(Exception e)
		{
		  logger.warn("Error al Consultar el soporte respiratorio para la Orden Medicas " +e.toString());
		  coleccion=null;
		}
		 return false;		
     }
	
     

     /**
      * Metodo para consultar y cargar la información de la dieta
      * @param con
      * @param codigoCuenta
      * @param asocio
      * @param institucion
      * @param centroCosto
      * @param ordenMedicaForm
      * @return true si existe nutrición oral para el paciente sino retorna false
      */
     public boolean cargarDieta(Connection con, int codigoCuenta, boolean asocio, int institucion, int centroCosto, OrdenMedicaForm ordenMedicaForm) 
     {
     	OrdenMedicaDao ordenMedicaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOrdenMedicaDao();
		Collection coleccion=null;
		Collection coleccionOral=null;
		Collection coleccionOtroOral=null;
		int codigoDieta=0;
		Vector temp=new Vector();
		
		try
		{	
			coleccion = ordenMedicaDao.cargarDieta(con, codigoCuenta);
			
			Iterator ite=coleccion.iterator();
			if(ite.hasNext())
			{
				HashMap col=(HashMap) ite.next();
				codigoDieta=Utilidades.convertirAEntero(col.get("codigo_histo_dieta")+"");
				this.nutricionOral=(col.get("nutricion_oral")+"").equals(ValoresPorDefecto.getValorTrueParaConsultas()) ? true : false;
				this.nutricionParenteral=(col.get("nutricion_parenteral")+"").equals(ValoresPorDefecto.getValorTrueParaConsultas()) ? true : false;
				this.finalizarDieta=(col.get("finalizar_dieta")+"").equals(ValoresPorDefecto.getValorTrueParaConsultas()) ? true : false;
				ordenMedicaForm.setSuspenderEnfermeria(col.get("suspendido_enfermeria")+"");
				//this.otroNutORal=(col.get("otro_oral")+"").equals("") ? "": (col.get("otro_oral")+"");
				if(col.get("mezcla")!=null){
					this.mezcla = Utilidades.convertirAEntero(col.get("mezcla")+"")==ConstantesBD.codigoNuncaValido?0:Utilidades.convertirAEntero(col.get("mezcla")+"");
				}else{
					this.mezcla = 0;
				}
				
				if (this.nutricionOral)
				{
//-----------------------------------------Cargar tipos de nutrición Oral parametrizados-------------------------------------------------//
					//coleccionOral = ordenMedicaDao.cargarNutricionOral(con, codigoCuenta, codigoDieta);
					if (!asocio)
					{
						logger.info("1");
						coleccionOral = ordenMedicaDao.cargarNutricionOral(con, codigoCuenta, asocio, institucion, centroCosto);
						Iterator ite2=coleccionOral.iterator();
						for(int i=0;i<coleccionOral.size();i++)
						{
							if(ite2.hasNext())
							{
								HashMap colOral=(HashMap) ite2.next();
								if (colOral.get("codigo")!=null && !colOral.get("codigo").equals(""))
									{
									this.codigosNutOral.add(colOral.get("codigo"));
									this.setMapa("tipoNut_"+colOral.get("codigo"), colOral.get("codigo")+"");
									}								
							}
						}
					}
					else
					{
						//-------------Se obtiene el número de la secuencia de otro tipo de nutrición oral---------------//
						logger.info("2");
						int codSeq=0;
						DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
						codSeq=myFactory.obtenerUltimoValorSecuencia(con, "seq_otro_nutricion_oral");
						//------------------------------------------------------------------------------------------------------------------------//
						
						coleccionOral = ordenMedicaDao.cargarNutricionOral(con, codigoCuenta, asocio, institucion, centroCosto);
						Iterator ite2=coleccionOral.iterator();
						
						for(int i=0;i<coleccionOral.size();i++)
						{
							if(ite2.hasNext())
							{
								HashMap colOral=(HashMap) ite2.next();
								try
								{
									Utilidades.convertirAEntero(colOral.get("codigo")+"");
									this.codigosNutOral.add(colOral.get("codigo"));
									this.setMapa("tipoNut_"+colOral.get("codigo"), colOral.get("codigo")+"");	
								}
								catch(NumberFormatException e)
								{
									
									codSeq+=1;
									
									colOral.put("descripcion", colOral.get("codigo").toString());
									
									temp.add(colOral.get("codigo"));
									
									try
									{
									colOral.put("codigo",new Integer(codSeq));
									}
									catch(Exception e1)
									{
										colOral.put("codigo",new String(codSeq+""));
									}
																		
																		
									//-------------Se adiciona el tipo de nutrición oral no parametrizado en hospitalización a la colección 
									//-------------que tiene los otros tipos de nutrición oral------------------------------------//
									ordenMedicaForm.getListadoOtrosNutOral().add(colOral);
									
																								
								}								
							}//if
						}//for
						
						//----------Se guarda en este vector cada una de las descripciones no parametrizadas en hospitalización--------//
						this.setOtrasNutricionesOrales(temp);
						
					}//else
//----------------------------------------------------------Cargar otros tipos de nutrición Oral-------------------------------------------------//
					//coleccionOtroOral = ordenMedicaDao.cargarOtroNutricionOral(con, codigoCuenta, codigoDieta);
					logger.info("3");
					coleccionOtroOral = ordenMedicaDao.cargarOtroNutricionOral(con, codigoCuenta);
					Iterator ite3=coleccionOtroOral.iterator();
					for(int i=0;i<coleccionOtroOral.size();i++)
					{
						if(ite3.hasNext())
						{
							HashMap colOralOtro=(HashMap) ite3.next();
							this.codigosOtroNutOral.add(colOralOtro.get("nutricion_oral"));
							this.setMapa("tipoNutOtro_"+colOralOtro.get("nutricion_oral"), colOralOtro.get("nutricion_oral")+"");
						}
					}
					
				}//if nutricionOral=true
				else
					this.otroNutORal = "";
				
				logger.info("sale victoriosa d ela carga nutrision oral");								
				return true;
			}
			else
			{
			 return false;	
			}
			
			
		}
		catch(Exception e)
		{
		  logger.warn("Error al Consultar la dieta para la Orden Medica ",e);
		  coleccion=null;
		}
		 return false;		
     }
     
     
     /**
 	 * Funcion que retorna una collecion con el listado de los otros tipos de nutrición oral a una cuenta específica
 	 * @param con
 	 * @param cuenta
 	 * @param cuentaAsocio
 	 * @return Collection 
 	 */

      public Collection consultarOtrosNutricionOral(Connection con, int cuenta, int cuentaAsocio) 
      {
 		OrdenMedicaDao ordenMedicaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOrdenMedicaDao();
 		Collection coleccion=null;
 		try
 		{	
 			coleccion = ordenMedicaDao.consultarOtrosNutricionOral(con, cuenta, cuentaAsocio);
 		}
 		catch(Exception e)
 		{
 		  logger.warn("Error al Consultar otros tipos de nutrición oral " +e.toString());
 		  coleccion=null;
 		}
 		return coleccion;		
 	 }
     
      
      /**
       * Metodo para cargar la información ingresada en la cuenta de asocio, referente a descripción del soporte, dieta
       * y observaciones generales 
       * @param con
       * @param cuentaAsocio
       * @return True si existe información sino retorna false
       */
      
      public boolean cargarDatosUrgencias(Connection con, int cuentaAsocio) 
      {
    	 
      	OrdenMedicaDao ordenMedicaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOrdenMedicaDao();
 		Collection coleccion=null;
 		
 		try
 		{	
 			 List<DtoObservacionesGeneralesOrdenesMedicas> listaObservacion=ordenMedicaDao.consultarObservacionesOrdenMedica(con, cuentaAsocio);
 			coleccion = ordenMedicaDao.cargarDatosUrgencias(con, cuentaAsocio);
 			
 			Iterator ite=coleccion.iterator();
 			if(ite.hasNext())
 			{
 				HashMap col=(HashMap) ite.next();
 				this.descripcionSoporteUrgencias = (col.get("descripcion_soporte")+"").equals("null")  ? "" : (col.get("descripcion_soporte")+"");
				this.descripcionDietaUrgencias = (col.get("descripcion_dieta_oral")+"").equals("null")  ? "" : (col.get("descripcion_dieta_oral")+"");
				//this.observacionesGralesUrgencias = (col.get("observaciones_generales")+"").equals("null")  ? "" : (col.get("observaciones_generales")+"");
 				
				
				this.observacionesGralesUrgencias = "";
				
				
				for (int i = 0; i < listaObservacion.size(); i++) {
					if(listaObservacion.get(i)!=null){
						this.observacionesGralesUrgencias +="\n\n";
						if(listaObservacion.get(i).getFechaGeneracion()!=null){
							this.observacionesGralesUrgencias +="\n"+listaObservacion.get(i).getFechaGeneracion();
						}
						
						if(listaObservacion.get(i).getFechaOrden()!=null){
							this.observacionesGralesUrgencias +="\n"+listaObservacion.get(i).getFechaOrden();
						}
						
						if(listaObservacion.get(i).getObservaciones()!=null){
							this.observacionesGralesUrgencias +="\n"+listaObservacion.get(i).getObservaciones();
						}
						
						if(listaObservacion.get(i).getDatosMedico()!=null){
							this.observacionesGralesUrgencias +="\n"+listaObservacion.get(i).getDatosMedico();
						}
						
					
					}
				}
				
				
				return true;
 			}
 			else
 			{
 				return false;	
 			}
 			
 		}
 		catch(Exception e)
 		{
 		  logger.warn("Error al cargar la información de la cuenta de Asocio " +e.toString());
 		  coleccion=null;
 		}
 		 return false;		
      }
      
      /**
       * Método para comparar los tipos de nutrición oral parametrizados para la cuenta de hospitalización y de urgencias
       * cuando existe cuenta de asocio
       * @param con
       * @param institucion
       * @param centroCosto
       * @param viaUrgencias
       * @return
       */
      
    /*  public Collection compararTiposOral(Connection con, int institucion, int centro_costo, int viaUrgencias, OrdenMedicaForm ordenMedicaForm)
      {
      	OrdenMedicaDao ordenMedicaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOrdenMedicaDao();
		Collection coleccion1=null;
		Collection coleccion2=null;
		Vector temp=new Vector();
		boolean esta=false;
		
		try
		{	
			coleccion2 = ordenMedicaDao.consultarTipos(con, institucion, centro_costo, 1, -1);
			coleccion1 = ordenMedicaDao.consultarTipos(con, institucion, viaUrgencias, 1, -1);
			
			Iterator ite1=coleccion1.iterator();
			
			
			int codSeq=0;
			DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			codSeq=myFactory.obtenerUltimoValorSecuencia(con, "seq_otro_nutricion_oral");
			
		for(int j=0; j<coleccion1.size(); j++)
			{
			esta=false;
			if(ite1.hasNext())
			{
				HashMap col1=(HashMap) ite1.next();
				Iterator ite2=coleccion2.iterator();
				for(int i=0;i<coleccion2.size();i++)
					{
						if(ite2.hasNext())
						{
							HashMap col2=(HashMap) ite2.next();
							if( col1.get("codigo_tipo_oral").equals(col2.get("codigo_tipo_oral")))
							{
								esta=true;
								break;
							}
							if(esta==false && i==coleccion2.size()-1)
							{

								//-----------Se mira en que número va la secuencia de otro nutrición oral------------------------------//
								codSeq+=1;			
								
								col1.set("codigo",new Integer(codSeq));
								col1.set("codigo_tipo_oral",new Integer(0));
																
								ordenMedicaForm.getListadoOtrosNutOral().add(col1);
								
								//-----Se adiciona en este vector las descripciones de los nuevos tipos de nutrición oral-------------//
								temp.add(col1.get("descripcion"));
																						
							}
						}//if
				  }//for
				}//if
		}//for
		ordenMedicaForm.setOtrasNutricionesOrales(temp);
		
		}
		
		catch(Exception e)
		{
		  logger.warn("Error al Consultar tablas  parametrizables para Orden Medicas " +e.toString());
		  coleccion1=null;
		}
		return coleccion1;		
      }*/
      
      /**
   	 * Funcion que retorna una collecion con el listado de los otros tipos de cuidados de enfermería a una cuenta específica
   	 * @param con
   	 * @param cuenta
   	 * @param cuentaAsocio
   	 * @return Collection 
   	 */

        public Collection consultarOtrosCuidadosEnfer(Connection con, int cuenta, int cuentaAsocio) 
        {
   		OrdenMedicaDao ordenMedicaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOrdenMedicaDao();
   		Collection coleccion=null;
   		try
   		{	
   			coleccion = ordenMedicaDao.consultarOtrosCuidadosEnfer(con, cuenta, cuentaAsocio);
   		}
   		catch(Exception e)
   		{
   		  logger.warn("Error al Consultar otros tipos de cuidados de enfermería " +e.toString());
   		  coleccion=null;
   		}
   		return coleccion;		
   	 }
        
    /**
	 * Metodo para insertar otro tipo de cuidado de enfermería
	 * @param con
	  * @return
	 * @throws SQLException
	 */
	public int insertarOtroTipoCuidadoEnf(Connection con) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (ordenMedicaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (OrdenMedicaDao - insertarOtroTipoCuidadoEnf )");
		}
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
		
		resp1=ordenMedicaDao.insertarOtroTipoCuidadoEnf(con, this.OtroCuidadoEnf);
		
		if (!inicioTrans||resp1<1)
		{
		    myFactory.abortTransaction(con);
			return -1;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		return resp1;
	}
	
	 /**
     * Metodo para cargar la información de la orden de la hoja
     * neurológica
     * @param con
     * @return true si existe orden hoja neurológica
     */
    
    public boolean cargarOrdenHojaNeurologica(Connection con) 
    {
    OrdenMedicaDao ordenMedicaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOrdenMedicaDao();
	Collection coleccion=null;
	
	try
	{	
		coleccion = ordenMedicaDao.cargarOrdenHojaNeurologica(con, this.codigoOrden);
		
		Iterator ite=coleccion.iterator();
		if(ite.hasNext())
		{
			HashMap col=(HashMap) ite.next();
			this.presentaHojaNeuro= (col.get("presenta")+"").equals("null")  ? "" : (col.get("presenta")+"");
			this.observacionesHojaNeuro = (col.get("observaciones")+"").equals("null")  ? "" : (col.get("observaciones")+"");
			this.finalizadaHojaNeuro = (col.get("finalizada")+"").equals("null")  ? "" : (col.get("finalizada")+"");
			return true;
		}
		else
		{
			return false;	
		}
		
	}
	catch(Exception e)
	{
	  logger.warn("Error al Consultar la Orden de Hoja Neurológica " +e.toString());
	  coleccion=null;
	}
	 return false;		
    }
    
    /**
     * Método que consulta las mezclas parenterales y los articulos con su correspondiente
     * información de detalle
     * @param con
     * @param codigoCuenta
     * @param codigoCuentaAsocio
     * @param nroConsulta 
     * @return
     */
    public Collection consultarMezclasParenteral(Connection con, int codigoCuenta, int codigoCuentaAsocio, int nroConsulta)
    {
    	OrdenMedicaDao ordenMedicaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOrdenMedicaDao();
 		Collection coleccion=null;
 		try
 		{	
 			coleccion = ordenMedicaDao.consultarMezclasParenteral (con, codigoCuenta, codigoCuentaAsocio, nroConsulta);
 		}
 		catch(Exception e)
 		{
 		  logger.warn("Error al Consultar mezclas parenteral ver anteriores consulta "+nroConsulta+""+e.toString());
 		  coleccion=null;
 		}
 		return coleccion;
    }
    
    /**
     * Método que consulta la información de los articulos asociados a la mezcla en el ver anteriores
     * @param con
     * @param codigoCuenta
     * @param codigoCuentaAsocio
     * @param codMezcla
     * @param codEncaMin
     * @param codEncabezadoAnterior
     * @return
     */
    public Collection consultarDetalleMezclaAnteriores(Connection con, int codigoCuenta, int codigoCuentaAsocio, int codMezcla, int codEncaMin, int codEncabezadoAnterior)
    {
    	OrdenMedicaDao ordenMedicaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getOrdenMedicaDao();
 		Collection coleccion=null;
 		try
 		{	
 			coleccion = ordenMedicaDao.consultarDetalleMezclaAnteriores (con, codigoCuenta, codigoCuentaAsocio, codMezcla, codEncaMin, codEncabezadoAnterior);
 		}
 		catch(Exception e)
 		{
 		  logger.warn("Error al Consultar el detalle de la mezcla finalizada en ver anteriores "+e.toString());
 		  coleccion=null;
 		}
 		return coleccion;
    }
    
    
    /**
     * Consulta el listado de almacenes
     *  @param Connection con
     *  @param PersonaBasica paciente
     *  @param UsuarioBasico usuario
     * */
    public static HashMap consultarListadoAlmacenes(Connection con, PersonaBasica paciente, UsuarioBasico usuario)
    {
    	boolean planEspecial = false;
    	
    	// CAPTURO EL NUMERO DE CONVENIOS PLAN ESPECIAL POR INGRESO
	    int numConveniosPlanEspecial = Cargos.conveniosPlanEspecial(con, paciente.getCodigoIngreso());		    
        logger.info("Cantidad Convenios Plan Especial por Ingreso -> "+numConveniosPlanEspecial);		    
	    		    
        // PREGUNTO SI LOS CONVENIOS SON MAYORES A CERO PARA MOSTRAR LOS ALMACENES
        if(numConveniosPlanEspecial>0)
        {	        		        		        	
        	logger.info("Numero de Almacenes Parametrizados como Plan Especial --->"+UtilidadInventarios.existeAlmacenPlanEspecial(con, usuario.getCodigoCentroAtencion()));
        	if(UtilidadInventarios.existeAlmacenPlanEspecial(con, usuario.getCodigoCentroAtencion()) < 1)
        		planEspecial=true;	    	    
        }
        else
        {
        	logger.info("NO TIENE CONVENIOS PLAN ESPECIAL");
        	planEspecial=false;
        }	
		
		HashMap listadoAlmacenesMap = UtilidadInventarios.listadoAlmacenes(
										usuario.getCodigoInstitucionInt(),
										paciente.getCodigoArea(),
										ValoresPorDefecto.getCodigoTransSoliPacientes(usuario.getCodigoInstitucionInt(),true),
										"", 
										planEspecial);
        
		int numFarmacias=Utilidades.convertirAEntero(listadoAlmacenesMap.get("numRegistros")+"");				
		listadoAlmacenesMap.put("numRegistros",numFarmacias);
		return listadoAlmacenesMap;		               	
    	
    }
    
    
    /**
     * Elimina Todos los articulos nuevos
     * @param HashMap mapaBase
     * @param int numElementos
     * @param String codigoArticulo
     * */
    public static HashMap elimiarTodosArticulosNuevos(HashMap mapaBase, int numElementos, String codigoArticulo)
    {
    	for(int i=0; i<numElementos; i++)
    	{
    		if(mapaBase.containsKey("art_"+i) && codigoArticulo.equals(""))
    		{
	    		mapaBase.remove("nombreart_"+i);	    		
	    		mapaBase.remove("tipoParent_"+mapaBase.get("art_"+i).toString());
	    		mapaBase.remove("unidad_volumen_"+mapaBase.get("art_"+i).toString());
	    		mapaBase.put("codigosOtros","");
	    		mapaBase.remove("art_"+i);   	
	    		
	    		if(mapaBase.containsKey("espos_"+i))
	    			mapaBase.remove("espos_"+i);
    		}
    		else if(mapaBase.containsKey("art_"+i)
    				&& !codigoArticulo.equals("") 
    					&&  mapaBase.get("art_"+i).toString().equals(codigoArticulo)) 
    		{
    			mapaBase.remove("nombreart_"+i);
	    		mapaBase.remove("tipoParent_"+mapaBase.get("art_"+i).toString());
	    		mapaBase.remove("unidad_volumen_"+mapaBase.get("art_"+i).toString());
	    		mapaBase.put("codigosOtros",mapaBase.get("codigosOtros").toString().replace(codigoArticulo,""));
	    		mapaBase.remove("art_"+i);
	    		
	    		if(mapaBase.containsKey("espos_"+i))
	    			mapaBase.remove("espos_"+i);
	    		
	    		for(int j= i; j<numElementos; j++)
	    		{
	    			if((j+1)<numElementos)
	    			{
		    			mapaBase.put("nombreart_"+j,mapaBase.get("nombreart_"+(j+1)));			    		
			    		mapaBase.put("art_"+j,mapaBase.get("art_"+(j+1)));
			    		
			    		if(mapaBase.containsKey("espos_"+(j+1)))
			    			mapaBase.put("espos_"+j,mapaBase.get("espos_"+(j+1)));
	    			}
	    			else
	    			{
	    				mapaBase.remove("nombreart_"+j);	    		
	    	    		mapaBase.remove("art_"+j);
	    	    		
	    	    		if(mapaBase.containsKey("espos_"+j))
	    	    			mapaBase.remove("espos_"+j);
	    			}
	    		}
	    			    		
	    		return mapaBase;
    		}
    	}
  	
    	return mapaBase;    	
    }
        
	 //***************************************** SETS Y GETS ********************************************//    
	     
	/**
	 * @return Returns the codigoOrden.
	 */
	public int getCodigoOrden()
	{
		return codigoOrden;
	}
	/**
	 * @param codigoOrden The codigoOrden to set.
	 */
	public void setCodigoOrden(int codigoOrden)
	{
		this.codigoOrden = codigoOrden;
	}
	/**
	 * @return Returns the cuidadosEnfermeria.
	 */
	public ArrayList getCuidadosEnfermeria()
	{
		return cuidadosEnfermeria;
	}
	/**
	 * @param cuidadosEnfermeria The cuidadosEnfermeria to set.
	 */
	public void setCuidadosEnfermeria(ArrayList cuidadosEnfermeria)
	{
		this.cuidadosEnfermeria = cuidadosEnfermeria;
	}
	/**
	 * @return Returns the descripcionDieta.
	 */
	public String getDescripcionDieta()
	{
		return descripcionDieta;
	}
	/**
	 * @param descripcionDieta The descripcionDieta to set.
	 */
	public void setDescripcionDieta(String descripcionDieta)
	{
		this.descripcionDieta = descripcionDieta;
	}
		/**
		 * @return Returns the descripcionSoporteRespiratorio.
		 */
		public String getDescripcionSoporteRespiratorio()
		{
			return descripcionSoporteRespiratorio;
		}
		/**
		 * @param descripcionSoporteRespiratorio The descripcionSoporteRespiratorio to set.
		 */
		public void setDescripcionSoporteRespiratorio(
				String descripcionSoporteRespiratorio)
		{
			this.descripcionSoporteRespiratorio = descripcionSoporteRespiratorio;
		}
	/**
	 * @return Returns the equipoElemento.
	 */
	public int getEquipoElemento()
	{
		return equipoElemento;
	}
	/**
	 * @param equipoElemento The equipoElemento to set.
	 */
	public void setEquipoElemento(int equipoElemento)
	{
		this.equipoElemento = equipoElemento;
	}
	/**
	 * @return Returns the cantidadSoporteRespiratorio.
	 */
	public float getCantidadSoporteRespiratorio()
	{
		return cantidadSoporteRespiratorio;
	}
	/**
	 * @param cantidadSoporteRespiratorio The cantidadSoporteRespiratorio to set.
	 */
	public void setCantidadSoporteRespiratorio(float cantidadSoporteRespiratorio)
	{
		this.cantidadSoporteRespiratorio = cantidadSoporteRespiratorio;
	}
	/**
	 * @return Returns the farmacia.
	 */
	public int getFarmacia()
	{
		return farmacia;
	}
	/**
	 * @param farmacia The farmacia to set.
	 */
	public void setFarmacia(int farmacia)
	{
		this.farmacia = farmacia;
	}
	/**
	 * @return Returns the fechaAdmision.
	 */
	public String getFechaAdmision()
	{
		return fechaAdmision;
	}
	/**
	 * @param fechaAdmision The fechaAdmision to set.
	 */
	public void setFechaAdmision(String fechaAdmision)
	{
		this.fechaAdmision = fechaAdmision;
	}
	/**
	 * @return Returns the fechaGrabacion.
	 */
	public String getFechaGrabacion()
	{
		return fechaGrabacion;
	}
	/**
	 * @param fechaGrabacion The fechaGrabacion to set.
	 */
	public void setFechaGrabacion(String fechaGrabacion)
	{
		this.fechaGrabacion = fechaGrabacion;
	}
	/**
	 * @return Returns the fechaOrden.
	 */
	public String getFechaOrden()
	{
		return fechaOrden;
	}
	/**
	 * @param fechaOrden The fechaOrden to set.
	 */
	public void setFechaOrden(String fechaOrden)
	{
		this.fechaOrden = fechaOrden;
	}
	/**
	 * @return Returns the finalizarDieta.
	 */
	public boolean isFinalizarDieta()
	{
		return finalizarDieta;
	}
	/**
	 * @param finalizarDieta The finalizarDieta to set.
	 */
	public void setFinalizarDieta(boolean finalizarDieta)
	{
		this.finalizarDieta = finalizarDieta;
	}
	/**
	 * @return Returns the finalizarSoporte.
	 */
	public boolean isFinalizarSoporte()
	{
		return finalizarSoporte;
	}
	/**
	 * @param finalizarSoporte The finalizarSoporte to set.
	 */
	public void setFinalizarSoporte(boolean finalizarSoporte)
	{
		this.finalizarSoporte = finalizarSoporte;
	}
	/**
	 * @return Returns the oxigenoTerapia.
	 */
	public String getOxigenoTerapia()
	{
		return oxigenoTerapia;
	}
	/**
	 * @param oxigenoTerapia The oxigenoTerapia to set.
	 */
	public void setOxigenoTerapia(String oxigenoTerapia)
	{
		this.oxigenoTerapia = oxigenoTerapia;
	}
	/**
	 * @return Returns the horaAdmision.
	 */
	public String getHoraAdmision()
	{
		return horaAdmision;
	}
	/**
	 * @param horaAdmision The horaAdmision to set.
	 */
	public void setHoraAdmision(String horaAdmision)
	{
		this.horaAdmision = horaAdmision;
	}
	/**
	 * @return Returns the horaGrabacion.
	 */
	public String getHoraGrabacion()
	{
		return horaGrabacion;
	}
	/**
	 * @param horaGrabacion The horaGrabacion to set.
	 */
	public void setHoraGrabacion(String horaGrabacion)
	{
		this.horaGrabacion = horaGrabacion;
	}
	/**
	 * @return Returns the horaOrden.
	 */
	public String getHoraOrden()
	{
		return horaOrden;
	}
	/**
	 * @param horaOrden The horaOrden to set.
	 */
	public void setHoraOrden(String horaOrden)
	{
		this.horaOrden = horaOrden;
	}
	/**
	 * @return Returns the logger.
	 */
	public Logger getLogger()
	{
		return logger;
	}
	/**
	 * @param logger The logger to set.
	 */
	public void setLogger(Logger logger)
	{
		this.logger = logger;
	}
	
	//----------Funcion del hashMap----------------
	/**
	 * @return Returna la propiedad del mapa mapa.
	 */
	public Object getMapa(String key)
	{
		return mapa.get(key);
	}
	/**
	 * 
	 * */
	public HashMap getMapa()
	{
		return mapa;
	}
	/**
	 * @param Asigna la propiedad al mapa
	 */
	public void setMapa(String key, Object value)
	{
		this.mapa.put(key, value);
	}
	
	/**
	 * @return Retorna nutricion.
	 */
	public HashMap getMapaCompleto() {
		return this.mapa;
	}
	/**
	 * @param Asigna nutricion.
	 */
	public void setMapaCompleto(HashMap mapa) {
		this.mapa = mapa;
	}
	
	//------------------------------------------------
	
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
		this.observacionesGenerales = observacionesGenerales;
	}
	/**
	 * @return Returns the ordenMedicaDao.
	 */
	public OrdenMedicaDao getOrdenMedicaDao()
	{
		return ordenMedicaDao;
	}
	/**
	 * @param ordenMedicaDao The ordenMedicaDao to set.
	 */
	public void setOrdenMedicaDao(OrdenMedicaDao ordenMedicaDao)
	{
		this.ordenMedicaDao = ordenMedicaDao;
	}
	/**
	 * @return Returns the tipoMonitoreo.
	 */
	public int getTipoMonitoreo()
	{
		return tipoMonitoreo;
	}
	/**
	 * @param tipoMonitoreo The tipoMonitoreo to set.
	 */
	public void setTipoMonitoreo(int tipoMonitoreo)
	{
		this.tipoMonitoreo = tipoMonitoreo;
	}
	
	/**
	 * Método para obtener el consecutivo de la orden médica
	 * @param con
	 * @return
	 */
	public int obternerCodigoOrden(Connection con)
	{
		int codigo=0;
		
		codigo = ordenMedicaDao.obtenerCodigoOrden(con);
		return codigo;
	}
	
	/**
	 * @return Retorna listadoNutOral.
	 */
	public Collection getListadoNutOral() {
		return listadoNutOral;
	}
	/**
	 * @param Asigna listadoNutOral.
	 */
	public void setListadoNutOral(Collection listadoNutOral) {
		this.listadoNutOral = listadoNutOral;
	}	
	
	/**
	 * @return Retorna otroNutORal.
	 */
	public String getOtroNutORal() {
		return otroNutORal;
	}
	
	/**
	 * @param Asigna otroNutORal.
	 */
	public void setOtroNutORal(String otroNutORal) {
		this.otroNutORal = otroNutORal;
	}
	
	
	/**
	 * @return Retorna otroCuidadoEnf.
	 */
	public String getOtroCuidadoEnf() {
		return OtroCuidadoEnf;
	}
	/**
	 * @param Asigna otroCuidadoEnf.
	 */
	public void setOtroCuidadoEnf(String otroCuidadoEnf) {
		OtroCuidadoEnf = otroCuidadoEnf;
	}
	
	
	/**
	 * @return Retorna nutricionOral.
	 */
	public boolean getNutricionOral() {
		return nutricionOral;
	}
	/**
	 * @param Asigna nutricionOral.
	 */
	public void setNutricionOral(boolean nutricionOral) {
		this.nutricionOral = nutricionOral;
	}
	/**
	 * @return Retorna nutricionParenteral.
	 */
	public boolean getNutricionParenteral() {
		return nutricionParenteral;
	}
	/**
	 * @param Asigna nutricionParenteral.
	 */
	public void setNutricionParenteral(boolean nutricionParenteral) {
		this.nutricionParenteral = nutricionParenteral;
	}
	/**
	 * @return Returns the codigosNutOral.
	 */
	public Vector getCodigosNutOral()
	{
		return codigosNutOral;
	}
	/**
	 * @param codigosNutOral The codigosNutOral to set.
	 */
	public void setCodigosNutOral(Vector codigosNutOral)
	{
		this.codigosNutOral = codigosNutOral;
	}
	/**
	 * @return Returns the codigosOtroNutOral.
	 */
	public Vector getCodigosOtroNutOral()
	{
		return codigosOtroNutOral;
	}
	/**
	 * @param codigosOtroNutOral The codigosOtroNutOral to set.
	 */
	public void setCodigosOtroNutOral(Vector codigosOtroNutOral)
	{
		this.codigosOtroNutOral = codigosOtroNutOral;
	}
	/**
	 * @return Returns the observacionesGralesUrgencias.
	 */
	public String getObservacionesGralesUrgencias()
	{
		return observacionesGralesUrgencias;
	}
	/**
	 * @param observacionesGralesUrgencias The observacionesGralesUrgencias to set.
	 */
	public void setObservacionesGralesUrgencias(String observacionesGralesUrgencias)
	{
		this.observacionesGralesUrgencias = observacionesGralesUrgencias;
	}
	/**
	 * @return Returns the descripcionDietaUrgencias.
	 */
	public String getDescripcionDietaUrgencias()
	{
		return descripcionDietaUrgencias;
	}
	/**
	 * @param descripcionDietaUrgencias The descripcionDietaUrgencias to set.
	 */
	public void setDescripcionDietaUrgencias(String descripcionDietaUrgencias)
	{
		this.descripcionDietaUrgencias = descripcionDietaUrgencias;
	}
	/**
	 * @return Returns the descripcionSoporteUrgencias.
	 */
	public String getDescripcionSoporteUrgencias()
	{
		return descripcionSoporteUrgencias;
	}
	/**
	 * @param descripcionSoporteUrgencias The descripcionSoporteUrgencias to set.
	 */
	public void setDescripcionSoporteUrgencias(String descripcionSoporteUrgencias)
	{
		this.descripcionSoporteUrgencias = descripcionSoporteUrgencias;
	}
	/**
	 * @return Returns the otrasNutricionesOrales.
	 */
	public Vector getOtrasNutricionesOrales()
	{
		return otrasNutricionesOrales;
	}
	/**
	 * @param otrasNutricionesOrales The otrasNutricionesOrales to set.
	 */
	public void setOtrasNutricionesOrales(Vector otrasNutricionesOrales)
	{
		this.otrasNutricionesOrales = otrasNutricionesOrales;
	}
	
	/**
	 * @return Retorna the finalizadaHojaNeuro.
	 */
	public String getFinalizadaHojaNeuro()
	{
		return finalizadaHojaNeuro;
	}
	
	/**
	 * @return Retorna mezcla.
	 */
	public int getMezcla()
	{
		return mezcla;
	}
	
	
	/**
	 * @param finalizadaHojaNeuro The finalizadaHojaNeuro to set.
	 */
	public void setFinalizadaHojaNeuro(String finalizadaHojaNeuro)
	{
		this.finalizadaHojaNeuro = finalizadaHojaNeuro;
	}
	
	/**
	 * @return Retorna the observacionesHojaNeuro.
	 */
	public String getObservacionesHojaNeuro()
	{
		return observacionesHojaNeuro;
	}
	
	/**
	 * @param observacionesHojaNeuro The observacionesHojaNeuro to set.
	 */
	public void setObservacionesHojaNeuro(String observacionesHojaNeuro)
	{
		this.observacionesHojaNeuro = observacionesHojaNeuro;
	}
	
	/**
	 * @return Retorna the presentaHojaNeuro.
	 */
	public String getPresentaHojaNeuro()
	{
		return presentaHojaNeuro;
	}
	
	/**
	 * @param presentaHojaNeuro The presentaHojaNeuro to set.
	 */
	public void setPresentaHojaNeuro(String presentaHojaNeuro)
	{
		this.presentaHojaNeuro = presentaHojaNeuro;
	}
	
	/**
	 * @param mezcla Asigna mezcla.
	 */
	public void setMezcla(int mezcla)
	{
		this.mezcla = mezcla;
	}
	
	/**
	 * @return Retorna descripcionDietaParenteral.
	 */
	public String getDescripcionDietaParenteral()
	{
		return descripcionDietaParenteral;
	}
	
	/**
	 * @param descripcionDietaParenteral Asigna descripcionDietaParenteral.
	 */
	public void setDescripcionDietaParenteral(String descripcionDietaParenteral)
	{
		this.descripcionDietaParenteral = descripcionDietaParenteral;
	}

	/**
	 * 
	 * @param con 
	 * @param orden
	 * @param solicitud
	 * @return
	 */
	public HashMap consultarMezclaModificar(Connection con, String orden, String solicitud)
	{
		return ordenMedicaDao.consultarMezclaModificar(con,orden,solicitud);
	}

	
	/**
	 * 
	 * @param con
	 * @param mezclaModificar
	 * @param paciente 
	 * @param medico 
	 */
	public boolean guardarModificacionMezcla(
			Connection con,
			HashMap mezclaModificar,
			UsuarioBasico medico,
			PersonaBasica paciente) throws IPSException
	{		
		return ordenMedicaDao.guardarModificacionMezcla(con,mezclaModificar,medico,paciente);
	}
	
	
	
	/**
	 * Guarda la informacion de la mezcla junto a la informacion NO POS que posea
	 * @param con
	 * @param mezclaModificar
	 * @param justificacionMap
	 * @param paciente 
	 * @param medico 
	 */
	public boolean guardarModificacionMezcla(
			Connection con,
			int numeroSolicitud,
			HashMap mezclaModificar,
			HashMap justificacionMap,
			HashMap medicamentosNoPosMap,
			HashMap medicamentosPosMap,
			HashMap sustitutosNoPosMap,
			HashMap diagnosticosDefinitivos,
			UsuarioBasico medico,
			PersonaBasica paciente,
			boolean modificacionRegistroEnfermeria) throws IPSException
	{
		FormatoJustArtNopos fjan= new FormatoJustArtNopos();		
		
		if(ordenMedicaDao.guardarModificacionMezcla(con,mezclaModificar,medico,paciente))
		{				
			for(int i=0; i<Utilidades.convertirAEntero(mezclaModificar.get("numRegistros").toString()); i++)
			{
				if(Utilidades.convertirAEntero(mezclaModificar.get("codigojustificacion_"+i).toString())<0 && 
						mezclaModificar.get("espos_"+i).toString().equals("false") && 
							Utilidades.convertirAEntero(mezclaModificar.get("volumen_"+i).toString())>0)
				{
					if(!modificacionRegistroEnfermeria && 
							justificacionMap.get(mezclaModificar.get("articulo_"+i).toString()+"_pendiente").equals("1"))
					{
						//frecuencia estaba en -1 int
						fjan.insertarJustificacion(	
											con,
											numeroSolicitud,
											ConstantesBD.codigoNuncaValido,
											justificacionMap,
											medicamentosNoPosMap,
											medicamentosPosMap,
											sustitutosNoPosMap,
											diagnosticosDefinitivos,
											Utilidades.convertirAEntero(mezclaModificar.get("articulo_"+i).toString()),										
											medico.getCodigoInstitucionInt(), 
											"", 
											ConstantesBD.continuarTransaccion,
											Utilidades.convertirAEntero(mezclaModificar.get("articulo_"+i).toString()),										 
											"", 
											mezclaModificar.get("volumen_"+i).toString(),
											"", 
											-1, 
											"", 
											"", 
											Utilidades.convertirAEntero(mezclaModificar.get("volumen_"+i).toString()), 
											"-1",
											medico.getLoginUsuario()
											);
					}
					else if(modificacionRegistroEnfermeria)
					{
						double subcuenta = Cargos.obtenerCodigoSubcuentaDetalleCargo(con, Utilidades.convertirAEntero(mezclaModificar.get("articulo_"+i).toString()), numeroSolicitud, ConstantesBD.codigoNuncaValido, true);
						
						UtilidadJustificacionPendienteArtServ.insertarJusNP(
								con, 
								numeroSolicitud, 
								Utilidades.convertirAEntero(mezclaModificar.get("articulo_"+i).toString()), 
								Utilidades.convertirAEntero(mezclaModificar.get("volumen_"+i).toString()),
								medico.getLoginUsuario(),
								true,
								false,
								Utilidades.convertirAEntero(subcuenta+""),"");					
					}			
				}
				else if(Utilidades.convertirAEntero(mezclaModificar.get("codigojustificacion_"+i).toString())>0 &&
							mezclaModificar.get("tiporegistro_"+i).toString().equals("BD") &&
								mezclaModificar.get("espos_"+i).toString().equals("false") &&
									!mezclaModificar.get("volumenold_"+i).toString().equals(mezclaModificar.get("volumen_"+i).toString()))
				{
					fjan.actualizarCantidadJustificacion(
														con,
														mezclaModificar.get("codigojustificacion_"+i).toString(),
														mezclaModificar.get("volumen_"+i).toString());
				}
			}
		}
		else
		{
			return false;
		}
		return true;
	}
	
	
	
	/**
	 * Validacion de las modificaciones sobre las mezclas
	 * @param Connection con
	 * @param HashMap mezclaModificar
	 * @param ActionErrors errores
	 * */
	public static ActionErrors validacionModificacionMezcla(
			Connection con,
			HashMap mapa,
			HashMap justificacionMap,
			PersonaBasica paciente,
			UsuarioBasico usuario,
			ActionErrors errores,
			boolean esModificacionEnfermera)
	{
		int num = Utilidades.convertirAEntero(mapa.get("numRegistros").toString());
		int valortotalvolumen = 0;
		
		//Recorre los articulos de la mezcla
		for(int i=0; i<num; i++)
		{
			if(Utilidades.convertirADouble(mapa.get("volumen_"+i).toString()) > 0 
					&& mapa.get("unidad_volumen_"+i).toString().equals("") 
						&& mapa.get("esmedicamento_"+i).toString().equals(ConstantesBD.acronimoSi))
			{
				errores.add("descripcion", new ActionMessage("errors.required", "La unidad de la Unidosis del Medicamento ["+mapa.get("nombrearticulo_"+i).toString().toLowerCase()+"] "));
			}
			else if(Utilidades.convertirAEntero(mapa.get("volumen_"+i).toString()) < 0)
			{
				mapa.put("volumen_"+i,"0");				
			}
			
			//Suma la cantidad total de unidades de los medicamentos
			if(Utilidades.convertirADouble(mapa.get("volumen_"+i).toString()) > 0)
				valortotalvolumen += Utilidades.convertirADouble(mapa.get("volumen_"+i).toString());
			
			//Validaciones para justificacion No Pos
			if(Utilidades.convertirAEntero(mapa.get("codigojustificacion_"+i).toString())<0  
					&& UtilidadesFacturacion.requiereJustificacioArt(con, paciente.getCodigoConvenio(),Utilidades.convertirAEntero(mapa.get("articulo_"+i)+""))
						&& mapa.get("espos_"+i).toString().equals("false") 
							&& Utilidades.convertirADouble(mapa.get("volumen_"+i).toString()) > 0 && !esModificacionEnfermera)
			{
				if(UtilidadesHistoriaClinica.validarEspecialidadProfesionalSalud(con, usuario, true)
						&& !justificacionMap.containsKey(mapa.get("articulo_"+i)+""+"_yajustifico") 
							&& !justificacionMap.containsKey(mapa.get("articulo_"+i)+""+"_pendiente"))
				{
					errores.add("Justificacion", new ActionMessage("errors.required", "Justificacion del articulo - "+mapa.get("nombrearticulo_"+i).toString().toLowerCase()));
				}
				if(!esModificacionEnfermera 
						&& !UtilidadesHistoriaClinica.validarEspecialidadProfesionalSalud(con, usuario, true))
				{
					errores.add("Justificacion", new ActionMessage("errors.warnings","No puede solicitar el Articulo No POS "+mapa.get("nombrearticulo_"+i).toString().toLowerCase()+" Ya que no cumple con la validacion médico especialista"));
				}
			}
		}
		
		//Si se realiza desde la funcionalidad de enfermeria se hace requerida la velocidad de infusion en el caso de que el valor 
		//total de todas las unidades de los medicamentos se mayor a cero
		if(esModificacionEnfermera 
				&& valortotalvolumen > 0 
					&& mapa.get("velocidadInfusion").toString().equals(""))
			errores.add("", new ActionMessage("errors.required","La Velocidad de Infusión de la Mezcla"));
					
		if(esModificacionEnfermera 
				&& valortotalvolumen > 0
					&& mapa.get("volumenTotal").toString().equals(""))				
			errores.add("", new ActionMessage("errors.required"," EL Volumen Total de la Mezcla "));
		
		//Se verifica la dosificación
		if(UtilidadTexto.isEmpty(mapa.get("dosificacion").toString()))				
			errores.add("", new ActionMessage("errors.required","La dosificación de la Mezcla"));
				
		return errores;
	}

	/**
	 * Por ahora enviamos un mapa con los atributos, de la mezcla a anular, ya que mas adelante, pueden hacerse otras cosas, y necesitar mas datos,
	 * que se pordrian encontrar en el mapa.
	 * @param con
	 * @param mezcla
	 */
	public boolean accionAnularMezcla(Connection con, HashMap mezcla)
	{
		return ordenMedicaDao.accionAnularMezcla(con,mezcla);
	}

	public HashMap consultarFechasHistoCuidadosEspe(Connection con, int codigoCuenta, int codigoCuentaAsocio)
	{
		return ordenMedicaDao.consultarFechasHistoCuidadosEspe(con,codigoCuenta, codigoCuentaAsocio);
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isFinalizarDietaEnfermeria() {
		return finalizarDietaEnfermeria;
	}
	
	/**
	 * 
	 * @param finalizarDietaEnfermeria
	 */
	public void setFinalizarDietaEnfermeria(boolean finalizarDietaEnfermeria) {
		this.finalizarDietaEnfermeria = finalizarDietaEnfermeria;
	}
	
	/**
	 * Metodo para consultar el estado del Parametro Interfaz Nutricion
	 * @param con
	 * @return
	 */
	public String consultarParametroInterfazNutricion(Connection con) 
	{
		return ordenMedicaDao.consultarParametroInterfazNutricion(con);
	}

	/**
	 * Metodo para consultar el Piso al que pertence una Cama
	 * @param con
	 * @param codigoCama
	 * @return
	 */
	public String consultarPisoCama(Connection con, int codigoCama) 
	{
		return ordenMedicaDao.consultarPisoCama(con, codigoCama);
	}
	
	/**
	 * Metodo para consultar el Numero de la cama comun para la interfaz
	 * @param con
	 * @param codigoCama
	 * @return
	 */
	public String consultarNumeroCama(Connection con, int codigoCama) 
	{
		return ordenMedicaDao.consultarNumeroCama(con, codigoCama);
	}
	
	/**
	 * Metodo para consultar la Fecha de la Orden Dieta
	 * @param con
	 * @param codigoEncabezado
	 * @return
	 */
	public String consultarFechaDieta(Connection con, int codigoEncabezado) 
	{
		return ordenMedicaDao.consultarFechaDieta(con, codigoEncabezado);
	}

	/**
	 * Metodo para consultar la Hora de la Orden Dieta
	 * @param con
	 * @param codigoEncabezado
	 * @return
	 */
	public String consultarHoraDieta(Connection con, int codigoEncabezado) 
	{
		return ordenMedicaDao.consultarHoraDieta(con, codigoEncabezado);
	}

	/**
	 * Metodo para consultar la Fecha de Grabacion de la Dieta
	 * @param con
	 * @param codigoEncabezado
	 * @return
	 */
	public String consultarFechaGrabacion(Connection con, int codigoEncabezado) 
	{
		return ordenMedicaDao.consultarFechaGrabacion(con, codigoEncabezado);
	}
	
	/**
	 * Metodo para consultar la Hora de Grabacion de la Dieta
	 * @param con
	 * @param codigoEncabezado
	 * @return
	 */
	public String consultarHoraGrabacion(Connection con, int codigoEncabezado) 
	{
		return ordenMedicaDao.consultarHoraGrabacion(con, codigoEncabezado);
	}

	/**
	 * Metodo para Consultar el campo VIP del Convenio asociado al Ingreso del Paciente
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public String consultarConvenioVip(Connection con, int codigoConvenio) 
	{
		return ordenMedicaDao.consultarConvenioVip(con, codigoConvenio);
	}

	/**
	 * Metodo para Consultar los tipos de dieta activos para la dieta actual del paciente
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public HashMap tiposNutricionOralActivo(Connection con, int codigoCuenta) 
	{
		return ordenMedicaDao.tiposNutricionOralActivo(con, codigoCuenta);
	}

	/**
	 * Metodo para Consultar la Descripcion de la Dieta del Paciente
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public String consultarDescripcionDieta(Connection con, int codigoCuenta) 
	{
		return ordenMedicaDao.consultarDescripcionDieta(con, codigoCuenta);
	}

	/**
	 * Método para consultar el arreglo de un campo de la seccion prescripcion diálisis según tipo de consulta
	 * @param con
	 * @param tipo consulta
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> cargarArregloPrescripcionDialisis(Connection con,int tipoConsulta,String parametroAdicional1)
	{
		HashMap campos = new HashMap();
		campos.put("tipoConsulta",tipoConsulta);
		campos.put("parametroAdicional1",parametroAdicional1);
		return ordenMedicaDao().cargarArregloPrescripcionDialisis(con, campos);
	}
	
	/**
	 * Método para insertar una prescripcion de diálisis
	 * @param con
	 * @param dialisis
	 * @return
	 */
	public int insertarPrescripcionDialisis(Connection con,DtoPrescripcionDialisis dialisis)
	{
		return ordenMedicaDao.insertarPrescripcionDialisis(con, dialisis);
	}
	
	/**
	 * Método implementado para cargar el histórico de prescripciones diálisis de un paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public ArrayList<DtoPrescripcionDialisis> getHistoricoPrescripcionDialisis(Connection con,int codigoPaciente)
	{
		HashMap campos = new HashMap();
		campos.put("codigoPaciente", codigoPaciente);
		campos.put("codigoHistoEnca", "");
		return ordenMedicaDao.getHistoricoPrescripcionDialisis(con, campos);
	}
	
	/**
	 * Método implementado para cargar el histórico de prescripciones diálisis de un paciente
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<DtoPrescripcionDialisis> getHistoricoPrescripcionDialisisStatic(Connection con,int codigoPaciente)
	{
		HashMap campos = new HashMap();
		campos.put("codigoPaciente", codigoPaciente);
		campos.put("codigoHistoEnca", "");
		return ordenMedicaDao().getHistoricoPrescripcionDialisis(con, campos);
	}
	
	/**
	 * Método que carga la prescripción de diálisis ingresada para un registro de orden médica específico
	 * @param con
	 * @param codigoHistoEnca
	 * @return
	 */
	public static DtoPrescripcionDialisis getPrescripcionDialisis(Connection con,String codigoHistoEnca)
	{
		HashMap campos = new HashMap();
		campos.put("codigoPaciente", ConstantesBD.codigoNuncaValido);
		campos.put("codigoHistoEnca", codigoHistoEnca);
		ArrayList<DtoPrescripcionDialisis> arreglo =  ordenMedicaDao().getHistoricoPrescripcionDialisis(con, campos);
		
		if(arreglo.size()>0)
			return arreglo.get(0);
		else
			return new DtoPrescripcionDialisis();
	}
	
	
	/**
	 * Consulta las observaciones realizadas a las mezclas 
	 * @param Connection con
	 * @param String numeroSolicitud 
	 * */
	public HashMap obtenerObservacionesMezcla(Connection con,String numeroSolicitud)
	{
		HashMap parametros = new HashMap();
		parametros.put("numeroSolicitud",numeroSolicitud);		
		return ordenMedicaDao().obtenerObservacionesMezcla(con, parametros);
	}
	
	
	/**
	 * Actualiza las observaciones realizadas a las mezclas 
	 * @param Connection con
	 * @param String descripcionDietaEnfermera
	 * @param String codigoPk 
	 * */
	public boolean actualizarObservacionesMezcla(Connection con,String descripcionDietaEnfermera, String codigoPkOrdenMedica)
	{
		HashMap parametros = new HashMap();
		parametros.put("descripcionDietaEnfermera",descripcionDietaEnfermera);
		parametros.put("codigoPk",codigoPkOrdenMedica);
		return ordenMedicaDao().actualizarObservacionesMezcla(con, parametros); 
	}
	
	
	
	/**
	 * Método implementado para modificar una prescripción de dialisis
	 * @param con
	 * @param dialisis
	 * @return
	 */
	public static int modificarPrescripcionDialisis(Connection con,DtoPrescripcionDialisis dialisis)
	{
		return ordenMedicaDao().modificarPrescripcionDialisis(con, dialisis);
	}

	/**
	 * 
	 * @param con
	 * @param mezcla
	 * @return
	 */
	public String consultarDosificacionMezcla(Connection con, int mezcla,int codigoCuenta, int cuentaAsocio) 
	{
		return ordenMedicaDao.consultarDosificacionMezcla(con, mezcla,codigoCuenta,cuentaAsocio);
	}

	/**
	 * @param velocidadInfusion the velocidadInfusion to set
	 */
	public void setVelocidadInfusion(String velocidadInfusion) {
		this.velocidadInfusion = velocidadInfusion;
	}

	/**
	 * @return the unidadVolumenTotal
	 */
	public String getUnidadVolumenTotal() {
		return unidadVolumenTotal;
	}

	/**
	 * @param unidadVolumenTotal the unidadVolumenTotal to set
	 */
	public void setUnidadVolumenTotal(String unidadVolumenTotal) {
		this.unidadVolumenTotal = unidadVolumenTotal;
	}

	/**
	 * @return the volumenTotal
	 */
	public String getVolumenTotal() {
		return volumenTotal;
	}

	/**
	 * @param volumenTotal the volumenTotal to set
	 */
	public void setVolumenTotal(String volumenTotal) {
		this.volumenTotal = volumenTotal;
	}

	/**
	 * 
	 * @param ingresoPaciente
	 * @param cuentaPaciente
	 * @param centroCostoPaciente
	 * @param cargarParametrizacion
	 * @return
	 */
	public static ArrayList<Object> cargarResultadoLaboratorios(Connection con,int ingresoPaciente, int cuentaPaciente, int centroCostoPaciente, boolean cargarParametrizacion,boolean esHistoricos)
	{
		return ordenMedicaDao().cargarResultadoLaboratorios(con,ingresoPaciente,cuentaPaciente,centroCostoPaciente,cargarParametrizacion,esHistoricos);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEncabezado
	 * @param resultadoLaboratorios
	 * @return
	 */
	public int insertaResultadosLaboratorios(Connection con,int codigoEncabezado,ArrayList<Object> resultadoLaboratorios) 
	{
		return ordenMedicaDao().insertaResultadosLaboratorios(con,codigoEncabezado,resultadoLaboratorios);
	}
	
	public  ArrayList<String> cargarOtrosDietaReproteHC(Connection con, int codigoCuenta){
		return ordenMedicaDao().cargarOtrosDietaReproteHC(con, codigoCuenta);
	}
	
	/**
	 * Se crea el registro de alerta para el ingreso de ordenes medicas según la sección indicada
	 * 
	 * @param con
	 * @param seccion
	 * @param cuenta
	 * @param medicoOrdena
	 * @return
	 */
	public ArrayList<Integer> registrarAlertaOrdenMedica(Connection con, int seccion, long cuenta, String medicoOrdena) {
		
		ArrayList<DtoRegistroAlertaOrdenesMedicas> listaRegistros = 
			new ArrayList<DtoRegistroAlertaOrdenesMedicas>();
		ArrayList<Integer> listaErrores = new ArrayList<Integer>();

		RegistroEnfermeriaDao registroEnfermeria = 
			DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroEnfermeriaDao();

		DtoRegistroAlertaOrdenesMedicas dtoRegistroAlert = new DtoRegistroAlertaOrdenesMedicas(); 

		dtoRegistroAlert.setCuenta(cuenta);
		dtoRegistroAlert.setMedicoOrdena(medicoOrdena);
		dtoRegistroAlert.setFechaOrden(Date.valueOf(Utilidades.capturarFechaBD(con)));
		dtoRegistroAlert.setHoraOrden(Utilidades.capturarHoraBD(con));
		dtoRegistroAlert.setActivo(true);
		dtoRegistroAlert.setTipoSeccionOrden(seccion);

		listaRegistros.add(dtoRegistroAlert);
		registroEnfermeria.insertarRegistroAlertaOrdenesMedicas(con, listaRegistros);

		return listaErrores;
	}
	
	/**
	 * @return the codigoEncabezadoSoporteRespira
	 */
	public int getCodigoEncabezadoSoporteRespira() {
		return codigoEncabezadoSoporteRespira;
	}

	/**
	 * @param codigoEncabezadoSoporteRespira the codigoEncabezadoSoporteRespira to set
	 */
	public void setCodigoEncabezadoSoporteRespira(int codigoEncabezadoSoporteRespira) {
		this.codigoEncabezadoSoporteRespira = codigoEncabezadoSoporteRespira;
	}

	/**
	 * @return the descripcionIndivSoporteRespira
	 */
	public String getDescripcionIndivSoporteRespira() {
		return descripcionIndivSoporteRespira;
	}

	/**
	 * @param descripcionIndivSoporteRespira the descripcionIndivSoporteRespira to set
	 */
	public void setDescripcionIndivSoporteRespira(String descripcionIndivSoporteRespira) {
		this.descripcionIndivSoporteRespira = descripcionIndivSoporteRespira;
	}
	
}
