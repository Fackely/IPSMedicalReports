/*
 * Creado en Feb 17, 2006
 */
package com.princetonsa.mundo.enfermeria;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.LogsAxioma;
import util.ResultadoBoolean;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.action.ComunAction;
import com.princetonsa.actionform.enfermeria.RegistroEnfermeriaForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.enfermeria.RegistroEnfermeriaDao;
import com.princetonsa.dto.enfermeria.DtoFrecuenciaCuidadoEnferia;
import com.princetonsa.dto.enfermeria.DtoRegistroAlertaOrdenesMedicas;
import com.princetonsa.mundo.CuentasPaciente;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.atencion.Egreso;


/**
 * @author Andrés Mauricio Ruiz Vélez
 * Princeton S.A. (Parquesoft-Manizales)
 */
public class RegistroEnfermeria
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private static Logger logger = Logger.getLogger(RegistroEnfermeria.class);
		
	/**
	 * Interfaz para acceder a la fuente de datos
	 */
	private RegistroEnfermeriaDao  registroEnfermeriaDao = null;

	//	---------------------------------------------------DECLARACIÓN DE LOS ATRIBUTOS-----------------------------------------------------------//
		
	//----Informacion del encabezado
	/**
	 * Fecha del registro de enfermería
	 */
	private String fechaRegistro;
	
	/**
	 * Fecha del registro de enfermería
	 */
	private String horaRegistro;
	
	
	//------------------Campos de la Seccion Dieta-----------------------------------//
	
	/**
	 * Campo para determinar si el paciente tiene nutrición oral
	 */
	private boolean hayDieta;
	
	/**
	 * Campo para determinar si el paciente tiene nutrición oral
	 */
	private String nutricionOral;
		
	/**
	 * Campo para almacenar los tipos de nutricion oral registrados 
	 */
	private String tiposNutricionOral;
		
	/**
	 * Campo para determinar si el paciente tiene nutrición parenteral
	 */
	private String nutricionParenteral;

	/**
	 * Campo para almacenar la descripcion de la dieta del paciente que se registro desde ordenes medicas
	 */
	private String descripcionDieta;

	/**
     * Mapa para almacenar la informacion de dieta, de los medicamentos.
     */
    private HashMap mapaDieta;
	/**
     * Mapa para almacenar la informacion de dieta, de los medicamentos.
     */
    private HashMap mapaDietaHistorico;
    
    /**
     * Mapa para almacenar la informacion histórica de ORDENES MEDICAS.
     */
    private HashMap mapaDietaOrdenes;

    
    /**
     * Campo para determinar cuando se finalizara un turno de enfermeria en el momento
     * de guardar información.
     */

    private boolean finalizarTurno;
    
    //-----Informacion de finalizacion desde la orden medica de la dieta.
    
    private String fechaGrabacionDietaOrden; 
    private String fechaRegistroDietaOrden; 
    private String medicoDietaOrden; 
    private String observacionDietaParenteOrden;
    

    //---------------------------------Campos de la sección soporte respiratorio
    
    /**
     * Observaciones de los ingresos de soporte respiratorio
     */
    private String obsSoporte; 

    /**
     * Vector con lo datos ingresados de soporte respiratorio
     */
    private Vector soporteRespiratorio;
    
	/**
	 * Descripción del soporte respiratorio de la orden médica
	 */
	private String descripcionSoporteOrdenMedica = "";
    
    //--------------------------------Campos para la seccion nanda
	/**
	 * Diagnosticos de enfermería para ingresar
	 * (Cada elemento es un vector que contiene en la posición 0 el codigo por institución y
	 * en la posición 1 las observaciones no requeridas)
	 */
	private Vector diagnosticosEnfermeria;
	
	 //---------------------------------Campos de la sección Signos Vitales---------------------------------//
    /**
     * Campo frecuencia cardiaca
     */
    private String frecuenciaCardiaca;
    
    /**
     * Campo frecuencia respiratoria
     */
    private String frecuenciaRespiratoria;
    
    /**
     * Campo Presión Arterial Sistólica
     */
    private String presionArterialSistolica;
    
    /**
     * Campo Presión Arterial Diastólica
     */
    private String presionArterialDiastolica;
    
    /**
     * Campo Presión Arterial Media
     */
    private String presionArterialMedia;
    
    /**
     * Campo Temperatura del paciente
     */
    private String temperaturaPaciente;
    
    /**
     * Mapa para almacenar la informacion de los signos vitales parametrizados por 
     * institución centro de costo
     */
    private HashMap mapaSignosVitales = new HashMap();
    
    //---------------------------------Campos de la sección Catéteres y Sondas---------------------------------//
    /**
     * Mapa para guardar la información de los catéteres y sondas ingresados
     * tanto para las columnas fijas como las parametrizadas
     */
    private HashMap mapaCateterSonda = new HashMap();
    
    //---------------------------------Campos de la sección Exámenes Fisicos---------------------------------//
    /**
     * Mapa para almacenar la informacion de los exàmenes fìsicos parametrizados por 
     * institución centro de costo
     */
    private HashMap mapaExamenesFisicos = new HashMap();
    
    //  ---------------------------------Campos de la sección Anotaciones de Enfermería---------------------------------//
    /**
     * Campo que guarda la anotación de enfermería
     */
    private String anotacionEnfermeria;
    
    // --------------------------- Campo de la sección Cuidados Especiales de Enfermaría ------------------------//
    /**
     * Mapa para el manejo de los cuidados especiales de enfermería
     */
    private HashMap mapaCuidadosEspeciales;
    
    //-------------------------Observaciones Generales -------------------------------------------------//
	/**
	 * Observaciones Generales de la Orden Médica
	 */
	private String observacionesOrdenMedica;
	
