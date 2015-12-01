/**
 * 
 */
package com.princetonsa.actionform.cartera;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;

import com.princetonsa.dto.cartera.DtoFiltroReporteCuentasCobro;
import com.princetonsa.dto.cartera.DtoResultadoReporteCuentaCobro;

/**
 * @author armando
 *
 */
public class ReporteCuentaCobroForm extends ValidatorForm 
{
	/**objetos de retorno en el reporte **/
	private boolean reporteGenerado=false;
	
	/**
	 * 
	 */
	private String estado;
	
	/**
	 * 
	 */
	private DtoFiltroReporteCuentasCobro dtoFiltro;
	
	/**
	 * 
	 */
	private ArrayList<DtoResultadoReporteCuentaCobro> dtoResultado;
	
	
	public void reset()
	{
		this.dtoFiltro=new DtoFiltroReporteCuentasCobro();
		this.dtoResultado=new ArrayList<DtoResultadoReporteCuentaCobro>();
		this.reporteGenerado=false;
	}
	
	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{		
		ActionErrors errores = new ActionErrors();
		if(this.estado.equals("imprimirArchivoPlano")||this.estado.equals("imprimirVisual"))
		{
			if(UtilidadTexto.isEmpty(this.dtoFiltro.getFechaElaboracionInicial())&&UtilidadTexto.isEmpty(this.dtoFiltro.getFechaElaboracionFinal())&&UtilidadTexto.isEmpty(this.dtoFiltro.getFechaRadicacionInicial())&&UtilidadTexto.isEmpty(this.dtoFiltro.getFechaRadicacionFinal())&&UtilidadTexto.isEmpty(this.dtoFiltro.getCuentaCobroInicial())&&UtilidadTexto.isEmpty(this.dtoFiltro.getCuentaCobroFinal()))
			{
				errores.add("error", new ActionMessage("errors.required", "La Fecha Elaboracion o La Fecha Radicacion o El Rango de Cuentas de Cobro"));
			}
			else
			{
				String fechaSistema=UtilidadFecha.getFechaActual();
				if(!UtilidadTexto.isEmpty(this.dtoFiltro.getFechaElaboracionInicial())&&!UtilidadTexto.isEmpty(this.dtoFiltro. getFechaElaboracionFinal()))
				{
					if(!UtilidadFecha.validarFecha(this.dtoFiltro.getFechaElaboracionInicial()))
					{
						errores.add("fecha", new ActionMessage("errors.formatoFechaInvalido",this.dtoFiltro.getFechaElaboracionInicial()));
					}
					else
					{
						if((UtilidadFecha.conversionFormatoFechaABD(this.dtoFiltro.getFechaElaboracionInicial())).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaSistema))>0)
						{
							errores.add("fecha ", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Elaboracion Inicial", "Sistema"));
						}
						if(!UtilidadFecha.validarFecha(this.dtoFiltro.getFechaElaboracionFinal()))
						{
							errores.add("fecha", new ActionMessage("errors.formatoFechaInvalido",this.dtoFiltro.getFechaElaboracionFinal()));
						}
						else
						{
							if((UtilidadFecha.conversionFormatoFechaABD(this.dtoFiltro.getFechaElaboracionFinal())).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaSistema))>0)
							{
								errores.add("fecha ", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Elaboracion Final", "Sistema"));
							}
							if((UtilidadFecha.conversionFormatoFechaABD(this.dtoFiltro.getFechaElaboracionInicial())).compareTo(UtilidadFecha.conversionFormatoFechaABD(dtoFiltro.getFechaElaboracionFinal()))>0)
							{
								errores.add("fecha de radicacion", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Elaboracion Inicial", "Elaboracion Final"));
							}
						}
					}
				}
				if(!UtilidadTexto.isEmpty(this.dtoFiltro.getFechaRadicacionInicial())&&!UtilidadTexto.isEmpty(this.dtoFiltro. getFechaRadicacionFinal()))
				{
					if(!UtilidadFecha.validarFecha(this.dtoFiltro.getFechaRadicacionInicial()))
					{
						errores.add("fecha", new ActionMessage("errors.formatoFechaInvalido",this.dtoFiltro.getFechaRadicacionInicial()));
					}
					else
					{
						if((UtilidadFecha.conversionFormatoFechaABD(this.dtoFiltro.getFechaRadicacionInicial())).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaSistema))>0)
						{
							errores.add("fecha ", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Radicacion Inicial", "Sistema"));
						}
						if(!UtilidadFecha.validarFecha(this.dtoFiltro.getFechaRadicacionFinal()))
						{
							errores.add("fecha", new ActionMessage("errors.formatoFechaInvalido",this.dtoFiltro.getFechaRadicacionFinal()));
						}
						else
						{
							if((UtilidadFecha.conversionFormatoFechaABD(this.dtoFiltro.getFechaRadicacionFinal())).compareTo(UtilidadFecha.conversionFormatoFechaABD(fechaSistema))>0)
							{
								errores.add("fecha ", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Radicacion Final", "Sistema"));
							}
							if((UtilidadFecha.conversionFormatoFechaABD(this.dtoFiltro.getFechaRadicacionInicial())).compareTo(UtilidadFecha.conversionFormatoFechaABD(dtoFiltro.getFechaRadicacionFinal()))>0)
							{
								errores.add("fecha de radicacion", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Radicacion Inicial", "Radicacion Final"));
							}
						}
					}
				}
				if(UtilidadTexto.isEmpty(dtoFiltro.getCuentaCobroInicial())&&!UtilidadTexto.isEmpty(dtoFiltro.getCuentaCobroFinal()))
				{
					errores.add("error", new ActionMessage("errors.required", "La Cuenta de Cobro Inicial"));
				}
				if(!UtilidadTexto.isEmpty(dtoFiltro.getCuentaCobroInicial())&&UtilidadTexto.isEmpty(dtoFiltro.getCuentaCobroFinal()))
				{
					errores.add("error", new ActionMessage("errors.required", "La Cuenta de Cobro Final"));
				}
			}
		}
		return errores;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public DtoFiltroReporteCuentasCobro getDtoFiltro() {
		return dtoFiltro;
	}


	public void setDtoFiltro(DtoFiltroReporteCuentasCobro dtoFiltro) {
		this.dtoFiltro = dtoFiltro;
	}

	public ArrayList<DtoResultadoReporteCuentaCobro> getDtoResultado() {
		return dtoResultado;
	}

	public void setDtoResultado(
			ArrayList<DtoResultadoReporteCuentaCobro> dtoResultado) {
		this.dtoResultado = dtoResultado;
	}

	public boolean isReporteGenerado() {
		return reporteGenerado;
	}

	public void setReporteGenerado(boolean reporteGenerado) {
		this.reporteGenerado = reporteGenerado;
	}


	

}
