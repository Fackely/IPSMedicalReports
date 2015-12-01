package com.princetonsa.dto.carteraPaciente;

import java.io.Serializable;
import java.math.BigDecimal;


import com.princetonsa.dto.administracion.DtoPersonas;
import com.princetonsa.dto.facturacion.DtoFactura;
import com.princetonsa.dto.facturasVarias.DtoRecibosCaja;


public class DtoReporteDocumentosCarteraPaciente implements Serializable
{	
	DtoDocumentosGarantia infoDocsGarantia;
	DtoFactura infoFactura;
	DtoDeudor infoDeudor;
	DtoPersonas infoPaciente;
	DtoRecibosCaja infoRC;
	DtoDatosFinanciacion infoDatosFin;
	
	public DtoReporteDocumentosCarteraPaciente()
	{
		this.reset();
	}
	
	public void reset()
	{
		this.infoDocsGarantia=new DtoDocumentosGarantia();
		this.infoFactura=new DtoFactura();
		this.infoDeudor=new DtoDeudor();
		this.infoPaciente=new DtoPersonas();
		this.infoRC=new DtoRecibosCaja();
		this.infoDatosFin=new DtoDatosFinanciacion();
	}

	public DtoDocumentosGarantia getInfoDocsGarantia() {
		return infoDocsGarantia;
	}

	public void setInfoDocsGarantia(DtoDocumentosGarantia infoDocsGarantia) {
		this.infoDocsGarantia = infoDocsGarantia;
	}

	public DtoFactura getInfoFactura() {
		return infoFactura;
	}

	public void setInfoFactura(DtoFactura infoFactura) {
		this.infoFactura = infoFactura;
	}

	public DtoDeudor getInfoDeudor() {
		return infoDeudor;
	}

	public void setInfoDeudor(DtoDeudor infoDeudor) {
		this.infoDeudor = infoDeudor;
	}

	public DtoPersonas getInfoPaciente() {
		return infoPaciente;
	}

	public void setInfoPaciente(DtoPersonas infoPaciente) {
		this.infoPaciente = infoPaciente;
	}

	public DtoRecibosCaja getInfoRC() {
		return infoRC;
	}

	public void setInfoRC(DtoRecibosCaja infoRC) {
		this.infoRC = infoRC;
	}

	public DtoDatosFinanciacion getInfoDatosFin() {
		return infoDatosFin;
	}

	public void setInfoDatosFin(DtoDatosFinanciacion infoDatosFin) {
		this.infoDatosFin = infoDatosFin;
	}
}