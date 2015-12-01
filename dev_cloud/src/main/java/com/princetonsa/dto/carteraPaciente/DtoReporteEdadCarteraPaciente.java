package com.princetonsa.dto.carteraPaciente;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;
import util.InfoDatosDouble;
import util.InfoDatosInt;
import util.InfoDatosStr;

public class DtoReporteEdadCarteraPaciente implements Serializable{
	
	public String fechaCorte;
	
	public String tipoDocumento;
	
	public String centroAtencion;
	
	public String tipoSalida;
	
	public ArrayList<DtoDatosFinanciacion> arrayDatos;
	
	public ArrayList<InfoDatosInt> arrayRangos;
	
	public ArrayList<InfoDatosDouble> arrayTotalFilas;
	
	public ArrayList<InfoDatosDouble> arrayTotalColumnas;
	
	public String rutaNombreArchivo ;
	
	public String rutaNombreArchivoPrivado ;
	
	public DtoReporteEdadCarteraPaciente()
	{
		fechaCorte = "";
		tipoDocumento = "";
		centroAtencion = "";
		tipoSalida = "";
		arrayDatos = new ArrayList<DtoDatosFinanciacion>();
		inicializarRangos();
		this.rutaNombreArchivo = "";
		rutaNombreArchivoPrivado ="";
	}
	
	public void inicializarRangos()
	{
		this.arrayRangos = new ArrayList<InfoDatosInt>();
		
		InfoDatosInt info = new InfoDatosInt();
		info.setDescripcion("0");
		info.setCodigo(0);
		info.setCodigo2(0);
		this.arrayRangos.add(info);
		
		info = new InfoDatosInt();
		info.setDescripcion("1 a 30");
		info.setCodigo(1);
		info.setCodigo2(30);
		this.arrayRangos.add(info);
		
		info = new InfoDatosInt();
		info.setDescripcion("31 a 60");
		info.setCodigo(31);
		info.setCodigo2(60);
		this.arrayRangos.add(info);
		
		info = new InfoDatosInt();
		info.setDescripcion("61 a 90");
		info.setCodigo(61);
		info.setCodigo2(90);
		this.arrayRangos.add(info);
		
		info = new InfoDatosInt();
		info.setDescripcion("91 a 120");
		info.setCodigo(91);
		info.setCodigo2(120);
		this.arrayRangos.add(info);
		
		info = new InfoDatosInt();
		info.setDescripcion("121 a 150");
		info.setCodigo(121);
		info.setCodigo2(150);
		this.arrayRangos.add(info);
		
		info = new InfoDatosInt();
		info.setDescripcion("151 a 180");
		info.setCodigo(151);
		info.setCodigo2(180);
		this.arrayRangos.add(info);
		
		info = new InfoDatosInt();
		info.setDescripcion("181 a 360");
		info.setCodigo(181);
		info.setCodigo2(360);
		this.arrayRangos.add(info);
		
		info = new InfoDatosInt();
		info.setDescripcion(" > 360");
		info.setCodigo(361);
		info.setCodigo2(ConstantesBD.codigoNuncaValido);
		this.arrayRangos.add(info);
	}

	public String getFechaCorte() {
		return fechaCorte;
	}

	public void setFechaCorte(String fechaCorte) {
		this.fechaCorte = fechaCorte;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getCodigoCentroAtencion() {
		return centroAtencion.split(ConstantesBD.separadorSplit)[0];
	}
	
	public String getNombreCentroAtencion() {
		return centroAtencion.split(ConstantesBD.separadorSplit)[1];
	}	

	public String getTipoSalida() {
		return tipoSalida;
	}

	public void setTipoSalida(String tipoSalida) {
		this.tipoSalida = tipoSalida;
	}

	public ArrayList<DtoDatosFinanciacion> getArrayDatos() {
		return arrayDatos;
	}

	public void setArrayDatos(ArrayList<DtoDatosFinanciacion> arrayDatos) {
		this.arrayDatos = arrayDatos;
	}

	public ArrayList<InfoDatosInt> getArrayRangos() {
		return arrayRangos;
	}

	public void setArrayRangos(ArrayList<InfoDatosInt> arrayRangos) {
		this.arrayRangos = arrayRangos;
	}

	public ArrayList<InfoDatosDouble> getArrayTotalFilas() {
		return arrayTotalFilas;
	}

	public void setArrayTotalFilas(ArrayList<InfoDatosDouble> arrayTotalFilas) {
		this.arrayTotalFilas = arrayTotalFilas;
	}

	public ArrayList<InfoDatosDouble> getArrayTotalColumnas() {
		return arrayTotalColumnas;
	}

	public void setArrayTotalColumnas(ArrayList<InfoDatosDouble> arrayTotalColumnas) {
		this.arrayTotalColumnas = arrayTotalColumnas;
	}

	public String getCentroAtencion() {
		return centroAtencion;
	}

	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public String getRutaNombreArchivo() {
		return rutaNombreArchivo;
	}

	public void setRutaNombreArchivo(String rutaNombreArchivo) {
		this.rutaNombreArchivo = rutaNombreArchivo;
	}

	public String getRutaNombreArchivoPrivado() {
		return rutaNombreArchivoPrivado;
	}

	public void setRutaNombreArchivoPrivado(String rutaNombreArchivoPrivado) {
		this.rutaNombreArchivoPrivado = rutaNombreArchivoPrivado;
	}
}