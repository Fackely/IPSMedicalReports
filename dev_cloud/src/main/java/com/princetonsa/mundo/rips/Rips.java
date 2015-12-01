/*
 * Jun 10, 2005
 *
 */
package com.princetonsa.mundo.rips;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.InfoDatos;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.UtilidadFileUpload;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.interfaces.UtilidadBDInterfaz;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.RipsDao;
import com.princetonsa.mundo.cargos.Convenio;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;
import com.princetonsa.mundo.manejoPaciente.GeneracionAnexosForecat;
import com.servinte.axioma.fwk.exception.IPSException;


/**
 * @author Sebastián Gómez R
 *	Clase implementada para la generación de RIPS
 */
public class Rips {
	
	private static Logger logger = Logger.getLogger(Rips.class);
	/**
	 * DAO para el manejo de los RIPS
	 */
	private RipsDao ripsDao=null;
	 
	//********ATRIBUTOS DE BÚSQUEDA*******************
	/**
	 * convenio usado como parámetro de búsqueda en RIPS
	 */
	private int convenio;
	
	/**
	 * Tarifario Oficial usado para generar los rips
	 */
	private int tipoCodigo;
	/**
	 * fecha inicial de rango de generación de RIPS por factura
	 */
	private String fechaInicial;
	/**
	 * fecha final de rango de generación de RIPS por factura
	 */
	private String fechaFinal;
	/**
	 * numero de cuenta de cobro usado para generar RIPS por cuenta de de cobro
	 */
	private double numeroCuentaCobro;
	
	/**
	 * fecha de remisión de la generación de RIPS
	 */
	private String fechaRemision;
	
	/**
	 * Número de Remisión de la generación de RIPS
	 */
	private String numeroRemision;
	/**
	 * Institución en la que se van a hacer los archivos RIPS
	 *
	 */
	private ParametrizacionInstitucion institucion;
	
	/**
	 * Convenio al cual se le va a hacer los archivos RIPS
	 */
	private Convenio objConvenio;
	
	/**
	 * Indicador de parámetros generales que indica si se incluye información de facturación en los RIPS
	 */
	private boolean ripsConFactura;
	
	/**
	 *Indicador para saber si la búsqueda de la información se hace por facturas o por cuenta de cobro
	 */
	private boolean esFactura;
	
	/**
	 * Directorio donde se almacenarán los RIPS
	 */
	private File dirConvenio;
	
	/**
	 * HashMap que indica cuales fueron los archivos seleccionados para generar
	 */
	@SuppressWarnings("rawtypes")
	private HashMap seleccionArchivos;
	
	/**
	 * Archivo que indica inconsistencias en la generación de archivos
	 */
	private boolean huboInconsistencias;
	
	/**
	 * HashMap que almacena el contenido del archivo de inconsistencias
	 */
	@SuppressWarnings("rawtypes")
	private HashMap inconsistencias;
	
	/**
	 * Hashmap para almacenar el número de registros creados por archivo
	 *
	 */
	@SuppressWarnings("rawtypes")
	private HashMap numeroRegistrosCreados;
	
	/**
	 * Variable que indica el tipo de RIPS que se va a usar, Hasta ahora son:
	 *  'Cartera'
	 * 	'Consultorios'
	 */
	private String tipoRips;
	
	/**
	 * Número de la factura que se relaciona para los RIPS consultorios
	 */
	private String numeroFactura;
	
	/**
	 * Fecha de la factura que se relaciona para los RIPS consultorios
	 */
	private String fechaFactura;
	
	/**
	 * Objeto para almacenar los registros RIPS consultorios que van a ser almacenados en la
	 * tabla rips_consultorios
	 */
	@SuppressWarnings("rawtypes")
	private HashMap registrosRangos; 
	
	/**
	 * Login del usuario que almacena los rips consultorios
	 */
	private String loginUsuario;
	
	/**
	 * Variable que almacena el path donde se generaron los archivos RIPS
	 */
	private String pathGeneracion;
	
	/**
	 * Variable que indica si hubo información para generar RIPS
	 */
	private boolean huboRegistros;
	
	/**
	 * Códigos de los contratos de la cuenta de cobro capitacion seleccionada
	 */
	private Vector<Object> codigosContrato;
	
	/**
	 * Numeros de los contratos de la cuenta de cobro capitación seleccionada
	 */
	private Vector<Object> numerosContrato;
	
	/**
	 * Valor de la cuenta de cobro capitada
	 */
	private String valorCuenta;
	
	/**
	 * Mapa donde se almacena la información del mapa empresa institución
	 */
	private HashMap<String, Object> empresaInstitucion = new HashMap<String, Object>();
	
	/**
	 * Para saber si la aplicacion maneja multiempresa
	 */
	private boolean manejaMultiEmpresa;
	
	
	/**
	 * Campo de manejo de la consulta, el numero de envio se refiere a la tabla ax_rips
	 */
	private String numeroEnvio;
	
	
	/**
	 * Nit de la Entidad Asociada al convenio
	 */
	private String nitEntidad;
	
	/**
	 * numero de factura consultada en la interfaz ax_rips por el numero de envio dado
	 */
	private String nroFacturaAx;
	
	
	/**
	 * boolean de definicion si es una consulkta de archivos rips para la interfaz de ax_rips
	 */
	private boolean esAxRips;
	
	
	/**
	 * Bandera de informacion si fueron creados archivos forecat
	 */
	private boolean generoArchvsForecat;
	
	/**
	 * Path generacion archivos forecat
	 */
	private String pathGeneracionForecat;
	
	
	/**
	 * Mapa de resultados de la generacion de archivos forecat
	 */
	private HashMap<String, Object> resultadosForecat;
	
	
	/**
	 * Variable para almacenar los archivos generados para realizar su compresion en un zip
	 */
	private ArrayList<String> archivosGenerados;
	
	/**
	 * Variable para almacenar el nombre del archivo zip para la descarga
	 */
	private String nomZip;
	
	/**
	 * Indica si se genero el .zip o no???
	 */
	private boolean zip;
	
	/**
	 * Atributo para el manejo de los errores
	 */
	private ActionErrors errores = new ActionErrors();
	
	//*********************CONSTRUCTORES E INICIALIZADORES**************
	/**
	 * Constructor
	 */
	public Rips()
	{
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * método para inicializar los datos
	 *
	 */
	@SuppressWarnings("rawtypes")
	public void clean()
	{
		this.convenio=0;
		this.fechaInicial="";
		this.fechaFinal="";
		this.numeroCuentaCobro=0;
		this.ripsConFactura=false;
		this.esFactura=false;
		this.fechaRemision="";
		this.numeroRemision="";
		this.seleccionArchivos=new HashMap();
		this.huboInconsistencias=false;
		this.numeroRegistrosCreados=new HashMap();
		this.inconsistencias=new HashMap();
		this.tipoRips="Cartera";
		this.numeroFactura="";
		this.registrosRangos=new HashMap();
		this.loginUsuario="";
		this.fechaFactura="";
		this.pathGeneracion="";
		this.huboRegistros = true;
		this.tipoCodigo = -1;
		this.numeroRegistrosCreados=new HashMap();
		this.inconsistencias=new HashMap();
		this.valorCuenta = "";
		this.empresaInstitucion = new HashMap<String, Object>();
		this.manejaMultiEmpresa = false;
		this.numeroEnvio = "";
		this.nroFacturaAx = "";
		this.esAxRips = false;
		this.generoArchvsForecat = false;
		this.pathGeneracionForecat = "";
		this.resultadosForecat = new HashMap<String, Object>();
		this.nomZip ="";
		archivosGenerados = new ArrayList<String> ();
		this.zip = false;
		this.errores = new ActionErrors();
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (ripsDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			ripsDao = myFactory.getRipsDao();
		}
		
		
		
	}
	
	//******************MÉTODOS*************************************************
	/**
	 * Método para consultar los datos del archivo AF por Factura
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param convenio
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaFacturaAF(Connection con,String fechaInicial,String fechaFinal,int convenio,int codigoInstitucion, boolean esAxRip, String nroFacAx)
	{
		return ripsDao.consultaFacturaAF(con,fechaInicial,fechaFinal,convenio,codigoInstitucion, esAxRip , nroFacAx);		
	}
	
	/**
	 * Método para consultar los datos del archivo AF por cuenta de cobro
	 * @param con
	 * @param numeroCuentaCobro
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCuentaCobroAF(Connection con,double numeroCuentaCobro,int codigoInstitucion)
	{
		return ripsDao.consultaCuentaCobroAF(con,numeroCuentaCobro,codigoInstitucion);
	}
	
	/**
	 * Método para consultar los registros del archivo AD por factura
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param convenio
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaFacturaAD(Connection con,String fechaInicial,String fechaFinal,int convenio,int codigoInstitucion, boolean esAxRip, String nroFactura)
	{
		return ripsDao.consultaFacturaAD(con,fechaInicial,fechaFinal,convenio,codigoInstitucion,esAxRip, nroFactura);
	}
	
	/**
	 * Método para consultar los registros del archivo AD por cuenta de cobro
	 * @param con
	 * @param numeroCuentaCobro
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCuentaCobroAD(Connection con,double numeroCuentaCobro,int codigoInstitucion)
	{
		return ripsDao.consultaCuentaCobroAD(con,numeroCuentaCobro,codigoInstitucion);
	}
	
	/**
	 * Método para cosnultar los registros del archivo US por factura
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param convenio
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaFacturaUS(Connection con,String fechaInicial,String fechaFinal,int convenio,int codigoInstitucion, boolean esAxRip, String nroFactura)
	{
		 return ripsDao.consultaFacturaUS(con,fechaInicial,fechaFinal,convenio,codigoInstitucion,esAxRip,nroFactura);
	}
	
	/**
	 * Método para consultar los datos del archivo US por cuenta de cobro
	 * @param con
	 * @param numeroCuentaCobro
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaCuentaCobroUS(Connection con,double numeroCuentaCobro,int codigoInstitucion)
	{
		return ripsDao.consultaCuentaCobroUS(con,numeroCuentaCobro,codigoInstitucion);
	}
	
	/**
	 * Método para consultar los datos del archivo AC por factura
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param convenio
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaFacturaAC(Connection con,String fechaInicial,String fechaFinal,int convenio,int codigoInstitucion)
	{
		 return ripsDao.consultaFacturaAC(con,fechaInicial,fechaFinal,convenio,codigoInstitucion,this.tipoCodigo, this.esAxRips, this.nroFacturaAx);
	}
	
	/**
	 * Método para consultar los datos del archivo AC por cuenta de cobro
	 * @param con
	 * @param numeroCuentaCobro
	 * @return
	 */
	@SuppressWarnings({ "rawtypes" })
	public HashMap consultaCuentaCobroAC(Connection con,double numeroCuentaCobro,int codigoInstitucion)
	{
		return ripsDao.consultaCuentaCobroAC(con,numeroCuentaCobro,codigoInstitucion, this.tipoCodigo);
		
	}
	
	/**
	 * Método que consulta el contenido de un archivo por su prefijo
	 * @param codigoInstitucion
	 * @param prefijo
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap cargarArchivo(Connection con,int codigoInstitucion,String prefijo) throws IPSException
	{
		
		//objeto usado para llenar el contenido del archivo
		HashMap contenido=new HashMap();
		String separador=System.getProperty("file.separator");
		//***SE CARGA LA INSTITUCIÓN***************************
		institucion=new ParametrizacionInstitucion();
		institucion.cargar(con,codigoInstitucion);
		
		//***SE CARGA EL CONVENIO***************************
		objConvenio=new Convenio();
		objConvenio.cargarResumen(con,this.convenio);
		
		//****SE EDITA LA RUTA*******************************
		File directorio = new File(institucion.getPath());
		
		// Modificar el path si es un archivo forecat /Forecat
		// primero verificar si se crearon archivos forecat
		if (this.isGeneroArchvsForecat())
			//Se añade nuevo directorio para el convenio / Forecat
			dirConvenio=new File(directorio,"C"+this.convenio+separador+"FORECAT");
		else		
		{	
			if(!this.numeroEnvio.equals(""))
			{
				dirConvenio= new File(directorio,this.numeroEnvio+separador+this.nitEntidad);
			}
			else
			{
				//Se añade nuevo directorio para el convenio
				dirConvenio=new File(directorio,"C"+this.convenio);
			}
				
		}
		
		
		try
		{
			int contador=0;
			String cadena="";
			//******SE INICIALIZA ARCHIVO*************************
			String aux=this.revisarCaracteresEspeciales(this.numeroRemision);
			File archivo=new File(dirConvenio.getAbsolutePath(),prefijo+aux+".txt");
			FileReader stream=new FileReader(archivo); //se coloca false para el caso de que esté repetido
			BufferedReader buffer=new BufferedReader(stream);
			
			//********SE RECORRE LÍNEA POR LÍNEA**************
			cadena=buffer.readLine();
			while(cadena!=null)
			{
				//se almacena cada línea en el hashmap
				contenido.put(contador+"",cadena);
				contador++;
				cadena=buffer.readLine();
			}
			contenido.put("numElementos",contador+"");
			
			//***************CERRAR ARCHIVO****************************
			buffer.close();
		
			return contenido;
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo "+prefijo+" al cargarlo: "+e);
			return null;
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo "+prefijo+" al cargarlo: "+e);
			return null;
		}
		
	}
	
	/**
	 * Metodo de consulta de un numero de factura en la interfaz_rips por un numero de envio dado
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultarNroFacInterfaz (String numeroEnv, int institucion)
	{
		//		consultar el numero de factura referente al numero de envio dado
		UtilidadBDInterfaz objInterfaz = new UtilidadBDInterfaz();
		//int nroFactura = Utilidades.convertirAEntero(objInterfaz.consultarFacturasEnAxRips(this.numeroEnvio, this.institucion.getCodigo()));
		HashMap nroFactura = new HashMap();
		nroFactura=objInterfaz.consultarFacturasEnAxRips(numeroEnv, institucion);
		return nroFactura;
	}
	
	/**
	 * Metodo de actualizacion del campo de estado en la interfaz ax_rips
	 * @param estado
	 * @param numEnvio
	 * @param convenio 
	 * @param hora 
	 * @param fecha 
	 * @param inst
	 */
	public void actualizarEstadoAxRips ( String estado, String numEnvio, String convenio, String fecha, String hora, int inst)
	{
		//		consultar el numero de factura referente al numero de envio dado
		UtilidadBDInterfaz objInterfaz = new UtilidadBDInterfaz();
		//ResultadoBoolean resultado =  objInterfaz.actualizarEstadoAxRips(estado, this.numeroEnvio, this.institucion.getCodigo());
		ResultadoBoolean resultado =  objInterfaz.actualizarEstadoAxRips(estado, numEnvio, convenio, fecha, hora, inst);
		if (resultado.isTrue())
			logger.info("\n\n Estado en la interfaz ax_rips actualizado correctamente");
		else
			logger.info("\n\n El Estado en la interfaz ax_rips no fue actualizado correctamente");
	}
	
	/**
	 * metodo de consulta del numero del convenio de la factura
	 * @param con
	 * @param nroFact
	 * @return
	 */
	public int consultarConvenioFactura(Connection con, int nroFact)
	{
		return ripsDao.consultarConvenioFactura(con,nroFact );
	}
	
	/**
	 * metodo de consulta del tarifario de la factura
	 * @param con
	 * @param nroFact
	 * @return
	 */
	public int consulartConvenioXNroFactura(Connection con, int nroFact)
	{
		return ripsDao.consulartConvenioXNroFactura(con, nroFact);
	}
	
	/**
	 * Método centralizado para la generación de los archivos RIPS
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param convenio
	 * @param numCuentaCobro
	 * @param fechaRemision
	 * @param numeroRemision
	 * @param codigoInstitucion
	 * @param esFactura
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap generarArchivos(Connection con,int codigoInstitucion) throws IPSException
	{
		String separador=System.getProperty("file.separator");
		//vector de resultados
		HashMap resultados=new HashMap();
		//se limpian las inconsistencia
		this.inconsistencias=new HashMap();
		this.huboRegistros = true;
		//***SE CARGA LA INSTITUCIÓN***************************
		institucion=new ParametrizacionInstitucion();
		institucion.cargar(con,codigoInstitucion);
		
		
		// llamar la funcion d consulta de ax_rips y guardar el numero de la factura en una variable global.
		// verificar si el campo de numero de envio es vacio, si no lo es es una consulta de interfaz
		/*if (!this.numeroEnvio.equals("")){
			//consultar el numero de factura referente al numero de envio dado	
			// cargar el numero de la factura en la variable correpondiente
			//this.setNroFacturaAx(this.consultarNroFacInterfaz());
			
			// cargar tarifario o tipo convenio
			this.setTipoCodigo(ripsDao.consulartConvenioXNroFactura(con, this.nroFacturaAx));
			
			
			// cargar el convenio
			this.setConvenio(ripsDao.consultarConvenioFactura(con, this.nroFacturaAx));
			if (this.getNroFacturaAx() > 0)
			{
				// actualizar el campo de estado en la interfaz de ax_rips a 2 dado que la lectura se realiza correctamente
				this.actualizarEstadoAxRips("2");
			}
		}*/
			
		
		
		//***SE CARGA EL CONVENIO***************************
		objConvenio=new Convenio();
		
		
		// cargar el convenio cuando es una consulta a interfaz ax_rips
		if (!this.numeroEnvio.equals("")){
			//consultar el numero de convenio asociado al numero de factura consultado de la interfaz ax_rips
			logger.info("CONVENIO CONSULTADO POR FACTURA DE NUMERO DE ENVIO=>"+this.convenio);
			// cargar la variable de diferenciacion de que es una consulta de interfaz de ax_rips
			this.setEsAxRips(true);
		}
		// si no es una consulta a la interfaz se carga el convenio de forma normal, con el valor que trae del form
		objConvenio.cargarResumen(con,this.convenio);
		
		
		
		
		//**SE VERIFICA SI SE DEBE CARGAR LA INFORMACION DE LA EMPRESA INSTITUCION************
		this.manejaMultiEmpresa = UtilidadTexto.getBoolean(ValoresPorDefecto.getInstitucionMultiempresa(codigoInstitucion));
		if(this.manejaMultiEmpresa)
		{
			HashMap criterios = new HashMap();
			criterios.put("codigo1_", objConvenio.getEmpresaInstitucion()); 
			criterios.put("institucion3_", codigoInstitucion+"");
			this.empresaInstitucion = ParametrizacionInstitucion.consultarEmpresas(con, criterios);
			if(Utilidades.convertirAEntero(this.empresaInstitucion.get("numRegistros")+"", true)>0)
			{
				//Se cambian los datos de la institucion
				this.institucion.setCodMinSalud(this.empresaInstitucion.get("minSalud9_0").toString());
				this.institucion.setRazonSocial(this.empresaInstitucion.get("razonSocial4_0").toString());
				this.institucion.setIdentificacion(this.empresaInstitucion.get("tipoNit15_0").toString());
				this.institucion.setNit(this.empresaInstitucion.get("nit2_0").toString());
			}
			else
			{
				logger.error("multi empresa no parametrizada para el convenoi "+objConvenio.getNombre());
				resultados.put("error","error.rips.convenioSinEmpresaInstitucion");
				return resultados;
			}
		}
		//*************************************************************************************
		
			
		//*****VALIDACIÓN DE LA RUTA***************
		
		//caso en que la ruta sea nula (no se haya definido)
		if(institucion.getPath()==null)
			institucion.setPath("");
				
		File directorio = new File(institucion.getPath());
		
		
		//se verifica que el directorio exista, sea directorio y sea absoluto
		if(!directorio.isDirectory()||!directorio.exists()||!directorio.isAbsolute())
		{
			logger.error("PATH inválido: "+institucion.getPath());
			resultados.put("error","error.rips.rutaInvalida");
			return resultados;
		}
		
		//se añade nuevo directorio para el convenio
		if(!this.numeroEnvio.equals(""))
			dirConvenio= new File(directorio,this.numeroEnvio+separador+this.nitEntidad);
		else
			dirConvenio=new File(directorio,"C"+this.convenio);
		
		
		if(!dirConvenio.isDirectory()&&!dirConvenio.exists())
		{
			if(!dirConvenio.mkdirs())
			{
				logger.error("Error creando directorio en generarRIPS: "+dirConvenio.getAbsolutePath());				
				resultados.put("error","error.rips.directorioConvenio");
				return resultados;
			}
			
		}
		this.pathGeneracion=dirConvenio.getAbsolutePath();
		//********ACTUALIZACIÓN DE TABLA RIPS CONSULTORIOS*******
		
		if(this.tipoRips.equals("Consultorios"))
		{
			int respConsultorios=this.actualizarDatosRips(
					con,this.getNumeroFactura(),
					this.getFechaFactura(),this.getFechaRemision(),
					this.getLoginUsuario(),this.getRegistrosRangos()
					);
			
			
			if(respConsultorios<1)
			{
				logger.error("Error: no hay datos RIPS para generar archivos");	
				if(respConsultorios==-1)
					resultados.put("error","error.rips.sinDatos");
				else
					resultados.put("error","error.rips.problemasDatos");
				return resultados;
			}
			
		}
		
		
		//*****GENERACIÓN DE LOS ARCHIVOS*******************************
		
		//generación del archivo AC
		if(this.getSeleccionArchivos().get(ConstantesBD.ripsAC)!=null)
			if((this.getSeleccionArchivos().get(ConstantesBD.ripsAC)+"").equals("true"))
				resultados.put(ConstantesBD.ripsAC,this.generarAC(con));
		
		//generacion del archivo AP
		if(this.getSeleccionArchivos().get(ConstantesBD.ripsAP)!=null)
			if((this.getSeleccionArchivos().get(ConstantesBD.ripsAP)+"").equals("true"))
				resultados.put(ConstantesBD.ripsAP,this.generarAP(con));
			
		//generacion del archivo AH	
		if(this.getSeleccionArchivos().get(ConstantesBD.ripsAH)!=null)
			if((this.getSeleccionArchivos().get(ConstantesBD.ripsAH)+"").equals("true"))
				resultados.put(ConstantesBD.ripsAH,this.generarAH(con));
			
		//generación del archivo AU
		if(this.getSeleccionArchivos().get(ConstantesBD.ripsAU)!=null)
			if((this.getSeleccionArchivos().get(ConstantesBD.ripsAU)+"").equals("true"))
				resultados.put(ConstantesBD.ripsAU,this.generarAU(con));
		
		//generación del archivo AM
		if(this.getSeleccionArchivos().get(ConstantesBD.ripsAM)!=null)
			if((this.getSeleccionArchivos().get(ConstantesBD.ripsAM)+"").equals("true"))
				resultados.put(ConstantesBD.ripsAM,this.generarAM(con));
		
		//generación del archivo AT
		if(this.getSeleccionArchivos().get(ConstantesBD.ripsAT)!=null)
			if((this.getSeleccionArchivos().get(ConstantesBD.ripsAT)+"").equals("true"))
				resultados.put(ConstantesBD.ripsAT,this.generarAT(con));
		
		//generación del archivo AN
		if(this.getSeleccionArchivos().get(ConstantesBD.ripsAN)!=null)
			if((this.getSeleccionArchivos().get(ConstantesBD.ripsAN)+"").equals("true"))
				resultados.put(ConstantesBD.ripsAN,this.generarAN(con));
		
		//se revisa si de los archivos seleccionados se generó algún registro
		//cuando no se haya generado inconsistencias
		if((this.tipoRips.equals("Cartera")||this.tipoRips.equals("Capitacion"))&&!this.huboInconsistencias)
		{
			huboRegistros = verificarArchivosSeleccionados();
		}	
		resultados.put(ConstantesBD.ripsUS,this.generarUS(con));
		int reg= resultados.size();
		if (resultados.get("US")!=null)
		{huboRegistros=true;}
		if(huboRegistros)
		{
			///generación de los archivos básicos
			if(this.tipoRips.equals("Cartera")||this.tipoRips.equals("Consultorios"))
				resultados.put(ConstantesBD.ripsAF,this.generarAF(con));
			
			resultados.put(ConstantesBD.ripsAD,this.generarAD(con));
						
			logger.info("HUBO INCONSISTENCIAS DESDE EL MUNDO? "+this.huboInconsistencias);
			///generacion del archivo de inconsistencias
			if(this.huboInconsistencias)
			{
				resultados.put(ConstantesBD.ripsInconsistencias,this.generarInconsistencias());
			}
			else
			{
				File archivoIncon=new File(dirConvenio.getAbsolutePath(),ConstantesBD.ripsInconsistencias+this.numeroRemision+".txt");
				if(archivoIncon.exists())
					archivoIncon.delete();
			}
			
			//Se generarn los archivos básicos después de generadas las inconsistencias
			if(this.tipoRips.equals("Capitacion"))
			{
				resultados.put(ConstantesBD.ripsAF,this.generarAF(con));
				
				//Se verifica si hubo inconsistencias y el archivo no se ha generado
				//Nota* Quiere decir que hubo inconsistencias en el archivo
				if(this.huboInconsistencias&&resultados.get(ConstantesBD.ripsInconsistencias)==null)
					resultados.put(ConstantesBD.ripsInconsistencias, "../ripsCapitacion/ripsCapitacion.do?estado=detalle&archivo=Incon");
			}
		    
			//generación del archivo CT
			resultados.put(ConstantesBD.ripsCT,this.generarCT());
		}
		else
		{
			this.huboInconsistencias = false;
		}
			
		
		
		//*******************************************************************************************************
		//********************************** GENERACION AUTOMATICA DE ARCHIVOS FORECAT **************************
		// verificar que la opcion de generacion de archivos forecat en la generacion de archivos rips este activa
		logger.info("¿GENERACION AUTOMATICA DE ARCHIVO FORECAT? *"+ValoresPorDefecto.getGenForecatRips(institucion.getCodigo())+"*");
		if (UtilidadTexto.getBoolean(ValoresPorDefecto.getGenForecatRips(institucion.getCodigo())))
		{
			if(this.esFactura)			
				// si se generaaron archivos forecats
				this.setGeneroArchvsForecat(this.generacionArchivosForecat(con,resultados, true));
			else
				//listadoAC=this.consultaCuentaCobroAC(con,this.numeroCuentaCobro,institucion.getCodigo());
				//		 si se generaaron archivos forecats
				//this.setGeneroArchvsForecat(this.generacionArchivosForecat(con,resultados));
				// generar forecat para cuentas cobro
				this.setGeneroArchvsForecat(this.generacionArchivosForecat(con,resultados, false));
				
		} // fin if de verificacion de generacion de archivos forecat con rips.
		
		
		
		//	------------------	GENERO COPIA EN .ZIP PARA PERMITIR LA DESCARGA  ----------------------------------------------------------
		
		String ubicando = ValoresPorDefecto.getFilePath();
		
		logger.info("-----------------BACKUPS--------------");
		logger.info("*****************  ARRAY LIST ARCHIVOS GENERADOS\n"+archivosGenerados);
		
		int tamanoArray = archivosGenerados.size();
		logger.info("*****************  Tamaño Array ----->"+tamanoArray);
		
		String cadena="";
		String cadenaFinal="";
		for(int i=0; i<tamanoArray;i++)
		{
			cadena=this.getPathGeneracion()+"/"+archivosGenerados.get(i)+" ";
			cadenaFinal = cadenaFinal+cadena;
		}
		//logger.info("Cadena Final"+cadenaFinal);
		
		// SE TRAE LA FECHA ACTUAL PARA GENERAR EL ARCHIVO ZIP
		String splitFecha = UtilidadFecha.getFechaActual();
		String [] formFecha =splitFecha.split("/");
		splitFecha = formFecha[0]+formFecha[1]+formFecha[2];
		
		//	SE TRAE LA FECHA ACTUAL PARA GENERAR EL ARCHIVO ZIP
		String splitHora = UtilidadFecha.getHoraActual();
		
		//String [] formHora= splitFecha.split(":");
		//splitHora = formHora[0]+formHora[1];
		
		logger.info("Fecha "+splitFecha);
		logger.info("Hora "+splitHora);
		
		nomZip = "Rips"+splitFecha+"_"+splitHora+".zip";
		
		logger.info("NOMBRE ZIP "+nomZip);
		logger.info("zip -j "+ubicando+nomZip+" "+cadenaFinal);
		
		if(BackUpBaseDatos.EjecutarComandoSO("zip -j "+ubicando+nomZip+" "+cadenaFinal) != -1)
		{
			logger.info("Ejecuto el comando del SO...................................");
			this.setZip(UtilidadFileUpload.existeArchivo(ubicando, nomZip));
		}	
		else
			this.setZip(false);
		
		logger.info("ZIP EXITOSO?????->"+this.isZip());
		
		//		------------------	 FIN GENERACION COPIA EN .ZIP PARA PERMITIR LA DESCARGA  ----------------------------------------------
		
		return resultados;
	}

	
	/**
	 * Metodo de emulacion del action de generacion de anexos forecat.
	 * 
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean generacionArchivosForecat (Connection con, HashMap<String, Object> resultados, boolean esFac)
	{
		logger.info("\n\n\n ============================ Se generaran los archivos FORECAT automaticamente =============================== \n\n\n");
		//		*******************************************************************************************************
		//********************************** GENERACION AUTOMATICA DE ARCHIVOS FORECAT **************************
		// verificar que la opcion de generacion de archivos forecat en la generacion de archivos rips este activa 
		
		//if (ValoresPorDefecto.getG)
		//{
			// instanciar el mundo de generacion de archivos forecat, y realizar la generacion de archivos
			GeneracionAnexosForecat mundoForecat = new GeneracionAnexosForecat();
			
		// verificar si se debe generar archivos forecats para factura o para cuentas cobro
		if (esFac){	
			// cargar valores necesarios
			// si el campo de numero de envio fue tramitado entonces se debe obviar la carga de los atributos de fecha inicial y 
			// fecha final, si no se debe realizar todo igual
			if (this.numeroEnvio.equals(""))
			{
				mundoForecat.setConvenio(this.convenio);
				mundoForecat.setTipoCodigo(this.tipoCodigo);
				mundoForecat.setFechaInicial(this.fechaInicial);
				mundoForecat.setFechaFinal(this.fechaFinal);
				mundoForecat.setFechaRemision(this.fechaRemision);
				mundoForecat.setNumeroRemision(this.numeroRemision);
			}
			else
			{
				mundoForecat.setConvenio(this.convenio);
				mundoForecat.setTipoCodigo(this.tipoCodigo);			
				mundoForecat.setFechaRemision(UtilidadFecha.getFechaActual());
				mundoForecat.setNumeroRemision(this.numeroEnvio);
				mundoForecat.setNumeroFactura(Utilidades.convertirAEntero(this.nroFacturaAx));
				
				// variable de diferenciacion de generacion de archivos forecat
				// si es true la consulta debe de ser enfocada a la interfaz de ax_rips y filtrada solamente por el numero de factura
				mundoForecat.setEsAxRips(true);
			}
			
		}		
		else
		{
			// llenar datos para generacion de forectas por cuentas cobro
			mundoForecat.setConvenio(this.convenio);
			mundoForecat.setTipoCodigo(this.tipoCodigo);
			mundoForecat.setFechaRemision(this.fechaRemision);
			mundoForecat.setNumeroRemision(this.numeroRemision);
			// cargar el numero de cuenta cobro por el cual se filtraran las consultas en la generacion de los forecats
			mundoForecat.setNumeroCuentaCobro(this.numeroCuentaCobro);	
			
			// cargar al mundo de forecat la bandera de que si es forecats para cuentas cobro
			mundoForecat.setEsCuentaCobro(true);			
			
		}
		
		
		//		*********************************
		//			 ademas cargar la variable bandera que se refier a que es una generacion automatica de forecats.
		//*********************************
		mundoForecat.setEsRipsForecatAuto(true);
		
		// realizar la generacion de archivos
		// llamado a método del mundo forecat para la generacion de los archivos FORECAT
		this.setResultadosForecat(mundoForecat.generarArchivos(con,institucion.getCodigo()));
		
		//se verifica si se encontraron registros en la generación FORECAT
		if (mundoForecat.isHuboRegistros())
		{
			//Se verifica si hubo errores en la generación de FORECAT
			if (resultadosForecat.get("error")!= null)
			{
				logger.error("Error: problemas generando Forecat " + resultadosForecat.get("error").toString());	
				resultados.put("error",resultadosForecat.get("error").toString());
			}
			else
			{
				resultados.put("forecat",mundoForecat.getPathGeneracion());
				//this.setPathGeneracion(this.getPathGeneracion() + )
				
				// si se generaaron archivos forecats
				//this.setGeneroArchvsForecat(true);
				
				// cargar path archivos forecats
				this.setPathGeneracionForecat(mundoForecat.getPathGeneracion());
				
				
				return true;
			}
		}// fin verificacion de generacion de archivos forecats para facturas o para cuentas cobro
		
		//Si había errores de forecat se añaden a los de RIPS
		if(!mundoForecat.getErrores().isEmpty())
		{
			Iterator iterador = mundoForecat.getErrores().get();
			while(iterador.hasNext())
				this.errores.add("",(ActionMessage)iterador.next());
		}
		
		
		//} // fin if de verificacion de generacion de archivos forecat con rips.
		return false;
	}
	
	
	/**
	 * Método que verifica si de los archivos seleccionados se generaron registros
	 * para saber si se generan los archivos RIPS básicos
	 * @return
	 */
	private boolean verificarArchivosSeleccionados() 
	{
		boolean exito = false;
		
		if(this.getSeleccionArchivos().get(ConstantesBD.ripsAC)!=null)
			if((this.getSeleccionArchivos().get(ConstantesBD.ripsAC)+"").equals("true"))
				if(Utilidades.convertirAEntero(this.numeroRegistrosCreados.get(ConstantesBD.ripsAC)+"")>0)
					exito = true;
		
		if(this.getSeleccionArchivos().get(ConstantesBD.ripsAH)!=null)
			if((this.getSeleccionArchivos().get(ConstantesBD.ripsAH)+"").equals("true"))
				if(Utilidades.convertirAEntero(this.numeroRegistrosCreados.get(ConstantesBD.ripsAH)+"")>0)
					exito = true;
		
		if(this.getSeleccionArchivos().get(ConstantesBD.ripsAM)!=null)
			if((this.getSeleccionArchivos().get(ConstantesBD.ripsAM)+"").equals("true"))
				if(Utilidades.convertirAEntero(this.numeroRegistrosCreados.get(ConstantesBD.ripsAM)+"")>0)
					exito = true;
			
		if(this.getSeleccionArchivos().get(ConstantesBD.ripsAN)!=null)
			if((this.getSeleccionArchivos().get(ConstantesBD.ripsAN)+"").equals("true"))
				if(Utilidades.convertirAEntero(this.numeroRegistrosCreados.get(ConstantesBD.ripsAN)+"")>0)
					exito = true;
		
		if(this.getSeleccionArchivos().get(ConstantesBD.ripsAP)!=null)
			if((this.getSeleccionArchivos().get(ConstantesBD.ripsAP)+"").equals("true"))
				if(Utilidades.convertirAEntero(this.numeroRegistrosCreados.get(ConstantesBD.ripsAP)+"")>0)
					exito = true;
		
		if(this.getSeleccionArchivos().get(ConstantesBD.ripsAT)!=null)
			if((this.getSeleccionArchivos().get(ConstantesBD.ripsAT)+"").equals("true"))
				if(Utilidades.convertirAEntero(this.numeroRegistrosCreados.get(ConstantesBD.ripsAT)+"")>0)
					exito = true;
		
		if(this.getSeleccionArchivos().get(ConstantesBD.ripsAU)!=null)
			if((this.getSeleccionArchivos().get(ConstantesBD.ripsAU)+"").equals("true"))
				if(Utilidades.convertirAEntero(this.numeroRegistrosCreados.get(ConstantesBD.ripsAU)+"")>0)
					exito = true;
		
		return exito;
	}
	
