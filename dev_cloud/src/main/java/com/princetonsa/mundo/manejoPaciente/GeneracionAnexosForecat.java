/*
 * Abril 30, 2007
 */
package com.princetonsa.mundo.manejoPaciente;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatos;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.UtilidadesFacturacion;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.GeneracionAnexosForecatDao;
import com.princetonsa.mundo.facturacion.ParametrizacionInstitucion;

/**
 * @author Sebastián Gómez 
 *
 *Clase que representa el Mundo con sus atributos y métodos de la funcionalidad
 * Generación Anexos Forecat
 */
public class GeneracionAnexosForecat 
{
	private static Logger logger = Logger.getLogger(GeneracionAnexosForecat.class);
	/**
	 * DAO para el manejo de GeneracionAnexosForecat
	 */
	GeneracionAnexosForecatDao generacionDao = null;
	
	//**********ATRIBUTOS**********************************************
	/**
	 * convenio FISALUD
	 */
	private int convenio;
	
	/**
	 * Tarifario Oficial usado para generar los archivos FORECAT
	 */
	private int tipoCodigo;
	/**
	 * fecha inicial de rango de generación de FORECAT 
	 */
	private String fechaInicial;
	/**
	 * fecha final de rango de generación de FORECAT 
	 */
	private String fechaFinal;
	/**
	 * fecha de remisión de la generación de FORECAT
	 */
	private String fechaRemision;
	
	/**
	 * Número de Remisión de la generación de FORECAT
	 */
	private String numeroRemision;
	/**
	 * Institución en la que se van a hacer los archivos RIPS
	/**
	 * Institución en la que se van a hacer los archivos FORECAT
	 *
	 */
	private ParametrizacionInstitucion institucion;
	
	
	/**
	 * Directorio donde se almacenarán los FORECAT
	 */
	private File dirConvenio;
	
	/**
	 * Archivo que indica inconsistencias en la generación de archivos
	 */
	private boolean huboInconsistencias;
	
	/**
	 * HashMap que almacena el contenido del archivo de inconsistencias
	 */
	private HashMap inconsistencias;
	
	/**
	 * Hashmap para almacenar el número de registros creados por archivo
	 *
	 */
	private HashMap numeroRegistrosCreados;
	
	/**
	 * Variable que almacena el path donde se generaron los archivos FORECAT
	 */
	private String pathGeneracion;
	
	/**
	 * Variable que indica si hubo información para generar FORECAT
	 */
	private boolean huboRegistros;
	
	/**
	 * 1/4 del salario mínimo vigente
	 */
	private double salarioMinimo;
	
	
	/**
	 * Variable referente a la ejecucion de generacion de rips para la interfaz ax_rips en caso de ser true
	 */
	private boolean esAxRips;
	
	
	/**
	 * Variable utilizada para la recepcion del numero de factura, esto cuanndo se realiza una generacion de archivos forecat para ax_rips
	 */
	private int numeroFactura;
	
	
	/**
	 * Variable bandera de identificacion de si es una generacion automatica de rips, 
	 * esto para modificar la ruta de exposicion del archivo al usuario en pantalla
	 */
	private boolean esRipsForecatAuto;
	
	
	/**
	 * Variable que almacena el numero de cuenta cobro para la accion de generacion de rips automaticos para cuentas_cobro
	 */
	private double numeroCuentaCobro;
	
	
	/**
	 * Bandera de diferenciacion sobre la generacion de los forectas automaticos, diferencia si es para factura o para cuentas cobro 
	 */
	private boolean esCuentaCobro;
	
	/**
	 * Manejo de los errores 
	 */
	private ActionErrors errores = new ActionErrors();
	
