/*
 * Marzo 25, 2008
 */
package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.facturacion.InfoResponsableCobertura;
import util.historiaClinica.UtilidadesHistoriaClinica;
import util.salas.UtilidadesSalas;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.CargosDirectosCxDytDao;
import com.princetonsa.dto.historiaClinica.DtoInformacionParto;
import com.princetonsa.dto.historiaClinica.DtoInformacionRecienNacido;
import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.princetonsa.dto.salasCirugia.DtoProfesionalesCirugia;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.ordenesmedicas.cirugias.HojaQuirurgica;
import com.princetonsa.mundo.ordenesmedicas.cirugias.SolicitudesCx;
import com.princetonsa.mundo.parametrizacion.TecnicaAnestesia;
import com.princetonsa.mundo.salasCirugia.HojaAnestesia;
import com.princetonsa.mundo.salasCirugia.LiquidacionServicios;
import com.princetonsa.mundo.salasCirugia.Peticion;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * @author Sebastiï¿½n Gï¿½mez R
 *
 *Clase que representa el Mundo con sus atributos y mï¿½todos de la funcionalidad
 * Cargos Directos x cirugias y DYT
 */
public class CargosDirectosCxDyt 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(CargosDirectosCxDyt.class);
	
	/**
	 * DAO para el manejo de CargosDirectosCxDytDao
	 */
	private CargosDirectosCxDytDao cargosDirectosDao=null;
	
	private HashMap<String, Object> encabezadoSolicitud = new HashMap<String, Object>();
	private HashMap<String, Object> cirugiasSolicitud = new HashMap<String, Object>();
	private HashMap<String, Object> datosActoQx = new HashMap<String, Object>();
	private HashMap<String, Object> otrosProfesionales = new HashMap<String, Object>(); 
	private HashMap<String, Object> infoRecienNacidos = new HashMap<String, Object>();
	
	private ActionErrors errores = new ActionErrors();
	private int codigoPeticion;
	private int numeroSolicitud;
	private ArrayList<Integer[]> cirujanosInsertados = new ArrayList<Integer[]>();
	private String diagnosticoPrincipal;
	
	/**
	 * Objeto que almacena los datos del ï¿½ltimo diagnï¿½stico 
	 * relacionado a la solicitud
	 */
	private DtoDiagnostico dtoDiagnostico;
	private boolean solPYP;
	
	/**Atributo que se encarga de obtener los datos de la cobertura de la cirugia*/
	private List<InfoResponsableCobertura> listaCoberturaCargoCirugia;
	
	//*****************************************************************
	//**********INICIALIZADORES & CONSTRUCTORES***********************
	/**
	 * Constructor
	 */
	public CargosDirectosCxDyt() 
	{
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * mï¿½todo para inicializar los datos
	 *
	 */
	public void clean()
	{
		this.encabezadoSolicitud = new HashMap<String, Object>();
		this.cirugiasSolicitud = new HashMap<String, Object>();
		this.datosActoQx = new HashMap<String, Object>();
		this.otrosProfesionales = new HashMap<String, Object>();
		this.infoRecienNacidos = new HashMap<String, Object>();
		
		this.errores = new ActionErrors();
		this.codigoPeticion = ConstantesBD.codigoNuncaValido;
		this.numeroSolicitud = ConstantesBD.codigoNuncaValido;
		this.cirujanosInsertados = new ArrayList<Integer[]>();
		this.dtoDiagnostico= new DtoDiagnostico();
		this.solPYP=false;
	}
	
	//*****************************************************************************************
	
	/**
	 * Mï¿½todo para generar el cargo directo de cirugï¿½a o No Cruento
	 */
	public boolean generarCargoDirecto(Connection con, UsuarioBasico usuario, PersonaBasica paciente, String idCuenta) throws IPSException
	{
		
		boolean exito = true;
		int resp1 = ConstantesBD.codigoNuncaValido, resp2 = ConstantesBD.codigoNuncaValido;
		 String cubierto=ConstantesBD.acronimoNo;	        
         Integer codigoContrato = null;
         InfoResponsableCobertura infoResponsableCobertura = null;
		/*
		 * Validaciï¿½n de la cobertura
		 */
		int codigoServicio = ConstantesBD.codigoNuncaValido;
		
		listaCoberturaCargoCirugia = new ArrayList<InfoResponsableCobertura>();
		
		//hermorhu - MT6679 
		//Se debe validar cobertura y contrato/convenio para cada uno de los servicios
        for(int i=0;i<this.getNumCirugias();i++) { 
        	if(!UtilidadTexto.getBoolean(this.cirugiasSolicitud.get("fueEliminado_"+i)+"")) {
        		codigoServicio = Integer.parseInt(this.cirugiasSolicitud.get("codigoServicio_"+i).toString());
        		
        		infoResponsableCobertura = new InfoResponsableCobertura();;
        		infoResponsableCobertura = Cobertura.validacionCoberturaServicio(con, paciente.getCodigoIngreso()+"", paciente.getCodigoUltimaViaIngreso(), paciente.getCodigoTipoPaciente(), codigoServicio, usuario.getCodigoInstitucionInt(), false, "" /*subCuentaCoberturaOPCIONAL*/);
		
        		/*Verifico si el servicio se encuentra cubierto por el convenio responsable */
                if(infoResponsableCobertura.getInfoCobertura().incluido() && infoResponsableCobertura.getInfoCobertura().existe()) {
                	cubierto=ConstantesBD.acronimoSi;
                }else {
                	cubierto = ConstantesBD.acronimoNo;
                }
                
                codigoContrato = infoResponsableCobertura.getDtoSubCuenta().getContrato();
        
                cirugiasSolicitud.put("cubierto_"+i, cubierto);
                cirugiasSolicitud.put("contratoConvenio_"+i, codigoContrato);
                
        		//se obtiene datos de cobertura de cada servicio para evaluacion posterior en la Autorizacion de Capitacion
        		DtoSolicitudesSubCuenta dtoSolicitudesSubCuenta 	= new DtoSolicitudesSubCuenta();
        		dtoSolicitudesSubCuenta.getServicio().setId(String.valueOf(codigoServicio));
        		infoResponsableCobertura.getDtoSubCuenta().getSolicitudesSubcuenta().add(dtoSolicitudesSubCuenta);
        		listaCoberturaCargoCirugia.add(infoResponsableCobertura);	
        	}
        }	
		
		//*************SE INSERTA PRIMERO LA PETICION QX ********************************************
		this.insertarPeticion(con, usuario, paciente);
		//*******************************************************************************************
        
        if(codigoPeticion>0)
        {
        	//*******************SE INSERTA UNA SOLICITUD Bï¿½SICA*******************************************
        	this.insertarSolicitudGeneral(con,usuario,paciente,idCuenta);
	        //***********************************************************************************************
			
	        if(numeroSolicitud>0)
	        {
	        	//hermorhu - MT6679 
	        	//En este punto ya existe una solicitud asi que se adiciona al primer elemento de la listaCoberturaCargoCirugia para aceederla desde la autorizacion
	        	listaCoberturaCargoCirugia.get(0).getDtoSubCuenta().getSolicitudesSubcuenta().get(0).setNumeroSolicitud(String.valueOf(numeroSolicitud));
	        	
	        	//************INSERCIï¿½N DEL ENCABEZADO DE LA SOLICITUD CIRUGï¿½A************************************
	        	SolicitudesCx mundoSolCx= new SolicitudesCx();
	        	boolean resp0 = this.insertarSolicitudCirugia(con, usuario, paciente, mundoSolCx, listaCoberturaCargoCirugia, codigoServicio);
	        	//***********************************************************************************************
	        	
	        	logger.info("RESULTADO RESP0=> "+resp0);
				if(resp0)
				{
					//********************INSERCION DE LAS ESPECIALIDADES QUE INTERVIENEN***********************************
					this.insertarEspecialidadesIntervienen(con,usuario);
					//********************************************************************************************************
					
					//2) Se inserta el detalle de la cirugia (El servicio)
					int cont = 0;
					for(int i=0;i<this.getNumCirugias();i++)
			        	if(!UtilidadTexto.getBoolean(this.cirugiasSolicitud.get("fueEliminado_"+i)+""))
			        	{
			        		cont ++;
			        		resp1 = mundoSolCx.insertarSolicitudCxXServicioTransaccional(
									con, 
									numeroSolicitud+"", 
									this.cirugiasSolicitud.get("codigoServicio_"+i).toString(), 
									ConstantesBD.codigoNuncaValido, //codigo tipo cirugia
									cont,
									Integer.parseInt(this.cirugiasSolicitud.get("codigoEsquemaTarifario_"+i).toString()), // esquema tarifario
									Utilidades.convertirADouble(this.cirugiasSolicitud.get("grupoUvr_"+i).toString(),true), //grupo o uvr 
									usuario.getCodigoInstitucionInt(), 
									/*this.cirugiasSolicitud.get("autorizacion_"+i).toString(),*/ 
									Utilidades.convertirAEntero(this.cirugiasSolicitud.get("codigoFinalidadCx_"+i)+""), //finalidad 
									"", 
									this.cirugiasSolicitud.get("codigoViaCx_"+i).toString(), //via Cx 
									this.cirugiasSolicitud.get("indBilateral_"+i).toString(),  // indicativo bilateral
									this.cirugiasSolicitud.get("indViaAcceso_"+i).toString(),  //indicativo via de acceso
									Integer.parseInt(this.cirugiasSolicitud.get("codigoEspecialidadInterviene_"+i).toString()), // codigo especialidad
									this.cirugiasSolicitud.get("liquidarServicio_"+i).toString(), //liquidar servicio 
									ConstantesBD.continuarTransaccion,
									cirugiasSolicitud.get("cubierto_"+i).toString(),
									Integer.parseInt(cirugiasSolicitud.get("contratoConvenio_"+i).toString()));
						
						
							if(resp1<=0)
							{
								errores.add("",new ActionMessage("errors.noSeGraboInformacion","DEL DETALLE DE LA SOLICITUD Qx PARA EL SERVICIO "+this.cirugiasSolicitud.get("descripcionServicio_"+i)));
								i = this.getNumCirugias(); //se rompe el ciclo
							}
							else
							{
								//**********************INSERCION DE LOS DIAGNOSTICOS**************************************************************
								resp2 = this.insercionDiagnosticos(con,usuario,i,resp1);
								//*****************************************************************************************************************
								if(resp2>0)
									//************************INSERCIï¿½N DE LOS ASOCIOS*****************************************
									this.insercionAsocios(con,usuario,i,resp1);
									//******************************************************************************************
							} 
			        	} //Fin FOR servicios de la orden de cirugï¿½as
					
					logger.info("INSERTO RESP1=> "+resp1);
					if(resp1>0)
					{
						logger.info("------------------!VOY A INSERTAR LA HOJA QUIRURGICA!---------------------------------");
						//********************INSERCION DE LA HOJA QUIRURGICA*****************************************************
						this.insercionHojaQuirurgica(con,usuario);
						//*******************FIN INSERCIï¿½N HOJA QUIRURGICA******************************************************************
						
						//********************INSERCIï¿½N DE LA HOJA DE ANESTESIA************************************************************
						
						//********************INSERCIÃ“N DE LA HOJA DE ANESTESIA************************************************************
						boolean validacionCapitacion=false;
						if (!UtilidadTexto.isEmpty(this.datosActoQx.get("codigoAnestesiologo").toString()))
							this.insercionHojaAnestesia(con,usuario, validacionCapitacion);		
						
						//*********************FIN INSERCIï¿½N HOJA ANESTESIA*****************************************************************
						
						//****************INSERCIï¿½N DE LA INFORMACIï¿½N DE PARTOS*********************************************************
						this.insercionInfoRecienNacidos(con,paciente,usuario);
						//****************FIN INSERCIï¿½N DE LA INFORMACIï¿½N DE PARTOS*****************************************************
						
					} //FIN IF - DETALLE SOLICITUD CIRUGIAS
						
				} //Fin IF - SOLICITUD CIRUGIAS
				
				//*******************************************************************************************************
	        }//Fin IF - NUMERO SOLICITUD
			
        } //Fin IF - NUMERO PETICION	
        
        if(errores.isEmpty())
        	exito = true;
        else
        	exito = false; 
        
        return exito;
	}
	
	/**
	 * Mï¿½todo implementado para insertar la informaciï¿½n de los reciï¿½n nacidos
	 * @param con
	 * @param paciente
	 * @param usuario
	 */
	private void insercionInfoRecienNacidos(Connection con, PersonaBasica paciente, UsuarioBasico usuario) 
	{
		///Se verifica si se ingresaron hijos
		int nroHijos = Utilidades.convertirAEntero(this.infoRecienNacidos.get("nroHijos")+"", true);
		if(Utilidades.convertirAEntero(this.infoRecienNacidos.get("nroHijos")+"", true)>0)
		{
			DtoInformacionParto infoParto = new DtoInformacionParto();
			infoParto.setCodigoIngreso(paciente.getCodigoIngreso()+"");
			infoParto.setNumeroSolicitud(numeroSolicitud+"");
			infoParto.setCodigoPaciente(paciente.getCodigoPersona());
			infoParto.setParto(true);
			infoParto.setLoginUsuario(usuario.getLoginUsuario());
			infoParto.setInstitucion(usuario.getCodigoInstitucionInt());
			infoParto.setSemanasGestacional(Utilidades.convertirAEntero(this.infoRecienNacidos.get("edadGestacional")+"", true));
			infoParto.setControlPrenatal(UtilidadTexto.getBoolean(this.infoRecienNacidos.get("controlPrenatal")+""));
			infoParto.setFinalizado(true);
			
			//DTO para ingresar la informaciï¿½n de reciï¿½n nacido
			ArrayList<DtoInformacionRecienNacido> arregloRecienNacido = new ArrayList<DtoInformacionRecienNacido>();
			String[] diagnostico = null;
			
			for(int i=0;i<nroHijos;i++)
			{
				
				//Se llena la informacion de cada reciï¿½n nacido
				DtoInformacionRecienNacido infoRecienNacido = new DtoInformacionRecienNacido();
				
				infoRecienNacido.setCodigoIngreso(paciente.getCodigoIngreso()+"");
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
					errores.add("",new ActionMessage("errors.noSeGraboInformacion","DE LA INFORMACIï¿½N DE PARTOS"));
				
			
		}
		
	}
	/**
	 * Mï¿½todo para insertar una solicitud de cirugï¿½a
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param mundoSolCx 
	 */
	private boolean insertarSolicitudCirugia(Connection con, UsuarioBasico usuario, PersonaBasica paciente, SolicitudesCx mundoSolCx, List<InfoResponsableCobertura> listInfoResponsableCobertura, int codigoServicioPrincipal) 
	{
        
        double subCuenta=ConstantesBD.codigoNuncaValido;
		subCuenta = listInfoResponsableCobertura.get(0).getDtoSubCuenta().getSubCuentaDouble();
		
		/**
    	 * FIXME De acuerdo a la validacion DCU 174 v 2.1 (verificar si el Convenio del Servicio es el mismo Convenio/Contrato para todos los servicios.)
    	 * 		 1. Actualmente: Se toma la convenio del servicio principal (el primer servicio).
    	 * 		 2. Se deja pendiente para Analisis como se deben evaluar los demas
    	 * 		 Camilo Gomez 
    	 */
		//se obtiene datos de cobertura del servicio principal para evaluacion posterior en la Autorizacion de Capitacion
//		DtoSolicitudesSubCuenta dtoSolicitudesSubCuenta 	= new DtoSolicitudesSubCuenta();
//		dtoSolicitudesSubCuenta.setNumeroSolicitud(numeroSolicitud+"");
//		dtoSolicitudesSubCuenta.getServicio().setId(codigoServicioPrincipal+"");
//		infoResponsableCobertura.getDtoSubCuenta().getSolicitudesSubcuenta().add(dtoSolicitudesSubCuenta);
//		listaCoberturaCargoCirugia = new ArrayList<InfoResponsableCobertura>();
//		listaCoberturaCargoCirugia.add(infoResponsableCobertura);	
        //************************************************************************************************
		
		//*************SE INSERTA LA SOLICITUD DE CIRUGIA ************************************************
		boolean resp0 = false;
		
		String indQx = ConstantesIntegridadDominio.acronimoIndicativoCargoNoCruentoCargoDirecto, tipoServicio = "";
		//Se verifica el indicativo de cargo
		//Con el hecho de que haya un servicio Partos y Cesï¿½reas o Quirï¿½rgico es Cargo Directo Cirugia, de lo contrario
		//es Cargo Directo No Cruentos
		for(int i=0;i<this.getNumCirugias();i++)
		{
			tipoServicio = this.cirugiasSolicitud.get("tipoServicio_"+i).toString();
			
        	if(!UtilidadTexto.getBoolean(this.cirugiasSolicitud.get("fueEliminado_"+i)+"")&&
        		(tipoServicio.equals(ConstantesBD.codigoServicioQuirurgico+"")||tipoServicio.equals(ConstantesBD.codigoServicioPartosCesarea+"")))
        	{
        		indQx = ConstantesIntegridadDominio.acronimoIndicativoCargoCirugiaCargoDirecto;
        		i = this.getNumCirugias();
        	}
		}
		//1) Se inserta encabezado de la cirugia
		try 
		{
			mundoSolCx.setFechaInicialCx(this.datosActoQx.get("fechaInicialCx").toString());
			mundoSolCx.setHoraInicialCx(this.datosActoQx.get("horaInicialCx").toString());
			mundoSolCx.setFechaFinalCx(this.datosActoQx.get("fechaFinalCx").toString());
			mundoSolCx.setHoraFinalCx(this.datosActoQx.get("horaFinalCx").toString());
			mundoSolCx.setFechaIngresoSala(this.datosActoQx.get("fechaIngresoSala").toString());
			mundoSolCx.setHoraIngresoSala(this.datosActoQx.get("horaIngresoSala").toString());
			mundoSolCx.setFechaSalidaSala(this.datosActoQx.get("fechaSalidaSala").toString());
			mundoSolCx.setHoraSalidaSala(this.datosActoQx.get("horaSalidaSala").toString());
			mundoSolCx.setLiquidarAnestesia(this.datosActoQx.get("cobrarAnestesia").toString());
			mundoSolCx.setDuracionCx(this.datosActoQx.get("duracionCx").toString());
			resp0 = mundoSolCx.insertarSolicitudCxGeneralTransaccional1(
																		con, 
																		numeroSolicitud+"", 
																		codigoPeticion+"", 
																		false, 
																		ConstantesBD.continuarTransaccion, 
																		subCuenta,
																		indQx);				
		
		} catch (SQLException e) 
		{
			resp0 = false;
			logger.error("Error al insertar solicitud general de cirugï¿½a: "+e);
		}
		if(!resp0)
			 errores.add("",new ActionMessage("errors.noSeGraboInformacion","DE LA SOLICITUD DE CIRUGï¿½A"));
		return resp0;
	}
	/**
	 * Mï¿½todo que inserta las especialidades que intervienen
	 * @param con
	 * @param usuario
	 */
	private void insertarEspecialidadesIntervienen(Connection con, UsuarioBasico usuario) 
	{
		ArrayList<Integer> especialidades = new ArrayList<Integer>();
		boolean repetida = false;
		
		for(int i=0;i<this.getNumCirugias();i++)
			if(!UtilidadTexto.getBoolean(this.cirugiasSolicitud.get("fueEliminado_"+i)+""))
			{
				repetida = false;
				for(Integer esp:especialidades)
					if(esp.intValue()==Integer.parseInt(this.cirugiasSolicitud.get("codigoEspecialidadInterviene_"+i)+""))
						repetida = true;
				
				if(!repetida)
					especialidades.add(Integer.parseInt(this.cirugiasSolicitud.get("codigoEspecialidadInterviene_"+i)+""));
			}
		
		HojaAnestesia mundoAnestesia = new HojaAnestesia();
		for(Integer esp:especialidades)
		{
			if(!mundoAnestesia.insertarEspecialidadesIntervienen(con, numeroSolicitud+"", esp.intValue()+"", ConstantesBD.acronimoSi, usuario.getLoginUsuario()))
				errores.add("",new ActionMessage("errors.noSeGraboInformacion","DE LA ESPECIALIDAD QUE INTERVIENE "+Utilidades.getNombreEspecialidad(con, esp.intValue())));
		}
		
	}
	/**
	 * Mï¿½todo implementado para insertar los asocios de un servicio
	 * @param con
	 * @param usuario
	 * @param i
	 * @param codSolServ
	 * @return
	 */
	private int insercionAsocios(Connection con, UsuarioBasico usuario, int i, int codSolServ) 
	{
		int resp3 = 1;
		HojaAnestesia mundoAnestesia = new HojaAnestesia();
		int codigoAsocioCirujano = Integer.parseInt(ValoresPorDefecto.getAsocioCirujano(usuario.getCodigoInstitucionInt()));
		boolean existeCirujano = false;
		//************************INSERCIï¿½N DE LOS ASOCIOS*****************************************
		for(int j=0;j<Utilidades.convertirAEntero(this.cirugiasSolicitud.get("numProfesionales_"+i)+"", true);j++)
			if(!UtilidadTexto.getBoolean(this.cirugiasSolicitud.get("fueEliminado_"+i+"_"+j)+""))
			{
				existeCirujano = false;
				
				DtoProfesionalesCirugia dtoProfesional = new DtoProfesionalesCirugia();
				dtoProfesional.setCodSolCxServ(codSolServ+"");
				dtoProfesional.setCodigoAsocio(Integer.parseInt(this.cirugiasSolicitud.get("codigoAsocio_"+i+"_"+j).toString()));
				dtoProfesional.setCodigoEspecialidad(Integer.parseInt(this.cirugiasSolicitud.get("codigoEspecialidad_"+i+"_"+j).toString()));
				dtoProfesional.setCodigoProfesional(Integer.parseInt(this.cirugiasSolicitud.get("codigoProfesional_"+i+"_"+j).toString()));
				dtoProfesional.setCobrable(this.cirugiasSolicitud.get("cobrable_"+i+"_"+j).toString());
				dtoProfesional.setCodigoPool(Utilidades.convertirAEntero(this.cirugiasSolicitud.get("codigoPool_"+i+"_"+j)+"", true));
				
				resp3 = HojaQuirurgica.insertarProfesionalCirugia(con, dtoProfesional, usuario.getLoginUsuario());
				
				if(resp3<=0)
				{
					j = Utilidades.convertirAEntero(this.cirugiasSolicitud.get("numProfesionales_"+i)+"", true);
					errores.add("",new ActionMessage("errors.noSeGraboInformacion","DE LOS ASOCIOS PARA EL SERVICIO "+this.cirugiasSolicitud.get("descripcionServicio_"+i)));
				}
				
				//Se inserta el cirujano----------------------------------------------------------------------------------------------
				if(codigoAsocioCirujano==dtoProfesional.getCodigoAsocio())
				{
					//Primero se verifica que no se haya insertado
					for(Integer[] elemento:this.cirujanosInsertados)
						if(elemento[0].intValue()==dtoProfesional.getCodigoEspecialidad()&&elemento[1].intValue()==dtoProfesional.getCodigoProfesional())
							existeCirujano = true;
					
					if(!existeCirujano&&!mundoAnestesia.insertarCirujanosIntervienen(con, numeroSolicitud+"", dtoProfesional.getCodigoEspecialidad()+"", dtoProfesional.getCodigoProfesional()+"", ConstantesBD.acronimoSi, usuario.getLoginUsuario()))
					{
						j = Utilidades.convertirAEntero(this.cirugiasSolicitud.get("numProfesionales_"+i)+"", true);
						errores.add("",new ActionMessage("errors.noSeGraboInformacion","DEL ASOCIO CIRUJANO PARA EL SERVICIO "+this.cirugiasSolicitud.get("descripcionServicio_"+i)));
					}
					else
					{
						//Se aï¿½ade el cirujano como insertado
						Integer[] elemento = {new Integer(dtoProfesional.getCodigoEspecialidad()),new Integer(dtoProfesional.getCodigoProfesional())};
						this.cirujanosInsertados.add(elemento);
					}
				}
				//---------------------------------------------------------------------------------------------------------------------
			}
		
		return resp3;
	}
	/**
	 * Mï¿½todo implementado para la inserciï¿½n de los diagnï¿½sticos
	 * @param con
	 * @param usuario
	 * @param i
	 * @param codSolServ 
	 */
	private int insercionDiagnosticos(Connection con, UsuarioBasico usuario, int i, int codSolServ) 
	{
		int resp2 = 1;
		String[] diagnostico = null;
		//Inserciï¿½n de los diagnï¿½sticos principales
		if(!UtilidadTexto.isEmpty(this.cirugiasSolicitud.get("diagPrincipal_"+i)+""))
		{
			diagnostico = this.cirugiasSolicitud.get("diagPrincipal_"+i).toString().split(ConstantesBD.separadorSplit);
			resp2 = HojaQuirurgica.insertarDiagnosticoPostOperatorio(
					con, 
					codSolServ+"", 
					diagnostico[0], 
					Integer.parseInt(diagnostico[1]), 
					true,  //principal
					false, // complicacion
					usuario.getLoginUsuario());
			
			if(resp2<=0)
				errores.add("",new ActionMessage("errors.noSeGraboInformacion","DEL DX. PRINCIPAL PARA EL SERVICIO "+this.cirugiasSolicitud.get("descripcionServicio_"+i)));
		}
		
		if(resp2>0)
			//Inserciï¿½n de los diagnï¿½sticos relacionados
			for(int j=0;j<Utilidades.convertirAEntero(this.cirugiasSolicitud.get("numDiagnosticos_"+i)+"", true);j++)
			{
				if(UtilidadTexto.getBoolean(this.cirugiasSolicitud.get("checkDiagRel_"+i+"_"+j)+""))
				{
					diagnostico = this.cirugiasSolicitud.get("diagRelacionado_"+i+"_"+j).toString().split(ConstantesBD.separadorSplit);
					resp2 = HojaQuirurgica.insertarDiagnosticoPostOperatorio(
							con, 
							codSolServ+"", 
							diagnostico[0], 
							Integer.parseInt(diagnostico[1]), 
							false,  //principal
							false, // complicacion
							usuario.getLoginUsuario());
					
					if(resp2<=0)
					{
						j = Utilidades.convertirAEntero(this.cirugiasSolicitud.get("numDiagnosticos_"+i)+"", true);
						errores.add("",new ActionMessage("errors.noSeGraboInformacion","DEL DX. RELACIONADO Nï¿½"+(j+1)+" PARA EL SERVICIO "+this.cirugiasSolicitud.get("descripcionServicio_"+i)));
					}
				}
			}
		
		if(resp2>0)
			//Inserciï¿½n del diagnï¿½stico de complicaciï¿½n
			if(!UtilidadTexto.isEmpty(this.cirugiasSolicitud.get("diagComplicacion_"+i)+""))
			{
				diagnostico = this.cirugiasSolicitud.get("diagComplicacion_"+i).toString().split(ConstantesBD.separadorSplit);
				resp2 = HojaQuirurgica.insertarDiagnosticoPostOperatorio(
						con, 
						codSolServ+"", 
						diagnostico[0], 
						Integer.parseInt(diagnostico[1]), 
						false,  //principal
						true, // complicacion
						usuario.getLoginUsuario());
				
				if(resp2<=0)
					errores.add("",new ActionMessage("errors.noSeGraboInformacion","DEL DX. COMPLICACION PARA EL SERVICIO "+this.cirugiasSolicitud.get("descripcionServicio_"+i)));
			}
		
		return resp2;
		
	}
	/**
	 * Mï¿½todo para insertar una solicitud general
	 * @param con
	 * @param usuario
	 * @param paciente
	 * @param idCuenta 
	 */
	private void insertarSolicitudGeneral(Connection con, UsuarioBasico usuario, PersonaBasica paciente, String idCuenta) 
	{
		Solicitud objectSolicitud= new Solicitud();
        
        objectSolicitud.clean();
        objectSolicitud.setFechaSolicitud(this.encabezadoSolicitud.get("fechaSolicitud").toString());
        objectSolicitud.setHoraSolicitud(this.encabezadoSolicitud.get("horaSolicitud").toString());
        objectSolicitud.setTipoSolicitud(new InfoDatosInt(ConstantesBD.codigoTipoSolicitudCirugia));
        //objectSolicitud.setNumeroAutorizacion(this.encabezadoSolicitud.get("numeroAutorizacion").toString());
        objectSolicitud.setEspecialidadSolicitante(new InfoDatosInt(ConstantesBD.codigoEspecialidadMedicaNinguna));
        objectSolicitud.setOcupacionSolicitado(new InfoDatosInt(ConstantesBD.codigoNuncaValido));
        objectSolicitud.setCentroCostoSolicitante(new InfoDatosInt(paciente.getCodigoArea()));
        objectSolicitud.setCodigoMedicoSolicitante(ConstantesBD.codigoNuncaValido);
        objectSolicitud.setCentroCostoSolicitado(new InfoDatosInt(Integer.parseInt(this.encabezadoSolicitud.get("centroCostoSolicitado").toString())));
        objectSolicitud.setCodigoCuenta(Integer.parseInt(idCuenta));
        objectSolicitud.setCobrable(true);
        objectSolicitud.setVaAEpicrisis(false);
        objectSolicitud.setUrgente(false);
        objectSolicitud.setEstadoHistoriaClinica(new InfoDatosInt(ConstantesBD.codigoEstadoHCCargoDirecto));
        objectSolicitud.setSolPYP(false);
        objectSolicitud.setTieneCita(false);
        objectSolicitud.setLiquidarAsocio(ConstantesBD.acronimoNo);
        
        //Se agrega diagnóstico a la solicitud por Anexo Cargos Directos de Cirugias-DYT 551 V 1.2 
        //MT 5429 hermorhu
		// Validacion en caso de que el diagnostico sea el primero ingresado
        
        DtoDiagnostico dtoDiagnostico = Utilidades.getDiagnosticoPacienteCuenta(paciente.getCodigoCuenta());
	  
        if(dtoDiagnostico != null && !dtoDiagnostico.getAcronimoDiagnostico().isEmpty() && !dtoDiagnostico.getTipoCieDiagnostico().isEmpty()) {
        	objectSolicitud.setDtoDiagnostico(dtoDiagnostico);
        	this.dtoDiagnostico=dtoDiagnostico;
        } else {
        	String[] diagnostico =this.diagnosticoPrincipal.split("@@@@@");
        	DtoDiagnostico diagnosticoPrimerVez = new DtoDiagnostico();
			diagnosticoPrimerVez.setAcronimoDiagnostico(diagnostico[0]);
			diagnosticoPrimerVez.setTipoCieDiagnostico(diagnostico[1]);
			objectSolicitud.setDtoDiagnostico(diagnosticoPrimerVez);
			this.dtoDiagnostico=diagnosticoPrimerVez;
        }
	  	
	  	boolean solPyp = objectSolicitud.isSolPYP();
	  	this.solPYP=solPyp;
	  	
        
        try
        {
            numeroSolicitud=objectSolicitud.insertarSolicitudGeneralTransaccional(con, ConstantesBD.continuarTransaccion);
            if(numeroSolicitud>0&&Solicitud.interpretarSolicitud(con, "", ConstantesBD.codigoNuncaValido, UtilidadFecha.conversionFormatoFechaABD(this.encabezadoSolicitud.get("fechaSolicitud").toString()),this.encabezadoSolicitud.get("horaSolicitud").toString(), numeroSolicitud+"")<=0)
            	errores.add("",new ActionMessage("errors.noSeGraboInformacion","AL ACTUALIZAR LA FECHA DE INTERPETACION DE LA SOLICITUD"));
            else if(numeroSolicitud>0&&!objectSolicitud.cambiarEstadosSolicitud(con, numeroSolicitud, ConstantesBD.codigoNuncaValido, ConstantesBD.codigoEstadoHCCargoDirecto).isTrue())
            	errores.add("",new ActionMessage("errors.noSeGraboInformacion","AL ACTUALIZAR EL ESTADO Mï¿½DICO DE LA SOLICITUD"));
            	
        }
        catch(SQLException sqle)
        {
            logger.warn("Error al generar la solicitud basica de cirugï¿½as: "+sqle);
            errores.add("",new ActionMessage("errors.noSeGraboInformacion","DE LA SOLICITUD "));
            
        }
		
	}
	/**
	 * Mï¿½todo para insertar la peticion
	 * @param con
	 * @param usuario
	 * @param paciente
	 */
	private void insertarPeticion(Connection con, UsuarioBasico usuario, PersonaBasica paciente) 
	{
		///Datos del encabezado de la peticion
        HashMap peticionEncabezadoMap= new HashMap();
        peticionEncabezadoMap.put("tipoPaciente",   paciente.getCodigoTipoPaciente());
        peticionEncabezadoMap.put("fechaPeticion", UtilidadFecha.getFechaActual(con));
        peticionEncabezadoMap.put("horaPeticion", UtilidadFecha.getHoraActual(con));
        peticionEncabezadoMap.put("duracion", "");
        peticionEncabezadoMap.put("solicitante", usuario.getCodigoPersona()+"");
        peticionEncabezadoMap.put("fechaEstimada", "");
        peticionEncabezadoMap.put("requiereUci", ConstantesBD.acronimoNo);
        peticionEncabezadoMap.put("programable", ConstantesBD.acronimoNo);
        
        //Datos del servicio
        HashMap serviciosPeticionMap=new HashMap();
        serviciosPeticionMap.put("numeroFilasMapaServicios", this.getNumCirugiasReales());
        int cont = 0;
        
        for(int i=0;i<this.getNumCirugias();i++)
        {
        	if(!UtilidadTexto.getBoolean(this.cirugiasSolicitud.get("fueEliminado_"+i)+""))
        	{
        		serviciosPeticionMap.put("fueEliminadoServicio_"+cont, "false");
                serviciosPeticionMap.put("codigoServicio_"+cont, this.cirugiasSolicitud.get("codigoServicio_"+i));
                serviciosPeticionMap.put("codigoEspecialidad_"+cont, ""); //va vaï¿½o
                serviciosPeticionMap.put("codigoTipoCirugia_"+cont, "-1"); //no se ingresa informacion
                serviciosPeticionMap.put("numeroServicio_"+cont, (cont+1)+"");
                serviciosPeticionMap.put("cubierto_"+cont, cirugiasSolicitud.get("cubierto_"+i));
                serviciosPeticionMap.put("contrato_convenio_"+cont, cirugiasSolicitud.get("contratoConvenio_"+i));
                cont++;
                
        	}
        }
        
        //Datos de los artï¿½culos
        HashMap articulosPeticionMap= new HashMap();
        articulosPeticionMap.put("numeroMateriales", "0");
        articulosPeticionMap.put("numeroOtrosMateriales", "0");
        
        //Datos de los porfesionales
        HashMap profesionalesPeticionMap= new HashMap();
        profesionalesPeticionMap.put("numeroProfesionales", "0");
        
        Peticion mundoPeticion= new Peticion();

        int codigoPeticionYNumeroInserciones[]= mundoPeticion.insertar(
        	con, 
        	peticionEncabezadoMap, 
        	serviciosPeticionMap, 
        	profesionalesPeticionMap, 
        	articulosPeticionMap, 
        	paciente.getCodigoPersona(),
        	ConstantesBD.codigoNuncaValido,
        	usuario, true, false);
        
        //Se verifica nï¿½mero de inserciones
        if(codigoPeticionYNumeroInserciones[0]<1)
        	errores.add("",new ActionMessage("errors.noSeGraboInformacion","DE LA PETICION"));
        else
        {
        	codigoPeticion = codigoPeticionYNumeroInserciones[1];
        	if(!mundoPeticion.actualizarEstadoPeticion(con, ConstantesBD.codigoEstadoPeticionAtendida, codigoPeticion+""))
        		errores.add("",new ActionMessage("errors.noSeGraboInformacion","DE LA PETICION"));
        }
		
	}
	/**
	 * Mï¿½todo para insertar datos de la hoja de anestesia
	 * @param con
	 * @param usuario
	 */
	private void insercionHojaAnestesia(Connection con, UsuarioBasico usuario, boolean validacionCapitacion) 
	{
		HojaAnestesia mundoAnestesia = new HojaAnestesia();
		//Si el tipo de anestesia no se mostraba en la hoja quirï¿½rgica, entonces se inserta la hoja de anestesia
		if(!UtilidadTexto.getBoolean(this.datosActoQx.get("mostrarTipoAnesEnHojaQx").toString()))
		{
			if(mundoAnestesia.insertarHojaAnestesia(
				con, 
				numeroSolicitud+"", 
				usuario.getCodigoInstitucion(), 
				"", //fecha incial anestesia 
				"", //hora inicial anestesia
				"", //fecha final anestesia 
				"", //hora final anestesia 
				"", //preanestesia 
				"", //Datos del mï¿½dico 
				true+"", //finalizada
				UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con)), //fecha finalizaciï¿½n 
				UtilidadFecha.getHoraActual(con), // hora finalizacion 
				UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual(con)), //fecha grabaciï¿½n 
				UtilidadFecha.getHoraActual(con), // hora grabaciï¿½n
				"", //observaciones signos vitales
				this.datosActoQx.get("cobrarAnestesia").toString(),
				validacionCapitacion))
			{
				
				//Se inserta el anestesiï¿½logo
				if(mundoAnestesia.insertarAnestesiologos(con, numeroSolicitud+"", this.datosActoQx.get("codigoAnestesiologo").toString(), ConstantesBD.acronimoSi))
				{
					//Se consulta el consecutivo del tipo de anestesia de la parametrizaciï¿½n
					int codigoTipoAnestesiaInstCC = ConstantesBD.codigoNuncaValido;
					ArrayList<HashMap<String, Object>> tiposAnestesia = UtilidadesSalas.obtenerTiposAnestesiaInstitucionCentroCosto(con, usuario.getCodigoInstitucionInt(), Integer.parseInt(this.encabezadoSolicitud.get("centroCostoSolicitado")+""));
					
					for(HashMap elemento:tiposAnestesia)
						if(Integer.parseInt(elemento.get("codigo").toString())==Integer.parseInt(this.datosActoQx.get("codigoTipoAnestesia")+""))
							codigoTipoAnestesiaInstCC = Integer.parseInt(elemento.get("consecutivo").toString()); 
					
					//Se inserta el tipo de anestesia
					if(!TecnicaAnestesia.insertarTecnicaAnestesia(con, numeroSolicitud, Integer.parseInt(this.datosActoQx.get("codigoTipoAnestesia")+""), codigoTipoAnestesiaInstCC, usuario.getLoginUsuario()))
						errores.add("",new ActionMessage("errors.noSeGraboInformacion","DEL TIPO DE ANESTESIA"));
				}
				else
					errores.add("",new ActionMessage("errors.noSeGraboInformacion","DEL ANESTESIï¿½LOGO"));
			}
			else
				errores.add("",new ActionMessage("errors.noSeGraboInformacion","DE LA HOJA DE ANESTESIA"));
		}
		
	}
	/**
	 * Mï¿½todo para insertar datos de la hoja quirï¿½rgica y los otros profesionales
	 * @param con
	 * @param usuario
	 */
	private void insercionHojaQuirurgica(Connection con, UsuarioBasico usuario) 
	{
		//Se toma el cirujano del primer servicio
		int codigoMedicoResponde = ConstantesBD.codigoNuncaValido;
		int codigoAsocioCirujano = Integer.parseInt(ValoresPorDefecto.getAsocioCirujano(usuario.getCodigoInstitucionInt()));
		int posPrimeraCx = 0;
		for(int i=0;i<getNumCirugias();i++)
			if(!UtilidadTexto.getBoolean(this.cirugiasSolicitud.get("fueEliminado_"+i)+""))
			{
				posPrimeraCx = i;
				i= getNumCirugias();
			}
		
		
		for(int i=0;i<Utilidades.convertirAEntero(this.cirugiasSolicitud.get("numProfesionales_"+posPrimeraCx)+"", true);i++)
			if(codigoAsocioCirujano==Utilidades.convertirAEntero(this.cirugiasSolicitud.get("codigoAsocio_"+posPrimeraCx+"_"+i)+"", true)&&
				!UtilidadTexto.getBoolean(this.cirugiasSolicitud.get("fueEliminado_"+posPrimeraCx+"_"+i)+""))
				codigoMedicoResponde = Integer.parseInt(this.cirugiasSolicitud.get("codigoProfesional_"+posPrimeraCx+"_"+i).toString());
		
		UsuarioBasico medico = new UsuarioBasico();
		try 
		{
			medico.cargarUsuarioBasico(con, codigoMedicoResponde);
			
			Solicitud mundoSolicitud = new Solicitud();
			if(mundoSolicitud.actualizarMedicoRespondeTransaccional(con,this. numeroSolicitud, medico, ConstantesBD.continuarTransaccion)<=0)
				errores.add("",new ActionMessage("errors.noSeGraboInformacion","DEL PROFESIONAL QUE RESPONDE"));
			
		} catch (SQLException e) 
		{
			logger.error("Error el actualizar medico responde en solicitud: "+e);
		}
		
		
		HashMap datosHQ = new HashMap();
		datosHQ.put("numSolicitud", this.numeroSolicitud);
		datosHQ.put("finalizada", true);
		datosHQ.put("datosMedico", medico.getInformacionGeneralPersonalSalud());
		datosHQ.put("medicoFinaliza", codigoMedicoResponde);
			
		/**MT 3911 -Diana Ruiz */
		datosHQ.put("anestesiologo", this.datosActoQx.get("codigoAnestesiologo"));
		
		if(HojaQuirurgica.insertarHojaQxBasica(con, datosHQ))
		{
			datosHQ.put("numSol", this.numeroSolicitud);
			datosHQ.put("medicoFin", codigoMedicoResponde);
			
			if(HojaQuirurgica.cambiarEstadoHqx(con, datosHQ))
			{
				datosHQ.put("numSol", this.numeroSolicitud);
				datosHQ.put("poli", UtilidadTexto.getBoolean(this.datosActoQx.get("politraumatismo")+""));
				datosHQ.put("tipoSala",this.datosActoQx.get("codigoTipoSala"));
				datosHQ.put("sala",this.datosActoQx.get("codigoSala"));
				datosHQ.put("tipoHerida", ""); //no aplica tipo herida
				datosHQ.put("partAnest", !UtilidadTexto.getBoolean(this.datosActoQx.get("mostrarTipoAnesEnHojaQx").toString())?true:false);
				datosHQ.put("tipoAnest", !UtilidadTexto.getBoolean(this.datosActoQx.get("mostrarTipoAnesEnHojaQx").toString())?"":this.datosActoQx.get("codigoTipoAnestesia").toString());
				if(HojaQuirurgica.actualizarInfoQx(con, datosHQ))
				{
					//**********INSERCION PROFESONALES ACTO QUIRURGICO**********************************************
					for(int i=0;i<Utilidades.convertirAEntero(this.otrosProfesionales.get("numRegistros")+"");i++)
						if(!UtilidadTexto.getBoolean(this.otrosProfesionales.get("fueEliminado_"+i)+""))
						{
							datosHQ = new HashMap();
							datosHQ.put("numSol", this.numeroSolicitud);
							datosHQ.put("tipoAsoc", this.otrosProfesionales.get("codigoAsocio_"+i));
							datosHQ.put("prof", this.otrosProfesionales.get("codigoProfesional_"+i));
							datosHQ.put("cobrable", this.otrosProfesionales.get("cobrable_"+i));
							datosHQ.put("usuario", usuario.getLoginUsuario());
							if(!HojaQuirurgica.insertarProfInfoQx(con, datosHQ))
							{
								i = Utilidades.convertirAEntero(this.otrosProfesionales.get("numRegistros")+"");
								errores.add("",new ActionMessage("errors.noSeGraboInformacion","DE LOS OTROS PROFESIONALES "));
							}
						}
					//*************************************************************************************************
				}
				else
					errores.add("",new ActionMessage("errors.noSeGraboInformacion","DE LA SALA Y TIPO SALA"));
			}
			else
				errores.add("",new ActionMessage("errors.noSeGraboInformacion","DE LA FINALIZACIï¿½N DE LA HOJA QUIRï¿½RGICA"));
		}
		else
			errores.add("",new ActionMessage("errors.noSeGraboInformacion","DEL ENCABEZADO DE LA HOJA QUIRï¿½RGICA"));
		
	}
	
	
	/**
	 * Mï¿½todo implementado para cargar el resumen del cargo directo
	 * @param con
	 */
	public void cargarResumenCargoDirecto(Connection con)
	{
		LiquidacionServicios mundoLiquidacion = new LiquidacionServicios();
		mundoLiquidacion.setNumeroSolicitud(this.numeroSolicitud+"");
		mundoLiquidacion.setCon(con);
		mundoLiquidacion.cargarDetalleOrden();
		
		//SE llenan los mapas obtenidos
		this.encabezadoSolicitud = mundoLiquidacion.getEncabezadoOrden();
		this.datosActoQx = mundoLiquidacion.getDatosActoQx();
		this.otrosProfesionales = mundoLiquidacion.getOtrosProfesionales();
		
		//Se consultan los servicios de la orden
		SolicitudesCx mundoSolCx = new SolicitudesCx();
		this.cirugiasSolicitud = mundoSolCx.cargarServiciosXSolicitudCx(con, this.numeroSolicitud+"",false);
		
		HashMap criterios = new HashMap();
		String[] indicesAsocios = HojaQuirurgica.indicesProfesionales;
		for(int i=0;i<Integer.parseInt(this.cirugiasSolicitud.get("numRegistros").toString());i++)
		{
			//**********SE CARGAN LOS ASOCIOS DEL SERVICIO****************************************
			criterios.put("codigoSolCXSer",this.cirugiasSolicitud.get("codigo_"+i));
			HashMap datosAsocios = HojaQuirurgica.consultarProfecionalesCx(con, criterios);
			
			this.cirugiasSolicitud.put("numProfesionales_"+i,datosAsocios.get("numRegistros"));
			for(int j=0;j<Integer.parseInt(datosAsocios.get("numRegistros")+"");j++)
			{
				this.cirugiasSolicitud.put("nombreAsocio_"+i+"_"+j, datosAsocios.get(indicesAsocios[9]+j));
				this.cirugiasSolicitud.put("nombreProfesional_"+i+"_"+j, datosAsocios.get(indicesAsocios[8]+j));
				this.cirugiasSolicitud.put("nombreEspecialidad_"+i+"_"+j, datosAsocios.get(indicesAsocios[15]+j));
				this.cirugiasSolicitud.put("cobrable_"+i+"_"+j, UtilidadTexto.getBoolean(datosAsocios.get(indicesAsocios[4]+j).toString())?"Sï¿½":"No");
				this.cirugiasSolicitud.put("nombrePool_"+i+"_"+j, datosAsocios.get(indicesAsocios[16]+j));
				
			}
			//**********************************************************************************
			//********SE CARGAN LO DIAGNOSTICOS DEL SERVICIO***********************************
			HashMap datosDiagnosticos = HojaQuirurgica.cargarDiagnosticosPorServicio(con, Integer.parseInt(this.cirugiasSolicitud.get("codigo_"+i).toString()), 0);
			
			//Se toma el diagnï¿½stico principal
			for(int j=0;j<Integer.parseInt(datosDiagnosticos.get("numRegistros").toString());j++)
				if(UtilidadTexto.getBoolean(datosDiagnosticos.get("principal_"+j).toString()))
					this.cirugiasSolicitud.put("diagnosticoPrincipal_"+i,"("+datosDiagnosticos.get("diagnostico_"+j)+"-"+datosDiagnosticos.get("tipoCie_"+j)+") "+datosDiagnosticos.get("nombreDiagnostico_"+j));
			
			//Se toman los diagnosticos relacionados
			int numDiagnosticos = 0;
			for(int j=0;j<Integer.parseInt(datosDiagnosticos.get("numRegistros").toString());j++)
				if(!UtilidadTexto.getBoolean(datosDiagnosticos.get("principal_"+j).toString())&&!UtilidadTexto.getBoolean(datosDiagnosticos.get("complicacion_"+j).toString()))
				{
					this.cirugiasSolicitud.put("diagnosticoRelacionado_"+i+"_"+numDiagnosticos,"("+datosDiagnosticos.get("diagnostico_"+j)+"-"+datosDiagnosticos.get("tipoCie_"+j)+") "+datosDiagnosticos.get("nombreDiagnostico_"+j));
					numDiagnosticos++;
				}
			this.cirugiasSolicitud.put("numDiagnosticos_"+i,numDiagnosticos);
			
			//Se toma el diagnostico de complicacion
			for(int j=0;j<Integer.parseInt(datosDiagnosticos.get("numRegistros").toString());j++)
				if(UtilidadTexto.getBoolean(datosDiagnosticos.get("complicacion_"+j).toString()))
					this.cirugiasSolicitud.put("diagnosticoComplicacion_"+i,"("+datosDiagnosticos.get("diagnostico_"+j)+"-"+datosDiagnosticos.get("tipoCie_"+j)+") "+datosDiagnosticos.get("nombreDiagnostico_"+j));
			//***********************************************************************************
		}
		
		//**********SE CARGA LA INFORMACION DE PARTO*****************************************
		DtoInformacionParto infoParto = UtilidadesHistoriaClinica.cargarInformacionPartoParaRips(con, numeroSolicitud+"", "");
		
		if(!infoParto.getConsecutivo().equals("")&&infoParto.getRecienNacidos().size()>0)
		{
			this.infoRecienNacidos.put("existe", ConstantesBD.acronimoSi);
			
			this.infoRecienNacidos.put("edadGestacional", infoParto.getSemanasGestacional()>0?(infoParto.getSemanasGestacional()+""):"");
			this.infoRecienNacidos.put("controlPrenatal",infoParto.isControlPrenatal()?"Sï¿½":"No");
			this.infoRecienNacidos.put("nroHijos", infoParto.getRecienNacidos().size());
			
			int cont = 0;
			for(DtoInformacionRecienNacido infoRecienNacido:infoParto.getRecienNacidos())
			{
				this.infoRecienNacidos.put("fechaNacimiento_"+cont,infoRecienNacido.getFechaNacimiento());
				this.infoRecienNacidos.put("horaNacimiento_"+cont,infoRecienNacido.getHoraNacimiento());
				this.infoRecienNacidos.put("nombreSexo_"+cont,infoRecienNacido.getNombreSexo());
				this.infoRecienNacidos.put("peso_"+cont,infoRecienNacido.getPesoEgreso()>0?(infoRecienNacido.getPesoEgreso()+""):"");
				if(!infoRecienNacido.getAcronimoDiagnosticoRecienNacido().equals(""))
					this.infoRecienNacidos.put("diagnosticoRN_"+cont,"("+infoRecienNacido.getAcronimoDiagnosticoRecienNacido()+"-"+infoRecienNacido.getCieDiagnosticoRecienNacido()+") "+infoRecienNacido.getDiagnosticoRecienNacido().getNombre());
				else
					this.infoRecienNacidos.put("diagnosticoRN_"+cont,"");
				if(!infoRecienNacido.getAcronimoDiagnosticoMuerte().equals(""))
					this.infoRecienNacidos.put("diagnosticoMuerte_"+cont,"("+infoRecienNacido.getAcronimoDiagnosticoMuerte()+"-"+infoRecienNacido.getCieDiagnosticoMuerte()+") "+infoRecienNacido.getDiagnosticoMuerte().getNombre());
				else
					this.infoRecienNacidos.put("diagnosticoMuerte_"+cont,"");
				this.infoRecienNacidos.put("fechaMuerte_"+cont,infoRecienNacido.getFechaMuerte());
				this.infoRecienNacidos.put("horaMuerte_"+cont,infoRecienNacido.getHoraMuerte());
				
				cont ++;
			}
			
			
		}
		else
			this.infoRecienNacidos.put("existe", ConstantesBD.acronimoNo);
		//***********************************************************************************
	}
	//*******************************************************************************************
	
	/**
	 * Mï¿½todo para consultar el nï¿½mero de cirugias
	 * @return
	 */
	private int getNumCirugias()
	{
		return Utilidades.convertirAEntero(this.cirugiasSolicitud.get("numRegistros")+"",true);
	}
	
	/**
	 * Mï¿½todo para obtener el nï¿½mero real de cirugias que no han sido eliminadas
	 * @return
	 */
	private int getNumCirugiasReales()
	{
		int contador = 0;
		
		for(int i=0;i<getNumCirugias();i++)
			if(!UtilidadTexto.getBoolean(this.cirugiasSolicitud.get("fueEliminado_"+i)+""))
				contador++;
		
		return contador;
	}
	
	/**
	 * @return the cirugiasSolicitud
	 */
	public HashMap<String, Object> getCirugiasSolicitud() {
		return cirugiasSolicitud;
	}
	/**
	 * @param cirugiasSolicitud the cirugiasSolicitud to set
	 */
	public void setCirugiasSolicitud(HashMap<String, Object> cirugiasSolicitud) {
		this.cirugiasSolicitud = cirugiasSolicitud;
	}
	/**
	 * @return the datosActoQx
	 */
	public HashMap<String, Object> getDatosActoQx() {
		return datosActoQx;
	}
	/**
	 * @param datosActoQx the datosActoQx to set
	 */
	public void setDatosActoQx(HashMap<String, Object> datosActoQx) {
		this.datosActoQx = datosActoQx;
	}
	/**
	 * @return the encabezadoSolicitud
	 */
	public HashMap<String, Object> getEncabezadoSolicitud() {
		return encabezadoSolicitud;
	}
	/**
	 * @param encabezadoSolicitud the encabezadoSolicitud to set
	 */
	public void setEncabezadoSolicitud(HashMap<String, Object> encabezadoSolicitud) {
		this.encabezadoSolicitud = encabezadoSolicitud;
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
	 * @return the otrosProfesionales
	 */
	public HashMap<String, Object> getOtrosProfesionales() {
		return otrosProfesionales;
	}
	/**
	 * @param otrosProfesionales the otrosProfesionales to set
	 */
	public void setOtrosProfesionales(HashMap<String, Object> otrosProfesionales) {
		this.otrosProfesionales = otrosProfesionales;
	}
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores vï¿½lidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (cargosDirectosDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			cargosDirectosDao = myFactory.getCargosDirectosCxDytDao();
		}	
	}
	
	/**
	 * Mï¿½todo que retorna el DAO instanciado de Condiciones por Servicio
	 * @return
	 */
	public static CargosDirectosCxDytDao cargosDirectosDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCargosDirectosCxDytDao();
	}
	//**********************************************************************************************************
	//***********************************************************************************************************
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
	 * @return the codigoPeticion
	 */
	public int getCodigoPeticion() {
		return codigoPeticion;
	}
	/**
	 * @return the numeroSolicitud
	 */
	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}
	/**
	 * @param numeroSolicitud the numeroSolicitud to set
	 */
	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}
	public void setDtoDiagnostico(DtoDiagnostico dtoDiagnostico) {
		this.dtoDiagnostico = dtoDiagnostico;
	}
	public DtoDiagnostico getDtoDiagnostico() {
		return dtoDiagnostico;
	}
	public void setSolPYP(boolean solPYP) {
		this.solPYP = solPYP;
	}
	public boolean isSolPYP() {
		return solPYP;
	}
	public List<InfoResponsableCobertura> getListaCoberturaCargoCirugia() {
		return listaCoberturaCargoCirugia;
	}
	public void setListaCoberturaCargoCirugia(
			List<InfoResponsableCobertura> listaCoberturaCargoCirugia) {
		this.listaCoberturaCargoCirugia = listaCoberturaCargoCirugia;
	}
	/**
	 * @return the diagnosticoPrincipal
	 */
	public String getDiagnosticoPrincipal() {
		return diagnosticoPrincipal;
	}
	/**
	 * @param diagnosticoPrincipal the diagnosticoPrincipal to set
	 */
	public void setDiagnosticoPrincipal(String diagnosticoPrincipal) {
		this.diagnosticoPrincipal = diagnosticoPrincipal;
	}
}
