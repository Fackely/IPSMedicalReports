/**
 * Author: Juan Sebastian Castaño
 */

package com.princetonsa.dao.sqlbase.facturasVarias;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.consultaExterna.DtoMultasCitas;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.mundo.fabrica.facturacion.FacturacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.facturacion.IHistoricoEncabezadoMundo;
import com.servinte.axioma.orm.HistoricoEncabezado;
import com.servinte.axioma.persistencia.UtilidadTransaccion;

public class SqlBaseGenModFacturasVariasDao {

	public static Logger logger = Logger
			.getLogger(SqlBaseGenModFacturasVariasDao.class);

	private static final String queryModificacionFacturasVarias = "UPDATE facturas_varias SET"
			+
			// " codigo_fac_var = ?," +
			" estado_factura = ?,"
			+ " centro_atencion = ?,"
			+ " centro_costo = ?,"
			+ " fecha = ?,"
			+ " concepto = ?,"
			+ " valor_factura = ?,"
			+ " deudor = ?,"
			+ " observaciones = ?,"
			+ " usuario_modifica = ?,"
			+ " fecha_modifica = CURRENT_DATE,"
			+ " hora_modifica = "
			+ ValoresPorDefecto.getSentenciaHoraActualBD()
			+ ""
			+ "WHERE "
			+ " consecutivo = ? and codigo_fac_var = ? and institucion = ? ";
	
	/**
	 * Cadena para eliminar las multas de la factura varia
	 */
	private static final String eliminarMultasFacturaStr = "DELETE FROM multas_facturas_varias WHERE factura_varia = ?";

	private static final String queryInsercionFacturasVarias = "INSERT INTO facturas_varias ( "
			+ " consecutivo, "
			+ " estado_factura,"
			+ " centro_atencion,"
			+ " centro_costo,"
			+ " fecha,"
			+ " concepto,"
			+ " valor_factura,"
			+ " deudor,"
			+ " observaciones,"
			+ " usuario_modifica,"
			+ " fecha_modifica,"
			+ " hora_modifica,"
			+ " institucion,"
			+ " codigo_fac_var,"
			+ " historico_encabezado"
			+ " ) "
			+ " VALUES "
			+ "(?,?,?,?,?,?,?,?,?,?,CURRENT_DATE,"
			+ ValoresPorDefecto.getSentenciaHoraActualBD() + ",?,?,?)";

	private static final String strConsultaFacturasVarias = 
		" SELECT " +
			" fv.consecutivo As consecutivo, " +
			" fv.estado_factura As consecutivo_factura," +
			" fv.centro_atencion As centro_atencion," +
			" fv.centro_costo As centro_costo ," +
			" to_char(fv.fecha, 'DD/MM/YYYY') As fecha," +
			" fv.concepto As concepto," +
			" fv.valor_factura As valor_factura," +
			" fv.deudor As deudor," +
			" fv.observaciones As observaciones," +
			" fv.codigo_fac_var As codigo_fac_var, " +
			" cfv.tipo_concepto as tipo_concepto, " +
			" cfv.descripcion as nombre_concepto, " +
			" to_char(fv.fecha_aprobacion,'"+ConstantesBD.formatoFechaAp+"') as fecha_aprobacion," +
			" d.tipo AS tipo_deudor " +
			" FROM facturas_varias fv  "
			+ " INNER JOIN conceptos_facturas_varias cfv ON(cfv.consecutivo = fv.concepto) " +
			"   INNER JOIN deudores d ON (d.codigo=fv.deudor) "
			+ " WHERE fv.institucion=? AND fv.codigo_fac_var=? " +

			"";

	public static final String indicesDeudorOtros[] = { "codigo_",
			"numeroIdentificacion_", "descripcion_", "direccion_", "telefono_",
			"email_", "repreLegal_", "nomContacto_" };

	public static final String indicesDeudorEmpresa[] = { "razonSocial_",
			"direccion_", "telefono_", "email_", "nomRepresentante_",
			"nomContacto_", "nit_" };