	//**********INICIALIZADORES & CONSTRUCTORES***********************
	/**
	 * Constructor
	 */
	public GeneracionAnexosForecat() {
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * método para inicializar los datos
	 *
	 */
	public void clean()
	{
		this.convenio = 0;
		this.tipoCodigo = 0;
		this.fechaInicial = "";
		this.fechaFinal = "";
		this.fechaRemision = "";
		this.numeroRemision = "";
		this.institucion = new ParametrizacionInstitucion();
		this.huboInconsistencias = false;
		this.inconsistencias = new HashMap();
		this.numeroRegistrosCreados = new HashMap();
		this.pathGeneracion = "";
		this.huboRegistros = false;
		this.salarioMinimo = 0;
		this.esAxRips = false;
		this.numeroFactura = 0;
		this.esRipsForecatAuto = false;
		this.numeroCuentaCobro = 0;
		this.esCuentaCobro = false;
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
		if (generacionDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			generacionDao = myFactory.getGeneracionAnexosForecatDao();
		}	
	}
	
	//*************MÉTODOS*************************************************
	
	/**
	 * Método que consulta el contenido de un archivo por su prefijo
	 * @param codigoInstitucion
	 * @param prefijo
	 * @return
	 */
	public HashMap cargarArchivo(Connection con,int codigoInstitucion,String prefijo)
	{
		//objeto usado para llenar el contenido del archivo
		HashMap contenido=new HashMap();
		String separador=System.getProperty("file.separator");
		//***SE CARGA LA INSTITUCIÓN***************************
		institucion=new ParametrizacionInstitucion();
		institucion.cargar(con,codigoInstitucion);
		
		//****SE EDITA LA RUTA*******************************
		File directorio = new File(institucion.getPath());
		//Se añade nuevo directorio para el convenio
		dirConvenio=new File(directorio,"RIPS"+separador+codigoInstitucion+separador+"C"+this.convenio+separador+"FORECAT");
		
		try
		{
			int contador=0;
			String cadena="";
			//******SE INICIALIZA ARCHIVO*************************
			String aux=UtilidadTexto.revisarCaracteresEspeciales(this.numeroRemision);
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
	 * Método centralizado para la generación de los archivos FORECAT
	 * @param con
	 * @param numeroRemision
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap generarArchivos(Connection con,int codigoInstitucion)
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
		
			
		//*****VALIDACIÓN DE LA RUTA***************
		
		//caso en que la ruta sea nula (no se haya definido)
		if(institucion.getPath()==null)
			institucion.setPath("");
				
		File directorio = new File(institucion.getPath());
		
		
		//se verifica que el directorio exista, sea directorio y sea absoluto
		if(!directorio.isDirectory()||!directorio.exists()||!directorio.isAbsolute())
		{
			logger.error("PATH inválido: "+institucion.getPath());
			resultados.put("error","error.forecat.rutaInvalida");
			return resultados;
		}
		
		//se añade nuevo directorio para el convenio
		dirConvenio=new File(directorio,"RIPS"+separador+codigoInstitucion+separador+"C"+this.convenio+separador+"FORECAT");
		
		
		if(!dirConvenio.isDirectory()&&!dirConvenio.exists())
		{
			if(!dirConvenio.mkdirs())
			{
				logger.error("Error creando directorio en generarFORECAT: "+dirConvenio.getAbsolutePath());				
				resultados.put("error","error.rips.directorioConvenio");
				return resultados;
			}
			
		}
		this.pathGeneracion=dirConvenio.getAbsolutePath();
		
		//****CÁLCULO DE 1/4 DEL SALARIO MINIMO VIGENTE**************************
		this.salarioMinimo = UtilidadesFacturacion.obtenerValorSalarioMinimoVigente(con)/4;
		logger.info("EL 1/4 DEL SALARIO MÍNIMO ES=> "+salarioMinimo);
		//*****GENERACIÓN DE LOS ARCHIVOS*******************************
		
		//generación del archivo AA
		resultados.put(ConstantesBD.forecatAA,this.generarAA(con));
		
		//generacion del archivo VH
		resultados.put(ConstantesBD.forecatVH,this.generarVH(con));
			
		//generacion del archivo AV
		resultados.put(ConstantesBD.forecatAV,this.generarAV(con));
		
		logger.info("RESULTADOS=> "+resultados);
		logger.info("NUMERO CREGISTROS CREADOS=> "+numeroRegistrosCreados);
		
		huboRegistros = verificarArchivos();
		
		if(huboRegistros)
		{
		
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
			
		    
			//generación del archivo AC
			resultados.put(ConstantesBD.forecatAC,this.generarAC());
		}
		else
		{
			this.huboInconsistencias = false;
		}
		
		return resultados;
	}
	
	/**
	 * Método implementado para generar el archivo AA
	 * @param con
	 * @return
	 */
	private String generarAA(Connection con) 
	{
		HashMap listadoAA;
		String registroAA="";
		int numeroRegistros; //número de registros cosnultado desde la BD
		int contadorRegistros=0; //cuenta del número de registros insertados en el fichero
		
		String aux="";//variable auxiliar para la revisión de los caracteres especiales en los campos de texto
		
		boolean desplazado = false; //variable temporal para saber si el evento es desplazado
		String tipoEvento = ""; //variable temporal para saber el tipo de evento (Accidente Transito o Evento Catastorfico)
		
		//vector de boolean para el manejo de cada una de las inconsistencias generales-----------------
		// código de la entidad administradora
		boolean presenciaInconsistenciaGeneral=false;
		//Se realiza la consulta de la informacion del archivo AA
		listadoAA = this.consultaAA(con);
		
		
		numeroRegistros=Integer.parseInt(listadoAA.get("numRegistros")+"");
		
		try
		{
			//apertura de archivo AA
			aux=UtilidadTexto.revisarCaracteresEspeciales(this.numeroRemision);
			File archivoAA=new File(dirConvenio.getAbsolutePath(),ConstantesBD.forecatAA+aux+".txt");
			FileWriter streamAA=new FileWriter(archivoAA,false); //se coloca false para el caso de que esté repetido
			BufferedWriter bufferAA=new BufferedWriter(streamAA);
			
			for(int i=0;i<numeroRegistros;i++)
			{
				//se toman variables 
				desplazado = UtilidadTexto.getBoolean(listadoAA.get("desplazado_"+i).toString());
				tipoEvento = listadoAA.get("tipoEvento_"+i).toString();
				
				//edicion de registro
				registroAA="";
				//CAMPO 1 **********CÓDIGO PRESTADOR DE SERVICIOS***************************
				if(!presenciaInconsistenciaGeneral)
				{
					if(institucion.getCodMinSalud().trim().equals(""))
					{
						aux="campo sin información (para todas las facturas)";
						
						this.editarInconsistencia(listadoAA,
								ConstantesBD.forecatAA,i,
								"código del prestador de servicios",
								aux);
						this.huboInconsistencias=true;
						presenciaInconsistenciaGeneral=true;
					}
					else
					{
						aux = UtilidadTexto.revisarCaracteresEspeciales(institucion.getCodMinSalud().toUpperCase());
						if(aux.trim().length()>12)
							aux = aux.substring(0,12);
						
						registroAA+=aux.trim();
					}
				}
					
				//CAMPO 2 **********TIPO DE IDENTIFICACION DE LA VÍCTIMA**************
				if((listadoAA.get("tipoIdentificacion_"+i)+"").trim().equals(""))
				{
					this.editarInconsistencia(listadoAA,
							ConstantesBD.forecatAA,i,
							"tipo identificacion de la víctima",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAA+=",";
				}
				else
				{
					String tipoId=(listadoAA.get("tipoIdentificacion_"+i)+"").trim();
					if(!tipoId.equals("CC")&&!tipoId.equals("CE")&&!tipoId.equals("PA")&&!tipoId.equals("RC")&&
						!tipoId.equals("TI")&&!tipoId.equals("AS")&&!tipoId.equals("MS")&&!tipoId.equals("NU"))
					{
						this.editarInconsistencia(listadoAA,
								ConstantesBD.forecatAA,i,
								"tipo identificacion víctima",
								"campo con datos inválidos");
						this.huboInconsistencias=true;
					}
					registroAA+=","+tipoId.toUpperCase();
					
				}
				
				//CAMPO 3 **********NÚMERO DE IDENTIFICACIÓN DE LA VÍCTIMA************		
				if((listadoAA.get("numeroIdentificacion_"+i)+"").trim().equals(""))
				{
					this.editarInconsistencia(listadoAA,
							ConstantesBD.forecatAA,i,
							"número identificacion víctima",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAA+=",";
				}
				else
				{
					aux=UtilidadTexto.revisarCaracteresEspeciales(listadoAA.get("numeroIdentificacion_"+i)+"");
					
					if(aux.trim().length()>20)
						aux = aux.substring(0,20);
					
					registroAA+=","+aux.trim().toUpperCase();
				}
				
				//CAMPO 4 **********PRIMER APELLIDO DE LA VÍCTIMA************		
				if((listadoAA.get("primerApellido_"+i)+"").trim().equals(""))
				{
					this.editarInconsistencia(listadoAA,
							ConstantesBD.forecatAA,i,
							"primer apellido víctima",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAA+=",";
				}
				else
				{
					aux=UtilidadTexto.revisarCaracteresEspeciales(listadoAA.get("primerApellido_"+i)+"");
					if(aux.trim().length()>30)
						aux = aux.substring(0,30);
					registroAA+=","+aux.trim().toUpperCase();
				}
				
				//CAMPO 5********SEGUNDO APELLIDO DE LA VÍCTIMA********************************
				aux=UtilidadTexto.revisarCaracteresEspeciales(listadoAA.get("segundoApellido_"+i)+"");
				if(aux.trim().length()>30)
					aux = aux.substring(0,30);		
				registroAA+=","+aux.trim().toUpperCase();
				
				//CAMPO 6 **********PRIMER NOMBRE DE LA VÍCTIMA************		
				if((listadoAA.get("primerNombre_"+i)+"").trim().equals(""))
				{
					this.editarInconsistencia(listadoAA,
							ConstantesBD.forecatAA,i,
							"primer nombre víctima",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAA+=",";
				}
				else
				{
					aux=UtilidadTexto.revisarCaracteresEspeciales(listadoAA.get("primerNombre_"+i)+"");
					if(aux.trim().length()>20)
						aux = aux.substring(0,20);
					registroAA+=","+aux.trim().toUpperCase();
				}
				
				//CAMPO 7********SEGUNDO NOMBRE DE LA VÍCTIMA********************************
				aux=UtilidadTexto.revisarCaracteresEspeciales(listadoAA.get("segundoNombre_"+i)+"");
				if(aux.trim().length()>20)
					aux = aux.substring(0,20);		
				registroAA+=","+aux.trim().toUpperCase();
				
				//CAMPO 8 Y 9*******EDAD Y UNIDAD DE MEDIDA DE LA EDAD***************************		
				InfoDatos edad=UtilidadFecha.calcularEdadFormato((listadoAA.get("fechaNacimiento_"+i)+""),(listadoAA.get("fechaApertura_"+i)+""));
				
				if(edad.getCodigo()<0)
				{
					this.editarInconsistencia(listadoAA,
							ConstantesBD.forecatAA,i,
							"edad y unidad de medida de edad",
							"fecha de Nacimiento mayor a la fecha de atención");
					this.huboInconsistencias=true;
					registroAA+=",,";
				}
				else
				{
					//edicion de la edad
					registroAA+=","+edad.getCodigo();
					//edición del formato de la edad 1=Años, 2=Meses, 3=Días
					registroAA+=","+edad.getValue();
				}
				
				//CAMPO 10***************SEXO****************************************************
				if(listadoAA.get("sexo_"+i).toString().equals(""))
				{
					this.editarInconsistencia(listadoAA,
							ConstantesBD.forecatAA,i,
							"sexo",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAA+=",";
				}
				else
				{
					
					if(listadoAA.get("sexo_"+i).toString().equals(ConstantesBD.codigoSexoFemenino+""))
						registroAA+=",F";
					else if(listadoAA.get("sexo_"+i).toString().equals(ConstantesBD.codigoSexoMasculino+""))
						registroAA+=",M";
					else
					{
						this.editarInconsistencia(listadoAA,
								ConstantesBD.forecatAA,i,
								"sexo",
								"campo con datos inválidos");
						this.huboInconsistencias=true;
						registroAA+=","+listadoAA.get("sexo_"+i).toString();
					}
				}
				
				//CAMPO 11********DIRECCIÓN DE RESIDENCIA DE LA VÍCTIMA********************************
				aux=UtilidadTexto.revisarCaracteresEspeciales(listadoAA.get("direccionResidencia_"+i)+"");
				if(aux.trim().length()>40)
					aux = aux.substring(0,40);		
				registroAA+=","+aux.trim().toUpperCase();
				
				//CAMPO 12 **********CÓDIGO DEL DEPARTAMENTO DE RESIDENCIA VÍCTIMA**************
				if((listadoAA.get("codigoDeptoResidencia_"+i)+"").equals(""))
				{
					this.editarInconsistencia(listadoAA,
							ConstantesBD.forecatAA,i,
							"código departamento residencia",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAA+=",";
				}
				else
				{
					String deptoVivienda=(listadoAA.get("codigoDeptoResidencia_"+i)+"").trim();
					//se valida que el codigo del departamento de vivienda sea positivo
					if(Integer.parseInt(deptoVivienda)>0)
					{
						if(deptoVivienda.length()>2)
							deptoVivienda = deptoVivienda.substring(0,2);
						
						registroAA+=","+deptoVivienda.trim().toUpperCase();
						
					}
					else
					{
						this.editarInconsistencia(listadoAA,
								ConstantesBD.forecatAA,i,
								"código departamento residencia",
								"campo con datos inválidos");
						this.huboInconsistencias=true;
						registroAA+=","+deptoVivienda.trim().toUpperCase();
					}
				}
					
				//CAMPO 13 ***********CÓDIGO DEL MUNICIPIO DE RESIDENCIA VÍCTIMA***************
				if((listadoAA.get("codigoMuniResidencia_"+i)+"").equals(""))
				{
					this.editarInconsistencia(listadoAA,
							ConstantesBD.forecatAA,i,
							"código ciudad residencia",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAA+=",";
				}
				else
				{
					String ciudadVivienda=(listadoAA.get("codigoMuniResidencia_"+i)+"").trim();
					//se verifica que el código sea positivo
					if(Integer.parseInt(ciudadVivienda)>0)
					{
						if(ciudadVivienda.length()>3)
							ciudadVivienda = ciudadVivienda.substring(0,3);
						
						registroAA+=","+ciudadVivienda.trim().toUpperCase();
						
					}
					else
					{
						this.editarInconsistencia(listadoAA,
								ConstantesBD.forecatAA,i,
								"código ciudad residencia",
								"campo con datos inválidos");
						this.huboInconsistencias=true;
						registroAA+=","+ciudadVivienda.trim().toUpperCase();
					}
				}
				
				//CAMPO 14********TELÉFONO DE RESIDENCIA DE LA VÍCTIMA********************************
				aux=UtilidadTexto.revisarCaracteresEspeciales(listadoAA.get("telefonoResidencia_"+i)+"");
				if(aux.trim().length()>9)
					aux = aux.substring(0,9);		
				registroAA+=","+aux.trim().toUpperCase();
				
				//CAMPO 15*********NATURALEZA DEL EVENTO****************************************************
				aux = listadoAA.get("naturalezaEvento_"+i).toString();
				if(aux.equals(""))
				{
					this.editarInconsistencia(listadoAA,
							ConstantesBD.forecatAA,i,
							"naturaleza del evento",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAA+=",";
				}
				else
				{
					if(Integer.parseInt(aux)<1||Integer.parseInt(aux)>14)
					{
						this.editarInconsistencia(listadoAA,
								ConstantesBD.forecatAA,i,
								"naturaleza del evento",
								"campo con datos inválidos");
						this.huboInconsistencias=true;
					}
					registroAA+=","+aux;
				}
				
				//CAMPO 16 **********DIRECCIÓN DE OCURRENCIA DEL EVENTO************		
				if((listadoAA.get("direccionEvento_"+i)+"").trim().equals("")&&!desplazado)
				{
					this.editarInconsistencia(listadoAA,
							ConstantesBD.forecatAA,i,
							"dirección ocurrencia evento",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAA+=",";
				}
				else
				{
					aux=UtilidadTexto.revisarCaracteresEspeciales(listadoAA.get("direccionEvento_"+i)+"");
					if(aux.trim().length()>60)
						aux = aux.substring(0,60);
					registroAA+=","+aux.trim().toUpperCase();
				}
				
				//CAMPO 17 **********FECHA DE OCURRENCIA DEL EVENTO************
				aux = (listadoAA.get("fechaEvento_"+i)+"").trim();
				if(aux.equals("")&&!desplazado)
				{
					this.editarInconsistencia(listadoAA,
							ConstantesBD.forecatAA,i,
							"fecha ocurrencia evento",
							"campo sin información");
					this.huboInconsistencias=true;
				}
				registroAA+=","+aux.trim().toUpperCase();
				
				//CAMPO 18 **********HORA DE OCURRENCIA DEL EVENTO************		
				if((listadoAA.get("horaEvento_"+i)+"").trim().equals("")&&tipoEvento.equals(ConstantesIntegridadDominio.acronimoAccidenteTransito))
				{
					this.editarInconsistencia(listadoAA,
							ConstantesBD.forecatAA,i,
							"hora ocurrencia evento",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAA+=",";
				}
				else
				{
					aux=listadoAA.get("horaEvento_"+i).toString().trim();
					if(!aux.equals("")&&!UtilidadFecha.validacionHora(aux).puedoSeguir)
					{
						this.editarInconsistencia(listadoAA,
								ConstantesBD.forecatAA,i,
								"hora ocurrencia evento",
								"campo con datos inválidos");
						this.huboInconsistencias=true;
					}
					if(aux.trim().length()>5)
						aux = aux.substring(0,5);
					registroAA+=","+aux.trim().toUpperCase();
				}
				
				//CAMPO 19 **********CÓDIGO DEL DEPARTAMENTO DE EVENTO**************
				if((listadoAA.get("codigoDeptoEvento_"+i)+"").equals("")&&!desplazado)
				{
					this.editarInconsistencia(listadoAA,
							ConstantesBD.forecatAA,i,
							"código departamento evento",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAA+=",";
				}
				else
				{
					String deptoEvento="";
					
					//Se verifica que no sea desplazado
					if(!desplazado)
					{
						deptoEvento = (listadoAA.get("codigoDeptoEvento_"+i)+"").trim();
						//se valida que el codigo del departamento de evento sea positivo
						if(Integer.parseInt(deptoEvento)>0)
						{
							if(deptoEvento.length()>2)
								deptoEvento = deptoEvento.substring(0,2);
						}
						else
						{
							this.editarInconsistencia(listadoAA,
									ConstantesBD.forecatAA,i,
									"código departamento evento",
									"campo con datos inválidos");
							this.huboInconsistencias=true;
						}
					}
					registroAA+=","+deptoEvento.trim().toUpperCase();
				}
					
				//CAMPO 20 ***********CÓDIGO DEL MUNICIPIO DE EVENTO***************
				if((listadoAA.get("codigoMuniEvento_"+i)+"").equals("")&&!desplazado)
				{
					this.editarInconsistencia(listadoAA,
							ConstantesBD.forecatAA,i,
							"código ciudad evento",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAA+=",";
				}
				else
				{
					String ciudadEvento="";
					
					//Se verifica que no sea desplazado
					if(!desplazado)
					{
						ciudadEvento = (listadoAA.get("codigoMuniEvento_"+i)+"").trim();
						//se verifica que el código sea positivo
						if(Integer.parseInt(ciudadEvento)>0)
						{
							if(ciudadEvento.length()>3)
								ciudadEvento = ciudadEvento.substring(0,3);
						}
						else
						{
							this.editarInconsistencia(listadoAA,
									ConstantesBD.forecatAA,i,
									"código ciudad evento",
									"campo con datos inválidos");
							this.huboInconsistencias=true;
						}
					}
					registroAA+=","+ciudadEvento.trim().toUpperCase();
				}
				
				//CAMPO 21 *********ZONA ****************
				if(listadoAA.get("zona_"+i).toString().equals("")&&!desplazado)
				{
					this.editarInconsistencia(listadoAA,
							ConstantesBD.forecatAA,i,
							"zona",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAA+=",";
				}
				else
				{
					String zona=listadoAA.get("zona_"+i).toString().trim();
					if(!zona.equals("U")&&!zona.equals("R")&&!desplazado)
					{
						this.editarInconsistencia(listadoAA,
								ConstantesBD.forecatAA,i,
								"zona",
								"campo con datos inválidos");
						this.huboInconsistencias=true;
					}
					if(zona.length()>1)
						zona = zona.substring(0, 1);
					registroAA+=","+zona.trim().toUpperCase();
				}
				
				//CAMPO 22 *********INFORME EVENTO ****************
				if(listadoAA.get("informeEvento_"+i).toString().equals("")&&tipoEvento.equals(ConstantesIntegridadDominio.acronimoAccidenteTransito))
				{
					this.editarInconsistencia(listadoAA,
							ConstantesBD.forecatAA,i,
							"informe del evento",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAA+=",";
				}
				else
				{
					aux=UtilidadTexto.revisarCaracteresEspeciales(listadoAA.get("informeEvento_"+i)+"");
					if(aux.trim().length()>255)
						aux = aux.trim().substring(0,255);
					registroAA+=","+aux.trim().toUpperCase();
				}
					
				//*************FIN**************************************
				
				registroAA+="\n";
				bufferAA.write(registroAA);
				contadorRegistros++;
				
			} //fin for
			
			//se almacena el número de registros creados para el archivo AA
			this.numeroRegistrosCreados.put(ConstantesBD.forecatAA,contadorRegistros+"");
			
			bufferAA.close();
			if(contadorRegistros<1)
			{
				//archivo sin registros
				archivoAA.delete();
				return null;
			}
			
			// verificar si es una creacion automatica de forecats desde la consulta de rips
			if (this.esRipsForecatAuto)
				// si lo es cambiar ruta de acceso a el
				// pero primero verificar para que tipo de archivo se iso, si para cunetas cobro o para facturas
				if (this.esCuentaCobro)
					return "../ripsCuentaCobro/ripsCuentaCobro.do?estado=detalle&archivo=AA";
				else
					return "../ripsFactura/ripsFactura.do?estado=detalle&archivo=AA";
			else
				return "../generacionAnexosForecat/generacionAnexosForecat.do?estado=detalle&archivo=AA";
			
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo AA al generarlo: "+e);
			this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo AA (Forecat): "+e));
			return null;
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo AA: "+e);
			this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo AA (Forecat): "+e));
			return null;
		}
	}
	
	
	/**
	 * Método implementado para generar el archivo VH
	 * @param con
	 * @return
	 */
	private String generarVH(Connection con) 
	{
		HashMap listadoVH;
		String registroVH="";
		int numeroRegistros; //número de registros cosnultado desde la BD
		int contadorRegistros=0; //cuenta del número de registros insertados en el fichero
		
		String aux="";//variable auxiliar para la revisión de los caracteres especiales en los campos de texto
		
		String estadoAseguramiento = ""; //variable temporal para almacenar el estado de aseguramiento
		
		//vector de boolean para el manejo de cada una de las inconsistencias generales-----------------
		// código de la entidad administradora
		boolean presenciaInconsistenciaGeneral=false;
		//Se realiza la consulta de la informacion del archivo VH
		listadoVH = this.consultaVH(con);
		
		
		numeroRegistros=Integer.parseInt(listadoVH.get("numRegistros")+"");
		
		try
		{
			//apertura de archivo VH
			aux=UtilidadTexto.revisarCaracteresEspeciales(this.numeroRemision);
			File archivoVH=new File(dirConvenio.getAbsolutePath(),ConstantesBD.forecatVH+aux+".txt");
			FileWriter streamVH=new FileWriter(archivoVH,false); //se coloca false para el caso de que esté repetido
			BufferedWriter bufferVH=new BufferedWriter(streamVH);
			
			for(int i=0;i<numeroRegistros;i++)
			{
				//se toman variables 
				estadoAseguramiento = listadoVH.get("estadoAseguramiento_"+i).toString();
				
				//edicion de registro
				registroVH="";
				//CAMPO 1 **********CÓDIGO PRESTADOR DE SERVICIOS***************************
				if(!presenciaInconsistenciaGeneral)
				{
					if(institucion.getCodMinSalud().trim().equals(""))
					{
						aux="campo sin información (para todas las facturas)";
						
						this.editarInconsistencia(listadoVH,
								ConstantesBD.forecatVH,i,
								"código del prestador de servicios",
								aux);
						this.huboInconsistencias=true;
						presenciaInconsistenciaGeneral=true;
					}
					else
					{
						aux = UtilidadTexto.revisarCaracteresEspeciales(institucion.getCodMinSalud());
						if(aux.trim().length()>12)
							aux = aux.substring(0,12);
						
						registroVH+=aux.trim().toUpperCase();
					}
				}
					
				//CAMPO 2 **********TIPO DE IDENTIFICACION DE LA VÍCTIMA**************
				if((listadoVH.get("tipoIdentificacion_"+i)+"").trim().equals(""))
				{
					this.editarInconsistencia(listadoVH,
							ConstantesBD.forecatVH,i,
							"tipo identificacion de la víctima",
							"campo sin información");
					this.huboInconsistencias=true;
					registroVH+=",";
				}
				else
				{
					String tipoId=(listadoVH.get("tipoIdentificacion_"+i)+"").trim();
					if(!tipoId.equals("CC")&&!tipoId.equals("CE")&&!tipoId.equals("PA")&&!tipoId.equals("RC")&&
						!tipoId.equals("TI")&&!tipoId.equals("AS")&&!tipoId.equals("MS")&&!tipoId.equals("NU"))
					{
						this.editarInconsistencia(listadoVH,
								ConstantesBD.forecatVH,i,
								"tipo identificacion víctima",
								"campo con datos inválidos");
						this.huboInconsistencias=true;
					}
					registroVH+=","+tipoId.toUpperCase();
					
				}
				
				//CAMPO 3 **********NÚMERO DE IDENTIFICACIÓN DE LA VÍCTIMA************		
				if((listadoVH.get("numeroIdentificacion_"+i)+"").trim().equals(""))
				{
					this.editarInconsistencia(listadoVH,
							ConstantesBD.forecatVH,i,
							"número identificacion víctima",
							"campo sin información");
					this.huboInconsistencias=true;
					registroVH+=",";
				}
				else
				{
					aux=UtilidadTexto.revisarCaracteresEspeciales(listadoVH.get("numeroIdentificacion_"+i)+"");
					
					if(aux.trim().length()>20)
						aux = aux.substring(0,20);
					
					registroVH+=","+aux.trim().toUpperCase();
				}
				
				//CAMPO 4 **********ESTADO DE ASEGURAMIENTO DEL VEHÍCULO************		
				if(estadoAseguramiento.trim().equals(""))
				{
					this.editarInconsistencia(listadoVH,
							ConstantesBD.forecatVH,i,
							"estado aseguramiento",
							"campo sin información");
					this.huboInconsistencias=true;
					registroVH+=",";
				}
				else
				{
					if(Integer.parseInt(estadoAseguramiento)<1||Integer.parseInt(estadoAseguramiento)>5)
					{
						this.editarInconsistencia(listadoVH,
								ConstantesBD.forecatVH,i,
								"estado aseguramiento",
								"campo con datos inválidos");
						this.huboInconsistencias=true;
					}
					
					registroVH+=","+estadoAseguramiento.trim().toUpperCase();
				}
				
				//CAMPO 5 **********MARCA***************************************************************		
				//Solo es obligatorio para los estados de aseguramiento 1,2,4,5
				if(listadoVH.get("marca_"+i).toString().trim().equals("")
					&&(estadoAseguramiento.equals("1")||estadoAseguramiento.equals("2")||estadoAseguramiento.equals("4")||estadoAseguramiento.equals("5")))
				{
					this.editarInconsistencia(listadoVH,
							ConstantesBD.forecatVH,i,
							"marca",
							"campo sin información");
					this.huboInconsistencias=true;
					registroVH+=",";
				}
				else
				{
					aux = UtilidadTexto.revisarCaracteresEspeciales(listadoVH.get("marca_"+i).toString().trim());
					if(aux.length()>15)
						aux = aux.substring(0,15);
					registroVH+=","+aux.toUpperCase();
				}
				
				//CAMPO 6 **********PLACA***************************************************************		
				//Solo es obligatorio para los estados de aseguramiento 1,2,4,5
				if(listadoVH.get("placa_"+i).toString().trim().equals("")
					&&(estadoAseguramiento.equals("1")||estadoAseguramiento.equals("2")||estadoAseguramiento.equals("4")||estadoAseguramiento.equals("5")))
				{
					this.editarInconsistencia(listadoVH,
							ConstantesBD.forecatVH,i,
							"placa",
							"campo sin información");
					this.huboInconsistencias=true;
					registroVH+=",";
				}
				else
				{
					aux = UtilidadTexto.revisarCaracteresEspeciales(listadoVH.get("placa_"+i).toString().trim());
					if(aux.length()>6)
						aux = aux.substring(0,6);
					registroVH+=","+aux.toUpperCase();
				}
				
				//CAMPO 7 **********CLASE***************************************************************		
				//Solo es obligatorio para los estados de aseguramiento 1,2,4,5
				if(listadoVH.get("clase_"+i).toString().trim().equals("")
					&&(estadoAseguramiento.equals("1")||estadoAseguramiento.equals("2")||estadoAseguramiento.equals("4")||estadoAseguramiento.equals("5")))
				{
					this.editarInconsistencia(listadoVH,
							ConstantesBD.forecatVH,i,
							"clase",
							"campo sin información");
					this.huboInconsistencias=true;
					registroVH+=",";
				}
				else
				{
					aux = UtilidadTexto.revisarCaracteresEspeciales(listadoVH.get("clase_"+i).toString().trim());
					if(aux.length()>2)
						aux = aux.substring(0,2);
					registroVH+=","+aux.toUpperCase();
				}
				
				//CAMPO 8 **********NOMBRE DE LA ASEGURADORA***************************************************************		
				//Solo es obligatorio para los estados de aseguramiento 1,4,5
				if(listadoVH.get("nombreAseguradora_"+i).toString().trim().equals("")
					&&(estadoAseguramiento.equals("1")||estadoAseguramiento.equals("4")||estadoAseguramiento.equals("5")))
				{
					this.editarInconsistencia(listadoVH,
							ConstantesBD.forecatVH,i,
							"nombre de la aseguradora",
							"campo sin información");
					this.huboInconsistencias=true;
					registroVH+=",";
				}
				else
				{
					aux = UtilidadTexto.revisarCaracteresEspeciales(listadoVH.get("nombreAseguradora_"+i).toString().trim());
					if(aux.length()>40)
						aux = aux.substring(0,40);
					registroVH+=","+aux.toUpperCase();
				}
				
				//CAMPO 9 **********POLIZA SOAT NUMERO***************************************************************		
				//Solo es obligatorio para los estados de aseguramiento 1,4,5
				if(listadoVH.get("polizaSoat_"+i).toString().trim().equals("")
					&&(estadoAseguramiento.equals("1")||estadoAseguramiento.equals("4")||estadoAseguramiento.equals("5")))
				{
					this.editarInconsistencia(listadoVH,
							ConstantesBD.forecatVH,i,
							"número póliza SOAT",
							"campo sin información");
					this.huboInconsistencias=true;
					registroVH+=",";
				}
				else
				{
					aux = UtilidadTexto.revisarCaracteresEspeciales(listadoVH.get("polizaSoat_"+i).toString().trim());
					if(aux.length()>20)
						aux = aux.substring(0,20);
					registroVH+=","+aux.toUpperCase();
				}
				
				//CAMPO 10 **********FECHA INICIO VIGENCIA POLIZA***************************************************************		
				//Solo es obligatorio para los estados de aseguramiento 1,4,5
				if(listadoVH.get("fechaInicioVigencia_"+i).toString().trim().equals("")
					&&(estadoAseguramiento.equals("1")||estadoAseguramiento.equals("4")||estadoAseguramiento.equals("5")))
				{
					this.editarInconsistencia(listadoVH,
							ConstantesBD.forecatVH,i,
							"fecha inicio vigencia póliza",
							"campo sin información");
					this.huboInconsistencias=true;
					registroVH+=",";
				}
				else
				{
					aux = listadoVH.get("fechaInicioVigencia_"+i).toString().trim();
					if(aux.length()>10)
						aux = aux.substring(0,10);
					registroVH+=","+aux.toUpperCase();
				}
				
				//CAMPO 11 **********FECHA FIN VIGENCIA POLIZA***************************************************************		
				//Solo es obligatorio para los estados de aseguramiento 1,4,5
				if(listadoVH.get("fechaFinVigencia_"+i).toString().trim().equals("")
					&&(estadoAseguramiento.equals("1")||estadoAseguramiento.equals("4")||estadoAseguramiento.equals("5")))
				{
					this.editarInconsistencia(listadoVH,
							ConstantesBD.forecatVH,i,
							"fecha fin vigencia póliza",
							"campo sin información");
					this.huboInconsistencias=true;
					registroVH+=",";
				}
				else
				{
					aux = listadoVH.get("fechaFinVigencia_"+i).toString().trim();
					if(aux.length()>10)
						aux = aux.substring(0,10);
					registroVH+=","+aux.toUpperCase();
				}
				
				//CAMPO 12 **********PRIMER APELLIDO PROPIETARIO***************************************************************		
				//Solo es obligatorio para los estados de aseguramiento 1,2,4,5
				if(listadoVH.get("primerApellidoProp_"+i).toString().trim().equals("")
					&&(estadoAseguramiento.equals("1")||estadoAseguramiento.equals("2")||estadoAseguramiento.equals("4")||estadoAseguramiento.equals("5")))
				{
					this.editarInconsistencia(listadoVH,
							ConstantesBD.forecatVH,i,
							"primer apellido propietario",
							"campo sin información");
					this.huboInconsistencias=true;
					registroVH+=",";
				}
				else
				{
					aux = UtilidadTexto.revisarCaracteresEspeciales(listadoVH.get("primerApellidoProp_"+i).toString().trim());
					if(aux.length()>30)
						aux = aux.substring(0,30);
					registroVH+=","+aux.toUpperCase();
				}
				
				//CAMPO 13********SEGUNDO APELLIDO PROPIETARIO********************************
				aux=UtilidadTexto.revisarCaracteresEspeciales(listadoVH.get("segundoApellidoProp_"+i)+"");
				if(aux.trim().length()>30)
					aux = aux.trim().substring(0,30);		
				registroVH+=","+aux.trim().toUpperCase();
				
				//CAMPO 14 **********PRIMER NOMBRE PROPIETARIO***************************************************************		
				//Solo es obligatorio para los estados de aseguramiento 1,2,4,5
				if(listadoVH.get("primerNombreProp_"+i).toString().trim().equals("")
					&&(estadoAseguramiento.equals("1")||estadoAseguramiento.equals("2")||estadoAseguramiento.equals("4")||estadoAseguramiento.equals("5")))
				{
					this.editarInconsistencia(listadoVH,
							ConstantesBD.forecatVH,i,
							"primer nombre propietario",
							"campo sin información");
					this.huboInconsistencias=true;
					registroVH+=",";
				}
				else
				{
					aux = UtilidadTexto.revisarCaracteresEspeciales(listadoVH.get("primerNombreProp_"+i).toString().trim());
					if(aux.length()>20)
						aux = aux.substring(0,20);
					registroVH+=","+aux.toUpperCase();
				}
				
				//CAMPO 15********SEGUNDO NOMBRE PROPIETARIO********************************
				aux=UtilidadTexto.revisarCaracteresEspeciales(listadoVH.get("segundoNombreProp_"+i)+"");
				if(aux.trim().length()>20)
					aux = aux.trim().substring(0,20);		
				registroVH+=","+aux.trim().toUpperCase();
				
				//CAMPO 16 **********TIPO DE IDENTIFICACION DEL PROPIETARIO**************
				//Solo es obligatorio para los estados de aseguramiento 1,2,4,5
				if(listadoVH.get("tipoIdentificacionProp_"+i).toString().trim().equals("")
					&&(estadoAseguramiento.equals("1")||estadoAseguramiento.equals("2")||estadoAseguramiento.equals("4")||estadoAseguramiento.equals("5")))
				{
					this.editarInconsistencia(listadoVH,
							ConstantesBD.forecatVH,i,
							"tipo identificacion del propietario",
							"campo sin información");
					this.huboInconsistencias=true;
					registroVH+=",";
				}
				else
				{
					String tipoId=listadoVH.get("tipoIdentificacionProp_"+i).toString().trim();
					if(!tipoId.equals("CC")&&!tipoId.equals("CE")&&!tipoId.equals("PA")&&!tipoId.equals("RC")&&
						!tipoId.equals("TI")&&!tipoId.equals("AS")&&!tipoId.equals("MS")&&!tipoId.equals("NU")&&!tipoId.equals(""))
					{
						this.editarInconsistencia(listadoVH,
								ConstantesBD.forecatVH,i,
								"tipo identificacion del propietario",
								"campo con datos inválidos");
						this.huboInconsistencias=true;
					}
					registroVH+=","+tipoId.toUpperCase();
				}
				
				//CAMPO 17 **********NÚMERO DE IDENTIFICACIÓN DEL PROPIETARIO************
				//Solo es obligatorio para los estados de aseguramiento 1,2,4,5
				if(listadoVH.get("numeroIdentificacionProp_"+i).toString().trim().equals("")
					&&(estadoAseguramiento.equals("1")||estadoAseguramiento.equals("2")||estadoAseguramiento.equals("4")||estadoAseguramiento.equals("5")))
				{
					this.editarInconsistencia(listadoVH,
							ConstantesBD.forecatVH,i,
							"número identificacion propietario",
							"campo sin información");
					this.huboInconsistencias=true;
					registroVH+=",";
				}
				else
				{
					aux=UtilidadTexto.revisarCaracteresEspeciales(listadoVH.get("numeroIdentificacionProp_"+i)+"");
					
					if(aux.trim().length()>20)
						aux = aux.substring(0,20);
					
					registroVH+=","+aux.trim().toUpperCase();
				}
				
				//CAMPO 18 **********CÓDIGO DEL DEPARTAMENTO DE RESIDENCIA PROPIETARIO**************
				//Solo es obligatorio para los estados de aseguramiento 1,2,4,5
				if((listadoVH.get("codigoDeptoProp_"+i)+"").equals("")
					&&(estadoAseguramiento.equals("1")||estadoAseguramiento.equals("2")||estadoAseguramiento.equals("4")||estadoAseguramiento.equals("5")))
				{
					this.editarInconsistencia(listadoVH,
							ConstantesBD.forecatVH,i,
							"código departamento propietario",
							"campo sin información");
					this.huboInconsistencias=true;
					registroVH+=",";
				}
				else
				{
					String deptoProp=(listadoVH.get("codigoDeptoProp_"+i)+"").trim();
					//se valida que el codigo del departamento de propietario sea positivo
					if(!deptoProp.equals(""))
					{
						if(Integer.parseInt(deptoProp)>0)
						{
							if(deptoProp.length()>2)
								deptoProp = deptoProp.substring(0,2);
						}
						else
						{
							this.editarInconsistencia(listadoVH,
									ConstantesBD.forecatVH,i,
									"código departamento propietario",
									"campo con datos inválidos");
							this.huboInconsistencias=true;
						}
					}
					registroVH+=","+deptoProp.trim().toUpperCase();
				}
				
				//CAMPO 19 ***********CÓDIGO DEL MUNICIPIO DE RESIDENCIA PROPIETARIO***************
				//Solo es obligatorio para los estados de aseguramiento 1,2,4,5
				if((listadoVH.get("codigoMuniProp_"+i)+"").equals("")
					&&(estadoAseguramiento.equals("1")||estadoAseguramiento.equals("2")||estadoAseguramiento.equals("4")||estadoAseguramiento.equals("5")))
				{
					this.editarInconsistencia(listadoVH,
							ConstantesBD.forecatVH,i,
							"código ciudad propietario",
							"campo sin información");
					this.huboInconsistencias=true;
					registroVH+=",";
				}
				else
				{
					String ciudadProp=(listadoVH.get("codigoMuniProp_"+i)+"").trim();
					if(!ciudadProp.equals(""))
					{
						//se verifica que el código sea positivo
						if(Integer.parseInt(ciudadProp)>0)
						{
							if(ciudadProp.length()>3)
								ciudadProp = ciudadProp.substring(0,3);	
							
						}
						else
						{
							this.editarInconsistencia(listadoVH,
									ConstantesBD.forecatVH,i,
									"código ciudad propietario",
									"campo con datos inválidos");
							this.huboInconsistencias=true;
						}
					}
					registroVH+=","+ciudadProp.trim().toUpperCase();
				}
				
				//CAMPO 20 ***********DIRECCIÓN DEL PROPIETARIO***************
				//Solo es obligatorio para los estados de aseguramiento 1,2,4,5
				if((listadoVH.get("direccionProp_"+i)+"").equals("")
					&&(estadoAseguramiento.equals("1")||estadoAseguramiento.equals("2")||estadoAseguramiento.equals("4")||estadoAseguramiento.equals("5")))
				{
					this.editarInconsistencia(listadoVH,
							ConstantesBD.forecatVH,i,
							"dirección del propietario",
							"campo sin información");
					this.huboInconsistencias=true;
					registroVH+=",";
				}
				else
				{
					aux=UtilidadTexto.revisarCaracteresEspeciales(listadoVH.get("direccionProp_"+i).toString().trim());
					if(aux.length()>40)
						aux = aux.substring(0, 40);
					registroVH+=","+aux.toUpperCase();
				}
				
				//CAMPO 21 ***********TELÉFONO DEL PROPIETARIO***************
				aux=UtilidadTexto.revisarCaracteresEspeciales(listadoVH.get("telefonoProp_"+i)+"");
				if(aux.trim().length()>9)
					aux = aux.substring(0,9);		
				registroVH+=","+aux.trim().toUpperCase();
				
				//CAMPO 22 **********PRIMER APELLIDO CONDUCTOR***************************************************************		
				//Solo es obligatorio para los estados de aseguramiento 1,2,4,5
				if(listadoVH.get("primerApellidoCond_"+i).toString().trim().equals("")
					&&(estadoAseguramiento.equals("1")||estadoAseguramiento.equals("2")||estadoAseguramiento.equals("4")||estadoAseguramiento.equals("5")))
				{
					this.editarInconsistencia(listadoVH,
							ConstantesBD.forecatVH,i,
							"primer apellido conductor",
							"campo sin información");
					this.huboInconsistencias=true;
					registroVH+=",";
				}
				else
				{
					aux = UtilidadTexto.revisarCaracteresEspeciales(listadoVH.get("primerApellidoCond_"+i).toString().trim());
					if(aux.length()>30)
						aux = aux.substring(0,30);
					registroVH+=","+aux.toUpperCase();
				}
				
				//CAMPO 23********SEGUNDO APELLIDO CONDUCTOR********************************
				aux=UtilidadTexto.revisarCaracteresEspeciales(listadoVH.get("segundoApellidoCond_"+i)+"");
				if(aux.trim().length()>30)
					aux = aux.substring(0,30);		
				registroVH+=","+aux.trim().toUpperCase();
				
				//CAMPO 24 **********PRIMER NOMBRE CONDUCTOR***************************************************************		
				//Solo es obligatorio para los estados de aseguramiento 1,2,4,5
				if(listadoVH.get("primerNombreCond_"+i).toString().trim().equals("")
					&&(estadoAseguramiento.equals("1")||estadoAseguramiento.equals("2")||estadoAseguramiento.equals("4")||estadoAseguramiento.equals("5")))
				{
					this.editarInconsistencia(listadoVH,
							ConstantesBD.forecatVH,i,
							"primer nombre conductor",
							"campo sin información");
					this.huboInconsistencias=true;
					registroVH+=",";
				}
				else
				{
					aux = UtilidadTexto.revisarCaracteresEspeciales(listadoVH.get("primerNombreCond_"+i).toString().trim());
					if(aux.length()>20)
						aux = aux.substring(0,20);
					registroVH+=","+aux.toUpperCase();
				}
				
				//CAMPO 25********SEGUNDO NOMBRE CONDUCTOR********************************
				aux=UtilidadTexto.revisarCaracteresEspeciales(listadoVH.get("segundoNombreCond_"+i)+"");
				if(aux.trim().length()>20)
					aux = aux.substring(0,20);		
				registroVH+=","+aux.trim().toUpperCase();
				
				//CAMPO 26 **********TIPO DE IDENTIFICACION DEL CONDUCTOR**************
				//Solo es obligatorio para los estados de aseguramiento 1,2,4,5
				if(listadoVH.get("tipoIdentificacionCond_"+i).toString().trim().equals("")
					&&(estadoAseguramiento.equals("1")||estadoAseguramiento.equals("2")||estadoAseguramiento.equals("4")||estadoAseguramiento.equals("5")))
				{
					this.editarInconsistencia(listadoVH,
							ConstantesBD.forecatVH,i,
							"tipo identificacion del conductor",
							"campo sin información");
					this.huboInconsistencias=true;
					registroVH+=",";
				}
				else
				{
					String tipoId=listadoVH.get("tipoIdentificacionCond_"+i).toString().trim();
					if(!tipoId.equals("CC")&&!tipoId.equals("CE")&&!tipoId.equals("PA")&&!tipoId.equals("TI")&&!tipoId.equals(""))
					{
						this.editarInconsistencia(listadoVH,
								ConstantesBD.forecatVH,i,
								"tipo identificacion del conductor",
								"campo con datos inválidos");
						this.huboInconsistencias=true;
					}
					registroVH+=","+tipoId.toUpperCase();
				}
				
				//CAMPO 27 **********NÚMERO DE IDENTIFICACIÓN DEL CONDUCTOR************
				//Solo es obligatorio para los estados de aseguramiento 1,2,4,5
				if(listadoVH.get("numeroIdentificacionCond_"+i).toString().trim().equals("")
					&&(estadoAseguramiento.equals("1")||estadoAseguramiento.equals("2")||estadoAseguramiento.equals("4")||estadoAseguramiento.equals("5")))
				{
					this.editarInconsistencia(listadoVH,
							ConstantesBD.forecatVH,i,
							"número identificacion conductor",
							"campo sin información");
					this.huboInconsistencias=true;
					registroVH+=",";
				}
				else
				{
					aux=UtilidadTexto.revisarCaracteresEspeciales(listadoVH.get("numeroIdentificacionCond_"+i)+"");
					
					if(aux.trim().length()>20)
						aux = aux.substring(0,20);
					
					registroVH+=","+aux.trim().toUpperCase();
				}
				
				//CAMPO 28 **********CÓDIGO DEL DEPARTAMENTO DE RESIDENCIA CONDUCTOR**************
				//Solo es obligatorio para los estados de aseguramiento 1,2,4,5
				if((listadoVH.get("codigoDeptoCond_"+i)+"").equals("")
					&&(estadoAseguramiento.equals("1")||estadoAseguramiento.equals("2")||estadoAseguramiento.equals("4")||estadoAseguramiento.equals("5")))
				{
					this.editarInconsistencia(listadoVH,
							ConstantesBD.forecatVH,i,
							"código departamento conductor",
							"campo sin información");
					this.huboInconsistencias=true;
					registroVH+=",";
				}
				else
				{
					String deptoCond=(listadoVH.get("codigoDeptoCond_"+i)+"").trim();
					//se valida que el codigo del departamento de conductor sea positivo
					if(!deptoCond.equals(""))
					{
						if(Integer.parseInt(deptoCond)>0)
						{
							if(deptoCond.length()>2)
								deptoCond = deptoCond.substring(0,2);
						}
						else
						{
							this.editarInconsistencia(listadoVH,
									ConstantesBD.forecatVH,i,
									"código departamento conductor",
									"campo con datos inválidos");
							this.huboInconsistencias=true;
						}
					}
					registroVH+=","+deptoCond.trim().toUpperCase();
				}
				
				//CAMPO 29 ***********CÓDIGO DEL MUNICIPIO DE RESIDENCIA CONDUCTOR***************
				//Solo es obligatorio para los estados de aseguramiento 1,2,4,5
				if((listadoVH.get("codigoMuniCond_"+i)+"").equals("")
					&&(estadoAseguramiento.equals("1")||estadoAseguramiento.equals("2")||estadoAseguramiento.equals("4")||estadoAseguramiento.equals("5")))
				{
					this.editarInconsistencia(listadoVH,
							ConstantesBD.forecatVH,i,
							"código ciudad conductor",
							"campo sin información");
					this.huboInconsistencias=true;
					registroVH+=",";
				}
				else
				{
					String ciudadCond=(listadoVH.get("codigoMuniCond_"+i)+"").trim();
					if(!ciudadCond.equals(""))
					{
						//se verifica que el código sea positivo
						if(Integer.parseInt(ciudadCond)>0)
						{
							if(ciudadCond.length()>3)
								ciudadCond = ciudadCond.substring(0,3);
							
						}
						else
						{
							this.editarInconsistencia(listadoVH,
									ConstantesBD.forecatVH,i,
									"código ciudad conductor",
									"campo con datos inválidos");
							this.huboInconsistencias=true;
							
						}
					}
					registroVH+=","+ciudadCond.trim().toUpperCase();
				}
				
				//CAMPO 30 ***********DIRECCIÓN DEL CONDUCTOR***************
				//Solo es obligatorio para los estados de aseguramiento 1,2,4,5
				if((listadoVH.get("direccionCond_"+i)+"").equals("")
					&&(estadoAseguramiento.equals("1")||estadoAseguramiento.equals("2")||estadoAseguramiento.equals("4")||estadoAseguramiento.equals("5")))
				{
					this.editarInconsistencia(listadoVH,
							ConstantesBD.forecatVH,i,
							"dirección del conductor",
							"campo sin información");
					this.huboInconsistencias=true;
					registroVH+=",";
				}
				else
				{
					aux=UtilidadTexto.revisarCaracteresEspeciales(listadoVH.get("direccionCond_"+i).toString().trim());
					if(aux.length()>40)
						aux = aux.substring(0, 40);
					registroVH+=","+aux.toUpperCase();
				}
				
				//CAMPO 31 ***********TELÉFONO DEL CONDUCTOR***************
				aux=UtilidadTexto.revisarCaracteresEspeciales(listadoVH.get("telefonoCond_"+i)+"");
				if(aux.trim().length()>9)
					aux = aux.substring(0,9);		
				registroVH+=","+aux.trim().toUpperCase();
				
					
				//*************FIN**************************************
				
				registroVH+="\n";
				bufferVH.write(registroVH);
				contadorRegistros++;
				
			} //fin for
			
			//se almacena el número de registros creados para el archivo VH
			this.numeroRegistrosCreados.put(ConstantesBD.forecatVH,contadorRegistros+"");
			
			bufferVH.close();
			if(contadorRegistros<1)
			{
				//archivo sin registros
				archivoVH.delete();
				return null;
			}
			
			//			 verificar si es una creacion automatica de forecats desde la consulta de rips
			if (this.esRipsForecatAuto)
				//				 si lo es cambiar ruta de acceso a el
				// pero primero verificar para que tipo de archivo se iso, si para cunetas cobro o para facturas
				if (this.esCuentaCobro)
					return "../ripsCuentaCobro/ripsCuentaCobro.do?estado=detalle&archivo=VH";
				else
					return "../ripsFactura/ripsFactura.do?estado=detalle&archivo=VH";
			else
				return "../generacionAnexosForecat/generacionAnexosForecat.do?estado=detalle&archivo=VH";
			
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo VH al generarlo: "+e);
			this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo VH (Forecat): "+e));
			return null;
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo VH: "+e);
			this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo VH (Forecat): "+e));
			return null;
		}
	}
	/**
	 * Método implementado para generar el archivo AV
	 * @param con
	 * @return
	 */
	private String generarAV(Connection con) 
	{
		HashMap listadoAV;
		String registroAV="";
		int numeroRegistros; //número de registros cosnultado desde la BD
		int contadorRegistros=0; //cuenta del número de registros insertados en el fichero
		
		String aux="";//variable auxiliar para la revisión de los caracteres especiales en los campos de texto
		
		String tipoServicio = "";  //variable temporal para saber el tipo de servicio
		String naturaleza = ""; //variable temporal para saber la naturaleza del servicio o del medicamento
		boolean esMedicamento = false; //variable temporal para saber si el cargo de artículo es medicamento o no
		boolean esPos = false; //variable temporal para saber si el cargo de medicamento es pos o no pos
		int categoria = 0; //variable que se usa para detectar la categoria del registro, puede tener los siguientes valores:
		/**
		 * 1: Consulta
		 * 2: Procedimiento
		 * 3: Medicamento
		 * 4: Otros
		 */
		
		//vector de boolean para el manejo de cada una de las inconsistencias generales-----------------
		// código de la entidad administradora
		boolean presenciaInconsistenciaGeneral=false;
		//Se realiza la consulta de la informacion del archivo AV
		listadoAV = this.consultaAV(con);
		
		
		numeroRegistros=Integer.parseInt(listadoAV.get("numRegistros")+"");
		
		try
		{
			//apertura de archivo AV
			aux=UtilidadTexto.revisarCaracteresEspeciales(this.numeroRemision);
			File archivoAV=new File(dirConvenio.getAbsolutePath(),ConstantesBD.forecatAV+aux+".txt");
			FileWriter streamAV=new FileWriter(archivoAV,false); //se coloca false para el caso de que esté repetido
			BufferedWriter bufferAV=new BufferedWriter(streamAV);
			
			for(int i=0;i<numeroRegistros;i++)
			{
				if(listadoAV.get("orden_"+i).toString().equals("1329357"))
				{
					logger.info("**************ORDEN 1329357*************************");
					logger.info("el tipo de servicio=>"+listadoAV.get("tipoServ_"+i));
					logger.info("la naturaleza es=>"+listadoAV.get("naturaleza_"+i));
				}
				
				
				//se toman variables 
				tipoServicio = listadoAV.get("tipoServ_"+i).toString().trim();
				naturaleza = listadoAV.get("naturaleza_"+i).toString().trim();
				esMedicamento = UtilidadTexto.getBoolean(listadoAV.get("esMedicamento_"+i).toString());
				esPos = UtilidadTexto.getBoolean(listadoAV.get("esPos_"+i).toString());
				
				//**************************************************************************
				//******************VALIDACIONES DE TIPO SERVICIO Y NATURALEZA***************
				//***************************************************************************
				//Categoria Consulta
				if(tipoServicio.equals(ConstantesBD.codigoServicioInterconsulta+"")&&
					(naturaleza.equals(ConstantesBD.codigoNaturalezaServicioConsultas+"")||
					naturaleza.equals(ConstantesBD.codigoNaturalezaServicioPromocionPrevencion+""))
				)
					categoria = 1;
				//Categoria Procedimiento
				else if(
					(tipoServicio.equals(ConstantesBD.codigoServicioProcedimiento+"")&&
					(naturaleza.equals(ConstantesBD.codigoNaturalezaServicioDiagnostico+"")||
					naturaleza.equals(ConstantesBD.codigoNaturalezaServicioTerapeuticoNoQx+"")))||
					
					(tipoServicio.equals(ConstantesBD.codigoServicioNoCruentos+"")&&
					(naturaleza.equals(ConstantesBD.codigoNaturalezaServicioDiagnostico+"")||
					naturaleza.equals(ConstantesBD.codigoNaturalezaServicioTerapeuticoNoQx+"")||
					naturaleza.equals(ConstantesBD.codigoNaturalezaServicioTerapeuticoQx+"")))||
					
					(tipoServicio.equals(ConstantesBD.codigoServicioQuirurgico+"")&&
					naturaleza.equals(ConstantesBD.codigoNaturalezaServicioTerapeuticoQx+""))||
					
					(tipoServicio.equals(ConstantesBD.codigoServicioPartosCesarea+"")&&
					(naturaleza.equals(ConstantesBD.codigoNaturalezaServicioTerapeuticoNoQx+"")||
					naturaleza.equals(ConstantesBD.codigoNaturalezaServicioTerapeuticoQx+"")))||
					
					(tipoServicio.equals(ConstantesBD.codigoServicioPaquetes+"")&&
					(naturaleza.equals(ConstantesBD.codigoNaturalezaServicioTerapeuticoQx+"")||
					naturaleza.equals(ConstantesBD.codigoNaturalezaServicioTerapeuticoNoQx+"")))
				)
					categoria = 2;
				//Categoria Medicamento
				else if(tipoServicio.equals("")&&esMedicamento)
					categoria = 3;
				else if(!tipoServicio.equals("")&&
						(naturaleza.equals(ConstantesBD.codigoNaturalezaServicioEstancias+"")||
						naturaleza.equals(ConstantesBD.codigoNaturalezaServicioHonorarios+"")||
						naturaleza.equals(ConstantesBD.codigoNaturalezaServicioTrasladoPaciente+""))||
						
						(tipoServicio.equals("")&&!esMedicamento)
						
				)
					categoria = 4;
				else
					categoria = 0;
				//*************************************************************************
				//**************************************************************************
				
				//Se revisa si el registro aplica alguna categoria, de lo contrario se genera inconsistencia
				if(categoria==0)
				{
					this.editarInconsistencia(listadoAV,
							ConstantesBD.forecatAV,i,
							"naturaleza del "+(tipoServicio.equals("")?"artículo":"servicio"),
							"campo con datos inválidos");
					this.huboInconsistencias=true;
				}
				
				//edicion de registro
				registroAV="";
				//CAMPO 1 **********CÓDIGO PRESTADOR DE SERVICIOS***************************
				if(!presenciaInconsistenciaGeneral)
				{
					if(institucion.getCodMinSalud().trim().equals(""))
					{
						aux="campo sin información (para todas las facturas)";
						
						this.editarInconsistencia(listadoAV,
								ConstantesBD.forecatAV,i,
								"código del prestador de servicios",
								aux);
						this.huboInconsistencias=true;
						presenciaInconsistenciaGeneral=true;
					}
					else
					{
						aux = UtilidadTexto.revisarCaracteresEspeciales(institucion.getCodMinSalud());
						if(aux.trim().length()>12)
							aux = aux.substring(0,12);
						
						registroAV+=aux.trim().toUpperCase();
					}
				}
					
				//CAMPO 2 **********TIPO DE IDENTIFICACION DE LA VÍCTIMA**************
				if((listadoAV.get("tipoIdentificacion_"+i)+"").trim().equals(""))
				{
					this.editarInconsistencia(listadoAV,
							ConstantesBD.forecatAV,i,
							"tipo identificacion de la víctima",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAV+=",";
				}
				else
				{
					String tipoId=(listadoAV.get("tipoIdentificacion_"+i)+"").trim();
					if(!tipoId.equals("CC")&&!tipoId.equals("CE")&&!tipoId.equals("PA")&&!tipoId.equals("RC")&&
						!tipoId.equals("TI")&&!tipoId.equals("AS")&&!tipoId.equals("MS")&&!tipoId.equals("NU"))
					{
						this.editarInconsistencia(listadoAV,
								ConstantesBD.forecatAV,i,
								"tipo identificacion víctima",
								"campo con datos inválidos");
						this.huboInconsistencias=true;
					}
					registroAV+=","+tipoId.toUpperCase();
					
				}
				
				//CAMPO 3 **********NÚMERO DE IDENTIFICACIÓN DE LA VÍCTIMA************		
				if((listadoAV.get("numeroIdentificacion_"+i)+"").trim().equals(""))
				{
					this.editarInconsistencia(listadoAV,
							ConstantesBD.forecatAV,i,
							"número identificacion víctima",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAV+=",";
				}
				else
				{
					aux=UtilidadTexto.revisarCaracteresEspeciales(listadoAV.get("numeroIdentificacion_"+i)+"");
					
					if(aux.trim().length()>20)
						aux = aux.substring(0,20);
					
					registroAV+=","+aux.trim().toUpperCase();
				}
				
				//CAMPO 4 **********FECHA DE ATENCION	**********************************************		
				if(listadoAV.get("fechaAtencion_"+i).toString().trim().equals(""))
				{
					this.editarInconsistencia(listadoAV,
							ConstantesBD.forecatAV,i,
							"fecha de atención",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAV+=",";
				}
				else
				{
					aux=listadoAV.get("fechaAtencion_"+i).toString();
					if(aux.trim().length()>10)
						aux = aux.substring(0,10);
					registroAV+=","+aux.trim().toUpperCase();
				}
				
				//CAMPO 5********CÓDIGO DE LA CONSULTA********************************
				aux=UtilidadTexto.revisarCaracteresEspeciales(listadoAV.get("codigoConsulta_"+i)+"");
				if(aux.trim().length()>8)
					aux = aux.substring(0,8);		
				registroAV+=","+aux.trim().toUpperCase();
				
				
				//CAMPO 6 **********DIAGNÓSTICO PRINCIPAL DE INGRESO**********************************************
				//Solo es obligatorio para consultas
				if(listadoAV.get("diagPpalIngreso_"+i).toString().trim().equals("")&&categoria==1)
				{
					this.editarInconsistencia(listadoAV,
							ConstantesBD.forecatAV,i,
							"diagnóstico principal ingreso",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAV+=",";
				}
				else
				{
					aux=UtilidadTexto.revisarCaracteresEspeciales(listadoAV.get("diagPpalIngreso_"+i).toString().trim());
					if(aux.length()>4)
						aux = aux.substring(0,4);
					registroAV+=","+aux.toUpperCase();
				}
				
				//CAMPO 7 **********DIAGNÓSTICO PRINCIPAL DE EGRESO**********************************************
				//Solo es obligatorio para consultas
				if(listadoAV.get("diagPpalEgreso_"+i).toString().trim().equals("")&&categoria==1)
				{
					this.editarInconsistencia(listadoAV,
							ConstantesBD.forecatAV,i,
							"diagnóstico principal egreso",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAV+=",";
				}
				else
				{
					aux=UtilidadTexto.revisarCaracteresEspeciales(listadoAV.get("diagPpalEgreso_"+i).toString().trim());
					if(aux.length()>4)
						aux = aux.substring(0,4);
					registroAV+=","+aux.toUpperCase();
				}
				
				//CAMPO 8********VALOR DE LA CONSULTA********************************
				//Solo aplica para Consultas
				if(listadoAV.get("valorConsulta_"+i).toString().trim().equals("")&&categoria==1)
				{
					this.editarInconsistencia(listadoAV,
							ConstantesBD.forecatAV,i,
							"valor de la consulta",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAV+=",";
				}
				else
				{
					aux = listadoAV.get("valorConsulta_"+i).toString();
					if(!aux.equals(""))
					{
						aux=UtilidadTexto.formatearValores(aux,"######0.00");
						aux = UtilidadTexto.separarParteEnteraYDecimal(aux)[0];
						if(aux.trim().length()>15)
							aux = aux.substring(0,15);		
					}
					registroAV+=","+aux.trim().toUpperCase();
				}
				
				//CAMPO 9********CÓDIGO DEL PROCEDIMIENTO********************************
				aux=UtilidadTexto.revisarCaracteresEspeciales(listadoAV.get("codigoProcedimiento_"+i)+"");
				if(aux.trim().length()>8)
					aux = aux.substring(0,8);		
				registroAV+=","+aux.trim().toUpperCase();
				
				//CAMPO 10********CANTIDAD DEL PROCEDIMIENTO********************************
				//Solo aplica para Procedimientos
				if(listadoAV.get("cantidadProcedimiento_"+i).toString().trim().equals("")&&categoria==2)
				{
					this.editarInconsistencia(listadoAV,
							ConstantesBD.forecatAV,i,
							"cantidad del procedimiento",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAV+=",";
				}
				else
				{
					aux = listadoAV.get("cantidadProcedimiento_"+i).toString();
					if(aux.trim().length()>3)
						aux = aux.substring(0,3);
					registroAV+=","+aux.trim().toUpperCase();
				}
				
				//CAMPO 11********VALOR DEL PROCEDIMIENTO********************************
				//Solo aplica para Procedimientos
				if(listadoAV.get("valorProcedimiento_"+i).toString().trim().equals("")&&categoria==2)
				{
					this.editarInconsistencia(listadoAV,
							ConstantesBD.forecatAV,i,
							"valor del procedimiento",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAV+=",";
				}
				else
				{
					aux = listadoAV.get("valorProcedimiento_"+i).toString();
					if(!aux.equals(""))
					{
						aux=UtilidadTexto.formatearValores(aux,"######0.00");
						aux = UtilidadTexto.separarParteEnteraYDecimal(aux)[0];
						if(aux.trim().length()>15)
							aux = aux.substring(0,15);		
					}
					registroAV+=","+aux.trim().toUpperCase();
				}
				
				//CAMPO 12********TIPO DE MEDICAMENTO********************************
				//Solo aplica para Medicamentos
				if(listadoAV.get("tipoMedicamento_"+i).toString().trim().equals("")&&categoria==3)
				{
					this.editarInconsistencia(listadoAV,
							ConstantesBD.forecatAV,i,
							"tipo de medicamento",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAV+=",";
				}
				else
				{
					aux = listadoAV.get("tipoMedicamento_"+i).toString();
					if(!aux.equals(""))
					{
						if(!aux.equals("1")&&!aux.equals("2"))
						{
							this.editarInconsistencia(listadoAV,
									ConstantesBD.forecatAV,i,
									"tipo de medicamento",
									"campo con datos inválidos");
							this.huboInconsistencias=true;
						}
					}
					registroAV+=","+aux.trim().toUpperCase();
				}
				
				//CAMPO 13********CÓDIGO MEDICAMENTO POS********************************
				//Solo obligatorio para Medicamentos POS
				if(listadoAV.get("codigoMedPos_"+i).toString().trim().equals("")&&categoria==3&&esPos)
				{
					this.editarInconsistencia(listadoAV,
							ConstantesBD.forecatAV,i,
							"código medicamento pos",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAV+=",";
				}
				else
				{
					aux = listadoAV.get("codigoMedPos_"+i).toString().trim();
					if(!aux.equals("")&&Integer.parseInt(aux)<=0)
					{
						this.editarInconsistencia(listadoAV,
								ConstantesBD.forecatAV,i,
								"código medicamento pos",
								"campo con datos inválidos");
						this.huboInconsistencias=true;
					}
					if(aux.length()>20)
						aux = aux.substring(0, 20);
					registroAV+=","+aux.trim().toUpperCase();
				}
				
				//CAMPO 14********NOMBRE MEDICAMENTO NO POS********************************
				//Solo obligatorio para Medicamentos NO POS
				if(listadoAV.get("nombreMedPos_"+i).toString().trim().equals("")&&categoria==3&&!esPos)
				{
					this.editarInconsistencia(listadoAV,
							ConstantesBD.forecatAV,i,
							"nombre medicamento no pos",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAV+=",";
				}
				else
				{
					aux = UtilidadTexto.revisarCaracteresEspeciales(listadoAV.get("nombreMedPos_"+i).toString().trim());
					if(aux.length()>30)
						aux = aux.substring(0, 30);
					registroAV+=","+aux.trim().toUpperCase();
				}
				
				//CAMPO 15********FORMA FARMACÉUTICA********************************
				//Solo obligatorio para Medicamentos NO POS
				if(listadoAV.get("formaFarmaceutica_"+i).toString().trim().equals("")&&categoria==3&&!esPos)
				{
					this.editarInconsistencia(listadoAV,
							ConstantesBD.forecatAV,i,
							"forma farmacéutica",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAV+=",";
				}
				else
				{
					aux = UtilidadTexto.revisarCaracteresEspeciales(listadoAV.get("formaFarmaceutica_"+i).toString().trim());
					if(aux.length()>20)
						aux = aux.substring(0, 20);
					registroAV+=","+aux.trim().toUpperCase();
				}
				
				//CAMPO 16********CONCENTRACIÓN MEDICAMENTO NO POS********************************
				//Solo obligatorio para Medicamentos NO POS
				if(listadoAV.get("concentracionMedPos_"+i).toString().trim().equals("")&&categoria==3&&!esPos)
				{
					this.editarInconsistencia(listadoAV,
							ConstantesBD.forecatAV,i,
							"concentración medicamento no pos",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAV+=",";
				}
				else
				{
					aux = UtilidadTexto.revisarCaracteresEspeciales(listadoAV.get("concentracionMedPos_"+i).toString().trim());
					if(aux.length()>20)
						aux = aux.substring(0, 20);
					registroAV+=","+aux.trim().toUpperCase();
				}
				
				//CAMPO 17********UNIDAD MEDIDA MEDICAMENTO********************************
				//Solo obligatorio para Medicamentos NO POS
				if(listadoAV.get("unidadMedidaPos_"+i).toString().trim().equals("")&&categoria==3&&!esPos)
				{
					this.editarInconsistencia(listadoAV,
							ConstantesBD.forecatAV,i,
							"unidad de medida medicamento",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAV+=",";
				}
				else
				{
					aux = UtilidadTexto.revisarCaracteresEspeciales(listadoAV.get("unidadMedidaPos_"+i).toString().trim());
					if(aux.length()>20)
						aux = aux.substring(0, 20);
					registroAV+=","+aux.trim().toUpperCase();
				}
				
				//CAMPO 18********CANTIDAD DE MEDICAMENTOS********************************
				//Solo obligatorio para Medicamentos 
				if(listadoAV.get("cantidadMed_"+i).toString().trim().equals("")&&categoria==3)
				{
					this.editarInconsistencia(listadoAV,
							ConstantesBD.forecatAV,i,
							"cantidad de medicamentos",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAV+=",";
				}
				else
				{
					aux = listadoAV.get("cantidadMed_"+i).toString().trim();
					if(aux.length()>3)
						aux = aux.substring(0, 3);
					registroAV+=","+aux.trim().toUpperCase();
				}
				
				//CAMPO 19********VALOR DEL MEDICAMENTO********************************
				//Solo aplica para Medicamentos
				if(listadoAV.get("valorTotalMed_"+i).toString().trim().equals("")&&categoria==3)
				{
					this.editarInconsistencia(listadoAV,
							ConstantesBD.forecatAV,i,
							"valor total medicamento",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAV+=",";
				}
				else
				{
					aux = listadoAV.get("valorTotalMed_"+i).toString();
					if(!aux.equals(""))
					{
						aux=UtilidadTexto.formatearValores(aux,"######0.00");
						aux = UtilidadTexto.separarParteEnteraYDecimal(aux)[0];
						if(aux.trim().length()>15)
							aux = aux.substring(0,15);		
					}
					registroAV+=","+aux.trim().toUpperCase();
				}
				
				//CAMPO 20********TIPO DE SERVICIO********************************
				//Solo aplica para Otros
				if(listadoAV.get("tipoServicio_"+i).toString().trim().equals("")&&categoria==4)
				{
					this.editarInconsistencia(listadoAV,
							ConstantesBD.forecatAV,i,
							"tipo de servicio",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAV+=",";
				}
				else
				{
					aux = listadoAV.get("tipoServicio_"+i).toString();
					if(!aux.equals(""))
					{
						if(Integer.parseInt(aux)<1||Integer.parseInt(aux)>4)
						{
							this.editarInconsistencia(listadoAV,
									ConstantesBD.forecatAV,i,
									"tipo de servicio",
									"campo con datos inválidos");
							this.huboInconsistencias=true;
						}
					}
					registroAV+=","+aux.trim().toUpperCase();
				}
				
				//CAMPO 21********CANTIDAD********************************
				//Solo aplica para Otros
				if(listadoAV.get("cantidad_"+i).toString().trim().equals("")&&categoria==4)
				{
					this.editarInconsistencia(listadoAV,
							ConstantesBD.forecatAV,i,
							"cantidad",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAV+=",";
				}
				else
				{
					aux = listadoAV.get("cantidad_"+i).toString().trim();
					if(aux.length()>3)
						aux = aux.substring(0, 3);
					registroAV+=","+aux.trim().toUpperCase();
				}
				
				//CAMPO 22********VALOR TOTAL SERVICIO********************************
				//Solo aplica para Otros
				if(listadoAV.get("valorTotalServicio_"+i).toString().trim().equals("")&&categoria==4)
				{
					this.editarInconsistencia(listadoAV,
							ConstantesBD.forecatAV,i,
							"valor total de los servicios",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAV+=",";
				}
				else
				{
					aux = listadoAV.get("valorTotalServicio_"+i).toString().trim();
					if(!aux.equals(""))
					{
						aux=UtilidadTexto.formatearValores(aux,"######0.00");
						aux = UtilidadTexto.separarParteEnteraYDecimal(aux)[0];
						if(aux.length()>15)
							aux = aux.substring(0, 15);
					}
					registroAV+=","+aux.toUpperCase();
				}
				
				//CAMPO 23********NÚMERO DE LA FACTURA********************************
				if(listadoAV.get("consecutivo_"+i).toString().trim().equals(""))
				{
					this.editarInconsistencia(listadoAV,
							ConstantesBD.forecatAV,i,
							"número de la factura",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAV+=",";
				}
				else
				{
					aux = UtilidadTexto.revisarCaracteresEspeciales(listadoAV.get("consecutivo_"+i).toString().trim());
					if(aux.length()>20)
						aux = aux.substring(0, 20);
					registroAV+=","+aux.toUpperCase();
				}
				
				//CAMPO 24********FECHA FACTURA********************************
				if(listadoAV.get("fechaFactura_"+i).toString().trim().equals(""))
				{
					this.editarInconsistencia(listadoAV,
							ConstantesBD.forecatAV,i,
							"fecha de la factura",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAV+=",";
				}
				else
				{
					aux = listadoAV.get("fechaFactura_"+i).toString().trim();
					if(aux.length()>10)
						aux = aux.substring(0, 10);
					registroAV+=","+aux.toUpperCase();
				}
				
				//CAMPO 25********VALOR TOTAL FACTURA********************************
				if(listadoAV.get("valorTotalFactura_"+i).toString().trim().equals(""))
				{
					this.editarInconsistencia(listadoAV,
							ConstantesBD.forecatAV,i,
							"valor total de la factura",
							"campo sin información");
					this.huboInconsistencias=true;
					registroAV+=",";
				}
				else
				{
					aux = listadoAV.get("valorTotalFactura_"+i).toString().trim();
					if(!aux.equals(""))
					{
						aux=UtilidadTexto.formatearValores(aux,"######0.00");
						aux = UtilidadTexto.separarParteEnteraYDecimal(aux)[0];
						if(aux.length()>15)
							aux = aux.substring(0, 15);
					}
					registroAV+=","+aux.toUpperCase();
				}
				
				
					
				//*************FIN**************************************
				
				registroAV+="\n";
				bufferAV.write(registroAV);
				contadorRegistros++;
				
			} //fin for
			
			//se almacena el número de registros creados para el archivo AV
			this.numeroRegistrosCreados.put(ConstantesBD.forecatAV,contadorRegistros+"");
			
			bufferAV.close();
			if(contadorRegistros<1)
			{
				//archivo sin registros
				archivoAV.delete();
				return null;
			}
			
			//			 verificar si es una creacion automatica de forecats desde la consulta de rips
			if (this.esRipsForecatAuto)
				//				 si lo es cambiar ruta de acceso a el
				// pero primero verificar para que tipo de archivo se iso, si para cunetas cobro o para facturas
				if (this.esCuentaCobro)
					return "../ripsCuentaCobro/ripsCuentaCobro.do?estado=detalle&archivo=AV";
				else
					return "../ripsFactura/ripsFactura.do?estado=detalle&archivo=AV";
			else
				return "../generacionAnexosForecat/generacionAnexosForecat.do?estado=detalle&archivo=AV";
			
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo AV al generarlo: "+e);
			this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo AV (Forecat): "+e));
			return null;
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo AV: "+e);
			this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo AV (Forecat): "+e));
			return null;
		}
	}
	/**
	 * Método implementado para generar el archivo AC
	 * @return
	 */
	private String generarAC() 
	{
		String prefijo="";
		String registroAC="";
		//presencia de inconsistencias
		boolean[] presenciaInconsistencia={false,false,false};
		
		String aux=""; //variable auxiliar para revisar caracteres especiales en los campos de texto
		int contadorRegistros=0; //variable usada para contar los registros del AC
		
		try
		{
			//apertura de archivo AC
			aux=UtilidadTexto.revisarCaracteresEspeciales(this.numeroRemision);
			File archivoAC=new File(dirConvenio.getAbsolutePath(),ConstantesBD.forecatAC+aux+".txt");
			FileWriter streamAC=new FileWriter(archivoAC,false); //se coloca false para el caso de que esté repetido
			BufferedWriter bufferAC=new BufferedWriter(streamAC);
			
			for(int i=0;i<4;i++)
			{
				registroAC="";
				
				switch(i)
				{
					//referente a archivo AA
					case 0:
					prefijo=ConstantesBD.forecatAA;
					break;
					//referente a archivo VH
					case 1:
					prefijo=ConstantesBD.forecatVH;
					break;
					//referente a archivo AV
					case 2:
					prefijo=ConstantesBD.forecatAV;
					break;
					//referente a archivo AC
					case 3:
					prefijo=ConstantesBD.forecatAC;
					break;
				}
				
				//CAMPO 1***************RAZÓN SOCIAL********************************************
				if(institucion.getRazonSocial().trim().equals(""))
				{
					if(!presenciaInconsistencia[0])
						this.generarInconsistencia(ConstantesBD.forecatAC,"razón social","campo sin información");
					this.huboInconsistencias=true;
					presenciaInconsistencia[0]=true;
				}
				else
				{
					aux = UtilidadTexto.revisarCaracteresEspeciales(institucion.getRazonSocial().trim());
					if(aux.length()>60)
						aux = aux.substring(0, 60);
					registroAC+=aux.toUpperCase();
				}
				
				//CAMPO 2***************CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD********************************************
				if(institucion.getCodMinSalud().trim().equals(""))
				{
					if(!presenciaInconsistencia[1])
						this.generarInconsistencia(ConstantesBD.forecatAC,"código prestador de servicios","campo sin información");
					this.huboInconsistencias=true;
					presenciaInconsistencia[1]=true;
					registroAC+=",";
				}
				else
				{
					aux = UtilidadTexto.revisarCaracteresEspeciales(institucion.getCodMinSalud().trim());
					if(aux.length()>12)
						aux = aux.substring(0, 12);
					registroAC+=","+aux.toUpperCase();
				}
				
				//CAMPO 3***************NIT*********************************************************************************
				if(institucion.getNit().trim().equals(""))
				{
					if(!presenciaInconsistencia[2])
						this.generarInconsistencia(ConstantesBD.forecatAC,"nit","campo sin información");
					this.huboInconsistencias=true;
					presenciaInconsistencia[2]=true;
					registroAC+=",";
				}
				else
				{
					aux = UtilidadTexto.revisarCaracteresEspeciales(institucion.getNit().trim());
					if(aux.length()>20)
						aux = aux.substring(0, 20);
					registroAC+=","+aux.toUpperCase();
				}
				
				//CAMPO 4***********FECHA INICIAL*********************************
				registroAC+=","+this.fechaInicial;
				
				//CAMPO 5***********FECHA FINAL*********************************
				registroAC+=","+this.fechaFinal;
				
				//CAMPO 6***********FECHA DE ENVÍO*********************************
				registroAC+=","+this.fechaRemision.toUpperCase();
				
				//CAMPO 7***********NOMBRE DEL ARCHIVO***********************
				aux=UtilidadTexto.revisarCaracteresEspeciales(this.numeroRemision);
				aux=prefijo+aux;
				if(aux.length()>8)
					aux = aux.substring(0, 8);
				registroAC+=","+aux.toUpperCase();
				//CAMPO 4**********TOTAL REGISTROS************************
				if(prefijo.equals(ConstantesBD.forecatAC))
				{
					if(contadorRegistros>1)
					{
						contadorRegistros++;
						registroAC+=","+contadorRegistros+"";
					}
					else
						contadorRegistros--;
				}
				else
				{
					aux = this.numeroRegistrosCreados.get(prefijo).toString();
					if(aux.length()>5)
						aux = aux.substring(0, 5);
					registroAC+=","+aux.toUpperCase();
				}
				//************************FIN**********************************
					
				registroAC+="\n";
				bufferAC.write(registroAC);
				contadorRegistros++;
						
					
				
			}
			bufferAC.close();
			if(contadorRegistros<1)
			{
				//archivo sin registros
				archivoAC.delete();
				return null;
			}
			
			//			 verificar si es una creacion automatica de forecats desde la consulta de rips
			if (this.esRipsForecatAuto)

				//				 	si lo es cambiar ruta de acceso a el
				// pero primero verificar para que tipo de archivo se iso, si para cunetas cobro o para facturas
				if (this.esCuentaCobro)
					return "../ripsCuentaCobro/ripsCuentaCobro.do?estado=detalle&archivo=AC";
				else
					return "../ripsFactura/ripsFactura.do?estado=detalle&archivo=AC";
			else				
				return "../generacionAnexosForecat/generacionAnexosForecat.do?estado=detalle&archivo=AC";
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo AC al generarlo: "+e);
			this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo AC (Forecat): "+e));
			return null;
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo AC: "+e);
			this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo AC (Forecat): "+e));
			return null;
		}
	}
	//**********************************************************************
	
	/**
	 * Método implementado para efectuar la consulta del archivo AA
	 * @param con
	 * @return
	 */
	private HashMap consultaAA(Connection con) 
	{
		HashMap campos = new HashMap();
		campos.put("convenio", this.convenio+"");
		campos.put("salarioMinimo",this.salarioMinimo+"");
		campos.put("fechaInicial",this.fechaInicial);
		campos.put("fechaFinal",this.fechaFinal);
		
		// campopara buesqueda de facturas en la interfaz de ax_rips
		campos.put("numeroFactura",this.numeroFactura);
		
		//campo para la buesqueda filtrando por el numero de cuenta cobro
		campos.put("numeroCuentaCobro",this.numeroCuentaCobro);
		
		return generacionDao.consultaAA(con, campos,this.esAxRips, this.esCuentaCobro);
	}
	
	/**
	 * Método implementado para efectuar la consulta del archivo VH
	 * @param con
	 * @return
	 */
	private HashMap consultaVH(Connection con) 
	{
		HashMap campos = new HashMap();
		campos.put("convenio", this.convenio+"");
		campos.put("salarioMinimo",this.salarioMinimo+"");
		campos.put("fechaInicial",this.fechaInicial);
		campos.put("fechaFinal",this.fechaFinal);
		
		
		// campopara buesqueda de facturas en la interfaz de ax_rips
		campos.put("numeroFactura",this.numeroFactura);
		
		// campo para la buesqueda filtrando por el numero de cuenta cobro
		campos.put("numeroCuentaCobro",this.numeroCuentaCobro);
		
		return generacionDao.consultaVH(con, campos,this.esAxRips, this.esCuentaCobro);
	}
	
	/**
	 * Método implementado para efectuar la consulta del archivo AV
	 * @param con
	 * @return
	 */
	private HashMap consultaAV(Connection con) 
	{
		HashMap campos = new HashMap();
		campos.put("convenio", this.convenio+"");
		campos.put("tipoManual",this.tipoCodigo+"");
		campos.put("salarioMinimo",this.salarioMinimo+"");
		campos.put("fechaInicial",this.fechaInicial);
		campos.put("fechaFinal",this.fechaFinal);
		
//		 campopara buesqueda de facturas en la interfaz de ax_rips
		campos.put("numeroFactura",this.numeroFactura);
		
//		 campo para la buesqueda filtrando por el numero de cuenta cobro
		campos.put("numeroCuentaCobro",this.numeroCuentaCobro);
		
		return generacionDao.consultaAV(con, campos, this.esAxRips, this.esCuentaCobro);
	}
	
	/**
	 * Sub-método para editar las inconsistencias de FORECAT
	 * @param listado
	 * @param prefijo
	 * @param posicion
	 * @param campo
	 * @param observacion
	 */
	private void editarInconsistencia(HashMap listado, String prefijo, int posicion, String campo, String observacion) 
	{
		
		String consecutivo = listado.get("consecutivo_"+posicion)+"";
		String tipoEvento = listado.get("tipoEvento_"+posicion).toString();
		boolean desplazado = UtilidadTexto.getBoolean(listado.get("desplazado_"+posicion).toString());
		
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
				total=Integer.parseInt(this.inconsistencias.get("total")+"");
				total++;
				this.inconsistencias.put("total",total+"");
			}
			//encabezado de la inconsistencia
			this.inconsistencias.put("pos_"+total+"",consecutivo);
			this.inconsistencias.put(consecutivo,"true");
			this.inconsistencias.put("tipoIdentificacion_"+consecutivo,listado.get("tipoIdentificacion_"+posicion));
			this.inconsistencias.put("numeroIdentificacion_"+consecutivo,listado.get("numeroIdentificacion_"+posicion));
			this.inconsistencias.put("paciente_"+consecutivo,listado.get("paciente_"+posicion));
			this.inconsistencias.put("cuenta_"+consecutivo,listado.get("cuenta_"+posicion));
			this.inconsistencias.put("viaIngreso_"+consecutivo,listado.get("viaIngreso_"+posicion));
			//se toma la naturaleza del evento
			if(tipoEvento.equals(ConstantesIntegridadDominio.acronimoAccidenteTransito))
				this.inconsistencias.put("naturalezaEvento_"+consecutivo,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoAccidenteTransito));
			else if(tipoEvento.equals(ConstantesIntegridadDominio.acronimoEventoCatastrofico))
				this.inconsistencias.put("naturalezaEvento_"+consecutivo,ValoresPorDefecto.getIntegridadDominio(ConstantesIntegridadDominio.acronimoEventoCatastrofico));
			else if(desplazado)
				this.inconsistencias.put("naturalezaEvento_"+consecutivo,"Desplazado");
			else
				this.inconsistencias.put("naturalezaEvento_"+consecutivo,"");
			//Se verifica la naturaleza del evento
			this.inconsistencias.put("elementos_"+consecutivo,"1");
			//detalle de la inconsistencia
			this.inconsistencias.put("archivo_"+consecutivo+"_0",prefijo);
			this.inconsistencias.put("campo_"+consecutivo+"_0",campo);
			
			if(prefijo.equals(ConstantesBD.forecatAV))
			{
				this.inconsistencias.put("orden_"+consecutivo+"_0",listado.get("orden_"+posicion));
				this.inconsistencias.put("servArt_"+consecutivo+"_0",listado.get("servArt_"+posicion));
			}
			else
			{
				this.inconsistencias.put("orden_"+consecutivo+"_0","               ");
				this.inconsistencias.put("servArt_"+consecutivo+"_0","          ");
			}
			
			this.inconsistencias.put("observacion_"+consecutivo+"_0",observacion);
			
		}
		else
		{
			//si entra aquí quiere decir que la factura ya tiene inconsistencias
			//no es necesario añadir el encabezado, sólo el detalle
			int nElementos=Integer.parseInt(this.inconsistencias.get("elementos_"+consecutivo)+"");
			nElementos++;
			this.inconsistencias.put("elementos_"+consecutivo,nElementos+"");
			//detalle de la inconsistencia
			this.inconsistencias.put("archivo_"+consecutivo+"_"+(nElementos-1),prefijo);
			this.inconsistencias.put("campo_"+consecutivo+"_"+(nElementos-1),campo);
			if(prefijo.equals(ConstantesBD.forecatAV))
			{
				this.inconsistencias.put("orden_"+consecutivo+"_"+(nElementos-1),listado.get("orden_"+posicion));
				this.inconsistencias.put("servArt_"+consecutivo+"_"+(nElementos-1),listado.get("servArt_"+posicion));
			}
			else
			{
				this.inconsistencias.put("orden_"+consecutivo+"_"+(nElementos-1),"               ");
				this.inconsistencias.put("servArt_"+consecutivo+"_"+(nElementos-1),"          ");
			}
			
			this.inconsistencias.put("observacion_"+consecutivo+"_"+(nElementos-1),observacion);
		}
		
	}
	
	
	/**
	 * Método que verifica si se generaron registros en los archivos
	 */
	private boolean verificarArchivos() 
	{
		boolean exito = false;
			
		if(Utilidades.convertirAEntero(this.numeroRegistrosCreados.get(ConstantesBD.forecatAA)+"")>0)
			exito = true;

		if(Utilidades.convertirAEntero(this.numeroRegistrosCreados.get(ConstantesBD.forecatVH)+"")>0)
			exito = true;

		if(Utilidades.convertirAEntero(this.numeroRegistrosCreados.get(ConstantesBD.forecatAV)+"")>0)
			exito = true;
	
				
		return exito;
	}
	
	/**
	 * Método para insertar las inconsistencias del archivo AC
	 * @param prefijo
	 * @param campo
	 * @param observacion
	 */
	private void generarInconsistencia(String prefijo,String campo, String observacion) 
	{
		String registroIncon="";
		String aux="";
		try
		{
			//apertura de archivo Incon
			aux=UtilidadTexto.revisarCaracteresEspeciales(this.numeroRemision);
			File archivoIncon=new File(dirConvenio.getAbsolutePath(),ConstantesBD.ripsInconsistencias+aux+".txt");
			FileWriter streamIncon=new FileWriter(archivoIncon,true); 
			BufferedWriter bufferIncon=new BufferedWriter(streamIncon);
			
			registroIncon+="Archivo        Nombre del Campo                                     "+
			"Observaciones\n";
			
			registroIncon+=UtilidadTexto.editarEspacios(prefijo,prefijo.length(),15,false)+
			UtilidadTexto.editarEspacios(campo,campo.length(),53,false)+
			observacion+//this.editarEspacios(observacion,observacion.length(),53,false)+
			"\n\n\n";
			
			bufferIncon.write(registroIncon);
			bufferIncon.close();
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
	 * Método implementado para generar el archivos de Inconsistencias
	 * @return
	 */
	private String generarInconsistencias() 
	{
		//	se revisa si el número de las inconsistencias
		if(this.inconsistencias.get("total")!=null)
		{
			try
			{
				int numeroInconsistencias=Integer.parseInt(this.inconsistencias.get("total")+"");
				String registroIncon="";
				//apertura de archivo Incon
				String auxiliar=UtilidadTexto.revisarCaracteresEspeciales(this.numeroRemision);
				File archivoIncon=new File(dirConvenio.getAbsolutePath(),ConstantesBD.ripsInconsistencias+auxiliar+".txt");
				FileWriter streamIncon=new FileWriter(archivoIncon,false); //se coloca false para el caso de que esté repetido
				BufferedWriter bufferIncon=new BufferedWriter(streamIncon);
				
				for(int i=1;i<=numeroInconsistencias;i++)
				{
					
					//se limpia registro
					registroIncon="";
					//se toma el consecutivo de la factura de la inconsistencia
					String llave=this.inconsistencias.get("pos_"+i)+"";
					//editar el encabezado de la inconsistencias
					registroIncon+="Nº Factura: "+llave+
						" Identificación Paciente: "+
						this.inconsistencias.get("tipoIdentificacion_"+llave)+
						"."+UtilidadTexto.revisarCaracteresEspeciales(this.inconsistencias.get("numeroIdentificacion_"+llave)+"")+
						" Paciente: "+UtilidadTexto.revisarCaracteresEspeciales(this.inconsistencias.get("paciente_"+llave)+"")+
						" Cuenta Nº: "+this.inconsistencias.get("cuenta_"+llave)+
						" Naturaleza Evento: "+UtilidadTexto.revisarCaracteresEspeciales(this.inconsistencias.get("naturalezaEvento_"+llave)+"")+
						" Vía de Ingreso: "+this.inconsistencias.get("viaIngreso_"+llave)+"\n\n";
					//editar el detalle de la inconsistencia
					
					if(this.inconsistencias.get("elementos_"+llave)!=null)
					{
						
						bufferIncon.write(registroIncon);
						registroIncon="";
						int numeroDetalles=Integer.parseInt(this.inconsistencias.get("elementos_"+llave)+"");
						
						registroIncon+="Archivo        Nombre del Campo                                     Orden Médica   Serv/art  "+
							"Observaciones\n";
						bufferIncon.write(registroIncon);
						for(int j=1;j<=numeroDetalles;j++)
						{
							registroIncon="";
							String aux=this.inconsistencias.get("archivo_"+llave+"_"+(j-1))+"";
							registroIncon+=UtilidadTexto.editarEspacios(aux.trim(),aux.trim().length(),15,false);
							aux=this.inconsistencias.get("campo_"+llave+"_"+(j-1))+"";
							registroIncon+=UtilidadTexto.editarEspacios(aux.trim(),aux.trim().length(),53,false);
							aux=this.inconsistencias.get("orden_"+llave+"_"+(j-1))+"";
							registroIncon+=UtilidadTexto.editarEspacios(aux.trim(),aux.trim().length(),15,false);
							aux=this.inconsistencias.get("servArt_"+llave+"_"+(j-1))+"";
							registroIncon+=UtilidadTexto.editarEspacios(aux.trim(),aux.trim().length(),10,false);
							aux=this.inconsistencias.get("observacion_"+llave+"_"+(j-1))+"";
							registroIncon+=aux;//this.editarEspacios(aux.trim(),aux.trim().length(),50,false);
							registroIncon+="\n";
							
							bufferIncon.write(registroIncon);
						}
						registroIncon="\n\n\n\n";
						
						bufferIncon.write(registroIncon);
						
					}
					
					
				}
				bufferIncon.close();
				//				 verificar si es una creacion automatica de forecats desde la consulta de rips
				if (this.esRipsForecatAuto)

					//					 si lo es cambiar ruta de acceso a el
					// pero primero verificar para que tipo de archivo se iso, si para cunetas cobro o para facturas
					if (this.esCuentaCobro)
						return "../ripsCuentaCobro/ripsCuentaCobro.do?estado=detalle&archivo=Incon";
					else
						return "../ripsFactura/ripsFactura.do?estado=detalle&archivo=Incon";
				else
					return "../generacionAnexosForecat/generacionAnexosForecat.do?estado=detalle&archivo=Incon";
			}
			catch(FileNotFoundException e)
			{
				logger.error("No se pudo encontrar el archivo Incon al generarlo: "+e);
				this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo Inconsistencias (Forecat): "+e));
				return null;
			}
			catch(IOException e)
			{
				logger.error("Error en los streams del archivo Incon: "+e);
				this.errores.add("", new ActionMessage("errors.notEspecific","No se pudo generar el archivo Inconsistencias (Forecat): "+e));
				return null;
			}
		}
		else
		{
			return null;
		}
	}
	/**
	 * @return the convenio
	 */
	public int getConvenio() {
		return convenio;
	}
	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(int convenio) {
		this.convenio = convenio;
	}
	/**
	 * @return the fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}
	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	/**
	 * @return the fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}
	/**
	 * @param fechaInicial the fechaInicial to set
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	/**
	 * @return the fechaRemision
	 */
	public String getFechaRemision() {
		return fechaRemision;
	}
	/**
	 * @param fechaRemision the fechaRemision to set
	 */
	public void setFechaRemision(String fechaRemision) {
		this.fechaRemision = fechaRemision;
	}
	/**
	 * @return the tipoCodigo
	 */
	public int getTipoCodigo() {
		return tipoCodigo;
	}
	/**
	 * @param tipoCodigo the tipoCodigo to set
	 */
	public void setTipoCodigo(int tipoCodigo) {
		this.tipoCodigo = tipoCodigo;
	}
	/**
	 * @return the numeroRemision
	 */
	public String getNumeroRemision() {
		return numeroRemision;
	}
	/**
	 * @param numeroRemision the numeroRemision to set
	 */
	public void setNumeroRemision(String numeroRemision) {
		this.numeroRemision = numeroRemision;
	}
	/**
	 * @return the huboRegistros
	 */
	public boolean isHuboRegistros() {
		return huboRegistros;
	}
	/**
	 * @param huboRegistros the huboRegistros to set
	 */
	public void setHuboRegistros(boolean huboRegistros) {
		this.huboRegistros = huboRegistros;
	}
	/**
	 * @return the huboInconsistencias
	 */
	public boolean isHuboInconsistencias() {
		return huboInconsistencias;
	}
	/**
	 * @param huboInconsistencias the huboInconsistencias to set
	 */
	public void setHuboInconsistencias(boolean huboInconsistencias) {
		this.huboInconsistencias = huboInconsistencias;
	}
	/**
	 * @return the pathGeneracion
	 */
	public String getPathGeneracion() {
		return pathGeneracion;
	}
	/**
	 * @param pathGeneracion the pathGeneracion to set
	 */
	public void setPathGeneracion(String pathGeneracion) {
		this.pathGeneracion = pathGeneracion;
	}
	/**
	 * @return the institucion
	 */
	public ParametrizacionInstitucion getInstitucion() {
		return institucion;
	}
	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(ParametrizacionInstitucion institucion) {
		this.institucion = institucion;
	}
	/**
	 * @return the salarioMinimo
	 */
	public double getSalarioMinimo() {
		return salarioMinimo;
	}
	/**
	 * @param salarioMinimo the salarioMinimo to set
	 */
	public void setSalarioMinimo(double salarioMinimo) {
		this.salarioMinimo = salarioMinimo;
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
	 * @return the numeroFactura
	 */
	public int getNumeroFactura() {
		return numeroFactura;
	}
	/**
	 * @param numeroFactura the numeroFactura to set
	 */
	public void setNumeroFactura(int numeroFactura) {
		this.numeroFactura = numeroFactura;
	}
	/**
	 * @return the esRipsForecatAuto
	 */
	public boolean isEsRipsForecatAuto() {
		return esRipsForecatAuto;
	}
	/**
	 * @param esRipsForecatAuto the esRipsForecatAuto to set
	 */
	public void setEsRipsForecatAuto(boolean esRipsForecatAuto) {
		this.esRipsForecatAuto = esRipsForecatAuto;
	}
	/**
	 * @return the esCuentaCobro
	 */
	public boolean isEsCuentaCobro() {
		return esCuentaCobro;
	}
	/**
	 * @param esCuentaCobro the esCuentaCobro to set
	 */
	public void setEsCuentaCobro(boolean esCuentaCobro) {
		this.esCuentaCobro = esCuentaCobro;
	}
	/**
	 * @return the numeroCuentaCobro
	 */
	public double getNumeroCuentaCobro() {
		return numeroCuentaCobro;
	}
	/**
	 * @param numeroCuentaCobro the numeroCuentaCobro to set
	 */
	public void setNumeroCuentaCobro(double numeroCuentaCobro) {
		this.numeroCuentaCobro = numeroCuentaCobro;
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
}
