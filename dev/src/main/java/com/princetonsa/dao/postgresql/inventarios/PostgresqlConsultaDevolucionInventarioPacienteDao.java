package com.princetonsa.dao.postgresql.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.inventarios.ConsultaDevolucionInventarioPacienteDao;
import com.princetonsa.dao.sqlbase.inventarios.SqlBaseConsultaDevolucionInventarioPacienteDao;

public class PostgresqlConsultaDevolucionInventarioPacienteDao implements ConsultaDevolucionInventarioPacienteDao
{
	public HashMap<String, Object> consultaCentroCosto (Connection con)
	{
		return SqlBaseConsultaDevolucionInventarioPacienteDao.consultaCentroCosto(con);
	}
	
	public HashMap<String, Object> consultaEstadosDevolucion (Connection con)
	{
		return SqlBaseConsultaDevolucionInventarioPacienteDao.consultaEstadosDevolucion(con);
	}
	
	public HashMap<String, Object> consultaMotivosDevolucion (Connection con)
	{
		return SqlBaseConsultaDevolucionInventarioPacienteDao.consultaMotivosDevolucion(con);
	}
	
	public HashMap<String, Object> consultaListadoIngresos (Connection con, int codigoPaciente)
	{
		return SqlBaseConsultaDevolucionInventarioPacienteDao.consultaListadoIngresos(con, codigoPaciente);
	}
	
	public HashMap<String, Object> consultaDetalleIC (Connection con, String codigoCuenta)
	{
		return SqlBaseConsultaDevolucionInventarioPacienteDao.consultaDetalleIC(con, codigoCuenta);
	}
	
	public HashMap<String, Object> consultaDetalleAIC (Connection con, String codigoDevolucion, String tipoDevolucion)
	{
		return SqlBaseConsultaDevolucionInventarioPacienteDao.consultaDetalleAIC(con, codigoDevolucion, tipoDevolucion);
	}
	