	// consultar todas las facturas varias por los campos parametrisados
	public static final String queryBusquedaFacturasVarias = "SELECT "
			+ " fv.consecutivo AS consecutivo,"
			+ " fv.codigo_fac_var AS codigo_fac_var,"
			+ " fv.estado_factura AS estado_factura,"
			+ " fv.centro_atencion AS centro_atencion,"
			+ " fv.centro_costo AS centro_costo,"
			+ " to_char(fv.fecha,'dd/mm/yyyy') AS fecha,"
			+ " fv.concepto AS concepto,"
			+ " cfv.tipo_concepto AS tipo_concepto,"
			+ " fv.valor_factura AS valor_factura,"
			+ " fv.deudor AS deudor,"
			+
			// consultar informacion referente al deudor asociado a esta
			// factura, enviar el codigo y el tipo de deudor
			// " getInformacionTipoDeudor(fv.deudor,d.tipo,fv.institucion) AS info_deudor_asociado,"
			// +
			" fv.observaciones AS observaciones, "
			+ " getnomcentroatencion(fv.centro_atencion)  As nom_centro_atencion, "
			+ " getnomcentrocosto(fv.centro_costo) As  nom_centro_costo,"
			+ " getdescripciondeudor(fv.deudor,d.tipo) As nom_deudor, "
			+ " getdescconceptofacvar(fv.concepto) As nom_concepto, "
			+ " convertirletras(fv.valor_factura) AS valorletras, " +
			"   TO_CHAR(fv.fecha_aprobacion,'"+ConstantesBD.formatoFechaAp+"') as fecha_aprobacion, "
			+ " d.tipo as tipo_deudor "
			+ " FROM facturas_varias fv "
			+ " INNER JOIN deudores d on(d.codigo = fv.deudor) "
			+ " INNER JOIN conceptos_facturas_varias cfv ON(cfv.consecutivo = fv.concepto)  "
			+ " WHERE ";

	public static final String indicesBusquedaFacVarias[] = { "consecutivo_",
			"codigoFacVar_", "estadoFactura_", "centroAtencion_",
			"tipoConcepto_", "centroCosto_", "fecha_", "concepto_",
			"valorFactura_", "deudor_", "observaciones_", "nomCentroCosto_",
			"nomCentroAtencion_", "nomDeudor_", "tipoDeudor_", "nomConcepto_",
			"valorLetras_","fechaAprobacion_" };

	/**
	 * Cadena para obtener las multas asociadas al paciente
	 */
	private static final String obtenerMultasPacienteStr = "SELECT " +
			" distinct c.codigo as codigocita, "
			+ "mc.consecutivo as codigo_multa, "
			+ "to_char(mc.fecha_generacion,'"
			+ ConstantesBD.formatoFechaAp
			+ "') as fecha_generacion, "
			+ "mc.valor as valor, "
			+ "getnombreunidadconsulta(a.unidad_consulta) as unidad_agenda, "
			+ "a.fecha as fecha_cita, "
			+ "a.hora_inicio as hora_cita, "
			+ "coalesce(getnombrepersona(a.codigo_medico),'') as profesional "
			+ "FROM cita c "
			+ "INNER join multas_citas mc on(mc.cita = c.codigo) "
			+ "INNER JOIN agenda a ON(a.codigo = c.codigo_agenda) "
			+ "LEFT OUTER JOIN multas_facturas_varias mfv ON(mfv.multa_cita = mc.consecutivo) "
			+ "LEFT OUTER JOIN facturas_varias fv ON(fv.codigo_fac_var = mfv.factura_varia) "
			+ "WHERE " + "c.codigo_paciente = ? AND " + "mc.estado = '"
			+ ConstantesIntegridadDominio.acronimoEstadoGenerado + "' AND "
			+ "("
			+ " SELECT COUNT(1) AS facturas "
			+	" FROM facturas_varias fvi "
			+	" INNER JOIN multas_facturas_varias mfvi "
			+		" ON(fvi.codigo_fac_var=mfvi.factura_varia) "
			+	" WHERE fvi.codigo_fac_var IS NOT NULL "
			+	" AND fvi.estado_factura      = '"+ConstantesIntegridadDominio.acronimoEstadoGenerado + "' "
			+	" AND mfvi.multa_cita=mfv.multa_cita" 
			+ ") = 0 "
			+ "ORDER BY fecha_cita,hora_cita";