	/**
	 * Método para generar el archivo AT
	 * @param con
	 * @return ruta para ingresar al resumen del archivo
	 * si retorna nulo es que hubo error en la edición del fichero
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String generarAT(Connection con) 
	{
		HashMap listadoAT = new HashMap();
		String registroAT="";
		int numeroRegistros; //número de registros consultado desde la BD
		int contadorRegistros=0; //cuenta del número de registros insertados en el fichero
		int contadorFactura=0; //contador de registros por factura
		String numeroFacturaAnterior=""; //permite llevar el control de los registros de una solo factura
		boolean presenciaInconsistenciaGeneral=false;//variable boolean para el manejo de la inconsistencia general
		
		String aux=""; //variable auxiliar para la revisión de caracteres especiales en los campos texto
		String tipoCargo = ""; //variable para almacenar el tipo de cargo (articulos o servicios)
		
		//ejecucion de la consulta de datos
		//RIPS CARTERA ****************************************************************
		
		// Modificacion ************************************* Interfaz AX RIPS
		// verificar si el campo de numero de envio esta tramitado, si no lo esta realizar la consulta normalmente
		if (this.numeroEnvio.equals(""))
		{
		
			if(this.tipoRips.equals("Cartera"))
				//listadoAT = ripsDao.consultaAT(con,this.fechaInicial,this.fechaFinal,this.numeroCuentaCobro,this.convenio,institucion.getCodigo(),this.tipoCodigo,this.esFactura);
				listadoAT = ripsDao.consultaAT(con,this.fechaInicial,this.fechaFinal,this.numeroCuentaCobro,this.convenio,institucion.getCodigo(),this.tipoCodigo,this.esFactura, false, this.nroFacturaAx);
			//RIPS CAPITACION ****************************************************************
			else if(this.tipoRips.equals("Capitacion"))
				listadoAT = this.consultaCapitacionAT(con, institucion.getCodigo());
		}
		else
		{
			listadoAT = ripsDao.consultaAT(con,this.fechaInicial,this.fechaFinal,this.numeroCuentaCobro,this.convenio,institucion.getCodigo(),this.tipoCodigo,this.esFactura, true, this.nroFacturaAx);		
		}
		
		numeroRegistros=Utilidades.convertirAEntero(listadoAT.get("numRegistros")+"");
		try
		{
			//apertura de archivo AT
			aux=this.revisarCaracteresEspeciales(this.numeroRemision);
			File archivoAT=new File(dirConvenio.getAbsolutePath(),ConstantesBD.ripsAT+aux+".txt");
			FileWriter streamAT=new FileWriter(archivoAT,false); //se coloca false para el caso de que esté repetido
			BufferedWriter bufferAT=new BufferedWriter(streamAT);
			
			String nombreAT = ConstantesBD.ripsAT+aux+".txt";
			
			for(int i=0;i<numeroRegistros;i++)
			{
				//si no hubo inconsistencias entonces se hace una inserción del registro en el archivo
				//se verifica si el registro ha cambiado de factura
				//en el caso de que ya haya cambiado de factura ya se pueden insertar 
				//todos los registros relacionados con la factura 
				if(numeroFacturaAnterior.equals(""))
					numeroFacturaAnterior=listadoAT.get("consecutivo_"+i)+"";
				else if(!numeroFacturaAnterior.equals(listadoAT.get("consecutivo_"+i)+""))
				{
					
					bufferAT.write(registroAT);
					contadorRegistros+=contadorFactura;
					contadorFactura=0;
					numeroFacturaAnterior=listadoAT.get("consecutivo_"+i)+"";
					registroAT = "";
				}
				
				//se toma el tipo de cargo
				tipoCargo = listadoAT.get("tipo_cargo_"+i) + "";
				
				if(contadorRegistros>0||contadorFactura>0)
				{
					registroAT+="\r\n";
				}
				
				//edicion de registro
				//CAMPO1 *************NÚMERO DE LA FACTURA**************************************
				if(this.ripsConFactura)
				{
					if((listadoAT.get("consecutivo_"+i)+"").trim().equals(""))
					{
						this.editarInconsistencia(listadoAT,
								ConstantesBD.ripsAT,i,
								"número de la factura",
								"campo sin información");
						this.huboInconsistencias=true;
						//registroAT+="                    ";
					}
					else
					{
						aux = (listadoAT.get("consecutivo_"+i)+"").trim();
						if(aux.trim().length()>20)
							aux = aux.trim().substring(0,20);
						
						//registroAT+=this.editarEspacios(aux,aux.trim().length(),20,false);
						registroAT+=aux.trim();
					}
				}
				/*else
				{
					registroAT+="                    ";
				}*/
				
				//CAMPO 2 ***********CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD****************
				if(!presenciaInconsistenciaGeneral)
				{
					if(institucion.getCodMinSalud().trim().equals(""))
					{
						this.editarInconsistencia(listadoAT,
								ConstantesBD.ripsAT,i,
								"código del prestador de servicios de salud",
								"campo sin información (para todas las facturas)");
						this.huboInconsistencias=true;
						presenciaInconsistenciaGeneral=true;
						registroAT+=",";
					}
					else
					{
						aux = this.revisarCaracteresEspeciales(institucion.getCodMinSalud());
						if(aux.trim().length()>12)
							aux = aux.substring(0,12);
						//registroAT+=","+this.editarEspacios(aux.trim(),aux.trim().length(),12,false);
						registroAT+=","+aux.trim();
						
					}
				}
				else
					registroAT+=",";
				
				//CAMPO 3**********TIPO DE IDENTIFICACION DEL USUARIO****************************
				if((listadoAT.get("tipo_identificacion_"+i)+"").equals("")||
					listadoAT.get("tipo_identificacion_"+i)==null)
				{
					this.editarInconsistencia(listadoAT,
							ConstantesBD.ripsAT,i,
							"tipo identificación usuario",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAT+=",";
				}
				else
				{
					aux = listadoAT.get("tipo_identificacion_"+i)+"";
					if(aux.equals("CC")||aux.equals("CE")||aux.equals("PA")||aux.equals("RC")||aux.equals("TI")||aux.equals("AS")||
						aux.equals("MS")||aux.equals("NU"))
						//registroAT+=","+this.editarEspacios(aux.trim(),aux.trim().length(),2,false);
						registroAT+=","+aux.trim();
					else
					{
						this.editarInconsistencia(listadoAT,
								ConstantesBD.ripsAT,i,
								"tipo identificación usuario",
								"campo con datos inválidos");
						this.huboInconsistencias=true;
						registroAT+=",";
					}
				}
				
				//CAMPO 4********NÚMERO DE IDENTIFICACIÓN DEL USUARIO************************************
				if((listadoAT.get("numero_identificacion_"+i)+"").equals("")||
					listadoAT.get("numero_identificacion_"+i)==null)
				{
					this.editarInconsistencia(listadoAT,
							ConstantesBD.ripsAT,i,
							"número identificación usuario",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAT+=",";
				}
				else
				{
					aux = listadoAT.get("numero_identificacion_"+i)+"";
					if(aux.trim().length()>20)
						aux = aux.substring(0,20);
					aux=this.revisarCaracteresEspeciales(aux);
					//registroAT+=","+this.editarEspacios(aux.trim(),aux.trim().length(),20,false);
					registroAT+=","+aux.trim();
					
				}
				
				
				//CAMPO 5*******NÚMERO DE AUTORIZACIÓN*************************************************
				if(listadoAT.get("numero_autorizacion_"+i)==null)
					aux = "";
				else
					aux = listadoAT.get("numero_autorizacion_"+i) + "";
				if(aux.trim().length()>15)
					aux = aux.substring(0,15);
				aux=this.revisarCaracteresEspeciales(aux);
				//registroAT+=","+this.editarEspacios(aux.trim(),aux.trim().length(),15,false);
				registroAT+=","+aux.trim();
				
				//CAMPO 6 ****TIPO DE SERVICIO**************************************************
				if((listadoAT.get("tipo_servicio_"+i)+"").equals("")||
						listadoAT.get("tipo_servicio_"+i)==null)
					{
						this.editarInconsistencia(listadoAT,
								ConstantesBD.ripsAT,i,
								"tipo de servicio",
								"campo con datos inválidos");
						this.huboInconsistencias=true;
						registroAT+=",";
					}
					else
					{
						aux = listadoAT.get("tipo_servicio_"+i)+"";
						//registroAT+=","+this.editarEspacios(aux.trim(),aux.trim().length(),1,false);
						registroAT+=","+aux.trim();
					}
				
				
				
				//CAMPO 7*****CÓDIGO DEL SERVICIO*******************************************************
				//solo es requerido para servicios
				if(tipoCargo.equals(ConstantesBD.codigoTipoCargoServicios+""))
				{
					if(((listadoAT.get("codigo_servicio_"+i)+"").equals("")||
							listadoAT.get("codigo_servicio_"+i)==null))
					{
						this.editarInconsistencia(listadoAT,
								ConstantesBD.ripsAT,i,
								"código del servicio",
								"campo sin información");
						this.huboInconsistencias=true;
						registroAT+=",";
					}
					else
					{
						aux = listadoAT.get("codigo_servicio_"+i)+"";
						if(aux.trim().length()>20)
							aux.substring(0,20);
						
						aux=this.revisarCaracteresEspeciales(aux);
						//registroAT+=","+this.editarEspacios(aux.trim(),aux.trim().length(),20,false);
						registroAT+=","+aux.trim();
						
					}
				}
				else
					registroAT+=",";
				
				
				//CAMPO 8 ***NOMBRE DEL SERVICIO*********************************
				if((listadoAT.get("nombre_servicio_"+i)+"").equals("")||
					listadoAT.get("nombre_servicio_"+i)==null)
				{
					this.editarInconsistencia(listadoAT,
							ConstantesBD.ripsAT,i,
							"nombre del servicio",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAT+=",";
				}
				else
				{
					aux = listadoAT.get("nombre_servicio_"+i)+"";
					if(aux.trim().length()>60)
						aux = aux.substring(0,60);
					
					aux=this.revisarCaracteresEspeciales(aux);
					//registroAT+=","+this.editarEspacios(aux.trim(),aux.trim().length(),60,false);
					registroAT+=","+aux.trim();
					
				}
				
				//CAMPO 9******* CANTIDAD**********************************************************
				if(this.ripsConFactura)
				{
					if((listadoAT.get("cantidad_"+i)+"").trim().equals(""))
					{
						this.editarInconsistencia(listadoAT,
								ConstantesBD.ripsAT,i,
								"cantidad",
								"campo sin información");
						this.huboInconsistencias=true;
						registroAT+=",";
					}
					else
					{
						aux = (listadoAT.get("cantidad_"+i)+"");
						if(aux.trim().length()>5)
							aux.substring(0,5);
						
						//registroAT+=","+this.editarEspacios(aux.trim(),aux.trim().length(),5,true);
						registroAT+=","+aux.trim();
						
					}
				}
				else
				{
					registroAT+=",";
				}
				
				//CAMPO 10 ****** VALOR UNITARIO DEL MATERIAL Ó INSUMO**************************************
				if(this.ripsConFactura)
				{
					if((listadoAT.get("valor_unitario_"+i)+"").trim().equals(""))
					{
						this.editarInconsistencia(listadoAT,
								ConstantesBD.ripsAT,i,
								"valor unitario",
								"campo sin información");
						this.huboInconsistencias=true;
						registroAT+=",";
					}
					else
					{
						aux = (listadoAT.get("valor_unitario_"+i)+"");
						aux = UtilidadTexto.formatearValores(aux,"######0.00");
						
						//Se quitan los decimales en ceros
						if(aux.endsWith(".00"))
							aux= aux.replace(".00", "");
						
						
						if(aux.trim().length()>15)
							aux = aux.substring(0,15);
						
						//registroAT+=","+this.editarEspacios(aux.trim(),aux.trim().length(),15,true);
						registroAT+=","+aux.trim();
						
					}
				}
				else
				{
					registroAT+=",";
				}
				
				//CAMPO 11 *****VALOR TOTAL DEL MATERIAL Ó INSUMO********************************************
				if(this.ripsConFactura)
				{
					if((listadoAT.get("valor_total_"+i)+"").trim().equals(""))
					{
						this.editarInconsistencia(listadoAT,
								ConstantesBD.ripsAT,i,
								"valor total",
								"campo sin información");
						this.huboInconsistencias=true;
						registroAT+=",";
					}
					else
					{
						aux = (listadoAT.get("valor_total_"+i)+"");
						aux = UtilidadTexto.formatearValores(aux,"######0.00");
						
						//Se quitan los decimales en ceros
						if(aux.endsWith(".00"))
							aux = aux.replace(".00", "");
						
						if(aux.trim().length()>15)
							aux = aux.substring(0,15);
						//registroAT+=","+this.editarEspacios(aux.trim(),aux.trim().length(),15,true);
						registroAT+=","+aux.trim();
						
					}
				}
				else
				{
					registroAT+=",";
				}
				
				//*********FIN******************
				
				
				contadorFactura++;
				
			}
			
			
			bufferAT.write(registroAT);
			contadorRegistros+=contadorFactura;	
			
			
			//se almacena el número de registros creados para el archivo CT
			this.numeroRegistrosCreados.put(ConstantesBD.ripsAT,contadorRegistros+"");
			
			bufferAT.close();
			
			if(contadorRegistros<1)
			{
				//archivo sin registros
				archivoAT.delete();
				return null;
			}
			{	
				//LLENO EL ARRAY LIST PARA GENERAR LA DESCARGA
				archivosGenerados.add(nombreAT);
				getArchivosGenerados().add(nombreAT);
			}
			
			//SEGÚN EL TIPO DE RIPS SE RETORNA EL PATH ESPECÍFICO
			//RIPS CARTERA
			if(this.tipoRips.equals("Cartera"))
			{
				if(esFactura)
					return "../ripsFactura/ripsFactura.do?estado=detalle&archivo=AT&nitEntidad="+this.nitEntidad;
				else
					return "../ripsCuentaCobro/ripsCuentaCobro.do?estado=detalle&archivo=AT&nitEntidad="+this.nitEntidad;
			}
			//RIPS CAPITACION
			else
				return "../ripsCapitacion/ripsCapitacion.do?estado=detalle&archivo=AT&nitEntidad="+this.nitEntidad;
			
			
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo AT al generarlo: "+e);
			this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo AT: "+e));
			return null;
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo AT: "+e);
			this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo AT: "+e));
			return null;
		}
	}
	
	/**
	 * Método para generar el archivo AM
	 * @param con
	 * @return ruta para ingresar al resumen del archivo
	 * si retorna nulo es que hubo error en la edición del fichero
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String generarAM(Connection con) 
	{
		HashMap listadoAM;
		String registroAM="";
		int numeroRegistros; //número de registros consultado desde la BD
		int contadorRegistros=0; //cuenta del número de registros insertados en el fichero
		int contadorFactura=0; //contador de registros por factura
		String numeroFacturaAnterior=""; //permite llevar el control de los registros de una solo factura
		boolean presenciaInconsistenciaGeneral=false;//variable boolean para el manejo de la inconsistencia general
		
		String aux=""; //variable auxiliar para la revisión de caracteres especiales en los campos texto
	
		boolean esPos = false; //variables para saber si el articulo es  pos o no pos
		
		
		//		 verificar si es una consulta para la interfaz de ax_rips
		if (this.numeroEnvio.equals(""))
		{			
			//ejecucion de la consulta de datos
			//RIPS CARTERA****************************************
			if(this.tipoRips.equals("Cartera"))
				listadoAM = ripsDao.consultaAM(con,this.fechaInicial,this.fechaFinal,this.numeroCuentaCobro,this.convenio,institucion.getCodigo(),this.esFactura, false, this.nroFacturaAx);
			//RIPS CAPITACION***********************************
			else
				listadoAM = this.consultaCapitacionAM(con, institucion.getCodigo());
		}
		else
		{
			listadoAM = ripsDao.consultaAM(con,this.fechaInicial,this.fechaFinal,this.numeroCuentaCobro,this.convenio,institucion.getCodigo(),this.esFactura, true, this.nroFacturaAx);
		}
		
		numeroRegistros=Utilidades.convertirAEntero(listadoAM.get("numRegistros")+"");
		try
		{
			//apertura de archivo AM
			aux=this.revisarCaracteresEspeciales(this.numeroRemision);
			File archivoAM=new File(dirConvenio.getAbsolutePath(),ConstantesBD.ripsAM+aux+".txt");
			FileWriter streamAM=new FileWriter(archivoAM,false); //se coloca false para el caso de que esté repetido
			BufferedWriter bufferAM=new BufferedWriter(streamAM);
			
			String nombreAM = ConstantesBD.ripsAM+aux+".txt";
			
			for(int i=0;i<numeroRegistros;i++)
			{
				//si no hubo inconsistencias entonces se hace una inserción del registro en el archivo
				//se verifica si el registro ha cambiado de factura
				//en el caso de que ya haya cambiado de factura ya se pueden insertar 
				//todos los registros relacionados con la factura 
				if(numeroFacturaAnterior.equals(""))
					numeroFacturaAnterior=listadoAM.get("consecutivo_"+i)+"";
				else if(!numeroFacturaAnterior.equals(listadoAM.get("consecutivo_"+i)+""))
				{
					bufferAM.write(registroAM);
					contadorRegistros+=contadorFactura;
					contadorFactura=0;
					numeroFacturaAnterior=listadoAM.get("consecutivo_"+i)+"";
					registroAM = "";
				}
				
				
				esPos = UtilidadTexto.getBoolean(listadoAM.get("es_pos_"+i).toString());
				
				if(contadorRegistros>0||contadorFactura>0)
				{
					registroAM+="\r\n";
				}
				
				
				//edicion de registro
				//CAMPO1 *************NÚMERO DE LA FACTURA**************************************
				if(this.ripsConFactura)
				{
					if((listadoAM.get("consecutivo_"+i)+"").trim().equals(""))
					{
						this.editarInconsistencia(listadoAM,
								ConstantesBD.ripsAM,i,
								"número de la factura",
								"campo sin información");
						this.huboInconsistencias=true;
						//registroAM+="                    ";
					}
					else
					{
						aux = (listadoAM.get("consecutivo_"+i)+"").trim(); 
						if(aux.length()>20)
							aux = aux.substring(0,20);
						
						//registroAM+=this.editarEspacios(aux,aux.length(),20,false);
						registroAM+=aux;
					}
				}
				/*else
				{
					registroAM+="                    ";
				}*/
				
				//CAMPO 2 ***********CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD****************
				if(!presenciaInconsistenciaGeneral)
				{
					if(institucion.getCodMinSalud().trim().equals(""))
					{
						this.editarInconsistencia(listadoAM,
								ConstantesBD.ripsAM,i,
								"código del prestador de servicios de salud",
								"campo sin información (para todas las facturas)");
						this.huboInconsistencias=true;
						presenciaInconsistenciaGeneral=true;
						registroAM+=",";
					}
					else
					{
						aux = this.revisarCaracteresEspeciales(institucion.getCodMinSalud());
						if(aux.trim().length()>12)
							aux = aux.substring(0,12);
						
						//registroAM+=","+this.editarEspacios(aux.trim(),aux.trim().length(),12,false);
						registroAM+=","+aux.trim();
					}
				}
				else
					registroAM+=",";
				
