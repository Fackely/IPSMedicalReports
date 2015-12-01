package com.servinte.axioma.mundo.impl.tesoreria;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.struts.util.MessageResources;

import com.princetonsa.dto.tesoreria.DtoConsolidadoCierreReporte;
import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.IConsultaConsolidadoCierreDAO;
import com.servinte.axioma.mundo.fabrica.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.ICentroAtencionMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IInstitucionesMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IConsultaConsolidadoCierresMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IFormasPagoMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.IMovimientosCajaMundo;
import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.EmpresasInstitucion;
import com.servinte.axioma.orm.FormasPago;
import com.servinte.axioma.orm.Instituciones;

public class ConsultaConsolidadoCierresMundo implements IConsultaConsolidadoCierresMundo 
{

	/**
	 * Dao de cierre consolidado
	 */
	private IConsultaConsolidadoCierreDAO consultaConsolidadoCierresDAO;

	/**
	 * Movimiento caja mundo para calulos de reportes
	 */
	private IMovimientosCajaMundo MovimientosCajaMundo;

	/**
	 * Mundo de instituciones para hacer consultar las instituciones
	 */
	private IInstitucionesMundo institucionesMundo;

	/**
	 * Mundo de centro de atencion
	 */
	private ICentroAtencionMundo centroAtencionMundo;

	/**
	 * Mundo de las formas de pago
	 */
	private IFormasPagoMundo formasPagoMundo;

	/**
	 * Mensajes parametrizados del reporte.
	 */
	private MessageResources messageResource = MessageResources
			.getMessageResources("com.servinte.mensajes.tesoreria.ConsultarConsolidadosCierres");

	/**
	 * Constructor de clase
	 */
	public ConsultaConsolidadoCierresMundo() {
		consultaConsolidadoCierresDAO = TesoreriaFabricaDAO
				.crearConsolidacionDAO();
		MovimientosCajaMundo = TesoreriaFabricaMundo
				.crearMovimientosCajaMundo();
		institucionesMundo = AdministracionFabricaMundo
				.crearInstitucionesMundo();
		centroAtencionMundo = AdministracionFabricaMundo
				.crearCentroAtencionMundo();
		formasPagoMundo = TesoreriaFabricaMundo.crearFormasPagoMundo();

	}

	/**
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConsultaConsolidadoCierresMundo#consultarEmpresaInstitucionXinstitucion(java.lang.Integer)
	 */
	public ArrayList<EmpresasInstitucion> consultarEmpresaInstitucionXinstitucion(
			Integer institucion) {
		return consultaConsolidadoCierresDAO
				.consultarEmpresaInstitucionXinstitucion(institucion);
	}

	/**
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConsultaConsolidadoCierresMundo#consultarTodosCentrosAtencion()
	 */
	public ArrayList<CentroAtencion> consultarTodosCentrosAtencion() {
		return consultaConsolidadoCierresDAO.consultarCentrosAtencion();
	}

	/**
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConsultaConsolidadoCierresMundo#consultarconsolidadoCierre(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public ArrayList<DtoConsolidadoCierreReporte> consultarconsolidadoCierre(
			String fecha, String centroAtencion, String empresaInstitucion) {
		return MovimientosCajaMundo.consultarconsolidadoCierre(fecha,
				centroAtencion, empresaInstitucion, consultarFormaPago());
	}

	/**
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConsultaConsolidadoCierresMundo#consultarconsolidadoCierreCajaCajero(java.lang.String,
	 *      java.lang.String, java.lang.String)
	 */
	public ArrayList<DtoConsolidadoCierreReporte> consultarconsolidadoCierreCajaCajero(
			String fecha, String centroAtencion, String empresaInstitucion) {
		return MovimientosCajaMundo.consultarconsolidadoCierreCajaCajero(fecha,
				centroAtencion, empresaInstitucion, consultarFormaPago());
	}
	
	@Override
	public ArrayList<DtoConsolidadoCierreReporte> consultaTrasladoCajaMayorPrincipal(DtoConsolidadoCierreReporte parametros) 
	{
		return MovimientosCajaMundo.consultaTrasladoCajaMayorPrincipal(parametros);
	}

	
	@Override
	public ArrayList<DtoConsolidadoCierreReporte> consultaTrasladoHaciaCajaMayor(DtoConsolidadoCierreReporte parametros) 
	{
		return MovimientosCajaMundo.consultaTrasladoHaciaCajaMayor(parametros);
	}
	
	
	/**
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConsultaConsolidadoCierresMundo#consultarIntitucionusuario(java.lang.Integer)
	 */
	public Instituciones consultarIntitucionusuario(Integer codigo) {
		return institucionesMundo.buscarPorCodigo(codigo);
	}

