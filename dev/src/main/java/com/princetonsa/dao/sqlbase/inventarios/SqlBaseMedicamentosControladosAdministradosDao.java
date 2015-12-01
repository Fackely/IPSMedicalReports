package com.princetonsa.dao.sqlbase.inventarios;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;


public class SqlBaseMedicamentosControladosAdministradosDao {

	 // Manejador de logs de la clase
	private static Logger logger = Logger.getLogger(SqlBaseMedicamentosControladosAdministradosDao.class); 
	
	 // Cadena SELECT que consulta los Medicamentos Controlados Administrados
	private static String sqlSearchMedicamentosControladosAdministrados = ""+
		 "SELECT " +
		 	"am.usuario AS usuario, " + 
			"i.consecutivo AS numingreso, " + 
			"to_char(da.fecha, 'DD/MM/YYYY') AS fechaingresoadmin, " + 
			"da.hora AS horaingresoadmin, " +
			"s.centro_costo_solicitante AS codcentrocosto, " + 
			"trim(getnomcentrocosto(s.centro_costo_solicitante)) AS nomcentrocosto, " + 
			"getCodArticuloAxiomaInterfaz(da.articulo, ?) AS codmed, " +
			"getdescripcionarticulo(da.articulo) AS nomarticulo, " +
			"getunidadmedidaarticulo(da.articulo) AS unidadmedida, " +
			"getarticuloporcentajeiva(da.articulo) AS porcentajeiva, " +
			"da.cantidad AS cantadmin, " +
			"getnombreviaingreso(c.via_ingreso) AS viaingreso, " +
			"getnombretipopaciente(c.tipo_paciente) AS tipopaciente, " +
			"getnombrepersona(c.codigo_paciente) AS paciente, " +
			"getidpaciente(c.codigo_paciente) AS idpaciente, " +
			"s.estado_historia_clinica AS estado, " +
			"da.cantidad  AS cantidadart, " +
			"s.numero_solicitud AS numsolicitud, " + 
			"( " +
				"select " + 
					"coalesce(avg(dc.valor_unitario_cargado),0) " + 
				"FROM " +
					"det_cargos dc " + 
				"WHERE " +
					"dc.solicitud = s.numero_solicitud " + 
					"AND dc.articulo=da.articulo " +
			")*da.cantidad AS valortotal, " ;


