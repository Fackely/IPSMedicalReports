/**
 * 
 */
package com.princetonsa.dao.sqlbase.odontologia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.ibm.icu.math.BigDecimal;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoServicios;
import com.servinte.axioma.dto.odontologia.DtoFiltroReporteCitasOdontologicas;
import com.servinte.axioma.dto.odontologia.DtoResultadoReporteCitasOdontologicas;
import com.servinte.axioma.dto.odontologia.DtoResultadoReporteCitasOdontologicas.DtoCentroAtencionReporte;
import com.servinte.axioma.dto.odontologia.DtoResultadoReporteCitasOdontologicas.DtoCentroAtencionReporte.DtoEspecialidadReporte;
import com.servinte.axioma.dto.odontologia.DtoResultadoReporteCitasOdontologicas.DtoCentroAtencionReporte.DtoEspecialidadReporte.DtoCitasReporte;
import com.servinte.axioma.dto.odontologia.DtoResultadoReporteCitasOdontologicas.DtoCentroAtencionReporte.DtoEspecialidadReporte.DtoTipoCitaEstadoCita;
import com.servinte.axioma.dto.odontologia.DtoResultadoReporteCitasOdontologicas.DtoCentroAtencionReporte.DtoEspecialidadReporte.DtoCitasReporte.DtoServiciosCita;

/**
 * @author armando
 *
 */
public class SqlBaseReporteCitasOdontologicasDao 
{

	private static String consultaDetallado="SELECT  " +
													" co.codigo_pk as codigocita," +
													" coalesce(ei.codigo,-1) as codigoempresainstitucion," +
													" coalesce(ei.razon_social,'') as descripcionempresainstitucion," +
													" ca.codigo as codigoca," +
													" ca.consecutivo as consecutivoca," +
													" ca.descripcion as descripcionca," +
													" ca.ciudad as codigociudad," +
													" ciu.descripcion as descripcionciudad," +
													" d.codigo_departamento as codigodepartamento," +
													" d.descripcion as descripciondepartamento," +
													" p.codigo_pais as codigopais," +
													" p.descripcion as descripcionpais," +
													" rc.codigo as codigoregion," +
													" rc.descripcion as descripcionregion," +
													" uc.codigo as codigounidadagenda," +
													" uc.descripcion as descripcionunidadagenda," +
													" e.codigo as codigoespecialidad," +
													" e.nombre as descripcionespecialidad," +
													" to_char(ao.fecha,'dd/mm/yyyy') as fecha," +
													" to_char(co.fecha_programacion,'dd/mm/yyyy') as fechaProgram," +
													" substr(co.hora_inicio,1,5) as hora," +
													" per.tipo_identificacion as tipoidpaciente," +
													" per.numero_identificacion as numeroidpaciente," +
													" per.primer_nombre as primernombre," +
													" coalesce(per.segundo_nombre,'') as segundonombre," +
													" per.primer_apellido as primerapellido," +
													" coalesce(per.segundo_apellido,'') as segundoapellido," +
													" per.telefono_fijo as telefono1," +
													" per.telefono_celular as telefono2," +
													" per.telefono as telefono3," +
													" getnombrepersona(ao.codigo_medico) as nombreprofesional, " +
													" co.tipo as tipocita," +
													" co.estado as estadocita," +
													" coalesce(co.motivo_cancelacion,-1) as codigomotivocancelacion," +
													" coalesce(mcc.descripcion,'') as descripcionmotivocancelacion," +
													" coalesce(mcc.tipo_cancelacion,-1) as tipocancelacion," +
													" perusu.primer_nombre as primernombreprof," +
													" coalesce(perusu.segundo_nombre,'') as segundonombreprof," +
													" perusu.primer_apellido as primerapellidoprof," +
													" coalesce(perusu.segundo_apellido,'') as segundoapellidoprof," +
													" co.usuario_modifica as usuariomodifica, " +
													"prof.primer_nombre as primerNombreProfe, " +
													"coalesce(prof.segundo_nombre,'') as segundoNombreProfe, " +
													"prof.primer_apellido as primerApellidoProfe, " +
													"coalesce(prof.segundo_apellido,'') as segundoApellidoProfe, "+
													"getnombrepersona(prof.codigo) as profesional" +
										" from citas_odontologicas co " +
										" left outer join agenda_odontologica ao on(ao.codigo_pk=co.agenda)  " +
										" left outer join centros_costo cc on (co.centro_costo=cc.codigo)  " +
										" left outer join centro_atencion ca on (cc.centro_atencion=ca.consecutivo) " +
										" left outer join ciudades ciu on (ciu.codigo_ciudad=ca.ciudad and ciu.codigo_departamento=ca.departamento and ciu.codigo_pais=ca.pais) " +
										" left outer join departamentos d on (ca.departamento=d.codigo_departamento) " +
										" left outer join paises p on(p.codigo_pais=ca.pais) " +
										" left outer join regiones_cobertura rc on (ca.region_cobertura=rc.codigo) " +
										" left outer join unidades_consulta uc on (ao.unidad_agenda=uc.codigo) " +
										" left outer join especialidades e on (e.codigo=uc.especialidad) " +
										" left outer join personas per on (per.codigo=co.codigo_paciente)  " +
										" left outer join personas perusu on (perusu.codigo=ao.codigo_medico) " +
										" left outer join empresas_institucion ei on(ca.empresa_institucion=ei.codigo) " +
										" left outer join motivos_cancelacion_cita mcc on (mcc.codigo=co.motivo_cancelacion)" +
										" left outer join usuarios usu on (usu.login = co.usuario_modifica)" +
										" left outer join personas prof on (usu.codigo_persona = prof.codigo)"+
										" where 1=1 ";
	
