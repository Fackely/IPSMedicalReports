/*
 * Oct 4, 2005
 *
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import com.princetonsa.dao.DaoFactory;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.RespuestaHashMap;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.inventarios.UtilidadInventarios;

/**
 * @author Sebastián Gómez
 *
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la funcionalidad de Materiales Qx
 */
public class SqlBaseMaterialesQxDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseMaterialesQxDao.class);
	
	
	
	//*****************************************************************************************************
	//*****************************************************************************************************
	/**
	 * Cadena para consultar las ordenes de cirugia de una cuenta
	 */
	private static final String cargarOrdenesCirugiaSELECTStr = "SELECT "+
		"s.numero_solicitud AS numero_solicitud, "+
		"to_char(s.fecha_solicitud,'DD/MM/YYYY') ||' '|| hora_solicitud AS fecha_orden,"+
		"s.consecutivo_ordenes_medicas AS orden, "+
		"getnombrepersona(s.codigo_medico) AS medico_solicita , "+
		"coalesce(getnombrepersona(us.codigo_persona),'') AS medico_responde, "+
		"espeticionsolcitado(p.codigo) AS peticion_diferente," +
		"p.codigo AS peticion," +
		"getnombreestadopeticion(p.estado_peticion) AS estadoPeticion," +
		"to_char(p.fecha_peticion,'DD/MM/YYYY') AS fechapeticion," +
		"p.hora_peticion AS horapeticion,";
	
	/**
	 * Sección SELECT para consultar la fecha de cirugia de la petición
	 * de las ordenes de cirugia
	 */
	private static final String cargarOrdenesCirugiaSELECT_01Str = ""+
		"CASE WHEN p.fecha_cirugia IS NULL THEN to_char(sc.fecha_inicial_cx,'DD/MM/YYYY') ELSE coalesce(to_char(p.fecha_cirugia,'DD/MM/YYYY'),'') END AS fecha_cirugia ";
	
	/**
	 * Sección SELECT para consultar la fecha inicial de la cirugia de la
	 * hoja quirurgica en cada orden de cirugia
	 */
	private static final String cargarOrdenesCirugiaSELECT_02Str = ""+
		"to_char(sc.fecha_inicial_cx,'DD/MM/YYYY') AS fecha_cirugia ";
	
	/**
	 * Sección SELECT para consultar la fecha inicial de la cirugia de la
	 * hoja quirurgica en cada orden de cirugia y el valor de la orden
	 */
	private static final String cargarOrdenesCirugiaSELECT_03Str = ""+
		"to_char(sc.fecha_inicial_cx,'DD/MM/YYYY') AS fecha_cirugia ";
	
	/**
	 * Sección select para consultar el nombre del paciente
	 * en cada orden de cirugia
	 */
	private static final String cargarOrdenesCirugiaSELECT_04Str = " " +
		"getnombrepersona(cue.codigo_paciente) AS nombre_paciente, ";
	
	/**
	 * Sección FROM para consultar las ordenes de cirugia de una cuenta
	 */
	private static final String cargarOrdenesCirugiaFROMStr = ""+
		"FROM solicitudes s "+ 
		"INNER JOIN solicitudes_cirugia sc on(sc.numero_solicitud=s.numero_solicitud) " +
		"INNER JOIN peticion_qx p ON(sc.codigo_peticion=p.codigo) ";
	
	
	/**
	 * Sección LEFT OUTER JOIN que vincula la hoja quirurgica a la
	 * cosnulta de ordenes de cirugia
	 */
	private static final String cargarOrdenesCirugiaLEFTOUTERStr = " "+
		"LEFT OUTER JOIN usuarios us ON(us.login=sc.usuario_grabacion_salida) ";
	
	/**
	 * Seccion INNER JOIN que vincula la cuenta a la consulta de ordenes
	 * de cirugia
	 */
	private static final String cargarOrdenesCirugiaINNER_01Str = "" +
		"INNER JOIN cuentas cue ON(cue.id=s.cuenta) " +
		"INNER JOIN centros_costo cc ON(cc.codigo=cue.area) ";
		
	
	/**
	 * Sección WHERE de la consulta de ordenes de cirugia de la cuenta
	 */
	private static final String cargarOrdenesCirugiaWHEREStr = "WHERE "+ 
			"s.cuenta=? ";
			
	
	/**
	 * Sección WHERE de la consulta de ordenes de cirugia de la cuenta
	 */
	private static final String cargarOrdenesCirugiaWHERE1Str = "WHERE "+ 
			"s.cuenta IN(?,?) ";
	
	/**
	 * Cadena que consulta los datos generales del registro de materiales Qx.
	 */
	private static final String cargarDatosGeneralesStr="SELECT "+ 
		"m.numero_solicitud AS numero_solicitud,"+
		"to_char(m.fecha,'DD/MM/YYYY') AS fecha,"+
		"m.hora AS hora,"+
		"m.centro_costo AS centro_costo,"+
		"getnomcentrocosto(m.centro_costo) AS nombre_centro_costo, "+
		"coalesce(m.farmacia,"+ConstantesBD.codigoNuncaValido+") AS farmacia,"+
		"coalesce(getnomcentrocosto(m.farmacia),'') AS nombre_farmacia, "+
		"m.usuario AS usuario," +
		"m.es_acto AS es_acto," +
		"coalesce(m.finalizado,'') AS finalizado " +
		"FROM materiales_qx m WHERE m.numero_solicitud=?";
	
	/**
	 * Cadena que consulta las cirugias de una orden de cirugia
	 */
	private static final String cargarCirugiasStr = "SELECT "+ 
		"servicio AS codigo,"+
		"getnombreservicio(servicio,0) As descripcion,"+
		"consecutivo AS consecutivo "+ 
		"FROM sol_cirugia_por_servicio WHERE numero_solicitud=? " +
		"ORDER BY consecutivo";	
	
	
	//*****************************CONSULTA DE LOS ARTÍCULOS DEL CONSUMO DE MATERIALES******************************************************
	/**
	 * Sección Select para la consulta básica de la información de artículos
	 */
	private static final String cargarArticulosMaterialesSELECT_BASICO = "SELECT " +
		"va.codigo ||'-'|| va.descripcion || " +
		"' Conc:' || coalesce(va.concentracion,'') ||  " +
		"' F.F.:' || coalesce(getNomFormaFarmaceutica(va.forma_farmaceutica),'') || " +
		"' Nat:' || coalesce(getNomNaturalezaArticulo(va.naturaleza),'') AS articulo,"+
		"getNomUnidadMedida(va.unidad_medida) AS unidadMedida,"+
		"getTotalConsAnt(t.numero_solicitud,va.codigo,'"+ConstantesIntegridadDominio.acronimoEstadoPendiente+"') As total, " +
		"getTotalConsAnt(t.numero_solicitud,va.codigo,'"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"') As totalConsAnt, " +
		"coalesce(gettotalpedidosarticulos (va.codigo,t.numero_solicitud),0) AS totalPedidos," +
		"getespos(va.codigo) as tipoPosArticulo, " +
		"va.codigo as codigoArticulo, " +
		"gettienejustificacionnopos(va.codigo, t.numero_solicitud) as yajustifico," +
		"t.numero_solicitud as sol, ";


	
	/**
	 * Sección FROM para la consulta de artículos de materiales Qx. por Acto
	 */
	private static final String cargarArticulosMaterialesFROM_01 = "" +
		"'"+ConstantesBD.acronimoSi+"'"+" AS estaBd " + 
		"FROM det_materiales_qx t INNER JOIN view_articulos va ON(va.codigo=t.articulo) "+ 
		"WHERE t.numero_solicitud=? ";
	
	/**
	 * Sección ORDER para la ordenación del listado de artículos
	 */
	private static final String cargarArticulosMaterialesORDER = "ORDER BY va.descripcion";
	
	/**
	 * Sección GROUP BY para la ordenación del listado de artículos
	 */
	private static final String cargarArticulosMaterialesGROUPBY = "GROUP BY t.numero_solicitud,va.codigo,va.descripcion,va.concentracion, va.forma_farmaceutica, va.unidad_medida, va.naturaleza ";
	
	//**********************************************************************************************************************************************
	
	
	/**
	 * Cadena INSERT  para el ingreso del encabezado de los materiales Qx. 
	 */
	private static final String insertarEncabezadoMaterialesINSERT ="INSERT INTO materiales_qx " +
			"(numero_solicitud,centro_costo,usuario,fecha,hora,es_acto,finalizado,usuario_modifica,fecha_modifica,hora_modifica,farmacia,entidad_subcontratada) VALUES(?,?,?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?)";
	

	/**
	 * Sección UPDATE para la modificación del encabezado de materiales Qx.
	 */
	private static final String modificarEncabezadoMaterialesUPDATE = "UPDATE materiales_qx SET " +
		"centro_costo = ?, usuario = ?, fecha = ?, hora = ?, es_acto = ?, " +
		"finalizado = ?, usuario_modifica = ?, fecha_modifica = CURRENT_DATE, hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", farmacia = ? ,entidad_subcontratada=?" +
		" WHERE numero_solicitud = ?";

	
	/**
	 * Cadena para consultar los artículos del despacho
	 */
	private static final String consultarCantidadesArticulosDespachoStr = "SELECT * FROM "+ 
		"( "+
			//***************CONSULTA DE LOS DESPACHOS DE LA PETICION***********************************
			"SELECT " +
			"va.codigo AS codigo_articulo, "+ 
			"SUM(dp.cantidad) AS total_pedidos, "+ 
			"SUM(dp.cantidad) AS total, "+ 
			"va.codigo ||'-'|| va.descripcion || "+ 
				"' Conc:' || coalesce(va.concentracion,'') || "+ 
				"' F.F:' || coalesce(getNomFormaFarmaceutica(va.forma_farmaceutica),'') || "+
				"' Nat:' || coalesce(getNomNaturalezaArticulo(va.naturaleza),'') AS articulo, "+ 
			"getNomUnidadMedida(va.unidad_medida) AS unidad_medida, "+
			"0 AS total_cons_ant, "+
			"'"+ConstantesBD.acronimoNo+"' As esta_bd "+ 
			"FROM solicitudes_cirugia sc "+ 
			"INNER JOIN peticion_qx pq ON(pq.codigo=sc.codigo_peticion) "+ 
			"INNER JOIN pedidos_peticiones_qx ppq ON(ppq.peticion=pq.codigo) "+ 
			"INNER JOIN detalle_despacho_pedido dp ON(dp.pedido=ppq.pedido) "+ 
			"INNER JOIN articulo va ON(va.codigo=dp.articulo) "+ 
			"WHERE sc.numero_solicitud = ? " +
			"GROUP BY dp.articulo,va.codigo,va.descripcion,va.concentracion,va.forma_farmaceutica,va.unidad_medida,va.naturaleza "+
			"UNION "+ 
			//**************CONSULTA DE LOS PEDIDOS TERMINADOS DE LA PETICION**********************************
			"SELECT "+ 
			"va.codigo AS codigo_articulo, "+
			"SUM(dp.cantidad) AS total_pedidos, "+ 
			"SUM(dp.cantidad) AS total, "+ 
			"va.codigo ||'-'|| va.descripcion || "+ 
				"' Conc:' || coalesce(va.concentracion,'') || "+ 
				"' F.F:' || coalesce(getNomFormaFarmaceutica(va.forma_farmaceutica),'') || "+
				"' Nat:' || coalesce(getNomNaturalezaArticulo(va.naturaleza),'') AS articulo, "+ 
			"getNomUnidadMedida(va.unidad_medida) AS unidad_medida, "+
			"0 AS total_cons_ant, "+
			"'"+ConstantesBD.acronimoNo+"' As esta_bd "+ 
			"FROM solicitudes_cirugia sc "+ 
			"INNER JOIN peticion_qx pq ON(pq.codigo=sc.codigo_peticion) "+ 
			"INNER JOIN pedidos_peticiones_qx ppq ON(ppq.peticion=pq.codigo) "+ 
			"INNER JOIN pedido p ON(p.codigo=ppq.pedido) "+ 
			"INNER JOIN detalle_pedidos dp ON(dp.pedido=ppq.pedido) "+ 
			"INNER JOIN articulo va ON(va.codigo=dp.articulo) "+ 
			"WHERE sc.numero_solicitud = ? AND p.estado = "+ConstantesBD.codigoEstadoPedidoTerminado+" "+ 
			"GROUP BY dp.articulo,va.codigo,va.descripcion,va.concentracion,va.forma_farmaceutica,va.unidad_medida,va.naturaleza "+
		") t"; 
		
		
		
	/**
	 * Cadena que consulta el consecutivo de un consumo pendiente
	 */
	private static final String getCodigoConsumoPendienteStr = "SELECT codigo FROM det_materiales_qx WHERE numero_solicitud = ? AND articulo = ? AND estado = '"+ConstantesIntegridadDominio.acronimoEstadoPendiente+"' ";	
		
	/**
	 * Cadena implementada para eliminar el detalle final del consumo
	 */
	private static final String eliminarDetalleFinalizacionConsumoStr = "DELETE FROM det_fin_materiales_qx WHERE numero_solicitud = ? AND articulo = ? ";
	
	/**
	 * Cadena implementada para obtener el consumo total de un artículo
	 */
	private static final String obtenerConsumoTotalArticuloStr = "SELECT coalesce(sum(cantidad),0) AS total FROM det_materiales_qx WHERE numero_solicitud = ? AND articulo = ?";
	
	/**
	 * Cadena que verifica si una solicitud tiene una hoja de anestesia finalizada
	 */
	private static final String existeHojaAnestesiaFinalizadaStr = "SELECT count(1) as cuenta " +
		"FROM hoja_quirurgica hq " +
		"LEFT OUTER JOIN hoja_anestesia ha on(hq.numero_solicitud=ha.numero_solicitud) " +
		"WHERE hq.numero_solicitud = ? AND " +
		"hq.participo_anestesiologo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" AND " +
		"(ha.finalizada = "+ValoresPorDefecto.getValorFalseParaConsultas()+" OR ha.finalizada IS NULL)";
	
	/**
	 * Cadena que realiza la reversión de la finalización del consumo
	 */
	private static final String reversionFinalizacionConsumoStr = "UPDATE materiales_qx SET " +
		"finalizado = '"+ConstantesBD.acronimoNo+"', usuario_modifica = ?, fecha_modifica = CURRENT_DATE, hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" " +
		"WHERE numero_solicitud = ?";;
	
	/**
	 * Cadena que consulta el pedido pendiente de una petición
	 */
	private static final String consultarPedidoPendientePeticionStr = "SELECT "+ 
		"p.codigo AS codigo_pedido, "+
		"to_char(p.fecha,'"+ConstantesBD.formatoFechaAp+"') AS fecha, "+ 
		"substr(p.hora,0,6) AS hora, "+ 
		"getnomcentrocosto(p.centro_costo_solicitado) AS nombre_farmacia, "+
		"p.centro_costo_solicitado as codigo_farmacia, "+
		"p.urgente, "+
		"p.estado "+ 
		"FROM pedido p "+ 
		"INNER JOIN pedidos_peticiones_qx pp ON(pp.pedido=p.codigo) "+ 
		"WHERE pp.peticion = ? AND p.centro_costo_solicitado = ? AND p.estado = "+ConstantesBD.codigoEstadoPedidoSolicitado;
	
	/**
	 * Cadena que consulta los artículos de un pedido
	 */
	private static final String consultarArticuloPedidoPeticionStr = "SELECT "+ 
		"va.codigo AS codigo_articulo, "+ 
		"va.codigo || '-' || va.descripcion ||' CONC:'|| coalesce(va.concentracion,'')  ||' F.F:'|| coalesce(getNomFormaFarmaceutica(va.forma_farmaceutica),'') || ' NAT:' || coalesce(getnomnaturalezaarticulo(va.naturaleza),'') AS articulo, "+ 
		"dp.cantidad AS cantidad, "+ 
		"getNomUnidadMedida(va.unidad_medida) AS unidad_medida_articulo," +
		"'"+ConstantesBD.acronimoSi+"' AS existe_bd "+ 
		"FROM detalle_pedidos dp "+ 
		"INNER JOIN articulo va ON(va.codigo=dp.articulo) "+ 
		"WHERE dp.pedido = ? ORDER BY articulo";
	
	/**
	 * Método que consulta los artículos de pedidos anteriores de la peticion
	 */
	private static final String consultarArticulosPedidosAnterioresPeticionStr = "SELECT "+ 
		"va.codigo AS codigo_articulo, "+ 
		"va.codigo || '-' || va.descripcion ||' CONC:'|| coalesce(va.concentracion,'')  ||' F.F:'|| coalesce(getNomFormaFarmaceutica(va.forma_farmaceutica),'') || ' NAT:' || coalesce(getnomnaturalezaarticulo(va.naturaleza),'') AS articulo, "+ 
		"sum(dp.cantidad) AS cantidad_pedido, "+ 
		"getNomUnidadMedida(va.unidad_medida) AS unidad_medida_articulo "+ 
		"FROM pedidos_peticiones_qx pp "+ 
		"INNER JOIN pedido p ON(p.codigo=pp.pedido) "+ 
		"INNER JOIN detalle_pedidos dp ON(dp.pedido=p.codigo) "+ 
		"INNER JOIN articulo va ON(va.codigo=dp.articulo) "+ 
		"WHERE pp.peticion = ? AND p.estado IN ("+ConstantesBD.codigoEstadoPedidoDespachado+","+ConstantesBD.codigoEstadoPedidoTerminado+") "+ 
		"GROUP BY va.codigo,va.descripcion,va.concentracion,va.forma_farmaceutica,va.naturaleza,va.unidad_medida";
	
	/**
	 * Cadena que consulta los centros de costo de los pedidos de la peticion
	 */
	private static final String consultarCentrosCostoPedidosPeticionStr = "SELECT  " +
		"p.centro_costo_solicitante AS centro_costo," +
		"getnomcentrocosto(p.centro_costo_solicitante) || ' (' || getcentroatencioncc(p.centro_costo_solicitante) || ')' AS nombre_centro_costo, "+
		"p.centro_costo_solicitado As farmacia, " +
		"getnomcentrocosto(p.centro_costo_solicitado) || ' (' || getcentroatencioncc(p.centro_costo_solicitado) || ')' AS nombre_farmacia "+
		"FROM pedidos_peticiones_qx ppq " +
		"INNER JOIN pedido p ON(p.codigo=ppq.pedido) " +
		"WHERE ppq.peticion = ? order by ppq.pedido";
	
	
	
	/**
	 * Cadena para consultar las farmacia de los pedidos de la petición
	 */
	private static final String obtenerFarmaciasPedidosPeticionStr = "select " +
		"distinct getnomcentrocosto(p.centro_costo_solicitado) as farmacia " +
		"from pedidos_peticiones_qx ppq " +
		"inner join pedido p on(p.codigo = ppq.pedido) " +
		"WHERE " +
		"ppq.peticion = ? " +
		"and p.estado in ("+ConstantesBD.codigoEstadoPedidoTerminado+") " +
		"order by farmacia";
	
	/**
	 * Cadena usada para insertar el pedido al consumo
	 */
	private static final String insertarPedidoConsumoStr = "INSERT INTO det_ped_materiales_qx (det_materiales_qx,pedido) VALUES (?,?)";
	
	/**
	 * Cadena para validar la finalización del consumo
	 */
	private static final String validarTransaccionInventarioStr = "SELECT " +
		"count(1) as cuenta " +
		"from inventarios.trans_validas_x_cc_inven tv " +
		"WHERE " +
		"tv.centros_costo = ? and " +
		"tv.tipos_trans_inventario = ? and " +
		"tv.clase_inventario = getclaseinventarioarticulo(?) and " +
		"tv.institucion = ?";
	
	
	//***********************************************************************************************************************
	//***********************************************************************************************************************
	//*******************MÉTODOS PARA LA OPCIÓN INGRESO CONSUMO DE MATERALES*************************************************
	//***********************************************************************************************************************
	
	/**
	 * Método usado para consultar las ordenes de cirugia de una cuenta 
	 * que tengan estado de facturacion 'Pendiente' y estado de historia clínica 'Interpretada'
	 * @param con
	 * @param cuenta
	 * @param cargarOrdenesCirugiaStr
	 * @param funcionalidad
	 * @return
	 */
	public static HashMap cargarOrdenesCirugia(Connection con,int cuenta,String funcionalidad)
	{
		
		try
		{
			
			//***********SE VERIFICA SI LA CUENTA HACE PARTE DE UN ASOCIO*************************
			String cuentaAsociada = "";
			String consulta = "SELECT cuenta_inicial FROM asocios_cuenta WHERE activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" AND cuenta_final = "+cuenta;
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				cuentaAsociada = rs.getString("cuenta_inicial");
			//*************************************************************************************
			
			consulta = cargarOrdenesCirugiaSELECTStr;
			int pos = 1;
			
			//FUNCIONALIDAD LIQUIDACION QX.
			if(funcionalidad.equals("liquidacion"))
				consulta+=cargarOrdenesCirugiaSELECT_02Str ;
			//FUNCIONALIDAD MATERIALES QX.
			else if(funcionalidad.equals("materiales"))
				consulta+=cargarOrdenesCirugiaSELECT_01Str ;
			//FUNCIONALIDAD MODIFICAR/REVERSAR LIQUIDACION QX.
			else if(funcionalidad.equals("modificar/reversar")||
					funcionalidad.equals("consultaLiquidacion"))
				consulta += cargarOrdenesCirugiaSELECT_03Str ;
			
			
			
			consulta+= cargarOrdenesCirugiaFROMStr ;
			consulta += cargarOrdenesCirugiaLEFTOUTERStr ;
			
			int paciente= 0;
			String consPac="SELECT cu.codigo_paciente as pac FROM cuentas cu WHERE cu.id="+cuenta+ "";
				HashMap codigop= new HashMap();
				try {
					
			    	PreparedStatementDecorator pst1= new PreparedStatementDecorator(con.prepareStatement(consPac,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			    	ResultSetDecorator rs1= new ResultSetDecorator(pst1.executeQuery());
			    	codigop= UtilidadBD.cargarValueObject(rs1);
			    	rs1.close();
			    	pst1.close();
			    paciente= 	Integer.parseInt(codigop.get("pac_0").toString());
				}
				catch(Exception e){}
				
			String cuentasActivas="Select cu.id as idcuenta from cuentas cu where cu.codigo_paciente="+paciente+" and cu.estado_cuenta IN(0,3,6)";
			HashMap mapa=new HashMap();
			String cuentaso="";
			try {
			
	    	PreparedStatementDecorator pst1= new PreparedStatementDecorator(con.prepareStatement(cuentasActivas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    	ResultSetDecorator rs1= new ResultSetDecorator(pst1.executeQuery());
	    	mapa= UtilidadBD.cargarValueObject(rs1);
	    	rs1.close();
	    	pst1.close();}
			catch(Exception e){}

			
			//se verifica si se busca también por la cuenta asociada
			if(cuentaAsociada.equals("")||cuentaAsociada.equals("0"))
				{consulta += "WHERE "+ 
						"s.cuenta=? ";}
			else
				{consulta += "WHERE s.cuenta IN("; 
				for (int m=0; m<Integer.parseInt(mapa.get("numRegistros").toString());m++)
		    	{
					consulta +="?";
					if (m+1<Integer.parseInt(mapa.get("numRegistros").toString()))
					{
						consulta +=",";
					}
					
		    	}
				consulta+=") ";
			   }
			
			if(funcionalidad.equals("materiales"))
				consulta += " AND s.estado_historia_clinica != "+ConstantesBD.codigoEstadoHCAnulada+" ";
			else 
				consulta += " AND s.estado_historia_clinica in ("+ConstantesBD.codigoEstadoHCInterpretada+"," +ConstantesBD.codigoEstadoHCCargoDirecto+","+ConstantesBD.codigoEstadoHCRespondida+") ";
			
			
			/*----------------------------------------------------
			 * Adicionado por Jhony Alexander Duque A.
			 --------------------------------------------------*/
			//funcionalidad para materiales qx
			/**
			 * Nota * Esta validación se quita por tarea 
			 * 
			 * if(funcionalidad.equals("materiales"))
			{
				consulta+= " AND esSolicitudTotalPendiente(s.numero_solicitud) = '"+ConstantesBD.acronimoSi+"' ";
			}**/
			
			
			
			//FUNCIONALIDAD LIQUIDACION QX. 
			//la solicitud debe estar pendiente
			if(funcionalidad.equals("liquidacion"))
			{
				consulta += " AND esSolicitudTotalPendiente(s.numero_solicitud) = '"+ConstantesBD.acronimoSi+"' ";
			}
			//FUNCIONALIDAD MODIFICAR/REVERSAR LIQUIDACION QX.
			//la solicitud debe estar cargada
			else if(funcionalidad.equals("modificar/reversar")||
					funcionalidad.equals("consultaLiquidacion"))
			{
				consulta += " AND esSolCxCargada(s.numero_solicitud) = '"+ConstantesBD.acronimoSi+"' ";
			}
			
			//
			
			//Se agrega el order by
			consulta += " ORDER BY s.fecha_solicitud,s.hora_solicitud,orden";
			
			logger.info("CONSULTA ORDENES CX=> "+consulta+" cuenta=> "+cuenta+", cuentaASociada=> "+cuentaAsociada);
			pst= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			if(!cuentaAsociada.equals("")&&!cuentaAsociada.equals("0"))
			{
				for (int m=0; m<Integer.parseInt(mapa.get("numRegistros").toString());m++)
		    	{
					pst.setString(m+1,mapa.get("idcuenta_"+m).toString());
		    	}
				
			}
			else
			{
			pst.setInt(pos,cuenta);
			pos++;
			}
			
			
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()),true,false);
			pst.close();
			return mapaRetorno;
			
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarOrdenesCirugia de SqlBaseMaterialesQxDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método que realiza uan busqueda avanzada de ordenes de cirugia
	 * que ya estén cargadas
	 * @param con
	 * @param ordenInicial
	 * @param ordenFinal
	 * @param fechaOrdenInicial
	 * @param fechaOrdenFinal
	 * @param fechaCxInicial
	 * @param fechaCxFinal
	 * @param medico
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap cargarOrdenesCirugia(Connection con,
			String ordenInicial,String ordenFinal,String fechaOrdenInicial,
			String fechaOrdenFinal,String fechaCxInicial,String fechaCxFinal,int medico,int centroAtencion)
	{
		//columnas
		String[] columnas={
				"numero_solicitud",
				"fecha_orden",
				"orden",
				"medico_solicita",
				"fecha_cirugia",
				"medico_responde",
				"nombre_paciente"
				};
		try
		{
			
			String consulta = cargarOrdenesCirugiaSELECTStr +
				cargarOrdenesCirugiaSELECT_04Str +
				cargarOrdenesCirugiaSELECT_03Str + 
				cargarOrdenesCirugiaFROMStr +
				cargarOrdenesCirugiaINNER_01Str +
				cargarOrdenesCirugiaLEFTOUTERStr+
				" WHERE "+
				"esSolCxCargada(s.numero_solicitud) = '"+ConstantesBD.acronimoSi+"' AND " +
				"s.estado_historia_clinica IN ("+ ConstantesBD.codigoEstadoHCInterpretada + ","+ConstantesBD.codigoEstadoHCCargoDirecto+","+ConstantesBD.codigoEstadoHCRespondida+") ";
				
			
			//rango ordenes
			if(!ordenInicial.equals("")&&!ordenFinal.equals(""))
			{
				consulta += " AND  s.consecutivo_ordenes_medicas BETWEEN "+
					ordenInicial+" AND "+ordenFinal;
			}
			
			//rango fechas orden
			if(!fechaOrdenInicial.equals("")&&!fechaOrdenFinal.equals(""))
			{
				consulta += " AND s.fecha_solicitud BETWEEN '"+
					UtilidadFecha.conversionFormatoFechaABD(fechaOrdenInicial)+"' AND '"+
					UtilidadFecha.conversionFormatoFechaABD(fechaOrdenFinal)+"' ";
			}
			
			//rango fechas cirugia
			if(!fechaCxInicial.equals("")&&!fechaCxFinal.equals(""))
			{
				consulta += " AND  sc.fecha_inicial_cx BETWEEN '"+
				UtilidadFecha.conversionFormatoFechaABD(fechaCxInicial)+"' AND '"+
				UtilidadFecha.conversionFormatoFechaABD(fechaCxFinal)+"' ";
			}
			
			//medico
			if(medico>0)
			{
				consulta += " AND us.codigo_persona = "+medico;
			}
			
			consulta += " AND cc.centro_atencion = "+centroAtencion;
			consulta += " ORDER BY s.fecha_solicitud,s.hora_solicitud,orden ";
			logger.info("PASO POR AQUI CONSULTYA ARMADA=> "+consulta);
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(st.executeQuery(consulta)),false,true);
			return listado.getMapa();
			
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarOrdenesCirugia de SqlBaseMaterialesQxDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método usado para consultar los datos generales de un
	 * registro de materiales Qx.
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap cargarDatosGenerales(Connection con,int numeroSolicitud)
	{
		//columnas
		String[] columnas={
				"numero_solicitud",
				"fecha",
				"hora",
				"centro_costo",
				"nombre_centro_costo",
				"farmacia",
				"nombre_farmacia",
				"usuario",
				"es_acto",
				"finalizado"
				};
		try
		{
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(cargarDatosGeneralesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,numeroSolicitud);
			
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarDatosGenerales de SqlBaseMaterialesQxDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método para consultar las cirugías de un acto quirúgico que harán
	 * parte del registro de los materiales Qx.
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap cargarCirugiasPorActo(Connection con,int numeroSolicitud)
	{
		//columnas
		String[] columnas={
				"codigo",
				"descripcion",
				"consecutivo"
				};
		try
		{
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cargarCirugiasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,numeroSolicitud);
			
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarCirugiasPorActo de SqlBaseMaterialesQxDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método implementado para cargar los artículos para Materiales Qx. buscando así:
	 * 1) Se buscan primero si existen articulos ya ingresados en Materiales Qx.
	 * 2) Si no hay, se buscan artículos en el pedido de la orden.
	 * 3) Si no hay, se buscan artículos en la hoja de gastos
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap cargarArticulosMaterialesQx(Connection con,int numeroSolicitud)
	{
		
		///columnas
		String[] columnas={
				"articulo",
				"unidadMedida",
				"total",
				"totalConsAnt",
				"totalPedidos",
				"estaBd",
				"codigoArticulo",
				"tipoPosArticulo",
				"yajustifico",
				"sol"
				};
		try
		{
			//Se consultan los artículos desde materiales_qx en el caso de que existan
			String consulta = cargarArticulosMaterialesSELECT_BASICO+ cargarArticulosMaterialesFROM_01 + cargarArticulosMaterialesGROUPBY + cargarArticulosMaterialesORDER;
			logger.info("\n\n:::::::ENTE A CARGAR ARTICULOS:::::::::::"+consulta);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,numeroSolicitud);
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			
			return listado.getMapa();
			
			
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarArticulosMaterialesQx de SqlBaseMaterialesQxDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método que inserta la información del encabezado del ingreso de consumo
	 * de materiales Qx.
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param usuario
	 * @param fecha
	 * @param hora
	 * @param esActo
	 * @param finalizado
	 * @param entidadSubcontratada 
	 * @return
	 */
	public static int insertarEncabezadoMaterialesQx(Connection con,
			int numeroSolicitud,
			int centroCosto,
			String usuario,
			String fecha,
			String hora,
			boolean esActo,
			String finalizado,
			String estado,
			int farmacia, String entidadSubcontratada)
	{
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		try
		{
			
			if(estado.equals(ConstantesBD.inicioTransaccion))
				myFactory.beginTransaction(con);
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(insertarEncabezadoMaterialesINSERT,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,numeroSolicitud);
			pst.setInt(2,centroCosto);
			pst.setString(3,usuario);
			pst.setString(4,UtilidadFecha.conversionFormatoFechaABD(fecha));
			pst.setString(5,hora);
			pst.setString(6,esActo?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			if(finalizado.equals(""))
				pst.setNull(7,Types.VARCHAR);
			else
				pst.setString(7,finalizado);
			pst.setString(8,usuario);
			if(farmacia>0)
				pst.setInt(9,farmacia);
			else
				pst.setNull(9,Types.INTEGER);
			
			  if(entidadSubcontratada!=null){
					if(entidadSubcontratada.equals(""))
						pst.setNull(10, Types.VARCHAR);
					else
						pst.setString(10, entidadSubcontratada);
				}else
					pst.setNull(10, Types.VARCHAR);
			int resp = pst.executeUpdate();
			
			if(resp<=0)
				myFactory.abortTransaction(con);
			else if(estado.equals(ConstantesBD.finTransaccion))
				myFactory.endTransaction(con);
			
			return resp;
		}
		catch(SQLException e)
		{
			try
			{
				myFactory.abortTransaction(con);
			}
			catch(SQLException e1)
			{
				logger.error("Error abortando transacción!!");
			}
			logger.error("Error en insertarEncabezadoMaterialesQx de SqlBaseMaterialesQxDao: "+e);
			return -1;
			
		}
	}
	
	
	
	/**
	 * Método que modifica la información del encabezado de materiales Qx.
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param usuario
	 * @param fecha
	 * @param hora
	 * @param esActo
	 * @param finalizado
	 * @param estado
	 * @param entidadSubcontratada 
	 * @return
	 */
	public static int modificarEncabezadoMaterialesQx(Connection con,
			int numeroSolicitud,
			int centroCosto,
			String usuario,
			String fecha,
			String hora,
			boolean esActo,
			String finalizado,
			String estado,
			int farmacia, String entidadSubcontratada)
	{

		
		DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		try
		{
			if(estado.equals(ConstantesBD.inicioTransaccion))
				myFactory.beginTransaction(con);
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(modificarEncabezadoMaterialesUPDATE,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			pst.setInt(1,centroCosto);
			pst.setString(2,usuario);
			pst.setString(3,UtilidadFecha.conversionFormatoFechaABD(fecha));
			pst.setString(4,hora);
			pst.setString(5,esActo?ConstantesBD.acronimoSi:ConstantesBD.acronimoNo);
			if(finalizado.equals(""))
				pst.setNull(6,Types.VARCHAR);
			else
				pst.setString(6,finalizado);
			pst.setString(7,usuario);
			pst.setInt(8,farmacia);
			
			
			  if(entidadSubcontratada!=null){
					if(entidadSubcontratada.equals(""))
						pst.setNull(9, Types.VARCHAR);
					else
						pst.setString(9, entidadSubcontratada);
				}else
					pst.setNull(9, Types.VARCHAR);
			  
			pst.setInt(10,numeroSolicitud);
			
			int resp = pst.executeUpdate();
			
			if(resp<=0)
				myFactory.abortTransaction(con);
			else if(estado.equals(ConstantesBD.finTransaccion))
				myFactory.endTransaction(con);
			
			return resp;
		}
		catch(SQLException e)
		{
			try
			{
				myFactory.abortTransaction(con);
			}
			catch(SQLException e1)
			{
				logger.error("Error abortando transacción!!");
			}
			logger.error("Error en modificarEncabezadoMaterialesQx de SqlBaseMaterialesQxDao: ",e);
			return -1;
		}
		
	}
	
	
	/**
	 * Método que carga los artículos por cirugia de un registro de materiales Qx.
	 * @param con
	 * @param numeroSolicitud
	 * @param numeroCirugias
	 * @return
	 */
	public static HashMap cargarArticulosPorCirugia(Connection con,int numeroSolicitud,int numeroCirugias)
	{
		
		//columnas
		int numColumnas = 10 + numeroCirugias;
		String[] columnas=new String[numColumnas];
		
		//adición de las columnas Estandar
		columnas[0]= new String("articulo");
		columnas[1]= new String("unidadMedida");
		columnas[2]= new String("total");
		columnas[3]= new String("totalConsAnt");
		columnas[4]= new String("totalPedidos");
		columnas[5]= new String("estaBd");
		columnas[6]= new String("codigoArticulo");
		columnas[7]= new String("tipoPosArticulo");
		columnas[8]= new String("yajustifico");
		columnas[9]= new String("sol");
		try
		{
			//se reinicia el numero de columnas
			numColumnas = 10;
			
			//se postula el SELECT BÁSSICO
			String consulta = cargarArticulosMaterialesSELECT_BASICO;
			
			//se edita sección SELECT adicional
			for(int i=1;i<=numeroCirugias;i++)
			{
				consulta += "coalesce(getCantidadPorCx(t.numero_solicitud,va.codigo,"+i+"),0)  AS consumoActual"+i+", ";
				
				//se añade una columna más
				columnas[numColumnas] = new String("consumoActual"+i);
				numColumnas++;
			}
			
			
			
			//se añade la sección FROM_04
			consulta += cargarArticulosMaterialesFROM_01 + cargarArticulosMaterialesGROUPBY + cargarArticulosMaterialesORDER;
			logger.info("\n\n::::::::::ENTRE A CARGAR ARTICULOS POR CIRUGIA:::: "+consulta);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,numeroSolicitud);
			
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
		}
		catch(SQLException e)
		{
			logger.error("Error en cargarArticulosPorCirugia de SqlBaseMaterialesQxDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método que consulta la cantidad de los articulos despachados de la solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap consultarCantidadesArticulosDespacho(Connection con,String numeroSolicitud)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarCantidadesArticulosDespachoStr));
			pst.setObject(1,numeroSolicitud);
			pst.setObject(2,numeroSolicitud);
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
	        pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarArticulosDespacho: "+e);
			return null;
		}
	}
	
	
	//*********************NUEVOS MÉTODOS POR CAMBIO SHAIO DIC/07 ************************************************
	/**
	 * Método para consultar el pedido pendiente de una peticion
	 */
	public static HashMap consultarPedidoPendientePeticion(Connection con,HashMap campos)
	{
		HashMap<String, Object> resultado = new HashMap<String, Object>();
		resultado.put("numRegistros","0");
		try
		{
			//Se consulta el encabezado del pedido
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarPedidoPendientePeticionStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setObject(1, campos.get("codigoPeticion"));
			pst.setInt(2, Integer.parseInt(campos.get("codigoFarmacia").toString()));
			
			resultado =  UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), false, true);
			
			//Se consulta el detalle del pedido (los artículos)
			if(Integer.parseInt(resultado.get("numRegistros").toString())>0)
			{
				pst =  new PreparedStatementDecorator(con.prepareStatement(consultarArticuloPedidoPeticionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setObject(1,resultado.get("codigoPedido"));
				
				resultado.put("articulos", UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true));
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarPedidoPendiente: "+e);
		}
		return resultado;
	}
	
	/**
	 * Método que consulta los artículos de pedidos anteriores de la peticion
	 * @param con
	 * @param campos
	 * @return
	 */
	public static HashMap consultarArticulosPedidosAnterioresPeticion(Connection con,HashMap campos)
	{
		HashMap<String, Object> resultado = new HashMap<String, Object>();
		resultado.put("numRegistros", "0");
		try
		{
			//Se consultan los articulos de pedidos anteriores de la peticion
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarArticulosPedidosAnterioresPeticionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setObject(1,campos.get("codigoPeticion"));
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), true, true);
	        pst.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarArticulosPedidosAnterioresPeticion: "+e);
		}
		return resultado;
	}
	
	/**
	 * Método que consulta los centros de costo de los pedidos existentes de la peticion
	 * @param con
	 * @param numeroPeticion
	 * @return
	 */
	public static HashMap consultarCentrosCostoPedidosPeticion(Connection con,int numeroPeticion)
	{
		HashMap resultados = new HashMap();
		resultados.put("numRegistros", "0");
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consultarCentrosCostoPedidosPeticionStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,numeroPeticion);
			
			logger.info("consulta pedidos peticon=> "+consultarCentrosCostoPedidosPeticionStr+",  numeroPeticion: "+numeroPeticion);
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()), false, true);
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarCentrosCostoPedidosPeticion: "+e);
			
		}
		return resultados;
	}
	
	/**
	 * Método que registra el consumo de los articulos
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ActionErrors guardarArticulosConsumo(Connection con,HashMap campos)
	{
		//Se toman los distintos campos ---------------------------------------------------------
		ActionErrors errores = (ActionErrors)campos.get("errores");
		HashMap articulos = (HashMap)campos.get("articulos");
		HashMap cirugias = (HashMap)campos.get("cirugias");
		String esActo = campos.get("esActo").toString();
		boolean terminado = UtilidadTexto.getBoolean(campos.get("terminado").toString());
		String finalizado = campos.get("finalizado").toString();
		String numeroSolicitud = campos.get("numeroSolicitud").toString();
		int farmacia = Integer.parseInt(campos.get("codigoFarmacia").toString());
		int tipoTransaccionPedido = Integer.parseInt(campos.get("tipoTransaccionPedido").toString()); 
		int codigoInstitucion = Integer.parseInt(campos.get("codigoInstitucion").toString());
		boolean generarPedidoxConsumo = UtilidadTexto.getBoolean(campos.get("generarPedidoXConsumo").toString());
		String usuario = campos.get("usuario").toString();
		//seq_materiales_qx
		String secuenciaFinalizacion = campos.get("secuenciaFinalizacion").toString();
		
 
		//-----------------------------------------------------------------------------------------
		try
		{
			int numArticulos = Integer.parseInt(articulos.get("numRegistros").toString());
			PreparedStatementDecorator pst = null;

			for(int i=0;i<numArticulos;i++)
			{
				//*****************FLUJO POR ACTO************************************************************
				if(UtilidadTexto.getBoolean(esActo))
				{
					errores = guardarConsumoArticuloPorActo(con,articulos,i,numeroSolicitud,errores,terminado,usuario,farmacia,tipoTransaccionPedido,codigoInstitucion,generarPedidoxConsumo);
				}
				//**********************************************************************************************
				//******************FLUJO POR CIRUGIA***********************************************************
				else
				{
					errores = guardarConsumoArticuloPorCirugia(con,articulos,i,numeroSolicitud,errores,terminado,usuario,cirugias,farmacia,tipoTransaccionPedido,codigoInstitucion,generarPedidoxConsumo);
				}
				//***********************************************************************************************	
				
				//***********PROCESO DE FINALIZACIÓN***********************************************************
				//Se pregunta si se debe finalizar el consumo
				if(UtilidadTexto.getBoolean(finalizado))
				{
					
					int codigoArticulo = Integer.parseInt(articulos.get("articulo_"+i).toString().split("-")[0]);
//					boolean articuloValido = false;
					
					//Antes que nada, se eliminan los registros que tengan ese articulo y solicitud
					pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarDetalleFinalizacionConsumoStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst.setObject(1,numeroSolicitud);
					pst.setInt(2,codigoArticulo);
					pst.executeUpdate();
	
					//Se quita esta validación porque no aplica cuando no se genera pedido en el registro
					// de consumo de materiales
//					articuloValido = esArticuloValidoTransaccionAlmacen(con, codigoArticulo, farmacia, tipoTransaccionPedido, codigoInstitucion);
//					if(articuloValido)
//					{
						//****SE REVISA NUEVAMENTE EL FLUJO POR ACTO O POR CIRUGIA*********************************************
						if(UtilidadTexto.getBoolean(esActo))
							errores = registroFinalizacionConsumoPorActo(con,codigoArticulo,numeroSolicitud,errores,secuenciaFinalizacion,usuario);
						else
							errores = registroFinalizacionConsumoPorCirugia(con,articulos,i,numeroSolicitud,errores,secuenciaFinalizacion,usuario,cirugias);
//					}
					//Si la validacion de transaccion es inválida y había cantidad pendiente por pedir entonces se genera mensaje de error
//					else
						
					/**
					 * MT 6832
					 * @author javrammo
					 * Se quita la validacion !UtilidadTexto.getBoolean(articulos.get("validacionTransAlmacen_"+i)+"") ya que se hace mAs arriba.
					 * y debe salir un único mensaje almacen y no por articulo. 
					 * AdemAs la condicion "if(errores.size()<1)" no es valida ya que si se presenta errores anteriores no se incluirIa el mensaje. 
					 */
						
					/*if(!UtilidadTexto.getBoolean(articulos.get("validacionTransAlmacen_"+i)+"")&&Utilidades.convertirAEntero(articulos.get("difPedCons_"+i)+"",true)<0)
					  {
						//Mt 5915
						if(errores.size()<1){
						     errores.add("", new ActionMessage("errors.required","FAVOR SOLICITAR EL DESPACHO DEL PEDIDO QUIRURGICO almacen:"+"\n"+nombreFarmacia));
					      }
					  }
					*/
				}
				//*********************************************************************************************
			} 
		}
		catch(SQLException e)
		{
			logger.error("Error en guardarArticulosConsumo: "+e);
		}
		
		return errores;
	}
	
	
	/**
	 * Método que verifica si un artículo es válido
	 * @param con
	 * @param codigoArticulo
	 * @param codigoFarmacia
	 * @param tipoTransaccionPedido
	 * @param codigoInstitucion
	 * @return
	 */
	private static boolean esArticuloValidoTransaccionAlmacen(Connection con, int codigoArticulo, int codigoFarmacia,int tipoTransaccionPedido,int codigoInstitucion)
	{
		boolean esValido = false;
		try
		{
			///Se valida que el artículo haga parte de la transacción de inventarios
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(validarTransaccionInventarioStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoFarmacia);
			pst.setInt(2,tipoTransaccionPedido);
			pst.setInt(3,codigoArticulo);
			pst.setInt(4,codigoInstitucion);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				if(rs.getInt("cuenta")>0)
				{
					esValido = true;
				}
			}
			
		}
		catch(SQLException e)
		{
			logger.error("Error en esArticuloValidoTransaccionAlmacen: "+e);
		}
		return esValido;
	}
	
	/**
	 * Método implementado para registrar la finalizacion del consumo de un artículo por cada cirugía
	 * @param con
	 * @param pos 
	 * @param articulos 
	 * @param numeroSolicitud
	 * @param errores
	 * @param secuenciaFinalizacion
	 * @param usuario
	 * @param cirugias
	 * @return
	 */
	private static ActionErrors registroFinalizacionConsumoPorCirugia(Connection con, HashMap articulos, int pos, String numeroSolicitud, ActionErrors errores, String secuenciaFinalizacion, String usuario, HashMap cirugias) 
	{
		try
		{
			//Se inserta el detalle del consumo
			String consulta = "INSERT INTO det_fin_materiales_qx (codigo,numero_solicitud,articulo,costo_unitario,cantidad_consumo_total,usuario_modifica,fecha_modifica,hora_modifica,servicio) values ("+secuenciaFinalizacion+",?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?)";
			String actualizacionPorActo = "UPDATE det_materiales_qx SET servicio = ?, usuario_modifica = ?, fecha_modifica = current_date,  hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", estado = '"+ConstantesIntegridadDominio.acronimoEstadoFinalizado+"' WHERE articulo = ? AND servicio is null ";
			
			
			int numCirugias = Integer.parseInt(cirugias.get("numRegistros").toString());
			PreparedStatementDecorator pst = null;
			
			//Se iteran las cirugias
			for(int j=0;j<numCirugias;j++)
			{
				int codigoArticulo = Integer.parseInt(articulos.get("articulo_"+pos).toString().split("-")[0]);
				int codigoServicio = Integer.parseInt(cirugias.get("codigo_"+j).toString());
				int cantidad = obtenerConsumoTotalArticulo(con, numeroSolicitud, codigoArticulo, codigoServicio);
				
				//Si la cirugía es la Nr° 1, entonces se le asigna el consumo que haya quedado por fuera
				//Nota * Este es un proceso que se tiene que hacer en el caso de que se haya registrado inicialmente un consumo por acto
				//y repentinamente se haya pasado a consumo por cirugia, para que los articulos que no tienen servicio asociado no se queden por fuera
				if(j==0)
				{
					pst =  new PreparedStatementDecorator(con.prepareStatement(actualizacionPorActo, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst.setInt(1,codigoServicio);
					pst.setString(2,usuario);
					pst.setInt(3,codigoArticulo);
					pst.executeUpdate();
				}
				
				//Solo se registran los artículos que hayan tenido cantidad asociada al servicio
				if(cantidad > 0)
				{
				
					pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst.setObject(1,numeroSolicitud);
					pst.setInt(2,codigoArticulo);
					pst.setDouble(3, UtilidadInventarios.costoActualArticulo(codigoArticulo));
					pst.setInt(4,cantidad);
					pst.setString(5,usuario);
					pst.setInt(6,codigoServicio);
					
					if(pst.executeUpdate()<=0)
						errores.add("",new ActionMessage("errors.ingresoDatos","la finalización del consumo del artículo "+codigoArticulo+" en la cirugía "+cirugias.get("descripcion_"+j)));
				}
				
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en registroFinalizacionConsumoPorCirugia: "+e);
		}
		return errores;
	}

	/**
	 * Método que registra a finalización del consumo de un artículo por acto
	 * @param con
	 * @param codigoArticulo
	 * @param numeroSolicitud
	 * @param errores
	 * @param secuenciaFinalizacion
	 * @param usuario 
	 * @return
	 */
	private static ActionErrors registroFinalizacionConsumoPorActo(Connection con, int codigoArticulo, String numeroSolicitud, ActionErrors errores, String secuenciaFinalizacion, String usuario) 
	{
		try
		{
			//Se inserta el detalle del consumo
			String consulta = "INSERT INTO det_fin_materiales_qx (codigo,numero_solicitud,articulo,costo_unitario,cantidad_consumo_total,usuario_modifica,fecha_modifica,hora_modifica) values ("+secuenciaFinalizacion+",?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+")";
			
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setObject(1,numeroSolicitud);
			pst.setInt(2,codigoArticulo);
			pst.setDouble(3, UtilidadInventarios.costoActualArticulo(codigoArticulo));
			pst.setInt(4,obtenerConsumoTotalArticulo(con,numeroSolicitud,codigoArticulo,ConstantesBD.codigoNuncaValido));
			pst.setString(5,usuario);
			
			if(pst.executeUpdate()<=0)
				errores.add("",new ActionMessage("errors.ingresoDatos","la finalización del consumo del artículo "+codigoArticulo));
		}
		catch(SQLException e)
		{
			logger.error("Error en registroFinalizacionConsumoPorActo: "+e);
		}
		return errores;
	}

	/**
	 * Método usado para obtener el consumo total de un artículo
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoArticulo
	 * @return
	 */
	private static int obtenerConsumoTotalArticulo(Connection con, String numeroSolicitud, int codigoArticulo,int codigoServicio) 
	{
		int cantidad = 0;
		try
		{
			String consulta = obtenerConsumoTotalArticuloStr ;
			
			if(codigoServicio>0)
				consulta += " AND servicio = "+codigoServicio;
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setObject(1,numeroSolicitud);
			pst.setInt(2, codigoArticulo);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				cantidad = rs.getInt("total");
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerConsumoTotalArticulo: "+e);
		}
		return cantidad;
	}

	/**
	 * Método implamentado para ingresar el consumo de articulos por cirugia
	 * @param con
	 * @param articulos
	 * @param pos
	 * @param numeroSolicitud
	 * @param errores
	 * @param terminado
	 * @param usuario
	 * @param cirugias
	 * @param codigoInstitucion 
	 * @param tipoTransaccionPedido 
	 * @param codigoFarmacia 
	 * @param generarPedidoxConsumo 
	 * @return
	 */
	private static ActionErrors guardarConsumoArticuloPorCirugia(Connection con, HashMap articulos, int pos, String numeroSolicitud, ActionErrors errores, boolean terminado, String usuario, HashMap cirugias, int codigoFarmacia, int tipoTransaccionPedido, int codigoInstitucion, boolean generarPedidoxConsumo) 
	{
		try
		{
			int numCirugias = Integer.parseInt(cirugias.get("numRegistros").toString());
			int sumCantidad = 0;
			int codigoArticulo = Integer.parseInt(articulos.get("articulo_"+pos).toString().split("-")[0]);
			//Se iteran las cirugias
			for(int j=0;j<numCirugias;j++)
			{
				String consultaInsert = "INSERT INTO det_materiales_qx (codigo,numero_solicitud,articulo,servicio,cantidad,estado,usuario_modifica,fecha_modifica,hora_modifica) VALUES ";
				String consultaUpdate = "UPDATE det_materiales_qx SET cantidad = ?,estado = ?, usuario_modifica = ?, fecha_modifica = CURRENT_DATE, hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" WHERE codigo = ? ";
				String consultaDelete = "DELETE FROM det_materiales_qx WHERE codigo = ? ";
				String consulta = "";
				String codigoConsumo = "";
				String consCx = cirugias.get("consecutivo_"+j).toString();
				
				int codigoServicio = Integer.parseInt(cirugias.get("codigo_"+j).toString());
				String[] numerosPedidos = {""};
				if(articulos.get("numeros_pedidos_"+pos)!=null)
				{
					numerosPedidos = articulos.get("numeros_pedidos_"+pos).toString().split(",");
				}
				int cantidad = Utilidades.convertirAEntero(articulos.get("consumoActual"+consCx+"_"+pos)+"", true);
				PreparedStatementDecorator pst = null;
				
				
				//Se consulta el codigo del consumo existente solo si ya existe en la base de datos
				if(UtilidadTexto.getBoolean(articulos.get("estaBd_"+pos).toString()))
					codigoConsumo = getCodigoConsumoPendiente(con, numeroSolicitud, codigoArticulo, codigoServicio);
				
				//Caso en el que se guardará el consumo como terminado pero no hay pendientes o  no existe en la base de datos
				//Se hace un insert
				if(codigoConsumo.equals("")||!UtilidadTexto.getBoolean(articulos.get("estaBd_"+pos).toString()))
				{
					//Solo se inserta si la cantidad es mayor que 0
					if(cantidad>0)
					{
						codigoConsumo  = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_materiales_qx") + "";
						consulta = consultaInsert + " ("+codigoConsumo+",?,?,?,?,?,?,current_date,"+ValoresPorDefecto.getSentenciaHoraActualBD()+") ";
						pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						pst.setObject(1,numeroSolicitud);
						pst.setInt(2,codigoArticulo);
						pst.setInt(3,codigoServicio);
						pst.setInt(4,cantidad);
						pst.setString(5,terminado?ConstantesIntegridadDominio.acronimoEstadoFinalizado:ConstantesIntegridadDominio.acronimoEstadoPendiente);
						pst.setString(6,usuario);
						if(pst.executeUpdate()<=0)
							errores.add("", new ActionMessage("errors.ingresoDatos","el consumo del artículo "+articulos.get("articulo_"+pos)+" para el Serv. "+consCx));
					}
				}
				//Caso en el que ya existe un consumo pendiente y se desea terminar o seguir dejando pendiente
				else
				{
					//Si hay cantidad registrada es una modificación
					if(cantidad>0)
					{
						pst =  new PreparedStatementDecorator(con.prepareStatement(consultaUpdate, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						pst.setInt(1,cantidad);
						pst.setString(2,terminado?ConstantesIntegridadDominio.acronimoEstadoFinalizado:ConstantesIntegridadDominio.acronimoEstadoPendiente);
						pst.setString(3,usuario);
						pst.setObject(4,codigoConsumo);
						if(pst.executeUpdate()<=0)
							errores.add("", new ActionMessage("errors.noPudoActualizar","el consumo del artículo "+articulos.get("articulo_"+pos)+" para el Serv. "+consCx+". Problemas en la base de datos"));
					}
					//Si no se deja cantidad se elimina el consumo
					else
					{
						pst =  new PreparedStatementDecorator(con.prepareStatement(consultaDelete, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						pst.setObject(1,codigoConsumo);
						if(pst.executeUpdate()<=0)
							errores.add("", new ActionMessage("errors.problemasGenericos","al eliminar el consumo del artículo "+articulos.get("articulo_"+pos)+" para el Serv. "+consCx));
					}
				}
				
				
				
				//***************REGISTRO DE LOS PEDIDOS DEL ARTÍCULO****************************************************
				if(cantidad>0)
				{
					for(int k=0;k<numerosPedidos.length;k++)
					{
						int numeroPedido = Utilidades.convertirAEntero(numerosPedidos[k]);
						if(numeroPedido>0)
						{
							pst =  new PreparedStatementDecorator(con.prepareStatement(insertarPedidoConsumoStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
							pst.setInt(1,Integer.parseInt(codigoConsumo));
							pst.setInt(2,numeroPedido);
							if(pst.executeUpdate()<=0)
								errores.add("", new ActionMessage("errors.problemasGenericos","al registrar el pedido N°"+numeroPedido+" del artículo "+articulos.get("articulo_"+pos)+" para el Serv. "+consCx));
						}
					}
				}
				//******************************************************************************************************************
				
				sumCantidad+=cantidad;
				
			}
			
			///Si el consumo es terminado se hace la validacion de transaccion por almacen
			if(sumCantidad>0&&terminado&&generarPedidoxConsumo)
			{
				boolean esValido = esArticuloValidoTransaccionAlmacen(con, codigoArticulo, codigoFarmacia, tipoTransaccionPedido, codigoInstitucion);
				if(!esValido)
				{
					articulos.put("validacionTransAlmacen_"+pos,ConstantesBD.acronimoSi);
					errores.add("", new ActionMessage("errors.noExiste2","parametrización de transacciones válidas x centro de costo para el artículo "+codigoArticulo+" y la farmacia seleccionada. Proceso cancelado"));
				}
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en guardarConsumoArticuloPorCirugia: "+e);
			errores.add("",new ActionMessage("errors.problemasGenericos","al guardar el detalle del consumo"));
		}
		
		return errores;
	}

	/**
	 * Método implementado para guardar los consumos por acto
	 * @param con
	 * @param articulos
	 * @param pos
	 * @param numeroSolicitud
	 * @param errores
	 * @param terminado 
	 * @param usuario 
	 * @param codigoInstitucion 
	 * @param tipoTransaccionPedido 
	 * @param codigoFarmacia 
	 * @param generarPedidoxConsumo 
	 * @return
	 */
	private static ActionErrors guardarConsumoArticuloPorActo(Connection con, HashMap articulos, int pos, String numeroSolicitud, ActionErrors errores, boolean terminado, String usuario, int codigoFarmacia, int tipoTransaccionPedido, int codigoInstitucion, boolean generarPedidoxConsumo) 
	{
		try
		{
			String consultaInsert = "INSERT INTO det_materiales_qx (codigo,numero_solicitud,articulo,servicio,cantidad,estado,usuario_modifica,fecha_modifica,hora_modifica) VALUES ";
			String consultaUpdate = "UPDATE det_materiales_qx SET cantidad = ?,estado = ?, usuario_modifica = ?, fecha_modifica = CURRENT_DATE, hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+" WHERE codigo = ? ";
			String consultaDelete = "DELETE FROM det_materiales_qx WHERE codigo = ? ";
			String consulta = "";
			String codigoConsumo = "";
			int codigoArticulo = Integer.parseInt(articulos.get("articulo_"+pos).toString().split("-")[0]);
			int cantidad = Utilidades.convertirAEntero(articulos.get("total_"+pos)+"", true);
			String[] numerosPedidos = {""};
			if(articulos.get("numeros_pedidos_"+pos)!=null)
			{
				numerosPedidos = articulos.get("numeros_pedidos_"+pos).toString().split(",");
			}
			PreparedStatementDecorator pst = null;
			
			
			//Se consulta el codigo del consumo existente solo si ya existe en la base de datos
			if(UtilidadTexto.getBoolean(articulos.get("estaBd_"+pos).toString()))
				codigoConsumo = getCodigoConsumoPendiente(con, numeroSolicitud, codigoArticulo, 0);
			
			//Caso en el que se guardará el consumo pero no hay pendientes o  no existe en la base de datos
			//Se hace un insert
			if(codigoConsumo.equals("")||!UtilidadTexto.getBoolean(articulos.get("estaBd_"+pos).toString()))
			{
				//Solo se inserta si la cantidad es mayor que 0
				if(cantidad>0)
				{
					codigoConsumo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_materiales_qx")+"";
					consulta = consultaInsert + " ("+codigoConsumo+",?,?,?,?,?,?,current_date,"+ValoresPorDefecto.getSentenciaHoraActualBD()+") ";
					pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					pst.setObject(1,numeroSolicitud);
					pst.setInt(2,codigoArticulo);
					pst.setNull(3,Types.INTEGER);
					pst.setInt(4,cantidad);
					pst.setString(5,terminado?ConstantesIntegridadDominio.acronimoEstadoFinalizado:ConstantesIntegridadDominio.acronimoEstadoPendiente);
					pst.setString(6,usuario);
					if(pst.executeUpdate()<=0)
						errores.add("", new ActionMessage("errors.ingresoDatos","el consumo del artículo "+articulos.get("articulo_"+pos)));
				}
			}
			//Caso en el que ya existe un consumo pendiente y se desea terminar o seguir dejando pendiente
			else
			{
				//Si hay cantidad registrada es una modificación
				if(cantidad>0)
				{
					pst =  new PreparedStatementDecorator(con.prepareStatement(consultaUpdate, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst.setInt(1,cantidad);
					pst.setString(2,terminado?ConstantesIntegridadDominio.acronimoEstadoFinalizado:ConstantesIntegridadDominio.acronimoEstadoPendiente);
					pst.setString(3,usuario);
					pst.setObject(4,codigoConsumo);
					if(pst.executeUpdate()<=0)
						errores.add("", new ActionMessage("errors.noPudoActualizar","el consumo del artículo "+articulos.get("articulo_"+pos)+". Problemas en la base de datos"));
				}
				//Si no se deja cantidad se elimina el consumo
				else
				{
					pst =  new PreparedStatementDecorator(con.prepareStatement(consultaDelete, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst.setObject(1,codigoConsumo);
					if(pst.executeUpdate()<=0)
						errores.add("", new ActionMessage("errors.problemasGenericos","al eliminar el consumo del artículo "+articulos.get("articulo_"+pos)));
				}
			}
			
			//Si el consumo es terminado se hace la validacion de transaccion por almacen
			if(cantidad>0&&terminado&&generarPedidoxConsumo)
			{
				boolean esValido = esArticuloValidoTransaccionAlmacen(con, codigoArticulo, codigoFarmacia, tipoTransaccionPedido, codigoInstitucion);
				if(!esValido)
				{
					articulos.put("validacionTransAlmacen_"+pos,ConstantesBD.acronimoSi);
					errores.add("", new ActionMessage("errors.noExiste2","parametrización de transacciones válidas x centro de costo para el artículo "+codigoArticulo+" y la farmacia seleccionada. Proceso cancelado"));
				}
			}
			
			///***************REGISTRO DE LOS PEDIDOS DEL ARTÍCULO****************************************************
			if(cantidad>0)
			{
				for(int k=0;k<numerosPedidos.length;k++)
				{
					int numeroPedido = Utilidades.convertirAEntero(numerosPedidos[k]);
					if(numeroPedido>0)
					{
						pst =  new PreparedStatementDecorator(con.prepareStatement(insertarPedidoConsumoStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						pst.setInt(1,Integer.parseInt(codigoConsumo));
						pst.setInt(2,numeroPedido);
						if(pst.executeUpdate()<=0)
							errores.add("", new ActionMessage("errors.problemasGenericos","al registrar el pedido N°"+numeroPedido+" del artículo "+articulos.get("articulo_"+pos)+". Proceso cancelado "));
					}
				}
			}
			//******************************************************************************************************************
			
			
		}
		catch(SQLException e)
		{
			logger.error("Error en guardarConsumoArticuloPorActo: "+e);
			errores.add("",new ActionMessage("errors.problemasGenericos","al guardar el detalle del consumo"));
		}
		return errores;
	}

	/**
	 * Método que consulta el código del consumo pendiente de un artículo y/o servicio
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoArticulo
	 * @param codigoServicio
	 * @return
	 */
	private static String getCodigoConsumoPendiente(Connection con,String numeroSolicitud,int codigoArticulo,int codigoServicio)
	{
		String resultado = "";
		try
		{
			String consulta = getCodigoConsumoPendienteStr;
			
			if(codigoServicio>0)
				consulta += " AND servicio = "+codigoServicio;
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setObject(1,numeroSolicitud);
			pst.setInt(2,codigoArticulo);
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				resultado = rs.getString("codigo");
			
		}
		catch(SQLException e)
		{
			logger.error("Error en getCodigoConsumoPendiente: "+e);
		}
		return resultado;
	}
	
	/**
	 * Método que verifica si existe hoja de anestesia finalizada
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static boolean existeHojaAnestesiaSinFinalizar(Connection con,String numeroSolicitud)
	{
		boolean resultado = false;
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(existeHojaAnestesiaFinalizadaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			
			pst.setInt(1,Integer.parseInt(numeroSolicitud));
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
				if(rs.getInt("cuenta")>0)
					resultado = true;
		}
		catch(SQLException e)
		{
			logger.error("Error en existeHojaAnestesiaFinalizada: "+e);
		}
		return resultado;
	}
	
	/**
	 * Método que realiza la reversión de la finalización del consumo
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int reversionFinalizacionConsumo(Connection con,HashMap campos)
	{
		int resultado = 0;
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(reversionFinalizacionConsumoStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setObject(1, campos.get("usuario"));
			pst.setObject(2, campos.get("numeroSolicitud"));
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.info("Error reversionFinalizacionConsumo: "+e);
		}
		return resultado;
	}
	//************************************************************************************************************
	//*************MÉTODOS PARA EL REGISTRO AUTOMÁTICO DE CONSUMO MATERIALES**************************************
	/**
	 * Método que verifica si existe consumo de materiales finalizado
	 */
	public static boolean existeConsumoMateriales(Connection con,String numeroSolicitud,boolean finalizado)
	{
		boolean existe = false;
		try
		{
			String consulta = "SELECT coalesce(finalizado,'') as finalizado FROM materiales_qx WHERE numero_solicitud = "+numeroSolicitud;
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			ResultSetDecorator rs = new ResultSetDecorator(st.executeQuery(consulta));
			
			
			if(rs.next())
			{
				existe = true;
				
				//Como ya se sabe que existe consumo de materiale se verifica
				//que en el caso que se deseara verificar si está finalizado que
				//se encuentre finalizado
				if(finalizado&&!UtilidadTexto.getBoolean(rs.getString("finalizado")))
					existe = false; //entonces no existe como finalizado
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en existeConsumoMaterialesFinalizado: "+e);
		}
		return existe;
	}
	
	/**
	 * Método para insertar el consumo de un artículo de modo automático
	 * @param con
	 * @param campos
	 * @return
	 */
	public static int insertarConsumoArticuloAutomatico(Connection con,HashMap campos)
	{
		int resp = 0;
		try
		{
			//***********SE TOMAN LOS PARÁMETROS***********************************
			int numeroSolicitud = Integer.parseInt(campos.get("numeroSolicitud").toString());
			int codigoArticulo = Integer.parseInt(campos.get("codigoArticulo").toString());
			int cantidad = Integer.parseInt(campos.get("cantidad").toString());
			String usuario = campos.get("usuario").toString();
			//********************************************************************
			
			String consulta = "INSERT INTO det_materiales_qx (codigo,numero_solicitud,articulo,servicio,cantidad,estado,usuario_modifica,fecha_modifica,hora_modifica) VALUES (?,?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+")";
			int secuencia = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_materiales_qx");
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,secuencia);
			pst.setInt(2,numeroSolicitud);
			pst.setInt(3,codigoArticulo);
			pst.setNull(4,Types.INTEGER);
			pst.setInt(5,cantidad);
			pst.setString(6,ConstantesIntegridadDominio.acronimoEstadoFinalizado);
			pst.setString(7,usuario);
			
			if(pst.executeUpdate()>0)
				return secuencia;
		}
		catch(SQLException e)
		{
			logger.error("Error en insertarConsumoArticuloAutomatico: "+e);
		}
		return resp;
	}
	//************************************************************************************************************
	
	/**
	 * Método para consultar la cantidad del consumo del articulo de una peticion
	 * Si retorna -1 quiere decir que el artículo no tiene consumo
	 */
	public static int consultarCantidadConsumoArticuloPeticion(Connection con,HashMap campos)
	{
		int cantidadConsumo = ConstantesBD.codigoNuncaValido;
		int cantidadDespacho = 0;
		try
		{
			//*********SE TOMAN LOS PARÁMETROS****************************************
			int codigoPeticion = Utilidades.convertirAEntero(campos.get("codigoPeticion").toString());
			int codigoArticulo = Utilidades.convertirAEntero(campos.get("codigoArticulo").toString());
			//*************************************************************************
			
			String consulta = "SELECT " +
				"coalesce(sum(dm.cantidad),"+ConstantesBD.codigoNuncaValido+") as cantidad_consumo " +
				"from solicitudes_cirugia sc " +
				"inner join det_materiales_qx dm on(dm.numero_solicitud=sc.numero_solicitud) " +
				"WHERE sc.codigo_peticion = ? and dm.articulo = ?";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoPeticion);
			pst.setInt(2,codigoArticulo);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				cantidadConsumo = rs.getInt("cantidad_consumo");
				
				if(cantidadConsumo!=ConstantesBD.codigoNuncaValido)
				{
					//Se debe consultar la cantidad de despachos que se hayan realizado para ese articulo
					consulta = "SELECT " +
						"coalesce(sum(ddp.cantidad),0) as cantidad_despacho " +
						"from pedidos_peticiones_qx ppq " +
						"inner join despacho_pedido dp on(dp.pedido = ppq.pedido) " +
						"inner join detalle_despacho_pedido ddp on (ddp.pedido = dp.pedido) " +
						"WHERE ppq.peticion = ? and ddp.articulo = ?";
					pst =  new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
					pst.setInt(1,codigoPeticion);
					pst.setInt(2,codigoArticulo);
					ResultSetDecorator rs2 = new ResultSetDecorator(pst.executeQuery());
					if(rs2.next())
						cantidadDespacho = rs2.getInt("cantidad_despacho");
					
					cantidadConsumo = cantidadConsumo -cantidadDespacho;
				}
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarCantidadConsumoArticuloPeticion: "+e);
		}
		return cantidadConsumo;
	}
	
	/**
	 * Método para obtener las farmacias de los pedidos de la peticion
	 * @param con
	 * @param codigoPeticion
	 * @return
	 */
	public static String obtenerFarmaciasPedidosPeticion(Connection con,String codigoPeticion)
	{
		String nombresFarmacias = "";
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(obtenerFarmaciasPedidosPeticionStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
			pst.setInt(1,Integer.parseInt(codigoPeticion));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				nombresFarmacias += (nombresFarmacias.equals("")?"":", ") + rs.getString("farmacia");
			}
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerFarmaciasPedidosPeticion: "+e);
		}
		return nombresFarmacias;
	}
	
	
}
