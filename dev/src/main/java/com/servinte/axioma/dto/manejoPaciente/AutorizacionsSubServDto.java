package com.servinte.axioma.dto.manejoPaciente;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @author davgommo
 * @version 1.0
 * @created 20-jun-2012 02:24:02 p.m.
 */

public class AutorizacionsSubServDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 232043721816801365L;

	
	private long consecutivo;
	String nombre;
	double cantidad;
	private double valorTarifaS;
	private double valorTarifaSF;
	private int codGrupClas;
	private int numSol;
	private String acronimo;
	private int diagCie;
	private String nombreDiag;
	private int codigo;
	
	

		
	public AutorizacionsSubServDto(){
		
	}
	/**
	 * @param consecutivo
	 * @param valorTarifaS
	 * @param cantidadS
	 */
	public AutorizacionsSubServDto(long consecutivo,String nombre,Integer cantidad, BigDecimal valorTarifaS, Integer cod) {
		
		this.consecutivo = consecutivo;
		this.valorTarifaS = valorTarifaS.doubleValue();
		this.cantidad=cantidad.doubleValue();
		this.nombre=nombre;
		this.valorTarifaSF=0;
		this.codGrupClas=cod.intValue();
	
	}
	public AutorizacionsSubServDto(long consecutivo,String nombre, int Cie, String diagnostico) {
		
		this.consecutivo = consecutivo;
		this.acronimo=nombre;
		this.diagCie=Cie;
		this.nombreDiag=diagnostico;
	
	}
	public AutorizacionsSubServDto(long consecutivo, int Cie) {
		
		this.consecutivo = consecutivo;
		this.codigo=Cie;

	
	}
	/**
	 * @return the valorTarifaS
	 */
	public double getValorTarifaS() {
		return valorTarifaS;
	}

	/**
	 * @param valorTarifaS the valorTarifaS to set
	 */
	public void setValorTarifaS(double valorTarifaS) {
		this.valorTarifaS = valorTarifaS;
	}



	/**
	 * @return the consecutivo
	 */
	public long getConsecutivo() {
		return consecutivo;
	}
	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(long consecutivo) {
		this.consecutivo = consecutivo;
	}
	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}
	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	/**
	 * @return the cantidad
	 */
	public double getCantidad() {
		return cantidad;
	}
	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}
	/**
	 * @return the valorTarifaSF
	 */
	public double getValorTarifaSF() {
		return valorTarifaSF;
	}
	/**
	 * @param valorTarifaSF the valorTarifaSF to set
	 */
	public void setValorTarifaSF(double valorTarifaSF) {
		this.valorTarifaSF = valorTarifaSF;
	}
	/**
	 * @return the codGrupClas
	 */
	public int getCodGrupClas() {
		return codGrupClas;
	}
	/**
	 * @param codGrupClas the codGrupClas to set
	 */
	public void setCodGrupClas(int codGrupClas) {
		this.codGrupClas = codGrupClas;
	}
	/**
	 * @return the numSol
	 */
	public int getNumSol() {
		return numSol;
	}
	/**
	 * @param numSol the numSol to set
	 */
	public void setNumSol(int numSol) {
		this.numSol = numSol;
	}
	/**
	 * @return the acronimo
	 */
	public String getAcronimo() {
		return acronimo;
	}
	/**
	 * @param acronimo the acronimo to set
	 */
	public void setAcronimo(String acronimo) {
		this.acronimo = acronimo;
	}
	/**
	 * @return the diagCie
	 */
	public int getDiagCie() {
		return diagCie;
	}
	/**
	 * @param diagCie the diagCie to set
	 */
	public void setDiagCie(int diagCie) {
		this.diagCie = diagCie;
	}
	/**
	 * @return the nombreDiag
	 */
	public String getNombreDiag() {
		return nombreDiag;
	}
	/**
	 * @param nombreDiag the nombreDiag to set
	 */
	public void setNombreDiag(String nombreDiag) {
		this.nombreDiag = nombreDiag;
	}
	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}
	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}



	
}