	/**
	 * 
	 */
	private static String orderByConsultaDetallada=" order by " +
											" ei.codigo," +
											" ei.razon_social," +
											" ca.codigo," +
											" ca.consecutivo," +
											" ca.descripcion," +
											" ca.ciudad," +
											" ciu.descripcion," +
											" d.codigo_departamento," +
											" d.descripcion," +
											" p.codigo_pais," +
											" p.descripcion," +
											" rc.codigo," +
											" rc.descripcion," +
											" uc.codigo," +
											" uc.descripcion," +
											" e.codigo," +
											" e.nombre," + 
											" ao.fecha asc," +
											" co.hora_inicio asc" ;
	
	/**
	 * 
	 * @param dtoFiltro
	 * @param sinAgenda 
	 * @param consecutivoCA 
	 * @return
	 */
	public static String construirFiltro(DtoFiltroReporteCitasOdontologicas dtoFiltro)
	{
		/*
		dtoFiltro.setServicios(forma.getServicios());
		*/
		String cadena=" ";
		
			if(!UtilidadTexto.isEmpty(dtoFiltro.getFechaInicial()) && !UtilidadTexto.isEmpty(dtoFiltro.getFechaFinal())){
				cadena = cadena + "and ((co.agenda IS NOT NULL AND ao.fecha between '"+UtilidadFecha.conversionFormatoFechaABD(dtoFiltro.getFechaInicial())+
				"' and '"+UtilidadFecha.conversionFormatoFechaABD(dtoFiltro.getFechaFinal())+"') OR " +
						"(co.agenda IS NULL AND co.fecha_programacion between '"+UtilidadFecha.conversionFormatoFechaABD(dtoFiltro.getFechaInicial())+"' " +
								"and '"+UtilidadFecha.conversionFormatoFechaABD(dtoFiltro.getFechaFinal())+"') )";
			}

			if(dtoFiltro.getEstadosCita().length>0)
			{
				cadena=cadena+" and co.estado in (";
				for(int i=0;i<dtoFiltro.getEstadosCita().length;i++)
				{
					if(i>0)
						cadena=cadena+",";
					cadena=cadena+"'"+dtoFiltro.getEstadosCita()[i]+"'";
				}
				cadena=cadena+")";
			}
			
			if(UtilidadTexto.getBoolean(dtoFiltro.getCancelacionCita()) && 
					dtoFiltro.getCancelacionCita().equals(ConstantesBD.acronimoSi))
			{
				cadena=cadena+" and co.motivo_cancelacion > 0";
			}
			if(dtoFiltro.getCanceladaPor()>=0 && 
					dtoFiltro.getCancelacionCita().equals(ConstantesBD.acronimoSi))
			{
				//si es la opcion de cancelado ambos, solo importa que este cancelada, no el tipo de cancelacion
				if(dtoFiltro.getCanceladaPor()==0)
				{
					cadena=cadena+" and co.motivo_cancelacion > 0";
				}
				else
				{
					cadena=cadena+" and mcc.tipo_cancelacion="+dtoFiltro.getCanceladaPor();
				}
			}
			if(dtoFiltro.getEspecialidad().getCodigo()>0)
			{
				cadena=cadena+" and e.codigo="+dtoFiltro.getEspecialidad().getCodigo();
			}
			if(dtoFiltro.getUnidadAgenda().getCodigo()>0)
			{
				cadena=cadena+" and uc.codigo="+dtoFiltro.getUnidadAgenda().getCodigo();
			}
			if(dtoFiltro.getProfesional().getCodigo()>0)
			{
				cadena=cadena+" and ao.codigo_medico="+dtoFiltro.getProfesional().getCodigo();
			}
				
			if(dtoFiltro.getCentroAtencion().getConsecutivo()>0)
				cadena=cadena+" and ca.consecutivo="+dtoFiltro.getCentroAtencion().getConsecutivo();
			if(!UtilidadTexto.isEmpty(dtoFiltro.getPais().getCodigoPais()))
				cadena=cadena+" and ca.pais='"+dtoFiltro.getPais().getCodigoPais()+"'";
			if(!UtilidadTexto.isEmpty(dtoFiltro.getCiudad().getCodigoCiudad()))
				cadena=cadena+" and ca.ciudad='"+dtoFiltro.getCiudad().getCodigoCiudad()+"'";
			if(!UtilidadTexto.isEmpty(dtoFiltro.getCiudad().getDepartamento().getCodigoDepartamento()))
				cadena=cadena+" and ca.departamento='"+dtoFiltro.getCiudad().getDepartamento().getCodigoDepartamento()+"'";
			if(dtoFiltro.getRegionesCobertura().getCodigo()>0)
				cadena=cadena+" and rc.codigo="+dtoFiltro.getRegionesCobertura().getCodigo();
			if(dtoFiltro.getEmpresaInstitucion().getCodigo().intValue()>0)
				cadena=cadena+" and ca.empresa_institucion="+dtoFiltro.getEmpresaInstitucion().getCodigo().intValue();
			
			if(dtoFiltro.getTiposCita().length>0)
			{
				cadena=cadena+" and co.tipo in (";
				for(int i=0;i<dtoFiltro.getTiposCita().length;i++)
				{
					if(i>0)
						cadena=cadena+",";
					cadena=cadena+"'"+dtoFiltro.getTiposCita()[i]+"'";
				}		
				cadena=cadena+")";
			}
			
			if(!UtilidadTexto.isEmpty(dtoFiltro.getUsuario().getLogin()))
			{
				cadena=cadena+" and co.usuario_modifica='"+dtoFiltro.getUsuario().getLogin()+"'";
			}
			if(dtoFiltro.getServicios().size()>0)
			{
				String cadenaServicios="-1";
				for(DtoServicios servicio:dtoFiltro.getServicios())
				{
					cadenaServicios=cadenaServicios+","+servicio.getCodigoServicio();
				}
				cadena=cadena+" and (select  count(servicio) from odontologia.servicios_cita_odontologica " +
						"where cita_odontologica=co.codigo_pk and servicio in ("+cadenaServicios+"))>0";
				
			}
			
			cadena=cadena+ "and co.codigo_pk not in (select cita_programada from citas_asociadas_a_programada)";
			
		return cadena;
	}
	
