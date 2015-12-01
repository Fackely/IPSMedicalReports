package com.princetonsa.mundo.consultaExterna;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.Listado;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.consultaExterna.UtilidadesConsultaExterna;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.consultaExterna.MultasDao;
import com.princetonsa.dao.sqlbase.consultaExterna.SqlBaseMultasDao;
import com.princetonsa.dto.consultaExterna.DtoCitasNoRealizadas;
import com.princetonsa.dto.consultaExterna.DtoConceptoFacturaVaria;
import com.princetonsa.dto.consultaExterna.DtoMultasCitas;
import com.princetonsa.dto.consultaExterna.DtoServiciosCitas;
import com.princetonsa.dto.facturasVarias.DtoDeudor;
import com.princetonsa.dto.facturasVarias.DtoFacturaVaria;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.agenda.Cita;
import com.princetonsa.mundo.facturasVarias.AprobacionAnulacionFacturasVarias;
import com.princetonsa.mundo.facturasVarias.Deudores;
import com.princetonsa.mundo.facturasVarias.GenModFacturasVarias;
import com.servinte.axioma.mundo.fabrica.facturasvarias.FacturasVariasMundoFabrica;
import com.servinte.axioma.mundo.interfaz.facturasvarias.IFacturasVariasMundo;

/**
 * @author Jairo Gómez Fecha Junio de 2009
 */

public class Multas {

	private static Logger logger = Logger.getLogger(Multas.class);
	
	private String estadoActualizarCita; 
	
	private String motivoNoAtencion;
	
	private String conceptoFacturaVaria;
	
	private boolean aproAnulFactVar = false;
	
	private ArrayList<DtoCitasNoRealizadas> arrayCitasNoRealizadas = new ArrayList<DtoCitasNoRealizadas>();
	
	public static final String[] indicesCriterios = { "centAten0",
			"UnidadAgen1", "fechaIniCita2", "fechaFinCita3", "convenio4",
			"profesional5", "estado6", "institucion7" };

	
	
	public void clean()
	{
		this.estadoActualizarCita = "";
		this.motivoNoAtencion = "";
		this.conceptoFacturaVaria = "";
		this.aproAnulFactVar = false;
		this.arrayCitasNoRealizadas = new ArrayList<DtoCitasNoRealizadas>();
	}
	
	
	/**
	 * Constructor de la Clase
	 */
	public Multas() {
		this.init(System.getProperty("TIPOBD"));
	}