	public HashMap<String, Object> consultaDevR (Connection con, String numeroDevolucion, String centroAtencion, String almacen, String centroCosto, String fechaIni, String fechaFin, String estadoDevolucion, String tipoDevolucion, String motivoDevolucion, String usuarioDevuelve, String usuarioRecibe)
	{
		String sql="";
		if(!numeroDevolucion.equals("")){
				sql = "SELECT DISTINCT " +
							"dvm.codigo AS numdev, " +
							"dvm.fecha AS fechadev, " +
							"edv.descripcion AS estadodev, " +
							"cc.nombre AS centrocdev, " +
							"'Solicitud' AS campod, " +
							"c.codigo_paciente AS cpdv, " +
							"getnombrealmacen(dvm.farmacia) AS nom_farmacia, " +
							"sum((drm.cantidadrecibida * drm.costo_unitario)) AS valor " +
						"FROM " +
							"devolucion_med dvm " +
						"INNER JOIN " +
							"detalle_devol_med ddm ON (dvm.codigo = ddm.devolucion) " +
							//MT 6772
						"LEFT JOIN " +
							"recepciones_medicamentos rm ON (rm.devolucion = dvm.codigo) " +	
						"LEFT JOIN " +
							"detalle_recep_medicamentos drm ON (drm.codigo = ddm.codigo AND drm.articulo=ddm.articulo) " +
						"INNER JOIN " +
							"centros_costo cc ON (dvm.centro_costo_devuelve = cc.codigo) " +
						"INNER JOIN " +
							"estados_devolucion edv ON (dvm.estado = edv.codigo) " +
						"INNER JOIN " +
							"solicitudes s ON (ddm.numero_solicitud = s.numero_solicitud) " +
						"INNER JOIN " +
							"cuentas c ON (s.cuenta = c.id) " +
						"WHERE " +
							"dvm.codigo = " + numeroDevolucion + " GROUP BY dvm.codigo,dvm.fecha,edv.descripcion,cc.nombre,c.codigo_paciente,dvm.farmacia " + 
						"UNION ALL " + 
						"SELECT DISTINCT " +
							"dvp.codigo AS numdev, " +
							"dvp.fecha AS fechadev, " +
							"edv.descripcion AS estadodev, " +
							"cc.nombre AS centrocdev, " +
							"'Pedido' AS campod, " +
							"c.codigo_paciente AS cpdv, " + 
							"getnombrealmacen(p.centro_costo_solicitado) AS nom_farmacia, " + 
							"(sum(drp.cantidadrecibida * drp.costo_unitario)) AS valor " + 
						"FROM " +
							"devolucion_pedidos dvp " + 
						"INNER JOIN " +
							"detalle_devol_pedido ddp ON (ddp.devolucion = dvp.codigo) " +
						"INNER JOIN " +
							"pedido p ON (ddp.pedido = p.codigo) " +  
							//MT6772
						"LEFT JOIN " +
							"recepciones_pedidos rp ON (rp.devolucion = dvp.codigo) " + 
						"LEFT JOIN " +
							"detalle_recep_pedidos drp ON (drp.codigo = ddp.codigo AND drp.articulo=ddp.articulo) " + 
						"INNER JOIN " +
							"centros_costo cc ON (p.centro_costo_solicitante = cc.codigo) " + 
						"INNER JOIN " +
							"estados_devolucion edv ON (dvp.estado = edv.codigo) " +
						"INNER JOIN " +
							"pedidos_peticiones_qx ppqx ON (ppqx.pedido = p.codigo) " +
						"INNER JOIN " +
							"peticion_qx pqx ON (ppqx.peticion = pqx.codigo) " +
						"INNER JOIN " +
							"solicitudes_cirugia sc ON (sc.codigo_peticion = pqx.codigo) " +
						"INNER JOIN " +
							"solicitudes s ON (sc.numero_solicitud = s.numero_solicitud) " +
						"INNER JOIN " +
							"cuentas c ON (s.cuenta = c.id) " +
						"WHERE " +
							"dvp.codigo = " + numeroDevolucion + " GROUP BY dvp.codigo,dvp.fecha,edv.descripcion,cc.nombre,c.codigo_paciente,p.centro_costo_solicitado ";
		} else {
		
			if(tipoDevolucion.equals("1")){
				sql = "SELECT DISTINCT " +
							"dvm.codigo AS numdev, " +
							"dvm.fecha AS fechadev, " +
							"edv.descripcion AS estadodev, " +
							"cc.nombre AS centrocdev, " +
							"'Solicitud' AS campod, " +
							"c.codigo_paciente AS cpdv, " +
							"getnombrealmacen(dvm.farmacia) AS nom_farmacia, " +
							"(sum(drm.cantidadrecibida * drm.costo_unitario)) AS valor " +
						"FROM " +
							"devolucion_med dvm " +
						"INNER JOIN " +
							"detalle_devol_med ddm ON (dvm.codigo = ddm.devolucion) " +
						"INNER JOIN " +
							"mot_devolucion_inventario mdi ON (dvm.motivo = mdi.codigo) " +
						//MT6772
						"LEFT JOIN " +
							"recepciones_medicamentos rm ON (rm.devolucion = dvm.codigo) " +	
						"LEFT JOIN " +
							"detalle_recep_medicamentos drm ON (drm.codigo = ddm.codigo AND drm.articulo=ddm.articulo) " +
						"INNER JOIN " +
							"centros_costo cc ON (dvm.centro_costo_devuelve = cc.codigo) " +
						"INNER JOIN " +
							"estados_devolucion edv ON (dvm.estado = edv.codigo) " +
						"INNER JOIN " +
							"usuarios u ON (dvm.usuario = u.login) " +
						"INNER JOIN " +
							"personas per ON (per.codigo = u.codigo_persona) " +
						"INNER JOIN " +
							"solicitudes s ON (ddm.numero_solicitud = s.numero_solicitud) " +
						"INNER JOIN " +
							"cuentas c ON (s.cuenta = c.id) " +
						"WHERE " +
							"cc.centro_atencion = " + centroAtencion + " ";
				
				if(!almacen.equals("-1")){
					//MT6772
					sql += " AND dvm.farmacia = "+almacen;
				}
				
				
				if(!centroCosto.equals("-1")){
					//MT6772
					sql += " AND cc.codigo = "+centroCosto;
				}
					
				
				if(!fechaIni.equals("") && !fechaFin.equals("")){
					sql += " AND dvm.fecha BETWEEN '"+fechaIni+"' AND '"+fechaFin+"'";
				}
				
				
				if(!estadoDevolucion.equals("-1")){
					//MT 6772 
					sql += " AND edv.codigo = " +estadoDevolucion;
				}
				
				
				if(!motivoDevolucion.equals("-1")){
					sql += " AND dvm.motivo = '"+motivoDevolucion+"'";
				}
				
				
				if(!usuarioDevuelve.equals("")){
					//MT 6772 
					sql+= " AND u.login= '"+usuarioDevuelve+"'";	
					//sql += " AND UPPER(per.primer_nombre || ' ' || per.segundo_nombre || ' ' || per.primer_apellido || ' ' || per.segundo_apellido) LIKE UPPER('%"+usuarioDevuelve+"%') ";
				}
					
				
				if(!usuarioRecibe.equals("")){
					//MT 6772 
					sql+= " AND u.login= '"+usuarioRecibe+"'";	
					//sql += " AND UPPER(per.primer_nombre || ' ' || per.segundo_nombre || ' ' || per.primer_apellido || ' ' || per.segundo_apellido) LIKE UPPER('%"+usuarioRecibe+"%') ";
				}
					
				
				sql += " GROUP BY dvm.codigo,dvm.fecha,edv.descripcion,cc.nombre,cc.centro_atencion,c.codigo_paciente,dvm.farmacia,dvm.motivo,per.primer_nombre,per.segundo_nombre,per.primer_apellido,per.segundo_apellido ORDER BY dvm.codigo ";
			}
			
			if(tipoDevolucion.equals("2")){
				sql = "SELECT DISTINCT " +
							"dvp.codigo AS numdev, " +
							"dvp.fecha AS fechadev, " +
							"edv.descripcion AS estadodev, " +
							"cc.nombre AS centrocdev, " +
							"'Pedido' AS campod, " +
							"c.codigo_paciente AS cpdv, " + 
							"getnombrealmacen(p.centro_costo_solicitado) AS nom_farmacia, " + 
							"(sum(drp.cantidadrecibida * drp.costo_unitario)) AS valor " + 
						"FROM " +
							"devolucion_pedidos dvp " + 
						"INNER JOIN " +
							"detalle_devol_pedido ddp ON (ddp.devolucion = dvp.codigo) " +
						"INNER JOIN " +
							"mot_devolucion_inventario mdi ON (dvp.motivo = mdi.codigo) " +
						"INNER JOIN " +
							"pedido p ON (ddp.pedido = p.codigo) " +  
						//MT6772
						"LEFT JOIN " +
							"recepciones_pedidos rp ON (rp.devolucion = dvp.codigo) " + 
						"LEFT JOIN " +
							"detalle_recep_pedidos drp ON (drp.codigo = ddp.codigo AND drp.articulo=ddp.articulo) " + 
						"INNER JOIN " +
							"centros_costo cc ON (p.centro_costo_solicitante = cc.codigo) " + 
						"INNER JOIN " +
							"estados_devolucion edv ON (dvp.estado = edv.codigo) " +
						"INNER JOIN " +
							"usuarios u ON (dvp.usuario = u.login) " +
						"INNER JOIN " +
							"personas per ON (per.codigo = u.codigo_persona) " +
						"INNER JOIN " +
							"pedidos_peticiones_qx ppqx ON (ppqx.pedido = p.codigo) " +
						"INNER JOIN " +
							"peticion_qx pqx ON (ppqx.peticion = pqx.codigo) " +
						"INNER JOIN " +
							"solicitudes_cirugia sc ON (sc.codigo_peticion = pqx.codigo) " +
						"INNER JOIN " +
							"solicitudes s ON (sc.numero_solicitud = s.numero_solicitud) " +
						"INNER JOIN " +
							"cuentas c ON (s.cuenta = c.id) " +
						"WHERE	 cc.centro_atencion = " + centroAtencion + " ";
				//MT6772
				if(!almacen.equals("-1")){
					sql += " AND p.centro_costo_solicitado = "+almacen;
				}
				
				//MT6772
				if(!centroCosto.equals("-1")){
					sql += " AND cc.codigo = "+centroCosto;
				}
				
				
				if(!fechaIni.equals("") && !fechaFin.equals("")){
					sql += " AND dvp.fecha BETWEEN '"+fechaIni+"' AND '"+fechaFin+"'";
				}
				
				
				if(!estadoDevolucion.equals("-1")){
					//mt6772
					sql += " AND edv.codigo =  "+estadoDevolucion;
				}
				
				
				if(!motivoDevolucion.equals("-1")){
					sql += " AND dvp.motivo = '"+motivoDevolucion+"'";
				}
					
				
				if(!usuarioDevuelve.equals("")){
					//MT 6772 
					sql+= " AND u.login= '"+usuarioDevuelve+"'";
					//sql += " AND UPPER(per.primer_nombre || ' ' || per.segundo_nombre || ' ' || per.primer_apellido || ' ' || per.segundo_apellido) LIKE UPPER('%"+usuarioDevuelve+"%') ";
				
				}
					
				if(!usuarioRecibe.equals("")){
					//MT 6772 
					sql+= " AND u.login= '"+usuarioRecibe+"'";	
					//sql += " AND UPPER(per.primer_nombre || ' ' || per.segundo_nombre || ' ' || per.primer_apellido || ' ' || per.segundo_apellido) LIKE UPPER('%"+usuarioRecibe+"%') ";
				
				}
				
				sql += " GROUP BY dvp.codigo,dvp.fecha,edv.descripcion,cc.nombre,cc.centro_atencion,c.codigo_paciente,p.centro_costo_solicitado,dvp.motivo,per.primer_nombre,per.segundo_nombre,per.primer_apellido,per.segundo_apellido ORDER BY dvp.codigo ";
				
			}
			
			if(tipoDevolucion.equals("-1")){
				sql = "SELECT " +
							"f.numdev, " +
							"f.fechadev, " +
							"f.estadodev, " +
							"f.centrocdev, " +
							"f.campod, " +
							"f.nom_farmacia, " +
							"f.motdev, " +
							"f.valor, " +
							"f.pnom, " +
							"f.snom, " +
							"f.pape, " +
							"f.sape, " +
							"f.cadev, " +
							"f.cpdv, " +
							//mt 6772
							"f.codestadev, " +
							"f.usua, " +
							"f.usuare, " +
							"f.codigofarmacia, " +
							"f.codigocostos " +
							
						"FROM " +
							"((SELECT	dvm.codigo AS numdev, dvm.fecha AS fechadev, edv.descripcion AS estadodev, cc.nombre AS centrocdev, 'Solicitud' AS campod, cc.centro_atencion AS cadev, c.codigo_paciente AS cpdv, " +
										"getnombrealmacen(dvm.farmacia) AS nom_farmacia, dvm.motivo AS motDev, per.primer_nombre AS pnom, " +
							//mt6772
										"(sum(drm.cantidadrecibida * drm.costo_unitario)) AS valor, per.segundo_nombre AS snom, per.primer_apellido AS pape, per.segundo_apellido AS sape, edv.codigo AS codestadev, u.login AS usua, rm.usuario AS usuare, dvm.farmacia AS codigofarmacia, cc.codigo AS codigocostos " +
							"FROM	 devolucion_med dvm " +
										"INNER JOIN detalle_devol_med ddm ON (dvm.codigo = ddm.devolucion) " +
										"INNER JOIN mot_devolucion_inventario mdi ON (dvm.motivo = mdi.codigo) " +
										//MT6772
										"LEFT JOIN recepciones_medicamentos rm ON (rm.devolucion = dvm.codigo) " +	
										"LEFT JOIN detalle_recep_medicamentos drm ON (drm.codigo = ddm.codigo AND drm.articulo=ddm.articulo) " +
										"INNER JOIN centros_costo cc ON (dvm.centro_costo_devuelve = cc.codigo) " +
										"INNER JOIN estados_devolucion edv ON (dvm.estado = edv.codigo) " +
										"INNER JOIN usuarios u ON (dvm.usuario = u.login) " +
										"INNER JOIN personas per ON (per.codigo = u.codigo_persona) " +
										"INNER JOIN solicitudes s ON (ddm.numero_solicitud = s.numero_solicitud) " +
										"INNER JOIN cuentas c ON (s.cuenta = c.id) " +
								"GROUP BY dvm.codigo,dvm.fecha,edv.descripcion,cc.nombre,cc.centro_atencion,c.codigo_paciente,dvm.farmacia,dvm.motivo,per.primer_nombre,per.segundo_nombre,per.primer_apellido,per.segundo_apellido, edv.codigo , u.login , rm.usuario , dvm.farmacia , cc.codigo ) " +
						" UNION ALL " +
							"(SELECT	dvp.codigo AS numdev, dvp.fecha AS fechadev, edv.descripcion AS estadodev, cc.nombre AS centrocdev, 'Pedido' AS campod, cc.centro_atencion AS cadev, c.codigo_paciente AS cpdv, " + 
										"getnombrealmacen(p.centro_costo_solicitado) AS nom_farmacia, dvp.motivo AS motDev, per.primer_nombre AS pnom, " + 
										//MT6772
										"(sum(drp.cantidadrecibida * drp.costo_unitario)) AS valor, per.segundo_nombre AS snom, per.primer_apellido AS pape, per.segundo_apellido AS sape,  edv.codigo AS codestadev,  u.login AS usua, rp.usuario AS usuare, p.centro_costo_solicitado AS codigofarmacia, cc.codigo AS codigocostos  " + 
							"FROM	 devolucion_pedidos dvp " + 
										"INNER JOIN detalle_devol_pedido ddp ON (ddp.devolucion = dvp.codigo) " +
										"INNER JOIN mot_devolucion_inventario mdi ON (dvp.motivo = mdi.codigo) " +
										"INNER JOIN pedido p ON (ddp.pedido = p.codigo) " +  
										//MT6772
										"LEFT JOIN recepciones_pedidos rp ON (rp.devolucion = dvp.codigo) " + 
										"LEFT JOIN detalle_recep_pedidos drp ON (drp.codigo = ddp.codigo AND drp.articulo=ddp.articulo) " + 
										"INNER JOIN centros_costo cc ON (p.centro_costo_solicitante = cc.codigo) " + 
										"INNER JOIN estados_devolucion edv ON (dvp.estado = edv.codigo) " +
										"INNER JOIN usuarios u ON (dvp.usuario = u.login) " +
										"INNER JOIN personas per ON (per.codigo = u.codigo_persona) " +
										"INNER JOIN pedidos_peticiones_qx ppqx ON (ppqx.pedido = p.codigo) " +
										"INNER JOIN peticion_qx pqx ON (ppqx.peticion = pqx.codigo) " +
										"INNER JOIN solicitudes_cirugia sc ON (sc.codigo_peticion = pqx.codigo) " +
										"INNER JOIN solicitudes s ON (sc.numero_solicitud = s.numero_solicitud) " +
										"INNER JOIN cuentas c ON (s.cuenta = c.id) " +
								"GROUP BY dvp.codigo,dvp.fecha,edv.descripcion,cc.nombre,cc.centro_atencion,c.codigo_paciente,p.centro_costo_solicitado,dvp.motivo,per.primer_nombre,per.segundo_nombre,per.primer_apellido,per.segundo_apellido, edv.codigo ,  u.login , rp.usuario , p.centro_costo_solicitado , cc.codigo )) " +
						" f WHERE f.cadev = " + centroAtencion + " ";
				
				if(!almacen.equals("-1")){
					//MT 6772 
					sql += " AND f.codigofarmacia = "+almacen;
				}
				
				
				if(!centroCosto.equals("-1")){
					//MT 6772 
					sql += " AND f.codigocostos = "+centroCosto;
				}
					
				
				if(!fechaIni.equals("") && !fechaFin.equals("")){
					
					sql += " AND f.fechaDev BETWEEN '"+fechaIni+"' AND '"+fechaFin+"'";
				}
				
				
				if(!estadoDevolucion.equals("-1")){
					//MT 6772 
					sql += " AND f.codestadev = "+estadoDevolucion;
				}
				
				
				if(!motivoDevolucion.equals("-1")){
					
					sql += " AND f.motDev = '"+motivoDevolucion+"'";
				}
					
				
				if(!usuarioDevuelve.equals("")){
					//MT 6772 
					sql+= " AND f.usua = '"+usuarioDevuelve+"'";
					//sql += " AND UPPER(f.pnom || ' ' || f.snom || ' ' || f.pape || ' ' || f.sape) LIKE UPPER('%"+usuarioDevuelve+"%') ";
				
				}
					
				if(!usuarioRecibe.equals("")){
					//MT 6772 
					sql+= " AND f.usuare = '"+usuarioRecibe+"'";
					//sql += " AND UPPER(f.pnom || ' ' || f.snom || ' ' || f.pape || ' ' || f.sape) LIKE UPPER('%"+usuarioRecibe+"%') ";
				
				}
				
				sql += " ORDER BY f.numDev ";
			}	
		}	
		
		return SqlBaseConsultaDevolucionInventarioPacienteDao.consultaDevR(con, numeroDevolucion, centroAtencion, almacen, centroCosto, fechaIni, fechaFin, estadoDevolucion, tipoDevolucion, motivoDevolucion, usuarioDevuelve, usuarioRecibe, sql);
	}
}