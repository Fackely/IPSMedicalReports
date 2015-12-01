package com.princetonsa.dao.sqlbase.ordenesmedicas;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosString;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.facturacion.UtilidadesFacturacion;

public class SqlBaseResponderConsultasEntSubcontratadasDao {

	private static Logger logger = Logger.getLogger(SqlBaseResponderConsultasEntSubcontratadasDao.class);
	
	
	/**
	 * Cadena Sql para realizar la consulta de las ordenes a responder
	 */
	private static String consultaOrdenesaResponder= "SELECT " +
			"sol.numero_solicitud AS numerosolicitud, " +
		    "sol.consecutivo_ordenes_medicas AS consecutivordenes, " +     		
		    "to_char(sol.fecha_solicitud, 'YYYY-MM-DD') AS fechasolicitud, " +
		    "sol.hora_solicitud AS horasolicitud, " +
		    "sol.urgente AS esurgente, " +
		    "cu.codigo_paciente AS codpaciente,  " +
			"getNombrePersona(cu.codigo_paciente) AS nombrepaciente, " +
			"getTipoId(cu.codigo_paciente) AS tipoidpaciente, " +
			"getIdentificacionPaciente(cu.codigo_paciente) AS identificacionpaciente, " +
			"ing.centro_atencion  AS centroatencion , " +
			"getNomCentroAtencion(ing.centro_atencion) AS nombrecentroantencion,  " +
			"solinter.codigo_servicio_solicitado AS codservicio, " +
			"getnombreservicio(solinter.codigo_servicio_solicitado, " +ConstantesBD.separadorSplit+" ) AS nombreservicio " +
			
			"FROM solicitudes sol " +
			"INNER JOIN solicitudes_consulta solinter ON (solinter.numero_solicitud = sol.numero_solicitud) " +
			"INNER JOIN servicios serv ON (serv.codigo = solinter.codigo_servicio_solicitado) " +
			"INNER JOIN cuentas cu ON (cu.id = sol.cuenta) " +
			"INNER JOIN ingresos ing ON (ing.id = cu.id_ingreso) " +
			
			// PermitirAutorizarDiferenteDeSolicitudes
			"INNER JOIN auto_entsub_solicitudes aess ON (aess.numero_solicitud=sol.numero_solicitud) " +
			"INNER JOIN autorizaciones_entidades_sub autor ON (autor.consecutivo = aess.autorizacion_ent_sub AND autor.estado = '"+ConstantesIntegridadDominio.acronimoAutorizado+"' AND autor.tipo = '"+ConstantesIntegridadDominio.acronimoExterna+"' )  " +  
			
			"WHERE cu.codigo_paciente = ? AND autor.entidad_autorizada_sub = ? AND serv.especialidad IN ("+ConstantesBD.separadorSplitComplejo +") AND sol.estado_historia_clinica = "+ConstantesBD.codigoEstadoHCSolicitada + " ";
	
	
	public static ArrayList<DtoSolicitudesSubCuenta> obtenerListadoSolicitudes(int codigoPersona, int entidadSubcontratada, String tar, String especialidadesProf) 
	{
		
		ArrayList<DtoSolicitudesSubCuenta> arraySolicitudes=new ArrayList<DtoSolicitudesSubCuenta>();
		String cadenaConsulta = consultaOrdenesaResponder;
		int codigoPaciente = codigoPersona;
		cadenaConsulta=cadenaConsulta.replace(ConstantesBD.separadorSplit, tar);
		cadenaConsulta=cadenaConsulta.replace( ConstantesBD.separadorSplitComplejo, especialidadesProf);
		Connection con = null;
		con = UtilidadBD.abrirConexion();		
		
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1,codigoPaciente);
			ps.setInt(2,entidadSubcontratada);
			
			logger.info("\n CADENA CONSULTA SOLICITUDES >>  cadena >> "+cadenaConsulta+"  CodigoPaciente >> "+codigoPaciente);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoSolicitudesSubCuenta dto=new DtoSolicitudesSubCuenta();
				dto.setCodigo(rs.getString("numerosolicitud")==null?"":rs.getString("numerosolicitud"));
				dto.setNumeroSolicitud(rs.getString("consecutivordenes")==null?"":rs.getString("consecutivordenes"));
				dto.setFechaSolicitud(rs.getString("fechasolicitud"));
				dto.setHoraSolicitud(rs.getString("horasolicitud"));
				dto.setServicio(new InfoDatosString(rs.getString("codservicio")==null?"":rs.getString("codservicio").trim(),rs.getString("nombreservicio")==null?"":rs.getString("nombreservicio").trim()));	 
				dto.setEspecialidadServicioSolicitud(UtilidadesFacturacion.obtenerEspecialidadServicio(con,Utilidades.convertirAEntero(rs.getString("codservicio"))).getCodigo()+"");
				
				dto.setCodCentroAtencionIngreso(rs.getInt("centroatencion"));
				dto.setCentroAtencionIngreso(rs.getString("nombrecentroantencion")==null?"":rs.getString("nombrecentroantencion"));
				
				dto.setCodigoPaciente(rs.getInt("codpaciente"));
				dto.setNombrePaciente(rs.getString("nombrepaciente")==null?"":rs.getString("nombrepaciente"));
				dto.setTipoIdPaciente(rs.getString("tipoidpaciente")==null?"":rs.getString("tipoidpaciente"));
				dto.setNumeroIdPaciente(rs.getString("identificacionpaciente")==null?"":rs.getString("identificacionpaciente"));
				dto.setUrgenteSolicitud(rs.getBoolean("esurgente"));
				