	/**
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConsultaConsolidadoCierresMundo#obtenerCentroAtencionUsuario(java.lang.Integer)
	 */
	public CentroAtencion obtenerCentroAtencionUsuario(Integer codigo) {
		return centroAtencionMundo.buscarPorCodigo(codigo);
	}

	/**
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConsultaConsolidadoCierresMundo#listarInstituciones()
	 */
	public ArrayList<Instituciones> listarInstituciones() {
		return institucionesMundo.listarInstituciones();
	}

	/**
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConsultaConsolidadoCierresMundo#listarActivos()
	 */
	public ArrayList<CentroAtencion> listarActivos() {
		return centroAtencionMundo.listarActivos();
	}

	/**
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConsultaConsolidadoCierresMundo#consultarFormaPago()
	 */
	public List<FormasPago> consultarFormaPago() {
		return formasPagoMundo.obtenerFormasPagosActivos();
	}

	/**
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConsultaConsolidadoCierresMundo#totalesCierreCentroAtencion(java.util.ArrayList)
	 */
	public ArrayList<DtoConsolidadoCierreReporte> totalesCierreCentroAtencion(
			ArrayList<DtoConsolidadoCierreReporte> cierres) {

		List<FormasPago> formasPago = consultarFormaPago();
		DtoConsolidadoCierreReporte totalCosnulta = new DtoConsolidadoCierreReporte();
		ArrayList<BigDecimal> totales = new ArrayList<BigDecimal>();
		BigDecimal totalizador = new BigDecimal(0);
		BigDecimal totalizadorHorizontal = new BigDecimal(0);

		if (cierres.size() > 0) {

			// se recorren todas las formas de pago que estan parametrizadas en
			// el sistema
			for (int i = 0; i < formasPago.size(); i++) {
				totalizador = new BigDecimal(0);
				for (int j = 0; j < cierres.size(); j++) {

					// por cada cierre se totaliza por forma de pago
					totalizador = totalizador.add(cierres.get(j)
							.getTotalesPorFormaPago().get(i));
				}

				// se adiciona el valor totalizado en la lista resultante
				totales.add(totalizador);
			}

			// la lista resultado se suma para obtener el total horizontal del
			// centro de atencion
			for (BigDecimal bigDecimal : totales) {
				totalizadorHorizontal = totalizadorHorizontal.add(bigDecimal);
			}

			// se settean valores al objeto que contiene ls resultados
			totalCosnulta.setCentroAtencion(messageResource
					.getMessage("consolidacion_cierres_mensaje_reporte_jsp"));
			totalCosnulta.setIntitucion(cierres.get(0).getIntitucion());
			totalCosnulta.setTotalesPorFormaPago(totales);
			totalCosnulta.setTotalCentroAtencion(totalizadorHorizontal);
			totalCosnulta.setEsTotalJsp(true);
			cierres.add(totalCosnulta);
		}

		return cierres;
	}

	/**
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConsultaConsolidadoCierresMundo#totalesCierreCentroAtencionCajaCajero(java.util.ArrayList)
	 */
	public ArrayList<DtoConsolidadoCierreReporte> totalesCierreCentroAtencionCajaCajero(
			ArrayList<DtoConsolidadoCierreReporte> cierres) {

		DtoConsolidadoCierreReporte total = new DtoConsolidadoCierreReporte();
		List<FormasPago> formasPago = consultarFormaPago();
		ArrayList<BigDecimal> totalReporte = new ArrayList<BigDecimal>();
		BigDecimal totalizador = new BigDecimal(0);
		BigDecimal totalizadorHorizontal = new BigDecimal(0);
		DtoConsolidadoCierreReporte totalCosnulta = new DtoConsolidadoCierreReporte();

		if (cierres.size() > 0) {

			// se recorren todas las formas de pago que estan parametrizadas en
			// el sistema
			for (int j = 0; j < formasPago.size(); j++) {
				totalizador = new BigDecimal(0);
				for (int i = 0; i < cierres.size(); i++) {
					if (cierres.get(i) != null && cierres.get(i).isEsTotalJsp()) {

						// se totaliza por centro de atencion la forma de pago
						totalizador = totalizador.add(cierres.get(i)
								.getTotalesPorFormaPago().get(j));
					}
				}
				totalReporte.add(totalizador);

			}

			// suma total de centro atencion
			for (BigDecimal bigDecimal : totalReporte) {
				totalizadorHorizontal = totalizadorHorizontal.add(bigDecimal);
			}

			totalCosnulta.setCaja(messageResource
					.getMessage("consolidacion_cierres_mensaje_reporte_jsp"));
			totalCosnulta.setCajero("");
			totalCosnulta.setHoraTurnoCajero("");
			totalCosnulta.setIntitucion(cierres.get(cierres.size() - 2)
					.getIntitucion());
			totalCosnulta.setCentroAtencion(cierres.get(cierres.size() - 2)
					.getCentroAtencion());
			totalCosnulta.setTotalesPorFormaPago(totalReporte);
			totalCosnulta.setTotalCentroAtencion(totalizadorHorizontal);
			totalCosnulta.setEsTotalJsp(true);
			cierres.add(totalCosnulta);

			cierres.add(total);
		}

		return cierres;
	}

