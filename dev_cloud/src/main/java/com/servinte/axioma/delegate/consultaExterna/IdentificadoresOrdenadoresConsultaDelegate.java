/**
 * 
 */
package com.servinte.axioma.delegate.consultaExterna;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

import org.axioma.util.log.Log4JManager;
import org.hibernate.SQLQuery;
import org.hibernate.type.DoubleType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.StringType;

import util.ConstantesBD;

import com.servinte.axioma.dto.administracion.ProfesionalSaludDto;
import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;
import com.servinte.axioma.dto.consultaExterna.GrupoOrdenadoresConsultaExternaDto;
import com.servinte.axioma.dto.consultaExterna.OrdenadoresConsultaExternaDto;
import com.servinte.axioma.dto.consultaExterna.OrdenadoresConsultaExternaPlanoDto;
import com.servinte.axioma.dto.facturacion.GrupoServicioDto;
import com.servinte.axioma.dto.inventario.ClaseInventarioDto;
import com.servinte.axioma.dto.ordenes.MedicamentoInsumoAutorizacionOrdenDto;
import com.servinte.axioma.dto.ordenes.ServicioAutorizacionOrdenDto;
import com.servinte.axioma.fwk.exception.BDException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.fwk.persistencia.interfaz.IPersistenciaSvc;
import com.servinte.axioma.fwk.persistencia.servicio.PersistenciaSvc;

/**
 * @author jeilones
 * @created 2/11/2012
 *
 */
public class IdentificadoresOrdenadoresConsultaDelegate {
	/**
	 * Atributo que representa el servicio para el acceso a la Base
	 * de datos 
	 */
	private IPersistenciaSvc persistenciaSvc;
	
	/**
	 * Consulta las unidades de consulta que estan asociadas a un centro de atencion especifico 
	 * 
	 * @param codigoCentroAtencion
	 * @return
	 * @throws BDException
	 * @author jeilones
	 * @created 20/11/2012
	 */
	@SuppressWarnings("unchecked")
	public List<DtoUnidadesConsulta> consultarUnidadAgenda(Integer codigoCentroAtencion) throws BDException{
		List<DtoUnidadesConsulta> listaUnidadAgenda=new ArrayList<DtoUnidadesConsulta>(0);
		try{
			
			persistenciaSvc= new PersistenciaSvc();
			Map<String, Object>parametros=new HashMap<String, Object>(0);
			if(codigoCentroAtencion!=null){
				parametros.put("codigoCentroAtencion", codigoCentroAtencion);
				listaUnidadAgenda=(List<DtoUnidadesConsulta>)persistenciaSvc.createNamedQuery("catalogoConsultaExterna.consultarUnidadAgendaPorCentroAtencion",parametros);
			}
			return listaUnidadAgenda;
			
			
			}
			catch (Exception e){
				Log4JManager.error(e);
				throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
			}		
	}
	
