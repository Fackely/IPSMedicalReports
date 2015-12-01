package util.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.util.UtilidadOdontologiaDao;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.dto.odontologia.DtoAntecedentesAlerta;
import com.princetonsa.dto.odontologia.DtoAntecendenteOdontologico;
import com.princetonsa.dto.odontologia.DtoComponenteIndicePlaca;
import com.princetonsa.dto.odontologia.DtoDetalleIndicePlaca;
import com.princetonsa.dto.odontologia.DtoFiltroConsultaServiciosPaciente;
import com.princetonsa.dto.odontologia.DtoParentesco;
import com.princetonsa.dto.odontologia.DtoServicioOdontologico;
import com.princetonsa.dto.odontologia.DtoTratamientoExterno;
import com.princetonsa.dto.odontologia.DtoTratamientoInterno;

public class UtilidadOdontologia {
	
	/**
	 * get DaoFatory
	 */
	public static UtilidadOdontologiaDao getUtilidadOdontologiaDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadOdontologiaDao();
	}

	/** 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoParentesco> obtenerParentezco (DtoParentesco dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getParentezcoDao().cargar(dto);
	}
	
	/**
	 * Consulta las interpretaciones de indice de placa, la informacion la devuelve como lenguaje de marcas (XML)
	 * */
	public static String cargarInterpretacionIndicePlaca()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadOdontologiaDao().cargarInterpretacionIndicePlaca();
	}
	

	/**
	 * 
	 * Inserta el indice de placa
	 * @param DtoIndicePlaca dto
	 * * 
	 * */
	public static int insertarIndicePlaca(Connection con,DtoComponenteIndicePlaca dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getUtilidadOdontologiaDao().insertarIndicePlaca(con,dto);
	}
	
	
	public static boolean esBigDecimalMayorA(BigDecimal valor1, BigDecimal valor2){
		
		if(valor1.compareTo(valor2) > 0 ){
			
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean esBigDecimalMayorACero(BigDecimal valor1){
		
		if(valor1==null){
			
			return false;
		}else if(valor1.compareTo(new BigDecimal(0)) > 0 ){
			
			return true;
		}else{
			return false;
		}
	}

	/**
	 * 
	 * M&eacute;todo que carga los servicios odont&oacute;logicos. Si se envia el c&oacute;digo y tipo de servicio, 
	 * solo trae el registro correspondiente a ese servicio.
	 * Tambien se listan los servicios que estan asociados a una cita o a un grupo de citas espec&iacute;fico
	 * 
	 * @param parametros
	 * @return
	 */
	public static ArrayList<DtoServicioOdontologico> obtenerServicios(HashMap<String, Object> parametros)
	{

		return getUtilidadOdontologiaDao().obtenerServicios(parametros);
	}
	
	

	/**
	 * Método que obtiene los servicios asociados a un paciente,
	 * con un estado y tipo de cita que debe estar asociada a un ingreso espec&iacute;fico.
	 * 
	 * Si se envia el código de la cita, lo involucra en el filtro.
	 * 
	 * @param codigoPaciente
	 * @param codigoIngreso
	 * @param estadoCita
	 * @param tipoCita
	 * @param codigoCita
	 * @return
	 */
	public static ArrayList<DtoServicioOdontologico> obtenerServiciosXEstadoCitaXTipoCita (int codigoPaciente, int codigoIngreso, String estadoCita, String tipoCita, int codigoCita) {
		
		
		return getUtilidadOdontologiaDao().obtenerServiciosXEstadoCitaXTipoCita(codigoPaciente, codigoIngreso, estadoCita, tipoCita, codigoCita);
	}
	
		
	/**
	 * 
	 * @param filtro
	 * @return
	 */
	public static ArrayList<DtoServicioOdontologico> obtenerServiciosPaciente(DtoFiltroConsultaServiciosPaciente filtro) 
	{
		return getUtilidadOdontologiaDao().obtenerServicios(filtro);
	}
	

	/**
	 * meodo que retorna las piezas dentales
	 * @return
	 */
	public static ArrayList<InfoDatosInt> obtenerPiezasDentales()
	{
		return getUtilidadOdontologiaDao().obtenerPiezasDentales();
	}
	
	/**
	 * metodo que obtiene los tratamientos externos 
	 * @return
	 */
	public static ArrayList<DtoTratamientoExterno> obtenerAnteOdoTratamientosExternos(int codigoAnteOdo)
	{
		return getUtilidadOdontologiaDao().obtenerAnteOdoTratamientosExternos(codigoAnteOdo);
	}
	
	/**
	 * metodo que obtiene el antecedente odontologico
	 * @param codigoPaciente
	 * @return
	 */
	public static DtoAntecendenteOdontologico obtenerAntecedenteOdontologico(int codigoPaciente)
	{
		return getUtilidadOdontologiaDao().obtenerAntecedenteOdontologico(codigoPaciente);
	}
	
	/**
	 * Método implementado para cargar la informacion de antecedentes bien sea desde la valoracion o evolucion odontologica
	 * @param con
	 * @param antecedenteOdo
	 */
	public static void obtenerAntecedenteOdontologicoHistorico(Connection con,DtoAntecendenteOdontologico antecedenteOdo)
	{
		getUtilidadOdontologiaDao().obtenerAntecedenteOdontologicoHistorico(con, antecedenteOdo);
	}
	
	/**
	 * metodo que inserta un antecedente odontologico
	 * @param con
	 * @param dto
	 * @return
	 */
	public static int insertarAntcedenteOdontologico(Connection con, DtoAntecendenteOdontologico dto)
	{
		return getUtilidadOdontologiaDao().insertarAntcedenteOdontologico(con, dto);
	}
	
	/**
	 * metodo que llena el dto de tramientos internos
	 * @param infoAnteOdo
	 */
	public static void llenarDtoTratamientoInterno(InfoAntecedenteOdonto infoAnteOdo)
	{
		if(infoAnteOdo.getInfoPlaTratamiento()!=null && infoAnteOdo.getInfoPlaTratamiento().size()>0)
		{
			for(int i=0;i<infoAnteOdo.getInfoPlaTratamiento().size();i++)
			{
				InfoPlanTratamiento elem = (InfoPlanTratamiento) infoAnteOdo.getInfoPlaTratamiento().get(i);
				
				// Seccion DETALLE
				for(int j=0;j<elem.getSeccionHallazgosDetalle().size();j++)
				{
					InfoDetallePlanTramiento elem1 = (InfoDetallePlanTramiento) elem.getSeccionHallazgosDetalle().get(j);
					
					for(int w=0;w<elem1.getDetalleSuperficie().size();w++)
					{
						InfoHallazgoSuperficie elem2 = (InfoHallazgoSuperficie) elem1.getDetalleSuperficie().get(w);
						
						for(int z=0;z<elem2.getProgramasOservicios().size();z++)
						{
							InfoProgramaServicioPlan elem3 = (InfoProgramaServicioPlan) elem2.getProgramasOservicios().get(z);
							DtoTratamientoInterno dto = new DtoTratamientoInterno();
							dto.setCodigoProgSerPlanTrat(Utilidades.convertirAEntero(elem3.getCodigopk().toString()));
							dto.setFechaInicio(elem3.getFechaInicio());
							dto.setFechaFinal(elem3.getFechaFinal());
							dto.setCodigoPiezaDen(elem1.getPieza().getCodigo());
							dto.setCodigoEspecialidad(elem3.getCodigoEspecialidad());							
							infoAnteOdo.getAntecedenteOdon().getTratamientosInternos().add(dto);
						}
					}
				}
				
				// Seccion OTROS
				for(int j=0;j<elem.getSeccionOtrosHallazgos().size();j++)
				{
					InfoDetallePlanTramiento elem1 = (InfoDetallePlanTramiento) elem.getSeccionOtrosHallazgos().get(j);
					
					for(int w=0;w<elem1.getDetalleSuperficie().size();w++)
					{
						InfoHallazgoSuperficie elem2 = (InfoHallazgoSuperficie) elem1.getDetalleSuperficie().get(w);
						
						for(int z=0;z<elem2.getProgramasOservicios().size();z++)
						{
							InfoProgramaServicioPlan elem3 = (InfoProgramaServicioPlan) elem2.getProgramasOservicios().get(z);
							DtoTratamientoInterno dto = new DtoTratamientoInterno();
							dto.setCodigoProgSerPlanTrat(Utilidades.convertirAEntero(elem3.getCodigopk().toString()));
							dto.setFechaInicio(elem3.getFechaInicio());
							dto.setFechaFinal(elem3.getFechaFinal());
							dto.setCodigoPiezaDen(elem1.getPieza().getCodigo());
							dto.setCodigoEspecialidad(elem3.getCodigoEspecialidad());							
							infoAnteOdo.getAntecedenteOdon().getTratamientosInternos().add(dto);
						}
					}
				}
				
				// seccion BOCA
				for(int j=0;j<elem.getSeccionHallazgosBoca().size();j++)
				{
					InfoHallazgoSuperficie elem2 = (InfoHallazgoSuperficie) elem.getSeccionHallazgosBoca().get(j);
					
					for(int z=0;z<elem2.getProgramasOservicios().size();z++)
					{
						InfoProgramaServicioPlan elem3 = (InfoProgramaServicioPlan) elem2.getProgramasOservicios().get(z);
						DtoTratamientoInterno dto = new DtoTratamientoInterno();
						dto.setCodigoProgSerPlanTrat(Utilidades.convertirAEntero(elem3.getCodigopk().toString()));
						dto.setFechaInicio(elem3.getFechaInicio());
						dto.setFechaFinal(elem3.getFechaFinal());
						dto.setCodigoPiezaDen(ConstantesBD.codigoNuncaValido);
						dto.setCodigoEspecialidad(elem3.getCodigoEspecialidad());							
						infoAnteOdo.getAntecedenteOdon().getTratamientosInternos().add(dto);
					}
				}
			}
		}
		
		
	}
	
	/**
	 * Método que retorna un booleano indicando si tiene o no agenda generada un profesional de la salud
	 * @param connection
	 * @param codigoProfesional
	 * @return
	 */
	public static boolean consultarProfesionalTieneAgendaGenerada(Connection connection, int codigoProfesional){
		return getUtilidadOdontologiaDao().consultarProfesionalTieneAgendaGenerada(connection, codigoProfesional);
	}

	
	/**
	 * Metodo para consultar TODOS los Antecedentes Odontológicos Asociados a un Paciente (Version NUEVA - Agosto 2009)
	 *  
	 * @param codigoPersona
	 * @param codIngreso
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public static ArrayList<DtoAntecendenteOdontologico> obtenerAntecedentesOdontologicos(int codigoPersona, int codIngreso, String fechaInicial, String fechaFinal) {
		
		return getUtilidadOdontologiaDao().obtenerAntecedentesOdontologicos(codigoPersona,codIngreso,fechaInicial, fechaFinal);
	}
	
	/**
	 * metodo que actualiza el antecedente odontologico
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean actualizarAntecedenteOdontologico(Connection con, DtoAntecendenteOdontologico dto)
	{
		return getUtilidadOdontologiaDao().actualizarAntecedenteOdontologico(con, dto);
	}

	/**
	 * metodo que actualiza Tratamiento Externo 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean actualizarTratamientoExterno(Connection con, DtoTratamientoExterno dto)
	{
		return getUtilidadOdontologiaDao().actualizarTratamientoExterno(con, dto);
	}
	
	/**
	 * metodo eliminar tratamientos internos de un antecedente odontologico
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean deleteTratamientoInterno(Connection con, int codigoAnteOdon)
	{
		return getUtilidadOdontologiaDao().deleteTratamientoInterno(con, codigoAnteOdon);
	}
	
	/**
	 * metodo que elimina un tratamiento externo
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean deleteTratamientoExterno(Connection con, DtoTratamientoExterno dto)
	{
		return getUtilidadOdontologiaDao().deleteTratamientoExterno(con, dto);
	}

	/**
	 * metodo que inserta un tratamiento interno 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean insertarTratamientoInterno(Connection con, DtoTratamientoInterno dto)
	{
		return getUtilidadOdontologiaDao().insertarTratamientoInterno(con, dto);
	}

	/**
	 * metodo que inserta el un tratamiento externo
	 * @param con
	 * @param dto
	 * @return
	 */
	public static int insertarTratamientoExterno(Connection con, DtoTratamientoExterno dto)
	{
		return getUtilidadOdontologiaDao().insertarTratamientoExterno(con, dto);
	}
	
	/**
	 * metodo que actualiza el antecedente odontologico
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean actualizacionAntOdon(Connection con, DtoAntecendenteOdontologico dto)
	{
		// actualizacion del antecedente odontologico
		if(actualizarAntecedenteOdontologico(con, dto))
		{
			
			
			// eliminacion, modificacion e insercion del tratamiento externo
			for(DtoTratamientoExterno elem: dto.getTratamientosExternos())
			{
				elem.setCodigoAnteOdo(dto.getCodigoPk());
				if(elem.getEliminar().equals(ConstantesBD.acronimoSi))
				{
					if(!deleteTratamientoExterno(con, elem))
						return false;
				}else{
					if(elem.getNuevo().equals(ConstantesBD.acronimoSi))
					{
						if(insertarTratamientoExterno(con, elem)==ConstantesBD.codigoNuncaValido)
							return false;
					}else{
						if(elem.getModificar().equals(ConstantesBD.acronimoSi))
						{
							if(!actualizarTratamientoExterno(con, elem))
								return false;
						}
					}
				}
			}
			
			// actualizacion de los tratamientos internos
			if(deleteTratamientoInterno(con, dto.getCodigoPk()))
			{
				for(DtoTratamientoInterno elem1: dto.getTratamientosInternos())
				{
					elem1.setCodigoAnteOdo(dto.getCodigoPk());
					if(!insertarTratamientoInterno(con, elem1))
						return false;
				}
			}
			return true;
		}else
			return false;
	}
	
	/**
	 * metodo que obtiene el antecedente odontologico existente
	 * @param codigoPaciente
	 * @return
	 */
	public static DtoAntecendenteOdontologico obtenerAntecedenteOdontExistente(int codigoAnteOdon,boolean esEvolucion)
	{
		return getUtilidadOdontologiaDao().obtenerAntecedenteOdontExistente(codigoAnteOdon,esEvolucion);
	}
	
	/**
	 * Método implementado para confirmar el indice de placa
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ResultadoBoolean confirmarIndicePlaca(Connection con,String loginUsuario,String codigoPlantillaIngreso,String codigoPlantillaEvolucion)
	{
		HashMap campos = new HashMap();
		campos.put("loginUsuario",loginUsuario);
		campos.put("codigoPlantillaIngreso", codigoPlantillaIngreso);
		campos.put("codigoPlantillaEvolucion", codigoPlantillaEvolucion);
		return getUtilidadOdontologiaDao().confirmarIndicePlaca(con, campos);
	}
	
	/**
	 * metodo que actualiza el componente indice placa
	 * @param con
	 * @param dto
	 * @return boolean
	 */
	public static boolean actualizarComponenteIndicePlaca(Connection con, DtoComponenteIndicePlaca dto)
	{
		return getUtilidadOdontologiaDao().actualizarComponenteIndicePlaca(con, dto);
	}
	
	/**
	 * metodo de insercion de detalle de componente indice de placa
	 * @param con
	 * @param dto
	 * @param codigoSuperficie
	 * @return int
	 */
	public static int insertDetalleCompIndicePlaca(Connection con, DtoDetalleIndicePlaca dto)
	{
		return getUtilidadOdontologiaDao().insertDetalleCompIndicePlaca(con, dto);
	}
		
	/**
	 * metodo de eliminacion de detalle componente  indice placa
	 * @param con
	 * @param dto
	 * @return boolean
	 */
	public static boolean eliminarDetalleCompIndicePlaca(Connection con, DtoDetalleIndicePlaca dto)
	{
		return getUtilidadOdontologiaDao().eliminarDetalleCompIndicePlaca(con, dto);
	}
		
	/**
	 * metod que actualiza el indicador del detalle del indice de placa
	 * @param con
	 * @param dto
	 * @param codigoSuperficie
	 * @return boolean
	 */
	public static boolean actualizarDetalleCompIndicePlaca(Connection con, DtoDetalleIndicePlaca dto)
	{
		return getUtilidadOdontologiaDao().actualizarDetalleCompIndicePlaca(con, dto);
	}
		
	/**
	 * metodo que obtine el ultimo indice de placa
	 * @param connection
	 * @param codigoPaciente
	 * @return
	 */
	public static DtoComponenteIndicePlaca consultarComponenteIndicePlaca(Connection connection, int plantillaIngreso,int plantillaEvolucion, int codigoPaciente)
	{
		return getUtilidadOdontologiaDao().consultarComponenteIndicePlaca(connection, plantillaIngreso,plantillaEvolucion, codigoPaciente);
	}
		
	/**
	 * metodo que obtiene un listadod e piezas con las repectivas combinaciones de superficies 
	 * @param connection
	 * @param codigoInstitucion
	 * @param codigoCompIndPlaca
	 * @return
	 */
	public static ArrayList<DtoDetalleIndicePlaca> consultarDetalleCompIndicePlaca(
			Connection connection, 
			int codigoInstitucion, 
			int codigoCompIndPlaca)
	{
		return getUtilidadOdontologiaDao().consultarDetalleCompIndicePlaca(connection, codigoInstitucion, codigoCompIndPlaca);
	}
	
	/**
	 * Obtiene el c&oacute;digo y el nombre de la superficie seg&uacute;n el sector, estado y la instituci&oacute;n
	 * @param codigoInstitucion
	 * @param activo
	 * @param piezaDental
	 * @param sectores
	 * @return
	 */
	public static InfoDatosInt obtenerSuperficieDental(int codigoInstitucion, String activo, int sector, int piezaDental)
	{
		return getUtilidadOdontologiaDao().obtenerSuperficieDental(codigoInstitucion, activo, sector, piezaDental);
	}
	
	/**
	 * metodo que retorna una lista de antecedentes que requieren de alerta
	 * @param codigoPaciente
	 * @return ArrayList<DtoAntecedentesAlerta>
	 */
	public static ArrayList<DtoAntecedentesAlerta> obtenerAntecedentesAlerta(int codigoPaciente)
	{
		return getUtilidadOdontologiaDao().obtenerAntecedentesAlerta(codigoPaciente);
	}
	
	/**
	 * Método implementado para confirmar los antecedentes odontológicos
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ResultadoBoolean confirmarAntecedenteOdontologico(Connection con,int codigoMedico,String loginUsuario,String codigoPlantillaIngreso,String codigoPlantillaEvolucion)
	{
		HashMap campos = new HashMap();
		campos.put("codigoMedico", codigoMedico);
		campos.put("loginUsuario", loginUsuario);
		campos.put("codigoPlantillaIngreso",codigoPlantillaIngreso);
		campos.put("codigoPlantillaEvolucion",codigoPlantillaEvolucion);
		return getUtilidadOdontologiaDao().confirmarAntecedenteOdontologico(con, campos);
	}
	
	/**
	 * metodo que obtiene la condiciones toma del servicio
	 * @param codigoServicio
	 * @param codigoInstitucion
	 * @return
	 */
	public static ArrayList<InfoDatosInt> obtenerCondicionesTomaServicio(
			int codigoServicio, 
			int codigoInstitucion)
	{
		ArrayList<InfoDatosInt> array = new ArrayList<InfoDatosInt>();
		try
		{
			Connection con  = UtilidadBD.abrirConexion();
			array = getUtilidadOdontologiaDao().obtenerCondicionesTomaServicio(con, 
					Utilidades.convertirAEntero(ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(codigoInstitucion)),
					codigoServicio, 
					codigoInstitucion);
			UtilidadBD.cerrarConexion(con);
		}catch (Exception e) {e.printStackTrace();}
		return array;
	}
	
	/**
	 * metodo que obtiene la descripcion del servicios
	 * @param codigoTarifario
	 * @param codigoServicio
	 * @return
	 */
	public static String obenerNombreServicio (int codigoServicio, int codigoInstitucion)
	{
		return getUtilidadOdontologiaDao().obenerNombreServicio(codigoServicio, codigoInstitucion);
	}
	
	/**
	 * Método implementado para consultar el id del ingreso de una cita de odontologia
	 * @param con
	 * @param codigoCita
	 * @return
	 */
	public static BigDecimal consultarIngresoCitaOdontologica(Connection con,BigDecimal codigoCita)
	{
		return getUtilidadOdontologiaDao().consultarIngresoCitaOdontologica(con, codigoCita);
	}
	
	/**
	 * Método implementado para obtener el último registro de antecedentes odontologicos del paciente
	 * @param con
	 * @param dtoAntecedente
	 * @param camposParametrizables: atributo para validar que los ultimos antecedentes encontrados deben tener campos parametrizables
	 */
	public static void obtenerUltimoRegistroAntecedentesOdontologia(Connection con,DtoAntecendenteOdontologico dtoAntecedente,boolean camposParametrizables)
	{
		getUtilidadOdontologiaDao().obtenerUltimoRegistroAntecedentesOdontologia(con, dtoAntecedente,camposParametrizables);
	}
	
	/**
	 * Método para consultar la cita de un servicio del plan de tratamiento
	 * @param con
	 * @param progServPlanT
	 * @return
	 */
	public static ArrayList<BigDecimal> consultarCitaXProgSerPlanTrat(BigDecimal progServPlanT)
	{
		return getUtilidadOdontologiaDao().consultarCitaXProgSerPlanTrat(progServPlanT);
	}
	
	/**
	 * Método que verifica si un convenio de in ingreso esta relacionado a un presupuesto que está contratado
	 * @param con
	 * @param dtoSubCuenta
	 * @return
	 */
	public static boolean esConvenioRelacionadoAPresupuestoOdoContratado(Connection con,DtoSubCuentas dtoSubCuenta)
	{
		return getUtilidadOdontologiaDao().esConvenioRelacionadoAPresupuestoOdoContratado(con, dtoSubCuenta);
	}

	/**
	 * Metodo que verifica si el paciente tiene generada una órden médica
	 * generada previamente de tipo Interconsulta en estado Solicitada (Sin respuesta)
	 * y que no esté incluida en el plan de tratamiento .
	 * @param codigoCuentaActiva código del paciente a consultar
	 * @param codigoCuentaAsocio 
	 * @return true en caso de existir órden, false de lo contrario
	 */
	public static boolean validarTipoCitaInterconsulta(int codigoCuentaActiva, int codigoCuentaAsocio) {
		return getUtilidadOdontologiaDao().validarTipoCitaInterconsulta(codigoCuentaActiva, codigoCuentaAsocio);
	}
	
	/**
	 * 
	 * @param pieza
	 * @return
	 */
	public static String obtenerNombrePieza(int pieza)
	{
		return getUtilidadOdontologiaDao().obtenerNombrePieza(pieza);
	}

	/**
	 * 
	 * @param codigoPersona
	 * @param idIngreso
	 * @return
	 */
	public static boolean pacienteConValoracionInicial(int codigoPersona, int idIngreso) 
	{
		return getUtilidadOdontologiaDao().pacienteConValoracionInicial(codigoPersona, idIngreso);
	}
	
	/**
	 * M&eacute;todo encargado de verificar que la cita no 
	 * tenga servicios asignados a otra cita de menor orden para la
	 * misma pieza.
	 * @param codigoCita C&oacute;digo de la cita que se desea verificar
	 * @return boolean True en caso de existir servicios de menor órden asignados
	 */
	public static boolean existeServicioAsignadoDeMenorOrden(int codigoCita)
	{
		return getUtilidadOdontologiaDao().existeServicioAsignadoDeMenorOrden(codigoCita);
	}

	/**
	 * 
	 * @param superficie
	 * @return
	 */
	public static String obtenerNombreSuperficie(BigDecimal superficie) 
	{
		return getUtilidadOdontologiaDao().obtenerNombreSuperficie(superficie);
	}

	/**
	 * Metodo encargado de consultar los servicios asociados a una Solicitud de de Servicios InterConsulta 
	 * @param codigo
	 * @param codCuentaPaciente
	 * @return
	 */
	public static ArrayList<DtoServicioOdontologico> obtenerServiciosInterconsulta( int codCuentaPaciente, int codigoInstitucion, int codCita) {
		
		return getUtilidadOdontologiaDao().obtenerServiciosInterconsulta(codCuentaPaciente, codigoInstitucion,codCita);
	}

	/**
	 * Metodo utilizado para validar si Un profesional Tiene Asociado un Servicio 
	 * según la parametrica de  servicios adicionales por profesional de atención Odontológica
	 * @param codigoServicio
	 * @param codigoMedico
	 * @param unidadAgenda
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static boolean profesionalTieneAsosiadoServicioAdd(int codigoServicio, int codigoMedico,int codigoInstitucionInt) {
		
		return getUtilidadOdontologiaDao().profesionalTieneAsosiadoServicioAdd(codigoServicio,codigoMedico,codigoInstitucionInt);
	}

	
	/**
	 *  Metodo utilizado para Obtener el Numero de Solicitud de un Servicio de InterConsulta
	 * @param servicio
	 * @return
	 */
	public static int consultarNumeroSolicitudServicio(int servicio, int codCuentaPaciente) {
		
		return getUtilidadOdontologiaDao().consultarNumeroSolicitudServicio(servicio,codCuentaPaciente);
	}

	public static int obtenerCodigoProgramaHallazgoPiezaProgramaSerPT(Connection con, int codigoProgramaServicioPT) 
	{
		return getUtilidadOdontologiaDao().obtenerCodigoProgramaHallazgoPiezaProgramaSerPT(con,codigoProgramaServicioPT);
	}

	/**
	 * 
	 * @param codigoPkProgServ
	 * @param infoPlanTrata
	 * @return
	 */
	public static ArrayList<BigDecimal> consultarCitaXProgSerPlanTratHisConf(BigDecimal codigoPkProgServ, InfoPlanTratamiento infoPlanTrata) {
		return getUtilidadOdontologiaDao().consultarCitaXProgSerPlanTratHisConf(codigoPkProgServ, infoPlanTrata);
	}

	/**
	 * 
	 * @param con
	 * @param estado
	 * @param codigoPAciente 
	 * @return
	 */
	public static boolean pacienteConPlanTratamientoEnEstado(Connection con,String estado, int codigoPAciente) 
	{
		return getUtilidadOdontologiaDao().pacienteConPlanTratamientoEnEstado(con,estado,codigoPAciente);
	}
	
	
	/**
	 * 
	 * @param estado
	 * @param codigoPAciente
	 * @return
	 */
	public static boolean pacienteConPlanTratamientoEnEstados(ArrayList<String>estados, int codigoPAciente) 
	{
		boolean resultado=false;
		Connection con=UtilidadBD.abrirConexion();
		resultado=getUtilidadOdontologiaDao().pacienteConPlanTratamientoEnEstados(con,estados,codigoPAciente);
		UtilidadBD.closeConnection(con);
		return resultado;
	}

	
	

	
	
}