	/**
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConsultaConsolidadoCierresMundo#totalesXCentroAtencionCajaCajero(java.util.ArrayList)
	 */
	public ArrayList<DtoConsolidadoCierreReporte> totalesXCentroAtencionCajaCajero(
			ArrayList<DtoConsolidadoCierreReporte> totales) {
		ArrayList<DtoConsolidadoCierreReporte> res = new ArrayList<DtoConsolidadoCierreReporte>();
		DtoConsolidadoCierreReporte sumaTotal = new DtoConsolidadoCierreReporte();
		ArrayList<String> tmp = new ArrayList<String>();
		String institucion = "";
		String centroAtencion = "";
		BigDecimal totalCentroAtencionHorizontal = new BigDecimal(0);

		ArrayList<BigDecimal> listaTotalesXCentroAtencion = new ArrayList<BigDecimal>();
		BigDecimal totalizador = new BigDecimal(0);
		List<FormasPago> formasPago = consultarFormaPago();
		@SuppressWarnings("unused")
		Boolean flag = true;

		// se recorre la lista por institucion y por centro de atencion, cada
		// combinacion es adicionada a una lista de string para
		// que no se repitan sumas
		for (int i = 0; i < totales.size(); i++) {
			institucion = totales.get(i).getIntitucion();
			for (int j = 0; j < totales.size(); j++) {
				centroAtencion = totales.get(j).getCentroAtencion();
				sumaTotal = new DtoConsolidadoCierreReporte();
				if (!tmp.contains(institucion + centroAtencion)) {

					listaTotalesXCentroAtencion = new ArrayList<BigDecimal>();
					totalCentroAtencionHorizontal = new BigDecimal(0);
					for (int j6 = 0; j6 < formasPago.size(); j6++) {

						totalizador = new BigDecimal(0);

						for (int j2 = 0; j2 < totales.size(); j2++) {
							if (totales.get(j2).getIntitucion()
									.equals(institucion)
									&& totales.get(j2).getCentroAtencion()
											.equals(centroAtencion)) {
								// se adicionan valores totalizando

								totalizador = totalizador.add(totales.get(j2)
										.getTotalesPorFormaPago().get(j6));
							}
						}

						listaTotalesXCentroAtencion.add(totalizador);
					}

					for (BigDecimal totalesBig : listaTotalesXCentroAtencion) {
						totalCentroAtencionHorizontal = totalCentroAtencionHorizontal
								.add(totalesBig);
					}

					// se setean los objetos totalizados
					sumaTotal
							.setTotalCentroAtencion(totalCentroAtencionHorizontal);
					// se adiciona el valor de subtotal para mostar en el
					// reporte en jsp
					sumaTotal
							.setCaja(messageResource
									.getMessage("consolidacion_cierres_label_caja_cajero_subtotal_centro_atencion")
									+ " " + centroAtencion);
					sumaTotal.setCajero("");
					sumaTotal.setHoraTurnoCajero("");
					sumaTotal.setIntitucion(institucion);
					sumaTotal.setCentroAtencion(centroAtencion);
					sumaTotal
							.setTotalesPorFormaPago(listaTotalesXCentroAtencion);
					sumaTotal.setEsTotalJsp(true);
					res.add(sumaTotal);
					tmp.add(institucion + centroAtencion);
				}
			}
		}
		// lista totalizada
		return adicionarSubtotales(totales, res);
		// return totales;

	}

	/**
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.IConsultaConsolidadoCierresMundo#adicionarSubtotales(java.util.ArrayList,
	 *      java.util.ArrayList)
	 */
	public ArrayList<DtoConsolidadoCierreReporte> adicionarSubtotales(
			ArrayList<DtoConsolidadoCierreReporte> totales,
			ArrayList<DtoConsolidadoCierreReporte> subtotales) {
		ArrayList<DtoConsolidadoCierreReporte> res = new ArrayList<DtoConsolidadoCierreReporte>();

		// se recorren los subtotales y totales para ir adicionando los
		// subtotales en una unica lista
		for (int k = 0; k < subtotales.size(); k++) {
			for (int i = 0; i < totales.size(); i++) {

				// si el siguiente es de diferente institucion y centro atencion
				// se adiciona el subtotal
				if (totales.get(i).getIntitucion()
						.equals(subtotales.get(k).getIntitucion())
						&& totales.get(i).getCentroAtencion()
								.equals(subtotales.get(k).getCentroAtencion())) {
					res.add(totales.get(i));
				}
			}
			res.add(subtotales.get(k));
		}
		return res;
	}

}