	/**
	 * Cadena para obtener las multas de la factura
	 */
	private static final String obtenerMultasFacturaStr = "SELECT "
			+ "mc.consecutivo as codigo_multa, "
			+ "to_char(mc.fecha_generacion,'"
			+ ConstantesBD.formatoFechaAp
			+ "') as fecha_generacion, "
			+ "mc.valor as valor, "
			+ "getnombreunidadconsulta(a.unidad_consulta) as unidad_agenda, "
			+ "a.fecha as fecha_cita, "
			+ "a.hora_inicio as hora_cita, "
			+ "coalesce(getnombrepersona(a.codigo_medico),'') as profesional "
			+ "FROM multas_facturas_varias mfv "
			+ "INNER JOIN multas_citas mc on (mc.consecutivo = mfv.multa_cita) "
			+ "INNER JOIN cita c ON(c.codigo = mc.cita) "
			+ "INNER JOIN agenda a ON(a.codigo = c.codigo_agenda) " + "WHERE "
			+ "mfv.factura_varia = ? " + "ORDER BY fecha_cita,hora_cita";

	/**
	 * Metodo encargado de consultar la informacion de las facturas varias.
	 * 
	 * @param connection
	 * @param codigoFactura
	 * @param institucion
	 * @return
	 */
	public static HashMap buscarFacturasVarias(Connection connection,
			int codigoFactura, String institucion) {
		HashMap result = new HashMap();

		String cadena = strConsultaFacturasVarias;
		try {
			PreparedStatementDecorator ps = new PreparedStatementDecorator(
					connection.prepareStatement(cadena,
							ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			ps.setObject(1, institucion);
			ps.setInt(2, codigoFactura);

			result = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps
					.executeQuery()), false, true);

		} catch (SQLException e) {
			logger
					.info("\n problema consultando los datos de la factura varia "
							, e);
		}

		return result;
	}

	/**
	 * metodo para la consulta de los datos del asocio del deudor, osea la
	 * empresa o el tercero
	 * 
	 * @param con
	 * @param tipoDeudor
	 * @param codigoDeudor
	 * @param institucion
	 * @return
	 */
	public static HashMap<String, Object> cargarAsocioDeudor(Connection con,
			String tipoDeudor, int codigoDeudor, int institucion) {
		logger.info("\n entro a cargarAsocioDeudor tipoDeudor -->" + tipoDeudor
				+ "  codigoDeudor -->" + codigoDeudor + "  institucion-->"
				+ institucion);

		HashMap<String, Object> resultados = new HashMap<String, Object>();
		PreparedStatementDecorator pst = null;
		String consulta = "";

		try {

			// verificar que tipo de deudor es
			if (tipoDeudor
					.equals(ConstantesIntegridadDominio.acronimoTipoDeudorEmpresa)) {
				consulta = "SELECT "
						+ " e.razon_social as razon_social,"
						+ " e.direccion as direccion,"
						+ " e.telefono as telefono,"
						+ " e.email as email,"
						+ " e.nombre_representante as nom_representante,"
						+ " facturacion.getNombreContacto(e.codigo) as nom_contacto,"
						+ " t.numero_identificacion as nit,"
						+ " t.codigo As codigo, "
						+ " d.codigo_tercero As codigo_deudor "
						+ " FROM "
						+ " empresas  e "
						+ " INNER JOIN terceros  t  ON (e.tercero = t.codigo) "
						+ " LEFT OUTER JOIN  deudores d ON (d.codigo_tercero = t.codigo) "
						+ " WHERE e.codigo = ? and t.institucion = ? ";

				logger.info("\n cadena --> " + consulta);
				pst = new PreparedStatementDecorator(con.prepareStatement(
						consulta, ConstantesBD.typeResultSet,
						ConstantesBD.concurrencyResultSet));
				pst.setInt(1, codigoDeudor);
				pst.setInt(2, institucion);

				// ejecutar consulta y cargar resultados en el hashMap
				resultados = UtilidadBD.cargarValueObject(
						new ResultSetDecorator(pst.executeQuery()), true, true);

				resultados.put("INDICES", indicesDeudorEmpresa);

			} else if (tipoDeudor
					.equals(ConstantesIntegridadDominio.acronimoTipoDeudorOtros)) {
				consulta = "SELECT " + " t.codigo as codigo,"
						+ " t.numero_identificacion as numero_identificacion,"
						+ " t.descripcion as descripcion,"
						+ " d.direccion as direccion,"
						+ " d.telefono as telefono," + " d.e_mail as email,"
						+ " d.representante_legal as repre_legal,"
						+ " d.nombre_contacto as nom_contacto,"
						+ " d.codigo_tercero As codigo_deudor " + " FROM "
						+ " terceros t " + " LEFT OUTER JOIN " + " deudores  d"
						+ " ON (d.codigo_tercero = t.codigo) "
						+ " WHERE t.codigo = ? and t.institucion = ? ";

				logger.info("\n cadena otros --> " + consulta);
				pst = new PreparedStatementDecorator(con.prepareStatement(
						consulta, ConstantesBD.typeResultSet,
						ConstantesBD.concurrencyResultSet));
				pst.setInt(1, codigoDeudor);
				pst.setInt(2, institucion);

				// ejecutar consulta y cargar resultados en el hashMap
				resultados = UtilidadBD.cargarValueObject(
						new ResultSetDecorator(pst.executeQuery()), true, true);

				resultados.put("INDICES", indicesDeudorOtros);

			}

		} catch (SQLException e) {
			logger
					.info("Error en la funcion de carga de asociado deudor en el archivo SqlBaseGenModFacturasVariasDao "
							+ e);
		}

		return resultados;
	}

