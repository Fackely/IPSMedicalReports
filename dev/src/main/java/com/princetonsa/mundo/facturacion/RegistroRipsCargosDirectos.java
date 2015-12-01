/*
 * 11 Abril, 2008
 */
package com.princetonsa.mundo.facturacion;


import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.LogsAxioma;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.RegistroRipsCargosDirectosDao;
import com.princetonsa.dto.cargos.DtoCargoDirecto;
import com.princetonsa.dto.cargos.DtoCargoDirectoHC;
import com.princetonsa.dto.cargos.DtoDiagnosticosCargoDirectoHC;
import com.princetonsa.dto.historiaClinica.DtoInformacionParto;
import com.princetonsa.dto.historiaClinica.DtoInformacionRecienNacido;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.CargosDirectos;
import com.princetonsa.mundo.ordenesmedicas.cirugias.HojaQuirurgica;
import com.princetonsa.mundo.ordenesmedicas.cirugias.SolicitudesCx;

/**
 * @author Sebastián Gómez R
 *
 *Clase que representa el Mundo con sus atributos y métodos de la funcionalidad
 * Registro Rips Cargos Directos 
 */
public class RegistroRipsCargosDirectos 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(RegistroRipsCargosDirectos.class);
	
	/**
	 * DAO para el manejo de RegistroRipsCargosDirectosDao
	 */
	private RegistroRipsCargosDirectosDao cargosDirectosDao=null;
	
	public static final String[] camposListadoCuentas = {"consecutivoIngreso_","estadoIngreso_","fechaIngreso_",
		"horaIngreso_","idCuenta_","fechaApertura_","horaApertura_","estadoCuenta_","viaIngreso_","responsable_",
		"centroAtencion_","nombrePaciente_","codigoPaciente_"};
	
	public static final String[] camposListadoSolicitudes = {"orden_","numeroSolicitud_","fechaSolicitud_","horaSolicitud_",
		"fechaSolicitudBd_","tipoServicio_","codigoTipoServicio_","nombreServicio_","tipoSolicitud_","codigoServicio_"};
	
	
	private HashMap<String,Object> datos = new HashMap<String, Object>();
	private HashMap<String,Object> infoRecienNacidos = new HashMap<String, Object>();
	private String tipoServicio;
	private String numeroSolicitud;
	private int tipoSolicitud;
	private ActionErrors errores = new ActionErrors();
	
	//**********INICIALIZADORES & CONSTRUCTORES***********************
	/**
	 * Constructor
	 */
	public RegistroRipsCargosDirectos() 
	{
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * método para inicializar los datos
	 *
	 */
	public void clean()
	{
		this.datos = new HashMap<String, Object>();
		this.infoRecienNacidos = new HashMap<String, Object>();
		this.tipoServicio = "";
		this.numeroSolicitud = "";
		this.tipoSolicitud = 0;
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
		if (cargosDirectosDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			cargosDirectosDao = myFactory.getRegistroRipsCargosDirectosDao();
		}	
	}
	
	/**
	 * Método que retorna el DAO instanciado de Registro Rips Cargos Directos
	 * @return
	 */
	public static RegistroRipsCargosDirectosDao cargosDirectosDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroRipsCargosDirectosDao();
	}
	
	//*****************************************************************************************
	//********************MÉTODOS**************************************************************
	/**
	 * Método para consultar el listado de cuentas que tienen soolicitudes de cargos directos
	 */
	public static HashMap<String, Object> listadoCuentas(Connection con,int codigoPaciente,int centroAtencion,String fechaInicial,String fechaFinal,int estadoCuenta,int viaIngreso,int convenio)
	{
		HashMap campos = new HashMap();
		campos.put("codigoPaciente",codigoPaciente);
		campos.put("centroAtencion",centroAtencion);
		campos.put("fechaInicial",fechaInicial);
		campos.put("fechaFinal",fechaFinal);
		campos.put("estadoCuenta",estadoCuenta);
		campos.put("viaIngreso",viaIngreso);
		campos.put("convenio",convenio);
		return cargosDirectosDao().listadoCuentas(con, campos);
	
	}
	
	/**
	 * Método que consulta el listado de solicitudes de cargos directos de una cuenta
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap<String, Object> listadoSolicitudes(Connection con,String idCuenta)
	{
		HashMap campos = new HashMap();
		campos.put("idCuenta",idCuenta);
		return cargosDirectosDao().listadoSolicitudes(con, campos);
	}
	
	
	/**
	 * Método implementado para guardar la información de los rips dependiendo del tipo de servicio
	 * @param con
	 * @param usuario
	 * @return
	 */
	public boolean guardar(Connection con, UsuarioBasico usuario)
	{
		boolean exito = false;
		boolean huboModificacion = false;
		
		
		//*******************REGISTRO RIPS TIPO SOLICITUD CARGOS DIRECTOS SERVICIOS******************************************
		if(this.tipoSolicitud==ConstantesBD.codigoTipoSolicitudCargosDirectosServicios)
		{
			CargosDirectos mundoCargos = new CargosDirectos();
			
			DtoCargoDirectoHC dtoCargoAnterior = mundoCargos.consultarInformacionHC(con, this.numeroSolicitud);
			
			if(this.tipoServicio.equals(ConstantesBD.codigoServicioInterconsulta+"")||this.tipoServicio.equals(ConstantesBD.codigoServicioProcedimiento+""))
				this.cargarDatosServicios(con,mundoCargos,usuario);
			else
				this.cargarDatosServiciosCirugia(con,mundoCargos,usuario);
			
			huboModificacion = this.verificarModificacionServicios(dtoCargoAnterior,mundoCargos.getCargoDirectoHC());
			
			if(huboModificacion)
			{
				if(mundoCargos.insertar(con)>0)
				{
					exito = true;
					this.generarLogCargosServicios(dtoCargoAnterior,mundoCargos.getCargoDirectoHC(),usuario);
				}
				else
					this.errores.add("",new ActionMessage("errors.ingresoDatos","la información RIPS del servicio "+this.datos.get("descripcionServicio")+". Por favor intente más tarde"));
			}
			
		}
		//******************REGISTRO RIPS TIPO SOLICITUD CIRUGIAS**********************
		else if(this.tipoSolicitud==ConstantesBD.codigoTipoSolicitudCirugia)
		{
			//Se toman los datos actuales de la cirugia en variables temporales
			HashMap<String,Object> datosTemp = this.datos;
			HashMap<String,Object> infoRecienNacidosTemp = this.infoRecienNacidos;
			
			this.datos = new HashMap<String, Object>();
			this.infoRecienNacidos = new HashMap<String, Object>();
			
			//Se consultan los datos del estado actual de la cirugia
			consultarInformacionCirugia(con, usuario);
			
			//Se asigna esa informacion como la anterior
			HashMap datosAnterior = this.datos;
			HashMap infoRecienNacidosAnterior = this.infoRecienNacidos;
			
			//SE recupera la informacion que se va a insertar o modificar
			this.datos = new HashMap<String, Object>();
			this.infoRecienNacidos = new HashMap<String, Object>();
			this.datos = datosTemp;
			this.infoRecienNacidos = infoRecienNacidosTemp;
			
			//Se verifica si se modificó la cirugia
			huboModificacion = this.verificarModificacionCirugias(datosAnterior,infoRecienNacidosAnterior);
			
			logger.info("huboModificacion=> "+huboModificacion);
			if(huboModificacion)
			{
				exito = guardarInformacionCirugia(con,usuario);
				
				if(exito)
					this.generarLogCirugias(datosAnterior, infoRecienNacidosAnterior,usuario); 
			}
			
		}
		//*************************************************************************************************
		
		
		return exito;
	}
	
	/**
	 * Método para generar el log de cirugías
	 * @param datosAnterior
	 * @param infoRecienNacidosAnterior
	 * @param usuario 
	 */
	private void generarLogCirugias(HashMap datosAnterior, HashMap infoRecienNacidosAnterior, UsuarioBasico usuario) 
	{
		int tipoRegistro = ConstantesBD.tipoRegistroLogModificacion;
		String log = "";
		
			
		log = "\n            ====INFORMACION ORIGINAL DE LA ORDEN "+this.datos.get("orden")+" ===== " ;
		for(int i=0;i<Integer.parseInt(datosAnterior.get("numRegistros")+"");i++)
		{
			log += "\n*  Servicio ["+datosAnterior.get("descripcionServicio_"+i)+"]";
			log += "\n*  Finalidad Procedimiento ["+datosAnterior.get("nombreFinalidad_"+i)+"]";
			if(!UtilidadTexto.isEmpty(datosAnterior.get("diagPrincipal_"+i)+""))
			{
				String[] vector = datosAnterior.get("diagPrincipal_"+i).toString().split(ConstantesBD.separadorSplit);
				log += "\n*  Dx. Principal ["+vector[0]+"-"+vector[1]+"]";
			}
			else
				log += "\n*  Dx. Principal [Ninguno]";
			for(int j=0;j<Integer.parseInt(datosAnterior.get("numDiagnosticos_"+i).toString());j++)
				if(UtilidadTexto.getBoolean(datosAnterior.get("checkDiagRel_"+i+"_"+j)+""))
				{
					String[] vector = datosAnterior.get("diagRelacionado_"+i+"_"+j).toString().split(ConstantesBD.separadorSplit);
					log += "\n*  Dx. Relacionado N°"+(j+1)+" ["+vector[0]+"-"+vector[1]+"]";
				}
			if(!UtilidadTexto.isEmpty(datosAnterior.get("diagComplicacion_"+i)+""))
			{
				String[] vector = datosAnterior.get("diagComplicacion_"+i).toString().split(ConstantesBD.separadorSplit);
				log += "\n*  Dx. Complicación ["+vector[0]+"-"+vector[1]+"]";
			}
			log +="\n";
		}
		
		//Se ingresa la informacion del recién nacido original
		if(UtilidadTexto.getBoolean(infoRecienNacidosAnterior.get("abrirSeccion")+"")&&UtilidadTexto.getBoolean(infoRecienNacidosAnterior.get("existeBD")+""))
		{
			log += "\n*  Edad Gestacional ["+infoRecienNacidosAnterior.get("edadGestacional")+"]";
			log += "\n*  Control Prenatal ["+ValoresPorDefecto.getIntegridadDominio(infoRecienNacidosAnterior.get("controlPrenatal")+"")+"]";
			log += "\n*  Número de Hijos ["+infoRecienNacidosAnterior.get("nroHijos")+"]";
			
			for(int i=0;i<Utilidades.convertirAEntero(infoRecienNacidosAnterior.get("nroHijos").toString());i++)
			{
				log += "\n*  Hijo N°"+(i+1)+":";
				log += "\n  *  Fecha de Nacimiento ["+infoRecienNacidosAnterior.get("fechaNacimiento_"+i)+"]";
				log += "\n  *  Hora de Nacimiento ["+infoRecienNacidosAnterior.get("horaNacimiento_"+i)+"]";
				log += "\n  *  Sexo ["+infoRecienNacidosAnterior.get("nombreSexo_"+i)+"]";
				log += "\n  *  Peso ["+infoRecienNacidosAnterior.get("peso_"+i)+"]";
				
				if(!UtilidadTexto.isEmpty(infoRecienNacidosAnterior.get("diagnosticoRN_"+i)+""))
				{
					String[] vector = infoRecienNacidosAnterior.get("diagnosticoRN_"+i).toString().split(ConstantesBD.separadorSplit);
					log += "\n  *  Diagnóstico Recién Nacido ["+vector[0]+"-"+vector[1]+"]";
				}
				
				if(!UtilidadTexto.isEmpty(infoRecienNacidosAnterior.get("diagnosticoMuerte_"+i)+""))
				{
					String[] vector = infoRecienNacidosAnterior.get("diagnosticoMuerte_"+i).toString().split(ConstantesBD.separadorSplit);
					log += "\n  *  Diagnóstico Muerte ["+vector[0]+"-"+vector[1]+"]";
					log += "\n  *  Fecha de Muerte ["+infoRecienNacidosAnterior.get("fechaMuerte_"+i)+"]";
					log += "\n  *  Hora de Muerte ["+infoRecienNacidosAnterior.get("horaMuerte_"+i)+"]";
				}
			}
			
			log +="\n";
		}
		
		
		log += "\n            ====INFORMACION DESPUÉS DE LA MODIFICACIÓN DE LA ORDEN "+this.datos.get("orden")+" ===== " ;
		
		for(int i=0;i<Integer.parseInt(this.datos.get("numRegistros")+"");i++)
		{
			log += "\n*  Servicio ["+this.datos.get("descripcionServicio_"+i)+"]";
			log += "\n*  Finalidad Procedimiento ["+this.datos.get("nombreFinalidad_"+i)+"]";
			if(!UtilidadTexto.isEmpty(this.datos.get("diagPrincipal_"+i)+""))
			{
				String[] vector = this.datos.get("diagPrincipal_"+i).toString().split(ConstantesBD.separadorSplit);
				log += "\n*  Dx. Principal ["+vector[0]+"-"+vector[1]+"]";
			}
			else
				log += "\n*  Dx. Principal [Ninguno]";
			for(int j=0;j<Integer.parseInt(this.datos.get("numDiagnosticos_"+i).toString());j++)
				if(UtilidadTexto.getBoolean(this.datos.get("checkDiagRel_"+i+"_"+j)+""))
				{
					String[] vector = this.datos.get("diagRelacionado_"+i+"_"+j).toString().split(ConstantesBD.separadorSplit);
					log += "\n*  Dx. Relacionado N°"+(j+1)+" ["+vector[0]+"-"+vector[1]+"]";
				}
			if(!UtilidadTexto.isEmpty(this.datos.get("diagComplicacion_"+i)+""))
			{
				String[] vector = this.datos.get("diagComplicacion_"+i).toString().split(ConstantesBD.separadorSplit);
				log += "\n*  Dx. Complicación ["+vector[0]+"-"+vector[1]+"]";
			}
			log +="\n";
		}
		
		//Se ingresa la informacion del recién nacido despues de la modificacion
		if(UtilidadTexto.getBoolean(this.infoRecienNacidos.get("abrirSeccion")+""))
		{
			log += "\n*  Edad Gestacional ["+this.infoRecienNacidos.get("edadGestacional")+"]";
			log += "\n*  Control Prenatal ["+ValoresPorDefecto.getIntegridadDominio(this.infoRecienNacidos.get("controlPrenatal")+"")+"]";
			log += "\n*  Número de Hijos ["+this.infoRecienNacidos.get("nroHijos")+"]";
			
			for(int i=0;i<Utilidades.convertirAEntero(this.infoRecienNacidos.get("nroHijos").toString());i++)
			{
				log += "\n*  Hijo N°"+(i+1)+":";
				log += "\n  *  Fecha de Nacimiento ["+this.infoRecienNacidos.get("fechaNacimiento_"+i)+"]";
				log += "\n  *  Hora de Nacimiento ["+this.infoRecienNacidos.get("horaNacimiento_"+i)+"]";
				log += "\n  *  Sexo ["+this.infoRecienNacidos.get("nombreSexo_"+i)+"]";
				log += "\n  *  Peso ["+this.infoRecienNacidos.get("peso_"+i)+"]";
				
				if(!UtilidadTexto.isEmpty(this.infoRecienNacidos.get("diagnosticoRN_"+i)+""))
				{
					String[] vector = this.infoRecienNacidos.get("diagnosticoRN_"+i).toString().split(ConstantesBD.separadorSplit);
					log += "\n  *  Diagnóstico Recién Nacido ["+vector[0]+"-"+vector[1]+"]";
				}
				
				if(!UtilidadTexto.isEmpty(this.infoRecienNacidos.get("diagnosticoMuerte_"+i)+""))
				{
					String[] vector = this.infoRecienNacidos.get("diagnosticoMuerte_"+i).toString().split(ConstantesBD.separadorSplit);
					log += "\n  *  Diagnóstico Muerte ["+vector[0]+"-"+vector[1]+"]";
					log += "\n  *  Fecha de Muerte ["+this.infoRecienNacidos.get("fechaMuerte_"+i)+"]";
					log += "\n  *  Hora de Muerte ["+this.infoRecienNacidos.get("horaMuerte_"+i)+"]";
				}
			}
			
			log +="\n\n";
		}
		
		LogsAxioma.enviarLog(ConstantesBD.logRegistroRipsCargosDirectosCodigo, log, tipoRegistro, usuario.getLoginUsuario());
		
	}
	/**
	 * Método que guarda la información de la cirugia
	 * @param con
	 * @param usuario
	 * @return
	 */
	private boolean guardarInformacionCirugia(Connection con, UsuarioBasico usuario) 
	{
		boolean exito = false;
		int resp = 0;
		
		for(int i=0;i<Integer.parseInt(this.datos.get("numRegistros").toString());i++)
		{
			if(!SolicitudesCx.actualizarFinalidadServicioCx(con, this.datos.get("codigo_"+i).toString(), Integer.parseInt(this.datos.get("finalidad_"+i).toString())))
				errores.add("",new ActionMessage("errors.noPudoActualizar","la finalidad del servicio N°"+(i+1)+". Proceso cancelado"));
			
			//Se guarda el diagnostico principal
			String[] vector = this.datos.get("diagPrincipal_"+i).toString().split(ConstantesBD.separadorSplit);
			if(UtilidadTexto.isEmpty(this.datos.get("codigoDiagPrincipal_"+i)+""))
				resp = HojaQuirurgica.insertarDiagnosticoPostOperatorio(con, this.datos.get("codigo_"+i).toString(), vector[0], Integer.parseInt(vector[1]), true, false, usuario.getLoginUsuario());
			else
				resp = HojaQuirurgica.modificarDiagPotOpera(con, vector[0], vector[1], usuario.getLoginUsuario(), this.datos.get("codigoDiagPrincipal_"+i).toString())?1:0;
			if(resp<=0)
				errores.add("",new ActionMessage("errors.ingresoDatos","el diagnóstico principal del servicio N°"+(i+1)+". Proceso cancelado"));
			
			//Se guardan los diagnósticos relacionados
			for(int j=0;j<Integer.parseInt(this.datos.get("numDiagnosticos_"+i).toString());j++)
			{
				resp = 0;
				vector = this.datos.get("diagRelacionado_"+i+"_"+j).toString().split(ConstantesBD.separadorSplit);
				
				if(UtilidadTexto.isEmpty(this.datos.get("codigoDiagRelacionado_"+i+"_"+j)+""))
				{
					if(UtilidadTexto.getBoolean(this.datos.get("checkDiagRel_"+i+"_"+j)+""))
						resp = HojaQuirurgica.insertarDiagnosticoPostOperatorio(con, this.datos.get("codigo_"+i).toString(), vector[0], Integer.parseInt(vector[1]), false, false, usuario.getLoginUsuario());
					else
						resp = 1; //si no fue chequeado , no se inserta y no hay problema
				}
				else
				{
					//Se pregunta si se chequeó para saber si se modifica o elimina
					if(UtilidadTexto.getBoolean(this.datos.get("checkDiagRel_"+i+"_"+j)+""))
						resp = HojaQuirurgica.modificarDiagPotOpera(con, vector[0], vector[1], usuario.getLoginUsuario(), this.datos.get("codigoDiagRelacionado_"+i+"_"+j).toString())?1:0;
					else
						resp = HojaQuirurgica.eliminarDiagPostOpe(con, this.datos.get("codigo_"+i).toString(), this.datos.get("codigoDiagRelacionado_"+i+"_"+j).toString())?1:0;
					
				}
				
				if(resp<=0)
					errores.add("",new ActionMessage("errors.ingresoDatos","el diagnóstico relacionado N°"+(j+1)+" del servicio N°"+(i+1)+". Proceso cancelado"));
			}
			
			//Se guardan el diagnóstico de complicación
			if(!UtilidadTexto.isEmpty(this.datos.get("diagComplicacion_"+i)+""))
			{
				vector = this.datos.get("diagComplicacion_"+i).toString().split(ConstantesBD.separadorSplit);
				resp = 0;
				
				//Si el codigo diagnostico complicacion no existe se inserta, de lo contrario se modifica
				if(UtilidadTexto.isEmpty(this.datos.get("codigoDiagComplicacion_"+i)+""))
					resp = HojaQuirurgica.insertarDiagnosticoPostOperatorio(con, this.datos.get("codigo_"+i).toString(), vector[0], Integer.parseInt(vector[1]), false, true, usuario.getLoginUsuario());
				else
					resp = HojaQuirurgica.modificarDiagPotOpera(con, vector[0], vector[1], usuario.getLoginUsuario(), this.datos.get("codigoDiagComplicacion_"+i).toString())?1:0;
				
				if(resp<=0)
					errores.add("",new ActionMessage("errors.ingresoDatos","el diagnóstico complicación del servicio N°"+(i+1)+". Proceso cancelado"));
			}
		}
		
		//*********************SECCION INFORMACION RECIÉN NACIDO************************************************************
		if(UtilidadTexto.getBoolean(this.infoRecienNacidos.get("abrirSeccion").toString()))
		{
			DtoInformacionParto infoParto = new DtoInformacionParto();
			
			if(UtilidadTexto.getBoolean(this.infoRecienNacidos.get("existeBD").toString()))
			{
				infoParto.setConsecutivo(this.infoRecienNacidos.get("consecutivo").toString());
				infoParto.setExisteBaseDatos(true);
			}
			infoParto.setFinalizado(true);
			infoParto.setCodigoIngreso(UtilidadesHistoriaClinica.obtenerIdIngresoSolicitud(con, this.numeroSolicitud));
			infoParto.setNumeroSolicitud(this.numeroSolicitud);
			infoParto.setCodigoPaciente(UtilidadesHistoriaClinica.obtenerCodigoPacienteSolicitud(con, this.numeroSolicitud));
			infoParto.setParto(true);
			infoParto.setLoginUsuario(usuario.getLoginUsuario());
			infoParto.setInstitucion(usuario.getCodigoInstitucionInt());
			infoParto.setSemanasGestacional(Utilidades.convertirAEntero(this.infoRecienNacidos.get("edadGestacional")+"", true));
			infoParto.setControlPrenatal(UtilidadTexto.getBoolean(this.infoRecienNacidos.get("controlPrenatal")+""));
			infoParto.setFinalizado(true);
			
			//DTO para ingresar la información de recién nacido
			ArrayList<DtoInformacionRecienNacido> arregloRecienNacido = new ArrayList<DtoInformacionRecienNacido>();
			String[] diagnostico = null;
			
			for(int i=0;i<Integer.parseInt(this.infoRecienNacidos.get("nroHijos").toString());i++)
			{
				
				//Se llena la informacion de cada recién nacido
				DtoInformacionRecienNacido infoRecienNacido = new DtoInformacionRecienNacido();
				
				if(!UtilidadTexto.isEmpty(this.infoRecienNacidos.get("consecutivo_"+i)+""))
				{
					infoRecienNacido.setExisteBaseDatos(true);
					infoRecienNacido.setConsecutivo(this.infoRecienNacidos.get("consecutivo_"+i).toString());
				}
				
				infoRecienNacido.setCodigoIngreso(infoParto.getCodigoIngreso());
				infoRecienNacido.setNumeroSolicitud(numeroSolicitud+"");
				infoRecienNacido.setConsecutivoHijo(i+1);
				infoRecienNacido.setFechaNacimiento(UtilidadTexto.isEmpty(this.infoRecienNacidos.get("fechaNacimiento_"+i)+"")?"":this.infoRecienNacidos.get("fechaNacimiento_"+i).toString());
				infoRecienNacido.setHoraNacimiento(UtilidadTexto.isEmpty(this.infoRecienNacidos.get("horaNacimiento_"+i)+"")?"":this.infoRecienNacidos.get("horaNacimiento_"+i).toString());
				infoRecienNacido.setCodigoSexo(UtilidadTexto.isEmpty(this.infoRecienNacidos.get("sexo_"+i)+"")?0:Integer.parseInt(this.infoRecienNacidos.get("sexo_"+i).toString()));
				infoRecienNacido.setVivo(UtilidadTexto.isEmpty(this.infoRecienNacidos.get("diagnosticoMuerte_"+i)+"")?true:false);
				if(!UtilidadTexto.isEmpty(this.infoRecienNacidos.get("diagnosticoRN_"+i)+""))
				{
					diagnostico = this.infoRecienNacidos.get("diagnosticoRN_"+i).toString().split(ConstantesBD.separadorSplit);
					infoRecienNacido.setAcronimoDiagnosticoRecienNacido(diagnostico[0]);
					infoRecienNacido.setCieDiagnosticoRecienNacido(Integer.parseInt(diagnostico[1]));
						
				}
				infoRecienNacido.setFechaMuerte(UtilidadTexto.isEmpty(this.infoRecienNacidos.get("fechaMuerte_"+i)+"")?"":this.infoRecienNacidos.get("fechaMuerte_"+i).toString());
				infoRecienNacido.setHoraMuerte(UtilidadTexto.isEmpty(this.infoRecienNacidos.get("horaMuerte_"+i)+"")?"":this.infoRecienNacidos.get("horaMuerte_"+i).toString());
				if(!UtilidadTexto.isEmpty(this.infoRecienNacidos.get("diagnosticoMuerte_"+i)+""))
				{
					diagnostico = this.infoRecienNacidos.get("diagnosticoMuerte_"+i).toString().split(ConstantesBD.separadorSplit);
					infoRecienNacido.setAcronimoDiagnosticoMuerte(diagnostico[0]);
					infoRecienNacido.setCieDiagnosticoMuerte(Integer.parseInt(diagnostico[1]));
				}
				infoRecienNacido.setPesoEgreso(UtilidadTexto.isEmpty(this.infoRecienNacidos.get("peso_"+i)+"")?0:Integer.parseInt(this.infoRecienNacidos.get("peso_"+i).toString()));
				infoRecienNacido.setLoginUsuarioProceso(usuario.getLoginUsuario());
				infoRecienNacido.setFinalizada(true);
				
				arregloRecienNacido.add(infoRecienNacido);
				
			}
			
			if(arregloRecienNacido.size()>0)
				if(!UtilidadesHistoriaClinica.insertarInformacionPartoParaRips(con, infoParto, arregloRecienNacido))
					errores.add("",new ActionMessage("errors.noSeGraboInformacion","DE LA INFORMACIÓN DE PARTOS"));
			
		}
		//******************************************************************************************************************
		
		if(errores.isEmpty())
			exito = true;
		
		return exito;
	}
	/**
	 * Método que verifica su hubo modficación de cirugias
	 * @param datosAnterior
	 * @param infoRecienNacidosAnterior
	 * @return
	 */
	private boolean verificarModificacionCirugias(HashMap datosAnterior, HashMap infoRecienNacidosAnterior) 
	{
		boolean huboModificacion = false;
		
		logger.info("N° servicios orden anterior=> "+Integer.parseInt(datosAnterior.get("numRegistros").toString()));
		logger.info("N° servicios orden actual=> "+Integer.parseInt(this.datos.get("numRegistros").toString()));
		
		for(int i=0;i<Integer.parseInt(datosAnterior.get("numRegistros").toString());i++)
			for(int j=0;j<Integer.parseInt(this.datos.get("numRegistros").toString());j++)
				if(datosAnterior.get("codigo_"+i).toString().equals(this.datos.get("codigo_"+j).toString()))
				{
					logger.info("¡paso por aqui!");
					//*****cambio en la finalidad del servicio**********************************************************
					if(!datosAnterior.get("finalidad_"+i).toString().equals(this.datos.get("finalidad_"+j).toString()))
						huboModificacion = true;
					
					//****cambio en el diagnóstico principal*******************************************************+
					if(!(datosAnterior.get("diagPrincipal_"+i)+"").equals(this.datos.get("diagPrincipal_"+j)+""))
						huboModificacion = true;
					
					//***cambio en los diagnóstico relacionados********************************************************
					int numDiagAnterior = Utilidades.convertirAEntero(datosAnterior.get("numDiagnosticos_"+i).toString());
					int numDiagActual = Utilidades.convertirAEntero(this.datos.get("numDiagnosticos_"+j).toString());
					if(numDiagAnterior!=numDiagActual)
						huboModificacion = true;
					else
					{
						boolean existe = false;
						//Se verifica que el diagnostico anterior se encuentre en los diagnosticos nuevos
						for(int k=0;k<numDiagAnterior;k++)
						{
							existe = false;
							for(int l=0;l<numDiagActual;l++)
							{
								if(datosAnterior.get("diagRelacionado_"+i+"_"+k).toString().equals(this.datos.get("diagRelacionado_"+j+"_"+l).toString())&&
									datosAnterior.get("checkDiagRel_"+i+"_"+k).toString().equals(this.datos.get("checkDiagRel_"+j+"_"+l).toString()))
									existe = true;
							}
							
							//Si no se encuentra quiere decir que hubo modificación
							if(!existe)
								huboModificacion = true;
						}
					}
					
					//****cambio en el diagnóstico complicacion*******************************************************+
					if(!(datosAnterior.get("diagComplicacion_"+i)+"").equals(this.datos.get("diagComplicacion_"+j)+""))
						huboModificacion = true;
				}
		
		//************SECCION INFORMACION DE RECIEN NACIDOS************************************************
		if(UtilidadTexto.getBoolean(infoRecienNacidosAnterior.get("abrirSeccion").toString())&&
			UtilidadTexto.getBoolean(this.infoRecienNacidos.get("abrirSeccion").toString()))
		{
			if(!(infoRecienNacidosAnterior.get("edadGestacional")+"").equals(this.infoRecienNacidos.get("edadGestacional").toString()))
				huboModificacion = true;
			
			if(!(infoRecienNacidosAnterior.get("controlPrenatal")+"").equals(this.infoRecienNacidos.get("controlPrenatal").toString()))
				huboModificacion = true;
			
			int nroHijosAnterior = Utilidades.convertirAEntero(infoRecienNacidosAnterior.get("nroHijos")+"");
			int nroHijosActual = Utilidades.convertirAEntero(this.infoRecienNacidos.get("nroHijos").toString());
			if(nroHijosAnterior!=nroHijosActual)
				huboModificacion = true;
			else
			{
				for(int i=0;i<nroHijosAnterior;i++)
					for(int j=0;j<nroHijosActual;j++)
						if((infoRecienNacidosAnterior.get("consecutivo_"+i)+"").equals(this.infoRecienNacidos.get("consecutivo_"+j).toString()))
						{
							if(!(infoRecienNacidosAnterior.get("fechaNacimiento_"+i)+"").equals(this.infoRecienNacidos.get("fechaNacimiento_"+j).toString()))
								huboModificacion = true;
							
							if(!(infoRecienNacidosAnterior.get("horaNacimiento_"+i)+"").equals(this.infoRecienNacidos.get("horaNacimiento_"+j).toString()))
								huboModificacion = true;
							
							if(!(infoRecienNacidosAnterior.get("sexo_"+i)+"").equals(this.infoRecienNacidos.get("sexo_"+j).toString()))
								huboModificacion = true;
							
							if(!(infoRecienNacidosAnterior.get("peso_"+i)+"").equals(this.infoRecienNacidos.get("peso_"+j).toString()))
								huboModificacion = true;
							
							if(!(infoRecienNacidosAnterior.get("diagnosticoRN_"+i)+"").equals((this.infoRecienNacidos.get("diagnosticoRN_"+j)+"")))
								huboModificacion = true;
							
							if(!(infoRecienNacidosAnterior.get("diagnosticoMuerte_"+i)+"").equals((this.infoRecienNacidos.get("diagnosticoMuerte_"+j)+"")))
								huboModificacion = true;
							
							if(!(infoRecienNacidosAnterior.get("fechaMuerte_"+i)+"").equals((this.infoRecienNacidos.get("fechaMuerte_"+j)+"")))
								huboModificacion = true;
							
							if(!(infoRecienNacidosAnterior.get("horaMuerte_"+i)+"").equals((this.infoRecienNacidos.get("horaMuerte_"+j)+"")))
								huboModificacion = true;
							
						}
			}
			
		}
		//*************************************************************************************************
		
		return huboModificacion;
	}
	/**
	 * Método implementado para cargar los datos de un cargo directo de servicios que es de tipo cirugias
	 * @param con
	 * @param mundoCargos
	 * @param usuario
	 */
	private void cargarDatosServiciosCirugia(Connection con, CargosDirectos mundoCargos, UsuarioBasico usuario) 
	{
		if(!UtilidadTexto.isEmpty(this.datos.get("codigo_0")+""))
		{
			mundoCargos.getCargoDirectoHC().setExisteBaseDatos(true);
			mundoCargos.getCargoDirectoHC().setCodigo(this.datos.get("codigo_0").toString());
		}
		
		mundoCargos.getCargoDirectoHC().setCargado(true);
		mundoCargos.getCargoDirectoHC().setFechaSolicitud(this.datos.get("fechaSolicitud").toString());
		mundoCargos.getCargoDirectoHC().setHoraSolicitud(this.datos.get("horaSolicitud").toString());
		mundoCargos.getCargoDirectoHC().setCodigoServicio(Integer.parseInt(this.datos.get("codigoServicio_0").toString()));
		mundoCargos.getCargoDirectoHC().setCodigoTipoServicio(this.tipoServicio);
		mundoCargos.getCargoDirectoHC().setManejaRips(true);
		mundoCargos.getCargoDirectoHC().setCodigoFinalidadProcedimiento(Integer.parseInt(this.datos.get("finalidad_0").toString()));
		mundoCargos.getCargoDirectoHC().setNombreFinalidadProcedimiento(Utilidades.getNombreFinalidadServicio(con,this.datos.get("finalidad_0").toString()));
		mundoCargos.getCargoDirectoHC().setCodigoInstitucion(usuario.getCodigoInstitucionInt());
		mundoCargos.getCargoDirectoHC().setLoginUsuarioModifica(usuario.getLoginUsuario());
		mundoCargos.getCargoDirectoHC().setTipo(ConstantesIntegridadDominio.acronimoNormal);
		
		//Se añade el diagnóstico principal
		String[] vector = this.datos.get("diagPrincipal_0").toString().split(ConstantesBD.separadorSplit);
		DtoDiagnosticosCargoDirectoHC dtoDiagnostico = new DtoDiagnosticosCargoDirectoHC();
		dtoDiagnostico.setPrincipal(true);
		dtoDiagnostico.setComplicacion(false);
		dtoDiagnostico.setAcronimoDiagnostico(vector[0]);
		dtoDiagnostico.setCieDiagnostico(Integer.parseInt(vector[1]));
		mundoCargos.getCargoDirectoHC().getDiagnosticos().add(dtoDiagnostico);
		
		//Se añaden los diagnósticos relacionados
		for(int i=0;i<Integer.parseInt(this.datos.get("numDiagnosticos_0").toString());i++)
			if(UtilidadTexto.getBoolean(this.datos.get("checkDiagRel_0_"+i)+""))
			{
				
				vector = this.datos.get("diagRelacionado_0_"+i).toString().split(ConstantesBD.separadorSplit);
				DtoDiagnosticosCargoDirectoHC dtoDiagRel = new DtoDiagnosticosCargoDirectoHC();
				dtoDiagRel.setPrincipal(false);
				dtoDiagRel.setComplicacion(false);
				dtoDiagnostico.setAcronimoDiagnostico(vector[0]);
				dtoDiagnostico.setCieDiagnostico(Integer.parseInt(vector[1]));
				mundoCargos.getCargoDirectoHC().getDiagnosticos().add(dtoDiagnostico);
			}
		
		if(!UtilidadTexto.isEmpty(this.datos.get("diagComplicacion_0")+""))
		{
			//Se añade el diagnóstico de complicación
			vector = this.datos.get("diagComplicacion_0").toString().split(ConstantesBD.separadorSplit);
			DtoDiagnosticosCargoDirectoHC dtoComplicacion = new DtoDiagnosticosCargoDirectoHC();
			dtoComplicacion.setPrincipal(false);
			dtoComplicacion.setComplicacion(true);
			dtoComplicacion.setAcronimoDiagnostico(vector[0]);
			dtoComplicacion.setCieDiagnostico(Integer.parseInt(vector[1]));
			mundoCargos.getCargoDirectoHC().getDiagnosticos().add(dtoDiagnostico);
		}
		
		//Se llena el encabezado del cargo directo
		DtoCargoDirecto dtoCargo = new DtoCargoDirecto();
		dtoCargo.setExisteBaseDatos(true);
		dtoCargo.setNumeroSolicitud(this.numeroSolicitud);
		mundoCargos.getCargoDirecto().add(dtoCargo);
		
	}
	/**
	 * Método implementado para generar log de insercion/modificacion de cargos directos de servicios
	 * @param dtoCargoAnterior
	 * @param dtoCargoActual
	 * @param usuario
	 */
	private void generarLogCargosServicios(DtoCargoDirectoHC dtoCargoAnterior, DtoCargoDirectoHC dtoCargoActual, UsuarioBasico usuario) 
	{
		int tipoRegistro = ConstantesBD.codigoNuncaValido;
		String log = "";
		
		if(dtoCargoAnterior.isCargado())
		{
			tipoRegistro = ConstantesBD.tipoRegistroLogModificacion;
			 log = "\n            ====INFORMACION ORIGINAL DE LA ORDEN "+this.datos.get("orden")+" ===== " ;
			 if(this.tipoServicio.equals(ConstantesBD.codigoServicioInterconsulta+""))
			 {
				 log+= "\n*  Causa Externa ["+dtoCargoAnterior.getNombreCausaExterna()+"]";
				 log+= "\n*  Finalidad Consulta ["+dtoCargoAnterior.getNombreFinalidadConsulta()+"]";
			 }
			 else
			 {
				 log+= "\n*  Finalidad Procedimiento ["+dtoCargoAnterior.getNombreFinalidadProcedimiento()+"]";
			 }
			 
			 int cont = 1;
			 for(DtoDiagnosticosCargoDirectoHC diagTemp:dtoCargoAnterior.getDiagnosticos())
			 {
				 if(diagTemp.isPrincipal())
					 log+="\n*  Dx. Principal ";
				 else if(diagTemp.isComplicacion())
					 log+="\n*  Dx. Complicación ";
				 else
				 {
					 log+="\n*  Dx. Relacionado N°"+cont+" ";
					 cont++;
				 }
				 log+="["+diagTemp.getAcronimoDiagnostico()+"-"+diagTemp.getCieDiagnostico()+"]";
				 
				 //Si el diagnostico es principal y hay tipo de diagnóstico entonces se registra
				 if(diagTemp.isPrincipal()&&!diagTemp.getCodigoTipoDiagnostico().equals(""))
					 log+="\n*  Tipo Diagnóstico ["+ValoresPorDefecto.getIntegridadDominio(diagTemp.getCodigoTipoDiagnostico())+"]";
			 }
		}
		else
			tipoRegistro = ConstantesBD.tipoRegistroLogInsercion;
		
		if(tipoRegistro==ConstantesBD.tipoRegistroLogInsercion)
			log += "\n            ====INFORMACION INGRESADA EN LA ORDEN "+this.datos.get("orden")+" ===== " ;
		else
			log += "\n            ====INFORMACION DESPUÉS DE LA MODIFICACIÓN DE LA ORDEN "+this.datos.get("orden")+" ===== " ;
		
		if(this.tipoServicio.equals(ConstantesBD.codigoServicioInterconsulta+""))
		 {
			 log+= "\n*  Causa Externa ["+dtoCargoActual.getNombreCausaExterna()+"]";
			 log+= "\n*  Finalidad Consulta ["+dtoCargoActual.getNombreFinalidadConsulta()+"]";
		 }
		 else
		 {
			 log+= "\n*  Finalidad Procedimiento ["+dtoCargoActual.getNombreFinalidadProcedimiento()+"]";
		 }
		 
		 int cont = 1;
		 for(DtoDiagnosticosCargoDirectoHC diagTemp:dtoCargoActual.getDiagnosticos())
		 {
			 if(diagTemp.isPrincipal())
				 log+="\n*  Dx. Principal ";
			 else if(diagTemp.isComplicacion())
				 log+="\n*  Dx. Complicación ";
			 else
			 {
				 log+="\n*  Dx. Relacionado N°"+cont+" ";
				 cont++;
			 }
			 log+="["+diagTemp.getAcronimoDiagnostico()+"-"+diagTemp.getCieDiagnostico()+"]";
			 
			 //Si el diagnostico es principal y hay tipo de diagnóstico entonces se registra
			 if(diagTemp.isPrincipal()&&!diagTemp.getCodigoTipoDiagnostico().equals(""))
				 log+="\n*  Tipo Diagnóstico ["+ValoresPorDefecto.getIntegridadDominio(diagTemp.getCodigoTipoDiagnostico())+"]";
		 }
		log +="\n\n";
		 
		LogsAxioma.enviarLog(ConstantesBD.logRegistroRipsCargosDirectosCodigo, log, tipoRegistro, usuario.getLoginUsuario());
		
	}
	/**
	 * Método que verifica si hubo modificacion
	 * @param dtoCargoAnterior
	 * @param dtoCargoActual
	 * @return
	 */
	private boolean verificarModificacionServicios(DtoCargoDirectoHC dtoCargoAnterior, DtoCargoDirectoHC dtoCargoActual) 
	{
		boolean huboModificacion = false;
		
		//Si el cargo ya existía se verifica sino se toma como nuevo
		if(dtoCargoAnterior.isExisteBaseDatos())
		{
			if(this.tipoServicio.equals(ConstantesBD.codigoServicioInterconsulta+""))
			{
				if(dtoCargoAnterior.getCodigoCausaExterna()!=dtoCargoActual.getCodigoCausaExterna())
					huboModificacion = true;
				
				if(!dtoCargoAnterior.getCodigoFinalidadConsulta().equals(dtoCargoActual.getCodigoFinalidadConsulta()))
					huboModificacion = true;
			}
			else
			{
				if(dtoCargoAnterior.getCodigoFinalidadProcedimiento()!=dtoCargoActual.getCodigoFinalidadProcedimiento())
					huboModificacion = true;
			}
			
			//Se verifica si cambió el número de diagnosticos (con solo eso ya se sabe que hubo modificación)
			if(dtoCargoAnterior.getDiagnosticos().size()!=dtoCargoActual.getDiagnosticos().size())
				huboModificacion = true;
			else
			{
				boolean existe = false;
				//Se verifica que el diagnostico anterior se encuentre en los diagnosticos nuevos
				for(DtoDiagnosticosCargoDirectoHC diagTemp1:dtoCargoAnterior.getDiagnosticos())
				{
					existe = false;
					for(DtoDiagnosticosCargoDirectoHC diagTemp2:dtoCargoActual.getDiagnosticos())
					{
						if(diagTemp1.getAcronimoDiagnostico().equals(diagTemp2.getAcronimoDiagnostico())&&
							diagTemp1.getCieDiagnostico()==diagTemp2.getCieDiagnostico()&&
							diagTemp1.getCodigoTipoDiagnostico().equals(diagTemp2.getCodigoTipoDiagnostico())&&
							diagTemp1.isComplicacion()==diagTemp2.isComplicacion()&&
							diagTemp1.isPrincipal()==diagTemp2.isPrincipal())
							existe = true;
					}
					
					//Si no se encuentra quiere decir que hubo modificación
					if(!existe)
						huboModificacion = true;
				}
			}
		}
		else
			huboModificacion = true;
		
		return huboModificacion;
	}
	/**
	 * Método implementado para cargar los datos de los servicios
	 * @param con 
	 * @param mundoCargos
	 * @param usuario 
	 */
	private void cargarDatosServicios(Connection con, CargosDirectos mundoCargos, UsuarioBasico usuario) 
	{
		if(!UtilidadTexto.isEmpty(this.datos.get("codigo")+""))
		{
			mundoCargos.getCargoDirectoHC().setExisteBaseDatos(true);
			mundoCargos.getCargoDirectoHC().setCodigo(this.datos.get("codigo").toString());
		}
		
		mundoCargos.getCargoDirectoHC().setCargado(true);
		mundoCargos.getCargoDirectoHC().setFechaSolicitud(this.datos.get("fechaSolicitud").toString());
		mundoCargos.getCargoDirectoHC().setHoraSolicitud(this.datos.get("horaSolicitud").toString());
		mundoCargos.getCargoDirectoHC().setCodigoServicio(Integer.parseInt(this.datos.get("codigoServicio").toString()));
		mundoCargos.getCargoDirectoHC().setCodigoTipoServicio(this.tipoServicio);
		if(this.tipoServicio.equals(ConstantesBD.codigoServicioInterconsulta+""))
		{
			mundoCargos.getCargoDirectoHC().setCodigoCausaExterna(Integer.parseInt(this.datos.get("causaExterna").toString()));
			mundoCargos.getCargoDirectoHC().setNombreCausaExterna(UtilidadesHistoriaClinica.obtenerNombreCausaExterna(con, Integer.parseInt(this.datos.get("causaExterna").toString())));
		}
		mundoCargos.getCargoDirectoHC().setManejaRips(true);
		if(this.tipoServicio.equals(ConstantesBD.codigoServicioInterconsulta+""))
		{
			mundoCargos.getCargoDirectoHC().setCodigoFinalidadConsulta(this.datos.get("finalidad").toString());
			mundoCargos.getCargoDirectoHC().setNombreFinalidadConsulta(UtilidadesHistoriaClinica.obtenerNombreFinalidadConsulta(con, this.datos.get("finalidad").toString()));
		}
		else
		{
			mundoCargos.getCargoDirectoHC().setCodigoFinalidadProcedimiento(Integer.parseInt(this.datos.get("finalidad").toString()));
			mundoCargos.getCargoDirectoHC().setNombreFinalidadProcedimiento(Utilidades.getNombreFinalidadServicio(con,this.datos.get("finalidad").toString()));
		}
		mundoCargos.getCargoDirectoHC().setCodigoInstitucion(usuario.getCodigoInstitucionInt());
		mundoCargos.getCargoDirectoHC().setLoginUsuarioModifica(usuario.getLoginUsuario());
		mundoCargos.getCargoDirectoHC().setTipo(ConstantesIntegridadDominio.acronimoNormal);
		
		//Se añade el diagnóstico principal
		String[] vector = new String[3];
		if(!UtilidadTexto.isEmpty(this.datos.get("diagPrincipal")+""))
		{
			vector = this.datos.get("diagPrincipal").toString().split(ConstantesBD.separadorSplit);
			DtoDiagnosticosCargoDirectoHC dtoDiagnostico = new DtoDiagnosticosCargoDirectoHC();
			dtoDiagnostico.setPrincipal(true);
			dtoDiagnostico.setComplicacion(false);
			dtoDiagnostico.setAcronimoDiagnostico(vector[0]);
			dtoDiagnostico.setCieDiagnostico(Integer.parseInt(vector[1]));
			if(this.tipoServicio.equals(ConstantesBD.codigoServicioInterconsulta+""))
				dtoDiagnostico.setCodigoTipoDiagnostico(this.datos.get("tipoDiagnostico").toString());
			mundoCargos.getCargoDirectoHC().getDiagnosticos().add(dtoDiagnostico);
		}
		
		//Se añaden los diagnósticos relacionados
		for(int i=0;i<Integer.parseInt(this.datos.get("numDiagnosticos").toString());i++)
			if(UtilidadTexto.getBoolean(this.datos.get("checkDiagRel_"+i)+""))
			{
				
				vector = this.datos.get("diagRelacionado_"+i).toString().split(ConstantesBD.separadorSplit);
				DtoDiagnosticosCargoDirectoHC dtoDiagRel = new DtoDiagnosticosCargoDirectoHC();
				dtoDiagRel.setPrincipal(false);
				dtoDiagRel.setComplicacion(false);
				dtoDiagRel.setAcronimoDiagnostico(vector[0]);
				dtoDiagRel.setCieDiagnostico(Integer.parseInt(vector[1]));
				mundoCargos.getCargoDirectoHC().getDiagnosticos().add(dtoDiagRel);
			}
		
		if(!this.tipoServicio.equals(ConstantesBD.codigoServicioInterconsulta+"")&&!UtilidadTexto.isEmpty(this.datos.get("diagComplicacion")+""))
		{
			//Se añade el diagnóstico de complicación
			vector = this.datos.get("diagComplicacion").toString().split(ConstantesBD.separadorSplit);
			DtoDiagnosticosCargoDirectoHC dtoComplicacion = new DtoDiagnosticosCargoDirectoHC();
			dtoComplicacion.setPrincipal(false);
			dtoComplicacion.setComplicacion(true);
			dtoComplicacion.setAcronimoDiagnostico(vector[0]);
			dtoComplicacion.setCieDiagnostico(Integer.parseInt(vector[1]));
			mundoCargos.getCargoDirectoHC().getDiagnosticos().add(dtoComplicacion);
		}
		
		
		
		//Se llena el encabezado del cargo directo
		DtoCargoDirecto dtoCargo = new DtoCargoDirecto();
		dtoCargo.setExisteBaseDatos(true);
		dtoCargo.setNumeroSolicitud(this.numeroSolicitud);
		mundoCargos.getCargoDirecto().add(dtoCargo);
		
	}
	
	/**
	 * Método para consultar la información de una cirugía
	 * @param con
	 * @param usuario
	 */
	public void consultarInformacionCirugia(Connection con,UsuarioBasico usuario)
	{
		///Se consulta la fecha/hora inicio atencion
		HashMap fechaHoraAtencion = HojaQuirurgica.consultarDatosFechas(con, this.numeroSolicitud);
		this.datos.put("fechaInicioAtencion", fechaHoraAtencion.get("fechaInicial0"));
		this.datos.put("horaInicioAtencion", fechaHoraAtencion.get("horaInicial1"));
		
		boolean abrirInfoRecienNacidos = false;
		SolicitudesCx mundoSolCx = new SolicitudesCx();
		HashMap cirugiasSolicitud = mundoSolCx.cargarServiciosXSolicitudCx(con, this.numeroSolicitud, false);
		
		this.datos.put("numRegistros", cirugiasSolicitud.get("numRegistros"));
		
		for(int i=0;i<Integer.parseInt(cirugiasSolicitud.get("numRegistros")+"");i++)
		{
			this.datos.put("codigo_"+i, cirugiasSolicitud.get("codigo_"+i));
			this.datos.put("descripcionServicio_"+i, cirugiasSolicitud.get("descripcionServicio_"+i));
			this.datos.put("codigoServicio_"+i, cirugiasSolicitud.get("codigoServicio_"+i));
			this.datos.put("finalidad_"+i, cirugiasSolicitud.get("codigoFinalidadCx_"+i));
			this.datos.put("nombreFinalidad_"+i, cirugiasSolicitud.get("nombreFinalidadCx_"+i));
			this.datos.put("comboFinalidades_"+i, Utilidades.obtenerFinalidadesServicio(con, Integer.parseInt(cirugiasSolicitud.get("codigoServicio_"+i).toString()), usuario.getCodigoInstitucionInt()));
			
			//Se consultan los diagnosticos de la cirugia
			HashMap diagnosticos = HojaQuirurgica.cargarDiagnosticosPorServicio(con, Integer.parseInt(cirugiasSolicitud.get("codigo_"+i).toString()), 0);
			
			//Se toma el diagnostico principal
			for(int j=0;j<Integer.parseInt(diagnosticos.get("numRegistros").toString());j++)
				if(UtilidadTexto.getBoolean(diagnosticos.get("principal_"+j)+""))
				{
					this.datos.put("codigoDiagPrincipal_"+i, diagnosticos.get("codigo_"+j));
					this.datos.put("diagPrincipal_"+i, diagnosticos.get("diagnostico_"+j)+ConstantesBD.separadorSplit+diagnosticos.get("tipoCie_"+j)+ConstantesBD.separadorSplit+diagnosticos.get("nombreDiagnostico_"+j));
				}
			
			//Se toman los diagnósticos relacionados
			int cont = 0;
			String diagSeleccionados = "";
			for(int j=0;j<Integer.parseInt(diagnosticos.get("numRegistros").toString());j++)
				if(!UtilidadTexto.getBoolean(diagnosticos.get("principal_"+j)+"")&&!UtilidadTexto.getBoolean(diagnosticos.get("complicacion_"+j)+""))
				{
					diagSeleccionados += (diagSeleccionados.length()>0?",":"") + "'" +diagnosticos.get("diagnostico_"+j) + "'";
					this.datos.put("codigoDiagRelacionado_"+i+"_"+cont, diagnosticos.get("codigo_"+j));
					this.datos.put("diagRelacionado_"+i+"_"+cont,diagnosticos.get("diagnostico_"+j)+ConstantesBD.separadorSplit+diagnosticos.get("tipoCie_"+j)+ConstantesBD.separadorSplit+diagnosticos.get("nombreDiagnostico_"+j));
					this.datos.put("checkDiagRel_"+i+"_"+cont,"true");
					cont++;
				}
			this.datos.put("numDiagnosticos_"+i, cont+"");
			this.datos.put("diagSeleccionados_"+i, diagSeleccionados);
			
			//Se toma el diagnóstico de complicación
			for(int j=0;j<Integer.parseInt(diagnosticos.get("numRegistros").toString());j++)
				if(UtilidadTexto.getBoolean(diagnosticos.get("complicacion_"+j)+""))
				{
					this.datos.put("codigoDiagComplicacion_"+i, diagnosticos.get("codigo_"+j));
					this.datos.put("diagComplicacion_"+i, diagnosticos.get("diagnostico_"+j)+ConstantesBD.separadorSplit+diagnosticos.get("tipoCie_"+j)+ConstantesBD.separadorSplit+diagnosticos.get("nombreDiagnostico_"+j));
				}
			
			//Se verifica si se debe abrir la informacion de recién nacidos
			if(Utilidades.obtenerTipoServicio(con, cirugiasSolicitud.get("codigoServicio_"+i).toString()).equals(ConstantesBD.codigoServicioPartosCesarea+""))
				abrirInfoRecienNacidos = true;
		}
		
		//***********SE CONSULTA LA INFORMACIÓN DEL RECIEN NACIDO************************************************
		//Se verifica si la solicitud tiene algún servicio de partos y cesáreas
		if(abrirInfoRecienNacidos)
		{
			this.infoRecienNacidos.put("abrirSeccion", ConstantesBD.acronimoSi);
			this.infoRecienNacidos.put("seccionRecienNacidos", ConstantesBD.acronimoNo);
		
			DtoInformacionParto infoParto = UtilidadesHistoriaClinica.cargarInformacionPartoParaRips(con, this.numeroSolicitud, "");
			
			if(infoParto.isExisteBaseDatos())
			{
				this.infoRecienNacidos.put("consecutivo", infoParto.getConsecutivo());
				this.infoRecienNacidos.put("existeBD", ConstantesBD.acronimoSi);
				this.infoRecienNacidos.put("edadGestacional", infoParto.getSemanasGestacional());
				this.infoRecienNacidos.put("controlPrenatal", infoParto.isControlPrenatal()?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
				this.infoRecienNacidos.put("nroHijos", infoParto.getRecienNacidos().size());
				
				int cont = 0;
				for(DtoInformacionRecienNacido infoNacido:infoParto.getRecienNacidos())
				{
					this.infoRecienNacidos.put("fechaNacimiento_"+cont, infoNacido.getFechaNacimiento());
					this.infoRecienNacidos.put("horaNacimiento_"+cont, infoNacido.getHoraNacimiento());
					this.infoRecienNacidos.put("sexo_"+cont, infoNacido.getCodigoSexo());
					this.infoRecienNacidos.put("nombreSexo_"+cont, infoNacido.getNombreSexo());
					this.infoRecienNacidos.put("peso_"+cont, infoNacido.getPesoEgreso());
					if(!infoNacido.getAcronimoDiagnosticoRecienNacido().equals(""))
						this.infoRecienNacidos.put("diagnosticoRN_"+cont, infoNacido.getAcronimoDiagnosticoRecienNacido()+ConstantesBD.separadorSplit+infoNacido.getCieDiagnosticoRecienNacido()+ConstantesBD.separadorSplit+infoNacido.getDiagnosticoRecienNacido().getNombre());
					else
						this.infoRecienNacidos.put("diagnosticoRN_"+cont, "");
					if(!infoNacido.getAcronimoDiagnosticoMuerte().equals(""))
						this.infoRecienNacidos.put("diagnosticoMuerte_"+cont, infoNacido.getAcronimoDiagnosticoMuerte()+ConstantesBD.separadorSplit+infoNacido.getCieDiagnosticoMuerte()+ConstantesBD.separadorSplit+infoNacido.getDiagnosticoMuerte().getNombre());
					else
						this.infoRecienNacidos.put("diagnosticoMuerte_"+cont, "");
					
					this.infoRecienNacidos.put("fechaMuerte_"+cont, infoNacido.getFechaMuerte());
					this.infoRecienNacidos.put("horaMuerte_"+cont, infoNacido.getHoraMuerte());
					this.infoRecienNacidos.put("consecutivo_"+cont, infoNacido.getConsecutivo());
					cont++;
				}
			}
			else
				this.infoRecienNacidos.put("existeBD", ConstantesBD.acronimoNo);
		}
		else
			this.infoRecienNacidos.put("abrirSeccion", ConstantesBD.acronimoNo);
	}
	//*****************************************************************************************
	/**
	 * @return the datos
	 */
	public HashMap<String, Object> getDatos() {
		return datos;
	}
	/**
	 * @param datos the datos to set
	 */
	public void setDatos(HashMap<String, Object> datos) {
		this.datos = datos;
	}
	/**
	 * @return the numeroSolicitud
	 */
	public String getNumeroSolicitud() {
		return numeroSolicitud;
	}
	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(String numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}
	/**
	 * @return the tipoServicio
	 */
	public String getTipoServicio() {
		return tipoServicio;
	}
	/**
	 * @param tipoServicio the tipoServicio to set
	 */
	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}
	/**
	 * @return the tipoSolicitud
	 */
	public int getTipoSolicitud() {
		return tipoSolicitud;
	}
	/**
	 * @param tipoSolicitud the tipoSolicitud to set
	 */
	public void setTipoSolicitud(int tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}
	/**
	 * @return the infoRecienNacidos
	 */
	public HashMap<String, Object> getInfoRecienNacidos() {
		return infoRecienNacidos;
	}
	/**
	 * @param infoRecienNacidos the infoRecienNacidos to set
	 */
	public void setInfoRecienNacidos(HashMap<String, Object> infoRecienNacidos) {
		this.infoRecienNacidos = infoRecienNacidos;
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
