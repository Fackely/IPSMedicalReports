/*
 * @author artotor
 */
package com.princetonsa.dao.oracle.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import util.ConstantesBD;

import com.princetonsa.dao.manejoPaciente.PacientesConEgresoPorFacturarDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBasePacientesConEgresoPorFacturarDao;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.dto.manejoPaciente.DtoFiltroBusquedaAvanzadaEgresoPorFacturar;

/**
 * 
 * @author artotor
 *
 */
public class OraclePacientesConEgresoPorFacturarDao implements PacientesConEgresoPorFacturarDao 
{

	private static String consultaPacientesConEgresoPorFacturar="SELECT DISTINCT" +//Se adiciona distinct por inner de solicitudes
	" case when c.via_ingreso = "+ConstantesBD.codigoViaIngresoHospitalizacion+" then ah.consecutivo else au.consecutivo end as admision," +
	" case when c.via_ingreso = "+ConstantesBD.codigoViaIngresoHospitalizacion+" then to_char(ah.fecha_admision,'"+ConstantesBD.formatoFechaAp+"')||' '||ah.hora_admision else to_char(au.fecha_admision,'dd/mm/yyyy')||' '||au.hora_admision end as fechahoraadmision," +
	" getNombreViaIngresoTipoPac(c.id) as viaingreso," +
	" e.fecha_egreso AS fecha_egreso, " +
    " e.hora_egreso AS hora_egreso, " +
	" to_char(e.fecha_egreso,'"+ConstantesBD.formatoFechaAp+"')||' '||substr(e.hora_egreso||'',0,5) as fechahoraengreso," +
	" getnomcentrocosto(c.area) as area," +
	" getnombrepersona(c.codigo_paciente) as nombrepaciente," +
	" p.tipo_identificacion as tipoid," +
	" p.numero_identificacion as numeroid," +
	" case when e.codigo_medico is null then '' else administracion.getnombremedico(e.codigo_medico) end as nombremedico, " +
	" c.codigo_paciente as codigopaciente," +
	" getDescripEntidadSubXingreso(i.id)  AS descentidadsub," +
	" i.id as codigoingreso, " +
	" c.id as cuenta" +
	" from cuentas c  " +
	" inner join personas p on (p.codigo=c.codigo_paciente) " +
	" inner join ingresos i on(c.id_ingreso=i.id) " +
	" inner join egresos e on (e.cuenta=c.id and e.fecha_egreso is not null and e.hora_egreso is not null and (e.fecha_reversion_egreso is null or e.fecha_egreso>e.fecha_reversion_egreso or (e.fecha_egreso=e.fecha_reversion_egreso and e.hora_egreso>e.hora_reversion_egreso)) " +
	//arrglo tarea 269527, solicitaron que no validara el medico
	//" and e.codigo_medico is not null " +
	") " +
	" inner join centros_costo cc on (c.area=cc.codigo) " +
	" left outer join admisiones_urgencias au on (au.cuenta=c.id) " +
	" left outer join admisiones_hospi ah on (ah.cuenta=c.id) " +
	//MT 1430: Solo se listaran los pacientes cuyas ordenes de medicamentos se encuentren en estado Administrada y Anulada 
	" inner join solicitudes sol ON (sol.cuenta=c.id and ((sol.tipo="+ConstantesBD.codigoTipoSolicitudMedicamentos+" and sol.estado_historia_clinica in ("+ConstantesBD.codigoEstadoHCAnulada+","+ConstantesBD.codigoEstadoHCAdministrada+")) or sol.tipo!="+ConstantesBD.codigoTipoSolicitudMedicamentos+" ) )";

	/**
	 * 
	 */
	public HashMap cargarPacientesConEgresoPorFacturar(Connection con, UsuarioBasico usuario)
	{
		return SqlBasePacientesConEgresoPorFacturarDao.cargarPacientesConEgresoPorFacturar(con,usuario, consultaPacientesConEgresoPorFacturar);
	}
	
	@Override
	public HashMap cargarPacientesConEgresoPorFacturarAvanzado(Connection con,
			UsuarioBasico usuario,
			DtoFiltroBusquedaAvanzadaEgresoPorFacturar dtoFiltro) {
		return SqlBasePacientesConEgresoPorFacturarDao.cargarPacientesConEgresoPorFacturarAvanzado(con,usuario, consultaPacientesConEgresoPorFacturar,dtoFiltro);

	}
}