	/**
	 * Metodo de insercion de un nuevo registro de facturas varias
	 * 
	 * @param con
	 * @param consecutivo
	 * @param estadoFactura
	 * @param fecha
	 * @param concepto
	 * @param valorFactura
	 * @param deudor
	 * @param observaciones
	 * @param codigoFacturaVaria TODO
	 * @param historicoEncabezado 
	 * @param codigoFacVar
	 * @param centro_atencion
	 * @param centro_costo
	 * @param tipoDeudor
	 * @return
	 */
	public static ResultadoBoolean insertarFacturaVaria(Connection con,
			int consecutivo, String estadoFactura, int centroAtencion,
			int centroCosto, Date fecha, int concepto, double valorFactura,
			int deudor, String observaciones, String usuario, int institucion,
			ArrayList<DtoMultasCitas> multasFactura, int codigoFacturaVaria, HistoricoEncabezado historicoEncabezado) {
		ResultadoBoolean resultado = new ResultadoBoolean(true, "");
		
		
		
		logger.info("\n\n\n\n\n\n\n\n\n\n\n  entre a  INSERTAR FACTURAS VARIAS : "
						+ "\n consecutivo-->"
						+ consecutivo
						+ "  \n  "
						+ estadoFactura
						+ "  \n  "
						+ centroAtencion
						+ "  \n  "
						+ centroCosto
						+ "  \n  "
						+ fecha
						+ "  \n  "
						+ concepto
						+ "  \n  "
						+ valorFactura
						+ "  \n  "
						+ deudor
						+ "  \n  "
						+ observaciones
						+ "  \n  "
						+ usuario
						+ "  \n  "
						+ institucion
						+ "\n\n\n\n\n\n\n\n\n\n\n");

		PreparedStatementDecorator pst = null;

		try {
			
			UtilidadTransaccion.getTransaccion().begin();
			
			IHistoricoEncabezadoMundo historicoEncabezadoMundo = FacturacionFabricaMundo.crearHistoricoEncabezadoMundo();
			
			historicoEncabezadoMundo.insertar(historicoEncabezado);
			
			UtilidadTransaccion.getTransaccion().commit();
			
			pst = new PreparedStatementDecorator(con, queryInsercionFacturasVarias);

			/**
			 * INSERT INTO facturas_varias ( " + " consecutivo, " +
			 * " estado_factura," + " centro_atencion," + " centro_costo," +
			 * " fecha," + " concepto," + " valor_factura," " deudor," +
			 * " observaciones," + " usuario_modifica," + " fecha_modifica," +
			 * " hora_modifica," + " institucion,"+ "codigo_fac_var" + " ) " +
			 * " VALUES " +
			 * "(?,?,?,?,?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto
			 * .getSentenciaHoraActualBD()+",?,?)
			 */

			pst.setDouble(1, Utilidades.convertirADouble(consecutivo + ""));
			pst.setString(2, estadoFactura);
			pst.setInt(3, centroAtencion);
			pst.setInt(4, centroCosto);
			pst.setDate(5, fecha);
			pst.setDouble(6, Utilidades.convertirADouble(concepto + ""));
			pst.setDouble(7, valorFactura);
			pst.setInt(8, deudor);
			pst.setString(9, observaciones);
			pst.setString(10, usuario);
			pst.setInt(11, institucion);
			pst.setInt(12, codigoFacturaVaria);
			
			pst.setLong(13, historicoEncabezado.getCodigoPk());
			
			// ejecutar el update
			logger.info("\n\n\n\n\n\n*************************************************************************************************");
			logger.info(pst);
			logger.info("\n\n\n\n\n\n*************************************************************************************************");
			if (pst.executeUpdate() <= 0) {
				resultado.setResultado(false);
				resultado
						.setDescripcion("Ocurrió error tratando de registrar la información de la factura");
			} else {
				resultado.setDescripcion(codigoFacturaVaria + "");
				String consulta = "INSERT INTO multas_facturas_varias ("
						+ "codigo,"
						+ // 1
						"factura_varia,"
						+ // 2
						"multa_cita,"
						+ // 3
						"fecha_modifica," + "hora_modifica,"
						+ "usuario_modifica) "
						+ // 4
						"values " + "(?,?,?,current_date,"
						+ ValoresPorDefecto.getSentenciaHoraActualBD() + ",?)";

				// *************INSERCION DE LAS MULTAS DE LA
				// FACTURA******************************
				for (DtoMultasCitas multa : multasFactura) {
					if (resultado.isTrue() && multa.isSeleccionado()) {
						pst.close();

						pst = new PreparedStatementDecorator(con
								.prepareStatement(consulta,
										ConstantesBD.typeResultSet,
										ConstantesBD.concurrencyResultSet));
						pst.setInt(1, UtilidadBD
								.obtenerSiguienteValorSecuencia(con,
										"seq_multas_facturas_varias"));
						pst.setInt(2, codigoFacturaVaria);
						pst.setInt(3, multa.getCodigoMulta());
						pst.setString(4, usuario);

						if (pst.executeUpdate() <= 0) {
							
							resultado.setResultado(false);
							resultado.setDescripcion("Ocurrió error tratando de registrar la multa de la factura: "+ consecutivo);
						}
					}
				}
				//**************************************************************
				// ****************
			}
			pst.close();

		} catch (SQLException e) {
			
			logger.info("Error en la funcion de insercion de una factura varia en el archivo SqlBaseGenModFacturasVariasDao ", e);
			resultado.setResultado(false);
			resultado.setDescripcion("Error realizando la inserción de la factura: "+ e);
			
			eliminarHistoricoEncabezado(historicoEncabezado);
		}
		
		return resultado;
	}

	
	/**
	 * Método que elimina los Historicos Encabezados asociados a la
	 * Factura.
	 * 
	 * @param listaFacturas
	 */
	private static void eliminarHistoricoEncabezado(HistoricoEncabezado historicoEncabezado) {
		
		IHistoricoEncabezadoMundo historicoEncabezadoMundo = FacturacionFabricaMundo.crearHistoricoEncabezadoMundo();
	
		HibernateUtil.beginTransaction();
		
		if(historicoEncabezado!=null && historicoEncabezado.getCodigoPk() > 0){
			
			historicoEncabezadoMundo.eliminar(historicoEncabezado);
		}
		
		HibernateUtil.endTransaction();
	}
	