				//CAMPO 3**********TIPO DE IDENTIFICACION DEL USUARIO****************************
				if((listadoAM.get("tipo_identificacion_"+i)+"").equals("")||
					listadoAM.get("tipo_identificacion_"+i)==null)
				{
					this.editarInconsistencia(listadoAM,
							ConstantesBD.ripsAM,i,
							"tipo identificación usuario",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAM+=",";
				}
				else
				{
					aux = listadoAM.get("tipo_identificacion_"+i)+"";
					if(aux.equals("CC")||aux.equals("CE")||aux.equals("PA")||aux.equals("RC")||aux.equals("TI")||aux.equals("AS")||
						aux.equals("MS")||aux.equals("NU"))
						//registroAM+=","+this.editarEspacios(aux.trim(),aux.trim().length(),2,false);
						registroAM+=","+aux.trim();
					else
					{
						this.editarInconsistencia(listadoAM,
								ConstantesBD.ripsAM,i,
								"tipo identificación usuario",
								"campo con datos inválidos");
						this.huboInconsistencias=true;
						registroAM+=",";
					}
				}
				
				//CAMPO 4********NÚMERO DE IDENTIFICACIÓN DEL USUARIO************************************
				if((listadoAM.get("numero_identificacion_"+i)+"").equals("")||
					listadoAM.get("numero_identificacion_"+i)==null)
				{
					this.editarInconsistencia(listadoAM,
							ConstantesBD.ripsAM,i,
							"número identificación usuario",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAM+=",";
				}
				else
				{
					aux = listadoAM.get("numero_identificacion_"+i)+"";
					if(aux.trim().length()>20)
						aux = aux.substring(0,20);
					
					aux=this.revisarCaracteresEspeciales(aux);
					//registroAM+=","+this.editarEspacios(aux.trim(),aux.trim().length(),20,false);
					registroAM+=","+aux.trim();
					
				}
				
				
				//CAMPO 5*******NÚMERO DE AUTORIZACIÓN*************************************************
				if(listadoAM.get("numero_autorizacion_"+i)==null)
					aux = "";
				else
					aux = listadoAM.get("numero_autorizacion_"+i) + "";
				if(aux.trim().length()>15)
					aux = aux.substring(0,15);
				
				aux=this.revisarCaracteresEspeciales(aux);
				//registroAM+=","+this.editarEspacios(aux.trim(),aux.trim().length(),15,false);
				registroAM+=","+aux.trim();
				
				
				//CAMPO 6*****CÓDIGO DEL MEDICAMENTO*******************************************************
				if(((listadoAM.get("codigo_medicamento_"+i)+"").equals("")||
						listadoAM.get("codigo_medicamento_"+i)==null))
				{
					this.editarInconsistencia(listadoAM,
							ConstantesBD.ripsAM,i,
							"código del medicamento",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAM+=",";
				}
				else
				{
					aux = listadoAM.get("codigo_medicamento_"+i)+"";
					if(aux.trim().length()>20)
						aux = aux.substring(0,20);
					aux=this.revisarCaracteresEspeciales(aux);
					//registroAM+=","+this.editarEspacios(aux.trim(),aux.trim().length(),20,false);
					registroAM+=","+aux.trim();
					
				}
				
				
				//CAMPO 7 ***TIPO DE MEDICAMENTO***********************************************
				aux = listadoAM.get("tipo_"+i) + "";
				//registroAM+=","+this.editarEspacios(aux.trim(),aux.trim().length(),1,false);
				registroAM+=","+aux.trim();
				
				//CAMPO 8 ***NOMBRE GENÉRICO DEL MEDICAMENTO*********************************
				if((listadoAM.get("nombre_"+i)+"").equals("")||
					listadoAM.get("nombre_"+i)==null)
				{
					this.editarInconsistencia(listadoAM,
							ConstantesBD.ripsAM,i,
							"nombre genérico del medicamento",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAM+=",";
				}
				else
				{
					aux = listadoAM.get("nombre_"+i)+"";
					if(aux.trim().length()>30)
						aux = aux.substring(0,30);
					
					aux=this.revisarCaracteresEspeciales(aux);
					//registroAM+=","+this.editarEspacios(aux.trim(),aux.trim().length(),30,false);					
					registroAM+=","+aux.trim();
				}
				//CAMPO 9****FORMA FARMACEÚTICA****************************************************
				//solo es requerido para medicamentos NO POS
				if(!esPos)
				{
					if((listadoAM.get("forma_farmaceutica_"+i)+"").equals("")||
						listadoAM.get("forma_farmaceutica_"+i)==null)
					{
						this.editarInconsistencia(listadoAM,
								ConstantesBD.ripsAM,i,
								"forma farmaceútica",
								"campo sin información");
						this.huboInconsistencias=true;
						registroAM+=",";
					}
					else
					{
						aux = listadoAM.get("forma_farmaceutica_"+i)+"";
						if(aux.trim().length()>20)
							aux = aux.substring(0,20);
						
						aux=this.revisarCaracteresEspeciales(aux);
						//registroAM+=","+this.editarEspacios(aux.trim(),aux.trim().length(),20,false);
						registroAM+=","+aux.trim();
						
					}
				}
				else
					registroAM+=",";
				
				//CAMPO 10 ****CONCENTRACIÓN DEL MEDICAMENTO***********************************************
				//solo es requerido para medicamentos NO POS
				if(!esPos)
				{
					if((listadoAM.get("concentracion_"+i)+"").equals("")||
						listadoAM.get("concentracion_"+i)==null)
					{
						this.editarInconsistencia(listadoAM,
								ConstantesBD.ripsAM,i,
								"concentración del medicamento",
								"campo sin información");
						this.huboInconsistencias=true;
						registroAM+=",";
					}
					else
					{
						aux = listadoAM.get("concentracion_"+i)+"";
						if(aux.trim().length()>20)
							aux = aux.substring(0,20);
						
						aux=this.revisarCaracteresEspeciales(aux);
						//registroAM+=","+this.editarEspacios(aux.trim(),aux.trim().length(),20,false);
						registroAM+=","+aux.trim();
						
					}
				}
				else
					registroAM+=",";
				
				//CAMPO 11******* UNIDAD DE MEDIDA DEL MEDICAMENTO**************************************
				//solo es requerido para medicamentos NO POS
				if(!esPos)
				{
					if((listadoAM.get("unidad_medida_"+i)+"").equals("")||
						listadoAM.get("unidad_medida_"+i)==null)
					{
						this.editarInconsistencia(listadoAM,
								ConstantesBD.ripsAM,i,
								"unidad de medida",
								"campo sin información");
						this.huboInconsistencias=true;
						registroAM+=",";
					}
					else
					{
						aux = listadoAM.get("unidad_medida_"+i)+"";
						if(aux.trim().length()>20)
							aux = aux.substring(0,20);
						
						aux=this.revisarCaracteresEspeciales(aux);
						//registroAM+=","+this.editarEspacios(aux.trim(),aux.trim().length(),20,false);
						registroAM+=","+aux.trim();
						
					}
				}
				else
					registroAM+=",";
				
				//CAMPO 12******* NÚMERO DE UNIDADES**********************************************************
				if(this.ripsConFactura)
				{
					if((listadoAM.get("numero_unidades_"+i)+"").trim().equals(""))
					{
						this.editarInconsistencia(listadoAM,
								ConstantesBD.ripsAM,i,
								"número de unidades",
								"campo sin información");
						this.huboInconsistencias=true;
						registroAM+=",";
					}
					else
					{
						aux = (listadoAM.get("numero_unidades_"+i)+"");
						if(aux.trim().length()>5)
							aux = aux.substring(0,5);
						
						//registroAM+=","+this.editarEspacios(aux.trim(),aux.trim().length(),5,true);
						registroAM+=","+aux.trim();
						
					}
				}
				else
				{
					registroAM+=",";
				}
				
				//CAMPO 13 ****** VALOR UNITARIO DEL MEDICAMENTO**************************************
				if(this.ripsConFactura)
				{
					if((listadoAM.get("valor_unitario_"+i)+"").trim().equals(""))
					{
						this.editarInconsistencia(listadoAM,
								ConstantesBD.ripsAM,i,
								"valor unitario",
								"campo sin información");
						this.huboInconsistencias=true;
						registroAM+=",";
					}
					else
					{
						aux = (listadoAM.get("valor_unitario_"+i)+"");
						aux = UtilidadTexto.formatearValores(aux,"######0.00");
						
						//Se quitan los decimales en ceros
						if(aux.endsWith(".00"))
							aux = aux.replace(".00", "");
						
						if(aux.trim().length()>15)
							aux = aux.substring(0,15);
						
						//registroAM+=","+this.editarEspacios(aux.trim(),aux.trim().length(),15,true);
						registroAM+=","+aux.trim();
						
					}
				}
				else
				{
					registroAM+=",";
				}
				
				//CAMPO 14 *****VALOR TOTAL DEL MEDICAMENTO********************************************
				if(this.ripsConFactura)
				{
					if((listadoAM.get("valor_total_"+i)+"").trim().equals(""))
					{
						this.editarInconsistencia(listadoAM,
								ConstantesBD.ripsAM,i,
								"valor total",
								"campo sin información");
						this.huboInconsistencias=true;
						registroAM+=",";
					}
					else
					{
						aux = (listadoAM.get("valor_total_"+i)+"");
						aux = UtilidadTexto.formatearValores(aux,"######0.00");
						
						//Se quitan los decimales en ceros
						if(aux.endsWith(".00"))
							aux = aux.replace(".00", "");
						
						if(aux.trim().length()>15)
							aux = aux.substring(0,15);
						
						//registroAM+=","+this.editarEspacios(aux.trim(),aux.trim().length(),15,true);
						registroAM+=","+aux.trim();
					}
				}
				else
				{
					registroAM+=",";
				}
				
				//*********FIN******************
				
				
				contadorFactura++;
				
				
					
				
			}
			
			bufferAM.write(registroAM);
			contadorRegistros+=contadorFactura;
			
			//se almacena el número de registros creados para el archivo CT
			this.numeroRegistrosCreados.put(ConstantesBD.ripsAM,contadorRegistros+"");
			
			bufferAM.close();
			
			if(contadorRegistros<1)
			{
				//archivo sin registros
				archivoAM.delete();
				return null;
			}
			else
			{	
				//LLENO EL ARRAY LIST PARA GENERAR LA DESCARGA
				logger.info("Archivo AM"+nombreAM);
				archivosGenerados.add(nombreAM);
			}
			
			//SEGÚN EL TIPO DE RIPS SE RETORNA EL PATH ESPECÍFICO
			//RIPS CARTERA
			if(this.tipoRips.equals("Cartera"))
			{
				if(esFactura)
					return "../ripsFactura/ripsFactura.do?estado=detalle&archivo=AM&nitEntidad="+this.nitEntidad;
				else
					return "../ripsCuentaCobro/ripsCuentaCobro.do?estado=detalle&archivo=AM&nitEntidad="+this.nitEntidad;
			}
			//RIPS CAPITACION
			else
				return "../ripsCapitacion/ripsCapitacion.do?estado=detalle&archivo=AM&nitEntidad="+this.nitEntidad;
				
			
			
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo AM al generarlo: "+e);
			this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo AM: "+e));
			return null;
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo AM: "+e);
			this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo AM: "+e));
			return null;
		}
	}
	
	/**
	 * Método para generar el archivo AU
	 * @param con
	 * @return ruta para ingresar al resumen del archivo
	 * si retorna nulo es que hubo error en la edición del fichero
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String generarAU(Connection con) 
	{//TODO
		HashMap listadoAU;
		String registroAU="";
		int numeroRegistros; //número de registros consultado desde la BD
		int contadorRegistros=0; //cuenta del número de registros insertados en el fichero
		int contadorFactura=0; //contador de registros por factura
		String numeroFacturaAnterior=""; //permite llevar el control de los registros de una solo factura
		boolean presenciaInconsistenciaGeneral=false;//variable boolean para el manejo de la inconsistencia general
		
		String aux=""; //variable auxiliar para la revisión de caracteres especiales en los campos texto
		
		//		 verificar si es una consulta para la interfaz de ax_rips
		if (this.numeroEnvio.equals(""))
		{
			//ejecucion de la consulta de datos
			//RIPS CARTERA**************************
			if(this.tipoRips.equals("Cartera"))
				listadoAU = ripsDao.consultaAU(con,this.fechaInicial,this.fechaFinal,this.numeroCuentaCobro,this.convenio,institucion.getCodigo(),this.esFactura, false, this.nroFacturaAx);
			else
				listadoAU = this.consultaCapitacionAU(con, institucion.getCodigo());
		}
		else
		{
			listadoAU = ripsDao.consultaAU(con,this.fechaInicial,this.fechaFinal,this.numeroCuentaCobro,this.convenio,institucion.getCodigo(),this.esFactura, true, this.nroFacturaAx);
		}
		
		numeroRegistros=Utilidades.convertirAEntero(listadoAU.get("numRegistros")+"");
		try
		{
			//apertura de archivo AU
			aux=this.revisarCaracteresEspeciales(this.numeroRemision);
			File archivoAU=new File(dirConvenio.getAbsolutePath(),ConstantesBD.ripsAU+aux+".txt");
			FileWriter streamAU=new FileWriter(archivoAU,false); //se coloca false para el caso de que esté repetido
			BufferedWriter bufferAU=new BufferedWriter(streamAU);
			
			String nombreAU = ConstantesBD.ripsAU+aux+".txt";
			
			for(int i=0;i<numeroRegistros;i++)
			{
				//si no hubo inconsistencias entonces se hace una inserción del registro en el archivo
				//se verifica si el registro ha cambiado de factura
				//en el caso de que ya haya cambiado de factura ya se pueden insertar 
				//todos los registros relacionados con la factura 
				if(numeroFacturaAnterior.equals(""))
				{
					
					numeroFacturaAnterior=listadoAU.get("consecutivo_"+i)+"";
				}
				else if(!numeroFacturaAnterior.equals(listadoAU.get("consecutivo_"+i)+""))
				{
					bufferAU.write(registroAU);
					contadorRegistros+=contadorFactura;
					contadorFactura=0;
					numeroFacturaAnterior=listadoAU.get("consecutivo_"+i)+"";
					registroAU = "";
				}
				
				if(contadorRegistros>0||contadorFactura>0)
				{
					registroAU+="\r\n";
				}
				
				//edicion de registro
				//CAMPO1 *************NÚMERO DE LA FACTURA**************************************
				if(this.ripsConFactura)
				{
					if((listadoAU.get("consecutivo_"+i)+"").trim().equals(""))
					{
						this.editarInconsistencia(listadoAU,
								ConstantesBD.ripsAU,i,
								"número de la factura",
								"campo sin información");
						this.huboInconsistencias=true;
						//registroAU+="                    ";
					}
					else
					{
						aux = (listadoAU.get("consecutivo_"+i)+"").trim();
						if(aux.length()>20)
							aux = aux.substring(0,20);
						
						//registroAU+=this.editarEspacios(aux,aux.length(),20,false);
						registroAU+=aux;
						
					}
				}
				/*else
					registroAU+="                    ";*/
				
				//CAMPO 2 ***********CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD****************
				if(!presenciaInconsistenciaGeneral)
				{
					if(institucion.getCodMinSalud().trim().equals(""))
					{
						this.editarInconsistencia(listadoAU,
								ConstantesBD.ripsAU,i,
								"código del prestador de servicios de salud",
								"campo sin información (para todas las facturas)");
						this.huboInconsistencias=true;
						presenciaInconsistenciaGeneral=true;
						registroAU+=",";
					}
					else
					{
						aux = this.revisarCaracteresEspeciales(institucion.getCodMinSalud());
						if(aux.trim().length()>12)
							aux = aux.substring(0,12);
						
						//registroAU+=","+this.editarEspacios(aux.trim(),aux.trim().length(),12,false);
						registroAU+=","+aux.trim();
						
					}
				}
				else
					registroAU+=",";
				
				//CAMPO 3**********TIPO DE IDENTIFICACION DEL USUARIO****************************
				if((listadoAU.get("tipo_identificacion_"+i)+"").equals("")||
					listadoAU.get("tipo_identificacion_"+i)==null)
				{
					this.editarInconsistencia(listadoAU,
							ConstantesBD.ripsAU,i,
							"tipo identificación usuario",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAU+=",";
				}
				else
				{
					aux = listadoAU.get("tipo_identificacion_"+i)+"";
					if(aux.equals("CC")||aux.equals("CE")||aux.equals("PA")||aux.equals("RC")||aux.equals("TI")||aux.equals("AS")||
						aux.equals("MS")||aux.equals("NU"))
						//registroAU+=","+this.editarEspacios(aux.trim(),aux.trim().length(),2,false);
						registroAU+=","+aux.trim();
					else
					{
						this.editarInconsistencia(listadoAU,
								ConstantesBD.ripsAU,i,
								"tipo identificación usuario",
								"campo con datos inválidos");
						this.huboInconsistencias=true;
						registroAU+=",";
					}
				}
				
				//CAMPO 4********NÚMERO DE IDENTIFICACIÓN DEL USUARIO************************************
				if((listadoAU.get("numero_identificacion_"+i)+"").equals("")||
					listadoAU.get("numero_identificacion_"+i)==null)
				{
					this.editarInconsistencia(listadoAU,
							ConstantesBD.ripsAU,i,
							"número identificación usuario",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAU+=",";
				}
				else
				{
					aux = listadoAU.get("numero_identificacion_"+i)+"";
					if(aux.trim().length()>20)
						aux = aux.substring(0,20);
					
					aux=this.revisarCaracteresEspeciales(aux);
					//registroAU+=","+this.editarEspacios(aux.trim(),aux.trim().length(),20,false);
					registroAU+=","+aux.trim();
					
				}
				
				
				//CAMPO 5*******FECHA INGRESO USUARIO A OBSERVACIÓN***********************************
				if((listadoAU.get("fecha_ingreso_"+i)+"").trim().equals("")||
					listadoAU.get("fecha_ingreso_"+i)==null)
				{
					this.editarInconsistencia(listadoAU,
							ConstantesBD.ripsAU,i,
							"fecha ingreso del usuario a observación",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAU+=",";
				}
				else
				{
					aux = listadoAU.get("fecha_ingreso_"+i)+"";
					if(UtilidadFecha.validarFecha(aux))
					{
						//registroAU+=","+this.editarEspacios(aux.trim(),aux.trim().length(),10,false);
						registroAU+=","+aux.trim();
					}
					else
					{
						this.editarInconsistencia(listadoAU,
								ConstantesBD.ripsAU,i,
								"fecha ingreso del usuario a observación",
								"campo con datos inválidos");
						this.huboInconsistencias=true;
						registroAU+=",";
					}
				}
				
				//CAMPO 6******HORA INGRESO USUARIO A OBSERVACIÓN**********************************
				if((listadoAU.get("hora_ingreso_"+i)+"").trim().equals("")||
					listadoAU.get("hora_ingreso_"+i)==null)
				{
					this.editarInconsistencia(listadoAU,
							ConstantesBD.ripsAU,i,
							"hora ingreso del usuario a observación",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAU+=",";
				}
				else
				{
					aux = listadoAU.get("hora_ingreso_"+i)+"";
					if(UtilidadFecha.validacionHora(aux).puedoSeguir)
					{
						aux = UtilidadFecha.convertirHoraACincoCaracteres(aux);
						//registroAU+=","+this.editarEspacios(aux.trim(),aux.trim().length(),5,false);
						registroAU+=","+aux.trim();
					}
					else
					{
						this.editarInconsistencia(listadoAU,
								ConstantesBD.ripsAU,i,
								"hora ingreso del usuario a observación",
								"campo con datos inválidos");
						this.huboInconsistencias=true;
						registroAU+=",";
					}
				}
				
				//CAMPO 7******NÚMERO DE AUTORIZACIÓN***********************************************
				if(listadoAU.get("numero_autorizacion_"+i)==null)
					aux = "";
				else
					aux = listadoAU.get("numero_autorizacion_"+i)+"";
				aux=this.revisarCaracteresEspeciales(aux);
				//registroAU+=","+this.editarEspacios(aux.trim(),aux.trim().length(),15,false);
				registroAU+=","+aux.trim();
				
				//CAMPO 8******CAUSA EXTERNA****************************************************
				try
				{
					aux = (listadoAU.get("causa_externa_"+i)+"");
					Utilidades.convertirAEntero(aux);
				}
				catch(Exception e)
				{
					aux = "";
				}
				if(aux.equals(""))
				{
					this.editarInconsistencia(listadoAU,
							ConstantesBD.ripsAU,i,
							"causa externa",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAU+=",";
				}
				else
				{
					//la causa externa debe estar entre 1 y 15
					if(Utilidades.convertirAEntero(aux)>=1&&Utilidades.convertirAEntero(aux)<=15)
						registroAU+=","+this.editarCeros(aux.trim(),aux.trim().length(),2);
					else
					{
						this.editarInconsistencia(listadoAU,
								ConstantesBD.ripsAU,i,
								"causa externa",
								"campo con datos inválidos");
						this.huboInconsistencias=true;
						registroAU+=",";
					}
						
				}
				
				//CAMPO 9 ******DIAGNÓSTICO A LA SALIDA****************************************
				if((listadoAU.get("diag_egreso_"+i)+"").equals("")||
					listadoAU.get("diag_egreso_"+i)==null)
				{
					this.editarInconsistencia(listadoAU,
							ConstantesBD.ripsAU,i,
							"diagnóstico a la salida",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAU+=",";
				}
				else
				{
					aux = listadoAU.get("diag_egreso_"+i)+"";
					aux=this.revisarCaracteresEspeciales(aux);
					if(aux.trim().length()>4)
						aux = aux.substring(0,4);
					
					//registroAU+=","+this.editarEspacios(aux.trim(),aux.trim().length(),4,false);
					registroAU+=","+aux.trim();
						
				}
				
				//CAMPO 10 ***DIAGNÓSTICO RELACIONADO Nº 1 A LA SALIDA**************************************
				if(listadoAU.get("diag_egreso_rel1_"+i)==null)
					aux = "";
				else
					aux = listadoAU.get("diag_egreso_rel1_"+i)+"";
				aux=this.revisarCaracteresEspeciales(aux);
				if(aux.trim().length()>4)
					aux = aux.substring(0,4);
				
				//registroAU+=","+this.editarEspacios(aux.trim(),aux.trim().length(),4,false);
				registroAU+=","+aux.trim();
					
				
				//CAMPO 11 ***DIAGNÓSTICO RELACIONADO Nº 2 A LA SALIDA*************************************
				if(listadoAU.get("diag_egreso_rel2_"+i)==null)
					aux = "";
				else
					aux = listadoAU.get("diag_egreso_rel2_"+i)+"";
				aux=this.revisarCaracteresEspeciales(aux);
				if(aux.trim().length()>4)
					aux = aux.substring(0,4);
				
				//registroAU+=","+this.editarEspacios(aux.trim(),aux.trim().length(),4,false);
				registroAU+=","+aux.trim();
					
				
				//CAMPO 12 ***DIAGNÓSTICO RELACIONADO Nº 3 A LA SALIDA*************************************
				if(listadoAU.get("diag_egreso_rel3_"+i)==null)
					aux = "";
				else
					aux = listadoAU.get("diag_egreso_rel3_"+i)+"";
				aux=this.revisarCaracteresEspeciales(aux);
				if(aux.trim().length()>4)
					aux = aux.substring(0,4);
				
				//registroAU+=","+this.editarEspacios(aux.trim(),aux.trim().length(),4,false);
				registroAU+=","+aux.trim();
				
				
				//CAMPO 13 ***DESTINO A LA SALIDA DE OBSERVACIÓN***********************************************
				if((listadoAU.get("destino_salida_"+i)+"").equals("")||
					listadoAU.get("destino_salida_"+i)==null)
				{
					this.editarInconsistencia(listadoAU,
							ConstantesBD.ripsAU,i,
							"destino a la salida de observación",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAU+=",";
				}
				else
				{
					aux = listadoAU.get("destino_salida_"+i)+"";
					if(Utilidades.convertirAEntero(aux)<0)
					{
						this.editarInconsistencia(listadoAU,
								ConstantesBD.ripsAU,i,
								"destino a la salida de observación",
								"campo con datos inválidos");
						this.huboInconsistencias=true;
						registroAU+=",";
					}
					else
					{
						//registroAU+=","+this.editarEspacios(aux.trim(),aux.trim().length(),1,false);
						registroAU+=","+aux.trim();
					}	
				}
				//CAMPO 14 ***ESTADO DE LA SALIDA*******************************************************
				if((listadoAU.get("estado_salida_"+i)+"").equals("")||
					listadoAU.get("estado_salida_"+i)==null)
				{
					this.editarInconsistencia(listadoAU,
							ConstantesBD.ripsAU,i,
							"estado a la salida",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAU+=",";
				}
				else
				{
					aux = listadoAU.get("estado_salida_"+i)+"";
					//registroAU+=","+this.editarEspacios(aux.trim(),aux.trim().length(),1,false);
					registroAU+=","+aux.trim();
				} 
				
				//CAMPO 15 ***DIAGNÓSTICO DE LA CAUSA BÁSICA DE MUERTE**********************************
				aux = listadoAU.get("estado_salida_"+i)+"";
				aux=this.revisarCaracteresEspeciales(aux);
				//Si el estado de salida es 2, el diagnóstico de muerte es requerido
				if(aux.equals("2")&&((listadoAU.get("diag_muerte_"+i)+"").equals("")||listadoAU.get("diag_muerte_"+i)==null))
				{
					this.editarInconsistencia(listadoAU,
							ConstantesBD.ripsAU,i,
							"diagnóstico de muerte",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAU+=",";
				}
				else
				{
					if(listadoAU.get("diag_muerte_"+i)==null)
						aux = "";
					else
						aux = listadoAU.get("diag_muerte_"+i)+"";
					aux=this.revisarCaracteresEspeciales(aux);
					if(aux.trim().length()>4)
						aux = aux.substring(0,4);
					//registroAU+=","+this.editarEspacios(aux.trim(),aux.trim().length(),4,false);
					registroAU+=","+aux.trim();
					
				}
				
				//CAMPO 16 ***FECHA SALIDA DEL USUARIO DE OBSERVACIÓN******************************************
				if((listadoAU.get("fecha_egreso_"+i)+"").equals("")||
					listadoAU.get("fecha_egreso_"+i)==null)
				{
					this.editarInconsistencia(listadoAU,
							ConstantesBD.ripsAU,i,
							"fecha salida del usuario de observación",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAU+=",";
				}
				else
				{
					aux = listadoAU.get("fecha_egreso_"+i)+"";
					if(UtilidadFecha.validarFecha(aux))
						//registroAU+=","+this.editarEspacios(aux.trim(),aux.trim().length(),10,false);
						registroAU+=","+aux.trim();
					else
					{
						this.editarInconsistencia(listadoAU,
								ConstantesBD.ripsAU,i,
								"fecha salida del usuario de observación",
								"campo con datos inválidos");
						this.huboInconsistencias=true;
						registroAU+=",";
					}
				}
				
				//CAMPO 17 ***HORA SALIDA DEL USUARIO DE OBSERVACIÓN********************************************
				if((listadoAU.get("hora_egreso_"+i)+"").trim().equals("")||
					listadoAU.get("hora_egreso_"+i)==null)
				{
					this.editarInconsistencia(listadoAU,
							ConstantesBD.ripsAU,i,
							"hora salida del usuario de observación",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAU+=",";
				}
				else
				{
					aux = listadoAU.get("hora_egreso_"+i)+"";
					if(UtilidadFecha.validacionHora(aux).puedoSeguir)
					{
						aux = UtilidadFecha.convertirHoraACincoCaracteres(aux);
						//registroAU+=","+this.editarEspacios(aux.trim(),aux.trim().length(),5,false);
						registroAU+=","+aux.trim();
					}
					else
					{
						this.editarInconsistencia(listadoAU,
								ConstantesBD.ripsAU,i,
								"hora salida del usuario de observación",
								"campo con datos inválidos");
						this.huboInconsistencias=true;
						registroAU+=",";
					}
				}
				
				//*********FIN******************
				
				
				contadorFactura++;
				
					
				
			}
			
			bufferAU.write(registroAU);
			contadorRegistros+=contadorFactura;
			
			//se almacena el número de registros creados para el archivo CT
			this.numeroRegistrosCreados.put(ConstantesBD.ripsAU,contadorRegistros+"");
			
			bufferAU.close();
			
			if(contadorRegistros<1)
			{
				//archivo sin registros
				archivoAU.delete();
				return null;
			}
			else
			{	
				//LLENO EL ARRAY LIST PARA GENERAR LA DESCARGA
				//logger.info("Archivo AU ->"+nombreAU);
				archivosGenerados.add(nombreAU);
			}
			
			//SEGÚN EL TIPO DE RIPS SE RETORNA EL PATH ESPECÍFICO
			//RIPS CARTERA
			if(this.tipoRips.equals("Cartera"))
			{
				if(esFactura)
					return "../ripsFactura/ripsFactura.do?estado=detalle&archivo=AU&nitEntidad="+this.nitEntidad;
				else
					return "../ripsCuentaCobro/ripsCuentaCobro.do?estado=detalle&archivo=AU&nitEntidad="+this.nitEntidad;
			}
			//RIPS CAPITACION
			else
				return "../ripsCapitacion/ripsCapitacion.do?estado=detalle&archivo=AU&nitEntidad="+this.nitEntidad;
			
			
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo AU al generarlo: "+e);
			this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo AU: "+e));
			return null;
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo AU: "+e);
			this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo AU: "+e));
			return null;
		}
	}
	
	/**
	 * Método para generar el archivo AH
	 * @param con
	 * @return ruta para ingresar al resumen del archivo
	 * si retorna nulo es que hubo error en la edición del fichero
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String generarAH(Connection con) 
	{
		HashMap listadoAH;
		String registroAH="";
		int numeroRegistros; //número de registros consultado desde la BD
		int contadorRegistros=0; //cuenta del número de registros insertados en el fichero
		int contadorFactura=0; //contador de registros por factura
		String numeroFacturaAnterior=""; //permite llevar el control de los registros de una solo factura
		boolean presenciaInconsistenciaGeneral=false;//variable boolean para el manejo de la inconsistencia general
		
		String aux=""; //variable auxiliar para la revisión de caracteres especiales en los campos texto
		
		//		 verificar si es una consulta para la interfaz de ax_rips
		if (this.numeroEnvio.equals(""))
		{
		
			//ejecucion de la consulta de datos
			//RIPS CARTERA********************************
			if(this.tipoRips.equals("Cartera"))
				listadoAH = ripsDao.consultaAH(con,this.fechaInicial,this.fechaFinal,this.numeroCuentaCobro,this.convenio,institucion.getCodigo(),this.esFactura, false, this.nroFacturaAx);
			//RIPS CAPITACION****************************
			else
				listadoAH = this.consultaCapitacionAH(con, institucion.getCodigo());
		}
		else
		{
			listadoAH = ripsDao.consultaAH(con,this.fechaInicial,this.fechaFinal,this.numeroCuentaCobro,this.convenio,institucion.getCodigo(),this.esFactura, true, this.nroFacturaAx);
		}
		
		numeroRegistros=Utilidades.convertirAEntero(listadoAH.get("numRegistros")+"");
		try
		{
			//apertura de archivo AH
			aux=this.revisarCaracteresEspeciales(this.numeroRemision);
			File archivoAH=new File(dirConvenio.getAbsolutePath(),ConstantesBD.ripsAH+aux+".txt");
			FileWriter streamAH=new FileWriter(archivoAH,false); //se coloca false para el caso de que esté repetido
			BufferedWriter bufferAH=new BufferedWriter(streamAH);
			
			String nombreAH = ConstantesBD.ripsAH+aux+".txt";
			
			for(int i=0;i<numeroRegistros;i++)
			{
				//si no hubo inconsistencias entonces se hace una inserción del registro en el archivo
				//se verifica si el registro ha cambiado de factura
				//en el caso de que ya haya cambiado de factura ya se pueden insertar 
				//todos los registros relacionados con la factura 
				if(numeroFacturaAnterior.equals(""))
					numeroFacturaAnterior=listadoAH.get("consecutivo_"+i)+"";
				else if(!numeroFacturaAnterior.equals(listadoAH.get("consecutivo_"+i)+""))
				{
					bufferAH.write(registroAH);
					contadorRegistros+=contadorFactura;
					contadorFactura=0;
					numeroFacturaAnterior=listadoAH.get("consecutivo_"+i)+"";
					registroAH="";
				}
				
				
				if(contadorFactura>0||contadorRegistros>0)
				{
					registroAH+="\r\n";
				}
				
				//edicion de registro
				//CAMPO1 *************NÚMERO DE LA FACTURA**************************************
				if(this.ripsConFactura)
				{
					if((listadoAH.get("consecutivo_"+i)+"").trim().equals(""))
					{
						this.editarInconsistencia(listadoAH,
								ConstantesBD.ripsAH,i,
								"número de la factura",
								"campo sin información");
						this.huboInconsistencias=true;
						//registroAH+="                    ";
					}
					else
					{
						aux = (listadoAH.get("consecutivo_"+i)+"").trim();
						if(aux.length()>20)
							aux = aux.substring(0,20);
						
						//registroAH+=this.editarEspacios(aux,aux.length(),20,false);
						registroAH+=aux;
						
					}
				}
				/*else
					registroAH+="                    ";*/
				
				//CAMPO 2 ***********CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD****************
				if(!presenciaInconsistenciaGeneral)
				{
					if(institucion.getCodMinSalud().trim().equals(""))
					{
						this.editarInconsistencia(listadoAH,
								ConstantesBD.ripsAH,i,
								"código del prestador de servicios de salud",
								"campo sin información (para todas las facturas)");
						this.huboInconsistencias=true;
						presenciaInconsistenciaGeneral=true;
						registroAH+=",";
					}
					else
					{
						aux = this.revisarCaracteresEspeciales(institucion.getCodMinSalud());
						if(aux.trim().length()>12)
							aux = aux.substring(0,12);
						
						//registroAH+=","+this.editarEspacios(aux.trim(),aux.trim().length(),12,false);
						registroAH+=","+aux.trim();
						
					}
				}
				else
					registroAH+=",";
				
				//CAMPO 3**********TIPO DE IDENTIFICACION DEL USUARIO****************************
				if((listadoAH.get("tipo_identificacion_"+i)+"").equals("")||
					listadoAH.get("tipo_identificacion_"+i)==null)
				{
					this.editarInconsistencia(listadoAH,
							ConstantesBD.ripsAH,i,
							"tipo identificación usuario",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAH+=",";
				}
				else
				{
					aux = listadoAH.get("tipo_identificacion_"+i)+"";
					if(aux.equals("CC")||aux.equals("CE")||aux.equals("PA")||aux.equals("RC")||aux.equals("TI")||aux.equals("AS")||
						aux.equals("MS")||aux.equals("NU"))
						//registroAH+=","+this.editarEspacios(aux.trim(),aux.trim().length(),2,false);
						registroAH+=","+aux.trim();
					else
					{
						this.editarInconsistencia(listadoAH,
								ConstantesBD.ripsAH,i,
								"tipo identificación usuario",
								"campo con datos inválidos");
						this.huboInconsistencias=true;
						registroAH+=",";
					}
				}
				
				//CAMPO 4********NÚMERO DE IDENTIFICACIÓN DEL USUARIO************************************
				if((listadoAH.get("numero_identificacion_"+i)+"").equals("")||
					listadoAH.get("numero_identificacion_"+i)==null)
				{
					this.editarInconsistencia(listadoAH,
							ConstantesBD.ripsAH,i,
							"número identificación usuario",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAH+=",";
				}
				else
				{
					aux = listadoAH.get("numero_identificacion_"+i)+"";
					if(aux.trim().length()>20)
						aux = aux.substring(0,20);
					
					aux=this.revisarCaracteresEspeciales(aux);
					//registroAH+=","+this.editarEspacios(aux.trim(),aux.trim().length(),20,false);
					registroAH+=","+aux.trim();
					
				}
				
				//CAMPO 5*******VÍA DE INGRESO A LA INSTITUCIÓN*******************************************
				try
				{
					aux = (listadoAH.get("via_"+i)+""); 
					Utilidades.convertirAEntero(aux);
				}
				catch(Exception e)
				{
					aux = "";
				}
				if(aux.equals(""))
				{
					this.editarInconsistencia(listadoAH,
							ConstantesBD.ripsAH,i,
							"vía ingreso a la institución",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAH+=",";
				}
				else
				{
					aux = listadoAH.get("via_"+i)+"";
					//la vía debe tener los valores 1,2,3 o 4
					if(Utilidades.convertirAEntero(aux)>=1&&Utilidades.convertirAEntero(aux)<=4)
						//registroAH+=","+this.editarEspacios(aux.trim(),aux.trim().length(),1,false);
						registroAH+=","+aux.trim();
					else
					{
						this.editarInconsistencia(listadoAH,
								ConstantesBD.ripsAH,i,
								"vía ingreso a la institución",
								"campo con datos inválidos");
						this.huboInconsistencias=true;
						registroAH+=",";
					}
						
				}
				
				//CAMPO 6*******FECHA INGRESO USUARIO A LA INSTITUCIÓN***********************************
				if((listadoAH.get("fecha_ingreso_"+i)+"").equals("")||
					listadoAH.get("fecha_ingreso_"+i)==null)
				{
					this.editarInconsistencia(listadoAH,
							ConstantesBD.ripsAH,i,
							"fecha ingreso del usuario a la institución",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAH+=",";
				}
				else
				{
					aux = listadoAH.get("fecha_ingreso_"+i)+"";
					if(UtilidadFecha.validarFecha(aux))
						//registroAH+=","+this.editarEspacios(aux.trim(),aux.trim().length(),10,false);
						registroAH+=","+aux.trim();
					else
					{
						this.editarInconsistencia(listadoAH,
								ConstantesBD.ripsAH,i,
								"fecha ingreso del usuario a la institución",
								"campo con datos inválidos");
						this.huboInconsistencias=true;
						registroAH+=",";
					}
				}
				
				//CAMPO 7******HORA INGRESO USUARIO A LA INSTITUCIÓN**********************************
				if((listadoAH.get("hora_ingreso_"+i)+"").trim().equals("")||
					listadoAH.get("hora_ingreso_"+i)==null)
				{
					this.editarInconsistencia(listadoAH,
							ConstantesBD.ripsAH,i,
							"hora ingreso del usuario a la institución",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAH+=",";
				}
				else
				{
					aux = listadoAH.get("hora_ingreso_"+i)+"";
					if(UtilidadFecha.validacionHora(aux).puedoSeguir)
					{
						aux = UtilidadFecha.convertirHoraACincoCaracteres(aux);
						//registroAH+=","+this.editarEspacios(aux.trim(),aux.trim().length(),5,false);
						registroAH+=","+aux.trim();
					}
					else
					{
						this.editarInconsistencia(listadoAH,
								ConstantesBD.ripsAH,i,
								"hora ingreso del usuario a la institución",
								"campo con datos inválidos");
						this.huboInconsistencias=true;
						registroAH+=",";
					}
				}
				
				//CAMPO 8******NÚMERO DE AUTORIZACIÓN***********************************************
				if(listadoAH.get("numero_autorizacion_"+i)==null)
					aux = "";
				else
					aux = listadoAH.get("numero_autorizacion_"+i)+"";
				aux=this.revisarCaracteresEspeciales(aux);
				//registroAH+=","+this.editarEspacios(aux.trim(),aux.trim().length(),15,false);
				registroAH+=","+aux.trim();
				
				//CAMPO 9******CAUSA EXTERNA****************************************************
				try
				{
					aux = (listadoAH.get("causa_externa_"+i)+"");
					Utilidades.convertirAEntero(aux);
				}
				catch(Exception e)
				{
					aux = "";
				}
				if(aux.equals(""))
				{
					this.editarInconsistencia(listadoAH,
							ConstantesBD.ripsAH,i,
							"causa externa",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAH+=",";
				}
				else
				{
					//la causa externa debe estar entre 1 y 15
					if(Utilidades.convertirAEntero(aux)>=1&&Utilidades.convertirAEntero(aux)<=15)
						registroAH+=","+this.editarCeros(aux.trim(),aux.trim().length(),2);
					else
					{
						this.editarInconsistencia(listadoAH,
								ConstantesBD.ripsAH,i,
								"causa externa",
								"campo con datos inválidos");
						this.huboInconsistencias=true;
						registroAH+=",";
					}
						
				}
				
				//CAMPO 10*******DIAGNÒSTICO PRINCIPAL INGRESO***************************************
				if((listadoAH.get("diag_ingreso_"+i)+"").equals("")||
					listadoAH.get("diag_ingreso_"+i)==null)
				{
					this.editarInconsistencia(listadoAH,
							ConstantesBD.ripsAH,i,
							"diagnóstico principal de ingreso",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAH+=",";
				}
				else
				{
					aux = listadoAH.get("diag_ingreso_"+i)+"";
					aux=this.revisarCaracteresEspeciales(aux);
					if(aux.trim().length()>4)
						aux = aux.substring(0,4);
					
					//registroAH+=","+this.editarEspacios(aux.trim(),aux.trim().length(),4,false);
					registroAH+=","+aux.trim();
						
				}
				
				//CAMPO 11 ******DIAGNÓSTICO PRINCIPAL EGRESO****************************************
				if((listadoAH.get("diag_egreso_"+i)+"").equals("")||
					listadoAH.get("diag_egreso_"+i)==null)
				{
					this.editarInconsistencia(listadoAH,
							ConstantesBD.ripsAH,i,
							"diagnóstico principal de egreso",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAH+=",";
				}
				else
				{
					aux = listadoAH.get("diag_egreso_"+i)+"";
					aux=this.revisarCaracteresEspeciales(aux);
					if(aux.trim().length()>4)
						aux = aux.substring(0,4);
					
					//registroAH+=","+this.editarEspacios(aux.trim(),aux.trim().length(),4,false);
					registroAH+=","+aux.trim();
						
				}
				
				//CAMPO 12 ***DIAGNÓSTICO RELACIONADO Nº 1 DE EGRESO**************************************
				if(listadoAH.get("diag_egreso_rel1_"+i)==null)
					aux = "";
				else
					aux = listadoAH.get("diag_egreso_rel1_"+i)+"";
				aux=this.revisarCaracteresEspeciales(aux);
				if(aux.trim().length()>4)
					aux = aux.substring(0,4);
				
				//registroAH+=","+this.editarEspacios(aux.trim(),aux.trim().length(),4,false);
				registroAH+=","+aux.trim();
					
				
				//CAMPO 13 ***DIAGNÓSTICO RELACIONADO Nº 2 DE EGRESO*************************************
				if(listadoAH.get("diag_egreso_rel2_"+i)==null)
					aux = "";
				else
					aux = listadoAH.get("diag_egreso_rel2_"+i)+"";
				aux=this.revisarCaracteresEspeciales(aux);
				if(aux.trim().length()>4)
					aux = aux.substring(0,4);
				
				//registroAH+=","+this.editarEspacios(aux.trim(),aux.trim().length(),4,false);
				registroAH+=","+aux.trim();
					
				
				//CAMPO 14 ***DIAGNÓSTICO RELACIONADO Nº 3 DE EGRESO*************************************
				if(listadoAH.get("diag_egreso_rel3_"+i)==null)
					aux = "";
				else
					aux = listadoAH.get("diag_egreso_rel3_"+i)+"";
				aux=this.revisarCaracteresEspeciales(aux);
				if(aux.trim().length()>4)
					aux = aux.substring(0,4);
				
				//registroAH+=","+this.editarEspacios(aux.trim(),aux.trim().length(),4,false);
				registroAH+=","+aux.trim();
				
				
				//CAMPO 15 ***DIAGNÓSTICO COMPLICACIÓN***********************************************
				if(listadoAH.get("diag_complicacion_"+i)==null)
					aux = "";
				else
					aux = listadoAH.get("diag_complicacion_"+i)+"";
				aux=this.revisarCaracteresEspeciales(aux);
				if(aux.trim().length()>4)
					aux = aux.substring(0,4);
				
				//registroAH+=","+this.editarEspacios(aux.trim(),aux.trim().length(),4,false);
				registroAH+=","+aux.trim();
				
				
				//CAMPO 16 ***ESTADO DE LA SALIDA*******************************************************
				if((listadoAH.get("estado_salida_"+i)+"").equals("")||
					listadoAH.get("estado_salida_"+i)==null)
				{
					this.editarInconsistencia(listadoAH,
							ConstantesBD.ripsAH,i,
							"estado a la salida",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAH+=",";
				}
				else
				{
					aux = listadoAH.get("estado_salida_"+i)+"";
					//registroAH+=","+this.editarEspacios(aux.trim(),aux.trim().length(),1,false);
					registroAH+=","+aux.trim();
				} 
				
				//CAMPO 16 ***DIAGNÓSTICO DE LA CAUSA BÁSICA DE MUERTE**********************************
				aux = listadoAH.get("estado_salida_"+i)+"";
				aux=this.revisarCaracteresEspeciales(aux);
				//Si el estado de salida es 2, el diagnóstico de muerte es requerido
				if(aux.equals("2")&&(listadoAH.get("diag_muerte_"+i)+"").equals(""))
				{
					this.editarInconsistencia(listadoAH,
							ConstantesBD.ripsAH,i,
							"diagnóstico de muerte",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAH+=",";
				}
				else
				{
					if(listadoAH.get("diag_muerte_"+i)==null)
						aux = "";
					else
						aux = listadoAH.get("diag_muerte_"+i)+"";
					aux=this.revisarCaracteresEspeciales(aux);
					if(aux.trim().length()>4)
						aux = aux.substring(0,4);
					
					//registroAH+=","+this.editarEspacios(aux.trim(),aux.trim().length(),4,false);
					registroAH+=","+aux.trim();
					
				}
				
				//CAMPO 17 ***FECHA EGRESO DEL USUARIO A LA INSTITUCIÓN******************************************
				if((listadoAH.get("fecha_egreso_"+i)+"").equals("")||
					listadoAH.get("fecha_egreso_"+i)==null)
				{
					this.editarInconsistencia(listadoAH,
							ConstantesBD.ripsAH,i,
							"fecha egreso del usuario a la institución",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAH+=",";
				}
				else
				{
					aux = listadoAH.get("fecha_egreso_"+i)+"";
					if(UtilidadFecha.validarFecha(aux))
						//registroAH+=","+this.editarEspacios(aux.trim(),aux.trim().length(),10,false);
						registroAH+=","+aux.trim();
					else
					{
						this.editarInconsistencia(listadoAH,
								ConstantesBD.ripsAH,i,
								"fecha egreso del usuario a la institución",
								"campo con datos inválidos");
						this.huboInconsistencias=true;
						registroAH+=",";
					}
				}
				
				//CAMPO 18 ***HORA EGRESO DEL USUARIO A LA INSTITUCIÓN********************************************
				if((listadoAH.get("hora_egreso_"+i)+"").trim().equals("")||
					listadoAH.get("hora_egreso_"+i)==null)
				{
					this.editarInconsistencia(listadoAH,
							ConstantesBD.ripsAH,i,
							"hora egreso del usuario a la institución",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAH+=",";
				}
				else
				{
					aux = listadoAH.get("hora_egreso_"+i)+"";
					if(UtilidadFecha.validacionHora(aux).puedoSeguir)
					{
						aux = UtilidadFecha.convertirHoraACincoCaracteres(aux);
						//registroAH+=","+this.editarEspacios(aux.trim(),aux.trim().length(),5,false);
						registroAH+=","+aux.trim();
					}
					else
					{
						this.editarInconsistencia(listadoAH,
								ConstantesBD.ripsAH,i,
								"hora egreso del usuario a la institución",
								"campo con datos invalidos");
						this.huboInconsistencias=true;
						registroAH+=",";
					}
				}
				
				//*********FIN******************
				
				
				contadorFactura++;
				
					
				
			}
			
			bufferAH.write(registroAH);
			contadorRegistros+=contadorFactura;
			
			//se almacena el número de registros creados para el archivo CT
			this.numeroRegistrosCreados.put(ConstantesBD.ripsAH,contadorRegistros+"");
			
			bufferAH.close();
			
			if(contadorRegistros<1)
			{
				//archivo sin registros
				archivoAH.delete();
				return null;
			}
			else
			{	
				//LLENO EL ARRAY LIST PARA GENERAR LA DESCARGA
				//logger.info("Archivo AH ->"+nombreAH);
				archivosGenerados.add(nombreAH);
			}
			
			//SEGÚN EL TIPO DE RIPS SE RETORNA EL PATH ESPECÍFICO
			//RIPS CARTERA
			if(this.tipoRips.equals("Cartera"))
			{
				if(esFactura)
					return "../ripsFactura/ripsFactura.do?estado=detalle&archivo=AH&nitEntidad="+this.nitEntidad;
				else
					return "../ripsCuentaCobro/ripsCuentaCobro.do?estado=detalle&archivo=AH&nitEntidad="+this.nitEntidad;
			}
			//RIPS CAPITACION
			else
				return "../ripsCapitacion/ripsCapitacion.do?estado=detalle&archivo=AH&nitEntidad="+this.nitEntidad;
			
			
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo AH al generarlo: "+e);
			this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo AH: "+e));
			return null;
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo AH: "+e);
			this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo AH: "+e));
			return null;
		}
	}

	/**
	 * Metodo para generar el archivo AP
	 * @param con
	 * @return ruta para ingresar al resumen del archivo
	 * si retorna nulo es que hubo error en la edición del fichero
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String generarAP(Connection con) 
	{
		HashMap listadoAP;
		String registroAP="";
		int numeroRegistros; //número de registros cosnultado desde la BD
		int contadorRegistros=0; //cuenta del número de registros insertados en el fichero
		int contadorFactura=0; //contador de registros por factura
		boolean presenciaInconsistenciaGeneral=false;//variable boolean para el manejo de la inconsistencia general
		String numeroFacturaAnterior=""; //permite llevar el control de los registros de una solo factura
		
		String aux=""; //variable auxiliar para revisar los caracteres especiales de los campos de texto
		
		//ejecucion de la consulta de datos
		//RIPS CARTERA ******************************
		
		
		// Modificacion ************************************* Interfaz AX RIPS
		// verificar si el campo de numero de envio esta tramitado, si no lo esta realizar la consulta normalmente
		if (this.numeroEnvio.equals(""))
		{
			if(this.tipoRips.equals("Cartera"))
				listadoAP=ripsDao.consultaAP(con,this.fechaInicial,this.fechaFinal,this.numeroCuentaCobro,this.convenio,institucion.getCodigo(),this.tipoCodigo,this.esFactura,false, this.nroFacturaAx);
				//listadoAP=ripsDao.consultaAP(con,this.fechaInicial,this.fechaFinal,this.numeroCuentaCobro,this.convenio,institucion.getCodigo(),this.tipoCodigo,this.esFactura);
			//RIPS CAPITACION ******************************
			else if(this.tipoRips.equals("Capitacion"))
				listadoAP = this.consultaCapitacionAP(con, institucion.getCodigo());
			//RIPS CONSULTORIOS ********************************************************************************
			else
				listadoAP=this.consultaConsultoriosAP(con,this.getNumeroFactura(),this.getNumeroRemision());
			
		}
		else
		{
			// quiere decir que el campo de numero de envio esta tramitado, por ende enviar los valores necesarios
			//if(this.tipoRips.equals("Cartera"))
				listadoAP=ripsDao.consultaAP(con,this.fechaInicial,this.fechaFinal,this.numeroCuentaCobro,this.convenio,institucion.getCodigo(),this.tipoCodigo,this.esFactura, true, this.nroFacturaAx);
			
		}
		
		
		if(!listadoAP.containsKey("numRegistros"))
			listadoAP.put("numRegistros", "0");
		
		numeroRegistros=Utilidades.convertirAEntero(listadoAP.get("numRegistros")+"");
		try
		{
			//apertura de archivo AP
			aux=this.revisarCaracteresEspeciales(this.numeroRemision);
			File archivoAP=new File(dirConvenio.getAbsolutePath(),ConstantesBD.ripsAP+aux+".txt");
			FileWriter streamAP=new FileWriter(archivoAP,false); //se coloca false para el caso de que esté repetido
			BufferedWriter bufferAP=new BufferedWriter(streamAP);
			
			String nombreAP = ConstantesBD.ripsAP+aux+".txt";
			
			for(int i=0;i<numeroRegistros;i++)
			{
				//si no hubo inconsistencias entonces se hace una inserción del registro en el archivo
				//se verifica si el registro ha cambiado de factura
				//en el caso de que ya haya cambiado de factura ya se pueden insertar 
				//todos los registros relacionados con la factura 
				if(numeroFacturaAnterior.equals(""))
					numeroFacturaAnterior=listadoAP.get("consecutivo_"+i)+"";
				else if(!numeroFacturaAnterior.equals(listadoAP.get("consecutivo_"+i)+""))
				{
					bufferAP.write(registroAP);
					contadorRegistros+=contadorFactura;
					contadorFactura=0;
					numeroFacturaAnterior=listadoAP.get("consecutivo_"+i)+"";
					registroAP="";
				}
				
				
				String tipoServicio=listadoAP.get("tipo_servicio_"+i)+"";
				String naturalezaS=listadoAP.get("naturaleza_servicio_"+i)+"";
				if(
					//Procedimientos
					(tipoServicio.equals(ConstantesBD.codigoServicioProcedimiento+"")&&
					(
							naturalezaS.equals(ConstantesBD.codigoNaturalezaServicioDiagnostico)||
							naturalezaS.equals(ConstantesBD.codigoNaturalezaServicioTerapeuticoNoQx)||
							naturalezaS.equals(ConstantesBD.codigoNaturalezaServicioPromocionPrevencion)
					))||
					//No Cruentos
					(tipoServicio.equals(ConstantesBD.codigoServicioNoCruentos+"")&&
					(naturalezaS.equals(ConstantesBD.codigoNaturalezaServicioDiagnostico)||
					naturalezaS.equals(ConstantesBD.codigoNaturalezaServicioTerapeuticoNoQx)||
					naturalezaS.equals(ConstantesBD.codigoNaturalezaServicioTerapeuticoQx)))||
					//Servicios Quirúrgicos
					(tipoServicio.equals(ConstantesBD.codigoServicioQuirurgico+"")&&
					naturalezaS.equals(ConstantesBD.codigoNaturalezaServicioTerapeuticoQx))||
					//Partos y Cesáreas
					(tipoServicio.equals(ConstantesBD.codigoServicioPartosCesarea+"")&&
					(naturalezaS.equals(ConstantesBD.codigoNaturalezaServicioTerapeuticoNoQx)||
					naturalezaS.equals(ConstantesBD.codigoNaturalezaServicioTerapeuticoQx)))||
					//Paquetes
					(tipoServicio.equals(ConstantesBD.codigoServicioPaquetes+"")&&
					(naturalezaS.equals(ConstantesBD.codigoNaturalezaServicioTerapeuticoNoQx)||
					naturalezaS.equals(ConstantesBD.codigoNaturalezaServicioTerapeuticoQx))))
				{
					//SEGÚN TIPO DE RIPS SE TOMA LA DECISIÓN ESPECÍFICA
					if(contadorFactura>0||contadorRegistros>0)
					{
						registroAP+="\r\n";
					}
					//edicion del registro
					//CAMPO 1************NÚMERO DE LA FACTURA********************
					
					if(this.ripsConFactura)
					{
						if((listadoAP.get("consecutivo_"+i)+"").trim().equals("")||
							listadoAP.get("consecutivo_"+i)==null)
						{
							this.editarInconsistencia(listadoAP,
									ConstantesBD.ripsAP,i,
									"número de la factura",
									"campo sin información");
							this.huboInconsistencias=true;
							//registroAP+="                    ";
						}
						else
						{
							aux = (listadoAP.get("consecutivo_"+i)+"").trim();
							if(aux.length()>20)
								aux = aux.substring(0,20);
							
							//registroAP+=this.editarEspacios(aux,aux.length(),20,false);
							registroAP+=aux;
							
						}
					}
					/*else
						registroAP+="                    ";*/
					
					//CAMPO 2*********CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD**************
					if(!presenciaInconsistenciaGeneral)
					{
						if(institucion.getCodMinSalud().trim().equals(""))
						{
							if(this.tipoRips.equals("Cartera"))
								aux="campo sin información (para todas las facturas)";
							else
								aux="campo sin información";
							
							this.editarInconsistencia(listadoAP,
									ConstantesBD.ripsAP,i,
									"código del prestador de servicios de salud",
									aux);
							this.huboInconsistencias=true;
							presenciaInconsistenciaGeneral=true;
							registroAP+=",";
						}
						else
						{
							aux=this.revisarCaracteresEspeciales(institucion.getCodMinSalud());
							
							if(aux.trim().length()>12)
								aux = aux.substring(0,12);
							
							registroAP+=","+aux.trim();
							
						}
					}
					else
						registroAP+=",";
					//CAMPO 3*******TIPO DE IDENTIFICACIÓN DEL USUARIO***********************
					
					if((listadoAP.get("tipo_identificacion_"+i)+"").trim().equals("")||
						listadoAP.get("tipo_identificacion_"+i)==null)
					{
						this.editarInconsistencia(listadoAP,
								ConstantesBD.ripsAP,i,
								"tipo identificacion usuario",
								"campo sin información");
						this.huboInconsistencias=true;
						registroAP+=",";
					}
					else
					{
						String tipoId=(listadoAP.get("tipo_identificacion_"+i)+"").trim();
						if(!tipoId.equals("CC")&&!tipoId.equals("CE")&&!tipoId.equals("PA")&&!tipoId.equals("RC")&&
							!tipoId.equals("TI")&&!tipoId.equals("AS")&&!tipoId.equals("MS")&&!tipoId.equals("NU"))
						{
							this.editarInconsistencia(listadoAP,
									ConstantesBD.ripsAP,i,
									"tipo identificacion usuario",
									"campo con datos inválidos");
							this.huboInconsistencias=true;
							registroAP+=",";
						}
						else
						{
							registroAP+=","+tipoId;
						}
					}
					
					//CAMPO 4 **********NÚMERO DE IDENTIFICACIÓN DE USUARIO************
					
					if((listadoAP.get("numero_identificacion_"+i)+"").trim().equals("")||
						listadoAP.get("numero_identificacion_"+i)==null)
					{
						this.editarInconsistencia(listadoAP,
								ConstantesBD.ripsAP,i,
								"número identificacion usuario",
								"campo sin información");
						this.huboInconsistencias=true;
						registroAP+=",";
					}
					else
					{
						aux=this.revisarCaracteresEspeciales(listadoAP.get("numero_identificacion_"+i)+"");
						
						if(aux.trim().length()>20)
							aux = aux.substring(0,20);
						
						//registroAP+=","+this.editarEspacios(aux.trim(),aux.trim().length(),20,false);
						registroAP+=","+aux.trim();
						
					}
					
					//CAMPO 5 *******FECHA DEL PROCEDIMIENTO*****************************
					
					if((listadoAP.get("fecha_procedimiento_"+i)+"").equals("")||
						listadoAP.get("fecha_procedimiento_"+i)==null)
					{
						this.editarInconsistencia(listadoAP,
								ConstantesBD.ripsAP,i,
								"fecha del procedimiento",
								"campo sin información");
						this.huboInconsistencias=true;
						registroAP+=",";
					}
					else
					{
						aux = UtilidadFecha.conversionFormatoFechaAAp(listadoAP.get("fecha_procedimiento_"+i)+"");
						if(UtilidadFecha.validarFecha(aux))
							registroAP+=","+aux;
						else
						{
							this.editarInconsistencia(listadoAP,
									ConstantesBD.ripsAP,i,
									"fecha del procedimiento",
									"campo con datos inválidos");
							this.huboInconsistencias=true;
							registroAP+=",";
						}
					}
					
					//CAMPO 6*****NÚMERO DE AUTORIZACION****************************
					aux=this.revisarCaracteresEspeciales(listadoAP.get("numero_autorizacion_"+i)+"");
					
					if(aux.trim().length()>15)
						aux = aux.substring(0,15);
					
					//registroAP+=","+this.editarEspacios(aux.trim(),aux.trim().length(),15,false);
					registroAP+=","+aux.trim();
					
					
					//CAMPO 7*****CÓDIGO DEL PROCEDIMIENTO*****************************
					
					if((listadoAP.get("codigo_procedimiento_"+i)+"").trim().equals("")||
						listadoAP.get("codigo_procedimiento_"+i)==null)
					{
						this.editarInconsistencia(listadoAP,
								ConstantesBD.ripsAP,i,
								"codigo del procedimiento",
								"campo sin información");
						this.huboInconsistencias=true;
						registroAP+=",";
					}
					else
					{
						aux=this.revisarCaracteresEspeciales(listadoAP.get("codigo_procedimiento_"+i)+"");
						
						if(aux.trim().length()>8)
							aux = aux.substring(0,8);
						
						//registroAP+=","+this.editarEspacios(aux.trim(),aux.trim().length(),8,false);
						registroAP+=","+aux.trim();
					}
					
					//CAMPO 8***************AMBITO DE REALIZACIÓN*************************
					if((listadoAP.get("ambito_realizacion_"+i)+"").trim().equals("")||
						listadoAP.get("ambito_realizacion_"+i)==null)
					{
						this.editarInconsistencia(listadoAP,
								ConstantesBD.ripsAP,i,
								"ambito de realizacion",
								"campo sin información");
						this.huboInconsistencias=true;
						registroAP+=",";
					}
					else
					{
						int ambitoR=Utilidades.convertirAEntero(listadoAP.get("ambito_realizacion_"+i)+"");
						if(ambitoR>=1&&ambitoR<=3)
						{
							registroAP+=","+ambitoR;
						}
						else
						{
							this.editarInconsistencia(listadoAP,
									ConstantesBD.ripsAP,i,
									"ambito de realizacion",
									"campo con datos inválidos");
							this.huboInconsistencias=true;
							registroAP+=",";
						}
					}
					//CAMPO 9***************FINALIDAD DEL PROCEDIMIENTO********************
					if((listadoAP.get("finalidad_procedimiento_"+i)+"").trim().equals("")||
						listadoAP.get("finalidad_procedimiento_"+i)==null)
					{
						this.editarInconsistencia(listadoAP,
								ConstantesBD.ripsAP,i,
								"finalidad del procedimiento",
								"campo sin información");
						this.huboInconsistencias=true;
						registroAP+=",";
					}
					else
					{
						int finalidadP=Utilidades.convertirAEntero(listadoAP.get("finalidad_procedimiento_"+i)+"");
						if(finalidadP>=1&&finalidadP<=5)
							registroAP+=","+finalidadP;
						else
						{
							this.editarInconsistencia(listadoAP,
									ConstantesBD.ripsAP,i,
									"finalidad del procedimiento",
									"campo con datos inválidos");
							this.huboInconsistencias=true;
							registroAP+=",";
						}
						
					}
					
					//CAMPO 10********PERSONAL QUE ATIENDE***************************************
					
					if((listadoAP.get("personal_atiende_"+i)+"").trim().equals("")||
						listadoAP.get("personal_atiende_"+i)==null)
					{
						//se verifica que el tipo de servicio sea Partos (R)
						if(tipoServicio.equals(ConstantesBD.codigoServicioPartosCesarea+""))
						{
							this.editarInconsistencia(listadoAP,
									ConstantesBD.ripsAP,i,
									"personal que atiende",
									"campo sin información");
							this.huboInconsistencias=true;
						}
						registroAP+=",";
					}
					else
					{
						aux=listadoAP.get("personal_atiende_"+i)+"";
						
						if(Utilidades.convertirAEntero(aux)<1||Utilidades.convertirAEntero(aux)>5)
						{
							//se verifica que el tipo de servicio sea Partos (R)
							if(tipoServicio.equals(ConstantesBD.codigoServicioPartosCesarea+""))
							{
								//si es RIPS Cartera o Capitacion y el valor es -1 quiere decir que no
								//había asocio de Honorarios
								if((this.tipoRips.equals("Cartera")||this.tipoRips.equals("Capitacion"))&&
										Utilidades.convertirAEntero(aux)==-1)
									this.editarInconsistencia(listadoAP,
											ConstantesBD.ripsAP,i,
											"personal que atiende",
											"no existe asocio de Honorarios");
								else
									this.editarInconsistencia(listadoAP,
											ConstantesBD.ripsAP,i,
											"personal que atiende",
											"campo con dato inválido");
								
								this.huboInconsistencias=true;
							}
							registroAP+=",";
						}
						else
						{
							registroAP+=","+aux;
						}
						
					}
					
					//CAMPO 11********CÓDIGO DEL DIAGNÓSTICO PRINCIPAL**************************	
					if((listadoAP.get("cod_diag_ppal_"+i)+"").trim().equals("")||
						listadoAP.get("cod_diag_ppal_"+i)==null)
					{
						//se verifica que el servicio sea Quirúrgicos o Partos
						if(tipoServicio.equals(ConstantesBD.codigoServicioPartosCesarea+"")||
						   tipoServicio.equals(ConstantesBD.codigoServicioQuirurgico+""))
						{
							this.editarInconsistencia(listadoAP,
									ConstantesBD.ripsAP,i,
									"codigo diagnóstico principal",
									"campo sin información");
							this.huboInconsistencias=true;
						}
						registroAP+=",";
					}
					else
					{
						String diagnostico=this.revisarCaracteresEspeciales(listadoAP.get("cod_diag_ppal_"+i)+"");
						if(diagnostico.length()>4)
							diagnostico = diagnostico.substring(0,4);
						
						//registroAP+=","+this.editarEspacios(diagnostico,diagnostico.length(),4,false);
						registroAP+=","+diagnostico;
						
					}
					
					//CAMPO 12 ***CÓDIGO DEL DIAGNÓSTICO RELACIONADO ***********
					if(!(listadoAP.get("cod_diag_rel_"+i)+"").equals("")&&
						listadoAP.get("cod_diag_rel_"+i)!=null)
					{
						String diagnostico=this.revisarCaracteresEspeciales(listadoAP.get("cod_diag_rel_"+i)+"");
						if(diagnostico.length()>4)
							diagnostico = diagnostico.substring(0,4);
						
						//registroAP+=","+this.editarEspacios(diagnostico,diagnostico.length(),4,false);
						registroAP+=","+diagnostico;
					}
					else
						registroAP+=",";
					
					//CAMPO 13 ***CÓDIGO DEL DIAGNÓSTICO COMPLICACION***********
					if(!(listadoAP.get("cod_diag_com_"+i)+"").equals("")&&
						listadoAP.get("cod_diag_com_"+i)!=null)
					{
						String diagnostico=this.revisarCaracteresEspeciales(listadoAP.get("cod_diag_com_"+i)+"");
						if(diagnostico.length()>4)
							diagnostico = diagnostico.substring(0,4);
						
						//registroAP+=","+this.editarEspacios(diagnostico,diagnostico.length(),4,false);
						registroAP+=","+diagnostico;
					}
					else
						registroAP+=",";
					
					//CAMPO 14 ***FORMA DE REALIZACION************************
					if((listadoAP.get("forma_realizacion_"+i)+"").trim().equals("")||
						listadoAP.get("forma_realizacion_"+i)==null)
					{
						//se verifica que el tipo de servicio sea Partos (R),Quirúrgicos (Q) 
						if(tipoServicio.equals(ConstantesBD.codigoServicioPartosCesarea+"")||
						   tipoServicio.equals(ConstantesBD.codigoServicioQuirurgico+""))
						{
							this.editarInconsistencia(listadoAP,
									ConstantesBD.ripsAP,i,
									"forma de realizacion",
									"campo sin información");
							this.huboInconsistencias=true;
						}
						registroAP+=",";
					}
					else
					{
						aux=listadoAP.get("forma_realizacion_"+i)+"";
						
						if(Utilidades.convertirAEntero(aux)<1||Utilidades.convertirAEntero(aux)>5)
						{
							//se verifica que el tipo de servicio sea Partos (R),Quirúrgicos (Q) 
							if(tipoServicio.equals(ConstantesBD.codigoServicioPartosCesarea+"")||
							   tipoServicio.equals(ConstantesBD.codigoServicioQuirurgico+""))
							{
								this.editarInconsistencia(listadoAP,
										ConstantesBD.ripsAP,i,
										"forma de realizacion",
										"campo con datos inválidos");
								this.huboInconsistencias=true;
							}
							registroAP+=",";
						}
						else
						{
							registroAP+=","+aux;
						}
						
					}
					//CAMPO 15 ***VALOR DEL PROCEDIMIENTO***************************
				
					if(this.ripsConFactura)
					{
						if((listadoAP.get("valor_"+i)+"").trim().equals("")||
							listadoAP.get("valor_"+i)==null)
						{
							this.editarInconsistencia(listadoAP,
									ConstantesBD.ripsAP,i,
									"valor del procedimiento",
									"campo sin información");
							this.huboInconsistencias=true;
							registroAP+=",";
						}
						else
						{
							String auxiliar=(listadoAP.get("valor_"+i)+"").trim();
							auxiliar = UtilidadTexto.formatearValores(auxiliar,"######0.00");
							
							//Se quitan los decimales en ceros
							if(auxiliar.endsWith(".00"))
								auxiliar = auxiliar.replace(".00", "");
							
							if(auxiliar.trim().length()>15)
								auxiliar = auxiliar.substring(0,15);
								
							//registroAP+=","+this.editarEspacios(auxiliar,auxiliar.length(),15,true);
							registroAP+=","+auxiliar;
							
						}
					}
					else
					{
						registroAP+=",";
					}
				
				
					//*************FIN************************************************
					
					contadorFactura++;
				}
				//logger.info("REGISTRO AP["+i+"]: "+registroAP);
				
			}//FIN FOR
			//SEGÚN TIPO DE RIPS SE TOMA LA DECISIÓN ESPECÍFICA
			//logger.info("ESTO ES LO QUE VOY A ESCRIBIR DEL AP:"+registroAP);
			bufferAP.write(registroAP);
			contadorRegistros+=contadorFactura;
			
			//se almacena el número de registros creados para el archivo CT
			this.numeroRegistrosCreados.put(ConstantesBD.ripsAP,contadorRegistros+"");
			
			bufferAP.close();
			
			if(contadorRegistros<1)
			{
				//archivo sin registros
				archivoAP.delete();
				return null;
			}
			else
			{	
				//LLENO EL ARRAY LIST PARA GENERAR LA DESCARGA
				//logger.info("Archivo AP ->"+nombreAP);
				archivosGenerados.add(nombreAP);
			}
			
			//SEGÚN TIPO DE RIPS SE RETORNA EL PATH ESPECÍFICO
			if(this.tipoRips.equals("Cartera"))
			{
				if(esFactura)
					return "../ripsFactura/ripsFactura.do?estado=detalle&archivo=AP&nitEntidad="+this.nitEntidad;
				else
					return "../ripsCuentaCobro/ripsCuentaCobro.do?estado=detalle&archivo=AP&nitEntidad="+this.nitEntidad;
			}
			else if(this.tipoRips.equals("Capitacion"))
				return "../ripsCapitacion/ripsCapitacion.do?estado=detalle&archivo=AP&nitEntidad="+this.nitEntidad;
			else
				return "../ripsRangos/ripsRangos.do?estado=detalle&archivo=AP&nitEntidad="+this.nitEntidad;
			
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo AP al generarlo: "+e);
			this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo AP: "+e));
			return null;
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo AP: "+e);
			this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo AP: "+e));
			return null;
		}
	}
	
	/**
	 * Método para generar el archivo AN
	 * @param con
	 * @return ruta para ingresar al resumen del archivo
	 * si retorna nulo es que hubo error en la edición del fichero
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String generarAN(Connection con) 
	{
		HashMap listadoAN;
		String registroAN="";
		int numeroRegistros; //número de registros consultado desde la BD
		int contadorRegistros=0; //cuenta del número de registros insertados en el fichero
		int contadorFactura=0; //contador de registros por factura
		String numeroFacturaAnterior=""; //permite llevar el control de los registros de una solo factura
		boolean presenciaInconsistenciaGeneral=false;//variable boolean para el manejo de la inconsistencia general
		
		String aux=""; //variable auxiliar para la revisión de caracteres especiales en los campos texto
		
		
		// verificar si es una consulta para la interfaz de ax_rips
		if (this.numeroEnvio.equals(""))
		{
			//ejecucion de la consulta de datos
			//RIPS CARTERA
			if(this.tipoRips.equals("Cartera"))
				listadoAN = ripsDao.consultaAN(con,this.fechaInicial,this.fechaFinal,this.numeroCuentaCobro,this.convenio,institucion.getCodigo(),this.esFactura, false, this.nroFacturaAx);
			//RIPS CAPITACION
			else
				listadoAN = this.consultaCapitacionAN(con, institucion.getCodigo());			
		}
		else
			listadoAN = ripsDao.consultaAN(con,this.fechaInicial,this.fechaFinal,this.numeroCuentaCobro,this.convenio,institucion.getCodigo(),this.esFactura, true, this.nroFacturaAx);
		
		
		numeroRegistros=Utilidades.convertirAEntero(listadoAN.get("numRegistros")+"");
		try
		{
			//apertura de archivo AN
			aux=this.revisarCaracteresEspeciales(this.numeroRemision);
			File archivoAN=new File(dirConvenio.getAbsolutePath(),ConstantesBD.ripsAN+aux+".txt");
			FileWriter streamAN=new FileWriter(archivoAN,false); //se coloca false para el caso de que esté repetido
			BufferedWriter bufferAN=new BufferedWriter(streamAN);
			
			String nombreAN = ConstantesBD.ripsAN+aux+".txt";
			
			for(int i=0;i<numeroRegistros;i++)
			{
				//si no hubo inconsistencias entonces se hace una inserción del registro en el archivo
				//se verifica si el registro ha cambiado de factura
				//en el caso de que ya haya cambiado de factura ya se pueden insertar 
				//todos los registros relacionados con la factura 
				if(numeroFacturaAnterior.equals(""))
					numeroFacturaAnterior=listadoAN.get("consecutivo_"+i)+"";
				else if(!numeroFacturaAnterior.equals(listadoAN.get("consecutivo_"+i)+""))
				{
					bufferAN.write(registroAN);
					contadorRegistros+=contadorFactura;
					contadorFactura=0;
					numeroFacturaAnterior=listadoAN.get("consecutivo_"+i)+"";
					registroAN = "";
				}
				
				
				//se verifica si tuvo_informacion de parto
				boolean tuvoInformacion = UtilidadTexto.getBoolean(listadoAN.get("tuvo_informacion_"+i).toString());
				
				if(tuvoInformacion)
				{
					if(contadorFactura>0||contadorRegistros>0)
					{
						registroAN+="\r\n";
					}
					
					//edicion de registro
					//CAMPO1 *************NÚMERO DE LA FACTURA**************************************
					if(this.ripsConFactura)
					{
						if((listadoAN.get("consecutivo_"+i)+"").trim().equals(""))
						{
							this.editarInconsistencia(listadoAN,
									ConstantesBD.ripsAN,i,
									"número de la factura",
									"campo sin información");
							this.huboInconsistencias=true;
							//registroAN+="                    ";
						}
						else
						{
							aux = (listadoAN.get("consecutivo_"+i)+"").trim();
							if(aux.trim().length()>20)
								aux = aux.trim().substring(0,20);
							
							//registroAN+=this.editarEspacios(aux,aux.trim().length(),20,false);
							registroAN+=aux;
						}
					}
					/*else
						registroAN+="                    ";*/
				
					//CAMPO 2 ***********CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD****************
					if(!presenciaInconsistenciaGeneral)
					{
						if(institucion.getCodMinSalud().trim().equals(""))
						{
							this.editarInconsistencia(listadoAN,
									ConstantesBD.ripsAN,i,
									"código del prestador de servicios de salud",
									"campo sin información (para todas las facturas)");
							this.huboInconsistencias=true;
							presenciaInconsistenciaGeneral=true;
							registroAN+=",";
						}
						else
						{
							aux = this.revisarCaracteresEspeciales(institucion.getCodMinSalud());
							if(aux.trim().length()>12)
								aux = aux.substring(0,12);
							//registroAN+=","+this.editarEspacios(aux.trim(),aux.trim().length(),12,false);
							registroAN+=","+aux.trim();
							
						}
					}
					else
						registroAN+=",";
				
					//CAMPO 3**********TIPO DE IDENTIFICACION DE LA MADRE****************************
					if((listadoAN.get("tipo_identificacion_"+i)+"").equals("")||
						listadoAN.get("tipo_identificacion_"+i)==null)
					{
						this.editarInconsistencia(listadoAN,
								ConstantesBD.ripsAN,i,
								"tipo identificación madre",
								"campo sin información");
						this.huboInconsistencias=true;
						registroAN+=",";
					}
					else
					{
						aux = listadoAN.get("tipo_identificacion_"+i)+"";
						if(aux.equals("CC")||aux.equals("CE")||aux.equals("PA")||aux.equals("RC")||aux.equals("TI")||aux.equals("AS")||
							aux.equals("MS")||aux.equals("NU"))
						{
							//se verifica que el tipo Id de la factura sea igual al tipo Id de la madre
							
							if(aux.equals(listadoAN.get("tipo_id_madre_"+i).toString().trim()))
								//registroAN+=","+this.editarEspacios(aux.trim(),aux.trim().length(),2,false);
								registroAN+=","+aux.trim();
							else
							{
								this.editarInconsistencia(listadoAN,
										ConstantesBD.ripsAN,i,
										"tipo identificación madre",
										"no corresponde con la info. del parto");
								this.huboInconsistencias=true;
								registroAN+=",";
							}
						}
						else
						{
							this.editarInconsistencia(listadoAN,
									ConstantesBD.ripsAN,i,
									"tipo identificación madre",
									"campo con datos inválidos");
							this.huboInconsistencias=true;
							registroAN+=",";
						}
					}
				
					//CAMPO 4********NÚMERO DE IDENTIFICACIÓN DEL USUARIO************************************
					if((listadoAN.get("numero_identificacion_"+i)+"").equals("")||
						listadoAN.get("numero_identificacion_"+i)==null)
					{
						this.editarInconsistencia(listadoAN,
								ConstantesBD.ripsAN,i,
								"número identificación madre",
								"campo sin información");
						this.huboInconsistencias=true;
						registroAN+=",";
					}
					else
					{
						aux = listadoAN.get("numero_identificacion_"+i)+"";
						if(aux.equals(listadoAN.get("numero_id_madre_"+i).toString()))
						{
							if(aux.trim().length()>20)
								aux = aux.substring(0,20);
							aux=this.revisarCaracteresEspeciales(aux);
							//registroAN+=","+this.editarEspacios(aux.trim(),aux.trim().length(),20,false);
							registroAN+=","+aux.trim();
						}
						else
						{
							this.editarInconsistencia(listadoAN,
									ConstantesBD.ripsAN,i,
									"número identificación madre",
									"no corresponde con la info. del parto");
							this.huboInconsistencias=true;
							registroAN+=",";
						}
						
					}
				
				
					//CAMPO 5*******FECHA DE NACIMIENTO DEL RECIEN NACIDO*************************************************
					if((listadoAN.get("fecha_nacimiento_"+i)+"").trim().equals(""))
					{
						this.editarInconsistencia(listadoAN,
								ConstantesBD.ripsAN,i,
								"fecha de nacimiento recién nacido",
								"campo sin información");
						this.huboInconsistencias=true;
						registroAN+=",";
					}
					else
						registroAN+=","+listadoAN.get("fecha_nacimiento_"+i);
					
				
					//CAMPO 6 ****HORA DE NACIMIENTO**************************************************
					if((listadoAN.get("hora_nacimiento_"+i)+"").trim().equals(""))
					{
						this.editarInconsistencia(listadoAN,
								ConstantesBD.ripsAN,i,
								"hora de nacimiento recién nacido",
								"campo sin información");
						this.huboInconsistencias=true;
						registroAN+=",";
					}
					else
					{
						registroAN+=","+UtilidadFecha.convertirHoraACincoCaracteres(listadoAN.get("hora_nacimiento_"+i).toString());
					}
						
					//CAMPO 7*****EDAD GESTACIONAL*******************************************************
					if((listadoAN.get("edad_gestacional_"+i)+"").trim().equals(""))
					{
						this.editarInconsistencia(listadoAN,
								ConstantesBD.ripsAN,i,
								"edad gestacional",
								"campo sin información");
						this.huboInconsistencias=true;
						registroAN+=",";
					}
					else
					{
						aux = listadoAN.get("edad_gestacional_"+i).toString().trim();
						
						if(aux.length()>2)
							aux = aux.substring(0,2);
						//registroAN+=","+this.editarEspacios(aux.trim(),aux.trim().length(),2,true);
						registroAN+=","+aux.trim();
					}
					
					//CAMPO 8*****CONTROL PRENATAL*******************************************************
					if((listadoAN.get("control_prenatal_"+i)+"").trim().equals(""))
					{
						this.editarInconsistencia(listadoAN,
								ConstantesBD.ripsAN,i,
								"control prenatal",
								"campo sin información");
						this.huboInconsistencias=true;
						registroAN+=",";
					}
					else
					{
						aux = listadoAN.get("control_prenatal_"+i).toString().trim();
						if(aux.length()>1)
							aux = aux.substring(0,1);
						registroAN+=","+aux;
					}
					
					//CAMPO 9*****SEXO*******************************************************
					if((listadoAN.get("sexo_"+i)+"").trim().equals(""))
					{
						this.editarInconsistencia(listadoAN,
								ConstantesBD.ripsAN,i,
								"sexo",
								"campo sin información");
						this.huboInconsistencias=true;
						registroAN+=",";
					}
					else
					{
						aux = listadoAN.get("sexo_"+i).toString().trim();
						if(aux.length()>1)
							aux = aux.substring(0,1);
						registroAN+=","+aux;
					}
					
					//CAMPO 10*****PESO*******************************************************
					if((listadoAN.get("peso_"+i)+"").trim().equals(""))
					{
						this.editarInconsistencia(listadoAN,
								ConstantesBD.ripsAN,i,
								"peso",
								"campo sin información");
						this.huboInconsistencias=true;
						registroAN+=",";
					}
					else
					{
						aux = listadoAN.get("peso_"+i).toString().trim();
						if(aux.length()>4)
							aux = aux.substring(0,4);
						//registroAN+=","+this.editarEspacios(aux.trim(),aux.trim().length(),4,true);
						registroAN+=","+aux.trim();
					}
					
					//CAMPO 11*****DIAGNOSTICO DEL RECIÉN NACIDO*******************************************************
					if((listadoAN.get("diagnostico_rn_"+i)+"").trim().equals(""))
					{
						this.editarInconsistencia(listadoAN,
								ConstantesBD.ripsAN,i,
								"diagnóstico recién nacido",
								"campo sin información");
						this.huboInconsistencias=true;
						registroAN+=",";
					}
					else
					{
						aux = listadoAN.get("diagnostico_rn_"+i).toString().trim();
						if(aux.length()>4)
							aux = aux.substring(0,4);
						//registroAN+=","+this.editarEspacios(aux.trim(),aux.trim().length(),4,false);
						registroAN+=","+aux.trim();
					}
					
					//CAMPO 12*****CAUSA BÁSICA DE LA MUERTE*******************************************************
					if((listadoAN.get("causa_muerte_"+i)+"").trim().equals(""))
					{
						registroAN+=",";
					}
					else
					{
						aux = listadoAN.get("causa_muerte_"+i).toString().trim();
						if(aux.length()>4)
							aux = aux.substring(0,4);
						//registroAN+=","+this.editarEspacios(aux.trim(),aux.trim().length(),4,false);
						registroAN+=","+aux.trim();
					}
					
					//CAMPO 13*******FECHA DE MUERTE DEL RECIEN NACIDO*************************************************
					if((listadoAN.get("fecha_muerte_"+i)+"").trim().equals(""))
					{
						registroAN+=",";
					}
					else
					{
						registroAN+=","+listadoAN.get("fecha_muerte_"+i);
					}
					
				
					//CAMPO 6 ****HORA DE MUERTE**************************************************
					if((listadoAN.get("hora_muerte_"+i)+"").trim().equals(""))
					{
						registroAN+=",";
					}
					else
					{
						registroAN+=","+UtilidadFecha.convertirHoraACincoCaracteres(listadoAN.get("hora_muerte_"+i).toString());
					}
					
				
					//*********FIN******************
				
					
					contadorFactura++;
				}
				else
				{
					this.editarInconsistencia(listadoAN,
							ConstantesBD.ripsAN,i,
							"datos del parto",
							"campos sin información");
					this.huboInconsistencias=true;
				}
			}
			
			
			bufferAN.write(registroAN);
			contadorRegistros+=contadorFactura;	
			
			
			//se almacena el número de registros creados para el archivo CT
			this.numeroRegistrosCreados.put(ConstantesBD.ripsAN,contadorRegistros+"");
			
			bufferAN.close();
			
			if(contadorRegistros<1)
			{
				//archivo sin registros
				archivoAN.delete();
				return null;
			}
			else
			{	
				//LLENO EL ARRAY LIST PARA GENERAR LA DESCARGA
				//logger.info("Archivo AN ->"+nombreAN);
				archivosGenerados.add(nombreAN);
			}
			
			//SEGÚN EL TIPO DE RIPS SE RETORNA EL PATH ESPECÍFICO
			//RIPS CARTERA
			if(this.tipoRips.equals("Cartera"))
			{
				if(esFactura)
					return "../ripsFactura/ripsFactura.do?estado=detalle&archivo=AN&nitEntidad="+this.nitEntidad;
				else
					return "../ripsCuentaCobro/ripsCuentaCobro.do?estado=detalle&archivo=AN&nitEntidad="+this.nitEntidad;
			}
			//RIPS CAPITACION
			else
				return "../ripsCapitacion/ripsCapitacion.do?estado=detalle&archivo=AN&nitEntidad="+this.nitEntidad;
			
			
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo AN al generarlo: "+e);
			this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo AN: "+e));
			return null;
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo AN: "+e);
			this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo AN: "+e));
			return null;
		}
	}
	
	
	/**
	 * Método para editar las inconsistencias encontradas en la generación
	 * de los RIPS dentro del archivo de inconsistencias.
	 * @return
	 */
	private String generarInconsistencias() 
	{
		//SE GENERAN LAS INCONSISTENCIAS SEGUN LOS TIPOS DE RIPS
		if(this.tipoRips.equals("Cartera")||this.tipoRips.equals("Capitacion"))
			return this.generarInconsistenciasCartera();
		else
			return this.generarInconsistenciasConsultorios();
		
	}

	/**
	 * Sub-método usado para la generación de las inconsistencias de RIPS consultorios
	 * @return
	 */
	private String generarInconsistenciasConsultorios() {
		
		
		try
		{
			
			String registroIncon="";
			boolean existe=false; //variable usada para avisar si el HashMap tenía inconsistencias
			//apertura de archivo Incon
			String auxiliar=this.revisarCaracteresEspeciales(this.numeroRemision);
			File archivoIncon=new File(dirConvenio.getAbsolutePath(),ConstantesBD.ripsInconsistencias+auxiliar+".txt");
			FileWriter streamIncon=new FileWriter(archivoIncon,false); //se coloca false para el caso de que esté repetido
			BufferedWriter bufferIncon=new BufferedWriter(streamIncon);
			
			String nombreInco = ConstantesBD.ripsInconsistencias+auxiliar+".txt";
			
			//INCONSISTENCIAS GENERALES
			if(this.inconsistencias.get("total_general")!=null)
			{
				existe=true;
				int numeroInconsistencias=Utilidades.convertirAEntero(this.inconsistencias.get("total_general")+"");
				
				registroIncon="Archivo RIPS   Nombre del Campo                                     "+
				"Observaciones\r\n";
				bufferIncon.write(registroIncon);
					for(int i=0;i<numeroInconsistencias;i++)
					{
						registroIncon="";
						String aux=this.inconsistencias.get("archivo_"+i)+"";
						registroIncon+=this.editarEspacios(aux.trim(),aux.trim().length(),15,false);
						aux=this.inconsistencias.get("campo_"+i)+"";
						registroIncon+=this.editarEspacios(aux.trim(),aux.trim().length(),53,false);
						aux=this.inconsistencias.get("observacion_"+i)+"";
						registroIncon+=aux;//this.editarEspacios(aux.trim(),aux.trim().length(),50,false);
						registroIncon+="\r\n";
						
						bufferIncon.write(registroIncon);
					}
				registroIncon="\r\n\r\n\r\n\r\n";
				bufferIncon.write(registroIncon);
				
			}
			//INCONSISTENCIAS PARTICULARES***********************************************
			//se revisa si el número de las inconsistencias
			if(this.inconsistencias.get("total")!=null)
			{
				int numeroInconsistencias=Utilidades.convertirAEntero(this.inconsistencias.get("total")+"");
				existe=true;
				for(int i=0;i<numeroInconsistencias;i++)
				{
					
					//se limpia registro
					registroIncon="";
					
					//editar el encabezado de la inconsistencias
					registroIncon+="Nº Factura: "+this.inconsistencias.get("factura_"+i)+
						" Identificación Paciente: "+this.inconsistencias.get("tipo_num_"+i)+
						" Paciente: "+this.revisarCaracteresEspeciales(this.inconsistencias.get("paciente_"+i)+"")+
						" Vía de Ingreso: "+this.inconsistencias.get("via_ingreso_"+i)+"\r\n\r\n";
					//editar el detalle de la inconsistencia
					
					if(this.inconsistencias.get("elementos_"+i)!=null)
					{
						
						bufferIncon.write(registroIncon);
						registroIncon="";
						int numeroDetalles=Utilidades.convertirAEntero(this.inconsistencias.get("elementos_"+i)+"");
						
						registroIncon+="Archivo RIPS   Cuenta   Nombre del Campo                                     Orden Médica   "+
							"Observaciones\r\n";
						bufferIncon.write(registroIncon);
						for(int j=0;j<numeroDetalles;j++)
						{
							registroIncon="";
							String aux=this.inconsistencias.get("archivo_"+i+"_"+j)+"";
							registroIncon+=this.editarEspacios(aux.trim(),aux.trim().length(),15,false);
							aux=this.inconsistencias.get("cuenta_"+i+"_"+j)+"";
							registroIncon+=this.editarEspacios(aux.trim(),aux.trim().length(),9,false);
							aux=this.inconsistencias.get("campo_"+i+"_"+j)+"";
							registroIncon+=this.editarEspacios(aux.trim(),aux.trim().length(),53,false);
							aux=this.inconsistencias.get("orden_"+i+"_"+j)+"";
							registroIncon+=this.editarEspacios(aux.trim(),aux.trim().length(),15,false);
							aux=this.inconsistencias.get("observacion_"+i+"_"+j)+"";
							registroIncon+=aux;//this.editarEspacios(aux.trim(),aux.trim().length(),50,false);
							registroIncon+="\r\n";
							
							bufferIncon.write(registroIncon);
						}
						registroIncon="\r\n\r\n\r\n\r\n";
						
						bufferIncon.write(registroIncon);
						
					}
					
					
				}
				
			}
			
			bufferIncon.close();

			//LLENO EL ARRAY LIST PARA GENERAR LA DESCARGA
			logger.info("Archivo Inconsistencia ->"+nombreInco);
			archivosGenerados.add(nombreInco);
			
			//revisar si habían inconsistencias
			if(existe)
				return "../ripsRangos/ripsRangos.do?estado=detalle&archivo=Incon&nitEntidad="+this.nitEntidad;
			else
				return null;
			
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo Incon al generarlo: "+e);
			return null;
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo Incon: "+e);
			return null;
		}
		
	}

	/**
	 * Sub-método usado para la generación de las inconsistencias de RIPS cartera
	 * @return
	 */
	private String generarInconsistenciasCartera() {
		//	se revisa si el número de las inconsistencias
		if(this.inconsistencias.get("total")!=null)
		{
			try
			{
				int numeroInconsistencias=Utilidades.convertirAEntero(this.inconsistencias.get("total")+"");
				String registroIncon="";
				//apertura de archivo Incon
				String auxiliar=this.revisarCaracteresEspeciales(this.numeroRemision);
				File archivoIncon=new File(dirConvenio.getAbsolutePath(),ConstantesBD.ripsInconsistencias+auxiliar+".txt");
				FileWriter streamIncon=new FileWriter(archivoIncon,false); //se coloca false para el caso de que esté repetido
				BufferedWriter bufferIncon=new BufferedWriter(streamIncon);
				
				String nombreIconCartera = ConstantesBD.ripsInconsistencias+auxiliar+".txt";
				
				for(int i=1;i<=numeroInconsistencias;i++)
				{
					
					//se limpia registro
					registroIncon="";
					//se toma el consecutivo de la factura de la inconsistencia
					String llave=this.inconsistencias.get("pos_"+i)+"";
					//editar el encabezado de la inconsistencias
					registroIncon+="Nº Factura: "+llave+
						" Identificación Paciente: "+
						this.inconsistencias.get("tipo_identificacion_"+llave)+
						"."+this.revisarCaracteresEspeciales(this.inconsistencias.get("numero_identificacion_"+llave)+"")+
						" Paciente: "+this.revisarCaracteresEspeciales(this.inconsistencias.get("paciente_"+llave)+"")+
						" Cuenta Nº: "+this.inconsistencias.get("cuenta_"+llave)+
						" Vía de Ingreso: "+this.inconsistencias.get("via_ingreso_"+llave)+"\r\n\r\n";
					//editar el detalle de la inconsistencia
					
					if(this.inconsistencias.get("elementos_"+llave)!=null)
					{
						
						bufferIncon.write(registroIncon);
						registroIncon="";
						int numeroDetalles=Utilidades.convertirAEntero(this.inconsistencias.get("elementos_"+llave)+"");
						
						registroIncon+="Archivo RIPS   Nombre del Campo                                     Orden Médica   Serv/art  "+
							"Observaciones\r\n";
						bufferIncon.write(registroIncon);
						for(int j=1;j<=numeroDetalles;j++)
						{
							registroIncon="";
							String aux=this.inconsistencias.get("archivo_"+llave+"_"+(j-1))+"";
							registroIncon+=this.editarEspacios(aux.trim(),aux.trim().length(),15,false);
							aux=this.inconsistencias.get("campo_"+llave+"_"+(j-1))+"";
							registroIncon+=this.editarEspacios(aux.trim(),aux.trim().length(),53,false);
							aux=this.inconsistencias.get("orden_"+llave+"_"+(j-1))+"";
							registroIncon+=this.editarEspacios(aux.trim(),aux.trim().length(),15,false);
							aux=this.inconsistencias.get("serv_art_"+llave+"_"+(j-1))+"";
							registroIncon+=this.editarEspacios(aux.trim(),aux.trim().length(),10,false);
							aux=this.inconsistencias.get("observacion_"+llave+"_"+(j-1))+"";
							registroIncon+=aux;//this.editarEspacios(aux.trim(),aux.trim().length(),50,false);
							registroIncon+="\r\n";
							
							bufferIncon.write(registroIncon);
						}
						registroIncon="\r\n\r\n\r\n\r\n";
						
						bufferIncon.write(registroIncon);
						
					}
					
					
				}
				bufferIncon.close();
				
				// LLENO EL ARRAY LIST PARA GENERAR LA DESCARGA
				logger.info("Archivo Inconsistencia Cartera ->"+nombreIconCartera);
				archivosGenerados.add(nombreIconCartera);
				
				
				if(this.tipoRips.equals("Cartera"))
				{
					if(esFactura)
						return "../ripsFactura/ripsFactura.do?estado=detalle&archivo=Incon&nitEntidad="+this.nitEntidad;
					else
						return "../ripsCuentaCobro/ripsCuentaCobro.do?estado=detalle&archivo=Incon&nitEntidad="+this.nitEntidad;
				}
				else
					return "../ripsCapitacion/ripsCapitacion.do?estado=detalle&archivo=Incon&nitEntidad="+this.nitEntidad;
			}
			catch(FileNotFoundException e)
			{
				logger.error("No se pudo encontrar el archivo Incon al generarlo: "+e);
				this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo Inconsistencias: "+e));
				return null;
			}
			catch(IOException e)
			{
				logger.error("Error en los streams del archivo Incon: "+e);
				this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo Inconsistencias: "+e));
				return null;
			}
		}
		else
		{
			return null;
		}
	}

	/**
	 * Método para generar el archivo CT
	 * @return la ruta para acceder al resumen del archivo
	 * si retorna nulo es que hubo error en la edición del fichero
	 */
	private String generarCT() {
		
		boolean generado=false; //variable que indica si el archivo fue generado
		String prefijo="";
		String registroCT="";
		//presencia de inconsistencias
		boolean presenciaInconsistencia=false;
		
		String aux=""; //variable auxiliar para revisar caracteres especiales en los campos de texto
		int contadorRegistros=0; //variable usada para contar los registros del CT
		
		try
		{
			//apertura de archivo CT
			aux=this.revisarCaracteresEspeciales(this.numeroRemision);
			File archivoCT=new File(dirConvenio.getAbsolutePath(),ConstantesBD.ripsCT+aux+".txt");
			FileWriter streamCT=new FileWriter(archivoCT,false); //se coloca false para el caso de que esté repetido
			BufferedWriter bufferCT=new BufferedWriter(streamCT);
			
			String nombreCT = ConstantesBD.ripsCT+aux+".txt";
			
			for(int i=0;i<11;i++)
			{
				generado=false;
				registroCT="";
				
				
				switch(i)
				{
					//referente a archivo AF
					case 0:
					generado=true;
					prefijo=ConstantesBD.ripsAF;
					break;
					//referente a archivo AD
					case 1:
					generado=true;
					prefijo=ConstantesBD.ripsAD;
					break;
					//referente a archivo US
					case 2:
					generado=true;
					prefijo=ConstantesBD.ripsUS;
					break;
					//referente a archivo AC
					case 3:
					if((this.seleccionArchivos.get(ConstantesBD.ripsAC)+"").equals("true"))
						generado=true;
					prefijo=ConstantesBD.ripsAC;
					break;
					//referente a archivo AP
					case 4:
					if((this.seleccionArchivos.get(ConstantesBD.ripsAP)+"").equals("true"))
						generado=true;
					prefijo=ConstantesBD.ripsAP;
					break;
					//referente a archivo AH
					case 5:
					if((this.seleccionArchivos.get(ConstantesBD.ripsAH)+"").equals("true"))
						generado=true;
					prefijo=ConstantesBD.ripsAH;
					break;
					//referente a archivo AM
					case 6:
					if((this.seleccionArchivos.get(ConstantesBD.ripsAM)+"").equals("true"))
						generado=true;
					prefijo=ConstantesBD.ripsAM;
					break;
					//referente a archivo AN
					case 7:
					if((this.seleccionArchivos.get(ConstantesBD.ripsAN)+"").equals("true"))
						generado=true;
					prefijo=ConstantesBD.ripsAN;
					break;
					//referente a archivo AT
					case 8:
					if((this.seleccionArchivos.get(ConstantesBD.ripsAT)+"").equals("true"))
						generado=true;
					prefijo=ConstantesBD.ripsAT;
					break;
					//referente a archivo AU
					case 9:
					if((this.seleccionArchivos.get(ConstantesBD.ripsAU)+"").equals("true"))
						generado=true;
					prefijo=ConstantesBD.ripsAU;
					break;
					//referente a archivo CT
					case 10:
						generado=true;
						prefijo=ConstantesBD.ripsCT;
					break;
				}
				
				if(generado)
				{
					//se continua con la edición del registro
					//CAMPO 1*************CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD*************
					if(institucion.getCodMinSalud().trim().equals(""))
					{
						if(!presenciaInconsistencia)
						{
							
							this.generarInconsistencia(
									ConstantesBD.ripsCT,
									"código del prestador de servicios de salud",
									"campo sin información");
							
						}
						this.huboInconsistencias=true;
						presenciaInconsistencia=true;
						//registroCT+="          ";
					}
					else
					{
						aux=this.revisarCaracteresEspeciales(institucion.getCodMinSalud());
						if(aux.trim().length()>12)
							aux = aux.trim().substring(0,12);
						//registroCT+=this.editarEspacios(aux.trim(),aux.trim().length(),12,false);
						registroCT+=aux.trim();
						
						
					}
					//CAMPO 2***********FECHA DE REMISIÓN*********************************
					
						registroCT+=","+this.fechaRemision;
					//CAMPO 3***********CÓDIGO DEL ARCHIVO***********************
						
						aux=this.revisarCaracteresEspeciales(this.numeroRemision);
						String codigoArchivo=prefijo+aux;
						registroCT+=","+codigoArchivo.trim();
					//CAMPO 4**********TOTAL REGISTROS************************
					String totalRegistros;
					if(prefijo.equals(ConstantesBD.ripsCT))
					{
						if(contadorRegistros>1)
						{
							contadorRegistros++;
							totalRegistros=contadorRegistros+"";
							registroCT+=","+totalRegistros.trim();
						}
						else
							totalRegistros="0";
					}
					else
					{
						
						if(this.numeroRegistrosCreados.get(prefijo)!=null)
							totalRegistros=this.numeroRegistrosCreados.get(prefijo)+"";
						else
							totalRegistros="0";
						registroCT+=","+totalRegistros.trim();
					}
					//************************FIN**********************************
					//SEGÚN TIPO DE RIPS SE TOMA LA DECISIÓN CORRECTA
					if(!totalRegistros.equals("0"))
					{	
						if(i>0)
						{
							registroCT="\r\n"+registroCT;
						}
						bufferCT.write(registroCT);
						contadorRegistros++;
					}
					
				}
			}
			bufferCT.close();
			if(contadorRegistros<1)
			{
				//archivo sin registros
				archivoCT.delete();
				return null;
			}
			else
			{	
				//LLENO EL ARRAY LIST PARA GENERAR LA DESCARGA
				//logger.info("Archivo CT ->"+nombreCT);
				archivosGenerados.add(nombreCT);
			}
			
			//SEGÚN EL TIPO DE RIPS SE RETORNA EL PATH ESPECÍFICO
			if(this.tipoRips.equals("Cartera"))
			{
				if(esFactura)
					return "../ripsFactura/ripsFactura.do?estado=detalle&archivo=CT&nitEntidad="+this.nitEntidad;
				else
					return "../ripsCuentaCobro/ripsCuentaCobro.do?estado=detalle&archivo=CT&nitEntidad="+this.nitEntidad;
			}
			else if(this.tipoRips.equals("Capitacion"))
				return "../ripsCapitacion/ripsCapitacion.do?estado=detalle&archivo=CT&nitEntidad="+this.nitEntidad;
			else
				return "../ripsRangos/ripsRangos.do?estado=detalle&archivo=CT&nitEntidad="+this.nitEntidad;
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo CT al generarlo: "+e);
			this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo CT: "+e));
			return null;
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo CT: "+e);
			this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo CT: "+e));
			return null;
		}
	}

	/**
	 * Método para insertar las inconsistencias del archivo RIPS en el archivo Incon
	 * @param prefijo
	 * @param campo
	 * @param observacion
	 */
	private void generarInconsistencia(String prefijo,String campo, String observacion) {
		String registroIncon="";
		String aux="";
		try
		{
			//apertura de archivo Incon
			aux=this.revisarCaracteresEspeciales(this.numeroRemision);
			File archivoIncon=new File(dirConvenio.getAbsolutePath(),ConstantesBD.ripsInconsistencias+aux+".txt");
			FileWriter streamIncon=new FileWriter(archivoIncon,true); 
			BufferedWriter bufferIncon=new BufferedWriter(streamIncon);
			
			String nombreIncon = ConstantesBD.ripsInconsistencias+aux+".txt";
			
			//LOS RIPS DE CONSULTORIOS YA TIENEN UN ENCABEZADO DE 
			//LA INCONSISTENCIAS GENERALES
			if(this.tipoRips.equals("Cartera")||this.tipoRips.equals("Capitacion"))
				registroIncon+="Archivo RIPS   Nombre del Campo                                     "+
				"Observaciones\r\n";
			
			registroIncon+=this.editarEspacios(prefijo,prefijo.length(),15,false)+
			this.editarEspacios(campo,campo.length(),53,false)+
			observacion+//this.editarEspacios(observacion,observacion.length(),53,false)+
			"\r\n\r\n\r\n";
			
			bufferIncon.write(registroIncon);
			bufferIncon.close();
			

			//LLENO EL ARRAY LIST PARA GENERAR LA DESCARGA
			logger.info("Archivo Inconsistencia ->"+nombreIncon);
			archivosGenerados.add(nombreIncon);
			
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo Incon al generar inconsistencia "+prefijo+": "+e);
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo Incon al generar inconsistencia "+prefijo+": "+e);
		}
		
		
	}

	/**
	 * Método para generar el archivo US
	 * @param con
	 * @return la ruta para acceder al resumen del archivo
	 * si retorna nulo es que hubo error en la edición del fichero
	 */
	@SuppressWarnings({ "rawtypes", "deprecation", "unchecked" })
	private String generarUS(Connection con) 
	{
		HashMap listadoUS;
		String registroUS="";
		int numeroRegistros; //número de registros cosnultado desde la BD
		int contadorRegistros=0; //cuenta del número de registros insertados en el fichero
		
		String aux="";//variable auxiliar para la revisión de los caracteres especiales en los campos de texto
		
		
		//vector de boolean para el manejo de cada una de las inconsistencias generales-----------------
		// 0=> código de la entidad administradora
		// 1=> tipo de usuario
		boolean presenciaInconsistenciaGeneral[]=new boolean[2];
		for(int i=0;i<2;i++)
			presenciaInconsistenciaGeneral[i]=false;
		
		//datos para comparar el paciente anterior con el paciente actual
		//con el fin de que no hayan pacientes repetidos en el archivo
		String numeroIdAnterior="";
		String tipoIdAnterior="";
		boolean repetido=true;
		
		//ejecucion de la consulta de datos
		//RIPS CARTERA*********************************
		if(this.tipoRips.equals("Cartera"))
		{
			if (this.numeroEnvio.equals("")){
				if(this.esFactura)
					listadoUS=this.consultaFacturaUS(con,this.fechaInicial,this.fechaFinal,this.convenio,institucion.getCodigo(), false, this.nroFacturaAx);
				else
					listadoUS=this.consultaCuentaCobroUS(con,this.numeroCuentaCobro,institucion.getCodigo());
			}
			else
			{
				listadoUS=this.consultaFacturaUS(con,this.fechaInicial,this.fechaFinal,this.convenio,institucion.getCodigo(), true, this.nroFacturaAx);
			}
		}
		//RIPS CAPITACION*********************************
		else if(this.tipoRips.equals("Capitacion"))
		{
			listadoUS = this.consultaCapitacionUS(con, institucion.getCodigo());
		}
		else
		{
			listadoUS=this.consultaConsultoriosUS(con,this.getNumeroFactura(),this.getNumeroRemision());
		}
		
		numeroRegistros=Utilidades.convertirAEntero(listadoUS.get("numRegistros")+"");
		
		try
		{
			//apertura de archivo US
			aux=this.revisarCaracteresEspeciales(this.numeroRemision);
			File archivoUS=new File(dirConvenio.getAbsolutePath(),ConstantesBD.ripsUS+aux+".txt");
			FileWriter streamUS=new FileWriter(archivoUS,false); //se coloca false para el caso de que esté repetido
			BufferedWriter bufferUS=new BufferedWriter(streamUS);
			
			String nombreUS = ConstantesBD.ripsUS+aux+".txt";
			
			for(int i=0;i<numeroRegistros;i++)
			{
				repetido=true;
				if(numeroIdAnterior.equals("")&&tipoIdAnterior.equals(""))
				{
					numeroIdAnterior=listadoUS.get("numero_identificacion_"+i)+"";
					tipoIdAnterior=listadoUS.get("tipo_identificacion_"+i)+"";
					repetido=false;
				}
				//Se revisa que el paciente no sea el mismo
				else if(!numeroIdAnterior.equals(listadoUS.get("numero_identificacion_"+i)+"")||
						!tipoIdAnterior.equals(listadoUS.get("tipo_identificacion_"+i)+""))
				{
					numeroIdAnterior=listadoUS.get("numero_identificacion_"+i)+"";
					tipoIdAnterior=listadoUS.get("tipo_identificacion_"+i)+"";
					repetido=false;
				}
				if(!repetido)
				{
					
					//PREGUNTO POR AX_RIPS
					if(this.esAxRips)
					{
						objConvenio.setCodigoMinSalud(listadoUS.get("codigo_min_salud_"+i)+"");
						objConvenio.setTipoRegimen(listadoUS.get("tipo_regimen_"+i)+"");
					}
					
					
					//edicion de registro
					registroUS="";
					
					if(contadorRegistros>0)
					{
						registroUS+="\r\n";
					}
					
					//CAMPO 1 **********TIPO DE IDENTIFICACION DE USUARIO**************
					if((listadoUS.get("tipo_identificacion_"+i)+"").trim().equals(""))
					{
						this.editarInconsistencia(listadoUS,
								ConstantesBD.ripsUS,i,
								"tipo identificacion usuario",
								"campo sin información");
						this.huboInconsistencias=true;
						//registroUS+="  ";
					}
					else
					{
						String tipoId=(listadoUS.get("tipo_identificacion_"+i)+"").trim();
						if(!tipoId.equals("CC")&&!tipoId.equals("CE")&&!tipoId.equals("PA")&&!tipoId.equals("RC")&&
							!tipoId.equals("TI")&&!tipoId.equals("AS")&&!tipoId.equals("MS")&&!tipoId.equals("NU"))
						{
							this.editarInconsistencia(listadoUS,
									ConstantesBD.ripsUS,i,
									"tipo identificacion usuario",
									"campo con datos inválidos");
							this.huboInconsistencias=true;
							//registroUS+="  ";
						}
						else
						{
							registroUS+=tipoId;
						}
					}
					//CAMPO 2 **********NÚMERO DE IDENTIFICACIÓN DE USUARIO************
						
						if((listadoUS.get("numero_identificacion_"+i)+"").trim().equals(""))
						{
							this.editarInconsistencia(listadoUS,
									ConstantesBD.ripsUS,i,
									"número identificacion usuario",
									"campo sin información");
							this.huboInconsistencias=true;
							registroUS+=",";
						}
						else
						{
							aux=this.revisarCaracteresEspeciales(listadoUS.get("numero_identificacion_"+i)+"");
							
							if(aux.trim().length()>20)
								aux = aux.trim().substring(0,20);
							
							//registroUS+=","+this.editarEspacios(aux.trim(),aux.trim().length(),20,false);
							registroUS+=","+aux.trim();
						}
					
					//CAMPO 3************CÓDIGO ENTIDAD ADMINISTRADORA***********************************************
					
					if(!presenciaInconsistenciaGeneral[0])
					{
						if(objConvenio.getCodigoMinSalud().trim().equals(""))
						{
							if(this.tipoRips.equals("Cartera"))
								aux="campo sin información (para todas las facturas)";
							else
								aux="campo sin información";
							
							this.editarInconsistencia(listadoUS,
									ConstantesBD.ripsUS,i,
									"código entidad administradora",
									aux);
							this.huboInconsistencias=true;
							presenciaInconsistenciaGeneral[0]=true;
							registroUS+=",";
						}
						else
						{
							aux=this.revisarCaracteresEspeciales(objConvenio.getCodigoMinSalud());
							
							if(aux.trim().length()>6)
								aux = aux.trim().substring(0,6);
							
							//registroUS+=","+this.editarEspacios(aux.trim(),aux.trim().length(),6,false);
							registroUS+=","+aux.trim();
							
						}
					}
					else
					{
						aux=this.revisarCaracteresEspeciales(objConvenio.getCodigoMinSalud());
						registroUS+=",";
					}
					//CAMPO 4 **********TIPO DE USUARIO*******************************
					if(!presenciaInconsistenciaGeneral[1])
					{
						if(objConvenio.getTipoRegimen().trim().equals(""))
						{
							if(this.tipoRips.equals("Cartera"))
								aux="campo sin información (para todas las facturas)";
							else
								aux="campo sin información";
							
							this.editarInconsistencia(listadoUS,
									ConstantesBD.ripsUS,i,
									"tipo de usuario",
									aux);
							this.huboInconsistencias=true;
							presenciaInconsistenciaGeneral[1]=true;
							registroUS+=",";
						}
						else
						{
							if(objConvenio.getTipoRegimen().equals("C") && (listadoUS.get("desplazado_"+i)+"").trim().equals(ConstantesBD.acronimoNo))
								registroUS+=",1";
							else if(objConvenio.getTipoRegimen().equals("S") && (listadoUS.get("desplazado_"+i)+"").trim().equals(ConstantesBD.acronimoNo))
								registroUS+=",2";
							else if(objConvenio.getTipoRegimen().equals("V") && (listadoUS.get("desplazado_"+i)+"").trim().equals(ConstantesBD.acronimoNo))
								registroUS+=",3";
							else if(objConvenio.getTipoRegimen().equals("P") && (listadoUS.get("desplazado_"+i)+"").trim().equals(ConstantesBD.acronimoNo))
								registroUS+=",4";
							else if(objConvenio.getTipoRegimen().equals("O") && (listadoUS.get("desplazado_"+i)+"").trim().equals(ConstantesBD.acronimoNo))
								registroUS+=",5";
							else if(objConvenio.getTipoRegimen().equals("C") && (listadoUS.get("desplazado_"+i)+"").trim().equals(ConstantesBD.acronimoSi))
								registroUS+=",6";
							else if(objConvenio.getTipoRegimen().equals("S") && (listadoUS.get("desplazado_"+i)+"").trim().equals(ConstantesBD.acronimoSi))
								registroUS+=",7";
							else if(objConvenio.getTipoRegimen().equals("V") && (listadoUS.get("desplazado_"+i)+"").trim().equals(ConstantesBD.acronimoSi))
								registroUS+=",8";
							
							else
							{
								this.editarInconsistencia(listadoUS,
										ConstantesBD.ripsUS,i,
										"tipo de usuario",
										"campo con datos inválidos");
								this.huboInconsistencias=true;
								presenciaInconsistenciaGeneral[1]=true;
								registroUS+=",";
							}
						}
					}
					else
						registroUS+=",";
					//CAMPO 5 *****PRIMER APELLIDO USUARIO********************
					
						if((listadoUS.get("primer_apellido_"+i)+"").trim().equals(""))
						{
							this.editarInconsistencia(listadoUS,
									ConstantesBD.ripsUS,i,
									"primer apellido usuario",
									"campo sin información");
							this.huboInconsistencias=true;
							registroUS+=",";
						}
						else
						{
							aux=this.revisarCaracteresEspeciales(listadoUS.get("primer_apellido_"+i)+"");
							
							
							if(aux.trim().length()>30)
								aux = aux.trim().substring(0,30);
							
							//registroUS+=","+this.editarEspacios(aux.trim(),aux.trim().length(),30,false);
							registroUS+=","+aux.trim();
							
						}
					
					//CAMPO 6********SEGUNDO APELLIDO USUARIO********************************
						aux=this.revisarCaracteresEspeciales(listadoUS.get("segundo_apellido_"+i)+"");
						
						if(aux.trim().length()>30)
							aux = aux.trim().substring(0,30);
						
						//registroUS+=","+this.editarEspacios(aux.trim(),aux.trim().length(),30,false);
						registroUS+=","+aux.trim();
						
					
					//CAMPO 7*******PRIMER NOMBRE USUARIO********************************
					
						if((listadoUS.get("primer_nombre_"+i)+"").trim().equals(""))
						{
							this.editarInconsistencia(listadoUS,
									ConstantesBD.ripsUS,i,
									"primer nombre usuario",
									"campo sin información");
							this.huboInconsistencias=true;
							registroUS+=",";
						}
						else
						{
							aux=this.revisarCaracteresEspeciales(listadoUS.get("primer_nombre_"+i)+"");
							
							if(aux.trim().length()>20)
								aux = aux.trim().substring(0,20);
							
							//registroUS+=","+this.editarEspacios(aux.trim(),aux.trim().length(),20,false);
							registroUS+=","+aux.trim();
							
						}
					
					//CAMPO 8********SEGUNDO NOMBRE USUARIO******************************
						aux=this.revisarCaracteresEspeciales(listadoUS.get("segundo_nombre_"+i)+"");
						
						if(aux.trim().length()>20)
							aux = aux.trim().substring(0,20);
						
						//registroUS+=","+this.editarEspacios(aux.trim(),aux.trim().length(),20,false);
						registroUS+=","+aux.trim();
						
					
					//CAMPO 9 Y 10*******EDAD Y UNIDAD DE MEDIDA DE LA EDAD***************************
						
						
						
						InfoDatos edad=this.calcularEdad((listadoUS.get("fecha_nacimiento_"+i)+""),(listadoUS.get("fecha_apertura_"+i)+""));
						
						if(edad.getCodigo()<0)
						{
							this.editarInconsistencia(listadoUS,
									ConstantesBD.ripsUS,i,
									"edad y unidad de medida de edad",
									"fecha de Nacimiento mayor a la fecha de atención");
							this.huboInconsistencias=true;
							registroUS+=",,";
						}
						else
						{
							//edicion de la edad
							//registroUS+=","+this.editarEspacios(edad.getCodigo()+"",(edad.getCodigo()+"").length(),3,true);
							registroUS+=","+edad.getCodigo();
							//edición del formato de la edad 1=Años, 2=Meses, 3=Días
							registroUS+=","+edad.getValue();
						}
					
					//CAMPO 11***************SEXO****************************************************
					
						if((listadoUS.get("sexo_"+i)+"").equals(""))
						{
							this.editarInconsistencia(listadoUS,
									ConstantesBD.ripsUS,i,
									"sexo",
									"campo sin información");
							this.huboInconsistencias=true;
							registroUS+=",";
						}
						else
						{
							
							if((listadoUS.get("sexo_"+i)+"").equals("2"))
							{
								registroUS+=",F";
							}
							else if((listadoUS.get("sexo_"+i)+"").equals("1"))
							{
								registroUS+=",M";
							}
							else
							{
								this.editarInconsistencia(listadoUS,
										ConstantesBD.ripsUS,i,
										"sexo",
										"campo con datos inválidos");
								this.huboInconsistencias=true;
								registroUS+=",";
							}
						}
					
					//CAMPO 12 **********CÓDIGO DEL DEPARTAMENTO DE RESIDENCIA HABITUAL**************
					
						if((listadoUS.get("depto_vivienda_"+i)+"").equals(""))
						{
							this.editarInconsistencia(listadoUS,
									ConstantesBD.ripsUS,i,
									"código departamento vivienda",
									"campo sin información");
							this.huboInconsistencias=true;
							registroUS+=",";
						}
						else
						{
							String deptoVivienda=(listadoUS.get("depto_vivienda_"+i)+"").trim();
							//se valida que el codigo del departamento de vivienda sea positivo
							if(Utilidades.convertirAEntero(deptoVivienda)>0)
							{
								if(deptoVivienda.length()>2)
									deptoVivienda = deptoVivienda.substring(0,2);
								
								registroUS+=","+this.editarCeros(deptoVivienda,deptoVivienda.length(),2);
								
							}
							else
							{
								this.editarInconsistencia(listadoUS,
										ConstantesBD.ripsUS,i,
										"código departamento vivienda",
										"campo con datos inválidos");
								this.huboInconsistencias=true;
								registroUS+=",";
							}
						}
					
					//CAMPO 13 ***********CÓDIGO DEL MUNICIPIO DE RESIDENCIA HABITUAL***************
					
						if((listadoUS.get("ciudad_vivienda_"+i)+"").equals(""))
						{
							this.editarInconsistencia(listadoUS,
									ConstantesBD.ripsUS,i,
									"código ciudad vivienda",
									"campo sin información");
							this.huboInconsistencias=true;
							registroUS+=",";
						}
						else
						{
							String ciudadVivienda=(listadoUS.get("ciudad_vivienda_"+i)+"").trim();
							//se verifica que el código sea positivo
							if(Utilidades.convertirAEntero(ciudadVivienda)>0)
							{
								if(ciudadVivienda.length()>3)
									ciudadVivienda.substring(0,3);
								
								registroUS+=","+this.editarCeros(ciudadVivienda,ciudadVivienda.length(),3);
								
							}
							else
							{
								this.editarInconsistencia(listadoUS,
										ConstantesBD.ripsUS,i,
										"código ciudad vivienda",
										"campo con datos inválidos");
								this.huboInconsistencias=true;
								registroUS+=",";
							}
						}
					
					//CAMPO 14 *********ZONA DE RESIDENCIA HABITUAL****************
					
						if((listadoUS.get("zona_domicilio_"+i)+"").equals(""))
						{
							this.editarInconsistencia(listadoUS,
									ConstantesBD.ripsUS,i,
									"zona de residencia",
									"campo sin información");
							this.huboInconsistencias=true;
							registroUS+=",";
						}
						else
						{
							String zona=(listadoUS.get("zona_domicilio_"+i)+"").trim();
							if(zona.equals("U")||zona.equals("R"))
								registroUS+=","+zona;
							else
							{
								this.editarInconsistencia(listadoUS,
										ConstantesBD.ripsUS,i,
										"zona de residencia",
										"campo con datos inválidos");
								this.huboInconsistencias=true;
								registroUS+=",";
							}
						}
					
					//*************FIN**************************************
					//si no hubo inconsistencias entonces se hace una inserción del registro en el archivo
					//SEGÚN EL TIPO DE RIPS SE TOMAN LAS DECISIONES RESPECITVAS
					
					bufferUS.write(registroUS);
					contadorRegistros++;
					
				}//fin if
			} //fin for
			
			//se almacena el número de registros creados para el archivo CT
			this.numeroRegistrosCreados.put(ConstantesBD.ripsUS,contadorRegistros+"");
			
			bufferUS.close();
			if(contadorRegistros<1)
			{
				//archivo sin registros
				archivoUS.delete();
				return null;
			}
			else
			{	
				//LLENO EL ARRAY LIST PARA GENERAR LA DESCARGA
				//logger.info("Archivo US ->"+nombreUS);
				archivosGenerados.add(nombreUS);
			}
			
			//SEGÚN EL TIPO DE RIPS SE RETORNA EL PATH RESPECTIVO
			if(this.tipoRips.equals("Cartera"))
			{
				if(esFactura)
					return "../ripsFactura/ripsFactura.do?estado=detalle&archivo=US&nitEntidad="+this.nitEntidad;
				else
					return "../ripsCuentaCobro/ripsCuentaCobro.do?estado=detalle&archivo=US&nitEntidad="+this.nitEntidad;
			}
			else if(this.tipoRips.equals("Capitacion"))
				return "../ripsCapitacion/ripsCapitacion.do?estado=detalle&archivo=US&nitEntidad="+this.nitEntidad;
			else
				return "../ripsRangos/ripsRangos.do?estado=detalle&archivo=US&nitEntidad="+this.nitEntidad;
			
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo US al generarlo: "+e);
			this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo US: "+e));
			return null;
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo US: "+e);
			this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo US: "+e));
			return null;
		}
		
	}

	/**
	 * Método para llenar de ceros a la izquierda un campo numérico
	 * @param cadena
	 * @param inicio
	 * @param fin
	 * @return
	 */
	private String editarCeros(String cadena, int inicio, int fin) {
		String aux;
		String ceros="";
		
		for(int i=inicio+1;i<=fin;i++)
			ceros+="0";
		
		aux=ceros+cadena;
		
		return aux;
	}

	/**
	 * Método que calcula la fecha de un paciente desde la fecha de nacimiento
	 * hasta la fecha de apertura de la cuenta y retorna un formato:
	 *  1=> Años
	 * 	2=> Meses
	 * 	3=> Días
	 * @param fecha nacimiento
	 * @param fecha apertura
	 * @return InfoDatos
	 */
	private InfoDatos calcularEdad(String fechaNacimiento, String fechaApertura) {
		String fechaN[]=UtilidadFecha.conversionFormatoFechaAAp(fechaNacimiento).split("/");
		String fechaA[]=UtilidadFecha.conversionFormatoFechaAAp(fechaApertura).split("/");
		String formato;
		int edad;
		
		formato = "1";
		edad = UtilidadFecha.calcularEdad(fechaN[0],fechaN[1],fechaN[2],Utilidades.convertirAEntero(fechaA[0]),Utilidades.convertirAEntero(fechaA[1]),Utilidades.convertirAEntero(fechaA[2]));
		
		//Si no es en años se calcula meses
		if(edad<=0)
		{
			formato = "2";
			edad = UtilidadFecha.numeroMesesEntreFechas(UtilidadFecha.conversionFormatoFechaAAp(fechaNacimiento),UtilidadFecha.conversionFormatoFechaAAp(fechaApertura),false);
			
			//Si no es meses se calcula días
			if(edad<=0)
			{
				formato = "3";
				edad = UtilidadFecha.numeroDiasEntreFechas(UtilidadFecha.conversionFormatoFechaAAp(fechaNacimiento),UtilidadFecha.conversionFormatoFechaAAp(fechaApertura));
			}
		}
		
		
		//codigo=> edad, value=>formato
		return new InfoDatos(edad,formato);
	}

	/**
	 * Método para generar el archivo AD
	 * @param con
	 * @return la ruta para acceder al resumen del archivo
	 * si retorna nulo es que hubo error en la edición del fichero
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String generarAD(Connection con) 
	{
		HashMap listadoAD;
		String registroAD="";
		int numeroRegistros; //número de registros cosnultado desde la BD
		int contadorRegistros=0; //cuenta del número de registros insertados en el fichero
		int contadorFactura=0; //contador de registros por factura
		String numeroFacturaAnterior=""; //permite llevar el control de los registros de una solo factura
		
		String aux=""; //variable auxiliar para la revisión de caracteres especiales en los campos texto
		
		//ejecucion de la consulta de datos
		//RIPS CARTERA ***************************************
		if(this.tipoRips.equals("Cartera"))
		{
			// verificar si el campo numero de envio esta vacio			
			if (this.numeroEnvio.equals("")){
				if(this.esFactura)
					listadoAD=this.consultaFacturaAD(con,this.fechaInicial,this.fechaFinal,this.convenio,institucion.getCodigo(), false, this.nroFacturaAx);
				else
					listadoAD=this.consultaCuentaCobroAD(con,this.numeroCuentaCobro,institucion.getCodigo());
			}
			else{
				
				listadoAD=this.consultaFacturaAD(con,this.fechaInicial,this.fechaFinal,this.convenio,institucion.getCodigo(), true, this.nroFacturaAx);
			}
			//Se agrupa el listado obtenido por naturaleza de servicio
			listadoAD = organizarListadoADCartera(listadoAD);
		}
		//RIPS CAPITACION *********************************************
		else if(this.tipoRips.equals("Capitacion"))
		{
			listadoAD = this.consultaCapitacionAD(con, institucion.getCodigo());
			//Se agrupa el listado obtenido por naturaleza de servicio
			listadoAD = organizarListadoADCapitacion(listadoAD);
		}
		//RIPS CONSULTORIOS *********************************************
		else
		{
			listadoAD=this.consultaConsultoriosAD(con,this.getNumeroFactura(),this.getNumeroRemision());
		}
		
		numeroRegistros=Utilidades.convertirAEntero(listadoAD.get("numRegistros")+"");
		
		
		
		try
		{
			//apertura de archivo AD
			aux=this.revisarCaracteresEspeciales(this.numeroRemision);
			File archivoAD=new File(dirConvenio.getAbsolutePath(),ConstantesBD.ripsAD+aux+".txt");
			FileWriter streamAD=new FileWriter(archivoAD,false); //se coloca false para el caso de que esté repetido
			BufferedWriter bufferAD=new BufferedWriter(streamAD);
			
			String nombreAD = ConstantesBD.ripsAD+aux+".txt";
			
			for(int i=0;i<numeroRegistros;i++)
			{
				//si no hubo inconsistencias entonces se hace una inserción del registro en el archivo
				//se verifica si el registro ha cambiado de factura
				//en el caso de que ya haya cambiado de factura ya se pueden insertar 
				//todos los registros relacionados con la factura 
				if(numeroFacturaAnterior.equals(""))
					numeroFacturaAnterior=listadoAD.get("consecutivo_"+i)+"";
				else if(!numeroFacturaAnterior.equals(listadoAD.get("consecutivo_"+i)+""))
				{
					
					bufferAD.write(registroAD);
					contadorRegistros+=contadorFactura;
					contadorFactura=0;
					numeroFacturaAnterior=listadoAD.get("consecutivo_"+i)+"";
					registroAD="";
				}
				
				if(contadorFactura>0||contadorRegistros>0)
				{
					registroAD+="\r\n";
				}
				
				//edicion de registro
				//CAMPO1 *************NÚMERO DE LA FACTURA**************************************
				if(this.ripsConFactura)
				{
					aux = (listadoAD.get("consecutivo_"+i)+"").trim();
					if(aux.trim().length()>20)
						aux = aux.trim().substring(0,20);
					
					//registroAD+=this.editarEspacios(aux,aux.length(),20,false);
					registroAD+=aux;
				}
				/*else
					registroAD+="                    ";*/
				
				//CAMPO 2 ***********CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD****************
				aux = institucion.getCodMinSalud().trim();
				if(aux.length()>12)
					aux = aux.substring(0,12);
				
				aux=this.revisarCaracteresEspeciales(aux);
				//registroAD+=","+this.editarEspacios(aux.trim(),aux.trim().length(),12,false);
				registroAD+=","+aux.trim();
				
				//CAMPO 3**********CÓDIGO DEL CONCEPTO****************************
				aux = (listadoAD.get("naturaleza_servicio_"+i)+"").trim();
				if(aux.trim().length()>2)
					aux = aux.trim().substring(0,2);
				
				//registroAD+=","+this.editarEspacios(aux,aux.length(),2,false);
				registroAD+=","+aux;
				
				//CAMPO 4*********CANTIDAD************************
				aux = (listadoAD.get("cantidad_"+i)+"").trim();
				if(aux.trim().length()>15)
					aux = aux.trim().substring(0,15);
				
				//registroAD+=","+this.editarEspacios(aux,aux.length(),15,true);
				registroAD+=","+aux;
					
				
				//CAMPO 5************VALOR UNITARIO***************************
				registroAD+=",0";
				//CAMPO 6************VALOR TOTAL POR CONCEPTO*****************************
				if(this.ripsConFactura)
				{
					if((listadoAD.get("total_"+i)+"").equals(""))
					{
						registroAD+=",0";
					}
					else
					{
						String total=(listadoAD.get("total_"+i)+"").trim();
						total = UtilidadTexto.formatearValores(total,"######0.00");
						
						//Se quitan los decimales en ceros
						if(total.endsWith(".00"))
							total = total.replace(".00", "");
						
						if(total.trim().length()>15)
							total = total.trim().substring(0,15);
						
						//registroAD+=","+this.editarEspacios(total,total.length(),15,true);
						registroAD+=","+total;
					}
				}
				else
				{
					registroAD+=",";
				}
				
				//*********FIN******************
				
				
				contadorFactura++;
				
					
				
			}
			//revisión para el último elemento
			//SEGÚN EL TIPO DE RIPS SE TOMA LA DECISIÓN NECESARIA
			
			bufferAD.write(registroAD);
			contadorRegistros+=contadorFactura;
			
			//se almacena el número de registros creados para el archivo CT
			this.numeroRegistrosCreados.put(ConstantesBD.ripsAD,contadorRegistros+"");
			
			bufferAD.close();
			
			if(contadorRegistros<1)
			{
				//archivo sin registros
				archivoAD.delete();
				return null;
			}
			else
			{	
				//LLENO EL ARRAY LIST PARA GENERAR LA DESCARGA
				//logger.info("Archivo AD ->"+nombreAD);
				archivosGenerados.add(nombreAD);
			}
			
			//SEGÚN EL TIPO DE RIPS SE RETORNA EL PATH ESPECÍFICO
			if(this.tipoRips.equals("Cartera"))
			{
				if(esFactura)
					return "../ripsFactura/ripsFactura.do?estado=detalle&archivo=AD&nitEntidad="+this.nitEntidad;
				else
					return "../ripsCuentaCobro/ripsCuentaCobro.do?estado=detalle&archivo=AD&nitEntidad="+this.nitEntidad;
			}
			else if(this.tipoRips.equals("Capitacion"))
				return "../ripsCapitacion/ripsCapitacion.do?estado=detalle&archivo=AD&nitEntidad="+this.nitEntidad;
			else
				return "../ripsRangos/ripsRangos.do?estado=detalle&archivo=AD&nitEntidad="+this.nitEntidad;
			
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo AD al generarlo: "+e);
			this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo AD: "+e));
			return null;
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo AD: "+e);
			this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo AD: "+e));
			return null;
		}
	}

	/**
	 * Método implementado para organizar el listado de AD agrupando por naturaleza de servicio
	 * @param listadoAD
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private HashMap organizarListadoADCapitacion(HashMap listadoAD) 
	{
		HashMap mapa = new HashMap();
		int numRegistros = 0;
		String consecutivoFactura = "";
		String naturaleza = "";
		String tipoServicio = "";
		String esMedicamento = "";
		String esPos = "";
		boolean inconsistenciaGeneral = false;
		int cantidad = 0;
		double total = 0;
		
		
		for(int i=0;i<Utilidades.convertirAEntero(listadoAD.get("numRegistros").toString());i++)
		{
			if(!consecutivoFactura.equals(listadoAD.get("consecutivo_factura_"+i).toString()))
			{
				inconsistenciaGeneral = false;
				consecutivoFactura = listadoAD.get("consecutivo_factura_"+i).toString();
			}
			
			if(!naturaleza.equals(listadoAD.get("naturaleza_servicio_"+i).toString()))
			{
				//Si ya se había registrado una naturaleza se registra
				if(!naturaleza.equals(""))
				{
					mapa.put("consecutivo_"+numRegistros,listadoAD.get("consecutivo_"+(i-1)));
					mapa.put("naturaleza_servicio_"+numRegistros,naturaleza);
					mapa.put("cantidad_"+numRegistros,cantidad+"");
					mapa.put("total_"+numRegistros,"0");
					numRegistros++;
				}
				
				cantidad = 0;
				total = 0;
				
				
				naturaleza = listadoAD.get("naturaleza_servicio_"+i).toString();
			}
			
			//1) Se verifica inconsistencias con el consecutivo de la factura*****************************************
			if(consecutivoFactura.equals("")&&!inconsistenciaGeneral)
			{
				this.editarInconsistencia(listadoAD,ConstantesBD.ripsAD,i,"número de la factura","campo sin información");
				this.huboInconsistencias=true;
				inconsistenciaGeneral=true;
			}
			//2) Se verifica inconsistencias con el***********************************************************
			if(naturaleza.equals(""))
			{
				this.editarInconsistencia(listadoAD,ConstantesBD.ripsAD,i,"naturaleza","campo sin información");
				this.huboInconsistencias=true;
			}
			//se verifica que se encuentre en el rango 01 a 14
			else
			{
				boolean valido = true;
				
				try
				{
					Utilidades.convertirAEntero(naturaleza);
				}
				catch(Exception e)
				{
					logger.error("La naturaleza del servicio/articulo no es numérico");
					this.editarInconsistencia(listadoAD,ConstantesBD.ripsAD,i,"naturaleza","debe ser numérico");
					this.huboInconsistencias=true;
					valido = false;
				}
				
				//Verificar que la naturaleza correcponda según el tipo de servicio
				tipoServicio = listadoAD.get("tipo_servicio_"+i).toString();
				esMedicamento = listadoAD.get("es_medicamento_"+i).toString();
				esPos = listadoAD.get("es_pos_"+i).toString();
				
				if(
						valido
						&&
						(
						//Verifica que los cargos que son de servicios tengan la naturaleza correcta
						!tipoServicio.equals("")&&
						!naturaleza.equals("01")&&!naturaleza.equals("02")&&!naturaleza.equals("03")&&!naturaleza.equals("04")&&
						!naturaleza.equals("05")&&!naturaleza.equals("06")&&!naturaleza.equals("07")&&!naturaleza.equals("08")&&
						!naturaleza.equals("10")&&!naturaleza.equals("14")
						||
						//Verifica que los cargos de articulos (Materiales e insumos) tengan la naturaleza correcta
						tipoServicio.equals("")&&!UtilidadTexto.getBoolean(esMedicamento)&&!naturaleza.equals("09")&&!naturaleza.equals("11")
						||
						//Verifica que los cargos de articulos (Medicamento POS) tenga la naturaleza correcta
						tipoServicio.equals("")&&UtilidadTexto.getBoolean(esMedicamento)&&UtilidadTexto.getBoolean(esPos)&&!naturaleza.equals("12")
						||
						//Verifica que los cargos de articulos (Medicamento POS) tenga la naturaleza correcta
						tipoServicio.equals("")&&UtilidadTexto.getBoolean(esMedicamento)&&!UtilidadTexto.getBoolean(esPos)&&!naturaleza.equals("13")
						)
					)
				{
					this.editarInconsistencia(listadoAD,ConstantesBD.ripsAD,i,"naturaleza","campo con datos inválidos");
					this.huboInconsistencias=true;
				}
				
				
			}
			
			cantidad += Utilidades.convertirAEntero((listadoAD.get("cantidad_cargo_"+i)+""));
			total += Double.parseDouble((listadoAD.get("valor_total_"+i)+""));
			
			
		}
		
		mapa.put("consecutivo_"+numRegistros,listadoAD.get("consecutivo_"+(Utilidades.convertirAEntero(listadoAD.get("numRegistros").toString())-1)));
		mapa.put("naturaleza_servicio_"+numRegistros,naturaleza);
		mapa.put("cantidad_"+numRegistros,cantidad+"");
		mapa.put("total_"+numRegistros,"0");
		numRegistros++;
		
		mapa.put("numRegistros",numRegistros+"");
		
		//**********SE REVISA INCONSISTENCIA GENERAL************************************
		if(institucion.getCodMinSalud().trim().equals(""))
		{
			this.editarInconsistencia(listadoAD,ConstantesBD.ripsAD,0,"código del prestador de servicios de salud","campo sin información (para todas las facturas)");
			this.huboInconsistencias=true;
			mapa.put("numRegistros","0");
		}
		
		
		
		return mapa;
	}

	/**
	 * Método implementado para organizar el listado de AD agrupando por naturaleza de servicio
	 * @param listadoAD
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private HashMap organizarListadoADCartera(HashMap listadoAD) 
	{
		HashMap mapa = new HashMap();
		int numRegistros = 0;
		String consecutivoFactura = "";
		String naturaleza = "";
		String tipoServicio = "";
		String esMedicamento = "";
		String esPos = "";
		boolean inconsistenciaGeneral = false;
		int cantidad = 0;
		double total = 0;
		
		
		for(int i=0;i<Utilidades.convertirAEntero(listadoAD.get("numRegistros").toString());i++)
		{
			/*logger.info("paso por aqui consecutivo factura N°: "+listadoAD.get("consecutivo_"+i));
			logger.info("paso por aqui naturaleza servicio N°: "+listadoAD.get("naturaleza_servicio_"+i));*/
			if(!consecutivoFactura.equals(listadoAD.get("consecutivo_"+i).toString()))
			{
				inconsistenciaGeneral = false;
				consecutivoFactura = listadoAD.get("consecutivo_"+i).toString();
			}
			
			if(!naturaleza.equals(listadoAD.get("naturaleza_servicio_"+i).toString()))
			{
				
				//Si ya se había registrado una naturaleza se registra
				if(!naturaleza.equals(""))
				{
					mapa.put("consecutivo_"+numRegistros,listadoAD.get("consecutivo_"+(i-1)));
					mapa.put("naturaleza_servicio_"+numRegistros,naturaleza);
					mapa.put("cantidad_"+numRegistros,cantidad+"");
					mapa.put("total_"+numRegistros,total+"");
					numRegistros++;
				}
					
				
				
				cantidad = 0;
				total = 0;
				
				
				naturaleza = listadoAD.get("naturaleza_servicio_"+i).toString();
			}
			
			//1) Se verifica inconsistencias con el consecutivo de la factura*****************************************
			if(consecutivoFactura.equals("")&&!inconsistenciaGeneral)
			{
				this.editarInconsistencia(listadoAD,ConstantesBD.ripsAD,i,"número de la factura","campo sin información");
				this.huboInconsistencias=true;
				inconsistenciaGeneral=true;
			}
			//2) Se verifica inconsistencias con el***********************************************************
			if(naturaleza.equals(""))
			{
				this.editarInconsistencia(listadoAD,ConstantesBD.ripsAD,i,"código del concepto","campo sin información");
				this.huboInconsistencias=true;
			}
			//se verifica que se encuentre en el rango 01 a 14
			else
			{
				boolean valido = true;
				
				try
				{
					Utilidades.convertirAEntero(naturaleza);
				}
				catch(Exception e)
				{
					logger.error("La naturaleza del servicio/articulo no es numérico");
					this.editarInconsistencia(listadoAD,ConstantesBD.ripsAD,i,"código del concepto","debe ser numérico");
					this.huboInconsistencias=true;
					valido = false;
				}
				
				
				
				
				
				//Verificar que la naturaleza correcponda según el tipo de servicio
				tipoServicio = listadoAD.get("tipo_servicio_"+i).toString();
				esMedicamento = listadoAD.get("es_medicamento_"+i).toString();
				esPos = listadoAD.get("es_pos_"+i).toString();
				
				if(
						valido
						&&
						(
						//Verifica que los cargos que son de servicios tengan la naturaleza correcta
						!tipoServicio.equals("")&&
						!naturaleza.equals("01")&&!naturaleza.equals("02")&&!naturaleza.equals("03")&&!naturaleza.equals("04")&&
						!naturaleza.equals("05")&&!naturaleza.equals("06")&&!naturaleza.equals("07")&&!naturaleza.equals("08")&&
						!naturaleza.equals("10")&&!naturaleza.equals("14")&&!naturaleza.equals("09")
						||
						//Verifica que los cargos de articulos (Materiales e insumos) tengan la naturaleza correcta
						tipoServicio.equals("")&&!UtilidadTexto.getBoolean(esMedicamento)&&!naturaleza.equals("09")&&!naturaleza.equals("11")
						||
						//Verifica que los cargos de articulos (Medicamento POS) tenga la naturaleza correcta
						tipoServicio.equals("")&&UtilidadTexto.getBoolean(esMedicamento)&&UtilidadTexto.getBoolean(esPos)&&!naturaleza.equals("12")
						||
						//Verifica que los cargos de articulos (Medicamento POS) tenga la naturaleza correcta
						tipoServicio.equals("")&&UtilidadTexto.getBoolean(esMedicamento)&&!UtilidadTexto.getBoolean(esPos)&&!naturaleza.equals("13")
						)
					)
				{
					this.editarInconsistencia(listadoAD,ConstantesBD.ripsAD,i,"naturaleza","campos con datos inválidos");
					this.huboInconsistencias=true;
				}
			}
			
			cantidad += Utilidades.convertirAEntero((listadoAD.get("cantidad_cargo_"+i)+""));
			total += Double.parseDouble((listadoAD.get("valor_total_"+i)+""));
		}
		
		
		mapa.put("consecutivo_"+numRegistros,listadoAD.get("consecutivo_"+(Utilidades.convertirAEntero(listadoAD.get("numRegistros").toString())-1)));
		mapa.put("naturaleza_servicio_"+numRegistros,naturaleza);
		mapa.put("cantidad_"+numRegistros,cantidad+"");
		mapa.put("total_"+numRegistros,total+"");
		numRegistros++;
		
		
		mapa.put("numRegistros",numRegistros+"");
		
		//**********SE REVISA INCONSISTENCIA GENERAL************************************
		if(institucion.getCodMinSalud().trim().equals(""))
		{
			this.editarInconsistencia(listadoAD,ConstantesBD.ripsAD,0,"código del prestador de servicios de salud","campo sin información (para todas las facturas)");
			this.huboInconsistencias=true;
		}
		
		
		return mapa;
	}

	/**
	 * Método para generar los archivos AF
	 * @param con
	 * @return la ruta para acceder al resumen del archivo
	 * si retorna nulo es que hubo error en la edición del fichero
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String generarAF(Connection con) 
	{
		
		HashMap listadoAF;
		String registroAF;
		int numeroRegistros; //numero de registros cosnultados desde la BD
		int contadorRegistros=0; //cuenta del número de registros insertados en el fichero
		//vector de boolean para el manejo de cada una de las inconsistencias generales-----------------
		// 0=> codigo del prestador de servicios de salud
		// 1=> razón social
		// 2=> tipo de identificacion
		// 3=> número de identificacion
		// 4=> código de la entidad administradora
		// 5=> nombre de la entidad administradora
		// 6=> número del contrato
		// 7=> plan de beneficios
		boolean presenciaInconsistenciaGeneral[]=new boolean[8];
		for(int i=0;i<8;i++)
			presenciaInconsistenciaGeneral[i]=false;
		//----------------------------------------------------------------------------------------------
		
		String aux=""; //variable auxiliar para la revisión de caracteres especiales en campos de texto
		
		//ejecución de consulta de datos según el tipo de RIPS
		//RIPS CARTERA ***************************
		if(this.tipoRips.equals("Cartera"))
		{
			
			// verificar si el campo numero de envio tiene algun valor
			if (this.numeroEnvio.equals("")){
				if(this.esFactura)
					listadoAF=this.consultaFacturaAF(con,this.fechaInicial,this.fechaFinal,this.convenio,institucion.getCodigo(), false, this.nroFacturaAx);
				else
					listadoAF=this.consultaCuentaCobroAF(con,this.numeroCuentaCobro,institucion.getCodigo());
			}
			else
			{
				listadoAF=this.consultaFacturaAF(con,this.fechaInicial,this.fechaFinal,this.convenio,institucion.getCodigo(), true, this.nroFacturaAx);
				
			}
		}
		//RIPS CAPITACION***************************
		else if(this.tipoRips.equals("Capitacion"))
		{
			listadoAF = this.consultaCapitacionAF();
			
		}
		//RIPS CONSULTORIOS*********************
		else
		{
			
			listadoAF=this.consultaConsultoriosAF(con,this.numeroFactura,this.getNumeroRemision());
		}
		
		numeroRegistros=Utilidades.convertirAEntero(listadoAF.get("numRegistros")+"");
		
		try
		{
			//apertura de archivo AF
			aux=this.revisarCaracteresEspeciales(this.numeroRemision);
			File archivoAF=new File(dirConvenio.getAbsolutePath(),ConstantesBD.ripsAF+aux+".txt");
			FileWriter streamAF=new FileWriter(archivoAF,false); //se coloca false para el caso de que esté repetido
			BufferedWriter bufferAF=new BufferedWriter(streamAF);
			
			String nombreAF = ConstantesBD.ripsAF+aux+".txt";
			
			for(int i=0;i<numeroRegistros;i++)
			{
				registroAF="";
				
				//PREGUNTO POR AX_RIPS
				if(this.esAxRips)
				{
					objConvenio.setCodigoMinSalud(listadoAF.get("codigo_min_salud_"+i)+"");
					objConvenio.setTipoRegimen(listadoAF.get("tipo_regimen_"+i)+"");
					objConvenio.setNombre(listadoAF.get("nombre_convenio_"+i)+"");
					objConvenio.setPlanBeneficios(listadoAF.get("plan_beneficios_"+i)+"");
				}
				
				
				//SEGÚN TIPO RIPS SE HACEN LAS ACCIONES NECESARIAS
				if(contadorRegistros>0)
				{
					registroAF+="\r\n";
				}
				
				//edición de registro
				//CAMPO 1************CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD***********************************************
				if(!presenciaInconsistenciaGeneral[0])
				{
					if(institucion.getCodMinSalud().trim().equals(""))
					{
						
						if(this.tipoRips.equals("Cartera"))
							aux="campo sin información (para todas las facturas)";
						else
							aux="campo sin información";
						
						//Los RIPS de Capitacion tienen una forma diferente de generar las inconsistencias
						if(this.tipoRips.equals("Capitacion"))
							this.generarInconsistencia(ConstantesBD.ripsAF, "código del prestador de servicios de salud", aux);
						else
							this.editarInconsistencia(listadoAF,ConstantesBD.ripsAF,i,"código del prestador de servicios de salud",aux);
						
						this.huboInconsistencias=true;
						presenciaInconsistenciaGeneral[0]=true;
						//registroAF+="          ";
						
					}
					else
					{
						aux=this.revisarCaracteresEspeciales(institucion.getCodMinSalud());
						
						if(aux.trim().length()>12)
							aux = aux.substring(0,12);
						
						//registroAF+=this.editarEspacios(aux.trim(),aux.trim().length(),12,false);
						registroAF+=aux.trim();
					}
				}
				else
				{
					aux=this.revisarCaracteresEspeciales(institucion.getCodMinSalud());
					//registroAF+="          ";
				}
				//CAMPO 2************RAZÓN SOCIAL***********************************************
				if(!presenciaInconsistenciaGeneral[1])
				{
					
					
					if(institucion.getRazonSocial().trim().equals(""))
					{
						if(this.tipoRips.equals("Cartera"))
							aux="campo sin información (para todas las facturas)";
						else
							aux="campo sin información";
						
						if(this.tipoRips.equals("Capitacion"))
							this.generarInconsistencia(ConstantesBD.ripsAF, "razón social", aux);
						else
							this.editarInconsistencia(listadoAF,ConstantesBD.ripsAF,i,"razón social",aux);
						
						this.huboInconsistencias=true;
						presenciaInconsistenciaGeneral[1]=true;
						registroAF+=",";
					}
					else
					{
						aux=this.revisarCaracteresEspeciales(institucion.getRazonSocial());
						if(aux.trim().length()>60)
							aux = aux.substring(0,60);
						
						//registroAF+=","+this.editarEspacios(aux.trim(),aux.trim().length(),60,false);
						registroAF+=","+aux.trim();
						
					}
				}
				else
				{
					aux=this.revisarCaracteresEspeciales(institucion.getRazonSocial());
					registroAF+=",";
				}
				//CAMPO 3************TIPO DE IDENTIFICACIÓN***********************************************
				if(!presenciaInconsistenciaGeneral[2])
				{
					if(institucion.getIdentificacion().trim().equals(""))
					{
						if(this.tipoRips.equals("Cartera"))
							aux="campo sin información (para todas las facturas)";
						else
							aux="campo sin información";
						
						if(this.tipoRips.equals("Capitacion"))
							this.generarInconsistencia(ConstantesBD.ripsAF, "tipo de identificación", aux);
						else
							this.editarInconsistencia(listadoAF,ConstantesBD.ripsAF,i,"tipo de identificación",aux);
						
						this.huboInconsistencias=true;
						presenciaInconsistenciaGeneral[2]=true;
						registroAF+=",";
					}
					else
					{
						aux = institucion.getIdentificacion().trim();
						if(aux.length()>2)
							aux = aux.substring(0,2);
						
						
						if(!aux.equals("CC")&&!aux.equals("CE")&&!aux.equals("PA")&&!aux.equals("NI"))
						{
							if(this.tipoRips.equals("Capitacion"))
								this.generarInconsistencia(ConstantesBD.ripsAF, "tipo de identificación", "campo con datos inválidos");
							else
								this.editarInconsistencia(listadoAF,ConstantesBD.ripsAF,i,"tipo de identificacion","campo con datos inválidos");
							
							this.huboInconsistencias=true;
							registroAF+=",";
						}
						else
						{
							//registroAF+=","+this.editarEspacios(aux,aux.length(),2,false);
							registroAF+=","+aux;
						}
							
						
					}
				}
				else
					registroAF+=",";
				//CAMPO 4************NÚMERO DE IDENTIFICACIÓN***********************************************
				if(!presenciaInconsistenciaGeneral[3])
				{
					if(institucion.getNit().trim().equals(""))
					{
						if(this.tipoRips.equals("Cartera"))
							aux="campo sin información (para todas las facturas)";
						else
							aux="campo sin información";
						
						if(this.tipoRips.equals("Capitacion"))
							this.generarInconsistencia(ConstantesBD.ripsAF, "número de identificación", aux);
						else
							this.editarInconsistencia(listadoAF,ConstantesBD.ripsAF,i,"número de identificación",aux);
						this.huboInconsistencias=true;
						presenciaInconsistenciaGeneral[3]=true;
						registroAF+=",";
					}
					else
					{
						aux=this.revisarCaracteresEspeciales(institucion.getNit());
						
						if(aux.trim().length()>20)
							aux = aux.substring(0,20);
						
						//registroAF+=","+this.editarEspacios(aux.trim(),aux.trim().length(),20,false);
						registroAF+=","+aux.trim();
					}
				}
				else
					registroAF+=",";
				//CAMPO 5************NÚMERO DE LA FACTURA***********************************************
				
					if(this.ripsConFactura)
					{
						if((listadoAF.get("consecutivo_"+i)+"").trim().equals(""))
						{
							if(this.tipoRips.equals("Capitacion"))
								this.generarInconsistencia(ConstantesBD.ripsAF, "número de la factura", "campo sin información");
							else
								this.editarInconsistencia(listadoAF,ConstantesBD.ripsAF,i,"número de la factura","campo sin información");
							
							this.huboInconsistencias=true;
							registroAF+=",";
						}
						else
						{
							aux = (listadoAF.get("consecutivo_"+i)+"").trim();
							if(aux.length()>20)
								aux = aux.substring(0,20);
							
							//registroAF+=","+this.editarEspacios(aux,aux.length(),20,false);
							registroAF+=","+aux;
						}
					}
					else
						registroAF+=",";
				
				//CAMPO 6************FECHA DE EXPEDICIÓN DE LA FACTURA***********************************************
				
					if(this.ripsConFactura)
					{
						if((listadoAF.get("fecha_"+i)+"").trim().equals(""))
						{
							if(this.tipoRips.equals("Capitacion"))
								this.generarInconsistencia(ConstantesBD.ripsAF, "fecha de expedición de la factura", "campo sin información");
							else
								this.editarInconsistencia(listadoAF,ConstantesBD.ripsAF,i,"fecha de expedición de la factura","campo sin información");
							
							this.huboInconsistencias=true;
							registroAF+=",";
						}
						else
						{
							registroAF+=","+UtilidadFecha.conversionFormatoFechaAAp(listadoAF.get("fecha_"+i)+"");
						}
					}
					else
					{
						registroAF+=",";
					}
				
				//CAMPO 7************FECHA DE INICIO***********************************************
				
					if(this.esFactura)
					{
						if(this.numeroEnvio.equals(""))
							registroAF+=","+fechaInicial;
						else
							registroAF+=","+UtilidadFecha.obtenerFechaDiaMesSegunFechaDada(listadoAF.get("fecha_"+i)+"", true);
					}
					else
					{
						if((listadoAF.get("fecha_inicial_"+i)+"").trim().equals(""))
						{
							if(this.tipoRips.equals("Capitacion"))
								this.generarInconsistencia(ConstantesBD.ripsAF, "fecha de inicio", "campo sin información");
							else
								this.editarInconsistencia(listadoAF,ConstantesBD.ripsAF,i,"fecha de inicio","campo sin información");
							
							this.huboInconsistencias=true;
							registroAF+=",";
						}
						else
						{
							aux = UtilidadFecha.conversionFormatoFechaAAp(listadoAF.get("fecha_inicial_"+i)+"");
							if(UtilidadFecha.validarFecha(aux))
								registroAF+=","+aux;
							else
							{
								if(this.tipoRips.equals("Capitacion"))
									this.generarInconsistencia(ConstantesBD.ripsAF, "fecha de inicio", "campo con datos inválidos");
								else
									this.editarInconsistencia(listadoAF,ConstantesBD.ripsAF,i,"fecha de inicio","campo con datos inválidos");
								this.huboInconsistencias=true;
								registroAF+=",";
							}
						}
					}
				
				//CAMPO 8************FECHA FINAL***********************************************
				
					if(this.esFactura)
					{
						if(this.numeroEnvio.equals(""))
							registroAF+=","+fechaFinal;
						else
							registroAF+=","+UtilidadFecha.obtenerFechaDiaMesSegunFechaDada(listadoAF.get("fecha_"+i)+"", false);
					}
					else
					{
						if((listadoAF.get("fecha_final_"+i)+"").trim().equals(""))
						{
							if(this.tipoRips.equals("Capitacion"))
								this.generarInconsistencia(ConstantesBD.ripsAF, "fecha final", "campo sin información");
							else
								this.editarInconsistencia(listadoAF,ConstantesBD.ripsAF,i,"fecha final","campo sin información");
							
							this.huboInconsistencias=true;
							registroAF+=",";
						}
						else
						{
							aux = UtilidadFecha.conversionFormatoFechaAAp(listadoAF.get("fecha_final_"+i)+"");
							if(UtilidadFecha.validarFecha(aux))
								registroAF+=","+aux;
							else
							{
								if(this.tipoRips.equals("Capitacion"))
									this.generarInconsistencia(ConstantesBD.ripsAF, "fecha final", "campo sin información");
								else
									this.editarInconsistencia(listadoAF,ConstantesBD.ripsAF,i,"fecha final","campo sin información");
								
								this.huboInconsistencias=true;
								registroAF+=",";
							}
						}
					}
				
				//CAMPO 9************CÓDIGO ENTIDAD ADMINISTRADORA***********************************************
				if(!presenciaInconsistenciaGeneral[4])
				{
					if(objConvenio.getCodigoMinSalud().trim().equals(""))
					{
						if(this.tipoRips.equals("Cartera"))
							aux="campo sin información (para todas las facturas)";
						else
							aux="campo sin información";
						
						if(this.tipoRips.equals("Capitacion"))
							this.generarInconsistencia(ConstantesBD.ripsAF, "código entidad administradora", aux);
						else
							this.editarInconsistencia(listadoAF,ConstantesBD.ripsAF,i,"código entidad administradora",aux);
						this.huboInconsistencias=true;
						presenciaInconsistenciaGeneral[4]=true;
						registroAF+=",";
					}
					else
					{
						aux=this.revisarCaracteresEspeciales(objConvenio.getCodigoMinSalud());
						
						if(aux.trim().length()>6)
							aux = aux.substring(0,6);
						
						//registroAF+=","+this.editarEspacios(aux.trim(),aux.trim().length(),6,false);
						registroAF+=","+aux.trim();
					}
				}
				else
				{
					aux=this.revisarCaracteresEspeciales(objConvenio.getCodigoMinSalud());
					registroAF+=",";
				}
				//CAMPO 10************NOMBRE ENTIDAD ADMINISTRADORA***********************************************
				if(!presenciaInconsistenciaGeneral[5])
				{
					if(objConvenio.getNombre().trim().equals(""))
					{

						if(this.tipoRips.equals("Cartera"))
							aux="campo sin información (para todas las facturas)";
						else
							aux="campo sin información";
						
						if(this.tipoRips.equals("Capitacion"))
							this.generarInconsistencia(ConstantesBD.ripsAF, "nombre entidad administradora", aux);
						else
							this.editarInconsistencia(listadoAF,ConstantesBD.ripsAF,i,"nombre entidad administradora",aux);
						
						this.huboInconsistencias=true;
						presenciaInconsistenciaGeneral[5]=true;
						registroAF+=",";
					}
					else
					{
						aux=this.revisarCaracteresEspeciales(objConvenio.getNombre());
						
						if(aux.trim().length()>30)
							aux = aux.substring(0,30);
						
						//registroAF+=","+this.editarEspacios(aux.trim(),aux.trim().length(),30,false);
						registroAF+=","+aux.trim();
					}
				}
				else
				{
					aux=this.revisarCaracteresEspeciales(objConvenio.getNombre());
					registroAF+=",";
				}
				//CAMPO 11************NÚMERO DEL CONTRATO***********************************************
				if(!presenciaInconsistenciaGeneral[6])
				{
					if(objConvenio.getTipoRegimen().equals(ConstantesBD.codigoTipoRegimenParticular+""))
					{
						registroAF+=",";
					}
					else
					{
						if((listadoAF.get("contrato_"+i)+"").trim().equals(""))
						{
							
							if(this.ripsConFactura)
							{
								if(this.tipoRips.equals("Cartera"))
									aux="campo sin información (para todas las facturas)";
								else
									aux="campo sin información";
								
								if(this.tipoRips.equals("Capitacion"))
									this.generarInconsistencia(ConstantesBD.ripsAF, "número del contrato", aux);
								else
									this.editarInconsistencia(listadoAF,ConstantesBD.ripsAF,i,"número del contrato",aux);
								
								this.huboInconsistencias=true;
								presenciaInconsistenciaGeneral[6]=true;
								
							}
							registroAF+=",";
						}
						else
						{
							aux=this.revisarCaracteresEspeciales(listadoAF.get("contrato_"+i)+"");
							
							if(aux.trim().length()>15)
								aux = aux.substring(0,15);
							
							//registroAF+=","+this.editarEspacios(aux.trim(),aux.trim().length(),15,false);
							registroAF+=","+aux.trim();
						}
					}
				}
				else
				{
					aux=this.revisarCaracteresEspeciales(listadoAF.get("contrato_"+i)+"");
					registroAF+=",";
				}
				//CAMPO 12************PLAN DE BENEFICIOS***********************************************
				if(!presenciaInconsistenciaGeneral[7])
				{
					if(objConvenio.getTipoRegimen().equals(ConstantesBD.codigoTipoRegimenParticular+""))
					{
						registroAF+=",";
					}
					else
					{
						if(objConvenio.getPlanBeneficios().trim().equals(""))
						{
							if(this.ripsConFactura)
							{
								if(this.tipoRips.equals("Cartera"))
									aux="campo sin información (para todas las facturas)";
								else
									aux="campo sin información";
								
								if(this.tipoRips.equals("Capitacion"))
									this.generarInconsistencia(ConstantesBD.ripsAF, "plan de beneficios", aux);
								else
									this.editarInconsistencia(listadoAF,ConstantesBD.ripsAF,i,"plan de beneficios",aux);
								this.huboInconsistencias=true;
								presenciaInconsistenciaGeneral[7]=true;
							}
							registroAF+=",";
						}
						else
						{
							aux=this.revisarCaracteresEspeciales(objConvenio.getPlanBeneficios());
							
							if(aux.trim().length()>30)
								aux = aux.substring(0,30);
							
							//registroAF+=","+this.editarEspacios(aux.trim(),aux.trim().length(),30,false);
							registroAF+=","+aux.trim();
							
						}
					}
				}
				else
				{
					aux=this.revisarCaracteresEspeciales(objConvenio.getPlanBeneficios());
					registroAF+=",";
				}
				//CAMPO 13 *****************NÚMERO DE LA PÓLIZA**********************************************
				
					
					boolean transito=UtilidadTexto.getBoolean(listadoAF.get("indicativo_transito_"+i)+"");
					if(!transito)
					{
						registroAF+=",";
					}
					else
					{
						/*if((listadoAF.get("numero_poliza_"+i)+"").trim().equals(""))
						{
							if(this.tipoRips.equals("Capitacion"))
								this.generarInconsistencia(ConstantesBD.ripsAF, "número de la póliza", "campo sin información");
							else
								this.editarInconsistencia(listadoAF,ConstantesBD.ripsAF,i,"número de la póliza","campo sin información");
							
							this.huboInconsistencias=true;
							registroAF+=",";
						}
						else*/
						//{
						if(this.getObjConvenio().getCheckInfoAdicCuenta().equals(ConstantesBD.acronimoSi)){
							
							aux=this.revisarCaracteresEspeciales(listadoAF.get("numero_poliza_"+i)+"");
							
							if(aux.trim().length()>10)
								aux = aux.substring(0,10);
							
							//registroAF+=","+this.editarEspacios(aux.trim(),aux.trim().length(),10,false);
							registroAF+=","+aux.trim();
						}else{
							registroAF+=",";
						}
						//}
					}
				
				//CAMPO 14*********VALOR TOTAL DEL PAGO COMPARTIDO***************************
				
					if(ripsConFactura)
					{
						String auxiliar=(listadoAF.get("valor_bruto_"+i)+"").trim();
						auxiliar = UtilidadTexto.formatearValores(auxiliar,"######0.00");
						
						//Se quitan los decimales en ceros
						if(auxiliar.endsWith(".00"))
							auxiliar = auxiliar.replace(".00", "");
						
						if(auxiliar.trim().length()>15)
							auxiliar = auxiliar.substring(0,15);
						
						//registroAF+=","+this.editarEspacios(auxiliar,auxiliar.length(),15,true);
						registroAF+=","+auxiliar;
					}
					else
					{
						registroAF+=",";
					}
				
				//CAMPO 15***********VALOR DE LA COMISIÓN**********************************************
				
					if(ripsConFactura)
					{
						registroAF+=",0";
					}
					else
					{
						registroAF+=",";
					}
				
				//CAMPO 16***********VALOR TOTAL DE DESCUENTOS**********************************************
				
					if(ripsConFactura)
					{
						registroAF+=",0";
					}
					else
					{
						registroAF+=",";
					}
				
				//CAMPO 17*********VALOR NETO A PAGAR POR LA ENTIDAD CONTRATANTE***************************
				
					if(ripsConFactura)
					{
						String auxiliar=(listadoAF.get("valor_convenio_"+i)+"").trim();
						auxiliar = UtilidadTexto.formatearValores(auxiliar,"######0.00");
						
						//Se quitan los decimales en ceros
						if(auxiliar.endsWith(".00"))
							auxiliar = auxiliar.replace(".00", "");
						
						if(auxiliar.trim().length()>15)
							auxiliar = auxiliar.substring(0,15);
						
						//registroAF+=","+this.editarEspacios(auxiliar,auxiliar.length(),15,true);
						registroAF+=","+auxiliar;
					}
					else
					{
						registroAF+=",";
					}
				
				//*********FIN*****************************
				
				
				bufferAF.write(registroAF);
				contadorRegistros++;
				
			}
			
			//se almacena el número de registros creados para el archivo CT
			this.numeroRegistrosCreados.put(ConstantesBD.ripsAF,contadorRegistros+"");
			
			bufferAF.close();
			
			if(contadorRegistros<1)
			{
				//archivo sin registros
				archivoAF.delete();
				return null;
			}
			else
			{	
				//LLENO EL ARRAY LIST PARA GENERAR LA DESCARGA
				//logger.info("Archivo AF ->"+nombreAF);
				archivosGenerados.add(nombreAF);
			}
			
			//EL PATH CAMBIA DEPENDIENDO DEL TIPO DE RIPS QUE SE ESTÉ MANEJANDO
			if(this.tipoRips.equals("Cartera"))
			{
				if(esFactura)
					return "../ripsFactura/ripsFactura.do?estado=detalle&archivo=AF&nitEntidad="+this.nitEntidad;
				else
					return "../ripsCuentaCobro/ripsCuentaCobro.do?estado=detalle&archivo=AF&nitEntidad="+this.nitEntidad;
			}
			else if(this.tipoRips.equals("Capitacion"))
				return "../ripsCapitacion/ripsCapitacion.do?estado=detalle&archivo=AF&nitEntidad="+this.nitEntidad;
			else
				return "../ripsRangos/ripsRangos.do?estado=detalle&archivo=AF&nitEntidad="+this.nitEntidad;
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo AF al generarlo: "+e);
			this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo AF: "+e));
			return null;
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo AF: "+e);
			this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo AF: "+e));
			return null;
		}
	}

	

	

	/**
	 * Método para editar los espacios del campo en el archivo RIPS
	 * @param campo
	 * @param tamano
	 * @param limite
	 * @param esNumerico: si es numérico los espacios se añaden antes, si es cadena los espacios se añaden después
	 * @return
	 */
	private String editarEspacios(String campo, int tamano, int limite,boolean esNumerico)
	{
		
		String aux;
		String espacios="";
		
		for(int i=tamano+1;i<=limite;i++)
			espacios+=" ";
		
		if(esNumerico)
			aux=espacios+campo;
		else
			aux=campo+espacios;
		
		return aux;
	}

	/**
	 * Método para construir el registro de la inconsistencia
	 * @param listado
	 * @param prefijo
	 * @param posicion
	 * @param campo
	 * @param observacion
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	private void editarInconsistencia(HashMap listado, String prefijo, int posicion, String campo, String observacion) 
	{
		//según el tipo de RIPS se generan las inconsistencias respectivas
		if(this.tipoRips.equals("Cartera")||this.tipoRips.equals("Capitacion"))
			this.editarInconsistenciasCartera(listado,prefijo,posicion,campo,observacion);
		else if(this.tipoRips.equals("Consultorios"))
			this.editarInconsistenciasConsultorios(listado,prefijo,posicion,campo,observacion);
		
			
		
	}

	/**
	 * Sub-método para editar las inconsistencias de Consultorios
	 * @param listado
	 * @param prefijo
	 * @param posicion
	 * @param campo
	 * @param observacion
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void editarInconsistenciasConsultorios(HashMap listado, String prefijo, int posicion, String campo, String observacion) {
		//Se revisa el tipo de archivo al cual se le va a generar la inconsistencia***************
		if(prefijo.equals(ConstantesBD.ripsAF)||prefijo.equals(ConstantesBD.ripsAD))
		{
			//INCONSISTENCIAS GENERALES
			if(this.inconsistencias.get("total_general")==null)
			{
				this.inconsistencias.put("total_general","1");
				this.inconsistencias.put("archivo_0",prefijo);
				this.inconsistencias.put("campo_0",campo);
				this.inconsistencias.put("observacion_0",observacion);
			}
			//continuación de la inconsistencia
			else
			{
				int i=Utilidades.convertirAEntero(this.inconsistencias.get("total_general")+"");
				this.inconsistencias.put("archivo_"+i,prefijo);
				this.inconsistencias.put("campo_"+i,campo);
				this.inconsistencias.put("observacion_"+i,observacion);
				i++;
				this.inconsistencias.put("total_general",i+"");
			}
		}
		else
		{
			//INCONSISTENCIAS PARTICULARES
			//se utiliza como llave el tipo y numero de identificacion del paciente
			String llave=listado.get("tipo_identificacion_"+posicion)+"-"+listado.get("numero_identificacion_"+posicion);
			
			//se verifica si ya hay una inconsistencia existente de la factura
			
			if(this.inconsistencias.get(llave)==null)
			{
				
				int total=1;
				int i=0;
				//se verifica el total de registros en la inconsistencia
				if(this.inconsistencias.get("total")==null)
				{
					this.inconsistencias.put("total",total+"");
				}
				else
				{
					total=Utilidades.convertirAEntero(this.inconsistencias.get("total")+"");
					i=total;
					total++;
					this.inconsistencias.put("total",total+"");
				}
				//encabezado de la inconsistencia
				this.inconsistencias.put(llave,i+"");
				this.inconsistencias.put("factura_"+i,this.getNumeroFactura());
				this.inconsistencias.put("tipo_num_"+i,listado.get("tipo_identificacion_"+posicion)+". "+listado.get("numero_identificacion_"+posicion));
				this.inconsistencias.put("paciente_"+i,listado.get("paciente_"+posicion));
				this.inconsistencias.put("via_ingreso_"+i,listado.get("via_ingreso_"+posicion));
				this.inconsistencias.put("elementos_"+i,"1");
				//detalle de la inconsistencia
				this.inconsistencias.put("archivo_"+i+"_0",prefijo);
				this.inconsistencias.put("campo_"+i+"_0",campo);
				if(!(listado.get("orden_"+posicion)+"").equals("")&&!prefijo.equals(ConstantesBD.ripsUS))
					this.inconsistencias.put("orden_"+i+"_0",listado.get("orden_"+posicion));
				else
					this.inconsistencias.put("orden_"+i+"_0","               ");
				this.inconsistencias.put("observacion_"+i+"_0",observacion);
				if(!prefijo.equals(ConstantesBD.ripsUS))
					this.inconsistencias.put("cuenta_"+i+"_0",listado.get("cuenta_"+posicion));
				else
					this.inconsistencias.put("cuenta_"+i+"_0","         ");
				
			}
			else
			{
				int i=Utilidades.convertirAEntero(this.inconsistencias.get(llave)+"");
				//si entra aquí quiere decir que EL PACIENTE ya tiene inconsistencias
				//no es necesario añadir el encabezado, sólo el detalle
				int nElementos=Utilidades.convertirAEntero(this.inconsistencias.get("elementos_"+i)+"");
				nElementos++;
				this.inconsistencias.put("elementos_"+i,nElementos+"");
				//detalle de la inconsistencia
				this.inconsistencias.put("archivo_"+i+"_"+(nElementos-1),prefijo);
				this.inconsistencias.put("campo_"+i+"_"+(nElementos-1),campo);
				if(!(listado.get("orden_"+posicion)+"").equals("")&&!prefijo.equals(ConstantesBD.ripsUS))
					this.inconsistencias.put("orden_"+i+"_"+(nElementos-1),listado.get("orden_"+posicion));
				else
					this.inconsistencias.put("orden_"+i+"_"+(nElementos-1),"               ");
				this.inconsistencias.put("observacion_"+i+"_"+(nElementos-1),observacion);
				if(!prefijo.equals(ConstantesBD.ripsUS))
					this.inconsistencias.put("cuenta_"+i+"_"+(nElementos-1),listado.get("cuenta_"+posicion));
				else
					this.inconsistencias.put("cuenta_"+i+"_"+(nElementos-1),"         ");
			}

		}
		
	}

	/**
	 * Sub-método para editar las inconsistencias de Cartera
	 * @param listado
	 * @param prefijo
	 * @param posicion
	 * @param campo
	 * @param observacion
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void editarInconsistenciasCartera(HashMap listado, String prefijo, int posicion, String campo, String observacion) {
		String consecutivo="";
		//RIPS CARTERA
		if(this.tipoRips.equals("Cartera"))
			consecutivo = listado.get("consecutivo_"+posicion)+"";
		//RIPS CAPITACION
		else
			consecutivo = listado.get("consecutivo_factura_"+posicion)+"";
		
		//se verifica si ya hay una inconsistencia existente de la factura
		
		if(this.inconsistencias.get(consecutivo)==null)
		{
			
			int total=1;
			//se verifica el total de registros en la inconsistencia
			if(this.inconsistencias.get("total")==null)
			{
				this.inconsistencias.put("total",total+"");
			}
			else
			{
				total=Utilidades.convertirAEntero(this.inconsistencias.get("total")+"");
				total++;
				this.inconsistencias.put("total",total+"");
			}
			//encabezado de la inconsistencia
			this.inconsistencias.put("pos_"+total+"",consecutivo);
			this.inconsistencias.put(consecutivo,"true");
			this.inconsistencias.put("tipo_identificacion_"+consecutivo,listado.get("tipo_identificacion_"+posicion));
			this.inconsistencias.put("numero_identificacion_"+consecutivo,listado.get("numero_identificacion_"+posicion));
			this.inconsistencias.put("paciente_"+consecutivo,listado.get("paciente_"+posicion));
			this.inconsistencias.put("cuenta_"+consecutivo,listado.get("cuenta_"+posicion));
			this.inconsistencias.put("via_ingreso_"+consecutivo,listado.get("via_ingreso_"+posicion));
			this.inconsistencias.put("elementos_"+consecutivo,"1");
			//detalle de la inconsistencia
			this.inconsistencias.put("archivo_"+consecutivo+"_0",prefijo);
			this.inconsistencias.put("campo_"+consecutivo+"_0",campo);
			
			if(prefijo.equals(ConstantesBD.ripsAC)||
				prefijo.equals(ConstantesBD.ripsAP)||
				prefijo.equals(ConstantesBD.ripsAD)||
				prefijo.equals(ConstantesBD.ripsAM)||
				prefijo.equals(ConstantesBD.ripsAT)||
				prefijo.equals(ConstantesBD.ripsAN))
			{
				this.inconsistencias.put("orden_"+consecutivo+"_0",listado.get("orden_"+posicion).toString().equals(ConstantesBD.codigoNuncaValido+"")?"               ":listado.get("orden_"+posicion).toString());
				this.inconsistencias.put("serv_art_"+consecutivo+"_0",listado.get("serv_art_"+posicion).toString().equals(ConstantesBD.codigoNuncaValido+"")?"          ":listado.get("serv_art_"+posicion).toString());
			}
			else
			{
				this.inconsistencias.put("orden_"+consecutivo+"_0","               ");
				this.inconsistencias.put("serv_art_"+consecutivo+"_0","          ");
			}
			
			this.inconsistencias.put("observacion_"+consecutivo+"_0",observacion);
			
		}
		else
		{
			//si entra aquí quiere decir que la factura ya tiene inconsistencias
			//no es necesario añadir el encabezado, sólo el detalle
			int nElementos=Utilidades.convertirAEntero(this.inconsistencias.get("elementos_"+consecutivo)+"");
			nElementos++;
			this.inconsistencias.put("elementos_"+consecutivo,nElementos+"");
			//detalle de la inconsistencia
			this.inconsistencias.put("archivo_"+consecutivo+"_"+(nElementos-1),prefijo);
			this.inconsistencias.put("campo_"+consecutivo+"_"+(nElementos-1),campo);
			if(prefijo.equals(ConstantesBD.ripsAC)||
				prefijo.equals(ConstantesBD.ripsAD)||
				prefijo.equals(ConstantesBD.ripsAP)||
				prefijo.equals(ConstantesBD.ripsAM)||
				prefijo.equals(ConstantesBD.ripsAT)||
				prefijo.equals(ConstantesBD.ripsAN))
			{
				this.inconsistencias.put("orden_"+consecutivo+"_"+(nElementos-1),listado.get("orden_"+posicion).toString().equals(ConstantesBD.codigoNuncaValido+"")?"               ":listado.get("orden_"+posicion).toString());
				this.inconsistencias.put("serv_art_"+consecutivo+"_"+(nElementos-1),listado.get("serv_art_"+posicion).toString().equals(ConstantesBD.codigoNuncaValido+"")?"          ":listado.get("serv_art_"+posicion).toString());
			}
			else
			{
				this.inconsistencias.put("orden_"+consecutivo+"_"+(nElementos-1),"               ");
				this.inconsistencias.put("serv_art_"+consecutivo+"_"+(nElementos-1),"          ");
			}
			
			this.inconsistencias.put("observacion_"+consecutivo+"_"+(nElementos-1),observacion);
		}
		
	}

	/**
	 * Metodo para generar el archivo AC
	 * @param con
	 * @return ruta para ingresar al resumen del archivo
	 * si retorna nulo es que hubo error en la edición del fichero
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private String generarAC(Connection con) {
		
		HashMap listadoAC;
		String registroAC="";
		int numeroRegistros; //número de registros cosnultado desde la BD
		int contadorRegistros=0; //cuenta del número de registros insertados en el fichero
		int contadorFactura=0; //contador de registros por factura
		boolean presenciaInconsistenciaGeneral=false;//variable boolean para el manejo de la inconsistencia general
		String numeroFacturaAnterior=""; //permite llevar el control de los registros de una solo factura
		
		String aux=""; //variable auxiliar para revisar los caracteres especiales de los campos de texto
		
		//ejecucion de la consulta de datos
		//RIPS CARTERA ******************************
		if(this.tipoRips.equals("Cartera"))
		{
			if(this.esFactura)
				listadoAC=this.consultaFacturaAC(con,this.fechaInicial,this.fechaFinal,this.convenio,institucion.getCodigo());
			else
				listadoAC=this.consultaCuentaCobroAC(con,this.numeroCuentaCobro,institucion.getCodigo());
		}
		//RIPS CAPITACION ******************************
		else if(this.tipoRips.equals("Capitacion"))
			listadoAC = this.consultaCapitacionAC(con, institucion.getCodigo());
		else
			listadoAC=this.consultaConsultoriosAC(con,this.getNumeroFactura(),this.getNumeroRemision());
		
		numeroRegistros=Utilidades.convertirAEntero(listadoAC.get("numRegistros")+"");
		try
		{
			//apertura de archivo AC
			aux=this.revisarCaracteresEspeciales(this.numeroRemision);
			File archivoAC=new File(dirConvenio.getAbsolutePath(),ConstantesBD.ripsAC+aux+".txt");
			FileWriter streamAC=new FileWriter(archivoAC,false); //se coloca false para el caso de que esté repetido
			BufferedWriter bufferAC=new BufferedWriter(streamAC);
			
			String nombreAC = ConstantesBD.ripsAC+aux+".txt";
			
			for(int i=0;i<numeroRegistros;i++)
			{
				//si no hubo inconsistencias entonces se hace una inserción del registro en el archivo
				//se verifica si el registro ha cambiado de factura
				//en el caso de que ya haya cambiado de factura ya se pueden insertar 
				//todos los registros relacionados con la factura 
				if(numeroFacturaAnterior.equals(""))
					numeroFacturaAnterior=listadoAC.get("consecutivo_"+i)+"";
				else if(!numeroFacturaAnterior.equals(listadoAC.get("consecutivo_"+i)+""))
				{
					bufferAC.write(registroAC);
					contadorRegistros+=contadorFactura;
					contadorFactura=0;
					numeroFacturaAnterior=listadoAC.get("consecutivo_"+i)+"";
					registroAC="";
				}
				
				
				//validación del registro de consulta (que tenga la naturaleza de servicio apropiada)
				String naturalezaS=listadoAC.get("naturaleza_servicio_"+i)+"";
				if(naturalezaS.equals(ConstantesBD.codigoNaturalezaServicioConsultas)||
					naturalezaS.equals(ConstantesBD.codigoNaturalezaServicioPromocionPrevencion))
				{
					//SEGÚN TIPO DE RIPS SE TOMA LA DECISIÓN ESPECÍFICA
					if(contadorRegistros>0||contadorFactura>0)
					{
						registroAC+="\r\n";
					}
					
				//edicion del registro
				//CAMPO 1************NÚMERO DE LA FACTURA********************
				
					if(this.ripsConFactura)
					{
						if((listadoAC.get("consecutivo_"+i)+"").trim().equals(""))
						{
							this.editarInconsistencia(listadoAC,
									ConstantesBD.ripsAC,i,
									"número de la factura",
									"campo sin información");
							this.huboInconsistencias=true;
							//registroAC+="                    ";
						}
						else
						{
							aux = (listadoAC.get("consecutivo_"+i)+"").trim(); 
							if(aux.length()>20)
								aux = aux.substring(0,20); 
							
							//registroAC+=this.editarEspacios(aux,aux.length(),20,false);
							registroAC+=aux;
							
						}
					}
					/**else
					{
						registroAC+="                    ";
					}**/
				
				//CAMPO 2*********CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD**************
				if(!presenciaInconsistenciaGeneral)
				{
					if(institucion.getCodMinSalud().trim().equals(""))
					{
						if(this.tipoRips.equals("Cartera"))
							aux="campo sin información (para todas las facturas)";
						else
							aux="campo sin información";
						
						this.editarInconsistencia(listadoAC,
								ConstantesBD.ripsAC,i,
								"código del prestador de servicios de salud",
								aux);
						this.huboInconsistencias=true;
						presenciaInconsistenciaGeneral=true;
						registroAC+=",";
					}
					else
					{
						aux = this.revisarCaracteresEspeciales(institucion.getCodMinSalud());
						if(aux.trim().length()>12)
							aux = aux.substring(0,12);
						
						aux=this.revisarCaracteresEspeciales(aux);
						//registroAC+=","+this.editarEspacios(aux.trim(),aux.trim().length(),12,false);
						registroAC+=","+aux.trim();
						
					}
				}
				else
					registroAC+=",";
				//CAMPO 3*******TIPO DE IDENTIFICACIÓN DEL USUARIO***********************
					
					if((listadoAC.get("tipo_identificacion_"+i)+"").trim().equals(""))
					{
						this.editarInconsistencia(listadoAC,
								ConstantesBD.ripsAC,i,
								"tipo identificacion usuario",
								"campo sin información");
						this.huboInconsistencias=true;
						registroAC+=",";
					}
					else
					{
						String tipoId=(listadoAC.get("tipo_identificacion_"+i)+"").trim();
						if(!tipoId.equals("CC")&&!tipoId.equals("CE")&&!tipoId.equals("PA")&&!tipoId.equals("RC")&&
							!tipoId.equals("TI")&&!tipoId.equals("AS")&&!tipoId.equals("MS")&&!tipoId.equals("NU"))
						{
							
							this.editarInconsistencia(listadoAC,
									ConstantesBD.ripsAC,i,
									"tipo identificacion usuario",
									"campo con datos inválidos");
							this.huboInconsistencias=true;
							registroAC+=",";
						}
						else
						{
							registroAC+=","+tipoId;
						}
						
					}
				
				//CAMPO 4 **********NÚMERO DE IDENTIFICACIÓN DE USUARIO************
				
					if((listadoAC.get("numero_identificacion_"+i)+"").trim().equals(""))
					{
						this.editarInconsistencia(listadoAC,
								ConstantesBD.ripsAC,i,
								"número identificacion usuario",
								"campo sin información");
						this.huboInconsistencias=true;
						registroAC+=",";
					}
					else
					{
						aux=this.revisarCaracteresEspeciales(listadoAC.get("numero_identificacion_"+i)+"");
						if(aux.trim().length()>20)
							aux = aux.substring(0,20);
						
						//registroAC+=","+this.editarEspacios(aux.trim(),aux.trim().length(),20,false);
						registroAC+=","+aux.trim();
					}
				
				//CAMPO 5 *******FECHA DE LA CONSULTA*****************************
				
					if((listadoAC.get("fecha_consulta_"+i)+"").equals(""))
					{
						this.editarInconsistencia(listadoAC,
								ConstantesBD.ripsAC,i,
								"fecha de la consulta",
								"campo sin información");
						this.huboInconsistencias=true;
						registroAC+=",";
					}
					else
					{
						registroAC+=","+UtilidadFecha.conversionFormatoFechaAAp(listadoAC.get("fecha_consulta_"+i)+"");
					}
				
				//CAMPO 6*****NÚMERO DE AUTORIZACION****************************
					aux=this.revisarCaracteresEspeciales(listadoAC.get("numero_autorizacion_"+i)+"");
					
					if(aux.trim().length()>15)
						aux = aux.substring(0,15);
					
					//registroAC+=","+this.editarEspacios(aux.trim(),aux.trim().length(),15,false);
					registroAC+=","+aux.trim();
				
				//CAMPO 7*****CÓDIGO DE LA CONSULTA*****************************
				
					if((listadoAC.get("codigo_consulta_"+i)+"").trim().equals(""))
					{
						this.editarInconsistencia(listadoAC,
								ConstantesBD.ripsAC,i,
								"codigo de la consulta",
								"campo sin información");
						this.huboInconsistencias=true;
						registroAC+=",";
					}
					else
					{
						aux=this.revisarCaracteresEspeciales(listadoAC.get("codigo_consulta_"+i)+"");
						if(aux.trim().length()>8)
							aux = aux.substring(0,8);
						
						//registroAC+=","+this.editarEspacios(aux.trim(),aux.trim().length(),8,false);
						registroAC+=","+aux.trim();
					}
				
				//CAMPO 8***************FINALIDAD DE LA CONSULTA********************
				
					if((listadoAC.get("finalidad_consulta_"+i)+"").trim().equals(""))
					{
						this.editarInconsistencia(listadoAC,
								ConstantesBD.ripsAC,i,
								"finalidad de la consulta",
								"campo sin información");
						this.huboInconsistencias=true;
						registroAC+=",";
					}
					else
					{
						String finalidadC=(listadoAC.get("finalidad_consulta_"+i)+"").trim();
						finalidadC = this.editarCeros(finalidadC,finalidadC.length(),2);
						if(finalidadC.equals("01")||finalidadC.equals("02")||finalidadC.equals("03")||finalidadC.equals("04")||
						   finalidadC.equals("05")||finalidadC.equals("06")||finalidadC.equals("07")||finalidadC.equals("08")||
						   finalidadC.equals("09")||finalidadC.equals("10"))
						{
							registroAC+=","+finalidadC;
						}
						else
						{
							this.editarInconsistencia(listadoAC,
									ConstantesBD.ripsAC,i,
									"finalidad de la consulta",
									"campo con datos inválidos");
							this.huboInconsistencias=true;
							registroAC+=",";
						}
						
					}
				
				//CAMPO 9********CAUSA EXTERNA***************************************
				try
				{
					aux = (listadoAC.get("causa_externa_"+i)+"");
					Utilidades.convertirAEntero(aux);
				}
				catch(Exception e)
				{
					aux = "";
				}
				if(aux.trim().equals(""))
				{
					this.editarInconsistencia(listadoAC,
							ConstantesBD.ripsAC,i,
							"causa externa",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAC+=",";
				}
				else
				{
					int causaExterna=Utilidades.convertirAEntero(aux);
					if(causaExterna>=1&&causaExterna<=15)
					{
						registroAC+=","+this.editarCeros((listadoAC.get("causa_externa_"+i)+"").trim(),
								(listadoAC.get("causa_externa_"+i)+"").trim().length(),2);
					}
					else
					{
						this.editarInconsistencia(listadoAC,
								ConstantesBD.ripsAC,i,
								"causa externa",
								"campo con datos inválidos");
						this.huboInconsistencias=true;
						registroAC+=",";
					}
					
				}
				
				//CAMPO 10********CÓDIGO DEL DIAGNÓSTICO PRINCIPAL**************************
				
					
					if((listadoAC.get("cod_diag_ppal_"+i)+"").trim().equals(""))
					{
						this.editarInconsistencia(listadoAC,
								ConstantesBD.ripsAC,i,
								"codigo diagnóstico principal",
								"campo sin información");
						this.huboInconsistencias=true;
						registroAC+=",";
					}
					else
					{
						String diagnostico=this.revisarCaracteresEspeciales(listadoAC.get("cod_diag_ppal_"+i)+"");
						//registroAC+=","+this.editarEspacios(diagnostico,diagnostico.length(),4,false);
						registroAC+=","+diagnostico;
						
					}
				
				//CAMPO 11 ***CÓDIGO DEL DIAGNÓSTICO RELACIONADO 1***********
				
					if(!(listadoAC.get("cod_diag_rel1_"+i)+"").equals(""))
					{
						String diagnostico=this.revisarCaracteresEspeciales(listadoAC.get("cod_diag_rel1_"+i)+"");
						registroAC+=","+diagnostico;
					}
					else
					{
						registroAC+=",";
					}
				
				//CAMPO 12 ***CÓDIGO DEL DIAGNÓSTICO RELACIONADO 2***********
				
					if(!(listadoAC.get("cod_diag_rel2_"+i)+"").equals(""))
					{
						String diagnostico=this.revisarCaracteresEspeciales(listadoAC.get("cod_diag_rel2_"+i)+"");
						registroAC+=","+diagnostico;
					}
					else
					{
						registroAC+=",";
					}
				
				//CAMPO 13 ***CÓDIGO DEL DIAGNÓSTICO RELACIONADO 3************
				
					if(!(listadoAC.get("cod_diag_rel3_"+i)+"").equals(""))
					{
						String diagnostico=this.revisarCaracteresEspeciales(listadoAC.get("cod_diag_rel3_"+i)+"");
						registroAC+=","+diagnostico;
					}
					else
					{
						registroAC+=",";
					}
				
				//CAMPO 14 ***TIPO DE DIAGNÓSTICO PRINCIPAL************************
				
					if((listadoAC.get("tipo_diag_ppal_"+i)+"").trim().equals(""))
					{
						this.editarInconsistencia(listadoAC,
								ConstantesBD.ripsAC,i,
								"tipo diagnóstico principal",
								"campo sin información");
						this.huboInconsistencias=true;
						registroAC+=",";
					}
					else
					{
						int tipoDiagPpal=Utilidades.convertirAEntero(listadoAC.get("tipo_diag_ppal_"+i)+"");
						//se revisa que el tipo de diagnóstico sea 1, 2 o 3
						if(tipoDiagPpal>=1&&tipoDiagPpal<=3)
							registroAC+=","+listadoAC.get("tipo_diag_ppal_"+i);
						else
						{
							this.editarInconsistencia(listadoAC,
									ConstantesBD.ripsAC,i,
									"tipo diagnóstico principal",
									"campo con datos inválidos");
							this.huboInconsistencias=true;
							registroAC+=",";
						}
					}
				
				//CAMPO 15 ***VALOR DE LA CONSULTA***************************
				
					if(this.ripsConFactura)
					{
						if((listadoAC.get("valor_"+i)+"").trim().equals(""))
						{
							this.editarInconsistencia(listadoAC,
									ConstantesBD.ripsAC,i,
									"valor de la consulta",
									"campo sin información");
							this.huboInconsistencias=true;
							registroAC+=",";
						}
						else
						{
							String auxiliar=(listadoAC.get("valor_"+i)+"").trim();
							auxiliar = UtilidadTexto.formatearValores(auxiliar,"######0.00");
							//Se quitan los decimales en ceros
							if(auxiliar.endsWith(".00"))
								auxiliar = auxiliar.replace(".00", "");
							
							if(auxiliar.trim().length()>15)
								auxiliar = auxiliar.substring(0,15);
							
							registroAC+=","+auxiliar;
						}
					}
					else
						registroAC+=",";
				
				//CAMPO 16***********VALOR DE LA CUOTA MODERADORA**********************************************
				
					if(ripsConFactura)
					{
						//RIPS CARTERA O CAPITACION********************
						if(this.tipoRips.equals("Cartera")||this.tipoRips.equals("Capitacion"))
						{
							registroAC+=",0";
						}
						//RIPS CONSULTORIOS
						else
						{
							if((listadoAC.get("valor_copago_"+i)+"").trim().equals(""))
							{
								this.editarInconsistencia(listadoAC,
										ConstantesBD.ripsAC,i,
										"valor de la cuota moderadora",
										"campo sin información");
								this.huboInconsistencias=true;
								registroAC+=",";
							}
							else
							{
								String auxiliar=(listadoAC.get("valor_copago_"+i)+"").trim();
								auxiliar = UtilidadTexto.formatearValores(auxiliar,"######0.00");
								if(auxiliar.trim().length()>15)
									auxiliar = auxiliar.substring(0,15);
								
								//registroAC+=","+this.editarEspacios(auxiliar,auxiliar.length(),15,true);
								registroAC+=","+auxiliar;
							}
						}
						
					}
					else
						registroAC+=",";
				
				//CAMPO 17 ***VALOR NETO A PAGAR***************************
				
					if(this.ripsConFactura)
					{
						//RIPS CARTERA O CAPITACION *****************************************
						if(this.tipoRips.equals("Cartera")||this.tipoRips.equals("Capitacion"))
						{
							if((listadoAC.get("valor_"+i)+"").trim().equals(""))
							{
								this.editarInconsistencia(listadoAC,
										ConstantesBD.ripsAC,i,
										"valor neto a pagar",
										"campo sin información");
								this.huboInconsistencias=true;
								registroAC+=",";
							}
							else
							{
								String auxiliar=(listadoAC.get("valor_"+i)+"").trim();
								auxiliar = UtilidadTexto.formatearValores(auxiliar,"######0.00");
								
								//Se quitan los decimales en ceros
								if(auxiliar.endsWith(".00"))
									auxiliar = auxiliar.replace(".00", "");
								
								if(auxiliar.trim().length()>15)
									auxiliar = auxiliar.substring(0,15);
								
								//registroAC+=","+this.editarEspacios(auxiliar,auxiliar.length(),15,true);
								registroAC+=","+auxiliar;
								
							}
						}
						//RIPS CONSULTORIOS
						else
						{
							if((listadoAC.get("valor_empresa_"+i)+"").trim().equals(""))
							{
								this.editarInconsistencia(listadoAC,
										ConstantesBD.ripsAC,i,
										"valor neto a pagar",
										"campo sin información");
								this.huboInconsistencias=true;
								registroAC+=",";
							}
							else
							{
								String auxiliar=(listadoAC.get("valor_empresa_"+i)+"").trim();
								auxiliar = UtilidadTexto.formatearValores(auxiliar,"######0.00");
								
								//Se quitan los decimales en ceros
								if(auxiliar.endsWith(".00"))
									auxiliar = auxiliar.replace(".00", "");
								
								if(auxiliar.trim().length()>15)
									auxiliar = auxiliar.substring(0,15);
																	
								//registroAC+=","+this.editarEspacios(auxiliar,auxiliar.length(),15,true);
								registroAC+=","+auxiliar;
							}
						}
					}
					else
					{
						registroAC+=",";
					}
				
					//*************FIN************************************************
					
					contadorFactura++;
					
				}
				
			}//FIN FOR
			bufferAC.write(registroAC);
			contadorRegistros+=contadorFactura;
			
			
			//se almacena el número de registros creados para el archivo CT
			this.numeroRegistrosCreados.put(ConstantesBD.ripsAC,contadorRegistros+"");
			
