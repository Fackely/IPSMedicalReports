package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.UtilidadValidacion;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.historiaClinica.UtilidadesHistoriaClinica;

import com.princetonsa.dao.UtilidadesBDDao;
import com.princetonsa.dto.historiaClinica.DtoBalanceLiquidos;
import com.princetonsa.dto.historiaClinica.DtoEvolucion;
import com.princetonsa.dto.historiaClinica.DtoEvolucionComentarios;
import com.princetonsa.mundo.atencion.Diagnostico;
import com.princetonsa.mundo.atencion.SignoVital;
import com.servinte.axioma.dto.manejoPaciente.InformacionCentroCostoValoracionDto;

public class SqlBaseEvolucionesDao 
{
	/**
	 * 
	 */
	private static Logger logger= Logger.getLogger(SqlBaseEvolucionesDao.class);
	
	
	/**
	 * Consulta la evolucion base y la relacion con el egreso.
	 */
	private static final String cargarEvolucionStr = "SELECT "+
								"e.codigo as codigo_evolucion, "+ //
								"e.valoracion as valoracion_asociada, "+ //
								"e.diagnostico_complicacion as  diagnostico_complicacion," + //
								"e.diagnostico_complicacion_cie as diagnostico_complicacion_cie, "+ //
								"getnombrediagnostico(e.diagnostico_complicacion,e.diagnostico_complicacion_cie) as nombre_diag_complicacion, "+ //
								"to_char(e.fecha_grabacion,'"+ConstantesBD.formatoFechaAp+"') as fecha_grabacion, "+ //
								"e.hora_grabacion as hora_grabacion, "+  //
								"to_char(e.fecha_evolucion,'"+ConstantesBD.formatoFechaAp+"') as fecha_evolucion, "+ //
								"e.hora_evolucion as hora_evolucion, "+ //
								"e.informacion_dada_paciente as informacion_dada_paciente, "+ //
								"e.desc_complicacion as desc_complicacion, "+ //
								"e.tratamiento as tratamiento, "+ // 
								"e.resultados_tratamiento as resultados_tratamiento, "+ //
								"e.cambios_manejo as cambios_manejo, "+ //
								"e.hallazgos_importantes as hallazgos_importantes, "+ //
								"coalesce(e.proced_quirurgicos_obst,"+ConstantesBD.codigoNuncaValido+") as proced_quirurgicos_obst, "+ // Tipos monitoreo.
								"e.resultado_examenes_diag as resultado_examenes_diag, "+ //
								"e.pronostico as pronostico, "+ //
								"e.observaciones as observaciones, "+ //
								"e.codigo_medico as codigo_medico, "+ //
								"e.orden_salida as orden_salida, "+ //
								"e.tipo_evolucion as tipo_evolucion, "+ //
								"ev.nombre as desc_tipo_evol, "+ //
								"e.recargo as recargo, "+ //
								"e.cobrable as cobrable, "+ //
								"e.tipo_diagnostico_principal as tipo_diagnostico_principal, "+ //
								"td.nombre as nombre_tipo_diagnostico, "+ //
								"e.datos_medico as datos_medico, "+ //
								"e.centro_costo as codigo_centro_costo, "+ // 
								"getnomcentrocosto(e.centro_costo) as nombre_centro_costo, "+ //
								"coalesce(e.conducta_seguir,"+ConstantesBD.codigoNuncaValido+") as conducta_seguir, "+ //
								"coalesce(cs.descripcion,'') as desc_conducta_seguir, " + //
								"coalesce(e.tipo_referencia,'') as tipo_referencia, "+ //
								"eg.estado_salida as estado_salida, "+ //
								"eg.destino_salida as destino_salida, "+ //
								"ds.nombre as desc_destino_salida, "+ //
								"eg.otro_destino_salida as otro_destino_salida, "+ //
								"eg.fecha_egreso as fecha_egreso, "+ //
								"eg.hora_egreso as hora_egreso, "+ //
								"eg.fecha_grabacion as fecha_grabacion_egreso, "+ //
								"eg.hora_grabacion as hora_grabacion_egreso," + //
								"coalesce(e.dias_incapacidad||'','')  as dias_incapacidad," + //
								"coalesce(e.observaciones_incapacidad,'') as observaciones_incapacidad," + //
								"coalesce(eg.cuenta,"+ConstantesBD.codigoNuncaValido+") as cuenta, "+ //
								"tm.nombre as nombreTipoMonitoreo " + //Nombre del tipo de Monitoreo
								"FROM evoluciones e " +
								"INNER JOIN especialidades_val ev ON(ev.codigo=e.tipo_evolucion) "+
								"LEFT OUTER JOIN conductas_seguir_evolucion cs ON(cs.codigo=e.conducta_seguir) "+
								"LEFT OUTER JOIN egresos eg ON(eg.evolucion=e.codigo) "+
								"LEFT OUTER JOIN destinos_salida ds ON(ds.codigo=eg.destino_salida) "+
								"INNER JOIN tipos_diagnostico td ON(td.codigo=e.tipo_diagnostico_principal) "+
								"LEFT OUTER JOIN tipo_monitoreo tm on (tm.codigo = e.proced_quirurgicos_obst) " +
								"WHERE e.codigo= ? ";
	
	
	
	/**
	 * Consulta los diagnosticos de la evolucion,
	 */
	private static final String cargarDiagnosticosStr= "SELECT "+
								"acronimo_diagnostico AS acronimo, "+
								"tipo_cie_diagnostico AS tipo_cie, "+
								"getnombrediagnostico(acronimo_diagnostico,tipo_cie_diagnostico) as nombre_diagnostico, "+
								"numero AS numero, "+
								"principal AS principal, "+
								"definitivo AS definitivo "+
								"FROM evol_diagnosticos "+
								"WHERE evolucion= ? ";
	
	
	
	/**
	 * Consulta los signos vitales insertados en una evolucion.
	 */
	private static final String cargarSignosVitalesStr= "SELECT "+
								"e.signo_vital as codigo, "+
								"coalesce(e.descripcion,'') as descripcion, "+
								"coalesce(e.valor,'') as valor, "+
								"coalesce(sv.nombre,'') as nombre, "+
								"coalesce(sv.unidad_medida,'') as unidad_medida "+
								"FROM signos_vitales sv "+
								"LEFT OUTER JOIN evol_signos_vitales e ON(e.signo_vital=sv.codigo and e.evolucion=?) "+
								"order by sv.orden ";
	
	
	
	/**
	 * Consulta el balance de liquidos de un evolucion.
	 */
	private static final String cargarBalanceLiquidosStr="SELECT "+
								"b.valor as valor, "+
								"bc.codigo as codigo, "+
								"tb.nombre as nombre "+
								"FROM balance_liquidos_evol b "+
								"INNER JOIN balance_liq_evol_cc_ins bc ON(bc.codigo=b.balance_liq_evol_cc_ins) "+
								"INNER JOIN tipo_balance_liq_evol tb ON(tb.codigo=bc.tipo_balance_liq_evol) "+
								"WHERE  evolucion=? ";
	
	
	