	/**
	 * Metodo de consulta de facturas varias
	 * 
	 * @param con
	 * @param noFactura
	 * @param centroAtencion
	 * @param tipoDeudor
	 * @param deudor
	 * @param fechaInicial
	 * @param fechaFinal
	 * @return
	 */
	public static HashMap<String, Object> buscarFacturasVarias(Connection con,	String noFactura, int centroAtencion, String tipoDeudor,
			int deudor, String fechaInicial, String fechaFinal, int institucion) 
	{
	
		String consulta = queryBusquedaFacturasVarias;
		boolean tieneMasDeUnFiltro = false;
		PreparedStatementDecorator pst = null;
		String and = "";
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		// verificar cuales fueron los campos parametrizados
		if (!noFactura.equals("")) 
		{
			consulta = consulta + " fv.consecutivo = '" + noFactura + "' ";
			tieneMasDeUnFiltro = true;
		}
		if (centroAtencion > 0) 
		{
			if (tieneMasDeUnFiltro) 
			{
				and = " and ";
			} 
			else
				
				tieneMasDeUnFiltro = true;

			consulta = consulta + and + " fv.centro_atencion = "+ centroAtencion + " ";
		}

		if (deudor > 0) 
		{
			if (tieneMasDeUnFiltro) 
			{
				and = " and ";
			} 
			else
				tieneMasDeUnFiltro = true;

			consulta = consulta + and + " fv.deudor = " + deudor + " ";
		}
		
		if (!fechaInicial.equals("") && !fechaFinal.equals("")) 
		{
			if (tieneMasDeUnFiltro) 
			{
				and = " and ";
			}
			else
				tieneMasDeUnFiltro = true;
			
			consulta = consulta + and + " to_char(fv.fecha, 'YYYY-MM-DD') BETWEEN '"
									+ UtilidadFecha.conversionFormatoFechaABD(fechaInicial)
									+ "' AND '"
									+ UtilidadFecha.conversionFormatoFechaABD(fechaFinal)
									+ "' ";
		}

		if (tieneMasDeUnFiltro)
			and = " and ";
		else
			tieneMasDeUnFiltro = true;
		consulta = consulta + and + " fv.estado_factura = '"
				+ ConstantesIntegridadDominio.acronimoEstadoFacturaGenerada
				+ "' ";

		if (!tipoDeudor.equals("")) {
			
			if (tieneMasDeUnFiltro) 
			{
				and = " and ";
			} else
				tieneMasDeUnFiltro = true;

			consulta = consulta + and + " d.tipo = '" + tipoDeudor + "' ";
		}

		if (tieneMasDeUnFiltro)
			and = " and ";
		
		consulta = consulta + and + " fv.institucion = " + institucion + " ";
		consulta += " ORDER BY fv.consecutivo";
		logger.info("\n\n\n\n\n\n\n\n\n\n\n\n\n Consulta : " + consulta
				+ " \n\n\n\n\n\n\n\n\n\n\n\n\n");

		try {
			pst = new PreparedStatementDecorator(con.prepareStatement(consulta,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet));

			// ejecutar consulta y cargar resultados en el hashMap
			resultados = UtilidadBD.cargarValueObject(new ResultSetDecorator(
					pst.executeQuery()), true, true);

			resultados.put("INDICES", indicesBusquedaFacVarias);
		} catch (SQLException e) {
			logger
					.info("Error en la funcion de consulta de facturas varias en el archivo SqlBaseGenModFacturasVariasDao "
							+ e);
		}
		return resultados;
	}

