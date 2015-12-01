package com.princetonsa.dto.manejoPaciente;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.facturacion.DTOResultadoBusquedaDetalleMontos;
import com.princetonsa.dto.facturacion.DtoBusquedaMontosCobro;
import com.servinte.axioma.servicio.fabrica.facturacion.FacturacionServicioFabrica;
import com.servinte.axioma.servicio.interfaz.facturacion.IBusquedaMontosCobroServicio;

import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;


/**
 * Clase que contiene los campos para la validación de Montos.
 * solo se utiliza para tener centralizados todos los valores a validar, 
 * pero solo son específicos para este proceso.
 * 
 * @author Cristhian Murillo
 */
public class DtoParametrosBusquedaMonto 
{

	private String viaIngresoStr;
	private String clasificacionSEStr;
	private Boolean pasoValidaciones;
	
	public DtoParametrosBusquedaMonto() 
	{
		this.viaIngresoStr		= null;
		this.clasificacionSEStr	= null;
		this.pasoValidaciones	= false;
	}

	
	/**
	 * Valida los campos requeridos
	 * 
	 * @autor Cristhian Murillo
	 */
	public void validarCampos()
	{
		this.pasoValidaciones = true;
		
		if(UtilidadTexto.isEmpty(this.viaIngresoStr)){
			this.pasoValidaciones = false;
		}
		else if(UtilidadTexto.isEmpty(this.clasificacionSEStr)){
			this.pasoValidaciones = false;
		}
	}
	
	
	/**
	 * @return valor de viaIngresoStr
	 */
	public String getViaIngresoStr() {
		return viaIngresoStr;
	}

	/**
	 * @param viaIngresoStr el viaIngresoStr para asignar
	 */
	public void setViaIngresoStr(String viaIngresoStr) {
		this.viaIngresoStr = viaIngresoStr;
	}

	/**
	 * @return valor de clasificacionSEStr
	 */
	public String getClasificacionSEStr() {
		return clasificacionSEStr;
	}

	/**
	 * @param clasificacionSEStr el clasificacionSEStr para asignar
	 */
	public void setClasificacionSEStr(String clasificacionSEStr) {
		this.clasificacionSEStr = clasificacionSEStr;
	}

	/**
	 * @return valor de pasoValidaciones
	 */
	public Boolean getPasoValidaciones() {
		return pasoValidaciones;
	}

	/**
	 * @param pasoValidaciones el pasoValidaciones para asignar
	 */
	public void setPasoValidaciones(Boolean pasoValidaciones) {
		this.pasoValidaciones = pasoValidaciones;
	}
	
	
	/**
	 * Llama al proceso centralizado de búsqueda de Monto cobro. DCU 986
	 * @param clasificacionSE
	 * @param codigoConvenio
	 * @param excepcionMonto
	 * @param tipoAfiliado
	 * @param tipoPaciente
	 * @param viaIngreso
	 * @return ArrayList<DTOResultadoBusquedaDetalleMontos>
	 *
	 * @autor Cristhian Murillo
	 */
	public ArrayList<DTOResultadoBusquedaDetalleMontos> buscarMonto(Integer clasificacionSE, Integer codigoConvenio,
			Integer excepcionMonto, String tipoAfiliado, String tipoPaciente, Integer viaIngreso) 
	{
		
		IBusquedaMontosCobroServicio busquedaMontoServicio = FacturacionServicioFabrica.crearBusquedaMontosCobroServicio();
		
		Connection con = null;
		DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
		try {
			con = myFactory.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		ArrayList<DTOResultadoBusquedaDetalleMontos> listaResultadoBusquedaDetalleMontos = new ArrayList<DTOResultadoBusquedaDetalleMontos>();
		
		DtoBusquedaMontosCobro montoCobro= busquedaMontoServicio.consultarMontosCobro(
				clasificacionSE, codigoConvenio, UtilidadFecha.getFechaActual(con), 
				excepcionMonto, tipoAfiliado, tipoPaciente, viaIngreso);
		
		if(montoCobro.getResultado().isTrue())
		{
			@SuppressWarnings("rawtypes")
			Iterator it=montoCobro.getMontosCobro().iterator();
			if(it.hasNext())
			{
				DTOResultadoBusquedaDetalleMontos dto = (DTOResultadoBusquedaDetalleMontos)it.next();
				listaResultadoBusquedaDetalleMontos.add(dto);
			}
		}
		
	
		UtilidadBD.closeConnection(con);
		return listaResultadoBusquedaDetalleMontos;
	}

}
