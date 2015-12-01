package com.princetonsa.dto.interfaz;

import java.io.Serializable;
import java.util.ArrayList;

import util.ConstantesBD;

public class DtoTipoMovimiento implements Serializable
{
	/**
	 * 
	 * */
	public String codigo;
	
	/**
	 * 
	 * */
	public String descripcion;
	
	/**
	 * 
	 * */
	public boolean seleccionado;
	
	/**
	 * 
	 * */
	public ArrayList<DtoTipoMovimientoArchivo> archivos;
	
	/**
	 * 
	 * */
	public ArrayList<DtoTipoMovimientoArchivo> archivosIncon;
	
	
	public DtoTipoMovimiento()
	{
		reset();
	}
	
	public void reset()
	{
		this.codigo = "";
		this.descripcion = "";
		this.seleccionado = false;
		this.archivos = new ArrayList<DtoTipoMovimientoArchivo>();
		this.archivosIncon = new ArrayList<DtoTipoMovimientoArchivo>();
	}
	
	public int getPosArchivo(String indicador)
	{
		int resultado = ConstantesBD.codigoNuncaValido;
		
		for(int i=0; i<archivos.size(); i++)
		{
			if(archivos.get(i).getIndicadorArchivo().equals(indicador))
				return i;
		}
		
		return resultado;
	}
	
	public int getPosArchivoIncon(String indicador)
	{
		int resultado = ConstantesBD.codigoNuncaValido;
		
		for(int i=0; i<archivosIncon.size(); i++)
		{		
			if(archivosIncon.get(i).getIndicadorArchivo().equals(indicador))
				return i;
		}
			
		return resultado;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public boolean isSeleccionado() {
		return seleccionado;
	}

	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}

	public ArrayList<DtoTipoMovimientoArchivo> getArchivos() {
		return archivos;
	}

	public void setArchivos(ArrayList<DtoTipoMovimientoArchivo> archivos) {
		this.archivos = archivos;
	}

	public ArrayList<DtoTipoMovimientoArchivo> getArchivosIncon() {
		return archivosIncon;
	}

	public void setArchivosIncon(ArrayList<DtoTipoMovimientoArchivo> archivosIncon) {
		this.archivosIncon = archivosIncon;
	}

	
}