			bufferAC.close();
			
			if(contadorRegistros<1)
			{
				//archivo sin registros
				archivoAC.delete();
				return null;
			}
			else
			{	
				//LLENO EL ARRAY LIST PARA GENERAR LA DESCARGA
				//logger.info("Archivo AC ->"+nombreAC);
				archivosGenerados.add(nombreAC);
			}
			
			//SEGÚN TIPO DE RIPS SE RETORNA EL PATH ESPECÍFICO
			if(this.tipoRips.equals("Cartera"))
			{
				if(esFactura)
					return "../ripsFactura/ripsFactura.do?estado=detalle&archivo=AC&nitEntidad="+this.nitEntidad;
				else
					return "../ripsCuentaCobro/ripsCuentaCobro.do?estado=detalle&archivo=AC&nitEntidad="+this.nitEntidad;
			}
			else if(this.tipoRips.equals("Capitacion"))
				return "../ripsCapitacion/ripsCapitacion.do?estado=detalle&archivo=AC&nitEntidad="+this.nitEntidad;
			else
				return "../ripsRangos/ripsRangos.do?estado=detalle&archivo=AC&nitEntidad="+this.nitEntidad;
			
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo AC al generarlo: "+e);
			this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo AC: "+e));
			return null;
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo AC: "+e);
			this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo AC: "+e));
			return null;
		}
	}

	
	
	/**
	 * Método usado para quitar los caracteres especiales 
	 * en los campos de texto
	 * @param cadena
	 * @return
	 */
	private String revisarCaracteresEspeciales(String cadena)
	{
		String nuevaCadena="";
		for(int i=0;i<cadena.length();i++)
		{
			if(cadena.charAt(i)>=48&&cadena.charAt(i)<=57||
					cadena.charAt(i)>=65&&cadena.charAt(i)<=90||
					cadena.charAt(i)>=97&&cadena.charAt(i)<=122||
					cadena.charAt(i)==32||cadena.charAt(i)=='á'||
					cadena.charAt(i)=='é'||cadena.charAt(i)=='í'||
					cadena.charAt(i)=='ó'||cadena.charAt(i)=='ú'||
					cadena.charAt(i)=='Á'||
					cadena.charAt(i)=='É'||cadena.charAt(i)=='Í'||
					cadena.charAt(i)=='Ó'||cadena.charAt(i)=='Ú'||
					cadena.charAt(i)=='ñ'||cadena.charAt(i)=='Ñ'|| 
					cadena.charAt(i)=='.') 
			{
				nuevaCadena+=cadena.charAt(i);
			}
		}
		
		return nuevaCadena;
	}
	//********MÉTODOS NO RELACIONADOS CON LA GENERACIÓN DE ARCHIVOS******************
	/**
	 * Método que obtiene la informacion del archivo AF para los
	 * RIPS de Capitacion 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private HashMap consultaCapitacionAF() 
	{
		HashMap mapa = new HashMap();
		
		mapa.put("numRegistros", this.numerosContrato.size());
		for(int w=0; w<this.numerosContrato.size(); w++)
		{
			mapa.put("consecutivo_"+w, UtilidadTexto.formatearValores(this.numeroCuentaCobro, "0"));
			mapa.put("fecha_"+w, this.fechaFactura);
			mapa.put("fecha_inicial_"+w,this.fechaInicial);
			mapa.put("fecha_final_"+w,this.fechaFinal);
			mapa.put("indicativo_transito_"+w,"false");
			mapa.put("contrato_"+w, this.getNumerosContrato().get(w));
			mapa.put("valor_bruto_"+w,"0.00");
			mapa.put("valor_convenio_"+w,this.valorCuenta);
		}
		
		return mapa;
	}
	
	/**
	 * Método para consultar los RIPS del archivo AD en Capitacion
	 * @param con
	 * @param campos
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private HashMap consultaCapitacionAD(Connection con,int institucion)
	{
		HashMap campos = new HashMap();
		campos.put("numeroCuentaCobro",UtilidadTexto.formatearValores(this.numeroCuentaCobro, "0")+"");
		campos.put("codigoConvenio", this.convenio+"");
		campos.put("fechaInicial",this.fechaInicial);
		campos.put("fechaFinal", this.fechaFinal);
		campos.put("institucion",institucion+"");
		campos.put("codigosContrato",this.codigosContrato);
		return ripsDao.consultaCapitacionAD(con, campos);
	}
	
	/**
	 * Método que consulta los RIPS del archivo US Capitacion
	 * @param con
	 * @param institucino
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private HashMap consultaCapitacionUS(Connection con,int institucion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoConvenio", this.convenio+"");
		campos.put("fechaInicial",this.fechaInicial);
		campos.put("fechaFinal", this.fechaFinal);
		campos.put("institucion",institucion+"");
		campos.put("codigosContrato",this.codigosContrato);
		return ripsDao.consultaCapitacionUS(con, campos);
	}
	
	/**
	 * Método que consulta los RIPS del archivo AC Capitacion
	 * @param con
	 * @param institucion
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private HashMap consultaCapitacionAC(Connection con,int institucion)
	{
		HashMap campos = new HashMap();
		campos.put("tarifarioOficial",this.tipoCodigo+"");
		campos.put("numeroCuentaCobro",UtilidadTexto.formatearValores(this.numeroCuentaCobro, "0")+"");
		campos.put("codigoConvenio", this.convenio+"");
		campos.put("fechaInicial",this.fechaInicial);
		campos.put("fechaFinal", this.fechaFinal);
		campos.put("institucion",institucion+"");
		campos.put("codigosContrato",this.codigosContrato);
		return ripsDao.consultaCapitacionAC(con, campos);
	}
	
	/**
	 * Método que consulta los rips AP Capitacion
	 * @param con
	 * @param institucion
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private HashMap consultaCapitacionAP(Connection con,int institucion)
	{
		HashMap campos = new HashMap();
		campos.put("tarifarioOficial",this.tipoCodigo+"");
		campos.put("numeroCuentaCobro",UtilidadTexto.formatearValores(this.numeroCuentaCobro, "0")+"");
		campos.put("codigoConvenio", this.convenio+"");
		campos.put("fechaInicial",this.fechaInicial);
		campos.put("fechaFinal", this.fechaFinal);
		campos.put("institucion",institucion+"");
		campos.put("codigosContrato",this.codigosContrato);
		return ripsDao.consultaCapitacionAP(con, campos);
	}
	
	/**
	 * Método que consulta los rips del archivo AM Capitacion
	 * @param con
	 * @param institucion
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private HashMap consultaCapitacionAM(Connection con,int institucion)
	{
		HashMap campos = new HashMap();
		campos.put("numeroCuentaCobro",UtilidadTexto.formatearValores(this.numeroCuentaCobro, "0")+"");
		campos.put("codigoConvenio", this.convenio+"");
		campos.put("fechaInicial",this.fechaInicial);
		campos.put("fechaFinal", this.fechaFinal);
		campos.put("institucion",institucion+"");
		campos.put("codigosContrato",this.codigosContrato);
		return ripsDao.consultaCapitacionAM(con, campos);
	}
	
	/**
	 * Método que consulta los rips AT Capitacion
	 * @param con
	 * @param institucion
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private HashMap consultaCapitacionAT(Connection con,int institucion)
	{
		HashMap campos = new HashMap();
		campos.put("tarifarioOficial",this.tipoCodigo+"");
		campos.put("numeroCuentaCobro",UtilidadTexto.formatearValores(this.numeroCuentaCobro, "0")+"");
		campos.put("codigoConvenio", this.convenio+"");
		campos.put("fechaInicial",this.fechaInicial);
		campos.put("fechaFinal", this.fechaFinal);
		campos.put("institucion",institucion+"");
		campos.put("codigosContrato",this.codigosContrato);
		return ripsDao.consultaCapitacionAT(con, campos);
	}
	
	/**
	 * Método que consulta los rips de AU Capitacion
	 * @param con
	 * @param institucion
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private HashMap consultaCapitacionAU(Connection con,int institucion)
	{
		HashMap campos = new HashMap();
		campos.put("numeroCuentaCobro",UtilidadTexto.formatearValores(this.numeroCuentaCobro, "0")+"");
		campos.put("codigoConvenio", this.convenio+"");
		campos.put("fechaInicial",this.fechaInicial);
		campos.put("fechaFinal", this.fechaFinal);
		campos.put("institucion",institucion+"");
		campos.put("codigosContrato",this.codigosContrato);
		return ripsDao.consultaCapitacionAU(con, campos);
	}
	
	/**
	 * Método implementado para consultar los rips AH Capitacion
	 * @param con
	 * @param institucion
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private HashMap consultaCapitacionAH(Connection con,int institucion)
	{
		HashMap campos = new HashMap();
		campos.put("numeroCuentaCobro",UtilidadTexto.formatearValores(this.numeroCuentaCobro, "0")+"");
		campos.put("codigoConvenio", this.convenio+"");
		campos.put("fechaInicial",this.fechaInicial);
		campos.put("fechaFinal", this.fechaFinal);
		campos.put("institucion",institucion+"");
		campos.put("codigosContrato",this.codigosContrato);
		return ripsDao.consultaCapitacionAH(con, campos);
	}
	
	/**
	 * Método que consulta los rips del archivo AN
	 * @param con
	 * @param institucion
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private HashMap consultaCapitacionAN(Connection con,int institucion)
	{
		HashMap campos = new HashMap();
		campos.put("numeroCuentaCobro",UtilidadTexto.formatearValores(this.numeroCuentaCobro, "0")+"");
		campos.put("codigoConvenio", this.convenio+"");
		campos.put("fechaInicial",this.fechaInicial);
		campos.put("fechaFinal", this.fechaFinal);
		campos.put("institucion",institucion+"");
		campos.put("codigosContrato",this.codigosContrato);
		return ripsDao.consultaCapitacionAN(con, campos);
	}
	
	/**
	 * Método para consultar los registros del archivo AP
	 * del RIPS consultorios
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaConsultoriosAP(Connection con,String numeroFactura,String numeroRemision)
	{
		return ripsDao.consultaConsultoriosAP(con,numeroFactura,numeroRemision);
	}
	/**
	 * Método para consultar los registros del archivo AC
	 * del RIPS consultorios
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaConsultoriosAC(Connection con,String numeroFactura,String numeroRemision)
	{
		return ripsDao.consultaConsultoriosAC(con,numeroFactura,numeroRemision);
	}
	/**
	 * Método para consultar los registros del archivo US
	 * del RIPS consultorios
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaConsultoriosUS(Connection con,String numeroFactura,String numeroRemision)
	{
		return ripsDao.consultaConsultoriosUS(con,numeroFactura,numeroRemision);
	}
	/**
	 * Método para consultar los registros del archivo AD
	 * del RIPS consultorios
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaConsultoriosAD(Connection con,String numeroFactura,String numeroRemision)
	{
		return ripsDao.consultaConsultoriosAD(con,numeroFactura,numeroRemision);
	}
	/**
	 * Método para consultar los registros del archivo AF
	 * del RIPS consultorios
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaConsultoriosAF(Connection con,String numeroFactura,String numeroRemision)
	{
		return ripsDao.consultaConsultoriosAF(con,numeroFactura,numeroRemision);
	}
	/**
	 * Método para consultar los registros del RIPS por Rangos
	 * @param con
	 * @param codigoConvenio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaRegistrosPorRangos(Connection con,int codigoConvenio,String fechaInicial,String fechaFinal)
	{
		return ripsDao.consultaRegistrosPorRangos(con,codigoConvenio,fechaInicial,fechaFinal);
	}
	
	/**
	 * Método para consultar los registros del RIPS por Rangos Paciente
	 * @param con
	 * @param idCuenta
	 *
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaRegistrosRangosPaciente(Connection con,int idCuenta)
	{
		return ripsDao.consultaRegistrosRangosPaciente(con,idCuenta);
	}
	
	/**
	 * Método usado para cargar la información rips de un cita.
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap cargarDatosRipsCita(Connection con,int numeroSolicitud)
	{
		return ripsDao.cargarDatosRipsCita(con,numeroSolicitud);
	}
	
	/**
	 * Método usado para cargar parte de los datos rips de un solicitud diferente a cita que tiene cargada un servicio
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap cargarDatosRipsNoCita(Connection con,int numeroSolicitud)
	{
		return ripsDao.cargarDatosRipsNoCita(con,numeroSolicitud);
	}
	
	/**
	 * Método usado para consultar servicios por codigo CUPS
	 * @param con
	 * @param codigoCups
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultarServicios(Connection con,String codigoCups)
	{
		return ripsDao.consultarServicios(con,codigoCups);
	}
	
	/**
	 * Método usado para insertar un registro de cita en la tabla rips. CASO 1
	 * retorna el consecutivo automático del registro en la tabla
	 * @param con
	 * @param registro
	 * @param codigoInstitucion
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public int insertarDatosRipsCita(
			Connection con,HashMap registro,int codigoConvenio,
			String fechaInicial, String fechaFinal,int codigoInstitucion)
	{
		int tipos_cie[]= new int[3];
		String acronimos[]=new String[3];
		
		//revisión de los diagnosticos relacionados que no son obligatorios
		if((registro.get("diag_rel1_0")+"").equals("")||(registro.get("diag_rel1_0")+"").equals("null"))
		{
			tipos_cie[0]=-1;
			acronimos[0]=null;
		}
		else
		{
			tipos_cie[0]=Utilidades.convertirAEntero((registro.get("diag_rel1_0")+"").split(ConstantesBD.separadorSplit)[1]);
			acronimos[0]=(registro.get("diag_rel1_0")+"").split(ConstantesBD.separadorSplit)[0];
		}
		if((registro.get("diag_rel2_0")+"").equals("")||(registro.get("diag_rel2_0")+"").equals("null"))
		{
			tipos_cie[1]=-1;
			acronimos[1]=null;
		}
		else
		{
			tipos_cie[1]=Utilidades.convertirAEntero((registro.get("diag_rel2_0")+"").split(ConstantesBD.separadorSplit)[1]);
			acronimos[1]=(registro.get("diag_rel2_0")+"").split(ConstantesBD.separadorSplit)[0];
		}	
		if((registro.get("diag_rel3_0")+"").equals("")||(registro.get("diag_rel3_0")+"").equals("null"))
		{
			tipos_cie[2]=-1;
			acronimos[2]=null;
		}
		else
		{
			tipos_cie[2]=Utilidades.convertirAEntero((registro.get("diag_rel3_0")+"").split(ConstantesBD.separadorSplit)[1]);
			acronimos[2]=(registro.get("diag_rel3_0")+"").split(ConstantesBD.separadorSplit)[0];
		}
		
		
		return ripsDao.insertarDatorRipsCita(
				con,codigoConvenio,fechaInicial,fechaFinal,
				Utilidades.convertirAEntero((registro.get("servicio_0")+"").split(ConstantesBD.separadorSplit)[2]),
				Utilidades.convertirAEntero((registro.get("tipo_diag_0")+"").split(ConstantesBD.separadorSplit)[0]),
				(registro.get("diag_ppal_0")+"").split(ConstantesBD.separadorSplit)[0],
				Utilidades.convertirAEntero((registro.get("diag_ppal_0")+"").split(ConstantesBD.separadorSplit)[1]),
				acronimos[0],tipos_cie[0],
				acronimos[1],tipos_cie[1],
				acronimos[2],tipos_cie[2],
				Utilidades.convertirAEntero((registro.get("causa_externa_0")+"").split(ConstantesBD.separadorSplit)[0]),
				(registro.get("finalidad_0")+"").split(ConstantesBD.separadorSplit)[0],
				Double.parseDouble(registro.get("valor_total_0")+""),
				Double.parseDouble(registro.get("valor_copago_0")+""),
				Double.parseDouble(registro.get("valor_empresa_0")+""),
				registro.get("autorizacion_0")+"",
				Utilidades.convertirAEntero(registro.get("paciente_0")+""),
				Utilidades.convertirAEntero(registro.get("cuenta_0")+""),
				Utilidades.convertirAEntero(registro.get("solicitud_0")+""),
				codigoInstitucion,registro.get("fecha_0")+"");
	}
	
	/**
	 * Método usado para insertar un registro no cita con servicio asociado
	 * en la tabla rips. CASO 2
	 * retorna el consecutivo automático del registro en la tabla
	 * @param con
	 * @param registro
	 * @param codigoConvenio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoInstitucion
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public int insertarDatosRipsNoCita(
			Connection con,HashMap registro,int codigoConvenio,
			String fechaInicial, String fechaFinal,int codigoInstitucion)
	{
		int tipos_cie[]= new int[3];
		String acronimos[]=new String[3];
		int personal;
		int forma;
		int solicitud;
		
		//revisión de los diagnosticos ,pues no son obligatorios
		if((registro.get("diag_ppal_0")+"").equals("")||(registro.get("diag_ppal_0")+"").equals("null"))
		{
			tipos_cie[0]=-1;
			acronimos[0]=null;
		}
		else
		{
			tipos_cie[0]=Utilidades.convertirAEntero((registro.get("diag_ppal_0")+"").split(ConstantesBD.separadorSplit)[1]);
			acronimos[0]=(registro.get("diag_ppal_0")+"").split(ConstantesBD.separadorSplit)[0];
		}
		if((registro.get("diag_rel1_0")+"").equals("")||(registro.get("diag_rel1_0")+"").equals("null"))
		{
			tipos_cie[1]=-1;
			acronimos[1]=null;
		}
		else
		{
			tipos_cie[1]=Utilidades.convertirAEntero((registro.get("diag_rel1_0")+"").split(ConstantesBD.separadorSplit)[1]);
			acronimos[1]=(registro.get("diag_rel1_0")+"").split(ConstantesBD.separadorSplit)[0];
		}	
		if((registro.get("diag_rel2_0")+"").equals("")||(registro.get("diag_rel2_0")+"").equals("null"))
		{
			tipos_cie[2]=-1;
			acronimos[2]=null;
		}
		else
		{
			tipos_cie[2]=Utilidades.convertirAEntero((registro.get("diag_rel2_0")+"").split(ConstantesBD.separadorSplit)[1]);
			acronimos[2]=(registro.get("diag_rel2_0")+"").split(ConstantesBD.separadorSplit)[0];
		}
		
		if((registro.get("personal_0")+"").equals("")||(registro.get("personal_0")+"").equals("null"))
			personal=-1;
		else
			personal=Utilidades.convertirAEntero(registro.get("personal_0")+"");
		
		if((registro.get("forma_0")+"").equals("")||(registro.get("forma_0")+"").equals("null"))
			forma=-1;
		else
			forma=Utilidades.convertirAEntero(registro.get("forma_0")+"");
		
		if((registro.get("solicitud_0")+"").equals("")||(registro.get("solicitud_0")+"").equals("null")||(registro.get("solicitud_0")+"").equals("0"))
			solicitud=-1;
		else
			solicitud=Utilidades.convertirAEntero(registro.get("solicitud_0")+"");
		return ripsDao.insertarDatosRipsNoCita(
				con,codigoConvenio,fechaInicial,fechaFinal,
				Utilidades.convertirAEntero((registro.get("servicio_0")+"").split(ConstantesBD.separadorSplit)[2]),
				acronimos[0],tipos_cie[0],
				acronimos[1],tipos_cie[1],
				acronimos[2],tipos_cie[2],
				Double.parseDouble(registro.get("valor_total_0")+""),
				registro.get("autorizacion_0")+"",
				Utilidades.convertirAEntero(registro.get("ambito_0")+""),
				personal,
				forma,
				Utilidades.convertirAEntero(registro.get("paciente_0")+""),
				Utilidades.convertirAEntero(registro.get("cuenta_0")+""),
				solicitud,
				codigoInstitucion,registro.get("fecha_0")+"");
	}
	
	/**
	 * Método para insertar un registro Rips que tiene o no tiene solicitud,
	 * y que no tenía un servicio asociado. CASO 3
	 * retorna el consecutivo automático del registro en la tabla
	 * @param con
	 * @param registro
	 * @param codigoConvenio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param codigoInstitucion
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public int insertarDatosRipsSinServicio(
			Connection con,HashMap registro,int codigoConvenio,
			String fechaInicial,String fechaFinal,int codigoInstitucion)
	{
		int tipos_cie[]= new int[3];
		String acronimos[]=new String[3];
		int numeroSolicitud=-1;
		
		
		//revisión de los diagnosticos ,pues no son obligatorios
		if((registro.get("diag_rel1_0")+"").equals("")||(registro.get("diag_rel1_0")+"").equals("null"))
		{
			tipos_cie[0]=-1;
			acronimos[0]=null;
		}
		else
		{
			tipos_cie[0]=Utilidades.convertirAEntero((registro.get("diag_rel1_0")+"").split(ConstantesBD.separadorSplit)[1]);
			acronimos[0]=(registro.get("diag_rel1_0")+"").split(ConstantesBD.separadorSplit)[0];
		}
		if((registro.get("diag_rel2_0")+"").equals("")||(registro.get("diag_rel2_0")+"").equals("null"))
		{
			tipos_cie[1]=-1;
			acronimos[1]=null;
		}
		else
		{
			tipos_cie[1]=Utilidades.convertirAEntero((registro.get("diag_rel2_0")+"").split(ConstantesBD.separadorSplit)[1]);
			acronimos[1]=(registro.get("diag_rel2_0")+"").split(ConstantesBD.separadorSplit)[0];
		}	
		if((registro.get("diag_rel3_0")+"").equals("")||(registro.get("diag_rel3_0")+"").equals("null"))
		{
			tipos_cie[2]=-1;
			acronimos[2]=null;
		}
		else
		{
			tipos_cie[2]=Utilidades.convertirAEntero((registro.get("diag_rel3_0")+"").split(ConstantesBD.separadorSplit)[1]);
			acronimos[2]=(registro.get("diag_rel3_0")+"").split(ConstantesBD.separadorSplit)[0];
		}
		if((registro.get("solicitud_0")+"").equals("")||(registro.get("solicitud_0")+"").equals("null")||(registro.get("solicitud_0")+"").equals("0"))
		{
			numeroSolicitud=-1;
		}
		else
		{
			numeroSolicitud=Utilidades.convertirAEntero(registro.get("solicitud_0")+"");
		}
		return ripsDao.insertarDatosRipsSinServicio(
				con,codigoConvenio,fechaInicial,fechaFinal,
				Utilidades.convertirAEntero((registro.get("servicio_0")+"").split(ConstantesBD.separadorSplit)[2]),
				Utilidades.convertirAEntero(registro.get("tipo_diag_0")+""),
				(registro.get("diag_ppal_0")+"").split(ConstantesBD.separadorSplit)[0],
				Utilidades.convertirAEntero((registro.get("diag_ppal_0")+"").split(ConstantesBD.separadorSplit)[1]),
				acronimos[0],tipos_cie[0],
				acronimos[1],tipos_cie[1],
				acronimos[2],tipos_cie[2],
				Utilidades.convertirAEntero((registro.get("causa_externa_0")+"")),
				registro.get("finalidad_0")+"",
				Double.parseDouble(registro.get("valor_total_0")+""),
				Double.parseDouble(registro.get("valor_copago_0")+""),
				Double.parseDouble(registro.get("valor_empresa_0")+""),
				registro.get("autorizacion_0")+"",
				Utilidades.convertirAEntero(registro.get("paciente_0")+""),
				Utilidades.convertirAEntero(registro.get("cuenta_0")+""),
				numeroSolicitud,
				codigoInstitucion,registro.get("fecha_0")+"");
	}
	
	/**
	 * Método que consulta la tarifa del servicio y calcula el valor del copago según la cuenta
	 * @param con
	 * @param codigoServicio
	 * @param idCuenta
	 * @return valor total (separadorSplit) valor copago
	 */
	public String consultarValoresServicio(Connection con,int codigoServicio,int idCuenta)
	{
		return ripsDao.consultarValoresServicio(con,codigoServicio,idCuenta);
	}
	
	/**
	 * Método para consultar los servicio por descripcion
	 * @param con
	 * @param criterio
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultarServicioXNombre(Connection con,String criterio)
	{
		return ripsDao.consultarServicioXNombre(con,criterio);
	}
	
	/**
	 * Método para cargar el resumen de un registro RIPS
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultarRegistroRips(Connection con,int consecutivo)
	{
		return ripsDao.consultarRegistroRips(con,consecutivo);
	}
	
	/**
	 * Método usado para actualizar los registros RIPS consultorios que van a ser generados en los archivos
	 * RIPS
	 * @param con
	 * @param numeroFactura
	 * @param fechaFactura
	 * @param fechaRemision
	 * @param login
	 * @param registrosRangos
	 */
	@SuppressWarnings("rawtypes")
	public int actualizarDatosRips(
			Connection con,String numeroFactura,String fechaFactura,
			String fechaRemision,String login,HashMap registrosRangos
			)
	{
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		
		try
		{
			int numRegistros=Utilidades.convertirAEntero(registrosRangos.get("numRegistros")+"");
			int contadorRegistros=0; //variable usada para saber si hubo registros actualizados
			int estadoError=-1; //variable para indicar el tipo de error en la actualización
			
			myFactory.beginTransaction(con);
			
			//aumentar numero de la secuencia
			int numRemision=ripsDao.siguienteSecuenciaRemision(con);
			//numero de secuencia actual
			this.numeroRemision=numRemision+"";
			//se recorre todo el listado de registros 
			for(int i=0;i<numRegistros;i++)
			{
				String rips=registrosRangos.get("rips_"+i)+"";
				//se revisan los registros que fueron chequeados
				if(rips.equals("true"))
				{
					String consecutivo=registrosRangos.get("consecutivo_"+i)+"";
					//se verifica que los registros tengan consecutivo
					if(!consecutivo.equals("")&&!consecutivo.equals("null"))
					{
						String fechaAtencion=registrosRangos.get("fecha_"+i)+"";
						//actualización del registro
						int respuesta=ripsDao.actualizarDatosRips(con,Utilidades.convertirAEntero(consecutivo),numeroFactura,fechaFactura,fechaRemision,fechaAtencion,login);
						//se verifica que la actualizacion haya sido exitosa
						
						if(respuesta>=0)
							contadorRegistros++;
						else
							estadoError=-2;
					}
				}
			}
			
			
			
			if(contadorRegistros>0)
			{
				if(estadoError==-2)
				{
					myFactory.abortTransaction(con);
					return estadoError;
				}
				else
				{
					
					myFactory.endTransaction(con);
					
					return 1;
				}
			}
			else
			{
				myFactory.abortTransaction(con);
				return estadoError;
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en actualizarDatosRips: "+e);
			try
			{
				myFactory.abortTransaction(con);
			}
			catch(SQLException e1){
				logger.error("Error en actualizarDatosRips al abortar transaccion: "+e1);
			}
			return -2;
		}
	}
	
	/**
	 * Método usado para consultar la generación de RIPS consultorios
	 * @param con
	 * @param codigoConvenio
	 * @param fechaFactura
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public HashMap consultaGeneracionRips(Connection con,int codigoConvenio,String fechaFactura)
	{
		return ripsDao.consultaGeneracionRips(con,codigoConvenio,fechaFactura);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap verificacionRegistrosIniciales(Connection con,HashMap registros,int institucion)
	{
		int numeroRegistros=Utilidades.convertirAEntero(registros.get("numRegistros")+"");
		
		for(int i=0;i<numeroRegistros;i++)
		{
			//se revisa que el número de la solicitud exista en el registro
			if(!(registros.get("solicitud_"+i)+"").equals("")&&!(registros.get("solicitud_"+i)+"").equals("null")
					&&!(registros.get("solicitud_"+i)+"").equals("-1"))
			{
				//se verifica si el registro ya existe en la tabla rips_consultorios con la solicitud
				int consecutivoPrimero=ripsDao.verificarExistenciaRegistroRips(con,Utilidades.convertirAEntero(registros.get("solicitud_"+i)+""));
				if(consecutivoPrimero>0)
				{
					//si el registro ya existe se añade su consecutivo
					registros.put("consecutivo_"+i,consecutivoPrimero+"");
					registros.put("rips_"+i,"true");
					
					if((registros.get("datos_servicio_"+i)+"").equals("")||(registros.get("datos_servicio_"+i)+"").equals("null"))
					{
						HashMap registroTemporal=ripsDao.consultarRegistroRips(con,consecutivoPrimero);
						HashMap registroTemporal1=ripsDao.consultarServicio(con,Utilidades.convertirAEntero(registroTemporal.get("servicio_0")+""));
						registros.put("servicio_"+i,registroTemporal1.get("servicio_0"));
						registros.put("datos_servicio_"+i,registroTemporal1.get("datos_servicio_0"));
					}
					
					String[] datosServicio=(registros.get("datos_servicio_"+i)+"").split(ConstantesBD.separadorSplit);
					if(datosServicio[0].equals(ConstantesBD.codigoServicioInterconsulta+"")&&
							(datosServicio[1].equals("01")||datosServicio[1].equals("05")))
						{
						//verificacion de la excepción de rips consultorios en valoracion
						if(ripsDao.consultarExcepcionRipsConsultorios(con,consecutivoPrimero,institucion))
							registros.put("rips_"+i,"true");
						else
							registros.put("rips_"+i,"false");
						}
				}
				else
				{	
					//si el registro no existe se hace un análisis para saber si es apto para tener una inserción automática
					//se revisa si el registro tiene servicio asociado
					if(!(registros.get("datos_servicio_"+i)+"").equals(""))
					{
						String[] datosServicio=(registros.get("datos_servicio_"+i)+"").split(ConstantesBD.separadorSplit);
						if(datosServicio[0].equals(ConstantesBD.codigoServicioInterconsulta+"")&&
							(datosServicio[1].equals("01")||datosServicio[1].equals("05")))
						{
							int numeroSolicitud=Utilidades.convertirAEntero(registros.get("solicitud_"+i)+"");
							//Se cargan los datos de la solicitud
							HashMap datosRips=this.cargarDatosRipsCita(con,numeroSolicitud);
							
							//se revisan que se encuentren los datos requeridos
							if(!(datosRips.get("diag_ppal_0")+"").equals("")&&!(datosRips.get("diag_ppal_0")+"").equals("null")&&
								!(datosRips.get("tipo_diag_0")+"").equals("")&&!(datosRips.get("tipo_diag_0")+"").equals("null")&&
								!(datosRips.get("causa_externa_0")+"").equals("")&&!(datosRips.get("causa_externa_0")+"").equals("null")&&
								!(datosRips.get("finalidad_0")+"").equals("")&&!(datosRips.get("finalidad_0")+"").equals("null")&&
								!(datosRips.get("valor_total_0")+"").equals("")&&!(datosRips.get("valor_total_0")+"").equals("null")&&
								!(datosRips.get("valor_copago_0")+"").equals("")&&!(datosRips.get("valor_copago_0")+"").equals("null"))
							{
								//se calcula el valor empresa
								double valorEmpresa=Double.parseDouble(datosRips.get("valor_total_0")+"")-Double.parseDouble(datosRips.get("valor_copago_0")+"");
								if(valorEmpresa<0)
									valorEmpresa=0;
								datosRips.put("valor_empresa_0",valorEmpresa+"");
								
								//se asigna la fecha
								datosRips.put("fecha_0",UtilidadFecha.conversionFormatoFechaABD(registros.get("fecha_"+i)+""));
								
								int consecutivo=this.insertarDatosRipsCita(con,datosRips,convenio,fechaInicial,fechaFinal,institucion);
								// si la inserción es válidad entonces se añade el consecutivo del registro
								if(consecutivo>0)
								{
									registros.put("consecutivo_"+i,consecutivo+"");
									registros.put("rips_"+i,"true");
								}
							}
							
							//verificacion de la excepción de rips consultorios en valoracion
							if(ripsDao.consultarExcepcionRipsConsultorios(con,numeroSolicitud,institucion))
								registros.put("rips_"+i,"true");
							else
								registros.put("rips_"+i,"false");
						}
					}
				}
			}
			//se verifica si ya es un registro OTRO que ya tiene insercion en rips_consultorios
			else if((registros.get("solicitud_"+i)+"").equals("-1"))
			{
				//como el registro ya existe se añade su consecutivo
				registros.put("consecutivo_"+i,registros.get("tipo_"+i));
				registros.put("rips_"+i,"true");
			}
		}
		
		return registros;
	}
	
	/**
	 * Método para actualizar los datos de un registro RIPS ya insertado
	 * @param con
	 * @param consecutivo
	 * @param valorTotal
	 * @param valorCopago
	 * @param valorEmpresa
	 * @param autorizacion
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public int actualizarDatosRips(Connection con,int consecutivo,HashMap registro)
	{
		double valorCopago=-1;
		double valorEmpresa=-1;
		if(!(registro.get("valor_copago_0")+"").equals("")&&!(registro.get("valor_copago_0")+"").equals("null"))
			valorCopago=Double.parseDouble(registro.get("valor_copago_0")+"");
		if(!(registro.get("valor_empresa_0")+"").equals("")&&!(registro.get("valor_empresa_0")+"").equals("null"))
			valorEmpresa=Double.parseDouble(registro.get("valor_empresa_0")+"");
		
		
		return ripsDao.actualizarDatosRips(con,consecutivo,
				Double.parseDouble(registro.get("valor_total_0")+""),
				valorCopago,valorEmpresa,registro.get("autorizacion_0")+"");
		
	}
	
	/**
	 * Método usado para insertar una excepcion de rips consultorios
	 * desde la valoración
	 * @param con
	 * @param numeroSolicitud
	 * @param institucion
	 * @param rips
	 * @return
	 */
	public int insertarExcepcionRipsConsultorios(Connection con,int numeroSolicitud,int institucion,boolean rips)
	{
		return ripsDao.insertarExcepcionRipsConsultorios(con,numeroSolicitud,institucion,rips);
	}
	
	
	/**
	 * Método que consulta los datos de la cuenta de cobro capitacion
	 * que no se encuentre anulada
	 * @param con
	 * @param numeroCuentaCobro
	 * @param institucion
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static HashMap consultarDatosCxCCapitacion(Connection con,String numeroCuentaCobro,int institucion)
	{
		HashMap campos = new HashMap();
		campos.put("cuentaCobro",numeroCuentaCobro);
		campos.put("institucion", institucion);
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRipsDao().consultarDatosCxCCapitacion(con, campos);
	}
	
	//******************SETTERS & GETTERS****************************************
	
	
	/**
	 * @return Returns the convenio.
	 */
	public int getConvenio() {
		return convenio;
	}
	/**
	 * @param convenio The convenio to set.
	 */
	public void setConvenio(int convenio) {
		this.convenio = convenio;
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
	 * @return Returns the numeroCuentaCobro.
	 */
	public double getNumeroCuentaCobro() {
		return numeroCuentaCobro;
	}
	/**
	 * @param numeroCuentaCobro The numeroCuentaCobro to set.
	 */
	public void setNumeroCuentaCobro(double numeroCuentaCobro) {
		this.numeroCuentaCobro = numeroCuentaCobro;
	}
	/**
	 * @return Returns the fechaRemision.
	 */
	public String getFechaRemision() {
		return fechaRemision;
	}
	/**
	 * @param fechaRemision The fechaRemision to set.
	 */
	public void setFechaRemision(String fechaRemision) {
		this.fechaRemision = fechaRemision;
	}
	/**
	 * @return Returns the numeroRemision.
	 */
	public String getNumeroRemision() {
		return numeroRemision;
	}
	/**
	 * @param numeroRemision The numeroRemision to set.
	 */
	public void setNumeroRemision(String numeroRemision) {
		this.numeroRemision = numeroRemision;
	}
	/**
	 * @return Returns the ripsConFactura.
	 */
	public boolean isRipsConFactura() {
		return ripsConFactura;
	}
	/**
	 * @param ripsConFactura The ripsConFactura to set.
	 */
	public void setRipsConFactura(boolean ripsConFactura) {
		this.ripsConFactura = ripsConFactura;
	}
	/**
	 * @return Returns the seleccionArchivos.
	 */
	@SuppressWarnings("rawtypes")
	public HashMap getSeleccionArchivos() {
		return seleccionArchivos;
	}
	/**
	 * @param seleccionArchivos The seleccionArchivos to set.
	 */
	@SuppressWarnings("rawtypes")
	public void setSeleccionArchivos(HashMap seleccionArchivos) {
		this.seleccionArchivos = seleccionArchivos;
	}
	/**
	 * @return Returns the esFactura.
	 */
	public boolean isEsFactura() {
		return esFactura;
	}
	/**
	 * @param esFactura The esFactura to set.
	 */
	public void setEsFactura(boolean esFactura) {
		this.esFactura = esFactura;
	}
	/**
	 * @return Returns the huboInconsistencias.
	 */
	public boolean isHuboInconsistencias() {
		return huboInconsistencias;
	}
	/**
	 * @param huboInconsistencias The huboInconsistencias to set.
	 */
	public void setHuboInconsistencias(boolean huboInconsistencias) {
		this.huboInconsistencias = huboInconsistencias;
	}
	/**
	 * @return Returns the numeroFactura.
	 */
	public String getNumeroFactura() {
		return numeroFactura;
	}
	/**
	 * @param numeroFactura The numeroFactura to set.
	 */
	public void setNumeroFactura(String numeroFactura) {
		this.numeroFactura = numeroFactura;
	}
	/**
	 * @return Returns the tipoRips.
	 */
	public String getTipoRips() {
		return tipoRips;
	}
	/**
	 * @param tipoRips The tipoRips to set.
	 */
	public void setTipoRips(String tipoRips) {
		this.tipoRips = tipoRips;
	}
	/**
	 * @return Returns the registrosRangos.
	 */
	@SuppressWarnings("rawtypes")
	public HashMap getRegistrosRangos() {
		return registrosRangos;
	}
	/**
	 * @param registrosRangos The registrosRangos to set.
	 */
	@SuppressWarnings("rawtypes")
	public void setRegistrosRangos(HashMap registrosRangos) {
		this.registrosRangos = registrosRangos;
	}
	/**
	 * @return Returns the loginUsuario.
	 */
	public String getLoginUsuario() {
		return loginUsuario;
	}
	/**
	 * @param loginUsuario The loginUsuario to set.
	 */
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}
	/**
	 * @return Returns the fechaFactura.
	 */
	public String getFechaFactura() {
		return fechaFactura;
	}
	/**
	 * @param fechaFactura The fechaFactura to set.
	 */
	public void setFechaFactura(String fechaFactura) {
		this.fechaFactura = fechaFactura;
	}
	/**
	 * @return Returns the pathGeneracion.
	 */
	public String getPathGeneracion() {
		return pathGeneracion;
	}
	/**
	 * @param pathGeneracion The pathGeneracion to set.
	 */
	public void setPathGeneracion(String pathGeneracion) {
		this.pathGeneracion = pathGeneracion;
	}
	/**
	 * @return Returns the huboRegistros.
	 */
	public boolean isHuboRegistros() {
		return huboRegistros;
	}

	/**
	 * @return Returns the tipoCodigo.
	 */
	public int getTipoCodigo() {
		return tipoCodigo;
	}

	/**
	 * @param tipoCodigo The tipoCodigo to set.
	 */
	public void setTipoCodigo(int tipoCodigo) {
		this.tipoCodigo = tipoCodigo;
	}

	/**
	 * @return the codigosContrato
	 */
	public Vector<Object> getCodigosContrato() {
		return codigosContrato;
	}

	/**
	 * @param codigosContrato the codigosContrato to set
	 */
	public void setCodigosContrato(Vector<Object> codigosContrato) {
		this.codigosContrato = codigosContrato;
	}

	/**
	 * @return the numerosContrato
	 */
	public Vector<Object> getNumerosContrato() {
		return numerosContrato;
	}

	/**
	 * @param numerosContrato the numerosContrato to set
	 */
	public void setNumerosContrato(Vector<Object> numerosContrato) {
		this.numerosContrato = numerosContrato;
	}

	/**
	 * @return the valorCuenta
	 */
	public String getValorCuenta() {
		return valorCuenta;
	}

	/**
	 * @param valorCuenta the valorCuenta to set
	 */
	public void setValorCuenta(String valorCuenta) {
		this.valorCuenta = valorCuenta;
	}

	/**
	 * @return the numeroEnvio
	 */
	public String getNumeroEnvio() {
		return numeroEnvio;
	}

	/**
	 * @param numeroEnvio the numeroEnvio to set
	 */
	public void setNumeroEnvio(String numeroEnvio) {
		this.numeroEnvio = numeroEnvio;
	}

	/**
	 * @return the nroFacturaAx
	 */
	public String getNroFacturaAx() {
		return nroFacturaAx;
	}

	/**
	 * @param nroFacturaAx the nroFacturaAx to set
	 */
	public void setNroFacturaAx(String nroFacturaAx) {
		this.nroFacturaAx = nroFacturaAx;
	}

	/**
	 * @return the esAxRips
	 */
	public boolean isEsAxRips() {
		return esAxRips;
	}

	/**
	 * @param esAxRips the esAxRips to set
	 */
	public void setEsAxRips(boolean esAxRips) {
		this.esAxRips = esAxRips;
	}

	/**
	 * @return the generoArchvsForecat
	 */
	public boolean isGeneroArchvsForecat() {
		return generoArchvsForecat;
	}

	/**
	 * @param generoArchvsForecat the generoArchvsForecat to set
	 */
	public void setGeneroArchvsForecat(boolean generoArchvsForecat) {
		this.generoArchvsForecat = generoArchvsForecat;
	}

	/**
	 * @return the pathGeneracionForecat
	 */
	public String getPathGeneracionForecat() {
		return pathGeneracionForecat;
	}

	/**
	 * @param pathGeneracionForecat the pathGeneracionForecat to set
	 */
	public void setPathGeneracionForecat(String pathGeneracionForecat) {
		this.pathGeneracionForecat = pathGeneracionForecat;
	}

	/**
	 * @return the resultadosForecat
	 */
	public HashMap<String, Object> getResultadosForecat() {
		return resultadosForecat;
	}

	/**
	 * @param resultadosForecat the resultadosForecat to set
	 */
	public void setResultadosForecat(HashMap<String, Object> resultadosForecat) {
		this.resultadosForecat = resultadosForecat;
	}

	public ArrayList<String> getArchivosGenerados() {
		return archivosGenerados;
	}

	public void setArchivosGenerados(ArrayList<String> archivosGenerados) {
		this.archivosGenerados = archivosGenerados;
	}

	public String getNomZip() {
		return nomZip;
	}

	public void setNomZip(String nomZip) {
		this.nomZip = nomZip;
	}

	/**
	 * @return the zip
	 */
	public boolean isZip() {
		return zip;
	}

	/**
	 * @param zip the zip to set
	 */
	public void setZip(boolean zip) {
		this.zip = zip;
	}

	/**
	 * @return the errores
	 */
	public ActionErrors getErrores() {
		return errores;
	}

	/**
	 * @param errores the errores to set
	 */
	public void setErrores(ActionErrors errores) {
		this.errores = errores;
	}

	/**
	 * @return the objConvenio
	 */
	public Convenio getObjConvenio() {
		return objConvenio;
	}

	/**
	 * @param objConvenio the objConvenio to set
	 */
	public void setObjConvenio(Convenio objConvenio) {
		this.objConvenio = objConvenio;
	}
	
	/**
	 * @return the nitEntidad
	 */
	public String getNitEntidad() {
		return nitEntidad;
	}

	/**
	 * @param nitEntidad the nitEntidad to set
	 */
	public void setNitEntidad(String nitEntidad) {
		this.nitEntidad = nitEntidad;
	}
	
	/**
	 * Nombre del Tercero para mostrar como Entidad
	 * @param con
	 * @param nitTercero
	 * @return
	 */
	public String consultarNombreTercero(Connection con, String nitTercero) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRipsDao().consultarNombreTercero(con, nitTercero);
	}

	/**
	 * Nit del Convenio 
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public String consultarNitConvenio(Connection con, int codigoConvenio) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRipsDao().consultarNitConvenio(con, codigoConvenio);
	}
}