	//consultar Medicamentos Controlados Administrados con las condiciones dadas
	public static HashMap consultarMediControAdmin(Connection con, HashMap criterios, int tipoBD) {

		HashMap mapa = new HashMap<String, Object>();
		String parametros = "", condiciones = "", consulta = "";

		consulta = sqlSearchMedicamentosControladosAdministrados;
		
		// si es oracle o postgres
		switch(tipoBD) {
			case DaoFactory.ORACLE:
				
				consulta += "(" +
								"select getnombreconvenio(scu.convenio) " +
								"FROM " +
									"solicitudes_subcuenta ss INNER JOIN sub_cuentas scu on (scu.sub_cuenta=ss.sub_cuenta) " +
								"WHERE " +
									"ss.solicitud = am.numero_solicitud " +
									"AND ss.articulo = da.articulo " +
									"AND ss.eliminado='N' " +
									"AND rownum = 1 " +
									"AND scu.nro_prioridad = 1 " +
									") AS nombreconvenio " +
								"FROM " +
									"detalle_admin da " +
									"INNER JOIN admin_medicamentos am ON (da.administracion = am.codigo) " + 
									"INNER JOIN solicitudes_medicamentos solmed ON (am.numero_solicitud = solmed.numero_solicitud) " +
									"INNER JOIN solicitudes s ON (s.numero_solicitud = solmed.numero_solicitud) " +
									"INNER JOIN cuentas c ON (s.cuenta = c.id) " +
									"INNER JOIN ingresos i ON (c.id_ingreso = i.id) " + 
							"WHERE "; 
			break;

			case DaoFactory.POSTGRESQL:
				consulta += "(" +
								"select " +
									"getnombreconvenio(scu.convenio) " +
								"FROM " +
								"	solicitudes_subcuenta ss INNER JOIN sub_cuentas scu on (scu.sub_cuenta=ss.sub_cuenta) " +
								"WHERE " +
									"ss.solicitud = am.numero_solicitud " +
									"AND ss.articulo = da.articulo " +
									"AND ss.eliminado='N' " +
								"ORDER BY " +
									"scu.nro_prioridad " +
								"ASC "+ValoresPorDefecto.getValorLimit1()+" 1) AS nombreconvenio " +
							"FROM " +
								"detalle_admin da " +
								"INNER JOIN admin_medicamentos am ON (da.administracion = am.codigo) " + 
								"INNER JOIN solicitudes_medicamentos solmed ON (am.numero_solicitud = solmed.numero_solicitud) " +
								"INNER JOIN solicitudes s ON (s.numero_solicitud = solmed.numero_solicitud ) " +
								"INNER JOIN cuentas c ON (s.cuenta = c.id) " +
								"INNER JOIN ingresos i ON (c.id_ingreso = i.id) " + 
							"WHERE "; 
				break;
		}

		
		//condiciones iniciales de la funcionalidad
		consulta += " to_char(da.fecha, 'YYYY-MM-DD') BETWEEN '"+ UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaInicial")+"") +
					"' AND '" + UtilidadFecha.conversionFormatoFechaABD(criterios.get("fechaFinal")+"") +

					"' AND s.tipo IN (" + ConstantesBD.codigoTipoSolicitudMedicamentos +
					", " + ConstantesBD.codigoTipoSolicitudCargosDirectosArticulos +
					") AND s.estado_historia_clinica <> " + ConstantesBD.codigoEstadoHCAnulada +
					" AND i.centro_atencion =  " + Utilidades.convertirAEntero(criterios.get("centroAtencion")+"") +
					" AND getesmedicamento(da.articulo) = '1' AND getarticulocodcategoria(da.articulo) NOT IN (" 
					+ ConstantesBD.codigoCategoriaArtNormal + ") ";
					
		
					//91686 no se coloco <> para futuro colocar listado de codigos que se filtran segun la categoria 
					/*" AND getesmedicamento(art.codigo) = '1' AND art.categoria=" + ConstantesBD.codigoCategoriaArtControl + " ";*/
		
		parametros = "Centro Atención["+ Utilidades.obtenerNombreCentroAtencion(con, Utilidades.convertirAEntero(criterios.get("centroAtencion")+""))+"] \t ";
		parametros += "Fecha Inicial ["+criterios.get("fechaInicial")+"] \t Fecha Final ["+criterios.get("fechaFinal")+"] \t ";
		parametros += "Tipo Código[" + ValoresPorDefecto.getIntegridadDominio(criterios.get("tipoCodigo")+ "")+ "] \n";

		
		//Como la Via de Ingreso no es requerida se hace necesario validarlo si viene vacio o lleno
		if(UtilidadCadena.noEsVacio(criterios.get("viaIngresoVia")+""))	{
			parametros += " Via Ingreso ["+ Utilidades.obtenerNombreViaIngreso(con, Utilidades.convertirAEntero(criterios.get("viaIngresoVia")+""))+"] \t ";
			condiciones += " AND c.via_ingreso = " + criterios.get("viaIngresoVia") + " ";			
			
			if(UtilidadCadena.noEsVacio(criterios.get("viaIngresoTipoPac")+""))	{
				logger.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
				logger.info("La Via de Ingreso tiene mas de un Tipo Paciente: " + criterios.get("viaIngresoTipoPac"));

				parametros += " Tipo Paciente ["+ Utilidades.obtenerNombreTipoPaciente(con, criterios.get("viaIngresoTipoPac")+"") + "] \t ";
				condiciones += " AND c.tipo_paciente = '" + criterios.get("viaIngresoTipoPac") + "' ";
			}
		}
		else
			parametros += "Via Ingreso [Todos] \t ";


		//Como el Centro de Costo no es requerido se hace necesario validarlo si viene vacio o lleno
		if(UtilidadCadena.noEsVacio(criterios.get("centroCosto")+""))	{
			parametros += "Centro de Costo["+ Utilidades.obtenerNombreCentroCosto(Utilidades.convertirAEntero(criterios.get("centroCosto")+""), Utilidades.convertirAEntero(criterios.get("institucion")+"")) + "] \t ";
			condiciones += " AND s.centro_costo_solicitante = " + criterios.get("centroCosto") + " ";
//			Utilidades.obtenerNombreCentroCosto(Utilidades.convertirAEntero(criterios.get("centroCosto")+""), Utilidades.convertirAEntero(criterios.get("institucion")+""));
		}
		else
			parametros += "Centro Costo [Todos] \t ";


		//Como el Articulo no es requerido se hace necesario validarlo si viene vacio o lleno
		if(UtilidadCadena.noEsVacio(criterios.get("articulo")+""))	{
			parametros += "Articulo ["+ criterios.get("articulo")+ "] \t ";
			condiciones += " AND da.articulo = " + criterios.get("articulo")+ " ";
		}
		else
			parametros += "Articulo [Todos] \t ";

			
		//Como el convenio no es requerido se hace necesario validarlo si viene vacio o lleno
		if(UtilidadCadena.noEsVacio(criterios.get("convenio")+""))	{
			parametros += "Convenio ["+Utilidades.obtenerNombreConvenioOriginal(con, Utilidades.convertirAEntero(criterios.get("convenio")+""))+"] \t ";
			condiciones += " AND dc.convenio = "+criterios.get("convenio")+" ";
		}
		else
			parametros += "Convenio [Todos] \t ";

		
		consulta += condiciones + " ORDER BY getdescripcionarticulo(da.articulo) ";
		mapa.put("parametros", parametros);
		mapa.put("consulta", consulta);
		return mapa;
	}

	
	/** Metodo para almacenar los resultados y mandarlos a un archivo plano posteriormente o a la pantalla del usuario
	 * @param (con, criterios, tipoBD
	 * @return */
	public static HashMap generarResultados(Connection con, HashMap criterios, int tipoBD) {
		HashMap mapa = new HashMap<String, Object>();
		mapa.put("numRegistros","0");
		HashMap temporal = new HashMap<String, Object>();
		temporal = consultarMediControAdmin(con, criterios, tipoBD);

		try {
        	PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(temporal.get("consulta")+"",ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, criterios.get("tipoCodigo")+"");

        	logger.info("====>Consulta Medicamentos Controlados Administrados: " +temporal.get("consulta"));
            mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
        }
        catch (SQLException e) { 
            logger.error("ERROR EJECUTANDO LA CONSULTA DE MEDICAMENTOS CONTROLADOS ADMINISTRADOS.");
            e.printStackTrace();
        }
        return mapa;
	}
	
}