package com.princetonsa.dao.sqlbase.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.InfoDatos;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.ValoresPorDefecto;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.dao.sqlbase.cargos.SqlBaseCargosDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoMontoCobroFactura;
import com.princetonsa.dto.facturacion.DtoResponsableFacturaOdontologica;
import com.princetonsa.dto.facturacion.DtoSolicitudesResponsableFacturaOdontologica;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.delegate.administracion.CentroAtencionDelegate;

/**
 * Sql gen. para las consultas de factura odontologica
 * @author axioma
 *
 */
public class SqlBaseFacturaOdontologicaDao 
{
	/**
	 * Manejador de logs
	 */
	private static Logger logger = Logger.getLogger(SqlBaseFacturaOdontologicaDao.class);
	
	/**
	 * Metodo que carga los responsables de la cuenta 
	 * @param cuenta
	 * @param centroAtencion
	 * @return
	 */
	public static ArrayList<DtoResponsableFacturaOdontologica> cargarResponsables(BigDecimal cuenta, int centroAtencion, int institucion, ArrayList<BigDecimal> filtroCargosFacturaAutomatica) throws BDException
	{
		ArrayList<DtoResponsableFacturaOdontologica> lista= new ArrayList<DtoResponsableFacturaOdontologica>();
		String consultaStr=	"SELECT " +
	 							"sc.sub_cuenta as subcuenta, " +
	 							"sc.convenio as codigoconvenio, " +
	 							"t.descripcion AS nombreResponsable, "+
	 							"t.numero_identificacion AS nitResponsable, "+
	 							"c.nombre AS nombreSimpleConvenio, "+
	 							"CASE WHEN c.tipo_regimen<>'"+ConstantesBD.codigoTipoRegimenParticular+"' then getNomConvContrato(sc.contrato) ||' - PRIORIDAD '||sc.nro_prioridad else '['|| c.nombre ||'] '|| getNomDeudorIngreso(sc.ingreso) ||' '|| getIdentificacionDeudorIngreso(sc.ingreso)  end AS nombreconvenio, " +
	 							"sc.contrato as codigocontrato, " +
	 							"co.numero_contrato as nombrecontrato, " +
	 							"coalesce(co.paciente_paga_atencion, '"+ConstantesBD.acronimoNo+"') as paciente_paga_atencion, " +
	 							"co.controla_anticipos as controla_anticipos, " +
	 							"sc.empresas_institucion as empresains, " +
	 							"coalesce(i.pac_entidades_subcontratadas, "+ConstantesBD.codigoNuncaValido+") as pacienteentidadsubcontratada, " +
	 							"CASE WHEN i.pac_entidades_subcontratadas IS NULL THEN "+ConstantesBD.codigoNuncaValido+" ELSE (SELECT pes.entidad_subcontratada FROM pac_entidades_subcontratadas pes WHERE pes.consecutivo=i.pac_entidades_subcontratadas) END AS entidadsubcontratada, " +
	 							"CASE WHEN c.tipo_regimen<>'"+ConstantesBD.codigoTipoRegimenParticular+"' then '"+ConstantesBD.acronimoNo+"' else '"+ConstantesBD.acronimoSi+"' end as  esparticular, " +
	 							"coalesce(sc.clasificacion_socioeconomica, "+ConstantesBD.codigoNuncaValido+") as clasificacionsocioeconomica, " +
	 							"getnombreestrato(sc.clasificacion_socioeconomica) as nomclassocioeconomica, " +
	 							"sc.monto_cobro as montocobro, " +
	 							//FIXME
	 							"to_char(current_date-10, 'DD/MM/YYYY') as fechavigenciamontocobro, " +
	 							""+ConstantesBD.codigoNuncaValido+" as porcentajemonto," +
	 							""+ConstantesBD.codigoNuncaValido+" as valormonto," +
	 							""+ConstantesBD.codigoNuncaValido+" as tipomonto," +
	 							"'' as tipoafiliado," +
	 							"to_char(cu.fecha_apertura, 'DD/MM/YYYY') as fechavigenciatope, " +
	 							"coalesce(c.formato_factura,0) as codigoformatoimpresion, " +
								"getnombreformatofactura (coalesce(c.formato_factura, 0)) as nombreformatoimpresion, " +
								"case when sc.empresas_institucion is null then (select coalesce(ins.pref_factura,'') from instituciones ins where ins.codigo="+institucion+") else (select coalesce(eins.pref_factura,'') from facturacion.empresas_institucion eins where eins.codigo=sc.empresas_institucion) end as prefijo, " +
								"case when sc.empresas_institucion is null then (select coalesce(ins.resolucion,'') from instituciones ins where ins.codigo="+institucion+") else (select coalesce(eins.resolucion,'') from facturacion.empresas_institucion eins where eins.codigo=sc.empresas_institucion) end as resolucion, " +
								"c.tipo_regimen as tiporegimen, " +
								"getnomtiporegimen(c.tipo_regimen) as nombretiporegimen, " +
								"cu.via_ingreso as viaingreso, " +
								"getnombreviaingreso(cu.via_ingreso) as nombreviaingreso " +
							"FROM " +
		 						"manejopaciente.sub_cuentas sc " +
		 						"INNER JOIN facturacion.convenios c ON(c.codigo=sc.convenio) " +
		 						"INNER JOIN facturacion.contratos co on (co.codigo=sc.contrato) " +
		 						"INNER JOIN ingresos i on (sc.ingreso= i.id) " +
		 						"INNER JOIN cuentas cu ON(cu.id_ingreso=sc.ingreso) " +
		 						"LEFT OUTER JOIN empresas e ON (e.codigo=c.empresa) "+
		 						"LEFT OUTER JOIN facturacion.terceros t ON (t.codigo = e.tercero) " +
		 					"WHERE cu.id=? " +
	 						"ORDER BY c.nombre ";
		 
		try
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			logger.info("llega a cargarResponsables---------->"+consultaStr+" -->"+cuenta);
			ps.setInt(1, cuenta.intValue());
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoResponsableFacturaOdontologica dto= new DtoResponsableFacturaOdontologica();
				dto.setContrato(new InfoDatosInt(rs.getInt("codigocontrato"), rs.getString("nombrecontrato")));
				dto.setConvenio(new InfoDatosInt(rs.getInt("codigoconvenio"), rs.getString("nombreconvenio")));
				dto.setSubCuenta(rs.getBigDecimal("subcuenta"));
				dto.setNombreResponsable(rs.getString("nombreResponsable"));
				dto.setNitResponsable(rs.getString("nitResponsable"));
				dto.setNombreSimpleConvenio(rs.getString("nombreSimpleConvenio"));
				/*
				 *Cargar lista de Solicitudes  
				 */
				dto.setListaSolicitudes(cargarSolicitudesResponsable(dto.getSubCuenta(), cuenta, centroAtencion, filtroCargosFacturaAutomatica));
				
				dto.setPacientePagaAtencionXMontoCobro(UtilidadTexto.getBoolean(rs.getString("paciente_paga_atencion")));
				dto.setControlaAnticipos(UtilidadTexto.getBoolean(rs.getString("controla_anticipos")));
				dto.setEmpresaInstitucion(rs.getDouble("empresains"));
				dto.setEntidadSubContratada(rs.getDouble("entidadsubcontratada"));
				dto.setPacienteEntidadSubContratada(rs.getDouble("pacienteentidadsubcontratada"));
				dto.setEsParticular(UtilidadTexto.getBoolean(rs.getString("esparticular")));
				dto.setEstratoSocial(new InfoDatosInt(rs.getInt("clasificacionsocioeconomica"), rs.getString("nomclassocioeconomica")));
				
				DtoMontoCobroFactura dtoMonto= new DtoMontoCobroFactura();
				dtoMonto.setCodigo(rs.getInt("montocobro"));
				dtoMonto.setFechaVigenciaInicial(rs.getString("fechavigenciamontocobro"));
				dtoMonto.setPorcentajeMontoCobro(rs.getDouble("porcentajemonto"));
				dtoMonto.setTipoMonto(rs.getInt("tipomonto"));
				dtoMonto.setValorMontoCobro(rs.getBigDecimal("valormonto"));
				dtoMonto.setTipoAfiliado(rs.getString("tipoafiliado"));
				dto.setMontoCobro(dtoMonto);
				
				dto.setFechaVigenciaTopeCuenta(rs.getString("fechavigenciatope"));
				dto.setFormatoImpresion(new InfoDatosInt(rs.getInt("codigoformatoimpresion"), rs.getString("nombreformatoimpresion")));
				
				if(ValoresPorDefecto.getManejaConsecutivoFacturaPorCentroAtencionBool(institucion))
				{
					CentroAtencionDelegate centroDao=new CentroAtencionDelegate();
					CentroAtencion centroAtencion1=centroDao.findById(centroAtencion);
					dto.setPrefijoFactura(centroAtencion1.getPrefFactura());
					dto.setResolucion(centroAtencion1.getResolucion());
					//HibernateUtil.cerrarSession();
				}
				else
				{	
					dto.setPrefijoFactura(rs.getString("prefijo"));
					dto.setResolucion(rs.getString("resolucion"));
				}	
				dto.setTipoRegimen(new InfoDatos(rs.getString("tiporegimen"), rs.getString("nombretiporegimen")));
				
				dto.setViaIngreso(new InfoDatosInt(rs.getInt("viaingreso"), rs.getString("nombreviaingreso")));
				
				lista.add(dto);
			}	
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en cargarResponsables ", e);
			e.printStackTrace();
		}
		return lista; 
	}

	/**
	 * Metodo que carga las solicitudes de los responsables de la factura odontologica, carga info de
	 * historia clinica y de facturacion
	 * @param subCuenta
	 * @param cuenta
	 * @param centroAtencion
	 * @param filtroCargosFacturaAutomatica 
	 * @return
	 */
	private static ArrayList<DtoSolicitudesResponsableFacturaOdontologica> cargarSolicitudesResponsable(BigDecimal subCuenta, BigDecimal cuenta, int centroAtencion, ArrayList<BigDecimal> filtroCargosFacturaAutomatica) throws BDException 
	{
		ArrayList<DtoSolicitudesResponsableFacturaOdontologica> lista= new ArrayList<DtoSolicitudesResponsableFacturaOdontologica>();
		String consultaStr=	"SELECT " +
								"dc.codigo_detalle_cargo as coddetcargo, " +
								"s.estado_historia_clinica as codigohc, " +
								"ehc.nombre as nombreestadohc, " +
								"coalesce(s.pool,"+ConstantesBD.codigoNuncaValido+") as pool, " +
								" case when s.pool>0 then facturacion.getdescripcionpool(s.pool) else '' end as nombrepool, " +
								"s.consecutivo_ordenes_medicas as consecutivo_ordenes_medicas, " +
								"s.codigo_medico_responde as codigomedicoresponde, " +
								"administracion.getnombremedico(s.codigo_medico_responde) as nombremedicoresponde, " +
								"coalesce(m.tipo_liquidacion,'') as tipoliquidacion, " +
								"facturacion.getPorcentMedPoolXSol(s.numero_solicitud, "+ConstantesBD.codigoNuncaValido+", "+ConstantesBD.codigoNuncaValido+", "+ConstantesBD.codigoTipoSolicitudCirugia+", "+ConstantesBD.codigoNuncaValido+", "+ConstantesBD.codigoNuncaValido+") as porcentajeparticipacionpool " +
							"FROM " +
								"facturacion.det_cargos dc " +
								"INNER JOIN solicitudes s ON(s.numero_solicitud=dc.solicitud) " +
								"INNER JOIN estados_sol_his_cli ehc ON(ehc.codigo=s.estado_historia_clinica) " +
								"INNER JOIN administracion.centros_costo cc ON(cc.codigo=s.centro_costo_solicitado) " +
								"LEFT OUTER JOIN administracion.medicos m on(m.codigo_medico=s.codigo_medico_responde) " +
							"WHERE " +
								"dc.sub_cuenta=? " +
								"and dc.eliminado='"+ConstantesBD.acronimoNo+"' " +
								"and dc.facturado='"+ConstantesBD.acronimoNo+"' " +
								"and s.cuenta=? " +
								"and cc.centro_atencion=? " +
								"and s.tipo not in("+ConstantesBD.codigoTipoSolicitudCirugia+", "+ConstantesBD.codigoTipoSolicitudPaquetes+") " +
								"and dc.estado in("+ConstantesBD.codigoEstadoFCargada+", "+ConstantesBD.codigoEstadoFInactiva+", "+ConstantesBD.codigoEstadoFExento+") " +
								"and s.estado_historia_clinica in("+ConstantesBD.codigoEstadoHCRespondida+", "+ConstantesBD.codigoEstadoHCInterpretada+") ";
		
		if(filtroCargosFacturaAutomatica!=null && filtroCargosFacturaAutomatica.size()>0)
		{
			consultaStr+=" and dc.codigo_detalle_cargo in("+UtilidadTexto.convertirArrayBigDecimalACodigosSeparadosXComas(filtroCargosFacturaAutomatica)+") ";
		}
		
		try
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps=  new PreparedStatementDecorator(con, consultaStr);
			ps.setLong(1, subCuenta.longValue());
			ps.setInt(2, cuenta.intValue());
			ps.setInt(3, centroAtencion);
			logger.info("llega a cargarSolicitudesResponsable---------->"+ps.toString());
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoSolicitudesResponsableFacturaOdontologica dto= new DtoSolicitudesResponsableFacturaOdontologica();
				dto.setEstadoHC(new InfoDatosInt(rs.getInt("codigohc"), rs.getString("nombreestadohc")));
				
				/*
				 * Cargar Detalles de Cargo
				 */
				dto.setDetalleCargo(SqlBaseCargosDao.cargarDetalleCargo(con, rs.getBigDecimal("coddetcargo")));
				
				dto.setPool(new InfoDatosInt(rs.getInt("pool"), rs.getString("nombrepool")));
				dto.setConsecutivoSolicitud(rs.getString("consecutivo_ordenes_medicas"));
				dto.setMedicoResponde(new InfoDatosInt(rs.getInt("codigomedicoresponde"), rs.getString("nombremedicoresponde")));
				dto.setTipoLiquidacionMedico(rs.getString("tipoliquidacion"));
				dto.setPorcentajeParticipacionPool(rs.getDouble("porcentajeparticipacionpool"));
				lista.add(dto);
			}	
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en cargarResponsables ", e);
			e.printStackTrace();
		}
		return lista;
	}
	
	
}
