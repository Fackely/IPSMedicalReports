/*
 * Ago 05, 2009
 */
package com.princetonsa.dto.interfaz;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.Utilidades;

import com.princetonsa.dao.sqlbase.interfaz.SqlBaseGeneracionInterfaz1EDao;
import com.princetonsa.dto.facturacion.DtoConceptoRetencionTercero;

/**
 * DTO: que almacena el resultaod del cálculo de la retencion
 * @author Sebastián Gómez R
 *
 */
public class DtoRetencion implements Serializable
{
	private static Logger logger = Logger.getLogger(DtoRetencion.class);
	//Conceptos resultado del calculo de la retencion
	private ArrayList<DtoConceptoRetencionTercero> conceptos;
	private ArrayList<DtoConceptoRetencionTercero> conceptosFinales;
	//indicador para saber si es retencion o autoretencion
	private boolean retencion;
	
	/**
	 * Inconsisntencias de la retencion
	 */
	private ArrayList<String> inconsistenciasRetencion = new ArrayList<String>();
	
	
	public void clean()
	{
		this.conceptos = new ArrayList<DtoConceptoRetencionTercero>();
		this.conceptosFinales = new ArrayList<DtoConceptoRetencionTercero>();
		this.retencion = false;
		this.inconsistenciasRetencion = new ArrayList<String>();
	}
	
	/**
	 * Constructor
	 */
	public DtoRetencion()
	{
		this.clean();
	}

	/**
	 * @return the conceptos
	 */
	public ArrayList<DtoConceptoRetencionTercero> getConceptos() {
		return conceptos;
	}

	/**
	 * @param conceptos the conceptos to set
	 */
	public void setConceptos(ArrayList<DtoConceptoRetencionTercero> conceptos) {
		this.conceptos = conceptos;
	}

	/**
	 * @return the retencion
	 */
	public boolean isRetencion() {
		return retencion;
	}

	/**
	 * @param retencion the retencion to set
	 */
	public void setRetencion(boolean retencion) {
		this.retencion = retencion;
	}
	
	/**
	 * Método para saber el numero de conceptos que quedaron conretencion
	 * @return
	 */
	public void insertarConceptosConRetencion()
	{
		int contador = 0;
		//logger.info("Conceptos iniciales: "+this.conceptos.size());
		for(DtoConceptoRetencionTercero concepto:conceptos)
		{
			if(concepto.isCalculoRetencionExitoso()&&!concepto.isCancelarRetencion())
			{
				contador ++;
				DtoConceptoRetencionTercero nuevoConcepto = new DtoConceptoRetencionTercero();
				nuevoConcepto = concepto;
				logger.info("¡Se añade nuevo concepto !"+nuevoConcepto.getDescripcion());
				this.conceptosFinales.add(nuevoConcepto);
			}
		}
		
		this.conceptos = new ArrayList<DtoConceptoRetencionTercero>();
		//logger.info("Size conceptos iniciales: "+this.conceptos.size());
		//logger.info("Size conceptos finales: "+this.conceptosFinales.size());
		this.conceptos = this.conceptosFinales;
	}

	/**
	 * @return the inconsistenciasRetencion
	 */
	public ArrayList<String> getInconsistenciasRetencion() {
		return inconsistenciasRetencion;
	}

	/**
	 * @param inconsistenciasRetencion the inconsistenciasRetencion to set
	 */
	public void setInconsistenciasRetencion(
			ArrayList<String> inconsistenciasRetencion) {
		this.inconsistenciasRetencion = inconsistenciasRetencion;
	}
	
	/**
	 * Método para consultar el total del valor de la retencion
	 * @return
	 */
	public double consultarTotalValorRetencion()
	{
		double total = 0;
		for(DtoConceptoRetencionTercero concepto:this.conceptos)
		{
			total += Utilidades.convertirADouble(concepto.getValorRetencion(),true);
		}
		return total;
	}
}
