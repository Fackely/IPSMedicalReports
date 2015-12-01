package util.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.dto.cargos.DtoDetalleCargo;
import com.princetonsa.dto.odontologia.DtoLogProcAutoCitas;
import com.princetonsa.dto.odontologia.DtoLogProcAutoFact;
import com.princetonsa.dto.odontologia.DtoLogProcAutoServCita;
import com.princetonsa.dto.odontologia.DtoServicioCitaOdontologica;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.facturacion.AbonosYDescuentos;
import com.princetonsa.mundo.odontologia.CitaOdontologica;
import com.princetonsa.mundo.odontologia.ProcesosAutomaticosOdontologia;
import com.servinte.axioma.fwk.exception.IPSException;



/**
 * 
 * @author Edgar Carvajal
 *
 */
public class ActualizacionCitasOdontologicas extends Thread {
	
	/**
	 * Institución del proceso
	 */
	private int institucion;
	
	@Override
	public void run() {
		actualizarCitas();
	}
	
	/**
	 * 
	 * @param institucion
	 * @return
	 */
	public boolean actualizarCitas()
	{
		boolean controlaAbonosPorIngreso=UtilidadTexto.getBoolean(ValoresPorDefecto.getControlarAbonoPacientePorNroIngreso(institucion));
		
		Connection con = UtilidadBD.abrirConexion();
		
		Log4JManager.info("**************************************************ENTRE ACTUALIZAR CITAS************************************************");
		UtilidadBD.iniciarTransaccion(con);
		Log4JManager.info("---------valores -->-----------------"+ValoresPorDefecto.getEjecutarProcAutoActualizacionCitasOdo(institucion));
		Log4JManager.info("/////////////////////////////////////////////////////////////////////////////////////");
		
		if( ValoresPorDefecto.getEjecutarProcAutoActualizacionCitasOdo(institucion).equals(ConstantesBD.acronimoSi))
		{
			
			Log4JManager.info("**************************************************ENTRE  A METODO DE EJECUCION************************************************");
			
			String  minutosCaducaReservadas = ValoresPorDefecto.getMinutosCaducaCitasReservadas(institucion);
			Log4JManager.info("MINUTOS CADUCA---------------------------------->"+minutosCaducaReservadas);
			
			ArrayList<DtoLogProcAutoCitas> listLogReservadas = new ArrayList<DtoLogProcAutoCitas>();
			ArrayList<String> estados = new ArrayList<String>();
			estados.add(ConstantesIntegridadDominio.acronimoReservado);
			
			DtoLogProcAutoCitas dto = new DtoLogProcAutoCitas();
			dto.setInstitucion(institucion);
			
			/**
			 * PARA EL ESTADO RESERVADO
			 */
			listLogReservadas= ProcesosAutomaticosOdontologia.cargarCitasDisponibles(dto, minutosCaducaReservadas, estados, controlaAbonosPorIngreso);
			
			if(listLogReservadas.size()>0)
			{
				accionModificarCitasOdontologicas(con, listLogReservadas);
				guardarLogProcAutoCitas(con, listLogReservadas, institucion);
			}
			
			/**
			 * PARA ESTADO ASIGNADA O REPROGRAMADA 
			 */
			
			estados = new ArrayList<String>();
			estados.add(ConstantesIntegridadDominio.acronimoAsignado);
// Esto no se hace para las citas areprogramas, es solamente para las reprogramadas, el estado reprogramado ya no existe
//			estados.add(ConstantesIntegridadDominio.acronimoAreprogramar);
			
			String  minutosCaducaAsigReprog = ValoresPorDefecto.getMinutosCaducaCitasAsignadasReprogramadas(institucion);
			ArrayList<DtoLogProcAutoCitas> listLogReservadas1 = new ArrayList<DtoLogProcAutoCitas>();
			
			
			DtoLogProcAutoCitas dtoAR = new DtoLogProcAutoCitas();
			dtoAR.setInstitucion(institucion);
			dtoAR.setEstadoInicialCita(ConstantesIntegridadDominio.acronimoAsignado);
			
			/*
			 * 
			 */
			listLogReservadas1= ProcesosAutomaticosOdontologia.cargarCitasDisponibles(dtoAR, minutosCaducaAsigReprog, estados, controlaAbonosPorIngreso);
			
			if(listLogReservadas1.size()>0)
			{
				ArrayList<Integer> listaCodigoPk = new ArrayList<Integer>();
				for(DtoLogProcAutoCitas info: listLogReservadas1)
				{
					// Validar si las citas asignadas no están siendo atendidas
					if(!CitaOdontologica.estaCitaEnAtencion(info.getCitaOdontologica().intValue(), con))
					{
						listaCodigoPk.add(info.getCitaOdontologica().intValue());
						// Reversar movimientos abonos
						if(info.getValorTarifa()>0)
						{
							AbonosYDescuentos.insertarMovimientoAbonos(con, info.getCodigoPaciente(), info.getCitaOdontologica().intValue(), ConstantesBD.tipoMovimientoAbonoAnulacionReservaAbono, info.getValorTarifa(), institucion, info.getIngreso(), info.getCodigoCentroAtencion());
						}
					}
					
				}
				
				
				if(ProcesosAutomaticosOdontologia.modificarCita(listaCodigoPk, ConstantesIntegridadDominio.acronimoNoAsistio, con))
				{
					guardarLogProcAutoCitas(con, listLogReservadas1, institucion);
				}
			}
			//
		} // fin Ejecutar Proceso Automatico Actualizacion Citas Odo
		
		  
		UtilidadBD.finalizarTransaccion(con);
		UtilidadBD.closeConnection(con);
			
		  
		return true;
		   
		   
		
	}



