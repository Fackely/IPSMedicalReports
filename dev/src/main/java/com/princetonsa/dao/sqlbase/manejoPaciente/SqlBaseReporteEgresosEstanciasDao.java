/*
 * Sep 03, 2008
 */
package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.manejoPaciente.DtoFiltroReporteIndicadoresHospitalizacion;
import com.princetonsa.dto.manejoPaciente.DtoResultadoConsultaIndicadoresHospitalizacion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import com.princetonsa.dao.DaoFactory;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.ConstantesBDManejoPaciente;

/**
 * 
 * @author Sebastián Gómez 
 * Clase usada para la edición y definición de las consultas
 * que se usan para el reporte de estadísticas de egresos y estancias
 *
 */
public class SqlBaseReporteEgresosEstanciasDao 
{
	/**
	 * Para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseReporteEgresosEstanciasDao.class);
	
	//***********CONSULTAS REPORTE RESUMEN MENSUAL EGRESOS********************
	/**
	 * Método para consultar los datos del reporte de resumen mensual egresos
	 */
	public static HashMap<String, Object> obtenerDatosResumenMensualEgresos(Connection con,HashMap campos)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try
		{
			//****************SE TOMAN PARÁMETROS*****************************+
			int codigoInstitucion = Integer.parseInt(campos.get("codigoInstitucion").toString());
			int codigoCentroAtencion = Integer.parseInt(campos.get("codigoCentroAtencion").toString());
			String fechaInicial = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString());
			String fechaFinal = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString());
			int codigoViaIngreso = Integer.parseInt(campos.get("codigoViaIngreso").toString());
			String tipoPaciente = campos.get("codigoTipoPaciente").toString();
			int codigoSexo = Integer.parseInt(campos.get("codigoSexo").toString());
			String estadoFacturacion = campos.get("estadoFacturacion").toString();
			
			HashMap centrosCosto = (HashMap)campos.get("centrosCosto");
			String listadoCentrosCosto = "";
			for(int i=0;i<Integer.parseInt(centrosCosto.get("numRegistros").toString());i++)
				if(UtilidadTexto.getBoolean(centrosCosto.get("checkbox_"+i).toString()))
					listadoCentrosCosto += (listadoCentrosCosto.equals("")?"":",") + centrosCosto.get("codigo_"+i);
			//*****************************************************************
			
			
			String consulta = "", consultaEgresos = "", consultaEstancias = "", consultaConsultaExterna = "";
			
			//*********SE LLENA LA CONSULTA DE EGRESOS ****************************************************
			if(codigoViaIngreso==ConstantesBD.codigoNuncaValido||
				codigoViaIngreso==ConstantesBD.codigoViaIngresoUrgencias||
				codigoViaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion)
			{
				consultaEgresos = "( "+
					"SELECT "+ 
					"e.fecha_egreso - p.fecha_nacimiento AS edad, "+
					"c.via_ingreso as codigo_via_ingreso, "+
					"getnombreviaingreso(c.via_ingreso) as nombre_via_ingreso, "+
					"c.tipo_paciente as codigo_tipo_paciente, "+
					//Para la vía de ingreso de urgencias no es necesario mostrar el tipo de paciente
					"CASE WHEN c.via_ingreso = "+ConstantesBD.codigoViaIngresoUrgencias+" THEN '' ELSE getnombretipopaciente(c.tipo_paciente) END as nombre_tipo_paciente, "+ 
					"1 as valor, "+
					"'Egresos'  as tipo "+ 
					"FROM egresos e "+ 
					"INNER JOIN cuentas c ON(c.id = e.cuenta) "+ 
					"INNER JOIN centros_costo cc ON(cc.codigo = c.area and cc.centro_atencion = "+codigoCentroAtencion+") "+ 
					"INNER JOIN personas p ON(p.codigo = c.codigo_paciente) "+ 
					"WHERE e.fecha_egreso BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.usuario_responsable IS NOT NULL " ;
				
				//Filtro de la vía de ingreso
				if(codigoViaIngreso==ConstantesBD.codigoNuncaValido)
					consultaEgresos += " and c.via_ingreso in ("+ConstantesBD.codigoViaIngresoUrgencias+","+ConstantesBD.codigoViaIngresoHospitalizacion+") ";
				else
					consultaEgresos += " and c.via_ingreso = "+codigoViaIngreso+" ";
				
				//Filtro del tipo de paciente
				consultaEgresos += (tipoPaciente.equals("")?"":" and c.tipo_paciente = '"+tipoPaciente+"' ");
				
				//Filtro del estado de facturación
				if(!estadoFacturacion.equals(""))
					if(UtilidadTexto.getBoolean(estadoFacturacion))
						consultaEgresos += " and c.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaFacturada+" ";
					else
						consultaEgresos += " and c.estado_cuenta <> "+ConstantesBD.codigoEstadoCuentaFacturada+" ";
				
				//Filtro de los centros de costo
				if(!listadoCentrosCosto.equals(""))
					consultaEgresos += " and c.area in ("+listadoCentrosCosto+") ";
				
				//Filtro del sexo
				consultaEgresos += (codigoSexo>0?" and p.sexo = "+codigoSexo+" ":"");
				
				consultaEgresos += ")";		
			}
			//*********************************************************************************************
			///*********SE LLENA LA CONSULTA DE ESTANCIAS****************************************************
			//Solo se edita consulta para todas las vias de ingreso (con tal que no sea cirugía ambulatoria), u hospitalizacion / hospitalizado o Urgencias
			if((codigoViaIngreso==ConstantesBD.codigoNuncaValido&&!tipoPaciente.equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria))||
				(codigoViaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion&&!tipoPaciente.equals(ConstantesBD.tipoPacienteCirugiaAmbulatoria))||
				codigoViaIngreso==ConstantesBD.codigoViaIngresoUrgencias)
			{
				consultaEstancias = "( "+
					"SELECT "+ 
					"e.fecha_egreso - p.fecha_nacimiento AS edad, "+
					"c.via_ingreso as codigo_via_ingreso, "+
					"getnombreviaingreso(c.via_ingreso) as nombre_via_ingreso, "+
					"c.tipo_paciente as codigo_tipo_paciente, "+
					//Para urgencias no es necesario consultar el nombre del tipo de paciente
					"CASE WHEN c.via_ingreso = "+ConstantesBD.codigoViaIngresoUrgencias+" THEN '' ELSE getnombretipopaciente(c.tipo_paciente) END as nombre_tipo_paciente, "+ 
					"gettiempoestancia(getfechaingreso(c.id,c.via_ingreso),substr(gethoraingreso(c.id,c.via_ingreso),0,6),e.fecha_egreso,e.hora_egreso,c.via_ingreso) as valor, "+
					"'Estancias'  as tipo "+ 
					"FROM egresos e "+ 
					"INNER JOIN cuentas c ON(c.id = e.cuenta) "+  
					"INNER JOIN centros_costo cc ON(cc.codigo = c.area and cc.centro_atencion = "+codigoCentroAtencion+") "+ 
					"INNER JOIN personas p ON(p.codigo = c.codigo_paciente) "+ 
					"WHERE e.fecha_egreso BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.usuario_responsable IS NOT NULL " ;
				
				//Filtro por la via de ingreso
				if(codigoViaIngreso==ConstantesBD.codigoNuncaValido)
				{
					if(tipoPaciente.equals(""))
						consultaEstancias += " and ((c.via_ingreso = "+ConstantesBD.codigoViaIngresoHospitalizacion+" and c.tipo_paciente = '"+ConstantesBD.tipoPacienteHospitalizado+"') or c.via_ingreso = "+ConstantesBD.codigoViaIngresoUrgencias+") ";
					else if(tipoPaciente.equals(ConstantesBD.tipoPacienteAmbulatorio))
						consultaEstancias += " and c.via_ingreso = "+ConstantesBD.codigoViaIngresoUrgencias+" ";
					else if (tipoPaciente.equals(ConstantesBD.tipoPacienteHospitalizado))
						consultaEstancias += " and c.via_ingreso = "+ConstantesBD.codigoViaIngresoHospitalizacion+" and c.tipo_paciente = '"+ConstantesBD.tipoPacienteHospitalizado+"' ";
				}	
				else if(codigoViaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion)
					consultaEstancias += " and c.via_ingreso = "+ConstantesBD.codigoViaIngresoHospitalizacion+" and c.tipo_paciente = '"+ConstantesBD.tipoPacienteHospitalizado+"' ";
				else if(codigoViaIngreso == ConstantesBD.codigoViaIngresoUrgencias)
					consultaEstancias += " and c.via_ingreso = "+ConstantesBD.codigoViaIngresoUrgencias+" ";
				
				//Filtro del estado de facturación
				if(!estadoFacturacion.equals(""))
					if(UtilidadTexto.getBoolean(estadoFacturacion))
						consultaEstancias += " and c.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaFacturada+" ";
					else
						consultaEstancias += " and c.estado_cuenta <> "+ConstantesBD.codigoEstadoCuentaFacturada+" ";
				
				//Filtro de los centros de costo
				if(!listadoCentrosCosto.equals(""))
					consultaEstancias += " and c.area in ("+listadoCentrosCosto+") ";
				
				//Filtro del sexo
				consultaEstancias += (codigoSexo>0?" and p.sexo = "+codigoSexo+" ":"");
				
				consultaEstancias += ")";
			}
			//**********************************************************************************************
			//*************SE LLENA LA CONSULTA DE CONSULTA EXTERNA*****************************************
			//Solo se edita esta consulta cuando se seleccionó via de ingresio todas (pero se seleccionó tipo de paciente todos o ambulatorioas), o se seleccionó via de ingreso consulta externa
			if((codigoViaIngreso==ConstantesBD.codigoNuncaValido&&(tipoPaciente.equals("")||tipoPaciente.equals(ConstantesBD.tipoPacienteAmbulatorio)))||
				codigoViaIngreso==ConstantesBD.codigoViaIngresoConsultaExterna)
			{
				consultaConsultaExterna = "( "+
					"SELECT "+ 
					"c.fecha_apertura - p.fecha_nacimiento AS edad, "+
					"c.via_ingreso as codigo_via_ingreso, "+
					"getnombreviaingreso(c.via_ingreso) as nombre_via_ingreso, "+
					"c.tipo_paciente as codigo_tipo_paciente, "+
					"CASE WHEN c.via_ingreso = "+ConstantesBD.codigoViaIngresoConsultaExterna+" THEN '' ELSE getnombretipopaciente(c.tipo_paciente) END as nombre_tipo_paciente, "+ 
					"1 as valor, "+
					"CASE WHEN esCuentaPrimeraVez(c.id) = '"+ConstantesBD.acronimoSi+"' THEN 'Primera Vez' ELSE 'Control' END  as tipo "+ 
					"FROM cuentas c "+ 
					"INNER JOIN centros_costo cc ON(cc.codigo = c.area and cc.centro_atencion = "+codigoCentroAtencion+") "+ 
					"INNER JOIN personas p ON(p.codigo = c.codigo_paciente) "+ 
					"WHERE c.fecha_apertura BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and c.via_ingreso = "+ConstantesBD.codigoViaIngresoConsultaExterna+" ";
				
				//Filtro del estado de facturación
				if(!estadoFacturacion.equals(""))
					if(UtilidadTexto.getBoolean(estadoFacturacion))
						consultaConsultaExterna += " and c.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaFacturada+" ";
					else
						consultaConsultaExterna += " and c.estado_cuenta <> "+ConstantesBD.codigoEstadoCuentaFacturada+" ";
				
				//Filtro de los centros de costo
				if(!listadoCentrosCosto.equals(""))
					consultaConsultaExterna += " and c.area in ("+listadoCentrosCosto+") ";
				
				//Filtro del sexo
				consultaConsultaExterna += (codigoSexo>0?" and p.sexo = "+codigoSexo+" ":"");
				
				consultaConsultaExterna += ")";
			}
			//**********************************************************************************************
			
			//Se edita consulta FINAL
			consulta = "";
			if(!consultaEgresos.equals(""))
				consulta += consultaEgresos;
			if(!consultaEstancias.equals(""))
				consulta += (consulta.equals("")?"":" UNION ") + consultaEstancias;
			if(!consultaConsultaExterna.equals(""))
				consulta += (consulta.equals("")?"":" UNION ") + consultaConsultaExterna;
			
			if(!consulta.equals(""))
			{
				consulta = "SELECT re.codigo as codigo_reporte, re.nombre_etiqueta,re.orden,sum(t.valor) as valor,t.tipo,t.codigo_via_ingreso,t.nombre_via_ingreso,t.codigo_tipo_paciente,t.nombre_tipo_paciente FROM ("+
					consulta + ") t "+
					"INNER JOIN rangos_estadisticos re ON(re.rango_inicial <= t.edad AND re.rango_final >= t.edad and re.institucion = "+codigoInstitucion+" and re.activo = '"+ConstantesBD.acronimoSi+"' and re.reporte = "+ConstantesBDManejoPaciente.tipoReporteResumenMensualEgresos+") "+ 
					"GROUP BY re.codigo,re.nombre_etiqueta,re.orden,t.tipo,t.codigo_via_ingreso,t.nombre_via_ingreso,t.codigo_tipo_paciente,t.nombre_tipo_paciente "+ 
					"order by re.orden";	
				
				logger.info("consulta del reporte de consulta externa: "+consulta);
				Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
				resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)), true, true);
					
			}
			else
				resultados.put("numRegistros", "0");
			
			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerDatosResumenMensualEgresos: "+e);
			resultados.put("numRegistros", "0");
		}
		return resultados;
	}
	//**************************************************************************************
	//*************CONSULTA TIPO REPORTE EGRESOS POR CONVENIO*********************************
	/**
	 * Método para obtener la consulta del tipo de reporte de egresos por convenio
	 */
	public static String obtenerConsultaReporteEgresosConvenio(HashMap campos,int tipoBD)
	{
		String consulta = "", consulta1 = "", consulta2 = "";
		
		//************SE TOMAN LOS PARÁMETROS*****************************
		int codigoCentroAtencion = Integer.parseInt(campos.get("codigoCentroAtencion").toString());
		String fechaInicial = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString());
		String fechaFinal = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString());
		int codigoViaIngreso = Integer.parseInt(campos.get("codigoViaIngreso").toString());
		String tipoPaciente = campos.get("codigoTipoPaciente").toString();
		int codigoSexo = Integer.parseInt(campos.get("codigoSexo").toString());
		String estadoFacturacion = campos.get("estadoFacturacion").toString();
		int prioridad = Utilidades.convertirAEntero(campos.get("prioridad").toString());
		
		HashMap convenios = (HashMap)campos.get("convenios");
		String listadoConvenios = "";

		for(int i=0;i<Integer.parseInt(convenios.get("numRegistros").toString());i++)
			if(UtilidadTexto.getBoolean(convenios.get("checkbox_"+i).toString()))
				listadoConvenios += (listadoConvenios.equals("")?"":",") + convenios.get("codigo_"+i);
		//****************************************************************
		
		if(codigoViaIngreso==ConstantesBD.codigoNuncaValido||codigoViaIngreso==ConstantesBD.codigoViaIngresoUrgencias||codigoViaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion)
		{
			consulta1 = "( "+
				"SELECT "+ 
				"getnombreconvenio(sc.convenio) as nombre_convenio, "+ 
				"1 as paciente "+ 
				"FROM egresos e "+ 
				"INNER JOIN cuentas c ON (c.id= e.cuenta) "+
				"INNER JOIN centros_costo cc ON (cc.codigo = c.area and cc.centro_atencion = "+codigoCentroAtencion+") "+   
				"INNER JOIN sub_cuentas sc ON(sc.ingreso = c.id_ingreso and sc.nro_prioridad = 1) "+ 
				"WHERE " +
				"to_char(e.fecha_egreso, 'YYYY-MM-DD') between  '"+fechaInicial+"' and '"+fechaFinal+"' and " +
				"e.usuario_responsable IS NOT NULL " +
				" and c.via_ingreso "+(codigoViaIngreso==ConstantesBD.codigoNuncaValido?" in ("+ConstantesBD.codigoViaIngresoUrgencias+","+ConstantesBD.codigoViaIngresoHospitalizacion+") ":" = "+codigoViaIngreso)+
				(tipoPaciente.equals("")?"":" and c.tipo_paciente = '"+tipoPaciente+"' ");
			
			//Filtro del estado de facturación
			if(!estadoFacturacion.equals(""))
				if(UtilidadTexto.getBoolean(estadoFacturacion))
					consulta1 += " and c.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaFacturada+" ";
				else
					consulta1 += " and c.estado_cuenta <> "+ConstantesBD.codigoEstadoCuentaFacturada+" ";
			consulta1 += (codigoSexo>0?" and getsexopaciente(c.codigo_paciente) = "+codigoSexo+" ":"")+
				(listadoConvenios.equals("")?"":" and sc.convenio in ("+listadoConvenios+") ")+
				")";
		}

		if(codigoViaIngreso==ConstantesBD.codigoNuncaValido||codigoViaIngreso==ConstantesBD.codigoViaIngresoAmbulatorios||codigoViaIngreso==ConstantesBD.codigoViaIngresoConsultaExterna)
		{
			consulta2 = "( "+
				"SELECT "+ 
				"getnombreconvenio(sc.convenio) as nombre_convenio, "+ 
				"1 as paciente "+ 
				"FROM cuentas c "+ 
				"INNER JOIN centros_costo cc ON (cc.codigo = c.area and cc.centro_atencion = "+codigoCentroAtencion+") "+   
				"INNER JOIN sub_cuentas sc ON(sc.ingreso = c.id_ingreso and sc.nro_prioridad = 1) "+ 
				"WHERE " +
				"to_char(c.fecha_apertura, 'YYYY-MM-DD') between  '"+fechaInicial+"' and '"+fechaFinal+"' " +
				"and c.via_ingreso "+(codigoViaIngreso==ConstantesBD.codigoNuncaValido?" in ("+ConstantesBD.codigoViaIngresoConsultaExterna+","+ConstantesBD.codigoViaIngresoAmbulatorios+") ":" = "+codigoViaIngreso)+
				(tipoPaciente.equals("")?"":" and c.tipo_paciente = '"+tipoPaciente+"' ")+" AND c.estado_cuenta NOT IN ("+ConstantesBD.codigoEstadoCuentaCerrada+","+ConstantesBD.codigoEstadoCuentaExcenta+") ";
			
			//Filtro del estado de facturación
			if(!estadoFacturacion.equals(""))
				if(UtilidadTexto.getBoolean(estadoFacturacion))
					consulta2 += " and c.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaFacturada+" ";
				else
					consulta2 += " and c.estado_cuenta <> "+ConstantesBD.codigoEstadoCuentaFacturada+" ";
			consulta2 += (codigoSexo>0?" and getsexopaciente(c.codigo_paciente) = "+codigoSexo+" ":"")+
				(listadoConvenios.equals("")?"":" and sc.convenio in ("+listadoConvenios+") ")+
				")";
		}

		if(!UtilidadTexto.isEmpty(consulta1)&&!UtilidadTexto.isEmpty(consulta2))
		{
			consulta = "SELECT t.\"nombre_convenio\",sum(t.\"paciente\") as egresos FROM " +
			"("+consulta1 + " UNION ALL " + consulta2 +") t ";
		}

		else if(!UtilidadTexto.isEmpty(consulta1)&&UtilidadTexto.isEmpty(consulta2))
		{
			consulta = "SELECT t.\"nombre_convenio\",sum(t.\"paciente\") as egresos FROM " + "("+consulta1+") t ";
		}

		else
		{
			consulta = "SELECT t.\"nombre_convenio\",sum(t.\"paciente\") as egresos FROM " + "("+consulta2+") t ";
		}

		//Filtro de prioridad en el caso de base de datos de oracle
		consulta = consulta + (tipoBD==DaoFactory.ORACLE&&prioridad>0?" WHERE rownum = "+prioridad:"") + "GROUP BY t.\"nombre_convenio\" ";
		
		if(prioridad>0)
			consulta += "ORDER BY egresos desc "+(tipoBD==DaoFactory.POSTGRESQL?" "+ValoresPorDefecto.getValorLimit1()+" "+prioridad:"");
		else
			consulta += "ORDER BY t.\"nombre_convenio\" ";
		
		logger.info("*+* Consulta obtenidad reporte egresos convenio: "+consulta);
		return consulta;
	}

	//*****************************************************************************************
	//*************CONSULTA TIPO REPORTE EGRESOS POR LUGAR DE RESIDENCIA*************************
	/**
	 * Método para obtener la consulta del reporte de egresos por lugar de residencia
	 */
	public static String obtenerConsultaReporteEgresosLugarResidencia(HashMap campos,int tipoBD)
	{
		String consulta = "", consulta1 = "", consulta2 = "";
		//**************SE TOMAN LOS PARÁMETROS*******************************************
		int codigoCentroAtencion = Integer.parseInt(campos.get("codigoCentroAtencion").toString());
		String fechaInicial = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString());
		String fechaFinal = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString());
		int codigoViaIngreso = Integer.parseInt(campos.get("codigoViaIngreso").toString());
		String tipoPaciente = campos.get("codigoTipoPaciente").toString();
		int codigoSexo = Integer.parseInt(campos.get("codigoSexo").toString());
		int prioridad = Utilidades.convertirAEntero(campos.get("prioridad").toString());
		String codigoPais = campos.get("codigoPais").toString();
		
		HashMap ciudades = (HashMap)campos.get("ciudades");
		String listadoCiudades = "";
		for(int i=0;i<Integer.parseInt(ciudades.get("numRegistros").toString());i++)
			if(UtilidadTexto.getBoolean(ciudades.get("checkbox_"+i).toString()))
			{
				String[] vector = ciudades.get("codigo_"+i).toString().split(ConstantesBD.separadorSplit);
				listadoCiudades += (listadoCiudades.equals("")?"":",") + "'" + vector[1] + "-"+ vector[2] +"'";
			}
			
		HashMap diagnosticosEgreso = (HashMap)campos.get("diagnosticosEgreso");
		String listadoDiagEgreso = "";
		for(int i=0;i<Integer.parseInt(diagnosticosEgreso.get("numRegistros").toString());i++)
			if(UtilidadTexto.getBoolean(diagnosticosEgreso.get("checkbox_"+i).toString()))
			{
				String[] vector = diagnosticosEgreso.get(i+"").toString().split(ConstantesBD.separadorSplit);
				listadoDiagEgreso += (listadoDiagEgreso.equals("")?"":",") + "'" + vector[0] + "-"+ vector[1] +"'";
			}
		//********************************************************************************
		
		
		if(codigoViaIngreso==ConstantesBD.codigoNuncaValido||codigoViaIngreso==ConstantesBD.codigoViaIngresoUrgencias||codigoViaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion)
		{
			consulta1 = "( "+
				"SELECT "+ 
				"getnombredepto(p.codigo_pais_vivienda,p.codigo_departamento_vivienda) as nombre_departamento, "+
				"getnombreciudad(p.codigo_pais_vivienda,p.codigo_departamento_vivienda,p.codigo_ciudad_vivienda) as nombre_ciudad, "+
				"1 as paciente "+ 
				"FROM egresos e "+ 
				"INNER JOIN cuentas c ON (c.id= e.cuenta) "+
				"INNER JOIN personas p ON (p.codigo= c.codigo_paciente) "+
				"INNER JOIN centros_costo cc ON (cc.codigo = c.area and cc.centro_atencion = "+codigoCentroAtencion+") "+   
				"WHERE " +
				"to_char(e.fecha_egreso, 'YYYY-MM-DD') between  '"+fechaInicial+"' and '"+fechaFinal+"' and e.usuario_responsable IS NOT NULL " +
				(listadoDiagEgreso.equals("")?"":" and e.diagnostico_principal || '-' || e.diagnostico_principal_cie IN("+listadoDiagEgreso+") ")+
				" and c.via_ingreso "+(codigoViaIngreso==ConstantesBD.codigoNuncaValido?" in ("+ConstantesBD.codigoViaIngresoUrgencias+","+ConstantesBD.codigoViaIngresoHospitalizacion+") ":" = "+codigoViaIngreso+" ")+
				(tipoPaciente.equals("")?"":" and c.tipo_paciente = '"+tipoPaciente+"' ")+
				(codigoSexo>0?" and p.sexo = "+codigoSexo+" ":"")+
				" and p.codigo_pais_vivienda = '"+codigoPais+"' "+
				(listadoCiudades.equals("")?"":" and p.codigo_departamento_vivienda || '-' || p.codigo_ciudad_vivienda IN ("+listadoCiudades+") ")+
				")";
		}
		if(codigoViaIngreso==ConstantesBD.codigoNuncaValido||codigoViaIngreso==ConstantesBD.codigoViaIngresoConsultaExterna||codigoViaIngreso==ConstantesBD.codigoViaIngresoAmbulatorios)
		{
			consulta2 = "( "+
				"SELECT "+ 
				"getnombredepto(p.codigo_pais_vivienda,p.codigo_departamento_vivienda) as nombre_departamento, "+
				"getnombreciudad(p.codigo_pais_vivienda,p.codigo_departamento_vivienda,p.codigo_ciudad_vivienda) as nombre_ciudad, "+
				"1 as paciente "+ 
				"FROM cuentas c "+ 
				"INNER JOIN personas p ON (p.codigo= c.codigo_paciente) "+ 
				"INNER JOIN centros_costo cc ON (cc.codigo = c.area and cc.centro_atencion = "+codigoCentroAtencion+") "+   
				"WHERE " +
				"to_char(c.fecha_apertura, 'YYYY-MM-DD') between  '"+fechaInicial+"' and '"+fechaFinal+"' " +
				(listadoDiagEgreso.equals("")?"":" and getDiagnosticoEgreso1(c.id,c.via_ingreso) in (" +listadoDiagEgreso+") ")+
				 " and c.via_ingreso "+(codigoViaIngreso==ConstantesBD.codigoNuncaValido?" in ("+ConstantesBD.codigoViaIngresoConsultaExterna+","+ConstantesBD.codigoViaIngresoAmbulatorios+") ":" = "+codigoViaIngreso+" ")+
				(tipoPaciente.equals("")?"":" and c.tipo_paciente = '"+tipoPaciente+"' ")+
				(codigoSexo>0?" and p.sexo = "+codigoSexo+" ":"")+
				" and p.codigo_pais_vivienda = '"+codigoPais+"' "+
				(listadoCiudades.equals("")?"":" and p.codigo_departamento_vivienda || '-' || p.codigo_ciudad_vivienda IN ("+listadoCiudades+") ")+
				" AND c.estado_cuenta NOT IN ("+ConstantesBD.codigoEstadoCuentaCerrada+","+ConstantesBD.codigoEstadoCuentaExcenta+") "+
				")";
		}
		
		consulta = "SELECT " +
						"t.\"nombre_departamento\" as nombre_departamento," +
						"t.\"nombre_ciudad\" as nombre_ciudad," +
						"sum(t.\"paciente\") as egresos " +
					"FROM ("+consulta1;
		
		if(!consulta1.equals("")&&!consulta2.equals(""))
			consulta += " UNION ALL ";
		consulta += consulta2+") t " +
			(tipoBD==DaoFactory.ORACLE&&prioridad>0?" WHERE rownum = "+prioridad+" ":"")+
			"GROUP BY t.\"nombre_departamento\", t.\"nombre_ciudad\" ";
		
		if(prioridad>0)
			consulta += "ORDER BY sum(t.\"paciente\") desc "+(tipoBD==DaoFactory.POSTGRESQL?" "+ValoresPorDefecto.getValorLimit1()+" "+prioridad:"");
		else
			consulta += "ORDER BY t.\"nombre_departamento\", t.\"nombre_ciudad\" ";
		
		logger.info("consulta egresos x lugar residencia: "+consulta);
		return consulta;
	}
	//********************************************************************************************
	//***************CONSULTAS TIPO REPORTE DIAGNÓSTICO DE EGRESO POR RANGO DE EDAD**********************
	/**
	 * Método para obtener los diagnosticos prioritarios del tipo de reporte 
	 * de diagnóstico de egreso por rango de edad
	 */
	public static ArrayList<HashMap<String,Object>> obtenerDiagnosticosPrioridadEgresos(Connection con,HashMap campos,int tipoBD)
	{
		ArrayList<HashMap<String, Object>> resultados = new ArrayList<HashMap<String,Object>>();
		try
		{
			//**********SE TOMAN LOS PARÁMETROS DE BUSQUEDA**************************************
			int codigoCentroAtencion = Integer.parseInt(campos.get("codigoCentroAtencion").toString());
			String fechaInicial = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString());
			String fechaFinal = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString());
			int codigoViaIngreso = Integer.parseInt(campos.get("codigoViaIngreso").toString());
			String codigoTipoPaciente = campos.get("codigoTipoPaciente").toString();
			int prioridad = Integer.parseInt(campos.get("prioridad").toString());
			String estadoFacturacion = campos.get("estadoFacturacion").toString();
			int codigoSexo = Integer.parseInt(campos.get("codigoSexo").toString());
			//Se toma el listado de convenios si existe
			HashMap convenios = (HashMap) campos.get("convenios");
			String listadoConvenios = "";
			for(int i=0;i<Integer.parseInt(convenios.get("numRegistros")+"");i++)
				if(UtilidadTexto.getBoolean(convenios.get("checkbox_"+i).toString()))
					listadoConvenios += (listadoConvenios.equals("")?"":",") + convenios.get("codigo_"+i);
			//Se toma el listado de centros de costo
			HashMap centrosCosto = (HashMap) campos.get("centrosCosto");
			String listadoCentrosCosto = "";
			for(int i=0;i<Integer.parseInt(centrosCosto.get("numRegistros")+"");i++)
				if(UtilidadTexto.getBoolean(centrosCosto.get("checkbox_"+i).toString()))
					listadoCentrosCosto += (listadoCentrosCosto.equals("")?"":",") + centrosCosto.get("codigo_"+i);
			//************************************************************************************
			
			String consulta = "SELECT "+ 
				"e.diagnostico_principal || '-' || e.diagnostico_principal_cie as codigo_diagnostico, "+
				"getnombrediagnostico(e.diagnostico_principal,e.diagnostico_principal_cie) as nombre_diagnostico, "+
				"count(1) as egreso "+ 
				"FROM egresos e "+ 
				"INNER JOIN cuentas c ON(c.id = e.cuenta) " + 
				"INNER JOIN centros_costo cc ON(cc.codigo = c.area AND cc.centro_atencion = "+codigoCentroAtencion+") ";
			
			if(!listadoConvenios.equals(""))
				consulta += " INNER JOIN sub_cuentas sc ON(sc.ingreso = c.id_ingreso AND sc.nro_prioridad = 1)  ";
			
			consulta += "INNER JOIN personas p ON(p.codigo = c.codigo_paciente) "+ 
				"WHERE " +
				"e.fecha_egreso BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and " +
				"e.usuario_responsable IS NOT NULL and " +
				"c.via_ingreso = "+codigoViaIngreso+" "+
				(codigoTipoPaciente.equals("")?"":" and c.tipo_paciente = '"+codigoTipoPaciente+"' ");
			
			//Filtro del estado de facturación
			if(!estadoFacturacion.equals(""))
				if(UtilidadTexto.getBoolean(estadoFacturacion))
					consulta += " and c.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaFacturada+" ";
				else
					consulta += " and c.estado_cuenta <> "+ConstantesBD.codigoEstadoCuentaFacturada+" ";
			
			//Filtro por centro de costo
			consulta += (listadoCentrosCosto.equals("")?"":" and c.area in ("+listadoCentrosCosto+") ") ;
			
			//Filtro por convenio
			consulta += (listadoConvenios.equals("")?"":" and sc.convenio in ("+listadoConvenios+") ");
			
			//Filtro por sexo
			consulta += (codigoSexo>0?" and p.sexo = "+codigoSexo+" ":"");
			
			
			if(tipoBD==DaoFactory.ORACLE)
				consulta += " and rownum = "+prioridad+" ";
				
			consulta += " group by e.diagnostico_principal,e.diagnostico_principal_cie "+ 
				"order by egreso desc "+(tipoBD==DaoFactory.POSTGRESQL?" "+ValoresPorDefecto.getValorLimit1()+" "+prioridad+" ":"");
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			while(rs.next())
			{
				HashMap<String,Object> elemento = new HashMap<String, Object>();
				elemento.put("codigo", rs.getString("codigo_diagnostico"));
				elemento.put("nombre",rs.getString("nombre_diagnostico"));
				resultados.add(elemento);
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerDiagnosticosPrioridadEgresos: "+e);
		}
		return resultados;
	}
	
	/**
	 * Método para obtener los datos del tipo reporte Dx de egreso por rango de edad
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap<String,Object> obtenerDatosReporteDxEgresoRangoEdad(Connection con,HashMap campos)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try
		{
			//**************	SE TOMAN LOS PARÁMETROS*******************************
			int codigoCentroAtencion = Integer.parseInt(campos.get("codigoCentroAtencion").toString());
			int codigoInstitucion = Integer.parseInt(campos.get("codigoInstitucion").toString());
			String fechaInicial = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString());
			String fechaFinal = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString());
			int codigoViaIngreso = Integer.parseInt(campos.get("codigoViaIngreso").toString());
			String codigoTipoPaciente = campos.get("codigoTipoPaciente").toString();
			String estadoFacturacion = campos.get("estadoFacturacion").toString();
			int codigoSexo = Integer.parseInt(campos.get("codigoSexo").toString());
			//Se toma el listado de convenios si existe
			HashMap convenios = (HashMap) campos.get("convenios");
			String listadoConvenios = "";
			for(int i=0;i<Integer.parseInt(convenios.get("numRegistros")+"");i++)
				if(UtilidadTexto.getBoolean(convenios.get("checkbox_"+i).toString()))
					listadoConvenios += (listadoConvenios.equals("")?"":",") + convenios.get("codigo_"+i);
			//Se toma el listado de centros de costo
			HashMap centrosCosto = (HashMap) campos.get("centrosCosto");
			String listadoCentrosCosto = "";
			for(int i=0;i<Integer.parseInt(centrosCosto.get("numRegistros")+"");i++)
				if(UtilidadTexto.getBoolean(centrosCosto.get("checkbox_"+i).toString()))
					listadoCentrosCosto += (listadoCentrosCosto.equals("")?"":",") + centrosCosto.get("codigo_"+i);
			//*************************************************************************
			
			
			String consulta = "SELECT re.codigo as codigo_reporte,re.nombre_etiqueta,re.orden,sum(t.egreso) as egreso,sum(t.estancia) as estancia,t.codigo_diagnostico,t.nombre_diagnostico FROM "+
				"( "+
					"SELECT "+ 
					"e.diagnostico_principal || '-' || e.diagnostico_principal_cie as codigo_diagnostico, "+
					"getnombrediagnostico(e.diagnostico_principal,e.diagnostico_principal_cie) as nombre_diagnostico, "+
					"e.fecha_egreso - p.fecha_nacimiento AS edad, "+
					"1 as egreso, ";
			if(codigoViaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion)
				consulta += "gettiempoestancia(ah.fecha_admision,substr(ah.hora_admision,0,6),e.fecha_egreso,substr(e.hora_egreso,0,6),c.via_ingreso) as estancia ";
			else
				consulta += "CASE WHEN au.cama_observacion IS NULL THEN 0 ELSE gettiempoestancia(au.fecha_admision,substr(au.hora_admision,0,6),e.fecha_egreso,substr(e.hora_egreso,0,6),c.via_ingreso) END as estancia ";
			consulta += "" +
					"FROM egresos e "+ 
					"INNER JOIN cuentas c ON(c.id = e.cuenta) ";
			
			if(codigoViaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion)
				consulta += " INNER JOIN admisiones_hospi ah ON(ah.cuenta = c.id) ";
			else
				consulta += " INNER JOIN admisiones_urgencias au ON(au.cuenta = c.id) ";
			
			consulta +=	"INNER JOIN centros_costo cc ON(cc.codigo = c.area AND cc.centro_atencion = "+codigoCentroAtencion+") ";
			
			if(!listadoConvenios.equals(""))
				consulta += " INNER JOIN sub_cuentas sc ON(sc.ingreso = c.id_ingreso AND sc.nro_prioridad = 1)  ";
			
			consulta += "INNER JOIN personas p ON(p.codigo = c.codigo_paciente) "+ 
				"WHERE " +
				"e.fecha_egreso BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and " +
				"e.usuario_responsable IS NOT NULL and " +
				"c.via_ingreso = "+codigoViaIngreso+" "+
				(codigoTipoPaciente.equals("")?"":" and c.tipo_paciente = '"+codigoTipoPaciente+"' ");
			
			//Filtro del estado de facturación
			if(!estadoFacturacion.equals(""))
				if(UtilidadTexto.getBoolean(estadoFacturacion))
					consulta += " and c.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaFacturada+" ";
				else
					consulta += " and c.estado_cuenta <> "+ConstantesBD.codigoEstadoCuentaFacturada+" ";
			
			//Filtro por centro de costo
			consulta += (listadoCentrosCosto.equals("")?"":" and c.area in ("+listadoCentrosCosto+") ") ;
			
			//Filtro por convenio
			consulta += (listadoConvenios.equals("")?"":" and sc.convenio in ("+listadoConvenios+") ");
			
			//Filtro por sexo
			consulta += (codigoSexo>0?" and p.sexo = "+codigoSexo+" ":"");
			
			consulta += ") t "+ 
				"INNER JOIN rangos_estadisticos re ON(re.rango_inicial <= t.edad AND re.rango_final >= t.edad and re.institucion = "+codigoInstitucion+" and re.activo = '"+ConstantesBD.acronimoSi+"' and re.reporte = "+ConstantesBDManejoPaciente.tipoReporteDiagnosticosEgresosRangoEdad+") "+ 
				"GROUP BY re.codigo,re.nombre_etiqueta,re.orden,t.codigo_diagnostico,t.nombre_diagnostico "+ 
				"order by re.orden";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)), true, true);
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerDatosReporteDxEgresoRangoEdad: "+e);
		}
		return resultados;
	}
	//***************************************************************************************************
	//***************CONSULTAS TIPO REPORTE TOTAL DIAGNÓSTICOS EGRESO***********************************
	/**
	 * Método para obtener la consulta del tipo de reporte de total diagnósticos de egreso
	 */
	public static String obtenerConsultaReporteTotalDxEgreso(HashMap campos)
	{
		String consulta = "";
		
		//**************SE TOMAN LOS PARÁMETROS********************************************
		int codigoCentroAtencion = Integer.parseInt(campos.get("codigoCentroAtencion").toString());
		String fechaInicial = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString());
		String fechaFinal = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString());
		int codigoViaIngreso = Utilidades.convertirAEntero(campos.get("codigoViaIngreso").toString());
		String codigoTipoPaciente = campos.get("codigoTipoPaciente").toString();
		String estadoFacturacion = campos.get("estadoFacturacion").toString();
		int codigoSexo = Utilidades.convertirAEntero(campos.get("codigoSexo").toString());	
		int codigoCentroCosto = Utilidades.convertirAEntero(campos.get("codigoCentroCosto").toString());
		
		HashMap diagnosticosEgreso = (HashMap)campos.get("diagnosticosEgreso");
		String listadoDiagEgreso = "";
		for(int i=0;i<Integer.parseInt(diagnosticosEgreso.get("numRegistros").toString());i++)
			if(UtilidadTexto.getBoolean(diagnosticosEgreso.get("checkbox_"+i).toString()))
			{
				String[] vector = diagnosticosEgreso.get(i+"").toString().split(ConstantesBD.separadorSplit);
				listadoDiagEgreso += (listadoDiagEgreso.equals("")?"":",") + "'" + vector[0] + "'";
			}
		//************************************************************************************
		
		consulta = "SELECT "+
			"e.diagnostico_principal || '-' || e.diagnostico_principal_cie AS codigo_diagnostico, "+
			"getnombrediagnostico(e.diagnostico_principal,e.diagnostico_principal_cie) as nombre_diagnostico, "+ 
			"count(1) as total "+ 
			"FROM egresos e "+ 
			"INNER JOIN cuentas c ON(c.id = e.cuenta) "+ 
			"INNER JOIN centros_costo cc ON(cc.codigo = c.area AND cc.centro_atencion = "+codigoCentroAtencion+") "+ 
			"INNER JOIN personas p ON(p.codigo = c.codigo_paciente) "+ 
			"WHERE to_char(e.fecha_egreso, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.usuario_responsable IS NOT NULL "+
			(!listadoDiagEgreso.equals("")?" and e.diagnostico_principal in ("+listadoDiagEgreso+") ":"")+
			(codigoViaIngreso!=ConstantesBD.codigoNuncaValido?" and c.via_ingreso = "+codigoViaIngreso+" ":"")+
			(!codigoTipoPaciente.equals("")?" and c.tipo_paciente = '"+codigoTipoPaciente+"' ":"")+
			(codigoCentroCosto!=ConstantesBD.codigoNuncaValido?" and c.area = "+codigoCentroCosto+" ":"");
		
		//Filtro del estado de facturación
		if(!estadoFacturacion.equals(""))
			if(UtilidadTexto.getBoolean(estadoFacturacion))
				consulta += " and c.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaFacturada+" ";
			else
				consulta += " and c.estado_cuenta <> "+ConstantesBD.codigoEstadoCuentaFacturada+" ";
		
		consulta += (codigoSexo!=ConstantesBD.codigoNuncaValido?" and p.sexo = "+codigoSexo+" ":"");
		
		consulta += "GROUP BY e.diagnostico_principal,e.diagnostico_principal_cie "+ 
			"ORDER BY count(1) desc ";
		
		return consulta;
	}
	//**************************************************************************************************
	//******************CONSULTA TIPO REPORTE PRIMERAS 'N' CAUSAS DE MORBILIDAD*************************
	/**
	 * Método para obtener la consulta del reporte de la primeras N causas de morbilidad
	 */
	public static String obtenerConsultaReporteNPrimerasCausasMorbilidad(HashMap campos,int tipoBD)
	{
		String consulta = "", subconsultas = "";
		
		//**********************PARAMETROS QUE SE TOMAN***************************************
		int codigoCentroAtencion = Integer.parseInt(campos.get("codigoCentroAtencion").toString());
		String fechaInicial = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString());
		String fechaFinal = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString());
		int codigoViaIngreso = Utilidades.convertirAEntero(campos.get("codigoViaIngreso").toString());
		String codigoTipoPaciente = campos.get("codigoTipoPaciente").toString();
		String estadoSalida = campos.get("estadoSalida").toString();
		int codigoSexo = Utilidades.convertirAEntero(campos.get("codigoSexo").toString());
		int prioridad = Utilidades.convertirAEntero(campos.get("prioridad").toString());
		//************************************************************************************
		
		
		if(codigoViaIngreso==ConstantesBD.codigoNuncaValido || codigoViaIngreso == ConstantesBD.codigoViaIngresoUrgencias || codigoViaIngreso == ConstantesBD.codigoViaIngresoHospitalizacion)
		{
			//Sub consulta para la morbilidad de Urgencias y/o Hospitalizacion
			/**
			 * Nota * Solo sirve cuando el estado de salida es diferente de muerto o no se seleccionó estado salida
			 */
			if(estadoSalida.equals("")||UtilidadTexto.getBoolean(estadoSalida))
			{
				
				subconsultas = "(" +
					"SELECT "+
					"e.diagnostico_principal || '-' || e.diagnostico_principal_cie AS codigo_diagnostico, "+ 
					"getnombrediagnostico(e.diagnostico_principal,e.diagnostico_principal_cie) as nombre_diagnostico, "+ 
					"c.via_ingreso as codigo_via_ingreso, "+ 
					"'Morbilidad ' || getnombreviaingreso(c.via_ingreso) as nombre_via_ingreso, "+ 
					"count(1) as egreso "+ 
					"FROM egresos e "+ 
					"INNER JOIN cuentas c ON(c.id = e.cuenta) "+ 
					"INNER JOIN centros_costo cc ON(cc.codigo = c.area AND cc.centro_atencion = "+codigoCentroAtencion+") "+ 
					"INNER JOIN personas p ON(p.codigo = c.codigo_paciente) "+ 
					"WHERE "+ 
					"to_char(e.fecha_egreso, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.usuario_responsable IS NOT NULL ";
				
				//Filtro del estado de salida
				if(!estadoSalida.equals(""))
				{
					if(UtilidadTexto.getBoolean(estadoSalida))
						subconsultas += " AND e.estado_salida = "+ValoresPorDefecto.getValorFalseParaConsultas()+" ";
					else
						subconsultas += " AND e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+" ";
				}
				
				if(codigoViaIngreso!=ConstantesBD.codigoNuncaValido)
					subconsultas += " AND c.via_ingreso = "+codigoViaIngreso+" ";
				if(!codigoTipoPaciente.equals(""))
					subconsultas += " AND c.tipo_paciente = '"+codigoTipoPaciente+"' ";
				
				if(codigoSexo!=ConstantesBD.codigoNuncaValido)
					subconsultas += " AND p.sexo = "+codigoSexo+" ";
				
				subconsultas += " GROUP BY e.diagnostico_principal,e.diagnostico_principal_cie,c.via_ingreso )";
			}
			
			//Sub consulta para la mortalidad de Urgencias y/o Hospitalizacion
			/**
			 * Nota * Solo sirve cuando no se parametrizó estado salida o el estado salida es Muerto
			 */
			if((!estadoSalida.equals("")&&!UtilidadTexto.getBoolean(estadoSalida)) || estadoSalida.equals(""))
			{
				if(!subconsultas.equals(""))
					subconsultas +=  " UNION ";
				
				subconsultas += "(" +
					"SELECT "+
					"e.diagnostico_principal || '-' || e.diagnostico_principal_cie AS codigo_diagnostico, "+ 
					"getnombrediagnostico(e.diagnostico_principal,e.diagnostico_principal_cie) as nombre_diagnostico, "+ 
					"c.via_ingreso as codigo_via_ingreso, "+ 
					"'Mortalidad ' || getnombreviaingreso(c.via_ingreso) as nombre_via_ingreso, "+ 
					"count(1) as egreso "+ 
					"FROM egresos e "+ 
					"INNER JOIN cuentas c ON(c.id = e.cuenta) "+ 
					"INNER JOIN centros_costo cc ON(cc.codigo = c.area AND cc.centro_atencion = "+codigoCentroAtencion+") "+ 
					"INNER JOIN personas p ON(p.codigo = c.codigo_paciente) "+ 
					"WHERE "+ 
					"to_char(e.fecha_egreso, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.usuario_responsable IS NOT NULl and e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+" ";
				
				if(codigoViaIngreso!=ConstantesBD.codigoNuncaValido)
					subconsultas += " AND c.via_ingreso = "+codigoViaIngreso+" ";
				if(!codigoTipoPaciente.equals(""))
					subconsultas += " AND c.tipo_paciente = '"+codigoTipoPaciente+"' ";
				
				if(codigoSexo!=ConstantesBD.codigoNuncaValido)
					subconsultas += " AND p.sexo = "+codigoSexo+" ";
				
				subconsultas += "GROUP BY e.diagnostico_principal,e.diagnostico_principal_cie,c.via_ingreso ) ";
			}
		}
		
		if(
				(codigoViaIngreso == ConstantesBD.codigoNuncaValido || codigoViaIngreso == ConstantesBD.codigoViaIngresoAmbulatorios || codigoViaIngreso == ConstantesBD.codigoViaIngresoConsultaExterna)
				&&
				(estadoSalida.equals("")||UtilidadTexto.getBoolean(estadoSalida))
			)
		{
			//Subconsulta para la morbilidad de Consulta Externa y/o Ambulatorios
			if(!subconsultas.equals(""))
				subconsultas += " UNION ";
			
			subconsultas += "( "+
				"SELECT "+ 
				"temp.\"codigo_diagnostico\" as codigo_diagnostico, "+ 
				"getnombrediagnostico(temp.\"codigo_diagnostico\",null) as nombre_diagnostico, "+
				"temp.\"codigo_via_ingreso\" as codigo_via_ingreso, "+ 
				"temp.\"nombre_via_ingreso\" as nombre_via_ingreso, "+ 
				"sum(temp.\"egreso\") as egreso "+ 
				"FROM "+ 
				"( "+
					"SELECT "+ 
					"getdiagnosticoegreso1(c.id,c.via_ingreso) as codigo_diagnostico, "+
					"c.via_ingreso as codigo_via_ingreso, "+
					"'Morbilidad ' || getnombreviaingreso(c.via_ingreso) as nombre_via_ingreso, "+ 
					"1 as egreso "+ 
					"FROM cuentas c "+ 
					"INNER JOIN centros_costo cc ON(cc.codigo = c.area and cc.centro_atencion = "+codigoCentroAtencion+") "+ 
					"INNER JOIN personas p ON(p.codigo = c.codigo_paciente) "+ 
					"WHERE ";
			
			if(codigoViaIngreso==ConstantesBD.codigoNuncaValido)
				subconsultas += " c.via_ingreso in ("+ConstantesBD.codigoViaIngresoConsultaExterna+","+ConstantesBD.codigoViaIngresoAmbulatorios+") ";
			else
				subconsultas += " c.via_ingreso = "+codigoViaIngreso+" ";
			
			if(!codigoTipoPaciente.equals(""))
				subconsultas += " and c.tipo_paciente = '"+codigoTipoPaciente+"' ";
			
			subconsultas += " and c.estado_cuenta <> "+ConstantesBD.codigoEstadoCuentaCerrada+" and to_char(c.fecha_apertura, 'YYYY-MM-DD') between '"+fechaInicial+"' and '"+fechaFinal+"' ";
			
			if(codigoSexo!=ConstantesBD.codigoNuncaValido)
				subconsultas += " AND p.sexo = "+codigoSexo+" ";
			
			subconsultas += 
				") "+
				"temp " +
				"WHERE temp.\"codigo_diagnostico\" " ;
			if(tipoBD==DaoFactory.POSTGRESQL){
				subconsultas +=" <> '' ";  
			}else{
				if(tipoBD==DaoFactory.ORACLE){
					subconsultas +=" is not null ";
				}
			}
				
		subconsultas +="GROUP BY temp.\"codigo_diagnostico\",temp.\"codigo_via_ingreso\",temp.\"nombre_via_ingreso\" "+  
			")";
		}
		
		consulta = "SELECT " +
						"t.\"codigo_diagnostico\" as codigo_diagnostico," +
						"t.\"nombre_diagnostico\" as nombre_diagnostico," +
						"t.\"codigo_via_ingreso\" as codigo_via_ingreso," +
						"t.\"nombre_via_ingreso\" as nombre_via_ingreso," +
						"t.\"egreso\" as egreso " +
					"FROM "+ 
						"(" + subconsultas + ") t ";
		
		consulta += "ORDER BY t.\"egreso\" DESC "+(tipoBD==DaoFactory.POSTGRESQL&&prioridad!=ConstantesBD.codigoNuncaValido?(" "+ValoresPorDefecto.getValorLimit1()+" "+prioridad):"");

		if(tipoBD==DaoFactory.ORACLE&&prioridad!=ConstantesBD.codigoNuncaValido)
			consulta = "SELECT * FROM ("+consulta+") WHERE rownum <= "+prioridad+" ";
		
		return consulta;
	}
	//***************************************************************************************************
	//***************CONSULTA TIPO REPORTE ESTANCIA PROMEDIO MENSUAL DE PACIENTES EGRESADOS POR RANGO***************************
	/**
	 * Método para retornar la consulta del reporte de estancia promedio mensual pacienrtes egresados por rango
	 */
	public static String obtenerConsultaReporteEstanciaPromedioMensualPacientesEgresadosRango(HashMap campos)
	{
		String consulta = "";
		//******************SE TOMAN LOS PARÁMETROS**********************************
		int codigoCentroAtencion = Integer.parseInt(campos.get("codigoCentroAtencion").toString());
		int codigoInstitucion = Integer.parseInt(campos.get("codigoInstitucion").toString());
		String fechaInicial = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString());
		String fechaFinal = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString());
		int codigoViaIngreso = Utilidades.convertirAEntero(campos.get("codigoViaIngreso").toString());
		String codigoTipoPaciente = campos.get("codigoTipoPaciente").toString();
		int codigoSexo = Utilidades.convertirAEntero(campos.get("codigoSexo").toString());
		String estadoFacturacion = campos.get("estadoFacturacion").toString();
		
		HashMap diagnosticosEgreso = (HashMap)campos.get("diagnosticosEgreso");
		String listadoDiagEgreso = "";
		for(int i=0;i<Integer.parseInt(diagnosticosEgreso.get("numRegistros").toString());i++)
			if(UtilidadTexto.getBoolean(diagnosticosEgreso.get("checkbox_"+i).toString()))
			{
				String[] vector = diagnosticosEgreso.get(i+"").toString().split(ConstantesBD.separadorSplit);
				listadoDiagEgreso += (listadoDiagEgreso.equals("")?"":",") + "'" + vector[0] + "'";
			}
		//*****************************************************************************
		
		consulta = "SELECT " +
						"t.\"edad\" as edad," +
						"t.\"mes_egreso\" as mes_egreso," +
						"t.\"mes\" as mes," +
						"re.nombre_etiqueta as nombre_etiqueta," +
						"re.orden as orden," +
						"t.\"egreso\" as egreso," +
						"t.\"estancia\" as estancia FROM "+
			"( " +
			"SELECT "+ 
			"e.fecha_egreso - p.fecha_nacimiento AS edad, "+
			"to_char(e.fecha_egreso,'YYYY-MM') as mes_egreso, "+
			"getnombremes(to_char(e.fecha_egreso,'MM')) || ' ' || to_char(e.fecha_egreso,'YYYY') AS mes, "+
			"gettiempoestancia(ad.fecha_admision,substr(ad.hora_admision,0,6),e.fecha_egreso,substr(e.hora_egreso,0,6),c.via_ingreso) as estancia, "+
			"1 as egreso "+  
			"FROM egresos e "+ 
			"INNER JOIN cuentas c ON(c.id = e.cuenta) ";
		
		if(codigoViaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion)
			consulta += "INNER JOIN admisiones_hospi ad ON(ad.cuenta = c.id) ";
		else if(codigoViaIngreso==ConstantesBD.codigoViaIngresoUrgencias)
			consulta += "INNER JOIN admisiones_urgencias ad ON(ad.cuenta = c.id) ";
		
		consulta +=	"" +
			"INNER JOIN centros_costo cc ON(cc.codigo = c.area AND cc.centro_atencion = "+codigoCentroAtencion+") "+ 
			"INNER JOIN personas p ON(p.codigo = c.codigo_paciente) "+ 
			"WHERE "+ 
			"to_char(e.fecha_egreso, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.usuario_responsable IS NOT NULL "+
			(!listadoDiagEgreso.equals("")?" and e.diagnostico_principal in ("+listadoDiagEgreso+") ":"")+
			"and c.via_ingreso = "+codigoViaIngreso+" "+ 
			//Si es urgencias se debe verificar que la conducta a seguri sea Cama de Observación
			(codigoViaIngreso==ConstantesBD.codigoViaIngresoUrgencias?" and c.id in (SELECT s.cuenta from solicitudes s inner join valoraciones_urgencias vu on(vu.numero_solicitud=s.numero_solicitud) WHERE s.cuenta = c.id and vu.codigo_conducta_valoracion = "+ConstantesBD.codigoConductaSeguirCamaObservacion+")  ":"")+
			(!codigoTipoPaciente.equals("")?" and c.tipo_paciente = '"+codigoTipoPaciente+"' ":"");
		
		///Filtro del estado de facturación
		if(!estadoFacturacion.equals(""))
			if(UtilidadTexto.getBoolean(estadoFacturacion))
				consulta += " and c.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaFacturada+" ";
			else
				consulta += " and c.estado_cuenta <> "+ConstantesBD.codigoEstadoCuentaFacturada+" ";
		
			consulta += (codigoSexo!=ConstantesBD.codigoNuncaValido?" and p.sexo = "+codigoSexo+" ":"");
		
		consulta +=	") "+
			"t "+
			"INNER JOIN rangos_estadisticos re ON(re.rango_inicial <= t.\"edad\" AND re.rango_final >= t.\"edad\" and re.institucion = "+codigoInstitucion+" and re.activo = '"+ConstantesBD.acronimoSi+"' and re.reporte = "+ConstantesBDManejoPaciente.tipoReporteEstanciaPromMensualPacEgresadosRan+") "+ 
			"order by re.orden";
		
		logger.info("CONSULTA REPORTE=>"+consulta); 
		return consulta;
	}
	//****************************************************************************************************************************
	//***************CONSULTA TIPO REPORTE PACIENTES EGRESADOS DE PEDIATRÍA POR RANGO DE EDAD***************************************
	/**
	 * Método que arma la consulta para el reporte de pacientes egresados de pediatría por rango de edad
	 */
	public static String obtenerConsultaReportePacientesEgresadosPediatriaRangoEdad(HashMap campos)
	{
		String consulta = "";
		///******************SE TOMAN LOS PARÁMETROS**********************************
		int codigoCentroAtencion = Integer.parseInt(campos.get("codigoCentroAtencion").toString());
		int codigoInstitucion = Integer.parseInt(campos.get("codigoInstitucion").toString());
		String fechaInicial = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString());
		String fechaFinal = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString());
		int codigoViaIngreso = Utilidades.convertirAEntero(campos.get("codigoViaIngreso").toString());
		String codigoTipoPaciente = campos.get("codigoTipoPaciente").toString();
		int codigoSexo = Utilidades.convertirAEntero(campos.get("codigoSexo").toString());
		String estadoFacturacion = campos.get("estadoFacturacion").toString();
		
		HashMap diagnosticosEgreso = (HashMap)campos.get("diagnosticosEgreso");
		String listadoDiagEgreso = "";
		for(int i=0;i<Integer.parseInt(diagnosticosEgreso.get("numRegistros").toString());i++)
			if(UtilidadTexto.getBoolean(diagnosticosEgreso.get("checkbox_"+i).toString()))
			{
				String[] vector = diagnosticosEgreso.get(i+"").toString().split(ConstantesBD.separadorSplit);
				listadoDiagEgreso += (listadoDiagEgreso.equals("")?"":",") + "'" + vector[0] + "'";
			}
		//*****************************************************************************
		
		consulta = "SELECT " +
						"t.\"edad\" as edad," +
						"t.\"mes_egreso\" as mes_egreso," +
						"t.\"mes\" as mes," +
						"re.nombre_etiqueta as nombre_etiqueta," +
						"re.orden as orden," +
						"t.\"egreso\" as egreso," +
						"t.\"estancia\" as estancia " +
					"FROM "+
			"( "+
				"SELECT "+ 
				"e.fecha_egreso - p.fecha_nacimiento AS edad, "+
				"to_char(e.fecha_egreso,'YYYY-MM') as mes_egreso, "+
				"getnombremes(to_char(e.fecha_egreso,'MM')) || ' ' || to_char(e.fecha_egreso,'YYYY') AS mes, "+
				"gettiempoestancia(ad.fecha_admision,substr(ad.hora_admision,0,6),e.fecha_egreso,substr(e.hora_egreso,0,6),c.via_ingreso) as estancia, "+
				"1 as egreso "+  
				"FROM egresos e "+ 
				"INNER JOIN cuentas c ON(c.id = e.cuenta) ";
		
		if(codigoViaIngreso==ConstantesBD.codigoViaIngresoUrgencias)
			consulta += "INNER JOIN admisiones_urgencias ad ON(ad.cuenta = c.id) ";
		else if(codigoViaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion)
			consulta += "INNER JOIN admisiones_hospi ad ON(ad.cuenta = c.id) ";
		
		consulta +=	"" +
				"INNER JOIN centros_costo cc ON(cc.codigo = c.area AND cc.centro_atencion = "+codigoCentroAtencion+") "+ 
				"INNER JOIN personas p ON(p.codigo = c.codigo_paciente) "+ 
				"WHERE "+ 
				"to_char(e.fecha_egreso, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.usuario_responsable IS NOT NULL "+
				(!listadoDiagEgreso.equals("")?" and e.diagnostico_principal in ("+listadoDiagEgreso+") ":"")+
				"and c.via_ingreso = "+codigoViaIngreso+" "+ 
				//Si es urgencias se debe verificar que la conducta a seguri sea Cama de Observación
				(codigoViaIngreso==ConstantesBD.codigoViaIngresoUrgencias?" and c.id in (SELECT s.cuenta from solicitudes s inner join valoraciones_urgencias vu on(vu.numero_solicitud=s.numero_solicitud) WHERE s.cuenta = c.id and vu.codigo_conducta_valoracion = "+ConstantesBD.codigoConductaSeguirCamaObservacion+")  ":"")+
				(!codigoTipoPaciente.equals("")?" and c.tipo_paciente = '"+codigoTipoPaciente+"' ":"");
			
			///Filtro del estado de facturación
			if(!estadoFacturacion.equals(""))
				if(UtilidadTexto.getBoolean(estadoFacturacion))
					consulta += " and c.estado_cuenta = "+ConstantesBD.codigoEstadoCuentaFacturada+" ";
				else
					consulta += " and c.estado_cuenta <> "+ConstantesBD.codigoEstadoCuentaFacturada+" ";
			
				consulta += (codigoSexo!=ConstantesBD.codigoNuncaValido?" and p.sexo = "+codigoSexo+" ":"");
		
		consulta += ") t "+
			"INNER JOIN rangos_estadisticos re ON(re.rango_inicial <= t.\"edad\" AND re.rango_final >= t.\"edad\" and re.institucion = "+codigoInstitucion+" and re.activo = '"+ConstantesBD.acronimoSi+"' and re.reporte = "+ConstantesBDManejoPaciente.tipoReportePacEgresadosPediatriaRangoEdad+") "+ 
			"order by re.orden";
		
		
		logger.info("CONSULTA REPORTE=>"+consulta); 
		return consulta;
		
	}
	//*****************************************************************************************************************************
	//************CONSULTA TIPO REPORTE ESTANCIA PACIENTES MAYOR DE N DIAS**********************************************************
	/**
	 * Método para obtener la consulta del reporte de estancia de pacientes mayores de N días
	 */
	public static String obtenerConsultaReporteEstanciaPacientesMayorNDias(HashMap campos)
	{
		String consulta = "";
		
		//******************SE TOMAN LOS PARÁMETROS**********************************
		int codigoCentroAtencion = Integer.parseInt(campos.get("codigoCentroAtencion").toString());
		String fechaInicial = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString());
		String fechaFinal = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString());
		int codigoViaIngreso = Utilidades.convertirAEntero(campos.get("codigoViaIngreso").toString());
		String codigoTipoPaciente = campos.get("codigoTipoPaciente").toString();
		int codigoSexo = Utilidades.convertirAEntero(campos.get("codigoSexo").toString());
		String tipoEgreso = campos.get("tipoEgreso").toString();
		int prioridad = Utilidades.convertirAEntero(campos.get("prioridad").toString());
		String estadoSalida = campos.get("estadoSalida").toString();
		
		//Se toma el listado de convenios si existe
		HashMap convenios = (HashMap) campos.get("convenios");
		String listadoConvenios = "";
		for(int i=0;i<Integer.parseInt(convenios.get("numRegistros")+"");i++)
			if(UtilidadTexto.getBoolean(convenios.get("checkbox_"+i).toString()))
				listadoConvenios += (listadoConvenios.equals("")?"":",") + convenios.get("codigo_"+i);
		//*****************************************************************************
		
		consulta = "SELECT " +
						"t.\"numero_ingreso\" as numero_ingreso, " +
						"t.\"numero_identificacion\" as numero_identificacion, " +
						"t.\"numero_historia_clinica\" as numero_historia_clinica," +
						"t.\"paciente\" as paciente," +
						"t.\"sexo\" as sexo," +
						"t.\"edad\" as edad," +
						"t.\"fecha_ingreso\" as fecha_ingreso," +
						"t.\"fecha_egreso\" as fecha_egreso," +
						"t.\"estancia\" as estancia," +
						"t.\"codigo_diagnostico\" as codigo_diagnostico," +
						"t.\"nombre_diagnostico\" as nombre_diagnostico," +
						"t.\"diagnosticos_relacionados\" as diagnosticos_relacionados, " +
						"t.\"convenio\" as convenio, " +
						"t.\"estancia_filtro\" as  estancia_filtro "+
			"FROM ( "+
				"SELECT "+ 
				"getconsecutivoingreso(c.id_ingreso) as numero_ingreso, "+
				"p.numero_identificacion as numero_identificacion, "+
				"getnumhistocli(p.codigo) as numero_historia_clinica, "+ 
				"p.primer_nombre || coalesce(' '||p.segundo_nombre,'') || ' ' || p.primer_apellido || coalesce(' '||p.segundo_apellido,'') as paciente, "+
				"getdescripcionsexo(p.sexo) as sexo, "+
				"to_char(ad.fecha_admision,'"+ConstantesBD.formatoFechaAp+"') as fecha_ingreso, ";
		
		//Si no se seleccionó tipo egreso es posible calcular la edad hasta la fecha egreso o hasta la fecha actual
		if(tipoEgreso.equals(""))
			consulta += "getedad2(p.fecha_nacimiento,coalesce(e.fecha_egreso,current_date)) as edad, " +
						//es posible que haya fecha egreso
						"coalesce(to_char(e.fecha_egreso,'"+ConstantesBD.formatoFechaAp+"'),'') as fecha_egreso, "+
						//Es posible que haya tiempo estancia por egreso
						"gettiempoestancia(ad.fecha_admision,substr(ad.hora_admision,0,6),coalesce(e.fecha_egreso,current_date),coalesce(e.hora_egreso,"+ValoresPorDefecto.getSentenciaHoraActualBD()+"),c.via_ingreso) as estancia, "+
						"gettiempoestancia(ad.fecha_admision,substr(ad.hora_admision,0,6),coalesce(e.fecha_egreso,current_date),coalesce(e.hora_egreso,"+ValoresPorDefecto.getSentenciaHoraActualBD()+"),"+ConstantesBD.codigoViaIngresoHospitalizacion+") as estancia_filtro, "+
						//Es posible que haya diagnostico principal
						"CASE WHEN e.diagnostico_principal is not null and e.diagnostico_principal <> '"+ConstantesBD.acronimoDiagnosticoNoSeleccionado+"' then e.diagnostico_principal else '' end as codigo_diagnostico, "+
						//Se toma la descripcion del diagnóstico
						"CASE WHEN e.diagnostico_principal is not null and e.diagnostico_principal <> '"+ConstantesBD.acronimoDiagnosticoNoSeleccionado+"' then getnombrediagnostico(e.diagnostico_principal,e.diagnostico_principal_cie) else '' end as nombre_diagnostico, "+
						//Se toman los diagnosticos relacionados
						"case when e.diagnostico_relacionado1 <> '"+ConstantesBD.acronimoDiagnosticoNoSeleccionado+"' and e.diagnostico_relacionado1 is not null THEN " +
							"e.diagnostico_relacionado1 || ' - ' || getnombrediagnostico(e.diagnostico_relacionado1,e.diagnostico_relacionado1_cie) || '. '  " +
						"else " +
							"'' " +
						"end || " +
						"case when e.diagnostico_relacionado2 <> '"+ConstantesBD.acronimoDiagnosticoNoSeleccionado+"' and e.diagnostico_relacionado2 is not null THEN " +
							"e.diagnostico_relacionado2 || ' - ' || getnombrediagnostico(e.diagnostico_relacionado2,e.diagnostico_relacionado2_cie) || '. ' " +
						"else " +
							"'' " +
						"end || " +
						"case when e.diagnostico_relacionado3 <> '"+ConstantesBD.acronimoDiagnosticoNoSeleccionado+"' and e.diagnostico_relacionado3 is not null THEN " +
							"e.diagnostico_relacionado3 || ' - ' || getnombrediagnostico(e.diagnostico_relacionado3,e.diagnostico_relacionado3_cie) || '. ' " +
						"else " +
							"'' " +
						"end as diagnosticos_relacionados, ";
		//Si se seleleccionó tipo egreso: Egreso la edad del paciente se calcula respecto a la fecha egreso
		else if(UtilidadTexto.getBoolean(tipoEgreso))
			consulta += "getedad2(p.fecha_nacimiento,e.fecha_egreso) as edad, " +
						//con fecha egreso
						"to_char(e.fecha_egreso,'"+ConstantesBD.formatoFechaAp+"') as fecha_egreso, "+ 
						//se calcula tiempo estancia por egreso
						"gettiempoestancia(ad.fecha_admision,substr(ad.hora_admision,0,6),e.fecha_egreso,e.hora_egreso,c.via_ingreso) as estancia, "+
						"gettiempoestancia(ad.fecha_admision,substr(ad.hora_admision,0,6),e.fecha_egreso,e.hora_egreso,"+ConstantesBD.codigoViaIngresoHospitalizacion+") as estancia_filtro, "+
						//Se toma el diagnostico principal del egreso
						"e.diagnostico_principal as codigo_diagnostico, "+
						//Se toma la descripcion del diagnóstico
						"getnombrediagnostico(e.diagnostico_principal,e.diagnostico_principal_cie) as nombre_diagnostico, "+
						///Se toman los diagnosticos relacionados
						"case when e.diagnostico_relacionado1 <> '"+ConstantesBD.acronimoDiagnosticoNoSeleccionado+"' and e.diagnostico_relacionado1 is not null THEN " +
							"e.diagnostico_relacionado1 || ' - ' || getnombrediagnostico(e.diagnostico_relacionado1,e.diagnostico_relacionado1_cie) || '. '  " +
						"else " +
							"'' " +
						"end || " +
						"case when e.diagnostico_relacionado2 <> '"+ConstantesBD.acronimoDiagnosticoNoSeleccionado+"' and e.diagnostico_relacionado2 is not null THEN " +
							"e.diagnostico_relacionado2 || ' - ' || getnombrediagnostico(e.diagnostico_relacionado2,e.diagnostico_relacionado2_cie) || '. ' " +
						"else " +
							"'' " +
						"end || " +
						"case when e.diagnostico_relacionado3 <> '"+ConstantesBD.acronimoDiagnosticoNoSeleccionado+"' and e.diagnostico_relacionado3 is not null THEN " +
							"e.diagnostico_relacionado3 || ' - ' || getnombrediagnostico(e.diagnostico_relacionado3,e.diagnostico_relacionado3_cie) || '. ' " +
						"else " +
							"'' " +
						"end as diagnosticos_relacionados, ";
		//Si se seleccionó tipo egreso: Sin Egreso la edad del paciente se calcula respecto a la fecha actual
		else
			consulta += "getedad2(p.fecha_nacimiento,current_date) as edad, " +
						//sin fecha egreso
						"'' as fecha_egreso, "+
						//Se calcula estancia hasta la fecha actual
						"gettiempoestancia(ad.fecha_admision,substr(ad.hora_admision,0,6),current_date,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",c.via_ingreso) as estancia, "+
						"gettiempoestancia(ad.fecha_admision,substr(ad.hora_admision,0,6),current_date,"+ValoresPorDefecto.getSentenciaHoraActualBD()+","+ConstantesBD.codigoViaIngresoHospitalizacion+") as estancia_filtro, "+
						//No se muestra diagnostico principal
						"'' as codigo_diagnostico, "+
						//No se muestra descripcion de diagnostico principal
						"'' as nombre_diagnostico, "+
						//No se muestran diagnosticos relacionados
						"'' as diagnosticos_relacionados, ";
		
		//Dependiendo de si se seleccionaron convenios se toma la descripcion del responsable de la cuenta de manera diferente
		if(listadoConvenios.equals(""))
			consulta += "getnombreconvenioxingreso(c.id_ingreso) as convenio ";
		else
			consulta += "getnombreconvenio(sc.convenio) as convenio ";
				
				 
				
				 
		consulta += ""+
				"FROM cuentas c ";
		//Si se seleccionó tipo egreso : Egreso, se añade como inner join 
		if(UtilidadTexto.getBoolean(tipoEgreso))
			consulta += "INNER JOIN egresos e ON(e.cuenta = c.id) ";
		
		//Dependiendo de la vía de ingreso se hace el inner con la tabla de admision respectiva
		if(codigoViaIngreso==ConstantesBD.codigoViaIngresoUrgencias)
			consulta += "INNER JOIN admisiones_urgencias ad ON(ad.cuenta = c.id) ";
		else if(codigoViaIngreso==ConstantesBD.codigoViaIngresoHospitalizacion)
			consulta += "INNER JOIN admisiones_hospi ad ON(ad.cuenta = c.id) ";
		
		consulta += "INNER JOIN centros_costo cc ON(cc.codigo = c.area and cc.centro_atencion = "+codigoCentroAtencion+") ";
		
		//Si se eligieron convenios se hace inner con la tabla sub_cuentas
		if(!listadoConvenios.equals(""))
			consulta += "INNER JOIN sub_cuentas sc ON(sc.ingreso = c.id_ingreso and sc.nro_prioridad = 1) ";
		
		consulta += "INNER JOIN personas p ON(p.codigo = c.codigo_paciente) ";
		
		//Si nó se seleccionó tipo egreso aun así se hace left outer join con egresos
		if(tipoEgreso.equals(""))
			consulta += "left outer join egresos e ON(e.cuenta = ad.cuenta) ";
		
		
		consulta += "WHERE "+ 
				"c.via_ingreso = "+codigoViaIngreso+" "+
				(codigoTipoPaciente.equals("")?"":" and c.tipo_paciente = '"+codigoTipoPaciente+"' ");
		
		//Si son solo egresos el filtro de la fecha es diferente
		if(UtilidadTexto.getBoolean(tipoEgreso))
		{
			consulta += " and to_char(e.fecha_egreso, 'YYYY-MM-DD') between '"+fechaInicial+"' and '"+fechaFinal+"' and e.usuario_responsable IS NOT NULL ";
			
			//Se verifica el campo estado a la salida
			if(!estadoSalida.equals(""))
			{
				if(UtilidadTexto.getBoolean(estadoSalida))
					consulta += " and e.estado_salida = "+ValoresPorDefecto.getValorFalseParaConsultas()+" ";
				else
					consulta += " and e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+" ";
			}
		}
		else
			consulta += " and to_char(ad.fecha_admision, 'YYYY-MM-DD') between '"+fechaInicial+"' and '"+fechaFinal+"' ";
		
		//Se filtra poe los convenios
		if(!listadoConvenios.equals(""))
			consulta += " and sc.convenio in ("+listadoConvenios+") ";
		
		//Se filtra por el sexo del pacinete
		if(codigoSexo!=ConstantesBD.codigoNuncaValido)
			consulta += " and p.sexo = "+codigoSexo+" ";
		
		consulta +=	"" +
			") t "+ 
			"WHERE t.\"estancia_filtro\" >= "+prioridad+" ORDER BY t.\"estancia\" desc";
		
		logger.info("CONSULTA REPORTE=> "+consulta);
		return consulta;
	}
	//*******************************************************************************************************************************
	
	/**
	 * Método para realizar la consulta del reporte indicadores hospitalización
	 * @param con conexion a la BD
	 * @param dtoFiltro parametros de la consulta
	 */
	public static ArrayList<DtoResultadoConsultaIndicadoresHospitalizacion> consultarIndicadoresHospitalizacion(Connection con, DtoFiltroReporteIndicadoresHospitalizacion dtoFiltro)
	{
		PreparedStatement ps = null;
		ResultSet rs= null;

		String sql="select conv.nombre as convenio, " +
				"case when eg.cuenta is null " +
				"then 'Sin Egreso' " +
				"else 'Con Egreso' " +
				"end as estadoegreso, " +
				"administracion.getdescripcionsexo(per.sexo) as sexo, " +
				"count(ing.id) as cantidadpacientes " +
				"from ingresos ing " +
				"inner join cuentas cu on(cu.id_ingreso = ing.id) " +
				"inner join admisiones_hospi adho on(adho.cuenta = cu.id) " +
				"inner join pacientes pac on (pac.codigo_paciente = ing.codigo_paciente) " +
				"inner join personas per on(per.codigo = pac.codigo_paciente) " +
				"inner join sub_cuentas subc on(subc.ingreso = ing.id) " +
				"inner join convenios conv on(conv.codigo = subc.convenio) " +
				"left join egresos eg on(eg.cuenta = cu.id) " +
				"where cu.via_ingreso = "+ConstantesBD.codigoViaIngresoHospitalizacion+ 
				" and cu.tipo_paciente = '"+ConstantesBD.tipoPacienteHospitalizado+"'" +
				" and subc.nro_prioridad = 1 ";
		if(dtoFiltro.getConsecutivoCentroAtencion()!=ConstantesBD.codigoNuncaValido)
			sql+=" and ing.centro_atencion="+dtoFiltro.getConsecutivoCentroAtencion();
		if(!UtilidadTexto.isEmpty(dtoFiltro.getFechaInicial())&&!UtilidadTexto.isEmpty(dtoFiltro.getFechaFinal()))
			sql+=" and to_char(adho.fecha_admision, 'YYYY-MM-DD') between '"+UtilidadFecha.conversionFormatoFechaABD(dtoFiltro.getFechaInicial())+"' and '"+UtilidadFecha.conversionFormatoFechaABD(dtoFiltro.getFechaFinal())+"' ";
		if(!dtoFiltro.getDescripcionDiagnosticosEgreso().equals("Todos"))
			sql+=" and eg.diagnostico_principal in ("+dtoFiltro.getDescripcionDiagnosticosEgreso().trim()+")";
		if(dtoFiltro.getCodigoSexo()>ConstantesBD.codigoNuncaValido)
			sql+=" and per.sexo = "+dtoFiltro.getCodigoSexo();
		if(dtoFiltro.getCodigoConvenio()>ConstantesBD.codigoNuncaValido)
		{
			sql+=" and subc.convenio = "+dtoFiltro.getCodigoConvenio();
		}
		
			sql+=" group by conv.nombre, " +
				"case when eg.cuenta is null " +
				"then 'Sin Egreso' " +
				"else 'Con Egreso' end, " +
				"per.sexo " +
				"order by conv.nombre";
			
		ArrayList<DtoResultadoConsultaIndicadoresHospitalizacion> listaDtoResultados= 
			new ArrayList<DtoResultadoConsultaIndicadoresHospitalizacion>();
		try
		{
			ps = con.prepareStatement(sql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet);
			rs=ps.executeQuery();
			DtoResultadoConsultaIndicadoresHospitalizacion dtoResultado= null;
			while(rs.next())
			{
				dtoResultado= new DtoResultadoConsultaIndicadoresHospitalizacion();
				dtoResultado.setNombreConvenio(rs.getString("convenio"));
				dtoResultado.setEstadoEgreso(rs.getString("estadoegreso"));
				dtoResultado.setSexoPaciente(rs.getString("sexo"));
				dtoResultado.setCantidadPacientes(rs.getInt("cantidadpacientes"));
				listaDtoResultados.add(dtoResultado);
			}
		}
		catch(SQLException ex)
		{
			Log4JManager.error("Error generando en la consulta indicadores hospitalizacion --> "+ex);
		}
		finally
		{
			try {
				if(ps!=null)
					ps.close();
				if(rs!=null)
					rs.close();
			} catch (SQLException e) {
				Log4JManager.error("Error cerrando los objetos de persistencia consulta indicadores hospitalizacion.. ");
				e.printStackTrace();
			}
		}
		
		return listaDtoResultados;
	}
	
	
}
