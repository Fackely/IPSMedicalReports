package com.servinte.axioma.mundo.impl.tesoreria;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.comun.DtoResultado;
import com.princetonsa.dto.tesoreria.DtoConsultaTrasladoAbonoPAciente;
import com.princetonsa.dto.tesoreria.DtoGuardarTrasladoAbonoPaciente;
import com.princetonsa.dto.tesoreria.DtoInfoIngresoTrasladoAbonoPaciente;
import com.princetonsa.dto.tesoreria.DtoValidacionesTrasladoAbonoPaciente;
import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ITrasladosAbonosDAO;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IIngresosMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IPacientesMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ITrasladosAbonoMundo;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.DestinoTrasladosAbonosPac;
import com.servinte.axioma.orm.Ingresos;
import com.servinte.axioma.orm.Instituciones;
import com.servinte.axioma.orm.MovimientosAbonos;
import com.servinte.axioma.orm.OrigenTrasladosAbonosPac;
import com.servinte.axioma.orm.Personas;
import com.servinte.axioma.orm.TiposMovAbonos;
import com.servinte.axioma.orm.TrasladosAbonos;
import com.servinte.axioma.orm.TrasladosAbonosHome;
import com.servinte.axioma.orm.Usuarios;
import com.servinte.axioma.persistencia.UtilidadPersistencia;

/**
 * Contiene la l&oacute;gica de Negocio para los ingresos y egresos de caja
 * 
 * @author Cristhian Murillo
 * @see ITrasladosAbonoMundo
 */
@SuppressWarnings("rawtypes")
public class TrasladosAbonoMundo implements ITrasladosAbonoMundo{

	private IPacientesMundo pacientesMundo;
	private IIngresosMundo ingresosMundo;
	private ITrasladosAbonosDAO trasladosAbonosDAO;
	
	public TrasladosAbonoMundo() {
		inicializar();
	}
	
