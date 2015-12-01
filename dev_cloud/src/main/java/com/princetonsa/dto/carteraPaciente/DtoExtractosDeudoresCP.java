package com.princetonsa.dto.carteraPaciente;

import java.io.Serializable;

import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.facturacion.DtoFactura;

public class DtoExtractosDeudoresCP implements Serializable
{
	private DtoDeudor dtoDeudor;
	private DtoPersonas dtoPersonas;
	private DtoDatosFinanciacion dtoDatosFin;
	private DtoDocumentosGarantia dtoDocsGarantia;
	private DtoFactura dtoFactura;
	private DtoAplicacPagosCarteraPac dtoAplicacion;
	
	public DtoExtractosDeudoresCP()
	{
		this.reset();
	}
	
	public void reset()
	{
		this.dtoDeudor=new DtoDeudor();
		this.dtoPersonas=new DtoPersonas();
		this.dtoDatosFin= new DtoDatosFinanciacion();
		this.dtoDocsGarantia=new DtoDocumentosGarantia();
		this.dtoFactura=new DtoFactura();
		this.dtoAplicacion= new DtoAplicacPagosCarteraPac();
	}

	public DtoDeudor getDtoDeudor() {
		return dtoDeudor;
	}

	public void setDtoDeudor(DtoDeudor dtoDeudor) {
		this.dtoDeudor = dtoDeudor;
	}

	public DtoPersonas getDtoPersonas() {
		return dtoPersonas;
	}

	public void setDtoPersonas(DtoPersonas dtoPersonas) {
		this.dtoPersonas = dtoPersonas;
	}

	public DtoDatosFinanciacion getDtoDatosFin() {
		return dtoDatosFin;
	}

	public void setDtoDatosFin(DtoDatosFinanciacion dtoDatosFin) {
		this.dtoDatosFin = dtoDatosFin;
	}

	public DtoDocumentosGarantia getDtoDocsGarantia() {
		return dtoDocsGarantia;
	}

	public void setDtoDocsGarantia(DtoDocumentosGarantia dtoDocsGarantia) {
		this.dtoDocsGarantia = dtoDocsGarantia;
	}

	public DtoFactura getDtoFactura() {
		return dtoFactura;
	}

	public void setDtoFactura(DtoFactura dtoFactura) {
		this.dtoFactura = dtoFactura;
	}

	public DtoAplicacPagosCarteraPac getDtoAplicacion() {
		return dtoAplicacion;
	}

	public void setDtoAplicacion(DtoAplicacPagosCarteraPac dtoAplicacion) {
		this.dtoAplicacion = dtoAplicacion;
	}
	
	
}