	/**
	 * 
	 */
	private static final String cargarComentariosEvoluciones=" SELECT codigo, " +
															 "valor, " +
															 "fecha_modifica as fecha, " +
															 "hora_modifica as hora, " +
															 "usuario_modifica as usuario " +
															 "FROM comentarios_evolucion " +
															 "WHERE evolucion=? ";
	
	
	/**
	 * 
	 */
	private static final String cargarCausaExternaUltimaValoracionHospitalizacionStr="SELECT valgen.causa_externa as codigoCausaExterna from valoraciones valgen, val_hospitalizacion valh, solicitudes sol, causas_externas cext where valgen.numero_solicitud=valh.numero_solicitud and sol.numero_solicitud=valgen.numero_solicitud and valgen.causa_externa=cext.codigo and sol.cuenta=? order by valgen.numero_solicitud desc";
	
	/**
	 * 
	 */
	private static final String  cargarCausaExternaUltimaValoracionUrgenciasStr="SELECT valgen.causa_externa as codigoCausaExterna from valoraciones valgen, valoraciones_urgencias valur, solicitudes sol, causas_externas cext where valgen.numero_solicitud=valur.numero_solicitud and sol.numero_solicitud=valgen.numero_solicitud and valgen.causa_externa=cext.codigo and sol.cuenta=?";
	
	/**
	 * 
	 */
	

	/**
	 * Inserta la evolucion base.
	 */
	private static final String insertarEvolucionBaseStr="INSERT INTO evoluciones (" +
		"codigo,"+ 
		"valoracion,"+
		"diagnostico_complicacion,"+
		"diagnostico_complicacion_cie,"+
		"fecha_grabacion,"+
		"hora_grabacion,"+
		"fecha_evolucion,"+
		"hora_evolucion,"+
		"informacion_dada_paciente,"+ // Datos Subjetivos.
		"desc_complicacion,"+ // Analisis.
		"hallazgos_importantes,"+ 
		"proced_quirurgicos_obst,"+ //Tipos Monitoreo
		"pronostico,"+ // Plan Manejo.
		"codigo_medico,"+
		"orden_salida,"+
		"tipo_evolucion,"+
		"recargo,"+
		"cobrable,"+
		"tipo_diagnostico_principal,"+
		"datos_medico,"+
		"centro_costo,"+
		"conducta_seguir,"+
		"tipo_referencia," +
		"dias_incapacidad," +
		"observaciones_incapacidad," +
		"especialidad_solicitada"+
		") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	
	
	/**
	 * 
	 */
	private static final String insertarEvolucionHospitalariaStr="INSERT INTO evolu_hospitalarias (" +
			"evolucion," +
			"info_dada_pac_epicrisis," +
			"signos_vitales_epicrisis," +
			"hallazgos_epicrisis," +
			"complicaciones_epicrisis," +
			"diag_presuntivos_epicrisis," +
			"diag_definitivos_epicrisis," +
			"tratamiento_epicrisis," +
			"resultados_trat_epicrisis," +
			"cambios_manejo_epicrisis," +
			"proced_quirurgicos_obst_ep," +
			"resultado_examenes_diag_ep," +
			"pronostico_epicrisis," +
			"observaciones_epicrisis," +
			"va_a_epicrisis" +
			") VALUES (?,?,"+ValoresPorDefecto.getValorFalseParaConsultas()+",?,?,"+ ValoresPorDefecto.getValorTrueParaConsultas() +",?,"+ ValoresPorDefecto.getValorFalseParaConsultas() +","+ ValoresPorDefecto.getValorFalseParaConsultas() +","+ ValoresPorDefecto.getValorFalseParaConsultas() +",?,"+ ValoresPorDefecto.getValorFalseParaConsultas() +",?,"+ ValoresPorDefecto.getValorFalseParaConsultas() +","+ ValoresPorDefecto.getValorTrueParaConsultas() +")" ;
	
	
	
	/**
	 * 
	 */
	private static final String modificarEvolucionHospitalariaStr="UPDATE evolu_hospitalarias SET " +
			"info_dada_pac_epicrisis=?," +
			"signos_vitales_epicrisis=?," +
			"hallazgos_epicrisis=?," +
			"complicaciones_epicrisis=?," +
			"diag_definitivos_epicrisis=?," +
			"resultado_examenes_diag_ep=?," +
			"pronostico_epicrisis=? " +
			"WHERE evolucion=?";
	
	
	
	/**
	 * 
	 */
	private static final String cargarEvolucionesHospitalariasStr="SELECT " +
			"info_dada_pac_epicrisis AS datos_subjetivos, " +
			"signos_vitales_epicrisis AS datos_objetivos, " + //Datos Objetivos
			"hallazgos_epicrisis AS hallazgos_importantes, " +
			"complicaciones_epicrisis AS analisis, " +
			"diag_presuntivos_epicrisis AS diagnosticos_presuntivos, " + //Ya no existen.
			"diag_definitivos_epicrisis as diagnosticos_definitivos," +
			"tratamiento_epicrisis as tratamiento, " + //Ya no existen.
			"resultados_trat_epicrisis AS resultados_trat, " + //Ya no existen.
			"cambios_manejo_epicrisis as cambios_manejo, " + //Ya no existen.	
			"proced_quirurgicos_obst_ep AS proced_quirurgicos, " + //Ya no existen.
			"resultado_examenes_diag_ep AS balance_liquidos, " +
			"pronostico_epicrisis AS plan_manejo, " +
			"observaciones_epicrisis AS observaciones, " + //Ya no existen.
			"va_a_epicrisis AS epicrisis " +
			"FROM evolu_hospitalarias " +
			"WHERE evolucion=? ";
	
	
	
	/**
	 * 
	 */
	private static final String insertarComentariosEvolucionesStr = "INSERT INTO comentarios_evolucion (" +
			"codigo," +
			"evolucion," +
			"valor," +
			"fecha_modifica," +
			"hora_modifica," +
			"usuario_modifica" +
			") VALUES (?,?,?,?,?,?)  ";
	
	
	
	/**
	 * Inserta los diagnosticos de la evolucion.
	 */
	private static final String insertarDiagnosticosEvolucionStr="INSERT INTO evol_diagnosticos (" +
		"evolucion,"+
		"acronimo_diagnostico,"+
		"tipo_cie_diagnostico,"+
		"numero,"+
		"principal,"+
		"definitivo"+
		") VALUES (?,?,?,?,?,?)";
	
	
	/**
	 * Inserta los signos vitales relacionados a la evolucion.
	 */
	private static final String insertarSignosVitalesEvolucionStr="INSERT INTO evol_signos_vitales (" +
		"evolucion,"+
		"signo_vital,"+
		"descripcion,"+
		"valor"+
		") VALUES (?,?,?,?)";
	
	
	/**
	 * Inserta el balance de liquidos de la evolucion.
	 */
	private static final String insertarBalanceLiquidosEvolucionStr="INSERT INTO balance_liquidos_evol (" +
		"evolucion,"+
		"balance_liq_evol_cc_ins,"+
		"valor"+
		") VALUES (?,?,?)";
	
	
	
	/**
	 * Insertar el Ingreso de Cuidados Especiales.
	 */
	private static final String insertarIngresosCuidadosEspecialesStr="INSERT INTO ingresos_cuidados_especiales (" +
		"codigo,"+
		"ingreso,"+
		"estado,"+
		"indicativo,"+
		"tipo_monitoreo,"+
		"usuario_resp,"+
		"fecha_resp,"+
		"hora_resp,"+
		"usuario_modifica,"+
		"fecha_modifica,"+
		"hora_modifica,"+
		"evolucion_orden"+
		") VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
	
	
	/**
	 * 
	 */
	private static final String actualizarIngresoCuidadosEspecialesStr="UPDATE ingresos_cuidados_especiales SET tipo_monitoreo=?, centro_costo=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=? WHERE ingreso=? ";
	