//	----------------------------------------------Campos de la sección Hoja Neurológica-------------------------------------------------------------//
	/**
	 * Campo que indica si seleccionaron Si o No en Hoja Neurológica en la funcionalidad
	 * Orden Médica
	 */
	private String existeHojaNeurologica; 
	
	/**
	 * Campo que indica si está finalizada la hoja neurológica desde la orden médica
	 */
	private String finalizadaHojaNeurologica;
	
	/**
	 * Campo que guarda la fecha de finalización de la hoja neurológica desde la orden médica
	 */
	private String fechaFinalizacionHNeurologica;
	
	/**
	 * Campo que guarda el nombre del médico y registro que finalizó la hoja neurológica 
	 * desde la orden médica
	 */
	private String medicoHNeurologica;
	
	/**
	 * Campo que guarda las especialidades del médico que finalizó la hoja neurológica 
	 * desde la orden médica
	 */
	private String fechaGrabacionHojaNeuro;
	
	 /**
     * Mapa para almacenar la informacion de la subsección escala glasgow
     */
    private HashMap mapaEscalaGlasgow = new HashMap();
		
    
    
    private String suspendidoEnfermeria;
    
    private String observacionesEnfermeria="";
    
	//---------------------------------------------------FIN DE LA DECLARACIÓN DE LOS ATRIBUTOS-----------------------------------------------------------//
		
    /**
     * Mapa para muestrras de laboratorio.
     */
    private HashMap mapaMuestra = new HashMap();
    
	/**
	 * Constructor de la clase, inicializa en vacío todos los atributos
	 */
	public RegistroEnfermeria ()
	{
		reset();
		this.init(System.getProperty("TIPOBD"));
	}

	/**
	 * Este método inicializa los atributos de la clase con valores vacíos
	 */
	public void reset()
	{
		this.existeHojaNeurologica="";
		this.finalizadaHojaNeurologica="";
		this.fechaFinalizacionHNeurologica="";
		this.medicoHNeurologica="";
		this.fechaGrabacionHojaNeuro="";
	}
	
	/**
	 * Metodo para insertar el registro de emfermeria.
	 * @param con
	 * @param codigoCuenta
	 * @return Consecutivo Registro de Enfermeria 
	 * @throws SQLException
	 */
	public int insertarRegistroEnfermeria(Connection con, int codigoCuenta) throws SQLException 
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (registroEnfermeriaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (registroEnfermeriaDao - insertarRegistroEnfermeria)");
		}
		
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		inicioTrans=myFactory.beginTransaction(con);
		resp1=registroEnfermeriaDao.insertarRegistroEnfermeria(con, codigoCuenta);
		
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
	 * Metodo para insertar el encabezado.
	 * @param con
	 * @param codRegEnfer
	 * @param loginUsuario
	 * @param Datos Medico
	 * @return
	 * @throws SQLException
	 */
	public int insertarEncabezadoRegistroEnfermeria(Connection con, int codigoRegistroEnfermeria, String loginUsuario, String DatosMedico) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (registroEnfermeriaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (registroEnfermeriaDao - insertarEncabezadoRegistroEnfermeria )");
		}
		
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		inicioTrans=myFactory.beginTransaction(con);
		resp1=registroEnfermeriaDao.insertarEncabezadoRegistroEnfermeria(con, codigoRegistroEnfermeria, this.fechaRegistro, this.horaRegistro, loginUsuario, DatosMedico, obsSoporte);
		
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
	 * Método para consultar los históricos de
	 * cuidados de enfermería
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaFin 
	 * @param fechaInicio 
	 * @return Collection con los registros consultados
	 */
	public HashMap consultarCuidadosEnfermeria(Connection con, String cuentas, String fechaInicio, String fechaFin)
	{
		return registroEnfermeriaDao.consultarCuidadosEnfermeria(con, cuentas,fechaInicio,fechaFin);
	}

	/**
	 * Método para consultar el soporte respiratorio
	 * ingresado en la orden médica
	 * @param cuenta Código de la cuenta del paciente
	 * @param fechaInicio
	 * @param incluirAnterior
	 * @param fechaFin
	 * @param cuentaAsocio @todo
	 * @return HashMap con los datos del sporte respiratorio
	 */
	public HashMap consultarSoporteOrden(Connection con, String cuentas, String fechaInicio, boolean incluirAnterior, String fechaFin)
	{
		return registroEnfermeriaDao.consultarSoporteOrden(con, cuentas, fechaInicio, incluirAnterior, fechaFin);
	}

	/**
	 * Consultar Histroricos de soporte respiratorio
	 * @param con
	 * @param cuenta
	 * @param fechaInicio
	 * @param fechaFin
	 * @param codigoCuentaAsocio @todo
	 * @return
	 */
	public HashMap consultarHistoricoSoporteEnfer(Connection con, String cuentas, String fechaInicio, String fechaFin)
	{
		return registroEnfermeriaDao.consultarHistoricoSoporteEnfer(con, cuentas, fechaInicio, fechaFin);
	}
	
	/**
	 * Método para consultar las fechas en las cuales existen
	 * registros de soporte respiratorio
	 * @param con
	 * @param cuenta
	 * @param cuentaAsocio @todo
	 * @return
	 */
	public Collection consultarFechasHistoricoSoporte(Connection con, String cuentas)
	{
		return registroEnfermeriaDao.consultarFechasHistoricoSoporte(con, cuentas);
	}

	/**
	 * Método para consultar las fechas en las cuales existen
	 * registros de diagnósticos de enfermería
	 * @param con
	 * @param cuenta
	 * @param cuentaAsocio @todo
	 * @return Colección de fechas
	 */
	public Collection consultarFechasHistoricoNanda(Connection con, String cuentas)
	{
		return registroEnfermeriaDao.consultarFechasHistoricoNanda(con, cuentas);
	}
	
	
	/**
	 * Crea resgistros en frecuencias cuidados enfermeria para los cuidados que no los poseean
	 * @param Connection con
	 * @param int codigoIngreso
	 * @param Collection datos
	 * */
	public static void actualizarCodigoPkFrecCuidadosEnfer(Connection con,int codigoIngreso,Collection datos,String loginUsuario)
	{			
	 	Iterator iterador=datos.iterator();
	 	while(iterador.hasNext())
	 	{
	 		HashMap fila=(HashMap)iterador.next();
	 		
	 		//No posee registro
	 		if(Utilidades.convertirAEntero(fila.get("codigopkfreccuidenfer").toString()) <= 0)
	 		{
	 			if(fila.get("es_otro").toString().equals("0"))
	 			{
	 				ProgramacionCuidadoEnfer.insertarFrecuenciasCuidados(
	 						con, 
	 						codigoIngreso,
	 						Utilidades.convertirAEntero(fila.get("codigo").toString()), 
	 						ConstantesBD.codigoNuncaValido,
	 						ConstantesBD.codigoNuncaValido, 
	 						ConstantesBD.codigoNuncaValido,
	 						true, 
	 						ConstantesBD.codigoNuncaValido, 
	 						ConstantesBD.codigoNuncaValido,
	 						loginUsuario);
	 			}
	 			else
	 			{
	 				ProgramacionCuidadoEnfer.insertarFrecuenciasCuidados(
	 						con, 
	 						codigoIngreso,
	 						ConstantesBD.codigoNuncaValido,
	 						Utilidades.convertirAEntero(fila.get("codigo").toString()),	 						
	 						ConstantesBD.codigoNuncaValido, 
	 						ConstantesBD.codigoNuncaValido,
	 						true, 
	 						ConstantesBD.codigoNuncaValido, 
	 						ConstantesBD.codigoNuncaValido,
	 						loginUsuario);
	 			}
	 		}
	 	}
	}
	
	/**
	 * Carga el hashmap con los datos del dto
	 * @param HashMap datos
	 * @param ArrayList<DtoFrecuenciaCuidadoEnferia> dto
	 * */
	public static HashMap cargarFrecPeriodoCuidadosEnfer(HashMap datos,ArrayList<DtoFrecuenciaCuidadoEnferia> arrayDto)
	{
		Utilidades.imprimirMapa(datos);
		
		for(int i=0; i<arrayDto.size(); i++)
		{
			if(!arrayDto.get(i).isEsOtroCuidado())
			{
				if(arrayDto.get(i).getFrecuencia() > 0)
				{
					datos.put("frecuencia_"+arrayDto.get(i).getCodigoCuidadoEnferCcInst(),arrayDto.get(i).getFrecuencia());
					datos.put("frecuenciaold_"+arrayDto.get(i).getCodigoCuidadoEnferCcInst(),arrayDto.get(i).getFrecuencia());
				}
				else
				{
					datos.put("frecuencia_"+arrayDto.get(i).getCodigoCuidadoEnferCcInst(),"");
					datos.put("frecuenciaold_"+arrayDto.get(i).getCodigoCuidadoEnferCcInst(),"");
				}
				
				datos.put("tipofrecuencia_"+arrayDto.get(i).getCodigoCuidadoEnferCcInst(),arrayDto.get(i).getTipoFrecuencia());
				datos.put("tipofrecuenciaold_"+arrayDto.get(i).getCodigoCuidadoEnferCcInst(),arrayDto.get(i).getTipoFrecuencia());
				
				if(arrayDto.get(i).getPeriodo() > 0)
				{
					datos.put("periodo_"+arrayDto.get(i).getCodigoCuidadoEnferCcInst(),arrayDto.get(i).getPeriodo());
					datos.put("periodoold_"+arrayDto.get(i).getCodigoCuidadoEnferCcInst(),arrayDto.get(i).getPeriodo());
				}
				else
				{
					datos.put("periodo_"+arrayDto.get(i).getCodigoCuidadoEnferCcInst(),"");
					datos.put("periodoold_"+arrayDto.get(i).getCodigoCuidadoEnferCcInst(),"");
				}
				
				datos.put("tipoperiodo_"+arrayDto.get(i).getCodigoCuidadoEnferCcInst(),arrayDto.get(i).getTipoFrecuenciaPeriodo());
				datos.put("tipoperiodoold_"+arrayDto.get(i).getCodigoCuidadoEnferCcInst(),arrayDto.get(i).getTipoFrecuenciaPeriodo());
				
				datos.put("tieneprogramacion_"+arrayDto.get(i).getCodigoCuidadoEnferCcInst(),arrayDto.get(i).isTieneProgramacion());
				
				datos.put("codigoPkFrecCuidadosEnfer_"+arrayDto.get(i).getCodigoCuidadoEnferCcInst(),arrayDto.get(i).getCodigoPk());
			}
			else
			{
				if(arrayDto.get(i).getFrecuencia() > 0)
				{
					datos.put("frecuencia_"+arrayDto.get(i).getCodigoOtroCuidado(),arrayDto.get(i).getFrecuencia());
					datos.put("frecuenciaold_"+arrayDto.get(i).getCodigoOtroCuidado(),arrayDto.get(i).getFrecuencia());
				}
				else
				{
					datos.put("frecuencia_"+arrayDto.get(i).getCodigoOtroCuidado(),"");
					datos.put("frecuenciaold_"+arrayDto.get(i).getCodigoOtroCuidado(),"");
				}
					
				datos.put("tipofrecuencia_"+arrayDto.get(i).getCodigoOtroCuidado(),arrayDto.get(i).getTipoFrecuencia());
				datos.put("tipofrecuenciaold_"+arrayDto.get(i).getCodigoOtroCuidado(),arrayDto.get(i).getTipoFrecuencia());
				
				if(arrayDto.get(i).getPeriodo() > 0)
				{
					datos.put("periodo_"+arrayDto.get(i).getCodigoOtroCuidado(),arrayDto.get(i).getPeriodo());
					datos.put("periodoold_"+arrayDto.get(i).getCodigoOtroCuidado(),arrayDto.get(i).getPeriodo());
				}
				else
				{
					datos.put("periodo_"+arrayDto.get(i).getCodigoOtroCuidado(),"");
					datos.put("periodoold_"+arrayDto.get(i).getCodigoOtroCuidado(),"");
				}
				
				datos.put("tipoperiodo_"+arrayDto.get(i).getCodigoOtroCuidado(),arrayDto.get(i).getTipoFrecuenciaPeriodo());
				datos.put("tipoperiodoold_"+arrayDto.get(i).getCodigoOtroCuidado(),arrayDto.get(i).getTipoFrecuenciaPeriodo());
				
				datos.put("tieneprogramacion_"+arrayDto.get(i).getCodigoOtroCuidado(),arrayDto.get(i).isTieneProgramacion());				
				
				datos.put("codigoPkFrecCuidadosEnfer_"+arrayDto.get(i).getCodigoOtroCuidado(),arrayDto.get(i).getCodigoPk());
			}
		}
		
		return datos;
	}
	
	/**
	 * Actualiza o Inserta las frecuencias y periodo de los Cuidados de Enfermeria
	 * @param Connection con
	 * @param int codigoRegistroEnfermeria
	 * @param HashMap datos
	 * @param ArrayList<DtoFrecuenciaCuidadoEnferia> arrayDto
	 * @param String usuarioModifica
	 * */
	public static boolean insertarModificarCuidadosEnfermeria(
			Connection con,
			int codigoIngreso,
			HashMap datos,
			ArrayList<DtoFrecuenciaCuidadoEnferia> arrayDto,
			String usuarioModifica)
	{
		Vector codigosCuidadosEnfer = (Vector) datos.get("codigosCuidadoEnf");
		Vector tiposCuidadoEnf = (Vector) datos.get("tiposCuidadoEnf");
		logger.info("ENTRO A INSERTAR - MODIFICAR CUIDADOS ENFERMERIA  ");
		
		
		String valor = "";
		int pos = ConstantesBD.codigoNuncaValido,otro = ConstantesBD.codigoNuncaValido;
		
		if (codigosCuidadosEnfer != null 
				&& tiposCuidadoEnf != null)
		{
			for(int i=0; i<codigosCuidadosEnfer.size() && i < tiposCuidadoEnf.size();i++)
			{
				valor = codigosCuidadosEnfer.elementAt(i).toString();
				otro = Utilidades.convertirAEntero(tiposCuidadoEnf.elementAt(i).toString());				
				pos = ProgramacionCuidadoEnfer.getPosArrayFrecuenciaCuidadoEnfer(arrayDto,Utilidades.convertirAEntero(valor),otro==1?true:false);
											
				if(pos < 0)
				{
					logger.info("\n Entro a insertarFrecuenciaCUidados");
					if(ProgramacionCuidadoEnfer.insertarFrecuenciasCuidados(
							con, 
							codigoIngreso,
							otro==0?Utilidades.convertirAEntero(valor):ConstantesBD.codigoNuncaValido,							
							otro==1?Utilidades.convertirAEntero(valor):ConstantesBD.codigoNuncaValido,							
							Utilidades.convertirAEntero(datos.get("frecuencia_"+valor)+""),
							Utilidades.convertirAEntero(datos.get("tipofrecuencia_"+valor)+""),
							true,
							Utilidades.convertirAEntero(datos.get("periodo_"+valor)+""),
							Utilidades.convertirAEntero(datos.get("tipoperiodo_"+valor)+""), 
							usuarioModifica) < 0)
						return false;
				}
				else if(pos >= 0 
						&& ((arrayDto.get(pos).getFrecuencia()!=Utilidades.convertirAEntero(datos.get("frecuencia_"+valor).toString()) && (arrayDto.get(pos).getFrecuencia()-1)!=Utilidades.convertirAEntero(datos.get("frecuencia_"+valor).toString()))
							|| arrayDto.get(pos).getTipoFrecuencia()!=Utilidades.convertirAEntero(datos.get("tipofrecuencia_"+valor).toString())
								|| (arrayDto.get(pos).getPeriodo()!=Utilidades.convertirAEntero(datos.get("periodo_"+valor).toString()) && (arrayDto.get(pos).getPeriodo()-1)!=Utilidades.convertirAEntero(datos.get("periodo_"+valor).toString())) 
									|| arrayDto.get(pos).getTipoFrecuenciaPeriodo()!=Utilidades.convertirAEntero(datos.get("tipoperiodo_"+valor).toString())))
				{
					
					//Se actualiza el anterior registro
					logger.info("\n Entro a ActualizarFrecuenciaCUidados");
					if(ProgramacionCuidadoEnfer.actualizarEstadoFrecuenciasCuidadosRegEnfer(
							con,
							arrayDto.get(pos).getCodigoPk(),
							false,
							usuarioModifica))
					{
						//Se ingresa un nuevo registro por el cambio
						if(ProgramacionCuidadoEnfer.insertarFrecuenciasCuidados(
								con,
								codigoIngreso,
								otro==0?Utilidades.convertirAEntero(valor):ConstantesBD.codigoNuncaValido,
								otro==1?Utilidades.convertirAEntero(valor):ConstantesBD.codigoNuncaValido,
								Utilidades.convertirAEntero(datos.get("frecuencia_"+valor).toString()),
								Utilidades.convertirAEntero(datos.get("tipofrecuencia_"+valor).toString()),
								true,
								Utilidades.convertirAEntero(datos.get("periodo_"+valor).toString()),
								Utilidades.convertirAEntero(datos.get("tipoperiodo_"+valor).toString()),
								usuarioModifica) < 0)
							return false;							
					}
					else
						return false;
				}
				else 
				{
					logger.info("valor de la pos >> "+pos+" frec "+arrayDto.get(pos).getFrecuencia()+" "+Utilidades.convertirAEntero(datos.get("frecuencia_"+valor).toString())+" tipofre "+arrayDto.get(pos).getTipoFrecuencia()+" "+Utilidades.convertirAEntero(datos.get("tipofrecuencia_"+valor).toString())+" per "+arrayDto.get(pos).getPeriodo()+"  "+Utilidades.convertirAEntero(datos.get("periodo_"+valor).toString())+" tipoper "+arrayDto.get(pos).getTipoFrecuenciaPeriodo()+" "+Utilidades.convertirAEntero(datos.get("tipoperiodo_"+valor).toString()));
				}
			}
		}	

		return true;
	}
	
	/**
	 * Metodo para insertar los datos de la seccion dieta 
	 * @param con
	 * @param codigoRegistroEnferia
	 * @param codEncabezado
	 * @param fechaInicio 
	 * @param centroCosto
	 * @param institucion
	 * @param codigoCuentaAsocio  
	 * @throws SQLException
	 */
	public int insertarDieta(Connection con, int codigoRegistroEnferia, String cuentas, int codEncabezado, String loginUsuario, int centroCosto, int institucion, String fechaInicio) throws SQLException
	{
		//se organizan las cuentas
		
		int codigoCuenta=ConstantesBD.codigoNuncaValido;
	    String vectorCuentas[]=cuentas.split(",");
	    int cuentaActual=vectorCuentas.length - 1;
	    if (cuentaActual<0)
	    {cuentaActual=0;}
		codigoCuenta=Utilidades.convertirAEntero(vectorCuentas[cuentaActual]);//se toma la cuenta activa, osea la ultima cuenta del paciente
		
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (registroEnfermeriaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (registroEnfermeriaDao - insertarEncabezadoRegistroEnfermeria )");
		}
		
		//-------------------------------------------------------------------------------------------------------------------
		//----Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		inicioTrans=myFactory.beginTransaction(con);
		
		int nroMed = 0; 
		if ( UtilidadCadena.noEsVacio(this.getMapaDieta("medNuevos")+"") )
		{
			nroMed = Integer.parseInt(this.getMapaDieta("medNuevos")+"");  //-Los Medicamentos Nuevos
		}
		
		int nroMedReg = 0; 
		if ( UtilidadCadena.noEsVacio(this.getMapaDieta("numRegistros")+"") )
		{
			nroMedReg = Integer.parseInt(this.getMapaDieta("numRegistros")+"");  //-Los Medicamentos ya registrados
		}	

		//-------------------------------------------------------------------------------------------------------------------
		//----Barrer el mapa para insertar ( LÍQUIDOS / MEDICAMENTOS INFUSIÓN )
		for (int i = 0; i < nroMed; i++)
		{
			String des = (this.getMapaDieta("med_des_" + i) + "");
			String vol = (this.getMapaDieta("med_vol_" + i) + "");
			String vel = (this.getMapaDieta("med_vel_" + i) + "");
			//String sus = (this.getMapaDieta("med_sus_" + i) + "");
			
			//logger.info("\n\n i " + i + "  des " + des + " vol "  + vol + "  vel  " + vel + "  sus " + sus + "\n");
			
			if ( UtilidadCadena.noEsVacio(des) &&  UtilidadCadena.noEsVacio(des) && UtilidadCadena.noEsVacio(des) )
			{
				resp1=registroEnfermeriaDao.insertarLiqMedInfusion(con, codigoCuenta, codigoRegistroEnferia, des, vol, vel, false, 0 ); //-El cero es para insercion
			}
			else 
			{
				resp1 = 1; ///-----------------------OJO SE DEBE QUIRTAR APENAS SE HASGAN LAS VALIDACIONMES eN EL FORM
			}
			
			//---Para romper el ciclo y hacer el rollback
			if (resp1 < 1) { break; }
		}
		
		//-------------------------------------------------------------------------------------------------------------------
		//----Para insertar el balance de liquidos (El valor registrado para medicamentos parametrizados y no parametrizados)
		nroMedReg = 0; 
		if ( UtilidadCadena.noEsVacio(this.getMapaDieta("nroRegMedAdm")+"") )
		{
			nroMedReg = Integer.parseInt(this.getMapaDieta("nroRegMedAdm")+"");  //-Los Medicamentos 
		}

		for (int i = 0; i < nroMedReg; i++)
		{
			String cod = this.getMapaDieta("codigo_ci_" + i)+"";
			String codCi = this.getMapaDieta("codigo_" + i)+"";
			String param = this.getMapaDieta("param_" + i)+"";
			String codmed = this.getMapaDieta("codmedpaciente_" + i)+"";
			String codmez = this.getMapaDieta("codmezcla_" + i)+"";
			String cant = this.getMapaDieta("h_caj_" + codCi + "_" + param + "_" + codmed + "_" + codmez)+"";
			boolean esmezcla = UtilidadTexto.getBoolean(this.getMapaDieta("esmezcla_" + i)+"");
			
			//logger.info("\n\n esmezcla ["+esmezcla +"]  Parametrizado "  + param  + " codEncabezado  [" + codEncabezado + "]  codCI [" + codCi + "]  cantidad  ["  + cant + "] \n");
			
			if (esmezcla) //-------Insertar datos en la tabla de Balance de Mezclas   
			{
				if ( UtilidadCadena.noEsVacio(cant) )
				{
																				//-Encabezado Registro  //-Codigo Orden  //-Cantidad								
					resp1=registroEnfermeriaDao.insertarBalanceLiquidos(con, 3, codEncabezado, Integer.parseInt(codCi), Float.parseFloat(cant));
				}	
			}
			else
				if ( UtilidadCadena.noEsVacio(cant) )
				{
					//logger.info("\n\n  Parametrizado "  + param  + " codigoMed  [" + cod + "]  codCI [" + codCi + "]  cantidad  ["  + cant + "] \n");
					
					if ( cod.equals("-1") )
					{
						resp1=registroEnfermeriaDao.insertarBalanceLiquidos(con, Integer.parseInt(param), codEncabezado, Integer.parseInt(codCi), Float.parseFloat(cant)); 
					}
					else
					{
						resp1=registroEnfermeriaDao.insertarBalanceLiquidos(con, Integer.parseInt(param), codEncabezado, Integer.parseInt(cod), Float.parseFloat(cant)); 
					}
				}
				else 
				{
					resp1 = 1; ///-----------------------OJO SE DEBE QUIRTAR APENAS SE HASGAN LAS VALIDACIONMES eN EL FORM
				}
			
			//---Para romper el ciclo y hacer el rollback
			if (resp1 < 1) { break; }
		}

		//-------------------------------------------------------------------------------------------------------------------
		//----Para insertar el balance de liquidos (el valor registrado para medicamentos eliminados solamente hay parametrizados)
		nroMedReg = 0; 
		if ( UtilidadCadena.noEsVacio(this.getMapaDieta("nroLiqElim")+"") )
		{
			nroMedReg = Integer.parseInt(this.getMapaDieta("nroLiqElim")+"");  //-Los Medicamentos 
		}


		for (int i = 0; i < nroMedReg; i++)
		{
			String codCI =  mapaDieta.get("codigoelim_" + i) + "";
			String cod =  mapaDieta.get("codigoelim_ci_" + i) + "";
			String valor = mapaDieta.get("h_cajLiqElim_" + codCI) + "";
			   
			//logger.info("\n\n  Codigo "  + cod  + " caj " + valor  + "\n");

			if ( UtilidadCadena.noEsVacio(valor) )
			{
				resp1=registroEnfermeriaDao.insertarBalanceLiquidos(con, 2, codEncabezado, Integer.parseInt(cod), Float.parseFloat(valor)); 
			}
			else 
			{
				resp1 = 1; ///-----OJO SE DEBE QUITAR APENAS SE HASGAN LAS VALIDACIONMES EN EL FORM
			}
			
			//---Para romper el ciclo y hacer el rollback
			if (resp1 < 1) { break; }
		}
				
		
		//-------------------------------------------------------------------------------------------------------------------
		//----Se verificar si se debe FINALIZAR el turno.
		if (this.finalizarTurno)
		{
			String fechaUltimoFinTurno = registroEnfermeriaDao.ultimaHoraFinTurno(con, codigoCuenta);
			logger.info("La Fecha del ultimo turno -----------------------> " + fechaUltimoFinTurno);
			
			boolean hayValoresRegistrados = false;
			
			int codigoTurno = registroEnfermeriaDao.finalizarTurnoBalanceLiquidos (con, codigoRegistroEnferia, this.fechaRegistro, this.horaRegistro, loginUsuario);
			HashMap mp = new HashMap(); 
			
			//-Insertar los detalles de los med NO parametrizados 
			//mp = registroEnfermeriaDao.consultarLiqMedicamentosPaciente(con, codigoCuenta, -1, -1, 3, fechaUltimoFinTurno,"", codigoCuentaAsocio);
			mp = registroEnfermeriaDao.consultarLiqMedicamentosPaciente(con, codigoCuenta+"", centroCosto, institucion, 3, fechaUltimoFinTurno,"");
			//logger.info("\n\n NO PARAMETRIZADOS EL MAPA De La SIUMA   [" + mp.toString()  + "] \n\n");

			if ( UtilidadCadena.noEsVacio(mp.get("numRegistros")+"") )
			{
				int nroReg = Integer.parseInt(mp.get("numRegistros")+"");

				//-Insertar la suma total de los liquidos administrados por medicamento 
				for (int i = 0; i < nroReg; i++)
				{
					hayValoresRegistrados = true;
					
					int codigoMedicamento =  Integer.parseInt(mp.get("codigomed_" + i)+"");
					float valor =  Float.parseFloat(mp.get("valormed_" + i)+"");
					
					//logger.info("\n\n ** El Liquido   [" + codigoMedicamento + "] El Valor ["+ valor + "] \n\n");
					
					resp1=registroEnfermeriaDao.insertarDetFinTurnoBalanceLiquidos(con, codigoTurno, codigoMedicamento, valor, 0); //-El cero final es para que se inserte sobre la tabla no parametrizada

					//---Para romper el ciclo y hacer el rollback
					if (resp1 < 1) { break; }
				}
			}
			
			//-Insertar la suma de los detalles de los med parametrizados
			mp.clear();
			//mp = registroEnfermeriaDao.consultarLiqMedicamentosPaciente(con, codigoCuenta, -1, -1, 4, fechaUltimoFinTurno, "", codigoCuentaAsocio);
			mp = registroEnfermeriaDao.consultarLiqMedicamentosPaciente(con, cuentas, centroCosto, institucion, 4, fechaUltimoFinTurno, "");
			//logger.info("\n\n PARAMETRIZADOS EL MAPA De La SIUMA   [" + mp.toString()  + "] \n\n");
			
			if ( UtilidadCadena.noEsVacio(mp.get("numRegistros")+"") )
			{
				int nroReg = Integer.parseInt(mp.get("numRegistros")+"");
				
				//-Insertar la suma total de los liquidos administrados por medicamento 
				for (int i = 0; i < nroReg; i++)
				{
					hayValoresRegistrados = true;
					
					int codigoMedicamento =  Integer.parseInt(mp.get("codigomed_" + i)+"");
					float valor =  Float.parseFloat(mp.get("valormed_" + i)+"");
					resp1=registroEnfermeriaDao.insertarDetFinTurnoBalanceLiquidos(con, codigoTurno, codigoMedicamento, valor, 1); //-El uno final es para que se inserte sobre la tabla parametrizada

					//---Para romper el ciclo y hacer el rollback
					if (resp1 < 1) { break; }
				}
			}
			
			
			//-------------------------------------------------------------------------------------------------------------------
			//--Insertar de los medicamentos eliminados, primero se debe consultar la suma de los medicamentos desde la 
			//--ultima finalizacion del turno.
			mp.clear();
			//mp = registroEnfermeriaDao.consultarLiqMedicamentosPaciente(con, codigoCuenta, -1, -1, 7, fechaUltimoFinTurno, "", codigoCuentaAsocio);
			mp = registroEnfermeriaDao.consultarLiqMedicamentosPaciente(con, cuentas, centroCosto, institucion, 7, fechaUltimoFinTurno, "");
			if ( UtilidadCadena.noEsVacio(mp.get("numRegistros")+"") )
			{
				int nroReg = Integer.parseInt(mp.get("numRegistros")+"");
				
				//--Insertar la suma total de los liquidos eliminados por medicamento 
				for (int i = 0; i < nroReg; i++)
				{
					hayValoresRegistrados = true;
					
					//int codigoMedicamento =  Integer.parseInt(mp.get("codigomed_" + i)+"");
					int codigoMedicamento =  Integer.parseInt(mp.get("codigomed_ci_" + i)+"");
					float valor =  Float.parseFloat(mp.get("valormed_" + i)+"");

					resp1=registroEnfermeriaDao.insertarDetFinTurnoBalanceLiquidos(con, codigoTurno, codigoMedicamento, valor, 2); //-El Dos es para la insertar sobre la tabla de liquidos eliminados

					//---Para romper el ciclo y hacer el rollback
					if (resp1 < 1) { break; }
				}
			}
			
			//-------INSERTAR LA SUMA DE LAS CANTIDADES DE CADA MEZCLA.  
			mp.clear();
			mp = registroEnfermeriaDao.consultarLiqMedicamentosPaciente(con, cuentas, centroCosto, institucion, 8, fechaUltimoFinTurno, "");
			if ( UtilidadCadena.noEsVacio(mp.get("numRegistros")+"") )
			{
				int nroReg = Integer.parseInt(mp.get("numRegistros")+"");
				
				//--Insertar la suma total de las Mezclas por medicamento 
				for (int i = 0; i < nroReg; i++)
				{
					hayValoresRegistrados = true;

					int codigoMedicamento =  Integer.parseInt(mp.get("codigomed_" + i)+"");  //---Codigo de la Orden Dieta.
					float valor =  Float.parseFloat(mp.get("valormed_" + i)+"");
					logger.info("\n\n Entro a Totalizar [" + codigoMedicamento + "] ["+ valor +"] \n\n");	
					resp1=registroEnfermeriaDao.insertarDetFinTurnoBalanceLiquidos(con, codigoTurno, codigoMedicamento, valor, 3); //-El Tres es para la insertar sobre la tabla de liquidos Mezclas

					//---Para romper el ciclo y hacer el rollback
					if (resp1 < 1) { break; }
				}
			}
			
			
			
			//-Si no hubo algun medicamento registrado de los parametrizados y los NO parametrizados de los eliminados y los adminsitrados  
			//-se debe registra en cero el detalle
			if ( !hayValoresRegistrados )
			{

				//----Colocar en cero todos los medicamentos administrados  	
				HashMap mpf = new HashMap();
				mpf = registroEnfermeriaDao.consultarLiqMedicamentosPaciente(con, cuentas, centroCosto, institucion, 1, "", fechaInicio);

				if ( UtilidadCadena.noEsVacio(mpf.get("numRegistros")+"") )
				{
					int nroReg = Integer.parseInt(mpf.get("numRegistros")+"");

					for (int i = 0; i < nroReg; i++)
					{
						int codigoMedicamento =  Integer.parseInt(mpf.get("codigo_" + i)+"");
						int param = Integer.parseInt(mpf.get("param_" + i)+"");
						boolean  esMezcla = UtilidadTexto.getBoolean(mpf.get("esmezcla_" + i)+"");
						//logger.info("\n\n LA MEZCLA  [" + esMezcla + "] [" + mpf.get("esmezcla_" + i) + "]\n\n");
						
						if (!esMezcla) //-Insertar en los Medicamentos  
							resp1=registroEnfermeriaDao.insertarDetFinTurnoBalanceLiquidos(con, codigoTurno, codigoMedicamento, 0, param);
						else
						{
							resp1=registroEnfermeriaDao.insertarDetFinTurnoBalanceLiquidos(con, codigoTurno, codigoMedicamento, 0, 3);
						}	

						//---Para romper el ciclo y hacer el rollback
						if (resp1 < 1) { break; }
					}

				}
				
				//--------Consultar los medicamentos Eliminados acticos y colocarlos en CERO
				mpf.clear();
				mpf = registroEnfermeriaDao.consultarLiqMedicamentosPaciente(con, cuentas, centroCosto, institucion, 5, "", fechaInicio);
				if ( UtilidadCadena.noEsVacio(mpf.get("numRegistros")+"") )
				{
					int nroReg = Integer.parseInt(mpf.get("numRegistros")+"");

					for (int i = 0; i < nroReg; i++)
					{
						int codigoMedicamento = Integer.parseInt(mpf.get("codigoelim_" + i)+"");

						resp1=registroEnfermeriaDao.insertarDetFinTurnoBalanceLiquidos(con, codigoTurno, codigoMedicamento, 0, 2); 

						//---Para romper el ciclo y hacer el rollback
						if (resp1 < 1) { break; }
					}

				}
			}

			
		}	///---------------Fin de FINALIZAR la Dieta.
		
		//-------------------------------------------------------------------------------------------------------------------
		//---------Verificar si viene un elemento marcado para eliminarlo.
		String label = this.getMapaDieta("nroMedEliminar")+"";
		if ( UtilidadCadena.noEsVacio(label) )
		{
			String cod = this.getMapaDieta(label)+"";
			resp1 = registroEnfermeriaDao.insertarLiqMedInfusion(con, Integer.parseInt(cod), -1,  "", "", "", false, 1 ); //------El uno es para la eliminacion
			if ( resp1 > 0 )
			{

				String nombre = this.getMapaDieta("descripcion_" + label.split("_")[1] )+"";
				String vel = this.getMapaDieta("velocidad_infusion_" + label.split("_")[1] )+"";
				String vol = this.getMapaDieta("volumen_total_" + label.split("_")[1] )+"";
				
				String log = "\n====================================MEDICAMENTO ELIMINADO========================================="; 
				log += "\n NOMBRE :  " + nombre; 
				log += "\n VOLUMEN TOTAL  :  " + vol; 
				log += "\n VELOCIDAD DE INFUSIÓN  :  " + vel; 
				log += "\n=================================================================================================="; 

				//-Generar el log 
				LogsAxioma.enviarLog(ConstantesBD.logRegEnferMedDietaElimCodigo, log, ConstantesBD.tipoRegistroLogModificacion, loginUsuario);
			}
		}
		
		
		//-------------------------------------------------------------------------------------------------------------------
		//-Verificar si hubo cambios sobre los liquidos y medicamentos de infusion para generar el log.
		int nroRegMedNoParam=0;
		if(UtilidadCadena.noEsVacio(mapaDieta.get("numRegistros")+""))
			nroRegMedNoParam = Integer.parseInt( mapaDieta.get("numRegistros")+"");
		
		if ( nroRegMedNoParam > 0 )
		{
		   for(int j = 0 ; j < nroRegMedNoParam ; j ++)
			 { 
		   		String ant = this.getMapaDieta("descripcion_" + j) + "" + this.getMapaDieta("volumen_total_" + j) + "" + this.getMapaDieta("velocidad_infusion_" + j) + ""; 
		   		String des = this.getMapaDieta("h_descripcion_" + j) + "" + this.getMapaDieta("h_volumen_total_" + j) + "" + this.getMapaDieta("h_velocidad_infusion_" + j) + "";
		   		String cod = this.getMapaDieta("codigomedenfer_" + j) + "";
		   		
		   		//----Actualizar el medicamento si los suspendieron
		   		String suspedido = this.getMapaDieta("suspender_" + j) + "";

		   		if (!UtilidadCadena.vNull(this.getMapaDieta("consecutivo_liquido_" + j)+"", "-1"))
		   		{
			   		if ( UtilidadCadena.noEsVacio(suspedido) && suspedido.equals("on") )
			   		{
			   			//----Primero se debe hacer la modificación en la BD
						resp1 = registroEnfermeriaDao.insertarLiqMedInfusion(con, Integer.parseInt(cod), -1,  "" , "", "", true, 3 ); //------El 3 es para suspender el medicamento
	
						//---Para romper el ciclo y hacer el rollback
						if (resp1 < 1) { break; }
			   		}
			   		
			   		//logger.info("\n\n Medicamento ---> consecutivo Liq [ "+  this.getMapaDieta("consecutivo_liquido_" + j) + "]  " + cod +   "   " + des + "   " + suspedido + "   \n\n");
			   		
			   		//-Si son diferentes Generar el log.
			   		if ( !ant.equals(des) ) 
			   		{
						String nombre1 = this.getMapaDieta("descripcion_" + j)+"";
						String vel1 = this.getMapaDieta("velocidad_infusion_" + j )+"";
						String vol1 = this.getMapaDieta("volumen_total_" + j)+"";
	
			   			//----Primero se debe hacer la modificación en la BD
						resp1 = registroEnfermeriaDao.insertarLiqMedInfusion(con, Integer.parseInt(cod), -1,  nombre1, vol1, vel1, false, 2 ); //------El 2 es para Modificar el medicamento
						if ( resp1 > 0 )
						{
				   			String nombre = this.getMapaDieta("h_descripcion_" + j)+"";
							String vel = this.getMapaDieta("h_velocidad_infusion_" + j)+"";
							String vol = this.getMapaDieta("h_volumen_total_" + j)+"";
		
				   			String log =  "\n====================================MEDICAMENTO MODIFICADO========================================="; 
								   log += "\n INFORMACIÓN ANTERIOR  ";  
								   log += "\n NOMBRE :  " + nombre; 
								   log += "\n VOLUMEN TOTAL  :  " + vol; 
								   log += "\n VELOCIDAD DE INFUSIÓN  :  " + vel; 
								   log += "\n- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -"; 
								   log += "\n INFORMACIÓN NUEVA  ";  
								   log += "\n NOMBRE :  " + nombre1; 
								   log += "\n VOLUMEN TOTAL  :  " + vol1; 
								   log += "\n VELOCIDAD DE INFUSIÓN  :  " + vel1; 
								   log += "\n==================================================================================================="; 
				   			
				   			LogsAxioma.enviarLog(ConstantesBD.logRegEnferMedDietaModCodigo, log, ConstantesBD.tipoRegistroLogModificacion, loginUsuario);
						}
			   		}
		   		}
		   		
		   		
		   		//-----ES PARA INSERTAR LA SUSPENSIÓN PARA LA ORDEN MEDICA. 
		 		if ( UtilidadCadena.vNull(suspedido, "on") && UtilidadCadena.vNull(this.getMapaDieta("consecutivo_liquido_" + j)+"", "-1") )
		   		{
		 			String code = this.getMapaDieta("codigomedenfer_" + j)+"";
		   			//----Primero se debe hacer la modificación en la BD
					resp1 = registroEnfermeriaDao.insertarLiqMedInfusion(con, Integer.parseInt(code), -1,  "" , "", "", true, 4); //------El 3 es para suspender el medicamento

					//---Para romper el ciclo y hacer el rollback
					if (resp1 < 1) { break; }
		   		}
		  
		   		
		   		
			 }   	
		}
		
	
		//----Barre el mapa de dieta
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
	 * Método para cargar sólo la información guardada desde la orden médica
	  * @param con
	 * @param codigoCuenta
	 * @param fechaInicio
	 * @param codigoCuentaAsocio
	 * 
	 */
	public boolean cargarDietaOrdenMedica(Connection con, String cuentas, int centroCosto, int institucion, String fechaInicio)
	{
		RegistroEnfermeriaDao	registroEnferDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroEnfermeriaDao();
		this.hayDieta = false;
		
		try
		{	
			//-----El Cero es para consultar si hay via oral, parenteral, y la descripcion de la dieta 
			Collection coleccion = registroEnferDao.cargarDieta(con, cuentas,  0); 
				
			Iterator ite=coleccion.iterator();
			if(ite.hasNext())
			{
				HashMap col=(HashMap) ite.next();
				
				if ( UtilidadCadena.noEsVacio(col.get("nutricion_oral")+"") ) 
				{
					if ( (col.get("nutricion_oral")+"").equals(ValoresPorDefecto.getValorTrueParaConsultas())) { this.nutricionOral = "SI"; }
					else  { this.nutricionOral = "NO"; }
				}else { this.nutricionOral = ""; } //-No esta registrado
				
				//-Reutlizar la variable nutricionParenteral para cargar si finalizaron solamente la parte de la DIETA.
				if ( UtilidadCadena.noEsVacio(col.get("finalizar_dieta")+"") ) 
				{
					if ( UtilidadTexto.getBoolean(col.get("finalizar_dieta")+"") ) { this.nutricionParenteral = "SI"; }
					else  { this.nutricionParenteral = "NO"; }
				}else { this.nutricionParenteral = ""; } //-No esta registrado
				
				
				if ( UtilidadCadena.noEsVacio(col.get("medico")+"") )	{ this.medicoDietaOrden = col.get("medico")+""; 	}
				else { this.medicoDietaOrden = col.get("medico")+"";  }
				
				if ( UtilidadCadena.noEsVacio(col.get("fecha_orden")+"") )	{ this.fechaRegistroDietaOrden = col.get("fecha_orden")+""; 	}
				else { this.fechaRegistroDietaOrden = "";  }

				if ( UtilidadCadena.noEsVacio(col.get("fecha_grabacion")+"") )	{ this.fechaGrabacionDietaOrden = col.get("fecha_grabacion")+""; 	}
				else { this.fechaGrabacionDietaOrden = "";  }
				
				if ( UtilidadCadena.noEsVacio(col.get("observacion_parente")+"") )	{ this.observacionDietaParenteOrden = col.get("observacion_parente")+""; 	}
				else { this.observacionDietaParenteOrden = "";  }

				this.suspendidoEnfermeria=col.get("suspendido_enfermeria")+"";
				this.observacionesEnfermeria=col.get("observaciones")+"";
				this.hayDieta = true;
			}
			
			//----Cargar los ultimos tipos de nutrición registrados
			coleccion = registroEnferDao.cargarDieta(con, cuentas, 2);
			ite=coleccion.iterator();
			String aux = "";
			ite=coleccion.iterator();
			while(ite.hasNext()) 
			{
				HashMap col=(HashMap) ite.next();
				if (aux.equals(""))	{ aux = (col.get("nombre")+"");}
				else { aux = aux + ", " +  (col.get("nombre")+"");	}
				this.hayDieta = true;
			}
	
			//-Pasando todos los tipos de nutriciones del ultimo registro separados por comas.
			this.tiposNutricionOral = aux;			
			
			return true;	
		}
		catch(Exception e)
		{
			logger.warn("Error al Consultar la dieta de la Orden Medica (Mundo de registro de enfermeria) " +e.toString());
			return false;		
		} 
			
	}
	
	/**
	 * Metodo para consultar y cargar la información de la dieta que esta registrado en ordenes medicas
	 * @param con
	 * @param codigoCuenta
	 * @param fechaInicio
	 * @param codigoCuentaAsocio
	 */
	public boolean cargarDieta(Connection con, String cuentas, int centroCosto, int institucion, String fechaInicio)
	{
		RegistroEnfermeriaDao	registroEnferDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroEnfermeriaDao();
		//this.hayDieta = false;
		
		try
		{	
			//-----El Cero es para consultar si hay via oral, parenteral, y la descripcion de la dieta 
			/*Collection coleccion = registroEnferDao.cargarDieta(con, codigoCuenta, codigoCuentaAsocio,  0); 
				
			Iterator ite=coleccion.iterator();
			if(ite.hasNext())
			{
				HashMap col=(HashMap) ite.next();
				
				if ( UtilidadCadena.noEsVacio(col.get("nutricion_oral")+"") ) 
				{
					if ( (col.get("nutricion_oral")+"").equals(ValoresPorDefecto.getValorTrueParaConsultas())) { this.nutricionOral = "SI"; }
					else  { this.nutricionOral = "NO"; }
				}else { this.nutricionOral = ""; } //-No esta registrado
				
				//-Reutlizar la variable nutricionParenteral para cargar si finalizaron solamente la parte de la DIETA.
				if ( UtilidadCadena.noEsVacio(col.get("finalizar_dieta")+"") ) 
				{
					if ( UtilidadTexto.getBoolean(col.get("finalizar_dieta")+"") ) { this.nutricionParenteral = "SI"; }
					else  { this.nutricionParenteral = "NO"; }
				}else { this.nutricionParenteral = ""; } //-No esta registrado
				
				
				if ( UtilidadCadena.noEsVacio(col.get("medico")+"") )	{ this.medicoDietaOrden = col.get("medico")+""; 	}
				else { this.medicoDietaOrden = col.get("medico")+"";  }
				
				if ( UtilidadCadena.noEsVacio(col.get("fecha_orden")+"") )	{ this.fechaRegistroDietaOrden = col.get("fecha_orden")+""; 	}
				else { this.fechaRegistroDietaOrden = "";  }

				if ( UtilidadCadena.noEsVacio(col.get("fecha_grabacion")+"") )	{ this.fechaGrabacionDietaOrden = col.get("fecha_grabacion")+""; 	}
				else { this.fechaGrabacionDietaOrden = "";  }
				
				if ( UtilidadCadena.noEsVacio(col.get("observacion_parente")+"") )	{ this.observacionDietaParenteOrden = col.get("observacion_parente")+""; 	}
				else { this.observacionDietaParenteOrden = "";  }
								

				
				this.hayDieta = true;
			}*/

			//-----Descripcion de la dieta 
		/*	coleccion = registroEnferDao.cargarDieta(con, codigoCuenta, 1); 
				
			ite=coleccion.iterator();
			if(ite.hasNext())
			{
				HashMap col=(HashMap) ite.next();
				if ( UtilidadCadena.noEsVacio(col.get("descripcion_dieta_oral")+"") ) 
				{ 
		      		String cad = col.get("descripcion_dieta_oral")+"";
		      		
		      		for(int i=0; i<cad.length();i++)
		      		{
		      			if(cad.charAt(i)=='\n')
		      			{
		      				cad=cad.substring(0,i-1)+"<br>"+cad.substring(i+1, cad.length());
		          			i++;
		      			}
		      		}

					
					this.descripcionDieta = cad;	
				}
				else { this.descripcionDieta = "";	}
				this.hayDieta = true;
			}*/
			/*if (UtilidadCadena.noEsVacio(this.descripcionDieta))
			{
				this.hayDieta = true;
			}
						
			//----Cargar los ultimos tipos de nutrición registrados
			Collection coleccion = registroEnferDao.cargarDieta(con, codigoCuenta,  codigoCuentaAsocio, 2);
			Iterator ite=coleccion.iterator();
			String aux = "";
			ite=coleccion.iterator();
			while(ite.hasNext()) 
			{
				HashMap col=(HashMap) ite.next();
				if (aux.equals(""))	{ aux = (col.get("nombre")+"");}
				else { aux = aux + ", " +  (col.get("nombre")+"");	}
				this.hayDieta = true;
			}
	
			//-Pasando todos los tipos de nutriciones del ultimo registro separados por comas.
			this.tiposNutricionOral = aux;
			
			*/
			
			//-Cargar los liquidos y medicamentos infusion registrados anteriormente
			this.setMapaDieta(new HashMap());
			this.setMapaDieta( registroEnferDao.consultarLiqMedicamentosPaciente (con, cuentas, centroCosto, institucion, 0, "", fechaInicio) );
			
			//-Insertar la misma información en los hidden para poder hacer el despazamiento por los historicos 
			int nroRegMedNoParam = Integer.parseInt( mapaDieta.get("numRegistros")+"");
			if ( nroRegMedNoParam > 0 )
			{
			   for(int j = 0 ; j < nroRegMedNoParam ; j ++)
				 { 
				    this.setMapaDieta("h_registro_enfer_" + j, this.getMapaDieta("registro_enfer_"+ j));
				    this.setMapaDieta("h_consecutivo_liquido_" + j, this.getMapaDieta("consecutivo_liquido_"+ j));
				    this.setMapaDieta("h_descripcion_" + j, this.getMapaDieta("descripcion_"+ j));
				    this.setMapaDieta("h_volumen_total_" + j, this.getMapaDieta("volumen_total_"+ j));
				    this.setMapaDieta("h_velocidad_infusion_" + j, this.getMapaDieta("velocidad_infusion_"+ j));
				    this.setMapaDieta("h_suspender_" + j, this.getMapaDieta("suspender_"+ j));
				 }   	
			}

			//---Copiar solo los nombres y los codigos de los medicamentos parametrizados y no pàrametrizados.
			HashMap mp = new HashMap();
			mp = registroEnferDao.consultarLiqMedicamentosPaciente(con, cuentas, centroCosto, institucion, 1, "", fechaInicio);
			this.setMapaDieta("nroRegMedAdm", mp.get("numRegistros")+"");
			mp.remove("numRegistros");
			this.mapaDieta.putAll(mp);

			
			//---Copiar en el mapa la información registrada en el balance de liquidos administrados. 
			mp = new HashMap();
			mp = registroEnferDao.consultarLiqMedicamentosPaciente(con, cuentas, centroCosto, institucion, 2, "", fechaInicio);
			this.setMapaDieta("nroRegBalLiqAdm", mp.get("numRegistros")+"");
			mp.remove("numRegistros");
			this.mapaDieta.putAll(mp);

			//------Cargar los codigo y los nombres de los liquidos eliminados. (SOLAMENTE HAY PARAMETRIZABLES).
			mp.clear();
			mp = registroEnferDao.consultarLiqMedicamentosPaciente(con, cuentas, centroCosto, institucion, 5, "", fechaInicio);
			this.setMapaDieta("nroRegMedElim", mp.get("numRegistros")+"");
			mp.remove("numRegistros");
			this.mapaDieta.putAll(mp);
			
			//------Copiar en el mapa la información registrada en el balance de liquidos eliminados. 
			mp = new HashMap();
			mp = registroEnferDao.consultarLiqMedicamentosPaciente(con, cuentas, centroCosto, institucion, 6, "", fechaInicio);
			this.setMapaDieta("nroRegBalLiqElim", mp.get("numRegistros")+"");

			//-------------------------------------------------------------------------------------------------------------------------------------------
			//-------------------------------------------------------------------------------------------------------------------------------------------
			//-------------------------------------------------------------------------------------------------------------------------------------------
			//------Cargar Las Mezclas Registradas en ordenes Medicas. 
			
			this.setMapaDietaOrdenes( registroEnferDao.consultarMezclaOrdenes (con, 0, cuentas) ); 

			//------Cargar Las Mezclas y el detalle de cada articulo que conforma cada mezcla. 
			mp = new HashMap();
			mp = registroEnferDao.consultarMezclaOrdenes (con, 1, cuentas); 
			this.setMapaDietaOrdenes("nroArticulos", mp.get("numRegistros")+"");
			mp.remove("numRegistros");
			this.mapaDietaOrdenes.putAll(mp);

			//-------------------------------------------------------------------------------------------------------------------------------------------
			//-------------------------------------------------------------------------------------------------------------------------------------------
			
			return true;	
		}				
		catch(Exception e)
		{
			logger.warn("Error al Consultar la dieta de la Orden Medica (Mundo de registro de enfermeria) " +e.toString());
			return false;		
		} 
	}

	
	
	/**
	 * Metodo para cargar el historico de Dieta.
	 * @param con
	 * @param codigoCuenta
	 * @param codigoCentroCosto
	 * @param codigoInstitucionInt
	 * @param string
	 * @param fechaFin
	 * @param tipo de consulta para el Historico (si se consultaran fecha o los datos de los medicamentos) 
	 * 
	 */
	public HashMap cargarHistoricosDieta(Connection con, String cuentas, int centroCosto, int institucion, String fechaFin, int tipoConsultaHistorico)
	{
		RegistroEnfermeriaDao	registroEnferDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroEnfermeriaDao();

		//---Consultar los registros Historicos de Dieta.
		return registroEnferDao.cargarHistoricosDieta(con, cuentas, centroCosto, institucion, tipoConsultaHistorico, "", fechaFin);
	}
	
	
	
	/**
	 * Funcion para retornar una collecion con el listado de los tipos parametrizados
	 * por centro de costo e institución en el registro de enfermería 
	 * @param con
	 * @param codigo de la institucion
	 * @param codigo del centroCosto
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param Nro Consulta parametro que indica la informacion a sacar
	 *		1	Listado de tipos de signos vitales de enfermería		
	 * @return Collection 
	 */

	public Collection consultarTiposInstitucionCCosto(Connection con, int institucion, String cuentas, int nroConsulta) 
	{
		RegistroEnfermeriaDao registroEnfermeriaDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroEnfermeriaDao();
		Collection coleccion=null;
		try
		{	
			coleccion = registroEnfermeriaDao.consultarTiposInstitucionCCosto(con, institucion, cuentas, nroConsulta);
		}
		catch(Exception e)
		{
			logger.warn("Error al Consultar tablas	parametrizables por institución centro de costo	para Registro Enfermería " +e.toString());
			coleccion=null;
		}
		return coleccion;		
	}
	
	/**
	 * Método para insertar los exámenes fìsicos del registro de enfermería
	 * @param con una conexion abierta con una fuente de datos
	 * @param codRegEnfer
	 * @return true si se insertó con exito todo sino retorna false
	 * @throws SQLException
	 */
	public boolean insertarExamenesFisicos (Connection con, int codRegEnfer) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		boolean error=false;
		int examFisico=0;
		
		if (registroEnfermeriaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (RegistroEnfermeriaDao - insertarExamenesFisicos )");
		}
		
		//Iniciamos la transacción
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
		if(this.getMapaExamenesFisicos("codExamenesFisicos") != null && this.getMapaExamenesFisicos("codExamenesFisicosTipo") != null)
		{
			Vector codigosExamFisicos=(Vector) this.getMapaExamenesFisicos("codExamenesFisicos");
			Vector codExamenesFisicosTipo=(Vector) this.getMapaExamenesFisicos("codExamenesFisicosTipo");
			
			for (int i=0; i<codigosExamFisicos.size(); i++)
			{
				int examenFisicoCcIns=Integer.parseInt(codigosExamFisicos.elementAt(i)+"");
				int examenFisicoTipo=Integer.parseInt(codExamenesFisicosTipo.elementAt(i)+"");
				
				//Valor del exámen fìsico
				String valorExamenFisico=(String)this.getMapaExamenesFisicos("examenFisico_"+examenFisicoTipo);
				
				if(valorExamenFisico != null && !valorExamenFisico.trim().equals(""))
				{
					//Se inserta el exámen fìsico
					examFisico = registroEnfermeriaDao.insertarExamenesFisicos(con, codRegEnfer, examenFisicoCcIns, valorExamenFisico);
					
					if (examFisico < 1)
					{
						error=true;
						break;
					}
				}//if valorExamenFisico!=null
			}//for
		}//if codExamenesFisicos != null
		
		//Se finaliza la transacción cuando hay error en una inserción de datos o no se logró inicializar la transacción
		if (!inicioTrans || error)
		{
			myFactory.abortTransaction(con);
			return false;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		
		return true;
	}
	
	/**
	 * Método que insertar los signos vitales del registro de enfermería
	 * @param con
	 * @param codigoEncabezado
	 * @param indicaInsercion -> Indica en cual(es) signos vitales se ingresó información
	 * @return
	 * @throws SQLException
	 */
	public boolean insertarSignosVitales (Connection con, int codigoEncabezado, int indicaInsercion) throws SQLException
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		boolean error=false;
		int signoVital=0;
		int signoVital2=0;
		
		if (registroEnfermeriaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (RegistroEnfermeriaDao - insertarSignosVitales )");
		}
		
		//Iniciamos la transacción
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
		logger.info("indicaInsercion-->"+indicaInsercion+"\n");
		//Se verifica si se inserta en los signos vitales fijos
		if(indicaInsercion==1 || indicaInsercion==3)
			{
				signoVital2=registroEnfermeriaDao.insertarSignosVitalesFijos(con, codigoEncabezado, frecuenciaCardiaca, frecuenciaRespiratoria, presionArterialSistolica, presionArterialDiastolica, presionArterialMedia, temperaturaPaciente);
				if(signoVital2<0)
					error=true;
			}
		
		//Se verifica si se insertan los signos vitales parametrizados
		if((indicaInsercion==2 || indicaInsercion==3) && !error)
			{
				if(this.getMapaSignosVitales("codSignosVitales") != null)
					{
						Vector codigosSignosVitales=(Vector) this.getMapaSignosVitales("codSignosVitales");
						
						for (int i=0; i<codigosSignosVitales.size(); i++)
						{
							int signoVitalCcIns=Integer.parseInt(codigosSignosVitales.elementAt(i)+"");
							
							//Valor del signo vital parametrizado
							String valorSignoVital=(String)this.getMapaSignosVitales("valor_"+signoVitalCcIns);
							
							if(valorSignoVital != null && !valorSignoVital.trim().equals(""))
							{
								logger.info("signoVitalCcIns-->"+signoVitalCcIns);
								logger.info("valorSignoVital-->"+valorSignoVital);
								//Se inserta el signo vital parametrizado
								signoVital = registroEnfermeriaDao.insertarSignoVitalPametrizado(con, codigoEncabezado, signoVitalCcIns, valorSignoVital);
								
								if (signoVital < 1)
								{
									error=true;
									break;
								}
							}//if valorSignoVital!=null
						}//for
					}//if codigosSignosVitales != null
			}//if indicaInsercion
		
		//Se finaliza la transacción cuando hay error en una inserción de datos o no se logró inicializar la transacción
		if (!inicioTrans || error)
		{
			myFactory.abortTransaction(con);
			return false;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		
		return true;
	}
	
	/**
	 * Metodo que realiza la busqueda de lista de los pacientes que se encuentran en las camas ocupadas 
	 * cuyo centro de costo de la cama corresponde al centro de costo seleccionado.
	 * @param con
	 * @param centroCosto
	 * @return listadoPacientesCentroCosto
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public HashMap consultarPacientesXArea(Connection con, int centroCosto,String codigoPiso,String codigoHabitacion,String codigoCama, boolean pacientesNuevaInformacion) 
	{
		HashMap campos = new HashMap();
		campos.put("codigoCentroCosto",centroCosto+"");
		campos.put("codigoPiso",codigoPiso);
		campos.put("codigoHabitacion",codigoHabitacion);
		campos.put("codigoCama",codigoCama);
		campos.put("pacientesNuevaInformacion",pacientesNuevaInformacion);
		
		/**
		 * Si se selecciono el área se realiza la búsqueda de paciente por este criterio
		 * si no se selecciona se busca por piso, habitación o cama
		 */
		if (centroCosto > 0) {
			return registroEnfermeriaDao.consultarPacientesCentroCosto(con, campos);
		} else {
			return registroEnfermeriaDao.consultarPacientesPiso(con, campos);
		}
	}
	
    /**
     * Metodo para consultar y cargar la información de la sección Exámenes Físicos
     * @param con
     * @param codigoCuenta
     * @param cuentaAsocion
     * @return true si existe información de examenes fisicos sino retorna false
     */
    public boolean cargarExamenesFisicos (Connection con, String cuentas)
    {
    	try
		{
    		Collection colExamenesFisicos=registroEnfermeriaDao.cargarExamenesFisicos (con, cuentas);
    		Iterator ite=colExamenesFisicos.iterator();
    		String primerCodigo="", siguienteCodigo="";
    		StringBuffer nuevoExamenF=new StringBuffer();
    		for (int i=0; i<colExamenesFisicos.size(); i++)
    		{
    			if (ite.hasNext())
    			{
    				HashMap colExaFisico=(HashMap) ite.next();
    				if (i==0)
    				{
    					primerCodigo=colExaFisico.get("codigo_tipo")+"";
    				}
    				
    				siguienteCodigo=colExaFisico.get("codigo_tipo")+"";
    				if (primerCodigo.equals(siguienteCodigo))
    					{
    						nuevoExamenF.append(colExaFisico.get("valor")+"");
    					}
    				else
    				{
       					this.setMapaExamenesFisicos("examenFisico_"+primerCodigo, nuevoExamenF.toString());
       					primerCodigo=siguienteCodigo;
       					nuevoExamenF=new StringBuffer();
       					nuevoExamenF.append(colExaFisico.get("valor")+"");
    				}
    				
    				//--------Cuando llega al último exámen físico --------------//
    				if (i==colExamenesFisicos.size()-1)
    				{
    					this.setMapaExamenesFisicos("examenFisico_"+primerCodigo, nuevoExamenF.toString());
    				}
    				/*logger.info("codigo tipo-->"+colExaFisico.get("codigo_tipo")+"\n");
    				logger.info("valor-->"+colExaFisico.get("valor")+"\n");
    				if(colExaFisico.get("codigo") != null && !colExaFisico.get("codigo").equals(""))
    				{
    					this.setMapaExamenesFisicos("examenFisico_"+colExaFisico.get("codigo_tipo"), colExaFisico.get("valor"));
    				}*/
    			}//if
    		}//for
		}//try
    	catch(Exception e)
		{
		  logger.warn("Error al Consultar los exámenes físicos del registro de enfermería en el mundo" +e.toString());
		}
    	
    	return false;
    }
    
    /**
     * Método que inserta la anotación de enfermería
     * @param con
     * @param codigoEncabezado
     * @return
     * @throws SQLException
     */
	public int insertarAnotacionEnfermeria (Connection con, int codigoEncabezado) throws SQLException 
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		int  resp1=0;
			
		if (registroEnfermeriaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (registroEnfermeriaDao - insertarAnotacionEnfermeria)");
		}
		
		//Iniciamos la transacción, si el estado es empezar
		boolean inicioTrans;
		inicioTrans=myFactory.beginTransaction(con);
		resp1=registroEnfermeriaDao.insertarAnotacionEnfermeria(con, codigoEncabezado, this.anotacionEnfermeria);
		
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
	 * Método para consultar el listado de anotaciones de enfermería realizadas en el registro 
	 * de enfermería de acuerdo a la cuenta del paciente
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaFin 
	 * @param fechaInicio 
	 * @return listadoAnotacionesEnfermeria
	 */
	public Collection consultarAnotacionesEnfermeria (Connection con, String cuentas, String fechaInicio, String fechaFin)
	{
		Collection coleccion=null;
		try
		{	
			coleccion = registroEnfermeriaDao.consultarAnotacionesEnfermeria(con, cuentas,fechaInicio,fechaFin);
		}
		catch(Exception e)
		{
			logger.warn("Error al Consultar el histórico de las Anotaciones de Enfermería para Registro Enfermería " +e.toString());
			coleccion=null;
		}
		return coleccion;		
	}

	public HashMap consultarTomaMuestrasHistorico (Connection con, HashMap parametros)
	{
		HashMap mapa=null;
		try
		{	
			mapa = registroEnfermeriaDao.consultarTomaMuestrasHistorico(con, parametros);
		}
		catch(Exception e)
		{
			logger.warn("Error al Consultar el histórico de las Anotaciones de Enfermería para Registro Enfermería " +e.toString());
			mapa=null;
		}
		return mapa;		
	}

	/**
	 * Método para ingresar los diagnosticos de enfermería
	 * @param con
	 * @param codEncabezado
	 * @return int con numero registros insertados
	 */
	public int ingresarDiagnosticosNanda(Connection con, int codEncabezado)
	{
		return registroEnfermeriaDao.ingresarDiagnosticosNanda(con, codEncabezado, this.diagnosticosEnfermeria);
	}

	/**
	 * Método para ingresar los datos del soporte respiratorio
	 * @param con
	 * @param codEncabezado
	 * @return int con numero registros insertados
	 */
	public int ingresarSoporteRespiratorio(Connection con, int codEncabezado)
	{
		return registroEnfermeriaDao.ingresarSoporteRespiratorio(con, codEncabezado, this.soporteRespiratorio);
	}

	/**
	 * Método para consultar el historico de los diagnosticos de enfermería
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param institucion
	 * @param fechaInicio
	 * @param fechaFin
	 * @return
	 */
	public HashMap consultarDiagnosticosNanda(Connection con, String cuentas, int institucion, String fechaInicio, String fechaFin)
	{
		return registroEnfermeriaDao.consultarDiagnosticosNanda(con, cuentas, institucion, fechaInicio, fechaFin);
	}
	
	/**
	 * Método para consultar los insumos solicitados al paciente a través de solicitud de
     * medicamentos que hacen parte de catéteres y sondas que no se encuentran anuladas
     * y se encuentran ya despachadas
	 * @param con
	 * @param codigoCuenta
	 * @param institucion
	 * @return articulosDespachados
	 */
	public Collection consultarCateterSondaDespachados (Connection con, int codigoCuenta, int institucion)
	{
		Collection coleccion=null;
		try
		{	
			coleccion = registroEnfermeriaDao.consultarCateterSondaDespachados(con, codigoCuenta, institucion);
		}
		catch(Exception e)
		{
			logger.warn("Error al Consultar Cateter sonda despachados para Registro Enfermería " +e.toString());
			coleccion=null;
		}
		return coleccion;		
	}
	
	/**
	 * Método para consultar el histórico de los signos vitales fijos de la sección
     * de acuerdo a la hora inicio,fin del turno y la hora del sistema  
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicio
	 * @param fechaFin
	 * @return signosVitalesFijosHisto
	 */
	public Collection consultarSignosVitalesFijosHisto (Connection con, String cuentas, String fechaInicio, String fechaFin)
	{
		logger.info("\n entre a consultarSignosVitalesFijosHisto");
		Collection coleccion=null;
		try
		{	
			coleccion = registroEnfermeriaDao.consultarSignosVitalesFijosHisto(con, cuentas, fechaInicio, fechaFin);
		}
		catch(Exception e)
		{
			logger.warn("Error al Consultar el histórico de los signos vitales fijos para Registro Enfermería " +e.toString());
			coleccion=null;
		}
		return coleccion;		
	}
	
	/**
	 * Método para consultar el histórico de los signos vitales parametrizados por institución y centro de costo de la sección
     * de acuerdo a la hora inicio,fin del turno y la hora del sistema  
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param institucion
	 * @param centroCosto
	 * @param fechaInicio
	 * @param fechaFin
	 * @return signosVitalesParamHisto
	 */
	public Collection consultarSignosVitalesParamHisto (Connection con, String cuentas, int institucion, String fechaInicio, String fechaFin)
	{
		logger.info("\n entre a consultarSignosVitalesParamHisto");
		Collection coleccion=null;
		try
		{	
			coleccion = registroEnfermeriaDao.consultarSignosVitalesParamHisto(con, cuentas, institucion, fechaInicio, fechaFin);
		}
		catch(Exception e)
		{
			logger.warn("Error al Consultar el histórico de los signos vitales parametrizados por institucion centro costo para Registro Enfermería " +e.toString());
			coleccion=null;
		}
		return coleccion;		
	}
	
	/**
	 * Método para consultar el istado con los códigos històricos, fecha registro y hora registro,
     * de los signos vitales fijos y parametrizados 
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param institucion
	 * @param centroCosto
	 * @param fechaInicio
	 * @param fechaFin
	 * @return signosVitalesHistoTodos
	 */
	public Collection consultarSignosVitalesHistoTodos (Connection con, String cuentas, int institucion, String fechaInicio, String fechaFin)
	{
		logger.info("\n entre a  consultarSignosVitalesHistoTodos");
		Collection coleccion=null;
		try
		{	
			coleccion = registroEnfermeriaDao.consultarSignosVitalesHistoTodos(con, cuentas, institucion, fechaInicio, fechaFin);
		}
		catch(Exception e)
		{
			logger.warn("Error al Consultar el histórico de los signos vitales fijos y parametrizados para Registro Enfermería " +e.toString());
			coleccion=null;
		}
		return coleccion;		
	}
	
	  /**
     * Método que inserta la sección cateter sonda
     * @param con
     * @param codigoEncabezado
     * @param usuario
     * @param indicaInsercion
     * @return
     * @throws SQLException
     */
	public boolean insertarCateteresSonda (Connection con, int codigoEncabezado, UsuarioBasico usuario, int indicaInsercion) throws SQLException 
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		boolean error=false;
		//int examFisico=0;
		int cateterEnca=0, cateterSondaFijo=0, cateterSondaParam=0, catSondaFijo=0;
		
		if (registroEnfermeriaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (RegistroEnfermeriaDao - insertarCateteresSonda )");
		}
		
		//Iniciamos la transacción
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
		if (indicaInsercion==1 || indicaInsercion==3)
		{
			if (this.getMapaCateterSonda("codigosCateterSonda") != null)
			{
				Vector codigosCateterSonda=(Vector) this.getMapaCateterSonda("codigosCateterSonda");
				
				for (int c=0; c<codigosCateterSonda.size(); c++)
				{
					int catSondaRegEnfer=Integer.parseInt(this.getMapaCateterSonda("cateterSondaRegEnfer_"+codigosCateterSonda.elementAt(c))+"");
					String curacionesAnt=(String)this.getMapaCateterSonda("curaciones_"+codigosCateterSonda.elementAt(c));
					String curacionesNueva=(String)this.getMapaCateterSonda("curacionesNueva_"+codigosCateterSonda.elementAt(c));
					String observacionesAnt=(String)this.getMapaCateterSonda("observaciones_"+codigosCateterSonda.elementAt(c));
					String observacionesNueva=(String)this.getMapaCateterSonda("observacionesNueva_"+codigosCateterSonda.elementAt(c));
					String fechaInsercion=(String)this.getMapaCateterSonda("fechaInsercion_"+codigosCateterSonda.elementAt(c));
					String horaInsercion=(String)this.getMapaCateterSonda("horaInsercion_"+codigosCateterSonda.elementAt(c));
					String fechaRetiro=(String)this.getMapaCateterSonda("fechaRetiro_"+codigosCateterSonda.elementAt(c));
					String horaRetiro=(String)this.getMapaCateterSonda("horaRetiro_"+codigosCateterSonda.elementAt(c));
					
					
					//int codArticuoDespacho=Integer.parseInt(this.getMapaCateterSonda("tipoCateterSonda_"+codigosCateterSonda.elementAt(c))+""); 
					
					
					
					//----- Si hay nuevas información en los campos curaciones y observaciones se actualiza el registro, o en fecha/hora inserción y retiro ----//
					if (UtilidadCadena.noEsVacio(curacionesNueva) || UtilidadCadena.noEsVacio(observacionesNueva) || UtilidadCadena.noEsVacio(fechaInsercion) || UtilidadCadena.noEsVacio(fechaRetiro))
						{
							String curacionesAntNueva="";
							String observacionesAntNueva="";
							
							if (UtilidadCadena.noEsVacio(curacionesNueva))
								{
									String nuevaCuracion="Fecha/hora de Registro: "+this.fechaRegistro+" "+this.horaRegistro+"\n"+curacionesNueva;
									curacionesAntNueva=UtilidadTexto.agregarTextoAObservacionFechaGrabacion(curacionesAnt, nuevaCuracion, usuario, false);
								}
							
							if (UtilidadCadena.noEsVacio(observacionesNueva))
								{
								String nuevaObservacion="Fecha/hora Registro: "+this.fechaRegistro+" "+this.horaRegistro+"\n"+observacionesNueva;
								observacionesAntNueva=UtilidadTexto.agregarTextoAObservacionFechaGrabacion(observacionesAnt, nuevaObservacion, usuario, false);
								}
							
							/*logger.info("catSondaRegEnfer-->"+catSondaRegEnfer);
							logger.info("curacionesAnt-->"+curacionesAnt);
							logger.info("curacionesNueva-->"+curacionesNueva);
							logger.info("observacionesAnt-->"+observacionesAnt);
							logger.info("observacionesNueva-->"+observacionesNueva);
							logger.info("curacionesAntNueva-->"+curacionesAntNueva);
							logger.info("observacionesAntNueva-->"+observacionesNueva);*/
							
							catSondaFijo=registroEnfermeriaDao.actualizarCateterSondaFijo(con, catSondaRegEnfer, curacionesAntNueva, observacionesAntNueva, fechaInsercion, horaInsercion, fechaRetiro, horaRetiro);
							
							if (catSondaFijo <0 )
								{
									error=true;
									break;
								}
						}//if curaciones o observaciones != vacio
				}//for
			}//if codigosCateterSonda != null
		}// if indicaInsercion=1 o indicaInsercion=3
		
		if (indicaInsercion==2 || indicaInsercion==3)
		{
			if(this.getMapaCateterSonda("codsNuevosCateterSonda") != null)
			{
				String codNuevosCateter=this.getMapaCateterSonda("codsNuevosCateterSonda")+"";
				String[] vecNuevosCateteres=codNuevosCateter.split("-");
				
				logger.info("codNuevosCateter-->"+codNuevosCateter);
				
				for (int i=0; i<vecNuevosCateteres.length; i++)
				{
					//-------- Codigo de la fila del nuevo artículo-------------//
					int codFilaArticuloNuevo=Integer.parseInt(vecNuevosCateteres[i]);
					
					//Valor del tipo de articulo seleccionado
					int tipoArticulo = Integer.parseInt(this.getMapaCateterSonda("tipoCateterSonda_"+codFilaArticuloNuevo)+"");
					
					logger.info("tipo articulo-->"+tipoArticulo);
					
					//---- Si tipo articulo es diferente de -1 es porque si seleccionaron un artículo
					if (tipoArticulo != -1)
					{
						//---------- Se inserta el encabezado de cateter sonda (cateter_sonda_reg_enfer)-----//
						cateterEnca=registroEnfermeriaDao.insertarEncabezadoCateterSonda(con, codigoEncabezado, tipoArticulo);
						
						//Si cateterEnca es mayor que cero insertó bien el encabezado
						if (cateterEnca >0)
						{
							String viaInsercion = this.getMapaCateterSonda("viaInsercion_"+codFilaArticuloNuevo)+"";
							String fechaInsercion = this.getMapaCateterSonda("fechaInsercion_"+codFilaArticuloNuevo)+"";
							String horaInsercion = this.getMapaCateterSonda("horaInsercion_"+codFilaArticuloNuevo)+"";
							String fechaRetiro = this.getMapaCateterSonda("fechaRetiro_"+codFilaArticuloNuevo)+"";
							String horaRetiro = this.getMapaCateterSonda("horaRetiro_"+codFilaArticuloNuevo)+"";
							String curaciones = this.getMapaCateterSonda("curaciones_"+codFilaArticuloNuevo)+"";
							String observaciones = this.getMapaCateterSonda("observaciones_"+codFilaArticuloNuevo)+"";
							
							if (UtilidadCadena.noEsVacio(curaciones))
								{
									String nuevaCuracion="Fecha/hora Registro: "+this.fechaRegistro+" "+this.horaRegistro+"\n"+curaciones;
									curaciones=UtilidadTexto.agregarTextoAObservacionFechaGrabacion("", nuevaCuracion, usuario, false);
								}
							
							if (UtilidadCadena.noEsVacio(observaciones))
							{
								String nuevaObservacion="Fecha/hora Registro: "+this.fechaRegistro+" "+this.horaRegistro+"\n"+observaciones;	
								observaciones=UtilidadTexto.agregarTextoAObservacionFechaGrabacion("", nuevaObservacion, usuario, false);
							}
							
							if (UtilidadCadena.noEsVacio(viaInsercion) || UtilidadCadena.noEsVacio(fechaInsercion) || UtilidadCadena.noEsVacio(horaInsercion) || UtilidadCadena.noEsVacio(fechaRetiro) || UtilidadCadena.noEsVacio(horaRetiro) || UtilidadCadena.noEsVacio(curaciones) || UtilidadCadena.noEsVacio(observaciones))
								//------------- Se inserta el valor de las columnas fijas de los catéteres y sonda -------------//
								cateterSondaFijo=registroEnfermeriaDao.insertarCateterSondaFijo (con, cateterEnca, viaInsercion, fechaInsercion, horaInsercion, fechaRetiro, horaRetiro, curaciones, observaciones);
	
							if (cateterSondaFijo <0 )
							{
								error=true;
								break;
							}
							
							//------------ Se inserta el valor de las columnas de cateter sonda parametrizadas ------------//
							if (this.getMapaCateterSonda("colsCateterSonda") != null)
							{
								Vector colsCateterSondaParam=(Vector) this.getMapaCateterSonda("colsCateterSonda");
								for (int z=0; z<colsCateterSondaParam.size(); z++)
									{
										int colCateterCcIns = Integer.parseInt(colsCateterSondaParam.elementAt(z)+"");
										
										//------- Valor del cateter sonda parametrizado ---------------//
										String valorColCateterCcIns = (String)this.getMapaCateterSonda("colCateter_"+codFilaArticuloNuevo+"_"+colCateterCcIns);
										
										//------------ Se inserta el valor de la columna cateter sonda parametrizada si no está vacía--------//
										if (UtilidadCadena.noEsVacio(valorColCateterCcIns))
											{
												cateterSondaParam = registroEnfermeriaDao.insertarCateterSondaParam (con, cateterEnca, colCateterCcIns, valorColCateterCcIns);
												if (cateterSondaParam < 0)
												{
													error=true;
													break;
												}
											}
									}//for
							}//if colsCateterSonda != null
							
						}//if cateterEnca > 0
						else
						{
							error=false;
							break;
						}
					}//if tipoArticulo != -1
				}//for
			}//if
		}//if indicaInsercion=2 o indicaInsercion=3
		
		//Se finaliza la transacción cuando hay error en una inserción de datos o no se logró inicializar la transacción
		if (!inicioTrans || error)
		{
			myFactory.abortTransaction(con);
			return false;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		
		return true;
	}
	
	/**
	 * Método que consulta el histórico de los cateteres sonda fijos del paciente
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @return
	 */
	public Collection consultarCateterSondaFijosHisto(Connection con, String cuentas)
	{
		Collection coleccion=null;
		try
		{	
			coleccion = registroEnfermeriaDao.consultarCateterSondaFijosHisto(con, cuentas);
		}
		catch(Exception e)
		{
			logger.warn("Error al Consultar el histórico de los cateteres sonda fijos para Registro Enfermería " +e.toString());
			coleccion=null;
		}
		return coleccion;		
	}
	
	/**
	 * Método que consulta el histórico de los cateteres sonda parametrizados por institución centro costo
	 * del paciente
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param institucion
	 * @param centroCosto
	 * @return
	 */
	public Collection consultarCateterSondaParamHisto(Connection con, String cuentas, int institucion, int centroCosto)
	{
		Collection coleccion=null;
		try
		{	
			coleccion = registroEnfermeriaDao.consultarCateterSondaParamHisto(con, cuentas, institucion, centroCosto);
		}
		catch(Exception e)
		{
			logger.warn("Error al Consultar el histórico de los cateteres sonda parametrizados para Registro Enfermería " +e.toString());
			coleccion=null;
		}
		return coleccion;	
	}
	
	/**
	 * Método que consulta el histórico de los catéteres sonda fijos y parametrizados, despues de realizar
	 * el agrupamiento por el codigo del articulo despachado
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param institucion
	 * @param centroCosto
	 * @return
	 */
	public Collection consultarCateterSondaTodosHisto (Connection con, String cuentas, int institucion, int centroCosto)
	{
		Collection coleccion=null;
		try
		{	
			coleccion = registroEnfermeriaDao.consultarCateterSondaTodosHisto(con, cuentas, institucion, centroCosto);
		}
		catch(Exception e)
		{
			logger.warn("Error al Consultar el histórico de los cateteres sonda fijos y parametrizados para Registro Enfermería " +e.toString());
			coleccion=null;
		}
		return coleccion;	
	}
	
	/**
	 * Método que carga las descripcion del soporte respiratorio, y las observaciones generales
	 * del paciente en la orden médica
	 * @param con
	 * @param codigoCuenta
	 * @param codigoCuentaAsocio
	 * @return
	 */
	
	public boolean cargarInfoOrdenMedica (Connection con, String cuentas)
	{
		Collection coleccion=null;
		boolean resultado = false;
		
		try
		{
			coleccion=registroEnfermeriaDao.cargarDieta (con, cuentas, 1);
			Iterator ite=coleccion.iterator();
			
			if (ite.hasNext())
			{
				
				this.observacionesOrdenMedica="";
				HashMap col=(HashMap) ite.next();
				if ( UtilidadCadena.noEsVacio(col.get("descripcion_dieta_oral")+"") ) 
				{ 
		      		String cad = UtilidadCadena.replaceToken(col.get("descripcion_dieta_oral")+"", '\n', "<br>");
		      		
					this.descripcionDieta = cad;
				}
				else { this.descripcionDieta = "";	}
				
//				if ( UtilidadCadena.noEsVacio(col.get("descripcion_soporte")+"") ) 
//					this.descripcionSoporteOrdenMedica=UtilidadCadena.replaceToken(col.get("descripcion_soporte")+"", '\n', "<br>");
//				else
//					this.descripcionSoporteOrdenMedica="";
				
				coleccion=registroEnfermeriaDao.cargarDieta (con, cuentas, 3);
				Iterator ite1=coleccion.iterator();
				
				this.observacionesOrdenMedica="";

				while (ite1.hasNext())
				{
					HashMap col1=(HashMap) ite1.next();
						
						if (col1.get("observacion") != null && col1.get("fecha_orden") != null
								&& col1.get("datos_medico") != null && !col1.get("observacion").toString().trim().isEmpty()) 
						{
							String detObservacion=col1.get("fecha_orden").toString()+"\n"+
													col1.get("observacion").toString()+"\n"+
													col1.get("datos_medico").toString()+"\n\n";
							this.observacionesOrdenMedica+=detObservacion;
						}
							
						else
							this.observacionesOrdenMedica+="";
				}
				resultado = true;
//				return true;
			}
			else
			{
				this.descripcionDieta="";
//				this.descripcionSoporteOrdenMedica="";
				this.observacionesOrdenMedica="";
//				return false;
				resultado = false;
			}
			
			coleccion=registroEnfermeriaDao.cargarDieta (con, cuentas, 4);
			Iterator ite2=coleccion.iterator();
			
			while (ite2.hasNext())	{
				HashMap col=(HashMap) ite2.next();
				if ( UtilidadCadena.noEsVacio(col.get("descripcion")+"") ) {
					this.descripcionSoporteOrdenMedica += col.get("descripcion")+"";
					resultado = true;
				} else{
					this.descripcionSoporteOrdenMedica +="";	
				}	
			}
			
			return resultado;
		}
		catch(Exception e)
		{
		  logger.warn("Error al Consultar la descripción del soporte y las observaciones grales de la orden médica " +e.toString());
		  coleccion=null;
		}
		 return false;		
	}
	
	
	
	/**
	 * Método que inserta la información ingresada en los cuidados especiales de enfermería
	 * @param con
	 * @param codEncabezado
	 * @param medico
	 * @return
	 */
	public boolean insertarCuidadosEspeciales(Connection con, int codEncabezado, UsuarioBasico medico) throws SQLException 
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		boolean error=false;
		int resp=0;
				
		if (registroEnfermeriaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (RegistroEnfermeriaDao - insertarCuidadosEspeciales )");
		}
		
		//Iniciamos la transacción
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
		//-----------Codigo de los cuidados de enfermería parametrizados por institución centro costo y otros----------//
		Vector codigosCuidadosEnfer=(Vector) this.getMapaCuidadosEspeciales("codigosCuidadoEnf");
		//-----------Codigo que indican si el cuidado de enfermería es parametrizado o otro----------//
		Vector tiposCuidadoEnf=(Vector) this.getMapaCuidadosEspeciales("tiposCuidadoEnf");
		
		Utilidades.imprimirMapa(this.getMapaCuidadosEspeciales());
		
		if (codigosCuidadosEnfer != null && tiposCuidadoEnf != null)
		{						
			for(int i=0; i<codigosCuidadosEnfer.size();i++)
			{
				//------El codigo por institución centro del tipo de cuidado de enfermeria parametrizado u otro-----// 
				int tipoCuidado = Integer.parseInt(codigosCuidadosEnfer.elementAt(i)+"");
				
				//------El tipo de cuidado de enfermeria 0->Parametrizado   1->Otro cuidado
				int tipoCuidadoEnfer = Integer.parseInt(tiposCuidadoEnf.elementAt(i)+"");
				
				//------Radio que indica si presenta o no ---------//
				String presenta = (String)this.getMapaCuidadosEspeciales("presentaCuidado_"+tipoCuidado);
				
				//--- Descripción del tipo de cuidado de enfermería-------//
				String descripcion = (String)this.getMapaCuidadosEspeciales("descripcionCuidado_"+tipoCuidado);
				
				//-Verificar que se haya seleccionado el tipo de cuidado
				if (UtilidadCadena.noEsVacio(presenta) || (UtilidadCadena.noEsVacio(descripcion)) )
				{
					/*
						if(UtilidadTexto.getBoolean(this.getMapaCuidadosEspeciales("indicativoControlEspecial_"+tipoCuidado)+""))
						{
							descripcion=descripcion+"\n"+medico.getLoginUsuario();
						}
						else
						{
							//--------Si la descripción no es vacía se agrega la fecha/hora y profesional que hizo la observación------//
							if (UtilidadCadena.noEsVacio(descripcion))
							{
								String nuevaDescripcion="Fecha/Hora Registro: "+this.fechaRegistro+" "+this.horaRegistro+"\n"+descripcion;
								descripcion=UtilidadTexto.agregarTextoAObservacionFechaGrabacion("", nuevaDescripcion, medico, false);
							}
						}
						*/
						descripcion=descripcion+"\n"+medico.getLoginUsuario();
						resp=registroEnfermeriaDao.insertarDetalleCuidadoEnfermeria(
								con, 
								codEncabezado, 
								tipoCuidado,
								presenta, 
								descripcion,
								tipoCuidadoEnfer);
						
						if (resp < 1)
						{
							error=true;
							break;
						}
				}	
			}
		}//if codigosCuidadoEnf != null
		
		//Se finaliza la transacción cuando hay error en una inserción de datos o no se logró inicializar la transacción
		if (!inicioTrans || error)
		{
			myFactory.abortTransaction(con);
			return false;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		
		return true;
	}
	
	/**
	 * Método que inserta la información de la subsección Escala Glasgow
	 * @param con
	 * @param codEncabezado
	 * @param usuario
	 * @return
	 */
	public boolean insertarEscalaGlasgow (Connection con, int codEncabezado, UsuarioBasico usuario) throws SQLException 
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));

		boolean error=false;
		int resp=0;
				
		if (registroEnfermeriaDao==null)
		{
			throw new SQLException ("No se pudo inicializar la conexión con la fuente de datos (RegistroEnfermeriaDao - insertarEscalaGlasgow )");
		}
		
		//Iniciamos la transacción
		boolean inicioTrans;
		
		inicioTrans=myFactory.beginTransaction(con);
		
		//logger.info("\n ENTRO A INSERTAR DETALLE GLASGOW\n");
		//logger.info("\n codigos glasgow -->"+this.getMapaEscalaGlasgow("codigosGlasgow")+"\n");
		
		//-----------Codigos glasgow----------//
		Vector codigosGlasgow=(Vector) this.getMapaEscalaGlasgow("codigosGlasgow");
				
		if (codigosGlasgow != null)
		{
			for(int i=0; i<codigosGlasgow.size();i++)
			{
				//------El codigo de la característica de escala glasgow-----// 
				int codigoGlasgow = Integer.parseInt(codigosGlasgow.elementAt(i)+"");
				
				//-----Especificación Glasgow ---------//
				String especificacionGlasgow = (String)this.getMapaEscalaGlasgow("especificacion_"+codigoGlasgow);
				
				//----Observación Especificación Glasgow ---------//
				String observacionEspGlasgow = (String)this.getMapaEscalaGlasgow("obserEspecificacion_"+codigoGlasgow);
				
				//logger.info("\n especificacionGlasgow-->"+especificacionGlasgow+" \n");
				//logger.info("\n observacionEspGlasgow-->"+observacionEspGlasgow+" \n");
				
				//-Verificar que no esté null la especificación glasgow
				if (UtilidadCadena.noEsVacio(especificacionGlasgow))
				{
						//--------Si la descripción no es vacía se agrega la fecha/hora y profesional que hizo la observación------//
						if (UtilidadCadena.noEsVacio(observacionEspGlasgow))
						{
							//String nuevaObservacion="Fecha/Hora Registro: "+this.fechaRegistro+" "+this.horaRegistro+"\n";
							observacionEspGlasgow=UtilidadTexto.agregarTextoAObservacionFechaGrabacion("", observacionEspGlasgow, usuario, false);
						}
						resp=registroEnfermeriaDao.insertarDetalleEscalaGlasgow(con, codEncabezado, Integer.parseInt(especificacionGlasgow), observacionEspGlasgow);
						
						if (resp < 1)
						{
							error=true;
							break;
						}
				}	
			}
		}//if codigosGlasgow != null
		
		//Se finaliza la transacción cuando hay error en una inserción de datos o no se logró inicializar la transacción
		if (!inicioTrans || error)
		{
			myFactory.abortTransaction(con);
			return false;
		}
		else
		{
		    myFactory.endTransaction(con);
		}
		
		return true;
	}
	
	/**
	 * Método que consulta el histórico de la escala glasgow
	 * del paciente
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @return
	 */
	public HashMap consultarHistoricoEscalaGlasgow(Connection con, String cuentas)
	{
		return registroEnfermeriaDao.consultarHistoricoEscalaGlasgow(con, cuentas);
	}
	
	/**
	 * Método que consulta las columnas de los cuidados especiales de enfermería de la orden médica 
	 * y del registro de enfermería en el juego de información de la sección
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaFin 
	 * @param fechaInicio 
	 * @return HashMap
	 */
	public HashMap consultarColsCuidadosEspeciales (Connection con, String cuentas, String fechaInicio, String fechaFin)
	{
		return registroEnfermeriaDao.consultarColsCuidadosEspeciales(con, cuentas,fechaInicio,fechaFin);	 
	}
	
	/**
	 * Método que cargar la información ingresada en la orden médica de la hoja neurológica
	 * del paciente en la orden médica
	 * @param con
	 * @param codigoCuenta
	 * @param codigoCuentaAsocio
	 * @return
	 */
	public boolean cargarInfoHojaNeurologica (Connection con, String cuentas)
	{
		Collection coleccion=null;
		
		try
		{
			coleccion=registroEnfermeriaDao.cargarInfoHojaNeurologica (con, cuentas);
			Iterator ite=coleccion.iterator();
			
			this.existeHojaNeurologica = "true";
			if (ite.hasNext())
			{
				HashMap col=(HashMap) ite.next();
				//this.existeHojaNeurologica= (col.get("presenta")+"").equals("null")  ? "" : (col.get("presenta")+"");
				this.existeHojaNeurologica="true";
   				this.finalizadaHojaNeurologica= (col.get("finalizada")+"").equals("null")  ? "" : (col.get("finalizada")+"");
   				this.fechaFinalizacionHNeurologica= (col.get("fecha_grabacion")+"").equals("null")  ? "" : (col.get("fecha_grabacion")+"");
   				this.medicoHNeurologica= (col.get("medico")+"").equals("null")  ? "" : (col.get("medico")+"");
   				this.fechaGrabacionHojaNeuro= (col.get("fecha_fin")+"").equals("null")  ? "" : (col.get("fecha_fin")+"");
				
				return true;
			}
			else
			{
				this.existeHojaNeurologica="true";
				this.finalizadaHojaNeurologica="";
				this.fechaFinalizacionHNeurologica="";
				this.medicoHNeurologica="";
				this.fechaGrabacionHojaNeuro="";
				return false;
			}
		}
		catch(Exception e)
		{
		  logger.warn("Error al Consultar la Info ingresada en la orden médica de la Hoja Neurológica " +e.toString());
		  coleccion=null;
		}
		 return false;		
	}
	
	/**
	 * Método para consultar la información de anotaciones de enfermería de acuerdo a los parámetros
	 * de búsqueda, para ser mostrada como sección en la impresión de historía clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos			
	 * @return listadoAnotacionesEnfermeria
	 */
	public Collection consultarAnotacionesEnfermeriaImpresionHC (Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion)
	{
		return registroEnfermeriaDao.consultarAnotacionesEnfermeriaImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion);
	}
	
	/**
	 * Método para consultar la información historica de los signos vitales fijos de acuerdo a los parámetros
	 * de búsqueda, para ser mostrada como sección en la impresión de historía clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos			
	 * @return 
	 */
	public Collection consultarSignosVitalesFijosHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion) 
	{
		return registroEnfermeriaDao.consultarSignosVitalesFijosHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion);
	}
	
	/**
	 * Mètodo que consulta la información histórica de los signos vitales parametrizados por institución centro de
	 * costo, de acuerdo a los parámetros de busqueda, para ser mostrada como sección en la impresión de historia clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos		
	 * @return
	 */
	public Collection consultarSignosVitalesParamHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion) 
	{
		return registroEnfermeriaDao.consultarSignosVitalesParamHistoImpresionHC(con,cuentas,fechaInicial,fechaFinal,horaInicial,horaFinal,mostrarInformacion);
	}
	
	/**
	 * Método para consultar el listado con los códigos històricos, fecha registro,hora registro, usuario
     * de los signos vitales fijos y parametrizados, para la impresión de historia clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos		
	 * @return
	 */
	public Collection consultarSignosVitalesHistoTodosImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion) 
	{
		return registroEnfermeriaDao.consultarSignosVitalesHistoTodosImpresionHC(con,cuentas,fechaInicial,fechaFinal,horaInicial,horaFinal,mostrarInformacion);
	}
	
	/**
	 * Método para consultar la información de cateter sonda de las columnas fijas, para la 
	 * impresión de la historia clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos		
	 * @return
	 */
	public Collection consultarCateterSondaFijosHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion) 
	{
		return registroEnfermeriaDao.consultarCateterSondaFijosHistoImpresionHC(con, cuentas,fechaInicial,fechaFinal,horaInicial,horaFinal,mostrarInformacion);
	}
	
	/**
	 * Método para consultar la información de cateter sonda de las columnas parametrizadas, para la 
	 * impresión de la historia clínica 
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos	
	 * @return
	 */
	public Collection consultarCateterSondaParamHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion) 
	{
		return registroEnfermeriaDao.consultarCateterSondaParamHistoImpresionHC(con, cuentas,fechaInicial,fechaFinal,horaInicial,horaFinal,mostrarInformacion);
	}
	
	/**
	 * Mètodo para consultar los codigos históricos, fecha hora de registro, nombre usuario de los cateter sonda
	 * fijos y parametrizados
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos	
	 * @return
	 */
	public Collection consultarCateterSondaTodosHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion) 
	{
		return registroEnfermeriaDao.consultarCateterSondaTodosHistoImpresionHC(con, cuentas,fechaInicial,fechaFinal,horaInicial,horaFinal,mostrarInformacion);
	}
	
	/**
	 * Método que consulta la información del encabezado histórico de los cuidados especiales de acuerdo
	 * a los parámetros de búsqueda para mostrarse en la impresión de historia clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos	
	 * @return
	 */
	public HashMap consultarCuidadosEspecialesHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion) 
	{
		return registroEnfermeriaDao.consultarCuidadosEspecialesHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion);
	}
	
	/**
	 * Método que consulta el detalle histórico de los cuidados especiales de acuerdo
	 * a los parámetros de búsqueda para mostrarse en la impresión de historia clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos	
	 * @return
	 */
	public HashMap consultarCuidadosEspecialesDetalleHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion) 
	{
		return registroEnfermeriaDao.consultarCuidadosEspecialesDetalleHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion);
	}
	
	/**
	 * Método que consulta el histórico de escala glagow de acuerdo
	 * a los parámetros de búsqueda para mostrarse en la impresión de historia clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos	
	 * @return
	 */
	public HashMap consultarEscalaGlasgowHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion) 
	{
		return registroEnfermeriaDao.consultarEscalaGlasgowHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion);
	}
	
	/**
	 * Método que consulta el histórico de pupilas de acuerdo
	 * a los parámetros de búsqueda para mostrarse en la impresión de historia clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos	
	 * @return
	 */
	public HashMap consultarPupilasHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion) 
	{
		return registroEnfermeriaDao.consultarPupilasHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion);
	}
	
	/**
	 * Método que consulta el histórico de convulsiones de acuerdo
	 * a los parámetros de búsqueda para mostrarse en la impresión de historia clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos	
	 * @return
	 */
	public HashMap consultarConvulsionesHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion) 
	{
		return registroEnfermeriaDao.consultarConvulsionesHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion);
	}
	
	/**
	 * Método que consulta el histórico de control de esfinteres de acuerdo
	 * a los parámetros de búsqueda para mostrarse en la impresión de historia clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos	
	 * @return
	 */
	public HashMap consultarControlEsfinteresHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion) 
	{
		return registroEnfermeriaDao.consultarControlEsfinteresHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion);
	}
	
	/**
	 * Método que consulta el histórico de fuerza muscular de acuerdo
	 * a los parámetros de búsqueda para mostrarse en la impresión de historia clínica
	 * @param con
	 * @param codigoCuenta
	 * @param cuentaAsocio
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param horaInicial
	 * @param horaFinal
	 * @param mostrarInformacion (Para el caso de asocio de cuentas)
	 * 							 U -> Urgencias
	 * 							 H -> Hospitalizacion
	 * 							 A -> Ambos	
	 * @return
	 */
	public HashMap consultarFuerzaMuscularHistoImpresionHC(Connection con, String cuentas, String fechaInicial, String fechaFinal, String horaInicial, String horaFinal, String mostrarInformacion) 
	{
		return registroEnfermeriaDao.consultarFuerzaMuscularHistoImpresionHC(con, cuentas, fechaInicial, fechaFinal, horaInicial, horaFinal, mostrarInformacion);
	}

	//--------------------------------------------------------------------------------
	/**
	 * Validaciones para la modificacion de datos cuando la Nota de Enfermeria se encuentra Cerrada
	 * @param
	 * @param PersonaBasica paciente
	 * */
	public  ResultadoBoolean validarModificacionCerradaNota(Connection con,PersonaBasica paciente)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(false);
	
		//Evalua que la via de ingreso sea Hospitalización
		if(paciente.getCodigoUltimaViaIngreso() == ConstantesBD.codigoViaIngresoHospitalizacion)
		{
			//Valida que el estado del ingreso sea abierto o cerrado
			InfoDatosString info = UtilidadesHistoriaClinica.obtenerEstadoIngreso(con,paciente.getCodigoIngreso());
			
			if(info.getCodigo().equals(ConstantesIntegridadDominio.acronimoEstadoCerrado))
			{
				//Valida que el estado de la cuenta no sea anulada
				//falta realizar valdiacion de la cuenta
				//if(UtilidadesHistoriaClinica.obtenerEstadoCuenta(con,paciente.getCodigoCuenta()).getCodigo() == ConstantesBD.codigoEstadoCuenta )
	
				//Se valida que tenga cama asignada
				int codigoEstadoCama = Utilidades.obtenerCodigoEstadoCama(con,paciente.getCodigoCama());
				
				if(paciente.getCodigoCama() > 0 && (codigoEstadoCama == ConstantesBD.codigoEstadoCamaOcupada ||
						 codigoEstadoCama == ConstantesBD.codigoEstadoCamaPendientePorTrasladar || 
							codigoEstadoCama == ConstantesBD.codigoEstadoCamaPendientePorRemitir ||
								codigoEstadoCama == ConstantesBD.codigoEstadoCamaConSalida))
				{
					resultado.setResultado(true);					
				}			
				else 
				{
					resultado.setResultado(false);
					resultado.setDescripcion("El ingreso se encuentra cerrado  y no posee cama");
				}
			}
			else			
				resultado.setResultado(true);		
		}
		else		
			//Para las demas vias de ingreso no se realizan validaciones
			resultado.setResultado(true);
	
		return resultado;
	}	

	//--------------------------------------------------------------------------------
	
	/**
	 * Realiza las validaciones para la Apertura de Notas
	 * @param Connection con
	 * @param PersonaBasica paciente
	 * */
	public  ResultadoBoolean validacionesBotonAperturaNota(Connection con,PersonaBasica paciente)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(false,"ninguno");
		
		//Evalua que la via de ingreso sea Hospitalización
		if(paciente.getCodigoUltimaViaIngreso() == ConstantesBD.codigoViaIngresoHospitalizacion)
		{
			//Valida que el estado del ingreso sea abierto o cerrado
			InfoDatosString info = UtilidadesHistoriaClinica.obtenerEstadoIngreso(con,paciente.getCodigoIngreso()); 
			if(info.getCodigo().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto) || 
					info.getCodigo().equals(ConstantesIntegridadDominio.acronimoEstadoCerrado))
			{
				//Se valida que tenga cama asignada
				int codigoEstadoCama = Utilidades.obtenerCodigoEstadoCama(con,paciente.getCodigoCama());
				
				if(paciente.getCodigoCama() > 0 && (codigoEstadoCama == ConstantesBD.codigoEstadoCamaOcupada ||
						 codigoEstadoCama == ConstantesBD.codigoEstadoCamaPendientePorTrasladar || 
							codigoEstadoCama == ConstantesBD.codigoEstadoCamaPendientePorRemitir ||
								codigoEstadoCama == ConstantesBD.codigoEstadoCamaConSalida))
				{
					resultado.setResultado(true);					
				}			
				else 
				{
					resultado.setResultado(false);
					resultado.setDescripcion("No pasa validaciones de la cama. codigo cama >> "+paciente.getCodigoCama()+" >> estado cama >> "+codigoEstadoCama);
				}
			}
			else
			{
				resultado.setResultado(false);
				resultado.setDescripcion("El estado del ingreso no es ni abierto ni cerrado");
			}
		}
		else
		{
			resultado.setResultado(false);
			resultado.setDescripcion("No posee cuenta de Hospitalizacion ");
		}
	
		return resultado;
	}
	//--------------------------------------------------------------------------------
	
	/**
	 * Realiza las validaciones para el Cierre de Notas
	 * @param Connection con
	 * @param PersonaBasica paciente
	 * */
	public  ResultadoBoolean validacionesBotonCierrreNota(Connection con,PersonaBasica paciente) 
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true);
		
		//Evalua la via de Ingreso del paciente
		if(paciente.getCodigoUltimaViaIngreso() == ConstantesBD.codigoViaIngresoHospitalizacion ||
				paciente.getCodigoUltimaViaIngreso() == ConstantesBD.codigoViaIngresoUrgencias)
		{
			Egreso egreso = new Egreso();
			
			try
			{
				if(egreso.cargarEgresoGeneral(con,paciente.getCodigoCuenta()))
				{				
					//Valida que posea Egreso Medico o Administrativo.
					if(!(egreso.getDestinoSalida().getCodigo() != ConstantesBD.codigoNuncaValido &&								 
							egreso.getUsuarioGrabaEgreso().getCodigoPersona() <= 0) && 
								!UtilidadValidacion.existeEgresoCompleto(con,paciente.getCodigoCuenta()))				
					{
						resultado.setResultado(false);
						resultado.setDescripcion("No es un Egreso Medico y tampoco administrativo. egreso destino salida >> "+egreso.getDestinoSalida().getCodigo()+" >> numero evolucion >> "+egreso.getNumeroEvolucion()+" >> medico responsable >> "+egreso.getMedicoResponsable().getCodigoPersona()+" >> Usuario Graba Egreso >> "+egreso.getUsuarioGrabaEgreso().getCodigoPersona()+" egreso completo >> "+UtilidadValidacion.existeEgresoCompleto(con,paciente.getCodigoCuenta()));
						return resultado;
					}
				}
				else
				{
					resultado.setResultado(false);					
					resultado.setDescripcion("NO se Encontro Egreso");
					return resultado;
				}
				
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			resultado.setResultado(false);					
			resultado.setDescripcion("Las vias de Ingreso son diferentes de Hospitalización y Urgencias");
			return resultado;
		}
		
		//Valida que las ordenes de medicamentos se encuentren en estado medico administrada, anulada o cargo directo
		if(Utilidades.consultarCuantosSolicitudesEstadoMedico(
				con, 
				paciente.getCodigoCuenta(), 
				ConstantesBD.codigoTipoSolicitudMedicamentos, 
				ConstantesBD.codigoEstadoHCAdministrada+","+ConstantesBD.codigoEstadoHCAnulada+","+ConstantesBD.codigoEstadoHCCargoDirecto, 
				false) > 0)
		{
			resultado.setResultado(false);					
			resultado.setDescripcion("Existen Solicitudes de Medicamento con estados medicos diferentes a Administrada,Anulada o Cargo Directo");
			return resultado;
		}		
		
		return resultado;
	}
	
	//--------------------------------------------------------------------------------
	
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
			registroEnfermeriaDao = myFactory.getRegistroEnfermeriaDao();
			wasInited = (registroEnfermeriaDao != null);
		}
		return wasInited;
	}


	/**
	 * Consultar el Control de Liquidos del paciente.
	 * @param con
	 * @param parametros
	 * @return
	 */
	public HashMap consultarControlLiquidos(Connection con, HashMap parametros)
	{
		RegistroEnfermeriaDao	registroEnferDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroEnfermeriaDao();
		return registroEnferDao.consultarControlLiquidos(con, parametros);
	}

	
	/**
	 * @return Retorna descripcionDieta.
	 */
	public String getDescripcionDieta() {
		return descripcionDieta;
	}
	/**
	 * @param Asigna descripcionDieta.
	 */
	public void setDescripcionDieta(String descripcionDieta) {
		this.descripcionDieta = descripcionDieta;
	}
	/**
	 * @return Retorna tiposNutricionOral.
	 */
	public String getTiposNutricionOral() {
		return tiposNutricionOral;
	}
	/**
	 * @param Asigna tiposNutricionOral.
	 */
	public void setTiposNutricionOral(String tiposNutricionOral) {
		this.tiposNutricionOral = tiposNutricionOral;
	}


	/**
	 * @return Retorna fechaRegistro.
	 */
	public String getFechaRegistro() {
		return fechaRegistro;
	}
	/**
	 * @param Asigna fechaRegistro.
	 */
	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}
	/**
	 * @return Retorna horaRegistro.
	 */
	public String getHoraRegistro() {
		return horaRegistro;
	}
	/**
	 * @param Asigna horaRegistro.
	 */
	public void setHoraRegistro(String horaRegistro) {
		this.horaRegistro = horaRegistro;
	}

	/**
	 * @return Retorna mapaDieta.
	 */
	public HashMap getMapaDieta() {
		return mapaDieta;
	}
	/**
	 * @param Asigna mapaDieta.
	 */
	public void setMapaDieta(HashMap mapaDieta) {
		this.mapaDieta = mapaDieta;
	}
	/**
	 * @return Retorna mapaDieta.
	 */
	public Object getMapaDieta(Object key) {
		return mapaDieta.get(key);
	}
	/**
	 * @param Asigna mapaDieta.
	 */
	public void setMapaDieta(Object key, Object dato) {
		this.mapaDieta.put(key, dato);
	}
	/**
	 * @return Retorna mapaDietaHistorico.
	 */
	public HashMap getMapaDietaHistorico() {
		return mapaDietaHistorico;
	}

	/**
	 * @param Asigna mapaDietaHistorico.
	 */
	public void setMapaDietaHistorico(HashMap mapaDietaHistorico) {
		this.mapaDietaHistorico = mapaDietaHistorico;
	}

	/**
	 * @return Retorna mapaDieta.
	 */
	public Object getMapaDietaHistorico(Object key) {
		return mapaDieta.get(key);
	}
	/**
	 * @param Asigna mapaDieta.
	 */
	public void setMapaDietaHistorico(Object key, Object dato) {
		this.mapaDieta.put(key, dato);
	}
	
	
	/**
	 * @return Retorna diagnosticosEnfermeria.
	 */
	public Vector getDiagnosticosEnfermeria() {
		return diagnosticosEnfermeria;
	}
	/**
	 * @param Asigna diagnosticosEnfermeria.
	 */
	public void setDiagnosticosEnfermeria(Vector diagnosticosEnfermeria) {
		this.diagnosticosEnfermeria = diagnosticosEnfermeria;
	}
	/**
	 * @return Retorna hayDieta.
	 */
	public boolean getHayDieta() {
		return hayDieta;
	}
	/**
	 * @param Asigna hayDieta.
	 */
	public void setHayDieta(boolean hayDieta) {
		this.hayDieta = hayDieta;
	}
	/**
	 * @return Retorna nutricionOral.
	 */
	public String getNutricionOral() {
		return nutricionOral;
	}
	/**
	 * @param Asigna nutricionOral.
	 */
	public void setNutricionOral(String nutricionOral) {
		this.nutricionOral = nutricionOral;
	}
	/**
	 * @return Retorna nutricionParenteral.
	 */
	public String getNutricionParenteral() {
		return nutricionParenteral;
	}
	/**
	 * @param Asigna nutricionParenteral.
	 */
	public void setNutricionParenteral(String nutricionParenteral) {
		this.nutricionParenteral = nutricionParenteral;
	}
	/**
	 * @return Returns the frecuenciaCardiaca.
	 */
	public String getFrecuenciaCardiaca()
	{
		return frecuenciaCardiaca;
	}
	/**
	 * @param frecuenciaCardiaca The frecuenciaCardiaca to set.
	 */
	public void setFrecuenciaCardiaca(String frecuenciaCardiaca)
	{
		this.frecuenciaCardiaca = frecuenciaCardiaca;
	}
	/**
	 * @return Returns the frecuenciaRespiratoria.
	 */
	public String getFrecuenciaRespiratoria()
	{
		return frecuenciaRespiratoria;
	}
	/**
	 * @param frecuenciaRespiratoria The frecuenciaRespiratoria to set.
	 */
	public void setFrecuenciaRespiratoria(String frecuenciaRespiratoria)
	{
		this.frecuenciaRespiratoria = frecuenciaRespiratoria;
	}
	/**
	 * @return Returns the presionArterialDiastolica.
	 */
	public String getPresionArterialDiastolica()
	{
		return presionArterialDiastolica;
	}
	/**
	 * @param presionArterialDiastolica The presionArterialDiastolica to set.
	 */
	public void setPresionArterialDiastolica(String presionArterialDiastolica)
	{
		this.presionArterialDiastolica = presionArterialDiastolica;
	}
	/**
	 * @return Returns the presionArterialMedia.
	 */
	public String getPresionArterialMedia()
	{
		return presionArterialMedia;
	}
	/**
	 * @param presionArterialMedia The presionArterialMedia to set.
	 */
	public void setPresionArterialMedia(String presionArterialMedia)
	{
		this.presionArterialMedia = presionArterialMedia;
	}
	/**
	 * @return Returns the presionArterialSistolica.
	 */
	public String getPresionArterialSistolica()
	{
		return presionArterialSistolica;
	}
	/**
	 * @param presionArterialSistolica The presionArterialSistolica to set.
	 */
	public void setPresionArterialSistolica(String presionArterialSistolica)
	{
		this.presionArterialSistolica = presionArterialSistolica;
	}
	/**
	 * @return Returns the temperaturaPaciente.
	 */
	public String getTemperaturaPaciente()
	{
		return temperaturaPaciente;
	}
	/**
	 * @param temperaturaPaciente The temperaturaPaciente to set.
	 */
	public void setTemperaturaPaciente(String temperaturaPaciente)
	{
		this.temperaturaPaciente = temperaturaPaciente;
	}
	
	/**
	 * @return Retorna mapaSignosVitales.
	 */
	public HashMap getMapaSignosVitales() {
		return mapaSignosVitales;
	}
	/**
	 * @param Asigna mapaSignosVitales.
	 */
	public void setMapaSignosVitales(HashMap mapaSignosVitales) {
		this.mapaSignosVitales = mapaSignosVitales;
	}
	/**
	 * @return Retorna mapaSignosVitales.
	 */
	public Object getMapaSignosVitales(Object key) {
		return mapaSignosVitales.get(key);
	}
	/**
	 * @param Asigna mapaSignosVitales.
	 */
	public void setMapaSignosVitales(Object key, Object dato) {
		this.mapaSignosVitales.put(key, dato);
	}
	
	/**
	 * @return Retorna mapaExamenesFisicos.
	 */
	public HashMap getMapaExamenesFisicos() {
		return mapaExamenesFisicos;
	}
	/**
	 * @param Asigna mapaExamenesFisicos.
	 */
	public void setMapaExamenesFisicos(HashMap mapaExamenesFisicos) {
		this.mapaExamenesFisicos = mapaExamenesFisicos;
	}
	/**
	 * @return Retorna mapaExamenesFisicos.
	 */
	public Object getMapaExamenesFisicos(Object key) {
		return mapaExamenesFisicos.get(key);
	}
	/**
	 * @param Asigna dato.
	 */
	public void setMapaExamenesFisicos(Object key, Object dato) {
		this.mapaExamenesFisicos.put(key, dato);
	}

	/**
	 * @return Returns the anotacionEnfermeria.
	 */
	public String getAnotacionEnfermeria()
	{
		return anotacionEnfermeria;
	}
	/**
	 * @param anotacionEnfermeria The anotacionEnfermeria to set.
	 */
	public void setAnotacionEnfermeria(String anotacionEnfermeria)
	{
		this.anotacionEnfermeria = anotacionEnfermeria;
	}
	
	/**
	 * @return Retorna mapaCateterSonda.
	 */
	public HashMap getMapaCateterSonda() {
		return mapaCateterSonda;
	}
	/**
	 * @param Asigna mapaCateterSonda.
	 */
	public void setMapaCateterSonda(HashMap mapaCateterSonda) {
		this.mapaCateterSonda = mapaCateterSonda;
	}
	/**
	 * @return Retorna mapaCateterSonda.
	 */
	public Object getMapaCateterSonda(Object key) {
		return mapaCateterSonda.get(key);
	}
	/**
	 * @param Asigna dato.
	 */
	public void setMapaCateterSonda(Object key, Object dato) {
		this.mapaCateterSonda.put(key, dato);
	}

	/**
	 * @return Retorna obsSoporte.
	 */
	public String getObsSoporte()
	{
		return obsSoporte;
	}

	/**
	 * @param obsSoporte Asigna obsSoporte.
	 */
	public void setObsSoporte(String obsSoporte)
	{
		this.obsSoporte = obsSoporte;
	}

	/**
	 * @return Retorna soporteRespiratorio.
	 */
	public Vector getSoporteRespiratorio()
	{
		return soporteRespiratorio;
	}

	/**
	 * @param soporteRespiratorio Asigna soporteRespiratorio.
	 */
	public void setSoporteRespiratorio(Vector soporteRespiratorio)
	{
		this.soporteRespiratorio = soporteRespiratorio;
	}
	
	/**
	 * @return Retorna finalizarTurno.
	 */
	public boolean getFinalizarTurno() {
		return finalizarTurno;
	}
	/**
	 * @param Asigna finalizarTurno.
	 */
	public void setFinalizarTurno(boolean finalizarTurno) {
		this.finalizarTurno = finalizarTurno;
	}

	/**
	 * @return Returns the descripcionSoporteOrdenMedica.
	 */
	public String getDescripcionSoporteOrdenMedica()
	{
		return descripcionSoporteOrdenMedica;
	}
	/**
	 * @param descripcionSoporteOrdenMedica The descripcionSoporteOrdenMedica to set.
	 */
	public void setDescripcionSoporteOrdenMedica(
			String descripcionSoporteOrdenMedica)
	{
		this.descripcionSoporteOrdenMedica = descripcionSoporteOrdenMedica;
	}
	/**
	 * @return Returns the observacionesOrdenMedica.
	 */
	public String getObservacionesOrdenMedica()
	{
		return observacionesOrdenMedica;
	}
	/**
	 * @param observacionesOrdenMedica The observacionesOrdenMedica to set.
	 */
	public void setObservacionesOrdenMedica(String observacionesOrdenMedica)
	{
		this.observacionesOrdenMedica = observacionesOrdenMedica;
	}

	
	/**
	 * @return Retorna mapaCuidadosEspeciales.
	 */
	public HashMap getMapaCuidadosEspeciales() {
		return mapaCuidadosEspeciales;
	}
	/**
	 * @param Asigna mapaCuidadosEspeciales.
	 */
	public void setMapaCuidadosEspeciales(HashMap mapaCuidadosEspeciales) {
		this.mapaCuidadosEspeciales = mapaCuidadosEspeciales;
	}
	/**
	 * @return Retorna mapaCuidadosEspeciales.
	 */
	public Object getMapaCuidadosEspeciales(Object key) {
		return mapaCuidadosEspeciales.get(key);
	}
	/**
	 * @param Asigna dato.
	 */
	public void setMapaCuidadosEspeciales(Object key, Object dato) {
		this.mapaCuidadosEspeciales.put(key, dato);
	}
	/**
	 * @return Retorna the existeHojaNeurologica.
	 */
	public String getExisteHojaNeurologica()
	{
		return existeHojaNeurologica;
	}

	/**
	 * @param existeHojaNeurologica The existeHojaNeurologica to set.
	 */
	public void setExisteHojaNeurologica(String existeHojaNeurologica)
	{
		this.existeHojaNeurologica = existeHojaNeurologica;
	}

	/**
	 * @return Retorna the finalizadaHojaNeurologica.
	 */
	public String getFinalizadaHojaNeurologica()
	{
		return finalizadaHojaNeurologica;
	}

	/**
	 * @param finalizadaHojaNeurologica The finalizadaHojaNeurologica to set.
	 */
	public void setFinalizadaHojaNeurologica(String finalizadaHojaNeurologica)
	{
		this.finalizadaHojaNeurologica = finalizadaHojaNeurologica;
	}
	
	/**
	 * @return Retorna the fechaGrabacionHojaNeuro.
	 */
	public String getFechaGrabacionHojaNeuro()
	{
		return fechaGrabacionHojaNeuro;
	}

	/**
	 * @param fechaGrabacionHojaNeuro The fechaGrabacionHojaNeuro to set.
	 */
	public void setFechaGrabacionHojaNeuro(
			String especialidadesMedicoHNeurologica)
	{
		this.fechaGrabacionHojaNeuro = especialidadesMedicoHNeurologica;
	}

	/**
	 * @return Retorna the fechaFinalizacionHNeurologica.
	 */
	public String getFechaFinalizacionHNeurologica()
	{
		return fechaFinalizacionHNeurologica;
	}

	/**
	 * @param fechaFinalizacionHNeurologica The fechaFinalizacionHNeurologica to set.
	 */
	public void setFechaFinalizacionHNeurologica(
			String fechaFinalizacionHNeurologica)
	{
		this.fechaFinalizacionHNeurologica = fechaFinalizacionHNeurologica;
	}

	/**
	 * @return Retorna the medicoHNeurologica.
	 */
	public String getMedicoHNeurologica()
	{
		return medicoHNeurologica;
	}

	/**
	 * @param medicoHNeurologica The medicoHNeurologica to set.
	 */
	public void setMedicoHNeurologica(String medicoHNeurologica)
	{
		this.medicoHNeurologica = medicoHNeurologica;
	}

	/**
	 * @return Retorna mapaEscalaGlasgow.
	 */
	public HashMap getMapaEscalaGlasgow() {
		return mapaEscalaGlasgow;
	}
	/**
	 * @param Asigna mapaEscalaGlasgow.
	 */
	public void setMapaEscalaGlasgow(HashMap mapaEscalaGlasgow) {
		this.mapaEscalaGlasgow = mapaEscalaGlasgow;
	}
	/**
	 * @return Retorna mapaEscalaGlasgow.
	 */
	public Object getMapaEscalaGlasgow(Object key) {
		return mapaEscalaGlasgow.get(key);
	}
	/**
	 * @param Asigna dato.
	 */
	public void setMapaEscalaGlasgow(Object key, Object dato) {
		this.mapaEscalaGlasgow.put(key, dato);
	}

	/**
	 * @return Retorna mapaDietaOrdenes
	 */
	public HashMap getMapaDietaOrdenes() {
		return mapaDietaOrdenes;
	}

	/**
	 * @param Asigna mapaDietaHistorico.
	 */
	public void setMapaDietaOrdenes(HashMap mapaOrdenes) {
		this.mapaDietaOrdenes = mapaOrdenes;
	}

	/** 
	 * @return Retorna mapaDieta.
	 */
	public Object getMapaDietaOrdenes(Object key) {
		return mapaDietaOrdenes.get(key);
	}
	/**
	 * @param Asigna mapaDieta.
	 */
	public void setMapaDietaOrdenes(Object key, Object dato) {
		this.mapaDietaOrdenes.put(key, dato);
	}

	//************************************************metodos para la seccion convulsiones**************************************************** //
	
	/**
	 * 
	 */
	public HashMap obtenerTiposConvulsiones(Connection con) 
	{
		return registroEnfermeriaDao.obtenerTiposConvulsiones(con);
	}
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean guardarConvulsiones(Connection con, HashMap vo) 
	{
		return registroEnfermeriaDao.guardarConvulsiones(con,vo);
	}
	

	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public HashMap obtenerHistoricosConvulsiones(Connection con, String cuentas) 
	{
		return registroEnfermeriaDao.obtenerHistoricosConvulsiones(con,cuentas);
	}
	//*******************************************FIN METODOS CONVULSIONES ************************************///