	/**
	 * DAO de este objeto, para trabajar con la fuente de datos
	 */
	private static MultasDao getMultasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getMultasDao();
	}

	/**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * 
	 * @param tipoBD
	 *            el tipo de base de datos que va a usar el objeto (e.g.,
	 *            Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD son
	 *            los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD) {
		// if ( aplicacionDao == null )
		// {
		// // Obtengo el DAO que encapsula las operaciones de BD de este objeto
		// DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		// aplicacionDao = myFactory.getAsignarCitasControlPostOperatorioDao();
		// if( aplicacionDao!= null )
		// return true;
		// }
		return false;
	}
	
	/**
	 * metodo que realiza las inserciones correspondientes para ingresar una cita No realizada
	 * @param connection
	 * @param usuario
	 * @return
	 */
	public boolean ingresarCitasNoRealizadas (Connection connection, UsuarioBasico usuario)
	{
		boolean transaction = UtilidadBD.iniciarTransaccion(connection);
		
		ResultadoBoolean resp = new ResultadoBoolean(false);
		Cita cita = new Cita();
		GenModFacturasVarias genModFacturasVarias = new GenModFacturasVarias();
		DtoDeudor dtoDeudor;
		DtoMultasCitas dtoMultasCitas;
		ArrayList<DtoMultasCitas> arrayDtoMultasCitas;	
		
		
		for ( Iterator iterador = this.arrayCitasNoRealizadas.listIterator(); iterador.hasNext(); ) 
		{
			DtoCitasNoRealizadas dtoCitasNoRealizadas = (DtoCitasNoRealizadas) iterador.next();
			
			/**
			 * actualiza el estado y el motivo de no atencion de la cita
			 */
			if(dtoCitasNoRealizadas.getChkEstado().equals(ConstantesBD.acronimoSi))
			{
				resp = cita.actualizarEstadoCitaTransaccional(connection, this.estadoActualizarCita.equals(ConstantesBD.acronimoSi)?9:8, dtoCitasNoRealizadas.getCodCita(), this.motivoNoAtencion, ConstantesBD.inicioTransaccion, usuario.getLoginUsuario());
				if(resp.isTrue())
				{
					ArrayList<Integer> sol = UtilidadesConsultaExterna.consultarSolicitudesSinFacturar(connection, dtoCitasNoRealizadas.getCodCita());
					if(sol.size()>0)
					for ( Iterator itSol = sol.listIterator(); itSol.hasNext(); ) 
					{
						int numSolicitud = (Integer) itSol.next();
						resp = actualizarEstadoMedicoSolicitudYDetCargos(connection, numSolicitud);
					}
				} else {
					transaction = false;
					logger.info("LA TRANSACCION FUE ABORTADA POR FALLAS EN LA ACTUALIZACION DE ESTADO Y MOTIVO NO ATENCION");
					UtilidadBD.abortarTransaccion(connection);
				}
				
			}
			
			if (transaction)
			{
				/**
				 * inserta la informacion correspondiente a la Multa
				 */
				int codigoMultas=0;
				if(dtoCitasNoRealizadas.getChkMulta().equals(ConstantesBD.acronimoSi))
				{
					codigoMultas = this.insertarMulta(connection, usuario, dtoCitasNoRealizadas.getConManMulta(), dtoCitasNoRealizadas.getValMulta(), dtoCitasNoRealizadas.getCodCita());
					
					if ( codigoMultas == 0 )
					{
						transaction = false;
						logger.info("LA TRANSACCION FUE ABORTADA POR FALLAS EN LA INSERCION DE LA MULTA");
						UtilidadBD.abortarTransaccion(connection);
						return false;
					}
				}
				if(dtoCitasNoRealizadas.getChkFactura().equals(ConstantesBD.acronimoSi))
				{
					/**
					 * insertar deudores
					 */
					dtoDeudor = Deudores.cargarInformacionNuevoDeudor(connection, dtoCitasNoRealizadas.getCodPaciente()+"", ConstantesIntegridadDominio.acronimoPaciente);
					
					String codigoDeudor = "";
					logger.info("CODIGO DEL DEUDOR: "+dtoDeudor.getCodigo()+" ¿Existe deudor paciente? "+dtoDeudor.isExisteDeudorPaciente());
					if(!dtoDeudor.isExisteDeudorPaciente())
					{
						//Se asigna la información que faltaba
						dtoDeudor.setCodigoInstitucion(usuario.getCodigoInstitucionInt());
						dtoDeudor.setUsuarioModifica(usuario);
						resp = Deudores.ingresar(connection, dtoDeudor);
						codigoDeudor = dtoDeudor.getCodigo();
					}
					else
					{
						codigoDeudor = dtoDeudor.getCodigo();
					}
					if(!resp.isTrue())
					{
						transaction = false;
						logger.info("LA TRANSACCION FUE ABORTADA POR FALLAS EN LA INSERCION DEL DEUDOR");
						UtilidadBD.abortarTransaccion(connection);
					}
					/**
					 * inserta factura varia
					 */
					else
					{
						
						IFacturasVariasMundo facturasVariasMundo=FacturasVariasMundoFabrica.crearFacturasVariasMundo();

						String fecha=UtilidadFecha.getFechaActual();
						
						DtoFacturaVaria dtoFacturaVaria = new DtoFacturaVaria();
						
						dtoFacturaVaria.setEstadoFactura(ConstantesIntegridadDominio.acronimoEstadoFacturaGenerada);
						dtoFacturaVaria.setFecha(fecha);
						dtoFacturaVaria.setFechaAprobacion(fecha);
						dtoFacturaVaria.setUsuarioModifica(usuario.getLoginUsuario());
						dtoFacturaVaria.setCentroAtencion(usuario.getCodigoCentroAtencion());
						dtoFacturaVaria.setCentroCosto(usuario.getCodigoCentroCosto());
						dtoFacturaVaria.setConcepto(Utilidades.convertirALong(this.conceptoFacturaVaria));
						dtoFacturaVaria.setDeudor(Utilidades.convertirAEntero(codigoDeudor));
						dtoFacturaVaria.setObservaciones("");
						dtoFacturaVaria.setInstitucion(usuario.getCodigoInstitucionInt());
						
						dtoMultasCitas = new DtoMultasCitas();
						dtoMultasCitas.setCodigoMulta(codigoMultas);
						dtoMultasCitas.setSeleccionado(true);
						arrayDtoMultasCitas = new ArrayList<DtoMultasCitas>();
						arrayDtoMultasCitas.add(dtoMultasCitas);
						
						dtoFacturaVaria.setMultasCitas(arrayDtoMultasCitas);

						if (dtoCitasNoRealizadas.getConManMulta() != null && dtoCitasNoRealizadas.getConManMulta().equals(ConstantesBD.acronimoSi)){
						
							dtoFacturaVaria.setValorFactura(new BigDecimal(dtoCitasNoRealizadas.getValMulta()));
							
						}else{
							
							dtoFacturaVaria.setValorFactura(new BigDecimal(ValoresPorDefecto.getValorMultaPorIncumplimientoCitas(usuario.getCodigoInstitucionInt())));
						}
							
						//dtoFacturaVaria.setValorFactura(new BigDecimal(dtoVenta.getValorTotalTarjetas()));
						
						facturasVariasMundo.generarFacturaVaria(dtoFacturaVaria);

						//String nombreConsecutivo=genModFacturasVarias.obtenerNombreConsecutivoFacturasVarias(usuario);
						//String consecutivo=UtilidadBD.obtenerValorConsecutivoDisponible(nombreConsecutivo, usuario.getCodigoInstitucionInt());
						//genModFacturasVarias.setConsecutivo(Utilidades.convertirAEntero(consecutivo));
						//genModFacturasVarias.setEstadoFactura();
					
						//genModFacturasVarias.setFecha(Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
						//genModFacturasVarias.setConcepto(Utilidades.convertirAEntero(this.conceptoFacturaVaria));

						//genModFacturasVarias.setDeudor(Utilidades.convertirAEntero(codigoDeudor));
						//genModFacturasVarias.setObservaciones("");
						//genModFacturasVarias.setInstitucion(usuario.getCodigoInstitucionInt());
						
//						dtoMultasCitas = new DtoMultasCitas();
//						dtoMultasCitas.setCodigoMulta(codigoMultas);
//						dtoMultasCitas.setSeleccionado(true);
//						arrayDtoMultasCitas = new ArrayList<DtoMultasCitas>();
//						arrayDtoMultasCitas.add(dtoMultasCitas);
//						
//						genModFacturasVarias.setMultasFactura(arrayDtoMultasCitas);

						//resp = genModFacturasVarias.insertarFacturaVaria(connection, usuario.getLoginUsuario());
						//logger.info("DEBO APROBAR? "+this.aproAnulFactVar+ " codigo factura varia: "+resp.getDescripcion());
						
						if(dtoFacturaVaria.getCodigoFacturaVaria()==null || dtoFacturaVaria.getCodigoFacturaVaria() <= 0)
						{
							transaction = false;
							logger.info("LA TRANSACCION FUE ABORTADA POR FALLAS EN LA INSERCION DE LA FACTURA VARIA");
							UtilidadBD.abortarTransaccion(connection);
						
						}else if(this.aproAnulFactVar){
							
							HashMap temporal = new HashMap();
							temporal = AprobacionAnulacionFacturasVarias.aprobacionFacturaVaria(connection, resp.getDescripcion(), UtilidadFecha.getFechaActual(), "", usuario.getLoginUsuario());
							if (!UtilidadTexto.getBoolean(temporal.get("exito")+""))
							{
								ActionErrors actionErrors = new ActionErrors ();
								actionErrors = (ActionErrors)temporal.get("error");
								logger.info("LA TRANSACCION FUE ABORTADA POR"+ actionErrors.toString());
							}
						}
					}
				}
			}
		}
		if (transaction)
			UtilidadBD.finalizarTransaccion(connection);
		else
		{
			logger.info("LA TRANSACCION FUE ABORTADA");
		}
		return transaction;
	}
	
	/**
	 * Metodo que inserta una multa en la tabla Multas_citas
	 * @param connection
	 * @param usuario
	 * @param convenioManejaMulta
	 * @param valorMultaConvenio
	 * @param codCita 
	 * @return
	 */
	public int insertarMulta (Connection connection, UsuarioBasico usuario, String convenioManejaMulta, String valorMultaConvenio, int codCita)
	{
		return getMultasDao().insertarMulta(connection, usuario, convenioManejaMulta, valorMultaConvenio, codCita);
	}

	/**
	 * 
	 * @param connection
	 * @param criterios
	 * @param usuario 
	 * @return
	 */
	public ArrayList<DtoCitasNoRealizadas> buscarCitasNoRealizadasRango(Connection connection,
			HashMap criterios, UsuarioBasico usuario) {
		return getMultasDao().buscarCitasNoRealizadasRango(connection, criterios, usuario);
	}

	public ArrayList<DtoServiciosCitas> buscarDetalleCitasRango(Connection connection,
			UsuarioBasico usuario, DtoCitasNoRealizadas dtoCitasNoRealizadas) {
		return getMultasDao().buscarDetalleCitasRango(connection, usuario, dtoCitasNoRealizadas);
	}

	public ArrayList<DtoConceptoFacturaVaria> setArrayConcepFact(
			Connection connection, UsuarioBasico usuario) {
		return getMultasDao().setArrayConcepFact(connection, usuario);
	}
	
	public static ArrayList<DtoCitasNoRealizadas> ordenarColumna(ArrayList<DtoCitasNoRealizadas> lista, String ultimaPropiedad,String propiedad){
		
		ArrayList<DtoCitasNoRealizadas> listaOrdenada = new ArrayList<DtoCitasNoRealizadas>();
	
		HashMap mapaCitasNoRealizadas=new HashMap();
		mapaCitasNoRealizadas.put("numRegistros","0");
		
		HashMap mapaOrdenado= new HashMap();
		mapaOrdenado.put("numRegistros","0");
		
		String[] indices={"unidadAgenda_", "codUnidadAgenda_", "fechaCita_", "horaCita_", "codEstadoCita_", "estadoCita_", "profesional_",
				"codProfesional_", "codCentroAtencion_", "nombreCentroAtencion_", "idPaciente_", "codPaciente_", "chkEstado_", "chkMulta_",
				"chkFactura_", "nombrePaciente_", "consultorio_", "codAgenda_", "chkMultaActivo_", "valMulta_", "conManMulta_", "codCita_"};
		
		logger.info ("\n\n Tamaño Lista >> "+lista.size());
		
		int i = 0;
		for ( Iterator iterador = lista.listIterator(); iterador.hasNext(); ) 
		{	
			DtoCitasNoRealizadas dtoCitasNoRealizadas = (DtoCitasNoRealizadas) iterador.next();
			
			mapaCitasNoRealizadas.put("unidadAgenda_"+i, dtoCitasNoRealizadas.getUnidadAgenda()+"");
			mapaCitasNoRealizadas.put("codUnidadAgenda_"+i, dtoCitasNoRealizadas.getCodUnidadAgenda()+"");
			mapaCitasNoRealizadas.put("fechaCita_"+i, dtoCitasNoRealizadas.getFechaCita()+"");
			mapaCitasNoRealizadas.put("horaCita_"+i, dtoCitasNoRealizadas.getHoraCita()+"");
			mapaCitasNoRealizadas.put("codEstadoCita_"+i, dtoCitasNoRealizadas.getCodEstadoCita()+"");
			mapaCitasNoRealizadas.put("estadoCita_"+i, dtoCitasNoRealizadas.getEstadoCita()+"");
			mapaCitasNoRealizadas.put("profesional_"+i, dtoCitasNoRealizadas.getProfesional()+"");
			mapaCitasNoRealizadas.put("codProfesional_"+i, dtoCitasNoRealizadas.getCodProfesional()+"");
			mapaCitasNoRealizadas.put("codCentroAtencion_"+i, dtoCitasNoRealizadas.getCodCentroAtencion()+"");
			mapaCitasNoRealizadas.put("nombreCentroAtencion_"+i, dtoCitasNoRealizadas.getNombreCentroAtencion()+"");
			mapaCitasNoRealizadas.put("idPaciente_"+i, dtoCitasNoRealizadas.getIdPaciente()+"");
			mapaCitasNoRealizadas.put("codPaciente_"+i, dtoCitasNoRealizadas.getCodPaciente()+"");
			mapaCitasNoRealizadas.put("chkEstado_"+i, dtoCitasNoRealizadas.getChkEstado()+"");
			mapaCitasNoRealizadas.put("chkMulta_"+i, dtoCitasNoRealizadas.getChkMulta()+"");
			mapaCitasNoRealizadas.put("chkFactura_"+i, dtoCitasNoRealizadas.getChkFactura()+"");
			mapaCitasNoRealizadas.put("nombrePaciente_"+i, dtoCitasNoRealizadas.getNombrePaciente()+"");
			mapaCitasNoRealizadas.put("consultorio_"+i, dtoCitasNoRealizadas.getConsultorio()+"");
			mapaCitasNoRealizadas.put("codAgenda_"+i, dtoCitasNoRealizadas.getCodAgenda()+"");
			mapaCitasNoRealizadas.put("chkMultaActivo_"+i, dtoCitasNoRealizadas.getChkMultaActivo()+"");
			mapaCitasNoRealizadas.put("valMulta_"+i, dtoCitasNoRealizadas.getValMulta()+"");
			mapaCitasNoRealizadas.put("conManMulta_"+i, dtoCitasNoRealizadas.getConManMulta()+"");
			mapaCitasNoRealizadas.put("codCita_"+i, dtoCitasNoRealizadas.getCodCita()+"");
			
			mapaCitasNoRealizadas.put("numRegistros", (i+1));
			
			i++;
		}
		
		mapaOrdenado = Listado.ordenarMapa(indices, propiedad, ultimaPropiedad, mapaCitasNoRealizadas, Utilidades.convertirAEntero(mapaCitasNoRealizadas.get("numRegistros").toString()));
		mapaOrdenado.put("numRegistros", mapaCitasNoRealizadas.get("numRegistros").toString());
		
	    
		for(int j=0; j< Utilidades.convertirAEntero(mapaOrdenado.get("numRegistros").toString()); j++)
	 	{
			DtoCitasNoRealizadas dto = new DtoCitasNoRealizadas();
			
			dto.setUnidadAgenda(mapaOrdenado.get("unidadAgenda_"+j).toString());
			dto.setCodUnidadAgenda(Utilidades.convertirAEntero(mapaOrdenado.get("codUnidadAgenda_"+j).toString()));
			dto.setFechaCita(mapaOrdenado.get("fechaCita_"+j).toString());
			dto.setHoraCita(mapaOrdenado.get("horaCita_"+j).toString());
			dto.setCodEstadoCita(Utilidades.convertirAEntero(mapaOrdenado.get("codEstadoCita_"+j).toString()));
			dto.setEstadoCita(mapaOrdenado.get("estadoCita_"+j).toString());
			dto.setProfesional(mapaOrdenado.get("profesional_"+j).toString());
			dto.setCodProfesional(Utilidades.convertirAEntero(mapaOrdenado.get("codProfesional_"+j).toString()));
			dto.setCodCentroAtencion(Utilidades.convertirAEntero(mapaOrdenado.get("codCentroAtencion_"+j).toString()));
			dto.setNombreCentroAtencion(mapaOrdenado.get("nombreCentroAtencion_"+j).toString());
			dto.setIdPaciente(mapaOrdenado.get("idPaciente_"+j).toString());
			dto.setCodPaciente(Utilidades.convertirAEntero(mapaOrdenado.get("codPaciente_"+j).toString()));
			dto.setChkEstado(mapaOrdenado.get("chkEstado_"+j).toString());
			dto.setChkMulta(mapaOrdenado.get("chkMulta_"+j).toString());
			dto.setChkFactura(mapaOrdenado.get("chkFactura_"+j).toString());
			dto.setNombrePaciente(mapaOrdenado.get("nombrePaciente_"+j).toString());
			dto.setConsultorio(mapaOrdenado.get("consultorio_"+j).toString());
			dto.setCodAgenda(Utilidades.convertirAEntero(mapaOrdenado.get("codAgenda_"+j).toString()));
			dto.setChkMultaActivo(mapaOrdenado.get("chkMultaActivo_"+j).toString());
			dto.setValMulta(mapaOrdenado.get("valMulta_"+j).toString());
			dto.setConManMulta(mapaOrdenado.get("conManMulta_"+j).toString());
			dto.setCodCita(Utilidades.convertirAEntero(mapaOrdenado.get("codCita_"+j).toString()));
			
			listaOrdenada.add(dto);
		}
		
		return listaOrdenada;	
	}

	/**
	 * @param estadoActualizarCita the estadoActualizarCita to set
	 */
	public void setEstadoActualizarCita(String estadoActualizarCita) {
		this.estadoActualizarCita = estadoActualizarCita;
	}

	/**
	 * @return the estadoActualizarCita
	 */
	public String getEstadoActualizarCita() {
		return estadoActualizarCita;
	}


	/**
	 * @return the motivoNoAtencion
	 */
	public String getMotivoNoAtencion() {
		return motivoNoAtencion;
	}


	/**
	 * @param motivoNoAtencion the motivoNoAtencion to set
	 */
	public void setMotivoNoAtencion(String motivoNoAtencion) {
		this.motivoNoAtencion = motivoNoAtencion;
	}


	/**
	 * @return the citasNoRealizadas
	 */
	public ArrayList<DtoCitasNoRealizadas> getCitasNoRealizadas() {
		return arrayCitasNoRealizadas;
	}


	/**
	 * @param citasNoRealizadas the citasNoRealizadas to set
	 */
	public void setCitasNoRealizadas(
			ArrayList<DtoCitasNoRealizadas> citasNoRealizadas) {
		this.arrayCitasNoRealizadas = citasNoRealizadas;
	}


	/**
	 * @return the conceptoFacturaVaria
	 */
	public String getConceptoFacturaVaria() {
		return conceptoFacturaVaria;
	}


	/**
	 * @param conceptoFacturaVaria the conceptoFacturaVaria to set
	 */
	public void setConceptoFacturaVaria(String conceptoFacturaVaria) {
		this.conceptoFacturaVaria = conceptoFacturaVaria;
	}


	/**
	 * @param aproAnulFactVar the aproAnulFactVar to set
	 */
	public void setAproAnulFactVar(boolean aproAnulFactVar) {
		this.aproAnulFactVar = aproAnulFactVar;
	}


	/**
	 * @return the aproAnulFactVar
	 */
	public boolean isAproAnulFactVar() {
		return aproAnulFactVar;
	}
	
	/**
	 * Modificar el estado multa cita
	 * @param Connection connection
	 * @param HashMap parametros
	 * @return boolean
	 */
	public static boolean actualizarEstadoMultaCita(Connection connection, String estado, String consecutivo)
	{
		HashMap parametros = new HashMap();
		parametros.put("estado", estado);
		parametros.put("consecutivo", consecutivo);
		return getMultasDao().actualizarEstadoMultaCita(connection, parametros); 
	}
	
	/**
	 * Verificar el estado de la cita
	 * @param Connection connection
	 * @param HashMap parametros
	 * @return boolean
	 */
	public boolean verificarEstadoMultaCita(Connection connection, String estado, String consecutivo)
	{
		HashMap parametros = new HashMap();
		parametros.put("estado", estado);
		parametros.put("consecutivo", consecutivo);
		return getMultasDao().verificarEstadoMultaCita(connection, parametros);
	}
	
	/**
	 * se Obtiene los conceptos que estan activo
	 * @param Connection connection
	 * @param UsuarioBasico usuario
	 * @return ArrayList<DtoConceptoFacturaVaria>
	 */
	public ArrayList<DtoConceptoFacturaVaria> getArrayConcepFactVarias(Connection connection, UsuarioBasico usuario)
	{
		return getMultasDao().getArrayConcepFactVarias(connection, usuario);
	}


	public ArrayList<DtoCitasNoRealizadas> buscarCitasNoRealizadasPaciente(
			Connection connection, HashMap criterios, UsuarioBasico usuario,
			PersonaBasica paciente) {
		return getMultasDao().buscarCitasNoRealizadasPaciente(connection, criterios, usuario, paciente);
	}
	
	public ResultadoBoolean actualizarEstadoMedicoSolicitudYDetCargos(Connection connection, int numSolicitud)
	{
		return getMultasDao().actualizarEstadoMedicoSolicitudYDetCargos(connection, numSolicitud);
	}
}