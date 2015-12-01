/*
 * Junio 06, 2006
 */
package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadFecha;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.InformacionPartoDao;

/**
 * @author Sebastián Gómez 
 *
 *Clase que representa el Mundo con sus atributos y métodos de la funcionalidad
 * Informacion Parto
 */
public class InformacionParto 
{
	
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(InformacionParto.class);
	
	
	/**
	 * DAO para el manejo de DestinoTriageDao
	 */
	private InformacionPartoDao partoDao=null;
	
	//***********ATRIBUTOS****************************
	/**
	 * Mapa donde se almacena los datos del parto
	 */
	private HashMap infoParto = new HashMap();
	
	/**
	 * Consecutivo Axioma de la informacion del parto
	 */
	private String consecutivo;
	
	/**
	 * Código de la cirugia
	 */
	private String codigoCirugia;
	
	/**
	 * mapa de la seccion vigilancia clinica Map
	 */
	private HashMap vigilanciaClinicaMap= new HashMap();
	
	
	//********CONSTRUCTORES E INICIALIZADORES********************
	
	/**
	 * Constructor
	 */
	public InformacionParto() {
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * método para inicializar los datos
	 *
	 */
	public void clean()
	{
		this.infoParto = new HashMap();
		this.consecutivo = "";
		this.codigoCirugia = "";
		this.vigilanciaClinicaMap= new HashMap();
		this.vigilanciaClinicaMap.put("numRegistros", "0");
	}
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (partoDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			partoDao = myFactory.getInformacionPartoDao();
		}	
	}
	//************METODOS****************************************
	
	/**
	 * Método implementado para cargar la información del parto
	 */
	public boolean cargar(Connection con,String institucion)
	{
		HashMap campos = new HashMap();
		campos.put("consecutivo", this.consecutivo);
		campos.put("cirugia", this.codigoCirugia);
		campos.put("institucion", institucion);
		
		this.infoParto = partoDao.cargarInformacionParto(con,campos);
		if(!this.infoParto.containsKey("presentacion"))
			this.infoParto.put("presentacion", "");
		//se verifica exito de la consulta
		if(Integer.parseInt(infoParto.get("numRegistros").toString())>0)
			return true;
		else
			return false;
	}
	
	/**
	 * Método implementado para insertar la información del parto
	 */
	public boolean insertar(Connection con,String estado)
	{
		this.consecutivo = partoDao.insertarInformacionParto(con,this.infoParto,estado);
		
		//se verifica exito del proceso
		if(consecutivo.equals(""))
			return false;
		else
			return true;
	}
	
