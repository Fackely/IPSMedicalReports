/*
 * Created on Aug 20, 2005
 *
 * @todo To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.princetonsa.mundo.facturacion;

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
import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.LogsAxioma;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.facturacion.EstanciaAutomaticaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.EstanciaAutomaticaDao;
import com.princetonsa.dto.manejoPaciente.DtoDetAutorizacionEst;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.CargosDirectos;
import com.princetonsa.mundo.manejoPaciente.Autorizaciones;
import com.princetonsa.mundo.medicamentos.DespachoMedicamentos;
import com.princetonsa.mundo.solicitudes.Solicitud;

/**
 * @author sebacho
 *
 * Mundo para el manejo de todas las operaciones de Estancia Automatica
 * del módulo de Facturacion
 */
public class EstanciaAutomatica {
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(EstanciaAutomatica.class);
	
	/**
	 * DAO para el manejo de la Estancia Automática
	 */
	private EstanciaAutomaticaDao estanciaAutomaticaDao=null;
	
	//**************ATRIBUTOS*****************************************+
	/**
	 * Variable que almacena el centro de costo de la cama
	 */
	private int codigoCentroCosto;
	
	/**
	 * Variable que almacena el nombre del centro de costo de las camas
	 */
	private String nombreCentroCosto;
	/**
	 * Variables para almacenar el rango de las fechas de estancia
	 */
	private String fechaInicialEstancia;
	private String fechaFinalEstancia;
	
	/**
	 * Objeto para almacenar las cuentas a las cuales se les
	 * va a generar la solicitud de estancia
	 */
	private HashMap cuentasEstancia= new HashMap();
	
	/**
	 * Variable usada para identificar la cuenta a la cual se le
	 * va a generar la solicitud de tipo estancia
	 *
	 */
	private int idCuenta;
	
	/**
	 * Variable para almacenar el nombre del paciente
	 */
	private String paciente;
	
	/**
	 * Objeto para almacenar los traslados cama de la cuenta
	 */
	private HashMap trasladosCamaCuenta= new HashMap();
	
	/**
	 * Objeto usado para almacenar las inconsistencias
	 */
	private HashMap inconsistencias= new HashMap();
	
	/**
	 * Objeto para almacenar el tipo de monitoreo
	 * codigo => codigo del tipo monitoreo
	 * nombre => prioridad de cobro
	 * descripcion => servicio
	 */
	private InfoDatosInt tipoMonitoreo;
	
	/**
	 * Variable que sirva para indicar si dentro de los traslados cama
	 * hay camas UCI
	 */
	private boolean hayCamasUCI;
	
	/**
	 * Variable que verifica si ya se tiene un tipo de monitoreo
	 */
	private boolean hayTipoMonitoreo;
	
	/**
	 * Variable que indica si hubo inconsistencias en la generación
	 * de la estancia 
	 */
	private boolean huboInconsistencias;
	
	/**
	 * Variable donde se almacena el nombre del archivo de Inconsistencias
	 */
	private String nombreArchivo;
	
	/**
	 * Variable donde se almacena el tipo de estancia
	 */
	private int tipoEstancia;
	
	/**
	 * Centro de Atención
	 */
	private int centroAtencion;
	
	/**
	 *Codigo del convenio responsable de la cuenta del paciente 
	 */	
	private int convenioResponsablePaciente;
	
	/**
	 * Atributo que almacena el tipo de entidad que ejecuta el servicio de estancia
	 */
	private String tipo_entidad_autoriza;
	
	/**
	 * Atributo que almacena el codigo del contrato del convenio responsable del paciente 
	 */
	private int codigoContratoConvenio;
	
	/**
	 * Atributo que almacena la clasificación socioeconomica del paciente 
	 */
	private int clasificacionSocioEconomica;
	
	/**
	 * Atributo que almacena que almacena el codigo del paciente 
	 */
	private int codigoPaciente;
	
	/**
	 * Atributo que almacena el tipo de afiliado del paciente 
	 */
	private String tipoAfiliado;
	
	/**
	 * Atributo que almacena la naturaleza del paciente 
	 */
	private int naturalezaPaciente;
	
	/**
	 * Atributo que almacena el tipo de paciente 
	 */
	private String tipoPaciente;
	
	/**
	 * Atributo que almacena el porcentaje de monto cuando la cuenta no maneja montos 
	 */
	private Double porcentajeMontoCobro;
	
	/**
	 * Atributo que almacena el tipo del detalle del monto 
	 */
	private String  tipoDetalleMonto;
	
	/**
	 * Atributo que almacena el valor del monto general 
	 */
	private Double valorMontoGeneral;
	
	/**
	 * Atributo que almacena el porcentaje del monto general 
	 */
	private Double porcentajeMontoGeneral;
	
	/**
	 * Atributo que almacena el tipo del monto 
	 */
	private Integer tipoMonto;
	
	/**
	 * Atributo que almacena la cantidad del monto general 
	 */
	private Integer cantidadMontoGeneral;
	
	/**
	 * Atributo que almacena el codigo del detalle para el monto de cobro
	 */
	private Integer codDetalleMontoCobro;
	
	/**
	 * Atributo para indicar si se genero el proceso de estancia con exito
	 */
	private boolean generoEstancia;
	