	/**
	 * 
	 * @param dto
	 * @return 
	 */
	public static ArrayList<DtoResultadoReporteCitasOdontologicas> consultarCitasOdontologiaDetallado(DtoFiltroReporteCitasOdontologicas dtoFiltro) 
	{
		ArrayList<DtoResultadoReporteCitasOdontologicas> resultado=new ArrayList<DtoResultadoReporteCitasOdontologicas>();
		Connection con = UtilidadBD.abrirConexion();
		try
		{
			String consulta=consultaDetallado+construirFiltro(dtoFiltro)+orderByConsultaDetallada;
			
			Log4JManager.info("***************************consulta:**********"+ consulta);
			
			PreparedStatementDecorator ps1=new PreparedStatementDecorator(con,consulta);
			Log4JManager.info("consulta\n"+ps1);
			ResultSetDecorator rs1=new ResultSetDecorator(ps1.executeQuery());
			int codigoEmpresaInstitucionAnterior=-2;
			int codigoCentroAtencionAnterior=-2;
			int codigoEspecialidadAnterior=-2;
			int codigoUnidadAgendaAnterior=-2;
			while(rs1.next())
			{
				DtoResultadoReporteCitasOdontologicas dto;
				DtoCentroAtencionReporte dtoCA;
				DtoEspecialidadReporte dtoEsp;
				
				//Cargando la info de empresa institucion
				if(rs1.getInt("codigoempresainstitucion")!=codigoEmpresaInstitucionAnterior)
				{
					dto=new DtoResultadoReporteCitasOdontologicas();
					dto.setCodigoEmpresaInstitucion(rs1.getInt("codigoempresainstitucion"));
					dto.setDescripcionEmpresaInstitucion(rs1.getString("descripcionempresainstitucion"));
					resultado.add(dto);
					
					//si es una nueva empresa, se deben inicializar las variables de anteriores
					codigoCentroAtencionAnterior=-2;
					codigoEspecialidadAnterior=-2;
					codigoUnidadAgendaAnterior=-2;
				}
				codigoEmpresaInstitucionAnterior=rs1.getInt("codigoempresainstitucion");
				dto=resultado.get(resultado.size()-1);
					
				//cargar el centro atencion.
				if(codigoCentroAtencionAnterior!=rs1.getInt("consecutivoca"))
				{
					dtoCA=dto.new DtoCentroAtencionReporte();
					dtoCA.setCodigoCentroAtencion(rs1.getString("codigoca"));
					dtoCA.setConsecutivoCentroAtencion(rs1.getInt("consecutivoca"));
					dtoCA.setDescripcionCentroAtencion(rs1.getString("descripcionca"));
					dtoCA.setCodigoCiudadCA(rs1.getInt("codigociudad"));
					dtoCA.setDescripcionCiudadCA(rs1.getString("descripcionciudad"));
					dtoCA.setCodigoDepartamento(rs1.getInt("codigodepartamento"));
					dtoCA.setDescripcionDepartamento(rs1.getString("descripciondepartamento"));
					dtoCA.setCodigoPais(rs1.getInt("codigopais"));
					dtoCA.setDescripcionPais(rs1.getString("descripcionpais"));
					dtoCA.setCodigoRegionCA(rs1.getInt("codigoregion"));
					dtoCA.setDescripcionRegionCA(rs1.getString("descripcionregion"));
					dto.getCentrosAtencion().add(dtoCA);

					//si es un nuevo centro atencion, se deben inicializar las variables de anteriores
					codigoEspecialidadAnterior=-2;
					codigoUnidadAgendaAnterior=-2;
				}
				codigoCentroAtencionAnterior=rs1.getInt("consecutivoca");
				
				dtoCA=dto.getCentrosAtencion().get(dto.getCentrosAtencion().size()-1);
				
				
				//Cargar las especialidades.
				if(codigoEspecialidadAnterior!=rs1.getInt("codigoespecialidad")||codigoUnidadAgendaAnterior!=rs1.getInt("codigounidadagenda"))
				{
					dtoEsp=dtoCA.new DtoEspecialidadReporte();
					dtoEsp.setCodigoUnidadAgenda(rs1.getInt("codigounidadagenda"));
					dtoEsp.setDescripcionUnidadAgenda(rs1.getString("descripcionunidadagenda"));
					dtoEsp.setCodigoEspecialidad(rs1.getInt("codigoespecialidad"));
					dtoEsp.setDescripcionEspecialidad(rs1.getString("descripcionespecialidad"));
					dtoCA.getEspecialidades().add(dtoEsp);
				}				

				codigoEspecialidadAnterior=rs1.getInt("codigoespecialidad");
				codigoUnidadAgendaAnterior=rs1.getInt("codigounidadagenda");

				dtoEsp=dtoCA.getEspecialidades().get(dtoCA.getEspecialidades().size()-1);
				
				//Cargar el resumido
				boolean encontreRegistro=false;
				for(DtoTipoCitaEstadoCita tipoEstadoCita:dtoEsp.getResumenCitas())
				{
					if(tipoEstadoCita.getEstadoCita().equals(rs1.getString("estadocita"))&&tipoEstadoCita.getTipoCita().equals(rs1.getString("tipocita")))
					{
						encontreRegistro=true;
						tipoEstadoCita.setNumeroCitas(tipoEstadoCita.getNumeroCitas()+1);
					}
				}
				if(!encontreRegistro)
				{
					DtoTipoCitaEstadoCita tipoEstadoCita=dtoEsp.new DtoTipoCitaEstadoCita();
					tipoEstadoCita.setEstadoCita(rs1.getString("estadocita"));
					tipoEstadoCita.setTipoCita(rs1.getString("tipocita"));
					tipoEstadoCita.setNumeroCitas(1);
					dtoEsp.getResumenCitas().add(tipoEstadoCita);
				}
				
				//Cargar las citas.
				DtoCitasReporte dtoCita=dtoEsp.new DtoCitasReporte();
				dtoCita.setCodigoCita(rs1.getInt("codigocita"));
				dtoCita.setCodigoMotivoCancelacion(rs1.getInt("codigomotivocancelacion"));
				dtoCita.setDescripcionMotivoCancelacion(rs1.getString("descripcionmotivocancelacion"));
				dtoCita.setEstadoCita(rs1.getString("estadocita"));
				dtoCita.setFecha(rs1.getString("fecha"));
				dtoCita.setHora(rs1.getString("hora"));
				dtoCita.setNombresPaciente(rs1.getString("primernombre")+" "+rs1.getString("segundonombre")+" "+rs1.getString("primerapellido")+" "+rs1.getString("segundoapellido"));
				dtoCita.setNombresProfesional(rs1.getString("nombreprofesional"));
				dtoCita.setPrimerNombrePac(rs1.getString("primernombre"));
				dtoCita.setSegundoNombrePac(rs1.getString("segundonombre"));
				dtoCita.setPrimerApellidoPac(rs1.getString("primerapellido"));
				dtoCita.setSegundoApellidoPac(rs1.getString("segundoapellido"));
				dtoCita.setPrimerNombreProf(rs1.getString("primernombreprof"));
				dtoCita.setSegundoNombreProf(rs1.getString("segundonombreprof"));
				dtoCita.setPrimerApellidoProf(rs1.getString("primerapellidoprof"));
				dtoCita.setSegundoApellidoProf(rs1.getString("segundoapellidoprof"));
				dtoCita.setNumeroIDPaciente(rs1.getString("numeroidpaciente"));
				dtoCita.setUsuario(rs1.getString("usuariomodifica"));
				
				if(UtilidadTexto.isEmpty(rs1.getString("telefono1")))
				{
					if(UtilidadTexto.isEmpty(rs1.getString("telefono2")))
					{
						if(!UtilidadTexto.isEmpty(rs1.getString("telefono3")))
						{
							dtoCita.setTelefono(rs1.getString("telefono3"));
						}
						else
						{
							dtoCita.setTelefono("-");
						}
					}
					else
					{
						dtoCita.setTelefono(rs1.getString("telefono2"));
					}
				}
				else
				{
					dtoCita.setTelefono(rs1.getString("telefono1"));
				}
				dtoCita.setTipoCita(rs1.getString("tipocita"));
				dtoCita.setTipoIDPaciente(rs1.getString("tipoidpaciente"));

				String consultaServicios= "SELECT servicio as codigoservicio,getnombreserviciotarifa2(servicio,"+
				ConstantesBD.codigoTarifarioCups+") as nombreservicio,getcodigopropservicio2(servicio,"+
				ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dtoFiltro.getInstitucion())+
				") as codigopropietario,getnombreserviciotarifa2(servicio,"+ValoresPorDefecto.getCodigoManualEstandarBusquedaServicios(dtoFiltro.getInstitucion())+
				") as descpropietario " +
				" from servicios_cita_odontologica " +
				" where cita_odontologica="+dtoCita.getCodigoCita() +
				" and activo = '"+ConstantesBD.acronimoSi+"'";
				PreparedStatementDecorator ps=new PreparedStatementDecorator(con, consultaServicios);
				
				Log4JManager.info("******consulta servicios:**********"+ consultaServicios);
				ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
				ArrayList<DtoServiciosCita> listaServicios=new ArrayList<DtoServiciosCita>();
				
				while(rs.next())
				{
					DtoServiciosCita servicioCita=dtoCita.new DtoServiciosCita();
					servicioCita.setCodigoPropietarioServicio(rs.getString("codigopropietario"));
					servicioCita.setDescripcionPropietarioServicio(rs.getString("descpropietario"));
					servicioCita.setCodigoServicio(rs.getInt("codigoservicio"));
					servicioCita.setDescripcionServicio(rs.getString("nombreservicio"));
					
					String consultaValorCita= "SELECT sum(valor_tarifa) as valorServicio " +
							"from odontologia.servicios_cita_odontologica " +
							"where cita_odontologica="+dtoCita.getCodigoCita()+
							"and activo = '"+ConstantesBD.acronimoSi+"'";
					PreparedStatementDecorator psValor=new PreparedStatementDecorator(con, consultaValorCita);
					
					Log4JManager.info("******consulta valor cita:**********"+ consultaValorCita);
					ResultSetDecorator rsValor=new ResultSetDecorator(psValor.executeQuery());
					double valor = 0;
					
					while(rsValor.next()){
						valor = rsValor.getDouble("valorServicio");
					}
					rsValor.close();
					psValor.close();
					
					servicioCita.setValorCita(valor);
					
					listaServicios.add(servicioCita);
				}
				rs.close();
				ps.close();
				
				dtoCita.setServicios(listaServicios);
				
				if (listaServicios != null && listaServicios.size() > 0) {
					BigDecimal valorCita = new BigDecimal(listaServicios.get(0).getValorCita());
					dtoCita.setValorCita(valorCita);
				}
				
				
				int tipoCancelacion=rs1.getInt("tipocancelacion");
				dtoCita.setTipoCancelacion(tipoCancelacion);
				if(tipoCancelacion>0)
				{
					if(tipoCancelacion==ConstantesBD.codigoEstadoCitaCanceladaInstitucion)
					{
						dtoEsp.getCitasConCancelacionInstitucion().add(dtoCita);
					}
					else if(tipoCancelacion==ConstantesBD.codigoEstadoCitaCanceladaPaciente)
					{
						dtoEsp.getCitasConCancelacionPaciente().add(dtoCita);
					}
				}
				else
				{
					
					if (dtoEsp.getCodigoEspecialidad()>=0 && !UtilidadTexto.isEmpty(dtoEsp.getDescripcionEspecialidad())) {
						dtoEsp.getCitasSinCancelacion().add(dtoCita);
					}else {
						
						dtoCita.setFecha(rs1.getString("fechaProgram"));
						dtoCita.setNombresProfesional(rs1.getString("profesional"));
						dtoCita.setPrimerNombreProf(rs1.getString("primerNombreProfe"));
						dtoCita.setSegundoNombreProf(rs1.getString("segundoNombreProfe"));
						dtoCita.setPrimerApellidoProf(rs1.getString("primerApellidoProfe"));
						dtoCita.setSegundoApellidoProf(rs1.getString("segundoApellidoProfe"));
						dtoEsp.setCodigoEspecialidad(ConstantesBD.codigoNuncaValido);
						dtoEsp.getCitasSinEspecialidad().add(dtoCita);
					}
				}
				
			}
			rs1.close();
			ps1.close();
		}
		catch (SQLException e)
		{
			Log4JManager.error("error", e);
		}
		finally
		{
			UtilidadBD.closeConnection(con);
		}
		
		return resultado;
	}
}
