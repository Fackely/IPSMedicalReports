package com.mercury.mundo.odontologia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.struts.action.ActionErrors;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatos;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.odontologia.InfoDetallePlanTramiento;
import util.odontologia.InfoHallazgoSuperficie;
import util.odontologia.InfoPlanTratamiento;

import com.mercury.dao.odontologia.TratamientoOdontologiaDao;
import com.mercury.dto.odontologia.DtoDiagCartaDental;
import com.mercury.dto.odontologia.DtoOtroHallazgo;
import com.mercury.dto.odontologia.DtoTipoHallazgosPieza;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.administracion.DtoEspecialidades;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.cargos.Cargos;
import com.princetonsa.mundo.ordenesmedicas.procedimientos.RespuestaProcedimientos;
import com.princetonsa.mundo.solicitudes.SolicitudProcedimiento;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.HistoricoCopCeo;
import com.servinte.axioma.orm.TipoHallazgoCeoCop;
import com.servinte.axioma.orm.delegate.odontologia.HistoricoCeoCopDelegate;

/**
 * 
 *
 */
@SuppressWarnings("unchecked")
public class TratamientoOdontologia
{
	private int									codigo;

	private InfoDatosInt						tipoTratamiento;

	private UsuarioBasico						medico;

	private String								fecIniciacion;

	private String								fecFinalizacion;

	private String								diagPlanTratamiento;
	
	private String								motivoConsulta;

	private int									codPaciente;

	private ArrayList							evoluciones;

	private ArrayList							analisisSecciones;

	private ArrayList							obsSecciones;

	private ArrayList							tratamientosDientes;

	private String								observaciones;

	private ArrayList							odontogramas;

	private ArrayList							indicesPlaca;

	private ArrayList							procedimientos;
	
	private ArrayList							consentimientos;
	
	private HashMap									dientesAusentes;

	private static TratamientoOdontologiaDao	tratamientoOdontologia;
	
	public static final int					TIPOTRATAMIENTOODONTOLOGIA=1;
	
	public static final int					TIPOTRATAMIENTOHIGIENEORAL=5;
	
	private int 							especialidad;

	private PersonaBasica					paciente;
	
	private Integer especialidadPorfesionalSalud;
	
	private String informacionProfesionalMedico;
	
	private String nombreRegistroMedico;

	public static TratamientoOdontologiaDao getTratamientoOdontologiaDao()
	{
		if (tratamientoOdontologia == null)
		{
			String tipoBD = System.getProperty("TIPOBD");
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			tratamientoOdontologia = myFactory.getTratamientoOdontologiaDao();
		}
		return tratamientoOdontologia;
	}

	public TratamientoOdontologia()
	{
		this.codigo = -1;
		this.tipoTratamiento = new InfoDatosInt();
		this.medico = new UsuarioBasico();
		this.fecIniciacion = "";
		this.diagPlanTratamiento = "";
		this.motivoConsulta = "";
		this.codPaciente = -1;
		this.observaciones = "";

		this.evoluciones = new ArrayList();
		this.analisisSecciones = new ArrayList();
		this.obsSecciones = new ArrayList();
		this.odontogramas = new ArrayList();
		this.indicesPlaca = new ArrayList();
		this.tratamientosDientes = new ArrayList();
		this.procedimientos = new ArrayList();
		this.consentimientos = new ArrayList();
		this.dientesAusentes = new HashMap();
		this.especialidadPorfesionalSalud= new Integer(0);
		this.informacionProfesionalMedico="";
		this.nombreRegistroMedico="";
	}