	/**
	 * Atributo para almacenar el numero de solicitud 
	 */
	private Integer numeroSolicitud;
	
	
	//**********CONSTRUCTOR & MÉTODOS DE INICIALIZACIÓN*************************
	/**
	 * Constructor
	 */
	public EstanciaAutomatica() {
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * método para inicializar los datos
	 *
	 */
	public void clean()
	{
		this.codigoCentroCosto=0;
		this.nombreCentroCosto="";
		this.fechaInicialEstancia="";
		this.fechaFinalEstancia="";
		this.cuentasEstancia= new HashMap();
		this.idCuenta=0;
		this.paciente="";
		this.trasladosCamaCuenta=new HashMap();
		this.tipoMonitoreo=new InfoDatosInt();
		this.hayCamasUCI=false;
		this.hayTipoMonitoreo=false;
		this.huboInconsistencias=false;
		this.nombreArchivo="";
		this.tipoEstancia=0;
		
		this.centroAtencion = 0;
		this.setConvenioResponsablePaciente(ConstantesBD.codigoNuncaValido);
	
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (estanciaAutomaticaDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			estanciaAutomaticaDao = myFactory.getEstanciaAutomaticaDao();
		}	
	}
	//**************MÉTODOS***********************************************
	/**
	 * Método usado para consultar las cuentas a las cuales se les va a
	 * generar una solicitud de estancia y su cargo, en la opción
	 * de Estancia Automática Por Área "Automatico"
	 * @param con
	 * @param centroCosto
	 * @param fechaEstancia
	 * @return
	 */
	public HashMap consultaCuentasEstanciaPorAreaAutomatico(Connection con, int institucion)
	{
		return estanciaAutomaticaDao.consultaCuentasEstanciaPorArea(con,this.fechaInicialEstancia,this.centroAtencion, institucion,this.codigoCentroCosto);
	}
	/**
	 * Método usado para consultar las cuentas a las cuales se les va a
	 * generar una solicitud de estancia y su cargo, en la opción
	 * de Estancia Automática Por Área "Manual"
	 * @param con
	 * @param centroCosto
	 * @param fechaEstancia
	 * @return
	 */
	public HashMap consultaCuentasEstanciaPorArea(Connection con, int institucion)
	{
		return estanciaAutomaticaDao.consultaCuentasEstanciaPorArea(con,this.fechaInicialEstancia,this.centroAtencion, institucion,ConstantesBD.codigoNuncaValido);
	}
	
	/**
	 * Método Principal para consultar el servicio de la cama asociada
	 * a la solicitud de estancia de la cuenta
	 * @param con
	 * @param cuenta
	 * @return
	 * 0 => no se encontró servicio
	 * -1 => se generó inconsistencia en tipo Monitoreo 
	 * otro valor => se encontró servicio
	 * Estos valores se usan para el control de las inconsistencias
	 */
	public int obtenerServicioCargoEstancia(Connection con, String indica)
	{
		//método principal para consultar el servicio de la solicitud
		//estancia que se va a generar
		int codigoServicio=0;
		int tipoMonitoreo=0;

		//1)*****CONSULTAR LOS TRASLADOS CAMA DE LA CUENTA*********************
		//se verifica el tipo de estancia por Area y que sea manual
		if(this.tipoEstancia==ConstantesBD.codigoTipoEstanciaPorArea&&indica.equals("Manual"))
			this.trasladosCamaCuenta=estanciaAutomaticaDao.consultarTrasladosCuenta(con,this.idCuenta,this.fechaInicialEstancia,this.codigoCentroCosto);
		else
			//no se toma en cuenta el centro de costo cuando es por paciente o por area automatica
			this.trasladosCamaCuenta=estanciaAutomaticaDao.consultarTrasladosCuenta(con,this.idCuenta,this.fechaInicialEstancia,0);
		
		int numTraslados=Integer.parseInt(this.trasladosCamaCuenta.get("numRegistros")+"");
		logger.info("Número de traslados "+numTraslados+" de la cuenta "+this.idCuenta);
		//se verifica si hay camas UCI dentro de los traslados cama
		hayCamasUCI=this.verificacionCamasUCI();
		//se reinicia el indicativo de que se cuenta con tipo de monitoreo
		hayTipoMonitoreo=false;
		
		//2)*****ITERACIÓN DE LOS TRASLADOS CONSULTADOS **********************
		for(int i=0;i<numTraslados;i++)
		{
			this.tipo_entidad_autoriza= this.trasladosCamaCuenta.get("tipo_entidad_ejecuta_"+i)+"";
			this.codigoCentroCosto=Integer.parseInt(this.trasladosCamaCuenta.get("centro_costo_"+i)+"");
			logger.info("cama=> "+this.trasladosCamaCuenta.get("cama_"+i)+" de traslado "+i);
			//si hay camas UCI solo se tomaran estas
			if(hayCamasUCI)
			{
				
				//solo se tomarán las camas UCI
				String esCamaUCI=this.trasladosCamaCuenta.get("es_uci_"+i)+"";
				if(esCamaUCI.equals("t")||esCamaUCI.equals("true")||esCamaUCI.equals("1"))
				{
					logger.info("Entró a revisión cama UCI=> "+this.trasladosCamaCuenta.get("cama_"+i));
					//3.1) ***CONSULTAR EL TIPO DE MONITOREO ***************
					tipoMonitoreo=this.obtenerTipoMonitoreo(con,tipoMonitoreo);
					//se verifica si la consulta del tipo de monitoreo fue existosa
					if(tipoMonitoreo==0)
					{
						//¡¡¡no fue exitoso!!!
						//se genera la inconsistencia
						this.generarInconsistencias("B","Paciente sin definir tipo de monitoreo",null,indica);
						//se termina el ciclo
						i=numTraslados;
						//se asigna el valor -1 para indicar que hubo inconsistencia
						codigoServicio=-1;
					}
					//fue exitoso
					else
					{
						//se consulta el servicio tomando en cuenta el tipo de monitoreo
						//3.2)** CONSULTA DEL SERVICIO EN LA TABLA SERVICIOS_CAMA ****
						codigoServicio=estanciaAutomaticaDao.consultarServicioCama(con,
								Integer.parseInt(this.trasladosCamaCuenta.get("cama_"+i)+""),tipoMonitoreo);
						//se verifica que se haya encontrado servicio
						if(codigoServicio>0)
						{
							//para el caso de Estancia Por Paciente se asigna el centro de costo
							if(this.tipoEstancia==ConstantesBD.codigoTipoEstanciaPorPaciente){
								this.codigoCentroCosto=Integer.parseInt(this.trasladosCamaCuenta.get("centro_costo_"+i)+"");
								this.tipo_entidad_autoriza= this.trasladosCamaCuenta.get("tipo_entidad_ejecuta_"+i)+"";
							}
								
							
							//si se encuentra, se sale del ciclo
							i=numTraslados;
							
						}
						
					}
				}
			}
			//si no hay camas UCI no se toma en cuenta el tipo monitoreo
			else
			{
				//3) **CONSULTA DEL SERVICIO EN LA TABLA SERVICIOS_CAMA********
				codigoServicio=estanciaAutomaticaDao.consultarServicioCama(con,
						Integer.parseInt(this.trasladosCamaCuenta.get("cama_"+i)+""),tipoMonitoreo);
				logger.info("codigo del servicio=> "+codigoServicio);
				//para el caso de Estancia Por Paciente se asigna el centro de costo
				if(this.tipoEstancia==ConstantesBD.codigoTipoEstanciaPorPaciente){
					this.codigoCentroCosto=Integer.parseInt(this.trasladosCamaCuenta.get("centro_costo_"+i)+"");
					this.tipo_entidad_autoriza= this.trasladosCamaCuenta.get("tipo_entidad_ejecuta_"+i)+"";
				}
					
				//se finaliza el ciclo
				i=numTraslados;
			}
		}
		//se revisa si hubo traslados
		if(numTraslados<=0)
			codigoServicio=-2; //indica que la cuenta no tenía traslados del centro de costos parametrizado
		//caso solo cuando son camas UCI
		if(hayCamasUCI)
		{
			//puede suceder que el servicio no se haya encontrado al buscar en la información de las camas
			//entonces el servicio se toma directamente del tipo monitoreo
			//4) ****CONSULTAR EL SERVICIO EN TIPO MONITOREO *******************
			if(codigoServicio==0)
			{
				//se verifica que exista
				if(!this.tipoMonitoreo.getDescripcion().equals(""))
				{
					codigoServicio=Integer.parseInt(this.tipoMonitoreo.getDescripcion());
					//para el caso de Estancia Por Paciente se asigna el centro de costo
					if(this.tipoEstancia==ConstantesBD.codigoTipoEstanciaPorPaciente){
						//se toma el centro de costo del último traslado
						this.codigoCentroCosto=Integer.parseInt(this.trasladosCamaCuenta.get("centro_costo_"+(numTraslados-1))+"");
						this.tipo_entidad_autoriza= this.trasladosCamaCuenta.get("tipo_entidad_ejecuta_"+(numTraslados-1))+"";
					}
				}
				else
				{
					//se genera la inconsistencia
					this.generarInconsistencias("B","tipo de monitoreo "+Utilidades.getTipoMonitoreo(con,this.getTipoMonitoreo().getCodigo())[1]+" sin servicio asociado",null,indica);
					codigoServicio=-1;
				}
			}
		}
		
		return codigoServicio;
	}
	/**
	 * Método para editar la estructura donde se almacenan
	 * las inconsistencias, existen 2 tipos:
	 * A => inconsistencias hechas cuando no se genera cargo pendiente,
	 * 		pero sí se genera solicitud
	 * B => inconsistencias cuando no se generan solicitud ni cargo. 
	 * @param tipoInconsistencia
	 * @param motivo
	 */
	public void generarInconsistencias(String tipoInconsistencia, String motivo,String orden, String indica) {
		int posicion=0;
		
		//si aún no existe se genera una nueva
		if(this.inconsistencias.get(tipoInconsistencia)==null)
		{
			this.inconsistencias.put(tipoInconsistencia,1+"");
		}
		else
		{
			posicion=Integer.parseInt(this.inconsistencias.get(tipoInconsistencia)+"");
			//se agrega una posición más
			this.inconsistencias.put(tipoInconsistencia,(posicion+1)+"");
		}
		this.inconsistencias.put("paciente_"+tipoInconsistencia+"_"+posicion,this.paciente);
		this.inconsistencias.put("cuenta_"+tipoInconsistencia+"_"+posicion,this.idCuenta+"");
		this.inconsistencias.put("fecha_"+tipoInconsistencia+"_"+posicion,UtilidadFecha.conversionFormatoFechaAAp(this.fechaInicialEstancia));
		this.inconsistencias.put("indica_"+tipoInconsistencia+"_"+posicion,indica);
		//este caso aplica para inconsistencias de tipo A
		if(orden!=null)
			this.inconsistencias.put("orden_"+tipoInconsistencia+"_"+posicion,orden);
		this.inconsistencias.put("motivo_"+tipoInconsistencia+"_"+posicion,motivo);
		//SE HABILITA INDICADOR DE INCONSISTENCIAS***********
		this.huboInconsistencias=true;
	}
	
	/**
	 * Método usaod para generar el archivo de inconsistencias
	 * @return
	 */
	public String generarArchivoInconsistencias(Connection con,UsuarioBasico usuario)
	{
		try
		{
			String contenido=""; //variable para almacenar el contenido del archivo
			String aux=""; //variable auxiliar
			int numRegistros=0;
			String separador=System.getProperty("file.separator"); //separador
			//preparación del nombre del archivo *******************************
			String hora=UtilidadFecha.convertirHoraACincoCaracteres(UtilidadFecha.getHoraActual());
			String vectorFecha[]=UtilidadFecha.getFechaActual().split("/");
			String vectorHora[]=hora.split(":");
			this.nombreArchivo="InconEstancia-"+vectorFecha[0]+vectorFecha[1]+vectorFecha[2]+vectorHora[0]+vectorHora[1]+(this.tipoEstancia==ConstantesBD.codigoTipoEstanciaPorArea?"-"+this.codigoCentroCosto:"")+".txt";
			//se valida el directorio*******************************************************
			File directorio=new File(LogsAxioma.getRutaLogs()+separador+ConstantesBD.logFolderModuloFacturacion+separador+"logEstanciaAutomatica");
			if(!directorio.isDirectory() && !directorio.exists())	{
				if(!directorio.mkdirs()) {
					logger.error("Error creando el directorio "+LogsAxioma.getRutaLogs());
					return "¡Error Interno al generar al archivo "+this.nombreArchivo+" !";
				}
			}
			//apertura del archivo Inconsistencias********************************
			File archivoIncon= new File(directorio,this.nombreArchivo);
			FileWriter streamIncon=new FileWriter(archivoIncon,true); 
			BufferedWriter bufferIncon=new BufferedWriter(streamIncon);
			//Generación del Encabezado del Archivo ********************************
				//1) asignación del nombre de la institucion
				ParametrizacionInstitucion institucion=new ParametrizacionInstitucion();
				institucion.cargar(con,usuario.getCodigoInstitucionInt());
				contenido+=institucion.getRazonSocial()+"\n";
				//2) Tipo de estancia a generar
				if(this.tipoEstancia==ConstantesBD.codigoTipoEstanciaPorArea)
					contenido+="Generación de Estancia por Área: "+this.nombreCentroCosto+"    Fecha: "+UtilidadFecha.conversionFormatoFechaAAp(this.fechaInicialEstancia)+"\n\n\n";
				else
					contenido+="Generación de Estancia por Paciente: "+this.paciente+"    Fechas: "+this.fechaInicialEstancia+" a "+this.fechaFinalEstancia+"\n\n\n";
			//Generación PRIMERA SECCION (A): PACIENTES CON CARGO PENDIENTE DE GENERACION *********
				//se verifica si hay inconsistencias registradas
				if(this.inconsistencias.get("A")!=null)
				{
					//1) se edita el encabezado de la tabla
					numRegistros=Integer.parseInt(this.inconsistencias.get("A")+"");
					contenido+="Pacientes con Cargo pendiente de Generación:\n";
					contenido+=this.editarEspacios("Paciente",8,40,false);
					contenido+=this.editarEspacios("Cuenta",6,10,false);
					contenido+=this.editarEspacios("Fecha",5,12,false);
					contenido+=this.editarEspacios("Ind. Generación", 15, 17, false);
					contenido+="Orden Médica   ";
					contenido+="Motivo\n";
					//2) se edita contenido de la tabla
					for(int i=0;i<numRegistros;i++)
					{
						aux=inconsistencias.get("paciente_A_"+i)+"";
						contenido+=this.editarEspacios(aux,aux.length(),40,false);
						aux=inconsistencias.get("cuenta_A_"+i)+"";
						contenido+=this.editarEspacios(aux,aux.length(),10,false);
						aux=inconsistencias.get("fecha_A_"+i)+"";
						contenido+=this.editarEspacios(aux,aux.length(),12,false);
						aux=inconsistencias.get("indica_A_"+i)+"";
						contenido+=this.editarEspacios(aux,aux.length(),17,false);
						aux=inconsistencias.get("orden_A_"+i)+"";
						contenido+=this.editarEspacios(aux,aux.length(),15,false);
						contenido+=inconsistencias.get("motivo_A_"+i)+"\n";
					}
					contenido+="\n\n";
				}
			//clausura de archivo inconsistencias**************************************
				//se verifica si hay inconsistencias registradas
				if(this.inconsistencias.get("B")!=null)
				{
					//1) se edita el encabezado de la tabla
					numRegistros=Integer.parseInt(this.inconsistencias.get("B")+"");
					contenido+="Pacientes sin Solicitud y Cargo:\n";
					contenido+=this.editarEspacios("Paciente",8,40,false);
					contenido+=this.editarEspacios("Cuenta",6,10,false);
					contenido+=this.editarEspacios("Fecha",5,12,false);
					contenido+=this.editarEspacios("Ind. Generación", 15, 20, false);
					contenido+="Motivo\n";
					//2) se edita contenido de la tabla
					for(int i=0;i<numRegistros;i++)
					{
						aux=inconsistencias.get("paciente_B_"+i)+"";
						contenido+=this.editarEspacios(aux,aux.length(),40,false);
						aux=inconsistencias.get("cuenta_B_"+i)+"";
						contenido+=this.editarEspacios(aux,aux.length(),10,false);
						aux=inconsistencias.get("fecha_B_"+i)+"";
						contenido+=this.editarEspacios(aux,aux.length(),12,false);
						aux=inconsistencias.get("indica_B_"+i)+"";
						contenido+=this.editarEspacios(aux,aux.length(),20,false);
						contenido+=inconsistencias.get("motivo_B_"+i)+"\n";
					}
					contenido+="\n\n";
				}
			//se escirbe el contenido
			bufferIncon.write(contenido);
			bufferIncon.close();
			return archivoIncon.getAbsolutePath();
		
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo Incon al generarlo: "+e);
			return "¡Error Interno al generar al archivo "+this.nombreArchivo+"!";
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo Incon: "+e);
			return "¡Error Interno al generar al archivo "+this.nombreArchivo+"!";
		}
	}
	/**
	 * Método para editar los espacios del campo en el archivo Inconsistencias
	 * @param campo
	 * @param tamano
	 * @param limite
	 * @param antes si es true los espacios se añaden antes del texto
	 * @return
	 */
	private String editarEspacios(String campo, int tamano, int limite,boolean antes)
	{
		
		String aux;
		String espacios="";
		
		for(int i=tamano+1;i<=limite;i++)
			espacios+=" ";
		
		if(antes)
			aux=espacios+campo;
		else
			aux=campo+espacios;
		
		return aux;
	}
	/**
	 * Método para consultar el tipo de Monitoreo en la búsqueda
	 * del servicio de una cama UCI
	 * @param con
	 * @param tipoMonitoreo
	 * @return
	 */
	private int obtenerTipoMonitoreo(Connection con, int tipoMonitoreo) {
		
		//se verifica que no se tenga el tipo de monitoreo
		//de lo contrario se deja el tipo monitoreo que ya se tenía
		if(!hayTipoMonitoreo)
		{
			//se consultan los tipos de monitoreo del paciente
			 HashMap tiposMonitoreo=estanciaAutomaticaDao.consultarTiposMonitoreo(con,this.idCuenta,this.fechaInicialEstancia);
			int numTiposMonitoreo=Integer.parseInt(tiposMonitoreo.get("numRegistros")+"");
			logger.info("Número de tipos de monitoreo consultados desde el principio "+numTiposMonitoreo);
			//caso 1) no se encontró tipo de monitoreo
			if(numTiposMonitoreo<=0)
				tipoMonitoreo=0;
			//caso 2) solo se encontró 1 tipo de monitoreo
			else if(numTiposMonitoreo==1)
			{
				tipoMonitoreo=Integer.parseInt(tiposMonitoreo.get("tipo_monitoreo_0")+"");
				//se llena el InfoDatos para registrar los datos 
				//del tipo monitoreo
				this.tipoMonitoreo.setCodigo(tipoMonitoreo);
				//se registra la prioridad de cobro
				this.tipoMonitoreo.setNombre(tiposMonitoreo.get("prioridad_0")+"");
				//se registra su servicio
				this.tipoMonitoreo.setDescripcion(tiposMonitoreo.get("servicio_0")+"");
				//se alerta de que ya se tiene tipo de monitoreo
				this.hayTipoMonitoreo=true;
			}
			//caso 3 se encontraron varios tipos de monitoreo
			//por tal motivo se debe evaluar el de mayor prioridad
			else
				tipoMonitoreo=this.elegirTipoMonitoreoPrioritario(tiposMonitoreo);
				
		}
		
		return tipoMonitoreo;
		
	}
	/**
	 * Método para verificar cual es tipo de monitoreo de mayor prioridad
	 * @param mapa
	 * @return
	 */
	private int elegirTipoMonitoreoPrioritario(HashMap mapa) {
		String prioridad="";
		int maximaPrioridad=100;
		int tipoMonitoreo=0;
		int numTiposMonitoreo=Integer.parseInt(mapa.get("numRegistros")+"");
		logger.info("Número de Tipos de monitoreo consultados=> "+numTiposMonitoreo);
		for(int i=0;i<numTiposMonitoreo;i++)
		{
			prioridad=mapa.get("prioridad_"+i)+"";
			//se revisa que los tipos de monitoreo tengan prioridad
			logger.info("la prioridad["+i+"] es: "+prioridad);
			if(prioridad.equals("")||prioridad.equals("null"))
			{
				//se toma el primer tipo de monitoreo
				if(i==0)
				{
					tipoMonitoreo=Integer.parseInt(mapa.get("tipo_monitoreo_"+i)+"");
					//se llenan los datos del tipo monitoreo en el InfoDatos
					this.tipoMonitoreo.setCodigo(tipoMonitoreo);
					//se toma la prioridad del monitoreo
					this.tipoMonitoreo.setNombre(mapa.get("prioridad_"+i)+"");
					//se toma el servicio del monitoreo
					this.tipoMonitoreo.setDescripcion(mapa.get("servicio_"+i)+"");
					//se alerta de que ya se tiene tipo monitoreo
					this.hayTipoMonitoreo=true;
				}
				//se termina el ciclo
				i=numTiposMonitoreo;
			}
			//de lo contrario los tipos de monitoreo tienen prioridad
			else
			{
				//entre menor sea el número, es más alta la prioridad
				if(Integer.parseInt(prioridad)<maximaPrioridad)
				{
					//se asigna una nueva máxima prioridad
					maximaPrioridad=Integer.parseInt(prioridad);
					//se asigna el tipo monitoreo de esa prioridad
					tipoMonitoreo=Integer.parseInt(mapa.get("tipo_monitoreo_"+i)+"");
					//se llenan los datos del tipo monitoreo en el InfoDatos
					this.tipoMonitoreo.setCodigo(tipoMonitoreo);
					//se toma la prioridad del monitoreo
					this.tipoMonitoreo.setNombre(mapa.get("prioridad_"+i)+"");
					//se toma el servicio del monitoreo
					this.tipoMonitoreo.setDescripcion(mapa.get("servicio_"+i)+"");
					///se alerta de que ya se tiene tipo monitoreo
					this.hayTipoMonitoreo=true;
				}
			}
			
		}
		return tipoMonitoreo;
	}
	/**
	 * Método usado para verificar si los traslados cama de la cuenta
	 * tienen camas UCI
	 * @return
	 */
	private boolean verificacionCamasUCI() {
		int numRegistros=Integer.parseInt(this.trasladosCamaCuenta.get("numRegistros")+"");
		boolean indicador=false;
		String aux=""; //variable auxiliar
		for(int i=0;i<numRegistros;i++)
		{
			aux=this.trasladosCamaCuenta.get("es_uci_"+i)+"";
			if(aux.equals("t")||aux.equals("true")||aux.equals("1"))
				indicador=true;
		}
		return indicador;
	}
	
	/**
	 * Método implementado para cargar el archivo de inconsistencias de la estancia
	 * y almacenarlo en un HashMap
	 * @param path
	 * @return
	 */
	public HashMap cargarArchivoInconsistencias(String path)
	{
		HashMap lineas = new HashMap();
		try
		{
			int contador = 0;
			String cadena = "";
			//******SE INICIALIZA ARCHIVO*************************
			File archivo=new File(path);
			FileReader stream=new FileReader(archivo); //se coloca false para el caso de que esté repetido
			BufferedReader buffer=new BufferedReader(stream);
			
			//********SE RECORRE LÍNEA POR LÍNEA**************
			cadena=buffer.readLine();
			while(cadena!=null)
			{
				
				lineas.put(contador+"",cadena);
				contador++;
				
				cadena=buffer.readLine();
			}
			lineas.put("numRegistros",contador);
			
			//***************CERRAR ARCHIVO****************************
			buffer.close();
			
			return lineas;
		}
		catch(FileNotFoundException e)
		{
			logger.error("No se pudo encontrar el archivo "+path+" al cargarlo: "+e);
			lineas.put("numRegistros","0");
			return lineas;
		}
		catch(IOException e)
		{
			logger.error("Error en los streams del archivo "+path+" al cargarlo: "+e);
			lineas.put("numRegistros","0");
			return lineas;
		}
	}
	
	/**
	 * Método usado para registrar el LOG tipo Base de Datos del proceso
	 * de generación de Estancia Automática
	 * @param con
	 * @param tipo
	 * @param fechaInicialEstancia
	 * @param fechaFinalEstancia
	 * @param usuario
	 * @param institucion
	 * @param area
	 * @param paciente
	 * @param inconsistencia
	 * @param reporte
	 * @return
	 */
	public int insertarLogEstanciaAutomatica(Connection con,
			int tipo,String usuario,int institucion,int area,int paciente,int centroAtencion, String indica)
	{
		logger.info("datos para la insercion >>> \n "+tipo+usuario+institucion+area+paciente+centroAtencion+indica+"hubo inconsistencias?"+this.huboInconsistencias+"nombre archivo"+this.nombreArchivo);
		String fecha="";
		if(area>=0&&paciente<=0)
			fecha=this.fechaInicialEstancia;
		else
			fecha=this.fechaFinalEstancia;
		return estanciaAutomaticaDao.insertarLogEstanciaAutomatica(con,
				tipo,
				this.fechaInicialEstancia,
				fecha,
				usuario,
				institucion,
				area,
				paciente,
				centroAtencion,
				this.huboInconsistencias,
				this.nombreArchivo,
				indica);
	}
	
	/**
	 * Método que consulta y verifica que sobre la cuenta se
	 * pueda generar estancia automática para el día asignado.
	 * En el caso de que no se pueda generar estancia para ese día
	 * el método retorna un HashMap vacío
	 * @param con
	 * @param idCuenta
	 * @param fechaEstancia
	 * @param institucion
	 * @return
	 */
	public HashMap consultaCuentaEstanciaPorPaciente(Connection con,int institucion)
	{
		return estanciaAutomaticaDao.consultaCuentaEstanciaPorPaciente(con,this.idCuenta,this.fechaInicialEstancia,institucion);
	}
	
	/**
	 * Método usaod para consultar los registros del registro LOG
	 * de Estancia Automática
	 * @param con
	 * @param anio
	 * @param mes
	 * @param area
	 * @param tipo
	 * @param usuario
	 * @param centroAtencion
	 * @return
	 */
	public HashMap consultaLogEstanciaAutomatica(Connection con,int anio,int mes,int area,int tipo,String usuario,int centroAtencion, String indica)
	{
		return estanciaAutomaticaDao.consultaLogEstanciaAutomatica(con,anio,mes,area,tipo,usuario,centroAtencion, indica);
	}
	
	/**
	 * Método que consulta el numero de estancias generadas para una cuenta 
	 * en un rango de fechas determinado
	 * campos del mapa:
	 * idCuenta
	 * fechaInicial
	 * fechaFinal
	 * @param con
	 * @param campos
	 * @return
	 */
	public int numeroEstanciasPaciente(Connection con,HashMap campos)
	{
		return estanciaAutomaticaDao.numeroEstanciasPaciente(con,campos);
	}
	//**************GETTERS & SETTERS*********************************
	/**
	 * @return Returns the codigoCentroCosto.
	 */
	public int getCodigoCentroCosto() {
		return codigoCentroCosto;
	}
	/**
	 * @param codigoCentroCosto The codigoCentroCosto to set.
	 */
	public void setCodigoCentroCosto(int codigoCentroCosto) {
		this.codigoCentroCosto = codigoCentroCosto;
	}
	/**
	 * @return Returns the cuentasEstancia.
	 */
	public HashMap getCuentasEstancia() {
		return cuentasEstancia;
	}
	/**
	 * @param cuentasEstancia The cuentasEstancia to set.
	 */
	public void setCuentasEstancia(HashMap cuentasEstancia) {
		this.cuentasEstancia = cuentasEstancia;
	}
	/**
	 * @return Returns the fechaFinalEstancia.
	 */
	public String getFechaFinalEstancia() {
		return fechaFinalEstancia;
	}
	/**
	 * @param fechaFinalEstancia The fechaFinalEstancia to set.
	 */
	public void setFechaFinalEstancia(String fechaFinalEstancia) {
		this.fechaFinalEstancia = fechaFinalEstancia;
	}
	/**
	 * @return Returns the fechaInicialEstancia.
	 */
	public String getFechaInicialEstancia() {
		return fechaInicialEstancia;
	}
	/**
	 * @param fechaInicialEstancia The fechaInicialEstancia to set.
	 */
	public void setFechaInicialEstancia(String fechaInicialEstancia) {
		this.fechaInicialEstancia = fechaInicialEstancia;
	}
	/**
	 * @return Returns the idCuenta.
	 */
	public int getIdCuenta() {
		return idCuenta;
	}
	/**
	 * @param idCuenta The idCuenta to set.
	 */
	public void setIdCuenta(int idCuenta) {
		this.idCuenta = idCuenta;
	}
	
	/**
	 * @return Returns the paciente
	 */
	public String getPaciente() {
		return paciente;
	}
	/**
	 * @param paciente The paciente to set
	 */
	public void setPaciente(String paciente) {
		this.paciente = paciente;
	}
	/**
	 * @return Returns the tipoMonitoreo
	 */
	public InfoDatosInt getTipoMonitoreo() {
		return tipoMonitoreo;
	}
	/**
	 * @param tipoMonitoreo the tipoMonitoreo to set
	 */
	public void setTipoMonitoreo(InfoDatosInt tipoMonitoreo) {
		this.tipoMonitoreo = tipoMonitoreo;
	}
	public boolean isHuboInconsistencias() {
		return huboInconsistencias;
	}
	public void setHuboInconsistencias(boolean huboInconsistencias) {
		this.huboInconsistencias = huboInconsistencias;
	}
	public String getNombreCentroCosto() {
		return nombreCentroCosto;
	}
	public void setNombreCentroCosto(String nombreCentroCosto) {
		this.nombreCentroCosto = nombreCentroCosto;
	}
	public String getNombreArchivo() {
		return nombreArchivo;
	}
	public void setNombreArchivo(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}
	public boolean isHayCamasUCI() {
		return hayCamasUCI;
	}
	public void setHayCamasUCI(boolean hayCamasUCI) {
		this.hayCamasUCI = hayCamasUCI;
	}
	/**
	 * @return Returns the tipoEstancia.
	 */
	public int getTipoEstancia() {
		return tipoEstancia;
	}
	/**
	 * @param tipoEstancia The tipoEstancia to set.
	 */
	public void setTipoEstancia(int tipoEstancia) {
		this.tipoEstancia = tipoEstancia;
	}
	/**
	 * @return Returns the centroAtencion.
	 */
	public int getCentroAtencion() {
		return centroAtencion;
	}
	/**
	 * @param centroAtencion The centroAtencion to set.
	 */
	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}
	
	
	public void setConvenioResponsablePaciente(int convenioResponsablePaciente) {
		this.convenioResponsablePaciente = convenioResponsablePaciente;
	}
	public int getConvenioResponsablePaciente() {
		return convenioResponsablePaciente;
	}
	public String getTipo_entidad_autoriza() {
		return tipo_entidad_autoriza;
	}
	public void setTipo_entidad_autoriza(String tipo_entidad_autoriza) {
		this.tipo_entidad_autoriza = tipo_entidad_autoriza;
	}
	