//	**************************METODOS DE LA SECCION DE CONTROL DE ESFINTERES *************************
	/**
	 * Método para consultar el historico de la seccion de control de esfinteres
	 * segun un la cuenta del paciente
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public HashMap consultarHistoricoControlEsfinteres(Connection con, String cuentas)
	{
		RegistroEnfermeriaDao	registroEnferDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroEnfermeriaDao();
		return registroEnferDao.consultarHistoricoControlEsfinteres(con, cuentas);
	}
	
	/**
	 * Metodo para traer la informacion de liquidos del paciente en las ultimas 24 y Mostrarla en evoluciones. 
	 * @param con
	 * @param codigoCuenta
	 * @param centroCosto
	 * @param institucion
	 * @param fechaInicio
	 * @param codigoCuentaAsocio
	 * @param mapa
	 * @return
	 */
	public boolean cargarDietaEvolucion(Connection con, String cuentas, int centroCosto, int institucion, String fechaInicio, HashMap mapa)
	{
		RegistroEnfermeriaDao	registroEnferDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroEnfermeriaDao();
		try
		{	
			//-Cargar los liquidos y medicamentos infusion registrados anteriormente
			mapa.clear();
			mapa.putAll(registroEnferDao.consultarLiqMedicamentosPaciente (con, cuentas, centroCosto, institucion, 0, "", fechaInicio) );
			
			//-Insertar la misma información en los hidden para poder hacer el despazamiento por los historicos 
			int nroRegMedNoParam = Integer.parseInt( mapa.get("numRegistros")+"");
			if ( nroRegMedNoParam > 0 )
			{
			   for(int j = 0 ; j < nroRegMedNoParam ; j ++)
				 { 
				    mapa.put("h_registro_enfer_" + j, mapa.get("registro_enfer_"+ j));
				    mapa.put("h_consecutivo_liquido_" + j, mapa.get("consecutivo_liquido_"+ j));
				    mapa.put("h_descripcion_" + j, mapa.get("descripcion_"+ j));
				    mapa.put("h_volumen_total_" + j, mapa.get("volumen_total_"+ j));
				    mapa.put("h_velocidad_infusion_" + j, mapa.get("velocidad_infusion_"+ j));
				    mapa.put("h_suspender_" + j, mapa.get("suspender_"+ j));
				 }   	
			}

			//---Copiar solo los nombres y los codigos de los medicamentos parametrizados y no pàrametrizados.
			HashMap mp = new HashMap();
			mp = registroEnferDao.consultarLiqMedicamentosPaciente(con, cuentas, centroCosto, institucion, 1, "", fechaInicio);
			mapa.put("nroRegMedAdm", mp.get("numRegistros")+"");
			mp.remove("numRegistros");
			mapa.putAll(mp);

			
			//---Copiar en el mapa la información registrada en el balance de liquidos administrados. 
			/*
			mp = new HashMap();
			mp = registroEnferDao.consultarLiqMedicamentosPaciente(con, codigoCuenta, centroCosto, institucion, 2, "", fechaInicio, codigoCuentaAsocio);
			mapa.put("nroRegBalLiqAdm", mp.get("numRegistros")+"");
			mp.remove("numRegistros");
			mapa.putAll(mp);
			*/

			
			//------Cargar los codigo y los nombres de los liquidos eliminados. (SOLAMENTE HAY PARAMETRIZABLES).
			mp.clear();
			mp = registroEnferDao.consultarLiqMedicamentosPaciente(con, cuentas, centroCosto, institucion, 5, "", fechaInicio);
			mapa.put("nroRegMedElim", mp.get("numRegistros")+"");
			mp.remove("numRegistros");
			mapa.putAll(mp);
			
			//---Copiar en el mapa la información registrada en el balance de liquidos eliminados. 
			/*mp = new HashMap();
			mp = registroEnferDao.consultarLiqMedicamentosPaciente(con, codigoCuenta, centroCosto, institucion, 6, "", fechaInicio, codigoCuentaAsocio);
			mapa.put("nroRegBalLiqElim", mp.get("numRegistros")+"");*/

			
			return true;
			//-------------------------------------------------------------------------------------------------------------------------------------------
			//-------------------------------------------------------------------------------------------------------------------------------------------
			//-------------------------------------------------------------------------------------------------------------------------------------------
			//------Cargar Las Mezclas Registradas en ordenes Medicas. 
			
			/*this.setMapaDietaOrdenes( registroEnferDao.consultarMezclaOrdenes (con, 0, codigoCuenta, codigoCuentaAsocio) ); 

			//------Cargar Las Mezclas y el detalle de cada articulo que conforma cada mezcla. 
			mp = new HashMap();
			mp = registroEnferDao.consultarMezclaOrdenes (con, 1, codigoCuenta, codigoCuentaAsocio); 
			this.setMapaDietaOrdenes("nroArticulos", mp.get("numRegistros")+"");
			mp.remove("numRegistros");
			this.mapaDietaOrdenes.putAll(mp);*/

			//-------------------------------------------------------------------------------------------------------------------------------------------
			//-------------------------------------------------------------------------------------------------------------------------------------------
		}				
		catch(Exception e)
		{
			logger.warn("Error al Consultar la dieta de la Orden Medica (Mundo de registro de enfermeria) " +e.toString());
			return false;		
		} 
	}
	
	
	/**
	 * Realiza validaciones para ingreso por paciente
	 * @param Connection con
	 * @param ActionMapping mapping
	 * @param HttpServletRequest request
	 * @param UsuarioBasico usuario
	 * @param PersonaBasica paciente
	 * @param RegistroEnfermeriaForm forma
	 * @param RegistroEnfermeria mundo
	 * */
	public ActionForward validacionesAccionPaciente(
			Connection con,
			ActionMapping mapping,
			HttpServletRequest request,
			UsuarioBasico usuario,
			PersonaBasica paciente,
			RegistroEnfermeriaForm forma)
	{
		if(usuario == null )
		{
		    return ComunAction.accionSalirCasoError(mapping, request, con, logger, "No existe el usuario", "errors.usuario.noCargado", true);
		}
		//----Validar que el usuario sea profesional de la salud--------//
		else if(!UtilidadValidacion.esProfesionalSalud(usuario))
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "Usuario no es profesional salud", "errors.noProfesionalSalud", true);
		}
		else if(paciente==null || paciente.getCodigoPersona()<=0)
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.noCargado", "errors.paciente.noCargado", true);
		}		
		///Validación de autoatencion
		ResultadoBoolean respuesta = UtilidadesHistoriaClinica.esUsuarioAutoatendido(usuario, paciente);
		if(respuesta.isTrue())
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "El paciente no puede ser autoatendido", respuesta.getDescripcion(), true);
				 
		if(!UtilidadesHistoriaClinica.obtenerEstadoIngreso(con, paciente.getCodigoIngreso()).getCodigo().trim().equals(ConstantesIntegridadDominio.acronimoEstadoAbierto))
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.ingresoEstadoDiferente", "errors.ingresoEstadoDiferenteAbierto", true);
		}
		if(UtilidadValidacion.esCuentaValida(con, paciente.getCodigoCuenta())<1)
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.cuentaNoValida", "errors.paciente.cuentaNoValida", true);
		}
		if(paciente.getCodigoCuenta()==0)
		{
			return ComunAction.accionSalirCasoError(mapping, request, con, logger, "errors.paciente.cuentaNoAbierta", "errors.paciente.cuentaNoAbierta", true);
		}
		
		  /*********************************************************************************************************************/
    	/** Se verifica que el ingreso no sea realizado atraves de entidades subcontratadas*/
		if (paciente.esIngresoEntidadSubcontratada())
		{
			request.setAttribute("codigoDescripcionError", "error.ingresoEntidadSubContratada");
			return mapping.findForward("paginaError");
		}
		/*********************************************************************************************************************/		
		
		return null;
	}
	
	/**
	 * Método para insertar el detalle de la seccion de Control de Esfinteres
	 * @param con
	 * @param codigoEncabeRegEnfer
	 * @param codigoCaracControlEnfinter
	 * @param observacion
	 * @return
	 */
	public int insertarDetControlEsfinteres(Connection con, int codigoEncabeRegEnfer, int codigoCaracControlEnfinter, String observacion)
	{
		RegistroEnfermeriaDao	registroEnferDao = DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroEnfermeriaDao();
		return registroEnferDao.insertarDetControlEsfinteres(con, codigoEncabeRegEnfer, codigoCaracControlEnfinter, observacion);
	}
	

	/**
	 * Consultar las solicitudes 
	 * @param con
	 * @param mp
	 * @return
	 */
	public HashMap consultarTomaMuestras(Connection con, HashMap mp)
	{
		return registroEnfermeriaDao.consultarTomaMuestras(con, mp);
	}
	
	
	//*****************************FIN DE METOSO DE LA SECCION DE CONTROL DE ESFINTERES **************************

	
	//******************************METODOS FUERZA MUSCUALR***********//////////////////

	/**
	 * 
	 * @param con
	 * @return
	 */
	public HashMap consultarTiposFuerzaMuscular(Connection con)
	{
		return registroEnfermeriaDao.consultarTiposFuerzaMuscular(con);
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean guardarFuerzaMuscular(Connection con, HashMap vo)
	{
		return registroEnfermeriaDao.guardarFuerzaMuscular(con, vo);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public HashMap obtenerHistoricosFuerzaMuscular(Connection con, String codigoCuenta)
	{
		return registroEnfermeriaDao.obtenerHistoricosFuerzaMuscular(con, codigoCuenta);
	}

	//****************************** fin METODOS FUERZA MUSCUALR***********//////////////////

	
	//********** metodos de pupilas
	/**
	 * @param con
	 * @param codEncabezado
	 * @param tamanioD
	 * @param tamanioI
	 * @param reaccionD
	 * @param reaccionI
	 * @param obsDerecha @todo
	 * @param obsIzquierda @todo
	 * @return
	 */
	public int accionGuardarPupila(Connection con, int codEncabezado, int tamanioD, int tamanioI, String reaccionD, String reaccionI, String obsDerecha, String obsIzquierda)
	{
		return registroEnfermeriaDao.accionGuardarPupila(con, codEncabezado, tamanioD, tamanioI, reaccionD, reaccionI, obsDerecha, obsIzquierda);
	}

	/**
	 * @param con
	 * @param fechaInicio
	 * @param codigoCuenta
	 * @param codigoCuentaAsocio
	 * @return
	 */
	public HashMap consultarHistoricoPupilas(Connection con, String fechaInicio, String cuentas)
	{
		return registroEnfermeriaDao.consultarHistoricoPupilas(con, fechaInicio, cuentas);
	}

	/**
	 * @return Retorna fechaGrabacionDietaOrden.
	 */
	public String getFechaGrabacionDietaOrden() {
		return fechaGrabacionDietaOrden;
	}

	/**
	 * @param Asigna fechaGrabacionDietaOrden.
	 */
	public void setFechaGrabacionDietaOrden(String fechaGrabacionDietaOrden) {
		this.fechaGrabacionDietaOrden = fechaGrabacionDietaOrden;
	}

	/**
	 * @return Retorna fechaRegistroDietaOrden.
	 */
	public String getFechaRegistroDietaOrden() {
		return fechaRegistroDietaOrden;
	}

	/**
	 * @param Asigna fechaRegistroDietaOrden.
	 */
	public void setFechaRegistroDietaOrden(String fechaRegistroDietaOrden) {
		this.fechaRegistroDietaOrden = fechaRegistroDietaOrden;
	}


	/**
	 * @return Retorna medicoDietaOrden.
	 */
	public String getMedicoDietaOrden() {
		return medicoDietaOrden;
	}

	/**
	 * @param Asigna medicoDietaOrden.
	 */
	public void setMedicoDietaOrden(String medicoDietaOrden) {
		this.medicoDietaOrden = medicoDietaOrden;
	}

	/**
	 * @return Retorna observacionDietaParenteOrden.
	 */
	public String getObservacionDietaParenteOrden() {
		return observacionDietaParenteOrden;
	}

	/**
	 * @param Asigna observacionDietaParenteOrden.
	 */
	public void setObservacionDietaParenteOrden(String observacionDietaParenteOrden) {
		this.observacionDietaParenteOrden = observacionDietaParenteOrden;
	}


	//-----------mapaMuestra
	
	/**
	 * @return Retorna mapaPupilas.
	 */
	public Object getMapaMuestra(String key)
	{
		return mapaMuestra.get(key+"");
	}

	/**
	 * @param mapaPupilas Asigna mapaPupilas.
	 */
	public void setMapaMuestra(String key, String valor)
	{
		this.mapaMuestra.put(key,valor);
	}	

	/**
	 * @return Retorna mapaPupilas.
	 */
	public HashMap getMapaMuestra()
	{
		return mapaMuestra;
	}

	/**
	 * @param mapaPupilas Asigna mapaPupilas.
	 */
	public void setMapaMuestra(HashMap mapa)
	{
		this.mapaMuestra = mapa;
	}

	public Collection consultarAnotacionesEnfermeriaFechas(Connection con, String cuentas)
	{
		Collection coleccion=null;
		try
		{	
			coleccion = registroEnfermeriaDao.consultarAnotacionesEnfermeriaFechas(con, cuentas);
		}
		catch(Exception e)
		{
			logger.warn("Error al Consultar el histórico de las Anotaciones de Enfermería para Registro Enfermería " +e.toString());
			coleccion=null;
		}
		return coleccion;	
	}

	public String getSuspendidoEnfermeria() {
		return suspendidoEnfermeria;
	}

	public void setSuspendidoEnfermeria(String suspendidoEnfermeria) {
		this.suspendidoEnfermeria = suspendidoEnfermeria;
	}

	public String getObservacionesEnfermeria() {
		return observacionesEnfermeria;
	}

	public void setObservacionesEnfermeria(String observacionesEnfermeria) {
		this.observacionesEnfermeria = observacionesEnfermeria;
	}

	public int consultarUltimaDietaActiva(Connection con, int codigoCuenta) {
		// TODO Auto-generated method stub
		return registroEnfermeriaDao.consultarUltimaDietaActiva(con, codigoCuenta);
	}

	public boolean actualizarOrdenDieta(Connection con, int codOrdenDieta, boolean estadoDietaEnfermeria, String observacionesEnfermeria) 
	{
		
		logger.info("ESTADO DE LA ORDEN DIETA EN MUNDO ->"+estadoDietaEnfermeria);
		if(estadoDietaEnfermeria == true)
			setSuspendidoEnfermeria(ConstantesBD.acronimoSi);
		else
			if(estadoDietaEnfermeria == false)
				setSuspendidoEnfermeria(ConstantesBD.acronimoNo);
		
		logger.info("ESTADO DE LA VARIABLE DIETA EN EL MUNDO ->"+getSuspendidoEnfermeria());
				
		return registroEnfermeriaDao.actualizarOrdenDieta(con, codOrdenDieta, getSuspendidoEnfermeria(), observacionesEnfermeria);
		
	}

	public String consultarParametroInterfazNutricion(Connection con) 
	{
		return registroEnfermeriaDao.consultarParametroInterfaz(con);
	}

	public HashMap tiposNutricionOralActivo(Connection con, int codigoCuenta) 
	{
		return registroEnfermeriaDao.tiposNutricionOralActivo(con, codigoCuenta);
	}

	/**
	 * Consultar la Fecha de la Orden de la Dieta para Enviar a otro sistema Interfaz
	 * @param con
	 * @param codOrdenDieta
	 * @return
	 */
	public String consultarFechaDieta(Connection con, int codOrdenDieta) 
	{
		return registroEnfermeriaDao.consultarFechaDieta(con, codOrdenDieta);
	}
	
	/**
	 * Consultar la Hora de la Orden de la Dieta para Enviar a otro sistema Interfaz
	 * @param con
	 * @param codOrdenDieta
	 * @return
	 */
	public String consultarHoraDieta(Connection con, int codOrdenDieta) 
	{
		return registroEnfermeriaDao.consultarHoraDieta(con, codOrdenDieta);
	}
	
	/**
	 * Consultar El piso al que esta asociado el paciente para enviar a sistema interfaz
	 * @param con
	 * @param codigoCama
	 * @return
	 */
	public String consultarPisoCama(Connection con, int codigoCama) 
	{
		return registroEnfermeriaDao.consultarPisoCama(con, codigoCama);
	}
	
	/**
	 * Consultar el Numero de Cama que esta asociado al paciente para enviar por sistema interfaz
	 * @param con
	 * @param codigoCama
	 * @return
	 */
	public String consultarNumeroCama(Connection con, int codigoCama) 
	{
		return registroEnfermeriaDao.consultarNumeroCama(con, codigoCama);
	}
	
	/**
	 * Consultar el convenio que tiene asociado el paciente es VIP para enviar a sistema interfaz
	 * @param con
	 * @param codigoConvenio
	 * @return
	 */
	public String consultarConvenioVip(Connection con, int codigoConvenio) 
	{
		return registroEnfermeriaDao.consultarConvenioVip(con, codigoConvenio);
	}
	
	/**
	 * Método para saber si existe o no el registro de enfermeria para el paciente. 
	 * @param con -> conexion
	 * @param cuenta
	 * @return codigo si existe sino retorna -1
	 */
	public InfoDatosInt existeRegistroEnfermeria(Connection con, int cuenta)
	{
		return registroEnfermeriaDao.existeRegistroEnfermeria(con, cuenta);
	}
	
	/**
     * Actualiza el indicador de Nota finalizada
     * @param Connection con 
     * @param String codigoPk
     * @param boolean finalizada 
     * */
    public boolean actualizarRegistroEnfermeria(Connection con,String codigoPk,boolean finalizada)
    {
    	HashMap parametros = new HashMap();
    	parametros.put("codigoPk",codigoPk);
    	parametros.put("finalizada",finalizada?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
    	return registroEnfermeriaDao.actualizarRegistroEnfermeria(con, parametros);
    }
    
    
    /**
     * 
     * @param con
     * @param cuentasPacientes
     * @param todasMezclas flag, que indica si debe buscar todas las mezclas o solo las pendientes por confirmar.
     * @return
     */
    public static boolean pacienteTieneOrdenesMezclas(Connection con,ArrayList<CuentasPaciente> cuentasPacientes,boolean todasMezclas)
    {
    	return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroEnfermeriaDao().pacienteTieneOrdenesMezclas(con,cuentasPacientes,todasMezclas);
    }

    /**
     * 
     * @param con
     * @param cuentasPacienteArray
     * @return
     */
	public HashMap mezclasPendientesPaciente(Connection con,ArrayList<CuentasPaciente> cuentasPacienteArray) 
	{
		return registroEnfermeriaDao.mezclasPendientesPaciente(con,cuentasPacienteArray);
	}
	

	/**
	 * 
	 * @param con
	 * @param ingresoPaciente
	 * @param cuentaPaciente
	 * @param centroCostoPaciente
	 * @param cargarParametrizacion
	 * @param esHistoricos
	 * @return
	 */
	public static ArrayList<Object> cargarValoracionEnfermeria(Connection con,int ingresoPaciente, int cuentaPaciente, int centroCostoPaciente, boolean cargarParametrizacion,boolean esHistoricos) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroEnfermeriaDao().cargarValoracionEnfermeria(con,ingresoPaciente,cuentaPaciente,centroCostoPaciente,cargarParametrizacion,esHistoricos);
	}
	/**
	 * 
	 * @param con
	 * @param codigoEncabezado
	 * @param resultadoLaboratorios
	 * @return
	 */
	public static int insertarValoracionEnfermeria(Connection con,int codigoEncabezado,ArrayList<Object> valoracionEnfermeria) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroEnfermeriaDao().insertarValoracionEnfermeria(con,codigoEncabezado,valoracionEnfermeria);
	}

	/**
	 * 
	 * @param con
	 * @param codigoEncabezado
	 * @param resultadoLaboratorios
	 * @return
	 */
	public static int insertaResultadosLaboratorios(Connection con,int codigoEncabezado,
			ArrayList<Object> resultadoLaboratorios) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroEnfermeriaDao().insertaResultadosLaboratorios(con,codigoEncabezado,resultadoLaboratorios);
	}

	/**
	 * Inserta los registros de alerta cada vez que se genera un registro de orden medica
	 * @param con
	 * @param listaRegistroOrdenesMedicas
	 * @return Arreglo con el nombre de las secciones que no se guardaron para mostrar en el log de errores.
	 * 		   Si se guardaron todos los registros este arreglo debe retornar vacio.
	 */
	public static ArrayList<String> insertarRegistroAlertaOrdenesMedicas(Connection con, 
			ArrayList<DtoRegistroAlertaOrdenesMedicas> listaRegistroOrdenesMedicas) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroEnfermeriaDao().
			insertarRegistroAlertaOrdenesMedicas(con, listaRegistroOrdenesMedicas);
	}
	
	/**
	 * Metódo encargado de consultar las secciones con alerta de registro de órden mádica para
	 * para una cuenta dada y estado activo
	 * @param con
	 * @param cuenta
	 * @param seccion
	 * @return
	 */
	public static HashMap<Integer, DtoRegistroAlertaOrdenesMedicas> consultarAlertaOrdenMedicaCuenta (Connection con, long cuenta) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroEnfermeriaDao().
		consultarAlertaOrdenMedicaCuenta(con, cuenta);
	}
		
	/**
	 * Método encargado de actualizar el registro de alerta de nuevas ordenes medicas e
	 * inactivar los registros que ya fueron revisados.
	 * Se recibe la fecha en que se inicio el registro de enfermería para validar que 
	 * no se cambie el estado a las alertas que hayan sido generadas por el medico mientras
	 * la enfermera tenía abierta la ventana de registro de enfermería.
	 * 
	 * @param con
	 * @param listaRegistroOrdenesMedicas
	 * @param fechaInicioRevision
	 * @param horaInicioRevision
	 * @return 
	 */
	public static boolean actualizarRegistroAlertaOrdenesMedicas(Connection con, 
			HashMap<Integer, DtoRegistroAlertaOrdenesMedicas> listaRegistroOrdenesMedicas, 
			String fechaInicioRevision, String horaInicioRevision, long registroEnfermeria,
			String usuarioModifica) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroEnfermeriaDao().
		actualizarRegistroAlertaOrdenesMedicas(con, listaRegistroOrdenesMedicas, fechaInicioRevision, 
				horaInicioRevision, registroEnfermeria, usuarioModifica);
		
	}
}