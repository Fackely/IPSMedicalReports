package com.princetonsa.dto.administracion;

import java.io.Serializable;

import util.ConstantesBD;

/**
 * Julio Hernández
 * @author axioma
 * Éste DTO es transparente para los detalles de vigencias concepto retencion tanto para los grupos de servicio, clase de inventario y conceptos facturas varias
 *
 */
public class DtoDetVigConRet implements Serializable
{
		private String consecutivoPk;
		private String detVigconRetencion;
		private String tipoElementoRetencion;
		private String baseMinima;
		private String porcentaje;
		private String fechaModifica;
		private String horaModifica;
		private String usuarioModifica;
		private String activo;
		private String fechaInactivacion;
		private String horaInactivacion;
		private String usuarioInactivacion;
		
		//Adicionales apra Servicio
		private String descServicio="";
		//Adicional para Clase inv
		private String nombreInv="";
		//Adicional para cfv
		private String descConcepto="";
		
		/**
		 * Constructor
		 */
		public DtoDetVigConRet()
		{
			this.reset();
		}
		
		private void reset() 
		{
			this.consecutivoPk="";
			this.detVigconRetencion="";
			this.tipoElementoRetencion="";
			this.baseMinima="";
			this.porcentaje="";
			this.fechaModifica="";
			this.horaModifica="";
			this.usuarioModifica="";
			this.activo="";
			this.fechaInactivacion="";
			this.horaInactivacion="";
			this.usuarioInactivacion="";
			this.descServicio="";
			this.nombreInv="";
			this.descConcepto="";
		}
		public String getConsecutivoPk() {
			return consecutivoPk;
		}

		public void setConsecutivoPk(String consecutivoPk) {
			this.consecutivoPk = consecutivoPk;
		}

		public String getDetVigconRetencion() {
			return detVigconRetencion;
		}

		public void setDetVigconRetencion(String detVigconRetencion) {
			this.detVigconRetencion = detVigconRetencion;
		}

		public String getTipoElementoRetencion() {
			return tipoElementoRetencion;
		}

		public void setTipoElementoRetencion(String tipoElementoRetencion) {
			this.tipoElementoRetencion = tipoElementoRetencion;
		}

		public String getBaseMinima() {
			return baseMinima;
		}

		public void setBaseMinima(String baseMinima) {
			this.baseMinima = baseMinima;
		}

		public String getPorcentaje() {
			return porcentaje;
		}

		public void setPorcentaje(String porcentaje) {
			this.porcentaje = porcentaje;
		}

		public String getFechaModifica() {
			return fechaModifica;
		}

		public void setFechaModifica(String fechaModifica) {
			this.fechaModifica = fechaModifica;
		}

		public String getHoraModifica() {
			return horaModifica;
		}

		public void setHoraModifica(String horaModifica) {
			this.horaModifica = horaModifica;
		}

		public String getUsuarioModifica() {
			return usuarioModifica;
		}

		public void setUsuarioModifica(String usuarioModifica) {
			this.usuarioModifica = usuarioModifica;
		}

		public String getActivo() {
			return activo;
		}

		public void setActivo(String activo) {
			this.activo = activo;
		}

		public String getFechaInactivacion() {
			return fechaInactivacion;
		}

		public void setFechaInactivacion(String fechaInactivacion) {
			this.fechaInactivacion = fechaInactivacion;
		}

		public String getHoraInactivacion() {
			return horaInactivacion;
		}

		public void setHoraInactivacion(String horaInactivacion) {
			this.horaInactivacion = horaInactivacion;
		}

		public String getUsuarioInactivacion() {
			return usuarioInactivacion;
		}

		public void setUsuarioInactivacion(String usuarioInactivacion) {
			this.usuarioInactivacion = usuarioInactivacion;
		}

		public String getDescServicio() {
			return descServicio;
		}

		public void setDescServicio(String descServicio) {
			this.descServicio = descServicio;
		}

		public String getNombreInv() {
			return nombreInv;
		}

		public void setNombreInv(String nombreInv) {
			this.nombreInv = nombreInv;
		}

		public String getDescConcepto() {
			return descConcepto;
		}

		public void setDescConcepto(String descConcepto) {
			this.descConcepto = descConcepto;
		}
}