				arraySolicitudes.add(dto);
				
			}
			ps.close();
			rs.close();
			
		}
		catch (Exception e) {			
			e.printStackTrace();
			logger.info("\n error en  consulta Solicitudes Pendientes por Autorizar >>  cadena >> "+cadenaConsulta+" ");
			
		}
		UtilidadBD.closeConnection(con);
		return arraySolicitudes;
	}

 
     /**
      * 
      * @param parametrosBusqueda
      * @param tar
      * @return
      */
 	public static ArrayList<DtoSolicitudesSubCuenta> obtenerSolicitudesXRango(HashMap parametrosBusqueda, String tar) {
		
 		
 		ArrayList<DtoSolicitudesSubCuenta> arraySolicitudes=new ArrayList<DtoSolicitudesSubCuenta>();
 		String cadenaConsulta=consultaOrdenesaResponder;
 		String cadenaParametros = new String("");
		cadenaConsulta=cadenaConsulta.replace(ConstantesBD.separadorSplit, tar);
		cadenaConsulta=cadenaConsulta.replace( ConstantesBD.separadorSplitComplejo, parametrosBusqueda.get("especialidadesProf").toString());
		cadenaConsulta=cadenaConsulta.replace("cu.codigo_paciente = ?", "1=1");
		cadenaConsulta=cadenaConsulta.replace("sol.estado_historia_clinica = "+ConstantesBD.codigoEstadoHCSolicitada, "sol.estado_historia_clinica IN ( "+ConstantesBD.codigoEstadoHCSolicitada + ", "+ConstantesBD.codigoEstadoHCTomaDeMuestra+", "+ConstantesBD.codigoEstadoHCEnProceso+") ");
		int entidadSubcontratada = ConstantesBD.codigoNuncaValido;
		
		if(parametrosBusqueda.containsKey("fechaInicial") && 
				parametrosBusqueda.containsKey("fechaFinal") && 
					!parametrosBusqueda.get("fechaInicial").toString().equals("") && 
						!parametrosBusqueda.get("fechaFinal").toString().equals(""))
		    {
			cadenaParametros += " to_char(sol.fecha_solicitud,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametrosBusqueda.get("fechaInicial").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametrosBusqueda.get("fechaFinal").toString())+"'  ";	
		    }
		
		
		if(parametrosBusqueda.containsKey("entidadSubcontratada") && parametrosBusqueda.containsKey("entidadSubcontratada") && !parametrosBusqueda.get("entidadSubcontratada").toString().equals(""))
				{
			    entidadSubcontratada=Utilidades.convertirAEntero(parametrosBusqueda.get("entidadSubcontratada").toString());
          		}
		
		cadenaConsulta=cadenaConsulta.replace("1=1",cadenaParametros);
		
		Connection con = null;
		con = UtilidadBD.abrirConexion();
		
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1,entidadSubcontratada);

			
			logger.info("\n CADENA CONSULTA SOLICITUDES >>  cadena >> "+cadenaConsulta);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoSolicitudesSubCuenta dto=new DtoSolicitudesSubCuenta();
				dto.setCodigo(rs.getString("numerosolicitud")==null?"":rs.getString("numerosolicitud"));
				dto.setNumeroSolicitud(rs.getString("consecutivordenes")==null?"":rs.getString("consecutivordenes"));
				dto.setFechaSolicitud(rs.getString("fechasolicitud"));
				dto.setHoraSolicitud(rs.getString("horasolicitud"));
				dto.setServicio(new InfoDatosString(rs.getString("codservicio")==null?"":rs.getString("codservicio").trim(),rs.getString("nombreservicio")==null?"":rs.getString("nombreservicio").trim()));	 
				dto.setEspecialidadServicioSolicitud(UtilidadesFacturacion.obtenerEspecialidadServicio(con,Utilidades.convertirAEntero(rs.getString("codservicio"))).getCodigo()+"");
				
				dto.setCodCentroAtencionIngreso(rs.getInt("centroatencion"));
				dto.setCentroAtencionIngreso(rs.getString("nombrecentroantencion")==null?"":rs.getString("nombrecentroantencion"));
				
				dto.setCodigoPaciente(rs.getInt("codpaciente"));
				dto.setNombrePaciente(rs.getString("nombrepaciente")==null?"":rs.getString("nombrepaciente"));
				dto.setTipoIdPaciente(rs.getString("tipoidpaciente")==null?"":rs.getString("tipoidpaciente"));
				dto.setNumeroIdPaciente(rs.getString("identificacionpaciente")==null?"":rs.getString("identificacionpaciente"));
				dto.setUrgenteSolicitud(rs.getBoolean("esurgente"));
				
				arraySolicitudes.add(dto);
				
			}
			ps.close();
			rs.close();
			
		}
		catch (Exception e) {			
			e.printStackTrace();
			logger.info("\n error en  consulta Solicitudes Pendientes por Autorizar >>  cadena >> "+cadenaConsulta+" ");
			
		}
		UtilidadBD.closeConnection(con);
		return arraySolicitudes;
	}
	
	
	
}