	/**
	 * Este Método se encarga de guardar los log del proceso automático en 
	 * log_proc_auto_citas y log_proc_auto_serv_citas
	 * @param con
	 * @param listaCitasDisponibles
	 * @param institucion
	 * @author Yennifer Guerrero
	 */
	private void guardarLogProcAutoCitas(Connection con,
			ArrayList<DtoLogProcAutoCitas> listaCitasDisponibles,int institucion) {
		
		for(DtoLogProcAutoCitas info: listaCitasDisponibles)
		{
			double codigoLogCita = accionGuardarLogCitas(institucion, con, info); 
			if(codigoLogCita <= 0)
			{
				UtilidadBD.abortarTransaccion(con);
				UtilidadBD.closeConnection(con);
			}
			else
			{
				// Insercción Log ServCita
				for (DtoServicioCitaOdontologica servicio : info.getServiciosCita()) {
					
					DtoLogProcAutoServCita dtoLogServ = new DtoLogProcAutoServCita();
					
					dtoLogServ.setLogProcAutoCita(new BigDecimal(codigoLogCita));
					dtoLogServ.setServicioCitaOdo(new BigDecimal(servicio.getCodigoPk()));
					// Estado Historia Clinica
					
					if (servicio.getEstadoSolHistoClinica() > 0) {
						dtoLogServ.setEstadoInicialHc(servicio.getEstadoSolHistoClinica());
					}
					
					if (servicio.getEstadoSolFactura() > 0) {
						dtoLogServ.setEstadoInicialFact(servicio.getEstadoSolFactura());
					}
					
					double codigoLogServCita  = ProcesosAutomaticosOdontologia.guardarLogAutoServCita(dtoLogServ, con); 
					if (codigoLogServCita <= 0)
					{
						UtilidadBD.abortarTransaccion(con);
						UtilidadBD.closeConnection(con);
					}
				}
			}
		}
		
	}

	/**
	  * 
	  * @author Edgar Carvajal Ruiz
	  * @param con
	  * @param codigoLogServCita
	  * @param dtoLogFac
	  */
	private void accionModificarCargo(Connection con,double codigoLogServCita, DtoDetalleCargo dtoLogFac) throws IPSException
	{
		DtoLogProcAutoFact dtoNuevoAutoFac = new DtoLogProcAutoFact();
		dtoNuevoAutoFac.setDetCargo(new BigDecimal(dtoLogFac.getCodigoDetalleCargo()));
		dtoNuevoAutoFac.setEstadoInicialFact(dtoLogFac.getEstado());
		dtoNuevoAutoFac.setLogProcAutoServCita(new BigDecimal(codigoLogServCita));
		
		if( Cargos.modificarEstadoCargo(con, dtoNuevoAutoFac.getDetCargo().doubleValue(), ConstantesBD.estadoSolFactAnulada))
		{
			 accionGuardarLogProcesoAutoFact(con, dtoNuevoAutoFac);
		}
	}


	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param con
	 * @param dtoNuevoAutoFac
	 */
	private static void accionGuardarLogProcesoAutoFact(Connection con,	DtoLogProcAutoFact dtoNuevoAutoFac) {
		
		if(ProcesosAutomaticosOdontologia.guardarLogProcAutoFact(dtoNuevoAutoFac, con) <= 0)
		{
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.closeConnection(con);
		 }
	}



	
	/**
	 * 
	 * @author Edgar Carvajal Ruiz
	 * @param institucion
	 * @param con
	 * @param info
	 * @return
	 */
	private static double accionGuardarLogCitas(int institucion,
			Connection con, DtoLogProcAutoCitas info) {
		// Se forma Objeto Log Auto Cita
		DtoLogProcAutoCitas dtoLog = new DtoLogProcAutoCitas();
		dtoLog.setCitaOdontologica(info.getCitaOdontologica());
		dtoLog.setEstadoInicialCita(info.getEstadoInicialCita());
		dtoLog.setFechaEjecucion(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()));
		dtoLog.setHoraEjecucion(UtilidadFecha.getHoraActual());
		dtoLog.setInstitucion(institucion);

		// Se almacena
		double codigoLogCita = ProcesosAutomaticosOdontologia.guardarLogProcCitas(dtoLog, con);
		return codigoLogCita;
	}



	 /**
	  * 
	  * @author Edgar Carvajal Ruiz
	  * @param con
	  * @param listLogReservadas
	  */
	private static void accionModificarCitasOdontologicas(Connection con,ArrayList<DtoLogProcAutoCitas> listLogReservadas) {
		
		
		ArrayList<Integer> listaCodigoPk = new ArrayList<Integer>();
		for(DtoLogProcAutoCitas info: listLogReservadas)
		{
			listaCodigoPk.add(info.getCitaOdontologica().intValue());
			DtoServicioCitaOdontologica dtoWhere = new DtoServicioCitaOdontologica();
			dtoWhere.setCitaOdontologica(info.getCitaOdontologica().intValue());
			
			info.setListaLogServicios(ProcesosAutomaticosOdontologia.cargarProcAutoServCita(dtoWhere));
			
		}
		
		if(!ProcesosAutomaticosOdontologia.modificarCita(listaCodigoPk, ConstantesIntegridadDominio.acronimoNoAsistio, con))
		{
			UtilidadBD.abortarTransaccion(con);
			UtilidadBD.closeConnection(con);							
		}
	}



	/**
	 * Obtiene el atributo institucion
	 * @return institucion
	 */
	public int getInstitucion() {
		return institucion;
	}



	/**
	 * Asigna el atributo institucion
	 * @param institucion Atributo institucion
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}
	   
	  
	 
}
