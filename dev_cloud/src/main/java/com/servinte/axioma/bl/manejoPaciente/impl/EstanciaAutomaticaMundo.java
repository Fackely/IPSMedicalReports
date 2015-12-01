package com.servinte.axioma.bl.manejoPaciente.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.apache.struts.action.ActionErrors;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadFecha;

import com.princetonsa.dto.manejoPaciente.DtoDetAutorizacionEst;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.cargos.CargosDirectos;
import com.princetonsa.mundo.facturacion.EstanciaAutomatica;
import com.princetonsa.mundo.manejoPaciente.Autorizaciones;
import com.princetonsa.mundo.medicamentos.DespachoMedicamentos;
import com.princetonsa.mundo.solicitudes.Solicitud;
import com.servinte.axioma.bl.manejoPaciente.interfaz.IEstanciaAutomaticaMundo;


/**
 * Clase que implementa métodos correpondientes a al negocio de estancia automatica
 *  
 * @author DiaRuiPe
 * @version 1.0
 * @created 01-Agos-2012 11:34:37 a.m.
 */

public class EstanciaAutomaticaMundo implements IEstanciaAutomaticaMundo {
	
	/**
	 * Método que permite generar la estancia automatica, generando
	 * la solicitud, los cargos y la estancia automatica.
	 * @throws Exception 
	 */
	@Override
	public int generarEstanciaAutomatica(Connection con, EstanciaAutomatica estanciaAutomatica, int pos, HashMap mapaCuentas, UsuarioBasico usuario, int servicio) throws Exception {
		
		try{
			String indica="Manual";
			
			int numeroSolicitud=this.insertarSolicitudEstancia(con,estanciaAutomatica,pos,mapaCuentas);
			int resp=0;
			//si se insertó la solicitud se hace insert en cargos directos
			if(numeroSolicitud>0) {
				estanciaAutomatica.setNumeroSolicitud(numeroSolicitud);
				resp=this.insertarCargoDirectoEstancia(con,numeroSolicitud,usuario,servicio);
			}
			//**********************************************************************************************************
			//se verifica si la transacción fue exitosa
			if(resp<=0) {
				//se genera la inconsistencia
				estanciaAutomatica.generarInconsistencias("B","Error del sistema al generar la solicitud",null,indica);
			}
			else {
				resp=this.insertarCargoEstancia(con,numeroSolicitud,usuario,servicio,pos,mapaCuentas,estanciaAutomatica,indica);
			}
			
			
			if(resp<=0)
			{
				Solicitud solicitud=new Solicitud();
				try
				{
					//se carga datos de la solicitud
					solicitud.cargar(con,numeroSolicitud);
					//se genera la inconsistencia
					estanciaAutomatica.generarInconsistencias("A","Solicitud sin cargo generado",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				}
				catch(SQLException e)
				{
					Log4JManager.info("Error al cargar solicitud en accionGenerarArea de EstanciaAutomaticaAction: "+e);
					//se genera la inconsistencia de todas maneras
					estanciaAutomatica.generarInconsistencias("A","Solicitud sin cargo generado","No encontrada",indica);
				}
			}
			
			if(resp<=0)
				estanciaAutomatica.generarInconsistencias("B","Sin Cargo Generado",null,indica);	
			return resp;
			
		}catch (Exception e) {
			Log4JManager.error(e.getMessage(),e);	
			throw e;
		
		}
		
	}
	
	/**
	 * Método para insertar una solicitud estancia en la tabla solicitudes
	 * @param con
	 * @param estancia
	 * @param pos
	 * @param mapaCuentas
	 * @return
	 */
	private int insertarSolicitudEstancia(Connection con, EstanciaAutomatica estancia, int pos, HashMap mapaCuentas) {
		//se instancia el objeto solicitud
		Solicitud solicitud=new Solicitud();
		solicitud.clean();
		solicitud.setFechaSolicitud(estancia.getFechaInicialEstancia());
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
	        //se instancia este objeto para cambiar el estado Historia Clinica de la solicitud
	    	int numeroSolicitud=solicitud.insertarSolicitudGeneralTransaccional(con, ConstantesBD.inicioTransaccion);
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
			return 0;
	    }
	}
	