	public int getCodigoContratoConvenio() {
		return codigoContratoConvenio;
	}
	public void setCodigoContratoConvenio(int codigoContratoConvenio) {
		this.codigoContratoConvenio = codigoContratoConvenio;
	}
	public int getClasificacionSocioEconomica() {
		return clasificacionSocioEconomica;
	}
	public void setClasificacionSocioEconomica(int clasificacionSocioEconomica) {
		this.clasificacionSocioEconomica = clasificacionSocioEconomica;
	}
	public int getCodigoPaciente() {
		return codigoPaciente;
	}
	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}
	public String getTipoAfiliado() {
		return tipoAfiliado;
	}
	public void setTipoAfiliado(String tipoAfiliado) {
		this.tipoAfiliado = tipoAfiliado;
	}
	public int getNaturalezaPaciente() {
		return naturalezaPaciente;
	}
	public void setNaturalezaPaciente(int naturalezaPaciente) {
		this.naturalezaPaciente = naturalezaPaciente;
	}
	public String getTipoPaciente() {
		return tipoPaciente;
	}
	public void setTipoPaciente(String tipoPaciente) {
		this.tipoPaciente = tipoPaciente;
	}
	public Double getPorcentajeMontoCobro() {
		return porcentajeMontoCobro;
	}
	public void setPorcentajeMontoCobro(Double porcentajeMontoCobro) {
		this.porcentajeMontoCobro = porcentajeMontoCobro;
	}
	public String getTipoDetalleMonto() {
		return tipoDetalleMonto;
	}
	public void setTipoDetalleMonto(String tipoDetalleMonto) {
		this.tipoDetalleMonto = tipoDetalleMonto;
	}
	public Double getValorMontoGeneral() {
		return valorMontoGeneral;
	}
	public void setValorMontoGeneral(Double valorMontoGeneral) {
		this.valorMontoGeneral = valorMontoGeneral;
	}
	public Double getPorcentajeMontoGeneral() {
		return porcentajeMontoGeneral;
	}
	public void setPorcentajeMontoGeneral(Double porcentajeMontoGeneral) {
		this.porcentajeMontoGeneral = porcentajeMontoGeneral;
	}
	public Integer getTipoMonto() {
		return tipoMonto;
	}
	public void setTipoMonto(Integer tipoMonto) {
		this.tipoMonto = tipoMonto;
	}
	public Integer getCantidadMontoGeneral() {
		return cantidadMontoGeneral;
	}
	public void setCantidadMontoGeneral(Integer cantidadMontoGeneral) {
		this.cantidadMontoGeneral = cantidadMontoGeneral;
	}
	
	public Integer getCodDetalleMontoCobro() {
		return codDetalleMontoCobro;
	}
	public void setCodDetalleMontoCobro(Integer codDetalleMontoCobro) {
		this.codDetalleMontoCobro = codDetalleMontoCobro;
	}
	public boolean isGeneroEstancia() {
		return generoEstancia;
	}
	public void setGeneroEstancia(boolean generoEstancia) {
		this.generoEstancia = generoEstancia;
	}
	public Integer getNumeroSolicitud() {
		return numeroSolicitud;
	}
	public void setNumeroSolicitud(Integer numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}
	
	
	/**
	 * Método para insertar una solicitud estancia en la tabla solicitudes
	 * @param con
	 * @param pos
	 * @param mapaCuentas
	 * @return
	 */
	private int insertarSolicitudEstancia(Connection con, int pos, HashMap mapaCuentas, String estadoCon) {
		//se instancia el objeto solicitud
		Solicitud solicitud=new Solicitud();
		solicitud.clean();
		solicitud.setFechaSolicitud(this.getFechaInicialEstancia());
		solicitud.setHoraSolicitud(UtilidadFecha.getHoraActual());
		solicitud.setTipoSolicitud(new InfoDatosInt(ConstantesBD.codigoTipoSolicitudEstancia));
		solicitud.setEspecialidadSolicitante(new InfoDatosInt(ConstantesBD.codigoEspecialidadMedicaNinguna));
		solicitud.setOcupacionSolicitado(new InfoDatosInt(ConstantesBD.codigoOcupacionMedicaNinguna));
		solicitud.setCentroCostoSolicitante(new InfoDatosInt(Integer.parseInt(mapaCuentas.get("centro_costo_solicitante_"+pos)+"")));
		solicitud.setCentroCostoSolicitado(new InfoDatosInt(Integer.parseInt(mapaCuentas.get("centro_costo_solicitado_"+pos)+"")));
	    solicitud.setCodigoCuenta(Integer.parseInt(mapaCuentas.get("cuenta_"+pos)+""));
	    solicitud.setCobrable(true);
	    solicitud.setVaAEpicrisis(false);
	    solicitud.setUrgente(false);
	    //solicitud.setEstadoFacturacion(new InfoDatosInt(ConstantesBD.codigoEstadoFPendiente));
	    try
	    {
	        int numeroSolicitud=solicitud.insertarSolicitudGeneralTransaccional(con, estadoCon);
	        //se instancia este objeto para cambiar el estado Historia Clinica de la solicitud
	        DespachoMedicamentos despacho=new DespachoMedicamentos();
	        despacho.setNumeroSolicitud(numeroSolicitud);
	        //se cambia el estado Historia Clínica de la solicitud
	        int resp=despacho.cambiarEstadoMedicoSolicitudTransaccional(con,ConstantesBD.continuarTransaccion,ConstantesBD.codigoEstadoHCCargoDirecto/*,""*/);
	        if(resp<=0)
	        	return 0;
	        else
	        	return numeroSolicitud;
	    }
	    catch(SQLException sqle)
	    {
	        logger.warn("Error en la transaccion del insert en la solicitud básica");
			return 0;
	    }
	}
	
	/* Método para insertar el cargo directo de la solicitud de Estancia
	 * @param con
	 * @param numeroSolicitud
	 * @param usuario
	 * @param servicio
	 * @return
	 */
	private int insertarCargoDirectoEstancia(Connection con, int numeroSolicitud, UsuarioBasico usuario, int servicio) 
	{
		//se instancia el objeto Cargo
		CargosDirectos cargo=new CargosDirectos();
		cargo.llenarMundoCargoDirecto(numeroSolicitud,usuario.getLoginUsuario(),ConstantesBD.codigoTipoRecargoSinRecargo,servicio,"",true,"");
			//se realiza la inserción de los datos del cargo directo
		return cargo.insertar(con);
	}
	

	
	
	/**
	 * @param con
	 * @param numeroSolicitud
	 * @param usuario
	 * @param servicio
	 * @param pos
	 * @param mapaCuentas
	 * @return
	 */
	private int insertarCargoEstancia(Connection con, int numeroSolicitud, UsuarioBasico usuario, int servicio, int pos, HashMap mapaCuentas, String indica) {
		try
		{
			int idCuenta=Integer.parseInt(mapaCuentas.get("cuenta_"+pos)+"");
			String ingreso = mapaCuentas.get("ingreso_"+pos).toString();
			int centroCosto=Integer.parseInt(mapaCuentas.get("centro_costo_solicitado_"+pos)+"");
			int codigoPaciente=Integer.parseInt(mapaCuentas.get("codigo_paciente_"+pos)+"");
			//se cargan los datos de la cuenta
			//se cargan los datos del contrato
			//debo hacer uan nueva implementación para obtener información del
			//contrato y generar el cargo
			//se crea instancia de Cargo
			/*Cargo cargo=new Cargo();
			//******GENERACIÓN DEL CARGO DE LA ESTANCIA********************
			String[] mensajes=cargo.generarCargoTransaccional(
					con,
					numeroSolicitud,
					centroCosto,
					contrato.getCodigo(),
					contrato.isEstaVencido(),
					Integer.parseInt(cuenta.getCodigoViaIngreso()),
					ConstantesBD.codigoTipoSolicitudEstancia,
					contrato.getCodigoTipoTarifarioProcedimiento(),
					usuario.getLoginUsuario(),
					1,
					0,
					servicio,
					false,
					"",
					ConstantesBD.continuarTransaccion,
					false/*utilizarValorTarifaOpcional,
					-1/*valorTarifaOpcional);*/
			
			//GENERACION DEL CARGO Y SUBCUENTA - EVALUACION COBERTURA
			
			PersonaBasica paciente= new PersonaBasica();
			paciente.cargar(con, codigoPaciente);
			paciente.cargarPacienteXingreso(con, ingreso, usuario.getCodigoInstitucion(), usuario.getCodigoCentroAtencion()+"");
		    Cargos cargos= new Cargos();
		    cargos.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	con, 
																				usuario, 
																				paciente, 
																				false/*dejarPendiente*/, 
																				numeroSolicitud, 
																				ConstantesBD.codigoTipoSolicitudEstancia /*codigoTipoSolicitudOPCIONAL*/, 
																				idCuenta, 
																				centroCosto/*codigoCentroCostoEjecutaOPCIONAL*/, 
																				servicio/*codigoServicioOPCIONAL*/, 
																				1/*cantidadServicioOPCIONAL*/, 
																				ConstantesBD.codigoNuncaValidoDouble/*valorTarifaOPCIONAL*/, 
																				ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
																				/* "" -- numeroAutorzacionOPCIONAL*/
																				""/*esPortatil*/,false,this.getFechaInicialEstancia(),
																				"" /*subCuentaCoberturaOPCIONAL*/);
			
		    Vector<String> mensajes= cargos.getInfoErroresCargo().getMensajesErrorDetalle();
			
		    for(int i=0;i<mensajes.size();i++)
		    	logger.info("mensajes encontrados => "+mensajes.get(i));
		    
			//se instancia un objeto solicitud y se carga para obtener
			//el consecutivo de la orden médica
			Solicitud solicitud=new Solicitud();
			solicitud.cargar(con,numeroSolicitud);
			//se editan las inconsistencias en caso de que no se haya generado cargo
			
			
			/**
		     * Victor Gomez
		     */
		    // Generar Solicitudes de Autorizacion Autormatica
		    //************************************************************************************************
			logger.info("\n\n\nIngreso Automatico de Solicitud de Autorizacion de Estancias\n\n\n");
			ActionErrors errores = new ActionErrors();
			ArrayList<DtoDetAutorizacionEst> dtoaux = new ArrayList<DtoDetAutorizacionEst>();
			dtoaux =Autorizaciones.cargarInfoBasicaDetAutorizacionEstancia(con, 13 , 
		    		cargos.getDtoDetalleCargo().getCodigoSubcuenta()+"", null, null,
		    		ConstantesIntegridadDominio.acronimoAutomatica);
			errores = Autorizaciones.insertarSolAutorizcionAdmEstAutomatica(con, dtoaux,usuario);
			if(errores.isEmpty())
				logger.info("error en la insercion de solicitud de autorizacione de tipo estancia");
			logger.info("\n\n\nFIN Ingreso Automatico de Solicitud de Autorizacion de Estancias\n\n\n");
		    //************************************************************************************************
			
			
			for(int i=0;i<mensajes.size();i++)
			{
				if(mensajes.get(i).equals("error.cargo.contratoVencido"))
					this.generarInconsistencias("A","El contrato especificado en la generación del cargo está vencido",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.cargo.esquemaNoSeleccionado"))
					this.generarInconsistencias("A","El contrato no tiene un esquema tarifario definido",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.cargo.noHayTarifaISS")||
						mensajes.get(i).toString().startsWith("error.cargo.noHayTarifaEsquemaTarifarioISSCita"))
					this.generarInconsistencias("A","No hay tarifa ISS para el servicio dado",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.cargo.noHayTarifaSoat")||
						mensajes.get(i).toString().startsWith("error.cargo.noHayTarifaEsquemaTarifarioSoatCita"))
					this.generarInconsistencias("A","No hay tarifa Soat para el servicio dado",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.cargo.noHayTarifaEsquemaTarifarioISS"))
					this.generarInconsistencias("A","No hay tarifa para el servicio seleccionado del esquema tarifario: ISS",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.cargo.noHayTarifaEsquemaTarifarioSoat"))
					this.generarInconsistencias("A","No hay tarifa para el servicio seleccionado del esquema tarifario: Soat",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.cargo.noHayValorTarifa"))
					this.generarInconsistencias("A","No existe valor de tarifa para el servicio dado",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.cargo.hayRepetidos"))
					this.generarInconsistencias("A","Ya existe un registro en la BD para el servicio - contrato - convenio que desea ingresar",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.cargo.contratoVencidoPaciente"))
					this.generarInconsistencias("A","El contrato del paciente se encuentra vencido",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.cargo.contratoVencidoPaciente"))
					this.generarInconsistencias("A","El contrato del paciente se encuentra vencido",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.tipoComplejidad.noExiste"))
					this.generarInconsistencias("A","El convenio maneja complejidad y la cuenta no tiene asignada un tipo complejidad",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.cargo.noSeEspecificoServicio"))
					this.generarInconsistencias("A","No se especificó servicio en la generación del cargo",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				
			}
			return 1;
		  }
	    catch(SQLException slqe)
	    {
	        logger.warn("Error generando el cargo de la solicitud en EstanciaAutomaticaAction: "+ numeroSolicitud+" "+slqe);
			return 0;
	    }
	    catch(Exception e)
	    {
	        logger.warn("Error generando el cargo de la solicitud en EstanciaAutomaticaAction: "+ numeroSolicitud+" "+e);
			return 0;
	    }
	}
	
	
	/**
	 * Método usado para generar la estancia automática en Area
	 * @param con
	 * @param estanciaAutomaticaForm
	 * @param mapping
	 * @param usuario
	 * @return
	 */
	public void accionGenerarArea(Connection con, String fechaInicialEstancia, int centroAtencion, int institucion) 
	{
		//logger.info("con->"+con.)
		
		
		logger.info("\n\n\n [DATOS GENERADORES DE LA ESTANCIA]\n\n\n[Fecha Inicial Estancia]"+fechaInicialEstancia+"\n[centroAtencion]"+centroAtencion+"\n[Institucion]"+institucion);
		
		this.tipoEstancia = ConstantesBD.codigoTipoEstanciaPorArea;
		//*******se reinicia valor del estado página****************
		EstanciaAutomaticaForm estanciaAutomaticaForm= new EstanciaAutomaticaForm();

		//******SE REALIZA LA CONSULTA INICIAL***************
		
		UsuarioBasico usuario=new UsuarioBasico();
		try {
			usuario.cargarUsuarioBasico(con, 3);
		} catch (SQLException e1) {
			logger.info("\n\n[Error Cargando el usuario basico]\n\n"+e1);
			e1.printStackTrace();
		}
		//Se consultan los centros de costo del centro de atención seleccionado
		HashMap centrosCosto = Utilidades.getCentrosCostoXViaIngresoXCAtencion(con, ConstantesBD.codigoViaIngresoHospitalizacion, centroAtencion, institucion, ConstantesBD.tipoPacienteHospitalizado);
		
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getIncluirTipoPacienteCirugiaAmbulatoria(institucion)))
		{
			HashMap centrosCostoAmb = Utilidades.getCentrosCostoXViaIngresoXCAtencion(con, ConstantesBD.codigoViaIngresoHospitalizacion, centroAtencion, institucion, ConstantesBD.tipoPacienteCirugiaAmbulatoria);
			int numRegistros = Utilidades.convertirAEntero(centrosCosto.get("numRegistros").toString());
			
			for(int i=0;i<Integer.parseInt(centrosCostoAmb.get("numRegistros").toString());i++)
			{
				boolean existe = false;
				//Se verifica que el centro de costo no se encuentre en el listado de centros de costo de Hospitalizado
				for(int j=0;j<Integer.parseInt(centrosCosto.get("numRegistros").toString());j++)
					if(Integer.parseInt(centrosCostoAmb.get("codigo_"+i).toString())==Integer.parseInt(centrosCosto.get("codigo_"+j).toString()))
						existe = true;
				
				if(!existe)
				{
					centrosCosto.put("codigo_"+numRegistros,centrosCostoAmb.get("codigo_"+i));
					centrosCosto.put("nombre_"+numRegistros,centrosCostoAmb.get("nombre_"+i));
					numRegistros++;
				}
			}
			centrosCosto.put("numRegistros", numRegistros);
		}
		logger.info("***************IMPRIMIR MAPA CENTROS COSTO*******************");
		Utilidades.imprimirMapa(centrosCosto);
		
		//*************ITERACION DE CADA CENTRO DE COSTO DEL CENTRO DE ATENCION******************************************************
		for(int j=0;j<Integer.parseInt(centrosCosto.get("numRegistros").toString());j++)
		{
			//Se rerinician las inconsistencias
			this.inconsistencias = new HashMap();
			this.setHuboInconsistencias(false);
			this.nombreArchivo = "";
			
			this.setFechaInicialEstancia(fechaInicialEstancia);
			this.setFechaFinalEstancia(fechaInicialEstancia);
			usuario.setCodigoInstitucion(institucion+"");
		
			this.setCodigoCentroCosto(Integer.parseInt(centrosCosto.get("codigo_"+j).toString()));
			this.setNombreCentroCosto(centrosCosto.get("nombre_"+j).toString());
			estanciaAutomaticaForm.setEstadoPagina("");
			estanciaAutomaticaForm.setPathArchivoInconsistencias("");
			estanciaAutomaticaForm.setInconsistencias(new HashMap());
			estanciaAutomaticaForm.setNumInconsistencias(0);
			String indica="Automatico";
			int numeroTraslados=0;
	
			this.setCentroAtencion(centroAtencion);
			estanciaAutomaticaForm.setCuentasEstancia(this.consultaCuentasEstanciaPorAreaAutomatico(con, institucion));
			//****************************************************
			int numeroCuentas=Integer.parseInt(estanciaAutomaticaForm.getCuentasEstancia().get("numRegistros")+"");
			
			
			logger.info("\n\n\n\n\n	----------> NUMERO DE CUENTAS >>>"+numeroCuentas);
			//se verifica si se encontraron admisiones
			if(numeroCuentas>0)
			{
				//se cambia el estado de la pagina
				estanciaAutomaticaForm.setEstadoPagina("generacion");
				//iteración de cada cuenta consultada
				for(int i=0;i<numeroCuentas;i++)
				{
					//se toman datos generales
					this.setIdCuenta(Integer.parseInt(estanciaAutomaticaForm.getCuentasEstancia().get("cuenta_"+i)+""));
					//se obtiene nombre del paciente para las inconsistencias
					this.setPaciente(estanciaAutomaticaForm.getCuentasEstancia().get("paciente_"+i)+"");
					//se consulta si el paciente tiene traslados
					numeroTraslados=Integer.parseInt(estanciaAutomaticaForm.getCuentasEstancia().get("traslados_"+i)+"");
					//logger.info("Valor del centro de costo solicitado=> "+estanciaAutomaticaForm.getCuentasEstancia().get("centro_costo_solicitado_"+i));
					//se verifica que la cuenta tenga traslados
					//de lo contrario se genera inconsistencia
					if(numeroTraslados>0)
					{
						
						//*****SE CONSULTA EL SERVICIO ASOCIADO A LA CAMA*********
						int servicio=this.obtenerServicioCargoEstancia(con,indica);
						//validaciones según si se tomó el servicio
						//CASO 1) No se encontró el servicio (Inconsistencia)
						if(servicio==0)
						{
							//se genera la inconsistencia
							//se verifica si habían cama sUCI para editar la inconsistencia
							if(this.isHayCamasUCI())
								this.generarInconsistencias("B","Falta definir servicio para el Tipo de Monitoreo "+Utilidades.getTipoMonitoreo(con,this.getTipoMonitoreo().getCodigo())[1],null,indica);
							else
								this.generarInconsistencias("B","Falta definir servicio",null,indica);
							
						}
						//CASO 2) Se encontró el servicio
						else if(servicio>0)
						{
							//Se asigna centro de costo solicitado a HashMap Cuenta
							
							estanciaAutomaticaForm.getCuentasEstancia().put("centro_costo_solicitado_"+i,this.getCodigoCentroCosto()+"");
							//***********SE GENERA LA SOLICITUD *************************************************************************
							//se genera la solicitud
							
							UtilidadBD.iniciarTransaccion(con);
							
							int numeroSolicitud=this.insertarSolicitudEstancia(con,i,estanciaAutomaticaForm.getCuentasEstancia(),ConstantesBD.continuarTransaccion);
							int resp=0;
							//si se insertó la solicitud se hace insert en cargos directos
							if(numeroSolicitud>0)
							{
								resp=this.insertarCargoDirectoEstancia(con,numeroSolicitud,usuario,servicio);
							}
							//**********************************************************************************************************
							//se verifica si la transacción fue exitosa
							if(resp<=0)
							{
								//se genera la inconsistencia
								this.generarInconsistencias("B","Error del sistema al generar la solicitud",null,indica);
								UtilidadBD.abortarTransaccion(con);
							}
							else
							{
								//*********SE GENERA EL CARGO************************************************************************
								resp=this.insertarCargoEstancia(con,numeroSolicitud,usuario,servicio,i,estanciaAutomaticaForm.getCuentasEstancia(),indica);
								if(resp<=0)
								{
									Solicitud solicitud=new Solicitud();
									try
									{
										//se carga datos de la solicitud
										solicitud.cargar(con,numeroSolicitud);
										//se genera la inconsistencia
										this.generarInconsistencias("A","Solicitud sin cargo generado",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
									}
									catch(SQLException e)
									{
										logger.error("Error al cargar solicitud en accionGenerarArea de EstanciaAutomaticaAction: "+e);
										//se genera la inconsistencia de todas maneras
										this.generarInconsistencias("A","Solicitud sin cargo generado","No encontrada",indica);
									}
									UtilidadBD.abortarTransaccion(con);
								}
								//****************************************************************************************************
							}
							UtilidadBD.finalizarTransaccion(con);
						}
					}
					
					
				}
				
				//se verifica si se encontraron estancias
				
				
				//*****GENERACIÓN DEL ARCHIVO DE INCONSISTENCIAS******
				if(this.isHuboInconsistencias())
				{
					//se genera el archivo de las inconsistencias
					estanciaAutomaticaForm.setPathArchivoInconsistencias(this.generarArchivoInconsistencias(con,usuario));
				}
				//******GENERACIÓN DE LOG BASE DE DATOS*****************
				logger.info(">>>>>>>>GENERANDO LOG BASE DE DATOS<<<<<<<<<");
				int respprueba=this.insertarLogEstanciaAutomatica(con,
						ConstantesBD.codigoTipoEstanciaPorArea,
						usuario.getLoginUsuario(),
						institucion,
						this.getCodigoCentroCosto(),
						0,//se ignora el paciente 
						centroAtencion,
						indica); 
				//***********************************************************
			}
			//no se encontraron admisiones
			else
			{
				logger.info("NO TIENE SOLICITUDES");
			}
		}
		
		//*****************************************************************************************************
	}
	
}