	/**
	 * 
	 */
	private static final String actualizarAreaCuentaStr="UPDATE cuentas SET area=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=? WHERE id=? "; 
	
	
	/**
	 * 
	 * @param con
	 * @param evolucion
	 * @return
	 */
	public static DtoEvolucion cargarEvolucion(Connection con, String evolucion) 
	{
		
		DtoEvolucion evolucionBase= new DtoEvolucion();
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		
		
		
		try
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(cargarEvolucionStr));
			
			logger.info(">>>>CADENA CARGAR DTO>>>>"+cargarEvolucionStr);
			logger.info(">>>>EVOLUCION A CONSULTAR>>>>"+evolucion);
			
			ps.setInt(1,Utilidades.convertirAEntero(evolucion));
			rs = new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				evolucionBase.setCodigoEvolucion(rs.getString("codigo_evolucion"));
				evolucionBase.setValoracionAsociada(rs.getString("valoracion_asociada"));
				evolucionBase.setDatosMedico(rs.getString("datos_medico"));
				evolucionBase.setCambiosManejo(rs.getString("cambios_manejo"));
				evolucionBase.setDescComplicacion(rs.getString("desc_complicacion"));
				evolucionBase.setFechaEvolucion(rs.getString("fecha_evolucion"));
				evolucionBase.setFechaGrabacion(rs.getString("fecha_grabacion"));
				evolucionBase.setHallazgosImportantes(rs.getString("hallazgos_importantes"));
				evolucionBase.setHoraEvolucion(rs.getString("hora_evolucion"));
				evolucionBase.setHoraGrabacion(rs.getString("hora_grabacion"));
				evolucionBase.setInformacionDadaPaciente(rs.getString("informacion_dada_paciente"));
				evolucionBase.setObservaciones(rs.getString("observaciones"));
				evolucionBase.setProcedQuirurgicosObst(rs.getString("proced_quirurgicos_obst"));
				//MT5887
				evolucionBase.setCentroCostoMonitoreo(rs.getInt("codigo_centro_costo"));
				//Fin Mt
				evolucionBase.setPronostico(rs.getString("pronostico"));
				evolucionBase.setRecargo(rs.getString("recargo"));
				evolucionBase.setResultadoExamenesDiag(rs.getString("resultado_examenes_diag"));
				evolucionBase.setTipoReferencia(rs.getString("tipo_referencia"));
				evolucionBase.setTratamiento(rs.getString("tratamiento"));
				evolucionBase.setResultadosTratamiento(rs.getString("resultados_tratamiento"));
				evolucionBase.setCodigoCentroCosto(rs.getInt("codigo_centro_costo"));
				evolucionBase.setNombreCentroCosto(rs.getString("nombre_centro_costo"));
				evolucionBase.setCodigoTipoEvolucion(rs.getInt("tipo_evolucion"));
				evolucionBase.setNombreTipoEvolucion(rs.getString("desc_tipo_evol"));
				evolucionBase.setOrdenSalida(UtilidadTexto.getBoolean(rs.getString("orden_salida")));
				evolucionBase.setCobrable(UtilidadTexto.getBoolean(rs.getString("cobrable")));
				evolucionBase.setCodigoConductaSeguir(rs.getInt("conducta_seguir"));
				evolucionBase.setNombreConductaSeguir(rs.getString("desc_conducta_seguir"));
				evolucionBase.setCodigoTipoDiagnosticoPrincipal(rs.getInt("tipo_diagnostico_principal"));
				evolucionBase.setNombreTipoDiagnosticoPrincipal(rs.getString("nombre_tipo_diagnostico"));
				evolucionBase.getDiagnosticoComplicacion1().setAcronimo(rs.getString("diagnostico_complicacion"));
				evolucionBase.getDiagnosticoComplicacion1().setTipoCIE(rs.getInt("diagnostico_complicacion_cie"));
				evolucionBase.setNombreDiagnosticoComplicacion(rs.getString("nombre_diag_complicacion"));
				evolucionBase.getDiagnosticoComplicacion1().setNombre(evolucionBase.getNombreDiagnosticoComplicacion());
				
				evolucionBase.setCodigoDestinoSalida(rs.getInt("destino_salida"));
				evolucionBase.setNombreDestinoSalida(rs.getString("desc_destino_salida"));
				evolucionBase.setEstadoSalida(UtilidadTexto.getBoolean(rs.getString("estado_salida")));
				evolucionBase.setOtroDestinoSalida(rs.getString("otro_destino_salida"));
				evolucionBase.setFechaEgreso(rs.getString("fecha_egreso"));
				evolucionBase.setHoraEgreso(rs.getString("hora_egreso"));
				evolucionBase.setFechaGrabacionEgreso(rs.getString("fecha_grabacion_egreso"));
				evolucionBase.setHoraGrabacionEgreso(rs.getString("hora_grabacion_egreso"));
				evolucionBase.getProfesional().cargarUsuarioBasico(con, rs.getInt("codigo_medico"));
				
				evolucionBase.setDiasIncapacidad(rs.getString("dias_incapacidad"));
				evolucionBase.setObservacionesIncapacidad(rs.getString("observaciones_incapacidad"));
				evolucionBase.setCodigoCuenta(rs.getInt("cuenta"));
				
