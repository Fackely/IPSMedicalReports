
package com.servinte.axioma.servicio.impl.tesoreria;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.tesoreria.DtoBusquedaCierreArqueo;
import com.princetonsa.dto.tesoreria.DtoConsolidadoMovimiento;
import com.princetonsa.dto.tesoreria.DtoDetalleDocSopor;
import com.princetonsa.dto.tesoreria.DtoInformacionEntrega;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.impl.tesoreria.ETipoMovimiento;
import com.servinte.axioma.mundo.interfaz.tesoreria.IConsultaArqueoCierreCajaMundo;
import com.servinte.axioma.orm.MovimientosCaja;
import com.servinte.axioma.servicio.interfaz.tesoreria.IConsultaArqueoCierreCajaServicio;


/**
 * Implementaci&oacute;n de la interfaz {@link IConsultaArqueoCierreCajaServicio}
 * 
 * @autor Jorge Armando Agudelo Quintero
 *
 */
public class ConsultaArqueoCierreCajaServicio implements IConsultaArqueoCierreCajaServicio{

	private IConsultaArqueoCierreCajaMundo consultaArqueoCierreCajaMundo;

	/**
	 * Constructor de la clase
	 */
	public ConsultaArqueoCierreCajaServicio() {
		
		consultaArqueoCierreCajaMundo = TesoreriaFabricaMundo.crearConsultaArqueoCierreCajaMundo();
	}
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IConsultaArqueoCierreCajaServicio#consultarCierreTurnoCaja(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public DtoInformacionEntrega consultarCierreTurnoCaja(MovimientosCaja movimientoCierreTurno) {
		
		return consultaArqueoCierreCajaMundo.consultarCierreTurnoCaja(movimientoCierreTurno);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IConsultaArqueoCierreCajaServicio#consultarArqueoCaja(com.servinte.axioma.orm.MovimientosCaja)
	 */
	@Override
	public DtoConsolidadoMovimiento consultarArqueoCaja(MovimientosCaja arqueoCaja) {
		
		return consultaArqueoCierreCajaMundo.consultarArqueoCaja(arqueoCaja);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IConsultaArqueoCierreCajaServicio#consultarArqueoParcialPorEntrega(long, com.servinte.axioma.mundo.impl.tesoreria.ETipoMovimiento, int)
	 */
	@Override
	public DtoInformacionEntrega consultarArqueoParcialPorEntrega(long codigoEntrega, ETipoMovimiento eTipoMovimiento,int codigoInstitucion) {
		
		return consultaArqueoCierreCajaMundo.consultarArqueoParcialPorEntrega(codigoEntrega, eTipoMovimiento, codigoInstitucion);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IConsultaArqueoCierreCajaServicio#obtenerDtoInformacionEntregaImpresion(java.util.ArrayList, int)
	 */
	@Override
	public DtoInformacionEntrega obtenerDtoInformacionEntregaImpresion(	ArrayList<DtoDetalleDocSopor> listadoDefinitivoDocSop,	int codigoInstitucion) {
		
		return consultaArqueoCierreCajaMundo.obtenerDtoInformacionEntregaImpresion(listadoDefinitivoDocSop, codigoInstitucion);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IConsultaArqueoCierreCajaServicio#consultarCierreArqueo(com.princetonsa.dto.tesoreria.DtoBusquedaCierreArqueo)
	 */
	@Override
	public List<MovimientosCaja> consultarCierreArqueo(DtoBusquedaCierreArqueo dtoBusquedaCierreArqueo) {
		
		return consultaArqueoCierreCajaMundo.consultarCierreArqueo(dtoBusquedaCierreArqueo);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.IConsultaArqueoCierreCajaServicio#consultarEntregaAsociadaArqueoParcial(long)
	 */
	@Override
	public MovimientosCaja consultarEntregaAsociadaArqueoParcial(long codigoMovimientoArqueo) {
		
		return consultaArqueoCierreCajaMundo.consultarEntregaAsociadaArqueoParcial(codigoMovimientoArqueo);
	}
}
