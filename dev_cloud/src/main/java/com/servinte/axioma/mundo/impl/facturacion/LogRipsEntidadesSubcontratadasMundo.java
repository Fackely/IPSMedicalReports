package com.servinte.axioma.mundo.impl.facturacion;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.facturacion.ConstantesCamposProcesoRipsEntidadesSub;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.cargos.DtoTercero;
import com.princetonsa.dto.facturacion.DtoFiltroConsultaProcesoRipsEntidadesSub;
import com.princetonsa.dto.facturacion.DtoFiltroProcesarRipsEntidadesSub;
import com.princetonsa.dto.facturacion.DtoInconsistenciasRipsEntidadesSub;
import com.princetonsa.dto.facturacion.DtoResultadoConsultaLogRipsEntidadesSub;
import com.princetonsa.dto.facturacion.DtoResultadoProcesarRipsAutorizacion;
import com.princetonsa.dto.facturacion.DtoResultadoProcesarRipsEntidadesSub;
import com.princetonsa.dto.facturacion.DtoServicios;
import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.princetonsa.dto.manejoPaciente.DtoAutorizacionesEntSubRips;
import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.impl.ordenes.SolicitudesHibernateDAO;
import com.servinte.axioma.dao.interfaz.facturacion.ILogRipsEntidadesSubcontratadasDAO;
import com.servinte.axioma.mundo.interfaz.facturacion.ILogRipsEntidadesSubcontratadasMundo;
import com.servinte.axioma.orm.AutoEntsubSolicitudes;
import com.servinte.axioma.orm.AutorizacionesEntSubRips;
import com.servinte.axioma.orm.AutorizacionesEntSubServi;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;
import com.servinte.axioma.orm.LogRipsEntidadesSub;
import com.servinte.axioma.orm.Solicitudes;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.persistencia.UtilidadTransaccion;
import com.servinte.axioma.servicio.fabrica.ManejoPacienteServicioFabrica;
import com.servinte.axioma.servicio.fabrica.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.fabrica.inventario.InventarioServicioFabrica;
import com.servinte.axioma.servicio.interfaz.administracion.IPersonasServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IEntidadesSubcontratadasServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.IServiciosServicio;
import com.servinte.axioma.servicio.interfaz.facturacion.ITercerosServicio;
import com.servinte.axioma.servicio.interfaz.inventario.IArticulosServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAutorizacionesEntSubArticuloServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAutorizacionesEntSubRipsServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAutorizacionesEntSubServiServicio;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.IAutorizacionesEntidadesSubServicio;


/**
 * Esta clase se encarga de realizar las validaciones de todos los archivos del proceso rips de entidades subcontratadas
 * @author Fabián Becerra
 *
 */
public class LogRipsEntidadesSubcontratadasMundo implements ILogRipsEntidadesSubcontratadasMundo{
	
	
	ILogRipsEntidadesSubcontratadasDAO dao;
	
	public LogRipsEntidadesSubcontratadasMundo(){
		dao = FacturacionFabricaDAO.crearLogRipsEntiSubDAO();
	}
	
	/*********************      SERVICIOS UTILIZADOS   ***************************/
	IAutorizacionesEntidadesSubServicio servicioAutorizacionesPorEntidadesSubServicio= ManejoPacienteServicioFabrica.crearAutorizacionEntidadesSubServicio();
	IAutorizacionesEntSubServiServicio autorEntSubServiServicio
				= ManejoPacienteServicioFabrica.crearAutorizacionesEntSubServiServicio();
	IAutorizacionesEntSubArticuloServicio autorEntSubArtiServicio
				= ManejoPacienteServicioFabrica.crearAutorizacionesEntSubArticulo();
	IEntidadesSubcontratadasServicio servicioEntidadesSub= FacturacionServicioFabrica.crearEntidadesSubcontratadasServicio();
	IAutorizacionesEntSubRipsServicio autorEntSubRips=ManejoPacienteServicioFabrica.crearIAutorizacionesEntSubRipsServicio();
	IArticulosServicio servicioArticulo=InventarioServicioFabrica.crearArticulosServicio();
	IServiciosServicio servicioServ= FacturacionServicioFabrica.crearServiciosServicio();
	
	/**  
	 * LISTAS UTILIZADAS PARA ALMACENAR Y GUARDAR LAS INCONSISTENCIAS GENERADAS
	 * EN EL PROCESO Y LOS VALORES DE LOS ARCHIVOS
	 */
	private ArrayList<DtoInconsistenciasRipsEntidadesSub> listaInconsistencias= new ArrayList<DtoInconsistenciasRipsEntidadesSub>();
	private ArrayList<DtoInconsistenciasRipsEntidadesSub> listaInconsistenciasCampos= new ArrayList<DtoInconsistenciasRipsEntidadesSub>();
	private ArrayList<DtoInconsistenciasRipsEntidadesSub> listaAutorizacionesValidas= new ArrayList<DtoInconsistenciasRipsEntidadesSub>();
	private ArrayList<DtoInconsistenciasRipsEntidadesSub> listaValoresCampos= new ArrayList<DtoInconsistenciasRipsEntidadesSub>();
	
	
	/****    LISTAS QUE ALMACENAN LOS REGISTROS QUE PASARON LAS VALIDACIONES DE ESTRUCTURA DE ARCHIVOS **************/
	private ArrayList<Integer> registrosSinInconsistenciaAC= new ArrayList<Integer>();
	private ArrayList<Integer> registrosSinInconsistenciaAP= new ArrayList<Integer>();
	private ArrayList<Integer> registrosSinInconsistenciaAM= new ArrayList<Integer>();
	private ArrayList<Integer> registrosSinInconsistenciaAT= new ArrayList<Integer>();
	private ArrayList<Integer> registrosSinInconsistenciaUS= new ArrayList<Integer>();
	
	
	
	@SuppressWarnings("rawtypes")
	private HashMap tiposIdentificacion=new HashMap();
	private ArrayList<String> numerosFacturaArchivoAF= new ArrayList<String>();
	private ArrayList<String> codigosPropietariosServicioValidos= new ArrayList<String>();
	private ArrayList<String> codigosArticulosPorTipoCodigoMedicInsum= new ArrayList<String>();
	double sumatoriaValorPagarEntidadContratArchivoAF=0;
	double sumatoriaValorTotalPorConceptoArchivoAD=0;
	DtoResultadoProcesarRipsEntidadesSub dtoResultadoProcesarRipsEntSub=new DtoResultadoProcesarRipsEntidadesSub();
	
	private ActionErrors errors=new ActionErrors();
	MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.facturacion.RipsEntidadesSubcontratadasForm");
	
	
	/**
	 * Este método se encarga de validar si el nombre de archivo enviado como párametro 
	 * tiene el formato correcto establecido para los archivos planos de rips
	 * @param nombreArchivo nombre del archivo plano
	 * @param acronimoTipoArchivo tipo del archivo enviado
	 * @return error true si hay inconsistencias
	 */
	public boolean validarNombreExtensionArchivos(String nombreArchivo, String acronimoTipoArchivo){
		
		String nombreArchivoSinExt="";
		boolean error=false;
		if(nombreArchivo.contains("."))
			nombreArchivoSinExt =  nombreArchivo.substring(0, nombreArchivo.indexOf("."));
			
		if(UtilidadTexto.isEmpty(nombreArchivoSinExt)||nombreArchivoSinExt.length()<3||nombreArchivoSinExt.length()>8){
			//this.generarInconsistencia(acronimoTipoArchivo, ConstantesIntegridadDominio.acronimoNombreArchivoNoValido);
			this.generarInconsistencia(nombreArchivo, ConstantesIntegridadDominio.acronimoNombreArchivoNoValido);
			error=true;
		}else{
			String acronimoArchivo = nombreArchivo.substring(0, 2);
			String extensionArchivo= nombreArchivo.substring(nombreArchivo.indexOf("."), nombreArchivo.length());
			
			if(!acronimoArchivo.equals("")&&!acronimoArchivo.equals(acronimoTipoArchivo)){
				//this.generarInconsistencia(acronimoTipoArchivo, ConstantesIntegridadDominio.acronimoNombreArchivoNoValido);
				this.generarInconsistencia(nombreArchivo, ConstantesIntegridadDominio.acronimoNombreArchivoNoValido);
				error=true;
			}else
			if(!extensionArchivo.equals(".txt")){
				//this.generarInconsistencia(acronimoTipoArchivo, ConstantesIntegridadDominio.acronimoFormatoArchivoNoValido);
				this.generarInconsistencia(nombreArchivo, ConstantesIntegridadDominio.acronimoFormatoArchivoNoValido);
				error=true;
			}
		}
		
		return error;	
		
	}
	
	public boolean validarNumerosRemision(String numeroRemision, String nombreArchivo){
		
		String numeroRemisionActual=nombreArchivo.substring(2,nombreArchivo.indexOf("."));
		if(numeroRemisionActual.equals(numeroRemision)){
			return true;
		}else{
			return false;
		}
	}
	
