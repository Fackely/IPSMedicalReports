package com.princetonsa.mundo.enfermeria;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.action.enfermeria.ProgramacionCuidadosEnfermeriaAction;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.enfermeria.ProgramacionCuidadoEnferDao;
import com.princetonsa.dao.sqlbase.enfermeria.SqlBaseProgramacionCuidadoEnferDao;
import com.princetonsa.dto.enfermeria.DtoCuidadosEnfermeria;
import com.princetonsa.dto.enfermeria.DtoDetalleCuidadosEnfermeria;
import com.princetonsa.dto.enfermeria.DtoFrecuenciaCuidadoEnferia;
import com.princetonsa.mundo.IngresoGeneral;

public class ProgramacionCuidadoEnfer {
	
	static Logger logger =Logger.getLogger(ProgramacionCuidadoEnfer.class);		

	/**
	 * 
	 * */
	public static ProgramacionCuidadoEnferDao getProgramacionCuidadoEnferDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getProgramacionCuidadoEnferDao();
	}
	
	/**
	 * Consulta el listado de los tipo frecuencia por institución
	 * @param Connection con
	 * @param int institucion
	 * */	
	public static ArrayList<HashMap<String,Object>> consultarTipoFrecuenciaInst(Connection con,int institucion)
	{				
		HashMap parametros = new HashMap();
		parametros.put("institucion",institucion);
		return getProgramacionCuidadoEnferDao().consultarTipoFrecuenciaInst(con, parametros);
	}
	
	/**
	 * Consulta la informacion de las Frecuencias de los cuidados de enfermeria en el registro de enfermeria
	 * @param Connection con 
	 * @param String ingreso
	 * @param int codigoPkFrecCuidadoEnfer (opcional -1)
	 * @param Bollean activo
	 * */
	public static ArrayList<DtoFrecuenciaCuidadoEnferia> consultarFrecuenciaCuidado(
			Connection con,
			String ingreso,
			int codigoPkFrecCuidadoEnfer,
			boolean activo)
	{
		HashMap parametros = new HashMap();
		parametros.put("ingreso",ingreso);
		parametros.put("codigoPkFrecCuidadoEnfer",codigoPkFrecCuidadoEnfer);
		parametros.put("activo",activo?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		return getProgramacionCuidadoEnferDao().consultarFrecuenciaCuidado(con, parametros);
	}
	
	/**
	 * Consulta la informacion de las Frecuencias de los cuidados de enfermeria en ordenes medicas 
	 * @param Connection con 
	 * @param String ingreso
	 * */
	public static ArrayList<DtoFrecuenciaCuidadoEnferia> consultarFrecuenciaCuidado(Connection con,String ingreso)
	{
		HashMap parametros = new HashMap();
		parametros.put("ingreso",ingreso);
		return getProgramacionCuidadoEnferDao().consultarFrecuenciaCuidado(con, parametros);
	}
	
	/**
	 * Inserta registros de frecuencias de cuidados en Registro de Enfermeria
	 * @param Connection con
	 * @param int codigoIngreso
	 * @param int codigoCuidadoEnferCcInst
	 * @param int codigoOtroCuidado
	 * @param int frecuencia
	 * @param int tipoFrecuencia
	 * @param boolean activo
	 * @param int periodo
	 * @param int tipoFrecuenciaPeriodo
	 * @param String usuarioModifica
	 * */
	public static int insertarFrecuenciasCuidados(
			Connection con,
			int codigoIngreso,					
			int codigoCuidadoEnferCcInst,
			int codigoOtroCuidado,
			int frecuencia,
			int tipoFrecuencia,
			boolean activo,
			int periodo,
			int tipoFrecuenciaPeriodo,			
			String usuarioModifica
			)
	{
		DtoFrecuenciaCuidadoEnferia dto = new DtoFrecuenciaCuidadoEnferia();
		dto.setCodigoIngreso(codigoIngreso);
		dto.setCodigoCuidadoEnferCcInst(codigoCuidadoEnferCcInst);
		dto.setCodigoOtroCuidado(codigoOtroCuidado);
		dto.setFrecuencia(frecuencia);
		dto.setTipoFrecuencia(tipoFrecuencia);
		dto.setActivo(activo?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		dto.setPeriodo(periodo);
		dto.setTipoFrecuenciaPeriodo(tipoFrecuenciaPeriodo);
		return insertarFrecuenciasCuidados(con,dto, usuarioModifica);
	}
	
	/**
	 * Inserta registros de frecuencias de cuidados en Registro de Enfermeria
	 * @param Connection con
	 * @param DtoFrecuenciaCuidadoEnferia dto
	 * @param String usuarioModifica
	 * */
	public static int insertarFrecuenciasCuidados(Connection con,DtoFrecuenciaCuidadoEnferia dto, String usuarioModifica)
	{
		HashMap parametros = new HashMap();
		parametros.put("DtoFrecuenciaCuidadoEnferia",dto);
		parametros.put("usuarioModifica",usuarioModifica);
		return getProgramacionCuidadoEnferDao().insertarFrecuenciasCuidados(con, parametros);
	}
	
	/**
	 * actualiza la informacion de frecuencias de cuidados de registro de enfermeria
	 * @param Connection con
	 * @param DtoFrecuenciaCuidadoEnferia dto
	 * @param String usuarioModifica 
	 * */
	public static boolean actualizarFrecuenciasCuidadosRegEnfer(Connection con,DtoFrecuenciaCuidadoEnferia dto, String usuarioModifica)
	{
		HashMap parametros = new HashMap();
		parametros.put("DtoFrecuenciaCuidadoEnferia",dto);
		parametros.put("usuarioModifica",usuarioModifica);
		return getProgramacionCuidadoEnferDao().actualizarFrecuenciasCuidadosRegEnfer(con, parametros);
	}
		
	/**
	 * 
	 * @param con
	 * @param areaFiltro
	 * @param pisoFiltro
	 * @param habitacionFiltro
	 * @param programados
	 * @return
	 */
	public HashMap consultarListadoPacientes(Connection con, String areaFiltro, String pisoFiltro, String habitacionFiltro, String programados) 
	{
		return getProgramacionCuidadoEnferDao().consultarListadoPacientes(con, areaFiltro, pisoFiltro, habitacionFiltro, programados);
	}
	
	
	/**
	 * Devuelve la posicion dentro del array apartir del codigo del cuidado de enfermeria
	 * @param ArrayList<DtoFrecuenciaCuidadoEnferia> array
	 * @param int codigo
	 * @param boolean esOtro
	 * */
	public static int getPosArrayFrecuenciaCuidadoEnfer(ArrayList<DtoFrecuenciaCuidadoEnferia> array,int codigo,boolean esOtro)
	{
		for(int i=0; i<array.size(); i++)
		{
			if(!esOtro && array.get(i).getCodigoCuidadoEnferCcInst() == codigo)
				return i;
			else if (esOtro && array.get(i).getCodigoOtroCuidado() == codigo)
				return i;				
		}
		
		return ConstantesBD.codigoNuncaValido;
	}
	
	
	/**
	 * Actualiza el estado de activo de las frecuencias de cuidados de registro de enfermeria
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static boolean actualizarEstadoFrecuenciasCuidadosRegEnfer(Connection con,int codigoPk,boolean activo,String usuarioModifica)
	{
		HashMap parametros = new HashMap();
		parametros.put("activo",activo?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		parametros.put("usuarioModifica",usuarioModifica);
		parametros.put("codigoPk",codigoPk);
		
		return getProgramacionCuidadoEnferDao().actualizarEstadoFrecuenciasCuidadosRegEnfer(con, parametros);
	}
	
	
	/**
	 * Retorna la cadena sql de consulta de cuidados programados para un paciente en un rango de  fecha y hora especifica
	 * @param Connection con
	 * @param int ingreso
	 * @param int codigoPk (opcional -1) 
	 * @param int codigoCuidadoEnfer (opcional -1)
	 * @param String fechaHoraFiltro
	 * @param boolean cargarDetalle
	 * @param boolean esOtroCuidado
	 * @param boolean validarRangoFecha
	 * */
	public static HashMap getStringSqlConsulProgCuidEnfer(
			int ingreso,
			int codigoPk,			
			int codigoCuidadoEnfer,
			String fechaHoraFiltro,
			boolean cargarDetalle,
			boolean esOtroCuidado,
			boolean mostrarFechaAnidadas,
			boolean validarRangoFecha)
	{
		HashMap parametros = new HashMap();
		parametros.put("ingreso",ingreso);
		parametros.put("codigoPk",codigoPk);		
		parametros.put("codigoCuidadoEnfer",codigoCuidadoEnfer);
		parametros.put("fechaHoraFiltro",fechaHoraFiltro);
		parametros.put("cargarDetalle",cargarDetalle?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		parametros.put("esOtroCuidados",esOtroCuidado?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		parametros.put("mostrarFechasAnidadas",mostrarFechaAnidadas?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		parametros.put("validarRangoFecha",validarRangoFecha?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		
		return getProgramacionCuidadoEnferDao().getStringSqlConsulProgCuidEnfer(parametros);		
	}
	
	/**
	 * Retorna la cadena sql de la consulta de cuidados programados para un paciente
	 * @param ingreso
	 * @param codigoPk
	 * @param codigoCuidadoEnfer
	 * @param cargarDetalle
	 * @param mostrarFechaAnidadas
	 * @param esOtroCuidado
	 * @return
	 */
	public static HashMap getStringSqlConsulProgCuidEnferPaciente(
			int ingreso,
			int codigoPk,			
			int codigoCuidadoEnfer,
			boolean cargarDetalle,
			boolean esOtroCuidado,
			boolean mostrarFechaAnidadas)
	{
		HashMap parametros = new HashMap();
		parametros.put("ingreso",ingreso);
		parametros.put("codigoPk",codigoPk);		
		parametros.put("codigoCuidadoEnfer",codigoCuidadoEnfer);
		parametros.put("cargarDetalle",cargarDetalle?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		parametros.put("esOtroCuidados",esOtroCuidado?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		parametros.put("mostrarFechasAnidadas",mostrarFechaAnidadas?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		
		return getProgramacionCuidadoEnferDao().getStringSqlConsulProgCuidEnfer(parametros);		
	}
	
	
	/**
	 * Consulta la información de los programas de cuidados de enfermeria
	 * @param Connection con
	 * @param int ingreso
	 * @param int codigoPk (opcional -1) 
	 * @param int codigoCuidadoEnfer (opcional -1)
	 * @param String fechaHoraFiltro,
	 * @param boolean cargarDetalle
	 * @param boolean esOtroCuidado
	 * */
	public static ArrayList<DtoCuidadosEnfermeria> consultarProgCuidadosEnfer(
			Connection con,
			int ingreso,
			int codigoPk,			
			int codigoCuidadoEnfer,
			String fechaHoraFiltro,
			boolean cargarDetalle,
			boolean esOtroCuidado,
			boolean validarRangoFecha,
			boolean soloActivos)
	{
		HashMap parametros = new HashMap();
		parametros.put("ingreso",ingreso);
		parametros.put("codigoPk",codigoPk);		
		parametros.put("codigoCuidadoEnfer",codigoCuidadoEnfer);
		parametros.put("fechaHoraFiltro",fechaHoraFiltro);		
		parametros.put("cargarDetalle",cargarDetalle?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		parametros.put("esOtroCuidados",esOtroCuidado?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		parametros.put("validarRangoFecha",validarRangoFecha?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		parametros.put("activo",soloActivos?ConstantesBD.acronimoSi:"");
		
		return getProgramacionCuidadoEnferDao().consultarProgCuidadosEnfer(con, parametros);		
	}
	
	/**
	 * Realiza validaciones para determinar si la frecuencia y periodo son validos
	 * @param DtoFrecuenciaCuidadoEnferia dto
	 * */
	public static ActionErrors validacionesInfoFrePerProgramacion(DtoFrecuenciaCuidadoEnferia dto)
	{
		ActionErrors errores = new ActionErrors();
		
		if(!(dto.getFrecuencia() <= 0 && dto.getPeriodo() <= 0))
		{
			//validación de frecuencia y periodo requeridos
			if(dto.getFrecuencia() <= 0 || dto.getPeriodo() <= 0)
				errores.add("descripcion",new ActionMessage("errors.required","La programación se puede realizar con frecuencia Y periodo O sin frecuencia Y sin periodo. Determine la conbinación para la Programación. "));
		}
		
		return errores;
	}
	
	/**
	 * Realiza las validaciones sobre la fecha/hora de generacion de la programacion de 
	 * cuidados de enfermeria
	 * @para Connection con
	 * */
	public static ActionErrors validacionesFechaHoraProgr(
			Connection con,
			String fechaInicioProg,
			String horaInicioProg,
			String fechaIngresoPaciente,
			String horaIngresoPaciente,
			String fechaOrden,
			String horaOrden)
	{
		ActionErrors errores = new ActionErrors();
		
		//validacion de la fecha y hora de ingreso a la sala 
		if(fechaInicioProg.equals("") || horaInicioProg.equals(""))		
			errores.add("descripcion",new ActionMessage("errors.required","La Fecha/Hora Inicio Programación " ));		
		
		if(errores.isEmpty())
		{
			//Validacion del formato de la fecha
			if(!UtilidadFecha.validarFecha(fechaInicioProg))
			{
				errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido",fechaInicioProg));				
			}
			else
			{
				//validacion de la hora
				if(!UtilidadFecha.validacionHora(horaInicioProg).puedoSeguir)				
					errores.add("descripcion",new ActionMessage("errors.formatoHoraInvalido",horaInicioProg));				
				else
				{
					//validacion fecha de ingreso
					if(!UtilidadFecha.compararFechas(fechaInicioProg,horaInicioProg,fechaIngresoPaciente,horaIngresoPaciente).isTrue())					
						errores.add("descripcion",new ActionMessage("errors.fechaHoraAnteriorIgualActual","Fecha/Hora Inicio "+fechaInicioProg+" -  "+horaInicioProg,"Fecha/Hora Ingreso Paciente "+fechaIngresoPaciente+" - "+horaIngresoPaciente));					
					
					//validacion fecha Actual
					if(!UtilidadFecha.compararFechas(fechaInicioProg,horaInicioProg,UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual()).isTrue())
						errores.add("descripcion",new ActionMessage("errors.fechaHoraAnteriorIgualActual","Fecha/Hora Inicio "+fechaInicioProg+" -  "+horaInicioProg,"Fecha/Hora Actual "+UtilidadFecha.getFechaActual()+" - "+UtilidadFecha.getHoraActual()));
					
					//validacion fecha Orden
					if(!UtilidadFecha.compararFechas(fechaInicioProg,horaInicioProg,fechaOrden,horaOrden).isTrue())
						errores.add("descripcion",new ActionMessage("errors.fechaHoraAnteriorIgualActual","Fecha/Hora Inicio "+fechaInicioProg+" -  "+horaInicioProg,"Fecha/Hora de la Orden "+fechaOrden+" - "+horaOrden));					
				}
			}
		}			

		return errores;
	}
	
	
	/**
	 * Inserta el detalle de programacion de cuidados de enfermeria
	 * @param Connection con
	 * @param int codigoPkProCuidadosEnfer
	 * @param String fecha
	 * @param String hora
	 * @param boolean activo
	 * */
	public static int insertarDetalleProgCuidadosEnfer(
														Connection con,
														int codigoPkProCuidadosEnfer,
														String fecha,
														String hora,
														boolean activo)
	{
		HashMap parametros = new HashMap();
		parametros.put("codigoPkProCuidadosEnfer",codigoPkProCuidadosEnfer);
		parametros.put("fecha",fecha);
		parametros.put("hora",hora);
		parametros.put("activo",activo?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
		
		return getProgramacionCuidadoEnferDao().insertarDetalleProgCuidadosEnfer(con, parametros);
	}
	
	/**
	 * Inserta el detalle de programacion de cuidados de enfermeria
	 * @param Connection con
	 * @param ArrayList<DtoDetalleCuidadosEnfermeria> array
	 * */
	public static boolean insertarDetalleProgCuidadosEnfer(
														Connection con,
														ArrayList<DtoDetalleCuidadosEnfermeria> array)
	{
		HashMap parametros = new HashMap();
		
		for(int i = 0; i < array.size(); i++)
		{
			parametros.put("codigoPkProCuidadosEnfer",array.get(i).getCodigoPkProgramacion());
			parametros.put("fecha",UtilidadFecha.conversionFormatoFechaABD(array.get(i).getFecha()));
			parametros.put("hora",array.get(i).getHora());
			parametros.put("activo",array.get(i).getActivo());
			
			if(getProgramacionCuidadoEnferDao().insertarDetalleProgCuidadosEnfer(con, parametros) < 0)
				return false;
		}
		
		return true;
	}
	
	/**
	 * Inserta el encabezado de una programacion de cuidados de enfermeria
	 * @param Connection con
	 * @param int codigoPkFrecCuidadoEnfer
	 * @param String fechaInicio
	 * @param String horaInicio
	 * @param String observacion
	 * @param String usuarioProgramacion
	 * */
	public static int insertarProgCuidadosEnfer(
											Connection con,
											int codigoPkFrecCuidadoEnfer,
											String fechaInicio,
											String horaInicio,
											String observacion,
											String usuarioProgramacion)
	{
		
		
		HashMap parametros = new HashMap();
		parametros.put("codigoPkFrecCuidadoEnfer",codigoPkFrecCuidadoEnfer);
		parametros.put("fechaInicio",UtilidadFecha.conversionFormatoFechaABD(fechaInicio));
		parametros.put("horaInicio",horaInicio);
		parametros.put("observacion",observacion);
		parametros.put("usuarioProgramacion",usuarioProgramacion);	
		
		logger.info("Entro a insertar Programacion Cuidados Enfermeria");
		
		return getProgramacionCuidadoEnferDao().insertarProgCuidadosEnfer(con, parametros);
	}
	
	/**
	 * Verifica que los datos de frecuencia y periodo sean correctos
	 * @param ArrayList<DtoFrecuenciaCuidadoEnferia> array
	 * */
	public static ActionErrors validacionesDatosFrecuenciaPeriodo(ArrayList<DtoFrecuenciaCuidadoEnferia> array)
	{
		ActionErrors errores = new ActionErrors();
		
		for(int i=0; i<array.size();i++)
		{			
			//Frecuencia y tipo de frecuencia
			if(array.get(i).getFrecuencia()<= 0 && 
					array.get(i).getTipoFrecuencia() > 0)
				errores.add("descripcion", new ActionMessage("errors.notEspecific","La frecuencia del Cuidado Especial ["+array.get(i).getNombreCuidadoEnfer()+"]  debe ser un Valor numerico Mayor a Cero"));
			else if(array.get(i).getFrecuencia()> 0 && 
					array.get(i).getTipoFrecuencia() <= 0)
				errores.add("descripcion", new ActionMessage("errors.notEspecific","El Tipo de frecuencia del Cuidado Especial ["+array.get(i).getNombreCuidadoEnfer()+"] es Requerido"));
			
			//Periodo y tipo de periodo
			if(array.get(i).getPeriodo()<= 0 && 
					array.get(i).getTipoFrecuenciaPeriodo() > 0)
				errores.add("descripcion", new ActionMessage("errors.notEspecific","El Periodo del Cuidado Especial ["+array.get(i).getNombreCuidadoEnfer()+"]  debe ser un Valor numerico Mayor a Cero"));
			else if(array.get(i).getPeriodo()> 0 && 
					array.get(i).getTipoFrecuenciaPeriodo() <= 0)
				errores.add("descripcion", new ActionMessage("errors.notEspecific","El Tipo de Periodo del Cuidado Especial ["+array.get(i).getNombreCuidadoEnfer()+"] es Requerido"));
		
			if(array.get(i).getPeriodo()> 0 && 
					array.get(i).getTipoFrecuenciaPeriodo() > 0 && 
						array.get(i).getFrecuencia()<= 0 && array.get(i).getTipoFrecuencia() <= 0)
				errores.add("descripcion", new ActionMessage("errors.notEspecific","La Informacion de Frecuencia del Cuidado Especial ["+array.get(i).getNombreCuidadoEnfer()+"] es Requerido para Ingresar la Información del Periodo"));
			
			if(array.get(i).getPeriodo() > 0 &&
					array.get(i).getTipoFrecuenciaPeriodo() >= 0 && 
						array.get(i).getFrecuencia() > 0 && 
							array.get(i).getTipoFrecuencia() >= 0)
			{
				int frecuencia = 0,periodo = 0 ;
				
				frecuencia = array.get(i).getFrecuencia();
				periodo = array.get(i).getPeriodo();
				
				//Pasamos la frecuencia y el periodo a minutos
				if(array.get(i).getTipoFrecuencia() == ConstantesBD.codigoTipoFrecuenciaHoras)
					frecuencia = frecuencia * 60; 
				else if (array.get(i).getTipoFrecuencia() == ConstantesBD.codigoTipoFrecuenciaDias)
					frecuencia = frecuencia * 1440;
				
				if(array.get(i).getTipoFrecuenciaPeriodo() == ConstantesBD.codigoTipoFrecuenciaHoras)
					periodo = periodo * 60;
				else if(array.get(i).getTipoFrecuenciaPeriodo() == ConstantesBD.codigoTipoFrecuenciaDias)
					periodo = periodo * 1440;
				
				if(periodo < frecuencia)
					errores.add("descripcion", new ActionMessage("errors.notEspecific","La Relación Frecuencia/Tipo Frecuencia debe ser menor a la Relación Periodo/Tipo Periodo en el Cuidado Especial ["+array.get(i).getNombreCuidadoEnfer()+"]."));							
			}														
		}
		
		return errores;
	}
	
	/**
	 * Genera el detalle de la programacion
	 * @param String fechaInicio
	 * @param String horaInicio
	 * @param int frecuencia
	 * @param String tipoFrecuencia
	 * @param int periodo
	 * @param String tipoPeriodo
	 * @param int dosisPendientes
	 * */
	public static ArrayList<DtoDetalleCuidadosEnfermeria> programarAdministracion (
																					String fechaInicio,
																					String horaInicio,
																					int frecuencia,
																					int tipoFrecuencia,
																					int periodo,
																					int tipoPeriodo,
																					int codigoPkProgramacion)
	{
		logger.info("\n entre a programarCuidado fechaInicio >> "+fechaInicio+" horaInicio >> "+horaInicio+" frecuencia >> "+frecuencia+"  tipoFrecuencia >> "+tipoFrecuencia+" periodo >> "+periodo+" tipoPeriodo >> "+tipoPeriodo+" horaInicio >> "+horaInicio);		
		ArrayList<DtoDetalleCuidadosEnfermeria> array = new ArrayList<DtoDetalleCuidadosEnfermeria>();
		
		//Pasamos la frecuencia y el periodo a minutos
		if(tipoFrecuencia == ConstantesBD.codigoTipoFrecuenciaHoras)
			frecuencia = frecuencia * 60; 
		else if (tipoFrecuencia == ConstantesBD.codigoTipoFrecuenciaDias)
			frecuencia = frecuencia * 1440;
		
		if(tipoPeriodo == ConstantesBD.codigoTipoFrecuenciaHoras)
			periodo = periodo * 60;
		else if(tipoPeriodo == ConstantesBD.codigoTipoFrecuenciaDias)
			periodo = periodo * 1440;		
		
		//Operación con Frecuencia y Periodo
		if(periodo > 0 && frecuencia > 0)
		{
			for(int i=0; i<periodo; i=i+frecuencia)
			{
				DtoDetalleCuidadosEnfermeria dto = new DtoDetalleCuidadosEnfermeria();
				dto.setCodigoPkProgramacion(codigoPkProgramacion);
				dto.setFecha(fechaInicio);
				dto.setHora(horaInicio);
				dto.setActivo(ConstantesBD.acronimoSi);
				array.add(dto);
				
				String []fechaHora=UtilidadFecha.incrementarMinutosAFechaHora(fechaInicio, horaInicio,frecuencia,false);				
				fechaInicio=fechaHora[0];
				horaInicio=fechaHora[1];
			}
		}		
		else if(frecuencia <= 0 && periodo <= 0)
		{
			DtoDetalleCuidadosEnfermeria dto = new DtoDetalleCuidadosEnfermeria();
			dto.setCodigoPkProgramacion(codigoPkProgramacion);
			dto.setFecha(fechaInicio);
			dto.setHora(horaInicio);
			dto.setActivo(ConstantesBD.acronimoSi);
			array.add(dto);
		}
		
		return array;
	}
}