				/**
				 * MT 5568
				 * @author javrammo
				 */
				InformacionCentroCostoValoracionDto infoCentroCosto = new InformacionCentroCostoValoracionDto();
				infoCentroCosto.setIdCentroCosto(evolucionBase.getCodigoCentroCosto());
				infoCentroCosto.setDescripcionCentroCosto(evolucionBase.getNombreCentroCosto());
				if(evolucionBase.getProcedQuirurgicosObst()!= null && !evolucionBase.getProcedQuirurgicosObst().equals(String.valueOf(ConstantesBD.codigoNuncaValido))){
					infoCentroCosto.setIdTipoMonitoreo(Integer.parseInt(evolucionBase.getProcedQuirurgicosObst()));
					infoCentroCosto.setDescripcionTipoMonitoreo(rs.getString("nombreTipoMonitoreo"));
				}
				evolucionBase.setInfoCentroCostoValoracion(infoCentroCosto);
				/**
				 * Fin MT 5568
				 */
				
			}
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(cargarDiagnosticosStr));
			ps.setInt(1,Utilidades.convertirAEntero(evolucion));
			rs = new ResultSetDecorator(ps.executeQuery());
			
			
			logger.info("<<<<<<<<<<<<<<<<<<<<< Cargar Diagnosticos ");
			logger.info("" + cargarDiagnosticosStr + " - evolucion: " + evolucion);
			
			boolean esPrimero=true;
			while(rs.next())
			{
				Diagnostico diagnostico = new Diagnostico();
				diagnostico.setAcronimo(rs.getString("acronimo"));
				diagnostico.setTipoCIE(rs.getInt("tipo_cie"));
				diagnostico.setNombre(rs.getString("nombre_diagnostico"));
				diagnostico.setPrincipal(rs.getBoolean("principal"));
				diagnostico.setDefinitivo(rs.getBoolean("definitivo"));
				diagnostico.setNumero(rs.getInt("numero"));
				
				if((esPrimero) && (rs.getString("principal").equals(ValoresPorDefecto.getValorTrueCortoParaConsultas())))  
				{	
					evolucionBase.setDiagnosticoPrincipal(diagnostico);
					esPrimero=false;
				}
				evolucionBase.getDiagnosticos().add(diagnostico);
			}
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(cargarSignosVitalesStr));
			ps.setInt(1,Utilidades.convertirAEntero(evolucion));
			rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				SignoVital signoVital = new SignoVital();
				signoVital.setCodigo(rs.getInt("codigo"));
				signoVital.setDescripcion(rs.getString("descripcion"));
				signoVital.setValorSignoVital(rs.getString("valor"));
				signoVital.setNombre(rs.getString("nombre"));
				signoVital.setUnidadMedida(rs.getString("unidad_medida"));
				
				evolucionBase.getSignosVitales().add(signoVital);
			}
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(cargarBalanceLiquidosStr));
			ps.setInt(1, Utilidades.convertirAEntero(evolucion));
			rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoBalanceLiquidos balanceLiquidos= new DtoBalanceLiquidos();
				
				balanceLiquidos.setCodigoBalance(rs.getString("codigo"));
				balanceLiquidos.setNombreBalance(rs.getString("nombre"));
				balanceLiquidos.setValor(rs.getString("valor"));
				
				evolucionBase.getBalanceLiquidos().add(balanceLiquidos);
			}
			
			ps =  new PreparedStatementDecorator(con.prepareStatement(cargarEvolucionesHospitalariasStr));
			ps.setInt(1, Utilidades.convertirAEntero(evolucion));
			rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				evolucionBase.setAnalisis(UtilidadTexto.getBoolean(rs.getString("analisis")));
				evolucionBase.setBalanceLiquidosEpicrisis(UtilidadTexto.getBoolean(rs.getString("balance_liquidos")));
				evolucionBase.setDatosObjetivos(UtilidadTexto.getBoolean(rs.getString("datos_objetivos")));
				evolucionBase.setDatosSubjetivos(UtilidadTexto.getBoolean(rs.getString("datos_subjetivos")));
				evolucionBase.setHallazgosEpicrisis(UtilidadTexto.getBoolean(rs.getString("hallazgos_importantes")));
				evolucionBase.setDiagnosticosDefinitivos(UtilidadTexto.getBoolean(rs.getString("diagnosticos_definitivos")));
				evolucionBase.setPlanManejo(UtilidadTexto.getBoolean(rs.getString("plan_manejo")));
				
			}
			ps =  new PreparedStatementDecorator(con.prepareStatement(cargarComentariosEvoluciones));
			ps.setInt(1, Utilidades.convertirAEntero(evolucion));
			rs = new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoEvolucionComentarios comentariosEvolucion = new DtoEvolucionComentarios();
				
				comentariosEvolucion.setConsecutivo(rs.getString("codigo"));
				comentariosEvolucion.setValor(rs.getString("valor"));
				comentariosEvolucion.setFecha(rs.getString("fecha"));
				comentariosEvolucion.setHora(rs.getString("hora"));
				comentariosEvolucion.getProfesional().cargarUsuarioBasico(con, rs.getString("usuario"));
				
				evolucionBase.getComentarios().add(comentariosEvolucion);
				
			}
			//Se verifica si el paciente está muerto
			if(evolucionBase.isOrdenSalida())
			{
				int codigoPaciente = UtilidadesHistoriaClinica.obtenerCodigoPacienteSolicitud(con, evolucionBase.getValoracionAsociada());
				
				/*
				 * Cambio por MT 6357, no puede mirar directamente en la tabla pacientes, ya que si existen evoluciones anteriores
				 * con orden de salida no va a hacer consecuente con la información historica del mismo.
				 */
				//evolucionBase.setMuerto(UtilidadValidacion.esPacienteMuerto(con, codigoPaciente)+"", evolucionBase.getCodigoCuenta());
				
				boolean pacienteMuertoXEvolucion = UtilidadValidacion.esEvolucionConOrdenSalidaPacienteMuerto(con, evolucionBase.getCodigoCuenta(), Utilidades.convertirAEntero(evolucion)); 
				
				if(pacienteMuertoXEvolucion)
				{
					evolucionBase.setMuerto(String.valueOf(pacienteMuertoXEvolucion));
					HashMap informacionMuerte = UtilidadesHistoriaClinica.obtenerInfoMuertePaciente(con, codigoPaciente, evolucionBase.getCodigoCuenta());
					evolucionBase.getDiagnosticoMuerte().setAcronimo(informacionMuerte.get("diagnosticoMuerte").toString());
					evolucionBase.getDiagnosticoMuerte().setTipoCIE(Utilidades.convertirAEntero(informacionMuerte.get("diagnosticoMuerteCie").toString()));
					evolucionBase.getDiagnosticoMuerte().setNombre(informacionMuerte.get("diagnosticoMuerteNombre").toString());
					evolucionBase.setFechaMuerte(informacionMuerte.get("fechaMuerte").toString());
					evolucionBase.setHoraMuerte(informacionMuerte.get("horaMuerte").toString());
					evolucionBase.setCertificadoDefuncion(informacionMuerte.get("certificadoDefuncion").toString());
				}
			}
		}
		
		catch (Exception e) 
		{
			logger.error("Error al cargarBase: "+e);
		}finally{
	
			UtilidadBD.cerrarObjetosPersistencia(ps, rs, null);
		}
		return evolucionBase;
	}

	
	/**
	 * 
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static int insertarEvolucionBase(Connection con, HashMap parametros) 
	{
	 
		int consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_evolucion"); 
		PreparedStatementDecorator ps= null;

		try
		{
			
			if(System.getProperty("TIPOBD").equals("POSTGRESQL"))
    		{
	        	Statement stmt = con.createStatement();
	            stmt.execute("SET standard_conforming_strings TO off");
	            stmt.close();
    		}
			
			 ps =  new PreparedStatementDecorator(con.prepareStatement(insertarEvolucionBaseStr));
			
			/**
			 * INSERT INTO evoluciones (" +
				"codigo,"+ 
				"valoracion,"+
				"diagnostico_complicacion,"+
				"diagnostico_complicacion_cie,"+
				"fecha_grabacion,"+
				"hora_grabacion,"+
				"fecha_evolucion,"+
				"hora_evolucion,"+
				"informacion_dada_paciente,"+ // Datos Subjetivos.
				"desc_complicacion,"+ // Analisis.
				"hallazgos_importantes,"+ 
				"proced_quirurgicos_obst,"+ //Tipos Monitoreo
				"pronostico,"+ // Plan Manejo.
				"codigo_medico,"+
				"orden_salida,"+
				"tipo_evolucion,"+
				"recargo,"+
				"cobrable,"+ 18
				"tipo_diagnostico_principal,"+
				"datos_medico,"+
				"centro_costo,"+
				"conducta_seguir,"+
				"tipo_referencia," + 23
				"dias_incapacidad," +
				"observaciones_incapacidad,"+
				especialidad_solicitada "+
				") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
			 */
			
			ps.setInt(1,consecutivo);
			ps.setInt(2,Utilidades.convertirAEntero(parametros.get("valoracion")+""));
			
			if(UtilidadTexto.isEmpty(parametros.get("diagnostico_complicacion")+""))
				ps.setString(3, ConstantesBD.acronimoDiagnosticoNoSeleccionado);
			else
				ps.setString(3,parametros.get("diagnostico_complicacion")+"");
			
			if(UtilidadTexto.isEmpty(parametros.get("diagnostico_complicacion_cie")+"")||(parametros.get("diagnostico_complicacion_cie")+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setInt(4, ConstantesBD.codigoCieDiagnosticoNoSeleccionado);
			
			else
				ps.setInt(4,Utilidades.convertirAEntero(parametros.get("diagnostico_complicacion_cie")+""));
			
			ps.setDate(5,Date.valueOf(parametros.get("fecha_grabacion")+""));
			ps.setString(6,parametros.get("hora_grabacion")+"");
			ps.setDate(7,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(parametros.get("fecha_evolucion")+"")));
			ps.setString(8,parametros.get("hora_evolucion")+"");
			ps.setString(9,parametros.get("datos_subjetivos")+"");
			ps.setString(10,parametros.get("analisis")+"");
			ps.setString(11,parametros.get("hallazgos_importantes")+"");
			
			if(UtilidadTexto.isEmpty(parametros.get("tipo_monitoreo")+"")||(parametros.get("tipo_monitoreo")+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setNull(12, Types.INTEGER);
			else
				ps.setInt(12,Utilidades.convertirAEntero(parametros.get("tipo_monitoreo")+""));
			
			ps.setString(13,parametros.get("plan_manejo")+"");
			ps.setInt(14,Utilidades.convertirAEntero(parametros.get("codigo_medico")+""));
			ps.setBoolean(15,UtilidadTexto.getBoolean(parametros.get("orden_salida")+""));
			ps.setInt(16,Utilidades.convertirAEntero(parametros.get("tipo_evolucion")+""));
			ps.setInt(17,Utilidades.convertirAEntero(parametros.get("recargo")+""));
			ps.setBoolean(18, UtilidadTexto.getBoolean(parametros.get("cobrable")+""));
			ps.setInt(19,Utilidades.convertirAEntero(parametros.get("tipo_diagnostico_principal")+""));
			ps.setString(20,parametros.get("datos_medico")+"");
			ps.setInt(21,Utilidades.convertirAEntero(parametros.get("centro_costo")+""));
			ps.setInt(22,Utilidades.convertirAEntero(parametros.get("conducta_seguir")+""));
			
			if(UtilidadTexto.isEmpty(parametros.get("tipo_referencia")+""))
				ps.setNull(23, Types.CHAR);
			else
				ps.setString(23,parametros.get("tipo_referencia")+"");
			
			if(UtilidadTexto.isEmpty(parametros.get("dias_incapacidad")+"")||(parametros.get("dias_incapacidad")+"").equals(ConstantesBD.codigoNuncaValido+""))
				ps.setNull(24, Types.INTEGER);
			else
				ps.setInt(24, Utilidades.convertirAEntero(parametros.get("dias_incapacidad")+""));
			
			if(UtilidadTexto.isEmpty(parametros.get("observaciones_incapacidad")+""))
				ps.setNull(25, Types.CHAR);
			else
				ps.setString(25, parametros.get("observaciones_incapacidad")+"");
			
			if(UtilidadTexto.isEmpty(parametros.get("especialidadProfesional")+""))
				ps.setNull(26, Types.INTEGER);
			else
				ps.setInt(26, Utilidades.convertirAEntero(parametros.get("especialidadProfesional")+""));
			
			
             if(ps.executeUpdate() > 0)
			{						        
			  return consecutivo;	
						
			}
				
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
			
		}
		
		return ConstantesBD.codigoNuncaValido;
		
	}

	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static boolean insertarEvolucionesHospitalarias(Connection con, HashMap mapa) 
	{
		PreparedStatementDecorator ps= null;
		
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(insertarEvolucionHospitalariaStr));
			
			/**
			 * INSERT INTO evolu_hospitalarias (" +
			"evolucion," +
			"info_dada_pac_epicrisis," +
			"signos_vitales_epicrisis," +
			"hallazgos_epicrisis," +
			"complicaciones_epicrisis," +
			"diag_presuntivos_epicrisis," +
			"diag_definitivos_epicrisis," +
			"tratamiento_epicrisis," +
			"resultados_trat_epicrisis," +
			"cambios_manejo_epicrisis," +
			"proced_quirurgicos_obst_ep," +
			"resultado_examenes_diag_ep," +
			"pronostico_epicrisis," +
			"observaciones_epicrisis," +
			"va_a_epicrisis" +
			") VALUES (?,?,"+ValoresPorDefecto.getValorFalseParaConsultas()+",?,?,"+ ValoresPorDefecto.getValorTrueParaConsultas() +",?,"+ ValoresPorDefecto.getValorFalseParaConsultas() +","+ ValoresPorDefecto.getValorFalseParaConsultas() +","+ ValoresPorDefecto.getValorFalseParaConsultas() +",?,"+ ValoresPorDefecto.getValorFalseParaConsultas() +",?,"+ ValoresPorDefecto.getValorFalseParaConsultas() +","+ ValoresPorDefecto.getValorTrueParaConsultas() +")" ;
	
			 */
			ps.setInt(1, Utilidades.convertirAEntero(mapa.get("consecutivo_evolucion")+""));
			ps.setBoolean(2, UtilidadTexto.getBoolean(mapa.get("datos_subjetivos")+""));
			ps.setBoolean(3, UtilidadTexto.getBoolean(mapa.get("hallazgos_epicrisis")+""));
			ps.setBoolean(4, UtilidadTexto.getBoolean(mapa.get("analisis")+""));
			ps.setBoolean(5, UtilidadTexto.getBoolean(mapa.get("diagnosticos_definitivos")+""));
			ps.setBoolean(6, UtilidadTexto.getBoolean(mapa.get("balance_liquidos")+""));
			ps.setBoolean(7, UtilidadTexto.getBoolean(mapa.get("plan_manejo")+""));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
		
		}
		return false;
	}


	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static boolean insertarEvolSignosVitales(Connection con, HashMap mapa) 
	{
		PreparedStatementDecorator ps= null;
		
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(insertarSignosVitalesEvolucionStr));
			
			/**
			 * NSERT INTO evol_signos_vitales (" +
							"evolucion,"+
							"signo_vital,"+
							"descripcion,"+
							"valor"+
							") VALUES (?,?,?,?)";
			 */
			
			
			ps.setInt(1, Utilidades.convertirAEntero(mapa.get("consecutivo_evolucion")+""));
			ps.setInt(2, Utilidades.convertirAEntero(mapa.get("signo_vital")+""));
			
			if(UtilidadTexto.isEmpty(mapa.get("descripcion")+""))
				ps.setNull(3, Types.CHAR);
			else
				ps.setString(3, mapa.get("descripcion")+"");
			
			ps.setString(4, mapa.get("valor")+"");
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
			
		}
		return false;
	}



	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static boolean insertarDiagnosticosEvolucion(Connection con, HashMap mapa) 
	{
		PreparedStatementDecorator ps= null;
	 
		
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(insertarDiagnosticosEvolucionStr));
			
			/**
			 * 
			INSERT INTO evol_diagnosticos (" +
						"evolucion,"+
						"acronimo_diagnostico,"+
						"tipo_cie_diagnostico,"+
						"numero,"+
						"principal,"+
						"definitivo"+
						") VALUES (?,?,?,?,?,?)";
			 */
			
			logger.info("INSERT DX-->"+insertarDiagnosticosEvolucionStr);
			Utilidades.imprimirMapa(mapa);
			
			ps.setInt(1, Utilidades.convertirAEntero(mapa.get("consecutivo_evolucion")+""));
			ps.setString(2, mapa.get("acronimo_diagnostico")+"");
			ps.setInt(3, Utilidades.convertirAEntero(mapa.get("tipo_cie_diagnostico")+""));
			ps.setInt(4, Utilidades.convertirAEntero(mapa.get("numero")+""));
			ps.setBoolean(5, UtilidadTexto.getBoolean(mapa.get("principal")+""));
			ps.setBoolean(6, UtilidadTexto.getBoolean(mapa.get("definitivo")+""));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
			
		}
		return false;
	}


	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static boolean insertarBalanceLiquidosEvol(Connection con, HashMap mapa) 
	{
		PreparedStatementDecorator ps= null;
	
		
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(insertarBalanceLiquidosEvolucionStr));
			
			/**
			 * INSERT INTO balance_liquidos_evol (" +
						"evolucion,"+
						"balance_liq_evol_cc_ins,"+
						"valor"+
						") VALUES (?,?,?)";
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(mapa.get("consecutivo_evolucion")+""));
			ps.setInt(2, Utilidades.convertirAEntero(mapa.get("balance_liquidos")+""));
			ps.setString(3, mapa.get("valor")+"");
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
			
		}
		return false;
	}


	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static boolean insertarIngresoCuidadoEspecial(Connection con, HashMap mapa) 
	{
		PreparedStatementDecorator ps= null;

		
		
		int consecutivo= UtilidadBD.obtenerSiguienteValorSecuencia(con, "historiaclinica.seq_ingres_cuidados_especial");

		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(insertarIngresosCuidadosEspecialesStr));
			
			/**
			 * NSERT INTO ingresos_cuidados_especiales (" +
							"codigo,"+
							"ingreso,"+
							"estado,"+
							"indicativo,"+
							"tipo_monitoreo,"+
							"usuario_resp,"+
							"fecha_resp,"+
							"hora_resp,"+
							"usuario_modifica,"+
							"fecha_modifica,"+
							"hora_modifica,"+
							"evolucion_orden"+
							") VALUES (?,?,?,?,?,?,?,?,?,?,?,?)
			 */
			
			ps.setInt(1, consecutivo);
			ps.setInt(2, Utilidades.convertirAEntero(mapa.get("id_ingreso")+""));
			ps.setString(3, mapa.get("estado_ingreso")+"");
			ps.setString(4, mapa.get("indicativo_ingreso")+"");
			ps.setInt(5, Utilidades.convertirAEntero(mapa.get("tipo_monitoreo")+""));
			ps.setString(6, mapa.get("usuario_resp")+"");
			ps.setDate(7, Date.valueOf(mapa.get("fecha_resp")+""));
			ps.setString(8, mapa.get("hora_resp")+"");
			ps.setString(9, mapa.get("usuario_modifica")+"");
			ps.setDate(10, Date.valueOf(mapa.get("fecha_modifica")+""));
			ps.setString(11, mapa.get("hora_modifica")+"");
			ps.setInt(12, Utilidades.convertirAEntero(mapa.get("evolucion")+""));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
			
		}
		return false;
	}

	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @param tipoEvolucion
	 * @return
	 */
	public static int consultarCausaExternaValoracion(Connection con, int codigoCuenta, int tipoEvolucion) 
	{
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		

		
		
		try
		{
			
			if(tipoEvolucion==ConstantesBD.codigoEspecialidadValoracionHospitalizacion)
			{
				PreparedStatementDecorator buscarCausaExternaValoracionInicialStatement= new PreparedStatementDecorator(con.prepareStatement(cargarCausaExternaUltimaValoracionHospitalizacionStr));
				buscarCausaExternaValoracionInicialStatement.setInt(1, codigoCuenta);
				
				rs=new ResultSetDecorator(buscarCausaExternaValoracionInicialStatement.executeQuery());
				
				if (rs.next())
				{
					int resp=rs.getInt("codigoCausaExterna");
					rs.close();
					return resp;
				}
				else
				{
					rs.close();
					return 0;
				}
				
			}
			else
			{
				PreparedStatementDecorator buscarCausaExternaValoracionInicialStatement= new PreparedStatementDecorator(con.prepareStatement(cargarCausaExternaUltimaValoracionUrgenciasStr));
				buscarCausaExternaValoracionInicialStatement.setInt(1, codigoCuenta);
				
				rs=new ResultSetDecorator(buscarCausaExternaValoracionInicialStatement.executeQuery());
				
				if (rs.next())
				{
					int resp=rs.getInt("codigoCausaExterna");
					rs.close();
					return resp;
				}
				else
				{
					
					return 0;
				}
				
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en buscarCausaExternaValoracionInicialStatement de SqlBaseEvoluciones: "+e);
			return 0;
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
		}
	}

	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static int obtenerCodigoUltimaEvolucion(Connection con, int codigoCuenta) 
	{
		String consulta="SELECT " +
						"max(e.codigo) as ultimaevol " +
						"from " +
							"evoluciones e " +
							"inner join solicitudes s on(e.valoracion=s.numero_solicitud) " +
						"where " +
							"s.cuenta=? ";
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			
			logger.info("consulta>>>>>>>>>>"+consulta);
			logger.info("codigoCuenta>>>>>>>>>>"+codigoCuenta);
			
			ps.setInt(1, codigoCuenta);
			rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getInt(1);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
		}
		return ConstantesBD.codigoNuncaValido;
	}

	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static boolean actualizarIngresoCuidadoEspecial(Connection con, HashMap mapa) 
	{
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(actualizarIngresoCuidadosEspecialesStr));
			
			logger.info("INGRESO >>>>>>"+mapa.get("id_ingreso"));
			
			/**
			 * UPDATE ingresos_cuidados_especiales SET 
			 * tipo_monitoreo=?, 
			 * centro_costo=?, 
			 * usuario_modifica=?, 
			 * fecha_modifica=?, 
			 * hora_modifica=? WHERE ingreso=? 
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(mapa.get("tipo_monitoreo")+""));
			ps.setInt(2, Utilidades.convertirAEntero(mapa.get("area_monitoreo")+""));
			ps.setString(3, mapa.get("usuario_modifica")+"");
			ps.setDate(4, Date.valueOf(mapa.get("fecha_modifica")+""));
			ps.setString(5, mapa.get("hora_modifica")+"");
			ps.setInt(6, Utilidades.convertirAEntero(mapa.get("id_ingreso")+""));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
			
		}
		return false;
		
	}

	/**
	 * 
	 * @param con
	 * @param codigoEvolucion
	 * @return
	 */
	public static String[] obtenerDiagnosticoComplicacion(Connection con, int codigoEvolucion) 
	{
		String cuidadoEspecial[]={"","",""};
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		
		try
		{
		String consultaStr= "SELECT e.diagnostico_complicacion AS diag_complicacion, e.diagnostico_complicacion_cie AS diag_cie, d.nombre as nombre_diagnostico " +
											"FROM evoluciones e " +
											"INNER JOIN diagnosticos d ON(e.diagnostico_complicacion=d.acronimo and e.diagnostico_complicacion_cie=d.tipo_cie) " +
											"WHERE e.codigo=? ";
		
		ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr));
		
		ps.setInt(1, codigoEvolucion);
		
		rs=new ResultSetDecorator(ps.executeQuery());

			if (rs.next())
			{
				
				cuidadoEspecial[0]=rs.getString("diag_complicacion");
				cuidadoEspecial[1]=rs.getString("diag_cie");
				cuidadoEspecial[2]=rs.getString("nombre_diagnostico");
				
			}
		}	
		catch (SQLException e) 
		{
			logger.error("Error en obtenerFechaMesCierreSaldoCapitacion de SqlBaseUtilidadValidacionDao: "+e);
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
		}
		return cuidadoEspecial;
	}

	
	/**
	 * 
	 * @param con
	 * @param codigoEvolucion
	 * @return
	 */
	public static String[] obtenerDiagnosticoPrincipal(Connection con, int codigoEvolucion) 
	{
		String cuidadoEspecial[]={"","",""};
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		

		try {
			String consultaStr= "SELECT e.acronimo_diagnostico AS diag_complicacion, e.tipo_cie_diagnostico AS diag_cie, d.nombre as nombre_diagnostico " +
											"FROM evol_diagnosticos e " +
											"INNER JOIN diagnosticos d ON(e.acronimo_diagnostico=d.acronimo and e.tipo_cie_diagnostico=d.tipo_cie) " +
											"WHERE e.evolucion=? and e.principal='"+ValoresPorDefecto.getValorTrueCortoParaConsultas()+"' ";
		
			logger.info("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
			logger.info("OOOOOOOOOOOOOOOOO - Estado empezar Insert Nueva Evolucion");
			logger.info("Sql: " + consultaStr);
			logger.info("Codigo Evolucion: " + codigoEvolucion);
			logger.info("String : " + cuidadoEspecial);
			
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr));
			ps.setInt(1, codigoEvolucion);
			rs=new ResultSetDecorator(ps.executeQuery());

			if (rs.next()) {
				cuidadoEspecial[0]=rs.getString("diag_complicacion");
				cuidadoEspecial[1]=rs.getString("diag_cie");
				cuidadoEspecial[2]=rs.getString("nombre_diagnostico");
				
			}
		}	
		
		
		catch (SQLException e) 
		{
			logger.error("Error en obtenerFechaMesCierreSaldoCapitacion de SqlBaseUtilidadValidacionDao: "+e);
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
		}
		return cuidadoEspecial;
	}

	
	/**
	 * 
	 * @param con
	 * @param codigoIngreso
	 * @return
	 */
	public static String[] consultarTipoMonitoreo(Connection con, int codigoIngreso) 
	{
		String cuidadoEspecial[]={"",""};
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		
		
		try
		{
		String consultaStr= "SELECT tipo_monitoreo, centro_costo " +
											"FROM ingresos_cuidados_especiales " +
											"WHERE ingreso=? and estado='"+ConstantesIntegridadDominio.acronimoEstadoActivo+"' ";
		
		ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr));
		
		ps.setInt(1, codigoIngreso);
		
		rs=new ResultSetDecorator(ps.executeQuery());

			if (rs.next())
			{
				
				cuidadoEspecial[0]=rs.getString("tipo_monitoreo");
				cuidadoEspecial[1]=rs.getString("centro_costo");
				
			}
		}	
		catch (SQLException e) 
		{
			logger.error("Error en obtenerFechaMesCierreSaldoCapitacion de SqlBaseUtilidadValidacionDao: "+e);
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
		}
		return cuidadoEspecial;
	}


	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static boolean actualizarAreaCuenta(Connection con, HashMap mapa) 
	{
		PreparedStatementDecorator ps= null;

		
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(actualizarAreaCuentaStr));
			
			logger.info("CUENTA >>>>>>"+mapa.get("id_cuenta"));
			
			/**
			 * UPDATE cuentas SET area=?, usuario_modifica=?, fecha_modifica=?, hora_modifica=? WHERE id=? 
			 */
			
			ps.setInt(1, Utilidades.convertirAEntero(mapa.get("area")+""));
			ps.setString(2, mapa.get("usuario_modifica")+"");
			ps.setDate(3, Date.valueOf(mapa.get("fecha_modifica")+""));
			ps.setString(4, mapa.get("hora_modifica")+"");
			ps.setInt(5, Utilidades.convertirAEntero(mapa.get("id_cuenta")+""));
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
			
		}
		return false;
	}


	/**
	 * 
	 * @param con
	 * @param codigoEvolucion
	 * @return
	 */
	public static ArrayList<Diagnostico> consultaDiagnosticosRelacionados(Connection con, int codigoEvolucion) 
	{
		ArrayList<Diagnostico> diagnostico= new ArrayList<Diagnostico>();
		Diagnostico dia;
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		
		
		try
		{
		String consultaStr= "SELECT  CASE WHEN (e.acronimo_diagnostico IS NULL OR e.tipo_cie_diagnostico IS NULL) THEN '' ELSE  e.acronimo_diagnostico || '"+ConstantesBD.separadorSplit+"' || e.tipo_cie_diagnostico || '"+ConstantesBD.separadorSplit+"' || d.nombre END as nombre_diagnostico " +
											"FROM evol_diagnosticos e " +
											"INNER JOIN diagnosticos d ON(e.acronimo_diagnostico=d.acronimo and e.tipo_cie_diagnostico=d.tipo_cie) " +
											"WHERE e.evolucion=? and e.principal='"+ValoresPorDefecto.getValorFalseCortoParaConsultas()+"' ";
		
		ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr));
		
		logger.info("consultaStr >>>>>>>>>"+consultaStr);
		logger.info("codigoEvol >>>>>>>>>"+codigoEvolucion);
		
		ps.setInt(1, codigoEvolucion);		
		rs=new ResultSetDecorator(ps.executeQuery());
		
		while(rs.next())
		{
			dia = new Diagnostico();
			dia.setValor(rs.getString(1));
			diagnostico.add(dia);		
		}
		
		}	
		catch (SQLException e) 
		{
			logger.error("Error en obtenerFechaMesCierreSaldoCapitacion de SqlBaseUtilidadValidacionDao: "+e);
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
		}
		
		return diagnostico;
	}

	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public static String[] consultarInfoFallecimiento(Connection con, int codigoCuenta) 
	{
		String infoFallecimiento[]={"","","","",""};
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		
		
		try
		{
			String consultaStr= "select fecha_fallece, hora_fallece, diag_fallece, tipo_cie_fallece, d.nombre as nombre " +
								"from solicitudes_cirugia sc inner join solicitudes s on(s.numero_solicitud=sc.numero_solicitud) left outer join diagnosticos d on(sc.diag_fallece=d.acronimo and sc.tipo_cie_fallece=d.tipo_cie) " +
								"where s.cuenta=? and fecha_fallece is not null and hora_fallece is not null ";
			
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr));
			
			
			logger.info("CONSULTA INFORMACION FALLECIMIENTO EN LA CIRUGIA >>>>"+consultaStr);
			
			logger.info("CONSULTA INFORMACION FALLECIMIENTO EN LA CIRUGIA --> CUENTA >>>"+codigoCuenta);
			
			ps.setInt(1, codigoCuenta);
			
			rs=new ResultSetDecorator(ps.executeQuery());

			if (rs.next())
			{
				
				infoFallecimiento[0]=rs.getString("fecha_fallece");
				infoFallecimiento[1]=rs.getString("hora_fallece");
				infoFallecimiento[2]=rs.getString("diag_fallece");
				infoFallecimiento[3]=rs.getString("tipo_cie_fallece");
				infoFallecimiento[4]=rs.getString("nombre");
				
			}
		}	
		catch (SQLException e) 
		{
			logger.error("Error en obtenerFechaMesCierreSaldoCapitacion de SqlBaseUtilidadValidacionDao: "+e);
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
		}
		return infoFallecimiento;
	}

	/**
	 * 
	 * @param con
	 * @param codigoEvolucion
	 * @param seccionFija
	 * @return
	 */
	public static boolean enviadoEpicrisis(Connection con, String codigoEvolucion) 
	{
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		
		try
		{
			
			String consultaEpicrisis="SELECT codigo FROM epicrisis_secciones WHERE evolucion=? ";
			
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaEpicrisis));
			
			logger.info("CONSULTA EPICRISIS >>>>"+consultaEpicrisis);
			logger.info("CODIGO EVOLUCION >>>>"+codigoEvolucion);
			
			ps.setInt(1, Utilidades.convertirAEntero(codigoEvolucion+""));
			
			rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				return true;
			}
			else
			{
				return false;
			}
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
		}
		return false;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public static boolean insertarComentariosEvolucion(Connection con, HashMap mapa) 
	{
		PreparedStatementDecorator ps= null;

		
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(insertarComentariosEvolucionesStr));
			
			ps.setDouble(1, UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_comentarios_evolucion"));
			ps.setInt(2, Utilidades.convertirAEntero(mapa.get("evolucion")+""));
			ps.setString(3, mapa.get("valor")+"");
			ps.setDate(4, Date.valueOf(mapa.get("fecha")+""));
			ps.setString(5, mapa.get("hora")+""); 
			ps.setString(6, mapa.get("usuario")+"");
			
			return ps.executeUpdate()>0;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
			
		}
		return false;
	}


	/**
	 * 
	 * @param con
	 * @param codigoEvolucion
	 * @return
	 */
	public static String[] consultarOrdenEgreso(Connection con, int codigoEvolucion) 
	{
		String infoFallecimiento[]={"","","",""};
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		
		
		try
		{
			String consultaStr= "SELECT " +
								"eg.estado_salida as estado_salida, eg.destino_salida as destino_salida, e.orden_salida as orden_salida, e.conducta_seguir as conducta_seguir " +
								"FROM evoluciones e inner join egresos eg on(eg.evolucion=e.codigo) " +
								"WHERE e.codigo=? ";
			
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr));
			
			
			logger.info("CONSULTA INFORMACION FALLECIMIENTO EN LA CIRUGIA >>>>"+consultaStr);
			
			logger.info("CONSULTA INFORMACION FALLECIMIENTO EN LA CIRUGIA --> EVOLUCION >>>"+codigoEvolucion);
			
			ps.setInt(1, codigoEvolucion);
			
			rs=new ResultSetDecorator(ps.executeQuery());

			if (rs.next())
			{
				infoFallecimiento[0]=rs.getString("estado_salida");
				infoFallecimiento[1]=rs.getString("destino_salida");
				infoFallecimiento[2]=rs.getString("orden_salida");
				infoFallecimiento[3]=rs.getString("conducta_seguir");
			}
		}	
		catch (SQLException e) 
		{
			logger.error("Error en obtenerFechaMesCierreSaldoCapitacion de SqlBaseUtilidadValidacionDao: "+e);
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
		}
		return infoFallecimiento;
	}

	
	/**
	 * 
	 * @param con
	 * @param codigoEvolucion
	 * @return
	 */
	public static int obtenerUltimaConducta(Connection con, int codigoEvolucion) 
	{
		String consulta="SELECT " +
						"conducta_seguir as conductaseguir " +
						"from " +
							"evoluciones " +
						"where " +
							"codigo=? ";
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs = null;
		
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			
			logger.info("consulta>>>>>>>>>>"+consulta);
			logger.info("codigoCuenta>>>>>>>>>>"+codigoEvolucion);
			
			ps.setInt(1, codigoEvolucion);
			rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
			{
				return rs.getInt(1);
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}finally{
			if (ps != null){
				try{
					ps.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
			if (rs != null){
				try{
					rs.close();
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseEventosAdversosDao " + sqlException.toString() );
				}
			}
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	
	/**
	 * MT 5568 
	 * Metodo que consulta la informacion del area del paciente y el tipo de monitoreo por cuenta
	 * @param con
	 * @param idCuenta
	 * @return
	 * @author javrammo
	 */
	public static  InformacionCentroCostoValoracionDto informacionCentroCostoDeIngresoActivoCuidadoEspecialXCuenta(Connection con, int idCuenta){
		
		
		InformacionCentroCostoValoracionDto resp = new InformacionCentroCostoValoracionDto();;
		String consulta = " SELECT ice.centro_costo as idCentroCosto, cc.nombre as nombreCC, ice.tipo_monitoreo as idTipoMon, tm.nombre as nombreTM FROM INGRESOS_CUIDADOS_ESPECIALES ICE "+ 
				" INNER JOIN CUENTAS CTA ON CTA.ID_INGRESO = ICE.INGRESO "+
				" INNER JOIN CENTROS_COSTO_X_TIPO_M CCXTM ON CCXTM.tipo_monitoreo = ICE.tipo_monitoreo AND CCXTM.centro_costo= ICE.centro_costo " +
				" INNER JOIN CENTROS_COSTO CC ON CC.CODIGO = CCXTM.CENTRO_COSTO "+
				" INNER JOIN TIPO_MONITOREO TM ON TM.CODIGO = CCXTM.TIPO_MONITOREO "+
				" WHERE ICE.ESTADO = '"+ConstantesIntegridadDominio.acronimoEstadoActivo+"' AND cta.id = ?";

		PreparedStatement ps = null;
		ResultSet rs = null;
		try 
		{
			ps= con.prepareStatement(consulta);
			ps.setInt(1,idCuenta);
			rs=ps.executeQuery();
			if(rs.next())
			{
				resp.setIdCentroCosto(rs.getInt("idCentroCosto"));
				resp.setDescripcionCentroCosto(rs.getString("nombreCC"));
				resp.setIdTipoMonitoreo(rs.getInt("idTipoMon"));
				resp.setDescripcionTipoMonitoreo(rs.getString("nombreTM"));
			}
		} 
		catch (SQLException e) 
		{
			logger.error("Error consultando tipo monitoreo : "+e);
		}
		finally{
			if(ps != null){				
				try {
					ps.close();
				} catch (SQLException e) {}
			}
			if(rs != null){
				try {
					rs.close();
				} catch (SQLException e) {}
			}
		}
		
		
		return resp;
	}

	
}