	/**
	 * Método para insertar el cargo directo de la solicitud de Estancia
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
	 * @param estancia
	 * @return
	 */
	private int insertarCargoEstancia(Connection con, int numeroSolicitud, UsuarioBasico usuario, int servicio, int pos, HashMap mapaCuentas, EstanciaAutomatica estancia,String indica) {
		try
		{
			int idCuenta=Integer.parseInt(mapaCuentas.get("cuenta_"+pos)+"");
			String ingreso = mapaCuentas.get("ingreso_"+pos).toString();
			int centroCosto=Integer.parseInt(mapaCuentas.get("centro_costo_solicitado_"+pos)+"");
			int codigoPaciente=Integer.parseInt(mapaCuentas.get("codigo_paciente_"+pos)+"");
			
			//GENERACION DEL CARGO Y SUBCUENTA - EVALUACION COBERTURA
			int a=0;
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
																				/*"" -- numeroAutorzacionOPCIONAL*/
																				""/*esPortatil*/,false,estancia.getFechaInicialEstancia(),
																				"" /*subCuentaCoberturaOPCIONAL*/);

		    
		    Vector<String> mensajes= cargos.getInfoErroresCargo().getMensajesErrorDetalle();
		    
		    /*
		     * Se envia la información correspondiente para la generación de la autorización de capitación 
		     * subcontratada 
		     */
		    estancia.setConvenioResponsablePaciente(cargos.getDtoDetalleCargo().getCodigoConvenio());
		    estancia.setCodigoContratoConvenio(cargos.getDtoDetalleCargo().getCodigoContrato());
		    
		    for(int i=0;i<mensajes.size();i++)
		    	Log4JManager.info("mensajes encontrados => "+mensajes.get(i));
		    
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
			Log4JManager.info("\n\n\nIngreso Automatico de Solicitud de Autorizacion de Estancias\n\n\n");
			ActionErrors errores = new ActionErrors();
			ArrayList<DtoDetAutorizacionEst> dtoaux = new ArrayList<DtoDetAutorizacionEst>();
			dtoaux =Autorizaciones.cargarInfoBasicaDetAutorizacionEstancia(con, 13 , 
		    		cargos.getDtoDetalleCargo().getCodigoSubcuenta()+"", null, null,
		    		ConstantesIntegridadDominio.acronimoAutomatica);
			errores = Autorizaciones.insertarSolAutorizcionAdmEstAutomatica(con, dtoaux,usuario);
			if(errores.isEmpty()){
				Log4JManager.info("error en la insercion de solicitud de autorizacione de tipo estancia");
			}
				
		    //************************************************************************************************
			
			for(int i=0;i<mensajes.size();i++)
			{
				if(mensajes.get(i).equals("error.cargo.contratoVencido"))
					estancia.generarInconsistencias("A","El contrato especificado en la generación del cargo está vencido",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.cargo.esquemaNoSeleccionado"))
					estancia.generarInconsistencias("A","El contrato no tiene un esquema tarifario definido",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.cargo.noHayTarifaISS")||
						mensajes.get(i).toString().startsWith("error.cargo.noHayTarifaEsquemaTarifarioISSCita"))
					estancia.generarInconsistencias("A","No hay tarifa ISS para el servicio dado",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.cargo.noHayTarifaSoat")||
						mensajes.get(i).toString().startsWith("error.cargo.noHayTarifaEsquemaTarifarioSoatCita"))
					estancia.generarInconsistencias("A","No hay tarifa Soat para el servicio dado",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.cargo.noHayTarifaEsquemaTarifarioISS"))
					estancia.generarInconsistencias("A","No hay tarifa para el servicio seleccionado del esquema tarifario: ISS",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.cargo.noHayTarifaEsquemaTarifarioSoat"))
					estancia.generarInconsistencias("A","No hay tarifa para el servicio seleccionado del esquema tarifario: Soat",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.cargo.noHayValorTarifa"))
					estancia.generarInconsistencias("A","No existe valor de tarifa para el servicio dado",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.cargo.hayRepetidos"))
					estancia.generarInconsistencias("A","Ya existe un registro en la BD para el servicio - contrato - convenio que desea ingresar",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.cargo.contratoVencidoPaciente"))
					estancia.generarInconsistencias("A","El contrato del paciente se encuentra vencido",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.cargo.contratoVencidoPaciente"))
					estancia.generarInconsistencias("A","El contrato del paciente se encuentra vencido",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.tipoComplejidad.noExiste"))
					estancia.generarInconsistencias("A","El convenio maneja complejidad y la cuenta no tiene asignada un tipo complejidad",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				else if(mensajes.get(i).equals("error.cargo.noSeEspecificoServicio"))
					estancia.generarInconsistencias("A","No se especificó servicio en la generación del cargo",solicitud.getConsecutivoOrdenesMedicas()+"",indica);
				
			}
			return 1;
		  }
	    catch(SQLException slqe)
	    {
	        Log4JManager.info("Error generando el cargo de la solicitud en EstanciaAutomaticaAction: "+ numeroSolicitud+" "+slqe);
	        UtilidadBD.abortarTransaccion(con);
			return 0;
	    }
	    catch(Exception e)
	    {
	    	Log4JManager.info("Error generando el cargo de la solicitud en EstanciaAutomaticaAction: "+ numeroSolicitud+" "+e);
	        UtilidadBD.abortarTransaccion(con);
			return 0;
	    }
	}


	
	
	
}
