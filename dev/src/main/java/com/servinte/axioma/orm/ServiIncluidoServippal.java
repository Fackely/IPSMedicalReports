package com.servinte.axioma.orm;

// Generated Apr 7, 2010 3:58:52 PM by Hibernate Tools 3.2.5.Beta

import java.util.HashSet;
import java.util.Set;

/**
 * ServiIncluidoServippal generated by hbm2java
 */
public class ServiIncluidoServippal implements java.io.Serializable {

	private int codigo;
	private CentrosCosto centrosCosto;
	private Servicios servicios;
	private ServiPpalincluidos serviPpalincluidos;
	private int cantidad;
	private Set artServiIncluidoses = new HashSet(0);

	public ServiIncluidoServippal() {
	}

	public ServiIncluidoServippal(int codigo, CentrosCosto centrosCosto,
			Servicios servicios, ServiPpalincluidos serviPpalincluidos,
			int cantidad) {
		this.codigo = codigo;
		this.centrosCosto = centrosCosto;
		this.servicios = servicios;
		this.serviPpalincluidos = serviPpalincluidos;
		this.cantidad = cantidad;
	}

	public ServiIncluidoServippal(int codigo, CentrosCosto centrosCosto,
			Servicios servicios, ServiPpalincluidos serviPpalincluidos,
			int cantidad, Set artServiIncluidoses) {
		this.codigo = codigo;
		this.centrosCosto = centrosCosto;
		this.servicios = servicios;
		this.serviPpalincluidos = serviPpalincluidos;
		this.cantidad = cantidad;
		this.artServiIncluidoses = artServiIncluidoses;
	}

	public int getCodigo() {
		return this.codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public CentrosCosto getCentrosCosto() {
		return this.centrosCosto;
	}

	public void setCentrosCosto(CentrosCosto centrosCosto) {
		this.centrosCosto = centrosCosto;
	}

	public Servicios getServicios() {
		return this.servicios;
	}

	public void setServicios(Servicios servicios) {
		this.servicios = servicios;
	}

	public ServiPpalincluidos getServiPpalincluidos() {
		return this.serviPpalincluidos;
	}

	public void setServiPpalincluidos(ServiPpalincluidos serviPpalincluidos) {
		this.serviPpalincluidos = serviPpalincluidos;
	}

	public int getCantidad() {
		return this.cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public Set getArtServiIncluidoses() {
		return this.artServiIncluidoses;
	}

	public void setArtServiIncluidoses(Set artServiIncluidoses) {
		this.artServiIncluidoses = artServiIncluidoses;
	}

}
