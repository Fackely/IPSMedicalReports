/*
 * Creado el Jun 30, 2006
 * por Julian Montoya
 */
package com.princetonsa.dao.sqlbase.capitacion;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

public class SqlBaseAprobacionAjustesDao {

	
	/**
	 * Para manejar las excepciones de la clase.
	 */
	public static Logger logger = Logger.getLogger(SqlBaseExcepcionNivelDao.class);
	
	
	/**
	 * Metodo Para Cargar Informacion Parametrizada.
	 * @param con
	 * @param mapaParam
	 * @return
	 */
	public static HashMap cargarInformacion(Connection con, HashMap mapaParam)
	{
		StringBuffer consulta = new StringBuffer("");
		int nroConsulta = 0;
		
		//-Verificar el tipo de consulta.
		if (UtilidadCadena.noEsVacio(mapaParam.get("nroConsulta")+"")) { nroConsulta = Integer.parseInt(mapaParam.get("nroConsulta")+""); }
		else { return (new HashMap()); }
		
		//------------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------------
		//---- Consultar los Tipos de Ajustes (Debito Y Credito).
		if ( nroConsulta ==  0 )
		{
			consulta.append("	SELECT ta.codigo AS cod_ajuste, ta.descripcion AS nom_ajuste 	" +
							"		   FROM tipos_ajuste ta 									" +
							"				WHERE ta.codigo IN (" + ConstantesBD.codigoConceptosCarteraDebito + "," + ConstantesBD.codigoConceptosCarteraCredito + ")				" +
							"			  ORDER BY ta.descripcion");
		}
		//------------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------------
		//--- Consultar los codigos y los convenios.
		if ( nroConsulta ==  1 ) 
		{	
			consulta.append("	SELECT c.codigo AS cod_convenio, c.nombre AS nom_convenio																	" +
							 "		   FROM convenios c																										" +
							 "				WHERE c.codigo IN (	SELECT vcont.convenio 																		" +	
							 "										   FROM view_contratos_vigentes vcont 													" +
							 "												INNER JOIN convenios conv ON (conv.codigo=vcont.convenio)					 	" +
							 "													 WHERE conv.tipo_contrato != "+ConstantesBD.codigoTipoContratoEvento+
							 "													   AND conv.institucion = "+ mapaParam.get("codigoInstitucion"));
							 
		 if ( UtilidadTexto.getBoolean(mapaParam.get("vigentes")+"") )  
			 {
			 	consulta.append("													   AND vcont.es_vigente = " + ValoresPorDefecto.getValorTrueParaConsultas());
			 }
			 consulta.append("													   	   GROUP BY vcont.convenio												" +
							 "							   	   )																							" +
							 "			  ORDER BY c.nombre");
		}
		//------------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------------
		//---- Traer un listado de Ajustes. De Acuerdo a unos parametros de Busqueda.
		if ( nroConsulta ==  2 ) 
		{	

			consulta.append("	SELECT ac.codigo as codigo, ac.consecutivo AS consecutivo, ac.tipo_ajuste as cod_tipo_ajuste, ta.descripcion AS tipo_ajuste,  to_char(ac.fecha, 'DD/MM/YYYY')  as fecha_ajuste,											" +
							 "		   ac.valor as valor_total, ac.cuenta_cobro as cuenta_cobro, c.nombre as convenio 													" +
							 "		   FROM ajustes_cxc ac																												" +
							 "				INNER JOIN cuentas_cobro_capitacion ccc ON (ccc.institucion=ac.institucion AND ccc.numero_cuenta_cobro = ac.cuenta_cobro )  " +
							 "				INNER JOIN convenios c ON ( c.codigo = ccc.convenio )			" +
							 "				INNER JOIN tipos_ajuste ta ON ( ta.codigo = ac.tipo_ajuste )	" +
							 "					 WHERE ac.institucion = " +  mapaParam.get("codigoInstitucion")  +
							 "					   AND ac.estado = " + ConstantesBD.codigoEstadoAjusteCxCPendiente ); 
			
			
			
			//--- Adicionar los parametros de Busqueda.
			if ( UtilidadCadena.noEsVacio(mapaParam.get("tipoAjuste")+"") && Integer.parseInt(mapaParam.get("tipoAjuste")+"") != 0 ) 
			{
				consulta.append("  AND ac.tipo_ajuste = " + mapaParam.get("tipoAjuste"));
			}
			if ( UtilidadCadena.noEsVacio(mapaParam.get("nroAjuste")+"") && Integer.parseInt(mapaParam.get("nroAjuste")+"") != 0 )  
			{
				consulta.append("  AND ac.consecutivo = " + mapaParam.get("nroAjuste"));
			}
			if ( UtilidadCadena.noEsVacio(mapaParam.get("fechaAjuste")+"") ) 
			{
				consulta.append("  AND to_char(ac.fecha,'yyyy-mm-dd') = '" + UtilidadFecha.conversionFormatoFechaABD(""+mapaParam.get("fechaAjuste")) + "'");
			}
			if ( UtilidadCadena.noEsVacio(mapaParam.get("cuentaCobro")+"") && Integer.parseInt(mapaParam.get("cuentaCobro")+"") != 0 )  
			{
				consulta.append("  AND ac.cuenta_cobro = " + mapaParam.get("cuentaCobro"));
			}
			if ( UtilidadCadena.noEsVacio(mapaParam.get("convenio")+"") && Integer.parseInt(mapaParam.get("convenio")+"") != 0 ) 
			{
				consulta.append("  AND c.codigo = " + mapaParam.get("convenio"));
			}
		}
		
		//------------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------------
		//----- Traer la información de un ajustes especifico.
		if ( nroConsulta ==  3 ) 
		{	
			consulta.append("	SELECT ac.codigo as d_codigo_ajuste, ac.consecutivo AS d_consecutivo, to_char(ac.fecha,'yyyy-mm-dd') as d_fecha_ajuste, c.nombre as d_convenio, ac.cuenta_cobro as d_cuenta_cobro, 	"  +
							 "		   ac.valor as d_valor_total, ac.tipo_ajuste as d_cod_tipo_ajuste, ta.codigo as d_cod_tipo_ajuste, ta.descripcion AS d_nom_tipo_ajuste,   					" +
  							 " 		   cac.descripcion as d_concepto, ac.observaciones as d_observaciones, ea.nombre as d_estado_ajuste	" +
							 "		   FROM ajustes_cxc ac																												" +
							 "				INNER JOIN cuentas_cobro_capitacion ccc ON (ccc.institucion=ac.institucion AND ccc.numero_cuenta_cobro = ac.cuenta_cobro )  " +
							 "				INNER JOIN concepto_ajustes_cartera cac ON (cac.institucion=ac.institucion AND cac.codigo = ac.concepto )  " +
							 "				INNER JOIN convenios c ON ( c.codigo = ccc.convenio )			" +
							 "				INNER JOIN tipos_ajuste ta ON ( ta.codigo = ac.tipo_ajuste )	" +
							 "				INNER JOIN estados_ajuste ea ON ( ea.codigo = ac.estado )	" +
							 "					 WHERE ac.institucion  = " + mapaParam.get("codigoInstitucion") +
							 "					   AND ac.codigo = " + mapaParam.get("nroCodigoAjuste")); 
		}

		//------------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------------
		//----- Traer la información de los cargues de una cuenta de cobro especifica.
		if ( nroConsulta ==  4 ) 
		{	
			consulta.append("	SELECT cc.contrato as contrato,  													" +
							"		   to_char(cc.fecha_inicial, 'DD/MM/YYYY') as fecha_inicial, 					" +
							"		   to_char(cc.fecha_final, 'DD/MM/YYYY') as fecha_final,						" +
							"		   ajc.valor as valor_ajuste, cac.descripcion as concepto 						" +
							"		   FROM ajustes_cxc ac   														" +	 
						   	"				INNER JOIN ajustes_cargue ajc ON ( ac.codigo = ajc.ajuste ) 			" +	   
						   	"				INNER JOIN contrato_cargue cc ON ( cc.codigo = ajc.cargue )				" +
						   	"				INNER JOIN concepto_ajustes_cartera cac ON (cac.institucion=ac.institucion AND cac.codigo = ac.concepto )  " +	
							"					 WHERE ac.institucion = " + mapaParam.get("codigoInstitucion") +
							"					   AND ac.codigo = " + mapaParam.get("nroCodigoAjuste") +
 							"					   AND ac.cuenta_cobro = " + mapaParam.get("nroCuentaCobro") + 
 							"						   ORDER BY cc.contrato"); 
		}
		//------------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------------
		//----- Traer los tipos de estados de un ajuste 
		if ( nroConsulta ==  5 ) 
		{	
			consulta.append("	SELECT codigo as codigo_ta, nombre as nombre_ta " +
							"		   FROM estados_ajuste						" +
							"				ORDER BY nombre					 	"); 
		}
		//------------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------------
		//----- Consultar los conceptos de ajustes parametrizados en el sistema.
		if ( nroConsulta ==  6 ) 
		{	
			consulta.append("	SELECT cac.codigo as codigo_concepto, cac.descripcion as descripcion_concepto, ta.descripcion as tipo_concepto	" +
							"		   FROM concepto_ajustes_cartera cac					 													" +
							"				INNER JOIN tipos_ajuste	ta ON ( cac.naturaleza = ta.codigo ) 										" +
							"					  WHERE cac.institucion = " + mapaParam.get("codigoInstitucion") +
							"						AND cac.tipo_cartera IN (" + ConstantesBD.codigoTipoCarteraCapitacion+ "," + ConstantesBD.codigoTipoCarteraTodos + ")" + 
							"							ORDER BY ta.descripcion DESC, cac.descripcion											");
		}

		//------------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------------
		//---- Traer un listado de Ajustes. De Acuerdo a unos parametros de Busqueda. DESDE LA FUNCIONALIDAD CONSULTAR AJUSTES. 
		if ( nroConsulta ==  7 ) 
		{	
			

			consulta.append( "	SELECT ta.codigo as cod_tipo_ajuste,  ta.descripcion AS tipo_ajuste, ac.consecutivo as consecutivo,								" +
							 " 		   ea.nombre as estado_ajuste,  																							" +
							 "		   ac.estado as codigo_estado_ajuste, 																						" +
							 "		   to_char(ac.fecha, 'DD/MM/YYYY') as fecha_ajuste, 																		" +
							 "		   to_char(ac.fecha_grabacion, 'DD/MM/YYYY') ||' -- '|| ac.hora_grabacion as fecha_elaboracion, ac.usuario as usuario_elaboracion,										" +
							 "		   ac.valor as valor_total,																									" +
							 "		   vcv.numero_contrato as codigo_contrato, ac.cuenta_cobro as cuenta_cobro, 												" +
							 "		   c.nombre as convenio, ac.codigo as codigo_ajustes,																		" +
							 "		   CASE WHEN acc.fecha_aprobacion IS NULL THEN '' ELSE to_char(acc.fecha_aprobacion, 'DD/MM/YYYY') END AS fecha_aprobacion,	" +
							 "		   CASE WHEN acc.usuario IS NULL THEN '' ELSE acc.usuario END AS usuario_aprobacion							" +
							 "		   FROM ajustes_cxc ac																						" +
							 "				INNER JOIN cuentas_cobro_capitacion ccc ON (ccc.institucion=ac.institucion AND ccc.numero_cuenta_cobro = ac.cuenta_cobro )  " +
							 "				INNER JOIN view_contratos_vigentes vcv ON (vcv.convenio=ccc.convenio AND vcv.es_vigente = " + ValoresPorDefecto.getValorTrueParaConsultas() + ")   " +
							 "				INNER JOIN contrato_cargue ca ON (ca.cuenta_cobro=ac.cuenta_cobro and ca.contrato=vcv.codigo )  " +
							 "				INNER JOIN convenios c ON ( c.codigo = ccc.convenio )										" +
							 "				INNER JOIN tipos_ajuste ta ON ( ta.codigo = ac.tipo_ajuste )								" +
							 "				INNER JOIN estados_ajuste ea ON ( ea.codigo = ac.estado )									" +
							 "				LEFT OUTER JOIN aprobacion_ajustes_cxc acc ON ( ac.codigo = acc.codigo_ajuste  )			" +
							 "					 WHERE ac.institucion = " +  mapaParam.get("codigoInstitucion"));
			
							 //"					   AND ccc.estado = " + ConstantesBD.codigoEstadoCarteraGenerado  ); 
			

			//--- Adicionar los parametros de Busqueda.
			if ( UtilidadCadena.noEsVacio(mapaParam.get("tipoAjuste")+"") && Integer.parseInt(mapaParam.get("tipoAjuste")+"") != 0 ) 
			{
				consulta.append("  AND ac.tipo_ajuste = " + mapaParam.get("tipoAjuste"));
			}
			if ( UtilidadCadena.noEsVacio(mapaParam.get("estadoAjuste")+"") && Integer.parseInt(mapaParam.get("estadoAjuste")+"") != 0 ) 
			{
				consulta.append("  AND ac.estado = " + mapaParam.get("estadoAjuste"));
			}
			
			//-- Se Valida solo el consecutivo inicial ( porque la se debe escribir el final cunado se da el inicial en el jsp de la Busqueda)  
			if ( 
				( UtilidadCadena.noEsVacio(mapaParam.get("nroAjusteIni")+"") && Integer.parseInt(mapaParam.get("nroAjusteIni")+"") > 0 ) 
				 && 
				( UtilidadCadena.noEsVacio(mapaParam.get("nroAjusteFin")+"") && Integer.parseInt(mapaParam.get("nroAjusteFin")+"") > 0 )
			   )	
			{
				consulta.append("  AND (ac.consecutivo >= " + mapaParam.get("nroAjusteIni") + " AND ac.consecutivo <= " + mapaParam.get("nroAjusteFin") + " ) " );
			}			
			
			if ( UtilidadCadena.noEsVacio(mapaParam.get("fechaIniElab")+"") && UtilidadCadena.noEsVacio(mapaParam.get("fechaFinElab")+"") ) 
			{
				consulta.append("  AND (to_char(ac.fecha,'yyyy-mm-dd') >= '" + UtilidadFecha.conversionFormatoFechaABD(""+mapaParam.get("fechaIniElab")) + "'" 
							  + "  AND to_char(ac.fecha,'yyyy-mm-dd') <= '" + UtilidadFecha.conversionFormatoFechaABD(""+mapaParam.get("fechaFinElab")) + "')");
			}
			if ( UtilidadCadena.noEsVacio(mapaParam.get("cuentaCobro")+"") && Integer.parseInt(mapaParam.get("cuentaCobro")+"") != 0 )  
			{
				consulta.append("  AND ac.cuenta_cobro = " + mapaParam.get("cuentaCobro"));
			}
			if ( UtilidadCadena.noEsVacio(mapaParam.get("convenio")+"") && Integer.parseInt(mapaParam.get("convenio")+"") != 0 ) 
			{
				consulta.append("  AND c.codigo = " + mapaParam.get("convenio"));
			}

		  	//-- Ordenar los registros.  				
			consulta.append("  ORDER BY ta.descripcion DESC, ac.consecutivo ");
		}

		//------------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------------
		//---- Verificar que el Ajuste Exista pero no esta en estado PENDIENTE. 
		if ( nroConsulta ==  8 ) 
		{	
			consulta.append( "	SELECT ea.nombre as aux_estado_ajuste	" +
							 "		   FROM ajustes_cxc ac																						" +
							 "				INNER JOIN estados_ajuste ea ON ( ea.codigo = ac.estado )											" +
							 "					 WHERE ac.institucion = " +  mapaParam.get("codigoInstitucion")  +
							 "					   AND ac.tipo_ajuste = " +	mapaParam.get("tipoAjuste") +
							 "					   AND ac.consecutivo = " +	mapaParam.get("nroAjuste"));

		}	

		//------------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------------
		//---- Consultar el cierre inicial cartera. 
		if ( nroConsulta ==  9 ) 
		{	
			consulta.append( "	SELECT cs.anio_cierre ||'-'|| cs.mes_cierre	as fecha_cierre						" +
							 "		   FROM cierres_saldos_capitacion cs													" +
							 "				WHERE cs.institucion = " +  mapaParam.get("codigoInstitucion") +
							 "				  AND cs.tipo_cierre = '" + ConstantesBD.codigoTipoCierreSaldoInicialStr + "'");

		}	

		//------------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------------
		//---- Verificar  que exista la información de los cargues asociadas al ajuste. 
		if ( nroConsulta ==  10 ) 
		{	
			consulta.append( "	SELECT count(1)	as cantidad_cargues														" +
							 "		   FROM ajustes_cargue ac															" +
							 "				WHERE ac.ajuste = " + mapaParam.get("codigoAjuste") );
		}	

		//------------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------------
		//-- Verificar que los cargues de los ajustes sean menor igual al saldo del cargue.   
		
		if ( nroConsulta ==  11 ) 
		{	
			consulta.append( "	SELECT count(1) as cantidad																			  " +
							 "		   FROM (  SELECT (cc.valor_total - ac.valor) as suma											  " +
							 "					  	  FROM ajustes_cargue ac										 				  " +			
							 "						       INNER JOIN contrato_cargue cc ON  ( cc.codigo = ac.cargue )  			  " +
							 "						   	   INNER JOIN ajustes_cxc acc ON  ( acc.cuenta_cobro = cc.cuenta_cobro )	  " +	
							 "						   		    WHERE acc.tipo_ajuste = " + ConstantesBD.codigoConceptosCarteraCredito +
							 "								  	  AND acc.codigo = " +  mapaParam.get("codigoAjuste") +
							 "									  AND acc.institucion = " +  mapaParam.get("codigoInstitucion") +
							 "	   		   	 ) x WHERE suma < 0  ");
		}	

		//------------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------------
		//-- Totalizar el valor de cada cargue para el ajuste especifico.
		
		if ( nroConsulta ==  12 ) 
		{	
			consulta.append( "	SELECT sum (valor) as valorajuste		" +
							 "		   FROM ajustes_cargue 		 	    " +
							 "				WHERE ajuste = " +  mapaParam.get("codigoAjuste"));
		}	

		//------------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------------
		//-- Listar los cargues de los ajustes, para modificar la tabla contrato_cargue.   
		
		if ( nroConsulta ==  13 ) 
		{	
			consulta.append( "	 SELECT ac.cargue, ac.valor													" +
							 "			FROM ajustes_cargue ac												" +			
							 "				 INNER JOIN ajustes_cxc acc ON  ( ac.ajuste = acc.codigo )		" +
							 "					  WHERE acc.codigo = " +  mapaParam.get("codigoAjuste") +
							 "					    AND acc.institucion = " +  mapaParam.get("codigoInstitucion") );
		}	

		//------------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------------
		//-- Verificar  que una cuenta de cobro capitacion este en estado generada para poder modificar el valor_inicial_cuenta.   
		
		if ( nroConsulta ==  14 ) 
		{	
			consulta.append( mapaParam.get("consulta") + "");
		}	

		//------------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------------
		//----- Traer la información de un ajustes especifico.
		
		if ( nroConsulta ==  15 ) 
		{	
			consulta.append(" SELECT ac.cuenta_cobro as d_cuenta_cobro, c.nombre as d_convenio, ac.consecutivo AS d_consecutivo,			     	 				   " +
 							"		 ta.descripcion AS d_nom_tipo_ajuste, ea.nombre as d_estado_ajuste,																   " +
							"		 ac.fecha as d_fecha_elaboracion, ac.usuario as d_usuario_elaboracion,														 	   " +
							"		 CASE WHEN aa.fecha_aprobacion IS NULL THEN '' ELSE to_char (aa.fecha_aprobacion, 'DD/MM/YYYY') END as d_fecha_aprobacion," +
							"		 CASE WHEN aa.usuario IS NULL THEN '' ELSE aa.usuario END as d_usuario_aprobacion,													   " +
							"		 ac.observaciones as d_observaciones, ac.valor as d_valor_total																							   " +
							"		 FROM ajustes_cxc ac																											   " +
							"			  LEFT OUTER JOIN aprobacion_ajustes_cxc aa ON ( ac.codigo = aa.codigo_ajuste )													   " +
							"			  INNER JOIN concepto_ajustes_cartera cac ON (cac.institucion=ac.institucion AND cac.codigo = ac.concepto ) 				   " +
							"			  INNER JOIN cuentas_cobro_capitacion ccc ON (ccc.institucion=ac.institucion AND ccc.numero_cuenta_cobro = ac.cuenta_cobro )   " +
							"			  INNER JOIN convenios c ON ( c.codigo = ccc.convenio )			" +
							"			  INNER JOIN tipos_ajuste ta ON ( ta.codigo = ac.tipo_ajuste )	" +
							"			  INNER JOIN estados_ajuste ea ON ( ea.codigo = ac.estado )		" +
							"				   WHERE ac.institucion  = " + mapaParam.get("codigoInstitucion") +
							"				     AND ac.codigo = " + mapaParam.get("nroCodigoAjuste")); 
		}

		//------------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------------
		//----- Información de detalle de los cargues para un ajuste especifico.
		
		if ( nroConsulta ==  16 ) 
		{	
			consulta.append("SELECT cc.codigo AS codigo, con.numero_contrato AS contrato,													   " +
							"		to_char(cc.fecha_inicial, 'dd/mm/yyyy') || ' - ' || to_char(cc.fecha_final, 'dd/mm/yyyy') AS fecha_cargue, " +
							"		cc.valor_total + ABS(cc.ajustes_debito) - ABS(cc.ajustes_credito) AS saldo, ac.valor AS valor, 						   " +
							"		conc.descripcion AS nombre_concepto, conc.codigo AS concepto 											   " +
							"		FROM capitacion.contrato_cargue cc 																		   " +
							"			 INNER JOIN contratos con ON(con.codigo=cc.contrato)												   " +
							"			 INNER JOIN capitacion.cuentas_cobro_capitacion cob ON(cob.numero_cuenta_cobro=cc.cuenta_cobro AND cob.institucion=cc.institucion) " +
							"	 		 INNER JOIN capitacion.ajustes_cargue ac ON(ac.cargue=cc.codigo) 									   " +
							"			 INNER JOIN cartera.concepto_ajustes_cartera conc ON (conc.codigo=ac.concepto AND conc.institucion=ac.institucion) " +
							"				  WHERE ac.ajuste = " +  mapaParam.get("nroCodigoAjuste") + 
							"			  			ORDER BY ac.cargue, ac.concepto	");
		}

		//------------------------------------------------------------------------------------------------------------------------------------------
		//------------------------------------------------------------------------------------------------------------------------------------------
		PreparedStatementDecorator pst =  null;
		try
			{
				logger.info(consulta.toString());
				pst= new PreparedStatementDecorator(con.prepareStatement(consulta.toString()));
				
				HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
				
				return mapaRetorno;
			}
		catch (SQLException e)
			{
				logger.error("Error (SqlBaseAprobacionAjustesDao) consultando en (cargarInformacion)" ,e);
				return null;
			}finally{
				try{
					if(pst!=null){
						pst.close();					
					}
				}catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAprobacionAjustesDao "+sqlException.toString() );
				}
			}

	}

	//------------------------------------------------------------------------------------------------------------------------------------------
	//------------------------------------------------------------------------------------------------------------------------------------------
	/**
	 * Metodo para Aprobar el Ajuste Seleccionado.
	 * @param con
	 * @param mapaParam
	 * @return
	 * @throws SQLException 
	 */
	public static int aprobarAjuste(Connection con, HashMap mapaParam)  
	{
		
		String cadInsecion = " INSERT INTO capitacion.aprobacion_ajustes_cxc (codigo_ajuste, fecha_aprobacion, fecha_registro, hora_registro, institucion, usuario, estado_ajuste) " +
							 "			   VALUES ( ?, ?, ?, ?, ?, ?, ? ) ";

		int codigoAjuste = 0, codigoCuentaCobro = 0, institucion = 0, codigoTipoAjuste = 0;
		float valorAjuste = 0;

		if ( UtilidadCadena.noEsVacio(mapaParam.get("codigoAjuste")+"") ) 		{ codigoAjuste = Integer.parseInt(mapaParam.get("codigoAjuste")+""); }	
		if ( UtilidadCadena.noEsVacio(mapaParam.get("codigoCuentaCobro")+"") ) 	{ codigoCuentaCobro = Integer.parseInt(mapaParam.get("codigoCuentaCobro")+""); }
		if ( UtilidadCadena.noEsVacio(mapaParam.get("valorAjuste")+"") ) 		{ valorAjuste = Float.parseFloat(mapaParam.get("valorAjuste")+""); }
		if ( UtilidadCadena.noEsVacio(mapaParam.get("institucion")+"") ) 		{ institucion = Integer.parseInt(mapaParam.get("institucion")+""); }
		if ( UtilidadCadena.noEsVacio(mapaParam.get("codigoTipoAjuste")+"") ) { codigoTipoAjuste = Integer.parseInt(mapaParam.get("codigoTipoAjuste")+""); }
		PreparedStatementDecorator st =  null;
		try {
			
			//-Primero se deben hacer las actualizaciones Respectiva en Otras Tablas Antes 
			//-de registrar la aprobacion del ajuste.
			
			//-Primero Modificar el estado del ajuste en ajustes_cxc
			cadInsecion = " UPDATE capitacion.ajustes_cxc SET estado = " + ConstantesBD.codigoEstadoAjusteCxCAprobado + "  WHERE codigo = ? ";
			int vec [] = { codigoAjuste };
			
			if (UtilidadBD.updateGenerico(con, vec, cadInsecion) == 1)
			{
				//-Verificar Si el Valor del Ajuste es diferente de cero para actulizar los valores de Ajuste Cargue y Cartera.  
				if ( valorAjuste > 0 )
				{
					//-- Actulizar las demas tablas en el sistema en el momento de Aprobar el Ajuste.
					int re = actualizarCuentasCobroCapitacion(con, codigoAjuste, codigoCuentaCobro, institucion, codigoTipoAjuste);
					int res = actualizarContratoCargue(con, codigoAjuste, institucion, codigoTipoAjuste);
					
					
					if ( (re <= 0) || (res <= 0) )
					{
						return -1;
					}
					
				}
					
				cadInsecion = " INSERT INTO aprobacion_ajustes_cxc (codigo_ajuste, fecha_aprobacion, fecha_registro, hora_registro, institucion, usuario) " +
							  "			    VALUES ( ?, ?, current_date, "+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+", ?, ? ) ";

				st =  new PreparedStatementDecorator(con.prepareStatement(cadInsecion));
				st.setDouble(1, Utilidades.convertirADouble(mapaParam.get("codigoAjuste")+""));
				st.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD( mapaParam.get("fechaAprobacion")+"")));
				st.setInt(3, institucion);
				st.setString(4, mapaParam.get("usuario")+"");

				return st.executeUpdate();
			}
			else
			{
				return -1;
			}
		} 
		catch (SQLException e)
		{
			return -1;
		}finally{
			try{
				if(st!=null){
					st.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAprobacionAjustesDao "+sqlException.toString() );
			}
		}
	}

	
	/**
	 * Metodo para actualizar el ajuste.  
	 * @param con
	 * @param codigoAjuste
	 * @param codigoCuentaCobro
	 * @param institucion
	 * @param codigoTipoAjuste
	 * @return
	 * @throws SQLException 
	 */
	private static int actualizarContratoCargue(Connection con, int codigoAjuste, int institucion, int codigoTipoAjuste) throws SQLException
	{
		String cad = "";
		HashMap mp = new HashMap();
		
		
		//-- Consultar la informacion de cargues del Ajuste.
		mp.put("nroConsulta", "13");
		mp.put("codigoAjuste", codigoAjuste);
		mp.put("codigoInstitucion", institucion);
		mp = cargarInformacion(con, mp);
		PreparedStatementDecorator ps = null;
		try {
			
			if ( UtilidadCadena.noEsVacio(mp.get("numRegistros")+"")   )
			{
				int nroRows = Integer.parseInt(mp.get("numRegistros")+""); 
				
				String cad1 = "";
				if ( codigoTipoAjuste == ConstantesBD.codigoConceptosCarteraCredito )
				{
					cad1 = " ajustes_credito = ( ajustes_credito + ? )";
				}
				else
				{
					cad1 = " ajustes_debito = ( ajustes_debito + ? ) ";
				}
				
				//-Actualizar todos los cargues. que esten relacionados en el Ajuste.
				for (int i = 0; i < nroRows; i++) 
				{
					cad = " UPDATE contrato_cargue SET " + cad1 + 
							" 	   WHERE codigo = ? 			" + 
							"			 AND institucion = ? 		";
					
					int cargue = Utilidades.convertirAEntero(mp.get("cargue_"+i)+"");
					double valor = Utilidades.convertirADouble(mp.get("valor_"+ i)+"");
					
					
					ps =  new PreparedStatementDecorator(con.prepareStatement(cad));
					ps.setDouble(1, valor);
					ps.setDouble(2, Utilidades.convertirADouble(cargue+""));
					ps.setInt(3, institucion);
					if ( ps.executeUpdate() <= 0 )
					{
						return -1;
					}
				}	
				
				return nroRows; 
			}
			else
			{
				return -1; 
			}
		} catch (SQLException e) {
			throw e ;
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAprobacionAjustesDao "+sqlException.toString() );
			}
		}
		
		
	}	

	/**
	 * Metodo para actualizar las tabla del sistema que tienen relacion con la aprobacion del Ajuste. 
	 * @param con 
	 * @param codigoAjuste
	 * @param codigoTipoAjuste 
	 * @return
	 * @throws SQLException 
	 */
	private static int actualizarCuentasCobroCapitacion(Connection con, int codigoAjuste, int codigoCuentaCobro, int institucion, int codigoTipoAjuste) throws SQLException
	{
		String cad = "";
		HashMap mp = new HashMap();
		int resp = 0;
		
		
		//-- Traer la suma de los cargues y el tipo de Ajuste (debito o credito).
		mp.put("nroConsulta", "12");     			
		mp.put("codigoAjuste", "" + codigoAjuste);
		mp = cargarInformacion(con, mp);			
		
		if ( UtilidadCadena.noEsVacio(mp.get("numRegistros")+"") && (Integer.parseInt(mp.get("numRegistros")+"") > 0) )
		{
			//------------------------------------------------------------------------------------------------
			//------------------------- Actulizar LA TABLA CUENTAS_COBRO -------------------------------------
			//------------------------------------------------------------------------------------------------
			
			if ( codigoTipoAjuste == ConstantesBD.codigoConceptosCarteraCredito )
			{
				cad = " UPDATE cuentas_cobro_capitacion SET saldo_cuenta = saldo_cuenta - " + mp.get("valorajuste_0") +
					  " 	   WHERE numero_cuenta_cobro = ? 				   " + 
					  "			 AND institucion = ? 						   ";
			}
			else
			{
				cad = " UPDATE cuentas_cobro_capitacion SET saldo_cuenta = saldo_cuenta + " + mp.get("valorajuste_0") + 
					  " 	   WHERE numero_cuenta_cobro = ? 				   " + 
					  "			 AND institucion = ? 						   ";
			}
			
			int llaves [] = { codigoCuentaCobro, institucion };
			resp = UtilidadBD.updateGenerico(con, llaves, cad);
			
			if ( resp <= 0 )
			{
				return resp;
			}
			else
			{
				//---Actualizar la tabla de cuentas de Cobro. el Ajuste Credito y Debito.
				String cad1 = "";
				if ( codigoTipoAjuste == ConstantesBD.codigoConceptosCarteraCredito )
				{
					cad1 = " ajustes_credito = ( ajustes_credito + " + mp.get("valorajuste_0") + " )";
				}
				else
				{
					cad1 = " ajustes_debito = ( ajustes_debito +  " + mp.get("valorajuste_0") + " )";
				}
			
				cad = " UPDATE cuentas_cobro_capitacion SET " + cad1  + 
					  " 	   WHERE numero_cuenta_cobro = ?  		" + 
					  "			 AND institucion = ? 		  		";
			
					
				return UtilidadBD.updateGenerico(con, llaves, cad);
			}
		}
		else
		{
			return -1;
		}
	}
	
	
	
}