	/**
	 * Metodo de modificacion de facturas varias
	 * 
	 * @param con
	 * @param consecutivo
	 * @param codigoFacVar
	 * @param estadoFactura
	 * @param centroAtencion
	 * @param centroCosto
	 * @param fecha
	 * @param concepto
	 * @param valorFactura
	 * @param tipoDeudor
	 * @param deudor
	 * @param observaciones
	 * @param usuario
	 * @param institucion
	 * @return
	 */
	public static boolean guardarModFacturaVaria(Connection con,
			int consecutivo, String codigoFacVar, String estadoFactura,
			int centroAtencion, int centroCosto, Date fecha, int concepto,
			double valorFactura,  int deudor,
			String observaciones, String usuario, int institucion, ArrayList<DtoMultasCitas> multasFactura) 
	{
		boolean resultado = false;
		logger.info("\n CONCEPTO >>>>>>>" + concepto);
		logger.info("\n CONSECUTIVO >>>>>>>" + consecutivo);
		logger.info("\n CODIGO FACTURA >>>>>>>" + codigoFacVar);

		logger.info("\n estadoFactura >>>>>>>" + estadoFactura);
		logger.info("\n centroAtencion >>>>>>>" + centroAtencion);
		logger.info("\n centroCosto >>>>>>>" + centroCosto);

		logger.info("\n fecha >>>>>>>" + fecha);
		logger.info("\n valorFactura >>>>>>>" + valorFactura);

		logger.info("\n deudor >>>>>>>" + deudor);
		logger.info("\n observaciones >>>>>>>" + observaciones);
		logger.info("\n usuario >>>>>>>" + usuario);
		logger.info("\n institucion >>>>>>>" + institucion);

		PreparedStatementDecorator pst = null;
		try {
			pst = new PreparedStatementDecorator(con.prepareStatement(
					queryModificacionFacturasVarias,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet));


			pst.setString(1, estadoFactura);
			pst.setInt(2, centroAtencion);
			pst.setInt(3, centroCosto);
			pst.setDate(4, fecha);
			pst.setDouble(5, Utilidades.convertirADouble(concepto + ""));
			pst.setDouble(6, valorFactura);
			pst.setInt(7, deudor);
			pst.setString(8, observaciones);
			pst.setString(9, usuario);

			pst.setDouble(10, Utilidades.convertirADouble(consecutivo + ""));
			pst.setDouble(11, Utilidades.convertirADouble(codigoFacVar));
			pst.setInt(12, institucion);

			// ejecutar el update
			if (pst.executeUpdate() > 0)
			{
				//*********SE ELIMINAN LAS MULTAS QUE LA FACTURA TENÍA ASOCIADA*************************
				pst.close();
				resultado = true;
				pst = new PreparedStatementDecorator(con.prepareStatement(eliminarMultasFacturaStr, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
				pst.setLong(1,Long.parseLong(codigoFacVar));
				pst.executeUpdate();
				//***********************************************************
				
				
				String consulta = "INSERT INTO multas_facturas_varias ("
					+ "codigo,"
					+ // 1
					"factura_varia,"
					+ // 2
					"multa_cita,"
					+ // 3
					"fecha_modifica," + "hora_modifica,"
					+ "usuario_modifica) "
					+ // 4
					"values " + "(?,?,?,current_date,"
					+ ValoresPorDefecto.getSentenciaHoraActualBD() + ",?)";
				pst.close();
				for(DtoMultasCitas multa:multasFactura)
				{
					if(multa.isSeleccionado()&&resultado)
					{
						pst = new PreparedStatementDecorator(con.prepareStatement(consulta, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						pst.setInt(1, UtilidadBD
								.obtenerSiguienteValorSecuencia(con,
										"seq_multas_facturas_varias"));
						pst.setLong(2,Long.parseLong(codigoFacVar));
						pst.setInt(3, multa.getCodigoMulta());
						pst.setString(4, usuario);

						if (pst.executeUpdate() <= 0) 
						{
							resultado = false;
						}
					}
				}
				
				
			}
				
		} catch (SQLException e) {
			logger
					.info("Error en la funcion de consulta de facturas varias en el archivo SqlBaseGenModFacturasVariasDao "
							+ e);
			resultado = false;
		}

		return resultado;
	}

	/**
	 * Metodo de consulta de conceptos de facturas varias para realizar el
	 * reporte
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static String consultarConceptosFacVarias(Connection con,
			int consecutivo) {
		PreparedStatementDecorator pst = null;
		String consulta = "SELECT codigo as codigo, descripcion as descripcion FROM conceptos_facturas_varias WHERE consecutivo = ?  ";
		ResultSetDecorator rst = null;
		String resultado = "";
		try {
			pst = new PreparedStatementDecorator(con.prepareStatement(consulta,
					ConstantesBD.typeResultSet,
					ConstantesBD.concurrencyResultSet));
			pst.setDouble(1, Utilidades.convertirADouble(consecutivo + ""));

			rst = new ResultSetDecorator(pst.executeQuery());

			if (rst.next())
				resultado = rst.getString(1) + " - " + rst.getString(2);
			else
				resultado = "No hay Registros";

		} catch (SQLException e) {
			logger
					.info("Error en la funcion de consulta de conceptos de facturas varias en el archivo SqlBaseGenModFacturasVariasDao "
							+ e);
		}

		return resultado;
	}

	/**
	 * Método para obtener las multas del paciente
	 * 
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<DtoMultasCitas> obtenerMultasPaciente(
			Connection con, HashMap campos) {
		ArrayList<DtoMultasCitas> arreglo = new ArrayList<DtoMultasCitas>();
		try {
			// ************SE TOMAN LOS PARÁMETROS*************************+
			int codigoPaciente = Utilidades.convertirAEntero(campos.get(
					"codigoPaciente").toString());
			// ***************************************************************

			PreparedStatementDecorator pst = new PreparedStatementDecorator(con
					.prepareStatement(obtenerMultasPacienteStr,
							ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoPaciente);
			logger.info(obtenerMultasPacienteStr+" cod pac "+codigoPaciente);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while (rs.next()) {
				DtoMultasCitas multa = new DtoMultasCitas();
				multa.setCita(rs.getInt("codigocita"));
				multa.setCodigoMulta(rs.getInt("codigo_multa"));
				multa.setFechaMulta(rs.getString("fecha_generacion"));
				multa.setValorString(rs.getString("valor"));
				multa.setDescripcionUnidadAgenda(rs.getString("unidad_agenda"));
				multa.setFechaCita(rs.getString("fecha_cita"));
				multa.setHoraCita(UtilidadFecha
						.convertirHoraACincoCaracteres(rs
								.getString("hora_cita")));
				multa.setProfesional(rs.getString("profesional"));
				multa.setSeleccionado(false);
				multa.setServiciosCita(obtenerServiciosCita(multa.getCita()));
				
				arreglo.add(multa);

			}
			pst.close();
			rs.close();
		} catch (SQLException e) {
			logger.error("Error en obtenerMultasPaciente: " + e);
		}
		return arreglo;
	}
	
	/**
	 * 
	 * @param codigoCita
	 * @return
	 */
	private static HashMap<Object, Object> obtenerServiciosCita(int codigoCita)
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		mapa.put("numRegistros", 0);
		
		String consultaServiciosCitaStr="SELECT " +
							  "codigo_cita AS codcita, " +
							  "servicio  AS codservicio, " +
							  "getnombreservicio(servicio,"+ConstantesBD.codigoTarifarioCups +") AS nombreservicio, " +
							  "getcodigopropservicio2(servicio,"+ConstantesBD.codigoTarifarioCups +") AS codigocups " +
							  "FROM servicios_cita " +
							  "WHERE codigo_cita = ? ";
		
		try
		{
			Connection con= UtilidadBD.abrirConexion();
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con.prepareStatement(consultaServiciosCitaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, codigoCita);
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
			pst.close();
			UtilidadBD.closeConnection(con);
		}
		catch (SQLException e) 
		{
			logger.error("Error en obtenerServiciosCita: " + e);
		}
		
		return mapa;
	}
	
	/**
	 * Método para obtener las multas de la factura
	 * 
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<DtoMultasCitas> obtenerMultasFactura(
			Connection con, HashMap campos) {
		ArrayList<DtoMultasCitas> arreglo = new ArrayList<DtoMultasCitas>();
		try {
			// *****************SE TOMAN LOS
			// PARÁMETROS********************************+
			long consecutivoFactura = Long.parseLong(campos.get(
					"consecutivoFactura").toString());
			//******************************************************************
			// *****

			PreparedStatementDecorator pst = new PreparedStatementDecorator(con
					.prepareStatement(obtenerMultasFacturaStr,
							ConstantesBD.typeResultSet,
							ConstantesBD.concurrencyResultSet));
			pst.setLong(1, consecutivoFactura);

			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while (rs.next()) {
				DtoMultasCitas multa = new DtoMultasCitas();
				multa.setCodigoMulta(rs.getInt("codigo_multa"));
				multa.setFechaMulta(rs.getString("fecha_generacion"));
				multa.setValorString(rs.getString("valor"));
				multa.setDescripcionUnidadAgenda(rs.getString("unidad_agenda"));
				multa.setFechaCita(rs.getString("fecha_cita"));
				multa.setHoraCita(UtilidadFecha
						.convertirHoraACincoCaracteres(rs
								.getString("hora_cita")));
				multa.setProfesional(rs.getString("profesional"));
				multa.setSeleccionado(true);

				arreglo.add(multa);
			}
			pst.close();
			rs.close();
		} catch (SQLException e) {
			logger.error("Error en obtenerMultasFactura: " + e);
		}
		return arreglo;
	}

}