	private void inicializar() 
	{
		pacientesMundo		= ManejoPacienteFabricaMundo.crearPacientesMundo();
		ingresosMundo		= ManejoPacienteFabricaMundo.crearIngresosMundo();
		trasladosAbonosDAO 	= TesoreriaFabricaDAO.crearTrasladosAbonosDAO();
	}

	
	
	
	@Override
	public DtoValidacionesTrasladoAbonoPaciente validarPacienteParaTrasladoAbono(DtoPersonas paciente) 
	{
		DtoValidacionesTrasladoAbonoPaciente dtoValidacionesTrasladoAbonoPaciente = new DtoValidacionesTrasladoAbonoPaciente();
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getControlarAbonoPacientePorNroIngreso(paciente.getInstitucion())))
		{
			dtoValidacionesTrasladoAbonoPaciente.setParametroControlaAbonoPorIngreso(true);
			
			// :obtener listado de ingresos abono disponible DETALLADO
			if(Utilidades.isEmpty((ArrayList)ingresosMundo.obtenerIngresosParaTrasladoPorPaciente(paciente.getCodigo(), true)))
			{
				// :No tiene saldo
				dtoValidacionesTrasladoAbonoPaciente.setPacienteTieneSaldo(false);
				dtoValidacionesTrasladoAbonoPaciente.setPacienteTieneRegistrosDeIngreso(false); // ++
			}
			else
			{
				// :mostrar lista de abonos por ingreso
				// La lista que trae viene con los valores mayores a cero. Validacion que se hace en el delegate
				dtoValidacionesTrasladoAbonoPaciente.setPacienteTieneSaldo(true);
				dtoValidacionesTrasladoAbonoPaciente.setPacienteTieneRegistrosDeIngreso(true); // ++
				
				// ----- parte II validacion - Cuando tiene saldos (disponibles)
				dtoValidacionesTrasladoAbonoPaciente = validarPacienteParaTrasladoAbonoSaldoDisponible(dtoValidacionesTrasladoAbonoPaciente, paciente);
			}
		}
		else
		{
			dtoValidacionesTrasladoAbonoPaciente.setParametroControlaAbonoPorIngreso(false);
			
			// --> obtener el ultimo abono? size
			double valorAbonoDisponibleTotalizado = ConstantesBD.codigoNuncaValidoDouble;
			if(ingresosMundo.obtenerIngresosParaTrasladoPorPaciente(paciente.getCodigo(), false) != null)
			{
				// :obtener listado de ingresos abono disponible TOTALIZADO
				if(Utilidades.isEmpty((ArrayList)ingresosMundo.obtenerIngresosParaTrasladoPorPaciente(paciente.getCodigo(), false)))
				{
					dtoValidacionesTrasladoAbonoPaciente.setPacienteTieneRegistrosDeIngreso(false);
				}
				else
				{
					dtoValidacionesTrasladoAbonoPaciente.setPacienteTieneRegistrosDeIngreso(true);
					// La funcion utilizada retorna la lista de todos los ingresos pero con el mismo valor totalizado para cada uno de estos
					valorAbonoDisponibleTotalizado = ingresosMundo.obtenerIngresosParaTrasladoPorPaciente(paciente.getCodigo(), false).get(0).getAbonoDisponible();
				}
			}

			if(valorAbonoDisponibleTotalizado > 0)
			{
				dtoValidacionesTrasladoAbonoPaciente.setPacienteTieneSaldo(true);
				
				// ----- parte II validacion - Cuando tiene saldos disponibles 
				dtoValidacionesTrasladoAbonoPaciente = validarPacienteParaTrasladoAbonoSaldoDisponible(dtoValidacionesTrasladoAbonoPaciente, paciente);
			}
			else{
				// :no tiene saldo
				dtoValidacionesTrasladoAbonoPaciente.setPacienteTieneSaldo(false);
			}
		}
		
		return dtoValidacionesTrasladoAbonoPaciente;
	}
	
	
	
	/**
	 * Luego de validar si el paciente tiene saldo se validan los otros parametros 
	 * para mostrar la funcionalidad de traslado de abono de paciente.
	 * 
	 * @param dtoValidacionesTrasladoAbonoPaciente
	 * @param paciente
	 * @return
	 */
	private DtoValidacionesTrasladoAbonoPaciente validarPacienteParaTrasladoAbonoSaldoDisponible(DtoValidacionesTrasladoAbonoPaciente dtoValidacionesTrasladoAbonoPaciente, DtoPersonas paciente) 
	{
		// :parametro manejo especial instituciones odontologicas
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getManejoEspecialInstitucionesOdontologia(paciente.getInstitucion())))
		{
			dtoValidacionesTrasladoAbonoPaciente.setParametroManejoEspecialInstiOdonto(true);
			
			// :parametro traslado abono paciente saldo presupuesto contratado
			if(UtilidadTexto.getBoolean(ValoresPorDefecto.getTrasladoAbonosPacienteSoloPacientesConPresupuestoContratado(paciente.getInstitucion())))
			{
				dtoValidacionesTrasladoAbonoPaciente.setParametroTrasAbonoPacSaldoPresuContratado(true);
				
				// :obtener estado presupuesto paciente
				// :presupuesto paciente estado = contratadoContratado
				String[] listaEstadosPresupuesto = { ConstantesIntegridadDominio.acronimoContratadoContratado };
				if(pacientesMundo.tienePacientePresupuestoEnEstados(paciente.getCodigo(), listaEstadosPresupuesto))
				{
					// :tiene presupuesto estado acronimoContratadoContratado
					dtoValidacionesTrasladoAbonoPaciente.setPacienteTienePresupuestoEstadoCorrecto(true);
				}
				else{
					// :no tiene presupuesto estado acronimoContratadoContratado
					dtoValidacionesTrasladoAbonoPaciente.setPacienteTienePresupuestoEstadoCorrecto(false);
				}
			}
			else{
				dtoValidacionesTrasladoAbonoPaciente.setParametroTrasAbonoPacSaldoPresuContratado(false);
				// -->> Mostrar la info con formato
			}
		}
		else{
			dtoValidacionesTrasladoAbonoPaciente.setParametroManejoEspecialInstiOdonto(false);
			// -->> Mostrar la info con formato
		}
		
		return dtoValidacionesTrasladoAbonoPaciente;
	}


	
	
	@Override
	public DtoValidacionesTrasladoAbonoPaciente validarPacienteParaTrasladoAbonoDestino(DtoPersonas paciente) 
	{
		DtoValidacionesTrasladoAbonoPaciente dtoValidacionesTrasladoAbonoPaciente = new DtoValidacionesTrasladoAbonoPaciente();
		
		// :parametro control abono x ingreso
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getControlarAbonoPacientePorNroIngreso(paciente.getInstitucion())))
		{	
			dtoValidacionesTrasladoAbonoPaciente.setParametroControlaAbonoPorIngreso(true);
			String[] listaEstadosIngreso = { ConstantesIntegridadDominio.acronimoEstadoAbierto };
			
			if(Utilidades.isEmpty((ArrayList)ingresosMundo.obtenerIngresosPacientePorEstado(paciente.getCodigo(), listaEstadosIngreso)))
			{
				dtoValidacionesTrasladoAbonoPaciente.setPacienteTieneTodosIngresosCerrados(true);
				return dtoValidacionesTrasladoAbonoPaciente;
			}
			else
			{
				dtoValidacionesTrasladoAbonoPaciente.setPacienteTieneTodosIngresosCerrados(false);
				// --> segunda parte
			}
		}
		else
		{
			dtoValidacionesTrasladoAbonoPaciente.setParametroControlaAbonoPorIngreso(false);
			// --> segunda parte
		}
		
		// :segunda parte
		if(UtilidadTexto.getBoolean(ValoresPorDefecto.getManejoEspecialInstitucionesOdontologia(paciente.getInstitucion())))
		{
			dtoValidacionesTrasladoAbonoPaciente.setParametroManejoEspecialInstiOdonto(true);
			
			if(UtilidadTexto.getBoolean(ValoresPorDefecto.getTrasladoAbonosPacienteSoloPacientesConPresupuestoContratado(paciente.getInstitucion())))
			{
				dtoValidacionesTrasladoAbonoPaciente.setParametroTrasAbonoPacSaldoPresuContratado(true);
				String[] listaEstadosPresupuesto = { ConstantesIntegridadDominio.acronimoContratadoContratado };
				
				if(pacientesMundo.tienePacientePresupuestoEnEstados(paciente.getCodigo(), listaEstadosPresupuesto))
				{
					dtoValidacionesTrasladoAbonoPaciente.setPacienteTienePresupuestoEstadoCorrecto(true);
				}
				else{
					dtoValidacionesTrasladoAbonoPaciente.setPacienteTienePresupuestoEstadoCorrecto(false);
				}
			}
			else
			{
				dtoValidacionesTrasladoAbonoPaciente.setParametroTrasAbonoPacSaldoPresuContratado(false);
			}
		}
		else
		{
			dtoValidacionesTrasladoAbonoPaciente.setParametroManejoEspecialInstiOdonto(true);
		}
		return dtoValidacionesTrasladoAbonoPaciente;
	}

	
	
	
	@Override
	public DtoResultado guardarTrasladoAbono(DtoGuardarTrasladoAbonoPaciente dtoGuardarTrasladoAbonoPaciente) 
	{
		TrasladosAbonos trasladosAbonos = new TrasladosAbonos();
		Usuarios usuario = new Usuarios();
		usuario.setLogin(dtoGuardarTrasladoAbonoPaciente.getLoginUsuarioActual());
		trasladosAbonos.setUsuarios(usuario);
		trasladosAbonos.setFecha(UtilidadFecha.getFechaActualTipoBD());
		trasladosAbonos.setHora(UtilidadFecha.getHoraActual());
		CentroAtencion centroAtencion = new CentroAtencion();
		centroAtencion.setConsecutivo(dtoGuardarTrasladoAbonoPaciente.getCentroAtencionActual());
		trasladosAbonos.setCentroAtencion(centroAtencion);
		trasladosAbonos.setContabilizado(ConstantesBD.acronimoNo.charAt(0));
		//trasladosAbonos.setTipoComprobante(null);
		//trasladosAbonos.setNumeroComprobante(?);
		BigDecimal consecutivo = new BigDecimal(UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoTrasladoAbonosPaciente, dtoGuardarTrasladoAbonoPaciente.getInstitucion()));
		trasladosAbonos.setConsecutivo(consecutivo);
		Connection conH=UtilidadPersistencia.getPersistencia().obtenerConexion();
		UtilidadBD.cambiarUsoFinalizadoConsecutivo(conH,ConstantesBD.nombreConsecutivoTrasladoAbonosPaciente, dtoGuardarTrasladoAbonoPaciente.getInstitucion(), consecutivo.toString(), ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
		
		// Se sincroniza el objeto a persistir con la base de datos para 
		// obtener su llave primaria y poder asignarla. De lo contrario nunca 
		// podra ser asignado en este caso el numero del documento
		TrasladosAbonosHome trasladosAbonosHome = new TrasladosAbonosHome();
		trasladosAbonosHome.attachDirty(trasladosAbonos);
		// -------------------------------------------------------------
		
		ArrayList<MovimientosAbonos> listaMovimientosAbonosDestino = new ArrayList<MovimientosAbonos>();
		
		for (DtoInfoIngresoTrasladoAbonoPaciente ingresoPacDestino : dtoGuardarTrasladoAbonoPaciente.getListaIngresosPacDestino()) 
		{
			MovimientosAbonos movimientosAbonos = new MovimientosAbonos();
			Personas persona = new Personas();
			persona.setCodigo(ingresoPacDestino.getCodigoPaciente());
			movimientosAbonos.setPersonas(persona);
			movimientosAbonos.setCodigoDocumento(Integer.parseInt(trasladosAbonos.getCodigoPk()+""));
			TiposMovAbonos tipoMovAbono = new TiposMovAbonos();
			tipoMovAbono.setCodigo(ConstantesBD.tipoMovimientoAbonoIngresoPorTraslado);
			movimientosAbonos.setTiposMovAbonos(tipoMovAbono);
			movimientosAbonos.setValor(ingresoPacDestino.getAbonoTrasladar());
			movimientosAbonos.setFecha(UtilidadFecha.getFechaActualTipoBD());
			movimientosAbonos.setHora(UtilidadFecha.getHoraActual()); 
			Instituciones instituciones = new Instituciones();
			instituciones.setCodigo(dtoGuardarTrasladoAbonoPaciente.getInstitucion());
			movimientosAbonos.setInstituciones(instituciones);
			Ingresos ingresos = new Ingresos();
			ingresos.setId(ingresoPacDestino.getIdIngreso());
			
			movimientosAbonos.setIngresos(ingresos);
			movimientosAbonos.setCentroAtencionByCentroAtencion(centroAtencion);
			
			try {	
				
				int consecutivoCentroAt = pacientesMundo.obtenerDatosPaciente(ingresoPacDestino.getCodigoPaciente()).getcAtencionDuenio();
				
				if (consecutivoCentroAt > 0) 
				{
					CentroAtencion centroAtencionduenio = new CentroAtencion();
					centroAtencionduenio.setConsecutivo(consecutivoCentroAt);
					movimientosAbonos.setCentroAtencionByCentroAtencionDuenio(centroAtencionduenio);
				}
				
			}catch (Exception e) 
			{
				Log4JManager.error("El paciente con codigo "+ingresoPacDestino.getCodigoPaciente()+" No tiene un Centro de Atención Dueño asociado",e);
			}
			
			listaMovimientosAbonosDestino.add(movimientosAbonos);
		}
		
		ArrayList<MovimientosAbonos> listaMovimientosAbonosOrigen;
		
		
		
		if(dtoGuardarTrasladoAbonoPaciente.isParametroControlAbonoPorIngreso())
		{
			// por ingreso
			 listaMovimientosAbonosOrigen = new ArrayList<MovimientosAbonos>();
			for (DtoInfoIngresoTrasladoAbonoPaciente ingresoPacOrigen : dtoGuardarTrasladoAbonoPaciente.getListaIngresosPacOrigen()) 
			{
				MovimientosAbonos movimientosAbonos = new MovimientosAbonos();
				Personas persona = new Personas();
				persona.setCodigo(ingresoPacOrigen.getCodigoPaciente());
				movimientosAbonos.setPersonas(persona);
				movimientosAbonos.setCodigoDocumento(Integer.parseInt(trasladosAbonos.getCodigoPk()+""));
				TiposMovAbonos tipoMovAbono = new TiposMovAbonos();
				tipoMovAbono.setCodigo(ConstantesBD.tipoMovimientoAbonoSalidaPorTraslado);
				movimientosAbonos.setTiposMovAbonos(tipoMovAbono);
				movimientosAbonos.setValor(ingresoPacOrigen.getAbonoTrasladar());
				movimientosAbonos.setFecha(UtilidadFecha.getFechaActualTipoBD());
				movimientosAbonos.setHora(UtilidadFecha.getHoraActual()); 
				Instituciones instituciones = new Instituciones();
				instituciones.setCodigo(dtoGuardarTrasladoAbonoPaciente.getInstitucion());
				movimientosAbonos.setInstituciones(instituciones);
				Ingresos ingresos = new Ingresos();
				ingresos.setId(ingresoPacOrigen.getIdIngreso());
				movimientosAbonos.setIngresos(ingresos);
				movimientosAbonos.setCentroAtencionByCentroAtencion(centroAtencion);
				
				try 
				{	
					int consecutivoCentroAtencionduenio = pacientesMundo.obtenerDatosPaciente(ingresoPacOrigen.getCodigoPaciente()).getcAtencionDuenio();
					if(consecutivoCentroAtencionduenio > 0)
					{
						CentroAtencion centroAtencionduenio = new CentroAtencion();
						centroAtencionduenio.setConsecutivo(consecutivoCentroAtencionduenio);
						movimientosAbonos.setCentroAtencionByCentroAtencionDuenio(centroAtencionduenio);
					}
					
				}catch (Exception e) 
				{
					Log4JManager.error("El paciente con codigo "+ingresoPacOrigen.getCodigoPaciente()+" No tiene un Centro de Dtención Dueño asociado",e);
				}
				
				
				CentroAtencion centroAtencionduenio = new CentroAtencion();
				int concecutivoCentroAten = ConstantesBD.codigoNuncaValido;
				try {
					concecutivoCentroAten = pacientesMundo.obtenerDatosPaciente(ingresoPacOrigen.getCodigoPaciente()).getcAtencionDuenio();
				} catch (Exception e) {
					concecutivoCentroAten = ConstantesBD.codigoNuncaValido;
				}
				if(concecutivoCentroAten != ConstantesBD.codigoNuncaValido){
					centroAtencionduenio.setConsecutivo(concecutivoCentroAten);
					movimientosAbonos.setCentroAtencionByCentroAtencionDuenio(centroAtencionduenio);
					
				}else{ Log4JManager.error("Paciente origen sin Centro de Atención dueño"); 	}
				
				
				listaMovimientosAbonosOrigen.add(movimientosAbonos);
			}
		}
		else
		{
			// totalizado
			listaMovimientosAbonosOrigen = new ArrayList<MovimientosAbonos>();
			DtoInfoIngresoTrasladoAbonoPaciente ingresoPacOrigen = dtoGuardarTrasladoAbonoPaciente.getListaIngresosPacOrigen().get(0);
			MovimientosAbonos movimientosAbonos = new MovimientosAbonos();
			Personas persona = new Personas();
			persona.setCodigo(ingresoPacOrigen.getCodigoPaciente());
			movimientosAbonos.setPersonas(persona);
			movimientosAbonos.setCodigoDocumento(Integer.parseInt(trasladosAbonos.getCodigoPk()+""));
			TiposMovAbonos tipoMovAbono = new TiposMovAbonos();
			tipoMovAbono.setCodigo(ConstantesBD.tipoMovimientoAbonoSalidaPorTraslado);
			movimientosAbonos.setTiposMovAbonos(tipoMovAbono);
			movimientosAbonos.setValor(ingresoPacOrigen.getAbonoTrasladar());
			movimientosAbonos.setFecha(UtilidadFecha.getFechaActualTipoBD());
			movimientosAbonos.setHora(UtilidadFecha.getHoraActual()); 
			Instituciones instituciones = new Instituciones();
			instituciones.setCodigo(dtoGuardarTrasladoAbonoPaciente.getInstitucion());
			movimientosAbonos.setInstituciones(instituciones);
			Ingresos ingresos = new Ingresos();
			movimientosAbonos.setCentroAtencionByCentroAtencion(centroAtencion);
			
			if(ingresoPacOrigen.getIdIngreso() != ConstantesBD.codigoNuncaValido){
				ingresos.setId(ingresoPacOrigen.getIdIngreso());
				movimientosAbonos.setIngresos(ingresos);
			}else{ Log4JManager.error("Paciente origen sin Ingreso asociado"); 	}
			
			CentroAtencion centroAtencionduenio = new CentroAtencion();
			int concecutivoCentroAten = ConstantesBD.codigoNuncaValido;
			try {
				concecutivoCentroAten = pacientesMundo.obtenerDatosPaciente(ingresoPacOrigen.getCodigoPaciente()).getcAtencionDuenio();
			} catch (Exception e) {
				concecutivoCentroAten = ConstantesBD.codigoNuncaValido;
			}
			if(concecutivoCentroAten != ConstantesBD.codigoNuncaValido){
				centroAtencionduenio.setConsecutivo(concecutivoCentroAten);
				movimientosAbonos.setCentroAtencionByCentroAtencionDuenio(centroAtencionduenio);
				
			}else{ Log4JManager.error("Paciente origen sin Centro de Atención dueño"); 	}
			
				
			listaMovimientosAbonosOrigen.add(movimientosAbonos);
		}
		
		// Destinos
		HashSet<DestinoTrasladosAbonosPac> setDestinoTrasladosAbonosPac = new HashSet<DestinoTrasladosAbonosPac>();
		DestinoTrasladosAbonosPac destinoTrasladosAbonosPacs = new DestinoTrasladosAbonosPac();
		for (MovimientosAbonos movimientosAbonosDestino : listaMovimientosAbonosDestino) 
		{
			destinoTrasladosAbonosPacs = new DestinoTrasladosAbonosPac();
			destinoTrasladosAbonosPacs.setTrasladosAbonos(trasladosAbonos);
			destinoTrasladosAbonosPacs.setMovimientosAbonos(movimientosAbonosDestino);
			setDestinoTrasladosAbonosPac.add(destinoTrasladosAbonosPacs);
		}
		trasladosAbonos.setDestinoTrasladosAbonosPacs(setDestinoTrasladosAbonosPac);
		
		
		// Origen 
		HashSet<OrigenTrasladosAbonosPac> setOrigenTrasladosAbonosPac = new HashSet<OrigenTrasladosAbonosPac>();
		OrigenTrasladosAbonosPac origenTrasladosAbonosPac = new OrigenTrasladosAbonosPac();
		for (MovimientosAbonos movimientosAbonosOrigen : listaMovimientosAbonosOrigen) 
		{
			origenTrasladosAbonosPac = new OrigenTrasladosAbonosPac();
			origenTrasladosAbonosPac.setTrasladosAbonos(trasladosAbonos);
			origenTrasladosAbonosPac.setMovimientosAbonos(movimientosAbonosOrigen);
			setOrigenTrasladosAbonosPac.add(origenTrasladosAbonosPac);
		}
		trasladosAbonos.setOrigenTrasladosAbonosPacs(setOrigenTrasladosAbonosPac);

		
		DtoResultado resultado = new DtoResultado();
		if (!trasladosAbonosDAO.guardarTraslado(trasladosAbonos)){
			Connection con=UtilidadBD.abrirConexion();
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoTrasladoAbonosPaciente, dtoGuardarTrasladoAbonoPaciente.getInstitucion(), consecutivo.toString(), ConstantesBD.acronimoNo, ConstantesBD.acronimoNo);
			resultado.setExitoso(false);
			UtilidadBD.closeConnection(con);
			return resultado;
		}
	
		resultado.setExitoso(true);
		resultado.setPk(trasladosAbonos.getCodigoPk()+"");
		return resultado;
	}
	

	@Override
	public List<DtoConsultaTrasladoAbonoPAciente> obtenerDetallesTrasladoAbonos(
			DtoConsultaTrasladoAbonoPAciente dtoConsulta) {
		return trasladosAbonosDAO.obtenerDetallesTrasladoAbonos(dtoConsulta);
	}

	
}