	public DtoResultadoProcesarRipsEntidadesSub procesarRipsEntidadesSub(DtoFiltroProcesarRipsEntidadesSub dtoFiltro){
		
		
		boolean error;
		boolean realizoValidacionesArchivos=false;
		// 1) REVISO SI HAY INCONSISTENCIAS EN EL NOMBRE Y LA EXTENSION DEL ARCHIVO
		this.validarNombreExtensionArchivos(dtoFiltro.getNombreArchivoCT(), ConstantesBD.ripsCT);
		this.validarNombreExtensionArchivos(dtoFiltro.getNombreArchivoAF(), ConstantesBD.ripsAF);
		this.validarNombreExtensionArchivos(dtoFiltro.getNombreArchivoUS(), ConstantesBD.ripsUS);
		this.validarNombreExtensionArchivos(dtoFiltro.getNombreArchivoAD(), ConstantesBD.ripsAD);
				
		if(!UtilidadTexto.isEmpty(dtoFiltro.getNombreArchivoAC())){
			error=this.validarNombreExtensionArchivos(dtoFiltro.getNombreArchivoAC(), ConstantesBD.ripsAC);
			if(error)
				dtoFiltro.setNombreArchivoAC("");
		}
		
		if(!UtilidadTexto.isEmpty(dtoFiltro.getNombreArchivoAP())){
			error=this.validarNombreExtensionArchivos(dtoFiltro.getNombreArchivoAP(), ConstantesBD.ripsAP);
			if(error)
				dtoFiltro.setNombreArchivoAP("");
		}
		
		/*if(!UtilidadTexto.isEmpty(dtoFiltro.getNombreArchivoAH())){
			error=this.validarNombreExtensionArchivos(dtoFiltro.getNombreArchivoAH(), ConstantesBD.ripsAH);
			if(error)
				dtoFiltro.setNombreArchivoAH("");
		}*/
		
		/*if(!UtilidadTexto.isEmpty(dtoFiltro.getNombreArchivoAU())){
			error=this.validarNombreExtensionArchivos(dtoFiltro.getNombreArchivoAU(), ConstantesBD.ripsAU);
			if(error)
				dtoFiltro.setNombreArchivoAU("");
		}*/
		
		if(!UtilidadTexto.isEmpty(dtoFiltro.getNombreArchivoAM())){
			error=this.validarNombreExtensionArchivos(dtoFiltro.getNombreArchivoAM(), ConstantesBD.ripsAM);
			if(error)
				dtoFiltro.setNombreArchivoAM("");
		}
			
		if(!UtilidadTexto.isEmpty(dtoFiltro.getNombreArchivoAT())){	
			error=this.validarNombreExtensionArchivos(dtoFiltro.getNombreArchivoAT(), ConstantesBD.ripsAT);
			if(error)
				dtoFiltro.setNombreArchivoAT("");
		}
		
		
		ArrayList<String> inconsistenciasArchivos= new ArrayList<String>();
		ArrayList<String> nombresArchivosProcesados=new ArrayList<String>();
		for(DtoInconsistenciasRipsEntidadesSub dtoInconsistencia:listaInconsistencias){
			inconsistenciasArchivos.add(dtoInconsistencia.getNombreArchivo());
			nombresArchivosProcesados.add(dtoInconsistencia.getNombreArchivo());
		}
		
		// 2) SE VERIFICA QUE LOS ARCHIVOS OBLIGATORIOS NO TENGAN INCONSISTENCIAS
		if(inconsistenciasArchivos.contains(dtoFiltro.getNombreArchivoCT())||inconsistenciasArchivos.contains(dtoFiltro.getNombreArchivoAF())
				||inconsistenciasArchivos.contains(dtoFiltro.getNombreArchivoUS())||inconsistenciasArchivos.contains(dtoFiltro.getNombreArchivoAD())){
			
			errors.add("archivos requeridos", new ActionMessage("errors.notEspecific", 
					fuenteMensaje.getMessage("RipsEntidadesSubcontratadasForm.procesoCancelado")));
		}
		else // 3) SE VERIFICA QUE DE LOS ARCHIVOS NO OBLIGATORIOS POR LO MENOS UNO NO TENGA INCONSISTENCIAS
			if(inconsistenciasArchivos.contains(dtoFiltro.getNombreArchivoAC())&&inconsistenciasArchivos.contains(dtoFiltro.getNombreArchivoAP())
					//&&inconsistenciasArchivos.contains(ConstantesBD.ripsAH)&&inconsistenciasArchivos.contains(ConstantesBD.ripsAU)
					&&inconsistenciasArchivos.contains(dtoFiltro.getNombreArchivoAM())&&inconsistenciasArchivos.contains(dtoFiltro.getNombreArchivoAT())){
				
				errors.add("archivos requeridos", new ActionMessage("errors.notEspecific", 
						fuenteMensaje.getMessage("RipsEntidadesSubcontratadasForm.procesoCancelado")));
							
			}else{
				
				// 4) VALIDAR LOS NUMEROS DE REMISION SEAN IGUALES EN TODOS LOS ARCHIVOS QUE PASARON LAS VALIDACIONES ANTERIORES
				String numeroRemision= dtoFiltro.getNombreArchivoCT().substring(2,dtoFiltro.getNombreArchivoCT().indexOf("."));
				int valida=0;
				
				//Se valida el número de remision del archivo de control con el de los demas archivos 
				if(!this.validarNumerosRemision(numeroRemision,dtoFiltro.getNombreArchivoAF())){
					valida++;
					this.generarInconsistencia(dtoFiltro.getNombreArchivoAF(), ConstantesIntegridadDominio.acronimoNumeroRemisionNoValido);
				}
				
				if(!this.validarNumerosRemision(numeroRemision,dtoFiltro.getNombreArchivoUS())){
					this.generarInconsistencia(dtoFiltro.getNombreArchivoUS(), ConstantesIntegridadDominio.acronimoNumeroRemisionNoValido);
					valida++;
				}
				
				if(!this.validarNumerosRemision(numeroRemision,dtoFiltro.getNombreArchivoAD())){
					this.generarInconsistencia(dtoFiltro.getNombreArchivoAD(), ConstantesIntegridadDominio.acronimoNumeroRemisionNoValido);
					valida++;
				}
				
				if(!UtilidadTexto.isEmpty(dtoFiltro.getNombreArchivoAC()))
					if(!this.validarNumerosRemision(numeroRemision,dtoFiltro.getNombreArchivoAC())){
						this.generarInconsistencia(dtoFiltro.getNombreArchivoAC(), ConstantesIntegridadDominio.acronimoNumeroRemisionNoValido);
						valida++;
					}
					
				if(!UtilidadTexto.isEmpty(dtoFiltro.getNombreArchivoAP()))
					if(!this.validarNumerosRemision(numeroRemision,dtoFiltro.getNombreArchivoAP())){
						this.generarInconsistencia(dtoFiltro.getNombreArchivoAP(), ConstantesIntegridadDominio.acronimoNumeroRemisionNoValido);
						valida++;
					}
				/*if(!UtilidadTexto.isEmpty(dtoFiltro.getNombreArchivoAH()))
					if(!this.validarNumerosRemision(numeroRemision,dtoFiltro.getNombreArchivoAH()))
						valida++;
				
				if(!UtilidadTexto.isEmpty(dtoFiltro.getNombreArchivoAU()))
					if(!this.validarNumerosRemision(numeroRemision,dtoFiltro.getNombreArchivoAU()))
						valida++;
				*/
				
				if(!UtilidadTexto.isEmpty(dtoFiltro.getNombreArchivoAM()))
					if(!this.validarNumerosRemision(numeroRemision,dtoFiltro.getNombreArchivoAM())){
						this.generarInconsistencia(ConstantesBD.ripsAM, ConstantesIntegridadDominio.acronimoNumeroRemisionNoValido);
						valida++;
					}
				if(!UtilidadTexto.isEmpty(dtoFiltro.getNombreArchivoAT()))
					if(!this.validarNumerosRemision(numeroRemision,dtoFiltro.getNombreArchivoAT())){
						this.generarInconsistencia(ConstantesBD.ripsAT, ConstantesIntegridadDominio.acronimoNumeroRemisionNoValido);
						valida++;
					}
				
				if(valida==0){
					realizoValidacionesArchivos=true;
				}
				else{
					//El numero de remision no coincide se genera error e inconsistencias de los archivos
					errors.add("archivos requeridos", new ActionMessage("errors.notEspecific", 
							fuenteMensaje.getMessage("RipsEntidadesSubcontratadasForm.procesoCancelado")));
					this.generarInconsistencia(dtoFiltro.getNombreArchivoCT(), ConstantesIntegridadDominio.acronimoNumeroRemisionNoValido);
				
					/*if(!UtilidadTexto.isEmpty(dtoFiltro.getNombreArchivoAH()))
						this.generarInconsistencia(ConstantesBD.ripsAH, ConstantesIntegridadDominio.acronimoNumeroRemisionNoValido);
					
					if(!UtilidadTexto.isEmpty(dtoFiltro.getNombreArchivoAU()))
						this.generarInconsistencia(ConstantesBD.ripsAU, ConstantesIntegridadDominio.acronimoNumeroRemisionNoValido);
						*/
				}
						
				
				// 5) VALIDACIONES ESTRUCTURA DE ARCHIVOS PLANOS
				if(realizoValidacionesArchivos){
					
					//REALIZO PROCESOS GENERICOS UTILIZADOS EN VARIAS FUNCIONES
					//Cargar tipos de identificacion en el sistema
						this.tiposIdentificacion=Utilidades.consultarTiposidentificacion(dtoFiltro.getInstitucion());
					//Cargar los servicios por tipo tarifario oficial seleccionado
						ArrayList<DtoServicios> listaServicios=servicioServ.obtenerServiciosXTipoTarifarioOficial(ConstantesBD.codigoNuncaValido,dtoFiltro.getTarifarioSeleccionadoCodServicios());
						for(DtoServicios serv:listaServicios)
							this.codigosPropietariosServicioValidos.add(serv.getCodigoPropietarioServicio());
					//Cargar los codigos de los articulos dependiendo el tipo de codigo de medicam e insumo seleccionado
						this.codigosArticulosPorTipoCodigoMedicInsum=servicioArticulo.consultarCodigosArticulosPorTipoCodigo(dtoFiltro.getAcronimoCodMedicInsum());
					
						
						
						nombresArchivosProcesados=new ArrayList<String>();
					int errores=0;
					
					//Se llama primero este archivo para cargar los números de factura utilizados en los demas archivos
					//y la sumatoria del valor neto a pagar por la entidad subcontratante
					boolean erroresArchivoAF=this.validacionesGeneralesAF(dtoFiltro);
					nombresArchivosProcesados.add(ConstantesBD.ripsAF);
					
					//El archivo AD que carga el valor de la sumatoria del valor total por concepto
					boolean erroresArchivoAD=this.validacionesGeneralesAD(dtoFiltro);
					nombresArchivosProcesados.add(ConstantesBD.ripsAD);
					
					//Si no se presentaron errores en ambos archivos se comparan las sumatorias del valor total por concepto AD
					//y la sumatoria del valor neto a pagar por la entidad contratante AF
					boolean erroresSumatoria=false;
					if(!erroresArchivoAF&&!erroresArchivoAD){
						//Si no son iguales se genera inconsistencia para el archivo AF
						if(sumatoriaValorPagarEntidadContratArchivoAF!=sumatoriaValorTotalPorConceptoArchivoAD){
							erroresSumatoria=true;
							this.generarInconsistenciaCampos(ConstantesBD.ripsAF, ConstantesIntegridadDominio.acronimoSumatoriasValoresAFADNoIguales, "Valor neto a pagar por la entidad contratante", 1);
						}
					}
					
					if(erroresArchivoAF)
						errores++;
					
					if(erroresArchivoAD)
						errores++;
					
					
					
					/*if(!UtilidadTexto.isEmpty(dtoFiltro.getNombreArchivoAH())){
						nombresArchivosProcesados.add(ConstantesBD.ripsAH);
						if(this.validacionesGeneralesAH(dtoFiltro))
							errores++;
					}
						
					
					if(!UtilidadTexto.isEmpty(dtoFiltro.getNombreArchivoAU())){
						nombresArchivosProcesados.add(ConstantesBD.ripsAU);
						if(this.validacionesGeneralesAU(dtoFiltro))
							errores++;
					}*/
					
					if(!UtilidadTexto.isEmpty(dtoFiltro.getNombreArchivoAC())){
						nombresArchivosProcesados.add(ConstantesBD.ripsAC);
						if(this.validacionesGeneralesAC(dtoFiltro))
							errores++;
					}
					
					if(!UtilidadTexto.isEmpty(dtoFiltro.getNombreArchivoAP())){
						nombresArchivosProcesados.add(ConstantesBD.ripsAP);
						if(this.validacionesGeneralesAP(dtoFiltro))
							errores++;
					}
					
					if(!UtilidadTexto.isEmpty(dtoFiltro.getNombreArchivoAM())){
						nombresArchivosProcesados.add(ConstantesBD.ripsAM);
						if(this.validacionesGeneralesAM(dtoFiltro))
							errores++;
					}
					
					if(!UtilidadTexto.isEmpty(dtoFiltro.getNombreArchivoAT())){
						nombresArchivosProcesados.add(ConstantesBD.ripsAT);
						if(this.validacionesGeneralesAT(dtoFiltro))
							errores++;
					}
					
					
					//SE REALIZA LA VALIDACION PARA FACTURAS RELACIONADAS DE ARCHIVO AD CON ARCHIVOS AC, AP, AT  y AM
					boolean erroresFacturas=false;
					if(!erroresArchivoAD)
					{
						erroresFacturas=this.validacionesFacturas(dtoFiltro);
					}
					
					
					if(this.validacionesGeneralesUS(dtoFiltro))
						errores++;
					nombresArchivosProcesados.add(ConstantesBD.ripsUS);
					
					
					//SE VALIDA EL ARCHIVO DE CONTROL
					if(this.validacionesGeneralesCT(dtoFiltro))
						errores++;
					nombresArchivosProcesados.add(ConstantesBD.ripsCT);
					
					//if(errores==0){
					if(!erroresSumatoria&&!erroresFacturas){
						//VALIDACIONES ARCHIVO US OTROS ARCHIVOS
						this.validarArchivosConArchivoUS(dtoFiltro);
					}
					//}
				}
						
					
				
			}
		
		
		
		
		
		dtoResultadoProcesarRipsEntSub.setNombresArchivoCargue(nombresArchivosProcesados);
		dtoResultadoProcesarRipsEntSub.setErrores(errors);
		dtoResultadoProcesarRipsEntSub.setDtoInconsistenciasArchivos(listaInconsistencias);
		dtoResultadoProcesarRipsEntSub.setDtoInconsistenciasCamposReg(listaInconsistenciasCampos);
		dtoResultadoProcesarRipsEntSub.setDtoValoresCampos(listaValoresCampos);
		dtoResultadoProcesarRipsEntSub.setListaAutorizacionesValidas(listaAutorizacionesValidas);
				
		return dtoResultadoProcesarRipsEntSub;
		
	}
	
	
	/**
	 * Método para que realiza las validaciones de facturas relacionadas 
	 * de archivo AD con archivos AC,AP,AT,AM
	 * @param dtoFiltro
	 */
	private boolean validacionesFacturas(DtoFiltroProcesarRipsEntidadesSub dtoFiltro) 
	{
		
		boolean erroresFacturas=false;
		try
		{
			String cadenaAD="";
			String cadenaAC="";
			String cadenaAP="";
			String cadenaAT="";
			String cadenaAM="";
			
			int contador = 0;
			String[] camposAD = new String[0];
			String[] camposAC = new String[0];
			String[] camposAP = new String[0];
			String[] camposAT = new String[0];
			String[] camposAM = new String[0];
			//******SE INICIALIZA ARCHIVO*************************
			BufferedReader bufferAD = new BufferedReader(new java.io.InputStreamReader(dtoFiltro.getArchivoAD().getInputStream()));
			BufferedReader bufferAC = new BufferedReader(new java.io.InputStreamReader(dtoFiltro.getArchivoAC().getInputStream()));
			BufferedReader bufferAP = new BufferedReader(new java.io.InputStreamReader(dtoFiltro.getArchivoAP().getInputStream()));
			BufferedReader bufferAT = new BufferedReader(new java.io.InputStreamReader(dtoFiltro.getArchivoAT().getInputStream()));
			BufferedReader bufferAM = new BufferedReader(new java.io.InputStreamReader(dtoFiltro.getArchivoAM().getInputStream()));
			
			//********SE RECORRE LÍNEA POR LÍNEA ARCHIVO AD**************
			cadenaAD=bufferAD.readLine();
			while(cadenaAD!=null)
			{
				contador++;
				int valida=0;
				//Se toman los campos de cada línea del archivo
				if(cadenaAD.endsWith(","))
					cadenaAD+=" ";
				camposAD = cadenaAD.split(",");
				
				//********SE RECORRE LÍNEA POR LÍNEA ARCHIVO AC**************
				if(UtilidadTexto.isEmpty(dtoFiltro.getNombreArchivoAC())){
					valida++;
				}
				else{
					cadenaAC=bufferAC.readLine();
					while(cadenaAC!=null)
					{
						//Se toman los campos de cada línea del archivo
						if(cadenaAC.endsWith(","))
							cadenaAC+=" ";
						camposAC = cadenaAC.split(",");
						cadenaAC=bufferAC.readLine();
						
						//1) ******** NÚMERO DE FACTURA************************************
						if(camposAD[0].equals(camposAC[0])&&camposAD[5].equals(camposAC[16])){
							valida++;
							cadenaAC=null;
						}
						
						if(cadenaAC!=null)
						cadenaAC=bufferAC.readLine();
						
					}
				}
				
				//***************CERRAR ARCHIVO****************************
				bufferAC.close();
			
				//********SE RECORRE LÍNEA POR LÍNEA ARCHIVO AP**************
				if(UtilidadTexto.isEmpty(dtoFiltro.getNombreArchivoAP())){
					valida++;
				}
				else{
					cadenaAP=bufferAP.readLine();
					while(cadenaAP!=null)
					{
						//Se toman los campos de cada línea del archivo
						if(cadenaAP.endsWith(","))
							cadenaAP+=" ";
						camposAP = cadenaAP.split(",");
						cadenaAP=bufferAP.readLine();
						
						//1) ******** NÚMERO DE FACTURA************************************
						if(camposAD[0].equals(camposAP[0])&&camposAD[5].equals(camposAP[14])){
							valida++;
							cadenaAP=null;
						}
						
						if(cadenaAP!=null)
						cadenaAP=bufferAP.readLine();
						
					}
				}
				//***************CERRAR ARCHIVO****************************
				bufferAP.close();
				
				//********SE RECORRE LÍNEA POR LÍNEA ARCHIVO AT**************
				if(UtilidadTexto.isEmpty(dtoFiltro.getNombreArchivoAT())){
					valida++;
				}
				else{
					cadenaAT=bufferAT.readLine();
					while(cadenaAT!=null)
					{
						//Se toman los campos de cada línea del archivo
						if(cadenaAT.endsWith(","))
							cadenaAT+=" ";
						camposAT = cadenaAT.split(",");
						cadenaAT=bufferAT.readLine();
						
						//1) ******** NÚMERO DE FACTURA************************************
						if(camposAD[0].equals(camposAT[0])&&camposAD[5].equals(camposAT[10])){
							valida++;
							cadenaAT=null;
						}
						
						if(cadenaAT!=null)
						cadenaAT=bufferAT.readLine();
						
					}
				}
				//***************CERRAR ARCHIVO****************************
				bufferAT.close();
				
				//********SE RECORRE LÍNEA POR LÍNEA ARCHIVO AM**************
				if(UtilidadTexto.isEmpty(dtoFiltro.getNombreArchivoAM())){
					valida++;
				}
				else{
					cadenaAM=bufferAM.readLine();
					while(cadenaAM!=null)
					{
						//Se toman los campos de cada línea del archivo
						if(cadenaAM.endsWith(","))
							cadenaAM+=" ";
						camposAM = cadenaAM.split(",");
						cadenaAM=bufferAM.readLine();
						
						//1) ******** NÚMERO DE FACTURA************************************
						if(camposAD[0].equals(camposAM[0])&&camposAD[5].equals(camposAM[13])){
							valida++;
							cadenaAM=null;
						}
						
						if(cadenaAM!=null)
						cadenaAM=bufferAM.readLine();
						
					}
				}
				//***************CERRAR ARCHIVO****************************
				bufferAM.close();
				
				if(valida<4){//Significa que en los 4 no encontro la factura o los valores no son iguales
					this.generarInconsistenciaCampos(ConstantesBD.ripsAD, ConstantesIntegridadDominio.acronimoValoresFacturasADConACAPATAMNoIguales, "Valor total por concepto", contador);
					erroresFacturas=true;
				}
				
				cadenaAD=bufferAD.readLine();
			}
			
			
			//***************CERRAR ARCHIVO****************************
			bufferAD.close();
			
		}
		catch(FileNotFoundException e)
		{
			Log4JManager.error("No se pudo encontrar el archivo "+dtoFiltro.getArchivoAD().getFileName()+" al cargarlo: "+e);
			
		}
		catch(IOException e)
		{
			Log4JManager.error("Error en los streams del archivo "+dtoFiltro.getArchivoAD().getFileName()+" al cargarlo: "+e);
		}
		return erroresFacturas;
	}
	
	
	/**
	 * Método para realizar las validaciones del archivo CT
	 * @param pos
	 */
	private boolean validacionesGeneralesCT(DtoFiltroProcesarRipsEntidadesSub dtoFiltro) 
	{
		int contador = 0;
		boolean error=false;
		try
		{
			String cadena="";
			
			String[] campos = new String[0];
			//******SE INICIALIZA ARCHIVO*************************
			BufferedReader buffer = new BufferedReader(new java.io.InputStreamReader(dtoFiltro.getArchivoCT().getInputStream()));
			
			//********SE RECORRE LÍNEA POR LÍNEA**************
			cadena=buffer.readLine();
			while(cadena!=null)
			{
				contador++;
				int errores=0;
				//Se toman los campos de cada línea del archivo
				if(cadena.endsWith(","))
					cadena+=" ";
				campos = cadena.split(",");
								
				//Validación de que todo esté separado por comas
				if(campos.length<4||campos.length>4||UtilidadCadena.numeroOcurrenciasCaracter(cadena, ',')!=3)
				{
					//ALMACENO LOS CAMPOS A MOSTRAR EN LA CONSULTA POR EL ARCHIVO CT
					this.agregarValoresCampos(ConstantesBD.ripsCT, "", ConstantesCamposProcesoRipsEntidadesSub.fechaDeRemision, contador);
					this.agregarValoresCampos(ConstantesBD.ripsCT, "", ConstantesCamposProcesoRipsEntidadesSub.codigoDeArchivo, contador);
					this.agregarValoresCampos(ConstantesBD.ripsCT, "", ConstantesCamposProcesoRipsEntidadesSub.totalDeRegistros, contador);
					this.generarInconsistenciaCampos(ConstantesBD.ripsCT, ConstantesIntegridadDominio.acronimoCamposSeparacionComasErroneo, "",contador);
					error=true;
				}
				else
				{
					
					//ALMACENO LOS CAMPOS A MOSTRAR EN LA CONSULTA POR EL ARCHIVO CT
					this.agregarValoresCampos(ConstantesBD.ripsCT, campos[1], ConstantesCamposProcesoRipsEntidadesSub.fechaDeRemision, contador);
					this.agregarValoresCampos(ConstantesBD.ripsCT, campos[2], ConstantesCamposProcesoRipsEntidadesSub.codigoDeArchivo, contador);
					this.agregarValoresCampos(ConstantesBD.ripsCT, campos[3], ConstantesCamposProcesoRipsEntidadesSub.totalDeRegistros, contador);
					
					//1) ******* CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD**********************
					if(validacionGeneralCampo(campos[0],"Código prestador de servicios salud",12,ConstantesBD.ripsCT,contador,false,false,false,false,true))
						errores++;
					//2) ******** FECHA DE REMISION **********************************************
					if(validacionGeneralCampo(campos[1],"Fecha de remisión",10,ConstantesBD.ripsCT,contador,false,false,true,false,true))
						errores++;
					//3) ******** CODIGO DEL ARCHIVO ********************************************
					if(validacionGeneralCampo(campos[2],"Código del archivo",8,ConstantesBD.ripsCT,contador,false,false,false,false,true))
						errores++;
					//4) ******** TOTAL REGISTROS************************************
					if(validacionGeneralCampo(campos[3],"Total de registros",10,ConstantesBD.ripsCT,contador,true,false,false,false,true))
						errores++;
					
					//SI NO SE PRESENTARON ERRORES SE CONTINUA CON LAS VALIDACIONES, SI HAY ERRORES SE CANCELA EL PROCESO
					boolean errorvalidaciones=false;
					if(errores>0){
						error=true;
						errors.add("error archivo control", new ActionMessage("errors.notEspecific", 
								fuenteMensaje.getMessage("RipsEntidadesSubcontratadasForm.errorArchivoCT")));
					}else{
						
						//1) ******* CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD**********************
						//Validar que este código corresponda al código Minsalud parametrizado en el sistema para la entidad subcontratada en la funcionalidad de  'lectura de planos pacientes entidades subcontratadas'.
						/*EntidadesSubcontratadas entidadSub=servicioEntidadesSub.obtenerEntidadesSubcontratadasporId(dtoFiltro.getCodigoPkEntidadSub());
						if(UtilidadTexto.isEmpty(entidadSub.getCodigoMinsalud())){
							errorvalidaciones=true;
							this.generarInconsistenciaCampos(ConstantesBD.ripsCT, ConstantesIntegridadDominio.acronimoCodigoMinsaludNoParametrizado, "",contador);
							errors.add("error archivo control", new ActionMessage("errors.notEspecific", 
									fuenteMensaje.getMessage("RipsEntidadesSubcontratadasForm.codigoPrestServSaludNoParametrizado")));
						}
						
						if(!entidadSub.getCodigoMinsalud().equals(campos[0]))
						{
							this.generarInconsistenciaCampos(ConstantesBD.ripsCT, ConstantesIntegridadDominio.acronimoCampoNoValido, "Código del prestador de servicios de salud",contador);
							errorvalidaciones=true;
							errors.add("error archivo control", new ActionMessage("errors.notEspecific", 
									fuenteMensaje.getMessage("RipsEntidadesSubcontratadasForm.codigoPrestServSaludNoCoincideEntSub")));
						}*/
						if(campos[0].trim().length()>10)
							campos[0] = campos[0].substring(0, 10);
						
						//2) ******** FECHA DE REMISION **********************************************
						//Validar que la fecha sea < = a la fecha del sistema.
						String fechaRemision = UtilidadFecha.conversionFormatoFechaAAp(campos[1]);
						String fechaActual = UtilidadFecha.conversionFormatoFechaAAp(Calendar.getInstance().getTime());
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaRemision, fechaActual)){
							this.generarInconsistenciaCampos(ConstantesBD.ripsCT, ConstantesIntegridadDominio.acronimoCampoNoValido, "Fecha de remisión",contador);
							errorvalidaciones=true;
						}
						if(campos[1].trim().length()>10)
							campos[1] = campos[1].substring(0, 10);
						
						//3) ******** CODIGO DEL ARCHIVO ********************************************
						//Se verifica que el codigo del archivo concuerde con algun archivo leido
						boolean existe = false;
						if(!UtilidadTexto.isEmpty(dtoFiltro.getArchivoCT().getFileName())&&dtoFiltro.getArchivoCT().getFileName().contains("."))
						   if(dtoFiltro.getArchivoCT().getFileName().substring(0, dtoFiltro.getArchivoCT().getFileName().indexOf(".")).equals(campos[2].trim()))
								existe = true;
						if(!UtilidadTexto.isEmpty(dtoFiltro.getArchivoAF().getFileName())&&dtoFiltro.getArchivoAF().getFileName().contains("."))
							if(dtoFiltro.getArchivoAF().getFileName().substring(0, dtoFiltro.getArchivoAF().getFileName().indexOf(".")).equals(campos[2].trim()))
								existe = true;
						if(!UtilidadTexto.isEmpty(dtoFiltro.getArchivoUS().getFileName())&&dtoFiltro.getArchivoUS().getFileName().contains("."))
							if(dtoFiltro.getArchivoUS().getFileName().substring(0, dtoFiltro.getArchivoUS().getFileName().indexOf(".")).equals(campos[2].trim()))
								existe = true;
						if(!UtilidadTexto.isEmpty(dtoFiltro.getArchivoAD().getFileName())&&dtoFiltro.getArchivoAD().getFileName().contains("."))
							if(dtoFiltro.getArchivoAD().getFileName().substring(0, dtoFiltro.getArchivoAD().getFileName().indexOf(".")).equals(campos[2].trim()))
								existe = true;
						if(!UtilidadTexto.isEmpty(dtoFiltro.getArchivoAC().getFileName())&&dtoFiltro.getArchivoAC().getFileName().contains("."))
							if(dtoFiltro.getArchivoAC().getFileName().substring(0, dtoFiltro.getArchivoAC().getFileName().indexOf(".")).equals(campos[2].trim()))
								existe = true;
						if(!UtilidadTexto.isEmpty(dtoFiltro.getArchivoAP().getFileName())&&dtoFiltro.getArchivoAP().getFileName().contains("."))
							if(dtoFiltro.getArchivoAP().getFileName().substring(0, dtoFiltro.getArchivoAP().getFileName().indexOf(".")).equals(campos[2].trim()))
								existe = true;
						
						/*if(!UtilidadTexto.isEmpty(dtoFiltro.getArchivoAH().getFileName())&&dtoFiltro.getArchivoAH().getFileName().contains("."))
							if(dtoFiltro.getArchivoAH().getFileName().substring(0, dtoFiltro.getArchivoAH().getFileName().indexOf(".")).equals(campos[2].trim()))
								existe = true;
						if(!UtilidadTexto.isEmpty(dtoFiltro.getArchivoAU().getFileName())&&dtoFiltro.getArchivoAU().getFileName().contains("."))
							if(dtoFiltro.getArchivoAU().getFileName().substring(0, dtoFiltro.getArchivoAU().getFileName().indexOf(".")).equals(campos[2].trim()))
								existe = true;*/
						
								
						if(!UtilidadTexto.isEmpty(dtoFiltro.getArchivoAM().getFileName())&&dtoFiltro.getArchivoAM().getFileName().contains("."))
							if(dtoFiltro.getArchivoAM().getFileName().substring(0, dtoFiltro.getArchivoAM().getFileName().indexOf(".")).equals(campos[2].trim()))
								existe = true;
						if(!UtilidadTexto.isEmpty(dtoFiltro.getArchivoAT().getFileName())&&dtoFiltro.getArchivoAT().getFileName().contains("."))
							if(dtoFiltro.getArchivoAT().getFileName().substring(0, dtoFiltro.getArchivoAT().getFileName().indexOf(".")).equals(campos[2].trim()))
								existe = true;
						
						//Si no se encontró el nombre del archivo y no está vacío se genera inconsistencia
						if(!existe&&!campos[2].trim().equals(""))
						{
							this.generarInconsistenciaCampos(ConstantesBD.ripsCT, ConstantesIntegridadDominio.acronimoCampoNoValido, "Código del archivo",contador);
							errorvalidaciones=true;
						}else
						{
							// 4) En este campo se relaciona el número total de registros que tiene cada archivo a leer.
								
								if(!UtilidadTexto.isEmpty(dtoFiltro.getArchivoAF().getFileName())&&dtoFiltro.getArchivoAF().getFileName().contains("."))
									if(dtoFiltro.getArchivoAF().getFileName().substring(0, dtoFiltro.getArchivoAF().getFileName().indexOf(".")).equals(campos[2].trim()))
										if(this.dtoResultadoProcesarRipsEntSub.getRegistrosArchivoAF()!=Utilidades.convertirAEntero(campos[3])){
											this.generarInconsistenciaCampos(ConstantesBD.ripsCT, ConstantesIntegridadDominio.acronimoCampoNoValido, "Total de registros",contador);
											errorvalidaciones=true;
										}
										
								if(!UtilidadTexto.isEmpty(dtoFiltro.getArchivoUS().getFileName())&&dtoFiltro.getArchivoUS().getFileName().contains("."))
									if(dtoFiltro.getArchivoUS().getFileName().substring(0, dtoFiltro.getArchivoUS().getFileName().indexOf(".")).equals(campos[2].trim()))
										if(this.dtoResultadoProcesarRipsEntSub.getRegistrosArchivoUS()!=Utilidades.convertirAEntero(campos[3])){
											this.generarInconsistenciaCampos(ConstantesBD.ripsCT, ConstantesIntegridadDominio.acronimoCampoNoValido, "Total de registros",contador);
											errorvalidaciones=true;
										}
										
								if(!UtilidadTexto.isEmpty(dtoFiltro.getArchivoAD().getFileName())&&dtoFiltro.getArchivoAD().getFileName().contains("."))
									if(dtoFiltro.getArchivoAD().getFileName().substring(0, dtoFiltro.getArchivoAD().getFileName().indexOf(".")).equals(campos[2].trim()))
										if(this.dtoResultadoProcesarRipsEntSub.getRegistrosArchivoAD()!=Utilidades.convertirAEntero(campos[3])){
											this.generarInconsistenciaCampos(ConstantesBD.ripsCT, ConstantesIntegridadDominio.acronimoCampoNoValido, "Total de registros",contador);
											errorvalidaciones=true;
										}
										
								if(!UtilidadTexto.isEmpty(dtoFiltro.getArchivoAC().getFileName())&&dtoFiltro.getArchivoAC().getFileName().contains("."))
									if(dtoFiltro.getArchivoAC().getFileName().substring(0, dtoFiltro.getArchivoAC().getFileName().indexOf(".")).equals(campos[2].trim()))
										if(this.dtoResultadoProcesarRipsEntSub.getRegistrosArchivoAC()!=Utilidades.convertirAEntero(campos[3])){
											errorvalidaciones=true;
											this.generarInconsistenciaCampos(ConstantesBD.ripsCT, ConstantesIntegridadDominio.acronimoCampoNoValido, "Total de registros",contador);
										}
										
										
								if(!UtilidadTexto.isEmpty(dtoFiltro.getArchivoAP().getFileName())&&dtoFiltro.getArchivoAP().getFileName().contains("."))
									if(dtoFiltro.getArchivoAP().getFileName().substring(0, dtoFiltro.getArchivoAP().getFileName().indexOf(".")).equals(campos[2].trim()))
										if(this.dtoResultadoProcesarRipsEntSub.getRegistrosArchivoAP()!=Utilidades.convertirAEntero(campos[3])){
											this.generarInconsistenciaCampos(ConstantesBD.ripsCT, ConstantesIntegridadDominio.acronimoCampoNoValido, "Total de registros",contador);
											errorvalidaciones=true;
										}
								
								/*if(!UtilidadTexto.isEmpty(dtoFiltro.getArchivoAH().getFileName())&&dtoFiltro.getArchivoAH().getFileName().contains("."))
									if(dtoFiltro.getArchivoAH().getFileName().substring(0, dtoFiltro.getArchivoAH().getFileName().indexOf(".")).equals(campos[2].trim()))
										if(this.dtoResultadoProcesarRipsEntSub.getRegistrosArchivoAH()!=Utilidades.convertirAEntero(campos[3])){
											this.generarInconsistenciaCampos(ConstantesBD.ripsCT, ConstantesIntegridadDominio.acronimoCampoNoValido, "Total de registros",contador);
											errorvalidaciones=true;
										}*/
										
								/*if(!UtilidadTexto.isEmpty(dtoFiltro.getArchivoAU().getFileName())&&dtoFiltro.getArchivoAU().getFileName().contains("."))
									if(dtoFiltro.getArchivoAU().getFileName().substring(0, dtoFiltro.getArchivoAU().getFileName().indexOf(".")).equals(campos[2].trim()))
										if(this.dtoResultadoProcesarRipsEntSub.getRegistrosArchivoAU()!=Utilidades.convertirAEntero(campos[3])){
											this.generarInconsistenciaCampos(ConstantesBD.ripsCT, ConstantesIntegridadDominio.acronimoCampoNoValido, "Total de registros",contador);
											errorvalidaciones=true;
										}*/
										
								if(!UtilidadTexto.isEmpty(dtoFiltro.getArchivoAM().getFileName())&&dtoFiltro.getArchivoAM().getFileName().contains("."))
									if(dtoFiltro.getArchivoAM().getFileName().substring(0, dtoFiltro.getArchivoAM().getFileName().indexOf(".")).equals(campos[2].trim()))
										if(this.dtoResultadoProcesarRipsEntSub.getRegistrosArchivoAM()!=Utilidades.convertirAEntero(campos[3])){
											this.generarInconsistenciaCampos(ConstantesBD.ripsCT, ConstantesIntegridadDominio.acronimoCampoNoValido, "Total de registros",contador);
											errorvalidaciones=true;
										}
										
								if(!UtilidadTexto.isEmpty(dtoFiltro.getArchivoAT().getFileName())&&dtoFiltro.getArchivoAT().getFileName().contains("."))
									if(dtoFiltro.getArchivoAT().getFileName().substring(0, dtoFiltro.getArchivoAT().getFileName().indexOf(".")).equals(campos[2].trim()))
										if(this.dtoResultadoProcesarRipsEntSub.getRegistrosArchivoAT()!=Utilidades.convertirAEntero(campos[3])){
											this.generarInconsistenciaCampos(ConstantesBD.ripsCT, ConstantesIntegridadDominio.acronimoCampoNoValido, "Total de registros",contador);
											errorvalidaciones=true;
										}
							
						}
						if(errorvalidaciones)
							error=true;
						
						
						
						
					}
					
				}
					
				cadena=buffer.readLine();
				
			}
			
			
			//***************CERRAR ARCHIVO****************************
			buffer.close();
			
		}
		catch(FileNotFoundException e)
		{
			Log4JManager.error("No se pudo encontrar el archivo "+dtoFiltro.getArchivoCT().getFileName()+" al cargarlo: "+e);
			
		}
		catch(IOException e)
		{
			Log4JManager.error("Error en los streams del archivo "+dtoFiltro.getArchivoCT().getFileName()+" al cargarlo: "+e);
		}
		this.dtoResultadoProcesarRipsEntSub.setRegistrosArchivoCT(contador);
		return error;
	}
	
	/**
	 * Método para realizar las validaciones del archivo AP
	 * @param dtoFiltro
	 */
	private boolean validacionesGeneralesAP(DtoFiltroProcesarRipsEntidadesSub dtoFiltro) 
	{
		int contador = 0;
		boolean error=false;
		try
		{
			String cadena="";
			String[] campos = new String[0];
			//******SE INICIALIZA ARCHIVO*************************
			BufferedReader buffer = new BufferedReader(new java.io.InputStreamReader(dtoFiltro.getArchivoAP().getInputStream()));
				
			//********SE RECORRE LÍNEA POR LÍNEA**************
			cadena=buffer.readLine();
			while(cadena!=null)
				{
					contador++;
					int errores=0;
					//Se toman los campos de cada línea del archivo
					if(cadena.endsWith(","))
						cadena+=" ";
					campos = cadena.split(",");
					
					//Validación de que todo esté separado por comas
					if(campos.length<15||campos.length>15||UtilidadCadena.numeroOcurrenciasCaracter(cadena, ',')!=14)
					{
						this.agregarValoresCampos(ConstantesBD.ripsAP, "", ConstantesCamposProcesoRipsEntidadesSub.tipoIdUsuario, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAP, "", ConstantesCamposProcesoRipsEntidadesSub.numeroIdUsuario, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAP, "", ConstantesCamposProcesoRipsEntidadesSub.fechaDeProcedimiento, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAP, "", ConstantesCamposProcesoRipsEntidadesSub.codigoDeProcedimiento, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAP, "", ConstantesCamposProcesoRipsEntidadesSub.numeroAutorizacion, contador);
						this.generarInconsistenciaCampos(ConstantesBD.ripsAP,ConstantesIntegridadDominio.acronimoCamposSeparacionComasErroneo,"",contador);
						error=true;
					}
					else
					{
					
						//ALMACENO LOS CAMPOS A MOSTRAR EN LA CONSULTA POR EL ARCHIVO AP
						this.agregarValoresCampos(ConstantesBD.ripsAP, campos[2], ConstantesCamposProcesoRipsEntidadesSub.tipoIdUsuario, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAP, campos[3], ConstantesCamposProcesoRipsEntidadesSub.numeroIdUsuario, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAP, campos[4], ConstantesCamposProcesoRipsEntidadesSub.fechaDeProcedimiento, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAP, campos[6], ConstantesCamposProcesoRipsEntidadesSub.codigoDeProcedimiento, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAP, campos[5], ConstantesCamposProcesoRipsEntidadesSub.numeroAutorizacion, contador);
						
						
						
						
						
						//VALORES PARA LA CONSULTA DEL PROCESO
						this.agregarValoresCampos(ConstantesBD.ripsAP, campos[14], ConstantesCamposProcesoRipsEntidadesSub.valorArchivosAutorizaciones, contador);
						
						
							//1) ******** NÚMERO DE FACTURA************************************
							if(validacionGeneralCampo(campos[0],"Número de factura",20,ConstantesBD.ripsAP,contador,false,false,false,false,true))
								errores++;
							//2) ******* CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD**********************
							if(validacionGeneralCampo(campos[1],"Código prestador de servicios salud",12,ConstantesBD.ripsAP,contador,false,false,false,false,true))
								errores++;
							//3) ******** TIPO DE IDENTIFICACION ********************************************
							if(validacionGeneralCampo(campos[2],"Tipo de identificación",2,ConstantesBD.ripsAP,contador,false,false,false,false,true))
								errores++;
							//4) ******** NÚMERO DE IDENTIFICACIÓN************************************
							if(validacionGeneralCampo(campos[3],"Número de identificación",20,ConstantesBD.ripsAP,contador,false,false,false,false,true))
								errores++;
							//5) ******** FECHA DEL PROCEDIMIENTO************************************
							if(validacionGeneralCampo(campos[4],"Fecha del procedimiento",10,ConstantesBD.ripsAP,contador,false,false,true,false,true))
								errores++;
							//6) ******** NÚMERO DE AUTORIZACION ************************************
							if(validacionGeneralCampo(campos[5],"Número de autorización",15,ConstantesBD.ripsAP,contador,false,false,false,false,true))
								errores++;
							//7) ******** CODIGO DEL PROCEDIMIENTO ***************************************
							if(validacionGeneralCampo(campos[6],"Código del procedimiento",8,ConstantesBD.ripsAP,contador,false,false,false,false,true))
								errores++;
							//8) ******** AMBITO DE REALIZACION ********************************************
							if(validacionGeneralCampo(campos[7],"Ámbito de realización",1,ConstantesBD.ripsAP,contador,false,false,false,false,true))
								errores++;
							//9) ******** FINALIDAD DEL PROCEDIMIENTO ***************************************
							if(validacionGeneralCampo(campos[8],"Finalidad del procedimiento",1,ConstantesBD.ripsAP,contador,false,false,false,false,true))
								errores++;
							//10) ******** PERSONAL QUE ATIENDE ***************************************
							if(validacionGeneralCampo(campos[9],"Personal que atiende",1,ConstantesBD.ripsAP,contador,false,false,false,false,false))
								errores++;
							//11) ******** CODIGO DIAGNOSTICO PRINCIPAL ***************************************
							if(validacionGeneralCampo(campos[10],"Código diagnóstico principal",4,ConstantesBD.ripsAP,contador,false,false,false,false,false))
								errores++;
							//12) ******** CÓDIGO DIAGNÓSTICO RELACIONADO ***************************************
							if(validacionGeneralCampo(campos[11],"Código diagnóstico relacionado",4,ConstantesBD.ripsAP,contador,false,false,false,false,false))
								errores++;							
							//13) ******** DIAGNÓSTICO COMPLICACION ***************************************
							if(validacionGeneralCampo(campos[12],"Diagnóstico complicación",4,ConstantesBD.ripsAP,contador,false,false,false,false,false))
								errores++;							
							//14) ******** FORMA DE REALIZACIÓN ***************************************
							if(validacionGeneralCampo(campos[13],"Forma de realización",1,ConstantesBD.ripsAP,contador,false,false,false,false,false))
								errores++;
							//15) ******** VALOR NETO A PAGAR ***************************************
							if(validacionGeneralCampo(campos[14],"Valor procedimiento",15,ConstantesBD.ripsAP,contador,true,true,false,false,true))
								errores++;
							
							
							
							//SI NO SE PRESENTARON ERRORES SE CONTINUA CON LAS VALIDACIONES, SI HAY ERRORES SE CANCELA EL PROCESO
							boolean errorvalidaciones=false;
							if(errores>0){
								error=true;
							}else{
								
								//1) ******** NÚMERO DE FACTURA************************************
								if(!numerosFacturaArchivoAF.contains(campos[0])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAP, ConstantesIntegridadDominio.acronimoNumeroFacturaNoExisteEnAF, "Número de la factura",contador);
									errorvalidaciones=true;
								}
								
								//2) ******* CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD**********************
								//Validar que este código corresponda al código Minsalud parametrizado en el sistema para la entidad subcontratada en la funcionalidad de  'lectura de planos pacientes entidades subcontratadas'.
								/*EntidadesSubcontratadas entidadSub=servicioEntidadesSub.obtenerEntidadesSubcontratadasporId(dtoFiltro.getCodigoPkEntidadSub());
								if(!entidadSub.getCodigoMinsalud().equals(campos[1]))
								{
									this.generarInconsistenciaCampos(ConstantesBD.ripsAP, ConstantesIntegridadDominio.acronimoCampoNoValido, "Código del prestador de servicios de salud",contador);
									errorvalidaciones=true;
								}*/
								
								//3) ******** TIPO DE IDENTIFICACION ********************************************
								//Se verifica que el tipo de identificacion sea válido
								if(!this.tiposIdentificacion.containsValue(campos[2])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAP, ConstantesIntegridadDominio.acronimoCampoNoValido, "Tipo de identificación del usuario",contador);
									errorvalidaciones=true;
								}
									
								//5) ******** FECHA DEL PROCEDIMIENTO************************************
								//Validar que sea < = a la fecha del sistema.
								String fechaProcedimiento = UtilidadFecha.conversionFormatoFechaAAp(campos[4]);
								String fechaActual = UtilidadFecha.conversionFormatoFechaAAp(Calendar.getInstance().getTime());
								if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaProcedimiento, fechaActual)){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAP, ConstantesIntegridadDominio.acronimoCampoNoValido,"Fecha del procedimiento",contador);
									errorvalidaciones=true;
								}
								
								//7) ******** CODIGO DEL PROCEDIMIENTO ***************************************
								if(!this.codigosPropietariosServicioValidos.contains(campos[6])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAP, ConstantesIntegridadDominio.acronimoCampoNoValido,"Código del procedimiento",contador);
									errorvalidaciones=true;
								}
							
								//8) ******** AMBITO DE REALIZACION ********************************************
								//Se verifica que el ámbito de realización sea válido
								if(!campos[7].trim().equals("")&&!campos[7].equals(ConstantesBD.codigoViaIngresoHospitalizacion+"")
										&&!campos[7].equals(ConstantesBD.codigoViaIngresoAmbulatorios+"")
										&&!campos[7].equals(ConstantesBD.codigoViaIngresoUrgencias+"")){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAP, ConstantesIntegridadDominio.acronimoCampoNoValido, "Ámbito de realización del procedimiento",contador);
									errorvalidaciones=true;
								}
									
								//9) ******** FINALIDAD DEL PROCEDIMIENTO ***************************************
								//Se verifica que en el caso que exista finalidad del procedimiento tenga el código válido:
								if(!dtoFiltro.getFinalidadesServicioEnSistema().contains(Utilidades.convertirAEntero(campos[8]))){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAP, ConstantesIntegridadDominio.acronimoCampoNoValido, "Finalidad del procedimiento",contador);
									errorvalidaciones=true;
								}
									
								//10) ******** PERSONAL QUE ATIENDE ***************************************
								//Este campo debe contener uno de los siguientes valores: 1 =	Médico (a) especialista,
								//2 =	Médico (a) general, 3 =	Enfermera (o), 4 =	Auxiliar de enfermería, 5 =	Otro
								if(!campos[9].trim().equals("")&&!campos[9].trim().equals("1")&&!campos[9].trim().equals("2")&&!campos[9].trim().equals("3")&&
									!campos[9].trim().equals("4")&&!campos[9].trim().equals("5")&&!campos[9].trim().equals("")
									){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAP, ConstantesIntegridadDominio.acronimoCampoNoValido, "Personal que atiende",contador);
									errorvalidaciones=true;
								}
									
								//11) ******** CODIGO DIAGNOSTICO PRINCIPAL ***************************************
								if(!UtilidadTexto.isEmpty(campos[10])&&!dtoFiltro.getDiagnosticosEnSistema().contains(campos[10])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAP, ConstantesIntegridadDominio.acronimoCampoNoValido, "Diagnóstico principal",contador);
									errorvalidaciones=true;
								}
								
								//12) ******** CÓDIGO DIAGNÓSTICO RELACIONADO ***************************************
								if(!UtilidadTexto.isEmpty(campos[11])&&!dtoFiltro.getDiagnosticosEnSistema().contains(campos[11])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAP, ConstantesIntegridadDominio.acronimoCampoNoValido, "Diagnóstico relacionado",contador);
									errorvalidaciones=true;
								}
								
								//13) ******** DIAGNÓSTICO COMPLICACION ***************************************
								if(!UtilidadTexto.isEmpty(campos[12])&&!dtoFiltro.getDiagnosticosEnSistema().contains(campos[12])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAP, ConstantesIntegridadDominio.acronimoCampoNoValido, "Complicación",contador);
									errorvalidaciones=true;
								}
								
								//14) ******** FORMA DE REALIZACIÓN ***************************************
								//Este campo debe contener uno de los siguientes valores: 1 = Unico o unilateral,	2 = Múltiple o bilateral, misma vía, diferente especialidad
								//3 = Múltiple o bilateral, misma vía, igual especialidad,	4 = Múltiple o bilateral, diferente vía, diferente especialidad
								//5 = Múltiple o bilateral, diferente vía, igual especialidad
								if(!campos[13].trim().equals("")&&!campos[13].trim().equals("1")&&!campos[13].trim().equals("2")&&!campos[13].trim().equals("3")&&
									!campos[13].trim().equals("4")&&!campos[13].trim().equals("5")&&!campos[13].trim().equals("")
									){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAP, ConstantesIntegridadDominio.acronimoCampoNoValido, "Forma de realización del acto quirúrgico",contador);
									errorvalidaciones=true;
								}
									
								if(errorvalidaciones)
									error=true;
								else
									this.registrosSinInconsistenciaAP.add(contador);
								
								
							}
							
					}  //fin else IF 
						
					cadena=buffer.readLine(); //siguiente registro
					
				} //Fin While
				
				
				//***************CERRAR ARCHIVO****************************
				buffer.close();
				
			}
			catch(FileNotFoundException e)
			{
				Log4JManager.error("No se pudo encontrar el archivo "+dtoFiltro.getArchivoAP().getFileName()+" al cargarlo: "+e);
			}
			catch(IOException e)
			{
				Log4JManager.error("Error en los streams del archivo "+dtoFiltro.getArchivoAP().getFileName()+" al cargarlo: "+e);
			}
		
			this.dtoResultadoProcesarRipsEntSub.setRegistrosArchivoAP(contador);
		return error;
	}
	
	/**
	 * Método para realizar las validaciones del archivo AM
	 * @param pos
	 */
	private boolean validacionesGeneralesAM(DtoFiltroProcesarRipsEntidadesSub dtoFiltro) 
	{
		int contador = 0;
		boolean error=false;
		try
		{
			String cadena="";
			
			String[] campos = new String[0];
			//******SE INICIALIZA ARCHIVO*************************
			BufferedReader buffer = new BufferedReader(new java.io.InputStreamReader(dtoFiltro.getArchivoAM().getInputStream()));
			
			//********SE RECORRE LÍNEA POR LÍNEA**************
			cadena=buffer.readLine();
			while(cadena!=null)
			{
					contador++;
					int errores=0;
					//Se toman los campos de cada línea del archivo
					if(cadena.endsWith(","))
						cadena+=" ";
					campos = cadena.split(",");
					
					//Validación de que todo esté separado por comas
					if(campos.length<14||campos.length>14||UtilidadCadena.numeroOcurrenciasCaracter(cadena, ',')!=13)
					{
						//ALMACENO LOS CAMPOS A MOSTRAR EN LA CONSULTA POR EL ARCHIVO AM
						this.agregarValoresCampos(ConstantesBD.ripsAM, "", ConstantesCamposProcesoRipsEntidadesSub.tipoIdUsuario, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAM, "", ConstantesCamposProcesoRipsEntidadesSub.numeroIdUsuario, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAM, "", ConstantesCamposProcesoRipsEntidadesSub.tipoDeMedicamento, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAM, "", ConstantesCamposProcesoRipsEntidadesSub.codigoDeMedicamento, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAM, "", ConstantesCamposProcesoRipsEntidadesSub.numeroAutorizacion, contador);
						this.generarInconsistenciaCampos(ConstantesBD.ripsAM, ConstantesIntegridadDominio.acronimoCamposSeparacionComasErroneo, "",contador);
						error=true;
					}
					else
					{
						
						//ALMACENO LOS CAMPOS A MOSTRAR EN LA CONSULTA POR EL ARCHIVO AM
						this.agregarValoresCampos(ConstantesBD.ripsAM, campos[2], ConstantesCamposProcesoRipsEntidadesSub.tipoIdUsuario, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAM, campos[3], ConstantesCamposProcesoRipsEntidadesSub.numeroIdUsuario, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAM, campos[6], ConstantesCamposProcesoRipsEntidadesSub.tipoDeMedicamento, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAM, campos[5], ConstantesCamposProcesoRipsEntidadesSub.codigoDeMedicamento, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAM, campos[4], ConstantesCamposProcesoRipsEntidadesSub.numeroAutorizacion, contador);
						
						//VALORES PARA LA CONSULTA DEL PROCESO
						this.agregarValoresCampos(ConstantesBD.ripsAM, campos[13], ConstantesCamposProcesoRipsEntidadesSub.valorArchivosAutorizaciones, contador);
						
						
							//1) ******** NÚMERO DE FACTURA************************************
							if(validacionGeneralCampo(campos[0],"Número de factura",20,ConstantesBD.ripsAM,contador,false,false,false,false,true))
								errores++;
							//2) ******* CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD**********************
							if(validacionGeneralCampo(campos[1],"Código prestador de servicios salud",12,ConstantesBD.ripsAM,contador,false,false,false,false,true))
								errores++;
							//3) ******** TIPO DE IDENTIFICACION ********************************************
							if(validacionGeneralCampo(campos[2],"Tipo de identificación",2,ConstantesBD.ripsAM,contador,false,false,false,false,true))
								errores++;
							//4) ******** NÚMERO DE IDENTIFICACIÓN************************************
							if(validacionGeneralCampo(campos[3],"Número de identificación",20,ConstantesBD.ripsAM,contador,false,false,false,false,true))
								errores++;
							//5) ******** NÚMERO DE AUTORIZACION ************************************
							if(validacionGeneralCampo(campos[4],"Número de autorización",15,ConstantesBD.ripsAM,contador,false,false,false,false,true))
								errores++;
							//6) ******** CODIGO DEL MEDICAMENTO ***************************************
							if(validacionGeneralCampo(campos[5],"Código del medicamento",20,ConstantesBD.ripsAM,contador,false,false,false,false,true))
								errores++;
							//7) ******** TIPO DEL MEDICAMENTO ***************************************
							if(validacionGeneralCampo(campos[6],"Tipo del medicamento",1,ConstantesBD.ripsAM,contador,false,false,false,false,true))
								errores++;
							//8) ******** NOMBRE GENÉRICO DEL MEDICAMENTO ***************************************
							if(validacionGeneralCampo(campos[7],"Nombre del medicamento",30,ConstantesBD.ripsAM,contador,false,false,false,false,false))
								errores++;
							//9) ******** FORMA FARMACEUTICA ***************************************
							if(validacionGeneralCampo(campos[8],"Forma farmacéutica",20,ConstantesBD.ripsAM,contador,false,false,false,false,false))
								errores++;
							//10) ******** CONCENTRACIÓN DEL MEDICAMENTO ***************************************
							if(validacionGeneralCampo(campos[9],"Concentración del medicamento",20,ConstantesBD.ripsAM,contador,false,false,false,false,false))
								errores++;
							//11) ******** UNIDAD DE MEDIDA DEL MEDICAMENTO ***************************************
							if(validacionGeneralCampo(campos[10],"Unidad de medida",20,ConstantesBD.ripsAM,contador,false,false,false,false,false))
								errores++;
							//12) ******** NÚMERO DE UNIDADES ***************************************
							if(validacionGeneralCampo(campos[11],"Número de unidades",5,ConstantesBD.ripsAM,contador,true,false,false,false,true))
								errores++;
							//13) ******** VALOR UNITARIO ***************************************
							if(validacionGeneralCampo(campos[12],"Valor unitario",15,ConstantesBD.ripsAM,contador,true,true,false,false,true))
								errores++;
							//17) ******** VALOR TOTAL ***************************************
							if(validacionGeneralCampo(campos[13],"Valor total",15,ConstantesBD.ripsAM,contador,true,true,false,false,true))
								errores++;
							
							
							//SI NO SE PRESENTARON ERRORES SE CONTINUA CON LAS VALIDACIONES, SI HAY ERRORES SE CANCELA EL PROCESO
							boolean errorvalidaciones=false;
							if(errores>0){
								error=true;
							}else{
							
								//1) ******** NÚMERO DE FACTURA************************************
								if(!numerosFacturaArchivoAF.contains(campos[0])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAM, ConstantesIntegridadDominio.acronimoNumeroFacturaNoExisteEnAF, "Número de la factura",contador);
									errorvalidaciones=true;
								}
							
								//2) ******* CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD**********************
								//Validar que este código corresponda al código Minsalud parametrizado en el sistema para la entidad subcontratada en la funcionalidad de  'lectura de planos pacientes entidades subcontratadas'.
								/*EntidadesSubcontratadas entidadSub=servicioEntidadesSub.obtenerEntidadesSubcontratadasporId(dtoFiltro.getCodigoPkEntidadSub());
								if(!entidadSub.getCodigoMinsalud().equals(campos[1]))
								{
									this.generarInconsistenciaCampos(ConstantesBD.ripsAM, ConstantesIntegridadDominio.acronimoCampoNoValido, "Código del prestador de servicios de salud",contador);
									errorvalidaciones=true;
								}*/
								
								//3) ******** TIPO DE IDENTIFICACION ********************************************
								//Se verifica que el tipo de identificacion sea válido
								if(!this.tiposIdentificacion.containsValue(campos[2])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAM, ConstantesIntegridadDominio.acronimoCampoNoValido, "Tipo de identificación del usuario",contador);
									errorvalidaciones=true;
								}
								
								//6) ******** CODIGO DEL MEDICAMENTO ***************************************
								//Validar que corresponda a un código válido de acuerdo al tipo de código de medicamentos e insumos seleccionado.
								if(!this.codigosArticulosPorTipoCodigoMedicInsum.contains(campos[5])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAM, ConstantesIntegridadDominio.acronimoCampoNoValido, "Código de medicamento",contador);
									errorvalidaciones=true;
								}
								
									
								//7) ******** TIPO DEL MEDICAMENTO ***************************************
								//Se verifica que el tipo de medicamento sea válido
								if(!campos[6].trim().equals("")&&!campos[6].equals(ConstantesBD.acronimoNaturalezaArticuloMedicamentoPos)&&!campos[6].equals(ConstantesBD.acronimoNaturalezaArticuloMedicamentoNoPos)){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAM, ConstantesIntegridadDominio.acronimoCampoNoValido, "Tipo de medicamento",contador);
									errorvalidaciones=true;
								}
								
								if(errorvalidaciones)
									error=true;
								else
									this.registrosSinInconsistenciaAM.add(contador);
							}
							
					}  //fin else IF 
						
					cadena=buffer.readLine(); //siguiente registro
					
				} //Fin While
				
				
				//***************CERRAR ARCHIVO****************************
				buffer.close();
				
					
			}
			catch(FileNotFoundException e)
			{
				Log4JManager.error("No se pudo encontrar el archivo "+dtoFiltro.getNombreArchivoAM()+" al cargarlo: "+e);
			}
			catch(IOException e)
			{
				Log4JManager.error("Error en los streams del archivo "+dtoFiltro.getNombreArchivoAM()+" al cargarlo: "+e);
			}
		
			this.dtoResultadoProcesarRipsEntSub.setRegistrosArchivoAM(contador);
		return error;
	}
	
	/**
	 * Método para realizar las validaciones del archivo AT
	 * @param pos
	 */
	private boolean validacionesGeneralesAT(DtoFiltroProcesarRipsEntidadesSub dtoFiltro) 
	{
		int contador = 0;
		boolean error=false;
		try
		{
			String cadena="";
			String[] campos = new String[0];
			//******SE INICIALIZA ARCHIVO*************************
			BufferedReader buffer = new BufferedReader(new java.io.InputStreamReader(dtoFiltro.getArchivoAT().getInputStream()));
			
			//********SE RECORRE LÍNEA POR LÍNEA**************
			cadena=buffer.readLine();
			while(cadena!=null)
			{
					contador++;
					int errores=0;
					//Se toman los campos de cada línea del archivo
					if(cadena.endsWith(","))
						cadena+=" ";
					campos = cadena.split(",");
					
					//Validación de que todo esté separado por comas
					if(campos.length<11||campos.length>11||UtilidadCadena.numeroOcurrenciasCaracter(cadena, ',')!=10)
					{
						//ALMACENO LOS CAMPOS A MOSTRAR EN LA CONSULTA POR EL validacionesGeneralesAMARCHIVO AT
						this.agregarValoresCampos(ConstantesBD.ripsAT, "", ConstantesCamposProcesoRipsEntidadesSub.tipoIdUsuario, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAT, "", ConstantesCamposProcesoRipsEntidadesSub.numeroIdUsuario, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAT, "", ConstantesCamposProcesoRipsEntidadesSub.tipoDeServicio, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAT, "", ConstantesCamposProcesoRipsEntidadesSub.codigoDeServicio, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAT, "", ConstantesCamposProcesoRipsEntidadesSub.numeroAutorizacion, contador);
						this.generarInconsistenciaCampos(ConstantesBD.ripsAT, ConstantesIntegridadDominio.acronimoCamposSeparacionComasErroneo, "",contador);
						error=true;
					}
					else
					{
						//ALMACENO LOS CAMPOS A MOSTRAR EN LA CONSULTA POR EL validacionesGeneralesAMARCHIVO AT
						this.agregarValoresCampos(ConstantesBD.ripsAT, campos[2], ConstantesCamposProcesoRipsEntidadesSub.tipoIdUsuario, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAT, campos[3], ConstantesCamposProcesoRipsEntidadesSub.numeroIdUsuario, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAT, campos[5], ConstantesCamposProcesoRipsEntidadesSub.tipoDeServicio, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAT, campos[6], ConstantesCamposProcesoRipsEntidadesSub.codigoDeServicio, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAT, campos[4], ConstantesCamposProcesoRipsEntidadesSub.numeroAutorizacion, contador);
						
						//VALORES PARA LA CONSULTA DEL PROCESO
						this.agregarValoresCampos(ConstantesBD.ripsAT, campos[10], ConstantesCamposProcesoRipsEntidadesSub.valorArchivosAutorizaciones, contador);
						
						
							//1) ******** NÚMERO DE FACTURA************************************
							if(validacionGeneralCampo(campos[0],"Número de factura",20,ConstantesBD.ripsAT,contador,false,false,false,false,true))
								errores++;
							//2) ******* CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD**********************
							if(validacionGeneralCampo(campos[1],"Código prestador de servicios salud",12,ConstantesBD.ripsAT,contador,false,false,false,false,true))
								errores++;
							//3) ******** TIPO DE IDENTIFICACION ********************************************
							if(validacionGeneralCampo(campos[2],"Tipo de identificación",2,ConstantesBD.ripsAT,contador,false,false,false,false,true))
								errores++;
							//4) ******** NÚMERO DE IDENTIFICACIÓN************************************
							if(validacionGeneralCampo(campos[3],"Número de identificación",20,ConstantesBD.ripsAT,contador,false,false,false,false,true))
								errores++;
							//5) ******** NÚMERO DE AUTORIZACIÓN ************************************
							if(validacionGeneralCampo(campos[4],"Número de autorización",15,ConstantesBD.ripsAT,contador,false,false,false,false,true))
								errores++;
							//6) ******** TIPO DE SERVICIO ***************************************
							if(validacionGeneralCampo(campos[5],"Tipo de servicio",1,ConstantesBD.ripsAT,contador,false,false,false,false,true))
								errores++;
							//7) ******** CÓDIGO DEL SERVICIO ***************************************
							if(validacionGeneralCampo(campos[6],"Código del servicio",20,ConstantesBD.ripsAT,contador,false,false,false,false,true))
								errores++;
							//8) ******** NOMBRE DEL SERVICIO ***************************************
							if(validacionGeneralCampo(campos[7],"Nombre del servicio",60,ConstantesBD.ripsAT,contador,false,false,false,false,false))
								errores++;
							//9) ******** CANTIDAD ***************************************
							if(validacionGeneralCampo(campos[8],"Cantidad",5,ConstantesBD.ripsAT,contador,true,false,false,false,true))
								errores++;
							//10) ******** VALOR UNITARIO ***************************************
							if(validacionGeneralCampo(campos[9],"Valor unitario",15,ConstantesBD.ripsAT,contador,true,true,false,false,true))
								errores++;
							//11) ******** VALOR TOTAL ***************************************
							if(validacionGeneralCampo(campos[10],"Valor total",15,ConstantesBD.ripsAT,contador,true,true,false,false,true))
								errores++;
							
							//SI NO SE PRESENTARON ERRORES SE CONTINUA CON LAS VALIDACIONES, SI HAY ERRORES SE CANCELA EL PROCESO
							boolean errorvalidaciones=false;
							if(errores>0){
								error=true;
							}else{
								
								//1) ******** NÚMERO DE FACTURA************************************
								if(!numerosFacturaArchivoAF.contains(campos[0])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAT, ConstantesIntegridadDominio.acronimoNumeroFacturaNoExisteEnAF, "Número de la factura",contador);
									errorvalidaciones=true;
								}
							
								//2) ******* CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD**********************
								//Validar que este código corresponda al código Minsalud parametrizado en el sistema para la entidad subcontratada en la funcionalidad de  'lectura de planos pacientes entidades subcontratadas'.
								/*EntidadesSubcontratadas entidadSub=servicioEntidadesSub.obtenerEntidadesSubcontratadasporId(dtoFiltro.getCodigoPkEntidadSub());
								if(!entidadSub.getCodigoMinsalud().equals(campos[1]))
								{
									this.generarInconsistenciaCampos(ConstantesBD.ripsAT, ConstantesIntegridadDominio.acronimoCampoNoValido, "Código del prestador de servicios de salud",contador);
									errorvalidaciones=true;
								}*/
								
								//3) ******** TIPO DE IDENTIFICACION ********************************************
								//Se verifica que sea valido el tipo de identificacion
								if(!this.tiposIdentificacion.containsValue(campos[2])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAT, ConstantesIntegridadDominio.acronimoCampoNoValido, "Tipo de identificación del usuario",contador);
									errorvalidaciones=true;
								}
								
								//6) ******** TIPO DE SERVICIO ***************************************
								//Se valida que el tipo de servicio sea correcto
								//1 = Materiales e insumos	2 = Traslados .	3 = Estancias	4 = Honorarios 
								if(!campos[5].trim().equals("")&&!campos[5].equals("1")&&!campos[5].equals("2")&&!campos[5].equals("3")&&!campos[5].equals("4")){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAT, ConstantesIntegridadDominio.acronimoCampoNoValido, "Tipo de servicio",contador);
									errorvalidaciones=true;
								}
									
								//7) ******** CÓDIGO DEL SERVICIO ***************************************
								//Si tipo de Servicio = 1 Materiales e Insumos, Validar que corresponda a un código 
								//válido de acuerdo al tipo de código de medicamentos e insumos seleccionado.
								if(!campos[5].trim().equals("")&&campos[5].equals("2")&&campos[5].equals("3")&&campos[5].equals("4")){
									if(!this.codigosPropietariosServicioValidos.contains(campos[6])){
										this.generarInconsistenciaCampos(ConstantesBD.ripsAT, ConstantesIntegridadDominio.acronimoCampoNoValido, "Código del servicio",contador);
										errorvalidaciones=true;
									}
								}else
									if(!campos[5].trim().equals("")&&campos[5].equals("1")){
										if(!this.codigosArticulosPorTipoCodigoMedicInsum.contains(campos[6])){
											this.generarInconsistenciaCampos(ConstantesBD.ripsAT, ConstantesIntegridadDominio.acronimoCampoNoValido, "Código de medicamento",contador);
											errorvalidaciones=true;
										}
									}
							
								if(errorvalidaciones)
									error=true;
								else
									this.registrosSinInconsistenciaAT.add(contador);
							}
							
						
					}  //fin else IF 
						
					cadena=buffer.readLine(); //siguiente registro
					
				} //Fin While
				
				
				//***************CERRAR ARCHIVO****************************
				buffer.close();
				
			}
			catch(FileNotFoundException e)
			{
				Log4JManager.error("No se pudo encontrar el archivo "+dtoFiltro.getNombreArchivoAT()+" al cargarlo: "+e);
			}
			catch(IOException e)
			{
				Log4JManager.error("Error en los streams del archivo "+dtoFiltro.getNombreArchivoAT()+" al cargarlo: "+e);
			}
			
			this.dtoResultadoProcesarRipsEntSub.setRegistrosArchivoAT(contador);
		return error;
		
	}
	
	/**
	 * Método para realizar las validaciones del archivo AF
	 * @param dtoFiltro
	 * @author Fabián Becerra
	 */
	private boolean validacionesGeneralesAF(DtoFiltroProcesarRipsEntidadesSub dtoFiltro) 
	{
		boolean error=false;
		int contador = 0;
		try
		{
			String cadena="";
			String[] campos = new String[0];
			BufferedReader buffer = new BufferedReader(new java.io.InputStreamReader(dtoFiltro.getArchivoAF().getInputStream()));
		
			//********SE RECORRE LÍNEA POR LÍNEA**************
			cadena=buffer.readLine();
			while(cadena!=null)
			{
				contador++;
				int errores=0;
				//Se toman los campos de cada línea del archivo
				if(cadena.endsWith(","))
					cadena+=" ";
				campos = cadena.split(",");
				
				//Validación de que todo esté separado por comas
				if(campos.length<17||campos.length>17||UtilidadCadena.numeroOcurrenciasCaracter(cadena, ',')!=16)
				{
					//ALMACENO LOS CAMPOS A MOSTRAR EN LA CONSULTA POR EL ARCHIVO AF
					this.agregarValoresCampos(ConstantesBD.ripsAF, "", ConstantesCamposProcesoRipsEntidadesSub.tipoIdUsuario, contador);
					this.agregarValoresCampos(ConstantesBD.ripsAF, "", ConstantesCamposProcesoRipsEntidadesSub.numeroIdUsuario, contador);
					this.agregarValoresCampos(ConstantesBD.ripsAF, "", ConstantesCamposProcesoRipsEntidadesSub.fechaExpedicionFactura, contador);
					this.agregarValoresCampos(ConstantesBD.ripsAF, "", ConstantesCamposProcesoRipsEntidadesSub.numeroDeFactura, contador);
					this.agregarValoresCampos(ConstantesBD.ripsAF, "", ConstantesCamposProcesoRipsEntidadesSub.numeroDeContrato, contador);
					this.generarInconsistenciaCampos(ConstantesBD.ripsAF, ConstantesIntegridadDominio.acronimoCamposSeparacionComasErroneo, "",contador);
					error=true;
				}
				else
				{
			
					//ALMACENO LOS CAMPOS A MOSTRAR EN LA CONSULTA POR EL ARCHIVO AF
					this.agregarValoresCampos(ConstantesBD.ripsAF, campos[2], ConstantesCamposProcesoRipsEntidadesSub.tipoIdUsuario, contador);
					this.agregarValoresCampos(ConstantesBD.ripsAF, campos[3], ConstantesCamposProcesoRipsEntidadesSub.numeroIdUsuario, contador);
					this.agregarValoresCampos(ConstantesBD.ripsAF, campos[5], ConstantesCamposProcesoRipsEntidadesSub.fechaExpedicionFactura, contador);
					this.agregarValoresCampos(ConstantesBD.ripsAF, campos[4], ConstantesCamposProcesoRipsEntidadesSub.numeroDeFactura, contador);
					this.agregarValoresCampos(ConstantesBD.ripsAF, campos[10], ConstantesCamposProcesoRipsEntidadesSub.numeroDeContrato, contador);
					
						//VALIDACIONES GENERALES EN LA INFO DE LOS CAMPOS DE LOS ARCHIVOS
					
						//1) ******* CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD**********************
						if(validacionGeneralCampo(campos[0],"Código prestador de servicios salud",12,ConstantesBD.ripsAF,contador,false,false,false,false,true))
							errores++;
						//2) ******** RAZÓN SOCIAL **********************************************
						if(validacionGeneralCampo(campos[1],"Razón social",60,ConstantesBD.ripsAF,contador,false,false,false,false,true))
							errores++;
						//3) ******** TIPO DE IDENTIFICACION ********************************************
						if(validacionGeneralCampo(campos[2],"Tipo de identificación",2,ConstantesBD.ripsAF,contador,false,false,false,false,true))
							errores++;
						//4) ******** NÚMERO DE IDENTIFICACIÓN************************************
						if(validacionGeneralCampo(campos[3],"Número de identificación",20,ConstantesBD.ripsAF,contador,false,false,false,false,true))
							errores++;
						//5) ******** NÚMERO DE FACTURA************************************
						if(validacionGeneralCampo(campos[4],"Número de factura",20,ConstantesBD.ripsAF,contador,false,false,false,false,true))
							errores++;
						else
							numerosFacturaArchivoAF.add(campos[4]);
						//6) ******** FECHA DE EXPEDICIÓN************************************
						if(validacionGeneralCampo(campos[5],"Fecha de expedición",10,ConstantesBD.ripsAF,contador,false,false,true,false,true))
							errores++;
						//7) ******** FECHA DE INICIO ***************************************
						if(validacionGeneralCampo(campos[6],"Fecha de inicio",10,ConstantesBD.ripsAF,contador,false,false,true,false,true))
							errores++;
						//8) ******** FECHA FINAL ***************************************
						if(validacionGeneralCampo(campos[7],"Fecha final",10,ConstantesBD.ripsAF,contador,false,false,true,false,true))
							errores++;
						//9) ******** CODIGO ENTIDAD ADMINISTRADORA ***************************************
						if(validacionGeneralCampo(campos[8],"Código entidad administradora",6,ConstantesBD.ripsAF,contador,false,false,false,false,true))
							errores++;	
						//10) ******** NOMBRE ENTIDAD ADMINISTRADORA ***************************************
						if(validacionGeneralCampo(campos[9],"Nombre entidad administradora",30,ConstantesBD.ripsAF,contador,false,false,false,false,true))
							errores++;
						//11) ******** NÚMERO DEL CONTRATO ***************************************
						if(validacionGeneralCampo(campos[10],"Número del contrato",15,ConstantesBD.ripsAF,contador,false,false,false,false,false))
							errores++;
						//12) ******** PLAN DE BENEFICIOS ***************************************
						if(validacionGeneralCampo(campos[11],"Plan de beneficios",30,ConstantesBD.ripsAF,contador,false,false,false,false,false))
							errores++;
						//13) ******** NÚMERO DE LA PÓLIZA ***************************************
						if(validacionGeneralCampo(campos[12],"Número de la póliza",10,ConstantesBD.ripsAF,contador,false,false,false,false,false))
							errores++;
						//14) ******** VALOR COPAGO ***************************************
						if(validacionGeneralCampo(campos[13],"Valor copago",15,ConstantesBD.ripsAF,contador,true,true,false,false,false))
							errores++;
						//15) ******** VALOR COMISIÓN ***************************************
						if(validacionGeneralCampo(campos[14],"Valor comisión",15,ConstantesBD.ripsAF,contador,true,true,false,false,false))
							errores++;
						//16) ******** VALOR DESCUENTOS ***************************************
						if(validacionGeneralCampo(campos[15],"Valor descuentos",15,ConstantesBD.ripsAF,contador,true,true,false,false,false))
							errores++;
						//17) ******** VALOR NETO A PAGAR ***************************************
						if(validacionGeneralCampo(campos[16],"Valor neto a pagar",15,ConstantesBD.ripsAF,contador,true,true,false,false,true))
							errores++;	
						
						
						//SI NO SE PRESENTARON ERRORES SE CONTINUA CON LAS VALIDACIONES, SI HAY ERRORES SE CANCELA EL PROCESO
						boolean errorvalidaciones=false;
						if(errores>0){
							error=true;
						}else{
							//1) ******* CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD**********************
							//Validar que este código corresponda al código Minsalud parametrizado en el sistema para la entidad subcontratada en la funcionalidad de  'lectura de planos pacientes entidades subcontratadas'.
							/*EntidadesSubcontratadas entidadSub=servicioEntidadesSub.obtenerEntidadesSubcontratadasporId(dtoFiltro.getCodigoPkEntidadSub());
							if(!entidadSub.getCodigoMinsalud().equals(campos[0]))
							{
								this.generarInconsistenciaCampos(ConstantesBD.ripsAF, ConstantesIntegridadDominio.acronimoCampoNoValido, "Código del prestador de servicios de salud",contador);
								errorvalidaciones=true;
							}*/
							if(campos[0].trim().length()>10)
								campos[0] = campos[0].substring(0, 10);
							
							//2) ******** RAZÓN SOCIAL **********************************************
							if(campos[1].trim().length()>60)
								campos[1] = campos[1].substring(0, 60);
							
							//3) ******** TIPO DE IDENTIFICACION ********************************************
							//Se verifica que el tipo de identificacion sea válido
							if(!this.tiposIdentificacion.containsValue(campos[2])){
								this.generarInconsistenciaCampos(ConstantesBD.ripsAF, ConstantesIntegridadDominio.acronimoCampoNoValido, "Tipo de identificación",contador);
								errorvalidaciones=true;
							}
							
							//4) ******** NÚMERO DE IDENTIFICACIÓN************************************
							//comparar que corresponda con el NIT del tercero de la entidad Subcontratada seleccionada
							ITercerosServicio servicioTerceros=FacturacionServicioFabrica.crearTercerosServicio();
							DtoTercero dtoTercero=servicioTerceros.obtenerTerceroXEntidadSub(dtoFiltro.getCodigoPkEntidadSub());
							if(dtoTercero.getNumeroIdentificacion().equals(campos[3])){
								this.generarInconsistenciaCampos(ConstantesBD.ripsAF, ConstantesIntegridadDominio.acronimoCampoNoValido, "Número de identificación",contador);
								errorvalidaciones=true;
							}
							if(campos[3].trim().length()>20)
								campos[3] = campos[3].substring(0, 20);
							
							//5) ******** NÚMERO DE FACTURA************************************
							if(campos[4].trim().length()>20)
								campos[4] = campos[4].substring(0, 20);
							
							//6) ******** FECHA DE EXPEDICIÓN************************************
							//Validar que la fecha sea < = a la fecha del sistema.
							String fechaExpedicionFactura = UtilidadFecha.conversionFormatoFechaAAp(campos[5]);
							String fechaActual = UtilidadFecha.conversionFormatoFechaAAp(Calendar.getInstance().getTime());
							if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaExpedicionFactura, fechaActual)){
								this.generarInconsistenciaCampos(ConstantesBD.ripsAF, ConstantesIntegridadDominio.acronimoCampoNoValido, "Fecha de expedición de la factura",contador);
								errorvalidaciones=true;
							}
							if(campos[5].trim().length()>10)
								campos[5] = campos[5].substring(0, 10);
							
							//7) ******** FECHA DE INICIO ***************************************
							//Validar que la fecha sea < = a la fecha del sistema.
							String fechaInicio = UtilidadFecha.conversionFormatoFechaAAp(campos[6]);
							fechaActual = UtilidadFecha.conversionFormatoFechaAAp(Calendar.getInstance().getTime());
							if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaInicio, fechaActual)){
								this.generarInconsistenciaCampos(ConstantesBD.ripsAF, ConstantesIntegridadDominio.acronimoCampoNoValido, "Fecha de inicio",contador);
								errorvalidaciones=true;
							}
							if(campos[6].trim().length()>10)
								campos[6] = campos[6].substring(0, 10);
							
							//8) ******** FECHA FINAL ***************************************
							//Validar que la fecha sea < = a la fecha del sistema.
							String fechaFinal = UtilidadFecha.conversionFormatoFechaAAp(campos[7]);
							fechaInicio = UtilidadFecha.conversionFormatoFechaAAp(campos[6]);
							fechaActual = UtilidadFecha.conversionFormatoFechaAAp(Calendar.getInstance().getTime());
							if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaFinal, fechaActual)){
								this.generarInconsistenciaCampos(ConstantesBD.ripsAF, ConstantesIntegridadDominio.acronimoCampoNoValido, "Fecha final",contador);
								errorvalidaciones=true;
							}else
							if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaInicio, fechaFinal)){
								this.generarInconsistenciaCampos(ConstantesBD.ripsAF, ConstantesIntegridadDominio.acronimoCampoNoValido, "Fecha final",contador);
								errorvalidaciones=true;
							}
							if(campos[7].trim().length()>10)
								campos[7] = campos[7].substring(0, 10);
							
							//9) ******** CODIGO ENTIDAD ADMINISTRADORA ***************************************
							if(campos[8].trim().length()>6)
								campos[8] = campos[8].substring(0, 6);
							
							//10) ******** NOMBRE ENTIDAD ADMINISTRADORA ***************************************
							if(campos[9].trim().length()>30)
								campos[9] = campos[9].substring(0, 30);
							
							//11) ******** NÚMERO DEL CONTRATO ***************************************
							if(campos[10].trim().length()>15)
								campos[10] = campos[10].substring(0, 15);
							
							//12) ******** PLAN DE BENEFICIOS ***************************************
							if(campos[11].trim().length()>30)
								campos[11] = campos[11].substring(0, 30);
							
							//13) ******** NÚMERO DE LA PÓLIZA ***************************************
							if(campos[12].trim().length()>10)
								campos[12] = campos[12].substring(0, 10);
							
							//14) ******** VALOR COPAGO ***************************************
							if(campos[13].trim().length()>15)
								campos[13] = campos[13].substring(0, 15);
							
							//15) ******** VALOR COMISIÓN ***************************************
							if(campos[14].trim().length()>15)
								campos[14] = campos[14].substring(0, 15);
							
							//16) ******** VALOR DESCUENTOS ***************************************
							if(campos[15].trim().length()>15)
								campos[15] = campos[15].substring(0, 15);
							
							//17) ******** VALOR NETO A PAGAR ***************************************
							if(campos[16].trim().length()>15)
								campos[16] = campos[16].substring(0, 15);
							
							
							//SUMATORIA Valor neto a pagar por la entidad contratante
							sumatoriaValorPagarEntidadContratArchivoAF=sumatoriaValorPagarEntidadContratArchivoAF+Utilidades.convertirADouble(campos[16]);
							
						}
						
						if(errorvalidaciones)
							error=true;
						
						
				}  //fin else IF 
				
				if(cadena!=null)
				cadena=buffer.readLine(); //siguiente registro
				
			} //Fin While
			
			
			//***************CERRAR ARCHIVO****************************
			buffer.close();
			
		}
		catch(FileNotFoundException e)
		{
			Log4JManager.error("No se pudo encontrar el archivo "+dtoFiltro.getArchivoAF().getFileName()+" al cargarlo: "+e);
		}
		catch(IOException e)
		{
			Log4JManager.error("Error en los streams del archivo "+dtoFiltro.getArchivoAF().getFileName()+" al cargarlo: "+e);
		}
		
		this.dtoResultadoProcesarRipsEntSub.setRegistrosArchivoAF(contador);
		
		return error;
		
	}
	
	/**
	 * Método para realizar las validaciones del archivo AH
	 * @param dtoFiltro
	 */
	/*private boolean validacionesGeneralesAH(DtoFiltroProcesarRipsEntidadesSub dtoFiltro) 
	{
		int contador = 0;
		boolean error=false;
		try
		{
			String cadena="";
			String[] campos = new String[0];
			
			//******SE INICIALIZA ARCHIVO*************************
			BufferedReader buffer = new BufferedReader(new java.io.InputStreamReader(dtoFiltro.getArchivoAF().getInputStream()));
				
			//********SE RECORRE LÍNEA POR LÍNEA**************
			cadena=buffer.readLine();
			while(cadena!=null)
			{
					contador++;
					int errores=0;
					//Se toman los campos de cada línea del archivo
					if(cadena.endsWith(","))
						cadena+=" ";
					campos = cadena.split(",");
					
					//Validación de que todo esté separado por comas
					if(campos.length<19||campos.length>19||UtilidadCadena.numeroOcurrenciasCaracter(cadena, ',')!=18)
					{
						this.generarInconsistenciaCampos(ConstantesBD.ripsAH,ConstantesIntegridadDominio.acronimoCamposSeparacionComasErroneo,"",contador);
						error=true;
					}
					else
					{
						//ALMACENO LOS CAMPOS A MOSTRAR EN LA CONSULTA POR EL ARCHIVO AH
						this.agregarValoresCampos(ConstantesBD.ripsAH, campos[7], ConstantesCamposProcesoRipsEntidadesSub.numeroAutorizacion, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAH, campos[4], ConstantesCamposProcesoRipsEntidadesSub.viaIngresoUsuarioInstitucion, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAH, campos[5], ConstantesCamposProcesoRipsEntidadesSub.fechaIngresoUsuarioInstitucion, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAH, campos[3], ConstantesCamposProcesoRipsEntidadesSub.numeroIdUsuario, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAH, campos[2], ConstantesCamposProcesoRipsEntidadesSub.tipoIdUsuario, contador);
						
							//1) ******** NÚMERO DE FACTURA************************************
							if(validacionGeneralCampo(campos[0],"número de factura",20,ConstantesBD.ripsAH,contador,false,false,false,false,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//2) ******* CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD**********************
							if(validacionGeneralCampo(campos[1],"código prestador de servicios salud",10,ConstantesBD.ripsAH,contador,false,false,false,false,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//3) ******** TIPO DE IDENTIFICACION ********************************************
							if(validacionGeneralCampo(campos[2],"tipo de identificación",2,ConstantesBD.ripsAH,contador,false,false,false,false,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//4) ******** NÚMERO DE IDENTIFICACIÓN************************************
							if(validacionGeneralCampo(campos[3],"número de identificación",20,ConstantesBD.ripsAH,contador,false,false,false,false,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//5) ******** VÍA DE INGRESO************************************
							if(validacionGeneralCampo(campos[4],"vía de ingreso",1,ConstantesBD.ripsAH,contador,false,false,false,false,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//6) ******** FECHA DE INGRESO********************************************************
							if(validacionGeneralCampo(campos[5],"fecha de ingreso",10,ConstantesBD.ripsAH,contador,false,false,true,false,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//7) ******** HORA DE INGRESO********************************************************
							if(validacionGeneralCampo(campos[6],"hora de ingreso",5,ConstantesBD.ripsAH,contador,false,false,false,true,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//8) ******** NÚMERO DE AUTORIZACION ************************************
							if(validacionGeneralCampo(campos[7],"número de autorización",15,ConstantesBD.ripsAH,contador,false,false,false,false,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//9) ******** CAUSA EXTERNA ***************************************
							if(validacionGeneralCampo(campos[8],"causa externa",2,ConstantesBD.ripsAH,contador,false,false,false,false,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//10) ******** DIAGNOSTICO PRINCIPAL INGRESO ***************************************
							if(validacionGeneralCampo(campos[9],"diagnóstico principal ingreso",4,ConstantesBD.ripsAH,contador,false,false,false,false,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//11) ******** DIAGNOSTICO PRINCIPAL EGRESO ***************************************
							if(validacionGeneralCampo(campos[10],"diagnóstico principal egreso",4,ConstantesBD.ripsAH,contador,false,false,false,false,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//12) ******** DIAGNÓSTICO RELACIONADO 1 EGRESO ***************************************
							if(validacionGeneralCampo(campos[11],"diagnóstico rel. 1 de egreso",4,ConstantesBD.ripsAH,contador,false,false,false,false,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//13) ******** DIAGNÓSTICO RELACIONADO 2 EGRESO ***************************************
							if(validacionGeneralCampo(campos[12],"diagnóstico rel. 2 de egreso",4,ConstantesBD.ripsAH,contador,false,false,false,false,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//14) ******** DIAGNÓSTICO RELACIONADO 3 EGRESO ***************************************
							if(validacionGeneralCampo(campos[13],"diagnóstico rel. 3 de egreso",4,ConstantesBD.ripsAH,contador,false,false,false,false,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//15) ******** DIAGNÓSTICO COMPLICACIÓN ***************************************
							if(validacionGeneralCampo(campos[14],"diagnóstico de complicación",4,ConstantesBD.ripsAH,contador,false,false,false,false,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//16) ******** ESTADO A LA SALIDA ********************************************
							if(validacionGeneralCampo(campos[15],"estado a la salida",1,ConstantesBD.ripsAH,contador,false,false,false,false,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//17) ******** DIAGNÓSTICO MUERTE ***************************************
							if(validacionGeneralCampo(campos[16],"diagnóstico de muerte",4,ConstantesBD.ripsAH,contador,false,false,false,false,campos[15].equals("2")?true:false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//18) ******** FECHA DE EGRESO********************************************************
							if(validacionGeneralCampo(campos[17],"fecha de egreso",10,ConstantesBD.ripsAH,contador,false,false,true,false,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//19) ******** HORA DE EGRESO********************************************************
							if(validacionGeneralCampo(campos[18],"hora de egreso",5,ConstantesBD.ripsAH,contador,false,false,false,true,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							
							
							//SI NO SE PRESENTARON ERRORES SE CONTINUA CON LAS VALIDACIONES, SI HAY ERRORES SE CANCELA EL PROCESO
							boolean errorvalidaciones=false;
							if(errores>0){
								cadena=null;
								error=true;
							}else{
								
								//1) ******** NÚMERO DE FACTURA************************************
								if(!numerosFacturaArchivoAF.contains(campos[0])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAH, ConstantesIntegridadDominio.acronimoCampoNoValido, "Número de la factura",contador);
									errorvalidaciones=true;
								}
								
								//2) ******* CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD**********************
								//Validar que este código corresponda al código Minsalud parametrizado en el sistema para la entidad subcontratada en la funcionalidad de  'lectura de planos pacientes entidades subcontratadas'.
								EntidadesSubcontratadas entidadSub=servicioEntidadesSub.obtenerEntidadesSubcontratadasporId(dtoFiltro.getCodigoPkEntidadSub());
								if(!entidadSub.getCodigoMinsalud().equals(campos[1]))
								{
									this.generarInconsistenciaCampos(ConstantesBD.ripsAH, ConstantesIntegridadDominio.acronimoCampoNoValido, "Tipo de identificación del usuario",contador);
									errorvalidaciones=true;
								}
								
								//3) ******** TIPO DE IDENTIFICACION ********************************************
								//Se verifica que el tipo de identificacion sea valido
								if(!this.tiposIdentificacion.containsValue(campos[2])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAH, ConstantesIntegridadDominio.acronimoCampoNoValido, "Tipo de identificación del usuario",contador);
									errorvalidaciones=true;
								}
									
								
								//5) ******** VÍA DE INGRESO************************************
								//Se verifica que la vía de ingreso sea válida
								if(!campos[4].trim().equals("")&&!campos[4].equals(ConstantesBD.codigoOrigenAdmisionHospitalariaEsUrgencias+"")
										&&!campos[4].equals(ConstantesBD.codigoOrigenAdmisionHospitalariaEsConsultaExterna+"")
										&&!campos[4].equals(ConstantesBD.codigoOrigenAdmisionHospitalariaEsRemitido+"")
										&&!campos[4].equals(ConstantesBD.codigoOrigenAdmisionHospitalariaEsNacidoInstitucion+"")){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAH, ConstantesIntegridadDominio.acronimoCampoNoValido, "Código del prestador de servicios de salud",contador);
									errorvalidaciones=true;
								}
								
								
								//9) ******** CAUSA EXTERNA ***************************************
								//Se verifica que la causa externa sea válida
								if(!dtoFiltro.getCausasExternasEnSistema().contains(Utilidades.convertirAEntero(campos[8]))){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAH, ConstantesIntegridadDominio.acronimoCampoNoValido, "Causa externa",contador);
									errorvalidaciones=true;
								}
									
								//10) ******** DIAGNOSTICO PRINCIPAL INGRESO ***************************************
								//Validar que corresponda a un Diagnóstico válido
								if(!UtilidadTexto.isEmpty(campos[9].trim())&&!this.diagnosticosEnSistema.contains(campos[9])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAH, ConstantesIntegridadDominio.acronimoCampoNoValido, "Diagnóstico principal de ingreso",contador);
								}
								
								//11) ******** DIAGNOSTICO PRINCIPAL EGRESO ***************************************
								//Validar que corresponda a un Diagnóstico válido
								if(!UtilidadTexto.isEmpty(campos[10].trim())&&!this.diagnosticosEnSistema.contains(campos[10])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAH, ConstantesIntegridadDominio.acronimoCampoNoValido, "Diagnóstico principal de  egreso",contador);
								}
								
								//12) ******** DIAGNÓSTICO RELACIONADO 1 EGRESO ***************************************
								//Validar que corresponda a un Diagnóstico válido
								if(!UtilidadTexto.isEmpty(campos[11].trim())&&!this.diagnosticosEnSistema.contains(campos[11])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAH, ConstantesIntegridadDominio.acronimoCampoNoValido, "Diagnóstico relacionado No. 1, de egreso",contador);
								}
								
								//13) ******** DIAGNÓSTICO RELACIONADO 2 EGRESO ***************************************
								//Validar que corresponda a un Diagnóstico válido
								if(!UtilidadTexto.isEmpty(campos[12].trim())&&!this.diagnosticosEnSistema.contains(campos[12])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAH, ConstantesIntegridadDominio.acronimoCampoNoValido, "Diagnóstico relacionado No. 2, de egreso",contador);
								}
								
								//14) ******** DIAGNÓSTICO RELACIONADO 3 EGRESO ***************************************
								//Validar que corresponda a un Diagnóstico válido
								if(!UtilidadTexto.isEmpty(campos[13].trim())&&!this.diagnosticosEnSistema.contains(campos[13])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAH, ConstantesIntegridadDominio.acronimoCampoNoValido, "Diagnóstico relacionado No. 3, de egreso",contador);
								}
								
								//15) ******** DIAGNÓSTICO COMPLICACIÓN ***************************************
								//Validar que corresponda a un Diagnóstico válido
								if(!UtilidadTexto.isEmpty(campos[14].trim())&&!this.diagnosticosEnSistema.contains(campos[14])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAH, ConstantesIntegridadDominio.acronimoCampoNoValido, "Diagnóstico de la complicación",contador);
								}
								
								//16) ******** ESTADO A LA SALIDA ********************************************
								//Validar 1 = Vivo (a) 2 = Muerto (a)
								if(!campos[15].trim().equals("")&&!campos[15].equals("1")&&!campos[15].equals("2")){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAH, ConstantesIntegridadDominio.acronimoCampoNoValido, "Estado a la salida",contador);
									errorvalidaciones=true;
								}
								
								//17) ******** DIAGNÓSTICO MUERTE ***************************************
								//Validar que corresponda a un Diagnóstico válido de SCSONE.
								if(!UtilidadTexto.isEmpty(campos[16].trim())&&!this.diagnosticosEnSistema.contains(campos[16])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAH, ConstantesIntegridadDominio.acronimoCampoNoValido, "Diagnóstico de la causa básica de muerte",contador);
									errorvalidaciones=true;
								}
								
								if(errorvalidaciones)
									error=true;
								
							
								
							}
					   
							
					}  //fin else IF 
						
					cadena=buffer.readLine(); //siguiente registro
					
				} //Fin While
				
				
				//***************CERRAR ARCHIVO****************************
				buffer.close();
				
					
			}
			catch(FileNotFoundException e)
			{
				Log4JManager.error("No se pudo encontrar el archivo "+dtoFiltro.getArchivoAH().getFileName()+" al cargarlo: "+e);
			}
			catch(IOException e)
			{
				Log4JManager.error("Error en los streams del archivo "+dtoFiltro.getArchivoAH().getFileName()+" al cargarlo: "+e);
			}
		
			this.dtoResultadoProcesarRipsEntSub.setRegistrosArchivoAH(contador);
			return error;
		
	}*/
	
	/**
	 * Método para realizar las validaciones del archivo AU
	 * @param pos
	 */
	/*private boolean validacionesGeneralesAU(DtoFiltroProcesarRipsEntidadesSub dtoFiltro) 
	{
		int contador = 0;
		boolean error=false;
		try
		{
			String cadena="";
			String[] campos = new String[0];
			//******SE INICIALIZA ARCHIVO*************************
			BufferedReader buffer = new BufferedReader(new java.io.InputStreamReader(dtoFiltro.getArchivoAU().getInputStream()));
				
			//********SE RECORRE LÍNEA POR LÍNEA**************
			cadena=buffer.readLine();
			while(cadena!=null)
				{
					contador++;
					int errores=0;
					//Se toman los campos de cada línea del archivo
					if(cadena.endsWith(","))
						cadena+=" ";
					campos = cadena.split(",");
					
					//Validación de que todo esté separado por comas
					if(campos.length<17||campos.length>17||UtilidadCadena.numeroOcurrenciasCaracter(cadena, ',')!=16)
					{
						this.generarInconsistenciaCampos(ConstantesBD.ripsAU, ConstantesIntegridadDominio.acronimoCamposSeparacionComasErroneo, "",contador);
						error=true;
					}
					else
					{
						//ALMACENO LOS CAMPOS A MOSTRAR EN LA CONSULTA POR EL ARCHIVO AU
						this.agregarValoresCampos(ConstantesBD.ripsAU, campos[6], ConstantesCamposProcesoRipsEntidadesSub.numeroAutorizacion, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAU, campos[4], ConstantesCamposProcesoRipsEntidadesSub.fechaIngresoUsuarioObservacion, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAU, campos[3], ConstantesCamposProcesoRipsEntidadesSub.numeroIdUsuario, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAU, campos[2], ConstantesCamposProcesoRipsEntidadesSub.tipoIdUsuario, contador);
						
						
							//1) ******** NÚMERO DE FACTURA************************************
							if(validacionGeneralCampo(campos[0],"número de factura",20,ConstantesBD.ripsAU,contador,false,false,false,false,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//2) ******* CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD**********************
							if(validacionGeneralCampo(campos[1],"código prestador de servicios salud",10,ConstantesBD.ripsAU,contador,false,false,false,false,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//3) ******** TIPO DE IDENTIFICACION ********************************************
							if(validacionGeneralCampo(campos[2],"tipo de identificación",2,ConstantesBD.ripsAU,contador,false,false,false,false,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//4) ******** NÚMERO DE IDENTIFICACIÓN************************************
							if(validacionGeneralCampo(campos[3],"número de identificación",20,ConstantesBD.ripsAU,contador,false,false,false,false,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//5) ******** FECHA DE INGRESO********************************************************
							if(validacionGeneralCampo(campos[4],"fecha de ingreso",10,ConstantesBD.ripsAU,contador,false,false,true,false,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//6) ******** HORA DE INGRESO********************************************************
							if(validacionGeneralCampo(campos[5],"hora de ingreso",5,ConstantesBD.ripsAU,contador,false,false,false,true,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//7) ******** NÚMERO DE AUTORIZACION ************************************
							if(validacionGeneralCampo(campos[6],"número de autorización",15,ConstantesBD.ripsAU,contador,false,false,false,false,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//8) ******** CAUSA EXTERNA ***************************************
							if(validacionGeneralCampo(campos[7],"causa externa",2,ConstantesBD.ripsAU,contador,false,false,false,false,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//9) ******** DIAGNOSTICO A LA SALIDA ***************************************
							if(validacionGeneralCampo(campos[8],"diagnóstico a la salida",4,ConstantesBD.ripsAU,contador,false,false,false,false,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//10) ******** DIAGNÓSTICO RELACIONADO 1 A LA SALIDA ***************************************
							if(validacionGeneralCampo(campos[9],"diagnóstico rel. 1 a la salida",4,ConstantesBD.ripsAU,contador,false,false,false,false,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//11) ******** DIAGNÓSTICO RELACIONADO 2 A LA SALIDA ***************************************
							if(validacionGeneralCampo(campos[10],"diagnóstico rel. 2 a la salida",4,ConstantesBD.ripsAU,contador,false,false,false,false,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//12) ******** DIAGNÓSTICO RELACIONADO 1 A LA SALIDA ***************************************
							if(validacionGeneralCampo(campos[11],"diagnóstico rel. 3 a la salida",4,ConstantesBD.ripsAU,contador,false,false,false,false,false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//13) ******** DESTINO A LA SALIDA ***************************************
							if(validacionGeneralCampo(campos[12],"destino a la salida",1,ConstantesBD.ripsAU,contador,false,false,false,false,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//14) ******** ESTADO A LA SALIDA ********************************************
							if(validacionGeneralCampo(campos[13],"estado a la salida",1,ConstantesBD.ripsAU,contador,false,false,false,false,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//15) ******** CAUSA BÁSICA DE MUERTE ***************************************
							if(validacionGeneralCampo(campos[14],"causa básica de muerte",4,ConstantesBD.ripsAU,contador,false,false,false,false,campos[13].equals("2")?true:false,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//16) ******** FECHA DE SALIDA********************************************************
							if(validacionGeneralCampo(campos[15],"fecha de salida",10,ConstantesBD.ripsAU,contador,false,false,true,false,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]))
								errores++;
							//17) ******** HORA DE SALIDA********************************************************
							if(validacionGeneralCampo(campos[16],"hora de salida",5,ConstantesBD.ripsAU,contador,false,false,false,true,true,ConstantesBD.acronimoSi,campos[0],campos[2],campos[3]));
								errores++;
								
							//SI NO SE PRESENTARON ERRORES SE CONTINUA CON LAS VALIDACIONES, SI HAY ERRORES SE CANCELA EL PROCESO
							boolean errorvalidaciones=false;
							if(errores>0){
								error=true;
							}else{
								
								//1) ******** NÚMERO DE FACTURA************************************
								if(!numerosFacturaArchivoAF.contains(campos[0])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAU, ConstantesIntegridadDominio.acronimoCampoNoValido, "Número de la factura",contador);
									errorvalidaciones=true;
								}
								
								//2) ******* CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD**********************
								//Validar que este código corresponda al código Minsalud parametrizado en el sistema para la entidad subcontratada en la funcionalidad de  'lectura de planos pacientes entidades subcontratadas'.
								EntidadesSubcontratadas entidadSub=servicioEntidadesSub.obtenerEntidadesSubcontratadasporId(dtoFiltro.getCodigoPkEntidadSub());
								if(!entidadSub.getCodigoMinsalud().equals(campos[1]))
								{
									this.generarInconsistenciaCampos(ConstantesBD.ripsAU, ConstantesIntegridadDominio.acronimoCampoNoValido, "Código del prestador de servicios de salud",contador);
									errorvalidaciones=true;
								}
								
								//3) ******** TIPO DE IDENTIFICACION ********************************************
								//Se verifica que el tipo de identificacion sea valido
								if(!this.tiposIdentificacion.containsValue(campos[2])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAU, ConstantesIntegridadDominio.acronimoCampoNoValido, "Tipo de identificación del usuario",contador);
									errorvalidaciones=true;
								}
								
								
								//8) ******** CAUSA EXTERNA ***************************************
								//Se verifica que la causa externa sea válida
								if(!dtoFiltro.getCausasExternasEnSistema().contains(Utilidades.convertirAEntero(campos[7]))){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAU, ConstantesIntegridadDominio.acronimoCampoNoValido, "Causa externa",contador);
									errorvalidaciones=true;
								}
								
								
								//9) ******** DIAGNOSTICO A LA SALIDA ***************************************
								if(!UtilidadTexto.isEmpty(campos[8].trim())&&!this.diagnosticosEnSistema.contains(campos[8])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAU, ConstantesIntegridadDominio.acronimoCampoNoValido, "Diagnóstico a la salida",contador);
									errorvalidaciones=true;
								}
								
								//10) ******** DIAGNÓSTICO RELACIONADO 1 A LA SALIDA ***************************************
								if(!UtilidadTexto.isEmpty(campos[9].trim())&&!this.diagnosticosEnSistema.contains(campos[9])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAU, ConstantesIntegridadDominio.acronimoCampoNoValido, "Diagnóstico relacionado No. 1, a la salida",contador);
									errorvalidaciones=true;
								}
								
								//11) ******** DIAGNÓSTICO RELACIONADO 2 A LA SALIDA ***************************************
								if(!UtilidadTexto.isEmpty(campos[10].trim())&&!this.diagnosticosEnSistema.contains(campos[10])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAU, ConstantesIntegridadDominio.acronimoCampoNoValido, "Diagnóstico relacionado Nro. 2, a la salida",contador);
								}
								
								//12) ******** DIAGNÓSTICO RELACIONADO 1 A LA SALIDA ***************************************
								if(!UtilidadTexto.isEmpty(campos[11].trim())&&!this.diagnosticosEnSistema.contains(campos[11])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAU, ConstantesIntegridadDominio.acronimoCampoNoValido, "Diagnóstico relacionado Nro. 3, a la salida",contador);
									errorvalidaciones=true;
								}
								
								//13) ******** DESTINO A LA SALIDA ***************************************
								//Este campo debe corresponder a uno de los siguientes: 1 = Alta de urgencias, 2 = Remisión a otro nivel de complejidad, 3 = Hospitalización
								if(!campos[12].trim().equals("")&&!campos[12].equals("1")&&!campos[12].equals("2")&&!campos[12].equals("3")){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAU, ConstantesIntegridadDominio.acronimoCampoNoValido, "Destino del usuario a la salida de observación",contador);
									errorvalidaciones=true;
								}
									
								//14) ******** ESTADO A LA SALIDA ********************************************
								//Se verifica que el estado a la salida de realización sea válido: 1 = Vivo, 2 = Muerto
								if(!campos[13].trim().equals("")&&!campos[13].equals("1")&&!campos[13].equals("2")){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAU, ConstantesIntegridadDominio.acronimoCampoNoValido, "Estado a la salida",contador);
									errorvalidaciones=true;
								}
								
								//15) ******** CAUSA BÁSICA DE MUERTE ***************************************
								if(!UtilidadTexto.isEmpty(campos[14].trim())&&!this.diagnosticosEnSistema.contains(campos[14])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAU, ConstantesIntegridadDominio.acronimoCampoNoValido, "Causa básica de muerte en urgencias",contador);
									errorvalidaciones=true;
								}
									
								
								if(errorvalidaciones)
									error=true;
								
							}
								
							
						
					}  //fin else IF 
						
					cadena=buffer.readLine(); //siguiente registro
					
				} //Fin While
				
				
				//***************CERRAR ARCHIVO****************************
				buffer.close();
				
			}
			catch(FileNotFoundException e)
			{
				Log4JManager.error("No se pudo encontrar el archivo "+dtoFiltro.getArchivoAU().getFileName()+" al cargarlo: "+e);
			}
			catch(IOException e)
			{
				Log4JManager.error("Error en los streams del archivo "+dtoFiltro.getArchivoAU().getFileName()+" al cargarlo: "+e);
			}
		
			this.dtoResultadoProcesarRipsEntSub.setRegistrosArchivoAU(contador);
		return error;
	}*/
	
	/**
	 * Método para realizar las validaciones del archivo AD
	 * @param pos
	 */
	private boolean validacionesGeneralesAD(DtoFiltroProcesarRipsEntidadesSub dtoFiltro) 
	{
	
		boolean error=false;
		int contador = 0;
		try
		{
			String cadena="";
			String[] campos = new String[0];
			//******SE INICIALIZA ARCHIVO*************************
			BufferedReader buffer = new BufferedReader(new java.io.InputStreamReader(dtoFiltro.getArchivoAD().getInputStream()));
			
			//********SE RECORRE LÍNEA POR LÍNEA**************
			cadena=buffer.readLine();
			while(cadena!=null)
			{
				contador++;
				int errores=0;
				//Se toman los campos de cada línea del archivo
				if(cadena.endsWith(","))
					cadena+=" ";
				campos = cadena.split(",");
				
				//Validación de que todo esté separado por comas
				if(campos.length<6||campos.length>6||UtilidadCadena.numeroOcurrenciasCaracter(cadena, ',')!=5)
				{
					//ALMACENO LOS CAMPOS A MOSTRAR EN LA CONSULTA POR EL ARCHIVO AD
					this.agregarValoresCampos(ConstantesBD.ripsAD, "", ConstantesCamposProcesoRipsEntidadesSub.codigoDeConcepto, contador);
					this.agregarValoresCampos(ConstantesBD.ripsAD, "", ConstantesCamposProcesoRipsEntidadesSub.cantidad, contador);
					this.generarInconsistenciaCampos(ConstantesBD.ripsAD, ConstantesIntegridadDominio.acronimoCamposSeparacionComasErroneo, "",contador);
					error=true;
				}
				else
				{
					
					//ALMACENO LOS CAMPOS A MOSTRAR EN LA CONSULTA POR EL ARCHIVO AD
					this.agregarValoresCampos(ConstantesBD.ripsAD, campos[2], ConstantesCamposProcesoRipsEntidadesSub.codigoDeConcepto, contador);
					this.agregarValoresCampos(ConstantesBD.ripsAD, campos[3], ConstantesCamposProcesoRipsEntidadesSub.cantidad, contador);
					
					
					
						//1) ******** NÚMERO DE FACTURA************************************
						if(validacionGeneralCampo(campos[0],"Número de factura",20,ConstantesBD.ripsAD,contador,false,false,false,false,true))
							errores++;
						//2) ******* CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD**********************
						if(validacionGeneralCampo(campos[1],"Código prestador de servicios salud",12,ConstantesBD.ripsAD,contador,false,false,false,false,true))
							errores++;
						//3) ******** CÓDIGO DEL CONCEPTO ********************************************
						if(validacionGeneralCampo(campos[2],"Código del concepto",2,ConstantesBD.ripsAD,contador,false,false,false,false,true))
							errores++;
						//4) ******** CANTIDAD************************************
						if(validacionGeneralCampo(campos[3],"Cantidad",15,ConstantesBD.ripsAD,contador,true,false,false,false,true))
							errores++;
						//5) ******** VALOR UNITARIO************************************
						if(validacionGeneralCampo(campos[4],"Valor unitario",15,ConstantesBD.ripsAD,contador,true,true,false,false,false))
							errores++;
						//6) ******** VALOR TOTAL************************************
						if(validacionGeneralCampo(campos[5],"Valor concepto",15,ConstantesBD.ripsAD,contador,true,true,false,false,true))
							errores++;
						
						//SI NO SE PRESENTARON ERRORES SE CONTINUA CON LAS VALIDACIONES, SI HAY ERRORES SE CANCELA EL PROCESO
						boolean errorvalidaciones=false;
						if(errores>0){
							error=true;
						}else{
							
							//1) ******** NÚMERO DE FACTURA************************************
							if(!numerosFacturaArchivoAF.contains(campos[0])){
								this.generarInconsistenciaCampos(ConstantesBD.ripsAD, ConstantesIntegridadDominio.acronimoNumeroFacturaNoExisteEnAF, "Número de la factura",contador);
								errorvalidaciones=true;
							}
							
							//2) ******* CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD**********************
							//Validar que este código corresponda al código Minsalud parametrizado en el sistema para la entidad subcontratada en la funcionalidad de  'lectura de planos pacientes entidades subcontratadas'.
							/*EntidadesSubcontratadas entidadSub=servicioEntidadesSub.obtenerEntidadesSubcontratadasporId(dtoFiltro.getCodigoPkEntidadSub());
							if(!entidadSub.getCodigoMinsalud().equals(campos[1]))
							{
								this.generarInconsistenciaCampos(ConstantesBD.ripsAD, ConstantesIntegridadDominio.acronimoCampoNoValido, "Código del prestador de servicios de salud",contador);
								errorvalidaciones=true;
							}*/
							
							//3) ******** CÓDIGO DEL CONCEPTO ********************************************
							//Este campo debe contener uno de los siguientes valores:01 =  Consultas,02 = Procedimientos de diagnósticos
							//03 = Procedimientos terapéuticos no quirúrgicos, 04 = Procedimientos terapéuticos quirúrgicos, 05 = Procedimientos de promoción y prevención
							//06 = Estancias, 07 = Honorarios, 08 = Derechos de sala, 09 = Materiales e insumos 10 = Banco de sangre, 11 = Prótesis y órtesis
							//12 = Medicamentos POS, 13 = Medicamentos no POS, 14 = Traslado de pacientes
							if(!campos[2].equals("")&&!campos[2].equals("01")&&!campos[2].equals("02")&&!campos[2].equals("03")&&!campos[2].equals("04")&&
								!campos[2].equals("05")&&!campos[2].equals("06")&&!campos[2].equals("07")&&!campos[2].equals("08")&&
								!campos[2].equals("09")&&!campos[2].equals("10")&&!campos[2].equals("11")&&!campos[2].equals("12")&&
								!campos[2].equals("13")&&!campos[2].equals("14")
								){
								this.generarInconsistenciaCampos(ConstantesBD.ripsAD, ConstantesIntegridadDominio.acronimoCampoNoValido, "Código del concepto",contador);
								errorvalidaciones=true;
							}
							
							sumatoriaValorTotalPorConceptoArchivoAD=sumatoriaValorTotalPorConceptoArchivoAD+Utilidades.convertirADouble(campos[5]);
								
							
							
						}
						
						if(errorvalidaciones)
							error=true;
				
				}  //fin else IF 
					
				cadena=buffer.readLine(); //siguiente registro
				
			} //Fin While
			
			
			//***************CERRAR ARCHIVO****************************
			buffer.close();
			
		}
		catch(FileNotFoundException e)
		{
			Log4JManager.error("No se pudo encontrar el archivo "+dtoFiltro.getArchivoAD().getFileName()+" al cargarlo: "+e);
		}
		catch(IOException e)
		{
			Log4JManager.error("Error en los streams del archivo "+dtoFiltro.getArchivoAD().getFileName()+" al cargarlo: "+e);
		}
		
		this.dtoResultadoProcesarRipsEntSub.setRegistrosArchivoAD(contador);
		
		return error;
		
	}
	
	/**
	 * Método para realizar las validaciones del archivo US
	 * @param pos
	 */
	private boolean validacionesGeneralesUS(DtoFiltroProcesarRipsEntidadesSub dtoFiltro) 
	{
		int contador = 0;
		boolean error=false;
		try
		{
			String cadena="";
			String[] campos = new String[0];
			//******SE INICIALIZA ARCHIVO*************************
			BufferedReader buffer = new BufferedReader(new java.io.InputStreamReader(dtoFiltro.getArchivoUS().getInputStream()));
			
			//********SE RECORRE LÍNEA POR LÍNEA**************
			cadena=buffer.readLine();
			while(cadena!=null)
			{
				contador++;
				int errores=0;
				
				//Se toman los campos de cada línea del archivo
				if(cadena.endsWith(","))
					cadena+=" ";
				campos = cadena.split(",");
				
				//Validación de que todo esté separado por comas
				if(campos.length<14||campos.length>14||UtilidadCadena.numeroOcurrenciasCaracter(cadena, ',')!=13)
				{
					//ALMACENO LOS CAMPOS A MOSTRAR EN LA CONSULTA POR EL ARCHIVO US
					this.agregarValoresCampos(ConstantesBD.ripsUS, "", ConstantesCamposProcesoRipsEntidadesSub.tipoIdUsuario, contador);
					this.agregarValoresCampos(ConstantesBD.ripsUS, "", ConstantesCamposProcesoRipsEntidadesSub.numeroIdUsuario, contador);
					this.agregarValoresCampos(ConstantesBD.ripsUS, "", ConstantesCamposProcesoRipsEntidadesSub.primerApellidoUsuario, contador);
					this.agregarValoresCampos(ConstantesBD.ripsUS, "", ConstantesCamposProcesoRipsEntidadesSub.segundoApellidoUsuario, contador);
					this.agregarValoresCampos(ConstantesBD.ripsUS, "", ConstantesCamposProcesoRipsEntidadesSub.primerNombreUsuario, contador);
					this.agregarValoresCampos(ConstantesBD.ripsUS, "", ConstantesCamposProcesoRipsEntidadesSub.segundoNombreUsuario, contador);
					this.agregarValoresCampos(ConstantesBD.ripsUS, "", ConstantesCamposProcesoRipsEntidadesSub.sexo, contador);
					this.generarInconsistenciaCampos(ConstantesBD.ripsUS, ConstantesIntegridadDominio.acronimoCamposSeparacionComasErroneo, "",contador);
					error=true;
				}
				else
				{
						//ALMACENO LOS CAMPOS A MOSTRAR EN LA CONSULTA POR EL ARCHIVO US
					this.agregarValoresCampos(ConstantesBD.ripsUS, campos[0], ConstantesCamposProcesoRipsEntidadesSub.tipoIdUsuario, contador);
					this.agregarValoresCampos(ConstantesBD.ripsUS, campos[1], ConstantesCamposProcesoRipsEntidadesSub.numeroIdUsuario, contador);
					this.agregarValoresCampos(ConstantesBD.ripsUS, campos[4], ConstantesCamposProcesoRipsEntidadesSub.primerApellidoUsuario, contador);
					this.agregarValoresCampos(ConstantesBD.ripsUS, campos[5], ConstantesCamposProcesoRipsEntidadesSub.segundoApellidoUsuario, contador);
					this.agregarValoresCampos(ConstantesBD.ripsUS, campos[6], ConstantesCamposProcesoRipsEntidadesSub.primerNombreUsuario, contador);
					this.agregarValoresCampos(ConstantesBD.ripsUS, campos[7], ConstantesCamposProcesoRipsEntidadesSub.segundoNombreUsuario, contador);
					this.agregarValoresCampos(ConstantesBD.ripsUS, campos[10], ConstantesCamposProcesoRipsEntidadesSub.sexo, contador);
					
					
						//1) ******** TIPO DE IDENTIFICACION ********************************************
						if(validacionGeneralCampo(campos[0],"Tipo de identificación",2,ConstantesBD.ripsUS,contador,false,false,false,false,true))
							errores++;
						//2) ******** NÚMERO DE IDENTIFICACIÓN************************************
						if(validacionGeneralCampo(campos[1],"Número de identificación",20,ConstantesBD.ripsUS,contador,false,false,false,false,true))
							errores++;
						//3) ******* CÓDIGO ENTIDAD ADMINISTRADORA **************************************
						if(validacionGeneralCampo(campos[2],"Código entidad administradora",6,ConstantesBD.ripsUS,contador,false,false,false,false,true))
							errores++;
						//4) ******* TIPO DE USUARIO **************************************
						if(validacionGeneralCampo(campos[3],"Tipo de usuario",1,ConstantesBD.ripsUS,contador,false,false,false,false,true))
							errores++;
						//5) ******* PRIMER APELLIDO USUARIO **************************************
						if(validacionGeneralCampo(campos[4],"Primer apellido usuario",30,ConstantesBD.ripsUS,contador,false,false,false,false,true))
							errores++;
						//6) ******* SEGUNDO APELLIDO USUARIO **************************************
						if(validacionGeneralCampo(campos[5],"Segundo apellido usuario",30,ConstantesBD.ripsUS,contador,false,false,false,false,false))
							errores++;
						//7) ******* PRIMER NOMBRE USUARIO **************************************
						if(validacionGeneralCampo(campos[6],"Primer nombre usuario",20,ConstantesBD.ripsUS,contador,false,false,false,false,true))
							errores++;
						//8) ******* SEGUNDO NOMBRE USUARIO **************************************
						if(validacionGeneralCampo(campos[7],"Segundo nombre usuario",20,ConstantesBD.ripsUS,contador,false,false,false,false,false))
							errores++;
						//9) ******* EDAD **************************************
						if(validacionGeneralCampo(campos[8],"Edad",3,ConstantesBD.ripsUS,contador,true,false,false,false,false))
							errores++;
						//10) ******* UNIDAD DE MEDIDA EDAD **************************************
						if(validacionGeneralCampo(campos[9],"Unidad de medida edad",1,ConstantesBD.ripsUS,contador,false,false,false,false,false))
							errores++;
						//11) ******* SEXO **************************************
						if(validacionGeneralCampo(campos[10],"Sexo",1,ConstantesBD.ripsUS,contador,false,false,false,false,true))
							errores++;
						//12) ******* CODIGO DEPTO RESIDENCIA **************************************
						if(validacionGeneralCampo(campos[11],"Código depto residencia",2,ConstantesBD.ripsUS,contador,false,false,false,false,false))
							errores++;
						//13) ******* CODIGO MUNICIPIO RESIDENCIA **************************************
						if(validacionGeneralCampo(campos[12],"Código municipio residencia",3,ConstantesBD.ripsUS,contador,false,false,false,false,false))
							errores++;
						//14) ******* ZONA DE RESIDENCIA **************************************
						if(validacionGeneralCampo(campos[13],"Zona de residencia",1,ConstantesBD.ripsUS,contador,false,false,false,false,false))
							errores++;
						
						//SI NO SE PRESENTARON ERRORES SE CONTINUA CON LAS VALIDACIONES, SI HAY ERRORES SE CANCELA EL PROCESO
						boolean errorvalidaciones=false;
						if(errores>0){
							error=true;
						}else{
							
							//1) ******** TIPO DE IDENTIFICACION ********************************************
							//Se verifica que el tipo de identificacion sea válido
							if(!this.tiposIdentificacion.containsValue(campos[0])){
								this.generarInconsistenciaCampos(ConstantesBD.ripsUS, ConstantesIntegridadDominio.acronimoCampoNoValido, "Tipo de identificación del usuario",contador);
								errorvalidaciones=true;
							}
							
							//4) ******* TIPO DE USUARIO **************************************
							//Se verifica que el tipo de usuario sea válido
							//1 = Contributivo,	2 = Subsidiado,3 = Vinculado,4 = Particular,5 = Otro
							if(!campos[3].trim().equals("")&&!campos[3].equals("1")&&!campos[3].equals("2")&&!campos[3].equals("3")&&!campos[3].equals("4")&&!campos[3].equals("5")&&!campos[3].equals("6")&&!campos[3].equals("7")&&!campos[3].equals("8"))
							{
								this.generarInconsistenciaCampos(ConstantesBD.ripsUS, ConstantesIntegridadDominio.acronimoCampoNoValido, "Tipo de usuario",contador);
									errorvalidaciones=true;
							}
							
							//10) ******* UNIDAD DE MEDIDA EDAD **************************************
							//Se verifica la unidad de medida de la edad si se ingresó sea válida
							//1 = Años. 2 = Meses. 3 = Días
							if(!campos[9].trim().equals("")&&!campos[9].equals("1")&&!campos[9].equals("2")&&!campos[9].equals("3"))
							{
									this.generarInconsistenciaCampos(ConstantesBD.ripsUS, ConstantesIntegridadDominio.acronimoCampoNoValido, "Unidad de medida de la edad",contador);
									errorvalidaciones=true;
							}
							
							//11) ******* SEXO **************************************
							//Se verifica que el sexo sea válido
							if(!campos[10].trim().equals("")&&!campos[10].equals(ConstantesIntegridadDominio.acronimoMasculino)&&!campos[10].equals(ConstantesIntegridadDominio.acronimoFemenino))
							{
								this.generarInconsistenciaCampos(ConstantesBD.ripsUS, ConstantesIntegridadDominio.acronimoCampoNoValido, "Sexo",contador);
									errorvalidaciones=true;
							}
							
							//14) ******* ZONA DE RESIDENCIA **************************************
							//Se verifica que la zona de residencia sea válida
							if(!campos[13].trim().equals("")&&!campos[13].equals(ConstantesIntegridadDominio.acronimoZonaUrbana)&&!campos[13].equals(ConstantesIntegridadDominio.acronimoZonaRural))
							{
								this.generarInconsistenciaCampos(ConstantesBD.ripsUS, ConstantesIntegridadDominio.acronimoCampoNoValido, "Zona de residencia habitual",contador);
								errorvalidaciones=true;
							}
						
						
						if(errorvalidaciones)
							error=true;
						else
							this.registrosSinInconsistenciaUS.add(contador);
							
							
						}
						
						
				}  //fin else IF 
					
				cadena=buffer.readLine(); //siguiente registro
				
			} //Fin While
			
			
			//***************CERRAR ARCHIVO****************************
			buffer.close();
			
			
			
		}
		catch(FileNotFoundException e)
		{
			Log4JManager.error("No se pudo encontrar el archivo "+dtoFiltro.getArchivoUS().getFileName()+" al cargarlo: "+e);
		}
		catch(IOException e)
		{
			Log4JManager.error("Error en los streams del archivo "+dtoFiltro.getArchivoUS().getFileName()+" al cargarlo: "+e);
		}
		
		this.dtoResultadoProcesarRipsEntSub.setRegistrosArchivoUS(contador);
		return error;
	}
	
	/**
	 * Método para realizar las validaciones del archivo AC
	 * @param pos
	 */
	private boolean validacionesGeneralesAC(DtoFiltroProcesarRipsEntidadesSub dtoFiltro) 
	{
		
		int contador = 0;
		boolean error=false;
		try
		{
			String cadena="";
			String[] campos = new String[0];
			//******SE INICIALIZA ARCHIVO*************************
			BufferedReader buffer = new BufferedReader(new java.io.InputStreamReader(dtoFiltro.getArchivoAC().getInputStream()));
				
			//********SE RECORRE LÍNEA POR LÍNEA**************
			cadena=buffer.readLine();
			while(cadena!=null)
			{
					contador++;
					int errores=0;
					//Se toman los campos de cada línea del archivo
					if(cadena.endsWith(","))
						cadena+=" ";
					campos = cadena.split(",");
					
					//Validación de que todo esté separado por comas
					if(campos.length<17||campos.length>17||UtilidadCadena.numeroOcurrenciasCaracter(cadena, ',')!=16)
					{
						//ALMACENO LOS CAMPOS A MOSTRAR EN LA CONSULTA POR EL ARCHIVO AC
						this.agregarValoresCampos(ConstantesBD.ripsAC, "", ConstantesCamposProcesoRipsEntidadesSub.tipoIdUsuario, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAC, "", ConstantesCamposProcesoRipsEntidadesSub.numeroIdUsuario, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAC, "", ConstantesCamposProcesoRipsEntidadesSub.fechaDeConsulta, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAC, "", ConstantesCamposProcesoRipsEntidadesSub.codigoDeConsulta, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAC, "", ConstantesCamposProcesoRipsEntidadesSub.numeroAutorizacion, contador);
						this.generarInconsistenciaCampos(ConstantesBD.ripsAC, ConstantesIntegridadDominio.acronimoCamposSeparacionComasErroneo, "",contador);
						error=true;
					}
					else
					{
						
						//ALMACENO LOS CAMPOS A MOSTRAR EN LA CONSULTA POR EL ARCHIVO AC
						this.agregarValoresCampos(ConstantesBD.ripsAC, campos[2], ConstantesCamposProcesoRipsEntidadesSub.tipoIdUsuario, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAC, campos[3], ConstantesCamposProcesoRipsEntidadesSub.numeroIdUsuario, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAC, campos[4], ConstantesCamposProcesoRipsEntidadesSub.fechaDeConsulta, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAC, campos[6], ConstantesCamposProcesoRipsEntidadesSub.codigoDeConsulta, contador);
						this.agregarValoresCampos(ConstantesBD.ripsAC, campos[5], ConstantesCamposProcesoRipsEntidadesSub.numeroAutorizacion, contador);
						
						//VALORES PARA LA CONSULTA DEL PROCESO
						this.agregarValoresCampos(ConstantesBD.ripsAC, campos[16], ConstantesCamposProcesoRipsEntidadesSub.valorArchivosAutorizaciones, contador);
						
						
							//1) ******** NÚMERO DE FACTURA************************************
							if(validacionGeneralCampo(campos[0],"Número de factura",20,ConstantesBD.ripsAC,contador,false,false,false,false,true))
								errores++;
							//2) ******* CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD**********************
							if(validacionGeneralCampo(campos[1],"Código prestador de servicios salud",12,ConstantesBD.ripsAC,contador,false,false,false,false,true))
								errores++;
							//3) ******** TIPO DE IDENTIFICACION ********************************************
							if(validacionGeneralCampo(campos[2],"Tipo de identificación",2,ConstantesBD.ripsAC,contador,false,false,false,false,true))
								errores++;
							//4) ******** NÚMERO DE IDENTIFICACIÓN************************************
							if(validacionGeneralCampo(campos[3],"Número de identificación",20,ConstantesBD.ripsAC,contador,false,false,false,false,true))
								errores++;
							//5) ******** FECHA DE LA CONSULTA************************************
							if(validacionGeneralCampo(campos[4],"Fecha de consulta",10,ConstantesBD.ripsAC,contador,false,false,true,false,true))
								errores++;
							//6) ******** NÚMERO DE AUTORIZACION ************************************
							if(validacionGeneralCampo(campos[5],"Número de autorización",15,ConstantesBD.ripsAC,contador,false,false,false,false,true))
								errores++;
							//7) ******** CODIGO DE CONSULTA ***************************************
							if(validacionGeneralCampo(campos[6],"Código de consulta",8,ConstantesBD.ripsAC,contador,false,false,false,false,true))
								errores++;
							//8) ******** FINALIDAD DE CONSULTA ***************************************
							if(validacionGeneralCampo(campos[7],"Finalidad consulta",2,ConstantesBD.ripsAC,contador,false,false,false,false,false))
								errores++;
							//9) ******** CAUSA EXTERNA ***************************************
							if(validacionGeneralCampo(campos[8],"Causa externa",2,ConstantesBD.ripsAC,contador,false,false,false,false,true))
								errores++;		
							//10) ******** CODIGO DIAGNOSTICO PRINCIPAL ***************************************
							if(validacionGeneralCampo(campos[9],"Código diagnóstico principal",4,ConstantesBD.ripsAC,contador,false,false,false,false,true))
								errores++;
							//11) ******** CÓDIGO DIAGNÓSTICO RELACIONADO 1 ***************************************
							if(validacionGeneralCampo(campos[10],"Código diagnóstico relacionado 1",4,ConstantesBD.ripsAC,contador,false,false,false,false,false))
								errores++;
							//12) ******** CÓDIGO DIAGNÓSTICO RELACIONADO 2 ***************************************
							if(validacionGeneralCampo(campos[11],"Código diagnóstico relacionado 2",4,ConstantesBD.ripsAC,contador,false,false,false,false,false))
								errores++;
							//13) ******** CÓDIGO DIAGNÓSTICO RELACIONADO 3 ***************************************
							if(validacionGeneralCampo(campos[12],"Código diagnóstico relacionado 3",4,ConstantesBD.ripsAC,contador,false,false,false,false,false))
								errores++;
							//14) ******** TIPO DIAGNOSTICO PRINCIPAL ***************************************
							if(validacionGeneralCampo(campos[13],"Tipo diagnóstico principal",1,ConstantesBD.ripsAC,contador,false,false,false,false,true))
								errores++;
							//15) ******** VALOR CONSULTA ***************************************
							if(validacionGeneralCampo(campos[14],"Valor consulta",15,ConstantesBD.ripsAC,contador,true,true,false,false,true))
								errores++;
							//16) ******** VALOR CUOTA MODERADORA ***************************************
							if(validacionGeneralCampo(campos[15],"Valor cuota moderadora",15,ConstantesBD.ripsAC,contador,true,true,false,false,false))
								errores++;
							//17) ******** VALOR NETO A PAGAR ***************************************
							if(validacionGeneralCampo(campos[16],"Valor neto a pagar",15,ConstantesBD.ripsAC,contador,true,true,false,false,true))
								errores++;
							
							
							//SI NO SE PRESENTARON ERRORES SE CONTINUA CON LAS VALIDACIONES, SI HAY ERRORES SE CANCELA EL PROCESO
							boolean errorvalidaciones=false;
							if(errores>0){
								error=true;
							}else{
							
								//1) ******** NÚMERO DE FACTURA************************************
								if(!numerosFacturaArchivoAF.contains(campos[0])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAC, ConstantesIntegridadDominio.acronimoNumeroFacturaNoExisteEnAF, "Número de la factura",contador);
									errorvalidaciones=true;
								}
								
								//2) ******* CÓDIGO DEL PRESTADOR DE SERVICIOS DE SALUD**********************
								//Validar que este código corresponda al código Minsalud parametrizado en el sistema para la entidad subcontratada en la funcionalidad de  'lectura de planos pacientes entidades subcontratadas'.
								/*EntidadesSubcontratadas entidadSub=servicioEntidadesSub.obtenerEntidadesSubcontratadasporId(dtoFiltro.getCodigoPkEntidadSub());
								if(!entidadSub.getCodigoMinsalud().equals(campos[1]))
								{
									this.generarInconsistenciaCampos(ConstantesBD.ripsAC, ConstantesIntegridadDominio.acronimoCampoNoValido, "Código del prestador de servicios de salud",contador);
									errorvalidaciones=true;
								}*/
								
								//3) ******** TIPO DE IDENTIFICACION ********************************************
								//Se verifica que el tipo de identificacion sea válido
								if(!this.tiposIdentificacion.containsValue(campos[2])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAC, ConstantesIntegridadDominio.acronimoCampoNoValido, "Tipo de identificación del usuario",contador);
									errorvalidaciones=true;
								}
								
								//5) ******** FECHA DE LA CONSULTA************************************
								//Validar que la fecha sea < = a la fecha del sistema.
								String fechaFinalConsulta = UtilidadFecha.conversionFormatoFechaAAp(campos[4]);
								String fechaActual = UtilidadFecha.conversionFormatoFechaAAp(Calendar.getInstance().getTime());
								if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaFinalConsulta, fechaActual)){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAC, ConstantesIntegridadDominio.acronimoCampoNoValido,"Fecha de consulta",contador);
									errorvalidaciones=true;
								}
								
								//7) ******** CODIGO DE CONSULTA ***************************************
								//Código válido de acuerdo al tipo de tarifario oficial seleccionado
								if(!this.codigosPropietariosServicioValidos.contains(campos[6])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAC, ConstantesIntegridadDominio.acronimoCampoNoValido,"Código de consulta",contador);
									errorvalidaciones=true;
								}
								
								//8) ******** FINALIDAD DE CONSULTA ***************************************
								//se verifica que en el caso que exista finalidad de consulta tenga el código válido
								if(!campos[7].trim().equals("")&&!dtoFiltro.getFinalidadesConsultaEnSistema().contains(campos[7])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAC, ConstantesIntegridadDominio.acronimoCampoNoValido,"Finalidad de la consulta",contador);
									errorvalidaciones=true;
								}
								
								//9) ******** CAUSA EXTERNA ***************************************
								//Se verifica que la causa externa tenga el código válido
								if(!campos[8].trim().equals("")&&!dtoFiltro.getCausasExternasEnSistema().contains(Utilidades.convertirAEntero(campos[8]))){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAC, ConstantesIntegridadDominio.acronimoCampoNoValido, "Causa externa",contador);
									errorvalidaciones=true;
								}
									
								
								//10) ******** CODIGO DIAGNOSTICO PRINCIPAL ***************************************
								//Diagnosticos validos
								if(!campos[9].trim().equals("")&&!dtoFiltro.getDiagnosticosEnSistema().contains(campos[9])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAC, ConstantesIntegridadDominio.acronimoCampoNoValido, "Código del diagnóstico principal",contador);
									errorvalidaciones=true;
								}
								
								//11) ******** CÓDIGO DIAGNÓSTICO RELACIONADO 1 ***************************************
								if(!campos[10].trim().equals("")&&!dtoFiltro.getDiagnosticosEnSistema().contains(campos[10])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAC, ConstantesIntegridadDominio.acronimoCampoNoValido, "Código del diagnóstico relacionado No. 1",contador);
									errorvalidaciones=true;
								}
								
								//12) ******** CÓDIGO DIAGNÓSTICO RELACIONADO 2 ***************************************
								if(!campos[11].trim().equals("")&&!dtoFiltro.getDiagnosticosEnSistema().contains(campos[11])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAC, ConstantesIntegridadDominio.acronimoCampoNoValido, "Código del diagnóstico relacionado No. 2",contador);
									errorvalidaciones=true;
								}
								
								//13) ******** CÓDIGO DIAGNÓSTICO RELACIONADO 3 ***************************************
								if(!campos[12].trim().equals("")&&!dtoFiltro.getDiagnosticosEnSistema().contains(campos[12])){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAC, ConstantesIntegridadDominio.acronimoCampoNoValido, "Código del diagnóstico relacionado No. 3",contador);
									errorvalidaciones=true;
								}
								
								//14) ******** TIPO DIAGNOSTICO PRINCIPAL ***************************************
								//Se verifica que el tipo de diagnóstico tenga los valores 1 = Impresión diagnóstica,2 = Confirmado nuevo,3 =Confirmado repetido
								if(!campos[13].trim().equals("")&&!campos[13].equals("1")&&!campos[13].equals("2")&&!campos[13].equals("3")){
									this.generarInconsistenciaCampos(ConstantesBD.ripsAC, ConstantesIntegridadDominio.acronimoCampoNoValido, "Tipo de diagnóstico principal",contador);
									errorvalidaciones=true;
								}
									
								if(errorvalidaciones)
									error=true;
								else
									this.registrosSinInconsistenciaAC.add(contador);
							}
							
					}  //fin else IF 
						
					cadena=buffer.readLine(); //siguiente registro
					
				} //Fin While
				
				
				//***************CERRAR ARCHIVO****************************
				buffer.close();
				
			}
			catch(FileNotFoundException e)
			{
				Log4JManager.error("No se pudo encontrar el archivo "+dtoFiltro.getArchivoAC().getFileName()+" al cargarlo: "+e);
			}
			catch(IOException e)
			{
				Log4JManager.error("Error en los streams del archivo "+dtoFiltro.getArchivoAC().getFileName()+" al cargarlo: "+e);
			}
			
			this.dtoResultadoProcesarRipsEntSub.setRegistrosArchivoAC(contador);
			return error;
	}
	
	/**
	 * Método para realizar las validaciones del archivo AC
	 * @param pos
	 */
	private void validarArchivosConArchivoUS(DtoFiltroProcesarRipsEntidadesSub dtoFiltro) 
	{
		
		
		try
		{
			String cadenaUS="";
			
			String cadenaAC="";
			String cadenaAP="";
			String cadenaAM="";
			String cadenaAT="";
			//String cadenaAU="";
			//String cadenaAH="";
			
			int contador = 0;
			/**VARIABLE QUE INDICA SI EL PACIENTE SE ENCUENTRA EN TODOS LOS ARCHIVOS LEIDOS****/
			boolean encontroUsuario;
			
			String[] camposUS = new String[0];
			
			String[] camposAC = new String[0];
			String[] camposAP = new String[0];
			String[] camposAM = new String[0];
			String[] camposAT = new String[0];
			//String[] camposAU = new String[0];
			//String[] camposAH = new String[0];
			
			//******SE INICIALIZA ARCHIVO US Y AC*************************
			BufferedReader bufferUS = new BufferedReader(new java.io.InputStreamReader(dtoFiltro.getArchivoUS().getInputStream()));
			
			BufferedReader bufferAC = new BufferedReader(new java.io.InputStreamReader(dtoFiltro.getArchivoAC().getInputStream()));
			BufferedReader bufferAP = new BufferedReader(new java.io.InputStreamReader(dtoFiltro.getArchivoAP().getInputStream()));
			BufferedReader bufferAM = new BufferedReader(new java.io.InputStreamReader(dtoFiltro.getArchivoAM().getInputStream()));
			BufferedReader bufferAT = new BufferedReader(new java.io.InputStreamReader(dtoFiltro.getArchivoAT().getInputStream()));
			//BufferedReader bufferAU = new BufferedReader(new java.io.InputStreamReader(dtoFiltro.getArchivoAU().getInputStream()));
			//BufferedReader bufferAH = new BufferedReader(new java.io.InputStreamReader(dtoFiltro.getArchivoAH().getInputStream()));
			
			//********SE RECORRE LÍNEA POR LÍNEA ARCHIVO US**************
			cadenaUS=bufferUS.readLine();
			while(cadenaUS!=null)
			{
				
				encontroUsuario=false;
				
				contador++;
				if(this.registrosSinInconsistenciaUS.contains(contador)){
					//Se toman los campos de cada línea del archivo
					if(cadenaUS.endsWith(","))
						cadenaUS+=" ";
					camposUS = cadenaUS.split(",");
					
					//*********************SE RECORRE LÍNEA POR LÍNEA ARCHIVO AC*****************************************
					cadenaAC=bufferAC.readLine();
					int contadorAC=0;
					int numeroProcesados=0;
					
					while(cadenaAC!=null)
					{
						contadorAC++;
						if(this.registrosSinInconsistenciaAC.contains(contadorAC)){
							if(cadenaAC.endsWith(","))
								cadenaAC+=" ";
						
							camposAC = cadenaAC.split(",");
							
							//SI TIPO Y NUMERO DE IDENTIFICACION SON IGUALES
							if(camposUS[0].equals(camposAC[2])&&camposUS[1].equals(camposAC[3])){
								
								encontroUsuario=true;
								//SE TOMA Número de autorización,Código de consulta,Valor neto a pagar.
											//campos[5]				campos[6]            campos[16]
								
								UtilidadTransaccion.getTransaccion().begin();
								boolean encuentraAutorizacion=false;
								boolean encuentraValoresAutorizacion=false;
								boolean noSeEncuentraAlmacenado=false;
								DtoAutorizacionesEntSubRips dtoAutor=null;
								try{
									DtoAutorizacionEntSubcontratadasCapitacion dtoBusqueda = new DtoAutorizacionEntSubcontratadasCapitacion();
									dtoBusqueda.setCodigoEntidadSubcontratada(dtoFiltro.getCodigoPkEntidadSub());
								
									ArrayList<DtoAutorizacionEntSubcontratadasCapitacion> listaAutorizXEntidadSub=servicioAutorizacionesPorEntidadesSubServicio.obtenerAutorizacionesPorEntSubSinVigencia(dtoBusqueda);
									
									
									for(DtoAutorizacionEntSubcontratadasCapitacion dtoAutoriz : listaAutorizXEntidadSub){
										if(!UtilidadTexto.isEmpty(dtoAutoriz.getConsecutivoAutorizacion())){
											//VALIDACION SI EXISTE EL NUMERO DE AUTORIZACION EN EL ARCHIVO AC
											if(dtoAutoriz.getConsecutivoAutorizacion().equals(camposAC[5])){
												numeroProcesados++;
												encuentraAutorizacion=true;
												dtoAutor=autorEntSubRips.obtenerAutorizacionEntSubRipsPorEntSub(dtoAutoriz.getAutorizacion());
												if(dtoAutor==null)
												{
													noSeEncuentraAlmacenado=true;
													DtoAutorizacionEntSubcontratadasCapitacion dtoParametros=new DtoAutorizacionEntSubcontratadasCapitacion();
													dtoParametros.setAutorizacion(dtoAutoriz.getAutorizacion());
													ArrayList<AutorizacionesEntSubServi> listaServiciosAutorizacion=autorEntSubServiServicio.listarAutorizacionesEntSubServiPorAutoEntSub(dtoParametros);
													
													//SE VALIDA SI EL CODIGO DE LA CONSULTA ES IGUAL AL CODIGO O SERVICIO DE LA AUTORIZACION Y EL VALOR NETO Y LA TARIFA DE LA AUTORIZACION
													boolean encontro=false;
													for(AutorizacionesEntSubServi servicio:listaServiciosAutorizacion){
														ArrayList<DtoServicios> servicioConsulta=servicioServ.obtenerServiciosXTipoTarifarioOficial(servicio.getServicios().getCodigo(),dtoFiltro.getTarifarioSeleccionadoCodServicios());
														if(camposAC[6].equals(servicioConsulta.get(0).getCodigoPropietarioServicio())&&camposAC[16].equals(servicio.getValorTarifa()+"")){
															encuentraValoresAutorizacion=true;
															encontro=true;
														}
													}
													if(encontro){
														//SE GUARDA EL INDICATIVO RIPS PARA LA AUTORIZACION
														AutorizacionesEntSubRips autor=new AutorizacionesEntSubRips();
														autor.setFecha(UtilidadFecha.getFechaActualTipoBD());
														autor.setHora(UtilidadFecha.getHoraActual());
														Usuarios usu=new Usuarios();
														usu.setLogin(dtoFiltro.getUsuario().getLoginUsuario());
														autor.setUsuarios(usu);
														AutorizacionesEntidadesSub autorizacion=new AutorizacionesEntidadesSub();
														autorizacion.setConsecutivo(dtoAutoriz.getAutorizacion());
														autor.setAutorizacionesEntidadesSub(autorizacion);
														autorEntSubRips.guardarAutorizacionEntSubRips(autor);
														
														DtoInconsistenciasRipsEntidadesSub dtoAutorizacion=new DtoInconsistenciasRipsEntidadesSub();
														dtoAutorizacion.setNumeroFila(contadorAC);
														dtoAutorizacion.setNombreArchivo(ConstantesBD.ripsAC);
														listaAutorizacionesValidas.add(dtoAutorizacion);
														
													}
												}
											}
										}
									}
								
								UtilidadTransaccion.getTransaccion().commit();
								}
								catch(Exception e){
									UtilidadTransaccion.getTransaccion().rollback();
									Log4JManager.info("Error en la comparacion del archivo AC y en guardar el indicativo rips de la autorizacion");
									e.printStackTrace();
								}
								
								if(!encuentraAutorizacion)
									this.generarInconsistenciaCampos(ConstantesBD.ripsAC,ConstantesIntegridadDominio.acronimoAutorizacionNoEncontrada,"Número de Autorización",contador);
								if(encuentraAutorizacion&&!noSeEncuentraAlmacenado)
									this.generarInconsistenciaCampos(ConstantesBD.ripsAC,ConstantesIntegridadDominio.acronimoAutorizacionProcesadaConAnterioridad,"Número de Autorización"+ConstantesBD.separadorSplit+dtoAutor.getFechaProceso(),contador);
								if(encuentraAutorizacion&&noSeEncuentraAlmacenado&&!encuentraValoresAutorizacion)
									this.generarInconsistenciaCampos(ConstantesBD.ripsAC,ConstantesIntegridadDominio.acronimoValoresAutorizacionNoValidos,"Código y/o Valor Servicio",contador);
								
								
								
							}
						}
						//SIGUIENTE LINEA DEL ARCHIVO AC
						cadenaAC=bufferAC.readLine();
						
					}//fin while cadena AC
					bufferAC.close();
					this.dtoResultadoProcesarRipsEntSub.setRegistrosProcesadosArchivoAC(numeroProcesados);
					
					//*************************SE RECORRE LÍNEA POR LÍNEA ARCHIVO AP*************************************
					cadenaAP=bufferAP.readLine();
					int contadorAP=0;
					numeroProcesados=0;
					
					while(cadenaAP!=null)
					{
						contadorAP++;
						if(this.registrosSinInconsistenciaAP.contains(contadorAP)){
							if(cadenaAP.endsWith(","))
								cadenaAP+=" ";
							camposAP = cadenaAP.split(",");
							
							//SI TIPO Y NUMERO DE IDENTIFICACION SON IGUALES
							if(camposUS[0].equals(camposAP[2])&&camposUS[1].equals(camposAP[3])){
								
								encontroUsuario=true;
								//SE TOMA Número de autorización,Código del procedimiento,Valor del procedimiento.
											//campos[5]				campos[6]            campos[14]
								
								UtilidadTransaccion.getTransaccion().begin();
								boolean encuentraAutorizacion=false;
								boolean encuentraValoresAutorizacion=false;
								boolean noSeEncuentraAlmacenado=false;
								DtoAutorizacionesEntSubRips dtoAutor=null;
								try{
									
									DtoAutorizacionEntSubcontratadasCapitacion dtoBusqueda = new DtoAutorizacionEntSubcontratadasCapitacion();
									dtoBusqueda.setCodigoEntidadSubcontratada(dtoFiltro.getCodigoPkEntidadSub());
								
									ArrayList<DtoAutorizacionEntSubcontratadasCapitacion> listaAutorizXEntidadSub=servicioAutorizacionesPorEntidadesSubServicio.obtenerAutorizacionesPorEntSubSinVigencia(dtoBusqueda);
									
									for(DtoAutorizacionEntSubcontratadasCapitacion dtoAutoriz : listaAutorizXEntidadSub){
										if(!UtilidadTexto.isEmpty(dtoAutoriz.getConsecutivoAutorizacion())){
											//VALIDACION SI EXISTE EL NUMERO DE AUTORIZACION EN EL ARCHIVO AP
											if(dtoAutoriz.getConsecutivoAutorizacion().equals(camposAP[5])){
												numeroProcesados++;
												encuentraAutorizacion=true;
												dtoAutor=autorEntSubRips.obtenerAutorizacionEntSubRipsPorEntSub(dtoAutoriz.getAutorizacion());
												if(dtoAutor==null){
													noSeEncuentraAlmacenado=true;
													DtoAutorizacionEntSubcontratadasCapitacion dtoParametros=new DtoAutorizacionEntSubcontratadasCapitacion();
													dtoParametros.setAutorizacion(dtoAutoriz.getAutorizacion());
													ArrayList<AutorizacionesEntSubServi> listaServiciosAutorizacion=autorEntSubServiServicio.listarAutorizacionesEntSubServiPorAutoEntSub(dtoParametros);
													
													//SE VALIDA SI EL CODIGO DE LA CONSULTA ES IGUAL AL CODIGO O SERVICIO DE LA AUTORIZACION, COMPARAR EL VALOR NETO Y LA TARIFA DE LA AUTORIZACION
													boolean encontro=false;
													for(AutorizacionesEntSubServi servicio:listaServiciosAutorizacion){
														ArrayList<DtoServicios> servicioConsulta=servicioServ.obtenerServiciosXTipoTarifarioOficial(servicio.getServicios().getCodigo(),dtoFiltro.getTarifarioSeleccionadoCodServicios());
														if(camposAP[6].equals(servicioConsulta.get(0).getCodigoPropietarioServicio())&&camposAP[14].equals(servicio.getValorTarifa()+"")){
															encontro=true;
															encuentraValoresAutorizacion=true;
														}
													}
													
													if(encontro){
														//SE GUARDA EL INDICATIVO RIPS PARA LA AUTORIZACION
														AutorizacionesEntSubRips autor=new AutorizacionesEntSubRips();
														autor.setFecha(UtilidadFecha.getFechaActualTipoBD());
														autor.setHora(UtilidadFecha.getHoraActual());
														Usuarios usu=new Usuarios();
														usu.setLogin(dtoFiltro.getUsuario().getLoginUsuario());
														autor.setUsuarios(usu);
														AutorizacionesEntidadesSub autorizacion=new AutorizacionesEntidadesSub();
														autorizacion.setConsecutivo(dtoAutoriz.getAutorizacion());
														autor.setAutorizacionesEntidadesSub(autorizacion);
														autorEntSubRips.guardarAutorizacionEntSubRips(autor);
														
														DtoInconsistenciasRipsEntidadesSub dtoAutorizacion=new DtoInconsistenciasRipsEntidadesSub();
														dtoAutorizacion.setNumeroFila(contadorAP);
														dtoAutorizacion.setNombreArchivo(ConstantesBD.ripsAP);
														listaAutorizacionesValidas.add(dtoAutorizacion);
													}
													
												}
											}
										}
									}
								UtilidadTransaccion.getTransaccion().commit();
								}
								catch(Exception e){
									UtilidadTransaccion.getTransaccion().rollback();
									Log4JManager.info("Error en la comparacion del archivo AP y en guardar el indicativo rips de la autorizacion");
									e.printStackTrace();
								}
								
								if(!encuentraAutorizacion)
									this.generarInconsistenciaCampos(ConstantesBD.ripsAP,ConstantesIntegridadDominio.acronimoAutorizacionNoEncontrada,"Número de Autorización",contador);
								if(encuentraAutorizacion&&!noSeEncuentraAlmacenado)
									this.generarInconsistenciaCampos(ConstantesBD.ripsAP,ConstantesIntegridadDominio.acronimoAutorizacionProcesadaConAnterioridad,"Número de Autorización"+ConstantesBD.separadorSplit+dtoAutor.getFechaProceso(),contador);
								if(encuentraAutorizacion&&noSeEncuentraAlmacenado&&!encuentraValoresAutorizacion)
									this.generarInconsistenciaCampos(ConstantesBD.ripsAP,ConstantesIntegridadDominio.acronimoValoresAutorizacionNoValidos,"Código y/o Valor Servicio",contador);
								
								
							}
						}
						//SIGUIENTE LINEA DEL ARCHIVO AP
						cadenaAP=bufferAP.readLine();
					}//fin while cadena AP
					bufferAP.close();
					this.dtoResultadoProcesarRipsEntSub.setRegistrosProcesadosArchivoAP(numeroProcesados);
					
					//***************************SE RECORRE LÍNEA POR LÍNEA ARCHIVO AM*****************************************
					cadenaAM=bufferAM.readLine();
					int contadorAM=0;
					numeroProcesados=0;
					
					while(cadenaAM!=null)
					{
						contadorAM++;
						if(this.registrosSinInconsistenciaAM.contains(contadorAM)){
							if(cadenaAM.endsWith(","))
								cadenaAM+=" ";
							camposAM = cadenaAM.split(",");
							
							//SI TIPO Y NUMERO DE IDENTIFICACION SON IGUALES
							if(camposUS[0].equals(camposAM[2])&&camposUS[1].equals(camposAM[3])){
								
								encontroUsuario=true;
								
								//SE TOMA Número de autorización. Código del medicamento. Número de Unidades.	Valor total del medicamento.
											//campos[4]				   campos[5]               campos[11]				campos[13]
								
								UtilidadTransaccion.getTransaccion().begin();
								boolean encuentraAutorizacion=false;
								boolean encuentraValoresAutorizacion=false;
								boolean noSeEncuentraAlmacenado=false;
								DtoAutorizacionesEntSubRips dtoAutor=null;
								try{
									DtoAutorizacionEntSubcontratadasCapitacion dtoBusqueda = new DtoAutorizacionEntSubcontratadasCapitacion();
									dtoBusqueda.setCodigoEntidadSubcontratada(dtoFiltro.getCodigoPkEntidadSub());
								
									ArrayList<DtoAutorizacionEntSubcontratadasCapitacion> listaAutorizXEntidadSub=servicioAutorizacionesPorEntidadesSubServicio.obtenerAutorizacionesPorEntSubSinVigencia(dtoBusqueda);
									
									for(DtoAutorizacionEntSubcontratadasCapitacion dtoAutoriz : listaAutorizXEntidadSub){
										if(!UtilidadTexto.isEmpty(dtoAutoriz.getConsecutivoAutorizacion())){
											//VALIDACION SI EXISTE EL NUMERO DE AUTORIZACION EN EL ARCHIVO AM
											if(dtoAutoriz.getConsecutivoAutorizacion().equals(camposAM[4])){
												numeroProcesados++;
												encuentraAutorizacion=true;
												dtoAutor=autorEntSubRips.obtenerAutorizacionEntSubRipsPorEntSub(dtoAutoriz.getAutorizacion());
												if(dtoAutor==null){
												
													noSeEncuentraAlmacenado=true;
													DtoAutorizacionEntSubcontratadasCapitacion dtoParametros=new DtoAutorizacionEntSubcontratadasCapitacion();
													dtoParametros.setAutorizacion(dtoAutoriz.getAutorizacion());
													ArrayList<DtoArticulosAutorizaciones> listaArticAutor=autorEntSubArtiServicio.listarautorizacionesEntSubArticuPorAutoEntSub(dtoParametros);
													
													//PARA VERIFICAR TODOS LOS ARTICULOS DE LA AUTORIZACION CON TODOS LOS DEL ARCHIVO PLANO
													boolean validarArticulos=true;
													//SE VALIDA SI EL CODIGO DE LA CONSULTA ES IGUAL AL CODIGO O SERVICIO DE LA AUTORIZACION
													for(DtoArticulosAutorizaciones articulo:listaArticAutor){
														//Verificar que la cantidad autorizada y el valor del articulo del articulo la trae la consulta
														if(!camposAM[5].equals(articulo.getCodigoArticulo()+"")||!camposAM[11].equals(articulo.getTotalUnidadesFormulacion()+"")
																||!camposAM[13].equals(articulo.getValorArticulo()+"")){
															validarArticulos=false;
														}
														
													}
													if(Utilidades.isEmpty(listaArticAutor)){
														validarArticulos=false;
													}
													
													if(validarArticulos){
														
														//SE GUARDA EL INDICATIVO RIPS PARA LA AUTORIZACION
														encuentraValoresAutorizacion=true;
														AutorizacionesEntSubRips autor=new AutorizacionesEntSubRips();
														autor.setFecha(UtilidadFecha.getFechaActualTipoBD());
														autor.setHora(UtilidadFecha.getHoraActual());
														Usuarios usu=new Usuarios();
														usu.setLogin(dtoFiltro.getUsuario().getLoginUsuario());
														autor.setUsuarios(usu);
														AutorizacionesEntidadesSub autorizacion=new AutorizacionesEntidadesSub();
														autorizacion.setConsecutivo(dtoAutoriz.getAutorizacion());
														autor.setAutorizacionesEntidadesSub(autorizacion);
														autorEntSubRips.guardarAutorizacionEntSubRips(autor);
													
														DtoInconsistenciasRipsEntidadesSub dtoAutorizacion=new DtoInconsistenciasRipsEntidadesSub();
														dtoAutorizacion.setNumeroFila(contadorAM);
														dtoAutorizacion.setNombreArchivo(ConstantesBD.ripsAM);
														listaAutorizacionesValidas.add(dtoAutorizacion);
													}
													
													
												}
												
											}
										}
									}
								UtilidadTransaccion.getTransaccion().commit();
								}
								catch(Exception e){
									UtilidadTransaccion.getTransaccion().rollback();
									Log4JManager.info("Error en la comparacion del archivo AM y en guardar el indicativo rips de la autorizacion");
									e.printStackTrace();
								}
								
								if(!encuentraAutorizacion)
									this.generarInconsistenciaCampos(ConstantesBD.ripsAM,ConstantesIntegridadDominio.acronimoAutorizacionNoEncontrada,"Número de Autorización",contador);
								if(encuentraAutorizacion&&!noSeEncuentraAlmacenado)
									this.generarInconsistenciaCampos(ConstantesBD.ripsAM,ConstantesIntegridadDominio.acronimoAutorizacionProcesadaConAnterioridad,"Número de Autorización"+ConstantesBD.separadorSplit+dtoAutor.getFechaProceso(),contador);
								if(encuentraAutorizacion&&noSeEncuentraAlmacenado&&!encuentraValoresAutorizacion)
									this.generarInconsistenciaCampos(ConstantesBD.ripsAM,ConstantesIntegridadDominio.acronimoValoresAutorizacionNoValidos,"Código, Cantidad y/o Valor Articulo",contador);
								
							}
							
						}
						//SIGUIENTE LINEA DEL ARCHIVO AM
						cadenaAM=bufferAM.readLine();
					}//fin while cadena AM
					bufferAM.close();
					this.dtoResultadoProcesarRipsEntSub.setRegistrosProcesadosArchivoAM(numeroProcesados);
					
					//********SE RECORRE LÍNEA POR LÍNEA ARCHIVO AT**************
					cadenaAT=bufferAT.readLine();
					numeroProcesados=0;
					int contadorAT=0;
					
					while(cadenaAT!=null)
					{
						contadorAT++;
						if(this.registrosSinInconsistenciaAT.contains(contadorAT)){
							if(cadenaAT.endsWith(","))
								cadenaAT+=" ";
							camposAT = cadenaAT.split(",");
							
							//SI TIPO Y NUMERO DE IDENTIFICACION SON IGUALES
							if(camposUS[0].equals(camposAT[2])&&camposUS[1].equals(camposAT[3])){
								
								encontroUsuario=true;
								//SE TOMA Número de autorización. Código del Servicio. Cantidad. Valor total del material e insumo.
											//campos[4]				campos[6]          campos[8]		campos[10]
								
								UtilidadTransaccion.getTransaccion().begin();
								boolean encuentraAutorizacion=false;
								boolean encuentraValoresAutorizacion=false;
								boolean noSeEncuentraAlmacenado=false;
								DtoAutorizacionesEntSubRips dtoAutor=null;
								try{
									DtoAutorizacionEntSubcontratadasCapitacion dtoBusqueda = new DtoAutorizacionEntSubcontratadasCapitacion();
									dtoBusqueda.setCodigoEntidadSubcontratada(dtoFiltro.getCodigoPkEntidadSub());
								
									ArrayList<DtoAutorizacionEntSubcontratadasCapitacion> listaAutorizXEntidadSub=servicioAutorizacionesPorEntidadesSubServicio.obtenerAutorizacionesPorEntSubSinVigencia(dtoBusqueda);
									
									for(DtoAutorizacionEntSubcontratadasCapitacion dtoAutoriz : listaAutorizXEntidadSub){
										if(!UtilidadTexto.isEmpty(dtoAutoriz.getConsecutivoAutorizacion())){
											//VALIDACION SI EXISTE EL NUMERO DE AUTORIZACION EN EL ARCHIVO AT
											if(dtoAutoriz.getConsecutivoAutorizacion().equals(camposAT[4])){
												numeroProcesados++;
												encuentraAutorizacion=true;
												dtoAutor=autorEntSubRips.obtenerAutorizacionEntSubRipsPorEntSub(dtoAutoriz.getAutorizacion());
												if(dtoAutor==null){
													noSeEncuentraAlmacenado=true;
													DtoAutorizacionEntSubcontratadasCapitacion dtoParametros=new DtoAutorizacionEntSubcontratadasCapitacion();
													dtoParametros.setAutorizacion(dtoAutoriz.getAutorizacion());
													ArrayList<AutorizacionesEntSubServi> listaServiciosAutorizacion=autorEntSubServiServicio.listarAutorizacionesEntSubServiPorAutoEntSub(dtoParametros);
													ArrayList<DtoArticulosAutorizaciones> listaArticAutor=autorEntSubArtiServicio.listarautorizacionesEntSubArticuPorAutoEntSub(dtoParametros);
													
													boolean validarServiciosArticulos=true;
													
													//SE VALIDA SI EL CODIGO DE LA CONSULTA ES IGUAL AL CODIGO O SERVICIO DE LA AUTORIZACION
													for(AutorizacionesEntSubServi servicio:listaServiciosAutorizacion){
														ArrayList<DtoServicios> servicioConsulta=servicioServ.obtenerServiciosXTipoTarifarioOficial(servicio.getServicios().getCodigo(),dtoFiltro.getTarifarioSeleccionadoCodServicios());
														if(!camposAT[6].equals(servicioConsulta.get(0).getCodigoPropietarioServicio())||!camposAT[10].equals(servicio.getValorTarifa()+"")){
															validarServiciosArticulos=false;
														}
													}
													
													//SE VALIDA SI EL CODIGO DE LA CONSULTA ES IGUAL AL CODIGO O SERVICIO DE LA AUTORIZACION,
													//COMPARAR EL VALOR NETO Y LA TARIFA DE LA AUTORIZACION
													for(DtoArticulosAutorizaciones articulo:listaArticAutor){
														if(!(Utilidades.convertirAEntero(camposAT[6])==articulo.getCodigoArticulo().intValue())||!camposAT[10].equals(articulo.getValorArticulo()+"")){
															validarServiciosArticulos=false;
															
														}
													}
													
													if(Utilidades.isEmpty(listaServiciosAutorizacion)&&Utilidades.isEmpty(listaArticAutor)){
														validarServiciosArticulos=false;
													}
													
													if(validarServiciosArticulos){
														//SE GUARDA EL INDICATIVO RIPS PARA LA AUTORIZACION
														encuentraValoresAutorizacion=true;
														AutorizacionesEntSubRips autor=new AutorizacionesEntSubRips();
														autor.setFecha(UtilidadFecha.getFechaActualTipoBD());
														autor.setHora(UtilidadFecha.getHoraActual());
														Usuarios usu=new Usuarios();
														usu.setLogin(dtoFiltro.getUsuario().getLoginUsuario());
														autor.setUsuarios(usu);
														AutorizacionesEntidadesSub autorizacion=new AutorizacionesEntidadesSub();
														autorizacion.setConsecutivo(dtoAutoriz.getAutorizacion());
														autor.setAutorizacionesEntidadesSub(autorizacion);
														autorEntSubRips.guardarAutorizacionEntSubRips(autor);
														
														DtoInconsistenciasRipsEntidadesSub dtoAutorizacion=new DtoInconsistenciasRipsEntidadesSub();
														dtoAutorizacion.setNumeroFila(contadorAT);
														dtoAutorizacion.setNombreArchivo(ConstantesBD.ripsAT);
														listaAutorizacionesValidas.add(dtoAutorizacion);
													}
												}
												
											}
										}
									}
								UtilidadTransaccion.getTransaccion().commit();
								}
								catch(Exception e){
									UtilidadTransaccion.getTransaccion().rollback();
									Log4JManager.info("Error en la comparacion del archivo AT y en guardar el indicativo rips de la autorizacion");
									e.printStackTrace();
								}
								
								if(!encuentraAutorizacion)
									this.generarInconsistenciaCampos(ConstantesBD.ripsAT,ConstantesIntegridadDominio.acronimoAutorizacionNoEncontrada,"Número de Autorización",contador);
								if(encuentraAutorizacion&&!noSeEncuentraAlmacenado)
									this.generarInconsistenciaCampos(ConstantesBD.ripsAT,ConstantesIntegridadDominio.acronimoAutorizacionProcesadaConAnterioridad,"Número de Autorización"+ConstantesBD.separadorSplit+dtoAutor.getFechaProceso(),contador);
								if(encuentraAutorizacion&&noSeEncuentraAlmacenado&&!encuentraValoresAutorizacion)
									this.generarInconsistenciaCampos(ConstantesBD.ripsAT,ConstantesIntegridadDominio.acronimoValoresAutorizacionNoValidos,"Código y/o Valor Servicio/Articulo",contador);
								
								
							}
							
						}
						//SIGUIENTE LINEA DEL ARCHIVO AT
						cadenaAT=bufferAT.readLine();
					}//fin while cadena AT
					bufferAT.close();
					this.dtoResultadoProcesarRipsEntSub.setRegistrosProcesadosArchivoAT(numeroProcesados);
					
					//*******************************SE RECORRE LÍNEA POR LÍNEA ARCHIVO AU***********************************************
					/*cadenaAU=bufferAU.readLine();
					numeroProcesados=0;
					while(cadenaAU!=null)
					{
						if(cadenaAU.endsWith(","))
							cadenaAU+=" ";
						camposAU = cadenaAU.split(",");
						
						//SI TIPO Y NUMERO DE IDENTIFICACION SON IGUALES
						if(camposUS[0].equals(camposAU[2])&&camposUS[1].equals(camposAU[3])){
							
							//SE TOMA Número de autorización. Fecha de Ingreso del usuario a observación.
										//campos[6]							campos[4]            
							
							AutorizacionesIngresoEstanciaHibernateDAO dao=new AutorizacionesIngresoEstanciaHibernateDAO();
							ArrayList<DTOAdministracionAutorizacion> listaAutor=dao.obtenerAutorizacionesPorEntidadSub(dtoFiltro.getCodigoPkEntidadSub());
							
							boolean encuentraAutorizacion=false;
							boolean encuentraValoresAutorizacion=false;
							boolean noSeEncuentraAlmacenado=false;
							
							for(DTOAdministracionAutorizacion dtoAutoriz : listaAutor){
								
								//VALIDACION SI EXISTE EL NUMERO DE AUTORIZACION EN EL ARCHIVO AU
								if(dtoAutoriz.getConsecutivoAutorizacion()==Utilidades.convertirALong(camposAU[6])){
									numeroProcesados++;
									encuentraAutorizacion=true;
									DtoAutorizacionesEntSubRips dtoAutor=autorEntSubRips.obtenerAutorizacionEntSubRipsPorEntSub(dtoAutoriz.getConsecutivoAutorizacion());
									if(dtoAutor==null){
										noSeEncuentraAlmacenado=true;
										if(ConstantesBD.codigoViaIngresoUrgencias==dtoAutoriz.getCodigoViaIngreso()
												&&UtilidadFecha.conversionFormatoFechaAAp(dtoAutoriz.getFechaAdmision()).equals(camposAU[4])){
											
											//SE GUARDA EL INDICATIVO RIPS PARA LA AUTORIZACION
											encuentraValoresAutorizacion=true;
											UtilidadTransaccion.getTransaccion().begin();
											AutorizacionesEntSubRips autor=new AutorizacionesEntSubRips();
											autor.setFecha(UtilidadFecha.getFechaActualTipoBD());
											autor.setHora(UtilidadFecha.getHoraActual());
											Usuarios usu=new Usuarios();
											usu.setLogin(dtoFiltro.getUsuario().getLoginUsuario());
											autor.setUsuarios(usu);
											autor.setAutorizacionEntidadSub(dtoAutoriz.getConsecutivoAutorizacion());
											autorEntSubRips.guardarAutorizacionEntSubRips(autor);
											UtilidadTransaccion.getTransaccion().commit();
											
										}
										
										
									}
									
								}
							
							}
							
							if(!encuentraAutorizacion)
								this.generarInconsistenciaCampos(ConstantesBD.ripsAU,ConstantesIntegridadDominio.acronimoAutorizacionNoEncontrada,"Número de Autorización",contador);
							if(encuentraAutorizacion&&!noSeEncuentraAlmacenado)
								this.generarInconsistenciaCampos(ConstantesBD.ripsAU,ConstantesIntegridadDominio.acronimoAutorizacionProcesadaConAnterioridad,"Número de Autorización",contador);
							if(encuentraAutorizacion&&noSeEncuentraAlmacenado&&!encuentraValoresAutorizacion)
								this.generarInconsistenciaCampos(ConstantesBD.ripsAU,ConstantesIntegridadDominio.acronimoValoresAutorizacionNoValidos,"",contador);
							
							
						}
						//SIGUIENTE LINEA DEL ARCHIVO AU
						cadenaAU=bufferAU.readLine();
					}//fin while cadena AU
					
					bufferAU.close();
					this.dtoResultadoProcesarRipsEntSub.setRegistrosProcesadosArchivoAU(numeroProcesados);
					
					//*******************************SE RECORRE LÍNEA POR LÍNEA ARCHIVO AH***********************************************
					cadenaAH=bufferAH.readLine();
					numeroProcesados=0;
					while(cadenaAH!=null)
					{
						if(cadenaAH.endsWith(","))
							cadenaAH+=" ";
						camposAH = cadenaAH.split(",");
						
						//SI TIPO Y NUMERO DE IDENTIFICACION SON IGUALES
						if(camposUS[0].equals(camposAH[2])&&camposUS[1].equals(camposAH[3])){
							
							//SE TOMA Número de autorización. Vía de Ingreso a la Institución. Fecha de Ingreso del usuario a la institución. 
										//campos[7]					    campos[4]                               campos[5]								campos[9]
							
							AutorizacionesIngresoEstanciaHibernateDAO dao=new AutorizacionesIngresoEstanciaHibernateDAO();
							ArrayList<DTOAdministracionAutorizacion> listaAutor=dao.obtenerAutorizacionesPorEntidadSub(dtoFiltro.getCodigoPkEntidadSub());
							
							
							boolean encuentraAutorizacion=false;
							boolean encuentraValoresAutorizacion=false;
							boolean noSeEncuentraAlmacenado=false;
							
							for(DTOAdministracionAutorizacion dtoAutoriz : listaAutor){
								
								//VALIDACION SI EXISTE EL NUMERO DE AUTORIZACION EN EL ARCHIVO AH
								if(dtoAutoriz.getConsecutivoAutorizacion()==Utilidades.convertirALong(camposAH[7])){
									numeroProcesados++;
									encuentraAutorizacion=true;
									DtoAutorizacionesEntSubRips dtoAutor=autorEntSubRips.obtenerAutorizacionEntSubRipsPorEntSub(dtoAutoriz.getConsecutivoAutorizacion());
									if(dtoAutor==null){
										noSeEncuentraAlmacenado=true;
										if(dtoAutoriz.getCodigoViaIngreso()==ConstantesBD.codigoViaIngresoHospitalizacion
												&&UtilidadFecha.conversionFormatoFechaAAp(dtoAutoriz.getFechaAdmision()).equals(camposAH[5])
												&&dtoAutoriz.getAcronimoDiagnosticoPrinc().equals(camposAH[9])){
											//SE GUARDA EL INDICATIVO RIPS PARA LA AUTORIZACION
											encuentraValoresAutorizacion=true;
											UtilidadTransaccion.getTransaccion().begin();
											AutorizacionesEntSubRips autor=new AutorizacionesEntSubRips();
											autor.setFecha(UtilidadFecha.getFechaActualTipoBD());
											autor.setHora(UtilidadFecha.getHoraActual());
											Usuarios usu=new Usuarios();
											usu.setLogin(dtoFiltro.getUsuario().getLoginUsuario());
											autor.setUsuarios(usu);
											autor.setAutorizacionEntidadSub(dtoAutoriz.getConsecutivoAutorizacion());
											autorEntSubRips.guardarAutorizacionEntSubRips(autor);
											UtilidadTransaccion.getTransaccion().commit();
											
										}
										
										
									}
									
								}
										
							}
							if(!encuentraAutorizacion)
								this.generarInconsistenciaCampos(ConstantesBD.ripsAH,ConstantesIntegridadDominio.acronimoAutorizacionNoEncontrada,"Número de Autorización",contador);
							if(encuentraAutorizacion&&!noSeEncuentraAlmacenado)
								this.generarInconsistenciaCampos(ConstantesBD.ripsAH,ConstantesIntegridadDominio.acronimoAutorizacionProcesadaConAnterioridad,"Número de Autorización",contador);
							if(encuentraAutorizacion&&noSeEncuentraAlmacenado&&!encuentraValoresAutorizacion)
								this.generarInconsistenciaCampos(ConstantesBD.ripsAH,ConstantesIntegridadDominio.acronimoValoresAutorizacionNoValidos,"",contador);
							
						}
						//SIGUIENTE LINEA DEL ARCHIVO UH
						cadenaAH=bufferAH.readLine();
					}//fin while cadena AH
					bufferAH.close();*/
				}	
				
				
				if(!encontroUsuario){
					this.generarInconsistenciaCampos(ConstantesBD.ripsUS,ConstantesIntegridadDominio.acronimoUsuarioNoEncontrado,"Tipo y/o Número Identificación",contador);
				}
				
					//SIGUIENTE LINEA DEL ARCHIVO US
					cadenaUS=bufferUS.readLine();
				
			}
			
			//***************CERRAR ARCHIVO****************************
			bufferUS.close();
		}catch(FileNotFoundException e)
		{
			Log4JManager.error("No se pudo encontrar el archivo "+dtoFiltro.getArchivoUS().getFileName()+" al cargarlo: "+e);
		}
		catch(IOException e)
		{
			Log4JManager.error("Error en los streams del archivo "+dtoFiltro.getArchivoUS().getFileName()+" al cargarlo: "+e);
		}
	}
	
	/**
	 * Método que realiza la validacion sintáctica de los campos de los archivos RIPS
	 * @param campo
	 * @param nombreArchivo
	 * @param numeroLinea
	 * @param esNumerico
	 * @param elementoInconsistencia
	 * @param posIncon
	 * @param esRequerido
	 * @return
	 */
	private boolean validacionGeneralCampo(
			String campo, //contenido del campo 
			String nombreCampo, //nombre del campo 
			int tamanio, //tamaño que debe tener el campo 
			String nombreArchivo, //nombre del archivo que se está evaluando
			int numeroLinea, //numero de línea del registro del archivo que se está evaluando
			boolean esNumerico, //para saber si el campo es numérico 
			boolean esDecimal, //para saber si es decimal
			boolean esFecha, //para saber si es cmapo tipo fecha
			boolean esHora, // para saber si es campo tipo hora
			boolean esRequerido //para saber si el campo es requerido 
			) 
	{
		ArrayList<String> observaciones = new ArrayList<String>();
		boolean error=false;
		
		
		//verificacion de caracteres especiales dependiendo del tipo de campo
		if(esNumerico&&!esDecimal) //solo aplica para los numéricos
		{
			if(UtilidadCadena.tieneCaracteresEspecialesNumericoRips(campo))
				observaciones.add(ConstantesIntegridadDominio.acronimoCampoNumericoNoValido);
			else if(!campo.trim().equals("")) //si no esta vacío se verifica que sea entero
			{
				try
				{
					Integer.parseInt(campo.trim());
				}
				catch(Exception e)
				{
					observaciones.add(ConstantesIntegridadDominio.acronimoCampoNumericoNoValido);
				}
			}
		}
		else
		if(esDecimal){
			String[] numerico = campo.split("\\.");
				
				if(esDecimal)
				{
					if(numerico.length>1)
					{
						if(numerico.length!=2)
							observaciones.add(ConstantesIntegridadDominio.acronimoCampoDecimalNoValido);
						else
						{
							if(numerico[1].trim().length()>2)
								observaciones.add(ConstantesIntegridadDominio.acronimoCampoDecimalNoValido);
							
							try
							{
								Integer.parseInt(numerico[0].trim());
							}
							catch(Exception e)
							{
								observaciones.add(ConstantesIntegridadDominio.acronimoCampoDecimalNoValido);
							}
						}
					}
				}
				
				
			
			
		}
		else if(!esFecha&&!esHora) //solo aplica para los de tipo texto
		{
			if(UtilidadCadena.tieneCaracteresEspecialesTextoRips(campo))
				observaciones.add(ConstantesIntegridadDominio.acronimoCampoNoValido);
		}
		//se verifica la longitud
		if((campo.length()>tamanio))
			observaciones.add(ConstantesIntegridadDominio.acronimoLongitudCampoNoValida);
		
		//Se verifica la alineacion si no es de tipo fecha , ni hora
		if(!esFecha&&!esHora&&!UtilidadCadena.alineacionEspaciosValidaCampoRips(campo, esNumerico))
			observaciones.add(ConstantesIntegridadDominio.acronimoCampoNoAlineado);
		
		//Si es Fecha se valida el formato de la fecha
		if(esFecha&&!UtilidadFecha.validarFecha(campo)&&!campo.trim().equals(""))
			observaciones.add(ConstantesIntegridadDominio.acronimoFormatoFechaNoValido);
		
		//Si es hora se valida el formato de la hora
		if(esHora&&!UtilidadFecha.validacionHora(campo).puedoSeguir&&!campo.trim().equals(""))
			observaciones.add(ConstantesIntegridadDominio.acronimoFormatoHoraNoValido);
		
		//Validacion para saber si es requerido
		if(campo.trim().equals("")&&esRequerido)
			observaciones.add(ConstantesIntegridadDominio.acronimoCampoEsRequerido);
		
		
		//Se edita cada observacion como inconsistencia 
		for(int i=0;i<observaciones.size();i++)
		{
			this.generarInconsistenciaCampos(nombreArchivo, observaciones.get(i), nombreCampo, numeroLinea);
			error=true;
		}
	
		return error;
	}
	
	
	private void generarInconsistencia(String nombreArchivo, String tipoInconsistencia){
		DtoInconsistenciasRipsEntidadesSub dtoInconsistencias = new DtoInconsistenciasRipsEntidadesSub();
		dtoInconsistencias.setNombreArchivo(nombreArchivo);
		dtoInconsistencias.setObservaciones(tipoInconsistencia);
		listaInconsistencias.add(dtoInconsistencias);
	}
	
	private void generarInconsistenciaCampos(String nombreArchivo, String tipoInconsistencia, String campo, int numeroFila){
		DtoInconsistenciasRipsEntidadesSub dtoInconsistencias = new DtoInconsistenciasRipsEntidadesSub();
		dtoInconsistencias.setNombreArchivo(nombreArchivo);
		dtoInconsistencias.setObservaciones(tipoInconsistencia);
		dtoInconsistencias.setCampo(campo);
		dtoInconsistencias.setNumeroFila(numeroFila);
		listaInconsistenciasCampos.add(dtoInconsistencias);
	}
	
	private void agregarValoresCampos(String nombreArchivo, String valor, String campo, int numeroFila){
		DtoInconsistenciasRipsEntidadesSub dtoValoresCampos = new DtoInconsistenciasRipsEntidadesSub();
		dtoValoresCampos.setNombreArchivo(nombreArchivo);
		dtoValoresCampos.setObservaciones(valor);
		dtoValoresCampos.setCampo(campo);
		dtoValoresCampos.setNumeroFila(numeroFila);
		listaValoresCampos.add(dtoValoresCampos);
	}
	
	
	/**
	 * 
	 * Este Método se encarga de insertar en la base de datos
	 * un registro de log del proceso de rips entidades subcontratadas
	 * 
	 * @param logRipsEntSub log generado en el proceso
	 * @return boolean
	 * @author, Fabián Becerra
	 *
	 */
	public boolean guardarLogRipsEntidadesSub(LogRipsEntidadesSub logRipsEntSub){
		return dao.guardarLogRipsEntidadesSub(logRipsEntSub);
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar los
	 * registros de log del proceso de rips entidades subcontratadas
	 * 
	 * @param dtoFiltroConsultaProcesoRips parámetros para la consulta
	 * @return ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>
	 * @author, Fabián Becerra
	 *
	 */
	public ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> consultarRegistrosLogRipsEntidadesSub(DtoFiltroConsultaProcesoRipsEntidadesSub dtoFiltroConsultaProcesoRips){
		return dao.consultarRegistrosLogRipsEntidadesSub(dtoFiltroConsultaProcesoRips);
	}
	
	
	/**
	 * 
	 * Este Método se encarga de consultar los
	 * registros de log del proceso de rips entidades subcontratadas
	 * por su codigo pk para ser mostrado en el detalle de las consultas
	 * 
	 * @param codigoPkLogSeleccionado codigo pk de log rips entidades sub
	 * @return ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>
	 * @author, Fabián Becerra
	 *
	 */
	public ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> consultarRegistrosLogRipsEntidadesSubPorCodigoPk(long codigoPkLogSeleccionado){
		return dao.consultarRegistrosLogRipsEntidadesSubPorCodigoPk(codigoPkLogSeleccionado);
	}
	
	/**
	 * 
	 * Este Método se encarga de ordenar los
	 * registros de log del proceso de rips entidades subcontratadas
	 * para mostrar su detalle en la página de consulta
	 * 
	 * @param codigoPkLogSeleccionado codigo pk de log rips entidades sub
	 * @return DtoResultadoConsultaLogRipsEntidadesSub
	 * @author, Fabián Becerra
	 *
	 */
	public DtoResultadoConsultaLogRipsEntidadesSub ordenarRegistrosParaDetalleLogRips(long codigoPkLogSeleccionado){
		
		ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> listaLogsEntSub=this.consultarRegistrosLogRipsEntidadesSubPorCodigoPk(codigoPkLogSeleccionado);
		DtoResultadoConsultaLogRipsEntidadesSub dtoRespuesta= new DtoResultadoConsultaLogRipsEntidadesSub();
		if(!Utilidades.isEmpty(listaLogsEntSub)){
			
			
			dtoRespuesta.setFechaFormateada(UtilidadFecha.conversionFormatoFechaAAp(listaLogsEntSub.get(0).getFechaProceso()));
			dtoRespuesta.setHoraProceso(listaLogsEntSub.get(0).getHoraProceso());
			dtoRespuesta.setRazonSocialEntidadSub(listaLogsEntSub.get(0).getRazonSocialEntidadSub());
			dtoRespuesta.setUsuarioProceso(listaLogsEntSub.get(0).getPrimerApellidoUsuarioProceso()
					+" "+((UtilidadTexto.isEmpty(listaLogsEntSub.get(0).getSegundoApellidoUsuarioProceso()))?"":listaLogsEntSub.get(0).getSegundoApellidoUsuarioProceso())
					+" "+((UtilidadTexto.isEmpty(listaLogsEntSub.get(0).getPrimerNombreUsuarioProceso()))?"":listaLogsEntSub.get(0).getPrimerNombreUsuarioProceso())
					+" "+((UtilidadTexto.isEmpty(listaLogsEntSub.get(0).getSegundoNombreUsuarioProceso()))?"":listaLogsEntSub.get(0).getSegundoNombreUsuarioProceso()))
					;
			
			String[] tipoCodMed = new String[]{listaLogsEntSub.get(0).getAcronimoCodificacionMedicaInsu()};
			
			Connection con=UtilidadBD.abrirConexion();
			ArrayList<DtoIntegridadDominio> tipoCodMedInsu =Utilidades.generarListadoConstantesIntegridadDominio(
					con, tipoCodMed, false);
			UtilidadBD.closeConnection(con);
			
			dtoRespuesta.setAcronimoCodificacionMedicaInsu(tipoCodMedInsu.get(0).getDescripcion());
			dtoRespuesta.setCodificacionServicios(listaLogsEntSub.get(0).getCodificacionServicios());
			
			ArrayList<String> archivos=new ArrayList<String>();
			boolean inconsistenciasCampos=false;
			boolean inconsistenciasArchivos=false;
			for(DtoResultadoConsultaLogRipsEntidadesSub log:listaLogsEntSub){
				if(!archivos.contains(log.getNombreArchivo())){
					long inconsistencias=0;
					for(DtoResultadoConsultaLogRipsEntidadesSub logSub:listaLogsEntSub){
						if(log.getNombreArchivo().equals(logSub.getNombreArchivo())
								&&logSub.getCodigoPkInconsisCamp()!=null){
							inconsistencias++;
							inconsistenciasCampos=true;
						}
						if(log.getNombreArchivo().equals(logSub.getNombreArchivo())
								&&logSub.getCodigoPkInconsisArch()!=null&&logSub.getCodigoPkInconsisCamp()==null){
							inconsistenciasArchivos=true;
						}
						
					}
					log.setNumeroInconsistencias(inconsistencias);
					dtoRespuesta.getDtoResultadoArchivos().add(log);
					archivos.add(log.getNombreArchivo());
				}
			}
			if(!inconsistenciasCampos){
				if(inconsistenciasArchivos){
					String archivosObservaciones="";
					for(DtoResultadoConsultaLogRipsEntidadesSub logSub:listaLogsEntSub){
						archivosObservaciones+=logSub.getNombreArchivo();
						archivosObservaciones+="\n";
					}
					dtoRespuesta.setObservaciones(archivosObservaciones);
				}
			}
			
			
		}
		
		
		return dtoRespuesta;
	}
	
	
	/**
	 * 
	 * Este Método se encarga de consultar los
	 * registros de log del proceso de rips entidades subcontratadas por archivo
	 * por su codigo pk y el codigo pk del archivo seleccionado
	 * 
	 * @param codigoPkLogSeleccionado codigo pk de log rips entidades sub
	 * @param codigoPkLogArchivoSeleccionado codigo pk de log rips entidades sub archivo
	 * @return ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>
	 * @author, Fabián Becerra
	 *
	 */
	
	public ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> consultarRegistrosLogRipsEntidadesSubPorCodigoPkyArchivo(long codigoPkLogSeleccionado, long codigoPkLogArchivoSeleccionado){
		return dao.consultarRegistrosLogRipsEntidadesSubPorCodigoPkyArchivo(codigoPkLogSeleccionado, codigoPkLogArchivoSeleccionado);
	}
	
	/**
	 * 
	 * Este Método se encarga de organizar la información de la consulta
	 * de log del proceso de rips entidades subcontratadas de un archivo
	 * por su codigo pk y el codigo pk del archivo seleccionado
	 * 
	 * @param codigoPkLogSeleccionado codigo pk de log rips entidades sub
	 * @param codigoPkLogArchivoSeleccionado codigo pk de log rips entidades sub archivo
	 * @return DtoResultadoConsultaLogRipsEntidadesSub
	 * @author, Fabián Becerra
	 *
	 */
	
	public DtoResultadoConsultaLogRipsEntidadesSub ordenarRegistrosLogRipsEntidadesSubPorArchivo(long codigoPkLogSeleccionado, long codigoPkLogArchivoSeleccionado){
		
		ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> listaConsultaLogArchivo= new ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>();
		listaConsultaLogArchivo=this.consultarRegistrosLogRipsEntidadesSubPorCodigoPkyArchivo(codigoPkLogSeleccionado, codigoPkLogArchivoSeleccionado);
		
		DtoResultadoConsultaLogRipsEntidadesSub dtoOrdenadoPorArchivo=new DtoResultadoConsultaLogRipsEntidadesSub();
		ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> listaValoresCampos= new ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>();
		ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> listaPorRegistros= new ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>();
		ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> listaNombresCampos= new ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>();
		
		dtoOrdenadoPorArchivo.setNombreArchivo(listaConsultaLogArchivo.get(0).getNombreArchivo());
		
		//GUARDAR LOS NOMBRES DE LOS CAMPOS PARA MOSTRARLOS COMO TITULO
		ArrayList<String> nombresCampo=new ArrayList<String>();
		for(DtoResultadoConsultaLogRipsEntidadesSub logRegistro:listaConsultaLogArchivo){
			if(!nombresCampo.contains(logRegistro.getCampoMostrar())){
				if(!logRegistro.getCampoMostrar().equals(ConstantesCamposProcesoRipsEntidadesSub.valorArchivosAutorizaciones)){
					nombresCampo.add(logRegistro.getCampoMostrar());
					listaNombresCampos.add(logRegistro);
				}
			}
		}
		
		
		//GUARDAR LOS VALORES DE LOS CAMPOS UNA SOLA VEZ POR VARIAS INCONSISTENCIAS
		ArrayList<Long> codigosPkRegistros= new ArrayList<Long>();
		ArrayList<Long> codigosPkValoresCampos= new ArrayList<Long>();
		for(DtoResultadoConsultaLogRipsEntidadesSub logRegistro:listaConsultaLogArchivo){
			if(!codigosPkRegistros.contains(logRegistro.getCodigoPkLogRegistro())){
				
				listaValoresCampos= new ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>();
				codigosPkValoresCampos= new ArrayList<Long>();
				for(DtoResultadoConsultaLogRipsEntidadesSub logCampos:listaConsultaLogArchivo){
					if(!codigosPkValoresCampos.contains(logCampos.getCodigoPkValorCampo())){
						if(logRegistro.getCodigoPkLogRegistro().intValue()==logCampos.getCodigoPkLogRegistro().intValue()){
							if(!logCampos.getCampoMostrar().equals(ConstantesCamposProcesoRipsEntidadesSub.valorArchivosAutorizaciones)){
								listaValoresCampos.add(logCampos);
							}
							
						}
						codigosPkValoresCampos.add(logCampos.getCodigoPkValorCampo());
					}
				}
				logRegistro.setDtoResultadoLineasValores(listaValoresCampos);
				listaPorRegistros.add(logRegistro);
				codigosPkRegistros.add(logRegistro.getCodigoPkLogRegistro());
			}
		}
		
		
		//dtoOrdenadoPorArchivo.setDtoResultadoArchivos(listaConsultaLogArchivo);
		dtoOrdenadoPorArchivo.setDtoResultadoLineasValores(listaPorRegistros);
		dtoOrdenadoPorArchivo.setDtoResultadoNombresCampos(listaNombresCampos);
		ArrayList<Long> codigosPkInconsistencias= new ArrayList<Long>();
		Connection con=UtilidadBD.abrirConexion();
		ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> listaInconsistencias= new ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>();
		//GUARDAR LAS INCONSISTENCIAS POR REGISTRO
		for(DtoResultadoConsultaLogRipsEntidadesSub logRegistro:dtoOrdenadoPorArchivo.getDtoResultadoLineasValores()){
			listaInconsistencias= new ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>();
			for(DtoResultadoConsultaLogRipsEntidadesSub log:listaConsultaLogArchivo){
				if(log.getCodigoPkLogRegistro().intValue()==logRegistro.getCodigoPkLogRegistro()){
					if(!codigosPkInconsistencias.contains(log.getCodigoPkInconsisCamp())){
						
						String[] listadoTipoInconsistencia = new String[]{log.getTipoInconsistenciaCampo()};
						
						ArrayList<DtoIntegridadDominio> valor=Utilidades.generarListadoConstantesIntegridadDominio(
								con, listadoTipoInconsistencia, false);
						
						
						if(UtilidadTexto.isEmpty(log.getNombreCampoInconsistencia())){
							log.setTipoInconsistenciaCampo(valor.get(0).getDescripcion());
						}else{
							if(log.getNombreCampoInconsistencia().contains(ConstantesBD.separadorSplit)){
								String []valores=log.getNombreCampoInconsistencia().split(ConstantesBD.separadorSplit);
								log.setTipoInconsistenciaCampo(valor.get(0).getDescripcion()+" en el periodo "+valores[1]);
								log.setNombreCampoInconsistencia(valores[0]);
							}else
							{
								log.setTipoInconsistenciaCampo(valor.get(0).getDescripcion());
							}
						}
						
						listaInconsistencias.add(log);
						codigosPkInconsistencias.add(log.getCodigoPkInconsisCamp());
					}
				
				}
				
			}
			logRegistro.setDtoResultadoInconsistenciasPorRegistro(listaInconsistencias);
		}
		UtilidadBD.closeConnection(con);
		
		return dtoOrdenadoPorArchivo;
	}
	
	
	/**
	 * 
	 * Este Método se encarga de consultar los
	 * registros log del proceso de rips entidades subcontratadas
	 * para mostrarlos en el detalle por autorizaciones
	 * 
	 * @param codigoPkLogSeleccionado codigo pk de log rips entidades sub
	 * @return ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>
	 * @author, Fabián Becerra
	 *
	 */
	public ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> consultarRegistrosLogRipsEntidadesSubPorNumerosAutorizacion(long codigoPkLogSeleccionado){
		return dao.consultarRegistrosLogRipsEntidadesSubPorNumerosAutorizacion(codigoPkLogSeleccionado);
	}
	
	/**
	 * 
	 * Este Método se encarga de ordenar la consulta de
	 * registros log del proceso de rips entidades subcontratadas
	 * para mostrarlos en el detalle por autorizaciones
	 * 
	 * @param codigoPkLogSeleccionado codigo pk de log rips entidades sub
	 * @return DtoResultadoConsultaLogRipsEntidadesSub
	 * @author, Fabián Becerra
	 *
	 */
	public DtoResultadoConsultaLogRipsEntidadesSub ordenarConsultaRegistrosLogRipsEntidadesSubPorNumerosAutorizacion(long codigoPkLogSeleccionado){
		ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> listaConsulta=this.consultarRegistrosLogRipsEntidadesSubPorNumerosAutorizacion(codigoPkLogSeleccionado);
		DtoResultadoConsultaLogRipsEntidadesSub dtoResultado=new DtoResultadoConsultaLogRipsEntidadesSub();	
		ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> listaMostrar=new ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>();	
		ArrayList<Long> codigoPkValoresCampos=new ArrayList<Long>();
		DtoResultadoProcesarRipsAutorizacion dtoPorAutor=new DtoResultadoProcesarRipsAutorizacion();
		IAutorizacionesEntidadesSubServicio servicioAutorizacion=ManejoPacienteServicioFabrica.crearAutorizacionEntidadesSubServicio();
		AutorizacionesEntidadesSub autoriz=new AutorizacionesEntidadesSub();
		IPersonasServicio servicioPersonas=AdministracionFabricaServicio.crearPersonaServicio();
		DtoPersonas paciente=new DtoPersonas();
		SolicitudesHibernateDAO dao; dao=new SolicitudesHibernateDAO();
		IAutorizacionesEntSubArticuloServicio servicioArticulo=ManejoPacienteServicioFabrica.crearAutorizacionesEntSubArticulo();
		IAutorizacionesEntSubServiServicio servicioAutorizacionServicio=ManejoPacienteServicioFabrica.crearAutorizacionesEntSubServiServicio();
		boolean mostrarAutorizacion=false;
		
		for(DtoResultadoConsultaLogRipsEntidadesSub dtoConsulta:listaConsulta){
			
			dtoPorAutor=new DtoResultadoProcesarRipsAutorizacion();
			for(DtoResultadoConsultaLogRipsEntidadesSub dtoConsultaSub:listaConsulta){
				if(dtoConsultaSub.getCodigoPkLogRegistro().intValue()==dtoConsulta.getCodigoPkLogRegistro().intValue()){
					if(!codigoPkValoresCampos.contains(dtoConsultaSub.getCodigoPkValorCampo())){
						
						if(dtoConsultaSub.getCampoMostrar().equals(ConstantesCamposProcesoRipsEntidadesSub.numeroAutorizacion))
							dtoPorAutor.setAutorizacion(dtoConsultaSub.getValorCampoMostrar());
						else
							if(dtoConsultaSub.getCampoMostrar().equals(ConstantesCamposProcesoRipsEntidadesSub.tipoIdUsuario))
									dtoPorAutor.setTipoIDUsuario(dtoConsultaSub.getValorCampoMostrar());
							else
								if(dtoConsultaSub.getCampoMostrar().equals(ConstantesCamposProcesoRipsEntidadesSub.numeroIdUsuario))
										dtoPorAutor.setNumeroIDUsuario(dtoConsultaSub.getValorCampoMostrar());
								else
									if(dtoConsultaSub.getCampoMostrar().equals(ConstantesCamposProcesoRipsEntidadesSub.valorArchivosAutorizaciones))
										dtoPorAutor.setValor(dtoConsultaSub.getValorCampoMostrar());
							
						if(dtoConsultaSub.getCodigoPkInconsisCamp()!=null){
							mostrarAutorizacion=true;
						}
						
						codigoPkValoresCampos.add(dtoConsultaSub.getCodigoPkValorCampo());
					}
				}
			}
			
			autoriz=servicioAutorizacion.obtenerAutorizacionEntSubPorConsecutivoAutorizacion(dtoPorAutor.getAutorizacion());
			if(autoriz!=null){
				dtoPorAutor.setFechaAutorizacion(UtilidadFecha.conversionFormatoFechaAAp(autoriz.getFechaAutorizacion()));
				DtoAutorizacionEntSubcontratadasCapitacion dtoParametros=new DtoAutorizacionEntSubcontratadasCapitacion();
				dtoParametros.setAutorizacion(Utilidades.convertirALong(dtoPorAutor.getAutorizacion()));
				
				//FIXME validar si carga bien
				Integer numeroSolicitud = null;
				@SuppressWarnings("rawtypes")
				Iterator itSolicitudes = autoriz.getAutoEntsubSolicitudeses().iterator();
				while(itSolicitudes.hasNext()){
					AutoEntsubSolicitudes solicitudes = (AutoEntsubSolicitudes)itSolicitudes.next();
					numeroSolicitud = solicitudes.getSolicitudes().getNumeroSolicitud();
					break;					
				}
				// Solicitudes sol=dao.obtenerSolicitudPorId(autoriz.getSolicitudes().getNumeroSolicitud());
				Solicitudes sol=null;
				if(numeroSolicitud!=null){
					sol=dao.obtenerSolicitudPorId(numeroSolicitud);
				}
				
				
				if(sol!=null){
					
					try {
						Connection con=UtilidadBD.abrirConexion();
						String nombreTipo=Utilidades.obtenerNombreTipoSolicitud(con,sol.getTiposSolicitud().getCodigo()+"");
						dtoPorAutor.setTipoOrden(nombreTipo);
						UtilidadBD.cerrarConexion(con);
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}else{
					
					ArrayList<DtoArticulosAutorizaciones> listaArticulos=servicioArticulo.listarautorizacionesEntSubArticuPorAutoEntSub(dtoParametros);
					if(!Utilidades.isEmpty(listaArticulos)){
						dtoPorAutor.setTipoOrden("Medicamentos e Insumos");
					}else
					{
						ArrayList<AutorizacionesEntSubServi> listaServicios=servicioAutorizacionServicio.listarAutorizacionesEntSubServiPorAutoEntSub(dtoParametros);
						if(!Utilidades.isEmpty(listaServicios)){
							dtoPorAutor.setTipoOrden("Servicios");
						}else{
							dtoPorAutor.setTipoOrden("");
						}
					}
				}
			}else{
				dtoPorAutor.setFechaAutorizacion("");
				dtoPorAutor.setTipoOrden("");
			}
			
			dtoPorAutor.setMostrarAutorizacion(mostrarAutorizacion);
			
			paciente=servicioPersonas.buscarPersona(dtoPorAutor.getTipoIDUsuario(), dtoPorAutor.getNumeroIDUsuario());
			if(paciente!=null){
				dtoPorAutor.setPrimerNombrePaciente(paciente.getPrimerNombre());
				dtoPorAutor.setSegundoNombrePaciente((UtilidadTexto.isEmpty(paciente.getSegundoNombre())?"":paciente.getSegundoNombre()));
				dtoPorAutor.setPrimerApellidoPaciente(paciente.getPrimerApellido());
				dtoPorAutor.setSegundoApellidoPaciente((UtilidadTexto.isEmpty(paciente.getSegundoApellido())?"":paciente.getSegundoApellido()));
			}else{
				dtoPorAutor.setPrimerNombrePaciente("");
				dtoPorAutor.setSegundoNombrePaciente("");
				dtoPorAutor.setPrimerApellidoPaciente("");
				dtoPorAutor.setSegundoApellidoPaciente("");
			}
			
			
			dtoConsulta.setDtoListaPorAutorizaciones(dtoPorAutor);
		}
		
		for(DtoResultadoConsultaLogRipsEntidadesSub dtoConsulta:listaConsulta){
			if(!UtilidadTexto.isEmpty(dtoConsulta.getDtoListaPorAutorizaciones().getAutorizacion()))
			{
				listaMostrar.add(dtoConsulta);
			}
		}
		
		
		
		
		
		dtoResultado.setDtoResultadoArchivos(listaMostrar);
		return dtoResultado;
	}
	
	
	
	/**
	 * 
	 * Este Método se encarga de consultar un registro
	 * log del proceso de rips entidades subcontratadas
	 * por su codigopk, el codigo del archivo y el del registro
	 * 
	 * @param codigoPkLogSeleccionado codigo pk de log rips entidades sub
	 * @param codigoPkArchivo codigo pk de log rips entidades sub de archivo
	 * @param codigoPkRegistro codigo pk de log rips entidades sub de registro
	 * @return ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>
	 * @author, Fabián Becerra
	 *
	 */
	public ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> consultarRegistrosLogRipsEntidadesSubParaDetallePorNumeroAutorizacion(long codigoPkLogSeleccionado, long codigoPkArchivo, long codigoPkRegistro){
		return dao.consultarRegistrosLogRipsEntidadesSubParaDetallePorNumeroAutorizacion(codigoPkLogSeleccionado, codigoPkArchivo, codigoPkRegistro);
	}
	
	
	/**
	 * 
	 * Este Método se encarga de ordenar la consulta
	 * log del proceso de rips entidades subcontratadas
	 * por su codigopk, el codigo del archivo y el del registro
	 * para mostrarlo en el detalle por autorizaciones
	 * 
	 * @param codigoPkLogSeleccionado codigo pk de log rips entidades sub
	 * @param codigoPkArchivo codigo pk de log rips entidades sub de archivo
	 * @param codigoPkRegistro codigo pk de log rips entidades sub de registro
	 * @return ArrayList<DtoResultadoConsultaLogRipsEntidadesSub>
	 * @author, Fabián Becerra
	 *
	 */
	public DtoResultadoConsultaLogRipsEntidadesSub ordenarConsultaRegistrosLogRipsEntidadesSubParaDetallePorNumeroAutorizacion(long codigoPkLogSeleccionado, long codigoPkArchivo, long codigoPkRegistro){
		ArrayList<DtoResultadoConsultaLogRipsEntidadesSub> listaConsulta;
		DtoResultadoConsultaLogRipsEntidadesSub dtoResultado= new DtoResultadoConsultaLogRipsEntidadesSub();
		
		listaConsulta=this.consultarRegistrosLogRipsEntidadesSubParaDetallePorNumeroAutorizacion(codigoPkLogSeleccionado, codigoPkArchivo, codigoPkRegistro);
		
		DtoResultadoProcesarRipsAutorizacion dtoPorAutor=new DtoResultadoProcesarRipsAutorizacion();
		ArrayList<Long> codigoPkValoresCampos=new ArrayList<Long>();
		IAutorizacionesEntidadesSubServicio servicioAutorizacion=ManejoPacienteServicioFabrica.crearAutorizacionEntidadesSubServicio();
		AutorizacionesEntidadesSub autoriz=new AutorizacionesEntidadesSub();
		IPersonasServicio servicioPersonas=AdministracionFabricaServicio.crearPersonaServicio();
		DtoPersonas paciente=new DtoPersonas();
		
		//CARGAR LOS DATOS DE LA AUTORIZACION
		for(DtoResultadoConsultaLogRipsEntidadesSub dtoConsulta:listaConsulta){
			
				if(dtoConsulta.getCodigoPkLogRegistro().intValue()==dtoConsulta.getCodigoPkLogRegistro().intValue()){
					if(!codigoPkValoresCampos.contains(dtoConsulta.getCodigoPkValorCampo())){
						
						if(dtoConsulta.getCampoMostrar().equals(ConstantesCamposProcesoRipsEntidadesSub.numeroAutorizacion))
							dtoPorAutor.setAutorizacion(dtoConsulta.getValorCampoMostrar());
						else
							if(dtoConsulta.getCampoMostrar().equals(ConstantesCamposProcesoRipsEntidadesSub.tipoIdUsuario))
									dtoPorAutor.setTipoIDUsuario(dtoConsulta.getValorCampoMostrar());
							else
								if(dtoConsulta.getCampoMostrar().equals(ConstantesCamposProcesoRipsEntidadesSub.numeroIdUsuario))
										dtoPorAutor.setNumeroIDUsuario(dtoConsulta.getValorCampoMostrar());
								
						codigoPkValoresCampos.add(dtoConsulta.getCodigoPkValorCampo());
					}
				}
			
		}	
			autoriz=servicioAutorizacion.obtenerAutorizacionEntSubPorConsecutivoAutorizacion(dtoPorAutor.getAutorizacion());
			if(autoriz!=null){
				dtoPorAutor.setFechaAutorizacion(UtilidadFecha.conversionFormatoFechaAAp(autoriz.getFechaAutorizacion()));
				DtoAutorizacionEntSubcontratadasCapitacion dtoParametros=new DtoAutorizacionEntSubcontratadasCapitacion();
				dtoParametros.setAutorizacion(Utilidades.convertirALong(dtoPorAutor.getAutorizacion()));
				
			}else{
				dtoPorAutor.setFechaAutorizacion("");
			}
			
			
			paciente=servicioPersonas.buscarPersona(dtoPorAutor.getTipoIDUsuario(), dtoPorAutor.getNumeroIDUsuario());
			if(paciente!=null){
				dtoPorAutor.setPrimerNombrePaciente(paciente.getPrimerNombre());
				dtoPorAutor.setSegundoNombrePaciente((UtilidadTexto.isEmpty(paciente.getSegundoNombre())?"":paciente.getSegundoNombre()));
				dtoPorAutor.setPrimerApellidoPaciente(paciente.getPrimerApellido());
				dtoPorAutor.setSegundoApellidoPaciente((UtilidadTexto.isEmpty(paciente.getSegundoApellido())?"":paciente.getSegundoApellido()));
			}else{
				dtoPorAutor.setPrimerNombrePaciente("");
				dtoPorAutor.setSegundoNombrePaciente("");
				dtoPorAutor.setPrimerApellidoPaciente("");
				dtoPorAutor.setSegundoApellidoPaciente("");
			}
			
			
			
			/*************************CARGAR LAS INCONSISTENCIAS*************************/
			ArrayList<Long> codigoPkInconsisCampos=new ArrayList<Long>();
			DtoInconsistenciasRipsEntidadesSub dtoInconsistencia=new DtoInconsistenciasRipsEntidadesSub();
			ArrayList<DtoInconsistenciasRipsEntidadesSub> listaInconsistencias=new ArrayList<DtoInconsistenciasRipsEntidadesSub>();
			Connection con=UtilidadBD.abrirConexion();
			for(DtoResultadoConsultaLogRipsEntidadesSub dtoConsulta:listaConsulta){
				
				for(DtoResultadoConsultaLogRipsEntidadesSub dtoConsultaSub:listaConsulta){
					if(dtoConsultaSub.getCodigoPkLogRegistro().intValue()==dtoConsulta.getCodigoPkLogRegistro().intValue()){
						if(!codigoPkInconsisCampos.contains(dtoConsultaSub.getCodigoPkInconsisCamp())){
							
							dtoInconsistencia=new DtoInconsistenciasRipsEntidadesSub();
							//dtoInconsistencia.setCampo(dtoConsultaSub.getNombreCampoInconsistencia());
							
							String[] listadoTipoInconsistencia = new String[]{dtoConsultaSub.getTipoInconsistenciaCampo()};
							
							ArrayList<DtoIntegridadDominio> valor=Utilidades.generarListadoConstantesIntegridadDominio(
									con, listadoTipoInconsistencia, false);
							
							
							if(UtilidadTexto.isEmpty(dtoConsultaSub.getNombreCampoInconsistencia())){
								dtoInconsistencia.setObservaciones(valor.get(0).getDescripcion());
								dtoInconsistencia.setCampo(dtoConsultaSub.getNombreCampoInconsistencia());
							}else{
								if(dtoConsultaSub.getNombreCampoInconsistencia().contains(ConstantesBD.separadorSplit)){
									String []valores=dtoConsultaSub.getNombreCampoInconsistencia().split(ConstantesBD.separadorSplit);
									dtoInconsistencia.setObservaciones(valor.get(0).getDescripcion()+" en el periodo "+valores[1]);
									dtoInconsistencia.setCampo(valores[0]);
								}else
								{
									dtoInconsistencia.setObservaciones(valor.get(0).getDescripcion());
									dtoInconsistencia.setCampo(dtoConsultaSub.getNombreCampoInconsistencia());
								}
							}
							
							//dtoInconsistencia.setObservaciones(valor.get(0).getDescripcion());
							dtoInconsistencia.setNombreArchivo(dtoConsultaSub.getNombreArchivo());
							
							for(DtoResultadoConsultaLogRipsEntidadesSub dtoConsultaServicio:listaConsulta){
								
								if(dtoConsultaServicio.getCampoMostrar().equals(ConstantesCamposProcesoRipsEntidadesSub.nombreDeServicio)
										||dtoConsultaSub.getCampoMostrar().equals(ConstantesCamposProcesoRipsEntidadesSub.nombreGenericoMedicamento)){
									dtoInconsistencia.setNombreServicioMed(dtoConsultaServicio.getValorCampoMostrar());
								}
								if(dtoConsultaServicio.getCampoMostrar().equals(ConstantesCamposProcesoRipsEntidadesSub.codigoDeConcepto)
										||dtoConsultaServicio.getCampoMostrar().equals(ConstantesCamposProcesoRipsEntidadesSub.codigoDeConsulta)
										||dtoConsultaServicio.getCampoMostrar().equals(ConstantesCamposProcesoRipsEntidadesSub.codigoDeMedicamento)
										||dtoConsultaServicio.getCampoMostrar().equals(ConstantesCamposProcesoRipsEntidadesSub.codigoDeProcedimiento)
										||dtoConsultaServicio.getCampoMostrar().equals(ConstantesCamposProcesoRipsEntidadesSub.codigoDeServicio)
										){
									dtoInconsistencia.setCodigoServicioMed(dtoConsultaServicio.getValorCampoMostrar());
								}
							}
							
							
							
							listaInconsistencias.add(dtoInconsistencia);
							
							
							codigoPkInconsisCampos.add(dtoConsultaSub.getCodigoPkInconsisCamp());
						}
					}
				}
				
				
			}
			UtilidadBD.closeConnection(con);
			
		dtoPorAutor.setListaInconsistencias(listaInconsistencias);
		dtoResultado.setDtoListaPorAutorizaciones(dtoPorAutor);
		
		
		return dtoResultado;
	}
}
