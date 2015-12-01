/*
 * Ago 25, 2008
 */
package com.princetonsa.dao.sqlbase.manejoPaciente;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.sqlbase.SqlBaseEgresoDao;
import com.princetonsa.mundo.atencion.Diagnostico;


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
 * que se usan para el reporte de mortalidad
 *
 */
public class SqlBaseReporteMortalidadDao 
{
	/**
	 * Para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseReporteMortalidadDao.class);
	
	//***********CONSULTAS REPORTE TASA MORTALIDAD POR RANGO DE EDAD Y SEXO**********
	/**
	 * Método que edita las consultas del reporte de tasa de mortalidad 
	 * por rango de edad y sexo
	 */
	public static String[] obtenerConsultasTasaMortalidadRangoEdadSexo (HashMap campos)
	{
		String[] consultas = new String[2];
		
		//*******SE TOMAN LOS PARÁMETROS*************************************
		int codigoCentroAtencion = Utilidades.convertirAEntero(campos.get("codigoCentroAtencion").toString());
		String fechaInicial = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString());
		String fechaFinal = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString());
		int codigoSexo = Utilidades.convertirAEntero(campos.get("codigoSexo").toString());
		int codigoViaIngreso = Utilidades.convertirAEntero(campos.get("codigoViaIngreso").toString());
		int codigoInstitucion = Utilidades.convertirAEntero(campos.get("codigoInstitucion").toString());
		//*******************************************************************
		
		
		//Se edita la consulta principal de los rangos
		String rangoEdadSexoStr = "SELECT t.\"edad\",t.\"mes_egreso\",t.\"mes\",t.\"sexo\",re.nombre_etiqueta as nombre_etiqueta,re.orden as orden,'comodin' as comodin FROM "+
			"( "+
				"SELECT "+ 
					"coalesce(e.fecha_egreso,evo.fecha_evolucion) - p.fecha_nacimiento AS \"edad\", "+
					"coalesce(to_char(e.fecha_egreso,'YYYY-MM'),to_char(evo.fecha_evolucion,'YYYY-MM')) as \"mes_egreso\", "+
					"getnombremes(coalesce(to_char(e.fecha_egreso,'MM'),to_char(evo.fecha_evolucion,'MM'))) || ' ' || coalesce(to_char(e.fecha_egreso,'YYYY'),to_char(evo.fecha_evolucion,'YYYY')) AS \"mes\", "+
					"CASE WHEN p.sexo = "+ConstantesBD.codigoSexoMasculino+" THEN 'M' ELSE 'F' END AS \"sexo\" "+ 
				"FROM egresos e "+ 
				"INNER JOIN cuentas c ON(c.id = e.cuenta) " +
				"INNER JOIN centros_costo cc ON(cc.codigo = c.area and cc.centro_atencion = "+codigoCentroAtencion+") " +
				"INNER JOIN pacientes pac ON(pac.codigo_paciente = c.codigo_paciente) "+ 
				"INNER JOIN personas p ON(p.codigo = pac.codigo_paciente) " +
				"LEFT OUTER JOIN evoluciones evo ON(evo.codigo = e.evolucion) "+ 
				"WHERE " +
				"(" +
				//Egreso administrativo
				"(e.fecha_egreso IS NOT NULL AND to_char(e.fecha_egreso, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.usuario_responsable IS NOT NULL) " +
				" OR " +
				//Egreso medico
				"(evo.fecha_evolucion is not null and to_char(evo.fecha_evolucion, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.destino_salida is not null)" +
				") and e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+"  "+
				(codigoViaIngreso!=ConstantesBD.codigoNuncaValido?" and c.via_ingreso = "+codigoViaIngreso:"")+
				(codigoSexo!=ConstantesBD.codigoNuncaValido?" and p.sexo = "+codigoSexo:"") +
			") t "+
			"INNER JOIN rangos_estadisticos re ON(" +
				"re.rango_inicial <= t.\"edad\" AND " +
				"re.rango_final >= t.\"edad\" and " +
				"re.institucion = "+codigoInstitucion+" and " +
				"re.activo = '"+ConstantesBD.acronimoSi+"' and " +
				"re.reporte = "+ConstantesBDManejoPaciente.tipoReporteTasaMortalidadRangoEdadSexo+") "+ 
			"order by t.\"mes_egreso\",re.orden";
		consultas[0] = rangoEdadSexoStr;
		logger.info("consultas[0]: "+consultas[0]);
		
		//Se edita la consulta del total de egresos
		String totalEgresos = "SELECT t.\"mes_egreso\",t.\"mes\",sum(t.\"muerte\") as \"total_muerte\",sum(t.\"vida\") as \"total_vida\" FROM "+ 
			"( "+
				"SELECT "+ 
				"to_char(coalesce(e.fecha_egreso,evo.fecha_evolucion),'YYYY-MM') as \"mes_egreso\", "+
				"getnombremes(to_char(coalesce(e.fecha_egreso,evo.fecha_evolucion),'MM')) || ' ' || to_char(coalesce(e.fecha_egreso,evo.fecha_evolucion),'YYYY') AS \"mes\", "+
				"CASE WHEN e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 1 ELSE 0 END AS \"muerte\", "+ 
				"CASE WHEN e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 0 ELSE 1 END AS \"vida\" "+ 
				"FROM egresos e "+ 
				"INNER JOIN cuentas c ON(c.id = e.cuenta) "+ 
				"INNER JOIN centros_costo cc ON(cc.codigo = c.area and cc.centro_atencion = "+codigoCentroAtencion+") " +
				"INNER JOIN pacientes pac ON(pac.codigo_paciente = c.codigo_paciente) "+ 
				"INNER JOIN personas p ON(p.codigo = pac.codigo_paciente) " +
				"LEFT OUTER JOIN evoluciones evo ON(evo.codigo = e.evolucion) "+ 
				"WHERE " +
				"(" +
				//Egreso administrativo
				"(e.fecha_egreso IS NOT NULL AND to_char(e.fecha_egreso, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.usuario_responsable IS NOT NULL) " +
				" OR " +
				//Egreso medico
				"(evo.fecha_evolucion is not null and to_char(evo.fecha_evolucion, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.destino_salida is not null)" +
				")   "+
				(codigoViaIngreso!=ConstantesBD.codigoNuncaValido?" and c.via_ingreso = "+codigoViaIngreso:"")+
				(codigoSexo!=ConstantesBD.codigoNuncaValido?" and p.sexo = "+codigoSexo:"") +
				" ORDER BY \"mes_egreso\" "+
			") t GROUP BY t.\"mes_egreso\",t.\"mes\" ORDER BY t.\"mes_egreso\"";
		consultas[1] = totalEgresos;
		logger.info("consultas[1]: "+consultas[1]);
		
		return consultas;
	}
	//**********CONSULTA REPORTE NÚMERO DE PACIENTES FALLECIDOS POR DX MUERTE**********************
	/**
	 * Método que define la consulta del reporte de número de pacientes fallecidos
	 * por diagnóstico de muerte
	 */
	public static String obtenerConsultasNumeroPacFallecidosDxMuerte(HashMap campos)
	{
		//**********SE TOMAN LOS PARÁMETROS***********************************
		int codigoCentroAtencion = Utilidades.convertirAEntero(campos.get("codigoCentroAtencion").toString());
		String fechaInicial = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString());
		String fechaFinal = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString());
		HashMap diagnosticosMuerte = (HashMap)campos.get("diagnosticosMuerte");
		int codigoViaIngreso = Utilidades.convertirAEntero(campos.get("codigoViaIngreso").toString());
		//Se arma el listado de diagnósticos de muerte
		String listadoDiagnosticos = "";
		for(int i=0;i<Utilidades.convertirAEntero(diagnosticosMuerte.get("numRegistros").toString());i++)
			if(UtilidadTexto.getBoolean(diagnosticosMuerte.get("checkbox_"+i).toString()))
			{
				String[] vector = diagnosticosMuerte.get(""+i).toString().split(ConstantesBD.separadorSplit);
				listadoDiagnosticos += (listadoDiagnosticos.equals("")?"":",") + "'" + vector[0] + "'";
			}
		//********************************************************************
		
		
		String numPacFallecidosDxMuerte = "SELECT "+
			"to_char(coalesce(e.fecha_egreso,evo.fecha_evolucion),'YYYY-MM') as mes_egreso, "+
			"getnombremes(to_char(coalesce(e.fecha_egreso,evo.fecha_evolucion),'MM')) || ' ' || to_char(coalesce(e.fecha_egreso,evo.fecha_evolucion),'YYYY') AS mes, "+ 
			"e.diagnostico_muerte || '-' || e.diagnostico_muerte_cie || ' ' || getnombrediagnostico(e.diagnostico_muerte,e.diagnostico_muerte_cie) AS acronimo_diagnostico "+  
			"FROM egresos e "+ 
			"INNER JOIN cuentas c ON(c.id = e.cuenta) "+ 
			"INNER JOIN centros_costo cc ON(cc.codigo = c.area and cc.centro_atencion = "+codigoCentroAtencion+") " +
			"LEFT OUTER JOIN evoluciones evo ON(evo.codigo = e.evolucion) " +
			"WHERE " +
			"(" +
			//Egreso administrativo
			"(e.fecha_egreso IS NOT NULL AND to_char(e.fecha_egreso, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.usuario_responsable IS NOT NULL) " +
			" OR " +
			//Egreso medico
			"(evo.fecha_evolucion is not null and to_char(evo.fecha_evolucion, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.destino_salida is not null)" +
			") and e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+"  "+
			" and (e.diagnostico_muerte IN ("+listadoDiagnosticos+")) "+(codigoViaIngreso!=ConstantesBD.codigoNuncaValido?" and c.via_ingreso = "+codigoViaIngreso:"");
		
		logger.info("CONSULTA DE NUM PACIETNES FALLECIDOS DXMUERTE=> "+numPacFallecidosDxMuerte);
		return numPacFallecidosDxMuerte;
	}
	
	//********************************************************************************************
	//***********CONSULTA REPORTE MORTALIDAD MENSUAL CONVENIO***************************************
	/**
	 * Método que define la consulta del reporte de mortalidad mensual por convenio
	 */
	public static String obtenerConsultaMortalidadMensualConvenio(HashMap campos)
	{
		//***********SE TOMAN LOS PARÁMETROS******************************************************
		int codigoCentroAtencion = Utilidades.convertirAEntero(campos.get("codigoCentroAtencion").toString());
		String fechaInicial = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString());
		String fechaFinal = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString());
		HashMap convenios = (HashMap)campos.get("convenios");
		int codigoViaIngreso = Utilidades.convertirAEntero(campos.get("codigoViaIngreso").toString());
		int edad = Utilidades.convertirAEntero(campos.get("edad").toString());
		Diagnostico diagEgreso = (Diagnostico)campos.get("diagnosticoEgreso");
		//Se arma listado de convenios
		String listadoConvenios = "";
		for(int i=0;i<Utilidades.convertirAEntero(convenios.get("numRegistros").toString());i++)
			if(UtilidadTexto.getBoolean(convenios.get("checkbox_"+i).toString()))
				listadoConvenios += (listadoConvenios.equals("")?"":",") + convenios.get("codigo_"+i);
		//*****************************************************************************************
		
		
		String mortalidadMensualConvenio = "SELECT "+
			"to_char(coalesce(e.fecha_egreso,evo.fecha_evolucion),'YYYY-MM') as mes_egreso, "+
			"getnombremes(to_char(coalesce(e.fecha_egreso,evo.fecha_evolucion),'MM')) || ' ' || to_char(coalesce(e.fecha_egreso,evo.fecha_evolucion),'YYYY') AS mes, "+ 
			"getnombreconvenio(sc.convenio)  as nombre_convenio, "+ 
			"sc.convenio as codigo_convenio, "+
			"CASE WHEN e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 0 ELSE 1 END AS vivo, "+
			"CASE WHEN e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 1 ELSE 0 END AS muerto "+
			"FROM egresos e "+ 
			"INNER JOIN cuentas c ON(c.id = e.cuenta) "+ 
			"INNER JOIN centros_costo cc ON(cc.codigo = c.area and cc.centro_atencion = "+codigoCentroAtencion+") " +
			"INNER JOIN sub_cuentas sc ON(sc.ingreso = c.id_ingreso AND sc.nro_prioridad = 1) "+ 
			"INNER JOIN personas p ON(p.codigo = c.codigo_paciente) " +
			"LEFT OUTER JOIN evoluciones evo ON(evo.codigo = e.evolucion) "+
			"WHERE " +
			"(" +
				//Egreso administrativo
				"(e.fecha_egreso IS NOT NULL AND to_char(e.fecha_egreso, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.usuario_responsable IS NOT NULL) " +
				" OR " +
				//Egreso medico
				"(evo.fecha_evolucion is not null and to_char(evo.fecha_evolucion, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.destino_salida is not null)" +
			")   "+
			(!diagEgreso.getAcronimo().equals("")?" and e.diagnostico_principal = '"+diagEgreso.getAcronimo()+"' ":"")+
			(codigoViaIngreso!=ConstantesBD.codigoNuncaValido?" and c.via_ingreso = "+codigoViaIngreso+" ":"")+
			" and sc.convenio in ("+listadoConvenios+") "+
			(edad!=ConstantesBD.codigoNuncaValido?" and getedad2(p.fecha_nacimiento,e.fecha_egreso) = "+edad+" ":""); 
		
		logger.info("consulta mortalidad mensual convenio: "+mortalidadMensualConvenio);
		return mortalidadMensualConvenio;
	}
	//**********************************************************************************************
	//*********CONSULTA REPORTE LISTADO PACIENTES FALLECIDOS******************************************
	/**
	 * Método que define la consulta del reporte de listado de pacientes fallecidos
	 */
	public static String obtenerConsultaListadoPacientesFallecidos(HashMap campos)
	{
		//***********SE TOMAN LOS PARÁMETROS******************************************************
		int codigoCentroAtencion = Utilidades.convertirAEntero(campos.get("codigoCentroAtencion").toString());
		String fechaInicial = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString());
		String fechaFinal = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString());
		int codigoViaIngreso = Utilidades.convertirAEntero(campos.get("codigoViaIngreso").toString());
		int edad = Utilidades.convertirAEntero(campos.get("edad").toString());
		int estancia = Utilidades.convertirAEntero(campos.get("estancia").toString());
		Diagnostico diagEgreso = (Diagnostico)campos.get("diagnosticoEgreso");
		HashMap diagMuerte = (HashMap)campos.get("diagnosticoMuerte");
		String acronimoDiagMuerte = "";
		if(!UtilidadTexto.isEmpty(diagMuerte.get("valor")+""))
			acronimoDiagMuerte = diagMuerte.get("valor").toString().split(ConstantesBD.separadorSplit)[0];
		int codigoConvenio = Utilidades.convertirAEntero(campos.get("codigoConvenio").toString());
		//*********************************************************************************
		
		String listadoPacientesFallecidos = "SELECT "+
			"i.consecutivo || coalesce(' '||i.anio_consecutivo,'') as ingreso, "+
			"p.primer_apellido || coalesce(' '||p.segundo_apellido,'') || ' ' || p.primer_nombre || coalesce(' '||p.segundo_nombre,'') as paciente, "+
			"pac.historia_clinica || coalesce(' '||pac.anio_historia_clinica,'')  as hc, "+
			"getedaddetallada(p.fecha_nacimiento,coalesce(e.fecha_egreso,evo.fecha_evolucion)) as edad, "+
			"CASE WHEN p.sexo = "+ConstantesBD.codigoSexoMasculino+" THEN 'M' ELSE 'F' END as sexo, "+ 
			"to_char(getfechaingreso(c.id,c.via_ingreso),'"+ConstantesBD.formatoFechaAp+"') as fecha_ingreso, "+
			"to_char(coalesce(e.fecha_egreso,evo.fecha_evolucion),'"+ConstantesBD.formatoFechaAp+"') as fecha_egreso, "+
			"gettiempoestancia(getfechaingreso(c.id,c.via_ingreso),substr(gethoraingreso(c.id,c.via_ingreso),0,6),coalesce(e.fecha_egreso,evo.fecha_evolucion),substr(coalesce(e.hora_egreso,evo.hora_evolucion),0,6),c.via_ingreso) as estancia, "+
			"gettiempoestancia(getfechaingreso(c.id,c.via_ingreso),substr(gethoraingreso(c.id,c.via_ingreso),0,6),coalesce(e.fecha_egreso,evo.fecha_evolucion),substr(coalesce(e.hora_egreso,evo.hora_evolucion),0,6),"+ConstantesBD.codigoViaIngresoHospitalizacion+") as estancia_suma, "+
			"e.diagnostico_principal || '-' || e.diagnostico_principal_cie || ' ' || getnombrediagnostico(e.diagnostico_principal,e.diagnostico_principal_cie) as dx_egreso, "+
			"e.diagnostico_muerte || '-' || e.diagnostico_muerte_cie || ' ' || getnombrediagnostico(e.diagnostico_muerte,e.diagnostico_muerte_cie) as dx_muerte, "+
			"getnombreconvenio(sc.convenio)  as nombre_convenio "+ 
			"FROM egresos e "+ 
			"INNER JOIN cuentas c ON(c.id = e.cuenta) "+ 
			"INNER JOIN centros_costo cc ON(cc.codigo = c.area and cc.centro_atencion = "+codigoCentroAtencion+") " +
			"INNER JOIN ingresos i ON(i.id = c.id_ingreso) "+ 
			"INNER JOIN sub_cuentas sc ON(sc.ingreso = c.id_ingreso AND sc.nro_prioridad = 1) "+ 
			"INNER JOIN pacientes pac ON(pac.codigo_paciente = c.codigo_paciente) "+
			"INNER JOIN personas p ON(p.codigo = pac.codigo_paciente) " +
			"LEFT OUTER JOIN evoluciones evo ON(evo.codigo = e.evolucion) "+
			"WHERE " +
			"(" +
				//Egreso administrativo
				"(e.fecha_egreso IS NOT NULL AND to_char(e.fecha_egreso, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.usuario_responsable IS NOT NULL) " +
				" OR " +
				//Egreso medico
				"(evo.fecha_evolucion is not null and to_char(evo.fecha_evolucion, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.destino_salida is not null)" +
			") and e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+"  "+
			(!diagEgreso.getAcronimo().equals("")?" and e.diagnostico_principal = '"+diagEgreso.getAcronimo()+"' ":"")+
			(!acronimoDiagMuerte.equals("")?" and e.diagnostico_muerte = '"+acronimoDiagMuerte+"' ":"")+
			(codigoViaIngreso!=ConstantesBD.codigoNuncaValido?" and c.via_ingreso = "+codigoViaIngreso+" ":"")+
			(codigoConvenio>0?" and sc.convenio = "+codigoConvenio+" ":"")+
			(estancia!=ConstantesBD.codigoNuncaValido?" and gettiempoestancia(getfechaingreso(c.id,c.via_ingreso),substr(gethoraingreso(c.id,c.via_ingreso),0,6),e.fecha_egreso,substr(e.hora_egreso,0,6),c.via_ingreso) = "+estancia+" ":"")+
			(edad!=ConstantesBD.codigoNuncaValido?" and getedad2(p.fecha_nacimiento,e.fecha_egreso) = "+edad+" ":"");
		
		logger.info("listadoPacientesFallecidos: "+listadoPacientesFallecidos);
		return listadoPacientesFallecidos;
	}
	//************************************************************************************************
	//***********CONSULTA REPORTE MORTALIDAD POR DX DE MUERTE Y CENTRO DE COSTO***********************
	/**
	 * Método que define la consulta del reporte de mortalidad por diagnostico de muerte y centro de costo
	 */
	public static String obtenerConsultaMortalidadDxMuerteCentroCosto(HashMap campos)
	{
		//***********SE TOMAN LOS PARÁMETROS******************************************************
		int codigoCentroAtencion = Utilidades.convertirAEntero(campos.get("codigoCentroAtencion").toString());
		String fechaInicial = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString());
		String fechaFinal = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString());
		int codigoViaIngreso = Utilidades.convertirAEntero(campos.get("codigoViaIngreso").toString());
		HashMap diagMuerte = (HashMap)campos.get("diagnosticosMuerte");
		String listadoDiagMuerte = "";
		
		for(int i=0;i<Utilidades.convertirAEntero(diagMuerte.get("numRegistros").toString());i++)
			if(UtilidadTexto.getBoolean(diagMuerte.get("checkbox_"+i).toString()))
			{
				String[] vector = diagMuerte.get(i+"").toString().split(ConstantesBD.separadorSplit);
				listadoDiagMuerte += (listadoDiagMuerte.equals("")?"":",") + "'" + vector[0] + "'";
			}
		
		HashMap centrosCosto = (HashMap)campos.get("centrosCosto");
		String listadoCentrosCosto = "";
		for(int i=0;i<Utilidades.convertirAEntero(centrosCosto.get("numRegistros").toString());i++)
			if(UtilidadTexto.getBoolean(centrosCosto.get("checkbox_"+i).toString()))
				listadoCentrosCosto += (listadoCentrosCosto.equals("")?"":",") + centrosCosto.get("codigo_"+i);
		//*********************************************************************************
		
		
		String mortalidadDxMuerteCentroCosto = "SELECT "+
			"cc.nombre as nombre_centro_costo, "+
			"e.diagnostico_muerte || '-' || e.diagnostico_muerte_cie || ' ' || getnombrediagnostico(e.diagnostico_muerte,e.diagnostico_muerte_cie) as dx_muerte "+ 
			"FROM egresos e "+ 
			"INNER JOIN cuentas c ON(c.id = e.cuenta) "+ 
			"INNER JOIN centros_costo cc ON(cc.codigo = c.area and cc.centro_atencion = "+codigoCentroAtencion+") "+  
			"INNER JOIN sub_cuentas sc ON(sc.ingreso = c.id_ingreso AND sc.nro_prioridad = 1) "+ 
			"INNER JOIN personas p ON(p.codigo = c.codigo_paciente) " +
			"LEFT OUTER JOIN evoluciones evo ON(evo.codigo = e.evolucion) "+
			"WHERE " +
			"(" +
				//Egreso administrativo
				"(e.fecha_egreso IS NOT NULL AND to_char(e.fecha_egreso, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.usuario_responsable IS NOT NULL) " +
				" OR " +
				//Egreso medico
				"(evo.fecha_evolucion is not null and to_char(evo.fecha_evolucion, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.destino_salida is not null)" +
			") and e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+"  "+
			" and e.diagnostico_muerte IN ("+listadoDiagMuerte+") "+
			(codigoViaIngreso!=ConstantesBD.codigoNuncaValido?" and c.via_ingreso = "+codigoViaIngreso+" ":"")+
			(!listadoCentrosCosto.equals("")?" and c.area IN ("+listadoCentrosCosto+") ":"");
		
		return mortalidadDxMuerteCentroCosto;
	}
	//************************************************************************************************
	//**********CONSULTA REPORTE ESTANCIA PROMEDIO DE PACIENTES FALLECIDOS POR RANGO DE EDAD***********************
	/**
	 * Método que define la consulta del reporte de estancia promedio de pacientes fallecidos por rango de edad
	 */
	public static String[] obtenerConsultasEstanciaPromPacFallRangoEdad(HashMap campos)
	{
		String[] consultas = new String[2];
		
		//***********SE TOMAN LOS PARÁMETROS******************************************************
		int codigoCentroAtencion = Utilidades.convertirAEntero(campos.get("codigoCentroAtencion").toString());
		String fechaInicial = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString());
		String fechaFinal = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString());
		int codigoViaIngreso = Utilidades.convertirAEntero(campos.get("codigoViaIngreso").toString());
		int estancia = Utilidades.convertirAEntero(campos.get("estancia").toString());
		int codigoInstitucion = Utilidades.convertirAEntero(campos.get("codigoInstitucion").toString());
		
		HashMap diagMuerte = (HashMap)campos.get("diagnosticosMuerte");
		String acronimoDiagMuerte = "";
		if(!UtilidadTexto.isEmpty(diagMuerte.get("valor")+""))
			acronimoDiagMuerte = diagMuerte.get("valor").toString().split(ConstantesBD.separadorSplit)[0];
		
		Diagnostico diagEgreso = (Diagnostico)campos.get("diagnosticoEgreso");
		
		HashMap centrosCosto = (HashMap)campos.get("centrosCosto");
		int codigoCentroCosto = ConstantesBD.codigoNuncaValido;
		if(!UtilidadTexto.isEmpty(centrosCosto.get("codigo")+""))
			codigoCentroCosto = Utilidades.convertirAEntero(centrosCosto.get("codigo").toString());
		//*********************************************************************************
		
		String estanciaPromPacFallRangoEdad = "SELECT t.\"valor\",t.\"tipo\",t.\"edad\",t.\"mes_egreso\",t.\"mes\",t.\"sexo\",re.nombre_etiqueta as nombre_etiqueta,re.orden as orden FROM "+
			"( "+
				"SELECT "+ 
				"1 as valor, "+
				"coalesce(e.fecha_egreso,evo.fecha_evolucion) - p.fecha_nacimiento AS edad, "+
				"to_char(coalesce(e.fecha_egreso,evo.fecha_evolucion),'YYYY-MM') as mes_egreso, "+
				"getnombremes(to_char(coalesce(e.fecha_egreso,evo.fecha_evolucion),'MM')) || ' ' || to_char(coalesce(e.fecha_egreso,evo.fecha_evolucion),'YYYY') AS mes, "+
				"CASE WHEN p.sexo = "+ConstantesBD.codigoSexoMasculino+" THEN 'M' ELSE 'F' END AS sexo, "+ 
				"'PACIENTES' as tipo "+ 
				"FROM egresos e "+ 
				"INNER JOIN cuentas c ON(c.id = e.cuenta) "+ 
				"INNER JOIN centros_costo cc ON(cc.codigo = c.area and cc.centro_atencion = "+codigoCentroAtencion+") "+
				"INNER JOIN pacientes pac ON(pac.codigo_paciente = c.codigo_paciente) "+ 
				"INNER JOIN personas p ON(p.codigo = pac.codigo_paciente) " +
				"LEFT OUTER JOIN evoluciones evo ON(evo.codigo = e.evolucion) "+ 
				"WHERE " +
				"(" +
					//Egreso administrativo
					"(e.fecha_egreso IS NOT NULL AND to_char(e.fecha_egreso, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.usuario_responsable IS NOT NULL) " +
					" OR " +
					//Egreso medico
					"(evo.fecha_evolucion is not null and to_char(evo.fecha_evolucion, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.destino_salida is not null)" +
				") and e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+"  "+
				(!acronimoDiagMuerte.equals("")?" and e.diagnostico_muerte = '"+acronimoDiagMuerte+"' ":"")+
				(!diagEgreso.getAcronimo().equals("")?" and e.diagnostico_principal = '"+diagEgreso.getAcronimo()+"' ":"")+
				(codigoViaIngreso!=ConstantesBD.codigoNuncaValido?" and c.via_ingreso = "+codigoViaIngreso+" ":"") +
				(codigoCentroCosto>0?" and c.area = "+codigoCentroCosto+" ":"")+
				(estancia!=ConstantesBD.codigoNuncaValido?" and gettiempoestancia(getfechaingreso(c.id,c.via_ingreso),substr(gethoraingreso(c.id,c.via_ingreso),0,6),coalesce(e.fecha_egreso,evo.fecha_evolucion),substr(coalesce(e.hora_egreso,evo.hora_evolucion),0,6),c.via_ingreso) = "+estancia+" ":"")+
			") t "+ 
			"INNER JOIN rangos_estadisticos re ON(re.rango_inicial <= t.\"edad\" AND re.rango_final >= t.\"edad\" and re.institucion = "+codigoInstitucion+" and re.activo = '"+ConstantesBD.acronimoSi+"' and re.reporte = "+ConstantesBDManejoPaciente.tipoReporteEstanciaPromPacFallecidosRangoEdad+") "+ 
			"order by t.\"mes_egreso\",re.orden";
		logger.info("estanciaPromPacFallRangoEdad N° 1: "+estanciaPromPacFallRangoEdad);
		consultas[0] = estanciaPromPacFallRangoEdad;
		
		estanciaPromPacFallRangoEdad = "SELECT t.\"valor\",t.\"paciente\",t.\"tipo\",t.\"edad\",t.\"mes_egreso\",t.\"mes\",t.\"sexo\",re.nombre_etiqueta as nombre_etiqueta,re.orden as orden FROM "+
			"( "+ 
				"SELECT "+ 
				"gettiempoestancia(getfechaingreso(c.id,c.via_ingreso),substr(gethoraingreso(c.id,c.via_ingreso),0,6),coalesce(e.fecha_egreso,evo.fecha_evolucion),substr(coalesce(e.hora_egreso,evo.hora_evolucion),0,6),"+ConstantesBD.codigoViaIngresoHospitalizacion+") as valor, "+
				"1 as paciente, "+
				"coalesce(e.fecha_egreso,evo.fecha_evolucion) - p.fecha_nacimiento AS edad, "+
				"to_char(coalesce(e.fecha_egreso,evo.fecha_evolucion),'YYYY-MM') as mes_egreso, "+
				"getnombremes(to_char(coalesce(e.fecha_egreso,evo.fecha_evolucion),'MM')) || ' ' || to_char(coalesce(e.fecha_egreso,evo.fecha_evolucion),'YYYY') AS mes, "+
				"CASE WHEN p.sexo = "+ConstantesBD.codigoSexoMasculino+" THEN 'M' ELSE 'F' END AS sexo, "+ 
				"'ESTANCIA' as tipo "+ 
				"FROM egresos e "+ 
				"INNER JOIN cuentas c ON(c.id = e.cuenta) "+ 
				"INNER JOIN centros_costo cc ON(cc.codigo = c.area and cc.centro_atencion = "+codigoCentroAtencion+") "+
				"INNER JOIN pacientes pac ON(pac.codigo_paciente = c.codigo_paciente) "+ 
				"INNER JOIN personas p ON(p.codigo = pac.codigo_paciente) " +
				"LEFT OUTER JOIN evoluciones evo on (evo.codigo = e.evolucion) "+ 
				"WHERE " +
				"(" +
					//Egreso administrativo
					"(e.fecha_egreso IS NOT NULL AND to_char(e.fecha_egreso, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.usuario_responsable IS NOT NULL) " +
					" OR " +
					//Egreso medico
					"(evo.fecha_evolucion is not null and to_char(evo.fecha_evolucion, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.destino_salida is not null)" +
				") and e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+"  "+
				(!acronimoDiagMuerte.equals("")?" and e.diagnostico_muerte = '"+acronimoDiagMuerte+"' ":"")+
				(!diagEgreso.getAcronimo().equals("")?" and e.diagnostico_principal = '"+diagEgreso.getAcronimo()+"' ":"")+
				(codigoViaIngreso!=ConstantesBD.codigoNuncaValido?" and c.via_ingreso = "+codigoViaIngreso+" ":"") +
				(codigoCentroCosto>0?" and c.area = "+codigoCentroCosto+" ":"")+
				(estancia!=ConstantesBD.codigoNuncaValido?" and gettiempoestancia(getfechaingreso(c.id,c.via_ingreso),substr(gethoraingreso(c.id,c.via_ingreso),0,6),coalesce(e.fecha_egreso,evo.fecha_evolucion),substr(coalesce(e.hora_egreso,evo.hora_evolucion),0,6),c.via_ingreso) = "+estancia+" ":"")+
			") t "+ 
			"INNER JOIN rangos_estadisticos re ON(re.rango_inicial <= t.\"edad\" AND re.rango_final >= t.\"edad\" and re.institucion = "+codigoInstitucion+" and re.activo = '"+ConstantesBD.acronimoSi+"' and re.reporte = "+ConstantesBDManejoPaciente.tipoReporteEstanciaPromPacFallecidosRangoEdad+") "+ 
			"order by t.\"mes_egreso\",re.orden";
		logger.info("estanciaPromPacFallRangoEdad N° 2: "+estanciaPromPacFallRangoEdad);
		consultas[1] = estanciaPromPacFallRangoEdad;
		
		return consultas;
	}
	//***************************************************************************************************************
	//***********CONSULTA REPORTE MORTALIDAD RANGO DE TIEMPOS*********************************************************
	/**
	 * Método para obtener la consultar del reporte de mortalidad por rango de tiempos
	 */
	public static String obtenerConsultaMortalidadRangoTiempos(HashMap campos)
	{
		//***********SE TOMAN LOS PARÁMETROS******************************************************
		int codigoCentroAtencion = Utilidades.convertirAEntero(campos.get("codigoCentroAtencion").toString());
		String fechaInicial = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString());
		String fechaFinal = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString());
		int codigoViaIngreso = Utilidades.convertirAEntero(campos.get("codigoViaIngreso").toString());
		int edad = Utilidades.convertirAEntero(campos.get("edad").toString());
		int codigoInstitucion = Utilidades.convertirAEntero(campos.get("codigoInstitucion").toString());
		
		HashMap diagMuerte = (HashMap)campos.get("diagnosticosMuerte");
		String acronimoDiagMuerte = "";
		if(!UtilidadTexto.isEmpty(diagMuerte.get("valor")+""))
			acronimoDiagMuerte = diagMuerte.get("valor").toString().split(ConstantesBD.separadorSplit)[0];
		
		Diagnostico diagEgreso = (Diagnostico)campos.get("diagnosticoEgreso");
		
		HashMap centrosCosto = (HashMap)campos.get("centrosCosto");
		int codigoCentroCosto = ConstantesBD.codigoNuncaValido;
		if(!UtilidadTexto.isEmpty(centrosCosto.get("codigo")+""))
			codigoCentroCosto = Utilidades.convertirAEntero(centrosCosto.get("codigo").toString());
		//*********************************************************************************
		
		String mortalidadRangoTiempos = "SELECT t.\"estancia\",t.\"paciente\",t.\"tiempo_estancia\",t.\"sexo\",re.nombre_etiqueta as nombre_etiqueta,re.orden as orden FROM "+
			"( "+
				"SELECT "+ 
				"gettiempoestancia(getfechaingreso(c.id,c.via_ingreso),substr(gethoraingreso(c.id,c.via_ingreso),0,6),coalesce(e.fecha_egreso,evo.fecha_evolucion),substr(coalesce(e.hora_egreso,evo.hora_evolucion),0,6),"+ConstantesBD.codigoViaIngresoHospitalizacion+") as estancia, "+
				"1 as paciente, "+
				"gettiempoestancia(getfechaingreso(c.id,c.via_ingreso),substr(gethoraingreso(c.id,c.via_ingreso),0,6),coalesce(e.fecha_egreso,evo.fecha_evolucion),substr(coalesce(e.hora_egreso,evo.hora_evolucion),0,6),"+ConstantesBD.codigoViaIngresoUrgencias+")*60 as tiempo_estancia, "+
				"CASE WHEN p.sexo = "+ConstantesBD.codigoSexoMasculino+" THEN 'M' ELSE 'F' END AS sexo "+ 
				"FROM egresos e "+ 
				"INNER JOIN cuentas c ON(c.id = e.cuenta) "+ 
				"INNER JOIN centros_costo cc ON(cc.codigo = c.area and cc.centro_atencion = "+codigoCentroAtencion+") "+
				"INNER JOIN pacientes pac ON(pac.codigo_paciente = c.codigo_paciente) "+ 
				"INNER JOIN personas p ON(p.codigo = pac.codigo_paciente) " +
				"LEFT OUTER JOIN evoluciones evo ON(evo.codigo = e.evolucion)  "+ 
				"WHERE " +
				"(" +
					//Egreso administrativo
					"(e.fecha_egreso IS NOT NULL AND to_char(e.fecha_egreso, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.usuario_responsable IS NOT NULL) " +
					" OR " +
					//Egreso medico
					"(evo.fecha_evolucion is not null and to_char(evo.fecha_evolucion, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.destino_salida is not null)" +
				") and e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+"  "+
				(!acronimoDiagMuerte.equals("")?" and e.diagnostico_muerte = '"+acronimoDiagMuerte+"' ":"")+
				(!diagEgreso.getAcronimo().equals("")?" and e.diagnostico_principal = '"+diagEgreso.getAcronimo()+"' ":"")+
				(codigoViaIngreso!=ConstantesBD.codigoNuncaValido?" and c.via_ingreso = "+codigoViaIngreso+" ":"") +
				(codigoCentroCosto>0?" and c.area = "+codigoCentroCosto+" ":"")+
				(edad!=ConstantesBD.codigoNuncaValido?" and getedad2(p.fecha_nacimiento,coalesce(e.fecha_egreso,evo.fecha_evolucion)) = "+edad+" ":"")+
			") t "+ 
			"INNER JOIN rangos_estadisticos re ON(re.rango_inicial <= t.\"tiempo_estancia\" AND re.rango_final >= t.\"tiempo_estancia\" and re.institucion = "+codigoInstitucion+" and re.activo = '"+ConstantesBD.acronimoSi+"' and re.reporte = "+ConstantesBDManejoPaciente.tipoReporteMortalidadRangoTiempos+") "+ 
			"order by re.orden";
		
		return mortalidadRangoTiempos;
	}
	//*****************************************************************************************************************
	//***********CONSULTA REPORTE MORTALIDAD POR SEXO*********************************************************
	/**
	 * Método para obtener la consultar del reporte de mortalidad por sexo
	 */
	public static String obtenerConsultaMortalidadSexo(HashMap campos)
	{
		//***********SE TOMAN LOS PARÁMETROS******************************************************
		int codigoCentroAtencion = Utilidades.convertirAEntero(campos.get("codigoCentroAtencion").toString());
		String fechaInicial = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString());
		String fechaFinal = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString());
		int codigoViaIngreso = Utilidades.convertirAEntero(campos.get("codigoViaIngreso").toString());
		int codigoSexo = Utilidades.convertirAEntero(campos.get("codigoSexo").toString());
		int edad = Utilidades.convertirAEntero(campos.get("edad").toString());
		
		
		HashMap diagMuerte = (HashMap)campos.get("diagnosticosMuerte");
		String acronimoDiagMuerte = "";
		if(!UtilidadTexto.isEmpty(diagMuerte.get("valor")+""))
			acronimoDiagMuerte = diagMuerte.get("valor").toString().split(ConstantesBD.separadorSplit)[0];
		
		Diagnostico diagEgreso = (Diagnostico)campos.get("diagnosticoEgreso");
		
		HashMap centrosCosto = (HashMap)campos.get("centrosCosto");
		int codigoCentroCosto = ConstantesBD.codigoNuncaValido;
		if(!UtilidadTexto.isEmpty(centrosCosto.get("codigo")+""))
			codigoCentroCosto = Utilidades.convertirAEntero(centrosCosto.get("codigo").toString());
		//*********************************************************************************
		
		
		
		String mortalidadSexo = "SELECT "+
			"'comodin' as comodin, "+
			"CASE WHEN e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+" THEN 1 else 0 END as mortalidad, "+
			"1 as egreso, "+
			"getdescripcionsexo(p.sexo) AS sexo "+ 
			"FROM egresos e "+ 
			"INNER JOIN cuentas c ON(c.id = e.cuenta) " +
			"INNER JOIN centros_costo cc ON(cc.codigo = c.area and cc.centro_atencion = "+codigoCentroAtencion+") "+ 
			"INNER JOIN pacientes pac ON(pac.codigo_paciente = c.codigo_paciente) "+ 
			"INNER JOIN personas p ON(p.codigo = pac.codigo_paciente) " +
			"LEFT OUTER JOIN evoluciones evo ON(evo.codigo = e.evolucion)  "+ 
			"WHERE " +
			"(" +
				//Egreso administrativo
				"(e.fecha_egreso IS NOT NULL AND to_char(e.fecha_egreso, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.usuario_responsable IS NOT NULL) " +
				" OR " +
				//Egreso medico
				"(evo.fecha_evolucion is not null and to_char(evo.fecha_evolucion, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.destino_salida is not null)" +
			") and e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+"  "+
			(!acronimoDiagMuerte.equals("")?" and e.diagnostico_muerte = '"+acronimoDiagMuerte+"' ":"")+
			(!diagEgreso.getAcronimo().equals("")?" and e.diagnostico_principal = '"+diagEgreso.getAcronimo()+"' ":"")+
			(codigoViaIngreso!=ConstantesBD.codigoNuncaValido?" and c.via_ingreso = "+codigoViaIngreso+" ":"")+
			(codigoCentroCosto>0?" and c.area = "+codigoCentroCosto+" ":"")+
			(codigoSexo!=ConstantesBD.codigoNuncaValido?" and p.sexo = "+codigoSexo+" ":"")+
			(edad!=ConstantesBD.codigoNuncaValido?" and getedad2(p.fecha_nacimiento,coalesce(e.fecha_egreso,evo.fecha_evolucion)) = "+edad+" ":"");
		
		return mortalidadSexo;
		
	}
	//*******************************************************************************************************************
	//******************CONSULTA REPORTE TASA MORTALIDAD GLOBAL************************************************************
	/**
	 * Método para carga los datos de la tasa de mortalidad global de hospitalizados
	 */
	public static HashMap<String, Object> obtenerDatosTasaMortalidadGlobalHospitalizados(Connection con,HashMap campos)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try
		{
			//***************SE TOMAN LOS PARÁMETROS*****************************************
			int codigoCentroAtencion = Utilidades.convertirAEntero(campos.get("codigoCentroAtencion").toString());
			String fechaInicial = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString());
			String fechaFinal = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString());
			int codigoSexo = Utilidades.convertirAEntero(campos.get("codigoSexo").toString());
			int edad = Utilidades.convertirAEntero(campos.get("edad").toString());
			int codigoInstitucion = Utilidades.convertirAEntero(campos.get("codigoInstitucion").toString());
			
			HashMap diagMuerte = (HashMap)campos.get("diagnosticosMuerte");
			String acronimoDiagMuerte = "";
			if(!UtilidadTexto.isEmpty(diagMuerte.get("valor")+""))
				acronimoDiagMuerte = diagMuerte.get("valor").toString().split(ConstantesBD.separadorSplit)[0];
			
			Diagnostico diagEgreso = (Diagnostico)campos.get("diagnosticoEgreso");
			
			HashMap centrosCosto = (HashMap)campos.get("centrosCosto");
			int codigoCentroCosto = ConstantesBD.codigoNuncaValido;
			if(!UtilidadTexto.isEmpty(centrosCosto.get("codigo")+""))
				codigoCentroCosto = Utilidades.convertirAEntero(centrosCosto.get("codigo").toString());
			//*******************************************************************************
			
			
			String mortalidadGlobal = "SELECT t.tiempo_estancia,t.paciente,t.muerte,t.mes_egreso,t.mes,re.nombre_etiqueta as nombre_etiqueta,re.orden as orden FROM ( "+
					"SELECT "+ 
					"gettiempoestancia(ah.fecha_admision,substr(ah.hora_admision,0,6),coalesce(e.fecha_egreso,evo.fecha_evolucion),substr(coalesce(e.hora_egreso,evo.hora_evolucion),0,6),"+ConstantesBD.codigoViaIngresoHospitalizacion+")*60 as tiempo_estancia, "+
					"1 as paciente, "+
					"CASE WHen e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+" then 1 else 0 end as muerte, "+
					"to_char(coalesce(e.fecha_egreso,evo.fecha_evolucion),'YYYY-MM') as mes_egreso, "+
					"getnombremes(to_char(coalesce(e.fecha_egreso,evo.fecha_evolucion),'MM')) || ' ' || to_char(coalesce(e.fecha_egreso,evo.fecha_evolucion),'YYYY') AS mes "+ 
					"FROM egresos e "+ 
					"INNER JOIN cuentas c ON(c.id = e.cuenta) "+
					"INNER JOIN admisiones_hospi ah ON(ah.cuenta = c.id ) "+
					"INNER JOIN centros_costo cc ON(cc.codigo = c.area AND cc.centro_atencion = "+codigoCentroAtencion+") "+
					"INNER JOIN pacientes pac ON(pac.codigo_paciente = c.codigo_paciente) "+ 
					"INNER JOIN personas p ON(p.codigo = pac.codigo_paciente) " +
					"LEFT OUTER JOIN evoluciones evo ON(evo.codigo = e.evolucion)  "+ 
					"WHERE " +
					"(" +
						//Egreso administrativo
						"(e.fecha_egreso IS NOT NULL AND to_char(e.fecha_egreso, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.usuario_responsable IS NOT NULL) " +
						" OR " +
						//Egreso medico
						"(evo.fecha_evolucion is not null and to_char(evo.fecha_evolucion, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.destino_salida is not null)" +
					") and e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+"  "+
					(!acronimoDiagMuerte.equals("")?" and e.diagnostico_muerte = '"+acronimoDiagMuerte+"' ":"")+
					(!diagEgreso.getAcronimo().equals("")?" and e.diagnostico_principal = '"+diagEgreso.getAcronimo()+"' ":"")+
					" and c.via_ingreso = "+ConstantesBD.codigoViaIngresoHospitalizacion+" and c.tipo_paciente =  '"+ConstantesBD.tipoPacienteHospitalizado+"' "+
					(codigoCentroCosto>0?" and c.area = "+codigoCentroCosto+" ":"")+
					(codigoSexo!=ConstantesBD.codigoNuncaValido?" and p.sexo = "+codigoSexo+" ":"")+
					(edad!=ConstantesBD.codigoNuncaValido?" and getedad2(p.fecha_nacimiento,coalesce(e.fecha_egreso,evo.fecha_evolucion)) = "+edad+" ":"")+
				") t "+ 
				"INNER JOIN rangos_estadisticos re ON(re.rango_inicial <= t.tiempo_estancia AND re.rango_final >= t.tiempo_estancia and re.institucion = "+codigoInstitucion+" and re.activo = '"+ConstantesBD.acronimoSi+"' and re.reporte = "+ConstantesBDManejoPaciente.tipoReporteMortalidadGlobal+") "+ 
				"order by re.orden,t.mes_egreso";
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(mortalidadGlobal)),true,true);
			
			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerDatosTasaMortalidadGlobal: "+e);
		}
		return resultados;
	}
	
	/**
	 * Método para obtener el total de egresos
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int obtenerTotalEgresos(Connection con,HashMap campos)
	{
		int totalEgresos = 0;
		try
		{
			//***************SE TOMAN LOS PARÁMETROS*****************************************
			int codigoCentroAtencion = Utilidades.convertirAEntero(campos.get("codigoCentroAtencion").toString());
			String fechaInicial = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString());
			String fechaFinal = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString());
			int codigoSexo = Utilidades.convertirAEntero(campos.get("codigoSexo").toString());
			int codigoViaIngreso = Utilidades.convertirAEntero(campos.get("codigoViaIngreso").toString());
			int edad = Utilidades.convertirAEntero(campos.get("edad").toString());
			
			
			HashMap diagMuerte = (HashMap)campos.get("diagnosticosMuerte");
			String acronimoDiagMuerte = "";
			if(!UtilidadTexto.isEmpty(diagMuerte.get("valor")+""))
				acronimoDiagMuerte = diagMuerte.get("valor").toString().split(ConstantesBD.separadorSplit)[0];
			
			Diagnostico diagEgreso = (Diagnostico)campos.get("diagnosticoEgreso");
			
			HashMap centrosCosto = (HashMap)campos.get("centrosCosto");
			int codigoCentroCosto = ConstantesBD.codigoNuncaValido;
			if(!UtilidadTexto.isEmpty(centrosCosto.get("codigo")+""))
				codigoCentroCosto = Utilidades.convertirAEntero(centrosCosto.get("codigo").toString());
			
			String mesEgreso = campos.get("mesEgreso").toString();
			//*******************************************************************************
			
			String consulta = "SELECT "+ 
				"count(1) as num_egresos "+ 
				"FROM egresos e "+ 
				"INNER JOIN cuentas c ON(c.id = e.cuenta) " +
				"INNER JOIN centros_costo cc ON(cc.codigo = c.area AND cc.centro_atencion = "+codigoCentroAtencion+") "+ 
				"INNER JOIN pacientes pac ON(pac.codigo_paciente = c.codigo_paciente) "+ 
				"INNER JOIN personas p ON(p.codigo = pac.codigo_paciente) " +
				"LEFT OUTER JOIN evoluciones evo ON(evo.codigo = e.evolucion)  "+ 
				"WHERE " +
				"(" +
					//Egreso administrativo
					"(e.fecha_egreso IS NOT NULL AND to_char(e.fecha_egreso, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.usuario_responsable IS NOT NULL) " +
					" OR " +
					//Egreso medico
					"(evo.fecha_evolucion is not null and to_char(evo.fecha_evolucion, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.destino_salida is not null)" +
				") and e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+"  "+
				" and to_char(coalesce(e.fecha_egreso,evo.fecha_evolucion),'YYYY-MM') = '"+mesEgreso+"' "+
				(!acronimoDiagMuerte.equals("")?" and e.diagnostico_muerte = '"+acronimoDiagMuerte+"' ":"")+
				(!diagEgreso.getAcronimo().equals("")?" and e.diagnostico_principal = '"+diagEgreso.getAcronimo()+"' ":"")+
				" and c.via_ingreso = "+codigoViaIngreso+" "+
				(codigoCentroCosto>0?" and c.area = "+codigoCentroCosto+" ":"")+
				(codigoSexo!=ConstantesBD.codigoNuncaValido?" and p.sexo = "+codigoSexo+" ":"")+
				(edad!=ConstantesBD.codigoNuncaValido?" and getedad2(p.fecha_nacimiento,coalesce(e.fecha_egreso,evo.fecha_evolucion)) = "+edad+" ":"");
			
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			if(rs.next())
				totalEgresos = rs.getInt("num_egresos");
			
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerTotalEgresos: "+e);
		}
		return totalEgresos;
	}
	
	/**
	 * Método para obtener datos de la tasa de mortalidad global de las vias de ingreso
	 * que no manejan rangos de tiempos
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap<String, Object> obtenerDatosTasaMortalidadGlobalSinRangos(Connection con,HashMap campos)
	{
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		try
		{
			///***************SE TOMAN LOS PARÁMETROS*****************************************
			int codigoCentroAtencion = Utilidades.convertirAEntero(campos.get("codigoCentroAtencion").toString());
			String fechaInicial = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString());
			String fechaFinal = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString());
			int codigoSexo = Utilidades.convertirAEntero(campos.get("codigoSexo").toString());
			int codigoViaIngreso = Utilidades.convertirAEntero(campos.get("codigoViaIngreso").toString());
			String codigoTipoPaciente = campos.get("codigoTipoPaciente").toString();
			int edad = Utilidades.convertirAEntero(campos.get("edad").toString());
			
			
			HashMap diagMuerte = (HashMap)campos.get("diagnosticosMuerte");
			String acronimoDiagMuerte = "";
			if(!UtilidadTexto.isEmpty(diagMuerte.get("valor")+""))
				acronimoDiagMuerte = diagMuerte.get("valor").toString().split(ConstantesBD.separadorSplit)[0];
			
			Diagnostico diagEgreso = (Diagnostico)campos.get("diagnosticoEgreso");
			
			HashMap centrosCosto = (HashMap)campos.get("centrosCosto");
			int codigoCentroCosto = ConstantesBD.codigoNuncaValido;
			if(!UtilidadTexto.isEmpty(centrosCosto.get("codigo")+""))
				codigoCentroCosto = Utilidades.convertirAEntero(centrosCosto.get("codigo").toString());
			//*******************************************************************************
			
			String consulta = "SELECT sum(t.paciente) as total_egresos,sum(t.muerte) as total_mortalidad,t.mes_egreso,t.mes FROM "+ 
				"( "+
					"SELECT "+ 
					"1 as paciente, "+
					"CASE WHen e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+" then 1 else 0 end as muerte, "+
					"to_char(coalesce(e.fecha_egreso,evo.fecha_evolucion),'YYYY-MM') as mes_egreso, "+
					"getnombremes(to_char(coalesce(e.fecha_egreso,evo.fecha_evolucion),'MM')) || ' ' || to_char(coalesce(e.fecha_egreso,evo.fecha_evolucion),'YYYY') AS mes "+ 
					"FROM egresos e "+ 
					"INNER JOIN cuentas c ON(c.id = e.cuenta) " +
					"INNER JOIN centros_costo cc ON(cc.codigo = c.area and cc.centro_atencion = "+codigoCentroAtencion+") "+ 
					"INNER JOIN pacientes pac ON(pac.codigo_paciente = c.codigo_paciente) "+ 
					"INNER JOIN personas p ON(p.codigo = pac.codigo_paciente) " +
					"LEFT OUTER JOIN evoluciones evo ON(evo.codigo = e.evolucion)  "+ 
					"WHERE " +
					"(" +
					//Egreso administrativo
					"(e.fecha_egreso IS NOT NULL AND to_char(e.fecha_egreso, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.usuario_responsable IS NOT NULL) " +
					" OR " +
					//Egreso medico
					"(evo.fecha_evolucion is not null and to_char(evo.fecha_evolucion, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.destino_salida is not null)" +
					")   "+
					(!acronimoDiagMuerte.equals("")?" and e.diagnostico_muerte = '"+acronimoDiagMuerte+"' ":"")+
					(!diagEgreso.getAcronimo().equals("")?" and e.diagnostico_principal = '"+diagEgreso.getAcronimo()+"' ":"")+
					" and c.via_ingreso = "+codigoViaIngreso+" "+
					(!codigoTipoPaciente.equals("")?" and c.tipo_paciente = '"+codigoTipoPaciente+"' ":"")+
					(codigoCentroCosto>0?" and c.area = "+codigoCentroCosto+" ":"")+
					(codigoSexo!=ConstantesBD.codigoNuncaValido?" and p.sexo = "+codigoSexo+" ":"")+
					(edad!=ConstantesBD.codigoNuncaValido?" and getedad2(p.fecha_nacimiento,coalesce(e.fecha_egreso,evo.fecha_evolucion)) = "+edad+" ":"")+
				") t GROUP BY t.mes_egreso,t.mes ORDER BY t.mes_egreso";
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(st.executeQuery(consulta)), true, true);
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerDatosTasaMortalidadGlobalSinRangos: "+e);
		}
		return resultados;
	}
	//**********************************************************************************************************************
	//******************CONSULTA REPORTE MORTALIDAD POR RANGO DE TIEMPO Y CENTRO DE COSTO***********************************
	/**
	 * Método para obtener la consultar del reporte de mortalidad rango de tiempo y centro de costo
	 */
	public static String obtenerConsultaMortalidadRangoTiempoCentroCosto(HashMap campos)
	{
		//***********SE TOMAN LOS PARÁMETROS******************************************************
		int codigoCentroAtencion = Utilidades.convertirAEntero(campos.get("codigoCentroAtencion").toString());
		String fechaInicial = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaInicial").toString());
		String fechaFinal = UtilidadFecha.conversionFormatoFechaABD(campos.get("fechaFinal").toString());
		int codigoViaIngreso = Utilidades.convertirAEntero(campos.get("codigoViaIngreso").toString());
		int codigoInstitucion = Utilidades.convertirAEntero(campos.get("codigoInstitucion").toString());
		int codigoSexo = Utilidades.convertirAEntero(campos.get("codigoSexo").toString());
		
		
		HashMap diagMuerte = (HashMap)campos.get("diagnosticosMuerte");
		String acronimoDiagMuerte = "";
		if(!UtilidadTexto.isEmpty(diagMuerte.get("valor")+""))
			acronimoDiagMuerte = diagMuerte.get("valor").toString().split(ConstantesBD.separadorSplit)[0];
		
		Diagnostico diagEgreso = (Diagnostico)campos.get("diagnosticoEgreso");
		
		HashMap centrosCosto = (HashMap)campos.get("centrosCosto");
		String listadoCentrosCosto = "";
		for(int i=0;i<Utilidades.convertirAEntero(centrosCosto.get("numRegistros").toString());i++)
			if(UtilidadTexto.getBoolean(centrosCosto.get("checkbox_"+i).toString()))
				listadoCentrosCosto += (listadoCentrosCosto.equals("")?"":",") + centrosCosto.get("codigo_"+i);
		//******************************************************************************
		
		String rangoTiempoCentroCosto = "SELECT t.\"tiempo_estancia\",t.\"paciente\",t.\"muerte\",t.\"centro_costo\",re.nombre_etiqueta as nombre_etiqueta,re.orden as orden FROM " +
			"( "+
				"SELECT "+ 
				"gettiempoestancia(getfechaingreso(c.id,c.via_ingreso),substr(gethoraingreso(c.id,c.via_ingreso),0,6),coalesce(e.fecha_egreso,evo.fecha_evolucion),substr(coalesce(e.hora_egreso,evo.hora_evolucion),0,6),"+ConstantesBD.codigoViaIngresoUrgencias+")*60 as tiempo_estancia, "+
				"1 as paciente, "+
				"CASE WHen e.estado_salida = "+ValoresPorDefecto.getValorTrueParaConsultas()+" then 1 else 0 end as muerte, "+
				"getnomcentrocosto(c.area) as centro_costo "+ 
				"FROM egresos e "+ 
				"INNER JOIN cuentas c ON(c.id = e.cuenta) "+ 
				"INNER JOIN centros_costo cc ON(cc.codigo = c.area AND cc.centro_atencion = "+codigoCentroAtencion+") "+ 
				"INNER JOIN pacientes pac ON(pac.codigo_paciente = c.codigo_paciente) "+ 
				"INNER JOIN personas p ON(p.codigo = pac.codigo_paciente) " +
				"LEFT OUTER JOIN evoluciones evo ON(evo.codigo = e.evolucion) "+ 
				"WHERE " +
				"(" +
				//Egreso administrativo
				"(e.fecha_egreso IS NOT NULL AND to_char(e.fecha_egreso, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.usuario_responsable IS NOT NULL) " +
				" OR " +
				//Egreso medico
				"(evo.fecha_evolucion is not null and to_char(evo.fecha_evolucion, 'YYYY-MM-DD') BETWEEN '"+fechaInicial+"' and '"+fechaFinal+"' and e.destino_salida is not null)" +
				")   "+
				(!acronimoDiagMuerte.equals("")?" and e.diagnostico_muerte = '"+acronimoDiagMuerte+"' ":"")+
				(!diagEgreso.getAcronimo().equals("")?" and e.diagnostico_principal = '"+diagEgreso.getAcronimo()+"' ":"")+
				(codigoViaIngreso!=ConstantesBD.codigoNuncaValido?" and c.via_ingreso = "+codigoViaIngreso+" ":"")+
				" and c.area IN ("+listadoCentrosCosto+") "+
				(codigoSexo!=ConstantesBD.codigoNuncaValido?" and p.sexo = "+codigoSexo+" ":"")+
			") t "+ 
			"INNER JOIN rangos_estadisticos re ON(re.rango_inicial <= t.\"tiempo_estancia\" AND re.rango_final >= t.\"tiempo_estancia\" and re.institucion = "+codigoInstitucion+" and re.activo = '"+ConstantesBD.acronimoSi+"' and re.reporte = "+ConstantesBDManejoPaciente.tipoReporteMortalidadRangoTiempoCentroCosto+") "+ 
			"order by t.\"centro_costo\",re.orden";
		
		logger.info("rangoTiempoCentroCosto: "+rangoTiempoCentroCosto);
		return rangoTiempoCentroCosto;
	}
	//**********************************************************************************************************************
}