	public int getCodigo()
	{
		return this.codigo;
	}

	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}

	public InfoDatosInt getTipoTratamiento()
	{
		return this.tipoTratamiento;
	}

	public void setTipoTratamiento(InfoDatosInt tipoTratamiento)
	{
		this.tipoTratamiento = tipoTratamiento;
	}

	public String getFecIniciacion()
	{
		return this.fecIniciacion;
	}

	public void setFecIniciacion(String fecIniciacion)
	{
		this.fecIniciacion = fecIniciacion;
	}

	public String getFecFinalizacion()
	{
		return this.fecFinalizacion;
	}

	public void setFecFinalizacion(String fecFinalizacion)
	{
		this.fecFinalizacion = fecFinalizacion;
	}

	public String getDiagPlanTratamiento()
	{
		return this.diagPlanTratamiento;
	}

	public void setDiagPlanTratamiento(String diagPlanTratamiento)
	{
		this.diagPlanTratamiento = diagPlanTratamiento;
	}

	public UsuarioBasico getMedico()
	{
		return this.medico;
	}

	public void setMedico(UsuarioBasico medico)
	{
		this.medico = medico;
	}

	public int getCodPaciente()
	{
		return this.codPaciente;
	}

	public void setCodPaciente(int codPaciente)
	{
		this.codPaciente = codPaciente;
	}

	public void addEvolucion(EvolucionOdontologia evolucion)
	{
		this.evoluciones.add(evolucion);
	}

	public EvolucionOdontologia getEvolucion(int i)
	{
		return (EvolucionOdontologia) this.evoluciones.get(i);
	}

	public int getNumEvoluciones()
	{
		return this.evoluciones.size();
	}

	public void addTratamientoDiente(
			TratamientoDienteEnSeccion tratamientoDiente)
	{
		this.tratamientosDientes.add(tratamientoDiente);
	}

	public TratamientoDienteEnSeccion getTratamientoDiente(int i)
	{
		return (TratamientoDienteEnSeccion) this.tratamientosDientes.get(i);
	}

	public int getNumTratamientosDientes()
	{
		return this.tratamientosDientes.size();
	}

	public void addOdontograma(Odontograma odontograma)
	{
		this.odontogramas.add(odontograma);
	}

	public Odontograma getOdontograma(int i)
	{
		return (Odontograma) this.odontogramas.get(i);
	}

	public int getNumOdontogramas()
	{
		return this.odontogramas.size();
	}

	public void addIndicePlaca(IndicePlaca indicePlaca)
	{
		this.indicesPlaca.add(indicePlaca);
	}

	public IndicePlaca getIndicePlaca(int i)
	{
		return (IndicePlaca) this.indicesPlaca.get(i);
	}

	public int getNumIndicesPlaca()
	{
		return this.indicesPlaca.size();
	}

	public void addAnalisis(AnalisisTratamiento analisis)
	{
		this.analisisSecciones.add(analisis);
	}

	public AnalisisTratamiento getAnalisis(int i)
	{
		return (AnalisisTratamiento) this.analisisSecciones.get(i);
	}

	public int getNumAnalisis()
	{
		return this.analisisSecciones.size();
	}

	public void addObsSeccion(InfoDatosInt observacion)
	{
		this.obsSecciones.add(observacion);
	}

	public InfoDatosInt getObsSeccion(int i)
	{
		return (InfoDatosInt) this.obsSecciones.get(i);
	}

	public int getNumObsSecciones()
	{
		return this.obsSecciones.size();
	}

	public String getObservaciones()
	{
		return this.observaciones;
	}

	public void setObservaciones(String observaciones)
	{
		this.observaciones = observaciones;
	}

	public void addProcedimiento(Procedimiento procedimiento)
	{
		this.procedimientos.add(procedimiento);
	}

	public Procedimiento getProcedimiento(int i)
	{
		return (Procedimiento) this.procedimientos.get(i);
	}

	public int getNumProcedimientos()
	{
		return this.procedimientos.size();
	}
	
	public void addConsentimiento(ConsentimientoInformado consentimiento)
	{
		this.consentimientos.add(consentimiento);
	}
	
	public ConsentimientoInformado getConsentimiento(int i)
	{
		return (ConsentimientoInformado)this.consentimientos.get(i);
	}
	
	public int getNumConsentimientos()
	{
		return this.consentimientos.size();
	}
	
	private void modificarEvoluciones(Connection con) throws SQLException
	{
		for (int i = 0; i < this.getNumEvoluciones(); i++)
		{
			EvolucionOdontologia evolucion = this.getEvolucion(i);
			if (!evolucion.getEnBD()) // si no esta en la bd se
										// inserta
			{
				TratamientoOdontologia.getTratamientoOdontologiaDao()
						.insertarEvolucion(
								con,
								evolucion.getCodigo(),
								this.getCodigo(),
								evolucion.getDescripcion(),
								evolucion.getMedico()
										.getCodigoPersona(),
								evolucion.getFecha(),
								evolucion.getObservaciones());
			}
			else
			{
				TratamientoOdontologia.getTratamientoOdontologiaDao()
						.modificarEvolucion(con, evolucion.getCodigo(),
								this.getCodigo(),
								evolucion.getObservaciones());
			}
		}
	}
	
	private void modificarAnalisis(Connection con) throws SQLException
	{
		for (int i = 0; i < this.getNumAnalisis(); i++)
		{
			AnalisisTratamiento analisis = this.getAnalisis(i);
			if (!analisis.getEnBD()) // si no esta en la bd se
										// inserta
			{
				TratamientoOdontologia.getTratamientoOdontologiaDao()
						.insertarAnalisis(
								con,
								this.getCodigo(),
								analisis.getTipoAnalisis().getCodigo(),
								analisis.getOpcionTipoAnalisis()
										.getId(),
								analisis.getComentario(),
								analisis.getObservaciones());
			}
			else
			{
				TratamientoOdontologia.getTratamientoOdontologiaDao()
						.modificarAnalisis(con,
								analisis.getTipoAnalisis().getCodigo(),
								this.getCodigo(),
								analisis.getObservaciones());
			}
		}
	}
	
	private void modificarObsSecciones(Connection con) throws SQLException
	{
		for (int i = 0; i < this.getNumObsSecciones(); i++)
		{
			InfoDatosInt obsSeccion = this.getObsSeccion(i);
			if (!TratamientoOdontologia.getTratamientoOdontologiaDao()
					.existenObservacionesSeccion(con, this.getCodigo(),
							obsSeccion.getCodigo())) // si no esta en
														// la bd se
														// inserta
			{
				TratamientoOdontologia.getTratamientoOdontologiaDao()
						.insertarObservacionesSeccion(con,
								this.getCodigo(),
								obsSeccion.getCodigo(),
								obsSeccion.getDescripcion());
			}
			else
			{
				TratamientoOdontologia.getTratamientoOdontologiaDao()
						.modificarObservacionesSeccion(con,
								this.getCodigo(),
								obsSeccion.getCodigo(),
								obsSeccion.getDescripcion());
			}
		}
	}
	
	private void modificarOdontogramas(Connection con) throws SQLException
	{
		for (int i = 0; i < this.getNumOdontogramas(); i++)
		{
			Odontograma odontograma = this.getOdontograma(i);
			if(!odontograma.isOdontogramaTemporal())
			{
				if (!odontograma.getEnBD()) // si es nuevo
				{
					odontograma.setCodTratamiento(this.getCodigo() + "");
					odontograma.insertar(con);						
				}
				else
				{
					odontograma.modificar(con);
				}
			}
		}
	}
	
	private void modificarIndicePlaca(Connection con) throws SQLException
	{
		for (int i = 0; i < this.getNumIndicesPlaca(); i++)
		{
			IndicePlaca indicePlaca = this.getIndicePlaca(i);
			if (!indicePlaca.getEnBD()) // si es nuevo
			{
				indicePlaca.setCodTratamiento(this.getCodigo() + "");
				indicePlaca.insertar(con);
			}
			else
			{
				indicePlaca.modificar(con);
			}
		}
	}
	
	private void modificarTratamientosDientes(Connection con) throws SQLException
	{
		for (int i = 0; i < this.getNumTratamientosDientes(); i++) // se recorren cada uno de los tratamientos creados por diente
		{
			TratamientoDienteEnSeccion tratamientoDiente = this
					.getTratamientoDiente(i);
			if (!tratamientoDiente.getEnBD()) // si es nuevo, ingreso el tratamiento del diente
			{
				int codigoTratamientoDiente = TratamientoOdontologia
						.getTratamientoOdontologiaDao()
						.insertarTratamientoDiente(
								con,
								tratamientoDiente.getNumeroDiente(),
								tratamientoDiente
										.getCodSeccionTratamientoInst(),
								this.getCodigo(),
								tratamientoDiente.getMedico()
										.getCodigoPersona(),
								tratamientoDiente.getFecha(),
								tratamientoDiente.getObservaciones());
				tratamientoDiente.setCodigo(codigoTratamientoDiente);
			}
			else
			{
				TratamientoOdontologia.getTratamientoOdontologiaDao()
						.modificarTratamientoDiente(con,
								tratamientoDiente.getCodigo(),
								tratamientoDiente.getObservaciones());
			}

			for (int j = 0; j < tratamientoDiente
					.getNumAnalisisDiente(); j++)
			{
				AnalisisTratamiento analisisDiente = tratamientoDiente
						.getAnalisisDiente(j);
				if (!analisisDiente.getEnBD()) // si no está en la bd entonces lo inserto como nuevo
				{
					TratamientoOdontologia
							.getTratamientoOdontologiaDao()
							.insertarAnalisisDiente(
									con,
									tratamientoDiente.getCodigo(),
									analisisDiente.getTipoAnalisis()
											.getCodigo(),
									analisisDiente
											.getOpcionTipoAnalisis()
											.getId(),
									analisisDiente.getComentario(),
									analisisDiente.getObservaciones());
				}
				else
				{
					TratamientoOdontologia
							.getTratamientoOdontologiaDao()
							.modificarAnalisisDiente(
									con,
									tratamientoDiente.getCodigo(),
									analisisDiente.getTipoAnalisis()
											.getCodigo(),
									analisisDiente.getObservaciones());
				}
			}
		}
	}
	
	private void modificarProcedimientos(Connection con) throws SQLException
	{
		for (int i = 0; i < this.getNumProcedimientos(); i++)
		{
			Procedimiento procedimiento = this.getProcedimiento(i);
			if (!procedimiento.getEnBD()) // si no esta en la bd se inserta
			{
				TratamientoOdontologia.getTratamientoOdontologiaDao()
						.insertarProcedimiento(
								con,
								procedimiento.getCodigoServicio(),
								this.getCodigo(),
								procedimiento.getPrioridad(),
								procedimiento.getFechaRegistro(),
								procedimiento.getMedicoRegistra().getCodigoPersona(),
								procedimiento.getFechaPlaneado(),
								procedimiento.getSuperficieDental().getCodigo(),
								procedimiento.getDiente(),
								procedimiento.getObservaciones(), 
								procedimiento.getEstado(),procedimiento.getFechaCerrado(),(procedimiento.getMedicoCierra().getCodigoPersona()>0)?String.valueOf(procedimiento.getMedicoCierra().getCodigoPersona()):"");
			}
			else
			{
				TratamientoOdontologia.getTratamientoOdontologiaDao()
						.modificarProcedimiento(con, procedimiento.getConsecutivo(),
								procedimiento.getPrioridad(),
								procedimiento.getEstado(),
								procedimiento.getFechaCerrado(),
								(procedimiento.getMedicoCierra().getCodigoPersona()>0)?String.valueOf(procedimiento.getMedicoCierra().getCodigoPersona()):"",
								procedimiento.getObservaciones());
			}
			if(procedimiento.getEstadoEnBD()!=Procedimiento.REALIZADO && procedimiento.getEstado()==Procedimiento.REALIZADO && paciente.getCodigoCuenta()!=0)
			{
				generarSolicitudProcediemitno(con, procedimiento);
			}

		}
	}
	
	private void modificarConsentimientos(Connection con) throws SQLException
	{
		for (int i=0; i<this.getNumConsentimientos(); i++)
		{
			ConsentimientoInformado consentimiento = this.getConsentimiento(i);
			if(!consentimiento.getEnBD())
			{
				consentimiento.setCodigo(TratamientoOdontologia.getTratamientoOdontologiaDao().insertarConsentimientoInformado(
						con, consentimiento.getFecha(), consentimiento.getMedico().getCodigoPersona(),
						this.getCodigo(), consentimiento.getTipo().getCodigo()));
				
				for(int j=0; j<consentimiento.getNumValores(); j++)
				{
					int codValorOpcion = Integer.parseInt(consentimiento.getValor("codigo_"+j)+"");
					TratamientoOdontologia.getTratamientoOdontologiaDao().insertarValorOpcionConsentimientoInformado(
							con, consentimiento.getCodigo(), codValorOpcion, consentimiento.getValor("valor_"+codValorOpcion)+"");
				}
			}
		}
	}

	public void modificar(Connection con) throws SQLException	
	{
		boolean inicioTransaccion = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(System
				.getProperty("TIPOBD"));

		try
		{
			inicioTransaccion = myFactory.beginTransaction(con);
			if (inicioTransaccion)
			{
				TratamientoOdontologia.getTratamientoOdontologiaDao()
						.modificar(con, this.getCodigo(),
								this.getFecFinalizacion(),
								this.getObservaciones());
				
				this.modificarEvoluciones(con);
				this.modificarAnalisis(con);
				this.modificarObsSecciones(con);
				this.modificarOdontogramas(con);
				this.modificarIndicePlaca(con);
				this.modificarTratamientosDientes(con);
				this.modificarProcedimientos(con);
				this.modificarConsentimientos(con);
				myFactory.endTransaction(con);
				
				this.insertarHistoricoCeoCopOdotongramas();
			}
			else
			{
				throw new SQLException("No se pudo iniciar la transacción");
			}
		}
		catch (SQLException e)
		{
			if (inicioTransaccion)
				myFactory.abortTransaction(con);
			throw e;
		}
	}

	private void insertarEvoluciones(Connection con) throws SQLException
	{
		for (int i = 0; i < this.getNumEvoluciones(); i++)
		{
			EvolucionOdontologia evolucion = this.getEvolucion(i);
			TratamientoOdontologia.getTratamientoOdontologiaDao()
					.insertarEvolucion(con, evolucion.getCodigo(),
							this.getCodigo(),
							evolucion.getDescripcion(),
							evolucion.getMedico().getCodigoPersona(),
							evolucion.getFecha(),
							evolucion.getObservaciones());
		}
	}

	private void insertarAnalisis(Connection con) throws SQLException
	{
		for (int i = 0; i < this.getNumAnalisis(); i++)
		{
			AnalisisTratamiento analisis = this.getAnalisis(i);
			TratamientoOdontologia.getTratamientoOdontologiaDao()
					.insertarAnalisis(con, this.getCodigo(),
							analisis.getTipoAnalisis().getCodigo(),
							analisis.getOpcionTipoAnalisis().getId(),
							analisis.getComentario(),
							analisis.getObservaciones());
		}
	}
	
	private void insertarObsSecciones(Connection con) throws SQLException
	{
		for (int i = 0; i < this.getNumObsSecciones(); i++)
		{
			InfoDatosInt obsSeccion = this.getObsSeccion(i);
			TratamientoOdontologia.getTratamientoOdontologiaDao()
					.insertarObservacionesSeccion(con,
							this.getCodigo(), obsSeccion.getCodigo(),
							obsSeccion.getDescripcion());
		}
	}
	
	
	/**
	 * 
	 * @param con
	 * @throws SQLException
	 */
	private void insertarOdotongramas(Connection con) throws SQLException
	{
		for (int i = 0; i < this.getNumOdontogramas(); i++)
		{
			Odontograma odontograma = this.getOdontograma(i);
			odontograma.setCodTratamiento(this.getCodigo() + "");
			odontograma.insertar(con);
		}
	}

	private void insertarOtrosHallazgos(Connection con) throws SQLException
	{
		/* FIXME Hacer la inserción de los nuevos hallazgos */
		Log4JManager.info("No inserta otros hallazgos ");
	}

	
	private void insertarIndicesPlaca(Connection con) throws SQLException
	{
		for (int i = 0; i < this.getNumIndicesPlaca(); i++)
		{
			IndicePlaca indicePlaca = this.getIndicePlaca(i);
			indicePlaca.setCodTratamiento(this.getCodigo() + "");
			indicePlaca.insertar(con);
		}
	}
	
	private void insertarTratamientosDientes(Connection con) throws SQLException
	{
		for (int i = 0; i < this.getNumTratamientosDientes(); i++) 
		{
			TratamientoDienteEnSeccion tratamientoDiente = this
					.getTratamientoDiente(i);
			int codigoTratamientoDiente = TratamientoOdontologia
					.getTratamientoOdontologiaDao()
					.insertarTratamientoDiente(
							con,
							tratamientoDiente.getNumeroDiente(),
							tratamientoDiente
									.getCodSeccionTratamientoInst(),
							this.getCodigo(),
							tratamientoDiente.getMedico()
									.getCodigoPersona(),
							tratamientoDiente.getFecha(),
							tratamientoDiente.getObservaciones());
			tratamientoDiente.setCodigo(codigoTratamientoDiente);

			for (int j = 0; j < tratamientoDiente
					.getNumAnalisisDiente(); j++)
			{
				AnalisisTratamiento analisisDiente = tratamientoDiente
						.getAnalisisDiente(j);
				TratamientoOdontologia.getTratamientoOdontologiaDao()
						.insertarAnalisisDiente(
								con,
								tratamientoDiente.getCodigo(),
								analisisDiente.getTipoAnalisis()
										.getCodigo(),
								analisisDiente.getOpcionTipoAnalisis()
										.getId(),
								analisisDiente.getComentario(),
								analisisDiente.getObservaciones());
			}
		}
	}
	
	private void insertarProcedimientos(Connection con) throws SQLException
	{
		for (int i = 0; i < this.getNumProcedimientos(); i++)
		{
			Procedimiento procedimiento = this.getProcedimiento(i);
			TratamientoOdontologia.getTratamientoOdontologiaDao()
			.insertarProcedimiento(
					con,
					procedimiento.getCodigoServicio(),
					this.getCodigo(),
					procedimiento.getPrioridad(),
					procedimiento.getFechaRegistro(),
					procedimiento.getMedicoRegistra().getCodigoPersona(),
					procedimiento.getFechaPlaneado(),
					procedimiento.getSuperficieDental().getCodigo(),
					procedimiento.getDiente(),
					procedimiento.getObservaciones(), 
					procedimiento.getEstado(),procedimiento.getFechaCerrado(),(procedimiento.getMedicoCierra().getCodigoPersona()>0)?String.valueOf(procedimiento.getMedicoCierra().getCodigoPersona()):"");
			
			// Generar solicitud procedimiento
			if(procedimiento.getEstado()==Procedimiento.REALIZADO && paciente.getCodigoCuenta()!=0 )
			{
				generarSolicitudProcediemitno(con, procedimiento);
				
			}
		}
	}
	
	
	/**
	 * 
	 * @param con
	 * @throws SQLException
	 */
	public void insertarHistoricoCeoCopOdotongramas() throws SQLException
	{
		HibernateUtil.beginTransaction();
		HistoricoCeoCopDelegate historicoDelegate = new HistoricoCeoCopDelegate();
		int total = 0;
		
		try {
			for (int i = 0; i < this.getNumOdontogramas(); i++)
			{
				Odontograma odontograma = this.getOdontograma(i);
				
				if(!odontograma.isOdontogramaTemporal())
				{
					if (!odontograma.getEnBD()) { // si es nuevo
					
						Set<Long> listaTipos = odontograma.getDtoTiposHallazgosPieza().getTotalTiposHallazgo().keySet();
						for (Long tipoHallazgoCeoCop : listaTipos) {
							TipoHallazgoCeoCop tipoHallazgo = new TipoHallazgoCeoCop();
							tipoHallazgo.setCodigo(tipoHallazgoCeoCop);

							total = odontograma.getDtoTiposHallazgosPieza().getTotalTiposHallazgo().get(tipoHallazgoCeoCop);
							HistoricoCopCeo historicoCopCeo = new HistoricoCopCeo();
							historicoCopCeo.setCodigoOdontograma(odontograma.getCodigo());
							historicoCopCeo.setTipoHallazgoCeoCop(tipoHallazgo);
							historicoCopCeo.setCantidad(total);

							historicoDelegate.persist(historicoCopCeo);
						}
					}
				}	
			}
			HibernateUtil.endTransaction();
		} catch (Exception e) {
			HibernateUtil.abortTransaction();
		}
	}
	
	
	@SuppressWarnings("unused")
	private void generarSolicitudProcediemitno(Connection con,Procedimiento procedimiento) throws SQLException
	{
		SolicitudProcedimiento solicitud=new SolicitudProcedimiento();
		ActionErrors errores = new ActionErrors();
		
		//********************************NUEVO CÓDIGO SOLICITUD DE PROCEDIMIENTOS**********************************
		solicitud.setTipoSolicitud(new InfoDatosInt (ConstantesBD.codigoTipoSolicitudProcedimiento, ""));
		solicitud.setCobrable(true);
		String fecha=UtilidadFecha.getFechaActual();
		String hora=UtilidadFecha.getHoraActual();
		solicitud.setFechaSolicitud(fecha);
		solicitud.setHoraSolicitud(hora);
		solicitud.setSolPYP(false);
		
		solicitud.setComentario(procedimiento.getObservaciones());
		solicitud.setNumeroDocumento(solicitud.numeroDocumentoSiguiente(con));
		
		solicitud.setMultiple(false);
		solicitud.setUrgente(false);
		solicitud.setCobrable(true);
		
		solicitud.setCodigoMedicoSolicitante(medico.getCodigoPersona());
		solicitud.setCodigoMedicoResponde(medico.getCodigoPersona());
		solicitud.setCodigoServicioSolicitado(procedimiento.getCodigoServicio());
		solicitud.setEspecialidadSolicitante(new InfoDatosInt(especialidad));
		solicitud.setCentroCostoSolicitado(new InfoDatosInt(medico.getCodigoCentroCosto()));
		solicitud.setCodigoCuenta(paciente.getCodigoCuenta());
		solicitud.setOcupacionSolicitado(new InfoDatosInt(medico.getCodigoOcupacionMedica()));
		solicitud.setFinalidad(procedimiento.getFinalidad());
		solicitud.setEspecialidadSolicitadaOrdAmbulatorias(this.especialidadPorfesionalSalud);
		solicitud.setDatosMedico(informacionProfesionalMedico);
		
		
		//Inicio de transaccion
		UtilidadBD.iniciarTransaccion(con);
		
		boolean inserto=false;
		if(solicitud.insertarTransaccional(con, ConstantesBD.continuarTransaccion, solicitud.getNumeroDocumento(), paciente.getCodigoCuenta(), true,ConstantesBD.codigoNuncaValido+"")>0)
		{
			inserto = true;
		}
		if(inserto)
		{
			Cargos car=new Cargos();
		    inserto=car.generarSolicitudSubCuentaCargoServiciosEvaluandoCobertura(	con, 
					medico, 
					paciente, 
					false/*dejarPendiente*/, 
					solicitud.getNumeroSolicitud(), 
					ConstantesBD.codigoTipoSolicitudProcedimiento /*codigoTipoSolicitudOPCIONAL*/, 
					paciente.getCodigoCuenta() /*codigoCuentaOPCIONAL*/, 
					medico.getCodigoCentroCosto()/*codigoCentroCostoEjecutaOPCIONAL*/, 
					solicitud.getCodigoServicioSolicitado()/*codigoServicioOPCIONAL*/, 
					1 /*cantidadServicioOPCIONAL*/, 
					ConstantesBD.codigoNuncaValidoDouble/*valorTarifaOPCIONAL*/, 
					ConstantesBD.codigoNuncaValido /*codigoEvolucionOPCIONAL*/,
					/*numeroAutorizacionOPCIONAL*/
					""/*esPortatil*/,
					false,
					null,"");

			RespuestaProcedimientos respuesta=new RespuestaProcedimientos();
			//respuesta.set
			String secuencia = respuesta.insertarRespuestaProcedimiento(
					con, 
					solicitud.getNumeroSolicitud()+"",
					fecha, 
					procedimiento.getObservaciones(),
					"",
					ConstantesBD.codigoTipoRecargoSinRecargo,
					"",
					hora,
					medico.getCodigoPersona(),
					medico.getLoginUsuario(),solicitud.getFinalidad(),"");
			
			solicitud.cambiarEstadosSolicitud(con, solicitud.getNumeroSolicitud(), 0, ConstantesBD.codigoEstadoHCRespondida);
			
			UtilidadBD.finalizarTransaccion(con);
		}
		else
		{
			UtilidadBD.abortarTransaccion(con);
		}
	}

	private void insertarConsentimientos(Connection con) throws SQLException
	{
		for (int i=0; i<this.getNumConsentimientos(); i++)
		{
			ConsentimientoInformado consentimiento = this.getConsentimiento(i);
			consentimiento.setCodigo(TratamientoOdontologia.getTratamientoOdontologiaDao().insertarConsentimientoInformado(
					con, consentimiento.getFecha(), consentimiento.getMedico().getCodigoPersona(),
					this.getCodigo(), consentimiento.getTipo().getCodigo()));
			
			for(int j=0; j<consentimiento.getNumValores(); j++)
			{
				int codValorOpcion = Integer.parseInt(consentimiento.getValor("codigo_"+j)+"");
				TratamientoOdontologia.getTratamientoOdontologiaDao().insertarValorOpcionConsentimientoInformado(
						con, consentimiento.getCodigo(), codValorOpcion, consentimiento.getValor("valor_"+codValorOpcion)+"");
			}
		}
	}
	/**
	 * 
	 * @param con
	 * @throws SQLException
	 */
	public void insertar(Connection con) throws SQLException
	{
		boolean inicioTransaccion = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(System
				.getProperty("TIPOBD"));

		try
		{
			inicioTransaccion = myFactory.beginTransaction(con);
			if (inicioTransaccion)
			{
				this.codigo = TratamientoOdontologia
						.getTratamientoOdontologiaDao().insertar(con,
								this.getCodPaciente(),
								this.getTipoTratamiento().getCodigo(),
								this.getMedico().getCodigoPersona(),
								this.getFecIniciacion(),
								this.getDiagPlanTratamiento(),
								this.getObservaciones(),
								this.getMotivoConsulta(),
								this.nombreRegistroMedico,
								this.especialidadPorfesionalSalud
								);
				
				this.insertarEvoluciones(con);
				this.insertarAnalisis(con);
				this.insertarObsSecciones(con);
				this.insertarOdotongramas(con);
				this.insertarOtrosHallazgos(con);
				this.insertarIndicesPlaca(con);
				this.insertarTratamientosDientes(con);
				this.insertarProcedimientos(con);
				this.insertarConsentimientos(con);
				
				myFactory.endTransaction(con);
				
				this.insertarHistoricoCeoCopOdotongramas();
				
			}
			else
			{
				throw new SQLException("No se pudo iniciar la transacción");
			}
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			if (inicioTransaccion)
				myFactory.abortTransaction(con);
		}
	}

	private void consultarEvoluciones(Connection con) throws SQLException
	{
		this.evoluciones.clear();
		ResultSetDecorator rsEvoluciones = TratamientoOdontologia.getTratamientoOdontologiaDao().consultarEvoluciones(con, this.getCodigo());
		while (rsEvoluciones.next())
		{
			EvolucionOdontologia evolucion = new EvolucionOdontologia();
			evolucion.setCodigo(rsEvoluciones.getInt("codigo"));
			evolucion.setDescripcion(rsEvoluciones
					.getString("descripcion"));
			UsuarioBasico tempoMedico = new UsuarioBasico();
			tempoMedico.cargarUsuarioBasico(con, rsEvoluciones
					.getInt("codMedico"));
			evolucion.setMedico(tempoMedico);
			evolucion.setFecha(rsEvoluciones.getString("fecha"));
			evolucion.setObservaciones(rsEvoluciones
					.getString("observaciones"));
			evolucion.setEnBD(true);
			this.addEvolucion(evolucion);
		}
	}
	
	private void consultarAnalisis(Connection con) throws SQLException
	{
		this.analisisSecciones.clear();
		ResultSetDecorator rsAnalisis = TratamientoOdontologia.getTratamientoOdontologiaDao().consultarAnalisis(con, this.getCodigo());
		while (rsAnalisis.next())
		{
			AnalisisTratamiento analisis = new AnalisisTratamiento();
			InfoDatosInt tipoAnalisis = new InfoDatosInt();
			tipoAnalisis.setCodigo(rsAnalisis
					.getInt("codTipoAnalisisInst"));
			tipoAnalisis.setNombre(rsAnalisis
					.getString("nomTipoAnalisis"));
			analisis.setTipoAnalisis(tipoAnalisis);
			InfoDatos opcionTipoAnalisis = new InfoDatos();
			opcionTipoAnalisis.setId(rsAnalisis
					.getString("codOpcionTipoAnalisis"));
			opcionTipoAnalisis.setNombre(rsAnalisis
					.getString("nomOpcionTipoAnalisis"));
			analisis.setOpcionTipoAnalisis(opcionTipoAnalisis);
			analisis.setComentario(rsAnalisis.getString("comentario"));
			analisis.setObservaciones(rsAnalisis
					.getString("observaciones"));
			analisis.setEnBD(true);
			this.addAnalisis(analisis);
		}
	}
	
	private void consultarTratamientosDientes(Connection con) throws SQLException
	{
		this.tratamientosDientes.clear();
		// se cargan los tratamientos realizados a los dientes en las
		// secciones
		ResultSetDecorator rsTratamientosDientes = TratamientoOdontologia.getTratamientoOdontologiaDao()	.consultarTratamientosDientes(con, this.getCodigo());
		while (rsTratamientosDientes.next())
		{
			TratamientoDienteEnSeccion tratamientoDiente = new TratamientoDienteEnSeccion();
			tratamientoDiente.setCodigo(rsTratamientosDientes
					.getInt("codigo"));
			tratamientoDiente.setNumeroDiente(rsTratamientosDientes
					.getInt("numeroDiente"));
			tratamientoDiente
					.setCodSeccionTratamientoInst(rsTratamientosDientes
							.getInt("codSeccionTratamientoInst"));
			tratamientoDiente.setObservaciones(rsTratamientosDientes
					.getString("observaciones"));
			tratamientoDiente.setEnBD(true);
			UsuarioBasico tempoMedico = new UsuarioBasico();
			tempoMedico.cargarUsuarioBasico(con, rsTratamientosDientes
					.getInt("codMedico"));
			tratamientoDiente.setMedico(tempoMedico);
			tratamientoDiente.setFecha(rsTratamientosDientes
					.getString("fecha"));

			ResultSetDecorator rsAnalisisDiente = TratamientoOdontologia.getTratamientoOdontologiaDao().consultarAnalisisDiente(con,	tratamientoDiente.getCodigo());
			while (rsAnalisisDiente.next())
			{
				AnalisisTratamiento analisisDiente = new AnalisisTratamiento();
				InfoDatosInt tipoAnalisis = new InfoDatosInt();
				tipoAnalisis.setCodigo(rsAnalisisDiente
						.getInt("codTipoAnalisisInst"));
				tipoAnalisis.setNombre(rsAnalisisDiente
						.getString("nomTipoAnalisis"));
				analisisDiente.setTipoAnalisis(tipoAnalisis);
				InfoDatos opcionTipoAnalisis = new InfoDatos();
				opcionTipoAnalisis.setId(rsAnalisisDiente
						.getString("codOpcionTipoAnalisis"));
				opcionTipoAnalisis.setNombre(rsAnalisisDiente
						.getString("nomOpcionTipoAnalisis"));
				analisisDiente
						.setOpcionTipoAnalisis(opcionTipoAnalisis);
				analisisDiente.setComentario(rsAnalisisDiente
						.getString("comentario"));
				analisisDiente.setObservaciones(rsAnalisisDiente
						.getString("observaciones"));
				analisisDiente.setEnBD(true);
				tratamientoDiente.addAnalisisDiente(analisisDiente);
			}
			this.addTratamientoDiente(tratamientoDiente);
		}
	}
	
	private void consultarObsSecciones(Connection con) throws SQLException
	{
		this.obsSecciones.clear();
		ResultSetDecorator rsObsSecciones = TratamientoOdontologia.getTratamientoOdontologiaDao().consultarObservacionesSeccion(con, this.getCodigo());
		while (rsObsSecciones.next())
		{
			InfoDatosInt obsSeccion = new InfoDatosInt();
			obsSeccion.setCodigo(rsObsSecciones
					.getInt("codSeccionTratamiento"));
			obsSeccion.setNombre(rsObsSecciones
					.getString("nomSeccionTratamiento"));
			obsSeccion.setDescripcion(rsObsSecciones
					.getString("observaciones"));
			this.addObsSeccion(obsSeccion);
		}
	}
	
	private void consultarOdontogramas(Connection con) throws SQLException
	{
		this.odontogramas.clear();
		Collection tempOdontogramas = Odontograma.consultarOdontogramasTratamiento(con, this.getCodigo());
		Iterator iteraOdontogramas = tempOdontogramas.iterator();
		while (iteraOdontogramas.hasNext())
		{
			HashMap tempOdo = (HashMap) iteraOdontogramas.next();
			int codOdo = Utilidades.convertirAEntero((tempOdo.get("codigo")+""));
			Odontograma odontograma = new Odontograma();
			odontograma.consultar(con, codOdo);
			this.addOdontograma(odontograma);
		}
	}
	
	private void consultarIndicesPlaca(Connection con) throws SQLException
	{
		this.indicesPlaca.clear();
		Collection<HashMap<String, Object>> tempIndicesPlaca = IndicePlaca.consultarIndicesPlacaTratamiento(con, this.getCodigo());
		Iterator iteraIndicesPlaca = tempIndicesPlaca.iterator();
		while (iteraIndicesPlaca.hasNext())
		{
			HashMap tempIndicePlaca = (HashMap) iteraIndicesPlaca
					.next();
			
			int codIndicePlaca =0;
			codIndicePlaca = Integer.parseInt(tempIndicePlaca.get("codigo").toString());
			IndicePlaca indicePlaca = new IndicePlaca();
			indicePlaca.consultar(con, codIndicePlaca);
			this.addIndicePlaca(indicePlaca);
		}
	}
	
	private void consultarProcedimientos(Connection con) throws SQLException
	{
		this.procedimientos.clear();
		ResultSetDecorator rsProcedimientos = TratamientoOdontologia.getTratamientoOdontologiaDao().consultarProcedimientos(con, this.getCodigo());
		while (rsProcedimientos.next())
		{
			Procedimiento procedimiento = new Procedimiento();
			UsuarioBasico tempoMedico;
			
			procedimiento.setConsecutivo(rsProcedimientos.getInt("consecutivo"));
			procedimiento.setCodigoServicio(rsProcedimientos.getInt("codigoservicio"));
			procedimiento.setCodigoCUPS(rsProcedimientos.getInt("codigocups"));
			procedimiento.setDescripcionServicio(rsProcedimientos.getString("descripcionservicio"));
			procedimiento.setDescripcionEspecialidadServicio(rsProcedimientos.getString("descespecialidadservicio"));
			procedimiento.setCodigoTratamiento(codigo);
			procedimiento.setPrioridad(rsProcedimientos.getInt("prioridad"));
			procedimiento.setFechaRegistro(rsProcedimientos.getString("fecharegistro"));

			tempoMedico = new UsuarioBasico();
			tempoMedico.cargarUsuarioBasico(con, rsProcedimientos.getInt("codMedicoRegistra"));
			procedimiento.setMedicoRegistra(tempoMedico);
			
			procedimiento.setFechaPlaneado( (rsProcedimientos.getString("fechaplaneado")!=null)?rsProcedimientos.getString("fechaplaneado"):"" );
			procedimiento.setDiente(rsProcedimientos.getInt("diente"));
			
			procedimiento.setAplicaA(rsProcedimientos.getString("nombreSuperficieDental"));
			
			
			procedimiento.setSuperficieDental(new InfoDatosInt(
							rsProcedimientos.getInt("codigoSuperficieDental"),
							rsProcedimientos.getString("nombreSuperficieDental")));
			
			procedimiento.setEstado(rsProcedimientos.getInt("estado"));
			procedimiento.setEstadoEnBD(procedimiento.getEstado());
			procedimiento.setFechaCerrado(rsProcedimientos.getString("fechacerrado"));

			tempoMedico = new UsuarioBasico();
			tempoMedico.cargarUsuarioBasico(con, rsProcedimientos.getInt("codMedicoCierra"));
			procedimiento.setMedicoCierra(tempoMedico);
			
			procedimiento.setObservaciones(rsProcedimientos.getString("observaciones"));
			
			procedimiento.setEnBD(true);
			this.addProcedimiento(procedimiento);
		}
	}
	
	private void consultarConsentimientos(Connection con) throws SQLException
	{
		this.consentimientos.clear();
		ResultSetDecorator rsConsentimientos = TratamientoOdontologia.getTratamientoOdontologiaDao().consultarConsentimientosInformados(con, this.getCodigo());
		while (rsConsentimientos.next())
		{
			ConsentimientoInformado consentimiento = new ConsentimientoInformado();
			UsuarioBasico tempoMedico;
			
			consentimiento.setCodigo(rsConsentimientos.getInt("codigo"));
			consentimiento.setFecha(rsConsentimientos.getString("fecha"));
			consentimiento.setEnBD(true);
			InfoDatosInt tipoConsentimientoInf = new InfoDatosInt();
			tipoConsentimientoInf.setCodigo(rsConsentimientos.getInt("codigoTipo"));
			tipoConsentimientoInf.setNombre(rsConsentimientos.getString("nombreTipo"));
			tipoConsentimientoInf.setDescripcion(rsConsentimientos.getString("nombreReporte"));
			consentimiento.setTipo(tipoConsentimientoInf);
			tempoMedico = new UsuarioBasico();
			tempoMedico.cargarUsuarioBasico(con, rsConsentimientos.getInt("codMedico"));
			consentimiento.setMedico(tempoMedico);
			
			ResultSetDecorator rsValoresOpcionesConsentimiento = 
				TratamientoOdontologia.getTratamientoOdontologiaDao().consultarValoresOpcionesConsentimientoInformado(
						con, consentimiento.getCodigo());
			
			int i=0;
			while(rsValoresOpcionesConsentimiento.next())
			{
				int codValor = rsValoresOpcionesConsentimiento.getInt("codigoOpcion");
				consentimiento.setValor("codigo_"+i, codValor);
				consentimiento.setValor("nombre_"+codValor, rsValoresOpcionesConsentimiento.getString("nombreOpcion"));
				consentimiento.setValor("valor_"+codValor, rsValoresOpcionesConsentimiento.getString("valor"));
				i++;
			}
			consentimiento.setNumValores(i);
			
			this.addConsentimiento(consentimiento);
		}
	}
	
	public static HashMap consultarDientesAusentes(Connection con, int codPaciente) throws SQLException
	{
		HashMap tempDientesAusentes = new HashMap();
		int estados[]=new int[3], i;
		estados[0]=DienteOdontograma.ANODONCIAVERDADERA;
		estados[1]=DienteOdontograma.AUSENTE;
		estados[2]=DienteOdontograma.SINERUPCIONAR;
		
		ResultSetDecorator rsDientesAusentes = Odontograma.getOdontogramaDao().consultarDientesPacientePorEstados(con, codPaciente, estados);
		
		i=0;
		while(rsDientesAusentes.next())
		{
			tempDientesAusentes.put(rsDientesAusentes.getInt("numeroDiente")+"", "true");
			i++;
		}
		rsDientesAusentes.close();
		tempDientesAusentes.put("cantidad", i);
		return tempDientesAusentes;
	}
	
	public void consultar(Connection con, int codigo) throws SQLException
	{
		ResultSetDecorator rs = null;
		try {
			rs = TratamientoOdontologia.getTratamientoOdontologiaDao().consultar(con, codigo);
			if (rs.next())
			{
				this.setCodigo(codigo);
				this.setCodPaciente(rs.getInt("codPaciente"));
				this.setTipoTratamiento(new InfoDatosInt(rs.getInt("codTipoTratamiento"), 
						rs.getString("nomTipoTratamiento")));
				this.medico.cargarUsuarioBasico(con, rs.getInt("codMedico"));
				this.setFecIniciacion(rs.getString("fecIniciacion"));
				this.setFecFinalizacion(rs.getString("fecFinalizacion"));
				this.setDiagPlanTratamiento(rs.getString("diagPlanTratamiento"));
				this.setObservaciones(rs.getString("observaciones"));
				this.setMotivoConsulta(rs.getString("motivo_consulta"));
				this.setInformacionProfesionalMedico(rs.getString("PROFESIONAL_RESPONSABLE"));
				this.setEspecialidadPorfesionalSalud(rs.getInt("ESPECIALIDAD_PROFESIONAL"));

				this.consultarEvoluciones(con);
				this.consultarAnalisis(con);
				this.consultarTratamientosDientes(con);
				this.consultarObsSecciones(con);
				this.consultarOdontogramas(con);
				// llenar lista de piezas dentales
				// iterar hallazgos y llenar las superficies
				this.consultarIndicesPlaca(con);
				this.consultarProcedimientos(con);
				this.consultarConsentimientos(con);
				//this.dientesAusentes = TratamientoOdontologia.consultarDientesAusentes(con, this.getCodPaciente());
			}
			else {
				throw new SQLException("No existe tratamiento de codigo: " + codigo + " consultarTratamientoOdontologia");
			}
		} catch (Exception e) {
			Log4JManager.error("Problemas Consultadon Odontograma: ", e);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
			} catch (Exception e2) {
				Log4JManager.error("Cerrando Resulset", e2);
			}
		}
	}