	/**
	 * Consulta los diferentes servicios agrupados por grupo servicio ordenados y verifica si tienen tarifa 
	 * @return
	 * @throws BDException
	 * @author jeilones
	 * @param esquemaTarifarioArticulos 
	 * @created 8/11/2012
	 */
	@SuppressWarnings("unchecked")
	public List<ServicioAutorizacionOrdenDto> verificarTarifasServicios(GrupoOrdenadoresConsultaExternaDto grupoOrdenadoresConsultaExternaDto, int esquemaTarifarioServicios)throws BDException{
		List<ServicioAutorizacionOrdenDto>servicios=new ArrayList<ServicioAutorizacionOrdenDto>(0);
		try{
			persistenciaSvc=new PersistenciaSvc();
			
			Map<String, Object>parametros=new LinkedHashMap<String, Object>(0);
			StringBuffer select=new StringBuffer("");
			select.append("SELECT ");
			select.append("RS.CODIGO_GS_CI, ")
			.append("RS.DESCRIPCION_GS_CI, ")
			.append("RS.CANTIDAD_SERV_MED, ")
			.append("RS.VALOR_TARIFA ")
			.append("FROM (");
			//--ORDENES AMB CITA ARTICULOS
			select.append("SELECT DISTINCT ")
				.append("GS.CODIGO               AS CODIGO_GS_CI, ")
				.append("GS.DESCRIPCION          AS DESCRIPCION_GS_CI, ")
				.append("SERV.CODIGO             AS CANTIDAD_SERV_MED, ")
				.append("CONSULTAEXTERNA.CALCULO_TARIFA_SERVICIO(SERV.CODIGO,:codigoEsquemaTarifario)   AS VALOR_TARIFA ")
			.append("FROM AGENDA AG ")
			.append("INNER JOIN CONSULTAEXTERNA.CITA CT ON CT.CODIGO_AGENDA = AG.CODIGO ")
			/*PARA PODER SABER QUE SERVICIOS FUERON ORDENADOS LA RELACION DE LA ORDEN CON LA CITA ES OBLIGATORIA*/
			.append("INNER JOIN ORDENES.ORDENES_AMBULATORIAS OA ON OA.CITA_ASOCIADA=CT.CODIGO ")
			.append("INNER JOIN CONSULTAEXTERNA.SERVICIOS_CITA SC ")
			.append("ON SC.CODIGO_CITA = CT.CODIGO ")
			/*.append("INNER JOIN MEDICOS MED ON MED.codigo_medico = AG.CODIGO_MEDICO ")
			.append("INNER JOIN PERSONAS PER ON PER.CODIGO = MED.CODIGO_MEDICO ")
			.append("INNER JOIN USUARIOS US ON US.CODIGO_PERSONA = PER.CODIGO ")*/
			.append("INNER JOIN USUARIOS US ON US.LOGIN = CT.USUARIO_MODIFICA_ESTADO ")
            .append("INNER JOIN PERSONAS PER ON PER.CODIGO = US.CODIGO_PERSONA ")
            .append("INNER JOIN MEDICOS MED ON MED.codigo_medico = PER.CODIGO ")
            
			.append("INNER JOIN ADMINISTRACION.MEDICOS_INSTITUCIONES MED_INS ")
			.append("ON MED_INS.CODIGO_MEDICO = MED.CODIGO_MEDICO ")
			.append("INNER JOIN CONSULTAEXTERNA.UNIDADES_CONSULTA UC ")
			.append("ON UC.CODIGO = CT.UNIDAD_CONSULTA ")
			.append("INNER JOIN ORDENES.DET_ORDEN_AMB_SERVICIO DOAS ")
			.append("ON DOAS.CODIGO_ORDEN = OA.CODIGO ")
			.append("INNER JOIN FACTURACION.SERVICIOS SERV ")
			.append("ON SERV.CODIGO=DOAS.SERVICIO ")
			.append("INNER JOIN FACTURACION.GRUPOS_SERVICIOS GS ")
			.append("ON GS.CODIGO = SERV.GRUPO_SERVICIO ");
			
			/*FILTROS*/
			parametros.put("codigoEsquemaTarifario", esquemaTarifarioServicios);
			parametros.put("codigoEstadoCita", ConstantesBD.codigoEstadoCitaAtendida);
			parametros.put("codigoInstitucion", grupoOrdenadoresConsultaExternaDto.getInstitucion());
			
			StringBuffer where=new StringBuffer("WHERE ");		
			where.append("OA.CITA_ASOCIADA IS NOT NULL  ")
				.append("AND CT.ESTADO_CITA = :codigoEstadoCita  ")
				.append("AND MED_INS.CODIGO_INSTITUCION = :codigoInstitucion  ");
			
			if(grupoOrdenadoresConsultaExternaDto.getFechaInicialAtencionCita()!=null){
				if(grupoOrdenadoresConsultaExternaDto.getFechaFinalAtencionCita()!=null){
					where.append("AND SC.FECHA_CITA BETWEEN :fechaInicial  AND :fechaFinal ");
					parametros.put("fechaInicial", grupoOrdenadoresConsultaExternaDto.getFechaInicialAtencionCita());
					parametros.put("fechaFinal", grupoOrdenadoresConsultaExternaDto.getFechaFinalAtencionCita());
				}else{
					where.append("AND SC.FECHA_CITA >= :fechaInicial ");
					parametros.put("fechaInicial", grupoOrdenadoresConsultaExternaDto.getFechaInicialAtencionCita());
				}
			}else{
				if(grupoOrdenadoresConsultaExternaDto.getFechaFinalAtencionCita()!=null){
					where.append("AND SC.FECHA_CITA <= :fechaFinal ");
					parametros.put("fechaFinal", grupoOrdenadoresConsultaExternaDto.getFechaFinalAtencionCita());
				}
			}
			
			if(grupoOrdenadoresConsultaExternaDto.getFiltroIdCentroAtencion()!=null&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdCentroAtencion().trim().isEmpty()
					&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdCentroAtencion().trim().equals(ConstantesBD.codigoNuncaValido+"")){
				select.append("INNER JOIN ADMINISTRACION.CENTRO_ATENCION CA ")
					.append("ON CA.CONSECUTIVO = AG.CENTRO_ATENCION ");
				where.append("AND CA.CONSECUTIVO = :codigoCentroAtencion ");
				parametros.put("codigoCentroAtencion", Integer.parseInt(grupoOrdenadoresConsultaExternaDto.getFiltroIdCentroAtencion().trim()));
			}
			
			if(grupoOrdenadoresConsultaExternaDto.getFiltroIdUnidadConsulta()!=null&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdUnidadConsulta().trim().isEmpty()
					&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdUnidadConsulta().trim().equals(ConstantesBD.codigoNuncaValido+"")){
				where.append("AND UC.CODIGO = :codigoUnidadConsulta ");
				parametros.put("codigoUnidadConsulta", Integer.parseInt(grupoOrdenadoresConsultaExternaDto.getFiltroIdUnidadConsulta().trim()));
			}
			
			if(grupoOrdenadoresConsultaExternaDto.getFiltroIdConvenio()!=null&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdConvenio().trim().isEmpty()
					&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdConvenio().trim().equals(ConstantesBD.codigoNuncaValido+"")){
				select.append("INNER JOIN SOLICITUDES SOL ON SOL.NUMERO_SOLICITUD = SC.NUMERO_SOLICITUD ")
					.append("INNER JOIN SOLICITUDES_SUBCUENTA SOL_SUBCUENTA ON SOL_SUBCUENTA.SOLICITUD=SOL.NUMERO_SOLICITUD ")
					.append("INNER JOIN SUB_CUENTAS SUB_CUENTA ON SUB_CUENTA.SUB_CUENTA=SOL_SUBCUENTA.SUB_CUENTA ")
					.append("INNER JOIN FACTURACION.CONVENIOS CONV ON CONV.CODIGO = SUB_CUENTA.CONVENIO ");
				where.append("AND CONV.CODIGO = :codigoConvenio ");
				parametros.put("codigoConvenio", Integer.parseInt(grupoOrdenadoresConsultaExternaDto.getFiltroIdConvenio().trim()));
			}
			
			if(grupoOrdenadoresConsultaExternaDto.getFiltroProfesionalSalud()!=null
					&&grupoOrdenadoresConsultaExternaDto.getFiltroProfesionalSalud().getLoginUsuario()!=null
					&&!grupoOrdenadoresConsultaExternaDto.getFiltroProfesionalSalud().getLoginUsuario().trim().equals(ConstantesBD.codigoNuncaValido+"")){
				where.append("AND US.LOGIN = :usuario  ");
				parametros.put("usuario", grupoOrdenadoresConsultaExternaDto.getFiltroProfesionalSalud().getLoginUsuario());
			}
			
			if(grupoOrdenadoresConsultaExternaDto.getFiltroIdGrupoServicio()!=null
					&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdGrupoServicio().trim().isEmpty()
					&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdGrupoServicio().trim().equals(ConstantesBD.codigoNuncaValido+"")){
				where.append("AND GS.CODIGO = :codigoGrupoServicio ");
				parametros.put("codigoGrupoServicio", Integer.parseInt(grupoOrdenadoresConsultaExternaDto.getFiltroIdGrupoServicio().trim()));
			}
			
			/*FIN FILTROS*/
			
			select.append(where)
				.append("GROUP BY ")
				.append("GS.CODIGO, ")
				.append("GS.DESCRIPCION, ")
				.append("SERV.CODIGO ");
			
			select.append(") RS WHERE RS.VALOR_TARIFA IS NULL");
			
			SQLQuery sqlQuery=persistenciaSvc.getSession().createSQLQuery(select.toString());
			Set<String>keyParemetros=parametros.keySet();
			for(String key:keyParemetros){
				sqlQuery.setParameter(key, parametros.get(key));
			}
			List<Object[]>resultado=sqlQuery.list();
			
			
			final int codigoGrupoServicio=0;
			final int nombreGrupoServicio=1;
			final int codigoServicio=2;
			final int tarifaServicio=3;
			
			for(Object[]fila:resultado){
				ServicioAutorizacionOrdenDto servicioAutorizacionOrdenDto=new ServicioAutorizacionOrdenDto();
				servicioAutorizacionOrdenDto.setCodigoGrupoServicio((Integer) fila[codigoGrupoServicio]);
				servicioAutorizacionOrdenDto.setDescripcion((String) fila[nombreGrupoServicio]);
				servicioAutorizacionOrdenDto.setCodigo((Integer) fila[codigoServicio]);
				servicioAutorizacionOrdenDto.setValorTarifa(BigDecimal.valueOf((Double) fila[tarifaServicio]));
				servicios.add(servicioAutorizacionOrdenDto);
			}
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		return servicios;
	}
	/**
	 * Consulta los diferentes articulos agrupados por clase de inventario ordenados y verifica si tienen tarifa
	 * 
	 * @param grupoOrdenadoresConsultaExternaDto
	 * @param esquemaTarifarioArticulos
	 * @return
	 * @throws BDException
	 * @author jeilones
	 * @created 9/11/2012
	 */
	@SuppressWarnings("unchecked")
	public List<MedicamentoInsumoAutorizacionOrdenDto> verificarTarifasArticulos(GrupoOrdenadoresConsultaExternaDto grupoOrdenadoresConsultaExternaDto, int esquemaTarifarioArticulos)throws BDException{
		List<MedicamentoInsumoAutorizacionOrdenDto>medicamentos=new ArrayList<MedicamentoInsumoAutorizacionOrdenDto>(0);
		try{
			persistenciaSvc=new PersistenciaSvc();
			
			Map<String, Object>parametros=new LinkedHashMap<String, Object>(0);
			StringBuffer select=new StringBuffer("");
			select.append("SELECT ");
			select.append("RS.CODIGO_GS_CI, ")
			.append("RS.DESCRIPCION_GS_CI, ")
			.append("RS.CANTIDAD_SERV_MED, ")
			.append("RS.VALOR_TARIFA ")
			.append("FROM (");
			//--ORDENES AMB CITA ARTICULOS
			select.append("SELECT DISTINCT ")
				.append("CI.CODIGO               AS CODIGO_GS_CI, ")
				.append("CI.NOMBRE               AS DESCRIPCION_GS_CI, ")
				.append("ART.CODIGO                                    AS CANTIDAD_SERV_MED, ")
				.append("CONSULTAEXTERNA.CALCULO_TARIFA_ARTICULO(ART.CODIGO,:codigoEsquemaTarifario)   AS VALOR_TARIFA ")
			.append("FROM AGENDA AG ")
			.append("INNER JOIN CONSULTAEXTERNA.CITA CT ON CT.CODIGO_AGENDA = AG.CODIGO ")
			/*PARA PODER SABER QUE ARTICULOS FUERON ORDENADOS LA RELACION DE LA ORDEN CON LA CITA ES OBLIGATORIA*/
			.append("INNER JOIN ORDENES.ORDENES_AMBULATORIAS OA ON OA.CITA_ASOCIADA=CT.CODIGO ")
			.append("INNER JOIN CONSULTAEXTERNA.SERVICIOS_CITA SC ")
			.append("ON SC.CODIGO_CITA = CT.CODIGO ")
			/*.append("INNER JOIN MEDICOS MED ON MED.codigo_medico = AG.CODIGO_MEDICO ")
			.append("INNER JOIN PERSONAS PER ON PER.CODIGO = MED.CODIGO_MEDICO ")
			.append("INNER JOIN USUARIOS US ON US.CODIGO_PERSONA = PER.CODIGO ")*/
			.append("INNER JOIN USUARIOS US ON US.LOGIN = CT.USUARIO_MODIFICA_ESTADO ")
            .append("INNER JOIN PERSONAS PER ON PER.CODIGO = US.CODIGO_PERSONA ")
            .append("INNER JOIN MEDICOS MED ON MED.codigo_medico = PER.CODIGO ")
            
			.append("INNER JOIN ADMINISTRACION.MEDICOS_INSTITUCIONES MED_INS ")
			.append("ON MED_INS.CODIGO_MEDICO = MED.CODIGO_MEDICO ")
			.append("INNER JOIN CONSULTAEXTERNA.UNIDADES_CONSULTA UC ")
			.append("ON UC.CODIGO = CT.UNIDAD_CONSULTA ")
			.append("INNER JOIN ORDENES.DET_ORDEN_AMB_ARTICULO DOAA ")
			.append("ON DOAA.CODIGO_ORDEN = OA.CODIGO ")
			.append("INNER JOIN INVENTARIOS.ARTICULO ART ")
			.append("ON ART.CODIGO=DOAA.ARTICULO ")
			.append("INNER JOIN INVENTARIOS.SUBGRUPO_INVENTARIO SUBGI ")
			.append("ON SUBGI.CODIGO=ART.SUBGRUPO ")
			.append("INNER JOIN INVENTARIOS.GRUPO_INVENTARIO GI ")
			.append("ON (GI.CODIGO=SUBGI.GRUPO ")
			.append("AND GI.CLASE =SUBGI.CLASE) ")
			.append("INNER JOIN INVENTARIOS.CLASE_INVENTARIO CI  ")
			.append("ON CI.CODIGO=GI.CLASE  ");
			
			/*FILTROS*/
			parametros.put("codigoEsquemaTarifario", esquemaTarifarioArticulos);
			parametros.put("codigoEstadoCita", ConstantesBD.codigoEstadoCitaAtendida);
			parametros.put("codigoInstitucion", grupoOrdenadoresConsultaExternaDto.getInstitucion());
			
			StringBuffer where=new StringBuffer("WHERE ");		
			where.append("OA.CITA_ASOCIADA IS NOT NULL  ")
				.append("AND CT.ESTADO_CITA = :codigoEstadoCita  ")
				.append("AND MED_INS.CODIGO_INSTITUCION = :codigoInstitucion  ");
			
			if(grupoOrdenadoresConsultaExternaDto.getFechaInicialAtencionCita()!=null){
				if(grupoOrdenadoresConsultaExternaDto.getFechaFinalAtencionCita()!=null){
					where.append("AND SC.FECHA_CITA BETWEEN :fechaInicial  AND :fechaFinal ");
					parametros.put("fechaInicial", grupoOrdenadoresConsultaExternaDto.getFechaInicialAtencionCita());
					parametros.put("fechaFinal", grupoOrdenadoresConsultaExternaDto.getFechaFinalAtencionCita());
				}else{
					where.append("AND SC.FECHA_CITA >= :fechaInicial ");
					parametros.put("fechaInicial", grupoOrdenadoresConsultaExternaDto.getFechaInicialAtencionCita());
				}
			}else{
				if(grupoOrdenadoresConsultaExternaDto.getFechaFinalAtencionCita()!=null){
					where.append("AND SC.FECHA_CITA <= :fechaFinal ");
					parametros.put("fechaFinal", grupoOrdenadoresConsultaExternaDto.getFechaFinalAtencionCita());
				}
			}
			
			if(grupoOrdenadoresConsultaExternaDto.getFiltroIdCentroAtencion()!=null&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdCentroAtencion().trim().isEmpty()
					&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdCentroAtencion().trim().equals(ConstantesBD.codigoNuncaValido+"")){
				select.append("INNER JOIN ADMINISTRACION.CENTRO_ATENCION CA ")
					.append("ON CA.CONSECUTIVO = AG.CENTRO_ATENCION ");
				where.append("AND CA.CONSECUTIVO = :codigoCentroAtencion ");
				parametros.put("codigoCentroAtencion", Integer.parseInt(grupoOrdenadoresConsultaExternaDto.getFiltroIdCentroAtencion().trim()));
			}
			
			if(grupoOrdenadoresConsultaExternaDto.getFiltroIdUnidadConsulta()!=null&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdUnidadConsulta().trim().isEmpty()
					&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdUnidadConsulta().trim().equals(ConstantesBD.codigoNuncaValido+"")){
				where.append("AND UC.CODIGO = :codigoUnidadConsulta ");
				parametros.put("codigoUnidadConsulta", Integer.parseInt(grupoOrdenadoresConsultaExternaDto.getFiltroIdUnidadConsulta().trim()));
			}
			
			if(grupoOrdenadoresConsultaExternaDto.getFiltroIdConvenio()!=null&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdConvenio().trim().isEmpty()
					&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdConvenio().trim().equals(ConstantesBD.codigoNuncaValido+"")){
				select.append("INNER JOIN SOLICITUDES SOL ON SOL.NUMERO_SOLICITUD = SC.NUMERO_SOLICITUD ")
					.append("INNER JOIN SOLICITUDES_SUBCUENTA SOL_SUBCUENTA ON SOL_SUBCUENTA.SOLICITUD=SOL.NUMERO_SOLICITUD ")
					.append("INNER JOIN SUB_CUENTAS SUB_CUENTA ON SUB_CUENTA.SUB_CUENTA=SOL_SUBCUENTA.SUB_CUENTA ")
					.append("INNER JOIN FACTURACION.CONVENIOS CONV ON CONV.CODIGO = SUB_CUENTA.CONVENIO ");
				where.append("AND CONV.CODIGO = :codigoConvenio ");
				parametros.put("codigoConvenio", Integer.parseInt(grupoOrdenadoresConsultaExternaDto.getFiltroIdConvenio().trim()));
			}
			
			if(grupoOrdenadoresConsultaExternaDto.getFiltroProfesionalSalud()!=null
					&&grupoOrdenadoresConsultaExternaDto.getFiltroProfesionalSalud().getLoginUsuario()!=null
					&&!grupoOrdenadoresConsultaExternaDto.getFiltroProfesionalSalud().getLoginUsuario().trim().equals(ConstantesBD.codigoNuncaValido+"")){
				where.append("AND US.LOGIN = :usuario  ");
				parametros.put("usuario", grupoOrdenadoresConsultaExternaDto.getFiltroProfesionalSalud().getLoginUsuario());
			}
			
			if(grupoOrdenadoresConsultaExternaDto.getFiltroIdClaseInventario()!=null&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdClaseInventario().trim().isEmpty()
					&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdClaseInventario().trim().equals(ConstantesBD.codigoNuncaValido+"")){
				where.append("AND CI.CODIGO = :codigoClaseInventario ");
				parametros.put("codigoClaseInventario", Integer.parseInt(grupoOrdenadoresConsultaExternaDto.getFiltroIdClaseInventario().trim()));
			}
			
			/*FIN FILTROS*/
			
			select.append(where)
				.append("GROUP BY ")
				.append("CI.CODIGO, ")
				.append("CI.NOMBRE, ")
				.append("ART.CODIGO ");
			
			select.append(") RS WHERE RS.VALOR_TARIFA IS NULL");
			
			SQLQuery sqlQuery=persistenciaSvc.getSession().createSQLQuery(select.toString());
			Set<String>keyParemetros=parametros.keySet();
			for(String key:keyParemetros){
				sqlQuery.setParameter(key, parametros.get(key));
			}
			List<Object[]>resultado=sqlQuery.list();
			
			
			final int codigoClaseInventario=0;
			final int nombreClaseInventario=1;
			final int codigoArticulo=2;
			final int tarifaArticulo=3;
			
			for(Object[]fila:resultado){
				MedicamentoInsumoAutorizacionOrdenDto medicamentoInsumoAutorizacionOrdenDto=new MedicamentoInsumoAutorizacionOrdenDto();
				medicamentoInsumoAutorizacionOrdenDto.setClaseInventario((Integer) fila[codigoClaseInventario]);
				medicamentoInsumoAutorizacionOrdenDto.setNombreClaseInventario((String) fila[nombreClaseInventario]);
				medicamentoInsumoAutorizacionOrdenDto.setCodigo((Integer) fila[codigoArticulo]);
				medicamentoInsumoAutorizacionOrdenDto.setValorTarifa(BigDecimal.valueOf((Double) fila[tarifaArticulo]));
				medicamentos.add(medicamentoInsumoAutorizacionOrdenDto);
			}
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		
		return medicamentos;
	}
	
	/**
	 * Consulta para el reporte de ordenadores de consulta externa, sirve para PDF y EXCEL
	 * 
	 * @param grupoOrdenadoresConsultaExternaDto
	 * @param esquemaTarifarioArticulos
	 * @param esquemaTarifarioServicios
	 * @return
	 * @throws BDException
	 * @author jeilones
	 * @created 2/11/2012
	 */
	@SuppressWarnings("unchecked")
	public List<ProfesionalSaludDto> consultarIdentificadorOrdenadoresConsulta(GrupoOrdenadoresConsultaExternaDto grupoOrdenadoresConsultaExternaDto,int esquemaTarifarioArticulos,int esquemaTarifarioServicios)throws BDException{
		List<ProfesionalSaludDto>profesionales=new ArrayList<ProfesionalSaludDto>(0);
		try{
			persistenciaSvc=new PersistenciaSvc();
			
			Map<String, Object>parametros=new LinkedHashMap<String, Object>(0);
			StringBuffer consultaOrdenadoresConsultaExt=new StringBuffer("");
			consultaOrdenadoresConsultaExt.append("SELECT RS.CODIGO_UNIDAD_CONSULTA, ")
				.append("RS.DESCRIPCION_UNIDAD_CONSULTA, ")
				.append("RS.CODIGO_GS_CI, ")
				.append("RS.DESCRIPCION_GS_CI, ")
				.append("RS.TIPO_IDENTIFICACION, ")
				.append("RS.NUMERO_IDENTIFICACION, ")
				.append("RS.PRIMER_APELLIDO, ")
				.append("RS.SEGUNDO_APELLIDO, ")
				.append("RS.PRIMER_NOMBRE, ")
				.append("RS.SEGUNDO_NOMBRE, ")
				.append("RS.NUMERO_REGISTRO, ")
				.append("RS.ESPECIALIDADES_MED, ")
				.append("RS.CANTIDAD_ORD_AMB_GEN, ")
				.append("RS.CANTIDAD_CITAS_ATENDIDAS, ")
				.append("RS.CANTIDAD_SERV_MED, ")
				.append("RS.ES_SERVICIO, ")
				.append("RS.CANTIDAD_ORD_AMB_GEN AS PROM_CANT_ORD_X_CITA,  ")
				.append("CASE WHEN RS.CANTIDAD_ORD_AMB_GEN>0 ")
					.append("THEN RS.CANTIDAD_SERV_MED/RS.CANTIDAD_ORD_AMB_GEN ")
					.append("ELSE 0 ")
				.append("END AS PROM_SERV_MED_X_ORDEN, ")
				.append("RS.CANTIDAD_SERV_MED AS PROM_SERV_MED_X_CITA, ")
				.append("RS.VALOR_TOTAL_TARIFA AS VALOR_TOTAL_TARIFA, ")
				.append("RS.VALOR_EST_ORD_CITA, ")
				.append("RS.VALOR_EST_SERMED_ORDEN, ")
				.append("RS.VALOR_EST_SERMED_CITA, ")
				.append("RS.NOMBRE_USUARIO ")
			.append("FROM  ")
				.append("( ");
			
			consultaOrdenadoresConsultaExt.append(crearConsultaOrdenadores(grupoOrdenadoresConsultaExternaDto,esquemaTarifarioArticulos,esquemaTarifarioServicios,parametros));
				
			consultaOrdenadoresConsultaExt.append(") RS ")
			.append("ORDER BY ")
				.append("RS.PRIMER_NOMBRE, ")
				.append("RS.SEGUNDO_NOMBRE, ")
				.append("RS.PRIMER_APELLIDO, ")
				.append("RS.SEGUNDO_APELLIDO, ")
				.append("RS.DESCRIPCION_UNIDAD_CONSULTA, ")
				.append("RS.ES_SERVICIO DESC, ")
				.append("RS.DESCRIPCION_GS_CI ");
			
			SQLQuery sqlQuery=persistenciaSvc.getSession().createSQLQuery(consultaOrdenadoresConsultaExt.toString());
			sqlQuery.addScalar("CODIGO_UNIDAD_CONSULTA", new IntegerType());
			sqlQuery.addScalar("DESCRIPCION_UNIDAD_CONSULTA", new StringType());
			sqlQuery.addScalar("CODIGO_GS_CI", new IntegerType());
			
			sqlQuery.addScalar("DESCRIPCION_GS_CI", new StringType());
			sqlQuery.addScalar("TIPO_IDENTIFICACION", new StringType());
			sqlQuery.addScalar("NUMERO_IDENTIFICACION", new StringType());
			sqlQuery.addScalar("PRIMER_APELLIDO", new StringType());
			sqlQuery.addScalar("SEGUNDO_APELLIDO", new StringType());
			sqlQuery.addScalar("PRIMER_NOMBRE", new StringType());
			sqlQuery.addScalar("SEGUNDO_NOMBRE", new StringType());
			
			sqlQuery.addScalar("NUMERO_REGISTRO", new StringType());
			sqlQuery.addScalar("ESPECIALIDADES_MED", new StringType());
			
			sqlQuery.addScalar("CANTIDAD_ORD_AMB_GEN", new IntegerType());
			sqlQuery.addScalar("CANTIDAD_CITAS_ATENDIDAS", new IntegerType());
			sqlQuery.addScalar("CANTIDAD_SERV_MED", new IntegerType());
			sqlQuery.addScalar("ES_SERVICIO", new IntegerType());
			sqlQuery.addScalar("PROM_CANT_ORD_X_CITA", new DoubleType());
			sqlQuery.addScalar("PROM_SERV_MED_X_ORDEN", new DoubleType());
			sqlQuery.addScalar("PROM_SERV_MED_X_CITA", new DoubleType());
			sqlQuery.addScalar("VALOR_TOTAL_TARIFA", new DoubleType());
			sqlQuery.addScalar("VALOR_EST_ORD_CITA", new DoubleType());
			sqlQuery.addScalar("VALOR_EST_SERMED_ORDEN", new DoubleType());
			sqlQuery.addScalar("VALOR_EST_SERMED_CITA", new DoubleType());
			sqlQuery.addScalar("NOMBRE_USUARIO", new StringType());
			
			Set<String>keyParemetros=parametros.keySet();
			for(String key:keyParemetros){
				sqlQuery.setParameter(key, parametros.get(key));
			}
			List<Object[]>resultado=sqlQuery.list();
			
			final int CODIGO_UNIDAD_CONSULTA=0;
			final int DESCRIPCION_UNIDAD_CONSULTA=1;
			final int CODIGO_GS_CI=2;
			final int DESCRIPCION_GS_CI=3;
			final int TIPO_IDENTIFICACION=4;
			final int NUMERO_IDENTIFICACION=5;
			final int PRIMER_APELLIDO=6;
			final int SEGUNDO_APELLIDO=7;
			final int PRIMER_NOMBRE=8;
			final int SEGUNDO_NOMBRE=9;
			final int NUMERO_REGISTRO_MEDICO=10;
			final int ESPECIALIDADES_MEDICO=11;
			final int CANTIDAD_ORD_AMB_GEN=12;
			final int CANTIDAD_CITAS_ATENDIDAS=13;
			final int CANTIDAD_SERV_MED=14;
			final int ES_SERVICIO=15;
			//final int PROM_CANT_ORD_X_CITA=16;
			final int PROM_SERV_MED_X_ORDEN=17;
			//final int PROM_SERV_MED_X_CITA=18;
			final int VALOR_TOTAL_TARIFA=19;
			final int VALOR_EST_ORD_CITA=20;
			final int VALOR_EST_SERMED_ORDEN=21;
			final int VALOR_EST_SERMED_CITA=22;
			final int NOMBRE_USUARIO=23;
			
			Map<String,ProfesionalSaludDto>mapaProfesionales=new LinkedHashMap<String, ProfesionalSaludDto>(0);
			for(Object[]fila:resultado){
				String id=fila[TIPO_IDENTIFICACION]+"-"+fila[NUMERO_IDENTIFICACION];
				ProfesionalSaludDto proSaludDto=new ProfesionalSaludDto();
				proSaludDto.setUnidadesConsultas(new ArrayList<DtoUnidadesConsulta>(0));
				proSaludDto.setMapaUnidadesConsultas(new LinkedHashMap<Integer, DtoUnidadesConsulta>(0));
				
				proSaludDto.setTipoIdentificacion((String)fila[TIPO_IDENTIFICACION]);
				proSaludDto.setNumeroIdentificacion((String)fila[NUMERO_IDENTIFICACION]);
				
				proSaludDto.setPrimerApellido((String) fila[PRIMER_APELLIDO]);
				proSaludDto.setSegundoApellido((String) fila[SEGUNDO_APELLIDO]);
				proSaludDto.setPrimerNombre((String) fila[PRIMER_NOMBRE]);
				proSaludDto.setSegundoNombre((String) fila[SEGUNDO_NOMBRE]);
				proSaludDto.setLoginUsuario((String)fila[NOMBRE_USUARIO]);
				
				StringBuffer especialidades=new StringBuffer("");
				if(fila[NUMERO_REGISTRO_MEDICO]!=null){
					especialidades.append(fila[NUMERO_REGISTRO_MEDICO]);
				}
				if(fila[ESPECIALIDADES_MEDICO]!=null){
					especialidades.append(" ");
					especialidades.append(fila[ESPECIALIDADES_MEDICO]);
				}
				
				proSaludDto.setEspecializaciones(especialidades.toString());
				
				if(mapaProfesionales.containsKey(id)){
					proSaludDto=mapaProfesionales.get(id);
				}else{
					mapaProfesionales.put(id,proSaludDto);
					profesionales.add(proSaludDto);
					
					int cantidadCitasAtendiasProf=consultarCantidadCitasAtendidas(grupoOrdenadoresConsultaExternaDto.getFechaInicialAtencionCita(), grupoOrdenadoresConsultaExternaDto.getFechaFinalAtencionCita(), proSaludDto.getLoginUsuario(), null,null,null);
					proSaludDto.setCantidadCitasAtendidas(cantidadCitasAtendiasProf);
				}
				
				/*int cantidadCitasAtendidasProf=proSaludDto.getCantidadCitasAtendidas();
				cantidadCitasAtendidasProf+=((Integer)fila[CANTIDAD_CITAS_ATENDIDAS]).intValue();
				proSaludDto.setCantidadCitasAtendidas(cantidadCitasAtendidasProf);*/
				
				Map<Integer,DtoUnidadesConsulta>unidadesConsulta=proSaludDto.getMapaUnidadesConsultas();
				
				DtoUnidadesConsulta unidadConsulta=new DtoUnidadesConsulta();
				unidadConsulta.setCodigo((Integer) fila[CODIGO_UNIDAD_CONSULTA]);
				unidadConsulta.setDescripcion((String) fila[DESCRIPCION_UNIDAD_CONSULTA]);
				unidadConsulta.setListaOrdenadoresConsultaExterna(new ArrayList<OrdenadoresConsultaExternaDto>(0));
				if(unidadesConsulta.containsKey(fila[CODIGO_UNIDAD_CONSULTA])){
					unidadConsulta=unidadesConsulta.get(fila[CODIGO_UNIDAD_CONSULTA]);
				}else{
					unidadesConsulta.put((Integer) fila[CODIGO_UNIDAD_CONSULTA],unidadConsulta);
					proSaludDto.getUnidadesConsultas().add(unidadConsulta);
					
					int cantidadCitasAtendiasProfUnidadConsulta=consultarCantidadCitasAtendidas(grupoOrdenadoresConsultaExternaDto.getFechaInicialAtencionCita(), grupoOrdenadoresConsultaExternaDto.getFechaFinalAtencionCita(), proSaludDto.getLoginUsuario(), unidadConsulta.getCodigo(),null,null);
					unidadConsulta.setCantidadCitasAtendidas(cantidadCitasAtendiasProfUnidadConsulta);
				}
				
				proSaludDto.setGrupoUnidadesConsulta(new JRBeanCollectionDataSource(proSaludDto.getUnidadesConsultas(),false));
				
				/*int cantidadCitasAtendidasUnidadConsulta=unidadConsulta.getCantidadCitasAtendidas();
				cantidadCitasAtendidasUnidadConsulta+=((Integer)fila[CANTIDAD_CITAS_ATENDIDAS]).intValue();
				unidadConsulta.setCantidadCitasAtendidas(cantidadCitasAtendidasUnidadConsulta);*/
				
				if(fila[CODIGO_GS_CI]!=null){
					
					List<OrdenadoresConsultaExternaDto>ordenadores=unidadConsulta.getListaOrdenadoresConsultaExterna();
					OrdenadoresConsultaExternaDto ordenadoresConsultaExternaDto2=new OrdenadoresConsultaExternaDto();
					ordenadores.add(ordenadoresConsultaExternaDto2);
					
					unidadConsulta.setGrupoOrdenadoresConsultaExterna(new JRBeanCollectionDataSource(ordenadores,false));
					
					if(((Integer)fila[ES_SERVICIO]).intValue()==1){
						ordenadoresConsultaExternaDto2.setCodigoGrupoServicio((Integer) fila[CODIGO_GS_CI]);
						ordenadoresConsultaExternaDto2.setGrupoServicio((String) fila[DESCRIPCION_GS_CI]);
						ordenadoresConsultaExternaDto2.setEsServicio(true);
					}else{
						ordenadoresConsultaExternaDto2.setCodigoClaseInventario((Integer) fila[CODIGO_GS_CI]);
						ordenadoresConsultaExternaDto2.setClaseInventario((String) fila[DESCRIPCION_GS_CI]);
						ordenadoresConsultaExternaDto2.setEsServicio(false);
					}
					
					ordenadoresConsultaExternaDto2.setCantidadOrdenesAmbGeneradas((Integer) fila[CANTIDAD_ORD_AMB_GEN]);
					ordenadoresConsultaExternaDto2.setCantidadCitasAtendidas((Integer) fila[CANTIDAD_CITAS_ATENDIDAS]);
					
					ordenadoresConsultaExternaDto2.setPromedioServMedOrdenAmb((Double)fila[PROM_SERV_MED_X_ORDEN]);
					
					int cantidadCitasPorGrupoServClaseInv=consultarCantidadCitasAtendidas(grupoOrdenadoresConsultaExternaDto.getFechaInicialAtencionCita(), grupoOrdenadoresConsultaExternaDto.getFechaFinalAtencionCita(), proSaludDto.getLoginUsuario(), unidadConsulta.getCodigo(),ordenadoresConsultaExternaDto2.isEsServicio(), ordenadoresConsultaExternaDto2.isEsServicio()?ordenadoresConsultaExternaDto2.getCodigoGrupoServicio():ordenadoresConsultaExternaDto2.getCodigoClaseInventario());
					if(cantidadCitasPorGrupoServClaseInv>0){
						double promOrdAmbXCita=((Integer) fila[CANTIDAD_ORD_AMB_GEN]).doubleValue()/((double)cantidadCitasPorGrupoServClaseInv);
						ordenadoresConsultaExternaDto2.setPromedioOrdenesAmbXCitas(promOrdAmbXCita);
						
						double promServMedXCita=((Integer) fila[CANTIDAD_SERV_MED]).doubleValue()/((double)cantidadCitasPorGrupoServClaseInv);
						ordenadoresConsultaExternaDto2.setPromedioServMedCita(promServMedXCita);
						if(fila[VALOR_TOTAL_TARIFA]!=null){
							double promValorTarifa=((Double)fila[VALOR_TOTAL_TARIFA]).doubleValue()/(double)cantidadCitasPorGrupoServClaseInv;
							ordenadoresConsultaExternaDto2.setCostoPromedioCita(promValorTarifa);
							
							double totalCostoPromedioCita=proSaludDto.getTotalCostoPromedio()+promValorTarifa;
							double totalCostoPromedioCitaUnidadConsulta=unidadConsulta.getTotalCostoPromedioCita()+promValorTarifa;
							
							proSaludDto.setTotalCostoPromedio(totalCostoPromedioCita);
							unidadConsulta.setTotalCostoPromedioCita(totalCostoPromedioCitaUnidadConsulta);
						}
					}
					/*ordenadoresConsultaExternaDto2.setPromedioOrdenesAmbXCitas((Double) fila[PROM_CANT_ORD_X_CITA]);
					ordenadoresConsultaExternaDto2.setPromedioServMedOrdenAmb((Double)fila[PROM_SERV_MED_X_ORDEN]);
					ordenadoresConsultaExternaDto2.setPromedioServMedCita((Double)fila[PROM_SERV_MED_X_CITA]);*/
					
					ordenadoresConsultaExternaDto2.setValorEstCita((Double) fila[VALOR_EST_ORD_CITA]);
					ordenadoresConsultaExternaDto2.setValorEstServMedOrdenAmb((Double) fila[VALOR_EST_SERMED_ORDEN]);
					ordenadoresConsultaExternaDto2.setValorEstServMedCita((Double) fila[VALOR_EST_SERMED_CITA]);
				}
			}
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		
		return profesionales;
	}
	
	/**
	 * Consulta para el reporte de ordenadores de consulta externa para archivo plano
	 * 
	 * @param ordenadoresConsultaExternaDto
	 * @param esquemaTarifarioArticulos
	 * @param esquemaTarifarioServicios
	 * @return
	 * @throws BDException
	 * @author jeilones
	 * @created 2/11/2012
	 */
	@SuppressWarnings("unchecked")
	public List<OrdenadoresConsultaExternaPlanoDto> consultarIdentificadorOrdenadoresConsultaPlano(GrupoOrdenadoresConsultaExternaDto grupoOrdenadoresConsultaExternaPlanoDto,int esquemaTarifarioArticulos,int esquemaTarifarioServicios)throws BDException{
		List<OrdenadoresConsultaExternaPlanoDto>ordenadoresConsultaExterna=new ArrayList<OrdenadoresConsultaExternaPlanoDto>(0);
		try{
			persistenciaSvc=new PersistenciaSvc();
			
			Map<String, Object>parametros=new LinkedHashMap<String, Object>(0);
			StringBuffer consultaOrdenadoresConsultaExt=new StringBuffer("");
			consultaOrdenadoresConsultaExt.append("SELECT RS.CODIGO_UNIDAD_CONSULTA, ")
				.append("RS.DESCRIPCION_UNIDAD_CONSULTA, ")
				.append("RS.CODIGO_GS_CI, ")
				.append("RS.DESCRIPCION_GS_CI, ")
				.append("RS.TIPO_IDENTIFICACION, ")
				.append("RS.NUMERO_IDENTIFICACION, ")
				.append("RS.PRIMER_APELLIDO, ")
				.append("RS.SEGUNDO_APELLIDO, ")
				.append("RS.PRIMER_NOMBRE, ")
				.append("RS.SEGUNDO_NOMBRE, ")
				.append("RS.CANTIDAD_ORD_AMB_GEN, ")
				.append("RS.CANTIDAD_CITAS_ATENDIDAS, ")
				.append("RS.CANTIDAD_SERV_MED, ")
				.append("RS.ES_SERVICIO, ")
				.append("RS.CANTIDAD_ORD_AMB_GEN AS PROM_CANT_ORD_X_CITA,  ")
				.append("CASE WHEN RS.CANTIDAD_ORD_AMB_GEN>0 ")
					.append("THEN RS.CANTIDAD_SERV_MED/RS.CANTIDAD_ORD_AMB_GEN ")
					.append("ELSE 0 ")
				.append("END AS PROM_SERV_MED_X_ORDEN, ")
				.append("RS.CANTIDAD_SERV_MED AS PROM_SERV_MED_X_CITA, ")
				.append("RS.VALOR_TOTAL_TARIFA AS VALOR_TOTAL_TARIFA, ")
				.append("RS.VALOR_EST_ORD_CITA, ")
				.append("RS.VALOR_EST_SERMED_ORDEN, ")
				.append("RS.VALOR_EST_SERMED_CITA, ")
				.append("RS.NOMBRE_USUARIO ")
			.append("FROM  ")
				.append("( ");
			
			consultaOrdenadoresConsultaExt.append(crearConsultaOrdenadores(grupoOrdenadoresConsultaExternaPlanoDto,esquemaTarifarioArticulos,esquemaTarifarioServicios,parametros));
				
			consultaOrdenadoresConsultaExt.append(") RS ")
			.append("ORDER BY ")
				.append("RS.PRIMER_NOMBRE, ")
				.append("RS.SEGUNDO_NOMBRE, ")
				.append("RS.PRIMER_APELLIDO, ")
				.append("RS.SEGUNDO_APELLIDO, ")
				.append("RS.DESCRIPCION_UNIDAD_CONSULTA, ")
				.append("RS.ES_SERVICIO DESC, ")
				.append("RS.DESCRIPCION_GS_CI ");
			
			SQLQuery sqlQuery=persistenciaSvc.getSession().createSQLQuery(consultaOrdenadoresConsultaExt.toString());
			sqlQuery.addScalar("CODIGO_UNIDAD_CONSULTA", new IntegerType());
			sqlQuery.addScalar("DESCRIPCION_UNIDAD_CONSULTA", new StringType());
			sqlQuery.addScalar("CODIGO_GS_CI", new IntegerType());
			
			sqlQuery.addScalar("DESCRIPCION_GS_CI", new StringType());
			sqlQuery.addScalar("TIPO_IDENTIFICACION", new StringType());
			sqlQuery.addScalar("NUMERO_IDENTIFICACION", new StringType());
			sqlQuery.addScalar("PRIMER_APELLIDO", new StringType());
			sqlQuery.addScalar("SEGUNDO_APELLIDO", new StringType());
			sqlQuery.addScalar("PRIMER_NOMBRE", new StringType());
			sqlQuery.addScalar("SEGUNDO_NOMBRE", new StringType());
			sqlQuery.addScalar("CANTIDAD_ORD_AMB_GEN", new IntegerType());
			sqlQuery.addScalar("CANTIDAD_CITAS_ATENDIDAS", new IntegerType());
			sqlQuery.addScalar("CANTIDAD_SERV_MED", new IntegerType());
			sqlQuery.addScalar("ES_SERVICIO", new IntegerType());
			sqlQuery.addScalar("PROM_CANT_ORD_X_CITA", new DoubleType());
			sqlQuery.addScalar("PROM_SERV_MED_X_ORDEN", new DoubleType());
			sqlQuery.addScalar("PROM_SERV_MED_X_CITA", new DoubleType());
			sqlQuery.addScalar("VALOR_TOTAL_TARIFA", new DoubleType());
			sqlQuery.addScalar("VALOR_EST_ORD_CITA", new DoubleType());
			sqlQuery.addScalar("VALOR_EST_SERMED_ORDEN", new DoubleType());
			sqlQuery.addScalar("VALOR_EST_SERMED_CITA", new DoubleType());
			sqlQuery.addScalar("NOMBRE_USUARIO", new StringType());
			
			Set<String>keyParemetros=parametros.keySet();
			for(String key:keyParemetros){
				sqlQuery.setParameter(key, parametros.get(key));
			}
			List<Object[]>resultado=sqlQuery.list();
			
			final int CODIGO_UNIDAD_CONSULTA=0;
			final int DESCRIPCION_UNIDAD_CONSULTA=1;
			final int CODIGO_GS_CI=2;
			final int DESCRIPCION_GS_CI=3;
			final int TIPO_IDENTIFICACION=4;
			final int NUMERO_IDENTIFICACION=5;
			final int PRIMER_APELLIDO=6;
			final int SEGUNDO_APELLIDO=7;
			final int PRIMER_NOMBRE=8;
			final int SEGUNDO_NOMBRE=9;
			final int CANTIDAD_ORD_AMB_GEN=10;
			final int CANTIDAD_CITAS_ATENDIDAS=11;
			final int CANTIDAD_SERV_MED=12;
			final int ES_SERVICIO=13;
			//final int PROM_CANT_ORD_X_CITA=14;
			final int PROM_SERV_MED_X_ORDEN=15;
			//final int PROM_SERV_MED_X_CITA=16;
			final int VALOR_TOTAL_TARIFA=17;
			final int VALOR_EST_ORD_CITA=18;
			final int VALOR_EST_SERMED_ORDEN=19;
			final int VALOR_EST_SERMED_CITA=20;
			final int NOMBRE_USUARIO=21;
			
			for(Object[]fila:resultado){
				OrdenadoresConsultaExternaPlanoDto ordenadoresConsultaExtDto2=new OrdenadoresConsultaExternaPlanoDto();
				
				ordenadoresConsultaExtDto2.setTipoIdentificacion((String)fila[TIPO_IDENTIFICACION]);
				ordenadoresConsultaExtDto2.setNumeroIdentificacion((String)fila[NUMERO_IDENTIFICACION]);
				
				ordenadoresConsultaExtDto2.setPrimerApellido((String) fila[PRIMER_APELLIDO]);
				ordenadoresConsultaExtDto2.setSegundoApellido((String) fila[SEGUNDO_APELLIDO]);
				ordenadoresConsultaExtDto2.setPrimerNombre((String) fila[PRIMER_NOMBRE]);
				ordenadoresConsultaExtDto2.setSegundoNombre((String) fila[SEGUNDO_NOMBRE]);
				
				ordenadoresConsultaExtDto2.setCodigoUnidadConsulta((Integer) fila[CODIGO_UNIDAD_CONSULTA]);
				ordenadoresConsultaExtDto2.setDescripcionUnidadConsulta((String) fila[DESCRIPCION_UNIDAD_CONSULTA]);
				
				String nombreUsuario=(String)fila[NOMBRE_USUARIO];
				int cantidadCitasAtendiasProfUnidadConsulta=consultarCantidadCitasAtendidas(grupoOrdenadoresConsultaExternaPlanoDto.getFechaInicialAtencionCita(), grupoOrdenadoresConsultaExternaPlanoDto.getFechaFinalAtencionCita(), nombreUsuario, ordenadoresConsultaExtDto2.getCodigoUnidadConsulta(),null,null);
				ordenadoresConsultaExtDto2.setCantidadCitasAtendidas(cantidadCitasAtendiasProfUnidadConsulta);
				
				ordenadoresConsultaExtDto2.setCantidadCitasAtendidas(((Integer)fila[CANTIDAD_CITAS_ATENDIDAS]).intValue());
				
				if(((Integer)fila[ES_SERVICIO]).intValue()==1){
					ordenadoresConsultaExtDto2.setCodigoGrupoServicio((Integer) fila[CODIGO_GS_CI]);
					ordenadoresConsultaExtDto2.setGrupoServicio((String) fila[DESCRIPCION_GS_CI]);
					ordenadoresConsultaExtDto2.setEsServicio(true);
				}else{
					ordenadoresConsultaExtDto2.setCodigoClaseInventario((Integer) fila[CODIGO_GS_CI]);
					ordenadoresConsultaExtDto2.setClaseInventario((String) fila[DESCRIPCION_GS_CI]);
					ordenadoresConsultaExtDto2.setEsServicio(false);
				}
				
				ordenadoresConsultaExtDto2.setCantidadOrdenesAmbGeneradas((Integer) fila[CANTIDAD_ORD_AMB_GEN]);
				ordenadoresConsultaExtDto2.setCantidadCitasAtendidas((Integer) fila[CANTIDAD_CITAS_ATENDIDAS]);
				
				ordenadoresConsultaExtDto2.setPromedioServMedOrdenAmb((Double)fila[PROM_SERV_MED_X_ORDEN]);
				
				int cantidadCitasPorGrupoServClaseInv=consultarCantidadCitasAtendidas(grupoOrdenadoresConsultaExternaPlanoDto.getFechaInicialAtencionCita(), grupoOrdenadoresConsultaExternaPlanoDto.getFechaFinalAtencionCita(), nombreUsuario, ordenadoresConsultaExtDto2.getCodigoUnidadConsulta(),ordenadoresConsultaExtDto2.isEsServicio(), ordenadoresConsultaExtDto2.isEsServicio()?ordenadoresConsultaExtDto2.getCodigoGrupoServicio():ordenadoresConsultaExtDto2.getCodigoClaseInventario());				
				if(cantidadCitasPorGrupoServClaseInv>0){
					double promOrdAmbXCita=((Integer) fila[CANTIDAD_ORD_AMB_GEN]).doubleValue()/((double) cantidadCitasPorGrupoServClaseInv);
					ordenadoresConsultaExtDto2.setPromedioOrdenesAmbXCitas(promOrdAmbXCita);
					
					double promServMedXCita=((Integer) fila[CANTIDAD_SERV_MED]).doubleValue()/((double) cantidadCitasPorGrupoServClaseInv);
					ordenadoresConsultaExtDto2.setPromedioServMedCita(promServMedXCita);
					if(fila[VALOR_TOTAL_TARIFA]!=null){
						double promValorTarifa=((Double)fila[VALOR_TOTAL_TARIFA]).doubleValue()/(double)cantidadCitasPorGrupoServClaseInv;
						ordenadoresConsultaExtDto2.setCostoPromedioCita(promValorTarifa);
					}
				}
				
				ordenadoresConsultaExtDto2.setValorEstCita((Double) fila[VALOR_EST_ORD_CITA]);
				ordenadoresConsultaExtDto2.setValorEstServMedOrdenAmb((Double) fila[VALOR_EST_SERMED_ORDEN]);
				ordenadoresConsultaExtDto2.setValorEstServMedCita((Double) fila[VALOR_EST_SERMED_CITA]);
				
				ordenadoresConsultaExterna.add(ordenadoresConsultaExtDto2);
			}
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		
		return ordenadoresConsultaExterna;
	}
	
	/**
	 * Se crea la consulta para el reporte de identificadores de ordenadores de consulta externa
	 * 
	 * @param grupoOrdenadoresConsultaExternaDto
	 * @param esquemaTarifarioArticulos
	 * @return
	 * @author jeilones
	 * @param esquemaTarifarioServicios 
	 * @created 9/11/2012
	 */
	private StringBuffer crearConsultaOrdenadores(
			GrupoOrdenadoresConsultaExternaDto grupoOrdenadoresConsultaExternaDto,
			int esquemaTarifarioArticulos,int esquemaTarifarioServicios, Map<String, Object> parametros) {
		StringBuffer consultaOrdenadoresConsultaExt=new StringBuffer("");
		
		
		//CITAS QUE NO ESTAN ASOCIADAS A ORDENES AMBULATORIAS
		//Where
		StringBuffer where=new StringBuffer("WHERE ");
		where=new StringBuffer("WHERE ");		
		where.append("CT.ESTADO_CITA = :codigoEstadoCita  ")
			.append("AND CT.CODIGO NOT IN (SELECT CITA_ASOCIADA FROM ORDENES_AMBULATORIAS WHERE CITA_ASOCIADA IS NOT NULL) ")
			.append("AND MED_INS.CODIGO_INSTITUCION = :codigoInstitucion  ");
		
		if(grupoOrdenadoresConsultaExternaDto.getFechaInicialAtencionCita()!=null){
			if(grupoOrdenadoresConsultaExternaDto.getFechaFinalAtencionCita()!=null){
				where.append("AND SC.FECHA_CITA BETWEEN :fechaInicial  AND :fechaFinal ");
				parametros.put("fechaInicial", grupoOrdenadoresConsultaExternaDto.getFechaInicialAtencionCita());
				parametros.put("fechaFinal", grupoOrdenadoresConsultaExternaDto.getFechaFinalAtencionCita());
			}else{
				where.append("AND SC.FECHA_CITA >= :fechaInicial ");
				parametros.put("fechaInicial", grupoOrdenadoresConsultaExternaDto.getFechaInicialAtencionCita());
			}
		}else{
			if(grupoOrdenadoresConsultaExternaDto.getFechaFinalAtencionCita()!=null){
				where.append("AND SC.FECHA_CITA <= :fechaFinal ");
				parametros.put("fechaFinal", grupoOrdenadoresConsultaExternaDto.getFechaFinalAtencionCita());
			}
		}
		
		if(grupoOrdenadoresConsultaExternaDto.getFiltroIdCentroAtencion()!=null&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdCentroAtencion().trim().isEmpty()
				&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdCentroAtencion().trim().equals(ConstantesBD.codigoNuncaValido+"")){
			where.append("AND CA.CONSECUTIVO = :codigoCentroAtencion ");
			parametros.put("codigoCentroAtencion", Integer.parseInt(grupoOrdenadoresConsultaExternaDto.getFiltroIdCentroAtencion().trim()));
		}
		
		if(grupoOrdenadoresConsultaExternaDto.getFiltroIdUnidadConsulta()!=null&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdUnidadConsulta().trim().isEmpty()
				&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdUnidadConsulta().trim().equals(ConstantesBD.codigoNuncaValido+"")){
			where.append("AND UC.CODIGO = :codigoUnidadConsulta ");
			parametros.put("codigoUnidadConsulta", Integer.parseInt(grupoOrdenadoresConsultaExternaDto.getFiltroIdUnidadConsulta().trim()));
		}
		
		if(grupoOrdenadoresConsultaExternaDto.getFiltroIdConvenio()!=null&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdConvenio().trim().isEmpty()
				&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdConvenio().trim().equals(ConstantesBD.codigoNuncaValido+"")){
			where.append("AND CONV.CODIGO = :codigoConvenio ");
			parametros.put("codigoConvenio", Integer.parseInt(grupoOrdenadoresConsultaExternaDto.getFiltroIdConvenio().trim()));
		}
		
		if(grupoOrdenadoresConsultaExternaDto.getFiltroProfesionalSalud()!=null
				&&grupoOrdenadoresConsultaExternaDto.getFiltroProfesionalSalud().getLoginUsuario()!=null
				&&!grupoOrdenadoresConsultaExternaDto.getFiltroProfesionalSalud().getLoginUsuario().trim().equals(ConstantesBD.codigoNuncaValido+"")){
			where.append("AND US.LOGIN = :usuario  ");
			parametros.put("usuario", grupoOrdenadoresConsultaExternaDto.getFiltroProfesionalSalud().getLoginUsuario());
		}
		//Fin Where
		consultaOrdenadoresConsultaExt.append("SELECT RS.CODIGO_UNIDAD_CONSULTA, ")
		  .append("RS.DESCRIPCION_UNIDAD_CONSULTA, ")
		  .append("NULL AS CODIGO_GS_CI, ")
		  .append("NULL AS DESCRIPCION_GS_CI, ")
		  .append("RS.TIPO_IDENTIFICACION, ")
		  .append("RS.NUMERO_IDENTIFICACION, ")
		  .append("RS.PRIMER_APELLIDO, ")
		  .append("RS.SEGUNDO_APELLIDO, ")
		  .append("RS.PRIMER_NOMBRE, ")
		  .append("RS.SEGUNDO_NOMBRE, ")
		  .append("RS.NUMERO_REGISTRO, ")
		  .append("RS.ESPECIALIDADES_MED, ")
		  .append("RS.CANTIDAD_ORD_AMB_GEN, ")
		  .append("RS.CANTIDAD_CITAS_ATENDIDAS, ")
		  .append("0 AS CANTIDAD_SERV_MED, ")
		  .append("0 AS VALOR_TOTAL_TARIFA, ")
		  .append("NULL AS VALOR_EST_ORD_CITA, ")
		  .append("NULL AS VALOR_EST_SERMED_ORDEN, ")
		  .append("NULL AS VALOR_EST_SERMED_CITA, ")
		  .append("RS.ES_SERVICIO, ")
		  .append("RS.NOMBRE_USUARIO ")
		  .append("FROM (SELECT DISTINCT UC.CODIGO AS CODIGO_UNIDAD_CONSULTA, ")
			  .append("UC.DESCRIPCION          AS DESCRIPCION_UNIDAD_CONSULTA, ")
			  .append("PER.TIPO_IDENTIFICACION, ")
			  .append("PER.NUMERO_IDENTIFICACION, ")
			  .append("PER.PRIMER_APELLIDO, ")
			  .append("PER.SEGUNDO_APELLIDO, ")
			  .append("PER.PRIMER_NOMBRE, ")
			  .append("PER.SEGUNDO_NOMBRE, ")
			  .append("MED.NUMERO_REGISTRO, ")
			  .append("ADMINISTRACION.GETESPECIALIDADESMEDICO1(MED.CODIGO_MEDICO, ',') AS ESPECIALIDADES_MED, ")
			  .append("0 AS CANTIDAD_ORD_AMB_GEN, ")
			  .append("COUNT (DISTINCT CT.CODIGO) AS CANTIDAD_CITAS_ATENDIDAS, ")
			  .append("SUM(0)        AS CANTIDAD_SERV_MED, ")
			  .append("SUM(0) AS VALOR_TOTAL_TARIFA, ")
			  .append("0 AS ES_SERVICIO, ")
			  .append("US.LOGIN AS NOMBRE_USUARIO ")
			  .append("FROM AGENDA AG ")
			  .append("INNER JOIN CONSULTAEXTERNA.CITA CT ")
			  .append("ON CT.CODIGO_AGENDA = AG.CODIGO ")
			  .append("INNER JOIN CONSULTAEXTERNA.SERVICIOS_CITA SC ")
			  .append("ON SC.CODIGO_CITA = CT.CODIGO ")
			  /*.append("INNER JOIN MEDICOS MED ")
			  .append("ON MED.codigo_medico = AG.CODIGO_MEDICO ")
			  .append("INNER JOIN PERSONAS PER ")
			  .append("ON PER.CODIGO = MED.CODIGO_MEDICO ")
			  .append("INNER JOIN USUARIOS US ")
			  .append("ON US.CODIGO_PERSONA = PER.CODIGO ")*/
			  .append("INNER JOIN USUARIOS US ON US.LOGIN = CT.USUARIO_MODIFICA_ESTADO ")
              .append("INNER JOIN PERSONAS PER ON PER.CODIGO = US.CODIGO_PERSONA ")
              .append("INNER JOIN MEDICOS MED ON MED.codigo_medico = PER.CODIGO ")
			  
			  .append("INNER JOIN ADMINISTRACION.MEDICOS_INSTITUCIONES MED_INS ")
			  .append("ON MED_INS.CODIGO_MEDICO = MED.CODIGO_MEDICO ")
			  .append("INNER JOIN CONSULTAEXTERNA.UNIDADES_CONSULTA UC ")
			  .append("ON UC.CODIGO = CT.UNIDAD_CONSULTA ");
		if(grupoOrdenadoresConsultaExternaDto.getFiltroIdCentroAtencion()!=null
				&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdCentroAtencion().trim().isEmpty()
				&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdCentroAtencion().trim().equals(ConstantesBD.codigoNuncaValido+"")){
			consultaOrdenadoresConsultaExt.append("INNER JOIN ADMINISTRACION.CENTRO_ATENCION CA ")
				.append("ON CA.CONSECUTIVO = AG.CENTRO_ATENCION ");
		}
		if(grupoOrdenadoresConsultaExternaDto.getFiltroIdConvenio()!=null&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdConvenio().trim().isEmpty()
				&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdConvenio().trim().equals(ConstantesBD.codigoNuncaValido+"")){
			consultaOrdenadoresConsultaExt.append("INNER JOIN SOLICITUDES SOL ON SOL.NUMERO_SOLICITUD = SC.NUMERO_SOLICITUD ")
				.append("INNER JOIN SOLICITUDES_SUBCUENTA SOL_SUBCUENTA ON SOL_SUBCUENTA.SOLICITUD=SOL.NUMERO_SOLICITUD ")
				.append("INNER JOIN SUB_CUENTAS SUB_CUENTA ON SUB_CUENTA.SUB_CUENTA=SOL_SUBCUENTA.SUB_CUENTA ")
				.append("INNER JOIN FACTURACION.CONVENIOS CONV ON CONV.CODIGO = SUB_CUENTA.CONVENIO ");
		}
		 consultaOrdenadoresConsultaExt .append(where)			  
			  .append("GROUP BY UC.CODIGO, ")
			  .append("UC.DESCRIPCION, ")
			  .append("PER.TIPO_IDENTIFICACION, ")
			  .append("PER.NUMERO_IDENTIFICACION, ")
			  .append("PER.PRIMER_APELLIDO, ")
			  .append("PER.SEGUNDO_APELLIDO, ")
			  .append("PER.PRIMER_NOMBRE, ")
			  .append("PER.SEGUNDO_NOMBRE, ")
			  .append("MED.NUMERO_REGISTRO, ")
			  .append("ADMINISTRACION.GETESPECIALIDADESMEDICO1(MED.CODIGO_MEDICO, ','), ")
			  .append("US.LOGIN ")
			  .append(") RS ");
		
		consultaOrdenadoresConsultaExt.append("UNION ")
		//--ORDENES AMB CITA ARTICULO
		    .append("SELECT DISTINCT UC.CODIGO AS CODIGO_UNIDAD_CONSULTA, ")
			.append("UC.DESCRIPCION          AS DESCRIPCION_UNIDAD_CONSULTA, ")
			.append("CI.CODIGO               AS CODIGO_GS_CI, ")
			.append("CI.NOMBRE               AS DESCRIPCION_GS_CI, ")
			.append("PER.TIPO_IDENTIFICACION, ")
			.append("PER.NUMERO_IDENTIFICACION, ")
			.append("PER.PRIMER_APELLIDO, ")
			.append("PER.SEGUNDO_APELLIDO, ")
			.append("PER.PRIMER_NOMBRE, ")
			.append("PER.SEGUNDO_NOMBRE, ")
			.append("MED.NUMERO_REGISTRO, ")
			.append("ADMINISTRACION.GETESPECIALIDADESMEDICO1(MED.CODIGO_MEDICO, ',') AS ESPECIALIDADES_MED, ")
			.append("COUNT (DISTINCT OA.CODIGO)                    AS CANTIDAD_ORD_AMB_GEN, ")
			.append("COUNT (DISTINCT CT.CODIGO)                    AS CANTIDAD_CITAS_ATENDIDAS, ")
			.append("SUM(DOAA.CANTIDAD)                            AS CANTIDAD_SERV_MED, ")
			//.append("CASE WHEN ART.CODIGO IS NOT NULL ")
				//.append("THEN SUM(CONSULTAEXTERNA.CALCULO_TARIFA_ARTICULO(ART.CODIGO,:codigoEsquemaTarifarioArticulos)) ")*SUM(DOAA.CANTIDAD)
				//.append("ELSE 0 ")
			//.append("END AS VALOR_TOTAL_TARIFA, ")
			.append("SUM(DOAA.CANTIDAD*CONSULTAEXTERNA.CALCULO_TARIFA_ARTICULO(ART.CODIGO,:codigoEsquemaTarifarioArticulos))   AS VALOR_TOTAL_TARIFA, ")
			.append("PVE.VALOR_EST_ORD_CITA, ")
			.append("PVE.VALOR_EST_SERMED_ORDEN, ")
			.append("PVE.VALOR_EST_SERMED_CITA, ")
			.append("0                                    AS ES_SERVICIO, ")
			.append("US.LOGIN AS NOMBRE_USUARIO ")
		.append("FROM AGENDA AG ")
		.append("INNER JOIN CONSULTAEXTERNA.CITA CT ON CT.CODIGO_AGENDA = AG.CODIGO ")
		/*PARA PODER SABER QUE ARTICULOS FUERON ORDENADOS LA RELACION DE LA ORDEN CON LA CITA ES OBLIGATORIA*/
		.append("INNER JOIN ORDENES.ORDENES_AMBULATORIAS OA ON OA.CITA_ASOCIADA=CT.CODIGO ")
		.append("INNER JOIN CONSULTAEXTERNA.SERVICIOS_CITA SC ")
		.append("ON SC.CODIGO_CITA = CT.CODIGO ")
		/*.append("INNER JOIN MEDICOS MED ON MED.codigo_medico = AG.CODIGO_MEDICO ")
		.append("INNER JOIN PERSONAS PER ON PER.CODIGO = MED.CODIGO_MEDICO ")
		.append("INNER JOIN USUARIOS US ON US.CODIGO_PERSONA = PER.CODIGO ")*/
		.append("INNER JOIN USUARIOS US ON US.LOGIN = CT.USUARIO_MODIFICA_ESTADO ")
        .append("INNER JOIN PERSONAS PER ON PER.CODIGO = US.CODIGO_PERSONA ")
        .append("INNER JOIN MEDICOS MED ON MED.codigo_medico = PER.CODIGO ")
		
		.append("INNER JOIN ADMINISTRACION.MEDICOS_INSTITUCIONES MED_INS ")
		.append("ON MED_INS.CODIGO_MEDICO = MED.CODIGO_MEDICO ")
		.append("INNER JOIN CONSULTAEXTERNA.UNIDADES_CONSULTA UC ")
		.append("ON UC.CODIGO = CT.UNIDAD_CONSULTA ")
		.append("INNER JOIN ORDENES.DET_ORDEN_AMB_ARTICULO DOAA ")
		.append("ON DOAA.CODIGO_ORDEN = OA.CODIGO ")
		.append("INNER JOIN INVENTARIOS.ARTICULO ART ")
		.append("ON ART.CODIGO=DOAA.ARTICULO ")
		.append("INNER JOIN INVENTARIOS.SUBGRUPO_INVENTARIO SUBGI ")
		.append("ON SUBGI.CODIGO=ART.SUBGRUPO ")
		.append("INNER JOIN INVENTARIOS.GRUPO_INVENTARIO GI ")
		.append("ON (GI.CODIGO=SUBGI.GRUPO ")
		.append("AND GI.CLASE =SUBGI.CLASE) ")
		.append("INNER JOIN INVENTARIOS.CLASE_INVENTARIO CI  ")
		.append("ON CI.CODIGO=GI.CLASE  ")
		.append("LEFT JOIN CONSULTAEXTERNA.PARAMETRICA_VALOR_ESTANDAR PVE ")
		.append("ON (PVE.CLASE_INVENTARIOS=CI.CODIGO AND PVE.UNIDAD_AGENDA= UC.CODIGO) ");
		/*FILTROS*/
		parametros.put("codigoEsquemaTarifarioArticulos", esquemaTarifarioArticulos);
		parametros.put("codigoEstadoCita", ConstantesBD.codigoEstadoCitaAtendida);
		parametros.put("codigoInstitucion", grupoOrdenadoresConsultaExternaDto.getInstitucion());
		
		where=new StringBuffer("WHERE ");		
		where.append("CT.ESTADO_CITA = :codigoEstadoCita  ")
			.append("AND MED_INS.CODIGO_INSTITUCION = :codigoInstitucion  ");
		
		if(grupoOrdenadoresConsultaExternaDto.getFechaInicialAtencionCita()!=null){
			if(grupoOrdenadoresConsultaExternaDto.getFechaFinalAtencionCita()!=null){
				where.append("AND SC.FECHA_CITA BETWEEN :fechaInicial  AND :fechaFinal ");
				parametros.put("fechaInicial", grupoOrdenadoresConsultaExternaDto.getFechaInicialAtencionCita());
				parametros.put("fechaFinal", grupoOrdenadoresConsultaExternaDto.getFechaFinalAtencionCita());
			}else{
				where.append("AND SC.FECHA_CITA >= :fechaInicial ");
				parametros.put("fechaInicial", grupoOrdenadoresConsultaExternaDto.getFechaInicialAtencionCita());
			}
		}else{
			if(grupoOrdenadoresConsultaExternaDto.getFechaFinalAtencionCita()!=null){
				where.append("AND SC.FECHA_CITA <= :fechaFinal ");
				parametros.put("fechaFinal", grupoOrdenadoresConsultaExternaDto.getFechaFinalAtencionCita());
			}
		}
		
		if(grupoOrdenadoresConsultaExternaDto.getFiltroIdCentroAtencion()!=null&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdCentroAtencion().trim().isEmpty()
				&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdCentroAtencion().trim().equals(ConstantesBD.codigoNuncaValido+"")){
			consultaOrdenadoresConsultaExt.append("INNER JOIN ADMINISTRACION.CENTRO_ATENCION CA ")
				.append("ON CA.CONSECUTIVO = AG.CENTRO_ATENCION ");
			where.append("AND CA.CONSECUTIVO = :codigoCentroAtencion ");
			parametros.put("codigoCentroAtencion", Integer.parseInt(grupoOrdenadoresConsultaExternaDto.getFiltroIdCentroAtencion().trim()));
		}
		
		if(grupoOrdenadoresConsultaExternaDto.getFiltroIdUnidadConsulta()!=null&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdUnidadConsulta().trim().isEmpty()
				&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdUnidadConsulta().trim().equals(ConstantesBD.codigoNuncaValido+"")){
			where.append("AND UC.CODIGO = :codigoUnidadConsulta ");
			parametros.put("codigoUnidadConsulta", Integer.parseInt(grupoOrdenadoresConsultaExternaDto.getFiltroIdUnidadConsulta().trim()));
		}
		
		if(grupoOrdenadoresConsultaExternaDto.getFiltroIdConvenio()!=null&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdConvenio().trim().isEmpty()
				&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdConvenio().trim().equals(ConstantesBD.codigoNuncaValido+"")){
			consultaOrdenadoresConsultaExt.append("INNER JOIN SOLICITUDES SOL ON SOL.NUMERO_SOLICITUD = SC.NUMERO_SOLICITUD ")
				.append("INNER JOIN SOLICITUDES_SUBCUENTA SOL_SUBCUENTA ON SOL_SUBCUENTA.SOLICITUD=SOL.NUMERO_SOLICITUD ")
				.append("INNER JOIN SUB_CUENTAS SUB_CUENTA ON SUB_CUENTA.SUB_CUENTA=SOL_SUBCUENTA.SUB_CUENTA ")
				.append("INNER JOIN FACTURACION.CONVENIOS CONV ON CONV.CODIGO = SUB_CUENTA.CONVENIO ");
			where.append("AND CONV.CODIGO = :codigoConvenio ");
			parametros.put("codigoConvenio", Integer.parseInt(grupoOrdenadoresConsultaExternaDto.getFiltroIdConvenio().trim()));
		}
		
		if(grupoOrdenadoresConsultaExternaDto.getFiltroProfesionalSalud()!=null
				&&grupoOrdenadoresConsultaExternaDto.getFiltroProfesionalSalud().getLoginUsuario()!=null
				&&!grupoOrdenadoresConsultaExternaDto.getFiltroProfesionalSalud().getLoginUsuario().trim().equals(ConstantesBD.codigoNuncaValido+"")){
			where.append("AND US.LOGIN = :usuario  ");
			parametros.put("usuario", grupoOrdenadoresConsultaExternaDto.getFiltroProfesionalSalud().getLoginUsuario());
		}
		
		consultaOrdenadoresConsultaExt.append(where);
		if(grupoOrdenadoresConsultaExternaDto.getFiltroIdClaseInventario()!=null&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdClaseInventario().trim().isEmpty()
				&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdClaseInventario().trim().equals(ConstantesBD.codigoNuncaValido+"")){
			consultaOrdenadoresConsultaExt.append("AND CI.CODIGO = :codigoClaseInventario ");
			parametros.put("codigoClaseInventario", Integer.parseInt(grupoOrdenadoresConsultaExternaDto.getFiltroIdClaseInventario().trim()));
		}
		/*FIN FILTROS*/
		consultaOrdenadoresConsultaExt.append("GROUP BY UC.CODIGO, ")
			.append("UC.DESCRIPCION, ")
			.append("CI.CODIGO, ")
			.append("CI.NOMBRE, ")
			.append("PER.TIPO_IDENTIFICACION, ")
			.append("PER.NUMERO_IDENTIFICACION, ")
			.append("PER.PRIMER_APELLIDO, ")
			.append("PER.SEGUNDO_APELLIDO, ")
			.append("PER.PRIMER_NOMBRE, ")
			.append("PER.SEGUNDO_NOMBRE, ")
			.append("MED.NUMERO_REGISTRO, ")
			.append("ADMINISTRACION.GETESPECIALIDADESMEDICO1(MED.CODIGO_MEDICO, ','), ")
			.append("PVE.VALOR_EST_ORD_CITA, ")
			.append("PVE.VALOR_EST_SERMED_ORDEN, ")
			.append("PVE.VALOR_EST_SERMED_CITA, ")
			.append("US.LOGIN ")
		.append("UNION ")
		//--ORDENES AMB CITA SERVICIOS
		.append("SELECT DISTINCT UC.CODIGO AS CODIGO_UNIDAD_CONSULTA, ")
			.append("UC.DESCRIPCION          AS DESCRIPCION_UNIDAD_CONSULTA, ")
			.append("GS.CODIGO               AS CODIGO_GS_CI, ")
			.append("GS.DESCRIPCION          AS DESCRIPCION_GS_CI, ")
			.append("PER.TIPO_IDENTIFICACION, ")
			.append("PER.NUMERO_IDENTIFICACION, ")
			.append("PER.PRIMER_APELLIDO, ")
			.append("PER.SEGUNDO_APELLIDO, ")
			.append("PER.PRIMER_NOMBRE, ")
			.append("PER.SEGUNDO_NOMBRE, ")
			.append("MED.NUMERO_REGISTRO, ")
			.append("ADMINISTRACION.GETESPECIALIDADESMEDICO1(MED.CODIGO_MEDICO, ',') AS ESPECIALIDADES_MED, ")
			.append("COUNT (DISTINCT OA.CODIGO)                    AS CANTIDAD_ORD_AMB_GEN, ")
			.append("COUNT (DISTINCT CT.CODIGO)                    AS CANTIDAD_CITAS_ATENDIDAS, ")
			.append("COUNT (SERV.CODIGO)                           AS CANTIDAD_SERV_MED, ")
			//.append("CASE WHEN SERV.CODIGO IS NOT NULL ")
				//.append("THEN SUM(CONSULTAEXTERNA.CALCULO_TARIFA_SERVICIO(SERV.CODIGO,:codigoEsquemaTarifarioServicios)) ")
				//.append("ELSE 0 ")
			//.append("END AS VALOR_TOTAL_TARIFA, ")
			.append("SUM(CONSULTAEXTERNA.CALCULO_TARIFA_SERVICIO(SERV.CODIGO,:codigoEsquemaTarifarioServicios)) AS VALOR_TOTAL_TARIFA, ")
			.append("PVE.VALOR_EST_ORD_CITA, ")
			.append("PVE.VALOR_EST_SERMED_ORDEN, ")
			.append("PVE.VALOR_EST_SERMED_CITA, ")
			.append("1                                    AS ES_SERVICIO, ")
			.append("US.LOGIN AS NOMBRE_USUARIO ")
		//.append("FROM ADMINISTRACION.PERSONAS PER ")
		.append("FROM AGENDA AG ")
		.append("INNER JOIN CONSULTAEXTERNA.CITA CT ON CT.CODIGO_AGENDA = AG.CODIGO ")
		/*PARA PODER SABER QUE SERVICIOS FUERON ORDENADOS LA RELACION DE LA ORDEN CON LA CITA ES OBLIGATORIA*/
		.append("INNER JOIN ORDENES.ORDENES_AMBULATORIAS OA ON OA.CITA_ASOCIADA=CT.CODIGO ")
		.append("INNER JOIN CONSULTAEXTERNA.SERVICIOS_CITA SC ")
		.append("ON SC.CODIGO_CITA = CT.CODIGO ")
		/*.append("INNER JOIN MEDICOS MED ON MED.codigo_medico = AG.CODIGO_MEDICO ")
		.append("INNER JOIN PERSONAS PER ON PER.CODIGO = MED.CODIGO_MEDICO ")
		.append("INNER JOIN USUARIOS US ON US.CODIGO_PERSONA = PER.CODIGO ")*/
		.append("INNER JOIN USUARIOS US ON US.LOGIN = CT.USUARIO_MODIFICA_ESTADO ")
        .append("INNER JOIN PERSONAS PER ON PER.CODIGO = US.CODIGO_PERSONA ")
        .append("INNER JOIN MEDICOS MED ON MED.codigo_medico = PER.CODIGO ")
		
		.append("INNER JOIN ADMINISTRACION.MEDICOS_INSTITUCIONES MED_INS ")
		.append("ON MED_INS.CODIGO_MEDICO = MED.CODIGO_MEDICO ")
		.append("INNER JOIN CONSULTAEXTERNA.UNIDADES_CONSULTA UC ")
		.append("ON UC.CODIGO = CT.UNIDAD_CONSULTA ")
		.append("INNER JOIN ORDENES.DET_ORDEN_AMB_SERVICIO DOAS ")
		.append("ON DOAS.CODIGO_ORDEN = OA.CODIGO ")
		.append("INNER JOIN FACTURACION.SERVICIOS SERV ")
		.append("ON SERV.CODIGO=DOAS.SERVICIO ")
		.append("INNER JOIN FACTURACION.GRUPOS_SERVICIOS GS ")
		.append("ON GS.CODIGO = SERV.GRUPO_SERVICIO ")
		.append("LEFT JOIN CONSULTAEXTERNA.PARAMETRICA_VALOR_ESTANDAR PVE ")
		.append("ON (PVE.GRUPOS_SERVICIO=GS.CODIGO AND PVE.UNIDAD_AGENDA= UC.CODIGO) ");
		
		/*FILTROS*/
		parametros.put("codigoEsquemaTarifarioServicios", esquemaTarifarioServicios);
		if(grupoOrdenadoresConsultaExternaDto.getFiltroIdCentroAtencion()!=null&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdCentroAtencion().trim().isEmpty()
				&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdCentroAtencion().trim().equals(ConstantesBD.codigoNuncaValido+"")){
			consultaOrdenadoresConsultaExt.append("INNER JOIN ADMINISTRACION.CENTRO_ATENCION CA ")
				.append("ON CA.CONSECUTIVO = AG.CENTRO_ATENCION ");
		}
		if(grupoOrdenadoresConsultaExternaDto.getFiltroIdConvenio()!=null&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdConvenio().trim().isEmpty()
				&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdConvenio().trim().equals(ConstantesBD.codigoNuncaValido+"")){
			consultaOrdenadoresConsultaExt.append("INNER JOIN SOLICITUDES SOL ON SOL.NUMERO_SOLICITUD = SC.NUMERO_SOLICITUD ")
				.append("INNER JOIN SOLICITUDES_SUBCUENTA SOL_SUBCUENTA ON SOL_SUBCUENTA.SOLICITUD=SOL.NUMERO_SOLICITUD ")
				.append("INNER JOIN SUB_CUENTAS SUB_CUENTA ON SUB_CUENTA.SUB_CUENTA=SOL_SUBCUENTA.SUB_CUENTA ")
				.append("INNER JOIN FACTURACION.CONVENIOS CONV ON CONV.CODIGO = SUB_CUENTA.CONVENIO ");
		}
		consultaOrdenadoresConsultaExt.append(where);
		if(grupoOrdenadoresConsultaExternaDto.getFiltroIdGrupoServicio()!=null&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdGrupoServicio().trim().isEmpty()
				&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdGrupoServicio().trim().equals(ConstantesBD.codigoNuncaValido+"")){
			consultaOrdenadoresConsultaExt.append("AND GS.CODIGO = :codigoGrupoServicio ");
			parametros.put("codigoGrupoServicio", Integer.parseInt(grupoOrdenadoresConsultaExternaDto.getFiltroIdGrupoServicio().trim()));
		}
		/*FIN FILTROS*/
		
		consultaOrdenadoresConsultaExt.append("GROUP BY UC.CODIGO, ")
			.append("UC.DESCRIPCION, ")
			.append("GS.CODIGO, ")
			.append("GS.DESCRIPCION, ")
			.append("PER.TIPO_IDENTIFICACION, ")
			.append("PER.NUMERO_IDENTIFICACION, ")
			.append("PER.PRIMER_APELLIDO, ")
			.append("PER.SEGUNDO_APELLIDO, ")
			.append("PER.PRIMER_NOMBRE, ")
			.append("PER.SEGUNDO_NOMBRE, ")
			.append("MED.NUMERO_REGISTRO, ")
			.append("ADMINISTRACION.GETESPECIALIDADESMEDICO1(MED.CODIGO_MEDICO, ','), ")
			.append("PVE.VALOR_EST_ORD_CITA, ")
			.append("PVE.VALOR_EST_SERMED_ORDEN, ")
			.append("PVE.VALOR_EST_SERMED_CITA, ")
			.append("US.LOGIN ");
		return consultaOrdenadoresConsultaExt;
	}
	
	/**
	 * Consulta para el reporte de ordenadores de consulta externa, para grupos de servicio y clases de inventario sirve para PDF y EXCEL
	 * 
	 * @param grupoOrdenadoresConsultaExternaDto
	 * @param esquemaTarifarioArticulos
	 * @param esquemaTarifarioServicios
	 * @return
	 * @throws BDException
	 * @author jeilones
	 * @created 19/11/2012
	 */
	@SuppressWarnings("unchecked")
	public GrupoOrdenadoresConsultaExternaDto consultarIdentificadorOrdenadoresConsultaGrupoClase(GrupoOrdenadoresConsultaExternaDto grupoOrdenadoresConsultaExternaDto,int esquemaTarifarioArticulos,int esquemaTarifarioServicios)throws BDException{
		
		try{
			persistenciaSvc=new PersistenciaSvc();
			
			Map<String, Object>parametros=new LinkedHashMap<String, Object>(0);
			StringBuffer consultaOrdenadoresConsultaExt=new StringBuffer("");
			consultaOrdenadoresConsultaExt.append("SELECT RS.CODIGO_UNIDAD_CONSULTA, ")
				.append("RS.DESCRIPCION_UNIDAD_CONSULTA, ")
				.append("RS.CODIGO_GS_CI, ")
				.append("RS.DESCRIPCION_GS_CI, ")
				.append("RS.TIPO_IDENTIFICACION, ")
				.append("RS.NUMERO_IDENTIFICACION, ")
				.append("RS.PRIMER_APELLIDO, ")
				.append("RS.SEGUNDO_APELLIDO, ")
				.append("RS.PRIMER_NOMBRE, ")
				.append("RS.SEGUNDO_NOMBRE, ")
				//.append("RS.NUMERO_REGISTRO, ")
				//.append("RS.ESPECIALIDADES_MED, ")
				.append("RS.CANTIDAD_ORD_AMB_GEN, ")
				.append("RS.CANTIDAD_CITAS_ATENDIDAS, ")
				.append("RS.ES_SERVICIO, ")
				.append("RS.CANTIDAD_ORD_AMB_GEN AS PROM_CANT_ORD_X_CITA,  ")
				.append("CASE WHEN RS.CANTIDAD_ORD_AMB_GEN>0 ")
					.append("THEN RS.CANTIDAD_SERV_MED/RS.CANTIDAD_ORD_AMB_GEN ")
					.append("ELSE 0 ")
				.append("END AS PROM_SERV_MED_X_ORDEN, ")
				.append("RS.CANTIDAD_SERV_MED AS CANTIDAD_SERV_MED, ")
				.append("RS.VALOR_TOTAL_TARIFA AS VALOR_TOTAL_TARIFA, ")
				.append("RS.VALOR_EST_ORD_CITA, ")
				.append("RS.VALOR_EST_SERMED_ORDEN, ")
				.append("RS.VALOR_EST_SERMED_CITA, ")
				.append("RS.NOMBRE_USUARIO ")
			.append("FROM  ")
				.append("( ");
			
			consultaOrdenadoresConsultaExt.append(crearConsultaOrdenadoresPorGrupoClase(grupoOrdenadoresConsultaExternaDto,esquemaTarifarioArticulos,esquemaTarifarioServicios,parametros));
				
			consultaOrdenadoresConsultaExt.append(") RS ")
			.append("ORDER BY ")
				.append("RS.ES_SERVICIO, ")
				.append("RS.DESCRIPCION_GS_CI, ")
				.append("RS.DESCRIPCION_UNIDAD_CONSULTA, ")
				.append("RS.PRIMER_NOMBRE, ")
				.append("RS.SEGUNDO_NOMBRE, ")
				.append("RS.PRIMER_APELLIDO, ")
				.append("RS.SEGUNDO_APELLIDO ");
			
			SQLQuery sqlQuery=persistenciaSvc.getSession().createSQLQuery(consultaOrdenadoresConsultaExt.toString());
			sqlQuery.addScalar("CODIGO_UNIDAD_CONSULTA", new IntegerType());
			sqlQuery.addScalar("DESCRIPCION_UNIDAD_CONSULTA", new StringType());
			sqlQuery.addScalar("CODIGO_GS_CI", new IntegerType());
			
			sqlQuery.addScalar("DESCRIPCION_GS_CI", new StringType());
			sqlQuery.addScalar("TIPO_IDENTIFICACION", new StringType());
			sqlQuery.addScalar("NUMERO_IDENTIFICACION", new StringType());
			sqlQuery.addScalar("PRIMER_APELLIDO", new StringType());
			sqlQuery.addScalar("SEGUNDO_APELLIDO", new StringType());
			sqlQuery.addScalar("PRIMER_NOMBRE", new StringType());
			sqlQuery.addScalar("SEGUNDO_NOMBRE", new StringType());
			sqlQuery.addScalar("CANTIDAD_ORD_AMB_GEN", new IntegerType());
			sqlQuery.addScalar("CANTIDAD_CITAS_ATENDIDAS", new IntegerType());
			sqlQuery.addScalar("ES_SERVICIO", new IntegerType());
			sqlQuery.addScalar("PROM_CANT_ORD_X_CITA", new DoubleType());
			sqlQuery.addScalar("PROM_SERV_MED_X_ORDEN", new DoubleType());
			sqlQuery.addScalar("CANTIDAD_SERV_MED", new DoubleType());
			sqlQuery.addScalar("VALOR_TOTAL_TARIFA", new DoubleType());
			sqlQuery.addScalar("VALOR_EST_ORD_CITA", new DoubleType());
			sqlQuery.addScalar("VALOR_EST_SERMED_ORDEN", new DoubleType());
			sqlQuery.addScalar("VALOR_EST_SERMED_CITA", new DoubleType());
			sqlQuery.addScalar("NOMBRE_USUARIO", new StringType());
			
			Set<String>keyParemetros=parametros.keySet();
			for(String key:keyParemetros){
				sqlQuery.setParameter(key, parametros.get(key));
			}
			List<Object[]>resultado=sqlQuery.list();
			
			final int CODIGO_UNIDAD_CONSULTA=0;
			final int DESCRIPCION_UNIDAD_CONSULTA=1;
			final int CODIGO_GS_CI=2;
			final int DESCRIPCION_GS_CI=3;
			final int TIPO_IDENTIFICACION=4;
			final int NUMERO_IDENTIFICACION=5;
			final int PRIMER_APELLIDO=6;
			final int SEGUNDO_APELLIDO=7;
			final int PRIMER_NOMBRE=8;
			final int SEGUNDO_NOMBRE=9;
			final int CANTIDAD_ORD_AMB_GEN=10;
			//final int CANTIDAD_CITAS_ATENDIDAS=11;
			final int ES_SERVICIO=12;
			//final int PROM_CANT_ORD_X_CITA=13;
			final int PROM_SERV_MED_X_ORDEN=14;
			final int CANTIDAD_SERV_MED=15;
			final int VALOR_TOTAL_TARIFA=16;
			final int VALOR_EST_ORD_CITA=17;
			final int VALOR_EST_SERMED_ORDEN=18;
			final int VALOR_EST_SERMED_CITA=19;
			final int NOMBRE_USUARIO=20;
			
			List<GrupoServicioDto>gruposServicio=new ArrayList<GrupoServicioDto>(0);
			List<ClaseInventarioDto>clasesInventario=new ArrayList<ClaseInventarioDto>(0);
			
			Map<Integer, GrupoServicioDto>mapaGruposServicio=new LinkedHashMap<Integer, GrupoServicioDto>(0);
			Map<Integer, ClaseInventarioDto>mapaClasesInventario=new LinkedHashMap<Integer, ClaseInventarioDto>(0);
			for(Object[]fila:resultado){
				Map<Integer, DtoUnidadesConsulta>mapaUnidadesConsulta=null;
				DtoUnidadesConsulta unidadConsulta=null;
				GrupoServicioDto grupoServicioDto=null;
				ClaseInventarioDto claseInventarioDto=null;
				
				boolean esServicio=false;
				
				if(((Integer)fila[ES_SERVICIO]).intValue()==1){
					esServicio=true;
					if(mapaGruposServicio.containsKey((Integer) fila[CODIGO_GS_CI])){
						grupoServicioDto=mapaGruposServicio.get((Integer) fila[CODIGO_GS_CI]);
					}else{
						grupoServicioDto=new GrupoServicioDto();
						grupoServicioDto.setCodigo((Integer) fila[CODIGO_GS_CI]);
						grupoServicioDto.setDescripcion((String) fila[DESCRIPCION_GS_CI]);
						grupoServicioDto.setListaUnidadesConsulta(new ArrayList<DtoUnidadesConsulta>(0));
						grupoServicioDto.setMapaUnidadesConsulta(new HashMap<Integer, DtoUnidadesConsulta>(0));
						
						gruposServicio.add(grupoServicioDto);
						
						mapaGruposServicio.put((Integer) fila[CODIGO_GS_CI],grupoServicioDto);
					}
					
					int cantidadOrdAmbGenGrupoServicio=grupoServicioDto.getCantidadOrdenesAmbGeneradas();
					cantidadOrdAmbGenGrupoServicio+=((Integer)fila[CANTIDAD_ORD_AMB_GEN]).intValue();
					grupoServicioDto.setCantidadOrdenesAmbGeneradas(cantidadOrdAmbGenGrupoServicio);
					
					mapaUnidadesConsulta=grupoServicioDto.getMapaUnidadesConsulta();
					
				}else{
					if(mapaClasesInventario.containsKey((Integer) fila[CODIGO_GS_CI])){
						claseInventarioDto=mapaClasesInventario.get((Integer) fila[CODIGO_GS_CI]);
					}else{
						claseInventarioDto=new ClaseInventarioDto();
						claseInventarioDto.setCodigo((Integer) fila[CODIGO_GS_CI]);
						claseInventarioDto.setNombre((String) fila[DESCRIPCION_GS_CI]);
						claseInventarioDto.setListaUnidadesConsulta(new ArrayList<DtoUnidadesConsulta>(0));
						claseInventarioDto.setMapaUnidadesConsulta(new HashMap<Integer, DtoUnidadesConsulta>(0));
						
						clasesInventario.add(claseInventarioDto);
						
						mapaClasesInventario.put((Integer) fila[CODIGO_GS_CI],claseInventarioDto);
					}
					
					int cantidadOrdAmbGenClaseInventario=claseInventarioDto.getCantidadOrdenesAmbGeneradas();
					cantidadOrdAmbGenClaseInventario+=((Integer)fila[CANTIDAD_ORD_AMB_GEN]).intValue();
					claseInventarioDto.setCantidadOrdenesAmbGeneradas(cantidadOrdAmbGenClaseInventario);
					
					mapaUnidadesConsulta=claseInventarioDto.getMapaUnidadesConsulta();
				}
				
				if(mapaUnidadesConsulta.containsKey((Integer)fila[CODIGO_UNIDAD_CONSULTA])){
					unidadConsulta=mapaUnidadesConsulta.get((Integer)fila[CODIGO_UNIDAD_CONSULTA]);
				}else{
					unidadConsulta=new DtoUnidadesConsulta();
					unidadConsulta.setCodigo((Integer) fila[CODIGO_UNIDAD_CONSULTA]);
					unidadConsulta.setDescripcion((String) fila[DESCRIPCION_UNIDAD_CONSULTA]);
					unidadConsulta.setListaProfesionales(new ArrayList<ProfesionalSaludDto>(0));
					
					unidadConsulta.setValorEstCita((Double) fila[VALOR_EST_ORD_CITA]);
					unidadConsulta.setValorEstServMedOrdenAmb((Double) fila[VALOR_EST_SERMED_ORDEN]);
					unidadConsulta.setValorEstServMedCita((Double) fila[VALOR_EST_SERMED_CITA]);
					
					mapaUnidadesConsulta.put(unidadConsulta.getCodigo(), unidadConsulta);
					
					if(((Integer)fila[ES_SERVICIO]).intValue()==1){
						grupoServicioDto.getListaUnidadesConsulta().add(unidadConsulta);
						grupoServicioDto.setGrupoUnidadesConsulta(new JRBeanCollectionDataSource(grupoServicioDto.getListaUnidadesConsulta(),false));
					}else{
						claseInventarioDto.getListaUnidadesConsulta().add(unidadConsulta);
						claseInventarioDto.setGrupoUnidadesConsulta(new JRBeanCollectionDataSource(claseInventarioDto.getListaUnidadesConsulta(),false));
					}
				}
				
				int cantidadOrdAmbGenUnidadConsulta=unidadConsulta.getCantidadOrdenesAmbGeneradas();
				cantidadOrdAmbGenUnidadConsulta+=((Integer)fila[CANTIDAD_ORD_AMB_GEN]).intValue();
				unidadConsulta.setCantidadOrdenesAmbGeneradas(cantidadOrdAmbGenUnidadConsulta);
				
				ProfesionalSaludDto profesionalSaludDto=new ProfesionalSaludDto();
				profesionalSaludDto.setTipoIdentificacion((String) fila[TIPO_IDENTIFICACION]);
				profesionalSaludDto.setNumeroIdentificacion((String) fila[NUMERO_IDENTIFICACION]);
				
				profesionalSaludDto.setPrimerApellido((String) fila[PRIMER_APELLIDO]);
				profesionalSaludDto.setSegundoApellido((String) fila[SEGUNDO_APELLIDO]);
				profesionalSaludDto.setPrimerNombre((String) fila[PRIMER_NOMBRE]);
				profesionalSaludDto.setSegundoNombre((String) fila[SEGUNDO_NOMBRE]);
				profesionalSaludDto.setLoginUsuario((String) fila[NOMBRE_USUARIO]);
				
				
				profesionalSaludDto.setCantidadOrdenesAmbGeneradas((Integer) fila[CANTIDAD_ORD_AMB_GEN]);
				
				int cantidadCitasPorGrupoServClaseInv=consultarCantidadCitasAtendidas(grupoOrdenadoresConsultaExternaDto.getFechaInicialAtencionCita(), grupoOrdenadoresConsultaExternaDto.getFechaFinalAtencionCita(), profesionalSaludDto.getLoginUsuario(), unidadConsulta.getCodigo(),esServicio, esServicio?grupoServicioDto.getCodigo():claseInventarioDto.getCodigo());
				profesionalSaludDto.setCantidadCitasAtendidas(cantidadCitasPorGrupoServClaseInv);
				
				
				profesionalSaludDto.setPromedioServMedOrdenAmb((Double)fila[PROM_SERV_MED_X_ORDEN]);
				
				if(cantidadCitasPorGrupoServClaseInv>0){
					profesionalSaludDto.setPromedioOrdenesAmbXCitas(((Integer) fila[CANTIDAD_ORD_AMB_GEN]).doubleValue()/(double)cantidadCitasPorGrupoServClaseInv);
					profesionalSaludDto.setPromedioServMedCita(((Double)fila[CANTIDAD_SERV_MED]).doubleValue()/(double)cantidadCitasPorGrupoServClaseInv);
					
					if(fila[VALOR_TOTAL_TARIFA]!=null){
						double promValorTarifa=((Double)fila[VALOR_TOTAL_TARIFA]).doubleValue()/(double)cantidadCitasPorGrupoServClaseInv;
						profesionalSaludDto.setTotalCostoPromedio(promValorTarifa);
						
						double totalCostoPromedioCitaUnidadConsulta=unidadConsulta.getTotalCostoPromedioCita()+promValorTarifa;
						unidadConsulta.setTotalCostoPromedioCita(totalCostoPromedioCitaUnidadConsulta);
					}
				}
				unidadConsulta.getListaProfesionales().add(profesionalSaludDto);
				
				unidadConsulta.setGrupoProfesionales(new JRBeanCollectionDataSource(unidadConsulta.getListaProfesionales(),false));
				
			}
			
			grupoOrdenadoresConsultaExternaDto.setGruposServicio(gruposServicio);
			grupoOrdenadoresConsultaExternaDto.setClasesInventario(clasesInventario);
			
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		return grupoOrdenadoresConsultaExternaDto;
	}
	
	/**
	 * Se crea la consulta para el reporte de identificadores de ordenadores de consulta externa por grupos de servicio y clases de inventario
	 * 
	 * @param grupoOrdenadoresConsultaExternaDto
	 * @param esquemaTarifarioArticulos
	 * @param esquemaTarifarioServicios 
	 * @return
	 * @author jeilones
	 * @created 19/11/2012
	 */
	private StringBuffer crearConsultaOrdenadoresPorGrupoClase(
			GrupoOrdenadoresConsultaExternaDto grupoOrdenadoresConsultaExternaDto,
			int esquemaTarifarioArticulos,int esquemaTarifarioServicios, Map<String, Object> parametros) {
		StringBuffer consultaOrdenadoresConsultaExt=new StringBuffer("");
		
		
		//CITAS QUE NO ESTAN ASOCIADAS A ORDENES AMBULATORIAS
		//--ORDENES AMB CITA ARTICULO
	    consultaOrdenadoresConsultaExt.append("SELECT DISTINCT UC.CODIGO AS CODIGO_UNIDAD_CONSULTA, ")
			.append("UC.DESCRIPCION          AS DESCRIPCION_UNIDAD_CONSULTA, ")
			.append("CI.CODIGO               AS CODIGO_GS_CI, ")
			.append("CI.NOMBRE               AS DESCRIPCION_GS_CI, ")
			.append("PER.TIPO_IDENTIFICACION, ")
			.append("PER.NUMERO_IDENTIFICACION, ")
			.append("PER.PRIMER_APELLIDO, ")
			.append("PER.SEGUNDO_APELLIDO, ")
			.append("PER.PRIMER_NOMBRE, ")
			.append("PER.SEGUNDO_NOMBRE, ")
			.append("COUNT (DISTINCT OA.CODIGO)                    AS CANTIDAD_ORD_AMB_GEN, ")
			.append("COUNT (DISTINCT CT.CODIGO)                    AS CANTIDAD_CITAS_ATENDIDAS, ")
			.append("SUM(DOAA.CANTIDAD)                            AS CANTIDAD_SERV_MED, ")
			//.append("CASE WHEN ART.CODIGO IS NOT NULL ")
				//.append("THEN SUM(CONSULTAEXTERNA.CALCULO_TARIFA_ARTICULO(ART.CODIGO,:codigoEsquemaTarifarioArticulos)) ")*SUM(DOAA.CANTIDAD)
				//.append("ELSE 0 ")
			//.append("END AS VALOR_TOTAL_TARIFA, ")
			.append("SUM(DOAA.CANTIDAD*CONSULTAEXTERNA.CALCULO_TARIFA_ARTICULO(ART.CODIGO,:codigoEsquemaTarifarioArticulos))   AS VALOR_TOTAL_TARIFA, ")
			.append("PVE.VALOR_EST_ORD_CITA, ")
			.append("PVE.VALOR_EST_SERMED_ORDEN, ")
			.append("PVE.VALOR_EST_SERMED_CITA, ")
			.append("0                                    AS ES_SERVICIO, ")
			.append("US.LOGIN AS NOMBRE_USUARIO ")
		.append("FROM AGENDA AG ")
		.append("INNER JOIN CONSULTAEXTERNA.CITA CT ON CT.CODIGO_AGENDA = AG.CODIGO ")
		/*PARA PODER SABER QUE ARTICULOS FUERON ORDENADOS LA RELACION DE LA ORDEN CON LA CITA ES OBLIGATORIA*/
		.append("INNER JOIN ORDENES.ORDENES_AMBULATORIAS OA ON OA.CITA_ASOCIADA=CT.CODIGO ")
		.append("INNER JOIN CONSULTAEXTERNA.SERVICIOS_CITA SC ")
		.append("ON SC.CODIGO_CITA = CT.CODIGO ")
		/*.append("INNER JOIN MEDICOS MED ON MED.codigo_medico = AG.CODIGO_MEDICO ")
		.append("INNER JOIN PERSONAS PER ON PER.CODIGO = MED.CODIGO_MEDICO ")
		.append("INNER JOIN USUARIOS US ON US.CODIGO_PERSONA = PER.CODIGO ")*/
		.append("INNER JOIN USUARIOS US ON US.LOGIN = CT.USUARIO_MODIFICA_ESTADO ")
        .append("INNER JOIN PERSONAS PER ON PER.CODIGO = US.CODIGO_PERSONA ")
        .append("INNER JOIN MEDICOS MED ON MED.codigo_medico = PER.CODIGO ")
		
		.append("INNER JOIN ADMINISTRACION.MEDICOS_INSTITUCIONES MED_INS ")
		.append("ON MED_INS.CODIGO_MEDICO = MED.CODIGO_MEDICO ")
		.append("INNER JOIN CONSULTAEXTERNA.UNIDADES_CONSULTA UC ")
		.append("ON UC.CODIGO = CT.UNIDAD_CONSULTA ")
		.append("INNER JOIN ORDENES.DET_ORDEN_AMB_ARTICULO DOAA ")
		.append("ON DOAA.CODIGO_ORDEN = OA.CODIGO ")
		.append("INNER JOIN INVENTARIOS.ARTICULO ART ")
		.append("ON ART.CODIGO=DOAA.ARTICULO ")
		.append("INNER JOIN INVENTARIOS.SUBGRUPO_INVENTARIO SUBGI ")
		.append("ON SUBGI.CODIGO=ART.SUBGRUPO ")
		.append("INNER JOIN INVENTARIOS.GRUPO_INVENTARIO GI ")
		.append("ON (GI.CODIGO=SUBGI.GRUPO ")
		.append("AND GI.CLASE =SUBGI.CLASE) ")
		.append("INNER JOIN INVENTARIOS.CLASE_INVENTARIO CI  ")
		.append("ON CI.CODIGO=GI.CLASE  ")
		.append("LEFT JOIN CONSULTAEXTERNA.PARAMETRICA_VALOR_ESTANDAR PVE ")
		.append("ON (PVE.CLASE_INVENTARIOS=CI.CODIGO AND PVE.UNIDAD_AGENDA= UC.CODIGO) ");
		/*FILTROS*/
		parametros.put("codigoEsquemaTarifarioArticulos", esquemaTarifarioArticulos);
		parametros.put("codigoEstadoCita", ConstantesBD.codigoEstadoCitaAtendida);
		parametros.put("codigoInstitucion", grupoOrdenadoresConsultaExternaDto.getInstitucion());
		
		//Where
		StringBuffer where=new StringBuffer("WHERE ");		
		where.append("CT.ESTADO_CITA = :codigoEstadoCita  ")
			.append("AND MED_INS.CODIGO_INSTITUCION = :codigoInstitucion  ");
		
		if(grupoOrdenadoresConsultaExternaDto.getFechaInicialAtencionCita()!=null){
			if(grupoOrdenadoresConsultaExternaDto.getFechaFinalAtencionCita()!=null){
				where.append("AND SC.FECHA_CITA BETWEEN :fechaInicial  AND :fechaFinal ");
				parametros.put("fechaInicial", grupoOrdenadoresConsultaExternaDto.getFechaInicialAtencionCita());
				parametros.put("fechaFinal", grupoOrdenadoresConsultaExternaDto.getFechaFinalAtencionCita());
			}else{
				where.append("AND SC.FECHA_CITA >= :fechaInicial ");
				parametros.put("fechaInicial", grupoOrdenadoresConsultaExternaDto.getFechaInicialAtencionCita());
			}
		}else{
			if(grupoOrdenadoresConsultaExternaDto.getFechaFinalAtencionCita()!=null){
				where.append("AND SC.FECHA_CITA <= :fechaFinal ");
				parametros.put("fechaFinal", grupoOrdenadoresConsultaExternaDto.getFechaFinalAtencionCita());
			}
		}
		
		if(grupoOrdenadoresConsultaExternaDto.getFiltroIdCentroAtencion()!=null&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdCentroAtencion().trim().isEmpty()
				&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdCentroAtencion().trim().equals(ConstantesBD.codigoNuncaValido+"")){
			consultaOrdenadoresConsultaExt.append("INNER JOIN ADMINISTRACION.CENTRO_ATENCION CA ")
				.append("ON CA.CONSECUTIVO = AG.CENTRO_ATENCION ");
			where.append("AND CA.CONSECUTIVO = :codigoCentroAtencion ");
			parametros.put("codigoCentroAtencion", Integer.parseInt(grupoOrdenadoresConsultaExternaDto.getFiltroIdCentroAtencion().trim()));
		}
		
		if(grupoOrdenadoresConsultaExternaDto.getFiltroIdUnidadConsulta()!=null&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdUnidadConsulta().trim().isEmpty()
				&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdUnidadConsulta().trim().equals(ConstantesBD.codigoNuncaValido+"")){
			where.append("AND UC.CODIGO = :codigoUnidadConsulta ");
			parametros.put("codigoUnidadConsulta", Integer.parseInt(grupoOrdenadoresConsultaExternaDto.getFiltroIdUnidadConsulta().trim()));
		}
		
		if(grupoOrdenadoresConsultaExternaDto.getFiltroIdConvenio()!=null&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdConvenio().trim().isEmpty()
				&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdConvenio().trim().equals(ConstantesBD.codigoNuncaValido+"")){
			consultaOrdenadoresConsultaExt.append("INNER JOIN SOLICITUDES SOL ON SOL.NUMERO_SOLICITUD = SC.NUMERO_SOLICITUD ")
				.append("INNER JOIN SOLICITUDES_SUBCUENTA SOL_SUBCUENTA ON SOL_SUBCUENTA.SOLICITUD=SOL.NUMERO_SOLICITUD ")
				.append("INNER JOIN SUB_CUENTAS SUB_CUENTA ON SUB_CUENTA.SUB_CUENTA=SOL_SUBCUENTA.SUB_CUENTA ")
				.append("INNER JOIN FACTURACION.CONVENIOS CONV ON CONV.CODIGO = SUB_CUENTA.CONVENIO ");
			where.append("AND CONV.CODIGO = :codigoConvenio ");
			parametros.put("codigoConvenio", Integer.parseInt(grupoOrdenadoresConsultaExternaDto.getFiltroIdConvenio().trim()));
		}
		
		if(grupoOrdenadoresConsultaExternaDto.getFiltroProfesionalSalud()!=null
				&&grupoOrdenadoresConsultaExternaDto.getFiltroProfesionalSalud().getLoginUsuario()!=null
				&&!grupoOrdenadoresConsultaExternaDto.getFiltroProfesionalSalud().getLoginUsuario().trim().equals(ConstantesBD.codigoNuncaValido+"")){
			where.append("AND US.LOGIN = :usuario  ");
			parametros.put("usuario", grupoOrdenadoresConsultaExternaDto.getFiltroProfesionalSalud().getLoginUsuario());
		}
		
		consultaOrdenadoresConsultaExt.append(where);
		if(grupoOrdenadoresConsultaExternaDto.getFiltroIdClaseInventario()!=null&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdClaseInventario().trim().isEmpty()
				&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdClaseInventario().trim().equals(ConstantesBD.codigoNuncaValido+"")){
			consultaOrdenadoresConsultaExt.append("AND CI.CODIGO = :codigoClaseInventario ");
			parametros.put("codigoClaseInventario", Integer.parseInt(grupoOrdenadoresConsultaExternaDto.getFiltroIdClaseInventario().trim()));
		}
		/*FIN FILTROS*/
		consultaOrdenadoresConsultaExt.append("GROUP BY UC.CODIGO, ")
			.append("UC.DESCRIPCION, ")
			.append("CI.CODIGO, ")
			.append("CI.NOMBRE, ")
			.append("PER.TIPO_IDENTIFICACION, ")
			.append("PER.NUMERO_IDENTIFICACION, ")
			.append("PER.PRIMER_APELLIDO, ")
			.append("PER.SEGUNDO_APELLIDO, ")
			.append("PER.PRIMER_NOMBRE, ")
			.append("PER.SEGUNDO_NOMBRE, ")
			.append("PVE.VALOR_EST_ORD_CITA, ")
			.append("PVE.VALOR_EST_SERMED_ORDEN, ")
			.append("PVE.VALOR_EST_SERMED_CITA, ")
			.append("US.LOGIN ")
		.append("UNION ")
		//--ORDENES AMB CITA SERVICIOS
		.append("SELECT DISTINCT UC.CODIGO AS CODIGO_UNIDAD_CONSULTA, ")
			.append("UC.DESCRIPCION          AS DESCRIPCION_UNIDAD_CONSULTA, ")
			.append("GS.CODIGO               AS CODIGO_GS_CI, ")
			.append("GS.DESCRIPCION          AS DESCRIPCION_GS_CI, ")
			.append("PER.TIPO_IDENTIFICACION, ")
			.append("PER.NUMERO_IDENTIFICACION, ")
			.append("PER.PRIMER_APELLIDO, ")
			.append("PER.SEGUNDO_APELLIDO, ")
			.append("PER.PRIMER_NOMBRE, ")
			.append("PER.SEGUNDO_NOMBRE, ")
			.append("COUNT (DISTINCT OA.CODIGO)                    AS CANTIDAD_ORD_AMB_GEN, ")
			.append("COUNT (DISTINCT CT.CODIGO)                    AS CANTIDAD_CITAS_ATENDIDAS, ")
			.append("COUNT (SERV.CODIGO)                           AS CANTIDAD_SERV_MED, ")
			//.append("CASE WHEN SERV.CODIGO IS NOT NULL ")
				//.append("THEN SUM(CONSULTAEXTERNA.CALCULO_TARIFA_SERVICIO(SERV.CODIGO,:codigoEsquemaTarifarioServicios)) ")
				//.append("ELSE 0 ")
			//.append("END AS VALOR_TOTAL_TARIFA, ")
			.append("SUM(CONSULTAEXTERNA.CALCULO_TARIFA_SERVICIO(SERV.CODIGO,:codigoEsquemaTarifarioServicios)) AS VALOR_TOTAL_TARIFA, ")
			.append("PVE.VALOR_EST_ORD_CITA, ")
			.append("PVE.VALOR_EST_SERMED_ORDEN, ")
			.append("PVE.VALOR_EST_SERMED_CITA, ")
			.append("1                                    AS ES_SERVICIO, ")
			.append("US.LOGIN AS NOMBRE_USUARIO ")
		//.append("FROM ADMINISTRACION.PERSONAS PER ")
		.append("FROM AGENDA AG ")
		.append("INNER JOIN CONSULTAEXTERNA.CITA CT ON CT.CODIGO_AGENDA = AG.CODIGO ")
		/*PARA PODER SABER QUE SERVICIOS FUERON ORDENADOS LA RELACION DE LA ORDEN CON LA CITA ES OBLIGATORIA*/
		.append("INNER JOIN ORDENES.ORDENES_AMBULATORIAS OA ON OA.CITA_ASOCIADA=CT.CODIGO ")
		.append("INNER JOIN CONSULTAEXTERNA.SERVICIOS_CITA SC ")
		.append("ON SC.CODIGO_CITA = CT.CODIGO ")
		/*.append("INNER JOIN MEDICOS MED ON MED.codigo_medico = AG.CODIGO_MEDICO ")
		.append("INNER JOIN PERSONAS PER ON PER.CODIGO = MED.CODIGO_MEDICO ")
		.append("INNER JOIN USUARIOS US ON US.CODIGO_PERSONA = PER.CODIGO ")*/
		.append("INNER JOIN USUARIOS US ON US.LOGIN = CT.USUARIO_MODIFICA_ESTADO ")
        .append("INNER JOIN PERSONAS PER ON PER.CODIGO = US.CODIGO_PERSONA ")
        .append("INNER JOIN MEDICOS MED ON MED.codigo_medico = PER.CODIGO ")
		
		.append("INNER JOIN ADMINISTRACION.MEDICOS_INSTITUCIONES MED_INS ")
		.append("ON MED_INS.CODIGO_MEDICO = MED.CODIGO_MEDICO ")
		.append("INNER JOIN CONSULTAEXTERNA.UNIDADES_CONSULTA UC ")
		.append("ON UC.CODIGO = CT.UNIDAD_CONSULTA ")
		.append("INNER JOIN ORDENES.DET_ORDEN_AMB_SERVICIO DOAS ")
		.append("ON DOAS.CODIGO_ORDEN = OA.CODIGO ")
		.append("INNER JOIN FACTURACION.SERVICIOS SERV ")
		.append("ON SERV.CODIGO=DOAS.SERVICIO ")
		.append("INNER JOIN FACTURACION.GRUPOS_SERVICIOS GS ")
		.append("ON GS.CODIGO = SERV.GRUPO_SERVICIO ")
		.append("LEFT JOIN CONSULTAEXTERNA.PARAMETRICA_VALOR_ESTANDAR PVE ")
		.append("ON (PVE.GRUPOS_SERVICIO=GS.CODIGO AND PVE.UNIDAD_AGENDA= UC.CODIGO) ");
		
		/*FILTROS*/
		parametros.put("codigoEsquemaTarifarioServicios", esquemaTarifarioServicios);
		if(grupoOrdenadoresConsultaExternaDto.getFiltroIdCentroAtencion()!=null&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdCentroAtencion().trim().isEmpty()
				&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdCentroAtencion().trim().equals(ConstantesBD.codigoNuncaValido+"")){
			consultaOrdenadoresConsultaExt.append("INNER JOIN ADMINISTRACION.CENTRO_ATENCION CA ")
				.append("ON CA.CONSECUTIVO = AG.CENTRO_ATENCION ");
		}
		if(grupoOrdenadoresConsultaExternaDto.getFiltroIdConvenio()!=null&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdConvenio().trim().isEmpty()
				&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdConvenio().trim().equals(ConstantesBD.codigoNuncaValido+"")){
			consultaOrdenadoresConsultaExt.append("INNER JOIN SOLICITUDES SOL ON SOL.NUMERO_SOLICITUD = SC.NUMERO_SOLICITUD ")
				.append("INNER JOIN SOLICITUDES_SUBCUENTA SOL_SUBCUENTA ON SOL_SUBCUENTA.SOLICITUD=SOL.NUMERO_SOLICITUD ")
				.append("INNER JOIN SUB_CUENTAS SUB_CUENTA ON SUB_CUENTA.SUB_CUENTA=SOL_SUBCUENTA.SUB_CUENTA ")
				.append("INNER JOIN FACTURACION.CONVENIOS CONV ON CONV.CODIGO = SUB_CUENTA.CONVENIO ");
		}
		consultaOrdenadoresConsultaExt.append(where);
		if(grupoOrdenadoresConsultaExternaDto.getFiltroIdGrupoServicio()!=null&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdGrupoServicio().trim().isEmpty()
				&&!grupoOrdenadoresConsultaExternaDto.getFiltroIdGrupoServicio().trim().equals(ConstantesBD.codigoNuncaValido+"")){
			consultaOrdenadoresConsultaExt.append("AND GS.CODIGO = :codigoGrupoServicio ");
			parametros.put("codigoGrupoServicio", Integer.parseInt(grupoOrdenadoresConsultaExternaDto.getFiltroIdGrupoServicio().trim()));
		}
		/*FIN FILTROS*/
		
		consultaOrdenadoresConsultaExt.append("GROUP BY UC.CODIGO, ")
			.append("UC.DESCRIPCION, ")
			.append("GS.CODIGO, ")
			.append("GS.DESCRIPCION, ")
			.append("PER.TIPO_IDENTIFICACION, ")
			.append("PER.NUMERO_IDENTIFICACION, ")
			.append("PER.PRIMER_APELLIDO, ")
			.append("PER.SEGUNDO_APELLIDO, ")
			.append("PER.PRIMER_NOMBRE, ")
			.append("PER.SEGUNDO_NOMBRE, ")
			.append("PVE.VALOR_EST_ORD_CITA, ")
			.append("PVE.VALOR_EST_SERMED_ORDEN, ")
			.append("PVE.VALOR_EST_SERMED_CITA, ")
			.append("US.LOGIN ");
		return consultaOrdenadoresConsultaExt;
	}
	
	/**
	 * Consulta para el reporte de ordenadores de consulta externa para archivo plano
	 * 
	 * @param grupoOrdenadoresConsultaExternaDto
	 * @param esquemaTarifarioArticulos
	 * @param esquemaTarifarioServicios
	 * @return
	 * @throws BDException
	 * @author jeilones
	 * @created 20/11/2012
	 */
	@SuppressWarnings("unchecked")
	public List<OrdenadoresConsultaExternaPlanoDto> consultarIdentificadorOrdenadoresConsultaGrupoClasePlano(GrupoOrdenadoresConsultaExternaDto grupoOrdenadoresConsultaExternaDto,int esquemaTarifarioArticulos,int esquemaTarifarioServicios)throws BDException{
		List<OrdenadoresConsultaExternaPlanoDto> ordenadoresConsultaExterna=new ArrayList<OrdenadoresConsultaExternaPlanoDto>(0);
		
		try{
			persistenciaSvc=new PersistenciaSvc();
			
			Map<String, Object>parametros=new LinkedHashMap<String, Object>(0);
			StringBuffer consultaOrdenadoresConsultaExt=new StringBuffer("");
			consultaOrdenadoresConsultaExt.append("SELECT RS.CODIGO_UNIDAD_CONSULTA, ")
				.append("RS.DESCRIPCION_UNIDAD_CONSULTA, ")
				.append("RS.CODIGO_GS_CI, ")
				.append("RS.DESCRIPCION_GS_CI, ")
				.append("RS.TIPO_IDENTIFICACION, ")
				.append("RS.NUMERO_IDENTIFICACION, ")
				.append("RS.PRIMER_APELLIDO, ")
				.append("RS.SEGUNDO_APELLIDO, ")
				.append("RS.PRIMER_NOMBRE, ")
				.append("RS.SEGUNDO_NOMBRE, ")
				.append("RS.CANTIDAD_ORD_AMB_GEN, ")
				.append("RS.CANTIDAD_CITAS_ATENDIDAS, ")
				.append("RS.ES_SERVICIO, ")
				.append("RS.CANTIDAD_ORD_AMB_GEN AS PROM_CANT_ORD_X_CITA,  ")
				.append("CASE WHEN RS.CANTIDAD_ORD_AMB_GEN>0 ")
					.append("THEN RS.CANTIDAD_SERV_MED/RS.CANTIDAD_ORD_AMB_GEN ")
					.append("ELSE 0 ")
				.append("END AS PROM_SERV_MED_X_ORDEN, ")
				.append("RS.CANTIDAD_SERV_MED AS CANTIDAD_SERV_MED, ")
				.append("RS.VALOR_TOTAL_TARIFA AS VALOR_TOTAL_TARIFA, ")
				.append("RS.VALOR_EST_ORD_CITA, ")
				.append("RS.VALOR_EST_SERMED_ORDEN, ")
				.append("RS.VALOR_EST_SERMED_CITA, ")
				.append("RS.NOMBRE_USUARIO ")
			.append("FROM  ")
				.append("( ");
			
			consultaOrdenadoresConsultaExt.append(crearConsultaOrdenadoresPorGrupoClase(grupoOrdenadoresConsultaExternaDto,esquemaTarifarioArticulos,esquemaTarifarioServicios,parametros));
				
			consultaOrdenadoresConsultaExt.append(") RS ")
			.append("ORDER BY ")
				.append("RS.ES_SERVICIO, ")
				.append("RS.DESCRIPCION_GS_CI, ")
				.append("RS.DESCRIPCION_UNIDAD_CONSULTA, ")
				.append("RS.PRIMER_NOMBRE, ")
				.append("RS.SEGUNDO_NOMBRE, ")
				.append("RS.PRIMER_APELLIDO, ")
				.append("RS.SEGUNDO_APELLIDO ");
			
			SQLQuery sqlQuery=persistenciaSvc.getSession().createSQLQuery(consultaOrdenadoresConsultaExt.toString());
			sqlQuery.addScalar("CODIGO_UNIDAD_CONSULTA", new IntegerType());
			sqlQuery.addScalar("DESCRIPCION_UNIDAD_CONSULTA", new StringType());
			sqlQuery.addScalar("CODIGO_GS_CI", new IntegerType());
			
			sqlQuery.addScalar("DESCRIPCION_GS_CI", new StringType());
			sqlQuery.addScalar("TIPO_IDENTIFICACION", new StringType());
			sqlQuery.addScalar("NUMERO_IDENTIFICACION", new StringType());
			sqlQuery.addScalar("PRIMER_APELLIDO", new StringType());
			sqlQuery.addScalar("SEGUNDO_APELLIDO", new StringType());
			sqlQuery.addScalar("PRIMER_NOMBRE", new StringType());
			sqlQuery.addScalar("SEGUNDO_NOMBRE", new StringType());
			sqlQuery.addScalar("CANTIDAD_ORD_AMB_GEN", new IntegerType());
			sqlQuery.addScalar("CANTIDAD_CITAS_ATENDIDAS", new IntegerType());
			sqlQuery.addScalar("ES_SERVICIO", new IntegerType());
			sqlQuery.addScalar("PROM_CANT_ORD_X_CITA", new DoubleType());
			sqlQuery.addScalar("PROM_SERV_MED_X_ORDEN", new DoubleType());
			sqlQuery.addScalar("CANTIDAD_SERV_MED", new DoubleType());
			sqlQuery.addScalar("VALOR_TOTAL_TARIFA", new DoubleType());
			sqlQuery.addScalar("VALOR_EST_ORD_CITA", new DoubleType());
			sqlQuery.addScalar("VALOR_EST_SERMED_ORDEN", new DoubleType());
			sqlQuery.addScalar("VALOR_EST_SERMED_CITA", new DoubleType());
			sqlQuery.addScalar("NOMBRE_USUARIO", new StringType());
			
			Set<String>keyParemetros=parametros.keySet();
			for(String key:keyParemetros){
				sqlQuery.setParameter(key, parametros.get(key));
			}
			List<Object[]>resultado=sqlQuery.list();
			
			final int CODIGO_UNIDAD_CONSULTA=0;
			final int DESCRIPCION_UNIDAD_CONSULTA=1;
			final int CODIGO_GS_CI=2;
			final int DESCRIPCION_GS_CI=3;
			final int TIPO_IDENTIFICACION=4;
			final int NUMERO_IDENTIFICACION=5;
			final int PRIMER_APELLIDO=6;
			final int SEGUNDO_APELLIDO=7;
			final int PRIMER_NOMBRE=8;
			final int SEGUNDO_NOMBRE=9;
			final int CANTIDAD_ORD_AMB_GEN=10;
			//final int CANTIDAD_CITAS_ATENDIDAS=11;
			final int ES_SERVICIO=12;
			//final int PROM_CANT_ORD_X_CITA=13;
			final int PROM_SERV_MED_X_ORDEN=14;
			final int CANTIDAD_SERV_MED=15;
			final int VALOR_TOTAL_TARIFA=16;
			final int VALOR_EST_ORD_CITA=17;
			final int VALOR_EST_SERMED_ORDEN=18;
			final int VALOR_EST_SERMED_CITA=19;
			final int NOMBRE_USUARIO=20;
			
			List<GrupoServicioDto>gruposServicio=new ArrayList<GrupoServicioDto>(0);
			List<ClaseInventarioDto>clasesInventario=new ArrayList<ClaseInventarioDto>(0);
			
			Map<Integer, Integer>mapaCantidadOrdenesGruposServicio=new LinkedHashMap<Integer, Integer>(0);
			Map<Integer, Integer>mapaCantidadOrdenesClasesInventario=new LinkedHashMap<Integer, Integer>(0);
			
			Map<String, Integer>mapaCantidadUnidadConsultaGruposServicio=new LinkedHashMap<String, Integer>(0);
			Map<String, Integer>mapaCantidadUnidadConsultaClasesInventario=new LinkedHashMap<String, Integer>(0);
			
			for(Object[]fila:resultado){
				OrdenadoresConsultaExternaPlanoDto ordenadoresConsultaExtDto2=new OrdenadoresConsultaExternaPlanoDto();
				
				ordenadoresConsultaExtDto2.setTipoIdentificacion((String)fila[TIPO_IDENTIFICACION]);
				ordenadoresConsultaExtDto2.setNumeroIdentificacion((String)fila[NUMERO_IDENTIFICACION]);
				
				ordenadoresConsultaExtDto2.setPrimerApellido((String) fila[PRIMER_APELLIDO]);
				ordenadoresConsultaExtDto2.setSegundoApellido((String) fila[SEGUNDO_APELLIDO]);
				ordenadoresConsultaExtDto2.setPrimerNombre((String) fila[PRIMER_NOMBRE]);
				ordenadoresConsultaExtDto2.setSegundoNombre((String) fila[SEGUNDO_NOMBRE]);
				
				ordenadoresConsultaExtDto2.setCodigoUnidadConsulta((Integer) fila[CODIGO_UNIDAD_CONSULTA]);
				ordenadoresConsultaExtDto2.setDescripcionUnidadConsulta((String) fila[DESCRIPCION_UNIDAD_CONSULTA]);
				
				if(((Integer)fila[ES_SERVICIO]).intValue()==1){
					ordenadoresConsultaExtDto2.setCodigoGrupoServicio((Integer) fila[CODIGO_GS_CI]);
					ordenadoresConsultaExtDto2.setGrupoServicio((String) fila[DESCRIPCION_GS_CI]);
					ordenadoresConsultaExtDto2.setEsServicio(true);
					
					Integer cantidadOrdAmbGrupoServicio=mapaCantidadOrdenesGruposServicio.get((Integer) fila[CODIGO_GS_CI]);
					if(cantidadOrdAmbGrupoServicio==null){
						cantidadOrdAmbGrupoServicio=0;
					}
					
					cantidadOrdAmbGrupoServicio+=(Integer) fila[CANTIDAD_ORD_AMB_GEN];
					mapaCantidadOrdenesGruposServicio.put((Integer) fila[CODIGO_GS_CI], cantidadOrdAmbGrupoServicio);
					
					Integer cantidaOrdAmbUnidadConsulta=mapaCantidadUnidadConsultaGruposServicio.get(fila[CODIGO_GS_CI].toString()+"-"+fila[CODIGO_UNIDAD_CONSULTA].toString());
					if(cantidaOrdAmbUnidadConsulta==null){
						cantidaOrdAmbUnidadConsulta=0;
					}
					
					cantidaOrdAmbUnidadConsulta+=(Integer) fila[CANTIDAD_ORD_AMB_GEN];
					mapaCantidadUnidadConsultaGruposServicio.put(fila[CODIGO_GS_CI].toString()+"-"+fila[CODIGO_UNIDAD_CONSULTA].toString(), cantidaOrdAmbUnidadConsulta);
					
				}else{
					ordenadoresConsultaExtDto2.setCodigoClaseInventario((Integer) fila[CODIGO_GS_CI]);
					ordenadoresConsultaExtDto2.setClaseInventario((String) fila[DESCRIPCION_GS_CI]);
					ordenadoresConsultaExtDto2.setEsServicio(false);
					
					Integer cantidadOrdAmbClaseInventario=mapaCantidadOrdenesClasesInventario.get((Integer) fila[CODIGO_GS_CI]);
					if(cantidadOrdAmbClaseInventario==null){
						cantidadOrdAmbClaseInventario=0;
					}
					
					cantidadOrdAmbClaseInventario+=(Integer) fila[CANTIDAD_ORD_AMB_GEN];
					mapaCantidadOrdenesClasesInventario.put((Integer) fila[CODIGO_GS_CI], cantidadOrdAmbClaseInventario);
					
					Integer cantidaOrdAmbUnidadConsulta=mapaCantidadUnidadConsultaClasesInventario.get(fila[CODIGO_GS_CI].toString()+"-"+fila[CODIGO_UNIDAD_CONSULTA].toString());
					if(cantidaOrdAmbUnidadConsulta==null){
						cantidaOrdAmbUnidadConsulta=0;
					}
					
					cantidaOrdAmbUnidadConsulta+=(Integer) fila[CANTIDAD_ORD_AMB_GEN];
					mapaCantidadUnidadConsultaClasesInventario.put(fila[CODIGO_GS_CI].toString()+"-"+fila[CODIGO_UNIDAD_CONSULTA].toString(), cantidaOrdAmbUnidadConsulta);
				}
				
				String nombreUsuario=(String) fila[NOMBRE_USUARIO];
				int cantidadCitasPorGrupoServClaseInv=consultarCantidadCitasAtendidas(grupoOrdenadoresConsultaExternaDto.getFechaInicialAtencionCita(), grupoOrdenadoresConsultaExternaDto.getFechaFinalAtencionCita(), nombreUsuario, ordenadoresConsultaExtDto2.getCodigoUnidadConsulta(),ordenadoresConsultaExtDto2.isEsServicio(), ordenadoresConsultaExtDto2.isEsServicio()?ordenadoresConsultaExtDto2.getCodigoGrupoServicio():ordenadoresConsultaExtDto2.getCodigoClaseInventario());
				ordenadoresConsultaExtDto2.setCantidadCitasAtendidas(cantidadCitasPorGrupoServClaseInv);
				
				
				
				ordenadoresConsultaExtDto2.setCantidadOrdenesAmbGeneradas((Integer) fila[CANTIDAD_ORD_AMB_GEN]);
				ordenadoresConsultaExtDto2.setPromedioServMedOrdenAmb((Double)fila[PROM_SERV_MED_X_ORDEN]);
				
				if(cantidadCitasPorGrupoServClaseInv>0){
					ordenadoresConsultaExtDto2.setPromedioOrdenesAmbXCitas(((Integer) fila[CANTIDAD_ORD_AMB_GEN]).doubleValue()/(double)cantidadCitasPorGrupoServClaseInv);
					ordenadoresConsultaExtDto2.setPromedioServMedCita(((Double)fila[CANTIDAD_SERV_MED]).doubleValue()/(double)cantidadCitasPorGrupoServClaseInv);
					if(fila[VALOR_TOTAL_TARIFA]!=null){
						ordenadoresConsultaExtDto2.setCostoPromedioCita(((Double)fila[VALOR_TOTAL_TARIFA]).doubleValue()/(double)cantidadCitasPorGrupoServClaseInv);
					}
				}
				
				ordenadoresConsultaExtDto2.setValorEstCita((Double) fila[VALOR_EST_ORD_CITA]);
				ordenadoresConsultaExtDto2.setValorEstServMedOrdenAmb((Double) fila[VALOR_EST_SERMED_ORDEN]);
				ordenadoresConsultaExtDto2.setValorEstServMedCita((Double) fila[VALOR_EST_SERMED_CITA]);
				
				
				ordenadoresConsultaExterna.add(ordenadoresConsultaExtDto2);				
			}
			
			for(OrdenadoresConsultaExternaPlanoDto consultaExternaPlanoDto:ordenadoresConsultaExterna){
				if(consultaExternaPlanoDto.isEsServicio()){
					consultaExternaPlanoDto.setCantidadOrdenesAmbGeneradasXGrupoClase(mapaCantidadOrdenesGruposServicio.get(consultaExternaPlanoDto.getCodigoGrupoServicio()));
					consultaExternaPlanoDto.setCantidadOrdenesAmbGeneradasXUnidadConsulta(mapaCantidadUnidadConsultaGruposServicio.get(consultaExternaPlanoDto.getCodigoGrupoServicio()+"-"+consultaExternaPlanoDto.getCodigoUnidadConsulta()));
				}else{
					consultaExternaPlanoDto.setCantidadOrdenesAmbGeneradasXGrupoClase(mapaCantidadOrdenesClasesInventario.get(consultaExternaPlanoDto.getCodigoClaseInventario()));
					consultaExternaPlanoDto.setCantidadOrdenesAmbGeneradasXUnidadConsulta(mapaCantidadUnidadConsultaClasesInventario.get(consultaExternaPlanoDto.getCodigoClaseInventario()+"-"+consultaExternaPlanoDto.getCodigoUnidadConsulta()));
				}
			}
			
			grupoOrdenadoresConsultaExternaDto.setGruposServicio(gruposServicio);
			grupoOrdenadoresConsultaExternaDto.setClasesInventario(clasesInventario);
			
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		return ordenadoresConsultaExterna;
	}
	
	private int consultarCantidadCitasAtendidas(Date fechaInicial,Date fechaFinal, String nombreUsuario, Integer codigoUnidadConsulta,Boolean esServicio,Integer codigoGruposServClaseInv) throws BDException{
		int cantidadCitasAtendidas=0;
		try{
			persistenciaSvc=new PersistenciaSvc();
			Map<String, Object>parametros=new LinkedHashMap<String, Object>(0);
			
			StringBuffer consultaCantidadCitasAtendidas=new StringBuffer("SELECT count (DISTINCT CT.CODIGO) cantidad_citas ")
				.append("FROM AGENDA AG ")
				.append("INNER JOIN CONSULTAEXTERNA.CITA CT ")
				.append("ON CT.CODIGO_AGENDA = AG.CODIGO ")
				.append("INNER JOIN CONSULTAEXTERNA.SERVICIOS_CITA SC ")
				.append("ON SC.CODIGO_CITA = CT.CODIGO ");
			
			if(codigoUnidadConsulta!=null&&codigoUnidadConsulta.intValue()>0){
				consultaCantidadCitasAtendidas.append("INNER JOIN CONSULTAEXTERNA.UNIDADES_CONSULTA UC ") 
					.append("ON UC.CODIGO = CT.UNIDAD_CONSULTA ");
			}
			
			StringBuffer where=new StringBuffer(" WHERE SC.FECHA_CITA BETWEEN :fechaInicial  AND :fechaFinal ")
				.append("AND CT.ESTADO_CITA = :codigoEstadoCita ")
				.append("AND CT.USUARIO_MODIFICA_ESTADO = :nombreUsuario ");
			
			if(codigoUnidadConsulta!=null&&codigoUnidadConsulta.intValue()>0){
				where.append("AND UC.CODIGO = :codigoUnidadConsulta ");
				parametros.put("codigoUnidadConsulta", codigoUnidadConsulta);
			}
			
			if(esServicio!=null&&codigoGruposServClaseInv!=null){
				consultaCantidadCitasAtendidas.append("INNER JOIN ORDENES.ORDENES_AMBULATORIAS OA ON OA.CITA_ASOCIADA=CT.CODIGO ");
				if(esServicio){
					consultaCantidadCitasAtendidas.append("INNER JOIN ORDENES.DET_ORDEN_AMB_SERVICIO DOAS ON DOAS.CODIGO_ORDEN = OA.CODIGO ")
						.append("INNER JOIN FACTURACION.SERVICIOS SERV ON SERV.CODIGO=DOAS.SERVICIO ")
						.append("INNER JOIN FACTURACION.GRUPOS_SERVICIOS GS ON GS.CODIGO = SERV.GRUPO_SERVICIO");
					where.append("AND GS.CODIGO = :codigoGrupoServClaseInv ");
				}else{
					consultaCantidadCitasAtendidas.append("INNER JOIN ORDENES.DET_ORDEN_AMB_ARTICULO DOAA ON DOAA.CODIGO_ORDEN = OA.CODIGO ") 
						.append("INNER JOIN INVENTARIOS.ARTICULO ART ON ART.CODIGO=DOAA.ARTICULO ") 
						.append("INNER JOIN INVENTARIOS.SUBGRUPO_INVENTARIO SUBGI ON SUBGI.CODIGO=ART.SUBGRUPO ") 
						.append("INNER JOIN INVENTARIOS.GRUPO_INVENTARIO GI ON (")
	                        .append("GI.CODIGO=SUBGI.GRUPO AND GI.CLASE =SUBGI.CLASE )")
                        .append("INNER JOIN INVENTARIOS.CLASE_INVENTARIO CI ON CI.CODIGO=GI.CLASE ");
					where.append("AND CI.CODIGO = :codigoGrupoServClaseInv ");
				}
				
				parametros.put("codigoGrupoServClaseInv", codigoGruposServClaseInv);
			}
			
			parametros.put("fechaInicial", fechaInicial);
			parametros.put("fechaFinal", fechaFinal);
			
			parametros.put("codigoEstadoCita", ConstantesBD.codigoEstadoCitaAtendida);
			parametros.put("nombreUsuario", nombreUsuario);
			
			consultaCantidadCitasAtendidas.append(where);
			
			SQLQuery sqlQuery=persistenciaSvc.getSession().createSQLQuery(consultaCantidadCitasAtendidas.toString());
			sqlQuery.addScalar("cantidad_citas", new IntegerType());
			
			Set<String>keyParemetros=parametros.keySet();
			for(String key:keyParemetros){
				sqlQuery.setParameter(key, parametros.get(key));
			}
			cantidadCitasAtendidas=(Integer)sqlQuery.uniqueResult();
		}catch (Exception e) {
			Log4JManager.error(e);
			throw new BDException(CODIGO_ERROR_NEGOCIO.ERROR_BASE_DATOS, e);
		}
		return cantidadCitasAtendidas;		
	}
}