	/**
	 * Toca tener lleno en el objeto el consecutivo y el mapa de vigencia clinica map 
	 * @param con
	 * @return
	 */
	public boolean insertarVigilenciaClinica(Connection con, String usuario)
	{
		return partoDao.insertarVigilenciaClinica(con, this.consecutivo, this.getVigilanciaClinicaMap(), usuario);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param consecutivoInfoParto
	 * @param vigilanciaClinicaMap
	 * @return
	 */
	public boolean modificarVigilanciaParto (	Connection con, String usuario)
	{
		return partoDao.modificarVigilanciaParto(con, this.consecutivo, this.getVigilanciaClinicaMap(), usuario);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoInfoParto
	 * @param partogramaMap
	 * @return
	 */
	public boolean insertarPartogramaGeneral (	Connection con,
												String consecutivoInfoParto,
												HashMap partogramaMap,
												String loginUsuario
											  )
	{
		//primero se verifica si existe, de no ser asi se inserta todo
		if(!existePartogramaDadoConsecutivoInfoParto(con, consecutivoInfoParto))
		{
			if(!this.insertarPartograma(con, consecutivoInfoParto, partogramaMap))
			{
				//no inserto el partograma general
				return false;
			}
		}
		else
		{
			//se comienza modificando el partograma ppal
			if(		!partogramaMap.get("codigoposicion").toString().trim().equals("") 
					&& !partogramaMap.get("codigoparidad").toString().trim().equals("") 
					&& !partogramaMap.get("codigomembrana").toString().trim().equals(""))
			{
				if(!this.modificarPartograma(con, consecutivoInfoParto, partogramaMap))
				{
					return false;	
				}
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoInfoParto
	 * @return
	 */
	public boolean existePartogramaDadoConsecutivoInfoParto(Connection con, String consecutivoInfoParto)
	{
		return partoDao.existePartogramaDadoConsecutivoInfoParto(con, consecutivoInfoParto);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoInfoParto
	 * @param partogramaMap
	 * @return
	 */
	public boolean insertarPartograma (	Connection con,
												String consecutivoInfoParto,
												HashMap partogramaMap
											  )
	{
		if(		!partogramaMap.get("codigoposicion").toString().trim().equals("") 
				&& !partogramaMap.get("codigoparidad").toString().trim().equals("") 
				&& !partogramaMap.get("codigomembrana").toString().trim().equals(""))
		{
			return partoDao.insertarPartograma(con, consecutivoInfoParto, partogramaMap);
		}
		return true;
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoInfoParto
	 * @param partogramaMap
	 * @return
	 */
	public boolean modificarPartograma (	Connection con,
												String consecutivoInfoParto,
												HashMap partogramaMap
											  )
	{
		return partoDao.modificarPartograma(con, consecutivoInfoParto, partogramaMap);
	}
	
	/**
	 * Método que consulta el listado de solicitudes para ingresar informacion de parto
	 * @param con
	 * @param codigoPaciente
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param centroAtencion
	 * @return
	 */
	public HashMap cargarSolicitudesInformacionParto(Connection con,String codigoPaciente,String fechaInicial,String fechaFinal,int centroAtencion)
	{
		return partoDao.cargarSolicitudesInformacionParto(con,codigoPaciente,fechaInicial,fechaFinal,centroAtencion);
	}
	
	/**
	 * Método que consulta el consecutivo de la cirugia de partos
	 * qeu está dentro de la orden de cirugía
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public String obtenerCodigoCirugiaParto(Connection con,String numeroSolicitud)
	{
		return partoDao.obtenerCodigoCirugiaParto(con,numeroSolicitud);
		
	}
	
	/**
	 * Método que consulta la fecha/hora del egreso de una admision de un parto
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public String obtenerFechaHoraEgresoAdmisionParto(Connection con,String codigoCuenta)
	{
		return partoDao.obtenerFechaHoraEgresoAdmisionParto(con,codigoCuenta);
	}
	
	/**
	 * Método implementado para verificar si el paciente tiene informacion de partos
	 * pendiente de ingresar
	 * @param con
	 * @param codigoPaciente
	 * @param centroAtencion
	 * @return
	 */
	public String validacionInformacionPartoPaciente(Connection con,String codigoPaciente,int centroAtencion)
	{
		return partoDao.validacionInformacionPartoPaciente(con,codigoPaciente,centroAtencion);
	}
	
	/**
	 * Método que consulta el numero de embarazo de un embarazo no finalizado del paciente
	 * desde la hoja obstétrica
	 * @param con
	 * @param codigoPaciente
	 * @return
	 */
	public int consultarNumeroEmbarazoHojaObstetrica(Connection con,String codigoPaciente)
	{
		return partoDao.consultarNumeroEmbarazoHojaObstetrica(con, codigoPaciente);
	}
	
	/**
	 * Método que verifica si ya existe un numero de embarazo en los registros de hoja obstétrica del paciente
	 * @param con
	 * @param codigoPaciente
	 * @param numeroEmbarazo
	 * @return
	 */
	public boolean existeNumeroEmbarazoHojaObstetrica(Connection con,String codigoPaciente,int numeroEmbarazo)
	{
		return partoDao.existeNumeroEmbarazoHojaObstetrica(con, codigoPaciente, numeroEmbarazo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPaciente
	 * @param numeroEmbarazo
	 * @return
	 */
	public int obtenerConsultasPrenatalesHojaObstetrica(Connection con,String codigoPaciente,int numeroEmbarazo)
	{
		HashMap campos = new HashMap();
		campos.put("codigoPaciente", codigoPaciente);
		campos.put("numeroEmbarazo", numeroEmbarazo+"");
		return partoDao.obtenerConsultasPrenatalesHojaObstetrica(con,campos);
	}
	
	/**
	 * Método implementado para calcular las horas egreso postparto para la impresion del CLAP
	 * @param con
	 * @param fechaParto
	 * @param horaParto
	 * @param codigoCirugia
	 * @return
	 */
	public String calcularHorasEgresoParto(Connection con, String fechaParto, String horaParto, String codigoCirugia) 
	{
		String horasPostParto = "";
		
		logger.info("fecha/Hora Parto "+fechaParto+ " / "+horaParto);
		if(!fechaParto.equals("")&&!horaParto.equals(""))
		{
			String fechaHoraEgreso = partoDao.obtenerFechaHoraEgresoAdmisionPartoCirugia(con, codigoCirugia);
			logger.info("fechaHoraEgreso=> "+fechaHoraEgreso);
			if(!fechaHoraEgreso.equals(""))
			{
				String[] vector = fechaHoraEgreso.split(ConstantesBD.separadorSplit);
				String[] fechaHora = new String[0];
				int minutos = 0;
				int horas = 0;
				
				while(!UtilidadFecha.compararFechas(fechaParto,horaParto,vector[0],vector[1]).isTrue())
				{
					fechaHora = UtilidadFecha.incrementarMinutosAFechaHora(fechaParto,horaParto,1,false);
					minutos ++;
					if(minutos==60)
					{
						minutos = 0;
						horas ++;
					}
					fechaParto =  fechaHora[0];
					horaParto = fechaHora[1];
				}
				
				if(horas>0)
					horasPostParto = horas + "";
			}
		}
		
		return horasPostParto;
	}
	//********GETTERS & SETTERS*************************************
	
	/**
	 * 
	 * @param con
	 * @param consecutivoInfoParto
	 * @return
	 */
	public HashMap cargarVigilanciaClinica(	Connection con, 
			        						String consecutivoInfoParto
										 )
	{
		this.vigilanciaClinicaMap=partoDao.cargarVigilanciaClinica(con, consecutivoInfoParto);
		return this.vigilanciaClinicaMap;
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoInfoParto
	 * @return
	 */
	public HashMap cargarPartograma(Connection con, String consecutivoInfoParto)
	{
		return partoDao.cargarPartograma(con, consecutivoInfoParto);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoCx
	 * @return
	 */
	public static String obtenerConsecutivoInfoPartoDadoCx(Connection con, String codigoCx)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getInformacionPartoDao().obtenerConsecutivoInfoPartoDadoCx(con, codigoCx);
	}
	

	/**
	 * 
	 * @param con
	 * @param codigoCx
	 * @param codigoInstitucion
	 * @param fromStatement
	 * @return
	 */
	public static String obtenerControlPrenatal(Connection con, String codigoCx, int codigoInstitucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getInformacionPartoDao().obtenerControlPrenatal(con, codigoCx, codigoInstitucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoInfoParto
	 * @return
	 */
	public static String obtenerUltimaFechaHoraProcesoPartograma(Connection con, String consecutivoInfoParto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getInformacionPartoDao().obtenerUltimaFechaHoraProcesoPartograma(con, consecutivoInfoParto);
	}
	
	
	
	/**
	 * Metodo que retorna true en caso de ser un aborto.
	 * retorna false, en caso de no ser un aborto, o no tener informacion del parto.
	 * @param con
	 * @param consecutivoInfoParto
	 * @return
	 */
	public static boolean esCirugiaAborto(Connection con, int cirugia)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getInformacionPartoDao().esCirugiaAborto(con, cirugia);
	}
	

	/**
	 * Metodo que retorna la cantidad de hijos vivos + cantidad de hijos muertos.
	 * en caso de no terner informacion de parto, registrada en la cirugia o si no se han dado los valores para cantidad de hijos vivos y muertos , retorna  ConstantesBD.codigoNuncaValido;
	 * @param con
	 * @param consecutivoInfoParto
	 * @return
	 */
	public static int cantidadHijosVivosMuertos(Connection con, int cirugia)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getInformacionPartoDao().cantidadHijosVivosMuertos(con, cirugia);
	}
	
	/**
	 * Método implementado para consultar la fecha/hora ingreso a hospitalizacion
	 * a partir de la cuenta asociada
	 * @param con
	 * @param cuentaAsocio
	 * @return
	 */
	public static String[] consultarFechaIngresoCasoAsocio(Connection con,String cuentaAsocio)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getInformacionPartoDao().consultarFechaIngresoCasoAsocio(con, cuentaAsocio);
	}
	
	/**
	 * @return Returns the infoParto.
	 */
	public HashMap getInfoParto() {
		return infoParto;
	}
	/**
	 * @param infoParto The infoParto to set.
	 */
	public void setInfoParto(HashMap infoParto) {
		this.infoParto = infoParto;
	}
	/**
	 * @param asigna un elemento al mapa infoParto.
	 */
	public void setInfoParto(String key,Object obj) {
		this.infoParto.put(key,obj);
	}
	/**
	 * @return Returns the consecutivo.
	 */
	public String getConsecutivo() {
		return consecutivo;
	}
	/**
	 * @param consecutivo The consecutivo to set.
	 */
	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}
	/**
	 * @return the codigoCirugia
	 */
	public String getCodigoCirugia() {
		return codigoCirugia;
	}
	/**
	 * @param codigoCirugia the codigoCirugia to set
	 */
	public void setCodigoCirugia(String codigoCirugia) {
		this.codigoCirugia = codigoCirugia;
	}
	
	/**
	 * @return Returns the vigilanciaClinicaMap.
	 */
	public HashMap getVigilanciaClinicaMap()
	{
		return vigilanciaClinicaMap;
	}

	/**
	 * @param vigilanciaClinicaMap The vigilanciaClinicaMap to set.
	 */
	public void setVigilanciaClinicaMap(HashMap vigilanciaClinicaMap)
	{
		this.vigilanciaClinicaMap = vigilanciaClinicaMap;
	}
	
	/**
	 * @return Returns the vigilanciaClinicaMap.
	 */
	public Object getVigilanciaClinicaMap(Object key)
	{
		return vigilanciaClinicaMap.get(key);
	}

	/**
	 * @param vigilanciaClinicaMap The vigilanciaClinicaMap to set.
	 */
	public void setVigilanciaClinicaMap(Object key, Object value)
	{
		this.vigilanciaClinicaMap.put(key, value);
	}
	
	
}