/**
 * 
 * @param con
 * @param codPaciente
 * @return
 * @throws SQLException
 */
	public static Collection consultarTratamientosPaciente(Connection con,
			int codPaciente) throws SQLException
	{
		ResultSetDecorator rs = TratamientoOdontologia.getTratamientoOdontologiaDao()
				.consultarTratamientosPaciente(con, codPaciente);
		return UtilidadBD.resultSet2Collection(rs);
	}

	
	/**
	 * 
	 * @param con
	 * @param codPaciente
	 * @return
	 * @throws SQLException
	 */
	public static Collection consultarTratamientosSinFinalizarPaciente(
			Connection con, int codPaciente) throws SQLException
	{
		ResultSetDecorator rs = TratamientoOdontologia.getTratamientoOdontologiaDao()
				.consultarTratamientosSinFinalizarPaciente(con, codPaciente);
		Collection c= UtilidadBD.resultSet2Collection(rs);
		rs.close();
		return c;
	}

	public HashMap getDientesAusentes()
	{
		return dientesAusentes;
	}
	
	public static boolean existenTratamientosPaciente(Connection con, int codPaciente) throws SQLException
	{
		return TratamientoOdontologia.getTratamientoOdontologiaDao().existenTratamientosPaciente(con, codPaciente);
	}
	
	
	public static HashMap consultarDiagnosicosCartaDent(Connection con,int codigoInst){
		
		return TratamientoOdontologia.getTratamientoOdontologiaDao().consultarDiagnosicosCartaDent(con, codigoInst);
	
	}
	
	public static ArrayList<DtoDiagCartaDental> insertarDatosSeleccionDiag (ArrayList<DtoDiagCartaDental> array,String codigoElementodAdd,String nombreElementoAdd)
	{		
		
		DtoDiagCartaDental dto=new DtoDiagCartaDental();
		
		if(!codigoElementodAdd.toString().equals("") 
				&& !nombreElementoAdd.toString().equals(""))
		{
			
			dto.setCodigoPk(Utilidades.convertirAEntero(codigoElementodAdd));
			dto.setNombreEstSecDiente(nombreElementoAdd);
			array.add(dto);
		}
		
		return array;	
	}

	public static HashMap consultarTratamientosCartaDent(Connection con, int codigoInst) {
		return TratamientoOdontologia.getTratamientoOdontologiaDao().consultarTratamientosCartaDent(con, codigoInst);
	}
	
	
	
	public static HashMap consultarSuperficiesDiente(Connection con, int codigoInst){
		return TratamientoOdontologia.getTratamientoOdontologiaDao().consultarSuperficiesCartaDental(con, codigoInst);
		
	}

	/**
	 * @return Retorna atributo especialidad
	 */
	public int getEspecialidad()
	{
		return especialidad;
	}

	/**
	 * @param especialidad Asigna atributo especialidad
	 */
	public void setEspecialidad(int especialidad)
	{
		this.especialidad = especialidad;
	}

	/**
	 * @return Retorna atributo paciente
	 */
	public PersonaBasica getPaciente()
	{
		return paciente;
	}

	/**
	 * @param paciente Asigna atributo paciente
	 */
	public void setPaciente(PersonaBasica paciente)
	{
		this.paciente = paciente;
	}

	/**
	 * @return Retorna atributo motivoConsulta
	 */
	public String getMotivoConsulta()
	{
		return motivoConsulta;
	}

	/**
	 * @param motivoConsulta Asigna atributo motivoConsulta
	 */
	public void setMotivoConsulta(String motivoConsulta)
	{
		this.motivoConsulta = motivoConsulta;
	}
	
	
	/**
	 * Metodo que se encarga de consultar especialidades de un medico
	 * @param codMedico
	 * @return lista de especialdiades
	 * @throws SQLException
	 */
	public static ArrayList<DtoEspecialidades> consultarEspecialidadesMedico(Integer codMedico) throws SQLException
	{
		
		return TratamientoOdontologia.getTratamientoOdontologiaDao()
		.consultarEspecialidadesMedico(codMedico);
	}

	/**
	 * @return the especialidadPorfesionalSalud
	 */
	public Integer getEspecialidadPorfesionalSalud() {
		return especialidadPorfesionalSalud;
	}

	/**
	 * @param especialidadPorfesionalSalud the especialidadPorfesionalSalud to set
	 */
	public void setEspecialidadPorfesionalSalud(Integer especialidadPorfesionalSalud) {
		this.especialidadPorfesionalSalud = especialidadPorfesionalSalud;
	}

	/**
	 * @return the informacionProfesionalMedico
	 */
	public String getInformacionProfesionalMedico() {
		return informacionProfesionalMedico;
	}

	/**
	 * @param informacionProfesionalMedico the informacionProfesionalMedico to set
	 */
	public void setInformacionProfesionalMedico(String informacionProfesionalMedico) {
		this.informacionProfesionalMedico = informacionProfesionalMedico;
	}

	/**
	 * @return the nombreRegistroMedico
	 */
	public String getNombreRegistroMedico() {
		return nombreRegistroMedico;
	}

	/**
	 * @param nombreRegistroMedico the nombreRegistroMedico to set
	 */
	public void setNombreRegistroMedico(String nombreRegistroMedico) {
		this.nombreRegistroMedico = nombreRegistroMedico;
	}
	
	/**
	 * Método encargado de obtener el hallazgo odontologico especificado
	 * @param Connection con
	 * @param ArrayList<Integer> hallazgosDentales
	 * @return ArrayList<TipoHallazgoCeoCop> 
	 */
	public ArrayList<TipoHallazgoCeoCop> consultarCEOCOP(Connection con, ArrayList<Integer> hallazgosDentales) {
		return TratamientoOdontologia.getTratamientoOdontologiaDao().consultarCEOCOP(con, hallazgosDentales);
	}

	
	public DtoTipoHallazgosPieza obtenerTiposHallazgosPiezaOdontograma(Connection con, InfoPlanTratamiento info, ArrayList<DtoOtroHallazgo> listaOtrosHallazgos) {
		//String xml = "<contenido><diente pieza = '18' > <superficie codigo = '1' nombre = 'Vestibular' sector = '1' modificable='true'> <hallazgo><codigo>5</codigo><descripcion>5 CARIES 1</descripcion><path>/ipsmedico_nofuncionales/imagenesOdontologia/convencion/convencion_2.jpg</path><convencion>2</convencion><borde>0x330000</borde></hallazgo></superficie> <superficie codigo = '1' nombre = 'Mesial' sector = '2' modificable='true'> <hallazgo><codigo>6</codigo><descripcion>6 OBTURADOO1</descripcion><path>/ipsmedico_nofuncionales/imagenesOdontologia/convencion/convencion_3.jpg</path><convencion>3</convencion><borde>0xcc0000</borde></hallazgo></superficie> <superficie codigo = '1' nombre = 'Palatino' sector = '3' modificable='true'> <hallazgo><codigo>5</codigo><descripcion>5 CARIES 1</descripcion><path>/ipsmedico_nofuncionales/imagenesOdontologia/convencion/convencion_2.jpg</path><convencion>2</convencion><borde>0x330000</borde></hallazgo></superficie> <superficie codigo = '1' nombre = 'Distal'  sector = '4' modificable='true'> <hallazgo><codigo>6</codigo><descripcion>6 OBTURADOO1</descripcion><path>/ipsmedico_nofuncionales/imagenesOdontologia/convencion/convencion_3.jpg</path><convencion>3</convencion><borde>0xcc0000</borde></hallazgo></superficie> <superficie codigo = '1' nombre = 'Oclusal' sector = '5' modificable='true'> <hallazgo><codigo>5</codigo><descripcion>5 CARIES 1</descripcion><path>/ipsmedico_nofuncionales/imagenesOdontologia/convencion/convencion_2.jpg</path><convencion>2</convencion><borde>0x330000</borde></hallazgo></superficie> </diente><diente pieza = '28' > <superficie codigo = '-1' nombre = 'diente' modificable='true'> <hallazgo><codigo>1</codigo><descripcion>1 TEMPORALIZACION DESTAPADA</descripcion><path>/ipsmedico_nofuncionales/imagenesOdontologia/convencion/convencion_1.jpg</path><convencion>1</convencion><borde>0xff3300</borde></hallazgo></superficie> </diente><diente pieza = '38' > <superficie codigo = '-1' nombre = 'diente' modificable='true'> <hallazgo><codigo>7</codigo><descripcion>7 EXTRAIDO1</descripcion><path>/ipsmedico_nofuncionales/imagenesOdontologia/convencion/convencion_4.jpg</path><convencion>4</convencion><borde>0x000066</borde></hallazgo></superficie> </diente><diente pieza = '48' > <superficie codigo = '1' nombre = 'Oclusal' sector = '5' modificable='true'> <hallazgo><codigo>5</codigo><descripcion>5 CARIES 1</descripcion><path>/ipsmedico_nofuncionales/imagenesOdontologia/convencion/convencion_2.jpg</path><convencion>2</convencion><borde>0x330000</borde></hallazgo></superficie> </diente></contenido>";
		//InfoPlanTratamiento info = UtilidadOdontogramaXML.getInfoPlanTratamientoDeXML(xmlOdontograma);
		
		DtoTipoHallazgosPieza dtoTipoHallazgoPieza = new DtoTipoHallazgosPieza();
		HashMap<Long, Integer> totalTiposHallazgo = new HashMap<Long, Integer>();
		HashMap<Integer, ArrayList<Integer>> mapaPiezaHallazgos = new HashMap<Integer, ArrayList<Integer>>();
		ArrayList<Integer> listaHallazgosPieza = new ArrayList<Integer>();
		ArrayList<TipoHallazgoCeoCop> listaTipoHallazgo = new ArrayList<TipoHallazgoCeoCop>();
		int cont = 0, codigoHallazgo = ConstantesBD.codigoNuncaValido, codigoPieza = ConstantesBD.codigoNuncaValido;
		
		for (InfoDetallePlanTramiento infoDetalle : info.getSeccionHallazgosDetalle()) {
			ArrayList<InfoHallazgoSuperficie> infoSuper = infoDetalle.getDetalleSuperficie();
			for (InfoHallazgoSuperficie infoHallazgoSuperficie : infoSuper) {
				InfoDatosInt infoDatos = infoHallazgoSuperficie.getHallazgoREQUERIDO();
				if (!listaHallazgosPieza.contains(infoDatos.getCodigo())) {
					listaHallazgosPieza.add(infoDatos.getCodigo());
				}
			}
			
			listaTipoHallazgo = this.consultarCEOCOP(con, listaHallazgosPieza);
			
			for (TipoHallazgoCeoCop tipoHallazgoCeoCop : listaTipoHallazgo) {
				if (!totalTiposHallazgo.containsKey(tipoHallazgoCeoCop.getCodigo())) {
					totalTiposHallazgo.put(tipoHallazgoCeoCop.getCodigo(), 1);
				} else {
					cont = totalTiposHallazgo.get(tipoHallazgoCeoCop.getCodigo());
					totalTiposHallazgo.put(tipoHallazgoCeoCop.getCodigo(), ++cont );
				}
			}
			
			mapaPiezaHallazgos.put(infoDetalle.getPieza().getCodigo(), listaHallazgosPieza);
			listaHallazgosPieza = new ArrayList<Integer>();
		}
		
		for (DtoOtroHallazgo otroHallazgo : listaOtrosHallazgos) {
			codigoHallazgo = Utilidades.convertirAEntero(otroHallazgo.getHallazgo().getCodigo());
			codigoPieza = otroHallazgo.getPieza().getCodigoPk();
			if(codigoPieza != ConstantesBD.codigoNuncaValido) {
				if(!mapaPiezaHallazgos.containsKey(codigoPieza)) {
					listaHallazgosPieza.add(codigoHallazgo);
					mapaPiezaHallazgos.put(codigoPieza, listaHallazgosPieza);
				} else {
					if (!mapaPiezaHallazgos.get(codigoPieza).contains(codigoHallazgo)) {
						listaHallazgosPieza.add(codigoHallazgo);
						mapaPiezaHallazgos.get(codigoPieza).add(codigoHallazgo);
					}
				}
			}
		}
		
		listaTipoHallazgo = this.consultarCEOCOP(con, listaHallazgosPieza);
		
		for (TipoHallazgoCeoCop tipoHallazgoCeoCop : listaTipoHallazgo) {
			if (!totalTiposHallazgo.containsKey(tipoHallazgoCeoCop.getCodigo())) {
				totalTiposHallazgo.put(tipoHallazgoCeoCop.getCodigo(), 1);
			} else {
				cont = totalTiposHallazgo.get(tipoHallazgoCeoCop.getCodigo());
				totalTiposHallazgo.put(tipoHallazgoCeoCop.getCodigo(), ++cont );
			}
		}
		
		Set<Long> listaTipos = totalTiposHallazgo.keySet();
		for (Long tipoHallazgoCeoCop : listaTipos) {
			int total = totalTiposHallazgo.get(tipoHallazgoCeoCop);
			Log4JManager.info("Tipo: " + tipoHallazgoCeoCop + " total: " + total);
		}
		
		dtoTipoHallazgoPieza.setListaPiezaHallazgos(mapaPiezaHallazgos);
		dtoTipoHallazgoPieza.setTotalTiposHallazgo(totalTiposHallazgo);
		
		return dtoTipoHallazgoPieza;
	}
	
	public void actualizarTiposOtrosHallazgosPiezaOdontograma(Connection con, DtoTipoHallazgosPieza dtoTipoHallazgoPieza, DtoOtroHallazgo otroHallazgo) {

		ArrayList<Integer> listaHallazgosPieza = new ArrayList<Integer>();
		ArrayList<TipoHallazgoCeoCop> listaTipoHallazgo = new ArrayList<TipoHallazgoCeoCop>();
		int cont = 0, codigoHallazgo = ConstantesBD.codigoNuncaValido, codigoPieza = ConstantesBD.codigoNuncaValido;

		codigoHallazgo = Utilidades.convertirAEntero(otroHallazgo.getHallazgo().getCodigo());
		codigoPieza = otroHallazgo.getPieza().getCodigoPk();
		if(codigoPieza != ConstantesBD.codigoNuncaValido) {
			if(!dtoTipoHallazgoPieza.getListaPiezaHallazgos().containsKey(codigoPieza)) {
				listaHallazgosPieza.add(codigoHallazgo);
				dtoTipoHallazgoPieza.getListaPiezaHallazgos().put(codigoPieza, listaHallazgosPieza);
			} else {
				if (!dtoTipoHallazgoPieza.getListaPiezaHallazgos().get(codigoPieza).contains(codigoHallazgo)) {
					dtoTipoHallazgoPieza.getListaPiezaHallazgos().get(codigoPieza).add(codigoHallazgo);
					listaHallazgosPieza.add(codigoHallazgo);
				}
			}
		}

		listaTipoHallazgo = this.consultarCEOCOP(con, listaHallazgosPieza);

		for (TipoHallazgoCeoCop tipoHallazgoCeoCop : listaTipoHallazgo) {
			if (!dtoTipoHallazgoPieza.getTotalTiposHallazgo().containsKey(tipoHallazgoCeoCop.getCodigo())) {
				dtoTipoHallazgoPieza.getTotalTiposHallazgo().put(tipoHallazgoCeoCop.getCodigo(), 1);
			} else {
				cont = dtoTipoHallazgoPieza.getTotalTiposHallazgo().get(tipoHallazgoCeoCop.getCodigo());
				dtoTipoHallazgoPieza.getTotalTiposHallazgo().put(tipoHallazgoCeoCop.getCodigo(), ++cont );
			}
		}

		Set<Long> listaTipos = dtoTipoHallazgoPieza.getTotalTiposHallazgo().keySet();
		for (Long tipoHallazgoCeoCop : listaTipos) {
			int total = dtoTipoHallazgoPieza.getTotalTiposHallazgo().get(tipoHallazgoCeoCop);
			Log4JManager.info("Tipo: " + tipoHallazgoCeoCop + " total: " + total);
		}
	}
	
}
